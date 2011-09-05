// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.wizards;

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
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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
	private static final String TITLE=Messages.NewKeyPage_Title;
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
		Composite mainComposite = new Composite(parent, SWT.NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(85,18);
		final RowData textrd = new RowData(330,18);
		final RowData combord = new RowData(150,18);
		final RowData smalltextrd = new RowData(200,18);

		Label label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_Dimension);
		dim = new Combo(subComposite, SWT.READ_ONLY);
		dim.setLayoutData(combord);
		dim.add("", 0);
		dim.add("256", 1);
		dim.add("512", 2);
		dim.add("1024", 3);
		dim.add("2048", 4);
		dim.add("4096", 5);
		dim.select(1);

		label = new Label(subComposite, SWT.NONE);
		label.setSize(50,22);

		genButton = new Button(subComposite, SWT.PUSH);
		genButton.setText(Messages.GHNewKeyPage_Generate);
		genButton.setLayoutData(new RowData(130,22));
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

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
		label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText("n:");
		nText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		nText.setLayoutData(textrd);
		nText.setEditable(false);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText("g:");
		gText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		gText.setLayoutData(textrd);
		gText.setEditable(false);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText("l:");
		lText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		lText.setLayoutData(textrd);
		lText.setEditable(false);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText("mu:");
		mText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		mText.setLayoutData(textrd);
		mText.setEditable(false);

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.GHNewKeyPage_Save);

		yesButton = new Button(subComposite, SWT.RADIO);
		yesButton.setText(Messages.GHNewKeyPage_Yes);
		noButton = new Button(subComposite, SWT.RADIO);
		noButton.setText(Messages.GHNewKeyPage_No);
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
				pass.setVisible(true);
				password.setVisible(true);
				passwordverify.setVisible(true);
				setPageComplete(passMatch);
			}
		});

		subComposite = new Composite(mainComposite, SWT.NONE);
	    subComposite.setLayout(new RowLayout());


	    Composite subSubComposite = new Composite(subComposite, SWT.NONE);
	    subSubComposite.setLayout(new RowLayout(SWT.VERTICAL));
	    final RowData twolinelabelrd = new RowData(200,36);
		own = new Label(subSubComposite, SWT.NONE);
		own.setText(Messages.RSASaveKeypairPage_name);
		own.setLayoutData(twolinelabelrd);
		own.setVisible(false);

		owner = new Text(subSubComposite, SWT.BORDER | SWT.SINGLE);
		owner.addModifyListener(ml);
		owner.setLayoutData(smalltextrd);
		owner.setVisible(false);

		subSubComposite = new Composite(subComposite, SWT.NONE);
	    subSubComposite.setLayout(new RowLayout(SWT.VERTICAL));

		pass = new Label(subSubComposite, SWT.NONE);
		pass.setText(Messages.RSASaveKeypairPage_password);
		pass.setLayoutData(twolinelabelrd);
		pass.setVisible(false);

		password = new Text(subSubComposite, SWT.BORDER | SWT.PASSWORD);
		password.addModifyListener(ml);
		password.setLayoutData(smalltextrd);
		password.setVisible(false);
		passwordverify = new Text(subSubComposite, SWT.BORDER | SWT.PASSWORD);
		passwordverify.addModifyListener(ml);
		passwordverify.setLayoutData(smalltextrd);
		passwordverify.setVisible(false);

		setControl(mainComposite);
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
