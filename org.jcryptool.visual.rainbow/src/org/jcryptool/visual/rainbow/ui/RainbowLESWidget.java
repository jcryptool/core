package org.jcryptool.visual.rainbow.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.colors.*;

public class RainbowLESWidget extends Composite {

    private StyledText rainbowLesText;
    private List<StyleRange> styleRangeList;
    private RainbowPrivateKeyParameters params;
    private StringBuilder sb;

    public RainbowLESWidget(Composite parent, Font font, RainbowPrivateKeyParameters params) {
        super(parent, SWT.NONE);
        this.params = params;
        styleRangeList = new ArrayList<StyleRange>();
        rainbowLesText = new StyledText(this, SWT.READ_ONLY | SWT.MULTI);
        rainbowLesText.setFont(font);
        rainbowLesText.setBackground(ColorService.LIGHTGRAY);
        
        GridLayoutFactory.fillDefaults().applyTo(this);
        GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 9 * rainbowLesText.getLineHeight()).applyTo(this);

        sb = new StringBuilder();
        int start = 0;
        Layer[] layers = params.getLayers();
        for (int i = 0; i < layers.length; i++) {
            start = addLayerText(start, i, false);
        }

        // Last layer represented slightly different
        addLayerText(start, layers.length-1, true);
        
        rainbowLesText.setText(sb.toString());
        rainbowLesText.setStyleRanges(styleRangeList.toArray(new StyleRange[styleRangeList.size()]));

        FontData data = rainbowLesText.getFont().getFontData()[0];
        Font smallFont = new Font(getDisplay(), data.getName(), (int) (data.height / 2.5f), SWT.NORMAL);

        String patternStr = "[0-9]";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(rainbowLesText.getText());

        GC gc = new GC(getShell());
        gc.setFont(smallFont);
        FontMetrics smallMetrics = gc.getFontMetrics();
        final int smallBaseline = smallMetrics.getAscent() + smallMetrics.getLeading();
        gc.dispose();
        TextStyle subscript = new TextStyle(smallFont, null, null);
        subscript.rise = -(smallBaseline / 2);

        int i = 0;
        while (matcher.find(i + 1)) {
            i = matcher.start();
            StyleRange old = rainbowLesText.getStyleRangeAtOffset(i);
            subscript.foreground = old.foreground;
            StyleRange style = new StyleRange(subscript);
            style.start = i;
            style.length = 1;
            rainbowLesText.setStyleRange(style);
        }
    }

    private int addLayerText(int start, int numVi, boolean lastLayer) {
        int vi = 0, length;
        int color = SWT.COLOR_DARK_RED;
        sb.append("[ ");
        sb.append("v").append(vi + 1).append(", . . . , ").append("v").append(vi += params.getLayers()[0].getVi());
        styleRangeList.add(new StyleRange(start, length = sb.length() - start + 2, getDisplay().getSystemColor(color), null));
        start += length;
        color += 2;

        for (int j = 0; j < numVi; j++) {
            sb.append(", v").append(vi + 1).append(", . . . , ").append("v").append(vi += params.getLayers()[j].getOi());
            styleRangeList.add(new StyleRange(start, length = sb.length() - start + 2, getDisplay().getSystemColor(color), null));
            start += length;
            color += 2;
        }
        
        if (!lastLayer) {
            sb.append(" ]; { o").append(vi + 1).append(", . . . , ").append("o").append(vi += params.getLayers()[numVi].getOi());
            sb.append(" }\n");
            start +=2;

        } else {
            sb.append(", v").append(vi + 1).append(", . . . , ").append("v").append(vi += params.getLayers()[numVi].getOi());
            sb.append("]");
        }

        styleRangeList.add(new StyleRange(start, length = sb.length() - start, getDisplay().getSystemColor(color), null));
        return start += length;
    }
}
