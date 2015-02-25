package org.jcryptool.crypto.keystore.ui.wizardpages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class BackupRestorePage extends WizardPage {
	private static final String PAGENAME = "BackupRestorePage";

	private Button backupButton;
	private Button restoreButton;
	private boolean restoreRequested = false;
	
	public BackupRestorePage() {
		super(getPageName());
		// TODO Auto-generated constructor stub
	}

	public BackupRestorePage(String title, ImageDescriptor titleImage) {
		super(getPageName(), title, titleImage);
		// TODO Auto-generated constructor stub
	}

	private static final String getPageName() {
		return PAGENAME;
	}
	
	public boolean getRestoreRequested() {
		return restoreRequested;
	}
	
    /** the selection listener which updates the buttons when changing from keypair to pubkey and vice versa. */
    private final SelectionListener sl = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent e) {
            getContainer().updateButtons();
            restoreRequested = restoreButton.getSelection();
        }
    };

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));

        Text text = new Text(composite, 0);
        text.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        text.setText("You can choose to back up or to restore the JCrypTool keystore to or from a file.");
        text.setEnabled(false);
        
        backupButton = new Button(composite, SWT.RADIO);
        backupButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        backupButton.setText("Back up the keystore");
        backupButton.setToolTipText("Back up the keystore to a file");
        backupButton.addSelectionListener(sl);

        restoreButton = new Button(composite, SWT.RADIO);
        restoreButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        restoreButton.setText("Restore the keystore");
        restoreButton.setToolTipText("Restore the keystore from a file");
        restoreButton.addSelectionListener(sl);
        
		setControl(composite);
	}

}
