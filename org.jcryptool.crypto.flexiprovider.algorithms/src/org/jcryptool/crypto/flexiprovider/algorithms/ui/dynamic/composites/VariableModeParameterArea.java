//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.IInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher.ModeParameterSpecPage;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;

import de.flexiprovider.api.SecureRandom;
import de.flexiprovider.common.util.ByteUtils;

public class VariableModeParameterArea implements IInputArea, Listener {
	private static final List<String> hexValues = new ArrayList<String>(16);
	static {
		hexValues.add(0, "0"); hexValues.add(1, "1"); hexValues.add(2, "2"); hexValues.add(3, "3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(4, "4"); hexValues.add(5, "5"); hexValues.add(6, "6"); hexValues.add(7, "7"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(8, "8"); hexValues.add(9, "9"); hexValues.add(10, "A"); hexValues.add(11, "B"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		hexValues.add(12, "C"); hexValues.add(13, "D"); hexValues.add(14, "E"); hexValues.add(15, "F"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	private IMetaAlgorithm algorithm;
	private Combo lengthCombo;
	private Text ivText;
	private Button rndButton;
	private boolean random = false;
	private int size = -1;
	private ModeParameterSpecPage page;

	public void setModeParameterSpecPage(ModeParameterSpecPage page) {
		LogUtil.logInfo("setting page"); //$NON-NLS-1$
		this.page = page;
	}

	public VariableModeParameterArea(Composite parent) {
		GridData gridData21 = new GridData();
		gridData21.horizontalSpan = 3;
		gridData21.verticalAlignment = GridData.CENTER;
		gridData21.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 4;
		GridData gridData2 = new GridData();
		gridData2.horizontalSpan = 2;
		gridData2.grabExcessHorizontalSpace = true;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalSpan = 3;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.CENTER;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		Label descriptionLabel = new Label(parent, SWT.NONE);
		descriptionLabel.setText(Messages.VariableModeParameterArea_0);
		descriptionLabel.setLayoutData(gridData1);
		new Label(parent, SWT.NONE);
		Label lengthLabel = new Label(parent, SWT.NONE);
		lengthLabel.setText(Messages.VariableModeParameterArea_1);
		parent.setLayout(gridLayout1);
		lengthCombo = new Combo(parent, SWT.NONE);
		lengthCombo.addListener(SWT.Selection, this);

		Label bitsLabel = new Label(parent, SWT.NONE);
		bitsLabel.setText(Messages.VariableModeParameterArea_2);
		new Label(parent, SWT.NONE);
		ivText = new Text(parent, SWT.BORDER);
		ivText.setLayoutData(gridData21);
		ivText.addListener(SWT.Modify, this);
		ivText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (page != null) {
					page.setComplete(false);
				}
				if (e.character != SWT.BS && e.character != SWT.DEL) {
					if (!random) {
						String origInput = ivText.getText();

						e.text = e.text.toUpperCase();
						if ( (origInput.length()) == (size*2) ) {
							if (page != null) {
								page.setComplete(true);
							}
						}
						if ( !hexValues.contains(e.text) ) {
							e.doit = false;
							if (page != null) {
								page.setComplete(true);
							}
						} else if ( (origInput.length()+1) > (size*2) ) {
							e.doit = false;
							if (page != null) {
								page.setComplete(true);
							}
						}
					}
				}
			}

		});
		rndButton = new Button(parent, SWT.NONE);
		rndButton.setText(Messages.VariableModeParameterArea_3);
		rndButton.addListener(SWT.Selection, this);
	}

	private void initLengthCombo() {
		List<Integer> list = algorithm.getBlockLengths();
		LogUtil.logInfo("list.size: " + list.size()); //$NON-NLS-1$
		if (list.size() > 0) {
			Collections.sort(list);
			Iterator<Integer> it = list.iterator();
			while (it.hasNext()) {
				lengthCombo.setText(String.valueOf(it.next()));
			}
			lengthCombo.select(0);
			size = Integer.valueOf(lengthCombo.getText())/8;
		} else {
			LogUtil.logInfo("default length: " + algorithm.getDefaultBlockLength()); //$NON-NLS-1$
			lengthCombo.setText(String.valueOf(algorithm.getDefaultBlockLength()));
			lengthCombo.select(0);
			size = Integer.valueOf(lengthCombo.getText())/8;
			lengthCombo.setEnabled(false);
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(rndButton)) {
			random = true;
			String rndString = ByteUtils.toHexString(generateRandomValue(size));
			LogUtil.logInfo("random " + size + ": " + rndString);			 //$NON-NLS-1$ //$NON-NLS-2$
			ivText.setText(rndString.toUpperCase());
			page.setComplete(true);
			random = false;
		} else if (event.widget.equals(lengthCombo)) {
			size = Integer.valueOf(lengthCombo.getText())/8;
			ivText.setText(""); //$NON-NLS-1$
		}
	}

	@Override
	public Object getValue() {
		if (ivText == null || ivText.getText().equals("")) { //$NON-NLS-1$
			return new byte[0];
		} else {
			return ByteUtils.fromHexString(ivText.getText());
		}
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof IMetaAlgorithm) {
			LogUtil.logInfo("setting the algorithm"); //$NON-NLS-1$
			this.algorithm = (IMetaAlgorithm)value;
			initLengthCombo();
		} else {
			LogUtil.logInfo("wrong type"); //$NON-NLS-1$
		}
	}

	/**
	 * Generates a random value of the given byte length.<br>
	 * Uses this plugin's standard secure random instance.
	 *
	 * @param length	The byte length of the random value
	 * @return			The random value as a byte array
	 */
	private byte[] generateRandomValue(int length) {
		byte[] result = new byte[length];
		SecureRandom sr = FlexiProviderAlgorithmsPlugin.getSecureRandom();
		sr.nextBytes(result);
		return result;
	}

}
