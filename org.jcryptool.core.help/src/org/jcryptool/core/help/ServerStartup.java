package org.jcryptool.core.help;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.IWorkbenchHelpSystem;

public class ServerStartup implements IStartup {

	public static class StartupParsed {
		public boolean activated = false;
		public Optional<String> startupAddress = Optional.empty();
		
		public StartupParsed(boolean activated, Optional<String> startupAddress) {
			super();
			this.activated = activated;
			this.startupAddress = startupAddress;
		}

		public static StartupParsed parse() {
			
			String[] cmdlineargs = Platform.getCommandLineArgs();

			Optional<String> startingPage = Optional.empty();
			boolean pluginActivated = false;
			for (int i = 0; i < cmdlineargs.length; i++) {
				String currentArg = cmdlineargs[i];
				if(currentArg.equals("-StartupHelp")) {
					pluginActivated=true;
					if (cmdlineargs.length -1 == i) {
						// the help is to be started up with the usual starting page
						startingPage = Optional.empty();
					} else {
						String nextArg = cmdlineargs[i+1];
						startingPage = Optional.of(nextArg);
					}
				}
			}
			
			return new StartupParsed(pluginActivated, startingPage);
		}
		
		
	}
	public static String readStringFromURL(String requestURL) throws IOException
	{
	    try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
	            StandardCharsets.UTF_8.toString()))
	    {
	        scanner.useDelimiter("\\A");
	        return scanner.hasNext() ? scanner.next() : "";
	    }
	}	
	public List<String> getIdsForVersion(String version) {
		var url = "https://cryptool.org/jct/notifications/" + version + ".ids";
		try {
			System.out.println("DBG: Checking for new notifications from " + url);
			var idscontent = readStringFromURL(url);
			List<String> lines = idscontent.lines().filter(l -> ! l.isBlank()).collect(Collectors.toList());
			lines.forEach(l -> System.out.println("DBG: read id " + l));
			return lines;
		} catch (IOException e) {
			System.out.println("Error in getting ids from: " + url);
			return new LinkedList<String>();
		}
	}
	public String getURLForId(String id) {
		return "https://cryptool.org/jct/notifications/messages/" + id + ".html";
	}
	public String getHTMLForId(String id) {
		try {
			var content = readStringFromURL(getURLForId(id));
			return content;
		} catch (IOException e) {
			return "<div>Failed to fetch message from " + "https://cryptool.org/jct/notifications/messages/" + id + ".html" + "</div>";
		}
	}
	
	public void notificationChecker() {
// 		var version = System.getProperty("")
		var version = "Weekly-Build--1.0.8.24-20221010";
		var idsForVersion = getIdsForVersion(version);
		var idsForAll = getIdsForVersion("all");
		List<String> seenMessages = new LinkedList<String>();
		List<String> unseenMessagesBeforeFilter = new LinkedList<String>();
		for(String versionId : idsForVersion) { unseenMessagesBeforeFilter.add(versionId); }
		for(String allId : idsForAll) { unseenMessagesBeforeFilter.add(allId); }
		List<String> unseenMessages = unseenMessagesBeforeFilter.stream().filter(m -> ! seenMessages.contains(m)).distinct().collect(Collectors.toList());
//		var unseenMessageContents = unseenMessagesBeforeFilter.stream().map(id -> getHTMLForId(id)).collect(Collectors.toList());
		System.out.println("DBG: unseen: " + unseenMessages.size());
		if (unseenMessages.size() > 0) {
			var delayed = new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							var url = getURLForId(unseenMessages.get(0));
							System.out.println("DBG: opening shell: " + url);
							var display = Display.getDefault();
							var shell = new Shell(display);
							shell.setLayout(new GridLayout());
							var comp = new Composite(shell, SWT.NONE);
							comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
							comp.setLayout(new GridLayout());
							var viewer = new Browser(comp, SWT.NONE);
							viewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
							viewer.setUrl(url);
//							var w = display.getBounds().width / 2;
//							var h = display.getBounds().height / 2;
//							var x = display.getBounds().width / 4;
//							var y = display.getBounds().height / 4;
//							shell.setBounds(x, y, w, h);
							shell.setSize(800,600);
							shell.setText("A notification for users of JCrypTool version " + version);
							shell.layout();
// 							shell.pack();
							shell.open();
							while (!shell.isDisposed()) {
								if (! display.isDisposed()) {
							        if (!display.readAndDispatch()) {
										display.sleep();
							        }
								}
							}
						}
					});
				}
			};
			delayed.run();
		}
		
	}
	@Override
	public void earlyStartup() {
		StartupParsed helpAtStartupParsed = StartupParsed.parse();

// 		notificationChecker(); // TODO: reactivate
		if (helpAtStartupParsed.activated) {
			Display display = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell().getDisplay();
			display.syncExec(new Runnable() {
				@Override
				public void run() {
 					final IWorkbenchHelpSystem helpSystem = PlatformUI.getWorkbench().getHelpSystem();
					if (helpAtStartupParsed.startupAddress.isPresent()) {
  						helpSystem.displayHelpResource(helpAtStartupParsed.startupAddress.get());
					} else {
 						helpSystem.displayHelp();
					}
				}
			});
		} else {
			return;
		}
		
		

//		Display display = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell().getDisplay();
// 		oldStartup();
	}

	private void oldStartup() { // how to find and execute a command by string, butBAAAAAAD :D
		try {
			Display display = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getShell().getDisplay();
			display.syncExec(new Runnable() {
				
				@Override
				public void run() {
					String helpUICmdId = "org.eclipse.ui.help.displayHelp";
					try {
						getAllCommands().stream().filter(c -> c.getId().equals(helpUICmdId)).findFirst().get().execute(new ExecutionEvent());
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NotHandledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(
					"Could not start JCTJS server. The help system may malfunction. Please report this bug: http://github.com/jcryptool/core/issues. Thanks.");
		}
	}

	private void printAllCommands() {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		org.eclipse.ui.IWorkbenchWindow wbwin = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		CommandManager commandManager = wbwin.getService(CommandManager.class);
		for (Command c : commandManager.getAllCommands()) {
			try {
				System.out.println(String.format("%s: %s", c.getName(), c.getId()));
			} catch (NotDefinedException e) {
				e.printStackTrace();
			}
		}
	}
	private List<Command> getAllCommands() {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		org.eclipse.ui.IWorkbenchWindow wbwin = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		CommandManager commandManager = wbwin.getService(CommandManager.class);
		return Arrays.asList(commandManager.getAllCommands());
	}
	// does not work, because getting the port without having started the help system does not seem to work...
	private void startAndOpenServerDirectly_Defunct() throws Exception {
//		System.out.println("Starting server...");
		JCTJS_Server.getInstance().start();
//		System.out.println("Started server on port " + JCTJS_Server.getInstance().getPort());
		java.awt.Desktop.getDesktop().browse(URI.create((JCTJS_Server.getInstance().makeHelpsystemUrlStringFor("index.html"))));
//		System.out.println(URI.create((JCTJS_Server.getInstance().makeHelpsystemUrlStringFor("index.html"))));
	}

}
