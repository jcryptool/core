package org.jcryptool.core.cryptosystem.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Models alphabets with a finite number of elements 
 * which are maintained in a specific order (stored as a list). 
 * 
 * @param <C> the boundary generic type for elements 
 * contained in this alphabet
 * 
 * @author Simon L
 */
public abstract class Alphabet<C> {

	List<C> alphabetElements;

	/**
	 * Creates an alphabet by specifying its elements. The order in which the
	 * iterator of the collection returns the elements will be maintained.
	 * 
	 * @param alphabetElements
	 *            the alphabet elements.
	 * @throws IllegalArgumentException
	 *             if the collection is null or if the collection is empty
	 */
	public Alphabet(Collection<? extends C> alphabetContent) {
		if (alphabetContent != null) {
			if (alphabetContent.size() > 0) {
				this.alphabetElements = new LinkedList<C>(alphabetContent);
			} else {
				throw new IllegalArgumentException(
						"The alphabet collection may not be empty");
			}
		} else {
			throw new IllegalArgumentException(
					"The alphabet collection may not be null");
		}
	}

	/**
	 * @see #Alphabet(Collection)
	 */
	public Alphabet(C[] alphabetContent) {
		this(Arrays.asList(alphabetContent));
	}

	/**
	 * @return the elements of the alphabet
	 */
	public List<C> getContent() {
		return alphabetElements;
	}

	/**
	 * Filters non-alphabet content from a list (alters the given list!)
	 * 
	 * @param toFilter
	 *            the list to filter
	 */
	public void filterByAlphabet(List<C> toFilter) {
		for (Iterator<C> iterator = toFilter.iterator(); iterator.hasNext();) {
			C element = (C) iterator.next();
			if (!getContent().contains(element))
				iterator.remove();
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (C element : alphabetElements) {
			String prefix = ( alphabetElements.indexOf(element) > 0 ) ? 
						( (element instanceof Character) ? "" : ",") : 
						"";
			builder.append(prefix.concat(element.toString()));
		}

		return builder.toString();
	}

}
