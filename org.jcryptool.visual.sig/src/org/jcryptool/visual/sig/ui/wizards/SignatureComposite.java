//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sig.ui.wizards;

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
import org.jcryptool.visual.sig.algorithm.Input;

import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;

/**
 * This class contains the GUI elements for signature wizard. It also contains a method to load all RSA/DSA keys from
 * the keystore and displays them in a dropdown list.
 * 
 * @author Grebe
 */
public class SignatureComposite extends Composite implements SelectionListener {
    private Group grpSignatures;
    private Text txtDescription;
    private Button rdo1;
    private Button rdo2;
    private Button rdo3;
    private Button rdo4;
    private Combo combo;
    private KeyStoreAlias alias = null;
    private int keyType = 0;
    private SignatureWizardPage page = null;
    private static final HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();

    private int method;
    private Menu menuSig;
    private MenuItem mntmSig;
    private Label lblSelectAKey;

    public SignatureComposite(Composite parent, int style, int m, SignatureWizardPage p) {
        super(parent, style);
        method = m;
        page = p;
        
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
        rdo1.setSelection(Input.s == 0);

        rdo2 = new Button(grpSignatures, SWT.RADIO);
        rdo2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo2.setText(Messages.SignatureWizard_RSA);
        rdo2.setSelection(Input.s == 1);

        rdo3 = new Button(grpSignatures, SWT.RADIO);
        rdo3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo3.setText(Messages.SignatureWizard_ECDSA);
        rdo3.setSelection(Input.s == 2);

        rdo4 = new Button(grpSignatures, SWT.RADIO);
        rdo4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        rdo4.setText(Messages.SignatureWizard_RSAandMGF1);
        rdo4.setSelection(Input.s == 3);
        
        lblSelectAKey = new Label(this, SWT.NONE);
        lblSelectAKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        if (Input.s == 2) {
        	lblSelectAKey.setText(Messages.SignatureWizard_labelCurve);
        } else {
        	lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
        }

        combo = new Combo(this, SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                alias = keystoreitems.get(combo.getText());
                page.setPageComplete(true);
            }
        });
        
        //fill combo box depending on the last chosen signature method
        combo.removeAll();
        if (Input.s == 0 || Input.s == 1) {
        	initializeKeySelection(Input.s);
        } else if (Input.s == 3) {
        	initializeKeySelection(2);
        } else if (Input.s == 2) {
        	combo.add("Elliptic curve: ANSI X9.62 prime256v1 (256 bits)"); //$NON-NLS-1$
        	combo.select(0);
        }
        
        //select the last chosen key
        if (Input.key != null) {
        	alias = Input.key;
            for (String item : combo.getItems()) {
            	if (keystoreitems.get(item).toString().equals(alias.toString())) {
            		combo.select(combo.indexOf(item));
            	}
            }
            alias = keystoreitems.get(combo.getText());
            page.setPageComplete(true);
        }

        Group grpDescription = new Group(this, SWT.NONE);
        grpDescription.setText(Messages.SignatureWizard_grpDescription);
        grpDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        grpDescription.setLayout(new GridLayout());

        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.NO_BACKGROUND | SWT.V_SCROLL);
        txtDescription.setEditable(false);
        txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        if (Input.s == 0) {
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_DSA + Messages.SignatureWizard_Usage2 
        			+ Messages.SignatureWizard_DSA_description);
        } else if (Input.s == 1) {
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_RSA + Messages.SignatureWizard_Usage2 
        			+ Messages.SignatureWizard_RSA_description);
        } else if (Input.s == 2) {
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_ECDSA + Messages.SignatureWizard_Usage2
        			+ Messages.SignatureWizard_ECDSA_description);
        } else if (Input.s == 3) {
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_RSAandMGF1 + Messages.SignatureWizard_Usage2
        			+ Messages.SignatureWizard_RSAandMGF1_description);
        }

        menuSig = new Menu(txtDescription);
        txtDescription.setMenu(menuSig);

        mntmSig = new MenuItem(menuSig, SWT.NONE);
        mntmSig.setText(Messages.Wizard_menu);
        // To select all text
        mntmSig.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                txtDescription.selectAll();
            }
        });
        
        rdo1.addSelectionListener(this);
        rdo2.addSelectionListener(this);
        rdo3.addSelectionListener(this);
        rdo4.addSelectionListener(this);
        
        switch (method) {
            case 0: // MD5: RSA
                rdo2.setEnabled(true);
                rdo1.setEnabled(false);
                rdo3.setEnabled(false);
                rdo4.setEnabled(false);

                if (Input.s == -1) {
                    rdo2.setSelection(true);
                    rdo1.setSelection(false);
                    txtDescription.setText(Messages.SignatureWizard_Usage 
                			+ Messages.SignatureWizard_RSA + Messages.SignatureWizard_Usage2 
                			+ Messages.SignatureWizard_RSA_description);
                    keyType = 1;
                }
                break;
            case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
                rdo1.setEnabled(true);
                rdo2.setEnabled(true);
                rdo3.setEnabled(true);
                rdo4.setEnabled(true);

                if (Input.s == -1) {
	                rdo1.setSelection(true);
	                rdo2.setSelection(false);
	                txtDescription.setText(Messages.SignatureWizard_Usage 
	            			+ Messages.SignatureWizard_DSA + Messages.SignatureWizard_Usage2 
	            			+ Messages.SignatureWizard_DSA_description);
	                keyType = 0;
                }
                break;
            case 2:
            case 3:
            case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
                rdo2.setEnabled(true);
                rdo3.setEnabled(true);
                rdo4.setEnabled(true);
                rdo1.setEnabled(false);

                if (Input.s == -1) {
	                rdo2.setSelection(true);
	                rdo1.setSelection(false);
                    txtDescription.setText(Messages.SignatureWizard_Usage 
                			+ Messages.SignatureWizard_RSA + Messages.SignatureWizard_Usage2 
                			+ Messages.SignatureWizard_RSA_description);
	                keyType = 1;
                }
                break;
        }

        // If called by JCT-CA only SHA-256 can be used! Therefore only ECDSA, RSA and RSA with MGF1 will work
        if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) {
            // Enable RSA
            rdo2.setSelection(true);
            // Disable all other methods
            rdo1.setEnabled(false);
            rdo1.setSelection(false);
            rdo3.setEnabled(false);
            rdo4.setEnabled(false);
            // Disable the key selection
            combo.setVisible(false);
            lblSelectAKey.setVisible(false);
            // Enable the finish button
            page.setPageComplete(true);
        } else {
            // Load the keys
        	if (Input.s == -1) {
        		initializeKeySelection(keyType);
        	}  
        }

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

    @Override
    // Checks if the radio buttons have changed and updates the text and keys from the keystore
    public void widgetSelected(SelectionEvent e) { 
        if (rdo1.getSelection()) {
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_DSA + Messages.SignatureWizard_Usage2 
        			+ Messages.SignatureWizard_DSA_description);
            // Clean up
            keystoreitems.clear();
            combo.removeAll();
            lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            initializeKeySelection(0);
        } else {
            if (rdo2.getSelection()) {
                txtDescription.setText(Messages.SignatureWizard_Usage 
            			+ Messages.SignatureWizard_RSA + Messages.SignatureWizard_Usage2 
            			+ Messages.SignatureWizard_RSA_description);
                // Clean up
                keystoreitems.clear();
                combo.removeAll();
                lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
                initializeKeySelection(1);
            } else {
                if (rdo3.getSelection()) {
                    txtDescription.setText(Messages.SignatureWizard_Usage + Messages.SignatureWizard_ECDSA_description);
                    // Clean up
                    keystoreitems.clear();
                    combo.removeAll();
                    combo.add("Elliptic curve: ANSI X9.62 prime256v1 (256 bits)"); //$NON-NLS-1$
                    combo.select(0);
                    txtDescription.setText(Messages.SignatureWizard_Usage 
                			+ Messages.SignatureWizard_ECDSA + Messages.SignatureWizard_Usage2
                			+ Messages.SignatureWizard_ECDSA_description);
                    page.setPageComplete(true);
                } else {
                    if (rdo4.getSelection()) {
                    	txtDescription.setText(Messages.SignatureWizard_Usage 
                    			+ Messages.SignatureWizard_RSAandMGF1 + Messages.SignatureWizard_Usage2
                    			+ Messages.SignatureWizard_RSAandMGF1_description);
                        // Clean up
                        keystoreitems.clear();
                        combo.removeAll();
                        lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
                        initializeKeySelection(2);
                    }
                }
            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /**
     * Either loads available keys for the given method and fills them into the combo field or shows the given key from
     * JCTCA an disables the combo field
     * 
     * @param method The signature method (integer, 0=DSA, 1=RSA, 2=RSAandMGF1)
     */
    private void initializeKeySelection(int method) {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        KeyStoreAlias currAlias;
        Enumeration<String> aliases = ksm.getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            currAlias = new KeyStoreAlias(aliases.nextElement());
            currAlias.getAliasString();
            if (method == 0) { // DSA
                if (currAlias.getClassName().equals(DSAPrivateKey.class.getName())) {
                    // Fill in keys
                    combo.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " + currAlias.getClassName()); //$NON-NLS-1$ //$NON-NLS-2$
                    keystoreitems.put(
                            currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " + currAlias.getClassName(), //$NON-NLS-1$ //$NON-NLS-2$
                            currAlias);
                } // end if

            } else {
                if (method == 1) { // RSA
                    if (currAlias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                        // Fill in keys
                        combo.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                                + currAlias.getClassName());
                        keystoreitems
                                .put(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                                        + currAlias.getClassName(), currAlias);
                    } // end if
                } else {
                    if (method == 2) { // RSAandMGF1
                        if (currAlias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                            // Fill in keys
                            combo.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                                    + currAlias.getClassName());
                            keystoreitems.put(
                                    currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                                            + currAlias.getClassName(), currAlias);
                        }
                    }
                }
            }
        }
    combo.select(0);
    alias = keystoreitems.get(combo.getText());
    page.setPageComplete(true);
    }
}
