/*******************************************************************************
 * Copyright (c) 2010 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.propertytester;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;

/**
 * <p>
 * Tester for an IFile to check whether the file extension is of the expected type.
 * </p>
 *
 * <ul>
 * <li><b>isType:</b> The expected value may contain a dot before the extension (like
 * <b>.xml</b>) or not, but it must not contain a * (like <b>*.xml</b>).</li>
 * </ul>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class XMLPropertyTester extends PropertyTester {
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        return ((IFile) receiver).getName().endsWith(expectedValue.toString());
    }
}
