// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * This is the base class for an editor service. All contributors of the <i>editorServices</i> extension point must
 * provide a class which extends this one.
 * 
 * @author amro
 * @author t-kern
 * @author Dominik Schadow
 * @version 0.9.5
 */
public abstract class AbstractEditorService {
    private String editorID;
    private static int tempFileNumber = 1;
    private static int outputNumber = 1;

    /**
     * Constructor which needs an editor ID as a parameter.
     * 
     * @param editorID the id of an editor
     */
    protected AbstractEditorService(String editorID) {
        this.editorID = editorID;
    }

    public abstract InputStream getContentOfEditorAsInputStream(IEditorPart editorPart);

    /**
     * Subclasses must provide a mechanism to retrieve the content of the parameterized editor.
     * 
     * @deprecated Do not use with regard to modern cryptography. Use streams instead.
     * @param editorPart the editor which content is to be retrieved
     * @return the content of the editor
     */
    public abstract String getContentOfEditorAsString(IEditorPart editorPart);

    /**
     * @deprecated Do not use with regard to modern cryptography. Use streams instead.
     * @param editorPart
     * @return
     */
    public abstract byte[] getContentOfEditorAsBytes(IEditorPart editorPart);

    /**
     * Subclasses must provide a mechanism to set a new content for the parameterized editor.
     * 
     * @param editorPart the editor which content is to be setted
     * @param content the new content of the editor
     */
    public abstract void setContentOfEditor(IEditorPart editorPart, String content);

    /**
     * Subclasses must provide a mechanism to set a new content for the parameterized editor.
     * 
     * @param editorPart the editor which content is to be setted
     * @param content an inputStream to be read into the editor as new content
     */
    public abstract void setContentOfEditor(IEditorPart editorPart, InputStream content);

    /**
     * Getter for the editor ID.
     * 
     * @return the ID of the editor
     */
    public String getEditorID() {
        return editorID;
    }

    /**
     * Same as createOutputFile(String content) but with custom name.
     * 
     * @param name the name of the file
     * @param content the content of the file
     * @return An IEditorInput of an output file
     */
    public static final IEditorInput createOutputFile(String name, String content) {
        File outputFile = new File(new File(DirectoryService.getTempDir()), name
                + getFormatedFilenumber(outputNumber++) + IConstants.TXT_FILE_TYPE_EXTENSION);

        try {
            PrintWriter p = new PrintWriter(outputFile);
            p.print(content);
            p.flush();
            p.close();
        } catch (IOException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while writing to an output stream", e, false); //$NON-NLS-1$
        }
        outputFile.deleteOnExit();

        return new PathEditorInput(new Path(outputFile.getAbsolutePath()));
    }

    /**
     * Creates a new IEditorInput for an output file containing the given (String) content.
     * 
     * @param content The content that is being stored in the IEditorInput's underlying File
     * @return An IEditorInput of an output file containing the given (String) content
     */
    public static final IEditorInput createOutputFile(String content) {
        return createOutputFile(Messages.AbstractEditorService_1, content);
    }

    /**
     * Same as createOutputFile(InputStream is) but with custom name and file extension.
     * 
     * @param name the name of the file
     * @param extension the extension of the file
     * @param content the content of the file
     * @return An IEditorInput of an output file
     */
    public static final IEditorInput createOutputFile(String name, String extension, InputStream content) {
        File outputFile = new File(new File(DirectoryService.getTempDir()), name
                + getFormatedFilenumber(outputNumber++) + extension);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(outputFile, "rw"); //$NON-NLS-1$
            int i;
            while ((i = content.read()) != -1) {
                raf.write(i);
            }
            content.close();
            raf.close();
        } catch (FileNotFoundException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while initializing a RAF", e, false); //$NON-NLS-1$
        } catch (IOException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "IOException while reading/writing from a stream", e, false); //$NON-NLS-1$
        }
        outputFile.deleteOnExit();

        return new PathEditorInput(new Path(outputFile.getAbsolutePath()));
    }

    public static final IEditorInput createOutputFile(InputStream is) {
        return createOutputFile(Messages.AbstractEditorService_1, IConstants.BIN_FILE_TYPE_EXTENSION, is);
    }

    /**
     * Same as createOutputFile(byte[] name) but with custom name.
     * 
     * @param name the name of the file
     * @param content the content of the file
     * @return An IEditorInput of an output file
     */
    public static final IEditorInput createOutputFile(String name, String extension, byte[] content) {
        File outputFile = new File(new File(DirectoryService.getTempDir()), name
                + getFormatedFilenumber(outputNumber++) + extension);

        try {
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(content);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, ex);
        }
        outputFile.deleteOnExit();

        return new PathEditorInput(new Path(outputFile.getAbsolutePath()));
    }

    /**
     * Creates a new IEditorInput for an output file containing the given (byte[]) content.
     * 
     * @param content The content that is being stored in the IEditorInput's underlying File
     * @return An IEditorInput of an output file containing the given (byte[]) content
     */
    public static final IEditorInput createOutputFile(byte[] content) {
        return createOutputFile(Messages.AbstractEditorService_1, IConstants.BIN_FILE_TYPE_EXTENSION, content);
    }

    /**
     * Creates a new IEditorInput for an output file containing the given (byte[]) content.
     * 
     * @param content The content that is being stored in the IEditorInput's underlying File
     * @return An IEditorInput of an output file containing the given (byte[]) content
     */
    public static final IEditorInput createOutputFile(byte[] content, String extension) {
        return createOutputFile(Messages.AbstractEditorService_1, extension, content);
    }

    /**
     * Returns an IEditorInput containing a temporary file in the temp-file directory.
     * 
     * @return An IEditorInput containing a temporary file in the temp-file directory
     */
    public static final IEditorInput createTemporaryFile() {
        return createTemporaryEditorInput(createTempFile());
    }

    /**
     * Returns an IEditorInput containing a temporary file in the temp-file directory.
     * 
     * @return An IEditorInput containing a temporary file in the temp-file directory
     */
    public static final IEditorInput createTemporaryEmptyFile() {
        return createTemporaryEditorInput(createEmptyTempFile());
    }

    /**
     * Creates appropriate editor input.
     * 
     * @param file the file the method creates an editor input of
     * @return the created editor input
     */
    private static final IEditorInput createTemporaryEditorInput(File file) {
        IPath location = new Path(file.getAbsolutePath());

        return new PathEditorInput(location);
    }

    /**
     * Creates an empty file in the memory and fills it with the default message.<br>
     * The temporary files is consistent with the filename scheme <b>unsaved00x.txt</b>.
     * 
     * @return The new temporary file
     */
    private static final File createTempFile() {
        File tempfile = new File(new File(DirectoryService.getTempDir()), Messages.AbstractEditorService_2
                + getFormatedFilenumber(tempFileNumber++) + IConstants.TXT_FILE_TYPE_EXTENSION);

        FileOutputStream fos = null;
        FileInputStream fis = null;

        try {
            URL url = OperationsPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            File template = new File(FileLocator.toFileURL(url).getFile() + "templates" + File.separatorChar //$NON-NLS-1$
                    + Messages.AbstractEditorService_3);
            fos = new FileOutputStream(tempfile);
            fis = new FileInputStream(template);

            String s = ""; //$NON-NLS-1$
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = fis.read(buffer)) > -1) {
//                s = Charset.forName(IConstants.UTF8_ENCODING).decode(ByteBuffer.wrap(buffer, 0, read)).toString();
//                fos.write(s.getBytes());
                fos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while writing to an output stream", e, false); //$NON-NLS-1$
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException ex) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, ex);
            }
        }

        tempfile.deleteOnExit();
        return tempfile;
    }

    /**
     * Creates an empty file in the memory.<br>
     * The temporary files is consistent with the filename scheme <b>unsaved00x.txt</b>.
     * 
     * @return The new temporary file
     */
    private static final File createEmptyTempFile() {
        File tempfile = new File(new File(DirectoryService.getTempDir()), Messages.AbstractEditorService_2
                + getFormatedFilenumber(tempFileNumber++) + IConstants.TXT_FILE_TYPE_EXTENSION);

        FileOutputStream fos = null;
        FileInputStream fis = null;

        try {
            URL url = OperationsPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            File template = new File(FileLocator.toFileURL(url).getFile() + "templates" + File.separatorChar //$NON-NLS-1$
                    + "empty.txt"); //$NON-NLS-1$
            fos = new FileOutputStream(tempfile);
            fis = new FileInputStream(template);

            String s = ""; //$NON-NLS-1$
            byte[] buffer = new byte[4];
            int read = 0;
            while ((read = fis.read(buffer)) > -1) {
                s = Charset.forName(IConstants.UTF8_ENCODING).decode(ByteBuffer.wrap(buffer, 0, read)).toString();
                fos.write(s.getBytes());
            }
        } catch (IOException e) {
            LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while writing to an output stream", e, false); //$NON-NLS-1$
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException ex) {
                LogUtil.logError(OperationsPlugin.PLUGIN_ID, ex);
            }
        }

        tempfile.deleteOnExit();
        return tempfile;
    }

    /**
     * Resolves the given number to a String with leading 0s, if they are required (e.g. 1 -> '001', 10 -> '010', 128 ->
     * '128').
     * 
     * @param number A number
     * @return The number (with leading 0s) as a String
     * 
     * @author T. Kern
     */
    private static final String getFormatedFilenumber(int number) {
        if (0 < number && number < 10) {
            return "00" + String.valueOf(number); //$NON-NLS-1$
        } else if (10 <= number && number < 100) {
            return "0" + String.valueOf(number); //$NON-NLS-1$
        } else if (100 <= number && number < 1000) {
            return String.valueOf(number);
        }
        return null;
    }

}
