//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.freqanalysis.ui;

import java.math.BigInteger;
//import java.io.File;
//import java.io.FileReader;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
//import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.freqanalysis.calc.FreqAnalysisCalc;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.crypto.ui.background.BackgroundJob;
//import java.io.BufferedReader;
import org.jcryptool.crypto.ui.textloader.ui.wizard.TextLoadController;
//import org.jcryptool.core.operations.editors.EditorsManager;
//import org.jcryptool.crypto.ui.textmodify.wizard.ModifyWizard;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;

/**
 * @author SLeischnig
 *
 */
public class SimpleAnalysisUI extends AbstractAnalysisUI {
//	private Button button;
//	private Button button0;
//	private Button button1;
	private Composite composite0;
	private Composite composite1;
	private Group group1;
	private Label label1;
//	private Button button2;
	private Button button4;
	private Composite composite2;
	private Group group4;
	private Label label2;
	private Group group2;
	private Composite composite3;
	private TabFolder tabFolder1;
	private TabItem tabItem1;
	private TabItem tabItem2;
	private Composite composite4;
	private Label label3;
	private Label label4;
	private Label label5;
	private Text text1;
	private TextInputWithSource source;

	private FreqAnalysisCalc myAnalysis;
	TransformData myModifySettings;
	private TextLoadController textloader;
	public FreqAnalysisCalc return__freqanalysis;
	
	private static final int simpleFreqAnalysis_text_max_length = 999999; 
	private TextInputWithSource lastSuccessfullLoadedText;

	public SimpleAnalysisUI(final Composite parent, final int style) {
		super(parent, style);
		initGUI();
		hideObject(group2, button3.getSelection());

		myModifySettings = new TransformData();
		myModifySettings.setUnmodified();
	}

	private void initGUI() {
		setLayout(new GridLayout());

		composite0 = new Composite(this, SWT.NONE);
		composite0.setLayout(new GridLayout(2, false));
		composite0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		textloader = new TextLoadController(composite0, this, SWT.NONE, true, true);
		textloader.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				
				if (textloader.getText() != null) {
					myGraph.getFrequencyGraph().setInstruction(Messages.FreqAnalysisGraph_graph1);
					myGraph.redraw();
					if(textloader.getText().getText().length() < simpleFreqAnalysis_text_max_length) {
					text = textloader.getText().getText();
					
					lastSuccessfullLoadedText = textloader.getText();
					source = textloader.getText();
					}else{
						
						boolean result = MessageDialog.openQuestion(SimpleAnalysisUI.this.getShell(), Messages.SimpleAnalysisUI_warning, Messages.SimpleAnalysisUI_warning_text);
						if(result) {
							text = textloader.getText().getText();
							source = textloader.getText();
						}
						else {
							textloader.setTextData(lastSuccessfullLoadedText, null, true);
							source = lastSuccessfullLoadedText;
							return;
						}
					}
// 					button1.setEnabled(true);
					recalcSourceInfo();
					
					
					myGraph.getFrequencyGraph().setInstruction(Messages.FreqAnalysisGraph_shiftgraph0);

					if (text.equals("") || text == null) {
						MessageDialog.openInformation(getShell(), Messages.AbstractAnalysisUI_0,
								Messages.AbstractAnalysisUI_2);
					} else {
						recalcGraph();
					}				
				}
			}
		});
		
//		button = new Button(composite0, SWT.NONE);
//		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		button.setText(Messages.FullAnalysisUI_loadtext);
//
//		button0 = new Button(composite0, SWT.NONE);
//		button0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		button0.setText(Messages.FullAnalysisUI_loadeditor);
//
//		button1 = new Button(this, SWT.PUSH | SWT.CENTER);
//		button1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		button1.setText(Messages.FullAnalysisUI_startanalysis);
//		button1.setEnabled(false);
//
//		button.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent evt) {
//				try {
//					myGraph.getFrequencyGraph().setInstruction(Messages.FreqAnalysisGraph_graph1);
//					myGraph.redraw();
//					Display display = Display.getDefault();
//					Shell dialogShell = new Shell(display, SWT.APPLICATION_MODAL);
//					FileDialog fd_ChooseFile = new FileDialog(dialogShell, SWT.OPEN);
//					fd_ChooseFile.setFilterPath("\\"); //$NON-NLS-1$
//					fd_ChooseFile.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
//					File file_LoadReferenceText = new File(fd_ChooseFile.open());
//					source = file_LoadReferenceText.getAbsolutePath();
//					BufferedReader br = new BufferedReader(new FileReader(file_LoadReferenceText));
//					text = new String();
//					String line;
//					while ((line = br.readLine()) != null) {
//						text += line;
//					}
//
//					if (text == "") {
//						br.close();
//						throw new Exception();
//					}
//					button1.setEnabled(true);
//					recalcSourceInfo();
//					br.close();
//				} catch (Exception ex) {
//					MessageDialog.openInformation(getShell(), Messages.AbstractAnalysisUI_0,
//							Messages.AbstractAnalysisUI_2);
//				}
//			}
//		});

//		button0.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent evt) {
//				if (checkEditor()) {
//					myGraph.getFrequencyGraph().setInstruction(Messages.FreqAnalysisGraph_graph1);
//					myGraph.redraw();
//					text = getEditorText();
//					source = EditorsManager.getInstance().getActiveEditorTitle();
//					button1.setEnabled(true);
//					recalcSourceInfo();
//				}
//			}
//		});

//		button1.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent evt) {
//				myGraph.getFrequencyGraph().setInstruction(Messages.FreqAnalysisGraph_shiftgraph0);
//
//				if (text.equals("") || text == null) {
//					MessageDialog.openInformation(getShell(), Messages.AbstractAnalysisUI_0,
//							Messages.AbstractAnalysisUI_2);
//				} else {
//					recalcGraph();
//				}				
//			}
//		});

		composite1 = new Composite(this, SWT.NONE);
		composite1.setLayout(new GridLayout(2, false));
		composite1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group1 = new Group(composite1, SWT.NONE);
		group1.setLayout(new GridLayout());
		group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		group1.setText(Messages.SimpleAnalysisUI_graphlabel);

		myGraph = new CustomFreqCanvas(group1, SWT.NONE);
		myGraph.setLayout(new GridLayout());
		myGraph.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		group4 = new Group(composite1, SWT.NONE);
		group4.setLayout(new GridLayout());
		group4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		group4.setText(Messages.SimpleAnalysisUI_properties);

		tabFolder1 = new TabFolder(group4, SWT.NONE);

		tabItem1 = new TabItem(tabFolder1, SWT.NONE);
		tabItem1.setText(Messages.FullAnalysisUI_firsttablabel);

		composite3 = new Composite(tabFolder1, SWT.NONE);

		composite3.setLayout(new GridLayout());
		composite3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tabItem1.setControl(composite3);

		composite2 = new Composite(composite3, SWT.NONE);
		composite2.setLayout(new GridLayout());
		composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		button3 = new Button(composite2, SWT.RADIO);
		button3.setText(Messages.SimpleAnalysisUI_monoalphabetic);
		button3.setSelection(true);
		button3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				polyOnOffSelected(evt);
			}
		});

		button4 = new Button(composite2, SWT.RADIO);
		button4.setText(Messages.SimpleAnalysisUI_polyalphabetic);
		button4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				polyOnOffSelected(evt);
			}
		});
		
		
		Label lblPolyalphabetic = new Label(composite2, SWT.WRAP);
		lblPolyalphabetic.setText(Messages.FullAnalysisUI_6);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.widthHint = 100;
		lblPolyalphabetic.setLayoutData(layoutData);

		group2 = new Group(composite2, SWT.NONE);
		group2.setLayout(new GridLayout(2, false));
		group2.setText(Messages.SimpleAnalysisUI_vigeneresettings);

		spinner1 = new Spinner(group2, SWT.NONE);
		spinner1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent evt) {
				recalcGraph();
			}
		});

		spinner1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				changedVigOptions();
			}
		});
		spinner1.setSelection(1);

		label1 = new Label(group2, SWT.NONE);
		label1.setText(Messages.SimpleAnalysisUI_keylength);

		spinner2 = new Spinner(group2, SWT.NONE);
		spinner2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent evt) {
				recalcGraph();
			}
		});

		spinner2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				changedVigOptions();
			}
		});
		spinner2.setSelection(0);

		label2 = new Label(group2, SWT.NONE);
		label2.setText(Messages.SimpleAnalysisUI_offset);

//		button2 = new Button(composite2, SWT.PUSH | SWT.CENTER);
//		button2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
//		button2.setText(Messages.SimpleAnalysisUI_filtersettings);
//		button2.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDown(MouseEvent evt) {
//				myModifySettings = getTransformWizardSettings(myModifySettings);
//				recalcGraph();
//			}
//		});

		tabItem2 = new TabItem(tabFolder1, SWT.NONE);
		tabItem2.setText(Messages.FullAnalysisUI_thirdtablabel);
		composite4 = new Composite(tabFolder1, SWT.NONE);
		composite4.setLayout(new GridLayout());
		tabItem2.setControl(composite4);

		label3 = new Label(composite4, SWT.WRAP);
		GridData layoutData1 = new GridData(SWT.FILL, SWT.FILL, true, false);
		label3.setLayoutData(layoutData1);
		label4 = new Label(composite4, SWT.WRAP);
		GridData layoutData2 = new GridData(SWT.FILL, SWT.FILL, true, false);
		label4.setLayoutData(layoutData2);
		label5 = new Label(composite4, SWT.NONE);
		label5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		text1 = new Text(composite4, SWT.V_SCROLL | SWT.BORDER | SWT.MULTI | SWT.WRAP);
		text1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		recalcSourceInfo();

		tabFolder1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder1.setSelection(0);

		layout();
	}

	/**
	 * Method for aggregating information about source text file.
	 */
	protected void recalcSourceInfo() {
		if (text == null) {
			label3.setText(Messages.FullAnalysisUI_source + " -");
			label4.setText(Messages.FullAnalysisUI_textlength + " -");
			text1.setText("");
			composite4.layout(new Control[] {label3, label4});

		} else {
			label3.setText(Messages.FullAnalysisUI_source + " " + source.toString());

			String totalLength = Integer.toString(text.length());
			label4.setText(Messages.FullAnalysisUI_textlength + " " + totalLength);

			label5.setText(Messages.FullAnalysisUI_textexcerpt);
			text1.setText(text.substring(0, (text.length() > 1000) ? 1000 : text.length())
					+ ((text.length() > 1000) ? "..." : ""));
			text1.setEditable(false);
		}
	}

	/**
	 * executes a TextModify Wizard.
	 * 
	 * @param the Settings that have to be displayed at the beginning.
	 * @return the selected settings.
	 */
//	private TransformData getTransformWizardSettings(final TransformData predefined) {
//
//		ModifyWizard wizard = new ModifyWizard();
//		wizard.setPredefinedData(predefined);
//		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
//		dialog.setHelpAvailable(false);
//		int result = dialog.open();
//
//		if (result == 0) {
//			return wizard.getWizardData();
//		} else {
//			return predefined;
//		}
//
//	}

	public class FreqAnalysisJob extends BackgroundJob {

		private String text;
		private int myLength;
		private int myOffset;
		private String myOverlayAlphabet;
		public TransformData myModifySettings;
		
		@Override
		public String name() {
			return "Frequency Analysis";
		}

		@Override
		public IStatus computation(IProgressMonitor monitor) {
			SimpleAnalysisUI.this.return__freqanalysis = new FreqAnalysisCalc(this.text, this.myLength, this.myOffset, this.myModifySettings);
			return Status.OK_STATUS;
		}

	}
	/**
	 * frequency analysis main procedure
	 */
	@Override
	protected void analyze() {
		FreqAnalysisJob job = new FreqAnalysisJob();
		job.text = text;
		job.myLength = myLength;
		job.myOffset = myOffset;
		job.myModifySettings = myModifySettings;
		job.finalizeListeners.add(status -> {
			getDisplay().syncExec(() -> {
				job.liftNoClickDisplaySynced(getDisplay());
				if (status == Status.OK_STATUS) {
					myAnalysis = return__freqanalysis;
					myGraph.setAnalysis(myAnalysis);
				}
			});
		});
		job.imposeNoClickDisplayCurrentShellSynced(getDisplay());
		job.runInBackground();
	}

	/**
	 * excludes a control from the Layout calculation
	 * 
	 * @param that
	 * @param hideit
	 */
	private void hideObject(final Control that, final boolean hideit) {
		GridData GData = (GridData) that.getLayoutData();
		// the (GData != null) test is to work around a NullPointerException (bug #95)
		// happening after one leaves the Frequency Analysis open
		// while closing down JCrypTool, then restarts JCrypTool
		if (GData != null) {
			GData.exclude = true && hideit;
		}
		that.setVisible(true && !hideit);
		Control[] myArray = { that };
		layout(myArray);
	}

	private void polyOnOffSelected(final SelectionEvent evt) {
		hideObject(group2, button3.getSelection());

	}

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
	}

	public final void execute(final int keyLength, final int keyPos, final boolean resetShift,
			final boolean executeCalc) {
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
				recalcGraph();
			}
		}
		if (keyPos > -1) {
			spinner2.setSelection(keyPos);
			changedVigOptions();
			recalcGraph();
		}

		if (resetShift) {
			myGraph.getFrequencyGraph().resetDrag();
		}
		if (executeCalc) {
			if (checkEditor()) {
				text = getEditorText();
				recalcGraph();
			}
		}

	}

	public void resetClick() {
		// TODO Auto-generated method stub
		
	}

}
