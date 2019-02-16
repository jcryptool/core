package org.jcryptool.visual.merkletree.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
 * This class displays the key of an ISimpleMerkle instance
 * 
 * @author Michael Altenhuber
 *
 */

public class MerkleTreeKeyComposite extends Composite {

	private Text publicKeyText;
	private Label descLabel;
	private Text keyExplanation;

	private Group publicKeyGroup;
	private Group privateKeyGroup;

	private String publicKey;
	private String privateKey;
	private String splittedPublicKey[];
	private String splittedPrivateKey[];

	private Label indexLabel;
	private Text indexText;
	private Label seedLabel;
	private Text seedText;
	private Label privatePublicSeedLabel;
	private Text publicSeedText;
	private Composite indexSeedComposite;
	private Label otsLabel;
	private Spinner privateOTSSpinner;
	private Text privateOTSKey;
	private GridLayout indexSeedLayout;
	private ISimpleMerkle merkle;

	private int arrayCounter;

	/**
	 * Creates the GUI elements and required listeners
	 * 
	 * @param parent
	 * @param style
	 *        SWT Composite style bits
	 * @param merkle
	 */

	public MerkleTreeKeyComposite(Composite parent, int style, ISimpleMerkle merkle, SUIT mode) {
		super(parent, style);
		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN * 2, true));
		this.merkle = merkle;

		publicKey = merkle.getPublicKey();
		privateKey = merkle.getPrivateKey();

		// ***********************************
		// Beginning of GUI elements
		// ***********************************

		// Description in top right corner
		descLabel = new Label(this, SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));

		keyExplanation = new Text(this, SWT.WRAP);
		keyExplanation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		keyExplanation.setEditable(false);
		keyExplanation.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		publicKeyGroup = new Group(this, SWT.SHADOW_IN);
		publicKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, MerkleConst.H_SPAN_MAIN * 2, 1));
		publicKeyGroup.setLayout(new GridLayout(20, true));
		publicKeyGroup.setFont(FontService.getNormalBoldFont());
		publicKeyGroup.setText(Descriptions.MerkleTreeKeyTab_1);

		Label lengthDescriptionLabel = new Label(publicKeyGroup, SWT.NONE);
		lengthDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		lengthDescriptionLabel.setText(Descriptions.MerkleTreeKeyTab_8);

		Label lengthLabel = new Label(publicKeyGroup, SWT.NONE);
		lengthLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		String cleanedPublicKey = publicKey.replaceAll("\\|", "");
		lengthLabel.setText(Converter._numberToPrefix(cleanedPublicKey.length() / 2));

		Label spacerLine1 = new Label(publicKeyGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		spacerLine1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 20, 1));

		// as the key parts are displayed in their own text fields, it is splitted
		splittedPublicKey = publicKey.split("\\|");

		Label rootNodeLabel = new Label(publicKeyGroup, SWT.NONE);
		rootNodeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 7, 1));
		rootNodeLabel.setText(Descriptions.MerkleTreeKeyTab_9);

		Text rootNodeText = new Text(publicKeyGroup, SWT.READ_ONLY);
		rootNodeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 1));
		rootNodeText.setText(splittedPublicKey[0]);

		Label publicSeedLabel = new Label(publicKeyGroup, SWT.NONE);
		publicSeedLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 7, 1));

		publicKeyText = new Text(publicKeyGroup, SWT.READ_ONLY);
		publicKeyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 1));
		publicKeyText.setText(splittedPublicKey[1]);

		privateKeyGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
		privateKeyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN * 2, 1));
		privateKeyGroup.setLayout(new GridLayout(20, true));
		privateKeyGroup.setFont(FontService.getNormalBoldFont());
		privateKeyGroup.setText(Descriptions.MerkleTreeKeyTab_2);

		Label privateLengthDescriptionLabel = new Label(privateKeyGroup, SWT.NONE);
		privateLengthDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		privateLengthDescriptionLabel.setText(Descriptions.MerkleTreeKeyTab_8);

		Label privateLengthLabel = new Label(privateKeyGroup, SWT.NONE);
		privateLengthLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		String cleanedPrivateKey = privateKey.replaceAll("|", "");
		privateLengthLabel.setText(Converter._numberToPrefix(cleanedPrivateKey.length() / 2));

		Label spacerLine2 = new Label(privateKeyGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		spacerLine2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 20, 1));

		// The private key is splitted to get the corrrect positions of
		// seed/leaves
		splittedPrivateKey = privateKey.split("\\|");

		indexSeedComposite = new Composite(privateKeyGroup, SWT.NONE);
		indexSeedComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 20, 1));
		indexSeedLayout = new GridLayout(20, true);
		indexSeedLayout.marginWidth = 0;
		indexSeedComposite.setLayout(indexSeedLayout);

		indexLabel = new Label(indexSeedComposite, SWT.NONE);
		indexLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 7, 1));
		indexLabel.setText(Descriptions.MerkleTreeKeyTab_3);

		indexText = new Text(indexSeedComposite, SWT.READ_ONLY);
		indexText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 1));
		indexText.setText(splittedPrivateKey[arrayCounter++]);

		Label spacer = new Label(indexSeedComposite, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		seedLabel = new Label(indexSeedComposite, SWT.NONE);
		seedLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 7, 1));
		seedLabel.setText(Descriptions.MerkleTreeKeyTab_4);

		seedText = new Text(indexSeedComposite, SWT.READ_ONLY);
		seedText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 1));
		seedText.setText(splittedPrivateKey[arrayCounter++]);

		if (mode != SUIT.MSS) {
			Label spacer2 = new Label(indexSeedComposite, SWT.NONE);
			spacer2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

			privatePublicSeedLabel = new Label(indexSeedComposite, SWT.NONE);
			privatePublicSeedLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 7, 1));

			publicSeedText = new Text(indexSeedComposite, SWT.READ_ONLY);
			publicSeedText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 10, 1));
			publicSeedText.setText(splittedPrivateKey[arrayCounter++]);
		}

		otsLabel = new Label(privateKeyGroup, SWT.NONE);
		otsLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));

		privateOTSSpinner = new Spinner(privateKeyGroup, SWT.NONE);
		privateOTSSpinner.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		privateOTSSpinner.setMinimum(0);
		privateOTSSpinner.setMaximum(merkle.getLeafCounter() - 1);

		otsLabel.setText(Descriptions.MerkleTreeKeyTab_7 + " " + privateOTSSpinner.getSelection() + "/" + (merkle.getLeafCounter() - 1));

		privateOTSKey = new Text(privateKeyGroup, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		privateOTSKey.setText(splittedPrivateKey[arrayCounter]);
		privateOTSKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 13, 1));

		privateOTSSpinner.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				privateOTSKey.setText(splittedPrivateKey[privateOTSSpinner.getSelection() + arrayCounter]);
				otsLabel.setText(Descriptions.MerkleTreeKeyTab_7 + " " + privateOTSSpinner.getSelection() + "/" + (merkle.getLeafCounter() - 1));
			}

		});

		// setting Strings according to mode
		switch (mode) {
		case XMSS:
			descLabel.setText(Descriptions.XMSS.Tab1_Head0);
			keyExplanation.setText(Descriptions.XMSS.MerkleTreeKeyTab_0);
			publicSeedLabel.setText(Descriptions.MerkleTreeKeyTab_5);
			privatePublicSeedLabel.setText(Descriptions.MerkleTreeKeyTab_5);
			seedLabel.setText(Descriptions.MerkleTreeKeyTab_6);
			break;
		case XMSS_MT:
			descLabel.setText(Descriptions.XMSS_MT.Tab1_Head0);
			keyExplanation.setText(Descriptions.XMSS_MT.MerkleTreeKeyTab_0);
			publicSeedLabel.setText(Descriptions.MerkleTreeKeyTab_5);
			privatePublicSeedLabel.setText(Descriptions.MerkleTreeKeyTab_5);
			seedLabel.setText(Descriptions.MerkleTreeKeyTab_6);
			break;
		case MSS:
			descLabel.setText(Descriptions.MSS.Tab1_Head0);
			keyExplanation.setText(Descriptions.MSS.MerkleTreeKeyTab_0);
			publicSeedLabel.setText(Descriptions.MerkleTreeKeyTab_4);
			seedLabel.setText(Descriptions.MerkleTreeKeyTab_4);
		default:
			break;
		}

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of non-composite SWT components
		// this subclass is a composite
	}

	/**
	 * Force the private key to update its index, best called when a signature just got created
	 */
	public void updateIndexText() {
		indexText.setText(String.valueOf(merkle.getKeyIndex()));
	}

}
