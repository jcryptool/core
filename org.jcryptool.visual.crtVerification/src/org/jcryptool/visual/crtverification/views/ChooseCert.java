package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.Wizard;

public class ChooseCert extends Wizard {
    private String name = "Java Keystore";
    private ChooseCertPage page;
    private int certType; // [1] UserCert; [2] Cert; [3] RootCert

    public ChooseCert(int type) {
        super();
        setWindowTitle(name);
        certType = type;        
    }

    @Override
    public void addPages() {
        page = new ChooseCertPage(name, certType);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}
