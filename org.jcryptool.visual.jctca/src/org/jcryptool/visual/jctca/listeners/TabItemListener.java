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
			lbl_exp.setText("In der Ansicht \"Benutzer\" haben Sie die Möglichkeit, neue Zertifikate anzufordern, bereits ausgestellte Zertifikate zu verwalten sowie Texte oder Dateien zu signieren.\n\nWählen Sie eine der Aktionen aus, um fortzufahren und weitere Informationen zu erhalten. Wenn Sie dieses Plugin zum ersten Mal ausführen, empfiehlt es sich, mit \"Neues Zertifikat anfordern\" zu beginnen.");

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
			lbl_exp.setText("Die \"Registration Authority\" (RA) ist der Teil einer PKI, der für die Identitätsprüfung eines CSR zuständig ist. Im Zusammenhang mit einem persönlichen Zertifikat wie in diesem Fall muss also geprüft werden, ob der Antragsteller auch wirklich die Person ist, auf die das Zertifikat  ausgestellt werden soll.\n\nLinkerhand sehen Sie eine Liste von Zertifikatsanfragen. Wenn Sie eine davon auswählen, können Sie durch einen Klick auf den Button \"Identitätsprüfung vornehmen\" diesen prüfen. Sobald Sie das gemacht haben, können Sie den CSR entweder ablehnen oder an die CA weiterleiten.\n\nMehr über die Tätigkeit der RA erfahren Sie in der Onlinehilfe.");
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
			lbl_exp.setText(Messages.TabItemListener_exp_txt_ca_tab0
					+ Messages.TabItemListener_exp_txt_ca_tab1
					+ Messages.TabItemListener_exp_txt_ca_tab2
					+ Messages.TabItemListener_exp_txt_ca_tab3);

			Group g1 = (Group) parent.getChildren()[2];
			Composite c = (Composite) g1.getChildren()[0];
			Composite c1 = (Composite)c.getChildren()[0];
			Tree tree = (Tree)c1.getChildren()[0];
			tree.removeAll();
			Util.createRootNodes(tree);
			TreeItem csrRoot = tree.getItem(0);
			TreeItem rrRoot = tree.getItem(1);
			ArrayList<CSR> csr_list = CertificateCSRR.getInstance().getApproved();
			ArrayList<RR> rr_list = CertificateCSRR.getInstance().getRevocations();
			for(CSR csr : csr_list){
				TreeItem tree_item_crl = new TreeItem(csrRoot, SWT.NONE);
				tree_item_crl.setText("  " + csr.getFirst() + " " + csr.getLast());
				tree_item_crl.setData(csr);
			}
			for(RR rr : rr_list){
				TreeItem tree_item_crl = new TreeItem(rrRoot, SWT.NONE);
				tree_item_crl.setText(rr.getAlias().getContactName() + " - " + rr.getReason());
				tree_item_crl.setData(rr);
			}
			csrRoot.setExpanded(true);
			rrRoot.setExpanded(true);
			c1.layout();
			
		} else if (parent.getSelectionIndex() == 3) {
			lbl_exp.setText(Messages.TabItemListener_exp_txt_sec_user_tab0
					+ Messages.TabItemListener_exp_txt_sec_user_tab1
					+ Messages.TabItemListener_exp_txt_sec_user_tab2
					+ Messages.TabItemListener_exp_txt_sec_user_tab3);
		} else if (parent.getSelectionIndex() == 4) {
			lbl_exp.setText("Ein Tab, um den Keystore zu erkunden und zu verwalten.");
		}
		parent.layout(true);
		grp_exp.layout(true);
	}

}
