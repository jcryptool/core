package org.jcryptool.visual.jctca.CertificateViews;

import java.util.ArrayList;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.listeners.CAListener;

/**
 * Class that contains the GUI components used in the CA view that shows the approved CSRs and 
 * Revocation Requests.
 * @author mmacala
 *
 */
public class ShowReq implements Views {
	Composite composite;
	Composite left;
	Composite center;
	List lst_private_keys_ca;
	Button btn_accept_request;
	Button btn_reject_request;
	private Label lbl_firstname;
	private Label lbl_value_firstname;
	private Label lbl_lastname;
	private Label lbl_value_lastname;
	private Label lbl_value_street;
	private Label lbl_street;
	private Label lbl_ZIP;
	private Label lbl_value_ZIP;
	private Label lbl_city;
	private Label lbl_value_city;
	private Label lbl_country;
	private Label lbl_value_country;
	private Label lbl_mail;
	private Label lbl_value_mail;

	/**
	 * Certificate Authority View
	 * Shows CSRs and RRs and a list of root certificate for signing
	 * 
	 * @param content composite for showing the content of this view
	 * @param exp composite for showing the explanation of this view
	 **/
	public ShowReq(Composite content, Composite exp) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		GridData gd_comp = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd_comp);

		// left = composite for tree
		left = new Composite(composite, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// showSelectedRequest = group for list of root certificates and two
		// buttons (accept and reject)
		Composite right = new Composite(composite, SWT.NONE);
		right.setLayout(new GridLayout(2, true));
		GridData gd_grp = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_grp.widthHint = 500;
		right.setLayoutData(gd_grp);

		Tree tree = new Tree(left, SWT.BORDER);
		GridData gd_tree= new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_tree.widthHint = 300;
		tree.setLayoutData(gd_tree);

		Util.createCARootNodes(tree);

		lbl_firstname = new Label(right, SWT.NONE);
		lbl_firstname.setText("Vorname(n):");
		lbl_value_firstname = new Label(right, SWT.FILL);
		GridData lbl_gd = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
		lbl_value_firstname.setText("");
		lbl_value_firstname.setLayoutData(lbl_gd);

		lbl_lastname = new Label(right, SWT.None);
		lbl_lastname.setText("Nachname:");
		lbl_value_lastname = new Label(right, SWT.FILL);
		lbl_value_lastname.setText("");
		lbl_value_lastname.setLayoutData(lbl_gd);

		lbl_street = new Label(right, SWT.None);
		lbl_street.setText("Straße:");
		lbl_value_street = new Label(right, SWT.FILL);
		lbl_value_street.setText("");
		lbl_value_street.setLayoutData(lbl_gd);

		lbl_ZIP = new Label(right, SWT.None);
		lbl_ZIP.setText("Postleitzahl:");
		lbl_value_ZIP = new Label(right, SWT.FILL);
		lbl_value_ZIP.setText("");
		lbl_value_ZIP.setLayoutData(lbl_gd);

		lbl_city = new Label(right, SWT.None);
		lbl_city.setText("Stadt:");
		lbl_value_city = new Label(right, SWT.FILL);
		lbl_value_city.setText("");
		lbl_value_city.setLayoutData(lbl_gd);

		lbl_country = new Label(right, SWT.None);
		lbl_country.setText("Land:");
		lbl_value_country = new Label(right, SWT.FILL);
		lbl_value_country.setText("");
		lbl_value_country.setLayoutData(lbl_gd);

		lbl_mail = new Label(right, SWT.None);
		lbl_mail.setText("E-Mail-Adresse:");
		lbl_value_mail = new Label(right, SWT.FILL);
		lbl_value_mail.setText("");
		lbl_value_mail.setLayoutData(lbl_gd);
		
		btn_accept_request = new Button(right, SWT.NONE);
		btn_accept_request
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_accept_request.setText(Messages.ShowReq_btn_grant_cert);
		btn_accept_request.setEnabled(false);

		btn_reject_request = new Button(right, SWT.NONE);
		btn_reject_request
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_reject_request.setText(Messages.ShowReq_btn_reject_request);
		btn_reject_request.setEnabled(false);

		CAListener lst = new CAListener(tree,
				btn_accept_request, btn_reject_request, lbl_value_city, lbl_value_country, lbl_value_firstname, lbl_value_lastname, lbl_value_mail, lbl_value_street, lbl_value_ZIP);
		tree.addSelectionListener(lst);
		btn_accept_request.addSelectionListener(lst);
		btn_reject_request.addSelectionListener(lst);

	}

	/**
	 * Disposes the composite with all the content of this view
	 **/
	@Override
	public void dispose() {
		this.composite.dispose();
	}

	/**
	 * Sets the composite of this view visible
	 * @param visible boolean: true for visible, false for not visible
	 **/
	@Override
	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}
