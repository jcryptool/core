/*==========================================================================
 * 
 * HexEditorControl.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.17 $
 * $Date: 2012/11/06 16:45:23 $
 * $Name:  $
 *
 * Created on 10-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net) 
 *==========================================================================*/
package net.sourceforge.ehep.gui;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.actions.ActionFactory;

import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.core.EHEP;
import net.sourceforge.ehep.core.Encoder;
import net.sourceforge.ehep.core.Utils;
import net.sourceforge.ehep.editors.HexEditor;
import net.sourceforge.ehep.events.CursorKeyListener;

/**
 * The main plug-in GUI control
 * 
 * @author Marcel Palko alias Randallco
 * @author Uwe Voigt
 * @author randallco@users.sourceforge.net
 */
public class HexEditorControl {
	private final HexEditor hexEditor;
	private final Composite parent;
	private TableCursor cursor;
	private HexTable table;
	private Encoder encoder;
	private Vector<String> encodingTypes;
	private Combo encodingCombo;
	private Composite statusPanel;
	private Label statusOffset;
	private Label statusValue;
	private Label statusFileSize;
	private int findIndex = -1;
	private byte[] findPrevStatus;
	private Composite findRuler;
	private Composite hoverControl;
	private Text findText;
	private Text replaceText;
	private boolean rulerVisible;
	private Clipboard clipboard;
	private CursorKeyListener cursorKeyListener;
	private NumberFormat fileSizeFormat = NumberFormat.getInstance();
	
	/**
	 * Creates an instance of a HexEditorControl embedded inside
	 * the supplied parent Composite.
	 * 
	 * @param parent the container of the example
	 * @param file the file descriptor
	 */
	public HexEditorControl(final HexEditor hexEditor, final Composite parent) {
		this.parent = parent;
		this.hexEditor = hexEditor;
		encoder = new Encoder(parent);

		//
		// Get list of supported encodings
		//
		encodingTypes = encoder.getEncodingTypes();
		
		//
		// Define parent layout
		//
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 4;
		layout.marginHeight = 0;
		parent.setLayout(layout);
		
		hoverControl = new Composite(parent, SWT.None);
		layout = new GridLayout();
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginHeight = 2;
		hoverControl.setLayout(layout);
		hoverControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		hoverControl.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (rulerVisible)
					return;
				Rectangle cl = hoverControl.getClientArea();
				RGB rgb = e.gc.getBackground().getRGB();
				rgb.blue /= 1.5; rgb.green /= 1.5; rgb.red /= 1.5; 
				e.gc.setBackground(EhepPlugin.getColor(rgb));
				drawTriangle(e, cl.width / 2 - cl.height, 0, 2 * cl.height, cl.height);
			}
			private void drawTriangle(PaintEvent e, int x, int y, int width, int height) {
				e.gc.fillPolygon(new int[] { x, y , x + width / 2, y + height, x + width, y });
			}
		});
		
		//
		// Create main panel inside the parent panel
		//
		Composite mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setLayout(new RulerLayout());
		mainPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		//
		// Create top panel inside the main panel
		//
		createTopPanel(mainPanel);

		//
		// Create data panel inside the main panel
		//
		createDataPanel(mainPanel);

		//
		// Create bottom panel inside the main panel
		//
		createBottomPanel(mainPanel);
		
		Composite spacer = new Composite(parent, SWT.None);
		layout = new GridLayout();
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginHeight = 4;
		spacer.setLayout(layout);
		spacer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Listener hoverListener = new Listener() {
			public void handleEvent(Event e) {
				if (e.type == SWT.MouseEnter) {
					e.display.timerExec(1500, new Runnable() {
						public void run() {
							if (table == null)
								return;
							Point p = hoverControl.toControl(table.getDisplay().getCursorLocation());
							Rectangle cl = hoverControl.getClientArea();
							if (p.x >= 0 && p.x < cl.width && p.y >= 0 && p.y < cl.height)
								setRulerVisible(true);
						}
					});
				} else if (e.type == SWT.MouseDown) {
					setRulerVisible(true);
				}
			}
		};
		hoverControl.addListener(SWT.MouseEnter, hoverListener);
		hoverControl.addListener(SWT.MouseDown, hoverListener);
	}
	
	/**
	 * We use two stacks to store undo & redo information 
	 */
	private Stack<IHexEditorCommand> undolist = new Stack<IHexEditorCommand>();
	private Stack<IHexEditorCommand> redolist = new Stack<IHexEditorCommand>();
	
	/** 
	 * Undo a command stored in undolist, and move this command 
	 * in redolist
	 */	
	public void undo(){
		if(undolist.empty())
			return;
		
		IHexEditorCommand comm;
		comm = (IHexEditorCommand)undolist.pop();
		if (comm == null)
			return;
		comm.revoke();		
		redolist.push(comm);
		
		updateStatusPanel();
		cursor.update();
		updateActions();
	}
	
	/** 
	 * Redo a command stored in redolist, and move this command 
	 * in undolist
	 */
	public void redo(){
		if(redolist.empty())
			return;
		
		IHexEditorCommand comm;
		comm = (IHexEditorCommand)redolist.pop();
		if (comm == null)
			return;
		comm.execute();		
		undolist.push(comm);
		
		updateStatusPanel();
		updateActions();
		cursor.update();
	}
	
	public boolean canUndo() {
		return !undolist.isEmpty();
	}

	public boolean canRedo() {
		return !redolist.isEmpty();
	}
	
	public boolean canPaste() {
		return getClipboard().getContents(BinaryTransfer.instance) instanceof byte[] ||
				getClipboard().getContents(TextTransfer.getInstance()) instanceof String;
	}
	
	private void updateActions() {
		hexEditor.getEditorSite().getActionBars().getGlobalActionHandler(ActionFactory.UNDO.getId()).setEnabled(canUndo());
		hexEditor.getEditorSite().getActionBars().getGlobalActionHandler(ActionFactory.REDO.getId()).setEnabled(canRedo());
		hexEditor.getEditorSite().getActionBars().getGlobalActionHandler(ActionFactory.PASTE.getId()).setEnabled(canPaste());
	}

	/**
	 * Executes the command and adds a command to undolist, then redolist is cleared.
	 * undolist.size() will always be less than PROPERTY_MAX_UNDO_STEPS 
	 * @param comm
	 */
	private void addUndoStep(IHexEditorCommand comm){
		if (!comm.execute())
			return;
		int maxundo = EhepPlugin.getDefault().getPreferenceStore().getInt(EHEP.PROPERTY_MAX_UNDO_STEPS);		

		undolist.push(comm);
		redolist.clear();
		
		if(maxundo>0 && undolist.size()>maxundo){
			undolist.remove(0);
		}
		
		hexEditor.setDirty(true);
		updateActions();
	}

	interface IHexEditorCommand {
		/**
		 * @return true if something has been done, false else
		 */
		boolean execute();
		void revoke();
	}
	
	/**
	 * Represents modification of bytes without insertion or deletion
	 */
	class ModifyCommand implements IHexEditorCommand{
		private int row, col;
		private byte[] values;
		private boolean singleCommand;
		
		ModifyCommand(int rowIndex, int columnIndex, byte[] values, boolean singleCommand){
			row = rowIndex;
			col = columnIndex;
			this.values = values;
			this.singleCommand = singleCommand;
		}
		
		public boolean execute(){
			if (!core(EHEP.CELL_STATUS_CHANGED))
				return false;

			return row * EHEP.TABLE_NUM_DATA_COLUMNS + col < table.getBufferSize();
		}
		
		/**
		 * 
		 */
		private boolean core(int status) {
			byte[] oldBytes = new byte[values.length];
			int dataIndex = row * EHEP.TABLE_NUM_DATA_COLUMNS + col;
			if (dataIndex < 0 || dataIndex >= table.getBufferSize())
				return false;
			table.getData(oldBytes, dataIndex, values.length);
			table.setData(values, dataIndex, values.length);
			values = oldBytes;
			
			//
			// Mark row that it was changed
			//
			table.setCellStatus(row, col, values.length, status);

			HexTablePointer p = new HexTablePointer(row, col).move(values.length);
			for (int i = row; i <= p.getRowIndex(); i++) {
				if (table.isInVisibleRegion(i))
					table.updateItem(null, i, false, true, true);
			}
			return true;
		}

		//
		//Just the same as execute()
		//
		public void revoke(){
			core(EHEP.CELL_STATUS_UNDO);
			if (singleCommand) {
				table.select(row);
				cursor.setSelection(row, col + 1);
			}
		}
	}
	
	public void modify(int rowIndex, int columnIndex, byte[] values){
		addUndoStep(new ModifyCommand(rowIndex, columnIndex, values, true));
	}
	
	/** 
	 * Reprensents the delete commands
	 */
	class DeleteCommand implements IHexEditorCommand{
		private int size, row, col;
		private boolean singleCommand;
		private HexTableBuffer buffer;
		
		DeleteCommand(int rowIndex, int columnIndex, int delSize, boolean singleCommand){
			size = delSize;
			row = rowIndex;
			col = columnIndex;
			this.singleCommand = singleCommand;
			
			int tableSizeFromCursor = table.getTableSize(rowIndex, columnIndex);
			if (delSize == -1 || tableSizeFromCursor <= delSize)
				size = tableSizeFromCursor;
		}
		
		public boolean execute(){
			if (row * EHEP.TABLE_NUM_DATA_COLUMNS + col >= table.getBufferSize())
				return false;
			HexTablePointer p1 = new HexTablePointer(row, col);
			HexTablePointer p2 = new HexTablePointer(row, col).move(size-1);
			
			buffer = new HexTableBuffer(table);
			if (!buffer.storeTableRegion(p1,p2))
				return false;
			
			int before = table.getBufferSize();
			try {
				table.deleteData(size, row, col);
			} catch (OutOfMemoryError e) {
				MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.HexEditorControl_0 + e);
				return false;
			}
			table.updateVisibleTable(true, true, true);
			if (singleCommand) {
				HexTablePointer p = new HexTablePointer(table.getCellSelection().x);
				if (table.getItemCount() > p.getRowIndex())
					cursor.setSelection(p.getRowIndex(), p.getColumnIndex() + 1);
			}
			cursor.redraw();
			return before != table.getBufferSize();
		}
		
		public void revoke(){
			table.insertData(size, 0, row, col);
			buffer.restoreTableRegion(new HexTablePointer(row, col));
			table.setCellStatus(row, col,size, EHEP.CELL_STATUS_UNDO);
			table.updateVisibleTable(true, true, true);
			if (singleCommand) {
				table.select(row);
				cursor.setSelection(row, col + 1);
			}
		}
		
		public void dispose() {
			if (buffer != null)
				buffer.dispose();
		}
	}
	
	public void delete(int rowIndex, int columnIndex, int size){
		addUndoStep(new DeleteCommand(rowIndex, columnIndex, size, true));
	}
	
	/** 
	 * Reprensents the insert commands
	 */
	class InsertDataCommand implements IHexEditorCommand{
		private int size, row, col, value;
		private boolean singleCommand;
	
		InsertDataCommand(int dataSize, int dataValue, int rowIndex, int columnIndex, boolean singleCommand){
			size = dataSize;
			row = rowIndex;
			col = columnIndex;
			value = dataValue;
			this.singleCommand = singleCommand;
		}
		
		public boolean execute(){
			table.setCellSelection(row, col, false);
			int before = table.getBufferSize();
			try {
				if (!table.insertData(size, value, row, col))
					return false;
			} catch (OutOfMemoryError e) {
				MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.HexEditorControl_1 + e);
				return false;
			}
			table.setCellStatus(row, col,size, EHEP.CELL_STATUS_INSERTED);
			table.updateVisibleTable(true, true, true);
			return before != table.getBufferSize();
		}
		
		public void revoke(){
			table.deleteData(size, row, col);
			table.updateVisibleTable(true, true, true);
			if (singleCommand) {
				table.select(row);
				cursor.setSelection(row, col + 1);
			}
		}
	}
	
	public void insertData(int dataSize, int dataValue, int rowIndex, int columnIndex){
		addUndoStep(new InsertDataCommand(dataSize, dataValue, rowIndex, columnIndex, true));
	}
	
	/** 
	 * Reprensents the append data command
	 */
	class AppendDataCommand implements IHexEditorCommand{
		private int size, row, col, value;
	
		AppendDataCommand(int dataSize, int dataValue){
			size = dataSize;
			value = dataValue;

			row = table.getBufferSize() / EHEP.TABLE_NUM_DATA_COLUMNS;
			col = table.getBufferSize() % EHEP.TABLE_NUM_DATA_COLUMNS;
		}
		
		public boolean execute(){
			int before = table.getBufferSize();
			try {
				table.appendData(size, value);
			} catch (OutOfMemoryError e) {
				MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.HexEditorControl_2 + e);
				return false;
			}
			table.setCellStatus(row, col, size, EHEP.CELL_STATUS_INSERTED);
			table.updateVisibleTable(true, true, true);
			return before != table.getBufferSize();
		}
		
		public void revoke(){			
			table.deleteData(size, row, col);
			table.updateVisibleTable(true, true, true);
			table.select(row);
		}
	}
	
	public void appendData(int dataSize, int dataValue){
		addUndoStep(new AppendDataCommand(dataSize, dataValue));
	}
	
	private abstract class CombinedCommand implements IHexEditorCommand {
		protected List<IHexEditorCommand> commands = new ArrayList<IHexEditorCommand>();

		protected boolean executeCommand(IHexEditorCommand command) {
			if (!command.execute())
				return false;
			commands.add(command);
			return true;
		}
		
		public void revoke() {
			doRevoke(null);
		}

		protected void doRevoke(IProgressMonitor monitor) {
			for (int i = commands.size() - 1; i >= 0; i--) {
				final int index = i;
				table.getDisplay().syncExec(new Runnable() {
					public void run() {
						commands.get(index).revoke();
					}
				});
				if (monitor != null) {
					if (monitor.isCanceled())
						break;
					monitor.worked(1);
				}
			}
			commands.clear();
		}
	}
	
	private class FindReplaceCommand extends CombinedCommand {
		private String findString;
		private boolean findHex;
		private String replacement;
		private boolean replaceHex;
		private boolean forward;
		private boolean matchCase;
		private boolean wrap;
		private boolean replaceFind;
		private boolean all;
		private HexTablePointer position;
		
		public FindReplaceCommand(HexTablePointer position, String findString, boolean findHex,
				String replacement, boolean replaceHex, boolean forward,
				boolean matchCase, boolean wrap, boolean replaceFind, boolean all) {
			this.position = position;
			this.findString = findString;
			this.findHex = findHex;
			this.replacement = replacement;
			this.replaceHex = replaceHex;
			this.forward = forward;
			this.matchCase = matchCase;
			this.wrap = wrap;
			this.replaceFind = replaceFind;
			this.all = all;
		}

		public boolean execute() {
			byte[] findBytes = null;
			byte[] lowerCaseBytes = null;
			if (findHex) {
				findBytes = Utils.string2bytes(findString);
				lowerCaseBytes = findBytes;
				matchCase = true;
			} else {
				try {
					findBytes = findString.getBytes(getCurrentEncoding());
					lowerCaseBytes = findString.toLowerCase().getBytes(getCurrentEncoding());
				} catch (UnsupportedEncodingException e) {
					return false;
				}
			}
			byte[] replacementBytes = null;
			if (replacement != null) {
				if (replaceHex) {
					replacementBytes = Utils.string2bytes(replacement);
				} else {
					try {
						replacementBytes = replacement.getBytes(getCurrentEncoding());
					} catch (UnsupportedEncodingException e) {
						return false;
					}
				}
			}

			int matchCount = all ? runSearchLoopWithProgress(findBytes, lowerCaseBytes, replacementBytes)
					: runSearchLoop(findBytes, lowerCaseBytes, replacementBytes, null);
			return matchCount > 0;
		}
		
		private int runSearchLoopWithProgress(final byte[] findBytes, final byte[] lowerCaseBytes, final byte[] replacementBytes) {
			final int[] matchCount =  { 0 };
			try {
				hexEditor.getEditorSite().getWorkbenchWindow().run(true, true, new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(Messages.HexEditorControl_3, IProgressMonitor.UNKNOWN);
						try {
							matchCount[0] = runSearchLoop(findBytes, lowerCaseBytes, replacementBytes, monitor);
						} finally {
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException e) {
				EhepPlugin.log(e);
				MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.HexEditorControl_4 + e.getTargetException());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return matchCount[0];
		}

		private int runSearchLoop(byte[] findBytes, byte[] lowerCaseBytes, byte[] replacementBytes, IProgressMonitor monitor) {
			int matchCount = 0;
			final boolean[] wrapped = { false };
			HexTablePointer found = position;
			if (all)
				position = new HexTablePointer(0);
			if (all || replacementBytes == null)
				found = findAndSelect(position, findBytes, lowerCaseBytes, forward, matchCase, !all);
			if (wrap && found == null) {
				found = findAndSelect(new HexTablePointer(forward ? 0 : table.getBufferSize()), findBytes, lowerCaseBytes, forward, matchCase, !all);
				if (found != null) {
					table.getDisplay().syncExec(new Runnable() {
						public void run() {
							table.getDisplay().beep();
							hexEditor.getEditorSite().getActionBars()
									.getStatusLineManager().setMessage(forward ?
											Messages.HexEditorControl_5 : Messages.HexEditorControl_6);
							wrapped[0] = true;
						}
					});
				}
			}
			while (replacementBytes != null && found != null) {
				if (monitor != null && monitor.isCanceled())
					break;
				if (!executeReplaceOperations(findBytes, replacementBytes, found))
					break;
				matchCount++;
				
				if (replaceFind || all) {
					found = findAndSelect(found, findBytes, lowerCaseBytes, true, matchCase, !all);
					if (replaceFind)
						break;
				} else {
					break;
				}
			}
			indicateSearchResult(matchCount, found, wrapped[0]);
			return matchCount;
		}

		private boolean executeReplaceOperations(final byte[] findBytes, final byte[] replacementBytes, final HexTablePointer found) {
			final boolean[] result = { false };
			table.getDisplay().syncExec(new Runnable() {
				public void run() {
					result[0] = insertAndReplace(found, findBytes.length, replacementBytes, FindReplaceCommand.this);
				}
			});
			return result[0];
		}

		private void indicateSearchResult(final int matchCount, final HexTablePointer found, final boolean wrapped) {
			table.getDisplay().syncExec(new Runnable() {
				public void run() {
					// in monitor.done() the status line message is deleted, that's why we defer setting the message here
					table.getDisplay().timerExec(100, new Runnable() {
						public void run() {
							findText.setBackground(EhepPlugin.getColor(found == null && matchCount == 0 ? new RGB(255, 0, 0) : new RGB(255, 255, 255)));
							if (!wrapped)
								hexEditor.getEditorSite().getActionBars().getStatusLineManager().setMessage(found == null && matchCount == 0 ? Messages.HexEditorControl_7 : matchCount > 0 ? Messages.HexEditorControl_8 + matchCount + Messages.HexEditorControl_9 : null);
							updateStatusPanel();
						}
					});
				}
			});
		}
		
		public void revoke() {
			if (all) {
				try {
					hexEditor.getEditorSite().getWorkbenchWindow().run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							monitor.beginTask(Messages.HexEditorControl_10, commands.size());
							try {
								doRevoke(monitor);
							} finally {
								monitor.done();
							}
						}
					});
				} catch (InvocationTargetException e) {
					EhepPlugin.log(e);
					MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.HexEditorControl_11 + e.getTargetException());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				doRevoke(null);
			}
		}
	}
	
	public void replace(String findString, boolean findHex, String replacement, boolean replaceHex, boolean forward, boolean matchCase, boolean wrap, boolean replaceFind, boolean all) {
		addUndoStep(new FindReplaceCommand(getCursorPosition(), findString, findHex, replacement, replaceHex, forward, matchCase, wrap, replaceFind, all));
	}

	private  void search(HexTablePointer position, String findString, boolean findHex, boolean forward, boolean matchCase, boolean wrap) {
		addUndoStep(new FindReplaceCommand(position, findString, findHex, null, false, forward, matchCase, wrap, false, false));
	}
	
	private boolean insertAndReplace(HexTablePointer position, int selectionLength, byte[] content, CombinedCommand command) {
		if (content.length == selectionLength) {
			ModifyCommand modifyCommand = new ModifyCommand(position.getRowIndex(), position.getColumnIndex(), content, false);
			if (command.executeCommand(modifyCommand))
				return true;
		} else if (content.length > selectionLength) {
			HexTablePointer hp = new HexTablePointer(position.getRowIndex(), position.getColumnIndex());
			hp.move(selectionLength);
			InsertDataCommand insertDataCommand = new InsertDataCommand(content.length - selectionLength, 0, hp.getRowIndex(), hp.getColumnIndex(), false);
			if (!command.executeCommand(insertDataCommand))
				return false;
			ModifyCommand modifyCommand = new ModifyCommand(position.getRowIndex(), position.getColumnIndex(), content, false);
			if (command.executeCommand(modifyCommand))
				return true;
		} else {
			ModifyCommand modifyCommand = new ModifyCommand(position.getRowIndex(), position.getColumnIndex(), content, false);
			if (!command.executeCommand(modifyCommand))
				return false;
			HexTablePointer hp = new HexTablePointer(position.getRowIndex(), position.getColumnIndex());
			hp.move(content.length);
			DeleteCommand deleteCommand = new DeleteCommand(hp.getRowIndex(), hp.getColumnIndex(), selectionLength - content.length, false);
			if (command.executeCommand(deleteCommand))
				return true;
		}
		return false;
	}

	private class PasteCommand extends CombinedCommand {
		private Point selection;

		public PasteCommand(Point selection) {
			this.selection = selection;
		}

		public boolean execute() {
			HexTablePointer p = new HexTablePointer(selection.x);
			if (p.getColumnIndex() < 0 || p.getColumnIndex() >= EHEP.TABLE_NUM_DATA_COLUMNS)
				return false;
			Object contents = getClipboard().getContents(BinaryTransfer.instance);
			if (!(contents instanceof byte[])) {
				contents = getClipboard().getContents(TextTransfer.getInstance());
				if (!(contents instanceof String))
					return false;
				try {
					contents = ((String) contents).getBytes(getCurrentEncoding());
				} catch (UnsupportedEncodingException e) {
					return false;
				}
			}
			byte[] array = (byte[]) contents;
			// reduce the default selection length by one because when copying, it is ok to see the cursor itself
			// as selection, when pasting it is not
			if (selection.y == 1)
				selection.y--;
			boolean result = insertAndReplace(new HexTablePointer(selection.x), selection.y, array, this);
			table.setCellSelection(p.getRowIndex(), p.getColumnIndex(), false);
			p.move(array.length - 1);
			table.setCellSelection(p.getRowIndex(), p.getColumnIndex(), true);
			cursor.setSelection(p.getRowIndex(), p.getColumnIndex() + 1);
			cursor.redraw();
			// update the visible area because the scroll position may have been changed by the new cursor selection
			table.updateVisibleTable(true, true, true);
			updateStatusPanel();
			return result;
		}

		public void revoke() {
			doRevoke(null);
			HexTablePointer p = new HexTablePointer(selection.x);
			if (selection.y > 0)
				p.move(selection.y - 1);
			if (table.getTable().getItemCount() > p.getRowIndex())
				cursor.setSelection(p.getRowIndex(), p.getColumnIndex() + 1);
			table.setCellSelection(p.getRowIndex(), p.getColumnIndex(), true);
		}
	}
	
	public void paste() {
		addUndoStep(new PasteCommand(table.getCellSelection()));
	}

	static class BinaryTransfer extends ByteArrayTransfer {
		private static final BinaryTransfer instance = new BinaryTransfer();
		private static final String TYPE_NAME = "binary-transfer-format:" + System.currentTimeMillis() + ":" + instance.hashCode(); //$NON-NLS-1$ //$NON-NLS-2$
		private static final int TYPEID = registerType(TYPE_NAME);

		protected int[] getTypeIds() {
			return new int[] { TYPEID };
		}

		protected String[] getTypeNames() {
			return new String[] { TYPE_NAME };
		}
	}
	
	private Clipboard getClipboard() {
		if (clipboard == null)
			clipboard = new Clipboard(table.getDisplay());
		return clipboard;
	}
	
	public void copy() {
		HexTablePointer cp = getCursorPosition();
		if (cp.getColumnIndex() < 0 || cp.getColumnIndex() >= EHEP.TABLE_NUM_DATA_COLUMNS)
			return;
		Point selection = table.getCellSelection();
		if (selection.y <= 0)
			return;
		byte[] contents = new byte[selection.y];
		table.getData(contents, selection.x, selection.y);
		getClipboard().setContents(new Object[] { contents }, new Transfer[] { BinaryTransfer.instance });
	}
	
	public void cut() {
		HexTablePointer cp = getCursorPosition();
		if (cp.getColumnIndex() < 0 || cp.getColumnIndex() >= EHEP.TABLE_NUM_DATA_COLUMNS)
			return;
		Point selection = table.getCellSelection();
		if (selection.y <= 0)
			return;
		byte[] contents = new byte[selection.y];
		table.getData(contents, selection.x, selection.y);
		getClipboard().setContents(new Object[] { contents }, new Transfer[] { BinaryTransfer.instance });
		HexTablePointer p = new HexTablePointer(selection.x);
		delete(p.getRowIndex(), p.getColumnIndex(), contents.length);
	}
	
	public void deleteSelection() {
		HexTablePointer cp = getCursorPosition();
		if (cp.getColumnIndex() < 0 || cp.getColumnIndex() >= EHEP.TABLE_NUM_DATA_COLUMNS)
			return;
		Point selection = table.getCellSelection();
		if (selection.y <= 0)
			return;
		HexTablePointer p = new HexTablePointer(selection.x);
		delete(p.getRowIndex(), p.getColumnIndex(), selection.y);
	}
	
	private class RulerLayout extends Layout {
		protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
			Control[] children = composite.getChildren();
			Point size = new Point(0, 0);
			for (int i = 0; i < children.length; i++) {
				Point p = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
				size.x = Math.max(size.x, p.x);
				size.y += p.y;
			}
			return size;
		}

		protected void layout(Composite composite, boolean flushCache) {
			Rectangle area = composite.getClientArea();
			Control[] children = composite.getChildren();
			int[] heights = new int[children.length];
			int areaHeight = area.height;
			for (int i = 0; i < children.length; i++) {
				Point p = children[i].computeSize(SWT.DEFAULT, SWT.DEFAULT, flushCache);
				if (!"ruler".equals(children[i].getLayoutData()) || rulerVisible) { //$NON-NLS-1$
					heights[i] = p.y;
					areaHeight -= p.y;
				}
			}
			for (int i = 0, y = 0; i < children.length; i++) {
				if ("data".equals(children[i].getLayoutData())) //$NON-NLS-1$
					heights[i] += areaHeight;
				children[i].setBounds(0, y, area.width, heights[i]);
				y += heights[i];
			}
		}
	}
	
	/**
	 * Creates a combo box with all supported encoding types
	 * @param parent
	 * @return Combo 
	 */
	private Combo getEncodingCombo(Composite parent, Vector<String> encodingTypes) {
		Combo encodingCombo = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		
		for (int i = 0; i < encodingTypes.size(); i++) {
			encodingCombo.add(encodingTypes.get(i));
		} // for
		
		encodingCombo.select(0);
		return encodingCombo;
	}
	
	/**
	 * Returns table
	 * @return table
	 */
	public HexTable getHexTable() {
		return table;
	}
	
	/**
	 * Returns default encoding
	 * @return String default encoding
	 */
	public String getDefaultEncoding() {
		return encodingTypes.get(0);
	}
	
	/**
	 * Returns current encoding
	 * @return current encoding (String)
	 */
	public String getCurrentEncoding() {
		int selectedEncoding = encodingCombo.getSelectionIndex();
		return (selectedEncoding >= 0) ? encodingCombo.getItem(selectedEncoding) : getDefaultEncoding();
	}

	private void encodingChanged() {
		int selection = encodingCombo.getSelectionIndex();
		if (selection == -1) return;
		String encoding = encodingTypes.get(selection);
		table.updateVisibleTable(false, false, true);
		table.getTable().getColumn(EHEP.TABLE_NUM_COLUMNS-1).setText(encoding);
	}

	/**
	 * Grabs input focus.
	 */
	public void setFocus() {
		table.setFocus();
		table.select(0);
		updateActions();
	}

	/**
	 * Disposes of all resources associated with a particular
	 * instance of the HexEditorControl.
	 */	
	public void dispose() {
		if (table != null) {
			table.dispose();
			table = null;
		}
		
		if (cursor != null) {
			cursor.dispose();
			cursor = null;
		}
		
		if (clipboard != null) {
			clipboard.dispose();
		}
		
		disposeCommands(undolist);
		disposeCommands(redolist);
		
		encodingTypes = null;
	}

	private void disposeCommands(Stack<IHexEditorCommand> list) {
		for (IHexEditorCommand command : list) {
			if (command instanceof DeleteCommand)
				((DeleteCommand) command).dispose();
		}
		list.clear();
	}

	/**
	 * Creates top panel
	 * @param parent reference to a parent component
	 */
	private void createTopPanel(final Composite parent) {
		//
		// Create top panel
		//
		findRuler = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 0;
		layout.marginWidth = 2;
		layout.marginHeight = 0;
		findRuler.setLayout(layout);
		findRuler.setLayoutData("ruler"); //$NON-NLS-1$

		//
		// Create a panel with encoding selector
		//
		createEncodingPanel(findRuler, encodingTypes);
		createSearchPanel(findRuler);
	}
	
	public void toggleRulerVisibility() {
		setRulerVisible(!rulerVisible);
	}
	
	private void setRulerVisible(boolean visible) {
		rulerVisible = visible;
		findRuler.getParent().layout();
		if (rulerVisible) {
			findRuler.setEnabled(true);
			findText.setFocus();
		} else {
			findRuler.setEnabled(false);
		}
		hoverControl.redraw();
		hoverControl.update();
	}

	private void createSearchPanel(Composite parent) {
		Composite searchPanel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(6, false);
		layout.verticalSpacing = layout.horizontalSpacing = 0;
		layout.marginHeight = 0;
		searchPanel.setLayout(layout);
		searchPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label label = new Label(searchPanel, SWT.LEFT);
		label.setText(Messages.HexEditorControl_17);
		findText = new Text(searchPanel, SWT.LEFT | SWT.BORDER);
		findText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		ToolBar bar1 = new ToolBar(searchPanel, SWT.HORIZONTAL | SWT.FLAT);
		final ToolItem findHex = new ToolItem(bar1, SWT.CHECK);
		findHex.setText("0x"); //$NON-NLS-1$
		findHex.setToolTipText(Messages.HexEditorControl_19);
		final ToolItem next = new ToolItem(bar1, SWT.PUSH);
		next.setText(Messages.HexEditorControl_20);
		final ToolItem prev = new ToolItem(bar1, SWT.PUSH | SWT.FLAT);
		prev.setText(Messages.HexEditorControl_21);
		final ToolItem matchCase = new ToolItem(bar1, SWT.CHECK);
		matchCase.setText(Messages.HexEditorControl_22);
		final ToolItem wrap = new ToolItem(bar1, SWT.CHECK);
		wrap.setText(Messages.HexEditorControl_23);
		
		replaceText = new Text(searchPanel, SWT.LEFT | SWT.BORDER);
		replaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		ToolBar bar2 = new ToolBar(searchPanel, SWT.HORIZONTAL | SWT.FLAT);
		final ToolItem replaceHex = new ToolItem(bar2, SWT.CHECK);
		replaceHex.setText("0x"); //$NON-NLS-1$
		replaceHex.setToolTipText(Messages.HexEditorControl_25);
		final ToolItem replace = new ToolItem(bar2, SWT.PUSH | SWT.FLAT);
		replace.setText(Messages.HexEditorControl_26);
		final ToolItem replaceFind = new ToolItem(bar2, SWT.PUSH | SWT.FLAT);
		replaceFind.setText(Messages.HexEditorControl_27);
		final ToolItem replaceAll = new ToolItem(bar2, SWT.PUSH);
		replaceAll.setText(Messages.HexEditorControl_28);

		Listener replaceListener = new Listener() {
			public void handleEvent(Event event) {
				replace(findText.getText(), findHex.getSelection(), replaceText.getText(), replaceHex.getSelection(),
						true, matchCase.getSelection(), wrap.getSelection(),
						event.widget == replaceFind, event.widget == replaceAll);
			}
		};
		replace.addListener(SWT.Selection, replaceListener);
		replaceFind.addListener(SWT.Selection, replaceListener);
		replaceAll.addListener(SWT.Selection, replaceListener);
		
		class UpdateActions {
			boolean validateAndUpdate() {
				boolean findValid = validate(findHex, findText);
				boolean replaceValid = validate(replaceHex, replaceText);
				hexEditor.getEditorSite().getActionBars()
						.getStatusLineManager().setMessage(findValid && replaceValid ? null : Messages.HexEditorControl_29);
				boolean hasText = findText.getText().length() > 0;
				next.setEnabled(hasText && findValid);
				prev.setEnabled(hasText&& findValid);
				replace.setEnabled(hasText && findValid && replaceValid);
				replaceFind.setEnabled(hasText && findValid && replaceValid);
				replaceAll.setEnabled(hasText && findValid && replaceValid);
				return findValid && replaceValid;
			}

			private boolean validate(ToolItem item, Text text) {
				boolean valid = item.getSelection() && Utils.isValidHexString(text.getText(), true) || !item.getSelection();
				text.setBackground(EhepPlugin.getColor(valid ? new RGB(255, 255, 255) : new RGB(255, 0, 0)));
				return valid;
			}
		}
		
		final UpdateActions updateActions = new UpdateActions();

		Listener findListener = new Listener() {
			public void handleEvent(Event event) {
				if (!updateActions.validateAndUpdate())
					return;
				boolean forward = event.widget != prev;
				HexTablePointer position = getCursorPosition();
				if (event.type == SWT.DefaultSelection || event.widget == next)
					position.move(1);
				else if (event.widget == prev)
					position.move(-1);
				search(position, findText.getText(), findHex.getSelection(), forward, matchCase.getSelection(), wrap.getSelection());
			}
		};
		findText.addListener(SWT.Modify, findListener);
		findText.addListener(SWT.DefaultSelection, findListener);
		next.addListener(SWT.Selection, findListener);
		prev.addListener(SWT.Selection, findListener);

		Listener validateListener = new Listener() {
			public void handleEvent(Event event) {
				updateActions.validateAndUpdate();
			}
		};
		findHex.addListener(SWT.Selection, validateListener);
		replaceHex.addListener(SWT.Selection, validateListener);
		replaceText.addListener(SWT.Modify, validateListener);
		
		KeyAdapter keyListener = new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.ESC) {
					setRulerVisible(false);
					selectFindResult(true);
					table.updateVisibleTable(false, true, false);
				}
			}
		};
		findText.addKeyListener(keyListener);
		replaceText.addKeyListener(keyListener);
		
		Listener savePropertiesListener = new Listener() {
			public void handleEvent(Event event) {
				EhepPlugin.getDefault().getPreferenceStore().setValue(EHEP.PROPERTY_FIND_HEX, findHex.getSelection());
				EhepPlugin.getDefault().getPreferenceStore().setValue(EHEP.PROPERTY_REPLACE_HEX, replaceHex.getSelection());
				EhepPlugin.getDefault().getPreferenceStore().setValue(EHEP.PROPERTY_MATCH_CASE, matchCase.getSelection());
				EhepPlugin.getDefault().getPreferenceStore().setValue(EHEP.PROPERTY_WRAP, wrap.getSelection());
			}
		};
		findHex.addListener(SWT.Selection, savePropertiesListener);
		replaceHex.addListener(SWT.Selection, savePropertiesListener);
		matchCase.addListener(SWT.Selection, savePropertiesListener);
		wrap.addListener(SWT.Selection, savePropertiesListener);
		
		findHex.setSelection(EhepPlugin.getDefault().getPreferenceStore().getBoolean(EHEP.PROPERTY_FIND_HEX));
		replaceHex.setSelection(EhepPlugin.getDefault().getPreferenceStore().getBoolean(EHEP.PROPERTY_REPLACE_HEX));
		matchCase.setSelection(EhepPlugin.getDefault().getPreferenceStore().getBoolean(EHEP.PROPERTY_MATCH_CASE));
		wrap.setSelection(EhepPlugin.getDefault().getPreferenceStore().getBoolean(EHEP.PROPERTY_WRAP));

		updateActions.validateAndUpdate();
	}

	/**
	 * Creates a panel with encoding selector
	 * @param parent reference to a parent component
	 * @param encodingTypes vector with all supported encoding types
	 */
	private void createEncodingPanel(final Composite parent, final Vector<String> encodingTypes) {
		//
		// Create panel
		//
		Composite encodingPanel = new Composite(parent, SWT.NONE);
		encodingPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		encodingPanel.setLayout(layout);

		//
		// Create label
		//
		new Label(encodingPanel, SWT.NONE).setText(Messages.HexEditorControl_30);

		//
		// Create combo
		//
		encodingCombo = getEncodingCombo(encodingPanel, encodingTypes);

		//
		// Add selection listener to the combo box
		//
		encodingCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				encodingChanged();
			} // widgetSelected()
		});
	}

	/**
	 * Creates a data panel
	 * @param parent reference to a parent component
	 */
	private void createDataPanel(final Composite parent) {
		//
		// Create context (popup) menu
		//
		Menu menu = createPopupMenu();

		//
		// Create data panel
		//
		Composite dataPanel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		dataPanel.setLayout(layout);

		dataPanel.setLayoutData("data"); //$NON-NLS-1$

		//
		// Create table
		//
		table = new HexTable(dataPanel, this, SWT.VIRTUAL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
		table.getTable().setHeaderVisible(true);
		table.getTable().setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setData(encodingTypes);
		table.setMenu(menu);

		//
		// Create table cursor
		//
		cursor = new TableCursor(table.getTable(), SWT.NONE);
		cursor.setMenu(menu);

		//
		// Create an editor to edit the cell when the user hits 0..9 or A..F key 
		// while over a cell in the table
		//
		final ControlEditor editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;

		//
		// Add some cursor listeners
		//
		addCursorListeners(cursor, editor);

		//
		// Create table header
		//
		createTableHeader(table);
		
		table.initListeners();
		table.updateColors();
	}

	/**
	 * Creates bottom panel
	 * @param parent reference to a parent component
	 */
	private void createBottomPanel(final Composite parent) {
		//
		// Create bottom panel
		//
		Composite bottomPanel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 10;
		layout.verticalSpacing = 0;
		layout.marginWidth = 4;
		layout.marginHeight = 0;
		bottomPanel.setLayout(layout);

		//
		// Create and add a status panel
		//
		createStatusPanel(bottomPanel);
	}
	
	/**
	 * Creates a status panel
	 * @param parent reference to a parent component
	 */
	private void createStatusPanel(final Composite parent) {
		//
		// Create status panel
		//
		statusPanel = new Composite(parent, SWT.NONE);
		statusPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		statusPanel.setLayout(layout);

		//
		// Create status sub-panels
		//
		createOffsetPanel(statusPanel);
		createValuePanel(statusPanel);
		createFileSizePanel(statusPanel);
	}

	private void createOffsetPanel(Composite parent) {
		//
		// Offset panel
		//
		Composite offsetPanel = new Composite(parent, SWT.NONE);
		offsetPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		offsetPanel.setLayout(layout);
		
		statusOffset = new Label(offsetPanel, SWT.BORDER);
		statusOffset.setText(Messages.HexEditorControl_32);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		statusOffset.setLayoutData(gridData);
	}

	private void createValuePanel(Composite parent) {
		//
		// Value panel
		//
		Composite valuePanel = new Composite(parent, SWT.NONE);
		valuePanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		valuePanel.setLayout(layout);
		
		statusValue = new Label(valuePanel, SWT.BORDER);
		statusValue.setText(Messages.HexEditorControl_33);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		statusValue.setLayoutData(gridData);
	}

	private void createFileSizePanel(Composite parent) {
		//
		// FileSize panel
		//
		Composite fileSizePanel = new Composite(parent, SWT.NONE);
		fileSizePanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		fileSizePanel.setLayout(layout);
		
		statusFileSize = new Label(fileSizePanel, SWT.BORDER);
		statusFileSize.setText(Messages.HexEditorControl_34);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		statusFileSize.setLayoutData(gridData);
	}

	/**
	 * Adds some cursor listeners
	 * @param cursor table cursor
	 * @param editor table editor
	 */
	private void addCursorListeners(final TableCursor cursor, final ControlEditor editor) {
		//
		// Add key listener
		//
		cursorKeyListener = new CursorKeyListener(this, hexEditor, cursor, editor);
		
		cursor.addKeyListener(cursorKeyListener);

		//
		// Add selection listener
		//
		cursor.addSelectionListener(new SelectionAdapter() {
			//
			// When the TableEditor is over a cell, select the corresponding row in the table
			//
			public void widgetSelected(SelectionEvent e) {
				//
				// First check if there is an active cell editor and if it contains a valid value
				//
				if (cursorKeyListener.getText() != null) {
					String str = cursorKeyListener.getText().getText().toUpperCase();
					if (Utils.isValidHexNumber(str)) {
						cursorKeyListener.getCellEditorKeyListener().closeCellEditor(cursorKeyListener.getEventRow(), cursorKeyListener.getEventColumn(), str);
					} else {
						cursorKeyListener.setSelection();
						return;
					}
				}
				table.setSelection(new TableItem[] {cursor.getRow()});
				HexTablePointer p = getCursorPosition();
				table.setCellSelection(p.getRowIndex(), p.getColumnIndex(), cursorKeyListener.isShiftHold());
				
				updateStatusPanel();
			}

			//
			// When the user hits "ENTER" in the TableCursor, pop up a text editor so that 
			// they can change the text of the cell
			//
			public void widgetDefaultSelected(SelectionEvent e) {
				table.setSelection(new TableItem[] {cursor.getRow()});
			}
		});
	}

	/**
	 * Creates table header
	 * @param table data table
	 */
	private void createTableHeader(final HexTable table) {
		for (int i = 0; i < EHEP.TABLE_NUM_COLUMNS; i++) {
			TableColumn tableColumn = new TableColumn(table.getTable(), SWT.CENTER);
			tableColumn.setResizable(true);
			if (i == 0) {
				tableColumn.setText(Messages.HexEditorControl_35);
			} else if (i >= 1 && i <= EHEP.TABLE_NUM_DATA_COLUMNS) {
				tableColumn.setText(Integer.toHexString(i-1).toUpperCase());
			}
			else {
				tableColumn.setText(getCurrentEncoding());
			}
		} // for
	}

	public TableCursor getCursor() {
		return cursor;
	}
	
	public HexTablePointer getCursorPosition() {
		if (cursor.getRow() == null)
			return new HexTablePointer(0);
		return new HexTablePointer(table.getTable().indexOf(cursor.getRow()), cursor.getColumn() - 1);
	}
	
	/** 
	 * Creates all items located in the popup menu and associates
	 * all the menu items with their appropriate functions.
	 *
	 * @return	Menu The created popup menu.
	 */
	private Menu createPopupMenu() {
		Menu popUpMenu = new Menu(parent.getShell(), SWT.POP_UP);

		/** 
		 * Adds a listener to handle enabling and disabling 
		 * some items in the Edit submenu.
		 */
		popUpMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				Menu menu = (Menu) e.widget;
				MenuItem[] items = menu.getItems();
				int rowIndex = table.getSelectionIndex();
				int columnIndex = cursor.getColumn();
				int tableSize = table.getBufferSize();
				HexTablePointer p1 = new HexTablePointer(rowIndex, columnIndex-1);
				
				boolean isOffsetColumn = columnIndex == 0;
				boolean isCharacterColumn = columnIndex == EHEP.TABLE_NUM_COLUMNS - 1;
				boolean endOfBuffer = p1.getOffset() >= tableSize;
				boolean readonly = hexEditor.isReadOnly();
				
				boolean enableInsert = !isOffsetColumn && !isCharacterColumn && !endOfBuffer && !readonly;
				boolean enableDelete = !isOffsetColumn && !isCharacterColumn && !endOfBuffer && !readonly;
				boolean enableAppend = !readonly;
				
				items[0].setEnabled(enableInsert); // insert
				items[1].setEnabled(enableAppend); // append
				//--- separator ---
				items[3].setEnabled(canUndo());
				items[4].setEnabled(canRedo());
				//--- separator ---
				items[8].setEnabled(canPaste()); // paste
				items[9].setEnabled(enableDelete); // delete
				// --- separator ---
				items[11].setEnabled(true);        // goto
				// --- separator ---
				items[13].setEnabled(true);        // about
			} // menuShown()
		});


		//
		// "Insert" menu item
		//
		MenuItem item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.CASCADE*/);
		item.setText(Messages.HexEditorControl_37);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				popupMenuInsertData(table.getSelectionIndex(), cursor.getColumn());
			}
		});
	
		//
		// "Append" menu item
		//
		item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.CASCADE*/);
		item.setText(Messages.HexEditorControl_38);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				popupMenuAppendData();
			}
		});

		//
		// Separator
		//
		new MenuItem(popUpMenu, SWT.SEPARATOR);
		
		//
		// "Undo" and "Redo" menu item
		//
		item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.CASCADE*/);
		item.setText(Messages.HexEditorControl_39);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				hexEditor.undo();
			}
		});		
		
		item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.CASCADE*/);
		item.setText(Messages.HexEditorControl_40);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				hexEditor.redo();
			}
		});		
		//
		// Separator
		//
		new MenuItem(popUpMenu, SWT.SEPARATOR);	

		Listener copyPasteListener = new Listener() {
			public void handleEvent(Event event) {
				hexEditor.getEditorSite().getActionBars().getGlobalActionHandler(
						(String) event.widget.getData()).run();
			}
		};

		item = new MenuItem(popUpMenu, SWT.PUSH);
		item.setText(Messages.HexEditorControl_41);
		item.setData(ActionFactory.CUT.getId());
		item.addListener(SWT.Selection, copyPasteListener);

		item = new MenuItem(popUpMenu, SWT.PUSH);
		item.setText(Messages.HexEditorControl_42);
		item.setData(ActionFactory.COPY.getId());
		item.addListener(SWT.Selection, copyPasteListener);

		item = new MenuItem(popUpMenu, SWT.PUSH);
		item.setText(Messages.HexEditorControl_43);
		item.setData(ActionFactory.PASTE.getId());
		item.addListener(SWT.Selection, copyPasteListener);

		//
		// "Delete" menu item
		//
		item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.CASCADE*/);
		item.setText(Messages.HexEditorControl_44);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				popupMenuDeleteData(table.getSelectionIndex(), cursor.getColumn());
			}
		});

		//
		// Separator
		//
		new MenuItem(popUpMenu, SWT.SEPARATOR);	

		//
		// "Goto..." menu item
		//
		item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.CASCADE*/);
		item.setText(Messages.HexEditorControl_45);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				popupMenuGoto();
			}
		});

		//
		// Separator
		//
		new MenuItem(popUpMenu, SWT.SEPARATOR);	
	
		//
		// "About" menu item
		//
		item = new MenuItem(popUpMenu, SWT.PUSH/*SWT.NULL*/);
		item.setText(Messages.HexEditorControl_46);
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				popupMenuAbout();
			}
		});

		return popUpMenu;
	}

	/**
	 * Inserts new data (invoked from popup menu)
	 * @param rowIndex
	 * @param columnIndex
	 */
	private void popupMenuInsertData(int rowIndex, int columnIndex) {
		//
		// Display the dialog
		//
		AddDataDialog addDataDialog = new AddDataDialog(parent.getShell(), Messages.HexEditorControl_47, EHEP.DIALOG_TYPE_INSERT);
		if (addDataDialog.open() != Window.OK)
			return;
		int[] addDialogResult = addDataDialog.getResult();
		
		if (addDialogResult == null) {
			//
			// Cancel button pressed - do nothing
			//
			return;
		}
		
		//
		// Retrieve the parameters from the 'Insert' dialog
		//
		int dataSize = addDialogResult[0];
		int dataValue = addDialogResult[1];
		
		if (dataSize <= 0) return;
		
		//
		// Insert new bytes into the table
		//
		insertData(dataSize, dataValue, rowIndex, columnIndex-1);

		//
		// Update the status panel
		//
		updateStatusPanel();
	}

	/**
	 * Appends new data (invoked from popup menu)
	 */
	private void popupMenuAppendData() {
		//
		// Display the dialog
		//
		AddDataDialog addDataDialog = new AddDataDialog(table.getShell(), Messages.HexEditorControl_48, EHEP.DIALOG_TYPE_APPEND);
		if (addDataDialog.open() != Window.OK)
			return;
		int[] addDialogResult = addDataDialog.getResult();
		
		if (addDialogResult == null) {
			//
			// Cancel button pressed - do nothing
			//
			return;
		}
		
		//
		// Retrieve the parameters from the 'Insert' dialog
		//
		int dataSize = addDialogResult[0];
		int dataValue = addDialogResult[1];
		
		if (dataSize <= 0) return;
		
		//
		// Append new bytes into the table
		//
		appendData(dataSize, dataValue);

		//
		// Update the status panel
		//
		updateStatusPanel();
	}

	/**
	 * Deletes data from the table (invoked from popup menu)
	 * @param rowIndex
	 * @param columnIndex
	 */
	private void popupMenuDeleteData(int rowIndex, int columnIndex) {
		//
		// Display the dialog
		//
		DeleteDataDialog deleteDataDialog = new DeleteDataDialog(parent.getShell());
		if (deleteDataDialog.open() != Window.OK)
			return;
		int delSize = deleteDataDialog.getResult();
		
		if (delSize == 0) {
			//
			// Cancel button pressed - do nothing
			//
			return;
		}
		
		//
		// Delete data from the table
		delete(rowIndex, columnIndex-1, delSize);

		//
		// Update the status panel
		//
		updateStatusPanel();
	}

	/**
	 * Displays the 'Goto...' dialog
	 */
	public void popupMenuGoto() {
		int rowIndex = table.getSelectionIndex();
		int columnIndex = cursor.getColumn();
		//
		// Display the dialog
		//
		GotoDialog gotoDialog = new GotoDialog(parent.getShell(), rowIndex, columnIndex);
		if (gotoDialog.open() != Window.OK)
			return;
		HexTablePointer p = gotoDialog.getNewPosition();
		
		if (p == null) {
			//
			// Error calculating new position - do nothing
			//
			return;
		} // if
		
		if (p.getRowIndex() < 0 || p.getColumnIndex() < 0) {
			MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, Messages.HexEditorControl_49);
			cursor.setSelection(0, 1);
			cursor.redraw();
			table.setSelection(0);
			//
			// Update the status panel
			//
			updateStatusPanel();
			return;
		}
		
		try {
			cursor.setSelection(p.getRowIndex(), p.getColumnIndex()+1);
		}
		catch (IllegalArgumentException e) {
			int numRows = table.getItemCount();
			int lastRowIndex = (numRows > 0) ? (numRows-1) : numRows;
			MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_ERROR, "Address is out of range!");
			int lastColumnIndex = table.getBufferSize() % EHEP.TABLE_NUM_DATA_COLUMNS;
			cursor.setSelection(lastRowIndex, lastColumnIndex + 1);
			cursor.redraw();
			table.setSelection(lastRowIndex);
			//
			// Update the status panel
			//
			updateStatusPanel();
			return;
		}
		
		//
		// Place the cursor where was calculated
		//
		cursor.setSelection(p.getRowIndex(), p.getColumnIndex()+1);
		cursor.redraw();
		table.setSelection(p.getRowIndex());

		//
		// Update the status panel
		//
		updateStatusPanel();
	}

	/**
	 * Displays the 'About' dialog
	 */
	private void popupMenuAbout() {
		//
		// Display the dialog
		//
		AboutDialog aboutDialog = new AboutDialog(parent.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		aboutDialog.open();
	}

	/**
	 * Updates the status panel
	 */
	public void updateStatusPanel() {
		table.redraw();
		cursor.redraw();
		int rowIndex = table.getSelectionIndex();
		int columnIndex = cursor.getColumn();
		
		if (rowIndex < 0 || columnIndex < 0 || columnIndex >= EHEP.TABLE_NUM_COLUMNS) {
			EhepPlugin.log("updateStatusPanel(): incorrect cursor coordinates!"); //$NON-NLS-1$
			return;
		}
		
		String cellData = table.getTableItem(rowIndex).getText(columnIndex);
		
		if (columnIndex < 1 || columnIndex > EHEP.TABLE_NUM_DATA_COLUMNS || cellData.length() == 0 || cellData.equals(EHEP.TABLE_EMPTY_CELL)) {
			//
			// Cursor is out of data cells - nothing to display
			//
			statusOffset.setText(""); //$NON-NLS-1$
			statusValue.setText(""); //$NON-NLS-1$
		} // if
		else {
			//
			// Cursor is above the data cell - update offset panel
			//
			HexTablePointer p = new HexTablePointer(rowIndex, columnIndex-1);
			int offset = p.getOffset();
			int tableSize = table.getBufferSize();
			int ratio = (tableSize > 1) ? (100*offset/(tableSize-1)) : 100;
			statusOffset.setText(Messages.HexEditorControl_54 + Utils.zeroPadding(Integer.toHexString(offset).toUpperCase(), 8) + Messages.HexEditorControl_55 + Utils.zeroPadding(Integer.toHexString(tableSize-1).toUpperCase(), 8) + "h (" + ratio + "%) "); //$NON-NLS-3$ //$NON-NLS-4$
			
			//
			// Update value panel
			//
			int number = Integer.parseInt(cellData, 16);

			String binString = Integer.toBinaryString(number);
			statusValue.setText(Messages.HexEditorControl_58 + cellData + Messages.HexEditorControl_59 + number + Messages.HexEditorControl_60 + Integer.toOctalString(number) + Messages.HexEditorControl_61 + ("00000000".substring(binString.length()) + binString) + Messages.HexEditorControl_63); //$NON-NLS-5$
		} // else
		
		//
		// Update table/file size
		//
		statusFileSize.setText(Messages.HexEditorControl_64 + fileSizeFormat.format(table.getBufferSize()) + Messages.HexEditorControl_65);
	}

	private HexTablePointer findAndSelect(HexTablePointer position, byte[] find, byte[] lowerCase,
			final boolean searchForward, boolean caseSensitive, final boolean scrollToVisible) {

		// remove selection of previous result
		selectFindResult(true);
		findIndex = -1;

		if (find == null || find.length == 0)
			return null;

		int off = searchForward ? 0 : find.length - 1;
		int dataIndex = position.getOffset();
		int increment = searchForward ? 1 : -1;
		while (searchForward && dataIndex < table.getBufferSize() || !searchForward && dataIndex >= 0) {
			byte s = table.getData(dataIndex);
			if (caseSensitive && s == find[off] || !caseSensitive && Character.toLowerCase((char) s) == lowerCase[off]) {
				if (findIndex == -1)
					findIndex = dataIndex;
				off += searchForward ? 1 : -1;
				if (searchForward && off >= find.length || !searchForward && off < 0) {
					// adjust the index
					if (!searchForward)
						findIndex -= find.length - 1;
					final HexTablePointer p = new HexTablePointer(findIndex);
					findPrevStatus = new byte[find.length];
					for (int i = 0; i < findPrevStatus.length; i++) {
						findPrevStatus[i] = (byte) table.getCellStatus(p.getRowIndex(), p.getColumnIndex());
						p.move(1);
					}
					p.move(-find.length);
					table.getDisplay().syncExec(new Runnable() {
						public void run() {
							if (scrollToVisible) {
								table.getTable().select(p.getRowIndex() + table.getTable().getClientArea().height / table.getTable().getItemHeight() / 2);
								table.getTable().showSelection();
								table.getTable().select(p.getRowIndex());
							}
							selectFindResult(false);
							if (scrollToVisible)
								cursor.setSelection(p.getRowIndex(), p.getColumnIndex() + 1);
							table.setCellSelection(p.getRowIndex(), p.getColumnIndex(), false);
							table.updateVisibleTable(true, true, true);
						}
					});
					return p;
				}
			} else {
				boolean oneFound = findIndex != -1;
				off = searchForward ? 0 : find.length - 1;
				findIndex = -1;
				if (oneFound)
					continue;
			}
			dataIndex += increment;
		}
		return null;
	}

	private void selectFindResult(boolean reset) {
		if (findIndex == -1)
			return;
		HexTablePointer p = new HexTablePointer(findIndex);
		cursorKeyListener.closeCellEditor();
		// if the cell status has changed leave it as it is
		if (reset && table.getCellStatus(
				p.getRowIndex(), p.getColumnIndex()) != EHEP.CELL_STATUS_FINDRESULT)
			return;
		for (int i = 0; i < findPrevStatus.length; i++) {
			int status = reset ? findPrevStatus[i] : EHEP.CELL_STATUS_FINDRESULT;
			table.setCellStatus(p.getRowIndex(), p.getColumnIndex(), 1, status);
			p.move(1);
		}
	}
}
