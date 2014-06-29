package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class CrtVerViewComposite extends Composite implements PaintListener {
	// Object Controller
	CrtVerViewController controller = new CrtVerViewController();

	// Variables
	static Text TextRootCaFromDay;
	static Text TextCaFromDay;
	static Text TextCertFromDay;
	static Text TextRootCaThruDay;
	static Text TextCaThruDay;
	static Text TextCertThruDay;
	static Text TextSignatureDateDay;
	static Text TextVerificationDateDay;

	static Label thruRootCa;
	static Label fromRootCa;
	static Label thruCa;
	static Label fromCa;
	static Label thruCert;
	static Label fromCert;
	static Label signatureDate;
	static Label verificationDate;

	static Scale ScaleCertBegin;
	static Scale ScaleCertEnd;
	static Scale ScaleCaBegin;
	static Scale ScaleCaEnd;
	static Scale ScaleRootCaBegin;
	static Scale ScaleRootCaEnd;
	static Scale ScaleVerificationDate;
	static Scale ScaleSignatureDate;

	private Text txtDescription;

	static Button btnLoadRootCa;
	static Button btnLoadCa;
	static Button btnLoadUserCert;
	static Button btnValidate;
	static Canvas canvas1;
	static Canvas canvas2;
	static int arrowSigDiff = 0;
	static int arrowVerDiff = 0;

	static ControlDecoration validitySymbol;
	static Text txtLogWindow;

	/**
	 * counter for number of performed validations
	 */
	static int validationCounter = 0;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CrtVerViewComposite(Composite parent, int style, CrtVerView view) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		setLayout(new FillLayout(SWT.HORIZONTAL));

		// Adds reset button to the toolbar
		IToolBarManager toolBarMenu = view.getViewSite().getActionBars()
				.getToolBarManager();
		Action action = new Action("Reset", IAction.AS_PUSH_BUTTON) {public void run() {controller.reset();}}; //$NON-NLS-1$
		action.setImageDescriptor(Activator
				.getImageDescriptor("icons/reset.gif")); //$NON-NLS-1$
		toolBarMenu.add(action);

		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));

		TabItem tbtmSchalenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setText(Messages.CrtVerViewComposite_title);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		tbtmSchalenmodell.setControl(composite);
		GridLayout gl_composite = new GridLayout(9, false);
		gl_composite.marginTop = 5;
		gl_composite.marginBottom = 15;
		composite.setLayout(gl_composite);

		txtDescription = new Text(composite, SWT.READ_ONLY | SWT.WRAP
				| SWT.MULTI);
		txtDescription.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtDescription.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
				SWT.NORMAL));
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 9, 2));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblNotValidBefore = new Label(composite, SWT.NONE);
		lblNotValidBefore.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 5, 1));
		lblNotValidBefore.setFont(SWTResourceManager.getFont("Lucida Grande",
				11, SWT.NORMAL));
		lblNotValidBefore.setAlignment(SWT.CENTER);
		lblNotValidBefore.setText(Messages.CrtVerViewComposite_notValidBefore);

		Label lblNotValidAfter = new Label(composite, SWT.NONE);
		lblNotValidAfter.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		lblNotValidAfter.setText(Messages.CrtVerViewComposite_notValidAfter);
		lblNotValidAfter.setFont(SWTResourceManager.getFont("Lucida Grande",
				11, SWT.NORMAL));
		lblNotValidAfter.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 5, 1));

		Label label = new Label(composite_4, SWT.NONE);
		label.setText(controller.scaleUpdate(0, 180,
				controller.getDateformat3()));
		label.setBounds(0, 0, 59, 14);

		Label label_1 = new Label(composite_4, SWT.NONE);
		label_1.setText(controller.scaleUpdate(360, 180,
				controller.getDateformat3()));
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(301, 0, 59, 14);

		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_3.widthHint = 360;
		composite_3.setLayoutData(gd_composite_3);

		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setBounds(0, 0, 59, 14);
		label_2.setText(controller.scaleUpdate(0, 180,
				controller.getDateformat3()));

		Label label_3 = new Label(composite_3, SWT.NONE);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(301, 0, 59, 14);
		label_3.setText(controller.scaleUpdate(360, 180,
				controller.getDateformat3()));
		new Label(composite, SWT.NONE);

		Label lblRootCa = new Label(composite, SWT.NONE);
		GridData gd_lblRootCa = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblRootCa.heightHint = 20;
		gd_lblRootCa.verticalIndent = 10;
		lblRootCa.setLayoutData(gd_lblRootCa);
		lblRootCa.setText(Messages.CrtVerViewComposite_RootCa);
		lblRootCa.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
				SWT.NORMAL));
		lblRootCa.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);

		final Scale ScaleRootCaBegin = new Scale(composite, SWT.NONE);
		ScaleRootCaBegin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller.setLogText(Messages.CrtVerViewComposite_RootCa + " \""
						+ Messages.CrtVerViewComposite_notValidBefore + "\" "
						+ Messages.CrtVerViewComposite_dateSet + " "
						+ controller.getFromRootCa());
			}
		});
		CrtVerViewComposite.ScaleRootCaBegin = ScaleRootCaBegin;
		ScaleRootCaBegin.setToolTipText("");

		ScaleRootCaBegin.setMaximum(360);
		GridData gd_ScaleRootCaBegin = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 5, 1);
		gd_ScaleRootCaBegin.widthHint = 360;
		ScaleRootCaBegin.setLayoutData(gd_ScaleRootCaBegin);
		ScaleRootCaBegin.setSelection(180);

		final Scale ScaleRootCaEnd = new Scale(composite, SWT.NONE);
		ScaleRootCaEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller.setLogText(Messages.CrtVerViewComposite_RootCa + " \""
						+ Messages.CrtVerViewComposite_notValidAfter + "\" "
						+ Messages.CrtVerViewComposite_dateSet + " "
						+ controller.getThruRootCa());
			}
		});
		CrtVerViewComposite.ScaleRootCaEnd = ScaleRootCaEnd;
		GridData gd_ScaleRootCaEnd = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_ScaleRootCaEnd.widthHint = 360;
		ScaleRootCaEnd.setLayoutData(gd_ScaleRootCaEnd);
		ScaleRootCaEnd.setMaximum(360);
		ScaleRootCaEnd.setSelection(180);

		btnLoadRootCa = new Button(composite, SWT.NONE);
		btnLoadRootCa.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String text = Messages.CrtVerViewComposite_loadRootCa;
				Button button = (Button) e.widget;
				int buttonWidth = button.getSize().x;
				int buttonHeight = button.getSize().y;

				// Get text bounds.
				int textWidth = e.gc.textExtent(text).x;
				int textHeight = e.gc.textExtent(text).y;

				// Calculate text coordinates.
				int textX = ((buttonWidth - textWidth) / 2);
				int textY = ((buttonHeight - textHeight) / 2);

				btnLoadRootCa.setText("");
				// Draw the new text.
				e.gc.drawText(text, textX, textY, true);
			}
		});
		GridData gd_btnLoadRootCa = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnLoadRootCa.heightHint = 30;
		gd_btnLoadRootCa.widthHint = 200;
		btnLoadRootCa.setLayoutData(gd_btnLoadRootCa);
		btnLoadRootCa.setText(Messages.CrtVerViewComposite_loadRootCa);
		btnLoadRootCa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ChooseCert wiz = new ChooseCert(3);
					WizardDialog dialog = new WizardDialog(new Shell(Display
							.getCurrent()), wiz) {
						@Override
						protected void configureShell(Shell newShell) {
							super.configureShell(newShell);
							// set size of the wizard-window (x,y)
							newShell.setSize(550, 500);
						}
					};
					if (dialog.open() == Window.OK) {
						// Hier kann man Aktionen durfuehren die passieren
						// sollen wenn die WizardPage aufgerufen wird
						// zB aktivieren/deaktivieren von Buttons der
						// Hauptansicht
					}
				} catch (Exception ex) {
					LogUtil.logError(Activator.PLUGIN_ID, ex);
				}
			}
		});

		Label lblCa = new Label(composite, SWT.NONE);
		GridData gd_lblCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_lblCa.verticalIndent = 10;
		gd_lblCa.heightHint = 20;
		lblCa.setLayoutData(gd_lblCa);
		lblCa.setText(Messages.CrtVerViewComposite_Ca);
		lblCa.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
				SWT.NORMAL));
		lblCa.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);

		final Scale ScaleCaBegin = new Scale(composite, SWT.NONE);
		ScaleCaBegin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller.setLogText(Messages.CrtVerViewComposite_Ca + " \""
						+ Messages.CrtVerViewComposite_notValidBefore + "\" "
						+ Messages.CrtVerViewComposite_dateSet + " "
						+ controller.getFromCA());
			}
		});
		CrtVerViewComposite.ScaleCaBegin = ScaleCaBegin;
		ScaleCaBegin.setMaximum(360);
		GridData gd_ScaleCaBegin = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 5, 1);
		gd_ScaleCaBegin.widthHint = 360;
		ScaleCaBegin.setLayoutData(gd_ScaleCaBegin);
		ScaleCaBegin.setSelection(180);

		final Scale ScaleCaEnd = new Scale(composite, SWT.NONE);
		ScaleCaEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller.setLogText(Messages.CrtVerViewComposite_Ca + " \""
						+ Messages.CrtVerViewComposite_notValidAfter + "\" "
						+ Messages.CrtVerViewComposite_dateSet + " "
						+ controller.getThruCA());
			}
		});
		CrtVerViewComposite.ScaleCaEnd = ScaleCaEnd;
		ScaleCaEnd.setMaximum(360);
		GridData gd_ScaleCaEnd = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_ScaleCaEnd.widthHint = 360;
		ScaleCaEnd.setLayoutData(gd_ScaleCaEnd);
		ScaleCaEnd.setSelection(180);

		btnLoadCa = new Button(composite, SWT.NONE);
		btnLoadCa.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String text = Messages.CrtVerViewComposite_loadCa;
				Button button = (Button) e.widget;
				int buttonWidth = button.getSize().x;
				int buttonHeight = button.getSize().y;

				// Get text bounds.
				int textWidth = e.gc.textExtent(text).x;
				int textHeight = e.gc.textExtent(text).y;

				// Calculate text coordinates.
				int textX = ((buttonWidth - textWidth) / 2);
				int textY = ((buttonHeight - textHeight) / 2);

				btnLoadCa.setText("");
				// Draw the new text.
				e.gc.drawText(text, textX, textY, true);
			}
		});
		GridData gd_btnLoadCa = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnLoadCa.heightHint = 30;
		gd_btnLoadCa.widthHint = 200;
		btnLoadCa.setLayoutData(gd_btnLoadCa);
		btnLoadCa.setText(Messages.CrtVerViewComposite_loadCa);
		btnLoadCa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ChooseCert wiz = new ChooseCert(2);
					WizardDialog dialog = new WizardDialog(new Shell(Display
							.getCurrent()), wiz) {
						@Override
						protected void configureShell(Shell newShell) {
							super.configureShell(newShell);
							// set size of the wizard-window (x,y)
							newShell.setSize(550, 500);
						}
					};
					if (dialog.open() == Window.OK) {
						// Hier kann man Aktionen durfuehren die passieren
						// sollen wenn die WizardPage aufgerufen wird
						// zB aktivieren/deaktivieren von Buttons der
						// Hauptansicht
					}
				} catch (Exception ex) {
					LogUtil.logError(Activator.PLUGIN_ID, ex);
				}
			}
		});

		Label lblUserCertificate = new Label(composite, SWT.NONE);
		GridData gd_lblUserCertificate = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_lblUserCertificate.verticalIndent = 10;
		gd_lblUserCertificate.heightHint = 20;
		lblUserCertificate.setLayoutData(gd_lblUserCertificate);
		lblUserCertificate
				.setText(Messages.CrtVerViewComposite_UserCertificate);
		lblUserCertificate.setFont(SWTResourceManager.getFont("Lucida Grande",
				11, SWT.NORMAL));
		lblUserCertificate.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);

		final Scale ScaleCertBegin = new Scale(composite, SWT.NONE);
		ScaleCertBegin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller
						.setLogText(Messages.CrtVerViewComposite_UserCertificate + " \"" + 
								Messages.CrtVerViewComposite_notValidBefore + "\" "	+ 
								Messages.CrtVerViewComposite_dateSet + " " + 
								controller.getFromClient());
			}
		});
		CrtVerViewComposite.ScaleCertBegin = ScaleCertBegin;
		ScaleCertBegin.setMaximum(360);
		GridData gd_ScaleCertBegin = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 5, 1);
		gd_ScaleCertBegin.widthHint = 360;
		ScaleCertBegin.setLayoutData(gd_ScaleCertBegin);
		ScaleCertBegin.setSelection(180);

		final Scale ScaleCertEnd = new Scale(composite, SWT.NONE);
		ScaleCertEnd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller
						.setLogText(Messages.CrtVerViewComposite_UserCertificate + " \"" + 
								Messages.CrtVerViewComposite_notValidAfter + "\" " + 
								Messages.CrtVerViewComposite_dateSet + " " + 
								controller.getThruClient());
			}
		});
		CrtVerViewComposite.ScaleCertEnd = ScaleCertEnd;
		ScaleCertEnd.setMaximum(360);
		GridData gd_ScaleCertEnd = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_ScaleCertEnd.widthHint = 360;
		ScaleCertEnd.setLayoutData(gd_ScaleCertEnd);
		ScaleCertEnd.setSelection(180);

		btnLoadUserCert = new Button(composite, SWT.NONE);
		btnLoadUserCert.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String text = Messages.CrtVerViewComposite_loadUserCert;
				Button button = (Button) e.widget;
				int buttonWidth = button.getSize().x;
				int buttonHeight = button.getSize().y;

				// Get text bounds.
				int textWidth = e.gc.textExtent(text).x;
				int textHeight = e.gc.textExtent(text).y;

				// Calculate text coordinates.
				int textX = ((buttonWidth - textWidth) / 2);
				int textY = ((buttonHeight - textHeight) / 2);

				btnLoadUserCert.setText("");
				// Draw the new text.
				e.gc.drawText(text, textX, textY, true);
			}
		});
		// Selection Listeners | Scales
		btnLoadUserCert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ChooseCert wiz = new ChooseCert(1);
					WizardDialog dialog = new WizardDialog(new Shell(Display
							.getCurrent()), wiz) {
						@Override
						protected void configureShell(Shell newShell) {
							super.configureShell(newShell);
							// set size of the wizard-window (x,y)
							newShell.setSize(550, 500);
						}
					};
					if (dialog.open() == Window.OK) {
						// Hier kann man Aktionen durfuehren die passieren
						// sollen wenn die WizardPage aufgerufen wird
						// zB aktivieren/deaktivieren von Buttons der
						// Hauptansicht
					}
				} catch (Exception ex) {
					LogUtil.logError(Activator.PLUGIN_ID, ex);
				}
			}
		});

		GridData gd_btnLoadUserCert = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnLoadUserCert.heightHint = 30;
		gd_btnLoadUserCert.widthHint = 200;
		btnLoadUserCert.setLayoutData(gd_btnLoadUserCert);
		btnLoadUserCert.setText(Messages.CrtVerViewComposite_loadUserCert);

		Label lblArrowSig = new Label(composite, SWT.NONE);
		lblArrowSig.setAlignment(SWT.CENTER);
		lblArrowSig.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
				SWT.NORMAL));
		GridData gd_lblArrowSig = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblArrowSig.verticalIndent = 5;
		gd_lblArrowSig.heightHint = 25;
		lblArrowSig.setLayoutData(gd_lblArrowSig);
		lblArrowSig.setForeground(SWTResourceManager.getColor(30, 144, 255));
		lblArrowSig.setText(Messages.CrtVerViewComposite_signatureDate);
		new Label(composite, SWT.NONE);

		canvas1 = new Canvas(composite, SWT.NONE);
		canvas1.setLayout(new GridLayout(1, false));
		GridData gd_canvas1 = new GridData(SWT.FILL, SWT.FILL, false, false, 5,
				2);
		gd_canvas1.heightHint = 25;
		gd_canvas1.widthHint = 359;
		canvas1.setLayoutData(gd_canvas1);
		canvas1.addPaintListener(this);

		canvas2 = new Canvas(composite, SWT.NONE);
		GridData gd_canvas2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1,
				2);
		gd_canvas2.widthHint = 364;
		canvas2.setLayoutData(gd_canvas2);
		canvas2.setLayout(new GridLayout(1, false));
		canvas2.addPaintListener(this);

		Label lblLog = new Label(composite, SWT.NONE);
		lblLog.setText(Messages.CrtVerViewComposite_lblLog_text);

		Label lblArrowVer = new Label(composite, SWT.NONE);
		lblArrowVer.setAlignment(SWT.CENTER);
		GridData gd_lblArrowVer = new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1);
		gd_lblArrowVer.verticalIndent = 5;
		gd_lblArrowVer.heightHint = 25;
		lblArrowVer.setLayoutData(gd_lblArrowVer);
		lblArrowVer.setFont(SWTResourceManager.getFont("Lucida Grande", 11,
				SWT.NORMAL));
		lblArrowVer.setForeground(SWTResourceManager.getColor(72, 61, 139));
		lblArrowVer.setText(Messages.CrtVerViewComposite_verificationDate);
		new Label(composite, SWT.NONE);

		txtLogWindow = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL);
		txtLogWindow.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtLogWindow.setFont(SWTResourceManager.getFont("Lucida Grande", 13,
				SWT.NORMAL));
		txtLogWindow.setEditable(false);
		GridData gd_txtLogWindow = new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 6);
		gd_txtLogWindow.heightHint = 300;
		gd_txtLogWindow.widthHint = 200;
		txtLogWindow.setLayoutData(gd_txtLogWindow);

		Label SeperatorHorizontal = new Label(composite, SWT.SEPARATOR
				| SWT.HORIZONTAL);
		GridData gd_SeperatorHorizontal = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 8, 1);
		gd_SeperatorHorizontal.widthHint = 0;
		SeperatorHorizontal.setLayoutData(gd_SeperatorHorizontal);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Composite composite_5 = new Composite(composite, SWT.NONE);
		composite_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 6, 1);
		gd_composite_5.widthHint = 720;
		composite_5.setLayoutData(gd_composite_5);

		Label label_4 = new Label(composite_5, SWT.NONE);
		label_4.setText(controller.scaleUpdate(0, 360,
				controller.getDateformat3()));
		label_4.setBounds(0, 0, 59, 14);

		Label label_5 = new Label(composite_5, SWT.NONE);
		label_5.setText(controller.scaleUpdate(720, 360,
				controller.getDateformat3()));
		label_5.setAlignment(SWT.RIGHT);
		label_5.setBounds(666, 0, 59, 14);

		Label lblSignatureDate = new Label(composite, SWT.NONE);
		GridData gd_lblSignatureDate = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_lblSignatureDate.verticalIndent = 10;
		gd_lblSignatureDate.heightHint = 20;
		lblSignatureDate.setLayoutData(gd_lblSignatureDate);
		lblSignatureDate.setText(Messages.CrtVerViewComposite_signatureDate);
		lblSignatureDate.setFont(SWTResourceManager.getFont("Lucida Grande",
				11, SWT.NORMAL));
		lblSignatureDate.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);

		final Scale ScaleSignatureDate = new Scale(composite, SWT.NONE);
		ScaleSignatureDate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller
						.setLogText(Messages.CrtVerViewComposite_signatureDate
								+ " " + Messages.CrtVerViewComposite_dateSet
								+ " " + controller.getSigDate());
			}
		});
		CrtVerViewComposite.ScaleSignatureDate = ScaleSignatureDate;
		ScaleSignatureDate.setMaximum(720);
		GridData gd_ScaleSignatureDate = new GridData(SWT.LEFT, SWT.FILL,
				false, false, 6, 1);
		gd_ScaleSignatureDate.widthHint = 720;
		ScaleSignatureDate.setLayoutData(gd_ScaleSignatureDate);
		ScaleSignatureDate.setSelection(360);

		Label lblVerificationDate = new Label(composite, SWT.NONE);
		GridData gd_lblVerificationDate = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_lblVerificationDate.verticalIndent = 10;
		gd_lblVerificationDate.heightHint = 20;
		lblVerificationDate.setLayoutData(gd_lblVerificationDate);

		lblVerificationDate
				.setText(Messages.CrtVerViewComposite_verificationDate);
		lblVerificationDate.setFont(SWTResourceManager.getFont("Lucida Grande",
				11, SWT.NORMAL));
		lblVerificationDate.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);

		final Scale ScaleVerificationDate = new Scale(composite, SWT.NONE);
		ScaleVerificationDate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				controller.parseDatesFromComposite();
				controller
						.setLogText(Messages.CrtVerViewComposite_verificationDate
								+ " "
								+ Messages.CrtVerViewComposite_dateSet
								+ " " + controller.getVerDate());
			}
		});
		CrtVerViewComposite.ScaleVerificationDate = ScaleVerificationDate;
		GridData gd_ScaleVerificationDate = new GridData(SWT.LEFT, SWT.FILL,
				false, false, 6, 1);
		gd_ScaleVerificationDate.widthHint = 720;
		ScaleVerificationDate.setLayoutData(gd_ScaleVerificationDate);
		ScaleVerificationDate.setMaximum(720);
		ScaleVerificationDate.setSelection(360);

		Group grpDetails = new Group(composite, SWT.NONE);
		grpDetails.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		grpDetails.setFont(SWTResourceManager.getFont("Lucida Grande", 11, SWT.BOLD));
		GridData gd_grpDetails = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 8, 1);
		gd_grpDetails.verticalIndent = 15;
		gd_grpDetails.widthHint = 840;
		grpDetails.setLayoutData(gd_grpDetails);
		grpDetails.setText(Messages.CrtVerViewComposite_details);
		GridLayout gl_grpDetails = new GridLayout(10, false);
		gl_grpDetails.marginHeight = 10;
		gl_grpDetails.marginWidth = 10;
		gl_grpDetails.verticalSpacing = 10;
		gl_grpDetails.horizontalSpacing = 0;
		grpDetails.setLayout(gl_grpDetails);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);

		Label LabelHeaderRootCa = new Label(grpDetails, SWT.NONE);
		GridData gd_LabelHeaderRootCa = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_LabelHeaderRootCa.widthHint = 130;
		LabelHeaderRootCa.setLayoutData(gd_LabelHeaderRootCa);
		LabelHeaderRootCa.setText(Messages.CrtVerViewComposite_RootCa);

		Label LabelHeaderCa = new Label(grpDetails, SWT.NONE);
		GridData gd_LabelHeaderCa = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelHeaderCa.widthHint = 130;
		LabelHeaderCa.setLayoutData(gd_LabelHeaderCa);
		LabelHeaderCa.setText(Messages.CrtVerViewComposite_Ca);

		Label LabelHeaderCert = new Label(grpDetails, SWT.NONE);
		GridData gd_LabelHeaderCert = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelHeaderCert.widthHint = 130;
		LabelHeaderCert.setLayoutData(gd_LabelHeaderCert);
		LabelHeaderCert.setText(Messages.CrtVerViewComposite_UserCertificate);
		new Label(grpDetails, SWT.NONE);

		Label SeperatorDetailsVertical = new Label(grpDetails, SWT.SEPARATOR
				| SWT.VERTICAL);
		SeperatorDetailsVertical.setAlignment(SWT.CENTER);
		GridData gd_SeperatorDetailsVertical = new GridData(SWT.LEFT,
				SWT.TOP, false, false, 1, 4);
		gd_SeperatorDetailsVertical.heightHint = 100;
		gd_SeperatorDetailsVertical.widthHint = 50;
		SeperatorDetailsVertical.setLayoutData(gd_SeperatorDetailsVertical);
		new Label(grpDetails, SWT.NONE);

		Label LabelHeaderSignatureDate = new Label(grpDetails, SWT.NONE);
		GridData gd_LabelHeaderSignatureDate = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_LabelHeaderSignatureDate.widthHint = 130;
		LabelHeaderSignatureDate.setLayoutData(gd_LabelHeaderSignatureDate);
		LabelHeaderSignatureDate
				.setText(Messages.CrtVerViewComposite_signatureDate);

		Label LabelHeaderVerificationDate = new Label(grpDetails, SWT.NONE);
		GridData gd_LabelHeaderVerificationDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelHeaderVerificationDate.widthHint = 130;
		LabelHeaderVerificationDate.setLayoutData(gd_LabelHeaderVerificationDate);
		LabelHeaderVerificationDate
				.setText(Messages.CrtVerViewComposite_verificationDate);
		Label lblValidFrom = new Label(grpDetails, SWT.NONE);
		lblValidFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblValidFrom.setText(Messages.CrtVerViewComposite_validFrom);
		new Label(grpDetails, SWT.NONE);

		Composite composite_from_rootca = new Composite(grpDetails, SWT.NONE);
		composite_from_rootca.setLayout(new GridLayout(2, false));

		TextRootCaFromDay = new Text(composite_from_rootca, SWT.BORDER);
		TextRootCaFromDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextRootCaFromDay);
			}
		});
		TextRootCaFromDay
				.setToolTipText(Messages.CrtVerViewComposite_rootCaFromDay);
		TextRootCaFromDay.setText("1");
		TextRootCaFromDay.setTextLimit(2);
		GridData gd_TextRootCaFromDay = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_TextRootCaFromDay.widthHint = 17;
		TextRootCaFromDay.setLayoutData(gd_TextRootCaFromDay);
		TextRootCaFromDay.setSize(24, 19);

		Label LabelRootCaFrom = new Label(composite_from_rootca, SWT.NONE);
		GridData gd_LabelRootCaFrom = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelRootCaFrom.widthHint = 60;
		LabelRootCaFrom.setLayoutData(gd_LabelRootCaFrom);
		fromRootCa = LabelRootCaFrom;

		// Initialize Label "From Root CA" with actual date
		LabelRootCaFrom.setText("");

		Composite composite_from_ca = new Composite(grpDetails, SWT.NONE);
		composite_from_ca.setLayout(new GridLayout(2, false));

		TextCaFromDay = new Text(composite_from_ca, SWT.BORDER);
		TextCaFromDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextCaFromDay);
			}
		});
		TextCaFromDay.setToolTipText(Messages.CrtVerViewComposite_caFromDay);
		TextCaFromDay.setText("1");
		TextCaFromDay.setTextLimit(2);
		GridData gd_TextCaFromDay = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_TextCaFromDay.widthHint = 17;
		TextCaFromDay.setLayoutData(gd_TextCaFromDay);

		Label LabelCaFrom = new Label(composite_from_ca, SWT.NONE);
		GridData gd_LabelCaFrom = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelCaFrom.widthHint = 60;
		LabelCaFrom.setLayoutData(gd_LabelCaFrom);
		fromCa = LabelCaFrom;

		// Initialize Label "From CA" with actual date
		LabelCaFrom.setText("");

		Composite composite_from_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_from_user_cert.setLayout(new GridLayout(2, false));

		TextCertFromDay = new Text(composite_from_user_cert, SWT.BORDER);
		TextCertFromDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextCertFromDay);
			}
		});
		TextCertFromDay
				.setToolTipText(Messages.CrtVerViewComposite_userCertificateFromDay);
		TextCertFromDay.setText("1");
		TextCertFromDay.setTextLimit(2);
		GridData gd_TextCertFromDay = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_TextCertFromDay.widthHint = 17;
		TextCertFromDay.setLayoutData(gd_TextCertFromDay);

		Label LabelCertFrom = new Label(composite_from_user_cert, SWT.NONE);
		GridData gd_LabelCertFrom = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelCertFrom.widthHint = 60;
		LabelCertFrom.setLayoutData(gd_LabelCertFrom);
		fromCert = LabelCertFrom;

		// Initialize Label "From User Cert" with actual date
		LabelCertFrom.setText("");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);

		Composite composite_1 = new Composite(grpDetails, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));

		TextSignatureDateDay = new Text(composite_1, SWT.BORDER);
		TextSignatureDateDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextSignatureDateDay);
			}
		});
		TextSignatureDateDay
				.setToolTipText(Messages.CrtVerViewComposite_signatureDateDay);
		TextSignatureDateDay.setText("1");
		TextSignatureDateDay.setTextLimit(2);
		GridData gd_TextSignatureDateDay = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_TextSignatureDateDay.widthHint = 17;
		TextSignatureDateDay.setLayoutData(gd_TextSignatureDateDay);

		Label LabelSignatureDate = new Label(composite_1, SWT.NONE);
		GridData gd_LabelSignatureDate = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_LabelSignatureDate.widthHint = 60;
		LabelSignatureDate.setLayoutData(gd_LabelSignatureDate);
		signatureDate = LabelSignatureDate;

		// Initialize Label "Signature Date" with actual date
		LabelSignatureDate.setText("");

		Composite composite_2 = new Composite(grpDetails, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));

		TextVerificationDateDay = new Text(composite_2, SWT.BORDER);
		TextVerificationDateDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextVerificationDateDay);
			}
		});
		TextVerificationDateDay
				.setToolTipText(Messages.CrtVerViewComposite_verificationDateDay);
		TextVerificationDateDay.setText("1");
		TextVerificationDateDay.setTextLimit(2);
		GridData gd_TextVerificationDateDay = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_TextVerificationDateDay.widthHint = 17;
		TextVerificationDateDay.setLayoutData(gd_TextVerificationDateDay);

		final Label LabelVerificationDate = new Label(composite_2, SWT.NONE);
		GridData gd_LabelVerificationDate = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_LabelVerificationDate.widthHint = 60;
		LabelVerificationDate.setLayoutData(gd_LabelVerificationDate);
		verificationDate = LabelVerificationDate;
		LabelVerificationDate.setText("");

		Label lblValidThru = new Label(grpDetails, SWT.NONE);
		lblValidThru.setText(Messages.CrtVerViewComposite_validThru);
		new Label(grpDetails, SWT.NONE);

		Composite composite_thru_rootca = new Composite(grpDetails, SWT.NONE);
		composite_thru_rootca.setLayout(new GridLayout(2, false));

		TextRootCaThruDay = new Text(composite_thru_rootca, SWT.BORDER);
		TextRootCaThruDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextRootCaThruDay);
			}
		});
		TextRootCaThruDay
				.setToolTipText(Messages.CrtVerViewComposite_rootCaThruDay);
		TextRootCaThruDay.setText("1");
		TextRootCaThruDay.setTextLimit(2);
		GridData gd_TextRootCaThruDay = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_TextRootCaThruDay.widthHint = 17;
		TextRootCaThruDay.setLayoutData(gd_TextRootCaThruDay);

		Label LabelRootCaThru = new Label(composite_thru_rootca, SWT.NONE);
		GridData gd_LabelRootCaThru = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelRootCaThru.widthHint = 60;
		LabelRootCaThru.setLayoutData(gd_LabelRootCaThru);
		thruRootCa = LabelRootCaThru;

		// Initialize Label "Thru Root CA" with actual date
		LabelRootCaThru.setText("");

		Composite composite_thru_ca = new Composite(grpDetails, SWT.NONE);
		composite_thru_ca.setLayout(new GridLayout(2, false));

		TextCaThruDay = new Text(composite_thru_ca, SWT.BORDER);
		TextCaThruDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextCaThruDay);
			}
		});

		TextCaThruDay.setToolTipText(Messages.CrtVerViewComposite_caThruDay);
		TextCaThruDay.setText("1");
		TextCaThruDay.setTextLimit(2);
		GridData gd_TextCaThruDay = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_TextCaThruDay.widthHint = 17;
		TextCaThruDay.setLayoutData(gd_TextCaThruDay);

		Label LabelCaThru = new Label(composite_thru_ca, SWT.NONE);
		GridData gd_LabelCaThru = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelCaThru.widthHint = 60;
		LabelCaThru.setLayoutData(gd_LabelCaThru);
		thruCa = LabelCaThru;

		// Initialize Label "Thru CA" with actual date
		LabelCaThru.setText("");

		Composite composite_thru_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_thru_user_cert.setLayout(new GridLayout(2, false));

		TextCertThruDay = new Text(composite_thru_user_cert, SWT.BORDER);
		TextCertThruDay.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				controller.inputcheck(TextCertThruDay);
			}
		});
		TextCertThruDay
				.setToolTipText(Messages.CrtVerViewComposite_userCertificateThruDay);
		TextCertThruDay.setText("1");
		TextCertThruDay.setTextLimit(2);
		GridData gd_TextCertThruDay = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_TextCertThruDay.widthHint = 17;
		TextCertThruDay.setLayoutData(gd_TextCertThruDay);

		Label LabelCertThru = new Label(composite_thru_user_cert, SWT.NONE);
		GridData gd_LabelCertThru = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_LabelCertThru.widthHint = 60;
		LabelCertThru.setLayoutData(gd_LabelCertThru);
		thruCert = LabelCertThru;

		// Initialize Label "Thru User Cert" with actual date
		LabelCertThru.setText("");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);

		Button btnReset = new Button(composite, SWT.NONE);
		GridData gd_btnReset = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_btnReset.widthHint = 100;
		gd_btnReset.heightHint = 30;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.setText(Messages.CrtVerViewComposite_reset);

		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.reset();
			}
		});
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button btnBack = new Button(composite, SWT.NONE);
		btnBack.setEnabled(false);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		GridData gd_btnBack = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1);
		gd_btnBack.widthHint = 100;
		gd_btnBack.heightHint = 30;
		btnBack.setLayoutData(gd_btnBack);
		btnBack.setText(Messages.CrtVerViewComposite_pki_plugin);

		Button btnForward = new Button(composite, SWT.NONE);
		btnForward.setEnabled(false);
		btnForward.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow()
							.getActivePage()
							.showView(
									"org.jcryptool.visual.sigVerification.view");
				} catch (PartInitException e1) {
					LogUtil.logError(Activator.PLUGIN_ID, e1);
				}
			}
		});
		GridData gd_btnForward = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnForward.widthHint = 182;
		gd_btnForward.heightHint = 30;
		btnForward.setLayoutData(gd_btnForward);
		btnForward.setText(Messages.CrtVerViewComposite_signatureVerification);
		new Label(composite, SWT.NONE);

		Composite composite_6 = new Composite(composite, SWT.NONE);
		composite_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		composite_6.setLayout(new GridLayout(3, false));
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));

		final Button btnShellModel = new Button(composite_6, SWT.RADIO);
		btnShellModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validitySymbol.hide();
				ScaleVerificationDate.setEnabled(true);
			}
		});
		btnShellModel.setSelection(true);
		btnShellModel.setText(Messages.CrtVerViewComposite_shellModel);

		final Button btnModifiedShellModel = new Button(composite_6, SWT.RADIO);
		btnModifiedShellModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validitySymbol.hide();
				ScaleVerificationDate.setEnabled(false);
			}
		});
		btnModifiedShellModel
				.setText(Messages.CrtVerViewComposite_modifiedshellModel);

		final Button btnChainModel = new Button(composite_6, SWT.RADIO);
		btnChainModel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validitySymbol.hide();
				ScaleVerificationDate.setEnabled(false);
			}
		});
		btnChainModel.setText(Messages.CrtVerViewComposite_chainModel);

		Button btnCalculate = new Button(composite, SWT.NONE);
		btnValidate = btnCalculate;
		GridData gd_btnCalculate = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnCalculate.widthHint = 200;
		gd_btnCalculate.heightHint = 30;
		btnCalculate.setLayoutData(gd_btnCalculate);
		btnCalculate.setText(Messages.CrtVerViewComposite_validate);

		validitySymbol = new ControlDecoration(btnCalculate, SWT.LEFT | SWT.TOP);
		validitySymbol.hide();
	

		btnCalculate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.setLogText("### "
						+ String.format("%03d", validationCounter) + " ###");
				validationCounter++;

				controller.logValidityDates();

				if (btnShellModel.getSelection()) {
					controller.validate(0);
				} else if (btnModifiedShellModel.getSelection()) {
					controller.validate(1);
				} else if (btnChainModel.getSelection()) {
					controller.validate(2);
				}
				controller.setLogText("---------------------------------------------------");
			}
		});

		// Selection Listeners | Scales
		ScaleRootCaBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(fromRootCa, ScaleRootCaBegin, 180);
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleRootCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(thruRootCa, ScaleRootCaEnd, 180);
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleCaBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(fromCa, ScaleCaBegin, 180);
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(thruCa, ScaleCaEnd, 180);
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleCertBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(fromCert, ScaleCertBegin, 180);
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleCertEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(thruCert, ScaleCertEnd, 180);
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleSignatureDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(signatureDate, ScaleSignatureDate,
						360);
				if (((ScaleSignatureDate.getSelection() - 360) % 2) == 0) {
					arrowSigDiff = (ScaleSignatureDate.getSelection() - 360) / 2;
				} else {
					arrowSigDiff = ((ScaleSignatureDate.getSelection() + 1) - 360) / 2;
				}
				// arrowSigDiff = ScaleSignatureDate.getSelection()-360;
				canvas1.redraw();
				canvas2.redraw();
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		ScaleVerificationDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Add or Remain Time dependent on selection
				controller.updateElements(LabelVerificationDate,
						ScaleVerificationDate, 360);
				if (((ScaleVerificationDate.getSelection() - 360) % 2) == 0) {
					arrowVerDiff = (ScaleVerificationDate.getSelection() - 360) / 2;
				} else {
					arrowVerDiff = ((ScaleVerificationDate.getSelection() + 1) - 360) / 2;
				}
				// arrowVerDiff = ScaleVerificationDate.getSelection()-360;
				canvas1.redraw();
				canvas2.redraw();
				// Hide Validity Symbols (red/green)
				validitySymbol.hide();
				setLoadBtnsOrange();
			}
		});

		controller.reset();
	}

	/**
	 * Sets the symbols for a successful or unsuccessful validation. The symbols
	 * are a red cross or a green checkmark.
	 * 
	 * @param type
	 *            int: [1] valid [2] invalid
	 */
	public static void setValidtiySymbol(int type) {
		if (type == 1) {
			validitySymbol.setImage(ResourceManager.getPluginImage(
					"org.jcryptool.visual.crtVerification",
					"icons/gruenerHakenKlein.png"));
			validitySymbol
					.setDescriptionText(Messages.CrtVerViewComposite_validateSuccessful);
			validitySymbol.show();
		} else {
			validitySymbol.setImage(ResourceManager.getPluginImage(
					"org.jcryptool.visual.crtVerification",
					"icons/rotesKreuzKlein.png"));
			validitySymbol
					.setDescriptionText(Messages.CrtVerViewComposite_validateUnSuccessful);
			validitySymbol.show();
		}
	}

	/**
	 * This method paints the arrows used to indicate the validate date.
	 * 
	 * @param e
	 */
	public void paintControl(PaintEvent e) {
		// Set the used color
		Color lightblue = new Color(Display.getCurrent(), 30, 144, 255);
		Color darkblue = new Color(Display.getCurrent(), 72, 61, 139);
		Rectangle clientArea;
		int width;
		int height;
		// Coordinates of the document icon
		GC gc;

		gc = e.gc;

		// Max position right are left are +/-180
		if (arrowSigDiff < -180) {
			arrowSigDiff = -180;
		} else if (arrowSigDiff > 180) {
			arrowSigDiff = 180;
		}
		if (arrowVerDiff < -180) {
			arrowVerDiff = -180;
		} else if (arrowVerDiff > 180) {
			arrowVerDiff = 178;
		}

		// Get the size of the canvas area
		clientArea = canvas1.getClientArea();
		width = clientArea.width;
		height = clientArea.height;

		// Draw Arrow Signature Date
		gc.setBackground(lightblue);
		gc.fillRectangle(width / 2 + arrowSigDiff - 4, 9, 8, height);
		gc.fillPolygon(new int[] { (width / 2 - 8 + arrowSigDiff), 9,
				(width / 2 + arrowSigDiff), 0, (width / 2 + 8 + arrowSigDiff),
				9 });

		// Draw Arrow Verification Date
		gc.setBackground(darkblue);
		gc.fillRectangle(width / 2 + arrowVerDiff - 4, 9, 8, height - 4);
		gc.fillPolygon(new int[] { (width / 2 - 8 + arrowVerDiff), 11,
				(width / 2 + arrowVerDiff), 2, (width / 2 + 8 + arrowVerDiff),
				11 });

		gc.dispose();

	}

	/**
	 * Sets the font-color of the buttons btnLoadRootCa, btnLoadCa and
	 * btnLoadUserCert to orange. This happens when the scales are modified.
	 */
	public void setLoadBtnsOrange() {
		CrtVerViewComposite.btnLoadRootCa.setForeground(SWTResourceManager
				.getColor(255, 140, 0));
		CrtVerViewComposite.btnLoadCa.setForeground(SWTResourceManager
				.getColor(255, 140, 0));
		CrtVerViewComposite.btnLoadUserCert.setForeground(SWTResourceManager
				.getColor(255, 140, 0));
	}

	/**
	 * Sets the text of the description at the top of the GUI.
	 */
	public void setText() {
		txtDescription.setText(Messages.CrtVerViewComposite_description);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
