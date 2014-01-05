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
	private final Font font = new Font("Comic Sans", Font.PLAIN, 20);
	private Color text_color = Zudoku.KRYPTOLOGIKUM_BLUE;
	
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
	 
	 Graphics2D g2d = (Graphics2D)g;
	 g2d.setColor(Color.BLACK);
	 g2d.fillRoundRect(0,0,getWidth(),getHeight(),18,18);

	 g2d.setColor(Color.WHITE);
	 g2d.drawRoundRect(0,0,getWidth()-1,
	    getHeight()-1,20,20);

	 // Finding size of text to compute the center position.
	 FontRenderContext frc = 
	    new FontRenderContext(null, false, false);
	 Rectangle2D r = getFont().getStringBounds(getText(), frc);

	 float xMargin = (float)(getWidth()-r.getWidth())/2 - 20; // FIXME: remove -20
	 float yMargin = (float)(getHeight()-getFont().getSize())/2;

	 // Draw the text
	 g2d.setColor(text_color);
	 g2d.setFont(font);
	 g2d.drawString(getText(), xMargin, 
	    (float)getFont().getSize() + yMargin);
	}
}