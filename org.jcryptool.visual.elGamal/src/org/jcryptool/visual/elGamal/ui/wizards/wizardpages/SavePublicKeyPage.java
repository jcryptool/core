// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;

/**
 * page for saving a public key.
 *
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class SavePublicKeyPage extends SaveWizardPage {

    /**
     * constructor, sets this page incomplete and the description.
     * @param data shared Data object
     */
    public SavePublicKeyPage(ElGamalData data) {
        super("Save Public Key Page", Messages.SavePublicKeyPage_save_pubkey, null);
        setPageComplete(false);
        setDescription(Messages.SavePublicKeyPage_enter_save_params);
    }

    /**
     * Set up UI stuff.
     *
     * @param parent the parent composite
     */
    @Override
	public final void createControl(final Composite parent) {
        final Composite composite = new Composite(parent, SWT.NONE);
        // do stuff like layout et al
        final GridLayout gl = new GridLayout();
        gl.marginWidth = 50;
        composite.setLayout(gl);
        final Label own = new Label(composite, SWT.NONE);
        own.setText(Messages.SavePublicKeyPage_enter_name);
        own.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        owner = new Text(composite, SWT.BORDER | SWT.SINGLE);
        owner.addModifyListener(new ModifyListener() {
            @Override
			public void modifyText(final ModifyEvent e) {
                setPageComplete(!owner.getText().isEmpty()); 
                
            }
        });
        owner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // finish
        setControl(composite);
    }
    
    @Override
    public IWizardPage getNextPage() {
    	//There is no next page.
    	return null;
    }
    
    @Override
    public String getOwner() {
    	return owner.getText();
    }
    
}
