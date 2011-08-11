//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.ui.eclipseconsole;

import java.io.IOException;
import java.util.Observable;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.jcryptool.commands.ui.CommandsUIPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * Class for monitoring at a given IOConsole, and notifying
 * the observers, when new input from the input stream is available
 * 
 * @author Simon L
 */
public class AvailableInputIOConsoleObserver extends Observable {
	
//	private static final int CHECK_INTERVAL = 100;
//	private boolean started = false;
	private final IOConsole console;
	private final IOConsoleInputStream stream;
//	private Thread monitorThread;
	
//	private final Observable notifierObject;
	private IDocumentListener linebreakListener = new IDocumentListener() {
		public void documentChanged(DocumentEvent event) {
			if(	event.getText().startsWith("\r") ||  //$NON-NLS-1$
					event.getText().startsWith("\n") || //$NON-NLS-1$
					event.getText().startsWith(String.valueOf(Character.LINE_SEPARATOR))) {
					
					linebreakDetected();
			}
		}
		public void documentAboutToBeChanged(DocumentEvent event) {
			
		}
	};

	public AvailableInputIOConsoleObserver(IOConsole console) {
		this(console, false);
	}
	
	public AvailableInputIOConsoleObserver(IOConsole console, boolean autoStartMonitoring) {
//		this.notifierObject = this;
		this.console = console;
		this.stream = console.getInputStream();
		if(autoStartMonitoring) startMonitoring();
	}

//	private void createNewThread() {
//		if(monitorThread != null && monitorThread.isAlive() ) {
//			monitorThread.interrupt();
//		}
//		monitorThread = new Thread() {
//			@Override
//			public void run() {
//				while(!isInterrupted()) {
//					try {
//					   if (stream.available() > 0) {
//			        	   setChanged();
//			        	   notifyObservers();
//			           }
//			        } catch (IOException e) {
//			            if(! e.getMessage().equals("Input Stream Closed")) LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e); //$NON-NLS-1$
//			        }
//					
//					//Timeout
//					try {
//						synchronized(this) { this.wait(CHECK_INTERVAL); }
//					} catch (InterruptedException e) {
//						throw new RuntimeException("Interrupted..."); //$NON-NLS-1$
//					}
//				}
//			}
//		};
//		monitorThread.setDaemon(true);
//	}
	
	public void startMonitoring() {
		console.getDocument().addDocumentListener(linebreakListener );
		
//		createNewThread();
//		monitorThread.start();
	}
	
	protected void linebreakDetected() {
		try {
		   if (stream.available() > 0) {
        	   setChanged();
        	   notifyObservers();
           }
        } catch (IOException e) {
            if(! e.getMessage().equals("Input Stream Closed")) LogUtil.logError(CommandsUIPlugin.PLUGIN_ID, e); //$NON-NLS-1$
        }
	}

	public void stopMonitoring() {
//		if(monitorThread != null && monitorThread.isAlive()) monitorThread.interrupt();
		console.getDocument().removeDocumentListener(linebreakListener);
	}
	
}
