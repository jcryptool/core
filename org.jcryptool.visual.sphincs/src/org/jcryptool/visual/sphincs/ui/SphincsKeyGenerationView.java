//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
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

	private GridData gd_grp_seed = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	private Text outputTextSeed;
	private Text outputTextBitmask;
	private StyledText outputTextKey;
	private PrivateKey prk;
	private PublicKey puk;
	private String seed;
	private String bitmasks;
	private String key;
	private Text txtInformationOutput;

	boolean firstTimeGenerated = true;

	private boolean changedKey = false;

	private static String stringsplit(String s, int numberOfChars) {

		int counter = 0;
		StringBuilder sb = new StringBuilder();
		String subStr;
		int diff = 0;
		// int stringLength = s.length();

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

	public SphincsKeyGenerationView(Composite parent, int style, ViewPart masterView, aSPHINCS256 sphincs) {
		super(parent, style);

		this.setLayout(new GridLayout(3, false));

		StyledText titleLabel = new StyledText(this, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		titleLabel.setText(SphincsDescriptions.SphincsDescription_titleBox);
		titleLabel.setFont(FontService.getHeaderFont());
		titleLabel.setCaret(null);

		StyledText titleDescription = new StyledText(this, SWT.MULTI|SWT.WRAP);
		titleDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		titleDescription.setText(SphincsDescriptions.SphincsDescription_titleDesc);
		titleDescription.setCaret(null);

		Group grp_seed = new Group(this, SWT.NONE);
		grp_seed.setLayout(new GridLayout(1, false));
		grp_seed.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grp_seed.setText(SphincsDescriptions.SphincsDescription_grp_seed);

		grp_seed.setLayoutData(gd_grp_seed);

		outputTextSeed = new Text(grp_seed, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridData gd_outputTextSeed = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_outputTextSeed.widthHint = 123;
		gd_outputTextSeed.heightHint = 100;
		outputTextSeed.setLayoutData(gd_outputTextSeed);

		Group grp_bitmask = new Group(this, SWT.NONE);
		grp_bitmask.setLayout(new GridLayout(1, false));

		grp_bitmask.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grp_bitmask.setText(SphincsDescriptions.SphincsDescription_grp_bitmask);

		outputTextBitmask = new Text(grp_bitmask, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridData gd_outputTextBitmask = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_outputTextBitmask.widthHint = 20;
		gd_outputTextBitmask.heightHint = 100;
		outputTextBitmask.setLayoutData(gd_outputTextBitmask);

		Group grp_key = new Group(this, SWT.NONE);
		grp_key.setLayout(new GridLayout(1, false));
		grp_key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grp_key.setText(SphincsDescriptions.SphincsDescription_grp_key);

		outputTextKey = new StyledText(grp_key, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		GridData gd_outpuTextKey = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_outpuTextKey.widthHint = 20;
		gd_outpuTextKey.heightHint = 100;
		outputTextKey.setLayoutData(gd_outpuTextKey);
		new Label(this, SWT.NONE);

		Button btnGenerateKeys = new Button(this, SWT.NONE);
		GridData gd_btnGenerateKeys = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnGenerateKeys.widthHint = 155;
		btnGenerateKeys.setLayoutData(gd_btnGenerateKeys);
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
				bitmasks = SphincsKeyGenerationView.stringsplit(prk.BitMasksToString(), 55);
				key = SphincsKeyGenerationView.stringsplit(puk.toString(), 65);

				outputTextSeed.setText(seed);
				outputTextKey.setText(key);
				outputTextBitmask.setText(bitmasks);
				txtInformationOutput.setText(SphincsDescriptions.SphincsDescription_keyInfo1 + " " + prk.getLength()
						+ " " + SphincsDescriptions.SphincsDescription_keyInfo2 + " " + puk.getLength() + " "
						+ SphincsDescriptions.SphincsDescription_keyInfo3);
				grp_seed.setText(SphincsDescriptions.SphincsDescription_grp_seed + " "
						+ SphincsDescriptions.SphincsDescription_bracket + prk.getSeed().length + " "
						+ SphincsDescriptions.SphincsDescription_bytes);
				grp_bitmask.setText(SphincsDescriptions.SphincsDescription_grp_bitmask + " "
						+ SphincsDescriptions.SphincsDescription_bracket
						+ SphincsKeyGenerationView.getBitmasksLength(puk.getBitMasks()) + " "
						+ SphincsDescriptions.SphincsDescription_bytes);
				grp_key.setText(SphincsDescriptions.SphincsDescription_grp_key + " "
						+ SphincsDescriptions.SphincsDescription_bracket + puk.getLength() + " "
						+ SphincsDescriptions.SphincsDescription_bytes);

				// Einfarben
				String[] pukArray;
				String[] bitmaskArray;
				pukArray = puk.toString().split("");
				bitmaskArray = prk.BitMasksToString().split("");
				int index = puk.toString().indexOf(bitmaskArray[0]);
				int endIndex = 1;

				for (int i = 0; i < bitmaskArray.length; i++) {

					if (pukArray[i].equals(bitmaskArray[i]) == true) {
						endIndex = endIndex + 1;
					}
				}
				outputTextKey.setStyleRange(new StyleRange(index, endIndex - index + 20, ColorConstants.red,
						outputTextKey.getBackground()));
				outputTextKey.setTopIndex(0);

			}
		});
		new Label(this, SWT.NONE);

		Label lblInformationOutput = new Label(this, SWT.NONE);
		lblInformationOutput.setAlignment(SWT.RIGHT);
		lblInformationOutput.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblInformationOutput.setText(SphincsDescriptions.SphincsDescription_lbl_information);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		txtInformationOutput = new Text(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_txtInformationOutput = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_txtInformationOutput.minimumHeight = 15;

		txtInformationOutput.setLayoutData(gd_txtInformationOutput);

	}

	public void setUpdatedKey(boolean updateKey) {
		this.changedKey = updateKey;
	}

	public boolean getUpdatedKey() {
		return changedKey;
	}
}
