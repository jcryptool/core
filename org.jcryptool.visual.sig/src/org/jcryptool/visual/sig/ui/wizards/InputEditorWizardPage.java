package org.jcryptool.visual.sig.ui.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class InputEditorWizardPage extends WizardPage {

	private InputEditorComposite compositeEditor;
	private InputComposite composite;

	protected InputEditorWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub

		setTitle(Messages.InputEditorWizard_title);
		setDescription(Messages.InputEditorWizard_header);

	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub

		compositeEditor = new InputEditorComposite(parent, NONE);
		// composite.setBounds(x, y, width, height);
		setControl(compositeEditor);
		setPageComplete(true);
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return compositeEditor.getContent();
	}

	public IWizardPage getNextPage() {
		if (editorChoosen()) {
			return getWizard().getPage("InputEditor Wizard");
		} else {
			return null;
		}
	}

	public boolean canFlipToNextPage() {
		return true;

	}
	
	public boolean editorChoosen() {
		return composite.rdoFromEditor.getSelection();
	}
}
