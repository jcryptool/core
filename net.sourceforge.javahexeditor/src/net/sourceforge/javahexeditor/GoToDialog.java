/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * The official javahexeditor site is sourceforge.net/projects/javahexeditor
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.javahexeditor.common.NumberUtility;
import net.sourceforge.javahexeditor.common.SWTUtility;
import net.sourceforge.javahexeditor.common.TextUtility;

/**
 * Go to dialog. Remembers previous state.
 *
 * @author Jordi Bergenthal
 */
final class GoToDialog extends Dialog {

	Shell shell;
	private Composite composite;
	private Composite composite2;
	Button hexRadioButton;
	private Button decRadioButton;
	Button showButton;
	Button gotoButton;
	private Button closeButton;
	private Composite composite1;
	Text text;
	private Label label;
	Label label2;

	SelectionAdapter defaultSelectionAdapter = new SelectionAdapter() {
		@Override
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			text.setFocus();
		}
	};

	private long finalResult = -1L;
	long buttonPressed = 0;

	boolean lastHexButtonSelected = true;
	String lastLocationText;
	long limit = -1L;
	long tempResult = -1L;

	public GoToDialog(Shell aShell) {
		super(aShell);
		lastLocationText = Texts.EMPTY;
	}

	/**
	 * This method initializes composite
	 */
	private void createComposite() {
		RowLayout rowLayout1 = new RowLayout();
		// rowLayout1.marginHeight = 5;
		rowLayout1.marginTop = 2;
		rowLayout1.marginBottom = 2;
		// rowLayout1.marginWidth = 5;
		rowLayout1.type = SWT.VERTICAL;
		composite = new Composite(composite1, SWT.NONE);
		composite.setLayout(rowLayout1);

		SelectionAdapter hexTextSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				text.setText(text.getText()); // generate event
				lastHexButtonSelected = e.widget == hexRadioButton;
				/*
				 * Crashes when the text is not a number if (lastHexButtonSelected) return;
				 * String textNew = text.getText(); textNew =
				 * Integer.toHexString(Integer.parseInt(textNew)).toUpperCase();
				 * text.setText(textNew); // generate event lastHexButtonSelected = true;
				 */
			}
		};
		/*
		 * Crashes when the text is not radix 16 SelectionAdapter
		 * decTextSelectionAdapter = new SelectionAdapter() { public void
		 * widgetSelected(org.eclipse.swt.events.SelectionEvent e) { if
		 * (!lastHexButtonSelected) return; String textNew = text.getText(); textNew =
		 * Integer.toString(Integer.parseInt(textNew, 16)); text.setText(textNew); //
		 * generate event lastHexButtonSelected = false; } };
		 */
		// Besides the crashes: the user always knows which number is entering,
		// don't need any automatic
		// conversion. What does sometimes happen is one enters the right number
		// and the wrong hex or dec was
		// selected. In that case automatic conversion is the wrong thing to do
		// and very annoying.
		hexRadioButton = new Button(composite, SWT.RADIO);
		hexRadioButton.setText(Texts.GOTO_DIALOG_HEX);
		hexRadioButton.addSelectionListener(defaultSelectionAdapter);
		hexRadioButton.addSelectionListener(hexTextSelectionAdapter);

		decRadioButton = new Button(composite, SWT.RADIO);
		decRadioButton.setText(Texts.GOTO_DIALOG_DECIMAL);
		decRadioButton.addSelectionListener(defaultSelectionAdapter);
		decRadioButton.addSelectionListener(hexTextSelectionAdapter);// decTextSelectionAdapter);
	}

	/**
	 * Save the result and close dialog
	 */
	void saveResultAndClose() {
		lastLocationText = text.getText();
		finalResult = tempResult;
		shell.close();
	}

	public long getButtonPressed() {
		return buttonPressed;
	}

	/**
	 * This method initializes composite2
	 *
	 */
	private void createComposite2() {
		RowLayout rowLayout1 = new RowLayout();
		rowLayout1.type = org.eclipse.swt.SWT.VERTICAL;
		rowLayout1.marginHeight = 10;
		rowLayout1.marginWidth = 10;
		rowLayout1.fill = true;

		composite2 = new Composite(shell, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(composite1);
		formData.right = new FormAttachment(100);
		composite2.setLayoutData(formData);
		composite2.setLayout(rowLayout1);

		showButton = new Button(composite2, SWT.NONE);
		showButton.setText(Texts.GOTO_DIALOG_SHOW_LOCATION_BUTTON_LABEL);
		showButton.addSelectionListener(defaultSelectionAdapter);
		showButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				buttonPressed = 1;
				saveResultAndClose();
			}
		});

		gotoButton = new Button(composite2, SWT.NONE);
		gotoButton.setText(Texts.GOTO_DIALOG_GOTO_LOCATION_BUTTON_LABEL);
		gotoButton.addSelectionListener(defaultSelectionAdapter);
		gotoButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				buttonPressed = 2;
				saveResultAndClose();
			}
		});

		closeButton = new Button(composite2, SWT.NONE);
		closeButton.setText(Texts.BUTTON_CLOSE_LABEL);
		closeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				shell.close();
			}
		});

		shell.setDefaultButton(showButton);
	}

	/**
	 * This method initializes composite1
	 */
	private void createComposite1() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite1 = new Composite(shell, SWT.NONE);
		composite1.setLayout(gridLayout);
		createComposite();
		text = new Text(composite1, SWT.BORDER | SWT.SINGLE);
		text.setTextLimit(30);
		int columns = 35;
		GC gc = new GC(text);
		FontMetrics fm = gc.getFontMetrics();
		int width = (int)(columns * fm.getAverageCharacterWidth());
		gc.dispose();
		text.setLayoutData(new GridData(width, SWT.DEFAULT));
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String newText = text.getText();
				tempResult = NumberUtility.parseString(hexRadioButton.getSelection(), newText);

				if (tempResult >= 0L && tempResult <= limit) {
					showButton.setEnabled(true);
					gotoButton.setEnabled(true);
					label2.setText(Texts.EMPTY);
				} else {
					showButton.setEnabled(false);
					gotoButton.setEnabled(false);
					if (Texts.EMPTY.equals(newText)) {
						label2.setText(Texts.EMPTY);
					} else if (tempResult < 0) {
						label2.setText(Texts.DIALOG_ERROR_NOT_A_NUMBER_MESSAGE);
					} else {
						label2.setText(Texts.DIALOG_ERROR_LOCATION_OUT_OF_RANGE_MESSAGE);
					}
				}
			}
		});
		FormData formData = new FormData();
		formData.top = new FormAttachment(label);
		composite1.setLayoutData(formData);
	}

	/**
	 * This method initializes the shell.
	 */
	private void createShell() {
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		shell.setText(Texts.GOTO_DIALOG_GOTO_LOCATION_SHELL_LABEL);
		FormLayout formLayout = new FormLayout();
		formLayout.marginHeight = 3;
		formLayout.marginWidth = 3;
		shell.setLayout(formLayout);
		label = new Label(shell, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(100);
		label.setLayoutData(formData);
		createComposite1();
		createComposite2();
		label2 = new Label(shell, SWT.CENTER);
		FormData formData2 = new FormData();
		formData2.left = new FormAttachment(0);
		formData2.right = new FormAttachment(100);
		formData2.top = new FormAttachment(composite1);
		formData2.bottom = new FormAttachment(100, -10);
		label2.setLayoutData(formData2);
	}

	public long open(Shell parentShell, long limit) {
		this.limit = limit;
		finalResult = -1L;
		buttonPressed = 0;
		if (shell == null || shell.isDisposed()) {
			createShell();
		}

		SWTUtility.placeInCenterOf(shell, parentShell);
		if (lastHexButtonSelected) {
			hexRadioButton.setSelection(true);
		} else {
			decRadioButton.setSelection(true);
		}
		label.setText(TextUtility.format(Texts.GOTO_DIALOG_MESSAGE_ENTER_LOCATION,
				NumberUtility.getDecimalAndHexRangeString(0, this.limit)));
		text.setText(lastLocationText);
		text.selectAll();
		text.setFocus();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return finalResult;
	}
}
