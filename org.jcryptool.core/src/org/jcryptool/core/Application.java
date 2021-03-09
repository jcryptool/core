// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.osgi.service.environment.EnvironmentInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.jcryptool.core.logging.utils.LogUtil;



/**
 * This class controls all aspects of the application's execution.
 * 
 * @author Dominik Schadow
 * @version 1.0.0
 */
/**
 * @author snuc
 *
 */
/**
 * @author snuc
 *
 */
public class Application implements IApplication {

	/**
	 * If this field is set to something else than null, the application will consider 
	 * the exit code {@link IApplication#EXIT_RESTART} as {@link IApplication#EXIT_RELAUNCH}
	 * and set the exitdata property accordingly. This is e.g. used to filter out language 
	 * switches a.k.a. `-nl {language}`. Since other important switches like `-data {workspace}` may
	 * be present, we can't throw out every command line parameter — hence this filtering approach.
	 */
    private static Function<List<String>, List<String>> restart_cmdlinefilter = null;

	/*
     * (non-Javadoc)
     * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
     */
    @Override
	public Object start(IApplicationContext context) throws Exception {
        try {
        	// Set English as default language if the operating system language is neither German, English
        	System.setProperty("file.encoding", "UTF-8");
        	if (!Locale.getDefault().getLanguage().equals("de") &&
        			!Locale.getDefault().getLanguage().equals("en")) {

				Shell shell = null;
        		try {
        			IWorkbench wb = PlatformUI.getWorkbench();
					if (wb != null) {
						var windows = wb.getWorkbenchWindows();
						if (windows.length > 0) {
							shell = windows[0].getShell();
						}
					}
        		} catch (Exception e) {
					// do nothing, this is expected...
				}
				if (shell == null) {
					shell = new Shell();
				}
				LanguageChooser chooser = new LanguageChooser(shell);
				int retcode = chooser.open(); // actually, we have to restart, so we ignore the return code. Also, there is no cancel button.

				Function<List<String>, List<String>> inifileFilter = createLanguageRewriteInifileFilter(chooser.nl);
				applyFilterToInifile(inifileFilter);

				// this filters the cmdline arguments so that they don't change the language back
				Function<List<String>, List<String>> cmdlineFilter = createCmdlineRewriteRemoveLanguagespecFilter();
				filterCommandlineArgsForRelaunch(context, cmdlineFilter);
				// this is a low-level restart (can't use workbench here)
				return IApplication.EXIT_RELAUNCH;
        	}
        } catch (Exception e) {
            LogUtil.logError(CorePlugin.PLUGIN_ID, e);
        }
        
        return startApplication(context);
    }
    
	/*
     * (non-Javadoc)
     * @see org.eclipse.equinox.app.IApplication#stop()
     */
    @Override
	public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return;
        }
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {
            @Override
			public void run() {
                if (!display.isDisposed()) {
                    workbench.close();
                }
            }
        });
    }



    /**
     * Starts the GUI and controls the application's return code behavior.
     * @param context 
     * @return the exit code; see e.g. {@link IApplication#EXIT_RELAUNCH}
     */
    private Object startApplication(IApplicationContext context) {
        Display display = PlatformUI.createDisplay();
        // the application return code behavior
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
            	if (restart_cmdlinefilter != null) {
            		filterCommandlineArgsForRelaunch(context, restart_cmdlinefilter);
            		return IApplication.EXIT_RELAUNCH;
				} else {
					return IApplication.EXIT_RESTART;
				}
            } else {
                return IApplication.EXIT_OK;
            }
        } finally {
            display.dispose();
        }
	}

    /**
     * Filters commandlineargs for a relaunch. Only applicable within the {@link #start(IApplicationContext)} or {@link #startApplication(IApplicationContext)} functions.
     * If this behavior is desired elsewhere, see {@link Application#restart_cmdlinefilter} and use Workbench.restart().
     * 
     * @param context the application context
     * @param filter the filter to use on the commandline args
     * @return
     */
    private static void filterCommandlineArgsForRelaunch(IApplicationContext context, Function<List<String>, List<String>> filter) {
    	// TODO: these do not include the launcher or the equinox runnables!! Find `EnvironmentInfo` seems the way to proceed.
    	List<String> args = Arrays.asList(Platform.getCommandLineArgs());
    	List<String> filteredArgs = filter.apply(args);

		// TODO: escape whitespace? e.g. for -data ... paths?
    	String resultargs = filteredArgs.stream().collect(Collectors.joining(" "));
		// TODO: test if that actually has the desired effect
    	System.setProperty("eclipse.exitdata", resultargs);
	}

    /**
     * This restarts the application. May only be called when the workbench has been started yet! Else, 
     * see the code pertaining to {@link Application#restart_cmdlinefilter} and {@link #startApplication(IApplicationContext)}
     * 
     * @param ask whether a dialog box should be opened that asks whether the action should be performed.
     * @param cmdlineFilter a filter that is applied to the commandline arguments
     */
	private static void restartAppWithCommandlinefilter(boolean ask, Function<List<String>, List<String>> cmdlineFilter) {
		boolean doRestart = false;
		if (ask) {
			MessageBox mbox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			mbox.setText(org.jcryptool.core.preferences.pages.Messages.MessageTitleRestart);
			mbox.setMessage(org.jcryptool.core.preferences.pages.Messages.MessageRestart);
			if (mbox.open() == SWT.YES) {
				LogUtil.logInfo(org.jcryptool.core.preferences.pages.Messages.MessageLogRestart);
				doRestart = true;
			}
		} else {
			doRestart = true;
		}
		
		if (doRestart) {
			// this instructs the application to filter commandline arguments and relaunching instead of restarting
			Application.restart_cmdlinefilter = cmdlineFilter;
			PlatformUI.getWorkbench().restart();
		}
    }
    
	private static void applyFilterToInifile(Function<List<String>, List<String>> filter) throws IOException {
        String path = Platform.getInstallLocation().getURL().toExternalForm();
        String fileNameOrg = Platform.getProduct().getName() + ".ini"; //$NON-NLS-1$
        String fileNameBak = fileNameOrg + ".bak"; //$NON-NLS-1$

        if ("macosx".equalsIgnoreCase(Platform.getOS())) { //$NON-NLS-1$
            path += "JCrypTool.app/Contents/MacOS/"; //$NON-NLS-1$
        }
        File fileOrg = new File(new URL(path + fileNameOrg).getFile());
        File fileBak = new File(new URL(path + fileNameBak).getFile());
        if (fileBak.exists()) {
            fileBak.delete();
        }
        // solve the problem that if the .ini file doesn't exist yet, it can't be created
        if(!fileOrg.exists()) {
        	fileOrg.createNewFile();
        }
        fileOrg.renameTo(fileBak);
        BufferedReader in = new BufferedReader(new FileReader(fileBak));
        BufferedWriter out = new BufferedWriter(new FileWriter(fileOrg));

        // the filtering step
        List<String> lines = in.lines().collect(Collectors.toList());
		var linesOut = filter.apply(lines);

		for(String outLine : linesOut) {
        	out.write(outLine + "\n");
        }

		out.flush();
		out.close();
        in.close();
	}
	
	/**
	 * rewrites a commandline so that it does not include -nl flags
	 * @param newLanguage the new language (e.g. "en")
	 * @return the new commandline as list
	 */
	private static Function<List<String>, List<String>> createLanguageRewriteInifileFilter(String newLanguage) {
		return (List<String> in) -> {
			var linesOut = new LinkedList<String>();
			boolean previousWasNLFlag = false;
			for(String line: in) {
				boolean isNlFlag = line.trim().equals("-nl");
				// first, keep all lines that don't have to do with language
				if (! (isNlFlag || previousWasNLFlag)) {
					linesOut.add(line);
				}
				// keep track of state
				if (isNlFlag) {
					previousWasNLFlag = true;
				} else {
					previousWasNLFlag = false;
				}
			}
			// add the new language flags at the end
			linesOut.add("-nl");
			linesOut.add(newLanguage);
			return linesOut;
		};
	}

	/**
	 * rewrites a commandline so that it does not include -nl flags
	 * 
	 * @return the new commandline as list
	 */
	private static Function<List<String>, List<String>> createCmdlineRewriteRemoveLanguagespecFilter() {
		return (List<String> in) -> {
			var linesOut = new LinkedList<String>();
			boolean previousWasNLFlag = false;
			for(String line: in) {
				boolean isNlFlag = line.trim().equals("-nl");
				// first, keep all lines that don't have to do with language
				if (! (isNlFlag || previousWasNLFlag)) {
					linesOut.add(line);
				}
				// keep track of state
				if (isNlFlag) {
					previousWasNLFlag = true;
				} else {
					previousWasNLFlag = false;
				}
			}
			return linesOut;
		};
	}
	
	public static void restartWithChangedLanguage(String language, boolean ask) throws IOException {
		Function<List<String>, List<String>> cmdlineFilter = createCmdlineRewriteRemoveLanguagespecFilter();
		Function<List<String>, List<String>> inifileFilter = createLanguageRewriteInifileFilter(language);
		applyFilterToInifile(inifileFilter);
		restartAppWithCommandlinefilter(ask, cmdlineFilter);
	}

}
