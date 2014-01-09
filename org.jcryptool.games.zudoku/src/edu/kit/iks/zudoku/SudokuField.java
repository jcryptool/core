package edu.kit.iks.zudoku;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLayeredPane;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class SudokuField extends JLayeredPane {
	public static final Integer MARKER_LAYER = 0;
	public static final Integer STACK_LAYER = 1;
	public static final Integer CARD_LAYER = 2;
	public static final Integer MOVING_CARD_LAYER = 3;
	public static final Integer OVERLAY_LAYER = 4;
	
	private CardStack[] supply_stacks;
	private CardStack[] verify_stacks;
	private Sudoku sudoku;
	
	// Active flag changes whether the user can actively move around cards and 
	// supply stacks are provided, etc...
	private boolean active;
	
	private Set<Card> cards;
	
	private void createSupplyStacks() {
		assert(supply_stacks == null);
		supply_stacks = new SupplyStack[Zudoku.SUDOKU_SIZE];
    	for(int i = 0; i < supply_stacks.length; i++) {
    		supply_stacks[i] =
    			new SupplyStack(
    				this,
    				new Point(Zudoku.BORDER_PADDING, 
    				            Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_LEFT_PADDING 
    				          + i * (Zudoku.STACK_HEIGHT + Zudoku.STACK_PADDING)),
    				i + 1); 
    		add(supply_stacks[i]);//, STACK_LAYER);
    	}
	}
	
	private void createVerificationStacks() {
		assert(verify_stacks == null);
		
		verify_stacks = new Field[Zudoku.SUDOKU_SIZE];
    	for(int i = 0; i < verify_stacks.length; i++) {
    		verify_stacks[i] = 
    			new Field(this,
    					  new Point(  Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING
    							    + i * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING),
    							    Zudoku.BORDER_PADDING));
    		add(verify_stacks[i]); //, STACK_LAYER);
    	}
	}
	
	public SudokuField(boolean active) {
		super();
		
		this.active = active;
		
		cards = new HashSet<Card>();
		
		setOpaque(false);
		
		setPreferredSize(
		    	new Dimension(  Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING
		    			      + Zudoku.SUDOKU_SIZE * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING)
		    			      + Zudoku.BORDER_PADDING, 
		    			        Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING 
		    			      + Zudoku.SUDOKU_SIZE * (Zudoku.STACK_HEIGHT + Zudoku.STACK_PADDING)
		    			      + Zudoku.BORDER_PADDING));
		if(active) {
			createSupplyStacks();
		}
	    createVerificationStacks();
	    sudoku = new Sudoku(this,
	    		            Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING, 
		                    Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING,
		                    active);	
	}
	
	public void newSudoku() {
		/*
		 * Clean up
		 */
		sudoku.destroy();
		
		/*
		 * Create new sudoku
		 */
		sudoku = new Sudoku(this, 
				            Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING, 
				            Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING,
				            active);
		repaint();
	}
	
	public Sudoku getSudoku() {
		return sudoku;		
	}
	
	public CardStack[] getVerificationStacks() {
		return verify_stacks;
	}
	
    public CardStack placeCard(Card card, Point pos) {
    	/*
    	 * Try to place card in Sudoku.
    	 */
    	CardStack stack = this.sudoku.placeCard(card, pos);
    	
    	/*
    	 * Try to place card on a supply stack.
    	 */
    	if(stack != null) {
    		return stack;
    	}
		for(CardStack s : this.supply_stacks) {
			stack = s.placeCard(card, pos);
			if(stack != null) {
				return stack;
			}
		} 
    	return stack;
    }
    
    public void addCard(Card card) {
    	cards.add(card);
    	add(card, CARD_LAYER);
    }
    
    public void removeCard(Card card) {
    	cards.remove(card);
    	remove(card);
    }
}
