// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * To support lazy loading of the 'algorithms' extensions kind of a helper pattern is used. The helper is represented by
 * the ShadowAlgorithmAction class.
 * 
 * The class encapsulates the appropriate algorithm action. In its run-method it just calls the run-method of the
 * algorithm action. To guarantee lazy loading, the appropriate algorithm is loaded, the first time a run is invoked.
 * 
 * @author amro
 * @author t-kern
 * 
 */
public class ShadowAlgorithmAction extends Action {
    /** algorithm type */
    private String type;

    /**
     * action Id
     */
    private String algorithmActionID;

    /**
     * the appropriate algorithm action
     */
    private AbstractAlgorithmAction algorithmAction;

    /**
     * unique identifier of contribution
     */
    private String extensionID;

    /** The flag for FlexiProvider algorithms */
    private boolean isFlexiProviderAlgorithm;

    /**
     * Creates a new instance of ShadowAlgorithmAction with the given descriptor.
     * 
     * @param descriptor The descriptor with the details for this algorithm
     */
    public ShadowAlgorithmAction(IAlgorithmDescriptor descriptor) {
        super();
        setId(descriptor.getAlgorithmID());
        setText(descriptor.getName());
        if (descriptor.getToolTipText() != null)
            setToolTipText(descriptor.getToolTipText());
        this.type = descriptor.getType();
        this.extensionID = descriptor.getExtensionUID();
        this.algorithmActionID = descriptor.getAlgorithmID();
        setEnabled(false);
        this.isFlexiProviderAlgorithm = descriptor.isFlexiProviderAlgorithm();
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        // standard handling for everything not expressely specified above
        if (algorithmAction == null) {
            // create executable extension
            try {
                algorithmAction = createAlgorithmAction();
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                        "CoreException while creating an executable extension of an AlgorithmAction", //$NON-NLS-1$
                        e, false);
            }
            // set Action id
            algorithmAction.setId(algorithmActionID);
        }
        algorithmAction.run();
    }

    public void run(final IDataObject dataobject) {
        // standard handling for everything not expressely specified above
        if (algorithmAction == null) {
            // create executable extension
            try {
                algorithmAction = createAlgorithmAction();
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                        "CoreException while creating an executable extension of an AlgorithmAction", //$NON-NLS-1$
                        e, false);
            }
            // set Action id
            algorithmAction.setId(algorithmActionID);
        }

        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                if (dataobject instanceof ClassicDataObject)
                    if (((ClassicDataObject) dataobject).getInputStream() == null)
                        ((ClassicDataObject) dataobject).setInputStream(algorithmAction.getActiveEditorInputStream());
                    else if (dataobject instanceof ModernDataObject)
                        if (((ModernDataObject) dataobject).getInputStream() == null)
                            ((ModernDataObject) dataobject).setInputStream(algorithmAction.getActiveEditorInputStream());
            }
        });
        algorithmAction.run(dataobject);
    }

    /**
     * "Lazy-loads" the algorithm plug-in
     * 
     * @return The plug-in's implementation of AbstractAlgorithmAction
     * @throws CoreException
     */
    private AbstractAlgorithmAction createAlgorithmAction() throws CoreException {

        IExtensionRegistry registry = Platform.getExtensionRegistry();

        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_ALGORITHMS);

        AbstractAlgorithmAction action = null;

        IExtension extension = extensionPoint.getExtension(this.extensionID);

        IConfigurationElement[] configElements = extension.getConfigurationElements();

        for (int i = 0; i < configElements.length; i++) {
            IConfigurationElement element = configElements[i];

            if (element.getName().equals(IOperationsConstants.TAG_ALGORITHM)) {
                action = (AbstractAlgorithmAction) element
                        .createExecutableExtension(IOperationsConstants.ATT_ACTION_CLASS);
            }
        }
        return action;
    }

    /**
     * Getter for the algorithm action
     * 
     * @return an instance of AbstractAlgorithmAction
     */
    public AbstractAlgorithmAction getAlgorithmAction() {
        return algorithmAction;
    }

    /**
     * Getter for the algorithm's type
     * 
     * @return type as a string object
     */
    public String getType() {
        return type;
    }

    /**
     * Returns true for FlexiProvider algorithms
     * 
     * @return true for FlexiProvider algorithms
     */
    public boolean isFlexiProviderAlgorithm() {
        return isFlexiProviderAlgorithm;
    }
}
