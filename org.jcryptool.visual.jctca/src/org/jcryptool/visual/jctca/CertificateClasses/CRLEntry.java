/**
 * 
 */
package org.jcryptool.visual.jctca.CertificateClasses;

import java.math.BigInteger;
import java.util.Date;

/**
 * @author sho
 *
 */
public class CRLEntry {
	
	//Serial number of revoked certificate
	BigInteger sn;
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
