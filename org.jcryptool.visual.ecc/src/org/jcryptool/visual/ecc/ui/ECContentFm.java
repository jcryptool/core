// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ecc.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.jcryptool.visual.ecc.Messages;
import org.jcryptool.visual.ecc.algorithm.EC;
import org.jcryptool.visual.ecc.algorithm.ECFm;
import org.jcryptool.visual.ecc.algorithm.FmPoint;
import org.jcryptool.visual.ecc.algorithm.FpPoint;

public class ECContentFm extends Composite{

	private boolean bTable = false;
	private Button btnBrowse = null;
	private Button btnDeletePoints = null;
	private Button btnKP = null;
	private Button btnPQ = null;
	private Button btnClear = null;
	private Button btnSave = null;
	private Canvas canvasCurve = null;
	private Button cbAutoSave = null;
	private Button cbShowBinary;
	private Color black = ColorService.BLACK;
	private Color white = ColorService.WHITE;
	private Color lightBlue = new Color(this.getDisplay(), 0, 255, 255);
	private Color purple = new Color(this.getDisplay(), 255, 0, 255);
	private Color grey = new Color(this.getDisplay(), 235, 235, 235);
	private Color red = new Color(this.getDisplay(), 203, 0, 0);
	private Combo cA;
	private Combo cB;
	private Combo cF;
	private Combo cSaveResults = null;
	private EC curve;
	private Group groupCalculations = null;
	private Group groupCurve = null;
	private Group groupCurveAttributes = null;
	private Group groupCurveType = null;
	private Group groupElements = null;
	private Group groupPoints = null;
	private Group groupSettings = null;
	private Group groupSave = null;
	private int numItems;
	private int[] elements;
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
	private Spinner spnrK = null;
	private Spinner spnrM = null;
	private Table tableElements = null;
	private Table tablePoints = null;
	private TableCursor tcPoints;
	private TableItem[] tiPoints;
	private ECView view;
	private Composite content;
	private Group groupSize;
	private Button rbtnSmall;


	public ECContentFm(Composite parent, int style, ECView view) {
		super(parent, style);
		this.view = view;

		this.setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		content = new Composite(scrolledComposite, SWT.NONE);


		GridLayout gridLayout = new GridLayout(2, false);
		content.setLayout(gridLayout);

		createCompositeIntro();
		createGroupCurve();
		createGroupSettings();

		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		createGroupAttributesFm();
		createGroupPoints();
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT.H_SCROLL.
		SmoothScroller.scrollSmooth(scrolledComposite);
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
		groupCurve.setText(Messages.ECContentFm_0); //$NON-NLS-1$
		groupCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		createCanvasCurve();
		
		lblCurve = new Label(groupCurve, SWT.NONE);
		lblCurve.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		lblCurve.setText(""); //$NON-NLS-1$
		
		btnDeletePoints = new Button(groupCurve, SWT.NONE);
		btnDeletePoints.setToolTipText(Messages.ECContentFm_3); //$NON-NLS-1$
		btnDeletePoints.setText(Messages.ECView_RemoveSelection); //$NON-NLS-1$
		btnDeletePoints.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false));
		btnDeletePoints.setEnabled(false);
		btnDeletePoints.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnPQ.setSelection(true);
				btnPQ.setEnabled(false);
				btnClear.setEnabled(false);
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
		
	}

	/**
	 * This method initializes groupSize
	 *
	 */
	private void createGroupSize() {
		groupSize = new Group(groupSettings, SWT.NONE);
		groupSize.setText(Messages.ECView_SelectCurveSize); //$NON-NLS-1$
		groupSize.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		groupSize.setLayout(new GridLayout(2, true));
		
		rbtnSmall = new Button(groupSize, SWT.RADIO);
		rbtnSmall.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnSmall.setText(Messages.ECView_Small); //$NON-NLS-1$
		rbtnSmall.setSelection(true);
		
		rbtnLarge = new Button(groupSize, SWT.RADIO);
		rbtnLarge.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnLarge.setText(Messages.ECView_Large); //$NON-NLS-1$
		rbtnLarge.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
			
			@Override
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
        groupSettings.setText(Messages.ECContentFm_15); //$NON-NLS-1$
        groupSettings.setLayout(new GridLayout());
        groupSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 2));
        
		createGroupSize();
		createGroupCurveType();
		createGroupCurveAttributes();
		createGroupCalculations();
		createGroupSave();
		createGroupElements();
	}

	/**
	 * This method initializes groupPoints
	 *
	 */
	private void createGroupPoints() {
		groupPoints = new Group(content, SWT.NONE);
		groupPoints.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		groupPoints.setLayout(new GridLayout(1, false));
		groupPoints.setText(Messages.ECView_Points); //$NON-NLS-1$
		groupPoints.addListener(SWT.Resize, new Listener(){
			@Override
			public void handleEvent(Event event) {
				fillTablePoints();
			}
		});
		tablePoints = new Table(groupPoints, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.DOUBLE_BUFFERED);
		tablePoints.setHeaderVisible(false);
		tablePoints.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tablePoints.setLinesVisible(false);

		tcPoints = new TableCursor(tablePoints, SWT.NONE);
		tcPoints.setForeground(red);
		tcPoints.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
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
			@Override
			public void paintControl(PaintEvent e) {
				drawDiscrete(e);
			}
		});
		canvasCurve.addMouseMoveListener(new MouseMoveListener(){
			@Override
			public void mouseMove(MouseEvent e) {
				Point size = canvasCurve.getSize();
				if (points != null){
					double grid = (size.x - 30) / (Math.pow(2, spnrM.getSelection()) - 1);
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
			@Override
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
			@Override
			public void mouseEnter(MouseEvent e) {}
			@Override
			public void mouseExit(MouseEvent e) {
				pointSelect = null;
				updateCurve(false);
				fillTablePoints();
				if(pointP == null)
					lblP.setText(""); //$NON-NLS-1$
				if(pointQ == null)
					lblQ.setText(""); //$NON-NLS-1$
			}
			@Override
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
        groupSave.setText(Messages.ECView_SaveResults); //$NON-NLS-1$

        cSaveResults = new Combo(groupSave, SWT.READ_ONLY);
        cSaveResults
                .setItems(new String[] {
                        Messages.ECView_No, Messages.ECView_ToTextEditor, Messages.ECView_ToTextFile}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        cSaveResults.select(view.saveTo);
        cSaveResults.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
        cSaveResults.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
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
        btnBrowse.setText(Messages.ECView_Browse); //$NON-NLS-1$
        btnBrowse.setEnabled(view.saveTo == 2);
        btnBrowse.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
			public void widgetSelected(SelectionEvent e) {
                if (view.selectFileLocation()) {
                	lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
                }  
            }
        });
        btnSave = new Button(groupSave, SWT.NONE);
        btnSave.setText(Messages.ECView_SaveNow); //$NON-NLS-1$
        btnSave.setEnabled(view.saveTo != 0);
        btnSave.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
			public void widgetSelected(SelectionEvent e) {
                view.saveLog();
                lblSaveResults.setText(view.saveTo == 2 ? view.getFileName() : ""); //$NON-NLS-1$
            }
        });
        cbAutoSave = new Button(groupSave, SWT.CHECK);
        cbAutoSave.setText(Messages.ECView_AutoSave); //$NON-NLS-1$
        cbAutoSave.setEnabled(view.autoSave);
        cbAutoSave.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
        cbAutoSave.addSelectionListener(new SelectionListener() {
            @Override
			public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
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
		groupCurveType.setText(Messages.ECView_SelectCurveType); //$NON-NLS-1$
		
		rbtnReal = new Button(groupCurveType, SWT.RADIO);
		rbtnReal.setText(Messages.ECView_RealNumbers); //$NON-NLS-1$
		rbtnReal.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnReal.setSelection(false);
		rbtnReal.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				view.showReal();
			}

		});
		rbtnFP = new Button(groupCurveType, SWT.RADIO);
		rbtnFP.setText("F(p)"); //$NON-NLS-1$
		rbtnFP.setSelection(false);
		rbtnFP.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFP.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				view.showFp();
			}
		});
		rbtnFM = new Button(groupCurveType, SWT.RADIO);
		rbtnFM.setText("F(2^m)"); //$NON-NLS-1$
		rbtnFM.setSelection(true);
		rbtnFM.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false));
		rbtnFM.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
			@Override
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
		groupCurveAttributes.setText(Messages.ECView_SelectCurveAttributes); //$NON-NLS-1$
	}

	private void createGroupAttributesFm() {
		Control[] c = groupCurveAttributes.getChildren();
		for(int i = 0; i < c.length; i++) {
			c[i].dispose();
		}
		
		if(groupElements != null) {
			groupElements.setVisible(true);
		}
		
		if(curve == null) {
			curve = new ECFm();
		}

		Label label2 = new Label(groupCurveAttributes, SWT.NONE);
		label2.setText("m ="); //$NON-NLS-1$
		
		spnrM = new Spinner(groupCurveAttributes, SWT.BORDER);
		spnrM.setMaximum(6);
		spnrM.setSelection(4);
		spnrM.setMinimum(3);
		spnrM.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				((ECFm)curve).setM(spnrM.getSelection());
				setComboF(((ECFm)curve).getIrreduciblePolinomials());
				cF.select(0);
				((ECFm)curve).setF(cF.getSelectionIndex(), true);
				elements =((ECFm)curve).getElements();
				fillTableElements();
				setComboAB();
				updateCurve(true);
			}
		});
//		spnrM.setLayoutData(gridData1);
		GridData gd_spnrM = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_spnrM.widthHint = 100;
		spnrM.setLayoutData(gd_spnrM);
		((ECFm) curve).setM(spnrM.getSelection());


		Label lblF = new Label(groupCurveAttributes, SWT.NONE);
		lblF.setText("f ="); //$NON-NLS-1$
		cF = new Combo(groupCurveAttributes, SWT.READ_ONLY);
		int[] i = ((ECFm) curve).getIrreduciblePolinomials();
		String[] s = new String[i.length];
		for(int j = 0; j < s.length; j++) {
			s[j] = bits2Function(intToBitString(i[j], spnrM.getSelection() + 1));
		}
		cF.setItems(s);
		cF.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		cF.select(0);
		cF.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				((ECFm)curve).setF(cF.getSelectionIndex(), true);
				elements =((ECFm)curve).getElements();
				fillTableElements();
				setComboAB();
				updateCurve(true);
			}
		});
		cF.setToolTipText(Messages.ECContentFm_1); //$NON-NLS-1$
//		cF.setLayoutData(gridData1);
		GridData gd_cF = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_cF.widthHint = 100;
		cF.setLayoutData(gd_cF);
		((ECFm) curve).setF(cF.getSelectionIndex(), true);
		Label label = new Label(groupCurveAttributes, SWT.NONE);
		label.setText("a ="); //$NON-NLS-1$
		cA = new Combo(groupCurveAttributes, SWT.READ_ONLY);
//		cA.setLayoutData(gridData1);
		GridData gd_cA = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_cA.widthHint = 100;
		cA.setLayoutData(gd_cA);
		cA.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				((ECFm)curve).setA(cA.getSelectionIndex(), true);
				((ECFm)curve).setB(cB.getSelectionIndex(), true);
				updateCurve(true);
			}
		});
		cA.setToolTipText(Messages.ECContentFm_2); //$NON-NLS-1$
		Label label1 = new Label(groupCurveAttributes, SWT.NONE);
		label1.setText("b ="); //$NON-NLS-1$
		cB = new Combo(groupCurveAttributes, SWT.READ_ONLY);
		cB.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				((ECFm)curve).setA(cA.getSelectionIndex(), true);
				((ECFm)curve).setB(cB.getSelectionIndex(), true);
				updateCurve(true);
			}
		});
		cB.setToolTipText(Messages.ECContentFm_4); //$NON-NLS-1$
//		cB.setLayoutData(gridData1);
		GridData gd_cB = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_cB.widthHint = 100;
		cB.setLayoutData(gd_cB);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		cbShowBinary = new Button(groupCurveAttributes, SWT.CHECK);
		cbShowBinary.setText(Messages.ECContentFm_40); //$NON-NLS-1$
		cbShowBinary.setLayoutData(gridData);
		cbShowBinary.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				setPointP(pointP);
				setPointQ(pointQ);
				setPointR(pointR);

				int a = cA.getSelectionIndex();
				int b = cB.getSelectionIndex();
				elements =((ECFm)curve).getElements();
				setComboAB();
				cA.select(a);
				((ECFm)curve).setA(cA.getSelectionIndex(), true);
				cB.select(b);
				((ECFm)curve).setB(cB.getSelectionIndex(), true);

				updateCurve(false);
				fillTablePoints();
				fillTableElements();
				int temp = cF.getSelectionIndex();
				setComboF(((ECFm)curve).getIrreduciblePolinomials());
				cF.select(temp);
				setCurveLabel();
			}
		});
		elements = ((ECFm) curve).getElements();
		fillTableElements();
		setComboAB();
	}

	private String bits2Function(String bitString) {
		String s = "";
		for(int i=0; i<bitString.length(); i++){
			switch(i){
			case 0 : s = (bitString.charAt(bitString.length()-i-1)=='1' ? "1"+"+" : "") + s;break;
			case 1 : s = (bitString.charAt(bitString.length()-i-1)=='1' ? "x"+"+" : "") + s;break;
			case 2 : s = (bitString.charAt(bitString.length()-i-1)=='1' ? "x\u00b2"+"+" : "") + s;break;
			case 3 : s = (bitString.charAt(bitString.length()-i-1)=='1' ? "x\u00b3"+"+" : "") + s;break;
			default : s = (bitString.charAt(bitString.length()-i-1)=='1' ? "x^"+(i)+"+" : "") + s;
			}
		}
		return s.substring(0, s.length()-1);
	}

	/**
	 * This method creates the area for the title and the description of the plugin
	 *
	 */
	private void createCompositeIntro() {
    	TitleAndDescriptionComposite titleAndDescription = new TitleAndDescriptionComposite(content);
    	GridData gd_titleAndDescription = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
    	gd_titleAndDescription.widthHint = 800;
    	titleAndDescription.setLayoutData(gd_titleAndDescription);
    	titleAndDescription.setTitle(Messages.ECView_Title);
    	titleAndDescription.setDescription(Messages.ECView_Description);
    	}

	/**
	 * This method initializes groupCalculations
	 *
	 */
	private void createGroupCalculations() {
		groupCalculations = new Group(groupSettings, SWT.NONE);
		groupCalculations.setText(Messages.ECContentFm_45); //$NON-NLS-1$
		groupCalculations.setLayout(new GridLayout(3, false));
		groupCalculations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label label = new Label(groupCalculations, SWT.WRAP);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		label.setText(Messages.ECContentFm_46); //$NON-NLS-1$

		btnPQ = new Button(groupCalculations, SWT.RADIO);
		btnPQ.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		btnPQ.setText(Messages.ECContentFm_47); //$NON-NLS-1$
		btnPQ.setSelection(true);
		btnPQ.setEnabled(false);
		btnPQ.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				setPointQ(null);
			}

		});
		
		btnClear = new Button(groupCalculations, SWT.PUSH);
        btnClear.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        btnClear.setText(Messages.ECView_ClearQ);
        btnClear.setEnabled(false);
        
        btnClear.addSelectionListener(new SelectionListener() {
        	@Override
			public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        	
        	@Override
			public void widgetSelected(SelectionEvent e) {
                setPointQ(null);
                btnClear.setEnabled(false);
            }
        });

		btnKP = new Button(groupCalculations, SWT.RADIO);
		btnKP.setText(Messages.ECContentFm_48); //$NON-NLS-1$
		btnKP.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		btnKP.setEnabled(false);
		btnKP.addSelectionListener(new SelectionListener(){
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				spnrK.setEnabled(btnKP.getSelection());
				FpPoint q = curve.multiplyPoint(pointP, spnrK.getSelection());
				setPointQ(q);
				fillTableElements();
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
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) {
				FpPoint q = curve.multiplyPoint(pointP, spnrK.getSelection());
				setPointQ(q);
				updateCurve(false);
			}
		});

		label = new Label(groupCalculations, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		label.setText(Messages.ECContentFm_49); //$NON-NLS-1$

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

	/**
	 * This method initializes groupElements
	 *
	 */
	private void createGroupElements() {
		groupElements = new Group(groupSettings, SWT.NONE);
		groupElements.setLayout(new GridLayout(1, false));
		groupElements.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		groupElements.setText(Messages.ECView_Elements); //$NON-NLS-1$
		
		tableElements = new Table(groupElements, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.DOUBLE_BUFFERED);
		tableElements.setHeaderVisible(false);
		GridData gd_tableElements = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_tableElements.heightHint = 150;
		tableElements.setLayoutData(gd_tableElements);
		tableElements.setLinesVisible(false);
		for(int i = 0; i < 3; i++){
			new TableColumn(tableElements, SWT.NONE);
		}
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

		if(pointP == null) {
			lblP.setText((new FmPoint(pointSelect, (int) Math.pow(2, spnrM.getSelection()))).toString());
		}
		if(pointP != null && pointQ == null) {
			lblQ.setText((new FmPoint(pointSelect, (int) Math.pow(2, spnrM.getSelection()))).toString());
		}
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
			btnClear.setEnabled(false);
		} else {
			btnKP.setEnabled(true);
			btnPQ.setEnabled(true);
			btnKP.setEnabled(true);
			btnPQ.setEnabled(true);
			btnPQ.setSelection(true);
			btnClear.setEnabled(true);
			btnDeletePoints.setEnabled(true);
			pointP = p;
			view.log("\n" + Messages.ECView_Curve + ": " + lblCurve.getText()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			view.log(Messages.ECView_Point + " P = " + (new FmPoint(pointP, (int) Math.pow(2, spnrM.getSelection()))).toString()); //$NON-NLS-1$ //$NON-NLS-2$
			lblP.setText((new FmPoint(pointP, (int) Math.pow(2, spnrM.getSelection()))).toString());
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
			lblQ.setText((new FmPoint(pointQ, (int) Math.pow(2, spnrM.getSelection()))).toString());

			btnDeletePoints.setEnabled(true);

			view.log(Messages.ECView_Point + " Q = " + (new FmPoint(pointQ, (int) Math.pow(2, spnrM.getSelection()))).toString()); //$NON-NLS-1$ //$NON-NLS-2$
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
			if (r.isInfinite()) {
        		pointR = r;
                lblR.setText("O"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                MessageDialog.openInformation(getShell(), Messages.ECView_InfinityPoint, Messages.ECView_InfinityPointExplanation);
        	} else {
        		pointR = r;
    			lblR.setText((new FmPoint(pointR, (int) Math.pow(2, spnrM.getSelection()))).toString());
    			view.log(Messages.ECView_Point + " R = P + Q = " + (new FmPoint(pointR, (int) Math.pow(2, spnrM.getSelection()))).toString()); //$NON-NLS-1$ //$NON-NLS-2$
        	}
		}
	}

	/**
	 * Sets the label beneath the canvas
	 *
	 */
	public void setCurveLabel() {
		String s;
		if(points == null) {
			s = Messages.ECView_NoCurve; //$NON-NLS-1$
		} else
			s = cbShowBinary.getSelection() ? curve.toString() : ((ECFm) curve).toFunctionString();
		lblCurve.setText(s);
	}

	private void fillTablePoints() {
		bTable = true;
		tablePoints.removeAll();
		if(points == null || tablePoints.getClientArea().height == 0) {
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
				@Override
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

		if(elements != null){
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

					tiPoints[itemNr].setText(colNr, caption + (new FmPoint(points[i], (int) Math.pow(2, spnrM.getSelection()))).toString());//pointToString(points[i]));

					itemNr = (itemNr + 1) % numItems;
					if(itemNr == 0)
						colNr++;
				}
			}
		}

		bTable = false;
	}

	private void setComboAB() {
		cA.removeAll();
		cB.removeAll();
		if(elements == null) {
			return;
		}
		String[] sA = new String[elements.length + 1];
		String[] sB = new String[elements.length];
		for(int i = 0; i < elements.length; i++) {
			if(cbShowBinary.getSelection())
				sA[i] = intToBitString(elements[i], spnrM.getSelection());
			else  {
				if(i == 0)
					sA[i] = "1"; //$NON-NLS-1$
				else if (i == 1)
					sA[i] = "g"; //$NON-NLS-1$
				else
					sA[i] = "g" + i; //$NON-NLS-1$
			}
			sB[i] = sA[i];
		}
		sA[sA.length - 1] = "0"; //$NON-NLS-1$
		cA.setItems(sA);
		cA.select(0);
		((ECFm)curve).setA(cA.getSelectionIndex(), true);
		cB.setItems(sB);
		cB.select(0);
		((ECFm)curve).setB(cB.getSelectionIndex(), true);
	}

	private void setComboF(int[] p) {
		cF.removeAll();
		String[] s = new String[p.length];
		for(int i = 0; i < p.length; i++)
			s[i] = cbShowBinary.getSelection() ? intToBitString(p[i], spnrM.getSelection() + 1) : bits2Function(intToBitString(p[i], spnrM.getSelection() + 1));
		cF.setItems(s);
	}

	private void fillTableElements() {
		tableElements.removeAll();
		if(elements == null) {
			return;
		}

		TableItem[] tiElements = new TableItem[elements.length + 1];
		tiElements[0] = new TableItem(tableElements, SWT.NONE);
		tiElements[0].setText(new String[]{"1", "=", (cbShowBinary.getSelection() ? intToBitString(1, spnrM.getSelection()) : bits2Function(intToBitString(1, spnrM.getSelection()))+"                                     ")}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		for(int i = 1; i < elements.length; i++) {
			tiElements[i] = new TableItem(tableElements, SWT.NONE);
			tiElements[i].setText(new String[]{"g" + (i > 1 ? i : ""), "=", (cbShowBinary.getSelection() ? intToBitString(elements[i], spnrM.getSelection()) : bits2Function(intToBitString(elements[i], spnrM.getSelection())))}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		}
		tiElements[tiElements.length-1] = new TableItem(tableElements, SWT.NONE);
		tiElements[tiElements.length-1].setText(new String[]{"0", "=", (cbShowBinary.getSelection() ? intToBitString(0, spnrM.getSelection()) : ""+0)}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		TableColumn[] tcP = tableElements.getColumns();
		for(int i = 0; i < tcP.length; i++)
			tcP[i].pack();
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
			if(curve == null) {
				curve = new ECFm();
				((ECFm) curve).setM(4);
				setComboF(((ECFm) curve).getIrreduciblePolinomials());
				cF.select(0);
				((ECFm) curve).setF(cF.getSelectionIndex(), true);
				elements = ((ECFm) curve).getElements();
				fillTableElements();
				setComboAB();
				cA.select(4);
				cB.select(0);
			}
			((ECFm) curve).setA(cA.getSelectionIndex(), true);
			((ECFm) curve).setB(cB.getSelectionIndex(), true);

			points = curve.getPoints();
			if(points == null)
				groupPoints.setText(Messages.ECView_Points); //$NON-NLS-1$
			else {
				groupPoints.setText(Messages.ECView_Points + " (" + (points.length+1) + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				fillTablePoints();
			}
			setCurveLabel();
		}
		canvasCurve.redraw();
	}

	private void drawDiscrete(PaintEvent e) {
		GC gc = e.gc;
		Point size = canvasCurve.getSize();
		double space, x, y;
		int gridSize = (int)Math.pow(2, spnrM.getSelection()) - 1;
		int fontHeight = gc.getFontMetrics().getHeight();
		int requiredWidth;

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

		// x-Achsen Beschriftung
		x = 25;
		requiredWidth = gc.textExtent("1").x;
		gc.drawText("1", (int)x - requiredWidth / 2, size.y - fontHeight, true); //$NON-NLS-1$
		x += space / gridSize;
		space -= space / gridSize;
		for(int i = 1; i <= gridSize; i++) {
			if( i % 5 == 0) {
				gc.drawLine((int)x, size.y - 32, (int)x, size.y - 18);
			}
			else {
				gc.drawLine((int)x, size.y - 27, (int)x, size.y - 23);
			}
			if(spnrM.getSelection() < 5 || i % 5 == 0) {
				if(i == gridSize) {
					requiredWidth = gc.textExtent("0").x;
					gc.drawText("0", (int)x - requiredWidth / 2, size.y - fontHeight, true); //$NON-NLS-1$
				}
				else if (i == 1) {
					requiredWidth = gc.textExtent("g").x;
					gc.drawText("g", (int)x - requiredWidth / 2, size.y - fontHeight, true); //$NON-NLS-1$
				}
				else {
					requiredWidth = gc.textExtent("g" + i).x;
					gc.drawText("g" + i, (int)x - requiredWidth / 2, size.y - fontHeight, true); //$NON-NLS-1$
				}
			}
			x += space / (gridSize - i);
			space -= space / (gridSize - i);
		}
		
		// y-Achsen Beschriftung
		// Die auskommentierten Zeilen würden alignen die Beschriftung direkt neben die y-Achse.
		// Das ist schöner als die aktuelle Variante.
		// Da dort aber zu wenig Platz ist, werden die jeweiligen Zeilen darunter genutzt.
		space = size.x - 30;
		y = size.y - 25;
		requiredWidth = (int) gc.getFontMetrics().getAverageCharacterWidth();
//		gc.drawText("1", 2 * requiredWidth, (int)y - fontHeight / 2, true); //$NON-NLS-1$
		gc.drawText("1", requiredWidth, (int)y - fontHeight / 2, true); //$NON-NLS-1$
		y -= space / gridSize;
		space -= space / gridSize;
		for(int i = 1; i <= gridSize; i++) {
			if( i % 5 == 0) {
				gc.drawLine(18, (int)y, 32, (int)y);
			}
			else {
				gc.drawLine(23, (int)y, 27, (int)y);
			}
			if (spnrM.getSelection() < 5 || i % 5 == 0) {
				if (i == gridSize) {
//					gc.drawText("0", 2 * requiredWidth, (int) y - fontHeight / 2, true); //$NON-NLS-1$
					gc.drawText("0", requiredWidth, (int) y - fontHeight / 2, true); //$NON-NLS-1$
				} else if (i == 1) {
//					gc.drawText("g", 2 * requiredWidth, (int) y - fontHeight / 2, true); //$NON-NLS-1$
					gc.drawText("g", requiredWidth, (int) y - fontHeight / 2, true); //$NON-NLS-1$
				} else {
//					gc.drawText("g" + i, i < 10 ? requiredWidth : 0, (int) y - fontHeight / 2, true); //$NON-NLS-1$
					gc.drawText("g" + i, 0, (int) y - fontHeight / 2, true); //$NON-NLS-1$
				}
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

	private String intToBitString(int i, int length) {
		String s = ""; //$NON-NLS-1$
		int j = i;
		for(int k = 0; k < length; k++) {
			s = (j % 2) + s;
			j /= 2;
		}
		return s;
	}

	public void adjustButtons() {
		rbtnReal.setSelection(false);
		rbtnFP.setSelection(false);
		rbtnFM.setSelection(true);
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
