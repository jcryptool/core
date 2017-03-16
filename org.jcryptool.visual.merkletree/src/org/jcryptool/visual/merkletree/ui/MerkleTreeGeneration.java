package org.jcryptool.visual.merkletree.ui;

import java.security.SecureRandom;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.MultiTree;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite with the Seed in Tabpage 1
 * 
 * @author Fabian Mayer
 * @author <i>revised by</i>
 * @author Maximilian Lindpointner
 * 
 *
 */
public class MerkleTreeGeneration extends Composite {

	private ViewPart masterView;
	private SUIT mode;

	private byte[] publicSeed;
	private byte[] privateSeed;

	private Composite groupMaster;

	private Group publicSeedGroup;
	private Group wParameterGroup;
	private Group generateKeyGroup;
	private Group privateSeedGroup;

	private Text publicSeedText;
	private Button publicSeedButton;

	private Text privateSeedText;
	private Button privateSeedButton;
	// Planned for future feature
	// private StyledText bitmaskDescription;

	// w Parameter Box
	private Button buttonSet4;
	private Button buttonSet16;
	private int wParameter = 16;
	private StyledText wParamDescription;

	// Generate Keys Box
	private Button createKeysButton;
	private Label createdKey;
	private StyledText generateKeyDescription;
	private Spinner keypairSpinner;
	private int spinnerValue;

	private MessageBox successBox;
	private Combo multiTreeCombo;
	private int[][] multiTreeArguments;
	private int multiTreeArgumentIndex;
	private Label multitreeAmountLabel;

	/**
	 * Create the composite. Including Seed content and KeyPairComposite
	 * 
	 * @param parent
	 * @param style
	 *        SWT Composite style bits
	 */
	public MerkleTreeGeneration(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.mode = mode;
		this.masterView = masterView;

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		groupMaster = new Composite(this, SWT.NONE);
		groupMaster.setLayout(new GridLayout(8, true));
		groupMaster.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		publicSeedGroup = new Group(groupMaster, SWT.NONE);
		publicSeedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		publicSeedGroup.setLayout(new GridLayout(10, true));
		publicSeedGroup.setFont(FontService.getNormalBoldFont());

		Label leftSpacer0 = new Label(publicSeedGroup, SWT.NONE);
		leftSpacer0.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));

		publicSeedText = new Text(publicSeedGroup, SWT.BORDER | SWT.CENTER);
		publicSeedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		publicSeed = generateNewSeed();
		publicSeedText.setText(Converter._byteToHex(publicSeed));

		publicSeedButton = new Button(publicSeedGroup, SWT.NONE);
		publicSeedButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));

		publicSeedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				publicSeed = generateNewSeed();
				publicSeedText.setText(Converter._byteToHex(publicSeed));
				((MerkleTreeView) masterView).updateElement();
			}
		});

		if (mode == SUIT.XMSS || mode == SUIT.XMSS_MT) {
			privateSeedGroup = new Group(groupMaster, SWT.NONE);
			privateSeedGroup.setText(Descriptions.Tab0_Head5);
			privateSeedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			privateSeedGroup.setLayout(new GridLayout(10, true));
			privateSeedGroup.setText(Descriptions.Tab0_Seed0);
			privateSeedGroup.setFont(FontService.getNormalBoldFont());

			// bitmaskDescription = new StyledText(bitmaskGroup, SWT.WRAP |
			// SWT.BORDER);
			// bitmaskDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
			// true, false, 10, 2));
			// bitmaskDescription.setText(Descriptions.Tab0_Txt3);
			// bitmaskDescription.setEditable(false);
			// bitmaskDescription.setCaret(null);

			Label leftSpacer1 = new Label(privateSeedGroup, SWT.NONE);
			leftSpacer1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));

			privateSeedText = new Text(privateSeedGroup, SWT.BORDER | SWT.CENTER);
			privateSeedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

			privateSeedButton = new Button(privateSeedGroup, SWT.NONE);
			privateSeedButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
			privateSeedButton.setText(Descriptions.Tab0_Seed3);

			privateSeed = generateNewSeed();
			privateSeedText.setText(Converter._byteToHex(privateSeed));

			privateSeedButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					privateSeed = generateNewSeed();
					privateSeedText.setText(Converter._byteToHex(privateSeed));
					((MerkleTreeView) masterView).updateElement();
				}
			});
		}

		/**
		 * (non-javadoc)
		 * 
		 * Winternitz Parameter Box
		 * 
		 */

		wParameterGroup = new Group(groupMaster, SWT.NONE);
		wParameterGroup.setText(Descriptions.Tab0_Head5);
		wParameterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		GridLayout wParameterGroupLayout = new GridLayout(8, true);
		wParameterGroupLayout.verticalSpacing = 13;
		wParameterGroup.setLayout(wParameterGroupLayout);
		wParameterGroup.setFont(FontService.getNormalBoldFont());

		wParamDescription = new StyledText(wParameterGroup, SWT.WRAP | SWT.BORDER);
		wParamDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));
		wParamDescription.setEditable(false);
		wParamDescription.setCaret(null);

		buttonSet4 = new Button(wParameterGroup, SWT.RADIO);
		buttonSet4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet4.setText("4");

		buttonSet16 = new Button(wParameterGroup, SWT.RADIO);
		buttonSet16.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		buttonSet16.setText("16");
		buttonSet16.setSelection(true);

		buttonSet4.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 4;
				((MerkleTreeView) masterView).updateElement();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		buttonSet16.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				wParameter = 16;
				((MerkleTreeView) masterView).updateElement();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		generateKeyGroup = new Group(groupMaster, SWT.NONE);
		generateKeyGroup.setText(Descriptions.Tab0_Head2);
		generateKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		GridLayout generateKeyGroupLayout = new GridLayout(1, true);
		generateKeyGroupLayout.verticalSpacing = 13;
		generateKeyGroup.setLayout(generateKeyGroupLayout);
		generateKeyGroup.setFont(FontService.getNormalBoldFont());

		// text
		generateKeyDescription = new StyledText(generateKeyGroup, SWT.WRAP | SWT.BORDER);
		generateKeyDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));
		generateKeyDescription.setCaret(null);
		generateKeyDescription.setEditable(false);
		//
		Composite keyRow = new Composite(generateKeyGroup, SWT.NONE);
		GridLayout keyRowLayout = new GridLayout(16, true);
		keyRowLayout.verticalSpacing = 13;
		keyRow.setLayout(keyRowLayout);
		keyRow.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 8, 1));

		// spinner for the key-ammount
		if (mode != SUIT.XMSS_MT) {
			Label keysum = new Label(keyRow, SWT.NONE);
			keysum.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
			keysum.setText(Descriptions.Tab0_Lable1);

			keypairSpinner = new Spinner(keyRow, SWT.BORDER);
			keypairSpinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			keypairSpinner.setMinimum(2);
			keypairSpinner.setMaximum(64);
			keypairSpinner.setSelection(8);
			spinnerValue = 8;

			keypairSpinner.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					int selection = keypairSpinner.getSelection();
					//
					if (selection < spinnerValue) {
						keypairSpinner.setSelection(spinnerValue / 2);
					} else {
						keypairSpinner.setSelection(spinnerValue * 2);
					}
					spinnerValue = keypairSpinner.getSelection();
					((MerkleTreeView) masterView).updateElement();

				}
			});
		} else {
			// combobox arguments when calling MultiTree, some variants have
			// been commented because they make the tree look too messy.
			multiTreeArguments = new int[][] { /* { 1, 16 }, */ { 3, 16 }, /* { 1, 64 }, */ { 3, 64 }, { 4, 64 } };

			Label multiTreeAmountDescription = new Label(keyRow, SWT.NONE);
			multiTreeAmountDescription.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
			multiTreeAmountDescription.setText(Descriptions.Tab0_Lable1);

			multitreeAmountLabel = new Label(keyRow, SWT.NONE);
			multitreeAmountLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			multitreeAmountLabel.setText(String.valueOf(multiTreeArguments[0][1]));

		}

		createdKey = new Label(keyRow, SWT.NONE);
		createdKey.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 5, 2));
		createdKey.setText(Descriptions.MerkleTreeKey_1);

		createKeysButton = new Button(keyRow, SWT.NONE);
		createKeysButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 5, 2));
		createKeysButton.setFont(FontService.getNormalBoldFont());

		if (mode == SUIT.XMSS_MT) {
			Label keysum = new Label(keyRow, SWT.NONE);
			keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			keysum.setText(Descriptions.Tab0_Head6);
			multiTreeCombo = new Combo(keyRow, SWT.READ_ONLY);
			multiTreeCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));

			// combobox arguments when calling MultiTree, some variants have
			// been commented on purpose because they make the tree look too
			// messy, but could be used.
			// multiTreeCombo.add(Descriptions.Tab0_MT_1);
			multiTreeCombo.add(Descriptions.Tab0_MT_2);
			// multiTreeCombo.add(Descriptions.Tab0_MT_3);
			multiTreeCombo.add(Descriptions.Tab0_MT_4);
			multiTreeCombo.add(Descriptions.Tab0_MT_5);

			multiTreeCombo.select(0);
			multiTreeCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					multiTreeArgumentIndex = multiTreeCombo.getSelectionIndex();
					multitreeAmountLabel.setText(String.valueOf(multiTreeArguments[multiTreeArgumentIndex][1]));
					((MerkleTreeView) masterView).updateElement();
				}
			});
		}

		// MessageBox when successfully creating a Key
		successBox = new MessageBox(this.getShell(), SWT.ICON_WORKING | SWT.OK);
		successBox.setMessage(Descriptions.MerkleTreeKey_3);

		// Switch for setting Strings according to selected mode
		switch (mode) {
		case XMSS:
			publicSeedGroup.setText(Descriptions.Tab0_Seed1);
			publicSeedButton.setText(Descriptions.Tab0_Seed2);
			wParamDescription.setText(Descriptions.XMSS.Tab0_Txt1);
			generateKeyDescription.setText(Descriptions.XMSS.Tab0_Txt2);
			createKeysButton.setText(Descriptions.XMSS.Tab0_Key_Button);
			successBox.setText(Descriptions.XMSS.Tab0_MessageBox0);

			break;
		case XMSS_MT:
			publicSeedGroup.setText(Descriptions.Tab0_Seed1);
			publicSeedButton.setText(Descriptions.Tab0_Seed2);
			wParamDescription.setText(Descriptions.XMSS_MT.Tab0_Txt1);
			generateKeyDescription.setText(Descriptions.XMSS_MT.Tab0_Txt2);
			createKeysButton.setText(Descriptions.XMSS_MT.Tab0_Key_Button);
			successBox.setText(Descriptions.XMSS_MT.Tab0_MessageBox0);
			break;
		case MSS:
		default:
			publicSeedGroup.setText(Descriptions.Tab0_Head1);
			publicSeedButton.setText(Descriptions.Tab0_Button0);
			wParamDescription.setText(Descriptions.MSS.Tab0_Txt1);
			generateKeyDescription.setText(Descriptions.MSS.Tab0_Txt2);
			createKeysButton.setText(Descriptions.MSS.Tab0_Key_Button);
			successBox.setText(Descriptions.MSS.Tab0_MessageBox0);

			break;
		}

		// ***********************************
		// End of GUI elements
		// ***********************************

		// Event for creating a key
		createKeysButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc) generates the MerkleTree and the KeyPairs
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateMerkleTree();
				successBox.open();
				((MerkleTreeView) masterView).removeFocus();
			}

		});

		// Listener to set "neutral" focus on current view, where you can scroll
		// and click everything
		Listener setLocalFocus = new Listener() {
			@Override
			public void handleEvent(Event event) {
				((MerkleTreeView) masterView).removeFocus();
			}
		};
		// Gets all groups in this tab and adds above listener
		Control groupChildren[] = groupMaster.getChildren();

		for (int i = 0; i < groupChildren.length; ++i) {
			if (groupChildren[i] instanceof Group) {
				groupChildren[i].addListener(SWT.MouseUp, setLocalFocus);
			}
		}

	}

	private ISimpleMerkle merkle;

	/**
	 * Creates a MerkleTree key pair depending on SUIT
	 * Parameters required:
	 * -number of leafs (or when MT single tree height/tree counter
	 * -public seed
	 * -private seed (when XMSS or MT)
	 * -Hash and OTS Parameter
	 * -Winternitz Parameter (when Winternitz OTS is used)
	 * 
	 * @return an instance of ISimpleMerkle, containing a key pair and MerkleTree
	 */
	public ISimpleMerkle generateMerkleTree() {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {
				// select the type of scheme
				switch (mode) {
				case XMSS:
					merkle = new XMSSTree();
					break;
				case XMSS_MT:
					merkle = new MultiTree();
					break;
				case MSS:
				default:
					merkle = new SimpleMerkleTree();
					break;
				}

				// create the merkle tree with the chosen values
				if (mode == SUIT.XMSS) {
					((XMSSTree) merkle).setBitmaskSeed(privateSeed);
				}
				if (mode == SUIT.XMSS_MT) {
					merkle.setLeafCount(multiTreeArguments[multiTreeArgumentIndex][1]);
					((MultiTree) merkle).setSingleTreeHeight(multiTreeArguments[multiTreeArgumentIndex][0]);
					((MultiTree) merkle).setSKSeed(publicSeed);
					((MultiTree) merkle).setPKSeed(privateSeed);
				} else {
					merkle.setLeafCount(spinnerValue);
					merkle.setSeed(publicSeed);
				}

				// merkle.setLeafCount(spinnerValue);
				merkle.setWinternitzParameter(wParameter);
				merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
				merkle.generateKeyPairsAndLeaves();
				merkle.generateMerkleTree();
				((MerkleTreeView) masterView).setAlgorithm(merkle, mode);

				// set or update the key information
				createdKey.setText(Descriptions.MerkleTreeKey_2 + " " + Converter._numberToPrefix(merkle.getKeyLength()));

			}
		});
		return merkle;
	}

	/**
	 * generates a new random seed
	 * 
	 * @return random seed
	 */
	private byte[] generateNewSeed() {
		SecureRandom secureRandomGenerator = new SecureRandom();
		byte[] randomBytes = new byte[128];
		secureRandomGenerator.nextBytes(randomBytes);
		// set the seed length
		int seedByteCount = 16;
		byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
		return seed;
	}

}
