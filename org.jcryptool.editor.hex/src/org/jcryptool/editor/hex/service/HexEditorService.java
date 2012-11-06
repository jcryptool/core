// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.editor.hex.service;

import java.io.ByteArrayInputStream;
//import java.io.IOException;
import java.io.InputStream;
//import java.nio.ByteBuffer;

import org.eclipse.ui.IEditorPart;
//import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.hex.HexEditorConstants;

/***
 * 
 * Provides the editorservice to jcryptool
 * 
 * @author Anatoli Barski
 * 
 */
public class HexEditorService extends AbstractEditorService {

    /**
     * the default constructor
     */
    public HexEditorService() {
        super(HexEditorConstants.EditorID);
    }

    /**
     * the constructor from the abstract class
     * 
     * @param editorID
     */
    protected HexEditorService(String editorID) {
        super(editorID);
    }

    /**
     * gets the content as stream
     */
    public InputStream getContentOfEditorAsInputStream(IEditorPart editorPart) {
        byte[] contentAsByteArray = getContentOfEditorAsBytes(editorPart);
        if(contentAsByteArray == null) return null;
        
        InputStream is = new ByteArrayInputStream(contentAsByteArray);
        return is;
    }

    /**
     * get the content as byte array
     * max: ~2GB
     */
    public byte[] getContentOfEditorAsBytes(IEditorPart editorPart) {
//        try {

        	// TODO: refactor
        	return new byte[10];
//            BinaryContent content = HexEditorActionBarContributor.getManager().getContent();
//            if (content == null)
//                return null;
//            int size = 0;
//            if (content.length() > Integer.MAX_VALUE) {
//                LogUtil.logWarning("Editor content does not fit into a java array. It will be cut.");
//                size = Integer.MAX_VALUE;
//            } else
//                size = (int) content.length();
//            ByteBuffer buffer = ByteBuffer.allocate(size);
//            content.get(buffer, 0);
//            return buffer.array();
//        } catch (IOException e) {
//            LogUtil.logError(e);
//        }
//        return null;
    }

    /**
     * not used
     */
    public String getContentOfEditorAsString(IEditorPart editorPart) {
        return null;
    }

    /**
     * not used
     */
    public void setContentOfEditor(IEditorPart editorPart, String content) {
    }
}
