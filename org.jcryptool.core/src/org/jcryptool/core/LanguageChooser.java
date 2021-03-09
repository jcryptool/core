package org.jcryptool.core;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LanguageChooser extends Dialog {
	public Label lblNewLabel;
	public Button btnEnglish;
	public Button btnGerman;
	protected String nl = "en";

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public LanguageChooser(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));
		
		
		lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("JCrypTool is only available in English and German; which language do you prefer?");
		
		btnEnglish = new Button(container, SWT.RADIO);
		btnEnglish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LanguageChooser.this.nl="en";
				LanguageChooser.this.updateDialog();
			}
		});
		btnEnglish.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnEnglish.setText("Restart JCrypTool in English");
		btnEnglish.setSelection(true);
		
		btnGerman = new Button(container, SWT.RADIO);
		btnGerman.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnGerman.setText("Starte JCrypTool auf Deutsch");
		btnGerman.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LanguageChooser.this.nl="de";
				LanguageChooser.this.updateDialog();
			}
		});

		return container;
	}

	protected void updateDialog() {
		// nothing to do for now; any state is valid...
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
//		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 300);
	}

}
