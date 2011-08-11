// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TranspositionKeyTest {

    private TranspositionKey key1 = new TranspositionKey();
    private TranspositionKey key2 = new TranspositionKey();
    private TranspositionKey key3 = new TranspositionKey();

    // incomplete set (3 is missing, 4 is double)
    int[] rawKey1 = new int[] {0, 2, 1, 4, 5, 4};

    // complete set
    int[] rawKey2 = new int[] {1, 2, 0, 5, 3, 4};

    // incomplete set (5 is missing, 7 is missing)
    int[] rawKey3 = new int[] {1, 2, 0, 5, 3, 7};

    @Before
    public void before() {
        key1.fromArray(rawKey1);
        key2.fromArray(rawKey2);
        key3.fromArray(rawKey3);
    }

    @Test()
    public void positionTests() {
        // normal case
        assertEquals(0, key1.get(0));
        assertEquals(5, key2.get(3));

        // not set field
        assertEquals(-1, key1.get(3));
        assertEquals(-1, key3.get(6));
        assertEquals(-1, key3.get(7));

        // toArray reversability (only with complete sets (all positions are defined))
        int[] arrayFromKey = key2.toArray();
        assertArrayEquals(arrayFromKey, rawKey2);
    }

    @Test()
    public void otherUtilsTest() {
        // Length
        assertEquals(6, key1.getLength());
        assertEquals(6, key2.getLength());
        assertEquals(8, key3.getLength());

        // Key to String
        // Reversability of toString and fromString
        int[] key1Before = key1.toArray();
        String key1String = key1.toString();
        assertEquals("0|2|1|-1|5|4", key1String);
        key1.fromString(key1String);
        int[] key1After = key1.toArray();
        assertArrayEquals(key1Before, key1After);

        // One-relative output
        assertEquals("1|3|2|-1|6|5", key1.toStringOneRelative());

        // setLength - making the key smaller
        key3.setLength(4);
        assertArrayEquals(new int[] {1, 2, 0, -1}, key3.toArray());

        // Testing, if everything is right with alphabet to pure String (without delimiters, with alphabet given)

        TranspositionKey k4 = new TranspositionKey(new int[] {3, 1, 2, 0, 4});
        TranspositionKey k5 = new TranspositionKey(new int[] {3, 11, 10, 8, 9, 5, 7, 6, 2, 4, 1, 0});
        TranspositionKey k6 = new TranspositionKey(new int[] {3, 11, 10, 8, 9, 5, 7, 6, 2, 4, 1, 0, 12, 13, 14, 15, 16});
        char[] alpha1 = new char[] {'a', '0', 'b', '1', 'c', '2', '3', '4', '5', '6', '7', '8', 'd', '9'};

        assertEquals("31204", TranspositionKey.getKeyInChars(alpha1, k4));

        // There and back again
        TranspositionKey testK5 = new TranspositionKey(TranspositionKey.getKeyInChars(alpha1, k5), alpha1);
        assertArrayEquals(k5.toArray(), testK5.toArray());

        // Null (doesnt fit into alphabet)
        assertNull(TranspositionKey.getKeyInChars(alpha1, k6));
        

    }

    @After
    public void after() {

    }

}
