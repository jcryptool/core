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
<<<<<<< HEAD
=======
import org.jcryptool.visual.crtverification.*;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
>>>>>>> FETCH_HEAD

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
<<<<<<< HEAD
		composite.setLayout(new FormLayout());
		
		Scale root_ca_begin = new Scale(composite, SWT.NONE);
		FormData fd_root_ca_begin = new FormData();
		fd_root_ca_begin.bottom = new FormAttachment(0, 70);
		fd_root_ca_begin.right = new FormAttachment(0, 260);
		fd_root_ca_begin.top = new FormAttachment(0, 50);
		fd_root_ca_begin.left = new FormAttachment(0, 10);
		root_ca_begin.setLayoutData(fd_root_ca_begin);
		root_ca_begin.setSelection(50);
		
		Scale root_ca_end = new Scale(composite, SWT.NONE);
		FormData fd_root_ca_end = new FormData();
		fd_root_ca_end.bottom = new FormAttachment(0, 70);
		fd_root_ca_end.right = new FormAttachment(0, 550);
		fd_root_ca_end.top = new FormAttachment(0, 50);
		fd_root_ca_end.left = new FormAttachment(0, 300);
		root_ca_end.setLayoutData(fd_root_ca_end);
		root_ca_end.setSelection(50);
		
		Scale ca_begin = new Scale(composite, SWT.NONE);
		FormData fd_ca_begin = new FormData();
		fd_ca_begin.bottom = new FormAttachment(0, 110);
		fd_ca_begin.right = new FormAttachment(0, 260);
		fd_ca_begin.top = new FormAttachment(0, 90);
		fd_ca_begin.left = new FormAttachment(0, 10);
		ca_begin.setLayoutData(fd_ca_begin);
		ca_begin.setSelection(50);
		
		Scale ca_end = new Scale(composite, SWT.NONE);
		FormData fd_ca_end = new FormData();
		fd_ca_end.bottom = new FormAttachment(0, 110);
		fd_ca_end.right = new FormAttachment(0, 550);
		fd_ca_end.top = new FormAttachment(0, 90);
		fd_ca_end.left = new FormAttachment(0, 300);
		ca_end.setLayoutData(fd_ca_end);
		ca_end.setSelection(50);
		
		Scale crt_begin = new Scale(composite, SWT.NONE);
		FormData fd_crt_begin = new FormData();
		fd_crt_begin.bottom = new FormAttachment(0, 150);
		fd_crt_begin.right = new FormAttachment(0, 260);
		fd_crt_begin.top = new FormAttachment(0, 130);
		fd_crt_begin.left = new FormAttachment(0, 10);
		crt_begin.setLayoutData(fd_crt_begin);
		crt_begin.setSelection(50);
		
		Scale crt_end = new Scale(composite, SWT.NONE);
		FormData fd_crt_end = new FormData();
		fd_crt_end.bottom = new FormAttachment(0, 150);
		fd_crt_end.right = new FormAttachment(0, 550);
		fd_crt_end.top = new FormAttachment(0, 130);
		fd_crt_end.left = new FormAttachment(0, 300);
		crt_end.setLayoutData(fd_crt_end);
		crt_end.setSelection(50);
		
		Label lblNotValidBefore = new Label(composite, SWT.NONE);
		FormData fd_lblNotValidBefore = new FormData();
		fd_lblNotValidBefore.bottom = new FormAttachment(0, 54);
		fd_lblNotValidBefore.right = new FormAttachment(0, 207);
		fd_lblNotValidBefore.top = new FormAttachment(0, 27);
		fd_lblNotValidBefore.left = new FormAttachment(0, 58);
		lblNotValidBefore.setLayoutData(fd_lblNotValidBefore);
		lblNotValidBefore.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblNotValidBefore.setAlignment(SWT.CENTER);
		lblNotValidBefore.setText("Not Valid Before");
		
		Label lblNotValidAfter = new Label(composite, SWT.NONE);
		FormData fd_lblNotValidAfter = new FormData();
		fd_lblNotValidAfter.bottom = new FormAttachment(0, 54);
		fd_lblNotValidAfter.right = new FormAttachment(0, 502);
		fd_lblNotValidAfter.top = new FormAttachment(0, 27);
		fd_lblNotValidAfter.left = new FormAttachment(0, 353);
		lblNotValidAfter.setLayoutData(fd_lblNotValidAfter);
		lblNotValidAfter.setText("Not Valid After");
		lblNotValidAfter.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblNotValidAfter.setAlignment(SWT.CENTER);
		
		Label lblRootCa = new Label(composite, SWT.NONE);
		FormData fd_lblRootCa = new FormData();
		fd_lblRootCa.bottom = new FormAttachment(0, 80);
		fd_lblRootCa.right = new FormAttachment(0, 690);
		fd_lblRootCa.top = new FormAttachment(0, 50);
		fd_lblRootCa.left = new FormAttachment(0, 590);
		lblRootCa.setLayoutData(fd_lblRootCa);
		lblRootCa.setText("Root CA");
		lblRootCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblRootCa.setAlignment(SWT.CENTER);
		
		Label lblCa = new Label(composite, SWT.NONE);
		FormData fd_lblCa = new FormData();
		fd_lblCa.bottom = new FormAttachment(0, 120);
		fd_lblCa.right = new FormAttachment(0, 690);
		fd_lblCa.top = new FormAttachment(0, 90);
		fd_lblCa.left = new FormAttachment(0, 590);
		lblCa.setLayoutData(fd_lblCa);
		lblCa.setText("CA");
		lblCa.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblCa.setAlignment(SWT.CENTER);
		
		Label lblUserCertificate = new Label(composite, SWT.NONE);
		FormData fd_lblUserCertificate = new FormData();
		fd_lblUserCertificate.bottom = new FormAttachment(0, 160);
		fd_lblUserCertificate.right = new FormAttachment(0, 690);
		fd_lblUserCertificate.top = new FormAttachment(0, 130);
		fd_lblUserCertificate.left = new FormAttachment(0, 590);
		lblUserCertificate.setLayoutData(fd_lblUserCertificate);
		lblUserCertificate.setText("User Certificate");
		lblUserCertificate.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
		lblUserCertificate.setAlignment(SWT.CENTER);
		
		Label lblDetails = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.CENTER);
		FormData fd_lblDetails = new FormData();
		fd_lblDetails.bottom = new FormAttachment(0, 183);
		fd_lblDetails.right = new FormAttachment(0, 690);
		fd_lblDetails.top = new FormAttachment(0, 156);
		fd_lblDetails.left = new FormAttachment(0, 10);
		lblDetails.setLayoutData(fd_lblDetails);
		lblDetails.setAlignment(SWT.CENTER);
		lblDetails.setText("Details");
		
		Group grpDetails = new Group(composite, SWT.NONE);
		grpDetails.setText("Details");
		grpDetails.setLayout(new GridLayout(6, false));
		FormData fd_grpDetails = new FormData();
		fd_grpDetails.bottom = new FormAttachment(lblDetails, 133, SWT.BOTTOM);
		fd_grpDetails.right = new FormAttachment(lblRootCa, 0, SWT.RIGHT);
		fd_grpDetails.top = new FormAttachment(lblDetails, 6);
		fd_grpDetails.left = new FormAttachment(root_ca_begin, 0, SWT.LEFT);
		grpDetails.setLayoutData(fd_grpDetails);
=======
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
>>>>>>> FETCH_HEAD
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
		
<<<<<<< HEAD
		Label lbl_thru_rootca = new Label(composite_thru_rootca, SWT.NONE);
=======
		final Label lbl_thru_rootca = new Label(composite_thru_rootca, SWT.NONE);
>>>>>>> FETCH_HEAD
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
		
<<<<<<< HEAD
		Button btnCalculate = new Button(composite, SWT.NONE);
		FormData fd_btnCalculate = new FormData();
		fd_btnCalculate.top = new FormAttachment(grpDetails, 11);
		fd_btnCalculate.right = new FormAttachment(lblRootCa, 0, SWT.RIGHT);
		btnCalculate.setLayoutData(fd_btnCalculate);
		btnCalculate.setText("Calculate");
		
		Button btnReset = new Button(composite, SWT.NONE);
		FormData fd_btnReset = new FormData();
		fd_btnReset.bottom = new FormAttachment(btnCalculate, 0, SWT.BOTTOM);
		fd_btnReset.right = new FormAttachment(btnCalculate, -6);
		btnReset.setLayoutData(fd_btnReset);
		btnReset.setText("Reset");
		
		Button btnBack = new Button(composite, SWT.NONE);
		FormData fd_btnBack = new FormData();
		fd_btnBack.top = new FormAttachment(btnCalculate, 0, SWT.TOP);
		fd_btnBack.left = new FormAttachment(0, 10);
		btnBack.setLayoutData(fd_btnBack);
		btnBack.setText("Back");
		
		Button btnForward = new Button(composite, SWT.NONE);
		FormData fd_btnForward = new FormData();
		fd_btnForward.bottom = new FormAttachment(btnCalculate, 0, SWT.BOTTOM);
		fd_btnForward.left = new FormAttachment(btnBack, 5);
		btnForward.setLayoutData(fd_btnForward);
		btnForward.setText("Forward");
		
		TabItem tbtmKettenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmKettenmodell.setText("Kettenmodell");

=======
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
		
>>>>>>> FETCH_HEAD
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
