// -----BEGIN DISCLAIMER-----
package org.jcryptool.analysis.viterbi.views;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.viterbi.algorithm.BitwiseXOR;
import org.jcryptool.analysis.viterbi.algorithm.Combination;
import org.jcryptool.analysis.viterbi.algorithm.IO;
import org.jcryptool.analysis.viterbi.algorithm.ModularAddition;
import org.jcryptool.analysis.viterbi.views.XORComposite.XORCombinationBackgroundJob;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.crypto.ui.background.BackgroundJob;

/**
 *
 * This class creates the content of the xor tab. The basic layout is a grid
 * layout. In the grid layout, the GUI objects are connected with canvas.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class XORComposite extends Composite {
	/* set default values */

	public abstract class XORCombinationBackgroundJob extends BackgroundJob {
		
		public String __result;

	}

	private static final int LOADBUTTONHEIGHT = 30;
	private static final int LOADBUTTONWIDTH = 120;

	private Text plain1;
	private Text plain2;
	private Text cipher;
	private ViterbiView viterbiView;
	private Button hex;
	private Button text;
	private String cipherString = ""; //$NON-NLS-1$
	private Button xor;
	private Button mod;

	/* default value for the combination is xor */
	private Combination combi = new BitwiseXOR();
	private Composite g;

	/**
	 * @param the
	 *            parent class
	 * @param style
	 * @param reference
	 *            of the viterbiView
	 */
	public XORComposite(final Composite parent, final int style, ViterbiView viterbiView) {
		super(parent, style);

		this.viterbiView = viterbiView;
		
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		setLayout(gl);
		
		createHead();
		createMainArea();

		plain1.addModifyListener(e -> subjectChanged());
		plain2.addModifyListener(e -> subjectChanged());
		
		subjectChanged();
	}

	private void subjectChanged() {
		String t1 = plain1.getText();
		String t2 = plain2.getText();
		String encrypted = cipherString;
		Boolean canEncrypt = t1.trim().length() > 0 && t2.trim().length() > 0; 
		Boolean canProceed = encrypted.trim().length() > 0; 
		
		BiConsumer<Boolean, Boolean> fDoUI = (Boolean enc, Boolean proc) -> {
			calculate.setEnabled(enc);
			exportButton.setEnabled(proc);
			next.setEnabled(proc);
		};
		
		fDoUI.accept(canEncrypt, canProceed);
	}


	/**
	 * This method generates the head of the tab. The head has a title and a
	 * description.
	 */
	private void createHead() {
		TitleAndDescriptionComposite titleAndDescription = new TitleAndDescriptionComposite(this);
		titleAndDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		titleAndDescription.setTitle(Messages.XORComposite_tab_title);
		titleAndDescription.setDescription(Messages.XORComposite_description);
	}

	/**
	 * This method creates the main area. It calls most of the other methods in
	 * this class. The basic layout is a grid layout.
	 */
	private void createMainArea() {
		g = new Composite(this, SWT.NONE);
		// g.setText(Messages.XORComposite_algo_header);
		final GridLayout gl_g = new GridLayout(2, false);
		gl_g.horizontalSpacing = 20;
		g.setLayout(gl_g);
		GridData gd_g = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_g.heightHint = 550;
		g.setLayoutData(gd_g);

		createLoadFile1(g);
		createPlain1(g);
		createCombinationArea(g);

		createLoadFile2(g);
		createPlain2(g);

		createLoadCipher(g);
		createCipher(g);
		// createEncodingModeArea(g);
	}

	/**
	 *
	 * This class makes a button that is used to read input from a file and
	 * print it into a textfield.
	 *
	 * @param the
	 *            parent item
	 */
	private void createLoadFile1(final Composite parent) {
	}

	/**
	 * Creates a text field for the plaintext.
	 *
	 * @param parent
	 */
	private void createPlain1(final Composite parent) {
		final Canvas canvas_1 = new Canvas(parent, SWT.NONE);
		canvas_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		GridLayout gl_canvas_1 = new GridLayout();
		canvas_1.setLayout(gl_canvas_1);

		Label plain1Label_1 = new Label(canvas_1, SWT.PUSH);
		plain1Label_1.setText(Messages.XORComposite_Plain1);

		Button loadPlain1 = new Button(canvas_1, SWT.PUSH);
		loadPlain1.setText(Messages.XORComposite_loadFile);
		GridData gd_loadPlain1 = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
		gd_loadPlain1.grabExcessHorizontalSpace = true;
		gd_loadPlain1.horizontalAlignment = SWT.FILL;
		gd_loadPlain1.verticalAlignment = SWT.FILL;
		loadPlain1.setLayoutData(gd_loadPlain1);

		loadPlain1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				dialog.setFilterNames(new String[] { IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME });
				dialog.setFilterExtensions(
						new String[] { IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION });
				dialog.setFilterPath(DirectoryService.getUserHomeDir());

				String filename = dialog.open();
				if (filename != null) {
					String text = new IO().read(filename, "\r\n"); //$NON-NLS-1$
					plain1.setText(text); // printing text into textfield
				}
			}
		});
		Combo loadPlain1c = new Combo(canvas_1, SWT.NONE);
		GridData gd_loadPlain1c = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
		gd_loadPlain1c.grabExcessHorizontalSpace = true;
		gd_loadPlain1c.horizontalAlignment = SWT.FILL;
		gd_loadPlain1c.verticalAlignment = SWT.FILL;
		loadPlain1c.setLayoutData(gd_loadPlain1c);
		loadPlain1c.add(Messages.XORComposite_combo_default); 
		predefinedTexts.forEach(s -> loadPlain1c.add(limitS(30, s)));

		loadPlain1c.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				int idxOf = loadPlain1c.getSelectionIndex();
				if (idxOf > 0 && idxOf <= predefinedTexts.size()) {
					setPlaintext(1, predefinedTexts.get(idxOf - 1));
				}

			}
		});
		plain1 = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_plain1 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_plain1.horizontalIndent = 1;
		gd_plain1.heightHint = 100;
		// This avoids that the textfield width stays optimal when resizing the window.
		gd_plain1.widthHint = parent.getClientArea().x;
		plain1.setLayoutData(gd_plain1);
		plain1.setText(Messages.XORComposite_Plain1DefaultText);
		
		final Canvas canvas = new Canvas(parent, SWT.NONE);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		canvas.setLayout(new GridLayout());

		Label plain1Label = new Label(canvas, SWT.PUSH);
		plain1Label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		plain1Label.setText(Messages.XORComposite_Plain2);

		Button loadPlain2 = new Button(canvas, SWT.PUSH);
		loadPlain2.setText(Messages.XORComposite_loadFile);
		GridData gd_loadPlain2 = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
		gd_loadPlain2.grabExcessHorizontalSpace = true;
		gd_loadPlain2.verticalAlignment = SWT.TOP;
		gd_loadPlain2.horizontalAlignment = SWT.FILL;
		loadPlain2.setLayoutData(gd_loadPlain2);

		loadPlain2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				dialog.setFilterNames(new String[] { IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME });
				dialog.setFilterExtensions(
						new String[] { IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION });
				dialog.setFilterPath(DirectoryService.getUserHomeDir());

				String filename = dialog.open();
				if (filename != null) {
					String text = new IO().read(filename, "\r\n"); //$NON-NLS-1$
					plain2.setText(text); // printing text into textfield
				}
			}
		});
		Combo loadPlain2c = new Combo(canvas, SWT.NONE);
		GridData gd_loadPlain2c = new GridData(LOADBUTTONWIDTH, LOADBUTTONHEIGHT);
		gd_loadPlain2c.grabExcessHorizontalSpace = true;
		gd_loadPlain2c.horizontalAlignment = SWT.FILL;
		gd_loadPlain2c.verticalAlignment = SWT.FILL;
		loadPlain2c.setLayoutData(gd_loadPlain2c);
		loadPlain2c.add(Messages.XORComposite_combo_default); 
		predefinedTexts.forEach(s -> loadPlain2c.add(limitS(30, s)));

		loadPlain2c.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				int idxOf = loadPlain2c.getSelectionIndex();
				if (idxOf > 0 && idxOf <= predefinedTexts.size()) {
					setPlaintext(2, predefinedTexts.get(idxOf - 1));
				}

			}
		});
	}

	protected String limitS(int i, String s) {
		if (s.length() > i) {
			return s.substring(0, i) + "..."; //$NON-NLS-1$
		} else {
			return s;
		}
	}

	protected List<String> predefinedTexts = new LinkedList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("the kitten is in the basket fuzzy ball go"); //$NON-NLS-1$
			add("this is a codebook message that is unique"); //$NON-NLS-1$
			add("Give in like a good fellow, and bring your garrison to dinner, and beds afterwards. Nobody injured, I hope?"); //$NON-NLS-1$
			add("Deeply regret advise your Titanic sunk this morning fifteenth after collision iceberg resulting serious loss life further particulars later."); //$NON-NLS-1$
			add("It was a bright cold day in April, and the clocks were striking thirteen. Winston Smith, his chin nuzzled into his breast in an effort to escape the vile wind, slipped quickly through the glass doors of Victory Mansions, though not quickly enough to prevent a swirl of gritty dust from entering along with him. "); //$NON-NLS-1$

			add("Fürwahr! er dient Euch auf besondre Weise. Nicht irdisch ist des Toren Trank noch Speise. Ihn treibt die Gärung in die Ferne, er ist sich seiner Tollheit halb bewusst. Vom Himmel fordert er die schönsten Sterne, und von der Erde jede höchste Lust."); //$NON-NLS-1$
			add("Zieh diesen Geist von seinem Urquell ab, und führ ihn, kannst du ihn erfassen, auf deinem Wege mit herab, und steh' beschämt, wenn du bekennen musst: Ein guter Mensch, in seinem dunklen Drange, ist sich des rechten Weges wohl bewußt."); //$NON-NLS-1$
			add("Das Dorf lag in tiefem Schnee. Vom Schloßberg war nichts zu sehen, Nebel und Finsternis umgaben ihn, auch nicht der schwächste Lichtschein deutete das große Schloß an."); //$NON-NLS-1$
			add("Es war ein klarer, kalter Tag im April, und die Uhren schlugen gerade dreizehn, als Winston Smith, das Kinn an die Brust gepresst, um dem rauen Wind zu entgehen, rasch durch die Glasturen eines der Hauser des Victory-Blocks schlupfte, wenn auch nicht rasch genug, als dafür nicht zugleich mit ihm ein Wirbel griesigen Staubs eingedrungen ware."); //$NON-NLS-1$
		}
	};
	private Button calculate;
	private Button next;
	private Button exportButton;

	protected void setPlaintext(int i, String string) {
		if (i == 1) {
			plain1.setText(string);
		} else {
			plain2.setText(string);
		}
	}

	/**
	 * Creates radio buttons. This is used for determining the combination mode.
	 */
	private void createCombinationArea(final Composite parent) {

	}

	/**
	 *
	 * This class makes a button that is used to read input from a file and
	 * print it into a textfield.
	 *
	 * @param the
	 *            parent item
	 */
	private void createLoadFile2(final Composite parent) {
		plain2 = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_plain2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_plain2.heightHint = 100;
		// This avoids that the textfields width stays optimal when resizing the window.
		gd_plain2.widthHint = parent.getClientArea().x;
		plain2.setLayoutData(gd_plain2);
		plain2.setText(Messages.XORComposite_Plain2DefaultText);

		Canvas canvas_1 = new Canvas(g, SWT.NONE);
		canvas_1.setLayout(new GridLayout(2, false));
		canvas_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		Label plain1Label = new Label(canvas_1, SWT.PUSH);
		plain1Label.setText(Messages.XORComposite_cipher);
		new Label(canvas_1, SWT.NONE);

		Group options1 = new Group(canvas_1, SWT.NONE);
		GridLayout gl_options1 = new GridLayout();
		options1.setLayout(gl_options1);
		options1.setText(Messages.XORComposite_combination_header);
		options1.setToolTipText(Messages.options1tooltip);

		xor = new Button(options1, SWT.RADIO);
		mod = new Button(options1, SWT.RADIO);

		xor.setSelection(true);

		xor.setText(Messages.XORComposite_Combination_RadioXOR);
		mod.setText(Messages.XORComposite_Combination_RadioMOD);
		final Canvas canvas = new Canvas(canvas_1, SWT.NONE);
		canvas.setLayout(new GridLayout());

		Group options2 = new Group(canvas, SWT.NONE);
		options2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		options2.setLayout(new GridLayout());
		options2.setText(Messages.XORComposite_encodingmod_header);
		options2.setToolTipText(Messages.options2tooltip);

		hex = new Button(options2, SWT.RADIO);
		text = new Button(options2, SWT.RADIO);

		hex.setSelection(true);

		hex.setText(Messages.XORComposite_EncodingMode_RadioHEX);
		text.setText(Messages.XORComposite_EncodingMode_RadioUNI);

		hex.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				hex.setSelection(true);
				text.setSelection(false);

				// conversion to hex
				cipher.setText(ViterbiComposite.stringToHex(cipherString));
				subjectChanged();
			}
		});

		text.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setSelection(true);
				hex.setSelection(false);

				// unprintable chars will be replaced with "?"
				cipher.setText(ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd")); //$NON-NLS-1$
				subjectChanged();
			}
		});

		calculate = new Button(canvas_1, SWT.PUSH);
		calculate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		calculate.setText(Messages.XORComposite_calculate);

		exportButton = new Button(canvas_1, SWT.PUSH);
		exportButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		exportButton.setText(Messages.ViterbiComposite_exportButton);

		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
				dialog.setFilterNames(new String[] { IConstants.ALL_FILTER_NAME });
				dialog.setFilterExtensions(new String[] { IConstants.ALL_FILTER_EXTENSION });
				dialog.setFilterPath(DirectoryService.getUserHomeDir());

				String filename = dialog.open();
				if (filename != null) {
					IO io = new IO();
					io.write(cipher.getText(), filename + ".txt"); //$NON-NLS-1$
				}
			}
		});
		calculate.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {
				if (xor.getSelection()) {
					combi = new BitwiseXOR();
				} else {
					combi = new ModularAddition();
				}

				String plain1Text = plain1.getText();
				String plain2Text = plain2.getText();
				boolean textSelection = text.getSelection();
				XORCombinationBackgroundJob calculateJob = new XORCombinationBackgroundJob() {
					@Override
					public IStatus computation(IProgressMonitor monitor) {
						cipherString = combi.add(plain1Text, plain2Text);
						if (textSelection) {
							this.__result = ViterbiComposite.replaceUnprintableChars(cipherString, "\ufffd");
						} else {
							this.__result = ViterbiComposite.stringToHex(cipherString);
						}
						return Status.OK_STATUS;
					}
					public String name() {
						return "Viterbi: plaintext combination";
					};
				};
				calculateJob.finalizeListeners.add(status -> {
					calculateJob.liftNoClickDisplaySynced(getDisplay());
					if (status.isOK()) {
						getDisplay().syncExec(() -> {
							cipher.setText(calculateJob.__result); //$NON-NLS-1$
							subjectChanged();
						});
					}
				});
				calculateJob.imposeNoClickDisplayCurrentShellSynced(getDisplay());
				calculateJob.runInBackground();
			}
		});
		cipher = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_cipher = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_cipher.heightHint = 100;
		// This avoids that the textfields width stays optimal when resizing the window.
		gd_cipher.widthHint = parent.getClientArea().x;
		cipher.setLayoutData(gd_cipher);

		next = new Button(g, SWT.PUSH);
		next.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		next.setText(Messages.XORComposite_next);
		next.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				viterbiView.changeTab(cipherString, combi);
			}
		});
		new Label(g, SWT.NONE);
	}

	/**
	 * Creates a text field for the plaintext.
	 *
	 * @param parent
	 */
	private void createPlain2(final Composite parent) {
	}

	/**
	 * This class makes a button that is used to read input from a file and
	 * print it into a textfield.
	 *
	 * The format of the text printed depends on the format selected.
	 *
	 * @param the
	 *            parent item
	 */
	private void createLoadCipher(final Composite parent) {
	}

	/**
	 * Creates a text field for the ciphertext.
	 *
	 * @param parent
	 */
	private void createCipher(final Composite parent) {
	}

	/**
	 * This class creates radio buttons. It is used to determine the encoding
	 * mode
	 *
	 * @param parent
	 */
}
