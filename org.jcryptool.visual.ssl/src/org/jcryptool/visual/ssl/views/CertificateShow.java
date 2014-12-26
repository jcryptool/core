package org.jcryptool.visual.ssl.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

/**
 * Provides a Dialog with the generated certificate depending on the chosen
 * signature algorithm and public key.
 * 
 * @author Denk Gandalf
 * 
 */
// TO DO
// - Variablen f�r Text anlegen
// - V3 Extensions anlegen
public class CertificateShow extends JDialog implements ActionListener {
    private static final long serialVersionUID = 6369515539274727894L;

    /**
	 * TextAre which holds the Text of the certificate
	 */
	TextArea txtCert = new TextArea();

	/**
	 * Button for closing the dialog
	 */
	JButton btnClose = new JButton("Schlie�en");

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
	String strCertificate = "No Certificate";

	/**
	 * Defines the creating point of the dialog and the size of the created
	 * dialog.
	 */

	PublicKey pubKey;

	/**
	 * Constructor of the class, generates the dialog and adds a formated
	 * certificate to the {@link txtCert} for displaying purpose. Also adds a
	 * close button at the bottom of the dialog
	 * 
	 * @param cert
	 *            the displayed certificate
	 */
	public CertificateShow(X509Certificate cert, PublicKey pubKey) {
		this.pubKey = pubKey;

		MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.OK);
		messageBox.setMessage(setFormatCertificat(cert));
		messageBox.setText(Messages.certShowWindowName);
		messageBox.open();
	}

	/**
	 * Formats a given certificate into a String after the example given at
	 * de.wikipedia.org/wiki/X.509
	 * 
	 * @param cert
	 * @return
	 */
	public String setFormatCertificat(X509Certificate cert) {
		String[] strSig = cert.toString().split(":");
		String[] strKey = pubKey.toString().split("\n");

		// Format General
		strCertificate = "Certificate:\n" + "Data:\n" + "\tVersion: "
				+ cert.getVersion() + "\n" + "\tSerial Number: "
				+ cert.getSerialNumber() + "\n" + "\tSignature Algorithm: "
				+ cert.getSigAlgName() + "\n" + "\tIssuer: "
				+ cert.getIssuerDN() + "\n" + "\tValidity\n"
				+ "\t\tNot Befor: " + cert.getNotBefore() + "\n"
				+ "\t\tNot After: " + cert.getNotAfter() + "\n" + "\tSubject: "
				+ cert.getSubjectDN() + "\n" + "\tSubject Public Key Info:\n"
				+ "\t\tPublic Key Algorithm: " + pubKey.getAlgorithm();

		// Output Key
		if (pubKey.getAlgorithm().equals("RSA")) {
			strCertificate = strCertificate + "\t\t"
					+ formatKey(pubKey.toString().split("\n")[1].split(":")[1]);
		} else {
			strCertificate = strCertificate + "\t\t"
					+ formatKey(strKey[2]+strKey[3]);
		}

		// Format Signature
		strCertificate = strCertificate + "\n\tSignature Algorithmen: "
				+ cert.getSigAlgName() + formatKey(strSig[strSig.length - 1]);

		return strCertificate;
	}

	/**
	 * Formats a hard to read hexadecimal string into a easy to read string. New
	 * format is: 11:22:33:44:55:..:
	 * 
	 * @param key
	 *            the hard to read key
	 * @return the easy to read key
	 */
	public String formatKey(String key) {
		key = key.replace(" ", "").replace("\n", "").replace("\r", "");
		String format = "";
		int i = 0;
		for (i = 0; i < key.length(); i++) {
			if (i % 2 == 0 && i != 0) {
				format += ":";
			}
			if (i % 30 == 0) {
				format = format + "\n\t\t";
			}
			format = format + key.charAt(i);

		}
		return format;
	}

	/**
	 * Action Listener which provides Button events
	 * 
	 * @param event
	 *            the event
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		/**
		 * Closes the dialog
		 */
		if (event.getSource() == "Close")
			;
		this.dispose();
	}
}
