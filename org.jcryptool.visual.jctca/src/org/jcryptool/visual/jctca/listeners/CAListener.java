package org.jcryptool.visual.jctca.listeners;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RR;
import org.jcryptool.visual.jctca.UserViews.Messages;
import org.jcryptool.visual.jctca.*;

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
		if(src.equals(requests)||src.equals(keys)){
			TreeItem[] sel = requests.getSelection();
			if(sel!=null && sel[0].getParent()!= null && keys.getSelectionIndex()>=0){
				accept.setEnabled(true);
				reject.setEnabled(true);
			}
			else{
				accept.setEnabled(false);
				reject.setEnabled(false);
			}
		}
		else if(src.equals(accept)){
			TreeItem sel = requests.getSelection()[0];
			CSR csr = (CSR)sel.getData();
			KeyStoreManager mng = KeyStoreManager.getInstance();
			CertificateCSRR csrr = CertificateCSRR.getInstance();
			Date startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);// time from which certificate is valid
			Date expiryDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);// time after which certificate is not valid
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
				e.printStackTrace();
			}
		}
		else if(src.equals(reject)){
			TreeItem sel = requests.getSelection()[0];
			this.removeEntry(sel);
		}
		requests.getParent().layout(true);
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
			//TODO RR
		}
	}

}
