// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.vigenere.views;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.analysis.vigenere.VigenereBreakerPlugin;
import org.jcryptool.analysis.vigenere.ui.VigenereBreakerGui;
import org.jcryptool.core.util.ui.auto.SmoothScroller;

public class VigenereBreakerView extends ViewPart {
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = VigenereBreakerPlugin.PLUGIN_ID + ".views.VigenereBreakerView";

    public static final String GTK = "GTKLookAndFeel";

    /**
     * Contains object with all graphical components and actions of user
     * interfaces.
     */
    private VigenereBreakerGui gui;

    /**
     * Flag indicating whether this view already got focus; or not.
     */
    private boolean first = true;

    private ScrolledComposite scroll;

    /**
     * Constructs a new view of the Vigenère plug-in.
     */
    public VigenereBreakerView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scroll.setExpandHorizontal(true);
        scroll.setExpandVertical(true);
        scroll.setLayout(new GridLayout());
                
        gui = new VigenereBreakerGui(scroll, this::contentChanged);
		scroll.setContent(gui);
        
        contentChanged(new Control[] {gui});
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
            VigenereBreakerPlugin.PLUGIN_ID + ".vigenerebreaker"); //$NON-NLS-1$
        
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT-H_SCROLL.
    
        hookActionBar();
    }

	public void contentChanged(Control[] which) {
		if (gui != null && ! gui.isDisposed()) {
			scroll.setMinSize(gui.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			SmoothScroller.scrollSmooth(scroll);
			this.scroll.layout(which);
		}
	}

    private void hookActionBar() {
        IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
        mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        getViewSite().getActionBars().updateActionBars();
    }
    
    public void scrollToTop() {
    	if(scroll != null && !scroll.isDisposed()) {
    		if(scroll.getVerticalBar() != null && !scroll.getVerticalBar().isDisposed()) {
    			scroll.getVerticalBar().setSelection(0);
    			scroll.setOrigin(0, 0);
    		}
    	}
    }

    @Override
    public void setFocus() {
        gui.setFocus();
        maximizeView();
    }

    @Override
    public void dispose() {
        gui.dispose();
        scroll.dispose();
        super.dispose();
    }

    /**
     * Looks up this view and maximizes it by first call; otherwise does
     * nothing. Also sets flag {@link VigenereHelper2View#first} after first
     * call.
     */
    private void maximizeView() {
        // checks whether view got focus before or not.
        // if first focus continue; otherwise do nothing (through return)
        if (first) {
            return;
        }

        // gets list of all available pages in the active workbench window.
        IWorkbenchPage[] pages = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getPages();

        // no page page available than do nothing and return. view will
        // not be maximized. this case occurs when JCrypTool starts with
        // opened Vigenère plug-in (left over from previous session)
        if (0 >= pages.length) {
            return;
        }

        // gets list of list of all opened views and looks for view
        // with this view ID. maximizes view; otherwise does nothing.
        IViewReference[] refs = pages[0].getViewReferences();

        for (IViewReference r : refs) {
            if (VigenereBreakerView.ID.endsWith(r.getId())) {
                r.getPage().setPartState(r, IWorkbenchPage.STATE_MAXIMIZED);
            }
        }

        // view was focused once; so adjust flag.
        first = false;
    }
}
