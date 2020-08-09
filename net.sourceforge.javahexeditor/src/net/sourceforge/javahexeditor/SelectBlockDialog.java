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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.javahexeditor.BinaryContent.RangeSelection;
import net.sourceforge.javahexeditor.common.NumberUtility;
import net.sourceforge.javahexeditor.common.SWTUtility;
import net.sourceforge.javahexeditor.common.TextUtility;

/**
 * Select block dialog. Remembers previous state.
 *
 * @author Andre Bossert
 */
final class SelectBlockDialog extends Dialog {

	private final class MyModifyListener implements ModifyListener {
		long result;
		boolean empty;

		public MyModifyListener() {
			result = -1L;
			empty = true;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			String newText = ((Text) e.widget).getText();
			result = NumberUtility.parseString(hexRadioButton.getSelection(), newText);
			empty = newText.isEmpty();
			validateResults();
		}

		public long getResult() {
			return result;
		}

		public boolean isEmpty() {
			return empty;
		}
	}

	Shell shell;
	private Composite compositeRadio;
	private Composite compositeTexts;
	private Composite compositeButtons;
	Button hexRadioButton;
	private Button decRadioButton;
	private Button button;
	private Button button1;
	Text startText;
	MyModifyListener startTextListener;
	Text endText;
	MyModifyListener endTextListener;
	private Label label;
	private Label statusLabel;

	SelectionAdapter defaultSelectionAdapter = new SelectionAdapter() {
		@Override
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			startText.setFocus();
		}
	};

	long finalStartResult = -1L;
	long finalEndResult = -1L;
	boolean lastHexButtonSelected = true;
	String lastStartText;
	String lastEndText;
	private long limit = -1L;

	public SelectBlockDialog(Shell aShell) {
		super(aShell);
		lastStartText = Texts.EMPTY;
		lastEndText = Texts.EMPTY;
	}


	/**
	 * This method initializes composite1
	 */
	private void createComposite1() {
		compositeRadio = new Composite(shell, SWT.NONE);
		compositeRadio.setLayout(new GridLayout());


		SelectionAdapter hexTextSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				startText.setText(startText.getText()); // generate event
				endText.setText(endText.getText()); // generate event
				lastHexButtonSelected = e.widget == hexRadioButton;
				/*
				 * Crashes when the text is not a number if (lastHexButtonSelected) return;
				 * String startTextNew = startText.getText(); String endTextNew =
				 * endText.getText(); startTextNew =
				 * Integer.toHexString(Integer.parseInt(startTextNew )).toUpperCase();
				 * endTextNew = Integer.toHexString(Integer.parseInt
				 * (endTextNew)).toUpperCase(); startText.setText(startTextNew); // generate
				 * event endText.setText(endTextNew); // generate event lastHexButtonSelected =
				 * true;
				 */
			}
		};
		/*
		 * Crashes when the text is not radix 16 SelectionAdapter
		 * decTextSelectionAdapter = new SelectionAdapter() { public void
		 * widgetSelected(org.eclipse.swt.events.SelectionEvent e) { if
		 * (!lastHexButtonSelected) return; String startTextNew = startText.getText();
		 * String endTextNew = endText.getText(); startTextNew =
		 * Integer.toString(Integer.parseInt(startTextNew, 16)); endTextNew =
		 * Integer.toString(Integer.parseInt(endTextNew, 16));
		 * startText.setText(startTextNew); // generate event
		 * endText.setText(endTextNew); // generate event lastHexButtonSelected = false;
		 * } };
		 */
		// Besides the crashes: the user always knows which number is entering,
		// don't need any automatic
		// conversion. What does sometimes happen is one enters the right number
		// and the wrong hex or dec was
		// selected. In that case automatic conversion is the wrong thing to do
		// and very annoying.
		hexRadioButton = new Button(compositeRadio, SWT.RADIO);
		hexRadioButton.setText(Texts.SELECTION_BLOCK_DIALOG_HEX_LABEL);
		hexRadioButton.addSelectionListener(defaultSelectionAdapter);
		hexRadioButton.addSelectionListener(hexTextSelectionAdapter);

		decRadioButton = new Button(compositeRadio, SWT.RADIO);
		decRadioButton.setText(Texts.SELECTION_BLOCK_DIALOG_DEC_LABEL);
		decRadioButton.addSelectionListener(defaultSelectionAdapter);
		hexRadioButton.addSelectionListener(hexTextSelectionAdapter);

	}
	
	/**
	 * This method initializes composite2
	 *
	 */
	private void createComposite2() {
		compositeTexts = new Composite(shell, SWT.NONE);
		compositeTexts.setLayout(new GridLayout(2, false));
		
		startText = new Text(compositeTexts, SWT.BORDER | SWT.SINGLE);
		GridData gridData_startText = new GridData(SWT.FILL, SWT.CENTER, true, true);
		startText.setTextLimit(30);
		int columns = 35;
		GC gc = new GC(startText);
		FontMetrics fm = gc.getFontMetrics();
		int width = (int)(columns * fm.getAverageCharacterWidth());
		gc.dispose();
		gridData_startText.widthHint = width;
		startText.setLayoutData(gridData_startText);
		startTextListener = new MyModifyListener();
		startText.addModifyListener(startTextListener);

		endText = new Text(compositeTexts, SWT.BORDER | SWT.SINGLE);
		GridData gridData_endText = new GridData(SWT.FILL, SWT.CENTER, true, true);
		endText.setTextLimit(30);
		gc = new GC(endText);
		fm = gc.getFontMetrics();
		width = (int)(columns * fm.getAverageCharacterWidth());
		gc.dispose();
		gridData_endText.widthHint = width;
		endText.setLayoutData(gridData_endText);
		endTextListener = new MyModifyListener();
		endText.addModifyListener(endTextListener);
	}

	/**
	 * This method initializes composite3
	 *
	 */
	private void createComposite3() {
		compositeButtons = new Composite(shell, SWT.NONE);
		compositeButtons.setLayout(new GridLayout());
		
		button = new Button(compositeButtons, SWT.NONE);
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		button.setText(Texts.SELECTION_BLOCK_DIALOG_SELECT_BUTTON_LABEL);
		button.addSelectionListener(defaultSelectionAdapter);
		button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				lastStartText = startText.getText();
				finalStartResult = startTextListener.getResult();
				lastEndText = endText.getText();
				finalEndResult = endTextListener.getResult();
				shell.close();
			}
		});
		
		shell.setDefaultButton(button);
		
		button1 = new Button(compositeButtons, SWT.NONE);
		button1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		button1.setText(Texts.BUTTON_CLOSE_LABEL);
		button1.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				shell.close();
			}
		});
	}

	/**
	 * This method initializes the shell.
	 *
	 */
	private void createShell() {
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		shell.setText(Texts.SELECTION_BLOCK_DIALOG_TITLE);
		shell.setLayout(new  GridLayout(3, false));
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		createComposite1();
		createComposite2();
		createComposite3();
		
		statusLabel = new Label(shell, SWT.CENTER);
		statusLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
	}




	public boolean open(Shell parentShell, RangeSelection rangeSelection, long aLimit) {
		if (rangeSelection == null) {
			throw new IllegalArgumentException("Parameter 'rangeSelection' must not be null.");
		}
		limit = aLimit;
		finalStartResult = -1L;
		finalEndResult = -1L;
		if (shell == null || shell.isDisposed()) {
			createShell();
		}
		SWTUtility.placeInCenterOf(shell, parentShell);

		if (lastHexButtonSelected) {
			hexRadioButton.setSelection(true);
		} else {
			decRadioButton.setSelection(true);
		}
		label.setText(
				TextUtility.format(Texts.SELECTION_BLOCK_DIALOG_RANGE_LABEL, NumberUtility.getDecimalString(limit),
						NumberUtility.getHexString(0), NumberUtility.getHexString(limit)));
		if (rangeSelection.getLength() > 0) {
			if (lastHexButtonSelected) {
				lastStartText = NumberUtility.getHexString(rangeSelection.start);
				lastEndText = NumberUtility.getHexString(rangeSelection.end);
			} else {
				lastStartText = NumberUtility.getDecimalString(rangeSelection.start);
				lastEndText = NumberUtility.getDecimalString(rangeSelection.end);
			}
		}
		startText.setText(lastStartText);
		endText.setText(lastEndText);
		startText.selectAll();
		startText.setFocus();
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		return finalStartResult>=0  && finalEndResult >=0;
	}

	public void validateResults() {
		long result1 = startTextListener.getResult();
		long result2 = endTextListener.getResult();
		if ((result1 >= 0L) && (result1 <= limit) && (result2 >= 0L) && (result2 <= limit) && (result2 > result1)) {
			button.setEnabled(true);
			statusLabel.setText(Texts.EMPTY);
		} else {
			button.setEnabled(false);
			if (startTextListener.isEmpty() || endTextListener.isEmpty()) {
				statusLabel.setText(Texts.EMPTY);
			} else if ((result1 < 0) || (result2 < 0)) {
				statusLabel.setText(Texts.DIALOG_ERROR_NOT_A_NUMBER_MESSAGE);
			} else if (result2 <= result1) {
				statusLabel.setText(Texts.DIALOG_ERROR_END_SMALLER_THAN_OR_EQUAL_TO_START_MESSAGE);
			} else {
				statusLabel.setText(Texts.DIALOG_ERROR_LOCATION_OUT_OF_RANGE_MESSAGE);
			}
		}
	}

	public long getFinalStartResult() {
		return finalStartResult;
	}

	public long getFinalEndResult() {
		return finalEndResult;
	}

}
