/*==========================================================================
 * 
 * HexTable.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.13 $
 * $Date: 2012/11/06 16:45:23 $
 * 
 * Created on 22-Jan-2004
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.gui;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.core.EHEP;
import net.sourceforge.ehep.core.Utils;

/**
 * @author Marcel Palko
 * @author Uwe Voigt
 * @author randallco@users.sourceforge.net
 */
public class HexTable extends Composite implements IPropertyChangeListener {
	private static final int threshold = 100;

	private Table table;
	// this is the underlying data store
	// d0..d7 is data, d8..d14 is cell status, d15 is the selected flag
	private short[] data;
	private int length;
	private HexEditorControl hexEditorControl;
	private Point visibleRows = new Point(0, 0);
	private Listener visibleRowsDeterminator;
	private Color[] statusColors = new Color[12];
	private Point selection = new Point(0, 0);

	/**
	 * Constructor
	 * @param parent
	 * @param hexEditorControl
	 * @param style
	 */
	public HexTable(Composite parent, HexEditorControl hexEditorControl, int style) {
		super(parent, SWT.NONE);
		this.hexEditorControl = hexEditorControl;

		table = new Table(this, style);
		setFont(table.getFont());
		
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				onResize();
			}
		});

		addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event e) {
				onFocusIn();
			}
		});

		table.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event e) {
				onSetData((TableItem) e.item);
			}
		});
		
		Listener selectionListener = new Listener() {
			private boolean dragging = false;
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MouseDown:
					dragging = true;
					break;
				case SWT.MouseUp:
				case SWT.MouseExit:
					dragging = false;
					break;
				case SWT.MouseMove:
					if (!dragging)
						break;
					handleMouseMove(event);
					break;
				}
			}
		};

		table.addListener(SWT.MouseDown, selectionListener);
		table.addListener(SWT.MouseUp, selectionListener);
		table.addListener(SWT.MouseMove, selectionListener);
		table.addListener(SWT.MouseExit, selectionListener);
		
		visibleRowsDeterminator = new Listener() {
			private int scrollPosition;
			private Point size;
			public void handleEvent(Event event) {
				if (event.type == SWT.Selection
						|| event.type == SWT.Resize
						|| event.keyCode == SWT.ARROW_UP
						|| event.keyCode == SWT.ARROW_DOWN
						|| event.keyCode == SWT.PAGE_UP
						|| event.keyCode == SWT.PAGE_DOWN
						|| event.keyCode == SWT.HOME
						|| event.keyCode == SWT.END) {
					int position = table.getVerticalBar().getSelection();
					Point size = table.getSize();
					if (position != scrollPosition || this.size == null || size.x > this.size.x || size.y > this.size.y) {
						scrollPosition = position;
						this.size = size;
						updateVisibleTable(true, true, true);
					}
				}
			}
		};
		EhepPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().startsWith(EHEP.PLUGIN_NAME + ".color.") //$NON-NLS-1$
				|| event.getProperty().equals(EHEP.PROPERTY_FONT)) {
			Arrays.fill(statusColors, 0, statusColors.length, null);
		}
		updateColors();
	}
	
	void updateColors() {
		table.setBackground(EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_COLOR_BACKGROUND_TABLE)));
		table.setForeground(EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_COLOR_FOREGROUND_TABLE)));
		Font font = EhepPlugin.getFont(PreferenceConverter.getFontData(EhepPlugin.getDefault().getPreferenceStore(), EHEP.PROPERTY_FONT));
		if (font != table.getFont()) {
			table.setFont(font);
			hexEditorControl.getCursor().setFont(font);
			packColumns();
		}
		if (getItemCount() > 0) {
			Arrays.fill(statusColors, 0, statusColors.length - 1, null);
			updateVisibleTable(false, true, false);
		}
	}

	@Override
	public void dispose() {
        data = null;
		EhepPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		super.dispose();
	}
	
	void initListeners() {
		table.getVerticalBar().addListener(SWT.Selection, visibleRowsDeterminator);
		table.addListener(SWT.Resize, visibleRowsDeterminator);
		hexEditorControl.getCursor().addListener(SWT.KeyDown, visibleRowsDeterminator);
	}
	
	private void determineVisibleRows() {
		int numVisible = table.getClientArea().height / table.getItemHeight();
		visibleRows.x = table.getVerticalBar().getSelection();
		visibleRows.y = Math.min(visibleRows.x + numVisible - 1, table.getItemCount() - 1);
		if (EhepPlugin.isDebugMode())
			System.out.println("visibleRows: " + Integer.toHexString(visibleRows.x) + "-" + Integer.toHexString(visibleRows.y) + " numvisi:" + numVisible); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Returns the number of table items (rows)
	 * @return number of table rows
	 */
	public int getItemCount() {
		return data != null ? length / EHEP.TABLE_NUM_DATA_COLUMNS
				+ (length % EHEP.TABLE_NUM_DATA_COLUMNS > 0 ? 1 : 0) : 0;
	}

	/**
	 * Sets the selected cells.
	 * 
	 * @param row current position row
	 * @param column current position column
	 * @param extend true if an existing selection is to extend, false if a selection starting point is set
	 */
	public void setCellSelection(int row, int column, boolean extend) {
		int dataIndex = getDataIndex(row, column);
		if (dataIndex < 0 || dataIndex > length)
			return;
		if (extend) {
			setCellSelection(dataIndex - selection.x);
		} else {
			setCellSelection(0);
			selection.x = dataIndex;
		}
	}
	
	public void selectAll() {
		selection.x = 0;
		setCellSelection(getBufferSize());
	}

	private void handleMouseMove(Event event) {
		int row = table.getVerticalBar().getSelection() + (event.y + table.getGridLineWidth() - table.getHeaderHeight()) / table.getItemHeight();
		if (row < 0)
			row = 0;
		if (row >= getItemCount())
			row = getItemCount() - 1;
		if (row < 0 || row > getItemCount())
			return;
		int dataIndex = row * EHEP.TABLE_NUM_DATA_COLUMNS;
		TableColumn[] columns = table.getColumns();
		int x = 0;
		int column = 0;
		for (int n = Math.min(EHEP.TABLE_NUM_DATA_COLUMNS + 1, columns.length); column < n; column++) {
			x += columns[column].getWidth();
			if (event.x <= x) {
				dataIndex += column;
				break;
			}
		}
		hexEditorControl.getCursor().setSelection(row, column);
		setSelection(row);
		setCellSelection(dataIndex - selection.x - 1);
		if (row < visibleRows.x || row > visibleRows.y)
			table.showItem(table.getItem(row));
	}

	Point getCellSelection() {
		return adjustSelectionOffset(selection.y < 0 ? selection.y : selection.y + 1);
	}
	
	private void setCellSelection(int length) {
		Point p = adjustSelectionOffset(length);
		if (p.x < 0 || p.x + p.y > getBufferSize())
			return;
		Point p1 = adjustSelectionOffset(selection.y);
		if (p1.x >= 0)
			setCellStatus(p1.x / EHEP.TABLE_NUM_DATA_COLUMNS, p1.x % EHEP.TABLE_NUM_DATA_COLUMNS, p1.y, EHEP.CELL_STATUS_NORMAL, ~EHEP.CELL_STATUS_SELECTED << 8 | 0xff);

		setCellStatus(p.x / EHEP.TABLE_NUM_DATA_COLUMNS, p.x % EHEP.TABLE_NUM_DATA_COLUMNS, p.y, EHEP.CELL_STATUS_SELECTED, 0xffff);
		updateVisibleTable(true, true, true);
		selection.y = length;
	}
	
	private Point adjustSelectionOffset(int length) {
		Point p = new Point(selection.x, length);
		// adjust the offset so that for a negative length the selection still is correct
		if (p.y < 0) {
			HexTablePointer hp = new HexTablePointer(p.x);
			hp.move(p.y);
			p.x = hp.getOffset();
			p.y = -p.y + 1;
		}
		if (p.x + p.y > this.length)
			p.y = this.length - p.x;
		return p;
	}

	/**
	 * Selects the table item
	 * @param index
	 */
	public void select(int index) {
		table.select(index);
	}
	
	/**
	 * Sets the selection.
	 * <p>
	 * @param items new selection
	 *
	 * @exception SWTError <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
	 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed
	 *    <li>ERROR_NULL_ARGUMENT when items is null
	 * </ul>
	 * </p>
	 */
	public void setSelection(TableItem[] items) {
		table.setSelection(items);
	}
	
	/**
	 * Sets the table selection
	 * @param index
	 */
	public void setSelection(int index) {
		table.setSelection(index);
	}

	/**
	 * Gets the selection index
	 * @return selection index
	 */
	public int getSelectionIndex() {
		return table.getSelectionIndex();
	}

	/**
	 * Returns the table
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
	
	/**
	 * Returns widget size
	 * @param wHint
	 * @param hHint
	 */
	public Point computeSize(int wHint, int hHint) {
		return computeSize(hHint, wHint, true);
	}

	/**
	 * Returns widget size
	 * @param wHint
	 * @param hHint
	 * @param changed
	 */
	public Point computeSize(int wHint, int hHint, boolean changed) {
		super.computeSize(wHint, hHint, changed);
		return table.computeSize(wHint, hHint, changed);
	}

	/**
	 * Computes the widget trim.
	 * <p>
	 * Trim is widget specific and may include scroll
	 * bars and menu bar in addition to other trimmings
	 * that are outside of the widget's client area.
	 *
	 * @param x the x location of the client area
	 * @param y the y location of the client area
	 * @param width the width of the client area
	 * @param height the height of the client area
	 * @return a rectangle containing the trim of the widget.
	 *
	 * @exception SWTError <ul>
	 *          <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
	 *          <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
	 * </ul>
	 * </p>
	 */
	public Rectangle computeTrim(int x, int y, int width, int height) {
		return table.computeTrim(x, y, width, height);
	}

	/**
	 * Calculates the number of cells occupied by the data from the specified position to the end
	 * @param rowIndex
	 * @param columnIndex
	 * @return the number of cells occupied by data
	 */
	public int getTableSize(int rowIndex, int columnIndex) {
		return length - (rowIndex * EHEP.TABLE_NUM_DATA_COLUMNS + columnIndex);
	}

	private Color getColorForStatus(int status, boolean background) {
		int s = (status & EHEP.CELL_STATUS_SELECTED) > 0 ? statusColors.length / 2 - 1 : status;
		int index = s + (background ? statusColors.length / 2 : 0);
		Color color = statusColors[index];
		if (color == null)
			color = createColorForStatus(status, background);
		return statusColors[index] = color;
	}

	private Color createColorForStatus(int status, boolean background) {
		if ((status & EHEP.CELL_STATUS_SELECTED) > 0)
			return EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(),
					background ? EHEP.PROPERTY_COLOR_BACKGROUND_SELECTED : EHEP.PROPERTY_COLOR_FOREGROUND_SELECTED));
		switch (status) {
		default:
			return EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(),
					background ? EHEP.PROPERTY_COLOR_BACKGROUND_TABLE : EHEP.PROPERTY_COLOR_FOREGROUND_TABLE));
		case EHEP.CELL_STATUS_CHANGED:
			return EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(),
					background ? EHEP.PROPERTY_COLOR_BACKGROUND_CHANGED_CELL : EHEP.PROPERTY_COLOR_FOREGROUND_CHANGED_CELL));
		case EHEP.CELL_STATUS_INSERTED:
			return EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(),
					background ? EHEP.PROPERTY_COLOR_BACKGROUND_INSERTED_CELL : EHEP.PROPERTY_COLOR_FOREGROUND_INSERTED_CELL));
		case EHEP.CELL_STATUS_UNDO:
			return EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(),
					background ? EHEP.PROPERTY_COLOR_BACKGROUND_UNDO : EHEP.PROPERTY_COLOR_FOREGROUND_UNDO));
		case EHEP.CELL_STATUS_FINDRESULT:
			return EhepPlugin.getColor(PreferenceConverter.getColor(EhepPlugin.getDefault().getPreferenceStore(),
					background ? EHEP.PROPERTY_COLOR_BACKGROUND_FIND_RESULT : EHEP.PROPERTY_COLOR_FOREGROUND_FIND_RESULT));
		}
	}

	/**
	 * Sets the pop up menu.
	 * <p>
	 * Every control has an optional pop up menu that is
	 * displayed when the user requests a popup menu for
	 * the control.  The sequence of key strokes/button
	 * presses/button releases that is used to request
	 * a pop up menu is platform specific.
	 *
	 * @param menu the new pop up menu
	 *
	 * @exception SWTError <ul>
	 *          <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
	 *          <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
	 *          <li>ERROR_MENU_NOT_POP_UP when the menu is not a POP_UP</li>
	 *          <li>ERROR_NO_COMMON_PARENT when the menu is not in the same widget tree</li>
	 * </ul>
	 * </p>
	 */
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		table.setMenu(menu);
	}

	/**
	 * Sets the focus
	 */
	public boolean setFocus() {
		super.setFocus();
		return table.setFocus();
	}

	/**
	 * Shows the selection.
	 * <p>
	 * If there is no selection or the selection
	 * is already visible, this method does nothing.
	 * If the selection is scrolled out of view,
	 * the top index of the widget is changed such
	 * that selection becomes visible.
	 *
	 * @exception SWTError <ul>
	 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
	 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed
	 * </ul>
	 * </p
	 */
	public void showSelection() {
		table.showSelection();
	}

	/**
	 * @see #computeSize
	 */
	public void pack() {
		checkWidget();
		setSize(computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	/**
	 * Recalculates the size
	 */
	public void onResize() {
		Rectangle area = getClientArea();
		table.setBounds(0, 0, area.width, area.height);
	}

	/**
	 * Sets the focus (event handler)
	 */
	public void onFocusIn() {
		table.setFocus();
	}

	private void onSetData(TableItem item) {
		updateItem(item, -1, true, true, true);
	}

	/**
	 * Sets the size of the underlying data buffer. The buffer is newly created.
	 * 
	 * @param size the new size
	 */
	public void setBufferSize(int size) {
		data = new short[size + size % threshold];
		length = size;
	}
	
	public int getBufferSize() {
		return length;
	}

	/**
	 * Adds data to the hex table (at the end)
	 * @param index zero-based index
	 * @param buffer with data
	 * @param dataAvailable amount of data available in the buffer
	 */
	public void addData(int index, byte[] buffer, int dataAvailable) {
		if (dataAvailable < 0 || buffer == null || buffer.length == 0) {
			//
			// No data available or no buffer specified - do nothing
			//
			return;
		}
		
		int n = Math.min(index + dataAvailable, length);
		for (int i = 0, j = index; j < n && i < buffer.length; i++, j++) {
			data[j] = (short) (buffer[i] & 0x00ff);
		}
	}

	/**
	 * Inserts new data into the table from the specified position
	 * @param dataSize
	 * @param dataValue
	 * @param row
	 * @param column
	 * @return true for success
	 */
	public boolean insertData(int dataSize, int dataValue, int row, int column) {
		if (EhepPlugin.isDebugMode())
			EhepPlugin.log("insertData(): dataSize=" + dataSize + ", dataValue=" + dataValue + ", rowIndex=" + row + ", columnIndex=" + column); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		int offset = row * EHEP.TABLE_NUM_DATA_COLUMNS + column;
		if (offset < 0 || offset > length)
			return false;
		int newSize = length + (dataSize == 1 ? threshold - (length % threshold) : dataSize);
		if (newSize > data.length) {
			// resize the underlying data
			short[] tmp = new short[newSize];
			System.arraycopy(data, 0, tmp, 0, offset);
			System.arraycopy(data, offset, tmp, offset + dataSize, length - offset);
			data = tmp;
		} else {
			System.arraycopy(data, offset, data, offset + dataSize, length - offset);
		}
		length += dataSize;

		table.setItemCount(getItemCount());
		//
		// Put new data to the table
		//
		setData(row, column, dataSize, (byte) dataValue);
		return true;
	}

	/**
	 * Appends new data at the end of the table
	 * @param dataSize
	 * @param dataValue
	 */
	public void appendData(int dataSize, int dataValue) {
		if (dataSize <= 0)
			return;
		
		int oldLength = length;
		int newSize = length + (dataSize == 1 ? threshold - (length % threshold) : dataSize);
		if (newSize > data.length) {
			// resize the underlying data
			System.arraycopy(data, 0, data = new short[newSize], 0, oldLength);
		}
		length += dataSize;
		
		table.setItemCount(getItemCount());

		int row = oldLength / EHEP.TABLE_NUM_DATA_COLUMNS;
		int column = oldLength % EHEP.TABLE_NUM_DATA_COLUMNS;

		setData(row, column, dataSize, (byte) dataValue);
	}

	/**
	 * Deletes specified amount of data from the table from specified position
	 * @param delSize
	 * @param row
	 * @param column
	 */
	public void deleteData(int delSize, int row, int column) {
		if (EhepPlugin.isDebugMode())
			EhepPlugin.log("deleteData(): delSize=" + delSize + ", rowIndex=" + row + ", columnIndex=" + column); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		//
		// rowIndex, columnIndex are zero-based indexes to data-part of the table
		//
		if (delSize == 0)
			return;
		
		int tableSizeFromCursor = getTableSize(row, column);
		if (delSize == -1 || tableSizeFromCursor <= delSize)
			delSize = tableSizeFromCursor;
		
		int offset = row * EHEP.TABLE_NUM_DATA_COLUMNS + column;
		length -= delSize;
		System.arraycopy(data, offset + delSize, data, offset, length - offset);

		boolean scrolled = false;
		int itemCount = getItemCount();
		// if the last row is is visible and the first row is not visible,
		// the table is scrolled on item deletion
		// as a result the cursor must be shown again
		if (visibleRows.x > 0 && visibleRows.y == itemCount) {
			scrolled = true;
		}
		
		table.setItemCount(itemCount);

		if (scrolled) {
			hexEditorControl.getCursor().setVisible(true);
			hexEditorControl.getCursor().setFocus();
		}
	}

	/**
	 * Refreshes the visible part of the table
	 */
	public void updateVisibleTable(boolean includeOffset, boolean includeNumbers, boolean includeCharacters) {
		determineVisibleRows();
		for (int i = visibleRows.x; i <= visibleRows.y; i++) {
			updateItem(null, i, includeOffset, includeNumbers, includeCharacters);
		}
	}
	
	public boolean isInVisibleRegion(int row) {
		return row >= visibleRows.x && row <= visibleRows.y;
	}

	/**
	 * Compresses the size of all table columns
	 */
	public void packColumns() {
		for (int i = 0; i < EHEP.TABLE_NUM_COLUMNS; i++) {
			TableColumn tableColumn = table.getColumn(i);
			tableColumn.pack();
			tableColumn.setWidth(tableColumn.getWidth() * 6 / 5);
		} // for
	}
	
	/*
	 * item specific functions
	 */

	public TableItem getTableItem(int row) {
		return getTable().getItem(row);
	}
	
	private int getDataIndex(int row, int column) {
		return row * EHEP.TABLE_NUM_DATA_COLUMNS + column;
	}
	
	public byte getData(int dataIndex) {
		if (dataIndex < 0 || dataIndex >= length)
			return 0;
		return (byte) data[dataIndex];
	}

	/**
	 * Copies data from the table from certain row into the internal buffer.
	 * @param buffer data buffer
	 * @param dataIndex
	 * @param size
	 * @return number of bytes copied into the buffer
	 */
	public int getData(byte[] buffer, int dataIndex, int size) {
		int n = Math.min(length, dataIndex + size);
		for (int i = dataIndex, j = 0; i < n; i++, j++) {
			buffer[j] = (byte) (data[i] & 0xff);
		}
		return n - dataIndex;
	}

	public void setData(byte[] buffer, int dataIndex, int size) {
		int n = Math.min(length, dataIndex + size);
		for (int i = dataIndex, j = 0; i < n; i++, j++) {
			data[i] = (short) (buffer[j] & 0xff);
		}
	}

	public void setData(int row, int column, int size, byte b) {
		int dataIndex = getDataIndex(row, column);
		if (dataIndex < 0 || dataIndex >= length)
			return;
		int n = Math.min(dataIndex + size, length);
		for (int i = dataIndex; i < n; i++)
			data[i] = (short) ((data[i] & 0xff00) | (0x00ff & b));
	}
	
	public int getCellStatus(int row, int column) {
		int dataIndex = getDataIndex(row, column);
		if (dataIndex < 0 || dataIndex >= length)
			return 0;
		return data[dataIndex] >> 8;
	}

	public void setCellStatus(int row, int column, int size, int status) {
		setCellStatus(row, column, size, status, 0xff);
	}

	private void setCellStatus(int row, int column, int size, int status, int mask) {
		int dataIndex = getDataIndex(row, column);
		if (dataIndex < 0 || dataIndex >= length)
			return;
		int n = Math.min(dataIndex + size, length);
		for (int i = dataIndex; i < n; i++) {
			data[i] = (short) ((data[i] & mask) | status << 8);
		}
	}
	
	public void updateItem(TableItem item, int row, boolean includeOffset, boolean includeNumbers, boolean includeCharacters) {
		if (item == null)
			item = getTableItem(row);
		else
			row = table.indexOf(item);
			
		//
		// Define the offset column value
		//
		if (includeOffset)
			item.setText(0, Utils.int2string(16 * row) + ":"); //$NON-NLS-1$
	
		int dataIndex = getDataIndex(row, 0);
		if (includeNumbers) {
			int n = Math.min(length - dataIndex, EHEP.TABLE_NUM_DATA_COLUMNS);
			for (int i = 0, j = 1; i < n; i++, j++) {
				short b = data[dataIndex + i];
				item.setText(j, Utils.byte2string((byte) b));
				int status = (b >> 8) & 0xff;
				item.setBackground(j, getColorForStatus(status, true));
				item.setForeground(j, getColorForStatus(status, false));
			}
			while (n++ < EHEP.TABLE_NUM_DATA_COLUMNS) {
				item.setText(n, ""); //$NON-NLS-1$
				item.setBackground(n, getColorForStatus(EHEP.CELL_STATUS_NORMAL, true));
				item.setForeground(n, getColorForStatus(EHEP.CELL_STATUS_NORMAL, false));
			}
		}
		
		if (includeCharacters) {
			String encoding = hexEditorControl.getCurrentEncoding();
			StringBuffer stringData = new StringBuffer(EHEP.TABLE_NUM_DATA_COLUMNS);
			
			// row data as byte array
			byte[] rowData = new byte[EHEP.TABLE_NUM_DATA_COLUMNS];
			for(int i = 0; i < EHEP.TABLE_NUM_DATA_COLUMNS; i++)
				rowData[i] = (byte) data[dataIndex + i];
			
			String str = "";
			try {
				str = new String(rowData, encoding);
			} catch (UnsupportedEncodingException e) {
				str = new String(rowData);
			}
			str = str.replaceAll("[\\t\\n\\f\\r]", ".");
			stringData.append(str);
	
			item.setText(EHEP.TABLE_NUM_DATA_COLUMNS + 1, stringData.toString());
		}
	}
}
