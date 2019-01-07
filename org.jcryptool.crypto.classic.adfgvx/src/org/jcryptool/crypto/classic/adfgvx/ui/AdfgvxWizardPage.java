// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.jcryptool.crypto.ui.util.WidgetBubbleUIInputHandler;

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
    private final AbstractAlphabet adfgvxAlphabet = AlphabetsManager.getInstance().getAlphabetBySubName("ADFGVX"); //$NON-NLS-1$

    /** Instance of the AdfgvxFactory */
    private AdfgvxFactory factory = new AdfgvxFactory();
    private Group matrixGroup;
    private Group substititionGroup;
    private Group transpositionGroup;
    private Label transpositionDescriptionLabel;
    private Text transpositionKeyText;
    private Composite keyWordComposite;
    private Label keyWordDescriptionLabel;
    private Text keyWordText;
    private TextfieldInput<List<Character>> substitutionKeyInput;
    private TextfieldInput<String> transpositionKeyInput;

    protected String rawSubstKeyInput;

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
        for (char ch : chars) {
            substitute.add(ch);
        }
        return substitute;
    }

    private AdfgvxAlgorithmSpecification getMySpecification() {
        return (AdfgvxAlgorithmSpecification) specification;
    }

    /**
     * TranspositionKey -- transpositionKeyText -> isKeyValid(direct text) -> final transpositionKey
     * just alpha verification..
     * 
     * SubstitutionKey -- keyWordText -> isSubstKeyValid() ->getSubstKeyfrom[etc] -> final substKey
     * * verification: key in currentAlphabet, no double occurence
     * (Messages.AdfgvxWizardPage_onlyoccuronce) * RESET: einfach aus "" den standardkey umwandeln..
     * resetFlag = true; keyWordText.setText(""); //$NON-NLS-1$ resetFlag = false;
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
                List<KeyVerificator> verificators = getMySpecification().getKeyVerificatorsSubstitutionKey();
                return KeyVerificator.verify(getTextfield().getText(), getAlphabetInput().getContent(), verificators);
            }

            @Override
            public List<Character> readContent() {
                if (getTextfield().getText().equals(""))return getDefaultResult(); //$NON-NLS-1$
                return toCharacters(factory.getCipherAlphabet(adfgvxAlphabet, getTextfield().getText().toUpperCase()
                        .toCharArray()));
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
            public Text getTextfield() {
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
                return getAlphabetInput().getContent();
            }
        };

        substitutionKeyInput.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (arg == null) {
                    labelsSetText(fromCharacters(substitutionKeyInput.getContent()));

                    // save text field input for other uses
                    AdfgvxWizardPage.this.rawSubstKeyInput = substitutionKeyInput.getTextfield().getText();
                }
            }
        });

        transpositionKeyInput = new KeyInput<String>() {
            @Override
            protected InputVerificationResult verifyUserChange() {
                List<KeyVerificator> verificators = getMySpecification().getKeyVerificatorsTranspositionKey();
                return KeyVerificator.verify(getTextfield().getText(), getAlphabetInput().getContent(), verificators);
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
            public Text getTextfield() {
                return transpositionKeyText;
            }

            @Override
            public AbstractAlphabet getAlphabet() {
                return getAlphabetInput().getContent();
            }
        };
    }

    @Override
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
        verificationDisplayHandler.addAsObserverForInput(operationInput);
        verificationDisplayHandler.addAsObserverForInput(filterInput);
        verificationDisplayHandler.addAsObserverForInput(getAlphabetInput());
        verificationDisplayHandler.addAsObserverForInput(transformationInput);
        verificationDisplayHandler.addAsObserverForInput(transpositionKeyInput);
        verificationDisplayHandler.addAsObserverForInput(substitutionKeyInput);

        // static mappings (dynamic, like at operation, are handled above in the overridden method)
        verificationDisplayHandler.addInputWidgetMapping(getAlphabetInput(), alphabetCombo);
        verificationDisplayHandler.addInputWidgetMapping(filterInput, filterCheckBox);
        verificationDisplayHandler.addInputWidgetMapping(transformationInput, transformCheckBox);
        verificationDisplayHandler.addInputWidgetMapping(transpositionKeyInput, transpositionKeyText);
        verificationDisplayHandler.addInputWidgetMapping(substitutionKeyInput, keyWordText);
    }

    @Override
    protected void addPageObserver() {
        operationInput.addObserver(pageObserver);
        getAlphabetInput().addObserver(pageObserver);
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
     * @return "" if there was no subst key input yet, or the substitution key as entered in the
     *         textfield.
     */
    public String getSubstitutionKeyAsEntered() {
        return rawSubstKeyInput == null ? "" : rawSubstKeyInput;
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
        substititionGroup = new Group(parent, SWT.NONE);
        substititionGroup.setText(Messages.AdfgvxWizardPage_step1);
        substititionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        substititionGroup.setLayout(new GridLayout(2, false));
        createKeyWordGroup();
        createMatrixGroup();
        // createMatrixOptionsGroup();
    }

    /**
     * This method initializes transpositionGroup
     * 
     */
    private void createTranspositionGroup(Composite parent) {
        transpositionGroup = new Group(parent, SWT.NONE);
        transpositionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        transpositionGroup.setLayout(new GridLayout(2, false));
        transpositionGroup.setText(Messages.AdfgvxWizardPage_step2);
        transpositionDescriptionLabel = new Label(transpositionGroup, SWT.NONE);
        transpositionDescriptionLabel.setText(Messages.AdfgvxWizardPage_enterTranspKey);
        transpositionDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        transpositionKeyText = new Text(transpositionGroup, SWT.BORDER);
        transpositionKeyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        transpositionKeyText.setToolTipText(Messages.AdfgvxWizardPage_transpositionHint);
    }

    /**
     * This method initializes keyWordGroup.
     * 
     */
    private void createKeyWordGroup() {
        keyWordComposite = new Composite(substititionGroup, SWT.NONE);
        keyWordComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        keyWordComposite.setLayout(new GridLayout(2, false));
        keyWordDescriptionLabel = new Label(keyWordComposite, SWT.NONE);
        keyWordDescriptionLabel.setText(Messages.AdfgvxWizardPage_enterkeyword);
        keyWordDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
        keyWordText = new Text(keyWordComposite, SWT.BORDER);
        keyWordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        keyWordText.setToolTipText(Messages.AdfgvxWizardPage_substitutionHint);
    }

    /**
     * This method initializes matrixGroup
     * 
     */
    private void createMatrixGroup() {
        matrixGroup = new Group(substititionGroup, SWT.NONE);
        matrixGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        matrixGroup.setLayout(new GridLayout(7, false));
        matrixGroup.setText(Messages.AdfgvxWizardPage_adfgvxmatrix);
        hvLabel = new Label(matrixGroup, SWT.NONE);
        hvLabel.setText("\\"); //$NON-NLS-1$
        hvLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        hALabel = new Label(matrixGroup, SWT.NONE);
        hALabel.setText("A"); //$NON-NLS-1$
        hALabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        hDLabel = new Label(matrixGroup, SWT.NONE);
        hDLabel.setText("D"); //$NON-NLS-1$
        hDLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        hFLabel = new Label(matrixGroup, SWT.NONE);
        hFLabel.setText("F"); //$NON-NLS-1$
        hFLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        hGLabel = new Label(matrixGroup, SWT.NONE);
        hGLabel.setText("G"); //$NON-NLS-1$
        hGLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        hVLabel = new Label(matrixGroup, SWT.NONE);
        hVLabel.setText("V"); //$NON-NLS-1$
        hVLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        hXLabel = new Label(matrixGroup, SWT.NONE);
        hXLabel.setText("X"); //$NON-NLS-1$
        hXLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        vALabel = new Label(matrixGroup, SWT.NONE);
        vALabel.setText("A"); //$NON-NLS-1$
        vALabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc11 = new Label(matrixGroup, SWT.NONE);
        rc11.setText("A"); //$NON-NLS-1$
        rc11.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc12 = new Label(matrixGroup, SWT.NONE);
        rc12.setText("B"); //$NON-NLS-1$
        rc12.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc13 = new Label(matrixGroup, SWT.NONE);
        rc13.setText("C"); //$NON-NLS-1$
        rc13.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc14 = new Label(matrixGroup, SWT.NONE);
        rc14.setText("D"); //$NON-NLS-1$
        rc14.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc15 = new Label(matrixGroup, SWT.NONE);
        rc15.setText("E"); //$NON-NLS-1$
        rc15.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc16 = new Label(matrixGroup, SWT.NONE);
        rc16.setText("F"); //$NON-NLS-1$
        rc16.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        vDLabel = new Label(matrixGroup, SWT.NONE);
        vDLabel.setText("D"); //$NON-NLS-1$
        vDLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc21 = new Label(matrixGroup, SWT.NONE);
        rc21.setText("G"); //$NON-NLS-1$
        rc21.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc22 = new Label(matrixGroup, SWT.NONE);
        rc22.setText("H"); //$NON-NLS-1$
        rc22.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc23 = new Label(matrixGroup, SWT.NONE);
        rc23.setText("I"); //$NON-NLS-1$
        rc23.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc24 = new Label(matrixGroup, SWT.NONE);
        rc24.setText("J"); //$NON-NLS-1$
        rc24.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc25 = new Label(matrixGroup, SWT.NONE);
        rc25.setText("K"); //$NON-NLS-1$
        rc25.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc26 = new Label(matrixGroup, SWT.NONE);
        rc26.setText("L"); //$NON-NLS-1$
        rc26.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        vFLabel = new Label(matrixGroup, SWT.NONE);
        vFLabel.setText("F"); //$NON-NLS-1$
        vFLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc31 = new Label(matrixGroup, SWT.NONE);
        rc31.setText("M"); //$NON-NLS-1$
        rc31.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc32 = new Label(matrixGroup, SWT.NONE);
        rc32.setText("N"); //$NON-NLS-1$
        rc32.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc33 = new Label(matrixGroup, SWT.NONE);
        rc33.setText("O"); //$NON-NLS-1$
        rc33.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc34 = new Label(matrixGroup, SWT.NONE);
        rc34.setText("P"); //$NON-NLS-1$
        rc34.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc35 = new Label(matrixGroup, SWT.NONE);
        rc35.setText("Q"); //$NON-NLS-1$
        rc35.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc36 = new Label(matrixGroup, SWT.NONE);
        rc36.setText("R"); //$NON-NLS-1$
        rc36.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        vGLabel = new Label(matrixGroup, SWT.NONE);
        vGLabel.setText("G"); //$NON-NLS-1$
        vGLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc41 = new Label(matrixGroup, SWT.NONE);
        rc41.setText("S"); //$NON-NLS-1$
        rc41.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc42 = new Label(matrixGroup, SWT.NONE);
        rc42.setText("T"); //$NON-NLS-1$
        rc42.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc43 = new Label(matrixGroup, SWT.NONE);
        rc43.setText("U"); //$NON-NLS-1$
        rc43.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc44 = new Label(matrixGroup, SWT.NONE);
        rc44.setText("V"); //$NON-NLS-1$
        rc44.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc45 = new Label(matrixGroup, SWT.NONE);
        rc45.setText("W"); //$NON-NLS-1$
        rc45.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc46 = new Label(matrixGroup, SWT.NONE);
        rc46.setText("X"); //$NON-NLS-1$
        rc46.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        vVLabel = new Label(matrixGroup, SWT.NONE);
        vVLabel.setText("V"); //$NON-NLS-1$
        vVLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc51 = new Label(matrixGroup, SWT.NONE);
        rc51.setText("Y"); //$NON-NLS-1$
        rc51.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc52 = new Label(matrixGroup, SWT.NONE);
        rc52.setText("Z"); //$NON-NLS-1$
        rc52.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc53 = new Label(matrixGroup, SWT.NONE);
        rc53.setText("0"); //$NON-NLS-1$
        rc53.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc54 = new Label(matrixGroup, SWT.NONE);
        rc54.setText("1"); //$NON-NLS-1$
        rc54.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc55 = new Label(matrixGroup, SWT.NONE);
        rc55.setText("2"); //$NON-NLS-1$
        rc55.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc56 = new Label(matrixGroup, SWT.NONE);
        rc56.setText("3"); //$NON-NLS-1$
        rc56.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        vXLabel = new Label(matrixGroup, SWT.NONE);
        vXLabel.setText("X"); //$NON-NLS-1$
        vXLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc61 = new Label(matrixGroup, SWT.NONE);
        rc61.setText("4"); //$NON-NLS-1$
        rc61.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc62 = new Label(matrixGroup, SWT.NONE);
        rc62.setText("5"); //$NON-NLS-1$
        rc62.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc63 = new Label(matrixGroup, SWT.NONE);
        rc63.setText("6"); //$NON-NLS-1$
        rc63.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc64 = new Label(matrixGroup, SWT.NONE);
        rc64.setText("7"); //$NON-NLS-1$
        rc64.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc65 = new Label(matrixGroup, SWT.NONE);
        rc65.setText("8"); //$NON-NLS-1$
        rc65.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        rc66 = new Label(matrixGroup, SWT.NONE);
        rc66.setText("9"); //$NON-NLS-1$
        rc66.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    @Override
    protected void createKeyGroup(Composite parent) {
    	ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
    	scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    	scrolledComposite.setExpandHorizontal(true);
    	scrolledComposite.setExpandVertical(true);
    	
    	Composite composite = new Composite(scrolledComposite, SWT.NONE);
    	scrolledComposite.setContent(composite);
    	composite.setLayout(new GridLayout());
    	
        createSubstitutionGroup(composite);
        createTranspositionGroup(composite);
        
        scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    @Override
    protected void setHelpAvailable() {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), AdfgvxPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
    }

    // adfgvx -D -ed -kS SUBST -kT TRANSP
    @Override
    protected String generateCommandLineString() {
        String encDec = operationInput.getContent() ? "-E" : "-D";

        // String substKeyString = "";
        // for(Character c: substitutionKeyInput.getContent()) substKeyString += c;
        String keySubst = "-kS " + quoteCmdlineArgIfNecessary(keyWordText.getText());
        // String keySubst = "-kS " + quoteCmdlineArgIfNecessary(getSubstitutionKey());
        String keyTransp = "-kT " + quoteCmdlineArgIfNecessary(getTranspositionKey());

        String result = "adfgvx " + encDec + " -ed " + keySubst + " " + keyTransp;

        // result += " " + generateAlphabetPartForCommandLine();

        if (!isNonAlphaFilter())
            result += " --noFilter";
        return result;
    }

}
