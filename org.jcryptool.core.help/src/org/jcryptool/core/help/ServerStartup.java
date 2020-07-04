package org.jcryptool.core.help;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class ServerStartup implements IStartup {

	@Override
	public void earlyStartup() {
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

			
//			System.out.println("Starting server...");
//			JCTJS_Server.getInstance().start();
//			System.out.println("Started server on port " + JCTJS_Server.getInstance().getPort());
//			java.awt.Desktop.getDesktop().browse(URI.create((JCTJS_Server.getInstance().makeHelpsystemUrlStringFor("index.html"))));
//			System.out.println(URI.create((JCTJS_Server.getInstance().makeHelpsystemUrlStringFor("index.html"))));


// 			printAllCommands();

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
		System.out.println(commandManager.getAllCommands().length);
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
		System.out.println(commandManager.getAllCommands().length);
		return Arrays.asList(commandManager.getAllCommands());
	}

}
