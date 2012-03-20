//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.vernam.ui;

import java.io.InputStream;

import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicTransformationPage;
import org.jcryptool.crypto.classic.model.ui.wizard.AbstractClassicWizard;
import org.jcryptool.crypto.classic.vernam.algorithm.VernamAlgorithm;
/**
 * The Wizard for the Vernam cipher
 * 
 * @author Michael Sommer (M1S)
 * @version 0.0.1
 *
 */
public class VernamWizard extends AbstractClassicWizard 
{
	private VernamWizardPage page;
	/**
	 * Creates a new instance of Vernam Wizard
	 */
	public VernamWizard(String windowTitle) 
	{
		super("Vernam Wizard");
	}
	/**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
	public void addPages()
	{
		page = new VernamWizardPage();
		addPage( page );
	}
	/**
	 * 
	 */
	public boolean encrypt()
	{
		return page.getEncrypt();
	}
	/**
	 * 
	 */
	public boolean decrypt()
	{
		return page.getDecrypt();
	}
	/**
	 * 
	 */
	public String getKey()
	{
		return page.getKey();
	}
	/**
	 * 
	 * @return
	 */
	public String getMyT()
	{
		return page.getMyText();
	}
}
