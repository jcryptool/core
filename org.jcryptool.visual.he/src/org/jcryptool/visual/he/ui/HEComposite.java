// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.he.ui;

import java.math.BigInteger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.he.Messages;
import org.jcryptool.visual.he.algo.FHEParams;
import org.jcryptool.visual.he.algo.Functions;
import org.jcryptool.visual.he.algo.GHData;
import org.jcryptool.visual.he.algo.GHDecrypt;
import org.jcryptool.visual.he.algo.GHEncrypt;
import org.jcryptool.visual.he.algo.GHKeyPair;
import org.jcryptool.visual.he.algo.Paillier;
import org.jcryptool.visual.he.algo.PaillierData;
import org.jcryptool.visual.he.algo.RSAData;
import org.jcryptool.visual.he.rsa.Action;
import org.jcryptool.visual.he.wizards.GHInitialTextWizard;
import org.jcryptool.visual.he.wizards.GHKeySelectionWizard;
import org.jcryptool.visual.he.wizards.GHModulusWizard;
import org.jcryptool.visual.he.wizards.GHOperationTextWizard;
import org.jcryptool.visual.he.wizards.GHSettingsWizard;
import org.jcryptool.visual.he.wizards.PaillierInitialTextWizard;
import org.jcryptool.visual.he.wizards.PaillierKeySelectionWizard;
import org.jcryptool.visual.he.wizards.PaillierOperationTextWizard;
import org.jcryptool.visual.he.wizards.RSAInitialTextWizard;
import org.jcryptool.visual.he.wizards.RSAKeySelectionWizard;
import org.jcryptool.visual.he.wizards.RSAOperationTextWizard;


/**
 * Composite to display homomorphic encryption schemes
 *
 * @author Coen Ramaekers
 */

public class HEComposite extends Composite {
	/** Yellow */
	public Color YELLOW;

	/** Holds the tab choice */
	public int tabChoice;

	/** Handles the tab choice */
	public final int GENTRY_HALEVI = 1, RSA = 2, PAILLIER = 3;

	/** Button for running the key selection wizard */
	private Button keySel;

	/** Combo box to select field size */
	//private Combo modulus;
	private Button modulusSel;

	/** Button for running initial text selection wizard */
	private Button initTextSel;

	/** Buttons to run homomorphic operations */
	private Button homomorphMult, homomorphAdd;

	/** Decrypt button */
	private Button decryptButton;

	/** Reset buttons */
	private Button resetNumButton, resetAllButton;

	/** Settings button */
	private Button settingsButton;

	/** Keypair which is to be used */
	private GHKeyPair keyPair = new GHKeyPair();

	/** Holds the FHE Parameters*/
	private FHEParams fheParams = new FHEParams();

	/** Textboxes to display public and secret key for GH */
	private Text detText, rootText, cText, pkBlockText;

	/** Textboxes to display key info for RSA*/
	private Text eText, nText;

	/** Textboxes to display key info for Paillier */
	private Text gText;

	/** Log_2 of modulus to be used in calculations */
	private int logMod;

	/** Will hold data in wizards */
	private GHData data = new GHData();

	/** Will hold RSA data in wizards */
	private RSAData rsaEncData = new RSAData(Action.EncryptAction), rsaDecData = new RSAData(Action.DecryptAction);

	/** Will hold Paillier data in wizards */
	private PaillierData paillierData = new PaillierData();

	private Text modulus;

	/** Textboxes to display initial number and encryption */
	private Text initialPlain, initialPlainBits, initialEncryptedBits;

	/** Textboxes to display homomorphic number and encryption */
	private Text homomorphPlain, homomorphPlainBits, homomorphEncryptedBits;

	/** Textboxes to display results of homomorphic operation */
	private Text homomorphResultPlain, homomorphResultPlainBits, homomorphResultEncryptedBits;

	/** Textboxes to display plain operations */
	private Text plainOperations, plainResult;

	/** String to restore operation text after cancel */
	private String oldOperationText;

	/** To restore first after cancel */
	private boolean oldFirst;

	/** GH Result number */
	private int ghResult, ghOldResult;

	/** RSA Result number */
	private BigInteger rsaResult;

	/** Paillier Result number */
	private BigInteger paillierResult;

	/** Initial number bit array */
	private int[] initialBits;

	/** Homomorphic number bit array */
	private int[] homomorphBits;

	/** Result number bit array */
	private int[] homomorphResultBits;

	/** Initial number encrypted */
	private BigInteger[] initialEncrypted;

	/** Homomorphic number encrypted */
	private BigInteger[] homomorphEncrypted;

	/** Result number encrypted */
	private BigInteger[] homomorphResultEncrypted;

	/** Job */
	private Job multJob, addJob;

	/** Runnable to use display */
	private Runnable jobDone, jobCanceled;

	/** Check whether the operation is the first, for parentheses */
	private boolean first = true;

	public HEComposite(final Composite parent, final int tabChoice, final int style) {
		super(parent,style);
		this.tabChoice = tabChoice;
		this.initialize();
		YELLOW = HEComposite.this.getDisplay().getSystemColor(SWT.COLOR_YELLOW);

		/**
		 * The job which will be run when the addition or multiplication job is done,
		 * homomorphResultEncrypted will hold the encrypted result of the operation,
		 * decryptButton must be enabled so the user can decrypt
		 */
		jobDone = new Runnable() {
			public void run() {
				homomorphResultEncrypted = data.getArray3();
				for (int i = 0; i < homomorphResultEncrypted.length; i++) {
					homomorphResultEncryptedBits.append(i + ": " + homomorphResultEncrypted[i].toString());
					homomorphResultEncryptedBits.append(homomorphResultEncryptedBits.getLineDelimiter());
				}
				decryptButton.setEnabled(true);
				detText.setBackground(ColorService.LIGHTGRAY);
				rootText.setBackground(ColorService.LIGHTGRAY);
				cText.setBackground(ColorService.LIGHTGRAY);
				pkBlockText.setBackground(ColorService.LIGHTGRAY);
				initialPlain.setBackground(ColorService.LIGHTGRAY);
				initialPlainBits.setBackground(ColorService.LIGHTGRAY);
				initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlain.setBackground(YELLOW);
				homomorphPlainBits.setBackground(YELLOW);
				homomorphEncryptedBits.setBackground(YELLOW);
				homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultEncryptedBits.setBackground(YELLOW);
				plainResult.setBackground(YELLOW);
				plainOperations.setBackground(YELLOW);
			}

		};

		/**
		 * The job which will be run when the addition or multiplication job is canceled,
		 * clears the text boxes holding the homomorphic operation,
		 * puts the old results back
		 */
		jobCanceled = new Runnable() {
			public void run() {
				homomorphPlain.setText("");
				homomorphPlainBits.setText("");
				homomorphEncryptedBits.setText("");
				ghResult = ghOldResult;
				plainOperations.setText(oldOperationText);
				plainResult.setText(Integer.toString(ghResult));
				first = oldFirst;
				detText.setBackground(ColorService.LIGHTGRAY);
				rootText.setBackground(ColorService.LIGHTGRAY);
				cText.setBackground(ColorService.LIGHTGRAY);
				pkBlockText.setBackground(ColorService.LIGHTGRAY);
				initialPlain.setBackground(ColorService.LIGHTGRAY);
				initialPlainBits.setBackground(ColorService.LIGHTGRAY);
				initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlain.setBackground(ColorService.LIGHTGRAY);
				homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
				plainResult.setBackground(ColorService.LIGHTGRAY);
				plainOperations.setBackground(ColorService.LIGHTGRAY);
			}
		};

		/**
		 * Job such that the homomorphic multiplication can be canceled and progress can be viewed,
		 * this operation takes some time
		 */
		multJob = new Job(Messages.HEComposite_Multiply_Job_Name) {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				try {
					monitor.beginTask(Messages.HEComposite_Multiply_Task_Name, 1000);
					BigInteger[] result = Functions.mulCiphertexts(data.getArray1(), data.getArray2(),
							fheParams, keyPair.det, keyPair.root, keyPair.pkBlocksX,
							keyPair.ctxts, keyPair, monitor, 1000, data);
					if (result == null) {
						HEComposite.this.getDisplay().asyncExec(jobCanceled);
						return Status.CANCEL_STATUS;
					} else {
						data.setArray3(result);
						HEComposite.this.getDisplay().asyncExec(jobDone);
					}
				} catch (final Exception ex) {
					LogUtil.logError(ex);
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};

		/**
		 * Job such that the homomorphic addition can be canceled and progress can be viewed,
		 * this operation takes some time
		 */
		addJob = new Job(Messages.HEComposite_Add_Job_Name) {
			@Override
			public IStatus run(final IProgressMonitor monitor) {
				try {
					monitor.beginTask(Messages.HEComposite_Add_Task_Name, 100);
					BigInteger[] result = Functions.addCiphertexts("", data.getArray1(), data.getArray2(),
							fheParams, keyPair.det, keyPair.root, keyPair.pkBlocksX,
							keyPair.ctxts, monitor, 100, data);
					if (result == null) {
						HEComposite.this.getDisplay().asyncExec(jobCanceled);
						return Status.CANCEL_STATUS;
					} else {
						data.setArray3(result);
						HEComposite.this.getDisplay().asyncExec(jobDone);
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
	}

	/**
	 * Creates the layout and calls the creation of the head, which holds the title and description,
	 * and the main composite, which holds the visualization
	 */
	private void initialize() {
		this.setLayout(new GridLayout());
		this.createHead();
		this.createMain();
	}

	/**
	 * Creates the head of the screen, holds the title and description
	 */
	private void createHead() {
		final Composite head = new Composite(this, SWT.NONE);
        head.setBackground(ColorService.WHITE);
        head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        head.setLayout(new GridLayout());

        final Label label = new Label(head, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(ColorService.WHITE);

        /** Deals with the choice of scheme */
        switch(tabChoice) {
	        case GENTRY_HALEVI:  label.setText(Messages.HEComposite_GentryHalevi_Title); break;
	        case RSA:  label.setText(Messages.HEComposite_RSA_Title); break;
	        case PAILLIER:  label.setText(Messages.HEComposite_Paillier_Title); break;
        }

        final StyledText stDescription = new StyledText(head, SWT.READ_ONLY);
        switch(tabChoice) {
	        case GENTRY_HALEVI:  stDescription.setText(Messages.HEComposite_GentryHalevi_Description); break;
	        case RSA:  stDescription.setText(Messages.HEComposite_RSA_Description); break;
	        case PAILLIER:  stDescription.setText(Messages.HEComposite_Paillier_Description); break;
        }
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the main area, subdivided into the button area and the algorithm area
	 */
	private void createMain() {
		final Group g = new Group(this, SWT.NONE);
		final GridLayout gl = new GridLayout(2, false);
        //gl.horizontalSpacing = HORIZONTAL_SPACING;
		g.setText(Messages.HEComposite_Scheme);
        g.setLayout(gl);
        g.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
        this.createButtonArea(g);
        this.createAlgoArea(g);
	}

	/**
	 * Creates the button area
	 * @param parent the composite in which it is created
	 */
	private void createButtonArea(final Composite parent) {
		final Composite mainComposite = new Composite(parent, SWT.SHADOW_NONE);
		mainComposite.setLayout(new GridLayout());
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		//final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		//final RowData buttonrd = new RowData(130,30);
		//final RowData textrd = new RowData(100,20);

		Group subComposite1= new Group(mainComposite, SWT.SHADOW_NONE);
		subComposite1.setText(Messages.HEComposite_Key);
		subComposite1.setLayout(new RowLayout(SWT.VERTICAL));

        this.keySel = new Button(subComposite1, SWT.PUSH);
        this.keySel.setLayoutData(new RowData(130,30));
        this.keySel.setBackground(ColorService.RED);
        this.keySel.setEnabled(true);
        this.keySel.setText(Messages.HEComposite_Keysel);
        this.keySel.setToolTipText(Messages.HEComposite_Key_Tooltip);
        this.keySel.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		/** Every scheme has a different type of key, so requires his own wizard */
        		switch(tabChoice) {
        			case GENTRY_HALEVI:	if (new WizardDialog(HEComposite.this.getShell(),
        					new GHKeySelectionWizard(keyPair, fheParams, HEComposite.this.getDisplay())).open() == Window.OK) keySelected(); break;
        			case RSA:	if (new WizardDialog(getShell(),
        					new RSAKeySelectionWizard(rsaEncData, false)).open() == Window.OK) keySelected(); break;
        			case PAILLIER:	if (new WizardDialog(getShell(),
        					new PaillierKeySelectionWizard(paillierData, HEComposite.this.getDisplay())).open() == Window.OK) keySelected(); break;
        		}
        	}
        });

        Composite spacerComposite = new Composite(mainComposite, SWT.NONE);
        Label spacerLabel = new Label(spacerComposite, SWT.NONE);
        switch (tabChoice) {
        	case GENTRY_HALEVI: spacerLabel.setSize(130, 82); break; // spacerLabel.setSize(130, 139); break;
        	case RSA: spacerLabel.setSize(130, 12); break;
        	case PAILLIER: spacerLabel.setSize(130, 12); break;
        }

        if (tabChoice == GENTRY_HALEVI) {
        	Group subComposite2 = new Group(mainComposite, SWT.SHADOW_NONE);
    		subComposite2.setText(Messages.HEComposite_Modulus);
    		subComposite2.setLayout(new RowLayout(SWT.VERTICAL));
        	this.modulusSel = new Button(subComposite2, SWT.PUSH);
            this.modulusSel.setLayoutData(new RowData(130,30));
            this.modulusSel.setBackground(ColorService.RED);
            this.modulusSel.setEnabled(false);
            this.modulusSel.setText(Messages.HEComposite_Keysel);
            this.modulusSel.setToolTipText(Messages.HEComposite_Modulus_Tooltip);
            this.modulusSel.addSelectionListener(new SelectionAdapter() {
            	@Override
    			public void widgetSelected(final SelectionEvent e) {
            		/** Every scheme has a different type of key, so requires his own wizard */
            		if (new WizardDialog(HEComposite.this.getShell(),
            					new GHModulusWizard(data)).open() == Window.OK) modulusSelected();
            	}
            });
        }

        Group subComposite3 = new Group(mainComposite, SWT.SHADOW_NONE);
		subComposite3.setText(Messages.HEComposite_Initial_Text);
		subComposite3.setLayout(new RowLayout(SWT.VERTICAL));
		this.initTextSel = new Button(subComposite3, SWT.PUSH);
		this.initTextSel.setToolTipText(Messages.HEComposite_Initial_Tooltip);
        this.initTextSel.setLayoutData(new RowData(130,30));
        this.initTextSel.setBackground(ColorService.RED);
        this.initTextSel.setEnabled(false);
        this.initTextSel.setText(Messages.HEComposite_Initial_Text_Select);
        this.initTextSel.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		/** Since the modulus is different for each scheme, they each require an own wizard*/
        		switch(tabChoice) {
        		case GENTRY_HALEVI: if (new WizardDialog(HEComposite.this.getShell(),
        				new GHInitialTextWizard(logMod, data)).open() == Window.OK) initialTextSelected(); break;
        		case RSA: if (new WizardDialog(HEComposite.this.getShell(),
        				new RSAInitialTextWizard(rsaEncData)).open() == Window.OK) initialTextSelected(); break;
        		case PAILLIER: if (new WizardDialog(HEComposite.this.getShell(),
        				new PaillierInitialTextWizard(paillierData)).open() == Window.OK) initialTextSelected(); break;
        		}
        	}
        });

        spacerComposite = new Composite(mainComposite, SWT.NONE);
        spacerLabel = new Label(spacerComposite, SWT.NONE);
        switch (tabChoice) {
	    	case GENTRY_HALEVI: spacerLabel.setSize(130, 96); break;
	    	case RSA: spacerLabel.setSize(130, 106); break;
	    	case PAILLIER: spacerLabel.setSize(130, 106); break;
	    }

        Group subComposite4 = new Group(mainComposite, SWT.SHADOW_NONE);
		subComposite4.setText(Messages.HEComposite_Homomorphic_Text);
		subComposite4.setLayout(new RowLayout(SWT.VERTICAL));

		/** Only RSA does not have homomorphic addition */
		if (tabChoice != RSA) {
	        this.homomorphAdd = new Button(subComposite4, SWT.PUSH);
	        this.homomorphAdd.setToolTipText(Messages.HEComposite_Homomorphic_Tooltip_Add);
	        this.homomorphAdd.setLayoutData(new RowData(130,30));
	        this.homomorphAdd.setEnabled(false);
	        this.homomorphAdd.setText(Messages.HEComposite_Homomorphic_Add_Select);
	        this.homomorphAdd.addSelectionListener(new SelectionAdapter() {

	        	@Override
				public void widgetSelected(final SelectionEvent e) {
	        		switch(tabChoice) {
	        		case GENTRY_HALEVI: if (new WizardDialog(HEComposite.this.getShell(),
	        				new GHOperationTextWizard(logMod, data)).open() == Window.OK) addTextSelected(); break;
	        		case PAILLIER: if (new WizardDialog(HEComposite.this.getShell(),
	        				new PaillierOperationTextWizard(paillierData)).open() == Window.OK) addTextSelected(); break;
	        		}
	        	}
	        });
        }

		/** Only Paillier does not have homomorphic multiplication */
		if (tabChoice != PAILLIER) {
	        this.homomorphMult = new Button(subComposite4, SWT.PUSH);
	        this.homomorphMult.setToolTipText(Messages.HEComposite_Homomorphic_Tooltip_Multiply);
	        this.homomorphMult.setLayoutData(new RowData(130,30));
	        this.homomorphMult.setEnabled(false);
	        this.homomorphMult.setText(Messages.HEComposite_Homomorphic_Mult_Select);
	        this.homomorphMult.addSelectionListener(new SelectionAdapter() {

	        	@Override
				public void widgetSelected(final SelectionEvent e) {
	        		switch(tabChoice) {
		        		case GENTRY_HALEVI: {
		        			if (new WizardDialog(HEComposite.this.getShell(),
		        					new GHOperationTextWizard(logMod, data)).open() == Window.OK) multTextSelected();
	        				break;
		        		}
		        		case RSA: {
		        			if (new WizardDialog(HEComposite.this.getShell(),
		        					new RSAOperationTextWizard(rsaEncData)).open() == Window.OK) multTextSelected();
	        				break;
		        		}
	        		}
	        	}
	        });
		}

		spacerLabel = new Label(subComposite4, SWT.NONE);
		spacerLabel.setSize(130,10);

        this.decryptButton = new Button(subComposite4, SWT.PUSH);
        this.decryptButton.setLayoutData(new RowData(130,30));
        this.decryptButton.setToolTipText(Messages.HEComposite_Decrypt_Tooltip);
        this.decryptButton.setEnabled(false);
        this.decryptButton.setText(Messages.HEComposite_Decrypt_Select);
        this.decryptButton.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		/**
        		 * For Gentry and Halevi and Pallier the decryption key is simultaneously generated,
        		 * for RSA it needs to be selected
        		 */
        		switch(tabChoice) {
	        		case GENTRY_HALEVI: decryptResult(); break;
	        		case RSA: {
	        			if (rsaDecData.getD() == null) {
	        				if (new WizardDialog(getShell(),
	        					new RSAKeySelectionWizard(rsaDecData, false)).open() == Window.OK) {
	        						//dText.setText(rsaDecData.getD().toString());
	        						decryptResult();
	        				}
	        			} else {
	        				decryptResult();
	        			}
        			} break;
	        		case PAILLIER: decryptResult(); break;
        		}

        	}
        });

        spacerComposite = new Composite(mainComposite, SWT.NONE);
        spacerLabel = new Label(spacerComposite, SWT.NONE);
        switch (tabChoice) {
	    	case GENTRY_HALEVI: spacerLabel.setSize(130, 78); break;
	    	case RSA: spacerLabel.setSize(130, 281); break;
	    	case PAILLIER: spacerLabel.setSize(130, 281); break;
	    }

        Group subComposite5 = new Group(mainComposite, SWT.SHADOW_NONE);
		subComposite5.setText(Messages.HEComposite_Reset_Text);
		subComposite5.setLayout(new RowLayout(SWT.VERTICAL));

		this.resetNumButton = new Button(subComposite5, SWT.PUSH);
		this.resetNumButton.setToolTipText(Messages.HEComposite_Reset_Numbers_Tooltip);
		this.resetNumButton.setLayoutData(new RowData(130,30));
		this.resetNumButton.setEnabled(false);
		this.resetNumButton.setText(Messages.HEComposite_Reset_Numbers);
		this.resetNumButton.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		resetNumbers();
        	}

        });

		this.resetAllButton = new Button(subComposite5, SWT.PUSH);
		this.resetAllButton.setToolTipText(Messages.HEComposite_Reset_All_Tooltip);
		this.resetAllButton.setLayoutData(new RowData(130,30));
		this.resetAllButton.setEnabled(false);
		this.resetAllButton.setText(Messages.HEComposite_Reset_All);
		this.resetAllButton.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		resetAll();
        	}

        });

		if (tabChoice == GENTRY_HALEVI) {
			Group subComposite6 = new Group(mainComposite, SWT.SHADOW_NONE);
			subComposite6.setText(Messages.HEComposite_Settings_Group);
			subComposite6.setLayout(new RowLayout(SWT.VERTICAL));

			this.settingsButton = new Button(subComposite6, SWT.PUSH);
			this.settingsButton.setToolTipText(Messages.HEComposite_Settings_Tooltip);
			this.settingsButton.setLayoutData(new RowData(130,30));
			this.settingsButton.setText(Messages.HEComposite_Settings);
			this.settingsButton.addSelectionListener(new SelectionAdapter() {

	        	@Override
				public void widgetSelected(final SelectionEvent e) {
	        		new WizardDialog(HEComposite.this.getShell(),
        					new GHSettingsWizard(data)).open();
	        	}

	        });

		}
	}

	/**
	 * If the key is selected this function is called,
	 * the key is displayed in the algorithm area,
	 * the reset all button is enabled,
	 * the next necessary user entry is enabled
	 */
	private void keySelected() {
		this.keySel.setBackground(ColorService.GREEN);
		this.keySel.setForeground(ColorService.RED);
		this.resetAllButton.setEnabled(true);
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				this.modulusSel.setEnabled(true);
				//this.modulus.setEnabled(true);
				detText.setText(keyPair.det.toString());
				rootText.setText(keyPair.root.toString());
				//wText.setText(keyPair.w.toString());
				cText.setVisible(false);
				for (int i = 0; i < keyPair.ctxts.length; i++) {
		        	cText.append(i + ": " + keyPair.ctxts[i].toString());
		        	if (i != keyPair.ctxts.length-1) cText.append(cText.getLineDelimiter());
		        }
				cText.setVisible(true);
				cText.append(cText.getLineDelimiter());
				for (int i = 0; i < keyPair.pkBlocksX.length; i++) {
		        	pkBlockText.append(i + ": " + keyPair.pkBlocksX[i].toString());
		        	pkBlockText.append(pkBlockText.getLineDelimiter());
		        }

				detText.setBackground(YELLOW);
				rootText.setBackground(YELLOW);
				cText.setBackground(YELLOW);
				pkBlockText.setBackground(YELLOW);
				initialPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
			} break;
			case RSA: {
		        this.initTextSel.setEnabled(true);
		        if (rsaEncData.getE() != null) {
		            eText.setText(rsaEncData.getE().toString());
		        }
		        /*if (rsaEncData.getD() != null) {
		            dText.setText(rsaEncData.getD().toString());
		        }*/
		        if (rsaEncData.getN() != null) {
		            nText.setText(rsaEncData.getN().toString());
		        }
		        eText.setBackground(YELLOW);
		        nText.setBackground(YELLOW);

			} break;
			case PAILLIER: {
		        this.initTextSel.setEnabled(true);
		        nText.setText(paillierData.getPubKey()[0].toString());
		        gText.setText(paillierData.getPubKey()[1].toString());
		        //lText.setText(paillierData.getPrivKey()[0].toString());
		        //mText.setText(paillierData.getPrivKey()[1].toString());

		        nText.setBackground(YELLOW);
		        gText.setBackground(YELLOW);
			} break;
		}
		initialPlain.setBackground(ColorService.LIGHTGRAY);
		initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		plainResult.setBackground(ColorService.LIGHTGRAY);
		plainOperations.setBackground(ColorService.LIGHTGRAY);

	}

	private void modulusSelected() {
		this.modulusSel.setBackground(ColorService.GREEN);
		logMod = data.getModulus();
		data.initCount(logMod);
		modulus.setText(Integer.toString((int)Math.pow(2,logMod)));
		detText.setBackground(ColorService.LIGHTGRAY);
		rootText.setBackground(ColorService.LIGHTGRAY);
		cText.setBackground(ColorService.LIGHTGRAY);
		pkBlockText.setBackground(ColorService.LIGHTGRAY);
		modulus.setBackground(YELLOW);
		initialPlain.setBackground(ColorService.LIGHTGRAY);
		initialPlainBits.setBackground(ColorService.LIGHTGRAY);
		initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
		homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
		homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		plainResult.setBackground(ColorService.LIGHTGRAY);
		plainOperations.setBackground(ColorService.LIGHTGRAY);
		initTextSel.setEnabled(true);
	}

	/**
	 * The initial number is selected and encrypted, both being displayed in the algorithm area
	 */
	private void initialTextSelected() {
		this.initTextSel.setBackground(ColorService.GREEN);
		this.resetNumButton.setEnabled(true);
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				this.homomorphAdd.setEnabled(true);
				this.homomorphMult.setEnabled(true);
				data.setData(data.getNumber()%(1<<logMod));
				this.ghResult = data.getNumber();
				this.initialPlain.setText(Integer.toString(data.getNumber()));
				this.initialBits = Functions.numToBits(data.getNumber(), logMod);
				this.initialPlainBits.setText(Functions.bitArrayToString(initialBits));
				this.initialEncrypted = GHEncrypt.encrypt(fheParams, keyPair, initialBits);
				this.homomorphResultEncrypted = initialEncrypted;
				for (int i = 0; i < this.initialEncrypted.length; i++) {
					this.initialEncryptedBits.append(i + ": " + this.initialEncrypted[i].toString());
					this.initialEncryptedBits.append(this.initialEncryptedBits.getLineDelimiter());
				}
				this.plainOperations.setText(Integer.toString(data.getNumber()));

				detText.setBackground(ColorService.LIGHTGRAY);
				rootText.setBackground(ColorService.LIGHTGRAY);
				cText.setBackground(ColorService.LIGHTGRAY);
				pkBlockText.setBackground(ColorService.LIGHTGRAY);
				modulus.setBackground(ColorService.LIGHTGRAY);
				initialPlainBits.setBackground(YELLOW);
				homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
			} break;
			case RSA: {
				this.homomorphMult.setEnabled(true);
				this.rsaResult = (new BigInteger(rsaEncData.getPlainText())).mod(rsaEncData.getN());
				this.initialPlain.setText(rsaEncData.getPlainText());
				this.initialEncrypted = new BigInteger[]{
						new BigInteger(rsaEncData.getPlainText()).modPow(rsaEncData.getE(), rsaEncData.getN())};
				this.initialEncryptedBits.setText(this.initialEncrypted[0].toString());
				this.homomorphResultEncrypted = initialEncrypted;
				this.plainOperations.setText(rsaEncData.getPlainText());
		        eText.setBackground(ColorService.LIGHTGRAY);
		        nText.setBackground(ColorService.LIGHTGRAY);
			} break;
			case PAILLIER: {
				this.homomorphAdd.setEnabled(true);
				this.paillierResult = paillierData.getPlain().mod(paillierData.getPubKey()[0]);
				this.initialPlain.setText(paillierData.getPlain().toString());
				Paillier.encrypt(paillierData);
				this.initialEncrypted = new BigInteger[]{paillierData.getCipher()};
				this.initialEncryptedBits.setText(this.initialEncrypted[0].toString());
				this.homomorphResultEncrypted = this.initialEncrypted;
				this.plainOperations.setText(paillierData.getPlain().toString());
		        nText.setBackground(ColorService.LIGHTGRAY);
		        gText.setBackground(ColorService.LIGHTGRAY);
			} break;
		}

		initialPlain.setBackground(YELLOW);
		initialEncryptedBits.setBackground(YELLOW);
		homomorphPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		plainResult.setBackground(ColorService.LIGHTGRAY);
		plainOperations.setBackground(ColorService.LIGHTGRAY);

	}

	/**
	 * The number to be added is entered, encrypted and the addition is evaluated both homomorphically
	 * and plain, the results appear in the text boxes in the algorithm area
	 */
	private void addTextSelected() {
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				data.setData(data.getNumber()%(1<<logMod));
				this.homomorphPlain.setText(Integer.toString(data.getNumber()));
				this.homomorphBits = Functions.numToBits(data.getNumber(), logMod);
				this.homomorphPlainBits.setText(Functions.bitArrayToString(homomorphBits));
				this.homomorphEncrypted = GHEncrypt.encrypt(fheParams, keyPair, homomorphBits);
				this.homomorphEncryptedBits.setText("");
				this.homomorphResultEncryptedBits.setText("");
				this.homomorphResultPlain.setText("");
				this.homomorphResultPlainBits.setText("");
				this.oldOperationText = this.plainOperations.getText();
				this.ghOldResult = this.ghResult;
				this.oldFirst = first;
				for (int i = 0; i < this.homomorphEncrypted.length; i++) {
					this.homomorphEncryptedBits.append(i + ": " + this.homomorphEncrypted[i].toString());
					this.homomorphEncryptedBits.append(this.homomorphEncryptedBits.getLineDelimiter());
				}
				if (first) {
					this.plainOperations.setText(this.plainOperations.getText() + " + " + Integer.toString(data.getNumber()));
					first = false;
				} else {
					this.plainOperations.setText("(" + this.plainOperations.getText() + ") + " + Integer.toString(data.getNumber()));
				}
				ghResult = (ghResult + data.getNumber())%(1<<logMod);
				this.plainResult.setText(Integer.toString(ghResult));
				data.setArray1(this.homomorphResultEncrypted);
				data.setArray2(this.homomorphEncrypted);
				addJob.setUser(true);
				addJob.schedule();
			} break;
			case PAILLIER: {
				this.homomorphResultPlain.setText("");
				paillierData.setOperation(paillierData.getOperation().mod(paillierData.getPubKey()[0]));
				this.homomorphPlain.setText(paillierData.getOperation().toString());
				Paillier.encryptOperation(paillierData);
				this.homomorphEncrypted = new BigInteger[]{paillierData.getOperationCipher()};
				this.homomorphEncryptedBits.setText(this.homomorphEncrypted[0].toString());
				this.homomorphResultEncrypted[0] =
					this.homomorphResultEncrypted[0].multiply(this.homomorphEncrypted[0])
					.mod(paillierData.getPubKey()[0].pow(2));
				this.paillierData.setResultCipher(this.homomorphResultEncrypted[0]);
				this.homomorphResultEncryptedBits.setText(this.homomorphResultEncrypted[0].toString());
				this.decryptButton.setEnabled(true);
				if (first) {
					first = false;
				}
				this.plainOperations.setText(this.plainOperations.getText() + " + " + paillierData.getOperation());
				paillierResult = paillierResult.add(paillierData.getOperation()).mod(paillierData.getPubKey()[0].pow(2));
				this.plainResult.setText(paillierResult.toString());

		        nText.setBackground(ColorService.LIGHTGRAY);
		        gText.setBackground(ColorService.LIGHTGRAY);
				initialPlain.setBackground(ColorService.LIGHTGRAY);
				initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlain.setBackground(YELLOW);
				homomorphEncryptedBits.setBackground(YELLOW);
				homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
				homomorphResultEncryptedBits.setBackground(YELLOW);
				plainResult.setBackground(YELLOW);
				plainOperations.setBackground(YELLOW);
			} break;
		}
	}

	/**
	 * The number to be multiplied with is entered, encrypted and the multiplication is evaluated both
	 * homomorphically and plain, the results appear in the text boxes in the algorithm area
	 */
	private void multTextSelected() {
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				data.setData(data.getNumber()%(1<<logMod));
				this.homomorphPlain.setText(Integer.toString(data.getNumber()));
				this.homomorphBits = Functions.numToBits(data.getNumber(), logMod);
				this.homomorphPlainBits.setText(Functions.bitArrayToString(this.homomorphBits));
				this.homomorphEncrypted = GHEncrypt.encrypt(fheParams, keyPair, this.homomorphBits);
				this.homomorphEncryptedBits.setText("");
				this.homomorphResultEncryptedBits.setText("");
				this.homomorphResultPlain.setText("");
				this.homomorphResultPlainBits.setText("");
				this.oldOperationText = this.plainOperations.getText();
				this.ghOldResult = this.ghResult;
				this.oldFirst = first;
				for (int i = 0; i < this.homomorphEncrypted.length; i++) {
					this.homomorphEncryptedBits.append(i + ": " + this.homomorphEncrypted[i].toString());
					this.homomorphEncryptedBits.append(this.homomorphEncryptedBits.getLineDelimiter());
				}
				if (first) {
					this.plainOperations.setText(this.plainOperations.getText() + " * " + Integer.toString(data.getNumber()));
					first = false;
				} else {
					this.plainOperations.setText("(" + this.plainOperations.getText() + ") * " + Integer.toString(data.getNumber()));
				}
				ghResult = (ghResult * data.getNumber())%(1<<logMod);
				this.plainResult.setText(Integer.toString(ghResult));
				data.setArray1(this.homomorphResultEncrypted);
				data.setArray2(this.homomorphEncrypted);
				multJob.setUser(true);
				multJob.schedule();
			} break;
			case RSA: {
				this.homomorphResultPlain.setText("");
				this.homomorphPlain.setText(rsaEncData.getOperation());
				this.homomorphEncrypted = new BigInteger[]{
						new BigInteger(rsaEncData.getOperation()).modPow(rsaEncData.getE(), rsaEncData.getN())};
				this.homomorphEncryptedBits.setText(this.homomorphEncrypted[0].toString());
				this.homomorphResultEncrypted[0] =
					this.homomorphResultEncrypted[0].multiply(this.homomorphEncrypted[0]).mod(rsaEncData.getN());
				this.homomorphResultEncryptedBits.setText(this.homomorphResultEncrypted[0].toString());
				this.decryptButton.setEnabled(true);
				if (first) {
					first = false;
				}
				this.plainOperations.setText(this.plainOperations.getText() + " * " + rsaEncData.getOperation());
				rsaResult = rsaResult.multiply(new BigInteger(rsaEncData.getOperation()));
				this.plainResult.setText(rsaResult.toString());

				eText.setBackground(ColorService.LIGHTGRAY);
		        nText.setBackground(ColorService.LIGHTGRAY);
				initialPlain.setBackground(ColorService.LIGHTGRAY);
				initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlain.setBackground(YELLOW);
				homomorphEncryptedBits.setBackground(YELLOW);
				homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
				homomorphResultEncryptedBits.setBackground(YELLOW);
				plainResult.setBackground(YELLOW);
				plainOperations.setBackground(YELLOW);
			} break;
		}

	}

	/**
	 * Decrypts the homomorphic result
	 */
	private void decryptResult() {
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				this.homomorphResultBits = GHDecrypt.decrypt(fheParams, keyPair.det, this.homomorphResultEncrypted, keyPair.w);
				this.homomorphResultPlainBits.setText(Functions.bitArrayToString(homomorphResultBits));
				this.homomorphResultPlain.setText(Integer.toString(Functions.bitsToNum(homomorphResultBits)));

				detText.setBackground(ColorService.LIGHTGRAY);
				rootText.setBackground(ColorService.LIGHTGRAY);
				cText.setBackground(ColorService.LIGHTGRAY);
				pkBlockText.setBackground(ColorService.LIGHTGRAY);
				initialPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlainBits.setBackground(YELLOW);

			} break;
			case RSA: {
				this.homomorphResultPlain.setText(
				this.homomorphResultEncrypted[0].modPow(rsaDecData.getD(), rsaDecData.getN()).toString());

				eText.setBackground(ColorService.LIGHTGRAY);
		        nText.setBackground(ColorService.LIGHTGRAY);
			} break;
			case PAILLIER: {
				Paillier.decryptResult(paillierData);
				this.homomorphResultPlain.setText(paillierData.getResultPlain().toString());

		        nText.setBackground(ColorService.LIGHTGRAY);
		        gText.setBackground(ColorService.LIGHTGRAY);
			} break;
		}

		initialPlain.setBackground(ColorService.LIGHTGRAY);
		initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphResultPlain.setBackground(YELLOW);
		homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		plainResult.setBackground(ColorService.LIGHTGRAY);
		plainOperations.setBackground(ColorService.LIGHTGRAY);
	}

	/**
	 * Resets all numbers and disables all buttons such that a new initial text must be entered
	 */
	private void resetNumbers() {
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				this.initialPlain.setText("");
				this.initialPlainBits.setText("");
				this.initialEncryptedBits.setText("");
				this.modulus.setText("");
				this.homomorphPlain.setText("");
				this.homomorphPlainBits.setText("");
				this.homomorphEncryptedBits.setText("");
				this.homomorphResultEncryptedBits.setText("");
				this.homomorphResultPlain.setText("");
				this.homomorphResultPlainBits.setText("");
				this.plainOperations.setText("");
				this.plainResult.setText("");
				this.first = true;
				this.resetNumButton.setEnabled(false);
				this.decryptButton.setEnabled(false);
				this.homomorphMult.setEnabled(false);
				this.homomorphAdd.setEnabled(false);
				this.modulusSel.setBackground(ColorService.RED);
				//this.modulus.select(0);
				this.initTextSel.setEnabled(false);
				this.initTextSel.setBackground(ColorService.RED);
				initialPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
				homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
			} break;
			case RSA: {
				this.initialPlain.setText("");
				this.initialEncryptedBits.setText("");
				this.homomorphPlain.setText("");
				this.homomorphEncryptedBits.setText("");
				this.homomorphResultEncryptedBits.setText("");
				this.homomorphResultPlain.setText("");
				this.plainOperations.setText("");
				this.plainResult.setText("");
				this.first = true;
				this.resetNumButton.setEnabled(false);
				this.decryptButton.setEnabled(false);
				this.homomorphMult.setEnabled(false);
				this.initTextSel.setBackground(ColorService.RED);
			} break;
			case PAILLIER: {
				this.initialPlain.setText("");
				this.initialEncryptedBits.setText("");
				this.homomorphPlain.setText("");
				this.homomorphEncryptedBits.setText("");
				this.homomorphResultEncryptedBits.setText("");
				this.homomorphResultPlain.setText("");
				this.plainOperations.setText("");
				this.plainResult.setText("");
				this.first = true;
				this.resetNumButton.setEnabled(false);
				this.decryptButton.setEnabled(false);
				this.homomorphAdd.setEnabled(false);
				this.initTextSel.setBackground(ColorService.RED);
			}
		}


		initialPlain.setBackground(ColorService.LIGHTGRAY);
		initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
		homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
		plainResult.setBackground(ColorService.LIGHTGRAY);
		plainOperations.setBackground(ColorService.LIGHTGRAY);
	}

	/**
	 * Resets everything
	 */
	private void resetAll() {
		resetNumbers();
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				this.resetAllButton.setEnabled(false);
				this.modulusSel.setEnabled(false);
				this.detText.setText("");
				//this.wText.setText("");
				this.rootText.setText("");
				this.pkBlockText.setText("");
				this.cText.setText("");

				detText.setBackground(ColorService.LIGHTGRAY);
				rootText.setBackground(ColorService.LIGHTGRAY);
				cText.setBackground(ColorService.LIGHTGRAY);
				pkBlockText.setBackground(ColorService.LIGHTGRAY);
			} break;
			case RSA: {
				this.resetAllButton.setEnabled(false);
				this.keySel.setBackground(ColorService.RED);
				this.initTextSel.setEnabled(false);
				this.rsaEncData = new RSAData(Action.EncryptAction);
				this.rsaDecData = new RSAData(Action.DecryptAction);
				//this.dText.setText("");
				this.eText.setText("");
				this.nText.setText("");

				eText.setBackground(ColorService.LIGHTGRAY);
		        nText.setBackground(ColorService.LIGHTGRAY);
			} break;
			case PAILLIER: {
				this.resetAllButton.setEnabled(false);
				this.keySel.setBackground(ColorService.RED);
				this.initTextSel.setEnabled(false);
				this.paillierData = new PaillierData();
				this.nText.setText("");
				this.gText.setText("");
				//this.lText.setText("");
				//this.mText.setText("");

		        nText.setBackground(ColorService.LIGHTGRAY);
		        gText.setBackground(ColorService.LIGHTGRAY);
			}
		}
	}

	/**
	 * Creates the algorithm area
	 * @param parent the composite in wich the algorithm area is created
	 */
	private void createAlgoArea(final Composite parent) {
		final Composite g = new Composite(parent, SWT.SHADOW_NONE);
		g.setLayout(new GridLayout());
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		switch(tabChoice) {
			case GENTRY_HALEVI: {
				this.createGHKeyArea(g);
				this.createGHModulusArea(g);
				Group mainComposite = new Group(g, SWT.SHADOW_NONE);
				final RowLayout mrl = new RowLayout(SWT.VERTICAL);
				mainComposite.setText(Messages.HEComposite_HomomorphicArea);
				mainComposite.setLayout(mrl);
				this.createGHInitialArea(mainComposite);
				this.createGHHomomorphicArea(mainComposite);
				this.createGHPlainArea(g);
			} break;
			case RSA: {
				this.createRSAKeyArea(g);
				Group mainComposite = new Group(g, SWT.SHADOW_NONE);
				final RowLayout mrl = new RowLayout(SWT.VERTICAL);
				mainComposite.setText(Messages.HEComposite_HomomorphicArea);
				mainComposite.setLayout(mrl);
				this.createRSAInitialArea(mainComposite);
				this.createRSAHomomorphicArea(mainComposite);
				this.createRSAPlainArea(g);
			} break;
			case PAILLIER: {
				this.createPaillierKeyArea(g);
				Group mainComposite = new Group(g, SWT.SHADOW_NONE);
				final RowLayout mrl = new RowLayout(SWT.VERTICAL);
				mainComposite.setText(Messages.HEComposite_HomomorphicArea);
				mainComposite.setLayout(mrl);
				this.createPaillierInitialArea(mainComposite);
				this.createPaillierHomomorphArea(mainComposite);
				this.createPaillierPlainArea(g);
			} break;
		}
	}

	/**
	 * Creates the key area for the Gentry & Halevi scheme
	 * @param parent the composite in which it is created
	 */
	private void createGHKeyArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_KeyArea_Public_Key);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(104,30);
		final RowData labelrd2 = new RowData(110,30);
		final RowData textrd = new RowData(307,18);
		final RowData textmultirdSmall = new RowData(290,50);

		Label label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_KeyArea_Determinant);
		detText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		detText.setLayoutData(textrd);
		detText.setEditable(false);

		label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd2);
		label.setText(Messages.HEComposite_GH_KeyArea_Root);
		rootText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		rootText.setLayoutData(textrd);
		rootText.setEditable(false);

        Label spacerLabel = new Label(subComposite, SWT.NONE);
        spacerLabel.setLayoutData(new RowData(4,30));

		subComposite = new Composite(mainComposite, SWT.NONE);
		subComposite.setLayout(srl);
		label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_GH_KeyArea_Public_Key_Blocks);
		pkBlockText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		pkBlockText.setLayoutData(textmultirdSmall);
		pkBlockText.setEditable(false);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd2);
		label.setText(Messages.HEComposite_GH_KeyArea_Secret_Vector);
		cText = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		cText.setLayoutData(textmultirdSmall);
		cText.setEditable(false);
	}

	/**
	 * Creates the key area for the RSA scheme
	 * @param parent the composite in which it is created
	 */
	private void createRSAKeyArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout();
		mainComposite.setText(org.jcryptool.visual.he.Messages.HEComposite_KeyArea_Public_Key);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(145,18);
		final RowData textrd = new RowData(256,18);


        Label l = new Label(subComposite, SWT.RIGHT);
        l.setText("e  "); //$NON-NLS-1$
        l.setLayoutData(labelrd);
        eText = new Text(subComposite, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL);
        eText.setLayoutData(textrd);
        l = new Label(subComposite, SWT.RIGHT);
        l.setText("N  "); //$NON-NLS-1$
        l.setLayoutData(labelrd);
        nText = new Text(subComposite, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL);
        nText.setLayoutData(textrd);

        Label spacerLabel = new Label(subComposite, SWT.NONE);
        spacerLabel.setLayoutData(new RowData(3,18));
    }

	/**
	 * Creates the key area for the Paillier scheme
	 * @param parent the composite in which it is created
	 */
	private void createPaillierKeyArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(org.jcryptool.visual.he.Messages.HEComposite_KeyArea_Public_Key);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(145,18);
		final RowData textrd = new RowData(256,18);

        Label l = new Label(subComposite, SWT.RIGHT);
        l.setText("N  "); //$NON-NLS-1$
        l.setLayoutData(labelrd);
        nText = new Text(subComposite, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL);
        nText.setLayoutData(textrd);
        l = new Label(subComposite, SWT.RIGHT);
        l.setText("g  "); //$NON-NLS-1$
        l.setLayoutData(labelrd);
        gText = new Text(subComposite, SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL);
        gText.setLayoutData(textrd);

        Label spacerLabel = new Label(subComposite, SWT.NONE);
        spacerLabel.setLayoutData(new RowData(3,18));
	}

	private void createGHModulusArea(final Composite parent) {


        /**
         * Only the scheme by Gentry and Halevi requires the modulus to be manually set,
         * the other schemes inherit this from the key
         */
        if (tabChoice == GENTRY_HALEVI) {
	        Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
	        final RowLayout mrl = new RowLayout();
	        final RowData labelrd = new RowData(104,30);
	        mainComposite.setText(Messages.HEComposite_Modulus);
	        mainComposite.setLayout(mrl);

	        Label textLabel = new Label(mainComposite, SWT.RIGHT);
	        textLabel.setLayoutData(labelrd);
	        textLabel.setText(Messages.HEComposite_Modulus_Label);
	        this.modulus = new Text(mainComposite, SWT.NONE);
	        this.modulus.setLayoutData(new RowData(741,18));
	        this.modulus.setEditable(false);
	        /*this.modulus = new Combo(mainComposite, SWT.NONE);
	        this.modulus.setLayoutData(textrd);
	        this.modulus.setToolTipText(Messages.HEComposite_Modulus_Tooltip);
	        this.modulus.add("32",0);
	        this.modulus.add("64",1);
	        this.modulus.add("128",2);
	        this.modulus.add("256",3);
	        this.modulus.add("512",4);
	        this.modulus.add("1024",5);
	        this.modulus.setEnabled(false);
	        this.modulus.addModifyListener(new ModifyListener() {
	        	public void modifyText(final ModifyEvent e) {
	        		logMod = 5 + modulus.indexOf(modulus.getText());
	        		detText.setBackground(ColorService.LIGHTGRAY);
					rootText.setBackground(ColorService.LIGHTGRAY);
					cText.setBackground(ColorService.LIGHTGRAY);
					pkBlockText.setBackground(ColorService.LIGHTGRAY);
					initialPlain.setBackground(ColorService.LIGHTGRAY);
					initialPlainBits.setBackground(ColorService.LIGHTGRAY);
					initialEncryptedBits.setBackground(ColorService.LIGHTGRAY);
					homomorphPlain.setBackground(ColorService.LIGHTGRAY);
					homomorphPlainBits.setBackground(ColorService.LIGHTGRAY);
					homomorphEncryptedBits.setBackground(ColorService.LIGHTGRAY);
					homomorphResultPlain.setBackground(ColorService.LIGHTGRAY);
					homomorphResultPlainBits.setBackground(ColorService.LIGHTGRAY);
					homomorphResultEncryptedBits.setBackground(ColorService.LIGHTGRAY);
					plainResult.setBackground(ColorService.LIGHTGRAY);
					plainOperations.setBackground(ColorService.LIGHTGRAY);
	        		initTextSel.setEnabled(true);
	        	}
	        });*/

	        Label spacerLabel = new Label(mainComposite, SWT.NONE);
	        spacerLabel.setLayoutData(new RowData(7,30));
        }
	}

	/**
	 * Creates the area in which the initial number and its encryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHInitialArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Initial_Data);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd1 = new RowData(98,30);
		final RowData labelrd2 = new RowData(110,30);
		//final RowData labelrd = new RowData(150,18);
		//final RowData biglabelrd = new RowData(210,18);
		final RowData textrd = new RowData(307,18);
		//final RowData textrd = new RowData(282,18);
		final RowData textmultird = new RowData(720,50);

		Label label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd1);
		label.setText(Messages.HEComposite_Initial_Number);
		initialPlain = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		initialPlain.setLayoutData(textrd);
		initialPlain.setEditable(false);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd2);
		label.setText(Messages.HEComposite_Initial_Number_As_Bits);
		initialPlainBits = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		initialPlainBits.setLayoutData(textrd);
		initialPlainBits.setEditable(false);

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd1);
		label.setText(Messages.HEComposite_Initial_Number_As_Enc_Vec);
		initialEncryptedBits = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		initialEncryptedBits.setLayoutData(textmultird);
		initialEncryptedBits.setEditable(false);
	}

	/**
	 * Creates the area in which the initial number and its encryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createRSAInitialArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Initial_Data);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(139,18);
		final RowData textmultird = new RowData(660,50);

        Label l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Initial_Number);
        l.setLayoutData(labelrd);

        initialPlain = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        initialPlain.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Initial_Number_As_Enc);
        l.setLayoutData(labelrd);

        initialEncryptedBits = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        initialEncryptedBits.setLayoutData(textmultird);
    }

	/**
	 * Creates the area in which the initial number and its encryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createPaillierInitialArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Initial_Data);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(139,18);
		final RowData textmultird = new RowData(660,50);

        Label l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Initial_Number);
        l.setLayoutData(labelrd);

        initialPlain = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        initialPlain.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Initial_Number_As_Enc);
        l.setLayoutData(labelrd);

        initialEncryptedBits = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        initialEncryptedBits.setLayoutData(textmultird);
    }

	/**
	 * Creates the area in which the operation number, its encryption, the result and the decryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHHomomorphicArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Operation_Area);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd1 = new RowData(98,30);
		final RowData labelrd2 = new RowData(110,30);
		//final RowData labelrd = new RowData(150,18);
		//final RowData biglabelrd = new RowData(210,18);
		final RowData textrd = new RowData(307,18);
		//final RowData textrd = new RowData(282,18);
		final RowData textmultird = new RowData(720,50);

		Label label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd1);
		label.setText(Messages.HEComposite_Operation_Number);
		homomorphPlain = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		homomorphPlain.setLayoutData(textrd);
		homomorphPlain.setEditable(false);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd2);
		label.setText(Messages.HEComposite_Operation_Number_As_Bits);
		homomorphPlainBits = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		homomorphPlainBits.setLayoutData(textrd);
		homomorphPlainBits.setEditable(false);

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd1);
		label.setText(Messages.HEComposite_Operation_Number_As_Enc_Vec);
		homomorphEncryptedBits = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		homomorphEncryptedBits.setLayoutData(textmultird);
		homomorphEncryptedBits.setEditable(false);

		mainComposite = new Group(parent, SWT.SHADOW_NONE);
		mainComposite.setText(Messages.HEComposite_Result_Area);
		mainComposite.setLayout(mrl);

		subComposite = new Composite(mainComposite, SWT.NONE);
		subComposite.setLayout(srl);

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd1);
		label.setText(Messages.HEComposite_Result_Number_As_Enc_Vec);
		homomorphResultEncryptedBits = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		homomorphResultEncryptedBits.setLayoutData(textmultird);
		homomorphResultEncryptedBits.setEditable(false);

		subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);
        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd1);
		label.setText(Messages.HEComposite_Result_Number);
		homomorphResultPlain = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		homomorphResultPlain.setLayoutData(textrd);
		homomorphResultPlain.setEditable(false);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd2);
		label.setText(Messages.HEComposite_Result_Number_As_Bits);
		homomorphResultPlainBits = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		homomorphResultPlainBits.setLayoutData(textrd);
		homomorphResultPlainBits.setEditable(false);
	}

	/**
	 * Creates the area in which the operation number, its encryption, the result and the decryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createRSAHomomorphicArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Operation_Area);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(139,18);
		final RowData textmultird = new RowData(660,50);

        Label l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Operation_Number);
        l.setLayoutData(labelrd);
        homomorphPlain = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphPlain.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Operation_Number_As_Enc);
        l.setLayoutData(labelrd);
        homomorphEncryptedBits = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphEncryptedBits.setLayoutData(textmultird);

        mainComposite = new Group(parent, SWT.SHADOW_NONE);
		mainComposite.setText(Messages.HEComposite_Result_Area);
		mainComposite.setLayout(mrl);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Result_Number_As_Enc);
        l.setLayoutData(labelrd);
        homomorphResultEncryptedBits = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphResultEncryptedBits.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Result_Number);
        l.setLayoutData(labelrd);
        homomorphResultPlain = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphResultPlain.setLayoutData(textmultird);
	}

	/**
	 * Creates the area in which the operation number, its encryption, the result and the decryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createPaillierHomomorphArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Operation_Area);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(139,18);
		final RowData textmultird = new RowData(660,50);

        Label l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Operation_Number);
        l.setLayoutData(labelrd);
        homomorphPlain = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphPlain.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Operation_Number_As_Enc);
        l.setLayoutData(labelrd);
        homomorphEncryptedBits = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphEncryptedBits.setLayoutData(textmultird);

        mainComposite = new Group(parent, SWT.SHADOW_NONE);
		mainComposite.setText(Messages.HEComposite_Result_Area);
		mainComposite.setLayout(mrl);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Result_Number_As_Enc);
        l.setLayoutData(labelrd);
        homomorphResultEncryptedBits = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphResultEncryptedBits.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        l = new Label(subComposite, SWT.RIGHT);
        l.setText(Messages.HEComposite_Result_Number);
        l.setLayoutData(labelrd);
        homomorphResultPlain = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        homomorphResultPlain.setLayoutData(textmultird);
	}

	/**
	 * Creates the area in which the operations in plaintext are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHPlainArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Plain_Data);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(104,30);
		final RowData labelrd2 = new RowData(110,30);
		//final RowData labelrd = new RowData(150,18);
		final RowData textrd = new RowData(307,18);
		//final RowData textrd = new RowData(282,18);

		Label label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_Plain_Operations);
		plainOperations = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		plainOperations.setLayoutData(textrd);
		plainOperations.setEditable(false);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd2);
		label.setText(Messages.HEComposite_Result);
		plainResult = new Text(subComposite, SWT.MULTI | SWT.H_SCROLL);
		plainResult.setLayoutData(textrd);
		plainResult.setEditable(false);

        Label spacerLabel = new Label(subComposite, SWT.NONE);
        spacerLabel.setLayoutData(new RowData(4,30));
	}

	/**
	 * Creates the area in which the operations in plaintext are shown
	 * @param parent the composite in which it is created
	 */
	private void createRSAPlainArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Plain_Data);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(145,18);
		final RowData textmultird = new RowData(660,50);

		Label label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_Plain_Operations);
		plainOperations = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		plainOperations.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_Result);
		plainResult = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		plainResult.setLayoutData(textmultird);

        Label spacerLabel = new Label(subComposite, SWT.NONE);
        spacerLabel.setLayoutData(new RowData(3,18));
    }

	/**
	 * Creates the area in which the operations in plaintext are shown
	 * @param parent the composite in which it is created
	 */
	private void createPaillierPlainArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		final RowLayout mrl = new RowLayout(SWT.VERTICAL);
		mainComposite.setText(Messages.HEComposite_Plain_Data);
		mainComposite.setLayout(mrl);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		final RowLayout srl = new RowLayout();
		subComposite.setLayout(srl);
		final RowData labelrd = new RowData(145,18);
		final RowData textmultird = new RowData(660,50);

		Label label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_Plain_Operations);
		plainOperations = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		plainOperations.setLayoutData(textmultird);

        subComposite = new Composite(mainComposite, SWT.NONE);
        subComposite.setLayout(srl);

        label = new Label(subComposite, SWT.RIGHT);
		label.setLayoutData(labelrd);
		label.setText(Messages.HEComposite_Result);
		plainResult = new Text(subComposite, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		plainResult.setLayoutData(textmultird);

        Label spacerLabel = new Label(subComposite, SWT.NONE);
        spacerLabel.setLayoutData(new RowData(3,18));
	}
}
