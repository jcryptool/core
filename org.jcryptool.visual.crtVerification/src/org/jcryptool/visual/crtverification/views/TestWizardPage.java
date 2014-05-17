package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class TestWizardPage extends WizardPage {
    private TestComposite compositeFile;

    public TestWizardPage(String pageName) {
        super(pageName);

        setTitle(pageName);
        setDescription("Description TestWizard");
    }
    
    // TestComposite wird erzeugt
    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new TestComposite(parent, NONE);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public TestComposite getCompositeFile() {
        return compositeFile;
    }

}
