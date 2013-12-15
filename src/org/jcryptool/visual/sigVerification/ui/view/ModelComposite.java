package org.jcryptool.visual.sigVerification.ui.view;

import org.eclipse.jface.action.Action;

/**
 * This class contains all the code required for the design and functionality of the verification model view.
 * 
 */
public class ModelComposite extends Composite  {
    
    public ModelComposite(Composite parent, int style/*, ModelView view*/ ) {
        super(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        createContents(parent);
        createActions();
        
        // Adds reset button to the Toolbar
//        IToolBarManager toolBarMenu = view.getViewSite().getActionBars().getToolBarManager();
//        Action action = new Action("Reset", IAction.AS_PUSH_BUTTON) {public void run() {reset(0);}}; //$NON-NLS-1$
//        action.setImageDescriptor(SigVerificationPlugin.getImageDescriptor("icons/reset.gif")); //$NON-NLS-1$
//        toolBarMenu.add(action);
    }

    private Label lblGeneralDescription;
    private Label lblHeader;
    private Label lblTitle;
    

    /**
     * Create contents of the application window.
     * @param parent
     */
    private void createContents(Composite parent) { 
        parent.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
        parent.setLayout(null);
                        setLayout(null);
                        {
                            lblGeneralDescription = new Label(this, SWT.NONE);
                            lblGeneralDescription.setBounds(10, 37, 964, 78);
                            lblGeneralDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));
                            lblGeneralDescription.setText(Messages.ModelComposite_description);
                           
                        }
                        lblHeader = new Label(this, SWT.NONE);
                        lblHeader.setBounds(10, 10, 964, 31);
                        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
                        lblHeader.setText(Messages.ModelComposite_lblHeader);
                        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));
                        
                        lblTitle = new Label(this, SWT.NONE);
                        lblTitle.setBounds(10, 121, 237, 27);
                        lblTitle.setText(Messages.ModelComposite_lblTitle);
                        {
                            Composite border = new Composite(this, SWT.BORDER);
                            border.setBounds(10, 132, 964, 506);
                        }         

    }
    
    private void createActions() {
        
        // Adds a Listener for Return Button
/*        btnReturn.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                try {
                    Input.privateKey = null;
                    Input.publicKey = null;
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    IViewReference ref = page.findViewReference("org.jcryptool.visual.sigVerification.view"); //$NON-NLS-1$
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(ref);
                    page.closePerspective(null, false, true);
                } catch (Exception ex) {
                    LogUtil.logError(SigVerificationPlugin.PLUGIN_ID, ex);
                }
            }
        });
*/        
    }
    
    private void reset(int step) {
        // If the user already finished other steps, reset everything to this
        // step (keep the chosen algorithms)
        switch (step) {
        case 0:
            //btnHash.setEnabled(false);
        case 1:
           // btnDecrypt.setEnabled(false);
        case 2:
           // btnResult.setEnabled(false);
            break;
        default:
            break;
        }
    }

    /**
     * Create the menu manager.
     * @return the menu manager
     */
/*    @Override
    protected MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager("menu");
        return menuManager;
    }
*/
   
    /**
     * Create the status line manager.
     * @return the status line manager
     */
/*    @Override
    protected StatusLineManager createStatusLineManager() {
        StatusLineManager statusLineManager = new StatusLineManager();
        return statusLineManager;
    }
    
    /**
     * Return the initial size of the window.
     */
/*    @Override
    protected Point getInitialSize() {
        return new Point(1270, 750);
    }
*/    

}
