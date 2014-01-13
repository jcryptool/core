package org.jcryptool.visual.sigVerification.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class SigVerView extends ViewPart {
    public SigVerView() {
    }
    private Composite parent;
    TabFolder tf;
    TabItem ti;

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {                
        this.parent = parent;
        tf = new TabFolder(parent, SWT.TOP);
        
        // Signatur Verifikation tab
        ti = new TabItem(tf, SWT.NONE);
        ti.setText("Signatur-Verifikation");
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        SigVerComposite c = new SigVerComposite(sc, SWT.NONE, this);
        sc.setContent(c);
        ti.setControl(sc);
        sc.setMinSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        

        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent.getShell(), "org.jcryptool.visual.sigVerification.SigVerView"); //$NON-NLS-1$

    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        parent.setFocus();        
    }
    
    public void createTabItem(){
        // Signatur Gültigkeitsmodelle Tab
        ti = new TabItem(tf, SWT.NONE);
        ti.setText("Gültigkeitsmodelle");
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        ModelComposite model = new ModelComposite(sc, SWT.NONE, this);
        sc.setContent(model);
        ti.setControl(sc);
        sc.setMinSize(model.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }
    
    public void changeTab() {
        tf.setSelection(1);
        tf.getTabList()[1].setEnabled(true);
    }

}
