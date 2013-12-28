package org.jcryptool.visual.ssl.views;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidParameterSpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.ssl.protocol.Message;
import org.jcryptool.visual.ssl.protocol.ProtocolStep;

import codec.Hex;

/**
 * This class represents the Server Certificate Step in the SSL/TLS Handshake.
 * It provides the ability to activate and deactivate all buttons from the step.
 * Furthermore it checks if all the given Parameters are a valid input. When the
 * step is finished the returns the chosen Parameters in a Message() Object to
 * the using object.
 * 
 * @author Denk Gandalf
 * 
 */
public class ServerCertificateComposite extends Composite implements
		ProtocolStep {
	private SslView sslView;
	private boolean infoText=false;
	private Button rdbYes;
	private Button rdbNo;
	private Button btnShow;
	private Button btnInfo;
	private Button btnNextStep;

	private KeyPair keyPair;
	private Key key;
	private String strExchange;

	private String strCipher;
	private String strHash;
	private String strCipherSuit;
	private String strSignature;

	private final static int RSA_KEY_LENGTH = 1024;
	private final static int DSA_KEY_LENGTH = 56;
	private final static int TRIPLEDES_KEY_LENGTH = 168;
	private final static int DH_KEY_LENGTH = 512;

	private final static String BOUNCY_CASTLE_PROVIDER = "BC";

	private int cipLength = 512;
	private int exchangeLength = 1024;

	private boolean blnCertificateRequest = false;
	private X509Certificate certServer;

	private static SecureRandom secure = new SecureRandom();

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ServerCertificateComposite(final Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;
		strExchange = Message.getServerHelloKeyExchange();
		strHash = Message.getServerHelloHash();
		strCipher = Message.getServerHelloCipher();
		strCipherSuit = Message.getServerHelloCipherSuite();

		Group grpServerCertificate = new Group(this, SWT.NONE);
		grpServerCertificate
				.setText(Messages.ServerCertificateCompositeServerCertificate);
		grpServerCertificate.setBounds(10, 0, 326, 175);

		Label lblServerKeyExchange = new Label(grpServerCertificate, SWT.NONE);
		lblServerKeyExchange.setBounds(10, 55, 140, 15);
		lblServerKeyExchange
				.setText(Messages.ServerCertificateCompositeLblServerKeyExchange);
		
		Label lblCertificateRequest = new Label(grpServerCertificate, SWT.NONE);
		lblCertificateRequest.setBounds(10, 85, 173, 15);
		lblCertificateRequest
				.setText(Messages.ServerCertificateCompositeLblCertificateRequest);

		Label lblServerHelloDone = new Label(grpServerCertificate, SWT.NONE);
		lblServerHelloDone.setBounds(10, 115, 100, 15);
		lblServerHelloDone
				.setText(Messages.ServerCertificateCompositeLblServerHelloDone);

		rdbYes = new Button(grpServerCertificate, SWT.RADIO);
		rdbYes.setBounds(196,85, 50, 15);
		rdbYes.setText(Messages.ServerCertificateCompositeRdbYes);
		rdbYes.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				blnCertificateRequest = true;
			}
		});

		rdbNo = new Button(grpServerCertificate, SWT.RADIO);
		rdbNo.setBounds(256, 85, 60, 15);
		rdbNo.setText(Messages.ServerCertificateCompositeRdbNo);
		rdbNo.setSelection(true);
		rdbNo.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				blnCertificateRequest = false;
			}
		});

		btnInfo = new Button(grpServerCertificate, SWT.NONE);
		btnInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI
						.getWorkbench().getActiveWorkbenchWindow().getShell(),
						SWT.ICON_INFORMATION | SWT.OK);
				messageBox
						.setMessage(Messages.ServerCertificateInformationText);
				messageBox.setText(Messages.ServerHelloCompositeBtnInfo);
				messageBox.open();*/
				infoText=true;
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
		btnNextStep.setText(Messages.ServerCertificateCompositeBtnNextStep);
		btnNextStep.setBounds(176, 140, 140, 25);

		Label lblCertificate = new Label(grpServerCertificate, SWT.NONE);
		lblCertificate.setBounds(10, 25, 160, 15);
		lblCertificate
				.setText(Messages.ServerCertificateCompositeLblCertificate);

		btnShow = new Button(grpServerCertificate, SWT.NONE);
		btnShow.setBounds(241, 20, 75, 25);
		btnShow.setText(Messages.ServerCertificateCompositeBtnShow);
		btnShow.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				try {
					CertificateShow cShow = new CertificateShow(certServer);
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		try {
			// Server Certificate
			getExchangeParams(strExchange);
			getKeyParams(strCipher);
			keyPair = generateKeyExchange(strExchange, exchangeLength);
			
			//REMOVE IF DH Signature Works (or fixed any other way)
			if(!strSignature.contentEquals("DH"))
				certServer = generateX509(keyPair);

			// Server Key Exchange Message
			if (strExchange.contentEquals("RSA") || strExchange.contentEquals("DH_RSA") || strExchange.contentEquals("DH_DSS")) {
				//No ServerKeyExchange is sent
				lblServerKeyExchange.setEnabled(false);
			}else{
				//ServerKeyExchange is sent
			}

		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidParameterSpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CertificateEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SignatureException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/*
		 * if (!(strCipher == "NULL")) { getKeyParams(strCipher); key =
		 * generateKey(strCipher, cipLength); }
		 * 
		 * if (!(strHash == "NULL")) { System.out.println(generateHash(strHash,
		 * strCipherSuit)); }
		 */
		refreshInformations();
	}
	public void refreshInformations()
	{
		if(infoText)
		{
			sslView.setStxInformationText(Messages.ServerCertificateInformationText);
		}
		else
		{
			sslView.setStxInformationText("");
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
	}

	/**
	 * Checks if the given Parameters are valid.
	 * 
	 * @return
	 */
	public boolean checkParameters() {
		Message.setServerCertificateServerCertificateRequest(blnCertificateRequest);
		Message.setServerCertificateServerCertificate(certServer);
		Message.setServerCertificateHash(strHash);
		Message.setServerCertificateSignature(strSignature);
		Message.setServerCertificateServerKeyExchange(keyPair);
		Message.setServerCertificateServerHelloDone(strExchange);

		return true;
	}

	/**
	 * Calculates which KeyExchange Parameters are used and which size the keys
	 * should have
	 * 
	 * @param exchange
	 */
	private void getExchangeParams(String exchange) {
		String[] strEx = exchange.split("_");
		strExchange = strEx[0];
		System.out.println(exchange);

		if (strEx[0].contentEquals("RSA")) {
			strSignature = "RSA";
			exchangeLength = RSA_KEY_LENGTH;
		} else if (strEx[0].contentEquals("DHE")
				&& strEx[1].contentEquals("DSS")) {
			strExchange = "DSA";
			exchangeLength = DSA_KEY_LENGTH;
			strSignature = "DSA";
		} else if (strEx[0].contentEquals("DHE")
				&& strEx[1].contentEquals("RSA")) {
			strExchange = "RSA";
			exchangeLength = RSA_KEY_LENGTH;
			strSignature = "RSAEncryption";
		} else if (strEx[0].contentEquals("DH")) {
			strExchange = "DiffieHellman";
			exchangeLength = DH_KEY_LENGTH;
			strSignature = "DH";
		}
	}

	/**
	 * Checks which cipher algorithm is given and how long the key length is.
	 * 
	 * @param cipher
	 *            A cipher
	 */
	private void getKeyParams(String cipher) {
		String[] strCi = cipher.split("_");
		strCipher = strCi[0];

		if (strCi[0].contentEquals("RC4")) {
			cipLength = Integer.parseInt(strCi[1]);
		} else if (strCi[0].contentEquals("AES")) {
			cipLength = Integer.parseInt(strCi[1]);
		} else if (strCi[0].contentEquals("DES")) {
			cipLength = DSA_KEY_LENGTH;
		} else if (strCi[0].contentEquals("3DES")) {
			strCipher = "TRIPLEDES";
			cipLength = TRIPLEDES_KEY_LENGTH;
		}
	}

	/**
	 * Generates a default certificate with the given key pair {@link key}
	 * 
	 * @param key
	 * @throws CertificateEncodingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	private X509Certificate generateX509(KeyPair key)
			throws CertificateEncodingException, InvalidKeyException,
			IllegalStateException, NoSuchProviderException,
			NoSuchAlgorithmException, SignatureException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date notBefor = new Date();
		Date notAfter = new Date();
		notAfter.setYear(notBefor.getYear() + 1);
		notAfter.setHours(23);
		notAfter.setMinutes(59);
		notAfter.setSeconds(59);
		
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal certName = new X500Principal("CN=Test Server Certificate");
		certGen.setSerialNumber(BigInteger.ONE);
		certGen.setIssuerDN(certName);
		certGen.setNotAfter(notAfter);
		certGen.setNotBefore(notBefor);
		certGen.setSubjectDN(certName);
		certGen.setPublicKey(key.getPublic());
		certGen.setSignatureAlgorithm(strHash + "With" + strSignature);

		X509Certificate cert = certGen.generate(key.getPrivate());

		return cert;
	}

	/**
	 * Generates the hash of a given strMessage with the given strHash
	 * 
	 * @param strHash
	 *            The hash used
	 * @param strMessage
	 *            The message which is hashed
	 * @return returns the hashed message
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private String generateHash(String strHash, String strMessage)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String hash = "";
		MessageDigest md = MessageDigest.getInstance(strHash);
		md.update(strMessage.getBytes("UTF-8"));
		byte[] digest = md.digest();
		hash = Hex.encode(digest);
		return hash;
	}

	/**
	 * Generates the Key for a cipher with the given algorithm {@link strKeyTyp}
	 * and the size {@link KeySize}.
	 * 
	 * @param strKeyTyp
	 * @param KeySize
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	private Key generateKey(String strKeyTyp, int KeySize)
			throws NoSuchAlgorithmException, NoSuchPaddingException {
		KeyGenerator keyGen = KeyGenerator.getInstance(strKeyTyp);
		keyGen.init(KeySize, new SecureRandom());
		Key genKey = keyGen.generateKey();
		return genKey;
	}

	/**
	 * Generates a pair of Public and Private key of the given algorithm
	 * {@link strKeyTyp} and with the size of {@link KeySize}.
	 * 
	 * @param strKeyTyp
	 * @param KeySize
	 * @return
	 * @throws Exception 
	 */
	private KeyPair generateKeyExchange(String strKeyTyp, int KeySize)
			throws Exception {

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
				strKeyTyp, BOUNCY_CASTLE_PROVIDER);

		if (strKeyTyp.contentEquals("RSA")) {
			keyPairGenerator.initialize(KeySize, secure);

		} else if (strKeyTyp.contentEquals("DiffieHellman")) {
			keyPairGenerator = KeyPairGenerator.getInstance(strKeyTyp);
		    keyPairGenerator.initialize(KeySize);
		    
		    //REMOVE WHEN DH Signature works
		    btnShow.setEnabled(false);
		}
		KeyPair key = keyPairGenerator.generateKeyPair();
		return key;
	}

	public boolean isBlnCertificateRequest() {
		return blnCertificateRequest;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
