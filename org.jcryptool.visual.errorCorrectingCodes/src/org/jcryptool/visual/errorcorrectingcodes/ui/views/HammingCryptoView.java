package org.jcryptool.visual.errorcorrectingcodes.ui.views;

import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.codeText;
import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.matrixText;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.algorithm.HammingCrypto;
import org.jcryptool.visual.errorcorrectingcodes.data.HammingData;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;
import org.jcryptool.visual.errorcorrectingcodes.data.MatrixException;
import org.jcryptool.visual.errorcorrectingcodes.ui.Messages;
import org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper;
import org.jcryptool.visual.errorcorrectingcodes.ui.binding.InteractiveMatrixProperty;
import org.jcryptool.visual.errorcorrectingcodes.ui.widget.InteractiveMatrix;

/**
 * CryptoView represents the McEliece cryptographic system with Hamming code and small matrices S and P.
 * Usually, the encryption requires larger linear code, such as Goppa, and accordingly big permutation and scrambling matrices. Because the resulting large key and data sizes are not easily represented, we decided to simplify the original proposal. Therefore this is not a secure implementation of the system.
 * 
 * @author dhofmann
 *
 */
public class HammingCryptoView extends Composite {

    private static final int _WHINT = 400;
    private HammingData data;
    private HammingCrypto mce;
    private DataBindingContext dbc;

    private Composite parent;
    private Composite mainComposite;
    private Composite compHead;
    private Composite compInfoText;
    private Composite compPrivateKeyData;
    private Composite compPrivateKeyButton;
    private Composite compInverseMatrices;
    private Group grpDecryption;
    private Group grpPrivateKey;
    private Group grpEncryption;
    private Group grpInputStep;
    private Group grpOutput;
    private Group grpPublicKey;
    private Group grpControlButtons;
    private Group grpTextInfo;
    private Group grpCiphertext;

    private Text textOutput;
    private Text textInput;
    private StyledText textAsBinary;
    private StyledText textEncrypted;
    private StyledText textInfo;
    private StyledText textMatrixG;
    private StyledText textMatrixSInverse;
    private StyledText textMatrixPInverse;
    private StyledText textMatrixGSP;
    private StyledText textDecoded;
    private InteractiveMatrix compMatrixP;
    private InteractiveMatrix compMatrixS;

    private Button btnReset;
    private Button btnNextStep;
    private Button btnPrev;
    private Button btnGeneratePrivateKey;
    private Label lblHeader;
    private Label lblMatrixG;
    private Label lblMatrixS;
    private Label lblMatrixP;
    private Label lblMatrixGSP;
    private Label lblMatrixSInverse;
    private Label lblMatrixPInverse;
    private StyledText textInfoHead;
    private Button btnMatrixSEdit;

    public HammingCryptoView(Composite parent, int style) {
        super(parent, style);
        data = new HammingData();
        mce = new HammingCrypto(data);
        this.parent = parent;
        Point margins = new Point(5, 5);
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(margins);
        GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
        
        glf.applyTo(this);
        gdf.applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        gdf.applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.HammingCryptoView_lblHeader);
        textInfoHead = new StyledText(compHead,SWT.READ_ONLY | SWT.WRAP);
        textInfoHead.setText(Messages.HammingCryptoView_textHeader);
        GridDataFactory.fillDefaults().grab(true, false).hint(_WHINT, SWT.DEFAULT).applyTo(textInfoHead);
        
        mainComposite = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().margins(margins).numColumns(2).spacing(40, SWT.DEFAULT).equalWidth(true)
                .applyTo(mainComposite);
        gdf.applyTo(mainComposite);

        grpDecryption = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpDecryption);
        gdf.applyTo(grpDecryption);
        grpDecryption.setText(Messages.HammingCryptoView_grpDecryption);

        grpPrivateKey = new Group(grpDecryption, SWT.NONE);
        grpPrivateKey.setText(Messages.HammingCryptoView_grpPrivateKey);
        glf.applyTo(grpPrivateKey);
        GridDataFactory.fillDefaults().applyTo(grpPrivateKey);
        compPrivateKeyButton = new Composite(grpPrivateKey, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(compPrivateKeyButton);
        btnGeneratePrivateKey = new Button(compPrivateKeyButton, SWT.NONE);
        btnGeneratePrivateKey.setText(Messages.HammingCryptoView_btnGeneratePrivateKey);
        btnGeneratePrivateKey.addListener(SWT.Selection, e -> generateKey(true));

        compPrivateKeyData = new Composite(grpPrivateKey, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(6).spacing(20, SWT.DEFAULT).margins(margins)
                .applyTo(compPrivateKeyData);
        gdf.applyTo(compPrivateKeyData);
        lblMatrixG = new Label(compPrivateKeyData, SWT.NONE);
        lblMatrixG.setText("G = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixG);
        textMatrixG = matrixText(compPrivateKeyData, SWT.LEFT, SWT.CENTER, 7, 4);
        lblMatrixS = new Label(compPrivateKeyData, SWT.NONE);
        lblMatrixS.setText("S = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixS);
        compMatrixS = new InteractiveMatrix(compPrivateKeyData, 4, 4);
        glf.applyTo(compMatrixS);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(compMatrixS);
        lblMatrixP = new Label(compPrivateKeyData, SWT.NONE);
        lblMatrixP.setText("P = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixP);
        compMatrixP = new InteractiveMatrix(compPrivateKeyData, 7, 7);
        compMatrixP.setPermutation(true);
        glf.applyTo(compMatrixP);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(compMatrixP);

        compInverseMatrices = new Composite(grpDecryption, SWT.NONE);
        glf.numColumns(4).applyTo(compInverseMatrices);
        lblMatrixSInverse = new Label(compInverseMatrices, SWT.NONE);
        lblMatrixSInverse.setText("S' = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixSInverse);
        textMatrixSInverse = matrixText(compInverseMatrices, SWT.LEFT, SWT.CENTER, 4, 4);
        lblMatrixPInverse = new Label(compInverseMatrices, SWT.NONE);
        lblMatrixPInverse.setText("P' = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixPInverse);
        textMatrixPInverse = matrixText(compInverseMatrices, SWT.LEFT, SWT.CENTER, 7, 7);
        gdf.applyTo(compInverseMatrices);

        grpOutput = new Group(grpDecryption, SWT.NONE);
        glf.numColumns(1).applyTo(grpOutput);
        gdf.applyTo(grpOutput);
        grpOutput.setText(Messages.HammingCryptoView_lblOutput);
        textDecoded = codeText(grpOutput, SWT.FILL, SWT.TOP);
        textOutput = new Text(grpOutput, SWT.NONE);
        gdf.applyTo(textOutput);

        grpEncryption = new Group(mainComposite, SWT.NONE);
        grpEncryption.setText(Messages.HammingCryptoView_grpEncryption);
        glf.numColumns(1).applyTo(grpEncryption);
        gdf.applyTo(grpEncryption);

        grpInputStep = new Group(grpEncryption, SWT.NONE);
        glf.applyTo(grpInputStep);
        gdf.applyTo(grpInputStep);
        grpInputStep.setText(Messages.HammingCryptoView_lblTextOriginal);
        textInput = new Text(grpInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> updateVector());
        textAsBinary = codeText(grpInputStep, SWT.FILL, SWT.TOP);
        gdf.applyTo(textAsBinary);

        grpPublicKey = new Group(grpEncryption, SWT.NONE);
        glf.numColumns(2).applyTo(grpPublicKey);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).applyTo(grpPublicKey);
        grpPublicKey.setText(Messages.HammingCryptoView_grpPublicKey);
        lblMatrixGSP = new Label(grpPublicKey, SWT.NONE);
        lblMatrixGSP.setText("G' = SGP = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixGSP);
        textMatrixGSP = matrixText(grpPublicKey, SWT.LEFT, SWT.CENTER, 7, 4);

        grpCiphertext = new Group(grpEncryption, SWT.NONE);
        glf.numColumns(1).applyTo(grpCiphertext);
        gdf.applyTo(grpCiphertext);
        grpCiphertext.setText(Messages.HammingCryptoView_lblEncrypt);
        textEncrypted = codeText(grpCiphertext, SWT.FILL, SWT.BOTTOM);

        compInfoText = new Composite(grpEncryption, SWT.NONE);
        glf.applyTo(compInfoText);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BOTTOM).grab(true, true).hint(_WHINT, SWT.DEFAULT).applyTo(compInfoText);

        grpControlButtons = new Group(compInfoText, SWT.NONE);
        RowLayoutFactory.fillDefaults().pack(false).spacing(10).applyTo(grpControlButtons);
        btnPrev = new Button(grpControlButtons, SWT.NONE);
        btnPrev.setText(Messages.GeneralEccView_btnPrev);
        btnPrev.addListener(SWT.Selection, e -> prevStep());
        btnNextStep = new Button(grpControlButtons, SWT.NONE);
        btnNextStep.setText(Messages.GeneralEccView_btnNextStep);
        btnNextStep.addListener(SWT.Selection, e -> nextStep());
        btnReset = new Button(grpControlButtons, SWT.NONE);
        btnReset.setText(Messages.GeneralEccView_btnReset);
        btnReset.addListener(SWT.Selection, e -> initView());
        grpTextInfo = new Group(compInfoText, SWT.NONE);
        glf.applyTo(grpTextInfo);
        gdf.applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.HammingCryptoView_grpTextInfo);
        textInfo = UIHelper.mutltiLineText(grpTextInfo, SWT.FILL, SWT.FILL, SWT.DEFAULT, 6, null, true);

        bindValues();
        initView();
                
    }

    private void updateVector() {
        mce.stringToArray();
        textAsBinary.setText(data.getBinary().toString());
    }

    /**
     * Initializes the view, resetting elements.
     */
    private void initView() {
        resetKeys();
        textInput.setText("password"); //$NON-NLS-1$
        textInfo.setText(Messages.HammingCryptoView_step1);
        updateVector();
        btnPrev.setEnabled(false);
        btnGeneratePrivateKey.setEnabled(true);
        btnNextStep.setEnabled(true);
        grpPublicKey.setVisible(false);
        grpCiphertext.setVisible(false);
        compInverseMatrices.setVisible(false);
        grpOutput.setVisible(false);
        textMatrixG.setText(data.getMatrixG().toString());
    }

    /**
     * Display Next step by iterating the shown view elements.
     */
    private void nextStep() {
        if (!grpPublicKey.isVisible()) {
            try {
                generateKey(false);
                mce.computePublicKey();
                mce.encrypt();
                textMatrixGSP.setText(data.getMatrixSGP().toString());
                textEncrypted.setText(data.getEncrypted().toString());
                textInfo.setText(Messages.HammingCryptoView_step2);
                grpPublicKey.setVisible(true);
                grpCiphertext.setVisible(true);
                btnPrev.setEnabled(true);
                btnGeneratePrivateKey.setEnabled(false);
            } catch (MatrixException e) {
                MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
                dialog.setText("Input Error");
                dialog.setMessage(e.getMessage());
                // open dialog and await user selection
                dialog.open();
            }

        } else if (!grpOutput.isVisible()) {
            mce.decrypt();
            // UIHelper.markCode(textCorrected, SWT.COLOR_CYAN, ecc.getBitErrors());
            textMatrixPInverse.setText(data.getMatrixPInv().toString());
            textMatrixSInverse.setText(data.getMatrixSInv().toString());
            textInfo.setText(Messages.HammingCryptoView_step3);
            compInverseMatrices.setVisible(true);
            grpOutput.setVisible(true);
            btnNextStep.setEnabled(false);
        }
    }

    /**
     * Display previous step by iterating the hidden view elements.
     */
    private void prevStep() {
        if (grpOutput.isVisible()) {
            textInfo.setText(Messages.HammingCryptoView_step2);
            compInverseMatrices.setVisible(false);
            grpOutput.setVisible(false);
            btnNextStep.setEnabled(true);
        } else if (grpPublicKey.isVisible()) {
            textInfo.setText(Messages.HammingCryptoView_step1);
            grpCiphertext.setVisible(false);
            grpPublicKey.setVisible(false);
            btnPrev.setEnabled(false);
            btnGeneratePrivateKey.setEnabled(true);

        }
    }

    private void resetKeys() {
        compMatrixS.reset();
        compMatrixP.reset();
        compMatrixS.setModified(false);
        compMatrixP.setModified(false);
        data.setMatrixSInv(null);
        data.setMatrixPInv(null);
    }
    
    /**
     * Generate the private and public key matrices as well as their inverses.
     * @param reset if true, discard any user input by resetting S and P to all 0
     */

    private void generateKey(boolean reset) {
        if (reset) {
           resetKeys();
        }

        Matrix2D p, s, invS, invP;
        
        //when matrices have not been set yet generate them automatically
        if (!compMatrixP.isModified()) {
            mce.randomPermutationMatrix(7);
            //compMatrixP.setMatrix(data.getMatrixP());
        } else {
            invP = compMatrixP.getMatrix().invert();
            if (invP == null)
                throw new MatrixException("Matrix P is singular, no inverse could be found!");
            if (compMatrixP.getMatrix().equals(invP))
                throw new MatrixException("The inverse of Matrix P is equal, please choose other parameters.");
            //data.setMatrixP(compMatrixP.getMatrix());
            data.setMatrixPInv(invP);
        }

        if (!compMatrixS.isModified()) {
            mce.randomMatrix(4, 4);
            //compMatrixS.setMatrix(s);
        } else {
            invS = compMatrixS.getMatrix().invert();
            if (invS == null)
                throw new MatrixException("Matrix S is singular, no inverse could be found!");
            if (compMatrixS.getMatrix().equals(invS))
                throw new MatrixException("The inverse of Matrix S is equal, please choose other parameters.");
            //data.setMatrixS(s);
            data.setMatrixSInv(invS);
        }

        textMatrixSInverse.setText(data.getMatrixSInv().toString());
        try {
            textMatrixPInverse.setText(data.getMatrixPInv().toString());
        } catch (NullPointerException e) {
            LogUtil.logError(e);
        }
    }

    /**
     * Bind model values to view elements using the JFace framework.
     * 
     */
    private void bindValues() {
        dbc = new DataBindingContext();

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textInput),
                BeanProperties.value(HammingData.class, "originalString", String.class).observe(mce.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textAsBinary),
                BeanProperties.value(HammingData.class, "binaryAsString", String.class).observe(mce.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textEncrypted),
                BeanProperties.value(HammingData.class, "codeAsString", String.class).observe(mce.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textDecoded),
                BeanProperties.value(HammingData.class, "binaryDecoded", String.class).observe(mce.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textOutput),
                BeanProperties.value(HammingData.class, "decodedString", String.class).observe(mce.getData())); //$NON-NLS-1$
               
        dbc.bindValue(new InteractiveMatrixProperty().observe(compMatrixP),
                BeanProperties.value(HammingData.class, "matrixP", Matrix2D.class).observe(mce.getData())); //$NON-NLS-1$

        dbc.bindValue(new InteractiveMatrixProperty().observe(compMatrixS),
                BeanProperties.value(HammingData.class, "matrixS", Matrix2D.class).observe(mce.getData())); //$NON-NLS-1$
        
        
    }

}
