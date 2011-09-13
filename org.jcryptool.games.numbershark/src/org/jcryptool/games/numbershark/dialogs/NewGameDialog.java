// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.numbershark.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

public class NewGameDialog extends TitleAreaDialog {
    private int numberOfFields = 40;;

    public NewGameDialog(Shell shell) {
        super(shell);
        setShellStyle(SWT.TITLE | SWT.APPLICATION_MODAL);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(Messages.NewGameDialog_0);
        setMessage(Messages.NewGameDialog_1, IMessageProvider.INFORMATION);
        LocalResourceManager resources = new LocalResourceManager(JFaceResources.getResources(), getShell());
        setTitleImage(resources.createImage(NumberSharkPlugin.getImageDescriptor("/icons/new_game.png"))); //$NON-NLS-1$

        Composite area = (Composite) super.createDialogArea(parent);

        Group maximumNumberGroup = new Group(area, SWT.NONE);
        maximumNumberGroup.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        maximumNumberGroup.setLayout(new GridLayout(2, false));
        maximumNumberGroup.setText(Messages.NewGameDialog_2);

        final Slider numSlider = new Slider(maximumNumberGroup, SWT.NONE);
        numSlider.setValues(numberOfFields, 1, 1031, 7, 1, 10);
        numSlider.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

        final Spinner numSpinner = new Spinner(maximumNumberGroup, SWT.BORDER);
        numSpinner.setValues(numberOfFields, 1, 1024, 0, 1, 10);

        numSpinner.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                numSlider.setSelection(numSpinner.getSelection());
                numberOfFields = numSlider.getSelection();
            }

        });

        numSlider.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                numSpinner.setSelection(numSlider.getSelection());
                numberOfFields = numSlider.getSelection();
            }
        });

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, NumberSharkPlugin.PLUGIN_ID + ".newGameDialog"); //$NON-NLS-1$

        return area;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(425, 250);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.NewGameDialog_4);
    }

    /**
     * Create contents of the button bar.
     *
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

}
