package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.Wizard;

public class ChooseCert extends Wizard {
    private String name = "Java Keystore";
    private ChooseCertPage page;

    public ChooseCert() {
        super();
        setWindowTitle(name);
    }

    @Override
    public void addPages() {
        page = new ChooseCertPage(name);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        return true;
    }

}
