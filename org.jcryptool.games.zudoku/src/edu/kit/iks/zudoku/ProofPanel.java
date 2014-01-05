package edu.kit.iks.zudoku;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class ProofPanel extends JPanel  {		
	private JPanel controls;
	private SudokuField field;
	
	private Zudoku parent;
	
    /*
     * Helpers to holds state during multiple verification steps
     */
    private int vfy_step = 0;
    private JLabel vfy_marker;
    Map<Card, CardStack> vfy_original_stacks;
    private Set<Card> vfy_cards;
    private JLabel vfy_vmarker;
	
	private JPanel createControls() {
		JPanel controls = new JPanel();
		
		JButton button = null;
		List<JButton> buttons = new ArrayList<JButton>();
		
		if(Zudoku.PROOF_MODE_ACTIVE && Zudoku.VERIFICATION_MODE_ACTIVE) {
			button = new JButton();
			button.setText("Zurück");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					((CardLayout)ProofPanel.this.parent.getLayout()).show(ProofPanel.this.parent, Zudoku.WELCOME_CARD);
				}
			});
			buttons.add(button);
		}		
		
		button = new JButton();
		button.setText("Neues Sudoku");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				field.newSudoku();
			}
		});
		buttons.add(button);
		
		button = new JButton();
		button.setText("Löse Sudoku");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				field.getSudoku().solve();
			}
		});
		buttons.add(button);
		
		button = new JButton();
		button.setText("Fertig");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO: check whether the sudoku is complete
				if(vfy_step == 0) { // no verification going on
					field.getSudoku().flipCards();
				}
			}
		});
		buttons.add(button);
		
		button = new JButton();
		button.setText("Verifiziere");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step < 4) {
					vfy_step++;
					verify(vfy_step);
				}						
			}
		});
		buttons.add(button);
		
		controls.setLayout(new GridLayout(buttons.size(), 1));
		for(JButton b : buttons) {
			controls.add(b);
		}
		
		return controls; 
	}
	
	
	
    public ProofPanel(Zudoku parent, SudokuField field) {
    	super();
    	
    	this.parent = parent;
    	
    	setLayout(new BorderLayout());
    	
    	/*
    	 * Set up controls
    	 */
    	controls = createControls();
    	add(controls, BorderLayout.LINE_START);
    	
    	/*
    	 * Set up field
    	 */
    	this.field = field;
    	add(field, BorderLayout.CENTER);
    }
    
    private void verify(int step) {
    	Sudoku sudoku = field.getSudoku();
    	CardStack[] verify_stacks = field.getVerificationStacks();
    	switch(step) {
    		case 1:
		    	/*
		    	 * Pick random row, column, or block
		    	 */
		    	vfy_cards = null;
		    	vfy_marker = null;
		    	int index = (int)(Zudoku.SUDOKU_SIZE * Math.random());
		    	switch((int)(3 * Math.random())) {
			    	case 0:
			    		vfy_cards = sudoku.getColumn(index);
			    		vfy_marker = sudoku.getColumnMarker(index, Color.green);
			    		break;
			    	case 1:
			    		vfy_cards = sudoku.getRow(index);
			    		vfy_marker = sudoku.getRowMarker(index, Color.green);
			    		break;
			    	case 2:
			    		vfy_cards = sudoku.getBlock(index);
			    		vfy_marker = sudoku.getBlockMarker(index, Color.green);
			    		break;
			    	default:
			    		// Should never happen
			    		// TODO: Throw exception?
			    		System.err.println("Randomness out of control! o_O");
			    		return;
		    	}
		    	field.add(vfy_marker, SudokuField.MARKER_LAYER);
		    	break;
    		case 2:    	 	
		    	/*
		    	 * Move cards to verification stacks
		    	 */
		    	int i = 0;
		    	assert(vfy_cards.size() <= Zudoku.SUDOKU_SIZE);
		    	vfy_original_stacks = new HashMap<Card, CardStack>();
		    	for(Card card : vfy_cards) {
		    		vfy_original_stacks.put(card, card.getStack());
		    		card.getStack().popCard();
		    		verify_stacks[i].pushCard(card);
		    		card.setLocation(verify_stacks[i].getLocation());
		    		card.setStack(verify_stacks[i]);
		    		i++;    		
		    	}
		    	break;
    		case 3:  	
		    	/*
		    	 * Verify and mark
		    	 */
		    	Color color;
		    	for(Card card : vfy_cards) {
		    		card.flip();
		    	}
		    	if(sudoku.checkCards(vfy_cards)) {
		    		color = Color.green;
		    	} else {
		    		color = Color.red;
		    	}
		    	vfy_vmarker = new JLabel();
		    	int x = verify_stacks[0].getLocation().x - Sudoku.MARKER_PADDING;
				int y = verify_stacks[0].getLocation().y - Sudoku.MARKER_PADDING;
				int width = Zudoku.SUDOKU_SIZE * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING) - Zudoku.STACK_PADDING + 2 * Sudoku.MARKER_PADDING;
				int height = Zudoku.STACK_HEIGHT + 2 * Sudoku.MARKER_PADDING; 
				vfy_vmarker.setBounds(x, y, width, height);
				vfy_vmarker.setBorder(BorderFactory.createLineBorder(color, Sudoku.MARKER_THICKNESS));
				field.add(vfy_vmarker, SudokuField.MARKER_LAYER);
				break;
    		case 4:	
				/*
				 * Clean up
				 */
    			for(Card card : vfy_cards) {
		    		card.flip();
		    		card.getStack().popCard();
		    		vfy_original_stacks.get(card).pushCard(card);
		    		card.setLocation(vfy_original_stacks.get(card).getLocation());
		    		card.setStack(vfy_original_stacks.get(card));
		    	}
    			vfy_original_stacks = null;
    			
    			field.remove(vfy_marker);
    			vfy_marker = null;
    			field.remove(vfy_vmarker);
    			vfy_vmarker = null;
    			repaint();
    			
    			vfy_step = 0; // no verification currently going on
    			break;
    		default:
    			// We should never end up here
    			// TODO: Throw Exception
    			System.err.println("Unknown verification step: " + step);
    			break;	
    	}
    }
}
