//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizard.wizardpages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * This is the wizard to delete a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class ManageVisibleIdentitiesPage extends WizardPage {
	
	public ManageVisibleIdentitiesPage() {
		super("Identit\u00e4ten ausw\u00e4hlen", "Identit\u00e4ten ausw\u00e4hlen", null);
        setDescription("W\u00e4hlen Sie die anzuzeigenden Identit\u00e4t und best\u00e4tigen Sie Ihre Auswahl.");
	}

	private Label selection;
	private Table table;
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout grid = new GridLayout(1,false);
		grid.horizontalSpacing = 20;
		grid.verticalSpacing = 10;
		container.setLayout(grid);
		
		Label lbl = new Label(container, SWT.WRAP);
		lbl.setText("Selektieren Sie die Identit\u00e4ten, die in der Visualisierung angezeigt werden sollen. Zur Auswahl stehen alle\nIdentit\u00e4ten, die aktuell in Ihrem Schl\u00fcsselspeicher existieren.");
		
		String[]identities = {"Alice", "Bob", "Carol", "Dave"};
		
		
		table = new Table(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    for (String s: identities) {
	      TableItem item = new TableItem(table, SWT.NONE);
	      item.setText(s);
	    }
	    table.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String auswahl = "";
				int count = 0;
				for (TableItem ti : table.getItems()){
					if (ti.getChecked()){
						auswahl += ti.getText()+" ";
						count ++;
					}
					
				}
				
				selection.setText(count+".."+auswahl);
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		GridData gdTable = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
		gdTable.widthHint = 150;
		gdTable.heightHint = 150;
		table.setLayoutData(gdTable);
		
		
		selection = new Label(container, SWT.NONE);
		selection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1) );
		
	}

}
