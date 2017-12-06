// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.aco.controller.AntColEventController;
import org.jcryptool.visual.aco.model.CommonModel;

/**
 * Klasse zur Darstellung der Ablaeufe bei der Kryptanalyse mit Hilfe eines
 * Ameisenalgorithmus. Ist darstellendes Element des Tutorial.
 * 
 * @author Philipp Blohm
 * @version 03.08.07
 * 
 */
public class AntColPherMatrixComposite extends Composite {
	private CommonModel m;
	private Composite matComp;
	private Composite comp;

	/**
	 * Konstruktor erhaelt Model und Parent.
	 * 
	 * @param model
	 * @param c
	 */
	public AntColPherMatrixComposite(CommonModel model, Composite c) {
		super(c, SWT.NONE);
		this.m = model;
		redraw();
	}

	public void redraw() {
		super.redraw();
		if (comp != null) {
			comp.dispose();
		}

		comp = new Composite(this, SWT.NONE);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		comp.setLayout(new GridLayout(1, true));

		matComp = new Composite(comp, SWT.NONE);
		matComp.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, true));
		matComp.setLayout(new GridLayout(m.getSize(), true));
		
		Label label = new Label(matComp, SWT.NONE);
		GridData data = new GridData(SWT.CENTER, SWT.CENTER, false, false,
				m.getSize(), 1);
		label.setLayoutData(data);
		label.setText(Messages.Show_pheromoneMatrix); //$NON-NLS-1$

		double[][] matrix = m.getMatrix();
		for (int row = 0; row < matrix.length; row++) {
			for (int line = 0; line < matrix[row].length; line++) {
				label = new Label(matComp, SWT.NONE);
				data = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
				label.setLayoutData(data);
				if (row == line) {
					label.setText("-"); //$NON-NLS-1$
				} else {
					label.setText("" + Math.round(matrix[row][line] * 10) / (double) 10); //$NON-NLS-1$
				}
			}
		}
		
		String[] text = m.getKnots();

		if (text[0].length() > 0) {
			label = new Label(matComp, SWT.NONE);
			data = new GridData(SWT.CENTER, SWT.CENTER, false, false,
					m.getSize(), 1);
			data.verticalIndent = 30;
			label.setLayoutData(data);
			label.setText(Messages.PherMatrix_knotContent); //$NON-NLS-1$

			for (int line = 0; line < 4; line++) {
				for (int col = 0; col < text.length; col++) {
					label = new Label(matComp, SWT.NONE);
					data = new GridData(SWT.CENTER, SWT.CENTER, false, false,
							1, 1);
					label.setLayoutData(data);

					if (text[col].length() > line) {
						if (line == 3) {
							label.setText("...");
						} else {
							label.setText(Character.toString(text[col]
									.charAt(line)).toUpperCase()); //$NON-NLS-1$
						}
					}
				}
			}
		}
		layout();
	}

	public void addController(AntColEventController reg) {
	}
}
