package org.jcryptool.visual.merkletree.ui;

import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;

public class MerkleTreeKeyComposite extends Composite {

	StyledText publicKeyText;
	Label privateKeyLabel;
	Label publicKeyLabel;
	Label descLabel;
	Label spacer;
	Label buttonInfoLabel;
	Label spinnerInfoLabel;
	Label keyExplanation;

	Button buttonIndex;
	Button buttonSeed;
	Button buttonLeaves;
	Spinner spinnerLeaf;
	StyledText privateKeyText;
	Boolean nodeToggleMap[];
	int spinnerValue;
	int leafCounter;

	Boolean toggleIndex = false;
	Boolean toggleSeed = false;

	String publicKey;
	String privateKey;
	String splittedPrivateKey[];

	StyleRange indexBold;
	Color distinguishableColors[];
	Color black;
	ISimpleMerkle merkle;

	/**
	 * Creates the key tab composite. Displays Private/Public keys of a
	 * ISimpleMerkle *
	 * 
	 * @param parent
	 * @param style
	 * @param merkle
	 */

	public MerkleTreeKeyComposite(Composite parent, int style, ISimpleMerkle merkle) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN * 2, true));
		this.merkle = merkle;

		publicKey = merkle.getPublicKey();
		privateKey = merkle.getPrivateKey();

		/*
		 * Begin of building GUI Labels and styled Text fields
		 */

		// Description in top right corner
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));

		keyExplanation = new Label(this, SWT.WRAP | SWT.BORDER);
		keyExplanation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		keyExplanation.setText(Descriptions.MerkleTreeKeyTab_0);
		keyExplanation.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// spacer
		spacer = new Label(this, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN * 2, 1));

		Group publicKeyLabel = new Group(this, SWT.SHADOW_IN);
		publicKeyLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		publicKeyLabel.setLayout(new GridLayout(1, true));
		publicKeyLabel.setText(Descriptions.MerkleTreeKeyTab_1);

		// text field storing public key
		publicKeyText = new StyledText(publicKeyLabel, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		publicKeyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		publicKeyText.setText(publicKey);

		Group privateKeyLabel = new Group(this, SWT.SHADOW_ETCHED_IN);
		privateKeyLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, MerkleConst.H_SPAN_MAIN * 2, 1));
		privateKeyLabel.setLayout(new GridLayout(1, true));
		privateKeyLabel.setText(Descriptions.MerkleTreeKeyTab_2);

		// Buttons to toggle color highlighting
		Composite buttonComposite = new Composite(privateKeyLabel, SWT.NONE);

		buttonInfoLabel = new Label(buttonComposite, SWT.NONE);
		buttonInfoLabel.setText("Schlüsselteil farblich hervorheben: ");
		buttonInfoLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		buttonIndex = new Button(buttonComposite, SWT.TOGGLE);
		buttonIndex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		buttonIndex.setText(Descriptions.MerkleTreeKeyTab_3);

		buttonSeed = new Button(buttonComposite, SWT.TOGGLE);
		buttonSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		buttonSeed.setText(Descriptions.MerkleTreeKeyTab_4);

		buttonLeaves = new Button(buttonComposite, SWT.NONE);
		buttonLeaves.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false, 2, 1));
		buttonLeaves.setText(Descriptions.MerkleTreeKeyTab_5);

		// spinner to toggle leaf
		leafCounter = merkle.getLeafCounter();
		spinnerLeaf = new Spinner(buttonComposite, SWT.BORDER);
		spinnerLeaf.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		spinnerLeaf.setMinimum(0);
		spinnerLeaf.setMaximum(leafCounter - 1);
		spinnerValue = 0;

		spinnerInfoLabel = new Label(buttonComposite, SWT.NONE);
		spinnerInfoLabel.setText("0 - " + (merkle.getLeafCounter() - 1));

		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 16, 1));
		buttonComposite.setLayout(new GridLayout(8, true));

		Composite privateKeyComposite = new Composite(privateKeyLabel, SWT.NONE);
		privateKeyComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, MerkleConst.H_SPAN_MAIN * 2, 1));
		privateKeyComposite.setLayout(new GridLayout(1, true));

		privateKeyText = new StyledText(privateKeyComposite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		privateKeyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, MerkleConst.H_SPAN_MAIN, 1));
		privateKeyText.setText(privateKey);

		if (merkle instanceof XMSSTree) {
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
		} else if (merkle instanceof SimpleMerkleTree) {
			descLabel.setText(Descriptions.MSS.Tab1_Head0);

		}

		black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		// creates an array of well readable colors
		distinguishableColors = new Color[4];
		distinguishableColors[0] = new Color(getDisplay(), 186, 186, 0);
		distinguishableColors[1] = new Color(getDisplay(), 186, 0, 186);
		distinguishableColors[2] = new Color(getDisplay(), 0, 186, 186);
		distinguishableColors[3] = new Color(getDisplay(), 0, 186, 0);

		indexBold = new StyleRange(0, 1, new Color(getDisplay(), 176, 0, 0), privateKeyText.getBackground());
		indexBold.fontStyle = SWT.BOLD;
		// SelectionListener to toggle color highlight for index
		buttonIndex.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (toggleIndex) {
					setColor(0, 1, black);
					toggleIndex = false;
				} else {
					privateKeyText.setStyleRange(indexBold);
					// setColor(0, 1, new Color(getDisplay(), 176, 0, 0));
					toggleIndex = true;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// The private key is splitted to get the corrrect positions of
		// seed/leaves
		splittedPrivateKey = privateKey.split("\\|");

		// SelectionListener to toggle color highlight for seed
		buttonSeed.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (toggleSeed) {
					setColor(2, splittedPrivateKey[1].length(), black);
					toggleSeed = false;
				} else {
					setColor(2, splittedPrivateKey[1].length(), new Color(getDisplay(), 132, 132, 255));
					toggleSeed = true;
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// the spinner value which selects a leaf
		spinnerLeaf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				spinnerValue = spinnerLeaf.getSelection();
			}
		});

		nodeToggleMap = new Boolean[leafCounter];
		Arrays.fill(nodeToggleMap, false);

		// SelectionListener to toggle color highlight for leaves
		buttonLeaves.addSelectionListener(new SelectionListener() {
			int startingNode[] = new int[leafCounter];
			int endingNode[] = new int[leafCounter];

			@Override
			public void widgetSelected(SelectionEvent e) {
				// if leaf was already highlighted set black
				if (nodeToggleMap[spinnerValue]) {
					setColor(startingNode[spinnerValue], endingNode[spinnerValue] + 1, black);
					nodeToggleMap[spinnerValue] = false;
				} else {
					// if leaf wasn't highlighted calculate starting position
					// and length
					startingNode[spinnerValue] = 0;
					endingNode[spinnerValue] = 0;
					for (int i = 0; i < (spinnerValue + 2); ++i) {
						startingNode[spinnerValue] += splittedPrivateKey[i].length();
						startingNode[spinnerValue]++;
					}
					// set the values, the pipe is also set in color (--
					// operation) due to a formatting bug
					startingNode[spinnerValue]--;
					endingNode[spinnerValue] = splittedPrivateKey[spinnerValue + 2].length();
					setColor(startingNode[spinnerValue], endingNode[spinnerValue] + 1, getDistinguishableColor(spinnerValue % 4));
					nodeToggleMap[spinnerValue] = true;
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

	}

	// TODO: check if this method does anything, else remove it
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * 
	 * @param position
	 * @return a fixed value of a well distinguishable and readable color
	 */
	// this method actually needs a point, or it's useless
	private Color getDistinguishableColor(int position) {
		return distinguishableColors[position];
	}

	private void setColor(int start, int length, Color color) {
		privateKeyText.setStyleRange(new StyleRange(start, length, color, null));
	}

}
