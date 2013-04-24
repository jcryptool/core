package org.jcryptool.visual.sig.ui.wizards;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class InputWizard extends Wizard {
	
	// The pages of the wizard to select a document or to enter a text
	private InputWizardPage page;
	private InputEditorWizardPage pageEditor;

	// Constructor
	public InputWizard() {
		super();
	}

	@Override
	public void addPages() {
		// Create and add pages
		page = new InputWizardPage("Input Wizard");
		addPage(page);

		pageEditor = new InputEditorWizardPage("InputEditor Wizard");
		addPage(pageEditor);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
