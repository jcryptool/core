package org.jcryptool.visual.jctca.SecondUserViews;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jcryptool.visual.jctca.listeners.TabItemListener;

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
		showSelectedRequest.setText("Ausgewählte Daten und Signatur");

		// center = new Composite(showSelectedRequest, SWT.NONE);
		// center.setLayout(new GridLayout(1, true));
		// center.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, true));

		Tree tree = new Tree(left, SWT.BORDER);

		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		TreeItem tree_item_csr = new TreeItem(tree, SWT.NONE);
		tree_item_csr.setText("Signierte Texte");
		TreeItem tree_subitem_csr = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr.setText("Text #1 (22.4.2013, 15:25 Uhr)");
		TreeItem tree_subitem_csr1 = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr1.setText("Text #2 (23.4.2013, 16:15 Uhr)");
		TreeItem tree_subitem_csr2 = new TreeItem(tree_item_csr, SWT.NONE);
		tree_subitem_csr2.setText("Text #3 (23.4.2013, 15:46 Uhr)");
		TreeItem tree_item_crl = new TreeItem(tree, SWT.NONE);
		tree_item_crl.setText("Signierte Dateien");
		TreeItem tree_subitem_crl = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl.setText("Datei #1 (22.4.2013, 15:30 Uhr)");
		TreeItem tree_subitem_crl1 = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl1.setText("Datei #2 (23.4.2013, 15:45 Uhr)");
		TreeItem tree_subitem_crl2 = new TreeItem(tree_item_crl, SWT.NONE);
		tree_subitem_crl2.setText("Datei #3 (24.4.2013, 18:25 Uhr)");
		tree.getItems()[0].setExpanded(true);
		tree.getItems()[1].setExpanded(true);

		GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
		lbl_text = new Label(showSelectedRequest, SWT.LEFT | SWT.BORDER
				| SWT.WRAP);
		lbl_text.setText("Das ist eine Testnachricht.");
		lbl_text.setLayoutData(gd_txt);

		lbl_signature = new Label(showSelectedRequest, SWT.LEFT | SWT.BORDER
				| SWT.WRAP);
		lbl_signature
				.setText("iQIcBAEBAgAGBQJPO5rvAAoJECygNT2qmzas0TYQALfLUo4jlNaHtHxGOapp86wYyC23wq7qOqDj2cjRf0qi20IMm4NB/1jwjCiLEINPhzlJxVKHe8yTGCsVPR7zaz1p3zZJ");
		lbl_signature.setLayoutData(gd_txt);

		btn_check_signature = new Button(showSelectedRequest, SWT.NONE);
		btn_check_signature
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_check_signature.setText("Signatur überprüfen");

		btn_get_CRL = new Button(showSelectedRequest, SWT.CHECK);
		btn_get_CRL.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_get_CRL.setText("Widerrufsstatus überprüfen");

	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}
