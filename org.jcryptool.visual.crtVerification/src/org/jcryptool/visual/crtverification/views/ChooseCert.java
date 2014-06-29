package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.wizard.Wizard;

public class ChooseCert extends Wizard {
    private String name = "Java Keystore";
    private ChooseCertPage page;
    private int certType; // [1] UserCert; [2] Cert; [3] RootCert
    private CrtVerViewComposite composite;

    public ChooseCert(int type, CrtVerViewComposite composite) {
        super();
        this.composite = composite;
        TrayDialog.setDialogHelpAvailable(false);
        setWindowTitle(name);
        certType = type;        
    }

    @Override
    public void addPages() {
        page = new ChooseCertPage(name, certType, composite.controller);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
    	composite.btnValidate.setFocus();
        return true;
    }
    
    @Override
    public boolean performCancel() {
    	composite.btnValidate.setFocus();
        return true;
    }
    
}
