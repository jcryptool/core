/**
 *
 */
package org.jcryptool.visual.dsa.ui;

import static org.jcryptool.visual.library.Constants.BIGBUTTONHEIGHT;
import static org.jcryptool.visual.library.Constants.BIGBUTTONVERTICALSPACE;
import static org.jcryptool.visual.library.Constants.BIGBUTTONWIDTH;
import static org.jcryptool.visual.library.Constants.GREEN;
import static org.jcryptool.visual.library.Constants.HORIZONTAL_SPACING;
import static org.jcryptool.visual.library.Constants.MARGIN_WIDTH;
import static org.jcryptool.visual.library.Constants.RED;
import static org.jcryptool.visual.library.Constants.WHITE;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.dsa.Action;
import org.jcryptool.visual.dsa.DSAData;
import org.jcryptool.visual.dsa.ui.wizards.KeySelectionWizard;
import org.jcryptool.visual.dsa.ui.wizards.TextEntryWizard;
import org.jcryptool.visual.dsa.ui.wizards.UniqueKeyWizard;
import org.jcryptool.visual.library.Constants;

/**
 * composite, display of everything this visual shows, that is not contained within wizards.
 * @author Michael Gaber
 */
public class DSAComposite extends Composite {

	/** the chosen action. */
	private Action action;

	/** whether dialogs are wanted TODO: default to true. */
	private boolean dialog = false;
    private Font smallFont;
    private Font verySmallFont;

	/** group containing the action choice buttons. */
	private Group actionChoiceGroup;

	/** buttons for running the wizards and finishing up. */
	private Button keysel, textEnter, runCalc;

	/** shared data object. */
	private DSAData data = new DSAData();

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
	private int numberIndex;

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

	private Text pText;

	private Text qText;

	private Text gText;

	private Text yText;

	private Text xText;

	private Text stepResult;

	private Button startButton;

	private Button stepButton;

	private Composite fastExpTable;

	private final TextLayout fastExpText = new TextLayout(getDisplay());

	private Button uniqueKeyButton;

	/**
	 * constructor calls super and saves a reference to the view.
	 * @param parent the parent composite
	 * @param style style of the Widget to construct
	 * @see Composite#Composite(Composite, int)
	 */
	public DSAComposite(final Composite parent, final int style) {
		super(parent, style);
		initialize();
	}

	/**
	 * initializes the startup situation of this view.
	 */
	private void initialize() {
		// basic layout is a
		setLayout(new GridLayout());
		createHead();
		createActionChoice();
		createMainArea();
		createOptionsArea();
	}

	/**
	 * creates the description head of the window to display informations about the Algorithm itself.
	 */
	private void createHead() {
		final Composite head = new Composite(this, SWT.NONE);
		head.setBackground(WHITE);
		head.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		head.setLayout(new GridLayout());

		final Label label = new Label(head, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setBackground(WHITE);
		label.setText("DSA Berechnungen");

		final StyledText stDescription = new StyledText(head, SWT.READ_ONLY);
		stDescription.setText("Der DSA-Algorithmus ist ein asymmetrisches Signaturverfahren.\nEr baut auf dem Problem des diskreten Logarithmus in zyklischen Gruppen auf.");
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * creates the main area where everything except head and options is contained.
	 */
	private void createMainArea() {
		final Group g = new Group(this, SWT.NONE);
		g.setText("Algorithmus");
		final GridLayout gl = new GridLayout(2, false);
		gl.marginWidth = MARGIN_WIDTH;
		gl.horizontalSpacing = HORIZONTAL_SPACING;
		g.setLayout(gl);
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		createButtonArea(g);
		createAlgoArea(g);
	}

	/**
	 * create the group for the action choice buttons.
	 */
	private void createActionChoice() {
		// add a new group for the action choice buttons
		actionChoiceGroup = new Group(this, SWT.NONE);
		actionChoiceGroup.setText("Aktion");
		actionChoiceGroup.setLayout(new GridLayout(2, true));
		actionChoiceGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		// Set up a selection listener for the Buttons
		final SelectionListener listener = new SelectionListener() {
			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}

			public void widgetSelected(final SelectionEvent e) {
				action = (Action) ((Button) e.widget).getData();
				data.setAction(action);
				keysel.setEnabled(true);
			}
		};

		// create Buttons
		Button b1 = new Button(actionChoiceGroup, SWT.RADIO);
		b1.setText("Signieren");
		b1.addSelectionListener(listener);
		b1.setData(Action.SignAction);
		b1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		Button b2 = new Button(actionChoiceGroup, SWT.RADIO);
		b2.setText("Verifizieren");
		b2.addSelectionListener(listener);
		b2.setData(Action.VerifyAction);
		b2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
	}

	/**
	 * create the vertical area for the three main buttons.
	 * @param parent the parent composite
	 */
	private void createButtonArea(final Composite parent) {
		// set up the canvas for the Buttons
		final Canvas canvas = new Canvas(parent, SWT.NONE);
		canvas.setLayout(new FormLayout());
		canvas.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, true));

		// Form data to place Key selection Button
		final FormData fDkeysel = new FormData(BIGBUTTONWIDTH, BIGBUTTONHEIGHT);
		fDkeysel.left = new FormAttachment(4);
		fDkeysel.top = new FormAttachment(4);

		// Key selection Button
		keysel = new Button(canvas, SWT.PUSH);
		keysel.setBackground(RED);
		keysel.setEnabled(false);
		keysel.setText("Schlüsselwahl");
		keysel.setLayoutData(fDkeysel);
		keysel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(final SelectionEvent e) {
				// wont be called
			}

			public void widgetSelected(final SelectionEvent e) {
				if (dialog) {
					final MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
							| SWT.OK);
					messageBox.setText("Schlüsselwahl");
					messageBox.setMessage("Bitte wählen Sie nun den Schlüssel oder geben Sie einen neuen ein.");
					messageBox.open();
				}
				if (new WizardDialog(getShell(), new KeySelectionWizard(action, data, false)).open() == Window.OK) {
					keysel.setBackground(GREEN);
					textEnter.setEnabled(true);
					for (final Control c : actionChoiceGroup.getChildren()) {
						((Button) c).setEnabled(false);
					}
					if (data.getP() != null) {
						pText.setText(data.getP().toString());
					}
					if (data.getQ() != null) {
						pText.setText(data.getQ().toString());
					}
					if (data.getGenerator() != null) {
						gText.setText(data.getGenerator().toString());
					}
					if (data.getX() != null) {
						yText.setText(data.getX().toString());
					}
					if (data.getY() != null) {
						xText.setText(data.getY().toString());
					}
				}
			}

		});

		// Form data to place Text enter button
		final FormData fDtextEnter = new FormData(BIGBUTTONWIDTH, BIGBUTTONHEIGHT);
		fDtextEnter.left = new FormAttachment(4);
		fDtextEnter.top = new FormAttachment(keysel, BIGBUTTONVERTICALSPACE, SWT.BOTTOM);

		// Text enter Button
		textEnter = new Button(canvas, SWT.PUSH);
		textEnter.setBackground(RED);
		textEnter.setEnabled(false);
		textEnter.setText("Text eingeben");
		textEnter.setLayoutData(fDtextEnter);
		textEnter.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}

			public void widgetSelected(final SelectionEvent e) {
				if (dialog) {
					final MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
							| SWT.OK);
					messageBox.setText("Texteingabe");
					messageBox.setMessage("Bitte geben Sie nun den zu bearbeitenden Text ein.");
					messageBox.open();
				}
				if (new WizardDialog(getShell(), new TextEntryWizard(action, data)).open() == Window.OK) {
					keysel.setEnabled(false);
					textEnter.setBackground(GREEN);
					uniqueKeyButton.setEnabled(true);
					switch (action) {
					case SignAction:
						textText.setText(data.getPlainText());
						break;
					case VerifyAction:
						textText.setText(data.getPlainText());
						numberText.setText(data.getSignature().split(",")[1].replace(')', ' ').trim());
						uniqueKeyButton.setEnabled(false);
						uniqueKeyButton.setBackground(GREEN);
						runCalc.setEnabled(true);
						startButton.setEnabled(true);
						break;
					default:
						break;
					}
				}
			}

		});

		// form data to place unique parameter button
		final FormData fDkey = new FormData(BIGBUTTONWIDTH, BIGBUTTONHEIGHT);
		fDkey.left = new FormAttachment(4);
		fDkey.top = new FormAttachment(textEnter, BIGBUTTONVERTICALSPACE, SWT.BOTTOM);

		// unique parameter button
		uniqueKeyButton = new Button(canvas, SWT.PUSH);
		uniqueKeyButton.setBackground(RED);
		uniqueKeyButton.setEnabled(false);
		uniqueKeyButton.setText("Parameter wählen");
		uniqueKeyButton
				.setToolTipText("Klicken Sie hier um Ihren für diesen Vorgang einzigartigen Zusatzparameter zu wählen.");
		uniqueKeyButton.setLayoutData(fDkey);
		uniqueKeyButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				if (new WizardDialog(getShell(), new UniqueKeyWizard(data)).open() == Window.OK) {
					textEnter.setEnabled(false);
					uniqueKeyButton.setBackground(GREEN);
					runCalc.setEnabled(true);
					startButton.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}
		});

		// Form Data to place Calculate Button
		final FormData fDcalc = new FormData(BIGBUTTONWIDTH, BIGBUTTONHEIGHT);
		fDcalc.left = new FormAttachment(4);
		fDcalc.top = new FormAttachment(uniqueKeyButton, BIGBUTTONVERTICALSPACE, SWT.BOTTOM);

		// Run Calculations Button
		runCalc = new Button(canvas, SWT.PUSH);
		runCalc.setBackground(RED);
		runCalc.setEnabled(false);
		runCalc.setText("Berechnen");
		runCalc.setToolTipText("Klicken Sie hier um die Berechnungen abzuschließen ohne weitere Schritte anzuzeigen.");
		runCalc.setLayoutData(fDcalc);
		runCalc.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				uniqueKeyButton.setEnabled(false);
				textEnter.setEnabled(false);
				runCalc.setEnabled(false);
				runCalc.setBackground(GREEN);
				startButton.setEnabled(false);
				stepButton.setEnabled(false);
				if (dialog) {
					final MessageBox message = new MessageBox(new Shell(Display.getCurrent()), SWT.ICON_INFORMATION
							| SWT.OK);
					message.setText("Berechnungsabschluss");
					message
							.setMessage("Für alle im Zahlfeld angegebenen Hex-Werte wird nun der Berechnungsschritt durchgeführt. Das Ergebnis wird anschließen im Ergebnisfeld angezeigt.");
					message.open();
				}
				if (action == Action.SignAction) {
					final String value = action.run(data, numberText.getText().trim().split(" "));
					resultText.setText("(" + data.getR().toString(Constants.HEXBASE) + ", " + value + ")");
				} else {
					resultText.setText(action.run(data, numberText.getText().split(" ")));
				}
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}
		});
	}

	/**
	 * create the main algorithm view.
	 * @param parent the parent
	 */
	private void createAlgoArea(final Composite parent) {
		final Composite g = new Composite(parent, SWT.SHADOW_NONE);
		g.setLayout(new GridLayout());
		GridData gd_g = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_g.heightHint = 205;
		gd_g.widthHint = 84;
		g.setLayoutData(gd_g);
		createKeyGroup(g);
		createTextGroup(g);
		createCalcGroup(g);
		createResultGroup(g);
	}

	/**
	 * create the keygroup there e, d and n are displayed.
	 * @param parent the parent
	 */
	private void createKeyGroup(final Composite parent) {
		// set up Group
	    final GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
	    gd.widthHint = 64;
		final GridLayout gl = new GridLayout(11, true);
		final Group g = new Group(parent, SWT.SHADOW_NONE);
		g.setText("Schlüssel");
		g.setLayout(gl);
		g.setLayoutData(gd);
		Label l = new Label(g, SWT.NONE);
		l.setText("p");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		pText = new Text(g, SWT.READ_ONLY | SWT.BORDER);
		pText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		l = new Label(g, SWT.NONE);
		l.setText("q");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		qText = new Text(g, SWT.READ_ONLY | SWT.BORDER);
		qText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		l = new Label(g, SWT.NONE);
		l.setText("g");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		gText = new Text(g, SWT.READ_ONLY | SWT.BORDER);
		gText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		l = new Label(g, SWT.NONE);
		l.setText("y");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		yText = new Text(g, SWT.READ_ONLY | SWT.BORDER);
		yText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// Spacer
		l = new Label(g, SWT.NONE);
		l.setText("x");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		xText = new Text(g, SWT.READ_ONLY | SWT.BORDER);
		xText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// Spacer
		l = new Label(g, SWT.NONE);
		l.setText("    ");
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	}

	/**
	 * create the group where text and "translated" text are displayed.
	 * @param parent the parent
	 */
	private void createTextGroup(final Composite parent) {
		final Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout());
		GridData gd_g = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_g.heightHint = 165;
		gd_g.widthHint = 139;
		g.setLayoutData(gd_g);
		new Label(g, SWT.NONE).setText("Text");
		textText = new Text(g, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
		textText.setText("\n\n\n");
		textText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3));
		textText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent e) {
				if (textText.getText().equals("")) {
					return;
				}
				if (action == Action.SignAction) {
					numberText.setText(hash(textText.getText()));
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
		new Label(g, SWT.NONE).setText("Text als Zahlen");
		numberText = new Text(g, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
		numberText.setText("\n\n\n");
		numberText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3));
	}

	/**
	 * has function. Depends on value of {@link RSAData#getSimpleHash()}. <br />
	 * If simple Hash is used it's my own private hash function, which translates every character to it's unicode value.
	 * and adds them up mod N. <br />
	 * Else it's a SHA-1 Hash mod N
	 * @param text the text to hash
	 * @return the hash-value
	 */
	private String hash(final String text) {
		BigInteger rv = BigInteger.ZERO;
		if (data.getSimpleHash()) {
			final char[] chars = text.toCharArray();
			for (final char c : chars) {
				rv = rv.add(new BigInteger("" + (int) c));
			}
		} else {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("SHA-1");
				rv = new BigInteger(md.digest(text.getBytes(IConstants.UTF8_ENCODING)));
			} catch (final NoSuchAlgorithmException e) {
			    LogUtil.logError(e);
			} catch (final UnsupportedEncodingException e) {
			    LogUtil.logError(e);
			}
		}
		return rv.mod(data.getQ()).toString(Constants.HEXBASE);
	}

	/**
	 * create the calculations group where the fast exponentiation table and the step result are displayed.
	 * @param parent the parent
	 */
	private void createCalcGroup(final Composite parent) {
		final Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout(2, false));
		g.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		g.setText("Berechnungen");

		startButton = new Button(g, SWT.PUSH);
		startButton.setText("Start");
		startButton.setEnabled(false);
		startButton.setToolTipText("Berechnungen starten");
		startButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		startButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				uniqueKeyButton.setEnabled(false);
				numbers = numberText.getText().split(" ");
				numberIndex = 0;
				stepButton.setEnabled(numberIndex != numbers.length - 1);
				if (numberIndex == numbers.length - 1) {
					runCalc.setEnabled(false);
					runCalc.setBackground(GREEN);
				}
				startButton.setEnabled(false);
				textEnter.setEnabled(false);
				initTable();
				updateTable();
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called

			}
		});

		stepButton = new Button(g, SWT.PUSH);
		stepButton.setText("Schritt");
		stepButton.setEnabled(false);
		stepButton.setToolTipText("Nächsten Berechnungsschritt anzeigen");
		stepButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		stepButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				if (++numberIndex == numbers.length - 1) {
					stepButton.setEnabled(false);
					runCalc.setEnabled(false);
					runCalc.setBackground(GREEN);
				}
				updateTable();
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}
		});

		// set up a composite to draw final the fast exp shit on
		fastExpTable = new Composite(g, SWT.NONE);
		fastExpTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		fastExpTable.setBackground(WHITE);
		fastExpTable.setVisible(false);

		final Label l = new Label(g, SWT.NONE);
		l.setText("Schrittergebnis");
		stepResult = new Text(g, SWT.BORDER | SWT.READ_ONLY);
		stepResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	/**
	 * initializes the fast exponentiation table.
	 */
	private void initTable() {
		// get the graphics context
		final GC gc = new GC(fastExpTable);
		// get the associated fontData
		final FontData normalData = getDisplay().getSystemFont().getFontData()[0];
		// set the new font height to 12
		normalData.setHeight(12);
		// create small and very small data from the normal data and create
		// matching fonts with each 2pt final less height
		final FontData smallData = new FontData(normalData.getName(), normalData.getHeight() - 2, normalData.getStyle());
		smallFont = new Font(getDisplay(), smallData);
		final FontData verySmallData = new FontData(smallData.getName(), smallData.getHeight() - 2, smallData
				.getStyle());
		verySmallFont = new Font(getDisplay(), verySmallData);
		// some metrics, whatever they are
		final FontMetrics metrics = gc.getFontMetrics();
		// something to calculate the actual place of the superscripts
		final int baseline = metrics.getAscent() + metrics.getLeading();
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
		fastExpTable.addListener(SWT.Paint, new Listener() {

			public void handleEvent(final Event event) {
				fastExpText.draw(event.gc, 0, 0);
			}
		});
	}

	/**
	 * updates the fast exponentiation table i.e. displays the next step
	 */
	private void updateTable() {
		// reset resultText if it contains \n (only first run)
		if (resultText.getText().contains("\n")) {
			resultText.setText("");
		}
		BigInteger value;
		final StringBuilder sb = new StringBuilder();
		int offset0, offset1 = 0;
		final int offset2;
		final BigInteger modulus = data.getP();
		switch (action) {
		case SignAction:
			value = new BigInteger(action.run(data, numbers[numberIndex]), Constants.HEXBASE);
			sb.append("k = ");
			sb.append(data.getK().toString(Constants.HEXBASE));
			sb.append("\n");
			offset0 = sb.length();
			sb.append("r = gk = (");
			sb.append(data.getGenerator().toString(Constants.HEXBASE));
			sb.append(data.getK().toString(Constants.HEXBASE));
			sb.append(" mod ");
			sb.append(modulus.toString(Constants.HEXBASE));
			sb.append(") mod ");
			sb.append(data.getQ().toString(Constants.HEXBASE));
			sb.append(" = ");
			sb.append(data.getR().toString(Constants.HEXBASE));
			sb.append("\n");
			offset1 = sb.length();
			sb.append("s = (H(m) - xr)k-1 = (");
			sb.append(numbers[numberIndex]);
			sb.append(" - ");
			sb.append(data.getX().toString(Constants.HEXBASE));
			sb.append(" ∙ ");
			sb.append(data.getR().toString(Constants.HEXBASE));
			sb.append(") ∙ ");
			sb.append(data.getK().modInverse(data.getQ()).toString(Constants.HEXBASE));
			sb.append(" mod ");
			sb.append(data.getQ().toString(Constants.HEXBASE));
			sb.append(" = ");
			sb.append(value.toString(Constants.HEXBASE));
			// style it
			fastExpText.setText(sb.toString());
			fastExpText.setStyle(superScript, offset0 + 5, offset0 + 5);
			offset0 = offset0 + 10 + data.getGenerator().toString(Constants.HEXBASE).length();
			fastExpText.setStyle(superScript, offset0, data.getK().toString(Constants.HEXBASE).length() - 1);
			fastExpText.setStyle(superScript, offset1 + 16, offset1 + 17);
			// set result
			stepResult.setText("r = " + data.getR().toString(Constants.HEXBASE) + ", s = " + value.toString(Constants.HEXBASE));
			resultText.setText("(" + data.getR().toString(Constants.HEXBASE) + ", " + value.toString(Constants.HEXBASE) + ")");
			break;
		case VerifyAction:
			final BigInteger hash = new BigInteger(hash(data.getPlainText()), Constants.HEXBASE);
			final BigInteger r = data.getR();
			final BigInteger s = new BigInteger(data.getSignature().split(",")[1].replace(')', ' ').trim(), Constants.HEXBASE);
			final BigInteger p = data.getP();
			final BigInteger q = data.getQ();
			final BigInteger w = s.modInverse(q);
			final BigInteger u1 = hash.multiply(w).mod(q);
			final BigInteger u2 = r.multiply(w).mod(q);
			value = data.getGenerator().modPow(u1, p).multiply(data.getY().modPow(u2, p)).mod(p).mod(q);

			sb.append("r = ");
			sb.append(r.toString(Constants.HEXBASE));
			sb.append("\ns = ");
			sb.append(s.toString(Constants.HEXBASE));
			sb.append("\nw = s");
			offset0 = sb.length();
			sb.append("-1 mod ");
			sb.append(data.getQ());
			sb.append(" = ");
			sb.append(w.toString(Constants.HEXBASE));
			sb.append("\nu1 = H(m) ∙ w = ");
			sb.append(hash.toString(Constants.HEXBASE));
			sb.append(" ∙ ");
			sb.append(w.toString(Constants.HEXBASE));
			sb.append(" mod ");
			sb.append(q.toString(Constants.HEXBASE));
			sb.append(" = ");
			sb.append(u1.toString(Constants.HEXBASE));
			sb.append("\nu2 = r ∙ w = ");
			sb.append(r.toString(Constants.HEXBASE));
			sb.append(" ∙ ");
			sb.append(w.toString(Constants.HEXBASE));
			sb.append(" mod ");
			sb.append(q.toString(Constants.HEXBASE));
			sb.append(" = ");
			sb.append(u2.toString(Constants.HEXBASE));
			sb.append("\nv = g");
			offset1 = sb.length();
			sb.append("u1 ∙ yu2 = ( ");
			sb.append(data.getGenerator().toString(Constants.HEXBASE));
			offset2 = sb.length();
			sb.append(u1.toString(Constants.HEXBASE));
			sb.append(" ∙ ");
			sb.append(data.getY().toString(Constants.HEXBASE));
			final int offset3 = sb.length();
			sb.append(u2.toString(Constants.HEXBASE));
			sb.append(" mod ");
			sb.append(p.toString(Constants.HEXBASE));
			sb.append(" ) mod ");
			sb.append(" mod ");
			sb.append(q.toString(Constants.HEXBASE));
			sb.append(" = ");
			sb.append(value.toString(Constants.HEXBASE));

			// set style
			fastExpText.setText(sb.toString());
			fastExpText.setStyle(superScript, offset0, offset0 + 1);
			fastExpText.setStyle(superScript, offset1, offset1 + 1);
			fastExpText.setStyle(superScript, offset1 + 6, offset1 + 7);
			fastExpText.setStyle(superScript, offset2, offset2 + u1.toString(Constants.HEXBASE).length() - 1);
			fastExpText.setStyle(superScript, offset3, offset3 + u2.toString(Constants.HEXBASE).length() - 1);

			// set results
			stepResult.setText("g^u1*y^u2 = " + value.toString(Constants.HEXBASE));
			verifiedText.setData(r.toString(Constants.HEXBASE));
			resultText.setText(value.toString(Constants.HEXBASE));
			break;
		}
		fastExpTable.redraw();
	}

	/**
	 * create the resultgroup where the result and the copy button are displayed.
	 * @param parent the parent
	 */
	private void createResultGroup(final Composite parent) {
		final Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		group.setLayout(new GridLayout(3, false));
		group.setText("Ergebnis");
		resultText = new Text(group, SWT.V_SCROLL | SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.WRAP);
		resultText.setText("\n\n");
		resultText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		resultText.addModifyListener(new ModifyListener() {

			public void modifyText(final ModifyEvent e) {
				copyButton.setEnabled(true);
				if (action == Action.VerifyAction && !textText.getText().equals("")) {
					String text;
					if (verifiedText.getData() != null && resultText.getText().trim().equals(verifiedText.getData())) {
						text = "gültig";
						verifiedText.setForeground(GREEN);
					} else {
						text = "ungültig";
						verifiedText.setForeground(RED);
					}
					verifiedText.setText(text);
				}
			}
		});

		verifiedText = new StyledText(group, SWT.READ_ONLY);
		verifiedText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		verifiedText.setText("                ");

		copyButton = new Button(group, SWT.PUSH);
		copyButton.setEnabled(false);
		copyButton.setText("Kopieren");
		copyButton.setToolTipText("Text in die Zwischenablage kopieren");
		copyButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		copyButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				final Clipboard cb = new Clipboard(Display.getCurrent());
				cb.setContents(new Object[] { resultText.getText() }, new Transfer[] { TextTransfer.getInstance() });
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
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
		optionsGroup.setLayout(new GridLayout(8, false));
		optionsGroup.setText("Optionen");

		// temporary spacer
		final Button keyButton = new Button(optionsGroup, SWT.PUSH);
		keyButton.setText("Schlüsselerzeugung");
		keyButton.setToolTipText("Klicken Sie hier um den Schlüsselerzeugungs-Assistenten zu starten");
		keyButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
		keyButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(final SelectionEvent e) {
				new WizardDialog(new Shell(Display.getDefault()), new KeySelectionWizard(null, null, true)).open();
			}

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}
		});

		// initialize dialog checkbox
		final Button dialogButton = new Button(optionsGroup, SWT.CHECK);
		dialogButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		dialogButton.setText("Dialoge anzeigen");
		dialogButton.setSelection(dialog);
		dialogButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}

			public void widgetSelected(final SelectionEvent e) {
				dialog = ((Button) e.widget).getSelection();
			}

		});

		// initialize reset button
		final Button reset = new Button(optionsGroup, SWT.PUSH);
		reset.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		reset.setText("Zurücksetzen");
		new Label(optionsGroup, SWT.NONE);
		new Label(optionsGroup, SWT.NONE);
		new Label(optionsGroup, SWT.NONE);
		new Label(optionsGroup, SWT.NONE);
		new Label(optionsGroup, SWT.NONE);
		reset.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(final SelectionEvent e) {
				// won't be called
			}

			public void widgetSelected(final SelectionEvent e) {
				for (final Control c : actionChoiceGroup.getChildren()) {
					((Button) c).setEnabled(true);
					((Button) c).setSelection(false);
				}
				keysel.setEnabled(false);
				keysel.setBackground(RED);
				textEnter.setEnabled(false);
				textEnter.setBackground(RED);
				uniqueKeyButton.setEnabled(false);
				uniqueKeyButton.setBackground(RED);
				runCalc.setEnabled(false);
				runCalc.setBackground(RED);
				data = new DSAData();
				pText.setText("");
				gText.setText("");
				yText.setText("");
				xText.setText("");
				textText.setText("");
				numberText.setText("");
				// fastExpTable.removeAll();
				// for (final TableColumn column : fastExpTable.getColumns()) {
				// column.dispose();
				// }
				fastExpTable.setVisible(false);
				stepResult.setText("");
				stepButton.setEnabled(false);
				resultText.setText("");
				copyButton.setEnabled(false);
				verifiedText.setText("");
				// styl0r.setText("");
				// stylor.setText("");
				// styledFastExtText.redraw();
				// styledFastExpMulText.redraw();
			}
		});
	}

	@Override
	public void dispose() {
	    smallFont.dispose();
	    verySmallFont.dispose();

	    super.dispose();
	}
}
