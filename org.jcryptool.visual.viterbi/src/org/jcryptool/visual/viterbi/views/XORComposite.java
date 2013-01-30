// -----BEGIN DISCLAIMER-----
package org.jcryptool.visual.viterbi.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.viterbi.algorithm.BitwiseXOR;
import org.jcryptool.visual.viterbi.algorithm.Combination;
import org.jcryptool.visual.viterbi.algorithm.IO;
import org.jcryptool.visual.viterbi.algorithm.*;
import org.jcryptool.core.util.constants.IConstants;

/**
 *
 * This class creates the content of the xor tab. The basic layout is a grid layout. In the grid layout, the GUI objects
 * are connected with canvas.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class XORComposite extends Composite {
    /* set default values */
    private static final int HORIZONTAL_SPACING = 5;
    private static final int MARGIN_WIDTH = 5;

    private static final int LOADBUTTONHEIGHT = 30;
    private static final int LOADBUTTONWIDTH = 120;

    private Text plain1;
    private Text plain2;
    private Text cipher;
    private ViterbiView viterbiView;
    private Button hex;
    private Button text;
    private String cipherString = "";
    private Button xor;
    private Button mod;
    private StyledText stDescription;

    /* default value for the combination is xor */
    private Combination combi = new BitwiseXOR();
    private Composite g;

    /**
     * @param the parent class
     * @param style
     * @param reference of the viterbiView
     */
    public XORComposite(final Composite parent, final int style, ViterbiView viterbiView) {
        super(parent, style);

        this.viterbiView = viterbiView;
        setLayout(new GridLayout());
        createHead();
        createMainArea();
    }

    /**
     * Sets the default texts in the plaintext fields. This is just a bugfix, because setting the texts earlier would
     * destroy the layout.
     */
    public void displayDefaultTexts() {
        plain2.setText(Messages.XORComposite_Plain2DefaultText);
        plain1.setText(Messages.XORComposite_Plain1DefaultText);
        stDescription.setText(Messages.XORComposite_description);
    }

    /**
     * This method generates the head of the tab. The head has a title and a description.
     */
    private void createHead() {
        final Composite head = new Composite(this, SWT.NONE);
        head.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        head.setLayout(new GridLayout());

        final Label label = new Label(head, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        label.setText(Messages.XORComposite_tab_title);

        stDescription = new StyledText(head, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    /**
     * This method creates the main area. It calls most of the other methods in this class. The basic layout is a grid
     * layout.
     */
    private void createMainArea() {
        g = new Composite(this, SWT.NONE);
       // g.setText(Messages.XORComposite_algo_header);
        final GridLayout gl_g = new GridLayout(2, false); //$NON-NLS-1$
        // fixed size of 3
        // elements per line
        gl_g.marginWidth = MARGIN_WIDTH;
        gl_g.horizontalSpacing = HORIZONTAL_SPACING;
        g.setLayout(gl_g);
        GridData gd_g = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_g.heightHint = 550;
        g.setLayoutData(gd_g);

        createLoadFile1(g);
        createPlain1(g);
        createCombinationArea(g);

        createLoadFile2(g);
        createPlain2(g);

        createLoadCipher(g);
        createCipher(g);
//        createEncodingModeArea(g);
    }

    /**
     *
     * This class makes a button that is used to read input from a file and print it into a textfield.
     *
     * @param the parent item
     */
    private void createLoadFile1(final Composite parent) {
    }

    /**
     * Creates a text field for the plaintext.
     *
     * @param parent
     */
    private void createPlain1(final Composite parent) {
                final Canvas canvas_1 = new Canvas(parent, SWT.NONE);
                canvas_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
                canvas_1.setLayout(new GridLayout());

                        Label plain1Label_1 = new Label(canvas_1, SWT.PUSH);
                        plain1Label_1.setText(Messages.XORComposite_Plain1);

                                Button loadPlain1 = new Button(canvas_1, SWT.PUSH);
                                loadPlain1.setText(Messages.XORComposite_loadFile);
                                GridData gd_loadPlain1 = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
                                gd_loadPlain1.grabExcessHorizontalSpace = true;
                                gd_loadPlain1.horizontalAlignment = SWT.FILL;
                                gd_loadPlain1.verticalAlignment = SWT.FILL;
                                loadPlain1.setLayoutData(gd_loadPlain1);

                                        loadPlain1.addSelectionListener(new SelectionAdapter() {
                                            @Override
                                            public void widgetSelected(final SelectionEvent e) {
                                                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                                                dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
                                                dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION,
                                                        IConstants.ALL_FILTER_EXTENSION});
                                                dialog.setFilterPath(DirectoryService.getUserHomeDir());

                                                String filename = dialog.open();
                                                if (filename != null) {
                                                    String text = new IO().read(filename, "\r\n");
                                                    plain1.setText(text); // printing text into textfield
                                                }
                                            }
                                        });
        plain1 = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_plain1 = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_plain1.horizontalIndent = 1;
        gd_plain1.heightHint = 100;
        plain1.setLayoutData(gd_plain1);
        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
        canvas.setLayout(new GridLayout());

                Label plain1Label = new Label(canvas, SWT.PUSH);
                plain1Label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
                plain1Label.setText(Messages.XORComposite_Plain2);

                        Button loadPlain2 = new Button(canvas, SWT.PUSH);
                        loadPlain2.setText(Messages.XORComposite_loadFile);
                        GridData gd_loadPlain2 = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
                        gd_loadPlain2.grabExcessHorizontalSpace = true;
                        gd_loadPlain2.verticalAlignment = SWT.TOP;
                        gd_loadPlain2.horizontalAlignment = SWT.FILL;
                        loadPlain2.setLayoutData(gd_loadPlain2);

                                loadPlain2.addSelectionListener(new SelectionAdapter() {
                                    @Override
                                    public void widgetSelected(final SelectionEvent e) {
                                        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                                        dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
                                        dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION,
                                                IConstants.ALL_FILTER_EXTENSION});
                                        dialog.setFilterPath(DirectoryService.getUserHomeDir());

                                        String filename = dialog.open();
                                        if (filename != null) {
                                            String text = new IO().read(filename, "\r\n");
                                            plain2.setText(text); // printing text into textfield
                                        }
                                    }
                                });
    }

    /**
     * Creates radio buttons. This is used for determining the combination mode.
     */
    private void createCombinationArea(final Composite parent) {

    }

    /**
     *
     * This class makes a button that is used to read input from a file and print it into a textfield.
     *
     * @param the parent item
     */
    private void createLoadFile2(final Composite parent) {
                        plain2 = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
                        GridData gd_plain2 = new GridData(SWT.FILL, SWT.FILL, true, true);
                        gd_plain2.heightHint = 100;
                        plain2.setLayoutData(gd_plain2);

                        Canvas canvas_1 = new Canvas(g, SWT.NONE);
                        canvas_1.setLayout(new GridLayout(2, false));
                        canvas_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

                                                                                                                                Label plain1Label = new Label(canvas_1, SWT.PUSH);
                                                                                                                                plain1Label.setText(Messages.XORComposite_cipher);
                                                                                                                        new Label(canvas_1, SWT.NONE);

                                                                                                                        Group options1 = new Group(canvas_1, SWT.NONE);
                                                                                                                        GridLayout gl_options1 = new GridLayout();
                                                                                                                        options1.setLayout(gl_options1);
                                                                                                                        options1.setText(Messages.XORComposite_combination_header);
                                                                                                                        options1.setToolTipText(Messages.options1tooltip);

                                                                                                                                xor = new Button(options1, SWT.RADIO);
                                                                                                                                mod = new Button(options1, SWT.RADIO);

                                                                                                                                        xor.setSelection(true);

                                                                                                                                                xor.setText(Messages.XORComposite_Combination_RadioXOR);
                                                                                                                                                mod.setText(Messages.XORComposite_Combination_RadioMOD);
                                                                                                        final Canvas canvas = new Canvas(canvas_1, SWT.NONE);
                                                                                                        canvas.setLayout(new GridLayout());

                                                                                                                        Group options2 = new Group(canvas, SWT.NONE);
                                                                                                                        options2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
                                                                                                                        options2.setLayout(new GridLayout());
                                                                                                                        options2.setText(Messages.XORComposite_encodingmod_header);
                                                                                                                        options2.setToolTipText(Messages.options2tooltip);

                                                                                                                                hex = new Button(options2, SWT.RADIO);
                                                                                                                                text = new Button(options2, SWT.RADIO);

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

                                                                                                                                                                // conversion to hex
                                                                                                                                                                cipher.setText(ViterbiComposite.stringToHex(cipherString));
                                                                                                                                                            }
                                                                                                                                                        });



                                                                                                                                                        text.addSelectionListener(new SelectionListener() {
                                                                                                                                                            public void widgetDefaultSelected(SelectionEvent e) {
                                                                                                                                                                widgetSelected(e);
                                                                                                                                                            }

                                                                                                                                                            public void widgetSelected(SelectionEvent e) {
                                                                                                                                                                text.setSelection(true);
                                                                                                                                                                hex.setSelection(false);

                                                                                                                                                                // unprintable chars will be replaced with "?"
                                                                                                                                                                cipher.setText(ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd"));
                                                                                                                                                            }
                                                                                                                                                        });

                                                                                                        Button calculate = new Button(canvas_1, SWT.PUSH);
                                                                                                        calculate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
                                                                                                        calculate.setText(Messages.XORComposite_calculate);

                                                                                                                        Button exportButton = new Button(canvas_1, SWT.PUSH);
                                                                                                                        exportButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
                                                                                                                        exportButton.setText(Messages.ViterbiComposite_exportButton);

                                                                                                                                exportButton.addSelectionListener(new SelectionAdapter() {
                                                                                                                                    @Override
                                                                                                                                    public void widgetSelected(final SelectionEvent e) {
                                                                                                                                        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                                                                                                                                        dialog.setFilterNames(new String[] {IConstants.ALL_FILTER_NAME});
                                                                                                                                        dialog.setFilterExtensions(new String[] {IConstants.ALL_FILTER_EXTENSION});
                                                                                                                                        dialog.setFilterPath(DirectoryService.getUserHomeDir());

                                                                                                                                        String filename = dialog.open();
                                                                                                                                        if (filename != null) {
                                                                                                                                            IO io = new IO();
                                                                                                                                            io.write(cipher.getText(), filename + ".txt");
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                        calculate.addSelectionListener(new SelectionAdapter() {

                                                                                                            @Override
                                                                                                            public void widgetSelected(final SelectionEvent e) {
                                                                                                                if (xor.getSelection()) {
                                                                                                                    combi = new BitwiseXOR();
                                                                                                                } else {
                                                                                                                    combi = new ModularAddition();
                                                                                                                }

                                                                                                                cipherString = combi.add(plain1.getText(), plain2.getText());

                                                                                                                if (text.getSelection()) {
                                                                                                                    cipher.setText(ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd"));
                                                                                                                    // the ? is used for masking unprintable characters
                                                                                                                } else {
                                                                                                                    cipher.setText(ViterbiComposite.stringToHex(cipherString));
                                                                                                                }
                                                                                                            }
                                                                                                        });
                        cipher = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
                        GridData gd_cipher = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
                        gd_cipher.heightHint = 100;
                        cipher.setLayoutData(gd_cipher);

        Button next = new Button(g, SWT.PUSH);
        next.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        next.setText(Messages.XORComposite_next);
        next.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                viterbiView.changeTab(cipherString, combi);
            }
        });
        new Label(g, SWT.NONE);
    }

    /**
     * Creates a text field for the plaintext.
     *
     * @param parent
     */
    private void createPlain2(final Composite parent) {
    }

    /**
     * This class makes a button that is used to read input from a file and print it into a textfield.
     *
     * The format of the text printed depends on the format selected.
     *
     * @param the parent item
     */
    private void createLoadCipher(final Composite parent) {
    }

    /**
     * Creates a text field for the ciphertext.
     *
     * @param parent
     */
    private void createCipher(final Composite parent) {
    }

    /**
     * This class creates radio buttons. It is used to determine the encoding mode
     *
     * @param parent
     */
}
