package org.jcryptool.visual.merkletree.ui;

import java.util.Arrays;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;

public class MerkleTreeKeyComposite extends Composite {

	StyledText publicKeySign;
	Label privateKeyLabel;
	Label publicKeyLabel;
	Label descLabel;

	Button buttonIndex;
	Button buttonSeed;
	Button buttonLeaves;
	Spinner spinnerLeaf;
	Boolean nodeToggleMap[];
	int spinnerValue;
	int leafCounter;

	Boolean toggleIndex = false;
	Boolean toggleSeed = false;

	String publicKey;
	String privateKey;
	String splittedPrivateKey[];

	Color distinguishableColors[];
	Color colorBlack;
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
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.merkle = merkle;

		publicKey = merkle.getPublicKey();
		privateKey = merkle.getPrivateKey();

		/*
		 * Begin of building GUI Labels and styled Text fields
		 */

		// Description in top right corner
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		// Label for private key
		privateKeyLabel = new Label(this, SWT.NONE);
		privateKeyLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
		privateKeyLabel.setText("Private Key");

		// Buttons to toggle color highlighting
		buttonIndex = new Button(this, SWT.TOGGLE);
		buttonIndex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
		buttonIndex.setText("Index");

		buttonSeed = new Button(this, SWT.TOGGLE);
		buttonSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
		buttonSeed.setText("Seed");

		buttonLeaves = new Button(this, SWT.TOGGLE);
		buttonLeaves.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
		buttonLeaves.setText("Toggle Leaf");

		// spinner to toggle leaf
		leafCounter = merkle.getLeafCounter();
		spinnerLeaf = new Spinner(this, SWT.NONE);
		spinnerLeaf.setMinimum(0);
		spinnerLeaf.setMaximum(leafCounter - 1);
		spinnerValue = 0;

		colorBlack = getDisplay().getSystemColor(SWT.COLOR_BLACK);

		// TODO: rework this test declaration; maybe inner class
		PrivateKeyTextComposite test = new PrivateKeyTextComposite(this, SWT.NONE, merkle);
		test.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, MerkleConst.H_SPAN_MAIN, 1));
		test.setPrivateKeyText(privateKey);

		// Label for public key
		publicKeyLabel = new Label(this, SWT.NONE);
		publicKeyLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));
		publicKeyLabel.setText("Public Key");

		// text field storing public key
		publicKeySign = new StyledText(this, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		publicKeySign.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));

		if (merkle instanceof XMSSTree) {
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
		} else if (merkle instanceof SimpleMerkleTree) {
			descLabel.setText(Descriptions.MSS.Tab1_Head0);

		}

		publicKeySign.setText(publicKey);

		// creates an array of well readable colors
		distinguishableColors = new Color[4];
		distinguishableColors[0] = new Color(getDisplay(), 186, 186, 0);
		distinguishableColors[1] = new Color(getDisplay(), 186, 0, 186);
		distinguishableColors[2] = new Color(getDisplay(), 0, 186, 186);
		distinguishableColors[3] = new Color(getDisplay(), 0, 186, 0);
		// SelectionListener to toggle color highlight for index
		buttonIndex.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (toggleIndex) {
					test.setColor(0, 1, getDisplay().getSystemColor(SWT.COLOR_BLACK));
					toggleIndex = false;
				} else {
					test.setColor(0, 1, new Color(getDisplay(), 176, 0, 0));
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
					test.setColor(2, splittedPrivateKey[1].length(), getDisplay().getSystemColor(SWT.COLOR_BLACK));
					toggleSeed = false;
				} else {
					test.setColor(2, splittedPrivateKey[1].length(), new Color(getDisplay(), 132, 132, 255));
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
					test.setColor(startingNode[spinnerValue], endingNode[spinnerValue] + 1,
							getDisplay().getSystemColor(SWT.COLOR_BLACK));
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
					test.setColor(startingNode[spinnerValue], endingNode[spinnerValue] + 1,
							getDistinguishableColor(spinnerValue % 4));
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

}
