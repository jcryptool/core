//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.freqanalysis.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * @author SLeischnig
 *
 */

public class FreqAnalysisUI extends org.eclipse.swt.widgets.Composite {
    private Group group1;
    private Button button2;
    private Button fakebtn;
    private Button button1;
    SimpleAnalysisUI C1;
    FullAnalysisUI C2;

    public FreqAnalysisUI(final org.eclipse.swt.widgets.Composite parent, final int style) {
        super(parent, style);
        initGUI();

        // hideObject(C1, !button1.getSelection());
        // hideObject(C2, button1.getSelection());
        // this.layout();
    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout();
            thisLayout.makeColumnsEqualWidth = true;
            this.setLayout(thisLayout);
            this.setSize(525, 237);
            group1 = new Group(this, SWT.NONE);
            GridLayout group1Layout = new GridLayout();
            group1Layout.makeColumnsEqualWidth = true;
            group1Layout.numColumns = 2;
            group1.setLayout(group1Layout);
            GridData group1LData = new GridData();
            group1LData.grabExcessHorizontalSpace = true;
            group1LData.horizontalAlignment = GridData.FILL;
            // group1LData.heightHint = 9;
            group1.setLayoutData(group1LData);
            group1.setText(Messages.FreqAnalysisUI_usagedescisionlabel);
            button1 = new Button(group1, SWT.RADIO | SWT.LEFT);
            GridData button1LData = new GridData();
            button1LData.grabExcessHorizontalSpace = true;
            button1LData.horizontalAlignment = GridData.FILL;
            button1.setLayoutData(button1LData);
            button1.setText(Messages.FreqAnalysisUI_descision1label);
            button1.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent evt) {
                    switchComposites(evt);
                }
            });
            button1.setSelection(false);
            button2 = new Button(group1, SWT.RADIO | SWT.LEFT);
            GridData button2LData = new GridData();
            button2LData.grabExcessHorizontalSpace = true;
            button2LData.horizontalAlignment = GridData.FILL;
            button2.setLayoutData(button2LData);
            button2.setText(Messages.FreqAnalysisUI_descision2label);
            button2.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent evt) {
                    switchComposites(evt);
                }
            });
            button2.setSelection(false);
            fakebtn = new Button(group1, SWT.RADIO | SWT.LEFT);
            fakebtn.setText("button3"); //$NON-NLS-1$
            GridData fakebtnLData = new GridData();
            fakebtnLData.exclude = true;
            fakebtn.setLayoutData(fakebtnLData);
            fakebtn.setSelection(true);
            C1 = new SimpleAnalysisUI(this, SWT.NONE);
            GridLayout C1Layout = new GridLayout();
            C1Layout.makeColumnsEqualWidth = true;
            GridData C1LData = new GridData();
            C1LData.grabExcessHorizontalSpace = true;
            C1LData.grabExcessVerticalSpace = true;
            C1LData.horizontalAlignment = GridData.FILL;
            C1LData.verticalAlignment = GridData.FILL;
            C1LData.exclude = true;
            C1.setLayoutData(C1LData);
            C1.setLayout(C1Layout);
            C1.setVisible(false);
            C2 = new FullAnalysisUI(this, SWT.NONE);
            GridLayout C2Layout = new GridLayout();
            C2Layout.makeColumnsEqualWidth = true;
            GridData C2LData = new GridData();
            C2LData.grabExcessHorizontalSpace = true;
            C2LData.grabExcessVerticalSpace = true;
            C2LData.verticalAlignment = GridData.FILL;
            C2LData.horizontalAlignment = GridData.FILL;
            C2LData.exclude = true;
            C2.setLayoutData(C2LData);
            C2.setLayout(C2Layout);
            C2.setVisible(false);
            this.layout();
        } catch (Exception e) {
            LogUtil.logError(FreqAnalysisPlugin.PLUGIN_ID, e);
        }
    }

    /**
     * Excludes a control from Layout calculation
     *
     * @param that
     * @param hideit
     */
    private void hideObject(final Control that, final boolean hideit) {
        GridData GData = (GridData) that.getLayoutData();
        GData.exclude = hideit;
        that.setVisible(!hideit);
        Control[] myArray = {that};
        layout(myArray);
    }

    /**
     * sets, whether teh simple, or the extended view is shown
     */
    private void switchComposites(final SelectionEvent evt) {
        if (button1.getSelection() || button2.getSelection()) {
            hideObject(C1, !button1.getSelection());
            hideObject(C2, button1.getSelection());
        }
    }

    /**
     * The remote control procedure - control this plugin by importing the IFreqAnalysisInterface,
     * and access the view's execute procedure.
     *
     * @param simpleView whether the simple or the extended view is shown (true - simple, false -
     *        extended)
     * @param keyLength sets the key length
     * @param keyPos sets the offset
     * @param overlayIndex sets the selection index of the reference overlay text combo box.
     *        simpleView=false only!
     * @param resetShift resets the dragging of the graph
     * @param executeCalc whether a calculation has now to be executed
     * @param whichTab selects, which tab has to be shown. simpleView=false only!
     * @param activateOverlay activates the overlay. simpleView=false only!
     */
    public final void execute(final boolean simpleView, final int keyLength, final int keyPos,
            final int overlayIndex, final boolean resetShift, final boolean executeCalc,
            final boolean whichTab, final boolean activateOverlay) {
        if (simpleView) {
            button1.setSelection(true);
            button2.setSelection(false);
            fakebtn.setSelection(false);
            switchComposites(null);
        } else {
            button1.setSelection(false);
            button2.setSelection(true);
            fakebtn.setSelection(false);
            switchComposites(null);
        }

        if (simpleView) {
            C1.execute(keyLength, keyPos, resetShift, executeCalc);
        } else {
            C2.execute(keyLength, keyPos, overlayIndex, resetShift, executeCalc, whichTab,
                    activateOverlay);
        }

    }

}