/*******************************************************************************
 * Copyright (c) 2013 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.jcryptool.crypto.xml.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jcryptool.crypto.xml.core.tests.sign.CreateSignatureTest;
import org.jcryptool.crypto.xml.core.tests.utils.UtilsTest;
import org.jcryptool.crypto.xml.core.tests.utils.XmlSecurityConstantsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class sets up the XML Security Tools test suite.
 * 
 * @author Dominik Schadow
 * @version 1.0.0
 */
@RunWith(Suite.class)
@SuiteClasses({ CreateSignatureTest.class, UtilsTest.class, XmlSecurityConstantsTest.class })
public class XmlSecurityToolsCoreTestSuite extends TestSuite {
    public static Test suite() {
        return new XmlSecurityToolsCoreTestSuite();
    }

    public XmlSecurityToolsCoreTestSuite() {
        super("XML Security Core Tests");
        addTestSuite(CreateSignatureTest.class);
        addTestSuite(UtilsTest.class);
        addTestSuite(XmlSecurityConstantsTest.class);
    }
}
