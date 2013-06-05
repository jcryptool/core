package org.jcryptool.visual.sig.ui.wizards;

import java.security.KeyStoreException;
import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.exceptions.NoKeyStoreFileException;

import de.flexiprovider.core.dsa.DSAPrivateKey;
import de.flexiprovider.core.rsa.RSAPrivateCrtKey;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class SignatureComposite extends Composite implements SelectionListener{
	private Group grpSignatures;
	private Text txtDescription;
	private Button rdo1;
	private Button rdo2;
	private Button rdo3;
	private Button rdo4;
	private Combo combo;
	private KeyStoreAlias alias = null;
	private int keyType = 0;
	private final static HashMap<String, KeyStoreAlias> keystoreitems = new HashMap<String, KeyStoreAlias>();
	
	private int method;
	private Menu menuSig;
	private MenuItem mntmSig;
	
	//Constructor
	public SignatureComposite(Composite parent, int style, int m) {
		super(parent, style);
		method = m;
		//parent.setSize(600, 400); 
	    //Draw the controls
	    initialize();
	}
		
	/**
	* Draws all the controls of the composite
    */
	private void initialize() {
		grpSignatures = new Group(this, SWT.NONE);
		grpSignatures.setText(Messages.SignatureWizard_grpSignatures);
		grpSignatures.setBounds(10, 65, 300, 151);
		
		rdo1 = new Button(grpSignatures, SWT.RADIO);
		rdo1.setSelection(true);
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
		grpDescription.setBounds(10, 230, 300, 255);
		
		txtDescription = new Text(grpDescription, SWT.WRAP | SWT.NO_BACKGROUND);
		txtDescription.setEditable(false);
		txtDescription.setBounds(10, 15, 275, 213);
		//txtDescription.setBackground(new Color(Display.getCurrent(), 220, 220, 220));
		txtDescription.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		txtDescription.setText(Messages.SignatureWizard_DSA_description);
		
		menuSig = new Menu(txtDescription);
		txtDescription.setMenu(menuSig);
		
		mntmSig = new MenuItem(menuSig, SWT.NONE);
		mntmSig.setText(Messages.Wizard_menu);
		//To select all text
		mntmSig.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
		    }
		    public void widgetSelected(SelectionEvent e) {
		    	txtDescription.selectAll();
		    }//end widgetSelected
		});
		
		 //Add event listeners
	    rdo1.addSelectionListener(this);
	    rdo2.addSelectionListener(this);
	    rdo3.addSelectionListener(this);
	    rdo4.addSelectionListener(this);
	    
	    combo = new Combo(this, SWT.READ_ONLY);
	    combo.setBounds(10, 35, 300, 22);
	    combo.addSelectionListener(new SelectionListener() {
	    	public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				alias = keystoreitems.get(combo.getText());
			}
	    });
	    
	    Label lblSelectAKey = new Label(this, SWT.NONE);
	    lblSelectAKey.setBounds(10, 15, 176, 14);
	    lblSelectAKey.setText("Select a key");
	    
	    //Enable/disable methods
	    switch (method) {
	    	case 0: //MD5: RSA
	    		rdo2.setEnabled(true); 
	    		rdo1.setEnabled(false); 
	    		rdo3.setEnabled(false); 
	    		rdo4.setEnabled(false); 
	    		
	    		rdo2.setSelection(true); 
	    		rdo1.setSelection(false); 
	    		
	    		txtDescription.setText(Messages.SignatureWizard_RSA_description);
	    		
	    		keyType = 1;
	    		//initializeKeySelection(1); 
	    		
	    		break; 
	    	case 1: //SHA1: RSA, DSA, ECDSA, RSA + MGF1
	    		rdo1.setEnabled(true); 
	    		rdo2.setEnabled(true); 
	    		rdo3.setEnabled(true); 
	    		rdo4.setEnabled(true); 
	    		
	    		rdo1.setSelection(true); 
	    		rdo2.setSelection(false); 
	    		
	    		txtDescription.setText(Messages.SignatureWizard_DSA_description);
	    		
	    		keyType = 0;
	    		//initializeKeySelection(0); 
	    		break; 
	    	case 2:
	    	case 3:
	    	case 4: //SHA256+: RSA, ECDSA, RSA + MGF1
	    		rdo2.setEnabled(true); 
	    		rdo3.setEnabled(true); 
	    		rdo4.setEnabled(true); 
	    		rdo1.setEnabled(false);
	    		
	    		rdo2.setSelection(true); 
	    		rdo1.setSelection(false);
	    		
	    		txtDescription.setText(Messages.SignatureWizard_RSA_description);
	    		
	    		keyType = 1;
	    		//initializeKeySelection(1); 
	    		
	    		break; 
	    }//end switch
	    
	    //If called by JCT-CA only SHA-256 can be used! Therefore only ECDSA, RSA and RSA with MGF1 will work
	    if (org.jcryptool.visual.sig.algorithm.Input.privateKey != null) {
			rdo1.setEnabled(false);
			rdo1.setSelection(false);
			rdo2.setSelection(true);
			rdo3.setEnabled(false);
			rdo4.setEnabled(false);
			combo.setVisible(false);
			lblSelectAKey.setVisible(false);
			grpSignatures.setBounds(10, 10, 300, 151);
			grpDescription.setBounds(10,175,300,255);
		} else {
			//Load the keys
		    initializeKeySelection(keyType); 
		}
	    
	    //Temporary
	    /*
	    rdo3.setEnabled(false);
	    rdo4.setEnabled(false);
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
	public void widgetSelected(SelectionEvent e) {
		//alias = keystoreitems.get(combo.getText());
		if (rdo1.getSelection()) {
			txtDescription.setText(Messages.SignatureWizard_DSA_description);
			//Clean up
			keystoreitems.clear();
			combo.removeAll();
			initializeKeySelection(0);
		} else {
			if (rdo2.getSelection()) {
				txtDescription.setText(Messages.SignatureWizard_RSA_description);
				//Clean up
				keystoreitems.clear();
				combo.removeAll();
				initializeKeySelection(1);
			} else {
				if (rdo3.getSelection()) {
					txtDescription.setText(Messages.SignatureWizard_ECDSA_description);
					//Clean up
					keystoreitems.clear();
					combo.removeAll();
				} else {
					if (rdo4.getSelection()) {
						txtDescription.setText(Messages.SignatureWizard_RSAandMGF1_description);
						//Clean up
						keystoreitems.clear();
						combo.removeAll();
					}
				}
			}
		}
	}//end widgetSelected

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	/**
	 * Either loads available keys for the given method and fills them into the combo field or 
	 * shows the given key from JCTCA an disables the combo field
	 * 
	 * @param method The signature method (integer, 1=DSA, 2=RSA, 3=ECDSA)
	 */
	private void initializeKeySelection (int method) {
		KeyStoreManager ksm = KeyStoreManager.getInstance();
        try {
            ksm.loadKeyStore(KeyStorePlugin.getPlatformKeyStoreURI());
            KeyStoreAlias alias;
            Enumeration<String> aliases = ksm.getAliases();
            while (aliases != null && aliases.hasMoreElements()) {
                alias = new KeyStoreAlias(aliases.nextElement());
                alias.getAliasString();
                if (method == 0) { //DSA
	                if (alias.getClassName().equals(DSAPrivateKey.class.getName())) {
	                	//Fill in keys
	                	combo.add(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " + alias.getClassName());
	                	keystoreitems.put(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " + alias.getClassName(), alias);
	                } //end if
	              
                } else {
                	if (method == 1) { //RSA
                		if (alias.getClassName().equals(RSAPrivateCrtKey.class.getName())) {
    	                	//Fill in keys
    	                	combo.add(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " + alias.getClassName());
    	                	keystoreitems.put(alias.getContactName() + " - " + alias.getKeyLength() + "Bit - " + alias.getClassName(), alias);
    	                } //end if
                		
                	} else {
                		if (method == 2) { //ECDSA
                			//Generate Curve
                		}
                	}
                }
            }//end while
            combo.select(0);
        } catch (NoKeyStoreFileException e) {
            LogUtil.logError(e);
        } catch (KeyStoreException e) {
            LogUtil.logError(e);
        }
	}
}
