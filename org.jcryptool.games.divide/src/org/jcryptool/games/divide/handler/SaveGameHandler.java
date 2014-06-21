// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2013 JCrypTool team and contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.games.divide.handler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jcryptool.games.divide.dialogs.SaveRecordConfirmation;
import org.jcryptool.games.divide.views.DivideView;
import org.jcryptool.games.divide.views.Messages;

public class SaveGameHandler extends AbstractHandler {

    private int[] colSize;

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart workbench = HandlerUtil.getActivePart(event);

        if (workbench != null && workbench instanceof DivideView) {
            DivideView view = (DivideView) HandlerUtil.getActivePart(event);
            Shell parent = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
            FileDialog saveDialog = new FileDialog(parent, SWT.SAVE);
            saveDialog.setFilterExtensions(new String[] { "*.txt" });
            String file = saveDialog.open();
            if (file != null) {
                String newline = System.lineSeparator();
                String gameType = view.getGameType() ? Messages.DivideView_2 : Messages.DivideView_3;
                String startingValue = view.getTextStartValue().getText();
                // create output
                StringBuilder output = new StringBuilder();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                output.append("Date: " + timeStamp + newline);
                output.append(Messages.DivideView_1 + " " + gameType + newline);
                output.append(Messages.DivideView_4 + " " + startingValue + newline);
                output.append(newline);

                Table scoreTable = view.getScoreTable();
                int numCols = scoreTable.getColumnCount();
                int numItems = scoreTable.getItemCount();
                // save the max length for each column
                colSize = new int[numCols];
                // init with length of header fields
                for (int i = 0; i < numCols; i++) {
                    colSize[i] = scoreTable.getColumns()[i].getText().length();
                }

                // determine size of columns
                for (int i = 0; i < numItems; i++) {
                    TableItem row = scoreTable.getItem(i);
                    for (int j = 0; j < numCols; j++) {
                        int stringLength = row.getText(j).length();
                        if (stringLength > colSize[j]) {
                            colSize[j] = stringLength;
                        }
                    }
                }

                /*
                 * write score table
                 */

                // write header
                TableColumn[] header = scoreTable.getColumns();
                for (int i = 0; i < header.length; i++) {
                    String text = header[i].getText();
                    appendTableRow(output, text, i);
                }
                output.append(newline);

                // write body
                for (int i = 0; i < numItems; i++) {
                    TableItem row = scoreTable.getItem(i);
                    for (int j = 0; j < numCols; j++) {
                        String text = row.getText(j);
                        appendTableRow(output, text, j);
                    }
                    output.append(newline);
                }
                output.append(newline);

                // write to file
                Writer writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
                    writer.write(output.toString());
                    SaveRecordConfirmation successDialog = new SaveRecordConfirmation(parent, true, null);
                    successDialog.open();
                } catch (IOException ex) {
                    SaveRecordConfirmation errorDialog = new SaveRecordConfirmation(parent, false, ex.getMessage());
                    errorDialog.open();
                } finally {
                    try {
                        writer.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }

        return null;
    }

    private void appendTableRow(StringBuilder output, String text, int i) {
        if (text.length() >= colSize[i]) {
            output.append(text);
        } else {
            output.append(text);
            for (int j = 0; j < (colSize[i] - text.length()); j++) {
                output.append(" ");
            }
        }
        output.append("|");
    }
}
