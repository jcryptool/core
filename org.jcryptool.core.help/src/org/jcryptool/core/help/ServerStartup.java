package org.jcryptool.core.help;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
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
	public static String getURLForId(String id, Optional<String> lang) {
		return "https://cryptool.org/jct/notifications/messages/" + id + lang.map(l -> "." + l).orElseGet(() -> "") + ".html";
	}
	public static Optional<String> getContentForURL(String url) {
		try {
			var content = readStringFromURL(url);
			return Optional.of(content);
		} catch (IOException e) {
			return Optional.empty();
		}
	}
	
	public List<String> getSeenMessages() {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path("notifications_seen.txt"));
		if(! file.exists()) {
			return new LinkedList<String>();
		}
		byte[] content;
		try {
			var stream = file.getContents();
			content = stream.readAllBytes();
			stream.close();
		} catch (IOException | CoreException e) {
			e.printStackTrace();
			return new LinkedList<String>();
		} 
		List<String> contentLines = new String(content, StandardCharsets.UTF_8).lines().filter(l -> ! l.isBlank()).collect(Collectors.toList());
		return contentLines;
	}
	public void writeSeenMessages(List<String> seenMessages) {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path("notifications_seen.txt"));
		byte[] outBytes = seenMessages.stream().collect(Collectors.joining("\n")).getBytes(StandardCharsets.UTF_8);
		try {
			file.setContents(new ByteArrayInputStream(outBytes), false, false, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public static class Notification {
		public Notification(String id, String channel, Optional<String> enContent, Optional<String> deContent,
				Optional<String> fallbackContent, boolean critical) {
			super();
			this.id = id;
			this.channel = channel;
			this.enContent = enContent;
			this.deContent = deContent;
			this.fallbackContent = fallbackContent;
			this.critical = critical;
		}
		public String id;
		public String channel; // "all" or the version
		public Optional<String> enContent;
		public Optional<String> deContent;
		public Optional<String> fallbackContent;
		public Date date = new Date(2023, 01, 17);
		public boolean critical = false;
	}
	public static int compareNotification(Notification n1, Notification n2) {
		if(n1 == n2) return 0;
		if(n1.critical == n2.critical) {
			return n1.date.compareTo(n2.date);
		}
		if(n1.critical) {
			return -1;
		}
		return 1;
	}
	public static Optional<Notification> retrieveNotification(String id, String channel) {
		var enUrl = getURLForId(id, Optional.of("en")); 
		var deUrl = getURLForId(id, Optional.of("de"));
		var fallbackUrl = getURLForId(id, Optional.empty());
		var enContent = getContentForURL(enUrl);
		var deContent = getContentForURL(deUrl);
		var fallbackContent = getContentForURL(fallbackUrl);
		if(! enContent.isPresent() && ! deContent.isPresent() && ! fallbackContent.isPresent()) {
			return Optional.empty();
		}
		return Optional.of( new Notification(id,  channel, enContent, deContent, fallbackContent, false) );
	}
	public void notificationChecker() {
// 		var version = System.getProperty("")
    	var v1 = Platform.getProduct().getDefiningBundle().getVersion();
    	var locale = Locale.getDefault().getLanguage();
    	System.out.println(String.format("DBG: v1=%s, locale=%s", v1, locale));
    	// output: DBG: v1=1.0.8.24-20221010, locale=en
		
		
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
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							var unseenNotificationsForVersion = unseenMessages.stream().map(id -> retrieveNotification(id, version)).flatMap(retrieved -> retrieved.stream()).collect(Collectors.toList()); 
							var unseenNotificationsForAll = unseenMessages.stream().map(id -> retrieveNotification(id, "all")).flatMap(retrieved -> retrieved.stream()).collect(Collectors.toList()); 
							var unseenNotifications = new LinkedList<Notification>();
							unseenNotifications.sort(new Comparator<Notification>() {
								@Override
								public int compare(Notification o1, Notification o2) {
									return compare(o1, o2);
								}
							});
							var display = Display.getDefault();
							var shell = new Shell(display);
							shell.setLayout(new GridLayout());
							var comp = new Composite(shell, SWT.NONE);
							comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
							comp.setLayout(new GridLayout());
							if(! unseenNotifications.isEmpty()) {
								var notificationComposite = new NotificationDisplayComposite(comp);
								notificationComposite.setNotifications(unseenNotifications);
								notificationComposite.setNotificationIdx(0);
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
	//							while (!shell.isDisposed()) {
	//								if (! display.isDisposed()) {
	//							        if (!display.readAndDispatch()) {
	//										display.sleep();
	//							        }
	//								}
	//							}
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

 		notificationChecker(); // TODO: reactivate

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
