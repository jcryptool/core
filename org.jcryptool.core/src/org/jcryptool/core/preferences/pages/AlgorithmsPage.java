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
package org.jcryptool.core.preferences.pages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.CorePlugin;

/**
 * The start page for all <b>Algorithms</b> preference pages.</br>
 * If you want to extend it, put your code in the {@link #createContents(Composite)}
 * method.
 *
 * @author Dominik Schadow
 * @version 0.6.0
 */
public class AlgorithmsPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	private Button checkbox;
	
    public AlgorithmsPage() {
        super(GRID);
        setPreferenceStore(CorePlugin.getDefault().getPreferenceStore());
    }
    
    @Override
    protected Control createContents(Composite parent) {
    	
    	// This creates the group with the settings for the 
    	// algortihm perspective.
    	Group group = new Group(parent, SWT.NONE);
    	group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    	group.setText(Messages.AlgorithmsPage_group_title);
    	group.setLayout(new GridLayout());
    	
    	checkbox = new Button(group, SWT.CHECK);
    	checkbox.setText(Messages.AlgorithmsPage_checkbox_text);
    	checkbox.setSelection(!getPreferenceStore().getBoolean("DONT_SHOW_ALGORITHM_INTRODUCTION"));
    	
    	return super.createContents(parent);
    }
    
    @Override
    protected void performDefaults() {
    	// This sets the checkbox and the introduction is shown.
    	// The default behavior is to show the introduction for the algorithm perspective.
    	checkbox.setSelection(true);
    	
    	super.performDefaults();
    }
    
    @Override
    public boolean performOk() {
    	getPreferenceStore().setValue("DONT_SHOW_ALGORITHM_INTRODUCTION", !checkbox.getSelection());
    	return super.performOk();
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.jcryptool.core.algorithmsPreferences"); //$NON-NLS-1$
    }
    

    @Override
	public void init(IWorkbench workbench) {
    }

	@Override
	protected void createFieldEditors() {
		
	}
}
