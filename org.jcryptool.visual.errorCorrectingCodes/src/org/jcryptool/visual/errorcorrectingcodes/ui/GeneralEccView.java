/*
 * @author Daniel Hofmann
 */
package org.jcryptool.visual.errorcorrectingcodes.ui;

import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.createArrowCanvas;
import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.markCode;
import static org.jcryptool.visual.errorcorrectingcodes.ui.UIHelper.codeText;

import java.awt.Font;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.GroupLayout;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowDataFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.jface.widgets.LabelFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.EccController;
import org.jcryptool.visual.errorcorrectingcodes.data.EccData;

/**
 * The Class GeneralEccView represents the common process of detecting and correcting errors when
 * transmitting data over a noisy channel.
 */
public class GeneralEccView extends Composite {

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
    private Composite compOutputStep;

    private Group grpSender;
    private Group grpErrorCode;
    private Group grpFootButtons;
    private Group grpTextInfo;
    private StyledText textAsBinary;
    private StyledText textInput;
    private StyledText textEncoded;
    private StyledText textInfo;
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

    private Group grpReceiver;
    private StyledText textOutput;
    private Composite compArrowUp;
    private Label lblTextDecoded;
    private Composite compDecodeStep;
    private StyledText textCorrected;
    private StyledText textError;
    private Composite compHeadLabels;
    private Label lblEncrypt;
    private Label lblDecrypt;

    /**
     * Instantiates a new general ecc view.
     */

    public GeneralEccView(Composite parent, int style) {
        super(parent, style);
        ecc = new EccController(new EccData());
        this.parent = parent;

        // common grid layout for all elements
        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(5, 5).equalWidth(true);

        glf.applyTo(this);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(this);

        compHead = new Composite(this, SWT.NONE);
        glf.applyTo(compHead);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.GeneralEccView_lblHeader);

        mainComposite = new Composite(this, SWT.NONE);
        glf.equalWidth(false).numColumns(5).applyTo(mainComposite);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(mainComposite);

        grpSender = new Group(mainComposite, SWT.NONE);
        glf.numColumns(1).applyTo(grpSender);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpSender);
        grpSender.setText(Messages.GeneralEccView_grpSenderStep);
        compInputStep = new Composite(grpSender, SWT.NONE);
        glf.applyTo(compInputStep);
        GridDataFactory.fillDefaults().applyTo(compInputStep);
        lblTextOriginal = new Label(compInputStep, SWT.NONE);
        lblTextOriginal.setText(Messages.GeneralEccView_lblTextOriginal);
        textInput = new StyledText(compInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> ecc.textAsBinary());
        textAsBinary = codeText(grpSender, SWT.FILL, SWT.TOP);
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
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight1);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowRight1);
        arrowRight1 = createArrowCanvas(compArrowRight1, 10, 10, 130, 10, 2, 10.0);
        arrowRight1.setLineStyle(SWT.LINE_DASH);

        grpErrorCode = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpErrorCode);
        GridDataFactory.fillDefaults().minSize(SWT.DEFAULT, 600).grab(true, true).applyTo(grpErrorCode);
        grpErrorCode.setText(Messages.GeneralEccView_grpErrorCode);
        textError = codeText(grpErrorCode, SWT.FILL, SWT.CENTER);
        // GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.FILL).applyTo(textError);

        compArrowRight2 = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight2);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowRight2);
        arrowRight2 = createArrowCanvas(compArrowRight2, 10, 10, 130, 10, 2, 10.0);
        arrowRight2.setLineStyle(SWT.LINE_DASH);

        grpReceiver = new Group(mainComposite, SWT.NONE);
        glf.applyTo(grpReceiver);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpReceiver);
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
        GridDataFactory.fillDefaults().grab(true, true).applyTo(compFoot);
        grpTextInfo = new Group(compFoot, SWT.NONE);
        glf.applyTo(grpTextInfo);
        GridDataFactory.fillDefaults().grab(true,true).applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.GeneralEccView_grpTextInfo);
        textInfo = UIHelper.mutltiLineText(grpTextInfo, SWT.FILL, SWT.FILL, parent.getBounds().width-100, 8, null);
        textInfo.setText(Messages.GeneralEccView_textInfo_step1);

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
