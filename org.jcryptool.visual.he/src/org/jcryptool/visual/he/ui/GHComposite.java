// -----BEGIN DISCLAIMER-----
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
import org.jcryptool.visual.he.wizards.GHInitialTextWizard;
import org.jcryptool.visual.he.wizards.GHKeySelectionWizard;
import org.jcryptool.visual.he.wizards.GHModulusWizard;
import org.jcryptool.visual.he.wizards.GHOperationTextWizard;
import org.jcryptool.visual.he.wizards.GHSettingsWizard;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.swt.graphics.Point;


/**
 * Composite to display Gentry Halevi homomorphic encryption scheme
 *
 * @author Coen Ramaekers
 */

public class GHComposite extends Composite {
	/** Yellow */
	public Color YELLOW;

	/** Button for running the key selection wizard */
	private Button keySel;

	/** Combo box to select field size */
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

	/** Log_2 of modulus to be used in calculations */
	private int logMod;

	/** Will hold data in wizards */
	private GHData data = new GHData();

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
	private Label label_1;
	private Label label_2;
	private Composite composite;
	private Composite composite_1;
	private Composite subComposite_1;
	private Composite composite_2;
	private Label label_3;
	private Composite composite_3;
	private Composite composite_4;
	private Composite composite_5;
	private Group mainComposite_3;
	private Composite subComposite_2;
	private Label label_4;
	private Label label_5;
	private Composite composite_6;
	private Composite composite_7;
	private Label label_6;
	private Composite subComposite_3;
	private Label label_7;
	private Composite subComposite_4;
	private Label label_8;
	private Composite subComposite_5;
	private Composite composite_8;
	private Label label_9;
	private Composite composite_9;
	private Label label_10;
	private Composite composite_10;
	private Composite composite_11;
	private Label label_11;
	private Label spacerLabel_1;

	public GHComposite(final Composite parent, final int style) {
		super(parent,style);
		this.initialize();
		YELLOW = GHComposite.this.getDisplay().getSystemColor(SWT.COLOR_YELLOW);

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
				detText.setBackground(null);
				rootText.setBackground(null);
				cText.setBackground(null);
				pkBlockText.setBackground(null);
				initialPlain.setBackground(null);
				initialPlainBits.setBackground(null);
				initialEncryptedBits.setBackground(null);
				homomorphPlain.setBackground(YELLOW);
				homomorphPlainBits.setBackground(YELLOW);
				homomorphEncryptedBits.setBackground(YELLOW);
				homomorphResultPlain.setBackground(null);
				homomorphResultPlainBits.setBackground(null);
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
				detText.setBackground(null);
				rootText.setBackground(null);
				cText.setBackground(null);
				pkBlockText.setBackground(null);
				initialPlain.setBackground(null);
				initialPlainBits.setBackground(null);
				initialEncryptedBits.setBackground(null);
				homomorphPlain.setBackground(null);
				homomorphPlainBits.setBackground(null);
				homomorphEncryptedBits.setBackground(null);
				homomorphResultPlain.setBackground(null);
				homomorphResultPlainBits.setBackground(null);
				homomorphResultEncryptedBits.setBackground(null);
				plainResult.setBackground(null);
				plainOperations.setBackground(null);
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
						GHComposite.this.getDisplay().asyncExec(jobCanceled);
						return Status.CANCEL_STATUS;
					} else {
						data.setArray3(result);
						GHComposite.this.getDisplay().asyncExec(jobDone);
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
						GHComposite.this.getDisplay().asyncExec(jobCanceled);
						return Status.CANCEL_STATUS;
					} else {
						data.setArray3(result);
						GHComposite.this.getDisplay().asyncExec(jobDone);
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
        label.setText(Messages.HEComposite_GentryHalevi_Title);
	    
        final StyledText stDescription = new StyledText(head, SWT.READ_ONLY);
        stDescription.setText(Messages.HEComposite_GentryHalevi_Description);
	    stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Creates the main area, subdivided into the button area and the algorithm area
	 */
	private void createMain() {
		final Group g = new Group(this, SWT.NONE);
		final GridLayout gl = new GridLayout(2, false);
		g.setText(Messages.HEComposite_Scheme);
        g.setLayout(gl);
        g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
		GridData gd_mainComposite = new GridData(SWT.FILL, SWT.FILL, false, true);
		gd_mainComposite.minimumWidth = 180;
		gd_mainComposite.widthHint = 180;
		mainComposite.setLayoutData(gd_mainComposite);

		Group subComposite1= new Group(mainComposite, SWT.SHADOW_NONE);
		subComposite1.setLayout(new FillLayout(SWT.HORIZONTAL));
		subComposite1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subComposite1.setText(Messages.HEComposite_Key);

        this.keySel = new Button(subComposite1, SWT.PUSH);
        this.keySel.setBackground(ColorService.RED);
        this.keySel.setEnabled(true);
        this.keySel.setText(Messages.HEComposite_Keysel);
        this.keySel.setToolTipText(Messages.HEComposite_Key_Tooltip);
        this.keySel.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		/** Every scheme has a different type of key, so requires his own wizard */
        		if (new WizardDialog(GHComposite.this.getShell(),
        					new GHKeySelectionWizard(keyPair, fheParams, GHComposite.this.getDisplay())).open() == Window.OK) keySelected();
        	}
        });

        Composite spacerComposite = new Composite(mainComposite, SWT.NONE);
        Label spacerLabel = new Label(spacerComposite, SWT.NONE);
        spacerLabel.setSize(130, 82);
        
    	Group subComposite2 = new Group(mainComposite, SWT.SHADOW_NONE);
    	subComposite2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subComposite2.setText(Messages.HEComposite_Modulus);
    	subComposite2.setLayout(new FillLayout(SWT.HORIZONTAL));
    	this.modulusSel = new Button(subComposite2, SWT.PUSH);
        this.modulusSel.setBackground(ColorService.RED);
        this.modulusSel.setEnabled(false);
        this.modulusSel.setText(Messages.HEComposite_Keysel);
        this.modulusSel.setToolTipText(Messages.HEComposite_Modulus_Tooltip);
        this.modulusSel.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		/** Every scheme has a different type of key, so requires his own wizard */
        		if (new WizardDialog(GHComposite.this.getShell(),
        					new GHModulusWizard(data)).open() == Window.OK) modulusSelected();
        	}
        });
        
        Group subComposite3 = new Group(mainComposite, SWT.SHADOW_NONE);
        subComposite3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subComposite3.setText(Messages.HEComposite_Initial_Text);
		subComposite3.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.initTextSel = new Button(subComposite3, SWT.PUSH);
		this.initTextSel.setToolTipText(Messages.HEComposite_Initial_Tooltip);
        this.initTextSel.setBackground(ColorService.RED);
        this.initTextSel.setEnabled(false);
        this.initTextSel.setText(Messages.HEComposite_Initial_Text_Select);
        this.initTextSel.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		/** Since the modulus is different for each scheme, they each require an own wizard*/
        		if (new WizardDialog(GHComposite.this.getShell(),
        				new GHInitialTextWizard(logMod, data)).open() == Window.OK) initialTextSelected();
        	}
        });

        spacerComposite = new Composite(mainComposite, SWT.NONE);
        spacerLabel = new Label(spacerComposite, SWT.NONE);
        spacerLabel.setSize(130, 96);

        Group subComposite4 = new Group(mainComposite, SWT.SHADOW_NONE);
        subComposite4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subComposite4.setText(Messages.HEComposite_Homomorphic_Text);
        subComposite4.setLayout(new FillLayout(SWT.VERTICAL));

        this.homomorphAdd = new Button(subComposite4, SWT.PUSH);
        this.homomorphAdd.setToolTipText(Messages.HEComposite_Homomorphic_Tooltip_Add);
        this.homomorphAdd.setEnabled(false);
        this.homomorphAdd.setText(Messages.HEComposite_Homomorphic_Add_Select);
        this.homomorphAdd.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		if (new WizardDialog(GHComposite.this.getShell(),
        				new GHOperationTextWizard(logMod, data)).open() == Window.OK) addTextSelected();
        	}
        });

        this.homomorphMult = new Button(subComposite4, SWT.PUSH);
        this.homomorphMult.setToolTipText(Messages.HEComposite_Homomorphic_Tooltip_Multiply);
        this.homomorphMult.setEnabled(false);
        this.homomorphMult.setText(Messages.HEComposite_Homomorphic_Mult_Select);
        this.homomorphMult.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		if (new WizardDialog(GHComposite.this.getShell(),
	        		new GHOperationTextWizard(logMod, data)).open() == Window.OK) multTextSelected();
        	}
        });

		spacerLabel_1 = new Label(subComposite4, SWT.NONE);
		spacerLabel_1.setSize(new Point(0, 70));
		spacerLabel_1.setSize(130,10);

        this.decryptButton = new Button(subComposite4, SWT.PUSH);
        this.decryptButton.setToolTipText(Messages.HEComposite_Decrypt_Tooltip);
        this.decryptButton.setEnabled(false);
        this.decryptButton.setText(Messages.HEComposite_Decrypt_Select);
        this.decryptButton.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		decryptResult();
        	}
        });

        spacerComposite = new Composite(mainComposite, SWT.NONE);
        spacerLabel = new Label(spacerComposite, SWT.NONE);
        spacerLabel.setSize(130, 78);

        Group subComposite5 = new Group(mainComposite, SWT.SHADOW_NONE);
        subComposite5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subComposite5.setText(Messages.HEComposite_Reset_Text);
		subComposite5.setLayout(new FillLayout(SWT.VERTICAL));

		this.resetNumButton = new Button(subComposite5, SWT.PUSH);
		this.resetNumButton.setToolTipText(Messages.HEComposite_Reset_Numbers_Tooltip);
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
		this.resetAllButton.setEnabled(false);
		this.resetAllButton.setText(Messages.HEComposite_Reset_All);
		this.resetAllButton.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		resetAll();
        	}

        });

		Group subComposite6 = new Group(mainComposite, SWT.SHADOW_NONE);
		subComposite6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		subComposite6.setText(Messages.HEComposite_Settings_Group);
		subComposite6.setLayout(new FillLayout(SWT.HORIZONTAL));

		this.settingsButton = new Button(subComposite6, SWT.PUSH);
		this.settingsButton.setToolTipText(Messages.HEComposite_Settings_Tooltip);
		this.settingsButton.setText(Messages.HEComposite_Settings);
		this.settingsButton.addSelectionListener(new SelectionAdapter() {

        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		new WizardDialog(GHComposite.this.getShell(),
    					new GHSettingsWizard(data)).open();
        	}

        });
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
		this.modulusSel.setEnabled(true);
		detText.setText(keyPair.det.toString());
		rootText.setText(keyPair.root.toString());
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
		initialPlainBits.setBackground(null);
		homomorphPlainBits.setBackground(null);
		homomorphResultPlainBits.setBackground(null);
		initialPlain.setBackground(null);
		initialEncryptedBits.setBackground(null);
		homomorphPlain.setBackground(null);
		homomorphEncryptedBits.setBackground(null);
		homomorphResultPlain.setBackground(null);
		homomorphResultEncryptedBits.setBackground(null);
		plainResult.setBackground(null);
		plainOperations.setBackground(null);

	}

	private void modulusSelected() {
		this.modulusSel.setBackground(ColorService.GREEN);
		logMod = data.getModulus();
		data.initCount(logMod);
		modulus.setText(Integer.toString((int)Math.pow(2,logMod)));
		detText.setBackground(null);
		rootText.setBackground(null);
		cText.setBackground(null);
		pkBlockText.setBackground(null);
		modulus.setBackground(YELLOW);
		initialPlain.setBackground(null);
		initialPlainBits.setBackground(null);
		initialEncryptedBits.setBackground(null);
		homomorphPlain.setBackground(null);
		homomorphPlainBits.setBackground(null);
		homomorphEncryptedBits.setBackground(null);
		homomorphResultPlain.setBackground(null);
		homomorphResultPlainBits.setBackground(null);
		homomorphResultEncryptedBits.setBackground(null);
		plainResult.setBackground(null);
		plainOperations.setBackground(null);
		initTextSel.setEnabled(true);
	}

	/**
	 * The initial number is selected and encrypted, both being displayed in the algorithm area
	 */
	private void initialTextSelected() {
		this.initTextSel.setBackground(ColorService.GREEN);
		this.resetNumButton.setEnabled(true);
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

		detText.setBackground(null);
		rootText.setBackground(null);
		cText.setBackground(null);
		pkBlockText.setBackground(null);
		modulus.setBackground(null);
		initialPlainBits.setBackground(YELLOW);
		homomorphPlainBits.setBackground(null);
		homomorphResultPlainBits.setBackground(null);
		initialPlain.setBackground(YELLOW);
		initialEncryptedBits.setBackground(YELLOW);
		homomorphPlain.setBackground(null);
		homomorphEncryptedBits.setBackground(null);
		homomorphResultPlain.setBackground(null);
		homomorphResultEncryptedBits.setBackground(null);
		plainResult.setBackground(null);
		plainOperations.setBackground(null);

	}

	/**
	 * The number to be added is entered, encrypted and the addition is evaluated both homomorphically
	 * and plain, the results appear in the text boxes in the algorithm area
	 */
	private void addTextSelected() {
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
	}

	/**
	 * The number to be multiplied with is entered, encrypted and the multiplication is evaluated both
	 * homomorphically and plain, the results appear in the text boxes in the algorithm area
	 */
	private void multTextSelected() {
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
	}

	/**
	 * Decrypts the homomorphic result
	 */
	private void decryptResult() {
		this.homomorphResultBits = GHDecrypt.decrypt(fheParams, keyPair.det, this.homomorphResultEncrypted, keyPair.w);
		this.homomorphResultPlainBits.setText(Functions.bitArrayToString(homomorphResultBits));
		this.homomorphResultPlain.setText(Integer.toString(Functions.bitsToNum(homomorphResultBits)));

		detText.setBackground(null);
		rootText.setBackground(null);
		cText.setBackground(null);
		pkBlockText.setBackground(null);
		initialPlainBits.setBackground(null);
		homomorphPlainBits.setBackground(null);
		homomorphResultPlainBits.setBackground(YELLOW);
		initialPlain.setBackground(null);
		initialEncryptedBits.setBackground(null);
		homomorphPlain.setBackground(null);
		homomorphEncryptedBits.setBackground(null);
		homomorphResultPlain.setBackground(YELLOW);
		homomorphResultEncryptedBits.setBackground(null);
		plainResult.setBackground(null);
		plainOperations.setBackground(null);
	}

	/**
	 * Resets all numbers and disables all buttons such that a new initial text must be entered
	 */
	private void resetNumbers() {
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
		this.initTextSel.setEnabled(false);
		this.initTextSel.setBackground(ColorService.RED);
		initialPlainBits.setBackground(null);
		homomorphPlainBits.setBackground(null);
		homomorphResultPlainBits.setBackground(null);
		initialPlain.setBackground(null);
		initialEncryptedBits.setBackground(null);
		homomorphPlain.setBackground(null);
		homomorphEncryptedBits.setBackground(null);
		homomorphResultPlain.setBackground(null);
		homomorphResultEncryptedBits.setBackground(null);
		plainResult.setBackground(null);
		plainOperations.setBackground(null);
	}

	/**
	 * Resets everything
	 */
	private void resetAll() {
		resetNumbers();
		this.resetAllButton.setEnabled(false);
		this.modulusSel.setEnabled(false);
		this.detText.setText("");
		this.rootText.setText("");
		this.pkBlockText.setText("");
		this.cText.setText("");

		detText.setBackground(null);
		rootText.setBackground(null);
		cText.setBackground(null);
		pkBlockText.setBackground(null);
	}

	/**
	 * Creates the algorithm area
	 * @param parent the composite in wich the algorithm area is created
	 */
	private void createAlgoArea(final Composite parent) {
		final Composite g = new Composite(parent, SWT.SHADOW_NONE);
		g.setLayout(new GridLayout());
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.createGHKeyArea(g);
		this.createGHModulusArea(g);
		mainComposite_3 = new Group(g, SWT.SHADOW_NONE);
		mainComposite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainComposite_3.setText(Messages.HEComposite_HomomorphicArea);
		this.createGHInitialArea(mainComposite_3);
		this.createGHHomomorphicArea(mainComposite_3);
		this.createGHPlainArea(g);
	}

	/**
	 * Creates the key area for the Gentry & Halevi scheme
	 * @param parent the composite in which it is created
	 */
	private void createGHKeyArea(final Composite parent) {
		Group mainComposite_1 = new Group(parent, SWT.SHADOW_NONE);
		mainComposite_1.setLayout(new GridLayout(1, false));
		mainComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainComposite_1.setText(Messages.HEComposite_KeyArea_Public_Key);

		Composite subComposite = new Composite(mainComposite_1, SWT.NONE);
		subComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label;
		GridLayout gl_subComposite = new GridLayout(2, true);
		gl_subComposite.marginHeight = 0;
		gl_subComposite.marginWidth = 0;
		subComposite.setLayout(gl_subComposite);
		
		composite = new Composite(subComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		label_1 = new Label(composite, SWT.RIGHT);
		GridData gd_label_1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_label_1.widthHint = 100;
		label_1.setLayoutData(gd_label_1);
		label_1.setText(Messages.HEComposite_GH_KeyArea_Determinant);
		detText = new Text(composite, SWT.MULTI | SWT.H_SCROLL);
		detText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		detText.setEditable(false);
		
		composite_1 = new Composite(subComposite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		
				label_2 = new Label(composite_1, SWT.RIGHT);
				GridData gd_label_2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
				gd_label_2.widthHint = 100;
				label_2.setLayoutData(gd_label_2);
				label_2.setSize(new Point(110, 30));
				label_2.setText(Messages.HEComposite_GH_KeyArea_Root);
				rootText = new Text(composite_1, SWT.MULTI | SWT.H_SCROLL);
				rootText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				rootText.setEditable(false);

		subComposite_1 = new Composite(mainComposite_1, SWT.NONE);
		subComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_subComposite_1 = new GridLayout(2, true);
		gl_subComposite_1.marginHeight = 0;
		gl_subComposite_1.marginWidth = 0;
		subComposite_1.setLayout(gl_subComposite_1);
		
		composite_2 = new Composite(subComposite_1, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_2 = new GridLayout(2, false);
		gl_composite_2.marginHeight = 0;
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		label_3 = new Label(composite_2, SWT.RIGHT);
		GridData gd_label_3 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_label_3.widthHint = 100;
		label_3.setLayoutData(gd_label_3);
		label_3.setText(Messages.HEComposite_GH_KeyArea_Public_Key_Blocks);
		pkBlockText = new Text(composite_2, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_pkBlockText = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_pkBlockText.heightHint = 40;
		pkBlockText.setLayoutData(gd_pkBlockText);
		pkBlockText.setEditable(false);
		
		composite_3 = new Composite(subComposite_1, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.marginHeight = 0;
		gl_composite_3.marginWidth = 0;
		composite_3.setLayout(gl_composite_3);
		
		        label = new Label(composite_3, SWT.RIGHT);
		        GridData gd_label = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		        gd_label.widthHint = 100;
		        label.setLayoutData(gd_label);
		        label.setText(Messages.HEComposite_GH_KeyArea_Secret_Vector);
		        cText = new Text(composite_3, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		        GridData gd_cText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		        gd_cText.heightHint = 40;
		        cText.setLayoutData(gd_cText);
		        cText.setEditable(false);
	}

	/**
     * The scheme by Gentry and Halevi requires the modulus to be manually set.
     */
	private void createGHModulusArea(final Composite parent) {
        Group mainComposite_2 = new Group(parent, SWT.SHADOW_NONE);
        mainComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mainComposite_2.setText(Messages.HEComposite_Modulus);
        mainComposite_2.setLayout(new GridLayout(2, false));

        Label textLabel = new Label(mainComposite_2, SWT.RIGHT);
        GridData gd_textLabel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_textLabel.widthHint = 100;
        textLabel.setLayoutData(gd_textLabel);
        textLabel.setText(Messages.HEComposite_Modulus_Label);
        this.modulus = new Text(mainComposite_2, SWT.NONE);
        modulus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.modulus.setEditable(false);
	}

	/**
	 * Creates the area in which the initial number and its encryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHInitialArea(final Composite parent) {
		GridLayout gl_mainComposite_3 = new GridLayout(1, false);
		mainComposite_3.setLayout(gl_mainComposite_3);
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainComposite.setText(Messages.HEComposite_Initial_Data);
		GridLayout gl_mainComposite = new GridLayout(1, false);
		gl_mainComposite.marginHeight = 0;
		gl_mainComposite.marginWidth = 0;
		mainComposite.setLayout(gl_mainComposite);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		subComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_subComposite = new GridLayout(2, true);
		gl_subComposite.marginHeight = 0;
		gl_subComposite.marginWidth = 0;
		subComposite.setLayout(gl_subComposite);
		
		composite_4 = new Composite(subComposite, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_4 = new GridLayout(2, false);
		gl_composite_4.marginHeight = 0;
		gl_composite_4.marginWidth = 0;
		composite_4.setLayout(gl_composite_4);
	
		Label label = new Label(composite_4, SWT.RIGHT);
		GridData gd_label = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 100;
		label.setLayoutData(gd_label);
		label.setText(Messages.HEComposite_Initial_Number);
		initialPlain = new Text(composite_4, SWT.MULTI | SWT.H_SCROLL);
		initialPlain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initialPlain.setEditable(false);
        
        composite_5 = new Composite(subComposite, SWT.NONE);
        composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_composite_5 = new GridLayout(2, false);
        gl_composite_5.marginHeight = 0;
        gl_composite_5.marginWidth = 0;
        composite_5.setLayout(gl_composite_5);

        label_5 = new Label(composite_5, SWT.RIGHT);
        GridData gd_label_5 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_label_5.widthHint = 100;
        label_5.setLayoutData(gd_label_5);
		label_5.setText(Messages.HEComposite_Initial_Number_As_Bits);
		initialPlainBits = new Text(composite_5, SWT.MULTI | SWT.H_SCROLL);
		initialPlainBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initialPlainBits.setEditable(false);

		subComposite_2 = new Composite(mainComposite, SWT.NONE);
		subComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_subComposite_2 = new GridLayout(2, false);
        gl_subComposite_2.marginHeight = 0;
        gl_subComposite_2.marginWidth = 0;
        subComposite_2.setLayout(gl_subComposite_2);
        label_4 = new Label(subComposite_2, SWT.RIGHT);
        GridData gd_label_4 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_label_4.widthHint = 100;
        label_4.setLayoutData(gd_label_4);
		label_4.setText(Messages.HEComposite_Initial_Number_As_Enc_Vec);
		initialEncryptedBits = new Text(subComposite_2, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_initialEncryptedBits = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_initialEncryptedBits.heightHint = 70;
		initialEncryptedBits.setLayoutData(gd_initialEncryptedBits);
		initialEncryptedBits.setEditable(false);
	}

	/**
	 * Creates the area in which the operation number, its encryption, the result and the decryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHHomomorphicArea(final Composite parent) {
		Group mainComposite = new Group(parent, SWT.SHADOW_NONE);
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainComposite.setText(Messages.HEComposite_Operation_Area);
		GridLayout gl_mainComposite = new GridLayout(1, false);
		gl_mainComposite.marginHeight = 0;
		gl_mainComposite.marginWidth = 0;
		mainComposite.setLayout(gl_mainComposite);

		Composite subComposite = new Composite(mainComposite, SWT.NONE);
		subComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_subComposite = new GridLayout(2, true);
		gl_subComposite.marginHeight = 0;
		gl_subComposite.marginWidth = 0;
		subComposite.setLayout(gl_subComposite);
		
		composite_6 = new Composite(subComposite, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_6 = new GridLayout(2, false);
		gl_composite_6.marginHeight = 0;
		gl_composite_6.marginWidth = 0;
		composite_6.setLayout(gl_composite_6);
		
				Label label = new Label(composite_6, SWT.RIGHT);
				GridData gd_label = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
				gd_label.widthHint = 100;
				label.setLayoutData(gd_label);
				label.setText(Messages.HEComposite_Operation_Number);
				homomorphPlain = new Text(composite_6, SWT.MULTI | SWT.H_SCROLL);
				homomorphPlain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				homomorphPlain.setEditable(false);
		
		composite_7 = new Composite(subComposite, SWT.NONE);
		composite_7.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_7 = new GridLayout(2, false);
		gl_composite_7.marginHeight = 0;
		gl_composite_7.marginWidth = 0;
		composite_7.setLayout(gl_composite_7);
		
		        label_6 = new Label(composite_7, SWT.RIGHT);
		        GridData gd_label_6 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		        gd_label_6.widthHint = 100;
		        label_6.setLayoutData(gd_label_6);
		        label_6.setText(Messages.HEComposite_Operation_Number_As_Bits);
		        homomorphPlainBits = new Text(composite_7, SWT.MULTI | SWT.H_SCROLL);
		        homomorphPlainBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		        homomorphPlainBits.setEditable(false);

		subComposite_3 = new Composite(mainComposite, SWT.NONE);
		subComposite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_subComposite_3 = new GridLayout(2, false);
        gl_subComposite_3.marginHeight = 0;
        gl_subComposite_3.marginWidth = 0;
        subComposite_3.setLayout(gl_subComposite_3);
        label_7 = new Label(subComposite_3, SWT.RIGHT);
        GridData gd_label_7 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_label_7.widthHint = 100;
        label_7.setLayoutData(gd_label_7);
		label_7.setText(Messages.HEComposite_Operation_Number_As_Enc_Vec);
		homomorphEncryptedBits = new Text(subComposite_3, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_homomorphEncryptedBits = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_homomorphEncryptedBits.heightHint = 70;
		homomorphEncryptedBits.setLayoutData(gd_homomorphEncryptedBits);
		homomorphEncryptedBits.setEditable(false);

		Group mainComposite_2 = new Group(parent, SWT.SHADOW_NONE);
		mainComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainComposite_2.setText(Messages.HEComposite_Result_Area);
		GridLayout gl_mainComposite_2 = new GridLayout(1, false);
		gl_mainComposite_2.marginHeight = 0;
		gl_mainComposite_2.marginWidth = 0;
		mainComposite_2.setLayout(gl_mainComposite_2);

		subComposite_4 = new Composite(mainComposite_2, SWT.NONE);
		subComposite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_subComposite_4 = new GridLayout(2, false);
        gl_subComposite_4.marginHeight = 0;
        gl_subComposite_4.marginWidth = 0;
        subComposite_4.setLayout(gl_subComposite_4);
        label_8 = new Label(subComposite_4, SWT.RIGHT);
        GridData gd_label_8 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_label_8.widthHint = 100;
        label_8.setLayoutData(gd_label_8);
		label_8.setText(Messages.HEComposite_Result_Number_As_Enc_Vec);
		homomorphResultEncryptedBits = new Text(subComposite_4, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_homomorphResultEncryptedBits = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_homomorphResultEncryptedBits.heightHint = 70;
		homomorphResultEncryptedBits.setLayoutData(gd_homomorphResultEncryptedBits);
		homomorphResultEncryptedBits.setEditable(false);

		subComposite_5 = new Composite(mainComposite_2, SWT.NONE);
		subComposite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout gl_subComposite_5 = new GridLayout(2, true);
        gl_subComposite_5.marginHeight = 0;
        gl_subComposite_5.marginWidth = 0;
        subComposite_5.setLayout(gl_subComposite_5);
		
		composite_8 = new Composite(subComposite_5, SWT.NONE);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_8 = new GridLayout(2, false);
		gl_composite_8.marginHeight = 0;
		gl_composite_8.marginWidth = 0;
		composite_8.setLayout(gl_composite_8);
		label_9 = new Label(composite_8, SWT.RIGHT);
		GridData gd_label_9 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_label_9.widthHint = 100;
		label_9.setLayoutData(gd_label_9);
		label_9.setText(Messages.HEComposite_Result_Number);
		homomorphResultPlain = new Text(composite_8, SWT.MULTI | SWT.H_SCROLL);
		homomorphResultPlain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		homomorphResultPlain.setEditable(false);
		
		composite_9 = new Composite(subComposite_5, SWT.NONE);
		composite_9.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_9 = new GridLayout(2, false);
		gl_composite_9.marginHeight = 0;
		gl_composite_9.marginWidth = 0;
		composite_9.setLayout(gl_composite_9);
		
        label_10 = new Label(composite_9, SWT.RIGHT);
        GridData gd_label_10 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_label_10.widthHint = 100;
        label_10.setLayoutData(gd_label_10);
        label_10.setText(Messages.HEComposite_Result_Number_As_Bits);
        homomorphResultPlainBits = new Text(composite_9, SWT.MULTI | SWT.H_SCROLL);
        homomorphResultPlainBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        homomorphResultPlainBits.setEditable(false);
	}

	/**
	 * Creates the area in which the operations in plaintext are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHPlainArea(final Composite parent) {
		Group mainComposite_4 = new Group(parent, SWT.SHADOW_NONE);
		mainComposite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		mainComposite_4.setText(Messages.HEComposite_Plain_Data);
		GridLayout gl_mainComposite_4 = new GridLayout(1, false);
		mainComposite_4.setLayout(gl_mainComposite_4);

		Composite subComposite = new Composite(mainComposite_4, SWT.NONE);
		subComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_subComposite = new GridLayout(2, true);
		gl_subComposite.marginHeight = 0;
		gl_subComposite.marginWidth = 0;
		subComposite.setLayout(gl_subComposite);
		
		composite_10 = new Composite(subComposite, SWT.NONE);
		composite_10.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_10 = new GridLayout(2, false);
		gl_composite_10.marginHeight = 0;
		gl_composite_10.marginWidth = 0;
		composite_10.setLayout(gl_composite_10);
		
		Label label = new Label(composite_10, SWT.RIGHT);
		GridData gd_label = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_label.widthHint = 100;
		label.setLayoutData(gd_label);
		label.setText(Messages.HEComposite_Plain_Operations);
		plainOperations = new Text(composite_10, SWT.MULTI | SWT.H_SCROLL);
		plainOperations.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		plainOperations.setEditable(false);
		
		composite_11 = new Composite(subComposite, SWT.NONE);
		composite_11.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite_11 = new GridLayout(2, false);
		gl_composite_11.marginHeight = 0;
		gl_composite_11.marginWidth = 0;
		composite_11.setLayout(gl_composite_11);
		
		        label_11 = new Label(composite_11, SWT.RIGHT);
		        GridData gd_label_11 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		        gd_label_11.widthHint = 100;
		        label_11.setLayoutData(gd_label_11);
		        label_11.setText(Messages.HEComposite_Result);
		        plainResult = new Text(composite_11, SWT.MULTI | SWT.H_SCROLL);
		        plainResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		        plainResult.setEditable(false);
        initDataBindings();
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue sizeLabel_1ObserveValue = PojoProperties.value("size").observe(label_1);
		IObservableValue sizeLabel_3ObserveValue = PojoProperties.value("size").observe(label_3);
		bindingContext.bindValue(sizeLabel_1ObserveValue, sizeLabel_3ObserveValue, null, null);
		//
		return bindingContext;
	}
}