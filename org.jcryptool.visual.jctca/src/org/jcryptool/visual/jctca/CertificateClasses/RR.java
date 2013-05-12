package org.jcryptool.visual.jctca.CertificateClasses;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public class RR {
	private KeyStoreAlias pubAlias;
	private String reason;
	public RR(KeyStoreAlias ksAlias, String reason) {
		pubAlias = ksAlias;
		this.reason = reason;
	}
	
	public KeyStoreAlias getAlias(){
		return pubAlias;
	}
	
	public String getReason(){
		return reason;
	}

}
