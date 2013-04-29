package org.jcryptool.visual.sig.ui.wizards;

import java.io.File;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class InputFileComposite extends Composite implements PaintListener, SelectionListener {
	
	private Text txtPath;
	File file = null;
	private InputFileWizardPage page;

	public InputFileComposite(Composite parent, int style, InputFileWizardPage p) {
		super(parent, style);
		
		txtPath = new Text(this, SWT.BORDER);
		txtPath.setBounds(10, 10, 323, 19);
		
		Button btnBrowse = new Button(this, SWT.NONE);
		btnBrowse.setBounds(339, 6, 94, 28);
		btnBrowse.setText(Messages.InputFileWirard_btnBrowse);
		btnBrowse.addSelectionListener(this);
		
		page = p;
	}

	@Override
	public void paintControl(PaintEvent e) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			String strFile = null;
			FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
			//fd.setFilterExtensions(new String[] { "*.txt", "*.*" });
			fd.setText(Messages.InputWizard_FileOpenDialog);
			strFile = fd.open();
			file = new File(strFile);
			txtPath.setText(file.getAbsolutePath());
			page.setPageComplete(true);
		}
		catch (Exception ex) {}
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		
	}
	
	
}
