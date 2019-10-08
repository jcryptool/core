// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.ui.wizards;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ECDH.ECDHPlugin;
import org.jcryptool.visual.ECDH.Messages;
import org.jcryptool.visual.ECDH.algorithm.EC;
import org.jcryptool.visual.ECDH.algorithm.ECFm;
import org.jcryptool.visual.ECDH.algorithm.ECFp;
import org.jcryptool.visual.ECDH.algorithm.ECPoint;
import org.jcryptool.visual.ECDH.algorithm.LargeCurves;
import org.jcryptool.visual.ECDH.data.Curves;
import org.jcryptool.visual.ECDH.ECDHUtil;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurve;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurveGF2n;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurveGFP;
import de.flexiprovider.common.math.ellipticcurves.Point;
import de.flexiprovider.common.math.ellipticcurves.PointGF2n;
import de.flexiprovider.common.math.ellipticcurves.PointGFP;
import de.flexiprovider.common.math.finitefields.GF2nPolynomialElement;
import de.flexiprovider.common.math.finitefields.GF2nPolynomialField;
import de.flexiprovider.common.math.finitefields.GFPElement;

/**
 * UI page for selecting public parameters for the PublicParametersWizard
 * 
 * @author unknown
 * 
 *         revised by
 * @author Michael Altenhuber <michael@altenhuber.net>
 *
 */
public class PublicParametersComposite extends Composite {

	private static int SMALLCURVE = 1;
	private static int LARGECURVE = 2;
	
	public static int TYPE_FP = 4;
	public static int TYPE_FM = 8;

	private Group groupCurveType;
	private Group groupCurve;
	private Group groupGenerator;
	private Button btnCreateGeneratorSmall;
	private Button rbtnFP;
	private Button rbtnFM;
	private Group groupCurveSize;
	private Button rbtnSmall;
	private Button rbtnLarge;
	private Group groupAttributes;
	private Composite contentFp;
	private Composite contentFm;
	private Composite contentLarge;
	private Composite contentGeneratorSmall;
	private Composite contentGeneratorLarge;
	private Label labelA;
	private Label labelB;
	private Label labelP;
	private Label labelALarge;
	private Label labelBLarge;
	private Label labelPLarge;
	private Spinner spnrA;
	private Spinner spnrB;
	private Spinner spnrP;
	private Button btnGenerateCurveFp;
	private Button btnGenerateCurveFm;
	private Button btnGenerateCurveLarge;
	private StyledText stGeneratorSmall;
	private Label labelGSmall;
	private Label labelGLarge;
	private int prime[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101,
			103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227,
			229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359,
			367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499,
			503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647,
			653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811,
			821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971,
			977, 983, 991, 997 };
	private int lastPrime;
	private EC curve; // @jve:decl-index=0:
	private EllipticCurve largeCurve; // @jve:decl-index=0:
	private FlexiBigInt fbiOrderG;
	private Combo cA;
	private Combo cB;
	private Combo cG;
	private Combo cCurve;
	private Combo cStandard;
	private Point pointG; // @jve:decl-index=0:
	private Text txtALarge;
	private Text txtBLarge;
	private Text txtPLarge;
	private Text txtGeneratorLarge;
	private Spinner spnrM;
	private Combo cGenerator = null;
	private int[] elements;
	private PublicParametersWizardPage page;
	private ECPoint[] points;
	private int n;
	private int curveSize = SMALLCURVE;
	private StackLayout groupAttributesLayout; // @jve:decl-index=0:
	private StackLayout groupGeneratorLayout;
	private PublicParametersComposite ppComposite;
	private Font monoFont;

	public PublicParametersComposite(Composite parent, int style, PublicParametersWizardPage p, EC c, ECPoint g) {
		super(parent, style);
		page = p;
		page.getWizard().getContainer().getShell().setMinimumSize(600, 680);
		curve = c;
		ppComposite = this;
		monoFont = FontService.getNormalMonospacedFont();
		initialize();
	}

	private void initialize() {
		curve = new ECFp();
		((ECFp) curve).updateCurve(1, 1, 23);
		createGroupCurve();
		createGroupGenerator(SMALLCURVE);
		setLayout(new GridLayout());
	}

	/**
	 * Initialize groupCurveType (Radio Buttons where you can select Fp or F^m)
	 */
	private void createGroupCurveType() {
		groupCurveType = new Group(groupCurve, SWT.NONE);
		groupCurveType.setText(Messages.getString("ECDHWizPP.groupCurveType")); //$NON-NLS-1$
		groupCurveType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupCurveType.setLayout(new GridLayout(2, false));

		rbtnFP = new Button(groupCurveType, SWT.RADIO);
		rbtnFP.setText("F(p)"); //$NON-NLS-1$
		rbtnFP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rbtnFP.setSelection(true);
		rbtnFP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * Part of the state machine which switches between the states of the curve.
			 * This one does less as it is default initialized in the beginning.
			 */
			public void widgetSelected(SelectionEvent e) {
				if (rbtnFP.getSelection()) {
					if (rbtnSmall.getSelection() && (curve == null || curve.getType() != ECFp.ECFp)) {
						groupAttributesLayout.topControl = contentFp;
						groupAttributes.layout();
						curve = new ECFp();
						((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP.getSelection());
						fillGeneratorPointsSmall();
					} else if (rbtnLarge.getSelection()) {
						fillLargeCurveElements();
					}
				}
			}
		});

		rbtnFM = new Button(groupCurveType, SWT.RADIO);
		rbtnFM.setText("F(2^m)"); //$NON-NLS-1$
		rbtnFM.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rbtnFM.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * Part of the state machine which switches between the states of the curve.
			 * Select F^m and update according UI elements and the curve. Slightly depending
			 * on the size
			 */
			public void widgetSelected(SelectionEvent e) {
				if (rbtnFM.getSelection()) {
					if (curveSize == SMALLCURVE && contentFm == null) {
						CreateContentFmSmall();
						groupAttributesLayout.topControl = contentFm;
					}

					if (rbtnSmall.getSelection() && (curve == null || curve.getType() != ECFm.ECFm)) {
						createFmCurve(spnrM.getSelection(), -1);
						fillGeneratorPointsSmall();
						groupAttributesLayout.topControl = contentFm;

					} else if (rbtnLarge.getSelection()) {
						fillLargeCurveElements();
					}

					groupAttributes.layout();
					ppComposite.layout(true);
				}
			}
		});
	}

	/**
	 * Initialize diverse elements of groupCurve in default mode small
	 * 
	 * (Which contains the selection radio buttons (type and size) as well as the
	 * curve parameters)
	 */
	private void createGroupCurve() {
		groupCurve = new Group(this, SWT.NONE);
		groupCurve.setText(Messages.getString("ECDHWizPP.ellipticCurve")); //$NON-NLS-1$
		groupCurve.setLayout(new GridLayout(2, false));
		groupCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		createGroupCurveType();
		createGroupCurveSize();
		createGroupAttributes();
	}

	/**
	 * Initialize groupGenerator at the bottom (Which contains the combo box/label
	 * for the generator)
	 *
	 * @param sizeType either SMALLCURVE or LARGECURVE depending on the current
	 *                 curve mode
	 */
	private void createGroupGenerator(int sizeType) {

		// Prevent multiple instances of the group as this function may be called more
		// than once
		if (groupGenerator == null) {
			groupGeneratorLayout = new StackLayout();

			groupGenerator = new Group(this, SWT.NONE);
			groupGenerator.setText(Messages.getString("ECDHWizPP.groupGenerator")); //$NON-NLS-1$
			groupGenerator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			groupGenerator.setLayout(groupGeneratorLayout);
		}

		if (sizeType == SMALLCURVE && contentGeneratorSmall == null)
			createGroupGeneratorSmall();
		else
			createGroupGeneratorLarge();
		setTopGeneratorComposite(sizeType);
	}

	/**
	 * Initializes groupGeneratorSmall (Which contains the generator combo box and
	 * the random generator button)
	 */
	private void createGroupGeneratorSmall() {
		contentGeneratorSmall = new Composite(groupGenerator, SWT.NONE);
		contentGeneratorSmall.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 1, 1));
		contentGeneratorSmall.setLayout(new GridLayout(4, false));

		stGeneratorSmall = new StyledText(contentGeneratorSmall, SWT.READ_ONLY);
		stGeneratorSmall.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		stGeneratorSmall.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 3, 1));
		stGeneratorSmall.setText(Messages.getString("ECDHWizPP.textGenerator")); //$NON-NLS-1$

		btnCreateGeneratorSmall = new Button(contentGeneratorSmall, SWT.READ_ONLY);
		btnCreateGeneratorSmall.setText(Messages.getString("ECDHWizPP.btnCreateGenerator")); //$NON-NLS-1$
		btnCreateGeneratorSmall.setLayoutData(new GridData(SWT.END, SWT.DEFAULT, false, false));
		btnCreateGeneratorSmall.setEnabled(false);
		btnCreateGeneratorSmall.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (cGenerator.getItemCount() > 1) {
					Random r = new Random();
					cGenerator.select(r.nextInt(cGenerator.getItemCount()));
				}
			}

		});
		labelGSmall = new Label(contentGeneratorSmall, SWT.NONE);
		labelGSmall.setText("G ="); //$NON-NLS-1$
		labelGSmall.setFont(monoFont);
		cGenerator = new Combo(contentGeneratorSmall, SWT.READ_ONLY);
		cGenerator.setFont(monoFont);
		cGenerator.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));
		fillGeneratorPointsSmall();
	}

	/**
	 * Initializes groupGeneratorLarge (Which contains the generator text field)
	 */
	private void createGroupGeneratorLarge() {
		contentGeneratorLarge = new Composite(groupGenerator, SWT.NONE);
		contentGeneratorLarge.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 1, 1));
		contentGeneratorLarge.setLayout(new GridLayout(4, false));

		labelGLarge = new Label(contentGeneratorLarge, SWT.NONE);
		labelGLarge.setText("G ="); //$NON-NLS-1$
		labelGLarge.setFont(monoFont);
		txtGeneratorLarge = new Text(contentGeneratorLarge, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		txtGeneratorLarge.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		txtGeneratorLarge.setFont(monoFont);
	}

	/**
	 * Initializes groupCurveSize (Radio Buttons small/large)
	 */
	private void createGroupCurveSize() {
		groupCurveSize = new Group(groupCurve, SWT.NONE);
		groupCurveSize.setText(Messages.getString("ECDHWizPP.groupCurveSize")); //$NON-NLS-1$
		groupCurveSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupCurveSize.setLayout(new GridLayout(2, false));

		rbtnSmall = new Button(groupCurveSize, SWT.RADIO);
		rbtnSmall.setText(Messages.getString("ECDHWizPP.small")); //$NON-NLS-1$
		rbtnSmall.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rbtnSmall.setSelection(true);
		rbtnSmall.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * Part of the state machine which switches between the states of the curve.
			 * This one is the complicated. It handles various states when switching wildly
			 */
			public void widgetSelected(SelectionEvent e) {
				if (rbtnSmall.getSelection()) {

					// Setting the small generator happens always
					setTopGeneratorComposite(SMALLCURVE);

					// If the state is Fp just create a new curve if no Fp one exists and update
					if (rbtnFP.getSelection()) {
						groupAttributesLayout.topControl = contentFp;
						if (curve == null || curve.getType() != ECFp.ECFp) {
							groupAttributesLayout.topControl = contentFp;
							groupAttributes.layout();
							curve = new ECFp();
							((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP.getSelection());
						}
						fillGeneratorPointsSmall();

						// If the state is F^m
					} else {

						// It may happen that contentFm has not yet been created yet
						if (contentFm == null) {
							CreateContentFmSmall();
							groupAttributesLayout.topControl = contentFm;
							groupAttributes.layout();
						}
						groupAttributesLayout.topControl = contentFm;

						// Create an F^m curve if we need it
						if (curve == null || curve.getType() != ECFm.ECFm) {
							groupAttributesLayout.topControl = contentFm;
							groupAttributes.layout();
							createFmCurve(spnrM.getSelection(), -1);
							fillGeneratorPointsSmall();
						}

					}

					// This is a fallback check for some weird state interactions where you would
					// not have a valid generator point but you could press on finish
					if (page.isPageComplete() && cGenerator.getItemCount() <= 0) {
						page.setErrorMessage(Messages.getString("ECDHWizPP.PublicParametersError")); //$NON-NLS-1$
						page.setPageComplete(false);
					}
					groupGenerator.layout();
					groupAttributes.layout();
				}

			}
		});

		rbtnLarge = new Button(groupCurveSize, SWT.RADIO);
		rbtnLarge.setText(Messages.getString("ECDHWizPP.large")); //$NON-NLS-1$
		rbtnLarge.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		rbtnLarge.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * Part of the state machine which switches between the states of the curve.
			 * This one does less and most UI logic is put into the called functions.
			 */
			public void widgetSelected(SelectionEvent e) {
				if (rbtnLarge.getSelection()) {
					groupAttributesLayout.topControl = contentLarge;
					setTopGeneratorComposite(LARGECURVE);
					fillLargeCurveElements();

					groupGenerator.layout();
					groupAttributes.layout();
				}

			}
		});
	}

	/**
	 * Initializes groupAttributes (Elements for specific elliptic curve domain
	 * parameters, numbers and equations and stuff)
	 * 
	 * Note that this has a hidden part for the large curves
	 */
	private void createGroupAttributes() {
		groupAttributes = new Group(groupCurve, SWT.NONE);
		groupAttributes.setText(Messages.getString("ECDHWizPP.groupAttributes")); //$NON-NLS-1$
		groupAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		groupAttributesLayout = new StackLayout();
		groupAttributes.setLayout(groupAttributesLayout);

		// Default small content for an Fp curve
		createContentFp();
		// Extra two buttons (standard and curve) only for large curves
		createContentLarge();
		groupAttributesLayout.topControl = contentFp;
	}

	/**
	 * Initialize UI for elliptic curve domain parameters for small fp curves
	 */
	private void createContentFp() {
		contentFp = new Composite(groupAttributes, SWT.NONE);
		contentFp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		contentFp.setLayout(new GridLayout(2, false));

		btnGenerateCurveFp = new Button(contentFp, SWT.NONE);
		btnGenerateCurveFp.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1));
		btnGenerateCurveFp.setText(Messages.getString("ECDHWizPP.btnGenerateCurve")); //$NON-NLS-1$
		btnGenerateCurveFp.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				Random r = new Random();
				int[] i = Curves.ECFp[r.nextInt(Curves.ECFp.length - 1)];
				((ECFp) curve).updateCurve(i[1], i[2], i[0]);
				spnrP.setSelection(((ECFp) curve).getP());
				lastPrime = ((ECFp) curve).getP();
				spnrA.setMaximum(lastPrime - 1);
				spnrB.setMaximum(lastPrime - 1);
				spnrA.setSelection(curve.getA());
				spnrB.setSelection(curve.getB());
				fillGeneratorPointsSmall();
			}
		});

		labelA = new Label(contentFp, SWT.NONE);
		labelA.setText("a ="); //$NON-NLS-1$
		labelA.setFont(monoFont);
		spnrA = new Spinner(contentFp, SWT.NONE);
		spnrA.setFont(monoFont);
		spnrA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		spnrA.setMinimum(0);
		spnrA.setSelection(1);
		spnrA.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP.getSelection());
				fillGeneratorPointsSmall();
			}
		});
		labelB = new Label(contentFp, SWT.NONE);
		labelB.setText("b ="); //$NON-NLS-1$
		labelB.setFont(monoFont);
		spnrB = new Spinner(contentFp, SWT.NONE);
		spnrB.setFont(monoFont);
		spnrB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		spnrB.setMinimum(0);
		spnrB.setSelection(1);
		spnrB.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP.getSelection());
				fillGeneratorPointsSmall();
			}
		});
		labelP = new Label(contentFp, SWT.NONE);
		labelP.setText("p ="); //$NON-NLS-1$
		labelP.setFont(monoFont);
		spnrP = new Spinner(contentFp, SWT.NONE);
		spnrP.setFont(monoFont);
		spnrP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		spnrP.setMaximum(1000);
		spnrP.setMinimum(3);
		spnrP.setSelection(23);
		lastPrime = 23;
		spnrA.setMaximum(lastPrime - 1);
		spnrB.setMaximum(lastPrime - 1);
		spnrP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				boolean up = spnrP.getSelection() > lastPrime;
				for (int i = 0; i < prime.length; i++) {
					if (prime[i] > spnrP.getSelection()) {
						spnrP.setSelection(up ? prime[i] : prime[i - 1]);
						lastPrime = spnrP.getSelection();
						spnrA.setSelection(spnrA.getSelection() % lastPrime);
						spnrA.setMaximum(lastPrime - 1);
						spnrB.setSelection(spnrB.getSelection() % lastPrime);
						spnrB.setMaximum(lastPrime - 1);
						break;
					}
				}
				((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP.getSelection());
				fillGeneratorPointsSmall();
			}
		});
	}

	/**
	 * Initialize UI for elliptic curve domain parameters for small F^m curves
	 */
	private void CreateContentFmSmall() {
		contentFm = new Composite(groupAttributes, SWT.NONE);
		contentFm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		contentFm.setLayout(new GridLayout(2, false));

		btnGenerateCurveFm = new Button(contentFm, SWT.NONE);
		btnGenerateCurveFm.setLayoutData(new GridData(SWT.RIGHT, SWT.DEFAULT, false, false, 2, 1));
		btnGenerateCurveFm.setText(Messages.getString("ECDHWizPP.btnGenerateCurve")); //$NON-NLS-1$
		btnGenerateCurveFm.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				Random r = new Random();
				int m = r.nextInt(3); // Set m
				while (m == 1) {
					m = r.nextInt(3);
				}
				m += 3;
				spnrM.setSelection(m);
				((ECFm) curve).setM(m);
				int[] ip = ((ECFm) curve).getIrreduciblePolinomials();
				String[] s = new String[ip.length];
				for (int i = 0; i < s.length; i++)
					s[i] = ECDHUtil.intToBitString(ip[i]);
				cG.setItems(s);
				if (ip.length == 1)
					cG.select(0);
				else
					cG.select(r.nextInt(ip.length));
				((ECFm) curve).setG(cG.getSelectionIndex(), true); // set G

				elements = ((ECFm) curve).getElements();
				setComboAB(-1, -1, false);

				if (m == 3) {
					int a = r.nextInt(3);
					if (a == 0)
						cA.select(3);
					else if (a == 1)
						cA.select(5);
					else
						cA.select(6);
					((ECFm) curve).setA(cA.getSelectionIndex(), true);
					cB.select(0);
					((ECFm) curve).setB(cB.getSelectionIndex(), true);
					fillGeneratorPointsSmall();
				} else {
					cA.select(r.nextInt(cA.getItemCount()));
					((ECFm) curve).setA(cA.getSelectionIndex(), true);
					int b = r.nextInt(cB.getItemCount());
					int count = 0;
					do {
						cB.select(b);
						((ECFm) curve).setB(cB.getSelectionIndex(), true);
						fillGeneratorPointsSmall();
						b = (b + 1) % cB.getItemCount();
						count++;
					} while (cGenerator.getItemCount() == 0 && count < cB.getItemCount());

					if (count >= cB.getItemCount()) {
						try {
							throw new Exception("Generator fault, could not find correct curve"); //$NON-NLS-1$
						} catch (Exception ex) {
							LogUtil.logError(ECDHPlugin.PLUGIN_ID, ex);
						}
					}
				}
			}
		});

		Label label = new Label(contentFm, SWT.NONE);
		label.setText("m ="); //$NON-NLS-1$
		label.setFont(monoFont);
		spnrM = new Spinner(contentFm, SWT.NONE);
		spnrM.setFont(monoFont);
		spnrM.setMaximum(6);
		spnrM.setSelection(3);
		spnrM.setMinimum(3);
		spnrM.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		spnrM.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				cG.removeAll();
				cA.removeAll();
				cB.removeAll();

				((ECFm) curve).setM(spnrM.getSelection());
				int[] ia = ((ECFm) curve).getIrreduciblePolinomials();
				String[] s = new String[ia.length];
				for (int i = 0; i < s.length; i++)
					s[i] = ECDHUtil.intToBitString(ia[i]);
				cG.setItems(s);
			}
		});
		label = new Label(contentFm, SWT.NONE);
		label.setText("f(x) ="); //$NON-NLS-1$
		label.setFont(monoFont);
		cG = new Combo(contentFm, SWT.READ_ONLY);
		cG.setFont(monoFont);
		cG.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		cG.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				((ECFm) curve).setG(cG.getSelectionIndex(), true);
				elements = ((ECFm) curve).getElements();
				setComboAB(-1, -1, false);
			}
		});
		label = new Label(contentFm, SWT.NONE);
		label.setText("a ="); //$NON-NLS-1$
		label.setFont(monoFont);
		cA = new Combo(contentFm, SWT.READ_ONLY);
		cA.setFont(monoFont);
		cA.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		cA.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				((ECFm) curve).setA(cA.getSelectionIndex(), true);
				fillGeneratorPointsSmall();
			}
		});
		label = new Label(contentFm, SWT.NONE);
		label.setText("b ="); //$NON-NLS-1$
		label.setFont(monoFont);
		cB = new Combo(contentFm, SWT.READ_ONLY);
		cB.setFont(monoFont);
		cB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		cB.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				((ECFm) curve).setB(cB.getSelectionIndex(), true);
				fillGeneratorPointsSmall();
			}
		});

		/*
		 * Initialize a working curve as predefined setting // By setting cA to g4
		 * (index 3), we have a working curve
		 */
		createFmCurve(spnrM.getSelection(), 3);

		// Now we can calculate valid generators, which was deferred above
		fillGeneratorPointsSmall();
		// }
	}

	/**
	 * Initialize the two combo boxes (standard and curve) only visible in large
	 * mode
	 */
	private void createContentLarge() {
		contentLarge = new Composite(groupAttributes, SWT.NONE);
		contentLarge.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		contentLarge.setLayout(new GridLayout(4, false));

		cStandard = new Combo(contentLarge, SWT.READ_ONLY);
		cStandard.setLayoutData(new GridData(SWT.CENTER, SWT.BEGINNING, false, false, 2, 1));
		cStandard.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				updateLargeCurveElements();
				txtGeneratorLarge.requestLayout();
				txtALarge.requestLayout();
				txtBLarge.requestLayout();
				txtPLarge.requestLayout();
			}
		});

		cCurve = new Combo(contentLarge, SWT.READ_ONLY);
		cCurve.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				setCurve();
				txtGeneratorLarge.requestLayout();
				txtALarge.requestLayout();
				txtBLarge.requestLayout();
				txtPLarge.requestLayout();
			}
		});

		btnGenerateCurveLarge = new Button(contentLarge, SWT.NONE);
		btnGenerateCurveLarge.setText(Messages.getString("ECDHWizPP.btnGenerateCurve")); //$NON-NLS-1$
		btnGenerateCurveLarge.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		btnGenerateCurveLarge.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				Random r = new Random();
				if (rbtnFP.getSelection()) {
					cStandard.select(r.nextInt(cStandard.getItemCount()));
					updateLargeCurveElements();
				}
				cCurve.select(r.nextInt(cCurve.getItemCount()));
				setCurve();
			}
		});

		if (rbtnFP.getSelection()) {
			cStandard.setItems(LargeCurves.standardFp);
			cStandard.select(0);
			cCurve.setItems(LargeCurves.getNamesFp(0));
		} else {
			cStandard.setItems(LargeCurves.standardFm);
			cStandard.select(0);
			cCurve.setItems(LargeCurves.getNamesFm(0));
		}

		cCurve.select(0);
		labelALarge = new Label(contentLarge, SWT.NONE);
		labelALarge.setText("a ="); //$NON-NLS-1$
		labelALarge.setFont(monoFont);
		txtALarge = new Text(contentLarge, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		txtALarge.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		txtALarge.setFont(monoFont);
		labelBLarge = new Label(contentLarge, SWT.NONE);
		labelBLarge.setText("b ="); //$NON-NLS-1$
		labelBLarge.setFont(monoFont);
		txtBLarge = new Text(contentLarge, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		txtBLarge.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		txtBLarge.setFont(monoFont);
		labelPLarge = new Label(contentLarge, SWT.NONE);
		labelPLarge.setText("p ="); //$NON-NLS-1$
		labelPLarge.setFont(monoFont);
		txtPLarge = new Text(contentLarge, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
		txtPLarge.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		txtPLarge.setFont(monoFont);
	}

	/**
	 * Set values for a and b in Fp small curves.
	 * 
	 * @param a                    the curve parameter
	 * @param b                    the curve parameter
	 * @param deferGeneratorUpdate if true does not automatically update the
	 *                             generator points. Needed for init purposes
	 */
	private void setComboAB(int a, int b, boolean deferGeneratorUpdate) {
		cA.removeAll();
		cB.removeAll();
		if (elements == null) {
			return;
		}
		String[] sA = new String[elements.length + 1];
		String[] sB = new String[elements.length];
		for (int i = 0; i < elements.length; i++) {
			sA[i] = ECDHUtil.intToBitString(elements[i], spnrM.getSelection());
			if (i == 0)
				sA[i] += " (1)"; //$NON-NLS-1$
			else if (i == 1)
				sA[i] += " (g)"; //$NON-NLS-1$
			else
				sA[i] += " (g" + i + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			sB[i] = sA[i];
		}
		sA[sA.length - 1] = "0"; //$NON-NLS-1$
		cA.setItems(sA);
		if (a == -1) {
			cA.select(0);
			((ECFm) curve).setA(cA.getSelectionIndex(), true);
		} else {
			cA.select(sA.length - 1);
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] == a)
					cA.select(i);
			}
		}
		cB.setItems(sB);
		if (b == -1) {
			cB.select(0);
			((ECFm) curve).setB(cB.getSelectionIndex(), true);
		} else {
			for (int i = 0; i < elements.length; i++) {
				if (elements[i] == b)
					cB.select(i);
			}
		}

		// Generators have to be updated here
		if (!deferGeneratorUpdate)
			fillGeneratorPointsSmall();
	}

	/**
	 * Set the top of the StackLayout for the generator group - depending on
	 * selecting small or large curve
	 */
	private void setTopGeneratorComposite(int sizeType) {

		// Double check if exists for safety purposes
		if (sizeType == SMALLCURVE && contentGeneratorSmall == null) {
			createGroupGenerator(sizeType);
			return;
		} else if (sizeType == LARGECURVE && contentGeneratorLarge == null) {
			createGroupGenerator(sizeType);
			return;
		}

		// After made sure it exists, set the top composite
		groupGeneratorLayout.topControl = sizeType == SMALLCURVE ? contentGeneratorSmall : contentGeneratorLarge;
		curveSize = sizeType;
	}

	/**
	 * Fill the Generator combo box (only available with small curve) with generator
	 * elements
	 */
	private void fillGeneratorPointsSmall() {

		cGenerator.removeAll();
		page.setErrorMessage(Messages.getString("ECDHWizPP.PublicParametersError")); //$NON-NLS-1$
		page.setPageComplete(false);

		// This should not happen in a normal control flow
		if (curveSize != SMALLCURVE) {
			LogUtil.logError(
					"fillGeneratorPointsSmall() called with large curve. This should not happen in the program control flow"); //$NON-NLS-1$
			return;
		}

		if (cGenerator == null)
			return;

		// If we get an error here we could not retrieve any generators
		if (!getGeneratorPoints())
			return;

		String[] s = new String[points.length];

		/*
		 * This loop converts the elliptic curve points from points[] to strings
		 * specific to the selected curve.
		 */
		for (int i = 0; i < points.length; i++) {
			if (rbtnFP.getSelection())
				s[i] = "(" + points[i].x + "," + points[i].y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			else {
				s[i] = "(" //$NON-NLS-1$
						+ ECDHUtil.intToBitString(points[i].x == elements.length ? 0 : elements[points[i].x], spnrM.getSelection()) + "," //$NON-NLS-1$
						+ ECDHUtil.intToBitString(points[i].y == elements.length ? 0 : elements[points[i].y], spnrM.getSelection()) + ")"; //$NON-NLS-1$
				s[i] += " = ("; //$NON-NLS-1$
				if (points[i].x == 0)
					s[i] += "1, "; //$NON-NLS-1$
				else if (points[i].x == 1)
					s[i] += "g, "; //$NON-NLS-1$
				else if (points[i].x == elements.length)
					s[i] += "0, "; //$NON-NLS-1$
				else
					s[i] += "g" + points[i].x + ", "; //$NON-NLS-1$ //$NON-NLS-2$

				if (points[i].y == 0)
					s[i] += "1)"; //$NON-NLS-1$
				else if (points[i].y == 1)
					s[i] += "g)"; //$NON-NLS-1$
				else if (points[i].y == elements.length)
					s[i] += "0)"; //$NON-NLS-1$
				else
					s[i] += "g" + points[i].y + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		cGenerator.setItems(s);
		cGenerator.select(0);

		btnCreateGeneratorSmall.setEnabled(true);
		page.setPageComplete(true);
		page.setErrorMessage(null);
	}

	/**
	 * Retrieve generators for the currently present this.curve and store them into
	 * this.points[]
	 * 
	 * @return false on error, true otherwise
	 */
	private boolean getGeneratorPoints() {
		ECPoint[] p = curve.getPoints();

		points = null;
		if (p == null)
			return false;

		if (rbtnFP.getSelection()) {
			if (p.length == ((ECFp) curve).getP() || isPrime(p.length))
				return false;

			n = ((ECFp) curve).getP() + 1;
			ArrayList<ECPoint> list = new ArrayList<ECPoint>();
			for (int i = 0; i < p.length; i++) {
				if (p[i].y != 0) {
					for (int j = 2; j <= ((ECFp) curve).getP(); j++) {
						if (p[i].equals(new ECPoint(66, 34)) && j == 70)
							p[i] = p[i];
						if (curve.multiplyPoint(p[i], j) == null) {
							n = j;
							list.add(p[i]);
							j = ((ECFp) curve).getP() + 1;
						}
					}
				}
			}

			if (n == ((ECFp) curve).getP() + 1)
				return false;

			points = new ECPoint[list.size()];
			for (int i = 0; i < points.length; i++)
				points[i] = list.get(i);
		} else {
			ArrayList<ECPoint> list = new ArrayList<ECPoint>();
			int len = (int) Math.pow(2, ((ECFm) curve).getM());
			n = len;
			for (int i = 0; i < p.length; i++) {
				if (p[i].y != len) {
					for (int j = 2; j <= len; j++) {
						if (curve.multiplyPoint(p[i], j) == null) {
							if (j <= n && ((int) Math.pow(Math.sqrt(len) + 1, 2)) % j == 0
									&& ((int) Math.pow(Math.sqrt(len) + 1, 2)) / j <= 4) {
								if (j < n)
									list.clear();
								n = j;
								list.add(p[i]);
							}
							j = len + 1;
						}
					}
				}
			}

			if (n == len)
				return false;

			points = new ECPoint[list.size()];
			for (int i = 0; i < points.length; i++)
				points[i] = list.get(i);
		}

		return true;
	}

	/**
	 * If a large curve is selected, fill out all other elements
	 * 
	 * This includes the comboboxes, the curve parameters and the generator
	 */
	private void fillLargeCurveElements() {
		fillLargeSelections();
		updateLargeCurveElements();

		// May be necessary in some states to reset the error
		if (page.isPageComplete() && curve.getType() == ECFm.ECFm) {
			page.setErrorMessage(null);
			page.setPageComplete(true);
		}
		page.setPageComplete(true);
		page.setErrorMessage(null);
	}

	/**
	 * This function may seem useless but makes the code more readable
	 */
	private void updateLargeCurveElements() {
		setCurve();
	}

	/**
	 * Fills the combo boxes standard and curve selection with respective values (e.
	 * g. ANSI X9.62, prime192v1)
	 */
	private void fillLargeSelections() {
		String[] s;
		if (rbtnFP.getSelection()) {
			s = LargeCurves.standardFp;
		} else {
			s = LargeCurves.standardFm;
		}
		cStandard.setItems(s);
		cStandard.select(0);

		if (rbtnFP.getSelection()) {
			s = LargeCurves.getNamesFp(cStandard.getSelectionIndex());
		} else {
			s = LargeCurves.getNamesFm(cStandard.getSelectionIndex());
		}
		cCurve.setItems(s);
		cCurve.select(0);
	}

	/**
	 * Retrieves curve and updates all parameters for large curves. This includes
	 * the order, generator, equation parameters
	 * 
	 * This function is way simpler than those for the small ones, because you
	 * cannot select any arguments. The curves are predefined and static.
	 */
	private void setCurve() {
		String generatorX, generatorY;

		if (rbtnFP.getSelection()) {
			FlexiBigInt[] fbi = LargeCurves.getCurveFp(cStandard.getSelectionIndex(), cCurve.getSelectionIndex());
			largeCurve = new EllipticCurveGFP(new GFPElement(fbi[0], fbi[2]), new GFPElement(fbi[1], fbi[2]), fbi[2]);
			txtALarge.setText(ECDHUtil.spaceString(fbi[0].toString(16).toUpperCase()));
			txtBLarge.setText(ECDHUtil.spaceString(fbi[1].toString(16).toUpperCase()));
			txtPLarge.setText(ECDHUtil.spaceString(fbi[2].toString(16).toUpperCase()));
			labelPLarge.setText("p ="); //$NON-NLS-1$
			fbiOrderG = fbi[4];
			pointG = new PointGFP(fbi[3].toByteArray(), (EllipticCurveGFP) largeCurve);
			generatorX = ECDHUtil.spaceString(pointG.getX().toString().trim().toUpperCase());
			generatorY = ECDHUtil.spaceString(pointG.getY().toString().trim().toUpperCase());

		} else {
			FlexiBigInt[] fbi = LargeCurves.getCurveFm(cStandard.getSelectionIndex(), cCurve.getSelectionIndex());
			GF2nPolynomialField field = new GF2nPolynomialField(fbi[2].intValue());
			largeCurve = new EllipticCurveGF2n(new GF2nPolynomialElement(field, fbi[0].toByteArray()),
					new GF2nPolynomialElement(field, fbi[1].toByteArray()), fbi[2].intValue());
			txtALarge.setText(ECDHUtil.spaceString(fbi[0].toString(16).toUpperCase()));
			txtBLarge.setText(ECDHUtil.spaceString(fbi[1].toString(16).toUpperCase()));
			txtPLarge.setText(ECDHUtil.spaceString(fbi[2].toString(16).toUpperCase()));
			labelPLarge.setText("m ="); //$NON-NLS-1$
			fbiOrderG = fbi[4];
			pointG = new PointGF2n(fbi[3].toByteArray(), (EllipticCurveGF2n) largeCurve);
			generatorX = pointG.getX().toString().trim().toUpperCase();
			generatorY = pointG.getY().toString().trim().toUpperCase();
		}
		txtGeneratorLarge.setText(new String("(" + generatorX + ", " + generatorY + ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Create an F^m curve and set its according UI elements
	 * 
	 * @param m size of the parameter m
	 */
	private void createFmCurve(int m, int preselectedA) {

		curve = new ECFm();
		((ECFm) curve).setM(m);
		if (cG.getItemCount() == 0) {
			int[] ia = ((ECFm) curve).getIrreduciblePolinomials();
			String[] s = new String[ia.length];
			for (int i = 0; i < s.length; i++)
				s[i] = ECDHUtil.intToBitString(ia[i]);
			cG.setItems(s);
		}
		if (cG.getSelectionIndex() < 0)
			cG.select(0);
		((ECFm) curve).setG(cG.getSelectionIndex(), true);
		if (cA.getItemCount() == 0) {
			elements = ((ECFm) curve).getElements();
			setComboAB(curve.getA(), curve.getB(), false);
		}

		if (preselectedA >= cA.getItemCount() - 1)
			preselectedA = -1;

		if (preselectedA > 0)
			cA.select(preselectedA);

		((ECFm) curve).setA(cA.getSelectionIndex(), true);
		((ECFm) curve).setB(cB.getSelectionIndex(), true);
	}


	public ECPoint getGenerator() {
		return points[cGenerator.getSelectionIndex()];
	}

	public EC getCurve() {
		return curve;
	}

	public int getOrder() {
		return n;
	}

	private boolean isPrime(int p) {
		for (int i = 0; i < prime.length; i++) {
			if (p == prime[i])
				return true;
		}
		return false;
	}

	public boolean isLarge() {
		return rbtnLarge.getSelection();
	}

	public EllipticCurve getLargeCurve() {
		return largeCurve;
	}

	public Point getLargeGenerator() {
		return pointG;
	}

	public FlexiBigInt getLargeOrder() {
		return fbiOrderG;
	}
	
	public int getCurveType() {
		if (rbtnFP.getSelection())
			return TYPE_FP;
		else if (rbtnFM.getSelection())
			return TYPE_FM;
		else
			throw new IllegalStateException("Can not find type of curve to return");
	}

} // @jve:decl-index=0:visual-constraint="10,10"
