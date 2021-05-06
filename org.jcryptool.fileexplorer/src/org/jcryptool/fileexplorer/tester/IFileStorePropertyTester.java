// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.fileexplorer.tester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.jcryptool.core.logging.utils.LogUtil;

/**
 * <p>
 * Tester for a IFileStore to check whether the file extension is of the expected type.
 * </p>
 *
 * <ul>
 * <li><b>isCascade:</b> The expected value may contain a dot before the extension (like <b>.xml</b>) or not, but it
 * must not contain a * (like <b>*.xml</b>).</li>
 * <li><b>isFile:</b> The expected value must be empty.</li>
 * <li><b>isRoot:</b> The expected value must be empty.</li>
 * </ul>
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public class IFileStorePropertyTester extends PropertyTester {
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        boolean result = false;
        try {
            if ("isCascade".equals(property)) {
                result = ((IFileStore) receiver).getName().endsWith(expectedValue.toString());
            } else if ("isFile".equals(property)) {
                result = !((IFileStore) receiver).fetchInfo().isDirectory();
                if (result) // for empty folders fetchInfo().isDirectory() = false, so this second test is necessary
                {
                    result = !((IFileStore) receiver).toLocalFile(EFS.NONE, null).isDirectory();
                }
            } else if ("isRoot".equals(property)) {
                result = ((IFileStore) receiver).getParent() == null ? false : true;
            }
        } catch (Exception ex) {
            LogUtil.logError(ex);

            result = false;
        }

        return result;
    }
}
