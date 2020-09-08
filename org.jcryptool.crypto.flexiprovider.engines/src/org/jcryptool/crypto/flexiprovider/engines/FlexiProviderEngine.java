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
package org.jcryptool.crypto.flexiprovider.engines;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.flexiprovider.descriptors.IFlexiProviderOperation;

import de.flexiprovider.api.keys.Key;

public abstract class FlexiProviderEngine {
	protected static class KeyObject{
		private final Key key;
		private final char[] password;
		public KeyObject(Key key, char[] password) {
			this.key = key;
			this.password = password;
		}
		public Key getKey() {
			return key;
		}
		public char[] getPassword() {
			return password;
		}
	}

    protected static final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    protected boolean initialized = false;
    protected IFlexiProviderOperation operation;
    private URI outputURI;

    private static int outputNumber = 1;

    /**
     * Initializes operation
     * @return returns the used password and key for initializing the algorithm
     */
    public abstract KeyObject init(IFlexiProviderOperation operation);

    public abstract void perform(KeyObject key);

    protected URI getOutputURI() {
        return outputURI;
    }

    protected char[] promptPassword() {
        InputDialog dialog = new InputDialog(shell, Messages.FlexiProviderEngine_0,
                Messages.FlexiProviderEngine_1, "", null) { //$NON-NLS-1$

            protected Control createDialogArea(Composite parent) {
                Control control = super.createDialogArea(parent);
                getText().setEchoChar('*');
                return control;
            }
        };
        int result = dialog.open();
        if (result == Window.OK) {
            return dialog.getValue().toCharArray();
        } else {
            return null;
        }
    }

    protected InputStream initInput(String input) {
        InputStream is = null;
        if (input.equals(Messages.InputType)) {
            if (EditorsManager.getInstance().isEditorOpen()) {
                is = EditorsManager.getInstance().getActiveEditorContentInputStream();
            } else {
                MessageDialog.openInformation(shell, Messages.FlexiProviderEngine_2,
                        Messages.FlexiProviderEngine_3);
                return null;
            }
        } else {
            is = getFileInputStream(input);
        }
        return is;
    }

    protected OutputStream initOutput(String output) {
        if (output.equals("<Editor>")) { //$NON-NLS-1$
            LogUtil.logInfo("outputURI: " + outputURI.toASCIIString()); //$NON-NLS-1$
            outputURI = getTempFileURI();
        } else {
            outputURI = URIUtil.toURI(output);
            LogUtil.logInfo("outputURI: " + outputURI.toASCIIString()); //$NON-NLS-1$
        }

        return getFileOutputStream(outputURI);
    }

    private OutputStream getFileOutputStream(URI uri) {
        try {
            IFileStore store = EFS.getStore(uri);
            return store.openOutputStream(EFS.NONE, null);
        } catch (CoreException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "CoreException while opening an output stream from a file store", e, false); //$NON-NLS-1$
        }
        return null;
    }

    private InputStream getFileInputStream(String path) {
        try {
            URI uri = URIUtil.toURI(path);
            IFileStore store = EFS.getStore(uri);
            return new BufferedInputStream(store.openInputStream(EFS.NONE, null));
        } catch (CoreException e) {
            LogUtil.logError(FlexiProviderEnginesPlugin.PLUGIN_ID, "CoreException while opening an input stream from a file store", e, false); //$NON-NLS-1$
        }
        return null;
    }

    protected URI getTempFileURI() {
        return URIUtil.toURI(DirectoryService.getTempDir() + File.separator + "Output" //$NON-NLS-1$ //$NON-NLS-2$
                + getFormatedFilenumber(outputNumber++) + ".bin"); //$NON-NLS-1$
    }

    /**
     * Resolves the given number to a String with leading 0s, if they are
     * required (e.g. 1 -> '001', 10 -> '010', 128 -> '128').
     *
     * @param number A number
     * @return The number (with leading 0s) as a String
     *
     * @author T. Kern
     */
    private String getFormatedFilenumber(int number) {
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
