package org.jcryptool.visual.errorcorrectingcodes.ui;

import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowDataFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.EccController;
import org.jcryptool.visual.errorcorrectingcodes.McElieceSystem;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;

public class McElieceView extends Composite {

    private EccController ecc;
    private EccData eccData;
    private McElieceSystem mce;
    private DataBindingContext dbc;

    private Composite parent;
    private Composite mainComposite;
    private Composite compHead;
    private Composite compInfoText;
    private Composite compInputStep;
    private Composite compPrivateKeyText;
    private Composite compPrivateKeyButton;
    private Group grpDecryption;
    private Group grpPrivateKey;
    private Group grpErrorCode;
    private Group grpEncryption;
    private Group grpControlButtons;
    private Group grpTextInfo;

    private StyledText textAsBinary;
    private Text textInput;
    private StyledText textEncrypted;
    private StyledText textOutput;
    private StyledText textCorrected;
    private StyledText textError;
    private StyledText textInfo;
    private StyledText textMatrixG;
    private InteractiveMatrix compMatrixP;
    private InteractiveMatrix compMatrixS;

    private Button btnReset;
    private Button btnNextStep;
    private Button btnPrev;
    private Button btnGeneratePrivateKey;

    private Label lblHeader;
    private Label lblTextOriginal;
    private Label lblEncrypt;
    private Label lblCorrected;
    private Label lblOutput;
    private Label lblMatrixG;
    private Label lblMatrixS;
    private Label lblMatrixP;
    private Label lblMatrixGSP;
    private StyledText textMatrixGSP;
    private Group grpPublicKey;
    private Composite compEncrypted;
    private Composite compOutputStep;

    public McElieceView(Composite parent, int style) {
        super(parent, style);
        eccData = new EccData();
        ecc = new EccController(eccData);
        mce = new McElieceSystem(eccData);
        this.parent = parent;
        Point margins = new Point(5, 5);
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(margins);

        glf.applyTo(this);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        GridDataFactory.fillDefaults().applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.McElieceView_lblHeader);

        mainComposite = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().margins(5, 5).numColumns(2).spacing(100, SWT.DEFAULT).equalWidth(true).applyTo(mainComposite);
        GridDataFactory.fillDefaults().applyTo(mainComposite);

        grpDecryption = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpDecryption);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpDecryption);
        grpDecryption.setText(Messages.McElieceView_grpDecryption);
        
        grpPrivateKey = new Group(grpDecryption, SWT.NONE);
        grpPrivateKey.setText("Private Key");
        glf.applyTo(grpPrivateKey);
        GridDataFactory.fillDefaults().applyTo(grpPrivateKey);
        compPrivateKeyButton = new Composite(grpPrivateKey, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(compPrivateKeyButton);
        btnGeneratePrivateKey = new Button(compPrivateKeyButton, SWT.NONE);
        btnGeneratePrivateKey.setText("Generate Key");
        btnGeneratePrivateKey.addListener(SWT.Selection, e -> generateKey());
        
        compPrivateKeyText = new Composite(grpPrivateKey, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(6).spacing(20, SWT.DEFAULT).margins(margins).applyTo(compPrivateKeyText);
        GridDataFactory.fillDefaults().applyTo(compPrivateKeyText);
        lblMatrixG = new Label(compPrivateKeyText, SWT.NONE);
        lblMatrixG.setText("G = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lblMatrixG);
        textMatrixG = matrixText(compPrivateKeyText, SWT.LEFT, SWT.CENTER, 7, 4);
        lblMatrixS = new Label(compPrivateKeyText, SWT.NONE);
        lblMatrixS.setText("S = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lblMatrixS);
        compMatrixS = new InteractiveMatrix(compPrivateKeyText, 4, 4);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(compMatrixS);
        lblMatrixP = new Label(compPrivateKeyText, SWT.NONE);
        lblMatrixP.setText("P = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).applyTo(lblMatrixP);
        compMatrixP = new InteractiveMatrix(compPrivateKeyText, 7, 7);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(compMatrixP);
              
        compOutputStep = new Composite(grpDecryption, SWT.NONE);
        glf.numColumns(1).applyTo(compOutputStep);
        GridDataFactory.fillDefaults().applyTo(compOutputStep);
        lblCorrected = new Label(compOutputStep, SWT.NONE);
        lblCorrected.setText(Messages.McElieceView_lblCorrected);
        textCorrected = codeText(compOutputStep, SWT.FILL, SWT.BOTTOM);
        lblOutput = new Label(compOutputStep, SWT.NONE);
        lblOutput.setText(Messages.McElieceView_lblOutput);
        textOutput = codeText(compOutputStep, SWT.FILL, SWT.TOP);
        
        grpEncryption = new Group(mainComposite, SWT.NONE);
        grpEncryption.setText(Messages.McElieceView_grpEncryption);
        glf.applyTo(grpEncryption);
        GridDataFactory.fillDefaults().applyTo(grpEncryption);

        compInputStep = new Composite(grpEncryption, SWT.NONE);
        glf.applyTo(compInputStep);
        GridDataFactory.fillDefaults().applyTo(compInputStep);
        lblTextOriginal = new Label(compInputStep, SWT.NONE);
        lblTextOriginal.setText(Messages.McElieceView_lblTextOriginal);
        textInput = new Text(compInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> updateVector());
        textAsBinary = codeText(compInputStep, SWT.FILL, SWT.TOP);
        
        grpPublicKey = new Group(grpEncryption, SWT.NONE);
        glf.numColumns(2).applyTo(grpPublicKey);
        GridDataFactory.fillDefaults().applyTo(grpPublicKey);
        lblMatrixGSP = new Label(grpPublicKey, SWT.NONE);
        lblMatrixGSP.setText("G' = SGP = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixGSP);
        textMatrixGSP = matrixText(grpPublicKey, SWT.LEFT, SWT.CENTER, 7, 4);
        
        compEncrypted = new Composite(grpEncryption, SWT.NONE);
        glf.numColumns(1).applyTo(compEncrypted);
        GridDataFactory.fillDefaults().applyTo(compEncrypted);
        lblEncrypt = new Label(compEncrypted, SWT.NONE);
        lblEncrypt.setText(Messages.McElieceView_lblEncrypt);
        textEncrypted = codeText(compEncrypted, SWT.FILL, SWT.BOTTOM);
      
        grpErrorCode = new Group(grpEncryption, SWT.NONE);
        glf.applyTo(grpErrorCode);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpErrorCode);
        grpErrorCode.setText(Messages.McElieceView_grpErrorCode);
        textError = codeText(grpErrorCode, SWT.FILL, SWT.CENTER);
        
        compInfoText = new Composite(this, SWT.NONE);
        glf.applyTo(compInfoText);
        GridDataFactory.fillDefaults().applyTo(compInfoText);

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
        GridDataFactory.fillDefaults().applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.McElieceView_grpTextInfo);
        textInfo = new StyledText(grpTextInfo, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        GridDataFactory.fillDefaults().hint(SWT.DEFAULT, textInfo.getLineHeight()*8).applyTo(textInfo);
        textInfo.setText(Messages.McElieceView_step1);

        bindValues();
        initView();
    }

    private void generateKey() {
        mce.fillPrivateKey();
        compMatrixS.setMatrix(eccData.getMatrixS());
        compMatrixP.setMatrix(eccData.getMatrixP());
    }

    private void updateVector() {
        mce.stringToArray();
        textAsBinary.setText(mce.getBinary().toString());
    }
    
    
    /**
     * Initializes the view by hiding later steps.
     */
    private void initView() {
        textInput.setText("hello"); //$NON-NLS-1$
        btnPrev.setEnabled(false);
        updateVector();
        grpPrivateKey.setVisible(false);
        grpPublicKey.setVisible(false);
        grpErrorCode.setVisible(false);
        compOutputStep.setVisible(false);
        textMatrixG.setText(eccData.getMatrixG().toString());
    }

    /**
     * Display Next step by iterating the shown view elements.
     */
    private void nextStep() {
        if (!grpPrivateKey.isVisible()) {
            btnPrev.setEnabled(true);
            grpPrivateKey.setVisible(true);
            textInfo.setText(Messages.McElieceView_step2);
        } else if (!grpPublicKey.isVisible()) {
            // TODO remove auto generate when algorithm is ready
            generateKey();
            textMatrixGSP.setText(mce.getSGP().toString());
            grpPublicKey.setVisible(true);
        } else if (!grpErrorCode.isVisible()) {
            mce.encrypt();
            //UIHelper.markCode(textError, SWT.COLOR_RED, ecc.getBitErrors());
            grpErrorCode.setVisible(true);
            textEncrypted.setText(mce.getEncrypted().toHexString());
            textInfo.setText(Messages.McElieceView_step3);
        } else if (!compOutputStep.isVisible()) {
            ecc.correctErrors();
            UIHelper.markCode(textCorrected, SWT.COLOR_CYAN, ecc.getBitErrors());
            compOutputStep.setVisible(true);
            textInfo.setText(Messages.McElieceView_step4);
            btnNextStep.setEnabled(false);
        }
    }

    /**
     * Display previous step by iterating the hidden view elements.
     */
    private void prevStep() {
        if (compOutputStep.isVisible()) {
            btnNextStep.setEnabled(true);
            compOutputStep.setVisible(false);
            textInfo.setText(Messages.McElieceView_step3); // $NON-NLS-1$
        } else if (grpErrorCode.isVisible()) {
            grpErrorCode.setVisible(false);
            textInfo.setText(Messages.McElieceView_step2); // $NON-NLS-1$
        } else if (grpPrivateKey.isVisible()) {
            grpPublicKey.setVisible(false);
        } else if (!grpPublicKey.isVisible()) {
            grpPrivateKey.setVisible(false);
            btnPrev.setEnabled(false);
            textInfo.setText(Messages.McElieceView_step1); // $NON-NLS-1$
        }
    }

    /**
     * Bind model values to view elements using the JFace framework.
     * 
     */
    private void bindValues() {
        dbc = new DataBindingContext();

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textInput),
                BeanProperties.value(EccData.class, "originalString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textAsBinary),
                BeanProperties.value(EccData.class, "decodedString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textEncrypted),
                BeanProperties.value(EccData.class, "codeAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textError),
                BeanProperties.value(EccData.class, "codeStringWithErrors", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textCorrected),
                BeanProperties.value(EccData.class, "correctedString", String.class).observe(ecc.getData())); //$NON-NLS-1$

    }

}
