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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * In this class, values from a given type are polled.
 * Each Integer that is subject of choice in this set gets a rating
 * (zero: impossible; pos. infinite: 100% sure; 1: 50% (undecided))
 * 
 * @author Simon L
 *
 */
/**
 * @author Simon L
 * 
 */
public class PolledValue<T> {

	public static final double POSSIBILITY_HIGHLY_LIKELY = 6.0;
	public static final double POSSIBILITY_VERY_LIKELY = 3.2;
	public static final double POSSIBILITY_LIKELY = 2.0;
	public static final double POSSIBILITY_MAYBE = 1.8;
	public static final double POSSIBILITY_SLIGHTLY_OVER_DEFAULT = 1.4;
	public static final double POSSIBILITY_DEFAULT = 1.0;
	public static final double POSSIBILITY_SLIGHTLY_UNDER_DEFAULT = 1 / POSSIBILITY_SLIGHTLY_OVER_DEFAULT;
	public static final double POSSIBILITY_MAYNOTBE = 1 / POSSIBILITY_MAYBE;
	public static final double POSSIBILITY_UNLIKELY = 1 / POSSIBILITY_LIKELY;
	public static final double POSSIBILITY_VERY_UNLIKELY = 1 / POSSIBILITY_VERY_LIKELY;
	public static final double POSSIBILITY_HIGHLY_UNLIKELY = 1 / POSSIBILITY_HIGHLY_LIKELY;
	public static final double POSSIBILITY_IMPOSSIBLE = 0.0;

	protected Map<T, Double> internalRepresentation;

	public PolledValue() {
		internalRepresentation = new HashMap<T, Double>(0);
	}

	/**
	 * Looks up whether a given choice is contained in the poll
	 * 
	 * @param choice the choice
	 */
	public boolean hasChoice(T choice) {
		return internalRepresentation.containsKey(choice);
	}

	/**
	 * Add a choice to poll from, with default possibility. <br />
	 * If the choice already exists, it will be overwritten.
	 * 
	 * @param choice the integer
	 */
	public void addChoice(T choice) {
		this.addChoice(choice, POSSIBILITY_DEFAULT);
	}

	/**
	 * Removes a choice from the poll
	 * 
	 * @param choice the choice to remove
	 */
	public void removeChoice(T choice) {
		internalRepresentation.remove(choice);
	}

	/**
	 * Add a choice to poll from. <br />
	 * If the choice already exists, it will be overwritten.
	 * 
	 * @param choice             the integer
	 * @param initialPossibility
	 */
	public void addChoice(T choice, double initialPossibility) {
		internalRepresentation.put(choice, initialPossibility);
	}

	/**
	 * Gives all the choices the same default possibility
	 */
	public void setAllChoicesDefaultPossible() {
		for (Entry<T, Double> e : internalRepresentation.entrySet()) {
			e.setValue(POSSIBILITY_DEFAULT);
		}
	}

	/**
	 * returns the possibility that a given integer has.
	 * 
	 * @param choice the integer
	 * @return the possibility for this integer
	 */
	public double getPossibility(T choice) {
		if (internalRepresentation.containsKey(choice)) {
			return internalRepresentation.get(choice);
		}

		return POSSIBILITY_IMPOSSIBLE;
	}

	/**
	 * @return all integers that are subject of polling here
	 */
	public Set<T> getPollSubjects() {
		return internalRepresentation.keySet();
	}

	/**
	 * combines the possibilities of two polledIntegers into one. This Instance of
	 * PolledInteger will be altered, but will also return itself.
	 * 
	 * @param combineSubject
	 * @return
	 */
	public PolledValue<T> combineWith(PolledValue<T> combineSubject) {
		Set<T> combineInts = combineSubject.getPollSubjects();

		for (T subjectsChoice : combineInts) {
			double possibility = combineSubject.getPossibility(subjectsChoice);
			this.combinePossibility(subjectsChoice, possibility);
		}

		// remove choices that were not present in the combineSubject
		// The vice versa removal was done in the combine operation
		Set<T> removeSet = new HashSet<T>();
		for (T choice : this.getPollSubjects()) {
			if (!combineSubject.hasChoice(choice)) {
				removeSet.add(choice);
			}
		}
		for (T removeElement : removeSet)
			this.removeChoice(removeElement);

		return this;
	}

	/**
	 * combines a current possibility with another one, by multiplying the
	 * possibilities.
	 * 
	 * @param choice      the choice (integer)
	 * @param possibility the possibility itself
	 */
	public void combinePossibility(T choice, double possibility) {
		double combinedPossibility = possibility * this.getPossibility(choice);

		if (internalRepresentation.containsKey(choice)) {
			internalRepresentation.put(choice, combinedPossibility);
		}

	}

	private List<Entry<T, Double>> getSortedEntries() {
		List<Entry<T, Double>> sortedEntries = new LinkedList<Entry<T, Double>>(internalRepresentation.entrySet());

		// sort from high to low!
		Comparator<Entry<T, Double>> entryComparator = new Comparator<Entry<T, Double>>() {
			@Override
			public int compare(Entry<T, Double> o1, Entry<T, Double> o2) {
				if (o1.getValue() > o2.getValue())
					return -1;
				if (o2.getValue() > o1.getValue())
					return 1;
				return 0;
			}
		};

		Collections.sort(sortedEntries, entryComparator);

		return sortedEntries;
	}

	/**
	 * @return the most possible value.
	 */
	public T getBestValue() {
		List<Entry<T, Double>> sortedEntries = getSortedEntries();

		if (sortedEntries.size() > 0)
			return sortedEntries.get(0).getKey();
		return null;
	}

	/**
	 * Returns the values that were polled, ordered by possibility (highest first).
	 */
	public List<T> getValuesSortedByPossibility() {
		List<T> result = new LinkedList<T>();
		for (Entry<T, Double> entry : getSortedEntries()) {
			result.add(entry.getKey());
		}
		return result;
	}

	public int getChoiceCount() {
		return internalRepresentation.size();
	}

	/**
	 * Returns a clone of this polledValue, where all possibilities are set to
	 * {@link #POSSIBILITY_DEFAULT}.
	 */
	public PolledValue<T> cloneWithDefaultPossibilities() {
		PolledValue<T> result = clone();
		result.setAllChoicesDefaultPossible();
		return result;
	}

	@Override
	public String toString() {
		return getSortedEntries().toString();
	}

	@Override
	public PolledValue<T> clone() {
		Map<T, Double> internalRepresentationClone;
		internalRepresentationClone = new HashMap<T, Double>(internalRepresentation);
		PolledValue<T> clone = new PolledValue<T>();
		for (Entry<T, Double> internalEntry : internalRepresentationClone.entrySet()) {
			clone.addChoice(internalEntry.getKey(), internalEntry.getValue());
		}

		return clone;
	}
}
