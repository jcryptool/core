package org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewIdentityPage1 extends WizardPage{
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;

	public NewIdentityPage1() {
		super("Neue Identität", "Neue Identität", null);
        setDescription("Geben Sie den Namen Ihrer Identität ein und drücken Sie 'Weiter' um das Schlüsselpaar zu erstellen.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setBounds(10, 24, 110, 14);
		lblName.setText("Name der Identität");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(157, 21, 142, 19);
		
		Label lblVorname = new Label(container, SWT.NONE);
		lblVorname.setText("Vorname");
		lblVorname.setBounds(10, 57, 110, 14);
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(157, 54, 142, 19);
		
		Label lblNachname = new Label(container, SWT.NONE);
		lblNachname.setText("Nachname");
		lblNachname.setBounds(10, 80, 110, 14);
		
		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(157, 77, 142, 19);
		
		Label lblFirma = new Label(container, SWT.NONE);
		lblFirma.setText("Firma/Organisation");
		lblFirma.setBounds(10, 103, 110, 14);
		
		text_3 = new Text(container, SWT.BORDER);
		text_3.setBounds(157, 100, 142, 19);
		
		Label lblLand = new Label(container, SWT.NONE);
		lblLand.setText("Land/Region");
		lblLand.setBounds(10, 126, 110, 14);
		
		text_4 = new Text(container, SWT.BORDER);
		text_4.setBounds(157, 123, 142, 19);
		
	}

}
