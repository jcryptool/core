// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.fileexplorer.FileExplorerPlugin;

/**
 * General preference page for the <b>File Explorer</b> view.
 *
 * @author Dominik Schadow
 * @version 0.5
 */
public class GeneralPreferencePage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage {
    private DirectoryFieldEditor userHome;

    public GeneralPreferencePage() {
        super(GRID);
        setPreferenceStore(FileExplorerPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.GeneralPreferencePage_0);
    }

    @Override
    protected void createFieldEditors() {
        userHome = new DirectoryFieldEditor(PreferenceConstants.P_START_DIRECTORY,
                Messages.GeneralPreferencePage_1, getFieldEditorParent());
        addField(userHome);
        addField(new BooleanFieldEditor(PreferenceConstants.P_OPEN_SOURCE,
                Messages.GeneralPreferencePage_2, getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_OPEN_TARGET,
                Messages.GeneralPreferencePage_3, getFieldEditorParent()));
    }

    public void createControl(Composite parent) {
        super.createControl(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                "org.jcryptool.fileexplorer.fileExplorerPreferences"); //$NON-NLS-1$
    }

    public void init(IWorkbench workbench) {
    }

    private void storePath() {
        DirectoryService.setUserHomeDir(userHome.getStringValue());
    }

    @Override
    protected void performApply() {
        storePath();

        super.performApply();
    }

    @Override
    public boolean performOk() {
        storePath();

        return super.performOk();
    }
}
