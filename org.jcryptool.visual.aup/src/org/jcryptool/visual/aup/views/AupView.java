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
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
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


	//precomputed values for APU permutations depending on the APU's length
	private static int[] apuPerm = {
		1624,	//lenght 4
		8776,	//lenght 5 (4+5)
		34792,	//lenght 6 (4+5+6)
		107704,	//lenght 7 (4+5+6+7)
		248408,	//lenght 8 (4+5+6+7+8)
		389112	//lenght 9 (4+5+6+7+8+9)
		};
	private Composite headingBox;
	private Composite centerBox;
	private Group centerGroup;
	private Composite controlBox;
	private Composite visualization;
	private Group helpBox;
	private Group optionbox;
	private Label[] cntrBtn = new Label[9];
	private Region regionCircle;
	private Button setPattern;
	private Button changePattern;
	private Button checkPattern;
	private Button btnSave;
	private Button btnCancel;
	private StyledText descText;
	private Backend logic;
	private StyledText instrText1;
	private StyledText instrText2;
	private StyledText instrText3;
	private CLabel statusText;
	private CLabel infoText;
	private Label instrTextHeading;
	private Label descTextHeading;
	private Composite parent;
	private Boolean patternInput = false;
	private Boolean inputFinished = false;
	private Boolean advancedGraphic = false;
	private Font nFont;
	private Font bFont;

	/**
	 * The constructor.
	 */
	public AupView() {
		logic = new Backend(this);
	}

	/**
	 * add listener to buttons etc
	 *
	 */
	private void addActions() {
		visualization.addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {
				centerResize();
				visualization.redraw();
			}
		});

		// centrcalBtns
		for (int i = 0; i < cntrBtn.length; i++) {
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
			public void widgetDefaultSelected(SelectionEvent e) {
			}

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
					resizeControl(descText);
				}
				logic.btnSaveClick();
			}
		});

		btnCancel.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnCancel.setEnabled(false);
				btnSave.setEnabled(false);
				btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
				patternInput = inputFinished = false;
				logic.btnCancelClick();

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
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				logic.setModus(1);
				patternInput = inputFinished = false;
			}

		});
		changePattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				logic.setModus(2);
				patternInput = inputFinished = false;
				setStatusText("", null); //$NON-NLS-1$
			}

		});
		checkPattern.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				logic.setModus(3);
				patternInput = inputFinished = false;
				setStatusText("", null); //$NON-NLS-1$
			}

		});
	}


	public void centerResize() {
		// centerButtons
		GridLayout layout = (GridLayout) visualization.getLayout();
		int size = Math.min(
				visualization.getClientArea().height - layout.marginHeight * 2 - layout.horizontalSpacing * 3
						- layout.marginTop,
				visualization.getClientArea().width - layout.marginWidth * 2 - layout.verticalSpacing * 2
						- layout.marginLeft - layout.marginRight);
		if (size < 0)
			return; // Layout not yet initialized
		size = size / 3; // 3x3 centrcalBtns
		logic.recalculateLines();
		for (int i = 0; i < cntrBtn.length; i++) {
			if (cntrBtn[i].getData("icon") != null) { //$NON-NLS-1$
				cntrBtn[i].getImage().dispose(); // dispose the old image
				String tmpStr = cntrBtn[i].getData("icon").toString(); //$NON-NLS-1$
				ImageData tmp = AndroidUnlockPatternPlugin.getImageDescriptor(tmpStr).getImageData().scaledTo(size,
						size);
				Image img = new Image(cntrBtn[i].getDisplay(), tmp);
				GC gc = new GC(img);

				if (cntrBtn[i].getData("arc") != null && advancedGraphic) { //$NON-NLS-1$
					Image arrow = null;
					if (tmpStr.regionMatches(false, 6, "g", 0, 1)) //$NON-NLS-1$
						arrow = AndroidUnlockPatternPlugin.getImageDescriptor("icons/ArrowGreen.png") //$NON-NLS-1$
								.createImage(cntrBtn[i].getDisplay());
					else if (tmpStr.regionMatches(false, 6, "y", 0, 1)) //$NON-NLS-1$
						arrow = AndroidUnlockPatternPlugin.getImageDescriptor("icons/ArrowYellow.png") //$NON-NLS-1$
								.createImage(cntrBtn[i].getDisplay());

					if (arrow != null) {
						Transform oldTransform = new Transform(gc.getDevice());
						gc.getTransform(oldTransform);

						Transform transform = new Transform(gc.getDevice());
						transform.translate(size / 2, size / 2);
						transform.rotate((Float) cntrBtn[i].getData("arc")); //$NON-NLS-1$
						transform.translate(-size / 2, -size / 2);

						gc.setTransform(transform);
						gc.drawImage(arrow, 0, 0, arrow.getImageData().width, arrow.getImageData().height, 0, 0, size,
								size);
						gc.setTransform(oldTransform);
						oldTransform.dispose();
						transform.dispose();
						arrow.dispose();
					}
				}

				cntrBtn[i].setImage(img);
			}
			regionCircle = new Region();
			regionCircle.add(circle(size / 2, cntrBtn[i].getBounds().width / 2, cntrBtn[i].getBounds().height / 2));

			cntrBtn[i].setRegion(regionCircle);
			cntrBtn[i].setLayoutData(new GridData(size, size));
			regionCircle.dispose();
		}
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
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		Composite child = new Composite(sc, SWT.NONE);
		child.setLayout(new GridLayout(2, false));

		sc.setContent(child);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		headingBox = new Composite(child, SWT.NONE);
		headingBox.setLayout(new GridLayout());
		headingBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		headingBox.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		Label heading = new Label(headingBox, SWT.NONE);
		heading.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		heading.setFont(FontService.getHeaderFont());
		heading.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		heading.setText(Messages.AndroidUnlockPattern_Heading);

		Text lblHeaderInfoText = new Text(headingBox, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		lblHeaderInfoText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblHeaderInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		lblHeaderInfoText.setText(Messages.AndroidUnlockPattern_HeadingInfoText);		
		
		controlBox = new Composite(child, SWT.NONE);
		controlBox.setLayout(new GridLayout());
		controlBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		
		optionbox = new Group(controlBox, SWT.NONE);
		optionbox.setLayout(new GridLayout());
		optionbox.setText(Messages.AndroidUnlockPattern_GroupHeadingModes);
		optionbox.setToolTipText(Messages.AndroidUnlockPattern_optionbox_toolTipText);
		
		setPattern = new Button(optionbox, SWT.RADIO);
		setPattern.setText(Messages.AndroidUnlockPattern_ModeSetText);
		changePattern = new Button(optionbox, SWT.RADIO);
		changePattern.setText(Messages.AndroidUnlockPattern_ModeChangeText);
		checkPattern = new Button(optionbox, SWT.RADIO);
		checkPattern.setText(Messages.AndroidUnlockPattern_ModeCheckText);

		btnSave = new Button(controlBox, SWT.NONE);
		btnSave.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnSave.setText(Messages.AndroidUnlockPattern_ButtonSaveText);
		btnSave.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
	
		btnCancel = new Button(controlBox, SWT.NONE);
		btnCancel.setEnabled(false);
		btnCancel.setToolTipText(Messages.AndroidUnlockPattern_btnCancel_toolTipText);
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnCancel.setText(Messages.AndroidUnlockPattern_ButtonCancelText);
		
		centerBox = new Composite(child, SWT.NONE);
		centerBox.setLayout(new GridLayout());
		centerBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		centerGroup = new Group(centerBox, SWT.NONE);
		centerGroup.setText(Messages.AndroidUnlockPattern_centerbox_text);		
		centerGroup.setLayout(new GridLayout());
		centerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		visualization = new Composite(centerGroup, SWT.NONE);
		GridData gd_visualization = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_visualization.widthHint = 300;
		gd_visualization.heightHint = 300;
		visualization.setLayoutData(gd_visualization);
		GridLayout gl_visualization = new GridLayout(3, false);
		gl_visualization.marginLeft = 10;
		gl_visualization.marginRight = 10;
		gl_visualization.marginTop = 10;
		gl_visualization.horizontalSpacing = 15;
		gl_visualization.verticalSpacing = 15;
		visualization.setLayout(gl_visualization);

		
		for (int i = 0; i < cntrBtn.length; i++) {
			cntrBtn[i] = new Label(visualization, SWT.NONE);
			cntrBtn[i].setData("nummer", i); //$NON-NLS-1$
			cntrBtn[i].setSize(40, 40); // set initial size; will be updated during initiation
		}

		statusText = new CLabel(centerGroup, SWT.LEFT);
		statusText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		infoText = new CLabel(centerGroup, SWT.LEFT);
		infoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		infoText.setText(Messages.AupView_9);
		infoText.setImage(AndroidUnlockPatternPlugin.getImageDescriptor("platform:/plugin/org.eclipse.jface/icons/full/message_info.png").createImage()); //$NON-NLS-1$
		
		helpBox = new Group(child, SWT.NONE);
		helpBox.setToolTipText(Messages.AndroidUnlockPattern_helpBox_toolTipText);
		helpBox.setText(Messages.AndroidUnlockPattern_GroupHeadingHelp);
		helpBox.setLayout(new GridLayout(2, true));
		GridData gd_helpBox = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_helpBox.widthHint = 450;
		helpBox.setLayoutData(gd_helpBox);
		
		instrTextHeading = new Label(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrTextHeading.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		instrTextHeading.setText(Messages.AupView_0); //$NON-NLS-1$

		descTextHeading = new Label(helpBox, SWT.READ_ONLY | SWT.WRAP);
		GridData gd_descTextHeading = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_descTextHeading.horizontalIndent = 40;
		descTextHeading.setLayoutData(gd_descTextHeading);
		descTextHeading.setText(Messages.AndroidUnlockPattern_helpBox_descText_Heading);

		instrText1 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		instrText1.setText(Messages.AupView_1);

		descText = new StyledText(helpBox, SWT.READ_ONLY | SWT.V_SCROLL | SWT.WRAP);
		GridData gd_descText = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 3);
		gd_descText.horizontalIndent = 40;
		gd_descText.heightHint = 150;
		descText.setLayoutData(gd_descText);
		descText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		descText.setText(Messages.AndroidUnlockPattern_helpBox_descText);

		instrText2 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		instrText2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText2.setText(Messages.AupView_1);

		instrText3 = new StyledText(helpBox, SWT.READ_ONLY | SWT.WRAP);
		instrText3.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		instrText3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		instrText3.setText(Messages.AupView_1);
		
		addActions();
		centerResize();

		logic.init();
		child.pack();	//update the size of the visuals child's
		
		//get standard font
		FontData fd = setPattern.getFont().getFontData()[0];
		nFont = new Font(parent.getDisplay(), fd);
		fd.setStyle(SWT.BOLD);
		bFont = new Font(parent.getDisplay(), fd);

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
		
		sc.setMinSize(child.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
	 * @return the btnCancel
	 */
	public Button getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @return the btnSave
	 */
	public Button getBtnSave() {
		return btnSave;
	}

	/**
	 * @return the centerbox
	 */
	public Group getCenterbox() {
		return centerGroup;
	}

	/**
	 * @return the changePattern
	 */
	public Button getChangePattern() {
		return changePattern;
	}

	/**
	 * @return the checkPattern
	 */
	public Button getCheckPattern() {
		return checkPattern;
	}

	/**
	 * @return the cntrBtn
	 */
	public Label[] getCntrBtn() {
		return cntrBtn;
	}

	/**
	 * @return the setPattern
	 */
	public Button getSetPattern() {
		return setPattern;
	}

	public int MsgBox(String header, String msg, int options) {
		final MessageBox mb = new MessageBox(Display.getDefault()
				.getActiveShell(), options);
		mb.setText(header);
		mb.setMessage(msg);
		return mb.open();
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
			resizeControl(descText);
			logic.reset();
		}
	}

	/**
	 * Recalculate the size aof a control
	 */
	private void resizeControl(Control control) {
		parent.layout(new Control[] {control});
	}

	public void setBtnSaveText(String text) {
		btnSave.setText(text);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		parent.setFocus();
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
}
