//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.arc4;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.arc4.ui.ARC4Composite;

/**
 * This class holds everything that you see on the screen; It provides horizontal and vertical
 * scrolling and the reset button
 * 
 * @author Luca Rupp
 */
public class ARC4View extends ViewPart {

    private Composite parent;

    // for this scrolling
    private ScrolledComposite scroll;

    // this composite is what actually holds the plug-in contents
    private ARC4Composite arc4;

    @Override
    public void createPartControl(final Composite parent) {
        this.parent = parent;
        // provides horizontal and vertical scrolling for the plug-in
        scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scroll.setExpandHorizontal(true);
        scroll.setExpandVertical(true);
        // grid layout is used, because currently it seems to be the only layout provided by SWT
        // that is capable of
        // dealing with 4K monitors
        scroll.setLayout(new GridLayout(1, true));
        arc4 = new ARC4Composite(scroll, SWT.NONE);
        arc4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scroll.setContent(arc4);
        scroll.setMinSize(arc4.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        // makes the connection to the help of the plug-in
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.arc4.arc4view");
    }

    @Override
    public void setFocus() {
    	parent.setFocus();
    }

    /**
     * For the reset button
     */
    public void reset() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
    }

}