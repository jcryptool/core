//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----

package org.jcryptool.analysis.kegver.layer5;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/*
 * This class is started from the JCrypTool-GUI. 
 * 
 * The components of the GUI are programmed in KegverGui, this class focuses it and prepares 
 * size, scroll and focus.
 * 
 * TODO: ? Why is this not a Singleton?
 * TODO: ? What is GTK used for?
 * 
 * 
 * @author two
 *
 */
public class KegverView extends ViewPart {
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "org.jcryptool.analysis.kegver.layer5.views.KegverView";

    public static final String GTK = "GTKLookAndFeel";

    /**
     * Contains object with all graphical components and actions of user
     * interfaces.
     */
    private KegverGui gui;

    /**
     * Flag indicating whether this view already got focus; or not.
     */
    private boolean first = true;

    private ScrolledComposite scroll;

    /**
     * Constructs a new view of the Kegver plug-in.
     */
    public KegverView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scroll.setExpandHorizontal(true);
        scroll.setExpandVertical(true);
        gui = new KegverGui(scroll, SWT.NONE);
        gui.layout();
        scroll.setContent(gui);
        scroll.setMinSize(200, 200);
        scroll.layout();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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
        if (!first) {
            return;
        }

        // gets list of all available pages in the active workbench window.
        IWorkbenchPage[] pages = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getPages();

        // no page page available than do nothing and return. view will
        // not be maximized. this case occurs when JCrypTool starts with
        // opened kegver plug-in (left over from previous session)
        if (0 >= pages.length) {
            return;
        }

        // gets list of list of all opened views and looks for view
        // with this view ID. maximizes view; otherwise does nothing.
        IViewReference[] refs = pages[0].getViewReferences();

        for (IViewReference r : refs) {
            if (KegverView.ID.endsWith(r.getId())) {
                r.getPage().setPartState(r, IWorkbenchPage.STATE_MAXIMIZED);
            }
        }

        // view was focused once; so adjust flag.
        first = false;
    }
}
