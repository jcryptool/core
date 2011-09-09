// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.core.util.input.TextfieldInput;
import org.jcryptool.crypto.classic.adfgvx.AdfgvxPlugin;
import org.jcryptool.crypto.classic.adfgvx.algorithm.AdfgvxAlgorithmSpecification;
import org.jcryptool.crypto.classic.adfgvx.algorithm.AdfgvxFactory;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicCryptoPage;
import org.jcryptool.crypto.classic.model.ui.wizard.KeyInput;
import org.jcryptool.crypto.classic.model.ui.wizard.util.WidgetBubbleUIInputHandler;

/**
 * Wizard page for the ADFGVX cipher.
 *
 * @author t-kern
 *
 */
public class AdfgvxWizardPage extends AbstractClassicCryptoPage {

    // the top-right corner of the matric
    private Label hvLabel;

    // the horizontal name fields
    private Label hALabel;
    private Label hDLabel;
    private Label hFLabel;
    private Label hGLabel;
    private Label hVLabel;
    private Label hXLabel;

    // the vertical name fields
    private Label vALabel;
    private Label vDLabel;
    private Label vFLabel;
    private Label vGLabel;
    private Label vVLabel;
    private Label vXLabel;

    // the matrix cells
    private Label rc11;
    private Label rc12;
    private Label rc13;
    private Label rc14;
    private Label rc15;
    private Label rc16;
    private Label rc21;
    private Label rc22;
    private Label rc23;
    private Label rc24;
    private Label rc25;
    private Label rc26;
    private Label rc31;
    private Label rc32;
    private Label rc33;
    private Label rc34;
    private Label rc35;
    private Label rc36;
    private Label rc41;
    private Label rc42;
    private Label rc43;
    private Label rc44;
    private Label rc45;
    private Label rc46;
    private Label rc51;
    private Label rc52;
    private Label rc53;
    private Label rc54;
    private Label rc55;
    private Label rc56;
    private Label rc61;
    private Label rc62;
    private Label rc63;
    private Label rc64;
    private Label rc65;
    private Label rc66;

    /** Instance of the ADFGVX currentAlphabet */
    private final AbstractAlphabet adfgvxAlphabet = AlphabetsManager.getInstance().getAlphabetByName("ADFGVX Alphabet"); //$NON-NLS-1$

    /** Instance of the AdfgvxFactory */
    private AdfgvxFactory factory = new AdfgvxFactory();
    private Group matrixGroup;
    private Group substititionGroup;
    private Group transpositionGroup;
    private Label transpositionDescriptionLabel;
    private Text transpositionKeyText;
    private Composite keyWordGroup;
    private Label keyWordDescriptionLabel;
    private Text keyWordText;
    private TextfieldInput<List<Character>> substitutionKeyInput;
    private TextfieldInput<String> transpositionKeyInput;

    /**
     * Creates a new instance of AdfgvxWizardPage.
     */
    public AdfgvxWizardPage() {
        super(Messages.AdfgvxWizardPage_adfgvx, Messages.AdfgvxWizardPage_adfgvxorder);
        setMessage(Messages.AdfgvxWizardPage_adfgvxorder);
    }

    private char[] fromCharacters(List<Character> chars) {
        char[] substitute = new char[chars.size()];
        for (int i = 0; i < chars.size(); i++)
            substitute[i] = chars.get(i);
        return substitute;
    }

    private List<Character> toCharacters(char[] chars) {
        List<Character> substitute = new LinkedList<Character>();
        for (int i = 0; i < chars.length; i++)
            substitute.add(chars[i]);
        return substitute;
    }

    /**
     * TranspositionKey -- transpositionKeyText -> isKeyValid(direct text) -> final transpositionKey just alpha
     * verification..
     *
     * SubstitutionKey -- keyWordText -> isSubstKeyValid() ->getSubstKeyfrom[etc] -> final substKey * verification: key
     * in currentAlphabet, no double occurence (Messages.AdfgvxWizardPage_onlyoccuronce) * RESET: einfach aus "" den
     * standardkey umwandeln.. resetFlag = true; keyWordText.setText(""); //$NON-NLS-1$ resetFlag = false;
     * labelsSetText(adfgvxAlphabet.getCharacterSet());
     *
     *
     */

    @Override
    protected void createKeyInputObjects() {
        substitutionKeyInput = new KeyInput<List<Character>>() {
            private List<Character> getDefaultResult() {
                return toCharacters(factory.getCipherAlphabet(adfgvxAlphabet, adfgvxAlphabet.getCharacterSet()));
            }

            @Override
            public void writeContent(List<Character> content) {
                if (content == null)
                    setTextfieldTextExternal(""); //$NON-NLS-1$
                else {
                    setTextfieldTextExternal(String.valueOf(fromCharacters(content)));
                }
            }

            @Override
            protected InputVerificationResult verifyUserChange() {
                List<KeyVerificator> verificators =
                        ((AdfgvxAlgorithmSpecification) specification).getKeyVerificatorsSubstitutionKey();
                return KeyVerificator.verify(getTextfield().getText(), alphabetInput.getContent(), verificators);
            }

            @Override
            public List<Character> readContent() {
                if (getTextfield().getText().equals(""))return getDefaultResult(); //$NON-NLS-1$
                return toCharacters(factory.getCipherAlphabet(adfgvxAlphabet,
                        getTextfield().getText().toUpperCase().toCharArray()));
            }

            @Override
            public String getName() {
                return Messages.AdfgvxWizardPage_inputNameSubstitution;
            }

            @Override
            protected List<Character> getDefaultContent() {
                return getDefaultResult();
            }

            @Override
            protected Text getTextfield() {
                return keyWordText;
            }

            @Override
            protected void saveDefaultRawUserInput() {
                super.saveDefaultRawUserInput();
                this.textForReset = ""; //$NON-NLS-1$
                this.selectionForReset = new Point(0, 0);
            }

            @Override
            public AbstractAlphabet getAlphabet() {
                return alphabetInput.getContent();
            }
        };

        substitutionKeyInput.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                if (arg == null) {
                    labelsSetText(fromCharacters(substitutionKeyInput.getContent()));
                }
            }
        });

        transpositionKeyInput = new KeyInput<String>() {
            @Override
            protected InputVerificationResult verifyUserChange() {
                List<KeyVerificator> verificators =
                        ((AdfgvxAlgorithmSpecification) specification).getKeyVerificatorsTranspositionKey();
                return KeyVerificator.verify(getTextfield().getText(), alphabetInput.getContent(), verificators);
            }

            @Override
            public String readContent() {
                return getTextfield().getText();
            }

            @Override
            public String getName() {
                return Messages.AdfgvxWizardPage_inputNameTransposition;
            }

            @Override
            protected String getDefaultContent() {
                return ""; //$NON-NLS-1$
            }

            @Override
            protected Text getTextfield() {
                return transpositionKeyText;
            }

            @Override
            public AbstractAlphabet getAlphabet() {
                return alphabetInput.getContent();
            }
        };
    }

    @Override
    protected void createInputVerificationHandler(Shell shell) {
        verificationDisplayHandler = new WidgetBubbleUIInputHandler(shell) {
            @SuppressWarnings("rawtypes")
            @Override
            protected void changeTooltipDurationAtCleaninputButNotHidden(AbstractUIInput input) {
                // vanish instantly when it is the "not changing the encryption" tooltip
                if (getLastDisplayedResultType(input) == ClassicAlgorithmSpecification.RESULT_TYPE_NOKEY) { //$NON-NLS-1$
                    tooltipMap.get(input).setTimeToVanish(-1);
                } else {
                    super.changeTooltipDurationAtCleaninputButNotHidden(input);
                }
            }

            @SuppressWarnings("rawtypes")
            @Override
            public Control mapInputToWidget(AbstractUIInput input) {
                if (input.equals(operationInput)) {
                    return (Control) operationLastSelected;
                }
                return super.mapInputToWidget(input);
            }
        };
        verificationDisplayHandler.addAsObserverForInput(operationInput);
        verificationDisplayHandler.addAsObserverForInput(filterInput);
        verificationDisplayHandler.addAsObserverForInput(alphabetInput);
        verificationDisplayHandler.addAsObserverForInput(transformationInput);
        verificationDisplayHandler.addAsObserverForInput(transpositionKeyInput);
        verificationDisplayHandler.addAsObserverForInput(substitutionKeyInput);

        // static mappings (dynamic, like at operation, are handled above in the overridden method)
        verificationDisplayHandler.addInputWidgetMapping(alphabetInput, alphabetCombo);
        verificationDisplayHandler.addInputWidgetMapping(filterInput, filterCheckBox);
        verificationDisplayHandler.addInputWidgetMapping(transformationInput, transformCheckBox);
        verificationDisplayHandler.addInputWidgetMapping(transpositionKeyInput, transpositionKeyText);
        verificationDisplayHandler.addInputWidgetMapping(substitutionKeyInput, keyWordText);
    }

    @Override
    protected void addPageObserver() {
        operationInput.addObserver(pageObserver);
        alphabetInput.addObserver(pageObserver);
        filterInput.addObserver(pageObserver);
        transformationInput.addObserver(pageObserver);
        transpositionKeyInput.addObserver(pageObserver);
        substitutionKeyInput.addObserver(pageObserver);
    }

    @Override
    /**
     * Returns <code>true</code>, if the page is complete and the wizard may finish.
     *
     * @return	<code>true</code>, if the page is complete and the wizard may finish
     */
    protected boolean mayFinish() {
        if (getTranspositionKey() != null && getTranspositionKey().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the substitution key.
     *
     * @return The substitution key
     */
    public String getSubstitutionKey() {
        return substKeyFromMatrixAlph(substitutionKeyInput.getContent());
    }

    /**
     * Returns the transposition key.
     *
     * @return The transposition key
     */
    public String getTranspositionKey() {
        return transpositionKeyInput == null ? null : transpositionKeyInput.getContent();
    }

    private String substKeyFromMatrixAlph(List<Character> characters) {
        String substitutionKey = ""; //$NON-NLS-1$
        int y = characters.size();
        if (characters.size() != 36) {
            throw new IllegalArgumentException("Alphabet is not ADFGVX-compatible (must be 36 characters)"); //$NON-NLS-1$
        }

        for (int counter = 0; counter < 36; counter++) {
            if (counter < y)
                substitutionKey = substitutionKey.concat(String.valueOf(characters.get(counter)));
        }

        return substitutionKey;
    }

    /**
     * Sets the values for the ADFGVX matrix.
     *
     * @param cAlph The character array containing the new matrix values
     */
    public void labelsSetText(char[] cAlph) {
        int y = cAlph.length;
        int i = 0;
        if (cAlph.length != 36) {
            throw new IllegalArgumentException("Alphabet is not ADFGVX-compatible (must be 36 characters)"); //$NON-NLS-1$
        }
        if (i < y) {

            rc11.setText(Character.valueOf(cAlph[i]).toString());
            rc11.redraw();
            i++;
        }
        if (i < y) {

            rc12.setText(Character.valueOf(cAlph[i]).toString());
            rc12.redraw();
            i++;
        }
        if (i < y) {

            rc13.setText(Character.valueOf(cAlph[i]).toString());
            rc13.redraw();
            i++;
        }
        if (i < y) {

            rc14.setText(Character.valueOf(cAlph[i]).toString());
            rc14.redraw();
            i++;
        }
        if (i < y) {

            rc15.setText(Character.valueOf(cAlph[i]).toString());
            rc15.redraw();
            i++;
        }
        if (i < y) {

            rc16.setText(Character.valueOf(cAlph[i]).toString());
            rc16.redraw();
            i++;
        }
        if (i < y) {

            rc21.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc22.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc23.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc24.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc25.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc26.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc31.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc32.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc33.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc34.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc35.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc36.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc41.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc42.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc43.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc44.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc45.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc46.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc51.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc52.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc53.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc54.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc55.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc56.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc61.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc62.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc63.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc64.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc65.setText(Character.valueOf(cAlph[i]).toString());
            i++;
        }
        if (i < y) {

            rc66.setText(String.valueOf(cAlph[i]));
        }

    }

    /**
     * This method initializes substitutionGroup.
     *
     */
    private void createSubstitutionGroup(Composite parent) {
        GridLayout substitutionGroupGridLayout = new GridLayout();
        substitutionGroupGridLayout.numColumns = 2;
        GridData substitutionGroupGridData = new GridData();
        substitutionGroupGridData.grabExcessHorizontalSpace = true;
        substitutionGroupGridData.horizontalAlignment = GridData.FILL;
        substitutionGroupGridData.verticalAlignment = GridData.FILL;
        substitutionGroupGridData.grabExcessVerticalSpace = true;
        substititionGroup = new Group(parent, SWT.NONE);
        substititionGroup.setLayoutData(substitutionGroupGridData);
        createKeyWordGroup();
        substititionGroup.setText(Messages.AdfgvxWizardPage_step1);
        substititionGroup.setLayout(substitutionGroupGridLayout);
        createMatrixGroup();
        // createMatrixOptionsGroup();
    }

    /**
     * This method initializes transpositionGroup
     *
     */
    private void createTranspositionGroup(Composite parent) {
        GridData transpositionKeyTextGridData = new GridData();
        transpositionKeyTextGridData.horizontalAlignment = GridData.FILL;
        transpositionKeyTextGridData.grabExcessHorizontalSpace = true;
        transpositionKeyTextGridData.verticalAlignment = GridData.CENTER;
        GridData transpositionDescriptionLabelGridData = new GridData();
        transpositionDescriptionLabelGridData.grabExcessHorizontalSpace = false;
        transpositionDescriptionLabelGridData.horizontalAlignment = GridData.BEGINNING;
        transpositionDescriptionLabelGridData.verticalAlignment = GridData.CENTER;
        transpositionDescriptionLabelGridData.grabExcessVerticalSpace = true;
        GridLayout transpositionGroupGridLayout = new GridLayout();
        transpositionGroupGridLayout.numColumns = 2;
        GridData transpositionGroupGridData = new GridData();
        transpositionGroupGridData.grabExcessHorizontalSpace = true;
        transpositionGroupGridData.horizontalAlignment = GridData.FILL;
        transpositionGroupGridData.verticalAlignment = GridData.FILL;
        transpositionGroupGridData.grabExcessVerticalSpace = true;
        transpositionGroup = new Group(parent, SWT.NONE);
        transpositionGroup.setLayoutData(transpositionGroupGridData);
        transpositionGroup.setLayout(transpositionGroupGridLayout);
        transpositionGroup.setText(Messages.AdfgvxWizardPage_step2);
        transpositionDescriptionLabel = new Label(transpositionGroup, SWT.NONE);
        transpositionDescriptionLabel.setText(Messages.AdfgvxWizardPage_enterTranspKey);
        transpositionDescriptionLabel.setLayoutData(transpositionDescriptionLabelGridData);
        transpositionKeyText = new Text(transpositionGroup, SWT.BORDER);
        transpositionKeyText.setLayoutData(transpositionKeyTextGridData);
        transpositionKeyText.setToolTipText(Messages.AdfgvxWizardPage_transpositionHint);
    }

    /**
     * This method initializes keyWordGroup.
     *
     */
    private void createKeyWordGroup() {
        GridData keyWordTextGridData = new GridData();
        keyWordTextGridData.grabExcessHorizontalSpace = true;
        keyWordTextGridData.verticalAlignment = GridData.CENTER;
        keyWordTextGridData.horizontalAlignment = GridData.FILL;
        GridData keyWordDescriptionLabelGridData = new GridData();
        keyWordDescriptionLabelGridData.horizontalAlignment = GridData.FILL;
        keyWordDescriptionLabelGridData.grabExcessHorizontalSpace = false;
        keyWordDescriptionLabelGridData.grabExcessVerticalSpace = true;
        keyWordDescriptionLabelGridData.verticalAlignment = GridData.CENTER;
        GridLayout keyWordGroupGridLayout = new GridLayout();
        keyWordGroupGridLayout.numColumns = 2;
        GridData keyWordGroupGridData = new GridData();
        keyWordGroupGridData.horizontalAlignment = GridData.FILL;
        keyWordGroupGridData.grabExcessHorizontalSpace = true;
        keyWordGroupGridData.grabExcessVerticalSpace = true;
        keyWordGroupGridData.verticalAlignment = GridData.FILL;
        keyWordGroup = new Composite(substititionGroup, SWT.NONE);
        keyWordGroup.setLayoutData(keyWordGroupGridData);
        keyWordGroup.setLayout(keyWordGroupGridLayout);
        // keyWordGroup.setText(Messages.AdfgvxWizardPage_keyword);
        keyWordDescriptionLabel = new Label(keyWordGroup, SWT.NONE);
        keyWordDescriptionLabel.setText(Messages.AdfgvxWizardPage_enterkeyword);
        keyWordDescriptionLabel.setLayoutData(keyWordDescriptionLabelGridData);
        keyWordText = new Text(keyWordGroup, SWT.BORDER);
        keyWordText.setLayoutData(keyWordTextGridData);
        keyWordText.setToolTipText(Messages.AdfgvxWizardPage_substitutionHint);
    }

    /**
     * This method initializes matrixGroup
     *
     */
    private void createMatrixGroup() {
        GridData gridData61 = new GridData();
        gridData61.grabExcessHorizontalSpace = true;
        gridData61.horizontalAlignment = GridData.FILL;
        gridData61.verticalAlignment = GridData.FILL;
        gridData61.grabExcessVerticalSpace = true;
        GridData gridData60 = new GridData();
        gridData60.grabExcessHorizontalSpace = true;
        gridData60.horizontalAlignment = GridData.FILL;
        gridData60.verticalAlignment = GridData.FILL;
        gridData60.grabExcessVerticalSpace = true;
        GridData gridData59 = new GridData();
        gridData59.grabExcessHorizontalSpace = true;
        gridData59.horizontalAlignment = GridData.FILL;
        gridData59.verticalAlignment = GridData.FILL;
        gridData59.grabExcessVerticalSpace = true;
        GridData gridData58 = new GridData();
        gridData58.grabExcessHorizontalSpace = true;
        gridData58.horizontalAlignment = GridData.FILL;
        gridData58.verticalAlignment = GridData.FILL;
        gridData58.grabExcessVerticalSpace = true;
        GridData gridData57 = new GridData();
        gridData57.grabExcessHorizontalSpace = true;
        gridData57.horizontalAlignment = GridData.FILL;
        gridData57.verticalAlignment = GridData.FILL;
        gridData57.grabExcessVerticalSpace = true;
        GridData gridData56 = new GridData();
        gridData56.grabExcessHorizontalSpace = true;
        gridData56.horizontalAlignment = GridData.FILL;
        gridData56.verticalAlignment = GridData.FILL;
        gridData56.grabExcessVerticalSpace = true;
        GridData gridData55 = new GridData();
        gridData55.grabExcessHorizontalSpace = true;
        gridData55.horizontalAlignment = GridData.FILL;
        gridData55.verticalAlignment = GridData.FILL;
        gridData55.grabExcessVerticalSpace = true;
        GridData gridData54 = new GridData();
        gridData54.grabExcessHorizontalSpace = true;
        gridData54.horizontalAlignment = GridData.FILL;
        gridData54.verticalAlignment = GridData.FILL;
        gridData54.grabExcessVerticalSpace = true;
        GridData gridData53 = new GridData();
        gridData53.grabExcessHorizontalSpace = true;
        gridData53.horizontalAlignment = GridData.FILL;
        gridData53.verticalAlignment = GridData.FILL;
        gridData53.grabExcessVerticalSpace = true;
        GridData gridData52 = new GridData();
        gridData52.grabExcessHorizontalSpace = true;
        gridData52.horizontalAlignment = GridData.FILL;
        gridData52.verticalAlignment = GridData.FILL;
        gridData52.grabExcessVerticalSpace = true;
        GridData gridData51 = new GridData();
        gridData51.grabExcessHorizontalSpace = true;
        gridData51.horizontalAlignment = GridData.FILL;
        gridData51.verticalAlignment = GridData.FILL;
        gridData51.grabExcessVerticalSpace = true;
        GridData gridData50 = new GridData();
        gridData50.grabExcessHorizontalSpace = true;
        gridData50.horizontalAlignment = GridData.FILL;
        gridData50.verticalAlignment = GridData.FILL;
        gridData50.grabExcessVerticalSpace = true;
        GridData gridData49 = new GridData();
        gridData49.grabExcessHorizontalSpace = true;
        gridData49.horizontalAlignment = GridData.FILL;
        gridData49.verticalAlignment = GridData.FILL;
        gridData49.grabExcessVerticalSpace = true;
        GridData gridData48 = new GridData();
        gridData48.grabExcessHorizontalSpace = true;
        gridData48.horizontalAlignment = GridData.FILL;
        gridData48.verticalAlignment = GridData.FILL;
        gridData48.grabExcessVerticalSpace = true;
        GridData gridData47 = new GridData();
        gridData47.grabExcessHorizontalSpace = true;
        gridData47.horizontalAlignment = GridData.FILL;
        gridData47.verticalAlignment = GridData.FILL;
        gridData47.grabExcessVerticalSpace = true;
        GridData gridData46 = new GridData();
        gridData46.grabExcessHorizontalSpace = true;
        gridData46.horizontalAlignment = GridData.FILL;
        gridData46.verticalAlignment = GridData.FILL;
        gridData46.grabExcessVerticalSpace = true;
        GridData gridData45 = new GridData();
        gridData45.grabExcessHorizontalSpace = true;
        gridData45.horizontalAlignment = GridData.FILL;
        gridData45.verticalAlignment = GridData.FILL;
        gridData45.grabExcessVerticalSpace = true;
        GridData gridData44 = new GridData();
        gridData44.grabExcessHorizontalSpace = true;
        gridData44.horizontalAlignment = GridData.FILL;
        gridData44.verticalAlignment = GridData.FILL;
        gridData44.grabExcessVerticalSpace = true;
        GridData gridData43 = new GridData();
        gridData43.grabExcessHorizontalSpace = true;
        gridData43.horizontalAlignment = GridData.FILL;
        gridData43.verticalAlignment = GridData.FILL;
        gridData43.grabExcessVerticalSpace = true;
        GridData gridData42 = new GridData();
        gridData42.grabExcessHorizontalSpace = true;
        gridData42.horizontalAlignment = GridData.FILL;
        gridData42.verticalAlignment = GridData.FILL;
        gridData42.grabExcessVerticalSpace = true;
        GridData gridData41 = new GridData();
        gridData41.grabExcessHorizontalSpace = true;
        gridData41.horizontalAlignment = GridData.FILL;
        gridData41.verticalAlignment = GridData.FILL;
        gridData41.grabExcessVerticalSpace = true;
        GridData gridData40 = new GridData();
        gridData40.grabExcessHorizontalSpace = true;
        gridData40.horizontalAlignment = GridData.FILL;
        gridData40.verticalAlignment = GridData.FILL;
        gridData40.grabExcessVerticalSpace = true;
        GridData gridData39 = new GridData();
        gridData39.grabExcessHorizontalSpace = true;
        gridData39.horizontalAlignment = GridData.FILL;
        gridData39.verticalAlignment = GridData.FILL;
        gridData39.grabExcessVerticalSpace = true;
        GridData gridData38 = new GridData();
        gridData38.grabExcessHorizontalSpace = true;
        gridData38.horizontalAlignment = GridData.FILL;
        gridData38.verticalAlignment = GridData.FILL;
        gridData38.grabExcessVerticalSpace = true;
        GridData gridData37 = new GridData();
        gridData37.grabExcessHorizontalSpace = true;
        gridData37.horizontalAlignment = GridData.FILL;
        gridData37.verticalAlignment = GridData.FILL;
        gridData37.grabExcessVerticalSpace = true;
        GridData gridData36 = new GridData();
        gridData36.grabExcessHorizontalSpace = true;
        gridData36.horizontalAlignment = GridData.FILL;
        gridData36.verticalAlignment = GridData.FILL;
        gridData36.grabExcessVerticalSpace = true;
        GridData gridData35 = new GridData();
        gridData35.grabExcessHorizontalSpace = true;
        gridData35.horizontalAlignment = GridData.FILL;
        gridData35.verticalAlignment = GridData.FILL;
        gridData35.grabExcessVerticalSpace = true;
        GridData gridData34 = new GridData();
        gridData34.grabExcessHorizontalSpace = true;
        gridData34.horizontalAlignment = GridData.FILL;
        gridData34.verticalAlignment = GridData.FILL;
        gridData34.grabExcessVerticalSpace = true;
        GridData gridData33 = new GridData();
        gridData33.grabExcessHorizontalSpace = true;
        gridData33.horizontalAlignment = GridData.FILL;
        gridData33.verticalAlignment = GridData.FILL;
        gridData33.grabExcessVerticalSpace = true;
        GridData gridData32 = new GridData();
        gridData32.grabExcessHorizontalSpace = true;
        gridData32.horizontalAlignment = GridData.FILL;
        gridData32.verticalAlignment = GridData.FILL;
        gridData32.grabExcessVerticalSpace = true;
        GridData gridData31 = new GridData();
        gridData31.grabExcessHorizontalSpace = true;
        gridData31.horizontalAlignment = GridData.FILL;
        gridData31.verticalAlignment = GridData.FILL;
        gridData31.grabExcessVerticalSpace = true;
        GridData gridData30 = new GridData();
        gridData30.grabExcessHorizontalSpace = true;
        gridData30.horizontalAlignment = GridData.FILL;
        gridData30.verticalAlignment = GridData.FILL;
        gridData30.grabExcessVerticalSpace = true;
        GridData gridData29 = new GridData();
        gridData29.grabExcessHorizontalSpace = true;
        gridData29.horizontalAlignment = GridData.FILL;
        gridData29.verticalAlignment = GridData.FILL;
        gridData29.grabExcessVerticalSpace = true;
        GridData gridData28 = new GridData();
        gridData28.grabExcessHorizontalSpace = true;
        gridData28.horizontalAlignment = GridData.FILL;
        gridData28.verticalAlignment = GridData.FILL;
        gridData28.grabExcessVerticalSpace = true;
        GridData gridData27 = new GridData();
        gridData27.grabExcessHorizontalSpace = true;
        gridData27.horizontalAlignment = GridData.FILL;
        gridData27.verticalAlignment = GridData.FILL;
        gridData27.grabExcessVerticalSpace = true;
        GridData gridData26 = new GridData();
        gridData26.grabExcessHorizontalSpace = true;
        gridData26.horizontalAlignment = GridData.FILL;
        gridData26.verticalAlignment = GridData.FILL;
        gridData26.grabExcessVerticalSpace = true;
        GridData gridData25 = new GridData();
        gridData25.grabExcessHorizontalSpace = true;
        gridData25.horizontalAlignment = GridData.FILL;
        gridData25.verticalAlignment = GridData.FILL;
        gridData25.grabExcessVerticalSpace = true;
        GridData gridData24 = new GridData();
        gridData24.grabExcessHorizontalSpace = true;
        gridData24.horizontalAlignment = GridData.FILL;
        gridData24.verticalAlignment = GridData.FILL;
        gridData24.grabExcessVerticalSpace = true;
        GridData gridData23 = new GridData();
        gridData23.grabExcessHorizontalSpace = true;
        gridData23.horizontalAlignment = GridData.FILL;
        gridData23.verticalAlignment = GridData.FILL;
        gridData23.grabExcessVerticalSpace = true;
        GridData gridData22 = new GridData();
        gridData22.grabExcessHorizontalSpace = true;
        gridData22.horizontalAlignment = GridData.FILL;
        gridData22.verticalAlignment = GridData.FILL;
        gridData22.grabExcessVerticalSpace = true;
        GridData gridData21 = new GridData();
        gridData21.grabExcessHorizontalSpace = true;
        gridData21.horizontalAlignment = GridData.FILL;
        gridData21.verticalAlignment = GridData.FILL;
        gridData21.grabExcessVerticalSpace = true;
        GridData gridData20 = new GridData();
        gridData20.grabExcessHorizontalSpace = true;
        gridData20.horizontalAlignment = GridData.FILL;
        gridData20.verticalAlignment = GridData.FILL;
        gridData20.grabExcessVerticalSpace = true;
        GridData gridData19 = new GridData();
        gridData19.grabExcessHorizontalSpace = true;
        gridData19.horizontalAlignment = GridData.FILL;
        gridData19.verticalAlignment = GridData.FILL;
        gridData19.grabExcessVerticalSpace = true;
        GridData gridData18 = new GridData();
        gridData18.grabExcessHorizontalSpace = true;
        gridData18.horizontalAlignment = GridData.FILL;
        gridData18.verticalAlignment = GridData.FILL;
        gridData18.grabExcessVerticalSpace = true;
        GridData gridData17 = new GridData();
        gridData17.grabExcessHorizontalSpace = true;
        gridData17.horizontalAlignment = GridData.FILL;
        gridData17.verticalAlignment = GridData.FILL;
        gridData17.grabExcessVerticalSpace = true;
        GridData gridData16 = new GridData();
        gridData16.grabExcessHorizontalSpace = true;
        gridData16.horizontalAlignment = GridData.FILL;
        gridData16.verticalAlignment = GridData.FILL;
        gridData16.grabExcessVerticalSpace = true;
        GridData gridData15 = new GridData();
        gridData15.grabExcessHorizontalSpace = true;
        gridData15.horizontalAlignment = GridData.FILL;
        gridData15.verticalAlignment = GridData.FILL;
        gridData15.grabExcessVerticalSpace = true;
        GridData gridData14 = new GridData();
        gridData14.grabExcessHorizontalSpace = true;
        gridData14.horizontalAlignment = GridData.FILL;
        gridData14.verticalAlignment = GridData.FILL;
        gridData14.grabExcessVerticalSpace = true;
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.verticalAlignment = GridData.FILL;
        gridData2.grabExcessVerticalSpace = true;
        GridLayout gridLayout5 = new GridLayout();
        gridLayout5.numColumns = 7;
        GridData gridData5 = new GridData();
        gridData5.verticalSpan = 2;
        gridData5.verticalAlignment = GridData.FILL;
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.grabExcessVerticalSpace = true;
        gridData5.horizontalAlignment = GridData.FILL;
        matrixGroup = new Group(substititionGroup, SWT.NONE);
        matrixGroup.setLayoutData(gridData5);
        matrixGroup.setLayout(gridLayout5);
        matrixGroup.setText(Messages.AdfgvxWizardPage_adfgvxmatrix);
        hvLabel = new Label(matrixGroup, SWT.NONE);
        hvLabel.setText("\\"); //$NON-NLS-1$
        hvLabel.setLayoutData(gridData2);
        hALabel = new Label(matrixGroup, SWT.NONE);
        hALabel.setText("A"); //$NON-NLS-1$
        hALabel.setLayoutData(gridData14);
        hDLabel = new Label(matrixGroup, SWT.NONE);
        hDLabel.setText("D"); //$NON-NLS-1$
        hDLabel.setLayoutData(gridData15);
        hFLabel = new Label(matrixGroup, SWT.NONE);
        hFLabel.setText("F"); //$NON-NLS-1$
        hFLabel.setLayoutData(gridData16);
        hGLabel = new Label(matrixGroup, SWT.NONE);
        hGLabel.setText("G"); //$NON-NLS-1$
        hGLabel.setLayoutData(gridData17);
        hVLabel = new Label(matrixGroup, SWT.NONE);
        hVLabel.setText("V"); //$NON-NLS-1$
        hVLabel.setLayoutData(gridData18);
        hXLabel = new Label(matrixGroup, SWT.NONE);
        hXLabel.setText("X"); //$NON-NLS-1$
        hXLabel.setLayoutData(gridData19);
        vALabel = new Label(matrixGroup, SWT.NONE);
        vALabel.setText("A"); //$NON-NLS-1$
        vALabel.setLayoutData(gridData20);
        rc11 = new Label(matrixGroup, SWT.NONE);
        rc11.setText("A"); //$NON-NLS-1$
        rc11.setLayoutData(gridData21);
        rc12 = new Label(matrixGroup, SWT.NONE);
        rc12.setText("B"); //$NON-NLS-1$
        rc12.setLayoutData(gridData22);
        rc13 = new Label(matrixGroup, SWT.NONE);
        rc13.setText("C"); //$NON-NLS-1$
        rc13.setLayoutData(gridData23);
        rc14 = new Label(matrixGroup, SWT.NONE);
        rc14.setText("D"); //$NON-NLS-1$
        rc14.setLayoutData(gridData24);
        rc15 = new Label(matrixGroup, SWT.NONE);
        rc15.setText("E"); //$NON-NLS-1$
        rc15.setLayoutData(gridData25);
        rc16 = new Label(matrixGroup, SWT.NONE);
        rc16.setText("F"); //$NON-NLS-1$
        rc16.setLayoutData(gridData26);
        vDLabel = new Label(matrixGroup, SWT.NONE);
        vDLabel.setText("D"); //$NON-NLS-1$
        vDLabel.setLayoutData(gridData27);
        rc21 = new Label(matrixGroup, SWT.NONE);
        rc21.setText("G"); //$NON-NLS-1$
        rc21.setLayoutData(gridData28);
        rc22 = new Label(matrixGroup, SWT.NONE);
        rc22.setText("H"); //$NON-NLS-1$
        rc22.setLayoutData(gridData29);
        rc23 = new Label(matrixGroup, SWT.NONE);
        rc23.setText("I"); //$NON-NLS-1$
        rc23.setLayoutData(gridData30);
        rc24 = new Label(matrixGroup, SWT.NONE);
        rc24.setText("J"); //$NON-NLS-1$
        rc24.setLayoutData(gridData31);
        rc25 = new Label(matrixGroup, SWT.NONE);
        rc25.setText("K"); //$NON-NLS-1$
        rc25.setLayoutData(gridData32);
        rc26 = new Label(matrixGroup, SWT.NONE);
        rc26.setText("L"); //$NON-NLS-1$
        rc26.setLayoutData(gridData33);
        vFLabel = new Label(matrixGroup, SWT.NONE);
        vFLabel.setText("F"); //$NON-NLS-1$
        vFLabel.setLayoutData(gridData34);
        rc31 = new Label(matrixGroup, SWT.NONE);
        rc31.setText("M"); //$NON-NLS-1$
        rc31.setLayoutData(gridData35);
        rc32 = new Label(matrixGroup, SWT.NONE);
        rc32.setText("N"); //$NON-NLS-1$
        rc32.setLayoutData(gridData36);
        rc33 = new Label(matrixGroup, SWT.NONE);
        rc33.setText("O"); //$NON-NLS-1$
        rc33.setLayoutData(gridData37);
        rc34 = new Label(matrixGroup, SWT.NONE);
        rc34.setText("P"); //$NON-NLS-1$
        rc34.setLayoutData(gridData38);
        rc35 = new Label(matrixGroup, SWT.NONE);
        rc35.setText("Q"); //$NON-NLS-1$
        rc35.setLayoutData(gridData39);
        rc36 = new Label(matrixGroup, SWT.NONE);
        rc36.setText("R"); //$NON-NLS-1$
        rc36.setLayoutData(gridData40);
        vGLabel = new Label(matrixGroup, SWT.NONE);
        vGLabel.setText("G"); //$NON-NLS-1$
        vGLabel.setLayoutData(gridData41);
        rc41 = new Label(matrixGroup, SWT.NONE);
        rc41.setText("S"); //$NON-NLS-1$
        rc41.setLayoutData(gridData42);
        rc42 = new Label(matrixGroup, SWT.NONE);
        rc42.setText("T"); //$NON-NLS-1$
        rc42.setLayoutData(gridData43);
        rc43 = new Label(matrixGroup, SWT.NONE);
        rc43.setText("U"); //$NON-NLS-1$
        rc43.setLayoutData(gridData44);
        rc44 = new Label(matrixGroup, SWT.NONE);
        rc44.setText("V"); //$NON-NLS-1$
        rc44.setLayoutData(gridData45);
        rc45 = new Label(matrixGroup, SWT.NONE);
        rc45.setText("W"); //$NON-NLS-1$
        rc45.setLayoutData(gridData46);
        rc46 = new Label(matrixGroup, SWT.NONE);
        rc46.setText("X"); //$NON-NLS-1$
        rc46.setLayoutData(gridData47);
        vVLabel = new Label(matrixGroup, SWT.NONE);
        vVLabel.setText("V"); //$NON-NLS-1$
        vVLabel.setLayoutData(gridData48);
        rc51 = new Label(matrixGroup, SWT.NONE);
        rc51.setText("Y"); //$NON-NLS-1$
        rc51.setLayoutData(gridData49);
        rc52 = new Label(matrixGroup, SWT.NONE);
        rc52.setText("Z"); //$NON-NLS-1$
        rc52.setLayoutData(gridData50);
        rc53 = new Label(matrixGroup, SWT.NONE);
        rc53.setText("0"); //$NON-NLS-1$
        rc53.setLayoutData(gridData51);
        rc54 = new Label(matrixGroup, SWT.NONE);
        rc54.setText("1"); //$NON-NLS-1$
        rc54.setLayoutData(gridData52);
        rc55 = new Label(matrixGroup, SWT.NONE);
        rc55.setText("2"); //$NON-NLS-1$
        rc55.setLayoutData(gridData53);
        rc56 = new Label(matrixGroup, SWT.NONE);
        rc56.setText("3"); //$NON-NLS-1$
        rc56.setLayoutData(gridData54);
        vXLabel = new Label(matrixGroup, SWT.NONE);
        vXLabel.setText("X"); //$NON-NLS-1$
        vXLabel.setLayoutData(gridData55);
        rc61 = new Label(matrixGroup, SWT.NONE);
        rc61.setText("4"); //$NON-NLS-1$
        rc61.setLayoutData(gridData56);
        rc62 = new Label(matrixGroup, SWT.NONE);
        rc62.setText("5"); //$NON-NLS-1$
        rc62.setLayoutData(gridData57);
        rc63 = new Label(matrixGroup, SWT.NONE);
        rc63.setText("6"); //$NON-NLS-1$
        rc63.setLayoutData(gridData58);
        rc64 = new Label(matrixGroup, SWT.NONE);
        rc64.setText("7"); //$NON-NLS-1$
        rc64.setLayoutData(gridData59);
        rc65 = new Label(matrixGroup, SWT.NONE);
        rc65.setText("8"); //$NON-NLS-1$
        rc65.setLayoutData(gridData60);
        rc66 = new Label(matrixGroup, SWT.NONE);
        rc66.setText("9"); //$NON-NLS-1$
        rc66.setLayoutData(gridData61);
    }

    @Override
    protected void createKeyGroup(Composite parent) {
        createSubstitutionGroup(parent);
        createTranspositionGroup(parent);
    }

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), AdfgvxPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

}
