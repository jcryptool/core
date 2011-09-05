// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ecc.ui;

import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
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
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ecc.Messages;
import org.jcryptool.visual.ecc.algorithm.LargeCurves;

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

public class ECContentLarge extends Composite {
	private ECView view;
	private Group groupSize = null;
	private Button rbtnSmall = null;
	private Button rbtnLarge = null;
	private Composite compositeIntro = null;
	private Label lblSaveResults = null;
	private StyledText stDescription = null;
	private Group groupSettings = null;
	private Group groupType = null;
	private Button rbtnFP = null;
	private Button rbtnFM = null;
	private Button cbAutoSave = null;
	private Group groupSelectAttributes = null;
	private Combo cStandard = null;
	private Combo cCurve = null;
	private Combo cSaveResults = null;
	private Group groupCalculations = null;
	private Button rbtnPQ = null;
	private Button rbtnKP = null;
	private Spinner spnrK = null;
	private Group groupCurve = null;
	private Group groupAttributes = null;
	private Group groupSave = null;
	private Text txtA = null;
	private Text txtB = null;
	private Text txtP = null;
	private Text txtGx = null;
	private Text txtGy = null;
	private Text txtOrderG = null;
	private Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);

	private int radix = 10;

	private EllipticCurve curve;  //  @jve:decl-index=0:
	private Point pointP;  //  @jve:decl-index=0:
	private Point pointQ;  //  @jve:decl-index=0:
	private Point pointR;  //  @jve:decl-index=0:
	private Point pointG;  //  @jve:decl-index=0:
	private FlexiBigInt fbiA;
	private FlexiBigInt fbiB;
	private FlexiBigInt fbiP;  //  @jve:decl-index=0:
	private FlexiBigInt fbiOrderG;

	private Group groupRadix = null;
	private Button rbtnRadix2 = null;
	private Button rbtnRadix8 = null;
	private Button rbtnRadix10 = null;
	private Button rbtnRadix16 = null;
	private Group groupP = null;
	private Button btnPGenerate = null;
	private Button btnPGenerator = null;
	private Text txtPX = null;
	private Text txtPY = null;
	private Group groupQ = null;
	private Group groupR = null;
	private Button btnQGenerate = null;
	private Button btnQGenerator = null;
	private Text txtQX = null;
	private Text txtQY = null;
	private Label lblR = null;
	private Text txtRX = null;
	private Text txtRY = null;
	private Button btnClearPoints = null;
	private Button btnBrowse = null;
	private Button btnSave = null;
	private Label lblP = null;
	private Composite content;

	public ECContentLarge(Composite parent, int style, ECView v) {
		super(parent, style);
		view = v;

		setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		content = new Composite(scrolledComposite, SWT.NONE);


		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 2;
		content.setLayout(gridLayout);

		createCompositeIntro();
		createGroupCurve();
		createGroupSettings();

		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(862, 804));

		fillCSelection();
		updateScreen();
	}

	/**
	 * This method initializes groupSize
	 *
	 */
	private void createGroupSize() {
		groupSize = new Group(groupSettings, SWT.NONE);
		groupSize.setText(Messages.getString("ECView.SelectCurveSize")); //$NON-NLS-1$
		groupSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		groupSize.setLayout(new GridLayout(2, true));
		rbtnSmall = new Button(groupSize, SWT.RADIO);
		rbtnSmall.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnSmall.setText(Messages.getString("ECView.Small")); //$NON-NLS-1$
		rbtnSmall.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnSmall.getSelection()) {
					if(rbtnFP.getSelection())
						view.showFp();
					else
						view.showFm();
				}
			}

		});
		rbtnLarge = new Button(groupSize, SWT.RADIO);
		rbtnLarge.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnLarge.setText(Messages.getString("ECView.Large")); //$NON-NLS-1$
		rbtnLarge.setSelection(true);
	}



	/**
	 * This method initializes compositeIntro
	 *
	 */
	private void createCompositeIntro() {
		compositeIntro = new Composite(content, SWT.NONE);
		compositeIntro.setBackground(white);
		compositeIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		compositeIntro.setLayout(new GridLayout(1, false));

		Label label = new Label(compositeIntro, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setBackground(white);
		label.setText(Messages.getString("ECView.Title")); //$NON-NLS-1$

		stDescription = new StyledText(compositeIntro, SWT.READ_ONLY);
		stDescription.setText(Messages.getString("ECView.LargeDescription")); //$NON-NLS-1$
		stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
	}

	/**
	 * This method initializes groupSettings
	 *
	 */
	private void createGroupSettings() {
		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.widthHint = 300;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		groupSettings = new Group(content, SWT.NONE);
		groupSettings.setLayout(new GridLayout());
		groupSettings.setText(Messages.getString("ECView.Settings")); //$NON-NLS-1$
		groupSettings.setLayoutData(gridData2);
		createGroupSize();
		createGroupType();
		createGroupSelectAttributes();
		createGroupRadix();
		createGroupCalculations();
		createGroupSave();
	}

	/**
	 * This method initializes groupType
	 *
	 */
	private void createGroupType() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.numColumns = 3;
		gridLayout1.makeColumnsEqualWidth = true;
		groupType = new Group(groupSettings, SWT.NONE);
		groupType.setText(Messages.getString("ECView.SelectCurveType")); //$NON-NLS-1$
		groupType.setLayoutData(gridData3);
		groupType.setLayout(gridLayout1);
		Button button = new Button(groupType, SWT.RADIO);
		button.setText(Messages.getString("ECView.RealNumbers")); //$NON-NLS-1$
		button.setEnabled(false);
		button.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFP = new Button(groupType, SWT.RADIO);
		rbtnFP.setText("F(p)"); //$NON-NLS-1$
		rbtnFP.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFP.setSelection(true);
		rbtnFP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnFP.getSelection())
					fillCSelection();
			}
		});
		rbtnFM = new Button(groupType, SWT.RADIO);
		rbtnFM.setText("F(2^m)"); //$NON-NLS-1$
		rbtnFM.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFM.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnFM.getSelection())
					fillCSelection();
			}
		});
	}

	/**
	 * This method initializes groupSelectAttributes
	 *
	 */
	private void createGroupSelectAttributes() {
		GridData gridData48 = new GridData();
		gridData48.grabExcessHorizontalSpace = true;
		gridData48.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		groupSelectAttributes = new Group(groupSettings, SWT.NONE);
		groupSelectAttributes.setText(Messages.getString("ECContentLarge.12")); //$NON-NLS-1$
		groupSelectAttributes.setLayoutData(gridData48);
		groupSelectAttributes.setLayout(gridLayout2);
		Label label = new Label(groupSelectAttributes, SWT.NONE);
		label.setText(Messages.getString("ECView.Standard")); //$NON-NLS-1$
		createCStandard();
		label = new Label(groupSelectAttributes, SWT.NONE);
		label.setText(Messages.getString("ECView.Curve")); //$NON-NLS-1$
		createCCurve();
	}

	/**
	 * This method initializes cStandard
	 *
	 */
	private void createCStandard() {
		GridData gridData9 = new GridData();
		gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData9.grabExcessHorizontalSpace = true;
		cStandard = new Combo(groupSelectAttributes, SWT.NONE);
		cStandard.setLayoutData(gridData9);
		cStandard.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				fillCCurve();
			}

		});
	}

	/**
	 * This method initializes cCurve
	 *
	 */
	private void createCCurve() {
		GridData gridData10 = new GridData();
		gridData10.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData10.grabExcessHorizontalSpace = true;
		cCurve = new Combo(groupSelectAttributes, SWT.NONE);
		cCurve.setLayoutData(gridData10);
		cCurve.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				setCurve();
			}
		});
	}

	/**
	 * This method initializes groupCalculations
	 *
	 */
	private void createGroupCalculations() {
		GridData gridData45 = new GridData();
		gridData45.horizontalSpan = 2;
		gridData45.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData29 = new GridData();
		gridData29.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.numColumns = 2;
		GridData gridData7 = new GridData();
		gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.horizontalSpan = 2;
		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.horizontalSpan = 2;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		groupCalculations = new Group(groupSettings, SWT.NONE);
		groupCalculations.setLayoutData(gridData4);
		groupCalculations.setLayout(gridLayout3);
		groupCalculations.setText(Messages.getString("ECView.Calculations")); //$NON-NLS-1$
		btnClearPoints = new Button(groupCalculations, SWT.NONE);
		btnClearPoints.setText(Messages.getString("ECView.ClearPoints")); //$NON-NLS-1$
		btnClearPoints.setLayoutData(gridData45);
		btnClearPoints.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				pointP = null;
				pointQ = null;
				pointR = null;
				updateScreen();
			}
		});
		rbtnPQ = new Button(groupCalculations, SWT.RADIO);
		rbtnPQ.setText(Messages.getString("ECContentLarge.17")); //$NON-NLS-1$
		rbtnPQ.setLayoutData(gridData6);
		rbtnPQ.setSelection(true);
		rbtnPQ.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				pointR = null;
				updateScreen();
			}
		});
		rbtnKP = new Button(groupCalculations, SWT.RADIO);
		rbtnKP.setText(Messages.getString("ECContentLarge.18")); //$NON-NLS-1$
		rbtnKP.setLayoutData(gridData5);
		rbtnKP.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnKP.getSelection()) {
					pointQ = null;
					int i = spnrK.getSelection();
					String s = "R = P + " + (i > 2 ? i - 1 : "") + "P"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					if(i > 3) {
						s += " = "; //$NON-NLS-1$
						if(i % 2 == 1)
							s += "P + "; //$NON-NLS-1$
						s += (i / 2) + "P + " + (i / 2) + "P"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					pointR = multiplyPoint(pointP, i);
					lblR.setText(s);
					view.log("\n" + s); //$NON-NLS-1$
					view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
					updateScreen();
				}
			}
		});
		Label label = new Label(groupCalculations, SWT.NONE);
		label.setText("k ="); //$NON-NLS-1$
		spnrK = new Spinner(groupCalculations, SWT.NONE);
		spnrK.setMinimum(2);
		spnrK.setLayoutData(gridData29);
		spnrK.setMaximum(Integer.MAX_VALUE);
		spnrK.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				int i = spnrK.getSelection();
				String s = "R = P + " + (i > 2 ? i - 1 : "") + "P"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if(i > 3) {
					s += " = "; //$NON-NLS-1$
					if(i % 2 == 1)
						s += "P + "; //$NON-NLS-1$
					s += (i / 2) + "P + " + (i / 2) + "P"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				pointR = multiplyPoint(pointP, i);
				lblR.setText(s);
				view.log("\n" + s); //$NON-NLS-1$
				view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				updateScreen();
			}
		});
	}

	/**
	 * This method initializes groupCurve
	 *
	 */
	private void createGroupCurve() {
		GridData gridData8 = new GridData();
		gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData8.grabExcessHorizontalSpace = true;
		gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		groupCurve = new Group(content, SWT.NONE);
		groupCurve.setLayout(new GridLayout());
		groupCurve.setLayoutData(gridData8);
		createGroupAttributes();
		groupCurve.setText(Messages.getString("ECView.EllipticCurve")); //$NON-NLS-1$
		createGroupP();
		createGroupQ();
		createGroupR();
	}

	/**
	 * This method initializes groupAttributes
	 *
	 */
	private void createGroupAttributes() {
		GridData gridData28 = new GridData();
		gridData28.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData27 = new GridData();
		gridData27.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData26 = new GridData();
		gridData26.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData25 = new GridData();
		gridData25.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData24 = new GridData();
		gridData24.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData22 = new GridData();
		gridData22.grabExcessHorizontalSpace = true;
		gridData22.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.numColumns = 2;
		GridData gridData19 = new GridData();
		gridData19.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData18 = new GridData();
		gridData18.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData16 = new GridData();
		gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData15 = new GridData();
		gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData14 = new GridData();
		gridData14.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessHorizontalSpace = true;
		groupAttributes = new Group(groupCurve, SWT.NONE);
		groupAttributes.setLayoutData(gridData11);
		groupAttributes.setLayout(gridLayout5);
		groupAttributes.setText(Messages.getString("ECView.CurveAttributes")); //$NON-NLS-1$
		Label label = new Label(groupAttributes, SWT.NONE);
		label.setText("a ="); //$NON-NLS-1$
		label.setLayoutData(gridData12);
		txtA = new Text(groupAttributes, SWT.BORDER | SWT.READ_ONLY);
		txtA.setEditable(false);
		txtA.setLayoutData(gridData22);
		label = new Label(groupAttributes, SWT.NONE);
		label.setText("b ="); //$NON-NLS-1$
		label.setLayoutData(gridData13);
		txtB = new Text(groupAttributes, SWT.BORDER | SWT.READ_ONLY);
		txtB.setEditable(false);
		txtB.setLayoutData(gridData24);
		lblP = new Label(groupAttributes, SWT.NONE);
		lblP.setText("p ="); //$NON-NLS-1$
		lblP.setLayoutData(gridData14);
		txtP = new Text(groupAttributes, SWT.BORDER);
		txtP.setEditable(false);
		txtP.setLayoutData(gridData25);
		label = new Label(groupAttributes, SWT.NONE);
		label.setText(Messages.getString("ECView.Basepoint") + " G:"); //$NON-NLS-1$ //$NON-NLS-2$
		label.setLayoutData(gridData15);
		new Label(groupAttributes, SWT.NONE);
		label = new Label(groupAttributes, SWT.NONE);
		label.setText("x ="); //$NON-NLS-1$
		label.setLayoutData(gridData18);
		txtGx = new Text(groupAttributes, SWT.BORDER);
		txtGx.setEditable(false);
		txtGx.setLayoutData(gridData26);
		label = new Label(groupAttributes, SWT.NONE);
		label.setText("y ="); //$NON-NLS-1$
		label.setLayoutData(gridData19);
		txtGy = new Text(groupAttributes, SWT.BORDER);
		txtGy.setEditable(false);
		txtGy.setLayoutData(gridData27);
		label = new Label(groupAttributes, SWT.NONE);
		label.setText(Messages.getString("ECView.OrderOf") + " G ="); //$NON-NLS-1$ //$NON-NLS-2$
		label.setLayoutData(gridData16);
		txtOrderG = new Text(groupAttributes, SWT.BORDER);
		txtOrderG.setEditable(false);
		txtOrderG.setLayoutData(gridData28);
	}

	private void fillCSelection() {

		cStandard.removeAll();
		String[] s = null;
		if(rbtnFP.getSelection()) {
			s = LargeCurves.standardFp;
		} else {
			s = LargeCurves.standardFm;
		}
		cStandard.setItems(s);
		cStandard.select(0);
		fillCCurve();
	}

	private void fillCCurve() {
		String[] s = null;
		if(rbtnFP.getSelection()) {
			s = LargeCurves.getNamesFp(cStandard.getSelectionIndex());
		} else {
			s = LargeCurves.getNamesFm(cStandard.getSelectionIndex());
		}
		cCurve.setItems(s);
		cCurve.select(0);
		setCurve();
	}

	private void setCurve() {
		if(rbtnFP.getSelection()) {
			FlexiBigInt[] fbi = LargeCurves.getCurveFp(cStandard.getSelectionIndex(), cCurve.getSelectionIndex());
			curve = new EllipticCurveGFP(new GFPElement(fbi[0], fbi[2]), new GFPElement(fbi[1], fbi[2]), fbi[2]);
			fbiA = fbi[0];
			fbiB = fbi[1];
			fbiP = fbi[2];
			lblP.setText("p ="); //$NON-NLS-1$
			fbiOrderG = fbi[4];
			pointG = new PointGFP(fbi[3].toByteArray(), (EllipticCurveGFP) curve);
		} else {
			FlexiBigInt[] fbi = LargeCurves.getCurveFm(cStandard.getSelectionIndex(), cCurve.getSelectionIndex());
			GF2nPolynomialField field = new GF2nPolynomialField(fbi[2].intValue());
			curve = new EllipticCurveGF2n(new GF2nPolynomialElement(field, fbi[0].toByteArray()), new GF2nPolynomialElement(field, fbi[1].toByteArray()), fbi[2].intValue());
			fbiA = fbi[0];
			fbiB = fbi[1];
			fbiP = fbi[2];
			lblP.setText("m ="); //$NON-NLS-1$
			fbiOrderG = fbi[4];
			pointG = new PointGF2n(fbi[3].toByteArray(), (EllipticCurveGF2n) curve);
		}
		pointP = null;
		pointQ = null;
		pointR = null;
		updateScreen();
	}

	private Point multiplyPoint(Point p, int m) {
		if(m == 0)
			return null;
		if(m == 1)
			return p;
		if(m % 2 == 0)
			return multiplyPoint(p, m / 2).multiplyBy2();
		else
			return p.add(multiplyPoint(p, m - 1));
	}

	private void updateScreen() {
		txtA.setText(fbiA.toString(radix));
		txtB.setText(fbiB.toString(radix));
		if(rbtnFP.getSelection())
			txtP.setText(fbiP.toString(radix));
		else
			txtP.setText(fbiP.toString(10));
		txtGx.setText(new FlexiBigInt(pointG.getXAffin().toString(16).replaceAll("\\s", ""), 16).toString(radix));
		txtGy.setText(new FlexiBigInt(pointG.getYAffin().toString(16).replaceAll("\\s", ""), 16).toString(radix));
		txtOrderG.setText(fbiOrderG.toString(radix));

		if(pointP != null) {
			if(pointP.isZero()) {
				txtPX.setText("O"); //$NON-NLS-1$
				txtPY.setText("O"); //$NON-NLS-1$
			} else {
				txtPX.setText(pointP.getXAffin().toString(radix));
				txtPY.setText(pointP.getYAffin().toString(radix));
			}
		} else {
			txtPX.setText(""); //$NON-NLS-1$
			txtPY.setText(""); //$NON-NLS-1$
		}

		if(pointQ != null) {
			if(pointQ.isZero()) {
				txtQX.setText("O"); //$NON-NLS-1$
				txtQY.setText("O"); //$NON-NLS-1$
			} else {
				txtQX.setText(pointQ.getXAffin().toString(radix));
				txtQY.setText(pointQ.getYAffin().toString(radix));
			}
		} else {
			txtQX.setText(""); //$NON-NLS-1$
			txtQY.setText(""); //$NON-NLS-1$
		}

		if(pointR != null) {
			if(pointR.isZero()) {
				txtRX.setText("O"); //$NON-NLS-1$
				txtRY.setText("O"); //$NON-NLS-1$
			} else {
				txtRX.setText(pointR.getXAffin().toString(radix));
				txtRY.setText(pointR.getYAffin().toString(radix));
			}
		} else {
			txtRX.setText(""); //$NON-NLS-1$
			txtRY.setText(""); //$NON-NLS-1$
		}

		spnrK.setEnabled(rbtnKP.getSelection());
		btnPGenerate.setEnabled(curve != null);
		btnPGenerator.setEnabled(curve != null);
		btnQGenerate.setEnabled(pointP != null && rbtnPQ.getSelection());
		btnQGenerator.setEnabled(pointP != null && rbtnPQ.getSelection());
		btnClearPoints.setEnabled(pointP != null || pointQ != null || pointR != null);
		if(pointR == null)
			lblR.setText(""); //$NON-NLS-1$
	}

	/**
	 * This method initializes groupRadix
	 *
	 */
	private void createGroupRadix() {
		GridData gridData30 = new GridData();
		gridData30.grabExcessHorizontalSpace = true;
		gridData30.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData30.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		groupRadix = new Group(groupSettings, SWT.NONE);
		groupRadix.setLayout(new GridLayout());
		groupRadix.setLayoutData(gridData30);
		groupRadix.setText(Messages.getString("ECView.Radix")); //$NON-NLS-1$
		rbtnRadix2 = new Button(groupRadix, SWT.RADIO);
		rbtnRadix2.setText(Messages.getString("ECView.Binary")); //$NON-NLS-1$
		rbtnRadix2.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnRadix2.getSelection()) {
					radix = 2;
					updateScreen();
				}
			}
		});
		rbtnRadix8 = new Button(groupRadix, SWT.RADIO);
		rbtnRadix8.setText(Messages.getString("ECView.Octal")); //$NON-NLS-1$
		rbtnRadix8.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnRadix8.getSelection()) {
					radix = 8;
					updateScreen();
				}
			}
		});
		rbtnRadix10 = new Button(groupRadix, SWT.RADIO);
		rbtnRadix10.setText(Messages.getString("ECView.Decimal")); //$NON-NLS-1$
		rbtnRadix10.setSelection(true);
		rbtnRadix10.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnRadix10.getSelection()) {
					radix = 10;
					updateScreen();
				}
			}
		});
		rbtnRadix16 = new Button(groupRadix, SWT.RADIO);
		rbtnRadix16.setText(Messages.getString("ECView.Hexadecimal")); //$NON-NLS-1$
		rbtnRadix16.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(rbtnRadix16.getSelection()) {
					radix = 16;
					updateScreen();
				}
			}
		});
	}

	/**
	 * This method initializes groupP
	 *
	 */
	private void createGroupP() {
		GridData gridData44 = new GridData();
		gridData44.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData34 = new GridData();
		gridData34.horizontalSpan = 3;
		gridData34.grabExcessHorizontalSpace = true;
		gridData34.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData33 = new GridData();
		gridData33.horizontalSpan = 2;
		GridData gridData32 = new GridData();
		gridData32.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData32.horizontalSpan = 3;
		gridData32.grabExcessHorizontalSpace = true;
		GridLayout gridLayout6 = new GridLayout();
		gridLayout6.numColumns = 4;
		GridData gridData31 = new GridData();
		gridData31.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData31.grabExcessHorizontalSpace = true;
		gridData31.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		groupP = new Group(groupCurve, SWT.NONE);
		groupP.setLayoutData(gridData31);
		groupP.setLayout(gridLayout6);
		groupP.setText(Messages.getString("ECView.Point") + " P"); //$NON-NLS-1$ //$NON-NLS-2$
		btnPGenerate = new Button(groupP, SWT.NONE);
		btnPGenerate.setText(Messages.getString("ECView.GenerateRandomPoint")); //$NON-NLS-1$
		btnPGenerate.setLayoutData(gridData33);
		btnPGenerate.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(curve != null) {
					if(pointP == null)
						view.log("\n" + curve.toString() + "\n" + (rbtnFP.getSelection() ? "p = " : "m = ") + txtP.getText()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					if(rbtnFP.getSelection())
						pointP = new PointGFP((EllipticCurveGFP) curve, new Random());
					else
						pointP = new PointGF2n((EllipticCurveGF2n) curve, new Random());
					view.log("\n" + Messages.getString("ECView.Point") + " P = " + pointP.toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

					if(rbtnKP.getSelection()) {
						int i = spnrK.getSelection();
						String s = "R = P + " + (i > 2 ? i - 1 : "") + "P"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if(i > 3) {
							s += " = "; //$NON-NLS-1$
							if(i % 2 == 1)
								s += "P + "; //$NON-NLS-1$
							s += (i / 2) + "P + " + (i / 2) + "P"; //$NON-NLS-1$ //$NON-NLS-2$
						}
						pointR = multiplyPoint(pointP, i);
						lblR.setText(s);
						view.log("\n" + s); //$NON-NLS-1$
						view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
						updateScreen();
					} else if (pointQ != null) {
						pointR = pointP.add(pointQ);
						lblR.setText("R = P + Q"); //$NON-NLS-1$
						view.log("\nR = P + Q"); //$NON-NLS-1$
						view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
					}
					updateScreen();
				}
			}
		});
		btnPGenerator = new Button(groupP, SWT.NONE);
		btnPGenerator.setText(Messages.getString("ECView.UseGeneratorG")); //$NON-NLS-1$
		btnPGenerator.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(curve != null) {
					if(pointP == null)
						view.log("\n" + curve.toString()); //$NON-NLS-1$
					pointP = pointG;
					view.log("\n" + Messages.getString("ECView.Point") + " P = " + pointP.toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

					if(rbtnKP.getSelection()) {
						int i = spnrK.getSelection();
						String s = "R = P + " + (i > 2 ? i - 1 : "") + "P"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						if(i > 3) {
							s += " = "; //$NON-NLS-1$
							if(i % 2 == 1)
								s += "P + "; //$NON-NLS-1$
							s += (i / 2) + "P + " + (i / 2) + "P"; //$NON-NLS-1$ //$NON-NLS-2$
						}
						pointR = multiplyPoint(pointP, i);
						lblR.setText(s);
						view.log("\n" + s); //$NON-NLS-1$
						view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
						updateScreen();
					} else if (pointQ != null) {
						pointR = pointP.add(pointQ);
						lblR.setText("R = P + Q"); //$NON-NLS-1$
						view.log("\nR = P + Q"); //$NON-NLS-1$
						view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
					}
					updateScreen();
				}
			}
		});
		Label label = new Label(groupP, SWT.NONE);
		label.setText("x ="); //$NON-NLS-1$
		txtPX = new Text(groupP, SWT.BORDER | SWT.READ_ONLY);
		txtPX.setLayoutData(gridData32);
		label = new Label(groupP, SWT.NONE);
		label.setText("y ="); //$NON-NLS-1$
		txtPY = new Text(groupP, SWT.BORDER | SWT.READ_ONLY);
		txtPY.setLayoutData(gridData34);
	}

	/**
	 * This method initializes groupQ
	 *
	 */
	private void createGroupQ() {
		GridData gridData43 = new GridData();
		gridData43.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		GridData gridData39 = new GridData();
		gridData39.horizontalSpan = 3;
		gridData39.grabExcessHorizontalSpace = true;
		gridData39.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridData gridData38 = new GridData();
		gridData38.horizontalSpan = 2;
		GridData gridData37 = new GridData();
		gridData37.grabExcessHorizontalSpace = true;
		gridData37.horizontalSpan = 3;
		gridData37.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout7 = new GridLayout();
		gridLayout7.numColumns = 4;
		GridData gridData35 = new GridData();
		gridData35.grabExcessHorizontalSpace = true;
		gridData35.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		groupQ = new Group(groupCurve, SWT.NONE);
		groupQ.setLayoutData(gridData35);
		groupQ.setLayout(gridLayout7);
		groupQ.setText(Messages.getString("ECView.Point") + " Q"); //$NON-NLS-1$ //$NON-NLS-2$
		btnQGenerate = new Button(groupQ, SWT.NONE);
		btnQGenerate.setText(Messages.getString("ECView.GenerateRandomPoint")); //$NON-NLS-1$
		btnQGenerate.setLayoutData(gridData38);
		btnQGenerate.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				if(curve != null) {
					if(rbtnFP.getSelection())
						pointQ = new PointGFP((EllipticCurveGFP) curve, new Random());
					else
						pointQ = new PointGF2n((EllipticCurveGF2n) curve, new Random());
					view.log("\n" + Messages.getString("ECView.Point") + " Q = " + pointQ.toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

					if(rbtnPQ.getSelection()) {
						pointR = pointP.add(pointQ);
						lblR.setText("R = P + Q"); //$NON-NLS-1$
						view.log("\nR = P + Q"); //$NON-NLS-1$
						view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
					}
					updateScreen();
				}
			}
		});
		btnQGenerator = new Button(groupQ, SWT.NONE);
		btnQGenerator.setText(Messages.getString("ECView.UseGeneratorG")); //$NON-NLS-1$
		btnQGenerator.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {}
			public void widgetSelected(SelectionEvent e) {
				pointQ = pointG;
				view.log("\n" + Messages.getString("ECView.Point") + " Q = " + pointQ.toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

				if(rbtnPQ.getSelection()) {
					pointR = pointP.add(pointQ);
					lblR.setText("R = P + Q"); //$NON-NLS-1$
					view.log("\nR = P + Q"); //$NON-NLS-1$
					view.log(Messages.getString("ECView.Point") + " R = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
				}

				updateScreen();
			}
		});
		Label label = new Label(groupQ, SWT.NONE);
		label.setText("x ="); //$NON-NLS-1$
		txtQX = new Text(groupQ, SWT.BORDER | SWT.READ_ONLY);
		txtQX.setLayoutData(gridData37);
		label = new Label(groupQ, SWT.NONE);
		label.setText("y ="); //$NON-NLS-1$
		txtQY = new Text(groupQ, SWT.BORDER | SWT.READ_ONLY);
		txtQY.setLayoutData(gridData39);
	}

	/**
	 * This method initializes groupR
	 *
	 */
	private void createGroupR() {
		GridData gridData42 = new GridData();
		gridData42.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData42.grabExcessHorizontalSpace = true;
		GridData gridData41 = new GridData();
		gridData41.grabExcessHorizontalSpace = true;
		gridData41.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout9 = new GridLayout();
		gridLayout9.numColumns = 2;
		GridData gridData40 = new GridData();
		gridData40.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData40.horizontalSpan = 2;
		gridData40.grabExcessHorizontalSpace = true;
		GridData gridData36 = new GridData();
		gridData36.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData36.grabExcessHorizontalSpace = true;
		groupR = new Group(groupCurve, SWT.NONE);
		groupR.setLayoutData(gridData36);
		groupR.setLayout(gridLayout9);
		groupR.setText(Messages.getString("ECView.Point") + " R"); //$NON-NLS-1$ //$NON-NLS-2$
		lblR = new Label(groupR, SWT.NONE);
		lblR.setText(""); //$NON-NLS-1$
		lblR.setLayoutData(gridData40);
		Label label = new Label(groupR, SWT.NONE);
		label.setText("x ="); //$NON-NLS-1$
		txtRX = new Text(groupR, SWT.BORDER | SWT.READ_ONLY);
		txtRX.setLayoutData(gridData41);
		label = new Label(groupR, SWT.NONE);
		label.setText("y ="); //$NON-NLS-1$
		txtRY = new Text(groupR, SWT.BORDER | SWT.READ_ONLY);
		txtRY.setLayoutData(gridData42);
	}

	/**
	 * This method initializes groupSave
	 *
	 */
	private void createGroupSave() {
        groupSave = new Group(groupSettings, SWT.NONE);
        groupSave.setLayout(new GridLayout(2, false));
        groupSave.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        groupSave.setText(Messages.getString("ECView.SaveResults")); //$NON-NLS-1$

        cSaveResults = new Combo(groupSave, SWT.READ_ONLY);
        cSaveResults
                .setItems(new String[] {
                        Messages.getString("ECView.No"), Messages.getString("ECView.ToTextEditor"), Messages.getString("ECView.ToTextFile")}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        cSaveResults.select(view.saveTo);
        cSaveResults.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
        cSaveResults.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.saveTo = cSaveResults.getSelectionIndex();
                btnBrowse.setEnabled(view.saveTo == 2);
                btnSave.setEnabled(view.saveTo != 0);
                cbAutoSave.setEnabled(view.saveTo != 0);
                if (view.saveTo != 0 && view.autoSave)
                    view.saveLog();
                lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
            }
        });

        btnBrowse = new Button(groupSave, SWT.NONE);
        btnBrowse.setText(Messages.getString("ECView.Browse")); //$NON-NLS-1$
        btnBrowse.setEnabled(view.saveTo == 2);
        btnBrowse.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.selectFileLocation();
                lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
            }
        });
        btnSave = new Button(groupSave, SWT.NONE);
        btnSave.setText(Messages.getString("ECView.SaveNow")); //$NON-NLS-1$
        btnSave.setEnabled(view.saveTo != 0);
        btnSave.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.saveLog();
                lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
            }
        });
        cbAutoSave = new Button(groupSave, SWT.CHECK);
        cbAutoSave.setText(Messages.getString("ECView.AutoSave")); //$NON-NLS-1$
        cbAutoSave.setEnabled(view.autoSave);
        cbAutoSave.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
        cbAutoSave.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.autoSave = cbAutoSave.getSelection();
                lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
            }
        });
        lblSaveResults = new Label(groupSave, SWT.NONE);
        lblSaveResults.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
        lblSaveResults.setText(""); //$NON-NLS-1$
    }

	public void adjustButtons() {
		rbtnSmall.setSelection(false);
		rbtnLarge.setSelection(true);

		cSaveResults.select(view.saveTo);
		btnBrowse.setEnabled(view.saveTo == 2);
		btnSave.setEnabled(view.saveTo != 0);
		cbAutoSave.setEnabled(view.saveTo != 0);
		lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
	}
}
