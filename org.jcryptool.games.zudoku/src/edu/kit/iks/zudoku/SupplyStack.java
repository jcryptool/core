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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/*
 * Behaves like an unlimited stack of cards.
 */

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class SupplyStack extends CardStack {
	private int number;

	public SupplyStack(SudokuField parent, Point location, int number) {
		super(parent, location);
		assert(number > 0);
		this.number = number;
		
		/*
		 *  Create a fresh card.
		 */
		Card card = new Card(parent, this, this.number);
		getParent().addCard(card);
		super.pushCard(card);
	}
	
	@Override
	public void pushCard(Card card) {
		assert(false); // this function should never be called
	}
	
	@Override
	public Card popCard() {
		/*
		 * Caution: It is important actually pop card and not just return a fresh one!
		 * 
		 * E.g., the card might have been moved (drag & drop). Hence it lies on a 
		 * different stack now and this stack has to forget about it (and pop should
		 * return the card that has been moved).
		 */
		Card popped_card = super.popCard();
		
		/*
		 *  Create a fresh card.
		 */
		Card new_card = new Card(getParent(), this, this.number);
		getParent().addCard(new_card);
		super.pushCard(new_card);
		
		return popped_card;
	}

	@Override
	public CardStack placeCard(Card card, Point pos) {
		if(this.getBounds().contains(pos)) {
			getParent().removeCard(card);
			return this;
		} else {
			return null;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		//System.err.println("Paint SupplyStack: g: " + g.getClip() + ", stack: " + getBounds());
		super.paintComponent(g);
		setBackground(new Color(0, 157, 130));
	}
}