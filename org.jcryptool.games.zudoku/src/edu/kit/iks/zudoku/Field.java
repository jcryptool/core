//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 Florian Böhl <florian@boehl.name>
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package edu.kit.iks.zudoku;

import java.awt.Graphics;
import java.awt.Point;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class Field extends CardStack {
	public Field(SudokuField parent, Point location) {
		super(parent, location);
	}
	
	@Override
	public CardStack placeCard(Card card, Point pos) {
		if(this.isEmpty() && this.getBounds().contains(pos)) {
			this.pushCard(card);
			card.setLocation(this.getLocation());
			return this;
		} else {
			return null;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(ZudokuConfig.STACK_BACKGROUND_COLOR);
	}
}
