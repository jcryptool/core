//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.transpositionanalysis.calc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * In this class, an Integer is polled from a finite number of Integers. See
 * {@link PolledValue} for more information.
 * 
 * @author Simon L
 * 
 */
public class PolledPositiveInteger extends PolledValue<Integer> {

	@Override
	public void addChoice(Integer choice, double initialPossibility) {
		if (choice >= 0)
			super.addChoice(choice, initialPossibility);
		else
			throw new IllegalArgumentException("only positive Integers allowed to be added."); //$NON-NLS-1$
	}

	public boolean containsAllValuesInRange(int start, int end) {
		if (start > end)
			throw new IllegalArgumentException("start must be less than or equal to end"); //$NON-NLS-1$
		for (int i = start; i <= end; i++) {
			if (!internalRepresentation.containsKey(i))
				return false;
		}
		return true;
	}

	@Override
	public PolledPositiveInteger clone() {
		Map<Integer, Double> internalRepresentationClone;
		internalRepresentationClone = new HashMap<Integer, Double>(internalRepresentation);
		PolledPositiveInteger clone = new PolledPositiveInteger();
		for (Entry<Integer, Double> internalEntry : internalRepresentationClone.entrySet()) {
			clone.addChoice(internalEntry.getKey(), internalEntry.getValue());
		}

		return clone;
	}

	@Override
	public PolledPositiveInteger cloneWithDefaultPossibilities() {
		PolledPositiveInteger result = clone();
		result.setAllChoicesDefaultPossible();
		return result;
	}

}
