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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import net.sourceforge.javahexeditor.BinaryContentFinder.Match;
import net.sourceforge.javahexeditor.common.NumberUtility;
import net.sourceforge.javahexeditor.common.SWTUtility;
import net.sourceforge.javahexeditor.common.TextUtility;

/**
 * Find/Replace dialog with hex/text, forward/backward, and ignore case options.
 * Remembers previous state, in case it has been closed by the user and reopened
 * again.
 *
 * @author Jordi Bergenthal
 
 *         TODO: Fix layout/visiblity and enabling
 */
final class FindReplaceDialog extends Dialog {

	static final Pattern patternHexDigits = Pattern.compile("[0-9a-fA-F]*");

	SelectionAdapter defaultSelectionAdapter = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (lastIgnoreCase != ignoreCaseCheckBox.getSelection() || lastForward != forwardRadioButton.getSelection()
					|| lastFindHexButtonSelected != findGroup.hexRadioButton.getSelection()
					|| lastReplaceHexButtonSelected != replaceGroup.hexRadioButton.getSelection()) {
				feedbackLabel.setText(Texts.EMPTY);
			}
			lastFocused.textCombo.setFocus();
		}
	};

	private List<FindReplaceHistory.Entry> findList;
	private List<FindReplaceHistory.Entry> replaceList;

	HexTexts myTarget;
	TextHexInputGroup lastFocused;

	boolean lastForward = true;
	boolean lastFindHexButtonSelected = true;
	boolean lastReplaceHexButtonSelected = true;
	boolean lastIgnoreCase = false;
	boolean searching = false;

	// Visual components
	Shell shell;
	TextHexInputGroup findGroup;
	TextHexInputGroup replaceGroup;
	private Group directionGroup;
	Button forwardRadioButton;
	Button backwardRadioButton;
	private Composite ignoreCaseComposite;
	Button ignoreCaseCheckBox;

	private Composite feedbackComposite;
	Label feedbackLabel;
	Composite progressComposite;
	ProgressBar progressBar;
	private Button progressBarStopButton;

	private Button findButton;
	private Button replaceButton;
	private Button replaceAllButton;
	private Button closeButton;

	/**
	 * Group with text/hex selector and text input
	 */
	private final class TextHexInputGroup {
		List<FindReplaceHistory.Entry> items;

		// visual components
		Group group;
		private Composite composite;
		Button hexRadioButton;
		Button textRadioButton;
		Combo textCombo;

		public TextHexInputGroup(List<FindReplaceHistory.Entry> oldItems) {
			if (oldItems == null) {
				throw new IllegalArgumentException("Parameter 'oldItems' must not be null.");
			}
			items = oldItems;
		}

		public void initialise() {
			group = new Group(shell, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			group.setLayout(gridLayout);
			createComposite();
			textCombo = new Combo(group, SWT.BORDER);
			int columns = 35;
			GC gc = new GC(textCombo);
			FontMetrics fm = gc.getFontMetrics();
			int width = (int)(columns * fm.getAverageCharacterWidth());
			gc.dispose();
			textCombo.setLayoutData(new GridData(width, SWT.DEFAULT));
			textCombo.addVerifyListener(new VerifyListener() {
				@Override
				public void verifyText(VerifyEvent e) {
					if (e.keyCode == 0) {
						return; // a list selection
					}
					if (hexRadioButton.getSelection()) {
						Matcher numberMatcher = patternHexDigits.matcher(e.text);
						e.doit = numberMatcher.matches();
					}
				}
			});
			textCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = textCombo.getSelectionIndex();
					if (index < 0) {
						return;
					}
					refreshHexOrText((items.get(index).isHex()));
				}
			});
			textCombo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					feedbackLabel.setText(Texts.EMPTY);
					if (TextHexInputGroup.this == findGroup) {
						dataToUI();
					}
				}
			});
		}

		/**
		 * This method initializes composite
		 */
		private void createComposite() {
			RowLayout rowLayout1 = new RowLayout();
			rowLayout1.marginTop = 2;
			rowLayout1.marginBottom = 2;
			rowLayout1.type = SWT.VERTICAL;
			composite = new Composite(group, SWT.NONE);
			composite.setLayout(rowLayout1);
			hexRadioButton = new Button(composite, SWT.RADIO);
			hexRadioButton.setText(Texts.FIND_REPLACE_DIALOG_HEX_RADIO_LABEL);
			hexRadioButton.addSelectionListener(defaultSelectionAdapter);
			hexRadioButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Matcher numberMatcher = patternHexDigits.matcher(textCombo.getText());
					if (!numberMatcher.matches()) {
						textCombo.setText(Texts.EMPTY);
					}
				}
			});
			textRadioButton = new Button(composite, SWT.RADIO);
			textRadioButton.setText(Texts.FIND_REPLACE_DIALOG_TEXT_RADIO_LABEL);
			textRadioButton.addSelectionListener(defaultSelectionAdapter);
		}

		public void refreshCombo() {
			if (items == null) {
				return;
			}

			if (textCombo.getItemCount() > 0) {
				textCombo.remove(0, textCombo.getItemCount() - 1);
			}
			for (Iterator<FindReplaceHistory.Entry> iterator = items.iterator(); iterator.hasNext();) {
				String itemString = (iterator.next()).getStringValue();
				textCombo.add(itemString);
			}
			if (!items.isEmpty()) {
				textCombo.setText(items.get(0).getStringValue());
			}
			selectText();
		}

		public void refreshHexOrText(boolean hex) {
			hexRadioButton.setSelection(hex);
			textRadioButton.setSelection(!hex);
		}

		public void rememberText() {
			String lastText = textCombo.getText();
			if (Texts.EMPTY.equals(lastText) || items == null) {
				return;
			}

			for (Iterator<FindReplaceHistory.Entry> iterator = items.iterator(); iterator.hasNext();) {
				String itemString = iterator.next().getStringValue();
				if (lastText.equals(itemString)) {
					iterator.remove();
				}
			}
			items.add(0, new FindReplaceHistory.Entry(lastText, hexRadioButton.getSelection()));
			refreshCombo();
		}

		public void selectText() {
			textCombo.setSelection(new Point(0, textCombo.getText().length()));
		}

		public void setEnabled(boolean enabled) {
			group.setEnabled(enabled);
			hexRadioButton.setEnabled(enabled);
			textRadioButton.setEnabled(enabled);
			textCombo.setEnabled(enabled);
		}
	}

	/**
	 * Create find/replace dialog always on top of shell
	 *
	 * @param shell
	 *            where it is displayed
	 */
	public FindReplaceDialog(Shell shell) {
		super(shell);
	}

	private void activateProgressBar() {
		// Set the progress bar to visible after 0,5 seconds.
		Display.getCurrent().timerExec(500, new Runnable() {
			@Override
			public void run() {
				if (searching && !progressComposite.isDisposed()) {
					progressComposite.setVisible(true);
				}
			}
		});
		long max = myTarget.myContent.length();
		long min = myTarget.getCaretPos();
		if (backwardRadioButton.getSelection()) {
			max = min;
			min = 0L;
		}
		int factor = 0;
		while (max > Integer.MAX_VALUE) {
			max = max >>> 1;
			min = min >>> 1;
			++factor;
		}
		progressBar.setMaximum((int) max);
		progressBar.setMinimum((int) min);
		progressBar.setSelection(0);
		final int finalFactor = factor;
		Display.getCurrent().timerExec(1000, new Runnable() {
			@Override
			public void run() {
				if (!searching || progressBar.isDisposed()) {
					return;
				}

				int selection = 0;
				if (myTarget.myFinder != null) {
					selection = (int) (myTarget.myFinder.getSearchPosition() >>> finalFactor);
					if (backwardRadioButton.getSelection()) {
						selection = progressBar.getMaximum() - selection;
					}
				}
				progressBar.setSelection(selection);
				Display.getCurrent().timerExec(1000, this);
			}
		});
	}

	/**
	 * Open and display the dialog.
	 *
	 * @param target
	 *            The target with data to search, not <code>null</code>.
	 * @param findReplaceHistory
	 *            The modifiable find-replace history, not <code>null</code>.
	 **/

	public void open(HexTexts target, FindReplaceHistory findReplaceHistory) {
		if (target == null) {
			throw new IllegalArgumentException("Parameter 'target' must not be null.");
		}
		if (findReplaceHistory == null) {
			throw new IllegalArgumentException("Parameter 'findReplaceHistory' must not be null.");
		}

		myTarget = target;

		this.findList = findReplaceHistory.getFindList();
		this.replaceList = findReplaceHistory.getReplaceList();

		if (shell == null || shell.isDisposed()) {
			createShell();
		}
		SWTUtility.placeInCenterOf(shell, target.getShell());
		findGroup.refreshCombo();
		long selectionLength = myTarget.getSelection().getLength();
		if (selectionLength > 0L && selectionLength <= BinaryContentFinder.MAX_SEQUENCE_SIZE) {
			findGroup.refreshHexOrText(true);
			ignoreCaseCheckBox.setEnabled(false);
			StringBuilder selectedText = new StringBuilder();
			byte[] selection = new byte[(int) selectionLength];
			try {
				myTarget.myContent.get(ByteBuffer.wrap(selection), myTarget.getSelection().start);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			for (int i = 0; i < selectionLength; ++i) {
				selectedText.append(HexTexts.byteToHex[selection[i] & 0x0ff]);
			}
			findGroup.textCombo.setText(selectedText.toString());
			findGroup.selectText();
		} else {
			findGroup.refreshHexOrText(lastFindHexButtonSelected);
			ignoreCaseCheckBox.setEnabled(!lastFindHexButtonSelected);
		}

		replaceGroup.refreshHexOrText(lastReplaceHexButtonSelected);
		replaceGroup.refreshCombo();

		ignoreCaseCheckBox.setSelection(lastIgnoreCase);
		if (lastForward) {
			forwardRadioButton.setSelection(true);
		} else {
			backwardRadioButton.setSelection(true);
		}
		feedbackLabel.setText(Texts.FIND_REPLACE_DIALOG_MESSAGE_SPECIFY_VALUE_TO_FIND);

		lastFocused = findGroup;
		lastFocused.textCombo.setFocus();
		dataToUI();
		shell.open();
	}

	/**
	 * This method initializes composite3
	 */
	private void createIgnoreCaseComposite() {
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 10;
		fillLayout.marginWidth = 10;
		ignoreCaseComposite = new Composite(shell, SWT.NONE);
		ignoreCaseComposite.setLayout(fillLayout);
		ignoreCaseCheckBox = new Button(ignoreCaseComposite, SWT.CHECK);
		ignoreCaseCheckBox.setText(Texts.FIND_REPLACE_DIALOG_IGNORE_CASE_CHECKBOX_LABEL);
		ignoreCaseCheckBox.addSelectionListener(defaultSelectionAdapter);
	}

	/**
	 * This method initializes group1
	 */
	private void createDirectionGroup() {
		directionGroup = new Group(shell, SWT.NONE);
		directionGroup.setText(Texts.FIND_REPLACE_DIALOG_DIRECTION_GROUP_LABEL);

		RowLayout rowLayout = new RowLayout();
		rowLayout.fill = true;
		rowLayout.type = org.eclipse.swt.SWT.HORIZONTAL;
		directionGroup.setLayout(rowLayout);

		forwardRadioButton = new Button(directionGroup, SWT.RADIO);
		forwardRadioButton.setText(Texts.FIND_REPLACE_DIALOG_DIRECTION_FORWARD_RADIO_LABEL);
		forwardRadioButton.addSelectionListener(defaultSelectionAdapter);
		backwardRadioButton = new Button(directionGroup, SWT.RADIO);
		backwardRadioButton.setText(Texts.FIND_REPLACE_DIALOG_DIRECTION_BACKWARD_RADIO_LABEL);
		backwardRadioButton.addSelectionListener(defaultSelectionAdapter);
	}

	private void createFeedbackComposite() {
		feedbackComposite = new Composite(shell, SWT.NONE);
		FormLayout formLayout2 = new FormLayout();
		// formLayout2.spacing = 5;
		feedbackComposite.setLayout(formLayout2);

		feedbackLabel = new Label(feedbackComposite, SWT.LEFT);
		FormData feedbackLabelData = new FormData();
		feedbackLabelData.top = new FormAttachment(0);
		feedbackLabelData.left = new FormAttachment(0);
		feedbackLabelData.right = new FormAttachment(100);
		feedbackLabel.setLayoutData(feedbackLabelData);
	}

	private void createProgressComposite() {
		progressComposite = new Composite(shell, SWT.NONE);
		FormLayout formLayout3 = new FormLayout();
		formLayout3.spacing = 5;
		progressComposite.setLayout(formLayout3);
		// FormData formData4 = new FormData();
		// formData4.top = new FormAttachment(feedbackLabel);
		// formData4.top = new FormAttachment(feedbackComposite);
		// formData4.bottom = new FormAttachment(100);
		// formData4.left = new FormAttachment(0);
		// formData4.right = new FormAttachment(100);

		progressBar = new ProgressBar(progressComposite, SWT.NONE);
		FormData formData5 = new FormData();
		formData5.bottom = new FormAttachment(100);
		formData5.left = new FormAttachment(0);
		formData5.height = progressBar.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;
		progressBar.setLayoutData(formData5);

		progressBarStopButton = new Button(progressComposite, SWT.NONE);
		progressBarStopButton.setText(Texts.FIND_REPLACE_DIALOG_STOP_SEARCHING_BUTTON_LABEL);
		FormData formData6 = new FormData();
		formData6.right = new FormAttachment(100);
		progressBarStopButton.setLayoutData(formData6);
		formData5.right = new FormAttachment(progressBarStopButton);
		progressBarStopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				myTarget.stopSearching();
			}
		});
		// progressComposite.setVisible(false);
	}

	private void createButtonBarComposite() {

		Composite buttonBar = new Composite(shell, SWT.NONE);
		FormData buttonBarData = new FormData();
		buttonBarData.top = new FormAttachment(feedbackComposite);
		buttonBarData.bottom = new FormAttachment(100);
		buttonBarData.left = new FormAttachment(0);
		buttonBarData.right = new FormAttachment(100);
		// buttonBar.setLayoutData(buttonBarData);

		buttonBar.setLayout(new RowLayout());
		findButton = new Button(buttonBar, SWT.NONE);
		findButton.setText(Texts.FIND_REPLACE_DIALOG_FIND_BUTTON_LABEL);
		findButton.addSelectionListener(defaultSelectionAdapter);
		findButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doFind();
			}
		});

		replaceButton = new Button(buttonBar, SWT.NONE);
		replaceButton.setText(Texts.FIND_REPLACE_DIALOG_REPLACE_BUTTON_LABEL);
		replaceButton.addSelectionListener(defaultSelectionAdapter);
		replaceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doReplace();
			}
		});
		replaceAllButton = new Button(buttonBar, SWT.NONE);
		replaceAllButton.setText(Texts.FIND_REPLACE_DIALOG_REPLACE_ALL_BUTTON_LABEL);
		replaceAllButton.addSelectionListener(defaultSelectionAdapter);
		replaceAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doReplaceAll();
			}
		});

		closeButton = new Button(buttonBar, SWT.NONE);
		closeButton.setText(Texts.BUTTON_CLOSE_LABEL);

		FormData formData1 = new FormData();
		formData1.right = new FormAttachment(100);
		formData1.bottom = new FormAttachment(100);
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
	}

	/**
	 * This method initializes the shell
	 */
	private void createShell() {
		shell = new Shell(getParent(), SWT.MODELESS | SWT.DIALOG_TRIM);
		shell.setText(Texts.FIND_REPLACE_DIALOG_TITLE);
		FormLayout formLayout = new FormLayout();
		formLayout.marginHeight = 5;
		formLayout.marginWidth = 5;
		formLayout.spacing = 5;
		// shell.setLayout(formLayout);
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellActivated(ShellEvent e) {
				dataToUI();
			}
		});

		if (findGroup == null) {
			findGroup = new TextHexInputGroup(findList);
		}
		findGroup.initialise();
		findGroup.group.setText(Texts.FIND_REPLACE_DIALOG_FIND_GROUP_LABEL);
		SelectionAdapter hexTextSelectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ignoreCaseCheckBox.setEnabled(e.widget == findGroup.textRadioButton);
			}
		};
		findGroup.textRadioButton.addSelectionListener(hexTextSelectionAdapter);
		findGroup.hexRadioButton.addSelectionListener(hexTextSelectionAdapter);

		if (replaceGroup == null) {
			replaceGroup = new TextHexInputGroup(replaceList);
		}
		replaceGroup.initialise();
		replaceGroup.group.setText(Texts.FIND_REPLACE_DIALOG_REPLACE_GROUP_LABEL);
		FormData formData = new FormData();
		formData.top = new FormAttachment(findGroup.group);
		// replaceGroup.group.setLayoutData(formData);

		createDirectionGroup();
		createIgnoreCaseComposite();
		createFeedbackComposite();
		createProgressComposite();
		createButtonBarComposite();

		shell.setDefaultButton(findButton);

		shell.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				myTarget.stopSearching();
			}
		});
	}

	void doFind() {
		prepareToRun();
		progressBarStopButton.setText(Texts.FIND_REPLACE_DIALOG_STOP_SEARCHING_BUTTON_LABEL);
		String findLiteral = findGroup.textCombo.getText();
		String message;

		if (findLiteral.length() > 0) {
			Match match = myTarget.findAndSelect(findLiteral, findGroup.hexRadioButton.getSelection(),
					forwardRadioButton.getSelection(), ignoreCaseCheckBox.getSelection());
			if (match.isFound()) {
				message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_FOUND, findLiteral,
						NumberUtility.getDecimalAndHexString(match.getStartPosition()));
			} else {
				if (match.getException() == null) {
					message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_NOT_FOUND, findLiteral);
				} else {
					message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_ERROR_WHILE_SEARCHING, findLiteral,
							match.getException().getLocalizedMessage());
				}
			}
		} else {
			message = Texts.FIND_REPLACE_DIALOG_MESSAGE_SPECIFY_VALUE_TO_FIND;
		}
		endOfRun(message);
	}

	void doReplace() {
		replace();
		doFind();
	}

	void doReplaceAll() {
		prepareToRun();
		progressBarStopButton.setText(Texts.FIND_REPLACE_DIALOG_STOP_SEARCHING_BUTTON_LABEL);
		String findLiteral = findGroup.textCombo.getText();
		String replaceLiteral = replaceGroup.textCombo.getText();
		String message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_NOT_FOUND, findLiteral);

		if (findLiteral.length() > 0) {
			try {

				long[] result = myTarget.replaceAll(findLiteral, findGroup.hexRadioButton.getSelection(),
						forwardRadioButton.getSelection(), ignoreCaseCheckBox.getSelection(), replaceLiteral,
						replaceGroup.hexRadioButton.getSelection());
				long replacements = result[0];
				long startPosition = result[1];

				if (replacements == 1) {
					message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_ONE_REPLACEMENT, findLiteral,
							replaceLiteral, NumberUtility.getDecimalAndHexString(startPosition));
					message = Texts.FIND_REPLACE_DIALOG_MESSAGE_ONE_REPLACEMENT;
				} else {
					message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_MANY_REPLACEMENTS,
							NumberUtility.getDecimalString(replacements), findLiteral, replaceLiteral);
				}
			} catch (IOException ex) {
				message = TextUtility.format(Texts.FIND_REPLACE_DIALOG_MESSAGE_ERROR_WHILE_REPLACING, findLiteral,
						replaceLiteral, ex.getLocalizedMessage());
			}
		}
		endOfRun(message);
	}

	void dataToUI() {
		findGroup.setEnabled(!searching);
		replaceGroup.setEnabled(!searching);

		directionGroup.setEnabled(!searching);
		forwardRadioButton.setEnabled(!searching);
		backwardRadioButton.setEnabled(!searching);

		ignoreCaseCheckBox.setEnabled(!searching);

		findButton.setEnabled(!searching);
		replaceButton.setEnabled(!searching);
		replaceAllButton.setEnabled(!searching);

		closeButton.setEnabled(!searching);
		if (searching) {
			return;
		}

		boolean somethingToFind = findGroup.textCombo.getText().length() > 0;
		long selectionLength = 0L;
		if (myTarget != null) {
			selectionLength = myTarget.getSelection().getLength();
		}
		findButton.setEnabled(somethingToFind);
		replaceButton.setEnabled(selectionLength > 0L && somethingToFind);
		replaceAllButton.setEnabled(somethingToFind);
	}

	private void endOfRun(String message) {
		searching = false;
		if (progressComposite.isDisposed()) {
			return;
		}

		progressComposite.setVisible(false);
		feedbackLabel.setText(message);
		dataToUI();
	}

	private void prepareToRun() {
		searching = true;
		lastFindHexButtonSelected = findGroup.hexRadioButton.getSelection();
		lastReplaceHexButtonSelected = replaceGroup.hexRadioButton.getSelection();
		replaceGroup.rememberText();
		findGroup.rememberText();
		lastForward = forwardRadioButton.getSelection();
		lastIgnoreCase = ignoreCaseCheckBox.getSelection();
		feedbackLabel.setText(Texts.FIND_REPLACE_DIALOG_MESSAGE_SEARCHING);
		dataToUI();
		activateProgressBar();
	}

	private void replace() {
		myTarget.replace(replaceGroup.textCombo.getText(), replaceGroup.hexRadioButton.getSelection());
	}
}
