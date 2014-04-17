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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jcryptool.core.logging.utils.LogUtil;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized.
public class Card extends JPanel {
	private class CardDNDListener implements MouseListener, MouseMotionListener {
    	private boolean dragging = false;
    	private Point initial_card_position;
    	private Point last_mouse_position;

    	private Point getAbsoluteMousePosition(MouseEvent e) {
    		Point absolute_position = Card.this.getLocation();
    		absolute_position.x += e.getX();
    		absolute_position.y += e.getY();
    		return absolute_position;
    	}

		@Override
		public void mousePressed(MouseEvent e) {
			assert(!dragging);
			this.dragging = true;
			this.initial_card_position = Card.this.getLocation();
			this.last_mouse_position = Card.this.getLocation();
			this.last_mouse_position.x += e.getX();
			this.last_mouse_position.y += e.getY();
			Card.this.parent.moveToFront(Card.this);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(dragging) {
				dragging = false;

				/*
				 *  Try to place card at a new position in the parent's context.
				 */
				CardStack target_stack = parent.placeCard(Card.this, getAbsoluteMousePosition(e));
				if(target_stack != null) {
					/*
					 * Remove card from its current stack.
					 */
					Card popped = Card.this.stack.popCard();
					assert(popped.equals(Card.this));

					/*
					 * Set the new stack.
					 */
					Card.this.stack = target_stack;
					Card.this.parent.repaint();
				} else {
					/*
					 * Placing did not work. Move card back to original position.
					 */
					Card.this.setLocation(this.initial_card_position);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(dragging) {
				Point current_mouse_position = getAbsoluteMousePosition(e);
				Card.this.moveCard(current_mouse_position.x - last_mouse_position.x,
	                               current_mouse_position.y - last_mouse_position.y);
				this.last_mouse_position = current_mouse_position;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
    }

	private class CardAnimation implements ActionListener {
		// animation lasts Zudoku.ANIMATIONS_DURATION milliseconds
		private final int FRAMES = (int)((float)ZudokuConfig.ANIMATIONS_DURATION / 1000.0 * ZudokuConfig.ANIMATIONS_FRAMERATE);
		private Point from;
		private Point to;
		private int framecounter;
		private Timer timer;

		public CardAnimation(Point from, Point to) {
			super();
			this.from = from;
			this.to = to;
			this.framecounter = 0;

			timer = new Timer(1000 / ZudokuConfig.ANIMATIONS_FRAMERATE, this);
			timer.setInitialDelay(0);
			timer.start();

			Card.this.parent.setLayer(Card.this, SudokuField.MOVING_CARD_LAYER);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			framecounter++;
			float s = (float)framecounter / (float)FRAMES;
			Point trans = new Point((int)Math.ceil((to.x - from.x) * s) , (int)Math.ceil((to.y - from.y) * s));
			Card.this.setLocation(from.x + trans.x, from.y + trans.y);
			Card.this.repaint();
			if(framecounter >= FRAMES) {
				timer.removeActionListener(this);
				timer.stop();
				timer = null;
				Card.this.parent.setLayer(Card.this, SudokuField.CARD_LAYER);
			}
		}
	}

	/*
	 * Class Card
	 */
	private SudokuField parent;
	private CardStack stack;
	private Font front_font;
	private Image front_image;
	private Image back_image;

	private int number;
	private boolean fixed;
	private boolean active;
	private boolean covert = false; /* true if card is face down */
	private CardDNDListener dnd_listener = null;


	public Card(SudokuField parent, CardStack stack, int number) {
		this(parent, stack, number, false);
	}

	public Card(SudokuField parent, CardStack stack, int number, boolean fixed) {
		super();

		assert(parent != null);
		this.parent = parent;

		assert(stack != null);
		this.stack = stack;

		setBounds(new Rectangle(stack.getLocation().x, stack.getLocation().y,
				                ZudokuConfig.CARD_WIDTH, ZudokuConfig.CARD_HEIGHT));

		assert(number > 0);
		this.number = number;

		this.fixed = fixed;
		this.active = true; // Card may be the target of user actions

		setOpaque(false);

		front_font = new Font("Comic Sans", Font.PLAIN, ZudokuConfig.CARD_FONT_HEIGHT);
		this.front_image = null;
        try {
			this.front_image = ImageIO.read(getClass().getResource(ZudokuConfig.CARD_FRONT_IMAGE));
		} catch (IOException e) {
			LogUtil.logError(e);
		}

        this.back_image = null;
        try {
			this.back_image = ImageIO.read(getClass().getResource(ZudokuConfig.CARD_BACK_IMAGE));
		} catch (IOException e) {
            LogUtil.logError(e);
		}

        if(this.active && !this.fixed) {
        	// card can be moved by the user
        	assert(dnd_listener == null);
			dnd_listener = new CardDNDListener();
			this.addMouseListener(dnd_listener);
			this.addMouseMotionListener(dnd_listener);
        }
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		repaint();
	}

	public CardStack getStack() {
		return stack;
	}

	public void setStack(CardStack stack) {
		this.stack = stack;
	}

	public void moveCard(int dx, int dy) {
		Point current_location =  getLocation();
		setLocation(current_location.x + dx, current_location.y + dy);
	}

	public void flip() {
		covert = !covert;
		repaint();
	}

	public boolean isFixed() {
		return fixed;
	}

	public void newAnimation(Point to) {
		new CardAnimation(this.getLocation(), to);
	}

	public void setActive(boolean active) {
		if(active != this.active) {
			if(!fixed) {
				if(active) {
					assert(dnd_listener == null);
					dnd_listener = new CardDNDListener();
					this.addMouseListener(dnd_listener);
					this.addMouseMotionListener(dnd_listener);
				} else {
					assert(dnd_listener != null);
					this.removeMouseListener(dnd_listener);
					this.removeMouseMotionListener(dnd_listener);
					dnd_listener = null;
				}
			}
		}
		this.active = active;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle current_bounds = getBounds();

		int extra_margin = 3;
		int image_x = extra_margin;
		int image_y = extra_margin;
		int image_width = current_bounds.width - 2 * extra_margin;
		int image_height = current_bounds.height - 2 * extra_margin;
		if(covert && !fixed) {
			// draw back of the card
	        g.drawImage(back_image, image_x, image_y, image_width, image_height, this);
		} else {
			// draw front of the card
			if(fixed) {
				Color old_color = g.getColor();
				g.setColor(ZudokuConfig.CARD_FIXED_BORDER_COLOR);
				g.fillRect(0, 0, current_bounds.width, current_bounds.height);
				g.setColor(old_color);
			}

			int FONT_HEIGHT = 20;
			g.drawImage(front_image, image_x, image_y, image_width, image_height, this);
			g.setColor(ZudokuConfig.KRYPTOLOGIKUM_BLUE);
			g.setFont(front_font);
			g.drawString(Integer.toString(number), image_width / 2 - FONT_HEIGHT / 4 + 5, image_height / 2 + FONT_HEIGHT / 2);
		}
	}
}