//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.logging.preferences.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.LoggingPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

public class LoggerPage extends PreferencePage implements IWorkbenchPreferencePage {
	
    private Button btError;
    private Button btWarn;
    private Button btInfo;
    private Composite content;
    private Label lbError;
    private Label lbWarn;
    private Label lbInfo;
    private int loglevel;

    public LoggerPage() {
        super("LOGGING"); //$NON-NLS-1$
        setDescription(Messages.LoggingPage_0);
    }

    @Override
    protected Control createContents(Composite parent) {
        content = new Composite(parent, SWT.NONE);
        content.setLayout(new GridLayout());

        Group gLoglevel = new Group(content, SWT.SHADOW_ETCHED_IN);
        gLoglevel.setText(Messages.loglevel);
        gLoglevel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        gLoglevel.setLayout(new GridLayout(2, false));

        btError = new Button(gLoglevel, SWT.RADIO);
        btError.setText(Messages.leglevel_error);
        lbError = new Label(gLoglevel, SWT.NONE);
        lbError.setText("- " + Messages.loglevel_error_description); //$NON-NLS-1$
        btWarn = new Button(gLoglevel, SWT.RADIO);
        btWarn.setText(Messages.loglevel_warn);
        lbWarn = new Label(gLoglevel, SWT.NONE);
        lbWarn.setText("- " + Messages.loglevel_warn_description); //$NON-NLS-1$
        btInfo = new Button(gLoglevel, SWT.RADIO);
        btInfo.setText(Messages.loglevel_info);
        lbInfo = new Label(gLoglevel, SWT.NONE);
        lbInfo.setText("- " + Messages.loglevel_info_description); //$NON-NLS-1$

        refreshWidgets();

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.jcryptool.core.logging.preferences"); //$NON-NLS-1$

        return content;
    }

    protected void refreshWidgets() {
        if (loglevel == IStatus.ERROR)
            btError.setSelection(true);
        else
            btError.setSelection(false);
        if (loglevel == IStatus.WARNING)
            btWarn.setSelection(true);
        else
            btWarn.setSelection(false);
        if (loglevel == IStatus.INFO)
            btInfo.setSelection(true);
        else
            btInfo.setSelection(false);
    }

    protected void setDefaultLogLevel() {
        loglevel = doGetPreferenceStore().getInt(LogUtil.LOGGER_LOG_LEVEL);
        if (loglevel == 0)
            loglevel = LogUtil.getLogLevel();
    }

    @Override
	protected IPreferenceStore doGetPreferenceStore() {
        return LoggingPlugin.getDefault().getPreferenceStore();
    }

    @Override
	protected void performDefaults() {
        setDefaultLogLevel();
        refreshWidgets();
        super.performDefaults();
    }

    @Override
	public boolean performOk() {
        if (btError.getSelection()) {
            doGetPreferenceStore().setValue(LogUtil.LOGGER_LOG_LEVEL, IStatus.ERROR);
            LogUtil.setLogLevel(IStatus.ERROR);
        } else if (btWarn.getSelection()) {
            doGetPreferenceStore().setValue(LogUtil.LOGGER_LOG_LEVEL, IStatus.WARNING);
            LogUtil.setLogLevel(IStatus.WARNING);
        } else if (btInfo.getSelection()) {
            doGetPreferenceStore().setValue(LogUtil.LOGGER_LOG_LEVEL, IStatus.INFO);
            LogUtil.setLogLevel(IStatus.INFO);
        }
        return super.performOk();
    }

    @Override
	public void init(IWorkbench workbench) {
        setDefaultLogLevel();
    }
}
