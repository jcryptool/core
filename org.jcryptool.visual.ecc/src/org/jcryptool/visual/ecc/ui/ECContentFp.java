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
import org.eclipse.swt.custom.TableCursor;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ecc.Messages;
import org.jcryptool.visual.ecc.algorithm.EC;
import org.jcryptool.visual.ecc.algorithm.ECFp;
import org.jcryptool.visual.ecc.algorithm.FpPoint;

public class ECContentFp extends Composite{

	private boolean bTable = false;
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
	private Color purple = new Color(this.getDisplay(), 255, 0, 255);
	private Color grey = new Color(this.getDisplay(), 235, 235, 235);
	private Color red = new Color(this.getDisplay(), 203, 0, 0);
	private Combo cSaveResults = null;
	private Composite compositeIntro = null;
	private EC curve;
	private Group groupCalculations = null;
	private Group groupCurve = null;
	private Group groupCurveAttributes = null;
	private Group groupCurveType = null;
	private Group groupPoints = null;
	private Group groupSettings = null;
	private Group groupSave = null;
	private int numItems;
	private int lastPrime;
	private int prime[] = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997};
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
	private Spinner spnrP = null;
	private StyledText stDescription = null;
	private Table tablePoints = null;
	private TableCursor tcPoints;
	private TableItem[] tiPoints;
	private ECView view;
	private Composite content;
	private Group groupSize;
	private Button rbtnSmall;


	public ECContentFp(Composite parent, int style, ECView view) {
		super(parent, style);
		this.view = view;

		this.setLayout(new FillLayout());

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

		createGroupAttributesFp();
		createGroupPoints();
	}

	@Override
	public void dispose() {
		lightBlue.dispose();
		purple.dispose();
		grey.dispose();
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
		groupCurve.setText(Messages.getString("ECContentFp.0")); //$NON-NLS-1$
		groupCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		createCanvasCurve();
		lblCurve = new Label(groupCurve, SWT.NONE);
		lblCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		lblCurve.setText(""); //$NON-NLS-1$
		btnDeletePoints = new Button(groupCurve, SWT.NONE);
		btnDeletePoints.setToolTipText(Messages.getString("ECContentFp.3")); //$NON-NLS-1$
		btnDeletePoints.setText(Messages.getString("ECView.RemoveSelection")); //$NON-NLS-1$
		btnDeletePoints.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
		btnDeletePoints.setEnabled(false);
		btnDeletePoints.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
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
		sliderZoom.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				curve.updateCurve(spnrA.getSelection(), spnrB.getSelection(), 50 - sliderZoom.getSelection(), canvasCurve.getSize());
				points = curve.getPoints();
				updateCurve(false);
			}
		});
	}

	/**
	 * This method initializes groupSettings
	 *
	 */
	private void createGroupSettings() {
		groupSettings = new Group(content, SWT.NONE);
		groupSettings.setText(Messages.getString("ECContentFp.6")); //$NON-NLS-1$
		groupSettings.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2);
		gridData.widthHint = 300;
		groupSettings.setLayoutData(gridData);
		createGroupSize();
		createGroupCurveType();
		createGroupCurveAttributes();
		createGroupCalculations();
		createGroupSave();
	}

	/**
	 * This method initializes groupPoints
	 *
	 */
	private void createGroupPoints() {
		groupPoints = new Group(content, SWT.NONE);
		groupPoints.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		groupPoints.setLayout(new GridLayout(1, false));
		groupPoints.setText(Messages.getString("ECView.Points")); //$NON-NLS-1$
		groupPoints.addListener(SWT.Resize, new Listener(){
			public void handleEvent(Event event) {
				fillTablePoints();
			}
		});tablePoints = new Table(groupPoints, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.DOUBLE_BUFFERED);
		tablePoints.setHeaderVisible(false);
		tablePoints.setLinesVisible(false);
		tablePoints.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		tcPoints = new TableCursor(tablePoints, SWT.NONE);
		tcPoints.setForeground(red);
		tcPoints.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				int index = -1;
				for(int i = 0; i < tiPoints.length; i++) {
					if(tiPoints[i].equals(tcPoints.getRow())) {
						index = i + tcPoints.getColumn() * numItems - 1;
						if(pointP == null)
							setPointP(index == -1 ? new FpPoint() : points[index]);
						else
							setPointQ(index == -1 ? new FpPoint() : points[index]);
						break;
					}
				}
			}
		});

		int width = (canvasCurve.getSize().x - tablePoints.getVerticalBar().getSize().x) / 4;
		for(int i = 0; i < 4; i++) {
			TableColumn tc = new TableColumn(tablePoints, SWT.NONE);
			tc.setWidth(width);
		}
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
			public void widgetDefaultSelected(SelectionEvent e) { }
			public void widgetSelected(SelectionEvent e) {
				view.showLarge();
			}
		});
	}

	/**
	 * This method initializes canvasCurve
	 *
	 */
	private void createCanvasCurve() {
		canvasCurve = new Canvas(groupCurve, SWT.DOUBLE_BUFFERED);
		canvasCurve.setBackground(white);
		GridData gridData = new GridData(SWT.CENTER, SWT.FILL, false, false, 3, 1);
		gridData.heightHint = 500;
		gridData.widthHint = 500;
		canvasCurve.setLayoutData(gridData);
		canvasCurve.setSize(500,500);
		canvasCurve.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {drawDiscrete(e);}
		});
		canvasCurve.addMouseMoveListener(new MouseMoveListener(){
			public void mouseMove(MouseEvent e) {
				Point size = canvasCurve.getSize();
				if (points != null){
					double grid = (double)(size.x - 30) / (lastPrime - 1);
					double x = (e.x - 25) / grid;
					double y = (size.y - e.y - 25) / grid;

					FpPoint p = findNearestPoint(x, y);
					if(p != null && !p.equals(pointSelect)) {
						setPointSelect(p);
					}
				}
			}
		});
		canvasCurve.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event event) {
				if(pointSelect != null) {
					if(pointP == null) {
						setPointP(pointSelect);
					} else if(btnPQ.getSelection()) {
						setPointQ(pointSelect);
					}
				}
			}
		});
		canvasCurve.addMouseTrackListener(new MouseTrackListener(){
			public void mouseEnter(MouseEvent e) {}
			public void mouseExit(MouseEvent e) {
				pointSelect = null;
				updateCurve(false);
				fillTablePoints();
				if(pointP == null)
					lblP.setText(""); //$NON-NLS-1$
				if(pointQ == null)
					lblQ.setText(""); //$NON-NLS-1$
			}
			public void mouseHover(MouseEvent e) {}
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
		rbtnReal.setSelection(false);
		rbtnReal.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				view.showReal();
			}

		});
		rbtnFP = new Button(groupCurveType, SWT.RADIO);
		rbtnFP.setText("F(p)"); //$NON-NLS-1$
		rbtnFP.setSelection(true);
		rbtnFP.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFP.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				view.showFp();
			}
		});
		rbtnFM = new Button(groupCurveType, SWT.RADIO);
		rbtnFM.setText("F(2^m)"); //$NON-NLS-1$
		rbtnFM.setSelection(false);
		rbtnFM.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFM.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
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

	private void createGroupAttributesFp() {
		Control[] c = groupCurveAttributes.getChildren();
		for(int i = 0; i < c.length; i++)
			c[i].dispose();
		sliderZoom.setEnabled(false);

		Label label = new Label(groupCurveAttributes, SWT.NONE);
		label.setText("a ="); //$NON-NLS-1$
		spnrA = new Spinner(groupCurveAttributes, SWT.BORDER);
		spnrA.setMaximum(23);
		spnrA.setSelection(10);
		spnrA.setMinimum(0);
		spnrA.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
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
		spnrB.setMaximum(23);
		spnrB.setSelection(15);
		spnrB.setMinimum(0);
		spnrB.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
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
		Label label2 = new Label(groupCurveAttributes, SWT.NONE);
		label2.setText("p ="); //$NON-NLS-1$
		spnrP = new Spinner(groupCurveAttributes, SWT.BORDER);
		spnrP.setSelection(23);
		spnrP.setMinimum(3);
		spnrP.setMaximum(1000);
		spnrP.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
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

				boolean up = spnrP.getSelection() > lastPrime;
				for(int i = 0; i < prime.length; i++) {
					if(prime[i] > spnrP.getSelection()) {
						spnrP.setSelection(up ? prime[i] : prime[i - 1]);
						lastPrime = spnrP.getSelection();
						break;
					}
				}
				if(spnrA.getSelection() >= lastPrime)
					spnrA.setSelection(spnrA.getSelection() % lastPrime);
				spnrA.setMaximum(lastPrime - 1);
				if(spnrB.getSelection() >= lastPrime)
					spnrB.setSelection(spnrB.getSelection() % lastPrime);
				spnrB.setMaximum(lastPrime - 1);
				updateCurve(true);
			}

		});
		lastPrime = spnrP.getSelection();
		groupCurveAttributes.pack(true);

		Point p = groupSettings.getSize();
		groupSettings.pack();
		groupSettings.setSize(p);
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
		groupCalculations.setText(Messages.getString("ECContentFp.37")); //$NON-NLS-1$
		groupCalculations.setLayout(new GridLayout(3, false));
		groupCalculations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label label = new Label(groupCalculations, SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		label.setText(Messages.getString("ECContentFp.1")); //$NON-NLS-1$

		btnPQ = new Button(groupCalculations, SWT.RADIO);
		btnPQ.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		btnPQ.setText(Messages.getString("ECContentFp.39")); //$NON-NLS-1$
		btnPQ.setSelection(true);
		btnPQ.setEnabled(false);
		btnPQ.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				setPointQ(null);
			}

		});

		btnKP = new Button(groupCalculations, SWT.RADIO);
		btnKP.setText(Messages.getString("ECContentFp.40")); //$NON-NLS-1$
		btnKP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		btnKP.setEnabled(false);
		btnKP.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				spnrK.setEnabled(btnKP.getSelection());
				FpPoint q = curve.multiplyPoint(pointP, spnrK.getSelection());
				setPointQ(q);
				//fillTableElements();
				fillTablePoints();
				updateCurve(false);
			}

		});
		spnrK = new Spinner(groupCalculations, SWT.BORDER);
		spnrK.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		spnrK.setMinimum(1);
		spnrK.setMaximum(1000);
		spnrK.setEnabled(false);
		spnrK.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			public void widgetSelected(SelectionEvent e) {
				FpPoint q = curve.multiplyPoint(pointP, spnrK.getSelection());
				setPointQ(q);
				updateCurve(false);
			}
		});

		label = new Label(groupCalculations, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		label.setText(Messages.getString("ECContentFp.41")); //$NON-NLS-1$

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
		int row = 0;
		int col = 0;
		if(pointSelect != null) {
			int index = -1;
			for(int i = 0; i < points.length; i++) {
				if(points[i] == pointSelect) {
					index = i;
					i = points.length;
				}
			}
			row = (index + 1) % numItems;
			col = (index + 1) / numItems;
		}
		tcPoints.setSelection(row, col);
		if(pointP == null)
			lblP.setText(pointSelect.toString());
		if(pointP != null && pointQ == null)
			lblQ.setText(pointSelect.toString());

		updateCurve(false);
	}

	public void setPointP(FpPoint p) {
		if(p == null) {
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
			view.log(Messages.getString("ECView.Point") + " P = " + pointP.toString()); //$NON-NLS-1$ //$NON-NLS-2$
			lblP.setText(pointP.toString());
		}
		fillTablePoints();
		updateCurve(false);
	}

	public void setPointQ(FpPoint q) {
		if(q == null) {
			pointQ = null;
			lblQ.setText(""); //$NON-NLS-1$
			setPointR(null);
		} else {
			pointQ = q;
			lblQ.setText(pointQ.toString());

			btnDeletePoints.setEnabled(true);

			view.log(Messages.getString("ECView.Point") + " Q = " + pointQ.toString()); //$NON-NLS-1$ //$NON-NLS-2$
			setPointR(curve.addPoints(pointP, pointQ));
		}
		fillTablePoints();
		updateCurve(false);
	}

	public void setPointR(FpPoint r) {
		if(r == null) {
			pointR = null;
			lblR.setText(""); //$NON-NLS-1$
		} else {
			pointR = r;
			lblR.setText(pointR.toString());
			view.log(Messages.getString("ECView.Point") + " R = P + Q = " + pointR.toString()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Sets the label beneath the canvas
	 *
	 */
	public void setCurveLabel() {
		String s;
		if(points == null) {
			s = Messages.getString("ECView.NoCurve"); //$NON-NLS-1$
		} else
			s = curve.toString();
		lblCurve.setText(s);
	}

	private void fillTablePoints() {
		bTable = true;
		if(tablePoints == null)
			return;
		tablePoints.removeAll();
		if(points == null || tablePoints.getSize().y == 0) {
			bTable = false;
			return;
		}

		//fill the table in the points group
		numItems = (tablePoints.getSize().y - 5) / tablePoints.getItemHeight();

		if(numItems * 4 < points.length + 1)
			numItems = (int) ((double) (points.length + 1) / 4 + 0.75);


		tiPoints = new TableItem[numItems];
		for(int i = 0; i < tiPoints.length; i++) {
			tiPoints[i] = new TableItem(tablePoints, SWT.NONE);
			tiPoints[i].addListener(SWT.Settings, new Listener(){
				public void handleEvent(Event event) {
					if((event.detail & SWT.SELECTED) != 0 ){
						tablePoints.deselectAll();
					} else if ((event.type & SWT.Resize) != 0 && event.detail == 0) {
						if(!bTable) {
							fillTablePoints();
						}
					}
				}
			});
		}

		int colNr = 0;
		int itemNr = 1;
		String caption = ""; //$NON-NLS-1$

		if(pointP != null && pointP.isInfinite()){
			caption += "=P"; //$NON-NLS-1$
			tiPoints[0].setBackground(colNr, red);
			tiPoints[0].setForeground(colNr, white);
		}
		if(pointQ != null && pointQ.isInfinite()){
			caption += "=Q"; //$NON-NLS-1$
			tiPoints[0].setBackground(colNr, red);
			tiPoints[0].setForeground(colNr, white);
		}
		if(pointR != null && pointR.isInfinite()){
			caption += "=R"; //$NON-NLS-1$
			tiPoints[0].setBackground(colNr, purple);
			tiPoints[0].setForeground(colNr, white);
		}
		caption = caption.replaceFirst("=", ""); //$NON-NLS-1$ //$NON-NLS-2$


		tiPoints[0].setText(0, caption + " O"); //$NON-NLS-1$


		for(int i = 0; i < points.length; i++) {
			if(points[i] != null) {
				caption = ""; //$NON-NLS-1$
				if(points[i].equals(pointP) || points[i].equals(pointQ) || points[i].equals(pointR)) {
					tiPoints[itemNr].setBackground(colNr, red);
					tiPoints[itemNr].setForeground(colNr, white);

					if(points[i].equals(pointP))
						caption += "=P"; //$NON-NLS-1$
					if(points[i].equals(pointQ))
						caption += "=Q"; //$NON-NLS-1$
					if(points[i].equals(pointR)){
						caption += "=R"; //$NON-NLS-1$
						tiPoints[itemNr].setBackground(colNr, purple);
						tiPoints[itemNr].setForeground(colNr, white);
					}
					caption = caption.replaceFirst("=", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}

				tiPoints[itemNr].setText(colNr, caption + points[i].toString());

				itemNr = (itemNr + 1) % numItems;
				if(itemNr == 0)
					colNr++;
			}
		}
		bTable = false;
	}

	/**
	 * This method controls all the parameters that need to be updated
	 */
	public void updateCurve(boolean full) {
		if(full) {
			pointSelect = null;
			pointP = null;
			pointQ = null;
			pointR = null;
			setPointP(null);
			if(curve == null)
				curve = new ECFp();
			((ECFp) curve).updateCurve(spnrA.getSelection(), spnrB.getSelection(), spnrP.getSelection());
			points = curve.getPoints();
			if(groupPoints != null){
				if(points == null)
					groupPoints.setText(Messages.getString("ECView.Points")); //$NON-NLS-1$
				else {
					groupPoints.setText(Messages.getString("ECView.Points") + " (" + (points.length+1) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					fillTablePoints();
				}
			}
			setCurveLabel();
		}
		canvasCurve.redraw();
	}

	private void drawDiscrete(PaintEvent e) {
		GC gc = e.gc;
		Point size = canvasCurve.getSize();
		double space, x, y;
		int gridSize;
		gridSize = lastPrime - 1;

		gc.setForeground(grey);
		space = size.x - 30;
		x = 25;
		for(int i = 0; i <= gridSize; i++) {
			gc.drawLine((int)x, 5, (int)x, size.y - 25);
			x += space / (gridSize - i);
			space -= space / (gridSize - i);
		}
		space = size.x - 30;
		y = size.y - 25;
		for(int i = 0; i <= gridSize; i++) {
			gc.drawLine(25, (int)y, size.x - 5, (int)y);
			y -= space / (gridSize - i);
			space -= space / (gridSize - i);
		}

		gc.setForeground(black);
		gc.drawLine(25, 5, 25, size.y - 25);
		gc.drawLine(25, size.y - 25, size.x - 5, size.y - 25);
		space = size.x - 30;
			x = 25 + space / gridSize;
			space -= space / gridSize;
			for(int i = 1; i <= gridSize; i++) {
				if(i % 5 == 0) {
					gc.drawLine((int)x, size.y - 32, (int)x, size.y - 18);
					gc.drawText(i + "", (int)x - (i < 10 ? 2 : 6), size.y - 17, true); //$NON-NLS-1$
				} else {
					gc.drawLine((int)x, size.y - 27, (int)x, size.y - 23);
					if(gridSize < 15)
						gc.drawText(i + "", (int)x - (i < 10 ? 2 : 6), size.y - 17, true); //$NON-NLS-1$
				}
				x += space / (gridSize - i);
				space -= space / (gridSize - i);
			}
			space = size.x - 30;
			y = size.y - 25 - space / gridSize;
			space -= space / gridSize;
			for(int i = 1; i <= gridSize; i++) {
				if(i % 5 == 0) {
					gc.drawLine(18, (int)y, 32, (int)y);
					gc.drawText(i + "", i < 10 ? 5 : 3, (int)y - 7, true); //$NON-NLS-1$
				} else {
					gc.drawLine(23, (int)y, 27, (int)y);
					if(gridSize < 15)
						gc.drawText(i + "", i < 10 ? 5 : 3, (int)y - 7, true); //$NON-NLS-1$
				}
				y -= space / (gridSize - i);
				space -= space / (gridSize - i);
			}

		if(points != null) {
			double grid = (double)(size.x - 30) / gridSize;
			for(int i = 0; i < points.length; i++) {
				gc.setForeground(black);
				gc.drawOval((int)(grid * points[i].x) + 25 - 2, (int)(size.y - 25 - grid * points[i].y - 2), 4, 4);
				gc.setBackground(lightBlue);
				gc.fillRectangle((int)(grid * points[i].x) + 25 - 1, (int)(size.y - 25 - grid * points[i].y - 1), 3, 3);
			}
			if(pointP != null && !pointP.isInfinite()){
				gc.setBackground(red);
				gc.setForeground(red);
				x = getLabelPositionX(pointP.x, grid, gridSize);
				y = getLabelPositionY(pointP.y, size, grid, gridSize);

				String point = "P"; //$NON-NLS-1$
				if(pointP.equals(pointQ) || pointP.equals(pointSelect))
					point += "=Q"; //$NON-NLS-1$
				if(pointP.equals(pointR))
					point += "=R"; //$NON-NLS-1$
				gc.drawText(point, (int)(x), (int)(y), true);
				gc.fillRectangle((int)(grid * pointP.x) + 25 - 1, (int)(size.y - 25 - grid * pointP.y - 1), 3, 3);
			}
			if(pointQ != null && !pointQ.isInfinite() && !pointQ.equals(pointP)) {
				gc.setBackground(red);
				gc.setForeground(red);
				x = getLabelPositionX(pointQ.x, grid, gridSize);
				y = getLabelPositionY(pointQ.y, size, grid, gridSize);

				if(pointQ.equals(pointR))
					gc.drawText("Q=R", (int)(x), (int)(y), true); //$NON-NLS-1$
				else
					gc.drawText("Q", (int)(x), (int)(y), true); //$NON-NLS-1$
				gc.fillRectangle((int)(grid * pointQ.x) + 25 - 1, (int)(size.y - 25 - grid * pointQ.y - 1), 3, 3);
			}
			if(pointR != null && !pointR.isInfinite() && !pointR.equals(pointP) && !pointR.equals(pointQ)) {
				gc.setBackground(purple);
				gc.setForeground(purple);
				x = getLabelPositionX(pointR.x, grid, gridSize);
				y = getLabelPositionY(pointR.y, size, grid, gridSize);
				if(pointR.equals(pointSelect))
					gc.drawText("R=Q", (int)(x), (int)(y), true); //$NON-NLS-1$
				else
					gc.drawText("R", (int)(x), (int)(y), true); //$NON-NLS-1$
				gc.fillRectangle((int)(grid * pointR.x) + 25 - 1, (int)(size.y - 25 - grid * pointR.y - 1), 3, 3);
			}
			if(pointSelect != null && !pointSelect.isInfinite() && !pointSelect.equals(pointR) && !pointSelect.equals(pointQ) && !pointSelect.equals(pointP)){
				gc.setBackground(red);
				gc.setForeground(red);
				x = getLabelPositionX(pointSelect.x, grid, gridSize);
				y = getLabelPositionY(pointSelect.y, size, grid, gridSize);

				String point = ""; //$NON-NLS-1$
				if(btnPQ.getSelection()){
					point = "Q"; //$NON-NLS-1$
					if(pointP == null)
						point = "P"; //$NON-NLS-1$
					gc.drawText(point, (int) x, (int) y, true);
					gc.fillRectangle((int)(grid * pointSelect.x) + 25 - 1, (int)(size.y - 25 - grid * pointSelect.y - 1), 3, 3);
				}
			}
		}
	}

	public int getLabelPositionX(int x, double grid, int gridSize){
		return (int)(grid * x) + 25 + (x < gridSize / 2 ? 3 : -8);
	}

	public int getLabelPositionY(int y, Point size, double grid, int gridSize){
		return (int)(size.y - 25 - grid * y + (y < gridSize / 2? -15 : 3));
	}

	private FpPoint findNearestPoint(Double x, Double y){
		double minimum = Double.MAX_VALUE;
		FpPoint nearestPoint = null;
		for(int i=0; i<points.length; i++){
			double currentDistance = (points[i].x-x)*(points[i].x-x)+(points[i].y-y)*(points[i].y-y);
			if(currentDistance < minimum){
				minimum = currentDistance;
				nearestPoint = points[i];
			}
		}
		return nearestPoint;
	}

	public void adjustButtons() {
		rbtnReal.setSelection(false);
		rbtnFP.setSelection(true);
		rbtnFM.setSelection(false);
		rbtnLarge.setSelection(false);
		rbtnSmall.setSelection(true);
		if(pointP == null)
			updateCurve(true);

		cSaveResults.select(view.saveTo);
		btnBrowse.setEnabled(view.saveTo == 2);
		btnSave.setEnabled(view.saveTo != 0);
		cbAutoSave.setEnabled(view.saveTo != 0);
		lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
	}
}
