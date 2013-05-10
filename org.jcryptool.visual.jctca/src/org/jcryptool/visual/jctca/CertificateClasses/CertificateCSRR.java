package org.jcryptool.visual.jctca.CertificateClasses;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;

public class CertificateCSRR {
	private static CertificateCSRR instance = null;
	private ArrayList<CSR> approved_csrs;
	private ArrayList<RR> revocations;
	private ArrayList<AsymmetricCipherKeyPair> caKeys;
	private CertificateCSRR(){
		approved_csrs = new ArrayList<CSR>();
		revocations = new ArrayList<RR>();
		caKeys = new ArrayList<AsymmetricCipherKeyPair>();
	    // GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
		RSAKeyPairGenerator gen = new RSAKeyPairGenerator();
		SecureRandom sr = new SecureRandom();
		gen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(3),
				sr, 1024, 80));
		AsymmetricCipherKeyPair keypair = gen.generateKeyPair();
		caKeys.add(keypair);
		keypair = gen.generateKeyPair();
		caKeys.add(keypair);
		keypair = gen.generateKeyPair();
		caKeys.add(keypair);
		keypair = gen.generateKeyPair();
		caKeys.add(keypair);
		keypair = gen.generateKeyPair();
		caKeys.add(keypair);
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
	
	public ArrayList<RR> getRevocations(){
		return revocations;
	}

	public ArrayList<AsymmetricCipherKeyPair> getCAKeys() {
		return caKeys;
	}
}
