//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
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
import org.jcryptool.visual.jctca.listeners.SideBarListener;

/**
 * creates the tabitem and the basic view of the user view with the sidebar that has the buttons
 * 
 * @author mmacala
 * 
 */
public class UserTab {
    private Composite grp_exp;

    /**
     * Generating User Tab
     * 
     * @param parent TabFolder to which the new TabItem is added
     * @param exp Group in which the explanation is shown
     * @param style well, yeah, don't know
     **/
    public UserTab(TabFolder parent, Composite exp, int style) {
        // define the layout for the whole TabItem
        TabItem tab = new TabItem(parent, SWT.NONE);
        tab.setText(Messages.UserTab_tabitem_name);
        
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setLayout(new GridLayout());
        tab.setControl(scrolledComposite);
        
        
        Group grp_userTab = new Group(scrolledComposite, SWT.NONE);
        grp_userTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        this.grp_exp = exp;

        // 2 columns (actions and the actionswindow)
        grp_userTab.setLayout(new GridLayout(2, false));

        // Grid-Layout for all the buttons on the left side
        Composite left = new Composite(grp_userTab, SWT.NONE);
        left.setLayout(new GridLayout(1, true));
        left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        Composite right = new Composite(grp_userTab, SWT.NONE);
        right.setLayout(new GridLayout(1, true));
        right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        SideBarListener list_side = new SideBarListener(grp_exp, right);

        Group g1 = new Group(left, SWT.NONE);
        g1.setText(Messages.UserTab_PKI_processes);
        g1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        g1.setLayout(new GridLayout(1, true));

        Button btn_create_cert = new Button(g1, SWT.PUSH);
        btn_create_cert.setText(Messages.UserTab_btn_get_new_cert);
        btn_create_cert.setData(0); // set data for listener - see SideBarListener.java
        btn_create_cert.addSelectionListener(list_side);
        btn_create_cert.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Button btn_show_cert = new Button(g1, SWT.PUSH);
        btn_show_cert.setText(Messages.UserTab_btn_manage_certs);
        btn_show_cert.setData(1);
        btn_show_cert.addSelectionListener(list_side);
        btn_show_cert.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Group g2 = new Group(left, SWT.None);
        g2.setText(Messages.UserTab_User_processes);
        g2.setLayout(new GridLayout(1, true));
        g2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Button btn_sign_stuff = new Button(g2, SWT.PUSH);
        btn_sign_stuff.setText(Messages.UserTab_btn_sign_text_or_file);
        btn_sign_stuff.setData(2);
        btn_sign_stuff.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        btn_sign_stuff.addSelectionListener(list_side);
        
        scrolledComposite.setContent(grp_userTab);
        scrolledComposite.setMinSize(grp_userTab.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
    }
}