package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Grebe This class contains the page containing the file input composite. It is a part of the Input wizard.
 * 
 */
public class InputFileWizardPage extends WizardPage {

    private InputFileComposite compositeFile;

    protected InputFileWizardPage(String pageName) {
        super(pageName);

        setTitle(Messages.InputFileWizard_title);
        setDescription(Messages.InputFileWizard_header);
    }

    public void createControl(Composite parent) {
        setPageComplete(false);
        compositeFile = new InputFileComposite(parent, NONE, this);
        // composite.setBounds(x, y, width, height);
        setControl(compositeFile);
    }

    /**
     * @return the compositeFile
     */
    public InputFileComposite getCompositeFile() {
        return compositeFile;
    }

}
