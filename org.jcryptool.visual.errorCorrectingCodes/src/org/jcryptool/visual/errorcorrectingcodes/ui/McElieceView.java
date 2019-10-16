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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.EccController;
import org.jcryptool.visual.errorcorrectingcodes.McElieceSystem;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;
import org.jcryptool.visual.errorcorrectingcodes.data.Matrix2D;
import org.jcryptool.visual.errorcorrectingcodes.data.MatrixException;

import com.sun.org.glassfish.external.statistics.annotations.Reset;

public class McElieceView extends Composite {

    private EccController ecc;
    private EccData eccData;
    private McElieceSystem mce;
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
    private Group grpEncrypted;
    private Group grpDecryptStep;

    private Text textOutput;
    private Text textInput;

    private StyledText textAsBinary;
    private StyledText textEncrypted;
    private StyledText textInfo;
    private StyledText textMatrixG;
    private StyledText textMatrixSInverse;
    private StyledText textMatrixPInverse;
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
    private StyledText textMatrixGSP;
    private StyledText textDecoded;
    private Label lblClearText;
    private Label lblMatrixSInverse;
    private Label lblMatrixPInverse;

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
        GridLayoutFactory.fillDefaults().margins(margins).numColumns(2).spacing(80, SWT.DEFAULT).equalWidth(true)
                .applyTo(mainComposite);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(mainComposite);

        grpDecryption = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpDecryption);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpDecryption);
        grpDecryption.setText(Messages.McElieceView_grpDecryption);

        grpPrivateKey = new Group(grpDecryption, SWT.NONE);
        grpPrivateKey.setText(Messages.McElieceView_grpPrivateKey);
        glf.applyTo(grpPrivateKey);
        GridDataFactory.fillDefaults().applyTo(grpPrivateKey);
        compPrivateKeyButton = new Composite(grpPrivateKey, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(compPrivateKeyButton);
        btnGeneratePrivateKey = new Button(compPrivateKeyButton, SWT.NONE);
        btnGeneratePrivateKey.setText(Messages.McElieceView_btnGeneratePrivateKey);
        btnGeneratePrivateKey.addListener(SWT.Selection, e -> {
            resetKeys();
            generateKey();  
        });

        compPrivateKeyData = new Composite(grpPrivateKey, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(6).spacing(20, SWT.DEFAULT).margins(margins)
                .applyTo(compPrivateKeyData);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compPrivateKeyData);
        lblMatrixG = new Label(compPrivateKeyData, SWT.NONE);
        lblMatrixG.setText("G = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixG);
        textMatrixG = matrixText(compPrivateKeyData, SWT.LEFT, SWT.CENTER, 7, 4);
        lblMatrixS = new Label(compPrivateKeyData, SWT.NONE);
        lblMatrixS.setText("S = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixS);
        compMatrixS = new InteractiveMatrix(compPrivateKeyData, 4, 4);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(compMatrixS);
        lblMatrixP = new Label(compPrivateKeyData, SWT.NONE);
        lblMatrixP.setText("P = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixP);
        compMatrixP = new InteractiveMatrix(compPrivateKeyData, 7, 7);
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
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compInverseMatrices);

        grpOutput = new Group(grpDecryption, SWT.NONE);
        glf.numColumns(1).applyTo(grpOutput);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpOutput);
        grpOutput.setText(Messages.McElieceView_lblOutput);
        textDecoded = codeText(grpOutput, SWT.FILL, SWT.TOP);
        textOutput = new Text(grpOutput, SWT.NONE);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(textOutput);

        grpEncryption = new Group(mainComposite, SWT.NONE);
        grpEncryption.setText(Messages.McElieceView_grpEncryption);
        glf.numColumns(1).applyTo(grpEncryption);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpEncryption);

        grpInputStep = new Group(grpEncryption, SWT.NONE);
        glf.applyTo(grpInputStep);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(grpInputStep);
        grpInputStep.setText(Messages.McElieceView_lblTextOriginal);
        textInput = new Text(grpInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> updateVector());
        textAsBinary = codeText(grpInputStep, SWT.FILL, SWT.TOP);

        grpPublicKey = new Group(grpEncryption, SWT.NONE);
        glf.numColumns(2).applyTo(grpPublicKey);
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.TOP).applyTo(grpPublicKey);
        grpPublicKey.setText(Messages.McElieceView_grpPublicKey);
        lblMatrixGSP = new Label(grpPublicKey, SWT.NONE);
        lblMatrixGSP.setText("G' = SGP = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixGSP);
        textMatrixGSP = matrixText(grpPublicKey, SWT.LEFT, SWT.CENTER, 7, 4);

        grpEncrypted = new Group(grpEncryption, SWT.NONE);
        glf.numColumns(1).applyTo(grpEncrypted);
        GridDataFactory.fillDefaults().applyTo(grpEncrypted);
        grpEncrypted.setText(Messages.McElieceView_lblEncrypt);
        textEncrypted = codeText(grpEncrypted, SWT.FILL, SWT.BOTTOM);

        compInfoText = new Composite(this, SWT.NONE);
        glf.applyTo(compInfoText);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compInfoText);

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
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.McElieceView_grpTextInfo);
        textInfo = UIHelper.mutltiLineText(grpTextInfo, SWT.FILL, SWT.FILL, parent.getBounds().width - 100, 8, null);

        bindValues();
        initView();
    }

    private void generateKey() {
        Matrix2D p, s, invS, invP;
        if (!compMatrixP.isModified()) {
            p = mce.randomPermutationMatrix(7);
            invP = p.invert();
            compMatrixP.setMatrix(p);
        } else {
            p = compMatrixP.getMatrix();
            invP = p.invert();
            if (invP == null)
                throw new MatrixException("Matrix P is singular, no inverse could be found!");
        }

        if (!compMatrixS.isModified()) {
            do {
                s = mce.randomMatrix(4, 4);
                invS = s.invert();
            } while (invS == null);
            compMatrixS.setMatrix(s);
        } else {
            s = compMatrixS.getMatrix();
            invS = s.invert();
            if (invS == null)
                throw new MatrixException("Matrix S is singular, no inverse could be found!");
        }
        
        Matrix2D check = s.multBinary(invS);
        assert (check != null);

        mce.setMatrixP(p);
        mce.setMatrixPInv(invP);
        mce.setMatrixS(s);
        mce.setMatrixSInv(invS);

    }

    private void updateVector() {
        mce.stringToArray();
        textAsBinary.setText(mce.getBinary().toString());
    }

    /**
     * Initializes the view by hiding later steps.
     */
    private void initView() {
        resetKeys();
        textInput.setText("password"); //$NON-NLS-1$
        textInfo.setText(Messages.McElieceView_step1);
        UIHelper.formatTextOccurrence(textInfo, Messages.McElieceView_demoNote, SWT.BOLD);
        updateVector();
        btnPrev.setEnabled(false);
        btnGeneratePrivateKey.setEnabled(true);
        btnNextStep.setEnabled(true);
        grpPublicKey.setVisible(false);
        grpEncrypted.setVisible(false);
        compInverseMatrices.setVisible(false);
        grpOutput.setVisible(false);
        textMatrixG.setText(mce.getMatrixG().toString());
    }

    private void resetKeys() {
        compMatrixS.reset();
        compMatrixP.reset();
        compMatrixS.setModified(false);
        compMatrixP.setModified(false);        
    }

    /**
     * Display Next step by iterating the shown view elements.
     */
    private void nextStep() {
        if (!grpPublicKey.isVisible()) {
            try {
                generateKey();
                mce.computePublicKey();
                mce.encrypt();
                textMatrixGSP.setText(mce.getMatrixSGP().toString());
                textEncrypted.setText(mce.getEncrypted().toString());
                textInfo.setText(Messages.McElieceView_step2);
                grpPublicKey.setVisible(true);
                grpEncrypted.setVisible(true);
                btnPrev.setEnabled(true);
                btnGeneratePrivateKey.setEnabled(false);              
            } catch(MatrixException e) {
                MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
                dialog.setText("Input Error");
                dialog.setMessage(e.getMessage());
                // open dialog and await user selection
                dialog.open();            
            }
            
           
        } else if (!grpOutput.isVisible()) {
            mce.decrypt();
            // UIHelper.markCode(textCorrected, SWT.COLOR_CYAN, ecc.getBitErrors());
            textMatrixPInverse.setText(mce.getMatrixPInv().toString());
            textMatrixSInverse.setText(mce.getMatrixSInv().toString());
            textInfo.setText(Messages.McElieceView_step3);
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
            textInfo.setText(Messages.McElieceView_step2);
            compInverseMatrices.setVisible(false);
            grpOutput.setVisible(false);
            btnNextStep.setEnabled(true);
        } else if (grpPublicKey.isVisible()) {
            textInfo.setText(Messages.McElieceView_step1);
            UIHelper.formatTextOccurrence(textInfo, Messages.McElieceView_demoNote, SWT.BOLD);
            grpEncrypted.setVisible(false);
            grpPublicKey.setVisible(false);
            btnPrev.setEnabled(false);
            btnGeneratePrivateKey.setEnabled(true);

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
                BeanProperties.value(EccData.class, "binaryAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textEncrypted),
                BeanProperties.value(EccData.class, "codeAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textDecoded),
                BeanProperties.value(EccData.class, "binaryDecoded", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textOutput),
                BeanProperties.value(EccData.class, "decodedString", String.class).observe(ecc.getData())); //$NON-NLS-1$

    }

}
