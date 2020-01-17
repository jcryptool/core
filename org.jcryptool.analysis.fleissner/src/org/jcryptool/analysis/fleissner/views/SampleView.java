// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.fleissner.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.analysis.fleissner.UI.FleissnerWindow;
import org.jcryptool.analysis.fleissner.key.KeySchablone;
 

public class SampleView extends ViewPart {
    public SampleView() {

    }
    
    private Composite parent;
    private Composite viewParent;
    private FleissnerWindow fw;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.analysis.fleissner.views.SampleView";
	
	@Override
	public void createPartControl(Composite viewParent) {
	    this.viewParent = viewParent;
        ScrolledComposite scrolledComposite = new ScrolledComposite(viewParent, SWT.H_SCROLL | SWT.V_SCROLL);
        parent = new Composite(scrolledComposite, SWT.NONE);

        GridLayout gridLayoutParent = new GridLayout(1, false);
	    parent.setLayout(gridLayoutParent);
        
        fw = new FleissnerWindow(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1,true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        fw.setLayout(layout);
        fw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

	    scrolledComposite.setContent(parent);
        scrolledComposite.setMinSize(parent.computeSize( SWT.DEFAULT, SWT.DEFAULT+fw.getDescriptionText().getSize().y));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.layout();
        scrolledComposite.addListener( SWT.Resize, event -> {
            
            int height = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT).y+fw.getDescriptionText().getSize().y;
            scrolledComposite.setMinSize( parent.computeSize( SWT.DEFAULT, height));
        } );

		// Create the help context id for the viewer's control
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "org.jcryptool.analysis.fleissner.views");
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}
	
    public void resetView(){
        Control[] children = viewParent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(viewParent);
        viewParent.layout();
        fw.getModel().setKey(new KeySchablone(Integer.parseInt(fw.getKeySize().getText())));
        fw.reset();
    }
}