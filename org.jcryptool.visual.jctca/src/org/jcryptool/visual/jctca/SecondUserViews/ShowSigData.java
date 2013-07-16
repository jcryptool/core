//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.SecondUserViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.listeners.SecondUserListener;

/**
 * the view containing all widgets from the second user view
 * 
 * @author mmacala
 * 
 */
public class ShowSigData implements Views {
    Composite composite;
    Composite left;
    Composite center;

    List lst_private_keys_ca;
    Button btn_check_signature;
    Button btn_get_CRL;
    Button btn_deleteEntry;

    StyledText lbl_text;
    StyledText lbl_signature;

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
        showSelectedRequest.setText(Messages.ShowSigData_show_request_headline);

        Tree tree = new Tree(left, SWT.BORDER);

        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Util.create2ndUserRootNodes(tree);
        tree.getItems()[0].setExpanded(true);
        tree.getItems()[1].setExpanded(true);

        GridData gd_txt = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 20);
        lbl_text = new StyledText(showSelectedRequest, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lbl_text.setLayoutData(gd_txt);

        lbl_signature = new StyledText(showSelectedRequest, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        lbl_signature.setLayoutData(gd_txt);

        btn_get_CRL = new Button(showSelectedRequest, SWT.CHECK);
        btn_get_CRL.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn_get_CRL.setText(Messages.ShowSigData_checkbox_check_revoke_status);

        btn_check_signature = new Button(showSelectedRequest, SWT.NONE);
        btn_check_signature.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn_check_signature.setText(Messages.ShowSigData_btn_check_sig);
        btn_check_signature.setEnabled(false);

        btn_deleteEntry = new Button(showSelectedRequest, SWT.NONE);
        btn_deleteEntry.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn_deleteEntry.setText(Messages.ShowSigData_btn_delete_entry);
        btn_deleteEntry.setEnabled(false);
        SecondUserListener listener = new SecondUserListener(btn_check_signature, btn_get_CRL, tree, lbl_text,
                lbl_signature, btn_deleteEntry);
        tree.addSelectionListener(listener);
        btn_check_signature.addSelectionListener(listener);
        btn_deleteEntry.addSelectionListener(listener);
    }

    @Override
    public void dispose() {
        this.composite.dispose();
    }

    @Override
    public void setVisible(boolean visible) {
        composite.setVisible(visible);
    }
}
