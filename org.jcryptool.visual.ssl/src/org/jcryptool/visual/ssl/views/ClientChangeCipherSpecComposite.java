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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyAgreement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.ssl.protocol.Crypto;
import org.jcryptool.visual.ssl.protocol.Message;
import org.jcryptool.visual.ssl.protocol.ProtocolStep;

public class ClientChangeCipherSpecComposite extends Composite implements
		ProtocolStep {
	private Button btnInformationen;
	private boolean infoText = false;
	private String strText;
	private SslView sslView;
	private Group grpClientChangeCipher;
	private Label lblChangeCipherSpec;
	private int count = 0;

	/**
	 * Content Typ of the ChangeCipherSpec Message
	 */
	private static String CHANGE_CIPHER_MESSAGE = "14";

	/**
	 * The master secret needed for the encryption.
	 */
	private String masterSecret;

	/**
	 * The premaster secret of the server
	 */
	private String secret;

	/**
	 * The random number generated for the message clientHello
	 */
	private String clientRandom = Message.getClientHelloRandom();

	/**
	 * The random number generated for the message serverHello
	 */
	private String serverRandom = Message.getServerHelloRandom();

	/**
	 *
	 */
	private String clientMACsecret;

	/**
	 *
	 */
	private String clientKey;

	/**
	 *
	 */
	private String clientIV;

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
	public ClientChangeCipherSpecComposite(Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		grpClientChangeCipher = new Group(this, SWT.NONE);
		grpClientChangeCipher.setBounds(0, 0, 326, 175);
		grpClientChangeCipher
		.setText(Messages.ClientChangeCipherSpecCompositeLblClientChangeCipher);
		lblChangeCipherSpec = new Label(grpClientChangeCipher, SWT.NONE);
		lblChangeCipherSpec.setBounds(10, 25, 110, 20);
		lblChangeCipherSpec
		.setText(Messages.ClientChangeCipherSpecCompositeLblClientChangeCipherSpec);

		btnInformationen = new Button(grpClientChangeCipher, SWT.NONE);
		btnInformationen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInformationen.getText().equals(Messages.btnInformationToggleParams)){
					btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInformationen.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();

			}
		});
		btnInformationen.setLocation(216, 140);
		btnInformationen.setSize(100, 25);
		btnInformationen
		.setText(Messages.ClientChangeCipherSpecCompositeBtnInformation);
	}

	/**
	 * Starts the ServerChangeCipherSpec step. Calculates the master secret
	 * for the Server
	 */
	public void startStep() {
		c = new Crypto();
		SecureRandom random = new SecureRandom();
		int newIndex;
		String seed = serverRandom + clientRandom;

		secret = getPremasterSecret();

		if(!Message.getServerHelloVersion().equals("0303")){ //TLS1.0 or TLS1.1
			masterSecret = Message.getMasterSecret();

			//create encryption parameters
			if(Message.getServerHelloHash().equals("MD5")) {
				//16 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 32);
				newIndex = 64;
			}else { //SHA1
				//20 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 40);
				newIndex = 80;
			}

			if(Message.getServerHelloCipher().equals("RC4_128")) {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher().equals("AES_128")) {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher().equals("AES_256")) {
				//32 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 64);
				newIndex = newIndex + 128;
			}else if(Message.getServerHelloCipher().equals("DES")) {
				//7 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 14);
				newIndex = newIndex + 28;
			}else if(Message.getServerHelloCipher().equals("3DES")) {
				//21 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 42);
				newIndex = newIndex + 84;
			}else { //no Encryption
				clientKey = null;
			}

			if(!Message.getServerHelloVersion().equals("0302")) { //TLS1.0
				if(Message.getServerHelloCipherMode().equals("CBC")) {
					//16 Byte IV
					clientIV = masterSecret.substring(newIndex, newIndex + 32);
				}else { //no IVs necessary
					clientIV = Messages.ServerChangeCipherSpecNoIV;
				}
			}else { //TLS1.1
				if(Message.getServerHelloCipherMode().equals("CBC")) {
					//16 Byte IV
					clientIV = bytArrayToHex(random.generateSeed(16));
				}else { //no IVs necessary
					clientIV = Messages.ServerChangeCipherSpecNoIV;
				}
			}
		}else {
			//TLS1.2
			masterSecret = Message.getMasterSecret();

			//create encryption parameters
			if(Message.getServerHelloHash().equals("MD5")) {
				//16 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 32);
				newIndex = 64;
			}else if(Message.getServerHelloHash().equals("SHA1")){
				//20 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 40);
				newIndex = 80;
			}else if(Message.getServerHelloHash().equals("SHA256")) {
				//32 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 64);
				newIndex = 128;
			}else { //SHA384
				//48 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 96);
				newIndex = 192;
			}

			if(Message.getServerHelloCipher().equals("RC4_128")) {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher().equals("AES_128")) {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher().equals("AES_256")) {
				//32 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 64);
				newIndex = newIndex + 128;
			}else if(Message.getServerHelloCipher().equals("DES")) {
				//7 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 14);
				newIndex = newIndex + 28;
			}else if(Message.getServerHelloCipher().equals("3DES")) {
				//21 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 42);
				newIndex = newIndex + 84;
			}else { //no Encryption
				clientKey = null;
			}

			if(Message.getServerHelloCipherMode().equals("GCM")) {
				if(Message.getServerHelloCipher().equals("AES_128")) {
					//16 Byte IV
					clientIV = bytArrayToHex(random.generateSeed(16));
				}else { //AES-256
					//32 Byte IV
					clientIV = bytArrayToHex(random.generateSeed(32));
				}
			}else { //no IVs necessary
				clientIV = Messages.ServerChangeCipherSpecNoIV;
			}
		}

		if(Message.getServerHelloCipher().equals("RC4_128")) {
			//16 Byte key
			if(clientKey.length() < 128) {
				clientKey = PRF(clientKey, "key expansion", seed);
			}
		}else if(Message.getServerHelloCipher().equals("AES_128")) {
			//16 Byte key
			if(clientKey.length() < 128) {
				clientKey = PRF(clientKey, "key expansion", seed);
			}
		}else if(Message.getServerHelloCipher().equals("AES_256")) {
			//32 Byte key
			if(clientKey.length() < 256) {
				clientKey = PRF(clientKey, "key expansion", seed);
			}
		}else if(Message.getServerHelloCipher().equals("DES")) {
			//7 Byte key
			if(clientKey.length() < 56) {
				clientKey = PRF(clientKey, "key expansion", seed);
			}
		}else if(Message.getServerHelloCipher().equals("3DES")) {
			//21 Byte key
			if(clientKey.length() < 192) {
				clientKey = PRF(clientKey, "key expansion", seed);
			}
		}else { //no Encryption
			clientKey = null;
		}

		Message.setClientKey(clientKey);

		strText = Messages.ClientChangeCipherSpecInitationText
				+ Messages.ServerChangeCipherSpecMasterSecret
				+ masterSecret
				+ Messages.ClientChangeCipherSpecClientMACsecret
				+ clientMACsecret
				+ Messages.ClientChangeCipherSpecClientKey
				+ clientKey
				+ Messages.ClientChangeCipherSpecClientIV
				+ clientIV;
		refreshInformations();
	}

	/**
	 * The pseudorandom-function to generate the master secret
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

		if(Message.getServerHelloHash().equals("MD5")) {
			hash = P_hash(secret, seed, count, "MD5");
		}else if(Message.getServerHelloHash().equals("SHA1"))
		{
			hash = P_hash(secret, seed, count, "SHA1");
		}else if(Message.getServerHelloHash().equals("SHA256")) {
			hash = P_hash(secret, seed, count, "SHA256");
		}else { //SHA384
			hash = P_hash(secret, seed, count, "SHA384");
		}

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
	 * The hashfunction for the PRF of the master secret
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
	            LogUtil.logError(e);
			} catch (UnsupportedEncodingException e) {
	            LogUtil.logError(e);
			}
		}
		return hash;
	}

	/**
	 * A hashfunction to generate more bytes for the master secret
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
	            LogUtil.logError(e);
			} catch (UnsupportedEncodingException e) {
	            LogUtil.logError(e);
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
            LogUtil.logError(e);
		}
		return secret;
	}

	public void refreshInformations() {
		if (infoText) {
			sslView.setStxInformationText(Messages.ClientChangeCipherSpecInformationText);
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
		doClientChangeCipherSpec();
		infoText = false;
		refreshInformations();
		return true;
	}

	/**
	 * Calculates the hex message for the ClientChangeCipherSpec message. The message looks like:
	 * <ul>
	 * <li>Content Typ:
	 * <li>Version:
	 * <li>Length:
	 * <li>ChangeCipherSpecMessage
	 * </ul>
	 */
	private void doClientChangeCipherSpec(){
		String ServerChangeCipherSpec = "01";
		ServerChangeCipherSpec = CHANGE_CIPHER_MESSAGE
				+ Message.getServerHelloVersion()
				+ "01"
				+ ServerChangeCipherSpec;
		Message.setMessageServerChangeCipherSpec(ServerChangeCipherSpec);
	}

	@Override
	public void resetStep() {
		infoText=false;
		btnInformationen.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
