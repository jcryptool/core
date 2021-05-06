// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.views.factories;

import java.util.regex.Pattern;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.fileexplorer.FileExplorerPlugin;

/**
 * Determines the images for folders and files. The default file icon is selected if one is
 * available.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class ImageFactory {
    private static final String ICON_FILE = "icons/file.png"; //$NON-NLS-1$
    private static final String ICON_FILE_BINARY = "icons/file_binary.png"; //$NON-NLS-1$
    private static final String ICON_FOLDER = "icons/folder.png"; //$NON-NLS-1$
    private static final String ICON_FOLDER_NOACCESS = "icons/folder_noaccess.png"; //$NON-NLS-1$
    private static final String ICON_DEVICE = "icons/device.png"; //$NON-NLS-1$
    private static final String ICON_DEVICE_NOACCESS = "icons/device_noaccess.png"; //$NON-NLS-1$
    private static final String ICON_DEVICE_CD_DVD = "icons/device_cd_dvd.png"; //$NON-NLS-1$
    private static final String ICON_DEVICE_CD_DVD_NOACCESS = "icons/device_cd_dvd_noaccess.png"; //$NON-NLS-1$

    public static ImageDescriptor getIconForFile(IFileStore file) {
        try {
            if (Pattern.matches("[a-zA-Z]:\\\\", file.toLocalFile(EFS.NONE, null).getAbsolutePath())) { //$NON-NLS-1$
                // CD or DVD
                if (!file.toLocalFile(EFS.NONE, null).canWrite()) {
                    if (file.toLocalFile(EFS.NONE, null).canRead()) {
                    	return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_DEVICE_CD_DVD);
                    }
                    return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_DEVICE_CD_DVD_NOACCESS);
                }
                // HDD
                if (!file.toLocalFile(EFS.NONE, null).canRead()) {
                	return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_DEVICE_NOACCESS);
                }
                return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_DEVICE);
            }
        } catch (CoreException ex) {
            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
        }

        // handle directories
        if (file.fetchInfo().isDirectory()) {
        	return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_FOLDER);
        }

        // handled directories here are unaccessible
        try {
            if (file.toLocalFile(EFS.NONE, null).isDirectory()) {
                return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_FOLDER_NOACCESS);
            }
        } catch (CoreException ex) {
            LogUtil.logError(FileExplorerPlugin.PLUGIN_ID, ex);
        }

        // handle by extension
        int dot = file.getName().indexOf('.');
        if (dot > 0) {
            String extension = file.getName().substring(dot);
            Program program = Program.findProgram(extension);
            ImageData imageData = (program == null ? null : program.getImageData());
            // handle if a program defines an icon
            if (imageData != null) {
                return ImageDescriptor.createFromImageData(imageData);
            }
            // handle the bin extension for the hex editor
            if (extension.equalsIgnoreCase(".bin")) { //$NON-NLS-1$
            	return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_FILE_BINARY);
            }
        }

        // assign a default icon
        return ImageService.getImageDescriptor(FileExplorerPlugin.PLUGIN_ID, ImageFactory.ICON_FILE);
    }
}
