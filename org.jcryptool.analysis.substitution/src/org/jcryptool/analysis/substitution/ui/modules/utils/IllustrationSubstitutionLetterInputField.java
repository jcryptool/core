//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.substitution.Activator;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.core.util.input.InputVerificationResult;

public class IllustrationSubstitutionLetterInputField extends Composite {

    private static final String RESULTTYPE_ALPHAPROBLEM = "alpha-problem"; //$NON-NLS-1$
    private Mode mode;
    private AbstractAlphabet alphabet;
    private Text text;
    private List<Character> charactersInUse = new LinkedList<Character>();
    private Composite borderComposite;

    public static enum Mode {
        VERTICAL_POPUP(true), HORIZONTAL_POPUP(true), NO_POPUP(false);

        private boolean hasMenu;

        Mode(boolean hasMenu) {
            this.hasMenu = hasMenu;
        }

        public boolean hasMenu() {
            return hasMenu;
        }
    }

    public static class NoCharacterVerificationResult extends InputVerificationResult {

        public NoCharacterVerificationResult() {
        }

        @Override
        public String getMessage() {
            return String.format(Messages.SubstitutionLetterInputField_0);
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.INFORMATION;
        }

        @Override
        public boolean isStandaloneMessage() {
            return true;
        }

        @Override
        public boolean isValid() {
            return true;
        }

    }

    public static class NoCharRepresentationDetectedVerificationResult extends InputVerificationResult {

        protected String textfieldContent;
        protected Character parsedContent;

        public NoCharRepresentationDetectedVerificationResult(String textfieldContent, Character parsedContent) {
            this.textfieldContent = textfieldContent;
            this.parsedContent = parsedContent;
        }

        @Override
        public String getMessage() {
            return String.format(Messages.SubstitutionLetterInputField_1, textfieldContent);
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.WARNING;
        }

        @Override
        public boolean isStandaloneMessage() {
            return true;
        }

        @Override
        public boolean isValid() {
            return false;
        }

    }

    public static class CharNotInAlphabetVerificationResult extends InputVerificationResult {

        private Character parsedContent;

        public CharNotInAlphabetVerificationResult(String textfieldContent, Character parsedContent,
                AbstractAlphabet plaintextAlpha) {
            this.parsedContent = parsedContent;
        }

        @Override
        public String getMessage() {
            return String.format(Messages.SubstitutionLetterInputField_2, String.valueOf(parsedContent));
        }

        @Override
        public MessageType getMessageType() {
            return MessageType.WARNING;
        }

        @Override
        public boolean isStandaloneMessage() {
            return true;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public Object getResultType() {
            return RESULTTYPE_ALPHAPROBLEM;
        }

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public IllustrationSubstitutionLetterInputField(Composite parent, AbstractAlphabet alphabet) {
        super(parent, SWT.NONE);
        this.mode = Mode.VERTICAL_POPUP;
        this.alphabet = alphabet;
        // UNDETERMINED_SUBST_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);

        initGUI(mode, alphabet);
    }

    private void initGUI(Mode mode, AbstractAlphabet alphabet) {
        int columns = mode == Mode.HORIZONTAL_POPUP ? 2 : 1;
        GridLayout layout = new GridLayout(columns, false);
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        this.setLayout(layout);

        borderComposite = new Composite(this, SWT.NONE);
        GridLayout borderGridLayout = new GridLayout();
        borderGridLayout.marginWidth = 1;
        borderGridLayout.marginHeight = 1;
        borderComposite.setLayout(borderGridLayout);

        text = new Text(borderComposite, SWT.CENTER);
        GridData txtLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        txtLayoutData.widthHint = 20;
        text.setLayoutData(txtLayoutData);
        text.setText(Messages.IllustrationSubstitutionLetterInputField_1);
        text.setEditable(false);

        if (mode.hasMenu()) {
            final Label menuBtnLabel = new Label(this, SWT.NONE);
            GridData menuBtnLabelLData = new GridData(SWT.FILL, SWT.FILL, false, false);
            menuBtnLabelLData.verticalIndent = ((mode == Mode.HORIZONTAL_POPUP) ? 0 : 4);
            menuBtnLabelLData.horizontalIndent = ((mode == Mode.HORIZONTAL_POPUP) ? 4 : 0);
            menuBtnLabel.setLayoutData(menuBtnLabelLData);
            menuBtnLabel.setAlignment(SWT.CENTER);
            Image btnImage;
            if (mode == Mode.HORIZONTAL_POPUP) {
                btnImage = ImageService.getImage(Activator.PLUGIN_ID, "icons/tiny_arrow_right.png");
            } else {
                btnImage = ImageService.getImage(Activator.PLUGIN_ID, "icons/tiny_arrow_down.png");
            }
            menuBtnLabel.setImage(btnImage);
            menuBtnLabel.setVisible(false);

            menuBtnLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseDown(MouseEvent e) {
                    showPopupMenu(menuBtnLabel);
                }
            });
        }

    }

    protected void showPopupMenu(Label menuBtnLabel) {
        final Menu popupMenu = new Menu(menuBtnLabel);

        List<Character> characterList = new LinkedList<Character>();
        for (char c : alphabet.getCharacterSet())
            characterList.add(c);
        Collections.sort(characterList, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return compareCharsForMenu(o1, o2, alphabet, getCharactersInUse());
            }
        });

        boolean usedCharsSeparatorNotYetTried = true;
        for (Character c : characterList) {
            if (this.charactersInUse.contains(c) && usedCharsSeparatorNotYetTried) {
                if (popupMenu.getItemCount() > 0) {
                    MenuItem sep = new MenuItem(popupMenu, SWT.SEPARATOR);
                    MenuItem descr = new MenuItem(popupMenu, SWT.NONE);
                    descr.setEnabled(false);
                    descr.setText(Messages.SubstitutionLetterInputField_3);
                }
                usedCharsSeparatorNotYetTried = false;
            }

            makeInsertPopupMenuItem(popupMenu, c);
        }

        popupMenu.setLocation(getDisplay().getCursorLocation());
        popupMenu.setVisible(true);
    }

    private void makeInsertPopupMenuItem(final Menu popupMenu, final Character c) {
        MenuItem item = new MenuItem(popupMenu, SWT.PUSH);
        item.setText(getSpecialCharacterStringForPopupMenu(c));
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                popupMenu.dispose();
            }
        });
    }

    private static int compareCharsForMenu(Character c1, Character c2, AbstractAlphabet alphabet,
            List<Character> charactersInUse) {
        Integer lexicalValRangeSize = alphabet.getCharacterSet().length; // alphabet size
        Integer specialCharValRangeSize = 2; // true and false

        Integer lexicalPosWeight = 1;
        Integer specialCharPosWeight = lexicalValRangeSize * lexicalPosWeight;
        Integer notInUsePosWeight = specialCharValRangeSize * specialCharPosWeight;

        Integer lexicalPos1 = 0;
        Integer lexicalPos2 = 0;
        char[] alphaSet = alphabet.getCharacterSet();
        for (int i = 0; i < alphaSet.length; i++) {
            if (c1.equals(Character.valueOf(alphaSet[i]))) {
                lexicalPos1 = i;
            }
            if (c2.equals(Character.valueOf(alphaSet[i]))) {
                lexicalPos2 = i;
            }
        }
        // reverse lexical pos, because bigger numbers mean the object is earlier in the order
        lexicalPos1 = (alphaSet.length - lexicalPos1) - 1;
        lexicalPos2 = (alphaSet.length - lexicalPos2) - 1;

        Integer specialCharVal1 = isSpecialCharacterForPopupMenu(c1) ? 1 : 0;
        Integer specialCharVal2 = isSpecialCharacterForPopupMenu(c2) ? 1 : 0;

        Integer notInUseVal1 = (!charactersInUse.contains(Character.valueOf(c1))) ? 1 : 0;
        Integer notInUseVal2 = (!charactersInUse.contains(Character.valueOf(c2))) ? 1 : 0;

        Integer val1 = lexicalPos1 * lexicalPosWeight + specialCharVal1 * specialCharPosWeight + notInUseVal1
                * notInUsePosWeight;
        Integer val2 = lexicalPos2 * lexicalPosWeight + specialCharVal2 * specialCharPosWeight + notInUseVal2
                * notInUsePosWeight;

        return (-1) * val1.compareTo(val2);
    }

    private static boolean isSpecialCharacterForPopupMenu(Character c) {
        return (int) (c) <= 32 || (int) (c) >= 126;
    }

    private static String getSpecialCharacterStringForPopupMenu(Character c) {
        if ((int) (c) >= 126) {
            return String.format("%s (%s)", String.valueOf(c), String.format("{%d}", (int) c)); //$NON-NLS-1$ //$NON-NLS-2$
        } else if ((int) (c) < 32) {
            return AbstractAlphabet.getPrintableCharRepresentation(c);
        } else if ((int) (c) == 32) {
            return Messages.SubstitutionLetterInputField_4;
        } else {
            return String.format("%s (%s)", String.valueOf(c), AbstractAlphabet.getPrintableCharRepresentation(c)); //$NON-NLS-1$ 
        }
    }

    public void setTextfieldBorderColor(Color color) {
        borderComposite.setBackground(color);
    }

    public List<Character> getCharactersInUse() {
        return charactersInUse;
    }

    public void setCharactersInUse(List<Character> charactersInUse) {
        this.charactersInUse = new ArrayList<Character>(charactersInUse);
    }

}
