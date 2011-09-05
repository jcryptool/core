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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ecc.Messages;
import org.jcryptool.visual.ecc.algorithm.EC;
import org.jcryptool.visual.ecc.algorithm.FpPoint;

public class ECContentReal extends Composite {

    private Button btnBrowse = null;
    private Button btnDeletePoints = null;
    private Button btnKP = null;
    private Button btnPQ = null;
    private Button btnSave = null;
    private Canvas canvasCurve = null;
    private Button cbAutoSave = null;
    private Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
    private Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
    private Color lightBlue = new Color(this.getDisplay(), 0, 255, 255);
    private Color blue = new Color(this.getDisplay(), 0, 0, 255);
    private Color purple = new Color(this.getDisplay(), 255, 0, 255);
    private Color darkPurple = new Color(this.getDisplay(), 148, 3, 148);
    private Color red = new Color(this.getDisplay(), 203, 0, 0);
    private Combo cSaveResults = null;
    private Composite compositeIntro = null;
    private EC curve;
    private Group groupCalculations = null;
    private Group groupCurve = null;
    private Group groupCurveAttributes = null;
    private Group groupCurveType = null;
    private Group groupSettings = null;
    private Group groupSave = null;
    private Label lblCurve = null;
    private Label lblP = null;
    private Label lblQ = null;
    private Label lblR = null;
    private Label lblSaveResults = null;
    private FpPoint pointP;
    private FpPoint pointQ;
    private FpPoint pointR;
    private FpPoint pointSelect;
    private FpPoint[] points;
    private Button rbtnFP = null;
    private Button rbtnFM = null;
    private Button rbtnReal = null;
    private Button rbtnLarge = null;
    private Slider sliderZoom = null;
    private Spinner spnrA = null;
    private Spinner spnrB = null;
    private Spinner spnrK = null;
    private StyledText stDescription = null;
    private ECView view;
    private Composite content;
    private Group groupSize;
    private Button rbtnSmall;

    public ECContentReal(Composite parent, int style, ECView view) {
        super(parent, style);
        this.view = view;

        this.setLayout(new FillLayout());

        ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL
                | SWT.V_SCROLL);
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
        scrolledComposite.setMinSize(content.computeSize(862, 664));

        createGroupAttributesR();

        Display.getCurrent().asyncExec(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                updateCurve(true);
            }
        });
    }

    @Override
	public void dispose() {
    	blue.dispose();
        darkPurple.dispose();
        lightBlue.dispose();
		purple.dispose();
		red .dispose();
		super.dispose();
	}

    /**
     * This method initializes groupCurve1
     *
     */
    private void createGroupCurve() {
        groupCurve = new Group(content, SWT.NONE);
        groupCurve.setLayout(new GridLayout(3, false));
        groupCurve.setText(Messages.getString("ECContentReal.0")); //$NON-NLS-1$
        groupCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        createCanvasCurve();
        lblCurve = new Label(groupCurve, SWT.NONE);
        lblCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        lblCurve.setText(""); //$NON-NLS-1$
        btnDeletePoints = new Button(groupCurve, SWT.NONE);
        btnDeletePoints.setToolTipText(Messages.getString("ECContentReal.3")); //$NON-NLS-1$
        btnDeletePoints.setText(Messages.getString("ECView.RemoveSelection")); //$NON-NLS-1$
        btnDeletePoints.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
        btnDeletePoints.setEnabled(false);
        btnDeletePoints.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                btnPQ.setSelection(true);
                btnPQ.setEnabled(false);
                btnKP.setSelection(false);
                btnKP.setEnabled(false);
                spnrK.setEnabled(false);
                btnDeletePoints.setEnabled(false);
                setPointP(null);
                setPointQ(null);
                setPointR(null);
                updateCurve(false);
            }
        });
        Label label = new Label(groupCurve, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        label.setText(Messages.getString("ECView.ZoomGraph")); //$NON-NLS-1$
        sliderZoom = new Slider(groupCurve, SWT.NONE);
        sliderZoom.setSelection(10);
        sliderZoom.setMaximum(57);
        sliderZoom.setMinimum(0);
        sliderZoom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        sliderZoom.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                curve.updateCurve(spnrA.getSelection(), spnrB.getSelection(), 50 - sliderZoom
                        .getSelection(), canvasCurve.getSize());
                points = curve.getPoints();
                updateCurve(false);
            }
        });
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
        rbtnSmall.setSelection(true);
        rbtnLarge = new Button(groupSize, SWT.RADIO);
        rbtnLarge.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        rbtnLarge.setText(Messages.getString("ECView.Large")); //$NON-NLS-1$
        rbtnLarge.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                view.showLarge();
            }
        });
    }

    /**
     * This method initializes groupSettings
     *
     */
    private void createGroupSettings() {
        groupSettings = new Group(content, SWT.NONE);
        groupSettings.setText(Messages.getString("ECContentReal.9")); //$NON-NLS-1$
        groupSettings.setLayout(new GridLayout(1, false));
        groupSettings.setLayout(new GridLayout(1, false));
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
        gridData.widthHint = 300;
        groupSettings.setLayoutData(gridData);
        createGroupSize();
        createGroupCurveType();
        createGroupCurveAttributes();
        createGroupCalculations();
        createGroupSave();
    }

    /**
     * This method initializes canvasCurve
     *
     */
    private void createCanvasCurve() {
        canvasCurve = new Canvas(groupCurve, SWT.DOUBLE_BUFFERED);
        canvasCurve.setBackground(white);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        // gridData.heightHint = 500;
        // gridData.widthHint = 500;
        canvasCurve.setLayoutData(gridData);
        // canvasCurve.setSize(500,500);
        canvasCurve.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                drawGraph(e);
            }
        });
        canvasCurve.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent e) {
                Point size = canvasCurve.getSize();
                if (points != null) {
                    int gridSize = 50 - sliderZoom.getSelection();
                    double step = Math.pow((double) gridSize, -1);
                    double x = ((e.x - size.x / 2) * (step * 100));
                    double y = -((e.y - size.y / 2) * (step * 100));
                    FpPoint nearestPoint = findNearestPoint(x, y);
                    if (nearestPoint != null)
                        setPointSelect(nearestPoint);
                }
            }
        });
        canvasCurve.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
                if (pointSelect != null) {
                    if (pointP == null) {
                        setPointP(pointSelect);
                    } else if (btnPQ.getSelection()) {
                        setPointQ(pointSelect);
                    }
                }
            }
        });
        canvasCurve.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent e) {
            }

            public void mouseExit(MouseEvent e) {
                pointSelect = null;
                updateCurve(false);
                if (pointP == null)
                    lblP.setText(""); //$NON-NLS-1$
                if (pointQ == null)
                    lblQ.setText(""); //$NON-NLS-1$
            }

            public void mouseHover(MouseEvent e) {
            }
        });
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

    /**
     * This method initializes groupCurveType1
     *
     */
    private void createGroupCurveType() {
        groupCurveType = new Group(groupSettings, SWT.NONE);
        groupCurveType.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        groupCurveType.setLayout(new GridLayout(3, true));
        groupCurveType.setText(Messages.getString("ECView.SelectCurveType")); //$NON-NLS-1$
        rbtnReal = new Button(groupCurveType, SWT.RADIO);
        rbtnReal.setText(Messages.getString("ECView.RealNumbers")); //$NON-NLS-1$
        rbtnReal.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        rbtnReal.setSelection(true);
        rbtnReal.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.showReal();
            }

        });
        rbtnFP = new Button(groupCurveType, SWT.RADIO);
        rbtnFP.setText("F(p)"); //$NON-NLS-1$
        rbtnFP.setSelection(false);
        rbtnFP.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        rbtnFP.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.showFp();
            }
        });
        rbtnFM = new Button(groupCurveType, SWT.RADIO);
        rbtnFM.setText("F(2^m)"); //$NON-NLS-1$
        rbtnFM.setSelection(false);
        rbtnFM.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
        rbtnFM.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                view.showFm();
            }
        });
    }

    /**
     * This method initializes groupCurveAttributes
     *
     */
    private void createGroupCurveAttributes() {
        groupCurveAttributes = new Group(groupSettings, SWT.NONE);
        groupCurveAttributes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        groupCurveAttributes.setLayout(new GridLayout(2, false));
        groupCurveAttributes.setText(Messages.getString("ECView.SelectCurveAttributes")); //$NON-NLS-1$
    }

    private void createGroupAttributesR() {
        Control[] c = groupCurveAttributes.getChildren();
        for (int i = 0; i < c.length; i++)
            c[i].dispose();
        sliderZoom.setEnabled(true);

        GridData gridData16 = new GridData();
        gridData16.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        Label label = new Label(groupCurveAttributes, SWT.NONE);
        label.setText("a ="); //$NON-NLS-1$
        spnrA = new Spinner(groupCurveAttributes, SWT.BORDER);
        spnrA.setMaximum(100000);
        spnrA.setSelection(-10);
        spnrA.setMinimum(-100000);
        spnrA.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                btnPQ.setSelection(true);
                btnKP.setSelection(false);
                btnPQ.setEnabled(false);
                btnKP.setEnabled(false);
                spnrK.setEnabled(false);
                spnrK.setSelection(1);
                setPointP(null);
                setPointQ(null);
                setPointR(null);
                updateCurve(true);
            }

        });
        Label label1 = new Label(groupCurveAttributes, SWT.NONE);
        label1.setText("b ="); //$NON-NLS-1$
        spnrB = new Spinner(groupCurveAttributes, SWT.BORDER);
        spnrB.setMaximum(100000);
        spnrB.setSelection(15);
        spnrB.setMinimum(-100000);
        spnrB.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                btnPQ.setSelection(true);
                btnKP.setSelection(false);
                btnPQ.setEnabled(false);
                btnKP.setEnabled(false);
                spnrK.setEnabled(false);
                spnrK.setSelection(1);
                setPointP(null);
                setPointQ(null);
                setPointR(null);
                updateCurve(true);
            }

        });
        groupSettings.layout();
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
        stDescription.setText(Messages.getString("ECView.Description")); //$NON-NLS-1$
        stDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    /**
     * This method initializes groupCalculations
     *
     */
    private void createGroupCalculations() {
        groupCalculations = new Group(groupSettings, SWT.NONE);
        groupCalculations.setText(Messages.getString("ECContentReal.35")); //$NON-NLS-1$
        groupCalculations.setLayout(new GridLayout(3, false));
        groupCalculations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label label = new Label(groupCalculations, SWT.WRAP);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        label.setText(Messages.getString("ECContentReal.36")); //$NON-NLS-1$

        btnPQ = new Button(groupCalculations, SWT.RADIO);
        btnPQ.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        btnPQ.setText(Messages.getString("ECContentReal.37")); //$NON-NLS-1$
        btnPQ.setSelection(true);
        btnPQ.setEnabled(false);
        btnPQ.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                setPointQ(null);
            }
        });

        btnKP = new Button(groupCalculations, SWT.RADIO);
        btnKP.setText(Messages.getString("ECContentReal.38")); //$NON-NLS-1$
        btnKP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        btnKP.setEnabled(false);
        btnKP.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                spnrK.setEnabled(btnKP.getSelection());
                FpPoint q = curve.multiplyPoint(pointP, spnrK.getSelection());
                setPointQ(q);
                updateCurve(false);
            }
        });
        spnrK = new Spinner(groupCalculations, SWT.BORDER);
        spnrK.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
        spnrK.setMinimum(1);
        spnrK.setEnabled(false);
        spnrK.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                FpPoint q = curve.multiplyPoint(pointP, spnrK.getSelection());
                setPointQ(q);
                updateCurve(false);
            }
        });

        label = new Label(groupCalculations, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
        label.setText(Messages.getString("ECContentReal.39")); //$NON-NLS-1$

        label = new Label(groupCalculations, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label.setText("P ="); //$NON-NLS-1$
        lblP = new Label(groupCalculations, SWT.NONE);
        lblP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        lblP.setText(""); //$NON-NLS-1$

        label = new Label(groupCalculations, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label.setText("Q ="); //$NON-NLS-1$
        lblQ = new Label(groupCalculations, SWT.NONE);
        lblQ.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        lblQ.setText(""); //$NON-NLS-1$

        label = new Label(groupCalculations, SWT.NONE);
        label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        label.setText("R = P + Q ="); //$NON-NLS-1$
        lblR = new Label(groupCalculations, SWT.NONE);
        lblR.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        lblR.setText(""); //$NON-NLS-1$
    }

    public void setPointSelect(FpPoint p) {
        pointSelect = p;
        if (pointP == null)
            lblP
                    .setText("(" + ((double) pointSelect.x / 100) + "|" + ((double) pointSelect.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if (pointP != null && pointQ == null)
            lblQ
                    .setText("(" + ((double) pointSelect.x / 100) + "|" + ((double) pointSelect.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        updateCurve(false);
    }

    public void setPointP(FpPoint p) {
        if (p == null) {
            pointP = null;
            setPointQ(null);
            lblP.setText(""); //$NON-NLS-1$
            btnDeletePoints.setEnabled(false);
            btnKP.setEnabled(false);
            btnPQ.setEnabled(false);
            btnPQ.setSelection(true);
        } else {
            btnKP.setEnabled(true);
            btnPQ.setEnabled(true);
            btnKP.setEnabled(true);
            btnPQ.setEnabled(true);
            btnPQ.setSelection(true);
            btnDeletePoints.setEnabled(true);
            pointP = p;
            view.log("\n" + Messages.getString("ECView.Curve") + ": " + lblCurve.getText()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            view
                    .log(Messages.getString("ECView.Point") + " P = " + "(" + ((double) pointP.x / 100) + "|" + ((double) pointP.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            lblP.setText("(" + ((double) pointP.x / 100) + "|" + ((double) pointP.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        updateCurve(false);
    }

    public void setPointQ(FpPoint q) {
        if (q == null) {
            pointQ = null;
            lblQ.setText(""); //$NON-NLS-1$
            setPointR(null);
        } else {
            pointQ = q;
            lblQ.setText("(" + ((double) pointQ.x / 100) + "|" + ((double) pointQ.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

            btnDeletePoints.setEnabled(true);

            view
                    .log(Messages.getString("ECView.Point") + " Q = " + "(" + ((double) pointQ.x / 100) + "|" + ((double) pointQ.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            setPointR(curve.addPoints(pointP, pointQ));
        }
        updateCurve(false);
    }

    public void setPointR(FpPoint r) {
        if (r == null) {
            pointR = null;
            lblR.setText(""); //$NON-NLS-1$
        } else {
            pointR = r;
            lblR.setText("(" + ((double) pointR.x / 100) + "|" + ((double) pointR.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            view
                    .log(Messages.getString("ECView.Point") + " R = P + Q = " + "(" + ((double) pointR.x / 100) + "|" + ((double) pointR.y / 100) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }
    }

    /**
     * Sets the label beneath the canvas
     *
     */
    public void setCurveLabel() {
        String s;
        if (points == null) {
            s = Messages.getString("ECView.NoCurve"); //$NON-NLS-1$
        } else
            s = curve.toString();
        lblCurve.setText(s);
    }

    /**
     * This method controls all the parameters that need to be updated
     */
    public void updateCurve(boolean full) {
        if (full) {
            pointSelect = null;
            pointP = null;
            pointQ = null;
            pointR = null;
            setPointP(null);

            if (curve == null)
                curve = new EC();

            curve.updateCurve(spnrA.getSelection(), spnrB.getSelection(), 50 - sliderZoom
                    .getSelection(), canvasCurve.getSize());

            points = curve.getPoints();
            setCurveLabel();
        }
        canvasCurve.redraw();
    }

    private void drawGraph(PaintEvent e) {
        GC gc = e.gc;
        Point size = canvasCurve.getSize();
        int gridSize = 50 - sliderZoom.getSelection();
        Color black = new Color(canvasCurve.getDisplay(), 0, 0, 0);
        Color grey = new Color(canvasCurve.getDisplay(), 235, 235, 235);

        // first, draw the grid
        gc.setForeground(grey);
        for (int i = 0; i < size.x / 2; i += gridSize) {
            gc.drawLine(size.x / 2 - i, 0, size.x / 2 - i, size.y);
            gc.drawLine(size.x / 2 + i, 0, size.x / 2 + i, size.y);
        }
        for (int i = 0; i < size.y / 2; i += gridSize) {
            gc.drawLine(0, size.y / 2 - i, size.x, size.y / 2 - i);
            gc.drawLine(0, size.y / 2 + i, size.x, size.y / 2 + i);
        }

        // Draw the axis
        gc.setForeground(black); // Black
        gc.drawLine(size.x / 2, 0, size.x / 2, size.y);
        gc.drawLine(0, size.y / 2, size.x, size.y / 2);
        int labeljumps = 5;
        int scale = (size.x / 2 / gridSize) - ((size.x / 2 / gridSize) % labeljumps);
        if (scale > 50)
            labeljumps = 10;
        for (int i = 0; i < size.x / 2; i += gridSize) {
            if ((i / gridSize) % labeljumps == 0) {
                gc.drawLine(size.x / 2 + i, size.y / 2 - 8, size.x / 2 + i, size.y / 2 + 8);
                gc.drawLine(size.x / 2 - i, size.y / 2 - 8, size.x / 2 - i, size.y / 2 + 8);
                gc.drawLine(size.x / 2 - 8, size.y / 2 + i, size.x / 2 + 8, size.y / 2 + i);
                gc.drawLine(size.x / 2 - 8, size.y / 2 - i, size.x / 2 + 8, size.y / 2 - i);

                int label = i / gridSize;
                if (label < 10) {
                    if (label != 0) {
                        gc.drawText(label + "", size.x / 2 + i - 2, size.y / 2 + 10, true); //$NON-NLS-1$
                        gc.drawText(-label + "", size.x / 2 - i - 5, size.y / 2 + 10, true); //$NON-NLS-1$

                        gc.drawText(label + "", size.x / 2 + 13, size.y / 2 - i - 7, true); //$NON-NLS-1$
                        gc.drawText(-label + "", size.x / 2 + 13, size.y / 2 + i - 7, true); //$NON-NLS-1$
                    } else {
                        gc.drawText(label + "", size.x / 2 + i + 13, size.y / 2 + 10, true); //$NON-NLS-1$
                    }
                } else {
                    gc.drawText(label + "", size.x / 2 + i - 6, size.y / 2 + 10, true); //$NON-NLS-1$
                    gc.drawText(-label + "", size.x / 2 - i - 10, size.y / 2 + 10, true); //$NON-NLS-1$

                    gc.drawText(label + "", size.x / 2 + 13, size.y / 2 - i - 7, true); //$NON-NLS-1$
                    gc.drawText(-label + "", size.x / 2 + 13, size.y / 2 + i - 7, true); //$NON-NLS-1$
                }

            } else {
                gc.drawLine(size.x / 2 + i, size.y / 2 - 2, size.x / 2 + i, size.y / 2 + 2);
                gc.drawLine(size.x / 2 - i, size.y / 2 - 2, size.x / 2 - i, size.y / 2 + 2);
                gc.drawLine(size.x / 2 - 2, size.y / 2 + i, size.x / 2 + 2, size.y / 2 + i);
                gc.drawLine(size.x / 2 - 2, size.y / 2 - i, size.x / 2 + 2, size.y / 2 - i);
            }
        }

        if (points != null) {
            gc.setForeground(blue);
            double step = Math.pow((double) gridSize, -1);
            double x1, y1, x2, y2;
            for (int i = 2; i < points.length; i++) {
                if (points[i - 2].y == 0) {
                    if (points[i - 1] != null && points[i - 1].y != 0) {
                        x1 = (double) points[i - 2].x / 100 / step + size.x / 2;
                        y1 = -(double) points[i - 2].y / 100 / step + size.y / 2;
                        x2 = (double) points[i - 1].x / 100 / step + size.x / 2;
                        y2 = -(double) points[i - 1].y / 100 / step + size.y / 2;
                        gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                        gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                        x1 = (double) points[i - 2].x / 100 / step + size.x / 2;
                        y1 = -(double) points[i - 2].y / 100 / step + size.y / 2;
                        x2 = (double) points[i].x / 100 / step + size.x / 2;
                        y2 = -(double) points[i].y / 100 / step + size.y / 2;
                        gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                        gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                    } else {
                        x1 = (double) points[i - 2].x / 100 / step + size.x / 2;
                        y1 = -(double) points[i - 2].y / 100 / step + size.y / 2;
                        x2 = (double) points[i - 3].x / 100 / step + size.x / 2;
                        y2 = -(double) points[i - 3].y / 100 / step + size.y / 2;
                        gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                        gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                        x1 = (double) points[i - 2].x / 100 / step + size.x / 2;
                        y1 = -(double) points[i - 2].y / 100 / step + size.y / 2;
                        x2 = (double) points[i - 4].x / 100 / step + size.x / 2;
                        y2 = -(double) points[i - 4].y / 100 / step + size.y / 2;
                        gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                        gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                    }
                } else {
                    x1 = (double) points[i - 2].x / 100 / step + size.x / 2;
                    y1 = -(double) points[i - 2].y / 100 / step + size.y / 2;
                    x2 = (double) points[i].x / 100 / step + size.x / 2;
                    y2 = -(double) points[i].y / 100 / step + size.y / 2;
                    if ((int) Math.signum(points[i - 2].y + 0.0) == (int) Math
                            .signum(points[i].y + 0.0)) {
                        gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                        gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                    }
                }
                if (points[points.length - 1].y == 0) {
                    x1 = (double) points[points.length - 1].x / 100 / step + size.x / 2;
                    y1 = -(double) points[points.length - 1].y / 100 / step + size.y / 2;
                    x2 = (double) points[points.length - 2].x / 100 / step + size.x / 2;
                    y2 = -(double) points[points.length - 2].y / 100 / step + size.y / 2;
                    gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                    gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                    x1 = (double) points[points.length - 1].x / 100 / step + size.x / 2;
                    y1 = -(double) points[points.length - 1].y / 100 / step + size.y / 2;
                    x2 = (double) points[points.length - 3].x / 100 / step + size.x / 2;
                    y2 = -(double) points[points.length - 3].y / 100 / step + size.y / 2;
                    gc.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                    gc.drawLine((int) x1, (int) y1 + 1, (int) x2, (int) y2 + 1);
                }
            }

            if (pointR != null && !pointR.isInfinite()) {
                int rX;
                int rY;
                int lX;
                int lY;
                FpPoint p = pointP;
                FpPoint q = pointQ;
                FpPoint r = pointR;
                if (q != null) {
                    if (p.x < q.x && p.x < r.x) { // if pointP is the most left point
                        lX = (int) ((double) p.x / 100 / step + size.x / 2);
                        lY = (int) ((double) -p.y / 100 / step + size.y / 2);
                    } else if (q.x < r.x) { // if pointQ is the most left point
                        lX = (int) ((double) q.x / 100 / step + size.x / 2);
                        lY = (int) ((double) -q.y / 100 / step + size.y / 2);
                    } else { // if pointR is the most left point
                        lX = (int) ((double) r.x / 100 / step + size.x / 2);
                        lY = (int) ((double) r.y / 100 / step + size.y / 2);
                    }

                    if (p.x > q.x && p.x > r.x) { // if pointP is the most right point
                        rX = (int) ((double) p.x / 100 / step + size.x / 2);
                        rY = (int) ((double) -p.y / 100 / step + size.y / 2);
                    } else if (q.x > r.x) { // if pointQ is the most right point
                        rX = (int) ((double) q.x / 100 / step + size.x / 2);
                        rY = (int) ((double) -q.y / 100 / step + size.y / 2);
                    } else { // if pointR is the most left point
                        rX = (int) ((double) r.x / 100 / step + size.x / 2);
                        rY = (int) ((double) r.y / 100 / step + size.y / 2);
                    }
                } else {
                    if (p.x < r.x) { // if pointP is the most left point
                        lX = (int) ((double) p.x / 100 / step + size.x / 2);
                        lY = (int) ((double) -p.y / 100 / step + size.y / 2);
                        rX = (int) ((double) r.x / 100 / step + size.x / 2);
                        rY = (int) ((double) r.y / 100 / step + size.y / 2);
                    } else { // if pointR is the most left point
                        lX = (int) ((double) r.x / 100 / step + size.x / 2);
                        lY = (int) ((double) r.y / 100 / step + size.y / 2);
                        rX = (int) ((double) p.x / 100 / step + size.x / 2);
                        rY = (int) ((double) -p.y / 100 / step + size.y / 2);
                    }
                }

                int startX;
                int endX;
                int startY;
                int endY;

                if ((rX > lX ? rX - lX : lX - rX) > (rY > lY ? rY - lY : lY - rY)) {
                    double alfa = (double) (rY - lY) / (double) (rX - lX);
                    startX = lX - 30;
                    endX = rX + 30;
                    startY = lY - (int) (alfa * 30);
                    endY = rY + (int) (alfa * 30);
                } else {
                    double alfa = (double) (rX - lX) / (double) (rY - lY);
                    startX = lX + (lY > rY ? (int) (alfa * 30) : -(int) (alfa * 30));
                    endX = rX + (lY > rY ? -(int) (alfa * 30) : (int) (alfa * 30));
                    ;
                    startY = lY + (lY > rY ? 30 : -30);
                    endY = rY + (lY > rY ? -30 : 30);
                }

                if (startY < 0) {
                    double alfa = (double) (rX - lX) / (double) (rY - lY);
                    startX += (double) (-startY) * alfa;
                    startY = 0;
                } else if (startY > size.y) {
                    double alfa = (double) (rX - lX) / (double) (rY - lY);
                    startX += -(double) (startY - size.y) * alfa;
                    startY = size.y;
                }

                if (endY < 0) {
                    double alfa = (double) (rX - lX) / (double) (rY - lY);
                    endX += (double) (-endY - 1) * alfa;
                    endY = -1;
                } else if (endY > size.y) {
                    double alfa = (double) (rX - lX) / (double) (rY - lY);
                    endX -= (double) (endY - size.y) * alfa + 0.5;
                    endY = size.y;
                }

                gc.setForeground(darkPurple);
                gc.setLineWidth(2);
                gc.drawLine(startX, startY, endX, endY);

                startX = (int) ((double) pointR.x / 100 / step + size.x / 2);
                endX = startX;
                if (pointR.y > 0) {
                    startY = (int) ((double) pointR.y / 100 / step + size.y / 2) + 30;
                    endY = (int) ((double) -pointR.y / 100 / step + size.y / 2) - 30;
                } else {
                    startY = (int) ((double) pointR.y / 100 / step + size.y / 2) - 30;
                    endY = (int) ((double) -pointR.y / 100 / step + size.y / 2) + 30;
                }
                gc.setForeground(purple);
                gc.drawLine(startX, startY, endX, endY);
                gc.setLineWidth(1);

                double x = (double) pointR.x / 100 / step + size.x / 2;
                double y = (double) pointR.y / 100 / step + size.y / 2;
                gc.setForeground(black);
                gc.setBackground(darkPurple);
                gc.fillOval((int) x - 3, (int) y - 3, 7, 7);
                gc.drawOval((int) x - 3, (int) y - 3, 6, 6);
                gc.setForeground(darkPurple);
                gc.setBackground(white);
                if (y < size.x / 2) {
                    gc.drawText("-R", (int) x + 8, (int) y + 4, true); //$NON-NLS-1$
                } else {
                    gc.drawText("-R", (int) x + 8, (int) y - 10, true); //$NON-NLS-1$
                }
            } else if (pointP != null) {
                gc.setForeground(darkPurple);
                gc.setLineWidth(2);
                int x = (int) ((double) pointP.x / 100 / step + size.x / 2);
                gc.drawLine(x, 0, x, size.y);
                gc.setLineWidth(1);
            }

            if (pointSelect != null) {
                double x = (double) pointSelect.x / 100 / step + size.x / 2;
                double y = -(double) pointSelect.y / 100 / step + size.y / 2;
                String caption = "Q"; //$NON-NLS-1$
                if (pointP == null)
                    caption = "P"; //$NON-NLS-1$
                markPoint(x, y, caption, black, lightBlue, gc);
            }

            if (pointP != null) {
                double x = (double) pointP.x / 100 / step + size.x / 2;
                double y = -(double) pointP.y / 100 / step + size.y / 2;
                String caption = "P"; //$NON-NLS-1$
                if (pointP.equals(pointQ))
                    caption = "P=Q"; //$NON-NLS-1$
                if (pointP.equals(pointR))
                    caption = "P=R"; //$NON-NLS-1$

                markPoint(x, y, caption, red, red, gc);
            }

            if (pointQ != null && !pointQ.equals(pointP)) {
                double x = (double) pointQ.x / 100 / step + size.x / 2;
                double y = -(double) pointQ.y / 100 / step + size.y / 2;
                if (!pointQ.equals(pointP))
                    markPoint(x, y, "Q", red, red, gc); //$NON-NLS-1$
            }

            if (pointR != null && !pointR.isInfinite()) {
                double x = (double) pointR.x / 100 / step + size.x / 2;
                double y = -(double) pointR.y / 100 / step + size.y / 2;
                markPoint(x, y, "R", purple, purple, gc); //$NON-NLS-1$
            }
        }
    }

    private void markPoint(double x, double y, String label, Color fg, Color bg, GC gc) {
        gc.setForeground(black);
        gc.setBackground(bg);
        gc.fillOval((int) x - 3, (int) y - 3, 7, 7);
        gc.drawOval((int) x - 3, (int) y - 3, 6, 6);
        gc.setForeground(fg);
        gc.setBackground(white);
        if (y > 0) {
            gc.drawText(label, (int) x + 8, (int) y + 4, true);
        } else {
            gc.drawText(label, (int) x + 10, (int) y - 10, true);
        }
    }

    public int getLabelPositionX(int x, double grid, int gridSize) {
        return (int) (grid * x) + 25 + (x < gridSize / 2 ? 3 : -8);
    }

    public int getLabelPositionY(int y, Point size, double grid, int gridSize) {
        return (int) (size.y - 25 - grid * y + (y < gridSize / 2 ? -15 : 3));
    }

    private FpPoint findNearestPoint(Double x, Double y) {
        double minimum = Double.MAX_VALUE;
        FpPoint nearestPoint = null;
        for (int i = 0; i < points.length; i++) {
            if (points[i] != null) {
                double currentDistance = Math.sqrt((points[i].x - x) * (points[i].x - x)
                        + (points[i].y - y) * (points[i].y - y));
                if (currentDistance < minimum) {
                    minimum = currentDistance;
                    nearestPoint = points[i];
                }
            }
        }
        return nearestPoint;
    }

    public void adjustButtons() {
        rbtnReal.setSelection(true);
        rbtnFP.setSelection(false);
        rbtnFM.setSelection(false);
        rbtnLarge.setSelection(false);
        rbtnSmall.setSelection(true);

        cSaveResults.select(view.saveTo);
        btnBrowse.setEnabled(view.saveTo == 2);
        btnSave.setEnabled(view.saveTo != 0);
        cbAutoSave.setEnabled(view.saveTo != 0);
        lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
    }
}
