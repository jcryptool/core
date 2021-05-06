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
package org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.ByteArrayInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.CurveParamSelectionArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.DiscreteIntInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.FixedModeParameterArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.FlexiBigIntInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.QuadraticIdealComposite;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.RangeIntInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.StringInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.UnspecifiedIntInputArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.dynamic.composites.VariableModeParameterArea;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.blockcipher.ModeParameterSpecPage;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.reflect.Reflector;

import de.flexiprovider.api.parameters.AlgorithmParameterSpec;


public class DynamicComposite extends Composite implements IInputArea, IDynamicComposite {
	private List<IInputArea> inputAreas = new ArrayList<IInputArea>(1);
	private IMetaSpec spec = null;

	public DynamicComposite(Composite parent) {
		super(parent, SWT.BORDER);
	}

	public void addQuadraticIdealComposite() {
		inputAreas.add(new QuadraticIdealComposite(this));
	}

	public void addParameterSpecComposite(IMetaSpec spec) {
		DynamicComposite dynComp = InputFactory.getInstance().createParameterComposite(this, spec);
		dynComp.setSpec(spec);
		inputAreas.add(dynComp);
//		this.spec = spec;
	}

	public void addRangeIntInputArea(String description) {
		inputAreas.add(new RangeIntInputArea(this, description));
	}

	public void addUnspecfiedIntInputArea(String description) {
		inputAreas.add(new UnspecifiedIntInputArea(this, description));
	}

	public void addFlexiBigIntInputArea(String description) {
		inputAreas.add(new FlexiBigIntInputArea(this, description));
	}

	public void addByteArrayInputArea(String description) {
		inputAreas.add(new ByteArrayInputArea(this, description));
	}

	public void addStringInputArea(String description) {
		inputAreas.add(new StringInputArea(this, description));
	}

	public void addDiscreteIntInputArea(String description) {
		inputAreas.add(new DiscreteIntInputArea(this, description));
	}

	public void addCurveParamsComposite(List<String> standardParameters) {
		inputAreas.add(new CurveParamSelectionArea(this, standardParameters));
	}

	public void addFixedModeParameterArea() {
		inputAreas.add(new FixedModeParameterArea(this));
	}

	public void addVariableModeParameterArea() {
		inputAreas.add(new VariableModeParameterArea(this));
	}

	public void setWizardPage(WizardPage page) {
		Iterator<IInputArea> it = inputAreas.iterator();
		while (it.hasNext()) {
			IInputArea area = it.next();
			if (area instanceof IAlgorithmParameterInputArea) {
				((IAlgorithmParameterInputArea)area).setWizardPage(page);
			}
		}
	}

	public void setFixedModeSize(int size) {
		Iterator<IInputArea> it = inputAreas.iterator();
		while (it.hasNext()) {
			IInputArea area = it.next();
			if (area instanceof FixedModeParameterArea) {
				((FixedModeParameterArea)area).setValue(size);
			}
		}
	}

	public void setVariableModeAlgorithm(IMetaAlgorithm algorithm) {
		Iterator<IInputArea> it = inputAreas.iterator();
		while (it.hasNext()) {
			IInputArea area = it.next();
			if (area instanceof VariableModeParameterArea) {
				((VariableModeParameterArea)area).setValue(algorithm);
			}
		}
	}

	public void setModeParameterSpecPage(ModeParameterSpecPage page) {
		Iterator<IInputArea> it = inputAreas.iterator();
		while (it.hasNext()) {
			IInputArea area = it.next();
			if (area instanceof VariableModeParameterArea) {
				((VariableModeParameterArea)area).setModeParameterSpecPage(page);
			} else if (area instanceof FixedModeParameterArea) {
				((FixedModeParameterArea)area).setModeParameterSpecPage(page);
			}
		}
	}

	public IMetaSpec getSpec() {
		return spec;
	}

	public void setSpec(IMetaSpec spec) {
		this.spec = spec;
	}

	@Override
	public Object[] getValues() {
		Object[] values = new Object[inputAreas.size()];
		for (int i=0; i < values.length; i++) {
			values[i] = inputAreas.get(i).getValue();
		}
		return values;
	}

	@Override
	public Object getValue() {
		LogUtil.logInfo("returning value for DynamicComposite"); //$NON-NLS-1$
		if (spec != null) {
			try {
				AlgorithmParameterSpec algParamSpec = Reflector.getInstance().instantiateParameterSpec(spec.getClassName(), getValues());
				return algParamSpec;
			} catch (SecurityException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "SecurityException while instantiating an algorithm parameter spec", e, false);
			} catch (IllegalArgumentException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalArgumentException while instantiating an algorithm parameter spec", e, false);
			} catch (ClassNotFoundException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "ClassNotFoundException while instantiating an algorithm parameter spec", e, false);
			} catch (NoSuchMethodException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "NoSuchMethodException while instantiating an algorithm parameter spec", e, false);
			} catch (InstantiationException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InstantiationException while instantiating an algorithm parameter spec", e, false);
			} catch (IllegalAccessException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "IllegalAccessException while instantiating an algorithm parameter spec", e, false);
			} catch (InvocationTargetException e) {
			    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "InvocationTargetException while instantiating an algorithm parameter spec", e, false);
			}
		}
		return null;
	}

	@Override
	public void setValue(Object value) {
		// unused
	}

}
