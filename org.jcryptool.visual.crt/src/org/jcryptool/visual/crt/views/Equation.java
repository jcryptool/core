// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
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
	private VerifyListener aTextfieldVerifyListiner;
	private VerifyListener mTextfieldVerifyListiner;

	public Equation(int equationIndex, Equations equationSet, Composite equationGroup, CRTGroup mainGroup) {
		this.mainGroup = mainGroup;
		this.instance = this;
		this.equations = equationSet;
		this.equationGroup = equationGroup;
	}

	public void addEquationToGroup() {
		xLabel = new Label(equationGroup, SWT.NONE);
		xLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		xLabel.setText("x");

		congruenceLabel = new Label(equationGroup, SWT.NONE);
		congruenceLabel.setLayoutData(new GridData());
		congruenceLabel.setText(uCongruence);

		textfieldA = new Text(equationGroup, SWT.BORDER);
		final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_text.widthHint = 80;
		textfieldA.setLayoutData(gd_text);
		aTextfieldVerifyListiner = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
				 */
				if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127) {
					if (textfieldA.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (textfieldA.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}

		};
		textfieldA.addVerifyListener(aTextfieldVerifyListiner);

		modLabel = new Label(equationGroup, SWT.NONE);
		modLabel.setText("mod");

		textfieldM = new Text(equationGroup, SWT.BORDER);
		final GridData gd_text_1 = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text_1.widthHint = 80;
		textfieldM.setLayoutData(gd_text_1);
		mTextfieldVerifyListiner = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				/*
				 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
				 */
				if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127) {
					if (textfieldM.getText().length() == 0 && e.text.compareTo("0") == 0) {
						e.doit = false;
					} else if (textfieldM.getSelection().x == 0 && e.keyCode == 48) {
						e.doit = false;
					} else {
						e.doit = true;
					}
				} else {
					e.doit = false;
				}
			}

		};
		textfieldM.addVerifyListener(mTextfieldVerifyListiner);

		plusButton = new Button(equationGroup, SWT.NONE);
		plusButton.setLayoutData(new GridData(30, 20));
		plusButton.setText("+");
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
					equation.textfieldA.removeVerifyListener(equation.aTextfieldVerifyListiner);
					equation.textfieldM.removeVerifyListener(equation.mTextfieldVerifyListiner);
				}
				for (int i = equationSet.size() - 1; i > tmpIndex; i--) {
					equationSet.get(i).setTextfieldA(equationSet.get(i - 1).getTextfieldA());
					equationSet.get(i).setTextfieldM(equationSet.get(i - 1).getTextfieldM());
				}
				equationSet.get(tmpIndex + 1).setTextfieldA("");
				equationSet.get(tmpIndex + 1).setTextfieldM("");

				for (Equation equation : equationSet) {
					equation.textfieldA.addVerifyListener(equation.aTextfieldVerifyListiner);
					equation.textfieldM.addVerifyListener(equation.mTextfieldVerifyListiner);
				}
				equationGroup.pack();
				if (equationSet.size() <= 4) {
					ScrolledComposite scroll = (ScrolledComposite) equationGroup.getParent();
					scroll.setExpandVertical(true);
				} else {
					ScrolledComposite scroll = (ScrolledComposite) equationGroup.getParent();
					scroll.setExpandVertical(false);
				}

				if (equations.getNumberOfEquations() < 3) {
					minusButton.setEnabled(false);
				} else {
					for (Equation equation : equationSet) {
						equation.minusButton.setEnabled(true);
					}
				}

				// equationGroup.setSize(328, 175);

			}

		});

		minusButton = new Button(equationGroup, SWT.NONE);
		minusButton.setLayoutData(new GridData(30, 20));
		minusButton.setText("-");
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
					equationGroup.pack();
					if (equationSet.size() <= 5) {
						ScrolledComposite scroll = (ScrolledComposite) equationGroup.getParent();
						scroll.setExpandVertical(true);
					} else {
						ScrolledComposite scroll = (ScrolledComposite) equationGroup.getParent();
						scroll.setExpandVertical(false);
					}
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
		textfieldM.removeVerifyListener(mTextfieldVerifyListiner);
	}

	public void addTextfieldMVerifyListener() {
		textfieldM.addVerifyListener(mTextfieldVerifyListiner);
	}
}
