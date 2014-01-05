package edu.kit.iks.zudoku;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Optionen für den Benutzer
 * 
 * - Zurück
 * - Neues Sudoku generieren (mogeln an/aus)
 * - leak / aufgedeckt spielen.
 * 
 * TODO: Karten verschieben während animationen und co nicht zulassen...
 */

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class VerificationPanel extends JPanel implements ActionListener {
	private class SelectionButton extends JPanel {
		private JLabel marker;
		private Set<Card> cards;
		private Image image;
		
		
		// Note: Location is the center of the select button!
		public SelectionButton(Point location, JLabel marker, Set<Card> cards, Image image) {
			super();
			
			super.setBounds(new Rectangle(location.x - Zudoku.SELECT_BUTTON_WIDTH / 2, 
					                      location.y - Zudoku.SELECT_BUTTON_HEIGHT / 2, 
					                      Zudoku.SELECT_BUTTON_WIDTH,
					                      Zudoku.SELECT_BUTTON_HEIGHT));
			this.marker = marker;
			this.cards = cards;
			this.image = image;
			
			setOpaque(false);
			
			this.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(java.awt.event.MouseEvent arg0) {}
				
				@Override
				public void mousePressed(java.awt.event.MouseEvent arg0) {}
				
				@Override
				public void mouseExited(java.awt.event.MouseEvent arg0) {
					VerificationPanel.this.field.remove(SelectionButton.this.marker);
					VerificationPanel.this.field.repaint();
					
				}
				
				@Override
				public void mouseEntered(java.awt.event.MouseEvent arg0) {
					VerificationPanel.this.field.add(SelectionButton.this.marker, SudokuField.MARKER_LAYER);
					VerificationPanel.this.field.repaint();				
				}
				
				@Override
				public void mouseClicked(java.awt.event.MouseEvent arg0) {
					VerificationPanel.this.vfy_step++;
					VerificationPanel.this.vfy_cards = SelectionButton.this.cards;
					VerificationPanel.this.vfy_marker = SelectionButton.this.marker;
					VerificationPanel.this.verify(VerificationPanel.this.vfy_step);
				}
			});
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Rectangle current_bounds = getBounds();
			
			if(image == null) {
				// No image version
				setForeground(Color.green);
				Graphics2D g2d = (Graphics2D)g;
				Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, current_bounds.width, current_bounds.height);
				g2d.fill(circle);				
			} else {
				int x = (Zudoku.SELECT_BUTTON_WIDTH - Zudoku.SELECT_BUTTON_IMAGE_WIDTH) / 2;
				int y = (Zudoku.SELECT_BUTTON_HEIGHT - Zudoku.SELECT_BUTTON_IMAGE_HEIGHT) / 2;
				g.drawImage(image, x, y, Zudoku.SELECT_BUTTON_IMAGE_WIDTH, Zudoku.SELECT_BUTTON_IMAGE_HEIGHT, this);
			}
			
		}

	}
	
	private class Overlay extends JPanel implements ActionListener {
		private String text;
		
		private int fadeout_start;
		private int fadeout_frames;
		private Timer fadeout_timer;
		private int fadeout_framecounter = 0;

		public Overlay(String text) {
			super();		
			this.text = text;
			setLocation(Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING,
					    (  Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING
				         + Zudoku.SUDOKU_SIZE * (Zudoku.STACK_HEIGHT + Zudoku.STACK_PADDING) - Zudoku.STACK_PADDING) / 2);
			setSize(/* width:  */ Zudoku.SUDOKU_SIZE * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING) - Zudoku.STACK_PADDING,
					/* height: */ 100);
			setOpaque(false);	
		}
		
		public void startFadeout(int offset, int duration) {
			// set up timer
			fadeout_start = offset / 1000 * Zudoku.FADEOUTS_FRAMERATE;
			fadeout_frames = duration / 1000 * Zudoku.FADEOUTS_FRAMERATE;
			fadeout_framecounter = 0;
			fadeout_timer = new Timer(1000 / Zudoku.FADEOUTS_FRAMERATE, this);
			fadeout_timer.setInitialDelay(0);
			fadeout_timer.start();
			
		}
		
		public void setText(String text) {
			this.text = text;
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int alpha = 255;
			if(fadeout_framecounter > fadeout_start) {
				alpha = (int)(255 * (1.0 - (float)(fadeout_framecounter - fadeout_start) / (float)(fadeout_frames - fadeout_start)));
			}
			
			int border_spacing = 2;
			
			g.setColor(new Color(0,0,0,alpha));
			g.fillRoundRect(border_spacing, 
					        border_spacing, 
					        this.getSize().width - 2 * border_spacing, 
					        this.getSize().height - 2 * border_spacing, 
					        20, 20);
			
			g.setColor(new Color(255,255,255,alpha));
			g.drawRoundRect(border_spacing, 
			                border_spacing, 
			                this.getSize().width - 2 * border_spacing, 
			                this.getSize().height - 2 * border_spacing,
			                20, 20);
			
			int FONT_HEIGHT = 20;
			int FONT_WIDTH = 11; // estimate
			g.setColor(new Color(7, 123, 205, alpha)); // KRYPTOLOGIKUM_BLUE
			Font font = new Font("Comic Sans", Font.PLAIN, FONT_HEIGHT);
			g.setFont(font);
			g.drawString(text, (this.getSize().width - text.length() * FONT_WIDTH) / 2, 
					           (this.getSize().height + FONT_HEIGHT) / 2);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			fadeout_framecounter++;
			this.repaint();
			if(fadeout_framecounter >= fadeout_frames) {
				VerificationPanel.this.cheat_overlay = null; // FIXME: Overlay shouldn't contain cheat specific stuff
				VerificationPanel.this.field.remove(this);
				VerificationPanel.this.field.repaint();
				
				
				fadeout_timer.stop();
				fadeout_timer.removeActionListener(this);
				fadeout_timer = null;
			}	
		}
	}
	
	private Zudoku parent;
	private SudokuField field;
	private JPanel controls;
	
	private boolean ouvert = false;
	
	private Overlay cheat_overlay = null;
	private int cheat_counter = 0;
	
	private Image select_column_img;
	private Image select_row_img;
	private Image select_block_img;
	
	private GenericRoundedButton button_new;
	private GenericRoundedButton button_cheat;
	private GenericRoundedButton button_flip;
	private GenericRoundedButton button_challenge;
	
	/*
     * Helpers to holds state during multiple verification steps
     */
    private int vfy_step = 0;
    private Set<SelectionButton> vfy_selection_buttons;
    private JLabel vfy_marker;
    Map<Card, CardStack> vfy_original_stacks;
    private Set<Card> vfy_cards;
    private JLabel vfy_vmarker;
    private Timer vfy_timer = null;
	private Overlay vfy_overlay = null;
	
	public VerificationPanel(Zudoku parent, SudokuField field) {
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
    	field.getSudoku().solve();
		field.getSudoku().flipCards();
    	add(field, BorderLayout.CENTER);
    	
    	/*
    	 * Load images for selection buttons
    	 */
    	select_column_img = null;
    	try {
    		select_column_img = ImageIO.read(getClass().getResource(Zudoku.SELECT_COLUMN_BUTTON_IMAGE));
		} catch (IOException e) {
			System.err.println("Error: Could not load image - will use fallback drawing.");
			e.printStackTrace();			
		}
        
    	select_row_img = null;
    	try {
    		select_row_img = ImageIO.read(getClass().getResource(Zudoku.SELECT_ROW_BUTTON_IMAGE));
		} catch (IOException e) {
			System.err.println("Error: Could not load image - will use fallback drawing.");
			e.printStackTrace();			
		}
        
    	select_block_img = null;
    	try {
    		select_block_img = ImageIO.read(getClass().getResource(Zudoku.SELECT_BLOCK_BUTTON_IMAGE));
		} catch (IOException e) {
			System.err.println("Error: Could not load image - will use fallback drawing.");
			e.printStackTrace();			
		}
    }
	
	private JPanel createControls() {
		JPanel controls = new JPanel();
		
		GenericRoundedButton button = null;
		List<JButton> buttons = new ArrayList<JButton>();
		
		if(Zudoku.PROOF_MODE_ACTIVE && Zudoku.VERIFICATION_MODE_ACTIVE) {
			// If both modes are active, there is a welcome panel to go back to.
			// Otherwise omit this button.
			button = new GenericRoundedButton();
			button.setText("Zurück");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					((CardLayout)VerificationPanel.this.parent.getLayout()).show(VerificationPanel.this.parent, Zudoku.WELCOME_CARD);
				}
			});
			buttons.add(button);
		}
		
		button = new GenericRoundedButton();
		button.setText(" Neues Sudoku  ");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) {
					cheat_counter = 0;
					ouvert = false;
					button_flip.setText("Karten aufdecken    ");
					field.newSudoku();
					field.getSudoku().solve();
					if(!ouvert) {
						field.getSudoku().flipCards();
					}
				}
			}
		});
		buttons.add(button);
		button_new = button;
		
		button = new GenericRoundedButton();
		button.setText("  Schummeln  ");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) {
					cheat_counter++;
					String number = "";
					switch(cheat_counter) {
						case 1: number = "einer Stelle"; break;
						case 2: number = "zwei Stellen"; break;
						case 3: number = "drei Stellen"; break;
						case 4: number = "vier Stellen"; break;
						case 5: number = "fünf Stellen"; break;
						case 6: number = "sechs Stellen"; break;
						case 7: number = "sieben Stellen"; break;
						case 8: number = "acht Stellen"; break;
						case 9: number = "neun Stellen"; break;
						case 10: number = "zehn Stellen"; break;
						case 11: number = "elf Stellen"; break;
						case 12: number = "zwölf Stellen"; break;
					}
					
					String text = "Der Computer schummelt an " + number + ".";
					if(cheat_counter > 12) {
						text = "Mehr Schummeln geht nicht!";
					} else {
						field.getSudoku().cheat();
					}
					cheat_overlay = new Overlay(text);
					cheat_overlay.startFadeout(3000, 4000);
					field.add(cheat_overlay, SudokuField.OVERLAY_LAYER); 
				}
			}
		});
		buttons.add(button);
		button_cheat = button;
		
		button = new GenericRoundedButton();
		button.setText("Karten aufdecken    ");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) { // no verification going on
					field.getSudoku().flipCards();
					ouvert = !ouvert;
					if(ouvert) {
						button_flip.setText("Karten verdecken    ");
					} else {
						button_flip.setText("Karten aufdecken    ");
					}
				}
			}
		});
		button_flip = button;
		buttons.add(button);
		
		button = new GenericRoundedButton();
		button.setText("    Herausfordern          ");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) {
					vfy_step++;
					verify(vfy_step);
					
					// FIXME: Shouldn't use the cheat stuff
					cheat_overlay = new Overlay("Wähle eine Zeile, eine Spalte oder einen Block.");
					cheat_overlay.startFadeout(2000, 3000);
					field.add(cheat_overlay, SudokuField.OVERLAY_LAYER); 
				}
			}
		});
		buttons.add(button);
		button_challenge = button;
		
		controls.setLayout(new GridLayout(buttons.size(), 1));
		for(JButton b : buttons) {
			controls.add(b);
		}
		
		return controls; 
	}
	
	private void verify(int step) {
	   	Sudoku sudoku = field.getSudoku();
    	CardStack[] verify_stacks = field.getVerificationStacks();

		switch(step) {
		case 1:
			/*
			 * Show markers for selection
			 * TODO: Same things have to happen based on users choice
			 */
		  	vfy_cards = null;
	    	vfy_marker = null;
	    	vfy_selection_buttons = new HashSet<SelectionButton>();
	    	
	    	// Mark inactive buttons as inactive ;)
	    	button_new.setTextColor(Zudoku.KRYPTOLOGIKUM_GREY);
	    	button_cheat.setTextColor(Zudoku.KRYPTOLOGIKUM_GREY);
	    	button_flip.setTextColor(Zudoku.KRYPTOLOGIKUM_GREY);
	    	button_challenge.setTextColor(Zudoku.KRYPTOLOGIKUM_GREY);
	    	
	    	Set<Card> cards;
    		JLabel marker;
    		
    		// Create column buttons
    		for(int i = 0; i < Zudoku.SUDOKU_SIZE; i++) {
    			cards = sudoku.getColumn(i);
	    		marker = sudoku.getColumnMarker(i, Zudoku.KRYPTOLOGIKUM_BLUE);
	    		vfy_selection_buttons.add(
	    			new SelectionButton(
	    				// Compute position of select button
	    				new Point(Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING
							      + i * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING)
							      + Zudoku.STACK_WIDTH / 2,
							      Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING / 2), 
	    				marker, 
	    				cards,
	    				select_column_img));
    		}
    		
    		// Create row buttons
    		for(int j = 0; j < Zudoku.SUDOKU_SIZE; j++) {
    			cards = sudoku.getRow(j);
	    		marker = sudoku.getRowMarker(j, Zudoku.KRYPTOLOGIKUM_BLUE);
	    		vfy_selection_buttons.add(
	    			new SelectionButton(
    					// Compute center of select button
	    			    // TODO: Fix x value as soon as issue with supply stacks is solved
	    				new Point(  Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING - + Zudoku.SUDOKU_TOP_PADDING / 2,
							        Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING
							      + j * (Zudoku.STACK_HEIGHT + Zudoku.STACK_PADDING)
							      + Zudoku.STACK_HEIGHT / 2), 
	    				marker, 
	    				cards,
	    				select_row_img));
    		}
    		
    		// Create block buttons
    		// TODO positioning works correctly only for sudoku of size 4 at the moment
    		for(int k = 0; k < Zudoku.SUDOKU_SIZE; k++) {
    			cards = sudoku.getBlock(k);
	    		marker = sudoku.getBlockMarker(k, Zudoku.KRYPTOLOGIKUM_BLUE);
	    		int i = field.getSudoku().getBlockIndexI(k, 0);
	    		int j = field.getSudoku().getBlockIndexJ(k, 0);
	    		vfy_selection_buttons.add(
	    			new SelectionButton(
    					// Compute center of select button
	    				new Point(  Zudoku.BORDER_PADDING + Zudoku.STACK_WIDTH + Zudoku.SUDOKU_LEFT_PADDING
							      + i * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING)
							      + Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING / 2,
							        Zudoku.BORDER_PADDING + Zudoku.STACK_HEIGHT + Zudoku.SUDOKU_TOP_PADDING  
							      + j * (Zudoku.STACK_HEIGHT + Zudoku.STACK_PADDING)
							      + Zudoku.STACK_HEIGHT + Zudoku.STACK_PADDING / 2), 
	    				marker, 
	    				cards,
	    				select_block_img));
    		}
    		
	    	for(SelectionButton button : vfy_selection_buttons) {
	    		field.add(button, SudokuField.MOVING_CARD_LAYER); // TODO: just for debugging => move down
	    		
	    	}
	    	field.repaint();
	    	break;
		case 2:
			/*
	    	 * Move cards to verification stacks
	    	 */
			
			// Clean up selection buttons first 
			for(SelectionButton button : vfy_selection_buttons) {
	    		field.remove(button);	    		
	    	}
			vfy_selection_buttons = null;
						
			vfy_overlay = new Overlay("Karten verdeckt aufnehmen");
			field.add(vfy_overlay, SudokuField.OVERLAY_LAYER);
			
			// Move cards			
	    	int i = 0;
	    	assert(vfy_cards.size() <= Zudoku.SUDOKU_SIZE);
	    	vfy_original_stacks = new HashMap<Card, CardStack>();
	    	for(Card card : vfy_cards) {
	    		vfy_original_stacks.put(card, card.getStack());
	    		card.getStack().popCard();
	    		verify_stacks[i].pushCard(card);
	    		card.newAnimation(verify_stacks[i].getLocation());
	    		card.setStack(verify_stacks[i]);
	    		i++;    		
	    	}
	    	
			vfy_timer = new Timer(Zudoku.ANIMATIONS_DURATION + 1000, this); 
			vfy_timer.start();
			vfy_timer.setDelay(1500); // will set the delay _after_ step 3
	    	
	    	field.repaint();
	    	break;
		case 3:
			/*
			 * Shuffle
			 */
			vfy_overlay.setText("Karten verdeckt mischen");
			vfy_timer.setDelay(4000); // will set the delay _after_ step 4
			
			List<Card> cardlist = new ArrayList<Card>();
			for(i = 0; i < verify_stacks.length; i++) {
				cardlist.add(verify_stacks[i].popCard());
			}
			for(i = 0; i < verify_stacks.length; i++) {
				int j = (int)Math.floor(Math.random() * cardlist.size());
				Card card = cardlist.get(j);
				cardlist.remove(j);
				
				card.setLocation(verify_stacks[i].getLocation());
				verify_stacks[i].pushCard(card);
				card.setStack(verify_stacks[i]);
			}		
			break;
		case 4:
	    	/*
	    	 * Verify 
	    	 */
			vfy_timer.setDelay(1500); // will set the delay _after_ step 5
			
	    	Color color;
	    	if(!ouvert) {
		    	for(Card card : vfy_cards) {
		    		card.flip();
		    	}
	    	}
	    	String text = "Aufdecken und prüfen. ";
	    	if(sudoku.checkCards(vfy_cards)) {
	    		color = Color.green;
	    		text += "Passt alles!";
	    	} else {
	    		color = Color.red;
	    		text += "Passt nicht. Erwischt!";
	    	}
	    	vfy_overlay.setText(text);
	    	vfy_vmarker = new JLabel();
	    	int x = verify_stacks[0].getLocation().x - Sudoku.MARKER_PADDING;
			int y = verify_stacks[0].getLocation().y - Sudoku.MARKER_PADDING;
			int width = Zudoku.SUDOKU_SIZE * (Zudoku.STACK_WIDTH + Zudoku.STACK_PADDING) - Zudoku.STACK_PADDING + 2 * Sudoku.MARKER_PADDING;
			int height = Zudoku.STACK_HEIGHT + 2 * Sudoku.MARKER_PADDING; 
			vfy_vmarker.setBounds(x, y, width, height);
			vfy_vmarker.setBorder(BorderFactory.createLineBorder(color, Sudoku.MARKER_THICKNESS));
			field.add(vfy_vmarker, SudokuField.MARKER_LAYER);
			break;
		case 5:
			if(!ouvert) {
		    	for(Card card : vfy_cards) {
		    		card.flip();
		    	}
	    	}
			vfy_overlay.setText("Karten wieder verdecken.");
			
			field.remove(vfy_vmarker);
			vfy_vmarker = null;
			field.repaint();
			break;
		case 6:
			/*
			 * Shuffle
			 */
			vfy_overlay.setText("Verdeckte Karten mischen.");
			cardlist = new ArrayList<Card>();
			for(i = 0; i < verify_stacks.length; i++) {
				cardlist.add(verify_stacks[i].popCard());
			}
			for(i = 0; i < verify_stacks.length; i++) {
				int j = (int)Math.floor(Math.random() * cardlist.size());
				Card card = cardlist.get(j);
				cardlist.remove(j);

				verify_stacks[i].pushCard(card);
				card.setLocation(verify_stacks[i].getLocation());
				card.setStack(verify_stacks[i]);
			}
			vfy_timer.setDelay(Zudoku.ANIMATIONS_DURATION); // will set the delay _after_ step 7
			break;
		case 7:	
			/*
			 * Clean up
			 */
			vfy_overlay.setText("Karten zurücklegen.");
			vfy_overlay.startFadeout(Math.max(1000, Zudoku.ANIMATIONS_DURATION - 1000), 
					                 Math.max(2000, Zudoku.ANIMATIONS_DURATION));			
			vfy_overlay = null;
			
			for(Card card : vfy_cards) {		
	    		card.getStack().popCard();
	    		vfy_original_stacks.get(card).pushCard(card);
	    		card.newAnimation(vfy_original_stacks.get(card).getLocation());
	    		card.setStack(vfy_original_stacks.get(card));
	    	}
			vfy_original_stacks = null;
			
			field.remove(vfy_marker);
			vfy_marker = null;
			field.repaint();
			break;
		case 8:
			vfy_step = 0; // no verification currently going on
			vfy_timer.stop();
			vfy_timer = null;
			
			// Mark other buttons as active again
	    	button_new.setTextColor(Zudoku.KRYPTOLOGIKUM_BLUE);
	    	button_cheat.setTextColor(Zudoku.KRYPTOLOGIKUM_BLUE);
	    	button_flip.setTextColor(Zudoku.KRYPTOLOGIKUM_BLUE);
	    	button_challenge.setTextColor(Zudoku.KRYPTOLOGIKUM_BLUE);
			
			break;			
		default:
			// We should never end up here
			// TODO: Throw Exception
			System.err.println("Unknown verification step: " + step);
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(vfy_timer != null) {
			verify(++vfy_step);
		}		
	}
	
    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Zudoku.BACKGROUND_COLOR);
	}
}
