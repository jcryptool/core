// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.editor.hex.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.hex.HexEditorConstants;

import net.sourceforge.javahexeditor.plugin.editors.HexEditor;

/***
 * 
 * Provides the editorservice to jcryptool
 * 
 * @author Anatoli Barski
 * @author Thorben Groos (Migration from EHEP to JavaHexEditor)
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
    @Override
	public InputStream getContentOfEditorAsInputStream(IEditorPart editorPart) {
    	
    	HexEditor editor = getHexEditor(editorPart);
    	long editorContentLength = editor.getManager().getContent().length();
    	int bufferSize = 0;
    	
    	if (editorContentLength > Integer.MAX_VALUE) {
    		LogUtil.logWarning("Editor content does not fit into a java array. It will be cut.");
    		editorContentLength = Integer.MAX_VALUE;
    		bufferSize = Integer.MAX_VALUE;
    	} else {
    		bufferSize = (int) editorContentLength;
    	}
    	
    	ByteBuffer buf = ByteBuffer.allocate(bufferSize);
    	
    	try {
			int readBytes = editor.getManager().getContent().get(buf, bufferSize);
			System.out.println(readBytes + "\tof " + bufferSize);
			if (readBytes == bufferSize) {
				System.out.println("Alle Bytes gelesen.");
			} else {
				System.out.println("Somethign went wrong when reading data. Not all data has been read.");
			}
		} catch (IOException e) {
			LogUtil.logError(HexEditorConstants.EditorID, e);
		}
    	
    	byte[] array = buf.array();
    	
    	InputStream is = new ByteArrayInputStream(array);
    	
    	return is;
    }

    /**
     * get the content as byte array
     * max: ~2GB
     */
    @Override
	public byte[] getContentOfEditorAsBytes(IEditorPart editorPart) {

    	HexEditor editor = getHexEditor(editorPart);
    	long editorContentLength = editor.getManager().getContent().length();
    	int bufferSize = 0;
    	
    	if (editorContentLength > Integer.MAX_VALUE) {
    		LogUtil.logWarning("Editor content does not fit into a java array. It will be cut.");
    		editorContentLength = Integer.MAX_VALUE;
    		bufferSize = Integer.MAX_VALUE;
    	} else {
    		bufferSize = (int) editorContentLength;
    	}
    	
    	ByteBuffer buf = ByteBuffer.allocate(bufferSize);
    	
    	try {
			int readBytes = editor.getManager().getContent().get(buf, bufferSize);
			System.out.println("readBytes " + readBytes + " bufferSize " + bufferSize);
			if (readBytes == bufferSize) {
				System.out.println("Alle Bytes gelesen.");
			} else {
				System.out.println("Somethign went wrong when reading data. Not all data has been read.");
			}
		} catch (IOException e) {
			LogUtil.logError(HexEditorConstants.EditorID, e);
		}
    	
    	return buf.array();
    }

	private HexEditor getHexEditor(IEditorPart editorPart) {
		HexEditor hexEditor = editorPart.getAdapter(HexEditor.class);
		return hexEditor;
	}

    @Override
	public String getContentOfEditorAsString(IEditorPart editorPart) {
		String content = new String(getContentOfEditorAsBytes(editorPart));
		return content;
    }

    @Override
	public void setContentOfEditor(IEditorPart editorPart, String content) {
    	HexEditor editor = getHexEditor(editorPart);
    	if(editor == null)
    	{
    		LogUtil.logError(new IllegalArgumentException("cannot set content of undefined editor"));
    		return;
    	}

    	byte[] data = content.getBytes(StandardCharsets.UTF_8);
    	
    	ByteBuffer buffer = ByteBuffer.wrap(data);
    	
    	editor.getManager().getContent().insert(buffer, 0);
    }
    
    @Override
    public void setContentOfEditor(IEditorPart editorPart, InputStream is) {
    	HexEditor editor = getHexEditor(editorPart);
    	if(editor == null) {
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
    	
    	ByteBuffer buffer = ByteBuffer.wrap(data);
    	
    	editor.getManager().getContent().insert(buffer, 0);
    }
}
