/*==========================================================================
 * 
 * CursorKeyListener.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.8 $
 * $Date: 2012/11/06 16:45:24 $
 * $Name:  $
 * 
 * Created on 23-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.events;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.core.EHEP;
import net.sourceforge.ehep.editors.HexEditor;
import net.sourceforge.ehep.gui.HexEditorControl;
import net.sourceforge.ehep.gui.HexTable;
import net.sourceforge.ehep.gui.HexTablePointer;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class CursorKeyListener extends KeyAdapter {

	private final HexEditor hexEditor;
	private final HexEditorControl hexEditorControl;
	private final TableCursor cursor;
	private final ControlEditor editor;
	private Text text = null;
	private CellEditorKeyListener cellEditorKeyListener = null;
	private boolean shiftHold;

	private int eventColumnIndex;
	private int eventRowIndex;
	
	/**
	 * Creates a key listener for table cursor
	 * @param hexEditorControl
	 * @param hexEditor
	 * @param cursor
	 * @param editor
	 */
	public CursorKeyListener(final HexEditorControl hexEditorControl, final HexEditor hexEditor, final TableCursor cursor, final ControlEditor editor) {
		super();
		this.hexEditorControl = hexEditorControl;
		this.hexEditor = hexEditor;
		this.cursor = cursor;
		this.editor = editor;
	}

	public void keyReleased(KeyEvent e) {
		if (e.keyCode == SWT.SHIFT)
			shiftHold = false;
	}

	private byte[] cmpBuffer;
	/**
	 * Event handler for "key pressed" event
	 * @param e an event containing information about the key press
	 */
	public void keyPressed(KeyEvent e) {
		shiftHold = (e.stateMask & SWT.SHIFT) > 0 || e.keyCode == SWT.SHIFT;
		if (hexEditor.isReadOnly()) {
			//
			// File is read-only - do nothing
			//
			return;
		} // if
		
		if (EhepPlugin.getDefault().getPreferenceStore().getBoolean(EHEP.PROPERTY_DEBUG_MODE)) {
			if (e.character == 'c') {
				if (cmpBuffer != null) {
					System.out.println("comparing..."); //$NON-NLS-1$
					if (hexEditorControl.getHexTable().getBufferSize() != cmpBuffer.length) {
						System.out.println("they differ in length"); //$NON-NLS-1$
					} else {
						byte[] bs = new byte[cmpBuffer.length];
						hexEditorControl.getHexTable().getData(bs, 0, bs.length);
						boolean equal = true;
						int positions = 0;
						for (int i = 0; i < bs.length; i++) {
							if (bs[i] == cmpBuffer[i])
								continue;
							positions++;
							equal = false;
						}
						if (equal) {
							System.out.println("they are equal!"); //$NON-NLS-1$
						} else {
							System.out.println("they differ at " + positions + " positions");				 //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				} else {
					System.out.println("no cmp buffer!"); //$NON-NLS-1$
				}
				return;
			}
			if (e.character == 's'){
				System.out.println("saving..."); //$NON-NLS-1$
				cmpBuffer = new byte[hexEditorControl.getHexTable().getBufferSize()];
				hexEditorControl.getHexTable().getData(cmpBuffer, 0, cmpBuffer.length);
				return;
			}
		}
		
		if (e.keyCode == SWT.INSERT) {
			keyInsert();
			hexEditorControl.updateStatusPanel();
			return;
		} // if
		
		if (e.character != ',' && Character.digit(e.character, 16) == -1) {
			//
			// Invalid hex digit - ignore
			//
			return;
		} // if
		
		//
		// Valid hex digit
		//
		TableItem row = cursor.getRow();
		int column = cursor.getColumn();
		
		if (column == 0 || column > EHEP.TABLE_NUM_DATA_COLUMNS) {
			//
			// Wrong column - ignore
			//
			return;
		} // if
		
		if (row.getText(column).compareTo(EHEP.TABLE_EMPTY_CELL) == 0) {
			//
			// Cursor is out of file (over the cell without a content)
			//
			return;
		} // if
				
		//
		// Following operations are allowed only for columns 1...16 (actual hex view)
		//
		text = new Text(cursor, SWT.NONE);
		text.insert("" + e.character); //$NON-NLS-1$
		text.setFont(EhepPlugin.getFont(PreferenceConverter.getFontData(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_FONT)));
		text.setBackground(EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_COLOR_BACKGROUND_EDITOR)));
		text.setForeground(EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_COLOR_FOREGROUND_EDITOR)));
		text.setTextLimit(2);
		editor.setEditor(text);
		text.setFocus();

		//
		// Remember where the text editor was opened
		//
		HexTable table = hexEditorControl.getHexTable();
		int rowIndex = table.getSelectionIndex();
		eventColumnIndex = cursor.getColumn();
		eventRowIndex = rowIndex;

		//
		// Add key listener to cell editor (Text component)
		//
		cellEditorKeyListener = new CellEditorKeyListener(hexEditorControl, hexEditor, cursor, editor, text);
		text.addKeyListener(cellEditorKeyListener);
	}

	private void keyInsert() {
		//
		// Insert new byte into the table
		//
		HexTablePointer position = hexEditorControl.getCursorPosition();
			
		if (position.getColumnIndex() < 0 || position.getColumnIndex() >= EHEP.TABLE_NUM_DATA_COLUMNS) {
			//
			// Ignore - data insertion is not allowed
			//
			return;
		} // if
			
		//
		// Insert one table cell with default value 00h
		//
		hexEditorControl.insertData(1, 0, position.getRowIndex(), position.getColumnIndex());
	}

	/**
	 * Closed the cell editor if one is active without modifying the data
	 */
	public void closeCellEditor() {
		if (text != null && !text.isDisposed())
			text.dispose();
	}

	public CellEditorKeyListener getCellEditorKeyListener() {
		return cellEditorKeyListener;
	}

	public Text getText() {
		return (text == null || text.isDisposed()) ? null : text;
	}
	
	public boolean isShiftHold() {
		return shiftHold;
	}
	
	public void setSelection() {
		cursor.setSelection(eventRowIndex, eventColumnIndex);
	}

	public int getEventRow() {
		return eventRowIndex;
	}
	
	public int getEventColumn() {
		return eventColumnIndex;
	}
}
