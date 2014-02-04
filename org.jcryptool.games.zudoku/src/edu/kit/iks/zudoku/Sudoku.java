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
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Sudoku {
	public static final int MARKER_THICKNESS = 4;
	public static final int MARKER_PADDING = ZudokuConfig.STACK_PADDING / 2 + MARKER_THICKNESS / 2;
	
	/*
	 * The hard coded 4x4 solutions (transposed)
	 */
	public static final int[][][] SOLUTIONS = 
		{
			{
				{1, 2, 3, 4},
				{3, 4, 1, 2},
				{4, 3, 2, 1}, 
				{2, 1, 4, 3}
			},
			{
				{4, 3, 1, 2},
				{2, 1, 3, 4},
				{3, 4, 2, 1}, 
				{1, 2, 4, 3}
			},
			{
				{4, 3, 2, 1},
				{2, 1, 4, 3},
				{1, 2, 3, 4},
				{3, 4, 1, 2}				
			},
			{
				{1, 2, 4, 3},
				{3, 4, 2, 1},
				{2, 1, 3, 4},
				{4, 3, 1, 2}				
			},
			{
				{1, 2, 3, 4},
				{3, 4, 1, 2},
				{2, 1, 4, 3},
				{4, 3, 2, 1}				
			},
		};
	
	/*
	 * Caution: Assumption that fields is square is implicitly used
	 */
	private Field[][] fields;
	private int[][] solution;
	
	private SudokuField parent;
	private int pos_x;
	private int pos_y;

	// Active flag changes whether the user can actively move around cards and 
	// supply stacks are provided, etc...
	private boolean active;
	
	public Sudoku(SudokuField parent, int pos_x, int pos_y, boolean active) {
		assert(parent != null);
		this.parent = parent;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.active = active;
		
		this.fields = new Field[ZudokuConfig.SUDOKU_SIZE][ZudokuConfig.SUDOKU_SIZE];
		for(int i = 0; i < this.fields.length; i++) {
			for(int j = 0; j < this.fields.length; j++) {
				int x = this.pos_x + i * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING);
				int y = this.pos_y + j * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING);
				this.fields[i][j] = new Field(this.parent, new Point(x, y));
				this.parent.add(fields[i][j], SudokuField.STACK_LAYER);
			}
		}
		
		/*
		 * Prepare solution and hints
		 * 
		 * NOTE: Hints might not lead to a unique solution this way.
		 */
		if(ZudokuConfig.SUDOKU_SIZE == 4) {
			int r = (int)Math.floor(Sudoku.SOLUTIONS.length * Math.random());
			this.solution = Sudoku.SOLUTIONS[r].clone();
			
			// NOTE: Probabilistic algorithm - terminates with probability overwhelming in number of iterations. :)
			// It first picks a random starting block and then iterates over the block
			// and randomly selects one not yet selected field in the current block to use as a hint 
			// until enough fields are selected. 
			int n = 0;
			int k = (int)Math.floor(Math.random() * this.fields.length);
			while(n < Math.min(ZudokuConfig.NUMBER_OF_HINTS, this.fields.length * this.fields.length)) {
				int l = (int)Math.floor(Math.random() * this.fields.length);
				int i = getBlockIndexI(k, l);
				int j = getBlockIndexJ(k, l);
				if(this.fields[i][j].topCard() == null) {
					// This field is not used as a hint yet. Pick it. 
					// (Otherwise continue and pick new random i,j.)
					Card fixed_card = new Card(parent, this.fields[i][j], this.solution[i][j], true);
					fixed_card.setActive(active);
					this.parent.addCard(fixed_card);
					this.fields[i][j].pushCard(fixed_card);
					n++;
					k = (k + 1) % this.fields.length;
				}
			}
		} else {
			// TODO: Include solutions for other sizes (implement generator?)
			this.solution = new int[ZudokuConfig.SUDOKU_SIZE][ZudokuConfig.SUDOKU_SIZE];
		}
	}
	
	/*
	 * Helpers to address elements in blocks.
	 * 
	 * Blocks are counted from left to right and then from top to bottom. 
	 * E.g., for a sudoku of size 4:
	 * 
	 * 0 | 1
	 * --+--
	 * 2 | 3
	 * 
	 * The elements in blocks are counted the same way.
	 */
	public int getBlockIndexI(int block, int element) {
		assert(0 <= block && block < this.fields.length);
		assert(0 <= element && element < this.fields.length);
		int blocksize = (int)Math.sqrt(this.fields.length);
		return blocksize * (block % blocksize) + element % blocksize;
	}
	
	public int getBlockIndexJ(int block, int element) {
		assert(0 <= block && block < this.fields.length);
		assert(0 <= element && element < this.fields.length);
		int blocksize = (int)Math.sqrt(this.fields.length);
		return blocksize * (block / blocksize) + element / blocksize;
	}
	
	public CardStack placeCard(Card card, Point pos) {
		CardStack stack = null;
		for(int i = 0; i < this.fields.length; i++) {
			for(int j = 0; j < this.fields[i].length; j++) {
				// try to place card on field 
				stack = fields[i][j].placeCard(card, pos);
				if(stack != null) {
					return stack;
				}
			}
		}
		return stack;
	}
	
	public boolean checkCards(Set<Card> cards) {
		if(cards.size() < this.fields.length) {
			return false;
		}
		boolean[] occurred = new boolean[this.fields.length];
		for(Card card : cards) {
			int n = card.getNumber();
			assert(1 <= n && n <= this.fields.length);
			if(occurred[n - 1]) {
				return false;
			}
			occurred[n - 1] = true;
		}
		return true;	
	}
	
	public Set<Card> getColumn(int i) {
		assert(0 <= i && i < this.fields.length);
		Set<Card> cards = new HashSet<Card>();
		for(int j = 0; j < this.fields.length; j++) {
			if(this.fields[i][j].topCard() != null) {
				cards.add(this.fields[i][j].topCard());
			}			
		}
		return cards;		
	}
	
	public JLabel getColumnMarker(int i, Color color) {
		JLabel marker = new JLabel();
		int x = pos_x + i * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING) - MARKER_PADDING;
		int y = pos_y - MARKER_PADDING;
		int width = ZudokuConfig.STACK_WIDTH + 2 * MARKER_PADDING;
		int height = fields.length * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING + 2 * MARKER_PADDING; 
		marker.setBounds(x, y, width, height);
		marker.setBorder(BorderFactory.createLineBorder(color, MARKER_THICKNESS));
		return marker;
	}
	
	public Set<Card> getRow(int j) {
		assert(0 <= j && j < this.fields.length);
		Set<Card> cards = new HashSet<Card>();
		for(int i = 0; i < this.fields.length; i++) {
			if(this.fields[i][j].topCard() != null) {
				cards.add(this.fields[i][j].topCard());
			}
		}
		return cards;
	}
	
	public JLabel getRowMarker(int j, Color color) {
		JLabel marker = new JLabel();
		int x = pos_x - MARKER_PADDING;
		int y = pos_y + j * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING) - MARKER_PADDING;
		int width = fields.length * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING + 2 * MARKER_PADDING;
		int height = ZudokuConfig.STACK_HEIGHT + 2 * MARKER_PADDING; 
		marker.setBounds(x, y, width, height);
		marker.setBorder(BorderFactory.createLineBorder(color, MARKER_THICKNESS));
		return marker;
	}
	
	/*
	 * Blocks are counted from left to right and then from top to bottom. 
	 * E.g., for a sudoku of size 4:
	 * 
	 * 0 | 1
	 * --+--
	 * 2 | 3
	 * 
	 */
	public Set<Card> getBlock(int k) {
		assert(0 <= k && k < this.fields.length);
		Set<Card> cards = new HashSet<Card>();
		for(int l = 0; l < this.fields.length; l++) {
			int i = this.getBlockIndexI(k, l);
			int j = this.getBlockIndexJ(k, l);
			if(this.fields[i][j].topCard() != null) {
				cards.add(this.fields[i][j].topCard());
			}
		}
		return cards;		
	}
	
	public JLabel getBlockMarker(int k, Color color) {
		JLabel marker = new JLabel();
		int i = getBlockIndexI(k, 0);
		int j = getBlockIndexJ(k, 0);
		int x = pos_x + i * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING) - MARKER_PADDING;
		int y = pos_y + j * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING) - MARKER_PADDING;
		int width = (int)Math.sqrt(fields.length) * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING + 2 * MARKER_PADDING;
		int height = (int)Math.sqrt(fields.length) * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING + 2 * MARKER_PADDING;
		marker.setBounds(x, y, width, height);
		marker.setBorder(BorderFactory.createLineBorder(color, MARKER_THICKNESS));
		return marker;
	}
	
	public void solve() {
		/* TODO: Try to complete the current try first? */
		for(int i = 0; i < fields.length; i++) {
			for(int j = 0; j < fields.length; j++) {
				Card card = fields[i][j].topCard();
				if(card == null) {
					// field is empty
					card = new Card(parent, this.fields[i][j], this.solution[i][j]);
					card.setActive(active);
					parent.addCard(card);
					fields[i][j].pushCard(card);					
				} else {
					card.setNumber(this.solution[i][j]);
				}
			}
		}
		parent.revalidate();
	}
	
	public void flipCards() {
		for(int i = 0; i < fields.length; i++) {
			for(int j = 0; j < fields.length; j++) {
				Card card = fields[i][j].topCard();
				if(card != null) {
					card.flip();
				}
			}
		}
	}
	
	// TODO: Move this method elsewhere
	public void cheat() {
		Set<Card> cards = new HashSet<Card>();
		for(int i = 0; i < fields.length; i++) {
			for(int j = 0; j < fields.length; j++) {
				Card card = fields[i][j].topCard();
				if(card != null && !card.isFixed()) {
					cards.add(card);
				}
			}
		}
		if(cards.size() > 0) {
			Card rand_card = (Card)cards.toArray()[(int)(cards.size() * Math.random())];
			
			int rand_num;
			do {
				rand_num = (int)(ZudokuConfig.SUDOKU_SIZE * Math.random()) + 1;
			} while(rand_num == rand_card.getNumber());
			rand_card.setNumber(rand_num);
			rand_card.repaint();
		}
	}
	
	/*
	 * Clean up the sudoku
	 */
	public void destroy() {
		for(int i = 0; i < this.fields.length; i++) {
			for(int j = 0; j < this.fields.length; j++) {
				fields[i][j].destroy();
			}
		}
	}
}