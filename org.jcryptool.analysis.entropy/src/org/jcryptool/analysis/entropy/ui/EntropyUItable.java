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

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.analysis.entropy.calc.EntropyCalc;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class EntropyUItable extends Composite {
	private Table tableEnt;
	private DecimalFormat df;
	private DecimalFormat dfp;

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
		EntropyUItable inst = new EntropyUItable(shell, SWT.NULL);
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

	public EntropyUItable(Composite parent, int style) {
		super(parent, style);
		df = new DecimalFormat(Messages.EntropyUItable_0);
		dfp = new DecimalFormat(Messages.EntropyUItable_1);
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(new FormLayout());
			{
				FormData tableEntLData = new FormData();
				tableEntLData.width = 188;
				tableEntLData.height = 225;
				tableEntLData.left = new FormAttachment(0, 1000, 12);
				tableEntLData.top = new FormAttachment(0, 1000, 12);
				tableEntLData.right = new FormAttachment(1000, 1000, -12);
				tableEntLData.bottom = new FormAttachment(1000, 1000, -12);
				tableEnt = new Table(this, SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
				tableEnt.addSelectionListener(new SelectionAdapter() {
				    @Override
				    public void widgetSelected(SelectionEvent e) {
				        tableEnt.deselectAll();
				    }
                });
				tableEnt.setLayoutData(tableEntLData);
				tableEnt.setLinesVisible(true);
				tableEnt.setHeaderVisible(true);
				String[] titles = { Messages.EntropyUItable_2, Messages.EntropyUItable_3, Messages.EntropyUItable_4, Messages.EntropyUItable_5, Messages.EntropyUItable_6,
						Messages.EntropyUItable_7 };
				for (int i = 0; i < titles.length; i++) {
					TableColumn column = new TableColumn(tableEnt, SWT.NONE);
					column.setText(titles[i]);
				}
				for (int i = 0; i < titles.length; i++) {
					tableEnt.getColumn(i).pack();
				}
			}
			this.layout();
			pack();
		} catch (Exception e) {
            LogUtil.logError(e);
		}
	}

	public void printEntropyMatrix(EntropyCalc eC) {
		tableEnt.removeAll();
		double[][] matrix = eC.getResultMatrix();
		for (int i = 0; i < eC.getActualN(); i++) {
			String num = ((Integer) (i + 1)).toString();
			TableItem item = new TableItem(tableEnt, SWT.NONE);
			item.setText(0, num); // n
			item.setText(1, df.format(matrix[i][1])); // Gn
			if (i != 0)
				item.setText(2, dfp.format((matrix[i][9]))); // growth
			item.setText(3, df.format(matrix[i][0])); // Fn
			item.setText(4, df.format(matrix[i][6])); // Gn / n
			item.setText(5, dfp.format(matrix[i][8])); // redundancy
		}

		for (int i = 0; i < 6; i++) {
			tableEnt.getColumn(i).pack();
			tableEnt.getColumn(i).setAlignment(SWT.CENTER);
		}
	}

}
