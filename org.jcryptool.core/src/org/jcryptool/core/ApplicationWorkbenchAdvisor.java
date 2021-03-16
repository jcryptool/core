// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core;

import java.util.regex.Pattern;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.util.constants.IConstants;

/**
 * The ApplicationWorkbenchAdvisor class is used for configuring the workbench.
 *
 * @author Dominik Schadow
 * @author Holger Friedrich (a method in org.jcryptool.core.operations was renamed
 * as part of the effort to remove support for Actions)
 * @version 0.9.6
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
    /**
     * Creates a new workbench window advisor for configuring a new workbench window via the given workbench window
     * configurer.
     *
     * @param configurer the workbench window configurer
     * @return a new workbench window advisor
     */
    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }
    
    /**
     * Returns the id of the perspective to use for the initial workbench window, or <code>null</code> if no initial
     * perspective should be shown in the initial workbench window.
     *
     * @return the id of the perspective for the initial window, or <code>null</code> if no initial perspective should
     *         be shown
     */
    @Override
	public String getInitialWindowPerspectiveId() {
        return Perspective.PERSPECTIVE_ID;
    }

    /**
     * Performs arbitrary actions after the workbench windows have been opened (or restored), but before the main event
     * loop is run.
     */
    @Override
	public void postStartup() {
        super.postStartup();
        if (OperationsPlugin.getDefault().getAlgorithmsManager() != null) {
            if (getWorkbenchConfigurer().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null) {
                OperationsPlugin.getDefault().getAlgorithmsManager().setCommandsEnabled(true);
            }
        }
    }
    
    @Override
    public void preStartup() {
    	super.preStartup();
    }

    /**
     * @see org.eclipse.ui.application.WorkbenchAdvisor#preShutdown()
     */
    @Override
	public boolean preShutdown() {
        String unsavedRegex = Messages.ApplicationWorkbenchAdvisor_4 + "\\d\\d\\d.txt"; //$NON-NLS-1$
        String name;
        IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        LogUtil.logInfo("no of editor references: " + editorReferences.length); //$NON-NLS-1$
        for (int i = 0; i < editorReferences.length; i++) {
            try {
                name = editorReferences[i].getEditorInput().getName();
                LogUtil.logInfo("Name: " + name); //$NON-NLS-1$
                if (Pattern.matches(unsavedRegex, name) || Pattern.matches(IConstants.OUTPUT_REGEXP, name)) {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(
                            editorReferences[i].getEditor(false), false);
                }
            } catch (PartInitException e) {
                LogUtil.logError(CorePlugin.PLUGIN_ID, "Exception while accessing editor references", e, false); //$NON-NLS-1$
            }
        }

        return true;
    }


    /**
     * Initializes the JCrypTool workbench. Saves the state of the workbench when closing (like position, size). All
     * other defaults (like modern tab style) must be set up in the plugin_customization.ini file. See the
     * <strong>IWorkbenchPreferenceConstants</strong> for details on possible parameters.
     *
     * @param configurer The workbench configurer
     */
    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    }
}