// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ecc.algorithm;

public class FmPoint extends FpPoint {
	private final int max;

	public FmPoint(FpPoint p, int max) {
		this.x = p.x;
		this.y = p.y;
		this.max = max;
		this.infinite = p.isInfinite();
	}

	@Override
	public String toString() {
		if(infinite)
			return "(inf)"; //$NON-NLS-1$

		String s = "("; //$NON-NLS-1$

		if(x==0)s+="1"; //$NON-NLS-1$
		else if(x==max-1)s+="0"; //$NON-NLS-1$
		else s+="g"+x; //$NON-NLS-1$

		s += "|"; //$NON-NLS-1$

		if(y==0)s+="1"; //$NON-NLS-1$
		else if(y==max-1)s+="0"; //$NON-NLS-1$
		else s+="g"+y; //$NON-NLS-1$

		return s + ")"; //$NON-NLS-1$
	}
}
