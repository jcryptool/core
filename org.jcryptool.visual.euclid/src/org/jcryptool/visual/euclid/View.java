// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2014, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.euclid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.euclid.handler.CommandState.State;
import org.jcryptool.visual.euclid.handler.CommandState.Variable;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.jcryptool.visual.euclid.handler.CommandStateChanger;

/**
 * @author Felix Eckardt
 */
public class View extends ViewPart {
	public View() {
		
	}

	/*
	 * name_1 refers to the reciprocal subtraction name_2 refers to the extended
	 * euclidean algorithm
	 */
	private Composite parent;
	private ScrolledComposite scrolledComposite;
	private TabFolder tabFolder;
	
	private TabItem tbtmEuclidean;
	private ReciprocSubtraction subtraction;

	private TabItem tbtmXEuclidean;
	private ExtendedEuclid extendedEuclid;

	private CommandStateChanger csc = new CommandStateChanger();


	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		parent.setLayout(new GridLayout(1, false));

		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
		
		
		// 1st tab item
		tbtmEuclidean = new TabItem(tabFolder, SWT.NONE);
		tbtmEuclidean.setText(Messages.Euclid_Euclidean);
		subtraction = new ReciprocSubtraction(tabFolder, SWT.NONE, this);
		tbtmEuclidean.setControl(subtraction);

		
		// 2nd tab item
		tbtmXEuclidean = new TabItem(tabFolder, SWT.NONE);
		tbtmXEuclidean.setText(Messages.Euclid_XEuclidean);
		extendedEuclid = new ExtendedEuclid(tabFolder, SWT.NONE, this);
		tbtmXEuclidean.setControl(extendedEuclid);
		
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tabFolder.getSelectionIndex() == 0 && extendedEuclid.getState() == 5) {
					enableSafeButtons();
				}
			}
		});

		scrolledComposite.setContent(tabFolder);
		scrolledComposite.setMinSize(tabFolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));	
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT.H_SCROLL.
		SmoothScroller.scrollSmooth(scrolledComposite);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, EuclidPlugin.PLUGIN_ID + ".view"); //$NON-NLS-1$
	}

	public void reset() {
		subtraction.completeReset();
		extendedEuclid.completeReset();
		
		disableSafeButtons();
		
		tabFolder.setSelection(0);
	}
	
	public void enableSafeButtons() {
		csc.changeCommandState(Variable.CSV, State.ENABLED);
		csc.changeCommandState(Variable.TEX, State.ENABLED);
	}
	
	public void disableSafeButtons() {
		csc.changeCommandState(Variable.CSV, State.DISABLED);
		csc.changeCommandState(Variable.TEX, State.DISABLED);
	}

	@Override
	public void setFocus() {
		parent.setFocus();
	}
	
	public ExtendedEuclid getExtendedEuclid() {
		return extendedEuclid;
	}
}
