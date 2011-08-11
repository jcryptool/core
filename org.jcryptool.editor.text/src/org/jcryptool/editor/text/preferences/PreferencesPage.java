// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.editor.text.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

/**
 * Preference page for <b>JCrypTool Texteditor</b> preferences.
 * 
 * @author Dominik Schadow
 * @version 0.9.2
 */
public class PreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    public PreferencesPage() {
        super(GRID);

        setDescription(null);
        initializeDefaults();
    }

    private void initializeDefaults() {
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.jcryptool.editor.text.preferences"); //$NON-NLS-1$
    }

    @Override
    protected void createFieldEditors() {
    }

    public void init(IWorkbench workbench) {
        final IPreferenceStore store = JCTTextEditorPlugin.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

}
