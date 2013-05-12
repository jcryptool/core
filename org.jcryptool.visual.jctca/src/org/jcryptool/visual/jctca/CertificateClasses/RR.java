package org.jcryptool.visual.jctca.CertificateClasses;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public class RR {
	KeyStoreAlias pubAlias;
	String reason;
	public RR(KeyStoreAlias ksAlias, String reason) {
		pubAlias = ksAlias;
		this.reason = reason;
	}

}
