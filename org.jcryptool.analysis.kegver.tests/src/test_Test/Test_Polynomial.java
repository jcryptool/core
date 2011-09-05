// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package test_Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jcryptool.analysis.kegver.layer2.Tools;
import org.junit.Test;


public class Test_Polynomial {

	@Test
	public void test_get2powKMinus1(){
		int ec = 0; 	//Exception counter
		try{
			Tools.get2powKMinus1(-1);
		} catch (ArrayIndexOutOfBoundsException e){
			ec++;
		}
		assertTrue(ec==1);
		try {
			Tools.get2powKMinus1(0);

		} catch (ArrayIndexOutOfBoundsException e){
			ec++;
		}
		assertTrue(ec==2);
		assertEquals(1, Tools.get2powKMinus1(1));
		assertEquals(2, Tools.get2powKMinus1(2));
		assertEquals(4, Tools.get2powKMinus1(3));
		assertEquals(1073741824, Tools.get2powKMinus1(31));
		try {
			Tools.get2powKMinus1(32);

		} catch (ArrayIndexOutOfBoundsException e){
			ec++;
		}
		assertTrue(ec==3);
	}

	/**
	 *
	 * Note: i >= 33 returns 0, i = 32 returns -2147483648,
	 *
	 * @param inK > 1
	 * @return
	 */
	public static int get2powKMinus1(int inK){		//2^(k-1)
		// Check
		if( ! ( 0 < inK && inK < 32 ) ) {
			throw new ArrayIndexOutOfBoundsException("Bounds: 0 < k < 32");
		}
		// Execute
		final Polynomial p = (new Polynomial(1, inK-1));
		// Return
		return p.evaluate(2);
	}

}
