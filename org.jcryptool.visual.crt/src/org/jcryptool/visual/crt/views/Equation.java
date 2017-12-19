// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.crt.views;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Oryal Inel
 */
public class Equation implements Constants {
	private CRTGroup mainGroup;

	private Equation instance;
	private final Composite equationGroup;
	private Equations equations;

	private Label xLabel;
	private Label congruenceLabel;
	private Label modLabel;
	private Text textfieldA;
	private Text textfieldM;
	private Button plusButton;
	private Button minusButton;
	private VerifyListener aTextfieldVerifyListener;
	private VerifyListener mTextfieldVerifyListener;

	public Equation(int equationIndex, Equations equationSet, Composite equationGroup, CRTGroup mainGroup) {
		this.mainGroup = mainGroup;
		this.instance = this;
		this.equations = equationSet;
		this.equationGroup = equationGroup;
	}

	public void addEquationToGroup() {
		xLabel = new Label(equationGroup, SWT.NONE);
		xLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		xLabel.setText("x"); //$NON-NLS-1$

		congruenceLabel = new Label(equationGroup, SWT.NONE);
		congruenceLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		congruenceLabel.setText(uCongruence);

		textfieldA = new Text(equationGroup, SWT.BORDER);
		textfieldA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		aTextfieldVerifyListener = new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
				 */
				if (e.text.matches("^[0-9]*") || e.keyCode == 8 || e.keyCode == 127) { //$NON-NLS-1$
					e.doit = true;
				} else {
					e.doit = false;
				}
			}

		};
		textfieldA.addVerifyListener(aTextfieldVerifyListener);

		modLabel = new Label(equationGroup, SWT.NONE);
		modLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		modLabel.setText("mod"); //$NON-NLS-1$

		textfieldM = new Text(equationGroup, SWT.BORDER);
		textfieldM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		mTextfieldVerifyListener = new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
				 */
				// Nur 0 bis 9 und BACKSPACE und DEL zulassen
				if (e.text.matches("^[0-9]*") || e.keyCode == 8 || e.keyCode == 127) { //$NON-NLS-1$
					// Keine 0 am anfang zulassen
					if (textfieldM.getSelection().x == 0 && e.text.matches("^[0]\\d*")) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}

		};
		textfieldM.addVerifyListener(mTextfieldVerifyListener);

		plusButton = new Button(equationGroup, SWT.NONE);
		plusButton.setLayoutData(new GridData(30, 25));
		plusButton.setText("+"); //$NON-NLS-1$
		plusButton.setToolTipText(Messages.Equation_0);
		plusButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (CRTGroup.execute) {
					mainGroup.reset();
				}
				Vector<Equation> equationSet = equations.getEquationSet();

				int tmpIndex = equationSet.indexOf(instance);
				equations.createEquation(tmpIndex + 1, equationGroup, mainGroup);

				for (Equation equation : equationSet) {
					equation.textfieldA.removeVerifyListener(equation.aTextfieldVerifyListener);
					equation.textfieldM.removeVerifyListener(equation.mTextfieldVerifyListener);
				}
				for (int i = equationSet.size() - 1; i > tmpIndex; i--) {
					equationSet.get(i).setTextfieldA(equationSet.get(i - 1).getTextfieldA());
					equationSet.get(i).setTextfieldM(equationSet.get(i - 1).getTextfieldM());
				}
				equationSet.get(tmpIndex + 1).setTextfieldA(""); //$NON-NLS-1$
				equationSet.get(tmpIndex + 1).setTextfieldM(""); //$NON-NLS-1$

				for (Equation equation : equationSet) {
					equation.textfieldA.addVerifyListener(equation.aTextfieldVerifyListener);
					equation.textfieldM.addVerifyListener(equation.mTextfieldVerifyListener);
				}
				
				//Resize the scrolledComposite
				equationGroup.layout();
				ScrolledComposite scroll = (ScrolledComposite) equationGroup.getParent();
				scroll.setMinSize(equationGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));

				if (equations.getNumberOfEquations() < 3) {
					minusButton.setEnabled(false);
				} else {
					for (Equation equation : equationSet) {
						equation.minusButton.setEnabled(true);
					}
				}
			}
		});

		minusButton = new Button(equationGroup, SWT.NONE);
		minusButton.setLayoutData(new GridData(30, 25));
		minusButton.setText("-"); //$NON-NLS-1$
		minusButton.setToolTipText(Messages.Equation_1);
		if (equations.getNumberOfEquations() < 3) {
			minusButton.setEnabled(false);
		}
		minusButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				Vector<Equation> equationSet = equations.getEquationSet();
				if (CRTGroup.execute) {
					mainGroup.reset();
				}
				if (equations.getNumberOfEquations() > 2) {
					dispose();

					equations.removeEquation(instance);

					if (equationSet.size() <= 2) {
						equationSet.firstElement().setEnableMinusButton(false);
						equationSet.get(1).setEnableMinusButton(false);
					}
					equationGroup.layout();
					ScrolledComposite scroll = (ScrolledComposite) equationGroup.getParent();
					scroll.setMinSize(equationGroup.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			}
		});

	}

	public String getTextfieldA() {
		return textfieldA.getText();
	}

	public void setTextfieldA(String textfieldA) {
		this.textfieldA.setText(textfieldA);
	}

	public String getTextfieldM() {
		return textfieldM.getText();
	}

	public void setTextfieldM(String textfieldM) {
		this.textfieldM.setText(textfieldM);
	}

	public void dispose() {
		xLabel.dispose();
		congruenceLabel.dispose();
		textfieldA.dispose();
		modLabel.dispose();
		textfieldM.dispose();
		plusButton.dispose();
		minusButton.dispose();
	}

	public void setEnablePlusButton(boolean enabled) {
		this.plusButton.setEnabled(enabled);
	}

	public void setEnableMinusButton(boolean enabled) {
		this.minusButton.setEnabled(enabled);
	}

	public void setEquationEnable(boolean enable) {
		this.textfieldA.setEditable(enable);
		this.textfieldA.setEnabled(enable);

		this.textfieldM.setEditable(enable);
		this.textfieldM.setEnabled(enable);

		// this.plusButton.setEnabled(false);
		// this.minusButton.setEnabled(false);

	}

	public void removetextfieldMVerifyListener() {
		textfieldM.removeVerifyListener(mTextfieldVerifyListener);
	}

	public void addTextfieldMVerifyListener() {
		textfieldM.addVerifyListener(mTextfieldVerifyListener);
	}
}
