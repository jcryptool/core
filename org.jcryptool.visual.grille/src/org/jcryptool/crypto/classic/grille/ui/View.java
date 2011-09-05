// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.grille.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.classic.grille.GrillePlugin;
import org.jcryptool.crypto.classic.grille.algorithm.Grille;
import org.jcryptool.crypto.classic.grille.algorithm.KeySchablone;

public class View extends ViewPart {

    private Grille model;
    private Text text_input;
    private Button button_encrypt;
    private Button button_decrypt;
    private Button button_step6;
    private Button button_step5;
    private Button button_step4;
    private Button button_step3;
    private Button button_step2;
    private Button button_step1;
    private Button button_direct;
    private Button button_stepwise;
    private Text text_output;
    private Button button_okay;
    private Spinner spinner_keySize;
    private Canvas canvas_schluessel;
    private boolean setEncrypt = true;
    private Group group_output;
    private Group group_input;
    private Label label_step1;
    private Label label_step2;
    private Label label_step3;
    private Label label_step4;
    private Label label_step5;
    private Label label_step6;
    private Canvas canvas_demonstration;
    protected Demonstration demonstration;
    private KeyListener schluessel_listener;
    private Composite parent;

    public View() {
        model = new Grille();
        model.setKey(new KeySchablone(6));
    }

    @Override
    public void createPartControl(Composite viewParent) {
        ScrolledComposite scrolledComposite = new ScrolledComposite(viewParent, SWT.H_SCROLL | SWT.V_SCROLL);
        parent = new Composite(scrolledComposite, SWT.NONE);
        parent.setLayout(new GridLayout(3, false));

        createDescription(parent);
        createOptions(parent);
        createInputtext(parent);
        createOutputtext(parent);
        createSchablone(parent);
        createDemonstration(parent);
        createExecutionControls(parent);

        scrolledComposite.setContent(parent);
        scrolledComposite.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.layout();
    }

    private void createOutputtext(Composite parent) {
        group_output = new Group(parent, SWT.NONE);
        group_output.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
        group_output.setLayout(new FillLayout());
        group_output.setText(Messages.getString("View.ciphertext")); //$NON-NLS-1$
        text_output = new Text(group_output, SWT.MULTI | SWT.WRAP);
        text_output.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            public void keyPressed(KeyEvent e) {
                e.doit = false;
            }

            public void keyReleased(KeyEvent e) {
            }
        });

    }

    private void createExecutionControls(Composite parent) {

        Composite execType = new Composite(parent, SWT.NONE);
        execType.setLayout(new GridLayout(1, true));
        execType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

        createTypeSelection(execType);
        createSteps(execType);

    }

    private void createTypeSelection(Composite execType) {

        Group typeSelection = new Group(execType, SWT.NONE);
        GridData gd_typeSelection = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_typeSelection.widthHint = 270;
        typeSelection.setLayoutData(gd_typeSelection);
        typeSelection.setText(Messages.getString("View.type"));
        typeSelection.setLayout(new GridLayout(3, true));

        button_direct = new Button(typeSelection, SWT.RADIO);
        button_direct.setText(Messages.getString("View.direct")); //$NON-NLS-1$
        button_direct.setSelection(true);
        button_direct.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
            }
        });
        new Label(typeSelection, SWT.NONE);
        new Label(typeSelection, SWT.NONE);
        new Label(typeSelection, SWT.NONE);
        new Label(typeSelection, SWT.NONE);

                button_okay = new Button(typeSelection, SWT.NONE);
                button_okay.setImage(GrillePlugin.getImageDescriptor("icons/run_exc.gif").createImage());
                GridData gd_button_okay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
                gd_button_okay.widthHint = 78;
                gd_button_okay.heightHint = 39;
                button_okay.setLayoutData(gd_button_okay);
                button_okay.setText(Messages.getString("View.start")); //$NON-NLS-1$
                button_okay.setEnabled(false);
                button_okay.addSelectionListener(new SelectionListener() {
                    public void widgetDefaultSelected(SelectionEvent e) {
                        widgetSelected(e);
                    }

                    public void widgetSelected(SelectionEvent e) {
                        reset();
                        if (button_direct.getSelection()) {
                            if (setEncrypt == true) {
                                String checkedText = model.check(text_input.getText());
                                text_input.setText(checkedText);
                                text_output.setText(model.encrypt(checkedText));
                            } else {
                                String checkedText = model.check(text_input.getText());
                                if (checkedText.length() == text_input.getText().length())
                                    text_output.setText(model.decrypt(text_input.getText()));
                                else {
                                    MessageBox messageBox = new MessageBox(new Shell(Display.getCurrent()), SWT.ERROR);
                                    messageBox.setText(Messages.getString("View.error")); //$NON-NLS-1$
                                    messageBox.setMessage(Messages.getString("View.length_error")); //$NON-NLS-1$
                                    messageBox.open();
                                }

                            }
                        } else {
                            demonstration = new Demonstration(model, text_input.getText());
                            demonstration.showStep1();
                            canvas_demonstration
                                    .addPaintListener(new DemonstrationPainter(canvas_demonstration, demonstration));
                            canvas_demonstration.redraw();
                            canvas_schluessel.removeMouseListener(schluessel_listener);
                            button_step1.setEnabled(true);
                            label_step1.setEnabled(true);
                        }

                    }

                });

        button_stepwise = new Button(typeSelection, SWT.RADIO);
        button_stepwise.setText(Messages.getString("View.stepwise")); //$NON-NLS-1$
                new Label(typeSelection, SWT.NONE);
                new Label(typeSelection, SWT.NONE);
        button_stepwise.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
            }
        });
    }

    private void createSteps(Composite execType) {

        Group steps = new Group(execType, SWT.NONE);
        steps.setText(Messages.getString("View.steps"));
        steps.setLayout(new GridLayout(1, true));

        Group step1 = new Group(steps, SWT.NONE);

        step1.setLayout(new GridLayout(2, false));
        step1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        step1.setText(Messages.getString("View.step1")); //$NON-NLS-1$
        label_step1 = new Label(step1, SWT.NONE);
        label_step1.setText(Messages.getString("View.check")); //$NON-NLS-1$
        button_step1 = new Button(step1, SWT.NONE);
        button_step1.setText(Messages.getString("View.proceed")); //$NON-NLS-1$
        button_step1.setEnabled(false);
        button_step1.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                canvas_demonstration.redraw();
                canvas_schluessel.redraw();
                text_input.setText(text_input.getText() + demonstration.padding);
                demonstration.showStep2();
                label_step1.setEnabled(false);
                button_step2.setEnabled(true);
                label_step2.setEnabled(true);
            }
        });
        label_step1.setEnabled(false);

        Group step2 = new Group(steps, SWT.NONE);
        step2.setLayout(new GridLayout(2, false));
        step2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        step2.setText(Messages.getString("View.step2")); //$NON-NLS-1$
        label_step2 = new Label(step2, SWT.NONE);
        label_step2.setText(Messages.getString("View.first_turn")); //$NON-NLS-1$
        button_step2 = new Button(step2, SWT.NONE);
        button_step2.setText(Messages.getString("View.proceed")); //$NON-NLS-1$
        button_step2.setEnabled(false);
        button_step2.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                demonstration.showStep3();
                canvas_demonstration.redraw();
                canvas_schluessel.redraw();
                button_step2.setEnabled(false);
                label_step2.setEnabled(false);
                button_step3.setEnabled(true);
                label_step3.setEnabled(true);
            }
        });
        label_step2.setEnabled(false);

        Group step3 = new Group(steps, SWT.NONE);
        step3.setLayout(new GridLayout(2, false));
        step3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        step3.setText(Messages.getString("View.step3")); //$NON-NLS-1$
        label_step3 = new Label(step3, SWT.NONE);
        label_step3.setText(Messages.getString("View.second_turn")); //$NON-NLS-1$
        button_step3 = new Button(step3, SWT.NONE);
        button_step3.setText(Messages.getString("View.proceed")); //$NON-NLS-1$
        button_step3.setEnabled(false);
        button_step3.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                demonstration.showStep4();
                canvas_demonstration.redraw();
                canvas_schluessel.redraw();
                button_step3.setEnabled(false);
                label_step3.setEnabled(false);
                button_step4.setEnabled(true);
                label_step4.setEnabled(true);
            }
        });
        label_step3.setEnabled(false);

        Group step4 = new Group(steps, SWT.NONE);
        step4.setLayout(new GridLayout(2, false));
        step4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        step4.setText(Messages.getString("View.step4")); //$NON-NLS-1$
        label_step4 = new Label(step4, SWT.NONE);
        label_step4.setText(Messages.getString("View.third_turn")); //$NON-NLS-1$
        button_step4 = new Button(step4, SWT.NONE);
        button_step4.setText(Messages.getString("View.proceed")); //$NON-NLS-1$
        button_step4.setEnabled(false);
        button_step4.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                demonstration.showStep5();
                canvas_demonstration.redraw();
                canvas_schluessel.redraw();
                button_step4.setEnabled(false);
                label_step4.setEnabled(false);
                button_step5.setEnabled(true);
                label_step5.setEnabled(true);
            }
        });
        label_step4.setEnabled(false);

        Group step5 = new Group(steps, SWT.NONE);
        step5.setLayout(new GridLayout(2, false));
        step5.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        step5.setText(Messages.getString("View.step5")); //$NON-NLS-1$
        label_step5 = new Label(step5, SWT.NONE);
        label_step5.setText(Messages.getString("View.fourth_turn")); //$NON-NLS-1$
        button_step5 = new Button(step5, SWT.NONE);
        button_step5.setText(Messages.getString("View.proceed")); //$NON-NLS-1$
        button_step5.setEnabled(false);
        button_step5.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                demonstration.showStep6();
                canvas_demonstration.redraw();
                canvas_schluessel.redraw();
                button_step5.setEnabled(false);
                label_step5.setEnabled(false);
                button_step6.setEnabled(true);
                label_step6.setEnabled(true);
            }
        });
        label_step5.setEnabled(false);

        Group step6 = new Group(steps, SWT.NONE);
        step6.setLayout(new GridLayout(2, false));
        step6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        step6.setText(Messages.getString("View.step6")); //$NON-NLS-1$
        label_step6 = new Label(step6, SWT.NONE);
        label_step6.setText(Messages.getString("View.linewise")); //$NON-NLS-1$
        button_step6 = new Button(step6, SWT.NONE);
        button_step6.setText(Messages.getString("View.proceed")); //$NON-NLS-1$
        button_step6.setEnabled(false);
        button_step6.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                text_output.setText(demonstration.getOutput());
                button_step6.setEnabled(false);
                label_step6.setEnabled(false);
            }
        });
        label_step6.setEnabled(false);
    }

    private void createDemonstration(Composite parent) {
        Group illustration = new Group(parent, SWT.NONE);
        illustration.setLayout(new FillLayout());
        illustration.setText(Messages.getString("View.visualisation")); //$NON-NLS-1$
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData.widthHint = 300;
        illustration.setLayoutData(gridData);
        canvas_demonstration = new Canvas(illustration, SWT.DOUBLE_BUFFERED);
        canvas_demonstration.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        // canvas_demonstration.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
        // true, false));
        // canvas_demonstration.addPaintListener(new
        // DemonstrationPainter(demonstration));
    }

    private void createInputtext(Composite parent) {
        group_input = new Group(parent, SWT.NONE);
        group_input.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
        group_input.setLayout(new FillLayout());
        group_input.setText(Messages.getString("View.plaintext")); //$NON-NLS-1$
        text_input = new Text(group_input, SWT.MULTI | SWT.WRAP);
        text_input.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                reset();
            }
        });
    }

    private void createSchablone(Composite parent) {
        Group schablone = new Group(parent, SWT.NONE);
        schablone.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        schablone.setLayout(new GridLayout(2, true));
        schablone.setText(Messages.getString("View.keygrille")); //$NON-NLS-1$

        canvas_schluessel = new Canvas(schablone, SWT.DOUBLE_BUFFERED);
        canvas_schluessel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        canvas_schluessel.addPaintListener(new KeyPainter(canvas_schluessel, model));
        schluessel_listener = new KeyListener(model, this);
        canvas_schluessel.addMouseListener(schluessel_listener);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2);
        gridData.widthHint = 151;
        gridData.heightHint = 151;
        canvas_schluessel.setLayoutData(gridData);

        Label spinner = new Label(schablone, SWT.NONE);
        spinner.setText(Messages.getString("View.size")); //$NON-NLS-1$
        spinner.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

        spinner_keySize = new Spinner(schablone, SWT.NONE);
        spinner_keySize.setMinimum(3);
        spinner_keySize.setMaximum(10);
        spinner_keySize.setIncrement(1);
        spinner_keySize.setSelection(6);
        spinner_keySize.setEnabled(true);
        spinner_keySize.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        spinner_keySize.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
                model.setKey(new KeySchablone(Integer.parseInt(spinner_keySize.getText())));
                canvas_schluessel.removeMouseListener(schluessel_listener);
                canvas_schluessel.addMouseListener(schluessel_listener);
                canvas_schluessel.redraw();
                checkOkButton();
            }
        });
    }

    private void createOptions(Composite parent) {
        Group options = new Group(parent, SWT.NONE);
        options.setLayout(new GridLayout(2, true));
        options.setText(Messages.getString("View.operation")); //$NON-NLS-1$
        options.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        button_encrypt = new Button(options, SWT.RADIO);
        button_encrypt.setText(Messages.getString("View.encryption")); //$NON-NLS-1$
        button_encrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
                setEncrypt = true;
                group_input.setText(Messages.getString("View.plaintext")); //$NON-NLS-1$
                group_output.setText(Messages.getString("View.ciphertext")); //$NON-NLS-1$
                button_stepwise.setEnabled(true);
            }
        });
        button_encrypt.setSelection(true);
        button_decrypt = new Button(options, SWT.RADIO);
        button_decrypt.setText(Messages.getString("View.decryption")); //$NON-NLS-1$
        button_decrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
                setEncrypt = false;
                group_output.setText(Messages.getString("View.plaintext")); //$NON-NLS-1$
                group_input.setText(Messages.getString("View.ciphertext")); //$NON-NLS-1$
                button_stepwise.setEnabled(false);
                button_stepwise.setSelection(false);
                button_direct.setSelection(true);
            }
        });
    }

    private void createDescription(Composite parent) {
        Composite compositeIntro = new Composite(parent, SWT.NONE);
        compositeIntro.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        compositeIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        compositeIntro.setLayout(new GridLayout(1, false));

        Label label = new Label(compositeIntro, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        label.setText(Messages.getString("View.title")); //$NON-NLS-1$

        StyledText stDescription = new StyledText(compositeIntro, SWT.READ_ONLY);
        stDescription.setText(Messages.getString("View.description1") + //$NON-NLS-1$
                Messages.getString("View.description2") + //$NON-NLS-1$
                Messages.getString("View.description3")); //$NON-NLS-1$
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    private void reset() {
        if (demonstration != null)
            demonstration.reset();
        checkOkButton();
        button_step1.setEnabled(false);
        button_step2.setEnabled(false);
        button_step3.setEnabled(false);
        button_step4.setEnabled(false);
        button_step5.setEnabled(false);
        button_step6.setEnabled(false);
        label_step1.setEnabled(false);
        label_step2.setEnabled(false);
        label_step3.setEnabled(false);
        label_step4.setEnabled(false);
        label_step5.setEnabled(false);
        label_step6.setEnabled(false);
        text_output.setText(""); //$NON-NLS-1$
        canvas_demonstration.redraw();
        canvas_schluessel.redraw();
    }

    @Override
    public void setFocus() {
        parent.setFocus();
    }

    public void checkOkButton() {
        if (model.getKey().isValid() && !text_input.getText().equals("")) { //$NON-NLS-1$
            button_okay.setEnabled(true);
        } else {
            button_okay.setEnabled(false);
        }
    }
}
