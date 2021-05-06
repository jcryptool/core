// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.algorithm;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.operations.dataobject.modern.ModernDataObject;

/**
 * To support lazy loading of the 'algorithms' extensions kind of a helper pattern is used. The helper is represented by
 * the ShadowAlgorithmHandler class.
 * 
 * The class encapsulates the appropriate algorithm command handler. In its run-method it just calls the execute method of the
 * algorithm command handler. To guarantee lazy loading, the appropriate algorithm is loaded, the first time an execute is invoked.
 * 
 * @author amro
 * @author t-kern
 * @author Holger Friedrich (support for Commands, adapted from ShadowAlgorithmAction)
 * 
 */
public class ShadowAlgorithmHandler extends AbstractHandler {
    /** algorithm type */
    private String type;

    /**
     * action Id
     */
    private String algorithmActionID;

    /**
     * the appropriate algorithm command handler
     */
    private AbstractAlgorithmHandler algorithmHandler;

    /**
     * unique identifier of contribution
     */
    private String extensionID;

    /** The flag for FlexiProvider algorithms */
    private boolean isFlexiProviderAlgorithm;
    
    /** the text to use in menu, palette and tree entries */ 
    private String text;
    
    /** the tool tip text */
    private String toolTipText;

    /**
     * Creates a new instance of ShadowAlgorithmAction with the given descriptor.
     * 
     * @param descriptor The descriptor with the details for this algorithm
     */
    public ShadowAlgorithmHandler(IAlgorithmDescriptor descriptor) {
        super();
        // TODO do we need those?
        // setId(descriptor.getAlgorithmID());
        setText(descriptor.getName());
        if (descriptor.getToolTipText() != null)
            this.toolTipText = descriptor.getToolTipText();
        this.type = descriptor.getType();
        this.extensionID = descriptor.getExtensionUID();
        this.algorithmActionID = descriptor.getAlgorithmID();
        setEnabled(false);
        this.isFlexiProviderAlgorithm = descriptor.isFlexiProviderAlgorithm();
    }

    public void setText(String text) {
    	this.text = text;
    }
    
    public String getText() {
    	return(text);
    }
    
    public String getToolTipText() {
    	return(toolTipText);
    }
    
    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute()
     */
    @Override
    public Object execute(ExecutionEvent event) {
        // standard handling for everything not expressly specified above
        if (algorithmHandler == null) {
            // create executable extension
            try {
                algorithmHandler = createAlgorithmHandler();
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                        "CoreException while creating an executable extension of an AlgorithmHandler", //$NON-NLS-1$
                        e, false);
            }
            // set Action id
            algorithmHandler.setId(algorithmActionID);
        }
        algorithmHandler.execute(null);
        return(null);
    }

    public void run(final IDataObject dataobject) {
        // standard handling for everything not expressly specified above
        if (algorithmHandler == null) {
            // create executable extension
            try {
                algorithmHandler = createAlgorithmHandler();
            } catch (CoreException e) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID,
                        "CoreException while creating an executable extension of an AlgorithmHandler", //$NON-NLS-1$
                        e, false);
            }
            // set Action id
            algorithmHandler.setId(algorithmActionID);
        }

        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                if (dataobject instanceof ClassicDataObject)
                    if (((ClassicDataObject) dataobject).getInputStream() == null)
                        ((ClassicDataObject) dataobject).setInputStream(algorithmHandler.getActiveEditorInputStream());
                    else if (dataobject instanceof ModernDataObject)
                        if (((ModernDataObject) dataobject).getInputStream() == null)
                            ((ModernDataObject) dataobject).setInputStream(algorithmHandler.getActiveEditorInputStream());
            }
        });
        algorithmHandler.run(dataobject);
    }

    /**
     * "Lazy-loads" the algorithm plug-in
     * 
     * @return The plug-in's implementation of AbstractAlgorithmAction
     * @throws CoreException
     */
    private AbstractAlgorithmHandler createAlgorithmHandler() throws CoreException {

        IExtensionRegistry registry = Platform.getExtensionRegistry();

        IExtensionPoint extensionPoint = registry.getExtensionPoint(OperationsPlugin.PLUGIN_ID,
                IOperationsConstants.PL_ALGORITHMS_CMD);

        AbstractAlgorithmHandler handler = null;

        IExtension extension = extensionPoint.getExtension(this.extensionID);

        IConfigurationElement[] configElements = extension.getConfigurationElements();

        for (int i = 0; i < configElements.length; i++) {
            IConfigurationElement element = configElements[i];

            if (element.getName().equals(IOperationsConstants.TAG_ALGORITHM_CMD)) {
                handler = (AbstractAlgorithmHandler) element
                        .createExecutableExtension(IOperationsConstants.ATT_HANDLER_CLASS);
            }
        }
        return handler;
    }

    /**
     * Getter for the algorithm action
     * 
     * @return an instance of AbstractAlgorithmAction
     */
    public AbstractAlgorithmHandler getAlgorithmHandler() {
        return algorithmHandler;
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
