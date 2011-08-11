// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.aco.gui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.aco.tutorial.Model;

/**
 * Die Klasse stellt das Feld mit der Funktionalitaet beim Tutorial zur
 * Kryptanalyse von Transpositionschiffren mit Hilfe eines Ameisenalgorithmus
 * dar. Mit ihrer Hilfe ist es dem Anwender moeglich die Parameter des
 * Algorithmus zu veraendern und so interaktiv auf die Ablaeufe einzuwirken.
 *
 * @author Philipp Blohm
 * @version 03.08.07
 *
 */
public class Func extends Composite implements Observer {
	private Model m;
	private int nr;

	private Button set;
	private Button go;
	private Button all;
	private Button cb;
	private Button weiter;
	private Button reset;

	private View parentView;
	private Button repeat;
	private Slider slider;
	private Text txtperm;
	private Group groupSteps;
	private Group groupSettings;
	private Group groupGeneral;
	private Button selectSingle;
	private Button selectMultiple;
	private boolean singleStep = true;
	private Group groupAnalyse;
	private Button selectAnalyse;
	private Button selectEncrypt;
	private Text txtCipher;
	private Slider sliderAnalyse;
	private Text txt;
	private Label labelSlider;

	/**
	 * Konstruktor. Erhaelt das Model, das die Daten des Tutorials verwaltet und
	 * das Composite an das das Func-Objekt angehaengt werden soll.
	 *
	 * @param model
	 *            Model des Tutorials
	 * @param c
	 *            Parent
	 */
	public Func(Model model, Composite c, View parentView) {
		super(c, SWT.BORDER);
		this.m = model;
		this.parentView = parentView;
		nr = -1;
		setLayout(new GridLayout(1, false));
	}

	/**
	 * Update-Methode reagiert auf Aenderungen im Model und passt die
	 * Funktionalitaet daran an, an welcher Stelle sich der Anwender innerhalb
	 * des Tutorials befindet.
	 */
	public void update(Observable o, Object arg) {
		switch (m.getNr()) { // Fallunterscheidung
		case 0:
			step0();
			break;
		case 1:
			step1();
			break;
		case 2:
			step2();
			break;
		case 3:
			step3();
			break;
		case 4:
			step4();
			break;
		default:
			LogUtil.logWarning(Messages.getString("Func.error")); //$NON-NLS-1$
		}
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 4 des Tutorials
	 *
	 */
	private void step4() {
		if (nr != 4) {
			if (isSingleStepSelected()) {
				set.setEnabled(true);
				go.setEnabled(false);
				all.setEnabled(false);
				weiter.setEnabled(false);
			}
			nr = m.getNr();

			selectSingle.setEnabled(true);
			selectMultiple.setEnabled(true);
		}
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 3 des Tutorials
	 *
	 */
	private void step3() {
		if (nr != 3) {
			if (isSingleStepSelected()) {
				go.setEnabled(false);
				weiter.setEnabled(true);
				all.setEnabled(false);
			}
			nr = m.getNr();
		}
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 2 des Tutorials
	 *
	 */
	private void step2() {
		if (nr != 2) {
			if (isSingleStepSelected()) {
				set.setEnabled(false);
				go.setEnabled(true);
				all.setEnabled(true);
				weiter.setEnabled(false);
			}
			nr = m.getNr();
			selectSingle.setEnabled(false);
			selectMultiple.setEnabled(false);
		}
	}

	private void selectSingleStep(boolean single) {
		if (m.getNr() == 1 || m.getNr() == 4)
			singleStep = single;
	}

	private boolean isSingleStepSelected() {
		return singleStep;
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 1 des Tutorials
	 *
	 */
	private void step1() {
		if (nr == 0) {
			removeAll();
			// Initialisierung der Variablen, anhaengen von Listenern

			groupGeneral = new Group(this, SWT.NONE);
			groupGeneral.setText(Messages.getString("Func.general")); //$NON-NLS-1$
			groupGeneral.setLayout(new GridLayout(1, false));
			groupGeneral.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));

			// Klartext-Label
			Label lab = new Label(groupGeneral, SWT.FILL);
			lab.setText(Messages.getString("Func.plaintext")); //$NON-NLS-1$
			lab.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

			// Klartext-Textfeld
			Text txt = new Text(groupGeneral, SWT.BORDER);
			if (m.getAnalyse())
				txt.setEchoChar('?');
			txt.setText(m.getText().toLowerCase());
			txt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txt.setEditable(false);

			// Ciphertext-Label
			lab = new Label(groupGeneral, SWT.FILL);
			lab.setText(Messages.getString("Func.ciphertext")); //$NON-NLS-1$
			lab.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

			// Ciphertext-Textfeld
			txt = new Text(groupGeneral, SWT.BORDER);
			txt.setText(m.getCipher().toLowerCase());
			txt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txt.setEditable(false);

			// Schluessel-Label
			lab = new Label(groupGeneral, SWT.FILL);
			lab.setText(Messages.getString("Func.keytext")); //$NON-NLS-1$
			lab.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

			// Schluessel-Textfeld
			txt = new Text(groupGeneral, SWT.BORDER);
			String s = ""; //$NON-NLS-1$
			for (int i : m.getPerm())
				s += "," + (i + 1); //$NON-NLS-1$if(m.getAnalyse())
			if (m.getAnalyse())
				txt.setEchoChar('?');
			txt.setText(s.replaceFirst(",", "")); //$NON-NLS-1$ //$NON-NLS-2$
			txt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txt.setEditable(false);

			groupSteps = new Group(this, SWT.NONE);
			groupSteps.setText(Messages.getString("Func.antAnalysis")); //$NON-NLS-1$
			groupSteps.setLayout(new GridLayout(2, false));
			groupSteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));

			selectSingle = new Button(groupSteps, SWT.RADIO);
			selectSingle.setSelection(true);
			selectSingle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 2, 1));
			selectSingle.setText(Messages.getString("Func.oneStep")); //$NON-NLS-1$
			selectSingle.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				public void widgetSelected(SelectionEvent e) {
					if (!isSingleStepSelected()) {
						set.setEnabled(true);
						go.setEnabled(false);
						all.setEnabled(false);
						if (m.getNr() == 1)
							weiter.setEnabled(true);
						else
							weiter.setEnabled(false);
						repeat.setEnabled(false);
						selectSingleStep(true);
					}

					selectSingle.setSelection(isSingleStepSelected());
					selectMultiple.setSelection(!isSingleStepSelected());
				}
			});

			GridData gridData = new GridData(SWT.LEFT, SWT.FILL, false, false);
			gridData.widthHint = 10;
			Label space;

			space = new Label(groupSteps, SWT.NONE);
			space.setText("   "); //$NON-NLS-1$
			space.setSize(10, 10);

			// Setzen-Button
			set = new Button(groupSteps, SWT.PUSH);
			set.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			set.setText(Messages.getString("Func.newAnt")); //$NON-NLS-1$
			set.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					m.neuSetzen();
				}
			});

			space = new Label(groupSteps, SWT.NONE);
			space.setText("   "); //$NON-NLS-1$

			// Schritt-Button
			go = new Button(groupSteps, SWT.PUSH);
			go.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			go.setText(Messages.getString("Func.step")); //$NON-NLS-1$
			go.setEnabled(false);
			go.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (!m.getWorking())
						m.step();
				}
			});

			space = new Label(groupSteps, SWT.NONE);
			space.setText("   "); //$NON-NLS-1$

			// Durchlauf-Button
			all = new Button(groupSteps, SWT.PUSH);
			all.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			all.setText(Messages.getString("Func.cycle")); //$NON-NLS-1$
			all.setEnabled(false);
			all.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (!m.getWorking())
						m.steps();
				}
			});

			space = new Label(groupSteps, SWT.NONE);
			space.setText("   "); //$NON-NLS-1$

			// Weiter-Button
			weiter = new Button(groupSteps, SWT.PUSH);
			weiter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			weiter.setText(Messages.getString("Func.proceed")); //$NON-NLS-1$
			weiter.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (!m.getWorking())
						m.inc();
				}
			});
			weiter.setEnabled(true);

			selectMultiple = new Button(groupSteps, SWT.RADIO);
			selectMultiple.setSelection(false);
			selectMultiple.setText(Messages.getString("Func.allSteps")); //$NON-NLS-1$
			selectMultiple.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 2, 1));
			selectMultiple.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				public void widgetSelected(SelectionEvent e) {
					if (isSingleStepSelected()) {
						set.setEnabled(false);
						go.setEnabled(false);
						all.setEnabled(false);
						weiter.setEnabled(false);
						repeat.setEnabled(true);
						selectSingleStep(false);
					}

					selectSingle.setSelection(isSingleStepSelected());
					selectMultiple.setSelection(!isSingleStepSelected());
				}
			});

			space = new Label(groupSteps, SWT.NONE);
			space.setText("   "); //$NON-NLS-1$

			// Repeat-Button
			repeat = new Button(groupSteps, SWT.PUSH);
			repeat.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			repeat.setText(Messages.getString("Func.doWithNewAnt")); //$NON-NLS-1$
			repeat.setEnabled(false);
			repeat.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (!m.getWorking()) {
						if (cb.getSelection()) {
							selectSingle.setEnabled(false);
							selectMultiple.setEnabled(false);
							repeat.setEnabled(false);
						}

						m.neuSetzen();
						m.steps();

						// emulates the button clicks at the end of the
						// animation
						Thread t = new Thread(new Runnable() {
							public void run() {
								while (m.getWorking()) {
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
									}
								}
								Display.getCurrent().asyncExec(new Runnable() {
									public void run() {
										step4();
										m.inc();

										if (cb.getSelection()) {
											selectSingle.setEnabled(true);
											selectMultiple.setEnabled(true);
											repeat.setEnabled(true);
										}
									}
								});
							}
						});
						t.start();
					}
				}
			});

			groupSettings = new Group(this, SWT.NONE);
			groupSettings.setText(Messages.getString("Func.settings")); //$NON-NLS-1$
			groupSettings.setLayout(new GridLayout(1, false));
			groupSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));

			// Alpha-Label
			final Label alpha = new Label(groupSettings, SWT.TOP);
			final String str = Messages.getString("Func.alpha"); //$NON-NLS-1$
			alpha.setText(str + " " + m.getAlpha());
			alpha.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			// Alpha-Slider
			final Slider alphaslider = new Slider(groupSettings, SWT.HORIZONTAL);
			alphaslider.setValues(80, 0, 101, 1, 10, 1);
			alphaslider.setEnabled(true);
			alphaslider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));
			alphaslider.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					m.setAlpha((double) alphaslider.getSelection() / 100);
					alpha.setText(str + " " + m.getAlpha());
				}
			});

			// Beta-Label
			final Label beta = new Label(groupSettings, SWT.TOP);
			final String str2 = Messages.getString("Func.beta"); //$NON-NLS-1$
			beta.setText(str2 + " " + m.getBeta());
			beta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			// Beta-Slider
			final Slider betaslider = new Slider(groupSettings, SWT.HORIZONTAL);
			betaslider.setValues(80, 0, 101, 1, 10, 1);
			betaslider.setEnabled(true);
			betaslider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));
			betaslider.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					m.setBeta((double) betaslider.getSelection() / 100);
					beta.setText(str2 + " " + m.getBeta());
				}
			});

			// Verdunstung-Label
			final Label verd = new Label(groupSettings, SWT.TOP);
			final String str3;
			str3 = Messages.getString("Func.evap"); //$NON-NLS-1$
			verd.setText(str3 + " " + m.getVerd());
			verd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			// Verdunstung-Slider
			final Slider verdslider = new Slider(groupSettings, SWT.HORIZONTAL);
			verdslider.setValues(90, 0, 101, 1, 10, 1);
			verdslider.setEnabled(true);
			verdslider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));
			verdslider.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					m.setVerd((double) verdslider.getSelection() / 100);
					verd.setText(str3 + " " + m.getVerd());
				}
			});

			// Animation-Checkbox
			cb = new Button(groupSettings, SWT.CHECK);
			cb.setText(Messages.getString("Func.animation")); //$NON-NLS-1$
			cb.setSelection(true);
			cb.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (!m.getWorking())
						m.setEnAni(cb.getSelection());
					else
						cb.setSelection(!cb.getSelection());
				}
			});

			// Reset-Button
			reset = new Button(this, SWT.PUSH);
			reset.setText(Messages.getString("Func.reset")); //$NON-NLS-1$
			reset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			reset.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (!m.getWorking()) {
						parentView.reset();
					}
				}
			});

			weiter.setEnabled(true);

			nr = m.getNr();
			this.layout();
		} else if (nr != 1) { // wird nach einem Durchgang aufgerufen,
			// wenn man zu Schritt 1 zurueckkehrt
			if (isSingleStepSelected()) {
				set.setEnabled(true);
				go.setEnabled(false);
				all.setEnabled(false);
				weiter.setEnabled(true);
			}
		}
	}

	/**
	 * Setzt die Einstellungen fuer Schritt 0 des Tutorials (Verschluesselung).
	 *
	 */
	private void step0() {
		if (nr != 0) {
			removeAll();

			Group groupEncrypt = new Group(this, SWT.NONE);
			groupEncrypt.setText(Messages.getString("Func.encryption")); //$NON-NLS-1$
			groupEncrypt.setLayout(new GridLayout(1, false));
			groupEncrypt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));

			selectEncrypt = new Button(groupEncrypt, SWT.RADIO);
			selectEncrypt.setText(Messages.getString("Func.analyseCreated")); //$NON-NLS-1$
			selectEncrypt.setSelection(true);
			selectEncrypt.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				public void widgetSelected(SelectionEvent e) {
					selectAnalyse.setSelection(false);
					txtperm.setEnabled(true);
					txt.setEnabled(true);
					slider.setEnabled(true);

					sliderAnalyse.setEnabled(false);
					txtCipher.setEnabled(false);

					checkTextInput();
				}
			});

			// Klartext-Label
			Label lab = new Label(groupEncrypt, SWT.FILL);
			lab.setText(Messages.getString("Func.plaintext")); //$NON-NLS-1$
			lab.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

			// Klartext-Textfeld
			txt = new Text(groupEncrypt, SWT.BORDER);
			txt.setText(Messages.getString("Func.initial_plaintext")); //$NON-NLS-1$
			txt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txt.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					m.setText(txt.getText());
					checkTextInput();
				}
			});

			// Schluessellaenge-Label
			final Label len = new Label(groupEncrypt, SWT.TOP);
			final String str = Messages.getString("Func.keyLength"); //$NON-NLS-1$
			len.setText(str.concat(" 4")); //$NON-NLS-1$
			len.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

			// Schluessellaenge-Slider
			slider = new Slider(groupEncrypt, SWT.HORIZONTAL);
			slider.setValues(4, 3, 6, 1, 1, 1);
			slider.setEnabled(true);
			slider.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			slider.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (slider.getSelection() != m.getSize()) {
						m.setSize(slider.getSelection());
						len.setText(str + " " + m.getSize());
						updatePermutationText();
						checkTextInput();
					}
				}
			});

			// Permutation-Label
			Label perm = new Label(groupEncrypt, SWT.FILL);
			perm.setText(Messages.getString("Func.permutation")); //$NON-NLS-1$
			perm.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

			// Permutation-Eingabefeld
			txtperm = new Text(groupEncrypt, SWT.BORDER);
			txtperm.setTextLimit(15);
			updatePermutationText();
			txtperm.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			txtperm.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					String match = "[1-" + slider.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
					for (int i = 1; i < slider.getSelection(); i++) {
						match += ",[1-" + slider.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (txtperm.getText().matches(match))
						m.setPerm(txtperm.getText());

					checkTextInput();
				}
			});
			txtperm.addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {
					// not too long
					if ((txtperm.getText().length() >= 2 * slider
							.getSelection() - 1) && e.character != '\b')
						e.doit = false;
					// number already set
					if (Character.isDigit(e.character)
							&& txtperm.getText().contains("" + e.character)) //$NON-NLS-1$
						e.doit = false;
					// number out of range
					if (Character.isDigit(e.character)
							&& (Integer.parseInt("" + e.character) > m.getSize() //$NON-NLS-1$
							|| Integer.parseInt("" + e.character) == 0)) //$NON-NLS-1$
						e.doit = false;
					// only digits and commas
					if (!((Character.isDigit(e.character)
							|| (e.character == ',') || (e.character == '\b'))))
						e.doit = false;
				}

				public void keyReleased(KeyEvent e) {
					String match = "[1-" + slider.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
					for (int i = 1; i < slider.getSelection(); i++) {
						match += ",[1-" + slider.getSelection() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (txtperm.getText().matches(match)) {
						checkTextInput();
					} else {
						weiter.setEnabled(false);
					}
				}
			});

			groupAnalyse = new Group(this, SWT.NONE);
			groupAnalyse.setText(Messages.getString("Func.analysis")); //$NON-NLS-1$
			groupAnalyse.setLayout(new GridLayout(1, false));

			selectAnalyse = new Button(groupAnalyse, SWT.RADIO);
			selectAnalyse.setText(Messages.getString("Func.analyseGiven")); //$NON-NLS-1$
			selectAnalyse.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				public void widgetSelected(SelectionEvent e) {
					selectEncrypt.setSelection(false);
					txtperm.setEnabled(false);
					txt.setEnabled(false);
					slider.setEnabled(false);

					sliderAnalyse.setEnabled(true);
					txtCipher.setEnabled(true);

					checkTextInput();
				}
			});

			Label label = new Label(groupAnalyse, SWT.NONE);
			label.setText(Messages.getString("Func.ciphertext")); //$NON-NLS-1$

			txtCipher = new Text(groupAnalyse, SWT.SINGLE | SWT.BORDER);
			txtCipher.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));
			txtCipher.setEnabled(false);
			txtCipher.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					checkTextInput();
				}
			});

			labelSlider = new Label(groupAnalyse, SWT.NONE);
			labelSlider.setText(Messages.getString("Func.keyLength") + " 4"); //$NON-NLS-1$ //$NON-NLS-2$

			sliderAnalyse = new Slider(groupAnalyse, SWT.NONE);
			sliderAnalyse.setValues(4, 3, 6, 1, 1, 1);
			sliderAnalyse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false));
			sliderAnalyse.setEnabled(false);
			sliderAnalyse.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					labelSlider.setText(Messages.getString("Func.keyLength") + " " + sliderAnalyse.getSelection()); //$NON-NLS-1$
					checkTextInput();
				}
			});

			// Weiter-Button
			weiter = new Button(this, SWT.PUSH);
			weiter.setText(Messages.getString("Func.proceedToAnalysis")); //$NON-NLS-1$
			weiter.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
			weiter.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event e) {
					if (selectEncrypt.getSelection()) {
						m.setPerm(txtperm.getText());
						m.setSize(slider.getSelection());
						m.setText(txt.getText());
					} else {
						String perm = "1, 2"; //$NON-NLS-1$
						for (int i = 3; i < sliderAnalyse.getSelection() + 1; i++)
							perm += ", " + i; //$NON-NLS-1$
						m.setPerm(perm);
						m.setSize(sliderAnalyse.getSelection());
						m.setText(txtCipher.getText());
					}
					m.setAnalyse(selectAnalyse.getSelection());
					m.inc();
				}
			});

			layout();

			nr = m.getNr();
		}
	}

	private void checkTextInput() {
		if (m.getText().equals("")) //$NON-NLS-1$
			weiter.setEnabled(false);
		else {
			if (selectAnalyse.getSelection()) {
				if (txtCipher.getText().length() == 0
						|| txtCipher.getText().length()
								% sliderAnalyse.getSelection() != 0)
					weiter.setEnabled(false);
				else
					weiter.setEnabled(true);
			} else
				weiter.setEnabled(true);
		}
	}

	private void updatePermutationText() {
		String permutation = "1"; //$NON-NLS-1$
		for (int i = 2; i <= slider.getSelection(); i++) {
			permutation += "," + i;} //$NON-NLS-1$
		txtperm.setText(permutation);
	}

	/**
	 * Entfernt alle Elemente aus dem Func-Objekt.
	 *
	 */
	private void removeAll() {
		Control[] con = getChildren();
		for (Control c : con) {
			c.dispose();
		}
	}

	public void reset(Model m) {
		this.m = m;
		step0();
	}

}
