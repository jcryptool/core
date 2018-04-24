// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crtverification.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class CrtVerView extends ViewPart {
	
    private ScrolledComposite sc;
    private CrtVerViewComposite c;

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
	public void createPartControl(Composite parent) {
        sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        c = new CrtVerViewComposite(sc, SWT.NONE, this);
        sc.setContent(c);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.crtverification.views.CrtVerView"); //$NON-NLS-1$
    }

    /**
     * Passing the focus request to the viewer's control.
     */

    @Override
    public void setFocus() {
        c.setFocus();
    }
}