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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.DynamicComposite;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.InputFactory;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;

public class ModeParameterSpecPage extends WizardPage {
    private BlockCipherWizard wizard;
    private IMetaAlgorithm algorithm;
    // private IMetaMode mode;
    // private int modeBlockSize = -1;
    private boolean withAlgParams = false;

    private DynamicComposite flexibleComp;

    protected ModeParameterSpecPage(IMetaAlgorithm algorithm, IMetaMode initialMode, boolean withAlgParams,
            BlockCipherWizard wizard) {
        super("ModeParameterSpecPage", algorithm.getName(), null); //$NON-NLS-1$
        if (algorithm.getOID() != null) {
            setTitle(algorithm.getName() + " (OID: " + algorithm.getOID().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        this.wizard = wizard;
        this.algorithm = algorithm;
        this.withAlgParams = withAlgParams;
        // setMode(initialMode);
        setDescription(Messages.ModeParameterSpecPage_0);
    }

    @Override
	public void createControl(Composite parent) {
        if (withAlgParams) {
            LogUtil.logInfo("fixed mode parameter composite"); //$NON-NLS-1$
            flexibleComp = InputFactory.getInstance().createFixedModeParameterComposite(parent);
        } else {
            LogUtil.logInfo("variable mode parameter composite"); //$NON-NLS-1$
            flexibleComp = InputFactory.getInstance().createVariableModeParameterComposite(parent);
            flexibleComp.setVariableModeAlgorithm(algorithm);
        }
        flexibleComp.setModeParameterSpecPage(this);
        setControl(flexibleComp);
        LogUtil.logInfo("finished"); //$NON-NLS-1$
    }

    public void setComplete(boolean value) {
        LogUtil.logInfo("setting complete: " + value); //$NON-NLS-1$
        setPageComplete(value);
        wizard.setCanFinish(value);
    }

    protected Object[] getValues() {
        return flexibleComp.getValues();
    }

    // protected void setMode(IMetaMode mode) {
    // this.mode = mode;
    // }

    protected void setModeBlockSize(int modeBlockSize) {
        // this.modeBlockSize = modeBlockSize;
        flexibleComp.setFixedModeSize(modeBlockSize);
    }

}
