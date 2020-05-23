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

	private Composite compositeMaster;

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
	// May come to use
	// private Label createdKey;
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
	 *            SWT Composite style bits
	 */
	public MerkleTreeGeneration(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, style);
		
		GridLayout gl_this = new GridLayout();
		gl_this.marginHeight = 0;
		gl_this.marginWidth = 0;
		this.setLayout(gl_this);
		
		this.mode = mode;
		this.masterView = masterView;

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		compositeMaster = new Composite(this, SWT.NONE);
		GridLayout gl_compositeMaster = new GridLayout();
		// No 5 px border around this composite.
		gl_compositeMaster.marginHeight = 0;
		gl_compositeMaster.marginWidth = 0;
		compositeMaster.setLayout(gl_compositeMaster);
		compositeMaster.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		publicSeedGroup = new Group(compositeMaster, SWT.NONE);
		publicSeedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_publicSeedGroup = new GridLayout(4, true);
		gl_publicSeedGroup.verticalSpacing = 15;
		publicSeedGroup.setLayout(gl_publicSeedGroup);
		publicSeedGroup.setFont(FontService.getNormalBoldFont());

//		Spacer Label
		new Label(publicSeedGroup, SWT.NONE);

		publicSeedText = new Text(publicSeedGroup, SWT.BORDER | SWT.CENTER);
		publicSeedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		publicSeed = generateNewSeed();
		publicSeedText.setText(Converter._byteToHex(publicSeed));

		publicSeedButton = new Button(publicSeedGroup, SWT.NONE);
		publicSeedButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

		publicSeedButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				publicSeed = generateNewSeed();
				publicSeedText.setText(Converter._byteToHex(publicSeed));
				((MerkleTreeView) masterView).updateElement();
			}
		});
		
		// Spacer label for distance below the public seed.
		Label label1 = new Label(publicSeedGroup, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		
		

		if (mode == SUIT.XMSS || mode == SUIT.XMSS_MT) {
			privateSeedGroup = new Group(compositeMaster, SWT.NONE);
			privateSeedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			GridLayout gl_privateSeedGroup = new GridLayout(4, true);
			gl_privateSeedGroup.verticalSpacing = 15;
			privateSeedGroup.setLayout(gl_privateSeedGroup);
			privateSeedGroup.setText(Descriptions.Tab0_Seed0);
			privateSeedGroup.setFont(FontService.getNormalBoldFont());

			// Spacer Label left of private seed text
			new Label(privateSeedGroup, SWT.NONE);

			privateSeedText = new Text(privateSeedGroup, SWT.BORDER | SWT.CENTER);
			privateSeedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			privateSeed = generateNewSeed();
			privateSeedText.setText(Converter._byteToHex(privateSeed));

			privateSeedButton = new Button(privateSeedGroup, SWT.NONE);
			privateSeedButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
			privateSeedButton.setText(Descriptions.Tab0_Seed3);

			privateSeedButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					privateSeed = generateNewSeed();
					privateSeedText.setText(Converter._byteToHex(privateSeed));
					((MerkleTreeView) masterView).updateElement();
				}
			});
			
			// Spacer label for distance below the private seed.
			Label label2 = new Label(privateSeedGroup, SWT.NONE);
			label2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		}

		/**
		 * (non-javadoc)
		 * 
		 * Winternitz Parameter Box
		 * 
		 */

		wParameterGroup = new Group(compositeMaster, SWT.NONE);
		wParameterGroup.setText(Descriptions.Tab0_Head5);
		wParameterGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout gl_wParameterGroup = new GridLayout(MerkleConst.H_SPAN_MAIN, true);
		gl_wParameterGroup.verticalSpacing = 15;
		wParameterGroup.setLayout(gl_wParameterGroup);
		wParameterGroup.setFont(FontService.getNormalBoldFont());

		wParamDescription = new StyledText(wParameterGroup, SWT.WRAP | SWT.READ_ONLY);
		wParamDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));
		
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
		
		// Spacer label for distance below the 4 and 16 button
		Label label4 = new Label(wParameterGroup, SWT.NONE);
		label4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN, 1));

		generateKeyGroup = new Group(compositeMaster, SWT.NONE);
		generateKeyGroup.setText(Descriptions.Tab0_Head2);
		generateKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout generateKeyGroupLayout = new GridLayout(16, true);
		generateKeyGroupLayout.verticalSpacing = 15;
		generateKeyGroup.setLayout(generateKeyGroupLayout);
		generateKeyGroup.setFont(FontService.getNormalBoldFont());

		// text
		generateKeyDescription = new StyledText(generateKeyGroup, SWT.WRAP | SWT.READ_ONLY);
		generateKeyDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2 * MerkleConst.H_SPAN_MAIN, 1));


		// spinner for the key-ammount
		if (mode != SUIT.XMSS_MT) {
			Label keysum = new Label(generateKeyGroup, SWT.NONE);
			keysum.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
			keysum.setText(Descriptions.Tab0_Lable1);

			keypairSpinner = new Spinner(generateKeyGroup, SWT.BORDER);
			keypairSpinner.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			keypairSpinner.setMinimum(2);
			keypairSpinner.setMaximum(64);
			keypairSpinner.setSelection(8);
			spinnerValue = 8;

			keypairSpinner.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					int selection = keypairSpinner.getSelection();

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

			Label multiTreeAmountDescription = new Label(generateKeyGroup, SWT.NONE);
			multiTreeAmountDescription.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 4, 1));
			multiTreeAmountDescription.setText(Descriptions.Tab0_Lable1);

			multitreeAmountLabel = new Label(generateKeyGroup, SWT.NONE);
			multitreeAmountLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			multitreeAmountLabel.setText(String.valueOf(multiTreeArguments[0][1]));

		}

		createKeysButton = new Button(generateKeyGroup, SWT.NONE);
		createKeysButton.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 10, 2));
		createKeysButton.setFont(FontService.getNormalBoldFont());

		if (mode == SUIT.XMSS_MT) {
			Label keysum = new Label(generateKeyGroup, SWT.NONE);
			keysum.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			keysum.setText(Descriptions.Tab0_Head6);
			multiTreeCombo = new Combo(generateKeyGroup, SWT.READ_ONLY);
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
		generateKeyDescription.append(" " + Descriptions.MerkleTreeKey_1);

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

				// set or update the key information
				switch (mode) {
				case XMSS_MT:
					generateKeyDescription.setText(Descriptions.XMSS_MT.Tab0_Txt2);
					break;
				case XMSS:
					generateKeyDescription.setText(Descriptions.XMSS.Tab0_Txt2);
				case MSS:
				default:
					generateKeyDescription.setText(Descriptions.MSS.Tab0_Txt2);
					break;
				}
				// createdKey.setText(Descriptions.MerkleTreeKey_2 + " " + Converter._numberToPrefix(merkle.getKeyLength()));
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
		Control groupChildren[] = compositeMaster.getChildren();

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
