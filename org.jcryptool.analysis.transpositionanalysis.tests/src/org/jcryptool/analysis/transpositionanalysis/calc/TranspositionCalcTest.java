// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.calc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TranspositionCalcTest {

    private PolledValue<Integer> polledInt1;
    private PolledValue<Integer> polledInt2;

    // Aiding procedures --------------------
    // --------------------------------------

    /**
     * ensures that the choices between (including) start and end are in the poll.
     *
     * @param start from this choice ...
     * @param end to this choice.
     */
    public void ensureChoices(int start, int end, PolledValue<Integer> poll) {
        for (int i = start; i <= end; i++) {
            if (!poll.hasChoice(i)) {
                poll.addChoice(i);
            }
        }
    }

    /**
     * ensures that the choices between (including) start and end are in the poll, and no others.
     *
     * @param start from this choice ...
     * @param end to this choice.
     */
    public void ensureExclusiveChoices(int start, int end, PolledValue<Integer> poll) {
        ensureChoices(start, end, poll);

        Vector<Integer> removeItems = new Vector<Integer>(0);
        for (Integer choice : poll.getPollSubjects()) {
            if (choice < start || choice > end) {
                removeItems.add(choice);
            }
        }
        for (Integer choice : removeItems) {
            poll.removeChoice(choice);
        }
    }

    // Tests --------------------------
    // --------------------------------

    @Before
    public void before() {
        polledInt1 = new PolledValue<Integer>();
        polledInt2 = new PolledValue<Integer>();
    }

    @Test()
    public void blocklengthsByTextlength() {
        Integer[] expectedNoMargins = new Integer[] {1, 2, 3, 4, 6, 12};
        Integer[] expectedWithMargins = new Integer[] {2, 3, 4, 6};

        assertArrayEquals(expectedNoMargins, TranspositionCalc.blocklengthsByTextlength(12, -1, -1));
        assertArrayEquals(expectedWithMargins, TranspositionCalc.blocklengthsByTextlength(12, 2, 8));
    }

    @Test()
    public void pollIntegers() {

        // create choices from 0 to 4
        ensureChoices(0, 4, polledInt1);
        ensureChoices(1, 5, polledInt2);

        // default possibility for freshly created choices
        assertEquals(PolledValue.POSSIBILITY_DEFAULT, polledInt1.getPossibility(2), 0.01);
        // zero possibility for choices that are not included.
        assertEquals(PolledValue.POSSIBILITY_IMPOSSIBLE, polledInt1.getPossibility(5), 0.01);

        // Rate the choice "1" first a bit to "unlikely", and then a bit more to "likely"
        polledInt1.combinePossibility(1, PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT);
        polledInt1.combinePossibility(1, PolledValue.POSSIBILITY_LIKELY);

        // Possibility of "1" should now be higher than default. The Value of the PolledInteger
        // should be now "1"
        assertTrue(polledInt1.getPossibility(1) > PolledValue.POSSIBILITY_DEFAULT);
        assertEquals(Integer.valueOf(1), polledInt1.getBestValue());

        // combine possibility that is not included yet. should still not be included after.
        polledInt1.combinePossibility(6, PolledValue.POSSIBILITY_HIGHLY_LIKELY);
        assertFalse(polledInt1.hasChoice(6));

        // full test range
        // complete polledInt1
        polledInt1.combinePossibility(0, PolledValue.POSSIBILITY_VERY_UNLIKELY);
        polledInt1.combinePossibility(0, PolledValue.POSSIBILITY_LIKELY);
        polledInt1.combinePossibility(0, PolledValue.POSSIBILITY_SLIGHTLY_OVER_DEFAULT);
        polledInt1.addChoice(1, PolledValue.POSSIBILITY_DEFAULT);
        polledInt1.combinePossibility(1, PolledValue.POSSIBILITY_LIKELY);
        polledInt1.combinePossibility(1, PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT);
        polledInt1.combinePossibility(2, PolledValue.POSSIBILITY_VERY_UNLIKELY);
        polledInt1.combinePossibility(2, PolledValue.POSSIBILITY_HIGHLY_LIKELY);
        polledInt1.combinePossibility(3, PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT);
        polledInt1.combinePossibility(4, PolledValue.POSSIBILITY_VERY_UNLIKELY);
        polledInt1.combinePossibility(4, PolledValue.POSSIBILITY_LIKELY);

        // complete polledInt2
        polledInt2.combinePossibility(1, PolledValue.POSSIBILITY_LIKELY);
        polledInt2.combinePossibility(1, PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT);
        polledInt2.combinePossibility(2, PolledValue.POSSIBILITY_SLIGHTLY_OVER_DEFAULT);
        polledInt2.combinePossibility(3, PolledValue.POSSIBILITY_LIKELY);
        polledInt2.combinePossibility(3, PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT);
        polledInt2.combinePossibility(3, PolledValue.POSSIBILITY_VERY_UNLIKELY);
        polledInt2.combinePossibility(4, PolledValue.POSSIBILITY_HIGHLY_LIKELY);
        polledInt2.combinePossibility(5, PolledValue.POSSIBILITY_SLIGHTLY_UNDER_DEFAULT);
        polledInt2.combinePossibility(5, PolledValue.POSSIBILITY_VERY_UNLIKELY);
        polledInt2.combinePossibility(5, PolledValue.POSSIBILITY_LIKELY);

        // combine both into
        PolledValue<Integer> combinedInteger = polledInt1.combineWith(polledInt2);

        // test containing behaviour
        assertFalse(combinedInteger.hasChoice(0));
        assertFalse(combinedInteger.hasChoice(5));

        assertEquals(Integer.valueOf(4), combinedInteger.getBestValue());
    }

    @After
    public void after() {

    }

}
