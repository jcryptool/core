/**
 * 
 */
package org.jcryptool.visual.jctca.CertificateClasses;

import java.math.BigInteger;
import java.util.Date;

/**
 * An entry in the CRL containing the revocation time and the serial number of the revoked certificate
 * @author sho
 */
public class CRLEntry {
	
	/**
	 * serial number of the revoked certificate
	 */
	BigInteger sn;
	
	/**
	 * time when the certificate has been revoked
	 */
	Date revokeTime;
	
	/**
	 * generate new CRL Entry for the given serial number (hash)
	 * 
	 * @param sn Serial number of the certificate to be revoked
	 * @param time timestamp of revocation
	 * 
	 */
	public CRLEntry(BigInteger sn, Date time) {
		this.sn = sn;
		this.revokeTime = time;
	}
	
	/**
	 * get the serial number of the revoked certificate
	 * 
	 * @return Serial number of revoked certificates
	 */
	public BigInteger GetSerial() {
		return sn;
	}

}
