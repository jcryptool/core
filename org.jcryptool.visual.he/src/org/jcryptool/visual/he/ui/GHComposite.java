// -----BEGIN DISCLAIMER-----
package org.jcryptool.visual.he.ui;

import java.math.BigInteger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
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
	
	/** The width of the labels left to the text boxes */
	private int lblWidth = 120;
	
	/**The width of the buttons on the left side */
	private int buttonWidth = 200;
	
	private Label labelDeterminant;
	private Label labelRoot;
	private Composite compositeDeterminant;
	private Composite compositeRoot;
	private Composite compositeBlocks;
	private Label labelBlocks;
	private Composite compositeVector;
	private Group mainGroup;
	private Label lblEncVectorOp1;
	private Label lblBitVectorOp1;
	private Label lblBitVectorOp2;
	private Label lblEncVectorOp2;
	private Label lblResultEncVector;
	private Label lblResultDecimal;
	private Label lblResultBitVector;
	private Label lblResult;
	private Label spacerLabel;

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
				homomorphResultEncryptedBits.setTopIndex(0);
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
	 * Creates the main area
	 */
	private void createMain() {
		final Group mainGroup = new Group(this, SWT.NONE);
		final GridLayout gl = new GridLayout(1, false);
		mainGroup.setText(Messages.HEComposite_Scheme);
        mainGroup.setLayout(gl);
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
		final Composite mainComposite = new Composite(mainGroup, SWT.SHADOW_NONE);
		mainComposite.setLayout(new GridLayout(2, false));
		mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		createGHKeyArea(mainComposite);
		createGHModulusArea(mainComposite);
		createGHHomomorphicArea(mainComposite);
		createGHPlainArea(mainComposite);
		
		initDataBindings();
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
		cText.setTopIndex(0);
		for (int i = 0; i < keyPair.pkBlocksX.length; i++) {
        	pkBlockText.append(i + ": " + keyPair.pkBlocksX[i].toString());
        	pkBlockText.append(pkBlockText.getLineDelimiter());
        }
		pkBlockText.setTopIndex(0);
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
		this.initialEncryptedBits.setTopIndex(0);
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
		this.homomorphEncryptedBits.setTopIndex(0);
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
		this.homomorphEncryptedBits.setTopIndex(0);
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

		keySel.setBackground(ColorService.RED);
		keySel.setForeground(null);
		detText.setBackground(null);
		rootText.setBackground(null);
		cText.setBackground(null);
		pkBlockText.setBackground(null);
	}

	/**
	 * Creates the key area for the Gentry & Halevi scheme
	 * @param parent the composite in which it is created
	 */
	private void createGHKeyArea(final Composite parent) {		
		Group buttonGroup = new Group(parent, SWT.SHADOW_NONE);
		GridData gd_buttonGroup = new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1);
		gd_buttonGroup.widthHint = buttonWidth;
		FillLayout fl_buttonGroup = new FillLayout(SWT.HORIZONTAL);
		fl_buttonGroup.marginHeight = fl_buttonGroup.marginWidth = 2;
		buttonGroup.setLayoutData(gd_buttonGroup);
		buttonGroup.setLayout(fl_buttonGroup);	
		buttonGroup.setText(Messages.HEComposite_Key);
        this.keySel = new Button(buttonGroup, SWT.PUSH);
        this.keySel.setBackground(ColorService.RED);
        this.keySel.setEnabled(true);
        this.keySel.setText(Messages.HEComposite_Keysel);
        this.keySel.setToolTipText(Messages.HEComposite_Key_Tooltip);
        this.keySel.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		WizardDialog wd = new WizardDialog(GHComposite.this.getShell(),
    					new GHKeySelectionWizard(keyPair, fheParams, GHComposite.this.getDisplay()));
        		recalcMinSizeOnPageChange(wd);
        		if (wd.open() == Window.OK) keySelected();
        	}
        });
		
		Group mainGroup = new Group(parent, SWT.SHADOW_NONE);
		mainGroup.setLayout(new GridLayout(2, false));
		((GridLayout)mainGroup.getLayout()).verticalSpacing = 10;
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		mainGroup.setText(Messages.GHComposite_KeyArea_Public_Key);
		
		compositeDeterminant = new Composite(mainGroup, SWT.NONE);
		compositeDeterminant.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_compositeDeterminant = new GridLayout(2, false);
		gl_compositeDeterminant.marginHeight = 0;
		gl_compositeDeterminant.marginWidth = 0;
		compositeDeterminant.setLayout(gl_compositeDeterminant);
		labelDeterminant = new Label(compositeDeterminant, SWT.RIGHT);
		GridData gd_labelDeterminant = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_labelDeterminant.widthHint = lblWidth;
		labelDeterminant.setLayoutData(gd_labelDeterminant);
		labelDeterminant.setText(Messages.HEComposite_GH_KeyArea_Determinant);
		detText = new Text(compositeDeterminant, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL);
		detText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		detText.setEditable(false);
		
		compositeRoot = new Composite(mainGroup, SWT.NONE);
		compositeRoot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_compositeRoot = new GridLayout(2, false);
		gl_compositeRoot.marginHeight = 0;
		gl_compositeRoot.marginWidth = 0;
		compositeRoot.setLayout(gl_compositeRoot);		
		labelRoot = new Label(compositeRoot, SWT.RIGHT);
		GridData gd_labelRoot = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_labelRoot.widthHint = lblWidth;
		labelRoot.setLayoutData(gd_labelRoot);
		labelRoot.setText(Messages.HEComposite_GH_KeyArea_Root);
		rootText = new Text(compositeRoot, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL);
		rootText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		rootText.setEditable(false);
		
		compositeBlocks = new Composite(mainGroup, SWT.NONE);
		compositeBlocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeBlocks = new GridLayout(2, false);
		gl_compositeBlocks.marginHeight = 0;
		gl_compositeBlocks.marginWidth = 0;
		compositeBlocks.setLayout(gl_compositeBlocks);
		labelBlocks = new Label(compositeBlocks, SWT.RIGHT);
		GridData gd_labelBlocks = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_labelBlocks.widthHint = lblWidth;
		labelBlocks.setLayoutData(gd_labelBlocks);
		labelBlocks.setText(Messages.HEComposite_GH_KeyArea_Public_Key_Blocks);
		pkBlockText = new Text(compositeBlocks, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd_pkBlockText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_pkBlockText.heightHint = 50;
		pkBlockText.setLayoutData(gd_pkBlockText);
		pkBlockText.setEditable(false);
		
		compositeVector = new Composite(mainGroup, SWT.NONE);
		compositeVector.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_compositeVector = new GridLayout(2, false);
		gl_compositeVector.marginHeight = 0;
		gl_compositeVector.marginWidth = 0;
		compositeVector.setLayout(gl_compositeVector);		
        Label labelVector = new Label(compositeVector, SWT.RIGHT);
        GridData gd_labelVector = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_labelVector.widthHint = lblWidth;
        labelVector.setLayoutData(gd_labelVector);
        labelVector.setText(Messages.HEComposite_GH_KeyArea_Secret_Vector);
        cText = new Text(compositeVector, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gd_cText = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
        gd_cText.heightHint = 50;
        cText.setLayoutData(gd_cText);
        cText.setEditable(false);
	}

	/**
     * The scheme by Gentry and Halevi requires the modulus to be manually set.
     */
	private void createGHModulusArea(final Composite parent) {
		Group buttonGroup = new Group(parent, SWT.SHADOW_NONE);
		GridData gd_buttonGroup = new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1);
		gd_buttonGroup.widthHint = buttonWidth;
		FillLayout fl_buttonGroup = new FillLayout(SWT.HORIZONTAL);
		fl_buttonGroup.marginHeight = fl_buttonGroup.marginWidth = 2;
		buttonGroup.setLayoutData(gd_buttonGroup);
		buttonGroup.setLayout(fl_buttonGroup);	
		buttonGroup.setText(Messages.HEComposite_Modulus);
    	this.modulusSel = new Button(buttonGroup, SWT.PUSH);
        this.modulusSel.setBackground(ColorService.RED);
        this.modulusSel.setEnabled(false);
        this.modulusSel.setText(Messages.HEComposite_Keysel);
        this.modulusSel.setToolTipText(Messages.HEComposite_Modulus_Tooltip);
        this.modulusSel.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		WizardDialog wd = new WizardDialog(GHComposite.this.getShell(),
    					new GHModulusWizard(data));
        		recalcMinSizeOnPageChange(wd);
        		if (wd.open() == Window.OK) modulusSelected();
        	}
        });
        
        Group mainComposite_2 = new Group(parent, SWT.SHADOW_NONE);
        mainComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        mainComposite_2.setText(Messages.HEComposite_Modulus);
        mainComposite_2.setLayout(new GridLayout(2, false));

        Label textLabel = new Label(mainComposite_2, SWT.RIGHT);
        GridData gd_textLabel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_textLabel.widthHint = lblWidth;
        textLabel.setLayoutData(gd_textLabel);
        textLabel.setText(Messages.HEComposite_Modulus_Label);
        this.modulus = new Text(mainComposite_2, SWT.BORDER);
        modulus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.modulus.setEditable(false);
	}

	/**
	 * Creates the area in which the initial number, its encryption, the operation number, its encryption, the result and the decryption are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHHomomorphicArea(final Composite parent) {
		//Group Button First Operand
        Group grpBtnOp1 = new Group(parent, SWT.SHADOW_NONE);
        GridData gd_grpBtnOp1 = new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1);
        gd_grpBtnOp1.widthHint = buttonWidth;
		FillLayout fl_grpBtnOp1 = new FillLayout(SWT.HORIZONTAL);
		fl_grpBtnOp1.marginHeight = fl_grpBtnOp1.marginWidth = 2;
		grpBtnOp1.setText(Messages.HEComposite_Initial_Text);
		grpBtnOp1.setLayoutData(gd_grpBtnOp1);
		grpBtnOp1.setLayout(fl_grpBtnOp1);
		this.initTextSel = new Button(grpBtnOp1, SWT.PUSH);
		this.initTextSel.setToolTipText(Messages.HEComposite_Initial_Tooltip);
        this.initTextSel.setBackground(ColorService.RED);
        this.initTextSel.setEnabled(false);
        this.initTextSel.setText(Messages.HEComposite_Initial_Text_Select);
        this.initTextSel.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		WizardDialog wd = new WizardDialog(GHComposite.this.getShell(),
        				new GHInitialTextWizard(logMod, data));
        		recalcMinSizeOnPageChange(wd);
        		if (wd.open() == Window.OK) initialTextSelected();
        	}
        });
		
        //Main Group on the right side
		mainGroup = new Group(parent, SWT.SHADOW_NONE);
		mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		mainGroup.setText(Messages.HEComposite_HomomorphicArea);
		GridLayout gl_mainGroup = new GridLayout(1, false);
		mainGroup.setLayout(gl_mainGroup);
		
		//Group First Operand
		Group groupFirstOperand = new Group(mainGroup, SWT.SHADOW_NONE);
		groupFirstOperand.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupFirstOperand.setText(Messages.HEComposite_Initial_Data);
		GridLayout gl_mainComposite = new GridLayout(4, false);
		gl_mainComposite.verticalSpacing = 10;
		groupFirstOperand.setLayout(gl_mainComposite);

		Label lblDecOp1 = new Label(groupFirstOperand, SWT.RIGHT);
		GridData gd_lblDecOp1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblDecOp1.widthHint = lblWidth;
		lblDecOp1.setLayoutData(gd_lblDecOp1);
		lblDecOp1.setText(Messages.HEComposite_Initial_Number);
		initialPlain = new Text(groupFirstOperand, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		initialPlain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initialPlain.setEditable(false);

        lblBitVectorOp1 = new Label(groupFirstOperand, SWT.RIGHT);
        GridData gd_lblBitVectorOp1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblBitVectorOp1.widthHint = lblWidth;
        lblBitVectorOp1.setLayoutData(gd_lblBitVectorOp1);
		lblBitVectorOp1.setText(Messages.HEComposite_Initial_Number_As_Bits);
		initialPlainBits = new Text(groupFirstOperand, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		initialPlainBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initialPlainBits.setEditable(false);

        lblEncVectorOp1 = new Label(groupFirstOperand, SWT.RIGHT);
        GridData gd_lblEncVectorOp1 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblEncVectorOp1.widthHint = lblWidth;
        lblEncVectorOp1.setLayoutData(gd_lblEncVectorOp1);
		lblEncVectorOp1.setText(Messages.HEComposite_Initial_Number_As_Enc_Vec);
		initialEncryptedBits = new Text(groupFirstOperand, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd_initialEncryptedBits = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_initialEncryptedBits.heightHint = 50;
		initialEncryptedBits.setLayoutData(gd_initialEncryptedBits);
		initialEncryptedBits.setEditable(false);
		
		//Group Buttons Operations
		Group grpBtnOperations = new Group(parent, SWT.SHADOW_NONE);      
		GridData gd_grpBtnOperations = new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1);
		gd_grpBtnOperations.widthHint = buttonWidth;
	    FillLayout fl_grpBtnOperations = new FillLayout(SWT.VERTICAL);
		fl_grpBtnOperations.marginHeight = fl_grpBtnOperations.marginWidth = 2;
		grpBtnOperations.setText(Messages.HEComposite_Homomorphic_Text);
		grpBtnOperations.setLayoutData(gd_grpBtnOperations);
        grpBtnOperations.setLayout(fl_grpBtnOperations);

        this.homomorphAdd = new Button(grpBtnOperations, SWT.PUSH);
        this.homomorphAdd.setToolTipText(Messages.HEComposite_Homomorphic_Tooltip_Add);
        this.homomorphAdd.setEnabled(false);
        this.homomorphAdd.setText(Messages.HEComposite_Homomorphic_Add_Select);
        this.homomorphAdd.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		WizardDialog wd = new WizardDialog(GHComposite.this.getShell(),
        				new GHOperationTextWizard(logMod, data));
        		recalcMinSizeOnPageChange(wd);
        		if (wd.open() == Window.OK) addTextSelected();
        	}
        });

        this.homomorphMult = new Button(grpBtnOperations, SWT.PUSH);
        this.homomorphMult.setToolTipText(Messages.HEComposite_Homomorphic_Tooltip_Multiply);
        this.homomorphMult.setEnabled(false);
        this.homomorphMult.setText(Messages.HEComposite_Homomorphic_Mult_Select);
        this.homomorphMult.addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(final SelectionEvent e) {
	    		WizardDialog wd = new WizardDialog(GHComposite.this.getShell(),
		        		new GHOperationTextWizard(logMod, data));
	    		recalcMinSizeOnPageChange(wd);
	    		if (wd.open() == Window.OK) multTextSelected();
	    		}
	        });
        
		spacerLabel = new Label(grpBtnOperations, SWT.NONE);
		spacerLabel.setSize(new Point(0, 70));
		spacerLabel.setSize(130,10);

        this.decryptButton = new Button(grpBtnOperations, SWT.PUSH);
        this.decryptButton.setToolTipText(Messages.HEComposite_Decrypt_Tooltip);
        this.decryptButton.setEnabled(false);
        this.decryptButton.setText(Messages.HEComposite_Decrypt_Select);
        this.decryptButton.addSelectionListener(new SelectionAdapter() {
	        	@Override
				public void widgetSelected(final SelectionEvent e) {
	        		decryptResult();
	        	}
	        });

		//Group Second Operand
		Group groupSecondOperand = new Group(mainGroup, SWT.SHADOW_NONE);
		groupSecondOperand.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupSecondOperand.setText(Messages.HEComposite_Operation_Area);
		GridLayout gl_groupSecondOperand = new GridLayout(4, false);
		groupSecondOperand.setLayout(gl_groupSecondOperand);

		Label lblDecOp2 = new Label(groupSecondOperand, SWT.RIGHT);
		GridData gd_lblDecOp2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblDecOp2.widthHint = lblWidth;
		lblDecOp2.setLayoutData(gd_lblDecOp2);
		lblDecOp2.setText(Messages.HEComposite_Operation_Number);
		homomorphPlain = new Text(groupSecondOperand, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		homomorphPlain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		homomorphPlain.setEditable(false);

        lblBitVectorOp2 = new Label(groupSecondOperand, SWT.RIGHT);
        GridData gd_lblBitVectorOp2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblBitVectorOp2.widthHint = lblWidth;
        lblBitVectorOp2.setLayoutData(gd_lblBitVectorOp2);
        lblBitVectorOp2.setText(Messages.HEComposite_Operation_Number_As_Bits);
        homomorphPlainBits = new Text(groupSecondOperand, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
        homomorphPlainBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        homomorphPlainBits.setEditable(false);

        lblEncVectorOp2 = new Label(groupSecondOperand, SWT.RIGHT);
        GridData gd_lblEncVectorOp2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblEncVectorOp2.verticalIndent = 5;
        gd_lblEncVectorOp2.widthHint = lblWidth;
        lblEncVectorOp2.setLayoutData(gd_lblEncVectorOp2);
		lblEncVectorOp2.setText(Messages.HEComposite_Operation_Number_As_Enc_Vec);
		homomorphEncryptedBits = new Text(groupSecondOperand, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd_homomorphEncryptedBits = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_homomorphEncryptedBits.heightHint = 50;
		gd_homomorphEncryptedBits.verticalIndent = 5;
		homomorphEncryptedBits.setLayoutData(gd_homomorphEncryptedBits);
		homomorphEncryptedBits.setEditable(false);
		
		//Group Reset Buttons
        Group grpBtnReset = new Group(parent, SWT.SHADOW_NONE);
        GridData gd_grpBtnReset = new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1);
        gd_grpBtnReset.widthHint = buttonWidth;
	    FillLayout fl_grpBtnReset = new FillLayout(SWT.VERTICAL);
	    fl_grpBtnReset.marginHeight = fl_grpBtnOperations.marginWidth = 2;
		grpBtnReset.setText(Messages.HEComposite_Reset_Text);
		grpBtnReset.setLayoutData(gd_grpBtnReset);
		grpBtnReset.setLayout(fl_grpBtnReset);

		this.resetNumButton = new Button(grpBtnReset, SWT.PUSH);
		this.resetNumButton.setToolTipText(Messages.HEComposite_Reset_Numbers_Tooltip);
		this.resetNumButton.setEnabled(false);
		this.resetNumButton.setText(Messages.HEComposite_Reset_Numbers);
		this.resetNumButton.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		resetNumbers();
        	}
        });

		this.resetAllButton = new Button(grpBtnReset, SWT.PUSH);
		this.resetAllButton.setToolTipText(Messages.HEComposite_Reset_All_Tooltip);
		this.resetAllButton.setEnabled(false);
		this.resetAllButton.setText(Messages.HEComposite_Reset_All);
		this.resetAllButton.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		resetAll();
        	}
        });

		//Group Result
		Group groupResult = new Group(mainGroup, SWT.SHADOW_NONE);
		groupResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupResult.setText(Messages.HEComposite_Result_Area);
		GridLayout gl_groupResult = new GridLayout(4, false);
		groupResult.setLayout(gl_groupResult);

        lblResultEncVector = new Label(groupResult, SWT.RIGHT);
        GridData gd_lblResultEncVector = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblResultEncVector.widthHint = lblWidth;
        lblResultEncVector.setLayoutData(gd_lblResultEncVector);
		lblResultEncVector.setText(Messages.HEComposite_Result_Number_As_Enc_Vec);
		homomorphResultEncryptedBits = new Text(groupResult, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd_homomorphResultEncryptedBits = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_homomorphResultEncryptedBits.heightHint = 50;
		homomorphResultEncryptedBits.setLayoutData(gd_homomorphResultEncryptedBits);
		homomorphResultEncryptedBits.setEditable(false);

		lblResultDecimal = new Label(groupResult, SWT.RIGHT);
		GridData gd_lblResultDecimal = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblResultDecimal.verticalIndent = 5;
		gd_lblResultDecimal.widthHint = lblWidth;
		lblResultDecimal.setLayoutData(gd_lblResultDecimal);
		lblResultDecimal.setText(Messages.HEComposite_Result_Number);
		homomorphResultPlain = new Text(groupResult, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		homomorphResultPlain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		((GridData)homomorphResultPlain.getLayoutData()).verticalIndent = 5;
		homomorphResultPlain.setEditable(false);
		
        lblResultBitVector = new Label(groupResult, SWT.RIGHT);
        GridData gd_lblResultBitVector = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblResultBitVector.verticalIndent = 5;
        gd_lblResultBitVector.widthHint = lblWidth;
        lblResultBitVector.setLayoutData(gd_lblResultBitVector);
        lblResultBitVector.setText(Messages.HEComposite_Result_Number_As_Bits);
        homomorphResultPlainBits = new Text(groupResult, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
        homomorphResultPlainBits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        ((GridData)homomorphResultPlainBits.getLayoutData()).verticalIndent = 5;
        homomorphResultPlainBits.setEditable(false);
	}

	/**
	 * Creates the area in which the operations in plaintext are shown
	 * @param parent the composite in which it is created
	 */
	private void createGHPlainArea(final Composite parent) {
	    GridData gd_settingsButton = new GridData(SWT.FILL, SWT.BOTTOM, false, true, 1, 1);
	    gd_settingsButton.widthHint = buttonWidth;
		this.settingsButton = new Button(parent, SWT.PUSH);
		this.settingsButton.setLayoutData(gd_settingsButton);
		this.settingsButton.setToolTipText(Messages.HEComposite_Settings_Tooltip);
		this.settingsButton.setText(Messages.HEComposite_Settings);
		this.settingsButton.addSelectionListener(new SelectionAdapter() {
        	@Override
			public void widgetSelected(final SelectionEvent e) {
        		WizardDialog wd = new WizardDialog(GHComposite.this.getShell(),
    					new GHSettingsWizard(data));
        		recalcMinSizeOnPageChange(wd);
        		wd.open();
        	}

        });
	
		Group groupPlainTextOperations = new Group(parent, SWT.SHADOW_NONE);
		groupPlainTextOperations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupPlainTextOperations.setText(Messages.HEComposite_Plain_Data);
		GridLayout gl_mainComposite_4 = new GridLayout(4, false);
		groupPlainTextOperations.setLayout(gl_mainComposite_4);

		Label lblPlainTextOperation = new Label(groupPlainTextOperations, SWT.RIGHT);
		GridData gd_lblPlainTextOperation = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_lblPlainTextOperation.widthHint = lblWidth;
		lblPlainTextOperation.setLayoutData(gd_lblPlainTextOperation);
		lblPlainTextOperation.setText(Messages.HEComposite_Plain_Operations);
		plainOperations = new Text(groupPlainTextOperations, SWT.MULTI | SWT.H_SCROLL | SWT.BORDER);
		plainOperations.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		plainOperations.setEditable(false);

        lblResult = new Label(groupPlainTextOperations, SWT.RIGHT);
        GridData gd_lblResult = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_lblResult.widthHint = lblWidth;
        lblResult.setLayoutData(gd_lblResult);
        lblResult.setText(Messages.HEComposite_Result);
        plainResult = new Text(groupPlainTextOperations, SWT.MULTI | SWT.H_SCROLL| SWT.BORDER);
        plainResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        plainResult.setEditable(false);
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue sizeLabel_1ObserveValue = PojoProperties.value("size").observe(labelDeterminant);
		IObservableValue sizeLabel_3ObserveValue = PojoProperties.value("size").observe(labelBlocks);
		bindingContext.bindValue(sizeLabel_1ObserveValue, sizeLabel_3ObserveValue, null, null);
		//
		return bindingContext;
	}
	
	/**
	 * Adds a IPageChangedListener to the given WizardDialog and 
	 * recalculates the minSize of the shell every time its page changes.
	 * Uses the GridLayout of the page to calculate the minSize.
	 * @param dialog the WizardDialog 
	 */
    private void recalcMinSizeOnPageChange(WizardDialog dialog) {
    	if (dialog != null) {
        	dialog.addPageChangedListener(new IPageChangedListener() {
    			public void pageChanged(PageChangedEvent event) {
    				WizardPage page = ((WizardPage)event.getSelectedPage());
    				Point newMinSize = page.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
    				newMinSize.y += 122 + 69 + 41; //add the height of titleArea, buttonArea and titleBar
    				page.getShell().setMinimumSize(newMinSize);
    			}
    		});
    	}
    }
}