// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.ui.eclipseconsole;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.cli.ParseException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.jcryptool.commands.core.evaluator.CommandEvaluator;
import org.jcryptool.commands.ui.CommandsUIPlugin;
import org.jcryptool.commands.ui.eclipseconsole.console.IOConsolePromptShell;
import org.jcryptool.core.logging.utils.LogUtil;

public class CommandsUiStartup implements IStartup {

	private static final String PROMPT = Messages.CommandsUiStartup_prompt;

	public void earlyStartup() {
		final IOConsole ioConsole = new IOConsole(Messages.CommandsUiStartup_consolename, null);
		ioConsole.setConsoleWidth(0);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { ioConsole });
		CommandsUIPlugin.getDefault().setIoConsole(ioConsole);

		final CommandEvaluator evaluator = new CommandEvaluator();
		final IOConsolePromptShell monitor = new IOConsolePromptShell(ioConsole) {
			@Override
			protected void sendPrompt(final IOConsoleOutputStream o)
					throws IOException {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						Color prevColor = o.getColor();
						int prevStyle = o.getFontStyle();
						o.setColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
						try {
							o.write(PROMPT);
						} catch (IOException e) {
							LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
						}
						o.setColor(prevColor);
						o.setFontStyle(prevStyle);
					}
				});
			}
			@Override
			protected void initializeConsole(IOConsoleOutputStream o) {
				super.initializeConsole(o);
				try {
					o.write(Messages.CommandsUiStartup_welcome +
							Messages.CommandsUiStartup_welcome_tip);
				} catch (IOException e) {
					LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
				}
			}
		};
		Observer monitorObserver = new Observer() {
			public void update(Observable o, Object arg) {
				String line = monitor.getLine();

				String result = Messages.CommandsUiStartup_eval_error;
				try {
					result = evaluator.evaluate(line).getResult();
				} catch (ParseException e) {
					LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
				}

				IOConsoleOutputStream outStream = ioConsole.newOutputStream();
				try {
					outStream.write(result + "\n\n"); //$NON-NLS-1$
				} catch (IOException e) {
					LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
				}

				try {
					outStream.close();
				} catch (IOException e) {
					LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
				}
			}
		};

		monitor.addObserver(monitorObserver);
		monitor.startMonitoring();
	}

}
