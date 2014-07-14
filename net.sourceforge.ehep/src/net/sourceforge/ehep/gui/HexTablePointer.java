/*==========================================================================
 * 
 * HexTablePointer.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.3 $
 * $Date: 2012/11/06 16:45:23 $
 * $Name:  $
 * 
 * Created on 29-Jan-2004
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.gui;

import net.sourceforge.ehep.core.EHEP;

/**
 * @author Marcel Palko
 * @author randallco@users.sourceforge.net
 */
public class HexTablePointer {

	private int rowIndex;
	private int columnIndex;
	
	public HexTablePointer(int rowIndex, int columnIndex) {
		super();
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	public HexTablePointer(int offset) {
		move(offset);
	}

	/**
	 * Zero-based projection from the pointer position (row,column) to index 
	 * @return zero-based position index
	 */
	public int getOffset() {
		return rowIndex * EHEP.TABLE_NUM_DATA_COLUMNS + columnIndex;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public HexTablePointer move(int offset) {
		int newOffset = getOffset() + offset;
		rowIndex = newOffset / EHEP.TABLE_NUM_DATA_COLUMNS;
		columnIndex = newOffset % EHEP.TABLE_NUM_DATA_COLUMNS;
		return this;
	}
	
	public boolean equals(HexTablePointer p) {
		return (rowIndex == p.getRowIndex() && columnIndex == p.getColumnIndex());
	}

	public HexTablePointer adjust() {
		if (rowIndex < 0) rowIndex = 0;
		if (columnIndex < 0) columnIndex = 0;
		return this;
	}
	
	public String toString() {
		return Messages.HexTablePointer_0 + rowIndex + Messages.HexTablePointer_1 + columnIndex + ")"; //$NON-NLS-3$
	}
}
