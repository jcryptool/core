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
	private static final String TITLE=Messages.NewKeyPage_Title;
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
		this.setDescription(Messages.NewKeyPage_Message);
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
		Composite mainComposite = new Composite(parent, SWT.NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(110,30);
		final RowData combord = new RowData(150,18);
		final RowData textrd = new RowData(347,18);
		final RowData smalltextrd = new RowData(200,18);
		final RowData textmultird = new RowData(330,50);
		Label label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_Dimension);
		dim = new Combo(subComposite, SWT.READ_ONLY);
		dim.setLayoutData(combord);
		dim.add("", 0);
		dim.add("4", 1);
		dim.add("8", 2);
		dim.add("16", 3);
		dim.add("32", 4);
		dim.add("64", 5);
		dim.select(1);

		label = new Label(subComposite, SWT.NONE);
		label.setSize(50,22);

		subComposite = new Composite(mainComposite, SWT.NONE);
		subComposite.setLayout(srl);

		label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);

		genButton = new Button(subComposite, SWT.PUSH);
		genButton.setText(Messages.GHNewKeyPage_Generate);
		genButton.setLayoutData(new RowData(180,22));
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

		subComposite = new Composite(mainComposite, SWT.NONE);
		subComposite.setLayout(srl);
		label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_KeyArea_Determinant);
		detText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		detText.setLayoutData(textrd);
		detText.setEditable(false);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_KeyArea_Root);
		rootText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		rootText.setLayoutData(textrd);
		rootText.setEditable(false);

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_KeyArea_Public_Key_Blocks);
		pkBlocksText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		pkBlocksText.setLayoutData(textmultird);
		pkBlocksText.setEditable(false);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.NONE);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_KeyArea_Secret_Vector);
		vectorText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		vectorText.setLayoutData(textmultird);
		vectorText.setEditable(false);

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
		setControl(mainComposite);
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
