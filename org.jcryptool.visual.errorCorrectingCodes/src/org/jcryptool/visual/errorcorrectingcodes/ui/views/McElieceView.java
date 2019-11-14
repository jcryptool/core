package org.jcryptool.visual.errorcorrectingcodes.ui.views;

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
import org.jcryptool.visual.errorcorrectingcodes.data.HammingData;
import org.jcryptool.visual.errorcorrectingcodes.ui.Messages;
import org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper;

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.datatransfer.StringSelection;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

public class McElieceView extends Composite {

    private McElieceCrypto mceCrypto;

    private Composite parent;
    private Composite compHead;
    private Composite mainComposite;
    private Composite compButtons;

    private Group grpInput;
    private Group grpOutput;
    private Group grpAlgorithmInfo;
    private Group grpKeyParams;

    private Label lblHeader;
    private Label lblInput;
    private Button btnEncrypt;
    private Button btnDecrypt;
    private Combo comboValueM;
    private Text txtValueT;
    private StyledText txtInput;
    private StyledText txtOutput;

    private Text txtPrivateKey;

    private Button btnFillKey;

    public McElieceView(Composite parent, int style) {
        super(parent, style);
        this.parent = parent;
        mceCrypto = new McElieceCrypto();

        // common grid layout for all elements
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(5, 5).equalWidth(true);
        GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, true);

        glf.applyTo(this);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText("McEliece Cryptography System");

        mainComposite = new Composite(this, SWT.NONE);
        glf.applyTo(mainComposite);
        gdf.applyTo(mainComposite);

        grpAlgorithmInfo = new Group(mainComposite, SWT.NONE);
        grpAlgorithmInfo.setText("McEliece Algorithm Parameters");
        glf.applyTo(grpAlgorithmInfo);
        gdf.applyTo(grpAlgorithmInfo);
        grpKeyParams = new Group(grpAlgorithmInfo, SWT.NONE);
        grpKeyParams.setText("Key Parameters");
        glf.numColumns(2).applyTo(grpKeyParams);
        gdf.applyTo(grpKeyParams);
        Label lblValueM = new Label(grpKeyParams, SWT.NONE);
        lblValueM.setText("m");
        comboValueM = new Combo(grpKeyParams, SWT.READ_ONLY);
        comboValueM.setItems(mceCrypto.getValidMValues());
       
        Label lblValueT = new Label(grpKeyParams, SWT.NONE);
        lblValueT.setText("t");
        txtValueT = new Text(grpKeyParams, SWT.BORDER);
        
        Label lblPublicKey = new Label(grpKeyParams, SWT.NONE);
        lblPublicKey.setText("Public Key Size (kb):");
        txtPrivateKey = new Text(grpKeyParams, SWT.READ_ONLY);

        compButtons = new Composite(grpAlgorithmInfo, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(compButtons);
        btnFillKey = new Button(compButtons, SWT.NONE);
        btnFillKey.setText("Generate Keys");
        btnFillKey.addListener(SWT.Selection, e -> generateKeys());
        
        btnEncrypt = new Button(compButtons, SWT.NONE);
        btnEncrypt.setText("Encrypt");
        btnEncrypt.addListener(SWT.Selection, e -> performEncryption());
        btnDecrypt = new Button(compButtons, SWT.NONE);
        btnDecrypt.setText("Decrypt");
        btnDecrypt.addListener(SWT.Selection, e -> performDecryption());

        grpInput = new Group(mainComposite, SWT.NONE);
        grpInput.setText("Cleartext");
        glf.numColumns(1).applyTo(grpInput);
        gdf.applyTo(grpInput);
        lblInput = new Label(grpInput, SWT.NONE);
        lblInput.setText("Enter the input text here: ");
        txtInput = UIHelper.mutltiLineText(grpInput, SWT.FILL, SWT.FILL, SWT.DEFAULT, 5);

        grpOutput = new Group(mainComposite, SWT.NONE);
        grpOutput.setText("Ciphertext");
        GridLayoutFactory.fillDefaults().extendedMargins(5, 5, 50, 5).applyTo(grpOutput);
        gdf.applyTo(grpOutput);
        txtOutput = UIHelper.mutltiLineText(grpOutput, SWT.FILL, SWT.FILL, SWT.DEFAULT, 5);

    }
    
    private void generateKeys() {
        if (updateParams() == 0) {
            mceCrypto.setKeyParams(12,50);
            comboValueM.setText(String.valueOf(mceCrypto.getM()));
            txtValueT.setText(String.valueOf(mceCrypto.getT()));
            txtPrivateKey.setText(String.valueOf(UIHelper.round(mceCrypto.getPrivateKeySize(),2)));
        }
    }

    private int updateParams() {
        int m = comboValueM.getText().equals("") ? 0 : Integer.valueOf(comboValueM.getText());
        int t = txtValueT.getText().equals("") ? 0 : Integer.valueOf(txtValueT.getText());

        if (m != 0 && t != 0) {
            try {
                mceCrypto.setKeyParams(m, t);
            } catch (Exception ex) {
                LogUtil.logError(ex);
                MessageBox keyErrorDialog = new MessageBox(parent.getShell(), SWT.ERROR);
                keyErrorDialog.setText("Errorneous key parameters!");
                keyErrorDialog.setMessage("Could not init system with given parameters. Try selecting a smaller t or greater m.");
                keyErrorDialog.open();
            }
            
            txtPrivateKey.setText(String.valueOf(UIHelper.round(mceCrypto.getPrivateKeySize(),2)));

        }
        
        return m;
    }

    private void performDecryption() {
        
        if(Integer.valueOf(comboValueM.getText()) != mceCrypto.getM() || 
                Integer.valueOf(txtValueT.getText()) != mceCrypto.getT()) {
            updateParams();
        }
                
        try {
            mceCrypto.decrypt();
            txtInput.setText(mceCrypto.getClearText());
        } catch (Exception e) {
            LogUtil.logError(e);
            MessageBox invalidCipherDialog = new MessageBox(parent.getShell(), SWT.ERROR);
            invalidCipherDialog.setMessage("The entered Ciphertext is invalid.");
            invalidCipherDialog.open();
        }
    }

    private void performEncryption() {
        if(Integer.valueOf(comboValueM.getText()) != mceCrypto.getM() || 
                Integer.valueOf(txtValueT.getText()) != mceCrypto.getT()) {
            updateParams();
        }
           
        mceCrypto.encrypt(txtInput.getText().getBytes());
        txtOutput.setText(mceCrypto.getEncryptedHex());

    }
}
