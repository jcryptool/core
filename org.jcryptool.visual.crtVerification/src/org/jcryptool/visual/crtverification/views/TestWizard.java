package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.Wizard;

public class TestWizard extends Wizard {
    private String name = "TestWizard";
    private TestWizardPage page;

    public TestWizard() {
        super();
        setWindowTitle(name);
    }

    @Override
    public void addPages() {
        page = new TestWizardPage(name);
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        return false;
    }

}
