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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.jcryptool.core.logging.utils.LogUtil;

import net.sourceforge.javahexeditor.plugin.editors.HexEditor;

/**
 * StyledTextContent customized for content that fills up to one page of the
 * StyledText widget. No line delimiters, content wraps lines.
 *
 * @author Jordi Bergenthal
 */
final class DisplayedContent implements StyledTextContent {

	private StringBuilder myData;
	private Set<TextChangeListener> myTextListeners;
	private int numberOfColumns = -1;
	// private int numberOfLines = -1;
	private int linesTimesColumns = -1;

	/**
	 * Create empty content for a StyledText of the specified size
	 *
	 * @param numberOfLines
	 * @param numberOfColumns
	 */
	DisplayedContent(int numberOfColumns, int numberOfLines) {
		// reserve space and account for replacements
		myData = new StringBuilder(numberOfColumns * numberOfLines * 2);
		myTextListeners = new HashSet<TextChangeListener>();
		setDimensions(numberOfColumns, numberOfLines);
	}

	@Override
	public void addTextChangeListener(TextChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Parameter 'listener' must not be null.");
		}
		myTextListeners.add(listener);
	}

	@Override
	public int getCharCount() {
		return myData.length();
	}

	@Override
	public String getLine(int lineIndex) {
		return getTextRange(lineIndex * numberOfColumns, numberOfColumns);
	}

	@Override
	public int getLineAtOffset(int offset) {
		int result = offset / numberOfColumns;
		if (result >= getLineCount()) {
			return getLineCount() - 1;
		}

		return result;
	}

	@Override
	public int getLineCount() {
		return (myData.length() - 1) / numberOfColumns + 1;
	}

	@Override
	public String getLineDelimiter() {
		return Texts.EMPTY;
	}

	@Override
	public int getOffsetAtLine(int lineIndex) {
		return lineIndex * numberOfColumns;
	}

	@Override
	public String getTextRange(int start, int length) {
		int dataLength = myData.length();
		if (start > dataLength) {
			return Texts.EMPTY;
		}

		return myData.substring(start, Math.min(dataLength, start + length));
	}

	@Override
	public void removeTextChangeListener(TextChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Cannot remove a null listener");
		}

		myTextListeners.remove(listener);
	}

	/**
	 * Replaces part of the content with new text. Works only when the new text
	 * length is the same as replaceLength (when the content's size won't change).
	 * For other cases use <code>setText()</code> or <code>shiftLines()</code>
	 * instead.
	 *
	 * @see org.eclipse.swt.custom.StyledTextContent#replaceTextRange(int, int,
	 *      java.lang.String)
	 */
	@Override
	public void replaceTextRange(int start, int replaceLength, String text) {
		int length = text.length();
		if (length != replaceLength || start + length > myData.length()) {
			return;
		}

		myData.replace(start, start + length, text);
	}

	void setDimensions(int columns, int lines) {
		numberOfColumns = columns;
		// numberOfLines = lines;
		linesTimesColumns = lines * columns;
		setText(myData.toString());
	}

	@Override
	public void setText(String text) {
		myData.setLength(0);
		myData.append(text.substring(0, Math.min(text.length(), linesTimesColumns)));

		TextChangedEvent changedEvent = new TextChangedEvent(this);
		for (TextChangeListener listener : myTextListeners) {
			listener.textSet(changedEvent);
		}
	}

	/**
	 * Shifts full lines of text and fills the new empty space with text
	 *
	 * @param text
	 *            to replace new empty lines. Its size determines the number of
	 *            lines to shift
	 * @param forward
	 *            shifts lines either forward or backward
	 */
	@SuppressWarnings("boxing")
	void shiftLines(String text, boolean forward) {
		if (text.length() == 0) {
			return;
		}

		int linesInText = (text.length() - 1) / numberOfColumns + 1;
		int currentLimit = Math.min(myData.length(), linesTimesColumns);
		TextChangingEvent event = new TextChangingEvent(this);
		event.start = forward ? 0 : currentLimit;
		event.newText = text;
		event.replaceCharCount = 0;
		event.newCharCount = text.length();
		event.replaceLineCount = 0;
		event.newLineCount = linesInText;

		for (TextChangeListener listener : myTextListeners) {
			listener.textChanging(event);
		}
		myData.insert(event.start, text);
		LogUtil.logInfo(HexEditor.ID, "Event 1: start=" + event.start + ", newCharCount=" + event.newCharCount + ", newLineCount=" + event.newLineCount);

		TextChangedEvent changedEvent = new TextChangedEvent(this);
		for (TextChangeListener listener : myTextListeners) {
			listener.textChanged(changedEvent);
		}

		event = new TextChangingEvent(this);
		// event.start = forward ? linesTimesColumns : 0;
		event.start = forward ? linesTimesColumns - 1 : 0;
		event.newText = "";
		event.replaceCharCount = linesInText * numberOfColumns - linesTimesColumns + currentLimit;
		event.newCharCount = 0;
		event.replaceLineCount = linesInText;
		event.newLineCount = 0;
		for (TextChangeListener listener : myTextListeners) {
			listener.textChanging(event);
		}

		if (forward) {
			myData.delete(linesTimesColumns, linesTimesColumns + event.replaceCharCount);
		} else {
			myData.delete(0, event.replaceCharCount);
		}
//		Log.trace(this, );
		LogUtil.logInfo(HexEditor.ID, "Event 2: start=" + event.start + ", newCharCount=" + event.newCharCount + ", newLineCount=" +event.newLineCount);

		changedEvent = new TextChangedEvent(this);
		for (TextChangeListener listener : myTextListeners) {
			listener.textChanged(changedEvent);
		}
	}
}
