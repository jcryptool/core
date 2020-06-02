// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sigVerification.cert;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;


/**
* This class uses the Bouncycastle lightweight API to generate X.509 certificates programmatically.
* also allows to check validity of x.509 certificates (shell model)
*
* @author abdul/Huber
*
*/
public class CertGeneration{

  private static final String CERTIFICATE_ALIAS = "JCT";
  private static final String CERTIFICATE_ALGORITHM = "RSA";
  private static final String CERTIFICATE_DN = "CN=JCT, O=JCT, L=Hagenberg, ST=OOE, C= AUT";
  private static final String CERTIFICATE_NAME = "jct.SigVerification";
  private static final int CERTIFICATE_BITS = 1024;
  private int x; //changes to either 1,2 or 3 concerning the certificate(root=1;..)
  private X509Certificate root; //the root certificate
  private X509Certificate level2;//the 2nd level certificate
  private X509Certificate user;//the user certificate
  private Date now; //certificate not before date - save to allow reset
  private Date end; //certificate not after date - save to allow reset

  public X509Certificate getRoot() {
	return root;
}


public void setRoot(X509Certificate root) {
	this.root = root;
}


public X509Certificate getLevel2() {
	return level2;
}


public void setLevel2(X509Certificate level2) {
	this.level2 = level2;
}


public X509Certificate getUser() {
	return user;
}


public void setUser(X509Certificate user) {
	this.user = user;
}


public Date getNow() {
	return now;
}


public void setNow(Date now) {
	this.now = now;
}


public Date getEnd() {
	return end;
}


public void setEnd(Date end) {
	this.end = end;
}

 /**
 * Saves the current start- and enddate for the certificates in order to keep them valid whenever the plugin is started
 */
public CertGeneration(){
	 this.now=new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
	 this.end=new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10));
 }

  static {
      // adds the Bouncy castle provider to java security
      Security.addProvider(new BouncyCastleProvider());
  }


  /**
   * this method creates the x.509 certificate and the needed keypair
 * @param x - the number value of the certificate (root=1)
 * @return the generated certificate
 * @throws Exception
 */
public X509Certificate createCertificate(int x) throws Exception{
	  this.x=x;
      X509Certificate cert = null;
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CERTIFICATE_ALGORITHM);
      keyPairGenerator.initialize(CERTIFICATE_BITS, new SecureRandom());
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      // GENERATE THE X509 CERTIFICATE
      X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
      v3CertGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
      v3CertGen.setIssuerDN(new X509Principal(CERTIFICATE_DN));
      v3CertGen.setNotBefore(now);
      v3CertGen.setNotAfter(end);
      v3CertGen.setSubjectDN(new X509Principal(CERTIFICATE_DN));
      v3CertGen.setPublicKey(keyPair.getPublic());
      v3CertGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
      cert = v3CertGen.generateX509Certificate(keyPair.getPrivate());
      saveCert(cert,keyPair.getPrivate());
      return cert;
  }



/**
 * this method saves the created certificate
 * @param cert - the certificate to save
 * @param key - the private key
 * @throws Exception
 */
private void saveCert(X509Certificate cert, PrivateKey key) throws Exception {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      keyStore.load(null, null);
      keyStore.setKeyEntry(CERTIFICATE_ALIAS+x, key, "JCT".toCharArray(), new java.security.cert.Certificate[]{cert});
      File file = new File(".", CERTIFICATE_NAME+x);
      // Important Note: This will only work if the JCT is started as administrator.
      // If you start the JCT out of eclipse it will throw an exception.
      // To solve this exception simply start eclipse as administrator. The JCT will then be 
      // launched also as administrator and it works.
      keyStore.store(new FileOutputStream(file), "JCT".toCharArray());
  }

 /**
  * checks all validity terms
 * @param date - the date to validate
 * @return true if the verification succeeded
 */
public boolean verify(Date date){
	 boolean verify=false;
	  if(check(user,date)==true){
		  if(check(level2,date)==true){
			  if(check(root,date)==true){
				  if (checkTurn()==true){
					 verify=true;
				  }

			  }
		  }
	  }
	  return verify;
  }

  /**
   * higher leveled certificates need to have a later/the same not after date then those lower than them
 * @return true if the validity periods are correct
 */
private boolean checkTurn() {
	boolean verify=false;

	if(root.getNotAfter().getYear()>level2.getNotAfter().getYear()||root.getNotAfter().getYear()==level2.getNotAfter().getYear()&&root.getNotAfter().getMonth()>level2.getNotAfter().getMonth()||root.getNotAfter().getYear()==level2.getNotAfter().getYear()&&root.getNotAfter().getMonth()==level2.getNotAfter().getMonth()&&root.getNotAfter().getDate()>level2.getNotAfter().getDate()||root.getNotAfter().getYear()==level2.getNotAfter().getYear()&&root.getNotAfter().getMonth()==level2.getNotAfter().getMonth()&&root.getNotAfter().getDate()==level2.getNotAfter().getDate()){
		if(level2.getNotAfter().getYear()>user.getNotAfter().getYear()||level2.getNotAfter().getYear()==user.getNotAfter().getYear()&&level2.getNotAfter().getMonth()>user.getNotAfter().getMonth()||level2.getNotAfter().getYear()==user.getNotAfter().getYear()&&level2.getNotAfter().getMonth()==user.getNotAfter().getMonth()&&level2.getNotAfter().getDate()>user.getNotAfter().getDate()||level2.getNotAfter().getYear()==user.getNotAfter().getYear()&&level2.getNotAfter().getMonth()==user.getNotAfter().getMonth()&&level2.getNotAfter().getDate()==user.getNotAfter().getDate()){
			verify=true;
		}
	}

	return verify;
}



/**
 * makes sure that the date is within the certificates validity period
 * @param toCheck - the certificate to be checked
 * @param date - the date on question
 * @return true if the validity periods are correct
 */
private boolean check(X509Certificate toCheck,Date date){
	  boolean check=false;

	  if(toCheck.getNotBefore().getYear()<date.getYear()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()<date.getMonth()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()==date.getMonth()&&toCheck.getNotBefore().getDate()<date.getDate()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()==date.getMonth()&&toCheck.getNotBefore().getDate()==date.getDate()){
		  if(toCheck.getNotAfter().getYear()>date.getYear()||toCheck.getNotAfter().getYear()==date.getYear()&&toCheck.getNotAfter().getMonth()>date.getMonth()||toCheck.getNotAfter().getYear()==date.getYear()&&toCheck.getNotAfter().getMonth()==date.getMonth()&&toCheck.getNotAfter().getDate()>date.getDate()||toCheck.getNotAfter().getYear()==date.getYear()&&toCheck.getNotAfter().getMonth()==date.getMonth()&&toCheck.getNotAfter().getDate()==date.getDate()){
			  check=true;
		  }
	  }
	  return check;
  }



/**
 * this method creates the needed keypair and the x.509 certificate valid till the given date
 * @param x - the number value of the certificate (root=1)
 * @param newend - the new not after date
 * @return the generated certificate
 */
public X509Certificate createCertificateNew(int x, Date newend) throws Exception{
	this.x=x;
    X509Certificate cert = null;
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CERTIFICATE_ALGORITHM);
    keyPairGenerator.initialize(CERTIFICATE_BITS, new SecureRandom());
    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    // GENERATE THE X509 CERTIFICATE
    X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();
    v3CertGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
    v3CertGen.setIssuerDN(new X509Principal(CERTIFICATE_DN));
    v3CertGen.setNotBefore(now);
    v3CertGen.setNotAfter(newend);
    v3CertGen.setSubjectDN(new X509Principal(CERTIFICATE_DN));
    v3CertGen.setPublicKey(keyPair.getPublic());
    v3CertGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
    cert = v3CertGen.generateX509Certificate(keyPair.getPrivate());
    saveCert(cert,keyPair.getPrivate());
    return cert;
}

}