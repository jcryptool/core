package org.jcryptool.core.util.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;

public class VisualPluginHelp {

	/**
	 * 
	 * Generates a default action to be assigned to a title-and-description-composite.
	 * If an empty Optional is returned, the TitleAndDescriptionComposite does not display a help button.
	 * 
	 * @param parentOfTADComposite
	 * @return
	 */
	public static Optional<Consumer<TitleAndDescriptionComposite>> makeDefaultTADHelpAction(Composite parentOfTADComposite) {
		return Optional.of(VisualPluginHelp::onHelpButtonClickDefault);
	}
	
	public static void onHelpButtonClickDefault(TitleAndDescriptionComposite composite) {
		try {
			runContextHelpAction();
		} catch (ExecutionException | NotHandledException e) {
			LogUtil.logError(e, true);
		}
	}
	
	public static void runContextHelpAction() throws ExecutionException, NotHandledException {
		Command cmd = getCommand("org.eclipse.ui.help.dynamicHelp");
		cmd.execute(new ExecutionEvent());
	}

	private static Command getCommand(String id) {
		return getAllCommands().stream().filter(c -> c.getId().equals(id)).findFirst().get();
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
	private static List<Command> getAllCommands() {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
		org.eclipse.ui.IWorkbenchWindow wbwin = PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		CommandManager commandManager = wbwin.getService(CommandManager.class);
		return Arrays.asList(commandManager.getAllCommands());
	}

}
