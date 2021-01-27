// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.editor.text.editor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

/**
 * <p>This class is based on the org.eclipse.ui.examples.rcp.texteditor RCP editor plug-in.</p>
 *
 * A document provider that reads can handle <code>IPathEditorInput</code> editor inputs. Documents are created by
 * reading them in from the file that the <code>IPath</code> contained in the editor input points to.
 */
public class SimpleDocumentProvider extends AbstractDocumentProvider {
    private JCTTextEditor jctTextEditor;
    public SimpleDocumentProvider(JCTTextEditor jctTextEditor) {
        this.jctTextEditor = jctTextEditor;
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createDocument(java.lang.Object)
     */
    @Override
	protected IDocument createDocument(Object element) throws CoreException {
        if (element instanceof IEditorInput) {
            IDocument document = new Document();
            setDocumentContent(document, (IEditorInput) element);

            return document;
        }

        return null;
    }

    /**
     * Tries to read the file pointed at by <code>input</code> if it is an <code>IPathEditorInput</code>. If the file
     * does not exist, <code>true</code> is returned.
     *
     * @param document the document to fill with the contents of <code>input</code>
     * @param input the editor input
     * @return <code>true</code> if setting the content was successful or no file exists, <code>false</code> otherwise
     * @throws CoreException if reading the file fails
     */
    private boolean setDocumentContent(IDocument document, IEditorInput input) throws CoreException {
        FileInputStream fis = null;

        try {
            if (input instanceof IPathEditorInput)
                fis = new FileInputStream(((IPathEditorInput) input).getPath().toFile());
            else
                return false;
        } catch (FileNotFoundException e) {
            // return empty document and save later
            return true;
        }

        try {
            setDocumentContent(document, fis);
            return true;
        } catch (IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, JCTTextEditorPlugin.PLUGIN_ID,
                    IStatus.OK, Messages.SimpleDocumentProvider_0, e));
        }
    }

    /**
     * Reads in document content from a reader and fills <code>document</code>
     *
     * @param document the document to fill
     * @param reader the source
     * @throws IOException if reading fails
     */
    private void setDocumentContent(IDocument document, FileInputStream reader) throws IOException {
    	
		InputStreamReader isr = new InputStreamReader(reader,  IConstants.UTF8_ENCODING); //$NON-NLS-1$
        Reader in = new BufferedReader(isr);
        StringBuffer buffer = new StringBuffer();

        int ch;
        while ((ch = in.read()) > -1) {
            buffer.append((char) ch);
        }
        in.close();

        document.set(buffer.toString());
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#createAnnotationModel(java.lang.Object)
     */
    @Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
        return null;
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#doSaveDocument(org.eclipse.core.runtime
     * .IProgressMonitor, java.lang.Object, org.eclipse.jface.text.IDocument, boolean)
     */
    @Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
            throws CoreException {
        if (element instanceof IPathEditorInput) {
            IPathEditorInput pei = (IPathEditorInput) element;
            File file = pei.getPath().toFile();

            try {
                file.createNewFile();

                if (file.exists()) {
                    if (file.canWrite()) {
                        writeDocumentContent(document, new FileWriter(file), monitor);
                    } else {
                        jctTextEditor.doSaveAs();
                    }
                } else {
                    throw new CoreException(new Status(IStatus.ERROR, JCTTextEditorPlugin.PLUGIN_ID,
                            IStatus.OK, Messages.SimpleDocumentProvider_2, null));
                }
            } catch (IOException e) {
                throw new CoreException(new Status(IStatus.ERROR, JCTTextEditorPlugin.PLUGIN_ID,
                        IStatus.OK, Messages.SimpleDocumentProvider_3, e));
            }
        }
    }

    /**
     * Saves the document contents to a stream.
     *
     * @param document the document to save
     * @param writer the stream to save it to
     * @param monitor a progress monitor to report progress
     * @throws IOException if writing fails
     */
    private void writeDocumentContent(IDocument document, Writer writer, IProgressMonitor monitor) throws IOException {
        Writer out = new BufferedWriter(writer);
        try {
            out.write(document.get());
        } finally {
            out.close();
        }
    }

    /*
     * @see org.eclipse.ui.texteditor.AbstractDocumentProvider#getOperationRunner(org.eclipse.core.runtime
     * .IProgressMonitor)
     */
    @Override
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
        return null;
    }

    /*
     * @see org.eclipse.ui.texteditor.IDocumentProviderExtension#isModifiable(java.lang.Object)
     */
    @Override
	public boolean isModifiable(Object element) {
        if (element instanceof IPathEditorInput) {
            IPathEditorInput pei = (IPathEditorInput) element;
            File file = pei.getPath().toFile();
            return file.canWrite() || !file.exists(); // Allow to edit new files
        }
        return false;
    }

    /*
     * @see org.eclipse.ui.texteditor.IDocumentProviderExtension#isReadOnly(java.lang.Object)
     */
    @Override
	public boolean isReadOnly(Object element) {
        return !isModifiable(element);
    }

    /*
     * @see org.eclipse.ui.texteditor.IDocumentProviderExtension#isStateValidated(java.lang.Object)
     */
    @Override
	public boolean isStateValidated(Object element) {
        return true;
    }
}