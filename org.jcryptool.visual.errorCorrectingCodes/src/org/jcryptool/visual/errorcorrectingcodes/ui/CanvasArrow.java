package org.jcryptool.visual.errorcorrectingcodes.ui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.sun.prism.Graphics;

public class CanvasArrow extends Canvas {

    private GC gc;
    private Device display;
    private Composite parent;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int size;
    private double arrowLength;
    private double arrowAngle;

    public CanvasArrow(Composite parent,  int x1, int y1, int x2, int y2, int size, double arrowLength) {
        super(parent, SWT.NO_REDRAW_RESIZE);
        this.parent = parent;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.size = size;
        this.arrowLength = arrowLength;
        this.arrowAngle = Math.toRadians(40);
        this.display = parent.getDisplay();
        
        GridData gd_canvasArrow = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_canvasArrow.widthHint = x1 + (x2 - x1);
        gd_canvasArrow.heightHint = y1 + (y2 - y1);
        this.setLayoutData(gd_canvasArrow);
        this.setLayout(new GridLayout());
        
        this.addPaintListener(paintEvent -> drawArrow(paintEvent.gc));
    }

    public void drawArrow(GC gc) {
        gc.setLineWidth(size);
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        
        double theta = Math.atan2(y2 - y1, x2 - x1);
        // offset so the line does not go through the arrow
        double offset = (arrowLength - 2) * Math.cos(arrowAngle);

        // draw a line
        gc.drawLine(x1, y1, (int) (x2 - offset * Math.cos(theta)), (int) (y2 - offset * Math.sin(theta)));
        // draw the arrow head
        Path path = new Path(gc.getDevice());
        path.moveTo((float) (x2 - arrowLength * Math.cos(theta - arrowAngle)),
                (float) (y2 - arrowLength * Math.sin(theta - arrowAngle)));
        path.lineTo((float) x2, (float) y2);
        path.lineTo((float) (x2 - arrowLength * Math.cos(theta + arrowAngle)),
                (float) (y2 - arrowLength * Math.sin(theta + arrowAngle)));
        path.close();

        gc.fillPath(path);
        path.dispose();
        
    }
}
