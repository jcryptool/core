// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.viterbi.views;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.viterbi.ViterbiPlugin;
import org.jcryptool.visual.viterbi.algorithm.BitwiseXOR;
import org.jcryptool.visual.viterbi.algorithm.Combination;
import org.jcryptool.visual.viterbi.algorithm.IO;
import org.jcryptool.visual.viterbi.algorithm.LanguageModel;
import org.jcryptool.visual.viterbi.algorithm.NGramProvider;
import org.jcryptool.visual.viterbi.algorithm.Path;
import org.jcryptool.visual.viterbi.algorithm.Viterbi;
import org.jcryptool.visual.viterbi.algorithm.ViterbiObserver;

/**
 * This class generates the content of the "Viterbi" tab. With this tab the user can break the running key cipher
 * created in the first tab.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class ViterbiComposite extends Composite implements ViterbiObserver {

    /* set default values */
    private static final int HORIZONTAL_SPACING = 30;
    private static final int MARGIN_WIDTH = 30;

    private static final int LOADBUTTONHEIGHT = 30;
    private static final int LOADBUTTONWIDTH = 120;

    private static final int CONTINUEBUTTONHEIGHT = 50;
    private static final int CONTINUEBUTTONWIDTH = 150;

    /* colors for backgrounds. */
    private static final Color WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

    private Text cipher;
    private Text solution1;
    private Text solution2;
    private Button text;
    private Button hex;
    // the ciphertext that is displayed in the textfield and can analyzed
    // with the viterbi algorithm
    private String cipherString = "";
    private Combination combi = new BitwiseXOR();
    private Button de;
    private Button en;
    private Combo nGramDrop;
    private Combo pathDrop;
    /* latest path computed by the viterbi algorithm */
    private Path currentPath;
    private Button startButton;
    private boolean isRunning; /* if the viterbi-algorithm is runnung */
    private Viterbi viterbi;
    StyledText stDescription;

    private static final char DEFAULT_CHARACTER_SET_BEGIN = '\u0000';
    private static final char DEFAULT_CHARACTER_SET_END = '\u00ff';

    private static final int MAX_NGRAM_SIZE = 5; //$NON-NLS-1$
    private URL ngramsUrl = null;

    /**
     * Creates the Viterbi tab
     *
     * @param parent where the tab is added to
     * @param style
     * @param viterbiView used to synchronize with the xor tab
     */
    public ViterbiComposite(final Composite parent, final int style, ViterbiView viterbiView) {
        super(parent, style);

        try {
            ngramsUrl = FileLocator.toFileURL((ViterbiPlugin.getDefault().getBundle().getEntry("/")));
        } catch (IOException ex) {
            LogUtil.logError(ViterbiPlugin.PLUGIN_ID, ex);
        }

        setLayout(new GridLayout());
        createHead();
        createInput();
        createCalculation();
        createResult();
    }

    /**
     * Sets the default texts. This is just a bugfix, because setting the texts earlier would
     * destroy the layout.
     */
    public void displayDefaultTexts() {
    	stDescription.setText(Messages.ViterbiComposite_description);
    }


    /**
     * Generates the head of the tab. The head has a title and a description.
     */
    private void createHead() {
        final Composite head = new Composite(this, SWT.NONE);
        head.setBackground(WHITE);
        head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        head.setLayout(new GridLayout());

        final Label label = new Label(head, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(WHITE);
        label.setText(Messages.ViterbiComposite_tab_title);

        stDescription = new StyledText(head, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    /**
     *
     * Creates the first line of the viterbi tab content. The values may be filled by the XOR Tab.
     *
     */
    public void createInput() {

        final Group g = new Group(this, SWT.NONE);
        g.setText(Messages.ViterbiComposite_input_header);
        final GridLayout gl = new GridLayout(3, false); //$NON-NLS-1$
        gl.marginWidth = MARGIN_WIDTH;
        gl.horizontalSpacing = HORIZONTAL_SPACING;
        g.setLayout(gl);
        g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createLoadCipher(g);
        createCipher(g);
        createEncodingModeArea(g);

    }

    /**
     * This class makes a button that is used to read input from a file and print it into a textfield.
     *
     * The format of the text printed depends on the format selected.
     *
     * @param parent the parent item, where the button is added to
     */
    private void createLoadCipher(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(new GridLayout());

        Label plain1Label = new Label(canvas, SWT.PUSH);
        plain1Label.setText(Messages.XORComposite_cipher);

        final GridData fDloadButton = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
        Button loadCipher = new Button(canvas, SWT.PUSH);
        loadCipher.setText(Messages.XORComposite_loadFile);
        loadCipher.setLayoutData(fDloadButton);

        loadCipher.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
                dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());

                String filename = dialog.open();
                if (filename != null) {
                    cipherString = new IO().read(filename, "\r\n");

                    if (text.getSelection()) {
                        cipher.setText(replaceUnprintableChars(cipherString, "\ufffd"));
                    } else {
                        cipher.setText(stringToHex(cipherString));
                    }
                }
            }
        });
    }

    /**
     * Creates a text field for the ciphertext.
     *
     * @param parent the component to add the ciphertext to
     */
    private void createCipher(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);

        cipher = new Text(parent, SWT.NONE | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        cipher.setLayoutData(gd);
        cipher.setEnabled(false);

    }

    /**
     * This class creates radio buttons. It is used to determine the encoding mode
     *
     * @param parent
     */
    private void createEncodingModeArea(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        canvas.setLayout(new GridLayout());

        final Canvas encodingmod = new Canvas(canvas, SWT.NONE);
        encodingmod.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        encodingmod.setLayout(new FormLayout());

        final GridData gDloadButton = new GridData(LOADBUTTONWIDTH, 100);
        encodingmod.setLayoutData(gDloadButton);

        Group options = new Group(encodingmod, SWT.NONE);
        options.setLayout(new GridLayout());
        options.setText(Messages.XORComposite_encodingmod_header);

        hex = new Button(options, SWT.RADIO);
        text = new Button(options, SWT.RADIO);

        hex.setSelection(true);

        hex.setText(Messages.XORComposite_EncodingMode_RadioHEX);
        text.setText(Messages.XORComposite_EncodingMode_RadioUNI);

        hex.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                hex.setSelection(true);
                text.setSelection(false);

                // cipher.setEnabled(false);
                cipher.setText(stringToHex(cipherString));

            }
        });

        text.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                text.setSelection(true);
                hex.setSelection(false);

                // cipher.setEnabled(true);

                cipher.setText(ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd"));

            }
        });
    }

    /**
     * Creates the second line of the viterbi tab content, where parameters of the viterbi algorithm can be set and the
     * calculation can be started/canceled
     */
    public void createCalculation() {

        final Group g = new Group(this, SWT.NONE);
        g.setText(Messages.ViterbiComposite_calculation_header);
        g.setLayout(new GridLayout());
        g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        final Canvas canvas = new Canvas(g, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        final GridLayout gl = new GridLayout(3, false);
        gl.marginWidth = 0;
        gl.horizontalSpacing = HORIZONTAL_SPACING + 100;
        canvas.setLayout(gl);

        createLanguage(canvas);
        createOptions(canvas);
        createStartButton(canvas);

    }

    /**
     * Creates radio buttons to select the language.
     */
    private void createLanguage(final Composite parent) {

        final Canvas language = new Canvas(parent, SWT.NONE);
        language.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        language.setLayout(new GridLayout());

        Group options = new Group(language, SWT.NONE);
        options.setLayout(new GridLayout());
        options.setText(Messages.ViterbiComposite_language_header);
        options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        de = new Button(options, SWT.RADIO);
        en = new Button(options, SWT.RADIO);

        de.setSelection(true);

        de.setText(Messages.ViterbiComposite_language_german);
        en.setText(Messages.ViterbiComposite_language_english);
    }

    /**
     * Creates a drop down menu to select the parameters for the viterbi algorithm.
     *
     * @param parent
     */
    private void createOptions(final Composite parent) {

        final GridLayout gl = new GridLayout(2, false);

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(gl);

        Label nGramLabel = new Label(canvas, SWT.PUSH);
        nGramLabel.setText(Messages.ViterbiComposite_nGramLabel);

        nGramDrop = new Combo(canvas, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < MAX_NGRAM_SIZE; i++) {
            nGramDrop.add(Integer.toString(i + 1));
        }

        nGramDrop.select(4);

        Label pathLabel = new Label(canvas, SWT.PUSH);
        pathLabel.setText(Messages.ViterbiComposite_pathLabel);

        pathDrop = new Combo(canvas, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        for (int i = 1; i <= 256; i *= 2) {
            pathDrop.add(Integer.toString(i));
        }

        pathDrop.select(7);

    }

    /**
     * Creates the start button to start the viterbi algorithm. The algorithm will be started in an extra thread. This
     * thread can be stopped by pressing the same button again.
     *
     * @param parent
     */
    private void createStartButton(final Composite parent) {

        final GridData gDstartButton = new GridData(CONTINUEBUTTONWIDTH, CONTINUEBUTTONHEIGHT);

        startButton = new Button(parent, SWT.PUSH);
        startButton.setText(Messages.ViterbiComposite_startButton);
        startButton.setLayoutData(gDstartButton);

        startButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {

                Display display = Display.getDefault();

                if (ViterbiComposite.this.isRunning) {
                    ViterbiComposite.this.viterbi.stop();
                    ViterbiComposite.this.isRunning = false;

                    display.syncExec(new Runnable() {

                        public void run() {
                            ViterbiComposite.this.startButton.setText(Messages.ViterbiComposite_startButton);
                        }
                    });
                } else {

                    display.syncExec(new Runnable() {

                        public void run() {
                            ViterbiComposite.this.startButton.setText(Messages.ViterbiComposite_cancelButton);

                        }
                    });

                    StringBuilder path = new StringBuilder();
                    path.append(ngramsUrl.getFile());

                    if (de.getSelection()) {
                        path.append("data/ngrams_de.txt");
                    } else {
                        path.append("data/ngrams_en.txt");
                    }

                    int nGramSize = Integer.parseInt(nGramDrop.getText());
                    int prunningNumber = Integer.parseInt(pathDrop.getText());

                    Map<String, Integer> ngrams;

                    // reading ngrams from a textfile
                    NGramProvider provider = new NGramProvider(path.toString());
                    ngrams = provider.getNgrams();
                    int totalMonoGrams = provider.getTotalMonoGrams();

                    // creating the language model
                    LanguageModel language = new LanguageModel(ngrams, DEFAULT_CHARACTER_SET_END
                            - DEFAULT_CHARACTER_SET_BEGIN + 1, totalMonoGrams);

                    viterbi = new Viterbi(nGramSize, prunningNumber, language, combi, ViterbiComposite.this,
                            cipherString);

                    // starting the viterbi thread
                    isRunning = true;
                    new Thread(viterbi).start();
                }
            }
        });
    }

    /**
     * Creates the third line of the viterbi tab content, where the result of the viterbi calculation is displayed. This
     * contains the possible plaintexts and a button to export them
     */
    private void createResult() {

        final Group g = new Group(this, SWT.NONE);
        g.setText(Messages.ViterbiComposite_result_header);
        final GridLayout gl = new GridLayout(3, false);
        gl.marginWidth = MARGIN_WIDTH;
        gl.horizontalSpacing = HORIZONTAL_SPACING;
        g.setLayout(gl);
        g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createLabel1(g);
        createSolution1(g);
        createExportArea(g);
        createLabel2(g);
        createSolution2(g);

    }

    /**
     * Creates a label to describe the solution text field.
     *
     * @param parent
     */
    private void createLabel1(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(new GridLayout());

        final GridData gDLabel = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);

        Label solution1Label = new Label(canvas, SWT.PUSH);
        solution1Label.setText(Messages.ViterbiComposite_solution1);
        solution1Label.setLayoutData(gDLabel);
    }

    /**
     * Creates a new text field to display the first part of the solution.
     *
     * @param parent
     */
    private void createSolution1(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);

        solution1 = new Text(parent, SWT.NONE | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        solution1.setLayoutData(gd);

    }

    /**
     * Creates a button which allows the user to export the solution to a textfile.
     *
     * @param parent
     */
    private void createExportArea(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2);

        final Canvas export = new Canvas(parent, SWT.NONE);
        export.setLayoutData(gd);
        export.setLayout(new GridLayout());

        final GridData gDloadButton = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);

        Button exportButton = new Button(export, SWT.PUSH);
        exportButton.setText(Messages.ViterbiComposite_exportButton);
        exportButton.setLayoutData(gDloadButton);

        exportButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterNames(new String[] {IConstants.ALL_FILTER_NAME});
                dialog.setFilterExtensions(new String[] {IConstants.ALL_FILTER_EXTENSION});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setOverwrite(true);

                String filename = dialog.open();
                if (filename != null) {
                    IO io = new IO();
                    io.write(solution1.getText(), filename + "_1.txt");
                    io.write(solution2.getText(), filename + "_2.txt");
                }
            }
        });
    }

    /**
     * Creates a label to describe the solution text field.
     *
     * @param parent
     */
    private void createLabel2(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(new GridLayout());

        final GridData gDLabel = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);

        Label solution2Label = new Label(canvas, SWT.PUSH);
        solution2Label.setText(Messages.ViterbiComposite_solution2);
        solution2Label.setLayoutData(gDLabel);
    }

    /**
     * Creates a new text field to display the second part of the solution.
     *
     * @param parent
     */
    private void createSolution2(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);

        solution2 = new Text(parent, SWT.NONE | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        solution2.setLayoutData(gd);

    }

    /**
     *
     * Synchronizes this tab with the xor tab. The xor tab sends its ciphertext content to this method.
     *
     * @param newText the ciphertext to be analyzed
     *
     */
    public void setCipherText(String newText) {
        cipherString = newText;

        if (text.getSelection()) { // if the cipher should be displayed as text
            cipher.setText(replaceUnprintableChars(cipherString, "\ufffd"));
        } else { // else display it as hex-digits
            cipher.setText(stringToHex(cipherString));
        }
    }

    /**
     *
     * Synchronize this tab with the xor tab. The xor tab sends its mode of combination to this method.
     *
     * @param combi the mode of combination
     *
     */
    public void setCombination(Combination combi) {
        this.combi = combi;
    }

    /**
     *
     * replaces unprintable characters in a String
     *
     * @param in input string
     * @param replacement to replace special chars with it
     */
    public static String replaceUnprintableChars(String in, String repacement) {
        StringBuffer buffer = new StringBuffer();

        for (char c : in.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                buffer.append(c);
            } else {
                buffer.append(repacement);
            }
        }

        return buffer.toString();
    }

    /**
     *
     * converts a string with normal text representation into a hexadecimal representation.
     *
     * @param inputString
     * @return a string that contains the hex values of the input characters separated by a space (' ')
     *
     */
    public static String stringToHex(String inputString) {
        StringBuffer buffer = new StringBuffer();
        int intValue;
        for (int x = 0; x < inputString.length(); x++) {
            int cursor = 0;
            intValue = inputString.charAt(x);
            String binaryChar = Integer.toBinaryString(inputString.charAt(x));
            for (int i = 0; i < binaryChar.length(); i++) {
                if (binaryChar.charAt(i) == '1') {
                    cursor += 1;
                }
            }
            if ((cursor % 2) > 0) {
                intValue += 128;
            }
            String hex = Integer.toHexString(intValue);
            if (hex.length() == 1) {
                buffer.append("0" + hex + " ");
            } else {
                buffer.append(hex + " ");
            }
        }
        return buffer.toString();
    }

    /**
     * receives the current best viterbi path and shows it in the text fields. used to give feedback about a running
     * viterbi calculation
     *
     * @param path the new best path
     */
    public void update(Path path) {
        currentPath = path;

        Display display = Display.getDefault();
        // Object object = new Object();
        // object.toString();
        display.asyncExec(new Runnable() {
            public void run() {
                solution1.setText(ViterbiComposite.this.currentPath.getPlain1());
                solution2.setText(ViterbiComposite.this.currentPath.getPlain2());
            }
        });
    }

    /**
     * informs the user that the viterbi thread has finished and updates the startbutton. The viterbi method will call
     * this method if he is finished.
     */
    public void viterbiFinished() {
        Display display = Display.getDefault();
        this.isRunning = false;
        display.asyncExec(new Runnable() {
            public void run() {
                ViterbiComposite.this.startButton.setText(Messages.ViterbiComposite_startButton);

            }
        });
    }
}
