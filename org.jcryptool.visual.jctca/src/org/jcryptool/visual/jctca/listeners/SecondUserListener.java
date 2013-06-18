package org.jcryptool.visual.jctca.listeners;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
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
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;

/**
 * listener on all the components in the second user view where you can verify
 * signed messages
 * @author mmacala
 *
 */
public class SecondUserListener implements SelectionListener{
	Button btn_check_signature;
	Button btn_get_CRL;
	Tree tree;
	Label lbl_text;
	Label lbl_signature;
	Button btn_deleteEntry;
	
	public SecondUserListener(Button btn_check_signature, Button btn_get_CRL,
			Tree tree, Label lbl_text, Label lbl_signature, Button btn_deleteEntry) {
		this.btn_check_signature = btn_check_signature;
		this.btn_get_CRL = btn_get_CRL;
		this.tree = tree;
		this.lbl_signature = lbl_signature;
		this.lbl_text = lbl_text;
		this.btn_deleteEntry = btn_deleteEntry;
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
					byte[] data = (sig.getPath()!="" ? sig.getPath() : sig.getText()).getBytes(); //$NON-NLS-1$
					byte[] hash, signature;
					boolean certRevoked = false; //hilfsvariable um festzustellen ob Zertifikat widerrufen wurde, aber die signatur vor dem widerruf erstellt wurde
					if (btn_get_CRL.getSelection()) {
						//should revocation status be checkeD?
						KeyStoreAlias pubAlias = sig.getPubAlias();
						Certificate cert= null;
						try {
							cert = KeyStoreManager.getInstance().getCertificate(pubAlias);
						} catch (UnrecoverableEntryException e) {
							// TODO Auto-generated catch block
							LogUtil.logError(e);
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							LogUtil.logError(e);
						}
						if(cert instanceof X509Certificate){
							X509Certificate x509 = (X509Certificate)cert;
							if(Util.isCertificateRevoked(x509.getSerialNumber())){	//certificate has been revoked
								if(!Util.isDateBeforeRevocation(x509.getSerialNumber(), sig.getTime())){//signature is after revokation
									Util.showMessageBox(Messages.SecondUserListener_msgbox_title_was_revoked, Messages.SecondUserListener_msgbox_text_was_revoked, SWT.ICON_WARNING);
									return;
								}
								else{
									certRevoked = true;
								}
							}
						}
					}
					try {
						
						java.security.Signature checkSig = java.security.Signature.getInstance(sig.getHashAlgorithm()+"withRSA"); //$NON-NLS-1$
						X509Certificate cert = (X509Certificate)KeyStoreManager.getInstance().getCertificate(sig.getPubAlias());
						
						if(cert.getNotAfter().after(sig.getTime())){//signature after valid date of the certificate?
							PublicKey pk = cert.getPublicKey();
							checkSig.initVerify(pk);
							if(!sig.getPath().equals("")){
								FileInputStream file;
								try {
									file = new FileInputStream(sig.getPath());
									BufferedInputStream bufin = new BufferedInputStream(file);
									
									byte[] buffer = new byte[1024];
									int len;
									while(file.available()!=0){
										len = bufin.read(buffer);
										checkSig.update(buffer, 0, len);
									}
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							else{
								checkSig.update(sig.getText().getBytes());
							}
							if(checkSig.verify(sig.getSignature())){ //signature is valid
								if(certRevoked){ //certificate is revoked, but signature is before the revocation
									Util.showMessageBox(Messages.SecondUserListener_msgbox_title_success, Messages.SecondUserListener_msgbox_text_signed_before_revoke, SWT.ICON_INFORMATION);
								}
								else{ //certificate is not revoked
									Util.showMessageBox(Messages.SecondUserListener_msgbox_title_success,Messages.SecondUserListener_msgbox_text_was_not_revoked, SWT.ICON_INFORMATION);
								}
							}
							else{ //signature is invalid
								Util.showMessageBox(Messages.SecondUserListener_msgbox_title_badsig, Messages.SecondUserListener_msgbox_text_badsig, SWT.ICON_ERROR);
							}
						}
						else{ //certificate is out of date and the signature has been created afterwards
							Util.showMessageBox(Messages.SecondUserListener_msgbox_title_revoked,Messages.SecondUserListener_msgbox_text_revoked , SWT.ICON_ERROR);
						}
					} catch (SignatureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnrecoverableEntryException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		else if(src.equals(tree)){
			TreeItem sel = tree.getSelection()[0];
			if(sel != null && sel.getData() != null){ //get the content from the selected signature and set the fields according to it
				Signature sig = (Signature)sel.getData();
				lbl_signature.setText(Util.bytesToHex(sig.getSignature()));
				lbl_text.setText(sig.getPath().equals("") ? sig.getText() : sig.getPath()); //$NON-NLS-1$
				btn_deleteEntry.setEnabled(true);
				btn_check_signature.setEnabled(true);
			}
			else {
				lbl_signature.setText(""); //$NON-NLS-1$
				lbl_text.setText(""); //$NON-NLS-1$
				btn_check_signature.setEnabled(false);
				btn_deleteEntry.setEnabled(false);
			}
		}
		else if(src.equals(btn_deleteEntry)){ //delete an entry from the signature list
			TreeItem sel = tree.getSelection()[0];
			if(sel!=null && sel.getData()!=null){
				Signature sig = (Signature)sel.getData();
				CertificateCSRR.getInstance().removeSignature(sig);
				sel.dispose();
				tree.layout();
				lbl_signature.setText(""); //$NON-NLS-1$
				lbl_text.setText(""); //$NON-NLS-1$
			}
		}
	}
}
