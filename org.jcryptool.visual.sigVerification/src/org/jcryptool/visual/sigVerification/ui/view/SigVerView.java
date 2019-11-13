package org.jcryptool.visual.sigVerification.ui.view;

import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.images.ImageService;

public class SigVerView extends ViewPart {
    private boolean showModelTab = false;
    
    public SigVerView() {
    }
    private Composite parent;
    TabFolder tf;
    TabItem ti;
    TabItem ti2;
	protected IToolBarManager toolBarMenu;
	protected String resetCommandId;

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
	public void createPartControl(Composite parent) {  
        this.parent = parent;
    	
        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(parent.getShell(), "org.jcryptool.visual.sigVerification.SigVerView"); //$NON-NLS-1$
        toolBarMenu = getViewSite().getActionBars().getToolBarManager();
        resetCommandId = "org.jcryptool.visual.sigVerification.commands.reset"; //$NON-NLS-1$
        addContributionItem(toolBarMenu, resetCommandId, ImageService.IMAGEDESCRIPTOR_RESET, "Reset", SWT.PUSH);
    	
        tf = new TabFolder(parent, SWT.TOP);
        tf.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        
        // Signatur Verifikation tab
        ti = new TabItem(tf, SWT.NONE);
        ti.setText(Messages.SigVerView_Signature_verification);
        ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setExpandHorizontal(true);
        sc.setExpandVertical(true);
        SigVerComposite sigVerComposite = new SigVerComposite(sc, SWT.NONE, this);
        sc.setContent(sigVerComposite);
        ti.setControl(sc);
        sc.setMinSize(sigVerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sigVerComposite.addResetIconHandler();
        
        //Gültigkeitsmodelle-Tab
        ti2 = new TabItem(tf, SWT.NONE);
        ti2.setText(Messages.SigVerView_Validity_models);
        ScrolledComposite sc2 = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
        sc2.setExpandHorizontal(true);
        sc2.setExpandVertical(true);
        ModelComposite modelComposite = new ModelComposite(sc2, SWT.NONE, this);
        sc2.setContent(modelComposite);
        ti2.setControl(sc2);
        sc2.setMinSize(modelComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
        tf.addSelectionListener(new SelectionAdapter() {
        	@Override
	        public void widgetSelected(SelectionEvent e) {
        		if (tf.getSelection()[0].equals(ti)) {
        			sigVerComposite.addResetIconHandler();
        		} else if (tf.getSelection()[0].equals(ti2)) {
        			modelComposite.addResetIconHandler();
        		}
        	}
		});

    }
    
    private void addContributionItem(IContributionManager manager, final String commandId,
           	final ImageDescriptor icon, final String tooltip, int Style)
        {
           	CommandContributionItemParameter param = new CommandContributionItemParameter(PlatformUI.getWorkbench(),
           		null, commandId, Style);
           	if(icon != null)
           		param.icon = icon;
           	if(tooltip != null && !tooltip.equals(""))
           		param.tooltip = tooltip;
           	CommandContributionItem item = new CommandContributionItem(param);
           	manager.add(item);
        }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        parent.setFocus();        
    }
    
    public void createTabItem(){
        if (showModelTab == false){
            showModelTab = true;
         // Signatur Gültigkeitsmodelle Tab
//            ti = new TabItem(tf, SWT.NONE);
//            ti.setText(Messages.SigVerView_Validity_models);
//            ScrolledComposite sc = new ScrolledComposite(tf, SWT.H_SCROLL | SWT.V_SCROLL);
//            sc.setExpandHorizontal(true);
//            sc.setExpandVertical(true);
//            ModelComposite model = new ModelComposite(sc, SWT.NONE, this);
//            sc.setContent(model);
//            ti.setControl(sc);
//            sc.setMinSize(model.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }
    }
    
    public void changeTab() {
        tf.setSelection(1);
        tf.getTabList()[1].setEnabled(true);
    }

}
