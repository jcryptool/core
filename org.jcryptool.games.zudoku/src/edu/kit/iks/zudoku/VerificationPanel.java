//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2014, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.games.zudoku.views.Messages;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized.
public class VerificationPanel extends JPanel implements ActionListener {
	private class SelectionButton extends JPanel {
		private JLabel marker;
		private Set<Card> cards;
		private Image image;


		// Note: Location is the center of the select button!
		public SelectionButton(Point location, JLabel marker, Set<Card> cards, Image image) {
			super();

			super.setBounds(new Rectangle(location.x - ZudokuConfig.SELECT_BUTTON_WIDTH / 2,
					                      location.y - ZudokuConfig.SELECT_BUTTON_HEIGHT / 2,
					                      ZudokuConfig.SELECT_BUTTON_WIDTH,
					                      ZudokuConfig.SELECT_BUTTON_HEIGHT));
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
					allowAbort = false;
					VerificationPanel.this.vfy_step++;
					VerificationPanel.this.vfy_cards = SelectionButton.this.cards;
					VerificationPanel.this.vfy_marker = SelectionButton.this.marker;
					if (button_challenge != null && escapeListener != null) {
						button_challenge.removeKeyListener(escapeListener);
					}
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
				int x = (ZudokuConfig.SELECT_BUTTON_WIDTH - ZudokuConfig.SELECT_BUTTON_IMAGE_WIDTH) / 2;
				int y = (ZudokuConfig.SELECT_BUTTON_HEIGHT - ZudokuConfig.SELECT_BUTTON_IMAGE_HEIGHT) / 2;
				g.drawImage(image, x, y, ZudokuConfig.SELECT_BUTTON_IMAGE_WIDTH, ZudokuConfig.SELECT_BUTTON_IMAGE_HEIGHT, this);
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
			setLocation(ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_WIDTH + ZudokuConfig.SUDOKU_LEFT_PADDING,
					    (  ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_HEIGHT + ZudokuConfig.SUDOKU_TOP_PADDING
				         + ZudokuConfig.SUDOKU_SIZE * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING) / 2);
			setSize(/* width:  */ ZudokuConfig.SUDOKU_SIZE * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING,
					/* height: */ 100);
			setOpaque(false);
		}

		public void startFadeout(int offset, int duration) {
			// set up timer
			double tmp_offset = offset / 1000.;
			double tmp_duration = duration / 1000.;
			fadeout_start = (int) (tmp_offset * ZudokuConfig.FADEOUTS_FRAMERATE);
			fadeout_frames = (int) (tmp_duration  * ZudokuConfig.FADEOUTS_FRAMERATE);
			fadeout_framecounter = 0;
			fadeout_timer = new Timer(1000 / ZudokuConfig.FADEOUTS_FRAMERATE, this);
			fadeout_timer.setInitialDelay(0);
			fadeout_timer.start();
		}
		
		public void setText(String text) {
			this.text = text;
			repaint();
		}
		
		public void cancelFadeout() {
			fadeout_framecounter = fadeout_frames;
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
	private boolean supportsTooltips;

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
	private KeyListener escapeListener;
	private boolean abort;
	private boolean allowAbort;

	public VerificationPanel(Zudoku parent, SudokuField field) {
		super();

    	this.parent = parent;

    	setLayout(new BorderLayout());

    	/*
    	 * Set up controls
    	 */
    	detectTooltipSupport();
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
    		select_column_img = ImageIO.read(getClass().getResource(ZudokuConfig.SELECT_COLUMN_BUTTON_IMAGE));
		} catch (IOException e) {
            LogUtil.logError(e);
		}

    	select_row_img = null;
    	try {
    		select_row_img = ImageIO.read(getClass().getResource(ZudokuConfig.SELECT_ROW_BUTTON_IMAGE));
		} catch (IOException e) {
            LogUtil.logError(e);
		}

    	select_block_img = null;
    	try {
    		select_block_img = ImageIO.read(getClass().getResource(ZudokuConfig.SELECT_BLOCK_BUTTON_IMAGE));
		} catch (IOException e) {
            LogUtil.logError(e);
		}
    	
    	/*
    	 * Set up Escape listener for aborting Challenge
    	 */
    	escapeListener = new KeyListener() {
		
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE && allowAbort) {
					verify(8);
					abort = true;
					if(cheat_overlay != null) {
						cheat_overlay.cancelFadeout();
					}
				}
				
			}

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent arg0) { }
		
		};
	}
		
		private JPanel createControls() {
		JPanel controls = new JPanel();
		controls.setBackground(Color.black);

		GenericRoundedButton button = null;
		List<JButton> buttons = new ArrayList<JButton>();

		// Currently completely unused if
		if(ZudokuConfig.PROOF_MODE_ACTIVE && ZudokuConfig.VERIFICATION_MODE_ACTIVE) {
			// If both modes are active, there is a welcome panel to go back to.
			// Otherwise omit this button.
			button = new GenericRoundedButton();
			button.setText("Zurück");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					((CardLayout)VerificationPanel.this.parent.getLayout()).show(VerificationPanel.this.parent, ZudokuConfig.WELCOME_CARD);
				}
			});
			buttons.add(button);
		}
		
		button = new GenericRoundedButton();
		button.setText(Messages.VP_NEW_SUDOKU);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newSudoku();
			}
		});
		if (supportsTooltips) {
			button.setToolTipText(Messages.VP_NEW_SUDOKU_TOOLTIP);
		}
		buttons.add(button);
		button_new = button;
		
		button = new GenericRoundedButton();
		button.setText(Messages.VP_CHALLENGE);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) {
					// Set the abort flag to false every time the button is pressed.
					// Else we could run into concurrency issues.
					abort = false;
					
					// Increase the verify step to start and call verify
					vfy_step++;
					verify(vfy_step);
					
					// If the abort has been set in the meantime, don't even bother starting this.
					// However the user should not be able to be quick enough - just a precaution.
					// FIXME: Shouldn't use the cheat stuff
					if (!abort) {
						cheat_overlay = new Overlay(Messages.VP_PICK_ROW_COLUMN_OR_BLOCK);
						cheat_overlay.startFadeout(1000, 500);
						field.add(cheat_overlay, SudokuField.OVERLAY_LAYER);
					}
				}
			}
		});
		if (supportsTooltips) {
			button.setToolTipText(Messages.VP_CHALLENGE_TOOLTIP);
		}
		buttons.add(button);
		button_challenge = button;

		button = new GenericRoundedButton();
		button.setText(Messages.VP_CHEAT);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) {
					cheat_counter++;
					String number;
					switch(cheat_counter) {
						case 1: number = Messages.VP_CHEAT_INFO_01; break;
						case 2: number = Messages.VP_CHEAT_INFO_02; break;
						case 3: number = Messages.VP_CHEAT_INFO_03; break;
						case 4: number = Messages.VP_CHEAT_INFO_04; break;
						case 5: number = Messages.VP_CHEAT_INFO_05; break;
						case 6: number = Messages.VP_CHEAT_INFO_06; break;
						case 7: number = Messages.VP_CHEAT_INFO_07; break;
						case 8: number = Messages.VP_CHEAT_INFO_08; break;
						case 9: number = Messages.VP_CHEAT_INFO_09; break;
						case 10: number = Messages.VP_CHEAT_INFO_10; break;
						case 11: number = Messages.VP_CHEAT_INFO_11; break;
						case 12: number = Messages.VP_CHEAT_INFO_12; break;
						default: number = "";
					}

					String text = Messages.VP_CHEAT_INFO + " " + number + ".";
					if(cheat_counter > 12) {
						text = Messages.VP_CHEAT_INFO_LIMIT;
					} else {
						field.getSudoku().cheat();
					}
					cheat_overlay = new Overlay(text);
					cheat_overlay.startFadeout(3000, 4000);
					field.add(cheat_overlay, SudokuField.OVERLAY_LAYER);
				}
			}
		});
		if (supportsTooltips) {
			button.setToolTipText(Messages.VP_CHEAT_TOOLTIP);
		}
		buttons.add(button);
		button_cheat = button;

		button = new GenericRoundedButton();
		button.setText(Messages.VP_EXPOSE_CARDS);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(vfy_step == 0 && cheat_overlay == null) { // no verification going on
					field.getSudoku().flipCards();
					ouvert = !ouvert;
					if(ouvert) {
						button_flip.setText(Messages.VP_COVER_CARDS);
						if (supportsTooltips) {
							button_flip.setToolTipText("");
						}
					} else {
						button_flip.setText(Messages.VP_EXPOSE_CARDS);
						if (supportsTooltips) {
							button_flip.setToolTipText(Messages.VP_EXPOSE_CARDS_TOOLTIP);
						}
					}
				}
			}
		});
		button_flip = button;
		if (supportsTooltips) {
			button.setToolTipText(Messages.VP_EXPOSE_CARDS_TOOLTIP);
		}
		buttons.add(button);
		
		var layout = new GridLayout(buttons.size(), 1);
		layout.setVgap(20);

		controls.setLayout(layout);
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
	    	button_challenge.addKeyListener(escapeListener);
			allowAbort = true;
	    	

	    	// Mark inactive buttons as inactive ;)
	    	button_new.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_GREY);
	    	button_cheat.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_GREY);
	    	button_flip.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_GREY);
	    	button_challenge.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_GREY);

	    	Set<Card> cards;
    		JLabel marker;
    		
    		// Create column buttons
    		for(int i = 0; i < ZudokuConfig.SUDOKU_SIZE; i++) {
    			cards = sudoku.getColumn(i);
	    		marker = sudoku.getColumnMarker(i, ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    		vfy_selection_buttons.add(
	    			new SelectionButton(
	    				// Compute position of select button
	    				new Point(ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_WIDTH + ZudokuConfig.SUDOKU_LEFT_PADDING
							      + i * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING)
							      + ZudokuConfig.STACK_WIDTH / 2,
							      ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_HEIGHT + ZudokuConfig.SUDOKU_TOP_PADDING / 2),
	    				marker,
	    				cards,
	    				select_column_img));
    		}

    		// Create row buttons
    		for(int j = 0; j < ZudokuConfig.SUDOKU_SIZE; j++) {
    			cards = sudoku.getRow(j);
	    		marker = sudoku.getRowMarker(j, ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    		vfy_selection_buttons.add(
	    			new SelectionButton(
    					// Compute center of select button
	    			    // TODO: Fix x value as soon as issue with supply stacks is solved
	    				new Point(  ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_WIDTH + ZudokuConfig.SUDOKU_LEFT_PADDING - + ZudokuConfig.SUDOKU_TOP_PADDING / 2,
							        ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_HEIGHT + ZudokuConfig.SUDOKU_TOP_PADDING
							      + j * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING)
							      + ZudokuConfig.STACK_HEIGHT / 2),
	    				marker,
	    				cards,
	    				select_row_img));
    		}
    		
    		// Create block buttons
    		// TODO positioning works correctly only for sudoku of size 4 at the moment
    		for(int k = 0; k < ZudokuConfig.SUDOKU_SIZE; k++) {
    			cards = sudoku.getBlock(k);
	    		marker = sudoku.getBlockMarker(k, ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    		int i = field.getSudoku().getBlockIndexI(k, 0);
	    		int j = field.getSudoku().getBlockIndexJ(k, 0);
	    		vfy_selection_buttons.add(
	    			new SelectionButton(
    					// Compute center of select button
	    				new Point(  ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_WIDTH + ZudokuConfig.SUDOKU_LEFT_PADDING
							      + i * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING)
							      + ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING / 2,
							        ZudokuConfig.BORDER_PADDING + ZudokuConfig.STACK_HEIGHT + ZudokuConfig.SUDOKU_TOP_PADDING
							      + j * (ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING)
							      + ZudokuConfig.STACK_HEIGHT + ZudokuConfig.STACK_PADDING / 2),
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

			vfy_overlay = new Overlay(Messages.VP_VERIFICATION_TAKE_CARDS);
			field.add(vfy_overlay, SudokuField.OVERLAY_LAYER);

			// Move cards
	    	int i = 0;
	    	assert(vfy_cards.size() <= ZudokuConfig.SUDOKU_SIZE);
	    	vfy_original_stacks = new HashMap<Card, CardStack>();
	    	for(Card card : vfy_cards) {
	    		vfy_original_stacks.put(card, card.getStack());
	    		card.getStack().popCard();
	    		verify_stacks[i].pushCard(card);
	    		card.newAnimation(verify_stacks[i].getLocation());
	    		card.setStack(verify_stacks[i]);
	    		i++;
	    	}

			vfy_timer = new Timer(ZudokuConfig.ANIMATIONS_DURATION + 1000, this);
			vfy_timer.start();
			vfy_timer.setDelay(1500); // will set the delay _after_ step 3

	    	field.repaint();
	    	break;
		case 3:
			/*
			 * Shuffle
			 */
			vfy_overlay.setText(Messages.VP_VERIFICATION_SHUFFLE_CARDS);
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
	    	String text = Messages.VP_VERIFICATION_CHECK + " ";
	    	if(sudoku.checkCards(vfy_cards)) {
	    		color = Color.green;
	    		text += Messages.VP_VERIFICATION_CHECK_OK;
	    	} else {
	    		color = Color.red;
	    		text += Messages.VP_VERIFICATION_CHECK_FAIL;
	    	}
	    	vfy_overlay.setText(text);
	    	vfy_vmarker = new JLabel();
	    	int x = verify_stacks[0].getLocation().x - Sudoku.MARKER_PADDING;
			int y = verify_stacks[0].getLocation().y - Sudoku.MARKER_PADDING;
			int width = ZudokuConfig.SUDOKU_SIZE * (ZudokuConfig.STACK_WIDTH + ZudokuConfig.STACK_PADDING) - ZudokuConfig.STACK_PADDING + 2 * Sudoku.MARKER_PADDING;
			int height = ZudokuConfig.STACK_HEIGHT + 2 * Sudoku.MARKER_PADDING;
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
			vfy_overlay.setText(Messages.VP_VERIFICATION_COVER);

			field.remove(vfy_vmarker);
			vfy_vmarker = null;
			field.repaint();
			break;
		case 6:
			/*
			 * Shuffle
			 */
			vfy_overlay.setText(Messages.VP_VERIFICATION_SHUFFLE_CARDS);
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
			vfy_timer.setDelay(ZudokuConfig.ANIMATIONS_DURATION); // will set the delay _after_ step 7
			break;
		case 7:
			/*
			 * Clean up
			 */
			vfy_overlay.setText(Messages.VP_VERIFICATION_RETURN_CARDS);
			vfy_overlay.startFadeout(Math.max(1000, ZudokuConfig.ANIMATIONS_DURATION - 1000),
					                 Math.max(2000, ZudokuConfig.ANIMATIONS_DURATION));
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
			if (vfy_timer != null) {
				vfy_timer.stop();
				vfy_timer = null;
			}
			
			if (vfy_selection_buttons != null) {
				for(SelectionButton button : vfy_selection_buttons) {
					field.remove(button);
				}
				vfy_selection_buttons = null;
			}

			// Mark other buttons as active again
	    	button_new.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    	button_cheat.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    	button_flip.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    	button_challenge.setTextColor(ZudokuConfig.KRYPTOLOGIKUM_BLUE);
	    	field.repaint();
			break;
		default:
			// We should never end up here
			// TODO: Throw Exception
			break;
		}
	}
	
	/*
	 * About this method:
	 * we had the problem that under Linux/gtk-3 a button tooltip would run into an assertion error and crash whole
	 * JCrypTool (tested under Ubuntu 20.24 / gtk unknown and Manjaro-Linux / gtk 3.24.20)
	 * 
	 * I decided to implement a simple OS detection which enables the Tooltips on MacOS/Windows and disables them
	 * on any other unix-like systems just to be sure.
	 * If you want you can look into the bug.
	 */
	private void detectTooltipSupport() {
		String os;
		try {
			os = System.getProperty("os.name");
		} catch (Exception e) {
			os = "";
		}
		os = os.toLowerCase();
		if (os.indexOf("win")  >= 0 || os.indexOf("mac") >= 0) {
			supportsTooltips = true;
		} else {
			supportsTooltips = false;
		}
    }

	
	public void newSudoku() {
		if(vfy_step == 0 && cheat_overlay == null) {
			cheat_counter = 0;
			ouvert = false;
			button_flip.setText(Messages.VP_EXPOSE_CARDS);
			field.newSudoku();
			field.getSudoku().solve();
			if(!ouvert) {
				field.getSudoku().flipCards();
			}
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
		setBackground(ZudokuConfig.BACKGROUND_COLOR);
	}
}
