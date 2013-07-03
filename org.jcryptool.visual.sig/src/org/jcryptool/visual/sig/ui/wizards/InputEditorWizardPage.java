package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Grebe This class contains the page containing the editor input composite. It is a part of the Input wizard.
 * 
 */
public class InputEditorWizardPage extends WizardPage {

    private InputEditorComposite compositeEditor;

    protected InputEditorWizardPage(String pageName) {
        super(pageName);

        setTitle(Messages.InputEditorWizard_title);
        setDescription(Messages.InputEditorWizard_header);
    }

    @Override
    public void createControl(Composite parent) {
        compositeEditor = new InputEditorComposite(parent, NONE, this);
        setControl(compositeEditor);
        setPageComplete(false);
    }

    // @Override
    // public boolean canFlipToNextPage() {
    // return false;
    // }
}
