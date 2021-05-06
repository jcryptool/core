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

import java.lang.reflect.InvocationTargetException;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.AlgorithmIntroductionPage;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.AlgorithmWizard;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.types.RegistryType;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.ec.parameters.CurveRegistry;

public class SignatureWizard extends AlgorithmWizard {
    private AlgorithmIntroductionPage introPage;
    private SignatureWizardPage page;
    private boolean canFinish = true;
    private boolean useCurveParams = false;
    // private boolean useParamGenParameterSpec = false;

    private String algorithmName;
    private AlgorithmParameterSpec algorithmParameters;
    private IMetaAlgorithm algorithm;
    private IMetaSpec spec;

    public SignatureWizard(IMetaAlgorithm algorithm) {
        super(algorithm);
        setWindowTitle(Messages.SignatureWizard_0);
        LogUtil.logInfo("Signature: " + algorithm.getName()); //$NON-NLS-1$
        this.algorithm = algorithm;
        if (algorithm.getStandardParams() != null && algorithm.getStandardParams().size() > 0) {
            useCurveParams = true;
            page = new SignatureWizardPage(algorithm, null, this, true);
        } else {
            // if (Reflector.getInstance().getDefaultParamSpecs(spec) == null) {
            // spec =
            // AlgorithmsXMLManager.getInstance().getParameterSpec(algorithm.getParamGenParameterSpecClassName());
            // useParamGenParameterSpec = true;
            // } else {
            spec = AlgorithmsXMLManager.getInstance().getParameterSpec(algorithm.getParameterSpecClassName());
            // }
            page = new SignatureWizardPage(algorithm, spec, this, false);
        }
    }

    @Override
	protected void setCanFinish(boolean value) {
        canFinish = value;
        if (getContainer() != null) {
            getContainer().updateButtons();
        }
    }

    @Override
	public boolean canFinish() {
        return canFinish;
    }

    @Override
	public void addPages() {
        introPage = new AlgorithmIntroductionPage(algorithm, this, (algorithm.getParameterGeneratorClassName() != null));
        addPage(introPage);
        addPage(page);
    }

    @Override
	public AlgorithmDescriptor getDescriptor() {
        return new AlgorithmDescriptor(algorithm.getName(), RegistryType.SIGNATURE, algorithmParameters);
    }

    @Override
	protected Object[] getAlgorithmParameterValues() {
        return page.getValues();
    }

    @Override
	public void setAlgorithmParameterSpec(AlgorithmParameterSpec spec) {
        algorithmParameters = spec;
    }

    @Override
	public boolean performFinish() {
        algorithmName = algorithm.getName();
        if (introPage.useDefaultValues()) {
            LogUtil.logInfo("default values"); //$NON-NLS-1$
            if (algorithm.getParameterGeneratorClassName() != null) {
                setAlgorithmParameterSpec(Reflector.getInstance().generateDefaultParameterSpec(algorithm));
            } else {
                if (useCurveParams) {
                    try {
                        setAlgorithmParameterSpec(Registry.getAlgParamSpec(CurveRegistry.getDefaultCurveParams(192)));
                    } catch (InvalidAlgorithmParameterException e) {
                        LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InvalidAlgorithmParameterException while accessing curve params", e, true);
                    }
                } else {
                    setAlgorithmParameterSpec(Reflector.getInstance().getDefaultParamSpecs(spec));
                }
            }
            LogUtil.logInfo("Algorithm: " + algorithmName);
            LogUtil.logInfo("APS.class: " + algorithmParameters.getClass().getName()); //$NON-NLS-1$
            return true;
        } else {
            // if (useParamGenParameterSpec) {
            // setAlgorithmParameterSpec(Reflector.getInstance().generateParameterSpec(algorithm,
            // spec.getClassName(), getAlgorithmParameterValues()));
            // } else {
            try {
                setAlgorithmParameterSpec(Reflector.getInstance().instantiateParameterSpec(spec.getClassName(),
                        getAlgorithmParameterValues()));
            } catch (SecurityException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "SecurityException while instantiating AlgorithmParameterSpecs", e, true);
            } catch (IllegalArgumentException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalArgumentException while instantiating AlgorithmParameterSpecs", e, true);
            } catch (ClassNotFoundException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "ClassNotFoundException while instantiating AlgorithmParameterSpecs", e, true);
            } catch (NoSuchMethodException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "NoSuchMethodException while instantiating AlgorithmParameterSpecs", e, true);
            } catch (InstantiationException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InstantiationException while instantiating AlgorithmParameterSpecs", e, true);
            } catch (IllegalAccessException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalAccessException while instantiating AlgorithmParameterSpecs", e, true);
            } catch (InvocationTargetException e) {
                LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InvocationTargetException while instantiating AlgorithmParameterSpecs", e, true);
            }
            // }

            LogUtil.logInfo("APS: " + algorithmParameters); //$NON-NLS-1$
            return true;
        }
    }

}
