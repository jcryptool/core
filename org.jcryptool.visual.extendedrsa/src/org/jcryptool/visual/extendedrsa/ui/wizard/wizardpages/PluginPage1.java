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

public class PluginPage1 extends WizardPage{
	private Table table;

	public PluginPage1() {
		super("RSA", "RSA", null);
        setDescription("Um einen Text mit dem RSA-Algorithmus zu ver- oder zu entschlüsseln, wählen Sie einen schon vorhandenen Schlüssel oder erstellen Sie einen neuen Schlüssel.");
	}


	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Group grpFunktion = new Group(container, SWT.NONE);
		grpFunktion.setText("Funktion");
		grpFunktion.setBounds(10, 10, 570, 59);
		
		Button btnVerschlsseln = new Button(grpFunktion, SWT.RADIO);
		btnVerschlsseln.setBounds(10, 10, 101, 18);
		btnVerschlsseln.setText("Verschlüsseln");
		
		Button btnEntschlsseln = new Button(grpFunktion, SWT.RADIO);
		btnEntschlsseln.setSelection(true);
		btnEntschlsseln.setBounds(234, 10, 107, 18);
		btnEntschlsseln.setText("Entschlüsseln");
		
		Group grpSchlssel = new Group(container, SWT.NONE);
		grpSchlssel.setText("Schlüssel wählen");
		grpSchlssel.setBounds(10, 87, 570, 160);
		table = new Table(grpSchlssel, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 324, 115);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(221);
		tblclmnNewColumn.setText("Typ");
		
		tblclmnName.setData("1", "asdf");
		tblclmnNewColumn.setData("1", "description");
		
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText(new String[] {"Alice", "Private Key 1 - multiPrime RSA 1024 bit"});
		TableItem tableItem2 = new TableItem(table, SWT.NONE);
		tableItem2.setText(new String[] {"Bob", "Private Key 1 - RSA 2048 bit"});
		
		Button btnNeuesSchlsselpaarErstellen = new Button(grpSchlssel, SWT.NONE);
		btnNeuesSchlsselpaarErstellen.setEnabled(false);
		btnNeuesSchlsselpaarErstellen.setBounds(363, 47, 183, 39);
		btnNeuesSchlsselpaarErstellen.setText("Neues Schlüsselpaar erstellen");
		
	}
}
