//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa.ui.wizards.wizardpages;

import java.util.Arrays;
import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.visual.extendedrsa.IdentityManager;

/**
 * This is the wizard to delete a new Identity with the button in the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class ManageVisibleIdentitiesPage extends WizardPage {
	
	private TabFolder tabfolder;
	private Table table;
	private Vector<String> displayList;
	private Vector<String> alreadyShownIDs;
	
	public ManageVisibleIdentitiesPage(TabFolder folder) {
		super("Identit\u00e4ten ausw\u00e4hlen", "Identit\u00e4ten ausw\u00e4hlen", null);
        setDescription("W\u00e4hlen Sie die anzuzeigenden Identit\u00e4t und best\u00e4tigen Sie Ihre Auswahl.");
        this.tabfolder = folder;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		GridLayout grid = new GridLayout(1,false);
		grid.horizontalSpacing = 20;
		grid.verticalSpacing = 10;
		container.setLayout(grid);
		
		Label lbl = new Label(container, SWT.WRAP);
		lbl.setText("Selektieren Sie die Identit\u00e4ten, die in der Visualisierung angezeigt werden sollen. Zur Auswahl stehen alle\nIdentit\u00e4ten, die aktuell in Ihrem Schl\u00fcsselspeicher existieren.\n\nHinweis: Es m\u00fcssen mindestens 2 Identit\u00e4ten gew\u00e4hlt werden.");
		
		
		String[] identities =new String[IdentityManager.getInstance().getContacts().size()];
		IdentityManager.getInstance().getContacts().toArray(identities);
		Arrays.sort(identities);
		
		//identify already shown tabs
	    alreadyShownIDs = new Vector<String>();
	    for (TabItem ti : tabfolder.getItems()){
	    	alreadyShownIDs.add(ti.getText());
	    }
	    
		table = new Table(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	    for (String s: identities) {
	      TableItem item = new TableItem(table, SWT.NONE);
	      item.setText(s);
	      if (alreadyShownIDs.contains(s)){
	    	  item.setChecked(true);
	      }
	    }
	    
	    table.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				displayList = new Vector<String>();
				for (TableItem ti : table.getItems()){
					if (ti.getChecked()){
						displayList.add(ti.getText());
					}
				}
				if (displayList.size() < 2){
					setPageComplete(false);
				}else{
					setPageComplete(true);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		GridData gdTable = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
		gdTable.widthHint = 150;
		gdTable.heightHint = 150;
		table.setLayoutData(gdTable);
		
	}
	public Vector<String> getDisplayList(){
		return displayList;
	}
	
	public Vector<String> getAllreadyShownList(){
		return alreadyShownIDs;
	}
}
