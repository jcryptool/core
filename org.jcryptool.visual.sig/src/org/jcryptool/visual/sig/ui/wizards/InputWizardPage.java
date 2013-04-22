package org.jcryptool.visual.sig.ui.wizards;


//import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class InputWizardPage extends WizardPage {

	private InputComposite composite;

	//Constructor
	protected InputWizardPage(String pageName) {
		//TEST!
		super("Input Wizard");
		setTitle(Messages.InputWizard_title);
		setDescription(Messages.InputWizard_header);
	}
	
	public void createControl(Composite parent) {
		composite = new InputComposite(parent, NONE);
		//composite.setBounds(x, y, width, height);
		setControl(composite);
		setPageComplete(true);
	}
	
	/*public IWizardPage getNextPage() {
		if (composite.rdoFromFile.getSelection()) {
		return getWizard().getPage("file");
		}
		return getWizard().getPage("editor");
		
	}*/

}
