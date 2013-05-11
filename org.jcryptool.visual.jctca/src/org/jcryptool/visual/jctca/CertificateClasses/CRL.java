/**
 * 
 */
package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.ArrayList;

import org.bouncycastle.asn1.x500.X500Name;

/**
 * @author sho
 *
 */
public class CRL {
	
	X500Name CRLHolder;
	ArrayList<CRLEntry> revokedCertificates;
	
	/**
	 * 
	 */
	public CRL(X500Name CRLHolder) {
		this.CRLHolder = CRLHolder;
	}

	public ArrayList<CRLEntry> getRevokedCertificates() {
		return revokedCertificates;
	}
	
	public void addCertificateToCRL(CRLEntry crlEntry) {
		if (revokedCertificates == null) {
			this.revokedCertificates = new ArrayList<>();
		}
		revokedCertificates.add(crlEntry);
	}
}
