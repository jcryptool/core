/**
 *
 */
package org.jcryptool.visual.elGamal.ui;

import java.math.BigInteger;
import java.util.HashMap;

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
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.elGamal.Action;
import org.jcryptool.visual.elGamal.ElGamalData;
import org.jcryptool.visual.elGamal.Messages;
import org.jcryptool.visual.elGamal.ui.wizards.KeySelectionWizard;
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
    private Button keysel, textEnter, runCalc;

    /** shared data object. */
    private ElGamalData data;

    /** field for the text entered in the wizard. */
    private Text textText;

    /** field for the signature or the text translated to numbers. */
    private Text numberText;

    /** field for displaying the result. */
    private Text resultText;

    /** button to copy the result to the clipboard. */
    private Button copyButton;

    /** array containing the split up numbertext. */
    private String[] numbers;

    /** current index for the stepping through the fast exponentiation. */
//    private int numberIndex;
    private int numberIndex = 0;

    /**
     * small field showing whether the signature is ok when we chose to verify a signature and entered plaintext.
     */
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

    /** whether dialogs are wanted TODO: default to true. */
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

    /**
     * updates the label that shows the current calculated step
     */
	private void updateLabel() {
		stepText.setText(
				NLS.bind(Messages.ElGamalComposite_step1, new Object[] { numberIndex + 1, numbers.length }));

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
				WizardDialog keySelDialog = new WizardDialog(getShell(), new KeySelectionWizard(
						data.getAction(), data, false));
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
        textEnter.setText(Messages.ElGamalComposite_enter_text);
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
				WizardDialog textEnterDialog = new WizardDialog(ElGamalComposite.this.getShell(),
						new TextEntryWizard(ElGamalComposite.this.data.getAction(), ElGamalComposite.this.data));
				textEnterDialog.setHelpAvailable(false);
				if (textEnterDialog.open() == Window.OK) {
					ElGamalComposite.this.textEntered();
				}
			}

        });

        // unique parameter button
        uniqueKeyButton = new Button(compositeButtons, SWT.PUSH);
        uniqueKeyButton.setBackground(ColorService.RED);
        uniqueKeyButton.setEnabled(false);
        uniqueKeyButton.setText(Messages.ElGamalComposite_enter_param);
        uniqueKeyButton.setToolTipText(Messages.ElGamalComposite_enter_param_text);
        GridData gd_uniqueKeyButton = new GridData(SWT.FILL, SWT.FILL, false, false);
        gd_uniqueKeyButton.verticalIndent = 50;
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
        case EncryptAction : {
        	runCalc.setText(Messages.ElGamalComposite_Action_Encrypt);
        	break;
        }
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
        gd_runCalc.verticalIndent = 90;
        gd_runCalc.heightHint = 60;
        runCalc.setLayoutData(gd_runCalc);
        runCalc.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                uniqueKeyButton.setEnabled(false);
                textEnter.setEnabled(false);
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
                    final String value = data.getAction().run(data, numberText.getText().split(" ")); //$NON-NLS-1$
                    resultText.setText("(" + data.getR().toString(Constants.HEXBASE) + ", " + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    resultText.setText(data.getAction().run(data, numberText.getText().split(" "))); //$NON-NLS-1$
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
    private void createAlgoArea(final Composite parent) {
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
    private void createKeyGroup(final Composite parent) {
        groupKey = new Group(parent, SWT.SHADOW_NONE);
        groupKey.setText(Messages.ElGamalComposite_key);
        groupKey.setLayout(new GridLayout(10, false));
        groupKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label plabel = new Label(groupKey, SWT.NONE);
        plabel.setText("p = "); //$NON-NLS-1$
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
        Alabel.setText("A = "); //$NON-NLS-1$
        GridData gd_ALabel = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd_ALabel.horizontalIndent = 30;
        Alabel.setLayoutData(gd_ALabel);
        
        bigAText = new Text(groupKey, SWT.READ_ONLY | SWT.BORDER);
        bigAText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        Label alabel = new Label(groupKey, SWT.NONE);
        alabel.setText("a = "); //$NON-NLS-1$
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
        groupText.setLayout(new GridLayout());
        groupText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        
        new Label(groupText, SWT.NONE).setText(Messages.ElGamalComposite_text);
        
        textText = new Text(groupText, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        textText.setText("\n\n\n"); //$NON-NLS-1$
        textText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        textText.addModifyListener(new ModifyListener() {
        	
        	@Override
            public void modifyText(ModifyEvent e) {
                if (textText.getText().equals("")) { //$NON-NLS-1$
                    return;
                }
                if (data.getAction() == Action.SignAction) {
                    numberText.setText(Lib.hash(textText.getText(), data.getSimpleHash(), data.getModulus()));
                } else {
                    final StringBuffer sb = new StringBuffer();
                    final String s = ((Text) e.widget).getText();
                    for (int i = 0; i < s.length(); ++i) {
                        sb.append(Integer.toHexString(s.charAt(i)));
                        if (i != s.length() - 1) {
                            sb.append(' ');
                        }
                    }
                    numberText.setText(sb.toString());
                }
            }
        });
        
        new Label(groupText, SWT.NONE).setText(Messages.ElGamalComposite_hextext);
        
        numberText = new Text(groupText, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        numberText.setText("\n\n\n"); //$NON-NLS-1$
        numberText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    /**
     * create the calculations group where the fast exponentiation table and the step result are displayed.
     *
     * @param parent the parent
     */
    private void createCalcGroup(final Composite parent) {
        groupCalculations = new Group(parent, SWT.NONE);
        groupCalculations.setLayout(new GridLayout(3, false));
        groupCalculations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        groupCalculations.setText(Messages.ElGamalComposite_calculations);

        
        stepButton = new Button(groupCalculations, SWT.PUSH);
        stepButton.setText(Messages.ElGamalComposite_start);
        stepButton.setEnabled(false);
        stepButton.setToolTipText(Messages.ElGamalComposite_start_calc);
        stepButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        stepButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (firstRun) {
					uniqueKeyButton.setEnabled(false);
					textEnter.setEnabled(false);
					numbers = ElGamalComposite.this.numberText.getText().split(" "); //$NON-NLS-1$
					numberIndex = 0;
					stepButton.setEnabled(numberIndex != numbers.length - 1);
					initTable();
					updateTable();
					updateLabel();
					if (numberIndex == numbers.length - 1) {
						runCalc.setEnabled(false);
						runCalc.setBackground(ColorService.GREEN);
						finish();
					}
					stepButton.setText(Messages.ElGamalComposite_step);
					stepButton.pack();
					firstRun = false;
				} else {
					++numberIndex;
					updateTable();
					updateLabel();
					if (numberIndex == numbers.length - 1) {
						stepButton.setEnabled(false);
						runCalc.setEnabled(false);
						runCalc.setBackground(ColorService.GREEN);
						finish();
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

        stepText = new Text(groupCalculations, SWT.BORDER);
        stepText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        stepText.setEnabled(false);

        // set up a composite to draw final the fast exp shit on
        fastExpTable = new Composite(groupCalculations, SWT.NONE);
        GridData gd_fastExpTable = new GridData(SWT.FILL, SWT.CENTER, true, true, 3, 1);
        gd_fastExpTable.minimumHeight = 100;
        fastExpTable.setLayoutData(gd_fastExpTable);
        fastExpTable.setVisible(false);

        Label labelStepResult = new Label(groupCalculations, SWT.NONE);
        labelStepResult.setText(Messages.ElGamalComposite_stepresult);
        
        stepResult = new Text(groupCalculations, SWT.BORDER | SWT.READ_ONLY);
        stepResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
    }

    /**
     * initializes the fast exponentiation table.
     */
    private void initTable() {
        // get the graphics context
        final GC gc = new GC(this.fastExpTable);
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
        // reset resultText if it contains \n (only first run)
        if (resultText.getText().contains("\n")) { //$NON-NLS-1$
            resultText.setText(""); //$NON-NLS-1$
        }
        switch (this.data.getAction()) {
            case EncryptAction:
                // TODO das hier in ne konstante bei initialisierung der tabelle
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
        BigInteger value;
        int offset0;
        int offset1 = 0;
        int offset2;
        value = new BigInteger(data.getAction().run(data, (String) null), Constants.HEXBASE);
        sb.append("r = "); //$NON-NLS-1$
        sb.append(data.getR().toString(Constants.HEXBASE));
        sb.append("\ns = "); //$NON-NLS-1$
        final BigInteger s = new BigInteger(
                data.getSignature().split(",")[1].replace(')', ' ').trim(), Constants.HEXBASE); //$NON-NLS-1$
        sb.append(s.toString(Constants.HEXBASE));
        sb.append("\n"); //$NON-NLS-1$
        offset0 = sb.length();
        sb.append("gH(m) = "); //$NON-NLS-1$
        BigInteger ghm = null, hash = null;
        if (data.getPlainText().length() > 0) {
            sb.append(data.getGenerator().toString(Constants.HEXBASE));
            hash = new BigInteger(
                    Lib.hash(data.getPlainText(), data.getSimpleHash(), data.getModulus()),
                    Constants.HEXBASE);
            offset1 = sb.length();
            sb.append(hash.toString(Constants.HEXBASE));
            sb.append(" mod "); //$NON-NLS-1$
            sb.append(modulus.toString(Constants.HEXBASE));
            sb.append(" = "); //$NON-NLS-1$
            sb.append((ghm = data.getGenerator().modPow(hash, modulus)).toString(Constants.HEXBASE));
        }
        sb.append("\n"); //$NON-NLS-1$
        offset2 = sb.length();
        sb.append("Arrs = "); //$NON-NLS-1$
        sb.append(data.getPublicA().toString(Constants.HEXBASE));
        sb.append(data.getR().toString(Constants.HEXBASE));
        sb.append(" ∙ "); //$NON-NLS-1$
        sb.append(data.getR().toString(Constants.HEXBASE));
        sb.append(s.toString(Constants.HEXBASE));
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus.toString(Constants.HEXBASE));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(value.toString(Constants.HEXBASE));
        // set style
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 1, offset0 + 3);
        if (data.getPlainText().length() > 0) {
            fastExpText.setStyle(superScript, offset1, offset1 + hash.toString(Constants.HEXBASE).length()
                    - 1);
        }
        fastExpText.setStyle(superScript, offset2 + 1, offset2 + 1);
        fastExpText.setStyle(superScript, offset2 + 3, offset2 + 3);
        offset2 = offset2 + 7 + data.getPublicA().toString(Constants.HEXBASE).length();
        fastExpText.setStyle(superScript, offset2, offset2
                + data.getR().toString(Constants.HEXBASE).length() - 1);
        offset2 = offset2 + data.getR().toString(Constants.HEXBASE).length() + 5;
        fastExpText.setStyle(superScript, offset2, offset2 + s.toString(Constants.HEXBASE).length() - 1);

        // set results
        stepResult.setText("A^r*r^s = " + value.toString(Constants.HEXBASE)); //$NON-NLS-1$
        verifiedText.setData(ghm.toString(Constants.HEXBASE));
        resultText.setText(value.toString(Constants.HEXBASE));
    }

    /**
     * Sets up the table for signing
     */
    private void updateSign() {
        final StringBuilder sb = new StringBuilder();
        final BigInteger modulus = data.getModulus();
        BigInteger value;
        int offset0;
        int offset1;
        value = new BigInteger(data.getAction().run(data, numbers[numberIndex]), Constants.HEXBASE);
        sb.append("k = "); //$NON-NLS-1$
        sb.append(data.getK().toString(Constants.HEXBASE));
        sb.append("\n"); //$NON-NLS-1$
        offset0 = sb.length();
        sb.append("r = gk = "); //$NON-NLS-1$
        sb.append(data.getGenerator().toString(Constants.HEXBASE));
        sb.append(data.getK().toString(Constants.HEXBASE));
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus.toString(Constants.HEXBASE));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(data.getR().toString(Constants.HEXBASE));
        sb.append("\n"); //$NON-NLS-1$
        offset1 = sb.length();
        sb.append("s = (H(m) - ar)k-1 = ("); //$NON-NLS-1$
        sb.append(numbers[numberIndex]);
        sb.append(" - "); //$NON-NLS-1$
        sb.append(data.getA().toString(Constants.HEXBASE));
        sb.append(" ∙ "); //$NON-NLS-1$
        sb.append(data.getR().toString(Constants.HEXBASE));
        sb.append(") ∙ "); //$NON-NLS-1$
        sb.append(data.getK().modInverse(modulus.subtract(BigInteger.ONE)).toString(Constants.HEXBASE));
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus.subtract(BigInteger.ONE).toString(Constants.HEXBASE));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(value.toString(Constants.HEXBASE));
        // style it
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
        offset0 = offset0 + 9 + data.getGenerator().toString(Constants.HEXBASE).length();
        fastExpText.setStyle(superScript, offset0, offset0
                + data.getK().toString(Constants.HEXBASE).length() - 1);
        fastExpText.setStyle(superScript, offset1 + 16, offset1 + 17);
        // set result
        stepResult
                .setText("r = " + data.getR().toString(Constants.HEXBASE) + ", s = " + value.toString(Constants.HEXBASE)); //$NON-NLS-1$ //$NON-NLS-2$
        resultText
                .setText("(" + data.getR().toString(Constants.HEXBASE) + ", " + value.toString(Constants.HEXBASE) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * Sets up the table for signing
     */
    private void updateDecrypt() {
        final StringBuilder sb = new StringBuilder();
        final BigInteger modulus = data.getModulus();
        BigInteger value;
        int offset0;
        int offset1;
        int offset2;
        BigInteger x;
        sb.append("x = p - a - 1 = "); //$NON-NLS-1$
        sb.append(modulus.toString(Constants.HEXBASE));
        sb.append(" - "); //$NON-NLS-1$
        sb.append(data.getA().toString(Constants.HEXBASE));
        sb.append(" - 1 = "); //$NON-NLS-1$
        sb.append((x = modulus.subtract(data.getA()).subtract(BigInteger.ONE)).toString(Constants.HEXBASE));
        sb.append("\n"); //$NON-NLS-1$
        offset0 = sb.length();
        sb.append("B = gb mod p = "); //$NON-NLS-1$
        sb.append(data.getGenerator().toString(Constants.HEXBASE));
        sb.append(data.getB().toString(Constants.HEXBASE));
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus.toString(Constants.HEXBASE));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(data.getGPowB().toString(Constants.HEXBASE));
        sb.append("\n"); //$NON-NLS-1$
        offset1 = sb.length();
        sb.append("m = Bxc = "); //$NON-NLS-1$
        sb.append(data.getGPowB().toString(Constants.HEXBASE));
        offset2 = sb.length();
        sb.append(x.toString(Constants.HEXBASE));
        sb.append(" ∙ "); //$NON-NLS-1$
        sb.append(numbers[numberIndex]);
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus.toString(Constants.HEXBASE));
        sb.append(" = "); //$NON-NLS-1$
        value = new BigInteger(
                "" + (int) data.getAction().run(data, numbers[numberIndex]).charAt(0)); //$NON-NLS-1$
        sb.append(value.toString(Constants.HEXBASE));
        // set style
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
        offset0 = offset0 + 15 + data.getGenerator().toString(Constants.HEXBASE).length();
        fastExpText.setStyle(superScript, offset0, offset0
                + data.getB().toString(Constants.HEXBASE).length() - 1);
        fastExpText.setStyle(superScript, offset1 + 5, offset1 + 5);
        fastExpText.setStyle(superScript, offset2, offset2 + x.toString(Constants.HEXBASE).length() - 1);
        // set result
        stepResult.setText("m = " + (char) value.intValue()); //$NON-NLS-1$
        resultText.setText(resultText.getText() + (char) value.intValue());
    }

    /**
     * Sets up the table for signing
     */
    private void updateEncrypt() {
        final StringBuilder sb = new StringBuilder();
        final BigInteger modulus = data.getModulus();
        BigInteger value;
        int offset0;
        int offset1;
        int offset2;
        sb.append("b = "); //$NON-NLS-1$
        sb.append(data.getB().toString());
        sb.append("\n"); //$NON-NLS-1$
        offset0 = sb.length();
        sb.append("B = gb mod p = "); //$NON-NLS-1$
        sb.append(data.getGenerator().toString(Constants.HEXBASE));
        sb.append(data.getB().toString(Constants.HEXBASE));
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus.toString(Constants.HEXBASE));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(data.getGPowB().toString(Constants.HEXBASE));
        sb.append("\n"); //$NON-NLS-1$
        // TODO bis hier
        offset1 = sb.length();
        sb.append("c = Abm mod p = "); //$NON-NLS-1$
        sb.append(data.getPublicA().toString(Constants.HEXBASE));
        offset2 = sb.length();
        sb.append(data.getB().toString(Constants.HEXBASE));
        sb.append(" ∙ "); //$NON-NLS-1$
        sb.append(numbers[numberIndex]);
        sb.append(" mod "); //$NON-NLS-1$
        sb.append(modulus);
        sb.append(" = "); //$NON-NLS-1$
        value = new BigInteger(data.getAction().run(data, numbers[numberIndex]).trim(),
                Constants.HEXBASE);
        sb.append(value.toString(Constants.HEXBASE));
        int tmp;
        // set style
        fastExpText.setText(sb.toString());
        fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
        fastExpText.setStyle(superScript,
                tmp = offset0 + 15 + data.getGenerator().toString(Constants.HEXBASE).length(), tmp
                        + data.getB().toString(Constants.HEXBASE).length());
        fastExpText.setStyle(superScript, offset1 + 5, offset1 + 5);
        fastExpText.setStyle(superScript, offset2, offset2
                + data.getB().toString(Constants.HEXBASE).length() - 1);
        // set to stepresult
        stepResult.setText("c = " + value.toString(Constants.HEXBASE)); //$NON-NLS-1$
        if (resultText.getText().equals("")) { //$NON-NLS-1$
        	resultText.setText(value.toString(Constants.HEXBASE));
        } else {
        	resultText.setText(resultText.getText() + " " + value.toString(Constants.HEXBASE)); //$NON-NLS-1$
        }
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
        resultText = new Text(groupResult, SWT.V_SCROLL | SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.WRAP);
        resultText.setText("\n\n"); //$NON-NLS-1$
        resultText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        resultText.addModifyListener(new ModifyListener() {

        	@Override
        	public void modifyText(ModifyEvent e) {
                copyButton.setEnabled(true);
                if (data.getAction() == Action.VerifyAction
                        && !textText.getText().equals("")) { //$NON-NLS-1$
                    String text;
                    if (verifiedText.getData() != null
                            && resultText.getText().trim()
                                    .equals(verifiedText.getData())) {
                        text = Messages.ElGamalComposite_valid;
                        verifiedText.setForeground(ColorService.GREEN);
                    } else {
                        text = Messages.ElGamalComposite_invalid;
                        verifiedText.setForeground(ColorService.RED);
                    }
                    verifiedText.setText(text);
                }
            }
        });

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
                cb.setContents(new Object[] {resultText.getText()},
                        new Transfer[] {TextTransfer.getInstance()});
            }
        });
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
				WizardDialog keyButtonDialog = new WizardDialog(new Shell(Display.getDefault()),
						new KeySelectionWizard(null, null, true));
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
                	// If no data will be lot continue automatically.
                    if (proceed) {
                        final ElGamalData oldData = datas.get(newAction);
                        reset();
                        data.inherit(oldData);
                        // if we got any data at all insert the key parameters to
                        // their fields
                        if (data.getModulus() != null) {
                            keySelected();
                            // if we got plaintext/ciphertext/signature, set
                            // them up as well
                            if (data.getPlainText().length() != 0 || data.getCipherText().length() != 0) {
                                textEntered();
                                if (data.getB() != null) {
                                    bEntered();
                                    uniqueKeyButton.setEnabled(false);
                                }
                            }
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
                reset();
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
        textText.setText(""); //$NON-NLS-1$
        numberText.setText(""); //$NON-NLS-1$
        fastExpTable.setVisible(false);
        stepResult.setText(""); //$NON-NLS-1$
        stepButton.setEnabled(false);
        
        firstRun = true;
        numberIndex = 0;

        stepButton.setText(Messages.ElGamalComposite_start);
        stepButton.setToolTipText(Messages.ElGamalComposite_start_calc);
        stepButton.pack();
        stepText.setText(""); //$NON-NLS-1$
        resultText.setText(""); //$NON-NLS-1$
        copyButton.setEnabled(false);
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
            case SignAction:
                textText.setText(data.getPlainText());
                break;
            case DecryptAction:
                numberText.setText(data.getCipherText());
                break;
            case VerifyAction:
                textText.setText(data.getPlainText());
                numberText.setText(data.getSignature().split(",")[1].replace(')', ' ').trim()); //$NON-NLS-1$
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
                data.setCipherText(resultText.getText());
                break;
            case DecryptAction:
                data.setPlainText(resultText.getText());
                break;
            case SignAction:
                data.setSignature(resultText.getText());
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
