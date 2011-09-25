/*******************************************************************************
 * Copyright (c) 2011 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

import java.io.InputStream;

import org.apache.xml.security.c14n.Canonicalizer;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IEditorInput;
import org.eclipse.wst.xml.security.core.canonicalize.CreateCanonicalization;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.preferences.PreferenceConstants;
import org.eclipse.wst.xml.security.ui.utils.IXMLSecurityConstants;
import org.jcryptool.core.logging.dialogs.JCTMessageDialog;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithmAction;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.hybrid.HybridDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.util.constants.IConstants;

/**
 * <p>
 * Command used to start the <b>XML Canonicalization</b> for a new XML Canonicalization for the
 * selected XML document. The canonicalization process differs depending on whether editor content
 * or a file via a view should be canonicalized.
 * </p>
 *
 * <p>
 * This version maintains the XML comments.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class NewCanonicalizationMaintainCommand extends AbstractAlgorithmAction {
    /** Canonicalization version (exclusive or inclusive). */
    private String canonVersion;

    private void createCanonicalization() {
        getPreferenceValues();

        try {
            InputStream editorContent = getActiveEditorInputStream();

            if (editorContent != null) {
                byte[] outputBytes = canonicalize(editorContent);

                if (outputBytes != null && outputBytes.length > 0) {
	                IEditorInput output = AbstractEditorService.createOutputFile(outputBytes, IConstants.XML_FILE_TYPE_EXTENSION);
	                getActiveWorkbenchWindow().getActivePage().openEditor(output, IOperationsConstants.ID_TEXT_EDITOR);
                } else {
                	IStatus info = new Status(Status.WARNING, XSTUIPlugin.getId(), Messages.NewCanonicalizationMaintainCommand_0);
                	JCTMessageDialog.showInfoDialog(info);
                }
            }
        } catch (Exception ex) {
            LogUtil.logError(XSTUIPlugin.getId(), Messages.ErrorDuringCanonicalization, ex, true);
        }
    }

    /**
     * Determines the preference values for canonicalization.
     */
    private void getPreferenceValues() {
        IPreferenceStore store = XSTUIPlugin.getDefault().getPreferenceStore();

        canonVersion = store.getString(PreferenceConstants.CANON_TYPE);
    }

    /**
     * Calls the canonicalization method of the Apache XML Security API and executes the
     * canonicalization.
     *
     * @param stream The XML document to canonicalize as InputStream
     * @return The canonicalized XML
     * @throws Exception Exception during canonicalization
     */
    private byte[] canonicalize(final InputStream stream) throws Exception {
        CreateCanonicalization canonicalization = new CreateCanonicalization();
        canonicalization.init(stream, getCanonicalizationAlgorithm());

        return ((HybridDataObject) canonicalization.execute()).getOutput();
    }

    /**
     * Determines the canonicalization algorithm (exclusive or inclusive) based on the preference
     * selection. The algorithm type always maintains the comments. Version 1.1 is used in case of
     * inclusive canonicalization.
     *
     * @return The canonicalization algorithm to use
     */
    private String getCanonicalizationAlgorithm() {
        if (IXMLSecurityConstants.EXCLUSIVE_CANONICALIZATION.equals(canonVersion)) {
            return Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS;
        } else {
            return Canonicalizer.ALGO_ID_C14N11_WITH_COMMENTS;
        }
    }

    @Override
    public void run() {
        run(null);
    }

    @Override
    public void run(IDataObject dataobject) {
        createCanonicalization();
    }
}
