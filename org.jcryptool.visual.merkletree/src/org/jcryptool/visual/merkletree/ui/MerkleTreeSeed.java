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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.ui.MerkleConst.SUIT;

/**
 * Class for the Composite with the Seed in Tabpage 1
 * @author Fabian Mayer
 * 
 *TODO: Key auslesen aus TXTBox
 *TODO: Key auto generieren
 *
 */
public class MerkleTreeSeed extends Composite {
	private MerkleTreeKeyPairs keyPairc;
	public byte[] seedarray;
	private MerkleTreeBitmask bitMask;
	Button createSeed;
	Label prng;
	Text textSeed;
	
	/**
	 * Create the composite.
	 * Including Seed content and KeyPairComposite
	 * @param parent
	 * @param style
	 */
	public MerkleTreeSeed(Composite parent, int style, SUIT verfahren, ViewPart masterView) {
		super(parent, SWT.NONE);

		this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));
		Composite testComp = new Composite(this,SWT.NONE | SWT.BORDER);
		testComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true,8,SWT.FILL));
		testComp.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN,true));

		//Seed Lable
		prng = new Label(testComp, SWT.NONE);
		prng.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
		prng.setText(Descriptions.Tab0_Head1);
		
		//textbox - seed
		textSeed = new Text(testComp, SWT.BORDER | SWT.CENTER);
		textSeed.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		//button
		createSeed = new Button(testComp, SWT.NONE);
		createSeed.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		createSeed.setText(Descriptions.Tab0_Button1);
		
		//for xmss|xmss^MT a Bitmask-Field is injected
		if(verfahren != SUIT.MSS){
			bitMask = new MerkleTreeBitmask(this, SWT.WRAP | SWT.BORDER | SWT.LEFT, verfahren, masterView);
			bitMask.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));
		}else{
			bitMask = null;
		}
		keyPairc = new MerkleTreeKeyPairs(this, SWT.WRAP | SWT.BORDER | SWT.LEFT, verfahren, masterView);
		keyPairc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, SWT.FILL));
		
		//TODO: sec.Rand.gen falsch Methode getSeed()!
		createSeed.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SecureRandom secureRandomGenerator = new SecureRandom();
				byte[] randomBytes = new byte[128];
				secureRandomGenerator.nextBytes(randomBytes);
				int seedByteCount = 10;
				byte[] seed = secureRandomGenerator.generateSeed(seedByteCount);
				long value = 0;
				for (int i = 0; i < seed.length; i++) {
					value += (seed[i] & 0xffL) << (8 * i);
				}
				if (value < 0L) {
					value *= -1;
				}
				textSeed.setText(String.valueOf(value));
			}
		});
		

		/* not Nessasary? seed = empty => seed = 0 ?
		 * XXX: Frage
		textSeed.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Control[] controls = keyPairc.getChildren();
				for (int i = 0; i < controls.length; i++) {
					if (controls[i] instanceof Button) {
						if (textSeed.getText().length() > 0) {
							((Button) controls[i]).setEnabled(true);
						} else {
							((Button) controls[i]).setEnabled(false);
						}
					}
				}
			}
		});
		*/
	}
	
	/***
	 * geter methode
	 * @return seed as bytearray
	 */
	public byte[] getSeed() {
		return textSeed.getText().getBytes();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
