package org.jcryptool.visual.des.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
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
import org.jcryptool.core.util.colors.ColorService;
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
	private ScrolledComposite wrapper = null;
	private TabFolder tfolder_1;
	private TabItem tabAlg = null;;
	private Text lblTitle = null;
	private Text lblInformationText = null;
	private Composite comAlgMain = null;
	private Group grpAlgInput = null;
	private Group grpAlgOutput = null;
	private Group grpInformation = null;
	private Group grpAlgStatus = null;
	private StyledText txtAlgStatus = null;
	private Button btnAlgDecrypt = null;
	private Button btnAlgEncrypt = null;
	private Label lblAlgMode = null;
	private Button btnAlgK0 = null;
	private Button btnAlgK3 = null;
	private Button btnAlgK5 = null;
	private Button btnAlgK6 = null;
	private Button btnAlgK9 = null;
	private Button btnAlgK10 = null;
	private Button btnAlgK12 = null;
	private Button btnAlgK15 = null;
	private Label lblAlgInputKey = null;
	private Button btnAlgManual = null;
	private Label lblAlgInputData = null;
	private Label lblAlgInputPlaintext = null;
	private Text txtAlgKeyManual = null;
	private Text txtAlgInputData = null;
	private TabFolder tfAlgOutput = null;
	private TabItem tbtmAlgM0M17 = null;
	private TabItem tbtmAlgDES = null;
	private TabItem tbtmAlgHamming = null;
	private TabItem tbtmAlgRoundkeys = null;
	private TabItem tbtmAlgCDMatrix = null;
	private Button btnAlgEvaluate = null;
	private Button btnAlgReset = null;
	private Table tblAlgOutputDeskpei = null;
	private Composite comAlgOutputDeskpei = null;
	private Composite comAlgOutputRoundkeys = null;
	private Table tblAlgOutputRoundkeys = null;
	private Table tblAlgOutputCDMatrix = null;
	private Composite comAlgOutputCDMatrix = null;
	private Table tblAlgOutputM0M17 = null;
	private Label lblAlgInputSeparator = null;
	private Label lblAlgResult = null;
	private StyledText txtAlgResult = null;
	private Group grpAlgResult = null;
	private Label lblAlgInputManualCurChar = null;
	private Label lblAlgInputDataCurChar = null;
	private Table tblAlgOutputHamming1 = null;
	private Table tblAlgOutputHamming2 = null;
	private Composite comAlgOutputHamming = null;
	private StyledText txtInformation = null;
	private Text tblAlgOutputHammingDes = null;
	private Composite modeComposite;
	private Composite keyCompositeAA;
	private Composite dataCompositeAA;

	// FPoints Study
	private TabItem tabFPoints = null;
	private Composite comFPointsMain = null;
	private Group grpFPointsInput = null;
	private Group grpFPointsOutput = null;
	private Group grpFPointsStatus = null;
	private StyledText txtFPointsStatus = null;
	private Button btnFPointsEvaluate = null;
	private Button btnFPointsReset = null;
	private Label lblFPointsInputTarget = null;
	private Button btnFPointsFixedpoint = null;
	private Button btnFPointsAntifixedPoint = null;
	private Label lblFPointsInputKey = null;
	private Button btnFPointsK0 = null;
	private Button btnFPointsK1 = null;
	private Button btnFPointsK2 = null;
	private Button btnFPointsK3 = null;
	private Label lblFPointsInputData = null;
	private Text txtFPointsInputM8 = null;
	private Label lblFPointsInputM8 = null;
	private Label lblFPointsInputSeparator = null;
	private Group grpFPointsResult = null;
	private Label lblFPointsResult = null;
	private StyledText txtFPointsResult = null;
	private Table tblFPointsOutputAFP = null;
	private Composite targetComposite;
	private Composite keyComposite;
	private Composite dataComposite;

	// SBox Study
	private TabItem tabSBox = null;
	private Composite comSBoxMain = null;
	private Group grpSBoxInput = null;
	private Group grpSBoxOutput = null;
	private Group grpSBoxStatus = null;
	private StyledText txtSBoxStatus = null;
	private Label lblSBoxInputData = null;
	private Label lblSBoxInputDeltap = null;
	private Label lblSBoxInputDeltapCur = null;
	private Text txtSBoxInputDeltap = null;
	private Button btnSBoxEvaluate = null;
	private Button btnSBoxReset = null;
	private Button btnSBoxEvaluateSeries = null;
	private Table tblSBoxOutput = null;
	private Group grpSBoxResult = null;
	private Slider slSBoxInputSeriesCount = null;
	private Slider slSBoxInputSeriesTime = null;
	private Label lblSBoxInputSeries = null;
	private Label lblSBoxInputSeriesTime = null;
	private Label lblSBoxInputSeriesCount = null;
	private Label lblSBoxInputSeparator = null;
	private int intSBoxOutputCurStep = 0;
	private Label lblSBoxOutputCurStep = null;
	private Label lblSBoxOutputP = null;
	private Label lblSBoxOutputK = null;
	private Label lblFPointsInputM8cur = null;
	
	private Composite parent;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		wrapper = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		wrapper.setExpandHorizontal(true);
		wrapper.setExpandVertical(true);

		// Folder
		tfolder_1 = new TabFolder(wrapper, SWT.TOP);
		tfolder_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createAlgTab();
		createFPointsTab();
		createSBoxTab();

		wrapper.setContent(tfolder_1);
		wrapper.setMinSize(tfolder_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// Create DES Controller
		dESCon = new DESController();
	}

	@Override
	public void setFocus() {
		wrapper.setFocus();
	}
	
	/**
	 * Creates the Title and description Part on the top of each tab.
	 * @param parent The parent COmposite. must have an GrifLayout with 2 Columns.
	 */
	private void createTitleAndDescription(Composite parent) {
		// title
		lblTitle = new Text(parent, SWT.NONE);
		lblTitle.setEditable(false);
		lblTitle.setBackground(ColorService.WHITE);
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		lblTitle.setFont(FontService.getHeaderFont());
		lblTitle.setText(Messages.DesView_title);
		
		// title description
		lblInformationText = new Text(parent, SWT.WRAP | SWT.READ_ONLY);
		lblInformationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		lblInformationText.setText(Messages.DesView_text);
	}
	
	/**
	 * Creates the information Group on the left.
	 * @param parent
	 */
	private void createInformationGroup(Composite parent, String info) {
		
		grpInformation = new Group(parent, SWT.NONE);
		grpInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
		grpInformation.setText(Messages.DesView_3);
		grpInformation.setLayout(new GridLayout());
		
		// infobox
		txtInformation = new StyledText(grpInformation, SWT.WRAP | SWT.V_SCROLL);
		GridData gd_txtAlgInformation = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_txtAlgInformation.widthHint = 350;
		gd_txtAlgInformation.heightHint = 300;
		txtInformation.setLayoutData(gd_txtAlgInformation);
		txtInformation.setEditable(false);
		txtInformation.setAlwaysShowScrollBars(false);
		txtInformation.setText(info);
	}

	//TODO nur ein Anker
	/**
	 * Creates the "Algorithm Study" tab and all its GUI elements
	 */
	private void createAlgTab() {
		tabAlg = new TabItem(tfolder_1, SWT.NONE);
		tabAlg.setText(Messages.DesView_0);

		comAlgMain = new Composite(tfolder_1, SWT.NONE);
		comAlgMain.setLayout(new GridLayout(3, false));

		createTitleAndDescription(comAlgMain);
		
		createInformationGroup(comAlgMain, Messages.DesView_63 + Messages.DesView_70 + Messages.DesView_80);

		// Group holding Input Tab -> parameter/start buttons
		grpAlgInput = new Group(comAlgMain, SWT.NONE);
		grpAlgInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		grpAlgInput.setText(Messages.DesView_1);
		GridLayout gl_grpAlgInput = new GridLayout(5, false);
		gl_grpAlgInput.horizontalSpacing = 10;
		grpAlgInput.setLayout(gl_grpAlgInput);
		
		//Composite holding the radio Button where the mode is selectable
		modeComposite = new Composite(grpAlgInput, SWT.NONE);
		GridLayout gl_modeComposite = new GridLayout();
		gl_modeComposite.marginHeight = 0;
		gl_modeComposite.marginWidth = 0;
		modeComposite.setLayout(gl_modeComposite);
		modeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
		
		// Mode heading
		lblAlgMode = new Label(modeComposite, SWT.NONE);
		lblAlgMode.setFont(FontService.getLargeBoldFont());
		lblAlgMode.setText(Messages.DesView_11);
		
		//Radio Button Encrypt
		btnAlgEncrypt = new Button(modeComposite, SWT.RADIO);
		btnAlgEncrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgEncrypt.setText(Messages.DesView_8);
		btnAlgEncrypt.setSelection(true);
		btnAlgEncrypt.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblAlgInputPlaintext.setText(Messages.DesView_61);
				lblAlgResult.setText(Messages.DesView_71);
				txtAlgResult.setText("");
				comAlgMain.layout();
			}
		});
		
		//Radio Button Decrypt
		btnAlgDecrypt = new Button(modeComposite, SWT.RADIO);
		btnAlgDecrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgDecrypt.setText(Messages.DesView_5);
		btnAlgDecrypt.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblAlgInputPlaintext.setText(Messages.DesView_62);
				lblAlgResult.setText(Messages.DesView_81);
				txtAlgResult.setText("");
				comAlgMain.layout();
			}
		});
		
		keyCompositeAA = new Composite(grpAlgInput, SWT.NONE);
		GridLayout gl_keyCompositeAA = new GridLayout(6, false);
		gl_keyCompositeAA.marginHeight = 0;
		gl_keyCompositeAA.marginWidth = 0;
		gl_keyCompositeAA.horizontalSpacing = 10;
		keyCompositeAA.setLayout(gl_keyCompositeAA);
		keyCompositeAA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
		
		// key selection heading
		lblAlgInputKey = new Label(keyCompositeAA, SWT.NONE);
		lblAlgInputKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 6, 1));
		lblAlgInputKey.setFont(FontService.getLargeBoldFont());
		lblAlgInputKey.setText(Messages.DesView_13);
		
		//k[0]
		btnAlgK0 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK0.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK0.setText("k[0]");
		btnAlgK0.setSelection(true);
		
		//k[3]
		btnAlgK3 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK3.setText("k[3]");
		
		//k[5]
		btnAlgK5 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK5.setText("k[5]");
		
		//k[6}
		btnAlgK6 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK6.setText("k[6]");
		
		//manual input
		btnAlgManual = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgManual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 2, 1));
		btnAlgManual.setText(Messages.DesView_21);
		btnAlgManual.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnAlgManual.getSelection()) {
					txtAlgKeyManual.setEnabled(true);
				} else {
					txtAlgKeyManual.setEnabled(false);
				}
			}
		});
		
		//k[9]
		btnAlgK9 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK9.setLayoutData(new  GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK9.setText("k[9]");
		
		//k[10]
		btnAlgK10 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK10.setLayoutData(new  GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK10.setText("k[10]");
		
		//k[12]
		btnAlgK12 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK12.setLayoutData(new  GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK12.setText("k[12]");
		
		//k[15]
		btnAlgK15 = new Button(keyCompositeAA, SWT.RADIO);
		btnAlgK15.setLayoutData(new  GridData(SWT.FILL, SWT.CENTER, false, true));
		btnAlgK15.setText("k[15]");
		
		//manual
		txtAlgKeyManual = new Text(keyCompositeAA, SWT.BORDER);
		txtAlgKeyManual.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		txtAlgKeyManual.setTextLimit(16);
		txtAlgKeyManual.setEnabled(false);
		txtAlgKeyManual.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				lblAlgInputManualCurChar
						.setText("(" + txtAlgKeyManual.getText().length() + ")");
				if (txtAlgKeyManual.getText().length() >= 10)
					grpAlgInput.layout();
			}
		});
		
		//(0)
		lblAlgInputManualCurChar = new Label(keyCompositeAA, SWT.NONE);
		lblAlgInputManualCurChar.setLayoutData(new  GridData(SWT.FILL, SWT.CENTER, false, true));
		lblAlgInputManualCurChar.setText("(0)");
		
		dataCompositeAA = new Composite(grpAlgInput, SWT.NONE);
		GridLayout gl_dataCompositeAA = new GridLayout(2, false);
		gl_dataCompositeAA.marginHeight = 0;
		gl_dataCompositeAA.marginWidth = 0;
		dataCompositeAA.setLayout(gl_dataCompositeAA);
		dataCompositeAA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
		
		// Data heading
		lblAlgInputData = new Label(dataCompositeAA, SWT.NONE);
		lblAlgInputData.setText(Messages.DesView_25);
		lblAlgInputData.setFont(FontService.getLargeBoldFont());
		lblAlgInputData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		//Plaintext (16 hexdigits):
		lblAlgInputPlaintext = new Label(dataCompositeAA, SWT.NONE);
		lblAlgInputPlaintext.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 2, 1));
		lblAlgInputPlaintext.setText(Messages.DesView_61);
		
		txtAlgInputData = new Text(dataCompositeAA, SWT.BORDER);
		txtAlgInputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		txtAlgInputData.setTextLimit(16);
		txtAlgInputData.setText("1111111111111111");
		txtAlgInputData.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				lblAlgInputDataCurChar
						.setText("(" + txtAlgInputData.getText().length() + ")");
			}
		});
		
		//(16)
		lblAlgInputDataCurChar = new Label(dataCompositeAA, SWT.NONE);
		lblAlgInputDataCurChar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		lblAlgInputDataCurChar.setText("(16)");
		
		// Separator label
		lblAlgInputSeparator = new Label(grpAlgInput, SWT.SEPARATOR | SWT.VERTICAL);
		lblAlgInputSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));

		//Button evaluate
		btnAlgEvaluate = new Button(grpAlgInput, SWT.NONE);
		btnAlgEvaluate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2));
		btnAlgEvaluate.setText(Messages.DesView_107);
		btnAlgEvaluate.setFont(FontService.getLargeBoldFont());
		btnAlgEvaluate.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String sKey = "";
				String sMode = "";
				String err = "";

				dESCon.Alg_In_Data = txtAlgInputData.getText().toUpperCase();
				if (btnAlgEncrypt.getSelection()) {
					dESCon.Alg_In_Mode = 0;
					sMode = Messages.DesView_111;
				} else if (btnAlgDecrypt.getSelection()){
					dESCon.Alg_In_Mode = 1;
					sMode = Messages.DesView_112;
				}
				if (btnAlgK0.getSelection()) {
					dESCon.Alg_In_selectedKey = 0;
					sKey = "K[0]";
				} else if (btnAlgK3.getSelection()) {
					dESCon.Alg_In_selectedKey = 3;
					sKey = "K[3]";
				} else if (btnAlgK5.getSelection()) {
					dESCon.Alg_In_selectedKey = 5;
					sKey = "K[5]";
				} else if (btnAlgK6.getSelection()) {
					dESCon.Alg_In_selectedKey = 6;
					sKey = "K[6]";
				} else if (btnAlgK9.getSelection()) {
					dESCon.Alg_In_selectedKey = 9;
					sKey = "K[9]";
				} else if (btnAlgK10.getSelection()) {
					dESCon.Alg_In_selectedKey = 10;
					sKey = "K[10]";
				} else if (btnAlgK12.getSelection()) {
					dESCon.Alg_In_selectedKey = 12;
					sKey = "K[12]";
				} else if (btnAlgK15.getSelection()) {
					dESCon.Alg_In_selectedKey = 15;
					sKey = "K[15]";
				} else if (btnAlgManual.getSelection()) {
					dESCon.Alg_In_selectedKey = 16;
					dESCon.Alg_In_manualKey = txtAlgKeyManual.getText().toUpperCase();
					sKey = Messages.DesView_121 + txtAlgKeyManual.getText() + ")";
				}

				if (dESCon.algorithmStudy() == 0) {
					if (btnAlgEncrypt.getSelection()) {
						txtAlgResult.setText(dESCon.Alg_Out_EncDecResult);
					} else if (btnAlgDecrypt.getSelection()){
						txtAlgResult.setText(dESCon.Alg_Out_EncDecResult);
					}
					
					fillTable(tblAlgOutputM0M17, 0, 17, 1, 32, dESCon.Alg_Out_M0M17);
					colorTable(tblAlgOutputM0M17, 1);
					for (int i = 1; i < tblAlgOutputM0M17.getItemCount(); i++) {
						tblAlgOutputM0M17.getItem(i).setText(tblAlgOutputM0M17.getColumnCount() - 1,
								Integer.toString(dESCon.Alg_Out_M0M17_Dist[i]));
					}
					
					fillTable(tblAlgOutputDeskpei, 0, 64, 1, 64, dESCon.Alg_Out_cipherMatrix);
					for (int i = 1; i < tblAlgOutputDeskpei.getItemCount(); i++) {
						tblAlgOutputDeskpei.getItem(i).setText(tblAlgOutputDeskpei.getColumnCount() - 1,
								Integer.toString(dESCon.Alg_Out_cipherMatrix_Dist[i]));
					}
					
					colorTable(tblAlgOutputDeskpei, 1);
					fillTable(tblAlgOutputHamming1, 0, 7, 0, 7, dESCon.Alg_Out_DistMatrix1);
					fillTable(tblAlgOutputHamming2, 0, 7, 0, 7, dESCon.Alg_Out_DistMatrix2);
					fillTable(tblAlgOutputRoundkeys, 0, 15, 1, 48, dESCon.Alg_Out_Roundkeys);
					fillTable(tblAlgOutputCDMatrix, 0, 33, 1, 28, dESCon.Alg_Out_CDMatrix);
					colorTable(tblAlgOutputCDMatrix, 3);

					txtAlgStatus.append(getCurrentTime() + Messages.DesView_126 + sMode
							+ Messages.DesView_127 + sKey + Messages.DesView_128
							+ txtAlgInputData.getText().toUpperCase() + "\n");
					txtAlgStatus.setTopIndex(txtAlgStatus.getLineCount());
				} else {
					for (int i = 0; i < dESCon.errMsg.length; i++) {
						if (i != dESCon.errMsg.length - 1) {
							err += dESCon.errMsg[i] + ",";
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtAlgStatus.append(getCurrentTime() + "\\" + err + "\n");
					txtAlgStatus.setTopIndex(txtAlgStatus.getLineCount());
				}
			}
		});
		
		//Button reset
		btnAlgReset = new Button(grpAlgInput, SWT.NONE);
		btnAlgReset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		btnAlgReset.setText(Messages.DesView_87);
		btnAlgReset.addSelectionListener(new SelectionAdapter() {
			
			@Override
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
				txtAlgKeyManual.setText("");
				txtAlgKeyManual.setEnabled(false);
				txtAlgInputData.setText("1111111111111111");
				txtAlgResult.setText("");
				lblAlgInputManualCurChar.setText("(0)");
				lblAlgInputDataCurChar.setText("(16)");
				
				//Clear all tables and color them white and gray
				tblAlgOutputDeskpei.clearAll();
				colorTable(tblAlgOutputDeskpei, 4);
				tblAlgOutputM0M17.clearAll();
				colorTable(tblAlgOutputM0M17, 4);
				tblAlgOutputHamming1.clearAll();
				colorTable(tblAlgOutputHamming1, 4);
				tblAlgOutputHamming2.clearAll();
				colorTable(tblAlgOutputHamming2, 4);
				tblAlgOutputRoundkeys.clearAll();
				colorTable(tblAlgOutputRoundkeys, 4);
				tblAlgOutputCDMatrix.clearAll();
				colorTable(tblAlgOutputCDMatrix, 3);
				
				
				TableItem[] tblItems = tblAlgOutputM0M17.getItems();
				for (int i = 0; i < 18; i++) {
					tblItems[i].setText(0, "m[" + (i) + "]");
				}
				
				tblItems = tblAlgOutputDeskpei.getItems();
				tblItems[0].setText(0, Messages.DesView_96);
				for (int i = 1; i < 65; i++) {
					tblItems[i].setText(0, "DES(" + i + ")");
				}
				
				tblItems = tblAlgOutputRoundkeys.getItems();
				for (int i = 0; i < 16; i++) {
					tblItems[i].setText(0, "K[" + (i + 1) + "]");
				}
				
				tblItems = tblAlgOutputCDMatrix.getItems();
				for (int i = 0, j = 0; i < 34; i = i + 2, j++) {
					tblItems[i].setText(0, "C[" + j + "]");
					tblItems[i + 1].setText(0, "D[" + j + "]");
				}

				txtAlgStatus.append(getCurrentTime() + Messages.DesView_106 + "\n");
				txtAlgStatus.setTopIndex(txtAlgStatus.getLineCount());

				if (btnAlgEncrypt.getSelection()) {
					lblAlgInputPlaintext.setText(Messages.DesView_61);
				} else {
					lblAlgInputPlaintext.setText(Messages.DesView_62);
				}
				comAlgMain.layout();
			}
		});

		// Result group
		grpAlgResult = new Group(comAlgMain, SWT.NONE);
		grpAlgResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		grpAlgResult.setText(Messages.DesView_60);
		grpAlgResult.setLayout(new GridLayout());

		// Heading
		lblAlgResult = new Label(grpAlgResult, SWT.NONE);
		lblAlgResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		lblAlgResult.setFont(FontService.getLargeBoldFont());
		lblAlgResult.setText(Messages.DesView_71);

		// Encryption/Decryption Result
		txtAlgResult = new StyledText(grpAlgResult, SWT.CENTER | SWT.WRAP);
		txtAlgResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		txtAlgResult.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlgResult.setEditable(false);
		
		// Internal States Group
		grpAlgOutput = new Group(comAlgMain, SWT.NONE);
		GridData gd_grpAlgOutput = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_grpAlgOutput.widthHint = 300;
		gd_grpAlgOutput.heightHint = 200;
		grpAlgOutput.setLayoutData(gd_grpAlgOutput);
		grpAlgOutput.setText(Messages.DesView_2);
		grpAlgOutput.setLayout(new GridLayout());

		// Tabfolder for various tables
		tfAlgOutput = new TabFolder(grpAlgOutput, SWT.NONE);
		tfAlgOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tfAlgOutput.setLayout(new GridLayout());

		tbtmAlgM0M17 = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgM0M17.setText(Messages.DesView_32);

		Composite comAlgOutputM0M17 = new Composite(tfAlgOutput, SWT.NONE);
		tbtmAlgM0M17.setControl(comAlgOutputM0M17);
		comAlgOutputM0M17.setLayout(new GridLayout());
		comAlgOutputM0M17.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tblAlgOutputM0M17 = new Table(comAlgOutputM0M17, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		tblAlgOutputM0M17.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tblAlgOutputM0M17.setHeaderVisible(true);
		tblAlgOutputM0M17.setLinesVisible(true);
		
		TableColumn[] tblAlgOutputM0M17Column = new TableColumn[34];

		tblAlgOutputM0M17Column[0] = new TableColumn(tblAlgOutputM0M17, SWT.CENTER);
		
		for (int i = 1; i < 33; i++) {
			tblAlgOutputM0M17Column[i] = new TableColumn(tblAlgOutputM0M17, SWT.CENTER);
			tblAlgOutputM0M17Column[i].setText(i + "");
		}
		
		tblAlgOutputM0M17Column[33] = new TableColumn(tblAlgOutputM0M17, SWT.CENTER);
		tblAlgOutputM0M17Column[33].setText(Messages.DesView_34);
		
		TableItem[] tblAlgM0M17Item = new TableItem[18];
		
		for (int i = 0; i < 18; i++) {
			tblAlgM0M17Item[i] = new TableItem(tblAlgOutputM0M17, SWT.BORDER);
			tblAlgM0M17Item[i].setText(0, "m[" + (i) + "]");
		}

		resizeTable(tblAlgOutputM0M17);
		colorTable(tblAlgOutputM0M17, 4);

		
		tbtmAlgDES = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgDES.setText(Messages.DesView_37);

		comAlgOutputDeskpei = new Composite(tfAlgOutput, SWT.NONE);
		comAlgOutputDeskpei.setLayout(new GridLayout());
		tbtmAlgDES.setControl(comAlgOutputDeskpei);

		tblAlgOutputDeskpei = new Table(comAlgOutputDeskpei, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		tblAlgOutputDeskpei.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tblAlgOutputDeskpei.setHeaderVisible(true);
		tblAlgOutputDeskpei.setLinesVisible(true);
		
		TableColumn[] tblAlgOutputDESColumn = new TableColumn[66];
		
		tblAlgOutputDESColumn[0] = new TableColumn(tblAlgOutputDeskpei, SWT.CENTER);
		
		tblAlgOutputDESColumn[1] = new TableColumn(tblAlgOutputDeskpei, SWT.CENTER);
		tblAlgOutputDESColumn[1].setText(Messages.DesView_39);
		
		for (int i = 1; i < 65; i++) {
			tblAlgOutputDESColumn[i] = new TableColumn(tblAlgOutputDeskpei, SWT.CENTER);
			tblAlgOutputDESColumn[i].setText(i + "");
		}
		
		TableItem[] tblAlgDESItem = new TableItem[65];
		
		tblAlgDESItem[0] = new TableItem(tblAlgOutputDeskpei, SWT.BORDER);
		tblAlgDESItem[0].setText(0, Messages.DesView_40);
		
		for (int i = 1; i < 65; i++) {
			tblAlgDESItem[i] = new TableItem(tblAlgOutputDeskpei, SWT.BORDER);
			tblAlgDESItem[i].setText(0, "DES(" + i + ")");
		}
		
		resizeTable(tblAlgOutputDeskpei);
		colorTable(tblAlgOutputDeskpei, 4);

		// Create Hamming Tab
		tbtmAlgHamming = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgHamming.setText(Messages.DesView_43);
		
		ScrolledComposite sc = new ScrolledComposite(tfAlgOutput, SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		comAlgOutputHamming = new Composite(sc, SWT.NONE);
		comAlgOutputHamming.setLayout(new GridLayout(3, false));
		
		tbtmAlgHamming.setControl(sc);

		Label lblAlgOutputHamming1 = new Label(comAlgOutputHamming, SWT.CENTER);
		lblAlgOutputHamming1.setText(Messages.DesView_44);
		lblAlgOutputHamming1.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));
		
		Label lblAlgOutputHamming2 = new Label(comAlgOutputHamming, SWT.CENTER);
		lblAlgOutputHamming2.setText(Messages.DesView_45);
		lblAlgOutputHamming2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		Label spacer1 = new Label(comAlgOutputHamming, SWT.NONE);
		spacer1.setLayoutData(new GridData());

		tblAlgOutputHamming1 = new Table(comAlgOutputHamming, SWT.BORDER);
		tblAlgOutputHamming1.setHeaderVisible(false);
		tblAlgOutputHamming1.setLinesVisible(true);
		tblAlgOutputHamming1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		TableColumn[] tblAlgOutputHamming1Column = new TableColumn[8];
		
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming1Column[i] = new TableColumn(tblAlgOutputHamming1, SWT.CENTER);
		}
		
		TableItem[] tblAlgOutputHamming1TableItem = new TableItem[8];
		
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming1TableItem[i] = new TableItem(tblAlgOutputHamming1, SWT.BORDER);
		}
		
		resizeTable(tblAlgOutputHamming1);
		colorTable(tblAlgOutputHamming1, 4);

		tblAlgOutputHamming2 = new Table(comAlgOutputHamming, SWT.BORDER);
		tblAlgOutputHamming2.setHeaderVisible(false);
		tblAlgOutputHamming2.setLinesVisible(true);
		tblAlgOutputHamming2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		TableColumn[] tblAlgOutputHamming2Column = new TableColumn[8];
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming2Column[i] = new TableColumn(tblAlgOutputHamming2, SWT.CENTER);
		}
		
		TableItem[] tblAlgOutputHamming2TableItem = new TableItem[8];
		
		for (int i = 0; i < 8; i++) {
			tblAlgOutputHamming2TableItem[i] = new TableItem(tblAlgOutputHamming2, SWT.BORDER);
		}
		
		resizeTable(tblAlgOutputHamming2);
		colorTable(tblAlgOutputHamming2, 4);
		
		Label spacer2 = new Label(comAlgOutputHamming, SWT.NONE);
		spacer2.setLayoutData(new GridData());

		tblAlgOutputHammingDes = new Text(comAlgOutputHamming, SWT.WRAP | SWT.MULTI);
		GridData gd_tblAlgOutputHammingDes = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gd_tblAlgOutputHammingDes.widthHint = 450;
		tblAlgOutputHammingDes.setLayoutData(gd_tblAlgOutputHammingDes);
		tblAlgOutputHammingDes.setText(Messages.DesView_47);
		
		sc.setMinSize(comAlgOutputHamming.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sc.setContent(comAlgOutputHamming);

		// Roundkeys
		tbtmAlgRoundkeys = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgRoundkeys.setText(Messages.DesView_51);

		comAlgOutputRoundkeys = new Composite(tfAlgOutput, SWT.NONE);
		comAlgOutputRoundkeys.setLayout(new GridLayout());
		
		tbtmAlgRoundkeys.setControl(comAlgOutputRoundkeys);		

		tblAlgOutputRoundkeys = new Table(comAlgOutputRoundkeys, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		tblAlgOutputRoundkeys.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tblAlgOutputRoundkeys.setHeaderVisible(true);
		tblAlgOutputRoundkeys.setLinesVisible(true);
		
		TableColumn[] tblAlgOutputRoundkeysColumn = new TableColumn[49];
		
		tblAlgOutputRoundkeysColumn[0] = new TableColumn(tblAlgOutputRoundkeys, SWT.CENTER);
		
		for (int i = 1; i < 49; i++) {
			tblAlgOutputRoundkeysColumn[i] = new TableColumn(tblAlgOutputRoundkeys, SWT.CENTER);
			tblAlgOutputRoundkeysColumn[i].setText(i + "");
		}
		
		TableItem[] tblAlgRoundkeysItem = new TableItem[16];
		
		for (int i = 0; i < 16; i++) {
			tblAlgRoundkeysItem[i] = new TableItem(tblAlgOutputRoundkeys, SWT.BORDER);
			tblAlgRoundkeysItem[i].setText(0, "K[" + (i + 1) + "]");
		}
		
		resizeTable(tblAlgOutputRoundkeys);
		colorTable(tblAlgOutputRoundkeys, 4);

		tbtmAlgCDMatrix = new TabItem(tfAlgOutput, SWT.NONE);
		tbtmAlgCDMatrix.setText(Messages.DesView_55);

		comAlgOutputCDMatrix = new Composite(tfAlgOutput, SWT.NONE);
		comAlgOutputCDMatrix.setLayout(new GridLayout());
		tbtmAlgCDMatrix.setControl(comAlgOutputCDMatrix);
				
		tblAlgOutputCDMatrix = new Table(comAlgOutputCDMatrix, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		tblAlgOutputCDMatrix.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tblAlgOutputCDMatrix.setLinesVisible(true);
		
		TableColumn[] tblAlgOutputCDMatrixColumn = new TableColumn[29];

		tblAlgOutputCDMatrixColumn[0] = new TableColumn(tblAlgOutputCDMatrix, SWT.CENTER);
		
		for (int i = 1; i < 29; i++) {
			tblAlgOutputCDMatrixColumn[i] = new TableColumn(tblAlgOutputCDMatrix, SWT.CENTER);
		}

		TableItem[] tblAlgCDMatrixItem = new TableItem[34];
		
		for (int i = 0; i < 34; i++) {
			tblAlgCDMatrixItem[i] = new TableItem(tblAlgOutputCDMatrix, SWT.BORDER);
		}
		
		for (int i = 0, j = 0; i < 34; i = i + 2, j++) {
			tblAlgCDMatrixItem[i].setText(0, "C[" + j + "]");
			tblAlgCDMatrixItem[i + 1].setText(0, "D[" + j + "]");
		}
		
		resizeTable(tblAlgOutputCDMatrix);
		colorTable(tblAlgOutputCDMatrix, 3);
		
		tfAlgOutput.notifyListeners(SWT.Selection, new Event());

		grpAlgStatus = new Group(comAlgMain, SWT.NONE);
		grpAlgStatus.setText(Messages.DesView_4);
		grpAlgStatus.setLayout(new GridLayout());
		GridData gd_grpAlgStatus = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gd_grpAlgStatus.heightHint = 110;
		grpAlgStatus.setLayoutData(gd_grpAlgStatus);

		txtAlgStatus = new StyledText(grpAlgStatus, SWT.MULTI | SWT.V_SCROLL);
		txtAlgStatus.setEditable(false);
		txtAlgStatus.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtAlgStatus.setAlwaysShowScrollBars(false);
		txtAlgStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tabAlg.setControl(comAlgMain);
	}
	


	/**
	 * Creates the "Anti- /Fixed Point Study" tab and all its GUI elements
	 */
	private void createFPointsTab() {
		tabFPoints = new TabItem(tfolder_1, SWT.NONE);
		tabFPoints.setText(Messages.DesView_132);

		comFPointsMain = new Composite(tfolder_1, SWT.NONE);
		comFPointsMain.setLayout(new GridLayout(3, false));
		
		createTitleAndDescription(comFPointsMain);
		
		createInformationGroup(comFPointsMain, Messages.DesView_166);

		// Group holding Input Tab -> parameter and start buttons
		grpFPointsInput = new Group(comFPointsMain, SWT.NONE);
		grpFPointsInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		GridLayout gl_grpFPointsInput = new GridLayout(5, false);
		gl_grpFPointsInput.horizontalSpacing = 10;
		grpFPointsInput.setLayout(gl_grpFPointsInput);
		grpFPointsInput.setText(Messages.DesView_133);
		
		//Composite holding the two radio Buttons under Target
		targetComposite = new Composite(grpFPointsInput, SWT.NONE);
		GridLayout gl_targetComposite = new GridLayout();
		gl_targetComposite.marginHeight = 0;
		gl_grpFPointsInput.marginWidth = 0;
		targetComposite.setLayout(gl_targetComposite);
		targetComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
		
		// Target heading
		lblFPointsInputTarget = new Label(targetComposite, SWT.NONE);
		lblFPointsInputTarget.setText(Messages.DesView_137);
		lblFPointsInputTarget.setFont(FontService.getLargeBoldFont());
		
		//Radio Button Fixpoint
		btnFPointsFixedpoint = new Button(targetComposite, SWT.RADIO);
		btnFPointsFixedpoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnFPointsFixedpoint.setText(Messages.DesView_138);
		btnFPointsFixedpoint.setSelection(true);
		btnFPointsFixedpoint.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnFPointsK0.setText("k[0]");
				btnFPointsK1.setText("k[10]");
				btnFPointsK2.setText("k[5]");
				btnFPointsK3.setText("k[15]");
				lblFPointsResult.setText(Messages.DesView_143);
				txtFPointsResult.setText("");
			}
		});
		
		//Anti-fixed point
		btnFPointsAntifixedPoint = new Button(targetComposite, SWT.RADIO);
		btnFPointsAntifixedPoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnFPointsAntifixedPoint.setText(Messages.DesView_144);
		btnFPointsAntifixedPoint.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnFPointsK0.setText("k[3]");
				btnFPointsK1.setText("k[9]");
				btnFPointsK2.setText("k[6]");
				btnFPointsK3.setText("k[11]");
				lblFPointsResult.setText(Messages.DesView_144);
				txtFPointsResult.setText("");
			}
		});
		
		// Composite holding the radio buttons k[1] etc
		keyComposite = new Composite(grpFPointsInput, SWT.NONE);
		GridLayout gl_keyComposite = new GridLayout(2, false);
		gl_keyComposite.marginHeight = 0;
		gl_keyComposite.marginWidth = 0;
		gl_keyComposite.horizontalSpacing = 10;
		keyComposite.setLayout(gl_keyComposite);
		keyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
		
		// Key heading
		lblFPointsInputKey = new Label(keyComposite, SWT.NONE);
		lblFPointsInputKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		lblFPointsInputKey.setFont(FontService.getLargeBoldFont());
		lblFPointsInputKey.setText(Messages.DesView_150);
		
		//K[0]
		btnFPointsK0 = new Button(keyComposite, SWT.RADIO);
		btnFPointsK0.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnFPointsK0.setText("k[0]");
		btnFPointsK0.setSelection(true);
		
		//K[10]
		btnFPointsK1 = new Button(keyComposite, SWT.RADIO);
		btnFPointsK1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnFPointsK1.setText("k[10]");
		
		//K[5]
		btnFPointsK2 = new Button(keyComposite, SWT.RADIO);
		btnFPointsK2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnFPointsK2.setText("k[5]");
		
		//K[15]
		btnFPointsK3 = new Button(keyComposite, SWT.RADIO);
		btnFPointsK3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		btnFPointsK3.setText("k[15]");
		
		dataComposite = new Composite(grpFPointsInput, SWT.NONE);
		GridLayout gl_dataComposite = new GridLayout(2, false);
		gl_dataComposite.marginHeight = 0;
		gl_dataComposite.marginWidth = 0;
		dataComposite.setLayout(gl_dataComposite);
		dataComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 3));
		
		// Data heading
		lblFPointsInputData = new Label(dataComposite, SWT.NONE);
		lblFPointsInputData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		lblFPointsInputData.setFont(FontService.getLargeBoldFont());
		lblFPointsInputData.setText(Messages.DesView_155);
		
		//m[8] (8 hexdigits):
		lblFPointsInputM8 = new Label(dataComposite, SWT.NONE);
		lblFPointsInputM8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 2, 1));
		lblFPointsInputM8.setText(Messages.DesView_159);
		
		txtFPointsInputM8 = new Text(dataComposite, SWT.BORDER);
		txtFPointsInputM8.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		txtFPointsInputM8.setText(Messages.DesView_156);
		txtFPointsInputM8.setTextLimit(8);
		txtFPointsInputM8.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				lblFPointsInputM8cur.setText("(" + txtFPointsInputM8.getText().length() + ")");
			}
		});
		
		//(8)
		lblFPointsInputM8cur = new Label(dataComposite, SWT.NONE);
		lblFPointsInputM8cur.setText("(8)");
		lblFPointsInputM8cur.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		

		// vertical separator line
		lblFPointsInputSeparator = new Label(grpFPointsInput, SWT.SEPARATOR | SWT.VERTICAL);
		lblFPointsInputSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3));

		//Button evaluate
		btnFPointsEvaluate = new Button(grpFPointsInput, SWT.NONE);
		btnFPointsEvaluate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
		btnFPointsEvaluate.setFont(FontService.getLargeBoldFont());
		btnFPointsEvaluate.setText(Messages.DesView_207);
		btnFPointsEvaluate.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				dESCon.FPoints_In_FixedPoints = btnFPointsFixedpoint.getSelection();
				dESCon.FPoints_In_M8 = txtFPointsInputM8.getText().toUpperCase();
				String err = "";
				
				if (btnFPointsFixedpoint.getSelection()) {
					if (btnFPointsK0.getSelection()) 
						dESCon.FPoints_In_selectedKey = 0;
					if (btnFPointsK2.getSelection()) 
						dESCon.FPoints_In_selectedKey = 5;
					if (btnFPointsK1.getSelection()) 
						dESCon.FPoints_In_selectedKey = 10;
					if (btnFPointsK3.getSelection()) 
						dESCon.FPoints_In_selectedKey = 15;
				}
				
				if (btnFPointsAntifixedPoint.getSelection()) {
					if (btnFPointsK0.getSelection())
						dESCon.FPoints_In_selectedKey = 0;
					if (btnFPointsK2.getSelection())
						dESCon.FPoints_In_selectedKey = 6;
					if (btnFPointsK1.getSelection()) 
						dESCon.FPoints_In_selectedKey = 9;
					if (btnFPointsK3.getSelection())
						dESCon.FPoints_In_selectedKey = 11;
				}

				if (dESCon.FPointsStudy() == 0) {
					txtFPointsStatus.append(getCurrentTime() + Messages.DesView_218 + txtFPointsInputM8.getText() + "\n");
					txtFPointsStatus.setTopIndex(txtFPointsStatus.getLineCount());
					
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
							err += dESCon.errMsg[i] + ",";
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtFPointsStatus.append(getCurrentTime() + "\\" + err + "\n");
					txtFPointsStatus.setTopIndex(txtFPointsStatus.getLineCount());
				}
			}
		});
		
		//Reset
		btnFPointsReset = new Button(grpFPointsInput, SWT.PUSH);
		btnFPointsReset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		btnFPointsReset.setText(Messages.DesView_196);
		btnFPointsReset.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnFPointsK0.setSelection(true);
				btnFPointsK0.setText("K[0]");
				btnFPointsK1.setSelection(false);
				btnFPointsK1.setText("K[10]");
				btnFPointsK2.setSelection(false);
				btnFPointsK2.setText("K[5]");
				btnFPointsK3.setSelection(false);
				btnFPointsK3.setText("K[15]");
				txtFPointsInputM8.setText(Messages.DesView_156);
				tblFPointsOutputAFP.clearAll();
				colorTable(tblFPointsOutputAFP, 4);
				txtFPointsResult.setText("");

				TableItem[] tblItems = tblFPointsOutputAFP.getItems();
				for (int i = 0; i < 10; i++) {
					tblItems[i].setText(0, "m[" + (i + 8) + "]");
				}

				txtFPointsStatus.append(getCurrentTime() + Messages.DesView_206 + "\n");
				txtFPointsStatus.setTopIndex(txtFPointsStatus.getLineCount());
			}
		});

		// Group holding results
		grpFPointsResult = new Group(comFPointsMain, SWT.NONE);
		grpFPointsResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		grpFPointsResult.setLayout(new GridLayout());
		grpFPointsResult.setText(Messages.DesView_60);
		
		lblFPointsResult = new Label(grpFPointsResult, SWT.NONE);
		lblFPointsResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		lblFPointsResult.setFont(FontService.getLargeBoldFont());
		lblFPointsResult.setText(Messages.DesView_138);
		
		txtFPointsResult = new StyledText(grpFPointsResult, SWT.WRAP | SWT.CENTER);
		txtFPointsResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		txtFPointsResult.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtFPointsResult.setEditable(false);
		txtFPointsResult.setCaret(null);

		// Group holding the internal states tables
		grpFPointsOutput = new Group(comFPointsMain, SWT.NONE);
		GridData gd_grpFPointsOutput = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_grpFPointsOutput.widthHint = 300;
		gd_grpFPointsOutput.heightHint = 200;
		grpFPointsOutput.setLayoutData(gd_grpFPointsOutput);
		grpFPointsOutput.setLayout(new GridLayout());
		grpFPointsOutput.setText(Messages.DesView_134);
		
		tblFPointsOutputAFP = new Table(grpFPointsOutput, SWT.NONE);
		tblFPointsOutputAFP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tblFPointsOutputAFP.setHeaderVisible(true);
		tblFPointsOutputAFP.setLinesVisible(true);

		TableColumn[] tblFPointsColumn = new TableColumn[34];
		tblFPointsColumn[0] = new TableColumn(tblFPointsOutputAFP, SWT.CENTER);
		
		for (int i = 1; i < 33; i++) {
			tblFPointsColumn[i] = new TableColumn(tblFPointsOutputAFP, SWT.CENTER);
			tblFPointsColumn[i].setText(i + "");
		}
		tblFPointsColumn[33] = new TableColumn(tblFPointsOutputAFP, SWT.CENTER);
		tblFPointsColumn[33].setText(Messages.DesView_162);

		TableItem[] tblFPointsItem = new TableItem[10];
		for (int i = 0; i < 10; i++) {
			tblFPointsItem[i] = new TableItem(tblFPointsOutputAFP, SWT.BORDER);
			tblFPointsItem[i].setText(0, "m[" + (i + 8) + "]");
		}
		
		resizeTable(tblFPointsOutputAFP);
		colorTable(tblFPointsOutputAFP, 4);

		// group holding the statusbox
		grpFPointsStatus = new Group(comFPointsMain, SWT.NONE);
		grpFPointsStatus.setLayout(new GridLayout());
		GridData gd_grpFPointsStatus = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gd_grpFPointsStatus.heightHint = 110;
		grpFPointsStatus.setLayoutData(gd_grpFPointsStatus);
		grpFPointsStatus.setText(Messages.DesView_136);

		// statusbox
		txtFPointsStatus = new StyledText(grpFPointsStatus, SWT.MULTI | SWT.V_SCROLL);
		txtFPointsStatus.setEditable(false);
		txtFPointsStatus.setCaret(null);
		txtFPointsStatus.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtFPointsStatus.setAlwaysShowScrollBars(false);
		txtFPointsStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tabFPoints.setControl(comFPointsMain);
	}


	
	
	private void createSBoxTab() {

		tabSBox = new TabItem(tfolder_1, SWT.NONE);
		tabSBox.setText(Messages.DesView_224);

		comSBoxMain = new Composite(tfolder_1, SWT.NONE);
		comSBoxMain.setLayout(new GridLayout(3, false));
		
		createTitleAndDescription(comSBoxMain);
		
		createInformationGroup(comSBoxMain, Messages.DesView_244);

		// Group containing Input-Tab -> parameter/start buttons
		grpSBoxInput = new Group(comSBoxMain, SWT.NONE);
		grpSBoxInput.setLayoutData(new GridData(SWT.DEFAULT, SWT.FILL, false, false, 2, 1));
		GridLayout gl_grpSBoxInput = new GridLayout(7, false);
		gl_grpSBoxInput.horizontalSpacing = 10;
		grpSBoxInput.setLayout(gl_grpSBoxInput);
		grpSBoxInput.setText(Messages.DesView_225);
		
		// Data heading
		lblSBoxInputData = new Label(grpSBoxInput, SWT.NONE);
		lblSBoxInputData.setText(Messages.DesView_229);
		lblSBoxInputData.setFont(FontService.getLargeBoldFont());
		lblSBoxInputData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		// Series description Label
		lblSBoxInputSeries = new Label(grpSBoxInput, SWT.NONE);
		lblSBoxInputSeries.setText(Messages.DesView_235);
		lblSBoxInputSeries.setFont(FontService.getLargeBoldFont());
		lblSBoxInputSeries.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));

		// Vertical separator
		lblSBoxInputSeparator = new Label(grpSBoxInput, SWT.SEPARATOR | SWT.VERTICAL);
		lblSBoxInputSeparator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3));

		//Button Evaluate
		btnSBoxEvaluate = new Button(grpSBoxInput, SWT.NONE);
		btnSBoxEvaluate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2));
		btnSBoxEvaluate.setFont(FontService.getLargeBoldFont());
		btnSBoxEvaluate.setText(Messages.DesView_274);
		btnSBoxEvaluate.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				dESCon.SBox_In_Deltap = txtSBoxInputDeltap.getText().toUpperCase();
				String err = "";

				if (dESCon.SBoxStudy() == 0) {
					txtSBoxStatus.append(getCurrentTime() + Messages.DesView_277
							+ txtSBoxInputDeltap.getText() + "\n");
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount());
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
							err += dESCon.errMsg[i] + ",";
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtSBoxStatus.append(getCurrentTime() + "\\" + err + "\n");
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount());
				}
			}
		});
		
		//Evaluate series
		btnSBoxEvaluateSeries = new Button(grpSBoxInput, SWT.NONE);
		btnSBoxEvaluateSeries.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 2));
		btnSBoxEvaluateSeries.setFont(FontService.getLargeBoldFont());
		btnSBoxEvaluateSeries.setText(Messages.DesView_284);
		btnSBoxEvaluateSeries.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String err = "";
				dESCon.SBox_In_Deltap = txtSBoxInputDeltap.getText().toUpperCase();

				if (dESCon.SBoxStudy() == 0) {
					txtSBoxStatus.append(getCurrentTime() + Messages.DesView_287
							+ txtSBoxInputDeltap.getText() + Messages.DesView_288
							+ slSBoxInputSeriesCount.getSelection() + Messages.DesView_289
							+ slSBoxInputSeriesTime.getSelection() + "\n");
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount());
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
						tblSBoxOutput.update();
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
							err += dESCon.errMsg[i] + ",";
						} else {
							err += dESCon.errMsg[i];
						}
					}
					txtSBoxStatus.append(getCurrentTime() + "\\" + err + "\n");
					txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount());
				}
			}
		});
		
		// Delta_p description
		lblSBoxInputDeltap = new Label(grpSBoxInput, SWT.NONE);
		lblSBoxInputDeltap.setText(Messages.DesView_230);
		lblSBoxInputDeltap.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		// Slider (time) description Label
		lblSBoxInputSeriesTime = new Label(grpSBoxInput, SWT.NONE);
		lblSBoxInputSeriesTime.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		// Slider (rounds) description Label
		lblSBoxInputSeriesCount = new Label(grpSBoxInput, SWT.NONE);
		lblSBoxInputSeriesCount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		// Delta_p input box
		txtSBoxInputDeltap = new Text(grpSBoxInput, SWT.BORDER);
		txtSBoxInputDeltap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtSBoxInputDeltap.setTextLimit(16);
		txtSBoxInputDeltap.setText("1111111111111111");
		txtSBoxInputDeltap.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				lblSBoxInputDeltapCur
						.setText("(" + txtSBoxInputDeltap.getText().length() + ")");
			}
		});
		
		// Current character counter
		lblSBoxInputDeltapCur = new Label(grpSBoxInput, SWT.NONE);
		lblSBoxInputDeltapCur.setText("(16)");
		
		// Slider (time)
		slSBoxInputSeriesTime = new Slider(grpSBoxInput, SWT.NONE);
		slSBoxInputSeriesTime.setMinimum(1);
		slSBoxInputSeriesTime.setMaximum(1000);
		slSBoxInputSeriesTime.setSelection(100);
		slSBoxInputSeriesTime.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				lblSBoxInputSeriesTime.setText(Messages.DesView_236 + slSBoxInputSeriesTime.getSelection());
			}
		});
		
		// Slider (rounds)
		slSBoxInputSeriesCount = new Slider(grpSBoxInput, SWT.NONE);
		slSBoxInputSeriesCount.setMinimum(1);
		slSBoxInputSeriesCount.setMaximum(200);
		slSBoxInputSeriesCount.setSelection(20);
		slSBoxInputSeriesCount.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				lblSBoxInputSeriesCount.setText(Messages.DesView_238 + slSBoxInputSeriesCount.getSelection());
			}
		});
		
		lblSBoxInputSeriesTime.setText(Messages.DesView_237 + slSBoxInputSeriesTime.getSelection());
		lblSBoxInputSeriesCount.setText(Messages.DesView_239 + slSBoxInputSeriesCount.getSelection());
		
		//Reset
		btnSBoxReset = new Button(grpSBoxInput, SWT.NONE);
		btnSBoxReset.setText(Messages.DesView_265);
		btnSBoxReset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnSBoxReset.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				txtSBoxStatus.append(getCurrentTime() + Messages.DesView_267 + "\n");
				txtSBoxStatus.setTopIndex(txtSBoxStatus.getLineCount());
				txtSBoxInputDeltap.setText("1111111111111111");
				lblSBoxOutputCurStep.setText(Messages.DesView_269);
				intSBoxOutputCurStep = 0;
				lblSBoxOutputP.setText(Messages.DesView_270);
				lblSBoxOutputK.setText(Messages.DesView_271);
				slSBoxInputSeriesCount.setSelection(20);
				slSBoxInputSeriesTime.setSelection(100);
				lblSBoxInputSeriesCount.setText(Messages.DesView_272);
				lblSBoxInputSeriesTime.setText(Messages.DesView_273);
				txtSBoxInputDeltap.setEnabled(true);
				slSBoxInputSeriesTime.setEnabled(true);
				slSBoxInputSeriesCount.setEnabled(true);
				tblSBoxOutput.clearAll();
			}
		});

		// group containing output
		grpSBoxOutput = new Group(comSBoxMain, SWT.NONE);
		GridData gd_grpSBoxOutput = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_grpSBoxOutput.widthHint = 300;
		gd_grpSBoxOutput.heightHint = 200;
		grpSBoxOutput.setLayoutData(gd_grpSBoxOutput);
		grpSBoxOutput.setLayout(new GridLayout(2, false));
		grpSBoxOutput.setText(Messages.DesView_226);

		// Output Tab
		tblSBoxOutput = new Table(grpSBoxOutput, SWT.NO_FOCUS);
		tblSBoxOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		tblSBoxOutput.setHeaderVisible(true);
		tblSBoxOutput.setLinesVisible(true);

		TableColumn[] tblSBoxColumn = new TableColumn[8];
		for (int i = 0; i < 8; i++) {
			tblSBoxColumn[i] = new TableColumn(tblSBoxOutput, SWT.BORDER | SWT.CENTER);
			tblSBoxColumn[i].setText(Messages.DesView_240 + (i + 1));
		}

		TableItem[] tblSBoxItem = new TableItem[16];
		for (int i = 0; i < 16; i++) {
			tblSBoxItem[i] = new TableItem(tblSBoxOutput, SWT.BORDER);
		}
		
		resizeTable(tblSBoxOutput);

		// Group beside table showing current step, p, and x
		grpSBoxResult = new Group(grpSBoxOutput, SWT.NONE);
		grpSBoxResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		grpSBoxResult.setLayout(new GridLayout());

		lblSBoxOutputCurStep = new Label(grpSBoxResult, SWT.NONE);
		lblSBoxOutputCurStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSBoxOutputCurStep.setText(Messages.DesView_241);

		lblSBoxOutputP = new Label(grpSBoxResult, SWT.NONE);
		lblSBoxOutputP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSBoxOutputP.setText(Messages.DesView_242);

		lblSBoxOutputK = new Label(grpSBoxResult, SWT.NONE);
		lblSBoxOutputK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblSBoxOutputK.setText(Messages.DesView_243);

		// group for statusbox
		grpSBoxStatus = new Group(comSBoxMain, SWT.NONE);
		GridData gd_grpSBoxStatus = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
		gd_grpSBoxStatus.heightHint = 110;
		grpSBoxStatus.setLayoutData(gd_grpSBoxStatus);
		grpSBoxStatus.setText(Messages.DesView_228);
		grpSBoxStatus.setLayout(new GridLayout());

		txtSBoxStatus = new StyledText(grpSBoxStatus, SWT.MULTI | SWT.V_SCROLL);
		txtSBoxStatus.setEditable(false);
		txtSBoxStatus.setCaret(null);
		txtSBoxStatus.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		txtSBoxStatus.setAlwaysShowScrollBars(false);
		txtSBoxStatus.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		tabSBox.setControl(comSBoxMain);
	}

	// Helper Functions
	private void fillTable(Table table, int start_row, int end_row, int start_column, int end_column, int data[][]) {
		
		TableItem ti = null;

		for (int r = start_row; r <= end_row; r++) {
			for (int c = start_column; c <= end_column; c++) {
				ti = table.getItem(r);
				ti.setText(c, Integer.toString((data[r - start_row][c - start_column])));
			}
		}
	}

	/**
	 * 
	 * @param table
	 * @param mode 
	 * 0: Coloring S-Box Tab;
	 * 1: Coloring FPoints Tab, Alg Tab M0M17 Output, Alg Tab DES(k,p+e_i);
	 * 2: ? --  does nothing;
	 * 3: Coloring Alg Tab CD Matrix Output;
	 * 4: All lines alternately white gray;
	 */
	private void colorTable(Table table, int mode) {
		TableItem ti = null;
		TableItem tiOld = null;
		Color yellow = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
		Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		Color curColor = null;
		int i = 0, j = 0;

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
					} else {
						ti.setForeground(c, black);
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
			//Color all lines alternating white and gray 
		} else if (mode == 4) {
			for (int k = 0; k < table.getItemCount(); k++) {
				for (int l = 0; l < table.getColumnCount(); l++) {
					if ((k % 2) == 0) {
						table.getItems()[k].setBackground(l, white);
					} else {
						table.getItems()[k].setBackground(l, gray);
					}
				}
			}
		}
	}
	
	/**
	 * Fits the column size to the content
	 * @param table The table that should be resized
	 */
	private void resizeTable(Table table) {
		for (TableColumn tc : table.getColumns()) {
			tc.pack();
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
