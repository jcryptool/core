/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.canonicalize;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xml.security.c14n.Canonicalizer;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.modern.AbstractModernAlgorithm;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.modern.hybrid.HybridDataObject;

/**
 * <p>
 * Does the canonicalization for the selected XML document with or without comments. This decision is based on the
 * clicked menu item. Depending on the preference settings a new editor with the canonicalized version is opened or the
 * current editor content (or file) is updated directly.
 * </p>
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class CreateCanonicalization extends AbstractModernAlgorithm {
    private String uri;
    private HybridDataObject dataObject;

    /**
     * Canonicalizes the selected XML document with the determined canonicalization method identified by the given URI.
     *
     * @param input The XML document to canonicalize as InputStream
     * @param uri Defines the canonicalization URI (with or without comments)
     * @return Byte array of XML data
     * @throws IOException Any IO exception during processing
     */
    public void init(final InputStream input, final String uri) throws IOException {
        this.uri = uri;
        dataObject = new HybridDataObject();

        byte[] content = new byte[input.available()];
        input.read(content);

        dataObject.setInput(content);
    }

    @Override
    public IDataObject getDataObject() {
        return dataObject;
    }

    @Override
    public IDataObject execute() {
        try {
            Canonicalizer c14n = Canonicalizer.getInstance(uri);
            dataObject.setOutput(c14n.canonicalize(dataObject.getInput()));
        } catch (Exception ex) {
            LogUtil.logError(XmlSecurityPlugin.getId(), ex);
        }

        return dataObject;
    }

    @Override
    public String getAlgorithmName() {
        return "XML Canonicalization"; //$NON-NLS-1$
    }
}
