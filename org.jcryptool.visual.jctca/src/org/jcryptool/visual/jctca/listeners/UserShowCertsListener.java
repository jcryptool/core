/**
 * 
 */
package org.jcryptool.visual.jctca.listeners;

import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.visual.jctca.Util;

/**
 * Listener in the "Show Certificates" view. fills up the labels on the right side if a certificate has been selected
 * @author sho
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
	
	Button btn_revoke;

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
	 * @param btn_revoke 
	 */
	public UserShowCertsListener(Label lbl_value_common, Label lbl_value_org,
			Label lbl_value_orgUnit, Label lbl_value_city,
			Label lbl_value_country, Label lbl_value_mail,
			Label lbl_value_common_by, Label lbl_value_org_by,
			Label lbl_value_orgUnit_by, Label lbl_value_issued_on,
			Label lbl_value_expired_on, Button btn_revoke) {
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
		this.btn_revoke = btn_revoke;
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
		X509Certificate pubKey = null;
		try {
			pubKey = (X509Certificate) ksm.getCertificate(ksAlias);
		} catch (UnrecoverableEntryException e1) {
			// TODO Auto-generated catch block
			LogUtil.logError(e1);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			LogUtil.logError(e1);
		}
		//create X500Name from the X509 certificate Subjects distinguished name
		X500Name x500name = new X500Name(pubKey.getSubjectX500Principal().toString());
		//I don't know what this next line does exactly, it just works
		RDN rdn = x500name.getRDNs(BCStyle.CN)[0];
		lbl_value_common.setText(rdn.getFirst().getValue().toString());
		
		lbl_value_org.setText(Messages.UserShowCertsListener_not_part_of_cert);
		lbl_value_orgUnit.setText(Messages.UserShowCertsListener_not_part_of_cert);
		
		rdn = x500name.getRDNs(BCStyle.L)[0];
		lbl_value_city.setText(rdn.getFirst().getValue().toString());

		rdn = x500name.getRDNs(BCStyle.C)[0];
		lbl_value_country.setText(rdn.getFirst().getValue().toString());
		rdn = x500name.getRDNs(BCStyle.E)[0];
		lbl_value_mail.setText(rdn.getFirst().getValue().toString());
		
		
		x500name = new X500Name(pubKey.getIssuerDN().toString());
		rdn = x500name.getRDNs(BCStyle.CN)[0];
		lbl_value_common_by.setText(rdn.getFirst().getValue().toString());
		rdn = x500name.getRDNs(BCStyle.O)[0];
		lbl_value_org_by.setText(rdn.getFirst().getValue().toString());
		rdn = x500name.getRDNs(BCStyle.OU)[0];
		lbl_value_orgUnit_by.setText(rdn.getFirst().getValue().toString());
		
		lbl_value_issued_on.setText(pubKey.getNotBefore().toString());
		lbl_value_expired_on.setText(pubKey.getNotAfter().toString());
		
		btn_revoke.setData("selected", ksAlias); //$NON-NLS-1$
		if(Util.isCertificateRevoked(pubKey.getSerialNumber())){
			btn_revoke.setEnabled(false);
			btn_revoke.setText(Messages.UserShowCertsListener_btn_revoke_cert_was_revoked);
		}
		else{
			btn_revoke.setEnabled(true);
			btn_revoke.setText(Messages.UserShowCertsListener_btn_revoke_cert);
		}
		lbl_value_common.getParent().layout();		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
