package org.jcryptool.visual.errorcorrectingcodes.ui;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;

public class UIHelper {


    /**
      * Method to create multiline StyledText widgets.
      *
      * @param p the parent composite
      * @param hAlign SWT horizontal alignment
      * @param vAlign SWT vertical alignment
      * @param width the width of the text field
      * @param lines number of lines displayed without scrollbar
      * @return the styled text widget
      *
      */
     public static StyledText mutltiLineText(Composite p, int hAlign, int vAlign, int width, int lines, Font f) {
         StyledText text = new StyledText(p, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
         text.setBackground(p.getDisplay().getSystemColor(SWT.COLOR_WHITE));
         text.setAlwaysShowScrollBars(false);
         text.setMargins(2, 2, 4, 2);
         text.setFont(f);
         
         int actualWidth = getFontWidth(text);
         
         
         Point hint = new Point(actualWidth, lines * text.getLineHeight());
         GridDataFactory.fillDefaults().align(hAlign, vAlign).grab(true, true).hint(hint).applyTo(text);
         return text;
     }

    public static int getFontWidth(Control text) {
        GC gc = new GC(text);
        FontMetrics fm = gc.getFontMetrics();
        int charWidth = (int) fm.getAverageCharacterWidth();
        int actualWidth = text.computeSize(charWidth * 8, SWT.DEFAULT).x;
        gc.dispose();
        return actualWidth;
    }
     
     public static StyledText codeText(Composite p, int hAlign, int vAlign) {
         StyledText st = mutltiLineText(p, hAlign, vAlign, 220, 4, FontService.getLargeFont());
         return st;
     }
     
     public static StyledText matrixText(Composite p, int hAlign, int vAlign, int cols, int rows) {
         Font f = FontService.getNormalFont();
         StyledText st = mutltiLineText(p, hAlign, vAlign, cols, rows, f);
         return st;
     }
     
     /**
      * Mark a code element according to the produced / corrected errors.
      *
      * @param st a styled text widget
      * @param swtColor a SWT color enum
      */
     public static void markCode(StyledText st, int swtColor, List<BitSet> bitsToMark) {
         ArrayList<StyleRange> ranges = new ArrayList<>();

         Color color = st.getDisplay().getSystemColor(swtColor);

         for (int i = 1; i <= bitsToMark.size(); i++) {
             BitSet b = bitsToMark.get(i-1);
             for (int j = b.nextSetBit(0); j >= 0; j = b.nextSetBit(j + 1)) {
                 int idx = (i * 7)-j-1;
                 ranges.add(new StyleRange(idx, 1, color, null, SWT.ITALIC));
             }
         }
         st.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));
     }
     
     /**
      * Creates an arrow canvas.
      *
      * @param parent the parent composite
      * @param x1 the x of point 1 
      * @param y1 the y of point 1
      * @param x2 the x of point 2
      * @param y2 the y of point 2
      * @param length the length of the arrow line
      * @param arrowSize the arrow head size
      * @return the canvas widget
      */
     public static ArrowCanvas createArrowCanvas(Composite parent, int x1, int y1, int x2, int y2, int length,
             double arrowSize) {
         ArrowCanvas canvas = new ArrowCanvas(parent, x1, y1, x2, y2, length, arrowSize);
         GridLayoutFactory.fillDefaults().applyTo(canvas);
         GridDataFactory.fillDefaults().hint(canvas.getSizeHint()).applyTo(canvas);
         return canvas;
     }

}
