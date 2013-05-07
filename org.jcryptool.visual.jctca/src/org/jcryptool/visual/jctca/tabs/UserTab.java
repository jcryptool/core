package org.jcryptool.visual.jctca.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.RevokeCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;
import org.jcryptool.visual.jctca.listeners.SideBarListener;

public class UserTab {

	CreateCert cCert;
	ShowCert sCert;
	RevokeCert rCert;
	SignCert siCert;
	Group grp_exp;
	TabFolder parent;
	public UserTab(TabFolder parent, Group exp, int style) {
		// define the layout for the whole TabItem
		TabItem t = new TabItem(parent, SWT.NONE);
		t.setText("Benutzer");
		Group generalGroup = new Group(parent, SWT.NONE);
		generalGroup.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		t.setControl(generalGroup);
		this.grp_exp = exp;
		this.parent = parent;

		//2 columns (actions and the actionswindow)
        generalGroup.setLayout(new GridLayout(2, false));

        //Grid-Layout for all the buttons on the left side
        Composite left = new Composite(generalGroup, SWT.NONE);
        left.setLayout(new GridLayout(1, true));
        left.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

//   	Label label1 = new Label(composite, SWT.CENTER);
//      label1.setText("LabelText");
//      label1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

//      new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
//                true, 1, 1));

        Composite right = new Composite(generalGroup, SWT.NONE);
        right.setLayout(new GridLayout(1,true));
        right.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        
 //     sCert = new ShowCert(right);
 //     cCert = new CreateCert(right);
        SideBarListener list_side = new SideBarListener(cCert,sCert,rCert,siCert, grp_exp, right);
        
        Button btn_create_cert = new Button(left, SWT.PUSH);
        btn_create_cert.setText("Neues Zertifikat anfordern");
        btn_create_cert.addSelectionListener(list_side);
        btn_create_cert.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Button btn_show_cert = new Button(left, SWT.PUSH);
        btn_show_cert.setText("Eigene Zertifikate verwalten");
        btn_show_cert.addSelectionListener(list_side);
        btn_show_cert.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Button btn_sign_stuff = new Button(left, SWT.PUSH);
        btn_sign_stuff.setText("Text oder Datei signieren");
        btn_sign_stuff.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn_sign_stuff.addSelectionListener(list_side);
        
//        Button btn_revoke_cert = new Button(left, SWT.PUSH);
//        btn_revoke_cert.setText("Zertifikat widerrufen");
//        btn_revoke_cert.addSelectionListener(list_side);
//        btn_revoke_cert.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//	
	}

}
