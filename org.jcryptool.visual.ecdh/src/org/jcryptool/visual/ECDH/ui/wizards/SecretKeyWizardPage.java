// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.ECDH.Messages;

import de.flexiprovider.common.math.FlexiBigInt;

public class SecretKeyWizardPage extends WizardPage {
	private String name;
	private int max;
	private int secret;
	private FlexiBigInt largeMax;
	private FlexiBigInt largeSecret;
	private SecretKeyComposite composite;
	private boolean large;

	public SecretKeyWizardPage(String n, int s, int m) {
		super(Messages.getString("ECDHWizSK.title")); //$NON-NLS-1$
		large = false;
		name = n;
		max = m;
		secret = s;
		setTitle(Messages.getString("ECDHWizSK.title1") + " " + name + Messages.getString("ECDHWizSK.title2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setDescription(Messages.getString("ECDHWizSK.description1") + " (" + max + ")\n" + Messages.getString("ECDHWizSK.description2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public SecretKeyWizardPage(String n, FlexiBigInt s, FlexiBigInt m) {
		super(Messages.getString("ECDHWizSK.title")); //$NON-NLS-1$
		large = true;
		name = n;
		largeMax = m;
		largeSecret = s;
		setTitle(Messages.getString("ECDHWizSK.title1") + " " + name + Messages.getString("ECDHWizSK.title2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		setDescription(Messages.getString("ECDHWizSK.description1") + ".\n" + Messages.getString("ECDHWizSK.description2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public void createControl(Composite parent) {
		if(large)
			composite = new SecretKeyComposite(parent, NONE, name, largeSecret, largeMax);
		else
			composite = new SecretKeyComposite(parent, NONE, name, secret, max);
		setControl(composite);
		setPageComplete(true);
	}

	public int getSecret() {
		return composite.getSecret();
	}

	public FlexiBigInt getLargeSecret() {
		return composite.getLargeSecret();
	}
}