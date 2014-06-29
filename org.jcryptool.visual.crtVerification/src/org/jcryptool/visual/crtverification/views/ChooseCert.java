package org.jcryptool.visual.crtverification.views;

import java.security.cert.X509Certificate;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.keystore.keys.IKeyStoreAlias;

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
        IKeyStoreAlias alias = composite.controller.ksc.getAliasByContactName(page.contact_name);
        X509Certificate cert = (X509Certificate) composite.controller.ksc.getCertificate(alias);
        composite.controller.loadCertificate(page, cert, page.contact_name);
    	composite.btnValidate.setFocus();
        return true;
    }
    
    @Override
    public boolean performCancel() {
    	composite.btnValidate.setFocus();
        return true;
    }
    
}
