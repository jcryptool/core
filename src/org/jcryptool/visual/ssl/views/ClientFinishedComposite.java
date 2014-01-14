package org.jcryptool.visual.ssl.views;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.ssl.protocol.Crypto;
import org.jcryptool.visual.ssl.protocol.Message;
import org.jcryptool.visual.ssl.protocol.ProtocolStep;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.ui.PlatformUI;

public class ClientFinishedComposite extends Composite implements ProtocolStep {
	private Button btnInformationen;
	private boolean infoText = false;
	private String strText;
	private SslView sslView;
	private Group grpClientFinished;
	private Label lblFinished;

	/**
	 * The random number generated for the message clientHello
	 */
	private String clientRandom = Message.getClientHelloRandom();

	/**
	 * The random number generated for the message serverHello
	 */
	private String serverRandom = Message.getServerHelloRandom();

	/**
	 * The master secret needed for the encryption.
	 */
	private String masterSecret = Message.getMasterSecret();

	/**
	 * The object which provides crypto functions
	 */
	private Crypto c = null;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ClientFinishedComposite(Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		grpClientFinished = new Group(this, SWT.NONE);
		grpClientFinished.setBounds(0, 0, 326, 175);
		grpClientFinished
				.setText(Messages.ClientFinishedCompositeGrpServerFinished);

		lblFinished = new Label(grpClientFinished, SWT.NONE);
		lblFinished.setBounds(10, 25, 50, 20);
		lblFinished.setText(Messages.ClientFinishedCompositeLblFinished);

		btnInformationen = new Button(grpClientFinished, SWT.NONE);
		btnInformationen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if (btnInformationen.getText().equals(
						Messages.btnInformationToggleParams)) {
					btnInformationen
							.setText(Messages.ClientCertificateCompositeBtnInfo);
				} else {
					btnInformationen
							.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();

			}
		});
		btnInformationen.setLocation(216, 140);
		btnInformationen.setSize(100, 25);
		btnInformationen
				.setText(Messages.ClientFinishedCompositeBtnInformation);
	}

	public void startStep() {
		c = new Crypto();
		String finished = null;
		String cFinished = null;
		String hashMessages = Message.getMessageClientHello()
				+ Message.getMessageServerHello()
				+ Message.getMessageServerRequest()
				+ Message.getMessageServerCertificate()
				+ Message.getMessageServerKeyExchange()
				+ Message.getMessageServerHelloDone()
				+ Message.getMessageClientCertfificate()
				+ Message.getMessageClientKeyExchange()
				+ Message.getMessageClientVerify()
				+ Message.getMessageServerFinished();
		try {
			finished = PRF(masterSecret, "server finished",
					c.generateHash(Message.getServerHelloHash(), hashMessages));
			if(Message.getServerHelloCipherMode() == "CBC") {
				cFinished = c.encryptCBC(c.generateKey(Message.getServerHelloCipher(), Message.getClientKey().length()), finished);
			}else { //GCM
				cFinished = c.encryptGCM(c.generateKey(Message.getServerHelloCipher(), Message.getClientKey().length()), finished);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		strText = Messages.ClientFinishedInitationText + cFinished;
		refreshInformations();
	}

	/**
	 * The pseudorandom-function to generate the master secret
	 * 
	 * @param secret
	 * @param string
	 * @param seed
	 * @return
	 */
	private String PRF(String secret, String string, String seed) {
		String hash = "";
		int hash_length;
		String S1;
		String S2;
		byte[] b1;
		byte[] b2;
		int b_length;
		int i;

		if (Message.getServerHelloHash() == "MD5") {
			hash = P_hash(secret, seed, 5, "MD5");
		} else if (Message.getServerHelloHash() == "SHA1") {
			hash = P_hash(secret, seed, 4, "SHA1");
		} else if (Message.getServerHelloHash() == "SHA256") {
			hash = P_hash(secret, seed, 4, "SHA256");
		} else { // SHA384
			hash = P_hash(secret, seed, 4, "SHA384");
		}

		hash_length = hash.length();
		S1 = hash.substring(0, (hash_length / 2) - 1);
		S2 = hash.substring(hash_length / 2);

		b1 = P_hash(S1, string + seed, 5, "MD5").getBytes();
		b2 = P_hash(S2, string + seed, 4, "SHA1").getBytes();
		b_length = b1.length;

		byte[] b3 = new byte[b_length];

		for (i = 0; i < b_length; i++) {
			b3[i] = (byte) (b1[i] ^ b2[i]);
		}
		return bytArrayToHex(b3);
	}

	/**
	 * The hashfunction for the PRF of the master secret
	 * 
	 * @param secret
	 * @param seed
	 * @param count
	 * @param Hash
	 * @return
	 */
	private String P_hash(String secret, String seed, int max, String Hash) {
		String hash = "";
		for (int i = 0; i < max; i++) {
			try {
				hash = hash + c.generateHash(Hash, secret + A(i, Hash) + seed);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return hash;
	}

	/**
	 * A hashfunction to generate more bytes for the master secret
	 * 
	 * @param i
	 * @param Hash
	 * @return
	 */
	private String A(int i, String Hash) {
		if (i == 0) {
			return clientRandom + serverRandom;
		} else {
			try {
				return c.generateHash(Hash, masterSecret + A(i - 1, Hash)
						+ clientRandom + serverRandom);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return clientRandom + serverRandom;
		}
	}

	/**
	 * @param a
	 * @return
	 */
	private String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	public void refreshInformations() {
		if (infoText) {
			sslView.setStxInformationText(Messages.ClientFinishedInformationText);
		} else {
			sslView.setStxInformationText(strText);
		}
	}

	public void enableControls() {
		btnInformationen.setEnabled(true);
		refreshInformations();
	}

	public void disableControls() {
		btnInformationen.setEnabled(false);
		btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
	}

	public boolean checkParameters() {
		return true;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void resetStep() {
		infoText = false;
		btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
