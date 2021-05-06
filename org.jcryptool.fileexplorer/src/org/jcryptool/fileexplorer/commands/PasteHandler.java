// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.fileexplorer.FileExplorerPlugin;
import org.jcryptool.fileexplorer.views.FileExplorerView;

/**
 * Pastes the clipboard content (file(s) or directory/ directories) into the selected folder. To distinguish between a
 * paste operation after copy or a cut, the cut operation inserts an additional TextTransfer instance into the clipboard
 * with the content <b>CutOperation</b>. This is the indicator for the paste operation to delete the source file after
 * creating the target file. In case of a normal copy operation this TextTransfer instance does not exist, the value is
 * <code>null</code>.
 * 
 * @author Dominik Schadow
 * @version 0.9.7
 */
public class PasteHandler extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getCurrentSelection(event);
        if (selection instanceof IStructuredSelection) {
            IFileStore file = (IFileStore) ((IStructuredSelection) selection).getFirstElement();
            IFileStore directory = file;

            if (!file.fetchInfo().isDirectory()) {
                directory = file.getParent();
            }

            Clipboard clipboard = ((FileExplorerView) HandlerUtil.getActivePart(event)).getClipboard();
            String[] data = (String[]) clipboard.getContents(FileTransfer.getInstance());
            String dataInfo = (String) clipboard.getContents(TextTransfer.getInstance());

            if (data != null && data.length > 0) {

                for (int i = 0, length = data.length; i < length; i++) {
                    FileChannel in = null, out = null;
                    FileInputStream inStream = null;
                    FileOutputStream outStream = null;
                    File sourceFile = new File(data[i]);
                    File targetFile = new File(directory.toString() + File.separatorChar + sourceFile.getName());

                    if (targetFile.exists()) {
                        String newFilename = enterFilenameDialog(sourceFile.getName(), directory.toString());

                        if (newFilename == null) {
                            break;
                        }

                        targetFile = new File(directory.toString() + File.separatorChar + newFilename);
                    }

                    try {
                        inStream = new FileInputStream(sourceFile);
                        outStream = new FileOutputStream(targetFile);

                        in = inStream.getChannel();
                        out = outStream.getChannel();

                        MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());

                        out.write(buf);

                        if (dataInfo != null && "CutOperation".equals(dataInfo)) { //$NON-NLS-1$
                            sourceFile.delete();
                        }
                    } catch (Exception ex) {
                        LogUtil.logError(FileExplorerPlugin.PLUGIN_ID,
                                "Error while pasting clipboard content", ex, false); //$NON-NLS-1$
                    } finally {
                        ((FileExplorerView) HandlerUtil.getActivePart(event)).refresh();

                        try {
                            if (inStream != null) {
                                inStream.close();
                            }
                        } catch (IOException ex) {
                            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
                        }
                        try {
                            if (outStream != null) {
                                outStream.close();
                            }
                        } catch (IOException ex) {
                            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
                        }
                        try {
                            if (in != null) {
                                in.close();
                            }
                        } catch (IOException ex) {
                            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
                        }
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException ex) {
                            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
                        }
                    }
                }
            }

            clipboard.clearContents();
        }

        return null;
    }

    private String enterFilenameDialog(String filename, String directory) {
        InputDialog dialog = new InputDialog(null, Messages.PasteHandler_0, Messages.PasteHandler_1, filename, null);
        int status = dialog.open();

        if (status != Window.OK) {
            return null;
        }

        String newName = dialog.getValue().trim();

        File targetFile = new File(directory + File.separatorChar + newName);

        if (targetFile.exists()) {
            enterFilenameDialog(newName, directory);
        }

        return newName;
    }
}
