//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.sphincs.SphincsDescriptions;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.sphincs.algorithm.PrivateKey;
import org.jcryptool.visual.sphincs.algorithm.PublicKey;
import org.jcryptool.visual.sphincs.algorithm.aSPHINCS256;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * Class for the Composite of Tabpage "Bitmasks and Key generation" It provides
 * a GUI interface to generate Sphincs keys and displays a Sphincs public key as
 * well as the used seed and bitmasks.
 * 
 * @author Klaus Kaiser
 *
 */
public class SphincsKeyGenerationView extends Composite {

	private StyledText outputTextKey;
	private StyledText titleLabel;
	private StyledText titleDescription;

	private Group seedGroup;
	private Group bitmaskGroup;
	private Group keyGroup;

	private Label lblInformationOutput;
	private Button btnGenerateKeys;

	private GridData seedGroupLayout;
	private GridData outputTextSeedLayout;
	private GridData outputTextBitmaskLayout;
	private GridData outputKeyLayout;
	private GridData btnGenerateKeysLayout;
	private GridData informationOutputLayout;

	private Text outputTextSeed;
	private Text outputTextBitmask;
	private Text informationOutput;

	private PrivateKey prk;
	private PublicKey puk;
	private String seed;
	private String bitmasks;
	private String key;

	private boolean firstTimeGenerated = true;
	private boolean changedKey = false;

	public SphincsKeyGenerationView(Composite parent, int style, ViewPart masterView, aSPHINCS256 sphincs) {
		super(parent, style);

		this.setLayout(new GridLayout(3, false));

		titleLabel = new StyledText(this, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		titleLabel.setText(SphincsDescriptions.SphincsDescription_titleBox);
		titleLabel.setFont(FontService.getHeaderFont());
		titleLabel.setCaret(null);

		titleDescription = new StyledText(this, SWT.MULTI | SWT.WRAP);
		titleDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		titleDescription.setText(SphincsDescriptions.SphincsDescription_titleDesc);
		titleDescription.setCaret(null);

		seedGroup = new Group(this, SWT.NONE);
		seedGroup.setLayout(new GridLayout(1, false));
		seedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		seedGroup.setText(SphincsDescriptions.SphincsDescription_grp_seed);
		seedGroupLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		seedGroup.setLayoutData(seedGroupLayout);

		outputTextSeed = new Text(seedGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		outputTextSeedLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		outputTextSeedLayout.widthHint = 123;
		outputTextSeedLayout.heightHint = 100;
		outputTextSeed.setLayoutData(outputTextSeedLayout);

		bitmaskGroup = new Group(this, SWT.NONE);
		bitmaskGroup.setLayout(new GridLayout(1, false));

		bitmaskGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		bitmaskGroup.setText(SphincsDescriptions.SphincsDescription_grp_bitmask);

		outputTextBitmask = new Text(bitmaskGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		outputTextBitmaskLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		outputTextBitmaskLayout.widthHint = 20;
		outputTextBitmaskLayout.heightHint = 100;
		outputTextBitmask.setLayoutData(outputTextBitmaskLayout);

		keyGroup = new Group(this, SWT.NONE);
		keyGroup.setLayout(new GridLayout(1, false));
		keyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		keyGroup.setText(SphincsDescriptions.SphincsDescription_grp_key);

		outputTextKey = new StyledText(keyGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		outputKeyLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		outputKeyLayout.widthHint = 20;
		outputKeyLayout.heightHint = 100;
		outputTextKey.setLayoutData(outputKeyLayout);

		new Label(this, SWT.NONE); // spacing label

		btnGenerateKeys = new Button(this, SWT.NONE);
		btnGenerateKeysLayout = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		btnGenerateKeysLayout.widthHint = 155;
		btnGenerateKeys.setLayoutData(btnGenerateKeysLayout);
		btnGenerateKeys.setText(SphincsDescriptions.SphincsDescription_btn_Generation);

		btnGenerateKeys.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (firstTimeGenerated == true) {
					setUpdatedKey(false);
					firstTimeGenerated = false;
					btnGenerateKeys.setText(SphincsDescriptions.SphincsDescription_btn_renewKey);
				} else {
					setUpdatedKey(true);
				}

				sphincs.generateKeys();
				puk = sphincs.getPublicKey();
				prk = sphincs.getPrivateKey();

				seed = prk.getSeedToString();
				bitmasks = SphincsKeyGenerationView.stringSplit(prk.BitMasksToString(), 55);
				key = SphincsKeyGenerationView.stringSplit(puk.toString(), 65);

				outputTextSeed.setText(seed);
				outputTextKey.setText(key);
				outputTextBitmask.setText(bitmasks);
				informationOutput.setText(SphincsDescriptions.SphincsDescription_keyInfo1 + " " + prk.getLength() + " "
						+ SphincsDescriptions.SphincsDescription_keyInfo2 + " " + puk.getLength() + " "
						+ SphincsDescriptions.SphincsDescription_keyInfo3);
				seedGroup.setText(SphincsDescriptions.SphincsDescription_grp_seed + " "
						+ SphincsDescriptions.SphincsDescription_bracket + prk.getSeed().length + " "
						+ SphincsDescriptions.SphincsDescription_bytes);
				bitmaskGroup.setText(SphincsDescriptions.SphincsDescription_grp_bitmask + " "
						+ SphincsDescriptions.SphincsDescription_bracket
						+ SphincsKeyGenerationView.getBitmasksLength(puk.getBitMasks()) + " "
						+ SphincsDescriptions.SphincsDescription_bytes);
				keyGroup.setText(SphincsDescriptions.SphincsDescription_grp_key + " "
						+ SphincsDescriptions.SphincsDescription_bracket + puk.getLength() + " "
						+ SphincsDescriptions.SphincsDescription_bytes);
			}
		});

		new Label(this, SWT.NONE); // spacing label for new line in grid

		lblInformationOutput = new Label(this, SWT.NONE);
		lblInformationOutput.setAlignment(SWT.RIGHT);
		lblInformationOutput.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblInformationOutput.setText(SphincsDescriptions.SphincsDescription_lbl_information);

		informationOutput = new Text(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		informationOutputLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		informationOutputLayout.minimumHeight = 15;

		informationOutput.setLayoutData(informationOutputLayout);

	}

	/**
	 * 
	 * Splits a string into equally large substrings
	 * 
	 * @param s             input strings
	 * @param numberOfChars expected length of the substring
	 * @return The substrings as newline seperated string
	 */
	private static String stringSplit(String s, int numberOfChars) {

		int counter = 0, diff = 0;
		StringBuilder sb = new StringBuilder();
		String subStr;

		while ((counter + numberOfChars) < s.length()) {
			subStr = s.substring(counter, counter + numberOfChars);
			sb.append(subStr);
			sb.append("\n");
			counter = counter + numberOfChars;
		}

		diff = (s.length() - counter);

		if (diff <= numberOfChars) {
			subStr = s.substring(counter, s.length() - 1);
			sb.append(subStr);
		}
		return sb.toString();
	}

	private static int getBitmasksLength(byte[][] bitmasks) {

		int length = 0;

		for (int i = 0; i < bitmasks.length; i++) {

			length = length + bitmasks[i].length;

		}
		return length;
	}

	public void setUpdatedKey(boolean updateKey) {
		this.changedKey = updateKey;
	}

	public boolean getUpdatedKey() {
		return changedKey;
	}
}
