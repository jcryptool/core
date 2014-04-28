package org.jcryptool.visual.crtverification.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.visual.crtverification.*;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.widgets.DateTime;

public class CrtVerView extends Composite {
	private Text txt_root_ca_from_day;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CrtVerView(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		
		TabItem tbtmSchalenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setText("Schalenmodell");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSchalenmodell.setControl(composite);
		composite.setLayout(new GridLayout(9, false));
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
		lblNotValidBefore.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblNotValidBefore.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblNotValidBefore.setAlignment(SWT.CENTER);
		lblNotValidBefore.setText("Not Valid Before");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblNotValidAfter = new Label(composite, SWT.NONE);
		lblNotValidAfter.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		lblNotValidAfter.setText("Not Valid After");
		lblNotValidAfter.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblNotValidAfter.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Scale root_ca_begin = new Scale(composite, SWT.NONE);
		root_ca_begin.setSelection(50);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		final Scale root_ca_end = new Scale(composite, SWT.NONE);
		
		
		root_ca_end.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		root_ca_end.setSelection(50);
		
		Label lblRootCa = new Label(composite, SWT.NONE);
		lblRootCa.setText("Root CA");
		lblRootCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblRootCa.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		
		Scale ca_begin = new Scale(composite, SWT.NONE);
		ca_begin.setSelection(50);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Scale ca_end = new Scale(composite, SWT.NONE);
		ca_end.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		ca_end.setSelection(50);
		
		Label lblCa = new Label(composite, SWT.NONE);
		lblCa.setText("CA");
		lblCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblCa.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		
		Scale crt_begin = new Scale(composite, SWT.NONE);
		crt_begin.setSelection(50);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Scale crt_end = new Scale(composite, SWT.NONE);
		crt_end.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		crt_end.setSelection(50);
		
		Label lblUserCertificate = new Label(composite, SWT.NONE);
		lblUserCertificate.setText("User Certificate");
		lblUserCertificate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblUserCertificate.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 8, 1);
		gd_label.widthHint = 395;
		label.setLayoutData(gd_label);
		new Label(composite, SWT.NONE);
		
		Scale scale = new Scale(composite, SWT.NONE);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 7, 1));
		scale.setSelection(50);
		
		Label lblSignatureDate = new Label(composite, SWT.NONE);
		lblSignatureDate.setText("Signature Date");
		lblSignatureDate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblSignatureDate.setAlignment(SWT.CENTER);
		new Label(composite, SWT.NONE);
		
		Group grpDetails = new Group(composite, SWT.NONE);
		grpDetails.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 9, 1));
		grpDetails.setText("Details");
		grpDetails.setLayout(new GridLayout(6, false));
		new Label(grpDetails, SWT.NONE);
		
		Label lblRootCa_1 = new Label(grpDetails, SWT.NONE);
		lblRootCa_1.setAlignment(SWT.CENTER);
		GridData gd_lblRootCa_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblRootCa_1.widthHint = 100;
		lblRootCa_1.setLayoutData(gd_lblRootCa_1);
		lblRootCa_1.setText("Root CA");
		
		Label lblCa_1 = new Label(grpDetails, SWT.NONE);
		lblCa_1.setAlignment(SWT.CENTER);
		GridData gd_lblCa_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblCa_1.widthHint = 100;
		lblCa_1.setLayoutData(gd_lblCa_1);
		lblCa_1.setText("CA");
		
		Label lblUserCertificate_1 = new Label(grpDetails, SWT.NONE);
		lblUserCertificate_1.setAlignment(SWT.CENTER);
		GridData gd_lblUserCertificate_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblUserCertificate_1.widthHint = 100;
		lblUserCertificate_1.setLayoutData(gd_lblUserCertificate_1);
		lblUserCertificate_1.setText("User Certificate");
		
		SashForm sashForm = new SashForm(grpDetails, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		sashForm.setSashWidth(4);
		sashForm.setWeights(new int[] {});
		
		Label lblValidFrom = new Label(grpDetails, SWT.NONE);
		lblValidFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValidFrom.setText("valid from:");
		
		Composite composite_from_rootca = new Composite(grpDetails, SWT.NONE);
		composite_from_rootca.setLayout(new GridLayout(2, false));
		
		txt_root_ca_from_day = new Text(composite_from_rootca, SWT.BORDER);
		GridData gd_txt_root_ca_from_day = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txt_root_ca_from_day.widthHint = 20;
		txt_root_ca_from_day.setLayoutData(gd_txt_root_ca_from_day);
		txt_root_ca_from_day.setSize(24, 19);
		
		Label lbl_from_rootca = new Label(composite_from_rootca, SWT.NONE);
		lbl_from_rootca.setText("09/2014");
		
		Composite composite_from_ca = new Composite(grpDetails, SWT.NONE);
		composite_from_ca.setLayout(new GridLayout(2, false));
		
		text = new Text(composite_from_ca, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text.widthHint = 20;
		text.setLayoutData(gd_text);
		
		Label lbl_from_ca = new Label(composite_from_ca, SWT.NONE);
		lbl_from_ca.setText("New Label");
		
		Composite composite_from_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_from_user_cert.setLayout(new GridLayout(2, false));
		
		text_1 = new Text(composite_from_user_cert, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_1.widthHint = 20;
		text_1.setLayoutData(gd_text_1);
		
		Label lbl_from_user_cert = new Label(composite_from_user_cert, SWT.NONE);
		lbl_from_user_cert.setText("New Label");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Label lblValidThru = new Label(grpDetails, SWT.NONE);
		lblValidThru.setText("valid thru:");
		
		Composite composite_thru_rootca = new Composite(grpDetails, SWT.NONE);
		composite_thru_rootca.setLayout(new GridLayout(2, false));
		
		text_2 = new Text(composite_thru_rootca, SWT.BORDER);
		GridData gd_text_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_2.widthHint = 20;
		text_2.setLayoutData(gd_text_2);
		
		final Label lbl_thru_rootca = new Label(composite_thru_rootca, SWT.NONE);
		lbl_thru_rootca.setText("New Label");
		
		Composite composite_thru_ca = new Composite(grpDetails, SWT.NONE);
		composite_thru_ca.setLayout(new GridLayout(2, false));
		
		text_3 = new Text(composite_thru_ca, SWT.BORDER);
		GridData gd_text_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_3.widthHint = 20;
		text_3.setLayoutData(gd_text_3);
		
		Label lbl_thru_ca = new Label(composite_thru_ca, SWT.NONE);
		lbl_thru_ca.setText("New Label");
		
		Composite composite_thru_user_cert = new Composite(grpDetails, SWT.NONE);
		composite_thru_user_cert.setLayout(new GridLayout(2, false));
		
		text_4 = new Text(composite_thru_user_cert, SWT.BORDER);
		GridData gd_text_4 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_text_4.widthHint = 20;
		text_4.setLayoutData(gd_text_4);
		
		Label lbl_thru_user_cert = new Label(composite_thru_user_cert, SWT.NONE);
		lbl_thru_user_cert.setText("New Label");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Button btnReset = new Button(composite, SWT.NONE);
		btnReset.setText("Reset");
		
		Button btnBack = new Button(composite, SWT.NONE);
		btnBack.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 5, 1));
		btnBack.setText("Back");
		
		Button btnForward = new Button(composite, SWT.NONE);
		btnForward.setText("Forward");
		
		Button btnCalculate = new Button(composite, SWT.NONE);
		btnCalculate.setText("Calculate");
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
		
		DateTime dateTime = new DateTime(composite, SWT.BORDER);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		TabItem tbtmKettenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmKettenmodell.setText("Kettenmodell");
		
		root_ca_end.addControlListener(new ControlAdapter() {
			@Override
			public void controlMoved(ControlEvent e) {
				int a = root_ca_end.getSelection();
				lbl_thru_rootca.setText("text"+a);
			}
		});

		root_ca_end.addDragDetectListener(new DragDetectListener() {
			public void dragDetected(DragDetectEvent e) {
				int a = root_ca_end.getSelection();
				lbl_thru_rootca.setText("text"+a);
			}
		});
		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
