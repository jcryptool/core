"""Recursively search for links in HTML files and check if they are reachable.

This is a utility python script to check if links in our documentation are healthy
or we are linking to gone websites.

It requires Python 3.7 or higher and has no external dependencies.
"""
from argparse import ArgumentParser, RawDescriptionHelpFormatter
import http.client
import multiprocessing
import os
import re
import socket
import sys
from abc import ABC
from dataclasses import dataclass, field
from enum import Enum
from html.parser import HTMLParser
from multiprocessing import Manager, Queue, Process
from pathlib import Path
from typing import Optional, Union, List, Dict, Set
from urllib.error import URLError
from urllib.parse import urlparse
from urllib.request import Request, urlopen


class DefaultSettings(Enum):
    FILTER_PATTERN = "nl/**/"
    EXCLUDE_PROJECTS = "org.jcryptool.core.nl, org.jcryptool.games.sudoku"
    USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 " \
                 "(KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36"
    RETRY_COUNT = 3
    TIMEOUT = 5
    IGNORE_FILE = "ignored_links.txt"


class Status(Enum):
    """Link target status"""
    PENDING = 0
    FAILED = 1
    OK = 2
    TIMEOUT = 3
    PROCESSING = 4
    IGNORED = 5


class HTMLLinkParser(HTMLParser, ABC):
    """Simple HTML parser which can search for <a href="some-link" kind of links."""

    def __init__(self):
        super().__init__()
        self.hrefs = []

    def handle_starttag(self, tag, attrs):
        if tag == 'a' and attrs:
            for attribute, value in attrs:
                if attribute == 'href':
                    self.hrefs.append(value)

    def search_hrefs(self, data: str) -> List[str]:
        """Search for all <a href> occurrences in the given html data."""
        self.feed(data)
        tmp_refs = self.hrefs
        self.hrefs = []
        return tmp_refs


@dataclass(unsafe_hash=True)
class PingContext:
    """Data holding class for link ping requests."""
    url: str = field(hash=True)
    status: Status = field(hash=False)
    timeout: int = field(hash=True)
    http_code: Optional[int] = field(default=None, hash=False)
    error_reason: Optional[str] = field(default=None, hash=False)
    retry_count: int = field(default=0, hash=True)
    files: Set[Union[Path, str]] = field(default=None, hash=False)
    is_ignored: bool = field(default=False, hash=False)
    ignore_pattern: str = field(default=None, hash=False)

    def __str__(self):
        code_extension = ""
        error_extension = ""
        if self.error_reason:
            error_extension = f" (Reason: {self.error_reason})"
        if self.http_code:
            code_extension = f" HTTP: {str(self.http_code)}"
        return f"{self.url:<100}--> {self.status}{code_extension}{error_extension}"


def search_links(html: str) -> List[str]:
    """Search for all <a href> occurrences in the given html data."""
    return [link.replace(" ", "%20") for link in HTMLLinkParser().search_hrefs(html)]


def send_request(context: PingContext) -> PingContext:
    """Send a request and save the return status code and possible errors."""
    req = Request(context.url, headers={"User-Agent": DefaultSettings.USER_AGENT.value})
    try:
        _response = urlopen(req, timeout=context.timeout)
        context.status = Status.OK
        context.http_code = _response.getcode()
        context.error_reason = None
        return context
    except URLError as e:
        if hasattr(e, 'reason'):
            context.error_reason = e.reason
        elif hasattr(e, 'code'):
            context.http_code = e.code
        if "urlopen error timed out" in str(e):
            context.status = Status.TIMEOUT
            context.retry_count += 1
            context.timeout *= 2
        else:
            context.status = Status.FAILED
        return context
    except socket.timeout as e:
        context.status = Status.TIMEOUT
        context.retry_count += 1
        context.timeout *= 2
        context.error_reason = str(e)
        return context


def is_legit_external_link(url: str) -> bool:
    """(Heuristically) check if a link is a valid external link target.

     In many cases it's just something relative/internal/invalid.
     """
    try:
        _hostname = urlparse(url).hostname
        if _hostname:
            return re.search(global_dot_pattern, _hostname) is not None
        else:
            return False
    except ValueError as e:
        print_verbose(2, f'{url} is not a valid URL because {e}')
        return False
    except http.client.InvalidURL as e:
        print_verbose(2, f'{url} is not a valid URL because {e}')
        return False


def check_and_put(
        output: Dict[str, PingContext],
        links: List[str],
        current_file: Path,
        target_path: Path
) -> None:
    for link in links:
        if is_legit_external_link(link):
            _rel_path = current_file.relative_to(target_path)
            print_verbose(2, f"Collected link {link} from {_rel_path}")
            if link not in output:
                output[link] = PingContext(link,
                                           Status.PENDING,
                                           timeout=DefaultSettings.TIMEOUT.value,
                                           files={str(_rel_path)}
                                           )
            else:
                output[link].files.add(str(_rel_path))


def in_exclude(file: Path, exclude_list: List[str]) -> bool:
    for exclude_pattern in exclude_list:
        if exclude_pattern in str(file):
            print_verbose(2, f"Excluding {file} because it matches the exclude "
                             f"'{exclude_pattern}'")
            return True
    return False


def collect_all_links(
        target_path: Union[Path, List[Path]], filter_prefix: str, exclude: List[str]
) -> List[PingContext]:
    """Search all html files in a given directory path and collect links."""
    result = {}
    file_filter = f"{filter_prefix}*.html"
    if not isinstance(target_path, list):
        target_path = [target_path]
    for current_target_path in target_path:
        for file in current_target_path.rglob(file_filter):

            if in_exclude(file, exclude):
                continue

            with open(file, 'r') as file_descriptor:
                data = file_descriptor.read()
            links = search_links(data)
            check_and_put(result, links, file, current_target_path)
    return list(result.values())


def fill_waiting_queue(collected_links: List, mp_queue: Queue):
    """Fill the multiprocessing queue from a list of items."""
    for target in collected_links:
        mp_queue.put(target)


def query(
        targets: Queue,
        retry_count: int,
        successful_targets: List[PingContext],
        failed_targets: List[PingContext],
        ignored_targets: List[PingContext],
) -> None:
    """Query a target link and check if it's reachable or not."""
    while not targets.empty():
        target = targets.get()
        if target.status in (Status.PENDING, Status.TIMEOUT):
            print_verbose(2, f"[{os.getpid()}] Working on {target.url}")
            target.status = Status.PROCESSING
            target = send_request(target)
            if target.is_ignored and target.status != Status.OK:
                if target.ignore_pattern in str(target.error_reason):
                    target.status = Status.IGNORED
                    msg = f"[{os.getpid()}] Ignored '{target.url}' after it had an " \
                          f"error {target.error_reason}."
                    print_verbose(2, msg)
                    ignored_targets.append(target)
                    continue
                else:
                    msg = f"[{os.getpid()}] Link '{target.url}' on ignore list " \
                          f"returned with an error but the pattern did not match: " \
                          f"'{target.ignore_pattern}' not in '{target.error_reason}'"
                    print_verbose(2, msg)

            if target.status == Status.TIMEOUT and target.retry_count <= retry_count:
                print_verbose(2, f"[{os.getpid()}] Timeout on {target.url}, "
                                 f"retry {target.retry_count}/{retry_count}")
                target.status = Status.PENDING
                targets.put(target)
            elif target.status == Status.OK:
                successful_targets.append(target)
            else:
                failed_targets.append(target)
        else:
            if target.status == Status.OK:
                successful_targets.append(target)
            else:
                failed_targets.append(target)


def print_verbose(verbosity_level: int, message: str, *args, **kwargs) -> None:
    if verbosity_level <= global_verbosity_level:
        print(message, *args, **kwargs)


def set_properties(
        target_links: List[PingContext],
        timeout: int, ignore_links: Optional[Dict[str, str]]
) -> List[PingContext]:
    for target in target_links:
        if ignore_links and target.url in ignore_links:
            target.is_ignored = True
            target.ignore_pattern = ignore_links[target.url]
            print_verbose(2, f"Ignoring {target.url}")
        target.timeout = timeout
    return target_links


def load_ignore_list(path: Path) -> Dict[str, str]:
    output = dict()
    with open(path, "r") as fp:
        lines = fp.readlines()
    for line in lines:
        line = line.strip()
        if line.startswith('#'):
            continue
        tokens = line.split(" ")
        length = len(tokens)
        if length == 1:
            url = tokens[0]
            reason_filter = ""
        elif length >= 2:
            url = tokens[0]
            reason_filter = " ".join(tokens[1:]).strip()
        else:
            print_verbose(0, f"Could not parse ignore entry '{line}'", file=sys.stderr)
            continue
        output[url] = reason_filter
    print_verbose(1, f"Loaded {len(output)} ignore entries from '{str(path)}'")
    return output


description = """HTML link availability checker.

Recursively look up HTML files in a directory, extract all links (href) and
send a request to them. Produces reports about available / dead links."""

epilog = f"""Examples:

Returns:
0 (success) if all links were reachable or 1 (error) if at least one was unreachable

python3 {Path(__file__).name} git/core git/crypto
    Just check if any links are unavailable (check return code) in both core and crypto.
    Recommended way to call for batch jobs.

python3 {Path(__file__).name} --summary --results-dead git/core git/crypto
   Search in git/core and crypto, print the summary and unreachable links to stdout
   
python3 {Path(__file__).name} -v --results --result-paths -rf out.txt git/crypto
   Search in git/crypto, enable verbose output, print results with paths to file out.txt

python3 {Path(__file__).name} -vv --results --summary user/git/crypto
   Search in user/git/crypto, enable debug output, print summary and results to stdout

Hint:
In most cases you would want the option set --summary --results-dead --result-paths.
This enables a summary, the dead links and in which files these links are found.
 """


def parse_arguments():
    parser = ArgumentParser(description=description,
                            epilog=epilog,
                            formatter_class=RawDescriptionHelpFormatter)
    parser.add_argument("target_dir",
                        help="Directory to recursively look for HTML links",
                        nargs='+')
    parser.add_argument("-s", "--summary",
                        help="Print a (short) summary at the end", action="store_true")
    parser.add_argument("-c", "--retry-count",
                        help="Retry counter if a connection times out",
                        type=int, default=DefaultSettings.RETRY_COUNT.value)
    parser.add_argument("-t", "--timeout",
                        help="Time to wait for an answer per request",
                        type=int, default=DefaultSettings.TIMEOUT.value)
    parser.add_argument("-p", "--processes",
                        help="Processes to start (defaults to logical CPU cores or "
                             "at least 4 processes)",
                        type=int, default=max(multiprocessing.cpu_count(), 4))

    result_group = parser.add_mutually_exclusive_group()
    result_group.add_argument("-r", "--results",
                              help="Print the results of all target links in a "
                                   "verbose way",
                              action="store_true")
    result_group.add_argument("-rr", "--results-reachable",
                              help="Print the results of only reachable links in a "
                                   "verbose way",
                              action="store_true")
    result_group.add_argument("-rd", "--results-dead",
                              help="Print the results of only dead/unreachable links "
                                   "in a verbose way",
                              action="store_true")
    parser.add_argument("-rp", "--result-paths",
                        help="Also print corresponding file paths in the result output",
                        action="store_true")
    parser.add_argument("-rf", "--result-file", help="Redirect result output to file")
    parser.add_argument(
        "-f",
        "--filter-pattern",
        help="A filter pattern to apply to the html file search The term '*.html' is "
             "automatically added. Defaults to "
             f"'{DefaultSettings.FILTER_PATTERN.value}'",
        default=DefaultSettings.FILTER_PATTERN.value,
    )
    parser.add_argument(
        "-e",
        "--exclude",
        help="Projects (directories) to exclude from search as comma separated list. "
             f"Defaults to '{DefaultSettings.EXCLUDE_PROJECTS.value}'.",
        default=DefaultSettings.EXCLUDE_PROJECTS.value,
    )
    parser.add_argument(
        "-i",
        "--ignore",
        help="Path to a file with URls to ignore. The format is 'URL reason(optional)' "
             "with one entry per line. Note that the link will be queried everytime, "
             "but if it fails and is on the ignore list, it will not count toward the "
             "final result. The reason can be a textual representation of a "
             "http error of urllib, e.g. 'forbidden', 'timeout' or "
             "'certificate verify failed'. "
             f"Defaults to '{DefaultSettings.IGNORE_FILE.value}'.",
        default=DefaultSettings.IGNORE_FILE.value,
    )

    verbose_group = parser.add_mutually_exclusive_group()
    verbose_group.add_argument("-v", help="Enable verbose output", action="store_true")
    verbose_group.add_argument("-vv",
                               help="Enable more verbose output", action="store_true")
    return parser.parse_args()


def print_files(files: List[str], file=None) -> None:
    if files:
        for _file in files[:-1]:
            print(f"\t╠ {_file}", file=file)
        print(f"\t╚ {files[-1]}", file=file)


global_verbosity_level = 0
global_dot_pattern = re.compile(r'\.((?!html?).)')


def main():
    global global_verbosity_level
    args = parse_arguments()

    # Set arguments
    retry_count = args.retry_count
    timeout = args.timeout
    target_dir = [Path(target_dir) for target_dir in args.target_dir]
    process_count = args.processes
    print_success = args.results or args.results_reachable
    print_failed = args.results or args.results_dead
    print_paths = args.result_paths
    print_summary = args.summary
    if args.result_file:
        print_result_target = args.result_file
        print_result_use_file = True
    else:
        print_result_target = ""
        print_result_use_file = False
    global_verbosity_level = 0
    global_verbosity_level += 1 if args.v else 0
    global_verbosity_level += 2 if args.vv else 0
    exclude_projects = [string.strip() for string in args.exclude.split(",")]
    ignore_link_file = Path(args.ignore)

    if not ignore_link_file.exists():
        print_verbose(1, f"Specified --ignore file at '{str(ignore_link_file)}' could "
                         f"not be found. Ignoring it...", file=sys.stderr)
        target_ignore_links = None
    else:
        target_ignore_links = load_ignore_list(ignore_link_file)

    # Initialize multiprocessing data structures
    mp_manager = Manager()
    waiting_queue = mp_manager.Queue()
    successful_links = mp_manager.list()
    failed_links = mp_manager.list()
    ignored_links = mp_manager.list()

    # Search for links in the target directory and set their timeout
    target_links = collect_all_links(target_dir, args.filter_pattern, exclude_projects)
    target_links = set_properties(target_links, timeout, target_ignore_links)

    print_verbose(1, f"Collected {len(target_links)} links "
                     f"from *.html files in {', '.join([str(d) for d in target_dir])}")
    fill_waiting_queue(target_links, waiting_queue)
    print_verbose(1, f"Filled query queue, starting {process_count} workers")

    # Start the processes to query the links for availability.
    processes = []
    for _ in range(process_count):
        _process = Process(
            target=query,
            args=(
                waiting_queue,
                retry_count,
                successful_links,
                failed_links,
                ignored_links
            )
        )
        processes.append(_process)
        _process.start()

    for _process in processes:
        _process.join()
    print_verbose(1, f"Finished query job, all workers returned.")

    # From here on is output only. There are two major types of output,
    # the detailed results and the summary. The detailed results are
    # redirect-able to a file, the summary is not.
    if print_result_use_file:
        print_result_handler = open(print_result_target, 'w')
    else:
        print_result_handler = sys.stdout

    try:
        if print_success:
            print("\nResults - Reachable Links", file=print_result_handler)
            print("=========================", file=print_result_handler)
            for item in successful_links:
                print(item, file=print_result_handler)
                if print_paths:
                    print_files(list(item.files), file=print_result_handler)

        if print_failed:
            print("\nResults - Dead Links", file=print_result_handler)
            print("====================", file=print_result_handler)
            for item in failed_links:
                print(item, file=print_result_handler)
                if print_paths:
                    print_files(list(item.files), file=print_result_handler)
    finally:
        if print_result_use_file:
            print_result_handler.close()

    if print_summary:
        _len_successful = len(successful_links)
        _len_failed = len(failed_links)
        _len_ignored = len(ignored_links)
        _len_all = _len_successful + _len_failed + _len_ignored
        if _len_all > 1:
            success_rate = _len_successful / _len_all
            fail_rate = _len_failed / _len_all
            ignore_rate = _len_ignored / _len_all
        else:
            success_rate = 0
            fail_rate = 0
            ignore_rate = 0
        summary_msg = "Summary - Tested unique external links: "
        print(f"\n{summary_msg}{len(target_links)}")
        # Print as many '=' characters as the line above has.
        print(f"{'=' * (len(summary_msg) + len(str(len(target_links))))}")
        print(f"\tReachable     {_len_successful:>5} ({success_rate:>6.2%})")
        print(f"\tNot Reachable {_len_failed:>5} ({fail_rate:>6.2%})")
        if _len_ignored > 0:
            print(f"\tIgnored       {_len_ignored:>5} ({ignore_rate:>6.2%})")

    exit_code = 1 if failed_links else 0
    print_verbose(1, f"Done.")
    sys.exit(exit_code)


if __name__ == '__main__':
    main()
