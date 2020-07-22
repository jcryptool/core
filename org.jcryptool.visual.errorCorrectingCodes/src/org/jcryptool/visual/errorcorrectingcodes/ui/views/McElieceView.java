package org.jcryptool.visual.errorcorrectingcodes.ui.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.algorithm.McElieceCrypto;
import org.jcryptool.visual.errorcorrectingcodes.ui.Messages;
import org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper;

public class McElieceView extends Composite {

    private static final int _WHINT = 400;

    private McElieceCrypto mceCrypto;

    private Composite parent;
    private Composite composite;
    private Composite compHead;
    private Composite mainComposite;
    private Composite compButtons;

    private Group grpInput;
    private Group grpOutput;
    private Group grpAlgorithmInfo;
    private Group grpKeyInputParams;
    private Group grpKeyParams;

    private Label lblHeader;
    private Text textInfoHead;
    private StyledText txtInput;
    private StyledText txtOutput;
    private Text txtValueT;
    private Text txtPublicKey;
    private Button btnEncrypt;
    private Button btnDecrypt;
    private Button btnFillKey;
    private Combo comboValueM;

    private Composite grpParams;

    private Text txtValueK;

    private Text txtValueN;

    public McElieceView(Composite parent, int style) {
        super(parent, style);
        this.parent = parent;
        mceCrypto = new McElieceCrypto();

        // common grid layout for all elements
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(5, 5).equalWidth(true);
        GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);

        glf.applyTo(this);
        gdf.applyTo(this);
        // GridDataFactory.fillDefaults().grab(true, false).hint(1050, 1680).applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        gdf.applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.McElieceView_lblHeader);
        textInfoHead = new Text(compHead, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
        textInfoHead.setText(Messages.McElieceView_textHeader);
        textInfoHead.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        // textInfoHead.setMargins(5, 5, 5, 5);
        GridDataFactory.fillDefaults().grab(true, false).hint(_WHINT, SWT.DEFAULT).applyTo(textInfoHead);

        mainComposite = new Composite(this, SWT.NONE);
        glf.applyTo(mainComposite);
        gdf.applyTo(mainComposite);

        grpAlgorithmInfo = new Group(mainComposite, SWT.NONE);
        grpAlgorithmInfo.setText(Messages.McElieceView_grpAlgorithmInfo);
        glf.numColumns(2).applyTo(grpAlgorithmInfo);
        gdf.applyTo(grpAlgorithmInfo);
        grpKeyInputParams = new Group(grpAlgorithmInfo, SWT.NONE);
        grpKeyInputParams.setText(Messages.McElieceView_grpKeyParams);
        glf.numColumns(2).applyTo(grpKeyInputParams);
        gdf.applyTo(grpKeyInputParams);
        Label lblValueM = new Label(grpKeyInputParams, SWT.NONE);
        lblValueM.setText("m"); //$NON-NLS-1$
        comboValueM = new Combo(grpKeyInputParams, SWT.READ_ONLY);
        comboValueM.setItems(mceCrypto.getValidMValues());
        Label lblValueT = new Label(grpKeyInputParams, SWT.NONE);
        lblValueT.setText("t"); //$NON-NLS-1$
        txtValueT = new Text(grpKeyInputParams, SWT.BORDER);

        grpKeyParams = new Group(grpAlgorithmInfo, SWT.NONE);
        glf.numColumns(2).applyTo(grpKeyParams);
        gdf.applyTo(grpKeyParams);
        Label lblValueK = new Label(grpKeyParams, SWT.NONE);
        lblValueK.setText("k = ");
        txtValueK = new Text(grpKeyParams, SWT.READ_ONLY);
        txtValueK.setText("0");
        gdf.applyTo(txtValueK);

        Label lblValueN = new Label(grpKeyParams, SWT.NONE);
        lblValueN.setText("n = ");
        txtValueN = new Text(grpKeyParams, SWT.READ_ONLY);
        txtValueN.setText("0");
        gdf.applyTo(txtValueN);

        Label lblPublicKey = new Label(grpKeyParams, SWT.NONE);
        lblPublicKey.setText(Messages.McElieceView_lblPublicKey);
        txtPublicKey = new Text(grpKeyParams, SWT.READ_ONLY);
        txtPublicKey.setText("0");
        gdf.applyTo(txtPublicKey);

        compButtons = new Composite(grpAlgorithmInfo, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(compButtons);
        btnFillKey = new Button(compButtons, SWT.NONE);
        btnFillKey.setText(Messages.McElieceView_btnFillKey);
        btnFillKey.addListener(SWT.Selection, e -> generateKeys());

        btnEncrypt = new Button(compButtons, SWT.NONE);
        btnEncrypt.setText(Messages.McElieceView_btnEncrypt);
        btnEncrypt.addListener(SWT.Selection, e -> performEncryption());
        btnDecrypt = new Button(compButtons, SWT.NONE);
        btnDecrypt.setText(Messages.McElieceView_btnDecrypt);
        btnDecrypt.addListener(SWT.Selection, e -> performDecryption());

        grpInput = new Group(mainComposite, SWT.NONE);
        grpInput.setText(Messages.McElieceView_grpInput);
        glf.numColumns(1).applyTo(grpInput);
        gdf.grab(true, true).applyTo(grpInput);
        txtInput = UIHelper.mutltiLineText(grpInput, SWT.FILL, SWT.CENTER, SWT.DEFAULT, 10);

        grpOutput = new Group(mainComposite, SWT.NONE);
        grpOutput.setText(Messages.McElieceView_grpOutput);
        glf.applyTo(grpOutput);
        gdf.applyTo(grpOutput);
        txtOutput = UIHelper.mutltiLineText(grpOutput, SWT.FILL, SWT.CENTER, SWT.DEFAULT, 10);

    }

    private void generateKeys() {
        if (updateParams() != 0) {
            comboValueM.setText(String.valueOf(mceCrypto.getM()));
            txtValueT.setText(String.valueOf(mceCrypto.getT()));
            txtValueK.setText(String.valueOf(mceCrypto.getK()));
            txtValueN.setText(String.valueOf(mceCrypto.getCodeLength()));
            txtPublicKey.setText(String.valueOf(UIHelper.round(mceCrypto.getPrivateKeySize(), 2)));
        }
    }

    private int updateParams() {
        int m = comboValueM.getText().equals("") ? 0 : Integer.valueOf(comboValueM.getText()); //$NON-NLS-1$
        int t = txtValueT.getText().equals("") ? 0 : Integer.valueOf(txtValueT.getText()); //$NON-NLS-1$

        if (m == 0)
            m = 12;
        if (t == 0)
            t = 12;

        try {
            mceCrypto.setKeyParams(m, t);
        } catch (Exception ex) {
            LogUtil.logError(ex);
            MessageBox keyErrorDialog = new MessageBox(parent.getShell(), SWT.ERROR);
            keyErrorDialog.setText(Messages.McElieceView_errorParamsTitle);
            keyErrorDialog.setMessage(Messages.McElieceView_errorParams);
            keyErrorDialog.open();
        }

        txtPublicKey.setText(String.valueOf(UIHelper.round(mceCrypto.getPrivateKeySize(), 2)));
        return mceCrypto.getCodeLength();

    }

    private void performDecryption() {

        if (Integer.valueOf(comboValueM.getText()) != mceCrypto.getM()
                || Integer.valueOf(txtValueT.getText()) != mceCrypto.getT()) {
            updateParams();
        }

        try {
            mceCrypto.decrypt(txtOutput.getText());
            txtInput.setText(mceCrypto.getClearText());
        } catch (Exception e) {
            LogUtil.logError(e);
            MessageBox invalidCipherDialog = new MessageBox(parent.getShell(), SWT.ERROR);
            invalidCipherDialog.setMessage(Messages.McElieceView_errorCipher);
            invalidCipherDialog.open();
        }
    }

    private void performEncryption() {
        if (Integer.valueOf(comboValueM.getText()) != mceCrypto.getM()
                || Integer.valueOf(txtValueT.getText()) != mceCrypto.getT()) {
            updateParams();
        }

        mceCrypto.encrypt(txtInput.getText().getBytes());
        txtOutput.setText(mceCrypto.getEncryptedHex());

    }
}
