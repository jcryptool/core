// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.grille.ui;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Canvas;
import org.jcryptool.visual.grille.algorithm.Grille;
import org.jcryptool.visual.grille.algorithm.KeySchablone;

public class KeyListener implements MouseListener {

	private View view;
	private Grille model;

	public KeyListener(Grille model, View view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {}

	@Override
	public void mouseDown(MouseEvent e) {}

	@Override
	public void mouseUp(MouseEvent e) {
		KeySchablone key = model.getKey();
		int width = ((Canvas) e.widget).getSize().x;
		int cellWidth = width/key.getSize();
		int cellHeight = cellWidth;
		int posX = (int) Math.floor((double)e.x/cellWidth);
		int posY = (int) Math.floor((double)e.y/cellHeight);
		// key.toogle(...) setzt oder löscht ein loch aus der Schablone. 
		// Wird true, wenn ein Loch geändert wurde.
		if (key.toggle(posY,posX)) {
			//reset the outputText to "" if the key Schablone has changed.
			view.setText_outputText("");
			((Canvas) e.widget).redraw();
			view.checkOkButton();
			view.updateKeyText();
//			System.out.println(model.getKey().toString());
		}
	}
}
