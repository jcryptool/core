// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.integrator;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.util.ui.HexTextbox;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaPaddingScheme;
import org.jcryptool.crypto.flexiprovider.keystore.KeyStoreHelper;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.keys.KeyType;

/**
 * The wizard page for the FlexiProvider ciphers.
 *
 * @author mwalthart
 * @author Holger Friedrich (side effects of adding support for Eclipse Commands:  some constants
 * that used to be in the IntegratorAction class are now in IntegratorHandler)
 */
public class IntegratorWizardPage extends WizardPage {

    private static final String KEY_FROM_KEYSTORE_MODE_CREATED_KEY = "createdKey"; //$NON-NLS-1$
    private static final String KEY_FROM_KEYSTORE_MODE_CREATED_PAIR = "createdPair"; //$NON-NLS-1$
    private static final String KEY_FROM_KEYSTORE_MODE_SELECT = "select"; //$NON-NLS-1$
    // Key-source group
    private boolean useCustomKey = false;
    private boolean showKeySourceGroup;
    private Button useCustomKeyButton;
    private Button keyFromKeystoreButton;

    // Custom key group
    private Group keyByHandGroup;
    private HexTextbox customKeyTextbox;
    private int[] validKeyLengths;

    // Key from keystore group
    private Group keyFromKeystoreGroup;
    private KeyStoreAlias keyStoreAlias;
    private Vector<KeyStoreAlias> publicKeyMap;
    private Vector<KeyStoreAlias> privateKeyMap;

    private boolean showOperationGroup;
    private boolean showPaddingGroup;
    private String showKeyGroup;
    private boolean showSignatureGroup;
    private boolean showRandomGroup;
    private int showMessageDigestGroup;
    private boolean openFile = true;
    private boolean encrypt = true;
    private Button encryptButton;
    private Button decryptButton;

    private Button chooseFile;
    private Text signatureText;
    private IMetaPaddingScheme padding;
    private IMetaMode mode;
    private String signature;
    private int random;

    private Combo keyCombo;
    private Combo paddingCombo;
    private Combo modeCombo;

    private String CHOOSE_SIGNATURE_FILE_LABEL = Messages.getString("DummyWizardPage.0"); //$NON-NLS-1$
    private Text alphabet_text;
    protected byte[][] alphabet;
    protected boolean alphabet_doFilter;
    private Button alphabet_text_button;
    private Button alphabet_range_button;
    private Text alphabet_range_1;
    private Text alphabet_range_2;
    private String ALPHABET_SEPERATION_CHAR = " , "; //$NON-NLS-1$
    private Button alphabet_padding_button;
    private Text messageDigest_text;
    private String expectedChecksum;
    private Group paddingGroup;
    private Vector<KeyStoreAlias> comboKeyMap;
    private int algorithmType;

    /**
     * constructor
     *
     * @param page_title the page title
     * @param title the wizard title
     * @param header_description the header description
     * @param main_description the main/body description
     * @param showOperationGroup show/hide operation group
     * @param showPaddingGroup show/hide padding group
     * @param showKeyGroup show/hide key group
     * @param showSignatureGroup show/hide signature group
     * @param showMessageDigestGroup show/hide signature group
     * @param algorithmType
     */
    public IntegratorWizardPage(String page_title, String title, String header_description, boolean showOperationGroup,
            boolean showPaddingGroup, String showKeyGroup, boolean showKeySourceGroup, int[] validKeyLengths,
            boolean showSignatureGroup, boolean showRandomGroup, int showMessageDigestGroup, int algorithmType) {
        super(page_title, title, null);
        setMessage(header_description);
        this.showOperationGroup = showOperationGroup;
        this.showKeyGroup = showKeyGroup;
        this.showKeySourceGroup = showKeySourceGroup;
        this.validKeyLengths = validKeyLengths;
        this.showPaddingGroup = showPaddingGroup;
        this.showSignatureGroup = showSignatureGroup;
        this.showRandomGroup = showRandomGroup;
        this.showMessageDigestGroup = showMessageDigestGroup;
        this.algorithmType = algorithmType;

        publicKeyMap = new Vector<KeyStoreAlias>();
        privateKeyMap = new Vector<KeyStoreAlias>();
        comboKeyMap = new Vector<KeyStoreAlias>();
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
//         parent.setLayout(new GridLayout());

        masterComp = new Composite(parent, SWT.NONE);
        masterComp.setLayout(new GridLayout(1, true));
        GridData masterLData = new GridData(SWT.FILL, SWT.FILL, true, true);
        masterLData.heightHint = calculateHeightHintForPage();
        masterComp.setLayoutData(masterLData);

        if (showOperationGroup)
            createOperationGroup(masterComp);

        if (showKeySourceGroup) {
            createKeySourceSelectionGroup(masterComp);
            createKeyByHandGroup(masterComp);
        }
        encrypt = true;

        if (showKeyGroup != null && !showKeyGroup.equals("")) //$NON-NLS-1$
            createKeyFromKeystoreGroup(masterComp);
        if (showPaddingGroup)
            createPaddingGroup(masterComp);
        if (showSignatureGroup)
            createSignatureGroup(masterComp);
        if (showRandomGroup)
            createRandomGroup(masterComp);

        if (showMessageDigestGroup > 0) {
            createMessageDigestHintGroup(masterComp);
        }
        // This group is rather unnecessary and confusing
        // if (showMessageDigestGroup > 0)
        // createMessageDigestGroup(masterComp);
        showMessageDigestGroup = 0;

        if (showKeySourceGroup) {
            setKeySource(false);
        }

        setControl(masterComp);
        setOperationMode(true);
        calcAndSetPageCompletion();

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(getControl(), IntegratorPlugin.PLUGIN_ID + "." + getTitle() + "Wizard"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private void createMessageDigestHintGroup(Composite masterComp) {
        Label labelDigestHint = new Label(masterComp, SWT.WRAP);
        GridData labelDigestHintLData = new GridData(SWT.CENTER, SWT.BEGINNING, true, false);
        labelDigestHintLData.verticalIndent = 40;
        labelDigestHint.setLayoutData(labelDigestHintLData);
        labelDigestHint.setText(Messages.getString("IntegratorWizardPage.noFurtherInputNeeded")); //$NON-NLS-1$
        labelDigestHint.setAlignment(SWT.CENTER);
    }

    private int calculateHeightHintForPage() {
        return 300;
    }

    /**
     * Returns <code>true</code>, if the desired operation is <i>Encrypt</i>.
     *
     * @return <code>true</code>, if the desired operation is <i>Encrypt</i>
     */
    public boolean encrypt() {
        return encrypt;
    }

    public void calcAndSetPageCompletion() {
        boolean signatureGroupCondition = true;
        if (showSignatureGroup) {
        	if (signature() == null) {
				signatureGroupCondition = false;
        	} else {
				signatureGroupCondition = true;
			}
        }
        if (! signatureGroupCondition) {
        	setPageComplete(false);
        	return;
		}
        
        // Anm. simlei: below is the original content of the method which fell mighty short to address the whole picture.
        // the above is considered a hack, which couldn't be done better with sensible effort
        setPageComplete(false);
        if ((showKeyGroup == null || useCustomKey)) {
            setPageComplete(true);
        }
        if ((showKeyGroup != null && keyStoreAlias != null)) {
            setPageComplete(true);
        }
    }

    /**
     * This method initializes keyselection group (custom key or key from keystore)
     *
     */
    private void createKeySourceSelectionGroup(Composite parent) {
        GridData keystoreButtonGridData = new GridData();
        keystoreButtonGridData.horizontalAlignment = GridData.FILL;
        keystoreButtonGridData.grabExcessHorizontalSpace = true;
        keystoreButtonGridData.verticalAlignment = GridData.CENTER;

        GridLayout keySelectionGridLayout = new GridLayout();
        keySelectionGridLayout.numColumns = 2;

        Group keySelectionGroup = new Group(parent, SWT.NONE);
        keySelectionGroup.setLayoutData(keystoreButtonGridData);
        keySelectionGroup.setLayout(keySelectionGridLayout);
        keySelectionGroup.setText(Messages.getString("KeySelectionGroup.KeySource")); //$NON-NLS-1$

        Listener sourceBtnListener = new Listener() {
            // @Override
            public void handleEvent(Event event) {
                setKeySource(event.widget == useCustomKeyButton);
            }
        };

        useCustomKeyButton = new Button(keySelectionGroup, SWT.RADIO);
        useCustomKeyButton.setText(Messages.getString("KeySelectionGroup.CustomKey")); //$NON-NLS-1$
        useCustomKeyButton.setLayoutData(keystoreButtonGridData);
        useCustomKeyButton.setSelection(true);
        useCustomKeyButton.addListener(SWT.Selection, sourceBtnListener);

        keyFromKeystoreButton = new Button(keySelectionGroup, SWT.RADIO);
        keyFromKeystoreButton.setText(Messages.getString("KeySelectionGroup.KeyFromKeystore")); //$NON-NLS-1$
        keyFromKeystoreButton.setLayoutData(keystoreButtonGridData);
        keyFromKeystoreButton.addListener(SWT.Selection, sourceBtnListener);
    }

    protected void setKeySource(boolean useCustomKey) {
        this.useCustomKey = useCustomKey;
        hideObject(keyByHandGroup, !useCustomKey);
        hideObject(keyFromKeystoreGroup, useCustomKey);

        useCustomKeyButton.setSelection(useCustomKey);
        keyFromKeystoreButton.setSelection(!useCustomKey);
        calcAndSetPageCompletion();
    }

    private void setOperationMode(boolean encryptMode) {
        this.encrypt = encryptMode;

        if (createNewKeyButton != null) {
            if (algorithmType != IntegratorHandler.TYPE_SIGNATURE) {
                createNewKeyButton.setEnabled(encryptMode);
            } else {
                createNewKeyButton.setEnabled(!encryptMode);
            }
        }

        if (showKeyGroup != null)
            refreshKeysFromKeystore(keyStoreAlias);

        if (keyFromKeystoreMode.equals(KEY_FROM_KEYSTORE_MODE_CREATED_PAIR)) {
            setAliasFromKeyPairAndCombo();
        }

        if (showMessageDigestGroup > 0) {
            messageDigest_text.setEnabled(!encryptMode);
        }

        if (encryptMode) {
            if (showSignatureGroup) {
                CHOOSE_SIGNATURE_FILE_LABEL = Messages.getString("DummyWizardPage.6"); //$NON-NLS-1$
                openFile = true;
                signatureText.setText(CHOOSE_SIGNATURE_FILE_LABEL);
                chooseFile.setText(Messages.getString("DummyWizardPage.20")); //$NON-NLS-1$
                setPageComplete(false);
            }
        } else {
            if (showSignatureGroup) {
                CHOOSE_SIGNATURE_FILE_LABEL = Messages.getString("DummyWizardPage.9"); //$NON-NLS-1$
                openFile = false;
                signatureText.setText(CHOOSE_SIGNATURE_FILE_LABEL);
                chooseFile.setText(Messages.getString("IntegratorWizardPage.4")); //$NON-NLS-1$
                setPageComplete(false);
            }
        }

        calcAndSetPageCompletion();
    }

    /**
     * This method initializes operationGroup
     *
     */
    private void createOperationGroup(Composite parent) {
        GridData encryptButtonGridData = new GridData();
        encryptButtonGridData.horizontalAlignment = GridData.FILL;
        encryptButtonGridData.grabExcessHorizontalSpace = true;
        encryptButtonGridData.verticalAlignment = GridData.CENTER;

        GridData decryptButtonGridData = new GridData();
        decryptButtonGridData.horizontalAlignment = GridData.FILL;
        decryptButtonGridData.grabExcessHorizontalSpace = true;
        decryptButtonGridData.verticalAlignment = GridData.CENTER;

        GridLayout operationGroupGridLayout = new GridLayout();
        operationGroupGridLayout.numColumns = 2;

        GridData operationGroupGridData = new GridData();
        operationGroupGridData.horizontalAlignment = GridData.FILL;
        operationGroupGridData.grabExcessHorizontalSpace = true;
        operationGroupGridData.verticalAlignment = GridData.FILL;

        Group operationGroup = new Group(parent, SWT.NONE);
        operationGroup.setLayoutData(operationGroupGridData);
        operationGroup.setLayout(operationGroupGridLayout);
        operationGroup.setText(Messages.getString("DummyWizardPage.3")); //$NON-NLS-1$

        encryptButton = new Button(operationGroup, SWT.RADIO);
        encryptButton.setText(Messages.getString("DummyWizardPage.5")); //$NON-NLS-1$
        if (showSignatureGroup)
            encryptButton.setText(Messages.getString("DummyWizardPage.4")); //$NON-NLS-1$
        if (showMessageDigestGroup > 0)
            encryptButton.setText(Messages.getString("IntegratorWizardPage.0")); //$NON-NLS-1$
        encryptButton.setLayoutData(encryptButtonGridData);
        encryptButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if ((((Button) event.widget).getSelection()) && (!encrypt)) {
                    setOperationMode(true);
                }
            }
        });
        encryptButton.setSelection(true);

        decryptButton = new Button(operationGroup, SWT.RADIO);
        decryptButton.setText(Messages.getString("DummyWizardPage.8")); //$NON-NLS-1$
        if (showSignatureGroup)
            decryptButton.setText(Messages.getString("DummyWizardPage.7")); //$NON-NLS-1$
        if (showMessageDigestGroup > 0)
            decryptButton.setText(Messages.getString("IntegratorWizardPage.1")); //$NON-NLS-1$

        decryptButton.setLayoutData(decryptButtonGridData);
        decryptButton.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (((Button) event.widget).getSelection() && (encrypt)) {
                    setOperationMode(false);
                }
            }
        });
    }

    String keyFromKeystoreMode = KEY_FROM_KEYSTORE_MODE_SELECT; // other possible values:
                                                                // KEY_FROM_KEYSTORE_MODE_CREATED_KEY,
                                                                // KEY_FROM_KEYSTORE_MODE_CREATED_PAIR;
    private Button createNewKeyButton;
    private Composite masterComp;
    private NewKeyComposite createdKeyViewer;
    private Composite normalKeyFromKeystoreComposite;
    private Composite showcaseKeyFromKeystoreComposite;
    private KeyStoreAlias createdKeyPairAlias;
    private String buttonTextBeforeJobrunningMsg;

    /**
     * This method initializes the 'Key from keystore'-group
     *
     */
    private void createKeyFromKeystoreGroup(Composite parent) {
        GridLayout keyGroupGridLayout = new GridLayout();
        keyGroupGridLayout.numColumns = 1;

        GridData keyGroupGridData = new GridData();
        keyGroupGridData.horizontalAlignment = GridData.FILL;
        keyGroupGridData.grabExcessHorizontalSpace = true;
        keyGroupGridData.verticalAlignment = GridData.BEGINNING;

        keyFromKeystoreGroup = new Group(parent, SWT.NONE);
        keyFromKeystoreGroup.setLayoutData(keyGroupGridData);
        keyFromKeystoreGroup.setLayout(keyGroupGridLayout);
        keyFromKeystoreGroup.setText(Messages.getString("KeyFromKeystore.Title")); //$NON-NLS-1$

        normalKeyFromKeystoreComposite = new Composite(keyFromKeystoreGroup, SWT.NONE);
        GridLayout normalKeyFromKeystoreCompositeLayout = new GridLayout(2, false);
        normalKeyFromKeystoreCompositeLayout.marginWidth = 0;
        normalKeyFromKeystoreCompositeLayout.marginHeight = 0;
        normalKeyFromKeystoreComposite.setLayout(normalKeyFromKeystoreCompositeLayout);
        normalKeyFromKeystoreComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // Button btnKeystoreGrp1 = new Button(keyFromKeystoreGroup, SWT.RADIO);
        // btnKeystoreGrp1.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
        // btnKeystoreGrp1.setText("Schlüsselspeicher")

        Label label = new Label(normalKeyFromKeystoreComposite, SWT.WRAP);
        label.setText(Messages.getString("DummyWizardPage.17")); //$NON-NLS-1$
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        keyCombo = new Combo(normalKeyFromKeystoreComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        keyCombo.setLayoutData(gridData);
        keyCombo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                if (comboKeyMap.isEmpty())
                    return;
                keyStoreAlias = comboKeyMap.get(keyCombo.getSelectionIndex());
                calcAndSetPageCompletion();
            }
        });

        refreshKeysFromKeystore(null);

        Label createNewKeyLabel = new Label(normalKeyFromKeystoreComposite, SWT.NONE);
        createNewKeyLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
        createNewKeyLabel.setText(Messages.getString("IntegratorWizardPage.or")); //$NON-NLS-1$

        Composite createNewKeyComposite = new Composite(normalKeyFromKeystoreComposite, SWT.NONE);
        GridLayout createNewCompositeLayout = new GridLayout(1, false);
        createNewCompositeLayout.marginWidth = 0;
        createNewCompositeLayout.marginHeight = 0;
        createNewKeyComposite.setLayout(createNewCompositeLayout);
        createNewKeyComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Image btnImg = isAlgorithmTypeAsymmetric() ? 
        		ImageService.getImage(KeyStorePlugin.PLUGIN_ID, "/icons/16x16/kgpg_key2.png") : //$NON-NLS-1$
        		ImageService.getImage(KeyStorePlugin.PLUGIN_ID, "/icons/16x16/kgpg_key1.png"); //$NON-NLS-1$
        String newKeyBtnLabel = isAlgorithmTypeAsymmetric() ? Messages
                .getString("IntegratorWizardPage.createNewKeypairButton") : //$NON-NLS-1$
                Messages.getString("IntegratorWizardPage.newSymmetricKeyButtonlabel"); //$NON-NLS-1$
        String newKeyBtnTooltip = isAlgorithmTypeAsymmetric() ? Messages
                .getString("IntegratorWizardPage.willRemainInKeystore") : //$NON-NLS-1$
                Messages.getString("IntegratorWizardPage.newSymmetricKeyButtontip"); //$NON-NLS-1$
        createNewKeyButton = new Button(createNewKeyComposite, SWT.NONE);
        createNewKeyButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1));
        createNewKeyButton.setImage(btnImg); //$NON-NLS-1$
        createNewKeyButton.setText(newKeyBtnLabel);
        createNewKeyButton.setToolTipText(newKeyBtnTooltip);
        createNewKeyButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // preAddContacts = KeyStoreManager.getInstance().
                if (algorithmType == IntegratorHandler.TYPE_ASYMMETRIC_BLOCK
                        || algorithmType == IntegratorHandler.TYPE_ASYMMETRIC_HYBRID) {
                    makeNewKeypair();
                } else if (algorithmType == IntegratorHandler.TYPE_CIPHER
                        || algorithmType == IntegratorHandler.TYPE_CIPHER_BLOCK
                        || algorithmType == IntegratorHandler.TYPE_MESSAGE_AUTHTIFICATION_CODE) {
                    makeNewKey();
                }

            }
        });

        // Button createSecretKeyButton = new Button(createNewKeyComposite, SWT.NONE);
        // createSecretKeyButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        // createSecretKeyButton.setImage(KeyStorePlugin.getImageDescriptor("/icons/16x16/kgpg_key1.png").createImage());
        // createSecretKeyButton.setText("new secret key");
        // createSecretKeyButton.setEnabled(false);

        showcaseKeyFromKeystoreComposite = new Composite(keyFromKeystoreGroup, SWT.NONE);
        GridLayout showcaseKeyFromKeystoreCompositeLayout = new GridLayout(1, false);
        showcaseKeyFromKeystoreCompositeLayout.marginWidth = 0;
        showcaseKeyFromKeystoreCompositeLayout.marginHeight = 0;
        showcaseKeyFromKeystoreComposite.setLayout(showcaseKeyFromKeystoreCompositeLayout);
        showcaseKeyFromKeystoreComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        Label showcaseLabel = new Label(showcaseKeyFromKeystoreComposite, SWT.WRAP);
        showcaseLabel.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        showcaseLabel.setText(Messages.getString("IntegratorWizardPage.youChoseNewKeyLabel")/*+"\nRemove it for selecting an already existing key."*/); //$NON-NLS-1$

        // createdKeyViewer = new NewKeyComposite(showcaseKeyFromKeystoreComposite);
        // createdKeyViewer.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));

        setKeyForShowcase(null);
        if (!(isAlgorithmTypeAsymmetric() || isAlgorithmTypeSymmetric()
                || algorithmType == IntegratorHandler.TYPE_MESSAGE_AUTHTIFICATION_CODE || algorithmType == IntegratorHandler.TYPE_SIGNATURE)) {
            hideObject(createNewKeyLabel, true);
            hideObject(createNewKeyComposite, true);
        }
    }

    private void enableControls() {
        if (createNewKeyButton != null)
            createNewKeyButton.setEnabled(true);
        if (keyCombo != null)
            keyCombo.setEnabled(true);
        if (encryptButton != null)
            encryptButton.setEnabled(true);
        if (decryptButton != null)
            decryptButton.setEnabled(true);
    }

    private void disableControls() {
        if (createNewKeyButton != null)
            createNewKeyButton.setEnabled(false);
        if (keyCombo != null)
            keyCombo.setEnabled(false);
        if (encrypt && encryptButton != null) {
            encryptButton.setEnabled(true);
            if (decryptButton != null)
                decryptButton.setEnabled(false);
        }
        if (!encrypt && decryptButton != null) {
            decryptButton.setEnabled(true);
            if (encryptButton != null)
                encryptButton.setEnabled(false);
        }

    }

    protected void makeNewKey() {
        Job[] preJobs = Job.getJobManager().find(KeyStoreHelper.KEYSTOREHELPER_FAMILY);
        int preJobCount = preJobs.length;
        KeyStoreHelper.makeSymmetricKeyByWizard(showKeyGroup).addObserver(new Observer() {
            public void update(Observable o, final Object arg) {
                if (arg != null) {
                    keyFromKeystoreGroup.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            KeyStoreAlias ref = (KeyStoreAlias) arg;
                            setKeyForShowcase(ref);
                        }
                    });
                }
            }
        });

        Job[] jobs = Job.getJobManager().find(KeyStoreHelper.KEYSTOREHELPER_FAMILY);
        if (jobs.length > preJobCount) {
            createNewKeyButton.getDisplay().syncExec(new Runnable() {
                public void run() {
                    buttonTextBeforeJobrunningMsg = createNewKeyButton.getText();
                    createNewKeyButton.setText(Messages.getString("IntegratorWizardPage.generatingButtonHint")); //$NON-NLS-1$
                    disableControls();
                }

            });
            Job job = jobs[jobs.length - 1];
            IJobChangeListener listener = new IJobChangeListener() {
                public void sleeping(IJobChangeEvent event) {
                }

                public void done(IJobChangeEvent event) {
                    createNewKeyButton.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            createNewKeyButton.setText(buttonTextBeforeJobrunningMsg);
                            enableControls();
                        }
                    });
                }

                public void awake(IJobChangeEvent event) {
                }

                public void aboutToRun(IJobChangeEvent event) {
                }

                public void running(IJobChangeEvent event) {
                }

                public void scheduled(IJobChangeEvent event) {
                }
            };
            if (job.getState() != Job.NONE)
                job.addJobChangeListener(listener);
            else {
                listener.done(null);
            }
        }
    }

    protected void makeNewKeypair() {
        Job[] preJobs = Job.getJobManager().find(KeyStoreHelper.KEYSTOREHELPER_FAMILY);
        int preJobCount = preJobs.length;
        KeyStoreHelper.makeKeyPairByWizard(showKeyGroup).addObserver(new Observer() {
            public void update(Observable o, final Object arg) {
                if (arg != null) {
                    keyFromKeystoreGroup.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            KeyStoreAlias ref = (KeyStoreAlias) arg;
                            setKeyForShowcase(ref);
                        }
                    });
                }
            }
        });

        Job[] jobs = Job.getJobManager().find(KeyStoreHelper.KEYSTOREHELPER_FAMILY);
        if (jobs.length > preJobCount) {
            createNewKeyButton.getDisplay().syncExec(new Runnable() {
                public void run() {
                    buttonTextBeforeJobrunningMsg = createNewKeyButton.getText();
                    createNewKeyButton.setText(Messages.getString("IntegratorWizardPage.generatingButtonHint")); //$NON-NLS-1$
                    disableControls();
                }
            });
            Job job = jobs[jobs.length - 1];
            IJobChangeListener listener = new IJobChangeListener() {
                public void sleeping(IJobChangeEvent event) {
                }

                public void done(IJobChangeEvent event) {
                    createNewKeyButton.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            createNewKeyButton.setText(buttonTextBeforeJobrunningMsg);
                            enableControls();
                        }
                    });
                }

                public void awake(IJobChangeEvent event) {
                }

                public void aboutToRun(IJobChangeEvent event) {
                }

                public void running(IJobChangeEvent event) {
                }

                public void scheduled(IJobChangeEvent event) {
                }
            };
            if (job.getState() != Job.NONE)
                job.addJobChangeListener(listener);
            else {
                listener.done(null);
            }
        }
    }

    private boolean isAlgorithmTypeAsymmetric() {
        return algorithmType == IntegratorHandler.TYPE_ASYMMETRIC_BLOCK
                || algorithmType == IntegratorHandler.TYPE_ASYMMETRIC_HYBRID;
    }

    private boolean isAlgorithmTypeSymmetric() {
        return algorithmType == IntegratorHandler.TYPE_CIPHER || algorithmType == IntegratorHandler.TYPE_CIPHER_BLOCK;
    }

    private void setKeyForShowcase(KeyStoreAlias publicKeyAlias) {
        // String oldMode = keyFromKeystoreMode;
        this.createdKeyPairAlias = publicKeyAlias;
        if (publicKeyAlias != null) {
            if (createdKeyViewer != null) {
                if (!createdKeyViewer.isDisposed()) {
                    createdKeyViewer.dispose();
                }
            }
            if (isAlgorithmTypeAsymmetric()) {
                keyFromKeystoreMode = KEY_FROM_KEYSTORE_MODE_CREATED_PAIR;
                createdKeyViewer = new NewKeyPairComposite(showcaseKeyFromKeystoreComposite, publicKeyAlias);
            } else /* if(isAlgorithmTypeSymmetric()) */{
                keyFromKeystoreMode = KEY_FROM_KEYSTORE_MODE_CREATED_KEY;
                createdKeyViewer = new NewKeyComposite(showcaseKeyFromKeystoreComposite, publicKeyAlias);
            }

            createdKeyViewer.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
            createdKeyViewer.getRemoveObserver().addObserver(new Observer() {
                public void update(Observable o, Object arg) {
                    keyFromKeystoreGroup.getDisplay().syncExec(new Runnable() {
                        public void run() {
                            setKeyForShowcase(null);
                        }
                    });
                }
            });
            // if(oldMode.equals(KEY_FROM_KEYSTORE_MODE_SELECT)) {
            hideObject(normalKeyFromKeystoreComposite, true);
            hideObject(showcaseKeyFromKeystoreComposite, false);
            // }
        } else {
            keyFromKeystoreMode = KEY_FROM_KEYSTORE_MODE_SELECT;
            if (createdKeyViewer != null) {
                if (!createdKeyViewer.isDisposed()) {
                    createdKeyViewer.dispose();
                }
            }
            createdKeyViewer = null;
            // if(! oldMode.equals(KEY_FROM_KEYSTORE_MODE_SELECT)) {
            hideObject(normalKeyFromKeystoreComposite, false);
            hideObject(showcaseKeyFromKeystoreComposite, true);
            // }
        }
        setAliasFromKeyPairAndCombo();
        calcAndSetPageCompletion();
    }

    private void setAliasFromKeyPairAndCombo() {
        if (createdKeyPairAlias == null) {
            if (keyCombo.getSelectionIndex() >= 0 && keyCombo.getSelectionIndex() < comboKeyMap.size()
                    && comboKeyMap.get(keyCombo.getSelectionIndex()) != null) {
                keyStoreAlias = comboKeyMap.get(keyCombo.getSelectionIndex());
            } else {
                keyStoreAlias = null;
            }
        } else {
            if (isAlgorithmTypeAsymmetric()) {
                keyStoreAlias = this.encrypt ? createdKeyPairAlias : KeyStoreManager.getInstance().getPrivateForPublic(
                        createdKeyPairAlias);
                if (createdKeyViewer != null && !createdKeyViewer.isDisposed()) {
                    if (this.encrypt) {
                        ((NewKeyPairComposite) createdKeyViewer).setPublicMode();
                    } else {
                        ((NewKeyPairComposite) createdKeyViewer).setPrivateMode();
                    }
                }
            } else {
                keyStoreAlias = createdKeyPairAlias;
            }
        }
    }

    /**
     * Excludes a control from Layout calculation
     *
     * @param that
     * @param hideit
     */
    private void hideObject(final Control that, final boolean hideit) {
        GridData GData = (GridData) that.getLayoutData();
        GData.exclude = hideit;
        that.setVisible(!hideit);
        Control[] myArray = {that};
        masterComp.getShell().layout(myArray);
        masterComp.layout();
        masterComp.getShell().layout();
    }

    private void refreshKeysFromKeystore(KeyStoreAlias previousSelection) {
        publicKeyMap.clear();
        privateKeyMap.clear();
        comboKeyMap.clear();
        try {
            Enumeration<String> aliases = KeyStoreManager.getInstance().getAliases();
            KeyStoreAlias localKeyStoreAlias = null;
            while (aliases.hasMoreElements()) {
                localKeyStoreAlias = new KeyStoreAlias(aliases.nextElement());
                // String op = localKeyStoreAlias.getOperation();
                if (localKeyStoreAlias.isOperationMatchingKeyId(showKeyGroup)) { //$NON-NLS-1$
                    if (localKeyStoreAlias.getKeyStoreEntryType().getType().contains(KeyType.KEYPAIR.getType())) { // asymmetric
                        if (localKeyStoreAlias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PUBLIC_KEY)) {
                            publicKeyMap.add(localKeyStoreAlias);
                        }
                        if (localKeyStoreAlias.getKeyStoreEntryType().equals(KeyType.KEYPAIR_PRIVATE_KEY)) {
                            privateKeyMap.add(localKeyStoreAlias);
                        }
                    } else { // symmetric
                        publicKeyMap.add(localKeyStoreAlias);
                        privateKeyMap.add(localKeyStoreAlias);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.logError(e);
        }

        // if (publicKeyMap.isEmpty() || privateKeyMap.isEmpty()) {
        // if(keyFromKeystoreButton != null) keyFromKeystoreButton.setEnabled(false);
        //            this.setErrorMessage(Messages.getString("DummyWizardPage.23") + " " + showKeyGroup + " " + Messages.getString("DummyWizardPage.232")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        // }

        selectCurrentKey(previousSelection);
    }

    /**
     * selects current key in the key combo according to previousSelection
     *
     * @param previousSelection
     */
    private void selectCurrentKey(KeyStoreAlias previousSelection) {

        comboKeyMap.addAll(encrypt ? publicKeyMap : privateKeyMap);
        keyCombo.setItems(getKeyItems());

        if (keyCombo.getItemCount() == 0) {
            keyCombo.add(Messages.getString("IntegratorWizardPage.noKeyFound")); //$NON-NLS-1$
            keyCombo.select(0);
            keyCombo.setEnabled(false);
            keyStoreAlias = null;
        } else {
            keyCombo.setEnabled(true);

            if (previousSelection == null) {
                keyCombo.select(0);
            } else {
                if (comboKeyMap.contains(previousSelection)) {
                    keyCombo.select(comboKeyMap.indexOf(previousSelection));
                } else {
                    keyCombo.select(0);
                    String prevHash = previousSelection.getHashValue();

                    for (KeyStoreAlias alias : comboKeyMap) {
                        if (alias.getHashValue().equals(prevHash)) {
                            keyCombo.select(comboKeyMap.indexOf(alias));
                            break;
                        }
                    }
                }
            }

            keyStoreAlias = comboKeyMap.get(keyCombo.getSelectionIndex());
        }
    }

    /**
     * Creates the 'key by hand' group
     */
    private void createKeyByHandGroup(Composite parent) {
        GridLayout keyGroupGridLayout = new GridLayout();
        keyGroupGridLayout.numColumns = 2;

        GridData keyGroupGridData = new GridData();
        keyGroupGridData.horizontalAlignment = GridData.FILL;
        keyGroupGridData.grabExcessHorizontalSpace = true;
        keyGroupGridData.verticalAlignment = GridData.FILL;

        keyByHandGroup = new Group(parent, SWT.NONE);
        keyByHandGroup.setLayoutData(keyGroupGridData);
        keyByHandGroup.setLayout(keyGroupGridLayout);
        keyByHandGroup.setText(Messages.getString("CustomKey.Title")); //$NON-NLS-1$

        Label label = new Label(keyByHandGroup, SWT.LEFT);
        label.setText(Messages.getString("CustomKey.KeyLength")); //$NON-NLS-1$

        final Combo keylengthCombo = new Combo(keyByHandGroup, SWT.READ_ONLY);
        keylengthCombo.setLayoutData(keyByHandGroup.getLayoutData());

        for (int validKeyLength : validKeyLengths)
            keylengthCombo.add(Integer.toString(validKeyLength));

        keylengthCombo.select(0);

        label = new Label(keyByHandGroup, SWT.LEFT);
        label.setText(Messages.getString("DummyWizardPage.16") + ":"); //$NON-NLS-1$ //$NON-NLS-2$

        customKeyTextbox = new HexTextbox(keyByHandGroup, SWT.BORDER, validKeyLengths[0] / 8, '0');
        customKeyTextbox.setLayoutData(keyByHandGroup.getLayoutData());

        keylengthCombo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                String keyLen = keylengthCombo.getItem(keylengthCombo.getSelectionIndex());
                int numBytes = (new Integer(keyLen)) / 8;
                customKeyTextbox.setNumBytes(numBytes);
            }
        });

    }

    @SuppressWarnings("unused")
    private void createMessageDigestGroup(Composite pageComposite) {
        Group messageDigestGroup = new Group(pageComposite, SWT.NONE);
        messageDigestGroup.setLayout(new GridLayout(2, false));
        messageDigestGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        messageDigestGroup.setText(Messages.getString("IntegratorWizardPage.2")); //$NON-NLS-1$

        Label label = new Label(messageDigestGroup, SWT.NONE);
        label.setText(Messages.getString("IntegratorWizardPage.3")); //$NON-NLS-1$

        messageDigest_text = new Text(messageDigestGroup, SWT.BORDER);
        messageDigest_text.setEnabled(false);
        messageDigest_text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        messageDigest_text.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                if (!e.text.matches("[0-9abcdefABCDEF]*")) //$NON-NLS-1$
                    e.doit = false;
            }
        });
        messageDigest_text.setTextLimit(showMessageDigestGroup * 2);
        messageDigest_text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                expectedChecksum = messageDigest_text.getText();
            }
        });
    }

    /**
     * This method initializes signatureGroup
     *
     */
    private void createSignatureGroup(Composite parent) {
        GridLayout signatureGroupGridLayout = new GridLayout();
        signatureGroupGridLayout.numColumns = 3;
        GridData signatureGroupGridData = new GridData();
        signatureGroupGridData.horizontalAlignment = GridData.FILL;
        signatureGroupGridData.grabExcessHorizontalSpace = true;
        signatureGroupGridData.verticalAlignment = GridData.FILL;
        Group signatureGroup = new Group(parent, SWT.NONE);
        signatureGroup.setLayoutData(signatureGroupGridData);
        signatureGroup.setLayout(signatureGroupGridLayout);
        signatureGroup.setText(Messages.getString("DummyWizardPage.18")); //$NON-NLS-1$
        Label label = new Label(signatureGroup, SWT.LEFT);
        label.setText(Messages.getString("DummyWizardPage.19")); //$NON-NLS-1$
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        signatureText = new Text(signatureGroup, SWT.READ_ONLY | SWT.BORDER);
        signatureText.setText(CHOOSE_SIGNATURE_FILE_LABEL);
        signatureText.setLayoutData(gridData);
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = false;
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.widthHint = 120;
        chooseFile = new Button(signatureGroup, SWT.PUSH);
        chooseFile.setLayoutData(gridData2);
        chooseFile.setText(Messages.getString("DummyWizardPage.20")); //$NON-NLS-1$
        chooseFile.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                FileDialog dialog = new FileDialog(new Shell(), openFile ? SWT.OPEN : SWT.SAVE);
                dialog.setFilterExtensions(new String[] {IConstants.ALL_FILTER_EXTENSION});
                dialog.setFilterNames(new String[] {IConstants.ALL_FILTER_NAME});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setOverwrite(true);

                String filename = dialog.open();
                if (filename != null) {
//                    if ((new File(filename).exists()) && (!encrypt)) {
//                        MessageBox messageBox = new MessageBox(new Shell(), SWT.NONE);
//                        messageBox.setText(Messages.getString("DummyWizardPage.21")); //$NON-NLS-1$
//                        messageBox.setMessage(Messages.getString("DummyWizardPage.22")); //$NON-NLS-1$
//                        messageBox.open();
//                        return;
//                    } else if ((!new File(filename).exists()) && (encrypt)) {
                	if(((!new File(filename).exists()) && (encrypt))) {
                        MessageBox messageBox = new MessageBox(new Shell(), SWT.NONE);
                        messageBox.setText(Messages.getString("DummyWizardPage.21")); //$NON-NLS-1$
                        messageBox.setMessage(Messages.getString("DummyWizardPage.24")); //$NON-NLS-1$
                        messageBox.open();
                        return;
                    }

                    signatureText.setText(filename);
                    signature = filename;
                    calcAndSetPageCompletion();
                }
            }
        });
        setPageComplete(false);
    }

    /**
     * This method initializes paddingGroup
     *
     */
    private void createPaddingGroup(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData paddingGroupGridData = new GridData();
        paddingGroupGridData.horizontalAlignment = GridData.FILL;
        paddingGroupGridData.grabExcessHorizontalSpace = true;
        paddingGroupGridData.verticalAlignment = GridData.FILL;
        paddingGroup = new Group(parent, SWT.NONE);
        paddingGroup.setLayoutData(paddingGroupGridData);
        paddingGroup.setLayout(gridLayout);
        paddingGroup.setText(Messages.getString("DummyWizardPage.2")); //$NON-NLS-1$
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        Label label = new Label(paddingGroup, SWT.LEFT);
        label.setText(Messages.getString("DummyWizardPage.13")); //$NON-NLS-1$
        createModeCombo(paddingGroup, gridData);
        label = new Label(paddingGroup, SWT.LEFT);
        label.setText(Messages.getString("DummyWizardPage.14")); //$NON-NLS-1$
        createPaddingCombo(paddingGroup, gridData);
    }

    /**
     * This method initializes modeCombo
     *
     */
    private void createModeCombo(Group paddingGroup, GridData gridData) {
        final Vector<IMetaMode> modeMap = new Vector<IMetaMode>();
        modeCombo = new Combo(paddingGroup, SWT.READ_ONLY);
        modeCombo.setLayoutData(gridData);
        modeCombo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                mode = modeMap.get(modeCombo.getSelectionIndex());
            }
        });

        // init mode
        List<IMetaMode> modes = AlgorithmsXMLManager.getInstance().getModes();

        for (IMetaMode mode : modes) {
            modeCombo.add(mode.getDescription());
            modeMap.add(mode);
        }
        // select pkcs#5 padding as default
        modeCombo.select(0);
        mode = modeMap.get(modeCombo.getSelectionIndex());
    }

    /**
     * This method initializes paddingCombo
     *
     */
    private void createPaddingCombo(Group paddingGroup, GridData gridData) {
        final Vector<IMetaPaddingScheme> paddingMap = new Vector<IMetaPaddingScheme>();
        paddingCombo = new Combo(paddingGroup, SWT.READ_ONLY);
        paddingCombo.setLayoutData(gridData);
        paddingCombo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                padding = paddingMap.get(paddingCombo.getSelectionIndex());
            }
        });

        // init padding
        List<IMetaPaddingScheme> paddings = AlgorithmsXMLManager.getInstance().getPaddingSchemes();

        for (IMetaPaddingScheme padding : paddings) {
            paddingCombo.add(padding.getPaddingSchemeName());
            paddingMap.add(padding);
        }
        // select pkcs#5 padding as default
        paddingCombo.select(2);
        padding = paddingMap.get(paddingCombo.getSelectionIndex());
    }

    /**
     * This method initializes randomCombo
     *
     */
    private void createRandomGroup(Composite parent) {
        GridLayout randomGroupGridLayout = new GridLayout();
        randomGroupGridLayout.numColumns = 3;
        randomGroupGridLayout.makeColumnsEqualWidth = false;
        GridData randomGroupGridData = new GridData();
        randomGroupGridData.horizontalAlignment = GridData.FILL;
        randomGroupGridData.grabExcessHorizontalSpace = true;
        randomGroupGridData.verticalAlignment = GridData.FILL;
        Group randomGroup = new Group(parent, SWT.NONE);
        randomGroup.setLayoutData(randomGroupGridData);
        randomGroup.setLayout(randomGroupGridLayout);
        randomGroup.setText(Messages.getString("DummyWizardPage.29")); //$NON-NLS-1$
        Label label = new Label(randomGroup, SWT.LEFT);
        label.setText(Messages.getString("DummyWizardPage.30")); //$NON-NLS-1$
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = false;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.widthHint = 30;
        final Text randomText = new Text(randomGroup, SWT.BORDER);
        randomText.setLayoutData(gridData);
        // random_text.setOrientation(SWT.RIGHT_TO_LEFT);
        randomText.setText("128"); //$NON-NLS-1$
        randomText.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if ((Character.getType(e.character) != Character.DECIMAL_DIGIT_NUMBER) && (e.keyCode != SWT.ARROW_LEFT)
                        && (e.keyCode != SWT.ARROW_RIGHT) && (e.character != SWT.DEL) && (e.character != SWT.BS))
                    e.doit = false;
            }

            public void keyReleased(KeyEvent e) {
                random = Integer.parseInt(randomText.getText());
            }
        });
        random = Integer.parseInt(randomText.getText());

        label = new Label(randomGroup, SWT.LEFT);
        label.setText("Bytes"); //$NON-NLS-1$ // spaces to get the right width for all cells

        GridLayout alphabetGroupGridLayout = new GridLayout();
        alphabetGroupGridLayout.numColumns = 7;
        alphabetGroupGridLayout.makeColumnsEqualWidth = false;
        GridData alphabetGroupGridData = new GridData();
        alphabetGroupGridData.horizontalAlignment = GridData.FILL;
        alphabetGroupGridData.grabExcessHorizontalSpace = true;
        alphabetGroupGridData.verticalAlignment = GridData.FILL;

        Group alphabetGroup = new Group(parent, SWT.NONE);
        alphabetGroup.setLayoutData(alphabetGroupGridData);
        alphabetGroup.setLayout(alphabetGroupGridLayout);
        alphabetGroup.setText(Messages.getString("DummyWizardPage.27")); //$NON-NLS-1$

        // All Chars
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
        Button alphabet_button = new Button(alphabetGroup, SWT.RADIO);
        alphabet_button.setText(Messages.getString("DummyWizardPage.28")); //$NON-NLS-1$
        alphabet_button.setLayoutData(gridData);
        alphabet_button.setSelection(true);
        alphabet_button.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                alphabet_text.setEnabled(alphabet_text_button.getSelection());
                alphabet_range_1.setEnabled(alphabet_range_button.getSelection());
                alphabet_range_2.setEnabled(alphabet_range_button.getSelection());
                alphabet_padding_button.setEnabled(alphabet_range_button.getSelection());
                alphabet_doFilter = false;
                setPageComplete(true);
            }

        });

        // Filter chars
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);

        alphabet_text_button = new Button(alphabetGroup, SWT.RADIO);
        alphabet_text_button.setText(Messages.getString("DummyWizardPage.31")); //$NON-NLS-1$
        alphabet_text_button.setLayoutData(gridData);
        alphabet_text_button.setSelection(false);
        alphabet_text_button.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                alphabet_text.setEnabled(alphabet_text_button.getSelection());
                alphabet_range_1.setEnabled(alphabet_range_button.getSelection());
                alphabet_range_2.setEnabled(alphabet_range_button.getSelection());
                alphabet_padding_button.setEnabled(alphabet_range_button.getSelection());
                alphabet_doFilter = true;
                alphabet = parseAlphabet();
            }

        });

        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        label = new Label(alphabetGroup, SWT.LEFT);
        label.setLayoutData(gridData);
        label.setText(""); //$NON-NLS-1$

        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1);
        alphabet_text = new Text(alphabetGroup, SWT.BORDER);
        alphabet_text.setLayoutData(gridData);
        alphabet_text.setText("0123456789ABCDEF"); //$NON-NLS-1$
        alphabet_text.setEnabled(false);
        alphabet_text.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (alphabet_text.getText().contains("" + e.character)) //$NON-NLS-1$
                    e.doit = false;
            }

            public void keyReleased(KeyEvent e) {
                alphabet = parseAlphabet();
            }
        });

        // Filter range
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
        alphabet_range_button = new Button(alphabetGroup, SWT.RADIO);
        alphabet_range_button.setText(Messages.getString("DummyWizardPage.34")); //$NON-NLS-1$
        alphabet_range_button.setLayoutData(gridData);
        alphabet_range_button.setSelection(false);
        alphabet_range_button.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                alphabet_text.setEnabled(alphabet_text_button.getSelection());
                alphabet_range_1.setEnabled(alphabet_range_button.getSelection());
                alphabet_range_2.setEnabled(alphabet_range_button.getSelection());
                alphabet_padding_button.setEnabled(alphabet_range_button.getSelection());
                alphabet_doFilter = true;
                alphabet = parseAlphabet();
            }

        });

        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        label = new Label(alphabetGroup, SWT.LEFT);
        label.setLayoutData(gridData);
        label.setText(""); //$NON-NLS-1$

        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = 40;
        alphabet_range_1 = new Text(alphabetGroup, SWT.BORDER | SWT.CENTER);
        alphabet_range_1.setLayoutData(gridData);
        alphabet_range_1.setText("0"); //$NON-NLS-1$
        alphabet_range_1.setEnabled(false);
        alphabet_range_1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                alphabet = parseAlphabet();
            }
        });

        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = 8;
        label = new Label(alphabetGroup, SWT.NONE);
        label.setLayoutData(gridData);
        label.setText("-"); //$NON-NLS-1$

        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = 40;
        alphabet_range_2 = new Text(alphabetGroup, SWT.BORDER | SWT.CENTER);
        alphabet_range_2.setLayoutData(gridData);
        alphabet_range_2.setText("9"); //$NON-NLS-1$
        alphabet_range_2.setEnabled(false);
        alphabet_range_2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                alphabet = parseAlphabet();
            }
        });

        // only for space
        gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = 20;
        label = new Label(alphabetGroup, SWT.NONE);
        label.setLayoutData(gridData);
        label.setText(""); //$NON-NLS-1$

        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        alphabet_padding_button = new Button(alphabetGroup, SWT.CHECK);
        alphabet_padding_button.setText(Messages.getString("DummyWizardPage.35")); //$NON-NLS-1$
        alphabet_padding_button.setLayoutData(gridData);
        alphabet_padding_button.setSelection(false);
        alphabet_padding_button.setEnabled(false);
        alphabet_padding_button.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                alphabet = parseAlphabet();
            }
        });

        alphabet_doFilter = false;
    }

    protected byte[][] parseAlphabet() {
        if (alphabet_range_button.getSelection()) { // range
            try {
                int start = Integer.parseInt(alphabet_range_1.getText());
                int end = Integer.parseInt(alphabet_range_2.getText());

                if (start > end)
                    throw new Exception();

                int size = (int) Math.floor(Math.log10(end)) + 1;
                byte[][] alphabet = new byte[end - start + 1][];

                for (int i = 0; i <= end - start; i++) {
                    String s = "" + (start + i); //$NON-NLS-1$
                    if (alphabet_padding_button.getSelection()) {
                        for (int j = ("" + (start + i)).length(); j < size; j++) //$NON-NLS-1$
                            s = "0" + s; //$NON-NLS-1$
                    }
                    alphabet[i] = (s + ALPHABET_SEPERATION_CHAR).getBytes();
                }
                setPageComplete(true);
                return alphabet;
            } catch (Exception e) {
                setPageComplete(false);
                return null;
            }
        }
        if (alphabet_text_button.getSelection()) { // text
            String text = alphabet_text.getText();
            byte[][] alphabet = new byte[text.length()][];
            for (int i = 0; i < text.length(); i++) {
                alphabet[i] = (text.charAt(i) + ALPHABET_SEPERATION_CHAR).getBytes();
            }
            setPageComplete(true);
            return alphabet;
        }

        return null;
    }

    /**
     * Enables/disables a group and all it's child controls
     */
    protected void setEnableForGroup(Composite group, boolean enabled) {
        Control[] childs = group.getChildren();
        for (Control child : childs)
            if (child instanceof Composite) {
                setEnableForGroup((Composite) child, enabled);
            } else if (!(child instanceof Label)) {
                child.setEnabled(enabled);
            }
        group.setEnabled(enabled);
    }

    private String[] getKeyItems() {
        Vector<KeyStoreAlias> map;
        if (encrypt) {
            if (publicKeyMap.isEmpty() || privateKeyMap.isEmpty())
                if (showPaddingGroup) {
                    return new String[0];// {Messages.getString("DummyWizardPage.15")}; //$NON-NLS-1$
                } else {
                    return new String[0];// {Messages.getString("DummyWizardPage.25")}; //$NON-NLS-1$
                }
            map = publicKeyMap;
        } else {
            if (publicKeyMap.isEmpty() || privateKeyMap.isEmpty())
                if (showPaddingGroup) {
                    return new String[0];// {Messages.getString("DummyWizardPage.15")}; //$NON-NLS-1$
                } else {
                    return new String[0];// {Messages.getString("DummyWizardPage.26")}; //$NON-NLS-1$
                }
            map = privateKeyMap;
        }

        String[] items = new String[map.size()];
        int i = 0;
        for (KeyStoreAlias item : map) {
            items[i++] = "\"" + item.getContactName() + "\" - " + translateKeyStoreEntryType(item.getKeyStoreEntryType().getType()) + " - " + item.getKeyLength() + " bit"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
        return items;
    }

    private String translateKeyStoreEntryType(String type) {
        if (type.equals("keypair.public"))return Messages.getString("DummyWizardPage.key.public"); //$NON-NLS-1$ //$NON-NLS-2$
        if (type.equals("keypair.private"))return Messages.getString("DummyWizardPage.key.private"); //$NON-NLS-1$ //$NON-NLS-2$
        if (type.equals("secret"))return Messages.getString("DummyWizardPage.key.secret"); //$NON-NLS-1$ //$NON-NLS-2$
        return Messages.getString("DummyWizardPage.key.key"); //$NON-NLS-1$
    }

    /**
     * returns the selected keystore alias
     *
     * @return the selected keystore alias
     */
    public KeyStoreAlias key() {
        return keyStoreAlias;
    }

    /**
     * returns the selected signature
     *
     * @return the selected signature
     */
    public String signature() {
        return signature;
    }

    /**
     * returns the selected padding scheme
     *
     * @return the selected padding scheme
     */
    public IMetaPaddingScheme padding() {
        return padding;
    }

    /**
     * returns the selected mode
     *
     * @return the selected mode
     */
    public IMetaMode mode() {
        return mode;
    }

    /**
     * returns the entered random size
     *
     * @return the entered random size
     */
    public int getRandomSize() {
        return random;
    }

    /**
     * returns true if a filter applies
     *
     * @return true if a filter applies
     */
    public boolean doFilter() {
        return alphabet_doFilter;
    }

    /**
     * returns the characters to be filtered/allowed
     *
     * @return the characters to be filtered/allowed
     */
    public byte[][] getFilter() {
        return alphabet;
    }

    public String getExpectedChecksum() {
        return expectedChecksum;
    }

    /**
     * Returns whether a custom key should be used
     *
     * @return whether a custom key should be used
     */
    public boolean useCustomKey() {
        return useCustomKey;
    }

    /**
     * Returns the bytes entered in the custom key textbox
     *
     * @return the bytes entered in the custom key textbox
     */
    public byte[] getCustomKey() {
        return customKeyTextbox.getBytes();
    }
}
