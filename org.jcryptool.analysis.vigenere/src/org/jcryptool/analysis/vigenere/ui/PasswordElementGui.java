/* *****************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class PasswordElementGui {
	private boolean found = false;
	private Composite comp;
	private Text text;
	private int charnumber;
	private PasswordContainer contain;

	/**
	 * Constructor for PasswordElementGUI.
	 * 
	 * @param parent
	 *            The parent composite the control should be displayed in.
	 * @param number
	 * @param container
	 */
	public PasswordElementGui(final Composite parent, final int number, PasswordContainer container) {
		this.setNumber(number);
		this.contain = container;
		init(parent);
	}

	private void focus() {
		contain.showAnalysis(charnumber);
	}

	protected String getCharacter() {
		return text.getText();
	}

	/**
	 * @return the character
	 */
	public int getNumber() {
		return charnumber;
	}

	protected void hightlight(final String character) {
		found = true;
		text.setText(character);
		text.setBackground(new Color(null, 196, 255, 196));
	}

	/**
	 * Initializes the GUI for password
	 * 
	 * @param parent
	 *            The parent Composite in which the control should be displayed.
	 */
	private void init(final Composite parent) {

		comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_HAND));
		comp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent event) {
				focus();
			}
		});

		text = new Text(comp, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		text.setText(String.valueOf(charnumber));
		text.setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_HAND));
		text.setEditable(false);
		text.setEnabled(false);
	}

	public boolean isFound() {
		return found;
	}

	protected void select() {
		// old color.
		// text.setBackground(SWTResourceManager.getColor(180, 250, 255));
		text.setBackground(new Color(null, 55, 155, 255));
	}

	/**
	 * @param character
	 *            the character to set
	 */
	private void setNumber(int character) {
		this.charnumber = character;
	}

	protected void unselect() {
		if (found) {
			text.setBackground(new Color(null, 196, 255, 196));
		} else {
			text.setBackground(new Color(null, 250, 250, 250));
		}
	}
}
