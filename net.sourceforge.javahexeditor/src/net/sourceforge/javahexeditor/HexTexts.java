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
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;

import org.jcryptool.core.util.constants.IConstants;

import net.sourceforge.javahexeditor.BinaryContent.RangeSelection;
import net.sourceforge.javahexeditor.BinaryContentFinder.Match;
import net.sourceforge.javahexeditor.common.ByteArrayUtility;
import net.sourceforge.javahexeditor.common.SWTUtility;

/**
 * A binary file editor, composed of two synchronized displays: an hexadecimal
 * and a basic ASCII char display. The file size has no effect on the memory
 * footprint of the editor. It has binary, ASCII and Unicode find functionality.
 * Use addListener(SWT.Modify, Listener) to listen to changes of the 'dirty',
 * 'overwrite/insert', 'selection' and 'canUndo/canRedo' status.
 *
 * @author Jordi Bergenthal
 */
public final class HexTexts extends Composite {

	/**
	 * Map of displayed chars. Chars that cannot be displayed correctly are changed
	 * for a '.' char. There are differences on which chars can correctly be
	 * displayed in each operating system, charset encoding, or font system.
	 */
	public static final char[] byteToChar = new char[256]; // TODO should not be static

	static final String[] byteToHex = new String[256];

	private static final int CHARS_FOR_ADDRESS = 12; // Files up to 16 Ters: 11
	// hex digits
	// + ':'
	private final Color colorBlue = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
	private final Color colorLightShadow = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	private final Color colorNormalShadow = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	private final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final Color colorCaretLine = new Color(Display.getCurrent(), 232, 242, 254); // very light blue
	private final Color colorHighlight = new Color(Display.getCurrent(), 255, 248, 147); // mellow / yellow
	
	private static final byte[] HEX_TO_NIBBLE = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12,
			13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 10, 11, 12, 13, 14, 15 };
	private static final int MAX_SCREEN_RESOLUTION = 1920;
	private static final int MIN_CHAR_SIZE = 5;
	private static final char[] NIBBLE_TO_HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };
	private static final int SET_TEXT = 0;
	private static final int SHIFT_FORWARD = 1; // frame
	private static final int SHIFT_BACKWARD = 2;

	private int charsForFileSizeAddress = 0;
	private String charset;
	private boolean delayedInQueue = false;
	private Runnable delayedWaiting;
	boolean dragging = false;
	double fontCharWidth = -1;
	private String headerRow; // Computed based on MAX_SCREEN_RESOLUTION

	private List<Integer> highlightRangesInScreen;
	private List<Long> mergeChangeRanges;
	private List<Integer> mergeHighlightRanges;
	private int mergeIndexChange = -2;
	private int mergeIndexHighlight = -2;
	private boolean mergeRangesIsBlue = false;
	private boolean mergeRangesIsHighlight = false;
	private int mergeRangesPosition = -1;
	int myBytesPerLine = 16;
	boolean myCaretStickToStart = false; // stick to end
	BinaryContent myContent;
	BinaryContentFinder myFinder;
	boolean myInserting = false;
	private KeyListener myKeyAdapter = new MyKeyAdapter();
	int myLastFocusedTextArea = -1; // 1 or 2;
	private long myLastLocationPosition = -1L;
	private List<SelectionListener> myLongSelectionListeners;
	private long myPreviousFindEnd = -1;
	private boolean myPreviousFindIgnoredCase = false;
	private String myPreviousFindString;
	private boolean myPreviousFindStringWasHex = false;
	private int myPreviousLine = -1;
	private long myPreviousRedrawStart = -1;
	long myStart = 0L;
	long myTextAreasStart = 0L;
	long myEnd = 0L;

	private final MyTraverseAdapter myTraverseAdapter = new MyTraverseAdapter();
	int myUpANibble = 0; // always 0 or 1
	private final MyVerifyKeyAdapter myVerifyKeyAdapter = new MyVerifyKeyAdapter();
	private int numberOfLines = 16;
	private int numberOfLines_1 = numberOfLines - 1;
	private boolean stopSearching = false;
	private byte[] tmpRawBuffer = new byte[MAX_SCREEN_RESOLUTION / MIN_CHAR_SIZE / 3 * MAX_SCREEN_RESOLUTION
			/ MIN_CHAR_SIZE];
	private int verticalBarFactor = 0;

	// visual components
	private Font fontCurrent; // disposed externally
	private Font fontDefault; // disposed internally

	private Composite column0;
	private Text textSeparator0;
	private StyledText styledText0;

	private Composite column1;
	private StyledText header1Text;
	private StyledText styledText1;
	private GridData styledText1GridData;
	private GC styledText1GC;

	private Composite column2;
	private Text textSeparator2;
	private StyledText styledText2;
	private GridData styledText2GridData;
	private GC styledText2GC;

	/**
	 * Manager of the current Editor page.</br>
	 * Used by the ContextMenu to enable/disable the 
	 * commands in the context menu (right click menu).
	 */
	private Manager manager;
	

	/**
	 * compose byte-to-hex map
	 */
	private void composeByteToHexMap() {
		
		for (int i = 0; i < 256; ++i) {
			byteToHex[i] = Character.toString(NIBBLE_TO_HEX[i >>> 4]) + NIBBLE_TO_HEX[i & 0x0f];
		}
	}

	/**
	 * compose byte-to-char map
	 */
	private void composeByteToCharMap() {
		if (charset == null || styledText2 == null) {
			return;
		}

		CharsetDecoder d = Charset.forName(charset).newDecoder().onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith(".");
		ByteBuffer bb = ByteBuffer.allocate(1);
		CharBuffer cb = CharBuffer.allocate(1);
		for (int i = 0; i < 256; ++i) {
			if (i < 0x20 || i == 0x7f) {
				byteToChar[i] = '.';
			} else {
				bb.clear();
				bb.put((byte) i);
				bb.rewind();
				cb.clear();
				d.reset();
				d.decode(bb, cb, true);
				d.flush(cb);
				cb.rewind();
				char decoded = cb.get();
				// neither font metrics nor graphic context work for charset
				// 8859-1 chars between 128 and
				// 159
				String text = styledText2.getText();
				styledText2.setText("|" + decoded);
				if (styledText2.getLocationAtOffset(2).x
						- styledText2.getLocationAtOffset(1).x < styledText2.getLocationAtOffset(1).x
								- styledText2.getLocationAtOffset(0).x) {
					decoded = '.';
				}
				styledText2.setText(text);
				byteToChar[i] = decoded;
			}
		}
	}

	/**
	 * compose header row
	 */
	private void composeHeaderRow() {
		StringBuilder rowChars = new StringBuilder();
		for (int i = 0; i < MAX_SCREEN_RESOLUTION / MIN_CHAR_SIZE / 3; ++i) {
			rowChars.append(byteToHex[i & 0x0ff]).append(' ');
		}
		headerRow = rowChars.toString().toUpperCase();
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String name) {
		if ((name == null) || (name.length() == 0)) {
			name = IConstants.UTF8_ENCODING;
		}
		charset = name;
		composeByteToCharMap();
	}

	private class MyKeyAdapter extends KeyAdapter {
		public MyKeyAdapter() {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.keyCode) {
			case SWT.ARROW_UP:
			case SWT.ARROW_DOWN:
			case SWT.ARROW_LEFT:
			case SWT.ARROW_RIGHT:
			case SWT.END:
			case SWT.HOME:
			case SWT.PAGE_UP:
			case SWT.PAGE_DOWN:
				boolean selection = myStart != myEnd;
				boolean ctrlKey = (e.stateMask & SWT.CONTROL) != 0;
				boolean countNibbles = (e.widget == styledText1);
				if ((e.stateMask & SWT.SHIFT) != 0) { // shift mod2
					long newPos = doNavigateKeyPressed(ctrlKey, e.keyCode, getCaretPos(), false);
					shiftStartAndEnd(newPos);
				} else { // if no modifier or control or alt
					long position = doNavigateKeyPressed(ctrlKey, e.keyCode, getCaretPos(),
							countNibbles && !myInserting);
					setStartAndEnd(position, position);
					myCaretStickToStart = false;
				}
				ensureCaretIsVisible();
				Runnable delayed = new Runnable() {
					@Override
					public void run() {
						redrawTextAreas(false);
						runnableEnd();
					}
				};
				runnableAdd(delayed);
				notifyLongSelectionListeners();
				if (selection != (myStart != myEnd)) {
					notifyListeners(SWT.Modify, null);
				}
				e.doit = false;
				break;
			case SWT.INSERT:
				if ((e.stateMask & SWT.MODIFIER_MASK) == 0) {
					redrawCaret(true);
				} else if (e.stateMask == SWT.SHIFT) {
					paste();
				} else if (e.stateMask == SWT.CONTROL) {
					copy();
				}
				break;
			case 'a':
				if (e.stateMask == SWT.CONTROL) {
					selectAll();
				}
				break;
			case 'c':
				if (e.stateMask == SWT.CONTROL) {
					copy();
				}
				break;
			case 'v':
				if (e.stateMask == SWT.CONTROL) {
					paste();
				}
				break;
			case 'x':
				if (e.stateMask == SWT.CONTROL) {
					cut();
				}
				break;
			case 'y':
				if (e.stateMask == SWT.CONTROL) {
					redo();
				}
				break;
			case 'z':
				if (e.stateMask == SWT.CONTROL) {
					undo();
				}
				break;
			default:
				break;
			}
		}
	}

	private class MyMouseAdapter extends MouseAdapter {
		int charLen;

		public MyMouseAdapter(boolean hexContent) {
			charLen = 1;
			if (hexContent) {
				charLen = 3;
			}
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (e.button == 1) {
				dragging = true;
			}
			int textOffset = 0;
			try {
				textOffset = SWTUtility.getOffsetAtPoint(((StyledText) e.widget), new Point(e.x, e.y));
			} catch (IllegalArgumentException ex) {
				textOffset = ((StyledText) e.widget).getCharCount();
			}
			if (textOffset < 0) {
				return;
			}
			int byteOffset = textOffset / charLen;
			((StyledText) e.widget).setTopIndex(0);
			if (e.button == 1 && (e.stateMask & SWT.MODIFIER_MASK & ~SWT.SHIFT) == 0) {// no
				// modifier or shift
				if ((e.stateMask & SWT.MODIFIER_MASK) == 0) {
					myCaretStickToStart = false;
					long position = myTextAreasStart + byteOffset;
					setStartAndEnd(position, position);
				} else { // shift
					shiftStartAndEnd(myTextAreasStart + byteOffset);
				}
				refreshCaretsPosition();
				setFocus();
				refreshSelections();
				notifyListeners(SWT.Modify, null);
				notifyLongSelectionListeners();
			}
		}

		@Override
		public void mouseUp(MouseEvent e) {
			if (e.button == 1) {
				dragging = false;
			}
		}
	}

	/**
	 * Draws the separate lines on the right of every 8 byte block in the hex area.
	 */
	private class MyPaintAdapter1 implements PaintListener {

		@Override
		public void paintControl(PaintEvent event) {
			event.gc.setBackground(colorLightShadow);
			int xPos;
			for (int block = 8; block <= myBytesPerLine; block += 8) {
				 xPos = (int) (3 * block * fontCharWidth);
				event.gc.fillRectangle((int) (xPos - fontCharWidth), event.y, (int) fontCharWidth, event.y + event.height);
			}
		}
	}

	/**
	 * Draws the separate lines on the right of every 8 byte block in the text area.
	 */
	private class MyPaintAdapter2 implements PaintListener {

		@Override
		public void paintControl(PaintEvent event) {
			event.gc.setForeground(colorLightShadow);
			event.gc.setLineWidth(1);

			for (int block = 8; block <= myBytesPerLine; block += 8) {
				int xPos = (int) (block * fontCharWidth);
				event.gc.drawLine(xPos, event.y, xPos, event.y + event.height);
			}
		}
	}

	private class MySelectionAdapter extends SelectionAdapter {
		int charLen;

		public MySelectionAdapter(boolean hexContent) {
			charLen = 1;
			if (hexContent) {
				charLen = 3;
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (!dragging) {
				return;
			}

			boolean selection = myStart != myEnd;
			int lower = e.x / charLen;
			int higher = e.y / charLen;
			int caretPos = ((StyledText) e.widget).getCaretOffset() / charLen;
			myCaretStickToStart = caretPos < higher || caretPos < lower;
			if (lower > higher) {
				lower = higher;
				higher = e.x / charLen;
			}

			select(myTextAreasStart + lower, myTextAreasStart + higher);
			if (selection != (myStart != myEnd)) {
				notifyListeners(SWT.Modify, null);
			}

			redrawTextAreas(false);
		}
	}

	private class MyTraverseAdapter implements TraverseListener {
		public MyTraverseAdapter() {

		}

		@Override
		public void keyTraversed(TraverseEvent e) {
			if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
				e.doit = true;
			}
		}
	}

	private class MyVerifyKeyAdapter implements VerifyKeyListener {
		public MyVerifyKeyAdapter() {
		}

		@Override
		public void verifyKey(VerifyEvent e) {
			// Log.log(this, "verifyKey={0}", e);
			if ((e.character == SWT.DEL || e.character == SWT.BS) && myInserting) {
				if (!deleteSelected()) {
					if (e.character == SWT.BS) {
						long newStart = myStart + myUpANibble;
						long newEnd = myEnd;
						if (newStart > 0L) {
							myContent.delete(newStart - 1L, 1L);
							newEnd = --newStart;
						}
						setStartAndEnd(newStart, newEnd);
					} else { // e.character == SWT.DEL
						myContent.delete(myStart, 1L);
					}
					ensureWholeScreenIsVisible();
					ensureCaretIsVisible();
					Runnable delayed = new Runnable() {
						@Override
						public void run() {
							redrawTextAreas(true);
							runnableEnd();
						}
					};
					runnableAdd(delayed);
					updateScrollBar();

					notifyListeners(SWT.Modify, null);
					notifyLongSelectionListeners();
				}
				myUpANibble = 0;
			} else {
				doModifyKeyPressed(e);
			}

			e.doit = false;
		}
	}

	private final class MyFinderRunnable implements Runnable {
		private Match match;

		public MyFinderRunnable() {

		}

		@Override
		public void run() {
			match = myFinder.getNextMatch();
		}

		public Match getMatch() {
			if (match == null) {
				throw new IllegalStateException("Field 'match' must not be null.");
			}
			return match;
		}
	}

	/**
	 * Create a binary text editor
	 *
	 * @param parent
	 *            parent in the widget hierarchy
	 * @param style
	 *            not used for the moment
	 */
	public HexTexts(final Composite parent, int style, Manager mngr) {
		super(parent, style | SWT.BORDER | SWT.V_SCROLL);
		
		manager = mngr;

		highlightRangesInScreen = new ArrayList<Integer>();

		composeByteToHexMap();
		composeHeaderRow();

		myLongSelectionListeners = new ArrayList<SelectionListener>();
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				colorCaretLine.dispose();
				colorHighlight.dispose();
				if (fontDefault != null && !fontDefault.isDisposed()) {
					fontDefault.dispose();
				}
			}
		});
		initialize();
		myLastFocusedTextArea = 1;
		myPreviousLine = -1;
	}

	/**
	 * redraw the caret with respect of Inserting/Overwriting mode
	 *
	 * @param focus
	 */
	public void redrawCaret(boolean focus) {
		drawUnfocusedCaret(false);
		setInsertMode(focus ? (!myInserting) : myInserting);
		if (myInserting && myUpANibble != 0) {
			myUpANibble = 0;
			refreshCaretsPosition();
			if (focus) {
				setFocus();
			}
		} else {
			drawUnfocusedCaret(true);
		}
		if (focus) {
			notifyListeners(SWT.Modify, null);
		}
	}

	/**
	 * Adds a long selection listener. Events sent to the listener have long start
	 * and end points. The start point is formed by event.width as the most
	 * significant int and event.x as the least significant int. The end point is
	 * similarly formed by event.height and event.y A listener can obtain the long
	 * selection with this code: getLongSelection(SelectionEvent) long start =
	 * ((long)event.width) << 32 | (event.x & 0x0ffffffffL) Similarly for the end
	 * point: long end = ((long)event.height) << 32 | (event.y & 0x0ffffffffL)
	 *
	 * @param listener
	 *            the listener
	 * @see StyledText#addSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	public void addLongSelectionListener(SelectionListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("Parameter 'listener' must not be null.");
		}
		if (!myLongSelectionListeners.contains(listener)) {
			myLongSelectionListeners.add(listener);
		}
	}

	/**
	 * This method initializes composite
	 */
	private void initialize() {
		GridLayout mainGridLayout = new GridLayout();
		mainGridLayout.numColumns = 3;
		mainGridLayout.marginHeight = 0;
		mainGridLayout.verticalSpacing = 0;
		mainGridLayout.horizontalSpacing = 0;
		mainGridLayout.marginWidth = 0;
		setLayout(mainGridLayout);

		Display display = Display.getCurrent();
		fontDefault = new Font(display, Preferences.getDefaultFontData());
		fontCurrent = fontDefault;

		column0 = new Composite(this, SWT.NONE);
		GridLayout column0Layout = new GridLayout();
		column0Layout.marginHeight = 0;
		column0Layout.verticalSpacing = 0;
		column0Layout.horizontalSpacing = 0;
		column0Layout.marginWidth = 0;
		column0.setLayout(column0Layout);
		column0.setBackground(colorLightShadow);
		GridData gridDataColumn = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
		column0.setLayoutData(gridDataColumn);

		GridData gridDataTextSeparator = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gridDataTextSeparator.widthHint = 10;
		textSeparator0 = new Text(column0, SWT.SEPARATOR);
		textSeparator0.setEnabled(false);
		textSeparator0.setBackground(colorLightShadow);
		textSeparator0.setLayoutData(gridDataTextSeparator);

		styledText0 = new StyledText(column0, SWT.MULTI | SWT.READ_ONLY);
		styledText0.setEditable(false);
		styledText0.setEnabled(false);
		styledText0.setBackground(colorLightShadow);
		styledText0.setForeground(black);
		styledText0.setFont(fontCurrent);

		GC styledTextGC = new GC(styledText0);
		fontCharWidth = SWTUtility.getAverageCharacterWidth(styledTextGC);
		styledTextGC.dispose();

		GridData gridDataAddresses = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
		gridDataAddresses.heightHint = numberOfLines * styledText0.getLineHeight();
		styledText0.setLayoutData(gridDataAddresses);
		setAddressesGridDataWidthHint();
		styledText0.setContent(new DisplayedContent(CHARS_FOR_ADDRESS, numberOfLines));

		column1 = new Composite(this, SWT.NONE);
		GridLayout column1Layout = new GridLayout();
		column1Layout.marginHeight = 0;
		column1Layout.verticalSpacing = 0;
		column1Layout.horizontalSpacing = 0;
		column1Layout.marginWidth = 0;
		column1.setLayout(column1Layout);
		column1.setBackground(colorLightShadow);

		GridData gridDataColumn1 = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
		column1.setLayoutData(gridDataColumn1);

		header1Text = new StyledText(column1, SWT.SINGLE | SWT.READ_ONLY);
		GridData gridData_header1Text = new GridData();
		gridData_header1Text.horizontalIndent = 0; // because of small line left
		header1Text.setLayoutData(gridData_header1Text);
		header1Text.setEditable(false);
		header1Text.setEnabled(false);
		header1Text.setForeground(black);
		header1Text.setBackground(colorLightShadow);
		header1Text.setFont(fontCurrent);
		refreshHeader();

		styledText1 = new StyledText(column1, SWT.MULTI);
		styledText1.setFont(fontCurrent);
		styledText1GC = new GC(styledText1);

		styledText1GridData = new GridData(SWT.DEFAULT, SWT.FILL, false, true);
		styledText1.setLayoutData(styledText1GridData);
		styledText1.addKeyListener(myKeyAdapter);
		FocusListener myFocusAdapter = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				drawUnfocusedCaret(false);
				myLastFocusedTextArea = 1;
				if (e.widget == styledText2) {
					myLastFocusedTextArea = 2;
				}
				getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						drawUnfocusedCaret(true);
					}
				});
			}
		};
		styledText1.addFocusListener(myFocusAdapter);
		styledText1.addMouseListener(new MyMouseAdapter(true));
		styledText1.addPaintListener(new MyPaintAdapter1());
		styledText1.addTraverseListener(myTraverseAdapter);
		styledText1.addVerifyKeyListener(myVerifyKeyAdapter);
		styledText1.setContent(new DisplayedContent(myBytesPerLine * 3, numberOfLines));
		styledText1.setDoubleClickEnabled(false);
		styledText1.addSelectionListener(new MySelectionAdapter(true));
		// StyledText.setCaretOffset() version 3.448 bug resets the caret size
		// if using the default one,
		// so we use not the default one.
		Caret defaultCaret = styledText1.getCaret();
		Caret nonDefaultCaret = new Caret(defaultCaret.getParent(), defaultCaret.getStyle());
		nonDefaultCaret.setBounds(defaultCaret.getBounds());
		styledText1.setCaret(nonDefaultCaret);
		
		// This creates the context menu on the hex area.
		// TODO here I work.
//		styledText1.setMenu(new ContextMenu(styledText1, manager));
		
		ContextMenu.createMenuForText(styledText1, manager);
		
//		Menu contextMenu = new Menu(styledText1);
//		contextMenu.addMenuListener(new MenuListener() {
//			
//			@Override
//			public void menuShown(MenuEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void menuHidden(MenuEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
//		styledText1.setMenu(contextMenu);
//		styledText1
		
//		MenuItem mi1 = new MenuItem(contextMenu, SWT.None);
//		mi1.setText("Rechtsklick9");
//		mi1.setMenu(contextMenu);
//		mi1.

		column2 = new Composite(this, SWT.NONE);
		GridLayout column2Layout = new GridLayout();
		column2Layout.marginHeight = 0;
		column2Layout.verticalSpacing = 0;
		column2Layout.horizontalSpacing = 0;
		column2Layout.marginWidth = 0;
		column2.setLayout(column2Layout);
		column2.setBackground(styledText1.getBackground());
		GridData gridDataColumn2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		column2.setLayoutData(gridDataColumn2);

		textSeparator2 = new Text(column2, SWT.SEPARATOR);
		textSeparator2.setEnabled(false);
		textSeparator2.setBackground(colorLightShadow);
		textSeparator2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		makeFirstRowSameHeight();

		styledText2 = new StyledText(column2, SWT.MULTI);
		styledText2.setFont(fontCurrent);
		styledText2GridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		styledText2.setLayoutData(styledText2GridData);
		styledText2.addKeyListener(myKeyAdapter);
		styledText2.addFocusListener(myFocusAdapter);
		styledText2.addMouseListener(new MyMouseAdapter(false));
		styledText2.addPaintListener(new MyPaintAdapter2());
		styledText2.addTraverseListener(myTraverseAdapter);
		styledText2.addVerifyKeyListener(myVerifyKeyAdapter);
		styledText2.setContent(new DisplayedContent(myBytesPerLine, numberOfLines));
		styledText2.setDoubleClickEnabled(false);
		styledText2.addSelectionListener(new MySelectionAdapter(false));
		// StyledText.setCaretOffset() version 3.448 bug resets the caret size
		// if using the default one,
		// so we use not the default one.
		defaultCaret = styledText2.getCaret();
		nonDefaultCaret = new Caret(defaultCaret.getParent(), defaultCaret.getStyle());
		nonDefaultCaret.setBounds(defaultCaret.getBounds());
		styledText2.setCaret(nonDefaultCaret);
		styledText2GC = new GC(styledText2);
		setCharset(null);
		
		ContextMenu.createMenuForText(styledText2, manager);
		
		// TODO: For debugging layout issues
		// styledText2.setBackground(colorBlue);
		adaptWidthToBytesPerLine();

		super.setFont(fontCurrent);
		ScrollBar vertical = getVerticalBar();
		vertical.setSelection(0);
		vertical.setMinimum(0);
		vertical.setIncrement(1);
		vertical.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				e.doit = false;
				long previousStart = myTextAreasStart;
				myTextAreasStart = getVerticalBar().getSelection();
				myTextAreasStart = (myTextAreasStart << verticalBarFactor) * myBytesPerLine;
				if (previousStart == myTextAreasStart) {
					return;
				}

				Runnable delayed = new Runnable() {
					@Override
					public void run() {
						redrawTextAreas(false);
						setFocus();
						runnableEnd();
					}
				};
				runnableAdd(delayed);
			}
		});
		updateScrollBar();
		addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
			@Override
			public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
				setFocus();
			}
		});
		addControlListener(new org.eclipse.swt.events.ControlAdapter() {
			@Override
			public void controlResized(org.eclipse.swt.events.ControlEvent e) {
				updateTextsMetrics();
			}
		});
		addDisposeListener(new org.eclipse.swt.events.DisposeListener() {
			@Override
			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
				if (myContent != null) {
					myContent.dispose();
				}
			}
		});
	}

	/**
	 * Calculate the width of the styled text containing the hex values 
	 * and the styled text containing the utf-8 values.
	 */
	private void adaptWidthToBytesPerLine() {
		int width = (int) (((myBytesPerLine * 3) -1 ) * fontCharWidth);
		styledText1GridData.widthHint = styledText1.computeTrim(0, 0, width, 0).width;

		width = (int) ((myBytesPerLine - 1) * fontCharWidth + 1); // one pixel for caret in last column
		styledText2GridData.widthHint = styledText2.computeTrim(0, 0, width, 0).width;
	}

	public boolean isValid() {
		return myContent != null;
	}

	public boolean isEditable() {
		return myContent != null;
	}

	/**
	 * Tells whether the last action can be redone
	 *
	 * @return true: an action can be redone
	 */
	public boolean canRedo() {
		return myContent != null && myContent.canRedo();
	}

	/**
	 * Tells whether the last action can be undone
	 *
	 * @return true: an action can be undone
	 */
	public boolean canUndo() {
		return myContent != null && myContent.canUndo();
	}

	/**
	 * Copies the selection into the clipboard. If nothing is selected leaves the
	 * clipboard with its current contents. The clipboard will hold text data (for
	 * pasting into a text editor) and binary data (internal for HexText). Text data
	 * is limited to 4Mbytes, binary data is limited by disk space.
	 */
	public void copy() {
		if (myStart >= myEnd) {
			return;
		}


		long length = myEnd - myStart;
		boolean toBigforClipboard = false;
		if (length > (4 * 1024 * 1024)) {
			toBigforClipboard = true;
		}

		// Create the "copy hex or text" dialog 
		Dialog d = new CopyDialog(Display.getCurrent().getActiveShell(), toBigforClipboard);
		int returnValue = d.open();

		if (returnValue == 0 || returnValue == 1) {
			// 1 is returned if the user closed the dialog 
			// Do nothing
		} else if (returnValue == 2) {
			// The user pressed "hex"
			// Copy the hex values to the clipboard
			ClipboardHelper.setHexContentToClipboard(myContent, myStart, length);
		} else  if (returnValue == 3) {
			// The user pressed "utf8"
			// copy the text representation to the clipboard.
			ClipboardHelper.setContentsText(myContent, myStart, length);
		}
	}

	private StringBuilder cookAddresses(long address, int limit) {
		StringBuilder theText = new StringBuilder();
		for (int i = 0; i < limit; i += myBytesPerLine, address += myBytesPerLine) {
			boolean indenting = true;
			for (int j = (CHARS_FOR_ADDRESS - 2) * 4; j > 0; j -= 4) {
				int nibble = ((int) (address >>> j)) & 0x0f;
				if (nibble != 0) {
					indenting = false;
				}
				if (indenting) {
					if (j >= (charsForFileSizeAddress * 4)) {
						theText.append(' ');
					} else {
						theText.append('0');
					}
				} else {
					theText.append(NIBBLE_TO_HEX[nibble]);
				}
			}
			theText.append(NIBBLE_TO_HEX[((int) address) & 0x0f]).append(':');
		}

		return theText;
	}

	private StringBuilder cookTexts(boolean hex, int length) {
		if (length > tmpRawBuffer.length) {
			length = tmpRawBuffer.length;
		}
		StringBuilder result;

		if (hex) {
			result = new StringBuilder(length * 3);
			for (int i = 0; i < length; ++i) {
				result.append(byteToHex[tmpRawBuffer[i] & 0x0ff]).append(' ');
			}
		} else {
			result = new StringBuilder(length);
			for (int i = 0; i < length; ++i) {
				result.append(byteToChar[tmpRawBuffer[i] & 0x0ff]);
			}
		}

		return result;
	}

	/**
	 * Calls copy();deleteSelected();
	 *
	 * @see #copy() #deleteSelected()
	 */
	public void cut() {
		copy();
		deleteSelected();
	}

	private void setStartAndEnd(long start, long end) {

		if (start < 0) {
			throw new IllegalArgumentException(
					"Parameter start must not be negative. Specifed value is " + start + ".");
		}
		if (end < 0) {
			throw new IllegalArgumentException("Parameter end must not be negative. Specifed value is " + end + ".");
		}
		myStart = start;
		myEnd = end;

	}

	/**
	 * While in insert mode, trims the selection
	 *
	 * @return did delete something
	 */
	public boolean deleteNotSelected() {
		if (!myInserting || myStart < 1L && myEnd >= myContent.length()) {
			return false;
		}

		myContent.delete(myEnd, myContent.length() - myEnd);
		myContent.delete(0L, myStart);
		setStartAndEnd(0, myContent.length());

		myUpANibble = 0;
		ensureWholeScreenIsVisible();
		restoreStateAfterModify();

		return true;
	}

	/**
	 * While in insert mode, deletes the selection
	 *
	 * @return did delete something
	 */
	public boolean deleteSelected() {
		if (!handleSelectedPreModify()) {
			return false;
		}
		myUpANibble = 0;
		ensureWholeScreenIsVisible();
		restoreStateAfterModify();

		return true;
	}

	void doModifyKeyPressed(KeyEvent event) {
		char aChar = event.character;
		if (aChar == '\0' || aChar == '\b' || aChar == '\u007f' || event.stateMask == SWT.CTRL
				|| event.widget == styledText1 && ((event.stateMask & SWT.MODIFIER_MASK) != 0 || aChar < '0'
						|| aChar > '9' && aChar < 'A' || aChar > 'F' && aChar < 'a' || aChar > 'f')) {
			return;
		}

		if (getCaretPos() == myContent.length() && !myInserting) {
			ensureCaretIsVisible();
			redrawTextAreas(false);
			return;
		}
		handleSelectedPreModify();
		try {
			if (myInserting) {
				if (event.widget == styledText2) {
					myContent.insert((byte) aChar, getCaretPos());
				} else if (myUpANibble == 0) {
					myContent.insert((byte) (HEX_TO_NIBBLE[aChar - '0'] << 4), getCaretPos());
				} else {
					myContent.overwrite(HEX_TO_NIBBLE[aChar - '0'], 4, 4, getCaretPos());
				}
			} else {
				if (event.widget == styledText2) {
					myContent.overwrite((byte) aChar, getCaretPos());
				} else {
					myContent.overwrite(HEX_TO_NIBBLE[aChar - '0'], myUpANibble * 4, 4, getCaretPos());
				}
				myContent.get(ByteBuffer.wrap(tmpRawBuffer, 0, 1), null, getCaretPos());
				int offset = (int) (getCaretPos() - myTextAreasStart);
				styledText1.replaceTextRange(offset * 3, 2, byteToHex[tmpRawBuffer[0] & 0x0ff]);
				styledText1.setStyleRange(new StyleRange(offset * 3, 2, colorBlue, null));
				styledText2.replaceTextRange(offset, 1, Character.toString(byteToChar[tmpRawBuffer[0] & 0x0ff]));
				styledText2.setStyleRange(new StyleRange(offset, 1, colorBlue, null));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		long position = incrementPosWithinLimits(getCaretPos(), event.widget == styledText1);
		setStartAndEnd(position, position);

		Runnable delayed = new Runnable() {
			@Override
			public void run() {
				ensureCaretIsVisible();
				redrawTextAreas(false);
				if (myInserting) {
					updateScrollBar();
					redrawTextAreas(true);
				}
				refreshSelections();
				runnableEnd();
			}
		};
		runnableAdd(delayed);
		notifyListeners(SWT.Modify, null);
		notifyLongSelectionListeners();
	}

	long doNavigateKeyPressed(boolean ctrlKey, int keyCode, long oldPos, boolean countNibbles) {
		if (!countNibbles) {
			myUpANibble = 0;
		}
		switch (keyCode) {
		case SWT.ARROW_UP:
			if (oldPos >= myBytesPerLine) {
				oldPos -= myBytesPerLine;
			}
			break;

		case SWT.ARROW_DOWN:
			if (oldPos <= myContent.length() - myBytesPerLine) {
				oldPos += myBytesPerLine;
			}
			if (countNibbles && oldPos == myContent.length()) {
				myUpANibble = 0;
			}
			break;

		case SWT.ARROW_LEFT:
			if (countNibbles && (oldPos > 0 || oldPos == 0 && myUpANibble > 0)) {
				if (myUpANibble == 0) {
					--oldPos;
				}
				myUpANibble ^= 1; // 1->0, 0->1
			}
			if (!countNibbles && oldPos > 0) {
				--oldPos;
			}
			break;

		case SWT.ARROW_RIGHT:
			oldPos = incrementPosWithinLimits(oldPos, countNibbles);
			break;

		case SWT.END:
			if (ctrlKey) {
				oldPos = myContent.length();
			} else {
				oldPos = oldPos - oldPos % myBytesPerLine + myBytesPerLine - 1L;
				if (oldPos >= myContent.length()) {
					oldPos = myContent.length();
				}
			}
			myUpANibble = 0;
			if (countNibbles && oldPos < myContent.length()) {
				myUpANibble = 1;
			}
			break;

		case SWT.HOME:
			if (ctrlKey) {
				oldPos = 0;
			} else {
				oldPos = oldPos - oldPos % myBytesPerLine;
			}
			myUpANibble = 0;
			break;

		case SWT.PAGE_UP:
			if (oldPos >= myBytesPerLine) {
				oldPos = oldPos - myBytesPerLine * numberOfLines_1;
				if (oldPos < 0L) {
					oldPos = (oldPos + myBytesPerLine * numberOfLines_1) % myBytesPerLine;
				}
			}
			break;

		case SWT.PAGE_DOWN:
			if (oldPos <= myContent.length() - myBytesPerLine) {
				oldPos = oldPos + myBytesPerLine * numberOfLines_1;
				if (oldPos > myContent.length()) {
					oldPos = oldPos - ((oldPos - 1 - myContent.length()) / myBytesPerLine + 1) * myBytesPerLine;
				}
			}
			if (countNibbles && oldPos == myContent.length()) {
				myUpANibble = 0;
			}
			break;
		}

		return oldPos;
	}

	void drawUnfocusedCaret(boolean visible) {
		if (styledText1.isDisposed()) {
			return;
		}

		GC unfocusedGC = null;
		Caret unfocusedCaret = null;
		int chars = 0;
		int shift = 0;
		if (myLastFocusedTextArea == 1) {
			unfocusedCaret = styledText2.getCaret();
			unfocusedGC = styledText2GC;
		} else {
			unfocusedCaret = styledText1.getCaret();
			unfocusedGC = styledText1GC;
			chars = 1;
			if (styledText1.getCaretOffset() % 3 == 1) {
				shift = -1;
			}
		}
		if (unfocusedCaret.getVisible()) {
			Rectangle unfocused = unfocusedCaret.getBounds();
			unfocusedGC.setForeground(visible ? colorNormalShadow : colorCaretLine);
			unfocusedGC.drawRectangle(unfocused.x + shift * unfocused.width, unfocused.y, unfocused.width << chars,
					unfocused.height - 1);
		}
	}

	void ensureCaretIsVisible() {
		long caretPos = getCaretPos();
		long posInLine = caretPos % myBytesPerLine;

		if (myTextAreasStart > caretPos) {
			myTextAreasStart = caretPos - posInLine;
		} else if (myTextAreasStart + myBytesPerLine * numberOfLines < caretPos
				|| myTextAreasStart + myBytesPerLine * numberOfLines == caretPos && caretPos != myContent.length()) {
			myTextAreasStart = caretPos - posInLine - myBytesPerLine * numberOfLines_1;
			if (caretPos == myContent.length() && posInLine == 0) {
				myTextAreasStart = caretPos - myBytesPerLine * numberOfLines;
			}
			if (myTextAreasStart < 0L) {
				myTextAreasStart = 0L;
			}
		} else {

			return;
		}
		getVerticalBar().setSelection((int) ((myTextAreasStart / myBytesPerLine) >>> verticalBarFactor));
	}

	void ensureWholeScreenIsVisible() {
		if (myTextAreasStart + myBytesPerLine * numberOfLines > myContent.length()) {
			myTextAreasStart = myContent.length() - (myContent.length() - 1L) % myBytesPerLine - 1L
					- myBytesPerLine * numberOfLines_1;
		}

		if (myTextAreasStart < 0L) {
			myTextAreasStart = 0L;
		}
	}

	/**
	 * Performs a find on the text and sets the selection accordingly. The find
	 * starts at the current caret position.
	 *
	 * @param findString
	 *            the literal to find
	 * @param isHexString
	 *            consider the literal as an hex string (ie. "0fdA1"). Used for
	 *            binary finds. Will search full bytes only, odd number of hex
	 *            characters will have a leading '0' added.
	 * @param searchForward
	 *            look for matches after current position
	 * @param ignoreCase
	 *            match upper case with lower case characters
	 * @return whether a match was found
	 */
	public Match findAndSelect(String findString, boolean isHexString, boolean searchForward, boolean ignoreCase)
			throws NumberFormatException {
		if (findString == null) {
			throw new IllegalArgumentException("Parameter 'findString' must not be null.");
		}
		Match result = findAndSelectInternal(findString, isHexString, searchForward, ignoreCase, true);

		return result;
	}

	// Used by "find" and by "replace".
	private Match findAndSelectInternal(String findString, boolean isHexString, boolean searchForward,
			boolean ignoreCase, boolean updateGui) throws NumberFormatException {
		if (findString == null) {
			throw new IllegalArgumentException("Parameter 'findString' must not be null.");
		}

		initFinder(findString, isHexString, searchForward, ignoreCase);
		MyFinderRunnable finderRunnable = new MyFinderRunnable();
		SWTUtility.blockUntilFinished(finderRunnable);
		Match match = finderRunnable.getMatch();
		if (match.getException() != null) {
			return match;
		}

		if (match.isFound()) {
			myCaretStickToStart = false;
			if (updateGui) {
				setSelection(match.getStartPosition(), match.getEndPosition());
			} else {
				select(match.getStartPosition(), match.getEndPosition());
			}
			myPreviousFindEnd = getCaretPos();
		}

		return match;
	}

	/**
	 * Get caret position in file, which can be out of view
	 *
	 * @return the current caret position
	 */
	public long getCaretPos() {
		if (myCaretStickToStart) {
			return myStart;
		}
		return myEnd;
	}

	public byte getActualValue() {
		return getValue(getCaretPos());
	}

	public byte getValue(long pos) {
		if (myContent == null) {
			return -1;
		}
		try {
			myContent.get(ByteBuffer.wrap(tmpRawBuffer, 0, 1), null, pos);
		} catch (IOException ex) {
			throw new RuntimeException("Unexpected IO error at position " + pos, ex);
		}
		return tmpRawBuffer[0];
	}

	/**
	 * Get the binary content
	 *
	 * @return the content being edited
	 */
	public BinaryContent getContent() {
		return myContent;
	}

	private void getHighlightRangesInScreen(long start, int length) {
		highlightRangesInScreen.clear();
		if (myLastLocationPosition >= start && myLastLocationPosition < start + length) {
			highlightRangesInScreen.add((int) (myLastLocationPosition - myTextAreasStart));
			highlightRangesInScreen.add(1);
		}
	}

	/**
	 * Gets the selection start and end points as long values
	 *
	 * @return 2 elements long array, first one the start point (inclusive), second
	 *         one the end point (exclusive)
	 */
	public RangeSelection getSelection() {
		return new RangeSelection(myStart, myEnd);
	}

	public boolean isSelected() {
		return (myStart != myEnd);
	}

	private boolean handleSelectedPreModify() {
		if (myStart == myEnd || !myInserting) {
			return false;
		}

		myContent.delete(myStart, myEnd - myStart);
		myEnd = myStart;

		return true;
	}

	private long incrementPosWithinLimits(long oldPos, boolean countNibbles) {
		if (oldPos < myContent.length()) {
			if (countNibbles) {
				if (myUpANibble > 0) {
					++oldPos;
				}
				myUpANibble ^= 1; // 1->0, 0->1
			} else {
				++oldPos;
			}
		}

		return oldPos;
	}

	/**
	 * 
	 * @param findString
	 * @param isHexString
	 * @param searchForward
	 * @param ignoreCase
	 * @throws NumberFormatException
	 *             if the replace string is not a valid hex string
	 */
	private void initFinder(String findString, boolean isHexString, boolean searchForward, boolean ignoreCase)
			throws NumberFormatException {
		if (!searchForward) {
			myCaretStickToStart = true;
		}
		if (myFinder == null || !findString.equals(myPreviousFindString) || isHexString != myPreviousFindStringWasHex
				|| ignoreCase != myPreviousFindIgnoredCase) {
			myPreviousFindString = findString;
			myPreviousFindStringWasHex = isHexString;
			myPreviousFindIgnoredCase = ignoreCase;

			if (isHexString) {
				byte[] byteArray = ByteArrayUtility.parseString(findString);
				myFinder = new BinaryContentFinder(byteArray, myContent);
			} else {
				myFinder = new BinaryContentFinder(findString, myContent);
				if (ignoreCase) {
					myFinder.setCaseSensitive(false);
				}
			}
			myFinder.setNewStart(getCaretPos());
		}
		if (myPreviousFindEnd != getCaretPos()) {
			myFinder.setNewStart(getCaretPos());
		}
		myFinder.setDirectionForward(searchForward);
	}

	/**
	 * Tells whether the input is in overwrite or insert mode
	 *
	 * @return true: overwriting, false: inserting
	 */
	public boolean isOverwriteMode() {
		return !myInserting;
	}

	/**
	 *Adapts the heights of the spacers left and right of the adresses to
	 *the same height as the adresses text field.
	 */
	private void makeFirstRowSameHeight() {
		((GridData) textSeparator0.getLayoutData()).heightHint = header1Text.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		((GridData) textSeparator2.getLayoutData()).heightHint = header1Text.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
	}

	/**
	 * Merge ranges of changes in file with ranges of highlighted elements. Finds
	 * lowest range border, finds next lowest range border. That's the first result.
	 * Keeps going until last range border.
	 *
	 * @param changeRanges
	 * @param highlightRanges
	 *
	 * @return list of StyleRanges, each with a style of type 'changed',
	 *         'highlighted', or both.
	 */
	public List<StyleRange> mergeRanges(List<Long> changeRanges, List<Integer> highlightRanges) {
		if (!mergerInit(changeRanges, highlightRanges)) {
			return null;
		}
		List<StyleRange> result = new ArrayList<StyleRange>();
		mergerNext();
		int start = mergeRangesPosition;
		boolean blue = mergeRangesIsBlue;
		boolean highlight = mergeRangesIsHighlight;
		while (mergerNext()) {
			if (blue || highlight) {
				result.add(new StyleRange(start, mergeRangesPosition - start, blue ? colorBlue : null,
						highlight ? colorHighlight : null));
			}
			start = mergeRangesPosition;
			blue = mergeRangesIsBlue;
			highlight = mergeRangesIsHighlight;
		}

		return result;
	}

	private boolean mergerCatchUps() {
		boolean withinRange = false;
		if (mergeChangeRanges != null && mergeChangeRanges.size() > mergeIndexChange) {
			withinRange = true;
			if (mergerPosition(true) < mergeRangesPosition) {
				++mergeIndexChange;
			}
		}
		if (mergeHighlightRanges != null && mergeHighlightRanges.size() > mergeIndexHighlight) {
			withinRange = true;
			if (mergerPosition(false) < mergeRangesPosition) {
				++mergeIndexHighlight;
			}
		}

		return withinRange;
	}

	/**
	 * Initialize merger variables
	 *
	 * @param changeRanges
	 * @param highlightRanges
	 *
	 * @return whether the parameters hold any data
	 */
	private boolean mergerInit(List<Long> changeRanges, List<Integer> highlightRanges) {
		if ((changeRanges == null || changeRanges.size() < 2)
				&& (highlightRanges == null || highlightRanges.size() < 2)) {
			return false;
		}
		this.mergeChangeRanges = changeRanges;
		this.mergeHighlightRanges = highlightRanges;
		mergeRangesIsBlue = false;
		mergeRangesIsHighlight = false;
		mergeRangesPosition = -1;
		mergeIndexChange = 0;
		mergeIndexHighlight = 0;

		return true;
	}

	private int mergerMinimumInChangesHighlights() {
		int change = Integer.MAX_VALUE;
		if (mergeChangeRanges != null && mergeChangeRanges.size() > mergeIndexChange) {
			change = mergerPosition(true);
		}
		int highlight = Integer.MAX_VALUE;
		if (mergeHighlightRanges != null && mergeHighlightRanges.size() > mergeIndexHighlight) {
			highlight = mergerPosition(false);
		}
		int result = Math.min(change, highlight);
		if (change == result) {
			mergeRangesIsBlue = (mergeIndexChange & 1) == 0;
		}
		if (highlight == result) {
			mergeRangesIsHighlight = (mergeIndexHighlight & 1) == 0;
		}

		return result;
	}

	private boolean mergerNext() {
		++mergeRangesPosition;
		if (!mergerCatchUps()) {
			return false;
		}
		mergeRangesPosition = mergerMinimumInChangesHighlights();

		return true;
	}

	private int mergerPosition(boolean changesNotHighlights) {
		int result = -1;
		if (changesNotHighlights) {
			result = (int) (mergeChangeRanges.get(mergeIndexChange & 0xfffffffe).longValue() - myTextAreasStart);
			if ((mergeIndexChange & 1) == 1) {
				result = (int) Math.min(myBytesPerLine * numberOfLines,
						result + mergeChangeRanges.get(mergeIndexChange).longValue());
			}
		} else {
			result = mergeHighlightRanges.get(mergeIndexHighlight & 0xfffffffe).intValue();
			if ((mergeIndexHighlight & 1) == 1) {
				result += mergeHighlightRanges.get(mergeIndexHighlight).intValue();
			}
		}

		return result;
	}

	void notifyLongSelectionListeners() {
		if (myLongSelectionListeners.isEmpty()) {
			return;
		}

		Event basicEvent = new Event();
		basicEvent.widget = this;
		SelectionEvent anEvent = new SelectionEvent(basicEvent);
		anEvent.width = (int) (myStart >>> 32);
		anEvent.x = (int) myStart;
		anEvent.height = (int) (myEnd >>> 32);
		anEvent.y = (int) myEnd;

		Iterator<SelectionListener> listeners = myLongSelectionListeners.iterator();

		while (listeners.hasNext()) {
			SelectionListener aListener = listeners.next();
			aListener.widgetSelected(anEvent);
		}
	}

	public boolean canPaste() {
//		return myClipboard.hasContents();
		return ClipboardHelper.hasContents();
	}

	/**
	 * Pastes the clipboard content. The result depends on which insertion mode is
	 * currently active: Insert mode replaces the selection with the DND.CLIPBOARD
	 * clipboard contents or, if there is no selection, inserts at the current caret
	 * offset. Overwrite mode replaces contents at the current caret offset, unless
	 * pasting would overflow the content length, in which case does nothing.
	 */
	public void paste() {
		
		//TODO Add option to paste hex values
		
		if (!ClipboardHelper.hasContents()) {
			return;
		}
		
		handleSelectedPreModify();
		
		//TODO Check which type of content is in the clipboard.
		// Dann entscheiden, ob hex / Text dialog angezeigt wird oder nicht.
		// Bei Dateien wird er nicht angezeigt.
		
		Dialog d = new PasteDialog(Display.getCurrent().getActiveShell());
		int returnValue = d.open();
		
		
		long caretPos = getCaretPos();
		
		// Anzahl an BYTES die eingefuegt werden sollen
		long total = 0;
		
//		total = ClipboardHelper.tryGettingFiles(myContent, caretPos, myInserting);
		
		if (returnValue == 0 || returnValue == 1) {
			// 1 is returned if the user closed the dialog 
			// Do nothing
			return;
		} else if (returnValue == 2) {
			// The user pressed "hex"
			total = ClipboardHelper.tryGettingHex(myContent, caretPos, myInserting);
		} else  if (returnValue == 3) {
			// The user pressed "utf8"
			total = ClipboardHelper.tryGettingText(myContent, caretPos, myInserting);
		}

		
		setStartAndEnd(caretPos, caretPos + total);
		myCaretStickToStart = false;
		redrawTextAreas(true);
		restoreStateAfterModify();
	}

	/**
	 * Redoes the last undone action
	 */
	public void redo() {
		undo(false);
	}

	private void redrawTextAreas(int mode, StringBuilder newText, StringBuilder resultHex, StringBuilder resultChar,
			List<StyleRange> viewRanges) {
		styledText1.getCaret().setVisible(false);
		styledText2.getCaret().setVisible(false);
		if (mode == SET_TEXT) {
			styledText0.getContent().setText(newText.toString());
			styledText1.getContent().setText(resultHex.toString());
			styledText2.getContent().setText(resultChar.toString());
			myPreviousLine = -1;
		} else {
			boolean forward = mode == SHIFT_FORWARD;
			styledText0.setRedraw(false);
			styledText1.setRedraw(false);
			styledText2.setRedraw(false);
			((DisplayedContent) styledText0.getContent()).shiftLines(newText.toString(), forward);
			((DisplayedContent) styledText1.getContent()).shiftLines(resultHex.toString(), forward);
			((DisplayedContent) styledText2.getContent()).shiftLines(resultChar.toString(), forward);
			styledText0.setRedraw(true);
			styledText1.setRedraw(true);
			styledText2.setRedraw(true);
			if (myPreviousLine >= 0 && myPreviousLine < numberOfLines) {
				myPreviousLine += newText.length() / CHARS_FOR_ADDRESS * (forward ? 1 : -1);
			}
			if (myPreviousLine < -1 || myPreviousLine >= numberOfLines) {
				myPreviousLine = -1;
			}
		}
		if (viewRanges != null) {
			for (Iterator<StyleRange> i = viewRanges.iterator(); i.hasNext();) {
				StyleRange styleRange = i.next();
				styledText2.setStyleRange(styleRange);
				styleRange = (StyleRange) styleRange.clone();
				styleRange.start *= 3;
				styleRange.length *= 3;
				styledText1.setStyleRange(styleRange);
			}
		}
	}

	void redrawTextAreas(boolean fromScratch) {
		if (myContent == null || styledText1.isDisposed()) {
			return;
		}

		long newLinesStart = myTextAreasStart;
		int linesShifted = numberOfLines;
		int mode = SET_TEXT;
		if (!fromScratch && myPreviousRedrawStart >= 0L) {
			long lines = (myTextAreasStart - myPreviousRedrawStart) / myBytesPerLine;
			if (Math.abs(lines) < numberOfLines) {
				mode = lines > 0L ? SHIFT_BACKWARD : SHIFT_FORWARD;
				linesShifted = Math.abs((int) lines);
				if (linesShifted < 1) {
					refreshSelections();
					refreshCaretsPosition();

					return;
				}
				if (mode == SHIFT_BACKWARD) {
					newLinesStart = myTextAreasStart + (numberOfLines - (int) lines) * myBytesPerLine;
				}
			}
		}
		myPreviousRedrawStart = myTextAreasStart;

		StringBuilder newText = cookAddresses(newLinesStart, linesShifted * myBytesPerLine);

		ArrayList<Long> changeRanges = new ArrayList<Long>();
		int actuallyRead = 0;
		try {
			actuallyRead = myContent.get(ByteBuffer.wrap(tmpRawBuffer, 0, linesShifted * myBytesPerLine), changeRanges,
					newLinesStart);
		} catch (IOException e) {
			actuallyRead = 0;
		}
		StringBuilder resultHex = cookTexts(true, actuallyRead);
		StringBuilder resultChar = cookTexts(false, actuallyRead);
		getHighlightRangesInScreen(newLinesStart, linesShifted * myBytesPerLine);
		List<StyleRange> viewRanges = mergeRanges(changeRanges, highlightRangesInScreen);
		redrawTextAreas(mode, newText, resultHex, resultChar, viewRanges);
		refreshSelections();
		refreshCaretsPosition();
	}

	void refreshCaretsPosition() {
		drawUnfocusedCaret(false);
		long caretLocation = getCaretPos() - myTextAreasStart;
		if (caretLocation >= 0L && caretLocation < myBytesPerLine * numberOfLines
				|| getCaretPos() == myContent.length() && caretLocation == myBytesPerLine * numberOfLines) {
			int tmp = (int) caretLocation;
			if (tmp == myBytesPerLine * numberOfLines) {
				styledText1.setCaretOffset(tmp * 3 - 1);
				styledText2.setCaretOffset(tmp);
			} else {
				styledText1.setCaretOffset(tmp * 3 + myUpANibble);
				styledText2.setCaretOffset(tmp);
			}
			int line = styledText1.getLineAtOffset(styledText1.getCaretOffset());
			if (line != myPreviousLine) {
				if (myPreviousLine >= 0 && myPreviousLine < numberOfLines) {
					styledText1.setLineBackground(myPreviousLine, 1, null);
					styledText2.setLineBackground(myPreviousLine, 1, null);
				}
				styledText1.setLineBackground(line, 1, colorCaretLine);
				styledText2.setLineBackground(line, 1, colorCaretLine);
				myPreviousLine = line;
			}
			styledText1.getCaret().setVisible(true);
			styledText2.getCaret().setVisible(true);
			getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					drawUnfocusedCaret(true);
				}
			});
		} else {
			styledText1.getCaret().setVisible(false);
			styledText2.getCaret().setVisible(false);
		}
	}

	/**
	 * Sets the nummber of addresses in header1Text.
	 */
	private void refreshHeader() {
		header1Text.setText(headerRow.substring(0, Math.min(myBytesPerLine * 3, headerRow.length())));
	}

	void refreshSelections() {
		if (myStart >= myEnd || myStart > myTextAreasStart + myBytesPerLine * numberOfLines
				|| myEnd <= myTextAreasStart) {
			return;
		}

		long startLocation = myStart - myTextAreasStart;
		if (startLocation < 0L) {
			startLocation = 0L;
		}
		int intStart = (int) startLocation;

		long endLocation = myEnd - myTextAreasStart;
		if (endLocation > myBytesPerLine * numberOfLines) {
			endLocation = myBytesPerLine * numberOfLines;
		}
		int intEnd = (int) endLocation;

		if (myCaretStickToStart) {
			int tmp = intStart;
			intStart = intEnd;
			intEnd = tmp;
		}

		styledText1.setSelection(intStart * 3, intEnd * 3);
		styledText1.setTopIndex(0);
		styledText2.setSelection(intStart, intEnd);
		styledText2.setTopIndex(0);
	}

	/**
	 * Removes the specified selection listener
	 *
	 * @param listener
	 *
	 * @see StyledText#removeSelectionListener(org.eclipse.swt.events.SelectionListener)
	 */
	public void removeLongSelectionListener(SelectionListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}

		myLongSelectionListeners.remove(listener);
	}

	/**
	 * Replaces the selection. The result depends on which insertion mode is
	 * currently active: Insert mode replaces the selection with the replaceString
	 * or, if there is no selection, inserts at the current caret offset. Overwrite
	 * mode replaces contents at the current selection start.
	 *
	 * @param replaceString
	 *            the new string
	 * @param isHexString
	 *            consider the literal as an hex string (ie. "0fdA1"). Used for
	 *            binary finds. Will replace full bytes only, odd number of hex
	 *            characters will have a leading '0' added.
	 * @throws NumberFormatException
	 *             if the replace string is not a valid hex string
	 */
	public void replace(String replaceString, boolean isHexString) throws NumberFormatException {
		if (replaceString == null) {
			throw new IllegalArgumentException("Parameter 'replaceString' must not be null.");
		}
		handleSelectedPreModify();
		byte[] replaceData = replaceString.getBytes();
		if (isHexString) {
			replaceData = ByteArrayUtility.parseString(replaceString);
		}
		ByteBuffer newSelection = ByteBuffer.wrap(replaceData);
		if (myInserting) {
			myContent.insert(newSelection, myStart);
		} else {
			newSelection.limit((int) Math.min(newSelection.limit(), myContent.length() - myStart));
			myContent.overwrite(newSelection, myStart);
		}
		myEnd = myStart + newSelection.limit() - newSelection.position();
		myCaretStickToStart = false;
		redrawTextAreas(true);
		restoreStateAfterModify();
	}

	/**
	 * Replaces all occurrences of findString with replaceString. The find starts at
	 * the current caret position.
	 *
	 * @param findString
	 *            the literal to find
	 * @param isFindHexString
	 *            consider the literal as an hex string (ie. "0fdA1"). Used for
	 *            binary finds. Will search full bytes only, odd number of hex
	 *            characters will have a leading '0' added.
	 * @param searchForward
	 *            look for matches after current position
	 * @param ignoreCase
	 *            match upper case with lower case characters
	 * @param replaceString
	 *            the new string
	 * @param isReplaceHexString
	 *            consider the literal as an hex string (ie. "0fdA1"). Used for
	 *            binary finds. Will replace full bytes only, odd number of hex
	 *            characters will have a leading '0' added.
	 * @return An array with [0]=number of replacements, [1]=last replaced start
	 *         position
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public long[] replaceAll(String findString, boolean isFindHexString, boolean searchForward, boolean ignoreCase,
			String replaceString, boolean isReplaceHexString) throws IOException, NumberFormatException {
		if (findString == null) {
			throw new IllegalArgumentException("Parameter 'findString' must not be null.");
		}
		if (replaceString == null) {
			throw new IllegalArgumentException("Parameter 'replaceString' must not be null.");
		}
		long replacements = 0;
		long lastStartPosition = 0;
		stopSearching = false;
		while (!stopSearching) {

			Match match = findAndSelectInternal(findString, isFindHexString, searchForward, ignoreCase, false);
			if (match.isFound()) {
				replacements++;
				lastStartPosition = match.getStartPosition();
				replace(replaceString, isReplaceHexString);
			} else {
				stopSearching = true;
				if (match.getException() != null) {
					throw match.getException();
				}
			}
		}
		if (replacements > 0) {
			RangeSelection selection = getSelection();
			setSelection(selection.start, selection.end);
		}

		return new long[] { replacements, lastStartPosition };
	}

	private void restoreStateAfterModify() {
		ensureCaretIsVisible();
		redrawTextAreas(true);
		updateScrollBar();

		notifyListeners(SWT.Modify, null);
		notifyLongSelectionListeners();
	}

	void runnableAdd(Runnable delayed) {
		if (delayedInQueue) {
			delayedWaiting = delayed;
		} else {
			delayedInQueue = true;
			Display.getCurrent().asyncExec(delayed);
		}
	}

	void runnableEnd() {
		if (delayedWaiting != null) {
			Display.getCurrent().asyncExec(delayedWaiting);
			delayedWaiting = null;
		} else {
			delayedInQueue = false;
		}
	}

	/**
	 * Sets the selection to the entire text. Caret remains either at the selection
	 * start or end
	 */
	public void selectAll() {
		select(0L, myContent.length());
		refreshSelections();
	}

	/**
	 * Sets the selection from start to end.
	 *
	 * @param start
	 * @param end
	 */
	public void selectBlock(long start, long end) {
		select(start, end);
		refreshSelections();
		showMark(start);
	}

	void select(long start, long end) {
		myUpANibble = 0;
		boolean selection = (myStart != myEnd);
		long newStart = 0L;
		if (start > 0L) {
			newStart = start;
			if (newStart > myContent.length()) {
				newStart = myContent.length();
			}
		}

		long newEnd = newStart;
		if (end > newStart) {
			newEnd = end;
			if (newEnd > myContent.length()) {
				newEnd = myContent.length();
			}
		}
		setStartAndEnd(newStart, newEnd);
		notifyLongSelectionListeners();
		boolean newSelection = (myStart != myEnd);
		if (selection != newSelection) {
			notifyListeners(SWT.Modify, null);
		}
	}

	private void setAddressesGridDataWidthHint() {
		((GridData) styledText0.getLayoutData()).widthHint = (int) (CHARS_FOR_ADDRESS * fontCharWidth);
	}

	public void setInsertMode(boolean insert) {
		myInserting = insert;
		int width = 0;
		int height = styledText1.getCaret().getSize().y;
		if (!myInserting) {
			width = (int) fontCharWidth;
		}

		styledText1.getCaret().setSize(width, height);
		styledText2.getCaret().setSize(width, height);
	}

	/**
	 * Sets the content to be displayed. Replacing an existing content keeps the
	 * display area in the same position, but only if it falls within the new
	 * content's limits.
	 *
	 * @param newContent
	 *            the content to be displayed
	 */
	public void setContentProvider(BinaryContent newContent) {
		boolean firstContent = (myContent == null);
		if (myContent != null && myContent != newContent) {
			myContent.dispose();
		}
		myContent = newContent;
		myFinder = null;
		if (myContent != null) {
			myContent.setActionsHistory();
		}

		if (firstContent || myEnd > myContent.length() || myTextAreasStart >= myContent.length()) {
			myTextAreasStart = 0L;
			setStartAndEnd(myTextAreasStart, myTextAreasStart);
			myCaretStickToStart = false;
		}

		charsForFileSizeAddress = Long.toHexString(myContent.length()).length();

		setEnabled(true);
		updateScrollBar();
		redrawTextAreas(true);
		notifyLongSelectionListeners();
		notifyListeners(SWT.Modify, null);
	}

	/**
	 * Causes the receiver to have the keyboard focus. Within Eclipse, never call
	 * setFocus() before the workbench has called
	 * EditorActionBarContributor.setActiveEditor()
	 *
	 * @see Composite#setFocus()
	 */
	@Override
	public boolean setFocus() {
		redrawCaret(false);
		if (myLastFocusedTextArea == 1) {
			return styledText1.setFocus();
		}
		return styledText2.setFocus();
	}

	/**
	 * @see Control#setFont(org.eclipse.swt.graphics.Font) Font height must not be 1
	 *      or 2.
	 * @throws IllegalArgumentException
	 *             if font height is 1 or 2
	 */
	@Override
	public void setFont(Font font) {
		// bugfix: HexText's raw array overflows when font is very small and
		// window very big very small sizes would compromise responsiveness in large
		// windows, and they are too small to see anyway
		if (font != null) {
			int newSize = font.getFontData()[0].getHeight();
			if (newSize == 1 || newSize == 2) {
				throw new IllegalArgumentException("Font size is " + newSize + ", too small");
			}
		}

		fontCurrent = font;
		if (fontCurrent == null) {
			fontCurrent = fontDefault;
		}
		super.setFont(fontCurrent);
		header1Text.setFont(fontCurrent);
		header1Text.pack(true);
		GC gc = new GC(header1Text);
		fontCharWidth = SWTUtility.getAverageCharacterWidth(gc);
		gc.dispose();
		makeFirstRowSameHeight();
		styledText0.setFont(fontCurrent);
		setAddressesGridDataWidthHint();
		styledText0.pack(true);
		styledText1.setFont(fontCurrent);
		styledText1.pack(true);
		styledText2.setFont(fontCurrent);
		styledText2.pack(true);
		updateTextsMetrics();
		layout();
		setInsertMode(myInserting);
	}

	/**
	 * Sets the selection. The caret may change position but stays at the same
	 * selection point (if it was at the start of the selection it will move to the
	 * new start, otherwise to the new end point). The new selection is made visible
	 *
	 * @param start
	 *            inclusive start selection position
	 * @param end
	 *            exclusive end selection position
	 */
	public void setSelection(long start, long end) {
		select(start, end);
		ensureCaretIsVisible();
		redrawTextAreas(false);
	}

	void shiftStartAndEnd(long newPos) {
		long newStart;
		long newEnd;
		if (myCaretStickToStart) {
			newStart = Math.min(newPos, myEnd);
			newEnd = Math.max(newPos, myEnd);
		} else {
			newStart = Math.min(newPos, myStart);
			newEnd = Math.max(newPos, myStart);
		}
		setStartAndEnd(newStart, newEnd);
		myCaretStickToStart = myEnd != newPos;
	}

	/**
	 * Shows the position on screen.
	 *
	 * @param position
	 *            where relocation should go
	 */
	public void showMark(long position) {
		myLastLocationPosition = position;
		if (position < 0) {
			return;
		}

		position = position - position % myBytesPerLine;
		myTextAreasStart = position;
		if (numberOfLines > 2) {
			myTextAreasStart = position - (numberOfLines / 2) * myBytesPerLine;
		}
		ensureWholeScreenIsVisible();
		redrawTextAreas(true);
		// setFocus();
		updateScrollBar();
	}

	/**
	 * Stop findAndSelect() or replaceAll() calls. Long running searches can be
	 * stopped from another thread.
	 */
	public void stopSearching() {
		stopSearching = true;
		if (myFinder != null) {
			myFinder.stopSearching();
		}
	}

	private long totalNumberOfLines() {
		long result = 1L;
		if (myContent != null) {
			result = (myContent.length() - 1L) / myBytesPerLine + 1L;
		}

		return result;
	}

	/**
	 * Undoes the last action
	 */
	public void undo() {
		undo(true);
	}

	private void undo(boolean previousAction) {
		long[] selection = previousAction ? myContent.undo() : myContent.redo();
		if (selection == null) {
			return;
		}

		myUpANibble = 0;
		setStartAndEnd(selection[0], selection[1]);
		myCaretStickToStart = false;
		ensureWholeScreenIsVisible();
		restoreStateAfterModify();
	}

	private void updateNumberOfLines() {
		int height = getClientArea().height - header1Text.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y;

		numberOfLines = height / styledText0.getLineHeight();
		if (numberOfLines < 1) {
			numberOfLines = 1;
		}

		numberOfLines_1 = numberOfLines - 1;

		((DisplayedContent) styledText0.getContent()).setDimensions(CHARS_FOR_ADDRESS, numberOfLines);
		((DisplayedContent) styledText1.getContent()).setDimensions(myBytesPerLine * 3, numberOfLines);
		((DisplayedContent) styledText2.getContent()).setDimensions(myBytesPerLine, numberOfLines);
	}

	void updateScrollBar() {
		ScrollBar vertical = getVerticalBar();
		long max = totalNumberOfLines();
		verticalBarFactor = 0;
		while (max > Integer.MAX_VALUE) {
			max >>>= 1;
			++verticalBarFactor;
		}
		vertical.setMaximum((int) max);
		vertical.setSelection((int) ((myTextAreasStart / myBytesPerLine) >>> verticalBarFactor));
		vertical.setPageIncrement(numberOfLines_1);
		vertical.setThumb(numberOfLines);
	}

	void updateTextsMetrics() {
		int width = getClientArea().width - styledText0.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		int displayedNumberWidth = (int) (fontCharWidth * 4); // styledText1 and
		// styledText2
		myBytesPerLine = (width / displayedNumberWidth) & 0xfffffff8; // 0, 8, 16, 24, etc.
		if (myBytesPerLine < 8) {
			myBytesPerLine = 8;
		}
		adaptWidthToBytesPerLine();

		updateNumberOfLines();
		layout(new Control[] { header1Text, styledText0, styledText1, styledText2 }, SWT.DEFER);
		updateScrollBar();
		refreshHeader();
		myTextAreasStart = (((long) getVerticalBar().getSelection()) * myBytesPerLine) << verticalBarFactor;
		redrawTextAreas(true);
	}

}
