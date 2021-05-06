// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.AbstractOperationsManager;
import org.jcryptool.core.operations.CommandInfo;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;

/**
 * The AlgorithmsRegistry class is a specific implementation of the base class
 * <i>org.jcryptool.core.operations.AbstractOperationsManager</i>
 * 
 * @author amro
 * @author t-kern
 * @author Holger Friedrich (support for Commands)
 * @version 0.6.2
 * 
 */
public class AlgorithmRegistry extends AbstractOperationsManager implements IExtensionChangeHandler {
    /** Contains all registered algorithms */
    private ArrayList<IAlgorithmDescriptor> algorithms = new ArrayList<IAlgorithmDescriptor>();

    /**
     * @see org.jcryptool.core.operations.AbstractOperationsManager#loadAlgorithmsExtensions()
     */
    public void loadAlgorithmsExtensions() {
        LogUtil.logInfo("Loading the Extensions to the algorithms ExtensionPoint"); //$NON-NLS-1$
        IExtensionRegistry registry = Platform.getExtensionRegistry();

        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_ALGORITHMS_CMD);

        IExtension[] extensions = extensionPoint.getExtensions();
        List<IExtension> ext = new ArrayList<IExtension>();
        for(IExtension extension: extensions) {
        	ext.add(extension);
        }

        extensions = ext.toArray(new IExtension[0]);
        
        for (int i = 0; i < extensions.length; i++) {
            IExtension extension = extensions[i];

            IConfigurationElement[] configElements = extension.getConfigurationElements();

            for (int j = 0; j < configElements.length; j++) {
                IConfigurationElement element = configElements[j];

                if (element.getName().equals(IOperationsConstants.TAG_ALGORITHM)
                		|| element.getName().equals(IOperationsConstants.TAG_ALGORITHM_CMD)) {

                    boolean isFlexiProviderAlgorithm = false;
                    if (element.getAttribute(IOperationsConstants.ATT_FLEXIPROVIDER) != null) {
                        isFlexiProviderAlgorithm = element.getAttribute(IOperationsConstants.ATT_FLEXIPROVIDER).equals(
                                "true") ? true : false; //$NON-NLS-1$
                    }

                    IAlgorithmDescriptor desc = new AlgorithmDescriptor(
                            element.getAttribute(IOperationsConstants.ATT_NAME),
                            element.getAttribute(IOperationsConstants.ATT_TYPE).equals("USER_DEFINED") ? element.getAttribute(IOperationsConstants.ATT_USER_TYPE) //$NON-NLS-1$
                                    : element.getAttribute(IOperationsConstants.ATT_TYPE),
                            element.getAttribute(IOperationsConstants.ATT_ID), extension.getUniqueIdentifier(),
                            element.getAttribute(IOperationsConstants.ATT_KEYLENGTHS),
                            element.getAttribute(IOperationsConstants.ATT_BLOCKLENGTHS),
                            element.getAttribute(IOperationsConstants.ATT_TOOLTIP), isFlexiProviderAlgorithm);

                    addAlgorithm(desc);
                }
            }
        }
    }

    /**
     * @see org.jcryptool.core.operations.AbstractOperationsManager#getShadowAlgorithmCommands()
     */
    public CommandInfo[] getShadowAlgorithmCommands() {
        CommandInfo[] commands = new CommandInfo[algorithms.size()];
        for (int i = 0; i < commands.length; i++) {
            commands[i] = algorithms.get(i).getCommand();
        }

        return commands;
    }

    /**
     * @see org.jcryptool.core.operations.AbstractOperationsManager#getAlgorithmType(org.jcryptool.core.operations.CommandInfo)
     */
    public String getAlgorithmType(CommandInfo command) {
        IHandler handler = command.getHandler();
        if(handler != null && handler instanceof ShadowAlgorithmHandler) {
        	return ((ShadowAlgorithmHandler)handler).getType();
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    /**
     * @see org.jcryptool.core.operations.AbstractOperationsManager#setCommandsEnabled(boolean)
     */
    public void setCommandsEnabled(boolean active) {
        Iterator<IAlgorithmDescriptor> it = algorithms.iterator();
        while (it.hasNext()) {
            it.next().getCommand().setEnabled(active);
        }
    }

    /**
     * Adds a descriptor.
     * 
     * @param desc The descriptor that will be added
     */
    private void addAlgorithm(IAlgorithmDescriptor desc) {
        synchronized (algorithms) {
            algorithms.add(desc);
        }
    }

    /**
     * Removes a descriptor.
     * 
     * @param desc The descriptor that will be removed
     */
    @SuppressWarnings("unused")
    private void removeAlgorithm(IAlgorithmDescriptor desc) {
        synchronized (algorithms) {
            algorithms.remove(desc);
        }
    }

    public void addExtension(IExtensionTracker tracker, IExtension extension) {
        // TODO check implementation in Eclipse
    }

    public void removeExtension(IExtension extension, Object[] objects) {
        // TODO check implementation in Eclipse
    }

}