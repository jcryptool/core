package org.jcryptool.visual.jctca.CertificateClasses;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.jcryptool.visual.jctca.Util;

public class CertificateCSRR {
	private static CertificateCSRR instance = null;
	private ArrayList<CSR> approved_csrs;
	private ArrayList<RR> revocations;
	private ArrayList<AsymmetricCipherKeyPair> caKeys;
	private ArrayList<X509Certificate> certs;
	private CertificateCSRR(){
		approved_csrs = new ArrayList<CSR>();
		revocations = new ArrayList<RR>();
		caKeys = new ArrayList<AsymmetricCipherKeyPair>();
		certs = new ArrayList<X509Certificate>();
	    // GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
		RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
		SecureRandom sr = new SecureRandom();
		gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3),
				sr, 1024, 80));
		
		Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);// time from which certificate is valid
		Date expiryDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);// time after which certificate is not valid
		BigInteger serialNumber = new BigInteger(System.currentTimeMillis()+"");// serial number for certificate
		
		AsymmetricCipherKeyPair keypair =null;
		for(int i = 0; i<5; i++){
			keypair= gen.generateKeyPair();	
			KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(keypair);
		    // yesterday
		    Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
		    // in 2 years
		    Date validityEndDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);
		    X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
		    X509Name dnName = new X509Name("CN=JCrypTool, O=JCrypTool, OU=JCT-CA Visual");

		    certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		    certGen.setSubjectDN(dnName);
		    certGen.setIssuerDN(dnName); // use the same
		    certGen.setNotBefore(validityBeginDate);
		    certGen.setNotAfter(validityEndDate);
		    certGen.setPublicKey(kp.getPublic());
		    certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

		    X509Certificate cert = null;
			try {
				Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
				cert = certGen.generate(kp.getPrivate(), "BC");
			} catch (CertificateEncodingException | InvalidKeyException
					| IllegalStateException | NoSuchProviderException
					| NoSuchAlgorithmException | SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			caKeys.add(keypair);
			certs.add(cert);
		}
	}
	
	public static CertificateCSRR getInstance(){
		if(instance==null){
			instance = new CertificateCSRR();
		}
		return instance;
	}
	
	public void addCSR(CSR c){
		approved_csrs.add(c);
	}
	
	public ArrayList<CSR> getApproved(){
		return approved_csrs;
	}
	
	public AsymmetricCipherKeyPair getCAKey(int i){
		return caKeys.get(i);
	}
	
	public X509Certificate getCACert(int i){
		return certs.get(i);
	}
	
	public ArrayList<RR> getRevocations(){
		return revocations;
	}

	public ArrayList<AsymmetricCipherKeyPair> getCAKeys() {
		return caKeys;
	}

	public void removeCSR(int i){
		this.approved_csrs.remove(i);
	}

	public void removeCSR(CSR c){
		this.approved_csrs.remove(c);
	}
	public void removeRR(RR r){
		this.revocations.remove(r);
	}
}
