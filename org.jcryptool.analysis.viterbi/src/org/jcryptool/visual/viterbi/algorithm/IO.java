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
package org.jcryptool.visual.viterbi.algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.viterbi.ViterbiPlugin;

/**
 * This class is for reading, creating and writing into textfiles.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */

public class IO {

	/**
	 * The string source specifies the file name which will be read. Every
	 * newline will be replaced with the newLineReplacement. Use "\r\n" as
	 * newLineReplacement, for not replacing the newlines.
	 *
	 * @param String
	 *            source, specifies the name and path of the file, that should
	 *            be read
	 *
	 * @param String
	 *            newLineReplacement, specifies a String that will raplace every
	 *            newline
	 *
	 * @return the text read
	 */

	public String read(String source, String newLineReplacement) {

		StringBuilder text = new StringBuilder("");
		BufferedReader input = null;

		try {
			input = new BufferedReader(new FileReader(source));
			String line = input.readLine();

			while (line != null) {
				text.append(line);
				text.append(newLineReplacement); // replacing \r\n by the
													// newlineReplacement
				// text.append("\r\n"); // adding a "filesystem"-newline

				line = input.readLine();
			}
			text.delete(text.length() - newLineReplacement.length(),
					text.length());
			input.close();
		} catch (FileNotFoundException ex) {
		    LogUtil.logError(ViterbiPlugin.PLUGIN_ID, ex);
		} catch (IOException ex) {
            LogUtil.logError(ViterbiPlugin.PLUGIN_ID, ex);
		} finally {
			if (input != null) { // preventing null pointer exception
				try {
					input.close();
				} catch (IOException e) {
		            LogUtil.logError(ViterbiPlugin.PLUGIN_ID, e);
				}
			}
		}
		return text.toString();
	}

	/**
	 * This method writes a string into a textfile.
	 *
	 * @param String
	 *            text, the text that should be written
	 *
	 * @param String
	 *            target, name and path of the future file
	 *
	 */

	public void write(String text, String target) {

		PrintWriter output = null;

		try {
			output = new PrintWriter(target);
			output.write(text);
			output.close();
		} catch (FileNotFoundException ex) {
            LogUtil.logError(ViterbiPlugin.PLUGIN_ID, ex);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
}
