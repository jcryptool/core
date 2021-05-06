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
package org.jcryptool.core.preferences.pages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.CorePlugin;

/**
 * The start page for all <b>Visualizations</b> preference pages.
 *
 * @author Dominik Schadow
 * @version 0.6.0
 */
public class VisualsPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
    public VisualsPage() {
        super(GRID);
        setPreferenceStore(CorePlugin.getDefault().getPreferenceStore());
//        setDescription(Messages.VisualsPage_0);
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.jcryptool.core.visualsPreferences"); //$NON-NLS-1$
    }

    @Override
    protected void createFieldEditors() {
    }

    @Override
	public void init(IWorkbench workbench) {
    }
}
