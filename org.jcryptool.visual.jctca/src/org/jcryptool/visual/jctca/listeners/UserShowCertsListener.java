/**
 * 
 */
package org.jcryptool.visual.jctca.listeners;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import sun.security.x509.*;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.RDN;

/**
 * @author sho
 *
 */
public class UserShowCertsListener implements SelectionListener {
	
	KeyStoreManager ksm = KeyStoreManager.getInstance();

	Label lbl_value_common;
	Label lbl_value_org;
	Label lbl_value_orgUnit;
	Label lbl_value_city;
	Label lbl_value_country;
	Label lbl_value_mail;
	
	Label lbl_value_common_by;
	Label lbl_value_org_by;
	Label lbl_value_orgUnit_by;

	/**
	 * Create new selection listener for the certificate list in manage certificates of the user tab of the jct-ca visual
	 * 
	 * @param lbl_value_common 
	 * @param lbl_value_org
	 * @param lbl_value_orgUnit
	 * @param lbl_value_city
	 * @param lbl_value_country
	 * @param lbl_value_mail
	 * @param lbl_value_common_by
	 * @param lbl_value_org_by
	 * @param lbl_value_orgUnit_by
	 * @param lbl_value_issued_on
	 * @param lbl_value_expired_on
	 */
	public UserShowCertsListener(Label lbl_value_common, Label lbl_value_org,
			Label lbl_value_orgUnit, Label lbl_value_city,
			Label lbl_value_country, Label lbl_value_mail,
			Label lbl_value_common_by, Label lbl_value_org_by,
			Label lbl_value_orgUnit_by, Label lbl_value_issued_on,
			Label lbl_value_expired_on) {
		this.lbl_value_common = lbl_value_common;
		this.lbl_value_org = lbl_value_org;
		this.lbl_value_orgUnit = lbl_value_orgUnit;
		this.lbl_value_city = lbl_value_city;
		this.lbl_value_country = lbl_value_country;
		this.lbl_value_mail = lbl_value_mail;
		this.lbl_value_common_by = lbl_value_common_by;
		this.lbl_value_org_by = lbl_value_org_by;
		this.lbl_value_orgUnit_by = lbl_value_orgUnit_by;
		this.lbl_value_issued_on = lbl_value_issued_on;
		this.lbl_value_expired_on = lbl_value_expired_on;
	}

	Label lbl_value_issued_on;
	Label lbl_value_expired_on;

	
	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		List lst = (List) e.getSource();
		int selected = lst.getSelectionIndex();
		KeyStoreAlias ksAlias = (KeyStoreAlias) lst.getData(Integer.toString(selected));
		//get public key for the ksAlias and cast it to a X509 Certificate
		X509Certificate pubKey = (X509Certificate) ksm.getPublicKey(ksAlias);
		//create X500Name from the X509 certificate Subjects distinguished name
		X500Name x500name = new X500Name(pubKey.getSubjectDN().toString());
		//I don't know what this next line does exactly, it just works
		RDN cn = x500name.getRDNs(BCStyle.CN)[0];
		lbl_value_common.setText(cn.getFirst().getValue().toString());
		
		lbl_value_common.getParent().layout();
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
