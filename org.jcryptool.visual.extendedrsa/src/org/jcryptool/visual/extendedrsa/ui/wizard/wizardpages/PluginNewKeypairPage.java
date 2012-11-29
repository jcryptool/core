package org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class PluginNewKeypairPage extends WizardPage{
	private Text text;
	private Text text_1;

	public PluginNewKeypairPage() {
		super("Neues Schlüsselpaar", "Neues Schlüsselpaar", null);
        setDescription("Bitte wählen Sie die Art ihres neuen Schlüssels und geben Sie die gewünschen Parameter an.");
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Button button = new Button(container, SWT.RADIO);
		button.setText("RSA");
		button.setBounds(28, 25, 91, 18);
		
		Button button_1 = new Button(container, SWT.RADIO);
		button_1.setText("Multi-prime RSA");
		button_1.setSelection(true);
		button_1.setBounds(28, 111, 142, 18);
		
		Label lblSchlssellngeWhlen = new Label(container, SWT.NONE);
		lblSchlssellngeWhlen.setText("Schlüssellänge wählen:");
		lblSchlssellngeWhlen.setBounds(60, 53, 143, 14);
		
		Combo combo_1 = new Combo(container, SWT.NONE);
		combo_1.add("1024");
		combo_1.add("2048");
		combo_1.setBounds(209, 49, 64, 22);
		
		Label label_9 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_9.setBounds(28, 210, 474, 2);
		
		Label label_10 = new Label(container, SWT.NONE);
		label_10.setText("Passwort eingeben");
		label_10.setBounds(28, 231, 113, 14);
		
		Label label_11 = new Label(container, SWT.NONE);
		label_11.setText("Passwort bestätigen");
		label_11.setBounds(28, 253, 113, 14);
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(147, 228, 126, 19);
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(147, 250, 126, 19);
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Schlüssellänge wählen:");
		label.setBounds(60, 139, 143, 14);
		
		Combo combo = new Combo(container, SWT.NONE);
		combo.add("1024");
		combo.add("2048");
		combo.setBounds(209, 135, 64, 22);
		combo.select(1);
		
		Label lblAnzahlDerPrimfaktoren = new Label(container, SWT.NONE);
		lblAnzahlDerPrimfaktoren.setText("Anzahl der Primfaktoren:");
		lblAnzahlDerPrimfaktoren.setBounds(60, 163, 143, 14);
		
		Combo combo_2 = new Combo(container, SWT.NONE);
		combo_2.add("3");
		combo_2.add("4");
		combo_2.add("5");
		combo_2.setBounds(209, 159, 64, 22);
		combo_2.select(2);
		
	}
}
