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
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;

public class MerkleTreeKeyComposite extends Composite {

	private StyledText publicKeyText;
	private Label descLabel;
	private Label spacer;
	private Label buttonInfoLabel;
	private Label spinnerInfoLabel;
	private Text keyExplanation;

	private Group publicKeyGroup;
	private Group privateKeyGroup;

	private Button buttonIndex;
	private Button buttonSeed;
	private Button buttonLeaves;

	private Boolean nodeToggleMap[];
	private int spinnerValue;
	private int leafCounter;

	private Boolean toggleIndex = false;
	private Boolean toggleSeed = false;

	private String publicKey;
	private String privateKey;
	private String splittedPrivateKey[];

	private StyleRange indexBold;
	private Color distinguishableColors[];
	private Color black;

	private Label indexLabel;
	private Text indexText;
	private Label seedLabel;
	private Text seedText;
	private Composite indexSeedComposite;
	private Label leafLabel;
	private Label otsLabel;
	private Spinner privateOTSSpinner;
	private Text privateOTSKey;
	private GridLayout indexSeedLayout;
	private ISimpleMerkle merkle;

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

		keyExplanation = new Text(this, SWT.WRAP);
		keyExplanation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		keyExplanation.setText(Descriptions.MerkleTreeKeyTab_0);
		keyExplanation.setEditable(false);
		keyExplanation.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		publicKeyGroup = new Group(this, SWT.SHADOW_IN);
		publicKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		publicKeyGroup.setLayout(new GridLayout(1, true));
		publicKeyGroup.setFont(FontService.getNormalBoldFont());
		publicKeyGroup.setText(Descriptions.MerkleTreeKeyTab_1);

		// text field storing public key
		publicKeyText = new StyledText(publicKeyGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		publicKeyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		publicKeyText.setText(publicKey);

		privateKeyGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
		privateKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, MerkleConst.H_SPAN_MAIN * 2, 1));
		privateKeyGroup.setLayout(new GridLayout(8, true));
		privateKeyGroup.setFont(FontService.getNormalBoldFont());
		privateKeyGroup.setText(Descriptions.MerkleTreeKeyTab_2);

		// The private key is splitted to get the corrrect positions of
		// seed/leaves
		splittedPrivateKey = privateKey.split("\\|");

		indexSeedComposite = new Composite(privateKeyGroup, SWT.NONE);
		indexSeedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 8, 1));
		// indexSeedComposite.setLayoutData(indexSeedLayout);
		indexSeedLayout = new GridLayout(8, true);
		indexSeedLayout.marginWidth = 0;
		// indexSeedComposite.setLayout(new GridLayout(8, true));
		indexSeedComposite.setLayout(indexSeedLayout);

		indexLabel = new Label(indexSeedComposite, SWT.NONE);
		indexLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		indexLabel.setText(Descriptions.MerkleTreeKeyTab_3);

		indexText = new Text(indexSeedComposite, SWT.NONE);
		indexText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		indexText.setText(splittedPrivateKey[0]);

		Label spacer = new Label(indexSeedComposite, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));

		seedLabel = new Label(indexSeedComposite, SWT.NONE);
		seedLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		seedLabel.setText(Descriptions.MerkleTreeKeyTab_4);

		seedText = new Text(indexSeedComposite, SWT.NONE);
		seedText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		seedText.setText(splittedPrivateKey[1]);

		otsLabel = new Label(privateKeyGroup, SWT.NONE);
		otsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		otsLabel.setText(Descriptions.MerkleTreeKeyTab_5);

		privateOTSSpinner = new Spinner(privateKeyGroup, SWT.NONE);
		privateOTSSpinner.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		privateOTSSpinner.setMinimum(0);
		privateOTSSpinner.setMaximum(merkle.getLeafCounter() - 1);

		privateOTSKey = new Text(privateKeyGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		privateOTSKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1));
		privateOTSKey.setText(splittedPrivateKey[2]);

		privateOTSSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				privateOTSKey.setText(splittedPrivateKey[privateOTSSpinner.getSelection() + 2]);
			}

		});

		// Buttons to toggle color highlighting
		// Composite buttonComposite = new Composite(privateKeyGroup, SWT.NONE);
		//
		// buttonInfoLabel = new Label(buttonComposite, SWT.NONE);
		// buttonInfoLabel.setText(Descriptions.MerkleTreeKeyTab_6);
		// buttonInfoLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
		// false, false, 2, 1));
		//
		// buttonIndex = new Button(buttonComposite, SWT.TOGGLE);
		// buttonIndex.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
		// false, 1, 1));
		// buttonIndex.setText(Descriptions.MerkleTreeKeyTab_3);
		//
		// buttonLeaves = new Button(buttonComposite, SWT.NONE);
		// buttonLeaves.setLayoutData(new GridData(SWT.END, SWT.CENTER, false,
		// false, 2, 1));
		// buttonLeaves.setText(Descriptions.MerkleTreeKeyTab_5);
		//
		// // spinner to toggle leaf
		// leafCounter = merkle.getLeafCounter();
		// spinnerLeaf = new Spinner(buttonComposite, SWT.BORDER);
		// spinnerLeaf.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
		// false, 1, 1));
		// spinnerLeaf.setMinimum(0);
		// spinnerLeaf.setMaximum(leafCounter - 1);
		// spinnerValue = 0;
		//
		// spinnerInfoLabel = new Label(buttonComposite, SWT.NONE);
		// spinnerInfoLabel.setText("0 - " + (merkle.getLeafCounter() - 1));
		//
		// buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
		// true, false, 16, 1));
		// buttonComposite.setLayout(new GridLayout(8, true));

		// Composite privateKeyComposite = new Composite(privateKeyGroup,
		// SWT.NONE);
		// privateKeyComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
		// true, true, MerkleConst.H_SPAN_MAIN * 2, 1));
		// privateKeyComposite.setLayout(new GridLayout(1, true));

		// privateKeyText = new StyledText(privateKeyComposite, SWT.BORDER |
		// SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		// privateKeyText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		// true, MerkleConst.H_SPAN_MAIN, 1));
		// privateKeyText.setText(privateKey);

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

		int indexSeedLength = splittedPrivateKey[0].length() + splittedPrivateKey[1].length() + 1;
		// indexBold = new StyleRange(0, indexSeedLength, new
		// Color(getDisplay(), 176, 0, 0), privateKeyText.getBackground());
		// indexBold.fontStyle = SWT.BOLD;
		// SelectionListener to toggle color highlight for index
		// buttonIndex.addSelectionListener(new SelectionListener() {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// if (toggleIndex) {
		// setColor(0, indexSeedLength, black);
		// toggleIndex = false;
		// } else {
		// privateKeyText.setStyleRange(indexBold);
		// toggleIndex = true;
		// }
		// }
		//
		// @Override
		// public void widgetDefaultSelected(SelectionEvent e) {
		// }
		// });
		//
		// // the spinner value which selects a leaf
		// spinnerLeaf.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		//
		// spinnerValue = spinnerLeaf.getSelection();
		// }
		// });
		//
		// nodeToggleMap = new Boolean[leafCounter];
		// Arrays.fill(nodeToggleMap, false);
		//
		// // SelectionListener to toggle color highlight for leaves
		// buttonLeaves.addSelectionListener(new SelectionListener() {
		// int startingNode[] = new int[leafCounter];
		// int endingNode[] = new int[leafCounter];
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// // if leaf was already highlighted set black
		// if (nodeToggleMap[spinnerValue]) {
		// setColor(startingNode[spinnerValue], endingNode[spinnerValue] + 1,
		// black);
		// nodeToggleMap[spinnerValue] = false;
		// } else {
		// // if leaf wasn't highlighted calculate starting position
		// // and length
		// startingNode[spinnerValue] = 0;
		// endingNode[spinnerValue] = 0;
		// for (int i = 0; i < (spinnerValue + 2); ++i) {
		// startingNode[spinnerValue] += splittedPrivateKey[i].length();
		// startingNode[spinnerValue]++;
		// }
		// // set the values, the pipe is also set in color (--
		// // operation) due to a formatting bug
		// startingNode[spinnerValue]--;
		// endingNode[spinnerValue] = splittedPrivateKey[spinnerValue +
		// 2].length();
		// setColor(startingNode[spinnerValue], endingNode[spinnerValue] + 1,
		// getDistinguishableColor(spinnerValue % 4));
		// nodeToggleMap[spinnerValue] = true;
		// }
		// }
		//
		// @Override
		// public void widgetDefaultSelected(SelectionEvent e) {
		// }
		// });

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

	public void updateIndexText() {
		indexText.setText(String.valueOf(merkle.getKeyIndex()));
	}

	// private void setColor(int start, int length, Color color) {
	// privateKeyText.setStyleRange(new StyleRange(start, length, color, null));
	// }

}
