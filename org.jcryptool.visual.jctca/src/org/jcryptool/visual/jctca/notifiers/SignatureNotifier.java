package org.jcryptool.visual.jctca.notifiers;

import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;

/**
 * used by the sigvis plugin to notify this plugin if a signature was done successfully
 * @author mmacala
 *
 */
public class SignatureNotifier {
	
	public SignatureNotifier(){
		
	}
	
	/**
	 * has to be called when something has been signed in de SigVis Plugin
	 * @param sig - the signature
	 * @param file - the path to the file if a file has been signed, otherwise null
	 * @param text - the taxt that has been signed, otherwise null
	 * @param privAlias - KeyStoreAlias of the private Key
	 * @param pubAlias - KeyStoreAlias of the public Key
	 */
	public void signatureDone(byte[] sig, String file, String text, KeyStoreAlias privAlias, KeyStoreAlias pubAlias){
		Signature signature = new Signature(sig, file,text, new Date(System.currentTimeMillis()), privAlias, pubAlias);
		CertificateCSRR.getInstance().addSignature(signature);
	}
}
