package org.jcryptool.visual.errorcorrectingcodes.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.errorcorrectingcodes.EccController;
import org.jcryptool.visual.errorcorrectingcodes.EccData;

public class EccMainView extends ViewPart {
    private EccController ecc;
    private EccData eccData;
    
    private ScrolledComposite scrolledComposite;
    private Composite parent;
    private Composite mainComposite;
    private Composite compHead;
    private Composite compFoot;
    private Composite compEncode;
    private Composite compDecode;
    private Group grpEncodeStep;
    private Text textOriginal;
    private Text textAsBinary;
    private Text textInput;
    private Text textEncoded;
    private Text textInfo;
    private Group grpInputStep;
    private Group grpFootButtons;
    private Group grpTextInfo;
    private Button btnReset;
    private Button btnNextStep;
    private Button btnPrev;
    private CanvasArrow arrowDown;

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
        
        mainComposite = new Composite(scrolledComposite, SWT.NONE);
        mainComposite.setLayout(new GridLayout(1, true));
        GridData gd_mainComp = new GridData(GridData.FILL_BOTH);
        gd_mainComp.widthHint = 1200;
        mainComposite.setLayoutData(gd_mainComp);

        compHead = new Composite(mainComposite, SWT.NONE);
        compHead.setLayout(new RowLayout());
        Label lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText("Error Correcting Codes");

        compEncode = new Composite(mainComposite, SWT.NONE);
        compEncode.setLayout(new GridLayout(1, false));
        compEncode.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        grpInputStep = new Group(compEncode, SWT.NONE);
        grpInputStep.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        grpInputStep.setLayout(new GridLayout(2, false));
        grpInputStep.setText("Sender");
        Label lblTextOriginal = new Label(grpInputStep, SWT.NONE);
        //lblTextOriginal.setLayoutData(new GridData());
        lblTextOriginal.setText("Original:");
        textInput = new Text(grpInputStep, SWT.BORDER);
        textInput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        textInput.addListener(SWT.Modify, e -> {
            Text source = (Text) e.widget;
            ecc.textAsBinary(source.getText());
        });

        textAsBinary = new Text(grpInputStep, SWT.READ_ONLY);
        textAsBinary.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        textAsBinary.setText("0000");

        arrowDown = new CanvasArrow(compEncode, 30, 10, 30, 60, 3, 12.0);

        grpEncodeStep = new Group(compEncode, SWT.NONE);
        grpEncodeStep.setLayout(new GridLayout(1, false));
        Label lblTextEncoded = new Label(grpEncodeStep, SWT.NONE);
        lblTextEncoded.setLayoutData(gd_textorig);
        lblTextEncoded.setText("Encoded:");
        textEncoded = new Text(grpEncodeStep, SWT.READ_ONLY);
        textEncoded.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        textEncoded.setText("00000000");
        
        compDecode = new Composite(mainComposite, SWT.NONE);
                
        compFoot = new Composite(mainComposite, SWT.NONE);
        compFoot.setLayout(new GridLayout(1, true));
        grpTextInfo = new Group(compFoot, SWT.NONE);
        grpTextInfo.setLayout(new GridLayout(1,true));
        grpTextInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
        grpTextInfo.setText("Information");
        textInfo = new Text(grpTextInfo, SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
       // gdTextInfo.heightHint = 2 * textInput.getLineHeight();
       // gdTextInfo.widthHint = 400;
        textInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
        textInfo.setText("Code-based cryptographic systems rely on error-correcting codes and the hardness of decoding a general linear code. This visual shows the general process of how such codes are used find or correct errors in data transmissions.\n\nClick on the \"next step\" button to continue.");

        grpFootButtons = new Group(compFoot, SWT.NONE);
        grpFootButtons.setLayout(new GridLayout(3,true));

        btnPrev = new Button(grpFootButtons, SWT.NONE);
        btnPrev.setText("Previous Step");
        btnPrev.setEnabled(false);
        btnPrev.addListener(SWT.Selection, e -> { });
        btnNextStep = new Button(grpFootButtons, SWT.NONE);
        btnNextStep.setText("Next Step");
        btnNextStep.addListener(SWT.Selection, e -> nextStep());
        btnReset = new Button(grpFootButtons, SWT.NONE);
        btnReset.setText("Reset");
        btnReset.addListener(SWT.Selection, e -> resetView());


        scrolledComposite.setContent(mainComposite);
        scrolledComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        bindValues();
        initView();

        // composite.pack();
    }

    private void nextStep() {
        if(!grpEncodeStep.getVisible()) {
            ecc.encodeBits();
            grpEncodeStep.setVisible(true);
            arrowDown.setVisible(true);
        }
    }
    

    private void initView() {
       textInput.setText("h");
       grpEncodeStep.setVisible(false);
       arrowDown.setVisible(false);
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

    private void bindValues() {
        // Bind the fields
        DataBindingContext bindingContext = new DataBindingContext();

        IObservableValue<String> inputObserv = WidgetProperties.text(SWT.Modify).observe(textInput);
        bindingContext.bindValue(inputObserv, BeanProperties.value(EccData.class, "originalString").observe(eccData));
        
        IObservableValue<String> binaryObserv = WidgetProperties.text(SWT.Modify).observe(textAsBinary);
        bindingContext.bindValue(binaryObserv, BeanProperties.value(EccData.class, "binaryAsString").observe(eccData));

        IObservableValue<String> encodeObserv = WidgetProperties.text(SWT.Modify).observe(textEncoded);
        bindingContext.bindValue(encodeObserv, BeanProperties.value(EccData.class, "codeAsString").observe(eccData));

    }

}
