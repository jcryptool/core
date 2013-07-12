package org.jcryptool.visual.sig.ui.wizards;

import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
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
    private final static HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();

    private int method;
    private Menu menuSig;
    private MenuItem mntmSig;
    private Label lblSelectAKey;

    // Constructor
    public SignatureComposite(Composite parent, int style, int m, SignatureWizardPage p) {
        super(parent, style);
        method = m;
        page = p;
        // Draw the controls
        initialize();
    }

    /**
     * Draws all the controls of the composite
     */
    private void initialize() {
        grpSignatures = new Group(this, SWT.NONE);
        grpSignatures.setText(Messages.SignatureWizard_grpSignatures);
        grpSignatures.setBounds(10, 10, 406, 151);

        rdo1 = new Button(grpSignatures, SWT.RADIO);
        // rdo1.setSelection(true);
        rdo1.setBounds(10, 19, 118, 18);
        rdo1.setText(Messages.SignatureWizard_DSA);

        rdo2 = new Button(grpSignatures, SWT.RADIO);
        rdo2.setBounds(10, 43, 118, 18);
        rdo2.setText(Messages.SignatureWizard_RSA);

        rdo3 = new Button(grpSignatures, SWT.RADIO);
        rdo3.setBounds(10, 67, 118, 18);
        rdo3.setText(Messages.SignatureWizard_ECDSA);

        rdo4 = new Button(grpSignatures, SWT.RADIO);
        rdo4.setBounds(10, 91, 118, 18);
        rdo4.setText(Messages.SignatureWizard_RSAandMGF1);

        Group grpDescription = new Group(this, SWT.NONE);
        grpDescription.setText(Messages.SignatureWizard_grpDescription);
        grpDescription.setBounds(10, 220, 406, 255);

        txtDescription = new Text(grpDescription, SWT.WRAP | SWT.NO_BACKGROUND);
        txtDescription.setEditable(false);
        txtDescription.setBounds(10, 15, 382, 213);
        // txtDescription.setBackground(new Color(Display.getCurrent(), 220, 220, 220));
        txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
        txtDescription.setText(Messages.SignatureWizard_DSA_description);

        menuSig = new Menu(txtDescription);
        txtDescription.setMenu(menuSig);

        mntmSig = new MenuItem(menuSig, SWT.NONE);
        mntmSig.setText(Messages.Wizard_menu);
        // To select all text
        mntmSig.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                txtDescription.selectAll();
            }// end widgetSelected
        });

        // Add event listeners
        rdo1.addSelectionListener(this);
        rdo2.addSelectionListener(this);
        rdo3.addSelectionListener(this);
        rdo4.addSelectionListener(this);

        combo = new Combo(this, SWT.READ_ONLY);
        combo.setBounds(10, 185, 406, 22);
        combo.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                alias = keystoreitems.get(combo.getText());
                page.setPageComplete(true);
            }
        });

        lblSelectAKey = new Label(this, SWT.NONE);
        lblSelectAKey.setBounds(10, 167, 176, 14);
        lblSelectAKey.setText(Messages.SignatureWizard_labelKey);

        // Enable/disable methods
        switch (method) {
            case 0: // MD5: RSA
                rdo2.setEnabled(true);
                rdo1.setEnabled(false);
                rdo3.setEnabled(false);
                rdo4.setEnabled(false);

                rdo2.setSelection(true);
                rdo1.setSelection(false);

                txtDescription.setText(Messages.SignatureWizard_RSA_description);

                keyType = 1;
                // initializeKeySelection(1);

                break;
            case 1: // SHA1: RSA, DSA, ECDSA, RSA + MGF1
                rdo1.setEnabled(true);
                rdo2.setEnabled(true);
                rdo3.setEnabled(true);
                rdo4.setEnabled(true);

                rdo1.setSelection(true);
                rdo2.setSelection(false);

                txtDescription.setText(Messages.SignatureWizard_DSA_description);

                keyType = 0;
                // initializeKeySelection(0);
                break;
            case 2:
            case 3:
            case 4: // SHA256+: RSA, ECDSA, RSA + MGF1
                rdo2.setEnabled(true);
                rdo3.setEnabled(true);
                rdo4.setEnabled(true);
                rdo1.setEnabled(false);

                rdo2.setSelection(true);
                rdo1.setSelection(false);

                txtDescription.setText(Messages.SignatureWizard_RSA_description);

                keyType = 1;
                // initializeKeySelection(1);

                break;
        }// end switch

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
            // Move the description box up
            grpDescription.setBounds(10, 181, 406, 255);
            // Enable the finish button
            page.setPageComplete(true);
        } else {
            // Load the keys
            initializeKeySelection(keyType);
            // //Load the previous selection
            // switch (org.jcryptool.visual.sig.algorithm.Input.s) {
            // case 0: rdo1.setSelection(true); break;
            // case 1: rdo2.setSelection(true); break;
            // case 2: rdo3.setSelection(true); break;
            // case 3: rdo4.setSelection(true); break;
            // default: rdo1.setSelection(true); break;
            // }
            // //Fire an event to show the correct text. It doesn't matter which radio button triggers the event
            // //because it is checked in the event handler
            // rdo1.notifyListeners(SWT.Selection, new Event());
        }

        // Temporary
        /*
         * rdo4.setEnabled(false);
         */
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
            public
            void widgetSelected(SelectionEvent e) {
        // alias = keystoreitems.get(combo.getText());
        if (rdo1.getSelection()) {
            txtDescription.setText(Messages.SignatureWizard_DSA_description);
            // Store the chosen signature to keep the selected radio button for the next time the wizard is opened
            org.jcryptool.visual.sig.algorithm.Input.s = 0;
            // Clean up
            keystoreitems.clear();
            combo.removeAll();
            lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
            initializeKeySelection(0);
        } else {
            if (rdo2.getSelection()) {
                txtDescription.setText(Messages.SignatureWizard_RSA_description);
                org.jcryptool.visual.sig.algorithm.Input.s = 1;
                // Clean up
                keystoreitems.clear();
                combo.removeAll();
                lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
                if (org.jcryptool.visual.sig.algorithm.Input.privateKey == null) {
                    initializeKeySelection(1);
                }
            } else {
                if (rdo3.getSelection()) {
                    txtDescription.setText(Messages.SignatureWizard_ECDSA_description);
                    org.jcryptool.visual.sig.algorithm.Input.s = 2;
                    // Clean up
                    keystoreitems.clear();
                    combo.removeAll();
                    combo.add("Elliptic curve: ANSI X9.62 prime256v1 (256 bits)");
                    // combo.select(0);
                    lblSelectAKey.setText(Messages.SignatureWizard_labelCurve);
                    page.setPageComplete(false);
                } else {
                    if (rdo4.getSelection()) {
                        txtDescription.setText(Messages.SignatureWizard_RSAandMGF1_description);
                        org.jcryptool.visual.sig.algorithm.Input.s = 3;
                        // Clean up
                        keystoreitems.clear();
                        combo.removeAll();
                        lblSelectAKey.setText(Messages.SignatureWizard_labelKey);
                        initializeKeySelection(2);
                    }
                }
            }
        }
    }// end widgetSelected

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

    /**
     * Either loads available keys for the given method and fills them into the combo field or shows the given key from
     * JCTCA an disables the combo field
     * 
     * @param method The signature method (integer, 1=DSA, 2=RSA, 3=ECDSA)
     */
    private void initializeKeySelection(int method) {
        KeyStoreManager ksm = KeyStoreManager.getInstance();
        KeyStoreAlias alias;
        Enumeration<String> aliases = ksm.getAliases();
        while (aliases != null && aliases.hasMoreElements()) {
            alias = new KeyStoreAlias(aliases.nextElement());
            alias.getAliasString();
            if (method == 0) { // DSA
                if (alias.getClassName().equals(DSAPrivateKey.class.getName())) {
                    // Fill in keys
                    combo.add(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " + alias.getClassName());
                    keystoreitems.put(
                            alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " + alias.getClassName(),
                            alias);
                } // end if

            } else {
                if (method == 1) { // RSA
                    if (alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                        // Fill in keys
                        combo.add(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - "
                                + alias.getClassName());
                        keystoreitems
                                .put(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - "
                                        + alias.getClassName(), alias);
                    } // end if
                } else {
                    if (method == 2) { // RSAandMGF1
                        if (alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
                            // Fill in keys
                            combo.add(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - "
                                    + alias.getClassName());
                            keystoreitems.put(
                                    alias.getContactName() + " - " + alias.getKeyLength() + "Bit - "
                                            + alias.getClassName(), alias);
                        } // end if
                    } // end if
                }// end else
            }// end while
        }// end while
         // combo.select(0);
         // String s = combo.getText();
         // alias = keystoreitems.get(s);
        page.setPageComplete(false);
    }
}