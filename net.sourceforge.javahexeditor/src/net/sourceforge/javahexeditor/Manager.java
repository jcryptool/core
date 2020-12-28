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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import net.sourceforge.javahexeditor.BinaryContent.RangeSelection;
import net.sourceforge.javahexeditor.common.ResourceUtility;
import net.sourceforge.javahexeditor.common.SWTUtility;
import net.sourceforge.javahexeditor.common.TextUtility;
import net.sourceforge.javahexeditor.plugin.HexEditorPlugin;

/**
 * Manager of the javahexeditor application, either in its stand-alone or
 * Eclipse plugin version. Manages creation of widgets, and executes menu
 * actions, like "File->Save". Call {@link #createEditorPart} before any menu
 * actions.
 *
 * @author Jordi Bergenthal
 *
 */
public final class Manager {

	public static final String APPLICATION_NAME = "javahexeditor";

	// Path to the text files containing the OS and date time of the build.
	private static final String OS_PATH = "net/sourceforge/javahexeditor/Manager.os";
	private static final String VERSION_PATH = "net/sourceforge/javahexeditor/Manager.version";

	// Logic components
	private FileToucher fileToucher;

	// State
	private BinaryContent content;
	private File contentFile;

	private FindReplaceHistory findReplaceHistory;
	private FontData fontData;
	Font font;
	private List<Listener> listOfStatusChangedListeners;
	private List<SelectionListener> listOfLongListeners;

	// Visual controls
	private Shell shell;
	private Composite textsParent;
	HexTexts hexTexts;
	private StatusLine statusLine;

	private FindReplaceDialog findDialog;
	private GoToDialog goToDialog;
	private SelectBlockDialog selectBlockDialog;

	public Manager(FileToucher fileToucher) {
		if (fileToucher == null) {
			throw new IllegalArgumentException("Parameter 'fileToucher' must not be null.");
		}
		this.fileToucher = fileToucher;

	}

	/**
	 * Gets the build OS .
	 *
	 * @return The build OS, may be empty and not <code>null</code>.
	 */
	public String getBuildOS() {
		String os = ResourceUtility.loadResourceAsString(OS_PATH);
		if (os == null) {
			os = "";
		}
		return os;
	}

	/**
	 * Gets the build version in the format "OS YYYY-MM-DD HH:MM:SS".
	 *
	 * @return The build version, not empty and not <code>null</code>.
	 */
	public String getBuildVersion() {

		String version = ResourceUtility.loadResourceAsString(VERSION_PATH);
		if (version == null) {
			version = "????-??-?? ??:??:??";
		}
		return version;
	}

	/**
	 * Creates editor part of parent application. Can only be called once per
	 * Manager object.
	 *
	 * @param parent
	 *            Composite where the part will be created, not <code>null</code>.
	 */
	public HexTexts createEditorPart(Composite parent) {
		if (parent == null) {
			throw new IllegalArgumentException("Parameter 'parent' must not be null.");
		}
		if (hexTexts != null) {
			throw new IllegalStateException("Editor part exists already");
		}

		shell = parent.getShell();
		textsParent = parent;
		hexTexts = new HexTexts(textsParent, SWT.NONE);
		hexTexts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		hexTexts.setEnabled(false);

		hexTexts.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (font != null && !font.isDisposed()) {
					font.dispose();
				}
				hexTexts = null;
			}
		});
		if (fontData != null) {
			font = new Font(Display.getCurrent(), fontData);
			hexTexts.setFont(font);
		}

		hexTexts.addLongSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateStatusLineAfterLongSelection();
			}
		});
		hexTexts.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				updateStatusLineAfterModify();
			}
		});

		if (listOfStatusChangedListeners != null) {
			for (Listener listener : listOfStatusChangedListeners) {
				hexTexts.addListener(SWT.Modify, listener);
			}
			listOfStatusChangedListeners = null;
		}

		if (listOfLongListeners != null) {
			for (SelectionListener listener : listOfLongListeners) {
				hexTexts.addLongSelectionListener(listener);
			}
			listOfLongListeners = null;
		}
		return hexTexts;
	}

	/**
	 * Add a listener to changes of the 'dirty', 'insert/overwrite', 'selection' and
	 * 'canUndo/canRedo' status
	 *
	 * @param listener
	 *            the listener to be notified of changes
	 */
	public void addListener(Listener listener) {
		if (listener == null) {
			return;
		}

		if (hexTexts == null) {
			if (listOfStatusChangedListeners == null) {
				listOfStatusChangedListeners = new ArrayList<Listener>();
			}
			listOfStatusChangedListeners.add(listener);
		} else {
			hexTexts.addListener(SWT.Modify, listener);
		}
	}

	/**
	 * Adds a long selection listener. Events sent to the listener have long start
	 * and end points.
	 *
	 * @see HexTexts#addLongSelectionListener(SelectionListener)
	 * @param listener
	 *            the listener
	 * @see StyledText#addSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	public void addLongSelectionListener(SelectionListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}

		if (hexTexts == null) {
			if (listOfLongListeners == null) {
				listOfLongListeners = new ArrayList<SelectionListener>();
			}
			listOfLongListeners.add(listener);
		} else {
			hexTexts.addLongSelectionListener(listener);
		}
	}

	/**
	 * Get long selection start and end points. Helper method for long selection
	 * listeners. The start point is formed by event.width as the most significant
	 * int and event.x as the least significant int. The end point is similarly
	 * formed by event.height and event.y
	 *
	 * @param event
	 *            an event with long selection start and end points
	 * @return
	 * @see #addLongSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	public long[] getLongSelection(SelectionEvent event) {
		return new long[] { ((long) event.width) << 32 | (event.x & 0x0ffffffffL),
				((long) event.height) << 32 | (event.y & 0x0ffffffffL) };
	}

	public boolean isValid() {
		return hexTexts != null && hexTexts.isValid();
	}

	public boolean isFilled() {
		return hexTexts != null && hexTexts.getContent().length() > 0;
	}

	public boolean isEditable() {
		return hexTexts != null && hexTexts.isEditable();
	}

	/**
	 * Determines if the last action can be redone
	 *
	 * @return true: an action can be redone
	 */
	public boolean canRedo() {
		return hexTexts != null && hexTexts.canRedo();
	}

	/**
	 * Determines if the last action can be undone
	 *
	 * @return true: an action ca be undone
	 */
	public boolean canUndo() {
		return hexTexts != null && hexTexts.canUndo();
	}

	/**
	 * Creates status part of parent application.
	 *
	 * @param parent
	 *            Composite where the part will be created, not <code>null</code>.
	 * @param withLeftSeparator
	 *            so it can be put besides other status items (for plugin)
	 */
	public Composite createStatusPart(Composite parent, boolean withLeftSeparator) {
		if (parent == null) {
			throw new IllegalArgumentException("Parameter 'parent' must not be null.");
		}
		statusLine = new StatusLine(parent, SWT.NONE, withLeftSeparator);
		updateStatusLine();
		return statusLine;
	}

	/**
	 * Copies selection into clipboard
	 */
	public void doCopy() {
		if (hexTexts == null) {
			return;
		}

		hexTexts.copy();
	}

	/**
	 * Cuts selection into clipboard
	 */
	public void doCut() {
		if (hexTexts == null) {
			return;
		}

		hexTexts.cut();
	}

	/**
	 * While in insert mode, deletes the selection
	 */
	public void doDelete() {
		hexTexts.deleteSelected();
	}

	/**
	 * Open find dialog
	 */
	public void doFind() {
		if (hexTexts == null) {
			return;
		}

		if (findDialog == null) {
			findDialog = new FindReplaceDialog(textsParent.getShell());
			if (findReplaceHistory == null) {
				findReplaceHistory = new FindReplaceHistory();
			}
		}

		findDialog.open(hexTexts, findReplaceHistory);
	}

	/**
	 * Open 'go to' dialog
	 */
	public void doGoTo() {
		if (content.length() < 1L) {
			return;
		}

		if (goToDialog == null) {
			goToDialog = new GoToDialog(textsParent.getShell());
		}

		long location = goToDialog.open(hexTexts.getShell(), content.length() - 1L);
		if (location >= 0L) {
			long button = goToDialog.getButtonPressed();
			if (button == 1) {
				hexTexts.showMark(location);
			} else {
				hexTexts.selectBlock(location, location);
			}
		}
	}

	/**
	 * Open 'select block' dialog
	 */
	public void doSelectBlock() {
		if (content.length() < 1L) {
			return;
		}

		if (selectBlockDialog == null) {
			selectBlockDialog = new SelectBlockDialog(textsParent.getShell());
		}
		if (selectBlockDialog.open(hexTexts.getShell(), hexTexts.getSelection(), content.length())) {
			long start = selectBlockDialog.getFinalStartResult();
			long end = selectBlockDialog.getFinalEndResult();
			if ((start >= 0L) && (end >= 0L) && (start != end)) {
				hexTexts.selectBlock(start, end);
			}
		}
	}

	public void doOpen(File forceThisFile, boolean createNewFile, String charset) throws CoreException {
		String filePath = "";
		if (forceThisFile == null && !createNewFile) {
			FileDialog fileDialog = createFileDialog(shell, SWT.OPEN);
			filePath = fileDialog.open();
			if (filePath == null) {
				return;
			}
			forceThisFile = new File(filePath);
		}
		if (forceThisFile != null) {
			try {
				forceThisFile = forceThisFile.getCanonicalFile();
			} catch (IOException e) {
				// use non-canonical one then
			}
			filePath = forceThisFile.getAbsolutePath();
		}

		try {
			openFile(forceThisFile, charset);
		} catch (CoreException ex) {
			throw ex;
		}

		hexTexts.setFocus();
	}

	public boolean canPaste() {
		return isEditable() && hexTexts.canPaste();
	}

	/**
	 * Pastes clipboard into editor
	 */
	public void doPaste() {
		if (hexTexts == null) {
			return;
		}

		hexTexts.paste();
	}

	/**
	 * Perform save-selected-as action on selected data
	 *
	 * @param file
	 *            The file, not <code>null</code>.
	 *
	 * @throws IOException
	 *             If the operation fails
	 */
	public void doSaveSelectionAs(File file) throws IOException {
		if (isFileBeingRead(file)) {
			throw new IOException(TextUtility.format(Texts.MANAGER_SAVE_MESSAGE_CANNOT_OVERWRITE_FILE_IN_USE,
					file.getAbsolutePath()));
		}

		RangeSelection selection = hexTexts.getSelection();
		try {
			content.get(file, selection.start, selection.getLength());
		} catch (IOException ex) {
			throw new IOException(TextUtility.format(Texts.MANAGER_SAVE_MESSAGE_CANNOT_SAVE_FILE,
					file.getAbsolutePath(), ex.getMessage()));

		}
	}

	/**
	 * Selects all file contents in editor
	 */
	public void doSelectAll() {
		if (hexTexts == null) {
			return;
		}

		hexTexts.selectAll();
	}

	/**
	 * Redoes the last undone action
	 */
	public void doRedo() {
		hexTexts.redo();
	}

	/**
	 * While in insert mode, trims the selection
	 */
	public void doTrim() {
		hexTexts.deleteNotSelected();
	}

	/**
	 * Undoes the last action
	 */
	public void doUndo() {
		hexTexts.undo();
	}

	/**
	 * Gets the binary content.
	 *
	 * @return The content being edited or <code>null</code> if there is no content.
	 */
	public BinaryContent getContent() {
		return content;
	}

	/**
	 * Gets the binary content file.
	 *
	 * @return The file which represents the content being edited or
	 *         <code>null</code> if there is no file.
	 */
	public File getContentFile() {
		return contentFile;
	}

	/**
	 * Gets the current selection.
	 *
	 * @return The current selection, not <code>null</code>.
	 *
	 * @see HexTexts#getSelection()
	 */
	public RangeSelection getSelection() {
		if (hexTexts == null) {
			return new RangeSelection(0, 0);
		}

		return hexTexts.getSelection();
	}

	/**
	 * Get whether the content has been modified or not
	 *
	 * @return if changes have been performed
	 */
	public boolean isDirty() {
		if (content == null) {
			return false;
		}

		return content.isDirty();
	}

	private boolean isFileBeingRead(File file) {
		return file.equals(contentFile) || content.getOpenFiles().contains(file);
	}

	/**
	 * Tells whether the input is in overwrite or insert mode
	 *
	 * @return true: overwriting, false: inserting
	 */
	public boolean isOverwriteMode() {
		if (hexTexts == null) {
			return true;
		}

		return hexTexts.isOverwriteMode();
	}

	/**
	 * Tells whether the input has text selected
	 *
	 * @return true: text is selected, false: no text selected
	 */
	public boolean isTextSelected() {
		if (hexTexts == null) {
			return false;
		}

		return hexTexts.getSelection().getLength() > 0;
	}

	/**
	 * Open file for editing
	 *
	 * @param contentFile
	 *            the input file or <code>null</code> if this will be a new file
	 * @param charset
	 *            the charset, not <code>null</code>
	 * @throws CoreException
	 *             if the input file cannot be read
	 */
	public void openFile(File contentFile, String charset) throws CoreException {
		this.contentFile = contentFile;
		if (contentFile == null) {
			content = new BinaryContent();
		} else {
			try {
				content = new BinaryContent(contentFile);
			} catch (IOException ex) {
				this.contentFile = null;
				throw new CoreException(new Status(IStatus.ERROR, HexEditorPlugin.ID,
						TextUtility.format(Texts.MANAGER_OPEN_MESSAGE_CANNOT_OPEN_FILE, contentFile.getAbsolutePath()),
						ex));
			}
		}
		hexTexts.setCharset(charset);
		hexTexts.setContentProvider(content);
		if (contentFile == null || getContent().length() == 0) {
			hexTexts.setInsertMode(true);
		} else if (contentFile != null) {
			hexTexts.setInsertMode(false);
		}
		updateStatusLine();

	}

	/**
	 * Reuse the status line control from another manager. Useful for multiple open
	 * editors
	 *
	 * @param other
	 *            manager to copy its control from
	 */
	public void reuseStatusLinelFrom(Manager other) {
		if (other == null) {
			throw new IllegalArgumentException("Parameter 'other' must not be null.");
		}
		statusLine = other.statusLine;
	}

	/**
	 * Perform save-as action on opened file
	 *
	 * @param file
	 *            The new file, not <code>null</code>.
	 * @param monitor
	 *
	 * @throws IOException
	 *             If the operation fails
	 */
	public void saveAsFile(File file, IProgressMonitor monitor) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("Parameter 'file' must not be null.");
		}

		if (!file.equals(contentFile) && isFileBeingRead(file)) {
			throw new IOException(TextUtility.format(Texts.MANAGER_SAVE_MESSAGE_CANNOT_OVERWRITE_FILE_IN_USE,
					file.getAbsolutePath()));
		}

		try {
			content.get(file);
			content.dispose();
		} catch (IOException ex) {
			throw new IOException(TextUtility.format(Texts.MANAGER_SAVE_MESSAGE_CANNOT_SAVE_FILE,
					file.getAbsolutePath(), ex.getMessage()));
		}

		content = new BinaryContent(file);
		contentFile = file;
		fileToucher.touchFile(contentFile, monitor);

		hexTexts.setContentProvider(content);
	}

	/**
	 * Perform save action on opened file
	 * 
	 * @param monitor
	 *            the progress monitor or <code>null</code>
	 *
	 * @throws IOException
	 *             If the operation fails
	 */
	public void saveFile(IProgressMonitor monitor) throws IOException {
		saveAsFile(contentFile, monitor);
	}

	/**
	 * Sets Find/Replace combo lists pre-exisiting values.
	 *
	 * @param findReplaceHistory
	 *            The modifiable find-replace history, not <code>null</code>.
	 */
	public void setFindReplaceHistory(FindReplaceHistory findReplaceHistory) {
		if (findReplaceHistory == null) {
			throw new IllegalArgumentException("Parameter 'findReplaceHistory' must not be null.");
		}
		this.findReplaceHistory = findReplaceHistory;
	}

	/**
	 * Causes the text areas to have the keyboard focus
	 */
	public void setFocus() {
		if (hexTexts != null) {
			hexTexts.setFocus();
		}

		updateStatusLine();
	}

	private void updateStatusLine() {
		if (statusLine != null) {
			statusLine.updateInsertMode(hexTexts == null ? true : !hexTexts.isOverwriteMode());
			if (hexTexts != null && hexTexts.getContent() != null) {
				long size = hexTexts.getContent().length();
				if (hexTexts.isSelected()) {
					statusLine.updateSelectionAndSize(hexTexts.getSelection());
				} else {
					statusLine.updatePoitionAndSize(hexTexts.getCaretPos(), size);
				}
				statusLine.updateValue(hexTexts.getActualValue());
				

			} else {
				statusLine.clearValue();
				statusLine.clearSize();
			}
		}
		
	}

	public void setSelection(RangeSelection selection) {
		if (selection == null) {
			throw new IllegalArgumentException("Parameter 'selection' must not be null.");
		}
		if (hexTexts == null) {
			return;
		}
		hexTexts.setSelection(selection.start, selection.end);

	}

	/**
	 * Set the editor text font.
	 *
	 * @param aFont
	 *            new font to be used; should be a constant char width font. Use
	 *            <code>null</code> to set to the default font.
	 */
	public void setTextFont(FontData aFont) {
		fontData = aFont;
		if (Preferences.getDefaultFontData().equals(aFont)) {
			fontData = null;
		}
		// dispose it after setting new one
		// StyledTextRenderer 3.448 bug in line 994
		Font fontToDispose = font;
		font = null;
		if (hexTexts != null) {
			if (fontData != null) {
				font = new Font(Display.getCurrent(), fontData);
			}
			hexTexts.setFont(font);
		}
		if (fontToDispose != null && !fontToDispose.isDisposed()) {
			fontToDispose.dispose();
		}
	}

	/**
	 * Show a file dialog with a save-as message
	 *
	 * @param aShell
	 *            parent of the dialog
	 * @param selection
	 * @return
	 */
	public File showSaveAsDialog(Shell aShell, boolean selection) {
		FileDialog dialog = createFileDialog(aShell, SWT.SAVE);

		if (selection) {
			dialog.setText(Texts.MANAGER_SAVE_DIALOG_TITLE_SAVE_SELECTION_AS);
		} else {
			dialog.setText(Texts.MANAGER_SAVE_DIALOG_TITLE_SAVE_AS);
		}
		String filePath = dialog.open();
		if (filePath == null) {
			return null;
		}

		File file = new File(filePath);
		if (file.exists()) {
			if (SWTUtility.showMessage(aShell, SWT.ICON_WARNING | SWT.YES | SWT.NO,
					Texts.MANAGER_SAVE_DIALOG_TITLE_FILE_ALREADY_EXISTS,
					Texts.MANAGER_SAVE_DIALOG_MESSAGE_FILE_ALREADY_EXISTS, file.getAbsolutePath()) != SWT.YES) {
				return null;
			}
		}
		return file;
	}

	private FileDialog createFileDialog(Shell aShell, int style) {
		FileDialog dialog = new FileDialog(aShell, style);
		String filterPath;
		if (contentFile != null) {
			filterPath = contentFile.getParentFile().getAbsolutePath();
		} else {
			filterPath = System.getProperty("user.dir");
		}
		dialog.setFilterPath(filterPath);
		return dialog;
	}

	/**
	 * Event handler for updating the status line.
	 */
	void updateStatusLineAfterLongSelection() {
		updateStatusLine();
	}

	/**
	 * Event handler for updating the status line.
	 */
	void updateStatusLineAfterModify() {
		updateStatusLine();
	}

}
