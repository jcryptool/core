package org.jcryptool.visual.jctca.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;
import org.jcryptool.visual.jctca.listeners.SideBarListener;

/**
 * creates the tabitem and the basic view of the user view with the 
 * sidebar that has the buttons
 * @author mmacala
 *
 */
public class UserTab {

	CreateCert cCert;
	ShowCert sCert;
	SignCert siCert;
	Composite grp_exp;
	TabFolder parent;
	/**
	 * Generating User Tab 
	 * @param parent TabFolder to which the new TabItem is added
	 * @param exp Group in which the explanation is shown
	 * @param style well, yeah, don't know
	 **/
	public UserTab(TabFolder parent, Composite exp, int style) {
		// define the layout for the whole TabItem
		TabItem tab = new TabItem(parent, SWT.NONE);
		tab.setText(Messages.UserTab_tabitem_name);
		Group grp_userTab = new Group(parent, SWT.NONE);
		grp_userTab.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		tab.setControl(grp_userTab);
		this.grp_exp = exp;
		this.parent = parent;

		// 2 columns (actions and the actionswindow)
		grp_userTab.setLayout(new GridLayout(2, false));

		// Grid-Layout for all the buttons on the left side
		Composite left = new Composite(grp_userTab, SWT.NONE);
		left.setLayout(new GridLayout(1, true));
		left.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

		Composite right = new Composite(grp_userTab, SWT.NONE);
		right.setLayout(new GridLayout(1, true));
		right.setLayoutData(new GridData(GridData.FILL_BOTH));

		SideBarListener list_side = new SideBarListener(grp_exp, right);
		
		Group g1 = new Group(left, SWT.NONE);
		g1.setText("PKI-Prozesse");
		Button btn_create_cert = new Button(g1, SWT.PUSH);
		btn_create_cert.setText(Messages.UserTab_btn_get_new_cert);
		btn_create_cert.setData(0); //set data for listener - see SideBarListener.java
		btn_create_cert.addSelectionListener(list_side);
		btn_create_cert.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		
		Button btn_show_cert = new Button(g1, SWT.PUSH);
		btn_show_cert.setText(Messages.UserTab_btn_manage_certs);
		btn_show_cert.setData(1);
		btn_show_cert.addSelectionListener(list_side);
		btn_show_cert.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Group g2 = new Group(left, SWT.None);
		g2.setText("Benutzerprozesse");
		Button btn_sign_stuff = new Button(g2, SWT.PUSH);
		btn_sign_stuff.setText(Messages.UserTab_btn_sign_text_or_file);
		btn_sign_stuff.setData(2);
		btn_sign_stuff.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		btn_sign_stuff.addSelectionListener(list_side);

	}

}
