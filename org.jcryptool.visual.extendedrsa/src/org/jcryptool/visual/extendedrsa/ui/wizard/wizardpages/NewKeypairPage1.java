package org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

public class NewKeypairPage1 extends WizardPage{
	private Text text;
	private Text text_1;

	public NewKeypairPage1() {
		super("Neues Schlüsselpaar", "Neues Schlüsselpaar", null);
        setDescription("Bitte wählen Sie die Art ihres neuen Schlüssels und geben Sie die gewünschen Parameter an.");
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Button btnRsa = new Button(container, SWT.RADIO);
		btnRsa.setBounds(28, 25, 91, 18);
		btnRsa.setText("RSA");
		
		Button btnMultiprimeRsa = new Button(container, SWT.RADIO);
		btnMultiprimeRsa.setSelection(true);
		btnMultiprimeRsa.setBounds(28, 123, 142, 18);
		btnMultiprimeRsa.setText("Multi-prime RSA");
		
		Label lblP = new Label(container, SWT.NONE);
		lblP.setBounds(60, 49, 26, 14);
		lblP.setText("p:");
		
		Label lblE = new Label(container, SWT.NONE);
		lblE.setBounds(60, 73, 26, 14);
		lblE.setText("e:");
		
		Combo combo = new Combo(container, SWT.NONE);
		combo.setBounds(93, 45, 48, 22);
		
		Combo combo_1 = new Combo(container, SWT.NONE);
		combo_1.setBounds(93, 69, 48, 22);
		
		Label lblQ = new Label(container, SWT.NONE);
		lblQ.setText("q:");
		lblQ.setBounds(147, 49, 26, 14);
		
		Combo combo_2 = new Combo(container, SWT.NONE);
		combo_2.setBounds(180, 45, 48, 22);
		
		Label label = new Label(container, SWT.NONE);
		label.setText("p:");
		label.setBounds(60, 151, 26, 14);
		
		Combo combo_3 = new Combo(container, SWT.NONE);
		combo_3.setBounds(93, 147, 48, 22);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("q:");
		label_1.setBounds(147, 151, 26, 14);
		
		Combo combo_4 = new Combo(container, SWT.NONE);
		combo_4.setBounds(180, 147, 48, 22);
		
		Label lblR = new Label(container, SWT.NONE);
		lblR.setText("r:");
		lblR.setBounds(243, 151, 26, 14);
		
		Combo combo_5 = new Combo(container, SWT.NONE);
		combo_5.setBounds(276, 147, 48, 22);
		
		Label lblS = new Label(container, SWT.NONE);
		lblS.setText("s:");
		lblS.setBounds(330, 151, 26, 14);
		
		Combo combo_6 = new Combo(container, SWT.NONE);
		combo_6.setBounds(363, 147, 48, 22);
		
		Label lblT = new Label(container, SWT.NONE);
		lblT.setText("t:");
		lblT.setBounds(422, 151, 26, 14);
		
		Combo combo_7 = new Combo(container, SWT.NONE);
		combo_7.setBounds(454, 147, 48, 22);
		
		Label label_5 = new Label(container, SWT.NONE);
		label_5.setText("e:");
		label_5.setBounds(60, 175, 26, 14);
		
		Combo combo_8 = new Combo(container, SWT.NONE);
		combo_8.setBounds(93, 171, 48, 22);
		
		Label label_2 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(28, 210, 474, 2);
		
		Label lblPasswortEingeben = new Label(container, SWT.NONE);
		lblPasswortEingeben.setBounds(28, 231, 113, 14);
		lblPasswortEingeben.setText("Passwort eingeben");
		
		Label lblPasswortBesttigen = new Label(container, SWT.NONE);
		lblPasswortBesttigen.setText("Passwort bestätigen");
		lblPasswortBesttigen.setBounds(28, 253, 113, 14);
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(147, 228, 126, 19);
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(147, 250, 126, 19);
		
	}
}
