package net.sourceforge.javahexeditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.colors.ColorService;

public class CopyDialog extends Dialog {
	
	/**
	 * True, if the text is to big for the clipcoard and can not be copied.</br>
	 * False, if the text is less than 4 MB and can be copied in the clipboard.
	 */
	private boolean toBigForClipboard;

	/**
	 * Opens a dialog where you can choose whether to copy the hex values or
	 * the text representation. 
	 * @param parentShell Most likely Display.getCurrent().getActiveShell()
	 * @param allowHexCopy Whether the selected lenght is less than 4MB. If it 
	 * is more it would not fit into the clipboard.
	 */
	protected CopyDialog(Shell parentShell, boolean toBigForClipboard) {
		super(parentShell);
//		this.getShell().setText("Kopieroptionen");
		this.toBigForClipboard = toBigForClipboard;
		
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Texts.CopyDialog_title);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite container = (Composite) super.createDialogArea(parent);
		Text t = new Text(container, SWT.MULTI | SWT.WRAP);
		GridData gd_t = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_t.widthHint = 600;
		t.setLayoutData(gd_t);
		
		t.setText(Texts.CopyDialog_hex_or_text);
		t.setEditable(false);
		if (toBigForClipboard) {
			t.setText(Texts.CopyDialog_to_long_for_clipboard);
			t.setForeground(ColorService.RED);
		}

		return container;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		
		if (toBigForClipboard) {
			// Creates the "ok" Button
			createButton(parent, 1, "Ok", true); //$NON-NLS-1$
		} else {
			// Creates the "Hex" Button
			createButton(parent, 2, "Hex", false); //$NON-NLS-1$
			// Creates the "Text" Button.
			createButton(parent, 3, "Text", true); //$NON-NLS-1$
			
		}

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
