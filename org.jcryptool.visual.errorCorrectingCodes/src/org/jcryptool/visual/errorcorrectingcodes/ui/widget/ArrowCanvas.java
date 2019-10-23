package org.jcryptool.visual.errorcorrectingcodes.ui.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * ArrowCanvas is used to draw a single arrow from point (x1,y1) to point (x2,y2).
 * 
 * @author Daniel Hofmann
 **/
public class ArrowCanvas extends Canvas {

    private static final int ARROW_ANGLE = 40;

    private Display display;

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int arrowSize;
    private double arrowLength;
    private double arrowAngle;
    private int lineStyle;

    /**
     * Instantiates a new arrow canvas.
     *
     * @param parent the parent
     * @param x1 the x1
     * @param y1 the y1
     * @param x2 the x2
     * @param y2 the y2
     * @param size the size of the arrow head
     * @param arrowLength the arrow length
     */
    public ArrowCanvas(Composite parent, int x1, int y1, int x2, int y2, int size, double arrowLength) {
        super(parent, SWT.NO_REDRAW_RESIZE);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.arrowSize = size;
        this.arrowLength = arrowLength;
        this.arrowAngle = Math.toRadians(ARROW_ANGLE);
        this.display = parent.getDisplay();
        this.lineStyle = SWT.LINE_SOLID;

        this.addPaintListener(paintEvent -> drawArrow(paintEvent.gc));

    }

    /**
     * Draw method.
     *
     * @param gc the GC object of the caller, e.g. a paint event
     **/
    public void drawArrow(GC gc) {
        gc.setLineStyle(lineStyle);
        gc.setLineWidth(arrowSize);
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

    /**
     * Gets the approximate Size of the canvas.
     *
     * @return size as a Point
     */
    public Point getSizeHint() {
        int offset = 10;
        int x = (x1 > x2 ? x1 : x2) + offset;
        int y = (y1 > y2 ? y1 : y2) + offset;
        
        return new Point(x, y);
    }

    /**
     * Gets the display.
     *
     * @return the display
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Sets the display.
     *
     * @param display the new display
     */
    public void setDisplay(Display display) {
        this.display = display;
    }

    /**
     * Gets the arrow size.
     *
     * @return the arrow size
     */
    public int getArrowSize() {
        return arrowSize;
    }

    /**
     * Sets the arrow size.
     *
     * @param size the new arrow size
     */
    public void setArrowSize(int size) {
        this.arrowSize = size;
    }

    /**
     * Gets the arrow length.
     *
     * @return the arrow length
     */
    public double getArrowLength() {
        return arrowLength;
    }

    /**
     * Sets the arrow length.
     *
     * @param arrowLength the new arrow length
     */
    public void setArrowLength(double arrowLength) {
        this.arrowLength = arrowLength;
    }

    /**
     * Gets the arrow angle.
     *
     * @return the arrow angle
     */
    public double getArrowAngle() {
        return arrowAngle;
    }

    /**
     * Sets the arrow angle.
     *
     * @param arrowAngle the new arrow angle
     */
    public void setArrowAngle(double arrowAngle) {
        this.arrowAngle = arrowAngle;
    }

    /**
     * Gets the line style.
     *
     * @return the line style
     */
    public int getLineStyle() {
        return lineStyle;
    }

    /**
     * Sets the line style.
     *
     * @param lineStyle the new line style
     */
    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

}
