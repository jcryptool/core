package org.jcryptool.visual.jctca;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class UserTab {

	public UserTab(TabFolder parent, int style) {
	    // define the layout for the whole TabItem now
		TabItem t = new TabItem(parent, SWT.NONE);
		t.setText("User");
		Group generalGroup = new Group(parent, SWT.NONE);
		generalGroup.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		t.setControl(generalGroup);

        // 2 columns (actions and the actionswindow)
        generalGroup.setLayout(new GridLayout(2, false));

        // Grid-Layout for all the buttons on the left side
        Composite composite = new Composite(generalGroup, SWT.NONE);
        composite.setLayout(new GridLayout(1, true));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        Label label1 = new Label(composite, SWT.CENTER);

        label1.setText("LabelText");
        label1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));

        new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                true, 1, 1));
        
        Button btn_create_cert = new Button(composite, SWT.PUSH);
        btn_create_cert.setText("Create Certificate");
        
        Button btn_show_cert = new Button(composite, SWT.PUSH);
        btn_show_cert.setText("Show Certificate");
        
        Button btn_sign_stuff = new Button(composite, SWT.PUSH);
        btn_sign_stuff.setText("Sign File/Text");
        
        Button btn_revoke_cert = new Button(composite, SWT.PUSH);
        btn_revoke_cert.setText("Revoke Certificate");
        
        // composite for the labels and dropdows
        Composite right = new Composite(generalGroup, SWT.NONE);
        right.setLayout(new GridLayout(1,false));
        Composite right1 = new Composite(right, SWT.NONE);
        right1.setLayout(new GridLayout(10, false));
        Label lbl = new Label(right1, SWT.NONE);
        lbl.setText("foo?");
	    
	    lbl.setVisible(false);
	    
        Composite right2 = new Composite(right, SWT.NONE);
        right2.setLayout(new GridLayout(10, false));
        Label lbl2 = new Label(right2, SWT.NONE);
        lbl2.setText("WOHOOOO");
       // right.setLayoutData(right2);
       // right.layout(true);
	
	}

}
