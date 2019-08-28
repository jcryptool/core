package org.jcryptool.analysis.fleissner.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.*;
import org.jcryptool.analysis.fleissner.UI.FleissnerWindow;
import org.jcryptool.analysis.fleissner.key.KeySchablone;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

import javax.inject.Inject;
 

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

	@Inject IWorkbench workbench;
	
	@Override
	public void createPartControl(Composite viewParent) {
	    this.viewParent = viewParent;
        ScrolledComposite scrolledComposite = new ScrolledComposite(viewParent, SWT.H_SCROLL | SWT.V_SCROLL);
        parent = new Composite(scrolledComposite, SWT.NONE);

        GridLayout gridLayoutParent = new GridLayout(1, false);
	    parent.setLayout(gridLayoutParent);
        
        fw = new FleissnerWindow(parent, SWT.NONE);
        fw.setLayout(new GridLayout(1,true));
        fw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1));

	    scrolledComposite.setContent(parent);
        scrolledComposite.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.layout();

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(parent, "org.jcryptool.analysis.fleissner.views");
//		"org.jcryptool.analysis.fleissner2.viewer"

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