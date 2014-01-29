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
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;


/**
* This class uses the Bouncycastle lightweight API to generate X.509 certificates programmatically.
*
* @author abdul
*
*/
public class CertGeneration{

  private static final String CERTIFICATE_ALIAS = "JCT";
  private static final String CERTIFICATE_ALGORITHM = "RSA";
  private static final String CERTIFICATE_DN = "CN=JCT, O=JCT, L=Hagenberg, ST=OOE, C= AUT";
  private static final String CERTIFICATE_NAME = "jct.SigVerification";
  private static final int CERTIFICATE_BITS = 1024;
  private int x;
  private X509Certificate root;
  private X509Certificate level2;
  private X509Certificate user;
  private X509Certificate[] chain;
  private boolean verify=false;
  private Date now;
  private Date end;
  
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
  
 public CertGeneration(){
	 this.now=new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
	 this.end=new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10));
 }
  
  static {
      // adds the Bouncy castle provider to java security
      Security.addProvider(new BouncyCastleProvider());
  }

  /**
* @param args
* @throws Exception
//*/
//  public static void main(String[] args) throws Exception {
//	  System.out.println("Generated");
//      CertGeneration Certificate = new CertGeneration();
//  }

  @SuppressWarnings("deprecation")
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
      System.out.println(cert);
      return cert;
  }



private void saveCert(X509Certificate cert, PrivateKey key) throws Exception {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      keyStore.load(null, null);
      keyStore.setKeyEntry(CERTIFICATE_ALIAS+x, key, "JCT".toCharArray(), new java.security.cert.Certificate[]{cert});
      File file = new File(".", CERTIFICATE_NAME+x);
      keyStore.store( new FileOutputStream(file), "JCT".toCharArray() );    
  }
  
//  public X509Certificate[] setChain(){
//	  chain=new X509Certificate[3];
//	  chain[0]=user;
//	  chain[1]=level2;
//	  chain[2]=root;
//	  
//	  return chain;
//  }
  
 public boolean verify(Date date){	  
	  if(check(user,date)==true){
		  if(check(level2,date)==true){
			  if(check(root,date)==true){
				  verify=true;
			  }
		  }
	  }
	  return verify;
  }
  
  @SuppressWarnings("deprecation")
private boolean check(X509Certificate toCheck,Date date){
	  boolean check=false;
	  
	  if(toCheck.getNotBefore().getYear()<date.getYear()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()<date.getMonth()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()==date.getMonth()&&toCheck.getNotBefore().getDate()<date.getDate()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()==date.getMonth()&&toCheck.getNotBefore().getDate()==date.getDate()){
		  if(toCheck.getNotAfter().getYear()>date.getYear()||toCheck.getNotAfter().getYear()==date.getYear()&&toCheck.getNotAfter().getMonth()>date.getMonth()||toCheck.getNotAfter().getYear()==date.getYear()&&toCheck.getNotAfter().getMonth()==date.getMonth()&&toCheck.getNotAfter().getDate()>date.getDate()||toCheck.getNotBefore().getYear()==date.getYear()&&toCheck.getNotBefore().getMonth()==date.getMonth()&&toCheck.getNotBefore().getDate()==date.getDate()){
			  check=true;
		  }
	  }
	  return check;
  }

}