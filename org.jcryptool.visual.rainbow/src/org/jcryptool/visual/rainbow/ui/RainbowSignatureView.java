package org.jcryptool.visual.rainbow.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;

public class RainbowSignatureView extends ViewPart{

    private Composite parent;
    private ScrolledComposite compScrolled;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        
        compScrolled = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        compScrolled.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        compScrolled.setExpandHorizontal(true);
        compScrolled.setExpandVertical(true);
        
        
        
    }

    @Override
    public void setFocus() {
        compScrolled.setFocus();
    }
    
    public void reset() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
    }

}
