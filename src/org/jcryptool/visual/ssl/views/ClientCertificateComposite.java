package org.jcryptool.visual.ssl.views;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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

import javax.crypto.spec.DHParameterSpec;
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

/**
 * This class represents the Client Certificate Step in the SSL/TLS Handshake.
 * It provides the ability to activate and deactivate all buttons from the step.
 * Furthermore it checks if all the given Parameters are a valid input. When the
 * step is finished the returns the chosen Parameters in a Message() Object to
 * the using object.
 * 
 * @author Denk Gandalf
 * 
 */
public class ClientCertificateComposite extends Composite implements
		ProtocolStep {
	private SslView sslView;
	private boolean infoText=false;
	private Button btnShow;
	private Button btnInfo;
	private Button btnNextStep;

	private String strHash;
	private String strSignature;
	private X509Certificate certClient;
	
	private static BigInteger g512 = new BigInteger("123456789", 16);
	private static BigInteger p512 = new BigInteger("123456789", 16);
	
	private static String BOUNCY_CASTLE_PROVIDER = "BC";
	


	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ClientCertificateComposite(Composite parent, int style,
			final SslView sslView) {
		
		super(parent, style);
		this.sslView = sslView;
		strHash = Message.getServerCertificateHash();
		strSignature = Message.getServerCertificateSignature();
		try {
			if(!(Message.getServerCertificateServerHelloDone().contentEquals("DH")))
			certClient = generateX509(Message.getServerCertificateServerKeyExchange());
		} catch (CertificateEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InvalidKeyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalStateException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchProviderException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SignatureException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Group grpClientCertificate = new Group(this, SWT.NONE);
		grpClientCertificate
				.setText(Messages.ClientCertificateCompositeGrpClientCertificate);
		grpClientCertificate.setBounds(10, 0, 326, 175);
		Label lblCertificate = new Label(grpClientCertificate, SWT.NONE);
		lblCertificate.setBounds(10, 25, 160, 15);
		lblCertificate
				.setText(Messages.ClientCertificateCompositeLblCertifcate);

		Label lblClientKeyExchange = new Label(grpClientCertificate, SWT.NONE);
		lblClientKeyExchange.setBounds(10, 55, 110, 15);
		lblClientKeyExchange
				.setText(Messages.ClientCertificateCompositeLblClientKeyExchange);

		Label lblCertificateVerify = new Label(grpClientCertificate, SWT.NONE);
		lblCertificateVerify.setBounds(10, 85, 90, 15);
		lblCertificateVerify
				.setText(Messages.ClientCertificateCompositeLblCertificateVerify);

		btnShow = new Button(grpClientCertificate, SWT.NONE);
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					CertificateShow cShow = new CertificateShow(certClient);
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnShow.setText(Messages.ClientCertificateCompositeBtnShow);
		btnShow.setBounds(241, 20, 75, 25);
		if(!Message.getServerCertificateServerCertificateRequest()){
			btnShow.setEnabled(false);
			lblCertificate.setEnabled(false);
		}

		btnInfo = new Button(grpClientCertificate, SWT.NONE);
		btnInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ClientCertificateInformationText);
				 messageBox.setText(Messages.ClientCertificateCompositeBtnInfo);
				 messageBox.open();*/
				//sslView.addTextToStxInformationText(Messages.ClientCertificateInformationText);
				infoText=true;
				refreshInformations();
			}
		});
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
		btnInfo.setBounds(70, 140, 100, 25);

		btnNextStep = new Button(grpClientCertificate, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setText(Messages.ClientCertificateCompositeBtnNextStep);
		btnNextStep.setBounds(176, 140, 140, 25);
		refreshInformations();
		
	}
	
	public void refreshInformations()
	{
		if(infoText)
		{
			sslView.setStxInformationText(Messages.ClientCertificateInformationText);
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
	}

	private X509Certificate generateX509(KeyPair key)
			throws CertificateEncodingException, InvalidKeyException,
			IllegalStateException, NoSuchProviderException,
			NoSuchAlgorithmException, SignatureException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date notBefor = new Date();
		Date notAfter = new Date();
		notAfter.setYear(notBefor.getYear()+1);
		notAfter.setHours(23);
		notAfter.setMinutes(59);
		notAfter.setSeconds(59);
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal certName = new X500Principal("CN=Test Client Certificate");
		certGen.setSerialNumber(BigInteger.ONE);
		certGen.setIssuerDN(certName);
		certGen.setNotAfter(notAfter);
		certGen.setNotBefore(notBefor);
		certGen.setSubjectDN(certName);
		certGen.setPublicKey(key.getPublic());
		certGen.setSignatureAlgorithm(strHash + "With" + strSignature
				+ "Encryption");

		X509Certificate cert = certGen.generate(key.getPrivate(), BOUNCY_CASTLE_PROVIDER);

		return cert;
	}
	
	/**
	 * Generates a pair of Public and Private key of the given algorithm
	 * {@link strKeyTyp} and with the size of {@link KeySize}.
	 * 
	 * @param strKeyTyp
	 * @param KeySize
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidParameterSpecException
	 */
	private KeyPair generateKeyExchange(String strKeyTyp, int KeySize)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			InvalidAlgorithmParameterException, InvalidParameterSpecException {

		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
				strKeyTyp, BOUNCY_CASTLE_PROVIDER);

		if (strKeyTyp.contentEquals("RSA")) {
			keyPairGenerator.initialize(KeySize, new SecureRandom());

		} else {
			DHParameterSpec dhParams = new DHParameterSpec(p512, g512);
			keyPairGenerator.initialize(dhParams, new SecureRandom());

		}
		KeyPair key = keyPairGenerator.generateKeyPair();
		return key;
	}

	/**
	 * Checks if the given Parameters are valid.
	 * 
	 * @return
	 */
	public boolean checkParameters() {
		return true;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
