package org.jcryptool.visual.jctca.CertificateViews;

import java.util.ArrayList;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.listeners.CAListener;

/*
 * Certficiate Authority View
 * Shows CSRs and RRs and a list of root certificate for signing
 * 
 */
public class ShowReq implements Views {
	Composite composite;
	Composite left;
	Composite center;

	List lst_private_keys_ca;
	Button btn_accept_request;
	Button btn_reject_request;

	public ShowReq(Composite content, Composite exp) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(3, true));
		GridData gd_comp = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd_comp);

		// left = composite for tree
		left = new Composite(composite, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// showSelectedRequest = group for list of root certificates and two
		// buttons (accept and reject)
		Group showSelectedRequest = new Group(composite, SWT.NONE);
		showSelectedRequest.setLayout(new GridLayout(2, true));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		showSelectedRequest.setLayoutData(gd_grp);
		showSelectedRequest.setText(Messages.ShowReq_grp_show_request_headline);

		Tree tree = new Tree(left, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Util.createRootNodes(tree);

		lst_private_keys_ca = new List(showSelectedRequest, SWT.NONE);
		lst_private_keys_ca.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
				true, true, 2, 1));
		ArrayList<AsymmetricCipherKeyPair> caKeys = CertificateCSRR
				.getInstance().getCAKeys();
		int cnt = 1;
		for (AsymmetricCipherKeyPair key : caKeys) {
			lst_private_keys_ca.add(Messages.ShowReq_root_ca_entry_text + cnt
					+ ": " + key.getPublic().hashCode());//$NON-NLS-1$
			cnt++;
		}
		btn_accept_request = new Button(showSelectedRequest, SWT.NONE);
		btn_accept_request
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_accept_request.setText(Messages.ShowReq_btn_grant_cert);
		btn_accept_request.setEnabled(false);

		btn_reject_request = new Button(showSelectedRequest, SWT.NONE);
		btn_reject_request
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_reject_request.setText(Messages.ShowReq_btn_reject_request);
		btn_reject_request.setEnabled(false);

		CAListener lst = new CAListener(tree, lst_private_keys_ca,
				btn_accept_request, btn_reject_request);
		tree.addSelectionListener(lst);
		lst_private_keys_ca.addSelectionListener(lst);
		btn_accept_request.addSelectionListener(lst);
		btn_reject_request.addSelectionListener(lst);

	}

	@Override
	public void dispose() {
		this.composite.dispose();
	}

	@Override
	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}
