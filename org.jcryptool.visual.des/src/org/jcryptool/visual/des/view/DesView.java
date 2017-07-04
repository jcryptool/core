package org.jcryptool.visual.des.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.des.algorithm.DESController;

public class DesView extends ViewPart {
	public DesView() {
	}

	public static final String ID = "org.jcryptool.visual.des.view.DesView"; //$NON-NLS-1$

	// Controller
	private DESController dESCon = new DESController();

	// Layout Components
	// Algorithm Study
	ScrolledComposite wrapper = null;
	TabFolder tfolder = null;
	private TabFolder tfolder_1;
	TabItem tabAlg = null;;
	Label lblAlgTitle = null;
	Text lblAlgInformationText = null;
	SashForm sashAlgDivideMain = null;
	Composite comAlg = null;
	Composite comAlgHeader = null;
	Label lblAlgorithm = null;
	Composite comAlgMain = null;
	Composite comAlgMainDown = null;
	Composite comAlgMainLeft = null;
	Composite comAlgMainRight = null;
	Composite comAlgMainRightUp = null;
	Composite comAlgMainRightDown = null;
	Group grpAlgInput = null;
	Group grpAlgOutput = null;
	Group grpAlgInformation = null;
	Group grpAlgStatus = null;
	StyledText txtAlgStatus = null;
	Composite comAlgMainTop = null;
	Composite comAlgInputMode = null;
	Composite comAlgInputKey = null;
	Composite comAlgInputData = null;
	Button btnAlgDecrypt = null;
	Button btnAlgEncrypt = null;
	Label lblAlgMode = null;
	Button btnAlgK0 = null;
	Button btnAlgK3 = null;
	Button btnAlgK5 = null;
	Button btnAlgK6 = null;
	Button btnAlgK9 = null;
	Button btnAlgK10 = null;
	Button btnAlgK12 = null;
	Button btnAlgK15 = null;
	Label lblAlgInputKey = null;
	Button btnAlgManual = null;
	Label lblAlgInputData = null;
	Label lblAlgInputPlaintext = null;
	Text txtAlgKeyManual = null;
	Text txtAlgInputData = null;
	TabFolder tfAlgOutput = null;
	TabItem tbtmAlgStandard = null;
	TabItem tbtmAlgM0M17 = null;
	TabItem tbtmAlgDES = null;
	TabItem tbtmAlgHamming = null;
	TabItem tbtmAlgRoundkeys = null;
	TabItem tbtmAlgCDMatrix = null;
	Button btnAlgEvaluate = null;
	Button btnAlgReset = null;
	Table tblAlgOutputDeskpei = null;
	Composite comAlgOutputDeskpei = null;
	Composite comAlgOutputRoundkeys = null;
	Table tblAlgOutputRoundkeys = null;
	Table tblAlgOutputCDMatrix = null;
	Composite comAlgOutputCDMatrix = null;
	Group grpAlgHeader = null;
	Table tblAlgOutputM0M17 = null;
	Composite comAlgInputBtn = null;
	Label lblAlgInputSeparator = null;
	Label lblAlgResult = null;
	StyledText txtAlgResult = null;
	Group grpAlgResult = null;
	Composite comAlgResult = null;
	Label lblAlgInputManualCurChar = null;
	Label lblAlgInputDataCurChar = null;
	Table tblAlgOutputHamming1 = null;
	Table tblAlgOutputHamming2 = null;
	Composite comAlgOutputHamming = null;
	StyledText txtAlgInformation = null;

	// FPoints Study
	TabItem tabFPoints = null;
	Composite comFPoints = null;
	Composite comFPointsHeader = null;
	Label lblFPoints = null;
	Label lblFPointsTitle = null;
	Label lblFPointsInformationText = null;
	SashForm sashFPointsDivideMain = null;
	Composite comFPointsMain = null;
	Composite comFPointsMainDown = null;
	Composite comFPointsMainLeft = null;
	Composite comFPointsMainRight = null;
	Composite comFPointsMainRightUp = null;
	Composite comFPointsMainRightDown = null;
	Group grpFPointsInput = null;
	Group grpFPointsOutput = null;
	Group grpFPointsInformation = null;
	Group grpFPointsStatus = null;
	StyledText txtFPointsStatus = null;
	Button btnFPointsEvaluate = null;
	Button btnFPointsReset = null;
	Composite comFPointsInputKey = null;
	Composite comFPointsInputTarget = null;
	Label lblFPointsInputTarget = null;
	Button btnFPointsFixedpoint = null;
	Button btnFPointsAntifixedPoint = null;
	Label lblFPointsInputKey = null;
	Button btnFPointsK0 = null;
	Button btnFPointsK1 = null;
	Button btnFPointsK2 = null;
	Button btnFPointsK3 = null;
	Composite comFPointsInputM8 = null;
	Label lblFPointsInputData = null;
	Text txtFPointsInputM8 = null;
	Label lblFPointsInputM8 = null;
	Label lblFPointsInputSeparator = null;
	Composite comFPointsInputBtn = null;
	Group grpFPointsResult = null;
	Composite comFPointsResult = null;
	Label lblFPointsResult = null;
	StyledText txtFPointsResult = null;
	Group grpFPointsHeader = null;
	Table tblFPointsOutputAFP = null;
	Composite comFPointsOutput = null;
	StyledText txtFPointsInformation = null;

	// SBox Study
	TabItem tabSBox = null;
	Composite comSBox = null;
	Composite comSBoxHeader = null;
	Label lblSBox = null;
	Label lblSBoxTitle = null;
	Label lblSBoxInformationText = null;
	SashForm sashSBoxDivideMain = null;
	Composite comSBoxMain = null;
	Composite comSBoxMainDown = null;
	Composite comSBoxMainLeft = null;
	Composite comSBoxMainRight = null;
	Composite comSBoxMainRightUp = null;
	Composite comSBoxMainRightDown = null;
	Group grpSBoxInput = null;
	Group grpSBoxOutput = null;
	Group grpSBoxInformation = null;
	Group grpSBoxStatus = null;
	StyledText txtSBoxStatus = null;
	Composite comSBoxInput = null;
	Label lblSBoxInputData = null;
	Label lblSBoxInputDeltap = null;
	Label lblSBoxInputDeltapCur = null;
	Text txtSBoxInputDeltap = null;
	Button btnSBoxEvaluate = null;
	Button btnSBoxReset = null;
	Button btnSBoxEvaluateSeries = null;
	Table tblSBoxOutput = null;
	Group grpSBoxResult = null;
	Composite comSBoxInputDataSeries = null;
	Slider slSBoxInputSeriesCount = null;
	Slider slSBoxInputSeriesTime = null;
	Label lblSBoxInputSeries = null;
	Label lblSBoxInputSeriesTime = null;
	Label lblSBoxInputSeriesCount = null;
	Composite comSBoxInputBtn = null;
	Label lblSBoxInputSeparator = null;
	int intSBoxOutputCurStep = 0;
	Label lblSBoxOutputCurStep = null;
	Label lblSBoxOutputP = null;
	Label lblSBoxOutputK = null;
	Group grpSBoxHeader = null;
	Label lblFPointsInputM8cur = null;
	StyledText txtSBoxInformation = null;
	private Composite parent;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		// Wrapper Start
		wrapper = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		wrapper.setExpandHorizontal(true);
		wrapper.setExpandVertical(true);
		wrapper.setMinHeight(600);
		wrapper.setMinWidth(1325);
		wrapper.setLayout(new GridLayout());

		// Folder
		tfolder_1 = new TabFolder(wrapper, SWT.TOP);
		tfolder_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createAlgTab();
		createFPointsTab();
		createSBoxTab();

		makeArrays1();

		wrapper.setContent(tfolder_1);

		// Create DES Controller
		dESCon = new DESController();

		txtAlgInformation.setText(Messages.DesView_63 + Messages.DesView_70 + Messages.DesView_80);
	}

	@Override
	public void setFocus() {
		wrapper.setFocus();
	}

	/**
	 * Creates the "Algorithm Study" tab and all its GUI elements
	 */
	private void createAlgTab() {
		tabAlg = new TabItem(tfolder_1, SWT.NONE);
		tabAlg.setText(Messages.DesView_0);

		// Tab base composite
		comAlg = new Composite(tfolder_1, SWT.NONE);
		comAlg.setLayout(new FillLayout());

		comAlgMain = new Composite(comAlg, SWT.NONE);
		comAlgMain.setLayout(new GridLayout(2, false));

		// title
		lblAlgTitle = new Label(comAlgMain, SWT.NONE);
		lblAlgTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblAlgTitle.setFont(FontService.getHeaderFont());
		lblAlgTitle.setText(Messages.DesView_title);

		// title description
		lblAlgInformationText = new Text(comAlgMain, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		lblAlgInformationText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblAlgInformationText.setText(Messages.DesView_text);

		// slider between left infobox and right content
		sashAlgDivideMain = new SashForm(comAlgMain, SWT.HORIZONTAL);
		sashAlgDivideMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		sashAlgDivideMain.setLayout(new FillLayout());

		comAlgMainLeft = new Composite(sashAlgDivideMain, SWT.NONE);
		comAlgMainLeft.setLayout(new GridLayout(1, false));

		comAlgMainRight = new Composite(sashAlgDivideMain, SWT.NONE);
		comAlgMainRight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comAlgMainRight.setLayout(new GridLayout(1, false));

		sashAlgDivideMain.setWeights(new int[] { 23, 77 });
		sashAlgDivideMain.setSashWidth(10);

		comAlgMainRightUp = new Composite(comAlgMainRight, SWT.NONE);
		comAlgMainRightUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		comAlgMainRightUp.setLayout(new GridLayout(4, false));

		// Group holding Input Tab -> parameter/start buttons
		grpAlgInput = new Group(comAlgMainRightUp, SWT.NONE);
		grpAlgInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		grpAlgInput.setText(Messages.DesView_1);
		grpAlgInput.setLayout(new GridLayout(4, false));

		grpAlgInformation = new Group(comAlgMainLeft, SWT.NONE);
		grpAlgInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpAlgInformation.setText(Messages.DesView_3);
		grpAlgInformation.setLayout(new GridLayout(1, false));

		// infobox
		txtAlgInformation = new StyledText(grpAlgInformation, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txtAlgInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtAlgInformation.setEditable(false);
		txtAlgInformation.setCaret(null);
		txtAlgInformation.setAlwaysShowScrollBars(false);
		txtAlgInformation.setFont(FontService.getNormalFont());

		// Input Tab composite
		comAlgInputMode = new Composite(grpAlgInput, SWT.NONE);
		comAlgInputMode.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comAlgInputMode.setLayout(new GridLayout(1, false));

		// Mode heading
		lblAlgMode = new Label(comAlgInputMode, SWT.NONE);
		lblAlgMode.setFont(FontService.getLargeBoldFont());
		lblAlgMode.setText(Messages.DesView_11);

		/*
		 * Radio buttons
		 */
		btnAlgEncrypt = new Button(comAlgInputMode, SWT.RADIO);
		btnAlgEncrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAlgEncrypt.setText(Messages.DesView_8);
		btnAlgEncrypt.setSelection(true);

		btnAlgEncrypt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lblAlgInputPlaintext.setText(Messages.DesView_61);
				lblAlgResult.setText(Messages.DesView_71);
				txtAlgResult.setText(Messages.DesView_109);
				comAlgMainRightUp.layout();
			}

		});

		btnAlgDecrypt = new Button(comAlgInputMode, SWT.RADIO);
		btnAlgDecrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAlgDecrypt.setText(Messages.DesView_5);

		btnAlgDecrypt.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				lblAlgInputPlaintext.setText(Messages.DesView_62);
				lblAlgResult.setText(Messages.DesView_81);
				txtAlgResult.setText(Messages.DesView_109);
				comAlgMainRightUp.layout();
			}

		});

		// key selection composite
		comAlgInputKey = new Composite(grpAlgInput, SWT.NONE);
		comAlgInputKey.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comAlgInputKey.setLayout(new GridLayout(6, false));

		// key selection heading
		lblAlgInputKey = new Label(comAlgInputKey, SWT.NONE);
		lblAlgInputKey.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 6, 1));
		lblAlgInputKey.setFont(FontService.getLargeBoldFont());
		lblAlgInputKey.setText(Messages.DesView_13);

		/*
		 * Key selection radio buttons
		 */
		btnAlgK0 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK0.setText(Messages.DesView_12);
		btnAlgK0.setSelection(true);

		btnAlgK3 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK3.setText(Messages.DesView_14);

		btnAlgK5 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK5.setText(Messages.DesView_15);

		btnAlgK6 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK6.setText(Messages.DesView_16);

		btnAlgManual = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgManual.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnAlgManual.setText(Messages.DesView_21);

		btnAlgManual.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnAlgManual.getSelection()) {
					txtAlgKeyManual.setEnabled(true);
				} else {
					txtAlgKeyManual.setEnabled(false);
				}
			}
		});

		btnAlgK9 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK9.setText(Messages.DesView_17);

		btnAlgK10 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK10.setText(Messages.DesView_18);

		btnAlgK12 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK12.setText(Messages.DesView_19);

		btnAlgK15 = new Button(comAlgInputKey, SWT.RADIO);
		btnAlgK15.setText(Messages.DesView_20);

		txtAlgKeyManual = new Text(comAlgInputKey, SWT.BORDER);
		txtAlgKeyManual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtAlgKeyManual.setTextLimit(16);
		txtAlgKeyManual.setEnabled(false);

		txtAlgKeyManual.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				lblAlgInputManualCurChar
						.setText(Messages.DesView_23 + txtAlgKeyManual.getText().length() + Messages.DesView_24);
				if (txtAlgKeyManual.getText().length() >= 10)
					comAlgInputKey.layout();
			}

		});

		lblAlgInputManualCurChar = new Label(comAlgInputKey, SWT.NONE);
		lblAlgInputManualCurChar.setText(Messages.DesView_22);

		// Data heading composite
		comAlgInputData = new Composite(grpAlgInput, SWT.NONE);
		comAlgInputData.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comAlgInputData.setLayout(new GridLayout(2, false));

		// Data heading
		lblAlgInputData = new Label(comAlgInputData, SWT.NONE);
		lblAlgInputData.setText(Messages.DesView_25);
		lblAlgInputData.setFont(FontService.getLargeBoldFont());

		lblAlgInputPlaintext = new Label(comAlgInputData, SWT.NONE);
		lblAlgInputPlaintext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblAlgInputPlaintext.setText(Messages.DesView_61);

		txtAlgInputData = new Text(comAlgInputData, SWT.BORDER);
		txtAlgInputData.setTextLimit(16);
		txtAlgInputData.setText(Messages.DesView_26);

		txtAlgInputData.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				lblAlgInputDataCurChar
						.setText(Messages.DesView_27 + txtAlgInputData.getText().length() + Messages.DesView_28);
			}

		});

		lblAlgInputDataCurChar = new Label(comAlgInputData, SWT.NONE);
		lblAlgInputDataCurChar.setText(Messages.DesView_29);

		// Button composite (also holding separator)
		comAlgInputBtn = new Composite(grpAlgInput, SWT.NONE);
		comAlgInputBtn.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		comAlgInputBtn.setLayout(new GridLayout(3, false));

		// Separator label
		lblAlgInputSeparator = new Label(comAlgInputBtn, SWT.SEPARATOR | SWT.VERTICAL);
		lblAlgInputSeparator.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 2));

		/*
		 * Action Buttons (Evaluate and Reset)
		 */
		btnAlgEvaluate = new Button(comAlgInputBtn, SWT.NONE);
		btnAlgEvaluate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnAlgEvaluate.setText(Messages.DesView_107);
		btnAlgEvaluate.setFont(FontService.getLargeBoldFont());

		btnAlgEvaluate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String sKey = Messages.DesView_108;
				String sMode = Messages.DesView_109;
				String err = Messages.DesView_110;

				dESCon.Alg_In_Data = txtAlgInputData.getText().toUpperCase();
				if (btnAlgEncrypt.getSelection()) {
					dESCon.Alg_In_Mode = 0;
					sMode = Messages.DesView_111;
				} else {
					dESCon.Alg_In_Mode = 1;
					sMode = Messages.DesView_112;
				}
				if (btnAlgK0.getSelection()) {
					dESCon.Alg_In_selectedKey = 0;
					sKey = Messages.DesView_113;
				} else if (btnAlgK3.getSelection()) {
					dESCon.Alg_In_selectedKey = 3;
					sKey = Messages.DesView_114;
				} else if (btnAlgK5.getSelection()) {
					dESCon.Alg_In_selectedKey = 5;
					sKey = Messages.DesView_115;
				} else if (btnAlgK6.getSelection()) {
					dESCon.Alg_In_selectedKey = 6;
					sKey = Messages.DesView_116;
				} else if (btnAlgK9.getSelection()) {
					dESCon.Alg_In_selectedKey = 9;
					sKey = Messages.DesView_117;
				} else if (btnAlgK10.getSelection()) {
					dESCon.Alg_In_selectedKey = 10;
					sKey = Messages.DesView_118;
				} else if (btnAlgK12.getSelection()) {
					dESCon.Alg_In_selectedKey = 12;
					sKey = Messages.DesView_119;
				} else if (btnAlgK15.getSelection()) {
					dESCon.Alg_In_selectedKey = 15;
					sKey = Messages.DesView_120;
				} else if (btnAlgManual.getSelection()) {
					dESCon.Alg_In_selectedKey = 16;
					dESCon.Alg_In_manualKey = txtAlgKeyManual.getText().toUpperCase();
					sKey = Messages.DesView_121 + txtAlgKeyManual.getText() + Messages.DesView_122;
				}

				if (dESCon.algorithmStudy() == 0) {
					if (btnAlgEncrypt.getSelection()) {
						txtAlgResult.setText(dESCon.Alg_Out_EncDecResult);
					} else {
						txtAlgResult.setText(dESCon.Alg_Out_EncDecResult);
					}
					fillTable(tblAlgOutputM0M17, 0, 17, 1, 32, dESCon.Alg_Out_M0M17);
					colorTable(tblAlgOutputM0M17, 1);
					for (int i = 1; i < tblAlgOutputM0M17.getItemCount(); i++) {
						tblAlgOutputM0M17.getItem(i).setText(tblAlgOutputM0M17.getColumnCount() - 1,
								Integer.toString(dESCon.Alg_Out_M0M17_Dist[i]));
					}

					fillTable(tblAlgOutputDeskpei, 0, 64, 1, 64, dESCon.Alg_Out_cipherMatrix);
					// doColoring(0, 64, 1, 64, output2, true, 3,
					// DES_C.DES_action_type);
					for (int i = 1; i < tblAlgOutputDeskpei.getItemCount(); i++) {
						tblAlgOutputDeskpei.getItem(i).setText(tblAlgOutputDeskpei.getColumnCount() - 1,
								Integer.toString(dESCon.Alg_Out_cipherMatrix_Dist[i]));
					}
					colorTable(tblAlgOutputDeskpei, 1);

					fillTable(tblAlgOutputRoundkeys, 0, 15, 1, 48, dESCon.Alg_Out_Roundkeys);

					fillTable(tblAlgOutputCDMatrix, 0, 33, 1, 28, dESCon.Alg_Out_CDMatrix);
					colorTable(tblAlgOutputCDMatrix, 3);

					fillTable(tblAlgOutputHamming1, 0, 7, 0, 7, dESCon.Alg_Out_DistMatrix1);
					fillTable(tblAlgOutputHamming2, 0, 7, 0, 7, dESCon.Alg_Out_DistMatrix2);

					txtAlgStatus.append(Messages.DesView_125 + getCurrentTime() + Messages.DesView_126 + sMode
							+ Messages.DesView_127 + sKey + Messages.DesView_128
							+ txtAlgInputData.getText().toUpperCase());
					txtAlgStatus.setTopIndex(txtAlgStatus.getLineCount() - 1);
				} else {
					for (int i = 0; i < dESCon.errMsg.length; i++) {
						if (i != dESCon.errMsg.length - 1) {
							err += dESCon.errMsg[i] + Messages.DesView_129;
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtAlgStatus.append(Messages.DesView_130 + getCurrentTime() + Messages.DesView_131 + err);
					txtAlgStatus.setTopIndex(txtAlgStatus.getLineCount() - 1);
				}
			}
		});

		btnAlgReset = new Button(comAlgInputBtn, SWT.NONE);
		btnAlgReset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnAlgReset.setText(Messages.DesView_87);

		btnAlgReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btnAlgK0.setSelection(true);
				btnAlgK3.setSelection(false);
				btnAlgK5.setSelection(false);
				btnAlgK6.setSelection(false);
				btnAlgK9.setSelection(false);
				btnAlgK10.setSelection(false);
				btnAlgK12.setSelection(false);
				btnAlgK15.setSelection(false);
				btnAlgManual.setSelection(false);
				txtAlgKeyManual.setText(Messages.DesView_88);
				txtAlgKeyManual.setEnabled(false);
				txtAlgInputData.setText(Messages.DesView_232);
				txtAlgResult.setText(Messages.DesView_88);
				// lblAlgInputCipherOut.setText(Messages.DesView_91);
				lblAlgInputManualCurChar.setText(Messages.DesView_92);
				lblAlgInputDataCurChar.setText(Messages.DesView_93);
				cleanTable(tblAlgOutputDeskpei);
				cleanTable(tblAlgOutputRoundkeys);
				cleanTable(tblAlgOutputCDMatrix);
				cleanTable(tblAlgOutputM0M17);
				cleanTable(tblAlgOutputHamming1);
				cleanTable(tblAlgOutputHamming2);
				TableItem[] tblItems = tblAlgOutputM0M17.getItems();
				for (int i = 0; i < 18; i++) {
					tblItems[i].setText(0, Messages.DesView_94 + (i) + Messages.DesView_95);
				}
				tblItems = tblAlgOutputDeskpei.getItems();
				tblItems[0].setText(0, Messages.DesView_96);
				for (int i = 1; i < 65; i++) {
					tblItems[i].setText(0, Messages.DesView_97 + i + Messages.DesView_98);
				}
				tblItems = tblAlgOutputRoundkeys.getItems();
				for (int i = 0; i < 16; i++) {
					tblItems[i].setText(0, Messages.DesView_99 + (i + 1) + Messages.DesView_100);
				}
				tblItems = tblAlgOutputCDMatrix.getItems();
				for (int i = 0, j = 0; i < 34; i = i + 2, j++) {
					tblItems[i].setText(0, Messages.DesView_101 + j + Messages.DesView_102);
					tblItems[i + 1].setText(0, Messages.DesView_103 + j + Messages.DesView_104);
				}

				txtAlgStatus.append(Messages.DesView_105 + getCurrentTime() + Messages.DesView_106);
				txtAlgStatus.setTopIndex(txtAlgStatus.getLineCount() - 1);

				if (btnAlgEncrypt.getSelection()) {
					lblAlgInputPlaintext.setText(Messages.DesView_61);
				} else {
					lblAlgInputPlaintext.setText(Messages.DesView_62);
				}

			}
		});

		// Result group
		grpAlgResult = new Group(comAlgMainRightUp, SWT.NONE);
		grpAlgResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpAlgResult.setText(Messages.DesView_60);
		grpAlgResult.setLayout(new GridLayout(1, true));

		// pointless composite, there to keep the spacing aligned with the Input Tab
		comAlgResult = new Composite(grpAlgResult, SWT.NONE);
		comAlgResult.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		GridLayout comAlgResultLayout = new GridLayout(1, true);
		comAlgResultLayout.verticalSpacing = 20;
		comAlgResult.setLayout(comAlgResultLayout);

		// Heading
		lblAlgResult = new Label(comAlgResult, SWT.NONE);
		lblAlgResult.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblAlgResult.setFont(FontService.getLargeBoldFont());
		lblAlgResult.setText(Messages.DesView_71);

		// Encryption/Decryption Result
		txtAlgResult = new StyledText(comAlgResult, SWT.WRAP);
		txtAlgResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		txtAlgResult.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlgResult.setEditable(false);
		txtAlgResult.setCaret(null);
		txtAlgResult.setAlignment(SWT.CENTER);

		comAlgMainRightDown = new Composite(comAlgMainRight, SWT.NONE);
		comAlgMainRightDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comAlgMainRightDown.setLayout(new GridLayout(1, false));

		// Internal States Group
		grpAlgOutput = new Group(comAlgMainRightDown, SWT.NONE);
		grpAlgOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpAlgOutput.setText(Messages.DesView_2);
		grpAlgOutput.setLayout(new GridLayout(1, false));

		// Tabfolder for various tables
		tfAlgOutput = new TabFolder(grpAlgOutput, SWT.NONE);
		tfAlgOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tfAlgOutput.setLayout(new GridLayout(1, true));

		tbtmAlgM0M17 = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgM0M17.setText(Messages.DesView_32);

		Composite comAlgOutputM0M17 = new Composite(tfAlgOutput, SWT.NONE);
		tbtmAlgM0M17.setControl(comAlgOutputM0M17);
		comAlgOutputM0M17.setLayout(new GridLayout(1, true));
		comAlgOutputM0M17.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tblAlgOutputM0M17 = new Table(comAlgOutputM0M17, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL);
		tblAlgOutputM0M17.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tblAlgOutputM0M17.setHeaderVisible(true);
		tblAlgOutputM0M17.setLinesVisible(true);

		cleanTable(tblAlgOutputM0M17);

		tbtmAlgDES = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgDES.setText(Messages.DesView_37);

		comAlgOutputDeskpei = new Composite(tfAlgOutput, SWT.NONE);
		tbtmAlgDES.setControl(comAlgOutputDeskpei);
		comAlgOutputDeskpei.setLayout(new FormLayout());

		tblAlgOutputDeskpei = new Table(comAlgOutputDeskpei, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		FormData fd_tblAlgOutputDeskpei = new FormData();
		fd_tblAlgOutputDeskpei.left = new FormAttachment(0, 0);
		fd_tblAlgOutputDeskpei.right = new FormAttachment(100, 0);
		fd_tblAlgOutputDeskpei.top = new FormAttachment(0, 0);
		fd_tblAlgOutputDeskpei.bottom = new FormAttachment(100, 0);
		tblAlgOutputDeskpei.setLayoutData(fd_tblAlgOutputDeskpei);
		tblAlgOutputDeskpei.setHeaderVisible(true);
		tblAlgOutputDeskpei.setLinesVisible(true);

		cleanTable(tblAlgOutputDeskpei);

		// Create Hamming Tab
		tbtmAlgHamming = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgHamming.setText(Messages.DesView_43);

		comAlgOutputHamming = new Composite(tfAlgOutput, SWT.NONE);
		tbtmAlgHamming.setControl(comAlgOutputHamming);

		Label lblAlgOutputHamming1 = new Label(comAlgOutputHamming, SWT.NONE);
		lblAlgOutputHamming1.setBounds(130, 20, 100, 20);
		lblAlgOutputHamming1.setText(Messages.DesView_44);

		Composite t1 = new Composite(comAlgOutputHamming, SWT.BORDER);
		t1.setBounds(30, 50, 225, 161);

		tblAlgOutputHamming1 = new Table(t1, SWT.NONE);
		tblAlgOutputHamming1.setHeaderVisible(false);
		tblAlgOutputHamming1.setLinesVisible(true);
		tblAlgOutputHamming1.setBounds(0, 0, 224, 160);
		cleanTable(tblAlgOutputHamming1);

		Label lblAlgOutputHamming2 = new Label(comAlgOutputHamming, SWT.NONE);
		lblAlgOutputHamming2.setBounds(400, 20, 100, 20);
		lblAlgOutputHamming2.setText(Messages.DesView_45);

		Composite t2 = new Composite(comAlgOutputHamming, SWT.BORDER);
		t2.setBounds(300, 50, 225, 161);

		tblAlgOutputHamming2 = new Table(t2, SWT.NONE);
		tblAlgOutputHamming2.setHeaderVisible(false);
		tblAlgOutputHamming2.setLinesVisible(true);
		tblAlgOutputHamming2.setBounds(0, 0, 224, 160);
		cleanTable(tblAlgOutputHamming2);

		Label tblAlgOutputHammingDes = new Label(comAlgOutputHamming, SWT.NONE);
		tblAlgOutputHammingDes.setBounds(30, 230, 700, 80);
		tblAlgOutputHammingDes.setText(Messages.DesView_46 + Messages.DesView_47 + Messages.DesView_48
				+ Messages.DesView_49 + Messages.DesView_50);

		// Roundkeys
		tbtmAlgRoundkeys = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgRoundkeys.setText(Messages.DesView_51);

		comAlgOutputRoundkeys = new Composite(tfAlgOutput, SWT.NONE);
		tbtmAlgRoundkeys.setControl(comAlgOutputRoundkeys);
		comAlgOutputRoundkeys.setLayout(new FormLayout());

		tblAlgOutputRoundkeys = new Table(comAlgOutputRoundkeys, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		FormData fd_tblAlgOutputRoundkeys = new FormData();
		fd_tblAlgOutputRoundkeys.left = new FormAttachment(0, 0);
		fd_tblAlgOutputRoundkeys.right = new FormAttachment(100, 0);
		fd_tblAlgOutputRoundkeys.top = new FormAttachment(0, 0);
		fd_tblAlgOutputRoundkeys.bottom = new FormAttachment(100, 0);
		tblAlgOutputRoundkeys.setLayoutData(fd_tblAlgOutputRoundkeys);
		tblAlgOutputRoundkeys.setHeaderVisible(true);
		tblAlgOutputRoundkeys.setLinesVisible(true);
		cleanTable(tblAlgOutputRoundkeys);

		tbtmAlgCDMatrix = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgCDMatrix.setText(Messages.DesView_55);

		comAlgOutputCDMatrix = new Composite(tfAlgOutput, SWT.NONE);
		tbtmAlgCDMatrix.setControl(comAlgOutputCDMatrix);
		comAlgOutputCDMatrix.setLayout(new FormLayout());
		tblAlgOutputCDMatrix = new Table(comAlgOutputCDMatrix, SWT.V_SCROLL | SWT.H_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		FormData fd_tblAlgOutputCDMatrix = new FormData();
		fd_tblAlgOutputCDMatrix.left = new FormAttachment(0, 0);
		fd_tblAlgOutputCDMatrix.right = new FormAttachment(0, 675);
		fd_tblAlgOutputCDMatrix.top = new FormAttachment(0, 0);
		fd_tblAlgOutputCDMatrix.bottom = new FormAttachment(100, 0);
		tblAlgOutputCDMatrix.setLayoutData(fd_tblAlgOutputCDMatrix);
		tblAlgOutputCDMatrix.setLinesVisible(true);

		cleanTable(tblAlgOutputCDMatrix);

		comAlgMainDown = new Composite(comAlgMain, SWT.NONE);
		GridData comAlgMainDownLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		comAlgMainDownLayout.heightHint = 130;
		comAlgMainDown.setLayoutData(comAlgMainDownLayout);
		comAlgMainDown.setLayout(new GridLayout(1, false));

		grpAlgStatus = new Group(comAlgMainDown, SWT.NONE);
		grpAlgStatus.setText(Messages.DesView_4);
		grpAlgStatus.setLayout(new FormLayout());
		grpAlgStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		txtAlgStatus = new StyledText(grpAlgStatus, SWT.MULTI | SWT.V_SCROLL);
		txtAlgStatus.setEditable(false);
		txtAlgStatus.setCaret(null);
		txtAlgStatus.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlgStatus.setAlwaysShowScrollBars(false);
		FormData fd_txtAlgStatus = new FormData();
		fd_txtAlgStatus.bottom = new FormAttachment(100, -10);
		fd_txtAlgStatus.right = new FormAttachment(100, -10);
		fd_txtAlgStatus.top = new FormAttachment(0, 10);
		fd_txtAlgStatus.left = new FormAttachment(0, 10);
		txtAlgStatus.setLayoutData(fd_txtAlgStatus);

		tabAlg.setControl(comAlg);
	}

	private void makeArrays1() {
		TableColumn[] tblAlgOutputM0M17Column = new TableColumn[34];
		for (int i = 1; i < 33; i++) {
			tblAlgOutputM0M17Column[i] = new TableColumn(tblAlgOutputM0M17, SWT.NONE);
			tblAlgOutputM0M17Column[i].setWidth(25);
			tblAlgOutputM0M17Column[i].setAlignment(SWT.CENTER);
			tblAlgOutputM0M17Column[i].setText(i + Messages.DesView_33);

		}
		tblAlgOutputM0M17Column[0] = new TableColumn(tblAlgOutputM0M17, SWT.NONE);
		tblAlgOutputM0M17Column[33] = new TableColumn(tblAlgOutputM0M17, SWT.NONE);
		tblAlgOutputM0M17Column[33].setWidth(70);
		tblAlgOutputM0M17Column[33].setAlignment(SWT.CENTER);
		tblAlgOutputM0M17Column[33].setText(Messages.DesView_34);

		tblAlgOutputM0M17Column[0].setWidth(45);
		tblAlgOutputM0M17Column[0].setAlignment(SWT.CENTER);

		TableColumn[] tblAlgOutputDESColumn = new TableColumn[66];
		tblAlgOutputDESColumn[0] = new TableColumn(tblAlgOutputDeskpei, SWT.NONE);
		tblAlgOutputDESColumn[65] = new TableColumn(tblAlgOutputDeskpei, SWT.NONE);
		tblAlgOutputDESColumn[0].setWidth(70);
		tblAlgOutputDESColumn[0].setAlignment(SWT.CENTER);
		for (int i = 1; i < 65; i++) {
			tblAlgOutputDESColumn[i] = new TableColumn(tblAlgOutputDeskpei, SWT.NONE);
			tblAlgOutputDESColumn[i].setWidth(25);
			tblAlgOutputDESColumn[i].setAlignment(SWT.CENTER);
			tblAlgOutputDESColumn[i].setText(i + Messages.DesView_38);

		}
		tblAlgOutputDESColumn[65].setWidth(50);
		tblAlgOutputDESColumn[65].setAlignment(SWT.CENTER);
		tblAlgOutputDESColumn[65].setText(Messages.DesView_39);

		TableColumn[] tblAlgOutputRoundkeysColumn = new TableColumn[49];
		tblAlgOutputRoundkeysColumn[0] = new TableColumn(tblAlgOutputRoundkeys, SWT.NONE);
		tblAlgOutputRoundkeysColumn[0].setWidth(40);
		tblAlgOutputRoundkeysColumn[0].setAlignment(SWT.CENTER);
		for (int i = 1; i < 49; i++) {
			tblAlgOutputRoundkeysColumn[i] = new TableColumn(tblAlgOutputRoundkeys, SWT.NONE);
			tblAlgOutputRoundkeysColumn[i].setWidth(25);
			tblAlgOutputRoundkeysColumn[i].setAlignment(SWT.CENTER);
			tblAlgOutputRoundkeysColumn[i].setText(i + Messages.DesView_52);

		}

		TableColumn[] tblAlgOutputCDMatrixColumn = new TableColumn[29];

		TableItem[] tblAlgM0M17Item = new TableItem[18];
		for (int i = 0; i < 18; i++) {
			tblAlgM0M17Item[i] = new TableItem(tblAlgOutputM0M17, SWT.BORDER);
		}
		for (int i = 0; i < 18; i++) {
			tblAlgM0M17Item[i].setText(0, Messages.DesView_35 + (i) + Messages.DesView_36);
		}

		TableItem[] tblAlgDESItem = new TableItem[65];
		for (int i = 0; i < 65; i++) {
			tblAlgDESItem[i] = new TableItem(tblAlgOutputDeskpei, SWT.BORDER);
		}
		tblAlgDESItem[0].setText(0, Messages.DesView_40);
		for (int i = 1; i < 65; i++) {
			tblAlgDESItem[i].setText(0, Messages.DesView_41 + i + Messages.DesView_42);
		}

		TableColumn[] tblAlgOutputHamming1Column = new TableColumn[8];
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming1Column[i] = new TableColumn(tblAlgOutputHamming1, SWT.NONE);
			tblAlgOutputHamming1Column[i].setWidth(28);
			tblAlgOutputHamming1Column[i].setAlignment(SWT.CENTER);
		}
		TableItem[] tblAlgOutputHamming1TableItem = new TableItem[8];
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming1TableItem[i] = new TableItem(tblAlgOutputHamming1, SWT.BORDER);
		}

		TableColumn[] tblAlgOutputHamming2Column = new TableColumn[8];
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming2Column[i] = new TableColumn(tblAlgOutputHamming2, SWT.NONE);
			tblAlgOutputHamming2Column[i].setWidth(28);
			tblAlgOutputHamming2Column[i].setAlignment(SWT.CENTER);
		}
		TableItem[] tblAlgOutputHamming2TableItem = new TableItem[8];
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming2TableItem[i] = new TableItem(tblAlgOutputHamming2, SWT.BORDER);
		}

		TableItem[] tblAlgRoundkeysItem = new TableItem[16];
		for (int i = 0; i < 16; i++) {
			tblAlgRoundkeysItem[i] = new TableItem(tblAlgOutputRoundkeys, SWT.BORDER);
		}
		for (int i = 0; i < 16; i++) {
			tblAlgRoundkeysItem[i].setText(0, Messages.DesView_53 + (i + 1) + Messages.DesView_54);
		}

		tblAlgOutputCDMatrixColumn[0] = new TableColumn(tblAlgOutputCDMatrix, SWT.NONE);
		tblAlgOutputCDMatrixColumn[0].setWidth(40);
		tblAlgOutputCDMatrixColumn[0].setAlignment(SWT.CENTER);
		for (int i = 1; i < 29; i++) {
			tblAlgOutputCDMatrixColumn[i] = new TableColumn(tblAlgOutputCDMatrix, SWT.NONE);
			tblAlgOutputCDMatrixColumn[i].setWidth(22);
			tblAlgOutputCDMatrixColumn[i].setAlignment(SWT.CENTER);
		}

		TableItem[] tblAlgCDMatrixItem = new TableItem[34];
		for (int i = 0; i < 34; i++) {
			tblAlgCDMatrixItem[i] = new TableItem(tblAlgOutputCDMatrix, SWT.BORDER);
		}
		for (int i = 0, j = 0; i < 34; i = i + 2, j++) {
			tblAlgCDMatrixItem[i].setText(0, Messages.DesView_56 + j + Messages.DesView_57);
			tblAlgCDMatrixItem[i + 1].setText(0, Messages.DesView_58 + j + Messages.DesView_59);
		}
	}

	/**
	 * Creates the "Anti- /Fixed Point Study" tab and all its GUI elements
	 */
	private void createFPointsTab() {
		tabFPoints = new TabItem(tfolder_1, SWT.NONE);
		tabFPoints.setText(Messages.DesView_132);

		// Main composite of "Anti-/Fixed Point Study
		comFPoints = new Composite(tfolder_1, SWT.NONE);
		comFPoints.setLayout(new FillLayout());

		comFPointsMain = new Composite(comFPoints, SWT.NONE);
		comFPointsMain.setLayout(new GridLayout(2, false));

		// title
		lblFPointsTitle = new Label(comFPointsMain, SWT.NONE);
		lblFPointsTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblFPointsTitle.setFont(FontService.getHeaderFont());
		lblFPointsTitle.setText(Messages.DesView_title);

		// title description
		lblFPointsInformationText = new Label(comFPointsMain, SWT.NONE);
		lblFPointsInformationText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblFPointsInformationText.setText(Messages.DesView_text);

		// Slider between left infobox and right content
		sashFPointsDivideMain = new SashForm(comFPointsMain, SWT.HORIZONTAL);
		sashFPointsDivideMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		sashFPointsDivideMain.setLayout(new FillLayout());

		comFPointsMainLeft = new Composite(sashFPointsDivideMain, SWT.NONE);
		comFPointsMainLeft.setLayout(new GridLayout(1, false));

		comFPointsMainRight = new Composite(sashFPointsDivideMain, SWT.NONE);
		comFPointsMainRight.setLayout(new GridLayout(1, false));

		sashFPointsDivideMain.setWeights(new int[] { 23, 77 });
		sashFPointsDivideMain.setSashWidth(10);

		// Composite holding Result group
		comFPointsMainDown = new Composite(comFPointsMain, SWT.NONE);
		GridData comFPointsMainDownLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		comFPointsMainDownLayout.heightHint = 130;
		comFPointsMainDown.setLayoutData(comFPointsMainDownLayout);
		comFPointsMainDown.setLayout(new GridLayout(1, false));

		comFPointsMainRightUp = new Composite(comFPointsMainRight, SWT.NONE);
		comFPointsMainRightUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		comFPointsMainRightUp.setLayout(new GridLayout(4, false));

		comFPointsMainRightDown = new Composite(comFPointsMainRight, SWT.NONE);
		comFPointsMainRightDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comFPointsMainRightDown.setLayout(new GridLayout(1, false));

		// Group holding Input Tab -> parameter and start buttons
		grpFPointsInput = new Group(comFPointsMainRightUp, SWT.NONE);
		grpFPointsInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));
		grpFPointsInput.setLayout(new GridLayout(4, false));
		grpFPointsInput.setText(Messages.DesView_133);

		// Group holding results
		grpFPointsResult = new Group(comFPointsMainRightUp, SWT.NONE);
		grpFPointsResult.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1));
		grpFPointsResult.setLayout(new GridLayout(1, false));
		grpFPointsResult.setText(Messages.DesView_60);

		// Group holding the internal states tables
		grpFPointsOutput = new Group(comFPointsMainRightDown, SWT.NONE);
		grpFPointsOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpFPointsOutput.setLayout(new GridLayout(1, false));
		grpFPointsOutput.setText(Messages.DesView_134);

		// Group holding the infobox
		grpFPointsInformation = new Group(comFPointsMainLeft, SWT.NONE);
		grpFPointsInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpFPointsInformation.setLayout(new GridLayout(1, false));
		grpFPointsInformation.setText(Messages.DesView_135);

		// group holding the statusbox
		grpFPointsStatus = new Group(comFPointsMainDown, SWT.NONE);
		grpFPointsStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpFPointsStatus.setLayout(new FormLayout());
		grpFPointsStatus.setText(Messages.DesView_136);

		// infobox
		txtFPointsInformation = new StyledText(grpFPointsInformation, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		txtFPointsInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtFPointsInformation.setFont(FontService.getNormalFont());
		txtFPointsInformation.setEditable(false);
		txtFPointsInformation.setCaret(null);
		txtFPointsInformation.setAlwaysShowScrollBars(false);

		txtFPointsInformation.setText(Messages.DesView_166 + Messages.DesView_167 + Messages.DesView_168
				+ Messages.DesView_170 + Messages.DesView_171 + Messages.DesView_172 + Messages.DesView_173
				+ Messages.DesView_174 + Messages.DesView_175 + Messages.DesView_176 + Messages.DesView_177
				+ Messages.DesView_178 + Messages.DesView_179 + Messages.DesView_180 + Messages.DesView_181
				+ Messages.DesView_182 + Messages.DesView_183 + Messages.DesView_184 + Messages.DesView_185
				+ Messages.DesView_186 + Messages.DesView_187 + Messages.DesView_188 + Messages.DesView_189
				+ Messages.DesView_190 + Messages.DesView_191 + Messages.DesView_192 + Messages.DesView_193
				+ Messages.DesView_194 + Messages.DesView_195);

		// statusbox
		txtFPointsStatus = new StyledText(grpFPointsStatus, SWT.MULTI | SWT.V_SCROLL);
		txtFPointsStatus.setEditable(false);
		txtFPointsStatus.setCaret(null);
		txtFPointsStatus.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtFPointsStatus.setAlwaysShowScrollBars(false);
		FormData fd_txtFPointsStatus = new FormData();
		fd_txtFPointsStatus.bottom = new FormAttachment(100, -10);
		fd_txtFPointsStatus.right = new FormAttachment(100, -10);
		fd_txtFPointsStatus.top = new FormAttachment(0, 10);
		fd_txtFPointsStatus.left = new FormAttachment(0, 10);
		txtFPointsStatus.setLayoutData(fd_txtFPointsStatus);

		// Input Tab composite
		comFPointsInputTarget = new Composite(grpFPointsInput, SWT.NONE);
		comFPointsInputTarget.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comFPointsInputTarget.setLayout(new GridLayout(1, false));

		// Target heading
		lblFPointsInputTarget = new Label(comFPointsInputTarget, SWT.NONE);
		lblFPointsInputTarget.setText(Messages.DesView_137);
		lblFPointsInputTarget.setFont(FontService.getLargeBoldFont());

		/*
		 * Radio buttons
		 */
		btnFPointsFixedpoint = new Button(comFPointsInputTarget, SWT.RADIO);
		btnFPointsFixedpoint.setText(Messages.DesView_138);
		btnFPointsFixedpoint.setSelection(true);

		btnFPointsFixedpoint.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btnFPointsK0.setText(Messages.DesView_139);
				btnFPointsK1.setText(Messages.DesView_140);
				btnFPointsK2.setText(Messages.DesView_141);
				btnFPointsK3.setText(Messages.DesView_142);
				lblFPointsResult.setText(Messages.DesView_143);
				txtFPointsResult.setText(Messages.DesView_109);

			}
		});

		btnFPointsAntifixedPoint = new Button(comFPointsInputTarget, SWT.RADIO);
		btnFPointsAntifixedPoint.setText(Messages.DesView_144);

		btnFPointsAntifixedPoint.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btnFPointsK0.setText(Messages.DesView_145);
				btnFPointsK1.setText(Messages.DesView_146);
				btnFPointsK2.setText(Messages.DesView_147);
				btnFPointsK3.setText(Messages.DesView_148);
				lblFPointsResult.setText(Messages.DesView_144);
				txtFPointsResult.setText(Messages.DesView_109);

			}
		});

		// Key parameter composite
		comFPointsInputKey = new Composite(grpFPointsInput, SWT.NONE);
		comFPointsInputKey.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comFPointsInputKey.setLayout(new GridLayout(2, false));

		// Key heading
		lblFPointsInputKey = new Label(comFPointsInputKey, SWT.NONE);
		lblFPointsInputKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblFPointsInputKey.setFont(FontService.getLargeBoldFont());
		lblFPointsInputKey.setText(Messages.DesView_150);

		/*
		 * Key radio buttons
		 */
		btnFPointsK0 = new Button(comFPointsInputKey, SWT.RADIO);
		btnFPointsK0.setText(Messages.DesView_151);
		btnFPointsK0.setSelection(true);

		btnFPointsK1 = new Button(comFPointsInputKey, SWT.RADIO);
		btnFPointsK1.setText(Messages.DesView_152);

		btnFPointsK2 = new Button(comFPointsInputKey, SWT.RADIO);
		btnFPointsK2.setText(Messages.DesView_153);

		btnFPointsK3 = new Button(comFPointsInputKey, SWT.RADIO);
		btnFPointsK3.setText(Messages.DesView_154);

		// Data composite
		comFPointsInputM8 = new Composite(grpFPointsInput, SWT.NONE);
		comFPointsInputM8.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comFPointsInputM8.setLayout(new GridLayout(2, false));

		// Data heading
		lblFPointsInputData = new Label(comFPointsInputM8, SWT.NONE);
		lblFPointsInputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblFPointsInputData.setFont(FontService.getLargeBoldFont());
		lblFPointsInputData.setText(Messages.DesView_155);

		lblFPointsInputM8 = new Label(comFPointsInputM8, SWT.NONE);
		lblFPointsInputM8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		lblFPointsInputM8.setText(Messages.DesView_159);

		txtFPointsInputM8 = new Text(comFPointsInputM8, SWT.BORDER);
		txtFPointsInputM8.setText(Messages.DesView_156);
		txtFPointsInputM8.setTextLimit(8);

		txtFPointsInputM8.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				lblFPointsInputM8cur
						.setText(Messages.DesView_157 + txtFPointsInputM8.getText().length() + Messages.DesView_158);
			}
		});

		lblFPointsInputM8cur = new Label(comFPointsInputM8, SWT.NONE);
		lblFPointsInputM8cur.setText(Messages.DesView_160);

		// Composite holding Buttons and vertical separator line
		comFPointsInputBtn = new Composite(grpFPointsInput, SWT.NONE);
		comFPointsInputBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		comFPointsInputBtn.setLayout(new GridLayout(2, false));

		// vertical separator line
		lblFPointsInputSeparator = new Label(comFPointsInputBtn, SWT.SEPARATOR | SWT.VERTICAL);
		lblFPointsInputSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

		/*
		 * Action Buttons (Evaluate and Reset)
		 */
		btnFPointsEvaluate = new Button(comFPointsInputBtn, SWT.NONE);
		btnFPointsEvaluate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnFPointsEvaluate.setFont(FontService.getLargeBoldFont());
		btnFPointsEvaluate.setText(Messages.DesView_207);

		btnFPointsEvaluate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dESCon.FPoints_In_FixedPoints = btnFPointsFixedpoint.getSelection();
				dESCon.FPoints_In_M8 = txtFPointsInputM8.getText().toUpperCase();
				String err = Messages.DesView_208;

				if (btnFPointsK0.getSelection() && btnFPointsK0.getText().equals(Messages.DesView_209)) {
					dESCon.FPoints_In_selectedKey = 0;
				} else if (btnFPointsK0.getSelection() && btnFPointsK0.getText().equals(Messages.DesView_210)) {
					dESCon.FPoints_In_selectedKey = 0;
				} else if (btnFPointsK1.getSelection() && btnFPointsK1.getText().equals(Messages.DesView_211)) {
					dESCon.FPoints_In_selectedKey = 10;
				} else if (btnFPointsK1.getSelection() && btnFPointsK1.getText().equals(Messages.DesView_212)) {
					dESCon.FPoints_In_selectedKey = 9;
				} else if (btnFPointsK2.getSelection() && btnFPointsK2.getText().equals(Messages.DesView_213)) {
					dESCon.FPoints_In_selectedKey = 5;
				} else if (btnFPointsK2.getSelection() && btnFPointsK2.getText().equals(Messages.DesView_214)) {
					dESCon.FPoints_In_selectedKey = 6;
				} else if (btnFPointsK3.getSelection() && btnFPointsK3.getText().equals(Messages.DesView_215)) {
					dESCon.FPoints_In_selectedKey = 15;
				} else if (btnFPointsK3.getSelection() && btnFPointsK3.getText().equals(Messages.DesView_216)) {
					dESCon.FPoints_In_selectedKey = 11;
				}

				if (dESCon.FPointsStudy() == 0) {
					txtFPointsStatus.append(Messages.DesView_217 + getCurrentTime() + Messages.DesView_218
							+ txtFPointsInputM8.getText());
					txtFPointsStatus.setTopIndex(txtFPointsStatus.getLineCount() - 1);
					fillTable(tblFPointsOutputAFP, 0, 9, 1, 32, dESCon.FPoints_Out_M8M17);
					if (btnFPointsFixedpoint.getSelection()) {
						txtFPointsResult.setText(dESCon.FPoints_Out_AFpoints);
					} else {
						txtFPointsResult.setText(dESCon.FPoints_Out_AFpoints);
					}
					colorTable(tblFPointsOutputAFP, 1);
					for (int i = 1; i < tblFPointsOutputAFP.getItemCount(); i++) {
						tblFPointsOutputAFP.getItem(i).setText(tblFPointsOutputAFP.getColumnCount() - 1,
								Integer.toString(dESCon.FPoints_Out_Distances[i]));
					}
				} else {
					for (int i = 0; i < dESCon.errMsg.length; i++) {
						if (i != dESCon.errMsg.length - 1) {
							err += dESCon.errMsg[i] + Messages.DesView_221;
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtFPointsStatus.append(Messages.DesView_222 + getCurrentTime() + Messages.DesView_223 + err);
					txtFPointsStatus.setTopIndex(txtFPointsStatus.getLineCount() - 1);

				}
			}
		});

		btnFPointsReset = new Button(comFPointsInputBtn, SWT.NONE);
		btnFPointsReset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnFPointsReset.setText(Messages.DesView_196);

		btnFPointsReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btnFPointsK0.setSelection(true);
				btnFPointsK0.setText(Messages.DesView_197);
				btnFPointsK1.setSelection(false);
				btnFPointsK1.setText(Messages.DesView_198);
				btnFPointsK2.setSelection(false);
				btnFPointsK2.setText(Messages.DesView_199);
				btnFPointsK3.setSelection(false);
				btnFPointsK3.setText(Messages.DesView_200);
				txtFPointsInputM8.setText(Messages.DesView_156);
				cleanTable(tblFPointsOutputAFP);
				txtFPointsResult.setText(Messages.DesView_201);

				TableItem[] tblItems = tblFPointsOutputAFP.getItems();
				for (int i = 0; i < 10; i++) {
					tblItems[i].setText(0, Messages.DesView_203 + (i + 8) + Messages.DesView_204);
				}

				txtFPointsStatus.append(Messages.DesView_205 + getCurrentTime() + Messages.DesView_206);
				txtFPointsStatus.setTopIndex(txtFPointsStatus.getLineCount() - 1);
			}
		});

		comFPointsResult = new Composite(grpFPointsResult, SWT.NONE);
		GridData comFPointsResultLayoutDat = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		comFPointsResultLayoutDat.widthHint = 150;
		comFPointsResult.setLayoutData(comFPointsResultLayoutDat);
		GridLayout comFPointsResultLayout = new GridLayout(1, true);
		comFPointsResultLayout.verticalSpacing = 20;
		comFPointsResult.setLayout(comFPointsResultLayout);

		lblFPointsResult = new Label(comFPointsResult, SWT.NONE);
		lblFPointsResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblFPointsResult.setFont(FontService.getLargeBoldFont());
		lblFPointsResult.setText(Messages.DesView_138);

		txtFPointsResult = new StyledText(comFPointsResult, SWT.WRAP);
		txtFPointsResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		txtFPointsResult.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtFPointsResult.setEditable(false);
		txtFPointsResult.setCaret(null);
		txtFPointsResult.setAlignment(SWT.CENTER);

		// Internal states tab
		comFPointsOutput = new Composite(grpFPointsOutput, SWT.NONE);
		comFPointsOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comFPointsOutput.setLayout(new GridLayout(1, true));

		tblFPointsOutputAFP = new Table(comFPointsOutput, SWT.NO_FOCUS | SWT.VIRTUAL);
		tblFPointsOutputAFP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblFPointsOutputAFP.setHeaderVisible(true);
		tblFPointsOutputAFP.setLinesVisible(true);

		TableColumn[] tblFPointsColumn = new TableColumn[34];
		tblFPointsColumn[0] = new TableColumn(tblFPointsOutputAFP, SWT.NONE);
		tblFPointsColumn[0].setWidth(45);
		tblFPointsColumn[0].setAlignment(SWT.CENTER);
		for (int i = 1; i < 33; i++) {
			tblFPointsColumn[i] = new TableColumn(tblFPointsOutputAFP, SWT.NONE);
			tblFPointsColumn[i].setWidth(25);
			tblFPointsColumn[i].setAlignment(SWT.CENTER);
			tblFPointsColumn[i].setText(i + Messages.DesView_161);

		}
		tblFPointsColumn[33] = new TableColumn(tblFPointsOutputAFP, SWT.NONE);
		tblFPointsColumn[33].setWidth(70);
		tblFPointsColumn[33].setAlignment(SWT.CENTER);
		tblFPointsColumn[33].setText(Messages.DesView_162);

		TableItem[] tblFPointsItem = new TableItem[10];
		for (int i = 0; i < 10; i++) {
			tblFPointsItem[i] = new TableItem(tblFPointsOutputAFP, SWT.BORDER);
		}
		cleanTable(tblFPointsOutputAFP);
		for (int i = 0; i < 10; i++) {
			tblFPointsItem[i].setText(0, Messages.DesView_163 + (i + 8) + Messages.DesView_164);
		}

		tabFPoints.setControl(comFPoints);

	}

	private void createSBoxTab() {

		tabSBox = new TabItem(tfolder_1, SWT.NONE);
		tabSBox.setText(Messages.DesView_224);

		// Tab Layout
		comSBox = new Composite(tfolder_1, SWT.NONE);
		comSBox.setLayout(new FillLayout());

		comSBoxMain = new Composite(comSBox, SWT.NONE);
		comSBoxMain.setLayout(new GridLayout(2, false));

		// create title
		lblSBoxTitle = new Label(comSBoxMain, SWT.NONE);
		lblSBoxTitle.setFont(FontService.getHeaderFont());
		lblSBoxTitle.setText(Messages.DesView_title);
		lblSBoxTitle.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		// create title description
		lblSBoxInformationText = new Label(comSBoxMain, SWT.NONE);
		lblSBoxInformationText.setText(Messages.DesView_text);
		lblSBoxInformationText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		// Slider between left infobox and right content
		sashSBoxDivideMain = new SashForm(comSBoxMain, SWT.HORIZONTAL);
		sashSBoxDivideMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		sashSBoxDivideMain.setLayout(new FillLayout());

		comSBoxMainLeft = new Composite(sashSBoxDivideMain, SWT.NONE);
		comSBoxMainLeft.setLayout(new GridLayout(1, false));

		comSBoxMainRight = new Composite(sashSBoxDivideMain, SWT.NONE);
		comSBoxMainRight.setLayout(new GridLayout(1, false));

		sashSBoxDivideMain.setWeights(new int[] { 23, 77 });
		sashSBoxDivideMain.setSashWidth(10);

		// Composite for Status Group
		comSBoxMainDown = new Composite(comSBoxMain, SWT.NONE);
		GridData comSBoxMainDownLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		comSBoxMainDownLayout.heightHint = 130;
		comSBoxMainDown.setLayoutData(comSBoxMainDownLayout);
		comSBoxMainDown.setLayout(new GridLayout(1, false));

		comSBoxMainRightUp = new Composite(comSBoxMainRight, SWT.NONE);
		comSBoxMainRightUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		comSBoxMainRightUp.setLayout(new GridLayout(1, false));

		comSBoxMainRightDown = new Composite(comSBoxMainRight, SWT.NONE);
		comSBoxMainRightDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		comSBoxMainRightDown.setLayout(new GridLayout(1, false));

		// Group containing Input-Tab -> parameter/start buttons
		grpSBoxInput = new Group(comSBoxMainRightUp, SWT.NONE);
		grpSBoxInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpSBoxInput.setLayout(new GridLayout(3, false));
		grpSBoxInput.setText(Messages.DesView_225);

		// group containing output
		grpSBoxOutput = new Group(comSBoxMainRightDown, SWT.NONE);
		grpSBoxOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSBoxOutput.setLayout(new GridLayout(2, false));
		grpSBoxOutput.setText(Messages.DesView_226);

		// group containing infobox
		grpSBoxInformation = new Group(comSBoxMainLeft, SWT.NONE);
		grpSBoxInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSBoxInformation.setLayout(new GridLayout(1, false));
		grpSBoxInformation.setText(Messages.DesView_227);

		// infobox
		txtSBoxInformation = new StyledText(grpSBoxInformation, SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		txtSBoxInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txtSBoxInformation.setFont(FontService.getNormalFont());
		txtSBoxInformation.setEditable(false);
		txtSBoxInformation.setCaret(null);
		txtSBoxInformation.setAlwaysShowScrollBars(false);

		txtSBoxInformation
				.setText(Messages.DesView_244 + Messages.DesView_245 + Messages.DesView_246 + Messages.DesView_247
						+ Messages.DesView_248 + Messages.DesView_249 + Messages.DesView_250 + Messages.DesView_251
						+ Messages.DesView_252 + Messages.DesView_253 + Messages.DesView_254 + Messages.DesView_255
						+ Messages.DesView_256 + Messages.DesView_257 + Messages.DesView_258 + Messages.DesView_259
						+ Messages.DesView_260 + Messages.DesView_261 + Messages.DesView_263 + Messages.DesView_264);

		// group for statusbox
		grpSBoxStatus = new Group(comSBoxMainDown, SWT.NONE);
		grpSBoxStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSBoxStatus.setText(Messages.DesView_228);
		grpSBoxStatus.setLayout(new FormLayout());

		txtSBoxStatus = new StyledText(grpSBoxStatus, SWT.MULTI | SWT.V_SCROLL);
		txtSBoxStatus.setEditable(false);
		txtSBoxStatus.setCaret(null);
		txtSBoxStatus.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSBoxStatus.setAlwaysShowScrollBars(false);
		FormData fd_txtSBoxStatus = new FormData();
		fd_txtSBoxStatus.bottom = new FormAttachment(100, -10);
		fd_txtSBoxStatus.right = new FormAttachment(100, -10);
		fd_txtSBoxStatus.top = new FormAttachment(0, 10);
		fd_txtSBoxStatus.left = new FormAttachment(0, 10);
		txtSBoxStatus.setLayoutData(fd_txtSBoxStatus);

		// Input Tab
		comSBoxInput = new Composite(grpSBoxInput, SWT.NONE);
		comSBoxInput.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		comSBoxInput.setLayout(new GridLayout(2, false));

		// Data heading
		lblSBoxInputData = new Label(comSBoxInput, SWT.NONE);
		lblSBoxInputData.setText(Messages.DesView_229);
		lblSBoxInputData.setFont(FontService.getLargeBoldFont());
		lblSBoxInputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		// Delta_p description
		lblSBoxInputDeltap = new Label(comSBoxInput, SWT.NONE);
		lblSBoxInputDeltap.setText(Messages.DesView_230);
		lblSBoxInputDeltap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		// Delta_p input box
		txtSBoxInputDeltap = new Text(comSBoxInput, SWT.BORDER);
		txtSBoxInputDeltap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtSBoxInputDeltap.setTextLimit(16);
		txtSBoxInputDeltap.setText(Messages.DesView_232);

		txtSBoxInputDeltap.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				lblSBoxInputDeltapCur
						.setText(Messages.DesView_233 + txtSBoxInputDeltap.getText().length() + Messages.DesView_234);

			}

		});

		// Current character counter
		lblSBoxInputDeltapCur = new Label(comSBoxInput, SWT.NONE);
		lblSBoxInputDeltapCur.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSBoxInputDeltapCur.setText(Messages.DesView_231);

		// Composite for "Series options" parameters
		comSBoxInputDataSeries = new Composite(grpSBoxInput, SWT.NONE);
		comSBoxInputDataSeries.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout comSBoxInputDataSeriesLayout = new GridLayout(2, false);
		comSBoxInputDataSeriesLayout.horizontalSpacing = 30;
		comSBoxInputDataSeries.setLayout(comSBoxInputDataSeriesLayout);

		// Series description Label
		lblSBoxInputSeries = new Label(comSBoxInputDataSeries, SWT.NONE);
		lblSBoxInputSeries.setText(Messages.DesView_235);
		lblSBoxInputSeries.setFont(FontService.getLargeBoldFont());
		lblSBoxInputSeries.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		// Slider (time) description Label
		lblSBoxInputSeriesTime = new Label(comSBoxInputDataSeries, SWT.NONE);
		lblSBoxInputSeriesTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

		// Slider (rounds) description Label
		lblSBoxInputSeriesCount = new Label(comSBoxInputDataSeries, SWT.NONE);
		lblSBoxInputSeriesCount.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

		// Slider (time)
		slSBoxInputSeriesTime = new Slider(comSBoxInputDataSeries, SWT.NONE);
		slSBoxInputSeriesTime.setMinimum(1);
		slSBoxInputSeriesTime.setMaximum(1000);
		slSBoxInputSeriesTime.setSelection(100);

		slSBoxInputSeriesTime.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				lblSBoxInputSeriesTime.setText(Messages.DesView_236 + slSBoxInputSeriesTime.getSelection());
			}
		});

		// Slider (rounds)
		slSBoxInputSeriesCount = new Slider(comSBoxInputDataSeries, SWT.NONE);
		slSBoxInputSeriesCount.setMinimum(1);
		slSBoxInputSeriesCount.setMaximum(200);
		slSBoxInputSeriesCount.setSelection(20);

		slSBoxInputSeriesCount.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				lblSBoxInputSeriesCount.setText(Messages.DesView_238 + slSBoxInputSeriesCount.getSelection());
			}
		});

		lblSBoxInputSeriesTime.setText(Messages.DesView_237 + slSBoxInputSeriesTime.getSelection());
		lblSBoxInputSeriesCount.setText(Messages.DesView_239 + slSBoxInputSeriesCount.getSelection());

		// Action Buttons (Evalaute, Evaluate series, and Reset)
		comSBoxInputBtn = new Composite(grpSBoxInput, SWT.NONE);
		comSBoxInputBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout comSBoxInputBtnLayout = new GridLayout(3, false);
		comSBoxInputBtnLayout.verticalSpacing = 11;
		comSBoxInputBtn.setLayout(comSBoxInputBtnLayout);

		// Vertical line
		lblSBoxInputSeparator = new Label(comSBoxInputBtn, SWT.SEPARATOR | SWT.VERTICAL);
		lblSBoxInputSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

		btnSBoxEvaluate = new Button(comSBoxInputBtn, SWT.NONE);
		btnSBoxEvaluate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSBoxEvaluate.setFont(FontService.getLargeBoldFont());
		btnSBoxEvaluate.setText(Messages.DesView_274);

		btnSBoxEvaluate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dESCon.SBox_In_Deltap = txtSBoxInputDeltap.getText().toUpperCase();
				String err = Messages.DesView_275;

				if (dESCon.SBoxStudy() == 0) {
					txtSBoxStatus.append(Messages.DesView_276 + getCurrentTime() + Messages.DesView_277
							+ txtSBoxInputDeltap.getText());
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount() - 1);
					txtSBoxInputDeltap.setEnabled(false);
					intSBoxOutputCurStep++;
					lblSBoxOutputCurStep.setText(Messages.DesView_278 + intSBoxOutputCurStep);
					lblSBoxOutputP.setText(Messages.DesView_279 + dESCon.SBox_Out_randomm);
					lblSBoxOutputK.setText(Messages.DesView_280 + dESCon.SBox_Out_randomk);
					fillTable(tblSBoxOutput, 0, 15, 0, 7, dESCon.SBox_Out_activeBoxes);
					colorTable(tblSBoxOutput, 0);
				} else {
					for (int i = 0; i < dESCon.errMsg.length; i++) {
						if (i != dESCon.errMsg.length - 1) {
							err += dESCon.errMsg[i] + Messages.DesView_281;
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtSBoxStatus.append(Messages.DesView_282 + getCurrentTime() + Messages.DesView_283 + err);
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount() - 1);

				}
			}
		});

		btnSBoxEvaluateSeries = new Button(comSBoxInputBtn, SWT.NONE);
		btnSBoxEvaluateSeries.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSBoxEvaluateSeries.setFont(FontService.getLargeBoldFont());
		btnSBoxEvaluateSeries.setText(Messages.DesView_284);

		btnSBoxEvaluateSeries.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String err = Messages.DesView_285;
				dESCon.SBox_In_Deltap = txtSBoxInputDeltap.getText().toUpperCase();
				Display curdis = Display.getCurrent();

				if (dESCon.SBoxStudy() == 0) {
					txtSBoxStatus.append(Messages.DesView_286 + getCurrentTime() + Messages.DesView_287
							+ txtSBoxInputDeltap.getText() + Messages.DesView_288
							+ slSBoxInputSeriesCount.getSelection() + Messages.DesView_289
							+ slSBoxInputSeriesTime.getSelection());
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount() - 1);
					txtSBoxInputDeltap.setEnabled(false);
					slSBoxInputSeriesTime.setEnabled(false);
					slSBoxInputSeriesCount.setEnabled(false);
					btnSBoxEvaluate.setEnabled(false);
					btnSBoxReset.setEnabled(false);
					btnSBoxEvaluateSeries.setEnabled(false);

					for (int i = 0; i < slSBoxInputSeriesCount.getSelection() - 1; i++) {
						intSBoxOutputCurStep++;
						lblSBoxOutputCurStep.setText(Messages.DesView_290 + intSBoxOutputCurStep);
						lblSBoxOutputP.setText(Messages.DesView_291 + dESCon.SBox_Out_randomm);
						lblSBoxOutputK.setText(Messages.DesView_292 + dESCon.SBox_Out_randomk);
						fillTable(tblSBoxOutput, 0, 15, 0, 7, dESCon.SBox_Out_activeBoxes);
						colorTable(tblSBoxOutput, 0);
						tblSBoxOutput.redraw();
						tblSBoxOutput.update();
						curdis.update();
						try {
							Thread.sleep(slSBoxInputSeriesTime.getSelection());
						} catch (Exception ex) {
							LogUtil.logError(ex);
						}
						dESCon.SBoxStudy();
					}
					intSBoxOutputCurStep++;
					lblSBoxOutputCurStep.setText(Messages.DesView_293 + intSBoxOutputCurStep);

					slSBoxInputSeriesTime.setEnabled(true);
					slSBoxInputSeriesCount.setEnabled(true);
					btnSBoxEvaluate.setEnabled(true);
					btnSBoxReset.setEnabled(true);
					btnSBoxEvaluateSeries.setEnabled(true);

				} else {
					for (int i = 0; i < dESCon.errMsg.length; i++) {
						if (i != dESCon.errMsg.length - 1) {
							err += dESCon.errMsg[i] + Messages.DesView_294;
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtSBoxStatus.append(Messages.DesView_295 + getCurrentTime() + Messages.DesView_296 + err);
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount() - 1);
				}

			}
		});

		btnSBoxReset = new Button(comSBoxInputBtn, SWT.NONE);
		btnSBoxReset.setText(Messages.DesView_265);
		btnSBoxReset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		btnSBoxReset.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				txtSBoxStatus.append(Messages.DesView_266 + getCurrentTime() + Messages.DesView_267);
				txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount() - 1);
				txtSBoxInputDeltap.setText(Messages.DesView_232);
				lblSBoxOutputCurStep.setText(Messages.DesView_269);
				intSBoxOutputCurStep = 0;
				lblSBoxOutputP.setText(Messages.DesView_270);
				lblSBoxOutputK.setText(Messages.DesView_271);
				slSBoxInputSeriesCount.setSelection(20);
				slSBoxInputSeriesTime.setSelection(100);
				lblSBoxInputSeriesCount.setText(Messages.DesView_272);
				lblSBoxInputSeriesTime.setText(Messages.DesView_273);
				cleanTable(tblSBoxOutput);
				txtSBoxInputDeltap.setEnabled(true);
				slSBoxInputSeriesTime.setEnabled(true);
				slSBoxInputSeriesCount.setEnabled(true);
				cleanTable(tblSBoxOutput);
			}
		});

		// Output Tab
		tblSBoxOutput = new Table(grpSBoxOutput, SWT.NO_FOCUS | SWT.VIRTUAL);
		tblSBoxOutput.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		tblSBoxOutput.setHeaderVisible(true);
		tblSBoxOutput.setLinesVisible(true);

		TableColumn[] tblSBoxColumn = new TableColumn[8];
		for (int i = 0; i < 8; i++) {
			tblSBoxColumn[i] = new TableColumn(tblSBoxOutput, SWT.BORDER);
			tblSBoxColumn[i].setWidth(30);
			tblSBoxColumn[i].setAlignment(SWT.CENTER);
			tblSBoxColumn[i].setText(Messages.DesView_240 + (i + 1));

		}

		TableItem[] tblSBoxItem = new TableItem[16];
		for (int i = 0; i < 16; i++) {
			tblSBoxItem[i] = new TableItem(tblSBoxOutput, SWT.BORDER);
		}
		cleanTable(tblSBoxOutput);

		// Group beside table showing current step, p, and x
		grpSBoxResult = new Group(grpSBoxOutput, SWT.NONE);
		GridData grpSBoxResultData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		grpSBoxResultData.widthHint = 200;
		grpSBoxResult.setLayoutData(grpSBoxResultData);
		grpSBoxResult.setLayout(new GridLayout(1, true));

		lblSBoxOutputCurStep = new Label(grpSBoxResult, SWT.NONE);
		lblSBoxOutputCurStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSBoxOutputCurStep.setText(Messages.DesView_241);

		lblSBoxOutputP = new Label(grpSBoxResult, SWT.NONE);
		lblSBoxOutputP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSBoxOutputP.setText(Messages.DesView_242);

		lblSBoxOutputK = new Label(grpSBoxResult, SWT.NONE);
		lblSBoxOutputK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSBoxOutputK.setText(Messages.DesView_243);

		tabSBox.setControl(comSBox);
	}

	// Helper Functions
	private void fillTable(Table table, int start_row, int end_row, int start_column, int end_column, int data[][]) {
		;
		TableItem ti = null;

		for (int r = start_row; r <= end_row; r++) {
			for (int c = start_column; c <= end_column; c++) {
				ti = table.getItem(r);
				ti.setText(c, Integer.toString((data[r - start_row][c - start_column])));
			}
		}
	}

	private void colorTable(Table table, int mode) {
		TableItem ti = null;
		TableItem tiOld = null;
		Display display = null;
		Color yellow = null;
		Color red = null;
		Color gray = null;
		Color curColor = null;
		int i = 0, j = 0;

		display = Display.getCurrent();
		yellow = display.getSystemColor(SWT.COLOR_YELLOW);
		red = display.getSystemColor(SWT.COLOR_RED);
		gray = display.getSystemColor(SWT.COLOR_GRAY);

		// Coloring S-Box Tab
		if (mode == 0) {
			for (int r = 0; r < table.getItemCount(); r++) {
				ti = table.getItem(r);
				for (int c = 0; c < table.getColumnCount(); c++) {
					if (ti.getText(c).equals(Messages.DesView_297)) {
						ti.setBackground(c, red);
					} else if (ti.getText(c).equals(Messages.DesView_298)) {
						ti.setBackground(c, yellow);
					}
				}

			}
			// Coloring FPoints Tab, Alg Tab M0M17 Output, Alg Tab DES(k,p+e_i)
			// Output (Bit Change Coloring)
		} else if (mode == 1) {
			for (int r = 1; r < table.getItemCount(); r++) {
				ti = table.getItem(r);
				tiOld = table.getItem(r - 1);
				for (int c = 1; c < table.getColumnCount() - 1; c++) {
					if (!(ti.getText(c).equals(tiOld.getText(c)))) {
						ti.setForeground(c, red);
					}
				}
			}
			// Coloring Alg Tab Roundkeys Output
		} else if (mode == 2) {

			// Coloring Alg Tab CD Matrix Output
		} else if (mode == 3) {
			while (i < table.getItemCount()) {
				curColor = j % 2 == 0 ? yellow : gray;
				for (int c = 0; c < table.getColumnCount(); c++) {
					ti = table.getItem(i);
					ti.setBackground(c, curColor);
					ti = table.getItem(i + 1);
					ti.setBackground(c, curColor);
				}
				i = i + 2;
				j++;
			}
		}

	}

	private void cleanTable(Table table) {
		TableItem[] items = table.getItems();
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		String[] str = new String[table.getColumnCount()];

		for (int i = 0; i < str.length; i++) {
			str[i] = Messages.DesView_299;
		}

		for (int i = 0; i < table.getItemCount(); i++) {
			items[i].setText(str);
			for (int j = 0; j < table.getColumnCount(); j++) {

				if ((i % 2) == 0) {
					items[i].setBackground(j, white);
				} else {
					items[i].setBackground(j, gray);
				}
			}
		}
	}

	private String getCurrentTime() {
		DateFormat df = new SimpleDateFormat(Messages.DesView_30);
		Date d = new Date();

		return df.format(d);
	}

	public void reset() {
		Control[] children = parent.getChildren();
		for (Control control : children) {
			control.dispose();
		}
		createPartControl(parent);
		parent.layout();
	}

}
