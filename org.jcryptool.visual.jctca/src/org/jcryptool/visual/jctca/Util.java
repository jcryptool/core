package org.jcryptool.visual.jctca;

import java.util.ArrayList;
import java.util.ResourceBundle.Control;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;

public class Util {
	
	
	
	private static ArrayList<CSR> csr;
	
	public static ArrayList<CSR> getCSR(){
		return csr;
	}
	
	public static CSR getCSR(int i){
		if(csr != null && csr.size()>i){
			return csr.get(i);
		}
		return null;
	}
	
	public static void addCSR(String txt_first_name, String txt_last_name, String txt_street, String txt_zip, 
			String txt_town, String txt_country, String txt_mail, String path, KeyStoreAlias pubAlias, KeyStoreAlias privAlias){
		if(csr == null){
			csr = new ArrayList<CSR>();
		}
		csr.add(new CSR(txt_first_name, txt_last_name, txt_street, txt_zip, 
								txt_town, txt_country, txt_mail, path, pubAlias, privAlias));
	}
	
	public static void showMessageBox(String title, String text, int type){
		MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), type);
	    box.setText(title);
	    box.setMessage(text);
	    box.open();
	}
}
