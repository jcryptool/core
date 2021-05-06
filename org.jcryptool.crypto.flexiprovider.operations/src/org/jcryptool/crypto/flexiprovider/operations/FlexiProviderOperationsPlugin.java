// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.operations;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.operations.ui.listeners.IOperationChangedListener;
import org.jcryptool.crypto.flexiprovider.operations.ui.views.FlexiProviderOperationsView;
import org.jcryptool.crypto.flexiprovider.ui.nodes.TreeNode;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FlexiProviderOperationsPlugin extends AbstractUIPlugin {
    /** The plug-in ID. */
    public static final String PLUGIN_ID = "org.jcryptool.crypto.flexiprovider.operations"; //$NON-NLS-1$
	public static FlexiProviderOperationsView lastOperationsView;

	private boolean bubbleWasShown = false;	

    @Override
    public void start(BundleContext context) throws Exception {
    	super.start(context);
    	OperationsManager.getInstance().addOperationChangedListener(new IOperationChangedListener() {
			@Override
			public void update(TreeNode updated) {
				if (lastOperationsView != null) {
					lastOperationsView.update(updated);
				}
			}
			@Override
			public void removeOperation() {
				if (lastOperationsView != null) {
					lastOperationsView.removeOperation();
				}
			}
			@Override
			public void addOperation() {
				if (lastOperationsView != null) {
					lastOperationsView.addOperation();
				}
			}
			@Override
			public void addOperation(AlgorithmDescriptor descriptor) {
				IOperationChangedListener.super.addOperation(descriptor);

				IWorkbenchWindow wbwin = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				try {
					wbwin.getActivePage().showView("org.jcryptool.crypto.flexiprovider.operations.OperationsView"); //$NON-NLS-1$
				} catch (PartInitException e) {
					e.printStackTrace();
				}
				if (lastOperationsView != null) {
					lastOperationsView.addOperation();
				}
				if (lastOperationsView != null) {
					lastOperationsView.selectThis(descriptor);
					if(! bubbleWasShown) {
						lastOperationsView.showBubble(Messages.FlexiProviderOperationsPlugin_1);
						bubbleWasShown = true;
					}
				}
			}
		});
    }
    
}