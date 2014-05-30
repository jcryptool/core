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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class CrtVerViewComposite extends Composite {
	private Text TextRootCaFromDay;
	private Text TextCaFromDay;
	private Text TextCertFromDay;
	private Text TextRootCaThruDay;
	private Text TextCaThruDay;
	private Text TextCertThruDay;
	private Text TextSignatureDateDay;
	private Text TextVerificationDateDay;

	// Date now Instance
	private Label thruRootCa;
	private Label fromRootCa;
	private Label thruCa;
	private Label fromCa;
	private Label thruCert;
	private Label fromCert;
	private Label signatureDate;
	private Label verificationDate;
	private Label validity;
	CrtVerViewController controller = new CrtVerViewController();

	private Text txtDiesIstDer;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CrtVerViewComposite(Composite parent, int style, CrtVerView view) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		
		TabItem tbtmSchalenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setText("Schalenmodell");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setControl(composite);
		composite.setLayout(new GridLayout(17, false));
		
		txtDiesIstDer = new Text(composite, SWT.BORDER);
		txtDiesIstDer.setEnabled(false);
		txtDiesIstDer.setEditable(false);
		txtDiesIstDer.setText("Dieses Tool dient zur Visualisierung der G\u00FCltigkeiten von Zertifikatsb\u00E4umen");
		GridData gd_txtDiesIstDer = new GridData(SWT.FILL, SWT.CENTER, true, false, 15, 1);
		gd_txtDiesIstDer.heightHint = 70;
		txtDiesIstDer.setLayoutData(gd_txtDiesIstDer);
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
		lblNotValidBefore.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 12, 1));
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
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1));
		
		Label label = new Label(composite_4, SWT.NONE);
		label.setText(controller.scaleUpdate(0, 180, true));
		label.setBounds(0, 0, 59, 14);
		
		Label label_1 = new Label(composite_4, SWT.NONE);
		label_1.setText(controller.scaleUpdate(360, 180, true));
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(301, 0, 59, 14);
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_3.widthHint = 360;
		composite_3.setLayoutData(gd_composite_3);
		
		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setBounds(0, 0, 59, 14);
		label_2.setText(controller.scaleUpdate(0, 180, true));
		
		Label label_3 = new Label(composite_3, SWT.NONE);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(301, 0, 59, 14);
		label_3.setText(controller.scaleUpdate(360, 180, true));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblRootCa = new Label(composite, SWT.NONE);
		GridData gd_lblRootCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblRootCa.heightHint = 30;
		gd_lblRootCa.verticalIndent = 10;
		lblRootCa.setLayoutData(gd_lblRootCa);
		lblRootCa.setText("Root CA");
		lblRootCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblRootCa.setAlignment(SWT.CENTER);
		
		final Scale ScaleRootCaBegin = new Scale(composite, SWT.NONE);
		ScaleRootCaBegin.setToolTipText("");
		
		ScaleRootCaBegin.setMaximum(360);
		GridData gd_ScaleRootCaBegin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1);
		gd_ScaleRootCaBegin.widthHint = 360;
		ScaleRootCaBegin.setLayoutData(gd_ScaleRootCaBegin);
		ScaleRootCaBegin.setSelection(180);
		
		final Scale ScaleRootCaEnd = new Scale(composite, SWT.NONE);
		GridData gd_ScaleRootCaEnd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleRootCaEnd.widthHint = 360;
		ScaleRootCaEnd.setLayoutData(gd_ScaleRootCaEnd);
		ScaleRootCaEnd.setMaximum(360);
		ScaleRootCaEnd.setSelection(180);
		
		Button btnLoadRootCa = new Button(composite, SWT.NONE);
		GridData gd_btnLoadRootCa = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadRootCa.heightHint = 30;
		gd_btnLoadRootCa.widthHint = 100;
		btnLoadRootCa.setLayoutData(gd_btnLoadRootCa);
		btnLoadRootCa.setText("Load Root CA");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblCa = new Label(composite, SWT.NONE);
		GridData gd_lblCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblCa.verticalIndent = 10;
		gd_lblCa.heightHint = 30;
		lblCa.setLayoutData(gd_lblCa);
		lblCa.setText("CA");
		lblCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblCa.setAlignment(SWT.CENTER);
		
		final Scale ScaleCaBegin = new Scale(composite, SWT.NONE);
		ScaleCaBegin.setMaximum(360);
		GridData gd_ScaleCaBegin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1);
		gd_ScaleCaBegin.widthHint = 360;
		ScaleCaBegin.setLayoutData(gd_ScaleCaBegin);
		ScaleCaBegin.setSelection(180);
		
		final Scale ScaleCaEnd = new Scale(composite, SWT.NONE);
		ScaleCaEnd.setMaximum(360);
		GridData gd_ScaleCaEnd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleCaEnd.widthHint = 360;
		ScaleCaEnd.setLayoutData(gd_ScaleCaEnd);
		ScaleCaEnd.setSelection(180);
		
		Button btnLoadCa = new Button(composite, SWT.NONE);
		GridData gd_btnLoadCa = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadCa.heightHint = 30;
		gd_btnLoadCa.widthHint = 100;
		btnLoadCa.setLayoutData(gd_btnLoadCa);
		btnLoadCa.setText("Load CA");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblUserCertificate = new Label(composite, SWT.NONE);
		GridData gd_lblUserCertificate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblUserCertificate.verticalIndent = 10;
		gd_lblUserCertificate.heightHint = 30;
		lblUserCertificate.setLayoutData(gd_lblUserCertificate);
		lblUserCertificate.setText("User Certificate");
		lblUserCertificate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblUserCertificate.setAlignment(SWT.CENTER);
		
		final Scale ScaleCertBegin = new Scale(composite, SWT.NONE);
		ScaleCertBegin.setMaximum(360);
		GridData gd_ScaleCertBegin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1);
		gd_ScaleCertBegin.widthHint = 360;
		ScaleCertBegin.setLayoutData(gd_ScaleCertBegin);
		ScaleCertBegin.setSelection(180);
		
		final Scale ScaleCertEnd = new Scale(composite, SWT.NONE);
		ScaleCertEnd.setMaximum(360);
		GridData gd_ScaleCertEnd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleCertEnd.widthHint = 360;
		ScaleCertEnd.setLayoutData(gd_ScaleCertEnd);
		ScaleCertEnd.setSelection(180);
		Button btnLoadUserCert = new Button(composite, SWT.NONE);
		// Selection Listeners | Scales
        btnLoadUserCert.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    ChooseCert wiz = new ChooseCert();
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
        
		GridData gd_btnLoadUserCert = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadUserCert.heightHint = 30;
		gd_btnLoadUserCert.widthHint = 100;
		btnLoadUserCert.setLayoutData(gd_btnLoadUserCert);
		btnLoadUserCert.setText("Load User Cert");
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
		
		Label SeperatorHorizontal = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_SeperatorHorizontal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 14, 1);
		gd_SeperatorHorizontal.widthHint = 835;
		SeperatorHorizontal.setLayoutData(gd_SeperatorHorizontal);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_5 = new Composite(composite, SWT.NONE);
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.CENTER, false, false, 13, 1);
		gd_composite_5.widthHint = 720;
		composite_5.setLayoutData(gd_composite_5);
		
		Label label_4 = new Label(composite_5, SWT.NONE);
		label_4.setText(controller.scaleUpdate(0, 360, true));
		label_4.setBounds(0, 0, 59, 14);
		
		Label label_5 = new Label(composite_5, SWT.NONE);
		label_5.setText(controller.scaleUpdate(720, 360, true));
		label_5.setAlignment(SWT.RIGHT);
		label_5.setBounds(666, 0, 59, 14);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblSignatureDate = new Label(composite, SWT.NONE);
		GridData gd_lblSignatureDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSignatureDate.verticalIndent = 10;
		gd_lblSignatureDate.heightHint = 30;
		lblSignatureDate.setLayoutData(gd_lblSignatureDate);
		lblSignatureDate.setText("Signature Date");
		lblSignatureDate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblSignatureDate.setAlignment(SWT.CENTER);
		
		final Scale ScaleSignatureDate = new Scale(composite, SWT.NONE);
		ScaleSignatureDate.setMaximum(720);
		GridData gd_ScaleSignatureDate = new GridData(SWT.FILL, SWT.FILL, false, false, 13, 1);
		gd_ScaleSignatureDate.widthHint = 480;
		ScaleSignatureDate.setLayoutData(gd_ScaleSignatureDate);
		ScaleSignatureDate.setSelection(360);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblVerificationDate = new Label(composite, SWT.NONE);
		GridData gd_lblVerificationDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblVerificationDate.verticalIndent = 10;
		gd_lblVerificationDate.heightHint = 30;
		lblVerificationDate.setLayoutData(gd_lblVerificationDate);
		
		lblVerificationDate.setText("Verification Date");
		lblVerificationDate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblVerificationDate.setAlignment(SWT.CENTER);
		
		final Scale ScaleVerificationDate = new Scale(composite, SWT.NONE);
		GridData gd_ScaleVerificationDate = new GridData(SWT.FILL, SWT.FILL, false, false, 13, 1);
		gd_ScaleVerificationDate.widthHint = 480;
		ScaleVerificationDate.setLayoutData(gd_ScaleVerificationDate);
		ScaleVerificationDate.setMaximum(720);
		ScaleVerificationDate.setSelection(360);
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
		new Label(composite, SWT.NONE);
		
		Group grpDetails = new Group(composite, SWT.NONE);
		GridData gd_grpDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 16, 1);
		gd_grpDetails.widthHint = 717;
		grpDetails.setLayoutData(gd_grpDetails);
		grpDetails.setText("Details");
		grpDetails.setLayout(new GridLayout(10, false));
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
		
		Label LabelHeaderVerificationDate = new Label(grpDetails, SWT.NONE);
		LabelHeaderVerificationDate.setText("Verification Date");
		Label lblValidFrom = new Label(grpDetails, SWT.NONE);
		lblValidFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValidFrom.setText("valid from:");
		new Label(grpDetails, SWT.NONE);
		
		Composite composite_from_rootca = new Composite(grpDetails, SWT.NONE);
		composite_from_rootca.setLayout(new GridLayout(2, false));
		
		TextRootCaFromDay = new Text(composite_from_rootca, SWT.BORDER);
		TextRootCaFromDay.setText("1");
		TextRootCaFromDay.setTextLimit(2);
		GridData gd_TextRootCaFromDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextRootCaFromDay.widthHint = 17;
		TextRootCaFromDay.setLayoutData(gd_TextRootCaFromDay);
		TextRootCaFromDay.setSize(24, 19);
		
		Label LabelRootCaFrom = new Label(composite_from_rootca, SWT.NONE);
		GridData gd_LabelRootCaFrom = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelRootCaFrom.widthHint = 60;
		LabelRootCaFrom.setLayoutData(gd_LabelRootCaFrom);
		fromRootCa = LabelRootCaFrom;
		
		// Initialize Label "From Root CA" with actual date
		LabelRootCaFrom.setText("/May/14");
		
		Composite composite_from_ca = new Composite(grpDetails, SWT.NONE);
		composite_from_ca.setLayout(new GridLayout(2, false));
		
		TextCaFromDay = new Text(composite_from_ca, SWT.BORDER);
		TextCaFromDay.setText("1");
		TextCaFromDay.setTextLimit(2);
		GridData gd_TextCaFromDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCaFromDay.widthHint = 17;
		TextCaFromDay.setLayoutData(gd_TextCaFromDay);
		
		Label LabelCaFrom = new Label(composite_from_ca, SWT.NONE);
		GridData gd_LabelCaFrom = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelCaFrom.widthHint = 60;
		LabelCaFrom.setLayoutData(gd_LabelCaFrom);
		fromCa = LabelCaFrom;
		
		// Initialize Label "From CA" with actual date
		LabelCaFrom.setText("/May/14");
		
		Composite composite_from_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_from_user_cert.setLayout(new GridLayout(2, false));
		
		TextCertFromDay = new Text(composite_from_user_cert, SWT.BORDER);
		TextCertFromDay.setText("1");
		TextCertFromDay.setTextLimit(2);
		GridData gd_TextCertFromDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCertFromDay.widthHint = 17;
		TextCertFromDay.setLayoutData(gd_TextCertFromDay);
		
		Label LabelCertFrom = new Label(composite_from_user_cert, SWT.NONE);
		GridData gd_LabelCertFrom = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelCertFrom.widthHint = 60;
		LabelCertFrom.setLayoutData(gd_LabelCertFrom);
		fromCert = LabelCertFrom;
		
		// Initialize Label "From User Cert" with actual date
		LabelCertFrom.setText("/May/14");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Composite composite_1 = new Composite(grpDetails, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		
		TextSignatureDateDay = new Text(composite_1, SWT.BORDER);
		TextSignatureDateDay.setText("1");
		TextSignatureDateDay.setTextLimit(2);
		GridData gd_TextSignatureDateDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextSignatureDateDay.widthHint = 17;
		TextSignatureDateDay.setLayoutData(gd_TextSignatureDateDay);
		
		Label LabelSignatureDate = new Label(composite_1, SWT.NONE);
		GridData gd_LabelSignatureDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelSignatureDate.widthHint = 60;
		LabelSignatureDate.setLayoutData(gd_LabelSignatureDate);
		signatureDate = LabelSignatureDate;
		
		// Initialize Label "Signature Date" with actual date
		LabelSignatureDate.setText("/May/14");
		
		Composite composite_2 = new Composite(grpDetails, SWT.NONE);
		composite_2.setLayout(new GridLayout(2, false));
		
		TextVerificationDateDay = new Text(composite_2, SWT.BORDER);
		TextVerificationDateDay.setText("1");
		TextVerificationDateDay.setTextLimit(2);
		GridData gd_TextVerificationDateDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextVerificationDateDay.widthHint = 17;
		TextVerificationDateDay.setLayoutData(gd_TextVerificationDateDay);
		
		final Label LabelVerificationDate = new Label(composite_2, SWT.NONE);
		GridData gd_LabelVerificationDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelVerificationDate.widthHint = 60;
		LabelVerificationDate.setLayoutData(gd_LabelVerificationDate);
		verificationDate = LabelVerificationDate;
		LabelVerificationDate.setText("/May/14");
		
		Label lblValidThru = new Label(grpDetails, SWT.NONE);
		lblValidThru.setText("valid thru:");
		new Label(grpDetails, SWT.NONE);
		
		Composite composite_thru_rootca = new Composite(grpDetails, SWT.NONE);
		composite_thru_rootca.setLayout(new GridLayout(2, false));
		
		TextRootCaThruDay = new Text(composite_thru_rootca, SWT.BORDER);
		TextRootCaThruDay.setText("1");
		TextRootCaThruDay.setTextLimit(2);
		GridData gd_TextRootCaThruDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextRootCaThruDay.widthHint = 17;
		TextRootCaThruDay.setLayoutData(gd_TextRootCaThruDay);
		
		Label LabelRootCaThru = new Label(composite_thru_rootca, SWT.NONE);
		GridData gd_LabelRootCaThru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelRootCaThru.widthHint = 60;
		LabelRootCaThru.setLayoutData(gd_LabelRootCaThru);
		thruRootCa = LabelRootCaThru;
		
		// Initialize Label "Thru Root CA" with actual date
		LabelRootCaThru.setText("/May/14");
		
		Composite composite_thru_ca = new Composite(grpDetails, SWT.NONE);
		composite_thru_ca.setLayout(new GridLayout(2, false));
		
		TextCaThruDay = new Text(composite_thru_ca, SWT.BORDER);
		TextCaThruDay.setText("1");
		TextCaThruDay.setTextLimit(2);
		GridData gd_TextCaThruDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCaThruDay.widthHint = 17;
		TextCaThruDay.setLayoutData(gd_TextCaThruDay);
		
		Label LabelCaThru = new Label(composite_thru_ca, SWT.NONE);
		GridData gd_LabelCaThru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelCaThru.widthHint = 60;
		LabelCaThru.setLayoutData(gd_LabelCaThru);
		thruCa = LabelCaThru;
		
		// Initialize Label "Thru CA" with actual date
		LabelCaThru.setText("/May/14");
		
		Composite composite_thru_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_thru_user_cert.setLayout(new GridLayout(2, false));
		
		TextCertThruDay = new Text(composite_thru_user_cert, SWT.BORDER);
		TextCertThruDay.setText("1");
		TextCertThruDay.setTextLimit(2);
		GridData gd_TextCertThruDay = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_TextCertThruDay.widthHint = 17;
		TextCertThruDay.setLayoutData(gd_TextCertThruDay);
		
		Label LabelCertThru = new Label(composite_thru_user_cert, SWT.NONE);
		GridData gd_LabelCertThru = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_LabelCertThru.widthHint = 60;
		LabelCertThru.setLayoutData(gd_LabelCertThru);
		thruCert = LabelCertThru;
		
		// Initialize Label "Thru User Cert" with actual date
		LabelCertThru.setText("/May/14");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblvalidity = new Label(composite, SWT.NONE);
		validity = lblvalidity;
		lblvalidity.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		lblvalidity.setAlignment(SWT.CENTER);
		lblvalidity.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		GridData gd_lblvalidity = new GridData(SWT.CENTER, SWT.CENTER, false, false, 13, 1);
		gd_lblvalidity.heightHint = 25;
		gd_lblvalidity.widthHint = 400;
		lblvalidity.setLayoutData(gd_lblvalidity);
		lblvalidity.setText("NOT VALID");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Button btnReset = new Button(composite, SWT.NONE);
		GridData gd_btnReset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 10, 1);
		gd_btnReset.widthHint = 100;
		gd_btnReset.heightHint = 30;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ScaleRootCaBegin.setSelection(180);
				ScaleRootCaEnd.setSelection(180);
				ScaleCaBegin.setSelection(180);
				ScaleCaEnd.setSelection(180);
				ScaleCertBegin.setSelection(180);
				ScaleCertEnd.setSelection(180);
				ScaleVerificationDate.setSelection(360);
				ScaleSignatureDate.setSelection(360);
				
				fromRootCa.setText(controller.scaleUpdate(180, 180));
				thruRootCa.setText(controller.scaleUpdate(180, 180));
				fromCa.setText(controller.scaleUpdate(180, 180));
				thruCa.setText(controller.scaleUpdate(180, 180));
				fromCert.setText(controller.scaleUpdate(180, 180));
				thruCert.setText(controller.scaleUpdate(180, 180));
				signatureDate.setText(controller.scaleUpdate(360, 360));
				verificationDate.setText(controller.scaleUpdate(360, 360));
				
				TextRootCaFromDay.setText("1");
				TextRootCaThruDay.setText("1");
				TextCaFromDay.setText("1");
				TextCaThruDay.setText("1");
				TextCertFromDay.setText("1");
				TextCertThruDay.setText("1");
				TextVerificationDateDay.setText("1");
				TextSignatureDateDay.setText("1");
			}
		});
		btnReset.setText("Reset");
		new Label(composite, SWT.NONE);
		
		Button btnBack = new Button(composite, SWT.NONE);
		GridData gd_btnBack = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_btnBack.widthHint = 100;
		gd_btnBack.heightHint = 30;
		btnBack.setLayoutData(gd_btnBack);
		btnBack.setText("Back");
		
		Button btnForward = new Button(composite, SWT.NONE);
		GridData gd_btnForward = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnForward.widthHint = 100;
		gd_btnForward.heightHint = 30;
		btnForward.setLayoutData(gd_btnForward);
		btnForward.setText("Forward");
		
		Button btnCalculate = new Button(composite, SWT.NONE);
		btnCalculate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validity.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
				validity.setText("VALID");
			}
		});
		GridData gd_btnCalculate = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnCalculate.widthHint = 100;
		gd_btnCalculate.heightHint = 30;
		btnCalculate.setLayoutData(gd_btnCalculate);
		btnCalculate.setText("Validate");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		TabItem tbtmKettenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmKettenmodell.setText("Kettenmodell");
		
		
		// Selection Listeners | Scales
		ScaleRootCaBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromRootCa.setText(controller.scaleUpdate(ScaleRootCaBegin.getSelection(), 180));
				ScaleRootCaBegin.setToolTipText(controller.scaleUpdate(ScaleRootCaBegin.getSelection(), 180));
			}
		});
		
		ScaleRootCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruRootCa.setText(controller.scaleUpdate(ScaleRootCaEnd.getSelection(), 180));
				ScaleRootCaEnd.setToolTipText(controller.scaleUpdate(ScaleRootCaEnd.getSelection(), 180));
			}
		});
		
		ScaleCaBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromCa.setText(controller.scaleUpdate(ScaleCaBegin.getSelection(), 180));
				ScaleCaBegin.setToolTipText(controller.scaleUpdate(ScaleCaBegin.getSelection(), 180));
			}
		});
		
		ScaleCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruCa.setText(controller.scaleUpdate(ScaleCaEnd.getSelection(), 180));
				ScaleCaEnd.setToolTipText(controller.scaleUpdate(ScaleCaEnd.getSelection(), 180));
			}
		});
		
		ScaleCertBegin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromCert.setText(controller.scaleUpdate(ScaleCertBegin.getSelection(), 180));
				ScaleCertBegin.setToolTipText(controller.scaleUpdate(ScaleCertBegin.getSelection(), 180));
			}
		});
		
		ScaleCertEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruCert.setText(controller.scaleUpdate(ScaleCertEnd.getSelection(), 180));
				ScaleCertEnd.setToolTipText(controller.scaleUpdate(ScaleCertEnd.getSelection(), 180));
			}
		});
		
		ScaleSignatureDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				signatureDate.setText(controller.scaleUpdate(ScaleSignatureDate.getSelection(), 360));
				ScaleSignatureDate.setToolTipText(controller.scaleUpdate(ScaleSignatureDate.getSelection(), 180));
			}
		});
		
		ScaleVerificationDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				LabelVerificationDate.setText(controller.scaleUpdate(ScaleVerificationDate.getSelection(), 360));
				ScaleVerificationDate.setToolTipText(controller.scaleUpdate(ScaleVerificationDate.getSelection(), 180));

			}
		});
		
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
