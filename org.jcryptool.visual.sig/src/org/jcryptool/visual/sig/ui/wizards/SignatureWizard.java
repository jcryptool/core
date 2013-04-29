package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

public class SignatureWizard extends Wizard{
	//The only page of the wizard (for selecting the Hash method)
	SignatureWizardPage page;
	private String name;
	//Integer representing the chosen signature (0-4)
	private int signature;

	//Constructor
	public SignatureWizard() {
		super();
		name = "SignatureWizard";
	}	
	
	@Override
	public void addPages() {
		//Create page
		page = new SignatureWizardPage(name);
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
		return true;
	}

	/**
	 * @return the signature
	 */
	public int getSignature() {
		return signature;
	}
}
