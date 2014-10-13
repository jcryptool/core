package org.jcryptool.visual.euclid;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageBuffer {
    // Felder für Buffer-Technik; Am Ende vielleicht an günstigere Position verschieben
    private Image bufferImage;
    private GC bufferGC;

    private static final Color BLACK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
    private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
    private static final Color RED = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
    private static final Color BLUE = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);

    // - Wenn eine neue Berechnung gestartet wird: erst maximale Höhe/Breite
    // des Buffers berechnen.
    // - Dann mit dieser methode den Buffer initialisieren
    // - Dann damit arbeiten und bei Bedarf Teie des gepufferten Bildes aufs Canvas zeichnen
    // - (welchen Teil (x, y, with, height) steht im PaintEvent)
    public ImageBuffer(Device device, int width, int height) {
        // Image darauf erstellen. Das ist eigentlich euer Zeichenbuffer.
        bufferImage = new Image(device, width, height);

        // Für den Zeichenbuffer machen wir noch einen Graphical Context
        // für die nützlichen Zeichenoperationen
        bufferGC = new GC(bufferImage);
    }
    
    public void paintImage(ArrayList<int[]> values) {
        bufferGC.setForeground(BLACK);
        bufferGC.drawText(Messages.Euclid_Long_Line+values.get(0)[0], 10, 10, true);
        bufferGC.drawText(Messages.Euclid_Short_Line+values.get(0)[1], 10, 40, true);
        drawLine(10, 30, values.get(0)[0], GREEN, bufferGC);
        drawLine(10, 60, values.get(0)[1], RED, bufferGC);
        
        for(int i=1; i<values.size(); i++) {
            bufferGC.setForeground(BLACK);
            bufferGC.drawText(values.get(i)[0]+" - "+values.get(i)[2]+"*"+values.get(i)[1]+" = "+values.get(i)[3], 10, 75+45*(i-1), true);
            drawLine(10, 95+45*(i-1), values.get(i)[0], GREEN, bufferGC);
            for(int j=0; j<values.get(i)[2]; j++) {
                drawLine(10+5*j*values.get(i)[1], 105+45*(i-1)+(j%2)*2, values.get(i)[1], RED, bufferGC);
            }
            
            if (values.get(i)[3]==0) {
                bufferGC.setForeground(BLACK);
                bufferGC.drawText(Messages.Euclid_GCD+values.get(0)[0]+","+values.get(0)[1]+") = "+values.get(i)[1], 10, 75+45*(i), true);
            } else {
                drawLine(10+5*values.get(i)[1]*values.get(i)[2], 105+45*(i-1), values.get(i)[3], BLUE, bufferGC);
            }
        }
    }

    private void drawLine(int x, int y, int w, Color color, GC gc) {
        gc.setForeground(color);
        gc.drawLine(x, y, x+w*5, y);
        gc.drawLine(x, y+5, x+w*5, y+5);
        for(int i=0; i<=w; i++)
            gc.drawLine(x+5*i, y, x+5*i, y+5);
    }

    // diese Methode ist dafür da, das PaintEvent im Listener zu verarbeiten.
    // achtet darauf, dass das canvas und der Buffer immer dieselbe Höhe und Breite beitzen.
    public void copyImageToUI(PaintEvent event) {
        // Das Event sagt uns, welches Rechteck bemalt werden soll. Möglicheweise ist es imer nur der
        // gerade sichtbare Teil (was neben der bereits gepufferten Zeichenoperaton noch weiteren
        // Performance-Gewinn bringen würde)!
        int x = event.x;
        int y = event.y;
        int width = event.width;
        int height = event.height;
        GC canvasGC = event.gc;
        System.out.println(bufferImage.getBounds().width+" . "+bufferImage.getBounds().height);
        System.out.println(x+" "+y+" "+width+" "+height);
        canvasGC.drawImage(bufferImage,
            x, y, width, height,   // Buffer-Rechteck
            x, y, width, height);  // Ziel-Rechteck (im Canvas)
    }
}
