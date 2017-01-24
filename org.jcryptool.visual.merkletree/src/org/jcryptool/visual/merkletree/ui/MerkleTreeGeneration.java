package org.jcryptool.visual.merkletree.ui;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
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
import org.jcryptool.visual.merkletree.files.MathUtils;
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

	private byte[] seedarray;
	private byte[] bitmaskSeedarray;

	private Composite groupMaster;
	private Button buttonCreateSeed;
	private Label randomgenerator;
	private Text textSeed;
	private int wParameter = 16;

	private int spinnerValue;
	private int treeValue;

	// w Parameter Box
	private Button buttonSet4;
	private Button buttonSet16;
	private Label titleLabel;
	private StyledText wParamDescription;

	// Generate Keys Box
	private Button buttonCreateKeys;
	private Label createLabel;
	private Label createdKey;
	private StyledText descText;
	private Spinner keypairSpinner;

	private MessageBox successBox;
	private ArrayList<Integer> allowedHeight;
	private Spinner treeHeightSpinner;
	private int allowedIndex;

	/**
	 * Create the composite. Including Seed content and KeyPairComposite
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeGeneration(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		this.mode = mode;
		this.masterView = masterView;

		groupMaster = new Composite(this, SWT.NONE);
		groupMaster.setLayout(new GridLayout(8, true));
		groupMaster.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));

		/**
		 * (non-javadoc)
		 * 
		 * Seed Box
		 * 
		 */
		Group group = new Group(groupMaster, SWT.NONE);
		group.setText(Descriptions.Tab0_Head1);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		group.setLayout(new GridLayout(10, true));
		group.setFont(FontService.getNormalBoldFont());

		/*
		 * Seed Label
		 */
		randomgenerator = new Label(group, SWT.NONE);
		randomgenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
		// randomgenerator.setText(Descriptions.Tab0_Head1);

		/*
		 * Textbox for seed initiates textbox with a seed
		 */
		textSeed = new Text(group, SWT.BORDER | SWT.CENTER);
		textSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		seedarray = generateNewSeed();
		textSeed.setText(Converter._byteToHex(seedarray));

		/*
		 * Button generate new Seed
		 */
		buttonCreateSeed = new Button(group, SWT.NONE);
		buttonCreateSeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		buttonCreateSeed.setText(Descriptions.Tab0_Button1);

		buttonCreateSeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				seedarray = generateNewSeed();
				textSeed.setText(Converter._byteToHex(seedarray));
				((MerkleTreeView) masterView).updateElement();
			}
		});

		/*
		 * if XMSS or XMSS_MT is selected, also the Bitmask is requiered
		 * therefore the Bitmask Box is injected
		 */

		/**
		 * (non-javadoc)
		 * 
		 * Bitmask Seed
		 * 
		 */

		Button bitmaskButton;
		Label bitmaskLabel;
		StyledText bitmaskDescText;
		Text bitmaskText;
		Label randomgenerator;

		if (mode == SUIT.XMSS || mode == SUIT.XMSS_MT) {
			Group bitmaskGroup = new Group(groupMaster, SWT.NONE);
			bitmaskGroup.setText(Descriptions.Tab0_Head5);
			bitmaskGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
			bitmaskGroup.setLayout(new GridLayout(10, true));
			bitmaskGroup.setText(Descriptions.Tab0_Head3);
			bitmaskGroup.setFont(FontService.getNormalBoldFont());

			bitmaskLabel = new Label(bitmaskGroup, SWT.NONE);
			bitmaskLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 10, 1));

			bitmaskDescText = new StyledText(bitmaskGroup, SWT.WRAP | SWT.BORDER);
			bitmaskDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 2));
			bitmaskDescText.setText(Descriptions.Tab0_Txt3);
			bitmaskDescText.setEditable(false);
			bitmaskDescText.setCaret(null);

			/*
			 * Bitmask Seed Label
			 */
			randomgenerator = new Label(bitmaskGroup, SWT.NONE);
			randomgenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));
			randomgenerator.setText(Descriptions.Tab0_Head4);

			/*
			 * Textbox for seed
			 */
			bitmaskText = new Text(bitmaskGroup, SWT.BORDER | SWT.CENTER);
			bitmaskText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

			/*
			 * Button generate new Seed
			 */
			bitmaskButton = new Button(bitmaskGroup, SWT.NONE);
			bitmaskButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
			bitmaskButton.setText(Descriptions.Tab0_Button3);

			bitmaskSeedarray = generateNewSeed();
			bitmaskText.setText(Converter._byteToHex(bitmaskSeedarray));

			bitmaskButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					bitmaskSeedarray = generateNewSeed();
					bitmaskText.setText(Converter._byteToHex(bitmaskSeedarray));
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

		Group wParameterGroup = new Group(groupMaster, SWT.NONE);
		wParameterGroup.setText(Descriptions.Tab0_Head5);
		wParameterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		wParameterGroup.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		wParameterGroup.setFont(FontService.getNormalBoldFont());

		// headline
		titleLabel = new Label(wParameterGroup, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1));

		// text box with Description
		wParamDescription = new StyledText(wParameterGroup, SWT.WRAP | SWT.BORDER | SWT.READ_ONLY);
		wParamDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 2));

		// Radio Buttons 4/16
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

		/**
		 * (non-javadoc)
		 * 
		 * Key Generation Box
		 * 
		 */

		treeValue = 0;
		// this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		Group generateKeyGroup = new Group(groupMaster, SWT.NONE);
		generateKeyGroup.setText(Descriptions.Tab0_Head2);
		generateKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 1));
		generateKeyGroup.setLayout(new GridLayout(1, true));
		generateKeyGroup.setFont(FontService.getNormalBoldFont());

		// headline
		createLabel = new Label(generateKeyGroup, SWT.NONE);
		createLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

		// text
		descText = new StyledText(generateKeyGroup, SWT.WRAP | SWT.BORDER);
		descText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));
		descText.setCaret(null);

		//
		Composite keyRow = new Composite(generateKeyGroup, SWT.NONE);
		keyRow.setLayout(new GridLayout(8, true));
		keyRow.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 10, 1));

		// text - for the spinner
		Label keysum = new Label(keyRow, SWT.NONE);
		keysum.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));

		// spinner for the key-ammount
		Spinner keypairSpinner = new Spinner(keyRow, SWT.BORDER);
		if (mode != SUIT.XMSS_MT) {
			keypairSpinner.setMinimum(2);
			keypairSpinner.setSelection(8);
			spinnerValue = 8;
		} else {
			keypairSpinner.setMinimum(16);
			keypairSpinner.setSelection(16);
			spinnerValue = 16;
		}
		keypairSpinner.setMaximum(64);
		keypairSpinner.setSelection(8);
		keypairSpinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		allowedHeight = new ArrayList<>();

		// Text box with generated key info
		createdKey = new Label(keyRow, SWT.NONE);
		createdKey.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 3, 2));
		createdKey.setText(Descriptions.MerkleTreeKey_1);

		// 'create button'
		buttonCreateKeys = new Button(keyRow, SWT.NONE);
		buttonCreateKeys.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 2, 2));
		buttonCreateKeys.setFont(FontService.getNormalBoldFont());
		// if the Mode is MultiTree there is an extra spinner for the amount of
		// Trees (Tree-Layers)
		if (mode == SUIT.XMSS_MT) {

			Label trees = new Label(keyRow, SWT.NONE);
			trees.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
			trees.setText(Descriptions.XMSS_MT.Tab0_Lable2);

			treeHeightSpinner = new Spinner(keyRow, SWT.BORDER);
			treeHeightSpinner.setMaximum(64);
			treeHeightSpinner.setMinimum(2);
			treeHeightSpinner.setSelection(0);
			treeHeightSpinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			treeValue = 2;

			for (int i = 2; i < spinnerValue; ++i) {
				if (spinnerValue % i == 0) {
					allowedHeight.add(i);
				}
			}
			treeHeightSpinner.setMinimum(allowedHeight.get(0));
			treeHeightSpinner.setMaximum(allowedHeight.get(allowedHeight.size() - 1));

			allowedIndex = allowedHeight.lastIndexOf(treeValue);
			treeHeightSpinner.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int helper = treeHeightSpinner.getSelection();

					if (helper < treeValue) {
						treeHeightSpinner.setSelection(allowedHeight.get(--allowedIndex));
					} else {
						treeHeightSpinner.setSelection(allowedHeight.get(++allowedIndex));
					}

					((MerkleTreeView) masterView).updateElement();
					treeValue = treeHeightSpinner.getSelection();
				}
			});
		} else {
			Label trees = new Label(keyRow, SWT.NONE);
			trees.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 2, 1));
		}

		// set the spinner-value only to values of the power of 2
		keypairSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = keypairSpinner.getSelection();
				boolean isIncremented = true;
				//
				if (selection < spinnerValue) {
					isIncremented = false;
					keypairSpinner.setSelection(spinnerValue / 2);
				} else {
					keypairSpinner.setSelection(spinnerValue * 2);
				}
				spinnerValue = keypairSpinner.getSelection();

				((MerkleTreeView) masterView).updateElement();
				allowedHeight.clear();
				if (mode == SUIT.XMSS_MT) {

					while (true) {
						calculatePossibleTreeHeight();
						if (allowedHeight.size() <= 0) {
							keypairSpinner.setSelection(isIncremented ? spinnerValue * 2 : spinnerValue / 2);
							spinnerValue = keypairSpinner.getSelection();
						} else {
							break;
						}
					}

					treeHeightSpinner.setMinimum(allowedHeight.get(0));
					treeHeightSpinner.setMaximum(allowedHeight.get(allowedHeight.size() - 1));
				}

			}
		});

		// setting the text's depending on the actual suite
		keysum.setText(Descriptions.Tab0_Lable1);
		// createLabel.setText(Descriptions.Tab0_Head2);
		buttonCreateKeys.setText(Descriptions.Tab0_Button2);
		switch (mode) {
		case XMSS:
			descText.setText(Descriptions.XMSS.Tab0_Txt2);
			wParamDescription.setText(Descriptions.XMSS.Tab0_Txt1);
			break;
		case XMSS_MT:
			descText.setText(Descriptions.XMSS_MT.Tab0_Txt2);
			wParamDescription.setText(Descriptions.XMSS_MT.Tab0_Txt1);
			break;
		case MSS:
		default:
			descText.setText(Descriptions.MSS.Tab0_Txt2);
			wParamDescription.setText(Descriptions.MSS.Tab0_Txt1);
			break;
		}
		descText.setEditable(false);

		// MessageBox when successfully creating a Key
		successBox = new MessageBox(new Shell(), SWT.ICON_WORKING | SWT.OK);
		successBox.setText(Descriptions.MerkleTreeKey_4);
		successBox.setMessage(Descriptions.MerkleTreeKey_5);

		/**
		 * Event Listener for the generate keys button if this button is pressed
		 * a new merkle tree is generated
		 */
		buttonCreateKeys.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.
			 * eclipse.swt.events. SelectionEvent) generates the MerkleTree and
			 * the KeyPairs
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

	ISimpleMerkle merkle;

	public ISimpleMerkle generateMerkleTree() {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

			@Override
			public void run() {

				/*
				 * select the type of suite
				 */
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

				/*
				 * create the merkle tree with the chosen values
				 */
				// if the generated Tree is a XMSSTree -> the
				// bitmaskseed is also needed
				if (merkle instanceof XMSSTree) {
					((XMSSTree) merkle).setBitmaskSeed(bitmaskSeedarray);
				}
				if (merkle instanceof MultiTree) {
					// ((MultiTree) merkle).setLayers(treeValue);
					((MultiTree) merkle).setSingleTreeHeight(treeValue);
				}
				merkle.setSeed(seedarray);
				merkle.setLeafCount(spinnerValue);
				merkle.setWinternitzParameter(wParameter);
				merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
				merkle.generateKeyPairsAndLeaves();
				merkle.generateMerkleTree();
				((MerkleTreeView) masterView).setAlgorithm(merkle, mode);

				// set or update the key information
				createdKey.setText(Descriptions.MerkleTreeKey_2 + " " + merkle.getKeyLength() + " " + Descriptions.MerkleTreeKey_3);

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

	private void calculatePossibleTreeHeight() {
		int logarithmand = (int) MathUtils.log2nlz(spinnerValue);
		for (int i = 2; i < logarithmand; ++i) {
			if (logarithmand % i == 0) {
				allowedHeight.add(i);
			}
		}
	}

}
