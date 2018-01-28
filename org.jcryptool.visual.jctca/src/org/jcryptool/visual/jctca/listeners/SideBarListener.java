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
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;

/**
 * Listener for the buttons oon the left side of the user view
 * 
 * @author mmacala
 * 
 */
public class SideBarListener implements SelectionListener {
    private CreateCert cCert;
    private ShowCert sCert;
    private SignCert siCert;
    private Composite comp_right;
    private Composite grp_exp;

    public SideBarListener(Composite grp_exp, Composite comp_right) {
        this.comp_right = comp_right;
        this.grp_exp = grp_exp;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {

    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        Button btn = (Button) arg0.getSource();
        // get what button was pressed: 0 - create cert, 1 - manage certs, 2 - sign stuff
        Integer data = (Integer) btn.getData();
        int pressed = data.intValue();

        // dispose the current active view
        if (cCert != null) {
            cCert.dispose();
        }
        if (sCert != null) {
            sCert.dispose();
        }
        if (siCert != null) {
            siCert.dispose();
        }

        switch (pressed) { // create the next one, set it visible
            case 0:
                cCert = new CreateCert(comp_right, grp_exp);
                cCert.setVisible(true);
                break;
            case 1:
                sCert = new ShowCert(comp_right, grp_exp);
                sCert.setVisible(true);
                break;
            case 2:
                siCert = new SignCert(comp_right, grp_exp);
                siCert.setVisible(true);
                break;
        }
        // layout the composite afterwards. also changes the explain group
        comp_right.layout(true, true);
        ScrolledComposite sc1 = (ScrolledComposite) comp_right.getParent().getParent();
        sc1.setMinSize(comp_right.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        grp_exp.layout(true);
        
//        comp_right.getParent().getParent()
        
    }

}
