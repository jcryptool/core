/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.xml.security.ui.tests.verify.SignatureViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class sets up the XML Security Tools test suite.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
@RunWith(Suite.class)
@SuiteClasses( { SignatureViewTest.class})
public class XmlSecurityToolsUiTestSuite extends TestSuite {
    public static Test suite() {
        return new XmlSecurityToolsUiTestSuite();
    }

    public XmlSecurityToolsUiTestSuite() {
        super("XML Security UI Tests");
        addTestSuite(SignatureViewTest.class);
    }
}

