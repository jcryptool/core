package org.jcryptool.analysis.bruteforce.windows;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import java.text.ParseException;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.*;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;

import org.jcryptool.analysis.bruteforce.Functions;

public class GUI extends JFrame {
	
	private static final long serialVersionUID = 1;

	private JPanel contentPane;
	private static JTextField txtEnt;
	private static JTextField txtKey;

	public static void main(String[] args) {
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KeyDef frame = new KeyDef();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
		GUI frame = new GUI();
		frame.setVisible(true);
	}

	public static void setEnt(double ent) {
		txtEnt.setText(String.valueOf(ent));
		
	}
	public static void setKey(String key) {
		txtKey.setText(key);
	}

	public GUI() {
		setTitle("Brute-Force-Analysis");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 674, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JFormattedTextField keyInput = new JFormattedTextField(createFormatter("** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - **","0123456789abcdefABCDEF*"));
		keyInput.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				keyInput.setText(keyInput.getText().replaceAll("a", "A")
				.replaceAll("b", "B")
				.replaceAll("c", "C")
				.replaceAll("d", "D")
				.replaceAll("e", "E")
				.replaceAll("f", "F"));
			}
		});
		keyInput.setFont(new Font("Consolas", Font.PLAIN, 15));
		keyInput.setToolTipText("Insert * for any Character of the key that is unknown");
		keyInput.setText("** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - ** - **");
		keyInput.setBounds(12, 182, 629, 24);
		contentPane.add(keyInput);
		
		final JTextArea txtEncrypted = new JTextArea();
		txtEncrypted.setBounds(12, 40, 632, 78);
		contentPane.add(txtEncrypted);
		
		
		txtEnt = new JTextField();
		txtEnt.setEditable(false);
		txtEnt.setBounds(399, 219, 242, 22);
		contentPane.add(txtEnt);
		txtEnt.setColumns(10);
		
		txtKey = new JTextField();
		txtKey.setEditable(false);
		txtKey.setFont(new Font("Consolas", Font.PLAIN, 13));
		txtKey.setBounds(399, 245, 242, 22);
		contentPane.add(txtKey);
		txtKey.setColumns(10);
		
		JButton btnNewButton = new JButton("Execute");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String rawKey=keyInput.getText().replaceAll(" - ", "");
				char[] chars={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
				Functions.Bruteforce.run_e(chars, rawKey, txtEncrypted.getText());
			}
		});
		btnNewButton.setBounds(12, 219, 97, 48);
		contentPane.add(btnNewButton);
		
		JTextPane txtpnTest = new JTextPane();
		txtpnTest.setBackground(SystemColor.control);
		txtpnTest.setEditable(false);
		txtpnTest.setText("Please set all bytes of the key that are known in hex format.\r\nFor unknown bytes insert ' * '");
		txtpnTest.setBounds(12, 131, 497, 38);
		contentPane.add(txtpnTest);
		
		JTextPane txtpnInsertEncryptedHex = new JTextPane();
		txtpnInsertEncryptedHex.setText("Insert encrypted hex data");
		txtpnInsertEncryptedHex.setEditable(false);
		txtpnInsertEncryptedHex.setBackground(SystemColor.menu);
		txtpnInsertEncryptedHex.setBounds(12, 13, 497, 24);
		contentPane.add(txtpnInsertEncryptedHex);
		
		JLabel lblEntropie = new JLabel("Text value:");
		lblEntropie.setBounds(305, 222, 97, 16);
		contentPane.add(lblEntropie);
		
		JLabel lblKey = new JLabel("Key:");
		lblKey.setBounds(305, 246, 56, 16);
		contentPane.add(lblKey);
		
	}
	protected MaskFormatter createFormatter(String s){
		MaskFormatter formatter=null;
		try {
			formatter=new MaskFormatter(s);
		} catch (ParseException e) {
			System.out.println("formatter is bad...");
			e.printStackTrace();
		}
		return formatter;
	}
	protected MaskFormatter createFormatter(String s,String chars){
		MaskFormatter formatter=createFormatter(s);
		formatter.setValidCharacters(chars);
		return formatter;
	}
}

