// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.analysis.friedman.FriedmanPlugin;
import org.jcryptool.analysis.friedman.IFriedmanAccess;
import org.jcryptool.analysis.friedman.calc.FriedmanCalc;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * @author SLeischnig
 *
 */
public class FriedmanGraphUI extends org.eclipse.swt.widgets.Composite implements IFriedmanAccess {

    @SuppressWarnings("unused")
	private static final String ACTION_CANCELLED = "#cancelled#"; //$NON-NLS-1$

    {
        // Register as a resource user - SWTResourceManager will
        // handle the obtaining and disposing of resources
        SWTResourceManager.registerResourceUser(this);
    }

    private Button button9;

    public FriedmanGraphUI(final org.eclipse.swt.widgets.Composite parent, final int style) {
        super(parent, style);
        initGUI();

    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout();
            thisLayout.makeColumnsEqualWidth = true;
            this.setLayout(thisLayout);
            Composite composite1 = new Composite(this, SWT.NONE);
            GridLayout composite1Layout = new GridLayout();
            composite1Layout.numColumns = 2;
            composite1Layout.marginHeight = 0;
            composite1Layout.marginWidth = 0;
            composite1Layout.horizontalSpacing = 0;
            GridData composite1LData = new GridData();
            composite1LData.verticalAlignment = GridData.FILL;
            composite1LData.horizontalAlignment = GridData.FILL;
            composite1LData.grabExcessHorizontalSpace = true;
            composite1.setLayoutData(composite1LData);
            composite1.setLayout(composite1Layout);
            {
                Button button2 = new Button(composite1, SWT.PUSH | SWT.CENTER);
                GridData button2LData = new GridData();
                button2LData.horizontalAlignment = GridData.FILL;
                button2LData.grabExcessHorizontalSpace = true;
                button2.setLayoutData(button2LData);
                button2.setText(Messages.FriedmanGraphUI_start);
                button2.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        startAnalysis(e);
                    }
                });
            }
            group1 = new Group(this, SWT.NONE);
            GridLayout group1Layout = new GridLayout();
            group1Layout.makeColumnsEqualWidth = true;
            group1.setLayout(group1Layout);
            GridData group1LData = new GridData();
            group1LData.grabExcessHorizontalSpace = true;
            group1LData.horizontalAlignment = GridData.FILL;
            group1LData.verticalAlignment = GridData.FILL;
            group1LData.grabExcessVerticalSpace = true;
            group1.setLayoutData(group1LData);
            group1.setText(Messages.FriedmanGraphUI_graph);
            {
                myGraph = new CustomFriedmanCanvas(group1, SWT.DOUBLE_BUFFERED);

                GridLayout myGraphLayout = new GridLayout();
                myGraph.setLayout(myGraphLayout);
                GridData myGraphLData = new GridData();
                myGraphLData.verticalAlignment = GridData.FILL;
                myGraphLData.grabExcessHorizontalSpace = true;
                myGraphLData.horizontalAlignment = GridData.FILL;
                myGraphLData.grabExcessVerticalSpace = true;
                myGraph.setLayoutData(myGraphLData);
            }
            Composite composite2 = new Composite(this, SWT.NONE);
            GridLayout composite2Layout = new GridLayout();
            composite2Layout.numColumns = 2;
            GridData composite2LData = new GridData();
            composite2LData.grabExcessHorizontalSpace = true;
            composite2LData.horizontalAlignment = GridData.FILL;
            composite2.setLayoutData(composite2LData);
            composite2.setLayout(composite2Layout);
            {
                button9 = new Button(composite2, SWT.PUSH | SWT.CENTER);
                GridData button9LData = new GridData();
                button9.setLayoutData(button9LData);
                button9.setText(Messages.FriedmanGraphUI_showastable);
                button9.setEnabled(false);
                button9.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        showTableDialog(e);
                    }
                });
            }
            this.layout();
        } catch (Exception e) {
            LogUtil.logError(FriedmanPlugin.PLUGIN_ID, e);
        }

    }

    // --------------- End of generic formular description ---------------------
    // -------------------------------------------------------------------------
    // --------------- Begin of code ---------------------

    private CustomFriedmanCanvas myGraph;
    private Group group1;
    private String ciphertext, goodCiphertext = ""; //$NON-NLS-1$
    private String cachedResult = ""; //$NON-NLS-1$

    private int ciphertextHash = 0;

    private String analyzedFile;

    private FriedmanCalc myAnalysis;
    private boolean didSomeCalc;
    private TextViewer myDialog;

    public final void execute(final boolean executeCalc) {
        if (executeCalc) {
            executeMainfunction();
        }
    }


    /**
     * main friedman analysis procedure
     */
    private void analyze() {
        myAnalysis = new FriedmanCalc(goodCiphertext, Math.min(goodCiphertext.length(), 2000));
        myGraph.setAnalysis(myAnalysis);
        myGraph.redraw();
    }

    /**
     * message shows a message box
     */
    private void showMessage(final String message) {
        MessageBox messageBox = new MessageBox(getShell());
        messageBox.setText(""); //$NON-NLS-1$
        messageBox.setMessage(message);
        messageBox.open();
    }

    // Leaves only chars with byte values from 65 to 90 (A-Z), thus replacing ö, ä, ü, ß and casting
    // to upper case
    private String makeFormattedString(final String input) {
        String textOld = input;
        textOld = textOld.toUpperCase();
        //		textOld.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
        //		textOld.replace("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
        //		textOld.replace("\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
        //		textOld.replace("Ä", "AE"); //$NON-NLS-1$ //$NON-NLS-2$
        //		textOld.replace("Ö", "OE"); //$NON-NLS-1$ //$NON-NLS-2$
        //		textOld.replace("Ü", "UE"); //$NON-NLS-1$ //$NON-NLS-2$
        //		textOld.replace("ß", "SS"); //$NON-NLS-1$ //$NON-NLS-2$

        for (int i = 0; i < textOld.length();) {
            if (textOld.charAt(i) < 65 || textOld.charAt(i) > 90) {
                textOld = textOld.substring(0, i).concat(textOld.substring(i + 1));
            } else {
                i++;
            }
        }
        return textOld;
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
			LogUtil.logError(FriedmanPlugin.PLUGIN_ID, e1);
		}

    	StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
            	myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(FriedmanPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    /**
     * reads the text from the opened editor
     */
    private void getText() {
        InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
        if (stream == null) {
            showMessage(Messages.FriedmanGraphUI_openandselect);
            return;
        } else {
            ciphertext = InputStreamToString(stream);
        }

        analyzedFile = EditorsManager.getInstance().getActiveEditorTitle();

        if (ciphertext.hashCode() != ciphertextHash) {
            ciphertextHash = ciphertext.hashCode();
            goodCiphertext = makeFormattedString(ciphertext);
        }
    }

    /**
     * The main function of this plug-in
     */
    private void executeMainfunction() {
        getText();

        if (analyzedFile != null) {
            if (analyzedFile.length() != 0) {
                didSomeCalc = true;
            }
            if (didSomeCalc) {
                button9.setEnabled(true);
            }

            analyze();
        }
    }

    private void startAnalysis(final SelectionEvent evt) {
        executeMainfunction();
    }

    private void showTableDialog(final SelectionEvent evt) {
        if (myDialog == null || !myDialog.isVisible()) {
            myDialog = new TextViewer(getShell(), Messages.FriedmanGraphUI_coincidence, myAnalysis.toString());

            myDialog.open();
        }
    }

    public final String getCachedResult() {
        return cachedResult;
    }

}