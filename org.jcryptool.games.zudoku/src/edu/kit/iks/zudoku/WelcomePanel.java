package edu.kit.iks.zudoku;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class WelcomePanel extends JPanel  {
	private Zudoku parent;
	
	public WelcomePanel(Zudoku parent) {
		super();
		
		this.parent = parent;
		
		setLayout(new GridLayout(1, 2));
		
		JButton button_proof = new JButton();
		button_proof.setText("Ich will Beweiser sein!");
		button_proof.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((CardLayout)WelcomePanel.this.parent.getLayout()).show(WelcomePanel.this.parent, Zudoku.PROOF_CARD);
			}
		});
		add(button_proof);
		
		JButton button_verify = new JButton();
		button_verify.setText("Ich will Verifizierer sein!");
		button_verify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((CardLayout)WelcomePanel.this.parent.getLayout()).show(WelcomePanel.this.parent, Zudoku.VERIFICATION_CARD);				
			}
		});
		add(button_verify);
	}
}