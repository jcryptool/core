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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.signature;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.DynamicComposite;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.InputFactory;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;

public class SignatureWizardPage extends WizardPage {
    private IMetaAlgorithm algorithm;
    private IMetaSpec spec;
    private boolean curveParams = false;
    private DynamicComposite flexibleComp;
    private SignatureWizard wizard;

    protected SignatureWizardPage(IMetaAlgorithm algorithm, IMetaSpec spec, SignatureWizard wizard, boolean curveParams) {
        super("SignatureWizardPage", algorithm.getName(), null); //$NON-NLS-1$
        if (algorithm.getOID() != null) {
            setTitle(algorithm.getName() + " (OID: " + algorithm.getOID().toString() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        setDescription(Messages.SignatureWizardPage_0);
        this.algorithm = algorithm;
        this.spec = spec;
        this.curveParams = curveParams;
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
        // GridData gridData = new GridData();
        // gridData.grabExcessVerticalSpace = true;
        // gridData.grabExcessHorizontalSpace = true;
        // gridData.horizontalAlignment = GridData.FILL;
        // // gridData.verticalAlignment = GridData.FILL;
        // GridLayout gridLayout = new GridLayout();
        // gridLayout.numColumns =1;
        // gridLayout.makeColumnsEqualWidth = true;
        // ScrolledComposite scroll = new ScrolledComposite(parent,
        // SWT.V_SCROLL);
        // // scroll.setExpandVertical(true);
        // scroll.setLayoutData(gridData);
        if (!curveParams) {
            flexibleComp = InputFactory.getInstance().createParameterComposite(parent, spec);
        } else {
            flexibleComp = InputFactory.getInstance().createCurveParamsComposite(parent, algorithm.getStandardParams());
        }
        // flexibleComp.setLayoutData(gridData);
        // flexibleComp.setLayout(gridLayout);
        // scroll.setContent(flexibleComp);
        // // scroll.setMinSize(flexibleComp.computeSize(SWT.DEFAULT,
        // SWT.DEFAULT));
        // flexibleComp.setSize(flexibleComp.computeSize(SWT.DEFAULT,
        // SWT.DEFAULT));
        setControl(flexibleComp);
        flexibleComp.setWizardPage(this);
    }

}
