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
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.DynamicComposite;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.InputFactory;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;

public class AlgorithmParameterWizardPage extends WizardPage {
    private AlgorithmWizard wizard;
    private IMetaSpec spec;
    private DynamicComposite flexibleComp;

    public AlgorithmParameterWizardPage(IMetaAlgorithm algorithm, IMetaSpec spec, AlgorithmWizard wizard) {
        super("AlgorithmParameterWizardPage", algorithm.getName(), null); //$NON-NLS-1$
        if (algorithm.getOID() != null) {
            setTitle(algorithm.getName() + " (OID: " + algorithm.getOID().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        setDescription(Messages.AlgorithmParameterWizardPage_0);
        this.spec = spec;
        this.wizard = wizard;
    }

    @Override
	public void setPageComplete(boolean value) {
        super.setPageComplete(value);
        wizard.setCanFinish(true);
    }

    public Object[] getValues() {
        return flexibleComp.getValues();
    }

    @Override
	public void createControl(Composite parent) {
        LogUtil.logInfo("creating the dynamic layout"); //$NON-NLS-1$
        flexibleComp = InputFactory.getInstance().createParameterComposite(parent, spec);
        setControl(flexibleComp);
        if (wizard != null) {
            flexibleComp.setWizardPage(this);
        }
    }

}
