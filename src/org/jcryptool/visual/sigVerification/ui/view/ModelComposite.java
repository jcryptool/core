package org.jcryptool.visual.sigVerification.ui.view;

//import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.visual.sigVerification.Messages;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
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
    private Text lblHeader;
    private Text lblTitle;
    private Button btnShellM;
    private Button btnChainM;
    private Label lblRoot;
    private Label lbllevel2;
    private Label lbllevel3;
    private Text lblrootChoose;
    private Text lbllevel2Choose;
    private Text lbllevel3Choose;
    private Label lblValidDate;
    private Button btnNewResult;
    private Text lblChoose;
    

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
                        lblHeader = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                        lblHeader.setEditable(false);
                        lblHeader.setBounds(10, 10, 964, 31);
                        lblHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.BOLD));
                        lblHeader.setText(Messages.ModelComposite_lblHeader);
                        lblHeader.setBackground(SWTResourceManager.getColor(255, 255, 255));
                        
                        lblTitle = new Text(this, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                        lblTitle.setEditable(false);
                        lblTitle.setBounds(20, 121, 216, 27);
                        lblTitle.setText(Messages.ModelComposite_lblTitle);
                        {
                            Composite border = new Composite(this, SWT.BORDER);
                            border.setBounds(10, 132, 964, 560);
                            {
                                btnShellM = new Button(border, SWT.NONE);
                                btnShellM.setBounds(293, 10, 180, 36);
                                btnShellM.setText(Messages.ModelComposite_btnShellM);
                            }
                            {
                                btnChainM = new Button(border, SWT.NONE);
                                btnChainM.setEnabled(false);
                                btnChainM.setBounds(470, 10, 171, 36);
                                btnChainM.setText(Messages.ModelComposite_btnChainM);
                                
                            }
                            {
                                lblRoot = new Label(border, SWT.NONE);
                                lblRoot.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lblRoot.setBounds(64, 133, 380, 67);
                                lblRoot.setText(Messages.ModelComposite_lblroot);
                            }
                            {
                                lbllevel2 = new Label(border, SWT.NONE);
                                lbllevel2.setText(Messages.ModelComposite_lbllevel2);
                                lbllevel2.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lbllevel2.setBounds(103, 219, 341, 67);
                            }
                            {
                                lbllevel3 = new Label(border, SWT.NONE);
                                lbllevel3.setText(Messages.ModelComposite_lbllevel3);
                                lbllevel3.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lbllevel3.setBounds(145, 304, 299, 67);
                            }
                            {
                                lblrootChoose = new Text(border, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                                lblrootChoose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lblrootChoose.setBounds(773, 133, 146, 67);
                            }
                            {
                                lbllevel2Choose = new Text(border, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                                lbllevel2Choose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lbllevel2Choose.setBounds(773, 219, 146, 67);
                            }
                            {
                                lbllevel3Choose = new Text(border, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                                lbllevel3Choose.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lbllevel3Choose.setBounds(773, 304, 146, 67);
                            }
                            {
                                lblValidDate = new Label(border, SWT.NONE);
                                lblValidDate.setText(" ");
                                lblValidDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
                                lblValidDate.setBounds(404, 416, 146, 67);
                            }
                            {
                                btnNewResult = new Button(border, SWT.NONE);
                                btnNewResult.setBounds(293, 515, 322, 31);
                                btnNewResult.setText(Messages.ModelComposite_btnNewResult);
                            }
                            
                            Button btnReset = new Button(border, SWT.NONE);
                            btnReset.setLocation(860, 515);
                            btnReset.setSize(90, 30);
                            btnReset.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent e) {
                                }
                            });
                            btnReset.setText(Messages.SigVerComposite_btnReset);
                            {
                                lblChoose = new Text(border, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
                                lblChoose.setEditable(false);
                                lblChoose.setText(Messages.ModelComposite_Choose);
                                lblChoose.setBounds(719, 70, 200, 50);
                            }
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
