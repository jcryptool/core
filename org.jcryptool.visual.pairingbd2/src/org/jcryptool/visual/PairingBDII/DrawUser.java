//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.util.fonts.FontService;

public class DrawUser {
    private final int index;
    private final int posx;
    private final int posy;
    private final Display display;
    private final GC gc;
    private final Color color;

    public DrawUser(int myindex, int myx, int myy, Display myd, GC myGC, Color color) {
        index = myindex;
        posx = myx;
        posy = myy;
        display = myd;
        gc = myGC;

        final Color grey = new Color(display, 235, 235, 235);
        this.color = color;

        gc.setForeground(color);
        gc.setBackground(color);
        gc.fillOval(posx, posy, 10, 10);
        gc.setBackground(grey);
        gc.setFont(FontService.getSmallFont());
        gc.drawString(Integer.toString(index), posx + 10, posy + 10);
        grey.dispose();
    }

    public void drawLineTo(DrawUser User2) {
        gc.setLineWidth(1);
        final int fromx = getX() + 5;
        final int fromy = getY() + 5;
        final int tox = User2.getX() + 5;
        final int toy = User2.getY() + 5;

        gc.setForeground(color);
        gc.drawLine(fromx, fromy, tox, toy);

    }

    public int getIndex() {
        return index;
    }

    public int getX() {
        return posx;
    }

    public int getY() {
        return posy;
    }
}
