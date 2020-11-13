// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.analysis.friedman.FriedmanPlugin;
import org.jcryptool.analysis.friedman.IFriedmanAccess;
import org.jcryptool.analysis.friedman.calc.FriedmanCalc;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.crypto.ui.background.BackgroundJob;
import org.jcryptool.crypto.ui.textloader.ui.wizard.TextLoadController;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

/**
 * @author SLeischnig
 *
 */
public class FriedmanGraphUI extends Composite implements IFriedmanAccess {

	private Button btnShowTable;
	private Button btnResetGraph;
	private String message;
	private TextInputWithSource lastSuccessfullLoadedText;
	
	private static final int friedman_max_text_length = 550000;

	public FriedmanGraphUI(final Composite parent, final int style) {
		super(parent, style);
		initGUI();

	}

	private void initGUI() {
		try {
			this.setLayout(new GridLayout());
			
			TitleAndDescriptionComposite td = new TitleAndDescriptionComposite(this);
			td.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			td.setTitle(Messages.FriedmanGraphUI_0);
			td.setDescription(Messages.FriedmanGraphUI_1);
			

			Composite composite1 = new Composite(this, SWT.NONE);
			GridLayout composite1Layout = new GridLayout();
			composite1Layout.numColumns = 4;
			composite1Layout.marginHeight = 0;
			composite1Layout.marginWidth = 0;
			composite1Layout.horizontalSpacing = 0;
			composite1Layout.makeColumnsEqualWidth = true;
			composite1.setLayout(composite1Layout);

			composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));


			TextLoadController textSelector = new TextLoadController(composite1, this, SWT.NONE, true, true);
			GridData textSelectorLData = new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1);
			textSelectorLData.widthHint = 275;
			textSelector.setLayoutData(textSelectorLData);

			Button button2 = new Button(composite1, SWT.PUSH | SWT.CENTER);
			GridData button2LData = new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1);
			button2LData.widthHint = 275;
			button2.setLayoutData(button2LData);
			button2.setText(Messages.FriedmanGraphUI_start);
			button2.setEnabled(false);
			button2.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					startAnalysis(e);
				}
			});

			textSelector.addObserver(new Observer() {

				@Override
				public void update(Observable o, Object arg) {
					if (textSelector.getText() != null) {
						if (textSelector.getText().getText() != null) {
							if(textSelector.getText().getText().length() < friedman_max_text_length){
							message = textSelector.getText().getText();
							lastSuccessfullLoadedText = textSelector.getText();
							}
							else{
								
								boolean result = MessageDialog.openQuestion(FriedmanGraphUI.this.getDisplay().getActiveShell(), Messages.FriedmanGraphUI_warning, Messages.FriedmanGraphUI_warning_text);
								if(result) {
									message = textSelector.getText().getText();
								}
								else {
									textSelector.setTextData(lastSuccessfullLoadedText, null, true);
									return;
								}
							}
						}
					}

					if (message != null && message.length() > 0) {
						button2.setEnabled(true);

						// After loading a text the button is replaced with information.
						// To let this be displayed properly the button size is removed,
						// and resizes to preferred
						textSelectorLData.widthHint = SWT.DEFAULT;
						textSelector.pack();
						composite1.layout();
					}
				}
			});

			btnShowTable = new Button(composite1, SWT.PUSH | SWT.CENTER);
			GridData btnShowTableLData = new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1);
			btnShowTableLData.widthHint = 275;
			btnShowTable.setLayoutData(btnShowTableLData);
			btnShowTable.setText(Messages.FriedmanGraphUI_showastable);
			btnShowTable.setEnabled(false);
			btnShowTable.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					showTableDialog(e);
				}
			});

			Label spacer4 = new Label(composite1, SWT.NONE);
			spacer4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
			Label spacer5 = new Label(composite1, SWT.NONE);
			spacer5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

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

			myGraph = new CustomFriedmanCanvas(group1, SWT.DOUBLE_BUFFERED);

			GridLayout myGraphLayout = new GridLayout();
			myGraph.setLayout(myGraphLayout);
			GridData myGraphLData = new GridData();
			myGraphLData.verticalAlignment = GridData.FILL;
			myGraphLData.grabExcessHorizontalSpace = true;
			myGraphLData.horizontalAlignment = GridData.FILL;
			myGraphLData.grabExcessVerticalSpace = true;
			myGraph.setLayoutData(myGraphLData);

			Composite composite2 = new Composite(this, SWT.NONE);
			GridLayout composite2Layout = new GridLayout();
			composite2Layout.numColumns = 2;
			GridData composite2LData = new GridData();
			composite2LData.grabExcessHorizontalSpace = true;
			composite2LData.horizontalAlignment = GridData.FILL;
			composite2.setLayoutData(composite2LData);
			composite2.setLayout(composite2Layout);

			btnResetGraph = new Button(composite2, SWT.PUSH | SWT.CENTER);
			btnResetGraph.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
			btnResetGraph.setText(Messages.FriedmanGraphUI_2);
			btnResetGraph.setEnabled(false);
			btnResetGraph.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					myGraph.getFrequencyGraph().resetDrag(40);
					myGraph.redraw();
				}
			});

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
	private String goodCiphertext = ""; //$NON-NLS-1$
	private String cachedResult = ""; //$NON-NLS-1$

	private FriedmanCalc myAnalysis;
	private TextViewer myDialog;
	public FriedmanCalc __return_myAnalysis;

	@Override
	public final void execute(final boolean executeCalc) {
		if (executeCalc) {
			executeMainfunction();
		}
	}

	public class FriedmanAnalysisJob extends BackgroundJob {

		public String goodCiphertext;
		
		@Override
		public String name() {
			return "Frequency Analysis";
		}

		@Override
		public IStatus computation(IProgressMonitor monitor) {
			FriedmanGraphUI.this.__return_myAnalysis = new FriedmanCalc(this.goodCiphertext, Math.min(this.goodCiphertext.length(), 2000));
			return Status.OK_STATUS;
		}

	}
	/**
	 * main friedman analysis procedure
	 */
	private void analyze() {
		FriedmanAnalysisJob backgroundJob = new FriedmanAnalysisJob();
		backgroundJob.goodCiphertext = goodCiphertext;

		backgroundJob.finalizeListeners.add(status -> {
			getDisplay().syncExec(() -> {
				backgroundJob.liftNoClickDisplaySynced(getDisplay());
				if (status == Status.OK_STATUS) {
					myAnalysis = __return_myAnalysis;
					myGraph.setAnalysis(myAnalysis);
					myGraph.redraw();
				}
			});
		});
		backgroundJob.imposeNoClickDisplayCurrentShellSynced(getDisplay());
		backgroundJob.runInBackground();
	}

	// /**
	// * message shows a message box
	// */
	// private void showMessage(final String message) {
	// MessageBox messageBox = new MessageBox(getShell());
	// messageBox.setText(""); //$NON-NLS-1$
	// messageBox.setMessage(message);
	// messageBox.open();
	// }

	// Leaves only chars with byte values from 65 to 90 (A-Z), thus replacing ö, ä,
	// ü, ß and casting
	// to upper case
	private String makeFormattedString(final String input) {
		String textOld = input;
		textOld = textOld.toUpperCase();
		// textOld.replace(" ", ""); //$NON-NLS-1$ //$NON-NLS-2$
		// textOld.replace("\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
		// textOld.replace("\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
		// textOld.replace("Ä", "AE"); //$NON-NLS-1$ //$NON-NLS-2$
		// textOld.replace("Ö", "OE"); //$NON-NLS-1$ //$NON-NLS-2$
		// textOld.replace("Ü", "UE"); //$NON-NLS-1$ //$NON-NLS-2$
		// textOld.replace("ß", "SS"); //$NON-NLS-1$ //$NON-NLS-2$

		for (int i = 0; i < textOld.length();) {
			if (textOld.charAt(i) < 65 || textOld.charAt(i) > 90) {
				textOld = textOld.substring(0, i).concat(textOld.substring(i + 1));
			} else {
				i++;
			}
		}
		return textOld;
	}

	// /**
	// * reads the current value from an input stream
	// *
	// * @param in
	// * the input stream
	// */
	// private String InputStreamToString(InputStream in) {
	// BufferedReader reader = null;
	// try {
	// reader = new BufferedReader(new InputStreamReader(in,
	// IConstants.UTF8_ENCODING));
	// } catch (UnsupportedEncodingException e1) {
	// LogUtil.logError(FriedmanPlugin.PLUGIN_ID, e1);
	// }
	//
	// StringBuffer myStrBuf = new StringBuffer();
	// int charOut = 0;
	// String output = ""; //$NON-NLS-1$
	// try {
	// while ((charOut = reader.read()) != -1) {
	// myStrBuf.append(String.valueOf((char) charOut));
	// }
	// } catch (IOException e) {
	// LogUtil.logError(FriedmanPlugin.PLUGIN_ID, e);
	// }
	// output = myStrBuf.toString();
	// return output;
	// }

	// /**
	// * reads the text from the opened editor
	// */
	// private void getText() {
	// InputStream stream =
	// EditorsManager.getInstance().getActiveEditorContentInputStream();
	// if (stream == null) {
	// showMessage(Messages.FriedmanGraphUI_openandselect);
	// return;
	// } else {
	// ciphertext = InputStreamToString(stream);
	// }
	//
	// analyzedFile = EditorsManager.getInstance().getActiveEditorTitle();
	//
	// if (ciphertext.hashCode() != ciphertextHash) {
	// ciphertextHash = ciphertext.hashCode();
	// goodCiphertext = makeFormattedString(ciphertext);
	// }
	// }

	/**
	 * The main function of this plug-in
	 */
	private void executeMainfunction() {
		// getText();

		if (message != null && message.length() > 0) {
			goodCiphertext = makeFormattedString(message);
			btnShowTable.setEnabled(true);
			btnResetGraph.setEnabled(true);
			analyze();
		}

		// if (analyzedFile != null) {
		// if (analyzedFile.length() != 0) {
		// didSomeCalc = true;
		// }
		// if (didSomeCalc) {
		// btnShowTable.setEnabled(true);
		// }
		//
		// analyze();
		// }
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