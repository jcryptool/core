//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;

/**
 * @author SLeischnig
 *
 */

public class FreqAnalysisUI extends Composite {
	private Group group1;
	private Button button2;
	private Button button1;
	private Composite headerComposite;
	private Label label;
	SimpleAnalysisUI C1;
	FullAnalysisUI C2;

	public FreqAnalysisUI(final Composite parent, final int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			setLayout(new GridLayout());
			headerComposite = new Composite(this, SWT.NONE);
			headerComposite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			headerComposite.setLayout(new GridLayout(1, false));
			headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

			label = new Label(headerComposite, SWT.NONE);
			label.setFont(FontService.getHeaderFont());
			label.setText(Messages.FreqAnalysisUI_frequency_analysis);
			label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

			group1 = new Group(this, SWT.NONE);
			group1.setLayout(new GridLayout(2, true));
			group1.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
			group1.setText(Messages.FreqAnalysisUI_usagedescisionlabel);

			button1 = new Button(group1, SWT.RADIO);
			button1.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
			button1.setText(Messages.FreqAnalysisUI_descision1label);
			button1.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					switchComposites(evt);
				}
			});
			button1.setSelection(false);

			button2 = new Button(group1, SWT.RADIO | SWT.LEFT);
			button2.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
			button2.setText(Messages.FreqAnalysisUI_descision2label);
			button2.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					switchComposites(evt);
				}
			});
			button2.setSelection(false);

			C1 = new SimpleAnalysisUI(this, SWT.NONE);
			GridData C1LData = new GridData(SWT.FILL, SWT.FILL, true, true);
			C1LData.exclude = true;
			C1.setLayoutData(C1LData);
			C1.setLayout(new GridLayout());
			C1.setVisible(false);

			C2 = new FullAnalysisUI(this, SWT.NONE);
			GridData C2LData = new GridData(SWT.FILL, SWT.FILL, true, true);
			C2LData.exclude = true;
			C2.setLayoutData(C2LData);
			C2.setLayout(new GridLayout());
			C2.setVisible(false);
			layout();
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
		Control[] myArray = { that };
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
	 * The remote control procedure - control this plugin by importing the
	 * IFreqAnalysisInterface, and access the view's execute procedure.
	 *
	 * @param simpleView      whether the simple or the extended view is shown (true
	 *                        - simple, false - extended)
	 * @param keyLength       sets the key length
	 * @param keyPos          sets the offset
	 * @param overlayIndex    sets the selection index of the reference overlay text
	 *                        combo box. simpleView=false only!
	 * @param resetShift      resets the dragging of the graph
	 * @param executeCalc     whether a calculation has now to be executed
	 * @param whichTab        selects, which tab has to be shown. simpleView=false
	 *                        only!
	 * @param activateOverlay activates the overlay. simpleView=false only!
	 */
	public final void execute(final boolean simpleView, final int keyLength, final int keyPos, final int overlayIndex,
			final boolean resetShift, final boolean executeCalc, final boolean whichTab,
			final boolean activateOverlay) {
		if (simpleView) {
			button1.setSelection(true);
			button2.setSelection(false);
			switchComposites(null);
		} else {
			button1.setSelection(false);
			button2.setSelection(true);
			switchComposites(null);
		}

		if (simpleView) {
			C1.execute(keyLength, keyPos, resetShift, executeCalc);
		} else {
			C2.execute(keyLength, keyPos, overlayIndex, resetShift, executeCalc, whichTab, activateOverlay);
		}

	}

}