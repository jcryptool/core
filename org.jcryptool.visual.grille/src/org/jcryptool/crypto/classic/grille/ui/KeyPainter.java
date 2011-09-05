// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.grille.ui;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.jcryptool.crypto.classic.grille.algorithm.Grille;
import org.jcryptool.crypto.classic.grille.algorithm.KeySchablone;

public class KeyPainter implements PaintListener {

    private int width;
    private int cellWidth;
    private int keyWidth;
    private int keyHeight;
    private int cellHeight;
    private Grille model;
    private Canvas parent;

    public KeyPainter(Canvas parent, Grille model) {
        this.model = model;
        this.parent = parent;

    }

    public void paintControl(PaintEvent e) {
        KeySchablone key = model.getKey();
        width = Math.min(parent.getSize().x, parent.getSize().y);
        cellWidth = width / key.getSize();
        cellHeight = cellWidth;
        keyWidth = cellWidth * key.getSize();
        keyHeight = keyWidth;
        for (int i = 0; i < key.getSize() + 1; i++) {
            e.gc.drawLine((i) * (keyWidth / key.getSize()), 0, (i) * (keyWidth / key.getSize()), keyHeight);
            e.gc.drawLine(0, (i) * (keyWidth / key.getSize()), keyHeight, (i) * (keyWidth / key.getSize()));
        }
        for (int y = 0; y < key.getSize(); y++) {
            for (int x = 0; x < key.getSize(); x++) {
                if (key.get(y, x) == '1') {
                    drawHole(e, x, y);
                }
                if (key.get(y, x) == '2') {
                    greyOut(e, x, y);
                }
            }
        }
    }

    private void drawHole(PaintEvent e, int x, int y) {
        Point eckeLO = new Point(x * cellWidth + 1, y * cellHeight + 1);
        e.gc.drawOval(eckeLO.x, eckeLO.y, cellWidth - 2, cellHeight - 2);

    }

    private void greyOut(PaintEvent e, int x, int y) {
        Point eckeLO = new Point(x * cellWidth + 1, y * cellHeight + 1);
        Color grey = new Color(e.display, 200, 200, 200);
        e.gc.setBackground(grey);
        e.gc.fillRectangle(eckeLO.x, eckeLO.y, cellWidth - 1, cellHeight - 1);
        grey.dispose();
    }
}