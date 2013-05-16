package org.jcryptool.visual.jctca.CertificateClasses;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;

@SuppressWarnings("deprecation")
public class CertificateCSRR {
	private static CertificateCSRR instance = null;
	private ArrayList<CSR> approved_csrs;
	private ArrayList<RR> revRequests;
	private ArrayList<AsymmetricCipherKeyPair> caKeys;
	private ArrayList<X509Certificate> certs;
	private ArrayList<CRLEntry> crl;

	private CertificateCSRR() {
		approved_csrs = new ArrayList<CSR>();
		revRequests = new ArrayList<RR>();
		caKeys = new ArrayList<AsymmetricCipherKeyPair>();
		certs = new ArrayList<X509Certificate>();
		crl = new ArrayList<CRLEntry>();
		checkCertificatesAndCRL();
	}

	public static CertificateCSRR getInstance() {
		if (instance == null) {
			instance = new CertificateCSRR();
		}
		return instance;
	}

	public void checkCertificatesAndCRL() {
		boolean certsExist = false;
		KeyStoreManager mng = KeyStoreManager.getInstance();
		for (KeyStoreAlias pubAlias : mng.getAllPublicKeys()) {
			if (pubAlias.getContactName().contains("JCT-CA Root Certificates")) {//$NON-NLS-1$
				certsExist = true;
				java.security.cert.Certificate c = mng.getCertificate(pubAlias);
				if (c instanceof X509Certificate) {
					certs.add((X509Certificate) c);
				}
			} else if (pubAlias.getContactName().contains(
					"JCT-CA Certificate Revocation List")) {//$NON-NLS-1$
				java.security.cert.Certificate c = mng.getCertificate(pubAlias);
				if (c instanceof X509Certificate) {
					long time = Long.parseLong(pubAlias.getOperation());
					X509Certificate cert = (X509Certificate) c;
					crl.add(new CRLEntry(cert.getSerialNumber(), new Date(time)));
				}
			}
		}
		if (!certsExist) {
			// GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
			RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
			SecureRandom sr = new SecureRandom();
			gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3), sr,
					1024, 80));

			AsymmetricCipherKeyPair keypair = null;
			for (int i = 0; i < 5; i++) {
				keypair = gen.generateKeyPair();
				KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(keypair);
				// yesterday
				Date validityBeginDate = new Date(System.currentTimeMillis()
						- 24 * 60 * 60 * 1000);
				// in 2 hours
				Date validityEndDate = new Date(System.currentTimeMillis() + 2
						* 365 * 24 * 60 * 60 * 1000);
				X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
				X509Name dnName = new X509Name(
						"CN=JCrypTool, O=JCrypTool, OU=JCT-CA Visual");//$NON-NLS-1$

				certGen.setSerialNumber(BigInteger.valueOf(System
						.currentTimeMillis()));
				certGen.setSubjectDN(dnName);
				certGen.setIssuerDN(dnName); // use the same
				certGen.setNotBefore(validityBeginDate);
				certGen.setNotAfter(validityEndDate);
				certGen.setPublicKey(kp.getPublic());
				certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");//$NON-NLS-1$

				X509Certificate cert = null;
				try {
					Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
					cert = certGen.generate(kp.getPrivate(), "BC");//$NON-NLS-1$

				} catch (Exception e) {

				}
				caKeys.add(keypair);
				certs.add(cert);
				KeyStoreAlias pubAlias = new KeyStoreAlias(
						"JCT-CA Root Certificates - DO NOT DELETE", KeyType.KEYPAIR_PUBLIC_KEY, "RSA", 1024, kp.getPublic().hashCode() + "", kp.getPublic().getClass().toString());//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				KeyStoreAlias privAlias = new KeyStoreAlias(
						"JCT-CA Root Certificates - DO NOT DELETE", KeyType.KEYPAIR_PUBLIC_KEY, "RSA", 1024, kp.getPrivate().hashCode() + "", kp.getPrivate().getClass().toString());//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				mng.addKeyPair(kp.getPrivate(), cert, "", privAlias, pubAlias); //$NON-NLS-1$
			}
		}
	}

	public void addCSR(CSR c) {
		approved_csrs.add(c);
	}

	public ArrayList<CSR> getApproved() {
		return approved_csrs;
	}

	public AsymmetricCipherKeyPair getCAKey(int i) {
		return caKeys.get(i);
	}

	public X509Certificate getCACert(int i) {
		return certs.get(i);
	}

	public ArrayList<RR> getRevocations() {
		return revRequests;
	}

	public ArrayList<AsymmetricCipherKeyPair> getCAKeys() {
		return caKeys;
	}

	public void removeCSR(int i) {
		this.approved_csrs.remove(i);
	}

	public void removeCSR(CSR c) {
		this.approved_csrs.remove(c);
	}

	public void removeRR(RR r) {
		this.revRequests.remove(r);
	}

	public ArrayList<CRLEntry> getCRL() {
		return crl;
	}

	public void addRR(RR rr) {
		this.revRequests.add(rr);
	}

	public void addCRLEntry(CRLEntry crle) {
		this.crl.add(crle);
	}

	public ArrayList<CRLEntry> getRevoked() {
		// TODO Auto-generated method stub
		return crl;
	}

}
