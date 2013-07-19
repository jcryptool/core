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
package org.jcryptool.visual.jctca.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jcryptool.visual.jctca.SecondUserViews.ShowSigData;

/**
 * creates the tabitem and the view object for the second user
 * 
 * @author mmacala
 * 
 */
public class SecondUserTab {
    /**
     * Generating SecondUser Tab
     * 
     * @param parent TabFolder to which the new TabItem is added
     * @param exp Group in which the explanation is shown
     * @param style well, yeah, don't know
     **/
    public SecondUserTab(TabFolder parent, Composite exp, int style) {
        TabItem t = new TabItem(parent, SWT.NONE);
        t.setText(Messages.SecondUserTab_tabitem_name);
        Group generalGroup = new Group(parent, SWT.NONE);
        generalGroup.setLayoutData(new GridData(SWT.TOP, SWT.TOP, true, true, 1, 1));
        t.setControl(generalGroup);
        @SuppressWarnings("unused")
        ShowSigData sSig = new ShowSigData(generalGroup, exp);
        generalGroup.setLayout(new GridLayout(1, false));
    }
}
