//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII.ui;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.PairingBDII.basics.Logging_BNP;
import org.jcryptool.visual.PairingBDII.basics.Logging_ECBDII;

public class Logging {

    private final Group group_Logging;

    private final Button buttonSaveLogfile;

    private final Label label_Title;
    private final Label label_TimePerBurdenedUser;
    private final Label label_TimePerUnburdenedUser;
    private final Label timePerBurdenedUser;
    private final Label timePerUnburdenedUser;

    public Logging(Composite parent) {

        group_Logging = new Group(parent, SWT.NONE);
        group_Logging.setText(Messages.Logging_0);
        group_Logging.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        group_Logging.setLayoutData(gridData);

        label_Title = new Label(group_Logging, SWT.NONE);
        label_Title.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        label_Title.setText(Messages.Logging_1);

        label_TimePerBurdenedUser = new Label(group_Logging, SWT.NONE);
        label_TimePerBurdenedUser.setText(Messages.Logging_2);

        timePerBurdenedUser = new Label(group_Logging, SWT.NONE);

        label_TimePerUnburdenedUser = new Label(group_Logging, SWT.NONE);
        label_TimePerUnburdenedUser.setText(Messages.Logging_3);

        timePerUnburdenedUser = new Label(group_Logging, SWT.NONE);

        buttonSaveLogfile = new Button(group_Logging, SWT.PUSH);
        buttonSaveLogfile.setText(Messages.Logging_4);
        buttonSaveLogfile.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
                dialog.setFilterPath(DirectoryService.getUserHomeDir());
                dialog.setFileName(Messages.Logging_6);
                dialog.setFilterNames(new String[] {Messages.Logging_7});
                dialog.setFilterExtensions(new String[] {Messages.Logging_8});
                dialog.setOverwrite(true);

                final String filename = dialog.open();

                Logging_ECBDII log = null;
                Logging_BNP logBN = null;

                if (filename != null) {
                    if (!Model.getDefault().isOnBNCurve) {
                        log =
                                new Logging_ECBDII(Model.getDefault().algorithm,
                                        Model.getDefault().sk, Model.getDefault().pk,
                                        Model.getDefault().nonces, Model.getDefault().usersData);
                        log.SetAsTime(Model.getDefault().bduser, Model.getDefault().unbduser);
                    } else {
                        logBN =
                                new Logging_BNP(Model.getDefault().numberOfUsers, 160,
                                        Model.getDefault().algorithmBN, Model.getDefault().bnpuData);
                        logBN.setAsTime(Model.getDefault().bduser, Model.getDefault().unbduser);
                    }

                    FileOutputStream output = null;
                    try {
                        output = new FileOutputStream(filename);
                        if (!Model.getDefault().isOnBNCurve) {
                            new PrintStream(output).append(log.PrintLog());
                        } else {
                            new PrintStream(output).append(logBN.printLog());
                        }
                    } catch (final FileNotFoundException e1) {
                        final MessageBox messageBox = new MessageBox(new Shell(), SWT.ERROR);
                        messageBox.setText(Messages.Logging_9);
                        messageBox.setMessage(Messages.Logging_10);
                        messageBox.open();
                    } finally {
                        try {
                            if (output != null) {
                                output.close();
                            }
                        } catch (final IOException ex) {
                            LogUtil.logError(ex);
                        }
                    }
                }
            }
        });
    }

    public Button getLogRerun() {
        return buttonSaveLogfile;
    }

    public void reset() {
        label_Title.setEnabled(false);
        label_TimePerBurdenedUser.setEnabled(false);
        label_TimePerUnburdenedUser.setEnabled(false);
        buttonSaveLogfile.setEnabled(false);
        timePerBurdenedUser.setText(""); //$NON-NLS-1$
        timePerUnburdenedUser.setText(""); //$NON-NLS-1$
    }

    public void setTimeTo(long timePerBurdenedUser, long timePerUnburdenedUser) {
        label_Title.setEnabled(true);
        label_TimePerBurdenedUser.setEnabled(true);
        label_TimePerUnburdenedUser.setEnabled(true);
        buttonSaveLogfile.setEnabled(true);
        this.timePerBurdenedUser.setText("= " + Long.toString(timePerBurdenedUser) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
        this.timePerUnburdenedUser.setText("= " + Long.toString(timePerUnburdenedUser) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
        group_Logging.layout();
    }
}
