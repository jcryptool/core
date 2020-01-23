package org.jcryptool.visual.rainbow.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.RowDataFactory;
import org.eclipse.jface.layout.RowLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.rainbow.algorithm.RainbowSignature;

public class RainbowSignatureView extends ViewPart {
    private RainbowSignature rainbow;
    private byte[] signature;
    private ViParameterWidget viParams;
    private RainbowLESWidget rainbowLESWidget;

    private ScrolledComposite sc;
    private Composite parent;
    private Composite content;
    private Composite compHead;
    private Composite compInputOutput;
    private Composite compVerification;
    private Composite compAlgorithm;
    private Composite compDetails; 
    private Group grpInput;
    private Group grpOutput;
    private Group grpViParams;
    private Group grpRainbowLES;

    private StyledText textInfoHead;
    private StyledText textMessage;
    private StyledText textSignature;
    private StyledText textInfo;
    private Button btnSign;
    private Button btnVerify;
    private Label lblCheck;
    private Label lblHeader;
    private Label lblXMark;
    private Group grpInfoBox;


    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        rainbow = new RainbowSignature();

        GridLayoutFactory glf = GridLayoutFactory.fillDefaults().margins(new Point(5, 5));
        GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, true);

        sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);

        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        content = new Composite(sc, SWT.NONE);
        glf.applyTo(content);
        gdf.applyTo(content);

        compHead = new Composite(content, SWT.NONE);
        glf.applyTo(compHead);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(compHead);
        lblHeader = new Label(compHead, SWT.NONE);
        lblHeader.setFont(FontService.getHeaderFont());
        lblHeader.setText(Messages.RainbowSignatureView_lblHeader);
        textInfoHead = new StyledText(compHead, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
        textInfoHead.setText(
                Messages.RainbowSignatureView_textInfoHead);
        glf.applyTo(textInfoHead);
        GridDataFactory.fillDefaults().grab(true, false).hint(600, SWT.DEFAULT).applyTo(textInfoHead);

        compAlgorithm = new Composite(content, SWT.NONE); 
        glf.numColumns(2).applyTo(compAlgorithm);
        gdf.applyTo(compAlgorithm);

        compInputOutput = new Composite(compAlgorithm, SWT.NONE);
        glf.numColumns(1).applyTo(compInputOutput);
        gdf.applyTo(compInputOutput);

        grpViParams = new Group(compInputOutput, SWT.NONE);
        grpViParams.setText(Messages.RainbowSignatureView_grpViParams);
        glf.numColumns(1).applyTo(grpViParams);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(grpViParams);

        viParams = new ViParameterWidget(grpViParams, rainbow.getVi());
        viParams.getBtnApply().addListener(SWT.Selection, e -> {
            try {
                rainbow = new RainbowSignature(viParams.getViList().stream().mapToInt(i -> i).toArray());
            } catch (Exception ex) {
                LogUtil.logError(ex);
                MessageBox keyErrorDialog = new MessageBox(parent.getShell(), SWT.ERROR);
                keyErrorDialog.setText(Messages.RainbowSignatureView_keyErrorDialog);
                keyErrorDialog.setMessage(Messages.RainbowSignatureView_keyErrorMessage);
                keyErrorDialog.open();

            }
        });
        
        viParams.getBtnGenerate().addListener(SWT.Selection, e -> {
            viParams.randomVi();
        });

        grpInput = new Group(compInputOutput, SWT.NONE);
        grpInput.setText(Messages.RainbowSignatureView_grpInput);
        glf.numColumns(1).applyTo(grpInput);
        gdf.applyTo(grpInput);
        textMessage = multiLineText(grpInput, SWT.FILL, SWT.FILL, 6, false);
        textMessage.setText(Messages.RainbowSignatureView_textMessage);
        btnSign = new Button(grpInput, SWT.NONE);
        btnSign.setText(Messages.RainbowSignatureView_btnSign);
        btnSign.addListener(SWT.Selection, e -> {
            signature = rainbow.sign(textMessage.getText().getBytes());
            textSignature.setText(javax.xml.bind.DatatypeConverter.printHexBinary(signature));
            textInfo.setText(getVarString());
            if (rainbowLESWidget != null)
                rainbowLESWidget.dispose();
            rainbowLESWidget = new RainbowLESWidget(grpRainbowLES, FontService.getHugeFont(), rainbow.getPrivateKeyParams());
                   
            grpRainbowLES.layout();
        });

        grpOutput = new Group(compInputOutput, SWT.NONE);
        grpOutput.setText(Messages.RainbowSignatureView_grpOutput);
        glf.applyTo(grpOutput);
        gdf.applyTo(grpOutput);
        textSignature = multiLineText(grpOutput, SWT.FILL, SWT.FILL, 2, false);
        compVerification = new Composite(grpOutput, SWT.NONE);
        RowLayoutFactory.fillDefaults().applyTo(compVerification);

        btnVerify = new Button(compVerification, SWT.NONE);
        btnVerify.setText(Messages.RainbowSignatureView_btnVerify);
        RowDataFactory.swtDefaults().applyTo(btnVerify);
        btnVerify.addListener(SWT.Selection, e -> {
            signature = javax.xml.bind.DatatypeConverter.parseHexBinary(textSignature.getText());
            boolean verified = rainbow.verify(textMessage.getText().getBytes(), signature);
            if (verified) {
                lblCheck.setVisible(true);
                lblXMark.setVisible(false);
            } else {
                lblCheck.setVisible(false);
                lblXMark.setVisible(true);
            }
        });
        Point buttonSize = btnVerify.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        lblCheck = iconAsLabel(compVerification, "/../icons/checkmark.png", buttonSize.y); //$NON-NLS-1$
        lblCheck.setVisible(false);

        lblXMark = iconAsLabel(compVerification, "/../icons/x-mark.png", buttonSize.y); //$NON-NLS-1$
        lblXMark.setVisible(false);

        compDetails = new Composite(compAlgorithm, SWT.NONE);
        glf.applyTo(compDetails);
        gdf.applyTo(compDetails);
        
        grpRainbowLES = new Group(compDetails, SWT.NONE);
        grpRainbowLES.setText(Messages.RainbowSignatureView_grpRainbowLES);
        glf.applyTo(grpRainbowLES);
        gdf.hint(500, 250).applyTo(grpRainbowLES);
        
        grpInfoBox = new Group(compDetails, SWT.NONE);
        grpInfoBox.setText(Messages.RainbowSignatureView_grpInfoBox);
        glf.applyTo(grpInfoBox);
        gdf.applyTo(grpInfoBox);
        textInfo = multiLineText(grpInfoBox, SWT.FILL, SWT.FILL, 20, true);
        
        sc.setContent(content);
        sc.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    private StyledText multiLineText(Composite p, int hAlign, int vAlign, int lines, boolean readonly) {
        int read_only = readonly ? SWT.READ_ONLY : 0;
        int border = readonly ? 0 : SWT.BORDER;
        StyledText text = new StyledText(p, read_only | border | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        text.setAlwaysShowScrollBars(false);
        text.setMargins(2, 2, 4, 2);

        if (readonly)
            text.setBackground(p.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

        Point hint = new Point(SWT.DEFAULT, lines * (text.getLineHeight()) + 4);
        GridDataFactory.fillDefaults().align(hAlign, vAlign).grab(true, true).hint(hint).applyTo(text);
        return text;
    }

    private Label iconAsLabel(Composite p, String path, int scale_height) {
        Image image = new Image(parent.getDisplay(), RainbowSignatureView.class.getResourceAsStream(path));

        final double width = image.getBounds().width;
        final double height = image.getBounds().height;

        double scale_width = width / (height / scale_height);
        Image scaled = new Image(parent.getDisplay(),
                image.getImageData().scaledTo((int) scale_width, (int) scale_height));

        Label icon = new Label(p, SWT.NONE);
        icon.setImage(scaled);
        return icon;
    }

    public String getVarString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----" + Messages.RainbowSignatureView_varPrivKey + "----\n") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            .append(Messages.RainbowSignatureView_varDocLength).append(rainbow.getPrivateKeyParams().getDocLength()).append("\n") //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
            .append(Messages.RainbowSignatureView_varLayers).append(rainbow.getPrivateKeyParams().getVi().length).append("\n") //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
            .append(Messages.RainbowSignatureView_varVi).append(arrayToString(rainbow.getPrivateKeyParams().getVi())).append("\n") //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
            .append("B1: ").append(arrayToString(rainbow.getPrivateKeyParams().getB1())).append("\n") //$NON-NLS-1$ //$NON-NLS-2$
            .append("B2: ").append(arrayToString(rainbow.getPrivateKeyParams().getB2())).append("\n") //$NON-NLS-1$ //$NON-NLS-2$
            .append("InvA1: ").append(arrayToString(rainbow.getPrivateKeyParams().getInvA1())).append("\n") //$NON-NLS-1$ //$NON-NLS-2$
            .append("InvA2: ").append(arrayToString(rainbow.getPrivateKeyParams().getInvA2())).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$

        sb.append("\n----" + Messages.RainbowSignatureView_varPubkey + "----\n") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            .append(Messages.RainbowSignatureView_varDocLength).append(rainbow.getPublicKey().getDocLength()).append("\n") //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
            .append(Messages.RainbowSignatureView_varCoeffQ).append(arrayToString(rainbow.getPublicKey().getCoeffQuadratic())).append("\n") //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
            .append(Messages.RainbowSignatureView_varCoeffScalar).append(arrayToString(rainbow.getPublicKey().getCoeffScalar())).append("\n") //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-1$
            .append(Messages.RainbowSignatureView_varCoeffSinglar).append(arrayToString(rainbow.getPublicKey().getCoeffSingular()));

        return sb.toString();
    }
    
    public String arrayToString(short[][] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.RainbowSignatureView_varDim + ": [").append(arr.length).append("][").append(arr[0].length).append("]\n");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        int count = 0;
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[r].length; c++) {
                sb.append(arr[r][c]).append(" "); //$NON-NLS-1$
                count++;
                if (count > 10)
                  break;
            }
            
        }
        sb.append("..."); //$NON-NLS-1$
        return sb.toString();
    }

    public String arrayToString(short[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.RainbowSignatureView_varDim + ": [").append(arr.length).append("]\n");  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
        for (int r = 0; r < arr.length; r++) {
            sb.append(arr[r]).append(" "); //$NON-NLS-1$
        }
        return sb.toString();
    }

    public String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < arr.length; r++) {
            sb.append(arr[r]).append(" "); //$NON-NLS-1$
        }
        return sb.toString();
    }

    @Override
    public void setFocus() {
        sc.setFocus();
    }

    public void reset() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
    }

}
