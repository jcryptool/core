package org.jcryptool.visual.jctca.CertificateClasses;

import java.util.ArrayList;

import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;

public class RegistrarCSR {
	private static RegistrarCSR instance = null;
	private ArrayList<CSR> csr;

	private RegistrarCSR(){
		csr = new ArrayList<CSR>();
	}
	
	public static RegistrarCSR getInstance(){
		if(instance==null){
			instance = new RegistrarCSR();
		}
		return instance;
	}
	public int csrSize(){
		return csr.size();
	}
	public ArrayList<CSR> getCSR() {
		return csr;
	}
	
	public boolean removeCSR(CSR c){
		return csr.remove(c);
	}

	public CSR getCSR(int i) {
		if (csr != null && csr.size() > i && i >= 0) {
			return csr.get(i);
		}
		return null;
	}

	public void addCSR(String txt_first_name, String txt_last_name,
			String txt_street, String txt_zip, String txt_town,
			String txt_country, String txt_mail, String path,
			KeyStoreAlias pubAlias, KeyStoreAlias privAlias) {
		csr.add(new CSR(txt_first_name, txt_last_name, txt_street, txt_zip,
				txt_town, txt_country, txt_mail, path, pubAlias, privAlias));
	}
}
