//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.substitution.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.substitution.SubstitutionPlugin;

/**
 * The wizard page for the substitution cipher.
 *
 * @author t-kern
 *
 */
public class SubstitutionWizardPage extends AbstractClassicCryptoPage {

	private Button keyEditorBtn;

	public SubstitutionWizardPage() {
		super(Messages.SubstitutionWizardPage_substitution, Messages.SubstitutionWizardPage_enterkey1);
	}

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                SubstitutionPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }
    
    @Override
    protected String generateCommandLineString() {
    	String encDec = operationInput.getContent()?"-E":"-D";
    	String key = "-k " + quoteCmdlineArgIfNecessary(getKey());
    	
    	String result = "substitution " + encDec + " -ed " + key;

    	result += " " + generateAlphabetPartForCommandLine();
    	
    	if(!isNonAlphaFilter()) result += " --noFilter";
    	return result;
    }

	@Override
	protected void createKeyInputObjects() {
		// TODO Auto-generated method stub
		super.createKeyInputObjects();
	}

	@Override
	protected void createKeyGroup(Composite parent) {
		keyGroup = new Group(parent, SWT.NONE);

        GridLayout keyGroupGridLayout = new GridLayout();
        keyGroupGridLayout.numColumns = 4;

        GridData keyGroupGridData = new GridData();
        keyGroupGridData.horizontalAlignment = GridData.FILL;
        keyGroupGridData.grabExcessHorizontalSpace = true;
        keyGroupGridData.grabExcessVerticalSpace = false;
        keyGroupGridData.verticalAlignment = SWT.TOP;

        keyGroup.setLayoutData(keyGroupGridData);
        keyGroup.setLayout(keyGroupGridLayout);
        keyGroup.setText(org.jcryptool.crypto.classic.model.ui.wizard.Messages.WizardPage_key);

        keyDescriptionLabel = new Label(keyGroup, SWT.NONE);

        GridData keyDescriptionLabelGridData = new GridData();
        keyDescriptionLabelGridData.horizontalAlignment = GridData.FILL;
        keyDescriptionLabelGridData.grabExcessVerticalSpace = true;
        keyDescriptionLabelGridData.exclude = false;

        keyDescriptionLabel.setText("Set key by password:");
        keyDescriptionLabel.setLayoutData(keyDescriptionLabelGridData);
        keyDescriptionLabel.setVisible(true);

        keyText = new Text(keyGroup, SWT.BORDER);

        GridData keyTextGridData = new GridData();
        keyTextGridData.grabExcessHorizontalSpace = true;
        keyTextGridData.horizontalAlignment = GridData.FILL;
        keyTextGridData.verticalAlignment = GridData.CENTER;
        keyTextGridData.grabExcessVerticalSpace = true;

        keyText.setLayoutData(keyTextGridData);
        keyText.setToolTipText(org.jcryptool.crypto.classic.model.ui.wizard.Messages.AbstractClassicCryptoPage_keyToolTip);
        
        Label lblKeyOrSeparator = new Label(keyGroup, SWT.NONE);
        lblKeyOrSeparator.setText("or");
        
        keyEditorBtn = new Button(keyGroup, SWT.PUSH);
        keyEditorBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        keyEditorBtn.setText("Set substitutions manually");
        
        keyEditorBtn.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		SubstitutionKeyEditor.launchKeyEditorDialog(getShell(), getSelectedAlphabet(), keyInput.getContent());
        	}
		});
	}
    
    
    
}
