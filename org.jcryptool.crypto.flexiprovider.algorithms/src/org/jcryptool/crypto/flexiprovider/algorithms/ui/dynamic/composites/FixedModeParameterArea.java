// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.IInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher.ModeParameterSpecPage;

import de.flexiprovider.api.SecureRandom;
import de.flexiprovider.common.util.ByteUtils;

public class FixedModeParameterArea implements IInputArea, Listener {
    private static final List<String> hexValues = new ArrayList<String>(16);
    static {
        hexValues.add(0, "0"); //$NON-NLS-1$
        hexValues.add(1, "1"); //$NON-NLS-1$
        hexValues.add(2, "2"); //$NON-NLS-1$
        hexValues.add(3, "3"); //$NON-NLS-1$
        hexValues.add(4, "4"); //$NON-NLS-1$
        hexValues.add(5, "5"); //$NON-NLS-1$
        hexValues.add(6, "6"); //$NON-NLS-1$
        hexValues.add(7, "7"); //$NON-NLS-1$
        hexValues.add(8, "8"); //$NON-NLS-1$
        hexValues.add(9, "9"); //$NON-NLS-1$
        hexValues.add(10, "A"); //$NON-NLS-1$
        hexValues.add(11, "B"); //$NON-NLS-1$
        hexValues.add(12, "C"); //$NON-NLS-1$
        hexValues.add(13, "D"); //$NON-NLS-1$
        hexValues.add(14, "E"); //$NON-NLS-1$
        hexValues.add(15, "F"); //$NON-NLS-1$
    }

    /** Mode block size in bytes */
    private int size = -1;

    private Text ivText;
    private Button rndButton;
    private Label disclaimerLabel;

    private ModeParameterSpecPage page;

    public void setModeParameterSpecPage(ModeParameterSpecPage page) {
        LogUtil.logInfo("setting page"); //$NON-NLS-1$
        this.page = page;
        LogUtil.logInfo("page set"); //$NON-NLS-1$
    }

    public FixedModeParameterArea(Composite parent) {
        LogUtil.logInfo("setting layout"); //$NON-NLS-1$
        GridData gridData2 = new GridData();
        gridData2.horizontalSpan = 2;
        gridData2.grabExcessHorizontalSpace = true;
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 2;
        gridData1.grabExcessHorizontalSpace = true;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.CENTER;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        Label descriptionLabel = new Label(parent, SWT.NONE);
        descriptionLabel.setText(Messages.FixedModeParameterArea_0);
        descriptionLabel.setLayoutData(gridData1);
        ivText = new Text(parent, SWT.BORDER);
        ivText.setLayoutData(gridData);
        ivText.addListener(SWT.Modify, this);
        ivText.addVerifyListener(new VerifyListener() {

            @Override
			public void verifyText(VerifyEvent e) {
                if (page != null) {
                    page.setComplete(false);
                }
                if (e.character != SWT.BS && e.character != SWT.DEL) {
                    String origInput = ivText.getText();
                    LogUtil.logInfo("origInputLength: " + origInput.length()); //$NON-NLS-1$
                    LogUtil.logInfo("size*2         : " + size * 2); //$NON-NLS-1$
                    if (!codeManipulation) {
                        e.text = e.text.toUpperCase();
                        if (!hexValues.contains(e.text)) {
                            e.doit = false;
                            if (page != null) {
                                page.setComplete(true);
                            }
                        } else if ((origInput.length() + 1) > (size * 2)) {
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
        rndButton.setText(Messages.FixedModeParameterArea_1);
        rndButton.addListener(SWT.Selection, this);
        disclaimerLabel = new Label(parent, SWT.NONE);
        disclaimerLabel.setText(NLS.bind(Messages.FixedModeParameterArea_2, (size * 8)));
        disclaimerLabel.setLayoutData(gridData2);
        parent.setLayout(gridLayout);
        LogUtil.logInfo("layout set"); //$NON-NLS-1$
    }

    private boolean codeManipulation = false;

    @Override
	public void handleEvent(Event event) {
        if (event.widget.equals(rndButton)) {
            codeManipulation = true;
            if (size > 0) {
                String rndString = ByteUtils.toHexString(generateRandomValue(size));
                ivText.setText(rndString.toUpperCase());
                codeManipulation = false;
                if (page != null) {
                    page.setComplete(true);
                }
            }
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
        if (value instanceof Integer) {
            codeManipulation = true;
            size = (Integer) value;
            LogUtil.logInfo("setting size: " + size + "!"); //$NON-NLS-1$ //$NON-NLS-2$
            disclaimerLabel.setText(NLS.bind(Messages.FixedModeParameterArea_3, (size * 8)));
            ivText.setText(""); //$NON-NLS-1$
            codeManipulation = false;
        } else {
            LogUtil.logInfo("wrong type"); //$NON-NLS-1$
        }
    }

    /**
     * Generates a random value of the given byte length.<br>
     * Uses this plugin's standard secure random instance.
     *
     * @param length The byte length of the random value
     * @return The random value as a byte array
     */
    private byte[] generateRandomValue(int length) {
        byte[] result = new byte[length];
        SecureRandom sr = FlexiProviderAlgorithmsPlugin.getSecureRandom();
        sr.nextBytes(result);
        return result;
    }

}
