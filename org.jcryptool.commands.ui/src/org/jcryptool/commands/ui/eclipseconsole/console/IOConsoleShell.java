// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.ui.eclipseconsole.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.jcryptool.commands.ui.CommandsUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;

/**
 * Class for monitoring at a given IOConsole, and notifying the observers, when new input from the input stream is
 * available
 *
 * @author Simon L
 */
public class IOConsoleShell extends Observable {

    protected final IOConsole console;
    protected final IOConsoleInputStream stream;

    private Queue<String> lines;

    private IDocumentListener linebreakListener = new IDocumentListener() {
        public void documentChanged(DocumentEvent event) {
            if (event.getText().startsWith("\r") || //$NON-NLS-1$
                    event.getText().startsWith("\n") || //$NON-NLS-1$
                    event.getText().startsWith(String.valueOf(Character.LINE_SEPARATOR))) {

                linebreakDetected();
            }
        }

        public void documentAboutToBeChanged(DocumentEvent event) {
        }
    };

    public IOConsoleShell(IOConsole console) {
        this(console, false);
    }

    public IOConsoleShell(IOConsole console, boolean autoStartMonitoring) {
        this.console = console;
        this.stream = console.getInputStream();
        this.lines = new LinkedList<String>();
        if (autoStartMonitoring)
            startMonitoring();

        IOConsoleOutputStream o = console.newOutputStream();
        initializeConsole(o);
        try {
            o.close();
        } catch (IOException e) {
            LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e);
        }
    }

    /**
     * Initialize the console before the first prompt with text
     *
     * @param o an outputStream of the console, does not need to be closed here
     */
    protected void initializeConsole(IOConsoleOutputStream o) {
    }

    public void startMonitoring() {
        console.getDocument().addDocumentListener(linebreakListener);
    }

    protected void linebreakDetected() {
        try {
            if (stream.available() > 0) { // \r\n neglected for empty lines
                returnDetected();
            }
        } catch (IOException e) {
            if (!e.getMessage().equals("Input Stream Closed"))LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e); //$NON-NLS-1$
        }
    }

    protected void returnDetected() {
        readUserInput();
        removeEmptyLines();
        if (lines.size() > 0) {
            setChanged();
            notifyObservers();
        }
    }

    private void removeEmptyLines() {
        @SuppressWarnings("unused")
        String line = ""; //$NON-NLS-1$
        while (lines.peek() != null && (line = lines.peek()).equals("")) { //$NON-NLS-1$
            lines.poll();
        }
    }

    private void readUserInput() {
        IOConsoleInputStream in = stream;
        IOConsoleInputStreamWrapper wrapperStream = new IOConsoleInputStreamWrapper(in);
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(wrapperStream, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException ex) {
            LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, ex);
        }
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = bufReader.read()) != -1) {
                output = output.concat(String.valueOf(Character.valueOf((char) charOut)));
            }
        } catch (IOException e) {
            if (!e.getMessage().equals("Input Stream Closed"))LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e); //$NON-NLS-1$
        }

        String line = processRawInputString(output);
        addLine(line);
    }

    /**
     * Returns the last read line from the console, or null, if there is no more line available
     */
    public String getLine() {
        return lines.poll();
    }

    /**
     * Returns the not yet read user input lines in queue (FIFO) format
     */
    public Queue<String> getLines() {
        return lines;
    }

    private void addLine(String line) {
        lines.add(line);
    }

    protected String processRawInputString(String output) {
        return output.trim();
    }

    public void stopMonitoring() {
        console.getDocument().removeDocumentListener(linebreakListener);
    }

}
