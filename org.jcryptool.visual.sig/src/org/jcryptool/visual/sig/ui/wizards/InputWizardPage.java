package org.jcryptool.visual.sig.ui.wizards;

// import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Grebe This class contains loads the composite for the first page of the input wizard.
 * 
 */
public class InputWizardPage extends WizardPage {

    private InputComposite composite;

    private boolean enableNext = true;

    // Constructor
    protected InputWizardPage(String pageName) {
        super(pageName);
        setTitle(Messages.InputWizard_title);
        setDescription(Messages.InputWizard_header);
    }

    public void createControl(Composite parent) {
        composite = new InputComposite(parent, NONE);
        setControl(composite);
        setPageComplete(true);
    }

    public int getRdoSelection() {
        if (composite.getRdoFromEditor().getSelection())
            return 0;
        else
            return 1;
    }

    public boolean canFlipToNextPage() {
        return enableNext;
    }

}
