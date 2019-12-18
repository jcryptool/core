package org.jcryptool.visual.rainbow.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
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

    public RainbowLESWidget(Composite parent, RainbowPrivateKeyParameters params) {
        super(parent, SWT.NONE);
        this.params = params;
        styleRangeList = new ArrayList<StyleRange>();
        rainbowLesText = new StyledText(this, SWT.READ_ONLY | SWT.MULTI);
        rainbowLesText.setFont(FontService.getHugeFont());
        rainbowLesText.setBackground(ColorService.LIGHTGRAY);
        StringBuilder sb = new StringBuilder();
        int color, start = 0, length = 0, vi;
        Layer[] layers = params.getLayers();
        for (int i = 0; i < layers.length; i++) {
            vi = 0;
            color = SWT.COLOR_DARK_RED;
            sb.append("[ ");
            sb.append("x").append(vi + 1).append(", . . . , ").append("x").append(vi += layers[0].getVi());
            styleRangeList.add(new StyleRange(start, length = sb.length() - start + 2, getDisplay().getSystemColor(color), null));
            start += length;
            color += 2;

            for (int j = 0; j < i; j++) {
                sb.append(", x").append(vi + 1).append(", . . . , ").append("x").append(vi += layers[j].getOi());
                styleRangeList.add(new StyleRange(start, length = sb.length() - start + 2, getDisplay().getSystemColor(color), null));
                start += length;
                color += 2;
            }
            
            sb.append(" ]; { ");
            sb.append("x").append(vi + 1).append(", . . . , ").append("x").append(vi += layers[i].getOi());
            sb.append(" }\n");
            start++;
            styleRangeList.add(new StyleRange(++start, length = sb.length() - start, getDisplay().getSystemColor(color), null));
            start += length;
        }

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
        subscript.rise = - (smallBaseline/2);

        int i = 0;
        while (matcher.find(i+1)) {
            i= matcher.start();
            StyleRange old = rainbowLesText.getStyleRangeAtOffset(i);
            subscript.foreground =  old.foreground;
            StyleRange style = new StyleRange(subscript);
            style.start = i;
            style.length = 1;
            rainbowLesText.setStyleRange(style);
        }
    }
}
