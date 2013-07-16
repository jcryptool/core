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
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;

/**
 * ???
 * 
 * @author mmacala
 * 
 */
public class PubKeyListener implements SelectionListener {
    Combo cmb_genKey;

    public PubKeyListener(Combo cmb_genKey) {
        this.cmb_genKey = cmb_genKey;
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        cmb_genKey.setEnabled(true);

    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }
}
