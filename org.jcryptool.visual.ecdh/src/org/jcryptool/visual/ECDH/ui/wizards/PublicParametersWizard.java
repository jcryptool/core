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

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.visual.ECDH.algorithm.EC;
import org.jcryptool.visual.ECDH.algorithm.ECPoint;

import de.flexiprovider.common.math.FlexiBigInt;
import de.flexiprovider.common.math.ellipticcurves.EllipticCurve;
import de.flexiprovider.common.math.ellipticcurves.Point;

public class PublicParametersWizard extends Wizard {
	private PublicParametersWizardPage page;
	private ECPoint generator;
	private EC curve;
	private int order;
	private boolean large;
	private EllipticCurve largeCurve;
	private Point pointG;
	private FlexiBigInt largeOrder;

	public PublicParametersWizard(EC c, ECPoint g) {
		super();
		curve = c;
		generator = g;
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new PublicParametersWizardPage(curve, generator);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		large = page.isLarge();
		if(large) {
			largeCurve = page.getLargeCurve();
			pointG = page.getLargeGenerator();
			largeOrder = page.getLargeOrder();
		} else {
			generator = page.getGenerator();
			curve = page.getCurve();
			order = page.getOrder();
		}
		return true;
	}


	public ECPoint getGenerator() {
		return generator;
	}

	public EC getCurve() {
		return curve;
	}

	public int getOrder() {
		return order;
	}

	public boolean isLarge() {
		return large;
	}

	public EllipticCurve getLargeCurve() {
		return largeCurve;
	}

	public Point getLargeGenerator() {
		return pointG;
	}

	public FlexiBigInt getLargeOrder() {
		return largeOrder;
	}
}