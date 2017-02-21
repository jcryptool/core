// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.FlexiProviderPlugin;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaConstructor;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaParameter;
import org.jcryptool.crypto.flexiprovider.descriptors.reflect.interfaces.IMetaSpec;
import org.jcryptool.crypto.flexiprovider.xml.AlgorithmsXMLManager;

import de.flexiprovider.api.Registry;
import de.flexiprovider.api.exceptions.InvalidAlgorithmParameterException;
import de.flexiprovider.api.exceptions.NoSuchAlgorithmException;
import de.flexiprovider.api.parameters.AlgorithmParameterGenerator;
import de.flexiprovider.api.parameters.AlgorithmParameterSpec;
import de.flexiprovider.common.mode.ModeParameterSpec;

public class Reflector {
    private static Reflector instance;

    public synchronized static Reflector getInstance() {
        if (instance == null) {
            instance = new Reflector();
        }
        return instance;
    }

    private Reflector() {
    }

    public AlgorithmParameterSpec generateDefaultParameterSpec(final IMetaAlgorithm algorithm) {
        final List<String> names = algorithm.getNames();
        String useName = "-1"; //$NON-NLS-1$
        for (final String name : names) {
            try {
                Registry.getAlgParamGenerator(name);
                useName = name;
                break;
            } catch (final NoSuchAlgorithmException e) {
            }
        }
        try {
            final AlgorithmParameterGenerator generator = Registry.getAlgParamGenerator(useName);
            final AlgorithmParameterSpec spec = Registry.getAlgParamSpec(useName + "ParamGen"); //$NON-NLS-1$
            generator.init(spec, FlexiProviderPlugin.getSecureRandom());
            return generator.generateParameters();
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "NoSuchAlgorithmException while generating default parameters for " + algorithm.getName(), e, true); //$NON-NLS-1$
        } catch (final InvalidAlgorithmParameterException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "InvalidAlgorithmParameterException while generating default parameters for " //$NON-NLS-1$
                            + algorithm.getName(), e, true);
        }
        return null;
    }

    public AlgorithmParameterSpec generateParameterSpec(final IMetaAlgorithm algorithm, final String specClassName,
            final Object[] parameters) {
        final List<String> names = algorithm.getNames();
        String useName = "-1"; //$NON-NLS-1$
        for (final String name : names) {
            try {
                Registry.getAlgParamGenerator(name);
                useName = name;
                break;
            } catch (final NoSuchAlgorithmException e) {
            }
        }
        try {
            final AlgorithmParameterGenerator generator = Registry.getAlgParamGenerator(useName);
            generator.init(this.instantiateParameterSpec(specClassName, parameters),
                    FlexiProviderPlugin.getSecureRandom());
            return generator.generateParameters();
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final InvalidAlgorithmParameterException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final SecurityException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final IllegalArgumentException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final ClassNotFoundException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final NoSuchMethodException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final InstantiationException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final IllegalAccessException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final InvocationTargetException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        }

        return null;
    }

    private Class<?> getClass(final String parameter) {
        if (parameter.equals("int")) { //$NON-NLS-1$
            return int.class;
        }
        try {
            return Class.forName(parameter);
        } catch (final ClassNotFoundException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "ClassNotFoundException while trying to get a class for: " + parameter, e, true); //$NON-NLS-1$
        }
        return null;
    }

    public AlgorithmParameterSpec getDefaultParamSpecs(final IMetaSpec spec) {
        try {
            return Reflector.getInstance().instantiateParameterSpec(spec.getClassName(), null);
        } catch (final SecurityException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "SecurityException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        } catch (final IllegalArgumentException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "IllegalArgumentException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        } catch (final ClassNotFoundException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "ClassNotFoundException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        } catch (final NoSuchMethodException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "NoSuchMethodException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        } catch (final InstantiationException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "InstantiationException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        } catch (final IllegalAccessException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "IllegalAccessException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        } catch (final InvocationTargetException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID,
                    "InvocationTargetException while instantiating default AlgorithmParameterSpecs", e, true); //$NON-NLS-1$
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
	public Class[] getParameterTypes(final String parameterSpecClassName) {
        final IMetaSpec spec = AlgorithmsXMLManager.getInstance().getParameterSpec(parameterSpecClassName);
        final List<IMetaConstructor> constructors = spec.getMetaConstructors();
        Class[] classes = null;
        if (constructors.size() == 1) {
            final List<IMetaParameter> parameters = constructors.get(0).getParameters();
            classes = new Class[parameters.size()];
            for (int i = 0; i < classes.length; i++) {
                classes[i] = this.getClass(parameters.get(i).getType());
            }
        }
        return classes;
    }

    @SuppressWarnings("rawtypes")
	public Object instantiate(final String className, final Object[] parameters) throws ClassNotFoundException,
            SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        final Class<?> clazz = Class.forName(className);
        final Class[] parameterTypes = new Class[parameters.length];
        int counter = 0;
        for (final Object o : parameters) {
            if (o.getClass().equals(Integer.class)) {
                parameterTypes[counter] = int.class;
                counter++;
            } else {
                parameterTypes[counter] = o.getClass();
                counter++;
            }
        }
        final Constructor<?> constructor = clazz.getConstructor(parameterTypes);
        return constructor.newInstance(parameters);
    }

    public ModeParameterSpec instantiateModeParameterSpec(final byte[] iv) {
        try {
            final Class<?> tmp = Registry.getAlgParamSpecClass("Mode"); //$NON-NLS-1$
            return (ModeParameterSpec) tmp.getConstructor(new Class[] {byte[].class}).newInstance(new Object[] {iv});
        } catch (final NoSuchAlgorithmException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final SecurityException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final NoSuchMethodException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final IllegalArgumentException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final InstantiationException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final IllegalAccessException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        } catch (final InvocationTargetException e) {
            LogUtil.logError(FlexiProviderPlugin.PLUGIN_ID, e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
	public AlgorithmParameterSpec instantiateParameterSpec(final String specClassName, final Object[] parameters)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        final Class<?> clazz = Class.forName(specClassName);
        if (parameters != null) {
            final Class[] parameterTypes = new Class[parameters.length];
            int counter = 0;
            for (final Object o : parameters) {
                if (o.getClass().equals(Integer.class)) {
                    parameterTypes[counter] = int.class;
                    counter++;
                } else {
                    parameterTypes[counter] = o.getClass();
                    counter++;
                }
            }
            final Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            return (AlgorithmParameterSpec) constructor.newInstance(parameters);
        } else {
            return (AlgorithmParameterSpec) clazz.newInstance();
        }
    }

}
