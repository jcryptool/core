package org.jcryptool.visual.jctca.CertificateViews;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.visual.jctca.listeners.TabItemListener;

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
		showSelectedRequest.setText("Ausgewählte Anfrage bearbeiten");

//		center = new Composite(showSelectedRequest, SWT.NONE);
//		center.setLayout(new GridLayout(1, true));
//		center.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));

		Tree tree = new Tree(left,SWT.BORDER);
		
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
		tree_item_csr.setText("Zertifikatsanfrage");
		TreeItem tree_subitem_csr = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr.setText("CSR #1 (durch RA verifiziert)");
		TreeItem tree_subitem_csr1 = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr1.setText("CSR #2 (durch RA verifiziert)");
		TreeItem tree_subitem_csr2 = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr2.setText("CSR #3 (durch RA verifiziert)");
		TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
		tree_item_crl.setText("Widerrufsanfrage");
		TreeItem tree_subitem_crl = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl.setText("RR #1");
		TreeItem tree_subitem_crl1 = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl1.setText("RR #2 ");
		TreeItem tree_subitem_crl2 = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl2.setText("RR #3");
		tree.getItems()[0].setExpanded(true);
		tree.getItems()[1].setExpanded(true);
		
		lst_private_keys_ca = new List(showSelectedRequest, SWT.NONE);
		lst_private_keys_ca.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, true,2,1));
		lst_private_keys_ca.add("CA Schlüssel #1");
		lst_private_keys_ca.add("CA Schlüssel #2");
		lst_private_keys_ca.add("CA Schlüssel #3");
		lst_private_keys_ca.add("CA Schlüssel #4");
		
		btn_accept_request = new Button(showSelectedRequest,SWT.NONE);
		btn_accept_request.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_accept_request.setText("Anfrage annehmen");
		btn_reject_request = new Button(showSelectedRequest, SWT.NONE);
		btn_reject_request.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_reject_request.setText("Anfrage ablehnen");
		
		
//		Label lbl_exp = (Label)exp.getChildren()[0];	
//       lbl_exp.setText("Hi, I explain what is going on in Show Request!");
		
		
	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}
