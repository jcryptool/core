package org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;

public class KeyringPage1 extends WizardPage{
	private Text text;
	private Table table;

	public KeyringPage1() {
		super("Meine Schlüssel", "Meine Schlüssel", null);
        setDescription("Hier können Sie Informationen zu Ihren Schlüssel einsehen.");
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Label lblWhlenSieEinen = new Label(container, SWT.NONE);
		lblWhlenSieEinen.setBounds(10, 22, 224, 14);
		lblWhlenSieEinen.setText("Wählen Sie einen Ihrer Schlüssel aus:");
		
		Combo combo = new Combo(container, SWT.NONE);
		combo.setBounds(240, 18, 165, 22);
		
		Label lblPasswortEingeben = new Label(container, SWT.NONE);
		lblPasswortEingeben.setBounds(10, 58, 114, 14);
		lblPasswortEingeben.setText("Passwort eingeben");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(240, 55, 165, 19);
		
		Button btnDatenAnzeigen = new Button(container, SWT.NONE);
		btnDatenAnzeigen.setBounds(441, 51, 108, 28);
		btnDatenAnzeigen.setText("Daten anzeigen");
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 126, 539, 98);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Key");
		
		TableColumn tblclmnValue = new TableColumn(table, SWT.NONE);
		tblclmnValue.setWidth(441);
		tblclmnValue.setText("Value");
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel.setBounds(240, 80, 242, 14);
		lblNewLabel.setText("Falsches Passwort für diesen Private-Key!");
		
	}
}
