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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.CertificateClasses.CSR;
import org.jcryptool.visual.jctca.CertificateClasses.RegistrarCSR;

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
	Group grp_exp;
	Label lbl_exp;
	public VerifyIdentity(Shell parent, List csr, Button forward_csr,
			Button reject_csr) {
		super(parent, SWT.APPLICATION_MODAL);
		this.parent = parent;

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
		if(proof == null){
			proof = "icons/ausweis.jpeg";//$NON-NLS-1$
		}
		if (proof.contains("icons\\") || proof.contains("icons/")) {//$NON-NLS-1$
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
		main.setLayout(new GridLayout(4, false));

		btn_true = new Button(main, SWT.PUSH);
		btn_true.setText("Identität als korrekt markieren");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		btn_true.setLayoutData(data);
		btn_true.addSelectionListener(this);

		btn_false = new Button(main, SWT.PUSH);
		btn_false.setText("Identität als gefälscht markieren");
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		btn_false.setLayoutData(data);
		btn_false.addSelectionListener(this);

		shell.setDefaultButton(btn_false);

		data = new GridData(1, 1, true, true);
		data.horizontalSpan = 3;
		img = new Label(main, SWT.FILL);
		img.setLayoutData(data);
		
		grp_exp = new Group(main, SWT.FILL);
		grp_exp.setText("Erklärung");
		data = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		grp_exp.setLayoutData(data);
		grp_exp.setLayout(new GridLayout(1, false));
		
		lbl_exp = new Label(grp_exp, SWT.WRAP);
		lbl_exp.setText("Sie sehen nun den Identitätsnachweis, der vom Antragsteller mit dem CSR übersendet wurde. In diesem Schritt müssen Sie bestätigen, ob es sich beim Antragsteller tatsächlich um die Person handelt, für die er oder sie sich ausgibt. Dabei sind vor allem zwei Dinge wichtig: Erstens, wirkt der Ausweis gefälscht? Und zweitens, stimmen die Daten am Ausweis mit den Angaben des Antragstellers überein?\n\n Die Tätigkeit der RA ist für die Vertrauenswürdigkeit einer PKI absolut entscheident. Nur durch die ausführliche und korrekte Prüfung der RA kann verhindert werden, dass sich kriminelle Personen als andere ausgeben können. Wie PKI-Betreiber in der Realität die Identität der Benutzer feststellen, lesen Sie in der Onlinehilfe.");
		data = new GridData(SWT.FILL, SWT.FILL, false, false,1,1);
		lbl_exp.setLayoutData(data);
		data.widthHint = 350;
		
		grp_exp.layout();

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Button btn = (Button) arg0.getSource();
		Integer data = (Integer) btn.getData();

		if (data != null && data.equals(0)) {
			int selected = csr.getSelectionIndex();
			CSR c = RegistrarCSR.getInstance().getCSR(selected);
			if (c != null) {
				this.open(c);
			}
		} else if (btn.equals(btn_true)) {
			forward_csr.setEnabled(true);
			reject_csr.setEnabled(true);
			c.setForwardenabled(true);
			c.setRejectenabled(true);
			parent.setVisible(false);
		} else if (btn.equals(btn_false)) {
			forward_csr.setEnabled(false);
			reject_csr.setEnabled(true);
			c.setForwardenabled(false);
			c.setRejectenabled(true);
			parent.setVisible(false);
		}

	}
}
