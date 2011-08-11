//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
* 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.securerandom;

import org.jcryptool.crypto.flexiprovider.algorithms.ui.wizards.AlgorithmWizard;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.AlgorithmDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.algorithms.SecureRandomDescriptor;
import org.jcryptool.crypto.flexiprovider.descriptors.meta.interfaces.IMetaAlgorithm;



public class SecureRandomWizard extends AlgorithmWizard {

	private IMetaAlgorithm algorithm;
	private SecureRandomWizardPage page;
	private boolean canFinish = false;

	public SecureRandomWizard(IMetaAlgorithm algorithm) {
		super(algorithm);
		this.algorithm = algorithm;
	}

	public void setCanFinish(boolean value) {
		canFinish = value;
		if (getContainer() != null) {
			getContainer().updateButtons();
		}
	}
	
	public boolean canFinish() {
		return canFinish;
	}
	
	public void addPages() {
		page = new SecureRandomWizardPage(algorithm, this);
		addPage(page);
	}

	public AlgorithmDescriptor getDescriptor() {
		return new SecureRandomDescriptor(algorithm.getName(), length);
	}
	
	private int length;
	
	public boolean performFinish() {
		length = page.getLength();
		return true;
	}

}
