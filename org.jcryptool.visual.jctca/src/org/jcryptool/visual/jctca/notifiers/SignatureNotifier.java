package org.jcryptool.visual.jctca.notifiers;

import java.util.Date;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;

public class SignatureNotifier {
	
	public SignatureNotifier(){
		
	}
	
	public void signatureDone(){
		byte[] sig = org.jcryptool.visual.sig.algorithm.Input.signature;
		String file = org.jcryptool.visual.sig.algorithm.Input.path;
		String text = new String(org.jcryptool.visual.sig.algorithm.Input.data);
		KeyStoreAlias privAlias = org.jcryptool.visual.sig.algorithm.Input.privateKey;
		KeyStoreAlias pubAlias = org.jcryptool.visual.sig.algorithm.Input.publicKey;
		Signature signature = new Signature(sig, file,text, new Date(System.currentTimeMillis()), privAlias, pubAlias);
		CertificateCSRR.getInstance().addSignature(signature);
	}
}
