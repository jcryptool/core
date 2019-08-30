package org.jcryptool.visual.errorcorrectingcodes.ui;

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
import org.jcryptool.visual.errorcorrectingcodes.EccData;

public class EccMainView extends ViewPart {
    private EccController ecc;
    private DataBindingContext dbc;

    private ScrolledComposite scrolledComposite;
    private Composite parent;
    private Composite composite;
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
    private Text textInfo;
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

    public EccMainView() {
        ecc = new EccController(new EccData());
    }

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        Point widthhint = new Point(800, SWT.DEFAULT);
        Point groupWidthHint = new Point(150, SWT.DEFAULT);

        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        composite = new Composite(scrolledComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(composite);
        GridDataFactory.fillDefaults().grab(true, true).hint(widthhint).applyTo(composite);

        compHead = new Composite(composite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compHead);
        GridDataFactory.fillDefaults().hint(widthhint).applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.EccMainView_lblHeader);

        mainComposite = new Composite(composite, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(5).applyTo(mainComposite);
        GridDataFactory.fillDefaults().applyTo(mainComposite);

        grpSender = new Group(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpSender);
        GridDataFactory.fillDefaults().hint(groupWidthHint).grab(true, true).applyTo(grpSender);
        grpSender.setText(Messages.EccMainView_grpSenderStep);
        compInputStep = new Composite(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compInputStep);
        GridDataFactory.fillDefaults().applyTo(compInputStep);
        lblTextOriginal = new Label(compInputStep, SWT.NONE);
        lblTextOriginal.setText(Messages.EccMainView_lblTextOriginal);
        textInput = new StyledText(compInputStep, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(textInput);
        textInput.addListener(SWT.FocusOut, e -> ecc.textAsBinary());
        textAsBinary = multiLineStyledText(grpSender, SWT.FILL, SWT.TOP);
        compArrowDown = new Composite(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(compArrowDown);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.TOP).applyTo(compArrowDown);
        arrowDown = createArrowCanvas(compArrowDown, 10, 10, 10, 130, 3, 13.0);
        lblTextEncoded = new Label(compArrowDown, SWT.NONE);
        lblTextEncoded.setText(Messages.EccMainView_lblTextEncode);
        compEncodeStep = new Composite(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compEncodeStep);
        GridDataFactory.fillDefaults().applyTo(compEncodeStep);
        textEncoded = multiLineStyledText(compEncodeStep, SWT.FILL, SWT.BOTTOM);

        compArrowRight1 = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight1);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowRight1);
        arrowRight1 = createArrowCanvas(compArrowRight1, 10, 10, 150, 10, 2, 10.0);
        arrowRight1.setLineStyle(SWT.LINE_DASH);

        grpErrorCode = new Group(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpErrorCode);
        GridDataFactory.fillDefaults().hint(groupWidthHint).grab(true, true).applyTo(grpErrorCode);
        grpErrorCode.setText(Messages.EccMainView_grpErrorCode);
        textError = multiLineStyledText(grpErrorCode, SWT.FILL, SWT.CENTER);
        // GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.FILL).applyTo(textError);

        compArrowRight2 = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight2);
        arrowRight2 = createArrowCanvas(compArrowRight2, 10, 10, 150, 10, 2, 10.0);
        arrowRight2.setLineStyle(SWT.LINE_DASH);

        grpReceiver = new Group(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpReceiver);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpReceiver);
        grpReceiver.setText(Messages.EccMainView_grpReceiver);
        compOutputStep = new Composite(grpReceiver, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compOutputStep);
        GridDataFactory.fillDefaults().applyTo(compOutputStep);
        textOutput = multiLineStyledText(compOutputStep, SWT.FILL, SWT.TOP);
        compArrowUp = new Composite(grpReceiver, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(compArrowUp);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowUp);
        arrowUp = createArrowCanvas(compArrowUp, 10, 180, 10, 10, 3, 13.0);
        lblTextDecoded = new Label(compArrowUp, SWT.NONE);
        lblTextDecoded.setText(Messages.EccMainView_lblTextDecoded);
        compDecodeStep = new Composite(grpReceiver, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compDecodeStep);
        GridDataFactory.fillDefaults().applyTo(compDecodeStep);
        textCorrected = multiLineStyledText(compDecodeStep, SWT.FILL, SWT.BOTTOM);

        compFoot = new Composite(composite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compFoot);
        grpTextInfo = new Group(compFoot, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpTextInfo);
        GridDataFactory.fillDefaults().applyTo(grpTextInfo);
        grpTextInfo.setText(Messages.EccMainView_grpTextInfo);
        textInfo = new Text(grpTextInfo, SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
        GridDataFactory.fillDefaults().hint(widthhint.x, 8 * textInfo.getLineHeight()).applyTo(textInfo);
        textInfo.setText(Messages.EccMainView_textInfo_step1);

        grpFootButtons = new Group(compFoot, SWT.NONE);
        RowLayoutFactory.fillDefaults().pack(false).spacing(10).applyTo(grpFootButtons);
        btnPrev = new Button(grpFootButtons, SWT.NONE);
        btnPrev.setText(Messages.EccMainView_btnPrev);
        btnPrev.addListener(SWT.Selection, e -> prevStep());
        btnNextStep = new Button(grpFootButtons, SWT.NONE);
        btnNextStep.setText(Messages.EccMainView_btnNextStep);
        btnNextStep.addListener(SWT.Selection, e -> nextStep());
        btnReset = new Button(grpFootButtons, SWT.NONE);
        btnReset.setText(Messages.EccMainView_btnReset);
        btnReset.addListener(SWT.Selection, e -> resetView());

        scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        initView();

        // composite.pack();
    }

    private StyledText multiLineStyledText(Composite p, int hAlign, int vAlign) {
        StyledText text = new StyledText(p, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        text.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        text.setFont(FontService.getLargeFont());
        Point hint = new Point(150, 3 * text.getLineHeight());
        GridDataFactory.fillDefaults().align(hAlign, vAlign).grab(true, true).hint(hint).applyTo(text);
        return text;
    }

    private void nextStep() {
        if (!compEncodeStep.isVisible()) {
            ecc.encodeBits();
            btnPrev.setEnabled(true);
            compEncodeStep.setVisible(true);
            compArrowDown.setVisible(true);
            textInfo.setText(Messages.EccMainView_textInfo_step2);
        } else if (!compArrowRight1.isVisible()) {
            ecc.flipBits();
            markCode(textError, SWT.COLOR_RED);
            compArrowRight1.setVisible(true);
            grpErrorCode.setVisible(true);
            textInfo.setText(Messages.EccMainView_textInfo_step3);
        } else if (!grpReceiver.isVisible()) {
            ecc.correctErrors();
            markCode(textCorrected, SWT.COLOR_CYAN);
            grpReceiver.setVisible(true);
            compArrowRight2.setVisible(true);
            compDecodeStep.setVisible(true);
            textInfo.setText(Messages.EccMainView_textInfo_step4);
        } else if (!compOutputStep.isVisible()) {
            btnNextStep.setEnabled(false);
            compArrowUp.setVisible(true);
            compOutputStep.setVisible(true);
        }
    }
    private void prevStep() {
        if (compOutputStep.isVisible()) {
            btnNextStep.setEnabled(true);
            compArrowUp.setVisible(false);
            compOutputStep.setVisible(false);
        } else if (grpReceiver.isVisible()) {
            grpReceiver.setVisible(false);
            compArrowRight2.setVisible(false);
            compDecodeStep.setVisible(false);
            textInfo.setText(Messages.EccMainView_textInfo_step3);
        } else if (compArrowRight1.isVisible()) {
            compArrowRight1.setVisible(false);
            grpErrorCode.setVisible(false);
            textInfo.setText(Messages.EccMainView_textInfo_step2);
        } else if (compEncodeStep.isVisible()) {
            btnPrev.setEnabled(false);
            compEncodeStep.setVisible(false);
            compArrowDown.setVisible(false);
            textInfo.setText(Messages.EccMainView_textInfo_step1);
        } 
    }

    private void initView() {
        bindValues();
        textInput.setText("h"); //$NON-NLS-1$
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

    private void markCode(StyledText st, int swtColor) {
        List<BitSet> bitsToMark = ecc.getBitErrors();

        ArrayList<StyleRange> ranges = new ArrayList<>();

        Color color = parent.getDisplay().getSystemColor(swtColor);

        for (int i = 1; i <= bitsToMark.size(); i++) {
            BitSet b = bitsToMark.get(i-1);
            for (int j = b.nextSetBit(0); j >= 0; j = b.nextSetBit(j + 1)) {
                int idx = (i * 7)-j-1;
                ranges.add(new StyleRange(idx, 1, color, null, Font.ITALIC));
            }
        }
        st.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));
    }

    private ArrowCanvas createArrowCanvas(Composite parent, int x1, int y1, int x2, int y2, int length,
            double arrowSize) {
        ArrowCanvas canvas = new ArrowCanvas(parent, x1, y1, x2, y2, length, arrowSize);
        GridLayoutFactory.fillDefaults().applyTo(canvas);
        GridDataFactory.fillDefaults().hint(canvas.getSizeHint()).applyTo(canvas);
        return canvas;
    }

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

    @Override
    public void setFocus() {
        mainComposite.setFocus();
    }

    public void resetView() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
    }

}
