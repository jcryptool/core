//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.ui;

import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.jcryptool.analysis.transpositionanalysis.ui.wizards.TranspTextWizardPage;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionKey;
import org.jcryptool.crypto.classic.transposition.algorithm.TranspositionTable;

/**
 * A Table where you can do the following: * Set Columns named after a given
 * scheme * Set a text through a wizard, which is displayed in the defined
 * columns * Access every "cell" by coordinates
 * 
 * @author Simon Leischnig
 */
public class TranspositionTableComposite extends Composite implements ControlListener {

	private Table transpTable;

	/**
	 * The actual (but not transformed) text in the table
	 */
	private String displayedText;
	/**
	 * The Text in the table as it was set by the user
	 */
	private String unchangedText;
	/**
	 * the actually displayed cells are stored there
	 */
	private char[][] cells;
	/**
	 * the actually displayed rows are stored here
	 */
	private String[] rows;
	/**
	 * the actually displayers columns are stored here
	 */
	private String[] columns;
	/**
	 * The column width in which a text is arranged in this table 0 means, the text
	 * will be arranged in one single row (no "line break" after column width is
	 * reached)
	 */
	private int columnCount = 9;
	/**
	 * decides, if the whole text set should be displayed or if only a part of it
	 * (first singleLineDisplayCount characters). As soon as columnCount is set to a
	 * value greater than zero, the full text will be displayed
	 */
	private boolean displayEverything = true;
	/**
	 * How much characters should be displayed, if displayEverything is false?
	 */
	private int displayCount = 40;
	/**
	 * The width of a column (inhabited by 1 character) in pixel
	 */
	private int columnWidth = 36;
	/**
	 * Column width
	 */
	private TableColumn[] disposableColumns;

	private Observer colReorderObserver;

	private int colMovedTime;

	private boolean readInOrder = false;

	public TranspositionTableComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		GridLayout thisLayout = new GridLayout();
		thisLayout.marginHeight = 0;
		thisLayout.marginWidth = 0;
		this.setLayout(thisLayout);

		transpTable = new Table(this, SWT.FULL_SELECTION | SWT.BORDER);
		GridData transpTableLData = new GridData(SWT.FILL, SWT.FILL, true, true);
		transpTableLData.minimumWidth = 5 * columnWidth + 4;
		transpTable.setLayoutData(transpTableLData);
		transpTable.setHeaderVisible(true);
		// transpTable.setFont(fatFont);

		// revert selection highlighting
		transpTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				transpTable.setSelection(-1);
			}
		});

	}

	private String unchangedToDisplayText(String text) {
		if (!displayEverything)
			return text.substring(0, Math.min(displayCount, text.length()));
		else
			return text;
	}

	private int charArrayLengthWithoutEMPTY(Character[] array) {
		int result = 0;
		for (int i = 0; i < array.length; i++)
			if (!array[i].equals(TranspositionTable.EMPTY))
				result++;
		return result;
	}

	private void calculateArrays() {
		if (displayedText == null)
			displayedText = ""; //$NON-NLS-1$
		int w = 0;
		int h = 0;
		int textL = displayedText.length();

		if (columnCount == 0) {
			w = textL;
		} else {
			w = columnCount;
		}

		if (w == 0)
			w = 1;

		TranspositionTable virtualTable = new TranspositionTable(w);
		virtualTable.fillCharsIntoTable(displayedText.toCharArray(), readInOrder);

		Character[][] virtualTableContent = virtualTable.getContent();
		w = virtualTableContent.length;
		if (w > 0)
			h = virtualTableContent[0].length;
		else
			h = 0;

		// Cells
		cells = new char[w][];
		for (int i = 0; i < w; i++) {
			cells[i] = new char[charArrayLengthWithoutEMPTY(virtualTableContent[i])];
			int k = 0;
			int counter = 0;
			while (k < virtualTableContent[i].length) {
				if (!virtualTableContent[i][k].equals(TranspositionTable.EMPTY)) {
					cells[i][counter] = virtualTableContent[i][k];
					counter++;
				}
				k++;
			}
		}

		// Rows
		rows = new String[h];
		for (int i = 0; i < h; i++) {
			rows[i] = ""; //$NON-NLS-1$
			for (int k = 0; (k < w) && (cells[k].length > i); k++)
				rows[i] = rows[i].concat(String.valueOf(cells[k][i]));
		}
		// Columns
		columns = new String[w];
		for (int i = 0; i < w; i++) {
			columns[i] = String.valueOf(cells[i]);
		}
	}

	/**
	 * put the Text from the rows / columns field into the Table
	 */
	private void drawTableText() {
		if ((rows != null) && (columns != null)) {
			transpTable.clearAll();
			if (disposableColumns != null) {
				for (int i = 0; i < disposableColumns.length; i++)
					disposableColumns[i].removeControlListener(this);
				for (int i = 0; i < disposableColumns.length; i++)
					disposableColumns[i].dispose();
			}
			disposableColumns = new TableColumn[0];
			transpTable.setItemCount(0);

			// create columns
			disposableColumns = new TableColumn[columns.length];
			for (int i = 0; i < columns.length; i++) {
				disposableColumns[i] = new TableColumn(transpTable, SWT.NONE);
				disposableColumns[i].setText("" + (i + 1)); //$NON-NLS-1$
				// calculate the width of a column.
				int colPxWidth = columnWidth + (("" + (i + 1)).length() - 1) * (columnWidth / 4); //$NON-NLS-1$
				disposableColumns[i].setWidth(colPxWidth);
				disposableColumns[i].setMoveable(true);
				disposableColumns[i].setAlignment(SWT.CENTER);
				disposableColumns[i].addControlListener(this);

			}
			for (int i = 0; i < rows.length; i++) {
				TableItem myItem = new TableItem(transpTable, SWT.NONE);
				String[] myRow = new String[rows[i].length()];
				for (int k = 0; k < myRow.length; k++) {
					myRow[k] = String.valueOf(rows[i].charAt(k));
				}
				myItem.setText(myRow);
				myItem.setFont(FontService.getNormalBoldFont());
			}

			transpTable.layout();
		}
	}

	/**
	 * Set a text to be displayed in the table rows. The text will be arranged in
	 * given columns, if a column width is set.
	 * 
	 * @param text the text to be displayed
	 */
	public void setText(String text) {
		setText(text, columnCount, true);
	}

	/**
	 * set a text to display; this method only calls setcolumnCount and setText in
	 * this order; read there for more information.
	 * 
	 * @param text        the text to display
	 * @param columnCount number of columns, in which the text should be arranged.
	 *                    Zero = no arrangement, but single line display.
	 */
	public void setText(String text, int columnCount) {
		setText(text, columnCount, true); // not calling inner method because
											// this
		// originates from the outside
	}

	/**
	 * set a text to display
	 * 
	 * @param text        the text
	 * @param columnCount the column count
	 * @param refresh     redraw/calculation, or not
	 */
	private void setText(String text, int columnCount, boolean refresh) {
		setText(text, columnCount, this.displayEverything, this.displayCount,
				TranspTextWizardPage.makeStdColumnOrder(columnCount), refresh);
	}

	/**
	 * inner setText procedure. Methods in this class should call this method only
	 * if they want to control whether a redraw/recalculate should occur or not.
	 * 
	 * @param text        the text
	 * @param columnCount the column count
	 * @param fulltext    should the text be displayed full or cropped
	 * @param croplength  the length where the text could be cropped (see above).
	 */
	public void setText(String text, int columnCount, boolean fulltext, int croplength) {
		setText(text, columnCount, fulltext, croplength, TranspTextWizardPage.makeStdColumnOrder(columnCount), true);
	}

	/**
	 * inner setText procedure. Methods in this class should call this method only
	 * if they want to control whether a redraw/recalculate should occur or not.
	 * 
	 * @param text        the text
	 * @param columnCount the column count
	 * @param fulltext    should the text be displayed full or cropped
	 * @param croplength  the length where the text could be cropped (see above).
	 * @param refresh     redraw/calculation, or not
	 */
	public void setText(String text, int columnCount, boolean fulltext, int croplength, int[] columnOrder,
			boolean refresh) {
		this.setDisplayEverything(fulltext);
		this.setDisplayCount(croplength);
		unchangedText = text;
		setColumnCount(columnCount, false);
		displayedText = unchangedToDisplayText(unchangedText);

		if (refresh) {
			refresh();
			setColumnOrder(columnOrder);
		}
	}

	/**
	 * Refreshes the display of the text in the table.
	 */
	public void refresh() {
		calculateArrays();
		drawTableText();
	}

	/**
	 * @param columnCount the column Width to set in which the text is displayed.
	 *                    <br />
	 *                    zero: no colum-arrangement (singleline)
	 */
	public void setColumnCount(int columnCount) {
		setColumnCount(columnCount, true);
	}

	/**
	 * inner columnCount procedure. methods in this class should call this if they
	 * want to control whether a redraw/recalculate should occur or not.
	 * 
	 * @param columnCount the column width
	 * @param refresh     redraw/calculation, or not
	 */
	private void setColumnCount(int columnCount, boolean refresh) {
		if (columnCount <= 0)
			this.columnCount = 0;
		else
			this.columnCount = columnCount;
		displayedText = unchangedToDisplayText(unchangedText);

		if (refresh) {
			calculateArrays();
			drawTableText();
		}
	}

	/**
	 * @return the column count in which the text is displayed. <br />
	 *         zero: no colum-arrangement (singleline)
	 */
	public int getColumnCount() {
		return columnCount;
	}

	public int getColumnCountDisplayed() {
		return transpTable.getColumnCount() == 0 ? 1 : transpTable.getColumnCount();
	}

	/**
	 * @return the displayEverythingSingleline
	 */
	public boolean isDisplayEverything() {
		return displayEverything;
	}

	/**
	 * @param displayEverything the displayEverythingSingleline to set
	 */
	public void setDisplayEverything(boolean displayEverything) {
		this.displayEverything = displayEverything;
	}

	/**
	 * @return the transpTable
	 */
	public Table getTranspTable() {
		return transpTable;
	}

	/**
	 * @return the cells
	 */
	public char[][] getCells() {
		return cells;
	}

	/**
	 * @return the rows
	 */
	public String[] getRows() {
		return rows;
	}

	public String getReorderedLine(int index) {
		String line = rows[index];
		int[] order = transpTable.getColumnOrder();
		order = new TranspositionKey(order).getReverseKey().toArray();
		String[] reordered = new String[order.length];
		for (int i = 0; i < reordered.length; i++)
			reordered[i] = ""; //$NON-NLS-1$
		for (int i = 0; i < line.length(); i++) {
			reordered[order[i]] = String.valueOf(line.charAt(i));
		}

		StringBuilder sb = new StringBuilder(""); //$NON-NLS-1$
		for (String s : reordered)
			sb.append(s);

		return sb.toString();
	}

	public String getText() {
		StringBuilder sb = new StringBuilder(""); //$NON-NLS-1$

		if (rows != null) {
			for (int i = 0; i < rows.length; i++) {
				sb.append(getReorderedLine(i));
			}
		}

		return sb.toString();
	}

	/**
	 * @return the columns
	 */
	public String[] getColumns() {
		return columns;
	}

	/**
	 * @param singleLineDisplayCount the singleLineDisplayCount to set
	 */
	public void setDisplayCount(int singleLineDisplayCount) {
		this.displayCount = singleLineDisplayCount;
	}

	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	/**
	 * @return
	 * @see org.eclipse.swt.widgets.Table#getColumnOrder()
	 */
	public int[] getColumnOrder() {
		return transpTable.getColumnOrder();
	}

	public void setColumnOrder(int[] order) {
		transpTable.setColumnOrder(order);
	}

	/**
	 * @param colReorderObserver the colReorderObserver to set
	 */
	public void setColReorderObserver(Observer colReorderObserver) {
		this.colReorderObserver = colReorderObserver;
	}

	/**
	 * Set, how the text should be displayed (row- or columnwise)
	 * 
	 * @param readInOrder
	 */
	public void setReadInOrder(boolean textDisplayOrder) {
		boolean changed = !(textDisplayOrder == this.readInOrder);
		this.readInOrder = textDisplayOrder;
		if (changed)
			refresh();
	}

	/**
	 * Set, how the text should be displayed (row- or columnwise)
	 * 
	 * @param readInOrder
	 */
	public void setReadInOrder(boolean textDisplayOrder, boolean refresh) {
		boolean changed = !(textDisplayOrder == this.readInOrder);
		this.readInOrder = textDisplayOrder;
		if (changed && refresh)
			refresh();
	}

	@Override
	public void controlMoved(ControlEvent e) {

		if (Math.abs(e.time - colMovedTime) > 200) {
			if (colReorderObserver != null)
				colReorderObserver.update(null, transpTable.getColumnOrder());
		}
		colMovedTime = e.time;

	}

	@Override
	public void controlResized(ControlEvent e) {
	}

	public boolean getReadInOrder() {
		return readInOrder;
	}

}
