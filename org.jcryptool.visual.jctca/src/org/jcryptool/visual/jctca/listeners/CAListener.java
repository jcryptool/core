package org.jcryptool.visual.jctca.listeners;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CRLEntry;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RR;

public class CAListener implements SelectionListener{

	Tree requests;
	List keys;
	Button accept;
	Button reject;
	
	public CAListener(Tree requests, List keys, Button accept, Button reject){
		this.requests = requests;
		this.keys = keys;
		this.accept = accept;
		this.reject = reject;
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Object src = arg0.getSource();
		if (src.equals(requests) || src.equals(keys)) {
			enableButtons(src);
		} else if ((src.equals(accept) || src.equals(reject)) && (requests.getSelection()[0].getData() instanceof CSR)) {
			handleCSR(src);
		} else if ((src.equals(accept) || src.equals(reject)) && (requests.getSelection()[0].getData() instanceof RR)) {
			handleRR(src);
		}
	}
		
	private void enableButtons(Object src) {
		TreeItem[] sel = requests.getSelection();
		if(sel!=null && sel[0].getParentItem()!= null && keys.getSelectionIndex()>=0){
			accept.setEnabled(true);
			reject.setEnabled(true);
		}
		else{
			accept.setEnabled(false);
			reject.setEnabled(false);
		}
	}
	
	private void handleRR(Object src) {
		if(src.equals(accept)){
			TreeItem sel = requests.getSelection()[0];
			RR rr = (RR)sel.getData();
			KeyStoreAlias pubAlias = rr.getAlias();
			KeyStoreManager mng = KeyStoreManager.getInstance();
			Certificate c = mng.getPublicKey(pubAlias);
			if(c instanceof X509Certificate){
				X509Certificate cert = (X509Certificate)c;
				BigInteger sn = cert.getSerialNumber();
				Date revokeTime = new Date(System.currentTimeMillis());
				CRLEntry crle = new CRLEntry(sn, revokeTime);
				CertificateCSRR.getInstance().addCRLEntry(crle);
//				mng.addCertificate(cert, new KeyStoreAlias("JCT-CA Certificate Revocation List - DO NOT DELETE", KeyType.PUBLICKEY, "RSA", 1024, cert.getPublicKey().hashCode()+"",cert.getClass().toString()));
				KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(CertificateCSRR.getInstance().getCAKey(0));
				mng.addKeyPair(kp.getPrivate(), 
								cert, 
								KeyStoreManager.getDefaultKeyPassword().toString(), 
								new KeyStoreAlias("JCT-CA Certificate Revocation List - DO NOT DELETE", KeyType.KEYPAIR_PRIVATE_KEY, "RSA", 1024, cert.getPublicKey().hashCode()+"",cert.getClass().toString()), 
								new KeyStoreAlias("JCT-CA Certificate Revocation List - DO NOT DELETE", KeyType.KEYPAIR_PUBLIC_KEY, revokeTime.getTime()+"", 1024, kp.getPrivate().hashCode()+"",kp.getPrivate().getClass().toString()));
				this.removeEntry(sel);
			}
		}
		else if(src.equals(reject)){
			TreeItem sel = requests.getSelection()[0];
			this.removeEntry(sel);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void handleCSR(Object src) {
		if(src.equals(accept)){
			TreeItem sel = requests.getSelection()[0];
			CSR csr = (CSR)sel.getData();
			KeyStoreManager mng = KeyStoreManager.getInstance();
			CertificateCSRR csrr = CertificateCSRR.getInstance();
			Date startDate = new Date(System.currentTimeMillis());// time from which certificate is valid
			Date expiryDate = new Date(System.currentTimeMillis()+ (10*5*60*60*1000));
			System.out.println("EXPIRY: " + expiryDate.getYear() + "." + expiryDate.getMonth() + "." + expiryDate.getDay());
			BigInteger serialNumber = new BigInteger(System.currentTimeMillis()+"");// serial number for certificate
			
			AsymmetricCipherKeyPair keypair = csrr.getCAKey(keys.getSelectionIndex());
			KeyPair kp = Util.asymmetricKeyPairToNormalKeyPair(keypair);
			X509Certificate caCert = csrr.getCACert(keys.getSelectionIndex());
			X509Certificate cert = Util.certificateForKeyPair(csr, serialNumber, caCert,expiryDate, startDate, kp.getPrivate());
			try {
				PrivateKey priv = mng.getPrivateKey(csr.getPrivAlias(), KeyStoreManager.getDefaultKeyPassword());
				this.removeEntry(sel);
				mng.addKeyPair(priv,cert, new String(KeyStoreManager.getDefaultKeyPassword()), csr.getPrivAlias(),csr.getPubAlias());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogUtil.logError(e);
			}
		}
		else if(src.equals(reject)){
			TreeItem sel = requests.getSelection()[0];
			this.removeEntry(sel);
		}
		
	}
	
	public void removeEntry(TreeItem sel){
		if(sel.getData() instanceof CSR){
			CSR csr = (CSR)sel.getData();
			CertificateCSRR.getInstance().removeCSR(csr);
			KeyStoreManager mng = KeyStoreManager.getInstance();
			if(csr.getPubAlias()!=null){
				mng.delete(csr.getPubAlias());
			}
		}
		else if(sel.getData() instanceof RR){
			RR rr = (RR)sel.getData();
			CertificateCSRR.getInstance().removeRR(rr);
		}
		
		sel.dispose();
		accept.setEnabled(false);
		reject.setEnabled(false);
	}

}
