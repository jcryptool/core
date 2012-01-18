package org.jcryptool.core.operations.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.ui.IEditorReference;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.util.constants.IConstants;

public class EditorUtils {
	// TODO: comment thoroughly

	/**
	 * reads the current value from an input stream
	 * 
	 * @param in
	 *            the input stream
	 */
	public static String inputStreamToString(InputStream in) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
		} catch (UnsupportedEncodingException e1) {
			LogUtil.logError(OperationsPlugin.PLUGIN_ID, e1);
		}

		StringBuffer myStrBuf = new StringBuffer();
		int charOut = 0;
		String output = ""; //$NON-NLS-1$
		try {
			while ((charOut = reader.read()) != -1) {
				myStrBuf.append(String.valueOf((char) charOut));
			}
		} catch (IOException e) {
			LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
		}
		output = myStrBuf.toString();
		return output;
	}

	public static String retrieveTextForEditor(IEditorReference bestEditor) {
		InputStream is = EditorsManager.getInstance().getContentInputStream(bestEditor.getEditor(false));
		return inputStreamToString(is);
	}
}
