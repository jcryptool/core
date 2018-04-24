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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.PairingBDII.basics.UserData_BNP;
import org.jcryptool.visual.PairingBDII.basics.UserData_ECBDII;

public class DefinitionAndDetails {
    private final Group groupDefinitions;
    private final Group groupDetails;

    private Label label;
    private final Label labelAuthetification;
    private final Label labelPK;
    private final Label labelExplanation;
    private final Text labelDetails;

    private final Spinner spinnerUserIndex;
    private final Label labelUserIndex;

    public DefinitionAndDetails(Composite parent) {
        groupDefinitions = new Group(parent, SWT.NONE);
        groupDefinitions.setText(Messages.DefinitionAndDetails_0);
        groupDefinitions.setLayout(new GridLayout(2, false));
        groupDefinitions.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        label = new Label(groupDefinitions, SWT.WRAP);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        label.setText(Messages.DefinitionAndDetails_1);

        label = new Label(groupDefinitions, SWT.WRAP);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3));
        label.setText(Messages.DefinitionAndDetails_2
                        + Messages.DefinitionAndDetails_3
                        + Messages.DefinitionAndDetails_4
                        + Messages.DefinitionAndDetails_5
                        + Messages.DefinitionAndDetails_6
                        + Messages.DefinitionAndDetails_7
                        + Messages.DefinitionAndDetails_8
                        + Messages.DefinitionAndDetails_9);

        label = new Label(groupDefinitions, SWT.WRAP);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        label.setText(Messages.DefinitionAndDetails_10 + Messages.DefinitionAndDetails_11
                + Messages.DefinitionAndDetails_12);
        
        labelPK = new Label(groupDefinitions, SWT.WRAP);
        labelPK.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        labelExplanation = new Label(groupDefinitions, SWT.WRAP);
        labelExplanation.setText(Messages.DefinitionAndDetails_13 + Messages.DefinitionAndDetails_14
                + Messages.DefinitionAndDetails_15
                + Messages.DefinitionAndDetails_16);
        labelExplanation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        labelAuthetification = new Label(groupDefinitions, SWT.WRAP);
        labelAuthetification.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        groupDetails = new Group(parent, SWT.NONE);
        groupDetails.setText(Messages.DefinitionAndDetails_17);
        groupDetails.setLayout(new GridLayout(1, false));
        groupDetails.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));

        final Composite userSelection = new Composite(groupDetails, SWT.NONE);
        userSelection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        userSelection.setLayout(new GridLayout(2, false));

        labelUserIndex = new Label(userSelection, SWT.NONE);
        labelUserIndex.setText(Messages.DefinitionAndDetails_18);
        spinnerUserIndex = new Spinner(userSelection, SWT.BORDER);
        spinnerUserIndex.setMinimum(1);
        spinnerUserIndex.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                Model.getDefault().infoUserIndex = spinnerUserIndex.getSelection()-1;

                if (!Model.getDefault().isOnBNCurve) {
                    displayUserDetails(Model.getDefault().usersData.get(Model.getDefault().infoUserIndex));
                } else {
                    displayUserDetails(Model.getDefault().bnpuData.get(Model.getDefault().infoUserIndex));
                }
            }
        });

        labelDetails = new Text(groupDetails, SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL);
        labelDetails.addMouseMoveListener(new MouseMoveListener() {
            // work around to ensure parent can be scrolled when entering this text field
            public void mouseMove(MouseEvent e) {
                groupDetails.getParent().setFocus();
            }
        });
        labelDetails.setEnabled(false);
        labelDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        parent.getParent().layout();
    }

    public void changetoKis2(boolean change) {
        if (change) {
            labelPK.setText(Messages.DefinitionAndDetails_19);
            labelAuthetification
                    .setText(Messages.DefinitionAndDetails_20
                            + Messages.DefinitionAndDetails_21);
        } else {
            labelPK.setText(Messages.DefinitionAndDetails_22);
            labelAuthetification
                    .setText(Messages.DefinitionAndDetails_23
                            + Messages.DefinitionAndDetails_24);
        }
        groupDefinitions.layout();
    }

    public void displayUserDetails(UserData_BNP userData) {
        labelDetails.setText(userData.toString() + " "); //$NON-NLS-1$
        labelDetails.setEnabled(true);
        //groupDetails.layout();
        groupDetails.getParent().layout(new Control[] {labelDetails});
        // group_Details.getParent().layout();
        // group_Details.getParent().getParent().layout();
        // group_Details.getParent().getParent().getParent().layout();
    }

    public void displayUserDetails(UserData_ECBDII userData) {
        labelDetails.setText(userData.toString() + " "); //$NON-NLS-1$
        labelDetails.setEnabled(true);
        //groupDetails.layout();
        groupDetails.getParent().layout(new Control[] {labelDetails});
        // group_Details.getParent().layout();
        // group_Details.getParent().getParent().layout();
        // group_Details.getParent().getParent().getParent().layout();
    }

    public Composite getGroupDefinitions() {
        return groupDefinitions;
    }

    public Group getGroupDetails() {
        return groupDetails;
    }

    public void reset() {
        labelUserIndex.setEnabled(false);
        labelDetails.setEnabled(false);
        spinnerUserIndex.setEnabled(false);
        labelDetails.setText("\n\n\n\n\n\n\n"); //$NON-NLS-1$
    }

    public void setMaximumNumberOfUsers(int numberOfUsers) {
        spinnerUserIndex.setMaximum(numberOfUsers);
        spinnerUserIndex.pack();
    }

    public void step1ExplVisible(boolean isvisible) {
        labelUserIndex.setEnabled(isvisible);
        spinnerUserIndex.setEnabled(isvisible);
        if (!isvisible) {
            labelDetails.setText("\n\n\n\n\n\n\n"); //$NON-NLS-1$
        }
    }

    public void step2ExplVisible(boolean isvisible) {
        labelExplanation.setText(Messages.DefinitionAndDetails_29
                + Messages.DefinitionAndDetails_30
                + Messages.DefinitionAndDetails_31
                + Messages.DefinitionAndDetails_32
                + Messages.DefinitionAndDetails_33);
        groupDefinitions.layout();
    }

    public void step3ExplVisible(boolean isvisible) {
        labelExplanation.setText(Messages.DefinitionAndDetails_34
                + Messages.DefinitionAndDetails_35);
        groupDefinitions.layout();
    }

    // public static void refresh() {
    // group_Definitions.layout();
    // }
}
