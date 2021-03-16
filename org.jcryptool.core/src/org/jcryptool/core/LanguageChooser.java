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
	private Label lblNewLabel2;

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
		getShell().setText("Choose your language");
		
		lblNewLabel = new Label(container, SWT.WRAP);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNewLabel.setText("JCrypTool is available only in English and German. Which language do you prefer?\nYou may change the language later in 'Window -> Settings -> JCT General'.");

		lblNewLabel2 = new Label(container, SWT.WRAP);
		lblNewLabel2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblNewLabel2.setText("JCrypTool ist nur auf Englisch und Deutsch verfügbar. Welche Sprache möchten Sie nutzen? \nSie können die Sprache später ändern im Menü 'Fenster -> Preferences -> JCT Allgemein'.");
		
		
		btnEnglish = new Button(container, SWT.RADIO);
		btnEnglish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LanguageChooser.this.nl = "en";
			}
		});
		btnEnglish.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnEnglish.setText("Start JCrypTool in English");
		btnEnglish.setSelection(true);
		
		btnGerman = new Button(container, SWT.RADIO);
		btnGerman.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnGerman.setText("Starte JCrypTool auf Deutsch");
		btnGerman.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LanguageChooser.this.nl = "de";
			}
		});

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
//      createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false); // disable CANCEL button
	}

	
}
