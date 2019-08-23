package org.jcryptool.visual.errorcorrectingcodes.ui;

import java.awt.Font;
import java.util.ArrayList;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
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
    private Text textInput;
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

    public EccMainView() {
        ecc = new EccController(new EccData());
    }

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        int widthhint = 1000;

        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        composite = new Composite(scrolledComposite, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        GridData gd_composite = new GridData(GridData.FILL_BOTH);
        gd_composite.widthHint = widthhint;
        composite.setLayoutData(gd_composite);

        compHead = new Composite(composite, SWT.NONE);
        compHead.setLayout(new RowLayout());
        Label lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.EccMainView_lblHeader);

        mainComposite = new Composite(composite, SWT.NONE);
        mainComposite.setLayout(new GridLayout(5, true));
        mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        grpSender = new Group(mainComposite, SWT.NONE);
        grpSender.setLayout(new GridLayout(1, false));
        grpSender.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        grpSender.setText(Messages.EccMainView_grpSenderStep);

        compInputStep = new Composite(grpSender, SWT.NONE);
        compInputStep.setLayout(new GridLayout(2, false));
        compInputStep.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        Label lblTextOriginal = new Label(compInputStep, SWT.NONE);
        lblTextOriginal.setText(Messages.EccMainView_lblTextOriginal);
        textInput = new Text(compInputStep, SWT.BORDER);
        GridData gd_textInput = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_textInput.widthHint = 150;
        textInput.setLayoutData(gd_textInput);
        textInput.addListener(SWT.FocusOut, e -> ecc.textAsBinary());
        textAsBinary = multiLineStyledText(compInputStep);
        compArrowDown = new Composite(grpSender, SWT.NONE);
        compArrowDown.setLayout(new GridLayout(2, true));
        compArrowDown.setLayoutData(new GridData(GridData.FILL_BOTH));
        arrowDown = createArrowCanvas(compArrowDown, 30, 10, 30, 70, 3, 13.0);
        Label lblTextEncoded = new Label(compArrowDown, SWT.NONE);
        lblTextEncoded.setText(Messages.EccMainView_lblTextEncode);

        compEncodeStep = new Composite(grpSender, SWT.NONE);
        compEncodeStep.setLayout(new GridLayout(1, false));

        textEncoded = multiLineStyledText(compEncodeStep);
        textEncoded.addListener(SWT.Modify, e -> markCodeElements(e, SWT.COLOR_BLUE));
        
        compArrowRight1 = new Composite(mainComposite, SWT.NONE);
        compArrowRight1.setLayout(new GridLayout(1, false));
        compArrowRight1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        arrowRight1 = createArrowCanvas(compArrowRight1, 10, 10, 60, 10, 3, 12.0);

        compTransmit = new Composite(mainComposite, SWT.NONE);
        compTransmit.setLayout(new GridLayout(1, false));
        compTransmit.setLayoutData(new GridData(GridData.FILL_BOTH));
        grpErrorCode = new Group(compTransmit, SWT.NONE);
        grpErrorCode.setLayout(new GridLayout(2, false));
        grpErrorCode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        grpErrorCode.setText(Messages.EccMainView_grpErrorCode);

        compArrowRight2 = new Composite(mainComposite, SWT.NONE);
        compArrowRight2.setLayout(new GridLayout(1, false));
        compArrowRight2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
        arrowRight2 = createArrowCanvas(compArrowRight2, 10, 10, 60, 10, 3, 12.0);

        compDecode = new Composite(mainComposite, SWT.NONE);

        compFoot = new Composite(composite, SWT.NONE);
        compFoot.setLayout(new GridLayout(1, true));
        grpTextInfo = new Group(compFoot, SWT.NONE);
        grpTextInfo.setLayout(new GridLayout(1, true));
        grpTextInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
        grpTextInfo.setText(Messages.EccMainView_grpTextInfo);
        textInfo = new Text(grpTextInfo, SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
        GridData gd_textInfo = new GridData(GridData.FILL_BOTH);
        gd_textInfo.heightHint = 8 * textInfo.getLineHeight();
        // gdTextInfo.widthHint = 400;
        textInfo.setLayoutData(gd_textInfo);
        textInfo.setText(Messages.EccMainView_textInfo_step1);

        grpFootButtons = new Group(compFoot, SWT.NONE);
        grpFootButtons.setLayout(new GridLayout(3, true));

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
        
        for (int i = 0; i < st.getText().length()/7; i++) {
            int j = i * 7;
            ranges.add(new StyleRange(j+4,3,color,null,Font.ITALIC));
        }
        

        st.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));
    }

    private StyledText multiLineStyledText(Composite p) {
        StyledText text = new StyledText(p, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, true);
        gd_text.widthHint = 150;
        gd_text.heightHint = 3 * text.getLineHeight();
        text.setLayoutData(gd_text);
        text.setFont(FontService.getLargeFont());
        return text;
    }

    private void nextStep() {
        if (!compEncodeStep.getVisible()) {
            ecc.encodeBits();
            compEncodeStep.setVisible(true);
            compArrowDown.setVisible(true);
            textInfo.setText(
                    Messages.EccMainView_textInfoStep2);
        }
    }

    private void initView() {
        bindValues();
        textInput.setText("h"); //$NON-NLS-1$
        ecc.textAsBinary();
        compEncodeStep.setVisible(false);
        compArrowDown.setVisible(false);
    }

    private ArrowCanvas createArrowCanvas(Composite parent, int x1, int y1, int x2, int y2, int length,
            double arrowSize) {
        ArrowCanvas canvas = new ArrowCanvas(parent, x1, y1, x2, y2, length, arrowSize);
        canvas.setLayout(new GridLayout());
        GridData gd_arrow = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_arrow.widthHint = x1 + (x2 - x2);
        gd_arrow.heightHint = y1 + (y2 - y1);
        canvas.setLayoutData(gd_arrow);
        canvas.addPaintListener(paintEvent -> canvas.drawArrow(paintEvent.gc));
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
