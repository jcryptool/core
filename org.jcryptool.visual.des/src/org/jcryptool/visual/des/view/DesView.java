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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
    // Auto Gen
    public static final String ID = "org.jcryptool.visual.des.view.DesView"; //$NON-NLS-1$

    // Controller
    private DESController DESCon = null;

    // Layout Components
    // Algorithm Study
    ScrolledComposite wrapper = null;
    TabFolder tfolder = null;
    TabItem tabAlg = null;;
    Label lblAlgTitle = null;
    Text lblAlgInformationText = null;
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
    Text txtAlgStatus = null;
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
    Composite comAlgInputSepOut = null;
    Label lblAlgInputSeparator = null;
    Label lblAlgInputCipherOut = null;
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
    Text txtFPointsStatus = null;
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
    Group grpFPointsHeader = null;
    Table tblFPointsOutputAFP = null;
    Composite comFPointsOutput = null;
    Label lblFPointsOutputAFPoint = null;
    StyledText txtFPointsInformation = null;

    // SBox Study
    TabItem tabSBox = null;
    Composite comSBox = null;
    Composite comSBoxHeader = null;
    Label lblSBox = null;
    Label lblSBoxTitle = null;
    Label lblSBoxInformationText = null;
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
    Text txtSBoxStatus = null;
    Composite comSBoxInput = null;
    Label lblSBoxInputData = null;
    Label lblSBoxInputDeltap = null;
    Label lblSBoxInputDeltapCur = null;
    Text txtSBoxInputDeltap = null;
    Button btnSBoxEvaluate = null;
    Button btnSBoxReset = null;
    Button btnSBoxEvaluateSeries = null;
    Table tblSBoxOutput = null;
    Composite comSBoxInputDataSeries = null;
    Slider slSBoxInputSeriesCount = null;
    Slider slSBoxInputSeriesTime = null;
    Label lblSBoxInputSeries = null;
    Label lblSBoxInputSeriesTime = null;
    Label lblSBoxInputSeriesCount = null;
    int intSBoxOutputCurStep = 0;
    Label lblSBoxOutputCurStep = null;
    Label lblSBoxOutputP = null;
    Label lblSBoxOutputK = null;
    Group grpSBoxHeader = null;
    Label lblFPointsInputM8cur = null;
    StyledText txtSBoxInformation = null;
    private Composite parent;

    public DesView() {
    }

    /**
     * Create contents of the view part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        parent.setLayout(new FillLayout());

        // Wrapper Start
        wrapper = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
        wrapper.setExpandHorizontal(true);
        wrapper.setExpandVertical(true);

        // Folder
        tfolder = new TabFolder(wrapper, SWT.TOP);

        // Algorithm Study Tab
        createAlgTab();
        // Fixed Points Study tab
        createFPointsTab();
        // S-Box Tab
        createSBoxTab();

        // Wrapper Final
        wrapper.setMinSize(1100, 780);
        wrapper.setContent(tfolder);

        // Create DES Controller
        DESCon = new DESController();

        createActions();
        initializeToolBar();
        initializeMenu();
    }

    public void dispose() {
        super.dispose();
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        // Create the actions
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {

    }

    /**
     * Initialize the menu.
     */
    private void initializeMenu() {

    }

    @Override
    public void setFocus() {
        // Set the focus
    }

    private void createAlgTab() {
        tabAlg = new TabItem(tfolder, SWT.NONE);
        tabAlg.setText(Messages.DesView_0);

        // Tab Layout
        comAlg = new Composite(tfolder, SWT.NONE);
        comAlg.setLayout(new FillLayout());

        comAlgMain = new Composite(comAlg, SWT.NONE);
        comAlgMain.setLayout(new FormLayout());

        // create title
        lblAlgTitle = new Label(comAlgMain, SWT.NONE);
        FormData fd_lblAlgTitle = new FormData();
        fd_lblAlgTitle.left = new FormAttachment(0, 10);
        fd_lblAlgTitle.top = new FormAttachment(0, 10);
        lblAlgTitle.setLayoutData(fd_lblAlgTitle);
        lblAlgTitle.setFont(FontService.getHeaderFont());
        lblAlgTitle.setBounds(0, 3, 650, 15);
        lblAlgTitle.setText(Messages.DesView_title);

        // create title - text
        lblAlgInformationText = new Text(comAlgMain, SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
        FormData fd_lblAlgInformationText = new FormData();
        fd_lblAlgInformationText.top = new FormAttachment(lblAlgTitle, 10);
        fd_lblAlgInformationText.left = new FormAttachment(0, 10);
        lblAlgInformationText.setLayoutData(fd_lblAlgInformationText);
        lblAlgInformationText.setBounds(0, 3, 650, 15);
        lblAlgInformationText.setText(Messages.DesView_text);

        comAlgMainLeft = new Composite(comAlgMain, SWT.NONE);
        comAlgMainLeft.setLayout(new FillLayout(SWT.HORIZONTAL));
        FormData fd_comAlgMainLeft = new FormData(250, 500);
        fd_comAlgMainLeft.left = new FormAttachment(0, 10);
        fd_comAlgMainLeft.top = new FormAttachment(lblAlgInformationText, 10);
        comAlgMainLeft.setLayoutData(fd_comAlgMainLeft);

        comAlgMainRight = new Composite(comAlgMain, SWT.NONE);
        comAlgMainRight.setLayout(new FormLayout());
        FormData fd_comAlgMainRight = new FormData(600, 500);
        fd_comAlgMainRight.left = new FormAttachment(comAlgMainLeft, 10);
        fd_comAlgMainRight.right = new FormAttachment(100, -10);
        fd_comAlgMainRight.top = new FormAttachment(lblAlgInformationText, 10);
        comAlgMainRight.setLayoutData(fd_comAlgMainRight);

        comAlgMainDown = new Composite(comAlgMain, SWT.NONE);
        FormData fd_comAlgMainDown = new FormData(0, 100);
        fd_comAlgMainDown.top = new FormAttachment(comAlgMainRight, 10);
        fd_comAlgMainDown.left = new FormAttachment(0, 10);
        fd_comAlgMainDown.right = new FormAttachment(100, -10);
        comAlgMainDown.setLayout(new FillLayout(SWT.HORIZONTAL));
        comAlgMainDown.setLayoutData(fd_comAlgMainDown);

        comAlgMainRightUp = new Composite(comAlgMainRight, SWT.NONE);
        comAlgMainRightUp.setLayout(new FillLayout(SWT.HORIZONTAL));
        FormData fd_comAlgMainRightUp = new FormData();
        fd_comAlgMainRightUp.top = new FormAttachment(0);
        fd_comAlgMainRightUp.left = new FormAttachment(0);
        fd_comAlgMainRightUp.right = new FormAttachment(100);
        comAlgMainRightUp.setLayoutData(fd_comAlgMainRightUp);

        comAlgMainRightDown = new Composite(comAlgMainRight, SWT.NONE);
        FormData fd_comAlgMainRightDown = new FormData(0, 150);
        fd_comAlgMainRightDown.right = new FormAttachment(100);
        fd_comAlgMainRightDown.top = new FormAttachment(comAlgMainRightUp, 10);
        fd_comAlgMainRightDown.left = new FormAttachment(0);
        fd_comAlgMainRightDown.bottom = new FormAttachment(100);
        comAlgMainRightDown.setLayout(new FillLayout(SWT.HORIZONTAL));
        comAlgMainRightDown.setLayoutData(fd_comAlgMainRightDown);

        grpAlgInput = new Group(comAlgMainRightUp, SWT.NONE);
        grpAlgInput.setText(Messages.DesView_1);
        grpAlgInput.setLayout(new FormLayout());

        grpAlgOutput = new Group(comAlgMainRightDown, SWT.NONE);
        grpAlgOutput.setText(Messages.DesView_2);
        grpAlgOutput.setLayout(new FillLayout());

        grpAlgInformation = new Group(comAlgMainLeft, SWT.NONE);
        grpAlgInformation.setText(Messages.DesView_3);
        grpAlgInformation.setLayout(new FormLayout());

        grpAlgStatus = new Group(comAlgMainDown, SWT.NONE);
        grpAlgStatus.setText(Messages.DesView_4);
        grpAlgStatus.setLayout(new FormLayout());

        txtAlgStatus = new Text(grpAlgStatus, SWT.MULTI | SWT.V_SCROLL);
        txtAlgStatus.setEditable(false);
        FormData fd_txtAlgStatus = new FormData();
        fd_txtAlgStatus.bottom = new FormAttachment(100, -10);
        fd_txtAlgStatus.right = new FormAttachment(100, -10);
        fd_txtAlgStatus.top = new FormAttachment(0, 10);
        fd_txtAlgStatus.left = new FormAttachment(0, 10);
        txtAlgStatus.setLayoutData(fd_txtAlgStatus);

        // Input Tab
        comAlgInputMode = new Composite(grpAlgInput, SWT.NONE);
        FormData fd_comAlgInputMode = new FormData();
        fd_comAlgInputMode.left = new FormAttachment(0, 10);
        fd_comAlgInputMode.top = new FormAttachment(0, 0);
        fd_comAlgInputMode.bottom = new FormAttachment(100);
        comAlgInputMode.setLayoutData(fd_comAlgInputMode);

        btnAlgDecrypt = new Button(comAlgInputMode, SWT.RADIO);
        btnAlgDecrypt.setText(Messages.DesView_5);
        btnAlgDecrypt.setBounds(0, 40, 86, 22);

        btnAlgDecrypt.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                lblAlgInputPlaintext.setText(Messages.DesView_6);
                lblAlgInputCipherOut.setText(Messages.DesView_7);
            }

        });

        btnAlgEncrypt = new Button(comAlgInputMode, SWT.RADIO);
        btnAlgEncrypt.setText(Messages.DesView_8);
        btnAlgEncrypt.setBounds(0, 20, 86, 22);
        btnAlgEncrypt.setSelection(true);

        btnAlgEncrypt.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                lblAlgInputPlaintext.setText(Messages.DesView_9);
                lblAlgInputCipherOut.setText(Messages.DesView_10);
            }

        });

        lblAlgMode = new Label(comAlgInputMode, SWT.NONE);
        lblAlgMode.setBounds(0, 3, 60, 15);
        lblAlgMode.setText(Messages.DesView_11);
        lblAlgMode.setFont(FontService.getLargeBoldFont());

        comAlgInputKey = new Composite(grpAlgInput, SWT.NONE);
        fd_comAlgInputMode.right = new FormAttachment(comAlgInputKey, -6);
        FormData fd_comAlgInputKey = new FormData();
        fd_comAlgInputKey.left = new FormAttachment(0, 102);
        fd_comAlgInputKey.top = new FormAttachment(0);
        fd_comAlgInputKey.bottom = new FormAttachment(100);
        comAlgInputKey.setLayoutData(fd_comAlgInputKey);

        btnAlgK0 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK0.setText(Messages.DesView_12);
        btnAlgK0.setBounds(0, 20, 43, 22);
        btnAlgK0.setSelection(true);

        lblAlgInputKey = new Label(comAlgInputKey, SWT.NONE);
        lblAlgInputKey.setBounds(0, 3, 60, 15);
        lblAlgInputKey.setText(Messages.DesView_13);
        lblAlgInputKey.setFont(FontService.getLargeBoldFont());

        btnAlgK3 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK3.setText(Messages.DesView_14);
        btnAlgK3.setBounds(50, 20, 43, 22);

        btnAlgK5 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK5.setText(Messages.DesView_15);
        btnAlgK5.setBounds(110, 20, 43, 22);

        btnAlgK6 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK6.setText(Messages.DesView_16);
        btnAlgK6.setBounds(170, 20, 43, 22);

        btnAlgK9 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK9.setText(Messages.DesView_17);
        btnAlgK9.setBounds(0, 40, 43, 22);

        btnAlgK10 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK10.setText(Messages.DesView_18);
        btnAlgK10.setBounds(50, 40, 56, 22);

        btnAlgK12 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK12.setText(Messages.DesView_19);
        btnAlgK12.setBounds(110, 40, 56, 22);

        btnAlgK15 = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgK15.setText(Messages.DesView_20);
        btnAlgK15.setBounds(170, 40, 55, 22);

        btnAlgManual = new Button(comAlgInputKey, SWT.RADIO);
        btnAlgManual.setText(Messages.DesView_21);
        btnAlgManual.setBounds(230, 23, 163, 15);

        btnAlgManual.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (btnAlgManual.getSelection()) {
                    txtAlgKeyManual.setEnabled(true);
                } else {
                    txtAlgKeyManual.setEnabled(false);
                }
            }
        });

        lblAlgInputManualCurChar = new Label(comAlgInputKey, SWT.NONE);
        lblAlgInputManualCurChar.setBounds(379, 43, 25, 15);
        lblAlgInputManualCurChar.setText(Messages.DesView_22);

        txtAlgKeyManual = new Text(comAlgInputKey, SWT.BORDER);
        txtAlgKeyManual.setBounds(230, 41, 140, 20);
        txtAlgKeyManual.setTextLimit(16);
        txtAlgKeyManual.setEnabled(false);

        txtAlgKeyManual.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                lblAlgInputManualCurChar.setText(Messages.DesView_23 + txtAlgKeyManual.getText().length()
                        + Messages.DesView_24);
            }

        });

        comAlgInputData = new Composite(grpAlgInput, SWT.NONE);
        FormData fd_comAlgInputData = new FormData();
        fd_comAlgInputData.top = new FormAttachment(0);
        fd_comAlgInputData.bottom = new FormAttachment(100);
        fd_comAlgInputData.left = new FormAttachment(comAlgInputKey, 25);
        comAlgInputData.setLayoutData(fd_comAlgInputData);

        lblAlgInputData = new Label(comAlgInputData, SWT.NONE);
        lblAlgInputData.setText(Messages.DesView_25);
        lblAlgInputData.setBounds(0, 3, 51, 15);
        lblAlgInputData.setFont(FontService.getLargeBoldFont());

        txtAlgInputData = new Text(comAlgInputData, SWT.BORDER);
        txtAlgInputData.setBounds(0, 41, 119, 20);
        txtAlgInputData.setTextLimit(16);
        txtAlgInputData.setText(Messages.DesView_26);

        txtAlgInputData.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                lblAlgInputDataCurChar.setText(Messages.DesView_27 + txtAlgInputData.getText().length()
                        + Messages.DesView_28);
            }

        });

        lblAlgInputDataCurChar = new Label(comAlgInputData, SWT.NONE);
        lblAlgInputDataCurChar.setBounds(125, 41, 30, 15);
        lblAlgInputDataCurChar.setText(Messages.DesView_29);

        lblAlgInputPlaintext = new Label(comAlgInputData, SWT.NONE);
        lblAlgInputPlaintext.setBounds(0, 22, 150, 15);
        lblAlgInputPlaintext.setText(Messages.DesView_30);

        comAlgInputSepOut = new Composite(grpAlgInput, SWT.NONE);
        FormData fd_comAlgInputSepOut = new FormData();
        fd_comAlgInputSepOut.top = new FormAttachment(0);
        fd_comAlgInputSepOut.bottom = new FormAttachment(100);
        fd_comAlgInputSepOut.left = new FormAttachment(comAlgInputData, 10);
        fd_comAlgInputSepOut.right = new FormAttachment(100);
        comAlgInputSepOut.setLayoutData(fd_comAlgInputSepOut);
        comAlgInputSepOut.setLayout(new FormLayout());

        lblAlgInputSeparator = new Label(comAlgInputSepOut, SWT.SEPARATOR | SWT.VERTICAL);
        FormData fd_lblAlgInputSeparator = new FormData();
        fd_lblAlgInputSeparator.top = new FormAttachment(0, 10);
        fd_lblAlgInputSeparator.bottom = new FormAttachment(100, -10);
        fd_lblAlgInputSeparator.left = new FormAttachment(0);
        lblAlgInputSeparator.setLayoutData(fd_lblAlgInputSeparator);

        lblAlgInputCipherOut = new Label(comAlgInputSepOut, SWT.NONE);
        FormData fd_lblAlgInputCipherOut = new FormData();
        fd_lblAlgInputCipherOut.top = new FormAttachment(0, 23);
        fd_lblAlgInputCipherOut.bottom = new FormAttachment(100);
        fd_lblAlgInputCipherOut.left = new FormAttachment(lblAlgInputSeparator, 15);
        fd_lblAlgInputCipherOut.right = new FormAttachment(100, 5);
        lblAlgInputCipherOut.setLayoutData(fd_lblAlgInputCipherOut);
        lblAlgInputCipherOut.setText(Messages.DesView_31);

        // Output Tab
        tfAlgOutput = new TabFolder(grpAlgOutput, SWT.NONE);

        tbtmAlgM0M17 = new TabItem(tfAlgOutput, SWT.NONE);
        tbtmAlgM0M17.setText(Messages.DesView_32);

        Composite comAlgOutputM0M17 = new Composite(tfAlgOutput, SWT.NONE);
        tbtmAlgM0M17.setControl(comAlgOutputM0M17);
        comAlgOutputM0M17.setLayout(new FormLayout());

        tblAlgOutputM0M17 = new Table(comAlgOutputM0M17, SWT.MULTI | SWT.V_SCROLL | SWT.VIRTUAL);
        FormData fd_tblAlgOutputM0M17 = new FormData();
        fd_tblAlgOutputM0M17.left = new FormAttachment(0, 0);
        fd_tblAlgOutputM0M17.right = new FormAttachment(100, 0);
        fd_tblAlgOutputM0M17.top = new FormAttachment(0, 0);
        fd_tblAlgOutputM0M17.bottom = new FormAttachment(100, 0);
        tblAlgOutputM0M17.setLayoutData(fd_tblAlgOutputM0M17);
        tblAlgOutputM0M17.setHeaderVisible(true);
        tblAlgOutputM0M17.setLinesVisible(true);

        TableColumn[] tblAlgOutputM0M17Column = new TableColumn[34];
        tblAlgOutputM0M17Column[0] = new TableColumn(tblAlgOutputM0M17, SWT.NONE);
        tblAlgOutputM0M17Column[0].setWidth(45);
        tblAlgOutputM0M17Column[0].setAlignment(SWT.CENTER);
        for (int i = 1; i < 33; i++) {
            tblAlgOutputM0M17Column[i] = new TableColumn(tblAlgOutputM0M17, SWT.NONE);
            tblAlgOutputM0M17Column[i].setWidth(25);
            tblAlgOutputM0M17Column[i].setAlignment(SWT.CENTER);
            tblAlgOutputM0M17Column[i].setText(i + Messages.DesView_33);

        }
        tblAlgOutputM0M17Column[33] = new TableColumn(tblAlgOutputM0M17, SWT.NONE);
        tblAlgOutputM0M17Column[33].setWidth(70);
        tblAlgOutputM0M17Column[33].setAlignment(SWT.CENTER);
        tblAlgOutputM0M17Column[33].setText(Messages.DesView_34);

        TableItem[] tblAlgM0M17Item = new TableItem[18];
        for (int i = 0; i < 18; i++) {
            tblAlgM0M17Item[i] = new TableItem(tblAlgOutputM0M17, SWT.BORDER);
        }
        cleanTable(tblAlgOutputM0M17);
        for (int i = 0; i < 18; i++) {
            tblAlgM0M17Item[i].setText(0, Messages.DesView_35 + (i) + Messages.DesView_36);
        }

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

        TableColumn[] tblAlgOutputDESColumn = new TableColumn[66];
        tblAlgOutputDESColumn[0] = new TableColumn(tblAlgOutputDeskpei, SWT.NONE);
        tblAlgOutputDESColumn[0].setWidth(70);
        tblAlgOutputDESColumn[0].setAlignment(SWT.CENTER);
        for (int i = 1; i < 65; i++) {
            tblAlgOutputDESColumn[i] = new TableColumn(tblAlgOutputDeskpei, SWT.NONE);
            tblAlgOutputDESColumn[i].setWidth(25);
            tblAlgOutputDESColumn[i].setAlignment(SWT.CENTER);
            tblAlgOutputDESColumn[i].setText(i + Messages.DesView_38);

        }
        tblAlgOutputDESColumn[65] = new TableColumn(tblAlgOutputDeskpei, SWT.NONE);
        tblAlgOutputDESColumn[65].setWidth(50);
        tblAlgOutputDESColumn[65].setAlignment(SWT.CENTER);
        tblAlgOutputDESColumn[65].setText(Messages.DesView_39);

        TableItem[] tblAlgDESItem = new TableItem[65];
        for (int i = 0; i < 65; i++) {
            tblAlgDESItem[i] = new TableItem(tblAlgOutputDeskpei, SWT.BORDER);
        }
        cleanTable(tblAlgOutputDeskpei);
        tblAlgDESItem[0].setText(0, Messages.DesView_40);
        for (int i = 1; i < 65; i++) {
            tblAlgDESItem[i].setText(0, Messages.DesView_41 + i + Messages.DesView_42);
        }

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
        TableItem[] tblAlgRoundkeysItem = new TableItem[16];
        for (int i = 0; i < 16; i++) {
            tblAlgRoundkeysItem[i] = new TableItem(tblAlgOutputRoundkeys, SWT.BORDER);
        }
        cleanTable(tblAlgOutputRoundkeys);
        for (int i = 0; i < 16; i++) {
            tblAlgRoundkeysItem[i].setText(0, Messages.DesView_53 + (i + 1) + Messages.DesView_54);
        }

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

        TableColumn[] tblAlgOutputCDMatrixColumn = new TableColumn[29];
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
        cleanTable(tblAlgOutputCDMatrix);
        for (int i = 0, j = 0; i < 34; i = i + 2, j++) {
            tblAlgCDMatrixItem[i].setText(0, Messages.DesView_56 + j + Messages.DesView_57);
            tblAlgCDMatrixItem[i + 1].setText(0, Messages.DesView_58 + j + Messages.DesView_59);
        }

        // Information Tab
        txtAlgInformation = new StyledText(grpAlgInformation, SWT.READ_ONLY | SWT.MULTI);
        FormData fd_txtAlgInformation = new FormData();
        fd_txtAlgInformation.right = new FormAttachment(100, -10);
        fd_txtAlgInformation.top = new FormAttachment(0, 10);
        fd_txtAlgInformation.left = new FormAttachment(0, 10);
        fd_txtAlgInformation.bottom = new FormAttachment(100, -10);
        txtAlgInformation.setLayoutData(fd_txtAlgInformation);

        txtAlgInformation.setText(Messages.DesView_60 + Messages.DesView_61 + Messages.DesView_62 + Messages.DesView_63
                + Messages.DesView_64 + Messages.DesView_65 + Messages.DesView_66 + Messages.DesView_67
                + Messages.DesView_68 + Messages.DesView_69 + Messages.DesView_70 + Messages.DesView_71
                + Messages.DesView_72 + Messages.DesView_73 + Messages.DesView_74 + Messages.DesView_75
                + Messages.DesView_76 + Messages.DesView_77 + Messages.DesView_78 + Messages.DesView_79
                + Messages.DesView_80 + Messages.DesView_81 + Messages.DesView_82 + Messages.DesView_83
                + Messages.DesView_84 + Messages.DesView_85 + Messages.DesView_86);

        txtAlgInformation.setFont(FontService.getNormalFont());

        // Action Buttons
        btnAlgReset = new Button(comAlgMain, SWT.NONE);
        FormData fd_btnAlgReset = new FormData();
        fd_btnAlgReset.top = new FormAttachment(comAlgMainDown, 10);
        fd_btnAlgReset.left = new FormAttachment(0, 10);
        btnAlgReset.setLayoutData(fd_btnAlgReset);
        btnAlgReset.setText(Messages.DesView_87);

        btnAlgReset.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                btnAlgEncrypt.setSelection(true);
                btnAlgDecrypt.setSelection(false);
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
                lblAlgInputPlaintext.setText(Messages.DesView_89);
                txtAlgInputData.setText(Messages.DesView_90);
                lblAlgInputCipherOut.setText(Messages.DesView_91);
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

            }
        });

        btnAlgEvaluate = new Button(comAlgMain, SWT.NONE);
        FormData fd_btnAlgEvaluate = new FormData();
        fd_btnAlgEvaluate.top = new FormAttachment(comAlgMainDown, 10);
        fd_btnAlgEvaluate.left = new FormAttachment(btnAlgReset, 10);
        btnAlgEvaluate.setLayoutData(fd_btnAlgEvaluate);
        btnAlgEvaluate.setText(Messages.DesView_107);

        btnAlgEvaluate.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String sKey = Messages.DesView_108;
                String sMode = Messages.DesView_109;
                String err = Messages.DesView_110;

                DESCon.Alg_In_Data = txtAlgInputData.getText();
                if (btnAlgEncrypt.getSelection()) {
                    DESCon.Alg_In_Mode = 0;
                    sMode = Messages.DesView_111;
                } else {
                    DESCon.Alg_In_Mode = 1;
                    sMode = Messages.DesView_112;
                }
                if (btnAlgK0.getSelection()) {
                    DESCon.Alg_In_selectedKey = 0;
                    sKey = Messages.DesView_113;
                } else if (btnAlgK3.getSelection()) {
                    DESCon.Alg_In_selectedKey = 3;
                    sKey = Messages.DesView_114;
                } else if (btnAlgK5.getSelection()) {
                    DESCon.Alg_In_selectedKey = 5;
                    sKey = Messages.DesView_115;
                } else if (btnAlgK6.getSelection()) {
                    DESCon.Alg_In_selectedKey = 6;
                    sKey = Messages.DesView_116;
                } else if (btnAlgK9.getSelection()) {
                    DESCon.Alg_In_selectedKey = 9;
                    sKey = Messages.DesView_117;
                } else if (btnAlgK10.getSelection()) {
                    DESCon.Alg_In_selectedKey = 10;
                    sKey = Messages.DesView_118;
                } else if (btnAlgK12.getSelection()) {
                    DESCon.Alg_In_selectedKey = 12;
                    sKey = Messages.DesView_119;
                } else if (btnAlgK15.getSelection()) {
                    DESCon.Alg_In_selectedKey = 15;
                    sKey = Messages.DesView_120;
                } else if (btnAlgManual.getSelection()) {
                    DESCon.Alg_In_selectedKey = 16;
                    DESCon.Alg_In_manualKey = txtAlgKeyManual.getText();
                    sKey = Messages.DesView_121 + txtAlgKeyManual.getText() + Messages.DesView_122;
                }

                if (DESCon.AlgorithmStudy() == 0) {
                    if (btnAlgEncrypt.getSelection()) {
                        lblAlgInputCipherOut.setText(Messages.DesView_123 + DESCon.Alg_Out_EncDecResult);
                    } else {
                        lblAlgInputCipherOut.setText(Messages.DesView_124 + DESCon.Alg_Out_EncDecResult);
                    }
                    fillTable(tblAlgOutputM0M17, 0, 17, 1, 32, DESCon.Alg_Out_M0M17);
                    colorTable(tblAlgOutputM0M17, 1);
                    for (int i = 1; i < tblAlgOutputM0M17.getItemCount(); i++) {
                        tblAlgOutputM0M17.getItem(i).setText(tblAlgOutputM0M17.getColumnCount() - 1,
                                Integer.toString(DESCon.Alg_Out_M0M17_Dist[i]));
                    }

                    fillTable(tblAlgOutputDeskpei, 0, 64, 1, 64, DESCon.Alg_Out_cipherMatrix);
                    // doColoring(0, 64, 1, 64, output2, true, 3, DES_C.DES_action_type);
                    for (int i = 1; i < tblAlgOutputDeskpei.getItemCount(); i++) {
                        tblAlgOutputDeskpei.getItem(i).setText(tblAlgOutputDeskpei.getColumnCount() - 1,
                                Integer.toString(DESCon.Alg_Out_cipherMatrix_Dist[i]));
                    }
                    colorTable(tblAlgOutputDeskpei, 1);

                    fillTable(tblAlgOutputRoundkeys, 0, 15, 1, 48, DESCon.Alg_Out_Roundkeys);

                    fillTable(tblAlgOutputCDMatrix, 0, 33, 1, 28, DESCon.Alg_Out_CDMatrix);
                    colorTable(tblAlgOutputCDMatrix, 3);

                    fillTable(tblAlgOutputHamming1, 0, 7, 0, 7, DESCon.Alg_Out_DistMatrix1);
                    fillTable(tblAlgOutputHamming2, 0, 7, 0, 7, DESCon.Alg_Out_DistMatrix2);

                    txtAlgStatus.append(Messages.DesView_125 + getCurrentTime() + Messages.DesView_126 + sMode
                            + Messages.DesView_127 + sKey + Messages.DesView_128 + txtAlgInputData.getText());
                } else {
                    for (int i = 0; i < DESCon.errMsg.length; i++) {
                        if (i != DESCon.errMsg.length - 1) {
                            err += DESCon.errMsg[i] + Messages.DesView_129;
                        } else {
                            err += DESCon.errMsg[i];
                        }
                    }
                    txtAlgStatus.append(Messages.DesView_130 + getCurrentTime() + Messages.DesView_131 + err);
                }
            }
        });

        tabAlg.setControl(comAlg);

    }

    private void createFPointsTab() {
        tabFPoints = new TabItem(tfolder, SWT.NONE);
        tabFPoints.setText(Messages.DesView_132);

        // Tab Layout
        comFPoints = new Composite(tfolder, SWT.NONE);
        comFPoints.setLayout(new FillLayout());

        comFPointsMain = new Composite(comFPoints, SWT.NONE);
        comFPointsMain.setLayout(new FormLayout());

        // create title
        lblFPointsTitle = new Label(comFPointsMain, SWT.NONE);
        FormData fd_lblFPointsTitle = new FormData();
        fd_lblFPointsTitle.left = new FormAttachment(0, 10);
        fd_lblFPointsTitle.top = new FormAttachment(0, 10);
        lblFPointsTitle.setLayoutData(fd_lblFPointsTitle);
        lblFPointsTitle.setFont(FontService.getHeaderFont());
        lblFPointsTitle.setBounds(0, 3, 650, 15);
        lblFPointsTitle.setText(Messages.DesView_title);

        // create title - text
        lblFPointsInformationText = new Label(comFPointsMain, SWT.NONE);
        FormData fd_lblFPointsInformationText = new FormData();
        fd_lblFPointsInformationText.top = new FormAttachment(lblFPointsTitle, 10);
        fd_lblFPointsInformationText.left = new FormAttachment(0, 10);
        lblFPointsInformationText.setLayoutData(fd_lblFPointsInformationText);
        lblFPointsInformationText.setBounds(0, 3, 650, 15);
        lblFPointsInformationText.setText(Messages.DesView_text);

        comFPointsMainLeft = new Composite(comFPointsMain, SWT.NONE);
        comFPointsMainLeft.setLayout(new FillLayout(SWT.HORIZONTAL));
        FormData fd_comFPointsMainLeft = new FormData(250, 510);
        fd_comFPointsMainLeft.left = new FormAttachment(0, 10);
        fd_comFPointsMainLeft.top = new FormAttachment(lblFPointsInformationText, 10);
        comFPointsMainLeft.setLayoutData(fd_comFPointsMainLeft);

        comFPointsMainRight = new Composite(comFPointsMain, SWT.NONE);
        comFPointsMainRight.setLayout(new FormLayout());
        FormData fd_comFPointsMainRight = new FormData(600,510);
        fd_comFPointsMainRight.top = new FormAttachment(lblFPointsInformationText, 10);
        fd_comFPointsMainRight.left = new FormAttachment(comFPointsMainLeft, 10);
        fd_comFPointsMainRight.right = new FormAttachment(100, -10);
        comFPointsMainRight.setLayoutData(fd_comFPointsMainRight);

        comFPointsMainDown = new Composite(comFPointsMain, SWT.NONE);
        FormData fd_comFPointsMainDown = new FormData(0, 100);
        fd_comFPointsMainDown.top = new FormAttachment(comFPointsMainLeft, 10);
        fd_comFPointsMainDown.left = new FormAttachment(0, 10);
        fd_comFPointsMainDown.right = new FormAttachment(100, -10);
        comFPointsMainDown.setLayout(new FillLayout(SWT.HORIZONTAL));
        comFPointsMainDown.setLayoutData(fd_comFPointsMainDown);

        comFPointsMainRightUp = new Composite(comFPointsMainRight, SWT.NONE);
        comFPointsMainRightUp.setLayout(new FillLayout(SWT.HORIZONTAL));
        FormData fd_comFPointsMainRightUp = new FormData();
        fd_comFPointsMainRightUp.top = new FormAttachment(0);
        fd_comFPointsMainRightUp.left = new FormAttachment(0);
        fd_comFPointsMainRightUp.right = new FormAttachment(100);
        comFPointsMainRightUp.setLayoutData(fd_comFPointsMainRightUp);

        comFPointsMainRightDown = new Composite(comFPointsMainRight, SWT.NONE);
        FormData fd_comFPointsMainRightDown = new FormData(0, 150);
        fd_comFPointsMainRightDown.right = new FormAttachment(100);
        fd_comFPointsMainRightDown.top = new FormAttachment(comFPointsMainRightUp, 10);
        fd_comFPointsMainRightDown.left = new FormAttachment(0);
        fd_comFPointsMainRightDown.bottom = new FormAttachment(100);
        comFPointsMainRightDown.setLayout(new FillLayout(SWT.HORIZONTAL));
        comFPointsMainRightDown.setLayoutData(fd_comFPointsMainRightDown);

        grpFPointsInput = new Group(comFPointsMainRightUp, SWT.NONE);
        grpFPointsInput.setText(Messages.DesView_133);
        grpFPointsInput.setLayout(new FormLayout());

        grpFPointsOutput = new Group(comFPointsMainRightDown, SWT.NONE);
        grpFPointsOutput.setText(Messages.DesView_134);
        grpFPointsOutput.setLayout(new FormLayout());

        grpFPointsInformation = new Group(comFPointsMainLeft, SWT.NONE);
        grpFPointsInformation.setText(Messages.DesView_135);
        grpFPointsInformation.setLayout(new FormLayout());

        grpFPointsStatus = new Group(comFPointsMainDown, SWT.NONE);
        grpFPointsStatus.setText(Messages.DesView_136);
        grpFPointsStatus.setLayout(new FormLayout());

        txtFPointsStatus = new Text(grpFPointsStatus, SWT.MULTI | SWT.V_SCROLL);
        txtFPointsStatus.setEditable(false);
        FormData fd_txtFPointsStatus = new FormData();
        fd_txtFPointsStatus.bottom = new FormAttachment(100, -10);
        fd_txtFPointsStatus.right = new FormAttachment(100, -10);
        fd_txtFPointsStatus.top = new FormAttachment(0, 10);
        fd_txtFPointsStatus.left = new FormAttachment(0, 10);
        txtFPointsStatus.setLayoutData(fd_txtFPointsStatus);

        // Input Tab
        comFPointsInputTarget = new Composite(grpFPointsInput, SWT.NONE);
        FormData fd_comFPointsInputTarget = new FormData();
        fd_comFPointsInputTarget.left = new FormAttachment(0, 10);
        fd_comFPointsInputTarget.top = new FormAttachment(0, 0);
        fd_comFPointsInputTarget.bottom = new FormAttachment(100);
        comFPointsInputTarget.setLayoutData(fd_comFPointsInputTarget);

        lblFPointsInputTarget = new Label(comFPointsInputTarget, SWT.NONE);
        lblFPointsInputTarget.setBounds(0, 0, 60, 15);
        lblFPointsInputTarget.setText(Messages.DesView_137);
        lblFPointsInputTarget.setFont(FontService.getLargeBoldFont());

        btnFPointsFixedpoint = new Button(comFPointsInputTarget, SWT.RADIO);
        btnFPointsFixedpoint.setBounds(0, 20, 87, 22);
        btnFPointsFixedpoint.setText(Messages.DesView_138);
        btnFPointsFixedpoint.setSelection(true);

        btnFPointsFixedpoint.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                btnFPointsK0.setText(Messages.DesView_139);
                btnFPointsK1.setText(Messages.DesView_140);
                btnFPointsK2.setText(Messages.DesView_141);
                btnFPointsK3.setText(Messages.DesView_142);
                lblFPointsOutputAFPoint.setText(Messages.DesView_143);

            }
        });

        btnFPointsAntifixedPoint = new Button(comFPointsInputTarget, SWT.RADIO);
        btnFPointsAntifixedPoint.setBounds(0, 40, 108, 22);
        btnFPointsAntifixedPoint.setText(Messages.DesView_144);

        btnFPointsAntifixedPoint.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                btnFPointsK0.setText(Messages.DesView_145);
                btnFPointsK1.setText(Messages.DesView_146);
                btnFPointsK2.setText(Messages.DesView_147);
                btnFPointsK3.setText(Messages.DesView_148);
                lblFPointsOutputAFPoint.setText(Messages.DesView_149);

            }
        });

        comFPointsInputKey = new Composite(grpFPointsInput, SWT.NONE);
        FormData fd_comFPointsInputKey = new FormData();
        fd_comFPointsInputKey.left = new FormAttachment(0, 142);
        fd_comFPointsInputKey.top = new FormAttachment(0);
        fd_comFPointsInputKey.bottom = new FormAttachment(100);
        comFPointsInputKey.setLayoutData(fd_comFPointsInputKey);

        lblFPointsInputKey = new Label(comFPointsInputKey, SWT.NONE);
        lblFPointsInputKey.setBounds(0, 0, 60, 15);
        lblFPointsInputKey.setText(Messages.DesView_150);
        lblFPointsInputKey.setFont(FontService.getLargeBoldFont());

        btnFPointsK0 = new Button(comFPointsInputKey, SWT.RADIO);
        btnFPointsK0.setBounds(0, 20, 51, 22);
        btnFPointsK0.setText(Messages.DesView_151);
        btnFPointsK0.setSelection(true);

        btnFPointsK1 = new Button(comFPointsInputKey, SWT.RADIO);
        btnFPointsK1.setBounds(0, 40, 51, 22);
        btnFPointsK1.setText(Messages.DesView_152);

        btnFPointsK2 = new Button(comFPointsInputKey, SWT.RADIO);
        btnFPointsK2.setBounds(55, 20, 49, 22);
        btnFPointsK2.setText(Messages.DesView_153);

        btnFPointsK3 = new Button(comFPointsInputKey, SWT.RADIO);
        btnFPointsK3.setBounds(55, 40, 60, 22);
        btnFPointsK3.setText(Messages.DesView_154);

        comFPointsInputM8 = new Composite(grpFPointsInput, SWT.NONE);
        fd_comFPointsInputKey.right = new FormAttachment(comFPointsInputM8, -6);
        FormData fd_comFPointsInputM8 = new FormData();
        fd_comFPointsInputM8.left = new FormAttachment(0, 273);
        fd_comFPointsInputM8.top = new FormAttachment(comFPointsInputTarget, 0, SWT.TOP);
        comFPointsInputM8.setLayoutData(fd_comFPointsInputM8);

        lblFPointsInputData = new Label(comFPointsInputM8, SWT.NONE);
        lblFPointsInputData.setText(Messages.DesView_155);
        lblFPointsInputData.setBounds(0, 0, 42, 13);
        lblFPointsInputData.setFont(FontService.getLargeBoldFont());

        txtFPointsInputM8 = new Text(comFPointsInputM8, SWT.BORDER);
        txtFPointsInputM8.setBounds(0, 40, 120, 20);
        txtFPointsInputM8.setText(Messages.DesView_156);

        txtFPointsInputM8.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                lblFPointsInputM8cur.setText(Messages.DesView_157 + txtFPointsInputM8.getText().length()
                        + Messages.DesView_158);
            }
        });

        lblFPointsInputM8 = new Label(comFPointsInputM8, SWT.NONE);
        lblFPointsInputM8.setText(Messages.DesView_159);
        txtFPointsInputM8.setTextLimit(8);
        lblFPointsInputM8.setBounds(0, 20, 120, 15);

        lblFPointsInputM8cur = new Label(comFPointsInputM8, SWT.NONE);
        lblFPointsInputM8cur.setText(Messages.DesView_160);
        lblFPointsInputM8cur.setBounds(125, 43, 24, 13);

        // Output Tab

        comFPointsOutput = new Composite(grpFPointsOutput, SWT.NONE);
        comFPointsOutput.setLayout(new FillLayout());
        FormData fd_comFPointsOutput = new FormData();
        fd_comFPointsOutput.left = new FormAttachment(0, 10);
        fd_comFPointsOutput.top = new FormAttachment(0, 10);
        fd_comFPointsOutput.bottom = new FormAttachment(0, 250);
        fd_comFPointsOutput.right = new FormAttachment(100, -10);
        comFPointsOutput.setLayoutData(fd_comFPointsOutput);

        tblFPointsOutputAFP = new Table(comFPointsOutput, SWT.NO_FOCUS | SWT.VIRTUAL);
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

        lblFPointsOutputAFPoint = new Label(grpFPointsOutput, SWT.None);
        FormData fd_lblFPointsOutputAFPoint = new FormData();
        fd_lblFPointsOutputAFPoint.left = new FormAttachment(0, 10);
        fd_lblFPointsOutputAFPoint.right = new FormAttachment(100, -10);
        fd_lblFPointsOutputAFPoint.top = new FormAttachment(comFPointsOutput, 10);
        lblFPointsOutputAFPoint.setLayoutData(fd_lblFPointsOutputAFPoint);
        lblFPointsOutputAFPoint.setText(Messages.DesView_165);

        // Information Tab
        txtFPointsInformation = new StyledText(grpFPointsInformation, SWT.READ_ONLY | SWT.MULTI);
        FormData fd_txtFPointsInformation = new FormData();
        fd_txtFPointsInformation.right = new FormAttachment(100, -10);
        fd_txtFPointsInformation.top = new FormAttachment(0, 10);
        fd_txtFPointsInformation.left = new FormAttachment(0, 10);
        fd_txtFPointsInformation.bottom = new FormAttachment(100, -10);
        txtFPointsInformation.setLayoutData(fd_txtFPointsInformation);

        txtFPointsInformation.setText(Messages.DesView_166 + Messages.DesView_167 + Messages.DesView_168
                + Messages.DesView_169 + Messages.DesView_170 + Messages.DesView_171 + Messages.DesView_172
                + Messages.DesView_173 + Messages.DesView_174 + Messages.DesView_175 + Messages.DesView_176
                + Messages.DesView_177 + Messages.DesView_178 + Messages.DesView_179 + Messages.DesView_180
                + Messages.DesView_181 + Messages.DesView_182 + Messages.DesView_183 + Messages.DesView_184
                + Messages.DesView_185 + Messages.DesView_186 + Messages.DesView_187 + Messages.DesView_188
                + Messages.DesView_189 + Messages.DesView_190 + Messages.DesView_191 + Messages.DesView_192
                + Messages.DesView_193 + Messages.DesView_194 + Messages.DesView_195);

        txtFPointsInformation.setFont(FontService.getNormalFont());

        // Action Buttons
        btnFPointsReset = new Button(comFPointsMain, SWT.NONE);
        FormData fd_btnFPointsReset = new FormData();
        fd_btnFPointsReset.top = new FormAttachment(comFPointsMainDown, 10);
        fd_btnFPointsReset.left = new FormAttachment(0, 10);
        btnFPointsReset.setLayoutData(fd_btnFPointsReset);
        btnFPointsReset.setText(Messages.DesView_196);

        btnFPointsReset.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                btnFPointsFixedpoint.setSelection(true);
                btnFPointsAntifixedPoint.setSelection(false);
                btnFPointsK0.setSelection(true);
                btnFPointsK0.setText(Messages.DesView_197);
                btnFPointsK1.setSelection(false);
                btnFPointsK1.setText(Messages.DesView_198);
                btnFPointsK2.setSelection(false);
                btnFPointsK2.setText(Messages.DesView_199);
                btnFPointsK3.setSelection(false);
                btnFPointsK3.setText(Messages.DesView_200);
                txtFPointsInputM8.setText(Messages.DesView_201);
                cleanTable(tblFPointsOutputAFP);
                lblFPointsOutputAFPoint.setText(Messages.DesView_202);

                TableItem[] tblItems = tblFPointsOutputAFP.getItems();
                for (int i = 0; i < 10; i++) {
                    tblItems[i].setText(0, Messages.DesView_203 + (i + 8) + Messages.DesView_204);
                }

                txtFPointsStatus.append(Messages.DesView_205 + getCurrentTime() + Messages.DesView_206);
            }
        });

        btnFPointsEvaluate = new Button(comFPointsMain, SWT.NONE);
        FormData fd_btnFPointsEvaluate = new FormData();
        fd_btnFPointsEvaluate.top = new FormAttachment(comFPointsMainDown, 10);
        fd_btnFPointsEvaluate.left = new FormAttachment(btnFPointsReset, 10);
        btnFPointsEvaluate.setLayoutData(fd_btnFPointsEvaluate);
        btnFPointsEvaluate.setText(Messages.DesView_207);

        btnFPointsEvaluate.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                DESCon.FPoints_In_FixedPoints = btnFPointsFixedpoint.getSelection();
                DESCon.FPoints_In_M8 = txtFPointsInputM8.getText();
                String err = Messages.DesView_208;

                if (btnFPointsK0.getSelection() && btnFPointsK0.getText().equals(Messages.DesView_209)) {
                    DESCon.FPoints_In_selectedKey = 0;
                } else if (btnFPointsK0.getSelection() && btnFPointsK0.getText().equals(Messages.DesView_210)) {
                    DESCon.FPoints_In_selectedKey = 0;
                } else if (btnFPointsK1.getSelection() && btnFPointsK1.getText().equals(Messages.DesView_211)) {
                    DESCon.FPoints_In_selectedKey = 10;
                } else if (btnFPointsK1.getSelection() && btnFPointsK1.getText().equals(Messages.DesView_212)) {
                    DESCon.FPoints_In_selectedKey = 9;
                } else if (btnFPointsK2.getSelection() && btnFPointsK2.getText().equals(Messages.DesView_213)) {
                    DESCon.FPoints_In_selectedKey = 5;
                } else if (btnFPointsK2.getSelection() && btnFPointsK2.getText().equals(Messages.DesView_214)) {
                    DESCon.FPoints_In_selectedKey = 6;
                } else if (btnFPointsK3.getSelection() && btnFPointsK3.getText().equals(Messages.DesView_215)) {
                    DESCon.FPoints_In_selectedKey = 15;
                } else if (btnFPointsK3.getSelection() && btnFPointsK3.getText().equals(Messages.DesView_216)) {
                    DESCon.FPoints_In_selectedKey = 11;
                }

                if (DESCon.FPointsStudy() == 0) {
                    txtFPointsStatus.append(Messages.DesView_217 + getCurrentTime() + Messages.DesView_218
                            + txtFPointsInputM8.getText());
                    fillTable(tblFPointsOutputAFP, 0, 9, 1, 32, DESCon.FPoints_Out_M8M17);
                    if (btnFPointsFixedpoint.getSelection()) {
                        lblFPointsOutputAFPoint.setText(Messages.DesView_219 + DESCon.FPoints_Out_AFpoints);
                    } else {
                        lblFPointsOutputAFPoint.setText(Messages.DesView_220 + DESCon.FPoints_Out_AFpoints);
                    }
                    colorTable(tblFPointsOutputAFP, 1);
                    for (int i = 1; i < tblFPointsOutputAFP.getItemCount(); i++) {
                        tblFPointsOutputAFP.getItem(i).setText(tblFPointsOutputAFP.getColumnCount() - 1,
                                Integer.toString(DESCon.FPoints_Out_Distances[i]));
                    }
                } else {
                    for (int i = 0; i < DESCon.errMsg.length; i++) {
                        if (i != DESCon.errMsg.length - 1) {
                            err += DESCon.errMsg[i] + Messages.DesView_221;
                        } else {
                            err += DESCon.errMsg[i];
                        }
                    }
                    txtFPointsStatus.append(Messages.DesView_222 + getCurrentTime() + Messages.DesView_223 + err);

                }
            }
        });

        tabFPoints.setControl(comFPoints);
    }

    private void createSBoxTab() {
        tabSBox = new TabItem(tfolder, SWT.NONE);
        tabSBox.setText(Messages.DesView_224);

        // Tab Layout
        comSBox = new Composite(tfolder, SWT.NONE);
        comSBox.setLayout(new FillLayout());

        comSBoxMain = new Composite(comSBox, SWT.NONE);
        comSBoxMain.setLayout(new FormLayout());

        // create title
        lblSBoxTitle = new Label(comSBoxMain, SWT.NONE);
        FormData fd_lblSBoxTitle = new FormData();
        fd_lblSBoxTitle.left = new FormAttachment(0, 10);
        fd_lblSBoxTitle.top = new FormAttachment(0, 10);
        lblSBoxTitle.setLayoutData(fd_lblSBoxTitle);
        lblSBoxTitle.setFont(FontService.getHeaderFont());
        lblSBoxTitle.setBounds(0, 3, 650, 15);
        lblSBoxTitle.setText(Messages.DesView_title);

        // create title - text
        lblSBoxInformationText = new Label(comSBoxMain, SWT.NONE);
        FormData fd_lblInformationText = new FormData();
        fd_lblInformationText.top = new FormAttachment(lblSBoxTitle, 10);
        fd_lblInformationText.left = new FormAttachment(0, 10);
        lblSBoxInformationText.setLayoutData(fd_lblInformationText);
        lblSBoxInformationText.setBounds(0, 3, 650, 15);
        lblSBoxInformationText.setText(Messages.DesView_text);

        comSBoxMainLeft = new Composite(comSBoxMain, SWT.NONE);
        comSBoxMainLeft.setLayout(new FillLayout(SWT.HORIZONTAL));
        FormData fd_comSBoxMainLeft = new FormData(250, 480);
        fd_comSBoxMainLeft.left = new FormAttachment(0, 10);
        fd_comSBoxMainLeft.top = new FormAttachment(lblSBoxInformationText, 10);
        comSBoxMainLeft.setLayoutData(fd_comSBoxMainLeft);

        comSBoxMainRight = new Composite(comSBoxMain, SWT.NONE);
        comSBoxMainRight.setLayout(new FormLayout());
        FormData fd_comSBoxMainRight = new FormData(600, 480);
        fd_comSBoxMainRight.top = new FormAttachment(lblSBoxInformationText, 10);
        fd_comSBoxMainRight.left = new FormAttachment(comSBoxMainLeft, 10);
        fd_comSBoxMainRight.right = new FormAttachment(100, -10);
        comSBoxMainRight.setLayoutData(fd_comSBoxMainRight);

        comSBoxMainDown = new Composite(comSBoxMain, SWT.NONE);
        FormData fd_comSBoxMainDown = new FormData(0, 100);
        fd_comSBoxMainDown.top = new FormAttachment(comSBoxMainLeft, 10);
        fd_comSBoxMainDown.left = new FormAttachment(0, 10);
        fd_comSBoxMainDown.right = new FormAttachment(100, -10);
        comSBoxMainDown.setLayout(new FillLayout(SWT.HORIZONTAL));
        comSBoxMainDown.setLayoutData(fd_comSBoxMainDown);

        comSBoxMainRightUp = new Composite(comSBoxMainRight, SWT.NONE);
        comSBoxMainRightUp.setLayout(new FillLayout(SWT.HORIZONTAL));
        FormData fd_comSBoxMainRightUp = new FormData();
        fd_comSBoxMainRightUp.top = new FormAttachment(0);
        fd_comSBoxMainRightUp.left = new FormAttachment(0);
        fd_comSBoxMainRightUp.right = new FormAttachment(100);
        comSBoxMainRightUp.setLayoutData(fd_comSBoxMainRightUp);

        comSBoxMainRightDown = new Composite(comSBoxMainRight, SWT.NONE);
        FormData fd_comSBoxMainRightDown = new FormData(0, 150);
        fd_comSBoxMainRightDown.right = new FormAttachment(100);
        fd_comSBoxMainRightDown.top = new FormAttachment(comSBoxMainRightUp, 10);
        fd_comSBoxMainRightDown.left = new FormAttachment(0);
        fd_comSBoxMainRightDown.bottom = new FormAttachment(100);
        comSBoxMainRightDown.setLayout(new FillLayout(SWT.HORIZONTAL));
        comSBoxMainRightDown.setLayoutData(fd_comSBoxMainRightDown);

        grpSBoxInput = new Group(comSBoxMainRightUp, SWT.NONE);
        grpSBoxInput.setText(Messages.DesView_225);
        grpSBoxInput.setLayout(new FormLayout());

        grpSBoxOutput = new Group(comSBoxMainRightDown, SWT.NONE);
        grpSBoxOutput.setText(Messages.DesView_226);
        grpSBoxOutput.setLayout(null);

        grpSBoxInformation = new Group(comSBoxMainLeft, SWT.NONE);
        grpSBoxInformation.setLayout(new FormLayout());
        grpSBoxInformation.setText(Messages.DesView_227);

        grpSBoxStatus = new Group(comSBoxMainDown, SWT.NONE);
        grpSBoxStatus.setText(Messages.DesView_228);
        grpSBoxStatus.setLayout(new FormLayout());

        txtSBoxStatus = new Text(grpSBoxStatus, SWT.MULTI | SWT.V_SCROLL);
        txtSBoxStatus.setEditable(false);
        FormData fd_txtSBoxStatus = new FormData();
        fd_txtSBoxStatus.bottom = new FormAttachment(100, -10);
        fd_txtSBoxStatus.right = new FormAttachment(100, -10);
        fd_txtSBoxStatus.top = new FormAttachment(0, 10);
        fd_txtSBoxStatus.left = new FormAttachment(0, 10);
        txtSBoxStatus.setLayoutData(fd_txtSBoxStatus);

        // Input Tab
        comSBoxInput = new Composite(grpSBoxInput, SWT.NONE);
        FormData fd_comSBoxInput = new FormData();
        fd_comSBoxInput.right = new FormAttachment(0, 254);
        fd_comSBoxInput.bottom = new FormAttachment(100);
        fd_comSBoxInput.top = new FormAttachment(0);
        fd_comSBoxInput.left = new FormAttachment(0, 10);
        comSBoxInput.setLayoutData(fd_comSBoxInput);

        lblSBoxInputData = new Label(comSBoxInput, SWT.NONE);
        lblSBoxInputData.setBounds(0, 0, 51, 13);
        lblSBoxInputData.setText(Messages.DesView_229);
        lblSBoxInputData.setFont(FontService.getLargeBoldFont());

        lblSBoxInputDeltap = new Label(comSBoxInput, SWT.NONE);
        lblSBoxInputDeltap.setBounds(0, 19, 181, 15);
        lblSBoxInputDeltap.setText(Messages.DesView_230);

        lblSBoxInputDeltapCur = new Label(comSBoxInput, SWT.NONE);
        lblSBoxInputDeltapCur.setBounds(185, 43, 50, 20);
        lblSBoxInputDeltapCur.setText(Messages.DesView_231);

        txtSBoxInputDeltap = new Text(comSBoxInput, SWT.BORDER);
        txtSBoxInputDeltap.setBounds(0, 38, 181, 20);
        txtSBoxInputDeltap.setTextLimit(16);
        txtSBoxInputDeltap.setText(Messages.DesView_232);

        txtSBoxInputDeltap.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                lblSBoxInputDeltapCur.setText(Messages.DesView_233 + txtSBoxInputDeltap.getText().length()
                        + Messages.DesView_234);

            }

        });

        comSBoxInputDataSeries = new Composite(grpSBoxInput, SWT.NONE);
        FormData fd_comSBoxInputDataSeries = new FormData();
        fd_comSBoxInputDataSeries.bottom = new FormAttachment(100);
        fd_comSBoxInputDataSeries.top = new FormAttachment(0);
        fd_comSBoxInputDataSeries.left = new FormAttachment(comSBoxInput, 10);
        comSBoxInputDataSeries.setLayoutData(fd_comSBoxInputDataSeries);

        lblSBoxInputSeries = new Label(comSBoxInputDataSeries, SWT.NONE);
        lblSBoxInputSeries.setBounds(0, 0, 150, 13);
        lblSBoxInputSeries.setText(Messages.DesView_235);
        lblSBoxInputSeries.setFont(FontService.getLargeBoldFont());

        slSBoxInputSeriesTime = new Slider(comSBoxInputDataSeries, SWT.NONE);
        slSBoxInputSeriesTime.setBounds(0, 43, 120, 15);
        slSBoxInputSeriesTime.setMinimum(1);
        slSBoxInputSeriesTime.setMaximum(1000);
        slSBoxInputSeriesTime.setSelection(100);

        slSBoxInputSeriesTime.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                lblSBoxInputSeriesTime.setText(Messages.DesView_236 + slSBoxInputSeriesTime.getSelection());
            }
        });

        lblSBoxInputSeriesTime = new Label(comSBoxInputDataSeries, SWT.NONE);
        lblSBoxInputSeriesTime.setBounds(0, 19, 181, 15);
        lblSBoxInputSeriesTime.setText(Messages.DesView_237 + slSBoxInputSeriesTime.getSelection());

        slSBoxInputSeriesCount = new Slider(comSBoxInputDataSeries, SWT.NONE);
        slSBoxInputSeriesCount.setBounds(180, 43, 120, 15);
        slSBoxInputSeriesCount.setMinimum(1);
        slSBoxInputSeriesCount.setMaximum(200);
        slSBoxInputSeriesCount.setSelection(20);

        slSBoxInputSeriesCount.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                lblSBoxInputSeriesCount.setText(Messages.DesView_238 + slSBoxInputSeriesCount.getSelection());
            }
        });

        lblSBoxInputSeriesCount = new Label(comSBoxInputDataSeries, SWT.NONE);
        lblSBoxInputSeriesCount.setBounds(180, 19, 181, 15);
        lblSBoxInputSeriesCount.setText(Messages.DesView_239 + slSBoxInputSeriesCount.getSelection());

        // Output Tab
        tblSBoxOutput = new Table(grpSBoxOutput, SWT.NO_FOCUS | SWT.VIRTUAL);
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

        tblSBoxOutput.setBounds(10, 20, 245, 345);

        lblSBoxOutputCurStep = new Label(grpSBoxOutput, SWT.NONE);
        lblSBoxOutputCurStep.setBounds(300, 50, 200, 20);
        lblSBoxOutputCurStep.setText(Messages.DesView_241);

        lblSBoxOutputP = new Label(grpSBoxOutput, SWT.NONE);
        lblSBoxOutputP.setBounds(300, 80, 200, 20);
        lblSBoxOutputP.setText(Messages.DesView_242);

        lblSBoxOutputK = new Label(grpSBoxOutput, SWT.NONE);
        lblSBoxOutputK.setBounds(300, 110, 200, 20);
        lblSBoxOutputK.setText(Messages.DesView_243);

        // Information Tab

        txtSBoxInformation = new StyledText(grpSBoxInformation, SWT.READ_ONLY | SWT.MULTI);
        FormData fd_txtSBoxInformation = new FormData();
        fd_txtSBoxInformation.right = new FormAttachment(100, -10);
        fd_txtSBoxInformation.top = new FormAttachment(0, 10);
        fd_txtSBoxInformation.left = new FormAttachment(0, 10);
        fd_txtSBoxInformation.bottom = new FormAttachment(100, -10);
        txtSBoxInformation.setLayoutData(fd_txtSBoxInformation);

        txtSBoxInformation.setText(Messages.DesView_244 + Messages.DesView_245 + Messages.DesView_246
                + Messages.DesView_247 + Messages.DesView_248 + Messages.DesView_249 + Messages.DesView_250
                + Messages.DesView_251 + Messages.DesView_252 + Messages.DesView_253 + Messages.DesView_254
                + Messages.DesView_255 + Messages.DesView_256 + Messages.DesView_257 + Messages.DesView_258
                + Messages.DesView_259 + Messages.DesView_260 + Messages.DesView_261 + Messages.DesView_262
                + Messages.DesView_263 + Messages.DesView_264);

        txtSBoxInformation.setFont(FontService.getNormalFont());

        // Action Buttons
        btnSBoxReset = new Button(comSBoxMain, SWT.NONE);
        FormData fd_btnSBoxReset = new FormData();
        fd_btnSBoxReset.top = new FormAttachment(comSBoxMainDown, 10);
        fd_btnSBoxReset.left = new FormAttachment(0, 10);
        btnSBoxReset.setLayoutData(fd_btnSBoxReset);
        btnSBoxReset.setText(Messages.DesView_265);

        btnSBoxReset.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

                txtSBoxStatus.append(Messages.DesView_266 + getCurrentTime() + Messages.DesView_267);
                txtSBoxInputDeltap.setText(Messages.DesView_268);
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

        btnSBoxEvaluate = new Button(comSBoxMain, SWT.NONE);
        FormData fd_btnSBoxEvaluate = new FormData();
        fd_btnSBoxEvaluate.top = new FormAttachment(comSBoxMainDown, 10);
        fd_btnSBoxEvaluate.left = new FormAttachment(btnSBoxReset, 10);
        btnSBoxEvaluate.setLayoutData(fd_btnSBoxEvaluate);
        btnSBoxEvaluate.setText(Messages.DesView_274);

        btnSBoxEvaluate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DESCon.SBox_In_Deltap = txtSBoxInputDeltap.getText();
                String err = Messages.DesView_275;

                if (DESCon.SBoxStudy() == 0) {
                    txtSBoxStatus.append(Messages.DesView_276 + getCurrentTime() + Messages.DesView_277
                            + txtSBoxInputDeltap.getText());
                    txtSBoxInputDeltap.setEnabled(false);
                    intSBoxOutputCurStep++;
                    lblSBoxOutputCurStep.setText(Messages.DesView_278 + intSBoxOutputCurStep);
                    lblSBoxOutputP.setText(Messages.DesView_279 + DESCon.SBox_Out_randomm);
                    lblSBoxOutputK.setText(Messages.DesView_280 + DESCon.SBox_Out_randomk);
                    fillTable(tblSBoxOutput, 0, 15, 0, 7, DESCon.SBox_Out_activeBoxes);
                    colorTable(tblSBoxOutput, 0);
                } else {
                    for (int i = 0; i < DESCon.errMsg.length; i++) {
                        if (i != DESCon.errMsg.length - 1) {
                            err += DESCon.errMsg[i] + Messages.DesView_281;
                        } else {
                            err += DESCon.errMsg[i];
                        }
                    }
                    txtSBoxStatus.append(Messages.DesView_282 + getCurrentTime() + Messages.DesView_283 + err);

                }
            }
        });

        btnSBoxEvaluateSeries = new Button(comSBoxMain, SWT.NONE);
        FormData fd_btnSBoxEvaluateSeries = new FormData();
        fd_btnSBoxEvaluateSeries.top = new FormAttachment(comSBoxMainDown, 10);
        fd_btnSBoxEvaluateSeries.left = new FormAttachment(btnSBoxEvaluate, 10);
        btnSBoxEvaluateSeries.setLayoutData(fd_btnSBoxEvaluateSeries);
        btnSBoxEvaluateSeries.setText(Messages.DesView_284);
        btnSBoxEvaluateSeries.setAlignment(SWT.CENTER);

        btnSBoxEvaluateSeries.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String err = Messages.DesView_285;
                DESCon.SBox_In_Deltap = txtSBoxInputDeltap.getText();
                Display curdis = Display.getCurrent();

                if (DESCon.SBoxStudy() == 0) {
                    txtSBoxStatus.append(Messages.DesView_286 + getCurrentTime() + Messages.DesView_287
                            + txtSBoxInputDeltap.getText() + Messages.DesView_288
                            + slSBoxInputSeriesCount.getSelection() + Messages.DesView_289
                            + slSBoxInputSeriesTime.getSelection());
                    txtSBoxInputDeltap.setEnabled(false);
                    slSBoxInputSeriesTime.setEnabled(false);
                    slSBoxInputSeriesCount.setEnabled(false);
                    btnSBoxEvaluate.setEnabled(false);
                    btnSBoxReset.setEnabled(false);
                    btnSBoxEvaluateSeries.setEnabled(false);

                    for (int i = 0; i < slSBoxInputSeriesCount.getSelection() - 1; i++) {
                        intSBoxOutputCurStep++;
                        lblSBoxOutputCurStep.setText(Messages.DesView_290 + intSBoxOutputCurStep);
                        lblSBoxOutputP.setText(Messages.DesView_291 + DESCon.SBox_Out_randomm);
                        lblSBoxOutputK.setText(Messages.DesView_292 + DESCon.SBox_Out_randomk);
                        fillTable(tblSBoxOutput, 0, 15, 0, 7, DESCon.SBox_Out_activeBoxes);
                        colorTable(tblSBoxOutput, 0);
                        tblSBoxOutput.redraw();
                        tblSBoxOutput.update();
                        curdis.update();
                        try {
                            Thread.sleep(slSBoxInputSeriesTime.getSelection());
                        } catch (Exception ex) {
                            LogUtil.logError(ex);
                        }
                        DESCon.SBoxStudy();
                    }
                    intSBoxOutputCurStep++;
                    lblSBoxOutputCurStep.setText(Messages.DesView_293 + intSBoxOutputCurStep);

                    slSBoxInputSeriesTime.setEnabled(true);
                    slSBoxInputSeriesCount.setEnabled(true);
                    btnSBoxEvaluate.setEnabled(true);
                    btnSBoxReset.setEnabled(true);
                    btnSBoxEvaluateSeries.setEnabled(true);

                } else {
                    for (int i = 0; i < DESCon.errMsg.length; i++) {
                        if (i != DESCon.errMsg.length - 1) {
                            err += DESCon.errMsg[i] + Messages.DesView_294;
                        } else {
                            err += DESCon.errMsg[i];
                        }
                    }
                    txtSBoxStatus.append(Messages.DesView_295 + getCurrentTime() + Messages.DesView_296 + err);
                }

            }
        });

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
            // Coloring FPoints Tab, Alg Tab M0M17 Output, Alg Tab DES(k,p+e_i) Output (Bit Change Coloring)
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
        DateFormat df = new SimpleDateFormat(Messages.DesView_300);
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
