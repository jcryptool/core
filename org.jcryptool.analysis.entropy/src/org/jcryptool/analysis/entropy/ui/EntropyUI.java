// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.entropy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class EntropyUI extends Composite {
	private CTabFolder cMainTabFolder;
	private CTabItem cTabConfig;
	private CTabItem cTabResult;
	private CTabItem cTabDetails;
	private EntropyUIresults compositeResults;
	private EntropyUIconfig compositeConfig;
	private EntropyUItable compositeTable;

	/**
	 * Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	 */
	protected void checkSubclass() {
	}

	public CTabFolder getCMainTabFolder() {
		return cMainTabFolder;
	}

	/**
	 * Auto-generated method to display this org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		EntropyUI inst = new EntropyUI(shell, SWT.NULL);
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

	public EntropyUI(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(new FormLayout());
			{
				cMainTabFolder = new CTabFolder(this, SWT.NONE);
				FormData cMainTabFolderLData = new FormData();
				cMainTabFolderLData.width = 622;
				cMainTabFolderLData.height = 288;
				cMainTabFolderLData.left = new FormAttachment(0, 1000, 0);
				cMainTabFolderLData.top = new FormAttachment(0, 1000, 0);
				cMainTabFolderLData.bottom = new FormAttachment(1000, 1000, 0);
				cMainTabFolderLData.right = new FormAttachment(1000, 1000, 0);
				cMainTabFolder.setLayoutData(cMainTabFolderLData);
				{
					cTabConfig = new CTabItem(cMainTabFolder, SWT.NONE);
					cTabConfig.setText(Messages.EntropyUI_0);
					{
						compositeConfig = new EntropyUIconfig(cMainTabFolder, SWT.NONE);
						GridLayout composite1Layout = new GridLayout();
						composite1Layout.makeColumnsEqualWidth = true;
						cTabConfig.setControl(compositeConfig);
					}
				}
				{
					cTabResult = new CTabItem(cMainTabFolder, SWT.NONE);
					cTabResult.setText(Messages.EntropyUI_1);
					{
						compositeResults = new EntropyUIresults(cMainTabFolder, SWT.NONE);
						GridLayout compositeResultsLayout = new GridLayout();
						compositeResultsLayout.makeColumnsEqualWidth = true;
						cTabResult.setControl(compositeResults);
					}
				}
				{
					cTabDetails = new CTabItem(cMainTabFolder, SWT.NONE);
					cTabDetails.setText(Messages.EntropyUI_2);
					{
						compositeTable = new EntropyUItable(cMainTabFolder, SWT.NONE);
						GridLayout compositeTableLayout = new GridLayout();
						compositeTableLayout.makeColumnsEqualWidth = true;
						cTabDetails.setControl(compositeTable);
					}
				}
				cMainTabFolder.setSelection(0);
			}
			this.layout();
			pack();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		compositeConfig.setEntropyUIpointer(this);
	}

	public EntropyUI getThis() {
		return this;
	}

	public EntropyUIresults getCompositeResults() {
		return compositeResults;
	}

	public EntropyUIconfig getCompositeConfig() {
		return compositeConfig;
	}

	public EntropyUItable getCompositeTable() {
		return compositeTable;
	}

}
