//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.ssl.views;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.crypto.KeyAgreement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.ssl.protocol.Crypto;
import org.jcryptool.visual.ssl.protocol.Message;
import org.jcryptool.visual.ssl.protocol.ProtocolStep;

import codec.Hex;

/**
 * This class represents the Client Certificate Step in the SSL/TLS Handshake.
 * It can send a:
 * <ul>
 * <li>Client Certificate
 * <li>Client Key Exchange
 * <li>Certificate Verify
 * </ul>
 * <p>
 * It provides the ability to activate and deactivate all buttons from the step.
 * Furthermore it checks if all the given Parameters are a valid input.
 * <p>
 * When the step is finished the returns the chosen Parameters in a Message()
 * Object to the using object.
 * 
 * @author Denk Gandalf
 * 
 */
public class ClientCertificateComposite extends Composite implements
		ProtocolStep {
	private SslView sslView;
	private boolean infoText = false;
	public Button btnShow;
	private Button btnInfo;
	private Button btnNextStep;
	private Group grpClientCertificate;
	private Label lblCertificate;
	private Label lblClientKeyExchange;
	private Label lblCertificateVerify;

	/**
	 * Length of the calculated RSA key.
	 */
	private final static int RSA_KEY_LENGTH = 1024;

	/**
	 * Length of the calculated DSA key
	 */
	private final static int DSA_KEY_LENGTH = 640;

	/**
	 * Handshake type for the client certificate message
	 */
	private final static String CLIENT_CERTIFICATE_MESSAGE = "0f";

	/**
	 * Handshake type for the client key exchange message
	 */
	private final static String CLIENT_KEY_EXCHANGE_MESSAGE = "10";

	/**
	 * Handshake type for the client verify message
	 */
	private final static String CLIENT_VERIFY_MESSAGE = "11";

	/**
	 * Hex value for Handshake
	 */
	private static final String HANDSHAKE_PROTOCOL = "16";

	/**
	 * Text of the Information Box.
	 */
	private String strText = "";

	/**
	 * Generated default certificate of the Client.
	 */
	private X509Certificate certClient;

	/**
	 * Creates a object of {@link Crypto}
	 */
	private Crypto c;

	/**
	 * The calculated exchange key form the client
	 */
	private KeyPair exchangeKey;

	/**
	 * The key used to sign the certificate
	 */
	private KeyPair sigKey;

	/**
	 * The premaster secret of the client
	 */
	private String secret;

	/**
	 * Creates all GUI objects and positions them. Also gives functions to all
	 * the buttons.
	 * 
	 * @param parent
	 *            parent of the frame
	 * @param style
	 */
	public ClientCertificateComposite(Composite parent, int style,
			final SslView sslView) {

		super(parent, style);
		this.sslView = sslView;

		grpClientCertificate = new Group(this, SWT.NONE);
		grpClientCertificate.setBounds(0, 0, 326, 175);
		grpClientCertificate
				.setText(Messages.ClientCertificateCompositeGrpClientCertificate);

		lblCertificate = new Label(grpClientCertificate, SWT.NONE);
		lblCertificate.setBounds(10, 25, 160, 15);
		lblCertificate
				.setText(Messages.ClientCertificateCompositeLblCertifcate);

		lblClientKeyExchange = new Label(grpClientCertificate, SWT.NONE);
		lblClientKeyExchange.setBounds(10, 55, 110, 15);
		lblClientKeyExchange
				.setText(Messages.ClientCertificateCompositeLblClientKeyExchange);

		lblCertificateVerify = new Label(grpClientCertificate, SWT.NONE);
		lblCertificateVerify.setBounds(10, 85, 90, 15);
		lblCertificateVerify
				.setText(Messages.ClientCertificateCompositeLblCertificateVerify);

		// Creates a new object from CertificateShow to display the client
		// certificate
		btnShow = new Button(grpClientCertificate, SWT.NONE);
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					CertificateShow cShow = new CertificateShow(certClient,
							exchangeKey.getPublic());
				} catch (IllegalStateException e1) {
				}
			}
		});
		btnShow.setBounds(241, 20, 75, 25);
		btnShow.setText(Messages.ClientCertificateCompositeBtnShow);

		// The information button which toggles to the word "Parameter" if
		// pressed once
		btnInfo = new Button(grpClientCertificate, SWT.NONE);
		btnInfo.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if (btnInfo.getText().equals(
						Messages.btnInformationToggleParams)) {
					btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
				} else {
					btnInfo.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();
			}
		});
		btnInfo.setBounds(70, 140, 100, 25);
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);

		// Ends this step and moves on to the next step
		btnNextStep = new Button(grpClientCertificate, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setBounds(176, 140, 140, 25);
		btnNextStep.setText(Messages.ClientCertificateCompositeBtnNextStep);
	}

	/**
	 * Starts the ClientCertificate step, calculates a certificate if need and a
	 * exchange key or uses the public key from the server to encrypt the
	 * premaster secret.
	 */
	public void startStep() {
		c = new Crypto();
		strText = Messages.ClientCertificateCompositeInitationText;

		// Client Key Exchange
		calculateKeyExchange(Message.getServerCertificateServerCertificate());

		// Client Certificate
		if (!Message.getServerCertificateServerCertificateRequest()) {
			btnShow.setEnabled(false);
			lblCertificate.setEnabled(false);
		} else {
			createCertificate();
			doClientCertificate();
		}

		// Client Certificate verify
		if (!Message.getServerCertificateServerCertificateRequest()
				|| (Message.getServerHelloKeyExchange().equals("DH_RSA") || Message
						.getServerHelloKeyExchange().equals("DH_DSS"))) {
			lblCertificateVerify.setEnabled(false);
		} else {
			strText = strText + Messages.ClientCertificateCompositeVerifyText;
			doClientVerify();
		}

		refreshInformations();
	}

	/**
	 * Refresh the Information text area with the needed text
	 */
	public void refreshInformations() {
		if (infoText) {
			sslView.setStxInformationText(Messages.ClientCertificateInformationText);
		} else {
			sslView.setStxInformationText(strText);
		}
	}

	/**
	 * Enables to use the controls of the Server Certificate step
	 */
	public void enableControls() {
		btnInfo.setEnabled(true);
		btnShow.setEnabled(true);
		btnNextStep.setEnabled(true);
		refreshInformations();
	}

	/**
	 * Disables the use of the controls from the Server Certificate step
	 */
	public void disableControls() {
		btnInfo.setEnabled(false);
		btnShow.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
	}

	/**
	 * Calculates the exchange Key for the Client by the given parameters of the
	 * server certificate. Also generates the premaster secret for a RSA key or
	 * calculates one for DH.
	 * 
	 * @param servCert
	 *            the server certificate
	 */
	public void calculateKeyExchange(X509Certificate servCert) {
		if (Message.getServerHelloKeyExchange().equals("RSA")) {
			// Calculates a RSA key
			try {
				PublicKey pubKey = Message
						.getServerCertificateServerCertificate().getPublicKey();

				secret = Hex.encode((new SecureRandom().generateSeed(48)));

				Message.setClientCertificatePremasterEncrypted(c.encryptCBC(
						pubKey, secret));
				strText = strText
						+ Messages.ClientCertificateCompositeKeyExchangeRSAText
						+ pubKey + Messages.ClientCertificateCompositeRSASecret
						+ secret
						+ Messages.ClientCertificateCompositeRSAEncrypt
						+ Message.getClientCertificatePremasterEncrypted();
			} catch (Exception e) {
				 
			}
		} else {
			// Calculates a DH Key
			try {
				exchangeKey = c.generateExchangeKey(Message
						.getKeyPairGenerator());
				KeyAgreement clientKeyAgree = KeyAgreement.getInstance("DH",
						"BC");
				clientKeyAgree.init(exchangeKey.getPrivate());
				clientKeyAgree.doPhase(Message
						.getServerCertificateServerKeyExchange().getPublic(),
						true);
				Message.setClientKeyAgreement(clientKeyAgree);
				Message.setClientCertificateServerKeyExchange(exchangeKey);

				secret = Hex.encode(Message.getClientKeyAgreement()
						.generateSecret());
			} catch (Exception e) {
				 
			}
			strText = strText + exchangeKey.getPublic()
					+ Messages.ClientCertificateCompositeDHSecret + secret;
		}
	}

	/**
	 * Creates a certificate for the Client
	 */
	public void createCertificate() {
		strText = strText
				+ Messages.ClientCertificateCompositeCertificateRequested;

		try {
			if (Message.getServerHelloKeyExchange().equals("RSA")) {
				exchangeKey = c.generateExchangeKey(c.generateGenerator("RSA",
						RSA_KEY_LENGTH));
			}

			if (Message.getServerHelloKeyExchange().contains("RSA")) {
				sigKey = c.generateExchangeKey(c.generateGenerator("RSA",
						RSA_KEY_LENGTH));
			} else if (Message.getServerHelloKeyExchange().contains("DSS")) {
				sigKey = c.generateExchangeKey(c.generateGenerator("DSA",
						DSA_KEY_LENGTH));
			}
			certClient = c.generateX509(exchangeKey, sigKey,
					Message.getServerCertificateHash(),
					Message.getServerCertificateSignature());
		} catch (Exception e1) {
		}
	}

	/**
	 * Checks if the given Parameters are valid.
	 * 
	 * @return
	 */
	public boolean checkParameters() {
		// Client Key Exchange
		try {
			String ClientKeyExchange = Hex.encode(exchangeKey.getPublic()
					.getEncoded());
			ClientKeyExchange = getNumber(ClientKeyExchange.length() / 2)
					+ ClientKeyExchange;
			String keyHash = c.generateHash(Message.getServerHelloHash(),
					ClientKeyExchange);
			ClientKeyExchange = ClientKeyExchange
					+ getNumber(keyHash.length() / 2) + keyHash;
			ClientKeyExchange = CLIENT_KEY_EXCHANGE_MESSAGE
					+ getNumber(ClientKeyExchange.length() / 2)
					+ ClientKeyExchange;
			ClientKeyExchange = HANDSHAKE_PROTOCOL
					+ Message.getServerHelloVersion()
					+ getNumber(ClientKeyExchange.length() / 2)
					+ ClientKeyExchange;
			Message.setMessageClientKeyExchange(ClientKeyExchange);
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (NullPointerException e) {
		}
		Message.setClientCertificateServerKeyExchange(exchangeKey);
		return true;
	}

	/**
	 * Calculates the hex message for the client verify.
	 */
	private void doClientVerify() {
		String ClientVerify = "01";
		ClientVerify = HANDSHAKE_PROTOCOL + Message.getServerHelloVersion()
				+ CLIENT_VERIFY_MESSAGE + getNumber(ClientVerify.length() / 2)
				+ ClientVerify;
		Message.setMessageClientVerify(ClientVerify);
	}

	/**
	 * Calculates the hex message for the client certificate.
	 */
	private void doClientCertificate() {
		String ClientCertificate;
		try {
			ClientCertificate = Hex.encode(certClient.getEncoded());
			ClientCertificate = CLIENT_CERTIFICATE_MESSAGE
					+ getNumber(ClientCertificate.length() / 2)
					+ ClientCertificate;
			ClientCertificate = HANDSHAKE_PROTOCOL
					+ Message.getServerHelloVersion()
					+ getNumber(ClientCertificate.length()) + ClientCertificate;
			Message.setMessageClientCertfificate(ClientCertificate);
		} catch (CertificateEncodingException e1) {
		}
	}

	/**
	 * Formats a number to a byte looking number with leading zeros
	 * 
	 * @param number
	 *            the number to format
	 * @return the formated number
	 */
	private String getNumber(int number) {
		String strNumber = "";
		int backNumber = number;
		int s = 3, n = 0, i = 0;
		while (number > 9) {
			number = number % 10;
			n++;
		}
		for (; i < (s - n); i++) {
			strNumber = strNumber + "0";
		}
		strNumber = strNumber + Integer.toString(backNumber);
		return strNumber;
	}

	/**
	 * Resets the step
	 */
	public void resetStep() {
		lblCertificate.setEnabled(true);
		lblCertificateVerify.setEnabled(true);
		certClient = null;
		sigKey = null;
		c = null;
		infoText = false;
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
		refreshInformations();
	}

}
