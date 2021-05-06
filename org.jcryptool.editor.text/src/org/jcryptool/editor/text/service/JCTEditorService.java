//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.editor.text.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.editor.text.JCTTextEditorPlugin;
import org.jcryptool.editor.text.editor.JCTTextEditor;

/**
 * This class must be implemented to contribute the extension point <i>editorServices</i>.
 * It implements the abstract methods of the class <i>org.jcryptool.core.operations.service.AbstractEditorService</i>
 *
 * @author Dominik Schadow
 * @version 0.9.2
 */
public class JCTEditorService extends AbstractEditorService {
    public JCTEditorService() {
        super(JCTTextEditor.ID);
    }

    /**
     * Getter of the active editorPart's content
     *
     * @param editorPart the active editorPart the content will be retrieved from
     * @return the content of editorPart as a String object
     */
    public String getContentOfEditorAsString(IEditorPart editorPart) {
        JCTTextEditor editor = (JCTTextEditor) editorPart;

        return editor.getDocument().get();
    }

    public byte[] getContentOfEditorAsBytes(IEditorPart editorPart) {
        return getContentOfEditorAsString(editorPart).getBytes();
    }

    /**
     * Sets a new content for active editor
     *
     * @param editorPart the active editor
     * @param content the new content
     */
    public void setContentOfEditor(IEditorPart editorPart, String content) {
        JCTTextEditor editor = (JCTTextEditor) editorPart;
        editor.getDocument().set(content);
    }

    public InputStream getContentOfEditorAsInputStream(IEditorPart editorPart) {
        JCTTextEditor editor = (JCTTextEditor) editorPart;
        try {
            return new BufferedInputStream(new ByteArrayInputStream(editor.getDocument().get().getBytes(IConstants.UTF8_ENCODING)));
        } catch (UnsupportedEncodingException e) {
            LogUtil.logError(JCTTextEditorPlugin.PLUGIN_ID, e);
        }

        return null;
    }
    
    @Override
    public void setContentOfEditor(IEditorPart editorPart, InputStream is) {
    	String newContent = "";
    	try {
            String UTF8 = "utf8";
            int BUFFER_SIZE = 8192;

            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    UTF8), BUFFER_SIZE);
            String str;
            while ((str = br.readLine()) != null) {
                newContent += str;
            }
        } catch (Exception e) {
        	LogUtil.logError(e);
        }
    	setContentOfEditor(editorPart, newContent);
    }
}
