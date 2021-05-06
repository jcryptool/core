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
package org.jcryptool.actions.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.actions.ui.ActionsUIPlugin;
import org.jcryptool.actions.ui.views.ActionView;

/**
 * General preference page for the <b>Actions view</b>.
 *
 * @author Thomas Wiese
 * @version 0.5
 */
public class GeneralPage extends PreferencePage implements IWorkbenchPreferencePage, Listener{
	
	private Boolean showFilenames;
	private Boolean storePasswords;
	private Boolean contOnError;
	private Boolean showErrors;
	
	private Button bt_showFilenames;
	private Button bt_storePasswords;
	private Button bt_contOnError;
	private Button bt_showErrors;
	
	private Composite content;
	
    public GeneralPage() {
        super();
        setPreferenceStore(ActionsUIPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.GeneralPage_0);
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.jcryptool.actions.ui.generalPreferences"); //$NON-NLS-1$
    }
    
    private void storeValues() {
    	showFilenames = bt_showFilenames.getSelection();
    	contOnError = bt_contOnError.getSelection();
    	storePasswords = bt_storePasswords.getSelection();
    	showErrors = bt_showErrors.getSelection();
    	getPreferenceStore().putValue(PreferenceConstants.P_SHOW_FILENAMES, Boolean.toString(showFilenames));
    	getPreferenceStore().putValue(PreferenceConstants.P_IGNORE_ERRORS, Boolean.toString(contOnError));
    	getPreferenceStore().putValue(PreferenceConstants.P_STORE_PASSWORDS, Boolean.toString(storePasswords));
    	getPreferenceStore().putValue(PreferenceConstants.P_DONT_SHOW_MESSAGES, Boolean.toString(showErrors));
    }

    @Override
	protected void performApply() {
    	storeValues();
    	super.performApply();
    	ActionView av = getActionView();
    	if (av != null) av.setFilenameVisibility();
	}
 
	@Override
	public boolean performOk() {
		storeValues();
		boolean ret = super.performOk();
    	if (ret) {
    		ActionView av = getActionView();
        	if (av != null) av.setFilenameVisibility();
    	}
    	return ret;
	}
	
	private ActionView getActionView(){
		return (ActionView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findViewReference(ActionView.ID).getPart(false); 
	}

    @Override
	public void init(IWorkbench workbench) {
    	showErrors = ActionsUIPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_DONT_SHOW_MESSAGES);
    	contOnError =  ActionsUIPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_IGNORE_ERRORS);
    	showFilenames = ActionsUIPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_SHOW_FILENAMES);
    	storePasswords = ActionsUIPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_STORE_PASSWORDS);
    }

	@Override
	public void handleEvent(Event event) {
		if (event.widget == bt_contOnError){
			if (bt_contOnError.getSelection())
				bt_showErrors.setEnabled(true);
			else
				bt_showErrors.setEnabled(false);
		}
	}

	@Override
	protected Control createContents(Composite parent) {
        content = new Composite(parent, SWT.NONE);
        content.setLayout(new GridLayout());
        
        Group layoutGroup = new Group(content, SWT.NONE);
        layoutGroup.setText(Messages.GeneralPage_1);
        layoutGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        layoutGroup.setLayout(new GridLayout());
        
        bt_showFilenames = new Button(layoutGroup, SWT.CHECK);
        bt_showFilenames.setText(Messages.GeneralPage_3);
        bt_showFilenames.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        bt_showFilenames.setSelection(showFilenames);

        Group securityGroup = new Group(content, SWT.NONE);
        securityGroup.setText(Messages.GeneralPage_2);
        securityGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        securityGroup.setLayout(new GridLayout());
        
        bt_storePasswords = new Button(securityGroup, SWT.CHECK);
        bt_storePasswords.setText(Messages.GeneralPage_4);
        bt_storePasswords.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        bt_storePasswords.setSelection(storePasswords);

        Group errorHandlingGroup = new Group(content, SWT.NONE);
        errorHandlingGroup.setText(Messages.GeneralPage_5);
        errorHandlingGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        errorHandlingGroup.setLayout(new GridLayout());

        bt_contOnError = new Button(errorHandlingGroup, SWT.CHECK);
        bt_contOnError.setText(Messages.GeneralPage_6);
        bt_contOnError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        bt_contOnError.addListener(SWT.Selection, this);
        bt_contOnError.setSelection(contOnError);
        
        bt_showErrors = new Button(errorHandlingGroup, SWT.CHECK);
        bt_showErrors.setText(Messages.GeneralPage_7);
        bt_showErrors.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        bt_showErrors.setSelection(showErrors);
        
        if (bt_contOnError.getSelection())
			bt_showErrors.setEnabled(true);
		else {
			bt_showErrors.setEnabled(false);
		}
        
        return content;
	}
}
