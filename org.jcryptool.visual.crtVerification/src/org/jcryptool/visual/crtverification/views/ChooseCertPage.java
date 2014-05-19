package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class ChooseCertPage extends WizardPage {
    private ChooseCertComposite compositeFile;

    public ChooseCertPage(String pageName) {
        super(pageName);

        setTitle(pageName);
        setDescription("Description TestWizard");
    }
    
    // TestComposite wird erzeugt
    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new ChooseCertComposite(parent, NONE);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public ChooseCertComposite getCompositeFile() {
        return compositeFile;
    }

}
