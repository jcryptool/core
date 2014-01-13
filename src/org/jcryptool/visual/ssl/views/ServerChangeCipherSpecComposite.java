package org.jcryptool.visual.ssl.views;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
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

public class ServerChangeCipherSpecComposite extends Composite implements
		ProtocolStep {
	private boolean infoText = false;
	private Button btnInformation;
	private Button btnNextStep;
	private String strText;
	private SslView sslView;
	private Group grpServerChangeCipher;
	private Label lblChangeCipherSpec;
	private int count = 0;

	/**
	 * The premaster secret of the server
	 */
	private String secret;
	
	/**
	 * The random number generated for the message clientHello
	 */
	private String clientRandom;
	
	/**
	 * The random number generated for the message serverHello
	 */
	private String serverRandom;
	
	/**
	 * The master secret needed for the encryption.
	 */
	private String masterSecret;
	
	/**
	 * 
	 */
	private String clientMACsecret;
	
	/**
	 * 
	 */
	private String serverMACsecret;
	
	/**
	 * 
	 */
	private String clientKey;
	
	/**
	 * 
	 */
	private String serverKey;
	
	/**
	 * 
	 */
	private String clientIV;
	
	/**
	 * 
	 */
	private String serverIV;

	/**
	 * The object which provides crypto functions
	 */
	private Crypto c = null;
	
	/**
	 * Returns the master secret.
	 * @return: master secret
	 */
	public String getMasterSecret() {
		return masterSecret;
	}

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ServerChangeCipherSpecComposite(Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		grpServerChangeCipher = new Group(this, SWT.NONE);
		grpServerChangeCipher.setBounds(0, 0, 326, 175);
		grpServerChangeCipher
				.setText(Messages.ServerChangeCipherSpecCompositeLblServerChangeCipher);

		lblChangeCipherSpec = new Label(grpServerChangeCipher, SWT.NONE);
		lblChangeCipherSpec.setBounds(10, 25, 110, 20);
		lblChangeCipherSpec
				.setText(Messages.ServerChangeCipherSpecCompositeLblServerChangeCipherSpec);

		btnInformation = new Button(grpServerChangeCipher, SWT.NONE);
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInformation.getText().equals(Messages.btnInformationToggleParams)){
					btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInformation.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();
			}
		});
		btnInformation.setLocation(70, 140);
		btnInformation.setSize(100, 25);
		btnInformation
				.setText(Messages.ServerChangeCipherSpecCompositeBtnInformation);

		btnNextStep = new Button(grpServerChangeCipher, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setBounds(176, 140, 140, 25);
		btnNextStep
				.setText(Messages.ServerChangeCipherSpecCompositeBtnNextStep);
	}

	/**
	 * Starts the ServerChangeCipherSpec step. Calculates the master secret
	 * for the Server
	 */
	public void startStep() {
		c = new Crypto();
		String seed;
		int newIndex;
		
		secret = getPremasterSecret();
		clientRandom = Message.getClientHelloRandom();
		serverRandom = Message.getServerHelloRandom();
		seed = serverRandom + clientRandom;
		
		if(Message.getServerHelloVersion() != 2){ //TLS1.0 or TLS1.1
			masterSecret = PRF(secret, "master secret", seed);
			while(masterSecret.length() < 272) {
				masterSecret = PRF(masterSecret, "key expansion", seed);
			}
			Messages.setMasterSecret(masterSecret);
			
			//create encryption parameters
			if(Message.getServerHelloHash() == "MD5") {
				//16 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 32);
				serverMACsecret = masterSecret.substring(33, 64);
				newIndex = 64;
			}else { //SHA1
				//20 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 40);
				serverMACsecret = masterSecret.substring(40, 80);
				newIndex = 80;
			}
			
			if(Message.getServerHelloCipher() == "RC4_128") {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				serverKey = masterSecret.substring(newIndex + 32, newIndex + 64);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher() == "AES_128") {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				serverKey = masterSecret.substring(newIndex + 32, newIndex + 64);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher() == "AES_256") {
				//32 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 64);
				serverKey = masterSecret.substring(newIndex + 64, newIndex + 128);
				newIndex = newIndex + 128;
			}else if(Message.getServerHelloCipher() == "DES") {
				//7 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 14);
				serverKey = masterSecret.substring(newIndex + 14, newIndex + 28);
				newIndex = newIndex + 28;
			}else if(Message.getServerHelloCipher() == "3DES") {
				//21 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 42);
				serverKey = masterSecret.substring(newIndex + 42, newIndex + 84);
				newIndex = newIndex + 84;
			}else {
				//no Encryption
				clientKey = null;
				serverKey = null;
			}
			
			if(Message.getServerHelloVersion() != 1) { //TLS1.0
				if(Message.getServerHelloCipherMode() == "CBC") {
					//16 Byte IV
					clientIV = masterSecret.substring(newIndex, newIndex + 32);
					serverIV = masterSecret.substring(newIndex + 32, newIndex + 64);
				}else {
					//no IVs necessary
					clientIV = Messages.ServerChangeCipherSpecNoIV;
					serverIV = Messages.ServerChangeCipherSpecNoIV;
				}
			}else { //TLS1.1
				if(Message.getServerHelloCipherMode() == "CBC") {
					//16 Byte IV
					SecureRandom random = new SecureRandom();
					byte[] IV = random.generateSeed(16);
					clientIV = bytArrayToHex(IV);
					serverIV = bytArrayToHex(IV);
				}else {
					//no IVs necessary
					clientIV = Messages.ServerChangeCipherSpecNoIV;
					serverIV = Messages.ServerChangeCipherSpecNoIV;
				}
			}
		}else {
			//TLS1.2
			SecureRandom random = new SecureRandom();
			byte[] rand = random.generateSeed(272);
			masterSecret = bytArrayToHex(rand);
			Messages.setMasterSecret(masterSecret);
			
			clientMACsecret = masterSecret.substring(0, 40);
			serverMACsecret = masterSecret.substring(40, 80);
			clientKey = masterSecret.substring(80, 144);
			serverKey = masterSecret.substring(144, 208);
			clientIV = masterSecret.substring(208, 240);
			serverIV = masterSecret.substring(240, 272);
			
			//masterSecret = PRF(secret, "master secret", clientRandom + serverRandom);
		}

		strText = Messages.ServerChangeCipherSpecInitationText
				+ Messages.ServerChangeCipherSpecPreMaster
				+ secret 
				+ Messages.ServerChangeCipherSpecMasterSecret
				+ masterSecret
				+ Messages.ServerChangeCipherSpecServerMACsecret
				+serverMACsecret
				+ Messages.ServerChangeCipherSpecServerKey
				+serverKey
				+ Messages.ServerChangeCipherSpecServerIV
				+serverIV;
		refreshInformations();
	}

	/**
	 * The pseudorandom-function used in TLS1.0 and TLS1.1 to generate the master secret
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
		count++;
		
		if(Message.getServerHelloHash() == "MD5") {
			hash = P_hash(secret, seed, count, "MD5");
		}else //if(Message.getServerHelloHash() == "SHA1")
		{
			hash = P_hash(secret, seed, count, "SHA1");
		}/*else if(Message.getServerHelloHash() == "SHA256") {
			hash = P_hash(secret, seed, count, "SHA256");
		}else { //SHA384
			hash = P_hash(secret, seed, count, "SHA384");
		}*/
		
		hash_length = hash.length();
		S1 = hash.substring(0, (hash_length/2)-1);
		S2 = hash.substring(hash_length/2);
		
		b1 = P_hash(S1, string + seed, count, "MD5").getBytes();
		b2 = P_hash(S2, string + seed, count, "SHA1").getBytes();
		b_length = b1.length;
		
		byte[] b3 = new byte[b_length];
		
		for(i = 0; i < b_length; i++) {
			b3[i] = (byte) (b1[i] ^ b2[i]);
		}
		return bytArrayToHex(b3);
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

	/**
	 * The hashfunction which is used in TLS1.0 and TLS1.1 for the PRF of the master secret
	 * @param secret
	 * @param seed
	 * @param count
	 * @param Hash
	 * @return
	 */
	private String P_hash(String secret, String seed, int max, String Hash) {
		String hash = "";
		for(int i = 0; i < max; i++) {
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
	 * A hashfunction used in TLS1.0 and TLS1.1 to generate more bytes for the master secret
	 * @param i
	 * @param Hash
	 * @return
	 */
	private String A(int i, String Hash) {
		if(i == 0) {
			return clientRandom + serverRandom;
		}else {
			try {
				return c.generateHash(Hash, secret + A(i-1, Hash) + clientRandom + serverRandom);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return clientRandom + serverRandom;
		}
	}

	/**
	 * Calculates the premaster secret for the server. Decrypts the secret if
	 * RSA is used or calculates the secret form a DH key.
	 * 
	 * @return the preamaster secret
	 */
	public String getPremasterSecret() {
		try {
			if (Message.getServerHelloKeyExchange().equals("RSA")) {
				secret = c.decryptCBC(Message
						.getServerCertificateServerKeyExchange().getPrivate(),
						Message.getClientCertificatePremasterEncrypted());
			} else {
				KeyAgreement k = Message.getServerKeyAgreement();
				k.doPhase(Message.getClientCertificateServerKeyExchange()
						.getPublic(), true);
				Message.setServerKeyAgreement(k);
				secret = bytArrayToHex(Message.getServerKeyAgreement()
						.generateSecret());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secret;
	}

	public void refreshInformations() {
		if (infoText) {
			strText = sslView.getStxInformationText();
			sslView.setStxInformationText(Messages.ServerChangeCipherSpecInformationText);
		} else {
			sslView.setStxInformationText(strText);
		}
	}

	public void enableControls() {
		btnInformation.setEnabled(true);
		btnNextStep.setEnabled(true);
		refreshInformations();
	}

	public void disableControls() {
		btnInformation.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
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
		secret = "";
		c = null;
		btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
