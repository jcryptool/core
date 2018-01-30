// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards.pages;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.Paillier;
import org.jcryptool.visual.he.algo.PaillierData;

/**
 * Page to generate a new key for Paillier homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class PaillierNewKeyPage extends WizardPage {
	private static final String PAGENAME = "New paillier key page";
	private static final String TITLE=Messages.PaillierNewKeyPage_Title;
	private final PaillierData data;
	private Combo dim;
	private Button genButton, yesButton, noButton;
	private Job keyGenJob;
	private final Display display;
	private Text nText, gText, lText, mText;
	private Label own, pass;
	private Runnable jobDone;

	/** field for the owner of this keypair. */
	protected Text owner;

	/** field for the password. */
	private Text password;

	/** password verification field. */
	private Text passwordverify;

	/** boolean to check if the key is set*/
	private boolean keySet = false;

	/** boolean to check whether the passwords match*/
	private boolean passMatch = false;

	/** modifyListener for the fields. */
	private final ModifyListener ml = new ModifyListener() {

		public void modifyText(final ModifyEvent e) {
			final boolean pwmatch = password.getText().equals(passwordverify.getText());
			setPageComplete(!owner.getText().equals("") && !password.getText().equals("") //$NON-NLS-1$ //$NON-NLS-2$
					&& pwmatch);
			if (pwmatch) {
				data.setContactName(owner.getText());
				data.setPassword(password.getText());
				passMatch = true;
				setPageComplete(keySet);
				setErrorMessage(null);
			} else {
				setErrorMessage(Messages.RSASaveKeypairPage_error_passwords_mismatch);
				data.setPassword(null);
				data.setContactName(null);
				setPageComplete(false);
				passMatch = false;
			}
		}
	};



	public PaillierNewKeyPage(PaillierData data, Display display) {
		super(PAGENAME, TITLE, null);
		this.setDescription(Messages.PaillierNewKeyPage_Message);
		this.data = data;
		this.display = display;
		setPageComplete(false);

		keyGenJob = new Job(Messages.HEComposite_KeyGen_Job_Name) {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				try {
					monitor.beginTask(Messages.HEComposite_Add_Task_Name, 100);
					Paillier.keyGen(PaillierNewKeyPage.this.data);
					if (PaillierNewKeyPage.this.data.set) {
						PaillierNewKeyPage.this.display.asyncExec(jobDone);
					}
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
				} catch (final Exception ex) {
					LogUtil.logError(ex);
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};

		jobDone = new Runnable() {
			public void run() {
				keySet();
			}
		};
	}

	public final void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.horizontalSpacing = 20;
		gl_composite.verticalSpacing = 10;
		composite.setLayout(gl_composite);

		Label lblDimension = new Label(composite, SWT.NONE);
		lblDimension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		lblDimension.setText(Messages.HEComposite_GH_Dimension);
		
		Composite dimensionComposite = new Composite(composite, SWT.NONE);
		GridData gd_dimensionComposite = new GridData(SWT.FILL, SWT.FILL, true, false);
		GridLayout gl_dimensionComposite = new GridLayout(2, false);
		gl_dimensionComposite.verticalSpacing = 0;
		gl_dimensionComposite.horizontalSpacing = 10;
		dimensionComposite.setLayoutData(gd_dimensionComposite);
		dimensionComposite.setLayout(gl_dimensionComposite);
		
		dim = new Combo(dimensionComposite, SWT.READ_ONLY);
		GridData gd_dim = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd_dim.widthHint = 100;
		dim.setLayoutData(gd_dim);
		dim.add("", 0);
		dim.add("256", 1);
		dim.add("512", 2);
		dim.add("1024", 3);
		dim.add("2048", 4);
		dim.add("4096", 5);
		dim.select(1);

		genButton = new Button(dimensionComposite, SWT.PUSH);
		genButton.setText(Messages.GHNewKeyPage_Generate);
		GridData gd_genButton = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		genButton.setLayoutData(gd_genButton);
		genButton.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		if (dim.getSelectionIndex() != 0) {
        			data.setS(1<<(dim.getSelectionIndex()+7));
        			keyGenJob.setUser(true);
        			keyGenJob.schedule();
        		}
        	}
        });

		Label lblN = new Label(composite, SWT.NONE);
		lblN.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		lblN.setText("N:");
		nText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		nText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		nText.setEditable(false);

        Label lblG = new Label(composite, SWT.NONE);
        lblG.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblG.setText("g:");
		gText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		gText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		gText.setEditable(false);

        Label lblL= new Label(composite, SWT.NONE);
        lblL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblL.setText("l:");
		lText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		lText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		lText.setEditable(false);

        Label lblMu = new Label(composite, SWT.NONE);
        lblMu.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblMu.setText("mu:");
		mText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		mText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		mText.setEditable(false);

        Label lblSave = new Label(composite, SWT.NONE);
        lblSave.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        lblSave.setText(Messages.GHNewKeyPage_Save);

        Composite radioButtons = new Composite(composite, SWT.NONE);
        GridLayout gl_radioButtons = new GridLayout(2, false);
        gl_radioButtons.verticalSpacing = 0;
        gl_radioButtons.horizontalSpacing = 10;
        radioButtons.setLayout(gl_radioButtons);
        radioButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		yesButton = new Button(radioButtons, SWT.RADIO);
		yesButton.setText(Messages.GHNewKeyPage_Yes);
		yesButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		noButton = new Button(radioButtons, SWT.RADIO);
		noButton.setText(Messages.GHNewKeyPage_No);
		noButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		noButton.setSelection(true);
		noButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().updateButtons();
				own.setVisible(false);
				owner.setVisible(false);
				pass.setVisible(false);
				password.setVisible(false);
				passwordverify.setVisible(false);
				setPageComplete(keySet);
			}
		});
		yesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getContainer().updateButtons();
				own.setVisible(true);
				owner.setVisible(true);
				owner.setFocus();
				pass.setVisible(true);
				password.setVisible(true);
				passwordverify.setVisible(true);
				setPageComplete(passMatch);
			}
		});

		Composite userDataComposite = new Composite(composite, SWT.NONE);
		GridData gd_userDataComposite = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_userDataComposite.widthHint = 600;
		userDataComposite.setLayoutData(gd_userDataComposite);
		GridLayout gl_userDataComposite = new GridLayout(2, true);
		gl_userDataComposite.horizontalSpacing = 10;
		userDataComposite.setLayout(gl_userDataComposite);
		own = new Label(userDataComposite, SWT.WRAP);
		own.setText(Messages.RSASaveKeypairPage_name);
		GridData gd_own = new GridData(SWT.LEFT, SWT.BOTTOM, true, false);
		own.setLayoutData(gd_own);
		own.setVisible(false);
		
		pass = new Label(userDataComposite, SWT.WRAP);
		pass.setText(Messages.RSASaveKeypairPage_password);
		GridData gd_pass = new GridData(SWT.LEFT, SWT.FILL, true, false);
		pass.setLayoutData(gd_pass);
		pass.setVisible(false);

		owner = new Text(userDataComposite, SWT.BORDER | SWT.SINGLE);
		owner.addModifyListener(ml);
		owner.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 2));
		owner.setVisible(false);
		
		password = new Text(userDataComposite, SWT.BORDER | SWT.PASSWORD);
		password.addModifyListener(ml);
		password.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		password.setVisible(false);
		
		passwordverify = new Text(userDataComposite, SWT.BORDER | SWT.PASSWORD);
		passwordverify.addModifyListener(ml);
		passwordverify.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		passwordverify.setVisible(false);

		setControl(composite);
	}

	@Override
	public final IWizardPage getNextPage() {
		return null;
	}

	public void keySet() {
		nText.setText(data.getPubKey()[0].toString());
		gText.setText(data.getPubKey()[1].toString());
		lText.setText(data.getPrivKey()[0].toString());
		mText.setText(data.getPrivKey()[1].toString());
		keySet = true;
		setPageComplete(!getSave());
	}

	public boolean getSave() {
		return yesButton.getSelection();
	}

	public static String getPagename() {
		return PAGENAME;
	}
}
