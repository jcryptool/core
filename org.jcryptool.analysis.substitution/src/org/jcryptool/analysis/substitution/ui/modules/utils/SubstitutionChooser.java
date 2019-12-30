//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class SubstitutionChooser extends Shell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static Map<Character, Character> getMappingBySelectorShell(AbstractAlphabet alpha, String data) {
		
		char[] chars = data.toCharArray();
		List<Character> myList = new LinkedList<Character>();
		for(char c: chars) myList.add(c);
		
		try {
			Display display = Display.getDefault();
			SubstitutionChooser shell = new SubstitutionChooser(display, alpha, myList);
			shell.open();
			shell.layout();
			shell.pack();
			
			Point mousePos = Display.getCurrent().getCursorLocation();
			shell.setLocation(mousePos.x, mousePos.y-shell.getSize().y);
			
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			if(shell.getSelectedMapping() != null) {
				return shell.getSelectedMapping();
			}
		} catch (Exception ex) {
		    LogUtil.logError(ex);
		}
		return null;
	}

	private List<Character> chars;
	private AbstractAlphabet alpha;
	protected Map<Character, Character> selectedMapping;
	private Button btnFinish;
	private SubstitutionKeyEditor editor;

	/**
	 * Create the shell.
	 * @param display
	 */
	public SubstitutionChooser(Display display, AbstractAlphabet alpha, List<Character> chars) {
		super(display, SWT.BORDER);
		this.alpha = alpha;
		this.chars = chars;
		createContents();
		updateState();
		this.addShellListener(new ShellListener() {
			
			@Override
			public void shellIconified(ShellEvent e) {
				close();
			}
			
			@Override
			public void shellDeiconified(ShellEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellDeactivated(ShellEvent e) {
				close();
			}
			
			@Override
			public void shellClosed(ShellEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void shellActivated(ShellEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application"); //$NON-NLS-1$
		setSize(450, 300);
		setLayout(new GridLayout(1, false));
		
//		comp = new Composite(this, SWT.BORDER);
//		comp.setLayout(new GridLayout(1, false));
//		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
//		Label l = new Label(comp, SWT.NONE);
//		l.setText("Geben Sie mindestens eine Substitution ein (Geheimtext -> Klartext):");
		
		editor = new SubstitutionKeyEditor(this, SWT.NONE, alpha, false, chars);
		editor.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		editor.setCharMappingExternal(new HashMap<Character, Character>());
		
		editor.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				updateState();
			}
		});
		
		Composite compCtrls = new Composite(this, SWT.NONE);
		compCtrls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		compCtrls.setLayout(new GridLayout(2, false));
		
		Button btnCancel = new Button(compCtrls, SWT.PUSH);
		btnCancel.setText(Messages.SubstitutionChooser_1);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedMapping = null;
				close();
			}
		});
		
		btnFinish = new Button(compCtrls, SWT.PUSH);
		btnFinish.setLayoutData(new GridData(SWT.TRAIL, SWT.CENTER, true, false));
		btnFinish.setText(Messages.SubstitutionChooser_2);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedMapping = editor.getCharMapping();
				close();
			}
		});
		
	}

	protected void updateState() {
		Map<Character, Character> charMapping = editor.getCharMapping();
		for(Character c: charMapping.values()) {
			if(c != null) {
				btnFinish.setEnabled(true);
				return;
			}
		}
		btnFinish.setEnabled(false);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
	public Map<Character, Character> getSelectedMapping() {
		return selectedMapping;
	}
}
