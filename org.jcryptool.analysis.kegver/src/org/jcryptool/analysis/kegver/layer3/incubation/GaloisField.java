// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer3.incubation;

import java.math.BigInteger;

import org.jcryptool.analysis.kegver.layer3.U;

public class GaloisField {

	private BigInteger order = U.ZERO;
	private BigInteger orderSuper = U.ZERO;
	private GaloisField aGF_g = null;
	private GaloisField aGF_h = null;
	private BigInteger numberOfElements = null;

	public GaloisField(BigInteger inOrder){
		this.setOrder(inOrder);
	}

	public GaloisField(BigInteger inOrder, BigInteger inOrderSuper){
		this.setOrder(inOrder);
//		this.setElements();
		this.setOrderSuper(inOrderSuper);
	}

	private BigInteger setOrderSuper(BigInteger inOrderSuper) {
		this.orderSuper = inOrderSuper;
		return this.getOrderSuper();
	}

	private BigInteger getOrderSuper() {
		return orderSuper;
	}

	public GaloisField calcGF_h(){
		BigInteger orderGF_g = this.getGF_g().getOrder();
		return null;
	}

	private GaloisField setGF_g(GaloisField inGF_g) {
		this.aGF_g = inGF_g;
		return this.getGF_g();
	}

	private GaloisField getGF_g() {
		return this.aGF_g;
	}

	public GaloisField selectGF_g() {
		GaloisField selectedGF_g = new GaloisField(U.selectUniformlyFrom0To(this.getOrder()), this.getOrder());
		return this.setGF_g(selectedGF_g);
	}

	private BigInteger setOrder(BigInteger inOrder) {
		this.order = inOrder;
		return this.getOrder();
	}

	private BigInteger getOrder() {
		return this.order;
	}

}
