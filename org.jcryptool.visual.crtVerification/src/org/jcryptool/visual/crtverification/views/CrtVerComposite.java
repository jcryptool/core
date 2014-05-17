package org.jcryptool.visual.crtverification.views;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.crtverification.Activator;

public class CrtVerComposite extends Composite {
	private Text TextRootCaFromDay;
	private Text TextCaFromDay;
	private Text TextCertFromDay;
	private Text TextRootCaThruDay;
	private Text TextCaThruDay;
	private Text TextCertThruDay;
	private Text TextSignatureDateDay;

	// Date now Instance
	private Label thruRootCa;
	private Label fromRootCa;
	private Label thruCa;
	private Label fromCa;
	private Label thruCert;
	private Label fromCert;
	private Label signatureDate;
	CrtVerViewController controller = new CrtVerViewController();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CrtVerComposite(Composite parent, int style, CrtVerView view) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		
		TabItem tbtmSchalenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setText("Schalenmodell");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setControl(composite);
		composite.setLayout(new GridLayout(10, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNotValidBefore = new Label(composite, SWT.NONE);
		lblNotValidBefore.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 7, 1));
		lblNotValidBefore.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblNotValidBefore.setAlignment(SWT.CENTER);
		lblNotValidBefore.setText("Not Valid Before");
		
		Label lblNotValidAfter = new Label(composite, SWT.NONE);
		lblNotValidAfter.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNotValidAfter.setText("Not Valid After");
		lblNotValidAfter.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblNotValidAfter.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		final Scale ScaleRootCaBegin = new Scale(composite, SWT.NONE);
		
		ScaleRootCaBegin.setMaximum(240);
		GridData gd_ScaleRootCaBegin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_ScaleRootCaBegin.widthHint = 240;
		ScaleRootCaBegin.setLayoutData(gd_ScaleRootCaBegin);
		ScaleRootCaBegin.setSelection(120);
		
		final Scale ScaleRootCaEnd = new Scale(composite, SWT.NONE);
		GridData gd_ScaleRootCaEnd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleRootCaEnd.widthHint = 240;
		ScaleRootCaEnd.setLayoutData(gd_ScaleRootCaEnd);
		ScaleRootCaEnd.setMaximum(240);
		ScaleRootCaEnd.setSelection(120);
		
		Button btnLoadRootCa = new Button(composite, SWT.NONE);
		GridData gd_btnLoadRootCa = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadRootCa.widthHint = 100;
		btnLoadRootCa.setLayoutData(gd_btnLoadRootCa);
		btnLoadRootCa.setText("Load Root CA");
		
		Label lblRootCa = new Label(composite, SWT.NONE);
		lblRootCa.setText("Root CA");
		lblRootCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblRootCa.setAlignment(SWT.CENTER);
		
		final Scale ScaleCaBegin = new Scale(composite, SWT.NONE);
		ScaleCaBegin.setMaximum(240);
		GridData gd_ScaleCaBegin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_ScaleCaBegin.widthHint = 240;
		ScaleCaBegin.setLayoutData(gd_ScaleCaBegin);
		ScaleCaBegin.setSelection(120);
		
		final Scale ScaleCaEnd = new Scale(composite, SWT.NONE);
		ScaleCaEnd.setMaximum(240);
		GridData gd_ScaleCaEnd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleCaEnd.widthHint = 240;
		ScaleCaEnd.setLayoutData(gd_ScaleCaEnd);
		ScaleCaEnd.setSelection(120);
		
		Button btnLoadCa = new Button(composite, SWT.NONE);
		GridData gd_btnLoadCa = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadCa.widthHint = 100;
		btnLoadCa.setLayoutData(gd_btnLoadCa);
		btnLoadCa.setText("Load CA");
		
		Label lblCa = new Label(composite, SWT.NONE);
		lblCa.setText("CA");
		lblCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblCa.setAlignment(SWT.CENTER);
		
		final Scale ScaleCertBegin = new Scale(composite, SWT.NONE);
		ScaleCertBegin.setMaximum(240);
		GridData gd_ScaleCertBegin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1);
		gd_ScaleCertBegin.widthHint = 240;
		ScaleCertBegin.setLayoutData(gd_ScaleCertBegin);
		ScaleCertBegin.setSelection(120);
		
		final Scale ScaleCertEnd = new Scale(composite, SWT.NONE);
		ScaleCertEnd.setMaximum(240);
		GridData gd_ScaleCertEnd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleCertEnd.widthHint = 240;
		ScaleCertEnd.setLayoutData(gd_ScaleCertEnd);
		ScaleCertEnd.setSelection(120);
		
		Button btnLoadUserCert = new Button(composite, SWT.NONE);
		GridData gd_btnLoadUserCert = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadUserCert.widthHint = 100;
		btnLoadUserCert.setLayoutData(gd_btnLoadUserCert);
		btnLoadUserCert.setText("Load User Cert");
		
		Label lblUserCertificate = new Label(composite, SWT.NONE);
		lblUserCertificate.setText("User Certificate");
		lblUserCertificate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblUserCertificate.setAlignment(SWT.CENTER);
		
		Label SeperatorHorizontal = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_SeperatorHorizontal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 10, 1);
		gd_SeperatorHorizontal.widthHint = 689;
		SeperatorHorizontal.setLayoutData(gd_SeperatorHorizontal);
		
		final Scale ScaleSignatureDate = new Scale(composite, SWT.NONE);
		ScaleSignatureDate.setMaximum(480);
		GridData gd_ScaleSignatureDate = new GridData(SWT.FILL, SWT.FILL, false, false, 8, 1);
		gd_ScaleSignatureDate.widthHint = 480;
		ScaleSignatureDate.setLayoutData(gd_ScaleSignatureDate);
		ScaleSignatureDate.setSelection(240);
		new Label(composite, SWT.NONE);
		
		Label lblSignatureDate = new Label(composite, SWT.NONE);
		lblSignatureDate.setText("Signature Date");
		lblSignatureDate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblSignatureDate.setAlignment(SWT.CENTER);
		
		Group grpDetails = new Group(composite, SWT.NONE);
		GridData gd_grpDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 10, 1);
		gd_grpDetails.widthHint = 608;
		grpDetails.setLayoutData(gd_grpDetails);
		grpDetails.setText("Details");
		grpDetails.setLayout(new GridLayout(9, false));
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Label LabelHeaderRootCa = new Label(grpDetails, SWT.NONE);
		LabelHeaderRootCa.setAlignment(SWT.CENTER);
		GridData gd_LabelHeaderRootCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelHeaderRootCa.widthHint = 100;
		LabelHeaderRootCa.setLayoutData(gd_LabelHeaderRootCa);
		LabelHeaderRootCa.setText("Root CA");
		
		Label LabelHeaderCa = new Label(grpDetails, SWT.NONE);
		LabelHeaderCa.setAlignment(SWT.CENTER);
		GridData gd_LabelHeaderCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelHeaderCa.widthHint = 100;
		LabelHeaderCa.setLayoutData(gd_LabelHeaderCa);
		LabelHeaderCa.setText("CA");
		
		Label LabelHeaderCert = new Label(grpDetails, SWT.NONE);
		LabelHeaderCert.setAlignment(SWT.CENTER);
		GridData gd_LabelHeaderCert = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelHeaderCert.widthHint = 100;
		LabelHeaderCert.setLayoutData(gd_LabelHeaderCert);
		LabelHeaderCert.setText("User Certificate");
		new Label(grpDetails, SWT.NONE);
		
		Label SeperatorDetailsVertical = new Label(grpDetails, SWT.SEPARATOR | SWT.VERTICAL);
		SeperatorDetailsVertical.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3));
		new Label(grpDetails, SWT.NONE);
		
		Label LabelHeaderSignatureDate = new Label(grpDetails, SWT.NONE);
		LabelHeaderSignatureDate.setText("Signature Date");
		Label lblValidFrom = new Label(grpDetails, SWT.NONE);
		lblValidFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValidFrom.setText("valid from:");
		new Label(grpDetails, SWT.NONE);
		
		Composite composite_from_rootca = new Composite(grpDetails, SWT.NONE);
		composite_from_rootca.setLayout(new GridLayout(2, false));
		
		TextRootCaFromDay = new Text(composite_from_rootca, SWT.BORDER);
		TextRootCaFromDay.setText("1");
		GridData gd_TextRootCaFromDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextRootCaFromDay.widthHint = 20;
		TextRootCaFromDay.setLayoutData(gd_TextRootCaFromDay);
		TextRootCaFromDay.setSize(24, 19);
		
		Label LabelRootCaFrom = new Label(composite_from_rootca, SWT.NONE);
		fromRootCa = LabelRootCaFrom;
		
		// Initialize Label "From Root CA" with actual date
		LabelRootCaFrom.setText(controller.now());
		
		Composite composite_from_ca = new Composite(grpDetails, SWT.NONE);
		composite_from_ca.setLayout(new GridLayout(2, false));
		
		TextCaFromDay = new Text(composite_from_ca, SWT.BORDER);
		TextCaFromDay.setText("1");
		GridData gd_TextCaFromDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCaFromDay.widthHint = 20;
		TextCaFromDay.setLayoutData(gd_TextCaFromDay);
		
		Label LabelCaFrom = new Label(composite_from_ca, SWT.NONE);
		fromCa = LabelCaFrom;
		
		// Initialize Label "From CA" with actual date
		LabelCaFrom.setText(controller.now());
		
		Composite composite_from_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_from_user_cert.setLayout(new GridLayout(2, false));
		
		TextCertFromDay = new Text(composite_from_user_cert, SWT.BORDER);
		TextCertFromDay.setText("1");
		GridData gd_TextCertFromDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCertFromDay.widthHint = 20;
		TextCertFromDay.setLayoutData(gd_TextCertFromDay);
		
		Label LabelCertFrom = new Label(composite_from_user_cert, SWT.NONE);
		fromCert = LabelCertFrom;
		
		// Initialize Label "From User Cert" with actual date
		LabelCertFrom.setText(controller.now());
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Composite composite_1 = new Composite(grpDetails, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		
		TextSignatureDateDay = new Text(composite_1, SWT.BORDER);
		TextSignatureDateDay.setText("1");
		GridData gd_TextSignatureDateDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextSignatureDateDay.widthHint = 20;
		TextSignatureDateDay.setLayoutData(gd_TextSignatureDateDay);
		
		Label LabelSignatureDate = new Label(composite_1, SWT.NONE);
		signatureDate = LabelSignatureDate;
		
		// Initialize Label "Signature Date" with actual date
		LabelSignatureDate.setText(controller.now());
		
		Label lblValidThru = new Label(grpDetails, SWT.NONE);
		lblValidThru.setText("valid thru:");
		new Label(grpDetails, SWT.NONE);
		
		Composite composite_thru_rootca = new Composite(grpDetails, SWT.NONE);
		composite_thru_rootca.setLayout(new GridLayout(2, false));
		
		TextRootCaThruDay = new Text(composite_thru_rootca, SWT.BORDER);
		TextRootCaThruDay.setText("1");
		GridData gd_TextRootCaThruDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextRootCaThruDay.widthHint = 20;
		TextRootCaThruDay.setLayoutData(gd_TextRootCaThruDay);
		
		Label LabelRootCaThru = new Label(composite_thru_rootca, SWT.NONE);
		thruRootCa = LabelRootCaThru;
		
		// Initialize Label "Thru Root CA" with actual date
		LabelRootCaThru.setText(controller.now());
		
		Composite composite_thru_ca = new Composite(grpDetails, SWT.NONE);
		composite_thru_ca.setLayout(new GridLayout(2, false));
		
		TextCaThruDay = new Text(composite_thru_ca, SWT.BORDER);
		TextCaThruDay.setText("1");
		GridData gd_TextCaThruDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCaThruDay.widthHint = 20;
		TextCaThruDay.setLayoutData(gd_TextCaThruDay);
		
		Label LabelCaThru = new Label(composite_thru_ca, SWT.NONE);
		thruCa = LabelCaThru;
		
		// Initialize Label "Thru CA" with actual date
		LabelCaThru.setText(controller.now());
		
		Composite composite_thru_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_thru_user_cert.setLayout(new GridLayout(2, false));
		
		TextCertThruDay = new Text(composite_thru_user_cert, SWT.BORDER);
		TextCertThruDay.setText("1");
		GridData gd_TextCertThruDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCertThruDay.widthHint = 20;
		TextCertThruDay.setLayoutData(gd_TextCertThruDay);
		
		Label LabelCertThru = new Label(composite_thru_user_cert, SWT.NONE);
		thruCert = LabelCertThru;
		
		// Initialize Label "Thru User Cert" with actual date
		LabelCertThru.setText(controller.now());
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Button btnReset = new Button(composite, SWT.NONE);
		btnReset.setText("Reset");
		// Selection Listeners | Scales
        btnReset.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    TestWizard wiz = new TestWizard();
                    WizardDialog dialog = new WizardDialog(new Shell(Display.getCurrent()), wiz){
                        @Override
                        protected void configureShell(Shell newShell) {
                            super.configureShell(newShell);
                            // set size of the wizard-window (x,y)
                            newShell.setSize(550, 500);
                        }
                };
                if (dialog.open() == Window.OK) {
                   // Hier kann man Aktionen durfuehren die passieren sollen wenn die WizardPage aufgerufen wird
                   // zB aktivieren/deaktivieren von Buttons der Hauptansicht
                }
                } catch (Exception ex) {
                    LogUtil.logError(Activator.PLUGIN_ID, ex);
                }
            }
        });
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnBack = new Button(composite, SWT.NONE);
		btnBack.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnBack.setText("Back");
		
		Button btnForward = new Button(composite, SWT.NONE);
		btnForward.setText("Forward");
		new Label(composite, SWT.NONE);
		
		Button btnCalculate = new Button(composite, SWT.NONE);
		btnCalculate.setText("Calculate");
		
		TabItem tbtmKettenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmKettenmodell.setText("Kettenmodell");
		
		
		// Selection Listeners | Scales
		ScaleRootCaBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromRootCa.setText(controller.scaleUpdate(ScaleRootCaBegin.getSelection(), 120));
			}
		});
		
		ScaleRootCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruRootCa.setText(controller.scaleUpdate(ScaleRootCaEnd.getSelection(), 120));
			}
		});
		
		ScaleCaBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromCa.setText(controller.scaleUpdate(ScaleCaBegin.getSelection(), 120));
			}
		});
		
		ScaleCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruCa.setText(controller.scaleUpdate(ScaleCaEnd.getSelection(), 120));
			}
		});
		
		ScaleCertBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromCert.setText(controller.scaleUpdate(ScaleCertBegin.getSelection(), 120));
			}
		});
		
		ScaleCertEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruCert.setText(controller.scaleUpdate(ScaleCertEnd.getSelection(), 120));
			}
		});
		
		ScaleSignatureDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				signatureDate.setText(controller.scaleUpdate(ScaleSignatureDate.getSelection(), 240));
			}
		});
		
	}

	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
