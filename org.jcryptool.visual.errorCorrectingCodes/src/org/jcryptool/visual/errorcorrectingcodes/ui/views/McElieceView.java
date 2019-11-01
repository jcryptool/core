package org.jcryptool.visual.errorcorrectingcodes.ui.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.algorithm.McElieceCrypto;
import org.jcryptool.visual.errorcorrectingcodes.ui.Messages;
import org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper;

import com.sun.xml.internal.ws.util.StringUtils;

import java.awt.datatransfer.StringSelection;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.InvalidCipherTextException;
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

    private Label lblHeader;

    private Group grpInput;

    private Label lblInput;

    private StyledText txtInput;

    private Group grpOutput;

    private StyledText txtOutput;

    private Group grpButtons;

    private Button btnEncrypt;

    private Button btnDecrypt;

    public McElieceView(Composite parent, int style) {
        super(parent, style);
        this.parent = parent;
        
        mceCrypto = new McElieceCrypto();
        
        // common grid layout for all elements
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(5, 5).equalWidth(true);
        glf.applyTo(this);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(this);

        
        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText("McEliece Cryptography System");
        
        mainComposite = new Composite(this, SWT.NONE);
        glf.applyTo(mainComposite);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(mainComposite);
        
        grpInput = new Group(mainComposite, SWT.NONE);
        grpInput.setText("Cleartext");
        glf.applyTo(grpInput);
        lblInput = new Label(grpInput, SWT.NONE);
        lblInput.setText("Enter the input text here: ");
        txtInput = UIHelper.mutltiLineText(grpInput, SWT.FILL, SWT.FILL, SWT.DEFAULT, 5);
                
        grpOutput = new Group(mainComposite, SWT.NONE);
        grpOutput.setText("Ciphertext");
        GridLayoutFactory.fillDefaults().extendedMargins(5, 5, 50, 5).applyTo(grpOutput);
        txtOutput = UIHelper.mutltiLineText(grpOutput, SWT.FILL, SWT.FILL, SWT.DEFAULT, 5);

        grpButtons = new Group(mainComposite, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(grpButtons);
        btnEncrypt = new Button(grpButtons, SWT.NONE);
        btnEncrypt.setText("Encrypt");
        btnEncrypt.addListener(SWT.Selection, e -> performEncryption());
        btnDecrypt = new Button(grpButtons, SWT.NONE);
        btnDecrypt.setText("Decrypt");
        btnDecrypt.addListener(SWT.Selection, e -> performDecryption());
    }

    private void performDecryption() {
        try {
            txtInput.setText(new String(mceCrypto.decrypt(), StandardCharsets.UTF_8));
        } catch (InvalidCipherTextException e) {
            LogUtil.logError(e);
            MessageBox invalidCipherDialog = new org.eclipse.swt.widgets.MessageBox(parent.getShell(), SWT.ERROR);
            invalidCipherDialog.setMessage("The entered Ciphertext is invalid.");
            invalidCipherDialog.open();
        }
    }
    
   
    private void performEncryption() {
        byte[] enc = mceCrypto.encrypt(txtInput.getText().getBytes());
        txtOutput.setText(javax.xml.bind.DatatypeConverter.printHexBinary(enc));
    }
}
