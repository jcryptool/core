package org.jcryptool.visual.ssl.views;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
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

/**
 * This class represents the <b>Server Certificate Step</b> in the SSL/TLS
 * Handshake. It can send a:
 * <ul>
 * <li>Server Certificate
 * <li>Server Key Exchange
 * <li>Server Certificate Request
 * <li>Server Hello Done
 * </ul>
 * <p>
 * When created it calculates a Exchange Key, and a default Certificate for the
 * server.
 * </p>
 * It provides the ability to activate and deactivate all buttons from the step.
 * Furthermore it checks if all the given Parameters are a valid input.
 * <p>
 * When the step is finished the returns the chosen Parameters in a Message()
 * Object to the using object.
 * 
 * @author Denk Gandalf
 * 
 */
public class ServerCertificateComposite extends Composite implements
		ProtocolStep {
	private SslView sslView;
	private boolean infoText = false;
	private Button rdbYes;
	private Button rdbNo;
	private Button btnShow;
	private Button btnInfo;
	private Button btnNextStep;

	/**
	 * Holds the calculated exchange key
	 */
	private KeyPair exchKey;

	/**
	 * The signature algorithm for the certificate
	 */
	private String strSignature;

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
	 * Creates a object of {@link Crypto}
	 */
	private Crypto c;

	/**
	 * If a certificate request is sent or not
	 */
	private boolean blnCertificateRequest = false;

	/**
	 * Generated default certificate of the Server.
	 */
	private X509Certificate certServer;

	/**
	 * Text of the Information Box.
	 */
	private String strText = "";

	private Group grpServerCertificate;
	private Label lblServerKeyExchange;
	private Label lblCertificateRequest;
	private Label lblServerHelloDone;
	private Label lblCertificate;

	/**
	 * Creates all GUI objects and positions them. Also gives functions to all
	 * the buttons.
	 * 
	 * @param parent
	 *            parent of the frame
	 * @param style
	 */
	public ServerCertificateComposite(final Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		grpServerCertificate = new Group(this, SWT.NONE);
		grpServerCertificate.setBounds(10, 0, 326, 175);
		grpServerCertificate
				.setText(Messages.ServerCertificateCompositeServerCertificate);

		lblServerKeyExchange = new Label(grpServerCertificate, SWT.NONE);
		lblServerKeyExchange.setBounds(10, 55, 140, 15);
		lblServerKeyExchange
				.setText(Messages.ServerCertificateCompositeLblServerKeyExchange);

		lblCertificateRequest = new Label(grpServerCertificate, SWT.NONE);
		lblCertificateRequest.setBounds(10, 85, 173, 15);
		lblCertificateRequest
				.setText(Messages.ServerCertificateCompositeLblCertificateRequest);

		lblServerHelloDone = new Label(grpServerCertificate, SWT.NONE);
		lblServerHelloDone.setBounds(10, 115, 100, 15);
		lblServerHelloDone
				.setText(Messages.ServerCertificateCompositeLblServerHelloDone);

		rdbYes = new Button(grpServerCertificate, SWT.RADIO);
		rdbYes.setBounds(196, 85, 50, 15);
		rdbYes.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				blnCertificateRequest = true;
			}
		});
		rdbYes.setText(Messages.ServerCertificateCompositeRdbYes);

		rdbNo = new Button(grpServerCertificate, SWT.RADIO);
		rdbNo.setBounds(256, 85, 60, 15);
		rdbNo.setSelection(true);
		rdbNo.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				blnCertificateRequest = false;
			}
		});
		rdbNo.setText(Messages.ServerCertificateCompositeRdbNo);

		btnInfo = new Button(grpServerCertificate, SWT.NONE);
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
		btnInfo.setText(Messages.ServerCertificateCompositeBtnInfo);

		btnNextStep = new Button(grpServerCertificate, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setBounds(176, 140, 140, 25);
		btnNextStep.setText(Messages.ServerCertificateCompositeBtnNextStep);

		lblCertificate = new Label(grpServerCertificate, SWT.NONE);
		lblCertificate.setBounds(10, 25, 160, 15);
		lblCertificate
				.setText(Messages.ServerCertificateCompositeLblCertificate);

		btnShow = new Button(grpServerCertificate, SWT.NONE);
		btnShow.setBounds(241, 20, 75, 25);
		btnShow.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				try {
					CertificateShow cShow = new CertificateShow(certServer, exchKey.getPublic());
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnShow.setText(Messages.ServerCertificateCompositeBtnShow);
	}

	/**
	 * Starts the ServerCertificate step by calculating the exchange Key and the
	 * needed certificate
	 */
	public void startStep() {
		c = new Crypto();

		strText = Messages.ServerCertificateCompositeInitationText
				+ Message.getServerHelloCipherSuite()
				+ Messages.ServerCertificateCompositeCertificateText;

		// Server Key Exchange
		if (Message.getServerHelloKeyExchange().contentEquals("RSA")
				|| Message.getServerHelloKeyExchange().contentEquals("DH_RSA")
				|| Message.getServerHelloKeyExchange().contentEquals("DH_DSS")) {
			// No ServerKeyExchange is sent
			lblServerKeyExchange.setEnabled(false);
		}
		exchKey = calculateKeyExchange();

		// Server Certificate
		try {
			calculateCertificate(exchKey, Message.getServerHelloHash());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Server Certificate Request
		strText = strText + Messages.ServerCertificateCompositeRequestText;
		refreshInformations();
	}

	/**
	 * Refresh the Information TextArea with the needed text
	 */
	public void refreshInformations() {
		if (infoText) {
			strText = sslView.getStxInformationText();
			sslView.setStxInformationText(Messages.ServerCertificateInformationText);
		} else {
			sslView.setStxInformationText(strText);
		}
	}

	/**
	 * Enables to use the controls of the Server Certificate step
	 */
	public void enableControls() {
		rdbNo.setEnabled(true);
		rdbYes.setEnabled(true);
		btnInfo.setEnabled(true);
		btnShow.setEnabled(true);
		btnNextStep.setEnabled(true);
		refreshInformations();
	}

	/**
	 * Disables the use of the controls from the Server Certificate step
	 */
	public void disableControls() {
		rdbNo.setEnabled(false);
		rdbYes.setEnabled(false);
		btnInfo.setEnabled(false);
		btnShow.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
	}

	/**
	 * Checks if the given parameters are valid. Sets all the needed parameters
	 * for the next step.
	 * 
	 * @return
	 */
	public boolean checkParameters() {
		Message.setServerCertificateServerCertificateRequest(blnCertificateRequest);
		Message.setServerCertificateServerCertificate(certServer);
		Message.setServerCertificateHash(Message.getServerHelloHash());
		Message.setServerCertificateSignature(strSignature);
		Message.setServerCertificateServerKeyExchange(exchKey);
		Message.setServerCertificateServerHelloDone(Message
				.getServerHelloKeyExchange());
		infoText = false;
		return true;
	}

	/**
	 * Calculates the need exchange Key depending on the given parameters. When
	 * DH is used the server also starts calculating its premaster secret with
	 * its own private key.
	 * 
	 * @return the calculated exchange key
	 */
	private KeyPair calculateKeyExchange() {
		KeyPair exchKey = null;
		try {
			if (Message.getServerHelloKeyExchange().equals("RSA")) {
				Message.setKeyPairGenerator(c.generateGenerator("RSA",
						RSA_KEY_LENGTH));
				exchKey = c.generateExchangeKey(Message.getKeyPairGenerator());
			} else {
				Message.setKeyPairGenerator(c.generateGenerator(
						"DiffieHellman", DH_KEY_LENGTH));
				exchKey = c.generateExchangeKey(Message.getKeyPairGenerator());

				KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DH",
						"BC");
				serverKeyAgree.init(exchKey.getPrivate());
				Message.setServerKeyAgreement(serverKeyAgree);
				Message.setServerCertificateServerKeyExchange(exchKey);
				strText = strText
						+ Messages.ServerCertificateCompositeKeyExchangeText;
			}
			strText = strText + exchKey.getPublic();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exchKey;
	}

	/**
	 * Calculates which KeyExchange Parameters are used and which size the keys
	 * must have. Also calls the functions for calculating a key pair and
	 * generating the default certificate for the given parameters with the
	 * needed Keys and Hash
	 * 
	 * @param exchange
	 *            the key exchange algorithm which is used
	 * @param hash
	 *            the has which is used for the certificate
	 * 
	 * @throws Exception
	 *             if the exchange/hash algorithm does not exist
	 */
	private void calculateCertificate(KeyPair exchange, String hash)
			throws Exception {
		KeyPair sigKey = exchange;

		if (exchange.getPublic().getAlgorithm().contentEquals("RSA")) {
			strSignature = "RSAEncryption";

		} else {
			if (Message.getServerHelloKeyExchange().contains("RSA")) {
				sigKey = c.generateExchangeKey(c.generateGenerator("RSA",
						RSA_KEY_LENGTH));
				strSignature = "RSAEncryption";
			} else {
				sigKey = c.generateExchangeKey(c.generateGenerator("DSA",
						DSA_KEY_LENGTH));
				strSignature = "DSA";
			}
		}

		certServer = c.generateX509(exchange, sigKey, hash, strSignature);
	}

	public boolean isBlnCertificateRequest() {
		return blnCertificateRequest;
	}

	/**
	 * Sets everything from this step to zero.
	 */
	public void resetStep() {
		exchKey = null;
		strSignature = null;
		certServer = null;
		blnCertificateRequest = false;
		rdbNo.setSelection(true);
		rdbYes.setSelection(false);
		lblServerKeyExchange.setEnabled(true);
		c = null;
		infoText=false;
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
		refreshInformations();
	}
}
