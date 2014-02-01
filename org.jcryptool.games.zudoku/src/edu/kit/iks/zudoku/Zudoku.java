package edu.kit.iks.zudoku;

import java.awt.Color;

import javax.swing.JPanel;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class Zudoku extends JPanel {
	public Zudoku() {
		add(new VerificationPanel(this, new SudokuField(false)));	
		setBackground(Color.BLACK);
	}
}
