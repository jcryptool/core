// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.editor.hex.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.hex.HexEditorConstants;

import net.sourceforge.ehep.editors.HexEditor;
import net.sourceforge.ehep.gui.HexEditorControl;
import net.sourceforge.ehep.gui.HexTable;

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
        HexTable hexTable = getHexEditor(editorPart).getControl().getHexTable();
		if(hexTable == null)
			return null;
		
		// This actually cannot happen because HexTable is backed by an byte array, but may be this will change
		int size = 0;
		if (hexTable.getBufferSize() > Integer.MAX_VALUE) {
		    LogUtil.logWarning("Editor content does not fit into a java array. It will be cut.");
		    size = Integer.MAX_VALUE;
		} else
		    size = (int)hexTable.getBufferSize();
		ByteBuffer buffer = ByteBuffer.allocate(size);
		hexTable.getData(buffer.array(), 0, size);
		return buffer.array();
    }

	private HexEditor getHexEditor(IEditorPart editorPart) {
		HexEditor hexEditor = (HexEditor)editorPart.getAdapter(HexEditor.class);
		return hexEditor;
	}

    public String getContentOfEditorAsString(IEditorPart editorPart) {
    	HexEditorControl control = getHexEditor(editorPart).getControl();
		if(control == null)
			return null;
		try {
			String content = new String(getContentOfEditorAsBytes(editorPart), control.getCurrentEncoding());
			return content;
		} catch (UnsupportedEncodingException e) {
			LogUtil.logError(e);
		}
		return null;
    }

    public void setContentOfEditor(IEditorPart editorPart, String content) {
    	HexEditor editor = getHexEditor(editorPart);
    	if(editor == null)
    	{
    		LogUtil.logError(new IllegalArgumentException("cannot set content of undefined editor"));
    		return;
    	}
    	HexEditorControl control = editor.getControl();
    	HexTable hexTable = control.getHexTable();
    	byte[] data = content.getBytes(Charset.forName(control.getCurrentEncoding()));
    	hexTable.setBufferSize(data.length);
    	hexTable.setData(data, 0, data.length);
    }
    
    @Override
    public void setContentOfEditor(IEditorPart editorPart, InputStream is) {
    	HexEditor editor = getHexEditor(editorPart);
    	if(editor == null)
    	{
    		LogUtil.logError(new IllegalArgumentException("cannot set content of undefined editor"));
    		return;
    	}
    	
    	int bufsize = 8092;
    	byte[] buf = new byte[bufsize];
    	byte[] data = new byte[0];
    	try {
            int nrBytes;
            
            while ((nrBytes = is.read(buf)) != -1) {
            	data = new byte[data.length + nrBytes];
                System.arraycopy(buf, 0, data, 0, nrBytes);
            }
        } catch (Exception e) {
        	LogUtil.logError(e);
        }
    	
    	HexEditorControl control = editor.getControl();
    	HexTable hexTable = control.getHexTable();
    	hexTable.setBufferSize(data.length);
    	hexTable.setData(data, 0, data.length);
    }
}
