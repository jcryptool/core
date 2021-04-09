package net.sourceforge.javahexeditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.images.ImageService;

public class PasteDialog extends Dialog {

	protected PasteDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Texts.PasteDialog_title);
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		
		container.setLayout(new GridLayout(2, false));
		
		Text t = new Text(container, SWT.MULTI | SWT.WRAP);
		GridData gd_t = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_t.widthHint = 600;
		t.setLayoutData(gd_t);
		t.setEditable(false);
		t.setText(Texts.PasteDialog_hexOrText);
		
//		Information Icon
		new Label(container, SWT.NONE).setImage(ImageService.ICON_INFO);
		
		
		Text note = new Text(container, SWT.MULTI | SWT.WRAP);
		GridData gd_note = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_note.widthHint = 600;
		note.setLayoutData(gd_note);
		note.setForeground(ColorService.GRAY);
		note.setEditable(false);
		note.setText(Texts.PasteDialog_Information);

		return container;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		// Creates the cancel button.
		createButton(parent, 1, Texts.PasteDialog_cancel, false);
		// Creates the "Hex" Button
		createButton(parent, 2, "Hex", false); //$NON-NLS-1$
		// Creates the "Text" Button.
		createButton(parent, 3, "Text", true); //$NON-NLS-1$

	}

	
	@Override
	protected void buttonPressed(int buttonId) {
		// The user pressed the "Hex" Button
		if (buttonId == 2) {
			setReturnCode(2);
		}

		// The user pressed the "Text" Button
		if (buttonId == 3) {
			setReturnCode(3);
		}
		
		// Close the dialog.
		close();
	}
	
}
