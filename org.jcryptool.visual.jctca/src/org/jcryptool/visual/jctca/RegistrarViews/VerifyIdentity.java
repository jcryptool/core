package org.jcryptool.visual.jctca.RegistrarViews;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.Util;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;

public class VerifyIdentity extends Dialog implements SelectionListener {
	Shell parent;

	Composite main;

	Label img;

	Button btn_true;
	Button btn_false;

	List csr;
	Button forward_csr;
	Button reject_csr;

	CSR c;

	public VerifyIdentity(Shell parent, List csr, Button forward_csr,
			Button reject_csr) {
		super(parent, SWT.APPLICATION_MODAL);
		this.parent = parent;
		this.parent.setText("Itentitätsprüfung");
		this.csr = csr;
		this.forward_csr = forward_csr;
		this.reject_csr = reject_csr;
	}

	public String open(CSR c) {
		Shell shell = new Shell(getParent(), getStyle());
		this.c = c;
		parent = shell;
		shell.setText(getText());
		createContents(shell);
		String proof = c.getProof();
		Image i = null;
		if (proof.contains("icons\\") || proof.contains("icons/")) {
			i = Activator.getImageDescriptor(proof).createImage();
		} else {
			i = new Image(Display.getCurrent(), c.getProof());
		}
		img.setImage(i);
		parent.layout();
		shell.pack();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return "foo";
	}

	private void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(1, true));

		main = new Composite(shell, SWT.FILL);
		main.setLayout(new GridLayout(2, true));

		btn_true = new Button(main, SWT.PUSH);
		btn_true.setText("Match");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		btn_true.setLayoutData(data);
		btn_true.addSelectionListener(this);

		btn_false = new Button(main, SWT.PUSH);
		btn_false.setText("Reject");
		data = new GridData(GridData.FILL_HORIZONTAL);
		btn_false.setLayoutData(data);
		btn_false.addSelectionListener(this);

		shell.setDefaultButton(btn_false);

		data = new GridData(1, 1, true, true);
		data.horizontalSpan = 2;
		img = new Label(main, SWT.FILL);
		img.setLayoutData(data);

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Button src = (Button) arg0.getSource();
		String text = src.getText();

		if (text.equals(Messages.ShowCSR_verify_identity)) {
			int selected = csr.getSelectionIndex();
			CSR c = Util.getCSR(selected);
			if (c != null) {
				this.open(c);
			}
		} else if (src.equals(btn_true)) {
			forward_csr.setEnabled(true);
			reject_csr.setEnabled(true);
			c.setForwardenabled(true);
			c.setRejectenabled(true);
			parent.setVisible(false);
		} else if (src.equals(btn_false)) {
			forward_csr.setEnabled(false);
			reject_csr.setEnabled(true);
			c.setForwardenabled(false);
			c.setRejectenabled(true);
			parent.setVisible(false);
		}

	}
}
