//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jcryptool.analysis.transpositionanalysis.calc.transpositionanalysis.model.PolledTranspositionKey;
import org.junit.Test;

public class PolledTranspositionKeyTest {
	
	@Test
	public void bestSolutionCase1() {
		PolledTranspositionKey key1 = new PolledTranspositionKey();
		key1.setLength(4);
		
		key1.setPossibility(0, 0, 5);
		key1.setPossibility(0, 1, 2);
		key1.setPossibility(0, 2, 3);
		key1.setPossibility(0, 3, 1);
		
		key1.setPossibility(1, 0, 3);
		key1.setPossibility(1, 1, 3);
		key1.setPossibility(1, 2, 1);
		key1.setPossibility(1, 3, 4);
		
		key1.setPossibility(2, 0, 6);
		key1.setPossibility(2, 1, 3);
		key1.setPossibility(2, 2, 5);
		key1.setPossibility(2, 3, 2);
		
		key1.setPossibility(3, 0, 1);
		key1.setPossibility(3, 1, 4);
		key1.setPossibility(3, 2, 5);
		key1.setPossibility(3, 3, 1);
		
		/** Array:
		 * Position -------
		 * 		0|	5 2 3 1  Sequenz 0-3-2-1 = 400
		 * 		1|	3 3 1 4
		 * 		2|	6 3 5 2
		 * 		3|	1 4 5 1
		 */
		
		int[] solutionArray = key1.getBestChoice().toArray();
		assertArrayEquals(new int[]{0, 3, 2, 1}, solutionArray);
	}
	
	@Test
	public void bestSolutionCase2() {
		PolledTranspositionKey key1 = new PolledTranspositionKey();
		key1.setLength(3);
		
		key1.setPossibility(0, 0, 9);
		key1.setPossibility(0, 1, 7);
		key1.setPossibility(0, 2, 7);
		
		key1.setPossibility(1, 0, 8);
		key1.setPossibility(1, 1, 0.2);
		key1.setPossibility(1, 2, 0.3);
		
		key1.setPossibility(2, 0, 3);
		key1.setPossibility(2, 1, 0.7);
		key1.setPossibility(2, 2, 1.3);
		
		/** Array:
		 * Position -------
		 * 		0|	9.0   7    7    Sequenz 1-0-2 = 72.8
		 * 		1|	8.0  0.2  0.3
		 * 		2|	3.0  0.7  1.3
		 */
		
		int[] solutionArray = key1.getBestChoice().toArray();
		assertArrayEquals(new int[]{1, 0, 2}, solutionArray);
	}
	
	@Test
	public void testStandardOperations() {
		// Length of void key
		PolledTranspositionKey test1 = new PolledTranspositionKey();
		assertEquals(0, test1.getLength());
		//invertability of to/fromArray
		double[][] possibilityArray = new double[3][];
		possibilityArray[0] = new double[]{2, 1, 1};
		possibilityArray[1] = new double[]{3, 1, 5};
		possibilityArray[2] = new double[]{6, 2, 1};
		test1 = new PolledTranspositionKey(possibilityArray);
		assertTrue(Arrays.deepEquals(possibilityArray, test1.toArray()));
		//combine some possibilities
		test1.combinePossibility(1, 0, (double)4/(double)3);
		assertEquals(4, test1.toArray()[1][0], 0.01);
	}
}
