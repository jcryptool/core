package org.jcryptool.visual.jctca.SecondUserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ShowSigData implements Views {
	Composite composite;
	Composite left;
	Composite center;

	List lst_private_keys_ca;
	Button btn_check_signature;
	Button btn_get_CRL;

	Label lbl_text;
	Label lbl_signature;

	public ShowSigData(Composite content, Composite exp) {
		composite = new Composite(content, SWT.NONE);
		composite.setLayout(new GridLayout(2, true));
		GridData gd_comp = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite.setLayoutData(gd_comp);

		left = new Composite(composite, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Group showSelectedRequest = new Group(composite, SWT.NONE);
		showSelectedRequest.setLayout(new GridLayout(1, true));
		GridData gd_grp = new GridData(SWT.FILL, SWT.TOP, true, true);
		showSelectedRequest.setLayoutData(gd_grp);
		showSelectedRequest.setText(Messages.ShowSigData_show_request_headline);

		Tree tree = new Tree(left, SWT.BORDER);

		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
		tree_item_csr.setText(Messages.ShowSigData_signed_texts_tree_headline);
		TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
		tree_item_crl.setText(Messages.ShowSigData_signed_files_tree_headline);
		tree.getItems()[0].setExpanded(true);
		tree.getItems()[1].setExpanded(true);

		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
		lbl_text = new Label(showSelectedRequest, SWT.LEFT | SWT.BORDER
				| SWT.WRAP);
		lbl_text.setLayoutData(gd_txt);

		lbl_signature = new Label(showSelectedRequest, SWT.LEFT | SWT.BORDER
				| SWT.WRAP);
		lbl_signature.setLayoutData(gd_txt);

		btn_check_signature = new Button(showSelectedRequest, SWT.NONE);
		btn_check_signature
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_check_signature.setText(Messages.ShowSigData_btn_check_sig);

		btn_get_CRL = new Button(showSelectedRequest, SWT.CHECK);
		btn_get_CRL.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_get_CRL.setText(Messages.ShowSigData_checkbox_check_revoke_status);

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
