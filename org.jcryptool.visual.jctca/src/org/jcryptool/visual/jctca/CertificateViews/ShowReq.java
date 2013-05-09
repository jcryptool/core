package org.jcryptool.visual.jctca.CertificateViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

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

		left = new Composite(composite, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Group showSelectedRequest = new Group(composite, SWT.NONE);
		showSelectedRequest.setLayout(new GridLayout(2, true));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		showSelectedRequest.setLayoutData(gd_grp);
		showSelectedRequest.setText(Messages.ShowReq_editCSR_RR);

		// center = new Composite(showSelectedRequest, SWT.NONE);
		// center.setLayout(new GridLayout(1, true));
		// center.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));

		Tree tree = new Tree(left, SWT.BORDER);

		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
		tree_item_csr.setText(Messages.ShowReq_CertReqs);
		TreeItem tree_subitem_csr = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr.setText(Messages.ShowReq_DummyCSR0);
		TreeItem tree_subitem_csr1 = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr1.setText(Messages.ShowReq_DummyCSR1);
		TreeItem tree_subitem_csr2 = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr2.setText(Messages.ShowReq_DummyCSR2);
		TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
		tree_item_crl.setText(Messages.ShowReq_RevReqs);
		TreeItem tree_subitem_crl = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl.setText(Messages.ShowReq_DummyRR0);
		TreeItem tree_subitem_crl1 = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl1.setText(Messages.ShowReq_DummyRR1);
		TreeItem tree_subitem_crl2 = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl2.setText(Messages.ShowReq_DummyRR2);
		tree.getItems()[0].setExpanded(true);
		tree.getItems()[1].setExpanded(true);

		lst_private_keys_ca = new List(showSelectedRequest, SWT.NONE);
		lst_private_keys_ca.setLayoutData(new GridData(SWT.FILL, SWT.NONE,
				true, true, 2, 1));
		lst_private_keys_ca.add(Messages.ShowReq_DummyCAkey0);
		lst_private_keys_ca.add(Messages.ShowReq_DummyCAkey1);
		lst_private_keys_ca.add(Messages.ShowReq_DummyCAkey2);
		lst_private_keys_ca.add(Messages.ShowReq_DummyCAkey3);

		btn_accept_request = new Button(showSelectedRequest, SWT.NONE);
		btn_accept_request
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_accept_request.setText(Messages.ShowReq_ReqGrant);
		btn_reject_request = new Button(showSelectedRequest, SWT.NONE);
		btn_reject_request
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_reject_request.setText(Messages.ShowReq_ReqDeny);

		// Label lbl_exp = (Label)exp.getChildren()[0];
		// lbl_exp.setText("Hi, I explain what is going on in Show Request!");

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
