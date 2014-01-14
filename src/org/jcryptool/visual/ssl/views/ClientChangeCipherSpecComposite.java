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

public class ClientChangeCipherSpecComposite extends Composite implements
		ProtocolStep {
	private Button btnInformationen;
	private boolean infoText = false;
	private String strText;
	private SslView sslView;
	private Group grpClientChangeCipher;
	private Label lblChangeCipherSpec;
	
	/**
	 * Content Typ of the ChangeCipherSpec Message
	 */
	private static String CHANGE_CIPHER_MESSAGE = "14";
	
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
		
		if(Message.getServerHelloVersion() != "0303"){ //TLS1.0 or TLS1.1
			masterSecret = Message.getMasterSecret();
			
			//create encryption parameters
			if(Message.getServerHelloHash() == "MD5") {
				//16 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 32);
				newIndex = 64;
			}else { //SHA1
				//20 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 40);
				newIndex = 80;
			}
			
			if(Message.getServerHelloCipher() == "RC4_128") {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher() == "AES_128") {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher() == "AES_256") {
				//32 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 64);
				newIndex = newIndex + 128;
			}else if(Message.getServerHelloCipher() == "DES") {
				//7 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 14);
				newIndex = newIndex + 28;
			}else if(Message.getServerHelloCipher() == "3DES") {
				//21 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 42);
				newIndex = newIndex + 84;
			}else { //no Encryption
				clientKey = null;
			}
			
			if(Message.getServerHelloVersion() != "0302") { //TLS1.0
				if(Message.getServerHelloCipherMode() == "CBC") {
					//16 Byte IV
					clientIV = masterSecret.substring(newIndex, newIndex + 32);
				}else { //no IVs necessary
					clientIV = Messages.ServerChangeCipherSpecNoIV;
				}
			}else { //TLS1.1
				if(Message.getServerHelloCipherMode() == "CBC") {
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
			if(Message.getServerHelloHash() == "MD5") {
				//16 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 32);
				newIndex = 64;
			}else if(Message.getServerHelloHash() == "SHA1"){
				//20 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 40);
				newIndex = 80;
			}else if(Message.getServerHelloHash() == "SHA256") {
				//32 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 64);
				newIndex = 128;
			}else { //SHA384
				//48 Byte MAC key
				clientMACsecret = masterSecret.substring(0, 96);
				newIndex = 192;
			}
			
			if(Message.getServerHelloCipher() == "RC4_128") {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher() == "AES_128") {
				//16 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 32);
				newIndex = newIndex + 64;
			}else if(Message.getServerHelloCipher() == "AES_256") {
				//32 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 64);
				newIndex = newIndex + 128;
			}else if(Message.getServerHelloCipher() == "DES") {
				//7 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 14);
				newIndex = newIndex + 28;
			}else if(Message.getServerHelloCipher() == "3DES") {
				//21 Byte key
				clientKey = masterSecret.substring(newIndex, newIndex + 42);
				newIndex = newIndex + 84;
			}else { //no Encryption
				clientKey = null;
			}
			
			if(Message.getServerHelloCipherMode() == "GCM") {
				if(Message.getServerHelloCipher() == "AES_128") {
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
