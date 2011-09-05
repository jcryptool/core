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
import org.jcryptool.visual.ECDH.algorithm.EC;
import org.jcryptool.visual.ECDH.algorithm.ECPoint;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurve;
import de.flexiprovider.common.math.ellipticcurves.Point;

public class PublicParametersWizardPage extends WizardPage {
	private PublicParametersComposite composite;
	private EC curve;
	private ECPoint generator;

	public PublicParametersWizardPage(EC c, ECPoint g) {
		super(Messages.getString("ECDHWizPP.title")); //$NON-NLS-1$
		setTitle(Messages.getString("ECDHWizPP.title")); //$NON-NLS-1$
		setDescription(Messages.getString("ECDHWizPP.textCurve")); //$NON-NLS-1$
		curve = c;
		generator = g;
	}

	public void createControl(Composite parent) {
		setPageComplete(false);
		composite = new PublicParametersComposite(parent, NONE, this, curve, generator);
		setControl(composite);
	}


	public ECPoint getGenerator() {
		return composite.getGenerator();
	}

	public EC getCurve() {
		return composite.getCurve();
	}

	public int getOrder() {
		return composite.getOrder();
	}

	public boolean isLarge() {
		return composite.isLarge();
	}

	public EllipticCurve getLargeCurve() {
		return composite.getLargeCurve();
	}

	public Point getLargeGenerator() {
		return composite.getLargeGenerator();
	}

	public FlexiBigInt getLargeOrder() {
		return composite.getLargeOrder();
	}
}