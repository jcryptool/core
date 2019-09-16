package org.jcryptool.visual.errorcorrectingcodes.ui;

import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.EccController;
import org.jcryptool.visual.errorcorrectingcodes.EccData;
import org.jcryptool.visual.errorcorrectingcodes.McElieceSystem;

public class McElieceView extends Composite {

    private EccController ecc;
    private McElieceSystem mce;
    private DataBindingContext dbc;
    
    private Composite parent;
    private Composite mainComposite;
    private Composite compProcedure;
    private Composite compHead;
    private Composite compInfoText;
    private Composite compInputStep;
    private Composite compMatrices;
    private Group grpSender;
    private Group grpEncodeStep;
    private Group grpErrorCode;
    private Group grpDecodeStep;
    private Group grpControlButtons;
    private Group grpTextInfo;
    
    private StyledText textAsBinary;
    private StyledText textInput;
    private StyledText textEncoded;
    private StyledText textOutput;
    private StyledText textCorrected;
    private StyledText textError;
    private StyledText textInfo;
    private StyledText textMatrixG;
    private StyledText textMatrixS;
    private StyledText textMatrixP;
   
    private Button btnReset;
    private Button btnNextStep;
    private Button btnPrev;    
    private Label lblHeader;
    private Label lblTextOriginal;
    private Label lblEncode;
    private Label lblCorrected;
    private Label lblOutput;    
    private Label lblMatrixG;
    private Label lblMatrixS;
    private Label lblMatrixP;
    private Label lblMatrixGSP;
    private StyledText textMatrixGSP;
    
    public McElieceView(Composite parent, int style) {
        super(parent, style);

        ecc = new EccController(new EccData());
        mce = new McElieceSystem();
        this.parent = parent;
        Point widthhint = new Point(1000, SWT.DEFAULT);
        Point colSize = new Point(500, SWT.DEFAULT);

        GridLayoutFactory.fillDefaults().applyTo(this);
        GridDataFactory.fillDefaults().grab(true, true).hint(widthhint).applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compHead);
        GridDataFactory.fillDefaults().hint(widthhint).applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.McElieceView_lblHeader); 

        mainComposite = new Composite(this, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(mainComposite);
        GridDataFactory.fillDefaults().applyTo(mainComposite);

        compProcedure = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compProcedure);
        GridDataFactory.fillDefaults().hint(colSize).applyTo(compProcedure);
        
        grpSender = new Group(compProcedure, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpSender);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpSender);
        grpSender.setText(Messages.McElieceView_grpSender); 
        compInputStep = new Composite(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compInputStep);
        GridDataFactory.fillDefaults().applyTo(compInputStep);
        lblTextOriginal = new Label(compInputStep, SWT.NONE);
        lblTextOriginal.setText(Messages.McElieceView_lblTextOriginal); 
        textInput = new StyledText(compInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> ecc.textAsBinary());
        textAsBinary = multiLineStyledText(grpSender, SWT.FILL, SWT.TOP);
       
        grpEncodeStep = new Group(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpEncodeStep);
        GridDataFactory.fillDefaults().applyTo(grpEncodeStep);
        compMatrices = new Composite(grpEncodeStep, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(6).applyTo(compMatrices);
        GridDataFactory.fillDefaults().applyTo(compMatrices);
        lblMatrixG = new Label(compMatrices, SWT.NONE);
        lblMatrixG.setText("G = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixG);
        textMatrixG = matrixText(compMatrices, SWT.LEFT, SWT.CENTER, 7, 4);
        lblMatrixS = new Label(compMatrices, SWT.NONE);
        lblMatrixS.setText("S = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixS);
        textMatrixS = matrixText(compMatrices, SWT.LEFT, SWT.CENTER, 4, 4);
        lblMatrixP = new Label(compMatrices, SWT.NONE);
        lblMatrixP.setText("P = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixP);
        textMatrixP = matrixText(compMatrices, SWT.LEFT, SWT.CENTER, 7, 7);
        lblMatrixGSP = new Label(compMatrices, SWT.NONE);
        lblMatrixGSP.setText("G' = SGP = "); //$NON-NLS-1$
        GridDataFactory.fillDefaults().align(SWT.LEFT, SWT.CENTER).applyTo(lblMatrixGSP);
        textMatrixGSP = matrixText(compMatrices, SWT.LEFT, SWT.CENTER, 7, 4);

        
        lblEncode = new Label(grpEncodeStep, SWT.NONE);
        lblEncode.setText(Messages.McElieceView_lblEncode); 
        textEncoded = multiLineStyledText(grpEncodeStep, SWT.FILL, SWT.BOTTOM);

        grpErrorCode = new Group(compProcedure, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpErrorCode);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpErrorCode);
        grpErrorCode.setText(Messages.McElieceView_grpErrorCode); 
        textError = multiLineStyledText(grpErrorCode, SWT.FILL, SWT.CENTER);
        // GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.FILL).applyTo(textError);
       
        grpDecodeStep = new Group(compProcedure, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpDecodeStep);
        GridDataFactory.fillDefaults().applyTo(grpDecodeStep);
        grpDecodeStep.setText(Messages.McElieceView_grpDecodeStep); 
        lblCorrected = new Label(grpDecodeStep, SWT.NONE);
        lblCorrected.setText(Messages.McElieceView_lblCorrected); 
        textCorrected = multiLineStyledText(grpDecodeStep, SWT.FILL, SWT.BOTTOM);
        lblOutput = new Label(grpDecodeStep, SWT.NONE);
        lblOutput.setText(Messages.McElieceView_lblOutput); 
        textOutput = multiLineStyledText(grpDecodeStep, SWT.FILL, SWT.TOP);
        
        compInfoText = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compInfoText);
        GridDataFactory.fillDefaults().hint(colSize).applyTo(compInfoText);

        grpTextInfo = new Group(compInfoText, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpTextInfo);
        GridDataFactory.fillDefaults().applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.McElieceView_grpTextInfo); 
        textInfo = new StyledText(grpTextInfo, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        GridDataFactory.fillDefaults().hint(colSize.x-20, 600).applyTo(textInfo);
        textInfo.setText(Messages.McElieceView_step1);

        grpControlButtons = new Group(compInfoText, SWT.NONE);
        RowLayoutFactory.fillDefaults().pack(false).spacing(10).applyTo(grpControlButtons);
        btnPrev = new Button(grpControlButtons, SWT.NONE);
        btnPrev.setText(Messages.McElieceView_btnPrev); 
        btnPrev.addListener(SWT.Selection, e -> prevStep());
        btnNextStep = new Button(grpControlButtons, SWT.NONE);
        btnNextStep.setText(Messages.McElieceView_btnNextStep); 
        btnNextStep.addListener(SWT.Selection, e -> nextStep());
        btnReset = new Button(grpControlButtons, SWT.NONE);
        btnReset.setText(Messages.McElieceView_btnReset); 
        btnReset.addListener(SWT.Selection, e -> initView());
        bindValues();
        initView();
    }
    
    /**
     * Initializes the view by hiding later steps.
     */
    private void initView() {
        textInput.setText("hello"); //$NON-NLS-1$
        ecc.textAsBinary();
        btnPrev.setEnabled(false);
        grpEncodeStep.setVisible(false);
        grpErrorCode.setVisible(false);
        grpDecodeStep.setVisible(false);
        textMatrixG.setText(mce.printMatrix(mce.getG()));
        textMatrixS.setText(mce.printMatrix(mce.getS()));
        textMatrixP.setText(mce.printMatrix(mce.getP()));
        textMatrixGSP.setText(mce.printMatrix(mce.getSGP()));
    }
    
    /**
     * Display Next step by iterating the shown view elements.
     */
    private void nextStep() {
        if (!grpEncodeStep.isVisible()) {
            ecc.encodeBits();
            btnPrev.setEnabled(true);
            grpEncodeStep.setVisible(true);
            textInfo.setText(Messages.McElieceView_step2);
        } else if (!grpErrorCode.isVisible()) {
            ecc.flipBits();
            UIHelper.markCode(textError, SWT.COLOR_RED, ecc.getBitErrors());
            grpErrorCode.setVisible(true);
            textInfo.setText(Messages.McElieceView_step3);
        } else if (!grpDecodeStep.isVisible()) {
            ecc.correctErrors();
            UIHelper.markCode(textCorrected, SWT.COLOR_CYAN, ecc.getBitErrors());
            grpDecodeStep.setVisible(true);
            textInfo.setText(Messages.McElieceView_step4);
            btnNextStep.setEnabled(false);
        }
    }

    /**
     * Display previous step by iterating the hidden view elements.
     */
    private void prevStep() {
      if (grpDecodeStep.isVisible()) {
            btnNextStep.setEnabled(true);
            grpDecodeStep.setVisible(false);
            textInfo.setText(Messages.McElieceView_step3); //$NON-NLS-1$
        } else if (grpErrorCode.isVisible()) {
            grpErrorCode.setVisible(false);
            textInfo.setText(Messages.McElieceView_step2); //$NON-NLS-1$
        } else if (grpEncodeStep.isVisible()) {
            btnPrev.setEnabled(false);
            grpEncodeStep.setVisible(false);
            textInfo.setText(Messages.McElieceView_step1); //$NON-NLS-1$
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

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textOutput),
                BeanProperties.value(EccData.class, "binaryAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textAsBinary),
                BeanProperties.value(EccData.class, "binaryAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textEncoded),
                BeanProperties.value(EccData.class, "codeAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textError),
                BeanProperties.value(EccData.class, "codeStringWithErrors", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textCorrected),
                BeanProperties.value(EccData.class, "correctedString", String.class).observe(ecc.getData())); //$NON-NLS-1$
        
    }    
   
}
