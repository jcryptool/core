package org.jcryptool.visual.merkletree.ui;

import java.security.SecureRandom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.MerkleTreeView;
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
public class MerkleTreeSeed extends Composite {
	private MerkleTreeKeyPairs keyPairc;
	private MerkleTreeWParam wParamc;
	public byte[] seedarray;
	public byte[] bitmaskSeedarray;
	private MerkleTreeBitmask bitMask;
	private Button buttonCreateSeed;
	private Label randomgenerator;
	private Text textSeed;

	/**
	 * Create the composite. Including Seed content and KeyPairComposite
	 * 
	 * @param parent
	 * @param style
	 */
	public MerkleTreeSeed(Composite parent, int style, SUIT mode, ViewPart masterView) {
		super(parent, SWT.NONE);

		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		Composite testComp = new Composite(this, SWT.NONE | SWT.BORDER);
		testComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));
		testComp.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

		/*
		 * Seed Label
		 */
		randomgenerator = new Label(testComp, SWT.NONE);
		randomgenerator.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		randomgenerator.setText(Descriptions.Tab0_Head1);

		/*
		 * Textbox for seed initiates textbox with a seed
		 */
		textSeed = new Text(testComp, SWT.BORDER | SWT.CENTER);
		textSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		seedarray = generateNewSeed();
		textSeed.setText(Converter._byteToHex(seedarray));

		/*
		 * Button generate new Seed
		 */
		buttonCreateSeed = new Button(testComp, SWT.NONE);
		buttonCreateSeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		buttonCreateSeed.setText(Descriptions.Tab0_Button1);

		/*
		 * if XMSS or XMSS_MT is selected, also the Bitmask is requiered
		 * therefore the Bitmask Box is injected
		 */
		if (mode == SUIT.XMSS || mode == SUIT.XMSS_MT) {

			/*
			 * create Bitmask box
			 */
			bitMask = new MerkleTreeBitmask(this, SWT.WRAP | SWT.BORDER | SWT.LEFT, masterView);
			bitMask.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));
			// initial random Bitmask
			bitmaskSeedarray = generateNewSeed();
			bitMask.textSeed.setText(Converter._byteToHex(bitmaskSeedarray));

			/*
			 * add Listeners for button and for manual text changes
			 */
			bitMask.buttonCreateSeed.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					bitmaskSeedarray = generateNewSeed();
					bitMask.textSeed.setText(Converter._byteToHex(bitmaskSeedarray));
					((MerkleTreeView) masterView).updateElement();
				}
			});
		} else {
			bitMask = null;
		}

		/*
		 * Create sub-Frame for the Winternitz Parameter selection
		 */

		wParamc = new MerkleTreeWParam(this, SWT.WRAP | SWT.BORDER | SWT.LEFT, mode, masterView);
		wParamc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));

		/*
		 * Create sub-Frame for the Key Text/Create-Button
		 */
		keyPairc = new MerkleTreeKeyPairs(this, SWT.WRAP | SWT.BORDER | SWT.LEFT, mode, masterView);
		keyPairc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));

		buttonCreateSeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				seedarray = generateNewSeed();
				textSeed.setText(Converter._byteToHex(seedarray));
				((MerkleTreeView) masterView).updateElement();
			}
		});

	}

	/**
	 * @return seed
	 */
	public byte[] getSeed() {
		return seedarray;
	}

	/**
	 * @return bitmaskSeed
	 */
	public byte[] getBitmaskSeed() {
		if (bitMask != null)
			return bitmaskSeedarray;
		else
			return null;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * returns Key-Frame
	 * 
	 * @return MerkleTreeKeyPairs (Composite)
	 */
	public MerkleTreeKeyPairs getMTKP() {
		return keyPairc;
	}

	/**
	 * returns Bitmask-Frame
	 * 
	 * @return MerkleTreeBitmask (Composite)
	 */
	public MerkleTreeBitmask getMTB() {
		return bitMask;
	}

	/**
	 * generates a new random seed
	 * 
	 * @return random seed
	 */
	public byte[] generateNewSeed() {
		SecureRandom secureRandomGenerator = new SecureRandom();
		byte[] randomBytes = new byte[128];
		secureRandomGenerator.nextBytes(randomBytes);
		// set the seed length
		int seedByteCount = 16;
		byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
		return seed;
	}

	public int getWinternitzParameter() {
		return wParamc.getWinternitzParameter();
	}

}
