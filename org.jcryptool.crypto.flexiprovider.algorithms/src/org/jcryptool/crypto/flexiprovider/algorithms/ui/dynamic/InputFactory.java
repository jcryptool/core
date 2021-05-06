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

import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaConstructor;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaParameter;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

public class InputFactory {

    private static InputFactory instance;

    private InputFactory() {
    }

    public synchronized static InputFactory getInstance() {
        if (instance == null)
            instance = new InputFactory();
        return instance;
    }

    public DynamicComposite createCurveParamsComposite(Composite parent, List<String> standardParameters) {
        DynamicComposite composite = new DynamicComposite(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        composite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        composite.setLayoutData(gridData);

        composite.addCurveParamsComposite(standardParameters);

        return composite;
    }

    public DynamicComposite createFixedModeParameterComposite(Composite parent) {
        DynamicComposite composite = new DynamicComposite(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        composite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        composite.setLayoutData(gridData);

        composite.addFixedModeParameterArea();

        return composite;
    }

    public DynamicComposite createVariableModeParameterComposite(Composite parent) {
        DynamicComposite composite = new DynamicComposite(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        composite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        composite.setLayoutData(gridData);

        composite.addVariableModeParameterArea();

        return composite;
    }

    public DynamicComposite createParameterComposite(Composite parent, IMetaSpec metaSpec) {
        DynamicComposite composite = new DynamicComposite(parent);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        gridLayout.makeColumnsEqualWidth = true;
        composite.setLayout(gridLayout);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        composite.setLayoutData(gridData);

        List<IMetaConstructor> cons = metaSpec.getMetaConstructors();
        if (cons.size() == 1) {
            IMetaConstructor constructor = cons.get(0);
            List<IMetaParameter> params = constructor.getParameters();
            for (IMetaParameter param : params) {
                if (param.getType().equals("int")) { //$NON-NLS-1$
                    if (param.getDescription().contains("...") && //$NON-NLS-1$
                            param.getDescription().contains(")")) { //$NON-NLS-1$
                        composite.addRangeIntInputArea(param.getDescription());
                    } else if (param.getDescription().contains(",") && //$NON-NLS-1$
                            param.getDescription().contains("bits)")) { //$NON-NLS-1$
                        composite.addDiscreteIntInputArea(param.getDescription());
                    } else {
                        composite.addUnspecfiedIntInputArea(param.getDescription());
                    }
                } else if (param.getType().endsWith("FlexiBigInt")) { //$NON-NLS-1$
                    composite.addFlexiBigIntInputArea(param.getDescription());
                } else if (param.getType().endsWith("String")) { //$NON-NLS-1$
                    composite.addStringInputArea(param.getDescription());
                } else if (param.getType().equals("byte[]")) { //$NON-NLS-1$
                    composite.addByteArrayInputArea(param.getDescription());
                } else if (param.getType().endsWith("Spec")) { //$NON-NLS-1$
                    IMetaSpec subSpec = AlgorithmsXMLManager.getInstance().getParameterSpec(param.getType());
                    if (subSpec != null) {
                        composite.addParameterSpecComposite(subSpec);
                    } else {
                        Label unspecifiedLabel = new Label(composite, SWT.NONE);
                        unspecifiedLabel.setText(NLS.bind(Messages.InputFactory_0,
                                new Object[] {param.getType(), param.getDescription()}));
                        addSeparator(composite);
                    }
                } else if (param.getType().endsWith("QuadraticIdeal")) { //$NON-NLS-1$
                    composite.addQuadraticIdealComposite();
                } else {
                    Label unspecifiedLabel = new Label(composite, SWT.NONE);
                    unspecifiedLabel.setText("Unspecified(" + param.getType() + "): Enter " + param.getDescription()); //$NON-NLS-1$ //$NON-NLS-2$
                    addSeparator(composite);
                }
            }
        }
        composite.setLayout(new GridLayout());
        return composite;
    }

    private void addSeparator(Composite parent) {
        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData separatorGridData = new GridData();
        separatorGridData.horizontalAlignment = GridData.FILL;
        separatorGridData.grabExcessHorizontalSpace = true;
        separator.setLayoutData(separatorGridData);
    }

}
