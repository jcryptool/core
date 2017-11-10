//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
 //-----END DISCLAIMER-----

package org.jcryptool.visual.aup.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.aup.AndroidUnlockPatternPlugin;

/**
 *
 * @author Michael Schäfer
 * @author Stefan Kraus
 *
 */
public class AupView extends ViewPart {

	public enum ApuState {
		ERROR, WARNING, INFO, OK
	}


	private Composite headingBox;
	private Composite centerBox;
	private Group centerGroup;
	private Composite controlBox;
	private Group helpBox;
	private Group optionbox;
	private Label[] cntrBtn = new Label[9];
	private Region regionCircle;
	private Button setPattern;
	private Button changePattern;
	private Button checkPattern;
	private Button btnSave;
	private Button btnCancel;
	private ScrolledComposite descTextScroller;
	private StyledText descText;
	private Backend logic;
	private StyledText instrText1;
	private StyledText instrText2;
	private StyledText instrText3;
	private CLabel statusText;
	private Label instrTextHeading;
	private Label descTextHeading;
	private Composite parent;
	private Boolean patternInput = false;
	private Boolean inputFinished = false;
	private Boolean advancedGraphic = false;
	private Font nFont;
	private Font bFont;

	//precomputed values for APU permutations depending on the APU's length
	private static int[] apuPerm = {
		1624,	//lenght 4
		8776,	//lenght 5 (4+5)
		34792,	//lenght 6 (4+5+6)
		107704,	//lenght 7 (4+5+6+7)
		248408,	//lenght 8 (4+5+6+7+8)
		389112	//lenght 9 (4+5+6+7+8+9)
		};

	/**
	 * The constructor.
	 */
	public AupView() {
		logic = new Backend(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 *
	 * @param parent
	 *            a swt Composite, which is the parent..
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;

		// set context help
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(
						parent,
						AndroidUnlockPatternPlugin.PLUGIN_ID
								+ ".ContextHelpView"); //$NON-NLS-1$

		// Create the ScrolledComposite to scroll horizontally and vertically
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL);
		sc.setMinHeight(500);
		sc.setMinWidth(600);
		// Create a child composite to hold the controls
		final Composite child = new Composite(sc, SWT.NONE);
		child.setLayout(new FormLayout());

		sc.setContent(child);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		headingBox = new Composite(child, SWT.NONE);
		controlBox = new Composite(child, SWT.NONE);
		centerBox = new Composite(child, SWT.NONE);
		
		centerGroup = new Group(centerBox, SWT.NONE);
		centerGroup.setText(Messages.AndroidUnlockPattern_centerbox_text);
		
		optionbox = new Group(controlBox, SWT.NONE);
		optionbox.setText(Messages.AndroidUnlockPattern_GroupHeadingModes);
		optionbox.setToolTipText(Messages.AndroidUnlockPattern_optionbox_toolTipText);
		
		helpBox = new Group(child, SWT.NONE);
		helpBox.setToolTipText(Messages.AndroidUnlockPattern_helpBox_toolTipText);
		helpBox.setText(Messages.AndroidUnlockPattern_GroupHeadingHelp);
		
		setPattern = new Button(optionbox, SWT.RADIO);
		setPattern.setText(Messages.AndroidUnlockPattern_ModeSetText);
		changePattern = new Button(optionbox, SWT.RADIO);
		changePattern.setText(Messages.AndroidUnlockPattern_ModeChangeText);
		checkPattern = new Button(optionbox, SWT.RADIO);
		checkPattern.setText(Messages.AndroidUnlockPattern_ModeCheckText);

		for (int i = 0; i < cntrBtn.length; i++) {
			cntrBtn[i] = new Label(centerGroup, SWT.NONE);
			cntrBtn[i].setData("nummer", i); //$NON-NLS-1$
			cntrBtn[i].setSize(40, 40); // set initial size; will be updated during initiation
		}

		statusText = new CLabel(centerGroup, SWT.LEFT);
		statusText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		//get standard font
		FontData fd = setPattern.getFont().getFontData()[0];
		nFont = new Font(child.getDisplay(), fd);
		fd.setStyle(SWT.BOLD);
		bFont = new Font(child.getDisplay(), fd);

		initLayout();
		addActions();
		centerResize();

		logic.init();
		child.pack();	//update the size of the visuals child's

		//dispose allocated resources on shutdown
		parent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				for(Label l:cntrBtn)
				{
					if(l.getImage() != null) l.getImage().dispose(); //dispose image
				}
				if(statusText.getImage() != null) statusText.getImage().dispose();
				headingBox.getChildren()[0].getFont().dispose();
				nFont.dispose();
				bFont.dispose();
			}
		});

		//test if advanced graphic processing is available
		Image img = AndroidUnlockPatternPlugin.getImageDescriptor("icons/view.gif").createImage(child.getDisplay()); //$NON-NLS-1$
		GC gc = new GC(img);
		gc.setAdvanced(true);	// will do nothing if advanced graphic processing is not available
		if (gc.getAdvanced()){
			advancedGraphic = true;
		}
		gc.dispose();
		img.dispose();
	}

	/**
	 * sets the initial Layout
	 */
	private void initLayout() {
		
		headingBox.setLayout(new GridLayout());
		headingBox.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		FormData fd_headingBox = new FormData();
		fd_headingBox.left = new FormAttachment(0);
		fd_headingBox.right = new FormAttachment(100);
		fd_headingBox.top = new FormAttachment(0);
		headingBox.setLayoutData(fd_headingBox);

		// top
		controlBox.setLayout(new GridLayout());
		final FormData fd_controlBox = new FormData();
		fd_controlBox.top = new FormAttachment(headingBox);
		fd_controlBox.bottom = new FormAttachment(helpBox);
		fd_controlBox.right = new FormAttachment(centerBox);
		fd_controlBox.left = new FormAttachment(0);
		controlBox.setLayoutData(fd_controlBox);

		// optionbox
		optionbox.setLayout(new GridLayout());
		
		btnSave = new Button(controlBox, SWT.NONE);
		btnSave.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnSave.setText(Messages.AndroidUnlockPattern_ButtonSaveText);
		btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
	
		btnCancel = new Button(controlBox, SWT.NONE);
		btnCancel.setEnabled(false);
		btnCancel.setToolTipText(Messages.AndroidUnlockPattern_btnCancel_toolTipText);
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnCancel.setText(Messages.AndroidUnlockPattern_ButtonCancelText);

		// center
		centerBox.setLayout(new GridLayout());
		FormData fd_centerBox = new FormData();
		fd_centerBox.top = new FormAttachment(headingBox);
		fd_centerBox.bottom = new FormAttachment(helpBox);
		fd_centerBox.left = new FormAttachment(controlBox);
		fd_centerBox.right = new FormAttachment(100);
		centerBox.setLayoutData(fd_centerBox);
		
		GridLayout clayout = new GridLayout(3, false);
		clayout.marginLeft = 10;
		clayout.marginRight = 10;
		clayout.marginTop = 10;
		clayout.horizontalSpacing = 15;
		clayout.verticalSpacing = 15;
		centerGroup.setLayout(clayout);
		centerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label heading = new Label(headingBox, SWT.NONE);
		heading.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		heading.setFont(FontService.getHeaderFont());
		heading.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		heading.setText(Messages.AndroidUnlockPattern_Heading);

		Text lblHeaderInfoText = new Text(headingBox, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		lblHeaderInfoText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblHeaderInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		lblHeaderInfoText.setText(Messages.AndroidUnlockPattern_HeadingInfoText);

		GridLayout gl_helpBox = new GridLayout(2, true);
		helpBox.setLayout(gl_helpBox);
		final FormData fd_helpBox = new FormData();
		fd_helpBox.top = new FormAttachment(75);
		fd_helpBox.bottom = new FormAttachment(100);
		fd_helpBox.left = new FormAttachment(0);
		fd_helpBox.right = new FormAttachment(100);
		helpBox.setLayoutData(fd_helpBox);

		instrTextHeading = new Label(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrTextHeading.setText(Messages.AupView_0); //$NON-NLS-1$

		descTextHeading = new Label(helpBox, SWT.READ_ONLY | SWT.WRAP);
		GridData gd_descTextHeading = new GridData();
		gd_descTextHeading.horizontalIndent = 40;
		descTextHeading.setLayoutData(gd_descTextHeading);
		descTextHeading.setText(Messages.AndroidUnlockPattern_helpBox_descText_Heading);

		instrText1 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText1.setDoubleClickEnabled(false);
		instrText1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		instrText1.setAlignment(SWT.LEFT);
		instrText1.setText(Messages.AupView_1);

		descTextScroller = new ScrolledComposite(helpBox, SWT.V_SCROLL);
		GridData gd_descTextScroller = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
		gd_descTextScroller.horizontalIndent = 40;
		descTextScroller.setLayoutData(gd_descTextScroller);
		descTextScroller.setExpandHorizontal(true);
		descTextScroller.setExpandVertical(true);

		descText = new StyledText(descTextScroller, SWT.READ_ONLY | SWT.WRAP);
		descText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		descText.setDoubleClickEnabled(false);
		descText.setText(Messages.AndroidUnlockPattern_helpBox_descText);
		descTextScroller.setContent(descText);

		instrText2 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText2.setDoubleClickEnabled(false);
		instrText2.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		instrText2.setAlignment(SWT.LEFT);
		instrText2.setText(Messages.AupView_1);

		instrText3 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText3.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText3.setDoubleClickEnabled(false);
		instrText3.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		instrText3.setAlignment(SWT.LEFT);
		instrText3.setText(Messages.AupView_1);

	}

	// Code from dev.eclipse.org Licence: Eclipse Public License
	// http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet294.java?view=co
	private int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}

	/**
	 * add listener to buttons etc
	 *
	 */
	private void addActions() {
		centerGroup.addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {
				centerResize();
				centerGroup.redraw();
			}
		});

		// centrcalBtns
		for (int i = 0; i < cntrBtn.length; i++) {
//			cntrBtn[i].addSelectionListener(new SelectionListener() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					if (e.widget
//							.getData("icon").toString().regionMatches(false, 6, "b", 0, 1)) { //$NON-NLS-1$ //$NON-NLS-2$
//						// to get here the button needs to be unclicked
//						// (in this case e.widget.getData("icon").toString() is
//						// "icons/black.png")
//						// for performance reasons only the 7. char of the
//						// string is checked
//						final int btnNummer = (Integer) e.widget
//								.getData("nummer"); //$NON-NLS-1$
//						logic.btnMainClick(btnNummer);
//					}
//				}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//
//				}
//			});
			cntrBtn[i].addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}

				@Override
				public void mouseDown(MouseEvent e) {
					patternInput = true; //enable touch input
					((Label)e.widget).setCapture(false); //release mouse to fire subsequent mouse events

					if (!inputFinished && e.widget.getData("icon").toString().regionMatches(false, 6, "b", 0, 1)) { //$NON-NLS-1$ //$NON-NLS-2$
						// to get here the button needs to be unclicked
						// (in this case e.widget.getData("icon").toString() is "icons/black.png")
						// for performance reasons only the 7. char of the string is checked
						int btnNummer = (Integer) e.widget.getData("nummer"); //$NON-NLS-1$
						logic.btnMainClick(btnNummer);
					}
				}

				@Override
				public void mouseUp(MouseEvent e) {
					patternInput = false; //disable touch input
					inputFinished = true; //disable subsequent pattern input
				}

			});
			cntrBtn[i].addMouseTrackListener(new MouseTrackAdapter() {

				@Override
				public void mouseEnter(MouseEvent e) {
					if (patternInput && !inputFinished &&  e.widget.getData("icon").toString().regionMatches(false, 6, "b", 0, 1)) { //$NON-NLS-1$ //$NON-NLS-2$
						final int btnNummer = (Integer) e.widget.getData("nummer"); //$NON-NLS-1$
						logic.btnMainClick(btnNummer);
					}
				}
			});
		}

		btnSave.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnSave.setEnabled(false);
				btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				btnCancel.setEnabled(false);
				patternInput = inputFinished = false;
				if(logic.isFirst()) {
					int length = 0;
					for(Label a : cntrBtn) {
						if(a.getData("icon").toString().regionMatches(false, 6, "g", 0, 1)) //$NON-NLS-1$ //$NON-NLS-2$
							length++;
					}
					descText.setText(String.format(Messages.AndroidUnlockPattern_helpBox_descText_Security, Messages.AndroidUnlockPattern_helpBox_descText, length, apuPerm[length-4]));
					recalcDescTextScrolling();
					helpBox.layout(true);
				}
				logic.btnSaveClick();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btnCancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCancel.setEnabled(false);
				btnSave.setEnabled(false);
				btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				patternInput = inputFinished = false;
				logic.btnCancelClick();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		centerGroup.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				drawLines(e);
			}

		});

		setPattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				logic.setModus(1);
				patternInput = inputFinished = false;
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		changePattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//				if(logic.isChangeable())
//					setStatusText(Messages.Backend_PopupCancelMessage, ApuState.WARNING); //$NON-NLS-1$
//				else
//					setStatusText("", null); //$NON-NLS-1$
				logic.setModus(2);
				patternInput = inputFinished = false;
				setStatusText("", null); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		checkPattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
//				if(logic.isChangeable())
//					setStatusText(Messages.Backend_PopupCancelMessage, ApuState.WARNING); //$NON-NLS-1$
//				else
//					setStatusText("", null); //$NON-NLS-1$
				logic.setModus(3);
				patternInput = inputFinished = false;
				setStatusText("", null); //$NON-NLS-1$
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
		descTextScroller.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				recalcDescTextScrolling();
			}
		});
	}

	protected void drawLines(PaintEvent e) {
		centerResize();
		e.gc.setForeground(logic.getLineColor());
		e.gc.setLineWidth(10);
		for (int[] point : logic.getPoints()) {
			e.gc.drawLine(point[0], point[1], point[2], point[3]);
		}

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		parent.setFocus();
	}

	public int MsgBox(String header, String msg, int options) {
		final MessageBox mb = new MessageBox(Display.getDefault()
				.getActiveShell(), options);
		mb.setText(header);
		mb.setMessage(msg);
		return mb.open();
	}

	public void setBtnSaveText(String text) {
		btnSave.setText(text);
	}

	/**
	 * @return the setPattern
	 */
	public Button getSetPattern() {
		return setPattern;
	}

//	/**
//	 * @param setPattern
//	 *            the setPattern to set
//	 */
//	public void setSetPattern(Button setPattern) {
//		this.setPattern = setPattern;
//	}

	/**
	 * @return the changePattern
	 */
	public Button getChangePattern() {
		return changePattern;
	}

//	/**
//	 * @param changePattern
//	 *            the changePattern to set
//	 */
//	public void setChangePattern(Button changePattern) {
//		this.changePattern = changePattern;
//	}

	/**
	 * @return the checkPattern
	 */
	public Button getCheckPattern() {
		return checkPattern;
	}

//	/**
//	 * @param checkPattern
//	 *            the checkPattern to set
//	 */
//	public void setCheckPattern(Button checkPattern) {
//		this.checkPattern = checkPattern;
//	}

	/**
	 * @return the cntrBtn
	 */
	public Label[] getCntrBtn() {
		return cntrBtn;
	}

	/**
	 * @return the btnSave
	 */
	public Button getBtnSave() {
		return btnSave;
	}

	/**
	 * @return the btnCancel
	 */
	public Button getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @return the centerbox
	 */
	public Group getCenterbox() {
		return centerGroup;
	}

	public void centerResize() {
		// centerButtons
		GridLayout layout = (GridLayout) centerGroup.getLayout();
		int size = Math.min(centerGroup.getClientArea().height
				- layout.marginHeight * 2 - layout.horizontalSpacing * 3 - layout.marginTop
				- statusText.getClientArea().height,
				centerGroup.getClientArea().width - layout.marginWidth * 2
						- layout.verticalSpacing * 2 - layout.marginLeft - layout.marginRight);
		if (size < 0)
			return; // Layout not yet initialized
		size = size / 3; // 3x3 centrcalBtns
		logic.recalculateLines();
		for (int i = 0; i < cntrBtn.length; i++) {
			if (cntrBtn[i].getData("icon") != null) { //$NON-NLS-1$
				cntrBtn[i].getImage().dispose(); //dispose the old image
				String tmpStr = cntrBtn[i].getData("icon").toString(); //$NON-NLS-1$
				ImageData tmp = AndroidUnlockPatternPlugin
						.getImageDescriptor(tmpStr).getImageData()
						.scaledTo(size, size);
				Image img = new Image(cntrBtn[i].getDisplay(), tmp);
				GC gc = new GC(img);

				if(cntrBtn[i].getData("arc") != null && advancedGraphic) { //$NON-NLS-1$
					Image arrow = null;
					if(tmpStr.regionMatches(false, 6, "g", 0, 1)) //$NON-NLS-1$
						arrow = AndroidUnlockPatternPlugin.getImageDescriptor("icons/ArrowGreen.png").createImage(cntrBtn[i].getDisplay()); //$NON-NLS-1$
					else if(tmpStr.regionMatches(false, 6, "y", 0, 1)) //$NON-NLS-1$
						arrow = AndroidUnlockPatternPlugin.getImageDescriptor("icons/ArrowYellow.png").createImage(cntrBtn[i].getDisplay()); //$NON-NLS-1$

					if(arrow != null) {
						Transform oldTransform = new Transform(gc.getDevice());
						gc.getTransform(oldTransform);

						Transform transform = new Transform(gc.getDevice());
						transform.translate(size/2, size/2);
						transform.rotate((Float)cntrBtn[i].getData("arc")); //$NON-NLS-1$
						transform.translate(-size/2, -size/2);

						gc.setTransform(transform);
						gc.drawImage(arrow, 0, 0, arrow.getImageData().width, arrow.getImageData().height, 0, 0, size, size);
						gc.setTransform(oldTransform);
						oldTransform.dispose();
						transform.dispose();
						arrow.dispose();
					}
				}

				cntrBtn[i].setImage(img);
			}
			regionCircle = new Region();
			regionCircle.add(circle(size / 2, cntrBtn[i].getBounds().width / 2,
					cntrBtn[i].getBounds().height / 2));

			cntrBtn[i].setRegion(regionCircle);
			cntrBtn[i].setLayoutData(new GridData(size, size));
			regionCircle.dispose();
		}
	}

	/**
	 * Resets the state information of the plug-in. Asks the user first if he is really sure.
	 */
	public void resetClick() {
		int tmp = MsgBox(Messages.Backend_PopupResetHeading,
				Messages.Backend_PopupResetMessage, SWT.YES | SWT.NO
						| SWT.ICON_WARNING);
		if (tmp == SWT.YES) {
			setStatusText("", null); //$NON-NLS-1$
			patternInput = inputFinished = false;
			btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
			descText.setText(Messages.AndroidUnlockPattern_helpBox_descText);
			recalcDescTextScrolling();
			logic.reset();
		}
	}

	protected void setStatusText(String message, ApuState state) {
		if(statusText.getImage() != null)
			statusText.getImage().dispose();
		if(state == null) {
			statusText.setImage(null);
			statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		} else {
		switch (state) {
			case ERROR:
				statusText.setImage(AndroidUnlockPatternPlugin.getImageDescriptor("platform:/plugin/org.eclipse.ui/icons/full/obj16/error_tsk.png").createImage()); //$NON-NLS-1$
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
				break;
			case WARNING:
				statusText.setImage(AndroidUnlockPatternPlugin.getImageDescriptor("platform:/plugin/org.eclipse.ui/icons/full/obj16/warn_tsk.png").createImage()); //$NON-NLS-1$
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW));
				break;
			case INFO:
				statusText.setImage(AndroidUnlockPatternPlugin.getImageDescriptor("platform:/plugin/org.eclipse.jface/icons/full/message_info.png").createImage()); //$NON-NLS-1$
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
				break;
			case OK:
				statusText.setImage(AndroidUnlockPatternPlugin.getImageDescriptor("/icons/ok_st_obj.gif").createImage()); //$NON-NLS-1$
				statusText.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
				break;
			default:
				statusText.setImage(null);
			}
		}
		statusText.setText(message);
	}


	/**
	 * Updates the user progress information in the description box.
	 */
	protected void updateProgress() {
		switch(logic.getModus()) {
			case 1: {	// set
				if (logic.isFirst()) { // 1. step
					//set texts
					instrTextHeading.setText(Messages.AupView_0);
					instrText1.setText(Messages.AupView_1);
					instrText2.setText(Messages.AupView_2);
					instrText3.setText(""); //$NON-NLS-1$

					//set highlight
					instrText1.setFont(bFont);
					instrText2.setFont(nFont);
				} else { // 2. step
					instrText1.setFont(nFont);
					instrText2.setFont(bFont);
				}

				break;
			}
			case 2: {	// change
				if (!logic.isChangeable()) { // 1. step
					//set texts
					instrTextHeading.setText(Messages.AupView_8);
					instrText1.setText(Messages.AupView_4);
					instrText2.setText(Messages.AupView_5);
					instrText3.setText(Messages.AupView_6);
					
					//set highlight
					instrText1.setFont(bFont);
					instrText2.setFont(nFont);
					instrText3.setFont(nFont);
				} else if (logic.isFirst()) { //2. step
					instrText1.setFont(nFont);
					instrText2.setFont(bFont);
					instrText3.setFont(nFont);
				} else { // 3. step
					instrText1.setFont(nFont);
					instrText2.setFont(nFont);
					instrText3.setFont(bFont);
				}

				break;
			}
			case 3: {	// check
				instrTextHeading.setText(Messages.AupView_3);
				instrText1.setText(Messages.AupView_7);
				instrText2.setText(""); //$NON-NLS-1$
				instrText3.setText(""); //$NON-NLS-1$

				instrText1.setFont(bFont);
				break;
			}
		}
		helpBox.layout(true);
	}

	/**
	 * Recalculate the scrolling area size for the description text.
	 * <br>
	 * Has to be called after every description text update.
	 */
	private void recalcDescTextScrolling() {
		Point size = descText.computeSize(descTextScroller.getClientArea().width, SWT.DEFAULT);	// compute required height for fixed width
		descTextScroller.setMinHeight(size.y); // enable scrolling
	}

}