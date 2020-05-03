/*==========================================================================
 * 
 * CellEditorKeyListener.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.7 $
 * $Date: 2012/11/06 16:45:24 $
 * $Name:  $
 * 
 * Created on 23-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.events;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

import net.sourceforge.ehep.core.EHEP;
import net.sourceforge.ehep.core.Utils;
import net.sourceforge.ehep.editors.HexEditor;
import net.sourceforge.ehep.gui.HexEditorControl;
import net.sourceforge.ehep.gui.HexTable;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class CellEditorKeyListener extends KeyAdapter {
	private final HexEditor hexEditor;
	private final HexEditorControl hexEditorControl;
	private final TableCursor cursor;
	private final Text text;

	/**
	 * Creates a key listener for table cell editor
	 * @param hexEditorControl
	 * @param hexEditor
	 * @param cursor
	 * @param editor
	 * @param text
	 */
	public CellEditorKeyListener(final HexEditorControl hexEditorControl, final HexEditor hexEditor, final TableCursor cursor, final ControlEditor editor, final Text text) {
		super();
		this.hexEditorControl = hexEditorControl;
		this.hexEditor = hexEditor;
		this.cursor = cursor;
		this.text = text;
	}

	/**
	 * Event handler for "key pressed" event
	 * @param e an event containing information about the key press
	 */
	public void keyPressed(KeyEvent e) {
		//
		// Close the text editor and copy the data over when the user hits "ENTER"
		//
		if (e.character == SWT.CR) {
			keyPressedEnter();
			return;
		} // if CR

		//
		// Close the text editor when the user hits "ESC"
		//
		if (e.character == SWT.ESC) {
			//
			// Release cell editor, no changes in the table cell
			//
			text.dispose();
			hexEditorControl.updateStatusPanel();
			return;
		} // if ESC

		if (e.character == SWT.DEL || e.character < ' ') {
			//
			// User pressed 'Delete' or other control key
			//
			return;
		}

		boolean isHexaNumeric = (e.character >= '0' && e.character <= '9') || (e.character >= 'A' && e.character <= 'F') || (e.character >= 'a' && e.character <= 'f');
		boolean commaSpecified = text.getText().length() > 0 && text.getText().charAt(0) == ',';
		if (isHexaNumeric || commaSpecified) {
			//
			// User pressed the valid hex digit
			//
			if (text.getText().length() == 1) {
				String result = commaSpecified ? Integer.toHexString(e.character) : text.getText() + e.character;
					
				//
				// The cell contains valid 2-digit hex number - close the cell editor (pressing 'Enter' is not necessary)
				//
				int itemIndex = cursor.getRow().getParent().indexOf(cursor.getRow());
				if (Utils.isValidHexNumber(result)) {
					closeCellEditor(itemIndex, cursor.getColumn() - 1, result);
					//
					// Move cursor position to the next data cell
					//
					goToNextCell();
				}
			} // if
			
			hexEditorControl.updateStatusPanel();
		} // if
	} // keyPressed()

	/**
	 * Closes the cell editor and updates the new value in the table
	 * @param row table row where is the cell to close
	 * @param column specify the cell to close
	 * @param str text to be updated in the table cell (taken from cell editor) 
	 */
	public void closeCellEditor(int row, int column, String str) {
		//
		// Store the new value from ControlEditor into the table
		// row.setText(column, str.toUpperCase())
		hexEditorControl.modify(row,column, Utils.string2bytes(str.toUpperCase()));
		//
		// Release cell editor
		//
		text.dispose();
	}

	/**
	 * Moves cursor (data cell selector) to the next data cell
	 */
	final void goToNextCell() {
		int column = cursor.getColumn() - 1;
		HexTable table = hexEditorControl.getHexTable();
		int row = table.getSelectionIndex();

		if (row * EHEP.TABLE_NUM_DATA_COLUMNS + column >= table.getBufferSize())
			return;

		if (++column >= EHEP.TABLE_NUM_DATA_COLUMNS) {
			//
			// Last table cell at this row - go to next table row if possible
			//
			int numTableRows = table.getItemCount();
			if (row+1 == numTableRows) {
				//
				// Last data cell at the last table row reached - do not move the cursor
				//
				return;
			}
			//
			// Move cursor to the first data cell (column #1)
			//
			column = 0;
			row++;
			//
			// Move table row selector
			//
			table.setSelection(row);
		}
		//
		// Move table cell selector
		//
		cursor.setSelection(row, column + 1);
	}

	public void keyPressedEnter() {
		int itemIndex = cursor.getRow().getParent().indexOf(cursor.getRow());
		int column = cursor.getColumn() - 1;
		String str = text.getText();
			
		if (str.length() == 0) {
			//
			// Replace empty string with 00
			//
			str = "00"; //$NON-NLS-1$
		} else if (str.length() == 1) {
			//
			// Add zero padding
			//
			str = "0" + str; //$NON-NLS-1$
		}
		
		if (str.charAt(0) == ',')
			str = Integer.toHexString(str.charAt(1));

		if (Utils.isValidHexNumber(str)) {
			//
			// User entered a valid hex number - close the cell editor
			//
			closeCellEditor(itemIndex, column, str);
			//
			// Move cursor position to the next data cell
			//
			goToNextCell();

			hexEditorControl.updateStatusPanel();
		}
		else {
			//
			// String has incorrect format - do nothing
			//
			MessageDialog.openInformation(hexEditor.getSite().getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.CellEditorKeyListener_2 + str.toUpperCase() + Messages.CellEditorKeyListener_3);
		} // else
	}
}
