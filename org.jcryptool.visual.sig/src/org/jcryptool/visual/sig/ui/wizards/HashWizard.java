package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author Grebe Adds the page of the hash wizard.
 * 
 */
public class HashWizard extends Wizard {
    // The only page of the wizard (for selecting the Hash method)
    HashWizardPage page;
    private String name;
    // Integer representing the chosen hash (0-4)
    private int hash;

    // Constructor
    public HashWizard() {
        super();
        name = "HashWizard";
        setWindowTitle(Messages.HashWizard_Title);
    }

    @Override
    public void addPages() {
        // Create page
        page = new HashWizardPage(name);
        // Add the page to the wizard
        addPage(page);
    }

    @Override
    // Controls what happens after clicking "Finish"
            public
            boolean performFinish() {
        int i = 0; // 0-4
        // get all the radiobuttons from the WizardPage
        Control[] radiobutton = (Control[]) page.getGrpHashes().getChildren();
        // Check which radiobutton is selected
        while (i <= 4) {
            // Check if the current button is selected
            if (((Button) radiobutton[i]).getSelection()) {
                hash = i;
                i = 5; // leave the loop
            }
            i++;
        }
        return true;
    }

    /**
     * @return the hash
     */
    public int getHash() {
        return hash;
    }
}
