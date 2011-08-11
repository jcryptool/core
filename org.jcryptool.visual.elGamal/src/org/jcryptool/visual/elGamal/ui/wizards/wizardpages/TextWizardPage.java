package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Text;

/**
 * abstract superclass for all wizardpages that want only text-input.
 * 
 * @author Michael Gaber
 */
public abstract class TextWizardPage extends WizardPage {

    /**
     * Constructor does only call super.
     * 
     * @see WizardPage
     * @param pageName name of the Page
     * @param title title to be shown
     * @param titleImage the ImageDescriptor of the image to be shown
     */
    protected TextWizardPage(final String pageName, final String title, final ImageDescriptor titleImage) {
        super(pageName, title, titleImage);
    }

    /** textfield. */
    protected Text text;

    /**
     * getter for the text that was entered in this wizardpage.
     * 
     * @return content of the text-field
     */
    public final String getText() {
        return text.getText();
    }

}
