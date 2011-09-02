//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.friedman.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.analysis.friedman.IFriedmanAccess;
import org.jcryptool.analysis.friedman.ui.FriedmanGraphUI;

public class FriedmanView extends ViewPart implements IFriedmanAccess {
    public void execute(boolean executeCalc) {
        myUI.execute(executeCalc);
    }

    public String getCachedResult() {
        return myUI.getCachedResult();
    }

    private FriedmanGraphUI myUI;

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {
        myUI = new FriedmanGraphUI(parent, SWT.NONE);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
                "org.jcryptool.analysis.friedman.friedman"); //$NON-NLS-1$

        hookActionBar();
    }

    private void hookActionBar() {
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        getViewSite().getActionBars().updateActionBars();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        myUI.setFocus();
    }
}