package org.jcryptool.visual.jctca.listeners;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.RevokeCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;

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
		System.out.println(parent.getSelectionIndex());
		Label lbl_exp = (Label) grp_exp.getChildren()[0];
		if (parent.getSelectionIndex() == 0) {
			lbl_exp.setText(Messages.TabItemListener_exp_txt_user_tab0
					// "Unter \"Neues Zertifikat anfordern\" haben Sie die Möglichkeit, ein Zertifikat für einen öffentlichen Schlüssel anzufordern. Sollten Sie noch kein Schlüsselpaar haben, so können Sie sich eines generieren lassen.\nUnter \"Zertifikate verwalten\" können Sie sich, wenn Sie bereits ausgestellte Zertifikate haben, diese anzeigen lassen. Dort haben Sie außerdem die Möglichkeit, Zertifikate zu widerrufen.\nUnter \"Text oder Datei signieren\" können Sie Signaturen für von Ihnen eingegebenen Text oder Dateien auf Ihrem Computer erstellen.\n\n"
					+ Messages.TabItemListener_exp_txt_user_tab1);
			
			Control[] x = (parent.getChildren());
			if(x.length>0){
				Control[] foo = ((Group)x[0]).getChildren();
				Composite right = (Composite) foo[1];
				if (right.getChildren().length > 0) {
					Composite right2 = (Composite) right.getChildren()[0];
					right2.dispose();
					((Group)x[0]).layout(true);
				}
			}
		} else if (parent.getSelectionIndex() == 1) {
			lbl_exp.setText(Messages.TabItemListener_exp_txt_ra_tab0
					+ Messages.TabItemListener_exp_txt_ra_tab1
					+ Messages.TabItemListener_exp_txt_ra_tab2);
			Group g1 = (Group)parent.getChildren()[1];
			Group g2 = (Group)g1.getChildren()[0];
			Composite c = (Composite)g2.getChildren()[0];
			List lst_csr = (List)c.getChildren()[0];
			lst_csr.removeAll();
			ArrayList<CSR> csrs = Util.getCSR();
			for(int i = 0; csrs != null && i<csrs.size(); i++){
				CSR csr = csrs.get(i);
				lst_csr.add(Messages.TabItemListener_csr_item_number + (i+1));
			}
			c.layout();
			lst_csr.select(0);
		} else if (parent.getSelectionIndex() == 2) {
			lbl_exp.setText(Messages.TabItemListener_exp_txt_ca_tab0 +
					Messages.TabItemListener_exp_txt_ca_tab1 +
					Messages.TabItemListener_exp_txt_ca_tab2 +
					Messages.TabItemListener_exp_txt_ca_tab3);
		} else if (parent.getSelectionIndex() == 3) {
			lbl_exp.setText(Messages.TabItemListener_exp_txt_sec_user_tab0 +
					Messages.TabItemListener_exp_txt_sec_user_tab1 +
					Messages.TabItemListener_exp_txt_sec_user_tab2 +
					Messages.TabItemListener_exp_txt_sec_user_tab3);
		}
		parent.layout(true);
		grp_exp.layout(true);
	}

}
