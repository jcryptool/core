// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.preferences.pages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.CorePlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * <p>
 * General preference page. On this page the language can be changed between English and German. If the language is
 * changed the application needs to be restarted.
 * </p>
 * 
 * <p>
 * <b>This feature has no effect, if you start the application directly from an Eclipse IDE.</b>
 * </p>
 * 
 * @author Dominik Schadow
 * @version 0.9.0
 */
public class GeneralPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    private Combo listLanguage;
    private Label lblLocation;

    private String[] nl;
    private String[] nlText;

    private String currentLanguage = Platform.getNL().substring(0, 2);
    private String currentLocation;
    private URL currentLocationURL = Platform.getInstanceLocation().getURL();

    public GeneralPage() {
        super(GRID);
        setPreferenceStore(CorePlugin.getDefault().getPreferenceStore());
        IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint("org.jcryptool.core.platformLanguage"); //$NON-NLS-1$
        IExtension[] ext = p.getExtensions();
        nl = new String[ext.length];
        nlText = new String[ext.length];
        for (int i = 0; i < ext.length; i++) {
            IConfigurationElement element = ext[i].getConfigurationElements()[0];
            nl[i] = element.getAttribute("languageCode").substring(0, 2); //$NON-NLS-1$
            nlText[i] = element.getAttribute("languageDescription"); //$NON-NLS-1$
        }
        if(currentLocationURL.getProtocol().equals("file")) {
        	IPath path = new Path(currentLocationURL.getPath());
        	currentLocation = path.toOSString();
        } else {
        	currentLocation = currentLocationURL.toString();
        }
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.jcryptool.core.generalPreferences"); //$NON-NLS-1$
    }

    @Override
    protected Control createContents(Composite parent) {
        Group gLanguage = new Group(parent, SWT.NONE);
        gLanguage.setText(Messages.SelectLanguage);
        gLanguage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        gLanguage.setLayout(new GridLayout());

        listLanguage = new Combo(gLanguage, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);

        for (int i = 0; i < nl.length; i++) {
            listLanguage.add(nlText[i]);
            if (nl[i].equals(currentLanguage)) {
                listLanguage.select(i);
            }
        }
        if (!Arrays.asList(nl).contains(currentLanguage)) {
            listLanguage.select(0);
        }

        Group gLocation = new Group(parent, SWT.NONE);
        gLocation.setText(Messages.WorkspaceLocation);
        GridData gd_gLocation = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_gLocation.verticalIndent = 30;
        gLocation.setLayoutData(gd_gLocation);
        gLocation.setLayout(new GridLayout());
        
        lblLocation = new Label(gLocation, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
        lblLocation.setText(currentLocation);
        lblLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        return super.createContents(parent);
    }

    @Override
    public boolean performOk() {
        try {
            if (!nl[listLanguage.getSelectionIndex()].equals(currentLanguage)) {
                setLanguage(nl[listLanguage.getSelectionIndex()]);
                restartApp();
            }
        } catch (Exception ex) {
            LogUtil.logError("It is not possible to change the language.", ex); //$NON-NLS-1$
            setErrorMessage(Messages.MessageError);
        }
        return super.performOk();
    }

    @Override
    protected void performDefaults() {
        for (int i = 0; i < nl.length; i++) {
            if (nl[i].equals(currentLanguage)) {
                listLanguage.select(i);
            }
        }
        if (!Arrays.asList(nl).contains(currentLanguage)) {
            listLanguage.select(0);
        }

        setErrorMessage(null);
        super.performDefaults();
    }

    @Override
	public void init(IWorkbench workbench) {
    }

    /**
     * Sets the language in the <b>JCrypTool.ini</b>. This file is located in a different folder on Mac OS X.
     * 
     * @param language
     * @throws Exception
     */
    private void setLanguage(String language) throws Exception {
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

        try {
            String line = in.readLine();
            if (line != null && line.equals("-nl")) { //$NON-NLS-1$
                out.write(line);
                out.newLine();
                line = in.readLine();

                out.write(language);
                out.newLine();
                for (int i = 0; i < nl.length; i++) {
                    if (line.equals(nl[i])) {
                        line = in.readLine();
                    }
                }

            } else {
                out.write("-nl"); //$NON-NLS-1$
                out.newLine();
                out.write(language);
                out.newLine();
            }
            while (line != null) {
                if (line.equals("-nl")) { //$NON-NLS-1$
                    line = in.readLine();
                    for (int i = 0; i < nl.length; i++) {
                        if (line.equals(nl[i])) {
                            line = in.readLine();
                        }
                    }
                } else {
                    out.write(line);
                    out.newLine();
                }

                line = in.readLine();
            }
            out.flush();
        } catch (IOException ieo) {
            throw (ieo);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioe) {
                LogUtil.logError(ioe);
            }
        }
    }

    private void restartApp() {
        MessageBox mbox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        mbox.setText(Messages.MessageTitleRestart);
        mbox.setMessage(Messages.MessageRestart);
        if (mbox.open() == SWT.YES) {
            LogUtil.logInfo(Messages.MessageLogRestart);
            PlatformUI.getWorkbench().restart();
        }
    }

    @Override
    protected void createFieldEditors() {
    }
}
