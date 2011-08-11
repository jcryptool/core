// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.zeroknowledge.ui.magischetuer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MBob;
import org.jcryptool.visual.zeroknowledge.algorithm.magischetuer.MDoor;

/**
 * Klasse zum Zeichnen eines Gebäudes abhängig von der Wahl von Alice/Carol und Bob.
 *
 * @author Mareike Paul
 *@version 1.0.0
 */
public class ShowGeb implements PaintListener {

    private MBob bob;

    private Canvas canv;

    private MDoor door;

    private int fall;

    /**
     * Konstruktor, der einen neuen Canvas erstellt und das erstellt Objekt als PaintListener
     * hinzufügt.
     *
     * @param fall Fall, der bestrachtet werden soll
     * @param parent Parent des Canvas
     * @param b M_Bob, der die Raumwahl enthält
     */
    public ShowGeb(int fall, Composite parent, MBob b, MDoor door) {
        this.fall = fall;
        this.bob = b;
        this.door = door;
        canv = new Canvas(parent, SWT.BORDER);
        canv.setBackground(canv.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        canv.setSize(200, 190);
        canv.addPaintListener(this);
    }

    /**
     * Methode, die aufgerufen wird, falls der Canvas ein PaintEvent abschickt
     */
    public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        gc.setLineWidth(2);
        gc.drawLine(10, 10, 190, 10); // Hintere Wand
        gc.drawLine(10, 10, 10, 100); // Linke Wand
        gc.drawLine(190, 10, 190, 100); // Rechte Wand
        gc.drawLine(100, 50, 100, 100); // Mittelwand zwischen den Hauptraeumen
        gc.drawLine(80, 100, 120, 100); // Wand zwischen den nicht
        // abgeschlossenen Tueren
        gc.drawLine(10, 100, 40, 100); // kurze Wand links
        gc.drawLine(160, 100, 190, 100); // kurze Wand rechts
        gc.drawLine(20, 100, 20, 155); // Linke Wand Vorraum
        gc.drawLine(180, 100, 180, 155); // Rechte Wand Vorraum
        gc.drawLine(20, 155, 80, 155); // Linker Teil Vorderwand
        gc.drawLine(120, 155, 180, 155); // Rechter Teil Vorderwand

        gc.setFont(FontService.getNormalBoldFont());
        gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
        int[] code = door.getCode();
        String s = ""; //$NON-NLS-1$
        for (int i = 0; i < code.length / 2; i++)
            s += code[i];
        s += " "; //$NON-NLS-1$
        for (int i = code.length / 2; i < code.length; i++)
            s += code[i];
        gc.drawString(s, 68, 25);

        gc.setFont(FontService.getNormalBoldFont());
        gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        gc.drawString(Messages.ShowGeb_4, 65, 115);
        gc.drawString(Messages.ShowGeb_5, 35, 45);
        gc.drawString(Messages.ShowGeb_6, 125, 45);

        int x;
        if (bob.getRaumwahl() == 1) {
            x = 30;
        } else
            x = 160;

        String set;
        if ((fall & 16) == 0)
            set = "A"; //$NON-NLS-1$
        else
            set = "C"; //$NON-NLS-1$
        switch (fall) {
            // beide stehen davor
            case 0:
            case 16:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 68, 72);
                gc.drawLine(160, 100, 132, 72);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 20, 160);
                gc.drawString("B", 160, 160); //$NON-NLS-1$
                break;

            // in 1
            case 1:
            case 17:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 120, 155);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 20, 20);
                gc.drawString("B", 160, 160); //$NON-NLS-1$
                break;
            case 2:
            case 18:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 20, 20);
                gc.drawString("B", x, 115); //$NON-NLS-1$
                break;
            case 3:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(128, 22, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString("A", 170, 20); //$NON-NLS-1$
                gc.drawString("B", x, 115); //$NON-NLS-1$
                gc.setFont(FontService.getLargeBoldFont());
                gc.drawString("A", 20, 20); //$NON-NLS-1$
                gc.setLineStyle(SWT.LINE_DASH);
                gc.setLineWidth(1);
                gc.drawLine(35, 20, 160, 20);
                break;
            case 4:
            case 19:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 68, 72);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 20, 20);
                gc.drawString("B", x, 115); //$NON-NLS-1$
                break;

            // in 2
            case 5:
            case 20:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 120, 155);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 170, 20);
                gc.drawString("B", x, 160); //$NON-NLS-1$
                break;
            case 6:
            case 21:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 170, 20);
                gc.drawString("B", x, 115); //$NON-NLS-1$
                break;
            case 7:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 120, 100);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(128, 22, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString("A", 20, 20); //$NON-NLS-1$
                gc.drawString("B", x, 115); //$NON-NLS-1$
                gc.setFont(FontService.getLargeBoldFont());
                gc.drawString("A", 170, 20); //$NON-NLS-1$
                gc.setLineStyle(SWT.LINE_DASH);
                gc.setLineWidth(1);
                gc.drawLine(35, 20, 160, 20);
                break;
            case 8:
            case 22:
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_GREEN));
                gc.drawLine(40, 100, 80, 100);
                gc.drawLine(160, 100, 132, 72);
                gc.drawLine(80, 155, 115, 135);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_RED));
                gc.drawLine(100, 11, 100, 50);
                gc.setForeground(canv.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                gc.drawString(set, 170, 20);
                gc.drawString("B", x, 115); //$NON-NLS-1$
                break;
        }
        gc.dispose();
    }

    /**
     * macht den Canvas (un)sichtbar, abhängig von visible
     *
     * @param visible bestimmt die Sichtbarkeit des Canvas
     */
    public void setVisible(boolean visible) {
        canv.setVisible(visible);
    }
}
