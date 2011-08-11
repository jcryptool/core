package org.jcryptool.visual.elGamal.ui.wizards.wizardpages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Text;

/**
 * abstract superclass for unified access to {@link SaveKeypairPage} and {@link SavePublicKeyPage}.
 * 
 * @author Michael Gaber
 */
public abstract class SaveWizardPage extends WizardPage {

    /** field for the owner of this keypair. */
    protected Text owner;

    /**
     * constructor just calling super.
     * 
     * @param pageName page name
     * @param title title
     * @param titleImage title image
     */
    public SaveWizardPage(String pageName, String title, ImageDescriptor titleImage) {
        super(pageName, title, titleImage);
    }

}
