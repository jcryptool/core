// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.keystore.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.descriptors.NewEntryDescriptor;
import org.jcryptool.crypto.keystore.descriptors.interfaces.INewEntryDescriptor;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;
import org.jcryptool.crypto.keystore.keys.KeyType;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;

/**
 * @author t-kern
 * 
 */
public class NewSymmetricKeyWizardPage extends WizardPage implements Listener {
    private Group contactGroup = null;
    private Group algorithmGroup = null;
    private Group passwordGroup = null;
//    private Label contactDescriptionLabel = null;
    private Label contactNameLabel = null;
    private Combo contactNameCombo = null;
//    private Label algorithmDescriptionLabel = null;
    private Label algorithmLabel = null;
    private Combo algorithmCombo = null;
    private Button keyStrengthCheckBox = null;
//     private Label passwordDescriptionLabel = null;
    private Label enterPasswordLabel = null;
    private Text enterPasswordText = null;
    private Label confirmPasswordLabel = null;
    private Text confirmPasswordText = null;
    private Label keyStrengthLabel = null;
    private CCombo keyStrengthCCombo = null;
    private String keyType;

    protected NewSymmetricKeyWizardPage() {
        this(KeyStoreAlias.EVERYTHING_MATCHER);
    }

    public NewSymmetricKeyWizardPage(String keyType) {
        super("1", Messages.NewSymmetricKeyWizardPage_0,  //$NON-NLS-1$
        		ImageService.getImageDescriptor(KeyStorePlugin.PLUGIN_ID, "icons/48x48/kgpg_key1.png")); //$NON-NLS-1$
        setDescription(Messages.NewSymmetricKeyWizardPage_1);
        setPageComplete(false);
        this.keyType = keyType;
    }
    

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        LogUtil.logInfo("creating control"); //$NON-NLS-1$
        Composite pageComposite = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        pageComposite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        pageComposite.setLayoutData(gridData);

        // building the composite
        createContactGroup(pageComposite);
        createAlgorithmGroup(pageComposite);
        createPasswordGroup(pageComposite);

        // inititializing the composite
        initAlgorithmsCombo();
        initContactCombo();
        initKeyStrength();

        // register listeners
        registerListeners();

        pageComposite.setSize(350, 350);
        setControl(pageComposite);
    }

    private String getName(String displayedName) {
        if (displayedName.contains(",")) { //$NON-NLS-1$
            return displayedName.substring(0, displayedName.indexOf(",")); //$NON-NLS-1$
        } else if (displayedName.contains("(")) { //$NON-NLS-1$
            return displayedName.substring(0, displayedName.indexOf("(") - 1); //$NON-NLS-1$
        } else {
            return displayedName;
        }
    }

    private void initKeyStrength() {
        keyStrengthCheckBox.setSelection(true);
        keyStrengthCCombo.setEnabled(false);
        String tmp = getName(algorithmCombo.getText());
        showLengths(tmp);
    }

    private void initContactCombo() {
        int size = ContactManager.getInstance().getContactSize();
        String previousContactSelection = contactNameCombo.getText();
        if (size > 0) {
            List<String> contactNames = new ArrayList<String>();
            Iterator<Contact> it = ContactManager.getInstance().getContacts();
            Contact contact;
            while (it.hasNext()) {
                contact = it.next();

                String operation = ""; //$NON-NLS-1$
                if (algorithmCombo != null && algorithmCombo.getText().length() > 0) {
					operation = getName(algorithmCombo.getText());
				}
                String hasKey = contactHasKey(contact.getName(), algorithmCombo.getText());
                if (hasKey.length() > 0) {
                	continue;
				}
                contactNames.add(contact.getName());
            }

            String[] contactNamesArray = (String[]) contactNames.toArray(new String[] {});
            Arrays.sort(contactNamesArray);
            if (contactNamesArray.length == 0) {
				int contactnameCounter = 1;
				String contactname = Messages.NewSymmetricKeyWizardPage_17;
				String contactnameWithCounter = contactname + " " + contactnameCounter; //$NON-NLS-1$
				while (contactExists(contactnameWithCounter)) {
					contactnameCounter++;
					contactnameWithCounter = contactname + " " + contactnameCounter; //$NON-NLS-1$
				}
            	contactNamesArray = new String[] {contactnameWithCounter};
			}
            contactNameCombo.setItems(contactNamesArray);
            contactNameCombo.select(0);
            for (int i = 0; i < contactNamesArray.length; i++) {
				String cname = contactNamesArray[i];
				if (cname.equals(previousContactSelection)) {
            		contactNameCombo.select(i);
				}
			}
        } else {
            LogUtil.logInfo("No Contact"); //$NON-NLS-1$
        }
    }

    private boolean contactExists(String contactnameWithCounter) {
    	Iterator<Contact> contacts = ContactManager.getInstance().getContacts();
    	while (contacts.hasNext()) {
			Contact contact = contacts.next();
			if (contact.getName().equals(contactnameWithCounter)) {
				return true;
			}
		}
    	
    	return false;
	}

	private List<IMetaKeyGenerator> secretKeyGenerators;

    private void initAlgorithmsCombo() {
        secretKeyGenerators = AlgorithmsXMLManager.getInstance().getSecretKeyGenerators();
        Iterator<IMetaKeyGenerator> it = secretKeyGenerators.iterator();
        List<String> generators = new LinkedList<String>();
        while (it.hasNext()) {
            IMetaKeyGenerator current = it.next();
            String allNames = ""; //$NON-NLS-1$
            Iterator<String> namesIt = current.getNames().iterator();
            while (namesIt.hasNext()) {
                allNames += namesIt.next() + ", "; //$NON-NLS-1$
            }
            allNames = allNames.substring(0, allNames.length() - 2);

            String generatorId = allNames;
            if (current.getOID() != null) {
                generatorId += " (OID: " + current.getOID().getStringOID() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            }

            if (KeyStoreAlias.isOperationMatchingKeyId(generatorId, keyType)) {
                generators.add(generatorId);
            }
        }
        Collections.sort(generators);
        algorithmCombo.setItems(generators.toArray(new String[0]));
        algorithmCombo.select(0);
        algorithmCombo.setVisibleItemCount(10);
        algorithmCombo.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		initContactCombo();
        	}
		});
    }

    private void registerListeners() {
        algorithmCombo.addListener(SWT.Selection, this);
        keyStrengthCheckBox.addListener(SWT.Selection, this);
        enterPasswordText.addListener(SWT.Modify, this);
        confirmPasswordText.addListener(SWT.Modify, this);
    }

    Optional<String> lastValidMessage = Optional.of(""); //$NON-NLS-1$
    /**
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event) {
        if (event.widget.equals(keyStrengthCheckBox)) {
            LogUtil.logInfo("standard key strengh checkbox clicked"); //$NON-NLS-1$
            keyStrengthCheckBoxClicked();
        } else if (event.widget.equals(enterPasswordText) || event.widget.equals(confirmPasswordText)) {
            LogUtil.logInfo("one of the pw fields has been modified"); //$NON-NLS-1$
        } else if (event.widget.equals(algorithmCombo)) {
            String tmp = getName(algorithmCombo.getText());
            showLengths(tmp);
        }
        setPageComplete(isComplete());
    }
    
    private String contactHasKey(String contactname, String algorithmName) {
    	ArrayList<IKeyStoreAlias> privkeys = KeyStoreManager.getInstance().getAllPrivateKeys();
    	ArrayList<IKeyStoreAlias> pubkeys = KeyStoreManager.getInstance().getAllPublicKeys();
    	ArrayList<IKeyStoreAlias> secretkeys = KeyStoreManager.getInstance().getAllSecretKeys();
    	
    	for(IKeyStoreAlias key : secretkeys) {
    		if (! key.getContactName().equals(contactname)) {
				continue;
			}
    		String name = key.getOperation();
    		if (name.equals(algorithmName)) {
    			return "The selected contact already has a key of this type. Please select another contact."; //$NON-NLS-1$
			}
    	}
    	for(IKeyStoreAlias key : privkeys) {
    		if (! key.getContactName().equals(contactname)) {
				continue;
			}
    		String name = key.getOperation();
    		if (name.equals(algorithmName)) {
    			return "The selected contact already has a key of this type. Please select another contact."; //$NON-NLS-1$
			}
    	}
    	for(IKeyStoreAlias key : pubkeys) {
    		if (! key.getContactName().equals(contactname)) {
				continue;
			}
    		String name = key.getOperation();
    		if (name.equals(algorithmName)) {
    			return "The selected contact already has a key of this type. Please select another contact."; //$NON-NLS-1$
			}
    	}
    	return ""; //$NON-NLS-1$
    }

    private void showLengths(String name) {
        keyStrengthCCombo.removeAll();
        keyStrengthCCombo.setEditable(true);
        IMetaKeyGenerator keyGen = AlgorithmsXMLManager.getInstance().getSecretKeyGenerator(name);
        if (keyGen.getLengths() != null) {
            int defaultLength = keyGen.getLengths().getDefaultLength();
            if (keyGen.getLengths().getLengths() != null) {
                List<Integer> lengths = keyGen.getLengths().getLengths();
                Iterator<Integer> it = lengths.iterator();
                while (it.hasNext()) {
                    keyStrengthCCombo.add(String.valueOf(it.next()));
                }
                keyStrengthCCombo.setText(String.valueOf(defaultLength));
                keyStrengthCCombo.setEditable(false);
            } else if (keyGen.getLengths().getLowerBound() >= 0 && keyGen.getLengths().getUpperBound() > 0) {
                int lowerBound = keyGen.getLengths().getLowerBound();
                int upperBound = keyGen.getLengths().getUpperBound();
                String display = "(" + String.valueOf(lowerBound) + " - " + String.valueOf(upperBound) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                keyStrengthCCombo.add(display);
                keyStrengthCCombo.setText(String.valueOf(defaultLength));
            } else {
                keyStrengthCCombo.add(String.valueOf(defaultLength));
                keyStrengthCCombo.setText(String.valueOf(defaultLength));
                keyStrengthCCombo.setEditable(false);
            }
        }
        else
        {
        	keyStrengthCCombo.add("default"); //$NON-NLS-1$
        	keyStrengthCCombo.setEditable(false);
        	keyStrengthCCombo.setEnabled(true);
        	keyStrengthCCombo.setText("default"); //$NON-NLS-1$
        }
    }

    private void keyStrengthCheckBoxClicked() {
        boolean value = keyStrengthCheckBox.getSelection();
        LogUtil.logInfo("value: " + value); //$NON-NLS-1$
        if (!value) {
            keyStrengthCCombo.setEnabled(true);
        } else {
            keyStrengthCCombo.setEnabled(false);
        }
    }

    protected INewEntryDescriptor getKeyDescriptor() {
        LogUtil.logInfo("returning key descriptor"); //$NON-NLS-1$
        String algoName = getName(algorithmCombo.getText());
        int length = -1;
        try {
        	if (keyStrengthCCombo.getText() == "default") { //$NON-NLS-1$
        		length = -1;
			} else {
				int value = Integer.valueOf(keyStrengthCCombo.getText());
				length = value;
			}
        } catch (NumberFormatException e) {
        }
        return new NewEntryDescriptor(contactNameCombo.getText(), algoName, algorithmCombo.getText(), length,
                enterPasswordText.getText(), "PLACEHOLDER", KeyType.SECRETKEY); //$NON-NLS-1$
    }

    /**
     * Contains the "page finished" logic.
     * 
     * @return <code>true</code>, if the page is filled out completely
     */
    private boolean isComplete() {
        if (contactNameCombo.getText() != null) {
            LogUtil.logInfo("contactName: " + contactNameCombo.getText()); //$NON-NLS-1$
            // contact name is not null
            if (keyStrengthCheckBox.getSelection()) {
                // proceed with standard key length
                if (enterPasswordText.getText() != null && !enterPasswordText.getText().equals("") //$NON-NLS-1$
                        && enterPasswordText.getText().equals(confirmPasswordText.getText())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (keyStrengthCCombo.getText() != null) {
                    // key strength combo is not null
                    if (enterPasswordText.getText() != null && !enterPasswordText.getText().equals("") //$NON-NLS-1$
                            && enterPasswordText.getText().equals(confirmPasswordText.getText())) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /* ---------- just boring stuff beneath this point ---------- */

    /**
     * This method initializes contactGroup
     * 
     */
    private void createContactGroup(Composite parent) {
        GridData gridData5 = new GridData();
        gridData5.grabExcessVerticalSpace = true;
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.END;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.widthHint = 300;
        gridData4.verticalAlignment = GridData.CENTER;
        GridData gridData3 = new GridData();
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.grabExcessVerticalSpace = true;
        gridData3.horizontalSpan = 2;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        contactGroup = new Group(parent, SWT.NONE);
        contactGroup.setText(Messages.NewSymmetricKeyWizardPage_2);
        contactGroup.setLayout(gridLayout);
        contactGroup.setLayoutData(gridData);
//        contactDescriptionLabel = new Label(contactGroup, SWT.NONE);
//        contactDescriptionLabel.setText(Messages.NewSymmetricKeyWizardPage_3);
//        contactDescriptionLabel.setLayoutData(gridData3);
        contactNameLabel = new Label(contactGroup, SWT.NONE);
        contactNameLabel.setText(Messages.NewSymmetricKeyWizardPage_4);
        contactNameLabel.setLayoutData(gridData5);
        contactNameCombo = new Combo(contactGroup, SWT.BORDER);
        
        Label contactNameComboHint = new Label(contactGroup, SWT.WRAP);
        GridData ldata = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
//        ldata.widthHint = 200;
		contactNameComboHint.setLayoutData(ldata);
        contactNameComboHint.setText(Messages.NewSymmetricKeyWizardPage_16);

        contactNameCombo.setLayoutData(gridData4);
    }

    /**
     * This method initializes algorithmGroup
     * 
     */
    private void createAlgorithmGroup(Composite parent) {
        GridData gridData14 = new GridData();
        gridData14.grabExcessVerticalSpace = true;
        GridData gridData13 = new GridData();
        gridData13.grabExcessVerticalSpace = true;
        GridData gridData12 = new GridData();
        gridData12.horizontalAlignment = GridData.END;
        gridData12.grabExcessVerticalSpace = true;
        gridData12.widthHint = 200;
        gridData12.verticalAlignment = GridData.CENTER;
        GridData gridData8 = new GridData();
        gridData8.horizontalSpan = 2;
        gridData8.grabExcessVerticalSpace = true;
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = GridData.END;
        gridData7.grabExcessVerticalSpace = true;
        gridData7.widthHint = 200;
        gridData7.verticalAlignment = GridData.CENTER;
        GridData gridData6 = new GridData();
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.grabExcessVerticalSpace = true;
        gridData6.horizontalSpan = 2;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        GridData gridData1 = new GridData();
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.verticalAlignment = GridData.FILL;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.horizontalAlignment = GridData.FILL;
        algorithmGroup = new Group(parent, SWT.NONE);
        algorithmGroup.setText(Messages.NewSymmetricKeyWizardPage_5);
        algorithmGroup.setLayout(gridLayout1);
        algorithmGroup.setLayoutData(gridData1);
//        algorithmDescriptionLabel = new Label(algorithmGroup, SWT.NONE);
//        algorithmDescriptionLabel.setText(Messages.NewSymmetricKeyWizardPage_6);
//        algorithmDescriptionLabel.setLayoutData(gridData6);
        algorithmLabel = new Label(algorithmGroup, SWT.NONE);
        algorithmLabel.setText(Messages.NewSymmetricKeyWizardPage_7);
        algorithmLabel.setLayoutData(gridData13);
        algorithmCombo = new Combo(algorithmGroup, SWT.BORDER | SWT.READ_ONLY);
        algorithmCombo.setLayoutData(gridData7);
        keyStrengthCheckBox = new Button(algorithmGroup, SWT.CHECK);
        keyStrengthCheckBox.setText(Messages.NewSymmetricKeyWizardPage_8);
        keyStrengthCheckBox.setLayoutData(gridData8);
        keyStrengthLabel = new Label(algorithmGroup, SWT.NONE);
        keyStrengthLabel.setText(Messages.NewSymmetricKeyWizardPage_9);
        keyStrengthLabel.setLayoutData(gridData14);
        keyStrengthCCombo = new CCombo(algorithmGroup, SWT.BORDER);
        keyStrengthCCombo.setLayoutData(gridData12);
    }

    /**
     * This method initializes passwordGroup
     * 
     */
    private void createPasswordGroup(Composite parent) {
        GridData gridData16 = new GridData();
        gridData16.grabExcessVerticalSpace = true;
        GridData gridData15 = new GridData();
        gridData15.grabExcessVerticalSpace = true;
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.END;
        gridData11.grabExcessVerticalSpace = true;
        gridData11.widthHint = 200;
        gridData11.verticalAlignment = GridData.CENTER;
        GridData gridData10 = new GridData();
        gridData10.horizontalAlignment = GridData.END;
        gridData10.grabExcessVerticalSpace = true;
        gridData10.widthHint = 200;
        gridData10.verticalAlignment = GridData.CENTER;
        GridData gridData9 = new GridData();
        gridData9.horizontalSpan = 2;
        gridData9.grabExcessVerticalSpace = true;
        gridData9.grabExcessHorizontalSpace = true;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 2;
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.verticalAlignment = GridData.FILL;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.horizontalAlignment = GridData.FILL;
        passwordGroup = new Group(parent, SWT.NONE);
        passwordGroup.setText(Messages.NewSymmetricKeyWizardPage_10);
        passwordGroup.setLayout(gridLayout2);
        passwordGroup.setLayoutData(gridData2);
//        passwordDescriptionLabel = new Label(passwordGroup, SWT.NONE);
//        passwordDescriptionLabel.setText(Messages.NewSymmetricKeyWizardPage_11);
//        passwordDescriptionLabel.setLayoutData(gridData9);
        enterPasswordLabel = new Label(passwordGroup, SWT.NONE);
        enterPasswordLabel.setText(Messages.NewSymmetricKeyWizardPage_12);
        enterPasswordLabel.setLayoutData(gridData15);
        enterPasswordText = new Text(passwordGroup, SWT.BORDER | SWT.PASSWORD);
        enterPasswordText.setLayoutData(gridData10);
        confirmPasswordLabel = new Label(passwordGroup, SWT.NONE);
        confirmPasswordLabel.setText(Messages.NewSymmetricKeyWizardPage_13);
        confirmPasswordLabel.setLayoutData(gridData16);
        confirmPasswordText = new Text(passwordGroup, SWT.BORDER | SWT.PASSWORD);
        confirmPasswordText.setLayoutData(gridData11);
    }

}
