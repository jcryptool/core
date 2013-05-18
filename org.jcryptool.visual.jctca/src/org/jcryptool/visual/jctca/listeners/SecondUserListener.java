package org.jcryptool.visual.jctca.listeners;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;

public class SecondUserListener implements SelectionListener{
	Button btn_check_signature;
	Button btn_get_CRL;
	Tree tree;
	Label lbl_text;
	Label lbl_signature;
	public SecondUserListener(Button btn_check_signature, Button btn_get_CRL,
			Tree tree, Label lbl_text, Label lbl_signature) {
		this.btn_check_signature = btn_check_signature;
		this.btn_get_CRL = btn_get_CRL;
		this.tree = tree;
		this.lbl_signature = lbl_signature;
		this.lbl_text = lbl_text;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	} 

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Object src = arg0.getSource();
		if(src.equals(btn_check_signature)){
			if(tree.getSelectionCount()>0){
				TreeItem it = tree.getSelection()[0];
				if(it.getData()!=null){
					Signature sig = (Signature)it.getData();
					
					org.jcryptool.visual.sig.algorithm.Input.privateKey = sig.getPrivAlias();
					org.jcryptool.visual.sig.algorithm.Input.data = (sig.getPath()!="" ? sig.getPath() : sig.getText()).getBytes();
					org.jcryptool.visual.sig.algorithm.Input.path = sig.getPath();
					byte[] hash, signature;
					if (btn_get_CRL.getSelection()) {
						KeyStoreAlias pubAlias = sig.getPubAlias();
						Certificate cert = KeyStoreManager.getInstance().getCertificate(pubAlias);
						if(cert instanceof X509Certificate){
							X509Certificate x509 = (X509Certificate)cert;
							if(Util.isCertificateRevoked(x509.getSerialNumber())){
								if(!Util.isDateBeforeRevocation(x509.getSerialNumber(), sig.getTime())){
									Util.showMessageBox("Zertifikat widerrufen", "Die Signatur wurde nach dem widerruf des Zertifikats erstellt!", SWT.ICON_WARNING);
									return;
								}
							}
						}
					} 
					hash = org.jcryptool.visual.sig.algorithm.Input.data;
					try {
						hash = org.jcryptool.visual.sig.algorithm.Hash.hashInput("SHA-256", hash);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						LogUtil.logError(e1);
					}
					try {
						org.jcryptool.visual.sig.algorithm.SigGeneration.SignInput("SHA256withRSA", hash);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						LogUtil.logError(e1);
					}
					byte[] newSignature = org.jcryptool.visual.sig.algorithm.Input.signature;
					if(Arrays.equals(sig.getSignature(), newSignature)){
						Util.showMessageBox("Match","Die Signaturen stimmen überein. Man kann sich sicher sein, dass die Nachricht nicht verändert wurde und tatsächlich vom Absender stammt!", SWT.ICON_WARNING);
					}
					else{
						Util.showMessageBox("Fail", "Die Signaturen stimmen NICHT überein.", SWT.ICON_ERROR);
					}
				}
			}
		}
		else if(src.equals(tree)){
			TreeItem sel = tree.getSelection()[0];
			if(sel != null && sel.getData() != null){
				Signature sig = (Signature)sel.getData();
				lbl_signature.setText(Util.bytesToHex(sig.getSignature()));
				lbl_text.setText(sig.getPath()=="" ? sig.getText() : sig.getPath());
			}
			else {
				lbl_signature.setText("");
				lbl_text.setText("");
			}
		}
	}

}
