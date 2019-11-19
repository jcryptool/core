package org.jcryptool.visual.errorcorrectingcodes.ui.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
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
    private static final int DEFAULT_TAB = 0;
    private Composite parent;

    private CTabFolder tabFolder;
    private CTabItem tabGeneral;
    private CTabItem tabHamming;
    private CTabItem tabMcEliece;
    private GeneralEccView generalEcc;
    private HammingCryptoView hammingView;
    private McElieceView mcEliece;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        
        tabFolder = new CTabFolder(parent, SWT.NONE);
        tabFolder.setSelectionBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
        
        //McEliece Algorithm view
        ScrolledComposite sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
       // GridLayoutFactory.fillDefaults().applyTo(sc);
       // GridDataFactory.fillDefaults().grab(true, true).applyTo(sc);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        
        mcEliece = new McElieceView(sc, SWT.NONE);
        tabMcEliece = new CTabItem(tabFolder, SWT.NONE);
        tabMcEliece.setText(Messages.EccMainView_tabMcElieceText);
        sc.setContent(mcEliece);
        sc.setMinSize(mcEliece.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        tabMcEliece.setControl(sc);
        
        // Hamming McEliece view
        sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        hammingView = new HammingCryptoView(sc, SWT.NONE);
        tabHamming = new CTabItem(tabFolder, SWT.NONE);
        tabHamming.setText(Messages.EccMainView_tabHammingText);
        sc.setContent(hammingView);
        sc.setMinSize(hammingView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        tabHamming.setControl(sc);
        
        //Error correcting coe view
        sc = new ScrolledComposite(tabFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        generalEcc = new GeneralEccView(sc, SWT.NONE);
        tabGeneral = new CTabItem(tabFolder, SWT.NONE);
        tabGeneral.setText(Messages.EccMainView_tabGeneralText);
        sc.setContent(generalEcc);
        sc.setMinSize(generalEcc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        tabGeneral.setControl(sc);
        tabFolder.setSelection(DEFAULT_TAB);
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
