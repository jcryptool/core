package org.jcryptool.visual.ssl.views;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;

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
	 * Length of the Diffie Hellman key.
	 */
	private final static int DH_KEY_LENGTH = 512;

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

		btnShow = new Button(grpClientCertificate, SWT.NONE);
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					CertificateShow cShow = new CertificateShow(certClient,exchangeKey.getPublic());
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnShow.setBounds(241, 20, 75, 25);
		btnShow.setText(Messages.ClientCertificateCompositeBtnShow);

		btnInfo = new Button(grpClientCertificate, SWT.NONE);
		btnInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInfo.getText().equals(Messages.btnInformationToggleParams)){
					btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInfo.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();
			}
		});
		btnInfo.setBounds(70, 140, 100, 25);
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);

		btnNextStep = new Button(grpClientCertificate, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setBounds(176, 140, 140, 25);
		btnNextStep.setText(Messages.ClientCertificateCompositeBtnNextStep);
	}

	/**
	 * Starts the ClientCertificate step, calculates a certificate if need and a 
	 * exchange key or uses the public key from the server to encrypt the premaster 
	 * secret.
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
		}

		// Client Certificate verify
		if (!Message.getServerCertificateServerCertificateRequest()
				|| (Message.getServerHelloKeyExchange().equals("DH_RSA") || Message
						.getServerHelloKeyExchange().equals("DH_DSS"))) {
			lblCertificateVerify.setEnabled(false);
		} else {
			strText = strText + Messages.ClientCertificateCompositeVerifyText;
		}

		refreshInformations();
	}

	/**
	 * Refresh the Information text area with the needed text
	 */
	public void refreshInformations() {
		if (infoText) {
			strText = sslView.getStxInformationText();
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
	 * Calculates the exchange Key for the Client by the given parameters of 
	 * the server certificate. Also generates the premaster secret for a RSA key
	 * or calculates one for DH.
	 * @param servCert the server certificate
	 */
	public void calculateKeyExchange(X509Certificate servCert) {
		if (Message.getServerHelloKeyExchange().equals("RSA")) {
			try {
				PublicKey pubKey = Message
						.getServerCertificateServerCertificate().getPublicKey();

				secret = bytArrayToHex(new SecureRandom().generateSeed(48));
				
				Message.setClientCertificatePremasterEncrypted(c.encryptCBC(pubKey, secret));
				strText = strText
						+ Messages.ClientCertificateCompositeKeyExchangeRSAText
						+ pubKey + Messages.ClientCertificateCompositeRSASecret
						+ secret
						+ Messages.ClientCertificateCompositeRSAEncrypt
						+ Message.getClientCertificatePremasterEncrypted();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
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

				secret = bytArrayToHex(Message.getClientKeyAgreement()
						.generateSecret());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Checks if the given Parameters are valid.
	 * 
	 * @return
	 */
	public boolean checkParameters() {
		Message.setClientCertificateServerKeyExchange(exchangeKey);
		return true;
	}
	
	private String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	@Override
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
