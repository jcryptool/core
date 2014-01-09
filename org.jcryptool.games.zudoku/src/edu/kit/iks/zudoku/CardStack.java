package edu.kit.iks.zudoku;

import java.util.EmptyStackException;
import java.util.Stack;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public abstract class CardStack extends JPanel {
	private SudokuField parent;
	private Stack<Card> cards;
	
	public CardStack(SudokuField parent, Point location) {
		super();
		
		assert(parent != null);
		this.parent = parent;
		
		setBounds(new Rectangle(location.x, location.y, Zudoku.STACK_WIDTH, Zudoku.STACK_HEIGHT));		
		cards = new Stack<Card>();
		
		setOpaque(true);
	}
	
	public SudokuField getParent() {
		return parent;
	}
	
	/*
	 * Stack operations.
	 */
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	
	public void pushCard(Card card) {
		this.cards.push(card);
	}
	
	public Card popCard() {
		return this.cards.pop();
	}
	
	public Card topCard() {
		try {
			return this.cards.peek(); 
		} catch(EmptyStackException e) {
			return null;
		}
	}
	
	/*
	 * request to place a card in the tray
	 * pos is a pivot position (not necessarily the cards position, 
	 * might be the mouse pos as well.
	 */
	abstract public CardStack placeCard(Card card, Point pos);
	
	public void destroy() {
		for(Card card : cards) {
			this.getParent().removeCard(card);
		}
	}
}
