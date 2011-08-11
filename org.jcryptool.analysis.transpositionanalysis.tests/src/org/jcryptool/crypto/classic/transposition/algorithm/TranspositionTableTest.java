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

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 *
 *
 * @author Simon L
 */
public class TranspositionTableTest {

	@Test
	public void roughCases() {
		TranspositionTable table = new TranspositionTable(1);
		table.fillCharsIntoTable("".toCharArray(), true);
		table.fillCharsIntoTable("BLABLA".toCharArray(), false);
	}

	@Test
	public void testReadFunctionality() {
		TranspositionTable table1 = new TranspositionTable(5);
		String in = "Simonistderallerdollste";

		table1.fillCharsIntoTable(in.toCharArray(), true);
		String out = String.valueOf(table1.readOutContent(true));

		assertEquals(in, out);

		/**
		 *
		say we encrypted that text with "25341".
		Result would be:
		-----
		inmoS
		setdi
		aellr
		dlolr
		t0e0s
		-----
		Say we received that text, and know the decryption key. the blanks positions
		should be calculated through the key. (as we can see, it has been read in/out row-wise)
		 */

		String chiffre = "inmoSsetdiaellrdlolrtes";
		TranspositionKey key = new TranspositionKey(new int[]{1,4,2,3,0});

		TranspositionTable table2 = new TranspositionTable(5);
		TranspositionTable table3 = new TranspositionTable(5);
		table2.fillCharsIntoTable(chiffre.toCharArray(), false, key);

		//read columnwise to see if it happened correctly
		String expectedColumRead = "isadtneelmtloeodllSirrs";
		String actualColumnRead = String.valueOf(table2.readOutContent(true));
		assertEquals(expectedColumRead, actualColumnRead);

		//now decrypt with inverse key and see what it brings :D
		String expectedDechiffite = in;
		table2.transposeColumns(key.getReverseKey());
		String actualDechiffrite = String.valueOf(table2.readOutContent(false));

		assertEquals(expectedDechiffite, actualDechiffrite);

		//now decrypt with inverse key, but dont prepare a mask.
		table3.fillCharsIntoTable(chiffre.toCharArray(), false);
		table3.transposeColumns(key.getReverseKey());
		actualDechiffrite = String.valueOf(table3.readOutContent(false));
	}

}
