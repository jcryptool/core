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
package org.jcryptool.crypto.flexiprovider.algorithms.listeners;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.crypto.flexiprovider.algorithms.FlexiProviderAlgorithmsPlugin;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;

public class NewOperationManager {
	private static NewOperationManager instance;

	private static List<INewOperationListener> listeners = new ArrayList<INewOperationListener>();

	private NewOperationManager() {
		loadExtensions();
	}

	public synchronized static NewOperationManager getInstance() {
		if (instance==null) instance = new NewOperationManager();
		return instance;
	}

	public void fireNewOperation(AlgorithmDescriptor descriptor) {
		for (INewOperationListener listener : listeners) {
			listener.newOperation(descriptor);
		}
	}

	private static void loadExtensions() {
		LogUtil.logInfo("loading extensions"); //$NON-NLS-1$
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "newOperation"); //$NON-NLS-1$
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension.getConfigurationElements();
			for (IConfigurationElement configElement : configElements) {
				try {
					INewOperationListener newListener = (INewOperationListener)configElement.createExecutableExtension("listenerClass"); //$NON-NLS-1$
					listeners.add(newListener);
				} catch (CoreException e) {
				    LogUtil.logError(FlexiProviderAlgorithmsPlugin.PLUGIN_ID, "CoreException while creating a new INewOperationListener", e, false); //$NON-NLS-1$
				}
			}
		}
	}

}
