// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
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
import org.jcryptool.visual.ECDH.ECDHPlugin;
import org.jcryptool.visual.ECDH.Messages;
import org.jcryptool.visual.ECDH.algorithm.EC;
import org.jcryptool.visual.ECDH.algorithm.ECFm;
import org.jcryptool.visual.ECDH.algorithm.ECFp;
import org.jcryptool.visual.ECDH.algorithm.ECPoint;
import org.jcryptool.visual.ECDH.algorithm.LargeCurves;
import org.jcryptool.visual.ECDH.data.Curves;

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

public class PublicParametersComposite extends Composite {

    private Group groupCurveType = null;
    private Group groupCurve = null;
    private Group groupGenerator = null;
    private Button btnCreateGenerator = null;
    private Button rbtnFP = null;
    private Button rbtnFM = null;
    private Group groupCurveSize = null;
    private Button rbtnSmall = null;
    private Button rbtnLarge = null;
    private Group groupAttributes = null;
    private Composite contentFp;
    private Composite contentFm;
    private Composite contentLarge;
    private Label label = null;
    private Label label1 = null;
    private Label label2 = null;
    private Spinner spnrA = null;
    private Spinner spnrB = null;
    private Spinner spnrP = null;
    private Button btnGenerateCurveFp = null;
    private Button btnGenerateCurveFm = null;
    private Button btnGenerateCurveLarge = null;
    private StyledText stGenerator = null;
    private Label label3 = null;
    private int prime[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67,
            71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157,
            163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251,
            257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353,
            359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457,
            461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571,
            577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673,
            677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797,
            809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911,
            919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997};
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
    private Text txtA;
    private Text txtB;
    private Text txtP;
    private Label lblP;
    private Spinner spnrM;
    private Combo cGenerator = null;
    private int[] elements;
    private PublicParametersWizardPage page;
    private ECPoint[] points;
    private int n;
    private StackLayout groupAttributesLayout; // @jve:decl-index=0:

    public PublicParametersComposite(Composite parent, int style, PublicParametersWizardPage p,
            EC c, ECPoint g) {
        super(parent, style);
        page = p;
        parent.setSize(600, 600);
        curve = c;
        initialize();
    }

    private void initialize() {
        curve = new ECFp();
        ((ECFp) curve).updateCurve(1, 1, 23);
        createGroupCurve();
        createGroupGenerator();
        // setSize(new org.eclipse.swt.graphics.Point(606, 450));
        setLayout(new GridLayout());
    }

    /**
     * This method initializes groupCurveType
     *
     */
    private void createGroupCurveType() {
        GridData gridData6 = new GridData();
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData6.grabExcessHorizontalSpace = true;
        gridData6.grabExcessVerticalSpace = true;
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData5 = new GridData();
        gridData5.grabExcessHorizontalSpace = true;
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.grabExcessVerticalSpace = true;
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.grabExcessHorizontalSpace = true;
        gridData3.grabExcessVerticalSpace = true;
        gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        groupCurveType = new Group(groupCurve, SWT.NONE);
        groupCurveType.setText(Messages.getString("ECDHWizPP.groupCurveType")); //$NON-NLS-1$
        groupCurveType.setLayoutData(gridData3);
        groupCurveType.setLayout(gridLayout1);
        rbtnFP = new Button(groupCurveType, SWT.RADIO);
        rbtnFP.setText("F(p)"); //$NON-NLS-1$
        rbtnFP.setLayoutData(gridData5);
        rbtnFP.setSelection(true);
        rbtnFP.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (rbtnFP.getSelection()) {
                    if (rbtnSmall.getSelection() && (curve == null || curve.getType() != ECFp.ECFp)) {
                        groupAttributesLayout.topControl = contentFp;
                        groupAttributes.layout();
                        curve = new ECFp();
                        ((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(),
                                spnrP.getSelection());
                        setGeneratorPoints(curve.getPoints());
                    } else if (rbtnLarge.getSelection()) {
                        fillCSelection();
                    }
                }
            }
        });
        rbtnFM = new Button(groupCurveType, SWT.RADIO);
        rbtnFM.setText("F(2^m)"); //$NON-NLS-1$
        rbtnFM.setLayoutData(gridData6);
        rbtnFM.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (rbtnFM.getSelection()) {
                    if (rbtnSmall.getSelection() && (curve == null || curve.getType() != ECFm.ECFm)) {
                        groupAttributesLayout.topControl = contentFm;
                        groupAttributes.layout();
                        curve = new ECFm();
                        ((ECFm) curve).setM(spnrM.getSelection());
                        if (cG.getItemCount() == 0) {
                            int[] ia = ((ECFm) curve).getIrreduciblePolinomials();
                            String[] s = new String[ia.length];
                            for (int i = 0; i < s.length; i++)
                                s[i] = intToBitString(ia[i]);
                            cG.setItems(s);
                        }
                        if (cG.getSelectionIndex() < 0)
                            cG.select(0);
                        ((ECFm) curve).setG(cG.getSelectionIndex(), true);
                        if (cA.getItemCount() == 0) {
                            elements = ((ECFm) curve).getElements();
                            setComboAB(curve.getA(), curve.getB());
                        }
                        ((ECFm) curve).setA(cA.getSelectionIndex(), true);
                        ((ECFm) curve).setB(cB.getSelectionIndex(), true);
                        setGeneratorPoints(curve.getPoints());
                    } else if (rbtnLarge.getSelection()) {
                        fillCSelection();
                    }
                }
            }
        });
        if (curve == null || curve.getType() == ECFp.ECFp)
            rbtnFP.setSelection(true);
        else
            rbtnFM.setSelection(true);
    }

    /**
     * This method initializes groupCurve
     *
     */
    private void createGroupCurve() {
        GridLayout gridLayout3 = new GridLayout();
        gridLayout3.numColumns = 2;
        groupCurve = new Group(this, SWT.NONE);
        groupCurve.setText(Messages.getString("ECDHWizPP.ellipticCurve")); //$NON-NLS-1$
        groupCurve.setLayout(gridLayout3);
        groupCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createGroupCurveType();
        createGroupCurveSize();
        createGroupAttributes();
    }

    /**
     * This method initializes groupGenerator
     *
     */
    private void createGroupGenerator() {
        GridData gridData17 = new GridData();
        gridData17.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
        GridData gridData15 = new GridData();
        gridData15.horizontalSpan = 3;
        gridData15.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
        gridData15.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        groupGenerator = new Group(this, SWT.NONE);
        groupGenerator.setText(Messages.getString("ECDHWizPP.groupGenerator")); //$NON-NLS-1$
        groupGenerator.setLayoutData(gridData1);
        groupGenerator.setLayout(gridLayout);
        stGenerator = new StyledText(groupGenerator, SWT.READ_ONLY);
        stGenerator.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        stGenerator.setLayoutData(gridData15);
        stGenerator.setText(Messages.getString("ECDHWizPP.textGenerator")); //$NON-NLS-1$
        btnCreateGenerator = new Button(groupGenerator, SWT.READ_ONLY);
        btnCreateGenerator.setText(Messages.getString("ECDHWizPP.btnCreateGenerator")); //$NON-NLS-1$
        btnCreateGenerator.setLayoutData(gridData17);
        btnCreateGenerator.setEnabled(false);
        btnCreateGenerator.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (cGenerator.getItemCount() > 1) {
                    Random r = new Random();
                    cGenerator.select(r.nextInt(cGenerator.getItemCount()));
                }
            }

        });
        label3 = new Label(groupGenerator, SWT.NONE);
        label3.setText("G ="); //$NON-NLS-1$
        new Label(groupGenerator, SWT.NONE); // filler label
        createCGenerator();
    }

    /**
     * This method initializes groupCurveSize
     *
     */
    private void createGroupCurveSize() {
        GridData gridData8 = new GridData();
        gridData8.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData8.grabExcessHorizontalSpace = true;
        gridData8.grabExcessVerticalSpace = true;
        gridData8.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData7.grabExcessHorizontalSpace = true;
        gridData7.grabExcessVerticalSpace = true;
        gridData7.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        GridData gridData4 = new GridData();
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData4.grabExcessVerticalSpace = true;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 2;
        groupCurveSize = new Group(groupCurve, SWT.NONE);
        groupCurveSize.setText(Messages.getString("ECDHWizPP.groupCurveSize")); //$NON-NLS-1$
        groupCurveSize.setLayoutData(gridData4);
        groupCurveSize.setLayout(gridLayout2);
        rbtnSmall = new Button(groupCurveSize, SWT.RADIO);
        rbtnSmall.setText(Messages.getString("ECDHWizPP.small")); //$NON-NLS-1$
        rbtnSmall.setLayoutData(gridData7);
        rbtnSmall.setSelection(true);
        rbtnSmall.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                if (rbtnFP.getSelection()) {
                    groupAttributesLayout.topControl = contentFp;
                    if (curve == null || curve.getType() != ECFp.ECFp) {
                        groupAttributesLayout.topControl = contentFp;
                        groupAttributes.layout();
                        curve = new ECFp();
                        ((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(),
                                spnrP.getSelection());
                        setGeneratorPoints(curve.getPoints());
                    }
                } else {
                    groupAttributesLayout.topControl = contentFm;
                    if (curve == null || curve.getType() != ECFm.ECFm) {
                        groupAttributesLayout.topControl = contentFm;
                        groupAttributes.layout();
                        curve = new ECFm();
                        ((ECFm) curve).setM(spnrM.getSelection());
                        if (cG.getItemCount() == 0) {
                            int[] ia = ((ECFm) curve).getIrreduciblePolinomials();
                            String[] s = new String[ia.length];
                            for (int i = 0; i < s.length; i++)
                                s[i] = intToBitString(ia[i]);
                            cG.setItems(s);
                        }
                        if (cG.getSelectionIndex() < 0)
                            cG.select(0);
                        ((ECFm) curve).setG(cG.getSelectionIndex(), true);
                        if (cA.getItemCount() == 0) {
                            elements = ((ECFm) curve).getElements();
                            setComboAB(curve.getA(), curve.getB());
                        }
                        ((ECFm) curve).setA(cA.getSelectionIndex(), true);
                        ((ECFm) curve).setB(cB.getSelectionIndex(), true);
                    }
                }

                setGeneratorPoints(curve.getPoints());
                fillCSelection();
                groupAttributes.layout();
            }
        });
        rbtnLarge = new Button(groupCurveSize, SWT.RADIO);
        rbtnLarge.setText(Messages.getString("ECDHWizPP.large")); //$NON-NLS-1$
        rbtnLarge.setLayoutData(gridData8);
        rbtnLarge.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                groupAttributesLayout.topControl = contentLarge;
                setCurve();
                fillCSelection();
                groupAttributes.layout();
            }
        });
    }

    /**
     * This method initializes groupAttributes
     *
     */
    private void createGroupAttributes() {
        groupAttributes = new Group(groupCurve, SWT.NONE);
        groupAttributes.setText(Messages.getString("ECDHWizPP.groupAttributes")); //$NON-NLS-1$
        groupAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        groupAttributesLayout = new StackLayout();
        groupAttributes.setLayout(groupAttributesLayout);

        createContentFp();
        createContentFm();
        createContentLarge();
        groupAttributesLayout.topControl = contentFp;
    }

    private void createContentFp() {
        contentFp = new Composite(groupAttributes, SWT.NONE);
        contentFp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentFp.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData.verticalIndent = 4;
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 2;
        label = new Label(contentFp, SWT.NONE);
        label.setText("a ="); //$NON-NLS-1$
        spnrA = new Spinner(contentFp, SWT.NONE);
        spnrA.setLayoutData(gridData);
        spnrA.setMinimum(0);
        spnrA.setSelection(1);
        spnrA.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                ((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP
                        .getSelection());
                setGeneratorPoints(curve.getPoints());
            }
        });
        label1 = new Label(contentFp, SWT.NONE);
        label1.setText("b ="); //$NON-NLS-1$
        spnrB = new Spinner(contentFp, SWT.NONE);
        spnrB.setLayoutData(gridData);
        spnrB.setMinimum(0);
        spnrB.setSelection(1);
        spnrB.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                ((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP
                        .getSelection());
                setGeneratorPoints(curve.getPoints());
            }
        });
        label2 = new Label(contentFp, SWT.NONE);
        label2.setText("p ="); //$NON-NLS-1$
        spnrP = new Spinner(contentFp, SWT.NONE);
        spnrP.setLayoutData(gridData);
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
                ((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP
                        .getSelection());
                setGeneratorPoints(curve.getPoints());
            }
        });
        btnGenerateCurveFp = new Button(contentFp, SWT.NONE);
        btnGenerateCurveFp.setText(Messages.getString("ECDHWizPP.btnGenerateCurve")); //$NON-NLS-1$
        btnGenerateCurveFp.setLayoutData(gridData3);
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
                setGeneratorPoints(curve.getPoints());
            }
        });
    }

    private void createContentFm() {
        contentFm = new Composite(groupAttributes, SWT.NONE);
        contentFm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentFm.setLayout(new GridLayout(2, false));

        Label label = new Label(contentFm, SWT.NONE);
        label.setText("m ="); //$NON-NLS-1$
        spnrM = new Spinner(contentFm, SWT.NONE);
        spnrM.setMaximum(6);
        spnrM.setSelection(4);
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
                    s[i] = intToBitString(ia[i]);
                cG.setItems(s);
            }
        });
        label = new Label(contentFm, SWT.NONE);
        label.setText("f(x) ="); //$NON-NLS-1$
        cG = new Combo(contentFm, SWT.NONE);
        cG.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        cG.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                ((ECFm) curve).setG(cG.getSelectionIndex(), true);
                elements = ((ECFm) curve).getElements();
                setComboAB(-1, -1);
            }
        });
        label = new Label(contentFm, SWT.NONE);
        label.setText("a ="); //$NON-NLS-1$
        cA = new Combo(contentFm, SWT.NONE);
        cA.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        cA.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                ((ECFm) curve).setA(cA.getSelectionIndex(), true);
                setGeneratorPoints(curve.getPoints());
            }
        });
        label = new Label(contentFm, SWT.NONE);
        label.setText("b ="); //$NON-NLS-1$
        cB = new Combo(contentFm, SWT.NONE);
        cB.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        cB.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                ((ECFm) curve).setB(cB.getSelectionIndex(), true);
                setGeneratorPoints(curve.getPoints());
            }
        });
        GridData gridData3 = new GridData();
        gridData3.horizontalSpan = 2;
        btnGenerateCurveFm = new Button(contentFm, SWT.NONE);
        btnGenerateCurveFm.setText(Messages.getString("ECDHWizPP.btnGenerateCurve")); //$NON-NLS-1$
        btnGenerateCurveFm.setLayoutData(gridData3);
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
                    s[i] = intToBitString(ip[i]);
                cG.setItems(s);
                if (ip.length == 1)
                    cG.select(0);
                else
                    cG.select(r.nextInt(ip.length));
                ((ECFm) curve).setG(cG.getSelectionIndex(), true); // set G

                elements = ((ECFm) curve).getElements();
                setComboAB(-1, -1);

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
                    setGeneratorPoints(curve.getPoints());
                } else {
                    cA.select(r.nextInt(cA.getItemCount()));
                    ((ECFm) curve).setA(cA.getSelectionIndex(), true);
                    int b = r.nextInt(cB.getItemCount());
                    int count = 0;
                    do {
                        cB.select(b);
                        ((ECFm) curve).setB(cB.getSelectionIndex(), true);
                        setGeneratorPoints(curve.getPoints());
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
    }

    private void createContentLarge() {
        contentLarge = new Composite(groupAttributes, SWT.NONE);
        contentLarge.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentLarge.setLayout(new GridLayout(4, false));
        cStandard = new Combo(contentLarge, SWT.NONE);
        cStandard.setLayoutData(new GridData(SWT.CENTER, SWT.BEGINNING, false, false, 2, 1));
        cStandard.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                fillCSelection();
            }
        });
        cCurve = new Combo(contentLarge, SWT.NONE);
        cCurve.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                setCurve();
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
                    fillCSelection();
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
        Label label = new Label(contentLarge, SWT.NONE);
        label.setText("a ="); //$NON-NLS-1$
        txtA = new Text(contentLarge, SWT.BORDER | SWT.READ_ONLY);
        txtA.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        label = new Label(contentLarge, SWT.NONE);
        label.setText("b ="); //$NON-NLS-1$
        txtB = new Text(contentLarge, SWT.BORDER | SWT.READ_ONLY);
        txtB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        lblP = new Label(contentLarge, SWT.NONE);
        lblP.setText("p ="); //$NON-NLS-1$
        txtP = new Text(contentLarge, SWT.BORDER | SWT.READ_ONLY);
        txtP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
    }

    private String intToBitString(int i) {
        String s = ""; //$NON-NLS-1$
        int j = i;
        while (j > 1) {
            s = (j % 2) + s;
            j /= 2;
        }
        s = (j % 2) + s;
        return s;
    }

    private String intToBitString(int i, int length) {
        String s = ""; //$NON-NLS-1$
        int j = i;
        for (int k = 0; k < length; k++) {
            s = (j % 2) + s;
            j /= 2;
        }
        return s;
    }

    private void setComboAB(int a, int b) {
        cA.removeAll();
        cB.removeAll();
        if (elements == null) {
            return;
        }
        String[] sA = new String[elements.length + 1];
        String[] sB = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            sA[i] = intToBitString(elements[i], spnrM.getSelection());
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
        setGeneratorPoints(curve.getPoints());
    }

    /**
     * This method initializes cGenerator
     *
     */
    private void createCGenerator() {
        GridData gridData9 = new GridData();
        gridData9.horizontalSpan = 2;
        gridData9.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData9.grabExcessHorizontalSpace = true;
        cGenerator = new Combo(groupGenerator, SWT.READ_ONLY);
        cGenerator.setLayoutData(gridData9);
        setGeneratorPoints(curve.getPoints());
    }

    private void setGeneratorPoints(ECPoint[] p) {
        page.setErrorMessage(Messages.getString("PublicParametersComposite.34")); //$NON-NLS-1$

        if (cGenerator != null)
            cGenerator.removeAll();
        page.setPageComplete(false);
        btnCreateGenerator.setEnabled(false);
        points = null;
        if (p == null)
            return;

        if (rbtnFP.getSelection()) {
            if (p.length == ((ECFp) curve).getP() || isPrime(p.length))
                return;

            n = ((ECFp) curve).getP() + 1;
            ArrayList<ECPoint> list = new ArrayList<ECPoint>();
            for (int i = 0; i < p.length; i++) {
                if (p[i].y != 0) {
                    for (int j = 2; j <= ((ECFp) curve).getP(); j++) {
                        if (p[i].equals(new ECPoint(66, 34)) && j == 70)
                            p[i] = p[i];
                        if (curve.multiplyPoint(p[i], j) == null) {
                            // if(j <= n && p.length % j == 0 && p.length / j <= 4) {
                            // if(j < n)
                            // list.clear();
                            n = j;
                            list.add(p[i]);
                            // }
                            j = ((ECFp) curve).getP() + 1;
                        }
                    }
                }
            }

            if (n == ((ECFp) curve).getP() + 1)
                return;

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
                return;

            points = new ECPoint[list.size()];
            for (int i = 0; i < points.length; i++)
                points[i] = list.get(i);
        }

        String[] s = new String[points.length];

        for (int i = 0; i < points.length; i++) {
            if (rbtnFP.getSelection())
                s[i] = "(" + points[i].x + "," + points[i].y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            else {
                s[i] = "(" + intToBitString(points[i].x == elements.length ? 0 : elements[points[i].x], spnrM.getSelection()) + "," + intToBitString(points[i].y == elements.length ? 0 : elements[points[i].y], spnrM.getSelection()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

        if (cGenerator != null) {
            cGenerator.setItems(s);
            cGenerator.select(0);
            btnCreateGenerator.setEnabled(true);
            page.setPageComplete(true);
            page.setErrorMessage(null);
        }
    }

    private void fillCSelection() {
        String[] s;
        if (rbtnFP.getSelection()) {
            s = LargeCurves.standardFp;
        } else {
            s = LargeCurves.standardFm;
        }
        cStandard.setItems(s);
        cStandard.select(0);
        fillCCurve();
    }

    private void fillCCurve() {
        String[] s;
        if (rbtnFP.getSelection()) {
            s = LargeCurves.getNamesFp(cStandard.getSelectionIndex());
        } else {
            s = LargeCurves.getNamesFm(cStandard.getSelectionIndex());
        }
        cCurve.setItems(s);
        cCurve.select(0);
        setCurve();
    }

    private void setCurve() {
        if (rbtnFP.getSelection()) {
            FlexiBigInt[] fbi = LargeCurves.getCurveFp(cStandard.getSelectionIndex(), cCurve
                    .getSelectionIndex());
            largeCurve = new EllipticCurveGFP(new GFPElement(fbi[0], fbi[2]), new GFPElement(
                    fbi[1], fbi[2]), fbi[2]);
            txtA.setText(fbi[0].toString(16));
            txtB.setText(fbi[1].toString(16));
            txtP.setText(fbi[2].toString(16));
            lblP.setText("p ="); //$NON-NLS-1$
            fbiOrderG = fbi[4];
            pointG = new PointGFP(fbi[3].toByteArray(), (EllipticCurveGFP) largeCurve);
        } else {
            FlexiBigInt[] fbi = LargeCurves.getCurveFm(cStandard.getSelectionIndex(), cCurve
                    .getSelectionIndex());
            GF2nPolynomialField field = new GF2nPolynomialField(fbi[2].intValue());
            largeCurve = new EllipticCurveGF2n(new GF2nPolynomialElement(field, fbi[0]
                    .toByteArray()), new GF2nPolynomialElement(field, fbi[1].toByteArray()), fbi[2]
                    .intValue());
            txtA.setText(fbi[0].toString(16));
            txtB.setText(fbi[1].toString(16));
            txtP.setText(fbi[2].toString(16));
            lblP.setText("m ="); //$NON-NLS-1$
            fbiOrderG = fbi[4];
            pointG = new PointGF2n(fbi[3].toByteArray(), (EllipticCurveGF2n) largeCurve);
        }
        cGenerator.setItems(new String[] {"(" + pointG.getX() + ", " + pointG.getY() + ")"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        cGenerator.select(0);
        page.setPageComplete(true);
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

} // @jve:decl-index=0:visual-constraint="10,10"
