package org.jcryptool.visual.ssl.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Provides a Dialog with the generated certificate depending
 * on the chosen signature algorithm and public key.
 * @author Denk Gandalf
 * 
 */
//TO DO
// - Variablen für Text anlegen
// - V3 Extensions anlegen
public class CertificateShow extends JDialog implements ActionListener{
	/**
	 * TextAre which holds the Text of the certificate
	 */
	TextArea txtCert = new TextArea();
	
	/**
	 * Button for closing the dialog
	 */
	JButton btnClose = new JButton("Schließen");
	
	/**
	 * Layout of the Dialog
	 */
	BorderLayout layBorder = new BorderLayout();
	
	/**
	 * Panel for Buttons
	 */
	JPanel panBtn = new JPanel(new FlowLayout());
	
	/**
	 * Value of no Certificate is generated
	 */
	String strCertificate="No Certificate";
	
	/**
	 * Defines the creating point of the dialog and
	 * the size of the created dialog.
	 */
	private static int DIALOG_X = 200;
	private static int DIALOG_Y = 50;
	private static int DIALOG_WIDTH = 500;
	private static int DIALOG_HEIGHT = 650;
	
	/**
	 * Constructor of the class, generates the dialog and
	 * adds a formated certificate to the {@link txtCert} 
	 * for displaying purpose.
	 * Also adds a close button at the bottom of the dialog
	 * 
	 * @param cert
	 * 			the displayed certificate
	 */
	public CertificateShow(X509Certificate cert) 
	{
		panBtn.add(btnClose);
		
		this.setLayout(layBorder);
		
		this.add(txtCert,BorderLayout.CENTER);
		this.add(panBtn,BorderLayout.SOUTH);
		
		txtCert.setEditable(false);
		
		txtCert.setText(setFormatCertificat(cert));
		
		this.setBounds(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
		this.setVisible(true);
		
		btnClose.addActionListener(this);
	}
	
	/**
	 * Formats a given certificate into a String after the example 
	 * given at de.wikipedia.org/wiki/X.509
	 * @param cert
	 * @return
	 */
	public String setFormatCertificat(X509Certificate cert){
		String[] strPublic =cert.getPublicKey().toString().split("\n");
		String[] strSig =cert.toString().split(":");
		
		//Format General
		strCertificate="Certificate:\n"
				+ "\tData:\n"
				+ "\t\tVersion: "+cert.getVersion()+"\n"
				+ "\t\tSerial Number: "+cert.getSerialNumber()+"\n"
				+ "\t\tSignature Algorithm: "+cert.getSigAlgName()+"\n"
				+ "\t\tIssuer: "+cert.getIssuerDN()+"\n"
				+ "\t\tValidity\n"
				+ "\t\t\tNot Befor: "+cert.getNotBefore()+"\n"
				+ "\t\t\tNot After: "+cert.getNotAfter()+ "\n"
				+ "\t\tSubject: "+cert.getSubjectDN()+"\n"
				+ "\t\tSubject Public Key Info:\n"
				+ "\t\t\tPublic Key Algorithm: "+cert.getPublicKey().getAlgorithm()+"\n";
		
		//Output Key
		strCertificate=strCertificate+"\t\t\t"+strPublic[0];
		
		strCertificate=strCertificate
				+ "\t\t\t"+formatKey(strSig[12].split(" ")[1]);
		
		if(cert.getVersion()==3){
			//Format Extensions
			strCertificate=strCertificate
					+ "\n\t\tX509v3 extensions:\n";
		}
				
		//Format Signature
		strCertificate=strCertificate
				+ "\tSignature Algorithmen: "+cert.getSigAlgName()
				+formatKey(strSig[strSig.length-1]);
		
		
		return strCertificate;
	}
	
	/**
	 * Formats a hard to read hexadecimal string into a easy to 
	 * read string.
	 * New format is:
	 * 11:22:33:44:55:..:
	 * @param key	the hard to read key
	 * @return
	 * 		the easy to read key
	 */
	public String formatKey(String key){
		key = key.replace(" ", "").replace("\n", "").replace("\r","");
		String format="";
		int i = 0;
		for(i=0;i<key.length();i++){
			if(i%30==0){
				format=format+":\n\t\t\t\t";
			}
			if(i%2==0&&i!=0&&!(i%30==0)){
				format+=":";
			}
			format=format+key.charAt(i);
				
		}
		return format;
	}

	/**
	 * Action Listener which provides Button events
	 * @param event		the event
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		
		/**
		 * Closes the dialog
		 */
		if(event.getSource()=="Close");
		this.dispose();	
	}
}
