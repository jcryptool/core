package org.jcryptool.analysis.fleissner2.views;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.*;
import org.jcryptool.analysis.fleissner2.UI.FleissnerWindow;
import org.jcryptool.analysis.fleissner2.key.Grille;
import org.jcryptool.analysis.fleissner2.key.KeySchablone;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

import javax.inject.Inject;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {
    public SampleView() {

    }
    
    private Composite parent;
    private Composite viewParent;
    private FleissnerWindow fw;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.analysis.fleissner2.views.SampleView";

	@Inject IWorkbench workbench;
	
	@Override
	public void createPartControl(Composite viewParent) {
	    this.viewParent = viewParent;
        ScrolledComposite scrolledComposite = new ScrolledComposite(viewParent, SWT.H_SCROLL | SWT.V_SCROLL);
        parent = new Composite(scrolledComposite, SWT.NONE);
//        parent.setLayout(new GridLayout(3, false));
//	    this.parent = parent;
	    
//	      mainFleissner = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
//	      mainFleissner.setExpandHorizontal(true);
//	      mainFleissner.setExpandVertical(true);
//	      FleissnerWindow fw = new FleissnerWindow(mainFleissner, SWT.NONE);
//	      mainFleissner.setContent(fw);
//	      mainFleissner.setMinSize(fw.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	    
	    GridLayout gridLayoutParent = new GridLayout(1, false);
	    parent.setLayout(gridLayoutParent);
        
	    fw = new FleissnerWindow(parent, SWT.NONE);
	    
	    scrolledComposite.setContent(parent);
        scrolledComposite.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.layout();


		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(parent, "org.jcryptool.analysis.fleissner2.viewer");

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