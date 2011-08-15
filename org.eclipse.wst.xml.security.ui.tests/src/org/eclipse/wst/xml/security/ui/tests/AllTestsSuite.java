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

import junit.framework.TestSuite;

import org.eclipse.wst.xml.security.ui.tests.verify.SignatureViewTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This class specifies all the bundles of this component that provide a test suite to run during
 * automated testing.
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
@RunWith(Suite.class)
@SuiteClasses( { SignatureViewTest.class})
public class AllTestsSuite extends TestSuite {
    public AllTestsSuite() {
        super("All XML Security Tools UI Test Suite");
        addTest(XmlSecurityToolsUiTestSuite.suite());
    }

    /**
     * This is just required to run in a development environment workbench.
     */
    public void testAll() {
    }
}
