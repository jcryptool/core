// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.ui.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;
import org.jcryptool.crypto.classic.model.ui.wizard.util.MWizardMessage;
import org.jcryptool.crypto.classic.model.ui.wizard.util.WidgetBubbleUIInputHandler;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite.AlphabetAcceptor;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite.Mode;

/**
 * Implementation of a generic classic cryptoalgorithm wizard page for en-/decryption. Can be instantiated directly, but
 * is rather intended to be subclassed. <br>
 * This wizard page contains basic controls for en-/decryption parameters, like a key input text box, or an
 * currentAlphabet selection combo.
 * 
 * @author SLeischnig
 * 
 */
public class AbstractClassicCryptoPage extends WizardPage {

    SWTResourceManager resources = new SWTResourceManager();

    protected Group keyGroup;
    protected Label keyDescriptionLabel;
    protected Text keyText;
    protected Group alphabetGroup;
    protected Composite alphabetInnerGroup;
    protected Button showAlphabetContent;
    protected Label alphabetLabel;
    protected AlphabetSelectorComposite alphabetCombo;
    protected Button filterCheckBox;
    protected Group operationGroup;
    protected Button encryptButton;
    protected Button decryptButton;

    /** Used to override a VerifyListener */
    protected boolean resetFlag = false;

    protected Button transformCheckBox;
    protected Group transformGroup;
    protected boolean haveNextPage = true;

    /**
     * This standard message is the one displayed when everything is normal, and is generated from the message parameter
     * in the constructor and the file/editor name opened.
     */
    protected MWizardMessage normalStatusMsg; 
    protected AbstractUIInput<Boolean> operationInput;
    // protected AbstractUIInput<AbstractAlphabet> alphabetInput;
    protected AbstractUIInput<Boolean> filterInput;
    protected TextfieldInput<String> keyInput;
    protected WidgetBubbleUIInputHandler verificationDisplayHandler;
    protected Widget operationLastSelected;
    protected AbstractUIInput<Boolean> transformationInput;

    protected ClassicAlgorithmSpecification specification = new ClassicAlgorithmSpecification();

    /**
     * Observes every change made to the page, for setting the next page/can finish status.
     */
    protected Observer pageObserver = new Observer() {
        @Override
		public void update(Observable o, Object arg) {
            haveNextPage = transformationInput.getContent();
            setPageComplete(mayFinish());
            getContainer().updateButtons();
            updateCommandLineString();
        }
    };
    private Label customAlphaHint;

    protected Group consoleGroup;

    private Label lblConsoleDescr;

    private Label lblConsoleFurther;

    private Text txtConsoleCommand;

	protected Composite pageComposite;

    /**
     * Creates a new instance of AbstractClassicCryptoPage
     * 
     * @wbp.parser.constructor
     */
    public AbstractClassicCryptoPage() {
        this("", Messages.AbstractClassicCryptoPage_genericNormalMsg); //$NON-NLS-1$
    }

    /**
     * Creates a new instance of AbstractClassicCryptoPage, defining its window title.
     * 
     * @param title the window title
     */
    public AbstractClassicCryptoPage(String title) {
        this(title, Messages.AbstractClassicCryptoPage_genericNormalMsg);
    }

    /**
     * Creates a new instance of AbstractClassicCryptoPage, defining its window title and message.
     * 
     * @param title the window title
     * @param message the message of the page
     */
    public AbstractClassicCryptoPage(String title, final String message) {
        super("", title, null); //$NON-NLS-1$
        setTitle(title);
        normalStatusMsg = new MWizardMessage() {
            @Override
			public String getMessage() {
                return message;
            }

            @Override
			public int getMessageType() {
                return NONE;
            }

            @Override
			public boolean isStandaloneMessage() {
                return true;
            }
        };
        setTitleBarMessage(normalStatusMsg);
    }

    public void setAlgorithmSpecification(ClassicAlgorithmSpecification spec) {
        this.specification = spec;
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
	public void createControl(Composite parent) {
        GridData pageCompositeLayoutData = new GridData();
        GridLayout pageCompositeLayout = new GridLayout();
        pageCompositeLayoutData.grabExcessHorizontalSpace = true;
        pageCompositeLayoutData.grabExcessVerticalSpace = true;
        pageCompositeLayoutData.horizontalAlignment = SWT.FILL;
        pageCompositeLayoutData.verticalAlignment = SWT.FILL;
        pageComposite = new Composite(parent, SWT.NULL);
        pageComposite.setLayout(pageCompositeLayout);
        pageComposite.setLayoutData(pageCompositeLayoutData);

        createOperationGroup(pageComposite);
        createAlphabetGroup(pageComposite);
        createKeyGroup(pageComposite);
        createTransformGroup(pageComposite);
        createConsoleGroup(pageComposite);

        setControl(pageComposite);
        setHelpAvailable();

        createInputObjects();
        setPageComplete(mayFinish());

        createInputVerificationHandler(this.getShell());
        addPageObserver();
        updateCommandLineString();
    }

    /**
     * creates the input objects. extend or override for any further needs.
     */
    protected void createInputObjects() {
        createOperationInputObjects();
        createAlphabetInputObjects();
        createKeyInputObjects();
        createTransformationInputObject();
    }

    /**
     * The standard UIInput objects (with selection listener wiring) for the operation selection are created here. This
     * should be reimplemented if other input verification/handling is needed.
     */
    protected void createOperationInputObjects() {
        operationInput = new AbstractUIInput<Boolean>() {
            @Override
            public void writeContent(Boolean content) {
                encryptButton.setSelection(content);
            }

            @Override
            protected InputVerificationResult verifyUserChange() {
                return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
            }

            @Override
            public Boolean readContent() {
                return encryptButton.getSelection();
            }

            @Override
            public String getName() {
                return Messages.AbstractClassicCryptoPage_operation_input_name;
            }

            @Override
            protected Boolean getDefaultContent() {
                return true;
            }
        };

        SelectionAdapter adapter = new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                operationLastSelected = e.widget;
                operationInput.synchronizeWithUserSide();
            }
        };
        encryptButton.addSelectionListener(adapter);
        decryptButton.addSelectionListener(adapter);

        Observer operationObserver = new Observer() {
            @Override
			public void update(Observable o, Object arg) {
                if (transformationInput != null) {
                    transformationInput.writeContent(operationInput.getContent());
                    transformationInput.synchronizeWithUserSide();
                }
                if(operationInput.getContent()) {
                	filterCheckBox.setText(Messages.WizardPage_filterchars_enc);
                } else {
                	filterCheckBox.setText(Messages.WizardPage_filterchars_dec);
                }
            }
        };
		operationInput.addObserver(operationObserver);
        operationObserver.update(null, null);
    }

    /**
     * The standard UIInput objects (with selection listener wiring) for the currentAlphabet and filter selections are
     * created here. This should be reimplemented if other input verification/handling is needed.
     */
    protected void createAlphabetInputObjects() {

        filterInput = new ButtonInput() {

            @Override
            protected InputVerificationResult verifyUserChange() {
                return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
            }

            @Override
            public String getName() {
                return "nonalpha-chars filter"; //$NON-NLS-1$
            }

            @Override
            protected Boolean getDefaultContent() {
                return true;
            }

            @Override
            public Button getButton() {
                return filterCheckBox;
            }
        };

        getAlphabetInput().addObserver(new Observer() {
            @Override
			public void update(Observable o, Object arg) {
                if (arg == null) { // when there is really a change
                    if (transformationInput.getContent()) {
                        updateTransformationPage(getAlphabetInput().getContent());
                    }
                }
            }
        });

        updateTransformationPage(getAlphabetInput().getContent());
    }

    /**
     * @return the input object from the alphabet combo
     */
    protected AbstractUIInput<AbstractAlphabet> getAlphabetInput() {
        return alphabetCombo.getAlphabetInput();
    }

    /**
     * The standard UIInput objects (with selection listener wiring) for the key selections are created here. This
     * should be reimplemented if other input verification/handling is needed.
     */
    protected void createKeyInputObjects() {
        keyInput = new KeyInput<String>() {
            @Override
            public Text getTextfield() {
                return keyText;
            }

            @Override
            public String readContent() {
                return getTextfield().getText();
            }

            @Override
            protected String getDefaultContent() {
                return ""; //$NON-NLS-1$
            }

            @Override
            public String getName() {
                return Messages.AbstractClassicCryptoPage_key_input_name;
            }

            @Override
            protected void resetExternallyCaused(AbstractUIInput<?> inputWhichCausedThis) {
                // reacting to a change of alphabets
                String keyNow = getTextfield().getText();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < keyNow.length(); i++) {
                    if (getAlphabetInput().getContent().contains(keyNow.charAt(i))) {
                        stringBuilder.append(keyNow.charAt(i));
                    }
                }

                setTextfieldTextExternal(stringBuilder.toString());
                reread(inputWhichCausedThis);
            }

            @Override
            public AbstractAlphabet getAlphabet() {
                return getAlphabetInput().getContent();
            }

            @Override
            protected InputVerificationResult verifyUserChange() {
                return KeyVerificator.verify(getTextfield().getText(), getAlphabet(), getKeyVerificators());
            }
        };

        // changes in the currentAlphabet input must be forwarded to the key input for revalidation
        getAlphabetInput().addObserver(keyInput);
    }

    protected void createTransformationInputObject() {
        transformationInput = new AbstractUIInput<Boolean>() {
            @Override
            protected InputVerificationResult verifyUserChange() {
                return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
            }

            @Override
            public Boolean readContent() {
                return transformCheckBox.getSelection();
            }

            @Override
            public void writeContent(Boolean content) {
                transformCheckBox.setSelection(content);
            }

            @Override
            protected Boolean getDefaultContent() {
                if (operationInput != null) {
                    return operationInput.getContent();
                }
                return true;
            }

            @Override
            public String getName() {
                return Messages.AbstractClassicCryptoPage_transform_name;
            }
        };

        SelectionAdapter adapter = new SelectionAdapter() {
            @Override
			public void widgetSelected(SelectionEvent e) {
                transformationInput.synchronizeWithUserSide();
            }
        };
        transformCheckBox.addSelectionListener(adapter);

        // change selection when en/decrypt is selected
        transformationInput.addObserver(new Observer() {
            @Override
			public void update(Observable o, Object arg) {
                if (arg == null) { // when there is really a change
                    if (transformationInput.getContent()) {
                        updateTransformationPage(getAlphabetInput().getContent());
                    } else {
                        updateTransformationPage(null); 
                    }
                }
            }
        });
    }

    /**
     * Creates a standard input verification handler, which displays ballon popups beneath controls, when an error in
     * the input occured.
     * 
     * @param shell
     */
    protected void createInputVerificationHandler(Shell shell) {
        verificationDisplayHandler = new WidgetBubbleUIInputHandler(shell) {
            @Override
            protected void changeTooltipDurationAtCleaninputButNotHidden(AbstractUIInput<?> input) {
                // vanish instantly when it is the "not changing the encryption" tooltip
                if (getLastDisplayedResultType(input) == ClassicAlgorithmSpecification.RESULT_TYPE_NOKEY) {
                    tooltipMap.get(input).setTimeToVanish(-1);
                } else {
                    super.changeTooltipDurationAtCleaninputButNotHidden(input);
                }
            }

            @Override
            public Control mapInputToWidget(AbstractUIInput<?> input) {
                if (input.equals(operationInput)) {
                    return (Control) operationLastSelected;
                }
                return super.mapInputToWidget(input);
            }
        };
		if (operationInput != null)
			verificationDisplayHandler.addAsObserverForInput(operationInput);
		if (filterInput != null)
			verificationDisplayHandler.addAsObserverForInput(filterInput);
		if (getAlphabetInput() != null)
			verificationDisplayHandler.addAsObserverForInput(getAlphabetInput());
		if (keyInput != null)
			verificationDisplayHandler.addAsObserverForInput(keyInput);
		if (transformationInput != null)
			verificationDisplayHandler.addAsObserverForInput(transformationInput);

		// static mappings (dynamic, like at operation, are handled above in the
		// overridden method)
		if (getAlphabetInput() != null)
			verificationDisplayHandler.addInputWidgetMapping(getAlphabetInput(), alphabetCombo);
		if (filterInput != null)
			verificationDisplayHandler.addInputWidgetMapping(filterInput, filterCheckBox);
		if (keyInput != null)
			verificationDisplayHandler.addInputWidgetMapping(keyInput, keyText);
		if (transformationInput != null) 
			verificationDisplayHandler.addInputWidgetMapping(transformationInput, transformCheckBox);
    }

    /**
     * just an alias for the delegated method
     */
    protected List<KeyVerificator> getKeyVerificatorsDelegate() {
        return getKeyVerificators();
    }

    /**
     * Provides the key verificators for the standard implementation of the KeyInput. This method is meant for usage
     * when nothing else but key verificators have to be changed in the key input mechanism apart from the standard
     * implementation. Ideally, key verificators should be specified in the Algorithm Specification class.
     * 
     * @return a list of key verificators
     */
    protected List<KeyVerificator> getKeyVerificators() {
        return specification.getKeyVerificators();
    }

    /**
     * Add the {@link #pageObserver} as Observer everywhere, where page status changes are made, that could change the
     * finishable/next page status of this page.
     */
    protected void addPageObserver() {
        if(operationInput != null) operationInput.addObserver(pageObserver);
        if(getAlphabetInput() != null) getAlphabetInput().addObserver(pageObserver);
        if(filterInput != null) filterInput.addObserver(pageObserver);
        if(keyInput != null) keyInput.addObserver(pageObserver);
        if(transformationInput != null) transformationInput.addObserver(pageObserver);
    }

    /**
     * Subclasses should override this procedure to set the Help available flag and id, like
     * PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), DelastellePlugin.PLUGIN_ID +
     * ".delastelleWizard");
     * 
     */
    protected void setHelpAvailable() {

    }

    /**
     * Returns the available alphabets. Meant to be overridden in subclasses.
     * 
     * @return all available alphabets for this wizard page.
     */
    protected List<AbstractAlphabet> getAvailableAlphabets() {
        if (specification == null) {
            ArrayList<AbstractAlphabet> result = new ArrayList<AbstractAlphabet>();
            for (AbstractAlphabet a : AlphabetsManager.getInstance().getAlphabets()) {
                result.add(a);
            }

            return result;
        } else {
            return specification.getAvailablePlainTextAlphabets();
        }
    }

    /**
     * Returns the currentAlphabet that will be selected first. Meant to be overridden in subclasses.
     * 
     * @return the standard currentAlphabet for this wizard.
     */
    protected AbstractAlphabet getDefaultAlphabet() {
        if (specification == null) {
            List<AbstractAlphabet> alphabets = getAvailableAlphabets();
            if (alphabets.contains(AlphabetsManager.getInstance().getDefaultAlphabet())) {
                return AlphabetsManager.getInstance().getDefaultAlphabet();
            }
            return alphabets.size() > 0 ? alphabets.get(0) : AlphabetsManager.getInstance().getDefaultAlphabet();
        } else {
            return specification.getDefaultPlainTextAlphabet();
        }

    }

    @Override
    public IWizardPage getNextPage() {
        if (haveNextPage)
            return super.getNextPage();
        else
            return null;
    }

    /**
     * Returns <code>true</code>, if the page is complete and the wizard may finish.
     * 
     * @return <code>true</code>, if the page is complete and the wizard may finish
     */
    protected boolean mayFinish() {
        if (keyInput != null && keyInput.getContent().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the selected currentAlphabet.
     * 
     * @return The selected currentAlphabet
     */
    public AbstractAlphabet getSelectedAlphabet() {
        if(getAlphabetInput() != null) {
        	return getAlphabetInput().getContent();
        } else {
        	return specification.getDefaultPlainTextAlphabet();
        }
    }

    /**
     * @return whether the selected alphabet is a custom one.
     */
    public boolean isSelectedAlphaCustom() {
        return alphabetCombo.isCustomAlphabetSelected();
    }

    /**
     * Returns the entered key. This method should only be used, when the wizard page is complete.
     * 
     * @return The key in string form
     */
    public String getKey() {
        return keyInput.getContent();
    }

    /**
     * Returns <code>true</code>, if the desired operation is <i>Encrypt</i>.
     * 
     * @return <code>true</code>, if the desired operation is <i>Encrypt</i>
     */
    public boolean encrypt() {
        return operationInput.getContent();
    }

    /**
     * Returns <code>true</code>, if characters who are not part of the selected currentAlphabet are supposed to be
     * filtered out.
     * 
     * @return <code>true</code>, if characters who are not part of the selected currentAlphabet are supposed to be
     *         filtered out
     */
    public boolean isNonAlphaFilter() {
        return filterInput.getContent();
    }

    /**
     * This method initializes the operationGroup, provoding the encrypt/decrypt choice. Subclasses should override
     * this, if more controls are needed. *
     */
    protected void createOperationGroup(Composite parent) {

        operationGroup = new Group(parent, SWT.NONE);
        GridLayout operationGroupGridLayout = new GridLayout();
        operationGroupGridLayout.numColumns = 2;

        GridData operationGroupGridData = new GridData();
        operationGroupGridData.horizontalAlignment = GridData.FILL;
        operationGroupGridData.grabExcessHorizontalSpace = true;
        operationGroupGridData.grabExcessVerticalSpace = false;
        operationGroupGridData.verticalAlignment = SWT.TOP;

        operationGroup.setLayoutData(operationGroupGridData);
        operationGroup.setLayout(operationGroupGridLayout);
        operationGroup.setText(Messages.WizardPage_operation);

        encryptButton = new Button(operationGroup, SWT.RADIO);

        GridData encryptButtonGridData = new GridData();
        encryptButtonGridData.horizontalAlignment = GridData.FILL;
        encryptButtonGridData.grabExcessHorizontalSpace = true;
        encryptButtonGridData.grabExcessVerticalSpace = true;
        encryptButtonGridData.verticalAlignment = GridData.CENTER;

        encryptButton.setText(Messages.WizardPage_encrypt);
        encryptButton.setLayoutData(encryptButtonGridData);

        decryptButton = new Button(operationGroup, SWT.RADIO);

        GridData decryptButtonGridData = new GridData();
        decryptButtonGridData.horizontalAlignment = GridData.FILL;
        decryptButtonGridData.grabExcessHorizontalSpace = true;
        decryptButtonGridData.grabExcessVerticalSpace = true;
        decryptButtonGridData.verticalAlignment = GridData.CENTER;

        decryptButton.setText(Messages.WizardPage_decrypt);
        decryptButton.setLayoutData(decryptButtonGridData);

    }

    /**
     * This method initializes alphabetGroup, providing the controls for selecting an currentAlphabet and the filter
     * checkbox. <br>
     * Subclasses should override this, if more controls are needed.
     */
    protected void createAlphabetGroup(Composite parent) {
        alphabetGroup = new Group(parent, SWT.NONE);

        GridLayout alphabetGroupGridLayout = new GridLayout();

        GridData alphabetGroupGridData = new GridData();
        alphabetGroupGridData.horizontalAlignment = GridData.FILL;
        alphabetGroupGridData.grabExcessHorizontalSpace = true;
        alphabetGroupGridData.grabExcessVerticalSpace = false;
        alphabetGroupGridData.verticalAlignment = SWT.TOP;

        alphabetGroup.setLayoutData(alphabetGroupGridData);
        alphabetGroup.setLayout(alphabetGroupGridLayout);
        alphabetGroup.setText(Messages.WizardPage_alpha);

        alphabetInnerGroup = new Composite(alphabetGroup, SWT.NONE);

        GridLayout alphabetInnerGroupGridLayout = new GridLayout();
        alphabetInnerGroupGridLayout.numColumns = 3;

        GridData alphabetInnerGroupGridData = new GridData();
        alphabetInnerGroupGridData.horizontalAlignment = GridData.FILL;
        alphabetInnerGroupGridData.grabExcessHorizontalSpace = true;
        alphabetInnerGroupGridData.grabExcessVerticalSpace = false;
        alphabetInnerGroupGridData.verticalAlignment = SWT.TOP;

        alphabetInnerGroup.setLayoutData(alphabetInnerGroupGridData);
        alphabetInnerGroup.setLayout(alphabetInnerGroupGridLayout);

        alphabetLabel = new Label(alphabetInnerGroup, SWT.NONE);

        GridData alphabetLabelGridData = new GridData();
        alphabetLabelGridData.horizontalAlignment = GridData.FILL;
        alphabetLabelGridData.grabExcessVerticalSpace = true;

        alphabetLabel.setText(Messages.WizardPage_selectalpha);
        alphabetLabel.setLayoutData(alphabetLabelGridData);

        AlphabetAcceptor acceptor = new AlphabetAcceptor() {
            @Override
            public boolean accept(AbstractAlphabet alphabet) {
                return specification.isValidPlainTextAlphabet(alphabet);
            }
        };
        Mode alphabetSelectionMode = specification.isAllowCustomAlphabetCreation() ?
        // AlphabetSelectorComposite.Mode.SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS:
        AlphabetSelectorComposite.Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON
                : AlphabetSelectorComposite.Mode.SINGLE_COMBO_BOX_ONLY_EXISTING_ALPHABETS;
        alphabetCombo = new AlphabetSelectorComposite(alphabetInnerGroup, acceptor, getDefaultAlphabet(),
                alphabetSelectionMode);

        GridData filterComboGridData = new GridData();
        filterComboGridData.horizontalAlignment = GridData.FILL;
        filterComboGridData.grabExcessHorizontalSpace = true;
        filterComboGridData.grabExcessVerticalSpace = true;
        filterComboGridData.verticalAlignment = GridData.CENTER;

        alphabetCombo.setLayoutData(filterComboGridData);
        // alphabetCombo = new Combo(alphabetInnerGroup, SWT.BORDER | SWT.READ_ONLY);
        // GridData filterComboGridData = new GridData();
        // filterComboGridData.horizontalAlignment = GridData.FILL;
        // filterComboGridData.grabExcessHorizontalSpace = true;
        // filterComboGridData.grabExcessVerticalSpace = true;
        // filterComboGridData.verticalAlignment = GridData.CENTER;
        //
        // alphabetCombo.setLayoutData(filterComboGridData);
        {
            showAlphabetContent = new Button(alphabetInnerGroup, SWT.PUSH);
            GridData showAlphabetContentGridData = new GridData();
            showAlphabetContent.setLayoutData(showAlphabetContentGridData);
            showAlphabetContent.setText(Messages.AbstractClassicCryptoPage_showSelectedAlpha);
            showAlphabetContent.addSelectionListener(new SelectionAdapter() {
                @Override
				public void widgetSelected(SelectionEvent evt) {
                	verificationDisplayHandler.disposeTooltips();
                    ToolTip tooltip = new ToolTip(getShell(), SWT.BALLOON);
                    tooltip.setText(Messages.AbstractClassicCryptoPage_alphabetcontent_balloon_title);
                    tooltip.setMessage(
                    		AbstractAlphabet.alphabetContentAsString(
                    				getAlphabetInput().getContent().getCharacterSet()
                    				)
                    		+ Messages.AbstractClassicCryptoPage_clicktoclose);
                    tooltip.setAutoHide(true);
                    tooltip.setVisible(true);
                }
            });
        }

        boolean showAlphaCreationHint = false;
        if (showAlphaCreationHint && specification.isAllowCustomAlphabetCreation()) {
            new Label(alphabetInnerGroup, SWT.NONE);
            customAlphaHint = new Label(alphabetInnerGroup, SWT.NONE);
            GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
            layoutData.verticalIndent = -5;

            customAlphaHint.setLayoutData(layoutData);
            customAlphaHint.setText(Messages.AbstractClassicCryptoPage_customAlphabetHint);
            customAlphaHint.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
            customAlphaHint.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.ITALIC)); //$NON-NLS-1$
        }

        filterCheckBox = new Button(alphabetInnerGroup, SWT.CHECK);

        GridData filterCheckBoxGridData = new GridData();
        filterCheckBoxGridData.horizontalSpan = 3;
        if (specification.isAllowCustomAlphabetCreation())
            filterCheckBoxGridData.verticalIndent = 3;
        filterCheckBoxGridData.verticalAlignment = GridData.CENTER;
        filterCheckBoxGridData.grabExcessHorizontalSpace = true;
        filterCheckBoxGridData.grabExcessVerticalSpace = true;
        filterCheckBoxGridData.horizontalAlignment = GridData.FILL;

        filterCheckBox.setLayoutData(filterCheckBoxGridData);
    }

    /**
     * This method initializes transformaGroup, providing the transformation controls <br>
     * Subclasses should override this, if more controls are needed. *
     */
    protected void createTransformGroup(Composite parent) {
        transformGroup = new Group(parent, SWT.NONE);

        GridLayout transformGroupGridLayout = new GridLayout();
        transformGroupGridLayout.numColumns = 2;

        GridData transformGroupGridData = new GridData();
        transformGroupGridData.horizontalAlignment = GridData.FILL;
        transformGroupGridData.grabExcessHorizontalSpace = true;
        transformGroupGridData.grabExcessVerticalSpace = false;
        transformGroupGridData.verticalAlignment = SWT.TOP;

        transformGroup.setLayoutData(transformGroupGridData);
        transformGroup.setLayout(transformGroupGridLayout);
        transformGroup.setText(Messages.AbstractClassicCryptoPage_preOpTransformLabel);

        transformCheckBox = new Button(transformGroup, SWT.CHECK);

        GridData transformCheckBoxGridData = new GridData();
        transformCheckBoxGridData.horizontalSpan = 2;
        transformCheckBoxGridData.verticalAlignment = GridData.CENTER;
        transformCheckBoxGridData.grabExcessHorizontalSpace = true;
        transformCheckBoxGridData.grabExcessVerticalSpace = true;
        transformCheckBoxGridData.horizontalAlignment = GridData.FILL;

        transformCheckBox.setText(Messages.AbstractClassicCryptoPage_applyTransformLabel);
        transformCheckBox.setLayoutData(transformCheckBoxGridData);

        transformCheckBox.setSelection(true);
        if (!transformCheckBox.getSelection())
            ((AbstractClassicTransformationPage) getNextPage()).setTransformData(new TransformData());
    }

    /**
     * This method initializes the key input group. Subclasses should override this, if more controls are needed.
     */
    protected void createKeyGroup(Composite parent) {
        keyGroup = new Group(parent, SWT.NONE);

        GridLayout keyGroupGridLayout = new GridLayout();
        keyGroupGridLayout.numColumns = 2;

        GridData keyGroupGridData = new GridData();
        keyGroupGridData.horizontalAlignment = GridData.FILL;
        keyGroupGridData.grabExcessHorizontalSpace = true;
        keyGroupGridData.grabExcessVerticalSpace = false;
        keyGroupGridData.verticalAlignment = SWT.TOP;

        keyGroup.setLayoutData(keyGroupGridData);
        keyGroup.setLayout(keyGroupGridLayout);
        keyGroup.setText(Messages.WizardPage_key);

        keyDescriptionLabel = new Label(keyGroup, SWT.NONE);

        GridData keyDescriptionLabelGridData = new GridData();
        keyDescriptionLabelGridData.horizontalAlignment = GridData.FILL;
        keyDescriptionLabelGridData.grabExcessVerticalSpace = true;
        keyDescriptionLabelGridData.exclude = true;

        keyDescriptionLabel.setText(Messages.WizardPage_enterkey);
        keyDescriptionLabel.setLayoutData(keyDescriptionLabelGridData);
        keyDescriptionLabel.setVisible(false);

        keyText = new Text(keyGroup, SWT.BORDER);

        GridData keyTextGridData = new GridData();
        keyTextGridData.grabExcessHorizontalSpace = true;
        keyTextGridData.horizontalAlignment = GridData.FILL;
        keyTextGridData.verticalAlignment = GridData.CENTER;
        keyTextGridData.grabExcessVerticalSpace = true;

        keyText.setLayoutData(keyTextGridData);
        keyText.setToolTipText(Messages.AbstractClassicCryptoPage_keyToolTip);
    }

    protected void createConsoleGroup(Composite parent) {
        if (specification.hasConsoleRepresentation()) {
            Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
            GridData layoutData2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
            layoutData2.verticalIndent = 10;
			separator.setLayoutData(layoutData2);
        	
        	consoleGroup = new Group(parent, SWT.NONE);
            consoleGroup.setLayout(new GridLayout(2, false));
            GridData layoutData3 = new GridData(SWT.FILL, SWT.CENTER, true, false);
            layoutData3.verticalIndent = 10;
			consoleGroup.setLayoutData(layoutData3);
            consoleGroup.setText(Messages.AbstractClassicCryptoPage_1);

            {
                lblConsoleDescr = new Label(consoleGroup, SWT.WRAP);
                GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
                layoutData.widthHint = 320;
                layoutData.horizontalSpan = 2;
                lblConsoleDescr.setLayoutData(layoutData);
                lblConsoleDescr.setText(Messages.AbstractClassicCryptoPage_2);

//                lblConsoleFurther = new Label(consoleGroup, SWT.WRAP);
//                layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
//                layoutData.widthHint = 320;
//                layoutData.verticalIndent = 5;
//                layoutData.horizontalSpan = 2;
//                lblConsoleFurther.setLayoutData(layoutData);
//                lblConsoleFurther.setText(Messages.AbstractClassicCryptoPage_3);

                txtConsoleCommand = new Text(consoleGroup, SWT.BORDER | SWT.CENTER);
                txtConsoleCommand.setEditable(false);
                layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
                layoutData.verticalIndent = 5;
                layoutData.widthHint = generateWidthHintForConsoleTextfield();
                txtConsoleCommand.setLayoutData(layoutData);
                txtConsoleCommand.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NONE)); //$NON-NLS-1$
                // TODO: dispose
            }
        }
    }

    private int generateWidthHintForConsoleTextfield() {
		return 250;
	}

	/**
     * @return the alphabet command line part
     */
    protected String generateAlphabetPartForCommandLine() {
        String result = ""; //$NON-NLS-1$
        if (isSelectedAlphaCustom()) {
            setCommandLineWarning(String
                    .format("The custom alphabet %s cannot be used in the command. Specify an existing alphabet with the parameter '-a <alphabet name>'; currently, a standard alphabet will be used.", getAlphabetInput().getContent().getName())); //$NON-NLS-1$
        } else {
            result += "-a "; //$NON-NLS-1$
            if (getAlphabetInput().getContent().getName().contains(" ")) { //$NON-NLS-1$
                result += "\"" + getAlphabetInput().getContent().getName() + "\""; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                result += getAlphabetInput().getContent().getName();
            }
        }
        return result;
    }

    protected String quoteCmdlineArgIfNecessary(String arg) {
        if (arg.contains(" ")) { //$NON-NLS-1$
            return "\"" + arg + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return arg;
    }

    /**
     * Sets a warning to be displayed in the command line section
     * 
     * @param warning if null, the warning disappears
     */
    protected void setCommandLineWarning(String warning) {
        // TODO: handle
    }

    /**
     * This method generates a JCT console string which should result in the exact same result which would have been
     * yielded when finishing this wizard.
     * 
     * It is safe to assume when extending this function, that the wizard is filled out correctly, and the "finish"
     * button is enabled.
     * 
     * @return
     */
    protected String generateCommandLineString() {
        return ""; //$NON-NLS-1$
    }

    protected void updateCommandLineString() {
        if (isPageComplete()) {
            txtConsoleCommand.setText(generateCommandLineString());
        } else {
            txtConsoleCommand.setText(Messages.AbstractClassicCryptoPage_15);
        }
    }

    /**
     * Sets a MWizardMessage Object to this WizardPage
     * 
     * @param message
     */
    protected void setTitleBarMessage(MWizardMessage message) {
        this.setMessage(message.getMessage(), message.getMessageType());
    }

    /**
     * Excludes a control from Layout calculation
     * 
     * @param that
     * @param hideit
     */
    protected void hideObject(final Control that, final boolean hideit) {
        GridData GData = (GridData) that.getLayoutData();
        GData.exclude = hideit;
        that.setVisible(!hideit);
        Control[] myArray = { that };
        that.getParent().layout(myArray);
    }

    /**
     * Updates the Transformation Wizard Page to load the Transformation setting for a specified currentAlphabet
     * 
     * @param abstractAlphabet the name of the currentAlphabet
     */
    protected void updateTransformationPage(AbstractAlphabet abstractAlphabet) {
        TransformData myTransformation;
        if (abstractAlphabet == null) {
        	myTransformation = new TransformData(); 
        } else {
            myTransformation = AbstractClassicTransformationPage.getTransformFromName(abstractAlphabet);
        }

        ((AbstractClassicTransformationPage) super.getNextPage()).setTransformData(myTransformation);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (verificationDisplayHandler != null)
            verificationDisplayHandler.disposeTooltips();
    }

}
