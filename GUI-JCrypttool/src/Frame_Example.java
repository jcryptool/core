import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.AbstractListModel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;

public class Frame_Example extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame_Example frame = new Frame_Example();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame_Example() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 782, 451);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel pnl_user = new JPanel();
		tabbedPane.addTab("User", null, pnl_user, null);
		pnl_user.setLayout(new BorderLayout(0, 0));

		JPanel pnl_user_actions = new JPanel();
		pnl_user.add(pnl_user_actions, BorderLayout.WEST);
		pnl_user_actions.setLayout(new GridLayout(10, 0, 0, 0));

		JButton btn_create_certificate = new JButton("Create Certificate");
		pnl_user_actions.add(btn_create_certificate);

		JButton btn_show_certificate = new JButton("Show Certificate");
		pnl_user_actions.add(btn_show_certificate);

		JButton btnUseCertificates = new JButton("Sign Stuff");
		pnl_user_actions.add(btnUseCertificates);

		JButton btnRevokeCertificate = new JButton("Revoke Certificate");
		pnl_user_actions.add(btnRevokeCertificate);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		pnl_user.add(tabbedPane_1, BorderLayout.CENTER);

		JPanel pnl_create = new JPanel();
		tabbedPane_1.addTab("Create", null, pnl_create, null);
		pnl_create.setLayout(new GridLayout(12, 2, 0, 0));

		JLabel lblCN = new JLabel("Common Name");
		pnl_create.add(lblCN);

		textField = new JTextField();
		pnl_create.add(textField);
		textField.setColumns(10);

		JLabel lblOrganisation = new JLabel("Organisation");
		pnl_create.add(lblOrganisation);

		textField_1 = new JTextField();
		pnl_create.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblOrgUnit = new JLabel("Organisational Unit");
		pnl_create.add(lblOrgUnit);

		textField_2 = new JTextField();
		pnl_create.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblCity = new JLabel("City or Locality");
		pnl_create.add(lblCity);

		textField_3 = new JTextField();
		pnl_create.add(textField_3);
		textField_3.setColumns(10);

		JLabel lblState = new JLabel("State or Province");
		pnl_create.add(lblState);

		textField_4 = new JTextField();
		pnl_create.add(textField_4);
		textField_4.setColumns(10);

		JLabel lblCountry = new JLabel("Country");
		pnl_create.add(lblCountry);

		textField_5 = new JTextField();
		pnl_create.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblEmail = new JLabel("E-Mail");
		pnl_create.add(lblEmail);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		pnl_create.add(textField_6);

		JLabel lblPersonalausweis = new JLabel("Proof of Identity");
		pnl_create.add(lblPersonalausweis);

		JButton btnSelectFile = new JButton("Select File");
		pnl_create.add(btnSelectFile);
		
		JLabel lblPublicKey = new JLabel("Public Key");
		pnl_create.add(lblPublicKey);
		
		JButton btnOpen = new JButton("Select existing public key...");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		pnl_create.add(btnOpen);

		JLabel label = new JLabel("");
		pnl_create.add(label);
		
		JButton btnGenerateKeyPair = new JButton("Generate new key pair");
		pnl_create.add(btnGenerateKeyPair);
		
		JButton button_1 = new JButton("Send Certificate signing request");
		pnl_create.add(button_1);

		JPanel pnl_show = new JPanel();
		tabbedPane_1.addTab("Show", null, pnl_show, null);
		pnl_show.setLayout(new BorderLayout(0, 0));

		JList list_2 = new JList();
		list_2.setModel(new AbstractListModel() {
			String[] values = new String[] { "cert1", "cert2" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		pnl_show.add(list_2, BorderLayout.WEST);

		JPanel panel_4 = new JPanel();
		pnl_show.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new GridLayout(10, 2, 0, 0));

		JLabel lblEntry = new JLabel("entry 1");
		panel_4.add(lblEntry);

		JLabel lblValue = new JLabel("value1");
		panel_4.add(lblValue);

		JLabel lblEntry_1 = new JLabel("entry 2");
		panel_4.add(lblEntry_1);

		JLabel lblValue_1 = new JLabel("value 2");
		panel_4.add(lblValue_1);

		JLabel lblEntry_2 = new JLabel("entry 3");
		panel_4.add(lblEntry_2);

		JLabel lblValue_2 = new JLabel("value 3");
		panel_4.add(lblValue_2);

		JButton btnExportCert = new JButton("export cert");
		panel_4.add(btnExportCert);

		JPanel pnl_sign = new JPanel();
		tabbedPane_1.addTab("Sign", null, pnl_sign, null);
		pnl_sign.setLayout(new GridLayout(10, 2, 0, 0));

		JButton btnSelectFileTo = new JButton("Select File to sign");
		pnl_sign.add(btnSelectFileTo);

		JLabel lblNewLabel = new JLabel("OR");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		pnl_sign.add(lblNewLabel);

		JTextArea textArea = new JTextArea();
		pnl_sign.add(textArea);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] { "cert 1",
				"cert 2", "cert 3", "cert 4" }));
		pnl_sign.add(comboBox_1);

		JButton btnSignWithSelected_1 = new JButton(
				"sign with private key from selected cert");
		pnl_sign.add(btnSignWithSelected_1);

		JPanel pnl_revoke = new JPanel();
		tabbedPane_1.addTab("Revoke", null, pnl_revoke, null);
		pnl_revoke.setLayout(new GridLayout(10, 1, 0, 0));

		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] { "cert 1",
				"cert 2", "cert 3", "cert 4" }));
		pnl_revoke.add(comboBox_2);

		JButton btnNewButton = new JButton("revoke certificate");
		pnl_revoke.add(btnNewButton);

		JPanel pnl_registrar = new JPanel();
		tabbedPane.addTab("Registrar", null, pnl_registrar, null);
		pnl_registrar.setLayout(new BorderLayout(0, 0));

		Vector mdl = new Vector();
		mdl.addElement("CSR #1");
		mdl.addElement("CSR #2");
		mdl.addElement("CSR #3");
		mdl.addElement("CSR #4");
		mdl.addElement("CSR #5");
		JList list = new JList(mdl);
		pnl_registrar.add(list, BorderLayout.WEST);

		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		pnl_registrar.add(tabbedPane_2, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane_2.addTab("Selected CSR", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(10, 1, 0, 0));

		JLabel lblEintragAusCsr = new JLabel("Entry from CSR");
		panel_2.add(lblEintragAusCsr);

		JLabel lblEintragAusCsr_1 = new JLabel("Entry from CSR");
		panel_2.add(lblEintragAusCsr_1);

		JLabel label_1 = new JLabel("...");
		panel_2.add(label_1);

		JLabel label_2 = new JLabel("...");
		panel_2.add(label_2);

		JButton btnVerifyIdentity = new JButton("Verify identity");
		panel_2.add(btnVerifyIdentity);

		JButton btnWhoIsThis = new JButton("Drop CSR");
		panel_2.add(btnWhoIsThis);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 6));
		panel_1.setForeground(Color.GREEN);
		panel.add(panel_1);

		JLabel lblAusweisbildHier = new JLabel("");
		lblAusweisbildHier.setIcon(new ImageIcon("ausweis.jpg"));
		panel_1.add(lblAusweisbildHier);

		JPanel pnl_certificate = new JPanel();
		tabbedPane.addTab("Certificate Authority", null, pnl_certificate, null);

		Vector mdl2 = new Vector();
		mdl2.addElement("Verified CSR #1");
		mdl2.addElement("Verified CSR #2");
		mdl2.addElement("Verified CSR #3");
		mdl2.addElement("Revoke Request #1");
		mdl2.addElement("Revoke Request #2");
		pnl_certificate.setLayout(new BorderLayout(0, 0));
												JList list_1 = new JList(mdl2);
												pnl_certificate.add(list_1, BorderLayout.WEST);
												
														JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.TOP);
														pnl_certificate.add(tabbedPane_3);
														
																JPanel panel_3 = new JPanel();
																tabbedPane_3.addTab("Selected Verified CSR", null, panel_3, null);
																panel_3.setLayout(new GridLayout(10, 0, 0, 0));
																
																		JComboBox comboBox = new JComboBox();
																		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Root Certificate 1", "Root Certificate 2", "Root Certificate 3", "..."}));
																		panel_3.add(comboBox);
																		
																				JButton btnSignWithSelected = new JButton(
																						"Sign with selected Root Certificate");
																				btnSignWithSelected.addActionListener(new ActionListener() {
																					public void actionPerformed(ActionEvent arg0) {
																					}
																				});
																				panel_3.add(btnSignWithSelected);
																				
																						JButton btnRejectCsrBecause = new JButton(
																								"Reject CSR because of mistrust");
																						panel_3.add(btnRejectCsrBecause);
																						
																						JPanel panel_7 = new JPanel();
																						tabbedPane_3.addTab("Selected Revoke Request", null, panel_7, null);
																						panel_7.setLayout(new GridLayout(10, 0, 0, 0));
																						
																						JComboBox comboBox_3 = new JComboBox();
																						comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"Root Certificate #1", "Root Certificate #2", "Root Certificate #3", "Root Certificate #4"}));
																						panel_7.add(comboBox_3);
																						
																						JButton button_3 = new JButton("Add to revocation list");
																						panel_7.add(button_3);
																						
																						JButton button_4 = new JButton("Reject revoke request");
																						panel_7.add(button_4);

		JPanel pnl_2ndUser = new JPanel();
		tabbedPane.addTab("2nd User", null, pnl_2ndUser, null);
		pnl_2ndUser.setLayout(new BorderLayout(0, 0));

		JList list_3 = new JList();
		list_3.setModel(new AbstractListModel() {
			String[] values = new String[] { "signed stuff 1",
					"signed stuff 2", "signed stuff 3", "signed stuff 4" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		pnl_2ndUser.add(list_3, BorderLayout.WEST);
		
		JTabbedPane tabbedPane_4 = new JTabbedPane(JTabbedPane.TOP);
		pnl_2ndUser.add(tabbedPane_4, BorderLayout.CENTER);

		JPanel panel_5 = new JPanel();
		tabbedPane_4.addTab("Message", null, panel_5, null);
		panel_5.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JTextArea txtrMessage = new JTextArea();
		txtrMessage.setPreferredSize(new Dimension(600, 80));
		txtrMessage.setText("message");
		panel_5.add(txtrMessage);
				
						JTextArea txtrSignatur = new JTextArea();
						txtrSignatur.setPreferredSize(new Dimension(600, 80));
						txtrSignatur.setText("signatur");
						panel_5.add(txtrSignatur);
				
				JButton button_2 = new JButton("Check Signature");
				panel_5.add(button_2);
				button_2.setMargin(new Insets(0, 0, 0, 0));
				button_2.setPreferredSize(new Dimension(180, 40));
				
				JCheckBox chckbxNewCheckBox = new JCheckBox("Check revocation status");
				panel_5.add(chckbxNewCheckBox);
		
				JTextArea lblExplain = new JTextArea(
						"Wenn der User einmal die Signatur verifiziert hat, kann er sowohl die Signatur als auch die \nNachricht zu verändern. \r\nDadurch kann er sehen, welche Auswirkung das Manipulieren der Nachricht hat.");
				lblExplain.setWrapStyleWord(true);
				lblExplain.setPreferredSize(new Dimension(600, 60));
				panel_5.add(lblExplain);
		
		JPanel panel_6 = new JPanel();
		tabbedPane_4.addTab("File", null, panel_6, null);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JTextArea txtrFileName = new JTextArea();
		txtrFileName.setPreferredSize(new Dimension(600, 25));
		panel_6.add(txtrFileName);
		txtrFileName.setText("File name");
		
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setPreferredSize(new Dimension(600, 70));
		textArea_2.setText("signatur");
		panel_6.add(textArea_2);
		
		JButton button = new JButton("Check Signature");
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setPreferredSize(new Dimension(200, 40));
		panel_6.add(button);
		
		JCheckBox checkBox = new JCheckBox("Check revocation status");
		panel_6.add(checkBox);
	}
}
