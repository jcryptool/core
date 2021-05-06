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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;

public class AlgorithmIntroductionPage extends WizardPage implements Listener {
    private AlgorithmWizard wizard;
    private Group parametersGroup;
    private Button defaultRadioButton;
    // private Button generateRadioButton;
    private Button customRadioButton;
    private boolean useDefaultValues = true;

    // private boolean paramGenExists = false;

    public AlgorithmIntroductionPage(IMetaAlgorithm algorithm, AlgorithmWizard wizard, boolean paramGenExists) {
        super("AlgorithmIntroductionPage", algorithm.getName(), null); //$NON-NLS-1$
        if (algorithm.getOID() != null) {
            setTitle(algorithm.getName() + " (OID: " + algorithm.getOID().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.wizard = wizard;
        // this.paramGenExists = paramGenExists;
        setDescription(Messages.AlgorithmIntroductionPage_0);
    }

    public boolean useDefaultValues() {
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
        }
    }

    private void registerListeners() {
        defaultRadioButton.addListener(SWT.Selection, this);
        customRadioButton.addListener(SWT.Selection, this);
        // if (generateRadioButton != null) {
        // generateRadioButton.addListener(SWT.Selection, this);
        // }
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
        createParametersGroup(pageComposite);

        defaultRadioButton.setSelection(true);
        registerListeners();
        pageComposite.setSize(new Point(300, 200));
        pageComposite.setLayout(new GridLayout());
        setControl(pageComposite);
        setPageComplete(false);
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
        // GridData gridData8 = new GridData();
        // gridData8.horizontalAlignment = GridData.FILL;
        // gridData8.grabExcessHorizontalSpace = true;
        // gridData8.grabExcessVerticalSpace = true;
        // gridData8.verticalAlignment = GridData.FILL;
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = GridData.FILL;
        gridData5.grabExcessHorizontalSpace = true;
        parametersGroup = new Group(parent, SWT.NONE);
        parametersGroup.setLayout(new GridLayout());
        parametersGroup.setLayoutData(gridData5);
        parametersGroup.setText(Messages.AlgorithmIntroductionPage_1);
        defaultRadioButton = new Button(parametersGroup, SWT.RADIO);
        defaultRadioButton.setText(Messages.AlgorithmIntroductionPage_2);
        defaultRadioButton.setLayoutData(gridData6);
        // if (paramGenExists) {
        // generateRadioButton = new Button(parametersGroup, SWT.RADIO);
        // generateRadioButton.setText("Generate parameters");
        // generateRadioButton.setLayoutData(gridData8);
        // }
        customRadioButton = new Button(parametersGroup, SWT.RADIO);
        customRadioButton.setText(Messages.AlgorithmIntroductionPage_3);
        customRadioButton.setLayoutData(gridData7);
    }

}
