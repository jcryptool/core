package org.jcryptool.core.help;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
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
	
	@Override
	public void earlyStartup() {
    System.out.println("Hello World 1");
		StartupParsed helpAtStartupParsed = StartupParsed.parse();

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
