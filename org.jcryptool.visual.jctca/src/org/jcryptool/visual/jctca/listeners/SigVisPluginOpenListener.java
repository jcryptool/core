package org.jcryptool.visual.jctca.listeners;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.sig.algorithm.*;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;

public class SigVisPluginOpenListener implements SelectionListener {
	Button btn_check;
	Label lbl_file;
	Text txt_sign;
	Combo cmb_keys;
	
	public SigVisPluginOpenListener(Button btn_check, Label lbl_file, Text txt_sign, Combo cmb_keys) {
		this.btn_check = btn_check;
		this.lbl_file = lbl_file;
		this.txt_sign = txt_sign;
		this.cmb_keys = cmb_keys;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (lbl_file.getText() != "") { //$NON-NLS-1$
			System.out.println(lbl_file.getText());
		}else{
			System.out.println(txt_sign.getText());
		}
		String selected = cmb_keys.getText();
		String key_hash = selected.split("Hash:")[1]; //$NON-NLS-1$
		key_hash = key_hash.split(" ")[0]; //$NON-NLS-1$
		System.out.println("KEY HASH: " + key_hash);
		KeyStoreAlias hello = Util.getAliasForHash(key_hash);
		KeyStoreAlias pubAlias = (KeyStoreAlias)cmb_keys.getData(cmb_keys.getText());
		KeyStoreAlias privAlias = KeyStoreManager.getInstance().getPrivateForPublic(pubAlias);
		
		org.jcryptool.visual.sig.algorithm.Input.publicKey = pubAlias;
		org.jcryptool.visual.sig.algorithm.Input.privateKey = privAlias;
		org.jcryptool.visual.sig.algorithm.Input.data = (lbl_file.getText()!="" ? lbl_file.getText() : txt_sign.getText()).getBytes();
		org.jcryptool.visual.sig.algorithm.Input.path = lbl_file.getText();
		byte[] hash, sig;
		if (btn_check.getSelection() == true) {
			try {
				org.jcryptool.visual.sig.algorithm.Input.signNotifier = new SignatureNotifier();
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage()
						.showView("org.jcryptool.visual.sig.view"); //$NON-NLS-1$
				
			} catch (PartInitException e1) {
				LogUtil.logError(e1);
			}
		}
		else{
			
			hash = org.jcryptool.visual.sig.algorithm.Input.data;
			try {
				hash = org.jcryptool.visual.sig.algorithm.Hash.hashInput("SHA-256", hash);
				org.jcryptool.visual.sig.algorithm.SigGeneration.SignInput("SHA256withRSA", hash);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				LogUtil.logError(e1);
			}
			
			sig = org.jcryptool.visual.sig.algorithm.Input.signature;
			Signature signature = new Signature(sig, lbl_file.getText(), txt_sign.getText(), new Date(System.currentTimeMillis()), privAlias, pubAlias);
			CertificateCSRR.getInstance().addSignature(signature);
			Util.showMessageBox("Erfolg", "Die signierte Nachricht wurde versendet!", SWT.ICON_INFORMATION);
			this.lbl_file.setText("");
			this.txt_sign.setText("Text zum Signieren eingeben...");
		}		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

}
