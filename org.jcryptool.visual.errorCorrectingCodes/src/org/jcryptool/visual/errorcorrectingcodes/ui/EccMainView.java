package org.jcryptool.visual.errorcorrectingcodes.ui;

import java.awt.Font;
import java.util.ArrayList;

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
    private Composite compDecode;
    private Composite compTransmit;

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

    private DataBindingContext dbc;

    private Composite compArrowDown;

    private Label lblHeader;

    private Label lblTextOriginal;

    private Label lblTextEncoded;

    private Label lblArrowRight1;
    private Group grpReceiver;
    private Composite compOutputStep;
    private StyledText textOutput;
    private Composite compArrowUp;
    private ArrowCanvas arrowUp;
    private Label lblTextDecoded;
    private Composite compDecodeStep;
    private StyledText textCorrected;
    private StyledText textError;

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
        compHead.setLayout(new RowLayout());
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
        textAsBinary = multiLineStyledText(grpSender);
        compArrowDown = new Composite(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(compArrowDown);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.TOP).applyTo(compArrowDown);
        arrowDown = createArrowCanvas(compArrowDown, 10, 10, 10, 130, 3, 13.0);
        lblTextEncoded = new Label(compArrowDown, SWT.NONE);
        lblTextEncoded.setText(Messages.EccMainView_lblTextEncode);
        compEncodeStep = new Composite(grpSender, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compEncodeStep);
        GridDataFactory.fillDefaults().applyTo(compEncodeStep);
        textEncoded = multiLineStyledText(compEncodeStep);

        compArrowRight1 = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight1);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowRight1);
        arrowRight1 = createArrowCanvas(compArrowRight1, 10, 10, 150, 10, 2, 10.0);
        arrowRight1.setLineStyle(SWT.LINE_DASH);

      
        grpErrorCode = new Group(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(grpErrorCode);
        GridDataFactory.fillDefaults().hint(groupWidthHint).grab(true, true).applyTo(grpErrorCode);
        grpErrorCode.setText(Messages.EccMainView_grpErrorCode);
        textError =  multiLineStyledText(grpErrorCode);
        //GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.FILL).applyTo(textError);

        compArrowRight2 = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compArrowRight2);
        arrowRight2 = createArrowCanvas(compArrowRight2, 10, 10, 150, 10, 2, 10.0);
        arrowRight2.setLineStyle(SWT.LINE_DASH);

        compDecode = new Composite(mainComposite, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(compDecode);
        GridDataFactory.fillDefaults().applyTo(compDecode);
        grpReceiver = new Group(compDecode, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(grpReceiver);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(grpReceiver);
        grpReceiver.setText("Receiver");
        compOutputStep = new Composite(grpReceiver, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compOutputStep);
        GridDataFactory.fillDefaults().applyTo(compOutputStep);
        textOutput = multiLineStyledText(compOutputStep);
        compArrowUp = new Composite(grpReceiver, SWT.NONE);
        GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(compArrowUp);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(compArrowUp);
        arrowUp = createArrowCanvas(compArrowUp, 10, 120, 10, 10, 3, 13.0);
        lblTextDecoded = new Label(compArrowUp, SWT.NONE);
        lblTextDecoded.setText("decode");
        compDecodeStep = new Composite(grpReceiver, SWT.NONE);
        GridLayoutFactory.fillDefaults().applyTo(compDecodeStep);
        GridDataFactory.fillDefaults().applyTo(compDecodeStep);
        textCorrected = multiLineStyledText(compDecodeStep);
        textCorrected.setText("0000000 0000000");

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
        btnPrev.setEnabled(false);
        btnPrev.addListener(SWT.Selection, e -> {
        });
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

    private void markCodeElements(Event e, int swtColor) {
        StyledText st = (StyledText) e.widget;
        ArrayList<StyleRange> ranges = new ArrayList<>();
        Color color = parent.getDisplay().getSystemColor(swtColor);

        for (int i = 0; i < st.getText().length() / 7; i++) {
            int j = i * 7;
            ranges.add(new StyleRange(j + 4, 3, color, null, Font.ITALIC));
        }

        st.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));
    }

    private StyledText multiLineStyledText(Composite p) {
        StyledText text = new StyledText(p, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        text.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        text.setFont(FontService.getLargeFont());
        Point hint = new Point(150, 3 * text.getLineHeight());
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, true).hint(hint).applyTo(text);
        return text;
    }

    private void nextStep() {
        if (!compEncodeStep.isVisible()) {
            ecc.encodeBits();
            compEncodeStep.setVisible(true);
            compArrowDown.setVisible(true);
            textInfo.setText(Messages.EccMainView_textInfo_step2);
        } else if (!compArrowRight1.isVisible()) {
            ecc.flipBits();
            compArrowRight1.setVisible(true);
            compTransmit.setVisible(true);
        }
    }

    private void initView() {
        bindValues();
        textInput.setText("h"); //$NON-NLS-1$
        ecc.textAsBinary();
        compEncodeStep.setVisible(false);
        //compTransmit.setVisible(false);
        compArrowDown.setVisible(false);
        compArrowRight1.setVisible(false);
        
        // compArrowRight2.setVisible(false);

    }

    private ArrowCanvas createArrowCanvas(Composite parent, int x1, int y1, int x2, int y2, int length,
            double arrowSize) {
        ArrowCanvas canvas = new ArrowCanvas(parent, x1, y1, x2, y2, length, arrowSize);
        canvas.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
        GridLayoutFactory.fillDefaults().applyTo(canvas);
        GridDataFactory.fillDefaults().hint(canvas.getSizeHint()).applyTo(canvas);
        return canvas;
    }

    private void bindValues() {
        dbc = new DataBindingContext();

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textInput),
                BeanProperties.value(EccData.class, "originalString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textAsBinary),
                BeanProperties.value(EccData.class, "binaryAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$

        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textEncoded),
                BeanProperties.value(EccData.class, "codeAsString", String.class).observe(ecc.getData())); //$NON-NLS-1$
        
        dbc.bindValue(WidgetProperties.text(SWT.Modify).observe(textError),
                BeanProperties.value(EccData.class, "codeStringWithErrors", String.class).observe(ecc.getData())); //$NON-NLS-1$

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
