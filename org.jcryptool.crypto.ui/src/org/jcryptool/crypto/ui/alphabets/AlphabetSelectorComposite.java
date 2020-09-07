//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.alphabets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.ui.CryptoUIPlugin;
import org.jcryptool.crypto.ui.alphabets.customalphabets.CustomAlphabetWizard;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class AlphabetSelectorComposite extends org.eclipse.swt.widgets.Composite {
    // TODO!! when a custom alphabet is made and saved to the alphabet store, select the newly made
    // alphabet in the combo box
    // should the selector consist only of a combo.

    /**
     * Fields: <br />
     * {@link #COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON} <br />
     * {@link #SINGLE_COMBO_BOX_ONLY_EXISTING_ALPHABETS} <br />
     * {@link #SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS} <br />
     *
     * @author Simon L
     */
    public enum Mode {

        /**
         * Mode for displaying only a combo box filled with already known alphabets (those already
         * shown in the alphabet setting page)
         */
        SINGLE_COMBO_BOX_ONLY_EXISTING_ALPHABETS,
        /**
         * Mode for displaying only a combo box filled with already known alphabets (those already
         * shown in the alphabet setting page) plus one entry which invokes a wizard for creating
         * own alphabets.
         */
        SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS,
        /**
         * Mode for offering a radiobutton choice between a combo for selecting preexistent
         * alphabets, and a wizard for creating a custom alphabet.
         */
        COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON;

        public boolean isWithCustomComboEntry() {
            return this == SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS;
        }

        public boolean isWithCustomButton() {
            return this == Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON;
        }
    }

    private Mode mode;

    private Combo comboAlphas;
    private AbstractUIInput<AbstractAlphabet> alphabetInput;
    private AlphabetAcceptor acceptor;
    private List<AbstractAlphabet> registeredAlphas;
    private AbstractAlphabet customAlphaByCombo;
    private Button btnSelectAlphabet;
    protected AbstractAlphabet customAlphaByBtn;
    private Button btnCustomAlphabet;

    private AbstractAlphabet defaultAlphabet;

	private String hintOnlyThisSelectable;

    /**
     * Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
     */
    @Override
    protected void checkSubclass() {
    }

    public static abstract class AlphabetAcceptor {
        public abstract boolean accept(AbstractAlphabet alphabet);
    }

    /**
     * Creates an alphabet selector composite with the specified mode of user interaction. See
     * {@link Mode} enum class for details.<br />
     * The default alphabet can be chosen by overriding {@link #getDefaultAlphabet()}.
     *
     * @param parent the parent control
     * @param acceptor a filter for dynamic control of which already existing alphabets should be
     *            shown in the combo box
     * @param defaultAlphabet the default alphabet. If null, or not an accepted/existing alphabet,
     *            the first one in the list will be taken.
     * @param mode the mode of user interaction
     */
    public AlphabetSelectorComposite(org.eclipse.swt.widgets.Composite parent, AlphabetAcceptor acceptor,
            AbstractAlphabet defaultAlphabet, Mode mode) {
        super(parent, SWT.NONE);
        this.mode = mode;
        this.acceptor = acceptor;
        this.defaultAlphabet = defaultAlphabet;
        registeredAlphas = new LinkedList<AbstractAlphabet>();
        initGUI();
    }

    /**
     * Creates an alphabet selector composite with the specified mode of user interaction. See
     * {@link Mode} enum class for details.<br />
     * The default alphabet can be chosen by overriding {@link #getDefaultAlphabet()}.
     *
     * @param parent the parent control
     * @param registeredAlphabetsToShow the already existing alphabets to show in the selection
     *            combo
     * @param defaultAlphabet the default alphabet. If null, or not an accepted/existing alphabet,
     *            the first one in the list will be taken.
     * @param mode the mode of user interaction
     */
    public AlphabetSelectorComposite(org.eclipse.swt.widgets.Composite parent,
            final Collection<? extends AbstractAlphabet> registeredAlphabetsToShow, AbstractAlphabet defaultAlphabet,
            Mode mode) {
        this(parent, createAcceptorFromCollection(registeredAlphabetsToShow), defaultAlphabet, mode);
    }

    /**
     * Creates an alphabet selector composite with the specified mode of user interaction. See
     * {@link Mode} enum class for details.<br />
     * The default alphabet can be chosen by overriding {@link #getDefaultAlphabet()}.
     *
     * @param parent the parent control
     * @param defaultAlphabet the default alphabet. If null, or not an accepted/existing alphabet,
     *            the first one in the list will be taken.
     * @param mode the mode of user interaction
     * @wbp.parser.constructor
     */
    public AlphabetSelectorComposite(org.eclipse.swt.widgets.Composite parent, AbstractAlphabet defaultAlphabet,
            Mode mode) {
        this(parent, createAllAlphabetsAcceptor(), defaultAlphabet, mode);
    }

    private static AlphabetAcceptor createAcceptorFromCollection(
            final Collection<? extends AbstractAlphabet> registeredAlphabetsToShow) {
        return new AlphabetAcceptor() {
            @Override
            public boolean accept(AbstractAlphabet alphabet) {
                return registeredAlphabetsToShow.contains(alphabet);
            }
        };
    }

    private static AlphabetAcceptor createAllAlphabetsAcceptor() {
        return new AlphabetAcceptor() {
            @Override
            public boolean accept(AbstractAlphabet alphabet) {
                return true;
            }
        };
    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout();
            thisLayout.marginWidth = 0;
            thisLayout.marginHeight = 0;
            if (mode.isWithCustomButton()) {
                thisLayout.numColumns = 2;
                thisLayout.makeColumnsEqualWidth = false;
            }
            this.setLayout(thisLayout);
            this.setSize(367, 223);
            {
                if (mode.isWithCustomButton()) {
                    btnSelectAlphabet = new Button(this, SWT.RADIO);
                    btnSelectAlphabet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
                    btnSelectAlphabet.setText(Messages.getString("AlphabetSelectorComposite.0")); //$NON-NLS-1$

                    btnSelectAlphabet.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            if (btnSelectAlphabet.getSelection()) {
                                customAlphaByBtn = null;

                                alphabetInput.synchronizeWithUserSide();

                                if (btnCustomAlphabet.getSelection()) {
                                    showCustomAlphabetSelection();
                                } else {
                                    resetCustomAlphabetSelection();
                                }

                                comboAlphas.setEnabled(btnSelectAlphabet.getSelection());
                            }
                        }
                    });

                }

                comboAlphas = new Combo(this, SWT.NONE);
                GridData comboAlphasLData = new GridData();
                comboAlphasLData.grabExcessHorizontalSpace = true;
                comboAlphasLData.horizontalAlignment = GridData.FILL;
                comboAlphasLData.verticalAlignment = GridData.FILL;
                comboAlphas.setLayoutData(comboAlphasLData);
                comboAlphas.setText(""); //$NON-NLS-1$

                reloadAlphabetCombo();
                
                if (hintOnlyThisSelectable.length() > 0) {
                	Label lblHint = new Label(this, SWT.WRAP);
                	GridData layoutData = new GridData();
					lblHint.setLayoutData(layoutData);
					layoutData.grabExcessHorizontalSpace=true;
					layoutData.horizontalAlignment=SWT.FILL;
					layoutData.widthHint = 350;
					lblHint.setText(hintOnlyThisSelectable);
				}

                if (mode.isWithCustomButton()) {
                    btnCustomAlphabet = new Button(this, SWT.RADIO);
                    btnCustomAlphabet.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
                    ((GridData) btnCustomAlphabet.getLayoutData()).horizontalSpan = 2;
                    btnCustomAlphabet.setText(getCustomAlphabetLabel());
                    btnCustomAlphabet.addSelectionListener(new SelectionAdapter() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            if (btnCustomAlphabet.getSelection()) {
                                customAlphaByBtn = makeCustomAlphabet(e);
                                boolean customAlphaWasMade = customAlphaByBtn != null;

                                if (customAlphaWasMade) {
                                    reloadAlphabetCombo();
                                }

                                alphabetInput.synchronizeWithUserSide();

                                if (btnCustomAlphabet.getSelection()) {
                                    showCustomAlphabetSelection();
                                } else {
                                    resetCustomAlphabetSelection();
                                }

                                comboAlphas.setEnabled(btnSelectAlphabet.getSelection());
                            }
                        }
                    });

                    comboAlphasLData.horizontalIndent = Math.max(
                            btnCustomAlphabet.computeSize(SWT.DEFAULT, SWT.DEFAULT).x
                                    - btnSelectAlphabet.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 0);
                }

                createInputObject();
                comboAlphas.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        boolean selectedItemIsCustomItem = !isRegisteredAlphabetSetInCombo();

                        if (selectedItemIsCustomItem) {
                            customAlphaByCombo = makeCustomAlphabet(e);
                            if (customAlphaByCombo != null) {
                                reloadAlphabetCombo();
                            }
                        }

                        alphabetInput.synchronizeWithUserSide();

                        selectedItemIsCustomItem = !isRegisteredAlphabetSetInCombo();
                        if (selectedItemIsCustomItem) {
                            showCustomAlphabetSelection();
                        } else {
                            resetCustomAlphabetSelection();
                        }
                    }
                });
            }

            alterUICosmetics();

            this.layout();
        } catch (Exception ex) {
            LogUtil.logError(CryptoUIPlugin.PLUGIN_ID, ex);
        }
    }

    /**
     * Hook for altering button labels etc before the layout starts. Subclass for usage, does by
     * default nothing.
     */
    protected void alterUICosmetics() {

    }

    protected AbstractAlphabet makeCustomAlphabet(SelectionEvent evt) {
        CustomAlphabetWizard wiz = new CustomAlphabetWizard();
        Shell parentShell = new Shell(Display.getDefault());
        WizardDialog d = new WizardDialog(parentShell, wiz);
        d.open();
        int returnCode = d.getReturnCode();
        if (returnCode == Window.OK) {
            return wiz.getAlphabet();
        }

        return null;
    }

    private void makeRegisteredAlphasList() {
        registeredAlphas.clear();
        for (AbstractAlphabet alpha : AlphabetsManager.getInstance().getAlphabets()) {
            if (acceptor.accept(alpha)) {
                registeredAlphas.add(alpha);
            }
        }
    }

    public void setAcceptedAlphabets(Collection<? extends AbstractAlphabet> alphas) {
        this.acceptor = createAcceptorFromCollection(alphas);
        reloadAlphabetCombo();
    }

    public void setAcceptedAlphabetsToAll() {
        this.acceptor = createAllAlphabetsAcceptor();
        reloadAlphabetCombo();
    }

    private void reloadAlphabetCombo() {
        boolean afterResetSelectCustomAlphaItem = false;
        if (comboAlphas.getSelectionIndex() == getComboIndexForUnregisteredAlphabet()) {
            afterResetSelectCustomAlphaItem = true;
        }
        AbstractAlphabet alphaB4Reset = alphabetInput == null ? null : alphabetInput.getContent();

        makeRegisteredAlphasList();
        comboAlphas.setItems(new String[0]);
        for (AbstractAlphabet alpha : registeredAlphas) {
            if (acceptor.accept(alpha)) {
                comboAlphas.add(makeAlphaStringForCombo(alpha, false));
            }
        }

        if (mode.isWithCustomComboEntry()) {
            comboAlphas.add(getCustomAlphabetLabel());
        }

        if (registeredAlphas.contains(alphaB4Reset)) {
            comboAlphas.select(getIndexForRegisteredAlphabet(alphaB4Reset));
            // alphabetInput.writeContent(alphaB4Reset);
        }
        if (afterResetSelectCustomAlphaItem) {
            comboAlphas.select(getComboIndexForUnregisteredAlphabet());
            showCustomAlphabetSelection();
        }
        if (!afterResetSelectCustomAlphaItem && !registeredAlphas.contains(alphaB4Reset)) {
            comboAlphas.select(0);
            if (alphabetInput != null)
                alphabetInput.synchronizeWithUserSide();
        }

        comboAlphas.setEnabled(comboAlphas.getItemCount() >= 2);

        hintOnlyThisSelectable = ""; //$NON-NLS-1$
        if (mode == Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON || mode == Mode.SINGLE_COMBO_BOX_ONLY_EXISTING_ALPHABETS) {
        	if(comboAlphas.getItemCount() < 2) {
        		hintOnlyThisSelectable = Messages.getString("AlphabetSelectorComposite.3"); //$NON-NLS-1$
        	}
        }
        
        if (btnSelectAlphabet != null) {
            btnSelectAlphabet.setEnabled(!registeredAlphas.isEmpty());
        }
    }

    private String makeAlphaStringForCombo(AbstractAlphabet alpha, boolean custom) {
        if (!custom) {
            return alpha.getName();
        }

        return getCustomAlphabetLabel()
                + Messages.getString("AlphabetSelectorComposite.current") + alpha.getName() + ")";//$NON-NLS-1$ //$NON-NLS-2$
    }

    private String makeAlphaStringForBtn(AbstractAlphabet alpha) {
        return getCustomAlphabetLabel()
                + Messages.getString("AlphabetSelectorComposite.current") + alpha.getName() + "; Zeichen: " + AbstractAlphabet.alphabetContentAsString(alpha.getCharacterSet()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    private int getIndexForRegisteredAlphabet(AbstractAlphabet content) {
        return registeredAlphas.indexOf(content);
    }

    private int getComboIndexForUnregisteredAlphabet() {
        return registeredAlphas.size();
    }

    private void createInputObject() {
        alphabetInput = new AbstractUIInput<AbstractAlphabet>() {

            private boolean alphabetFromComboWasCustom;
            private boolean alphabetWasFromCustomBtn;

            @Override
            protected boolean canAutocorrect(InputVerificationResult result) {
                if (result.getMessage().contains("cancelled")) { //$NON-NLS-1$
                    return true;
                }
                return super.canAutocorrect(result);
            }

            @Override
            protected void autocorrect(InputVerificationResult result) {
                writeContent(getContent());
            }

            @Override
            protected InputVerificationResult verifyUserChange() {
                if (mode.isWithCustomComboEntry() && !isRegisteredAlphabetSetInCombo() && customAlphaByCombo == null) {
                    return new InputVerificationResult() {
                        @Override
                        public boolean isValid() {
                            return false;
                        }

                        @Override
                        public boolean isStandaloneMessage() {
                            return false;
                        }

                        @Override
                        public MessageType getMessageType() {
                            return InputVerificationResult.MessageType.NONE;
                        }

                        @Override
                        public String getMessage() {
                            return "<this should not be visible> reset when custom alphabet wizard has been cancelled"; //$NON-NLS-1$
                        }
                    };
                } else if (mode.isWithCustomButton() && btnCustomAlphabet.getSelection() && customAlphaByBtn == null) {
                    return new InputVerificationResult() {
                        @Override
                        public boolean isValid() {
                            return false;
                        }

                        @Override
                        public boolean isStandaloneMessage() {
                            return false;
                        }

                        @Override
                        public MessageType getMessageType() {
                            return InputVerificationResult.MessageType.NONE;
                        }

                        @Override
                        public String getMessage() {
                            return "<this should not be visible> reset when custom alphabet wizard has been cancelled"; //$NON-NLS-1$
                        }
                    };
                } else {
                    return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
                }
            }

            @Override
            public AbstractAlphabet readContent() {
                if (mode == Mode.SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS
                        || mode == Mode.SINGLE_COMBO_BOX_ONLY_EXISTING_ALPHABETS) {
                    AbstractAlphabet selfdefinedAlphabet = null;
                    selfdefinedAlphabet = getLastSelfdefinedAlphabetFromCombo();

                    if (selfdefinedAlphabet != null) {
                        return selfdefinedAlphabet;
                    } else if (isRegisteredAlphabetSetInCombo()) {
                        return getAlphabetFromCombo();
                    } else {
                        throw new UnsupportedOperationException(
                                "Notified readContent but selected \"create new Alpha\" and no custom alphabet was found."); //$NON-NLS-1$
                    }
                }

                if (mode == Mode.COMBO_BOX_WITH_CUSTOM_ALPHABET_BUTTON) {
                    AbstractAlphabet selfdefinedAlphabet = null;
                    selfdefinedAlphabet = getLastSelfdefinedAlphabetFromBtn();

                    if (selfdefinedAlphabet != null) {
                        return selfdefinedAlphabet;
                    } else if (isRegisteredAlphabetSetInCombo()) {
                        return getAlphabetFromCombo();
                    } else {
                        // return this.getContent();
                        throw new UnsupportedOperationException(
                                "Notified readContent but selected \"create new Alpha\" and no custom alphabet was found."); //$NON-NLS-1$
                    }
                }

                return null;
            }

            private AbstractAlphabet getAlphabetFromCombo() {
                if (isRegisteredAlphabetSetInCombo() && comboAlphas.getSelectionIndex() != -1) {
                    return registeredAlphas.get(comboAlphas.getSelectionIndex());
                }
                return null;
            }

            @Override
            public void writeContent(AbstractAlphabet content) {
                writeContent(content, !isAlphabetInRegisteredList(content));
            }

            private void writeContent(AbstractAlphabet content, boolean customAlphabet) {
                if (!customAlphabet) {
                    if (getIndexForRegisteredAlphabet(content) > -1) {
                        comboAlphas.select(getIndexForRegisteredAlphabet(content));
                    } else {
                        comboAlphas.select(-1);
                    }
                } else {
                    if (mode.isWithCustomComboEntry()) {
                        customAlphaByCombo = content;
                        comboAlphas.select(getComboIndexForUnregisteredAlphabet());
                        if (content != null)
                            showCustomAlphabetSelection(content);
                    } else if (mode.isWithCustomButton()) {
                        btnCustomAlphabet.setSelection(true);
                        customAlphaByBtn = content;
                        if (content != null)
                            showCustomAlphabetSelection(content);
                    }
                }

                if (mode.isWithCustomButton()) {
                    if (getIndexForRegisteredAlphabet(content) > -1) {
                        btnSelectAlphabet.setSelection(!customAlphabet);
                        btnCustomAlphabet.setSelection(customAlphabet);
                    }
                }
            }

            private boolean isAlphabetInRegisteredList(AbstractAlphabet content) {
                return registeredAlphas.contains(content);
            }

            @Override
            protected AbstractAlphabet getDefaultContent() {
                return getDefaultAlphabet();
            }

            @Override
            public String getName() {
                return "Alphabet"; //$NON-NLS-1$
            }

            @Override
            protected void saveRawUserInput() {
                alphabetFromComboWasCustom = !isRegisteredAlphabetSetInCombo();
                if (mode.isWithCustomButton()) {
                    alphabetWasFromCustomBtn = btnCustomAlphabet.getSelection();
                }
            }

            @Override
            protected void saveDefaultRawUserInput() {
                alphabetFromComboWasCustom = false;
            }

            @Override
            protected void resetUserInput() {
                if (mode.isWithCustomComboEntry()) {
                    writeContent(getContent(), alphabetFromComboWasCustom);
                }
                if (mode.isWithCustomButton()) {
                    writeContent(getContent(), alphabetWasFromCustomBtn);
                }
            }

        };

        comboAlphas.setToolTipText(generateAlphaContentTip(alphabetInput.getContent()));
        if (mode.isWithCustomButton()) {
            btnCustomAlphabet.setToolTipText(generateAlphaContentTip(alphabetInput.getContent()));
        }

        alphabetInput.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                comboAlphas.setToolTipText(generateAlphaContentTip(alphabetInput.getContent()));
                if (mode.isWithCustomButton()) {
                    btnCustomAlphabet.setToolTipText(generateAlphaContentTip(alphabetInput.getContent()));
                }
            }
        });
    }

    private String generateAlphaContentTip(AbstractAlphabet content) {
        return Messages.getString("AlphabetSelectorComposite.6") + ": " + AbstractAlphabet.alphabetContentAsString(content.getCharacterSet()); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Shows the custom alphabet's description in the UI
     */
    protected void showCustomAlphabetSelection() {
        showCustomAlphabetSelection(alphabetInput.getContent());
    }

    protected void showCustomAlphabetSelection(AbstractAlphabet alpha) {
        if (mode.isWithCustomComboEntry()) {
            comboAlphas.setItem(getComboIndexForUnregisteredAlphabet(), makeAlphaStringForCombo(alpha, true));
        }
        if (mode.isWithCustomButton()) {
            btnCustomAlphabet.setText(makeAlphaStringForBtn(alpha));
        }
    }

    /**
     * Resets the UI elements which show the custom alphabet's description.
     */
    protected void resetCustomAlphabetSelection() {
        if (mode.isWithCustomComboEntry()) {
            comboAlphas.setItem(getComboIndexForUnregisteredAlphabet(), getCustomAlphabetLabel());
        }
        if (mode.isWithCustomButton()) {
            btnCustomAlphabet.setText(getCustomAlphabetLabel());
        }
    }

    /**
     * @return the label that is show for buttons/items which show a custom alphabet dialog on
     *         click.
     */
    protected String getCustomAlphabetLabel() {
        return Messages.getString("AlphabetSelectorComposite.1"); //$NON-NLS-1$
    }

    /**
     * @return the default Alphabet for this composite. Override to change the default alphabet, or
     *         set the default alphabet at creation.
     */
    protected AbstractAlphabet getDefaultAlphabet() {
        if (defaultAlphabet != null && acceptor.accept(defaultAlphabet)) {
            return defaultAlphabet;
        } else {
            if (acceptor.accept(AlphabetsManager.getInstance().getDefaultAlphabet())) {
                return AlphabetsManager.getInstance().getDefaultAlphabet();
            } else {
                return Arrays.asList(AlphabetsManager.getInstance().getAlphabets()).stream()
                		.filter(alpha -> isAcceptable(alpha))
                		.findFirst().orElse(AlphabetsManager.getInstance().getAlphabets()[0]);
            }
        }
    }

    private boolean isAcceptable(AbstractAlphabet alpha) {
    	return acceptor.accept(alpha);
	}

	/**
     * @return whether in the combo, a registered alphabet is selected.
     */
    protected boolean isRegisteredAlphabetSetInCombo() {
        return (comboAlphas.getSelectionIndex() >= 0) && (registeredAlphas.size() > comboAlphas.getSelectionIndex());
    }

    /**
     * @return the last custom alphabet created by the user. This method resets the custom alphabet,
     *         such that it can only read once by this method.
     */
    protected AbstractAlphabet getLastSelfdefinedAlphabetFromCombo() {
        AbstractAlphabet selfdefinedAlphabet = this.customAlphaByCombo;
        this.customAlphaByCombo = null;
        return selfdefinedAlphabet;
    }

    /**
     * @return the last custom alphabet created by the user. This method resets the custom alphabet,
     *         such that it can only read once by this method.
     */
    protected AbstractAlphabet getLastSelfdefinedAlphabetFromBtn() {
        AbstractAlphabet selfdefinedAlphabet = this.customAlphaByBtn;
        this.customAlphaByBtn = null;
        return selfdefinedAlphabet;
    }

    /**
     * @return the input object for this alphabet to which an observer may be assigned and which
     *         holds the current alphabet.
     */
    public AbstractUIInput<AbstractAlphabet> getAlphabetInput() {
        return alphabetInput;
    }

    /**
     * @return the combo control which holds the pre-existent alphabets
     */
    protected Combo getComboAlphas() {
        return comboAlphas;
    }

    /**
     * @return the button which indicates the selection of a pre-existent alphabet
     */
    protected Button getBtnSelectAlphabet() {
        return btnSelectAlphabet;
    }

    /**
     * @return the button which indicates the selection of a custom alphabet
     */
    protected Button getBtnCustomAlphabet() {
        return btnCustomAlphabet;
    }

    Map<Control, Boolean> enabledStateMap = new HashMap<Control, Boolean>();

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == false && this.isEnabled()) {
            enabledStateMap = new HashMap<Control, Boolean>();
            disableAndPopulateEnabledMap(this);
        } else if (enabled == true && !this.isEnabled()) {
            applyEnabledMap();
        }
        super.setEnabled(enabled);
    }

    public boolean isCustomAlphabetSelected() {
        if (mode.isWithCustomButton()) {
            return btnCustomAlphabet.getSelection();
        }
        if (mode.isWithCustomComboEntry()) {
            return !isRegisteredAlphabetSetInCombo();
        }
        return false;
    }

    private void applyEnabledMap() {
        for (Control c : enabledStateMap.keySet()) {
            if (!c.isDisposed()) {
                c.setEnabled(enabledStateMap.get(c));
            }
        }
    }

    private void disableAndPopulateEnabledMap(Composite node) {
        for (Control c : node.getChildren()) {
            if (!c.isDisposed()) {
                enabledStateMap.put(c, c.isEnabled());
                if (c instanceof Composite) {
                    disableAndPopulateEnabledMap((Composite) c);
                }
                c.setEnabled(false);
            }
        }
    }

    public List<AbstractAlphabet> getAvailableAlphas() {
        return registeredAlphas;
    }

}
