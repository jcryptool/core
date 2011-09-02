package org.jcryptool.visual.he.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Text;

/**
 * abstract superclass for unified access to {@link SaveKeypairPage} and {@link SavePublicKeyPage}.
 * @author Michael Gaber
 */
public abstract class RSASaveWizardPage extends WizardPage {

	/** field for the owner of this keypair. */
	protected Text owner;

	/**
	 * constructor just calling super.
	 * @param pageName page name
	 * @param title title
	 * @param titleImage title image
	 */
	public RSASaveWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

}
