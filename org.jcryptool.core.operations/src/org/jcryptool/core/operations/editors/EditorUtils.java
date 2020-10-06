// -----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.core.operations.editors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.eclipse.ui.IEditorReference;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.util.constants.IConstants;

public class EditorUtils {
    // TODO: comment thoroughly

    /**
     * reads the current value from an input stream
     * 
     * @param in the input stream
     */
    public static String inputStreamToString(InputStream in) {
        try {
			return new String(in.readAllBytes(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			LogUtil.logError(e);
		}
		return "";
    }

    public static String retrieveTextForEditor(IEditorReference bestEditor) {
        InputStream is = EditorsManager.getInstance().getContentInputStream(bestEditor.getEditor(false));
        return inputStreamToString(is);
    }
}
