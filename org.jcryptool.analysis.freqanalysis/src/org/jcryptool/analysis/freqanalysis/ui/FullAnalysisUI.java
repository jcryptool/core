// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.freqanalysis.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisCalc;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.ui.SingleVanishTooltipLauncher;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * @author SLeischnig
 *
 */
public class FullAnalysisUI extends org.eclipse.swt.widgets.Composite {

    {
        // Register as a resource user - SWTResourceManager will
        // handle the obtaining and disposing of resources
        SWTResourceManager.registerResourceUser(this);
    }

    private Button button1;
    private Composite composite1;
    private Group group1;
    private CustomFreqCanvas myGraph;
    private Group group4;
    private Composite composite2;
    private Button button3;
    private Button button4;
    private Group group2;
    private Spinner spinner1;
    private Label label1;
    private Spinner spinner2;
    private TabItem tabItem2;
    private Button btnReferenceTools;
    private Composite composite4;
    private Composite composite3;
    private TabItem tabItem1;
    private TabFolder tabFolder1;
    private Combo combo1;
    private Combo combo2;
    private Label label3;
    private Label label2;

    private String text;
    private FreqAnalysisCalc myAnalysis;
    private int myOffset, myLength;
    private FreqAnalysisCalc overlayAnalysis;
    private String myOverlayAlphabet = ""; //$NON-NLS-1$
    private String reftext;
    private Vector<Reftext> reftexts;
    private FreqAnalysisCalc myLimitedAnalysis;
    private SingleVanishTooltipLauncher tipLauncher;
    private boolean appropriateAlphabetToBeDetected = false;

    /**
     * Contains reference texts for overlays
     *
     * @author SLeischnig
     *
     */
    private class Reftext {
        private String path, text, name;

        public Reftext(final String name, final String path) {
            this.name = name;
            this.path = path;
            text = InputStreamToString(openMyTestStream(this.path));
        }

        // public String getName() {return name;}
        public String getText() {
            return text;
        }
        // public String getPath() {return path;}
    }

    public FullAnalysisUI(final org.eclipse.swt.widgets.Composite parent, final int style) {
        super(parent, style);
        reftexts = new Vector<Reftext>(0, 1);

        initGUI();
        changedVigOptions();
        combo1.select(0);
        combo1WidgetSelected(null);
        hideObject(group2, button3.getSelection());
    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout();
            thisLayout.makeColumnsEqualWidth = true;
            this.setLayout(thisLayout);
            this.setSize(604, 337);
            button1 = new Button(this, SWT.PUSH | SWT.CENTER);
            GridData button1LData = new GridData();
            button1LData.horizontalAlignment = GridData.FILL;
            button1LData.grabExcessHorizontalSpace = true;
            button1.setLayoutData(button1LData);
            button1.setText(Messages.FullAnalysisUI_startanalysis);
            button1.addMouseListener(new MouseAdapter() {

                public void mouseDown(final MouseEvent evt) {
                    if (checkEditor()) {
                        text = getEditorText();
                        if (btnReferenceTools.getSelection()) {
                            selectAppropriateAlphabet();
                        } else {
                            appropriateAlphabetToBeDetected = true;
                        }
                        recalcGraph();
                    }
                }
            });
            composite1 = new Composite(this, SWT.NONE);
            GridLayout composite1Layout = new GridLayout();
            composite1Layout.numColumns = 2;
            composite1Layout.marginWidth = 0;
            composite1Layout.marginHeight = 0;
            GridData composite1LData = new GridData();
            composite1LData.grabExcessHorizontalSpace = true;
            composite1LData.horizontalAlignment = GridData.FILL;
            composite1LData.verticalAlignment = GridData.FILL;
            composite1LData.grabExcessVerticalSpace = true;
            composite1.setLayoutData(composite1LData);
            composite1.setLayout(composite1Layout);
            {
                group1 = new Group(composite1, SWT.NONE);
                GridLayout group1Layout = new GridLayout();
                group1.setLayout(group1Layout);
                GridData group1LData = new GridData();
                group1LData.horizontalAlignment = GridData.FILL;
                group1LData.grabExcessHorizontalSpace = true;
                group1LData.verticalAlignment = GridData.FILL;
                group1LData.grabExcessVerticalSpace = true;
                group1.setLayoutData(group1LData);
                group1.setText(Messages.FullAnalysisUI_graphlabel);
                {
                    myGraph = new CustomFreqCanvas(group1, SWT.NONE);

                    GridLayout myGraphLayout = new GridLayout();
                    myGraph.setLayout(myGraphLayout);
                    GridData myGraphLData = new GridData();
                    myGraphLData.verticalAlignment = GridData.FILL;
                    myGraphLData.grabExcessVerticalSpace = true;
                    myGraphLData.grabExcessHorizontalSpace = true;
                    myGraphLData.horizontalAlignment = GridData.FILL;
                    myGraph.setLayoutData(myGraphLData);
                }
            }
            {
                group4 = new Group(composite1, SWT.NONE);
                GridLayout group4Layout = new GridLayout();
                group4.setLayout(group4Layout);
                GridData group4LData = new GridData();
                group4LData.horizontalAlignment = GridData.FILL;
                group4LData.grabExcessVerticalSpace = true;
                group4LData.verticalAlignment = GridData.FILL;
                group4.setLayoutData(group4LData);
                group4.setText(Messages.FullAnalysisUI_propertieslabel);
                {
                    tabFolder1 = new TabFolder(group4, SWT.NONE);
                    {
                        tabItem1 = new TabItem(tabFolder1, SWT.NONE);
                        tabItem1.setText(Messages.FullAnalysisUI_firsttablabel);
                        {
                            composite3 = new Composite(tabFolder1, SWT.NONE);
                            GridLayout composite3Layout = new GridLayout();
                            composite3Layout.makeColumnsEqualWidth = true;
                            composite3.setLayout(composite3Layout);
                            GridData composite3LData = new GridData();
                            composite3LData.grabExcessHorizontalSpace = true;
                            composite3LData.horizontalAlignment = GridData.FILL;
                            composite3LData.verticalAlignment = GridData.FILL;
                            composite3LData.grabExcessVerticalSpace = true;
                            tabItem1.setControl(composite3);
                            {
                                composite2 = new Composite(composite3, SWT.NONE);
                                GridLayout composite2Layout = new GridLayout();
                                composite2Layout.makeColumnsEqualWidth = true;
                                composite2Layout.marginHeight = 0;
                                GridData composite2LData = new GridData();
                                composite2LData.grabExcessHorizontalSpace = true;
                                composite2LData.horizontalAlignment = GridData.FILL;
                                composite2.setLayoutData(composite2LData);
                                composite2.setLayout(composite2Layout);
                                {
                                    button3 = new Button(composite2, SWT.RADIO | SWT.LEFT);
                                    button3.setText(Messages.FullAnalysisUI_monoalphabetic);
                                    button3.setSelection(true);
                                    button3.addSelectionListener(new SelectionAdapter() {
                                        public void widgetSelected(SelectionEvent evt) {
                                            polyOnOffSelected(evt);
                                        }
                                    });
                                }
                                {
                                    button4 = new Button(composite2, SWT.RADIO | SWT.LEFT);
                                    button4.setText(Messages.FullAnalysisUI_polyalphabetic);
                                    button4.addSelectionListener(new SelectionAdapter() {
                                        public void widgetSelected(SelectionEvent evt) {
                                            polyOnOffSelected(evt);
                                        }
                                    });
                                }
                            }
                            {
                                group2 = new Group(composite3, SWT.NONE);
                                GridLayout group2Layout = new GridLayout();
                                group2Layout.numColumns = 2;
                                group2.setLayout(group2Layout);
                                GridData group2LData = new GridData();
                                group2LData.horizontalAlignment = GridData.FILL;
                                group2LData.verticalAlignment = GridData.BEGINNING;
                                group2LData.grabExcessHorizontalSpace = true;
                                group2.setLayoutData(group2LData);
                                group2.setText(Messages.FullAnalysisUI_vigeneresettings);
                                {
                                    GridData spinner1LData = new GridData();
                                    spinner1 = new Spinner(group2, SWT.NONE);
                                    spinner1.setLayoutData(spinner1LData);
                                    spinner1.addMouseListener(new MouseAdapter() {
                                        public void mouseDown(MouseEvent evt) {
                                            recalcGraph();
                                        }
                                    });
                                    spinner1.addSelectionListener(new SelectionAdapter() {
                                        public void widgetSelected(SelectionEvent evt) {
                                            changedVigOptions();
                                        }
                                    });
                                    spinner1.setSelection(1);
                                }
                                {
                                    label1 = new Label(group2, SWT.NONE);
                                    label1.setLayoutData(new GridData());
                                    label1.setText(Messages.FullAnalysisUI_keylength);
                                }
                                {
                                    GridData spinner2LData = new GridData();
                                    spinner2 = new Spinner(group2, SWT.NONE);
                                    spinner2.setLayoutData(spinner2LData);
                                    spinner2.addMouseListener(new MouseAdapter() {
                                        public void mouseDown(MouseEvent evt) {
                                            recalcGraph();
                                        }
                                    });
                                    spinner2.addSelectionListener(new SelectionAdapter() {
                                        public void widgetSelected(SelectionEvent evt) {
                                            changedVigOptions();
                                        }
                                    });
                                }
                                {
                                    label2 = new Label(group2, SWT.NONE);
                                    label2.setLayoutData(new GridData());
                                    label2.setText(Messages.FullAnalysisUI_keyoffset);
                                }
                            }
                        }
                    }
                    {
                        tabItem2 = new TabItem(tabFolder1, SWT.NONE);
                        tabItem2.setText(Messages.FullAnalysisUI_secondtablabel);
                        {
                            composite4 = new Composite(tabFolder1, SWT.NONE);
                            GridLayout composite4Layout = new GridLayout();
                            composite4Layout.makeColumnsEqualWidth = true;
                            composite4.setLayout(composite4Layout);
                            GridData composite4LData = new GridData();
                            composite4LData.grabExcessHorizontalSpace = true;
                            composite4LData.horizontalAlignment = GridData.FILL;
                            composite4LData.verticalAlignment = GridData.FILL;
                            composite4LData.grabExcessVerticalSpace = true;
                            tabItem2.setControl(composite4);
                            {
                                btnReferenceTools = new Button(composite4, SWT.CHECK | SWT.LEFT);
                                GridData button2LData = new GridData();
                                button2LData.grabExcessHorizontalSpace = true;
                                button2LData.horizontalAlignment = GridData.FILL;
                                btnReferenceTools.setLayoutData(button2LData);
                                btnReferenceTools.setText(Messages.FullAnalysisUI_enabledecrOverlay);
                                btnReferenceTools.addMouseListener(new MouseAdapter() {
                                    public void mouseDown(MouseEvent evt) {
                                        if (!btnReferenceTools.getSelection()) {
                                            myGraph.setAnalysis(myLimitedAnalysis);
                                            enableReferenceTools(true);
                                        } else {
                                            myGraph.setAnalysis(myAnalysis);
                                            enableReferenceTools(false);
                                        }
                                        myGraph.setOverlayActivated(!btnReferenceTools.getSelection());
                                        myGraph.redraw();
                                    }
                                });
                            }
                            {
                                combo1 = new Combo(composite4, SWT.READ_ONLY);
                                GridData combo1LData = new GridData();
                                combo1LData.grabExcessHorizontalSpace = true;
                                GridData combo1LData1 = new GridData();
                                combo1LData1.horizontalAlignment = GridData.FILL;
                                combo1LData1.heightHint = 21;
                                combo1.setLayoutData(combo1LData1);

                                // add reftexts
                                reftexts.add(new Reftext(Messages.FullAnalysisUI_germanreftextname1,
                                        Messages.FullAnalysisUI_0)); //$NON-NLS-2$
                                reftexts.add(new Reftext(Messages.FullAnalysisUI_germanreftextname2,
                                        Messages.FullAnalysisUI_1)); //$NON-NLS-2$
                                reftexts.add(new Reftext(Messages.FullAnalysisUI_englishreftextname1,
                                        Messages.FullAnalysisUI_2)); //$NON-NLS-2$

                                for (int i = 0; i < reftexts.size(); i++)
                                    combo1.add(reftexts.get(i).name);
                                combo1.addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        combo1WidgetSelected(evt);
                                    }
                                });
                                combo1.setText(combo1.getItem(0));
                            }
                            {
                                label3 = new Label(composite4, SWT.NONE);
                                GridData label3LData = new GridData();
                                label3LData.horizontalAlignment = GridData.FILL;
                                label3LData.grabExcessHorizontalSpace = true;
                                label3.setLayoutData(label3LData);
                                label3.setText(Messages.FullAnalysisUI_alphabetused);
                            }
                            {
                                combo2 = new Combo(composite4, SWT.READ_ONLY);
                                GridData combo2LData = new GridData();
                                combo2LData.heightHint = 21;
                                combo2LData.horizontalAlignment = GridData.FILL;
                                combo2.setLayoutData(combo2LData);
                                combo2.addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        combo2WidgetSelected(evt);
                                    }
                                });

                                AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
                                for (int i = 0; i < alphas.length; i++) {
                                    combo2.add(alphas[i].getName());
                                    if (alphas[i].isDefaultAlphabet()) {
                                        // combo2.setText(alphas[i].getName());
                                        combo2.select(i);
                                        myOverlayAlphabet = String.valueOf(alphas[i].getCharacterSet());
                                    }
                                }
                            }
                        }
                    }
                    GridData tabFolder1LData = new GridData();
                    tabFolder1LData.horizontalAlignment = GridData.FILL;
                    tabFolder1LData.verticalAlignment = GridData.FILL;
                    tabFolder1LData.grabExcessVerticalSpace = true;
                    tabFolder1LData.grabExcessHorizontalSpace = true;
                    tabFolder1.setLayoutData(tabFolder1LData);
                    tabFolder1.setSelection(0);
                }
            }
            this.layout();
        } catch (Exception e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        }

        tipLauncher = new SingleVanishTooltipLauncher(this.getShell());
        enableReferenceTools(false);
    }

    protected void enableReferenceTools(boolean b) {
        combo1.setEnabled(b);
        combo2.setEnabled(b);
        label3.setEnabled(b);
        if (appropriateAlphabetToBeDetected) {
            selectAppropriateAlphabet();
            appropriateAlphabetToBeDetected = false;
        }
        // btnReferenceTools.setSelection(b);
    }

    /**
     * opens a resource file stream
     *
     * @param filename the file path
     * @return the inputStream containing the file's content
     */
    private InputStream openMyTestStream(final String filename) {
        try {
            URL installURL = FreqAnalysisPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            URL url = new URL(installURL, filename);
            return (url.openStream());
        } catch (MalformedURLException e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        }
        return null;
    }

    /**
     * excludes a control from the Layout calculation
     *
     * @param that
     * @param hideit
     */
    private void hideObject(final Control that, final boolean hideit) {
        GridData GData = (GridData) that.getLayoutData();
        GData.exclude = true && hideit;
        that.setVisible(true && !hideit);
        Control[] myArray = {that};
        layout(myArray);
    }

    /**
     * takes the input control's values and sets the final analysis parameters
     */
    private void setFinalVigParameters() {
        myLength = 1;
        if (!button3.getSelection()) {
            myLength = spinner1.getSelection();
        }
        myOffset = 0;
        if (!button3.getSelection()) {
            myOffset = spinner2.getSelection();
        }
    }

    private void analyze() {
        myAnalysis = new FreqAnalysisCalc(text, myLength, myOffset, null);
        myLimitedAnalysis = new FreqAnalysisCalc(text, myLength, myOffset, null, myOverlayAlphabet);
        if (btnReferenceTools.getSelection())
            myGraph.setAnalysis(myLimitedAnalysis);
        else
            myGraph.setAnalysis(myAnalysis);
    }

    /**
     * rebuilds the frequency analysis graph
     */
    private void recalcGraph() {
        if (checkEditor()) {
            setFinalVigParameters();
            analyze();
            myGraph.redraw();
        }
    }

    /**
     * checks, whether an editor is opened or not.
     */
    private boolean checkEditor() {
        InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
        if (stream == null) {
            return false;
        }
        return true;
    }

    /**
     * get the text from an opened editor
     */
    private String getEditorText() {
        String text = ""; //$NON-NLS-1$
        InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
        text = InputStreamToString(stream);
        return text;
    }

    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private String InputStreamToString(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    /**
     * executes the necessary calculations when Vigenère parameters were changed
     */
    private void changedVigOptions() {
        if (spinner1.getSelection() < 1) {
            spinner1.setSelection(1);
        }
        if (spinner2.getSelection() < 0) {
            spinner2.setSelection(0);
        }
        spinner1.setMinimum(1);
        spinner1.setMaximum(999999);
        spinner2.setMinimum(0);
        spinner2.setMaximum(spinner1.getSelection() - 1);
        if (spinner2.getSelection() >= spinner1.getSelection()) {
            spinner2.setSelection(spinner1.getSelection() - 1);
        }
        spinner2.setMaximum(spinner1.getSelection());
        myGraph.getFrequencyGraph().resetDrag();
    }

    /**
     * removes a specific character from a string
     *
     * @param s the string
     * @param c the character
     */
    private String removeChar(final String s, final char c) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                result.append(s.charAt(i));
            }
        }
        return result.toString();
    }

    /**
     * Returns a String which contains each character and control sequence the text contains only and only once.
     */
    private String countDifferentChars(final String text) {

        int i = 0;
        String myText = text;
        while (i < myText.length()) {
            myText = myText.substring(0, i + 1).concat(removeChar(myText.substring(i + 1), myText.charAt(i)));
            i++;
        }
        return myText;
    }

    /**
     * a rating function; calculates a value for how much an alphabet is fitting a text.
     *
     * @param alphabet the alphabets (each character only to be contained once!)
     * @param text the text
     * @return the rating. Higher values are better. Rating zero is best.
     */
    private double rateAlphabetTextDifference(final String alphabet, final String text) {
        String condensedText = countDifferentChars(text);
        double penaltyTextNotInAlphabet = (double) (condensedText.length()) / 4; // Its really bad
                                                                                 // if one character
                                                                                 // from the text
                                                                                 // should not
                                                                                 // appear in the
                                                                                 // alphabet..
        double penaltyAlphabetNotInText = (double) (alphabet.length()) / 25;

        int rating = 0;
        for (int i = 0; i < condensedText.length(); i++) {
            if (!alphabet.contains(String.valueOf(condensedText.charAt(i)))) {
                rating -= penaltyTextNotInAlphabet;
            }
        }

        for (int i = 0; i < alphabet.length(); i++) {
            if (!condensedText.contains(String.valueOf(alphabet.charAt(i)))) {
                rating -= penaltyAlphabetNotInText;
            }
        }

        return rating;
    }

    /**
     * Selects the appropriate alphabet for the analysed text and sets the combo box selection.
     *
     */
    private void selectAppropriateAlphabet() {
        AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
        String prevAlpha = myOverlayAlphabet;

        double bestrating = -99999;
        int bestindex = 0;
        double actualrating = 0;
        for (int i = 0; i < alphas.length; i++) {
            actualrating = rateAlphabetTextDifference(String.valueOf(alphas[i].getCharacterSet()), text);
            if (actualrating > bestrating) {
                bestrating = actualrating;
                bestindex = i;
            }
        }

        String bestAlphaString = String.valueOf(alphas[bestindex].getCharacterSet());
        if (bestAlphaString != null && !bestAlphaString.equals(prevAlpha)) {
            if (combo2.isVisible() && combo2.isEnabled()) {
                tipLauncher.showNewTooltip(combo2.toDisplay(new Point(
                        (int) Math.round((double) (combo2.getSize().x) * 0.612), combo2.getSize().y)), 9000,
                        "", Messages.FullAnalysisUI_5); //$NON-NLS-1$
            }
        }

        combo2.select(bestindex);
        combo2WidgetSelected(null);
    }

    /**
     * analyses the selected overlay text.
     *
     * @param alphabet limits the overlay analysis to a specific alphabet (each character only to be contained once!)
     */
    private void analyzeOverlay(final String alphabet) {
        String overlayText = reftext;
        overlayAnalysis = new FreqAnalysisCalc(overlayText, 1, 0, null, alphabet);
        if (text != null && !text.isEmpty()) {
            myLimitedAnalysis = new FreqAnalysisCalc(text, myLength, myOffset, null, myOverlayAlphabet);
            if (btnReferenceTools.getSelection())
                myGraph.setAnalysis(myLimitedAnalysis);
            // else myGraph.setAnalysis(myAnalysis);
        }
        myGraph.setOverlayAnalysis(overlayAnalysis);
        myGraph.redraw();
    }

    private void polyOnOffSelected(final SelectionEvent evt) {
        hideObject(group2, button3.getSelection());

    }

    private void combo1WidgetSelected(final SelectionEvent evt) {
        // DONT REMOVE THIS, PLEASE!
        // if (combo1.getSelectionIndex() == combo1.getItemCount()-1)
        // {
        // //no action til now..
        // }
        // //Predefined one
        // else
        // {
        reftext = reftexts.get(combo1.getSelectionIndex()).getText();
        analyzeOverlay(myOverlayAlphabet);
        // }
    }

    private void combo2WidgetSelected(final SelectionEvent evt) {
        AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
        myOverlayAlphabet = String.valueOf(alphas[combo2.getSelectionIndex()].getCharacterSet());
        analyzeOverlay(myOverlayAlphabet);

    }

    public final void execute(final int keyLength, final int keyPos, final int overlayIndex, final boolean resetShift,
            final boolean executeCalc, final boolean whichTab, final boolean activateOverlay) {
        if (keyLength > 0) {
            if (keyLength == 1) {
                button3.setSelection(true);
                button4.setSelection(false);
            } else {
                button3.setSelection(false);
                button4.setSelection(true);
            }
            polyOnOffSelected(null);

            spinner1.setSelection(keyLength);
            changedVigOptions();
            if (keyPos < 0) {
                text = getEditorText();
                recalcGraph();
            }
        }
        if (keyPos > -1) {
            spinner2.setSelection(keyPos);
            changedVigOptions();
            text = getEditorText();
            recalcGraph();
        }

        if (overlayIndex > -1 && overlayIndex < combo1.getVisibleItemCount() - 1) {
            combo1.select(overlayIndex);
        }

        if (resetShift) {
            myGraph.getFrequencyGraph().resetDrag();
        }
        if (executeCalc) {
            if (checkEditor()) {
                text = getEditorText();
                selectAppropriateAlphabet();
                recalcGraph();
            }

        }

        if (whichTab) {
            tabFolder1.setSelection(0);
        } else {
            tabFolder1.setSelection(1);
        }

        btnReferenceTools.setSelection(activateOverlay);
        myGraph.setOverlayActivated(btnReferenceTools.getSelection());
        myGraph.redraw();

    }

}
