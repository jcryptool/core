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

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.sig.algorithm.Input;

import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import de.flexiprovider.ec.keys.ECPrivateKey;

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
    private SignatureWizardPage page = null;
    private static final HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();

    private int hashMethod;
    private int signatureMethod = -1;
    private Label lblSelectAKey;
    private String signatureMethodName;

    public SignatureComposite(Composite parent, int style, int hashMethod, SignatureWizardPage p) {
        super(parent, style);
        this.hashMethod = hashMethod;
        page = p;
        
        if (Input.s != -1) 
        	signatureMethod = Input.s;
        
        if (Input.privateKey != null) {
        	alias = Input.privateKey;
        }
        
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

        combo = new Combo(this, SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        combo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                alias = keystoreitems.get(combo.getText());
                page.setPageComplete(true);
            }
        });

        txtDescription = new Text(this, SWT.WRAP | SWT.NO_BACKGROUND);
        txtDescription.setEditable(false);
        txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        initializeHashMethodDependencies();
        initializeSignatureMethodDependencies();
        
        // If called by JCT-CA only SHA-256 can be used! Therefore only ECDSA, RSA and RSA with MGF1 will work
        if (org.jcryptool.visual.sig.algorithm.Input.privateKeyJCTCA != null) {
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
    		initializeKeySelection();
        }
    }

    private void initializeHashMethodDependencies() {
    	switch (hashMethod) {
        case 0: // MD5: RSA
            rdo2.setEnabled(true);
            rdo1.setEnabled(false);
            rdo3.setEnabled(false);
            rdo4.setEnabled(false);
            if (signatureMethod == -1) {
                signatureMethod = 1;
                signatureMethodName = Messages.SignatureWizard_RSA;
            }
            break;
        case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
            rdo1.setEnabled(true);
            rdo2.setEnabled(true);
            rdo3.setEnabled(true);
            rdo4.setEnabled(true);
            if (signatureMethod == -1) {
                signatureMethod = 0;
                signatureMethodName = Messages.SignatureWizard_DSA;
            }
            break;
        case 2:
        case 3:
        case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
            rdo2.setEnabled(true);
            rdo3.setEnabled(true);
            rdo4.setEnabled(true);
            rdo1.setEnabled(false);
            if (signatureMethod == -1) {
                signatureMethod = 1;
                signatureMethodName = Messages.SignatureWizard_RSA;
            }
            break;
    	}
	}
    
    private void initializeSignatureMethodDependencies() {
        rdo1.setSelection(Input.s == 0);
        rdo2.setSelection(Input.s == 1);
        rdo3.setSelection(Input.s == 2);
        rdo4.setSelection(Input.s == 3);
    	switch (signatureMethod) {
    	case 0:
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_DSA + Messages.SignatureWizard_Usage2 
        			+ Messages.SignatureWizard_FurtherInfoInOnlineHelp);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            break;
    	case 1:
            txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_RSA + Messages.SignatureWizard_Usage2 
        			+ Messages.SignatureWizard_FurtherInfoInOnlineHelp);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            break;
    	case 2:
            txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_ECDSA + Messages.SignatureWizard_Usage2
        			+ Messages.SignatureWizard_FurtherInfoInOnlineHelp);
        	lblSelectAKey.setText(Messages.SignatureWizard_labelCurve);
            break;
    	case 3:
        	txtDescription.setText(Messages.SignatureWizard_Usage 
        			+ Messages.SignatureWizard_RSAandMGF1 + Messages.SignatureWizard_Usage2
        			+ Messages.SignatureWizard_FurtherInfoInOnlineHelp);
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
                if (currAlias.getClassName().equals(DSAPrivateKey.class.getName())) {
                    // Fill in keys
                    combo.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " + currAlias.getClassName()); //$NON-NLS-1$ //$NON-NLS-2$
                    keystoreitems.put(
                            currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " + currAlias.getClassName(), //$NON-NLS-1$ //$NON-NLS-2$
                            currAlias);
                } 
            } else if (signatureMethod == 1 || signatureMethod == 3) { // RSA or RSAandMGF1
                if (currAlias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                    // Fill in keys
                    combo.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                            + currAlias.getClassName());
                    keystoreitems
                            .put(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                                    + currAlias.getClassName(), currAlias);
                }
            } else if (signatureMethod == 2) { // ECDSA
                if (currAlias.getClassName().equals(ECPrivateKey.class.getName())) {
                    // Fill in keys
//                    combo.add(currAlias.getContactName() + " - " + currAlias.getKeyLength() + " bit - " //$NON-NLS-1$ //$NON-NLS-2$
                	combo.add(currAlias.getContactName() + " - " + currAlias.getClassName());
                    keystoreitems.put(currAlias.getContactName() + " - " + currAlias.getClassName(), currAlias);
                }
            }
        }
        
        //if combo is still empty at this point, there is no key pair for the given signature method in the JCT-Keystore
        if (combo.getItemCount() == 0) {
            page.setPageComplete(false);
            txtDescription.setText(txtDescription.getText() + NLS.bind(Messages.SignatureWizard_noKeysHint, signatureMethodName));
        } else {
    	    //select the last chosen key (if a previous key was chosen, before)
            boolean keySelected = false;
            if (alias != null) {
                for (String item : combo.getItems()) {
                	if (keystoreitems.get(item).toString().equals(alias.toString())) {
                		combo.select(combo.indexOf(item));
                		keySelected = true;
                	}
                }
            }
            
    	    //else select the first key item possible
    	    if (!keySelected) {
    		    combo.select(0);
    	    }
    	    alias = keystoreitems.get(combo.getText());
        	
    	    page.setPageComplete(true);
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
            signatureMethod = 0;
            signatureMethodName = Messages.SignatureWizard_DSA;
            lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
        } else {
            if (rdo2.getSelection()) {   
                signatureMethod = 1;
                signatureMethodName = Messages.SignatureWizard_RSA;
                lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            } else {
                if (rdo3.getSelection()) {
                    signatureMethod = 2;
                    signatureMethodName = Messages.SignatureWizard_ECDSA;
                    lblSelectAKey.setText(Messages.SignatureWizard_labelCurve);
                } else {
                    if (rdo4.getSelection()) {
                        signatureMethod = 3;
                        signatureMethodName = Messages.SignatureWizard_RSAandMGF1;
                        lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
                    }
                }
            }
        }
    	txtDescription.setText(Messages.SignatureWizard_Usage 
    			+ signatureMethodName + Messages.SignatureWizard_Usage2 
    			+ Messages.SignatureWizard_FurtherInfoInOnlineHelp);

        keystoreitems.clear();
        combo.removeAll();
        initializeKeySelection();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

   
}
