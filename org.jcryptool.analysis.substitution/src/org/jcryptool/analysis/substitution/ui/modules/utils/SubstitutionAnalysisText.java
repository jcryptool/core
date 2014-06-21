package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class SubstitutionAnalysisText extends Composite {

    private static final int WIDTH_HINT_GLOBAL = 400;
    private boolean upperLowerCipherMode;
    private String text;
    private HashMap<Character, Character> substitutions;
    private ScrolledComposite scrolledComposite;
    private Composite scrollCompMain;
    private Text textCipher;
    private Text textPlain;
    private String lastPlaintext;

    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public SubstitutionAnalysisText(Composite parent, int style, String text, boolean upperLowerCipherMode) {
        super(parent, style);
        this.substitutions = new HashMap<Character, Character>();
        this.text = text;
        this.upperLowerCipherMode = upperLowerCipherMode;

        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        this.setLayout(layout);

        scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        layoutData.widthHint = WIDTH_HINT_GLOBAL;
        scrolledComposite.setLayoutData(layoutData);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        scrollCompMain = new Composite(scrolledComposite, SWT.NONE);
        scrollCompMain.setLayout(new GridLayout(2, false));
        scrollCompMain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

        Label lblCipher = new Label(scrollCompMain, SWT.NONE);
        lblCipher.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false));
        lblCipher.setText(Messages.SubstitutionAnalysisText_0);

        textCipher = new Text(scrollCompMain, SWT.NONE);
        textCipher.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false));
        textCipher.setEditable(false);
        textCipher.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL)); //$NON-NLS-1$

        Label lblPlain = new Label(scrollCompMain, SWT.NONE);
        lblPlain.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        lblPlain.setText(Messages.SubstitutionAnalysisText_2);

        textPlain = new Text(scrollCompMain, SWT.NONE);
        textPlain.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL)); //$NON-NLS-1$
        textPlain.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
        textPlain.setEditable(false);

        scrolledComposite.setContent(scrollCompMain);
        fillContent();

    }

    public void setCharacterSubstitutions(Map<Character, Character> substitutions) {
        this.substitutions = new HashMap<Character, Character>(substitutions);
    }

    public void setText(String text) {
        this.text = text;
        fillContent();
    }

    public void setMapping(Map<Character, Character> mapping) {
        this.substitutions = new HashMap<Character, Character>(mapping);
        setTextfieldContents(this.text);
    }

    private void fillContent() {
        setTextfieldContents(this.text);
        scrolledComposite.setMinSize(scrollCompMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        this.layout(new Control[] { textCipher, scrolledComposite });
    }

    private void setTextfieldContents(String text) {
        String ciphertext = text;

        List<Integer> substitutionPositions = new LinkedList<Integer>();
        StringBuilder upperLowerForm = new StringBuilder();
        String plaintext = generatePlaintext(ciphertext, this.substitutions, substitutionPositions, upperLowerForm);
        this.lastPlaintext = plaintext;

        displayCiphertext(ciphertext);
        displayPlaintext(plaintext, substitutionPositions, upperLowerForm.toString());
    }

    public String getLastPlaintext() {
        return lastPlaintext;
    }

    private void displayPlaintext(String plaintext, List<Integer> substitutionPositions, String upperLowerForm) {
        String textToSet = plaintext;
        if (upperLowerCipherMode) {
            textToSet = upperLowerForm;
        }

        textPlain.setText(textToSet);
    }

    private void displayCiphertext(String ciphertext) {
        String textToDisplay = ciphertext;
        if (this.upperLowerCipherMode) {
            textToDisplay = textToDisplay.toUpperCase();
        }
        textCipher.setText(ciphertext);
    }

    private String generatePlaintext(String ciphertext, HashMap<Character, Character> substitutions2,
            List<Integer> substitutionPositionsToFill, StringBuilder uppercaseFormBuilder) {
        StringBuilder b = new StringBuilder();
        char[] charArray = ciphertext.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            Character character = substitutions2.get(c);
            if (character != null) {
                substitutionPositionsToFill.add(i);
                b.append(character);
                uppercaseFormBuilder.append(Character.toLowerCase(character));
            } else {
                b.append(c);
                uppercaseFormBuilder.append(Character.toUpperCase(c));
            }
        }
        return b.toString();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
