package org.jcryptool.visual.jctca.listeners;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.CertificateCSRR;
import org.jcryptool.visual.jctca.CertificateClasses.RR;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;
import org.jcryptool.visual.jctca.CertificateClasses.Signature;

// TODO: dispose left side of usertab

public class TabItemListener implements SelectionListener {
	TabFolder parent;
	Composite grp_exp;

	public TabItemListener(TabFolder parent, Composite grp_exp) {
		this.parent = parent;
		this.grp_exp = grp_exp;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// System.out.println(parent.getSelectionIndex());
		Label lbl_exp = (Label) grp_exp.getChildren()[0];
		if (parent.getSelectionIndex() == 0) {
			lbl_exp.setText(Messages.TabItemListener_tab_user_explain);

			Control[] x = (parent.getChildren());
			if (x.length > 0) {
				Control[] foo = ((Group) x[0]).getChildren();
				Composite right = (Composite) foo[1];
				if (right.getChildren().length > 0) {
					Composite right2 = (Composite) right.getChildren()[0];
					right2.dispose();
					((Group) x[0]).layout(true);
				}
			}
		} else if (parent.getSelectionIndex() == 1) {
			lbl_exp.setText(Messages.TabItemListener_tab_ra_explain);
			Group g1 = (Group) parent.getChildren()[1];
			Group g2 = (Group) g1.getChildren()[0];
			Composite c = (Composite) g2.getChildren()[0];
			List lst_csr = (List) c.getChildren()[0];
			lst_csr.removeAll();
			ArrayList<CSR> csrs = RegistrarCSR.getInstance().getCSR();
			for (int i = 0; csrs != null && i < csrs.size(); i++) {
				//CSR csr = csrs.get(i);
				lst_csr.add("CSR #" + (i + 1));//$NON-NLS-1$
			}
			c.layout();
			lst_csr.select(0);
		} else if (parent.getSelectionIndex() == 2) {
			lbl_exp.setText(Messages.TabItemListener_tab_ca_explain);

			Group g1 = (Group) parent.getChildren()[2];
			Composite c = (Composite) g1.getChildren()[0];
			Composite c1 = (Composite)c.getChildren()[0];
			Tree tree = (Tree)c1.getChildren()[0];
			tree.removeAll();
			Util.createCARootNodes(tree);
			TreeItem csrRoot = tree.getItem(0);
			TreeItem rrRoot = tree.getItem(1);
			ArrayList<CSR> csr_list = CertificateCSRR.getInstance().getApproved();
			ArrayList<RR> rr_list = CertificateCSRR.getInstance().getRevocations();
			for(CSR csr : csr_list){
				TreeItem tree_item_crl = new TreeItem(csrRoot, SWT.NONE);
				tree_item_crl.setText("  " + csr.getFirst() + " " + csr.getLast());//$NON-NLS-1$ //$NON-NLS-2$
				tree_item_crl.setData(csr);
			}
			for(RR rr : rr_list){
				TreeItem tree_item_crl = new TreeItem(rrRoot, SWT.NONE);
				tree_item_crl.setText(rr.getAlias().getContactName() + " - " + rr.getReason());//$NON-NLS-1$
				tree_item_crl.setData(rr);
			}
			csrRoot.setExpanded(true);
			rrRoot.setExpanded(true);
			c1.layout();
			
		} else if (parent.getSelectionIndex() == 3) {
			lbl_exp.setText(Messages.TabItemListener_tab_secuser_explain);
			Group g1 = (Group) parent.getChildren()[3];
			Composite c = (Composite) g1.getChildren()[0];
			Composite c1 = (Composite)c.getChildren()[0];
			Tree tree = (Tree)c1.getChildren()[0];
			tree.removeAll();
			Util.create2ndUserRootNodes(tree);
			TreeItem textSig = tree.getItem(0);
			TreeItem fileSig = tree.getItem(1);
			ArrayList<Signature> sigs = CertificateCSRR.getInstance().getSignatures();
			TreeItem it = null;
			for(Signature sig: sigs){
				if(sig.getPath()==null || sig.getPath()==""){
					it = new TreeItem(textSig, SWT.NONE);
					String text = sig.getText();
					it.setText(text.substring(0, Math.min(text.length(), 20)));
				}
				else{
					it = new TreeItem(fileSig, SWT.NONE);
					String path = sig.getPath();
					it.setText(sig.getPath().substring(Math.max(0, path.length()-20), path.length()));
				}
				it.setData(sig);
			}
			textSig.setExpanded(true);
			fileSig.setExpanded(true);
		}
		parent.layout(true);
		grp_exp.layout(true);
	}

}
