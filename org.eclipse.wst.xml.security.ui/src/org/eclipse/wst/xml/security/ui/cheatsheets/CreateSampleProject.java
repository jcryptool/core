/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.cheatsheets;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;

/**
 * <p>Cheat Sheet action to create the sample project. Creates a new project
 * named <b>XML-Security</b> (if it doesn't exist yet) and a new XML
 * document named <b>FirstSteps.xml</b> (if it doesn't exist yet) containing
 * the sample XML document structure from the help pages.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class CreateSampleProject extends Action implements ICheatSheetAction {
    /** The sample project to create. */
    private static final String EXAMPLE_PROJECT = "XML-Security"; //$NON-NLS-1$
    /** The sample XML document to create. */
    private static final String EXAMPLE_FILE = "FirstSteps.xml"; //$NON-NLS-1$
    /** The sample XML file located in the plug-in. */
    private static final String SAMPLE_FILE = "resources/FirstSteps.xml"; //$NON-NLS-1$

    /**
     * Attempts to create the sample project <b>XML-Security</b> and the sample file
     * <b>FirstSteps.xml</b>. Opens the project if it already exists. The sample file
     * is only created if it doesn't exist either. After everything is done the sample
     * file will be opened in the dedicated XML editor (the opened file may be the
     * original sample file or the file that already existed in the workspace).
     *
     * @param params Parameters for the action
     * @param manager The Cheat Sheet manager
     */
    public void run(final String[] params, final ICheatSheetManager manager) {
        try {
            IProject sampleProject = ResourcesPlugin.getWorkspace().getRoot().getProject(EXAMPLE_PROJECT);

            // create project if it does not yet exist
            if (!sampleProject.exists()) {
                sampleProject.create(null);
            }

            // open the project if it is closed
            if (!sampleProject.isOpen()) {
                sampleProject.open(null);
            }

            IFile sampleFile = sampleProject.getFile(new Path(EXAMPLE_FILE));

            if (!sampleFile.exists()) {
                InputStream sampleData = null;

                try {
                    sampleData = FileLocator.openStream(XSTUIPlugin.getDefault().getBundle(), new Path(SAMPLE_FILE), false);

                    // create the new file and fill it with the sample content
                    sampleFile.create(sampleData, true, null);
                } catch (Exception ex) {
                    if (sampleData != null) {
                        sampleData.close();
                    }

                    String reason = ex.getMessage();
                    if (reason == null) {
                        reason = Messages.errorReasonUnavailable;
                    }

                    IStatus status = new Status(IStatus.ERROR, XSTUIPlugin.getDefault().getBundle().getSymbolicName(), 0, reason, ex);
                    XSTUIPlugin.getDefault().getLog().log(status);

                    showErrorDialog(Messages.error, Messages.createProjectFileXMLError, reason, status);
                }
            }

//            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(sampleFile),
//                    PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(sampleFile.getName()).getId(), true);
        } catch (Exception ex) {
            String reason = ex.getMessage();
            if (reason == null) {
                reason = Messages.errorReasonUnavailable;
            }

            IStatus status = new Status(IStatus.ERROR, XSTUIPlugin.getDefault().getBundle().getSymbolicName(), 0, reason, ex);
            XSTUIPlugin.getDefault().getLog().log(status);

            showErrorDialog(Messages.error, Messages.createProjectFileXMLError, reason, status);
        }
    }

    /**
     * Shows an error dialog with a details button for detailed error
     * information. The error message and error details are automatically
     * written to the workspace log file.
     *
     * @param title The title for this error dialog
     * @param message The message for this error dialog
     * @param reason The detailed error message for this error dialog
     * @param status The IStatus object for this error dialog
     * @param ex The exception
     */
    private void showErrorDialog(final String title, final String message, final String reason, final IStatus status) {
        ErrorDialog errorDialog = new ErrorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                title, message, status, IStatus.ERROR);
        errorDialog.open();
    }
}
