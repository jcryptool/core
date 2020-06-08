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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized.
public class GenericRoundedButton extends JButton {
	private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
	private Color text_color = ZudokuConfig.KRYPTOLOGIKUM_BLUE;
	private final int multilineOffset = 12; // Manual set offset, else it does not look good

	public GenericRoundedButton(String text) {
		super(text);
		setFont(font);
	}

	public GenericRoundedButton() {
		super();
	}

	public void setTextColor(Color c) {
		text_color = c;
		repaint();
	}

	public void paint(Graphics g) {
		setBackground(getParent().getBackground());
		setBorderPainted(false);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

		g2d.setColor(Color.WHITE);
		g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

		// Draw the text
		g2d.setColor(text_color);
		g2d.setFont(font);

		var frc = new FontRenderContext(null, false, false);
		// Split if multiline
		var text = getText().split("\n");

		if (text.length == 1) {
			// Finding size of text to compute the center position.

			Rectangle2D r = font.getStringBounds(text[0], frc);

			float xMargin = (float) (getWidth() - r.getWidth()) / 2;
			float yMargin = (float) (getHeight() - getFont().getSize()) / 2;
			g2d.drawString(getText(), xMargin, (float) getFont().getSize() + yMargin);
		} else {
			// Step 1: Calculate y offset by adding up all text heights + multiLineOffset as
			// spacing
			float yMargin, xMargin, textHeight = 0;
			var textBoxes = new Rectangle2D[text.length];
			for (int i = 0; i < text.length; ++i) {
				Rectangle2D r = font.getStringBounds(text[i], frc);
				textHeight += r.getHeight() + multilineOffset;
				textBoxes[i] = r;
			}
			yMargin = (this.getHeight() - textHeight) / 2;

			// Step 2: Calculate the respective x offset (each line centered) and draw
			for (int i = 0; i < text.length; ++i) {
				xMargin = (float) (getWidth() - textBoxes[i].getWidth()) / 2;
				g2d.drawString(text[i], xMargin, yMargin += (float) font.getSize() + multilineOffset);
			}
		}
	}
}