// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.secretsharing.views;

import java.math.BigInteger;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.secretsharing.algorithm.Point;
import org.jcryptool.visual.secretsharing.algorithm.ShamirsSecretSharing;

/**
 * @author Oryal Inel
 * @version 1.0.0
 */
public class ShamirsCompositeGraphical extends Composite implements Constants {

    private Label closeLabel;
    private Label seperatorLabel;
    private Label openLabel;
    private Label shareLabel;
    private Text yText;
    private Text xText;
    private StyledText stInfo;
    private StyledText stValue;
    private Label reconstructPxLabel;
    private Composite compositeReconstruction;
    private ScrolledComposite scrolledReconstruction;
    private Button deselectAllButton;
    private Button reconstructButton;
    private Composite compositeShares;

    private Button computeSharesButton;
    private Label pxLabel;
    private Label polynomLabel;
    private Button selectCoefficientButton;
    private Button numericalButton;
    private Button graphicalButton;
    private Group groupReconstruction;
    private StyledText stPolynom;
    private Label coefficentLabel;
    private Text secretText;
    private Label secretLabel;
    private Label numberOfConcernedLabel;
    private Composite canvasCurve;

    private Composite content;
    private SecretSharingView view;
    private Composite compositeIntro = null;

    private StyledText stDescription = null;

    private Group groupCurve = null;

    private Group groupSettings = null;

    private Group groupModus = null;
    private Group groupParameter = null;
    private Group groupShares = null;

    private Button selectAllButton = null;

    private Spinner spnrN = null;

    private Label numberForReconstructionLabel;
    private Spinner spnrT;
    private Label modulLabel;
    private Text modulText;

    private BigInteger[] coefficients;
    private BigInteger secret;
    private BigInteger modul;
    private ShamirsSecretSharing shamirsSecretSharing;
    private Point[] shares;

    private String result[];

    private Button[] sharesUseCheckButtonSet;

    private ScrolledComposite scrolledShares;

    private Vector<BigInteger[]> subpolynomial;
    private BigInteger[] reconstructedPolynomial;

    private int gridSizeX;
    private int gridSizeY;

    private Text sharesYCoordinateText;
    private Text shareYCoordinateModText;
    private VerifyListener numberOnlyVerifyListenerModul;
    private VerifyListener numberOnlyVerifyListenerSecret;

    private int mousePosX;
    private int mousePosY;
    private int xAxisGap;
    private int yAxisGap;
    private int pointValue;

    /**
     * Create the composite
     *
     * @param parent
     * @param style
     */
    public ShamirsCompositeGraphical(Composite parent, int style, SecretSharingView view) {
        super(parent, style);
        this.view = view;
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
        scrolledComposite.setMinSize(content.computeSize(862, 658));
    }

    @Override
    protected void checkSubclass() {
    }

    /**
     * Created the info header
     */
    private void createCompositeIntro() {
        compositeIntro = new Composite(content, SWT.NONE);
        compositeIntro.setBackground(WHITE);
        compositeIntro.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        compositeIntro.setLayout(new GridLayout(1, false));

        Label label = new Label(compositeIntro, SWT.NONE);
        label.setFont(FontService.getHeaderFont());
        label.setBackground(WHITE);
        label.setText(MESSAGE_TITLE);

        stDescription = new StyledText(compositeIntro, SWT.READ_ONLY);
        stDescription.setText(MESSAGE_DESCRIPTION + " " + MESSAGE_LAGRAGE + " " + MESSAGE_FORMULAR);
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    private void createGroupCurve() {
        groupCurve = new Group(content, SWT.NONE);
        groupCurve.setLayout(new GridLayout(11, false));
        groupCurve.setText(MESSAGE_GRAPH);
        final GridData gd_groupCurve = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_groupCurve.heightHint = 558;
        groupCurve.setLayoutData(gd_groupCurve);

        createCanvasCurve();

        reconstructButton = new Button(groupCurve, SWT.NONE);
        reconstructButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                /*
                 * clear the composite for the next visualization
                 */
                Control[] tmpWidgets = compositeReconstruction.getChildren();
                for (int i = 0; i < tmpWidgets.length; i++) {
                    tmpWidgets[i].dispose();
                }
                compositeReconstruction.pack();

                Vector<Point> tmpPointSet = new Vector<Point>();

                for (int i = 0; i < sharesUseCheckButtonSet.length; i++) {
                    if (sharesUseCheckButtonSet[i].getSelection()) {
                        tmpPointSet.add(shares[i]);
                    }
                }
                Point[] pointSet = new Point[tmpPointSet.size()];
                for (int i = 0; i < tmpPointSet.size(); i++) {
                    pointSet[i] = tmpPointSet.get(i);
                }

                reconstructedPolynomial = shamirsSecretSharing.interpolatePoints(pointSet, modul);

                subpolynomial = shamirsSecretSharing.getSubPolynomialNumerical();

                createReconstruction(subpolynomial.size());

                String tmpPolynomial = createPolynomialString(reconstructedPolynomial);
                if (tmpPolynomial.charAt(0) == '0' && tmpPolynomial.length() > 1) {
                    tmpPolynomial = tmpPolynomial.substring(4);
                }
                stValue.setText(tmpPolynomial);

                StyleRange styleValue = new StyleRange();
                StyleRange styleInfo = new StyleRange();
                styleValue.start = 0;
                styleInfo.start = 0;
                styleValue.length = stValue.getText().length();

                if (comparePolynomial(reconstructedPolynomial, coefficients)) {
                    styleValue.foreground = GREEN;

                    stInfo.setText(MESSAGE_RECONSTRUCTION_TRUE);
                    stInfo.setBackground(GREEN);
                } else {
                    styleValue.foreground = RED;

                    stInfo.setText(MESSAGE_RECONSTRUCTION_FALSE);
                    stInfo.setBackground(RED);
                }
                styleInfo.length = stInfo.getText().length();
                styleInfo.fontStyle = SWT.BOLD;
                styleValue.fontStyle = SWT.BOLD;
                stInfo.setStyleRange(styleInfo);
                stValue.setStyleRange(styleValue);

                reconstructPxLabel.setEnabled(true);

                canvasCurve.redraw();

            }
        });
        reconstructButton.setEnabled(false);
        reconstructButton.setText(MESSAGE_RECONSTRUCT);
        selectAllButton = new Button(groupCurve, SWT.NONE);
        selectAllButton.setEnabled(false);
        selectAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                for (int i = 0; i < sharesUseCheckButtonSet.length; i++) {
                    sharesUseCheckButtonSet[i].setSelection(true);
                }
                reconstructButton.setEnabled(true);
                canvasCurve.redraw();
            }
        });
        selectAllButton.setText(MESSAGE_SELECT_ALL);

        deselectAllButton = new Button(groupCurve, SWT.NONE);
        deselectAllButton.setEnabled(false);
        deselectAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                for (int i = 0; i < sharesUseCheckButtonSet.length; i++) {
                    sharesUseCheckButtonSet[i].setSelection(false);
                }
                reconstructButton.setEnabled(false);
                canvasCurve.redraw();
            }
        });
        deselectAllButton.setText(MESSAGE_DESELECT_ALL);

        final Label dummyLabel = new Label(groupCurve, SWT.NONE);
        dummyLabel.setText("dummy");
        dummyLabel.setVisible(false);

        shareLabel = new Label(groupCurve, SWT.NONE);
        shareLabel.setText("Share");
        shareLabel.setVisible(false);

        openLabel = new Label(groupCurve, SWT.NONE);
        openLabel.setText("(");
        openLabel.setVisible(false);

        xText = new Text(groupCurve, SWT.READ_ONLY | SWT.BORDER);
        final GridData gd_xText = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_xText.heightHint = 12;
        xText.setLayoutData(gd_xText);
        xText.setVisible(false);

        seperatorLabel = new Label(groupCurve, SWT.NONE);
        seperatorLabel.setText("|");
        seperatorLabel.setVisible(false);

        yText = new Text(groupCurve, SWT.READ_ONLY | SWT.BORDER);
        final GridData gd_yText = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd_yText.heightHint = 12;
        yText.setLayoutData(gd_yText);
        yText.setVisible(false);

        closeLabel = new Label(groupCurve, SWT.NONE);
        closeLabel.setText(")");
        closeLabel.setVisible(false);

        final Label phLabel = new Label(groupCurve, SWT.NONE);
        phLabel.setText("ph");
        phLabel.setVisible(false);

        reconstructPxLabel = new Label(groupCurve, SWT.NONE);
        reconstructPxLabel.setEnabled(false);
        reconstructPxLabel.setText(MESSAGE_RECONSTRUCTION);

        stValue = new StyledText(groupCurve, SWT.READ_ONLY | SWT.BORDER);
        stValue.setEnabled(false);
        final GridData gd_stValue = new GridData(SWT.FILL, SWT.CENTER, false, false, 10, 1);
        stValue.setLayoutData(gd_stValue);

        stInfo = new StyledText(groupCurve, SWT.WRAP | SWT.READ_ONLY | SWT.BORDER);
        stInfo.setWordWrap(true);
        stInfo.setEnabled(false);
        final GridData gd_stInfo = new GridData(SWT.FILL, SWT.FILL, true, false, 11, 1);
        gd_stInfo.heightHint = 36;
        stInfo.setLayoutData(gd_stInfo);
        stInfo.setEditable(true);

    }

    /**
     * Creates the canvas group
     */
    private void createCanvasCurve() {
        canvasCurve = new Composite(groupCurve, SWT.EMBEDDED);
        canvasCurve.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(final MouseEvent e) {
                mousePosX = e.x;
                mousePosY = e.y;

                if (shares != null) {
                    Point point = nearSharePoint(shares, mousePosX, mousePosY);

                    if (point != null) {
                        xText.setText(point.getX().toString());
                        yText.setText(point.getY().toString());
                        makePointVisible(true);
                    } else if (pointValue == Integer.MAX_VALUE) {
                        boolean found = false;
                        for (int i = 0; i < shares.length; i++) {
                            int tmpX = shares[i].getX().intValue() * 60 + xAxisGap;
                            int tmpY = 408 + 1 * 6;

                            if ((tmpX - 3 == mousePosX || tmpX - 2 == mousePosX || tmpX - 1 == mousePosX
                                    || tmpX == mousePosX || tmpX + 1 == mousePosX || tmpX + 2 == mousePosX || tmpX + 3 == mousePosX)
                                    && (tmpY - 3 == mousePosY || tmpY - 2 == mousePosY || tmpY - 1 == mousePosY
                                            || tmpY == mousePosY || tmpY + 1 == mousePosY || tmpY + 2 == mousePosY || tmpY + 3 == mousePosY)) {
                                xText.setText(shares[i].getX().toString());
                                yText.setText(shares[i].getY().toString());
                                found = true;
                            }
                        }
                        if (found) {
                            makePointVisible(true);
                        } else {
                            makePointVisible(false);
                        }
                    } else {
                        makePointVisible(false);
                    }
                }
            }
        });
        canvasCurve.setBackground(WHITE);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 11, 1);
        gridData.widthHint = 506;
        canvasCurve.setLayoutData(gridData);

        canvasCurve.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if (!selectCoefficientButton.isEnabled()) {
                    drawPolynomial(e);
                }
            }
        });
    }

    /**
     * compute the y value for a given x value for the reconstructed polynomial
     *
     * @param x is the point to evaluate
     * @return the corresponding y value
     */
    private float valueAtReconstruction(float x) {
        float value = 0;
        for (int i = 0; i < reconstructedPolynomial.length; i++) {
            value += reconstructedPolynomial[i].intValue() * Math.pow(x, i);
        }
        return value;
    }

    /**
     * compute the y value for a given x value for the original polynomial
     *
     * @param x is the point to evaluate
     * @return the corresponding y value
     */
    private float valueAt(float x) {
        float value = 0;
        for (int i = 0; i < coefficients.length; i++) {
            value += coefficients[i].intValue() * Math.pow(x, i);
        }
        return value;
    }

    /**
     * @param e the PaintEvent that represents the graphic context
     */
    private void drawPolynomial(PaintEvent e) {
        GC gc = e.gc;
        org.eclipse.swt.graphics.Point size = canvasCurve.getSize();
        gridSizeY = 6;
        gridSizeX = 60;

        gc.setForeground(LIGHTGREY);

        /*
         * draw the grid in x direction
         */
        xAxisGap = 0;
        for (int i = 0; i < size.x; i += gridSizeX) {
            if (xAxisGap + gridSizeX <= size.x / 2) {
                xAxisGap += gridSizeX;
            }
            xAxisGap = gridSizeX;

            gc.drawLine(i, 0, i, size.y);
        }

        /*
         * draw the grid in y direction
         */
        yAxisGap = 0;
        for (int i = 0; i < size.y; i += gridSizeY) {
            if (yAxisGap + gridSizeY <= size.y / 2) {
                yAxisGap += gridSizeY;
            }
            yAxisGap = gridSizeY * ((size.y / gridSizeY) - 6);

            gc.drawLine(0, i, size.x, i);
        }

        /*
         * draw the axis
         */
        gc.setForeground(BLACK);

        gc.drawLine(xAxisGap, 0, xAxisGap, size.y);
        gc.drawLine(0, yAxisGap, size.x, yAxisGap);

        int labeljumps = 5;
        int gapSmall = 3;
        int gapBig = 7;
        int textOffset = gapBig + 8;
        int fontWidth = 6;
        int fontHeight = 10;
        int numberLength = 0;

        /*
         * draw the x marker
         */
        int i = 0;
        for (int x = xAxisGap; x < size.x; x += gridSizeX) {
            i++;
            numberLength = String.valueOf(i).length();
            /*
             * thin lines
             */
            gc.drawLine(x, yAxisGap - gapSmall, x, yAxisGap + gapSmall);
            gc.drawLine(xAxisGap - i * gridSizeX, yAxisGap - gapSmall, xAxisGap - i * gridSizeX, yAxisGap + gapSmall);
            /*
             * thick lines
             */
            if ((i - 1) % labeljumps == 0) {
                gc.drawLine(x, yAxisGap - gapBig, x, yAxisGap + gapBig);
                gc.drawLine(xAxisGap - i * labeljumps * gridSizeX, yAxisGap - gapBig, xAxisGap - i * labeljumps
                        * gridSizeX, yAxisGap + gapBig);
                if (i != 1) {
                    gc.drawText(String.valueOf(i - 1), x - (fontWidth * numberLength) / 2, yAxisGap - gapBig
                            + textOffset, true);
                    gc.drawText(String.valueOf(i - 1), xAxisGap - i * labeljumps * gridSizeX, yAxisGap + gapBig, true);
                }
            }
        }

        /*
         * draw the y markers
         */
        i = 0;
        for (int y = yAxisGap; y >= 0; y -= gridSizeY) {
            i++;
            numberLength = String.valueOf(i).length();
            /*
             * thin lines
             */
            gc.drawLine(xAxisGap - gapSmall, y, xAxisGap + gapSmall, y);
            gc.drawLine(xAxisGap - gapSmall, yAxisGap + i * gridSizeY, xAxisGap + gapSmall, yAxisGap + i * gridSizeY);
            /*
             * thick lines
             */
            if ((i - 1) % labeljumps == 0) {
                gc.drawLine(xAxisGap - gapBig, y, xAxisGap + gapBig, y);
                gc.drawLine(xAxisGap - gapBig, yAxisGap + i * labeljumps * gridSizeY, xAxisGap + gapBig, yAxisGap + i
                        * labeljumps * gridSizeY);
                if (i != 1) {
                    gc.drawText(String.valueOf(i - 1), xAxisGap - gapBig - (fontWidth * numberLength) - 3, y
                            - fontHeight / 2 - 4, true);
                }
            }
        }
        gc.drawText(String.valueOf(-5), xAxisGap - gapBig - 2 - (fontWidth * numberLength), yAxisGap + 5 * gridSizeY
                - 8, true);

        /*
         * new GraphicContent for drawing the polynomial curve
         */
        GC polynomial = new GC(canvasCurve);
        Path polynomPath = new Path(null);
        float dx = 2.0f / gridSizeY;
        polynomPath.moveTo(-10, valueAt(-10));

        for (float x = -10.0f; x < size.x / 2; x += dx) {
            polynomPath.lineTo(x, valueAt(x));

        }
        polynomial.setForeground(BLUE);

        Transform polynomTransform = new Transform(null);
        polynomTransform.translate(xAxisGap, yAxisGap);
        polynomTransform.scale(gridSizeX, -gridSizeY);
        polynomial.setTransform(polynomTransform);

        polynomial.drawPath(polynomPath);

        /*
         * new GraphicContent for drawing the reconstructed polynomial curve
         */
        if (stValue.getText().length() > 0) {
            GC subPolynomial = new GC(canvasCurve);
            Transform subPolynomialTransform = new Transform(null);
            subPolynomialTransform.translate(xAxisGap, yAxisGap);
            subPolynomialTransform.scale(gridSizeX, -gridSizeY);

            Path subPolynomialPath = new Path(null);
            subPolynomialPath.moveTo(-10, valueAt(-10));
            for (float x = -10.0f; x < size.x / 2; x += dx) {
                subPolynomialPath.lineTo(x, valueAtReconstruction(x));
            }

            if (comparePolynomial(coefficients, reconstructedPolynomial)) {
                subPolynomial.setForeground(GREEN);
            } else {
                subPolynomial.setForeground(RED);
            }
            subPolynomial.setTransform(polynomTransform);
            subPolynomial.drawPath(subPolynomialPath);
        }

        /*
         * new GraphicContent for drawing shares points
         */
        GC points = new GC(canvasCurve);
        Transform pointTransform = new Transform(null);
        pointTransform.translate(xAxisGap, yAxisGap);
        points.setTransform(pointTransform);

        pointValue = (int) valueAt(shares.length);
        for (int k = 1; k <= shares.length; k++) {
            if (sharesUseCheckButtonSet[k - 1].getSelection()) {
                points.setBackground(RED);
            } else {
                points.setBackground(DARKPURPLE);
            }
            if (pointValue == Integer.MAX_VALUE) {
                points.fillOval(gridSizeX * k - 3, (pointValue) * -gridSizeY - 3, 6, 6);
            } else {
                points.fillOval(gridSizeX * k - 3, ((int) valueAt(k)) * -gridSizeY - 3, 6, 6);
            }
        }
    }

    /**
     * Creates the settings group
     */
    private void createGroupSettings() {
        GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, false, false);
        gridData2.heightHint = 508;
        gridData2.widthHint = 300;
        groupSettings = new Group(content, SWT.NONE);
        groupSettings.setLayout(new GridLayout());
        groupSettings.setText(MESSAGE_SETTINGS);
        groupSettings.setLayoutData(gridData2);

        createGroupModus();
        createGroupParameter();
        createGroupShares();
        createGroupReconstruction();
    }

    /**
     * Creates the select modus group
     */
    private void createGroupModus() {
        groupModus = new Group(groupSettings, SWT.NONE);
        groupModus.setText(MESSAGE_MODUS);
        groupModus.setLayout(new GridLayout(2, true));
        groupModus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        graphicalButton = new Button(groupModus, SWT.RADIO);
        graphicalButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                view.showGraphical();
            }
        });
        graphicalButton.setText(MESSAGE_GRAPHICAL);
        graphicalButton.setSelection(true);

        numericalButton = new Button(groupModus, SWT.RADIO);
        numericalButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                view.showNumerical();
            }
        });
        numericalButton.setText(MESSAGE_NUMERICAL);
    }

    /**
     * Creates the parameter group
     */
    private void createGroupParameter() {
        groupParameter = new Group(groupSettings, SWT.NONE);
        final GridData gd_groupParameter = new GridData(SWT.FILL, SWT.CENTER, true, false);
        groupParameter.setLayoutData(gd_groupParameter);
        groupParameter.setText(MESSAGE_PARAMETER);

        numberOfConcernedLabel = new Label(groupParameter, SWT.NONE);
        numberOfConcernedLabel.setBounds(10, 20, 174, 15);
        numberOfConcernedLabel.setText(MESSAGE_CONCERNED_PERSONS);

        spnrN = new Spinner(groupParameter, SWT.BORDER);
        spnrN.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                spnrT.setMaximum(spnrN.getSelection());
                spnrT.setSelection(spnrT.getSelection());

            }
        });
        spnrN.setBounds(225, 17, 55, 20);
        spnrN.setMinimum(2);
        spnrN.setMaximum(500);

        numberForReconstructionLabel = new Label(groupParameter, SWT.NONE);
        numberForReconstructionLabel.setBounds(10, 46, 210, 15);
        numberForReconstructionLabel.setText(MESSAGE_RECONSTRUCT_PERSONS);

        spnrT = new Spinner(groupParameter, SWT.BORDER);
        spnrT.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (spnrT.getSelection() >= spnrN.getSelection()) {
                    spnrT.setMaximum(spnrT.getSelection() + 1);
                    spnrN.setSelection(spnrT.getSelection());
                }
            }

        });
        spnrT.setBounds(225, 43, 55, 20);
        spnrT.setMinimum(2);
        spnrT.setMaximum(2);

        modulLabel = new Label(groupParameter, SWT.NONE);
        modulLabel.setBounds(10, 72, 152, 15);
        modulLabel.setText(MESSAGE_MODUL);

        modulText = new Text(groupParameter, SWT.BORDER);
        modulText.setBounds(168, 69, 113, 20);

        numberOnlyVerifyListenerModul = new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                /*
                 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
                 */
                if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127) {
                    if (modulText.getText().length() == 0 && e.text.compareTo("0") == 0) {
                        e.doit = false;
                    } else if (modulText.getSelection().x == 0 && e.keyCode == 48) {
                        e.doit = false;
                    } else {
                        e.doit = true;
                    }
                } else {
                    e.doit = false;
                }
            }
        };
        modulText.addVerifyListener(numberOnlyVerifyListenerModul);

        secretLabel = new Label(groupParameter, SWT.NONE);
        secretLabel.setText(MESSAGE_SECRET);
        secretLabel.setBounds(10, 101, 105, 15);

        secretText = new Text(groupParameter, SWT.BORDER);
        secretText.setBounds(121, 98, 159, 20);

        numberOnlyVerifyListenerSecret = new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                /*
                 * keyCode == 8 is BACKSPACE and keyCode == 48 is ZERO and keyCode == 127 is DEL
                 */
                if (e.text.matches("[0-9]") || e.keyCode == 8 || e.keyCode == 127) {
                    if (secretText.getText().length() == 0 && e.text.compareTo("0") == 0) {
                        e.doit = false;
                    } else if (secretText.getSelection().x == 0 && e.keyCode == 48) {
                        e.doit = false;
                    } else {
                        e.doit = true;
                    }
                } else {
                    e.doit = false;
                }
            }
        };
        secretText.addVerifyListener(numberOnlyVerifyListenerSecret);

        coefficentLabel = new Label(groupParameter, SWT.NONE);
        coefficentLabel.setText(MESSAGE_COEFFICIENT);
        coefficentLabel.setBounds(10, 127, 58, 15);

        selectCoefficientButton = new Button(groupParameter, SWT.NONE);
        selectCoefficientButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                int statusPrime = 0;
                int statusSecret = 0;

                result = new String[2];
                result[0] = modulText.getText();
                result[1] = secretText.getText();

                coefficients = new BigInteger[spnrT.getSelection()];

                String tmpModul = modulText.getText();
                String tmpSecret = secretText.getText();

                /*
                 * check if the input(modul and secret) isEmpty
                 */
                if (tmpModul.length() > 0 && tmpSecret.length() > 0) {
                    boolean isPrime = false;
                    modul = new BigInteger(tmpModul);
                    secret = new BigInteger(tmpSecret);
                    isPrime = modul.isProbablePrime(2000000);

                    if (modul.compareTo(new BigInteger(spnrN.getText())) > 0) {
                        /*
                         * check if the modul is prime
                         */
                        if (!isPrime) {
                            PrimeDialog primeDialog = new PrimeDialog(getDisplay().getActiveShell(), modul, result);
                            statusPrime = primeDialog.open();

                            if (statusPrime == 0) {
                                modulText.removeVerifyListener(numberOnlyVerifyListenerModul);
                                modulText.setText(result[0]);
                                modul = new BigInteger(modulText.getText());
                                modulText.addVerifyListener(numberOnlyVerifyListenerModul);
                            }
                        }
                        /*
                         * check if the secret is smaller than the modul
                         */
                        if (secret.compareTo(modul) >= 0 && statusPrime != 1) {

                            SecretDialog secretDialog = new SecretDialog(getDisplay().getActiveShell(), secret, result);
                            statusSecret = secretDialog.open();
                            if (statusSecret == 0) {
                                secretText.removeVerifyListener(numberOnlyVerifyListenerSecret);
                                secretText.setText(result[1]);
                                secret = new BigInteger(secretText.getText());
                                secretText.addVerifyListener(numberOnlyVerifyListenerSecret);
                            }
                        }
                        /*
                         * if the precondition is correct and the input is not empty than select the coefficients
                         */
                        if (statusPrime == 0 && statusSecret == 0) {
                            String polynomialString = "";
                            CoefficientDialog cdialog = new CoefficientDialog(getDisplay().getActiveShell(), spnrT
                                    .getSelection(), secret, coefficients, modul);
                            int statusCoefficient = cdialog.open();
                            if (statusCoefficient == 0) {
                                /*
                                 * make a polynomial string
                                 */
                                polynomialString = createPolynomialString(coefficients);

                                StyleRange stPolynomStyle = new StyleRange();
                                stPolynomStyle.start = 0;
                                stPolynomStyle.length = polynomialString.length();
                                stPolynomStyle.fontStyle = SWT.BOLD;
                                stPolynomStyle.foreground = BLUE;
                                stPolynom.setText(polynomialString);
                                stPolynom.setStyleRange(stPolynomStyle);

                                if (polynomialString.contains("-") || polynomialString.contains("+")) {
                                    computeSharesButton.setEnabled(true);
                                } else {
                                    computeSharesButton.setEnabled(false);
                                }
                                modulText.setEnabled(false);
                                secretText.setEnabled(false);
                                spnrN.setEnabled(false);
                                spnrT.setEnabled(false);
                                spnrN.setEnabled(false);
                                spnrT.setEnabled(false);
                                selectCoefficientButton.setEnabled(false);
                            }
                        }
                    }
                }
            }
        });
        selectCoefficientButton.setText(MESSAGE_SELECT);
        selectCoefficientButton.setBounds(107, 124, 173, 20);

        polynomLabel = new Label(groupParameter, SWT.NONE);
        polynomLabel.setText(MESSAGE_POLYNOM);
        polynomLabel.setBounds(10, 148, 270, 15);

        stPolynom = new StyledText(groupParameter, SWT.READ_ONLY | SWT.BORDER);
        stPolynom.setBounds(44, 169, 236, 20);

        pxLabel = new Label(groupParameter, SWT.NONE);
        pxLabel.setText(MESSAGE_P);
        pxLabel.setBounds(10, 169, 28, 15);

        computeSharesButton = new Button(groupParameter, SWT.NONE);
        computeSharesButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                BigInteger n = new BigInteger(String.valueOf(spnrN.getSelection()));
                BigInteger t = new BigInteger(String.valueOf(spnrT.getSelection()));
                modul = new BigInteger(modulText.getText());
                shares = new Point[n.intValue()];
                for (int j = 0; j < shares.length; j++) {
                    shares[j] = new Point(new BigInteger(String.valueOf(j + 1)));
                }

                shamirsSecretSharing = new ShamirsSecretSharing(t, n, modul, Mode.GRAPHICAL);
                shamirsSecretSharing.setCoefficient(coefficients);

                shares = shamirsSecretSharing.computeShares(shares);
                createShares(shares.length);

                computeSharesButton.setEnabled(false);
                selectAllButton.setEnabled(true);
                deselectAllButton.setEnabled(true);

                canvasCurve.redraw();

            }
        });
        computeSharesButton.setEnabled(false);
        computeSharesButton.setText(MESSAGE_COMPUTE_SHARES);
        computeSharesButton.setBounds(10, 195, 270, 20);

    }

    /**
     * Creates the share group
     */
    private void createGroupShares() {
        groupShares = new Group(groupSettings, SWT.NONE);
        groupShares.setLayout(new GridLayout());
        groupShares.setText(MESSAGE_SHARES);
        final GridData gd_groupShares = new GridData(SWT.FILL, SWT.FILL, true, false);
        groupShares.setLayoutData(gd_groupShares);

        scrolledShares = new ScrolledComposite(groupShares, SWT.V_SCROLL | SWT.BORDER);
        scrolledShares.setExpandHorizontal(true);
        final GridData gd_scrolledShares = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_scrolledShares.heightHint = 109;
        scrolledShares.setLayoutData(gd_scrolledShares);

        compositeShares = new Composite(scrolledShares, SWT.NONE);
        compositeShares.setLocation(0, 0);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 10;
        compositeShares.setLayout(gridLayout);

        /*
         * BEGIN dummy for design only
         */
        // final Label shareLabel = new Label(compositeShares, SWT.NONE);
        // shareLabel.setText("Share");
        //
        // final Label label = new Label(compositeShares, SWT.NONE);
        // label.setText("=");
        //
        // final Label label_1 = new Label(compositeShares, SWT.NONE);
        // label_1.setText("(");
        //
        // final Label pLabel = new Label(compositeShares, SWT.NONE);
        // pLabel.setText("P");
        //
        // final Label label_2 = new Label(compositeShares, SWT.NONE);
        // label_2.setText("|");
        //
        // final Text text = new Text(compositeShares, SWT.BORDER);
        // final GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true,
        // false);
        // gd_text.widthHint = 65;
        // text.setLayoutData(gd_text);
        //
        // final Label label_4 = new Label(compositeShares, SWT.NONE);
        // label_4.setText("Label");
        //
        // final Text text_1 = new Text(compositeShares, SWT.BORDER);
        // text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
        // false));
        //
        // final Label label_3 = new Label(compositeShares, SWT.NONE);
        // label_3.setText(")");
        //
        // final Button button = new Button(compositeShares, SWT.CHECK);
        // compositeShares.setSize(274, 105);
        /*
         * END dummy
         */

        scrolledShares.setContent(compositeShares);
        compositeShares.pack();

    }

    /**
     * Creates the reconstruction group
     */
    private void createGroupReconstruction() {
        groupReconstruction = new Group(groupSettings, SWT.NONE);
        GridLayout gridLayout5 = new GridLayout();
        GridData gd_groupReconstruction = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_groupReconstruction.heightHint = 97;

        groupReconstruction.setLayoutData(gd_groupReconstruction);
        groupReconstruction.setLayout(gridLayout5);
        groupReconstruction.setText(MESSAGE_RECONSTRUT);

        scrolledReconstruction = new ScrolledComposite(groupReconstruction, SWT.V_SCROLL | SWT.BORDER);
        scrolledReconstruction.setExpandHorizontal(true);
        final GridData gd_scrolledReconstruction = new GridData(SWT.FILL, SWT.FILL, true, true);
        scrolledReconstruction.setLayoutData(gd_scrolledReconstruction);

        compositeReconstruction = new Composite(scrolledReconstruction, SWT.NONE);
        compositeReconstruction.setLocation(0, 0);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        compositeReconstruction.setLayout(gridLayout);

        /*
         * BEGIN dummy for design only
         */
        // final Label w_1Label = new Label(compositeReconstruction, SWT.NONE);
        // w_1Label.setText("w_1");
        //
        // final Label label = new Label(compositeReconstruction, SWT.NONE);
        // label.setText("=");
        //
        // text_1 = new Text(compositeReconstruction, SWT.BORDER);
        // text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
        // false));
        // compositeReconstruction.setSize(253, 80);
        /*
         * END dummy
         */

        scrolledReconstruction.setContent(compositeReconstruction);
        compositeReconstruction.pack();
    }

    /**
     * Convert a number to a superscript index
     *
     * @param id is the number to be converted
     * @return a string which contains only superscript
     */
    private String convertToSuperscript(int id) {
        char[] data = String.valueOf(id).toCharArray();
        StringBuilder result = new StringBuilder();

        if (id == 0 || id == 1)
            return "";

        for (int i = 0; i < data.length; i++) {
            if (data[i] == '2')
                result.append(sTwo);
            else if (data[i] == '3')
                result.append(sThree);
            else if (data[i] == '4')
                result.append(sFour);
            else if (data[i] == '5')
                result.append(sFive);
            else if (data[i] == '6')
                result.append(sSix);
            else if (data[i] == '7')
                result.append(sSeven);
            else if (data[i] == '8')
                result.append(sEight);
            else if (data[i] == '9')
                result.append(sNine);
        }

        return result.toString();
    }

    /**
     * converts an array containing coefficients to a polynomial string
     *
     * @param coefficients
     * @return a string representation of a polynomial
     */
    private String createPolynomialString(BigInteger[] coefficients) {
        String result = "";

        for (int i = 0; i < coefficients.length; i++) {
            if (i == 0) {
                result = coefficients[i].toString() + " ";
            } else {
                BigInteger bi = coefficients[i];
                if (bi.compareTo(BigInteger.ZERO) != 0) {
                    if (bi.compareTo(BigInteger.ZERO) < 0) {
                        if (bi.compareTo(MINUS_ONE) == 0) {
                            result += "-x" + convertToSuperscript(i) + " ";
                        } else {
                            result += coefficients[i] + "x" + convertToSuperscript(i) + " ";
                        }
                    } else {
                        if (bi.compareTo(BigInteger.ONE) == 0) {
                            result += "+ " + "x" + convertToSuperscript(i) + " ";
                        } else {
                            result += "+ " + coefficients[i] + "x" + convertToSuperscript(i) + " ";
                        }
                    }
                }
            }
        }
        result = result.trim();

        return result;
    }

    /**
     * creates the shares
     *
     * @param n number of share rows
     */
    private void createShares(int n) {
        sharesUseCheckButtonSet = new Button[n];
        for (int i = 0; i < n; i++) {
            Label sharesPLabel = new Label(compositeShares, SWT.NONE);
            sharesPLabel.setText(MESSAGE_SHARE + (i + 1));

            Label sharesEquivalentLabel = new Label(compositeShares, SWT.NONE);
            sharesEquivalentLabel.setText(MESSAGE_EQUAL);

            Label sharesOpenBracetLabel = new Label(compositeShares, SWT.NONE);
            sharesOpenBracetLabel.setText(MESSAGE_LEFT_PARENTHESIS);

            Label sharesXCoordinateLabel = new Label(compositeShares, SWT.NONE);
            sharesXCoordinateLabel.setText(String.valueOf(i + 1));

            Label sharesSeperatorLabel = new Label(compositeShares, SWT.NONE);
            sharesSeperatorLabel.setText(MESSAGE_SEPERATOR);

            shareYCoordinateModText = new Text(compositeShares, SWT.READ_ONLY | SWT.BORDER);
            shareYCoordinateModText.setText(shares[i].getY().mod(modul).toString());
            GridData gd_sharesYCoordinateModText = new GridData(SWT.FILL, SWT.CENTER, true, false);
            gd_sharesYCoordinateModText.widthHint = 70;
            shareYCoordinateModText.setLayoutData(gd_sharesYCoordinateModText);

            Label shareCongruenceLabel = new Label(compositeShares, SWT.NONE);
            shareCongruenceLabel.setText(uCongruence);

            sharesYCoordinateText = new Text(compositeShares, SWT.READ_ONLY | SWT.BORDER);
            sharesYCoordinateText.setText(shares[i].getY().toString());
            GridData gd_sharesYCoordinateText = new GridData(SWT.FILL, SWT.CENTER, true, false);
            sharesYCoordinateText.setLayoutData(gd_sharesYCoordinateText);

            Label sharesCloseBarcetLabel = new Label(compositeShares, SWT.NONE);
            sharesCloseBarcetLabel.setText(MESSAGE_RIGHT_PARENTHESIS);

            Button sharesUseCheckButton = new Button(compositeShares, SWT.CHECK);
            sharesUseCheckButton.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(final SelectionEvent e) {
                    int checkButtonCounter = 0;
                    for (int j = 0; j < sharesUseCheckButtonSet.length; j++) {
                        if (sharesUseCheckButtonSet[j].getSelection()) {
                            checkButtonCounter++;
                        }
                    }

                    if (checkButtonCounter >= 2) {
                        reconstructButton.setEnabled(true);
                    } else {
                        reconstructButton.setEnabled(false);
                    }
                    canvasCurve.redraw();
                }
            });
            sharesUseCheckButtonSet[i] = sharesUseCheckButton;
        }
        compositeShares.pack();
    }

    /**
     * Creates the subpolynomial
     *
     * @param n the number of subpolynomial rows
     */
    private void createReconstruction(int n) {
        for (int i = 0; i < n; i++) {
            Label reconstructionLabel = new Label(compositeReconstruction, SWT.NONE);
            reconstructionLabel.setText(MESSAGE_W + convertToSubset(i));

            Label reconstructEquivalentLabel = new Label(compositeReconstruction, SWT.NONE);
            reconstructEquivalentLabel.setText(MESSAGE_EQUAL);

            Text reconstructPolynomial = new Text(compositeReconstruction, SWT.READ_ONLY | SWT.BORDER);

            String polynomString = createPolynomialString(subpolynomial.get(i));
            if (polynomString.charAt(0) == '0' && polynomString.length() > 1) {
                reconstructPolynomial.setText(polynomString.substring(4));
            } else {
                reconstructPolynomial.setText(polynomString);
            }
            GridData gd_reconstructPolynomial = new GridData(SWT.FILL, SWT.CENTER, true, false);
            reconstructPolynomial.setLayoutData(gd_reconstructPolynomial);
        }
        compositeReconstruction.pack();
    }

    /**
     * reset the control elements
     */
    public void adjustButtons() {
        spnrN.setEnabled(true);
        spnrN.setSelection(2);
        spnrN.setMinimum(2);
        spnrN.setMaximum(500);
        spnrT.setEnabled(true);
        spnrT.setSelection(2);
        spnrT.setMinimum(2);
        spnrT.setMaximum(2);
        numericalButton.setSelection(false);
        graphicalButton.setSelection(true);
        computeSharesButton.setEnabled(false);
        secret = null;
        modul = null;
        secretText.removeVerifyListener(numberOnlyVerifyListenerSecret);
        secretText.setText("");
        secretText.setEnabled(true);
        secretText.addVerifyListener(numberOnlyVerifyListenerSecret);
        modulText.removeVerifyListener(numberOnlyVerifyListenerModul);
        modulText.setText("");
        modulText.setEnabled(true);
        modulText.addVerifyListener(numberOnlyVerifyListenerModul);
        stPolynom.setText("");
        stPolynom.setEnabled(true);
        selectCoefficientButton.setEnabled(true);
        computeSharesButton.setEnabled(false);
        reconstructButton.setEnabled(false);
        selectAllButton.setEnabled(false);
        deselectAllButton.setEnabled(false);

        Control[] tmpWidgets = compositeShares.getChildren();
        for (int i = 0; i < tmpWidgets.length; i++) {
            tmpWidgets[i].dispose();
        }
        compositeShares.pack();

        tmpWidgets = compositeReconstruction.getChildren();
        for (int i = 0; i < tmpWidgets.length; i++) {
            tmpWidgets[i].dispose();
        }
        compositeReconstruction.pack();

        stValue.setText("");
        stInfo.setText("");

        stInfo.setBackground(WHITE);

        reconstructPxLabel.setEnabled(false);
    }

    /**
     * Converts the id value to subscript
     *
     * @param id
     * @return a subscript converted string
     */
    private String convertToSubset(int id) {
        char[] data = String.valueOf(id).toCharArray();
        String result = "";

        for (int i = 0; i < data.length; i++) {
            if (data[i] == '0')
                result += uZero;

            if (data[i] == '1')
                result += uOne;

            if (data[i] == '2')
                result += uTwo;

            if (data[i] == '3')
                result += uThree;

            if (data[i] == '4')
                result += uFour;

            if (data[i] == '5')
                result += uFive;

            if (data[i] == '6')
                result += uSix;

            if (data[i] == '7')
                result += uSeven;

            if (data[i] == '8')
                result += uEight;

            if (data[i] == '9')
                result += uNine;
        }

        return result;
    }

    /**
     * compares two BigInterger array equality
     *
     * @param a a BigInteger array
     * @param b a BigInteger array
     * @return true if the arrays are equal otherwise false
     */
    private boolean comparePolynomial(BigInteger[] a, BigInteger[] b) {
        boolean result = true;
        int n;
        if (a.length < b.length) {
            n = a.length;
        } else {
            n = b.length;
        }

        for (int i = 0; i < n; i++) {
            if (!a[i].mod(modul).equals(b[i].mod(modul))) {
                result = false;
            }
        }
        return result;
    }

    private Point nearSharePoint(Point[] sharePoints, int x, int y) {
        Point point = null;
        for (int i = 0; i < sharePoints.length; i++) {
            int tmpX = sharePoints[i].getX().intValue() * 60 + xAxisGap;
            int tmpY = yAxisGap - sharePoints[i].getY().intValue() * 6;

            if ((tmpX - 3 == x || tmpX - 2 == x || tmpX - 1 == x || tmpX == x || tmpX + 1 == x || tmpX + 2 == x || tmpX + 3 == x)
                    && (tmpY - 3 == y || tmpY - 2 == y || tmpY - 1 == y || tmpY == y || tmpY + 1 == y || tmpY + 2 == y || tmpY + 3 == y)) {
                point = sharePoints[i];
                return point;
            }
        }
        return point;
    }

    private void makePointVisible(boolean visible) {
        shareLabel.setVisible(visible);
        openLabel.setVisible(visible);
        xText.setVisible(visible);
        seperatorLabel.setVisible(visible);
        yText.setVisible(visible);
        closeLabel.setVisible(visible);
    }

}
