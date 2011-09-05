// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jcryptool.analysis.kegver.KegverPlugin;
import org.jcryptool.core.logging.utils.LogUtil;

public class IO {

	public static IO useFactory() {
		return IO.useFactory(null);
	}

	public static IO useFactory(Object inObject) {
		IO newIO = null;
		if (inObject == null) {
			try {
				throw new Throwable();
			} catch (Throwable e) {
				newIO = IO.useFactory(e.getStackTrace()[2].getClassName() + "."
						+ e.getStackTrace()[2].getMethodName());
			}
		} else if (inObject instanceof String) {
			String strFile = (String) inObject;
			newIO = IO.useFactory(new File(strFile));
		} else if (inObject instanceof File) {
			File aFile = (File) inObject;
			newIO = new IO();
			try {
				aFile = newIO.setFile(aFile);
				newIO.setBufferedReader(new BufferedReader(
						new FileReader(aFile)));
				newIO.setBufferedWriter(new BufferedWriter(
						new FileWriter(aFile)));
			} catch (FileNotFoundException ex) {
				LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
			} catch (IOException ex) {
				LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
			}
		} else {
			newIO = IO.useFactory(inObject.toString());
		}
		return newIO;
	}

	private IO() {
		;
	};

	private BufferedReader aBufferedReader = null;
	private BufferedWriter aBufferedWriter = null;

	private File aFile = null;

	public void buffer_needsClose(String inStr) {
		// Execute
		try {
			this.getBufferedWriter().write(inStr);
		} catch (NullPointerException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
	}

	public void close() {
		// Execute
		try {
			this.getBufferedWriter().flush();
			this.getBufferedWriter().close();
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
	}

	public boolean delete() {
		// Return
		this.aBufferedReader = null;
		this.aBufferedWriter = null;
		return this.aFile.delete();
	}

	public boolean exists() {
		// Return
		return this.getFile().exists();
	}

	@Override
	public void finalize() {
		try {
			this.getBufferedWriter().flush();
			this.getBufferedWriter().close();
			this.getBufferedReader().close();
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
	}

	private BufferedReader getBufferedReader() {
		return this.aBufferedReader;
	}

	private BufferedWriter getBufferedWriter() {
		return this.aBufferedWriter;
	}

	private File getFile() {
		return this.aFile;
	}

	private BufferedReader setBufferedReader(BufferedReader inBufferedReader) {
		this.aBufferedReader = inBufferedReader;
		return this.getBufferedReader();
	}

	private BufferedWriter setBufferedWriter(BufferedWriter inBufferedWriter) {
		this.aBufferedWriter = inBufferedWriter;
		return this.getBufferedWriter();
	}

	private File setFile(File inFile) {
		// Check
//		if (inFile.exists()) {	//TODO: overwrite good?
//		} else {
			try {
				inFile.createNewFile();
			} catch (IOException ex) {
				LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
			}
//		}
		// Execute
		this.aFile = inFile;
		// Return
		return this.getFile();
	}

	@Override
	public String toString() {
		return ("File: " + this.getFile().getAbsolutePath());
	}

	public void write(String inStr) {
		try {
			this.getBufferedWriter().write(inStr);
			this.getBufferedWriter().flush();
		} catch (NullPointerException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
	}

	public void writeln(String inStr) {
		this.write(inStr);
		this.newLine();
	}

	public String buffer(String inStr) {
		try {
			this.getBufferedWriter().write(inStr);
		} catch (NullPointerException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
		return inStr;
	}

	public String bufferln(String inStr) {
		try {
			this.getBufferedWriter().write(inStr);
			this.newLine();
		} catch (NullPointerException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
		return inStr;
	}

	public void newLine() {
		try {
			this.getBufferedWriter().newLine();
		} catch (IOException ex) {
			LogUtil.logError(KegverPlugin.PLUGIN_ID, ex);
		}
	}
}