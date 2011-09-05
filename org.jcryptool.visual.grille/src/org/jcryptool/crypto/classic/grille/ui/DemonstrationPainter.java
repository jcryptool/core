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

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.classic.grille.GrillePlugin;
import org.jcryptool.crypto.classic.grille.algorithm.Schablone;

public class DemonstrationPainter implements PaintListener {

    private Demonstration demonstration;
    private HashMap<Character, Point> charSize;
    private int width;
    private int height;
    private int cellWidth;
    private int cellHeight;
    private Canvas parent;

    @SuppressWarnings("unchecked")
    public DemonstrationPainter(Canvas parent, Demonstration demonstration) {
        this.demonstration = demonstration;
        this.parent = parent;
        try {
            File file = new File(FileLocator.toFileURL(GrillePlugin.getDefault().getBundle().getEntry("/")).getFile() //$NON-NLS-1$
                    + "files" + File.separatorChar + "charSize.map"); //$NON-NLS-1$ //$NON-NLS-2$
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            charSize = (HashMap<Character, Point>) ois.readObject();
        } catch (Exception e) {
            LogUtil.logError(GrillePlugin.PLUGIN_ID, e);
        }
    }

    public void paintControl(PaintEvent e) {
        width = parent.getSize().x;
        height = parent.getSize().y;
        if (demonstration.getCurrentStep() == 0) {
            e.gc.fillRectangle(0, 0, width, height);
        } else if (demonstration.getCurrentStep() == 1) {
            e.gc.setFont(FontService.getLargeFont());
            e.gc.drawText(Messages.getString("DemonstrationPainter.description"), 0, 0); //$NON-NLS-1$
            if (!demonstration.padding.equals("")) { //$NON-NLS-1$
                e.gc.drawText(Messages.getString("DemonstrationPainter.padding"), 0, 40); //$NON-NLS-1$
                Color savedColor = e.gc.getForeground();
                e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                e.gc.drawString(demonstration.padding.substring(0, Math.min(25, demonstration.padding.length())), 70, 40);
                for (int i = 25; i < demonstration.padding.length(); i = i + 40)
                    e.gc.drawString(demonstration.padding.substring(i, Math.min(demonstration.padding.length(), i + 40)), 0, 40 + (i / 25) * 20);
                e.gc.setForeground(savedColor);

            }
        } else if (demonstration.getCurrentStep() <= 5) {
            Schablone crypt = demonstration.getSchablone();
            cellWidth = width / crypt.getSize();
            cellHeight = cellWidth;
            int width = cellWidth * crypt.getSize();
            int height = width;

            for (int y = 0; y < crypt.getSize(); y++) {
                for (int x = 0; x < crypt.getSize(); x++) {
                    if (crypt.get(y, x) != '0') {
                        Color savedColor = e.gc.getForeground();
                        Font savedFont = e.gc.getFont();
                        if (demonstration.getKey().get(y, x) == '1')
                            e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

                        fillCell(e, x, y, crypt.get(y, x));

                        e.gc.setFont(savedFont);
                        e.gc.setForeground(savedColor);
                    }
                }
            }
            for (int i = 0; i < crypt.getSize() + 1; i++) {
                e.gc.drawLine((i) * (width / crypt.getSize()), 0, (i) * (width / crypt.getSize()), height);
                e.gc.drawLine(0, (i) * (width / crypt.getSize()), height, (i) * (width / crypt.getSize()));
            }
        } else {
            Schablone crypt = demonstration.getSchablone();
            cellWidth = width / crypt.getSize();
            cellHeight = cellWidth;
            int width = cellWidth * crypt.getSize();
            int height = width;
            Color savedColor = e.gc.getForeground();
            Font savedFont = e.gc.getFont();

            for (int y = 0; y < crypt.getSize(); y++) {
                for (int x = 0; x < crypt.getSize(); x++) {
                    if (crypt.get(y, x) != '0') {
                        fillCell(e, x, y, crypt.get(y, x));
                    }
                }
            }
            e.gc.setFont(savedFont);
            e.gc.setForeground(savedColor);

            for (int i = 0; i < crypt.getSize() + 1; i++) {
                e.gc.drawLine((i) * (width / crypt.getSize()), 0, (i) * (width / crypt.getSize()), height);
                e.gc.drawLine(0, (i) * (width / crypt.getSize()), height, (i) * (width / crypt.getSize()));
            }

            savedColor = e.gc.getForeground();
            e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));

            for (int i = 0; i < crypt.getSize(); i++) {
                e.gc.drawLine(cellWidth / 2, cellHeight / 2 + i * cellHeight, cellWidth / 2 + (crypt.getSize() - 1)
                        * cellWidth, cellHeight / 2 + i * cellHeight);
                e.gc.drawLine(cellWidth / 2, cellHeight / 2 - 1 + i * cellHeight, cellWidth / 2 + (crypt.getSize() - 1)
                        * cellWidth, cellHeight / 2 - 1 + i * cellHeight);
                e.gc.drawLine(cellWidth / 2, cellHeight / 2 + 1 + i * cellHeight, cellWidth / 2 + (crypt.getSize() - 1)
                        * cellWidth, cellHeight / 2 + 1 + i * cellHeight);
                e.gc.drawLine(cellWidth / 2, cellHeight / 2 + 2 + i * cellHeight, cellWidth / 2 + (crypt.getSize() - 1)
                        * cellWidth, cellHeight / 2 + 2 + i * cellHeight);
                e.gc.drawLine(cellWidth / 2, cellHeight / 2 - 2 + i * cellHeight, cellWidth / 2 + (crypt.getSize() - 1)
                        * cellWidth, cellHeight / 2 - 2 + i * cellHeight);
                if (i != crypt.getSize() - 1) {
                    e.gc.drawLine(cellWidth / 2, cellHeight / 2 + (i + 1) * cellHeight, cellWidth / 2
                            + (crypt.getSize() - 1) * cellWidth, cellHeight / 2 + i * cellHeight);
                    e.gc.drawLine(cellWidth / 2, cellHeight / 2 - 1 + (i + 1) * cellHeight, cellWidth / 2
                            + (crypt.getSize() - 1) * cellWidth, cellHeight / 2 - 1 + i * cellHeight);
                    e.gc.drawLine(cellWidth / 2, cellHeight / 2 + 1 + (i + 1) * cellHeight, cellWidth / 2
                            + (crypt.getSize() - 1) * cellWidth, cellHeight / 2 + 1 + i * cellHeight);

                }

            }
            e.gc.drawLine(cellWidth / 2 + (crypt.getSize() - 1) * cellWidth - (cellWidth / 2 / 2), cellHeight / 2
                    + (crypt.getSize() - 1) * cellHeight + (cellWidth / 2 / 2), cellWidth / 2 + (crypt.getSize() - 1)
                    * cellWidth, cellHeight / 2 + (crypt.getSize() - 1) * cellHeight);
            e.gc.drawLine(cellWidth / 2 + (crypt.getSize() - 1) * cellWidth - (cellWidth / 2 / 2) - 1, cellHeight / 2
                    + (crypt.getSize() - 1) * cellHeight + (cellWidth / 2 / 2), cellWidth / 2 + (crypt.getSize() - 1)
                    * cellWidth - 1, cellHeight / 2 + (crypt.getSize() - 1) * cellHeight);
            e.gc.drawLine(cellWidth / 2 + (crypt.getSize() - 1) * cellWidth - (cellWidth / 2 / 2) + 1, cellHeight / 2
                    + (crypt.getSize() - 1) * cellHeight + (cellWidth / 2 / 2), cellWidth / 2 + (crypt.getSize() - 1)
                    * cellWidth + 1, cellHeight / 2 + (crypt.getSize() - 1) * cellHeight);
            e.gc.drawLine(cellWidth / 2 + (crypt.getSize() - 1) * cellWidth - (cellWidth / 2 / 2), cellHeight / 2
                    + (crypt.getSize() - 1) * cellHeight - (cellWidth / 2 / 2), cellWidth / 2 + (crypt.getSize() - 1)
                    * cellWidth, cellHeight / 2 + (crypt.getSize() - 1) * cellHeight);
            e.gc.drawLine(cellWidth / 2 + (crypt.getSize() - 1) * cellWidth - (cellWidth / 2 / 2) - 1, cellHeight / 2
                    + (crypt.getSize() - 1) * cellHeight - (cellWidth / 2 / 2), cellWidth / 2 + (crypt.getSize() - 1)
                    * cellWidth - 1, cellHeight / 2 + (crypt.getSize() - 1) * cellHeight);
            e.gc.drawLine(cellWidth / 2 + (crypt.getSize() - 1) * cellWidth - (cellWidth / 2 / 2) + 1, cellHeight / 2
                    + (crypt.getSize() - 1) * cellHeight - (cellWidth / 2 / 2), cellWidth / 2 + (crypt.getSize() - 1)
                    * cellWidth + 1, cellHeight / 2 + (crypt.getSize() - 1) * cellHeight);
            e.gc.setForeground(savedColor);
        }
    }

    private void fillCell(PaintEvent e, int x, int y, char c) {
        Point eckeLO = new Point(x * cellWidth, y * cellHeight);
        int fontSize = (int) Math.round(10 / (double) 17 * cellHeight * 0.9);
        e.gc.setFont(new Font(Display.getCurrent(), "Times Roman", fontSize, SWT.NORMAL)); //$NON-NLS-1$
        e.gc.drawString("" + c, eckeLO.x + (int) Math.round(cellWidth - charSize.get(c).x / (double) 10 * fontSize) / 2, eckeLO.y + (cellHeight - fontSize - (int) Math.round(5 / (double) 10 * fontSize)) / 2); //$NON-NLS-1$
    }

}