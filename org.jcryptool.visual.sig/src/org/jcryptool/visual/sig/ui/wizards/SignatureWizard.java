package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

/**
 * 
 * @author Grebe
 * This class adds the pge for the signature wizard.
 *
 */
public class SignatureWizard extends Wizard{
	//The only page of the wizard (for selecting the Hash method)
	SignatureWizardPage page;
	private String name;
	//Integer representing the chosen signature (0-4)
	private int signature;
	private int method = 0;
	private KeyStoreAlias alias = null;

	//Constructor
	public SignatureWizard(int m) {
		super();
		name = "SignatureWizard";
		method = m;
		setWindowTitle(Messages.SignatureWizard_Title);
	}	
	
	@Override
	public void addPages() {
		//Create page
		page = new SignatureWizardPage(name, method);
		//Add the page to the wizard
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		int i = 0; //0-4
		//get all the radiobuttons from the WizardPage
		Control[] radiobutton = (Control[])page.getGrpSignatures().getChildren();
		//Check which radiobutton is selected
		while (i <= 4) {
			//Check if the current button is selected
			if (((Button)radiobutton[i]).getSelection()) {
				signature = i;
				i = 5; //leave the loop
			}
			i++;
		}
		
		//Get the Alias
		alias = page.getAlias();
		//Store the key
		if (alias != null) {
			org.jcryptool.visual.sig.algorithm.Input.key = alias;
		}
		return true;
	}

	/**
	 * @return the signature
	 */
	public int getSignature() {
		return signature;
	}
	
	/**
	 * @return the KeyStoreAlias
	 */
	public KeyStoreAlias getAlias() {
		return alias;
	}
}
