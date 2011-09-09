// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.entropy.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.entropy.EntropyPlugin;
import org.jcryptool.analysis.entropy.calc.EntropyCalc;
import org.jcryptool.analysis.textmodify.wizard.ModifyWizard;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;

import com.cloudgarden.resource.SWTResourceManager;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class EntropyUIconfig extends Composite {

    {
        // Register as a resource user - SWTResourceManager will
        // handle the obtaining and disposing of resources
        SWTResourceManager.registerResourceUser(this);
    }

    private Combo cComboSignificance;
    private CLabel cStatusLabel;
    private CLabel cLabel1;
    private Button buttonFilter;
    private Button buttonStart;
    private CLabel cLabel3;
    private Button buttonDeepAnalysis;
    private Combo cComboTupelLength;
    private Button buttonStandardAnalysis;
    private Group groupAnalysisConfig;
    private EntropyUI entropyUIpointer;

    private String editorname;

    private TransformData myModifySettings;

    /**
     * Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
     */
    protected void checkSubclass() {
    }

    /**
     * Auto-generated method to display this org.eclipse.swt.widgets.Composite inside a new Shell.
     */
    public static void showGUI() {
        Display display = Display.getDefault();
        Shell shell = new Shell(display);
        EntropyUIconfig inst = new EntropyUIconfig(shell, SWT.NULL);
        Point size = inst.getSize();
        shell.setLayout(new FillLayout());
        shell.layout();
        if (size.x == 0 && size.y == 0) {
            inst.pack();
            shell.pack();
        } else {
            Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
            shell.setSize(shellBounds.width, shellBounds.height);
        }
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

    public EntropyUIconfig(Composite parent, int style) {
        super(parent, style);
        initGUI();
        myModifySettings = new TransformData();
        myModifySettings.setUnmodified();
    }

    private void initGUI() {
        try {
            FormLayout thisLayout = new FormLayout();
            this.setLayout(thisLayout);

            {
                cStatusLabel = new CLabel(this, SWT.BORDER);
                FormData cStatusLabelLData = new FormData();
                cStatusLabelLData.width = 543;
                cStatusLabelLData.height = 19;
                cStatusLabelLData.left = new FormAttachment(0, 1000, 53);
                cStatusLabelLData.right = new FormAttachment(1000, 1000, -18);
                cStatusLabelLData.top = new FormAttachment(0, 1000, 240);
                cStatusLabel.setLayoutData(cStatusLabelLData);
                cStatusLabel.setText(Messages.EntropyUIconfig_0);
            }
            {
                cLabel1 = new CLabel(this, SWT.NONE);
                FormData cLabel1LData = new FormData();
                cLabel1LData.width = 41;
                cLabel1LData.height = 19;
                cLabel1LData.left = new FormAttachment(0, 1000, 13);
                cLabel1LData.top = new FormAttachment(0, 1000, 240);
                cLabel1.setLayoutData(cLabel1LData);
                cLabel1.setText(Messages.EntropyUIconfig_1);
            }
            {
                groupAnalysisConfig = new Group(this, SWT.NONE);
                GridLayout groupAnalysisConfigLayout = new GridLayout();
                groupAnalysisConfig.setLayout(groupAnalysisConfigLayout);
                FormData groupAnalysisConfigLData = new FormData();
                groupAnalysisConfigLData.width = 584;
                groupAnalysisConfigLData.height = 162;
                groupAnalysisConfigLData.left = new FormAttachment(0, 1000, 12);
                groupAnalysisConfigLData.top = new FormAttachment(0, 1000, 12);
                groupAnalysisConfigLData.right = new FormAttachment(1000, 1000, -12);
                groupAnalysisConfig.setLayoutData(groupAnalysisConfigLData);
                groupAnalysisConfig.setText(Messages.EntropyUIconfig_2);
                {
                    buttonFilter = new Button(groupAnalysisConfig, SWT.PUSH | SWT.CENTER);
                    buttonFilter.setText(Messages.EntropyUIconfig_3);
                    buttonFilter.setToolTipText(Messages.EntropyUIconfig_24);
                    buttonFilter.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            myModifySettings = getWizardSettings(myModifySettings);
                        }
                    });
                }
                {
                    buttonStandardAnalysis = new Button(groupAnalysisConfig, SWT.RADIO | SWT.LEFT);
                    buttonStandardAnalysis.setText(Messages.EntropyUIconfig_4);
                    buttonStandardAnalysis.setSelection(true);
                    buttonStandardAnalysis.setToolTipText(Messages.EntropyUIconfig_26);
                    buttonStandardAnalysis.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            cComboTupelLength.setEnabled(true);
                        }
                    });
                }
                {
                    cComboTupelLength = new Combo(groupAnalysisConfig, SWT.BORDER | SWT.READ_ONLY);
                    for (int i = 1; i <= 30; i++) {
                        cComboTupelLength.add(Messages.EntropyUIconfig_5 + i);
                    }
                    cComboTupelLength.select(4);

                }
                {
                    buttonDeepAnalysis = new Button(groupAnalysisConfig, SWT.RADIO | SWT.LEFT);
                    buttonDeepAnalysis.setText(Messages.EntropyUIconfig_6);
                    buttonDeepAnalysis.setToolTipText(Messages.EntropyUIconfig_27);
                    buttonDeepAnalysis.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            cComboTupelLength.setEnabled(false);
                        }
                    });
                }
                {
                    cLabel3 = new CLabel(groupAnalysisConfig, SWT.NONE);
                    cLabel3.setText(Messages.EntropyUIconfig_7);
                }
                {
                    cComboSignificance = new Combo(groupAnalysisConfig, SWT.BORDER | SWT.READ_ONLY);
                    cComboSignificance.setText(Messages.EntropyUIconfig_8);
                    cComboSignificance.add(Messages.EntropyUIconfig_9);
                    cComboSignificance.add(Messages.EntropyUIconfig_10);
                    cComboSignificance.add(Messages.EntropyUIconfig_11);
                    cComboSignificance.add(Messages.EntropyUIconfig_12);
                    cComboSignificance.add(Messages.EntropyUIconfig_13);
                    cComboSignificance.select(1);
                }
            }
            {
                buttonStart = new Button(this, SWT.PUSH | SWT.CENTER);
                FormData button1LData = new FormData();
                button1LData.width = 231;
                button1LData.height = 23;
                button1LData.left = new FormAttachment(0, 1000, 12);
                button1LData.top = new FormAttachment(0, 1000, 200);
                buttonStart.setLayoutData(button1LData);
                buttonStart.setText(Messages.EntropyUIconfig_22);
                buttonStart.setToolTipText(Messages.EntropyUIconfig_23);
                buttonStart.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent evt) {
                        final int n =
                                buttonDeepAnalysis.getSelection() ? 50 : cComboTupelLength.getSelectionIndex() + 1;
                        final double sig = getSignificance();

                        if (checkEditor()) {
                            String input = getEditorText();
                            editorname = EditorsManager.getInstance().getActiveEditorTitle();
                            if (input.length() >= n) {

                                Job job = new Job(Messages.EntropyUIconfig_14) {
                                    public IStatus run(final IProgressMonitor monitor) {
                                        monitor.beginTask(Messages.EntropyUIconfig_19, 2);

                                        Display.getDefault().asyncExec(new Runnable() {
                                            public void run() {
                                                blockStartButton();
                                            }
                                        });

                                        if (monitor.isCanceled()) {
                                            return Status.CANCEL_STATUS;
                                        }

                                        final EntropyCalc calc =
                                                new EntropyCalc(getEditorText(), n, sig, myModifySettings,
                                                        entropyUIpointer, editorname);

                                        monitor.worked(1);

                                        calc.startCalculator();

                                        Display.getDefault().asyncExec(new Runnable() {
                                            public void run() {
                                                entropyUIpointer.getCompositeResults().printSummary(calc);
                                                entropyUIpointer.getCompositeTable().printEntropyMatrix(calc);
                                                entropyUIpointer.getCMainTabFolder().setSelection(1);
                                            }
                                        });

                                        monitor.worked(2);

                                        monitor.done();

                                        Display.getDefault().asyncExec(new Runnable() {
                                            public void run() {
                                                freeStartButton();
                                            }
                                        });

                                        return Status.OK_STATUS;
                                    }
                                };
                                job.setUser(true);
                                job.schedule();
                            } else {
                                MessageDialog.openInformation(entropyUIpointer.getShell(), Messages.EntropyUIconfig_15,
                                        Messages.EntropyUIconfig_16);
                            }
                        } else {
                            MessageDialog.openInformation(entropyUIpointer.getShell(), Messages.EntropyUIconfig_17,
                                    Messages.EntropyUIconfig_18);
                        }
                    }
                });
            }
            this.layout();
            pack();
        } catch (Exception e) {
            LogUtil.logError(e);
        }
    }

    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private String InputStreamToString(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(EntropyPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(EntropyPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

    private TransformData getWizardSettings(TransformData predefined) {

        ModifyWizard wizard = new ModifyWizard();
        wizard.setPredefinedData(predefined);
        WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
        int result = dialog.open();

        if (result == 0)
            return wizard.getWizardData();
        else
            return predefined;

    }

    private boolean checkEditor() {
        return EditorsManager.getInstance().isEditorOpen();
    }

    private String getEditorText() {
        return InputStreamToString(EditorsManager.getInstance().getActiveEditorContentInputStream());
    }

    private void blockStartButton() {
        buttonStart.setText(Messages.EntropyUIconfig_21);
        buttonStart.setEnabled(false);
    }

    private void freeStartButton() {
        buttonStart.setText(Messages.EntropyUIconfig_22);
        buttonStart.setEnabled(true);
    }

    protected void setEntropyUIpointer(EntropyUI pointer) {
        this.entropyUIpointer = pointer;
    }

    private double getSignificance() {
        double result = 0;
        switch (cComboSignificance.getSelectionIndex()) {
            case 0:
                return result;
            case 1:
                result = 0.001;
                return result;
            case 2:
                result = 0.0025;
                return result;
            case 3:
                result = 0.005;
                return result;
            case 4:
                result = 0.01;
                return result;
            default:
                return result;
        }
    }

}
