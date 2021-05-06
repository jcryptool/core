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

import java.lang.reflect.InvocationTargetException;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.AlgorithmParameterWizardPage;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.AlgorithmWizard;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.BlockCipherDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaKeyGenerator;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaMode;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

import de.flexiprovider.api.keys.SecretKey;
import de.flexiprovider.api.keys.SecretKeyGenerator;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.common.mode.ModeParameterGenerator;
import de.flexiprovider.common.mode.ModeParameterSpec;

public class BlockCipherWizard extends AlgorithmWizard implements IBlockCipherWizardListener {
    private BlockCipherIntroductionPage initPage;
    private AlgorithmParameterWizardPage algoPage;
    private ModeParameterSpecPage modePage;
    private boolean canFinish = true;

    private IMetaAlgorithm algorithm;
    private IMetaSpec spec;
    private IMetaMode mode;
    private SecretKey dummyKey;
    private boolean isForMac = false;
    private String macMode;

    private String algorithmName;
    private String modeID;
    private String paddingID;
    private ModeParameterSpec modeParameters;
    private AlgorithmParameterSpec algorithmParameters;

    public BlockCipherWizard(IMetaAlgorithm algorithm) {
        super(algorithm);
        setWindowTitle(Messages.BlockCipherWizard_0);
        this.algorithm = algorithm;
        spec = AlgorithmsXMLManager.getInstance().getParameterSpec(algorithm.getParameterSpecClassName());
    }

    public BlockCipherWizard(IMetaAlgorithm algorithm, String mode) {
        super(algorithm);
        setWindowTitle(Messages.BlockCipherWizard_1);
        isForMac = true;
        macMode = mode;
        this.algorithm = algorithm;
        spec = AlgorithmsXMLManager.getInstance().getParameterSpec(algorithm.getParameterSpecClassName());
    }

    protected boolean isForMac() {
        return isForMac;
    }

    protected String getMacMode() {
        return macMode;
    }

    protected boolean hasAlgorithmParameterSpecPage() {
        return (algoPage != null);
    }

    protected Object[] getModeParameterValues() {
        return modePage.getValues();
    }

    protected void setModeBlockSize(int modeBlockSize) {
        modePage.setModeBlockSize(modeBlockSize);
    }

    protected IMetaAlgorithm getAlgorithm() {
        return algorithm;
    }

    protected SecretKey getDummyKey() {
        try {
            LogUtil.logInfo("generating dummy key"); //$NON-NLS-1$
            IMetaKeyGenerator secGen = AlgorithmsXMLManager.getInstance().getSecretKeyGenerator(algorithm.getName());
            SecretKeyGenerator gen = (SecretKeyGenerator) Class.forName(secGen.getClassName()).newInstance();
            gen.init(algorithm.getDefaultBlockLength());
            dummyKey = gen.generateKey();
            LogUtil.logInfo("Key generated: " + dummyKey.getAlgorithm()); //$NON-NLS-1$
            return dummyKey;
        } catch (ClassNotFoundException e) {
            LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "ClassNotFoundException while creating the dummy key", e, false);
        } catch (InstantiationException e) {
            LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InstantiationException while creating the dummy key", e, false);
        } catch (IllegalAccessException e) {
            LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalAccessException while creating the dummy key", e, false);
        }
        return null;
    }

    @Override
	public void addPages() {
        initPage = new BlockCipherIntroductionPage(algorithm, this);
        initPage.addBlockCipherWizardListener(this);
        setMode(initPage.getMode());
        addPage(initPage);
        if (spec != null) {
            LogUtil.logInfo("Adding the AlgorithmParameterSpecPage"); //$NON-NLS-1$
            algoPage = new AlgorithmParameterWizardPage(algorithm, spec, null);
            addPage(algoPage);
            modePage = new ModeParameterSpecPage(algorithm, mode, true, this);
        } else {
            modePage = new ModeParameterSpecPage(algorithm, mode, false, this);
        }
        addPage(modePage);
    }

    @Override
	public void setMode(IMetaMode mode) {
        this.mode = mode;
        // if (modePage != null) {
        // modePage.setMode(this.mode);
        // }
    }

    @Override
	public void setPadding(String padding) {
        this.paddingID = padding;
    }

    @Override
	public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameters) {
        this.algorithmParameters = algorithmParameters;
    }

    @Override
	public void setModeParameterSpec(ModeParameterSpec modeParameters) {
        this.modeParameters = modeParameters;
    }

    @Override
	protected void setCanFinish(boolean value) {
        canFinish = value;
        if (getContainer() != null) {
            getContainer().updateButtons();
        }
    }

    @Override
	protected Object[] getAlgorithmParameterValues() {
        return algoPage.getValues();
    }

    @Override
	public boolean canFinish() {
        return canFinish;
    }

    @Override
	public boolean performFinish() {
        algorithmName = algorithm.getName();
        LogUtil.logInfo("Algorithm: " + algorithm.getName()); //$NON-NLS-1$
        modeID = mode.getID();
        LogUtil.logInfo("Mode: " + mode.getID()); //$NON-NLS-1$
        paddingID = initPage.getPadding();
        LogUtil.logInfo("Padding: " + paddingID); //$NON-NLS-1$
        if (initPage.useDefaultValues()) {
            if (algoPage != null) {
                setAlgorithmParameterSpec(Reflector.getInstance().getDefaultParamSpecs(spec));
                LogUtil.logInfo("APS: " + algorithmParameters); //$NON-NLS-1$
            }
            setModeParameterSpec(getDefaultModeParamSpecs());
            LogUtil.logInfo("MPS: " + modeParameters); //$NON-NLS-1$
            return true;
        } else {
            if (algoPage != null) {
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
                LogUtil.logInfo("APS: " + algorithmParameters); //$NON-NLS-1$
            }
            Object[] params = getModeParameterValues();
            setModeParameterSpec(Reflector.getInstance().instantiateModeParameterSpec((byte[]) params[0]));
            LogUtil.logInfo("MPS: " + modeParameters); //$NON-NLS-1$
            return true;
        }
    }

    @Override
	public AlgorithmDescriptor getDescriptor() {
        return new BlockCipherDescriptor(algorithmName, modeID, paddingID, modeParameters, algorithmParameters);
    }

    private ModeParameterSpec getDefaultModeParamSpecs() {
        ModeParameterGenerator generator = new ModeParameterGenerator();
        generator.init(algorithm.getDefaultBlockLength() / 8, FlexiProviderAlgorithmsPlugin.getSecureRandom());
        return (ModeParameterSpec) generator.generateParameters();
    }

}
