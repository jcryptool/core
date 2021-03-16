// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.Application;
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
    private Text txtLocation;

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
        
        txtLocation = new Text(gLocation, SWT.BORDER | SWT.SINGLE);
        txtLocation.setText(currentLocation);
        txtLocation.setEditable(false);
        txtLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        return super.createContents(parent);
    }

    @Override
    public boolean performOk() {
        try {
            if (!nl[listLanguage.getSelectionIndex()].equals(currentLanguage)) {
                String language = nl[listLanguage.getSelectionIndex()];
            	Application.restartWithChangedLanguage(language, true);
            }
        } catch (Exception ex) {
            LogUtil.logError("org.jcryptool.core", ex); //$NON-NLS-1$
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


    @Override
    protected void createFieldEditors() {
    }
}
