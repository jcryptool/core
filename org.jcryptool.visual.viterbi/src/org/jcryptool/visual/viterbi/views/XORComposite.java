// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.viterbi.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.viterbi.algorithm.BitwiseXOR;
import org.jcryptool.visual.viterbi.algorithm.Combination;
import org.jcryptool.visual.viterbi.algorithm.IO;
import org.jcryptool.visual.viterbi.algorithm.ModularAddition;

/**
 *
 * This class creates the content of the xor tab. The basic layout is a grid layout. In the grid
 * layout, the GUI objects are connected with canvas.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class XORComposite extends Composite {

    /* set default values */
    private static final int HORIZONTAL_SPACING = 50;
    private static final int MARGIN_WIDTH = 100;

    private static final int LOADBUTTONHEIGHT = 30;
    private static final int LOADBUTTONWIDTH = 120;

    private static final int CONTINUEBUTTONHEIGHT = 50;
    private static final int CONTINUEBUTTONWIDTH = 150;

    private Text plain1;
    private Text plain2;
    private Text cipher;
    private ViterbiView viterbiView;
    private Button hex;
    private Button text;
    private String cipherString = "";
    private Button xor;
    private Button mod;
    StyledText stDescription;

    /* default value for the combination is xor */
    private Combination combi = new BitwiseXOR();

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
     * Sets the default texts in the plaintext fields. This is just a bugfix, because setting the
     * texts earlier would destroy the layout.
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
     * This method creates the main area. It calls most of the other methods in this class. The
     * basic layout is a grid layout.
     */
    private void createMainArea() {

        final Group g = new Group(this, SWT.NONE);
        g.setText(Messages.XORComposite_algo_header);
        final GridLayout gl = new GridLayout(3, false); //$NON-NLS-1$
        // fixed size of 3
        // elements per line
        gl.marginWidth = MARGIN_WIDTH;
        gl.horizontalSpacing = HORIZONTAL_SPACING;
        g.setLayout(gl);
        g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createLoadFile1(g);
        createPlain1(g);
        createCombinationArea(g);

        createLoadFile2(g);
        createPlain2(g);

        createLoadCipher(g);
        createCipher(g);
        createEncodingModeArea(g);

    }

    /**
     *
     * This class makes a button that is used to read input from a file and print it into a
     * textfield.
     *
     * @param the parent item
     */

    private void createLoadFile1(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(new GridLayout());

        Label plain1Label = new Label(canvas, SWT.PUSH);
        plain1Label.setText(Messages.XORComposite_Plain1);

        final GridData fDloadButton = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
        Button loadPlain1 = new Button(canvas, SWT.PUSH);
        loadPlain1.setText(Messages.XORComposite_loadFile);
        loadPlain1.setLayoutData(fDloadButton);

        loadPlain1.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
                dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
                dialog.setFilterPath(DirectoryService.getUserHomeDir());

                String filename = dialog.open();
                if (filename != null) {
                    String text = new IO().read(filename, "\r\n");
                    plain1.setText(text); // printing text into textfield
                }
            }
        });
    }

    /**
     * Creates a text field for the plaintext.
     *
     * @param parent
     */

    private void createPlain1(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);

        plain1 = new Text(parent, SWT.NONE | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        plain1.setLayoutData(gd);

    }

    /**
     * Creates radio buttons. This is used for determining the combination mode.
     */

    private void createCombinationArea(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 2);

        final Canvas combination = new Canvas(parent, SWT.NONE);
        combination.setLayoutData(gd);
        combination.setLayout(new GridLayout());

        Group options = new Group(combination, SWT.NONE);
        options.setLayout(new GridLayout());
        options.setText(Messages.XORComposite_combination_header);
        options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        xor = new Button(options, SWT.RADIO);
        mod = new Button(options, SWT.RADIO);

        xor.setSelection(true);

        xor.setText(Messages.XORComposite_Combination_RadioXOR);
        mod.setText(Messages.XORComposite_Combination_RadioMOD);

        final GridData gDcontinueButton = new GridData(CONTINUEBUTTONWIDTH, CONTINUEBUTTONHEIGHT);

        Button calculate = new Button(combination, SWT.PUSH);
        calculate.setText(Messages.XORComposite_calculate);
        calculate.setLayoutData(gDcontinueButton);
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
                    cipher.setText(ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd")); // the
                                                                                                      // �
                                                                                                      // is
                                                                                                      // used
                                                                                                      // for
                    // masking
                    // unprintable
                    // characters
                } else {
                    cipher.setText(ViterbiComposite.stringToHex(cipherString));
                }
            }
        });
    }

    /**
     *
     * This class makes a button that is used to read input from a file and print it into a
     * textfield.
     *
     * @param the parent item
     */

    private void createLoadFile2(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(new GridLayout());

        Label plain1Label = new Label(canvas, SWT.PUSH);
        plain1Label.setText(Messages.XORComposite_Plain2);

        final GridData fDloadButton = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
        Button loadPlain2 = new Button(canvas, SWT.PUSH);
        loadPlain2.setText(Messages.XORComposite_loadFile);
        loadPlain2.setLayoutData(fDloadButton);

        loadPlain2.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
                dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
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
     * Creates a text field for the plaintext.
     *
     * @param parent
     */

    private void createPlain2(final Composite parent) {

        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);

        plain2 = new Text(parent, SWT.NONE | SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        plain2.setLayoutData(gd);

    }

    /**
     * This class makes a button that is used to read input from a file and print it into a
     * textfield.
     *
     * The format of the text printed depends on the format selected.
     *
     * @param the parent item
     */

    private void createLoadCipher(final Composite parent) {

        final Canvas canvas = new Canvas(parent, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        canvas.setLayout(new GridLayout());

        Label plain1Label = new Label(canvas, SWT.PUSH);
        plain1Label.setText(Messages.XORComposite_cipher);

        final GridData gDloadButton = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);

        Button exportButton = new Button(canvas, SWT.PUSH);
        exportButton.setText(Messages.ViterbiComposite_exportButton);
        exportButton.setLayoutData(gDloadButton);

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
    }

    /**
     * Creates a text field for the ciphertext.
     *
     * @param parent
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

        final Canvas encodingmod = new Canvas(parent, SWT.NONE);
        encodingmod.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false));
        GridLayout gl = new GridLayout();
        gl.verticalSpacing = 20;
        gl.marginHeight = 0;
        encodingmod.setLayout(gl);

        Group options = new Group(encodingmod, SWT.NONE);
        options.setLayout(new GridLayout());
        options.setText(Messages.XORComposite_encodingmod_header);
        options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

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
                cipher.setText(ViterbiComposite.stringToHex(cipherString)); // converstion
                                                                            // into
                                                                            // hex
            }
        });

        text.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                text.setSelection(true);
                hex.setSelection(false);

                cipher.setText(ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd")); // unprintable
                                                                                                  // chars
                                                                                                  // will
                // be
                // replaced with "�"

            }
        });

        final GridData gDcontinueButton = new GridData(CONTINUEBUTTONWIDTH, CONTINUEBUTTONHEIGHT);

        Button next = new Button(encodingmod, SWT.PUSH);
        next.setText(Messages.XORComposite_next);
        next.setLayoutData(gDcontinueButton);

        next.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                viterbiView.changeTab(cipherString, combi);
            }
        });
    }
}
