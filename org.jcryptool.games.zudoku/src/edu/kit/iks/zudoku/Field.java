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
		setBackground(Zudoku.STACK_BACKGROUND_COLOR);
	}
}
