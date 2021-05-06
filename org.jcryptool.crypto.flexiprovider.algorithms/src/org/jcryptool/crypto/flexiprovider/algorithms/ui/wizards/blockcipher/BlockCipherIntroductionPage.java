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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaPaddingScheme;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

public class BlockCipherIntroductionPage extends WizardPage implements Listener {
    private BlockCipherWizard wizard;
    private Group modePaddingGroup;
    private Label modeLabel;
    private Label paddingLabel;
    private Combo modeCombo;
    private Combo paddingCombo;
    private Group parametersGroup;
    private Button defaultRadioButton;
    private Button customRadioButton;

    private IMetaAlgorithm metaAlgorithm;
    private boolean useDefaultValues = true;

    private List<IBlockCipherWizardListener> listeners = new ArrayList<IBlockCipherWizardListener>();

    protected BlockCipherIntroductionPage(IMetaAlgorithm algorithm, BlockCipherWizard wizard) {
        super("BlockCipherIntroductionPage", algorithm.getName(), null); //$NON-NLS-1$
        if (algorithm.getOID() != null) {
            setTitle(algorithm.getName() + " (OID: " + algorithm.getOID().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.wizard = wizard;
        setDescription(Messages.BlockCipherIntroductionPage_0);
        metaAlgorithm = algorithm;
    }

    protected IMetaMode getMode() {
        if (modeCombo != null && !modeCombo.getText().equals("")) { //$NON-NLS-1$
            return AlgorithmsXMLManager.getInstance().getMode(modeCombo.getText());
        } else {
            return AlgorithmsXMLManager.getInstance().getModeForID("CBC"); //$NON-NLS-1$
        }
    }

    protected String getPadding() {
        if (paddingCombo != null && !paddingCombo.getText().equals("")) { //$NON-NLS-1$
            return AlgorithmsXMLManager.getInstance().getPaddingScheme(paddingCombo.getText()).getID();
        } else {
            // fallback default. should never ever be actually returned.
            return "PKCS5Padding"; //$NON-NLS-1$
        }

    }

    protected boolean useDefaultValues() {
        return useDefaultValues;
    }

    @Override
	public void handleEvent(Event event) {
        if (event.widget.equals(defaultRadioButton)) {
            LogUtil.logInfo("default"); //$NON-NLS-1$
            setPageComplete(false);
            useDefaultValues = true;
            wizard.setCanFinish(true);

        } else if (event.widget.equals(customRadioButton)) {
            LogUtil.logInfo("custom"); //$NON-NLS-1$
            wizard.setCanFinish(false);
            useDefaultValues = false;
            setPageComplete(true);
            // canFlipToNextPage();
        } else if (event.widget.equals(modeCombo)) {
            Iterator<IBlockCipherWizardListener> it = listeners.iterator();
            while (it.hasNext()) {
                it.next().setMode(AlgorithmsXMLManager.getInstance().getMode(modeCombo.getText()));
            }
        }
    }

    @Override
	public void createControl(Composite parent) {
        LogUtil.logInfo("creating control"); //$NON-NLS-1$
        Composite pageComposite = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        pageComposite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        pageComposite.setLayoutData(gridData);
        createModePaddingGroup(pageComposite);
        createParametersGroup(pageComposite);

        initModeCombo(metaAlgorithm.getName());
        initPaddingCombo();

        defaultRadioButton.setSelection(true);
        registerListeners();
        pageComposite.setSize(new Point(300, 200));
        pageComposite.setLayout(new GridLayout());
        setControl(pageComposite);
        setPageComplete(false);
    }

    private void registerListeners() {
        modeCombo.addListener(SWT.Selection, this);
        defaultRadioButton.addListener(SWT.Selection, this);
        customRadioButton.addListener(SWT.Selection, this);
    }

    private void initModeCombo(String algorithm) {
        List<IMetaMode> modes = AlgorithmsXMLManager.getInstance().getModes();
        Collections.sort(modes);
        Iterator<IMetaMode> it = modes.iterator();
        while (it.hasNext()) {
            modeCombo.add(it.next().getDescription());
        }
        if (algorithm.endsWith("CBC") || algorithm.endsWith("ECB") || algorithm.endsWith("CFB") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                || algorithm.endsWith("OFB") || algorithm.endsWith("CTR")) { //$NON-NLS-1$ //$NON-NLS-2$
            modeCombo.setText(AlgorithmsXMLManager.getInstance().getModeForID(
                    algorithm.substring(algorithm.length() - 3, algorithm.length())).getDescription());
            modeCombo.setEnabled(false);
        } else if (wizard.isForMac()) {
            modeCombo.setText(AlgorithmsXMLManager.getInstance().getModeForID(wizard.getMacMode()).getDescription());
            modeCombo.setEnabled(false);
        } else {
            modeCombo.select(0);
            Iterator<IBlockCipherWizardListener> it2 = listeners.iterator();
            while (it2.hasNext()) {
                it2.next().setMode(AlgorithmsXMLManager.getInstance().getMode(modeCombo.getText()));
            }
        }
    }

    private void initPaddingCombo() {
        List<IMetaPaddingScheme> paddings = AlgorithmsXMLManager.getInstance().getPaddingSchemes();
        Collections.sort(paddings);

        Iterator<IMetaPaddingScheme> it = paddings.iterator();
        while (it.hasNext()) {
            paddingCombo.add(it.next().getPaddingSchemeName());
        }
        // select pkcs#5 padding as default
        paddingCombo.select(2);
    }

    protected void addBlockCipherWizardListener(IBlockCipherWizardListener listener) {
        listeners.add(listener);
    }

    /**
     * This method initializes modePaddingGroup
     *
     */
    private void createModePaddingGroup(Composite parent) {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = GridData.END;
        GridData gridData11 = new GridData();
        gridData11.horizontalAlignment = GridData.FILL;
        gridData11.grabExcessVerticalSpace = true;
        gridData11.verticalAlignment = GridData.END;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = GridData.FILL;
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.CENTER;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.makeColumnsEqualWidth = true;
        modePaddingGroup = new Group(parent, SWT.NONE);
        modePaddingGroup.setText(Messages.BlockCipherIntroductionPage_1);
        modePaddingGroup.setLayoutData(gridData1);
        modePaddingGroup.setLayout(gridLayout);
        modeLabel = new Label(modePaddingGroup, SWT.NONE);
        modeLabel.setText(Messages.BlockCipherIntroductionPage_3);
        modeLabel.setLayoutData(gridData11);
        paddingLabel = new Label(modePaddingGroup, SWT.NONE);
        paddingLabel.setText(Messages.BlockCipherIntroductionPage_4);
        paddingLabel.setLayoutData(gridData2);
        createModeCombo();
        createPaddingCombo();
    }

    /**
     * This method initializes modeCombo
     *
     */
    private void createModeCombo() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.grabExcessVerticalSpace = true;
        gridData3.verticalAlignment = GridData.CENTER;
        modeCombo = new Combo(modePaddingGroup, SWT.NONE | SWT.READ_ONLY);
        modeCombo.setLayoutData(gridData3);
    }

    /**
     * This method initializes paddingCombo
     *
     */
    private void createPaddingCombo() {
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = GridData.FILL;
        gridData4.grabExcessVerticalSpace = true;
        gridData4.verticalAlignment = GridData.CENTER;
        paddingCombo = new Combo(modePaddingGroup, SWT.NONE | SWT.READ_ONLY);
        paddingCombo.setLayoutData(gridData4);
    }

    /**
     * This method initializes parametersGroup
     *
     */
    private void createParametersGroup(Composite parent) {
        GridData gridData7 = new GridData();
        gridData7.grabExcessHorizontalSpace = true;
        gridData7.verticalAlignment = GridData.FILL;
        gridData7.grabExcessVerticalSpace = true;
        gridData7.horizontalAlignment = GridData.FILL;
        GridData gridData6 = new GridData();
        gridData6.horizontalAlignment = GridData.FILL;
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.grabExcessVerticalSpace = true;
        gridData6.verticalAlignment = GridData.FILL;
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL;
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.grabExcessVerticalSpace = true;
        gridData5.verticalAlignment = GridData.FILL;
        parametersGroup = new Group(parent, SWT.NONE);
        parametersGroup.setLayout(new GridLayout());
        parametersGroup.setLayoutData(gridData5);
        parametersGroup.setText(Messages.BlockCipherIntroductionPage_5);
        defaultRadioButton = new Button(parametersGroup, SWT.RADIO);
        defaultRadioButton.setText(Messages.BlockCipherIntroductionPage_6);
        defaultRadioButton.setLayoutData(gridData6);
        customRadioButton = new Button(parametersGroup, SWT.RADIO);
        customRadioButton.setText(Messages.BlockCipherIntroductionPage_7);
        customRadioButton.setLayoutData(gridData7);
    }

}
