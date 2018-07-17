/**
 *
 */
package org.jcryptool.visual.elGamal.ui;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.ui.textblockloader.NumberblocksAndTextViewer;
import org.jcryptool.crypto.ui.textblockloader.Repr;
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.ElGamalPlugin;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.elGamal.ui.wizards.KeySelectionWizard;
import org.jcryptool.visual.elGamal.ui.wizards.PlaintextforSignatureVerificationWizard;
import org.jcryptool.visual.elGamal.ui.wizards.TextEntryWizard;
import org.jcryptool.visual.elGamal.ui.wizards.UniqueKeyWizard;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * composite, display of everything this visual shows, that is not contained within wizards.
 *
 * @author Michael Gaber
 * @author Thorben Groos
 */
public class ElGamalComposite extends Composite {

    /** buttons for running the wizards and finishing up. */
    private Button keysel, textEnter, runCalc, plaintextForVerification;

    /** shared data object. */
    private ElGamalData data;

    /** field for the signature or the plaintext summed up. */
    private Text signatureText;

    /** button to copy the result to the clipboard. */
    private Button copyButton;

    /** current index for the stepping through the fast exponentiation. */
    private int numberIndex = 0;

    /** small field showing whether the signature is ok when we chose to verify a signature and entered plaintext. */
    private StyledText verifiedText;

    /** Textstyle constant for superscript. */
    private TextStyle superScript;

    /** Textstyle constant for double superscript. */
    private TextStyle superSuperScript;

    /** Textstyle constant for subscript. */
    private TextStyle subscript;

    /** field for displaying p */
    private Text pText;

    /** field for displaying g */
    private Text gText;

    /** field for displaying A */
    private Text bigAText;

    /** field for displaying a */
    private Text aText;

    /** field for displaying the result of the calculation step */
    private Text stepResult;

    /** button used for starting and stepping the stepwise calculation */
    private Button stepButton;

    /** place to display the steps of the stepwise calculation */
    private Composite fastExpTable;

    /** {@link TextLayout} to be able to display superscripts */
    private final TextLayout fastExpText = new TextLayout(getDisplay());

    /** Button for starting the unique key entering wizard */
    private Button uniqueKeyButton;

    /** map of all the data objects */
    private final HashMap<Action, ElGamalData> datas;

    /** whether dialogs are wanted */
    public boolean dialog = false;

    /** combo to list all pages to inherit data from another operation */
    private Combo inheritCombo;

    /** text that replaces stepLabel */
    private Text stepText;
    
    /** Group for the key arguments */
    private Group groupKey;
    
    /** Group for the Text */
    private Group groupText;
    
    /** Group for the calculations */
    private Group groupCalculations;
    
    /** Group for the results */
    private Group groupResult;
    
    /** to check if it is the first step of a decryption/encryption */
    private boolean firstRun = true;

    /** displays, if it is a private or public key */
	private Text text_keyType;

	/** A copy Button for the result of the current step */
	private Button copyStepResult;
	
	/** field for the text entered in the wizard. */
	private NumberblocksAndTextViewer textViewer;
	
	/** field for displaying the result. */
	private NumberblocksAndTextViewer resultViewer;
	


    /**
     * updates the label that shows the current calculated step
     */
	private void updateLabel() {
		if (data.getAction() == Action.DecryptAction) {
			stepText.setText(
					NLS.bind(Messages.ElGamalComposite_step1, new Object[] { numberIndex + 1, data.getCipherTextAsNumbers().size()}));
		} else {
			stepText.setText(
					NLS.bind(Messages.ElGamalComposite_step1, new Object[] { numberIndex + 1, data.getPlainTextAsNumbers().size()}));
		}
	}

    /**
     * constructor calls super and saves a reference to the view.
     *
     * @param parent the parent composite
     * @param style style of the Widget to construct
     * @param datas the set of data objects for all the operations
     * @param action the action of this tab
     * @see Composite#Composite(Composite, int)
     */
    public ElGamalComposite(Composite parent, int style, Action action,
            HashMap<Action, ElGamalData> datas) {
        super(parent, style);
        data = new ElGamalData(action);
        datas.put(action, data);
        this.datas = datas;
        initialize();
    }

    /**
     * initializes the startup situation of this view.
     */
    private void initialize() {
        // basic layout is a
        setLayout(new GridLayout());
        createHead();
        createMainArea();
        createOptionsArea();
    }

	/**
	 * creates the description head of the window to display informations about the
	 * Algorithm itself.
	 */
	private void createHead() {
		Composite head = new Composite(this, SWT.NONE);
		head.setBackground(ColorService.WHITE);
		head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		head.setLayout(new GridLayout());

		Label label = new Label(head, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setBackground(ColorService.WHITE);
		label.setText(Messages.ElGamalComposite_title);

		StyledText stDescription = new StyledText(head, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
		switch (data.getAction()) {
		case EncryptAction:
			stDescription.setText(Messages.ElGamalComposite_description_encrypt);
			break;
		case DecryptAction:
			stDescription.setText(Messages.ElGamalComposite_description_decrypt);
			break;
		case SignAction:
			stDescription.setText(Messages.ElGamalComposite_description_sign);
			break;
		case VerifyAction:
			stDescription.setText(Messages.ElGamalComposite_description_verify);
		default:
			break;
		}
		
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

    /**
     * creates the main area where everything except head and options is contained.
     */
    private void createMainArea() {
        Group groupAlgorithm = new Group(this, SWT.NONE);
        groupAlgorithm.setText(Messages.ElGamalComposite_algorithm);
        GridLayout gl = new GridLayout(2, false);
        groupAlgorithm.setLayout(gl);
        groupAlgorithm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createButtonArea(groupAlgorithm);
        createAlgoArea(groupAlgorithm);
    }

	/**
     * called to set the values of the key selection to their fields.
     */
    private void keySelected() {
        keysel.setBackground(ColorService.GREEN);
        if (data.getAction() == Action.VerifyAction) {
        	plaintextForVerification.setEnabled(true);
        }
        textEnter.setEnabled(true);
        if (data.getModulus() != null) {
            pText.setText(data.getModulus().toString());
        }
        if (data.getGenerator() != null) {
            gText.setText(data.getGenerator().toString());
        }
        if (data.getPublicA() != null) {
            bigAText.setText(data.getPublicA().toString());
        }
        if (data.getA() != null) {
            aText.setText(data.getA().toString());
        }
		//Check if it is a public key or private key. If it is a private key d is set
		if (data.getModulus() != null && data.getGenerator() != null && data.getPublicA() != null && data.getA() != null) {
			text_keyType.setText(Messages.ElGamalComposite_keyType_private);
		} else if (data.getModulus() != null && data.getGenerator() != null && data.getPublicA() != null) {
			//if it is a public key d isn't set.
			text_keyType.setText(Messages.ElGamalComposite_keyType_public);
		}
    }

    /**
     * create the vertical area for the three main buttons.
     *
     * @param parent the parent composite
     */
    private void createButtonArea(final Composite parent) {
        // set up the canvas for the Buttons
    	Composite compositeButtons = new Composite(parent, SWT.NONE);
        compositeButtons.setLayout(new GridLayout());
        GridData gd_btnComposite = new GridData(SWT.FILL, SWT.FILL, false, true);
        gd_btnComposite.verticalIndent = 10;
        compositeButtons.setLayoutData(gd_btnComposite);

        // Key selection Button
        keysel = new Button(compositeButtons, SWT.PUSH);
        keysel.setBackground(ColorService.RED);
        keysel.setEnabled(true);
        keysel.setText(Messages.ElGamalComposite_key_selection);
        GridData gd_keysel = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_keysel.heightHint = 60;
        keysel.setLayoutData(gd_keysel);
        keysel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (dialog) {
                    MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    messageBox.setText(Messages.ElGamalComposite_key_selection);
                    messageBox.setMessage(Messages.ElGamalComposite_key_selection_message_text);
                    messageBox.open();
                }
//				WizardDialog keySelDialog = new WizardDialog(getShell(), new KeySelectionWizard(
//						data, false));
                WizardDialog keySelDialog = new WizardDialog(getShell(), new KeySelectionWizard(data));
				keySelDialog.setHelpAvailable(false);
				if (keySelDialog.open() == Window.OK) {
					keySelected();
				}
            }
        });

     // Text enter Button
		textEnter = new Button(compositeButtons, SWT.PUSH);
		textEnter.setBackground(ColorService.RED);
		textEnter.setEnabled(false);
		switch (data.getAction()) {
		case EncryptAction:
			textEnter.setText(Messages.ElGamalComposite_enter_plaintext);
			break;
		case DecryptAction:
			textEnter.setText(Messages.ElGamalComposite_enter_ciphertext);
			break;
		case SignAction:
			textEnter.setText(Messages.ElGamalComposite_enter_plaintext); // $NON-NLS-1$
			break;
		case VerifyAction:
			textEnter.setText(Messages.ElGamalComposite_enter_signature); 
			break;
		default:
			break;
		}
		GridData gd_textEnter = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd_textEnter.verticalIndent = 10;
		gd_textEnter.heightHint = 60;
		textEnter.setLayoutData(gd_textEnter);
		textEnter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (dialog) {
					MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
					messageBox.setText(Messages.ElGamalComposite_textentry);
					messageBox.setMessage(Messages.ElGamalComposite_textentry_text);
					messageBox.open();
				}
				WizardDialog textEnterDialog = new WizardDialog(getShell(), new TextEntryWizard(data));
				textEnterDialog.setHelpAvailable(false);
				if (textEnterDialog.open() == Window.OK) {
					textEntered();
				}
				
			}
		});

		if (data.getAction() == Action.VerifyAction) {
			plaintextForVerification = new Button(compositeButtons, SWT.PUSH);
			plaintextForVerification.setEnabled(false);
			plaintextForVerification.setText(Messages.ElGamalComposite_enter_plaintext);
			GridData gd_signatureEnter = new GridData(SWT.FILL, SWT.FILL, false, false);
			gd_signatureEnter.heightHint = 60;
			plaintextForVerification.setLayoutData(gd_signatureEnter);
			plaintextForVerification.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (dialog) {
						MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
						messageBox.setText(Messages.ElGamalComposite_enter_plaintext_optional);
						messageBox.setMessage(Messages.ElGamalComposite_dialog_button_plaintext_verification);
						messageBox.open();
					}
					
					WizardDialog plaintextforverification = new WizardDialog(getShell(), 
							new PlaintextforSignatureVerificationWizard(data));
					plaintextforverification.setHelpAvailable(false);
					if (plaintextforverification.open() == Window.OK) {
						textViewer.setContent(data.getPlainTextAsNumbers(), data.getStb());
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
			});
		}
        
        // unique parameter button
        uniqueKeyButton = new Button(compositeButtons, SWT.PUSH);
        uniqueKeyButton.setBackground(ColorService.RED);
        uniqueKeyButton.setEnabled(false);
        uniqueKeyButton.setText(Messages.ElGamalComposite_enter_param);
        uniqueKeyButton.setToolTipText(Messages.ElGamalComposite_enter_param_text);
        GridData gd_uniqueKeyButton = new GridData(SWT.FILL, SWT.FILL, false, false);
        if (data.getAction() == Action.VerifyAction) {
        	gd_uniqueKeyButton.verticalIndent = 20;
        } else {
        	gd_uniqueKeyButton.verticalIndent = 50;
        }
        
        gd_uniqueKeyButton.heightHint = 60;
        uniqueKeyButton.setLayoutData(gd_uniqueKeyButton);
        uniqueKeyButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
            	WizardDialog uniqueKeyButtonDialog = new WizardDialog(getShell(), 
            			new UniqueKeyWizard(data));
            	uniqueKeyButtonDialog.setHelpAvailable(false);
            	if (uniqueKeyButtonDialog.open() == Window.OK) {
                    bEntered();
                }
            }
        });

        // Run Calculations Button
        runCalc = new Button(compositeButtons, SWT.PUSH);
        runCalc.setBackground(ColorService.RED);
        runCalc.setEnabled(false);
        switch (data.getAction()) {
        case EncryptAction:
        	runCalc.setText(Messages.ElGamalComposite_Action_Encrypt);
        	break;
		case DecryptAction:
			runCalc.setText(Messages.ElGamalComposite_Action_Decrypt);
			break;
		case SignAction:
			runCalc.setText(Messages.ElGamalComposite_Action_Sign);
			break;
		case VerifyAction:
			runCalc.setText(Messages.ElGamalComposite_Action_Verify);
			break;
		default:
			break;
        }
        runCalc.setToolTipText(Messages.ElGamalComposite_calculate_popup);
        GridData gd_runCalc = new GridData(SWT.FILL, SWT.FILL, false, false);
        if (data.getAction() == Action.VerifyAction) {
        	gd_runCalc.verticalIndent = 55;
        } else {
        	gd_runCalc.verticalIndent = 90;
        }
        gd_runCalc.heightHint = 60;
        runCalc.setLayoutData(gd_runCalc);
        runCalc.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                uniqueKeyButton.setEnabled(false);
                textEnter.setEnabled(false);
                if (data.getAction() == Action.VerifyAction) {
                	plaintextForVerification.setEnabled(false);
                }
                runCalc.setEnabled(false);
                runCalc.setBackground(ColorService.GREEN);
                // startButton.setEnabled(false);
                stepButton.setEnabled(false);
                if (dialog) {
                    MessageBox message = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
                    message.setText(Messages.ElGamalComposite_finish_calculations);
                    message.setMessage(Messages.ElGamalComposite_finish_calculations_text);
                    message.open();
                }
                if (data.getAction() == Action.SignAction) {
                	//calculate the value s and set it to the resultView.
                	List<Integer> temp = data.getAction().run(data, new ArrayList<Integer>() {/**
						 * 
						 */
						private static final long serialVersionUID = 1L;

					{add(Integer.parseInt(signatureText.getText()));}});
                    resultViewer.setContent(temp, data.getStb());
                    resultViewer.setContent("(" + data.getR().toString() + ", " + temp.get(0) + ")" , data.getStb()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    enableCopyButton();
                    
                    
                    //set the signature to the data object.
                    List<Integer> tempSignature = data.getSignatureAsNumbers();
                    tempSignature.add(0, temp.get(0));
                    tempSignature.add(1, data.getR().intValue());
                    data.setSignatureAsNumbers(tempSignature);
                    
                } else {
                    resultViewer.setContent(data.getAction().run(data, textViewer.getContent()), data.getStb());
                    enableCopyButton();
                    
                }
                finish();
            }
        });
    }

    /**
     * create the main algorithm view.
     *
     * @param parent the parent
     */
    private void createAlgoArea(Composite parent) {
        Composite compositeMain = new Composite(parent, SWT.SHADOW_NONE);
        compositeMain.setLayout(new GridLayout());
        compositeMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createKeyGroup(compositeMain);
        createTextGroup(compositeMain);
        createCalcGroup(compositeMain);
        createResultGroup(compositeMain);
    }

    /**
     * create the keygroup where the components of the key are displayed
     *
     * @param parent the parent
     */
    private void createKeyGroup(Composite parent) {
        groupKey = new Group(parent, SWT.SHADOW_NONE);
        groupKey.setText(Messages.ElGamalComposite_key);
        groupKey.setLayout(new GridLayout(10, false));
        groupKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label plabel = new Label(groupKey, SWT.NONE);
        plabel.setText("d = "); //$NON-NLS-1$
        plabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        
        pText = new Text(groupKey, SWT.READ_ONLY | SWT.BORDER);
        pText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label qlabel = new Label(groupKey, SWT.NONE);
        qlabel.setText("g = "); //$NON-NLS-1$
        GridData gd_qlabel = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_qlabel.horizontalIndent = 30;
        qlabel.setLayoutData(gd_qlabel);
        
        gText = new Text(groupKey, SWT.READ_ONLY | SWT.BORDER);
        gText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label Alabel = new Label(groupKey, SWT.NONE);
        Alabel.setText("B = "); //$NON-NLS-1$
        GridData gd_ALabel = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_ALabel.horizontalIndent = 30;
        Alabel.setLayoutData(gd_ALabel);
        
        bigAText = new Text(groupKey, SWT.READ_ONLY | SWT.BORDER);
        bigAText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label alabel = new Label(groupKey, SWT.NONE);
        alabel.setText("b = "); //$NON-NLS-1$
        GridData gd_aLabel = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_aLabel.horizontalIndent = 30;
        alabel.setLayoutData(gd_aLabel);
        
        aText = new Text(groupKey, SWT.READ_ONLY | SWT.BORDER);
        aText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Label label_keyType = new Label(groupKey, SWT.NONE);
		GridData gd_keyType = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_keyType.horizontalIndent = 30;
		label_keyType.setLayoutData(gd_keyType);
		label_keyType.setText(Messages.ElGamalComposite_keyType_keyType);
		
		text_keyType = new Text(groupKey, SWT.READ_ONLY | SWT.BORDER);
		text_keyType.setEnabled(false);
		GridData gd_text_keyType = new GridData(SWT.FILL, SWT.CENTER, true, false);
		text_keyType.setLayoutData(gd_text_keyType);
    }

    /**
     * create the group where text and "translated" text are displayed.
     *
     * @param parent the parent
     */
    private void createTextGroup(final Composite parent) {
        groupText = new Group(parent, SWT.NONE);
        groupText.setLayout(new GridLayout(2, false));
        groupText.setText(Messages.ElGamalComposite_text);
        groupText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        switch (data.getAction()) {
		case EncryptAction:
		case SignAction:
		case VerifyAction:
			textViewer = new NumberblocksAndTextViewer(groupText, SWT.NONE, Repr.ALL);
			break;
		case DecryptAction:
			textViewer = new NumberblocksAndTextViewer(groupText, SWT.NONE, Repr.ALLNUM);
			break;
		default:
			textViewer = new NumberblocksAndTextViewer(groupText, SWT.NONE, Repr.ALL);
			break;
		}
        
        GridData gdTextViewer = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        gdTextViewer.minimumHeight = 100;
        gdTextViewer.heightHint = 100;
        textViewer.setLayoutData(gdTextViewer);

        if (data.getAction() == Action.SignAction) {
        	new Label(groupText, SWT.NONE).setText(Messages.ElGamalComposite_hashedInput);
        	signatureText = new Text(groupText, SWT.BORDER);
            signatureText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            signatureText.setEditable(false);
        }
        
        if (data.getAction() == Action.VerifyAction) {
        	new Label(groupText, SWT.NONE).setText(Messages.ElGamalComposite_signature);
        	signatureText = new Text(groupText, SWT.BORDER);
            signatureText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            signatureText.setEditable(false);
        }
    }

    /**
     * create the calculations group where the fast exponentiation table and the step result are displayed.
     *
     * @param parent the parent
     */
    private void createCalcGroup(Composite parent) {
        groupCalculations = new Group(parent, SWT.NONE);
        groupCalculations.setLayout(new GridLayout(3, false));
        groupCalculations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        groupCalculations.setText(Messages.ElGamalComposite_calculations);
        
        Label stepwiseCalculation = new Label(groupCalculations, SWT.NONE);
        stepwiseCalculation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        stepwiseCalculation.setText(Messages.ElGamalComposite_stepwiseCalculation);

        stepButton = new Button(groupCalculations, SWT.PUSH);
        stepButton.setText(Messages.ElGamalComposite_start);
        stepButton.setEnabled(false);
        stepButton.setToolTipText(Messages.ElGamalComposite_start_calc);
        stepButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        stepButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (firstRun) {
					uniqueKeyButton.setEnabled(false);
					textEnter.setEnabled(false);
					numberIndex = 0;
					if (data.getAction() == Action.DecryptAction) {
						stepButton.setEnabled(numberIndex != data.getCipherTextAsNumbers().size() - 1);
					} else {
						stepButton.setEnabled(numberIndex != data.getPlainTextAsNumbers().size() - 1);
					}
					initTable();
					updateTable();
					
					if (data.getAction() == Action.SignAction || data.getAction() == Action.VerifyAction) {
						stepButton.setEnabled(false);
						runCalc.setEnabled(false);
						runCalc.setBackground(ColorService.GREEN);
						stepText.setText(NLS.bind(Messages.ElGamalComposite_step1, new Object[] {1, 1}));
						finish();
						if (data.getAction() == Action.VerifyAction) {
							plaintextForVerification.setEnabled(false);
						}
					} else {
						updateLabel();
					}
					
					if (data.getAction() == Action.DecryptAction) {
						if (numberIndex == data.getCipherTextAsNumbers().size() - 1) {
							runCalc.setEnabled(false);
							runCalc.setBackground(ColorService.GREEN);
							finish();
						}
					} else {
						if (numberIndex == data.getPlainTextAsNumbers().size() - 1) {
							runCalc.setEnabled(false);
							runCalc.setBackground(ColorService.GREEN);
							finish();
						}
					}
					
					stepButton.setText(Messages.ElGamalComposite_step);
					stepButton.setToolTipText(Messages.ElGamalComposite_step);
					stepButton.pack();
					firstRun = false;
					
				} else {
					++numberIndex;
					updateTable();
					updateLabel();
					if (data.getAction() == Action.DecryptAction) {
						if (numberIndex == data.getCipherTextAsNumbers().size()- 1) {
							stepButton.setEnabled(false);
							if (data.getAction() == Action.VerifyAction) {
								plaintextForVerification.setEnabled(false);
							}
							runCalc.setEnabled(false);
							runCalc.setBackground(ColorService.GREEN);
							finish();
						}
					} else {
						if (numberIndex == data.getPlainTextAsNumbers().size()- 1) {
							stepButton.setEnabled(false);
							if (data.getAction() == Action.VerifyAction) {
								plaintextForVerification.setEnabled(false);
							}
							runCalc.setEnabled(false);
							runCalc.setBackground(ColorService.GREEN);
							finish();
						}
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

        stepText = new Text(groupCalculations, SWT.BORDER);
        stepText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        stepText.setEnabled(false);

        // set up a composite to draw final the fast exp shit on
        fastExpTable = new Composite(groupCalculations, SWT.NONE);
        GridData gd_fastExpTable = new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1);
        gd_fastExpTable.minimumHeight = 130;
        fastExpTable.setLayoutData(gd_fastExpTable);
        fastExpTable.setVisible(false);

        Label labelStepResult = new Label(groupCalculations, SWT.NONE);
        labelStepResult.setText(Messages.ElGamalComposite_stepresult);
        labelStepResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        
        Composite stepResultComposite = new Composite(groupCalculations, SWT.NONE);
        GridLayout gl_stepResultComposite = new GridLayout(2, false);
        gl_stepResultComposite.marginHeight = 0;
        gl_stepResultComposite.marginWidth = 0;
        stepResultComposite.setLayout(gl_stepResultComposite);
        stepResultComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        
        stepResult = new Text(stepResultComposite, SWT.BORDER | SWT.READ_ONLY);
        stepResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        stepResult.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if (!(stepResult.getText().isEmpty())) {
					copyStepResult.setEnabled(true);
				}
			}
		});
        
        copyStepResult = new Button(stepResultComposite, SWT.PUSH);
        copyStepResult.setText(Messages.ElGamalComposite_copy);
        copyStepResult.setToolTipText(Messages.ElGamalComposite_copy_to_clipboard);
        copyStepResult.setEnabled(false);
        copyStepResult.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
                Clipboard cb = new Clipboard(Display.getCurrent());
                cb.setContents(new Object[] {stepResult.getText()},
                        new Transfer[] {TextTransfer.getInstance()});
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
    }

    /**
     * initializes the fast exponentiation table.
     */
    private void initTable() {
        // get the graphics context
        final GC gc = new GC(fastExpTable);
        // get the standard font we're using everywhere
        final Font normalFont = this.getDisplay().getSystemFont();
        // get the associated fontData
        final FontData normalData = normalFont.getFontData()[0];
        // set the new font height to 12
        normalData.setHeight(12);
        // create small and very small data from the normal data and create
        // matching fonts with each 2pt final less height
        final FontData smallData = new FontData(normalData.getName(), normalData.getHeight() - 2, normalData.getStyle());
        final Font smallFont = new Font(this.getDisplay(), smallData);
        final FontData verySmallData = new FontData(smallData.getName(), smallData.getHeight() - 2,
                smallData.getStyle());
        final Font verySmallFont = new Font(this.getDisplay(), verySmallData);
        // some metrics, whatever they are
        final FontMetrics metrics = gc.getFontMetrics();
        // something to calculate the actual place of the superscripts
        final int baseline = metrics.getAscent() + metrics.getLeading();
        // set the font to standard
        fastExpText.setFont(normalFont);
        // small and very small textstyles for the super- and subscripts
        superScript = new TextStyle(smallFont, null, null);
        superSuperScript = new TextStyle(verySmallFont, null, null);
        subscript = new TextStyle(verySmallFont, null, null);
        // rises, values found by experiment
        superScript.rise = baseline / 2 - 1;
        superSuperScript.rise = baseline - 2;
        subscript.rise = -baseline / 2 + 2;
        fastExpTable.setVisible(true);
        // add a paint listener which paints the text everytime it's needed
        fastExpTable.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				fastExpText.draw(e.gc, 0, 0);
			}
		});
    }

    /**
     * updates the fast exponentiation table i.e. displays the next step
     */
    private void updateTable() {
        switch (data.getAction()) {
            case EncryptAction:
                updateEncrypt();
                break;
            case DecryptAction:
                updateDecrypt();
                break;
            case SignAction:
                updateSign();
                break;
            case VerifyAction:
                updateVerify();
                break;
        }
        fastExpTable.redraw();
    }

    /**
     * Sets up the table for verification
     */
    private void updateVerify() {
        final BigInteger modulus = data.getModulus();
        final StringBuilder sb = new StringBuilder();
        int offset0, offset1 = 0, offset2;
        List<Integer> res = data.getAction().run(data, (ArrayList<Integer>) null);
        sb.append("K = " + data.getR().toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        int s = data.getSignatureAsNumbers().get(1);
        sb.append("s = " + s + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        offset0 = sb.length();
        sb.append("gH(m) = "); //$NON-NLS-1$
        BigInteger ghm = null, hash = null;
        if (data.getPlainTextAsNumbers().size() > 0) {
        	sb.append(data.getGenerator().toString());
            hash = new BigInteger(Lib.hash(data.getStb().revert(data.getPlainTextAsNumbers()), data.getSimpleHash(), data.getModulus()), Constants.HEXBASE);
            offset1 = sb.length();
            sb.append(hash.toString() + " mod " + modulus.toString() + " = "); //$NON-NLS-1$ //$NON-NLS-2$
            sb.append((ghm = data.getGenerator().modPow(hash, modulus)).toString());
        }
        sb.append("\n"); //$NON-NLS-1$
        offset2 = sb.length();
        sb.append("BKKs = " + data.getPublicA().toString() + data.getR().toString() + " ∙ "); //$NON-NLS-1$ //$NON-NLS-2$
        sb.append(data.getR().toString() + s + " mod " + modulus.toString() + " = " + res.get(0)); //$NON-NLS-1$ //$NON-NLS-2$
        // set style
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 1, offset0 + 4);

        if (data.getPlainTextAsNumbers().size() > 0) {	
        	fastExpText.setStyle(superScript, offset1, offset1 + hash.toString().length() - 1);
        }
        
        fastExpText.setStyle(superScript, offset2 + 1, offset2 + 1);
        fastExpText.setStyle(superScript, offset2 + 3, offset2 + 3);
        offset2 = offset2 + 7 + data.getPublicA().toString().length();
        fastExpText.setStyle(superScript, offset2, offset2 + data.getR().toString().length() - 1);
        offset2 = offset2 + data.getR().toString().length() + 5;
        fastExpText.setStyle(superScript, offset2, offset2 + String.valueOf(s).length() - 1);

        // set results
        stepResult.setText("B^K*K^s = " + res.get(0)); //$NON-NLS-1$
        
        if (!(ghm == null)) {
        	verifiedText.setData(ghm.intValue());
        }

        resultViewer.setContent(res, data.getStb());
        enableCopyButton();
    }

    /**
     * Sets up the table for signing
     */
    private void updateSign() {
        final StringBuilder sb = new StringBuilder();
        final BigInteger modulus = data.getModulus();
        int offset0, offset1;
        List<Integer> res = data.getAction().run(data, new ArrayList<Integer>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{add(Integer.parseInt(signatureText.getText()));}});
        
        sb.append("k = " + data.getK() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        offset0 = sb.length();
        sb.append("K = gk = " + data.getGenerator() + data.getK()); //$NON-NLS-1$
        sb.append(" mod " + modulus + " = " + data.getR() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        offset1 = sb.length();
        sb.append("s = (H(m) - aK)k-1 = (" + signatureText.getText() + " - "); //$NON-NLS-1$ //$NON-NLS-2$
        sb.append(data.getA() + " ∙ " + data.getR() + ") ∙ "); //$NON-NLS-1$ //$NON-NLS-2$
        sb.append(data.getK().modInverse(modulus.subtract(BigInteger.ONE)));
        sb.append(" mod " + modulus.subtract(BigInteger.ONE) + " = " + res.get(0)); //$NON-NLS-1$ //$NON-NLS-2$
        
        // style it
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
        offset0 = offset0 + 9 + data.getGenerator().toString().length();
        fastExpText.setStyle(superScript, offset0, offset0 + data.getK().toString().length() - 1);
        fastExpText.setStyle(superScript, offset1 + 16, offset1 + 17);
        
        // set result
        stepResult.setText("K = " + data.getR() + ", s = " + res.get(0)); //$NON-NLS-1$ //$NON-NLS-2$
        resultViewer.setContent("(" + data.getR().toString(256) + ", " + String.valueOf(res.get(0)) + ")", data.getStb()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        enableCopyButton();
        
        //save the resulting signatur in the data object.
        List<Integer> tempSignature = data.getSignatureAsNumbers();
        tempSignature.add(0, res.get(0));
        tempSignature.add(1, data.getR().intValue());
        data.setSignatureAsNumbers(tempSignature);
        
    }

    /**
     * Sets up the table for signing
     */
    private void updateDecrypt() {
        final StringBuilder sb = new StringBuilder();
        final BigInteger modulus = data.getModulus();
        int offset0, offset1, offset2;
        BigInteger x;
        
        sb.append("x = d - b - 1 = " + modulus + " - " + data.getA() + " - 1 = "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        sb.append((x = modulus.subtract(data.getA()).subtract(BigInteger.ONE)) + "\n"); //$NON-NLS-1$
        offset0 = sb.length();
        sb.append("K = gk mod d = " + data.getGenerator() + data.getB() + " mod " + modulus + " = "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        sb.append(data.getGPowB() + "\n"); //$NON-NLS-1$
        offset1 = sb.length();
        sb.append("m = KxM = " + data.getGPowB()); //$NON-NLS-1$
        offset2 = sb.length();
        sb.append(x + " ∙ " + data.getCipherTextAsNumbers().get(numberIndex) + " mod " + modulus + " = "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        List<Integer> res = data.getAction().run(data, new ArrayList<Integer>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{add(data.getCipherTextAsNumbers().get(numberIndex));}});
        sb.append(res.get(0));
        
        // set style
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
        offset0 = offset0 + 15 + data.getGenerator().toString().length();
        fastExpText.setStyle(superScript, offset0, offset0
                + data.getB().toString().length() - 1);
        fastExpText.setStyle(superScript, offset1 + 5, offset1 + 5);
        fastExpText.setStyle(superScript, offset2, offset2 + x.toString().length() - 1);
        
        // set result
        stepResult.setText("M = " + res.get(0)); //$NON-NLS-1$
        
        List<Integer> tempContent = resultViewer.getContent() == null ? new ArrayList<>() : resultViewer.getContent();
        tempContent.add(res.get(0));
        resultViewer.setContent(tempContent, data.getStb());
        enableCopyButton();
        
    }

    /**
     * Sets up the table for signing
     */
    private void updateEncrypt() {
        final StringBuilder sb = new StringBuilder();
        final BigInteger modulus = data.getModulus();
        int offset0, offset1, offset2;
        sb.append("k = " + data.getB() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        offset0 = sb.length();
        sb.append("K = gk mod d = " + data.getGenerator()); //$NON-NLS-1$
        sb.append(data.getB() + " mod " + modulus); //$NON-NLS-1$
        sb.append(" = " + data.getGPowB() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        offset1 = sb.length();
        sb.append("M = Bkm mod d = " + data.getPublicA()); //$NON-NLS-1$
        offset2 = sb.length();
        sb.append(data.getB() + " ∙ " + data.getPlainTextAsNumbers().get(numberIndex)); //$NON-NLS-1$
        sb.append(" mod " + modulus + " = "); //$NON-NLS-1$ //$NON-NLS-2$
        
        List<Integer> res = data.getAction().run(data, new ArrayList<Integer>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{add(data.getPlainTextAsNumbers().get(numberIndex));}});
        
        sb.append(res.get(0));
        
        // set style
        int tmp;
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
        fastExpText.setStyle(superScript,
                tmp = offset0 + 15 + data.getGenerator().toString().length(), tmp
                        + data.getB().toString().length());
        fastExpText.setStyle(superScript, offset1 + 5, offset1 + 5);
        fastExpText.setStyle(superScript, offset2, offset2
                + data.getB().toString().length() - 1);
        stepResult.setText("M = " + res.get(0)); //$NON-NLS-1$
        // If no value is in the temp result. Create a new empty list in which the values can be entered.
        List<Integer> tempContent = resultViewer.getContent() == null ? new ArrayList<>() : resultViewer.getContent();
        tempContent.add(res.get(0));
        resultViewer.setContent(tempContent, data.getStb());
        enableCopyButton();
    }

    /**
     * create the resultgroup where the result and the copy button are displayed.
     *
     * @param parent the parent
     */
    private void createResultGroup(final Composite parent) {
        groupResult = new Group(parent, SWT.NONE);
        groupResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        groupResult.setLayout(new GridLayout(3, false));
        groupResult.setText(Messages.ElGamalComposite_result);
        
        // Change the optional format in which the result can be displayed dependent on the current action
        switch (data.getAction()) {
		case EncryptAction:
		case VerifyAction:
			resultViewer = new NumberblocksAndTextViewer(groupResult, SWT.NONE, Repr.ALLNUM);
			break;
		case SignAction:
			//Little trick. If Repr.String is only once in the array there will be no radio button that can be displayed.
			resultViewer = new NumberblocksAndTextViewer(groupResult, SWT.NONE, new Repr[] {Repr.STRING, Repr.STRING} );
			break;
		case DecryptAction: 
			resultViewer = new NumberblocksAndTextViewer(groupResult, SWT.NONE, Repr.ALL);
			break;
		default:
			resultViewer = new NumberblocksAndTextViewer(groupResult, SWT.NONE, Repr.ALL);
			break;
		}
        
        GridData gd_resultViewer = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_resultViewer.minimumHeight = 60;
        resultViewer.setLayoutData(gd_resultViewer);

        verifiedText = new StyledText(groupResult, SWT.READ_ONLY);
        verifiedText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        verifiedText.setText("                "); //$NON-NLS-1$

		copyButton = new Button(groupResult, SWT.PUSH);
		copyButton.setEnabled(false);
		copyButton.setText(Messages.ElGamalComposite_copy);
		copyButton.setToolTipText(Messages.ElGamalComposite_copy_to_clipboard);
		copyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		copyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				final Clipboard cb = new Clipboard(Display.getCurrent());
				cb.setContents(new Object[] { data.getStb().revert(resultViewer.getContent()) },
						new Transfer[] { TextTransfer.getInstance() });
			}
		});
	}
    
    /**
     * Enables the copy button in the bottom right corner.
     * In case of Action.VerifyAction it check whether the signature
     * is valid or not an display it to the left of the copy
     * button.
     */
    private void enableCopyButton() {
    	copyButton.setEnabled(true);
    	if (data.getAction() == Action.VerifyAction && !textViewer.getContent().isEmpty()) {
			String text;
			if (verifiedText.getData() != null && resultViewer.getContent().get(0).equals(verifiedText.getData())) {
				text = Messages.ElGamalComposite_valid;
                verifiedText.setForeground(ColorService.GREEN);
            } else {
                text = Messages.ElGamalComposite_invalid;
                verifiedText.setForeground(ColorService.RED);
            }
            verifiedText.setText(text);
    	}
    }

    /**
     * creates the bottom options area.
     */
    private void createOptionsArea() {
        // setup the main layout for this group
        final Group optionsGroup = new Group(this, SWT.NONE);
        optionsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        optionsGroup.setLayout(new GridLayout(5, false));
        optionsGroup.setText(Messages.ElGamalComposite_options);

        // temporary spacer
        final Button keyButton = new Button(optionsGroup, SWT.PUSH);
        keyButton.setText(Messages.ElGamalComposite_key_generation);
        keyButton.setToolTipText(Messages.ElGamalComposite_key_generation_popup);
        keyButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        keyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				WizardDialog keyButtonDialog = new WizardDialog(new Shell(), new KeySelectionWizard(null));
				keyButtonDialog.setHelpAvailable(false);
				keyButtonDialog.open();
			}
        });

        // initialize copy data selector
        final Label l = new Label(optionsGroup, SWT.NONE);
        l.setText(Messages.ElGamalComposite_inherit_from);
        inheritCombo = new Combo(optionsGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        inheritCombo.add(""); //$NON-NLS-1$
        
        switch (data.getAction()) {
		case DecryptAction:
			inheritCombo.add(Messages.ElGamalComposite_encrypt);
	        inheritCombo.setData("1", Action.EncryptAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_sign);
	        inheritCombo.setData("2", Action.SignAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_verify);
	        inheritCombo.setData("3", Action.VerifyAction); //$NON-NLS-1$
			break;
		case EncryptAction:
	        inheritCombo.add(Messages.ElGamalComposite_decrypt);
	        inheritCombo.setData("1", Action.DecryptAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_sign);
	        inheritCombo.setData("2", Action.SignAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_verify);
	        inheritCombo.setData("3", Action.VerifyAction); //$NON-NLS-1$
			break;
		case SignAction:
			inheritCombo.add(Messages.ElGamalComposite_encrypt);
	        inheritCombo.setData("1", Action.EncryptAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_decrypt);
	        inheritCombo.setData("2", Action.DecryptAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_verify);
	        inheritCombo.setData("3", Action.VerifyAction); //$NON-NLS-1$
			break;
		case VerifyAction:
			inheritCombo.add(Messages.ElGamalComposite_encrypt);
	        inheritCombo.setData("1", Action.EncryptAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_decrypt);
	        inheritCombo.setData("2", Action.DecryptAction); //$NON-NLS-1$
	        inheritCombo.add(Messages.ElGamalComposite_sign);
	        inheritCombo.setData("3", Action.SignAction); //$NON-NLS-1$
			break;
		default:
			break;
        
        }
        inheritCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final Action newAction = (Action) inheritCombo.getData("" //$NON-NLS-1$
                        + ((Combo) e.widget).getSelectionIndex());
                if (((Combo) e.widget).getSelectionIndex() == 0 || newAction == data.getAction()) {
                	//Return if the user tries to inherit data to the dame tab or select no tab to inherit from
                    return;
                } else {
                	boolean proceed = true;
                	MessageBox mb = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
                    mb.setText(Messages.ElGamalComposite_sure);
                    mb.setMessage(Messages.ElGamalComposite_data_loss);
                    //Check if the warning that data will be lost is necessary
                	if (data.getModulus() != null) {
                        proceed = mb.open() == SWT.OK;
                	}
                	//If Ok is pressed in the dialog continue. 
                	//If no data will be lot continue automatically.
                    if (proceed) {
                        final ElGamalData oldData = datas.get(newAction);
                        reset();
                        
                        //Only insert Data in the textfields if the inherit() returns true 
                        if (data.inherit(oldData)) {
	                        if (data.getModulus() != null) {
	                            keySelected();
	                            // if we got plaintext/ciphertext/signature, set
	                            // them up as well
	                            if (!data.getPlainTextAsNumbers().isEmpty() || !data.getCipherTextAsNumbers().isEmpty()) {
	                                textEntered();
	                                if (data.getB() != null) {
	                                    bEntered();
	                                    uniqueKeyButton.setEnabled(false);
	                                }
	                            }
	                        }
                        } else {
            	    		JCTMessageDialog.showInfoDialog(new Status(IStatus.INFO, ElGamalPlugin.PLUGIN_ID,
            	    				Messages.ElGamalComposite_inheritFail));
                        }
                    }
                }
            }

        });

        // initialize dialog checkbox
        final Button dialogButton = new Button(optionsGroup, SWT.CHECK);
        dialogButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        dialogButton.setText(Messages.ElGamalComposite_show_dialogs);
        dialogButton.setSelection(dialog);
        dialogButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                dialog = ((Button) e.widget).getSelection();
            }
        });

        // initialize reset button
        final Button reset = new Button(optionsGroup, SWT.PUSH);
        reset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        reset.setText(Messages.ElGamalComposite_reset);
        reset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
            	softreset();
            }
        });
    }

    /**
     * reset the tab to its initial state
     */
    private void reset() {
        keysel.setEnabled(true);
        keysel.setBackground(ColorService.RED);
        textEnter.setEnabled(false);
        textEnter.setBackground(ColorService.RED);
        
        if (data.getAction() == Action.VerifyAction) {
        	plaintextForVerification.setEnabled(false);
        }
        if (data.getAction() == Action.VerifyAction || data.getAction() == Action.SignAction) {
        	signatureText.setText(""); //$NON-NLS-1$
        }
        
        uniqueKeyButton.setEnabled(false);
        uniqueKeyButton.setBackground(ColorService.RED);
        runCalc.setEnabled(false);
        runCalc.setBackground(ColorService.RED);
        data = new ElGamalData(data.getAction());
        datas.put(data.getAction(), data);
        pText.setText(""); //$NON-NLS-1$
        gText.setText(""); //$NON-NLS-1$
        bigAText.setText(""); //$NON-NLS-1$
        aText.setText(""); //$NON-NLS-1$
        text_keyType.setText(""); //$NON-NLS-1$
        textViewer.setContent("", data.getStb()); //$NON-NLS-1$
        fastExpTable.setVisible(false);
        stepResult.setText(""); //$NON-NLS-1$
        stepButton.setEnabled(false);
        
        firstRun = true;
        numberIndex = 0;

        stepButton.setText(Messages.ElGamalComposite_start);
        stepButton.setToolTipText(Messages.ElGamalComposite_start_calc);
        stepButton.pack();
        stepText.setText(""); //$NON-NLS-1$
        resultViewer.setContent("", data.getStb()); //$NON-NLS-1$
        copyButton.setEnabled(false);
        copyStepResult.setEnabled(false);
        verifiedText.setText(""); //$NON-NLS-1$
    }
    
    /*
     * resets the tab but keep the key
     */
    private void softreset() {
    	
    	//check if a key is entered
    	if (data.getModulus() == null) {
    		return;
    	}

    	textEnter.setEnabled(true);
        textEnter.setBackground(ColorService.RED);
        if (data.getAction() == Action.VerifyAction) {
        	plaintextForVerification.setEnabled(true);
        }
        if (data.getAction() == Action.VerifyAction || data.getAction() == Action.SignAction) {
        	signatureText.setText(""); //$NON-NLS-1$
        }
        uniqueKeyButton.setEnabled(false);
        uniqueKeyButton.setBackground(ColorService.RED);
        runCalc.setEnabled(false);
        runCalc.setBackground(ColorService.RED);
        
        //Keep the old data of the keys
        ElGamalData oldData = data;
        data = new ElGamalData(data.getAction());
        datas.put(data.getAction(), data);
        data.setA(oldData.getA());
        data.setB(oldData.getB());
        data.setContactName(oldData.getContactName());
        data.setGenerator(oldData.getGenerator());
        data.setModulus(oldData.getModulus());
        data.setPassword(data.getPassword());
        data.setPrivateAlias(oldData.getPrivateAlias());
        data.setPublicA(oldData.getPublicA());
        data.setPublicAlias(oldData.getPublicAlias());
        data.setR(oldData.getR());
        
        textViewer.setContent("", data.getStb()); //$NON-NLS-1$
        fastExpTable.setVisible(false);
        stepResult.setText(""); //$NON-NLS-1$
        stepButton.setEnabled(false);
        
        firstRun = true;
        numberIndex = 0;

        stepButton.setText(Messages.ElGamalComposite_start);
        stepButton.setToolTipText(Messages.ElGamalComposite_start_calc);
        stepButton.pack();
        stepText.setText(""); //$NON-NLS-1$
        
        resultViewer.setContent("", data.getStb()); //$NON-NLS-1$
        copyButton.setEnabled(false);
        copyStepResult.setEnabled(false);
        verifiedText.setText(""); //$NON-NLS-1$
    }

    /**
     * called when the textentry has finished
     */
    private void textEntered() {
        keysel.setEnabled(false);
        textEnter.setBackground(ColorService.GREEN);
        uniqueKeyButton.setEnabled(true);
        
        switch (data.getAction()) {
            case EncryptAction:
            	textViewer.setContent(data.getPlainTextAsNumbers(), data.getStb());
                break;
            case SignAction:
            	textViewer.setContent(data.getPlainTextAsNumbers(), data.getStb());
            	signatureText.setText(Integer.toString(Integer.parseInt(Lib.hash(data.getStb().revert(data.getPlainTextAsNumbers()), data.getSimpleHash(), data.getModulus()), 16)));
            	break;
            case DecryptAction:
            	textViewer.setContent(data.getCipherTextAsNumbers(), data.getStb());
                break;
            case VerifyAction:
            	textViewer.setContent(data.getPlainTextAsNumbers(), data.getStb());
                plaintextForVerification.setEnabled(true);
                signatureText.setText("(" + data.getSignatureAsNumbers().get(0) + ", " + data.getSignatureAsNumbers().get(1) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                uniqueKeyButton.setEnabled(false);
                uniqueKeyButton.setBackground(ColorService.GREEN);
                runCalc.setEnabled(true);
                stepButton.setEnabled(true);
                break;
            default:
                break;
        }
    }

    /**
     * finished up after the operation by setting the plaintext ciphertext or signature to the data object
     */
    private void finish() {
        switch (data.getAction()) {
            case EncryptAction:
            	data.setCipherTextAsNumbers(resultViewer.getContent());
                break;
            case DecryptAction:
            	data.setPlainTextAsNumbers(resultViewer.getContent());
                break;
            case SignAction:
            	List<Integer> temp = data.getSignatureAsNumbers();
            	temp.set(1, data.getR().intValue());
            	data.setSignatureAsNumbers(temp);
                break;
		default:
			break;
        }
    }

    /**
     * called when b has been entered via the wizard or inheritance
     */
    private void bEntered() {
        textEnter.setEnabled(false);
        uniqueKeyButton.setBackground(ColorService.GREEN);
        runCalc.setEnabled(true);
        stepButton.setEnabled(true);
    }

}
