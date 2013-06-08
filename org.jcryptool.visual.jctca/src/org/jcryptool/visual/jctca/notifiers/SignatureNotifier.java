package org.jcryptool.visual.jctca.notifiers;

import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;
import org.jcryptool.visual.sig.algorithm.Input;
import org.jcryptool.visual.sig.listener.SignatureEvent;
import org.jcryptool.visual.sig.listener.SignatureListener;

/**
 * used by the sigvis plugin to notify this plugin if a signature was done successfully
 * @author mmacala
 *
 */
public class SignatureNotifier implements SignatureListener{
	
	public SignatureNotifier(){
		
	}
	

	@Override
	public void signaturePerformed(SignatureEvent e) {
		Signature signature = new Signature(e.getSignature(), e.getPath(),e.getText(), new Date(System.currentTimeMillis()), e.getPrivAlias(),Input.publicKey, e.getHashAlgorithm());
		CertificateCSRR.getInstance().addSignature(signature);
		Input.privateKey = null;
		Input.publicKey = null;
	}
}
