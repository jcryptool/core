package org.jcryptool.visual.merkletree.ui;

import java.util.Arrays;

// import java.security.SecureRandom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Composite for the selection: "plain signature generation" as subelement of MerkleTreeSignatureComposite
 * It provides a simple GUI interface to sign messages and see the signature result
 * 
 * @author Kevin Muehlboeck
 * @author Christoph Sonnberger
 *
 */
public class PlainSignatureComposite extends Composite {

	private SUIT mode;
	private Text textSign;
	private Button createButton;
	private StyledText styledTextSign;
	private StyledText styledTextSignSize;
	private Label lSignaturSize;
	private String signature = null;
	private String[] splittedSignature;

	private Button indexSeedButton;
	private Button otsButton;
	private Spinner authPathSpinner;
	private Button authPathButton;

	private SelectionAdapter indexButtonListener;
	private SelectionAdapter otsButtonListener;
	private SelectionAdapter authPathButtonListener;

	private Color[] distinguishableColors;
	private Color black;
	private Color white;
	private int indexSeedLength;

	private int[] authPathStart;
	private int[] authPathEnd;
	private int[][][] reducedSignatures;
	private boolean[] authPathToggled;
	private int oldToggle = -1;

	private ISimpleMerkle merkle;
	protected StyleRange indexBold;

	/**
	 * Create the GUI. Includes message definition, signature generation
	 * and signature content
	 * 
	 * @param parent
	 * @param style
	 *        SWT Composite style bits
	 * @param merkle
	 * @param signatureComposite
	 *        the parent class needed for method calls
	 * @param mode
	 */
	public PlainSignatureComposite(Composite parent, int style, ISimpleMerkle merkle, MerkleTreeSignatureComposite signatureComposite, SUIT mode) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		this.merkle = merkle;
		this.mode = mode;

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		Group defineMessageGroup = new Group(this, SWT.NONE);
		defineMessageGroup.setLayout(new GridLayout(8, true));
		defineMessageGroup.setFont(FontService.getNormalBoldFont());
		defineMessageGroup.setText(Descriptions.MerkleTreeSign_0);

		GridData messageGroupLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1);
		messageGroupLayout.minimumHeight = SWT.DEFAULT;
		defineMessageGroup.setLayoutData(messageGroupLayout);

		textSign = new Text(defineMessageGroup, SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textSign.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN, 1));
		textSign.setText(Descriptions.MerkleTreeSign_1);
		createButton = new Button(this, SWT.NONE);
		createButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 2, 1));
		createButton.setText(Descriptions.MerkleTreeSign_2);

		lSignaturSize = new Label(this, SWT.READ_ONLY | SWT.WRAP | SWT.RIGHT);
		lSignaturSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
		lSignaturSize.setText(Descriptions.MerkleTreeSign_4);

		styledTextSignSize = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		styledTextSignSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
		styledTextSignSize.setText(Integer.toString(merkle.getKeyIndex()));

		Group signatureGroup = new Group(this, SWT.NONE);
		signatureGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN, 1));
		signatureGroup.setLayout(new GridLayout(40, true));
		signatureGroup.setText(Descriptions.MerkleTreeVerify_2);
		signatureGroup.setFont(FontService.getNormalBoldFont());

		Label buttonExplanationLabel = new Label(signatureGroup, SWT.NONE);
		buttonExplanationLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 10, 1));
		buttonExplanationLabel.setText(Descriptions.MerkleTreeSign_5);
		buttonExplanationLabel.setVisible(false);

		indexSeedButton = new Button(signatureGroup, SWT.TOGGLE);
		indexSeedButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));

		indexSeedButton.setVisible(false);

		if (mode != SUIT.XMSS_MT) {
			otsButton = new Button(signatureGroup, SWT.TOGGLE);
			otsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
			otsButton.setText(Descriptions.MerkleTreeSign_7);
			otsButton.setVisible(false);
		}

		authPathSpinner = new Spinner(signatureGroup, SWT.NONE);
		authPathSpinner.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		authPathSpinner.setVisible(false);

		authPathButton = new Button(signatureGroup, SWT.PUSH);
		authPathButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, mode != SUIT.XMSS_MT ? 8 : 22, 1));
		authPathButton.setVisible(false);

		black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		white = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		addColorListeners();

		// Set Strinsg depending on mode
		switch (mode) {
		case XMSS:
			indexSeedButton.setText(Descriptions.MerkleTreeSign_6_2);
			authPathButton.setText(Descriptions.MerkleTreeSign_8_1);
			break;
		case XMSS_MT:
			indexSeedButton.setText(Descriptions.MerkleTreeSign_6_2);
			authPathButton.setText(Descriptions.MerkleTreeSign_8_2);
			break;

		case MSS:
		default:
			indexSeedButton.setText(Descriptions.MerkleTreeSign_6_1);
			authPathButton.setText(Descriptions.MerkleTreeSign_8_1);
			break;
		}

		styledTextSign = new StyledText(signatureGroup, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
		styledTextSign.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 40, 1));

		textSign.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (textSign.getText().equals("")) {
					textSign.setText(Descriptions.MerkleTreeSign_1);
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				if (textSign.getText().equals(Descriptions.MerkleTreeSign_1)) {
					textSign.setText("");
				}

			}
		});

		createButton.addSelectionListener(new SelectionAdapter() {

			// Signature creation event
			@Override
			public void widgetSelected(SelectionEvent e) {
				String message = textSign.getText();
				signature = merkle.sign(message);
				if (signature != "") {

					styledTextSign.setText(signature);
					styledTextSignSize.setText(Converter._numberToPrefix(getSignatureLength(signature)));

					// Set highlight buttons visible
					buttonExplanationLabel.setVisible(true);
					indexSeedButton.setVisible(true);
					authPathSpinner.setVisible(true);
					authPathButton.setVisible(true);
					if (otsButton != null)
						otsButton.setVisible(true);
					// ******************************************
					// take calculations for color highlighting
					splittedSignature = signature.split("\\|");
					if (mode == SUIT.MSS) {
						indexSeedLength = splittedSignature[0].length();
					} else {
						indexSeedLength = splittedSignature[0].length() + splittedSignature[1].length() + 1;
					}
					indexBold = new StyleRange(0, indexSeedLength, new Color(getDisplay(), 176, 0, 0), styledTextSign.getBackground());
					indexBold.fontStyle = SWT.BOLD;
					switch (mode) {
					case XMSS:
						int i = 3;
						for (; i < splittedSignature.length; ++i)
							;
						authPathSpinner.setMinimum(0);
						authPathSpinner.setMaximum(i - 3 - 1);

						authPathStart = new int[i - 3];
						authPathEnd = new int[i - 3];
						authPathToggled = new boolean[i - 3];
						Arrays.fill(authPathToggled, false);

						authPathStart[0] = splittedSignature[0].length() + splittedSignature[1].length() + splittedSignature[2].length() + 7;
						for (int j = 1; j < authPathStart.length; ++j) {
							authPathEnd[j - 1] = authPathStart[j - 1] + splittedSignature[j + 3].length() + 1;
							authPathStart[j] = authPathStart[j - 1] + splittedSignature[j + 3].length() + 1;
						}
						authPathEnd[authPathEnd.length - 1] = authPathStart[authPathStart.length - 1] + splittedSignature[splittedSignature.length - 1].length();

						int authPathBeginning = splittedSignature[0].length() + splittedSignature[1].length() + 2;
						signature = signature.substring(0, authPathBeginning) + "\r\n" + signature.substring(authPathBeginning);
						styledTextSign.setText(signature);

						authPathBeginning = splittedSignature[0].length() + splittedSignature[1].length() + splittedSignature[2].length() + 5;
						signature = signature.substring(0, authPathBeginning) + "\r\n" + signature.substring(authPathBeginning);
						styledTextSign.setText(signature);

						break;
					case XMSS_MT:
						int reducedSignatureCount = 0;
						int maxSize = 0;
						for (int q = 2, t = 0; q < splittedSignature.length; ++q, ++t) {
							if (splittedSignature[q].length() > splittedSignature[1].length()) {
								reducedSignatureCount++;
								if (t > maxSize)
									maxSize = t;
							}
						}
						reducedSignatures = new int[reducedSignatureCount][maxSize][2];

						int j;
						int k;

						int lengthCounter = splittedSignature[0].length() + splittedSignature[1].length() + 2;
						String[] cutted = Arrays.copyOfRange(splittedSignature, 2, splittedSignature.length);
						for (i = 0, k = -1, j = 0; i < cutted.length; ++i) {
							if (cutted[i].length() > cutted[1].length()) {
								signature = signature.substring(0, lengthCounter) + "\r\n" + signature.substring(lengthCounter);
								lengthCounter += 2;
								++k;
								j = 0;
								reducedSignatures[k][j][0] = lengthCounter;
								reducedSignatures[k][j][1] = lengthCounter + cutted[i].length();
								lengthCounter += cutted[i].length() + 1;

							} else {
								reducedSignatures[k][++j][0] = lengthCounter;
								reducedSignatures[k][j][1] = lengthCounter + cutted[i].length();
								lengthCounter += cutted[i].length() + 1;
							}
						}
						styledTextSign.setText(signature);

						authPathToggled = new boolean[1];
						authPathToggled[0] = false;

						authPathSpinner.setMinimum(0);
						authPathSpinner.setMaximum(reducedSignatureCount - 1);

						break;
					case MSS:
						k = 2;
						for (; k < splittedSignature.length; ++k)
							;
						authPathSpinner.setMinimum(0);
						authPathSpinner.setMaximum(k - 3);
						authPathStart = new int[k - 2];
						authPathEnd = new int[k - 2];
						authPathToggled = new boolean[k - 2];
						Arrays.fill(authPathToggled, false);

						authPathStart[0] = splittedSignature[0].length() + splittedSignature[1].length() + 6;
						for (j = 1; j < authPathStart.length; ++j) {
							authPathEnd[j - 1] = authPathStart[j - 1] + splittedSignature[j + 2].length() + 1;
							authPathStart[j] = authPathStart[j - 1] + splittedSignature[j + 2].length() + 1;
						}
						authPathEnd[authPathEnd.length - 1] = authPathStart[authPathStart.length - 1] + splittedSignature[splittedSignature.length - 1].length();

						authPathBeginning = splittedSignature[0].length() + 1;
						signature = signature.substring(0, authPathBeginning) + "\r\n" + signature.substring(authPathBeginning);
						styledTextSign.setText(signature);

						authPathBeginning = splittedSignature[0].length() + splittedSignature[1].length() + 4;
						signature = signature.substring(0, authPathBeginning) + "\r\n" + signature.substring(authPathBeginning);
						styledTextSign.setText(signature);
					default:
						break;
					}
					// end calculations for color highlighting
					// ******************************************

					signatureComposite.addSignatureAndMessage(signature, message);
				} else {
					signatureComposite.keysExceededMessage();
				}
			}
		});
		// Disables the sign button if no message is given in the text field
		textSign.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (textSign.getText().length() > 0) {
					createButton.setEnabled(true);
				} else {
					createButton.setEnabled(false);
				}

			}
		});
	}

	/**
	 * Synchronizes Signature with the other Tabpages
	 * 
	 * @return Signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Return the used Message necessary for tab sync -> verification tab
	 * 
	 * @return usedText
	 */
	public String getMessage() {
		return textSign.getText();
	}

	/**
	 * Calculates the Length of a signature
	 *
	 * @param signature
	 * @return length of the signature
	 */
	public int getSignatureLength(String signature) {
		return signature.length() / 2;
	}

	/**
	 * returns the Index of a given signature as
	 * String the Index is the first Letter of the signature
	 * 
	 * @param signature
	 * @return index
	 */
	public String getKeyIndex(String signature) {
		int iend = signature.indexOf("|");
		String subString = signature.substring(0, iend);
		return subString;
	}

	/**
	 * Synchronizes the MerkleTree Object with the other Tabpages
	 * 
	 * @return ISimpleMerkle Object
	 */
	public ISimpleMerkle getMerkleFromForm() {
		return this.merkle;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of non-composite SWT components
		// this is a composite subclass
	}

	public void clearSignatureText() {
		styledTextSign.setText("");
		styledTextSignSize.setText("");
	}

	/**
	 * The following are color listeners used to highlight signature parts
	 * Some listeners are exclusive for certain modes
	 */
	private void addColorListeners() {
		distinguishableColors = new Color[4];
		distinguishableColors[0] = new Color(getDisplay(), 0, 186, 0);
		distinguishableColors[1] = new Color(getDisplay(), 186, 0, 186);
		distinguishableColors[2] = new Color(getDisplay(), 186, 186, 0);
		distinguishableColors[3] = new Color(getDisplay(), 0, 186, 186);

		// Used by every mode, either selects the index or index and seed
		indexButtonListener = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (indexSeedButton.getSelection()) {
					styledTextSign.setStyleRange(indexBold);
					styledTextSign.setTopIndex(0);
				} else {
					styledTextSign.setStyleRange(new StyleRange(0, indexSeedLength, black, white));
				}
			}

		};

		indexSeedButton.addSelectionListener(indexButtonListener);

		// This listener is only for MSS and XMSS. It is used to highlight the one-time signature
		otsButtonListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int start = indexSeedLength + 1;
				int length = mode == SUIT.MSS ? splittedSignature[1].length() + 2 : splittedSignature[2].length() + 2;
				if (otsButton.getSelection()) {
					styledTextSign.setStyleRange(new StyleRange(start, length, new Color(getDisplay(), 51, 204, 255), white));
					styledTextSign.setTopIndex(0);
				} else {
					styledTextSign.setStyleRange(new StyleRange(start, length, black, white));
				}
			}

		};
		if (mode != SUIT.XMSS_MT)
			otsButton.addSelectionListener(otsButtonListener);
		if (mode != SUIT.XMSS_MT) {
			// the authPathButton is used solely for the Authentication path in MSS and XMSS
			authPathButtonListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int spinner = authPathSpinner.getSelection();
					if (authPathToggled[spinner]) {
						styledTextSign.setStyleRange(new StyleRange(authPathStart[spinner], authPathEnd[spinner] - authPathStart[spinner], black, white));
						authPathToggled[spinner] = false;
					} else {
						styledTextSign.setTopIndex(styledTextSign.getLineAtOffset(authPathStart[spinner]) + 1);
						styledTextSign.setStyleRange(new StyleRange(authPathStart[spinner], authPathEnd[spinner] - authPathStart[spinner], distinguishableColors[spinner % 4], white));
						authPathToggled[spinner] = true;

					}
				}

			};
		} else {
			// in MultiTree however, it is used for reduced XMSS signatures. It is strictly
			// not the same feature but similiar
			authPathButtonListener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int spinner = authPathSpinner.getSelection();
					int start, end;
					boolean colouring = false;

					// Only one reduced XMSS signature should be highlighted at once.
					// (There are only few and it would be too confusing highlight more than one at the same time).
					// This logic provides, that only one signature can be highlighted at the same time
					if (authPathToggled[0] && oldToggle != spinner) {
						for (int i = 0; i < reducedSignatures[oldToggle].length && reducedSignatures[oldToggle][i][1] != 0; ++i) {
							start = reducedSignatures[oldToggle][i][0];
							end = reducedSignatures[oldToggle][i][1] - start;
							styledTextSign.setStyleRange(new StyleRange(start, end, black, white));
						}
						authPathToggled[0] = false;
					}
					if (oldToggle != spinner && authPathToggled[0] == false) {
						if (spinner == 0) {
							styledTextSign.setTopIndex(0);
						} else {
							styledTextSign.setTopIndex(styledTextSign.getLineAtOffset(reducedSignatures[spinner][0][0]));
						}
						for (int i = 0; i < reducedSignatures[spinner].length && reducedSignatures[spinner][i][1] != 0; ++i) {
							start = reducedSignatures[spinner][i][0];
							end = reducedSignatures[spinner][i][1] - start;
							styledTextSign.setStyleRange(new StyleRange(start, end, distinguishableColors[i % 4], white));
						}
						authPathToggled[0] = true;
						oldToggle = spinner;
						colouring = true;
					}
					if (authPathToggled[0] && colouring == false) {
						for (int i = 0; i < reducedSignatures[spinner].length && reducedSignatures[spinner][i][1] != 0; ++i) {
							start = reducedSignatures[spinner][i][0];
							end = reducedSignatures[spinner][i][1] - start;
							styledTextSign.setStyleRange(new StyleRange(start, end, black, white));
						}
						authPathToggled[0] = false;
						oldToggle = -1;
					}

				}
			};
		}
		authPathButton.addSelectionListener(authPathButtonListener);
	}

}
