package org.jcryptool.visual.crtverification.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.wb.swt.SWTResourceManager;
import swing2swt.layout.BoxLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class CrtVerView extends Composite {
	private Text txt_root_ca_from_day;

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
		grpDetails.setLayout(new GridLayout(7, false));
		FormData fd_grpDetails = new FormData();
		fd_grpDetails.bottom = new FormAttachment(lblDetails, 231, SWT.BOTTOM);
		fd_grpDetails.top = new FormAttachment(lblDetails, 6);
		fd_grpDetails.right = new FormAttachment(lblDetails, -20, SWT.RIGHT);
		fd_grpDetails.left = new FormAttachment(root_ca_begin, 0, SWT.LEFT);
		grpDetails.setLayoutData(fd_grpDetails);
		new Label(grpDetails, SWT.NONE);
		
		Label lblRootCa_1 = new Label(grpDetails, SWT.NONE);
		GridData gd_lblRootCa_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblRootCa_1.widthHint = 140;
		lblRootCa_1.setLayoutData(gd_lblRootCa_1);
		lblRootCa_1.setText("Root CA");
		new Label(grpDetails, SWT.NONE);
		
		Label lblCa_1 = new Label(grpDetails, SWT.NONE);
		GridData gd_lblCa_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblCa_1.widthHint = 100;
		lblCa_1.setLayoutData(gd_lblCa_1);
		lblCa_1.setText("CA");
		
		Label lblUserCertificate_1 = new Label(grpDetails, SWT.NONE);
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
		
		Composite composite_1 = new Composite(grpDetails, SWT.NONE);
		composite_1.setLayout(new GridLayout(1, false));
		
		txt_root_ca_from_day = new Text(composite_1, SWT.BORDER);
		txt_root_ca_from_day.setSize(24, 19);
		
		Label lblNewLabel = new Label(grpDetails, SWT.NONE);
		lblNewLabel.setText("New Label");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		Label lblValidThru = new Label(grpDetails, SWT.NONE);
		lblValidThru.setText("valid thru:");
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		new Label(grpDetails, SWT.NONE);
		
		TabItem tbtmKettenmodell = new TabItem(tabFolder, SWT.NONE);
		tbtmKettenmodell.setText("Kettenmodell");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
