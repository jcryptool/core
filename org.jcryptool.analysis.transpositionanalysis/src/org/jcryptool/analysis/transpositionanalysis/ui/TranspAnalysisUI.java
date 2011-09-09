//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.transpositionanalysis.TranspositionAnalysisPlugin;
import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.TranspositionAnalysisDataobject;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizard;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.autoanalysiswizard.AnalysisWizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.ShadowAlgorithmAction;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionTable;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class TranspAnalysisUI extends org.eclipse.swt.widgets.Composite implements Observer {

    private TranspositionTableComposite transpTable;
    private Button button1;

    private String text = ""; //$NON-NLS-1$
    private Label label1;
    private Label loadedFileLabel;
    private Composite composite7;
    private Label labelReadDir;
    private Composite compReadDir;
    private Link linkExport;
    private Composite composite8;
    private Button btnDecipher;
    private Label textpreviewDescription;
    private Group previewGroup;
    private Text previewText;
    private Composite composite6;
    private Composite composite5;
    private Label labelKeypreview;
    private Composite composite4;
    private Label label2;
    private Label text1;
    private Composite composite3;
    private Spinner spinner1;
    private Composite composite2;
    private Composite composite1;
    private boolean crop = true;
    private int croplength = 80;
    private int blocklength = 0;

    private boolean doAutoCrop = true;

    private AnalysisWizard analysisWizard;
    private TranspTextWizard textWizard;
    private WizardDialog dialog;
    private String fileName;
    private int manualInputState;
    private TranspositionKey keyUsedToEncrypt;
    private TranspositionAnalysisDataobject transpAnalysisDataobject;
	private ReadDirectionChooser readoutDirChooser;
	private boolean readInMode = false;

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        setText(text, true);
    }

    private void setText(String text, boolean refresh) {
        this.text = text;
        if (refresh)
            transpTable.setText(text);
    }

    /**
     * @param crop the crop to set
     */
    public void setCrop(boolean crop) {
        setCrop(crop, true);
    }

    public void setCrop(boolean crop, boolean refresh) {
        this.crop = crop;
        if (refresh)
            transpTable.setText(text, blocklength, !crop, croplength);
    }

    /**
     * @param croplength the croplength to set
     */
    public void setCroplength(int croplength) {
        setCroplength(croplength, true);
    }

    public void setCroplength(int croplength, boolean refresh) {
        this.croplength = croplength;
        if (refresh)
            transpTable.setText(text, blocklength, !crop, croplength);
    }

    /**
     * @param blocklength the blocklength to set
     */
    public void setBlocklength(int blocklength) {
        setBlocklength(blocklength, true);
    }

    public void setBlocklength(int blocklength, boolean refresh) {
        this.blocklength = blocklength;
        spinner1.setSelection(blocklength);

        if (refresh) {
            transpTable.setColumnCount(blocklength);
            columnsReordered(transpTable.getColumnOrder());
        }
    }

    private void setReadInMode(boolean readInDirection, boolean refresh) {
		this.readInMode = readInDirection;
		if(refresh) {
			transpTable.setReadInOrder(readInMode);
		}
	}

	/**
     * Auto-generated method to display this org.eclipse.swt.widgets.Composite inside a new Shell.
     */

    public TranspAnalysisUI(org.eclipse.swt.widgets.Composite parent, int style) {
        super(parent, style);
        initGUI();
    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout();
            thisLayout.numColumns = 2;
            this.setLayout(thisLayout);
            {
                composite1 = new Composite(this, SWT.NONE);
                GridLayout composite1Layout = new GridLayout();
                composite1Layout.makeColumnsEqualWidth = true;
                GridData composite1LData = new GridData();
                composite1LData.grabExcessHorizontalSpace = true;
                composite1LData.horizontalAlignment = GridData.FILL;
                composite1LData.verticalAlignment = GridData.BEGINNING;
                composite1.setLayoutData(composite1LData);
                composite1.setLayout(composite1Layout);
                {
                    button1 = new Button(composite1, SWT.PUSH | SWT.CENTER);
                    GridData button1LData = new GridData();
                    button1LData.grabExcessHorizontalSpace = true;
                    button1LData.horizontalAlignment = GridData.FILL;
                    button1.setLayoutData(button1LData);
                    button1.setText(Messages.TranspAnalysisUI_loadtext);
                    button1.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent e) {
                            mainButton();
                        }
                    });
                }
                {
                    composite2 = new Composite(composite1, SWT.NONE);
                    GridLayout composite2Layout = new GridLayout();
                    composite2Layout.numColumns = 2;
                    composite2Layout.marginHeight = 0;
                    GridData composite2LData = new GridData();
                    composite2LData.grabExcessHorizontalSpace = true;
                    composite2LData.horizontalAlignment = GridData.FILL;
                    composite2.setLayoutData(composite2LData);
                    composite2.setLayout(composite2Layout);
                    {
                        loadedFileLabel = new Label(composite2, SWT.WRAP);
                        GridData loadedFileLabelLData = new GridData();
                        loadedFileLabelLData.horizontalSpan = 2;
                        loadedFileLabelLData.grabExcessHorizontalSpace = true;
                        loadedFileLabelLData.horizontalAlignment = GridData.FILL;
                        loadedFileLabel.setLayoutData(loadedFileLabelLData);
                        loadedFileLabel.setText(Messages.TranspAnalysisUI_loadedfile
                                + Messages.TranspAnalysisUI_nofileloaded);
                    }
                    {
                        label1 = new Label(composite2, SWT.NONE);
                        GridData label1LData = new GridData();
                        label1LData.grabExcessHorizontalSpace = true;
                        label1.setLayoutData(label1LData);
                        label1.setText(Messages.TranspAnalysisUI_adjustcolumncount);
                    }
                    {
                        GridData spinner1LData = new GridData();
                        spinner1LData.horizontalAlignment = GridData.END;
                        spinner1LData.grabExcessHorizontalSpace = true;
                        spinner1 = new Spinner(composite2, SWT.NONE);
                        spinner1.setLayoutData(spinner1LData);
                        spinner1.addSelectionListener(new SelectionAdapter() {
                            public void widgetSelected(SelectionEvent evt) {
                                spinner1WidgetSelected(evt);
                            }
                        });
                        spinner1.setSelection(blocklength);
                    }
                }
            }
            {
                composite3 = new Composite(this, SWT.NONE);
                GridLayout composite3Layout = new GridLayout();
                composite3Layout.makeColumnsEqualWidth = true;
                GridData composite3LData = new GridData();
                composite3LData.verticalAlignment = GridData.FILL;
                composite3LData.horizontalAlignment = GridData.FILL;
                composite3LData.grabExcessHorizontalSpace = true;
                composite3.setLayoutData(composite3LData);
                composite3.setLayout(composite3Layout);
                {
                    text1 = new Label(composite3, SWT.WRAP);
                    GridData text1LData = new GridData();
                    text1LData.horizontalAlignment = GridData.FILL;
                    text1LData.grabExcessHorizontalSpace = true;
                    text1LData.widthHint = 50;
                    text1.setLayoutData(text1LData);
                    text1.setText(Messages.TranspAnalysisUI_findtherightblocklengthandtry);
                }
                {

                    Link link = new Link(composite3, SWT.NONE);
                    GridData linkLData = new GridData();
                    linkLData.verticalAlignment = GridData.FILL;
                    linkLData.verticalIndent = -5;
                    link.setLayoutData(linkLData);
                    link.setText(Messages.TranspAnalysisUI_furtheranalysislink);
                    link.setEnabled(false);
                    link.setVisible(false);
                    link.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent e) {
                            linkMouseDown(e);
                        }
                    });

                }
            }
            {
                composite5 = new Composite(this, SWT.NONE);
                GridLayout composite5Layout = new GridLayout();
                composite5Layout.makeColumnsEqualWidth = true;
                GridData composite5LData = new GridData();
                composite5LData.grabExcessHorizontalSpace = true;
                composite5LData.horizontalAlignment = GridData.FILL;
                composite5LData.verticalAlignment = GridData.FILL;
                composite5LData.grabExcessVerticalSpace = true;
                composite5.setLayoutData(composite5LData);
                composite5.setLayout(composite5Layout);
                {
                    GridData transpTableLData = new GridData();
                    transpTableLData.grabExcessHorizontalSpace = true;
                    transpTableLData.horizontalAlignment = GridData.FILL;
                    transpTableLData.verticalAlignment = GridData.FILL;
                    transpTableLData.grabExcessVerticalSpace = true;
//                    transpTableLData.minimumWidth = 200;
//                    transpTableLData.widthHint = 370;
                    transpTable = new TranspositionTableComposite(composite5, SWT.NONE);
                    transpTable.setLayoutData(transpTableLData);

                    transpTable.setColReorderObserver(this);
                }
            }
            {
                composite6 = new Composite(this, SWT.NONE);
                GridLayout composite6Layout = new GridLayout();
                composite6Layout.makeColumnsEqualWidth = true;
                GridData composite6LData = new GridData();
                composite6LData.grabExcessHorizontalSpace = true;
                composite6LData.horizontalAlignment = GridData.FILL;
                composite6LData.verticalAlignment = GridData.FILL;
                composite6LData.grabExcessVerticalSpace = true;
                composite6.setLayoutData(composite6LData);
                composite6.setLayout(composite6Layout);
                {
                    previewGroup = new Group(composite6, SWT.NONE);
                    GridLayout previewGroupLayout = new GridLayout();
                    previewGroupLayout.makeColumnsEqualWidth = true;
                    previewGroup.setLayout(previewGroupLayout);
                    GridData previewGroupLData = new GridData();
                    previewGroupLData.grabExcessHorizontalSpace = true;
                    previewGroupLData.horizontalAlignment = GridData.FILL;
                    previewGroupLData.grabExcessVerticalSpace = true;
                    previewGroupLData.verticalAlignment = GridData.FILL;
                    previewGroup.setLayoutData(previewGroupLData);
                    previewGroup.setText(Messages.TranspAnalysisUI_Results);
                    {
                        composite4 = new Composite(previewGroup, SWT.NONE);
                        GridLayout composite4Layout = new GridLayout();
                        composite4Layout.makeColumnsEqualWidth = true;
                        composite4Layout.marginWidth = 0;
                        composite4Layout.marginHeight = 0;
                        GridData composite4LData = new GridData();
                        composite4LData.grabExcessHorizontalSpace = true;
                        composite4LData.horizontalAlignment = GridData.FILL;
                        composite4.setLayoutData(composite4LData);
                        composite4.setLayout(composite4Layout);
                        {
                            label2 = new Label(composite4, SWT.NONE);
                            GridData label2LData = new GridData();
                            label2LData.grabExcessHorizontalSpace = true;
                            label2.setLayoutData(label2LData);
                            label2.setText(Messages.TranspAnalysisUI_keyfound);
                        }
                        {
                            composite8 = new Composite(composite4, SWT.NONE);
                            GridLayout composite8Layout = new GridLayout();
                            composite8Layout.numColumns = 2;
                            composite8Layout.marginWidth = 0;
                            composite8Layout.marginHeight = 0;
                            GridData composite8LData = new GridData();
                            composite8LData.grabExcessHorizontalSpace = true;
                            composite8LData.horizontalAlignment = GridData.FILL;
                            composite8.setLayoutData(composite8LData);
                            composite8.setLayout(composite8Layout);
                            {
                                labelKeypreview = new Label(composite8, SWT.BORDER);
                                GridData label3LData = new GridData();
                                label3LData.grabExcessHorizontalSpace = true;
                                label3LData.horizontalAlignment = GridData.FILL;
                                labelKeypreview.setLayoutData(label3LData);
                                labelKeypreview.setText(""); //$NON-NLS-1$
                            }
                            {
                                linkExport = new Link(composite8, SWT.NONE);
                                GridData linkExportLData = new GridData();
                                linkExport.setLayoutData(linkExportLData);
                                linkExport.setText("<a>" + Messages.TranspAnalysisUI_Export + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                linkExport.addSelectionListener(new SelectionAdapter() {
                                    public void widgetSelected(SelectionEvent evt) {
                                        if (keyUsedToEncrypt != null) {
                                            KeyViewer myKeyViewer = new KeyViewer(getShell(),
                                                    Messages.TranspAnalysisUI_keyviewer, keyUsedToEncrypt);
                                            myKeyViewer.open();
                                        }
                                    }
                                });
                            }
                        }
                        {
                            composite7 = new Composite(composite4, SWT.NONE);
                            GridLayout composite7Layout = new GridLayout();
                            composite7Layout.marginHeight = 0;
                            composite7Layout.marginWidth = 0;
                            GridData composite7LData = new GridData();
                            composite7LData.grabExcessHorizontalSpace = true;
                            composite7LData.horizontalAlignment = GridData.FILL;
                            composite7LData.verticalIndent = 12;
                            composite7.setLayoutData(composite7LData);
                            composite7.setLayout(composite7Layout);
                            {
                                textpreviewDescription = new Label(composite7, SWT.NONE);
                                GridData textpreviewDescriptionLData = new GridData();
                                textpreviewDescription.setLayoutData(textpreviewDescriptionLData);
                                textpreviewDescription.setText(Messages.TranspAnalysisUI_preview);
                            }
                            {
                            	compReadDir = new Composite(composite7, SWT.NONE);
                            	GridLayout compReadDirLayout = new GridLayout();
                            	compReadDirLayout.numColumns = 2;
                            	compReadDirLayout.marginWidth = 0;
                            	compReadDirLayout.marginHeight = 0;
                            	GridData compReadDirLData = new GridData();
                            	compReadDirLData.grabExcessHorizontalSpace = true;
                            	compReadDir.setLayoutData(compReadDirLData);
                            	compReadDir.setLayout(compReadDirLayout);
                            	{
                            		labelReadDir = new Label(compReadDir, SWT.NONE);
                            		GridData labelReadDirLData = new GridData();
                            		labelReadDir.setLayoutData(labelReadDirLData);
                            		labelReadDir.setText(Messages.TranspAnalysisUI_read_out_mode_label);
                            	}
                            	{
                            		readoutDirChooser = new ReadDirectionChooser(compReadDir, true);
                            		GridData readoutDirChooserLData = new GridData();
                            		readoutDirChooser.setLayoutData(readoutDirChooserLData);
                            		readoutDirChooser.setDirection(false);

                            		readoutDirChooser.getInput().addObserver(new Observer() {
										public void update(Observable o, Object arg) {
											if(arg==null) previewPlaintext();
										}
									});
                            	}
                            }
                        }
                    }
                    {
                        previewText = new Text(previewGroup, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
                        GridData previewTextLData = new GridData();
                        previewTextLData.grabExcessHorizontalSpace = true;
                        previewTextLData.horizontalAlignment = GridData.FILL;
                        previewTextLData.grabExcessVerticalSpace = true;
                        previewTextLData.verticalAlignment = GridData.FILL;
                        previewTextLData.minimumHeight = 20;
                        previewTextLData.widthHint = 150;
                        previewText.setLayoutData(previewTextLData);
                        previewText.setEditable(false);
                    }
                    {
                    	btnDecipher = new Button(previewGroup, SWT.PUSH | SWT.CENTER);
                    	GridData btnDecipherLData = new GridData();
                    	btnDecipherLData.grabExcessHorizontalSpace = true;
                    	btnDecipherLData.horizontalAlignment = GridData.END;
                    	btnDecipher.setLayoutData(btnDecipherLData);
                    	btnDecipher.setText(Messages.TranspAnalysisUI_decipher);
                    	btnDecipher.addSelectionListener(new SelectionAdapter() {
                    		public void widgetSelected(SelectionEvent evt) {
                    			decipherIntoEditor();
                    		}
                    	});
                    	btnDecipher.setEnabled(false);
                    }
                }
            }
            this.layout();
            pack();
        } catch (Exception e) {
            LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
        }

        hideObject(loadedFileLabel, true);
    }

    /**
     * Excludes a control from Layout calculation
     *
     * @param that
     * @param hideit
     */
    private void hideObject(final Control that, final boolean hideit) {
        GridData GData = (GridData) that.getLayoutData();
        GData.exclude = hideit;
        that.setVisible(!hideit);
        Control[] myArray = {that};
        layout(myArray);
    }

    private void mainButton() {
        setTableTextWithWizard();
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
            LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    private String getActiveEditorText() {
        InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
        if (stream != null)
            return InputStreamToString(stream);
        return ""; //$NON-NLS-1$
    }

    private String getActiveEditorTitle() {
        String result = EditorsManager.getInstance().getActiveEditorTitle();
        if (result != null)
            return result;
        return ""; //$NON-NLS-1$
    }

    private String buildFileName(String filename, int manualInputState) {
        String result = filename;

        if (result.equals("")) { //$NON-NLS-1$
            if (manualInputState == 2)
                result = Messages.TranspAnalysisUI_manualinput;
            else
                result = null;
        } else {
            if (manualInputState == 1)
                result += " + " + Messages.TranspAnalysisUI_manualinput; //$NON-NLS-1$
            else
            if (manualInputState == 2)
                result = Messages.TranspAnalysisUI_manualinput; //$NON-NLS-1$
        }

        return result;
    }

    private void setTableTextWithWizard() {
        textWizard = new TranspTextWizard();
        dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), textWizard);

        if (text != null && text.length() > 0) {
            textWizard.setAnalysisText(text);
            textWizard.setManualInputState(this.manualInputState);
            textWizard.setFileName(this.fileName);
        } else {
            textWizard.setAnalysisText(getActiveEditorText());
            textWizard.setManualInputState(0);
            textWizard.setFileName(getActiveEditorTitle());
        }
        textWizard.setBlocklength(blocklength);
        textWizard.setCroplength(croplength);
        textWizard.setCroptext(crop);
        textWizard.setReadInDirection(readInMode);

        int result = dialog.open();

        if (result == 0) {
            setText(textWizard.getText(), false);
            setCrop(textWizard.getCrop(), false);
            setCroplength(textWizard.getCropsize(), false);
            setBlocklength(textWizard.getBlocksize(), false);
            setReadInMode(textWizard.getReadInDirection(), false);

            doAutoCrop = (blocklength > 0 && !crop || blocklength == 0 && crop);

            transpTable.setReadInOrder(readInMode, false);
            transpTable.setText(text, blocklength, !crop, croplength);

            columnsReordered(transpTable.getColumnOrder());

            if (textWizard.getEditorFileName() != null) {
                this.fileName = textWizard.getEditorFileName();
                this.manualInputState = textWizard.getManualInputState();

                hideObject(loadedFileLabel, false);
                loadedFileLabel.setText(Messages.TranspAnalysisUI_loadedfile
                        + buildFileName(this.fileName, this.manualInputState));

                //because label wrapping could occur.
                this.layout(true);
            }
        }
    }

    private void spinner1WidgetSelected(SelectionEvent evt) {
        if (doAutoCrop) {
            if (spinner1.getSelection() > 0) {
                // if blocklength is greater than zero, make the text now uncropped

                setCrop(false, false);
                setBlocklength(spinner1.getSelection(), false);
                refreshTable();
            } else {
                // if blocklength is zero, make the text now cropped

                setCrop(true, false);
                setBlocklength(spinner1.getSelection(), false);
                refreshTable();
            }
        } else {
            setBlocklength(spinner1.getSelection(), true);
        }
        if (transpTable != null && !transpTable.isDisposed())
            columnsReordered(transpTable.getColumnOrder());

    }

    private void refreshTable() {
    	transpTable.setReadInOrder(readInMode, false);
        transpTable.setText(text, blocklength, !crop, croplength);
    }

    private void columnsReordered(int[] cols) {
        int[] order;
        if (cols.length != this.croplength)
            order = cols.clone();
        else
            order = null;
        setKeyUsedToEncrypt(order);

        if (transpTable != null) {
            previewPlaintext();
        }
    }

    private void previewPlaintext() {
		if(transpTable != null) {
			TranspositionTable table = new TranspositionTable(transpTable.getColumnCountDisplayed());
			table.fillCharsIntoTable(transpTable.getText().toCharArray(), false);
			previewText.setText(String.valueOf( table.readOutContent(readoutDirChooser.getInput().getContent()) ));
		}
	}

	public void update(Observable arg0, Object arg1) {
        if (arg1 instanceof int[]) {
            columnsReordered((int[]) arg1);
        }
    }

    /**
     * @param actualKey the actualKey to set
     */
    public void setKeyUsedToEncrypt(int[] key) {
        int actualKeyLength;
        if (key != null) {
        	this.keyUsedToEncrypt = new TranspositionKey(key);
            labelKeypreview.setText(" " + keyUsedToEncrypt.toStringOneRelative() + "|"); //$NON-NLS-1$ //$NON-NLS-2$
            btnDecipher.setEnabled(true);
            actualKeyLength = keyUsedToEncrypt.getLength();
            labelKeypreview.setEnabled(true);
        } else {
        	this.keyUsedToEncrypt = null;
            btnDecipher.setEnabled(false);
            labelKeypreview.setText(Messages.TranspAnalysisUI_keypreview_zerocolumns);
            actualKeyLength = 0;
            labelKeypreview.setEnabled(false);
        }

        boolean changeLength = (actualKeyLength != this.blocklength);
        if (changeLength)
            setBlocklength(actualKeyLength);
    }

    private void linkMouseDown(SelectionEvent evt) {
        if (text.equals("")) //$NON-NLS-1$
            mainButton();

        analysisWizard = new AnalysisWizard(text, transpAnalysisDataobject);
        WizardDialog analysisDialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                analysisWizard);

        if (analysisDialog.open() == WizardDialog.OK) {
            transpAnalysisDataobject = analysisWizard.getDataobject();
        }
    }

    private void decipherIntoEditor() {
        final String TRANSPOSITION_ALGORITHM = "org.jcryptool.crypto.classic.transposition.algorithm"; //$NON-NLS-1$

        OperationsPlugin op = OperationsPlugin.getDefault();
        IAction[] actions = op.getAlgorithmsManager().getShadowAlgorithmActions();
        for (final IAction action : actions) {
            if (TRANSPOSITION_ALGORITHM.equals(action.getId())) {
                ClassicDataObject myDO = new ClassicDataObject();
                AbstractAlphabet ascii = AlphabetsManager.getInstance().getAlphabetByName("Printable ASCII"); //$NON-NLS-1$
                myDO.setAlphabet(ascii);
                char[] key = new char[keyUsedToEncrypt.getLength()+2];
                key[0] = getInputMethodDecryption() ? ascii.getCharacterSet()[0] : ascii.getCharacterSet()[1];
                key[1] = getOutputMethodDecryption() ? ascii.getCharacterSet()[0] : ascii.getCharacterSet()[1];
                System.arraycopy(keyUsedToEncrypt.toUnformattedChars(ascii).toCharArray(), 0, key, 2, keyUsedToEncrypt.getLength());
                myDO.setKey(key);
                myDO.setKey2("".toCharArray()); //$NON-NLS-1$
                try {
                    myDO.setInputStream(new BufferedInputStream(new ByteArrayInputStream(text.getBytes(IConstants.UTF8_ENCODING))));
                } catch (UnsupportedEncodingException e) {
                    LogUtil.logError(TranspositionAnalysisPlugin.PLUGIN_ID, e);
                }
                myDO.setOpmode(AbstractAlgorithm.DECRYPT_MODE);
                myDO.setFilterNonAlphaChars(true);
                myDO.setTransformData(new TransformData());

                ((ShadowAlgorithmAction) action).run(myDO);
                break;
            }
        }
    }

	private boolean getOutputMethodDecryption() {
		return readInMode ;
	}

	private boolean getInputMethodDecryption() {
		// its vice-versa, because the entered mode was the read-OUT of the DECRYPTION
		return readoutDirChooser.getInput().getContent();
	}

}
