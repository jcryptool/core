package org.jcryptool.visual.sig.ui.wizards;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

public class InputWizard extends Wizard{
	//The only page of the wizard (for selecting the Hash method)
	private InputWizardPage page;
	private InputEditorWizardPage pageEditor;
	
	private File file;
	private String content;
	
	//Constructor
	public InputWizard() {
		super();
	}	
	
	@Override
	public void addPages() {
		//Create and add pages
		page = new InputWizardPage("Input Wizard");
		addPage(page);
		
		pageEditor = new InputEditorWizardPage("InputEditor Wizard");
		addPage(pageEditor);

	}

	@Override
	public boolean performFinish() {
		if (page.fileChoosen()) {

			String strFile = null;
			FileDialog fd = new FileDialog(Display.getCurrent()
					.getActiveShell(), SWT.OPEN);
			fd.setFilterExtensions(new String[] { "*.txt", "*.*" });
			fd.setText("Open file");
			strFile = fd.open();

			file = new File(strFile);
		} else {
			content = pageEditor.getContent();
		}
		return true;
	}

	public String getContent() {
		return content;
	}
}
