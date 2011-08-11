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
package org.jcryptool.analysis.freqanalysis.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.analysis.freqanalysis.IFreqAnalysisAccess;
import org.jcryptool.analysis.freqanalysis.ui.FreqAnalysisUI;

/**
 * @author SLeischnig
 *
 */
public class FreqAnalysisView extends ViewPart implements IFreqAnalysisAccess {
    /**
     * The constructor.
     */
    public FreqAnalysisView() {
    }

    FreqAnalysisUI myUI;

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public final void createPartControl(final Composite parent) {
        myUI = new FreqAnalysisUI(parent, SWT.NONE);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
                FreqAnalysisPlugin.PLUGIN_ID + ".freqanalysis"); //$NON-NLS-1$
        
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

    public final void execute(final boolean simpleView, final int keyLength, final int keyPos,
            final int overlayIndex, final boolean resetShift, final boolean executeCalc,
            final boolean whichTab, final boolean activateOverlay) {
        myUI.execute(simpleView, keyLength, keyPos, overlayIndex, resetShift, executeCalc,
                whichTab, activateOverlay);

    }
}