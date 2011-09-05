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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Canvas;
import org.jcryptool.crypto.classic.grille.algorithm.Grille;
import org.jcryptool.crypto.classic.grille.algorithm.KeySchablone;

public class KeyListener implements MouseListener {

	private View view;
	private Grille model;

	public KeyListener(Grille model, View view) {
		this.model = model;
		this.view = view;
	}

	public void mouseDoubleClick(MouseEvent e) {}

	public void mouseDown(MouseEvent e) {}

	public void mouseUp(MouseEvent e) {
		KeySchablone key = model.getKey();
		int width = ((Canvas) e.widget).getSize().x;
		int cellWidth = width/key.getSize();
		int cellHeight = cellWidth;
		int posX = (int) Math.floor((double)e.x/cellWidth);
		int posY = (int) Math.floor((double)e.y/cellHeight);
		key.toggle(posY,posX);
		((Canvas) e.widget).redraw();
		view.checkOkButton();
	}
}
