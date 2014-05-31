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

public class CrtVerViewComposite extends Composite {
	
	static Text TextRootCaFromDay;
	static Text TextCaFromDay;
	static Text TextCertFromDay;
	static Text TextRootCaThruDay;
	static Text TextCaThruDay;
	static Text TextCertThruDay;
	static Text TextSignatureDateDay;
	static Text TextVerificationDateDay;

	// Date now Instance
	static Label thruRootCa;
	static Label fromRootCa;
	static Label thruCa;
	static Label fromCa;
	static Label thruCert;
	static Label fromCert;
	static Label signatureDate;
	static Label verificationDate;
	static Label validity;
	
	static Scale ScaleFromTN;
	static Scale ScaleThruTN;
	static Scale ScaleFromCA;
	static Scale ScaleThruCA;
	static Scale ScaleFromRoot;
	static Scale ScaleThruRoot;
	
	static CrtVerViewController controller = new CrtVerViewController();

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
		tbtmSchalenmodell.setText("Shell Model");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setControl(composite);
		composite.setLayout(new GridLayout(15, false));
		
		txtDiesIstDer = new Text(composite, SWT.BORDER);
		txtDiesIstDer.setEnabled(false);
		txtDiesIstDer.setEditable(false);
		txtDiesIstDer.setText("DE: Mit diesem Plugin können Sie sehen, wie es zu einer Bewertung der Gültigkeit eines Signatur kommt, wenn man unterschiedliche Zertifikatsbäume und unterschiedliche Gültigkeitsmodelle benutzt.\n\nEN: This plugin helps to demonstrate the validation checks of the shell- and chain model. ");
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
		
		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1));
		
		Label label = new Label(composite_4, SWT.NONE);
		label.setText(controller.scaleUpdate(0, 180, "yyyy"));
		label.setBounds(0, 0, 59, 14);
		
		Label label_1 = new Label(composite_4, SWT.NONE);
		label_1.setText(controller.scaleUpdate(360, 180, "yyyy"));
		label_1.setAlignment(SWT.RIGHT);
		label_1.setBounds(301, 0, 59, 14);
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridData gd_composite_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_3.widthHint = 360;
		composite_3.setLayoutData(gd_composite_3);
		
		Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setBounds(0, 0, 59, 14);
		label_2.setText(controller.scaleUpdate(0, 180, "yyyy"));
		
		Label label_3 = new Label(composite_3, SWT.NONE);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(301, 0, 59, 14);
		label_3.setText(controller.scaleUpdate(360, 180, "yyyy"));
		new Label(composite, SWT.NONE);
		
		Label lblRootCa = new Label(composite, SWT.NONE);
		GridData gd_lblRootCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblRootCa.heightHint = 30;
		gd_lblRootCa.verticalIndent = 10;
		lblRootCa.setLayoutData(gd_lblRootCa);
		lblRootCa.setText("Root CA");
		lblRootCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblRootCa.setAlignment(SWT.CENTER);
		
		Scale ScaleRootCaBegin = new Scale(composite, SWT.NONE);
		ScaleFromRoot = ScaleRootCaBegin;
		ScaleFromRoot.setToolTipText("");
		
		ScaleFromRoot.setMaximum(360);
		GridData gd_ScaleFromRoot = new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1);
		gd_ScaleFromRoot.widthHint = 360;
		ScaleFromRoot.setLayoutData(gd_ScaleFromRoot);
		ScaleFromRoot.setSelection(180);
		
		Scale ScaleRootCaEnd = new Scale(composite, SWT.NONE);
		ScaleThruRoot = ScaleRootCaEnd;
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
		btnLoadRootCa.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    ChooseCert wiz = new ChooseCert(3);
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
		
		Label lblCa = new Label(composite, SWT.NONE);
		GridData gd_lblCa = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblCa.verticalIndent = 10;
		gd_lblCa.heightHint = 30;
		lblCa.setLayoutData(gd_lblCa);
		lblCa.setText("CA");
		lblCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblCa.setAlignment(SWT.CENTER);
		
		Scale ScaleCaBegin = new Scale(composite, SWT.NONE);
		ScaleFromCA = ScaleCaBegin;
		ScaleFromCA.setMaximum(360);
		GridData gd_ScaleFromCA = new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1);
		gd_ScaleFromCA.widthHint = 360;
		ScaleFromCA.setLayoutData(gd_ScaleFromCA);
		ScaleFromCA.setSelection(180);
		
		Scale ScaleCaEnd = new Scale(composite, SWT.NONE);
		ScaleThruCA = ScaleCaEnd;
		ScaleThruCA.setMaximum(360);
		GridData gd_ScaleThruCA = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleThruCA.widthHint = 360;
		ScaleThruCA.setLayoutData(gd_ScaleThruCA);
		ScaleThruCA.setSelection(180);
		
		Button btnLoadCa = new Button(composite, SWT.NONE);
		GridData gd_btnLoadCa = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnLoadCa.heightHint = 30;
		gd_btnLoadCa.widthHint = 100;
		btnLoadCa.setLayoutData(gd_btnLoadCa);
		btnLoadCa.setText("Load CA");
		btnLoadCa.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    ChooseCert wiz = new ChooseCert(2);
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
		
		Label lblUserCertificate = new Label(composite, SWT.NONE);
		GridData gd_lblUserCertificate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblUserCertificate.verticalIndent = 10;
		gd_lblUserCertificate.heightHint = 30;
		lblUserCertificate.setLayoutData(gd_lblUserCertificate);
		lblUserCertificate.setText("User Certificate");
		lblUserCertificate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblUserCertificate.setAlignment(SWT.CENTER);
		
		Scale ScaleCertBegin = new Scale(composite, SWT.NONE);
		ScaleFromTN = ScaleCertBegin;
		ScaleFromTN.setMaximum(360);
		GridData gd_ScaleFromTN = new GridData(SWT.LEFT, SWT.CENTER, false, false, 12, 1);
		gd_ScaleFromTN.widthHint = 360;
		ScaleFromTN.setLayoutData(gd_ScaleFromTN);
		ScaleFromTN.setSelection(180);
		
		Scale ScaleCertEnd = new Scale(composite, SWT.NONE);
		ScaleThruTN = ScaleCertEnd;
		ScaleThruTN.setMaximum(360);
		GridData gd_ScaleThruTN = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_ScaleThruTN.widthHint = 360;
		ScaleThruTN.setLayoutData(gd_ScaleThruTN);
		ScaleThruTN.setSelection(180);
		Button btnLoadUserCert = new Button(composite, SWT.NONE);
		// Selection Listeners | Scales
        btnLoadUserCert.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    ChooseCert wiz = new ChooseCert(1);
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
		
		Label SeperatorHorizontal = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_SeperatorHorizontal = new GridData(SWT.LEFT, SWT.CENTER, false, false, 14, 1);
		gd_SeperatorHorizontal.widthHint = 835;
		SeperatorHorizontal.setLayoutData(gd_SeperatorHorizontal);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_5 = new Composite(composite, SWT.NONE);
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.CENTER, false, false, 13, 1);
		gd_composite_5.widthHint = 720;
		composite_5.setLayoutData(gd_composite_5);
		
		Label label_4 = new Label(composite_5, SWT.NONE);
		label_4.setText(controller.scaleUpdate(0, 360, "yyyy"));
		label_4.setBounds(0, 0, 59, 14);
		
		Label label_5 = new Label(composite_5, SWT.NONE);
		label_5.setText(controller.scaleUpdate(720, 360, "yyyy"));
		label_5.setAlignment(SWT.RIGHT);
		label_5.setBounds(666, 0, 59, 14);
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
		
		Group grpDetails = new Group(composite, SWT.NONE);
		GridData gd_grpDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 13, 1);
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
		
		Label lblvalidity = new Label(composite, SWT.NONE);
		validity = lblvalidity;
		validity.setFont(SWTResourceManager.getFont("Arial", 14, SWT.BOLD));
		validity.setAlignment(SWT.CENTER);
		GridData gd_validity = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_validity.heightHint = 25;
		gd_validity.widthHint = 200;
		validity.setLayoutData(gd_validity);
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
		
		final Button btnModifiedShellModel = new Button(composite, SWT.CHECK);
		btnModifiedShellModel.setText("Modified Shell Model");
		new Label(composite, SWT.NONE);
		
		Button btnReset = new Button(composite, SWT.NONE);
		GridData gd_btnReset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 10, 1);
		gd_btnReset.widthHint = 100;
		gd_btnReset.heightHint = 30;
		btnReset.setLayoutData(gd_btnReset);
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ScaleFromRoot.setSelection(180);
				ScaleThruRoot.setSelection(180);
				ScaleFromCA.setSelection(180);
				ScaleThruCA.setSelection(180);
				ScaleFromTN.setSelection(180);
				ScaleThruTN.setSelection(180);
				ScaleVerificationDate.setSelection(360);
				ScaleSignatureDate.setSelection(360);
				
				fromRootCa.setText(controller.scaleUpdate(180, 180));
				fromRootCa.setToolTipText(controller.scaleUpdate(180, 180, "MMM/yy"));
				thruRootCa.setText(controller.scaleUpdate(180, 180));
				thruRootCa.setToolTipText(controller.scaleUpdate(180, 180, "MMM/yy"));
				fromCa.setText(controller.scaleUpdate(180, 180));
				fromCa.setToolTipText(controller.scaleUpdate(180, 180, "MMM/yy"));
				thruCa.setText(controller.scaleUpdate(180, 180));
				thruCa.setToolTipText(controller.scaleUpdate(180, 180, "MMM/yy"));
				fromCert.setText(controller.scaleUpdate(180, 180));
				fromCert.setToolTipText(controller.scaleUpdate(180, 180, "MMM/yy"));
				thruCert.setText(controller.scaleUpdate(180, 180));
				thruCert.setToolTipText(controller.scaleUpdate(180, 180, "MMM/yy"));
				signatureDate.setText(controller.scaleUpdate(360, 360));
				signatureDate.setToolTipText(controller.scaleUpdate(360, 360, "MMM/yy"));
				verificationDate.setText(controller.scaleUpdate(360, 360));
				verificationDate.setToolTipText(controller.scaleUpdate(360, 360, "MMM/yy"));
				
				TextRootCaFromDay.setText("1");
				TextRootCaThruDay.setText("1");
				TextCaFromDay.setText("1");
				TextCaThruDay.setText("1");
				TextCertFromDay.setText("1");
				TextCertThruDay.setText("1");
				TextVerificationDateDay.setText("1");
				TextSignatureDateDay.setText("1");
				validity.setBackground(null);
				validity.setText("");
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
				if(btnModifiedShellModel.getSelection()){
					controller.validate(2);
				}
				else{
					controller.validate(1);
				}
			}
		});
		GridData gd_btnCalculate = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnCalculate.widthHint = 100;
		gd_btnCalculate.heightHint = 30;
		btnCalculate.setLayoutData(gd_btnCalculate);
		btnCalculate.setText("Validate");
		
		TabItem tbtmKettenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmKettenmodell.setText("Chain Model");
		
		
		// Selection Listeners | Scales
		ScaleFromRoot.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromRootCa.setText(controller.scaleUpdate(ScaleFromRoot.getSelection(), 180));
				ScaleFromRoot.setToolTipText(controller.scaleUpdate(ScaleFromRoot.getSelection(), 180, "MMM/yy"));
				CrtVerViewController.flag=false;
			}
		});
		
		ScaleRootCaEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruRootCa.setText(controller.scaleUpdate(ScaleThruRoot.getSelection(), 180));
				ScaleThruRoot.setToolTipText(controller.scaleUpdate(ScaleThruRoot.getSelection(), 180, "MMM/yy"));
				CrtVerViewController.flag=false;
			}
		});
		
		ScaleFromCA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromCa.setText(controller.scaleUpdate(ScaleFromCA.getSelection(), 180));
				ScaleFromCA.setToolTipText(controller.scaleUpdate(ScaleFromCA.getSelection(), 180, "MMM/yy"));
				CrtVerViewController.flag=false;
			}
		});
		
		ScaleThruCA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruCa.setText(controller.scaleUpdate(ScaleThruCA.getSelection(), 180));
				ScaleThruCA.setToolTipText(controller.scaleUpdate(ScaleThruCA.getSelection(), 180, "MMM/yy"));
				CrtVerViewController.flag=false;
			}
		});
		
		ScaleFromTN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				fromCert.setText(controller.scaleUpdate(ScaleFromTN.getSelection(), 180));
				ScaleFromTN.setToolTipText(controller.scaleUpdate(ScaleFromTN.getSelection(), 180, "MMM/yy"));
				CrtVerViewController.flag=false;
			}
		});
		
		ScaleThruTN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				thruCert.setText(controller.scaleUpdate(ScaleThruTN.getSelection(), 180));
				ScaleThruTN.setToolTipText(controller.scaleUpdate(ScaleThruTN.getSelection(), 180, "MMM/yy"));
				CrtVerViewController.flag=false;
			}
		});
		
		ScaleSignatureDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				signatureDate.setText(controller.scaleUpdate(ScaleSignatureDate.getSelection(), 360));
				ScaleSignatureDate.setToolTipText(controller.scaleUpdate(ScaleSignatureDate.getSelection(), 360, "MMM/yy"));
			}
		});
		
		ScaleVerificationDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Add or Remain Time dependent on selection
				LabelVerificationDate.setText(controller.scaleUpdate(ScaleVerificationDate.getSelection(), 360));
				ScaleVerificationDate.setToolTipText(controller.scaleUpdate(ScaleVerificationDate.getSelection(), 360, "MMM/yy"));

			}
		});
		
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
