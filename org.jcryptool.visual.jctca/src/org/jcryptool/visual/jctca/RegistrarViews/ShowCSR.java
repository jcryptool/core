package org.jcryptool.visual.jctca.RegistrarViews;

import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.ResizeHelper;
import org.jcryptool.visual.jctca.listeners.ResizeListener;
import org.jcryptool.visual.jctca.listeners.CSRListener;
import org.jcryptool.visual.jctca.listeners.TabItemListener;

public class ShowCSR implements Views {
	Composite composite;
	Composite left;
	Composite right;
	Composite center;
	Composite grp_exp;

	List lst_csr;

	Label lbl_firstname;
	Label lbl_value_firstname;
	Label lbl_lastname;
	Label lbl_value_lastname;
	Label lbl_street;
	Label lbl_value_street;
	Label lbl_ZIP;
	Label lbl_value_ZIP;
	Label lbl_city;
	Label lbl_value_city;
	Label lbl_country;
	Label lbl_value_country;
	Label lbl_mail;
	Label lbl_value_mail;

	Button btn_verify_identity;
	Button btn_reject_csr;

	public ShowCSR(Composite content, Composite exp) {
		// composite = new Composite(content, SWT.NONE);
		// composite.setLayout(new GridLayout(1, false));
		// GridData gd_comp = new GridData(SWT.FILL, SWT.TOP, true, true);
		// composite.setLayoutData(gd_comp);

		Group showCSRGroup = new Group(content, SWT.NONE);
		showCSRGroup.setLayout(new GridLayout(3, false));
		//set SWT.FILL auf SWT.NONE, um grp nicht vollst. auszufuellen - bild bleibt dann kleiner
		GridData gd_grp = new GridData(SWT.NONE, SWT.FILL, true, true);
		showCSRGroup.setLayoutData(gd_grp);
		showCSRGroup.setText(Messages.ShowCSR_verify_csr_headline);

		left = new Composite(showCSRGroup, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		center = new Composite(showCSRGroup, SWT.NONE);
		center.setLayout(new GridLayout(2, false));
		center.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		right = new Composite(showCSRGroup, SWT.RESIZE);
		right.setLayout(new GridLayout(1, false));
		right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		lst_csr = new List(left, SWT.BORDER);
		GridData lst_ld = new GridData(SWT.FILL, SWT.FILL, true, true);
		lst_ld.minimumWidth= 52; //damit unter Linux die Liste auch korrekt angezeigt wird
		lst_csr.setLayoutData(lst_ld);
		
		lbl_firstname = new Label(center, SWT.NONE);
		lbl_firstname.setText(Messages.ShowCSR_first_name);
		lbl_value_firstname = new Label(center, SWT.FILL);
		lbl_value_firstname.setText(Messages.ShowCSR_dummy_first_name);

		lbl_lastname = new Label(center, SWT.None);
		lbl_lastname.setText(Messages.ShowCSR_last_name);
		lbl_value_lastname = new Label(center, SWT.FILL);
		lbl_value_lastname.setText(Messages.ShowCSR_dummy_last_name);

		lbl_street = new Label(center, SWT.None);
		lbl_street.setText(Messages.ShowCSR_street);
		lbl_value_street = new Label(center, SWT.FILL);
		lbl_value_street.setText(Messages.ShowCSR_dummy_street);

		lbl_ZIP = new Label(center, SWT.None);
		lbl_ZIP.setText(Messages.ShowCSR_zip);
		lbl_value_ZIP = new Label(center, SWT.FILL);
		lbl_value_ZIP.setText(Messages.ShowCSR_dummy_zip);

		lbl_city = new Label(center, SWT.None);
		lbl_city.setText(Messages.ShowCSR_city);
		lbl_value_city = new Label(center, SWT.FILL);
		lbl_value_city.setText(Messages.ShowCSR_dummy_city);

		lbl_country = new Label(center, SWT.None);
		lbl_country.setText(Messages.ShowCSR_country);
		lbl_value_country = new Label(center, SWT.FILL);
		lbl_value_country.setText(Messages.ShowCSR_dummy_country);

		lbl_mail = new Label(center, SWT.None);
		lbl_mail.setText(Messages.ShowCSR_email);
		lbl_value_mail = new Label(center, SWT.FILL);
		lbl_value_mail.setText(Messages.ShowCSR_dummy_email);

		btn_verify_identity = new Button(center, SWT.NONE);
		btn_verify_identity.setText(Messages.ShowCSR_verify_identity);
		GridData btn_ld = new GridData(SWT.FILL, SWT.FILL,
				true, true);
		btn_ld.minimumWidth = 140;
		btn_verify_identity.setLayoutData(btn_ld);
		
		btn_reject_csr = new Button(center, SWT.NONE);
		btn_reject_csr.setText(Messages.ShowCSR_csr_deny);
		btn_reject_csr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));
		btn_reject_csr.setLayoutData(btn_ld);
		
//		Label lbl_exp = (Label)exp.getChildren()[0];
//		lbl_exp.setText("Hi, I explain what is going on in Show CSR!");
		ResizeHelper util = new ResizeHelper();
		String ausweis = Messages.ShowCSR_dummy_ausweis_path;
		Label lbl_img = new Label(right, SWT.RESIZE);
		right.addControlListener(new ResizeListener(lbl_img, right));
		lbl_img.setImage(Activator.getImageDescriptor(ausweis).createImage());
		util.set_image_name("ausweis");
		lst_csr.addSelectionListener(new CSRListener(lbl_value_firstname, lbl_value_lastname, lbl_value_street, lbl_value_ZIP, lbl_value_city, lbl_value_country, lbl_value_mail, lbl_img));
		
	}

	public void dispose() {
		this.composite.dispose();
	}

	public void setVisible(boolean visible) {
		composite.setVisible(visible);
	}
}
