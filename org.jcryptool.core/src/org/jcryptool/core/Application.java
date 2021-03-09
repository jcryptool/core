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
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;
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
public class Application implements IApplication {

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
				int retcode = chooser.open();
				restartWithChangedLanguage(chooser.nl, false);
        	}
        } catch (Exception e) {
            LogUtil.logError(CorePlugin.PLUGIN_ID, e);
        }
        
        Display display = PlatformUI.createDisplay();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IApplication.EXIT_RESTART;
            } else {
                return IApplication.EXIT_OK;
            }
        } finally {
            display.dispose();
        }

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
     * Sets the language in the <b>JCrypTool.ini</b>. This file is located in a different folder on Mac OS X.
     * 
     * @param language
     * @throws IOException 
     * @throws Exception
     */
    private static void setLanguage(String language) throws IOException {
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
        var lines = in.lines().collect(Collectors.toList());
        in.close();
        var linesOut = new LinkedList<String>();
        boolean previousWasNLFlag = false;
        for(String line: lines) {
        	boolean isNlFlag = line.trim().equals("-nl");
        	// first, keep all lines that don't have to do with language
        	if (isNlFlag || previousWasNLFlag) {
				linesOut.add(line); // TODO: includes linebreak?
			}
        	// keep track of state
			if (isNlFlag) {
        		previousWasNLFlag = true;
        	} else {
        		previousWasNLFlag = false;
			}
        }
        linesOut.add("-nl");
        linesOut.add(language);
		for(String outLine : linesOut) {
        	out.write(outLine + "\n");
        }
		out.flush();
		out.close();
        
    }

    private static void restartAppWithInifileReload(boolean ask) {
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
			PlatformUI.getWorkbench().restart();
		}
    }
    
	public static void restartWithChangedLanguage(String language, boolean ask) throws IOException {
		setLanguage(language);
		restartAppWithInifileReload(ask);
	}
}
