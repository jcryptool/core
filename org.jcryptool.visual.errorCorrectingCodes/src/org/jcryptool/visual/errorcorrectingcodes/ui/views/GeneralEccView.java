/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.errorcorrectingcodes.ui.views;

import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.codeText;
import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.createArrowCanvas;
import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.markCode;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.algorithm.EccController;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;
import org.jcryptool.visual.errorcorrectingcodes.ui.Messages;
import org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper;
import org.jcryptool.visual.errorcorrectingcodes.ui.widget.ArrowCanvas;

/**
 * The Class GeneralEccView represents the common process of detecting and correcting errors when
 * transmitting data over a noisy channel.
 */
public class GeneralEccView extends Composite {

    private static final int _WHINT = 400;
    private EccController ecc;
    private DataBindingContext dbc;

    private Composite parent;
    private Composite mainComposite;
    private Composite compHead;
    private Composite compFoot;
    private Composite compInputStep;
    private Composite compEncodeStep;
    private Composite compArrowRight1;
    private Composite compArrowRight2;
    private Composite compArrowDown;
    private Composite compArrowUp;
    private Composite compOutputStep;   
    private Composite compDecodeStep;
    private Group grpSender;
    private Group grpErrorCode;
    private Group grpFootButtons;
    private Group grpTextInfo;
    private Group grpReceiver;

    private StyledText textInfoHead;
    private StyledText textAsBinary;
    private StyledText textInput;
    private StyledText textEncoded;
    private StyledText textInfo;
    private StyledText textOutput;
    private StyledText textCorrected;
    private StyledText textError;
    private Button btnReset;
    private Button btnNextStep;
    private Button btnPrev;
    private ArrowCanvas arrowDown;
    private ArrowCanvas arrowRight1;
    private ArrowCanvas arrowRight2;
    private ArrowCanvas arrowUp;

    private Label lblHeader;
    private Label lblTextOriginal;
    private Label lblTextEncoded;
    private Label lblTextDecoded;
    private Composite compErrorCode;


    /**
     * Instantiates a new general ecc view.
     */

    public GeneralEccView(Composite parent, int style) {
        super(parent, style);
        ecc = new EccController(new EccData());
        this.parent = parent;

        // common grid layout for all elements
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(5, 5).equalWidth(true);
        GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
        
        glf.applyTo(this);
        gdf.applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        gdf.applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.GeneralEccView_lblHeader);
        textInfoHead = new StyledText(compHead, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
        textInfoHead.setText(Messages.GeneralEccView_textHeader);
        GridDataFactory.fillDefaults().grab(true, false).hint(_WHINT, SWT.DEFAULT).applyTo(textInfoHead);

        
        mainComposite = new Composite(this, SWT.NONE);
        glf.equalWidth(false).numColumns(5).applyTo(mainComposite);
        gdf.applyTo(mainComposite);

        grpSender = new Group(mainComposite, SWT.NONE);
        glf.numColumns(1).applyTo(grpSender);
        gdf.applyTo(grpSender);
        grpSender.setText(Messages.GeneralEccView_grpSenderStep);
        compInputStep = new Composite(grpSender, SWT.NONE);
        glf.applyTo(compInputStep);
        GridDataFactory.fillDefaults().applyTo(compInputStep);
        lblTextOriginal = new Label(compInputStep, SWT.NONE);
        lblTextOriginal.setText(Messages.GeneralEccView_lblTextOriginal);
        textInput = new StyledText(compInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> ecc.textAsBinary());
        textAsBinary = codeText(compInputStep, SWT.FILL, SWT.TOP);
        compArrowDown = new Composite(grpSender, SWT.NONE);
        glf.numColumns(2).applyTo(compArrowDown);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.TOP).applyTo(compArrowDown);
        arrowDown = createArrowCanvas(compArrowDown, 10, 10, 10, 130, 3, 13.0);
        lblTextEncoded = new Label(compArrowDown, SWT.NONE);
        lblTextEncoded.setText(Messages.GeneralEccView_lblTextEncode);
        compEncodeStep = new Composite(grpSender, SWT.NONE);
        glf.numColumns(1).applyTo(compEncodeStep);
        GridDataFactory.fillDefaults().applyTo(compEncodeStep);
        textEncoded = codeText(compEncodeStep, SWT.FILL, SWT.BOTTOM);

        compArrowRight1 = new Composite(mainComposite, SWT.NONE);
        glf.applyTo(compArrowRight1);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowRight1);
        arrowRight1 = createArrowCanvas(compArrowRight1, 10, 10, 130, 10, 2, 10.0);
        arrowRight1.setLineStyle(SWT.LINE_DASH);

        grpErrorCode = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpErrorCode);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpErrorCode);
        grpErrorCode.setText(Messages.GeneralEccView_grpErrorCode);
        textError = codeText(grpErrorCode, SWT.FILL, SWT.CENTER);

        compArrowRight2 = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight2);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowRight2);
        arrowRight2 = createArrowCanvas(compArrowRight2, 10, 10, 130, 10, 2, 10.0);
        arrowRight2.setLineStyle(SWT.LINE_DASH);

        grpReceiver = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpReceiver);
        gdf.applyTo(grpReceiver);
        grpReceiver.setText(Messages.GeneralEccView_grpReceiver);
        compOutputStep = new Composite(grpReceiver, SWT.NONE);
        glf.applyTo(compOutputStep);
        GridDataFactory.fillDefaults().applyTo(compOutputStep);
        textOutput = codeText(compOutputStep, SWT.FILL, SWT.TOP);
        compArrowUp = new Composite(grpReceiver, SWT.NONE);
        glf.numColumns(2).applyTo(compArrowUp);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowUp);
        arrowUp = createArrowCanvas(compArrowUp, 10, 180, 10, 10, 3, 13.0);
        lblTextDecoded = new Label(compArrowUp, SWT.NONE);
        lblTextDecoded.setText(Messages.GeneralEccView_lblTextDecoded);
        compDecodeStep = new Composite(grpReceiver, SWT.NONE);
        glf.numColumns(1).applyTo(compDecodeStep);
        GridDataFactory.fillDefaults().applyTo(compDecodeStep);
        textCorrected = codeText(compDecodeStep, SWT.FILL, SWT.BOTTOM);

        compFoot = new Composite(this, SWT.NONE);
        glf.applyTo(compFoot);
        gdf.applyTo(compFoot);

        grpFootButtons = new Group(compFoot, SWT.NONE);
        RowLayoutFactory.fillDefaults().margins(5, 5).pack(false).spacing(10).applyTo(grpFootButtons);
        btnPrev = new Button(grpFootButtons, SWT.NONE);
        btnPrev.setText(Messages.GeneralEccView_btnPrev);
        btnPrev.addListener(SWT.Selection, e -> prevStep());
        btnNextStep = new Button(grpFootButtons, SWT.NONE);
        btnNextStep.setText(Messages.GeneralEccView_btnNextStep);
        btnNextStep.addListener(SWT.Selection, e -> nextStep());
        btnReset = new Button(grpFootButtons, SWT.NONE);
        btnReset.setText(Messages.GeneralEccView_btnReset);
        btnReset.addListener(SWT.Selection, e -> initView());
        
        grpTextInfo = new Group(compFoot, SWT.NONE);
        glf.applyTo(grpTextInfo);
        GridDataFactory.fillDefaults().grab(true,true).applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.GeneralEccView_grpTextInfo);
        textInfo = UIHelper.mutltiLineText(grpTextInfo, SWT.FILL, SWT.FILL, _WHINT, 6, null, true);
        textInfo.setText(Messages.GeneralEccView_textInfo_step1);

        bindValues();
        initView();
    }

    /**
     * Display Next step by iterating the shown view elements.
     */
    private void nextStep() {
        if (!compEncodeStep.isVisible()) {
            ecc.encodeBits();
            btnPrev.setEnabled(true);
            compEncodeStep.setVisible(true);
            compArrowDown.setVisible(true);
            textInfo.setText(Messages.GeneralEccView_textInfo_step2);
        } else if (!compArrowRight1.isVisible()) {
            ecc.flipBits();
            markCode(textError, SWT.COLOR_RED, ecc.getBitErrors());
            compArrowRight1.setVisible(true);
            grpErrorCode.setVisible(true);
            textInfo.setText(Messages.GeneralEccView_textInfo_step3);
        } else if (!grpReceiver.isVisible()) {
            ecc.correctErrors();
            markCode(textCorrected, SWT.COLOR_CYAN, ecc.getBitErrors());
            grpReceiver.setVisible(true);
            compArrowRight2.setVisible(true);
            compDecodeStep.setVisible(true);
            textInfo.setText(Messages.GeneralEccView_textInfo_step4);
        } else if (!compOutputStep.isVisible()) {
            btnNextStep.setEnabled(false);
            compArrowUp.setVisible(true);
            compOutputStep.setVisible(true);
            textInfo.setText(Messages.GeneralEccView_textInfo_step5);

        }
    }

    /**
     * Display previous step by iterating the hidden view elements.
     */
    private void prevStep() {
        if (compOutputStep.isVisible()) {
            btnNextStep.setEnabled(true);
            compArrowUp.setVisible(false);
            compOutputStep.setVisible(false);
            textInfo.setText(Messages.GeneralEccView_textInfo_step4);
        } else if (grpReceiver.isVisible()) {
            grpReceiver.setVisible(false);
            compArrowRight2.setVisible(false);
            compDecodeStep.setVisible(false);
            textInfo.setText(Messages.GeneralEccView_textInfo_step3);
        } else if (compArrowRight1.isVisible()) {
            compArrowRight1.setVisible(false);
            grpErrorCode.setVisible(false);
            textInfo.setText(Messages.GeneralEccView_textInfo_step2);
        } else if (compEncodeStep.isVisible()) {
            btnPrev.setEnabled(false);
            compEncodeStep.setVisible(false);
            compArrowDown.setVisible(false);
            textInfo.setText(Messages.GeneralEccView_textInfo_step1);
        }
    }

    /**
     * Initializes the view by hiding later steps.
     */
    private void initView() {
        textInput.setText("hello"); //$NON-NLS-1$
        ecc.textAsBinary();
        btnPrev.setEnabled(false);
        btnNextStep.setEnabled(true);
        compEncodeStep.setVisible(false);
        grpErrorCode.setVisible(false);
        grpReceiver.setVisible(false);
        compDecodeStep.setVisible(false);
        compOutputStep.setVisible(false);
        compArrowDown.setVisible(false);
        compArrowRight1.setVisible(false);
        compArrowRight2.setVisible(false);
        compArrowUp.setVisible(false);
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
