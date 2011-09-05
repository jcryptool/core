//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.PairingBDII.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class Illustration {

    private final Canvas canvas;

    private final Button GenAuthi;
    private final Button Broadcast2;
    private final Button Broadcast3;
    private final Button CompKey;
    private final Button Verify;

    private final Label VerifyLabel;

    private final Label Step1;
    private final Label Step2;
    private final Label Step3;
    private final Label Step4;

    private GridData gridData;

    public Illustration(Composite parent) {

        final Group group_Illustration = new Group(parent, SWT.NONE);
        group_Illustration.setLayout(new GridLayout(2, false));
        gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.horizontalSpan = 2;
        group_Illustration.setLayoutData(gridData);
        group_Illustration.setText(Messages.Illustration_0);

        canvas = new Canvas(group_Illustration, SWT.NONE);
        canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        gridData = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 5);
        gridData.widthHint = 300;
        gridData.heightHint = 360;
        canvas.setLayoutData(gridData);
        canvas.addPaintListener(new GraphPainter());

        final Group groupStep1 = new Group(group_Illustration, SWT.NONE);
        groupStep1.setText(Messages.Illustration_1);
        groupStep1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        groupStep1.setLayout(new GridLayout(1, false));
        Step1 = new Label(groupStep1, SWT.WRAP);
        Step1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Step1.setText(Messages.Illustration_2);
        Step1.setEnabled(false);
        GenAuthi = new Button(groupStep1, SWT.PUSH);
        GenAuthi.setText(Messages.Illustration_3);
        GenAuthi.setEnabled(false);
        GenAuthi.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                Model.getDefault().setupStep2();
            }
        });

        final Group groupStep2 = new Group(group_Illustration, SWT.NONE);
        groupStep2.setText(Messages.Illustration_4);
        groupStep2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        groupStep2.setLayout(new GridLayout(1, false));
        Step2 = new Label(groupStep2, SWT.WRAP);
        Step2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Step2
                .setText(Messages.Illustration_5);
        Step2.setEnabled(false);
        Broadcast2 = new Button(groupStep2, SWT.PUSH);
        Broadcast2.setText(Messages.Illustration_6);
        Broadcast2.setEnabled(false);
        Broadcast2.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                Model.getDefault().setupStep3();
            }
        });

        final Group groupStep3 = new Group(group_Illustration, SWT.NONE);
        groupStep3.setText(Messages.Illustration_7);
        groupStep3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        groupStep3.setLayout(new GridLayout(1, false));
        Step3 = new Label(groupStep3, SWT.WRAP);
        Step3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Step3
                .setText(Messages.Illustration_8
                        + Messages.Illustration_9
                        + Messages.Illustration_10
                        + Messages.Illustration_11
                        + Messages.Illustration_12);
        Step3.setEnabled(false);
        Broadcast3 = new Button(groupStep3, SWT.PUSH);
        Broadcast3.setEnabled(false);
        Broadcast3.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                Model.getDefault().setupStep4();
            }
        });
        // Broadcast3.addListener(SWT.Selection, listener);
        Broadcast3.setText(Messages.Illustration_13);

        final Group groupStep4 = new Group(group_Illustration, SWT.NONE);
        groupStep4.setText(Messages.Illustration_14);
        groupStep4.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        groupStep4.setLayout(new GridLayout(1, false));
        Step4 = new Label(groupStep4, SWT.WRAP);
        Step4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        Step4
                .setText(Messages.Illustration_15
                        + Messages.Illustration_16
                        + Messages.Illustration_17);
        Step4.setEnabled(false);
        CompKey = new Button(groupStep4, SWT.PUSH);
        CompKey.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                Model.getDefault().setupStep5();
            }
        });
        // CompKey.addListener(SWT.Selection, listener);
        CompKey.setText(Messages.Illustration_18);
        CompKey.setEnabled(false);

        final Group groupStep5 = new Group(group_Illustration, SWT.NONE);
        groupStep5.setText(Messages.Illustration_19);
        groupStep5.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        groupStep5.setLayout(new GridLayout(1, false));
        VerifyLabel = new Label(groupStep5, SWT.WRAP);
        VerifyLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        VerifyLabel.setText(Messages.Illustration_20);
        VerifyLabel.setEnabled(false);
        Verify = new Button(groupStep5, SWT.NONE);
        Verify.setText(Messages.Illustration_21);
        Verify.setEnabled(false);
        Verify.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                Model.getDefault().setupStep6();
                Verify.setEnabled(false);
            }
        });
    }

    public void changeToKis2(boolean change) {
        if (change) {
            Step2.setText(Messages.Illustration_22);
            Step3
                    .setText(Messages.Illustration_23
                            + Messages.Illustration_24);
            Step4
                    .setText(Messages.Illustration_25);
        } else {
            Step2.setText(Messages.Illustration_26);
            Step3
                    .setText(Messages.Illustration_27
                            + Messages.Illustration_28);
            Step4.setText(Messages.Illustration_29);
        }
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Button getStep0() {
        return GenAuthi;
    }

    public Button getStep1() {
        return Broadcast2;
    }

    public Button getStep2() {
        return Broadcast3;
    }

    public Button getStep3() {
        return CompKey;
    }

    public Button getVerify() {
        return Verify;
    }

    public Label getVerifyLabel() {
        return VerifyLabel;
    }

    // makes step 1 visible
    public void setStep1Enabled(boolean isvisible) {
        GenAuthi.setEnabled(isvisible);
        Step1.setEnabled(isvisible);

        Broadcast2.setEnabled(false);
        Step2.setEnabled(false);
        Broadcast3.setEnabled(false);
        Step3.setEnabled(false);
        CompKey.setEnabled(false);
        Step4.setEnabled(false);
        Verify.setEnabled(false);
        VerifyLabel.setEnabled(false);
    }

    // makes step 2 visible
    public void setStep2Enabled(boolean isvisible) {
        Broadcast2.setEnabled(isvisible);
        Step2.setEnabled(isvisible);
        GenAuthi.setEnabled(!isvisible);
    }

    // makes step 3 visible
    public void setStep3Enabled(boolean isvisible) {
        Broadcast3.setEnabled(isvisible);
        Step3.setEnabled(isvisible);
        Broadcast2.setEnabled(!isvisible);
    }

    // Makes step 4 visible
    public void setStep4Enabled(boolean isvisible) {
        CompKey.setEnabled(isvisible);
        Step4.setEnabled(isvisible);
        Broadcast3.setEnabled(!isvisible);
    }

    // makes the verification unit visible
    public void setVerifyEnabled(boolean isvisible) {
        Verify.setEnabled(isvisible);
        VerifyLabel.setEnabled(isvisible);
        CompKey.setEnabled(!isvisible);
    }
}
