// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.ui.wizards;

import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sigVerification.algorithm.Input;

import de.flexiprovider.core.dsa.DSAPublicKey;
import de.flexiprovider.core.rsa.RSAPublicKey;
import de.flexiprovider.ec.keys.ECPublicKey;

/**
 * This class contains the GUI elements for signature wizard.
 * 
 * @author Wilfing
 */
public class SignatureComposite extends Composite implements SelectionListener {
    private Group grpSignatures;
    private Text txtDescription;
    private Button rdo1;
    private Button rdo2;
    private Button rdo3;
    private Button rdo4;
    private int hashMethod;
    private int signatureMethod = -1;
    private Menu menuSig;
    private MenuItem mntmSig;
    private Input input;
	private Label lblSelectAKey;
	private Combo comboKey;
	private SignatureWizardPage page = null;
	private KeyStoreAlias alias = null;
    private static final HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();

    public SignatureComposite(Composite parent, int style, int m, SignatureWizardPage p, Input input) {
        super(parent, style);
        this.input = input;
        this.hashMethod = m;
        this.page = p;
        
        if (input.s != -1) //load previously used signature method 
        	signatureMethod = input.s;
        
        initialize();
    }

    /**
     * Draws all the controls of the composite
     */
    private void initialize() {
        setLayout(new GridLayout());
        
        grpSignatures = new Group(this, SWT.NONE);
        grpSignatures.setText(Messages.SignatureWizard_grpSignatures);
        grpSignatures.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        grpSignatures.setLayout(new GridLayout());

        rdo1 = new Button(grpSignatures, SWT.RADIO);
        rdo1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo1.setText(Messages.SignatureWizard_DSA);
        rdo1.addSelectionListener(this);

        rdo2 = new Button(grpSignatures, SWT.RADIO);
        rdo2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo2.setText(Messages.SignatureWizard_RSA);
        rdo2.addSelectionListener(this);

        rdo3 = new Button(grpSignatures, SWT.RADIO);
        rdo3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo3.setText(Messages.SignatureWizard_ECDSA);
        rdo3.addSelectionListener(this);

        rdo4 = new Button(grpSignatures, SWT.RADIO);
        rdo4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo4.setText(Messages.SignatureWizard_RSAandMGF1);
        rdo4.addSelectionListener(this);
        
        lblSelectAKey = new Label(this, SWT.NONE);
        lblSelectAKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        comboKey = new Combo(this, SWT.READ_ONLY);
        comboKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        comboKey.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                alias = keystoreitems.get(comboKey.getText());
                page.setPageComplete(true);
            }
        });
        
//        Group grpDescription = new Group(this, SWT.NONE);
//        grpDescription.setText(Messages.SignatureWizard_grpDescription);
//        grpDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        grpDescription.setLayout(new GridLayout());

//        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.NO_BACKGROUND | SWT.V_SCROLL);
        txtDescription = new Text(this, SWT.WRAP | SWT.NO_BACKGROUND);
        txtDescription.setEditable(false);
        txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        txtDescription.setText(Messages.HashWizard_FurtherInfoInOnlineHelp);

//        menuSig = new Menu(txtDescription);
//        txtDescription.setMenu(menuSig);
//
//        mntmSig = new MenuItem(menuSig, SWT.NONE);
//        mntmSig.setText(Messages.Wizard_menu);
//        // To select all text
//        mntmSig.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//                txtDescription.selectAll();
//            }
//        });

        initializeHashMethodDependencies();        
        initializeSignatureMethodDependencies();
    	initializeKeySelection();
    }
    
	/**
     * Initializes the radio buttons and the initial signatureMethod depending on the chosen hash method
     */
    private void initializeHashMethodDependencies() {
    	switch (hashMethod) {
        case 0: // MD5: RSA
            rdo2.setEnabled(true);
            rdo1.setEnabled(false);
            rdo3.setEnabled(false);
            rdo4.setEnabled(false);
            if (signatureMethod == -1) signatureMethod = 1;
            break;
        case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
            rdo1.setEnabled(true);
            rdo2.setEnabled(true);
            rdo3.setEnabled(true);
            rdo4.setEnabled(true);
            if (signatureMethod == -1) signatureMethod = 0;
            break;
        case 2:
        case 3:
        case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
            rdo2.setEnabled(true);
            rdo3.setEnabled(true);
            rdo4.setEnabled(true);
            rdo1.setEnabled(false);
            if (signatureMethod == -1) signatureMethod = 1;
            break;
        }
	}

    /**
     * Initializes the selected radio button and the description texts depending on the signature method
     */
    private void initializeSignatureMethodDependencies() {
    	rdo1.setSelection(signatureMethod == 0);
    	rdo2.setSelection(signatureMethod == 1);
    	rdo3.setSelection(signatureMethod == 2);
    	rdo4.setSelection(signatureMethod == 3);
    	switch (signatureMethod) {
    	case 0:
//            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_DSA_description);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            break;
    	case 1:
//            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSA_description);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            break;
    	case 2:
//            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_ECDSA_description);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelCurve);
            break;
    	case 3:
//            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSAandMGF1_description);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            break;
    	}
	}
    
    /**
     * Either loads available keys for the given method and fills them into the combo field or shows the given key from
     * JCTCA an disables the combo field
     * 
     */
    private void initializeKeySelection() {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        KeyStoreAlias currAlias;
        Enumeration<String> aliases = ksm.getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            currAlias = new KeyStoreAlias(aliases.nextElement());
            currAlias.getAliasString();
            if (signatureMethod == 0) { // DSA
                if (currAlias.getClassName().equals(DSAPublicKey.class.getName())) {
                    // Fill in keys
                    comboKey.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " + currAlias.getClassName()); //$NON-NLS-1$ //$NON-NLS-2$
                    keystoreitems.put(
                            currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " + currAlias.getClassName(), //$NON-NLS-1$ //$NON-NLS-2$
                            currAlias);
                }
            } else if (signatureMethod == 1 || signatureMethod == 3) { // RSA or RSAandMGF1 
                if (currAlias.getClassName().equals(RSAPublicKey.class.getName())) {
                    // Fill in keys
                    comboKey.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                            + currAlias.getClassName());
                    keystoreitems
                            .put(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                                    + currAlias.getClassName(), currAlias);
                } 
            } else if (signatureMethod == 2) { //ECDSA
                if (currAlias.getClassName().equals(ECPublicKey.class.getName())) {
                    // Fill in keys
                    comboKey.add(currAlias.getContactName() + " - "  + currAlias.getClassName());  //$NON-NLS-1$ //$NON-NLS-2$
                    keystoreitems
                            .put(currAlias.getContactName() + " - "  + currAlias.getClassName(), currAlias);  //$NON-NLS-1$ //$NON-NLS-2$
                }  
            }
        } 
    
	    //select the last chosen key (if a previous key was chosen, before)
        boolean keySelected = false;
	    if (input.publicKeyAlias != null) {
	    	alias = input.publicKeyAlias;
	        for (String item : comboKey.getItems()) {
	        	if (keystoreitems.get(item).toString().equals(alias.toString())) {
	        		comboKey.select(comboKey.indexOf(item));
	        		keySelected = true;
	        	}
	        }
	    } 
	    
	    if (!keySelected) {
		    comboKey.select(0);
	    }

    	alias = keystoreitems.get(comboKey.getText());
	    
	    page.setPageComplete(true);
    }

	/**
     * @return the grpSignatures
     */
    public Group getgrpSignatures() {
        return grpSignatures;
    }
    
    /**
     * @return the KeyStoreAlias
     */
    public KeyStoreAlias getAlias() {
        return alias;
    }

    // Checks if the radio buttons have changed and updates the text and keys from the keystore
    public void widgetSelected(SelectionEvent e) {
        if (rdo1.getSelection()) {
//            txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_DSA_description);
            signatureMethod = 0;
        } else {
            if (rdo2.getSelection()) {
//                txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSA_description);
                signatureMethod = 1;
            } else {
                if (rdo3.getSelection()) {
//                    txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_ECDSA_description);
                    signatureMethod = 2;
                } else {
                    if (rdo4.getSelection()) {
//                        txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_RSAandMGF1_description);
                        signatureMethod = 3;
                    }
                }
            }
        }
        comboKey.removeAll();
        keystoreitems.clear();
        initializeKeySelection();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        switch (hashMethod) {
        case 0: // MD5: RSA
        	signatureMethod = 1;
            break;
        case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
            signatureMethod = 0;
            break;
        case 2:
        case 3:
        case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
        	signatureMethod = 1;
            break;
        }
    }
}
