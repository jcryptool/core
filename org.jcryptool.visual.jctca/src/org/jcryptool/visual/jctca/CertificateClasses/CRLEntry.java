/**
 * 
 */
package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.Date;

/**
 * @author sho
 *
 */
public class CRLEntry {
	
	//Serial number of revoked certificate
	int sn;
	Date revokeTime;
	/**
	 * 
	 * @param sn Serial number of the certificate to be revoked
	 * @param time timestamp of revocation
	 * 
	 */
	public CRLEntry(int sn, Date time) {
		this.sn = sn;
		this.revokeTime = time;
	}
	
	/**
	 * get the serial number of the revoked certificate
	 * 
	 * @return Serial number of revoked certificates
	 */
	public int GetSerial() {
		return sn;
	}

}
