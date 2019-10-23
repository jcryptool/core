package org.jcryptool.visual.errorcorrectingcodes.ui.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.errorcorrectingcodes.ui.Messages;

public class EccMainView extends ViewPart {
    private static final int GENERAL_ECC_TAB = 0;
    private static final Point windowsSize = new Point(1280,800);
    private ScrolledComposite scrolledComposite;
    private Composite parent;

    private TabFolder tabFolder;
    private TabItem tabGeneral;
    private TabItem tabMcEliece;
    private GeneralEccView generalEcc;
    private McElieceView mcelieceView;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        
        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridLayoutFactory.fillDefaults().applyTo(scrolledComposite);
        GridDataFactory.fillDefaults().grab(true, true).hint(windowsSize).applyTo(scrolledComposite);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
        
        generalEcc = new GeneralEccView(tabFolder, SWT.NONE);
        tabGeneral = new TabItem(tabFolder, SWT.NONE);
        tabGeneral.setText(Messages.EccMainView_tabGeneralText);
        tabGeneral.setControl(generalEcc);
        mcelieceView = new McElieceView(tabFolder, SWT.NONE);
        tabMcEliece = new TabItem(tabFolder, SWT.NONE);
        tabMcEliece.setText(Messages.EccMainView_tabMcElieceText);
        tabMcEliece.setControl(mcelieceView);
        tabFolder.setSelection(GENERAL_ECC_TAB);
        

        scrolledComposite.setContent(tabFolder);
        scrolledComposite.setMinSize(windowsSize);
        tabFolder.pack();
    }

    @Override
    public void setFocus() {
        tabFolder.setFocus();        
    }

    /**
     * Reset the view to initial state.
     */
    public void resetView() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
    }

}
