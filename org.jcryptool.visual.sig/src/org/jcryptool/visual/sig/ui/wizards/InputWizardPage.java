package org.jcryptool.visual.sig.ui.wizards;

//import org.eclipse.jface.wizard.IWizardPage;
import java.io.File;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class InputWizardPage extends WizardPage {

	private InputComposite composite;
	File file = null;

	// Constructor
	protected InputWizardPage(String pageName) {
		super(pageName);
		setTitle(Messages.InputWizard_title);
		setDescription(Messages.InputWizard_header);
	}

	public void createControl(Composite parent) {
		composite = new InputComposite(parent, NONE);
		// composite.setBounds(x, y, width, height);
		setControl(composite);
		setPageComplete(false);
	}

	/*
	 * public IWizardPage getNextPage() { if
	 * (composite.rdoFromFile.getSelection()) { return
	 * getWizard().getPage("file"); } return getWizard().getPage("editor");
	 * 
	 * }
	 */

	public boolean fileChoosen() {
		return composite.rdoFromFile.getSelection();
	}

	public IWizardPage openFileDialog() {
		String strFile = null;
		FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(),
				SWT.OPEN);
		// fd.setFilterExtensions(new String[] { "*.txt", "*.*" });
		fd.setText("Open file");
		strFile = fd.open();
		file = new File(strFile);
		return null;
	}

	public IWizardPage getNextPage() {
		if (fileChoosen()) {
			return openFileDialog();
		} else {
			return getWizard().getPage("InputEditor Wizard");
		}
	}

	public boolean canFlipToNextPage() {
		return true;
	}
}
