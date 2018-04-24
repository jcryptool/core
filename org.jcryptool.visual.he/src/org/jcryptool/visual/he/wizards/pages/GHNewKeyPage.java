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
import org.jcryptool.visual.he.algo.FHEParams;
import org.jcryptool.visual.he.algo.GHKeyGen;
import org.jcryptool.visual.he.algo.GHKeyPair;

/**
 * Page to generate a new key for Gentry & Halevi fully homomorphic visualization
 * @author Coen Ramaekers
 *
 */
public class GHNewKeyPage extends WizardPage {
	private static final String PAGENAME = "New key page"; //$NON-NLS-1$
	private static final String TITLE=Messages.GHNewKeyPage_Title;
	private final GHKeyPair keyPair;
	private final FHEParams fheParams;
	private final Display display;
	private Text detText, rootText, vectorText, pkBlocksText;
	private Combo dim = null;
	private Button genButton, yesButton, noButton;
	private Job keyGenJob;
	private Runnable jobDone;
	private Label own, pass;

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
				keyPair.setContactName(owner.getText());
				keyPair.setPassword(password.getText());
				passMatch = true;
				setPageComplete(keySet);
				setErrorMessage(null);
			} else {
				setErrorMessage(Messages.RSASaveKeypairPage_error_passwords_mismatch);
				keyPair.setPassword(null);
				keyPair.setContactName(null);
				setPageComplete(false);
				passMatch = false;
			}
		}
	};

	public GHNewKeyPage(GHKeyPair keyPair, FHEParams fheParams, Display display) {
		super(PAGENAME, TITLE, null);
		this.setDescription(Messages.GHNewKeyPage_Message);
		this.keyPair = keyPair;
		this.fheParams = fheParams;
		this.display = display;
		setPageComplete(false);

		/**
		 * Job such that the key generation can be canceled and progress can be viewed,
		 * this operation takes some time
		 */
		keyGenJob = new Job(Messages.HEComposite_KeyGen_Job_Name) {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				try {
					monitor.beginTask(Messages.HEComposite_Add_Task_Name, 100);
					new GHKeyGen(GHNewKeyPage.this.fheParams, GHNewKeyPage.this.keyPair, monitor, 100);
					if (GHNewKeyPage.this.keyPair.set) {
						GHNewKeyPage.this.display.asyncExec(jobDone);
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
		GridLayout gd_composite = new GridLayout(2, false);
		gd_composite.horizontalSpacing = 20;
		gd_composite.verticalSpacing = 10;
		composite.setLayout(gd_composite);

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
		dim.add("4", 1);
		dim.add("8", 2);
		dim.add("16", 3);
		dim.add("32", 4);
		dim.add("64", 5);
		dim.select(1);

		genButton = new Button(dimensionComposite, SWT.PUSH);
		genButton.setText(Messages.GHNewKeyPage_Generate);
		GridData gd_genButton = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		genButton.setLayoutData(gd_genButton);
		genButton.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		if (dim.getSelectionIndex() != 0) {
        			fheParams.setPrms(72, 384, (dim.getSelectionIndex() + 1));
        			keyGenJob.setUser(true);
        			keyGenJob.schedule();
        		}
        	}
        });

		Label lblDet = new Label(composite, SWT.NONE);
		lblDet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		lblDet.setText(Messages.HEComposite_GH_KeyArea_Determinant);
		detText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		detText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		detText.setEditable(false);

        Label lblRoot = new Label(composite, SWT.NONE);
		lblRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		lblRoot.setText(Messages.HEComposite_GH_KeyArea_Root);
		rootText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		rootText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		rootText.setEditable(false);

        Label lblBlocks = new Label(composite, SWT.NONE);
		lblBlocks.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		lblBlocks.setText(Messages.HEComposite_GH_KeyArea_Public_Key_Blocks);
		pkBlocksText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd_kpBlocksText = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_kpBlocksText.heightHint = 50;
		pkBlocksText.setLayoutData(gd_kpBlocksText);
		pkBlocksText.setEditable(false);

        Label lblVector = new Label(composite, SWT.NONE);
		lblVector.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		lblVector.setText(Messages.HEComposite_GH_KeyArea_Secret_Vector);
		vectorText = new Text(composite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd_vectorText = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_vectorText.heightHint = 50;
		vectorText.setLayoutData(gd_vectorText);
		vectorText.setEditable(false);

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
		own.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false));
		own.setVisible(false);
	
		pass = new Label(userDataComposite, SWT.WRAP);
		pass.setText(Messages.RSASaveKeypairPage_password);
		pass.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
		pass.setVisible(false);

		owner = new Text(userDataComposite, SWT.BORDER | SWT.SINGLE);
		owner.addModifyListener(ml);
		owner.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 2));
		owner.setVisible(false);
		
		password = new Text(userDataComposite, SWT.BORDER | SWT.PASSWORD);
		password.addModifyListener(ml);
		password.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		password.setVisible(false);
		
		passwordverify = new Text(userDataComposite, SWT.BORDER | SWT.PASSWORD);
		passwordverify.addModifyListener(ml);
		passwordverify.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		passwordverify.setVisible(false);

		/*combo = new Combo(subComposite, SWT.READ_ONLY);
        combo.setLayoutData(combord);
        combo.add(Messages.GHNewKeyPage_Yes, 0);
        combo.add(Messages.GHNewKeyPage_No,1);
        combo.select(1);
        combo.addModifyListener(new ModifyListener() {
        	public void modifyText(final ModifyEvent e) {
        		getContainer().updateButtons();
        	}
        });*/
		setControl(composite);
	}

	public void keySet() {
		detText.setText(keyPair.det.toString());
		rootText.setText(keyPair.root.toString());
		//wText.setText(keyPair.w.toString());
		vectorText.setVisible(false);
		for (int i = 0; i < keyPair.ctxts.length; i++) {
        	vectorText.append(i + ": " + keyPair.ctxts[i].toString());
        	if (i != keyPair.ctxts.length-1) vectorText.append(vectorText.getLineDelimiter());
        }
		vectorText.setVisible(true);
		vectorText.append(vectorText.getLineDelimiter());
		for (int i = 0; i < keyPair.pkBlocksX.length; i++) {
        	pkBlocksText.append(i + ": " + keyPair.pkBlocksX[i].toString());
        	pkBlocksText.append(pkBlocksText.getLineDelimiter());
        }
		keySet = true;
		setPageComplete(!getSave());
	}

	@Override
	public final IWizardPage getNextPage() {
			return null;
	}

	public boolean getSave() {
		return yesButton.getSelection();
	}

	public static String getPagename() {
		return PAGENAME;
	}
}
