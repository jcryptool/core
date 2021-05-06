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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

import de.flexiprovider.api.parameters.AlgorithmParameterSpec;

public class AlgorithmWizard extends Wizard {
    private AlgorithmIntroductionPage introPage;
    private AlgorithmParameterWizardPage algorithmParameterPage;
    private boolean canFinish = true;
    // private boolean useParamGenParameterSpec = false;

    private String algorithmName;
    private AlgorithmParameterSpec algorithmParameters;
    private IMetaAlgorithm algorithm;
    private IMetaSpec spec;

    public AlgorithmWizard(IMetaAlgorithm algorithm) {
        setWindowTitle(Messages.AlgorithmWizard_0);
        this.algorithm = algorithm;
        spec = AlgorithmsXMLManager.getInstance().getParameterSpec(algorithm.getParameterSpecClassName());
        // if (Reflector.getInstance().getDefaultParamSpecs(spec) == null) {
        // // no default constructor -> use generator
        // spec =
        // AlgorithmsXMLManager.getInstance().getParameterSpec(algorithm.getParamGenParameterSpecClassName());
        // useParamGenParameterSpec = true;
        // }
    }

    @Override
	public void addPages() {
        introPage = new AlgorithmIntroductionPage(algorithm, this, (algorithm.getParameterGeneratorClassName() != null));
        addPage(introPage);
        algorithmParameterPage = new AlgorithmParameterWizardPage(algorithm, spec, this);
        addPage(algorithmParameterPage);
    }

    protected Object[] getAlgorithmParameterValues() {
        return algorithmParameterPage.getValues();
    }

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

    public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameters) {
        this.algorithmParameters = algorithmParameters;
    }

    @Override
	public boolean performFinish() {
        algorithmName = algorithm.getName();
        if (introPage.useDefaultValues()) {
            LogUtil.logInfo("HAS PARAM GEN: " + (algorithm.getParameterGeneratorClassName() != null)); //$NON-NLS-1$
            LogUtil.logInfo("default values"); //$NON-NLS-1$
            if (algorithm.getParameterGeneratorClassName() != null) {
                setAlgorithmParameterSpec(Reflector.getInstance().generateDefaultParameterSpec(algorithm));
            } else {
                setAlgorithmParameterSpec(Reflector.getInstance().getDefaultParamSpecs(spec));
            }
            if(algorithmParameters == null)
            	return false;
            LogUtil.logInfo("Algorithm: " + algorithmName);
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
        }
        LogUtil.logInfo("APS: " + algorithmParameters); //$NON-NLS-1$
        return true;
        // }
    }

    public AlgorithmDescriptor getDescriptor() {
        return new AlgorithmDescriptor(algorithmName, algorithm.getType(), algorithmParameters);
    }

}
