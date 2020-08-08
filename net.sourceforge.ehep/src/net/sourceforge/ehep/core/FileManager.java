/*==========================================================================
 * 
 * FileManager.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.7 $
 * $Date: 2012/11/06 16:45:21 $
 * $Name:  $
 * 
 * Created on 15-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;

import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.editors.HexEditor;
import net.sourceforge.ehep.gui.HexTable;

/**
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class FileManager {
	public final static byte[] buffer = new byte[EHEP.BUFFER_SIZE]; // use buffer in table
	private Composite parent;
	private File file;
	private InputStream fileStream;
	private long fileSize;
	private int dataAvailable;
	
	/**
	 * Creates a file manager
	 * @param parent
	 * @param file
	 */
	public FileManager(Composite parent, File file) {
		this.parent = parent;
		this.file = file;
		
		try {
			fileStream = new FileInputStream(file);
			fileSize = file.length();
		}
		
		catch (Exception e) {
			fileStream = null;
		}
	}

	/**
	 * Loads the data from file into the table
	 * @param table reference to the hex table
	 * @return true if the file has successfully been loaded, false else
	 */
	public boolean loadFile(final HexTable table) {
		
		final boolean[] success = { true };
		//
		// Fill data into the table
		//
		try {
			table.setBufferSize((int) fileSize);

			if (file == null || fileStream == null || fileSize <= 0) {
				//
				// Add a dummy item
				//
//				table.addDummyItem();
			} // if
			else {
				final ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(parent.getShell());
				monitorDialog.setOpenOnRun(false);

				//
				// Define a long running operation
				//
				IRunnableWithProgress op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						long openTime = System.currentTimeMillis() + 2000L;
						boolean opened = false;

						monitor.beginTask(Messages.FileManager_0 + file.getName() + "...", (int) (fileSize/EHEP.BUFFER_SIZE)); //$NON-NLS-2$

						//
						// Load the file
						//
						long progressCounter = 0, progressStatus = 0;
						int bufferIndex = 0;
						
						try {
							while ((dataAvailable = fileStream.read(buffer, 0, EHEP.BUFFER_SIZE)) > 0) {
								if (!opened && System.currentTimeMillis() > openTime) {
									monitorDialog.getShell().getDisplay().asyncExec(new Runnable() {
										public void run() {
											monitorDialog.open();
										}
									});

									opened = true;
								}

								//
								// Update progress info below the progress bar
								//
								progressCounter += dataAvailable;
								progressStatus = 100 * progressCounter / fileSize;

								if (progressCounter < 1024) {
									monitor.subTask("" + progressCounter + Messages.FileManager_3 + progressStatus + "%)"); //$NON-NLS-1$ //$NON-NLS-3$
								} else if (progressCounter < 1024*1024) {
									monitor.subTask("" + progressCounter/1024 + Messages.FileManager_6 + progressStatus + "%)"); //$NON-NLS-1$ //$NON-NLS-3$
								} else {
									monitor.subTask("" + progressCounter/(1024*1024) + Messages.FileManager_9 + progressStatus + "%)"); //$NON-NLS-1$ //$NON-NLS-3$
								}
								
								//
								// Send data stored in the buffer to the table
								//
								final int index = bufferIndex;

								monitorDialog.getShell().getDisplay().syncExec(new Runnable() {
									public void run() {
										table.addData(index, buffer, dataAvailable);
									}
								});
								
								bufferIndex += dataAvailable;
								monitor.worked(1);

								if (!monitor.isCanceled())
									continue;
								
								success[0] = false;
								break;
							} // while
						}

						catch (final Exception e) {
							//
							// Error reading file - do nothing
							//
							parent.getDisplay().syncExec(new Runnable() {
								public void run() {
									MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_EXCEPTION, Messages.FileManager_11 + file.getName() + "!\n\n" + e.toString()); //$NON-NLS-2$
								}
							});
						} // catch

						finally {
							monitor.done();
						}
					} // run()
				};
				
				//
				// Start long-running operation
				//
				monitorDialog.run(true, true, op);
			} // else

			//
			// Close the input stream
			//
			fileStream.close();
		} // try

		catch (InvocationTargetException e) {
			//
			// Handle exception
			//
			Throwable realCause = e.getCause();
			Throwable realException = e.getTargetException();
			MessageDialog.openError(parent.getShell(), EHEP.DIALOG_TITLE_EXCEPTION, Messages.FileManager_13 + realException.getMessage() + Messages.FileManager_14 + realCause.getMessage());
		}
		
		catch (OutOfMemoryError e) {
			
			throw new RuntimeException(Messages.FileManager_15 + e);
			
		}

		catch (Exception e) {
			//
			// Error reading file - do nothing
			//
			MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_EXCEPTION, Messages.FileManager_16 + file.getName() + "!\n\n" + e.toString()); //$NON-NLS-2$
		} // catch

		// at last set the virtual table item count
		// if an empty file is opened, a dummy item has been created
		if (table.getTable().getItemCount() == 0)
			table.getTable().setItemCount(table.getItemCount());

    	//
		// Compress the table column widths
		//
		table.packColumns();
		return success[0];
	}

	/**
	 * Saves the data from table
	 * @param hexEditor
	 * @param table
	 * @param iFile
	 * @param monitor
	 */
	public void saveFile(HexEditor hexEditor, HexTable table, File ioFile, IProgressMonitor monitor, boolean isSaveAs) {
		//
		// Get file location and name
		//
		String fileName = ioFile.getName();
		String filePathAndName = ioFile.getAbsolutePath();
		
		if (!isSaveAs) {
			//
			// Check file existency, accessibility and read-only status
			//
			if (!ioFile.exists()) {
				MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_WARNING, Messages.FileManager_18 + filePathAndName + Messages.FileManager_19);
				return;
			}
				 
			if (!ioFile.isFile()) {
				MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_WARNING, Messages.FileManager_20 + filePathAndName + "!"); //$NON-NLS-2$
				return;
			}
			
			if (!ioFile.canWrite()) {
				MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_WARNING, Messages.FileManager_22 + filePathAndName + Messages.FileManager_23);
				return;
			}
		} // if

		File file = new File(filePathAndName);

		if (isSaveAs) {
			//
			// Create empty file - only if SaveAs was selected
			//
			try {
				EhepPlugin.log(Messages.FileManager_24 + file.getAbsolutePath() + "..."); //$NON-NLS-2$
				file.createNewFile();
			}
			catch (Exception e) {
				EhepPlugin.log(Messages.FileManager_26 + file.getAbsolutePath() + "!"); //$NON-NLS-2$
				MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_EXCEPTION, Messages.FileManager_28 + file.getAbsolutePath() + "'!\n" + e.toString()); //$NON-NLS-2$
				return;
			}
		} // if
			
		FileOutputStream outputStream = null;
		int sizeEstimate = table.getItemCount() * EHEP.TABLE_NUM_DATA_COLUMNS;

		table.setRedraw(false);
		
		//
		// Show progress bar only for files larger than 1 kB
		//
		boolean showProgress = (monitor != null) && (sizeEstimate > 1024);
		
		try {
			//
			// Try to open output stream
			//
			outputStream = new FileOutputStream(file);
			
			if (showProgress) {
				//
				// Initialize the progress bar
				//
				monitor.beginTask(Messages.FileManager_30 + fileName + " ...", sizeEstimate); //$NON-NLS-2$
			} // if
			
			synchronized (buffer) {
				//
				// Get one-row data from the table into the buffer
				//

				int index = 0;
				while (true) {
					int n = table.getData(buffer, index, buffer.length);
					if (n == 0)
						break;
					//
					// Write data to the output stream
					//
					outputStream.write(buffer, 0, n);
					index += n;

					if (showProgress) {
						//
						// Update progress monitor
						//
						monitor.worked(n);
					} // if
					
				}
				table.setCellStatus(0, 0, table.getBufferSize(), EHEP.CELL_STATUS_NORMAL);
			} // synchronized

			//
			// Close output stream
			//
			outputStream.close();
			
			//
			// Clear "dirty" flag
			//
			hexEditor.setDirty(false);
			
			if (showProgress) {
				//
				// Close progress monitor
				//
				monitor.done();
			}
		} // try
		catch (Exception e) {
			//
			// In case of exception, display a message box
			//
			MessageDialog.openInformation(parent.getShell(), EHEP.DIALOG_TITLE_EXCEPTION, Messages.FileManager_32 + e.toString());
		} // catch

		table.setRedraw(true);
		table.redraw();
	}
}
