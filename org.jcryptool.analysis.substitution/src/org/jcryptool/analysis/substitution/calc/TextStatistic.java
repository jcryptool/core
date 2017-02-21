//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.calc;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class TextStatistic {
	
	private static final int MIN_TRIPLET_COUNT_FOR_CURBING = 35;
	private static final int MIN_DOUBLET_COUNT_FOR_CURBING = 35;

	private static final double ALPHABETS_DATA_FITS_THRESHHOLD = 0.8;

	public String getLanguage() {
		return language;
	}

	public String getName() {
		return name;
	}

	private String language;
	private String name;
	
	private Map<Character, Integer> characterOccurrences;
	private Map<String, Integer> doubletOccurrences;
	private Map<String, Integer> tripletOccurrences;
	private Integer totalCharacterCount;
	private Integer totalDoubletCount;
	private Integer totalTripletCount;
	private Set<Character> characterSet;

	private double getFrequencyFor(Map<?, Integer> map, Object key, Integer total) {
		Integer occurrence = map.get(key);
		if(occurrence == null || occurrence == 0) {
			return 0;
		}
		return (double)occurrence/(double)total;
	}
	
	public TextStatistic(String text, String name, String language) {
		this.name = name;
		this.language = language;
		Map<Character, Integer> newCharacterOccurrences = new HashMap<Character, Integer>();
		Map<String, Integer> newDoubletOccurrences = new HashMap<String, Integer>();
		Map<String, Integer> newTripletOccurrences = new HashMap<String, Integer>();
		analyze(text, newCharacterOccurrences, newDoubletOccurrences, newTripletOccurrences);

		this.totalCharacterCount = newCharacterOccurrences.keySet().size();
		this.totalDoubletCount = countTotalDoublets(newDoubletOccurrences);
		this.totalTripletCount = countTotalTriplets(newTripletOccurrences);
		
		this.characterSet = newCharacterOccurrences.keySet();
		
		newDoubletOccurrences = curbDoublets(newDoubletOccurrences);
		newTripletOccurrences = curbTriplets(newTripletOccurrences);
		newCharacterOccurrences = curbUnetts(newCharacterOccurrences);
		
		this.characterOccurrences = newCharacterOccurrences;
		this.doubletOccurrences = newDoubletOccurrences;
		this.tripletOccurrences = newTripletOccurrences;
	}
	
	public TextStatistic(File textFile, TransformData preAnalysisTransformation, String name, String language) {
		this(convertFileToText(textFile, preAnalysisTransformation), name, language);
	}

	public TextStatistic(String string, String name) {
		this(string, name, "Generic"); //$NON-NLS-1$
	}
	
	public TextStatistic(String string) {
		this(string, "Generic", "Generic"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String convertFileToText(File textFile, TransformData preAnalysisTransformation) {
		// TODO Auto-generated method stub
		return null;
	}

	private LinkedHashMap<String, Integer> curbTriplets(Map<String, Integer> newTripletOccurrences) {
		int curbCount = Math.min(MIN_TRIPLET_COUNT_FOR_CURBING, newTripletOccurrences.size());
		return curbMapHighestValuesRemain(newTripletOccurrences, curbCount);
	}

	private LinkedHashMap<String, Integer> curbDoublets(Map<String, Integer> newDoubletOccurrences) {
		int curbCount = Math.min(MIN_DOUBLET_COUNT_FOR_CURBING, newDoubletOccurrences.size());
		return curbMapHighestValuesRemain(newDoubletOccurrences, curbCount);
	}
	
	private LinkedHashMap<Character, Integer> curbUnetts(Map<Character, Integer> newUnettOccurrences) {
		int curbCount = Math.min(Integer.MAX_VALUE, newUnettOccurrences.size());
		return curbMapHighestValuesRemainChar(newUnettOccurrences, curbCount);
	}

	private LinkedHashMap<Character, Integer> curbMapHighestValuesRemainChar(Map<Character, Integer> map,
			int curbCount) {
		LinkedList<Entry<Character, Integer>> sortedEntries = new LinkedList<Map.Entry<Character, Integer>>();
		sortedEntries.addAll(map.entrySet());
		Comparator<Entry<Character, Integer>> comparator = new Comparator<Entry<Character, Integer>>() {
			@Override
			public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
				return (-1)*(o1.getValue().compareTo(o2.getValue()));
			}
		};
		Collections.sort(sortedEntries, comparator);
		
		LinkedHashMap<Character, Integer> result = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < curbCount; i++) {
			final Entry<Character, Integer> e = sortedEntries.get(i);
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

	private LinkedHashMap<String, Integer> curbMapHighestValuesRemain(Map<String, Integer> map, int curbCount) {
		LinkedList<Entry<String, Integer>> sortedEntries = new LinkedList<Map.Entry<String, Integer>>();
		sortedEntries.addAll(map.entrySet());
		Comparator<Entry<String, Integer>> comparator = new Comparator<Entry<String, Integer>>() {
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return (-1)*(o1.getValue().compareTo(o2.getValue()));
			}
		};
		Collections.sort(sortedEntries, comparator);
		
		LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < curbCount; i++) {
			final Entry<String, Integer> e = sortedEntries.get(i);
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

	private static Integer countTotalTriplets(Map<String, Integer> newTripletOccurrences) {
		int sum = 0;
		for(Integer value: newTripletOccurrences.values()) {
			sum += value;
		}
		return sum;
	}

	private static Integer countTotalDoublets(Map<String, Integer> newDoubletOccurrences) {
		int sum = 0;
		for(Integer value: newDoubletOccurrences.values()) {
			sum += value;
		}
		return sum;
	}

	private static void analyze(String text, Map<Character, Integer> newCharacterOccurrences,
			Map<String, Integer> newDoubletOccurrences, Map<String, Integer> newTripletOccurrences) {
		if(text.length() == 0) {
			return;
		} else if(text.length() == 1) {
			newCharacterOccurrences.put(text.charAt(0), 1);
		} else if(text.length() == 2) {
			if(text.charAt(0) == text.charAt(1)) {
				newCharacterOccurrences.put(text.charAt(0), 2);
			} else {
				newCharacterOccurrences.put(text.charAt(0), 1);
				newCharacterOccurrences.put(text.charAt(1), 1);
			}
			newDoubletOccurrences.put(text, 1);
		} else {
			char currentChar;
			String currentDoublet;
			String currentTriplet;
			for(int i=0; i<text.length(); i++) {
				Integer currentCount;
				currentChar = text.charAt(i);
				currentCount = newCharacterOccurrences.get(currentChar);
				newCharacterOccurrences.put(currentChar, (currentCount==null)?1:currentCount+1);
				
				if(i<text.length()-1) {
					currentDoublet = text.substring(i, i+2);
					currentCount = newDoubletOccurrences.get(currentDoublet);
					newDoubletOccurrences.put(currentDoublet, (currentCount==null)?1:currentCount+1);
				}
				if(i<text.length()-2) {
					currentTriplet = text.substring(i, i+3);
					currentCount = newTripletOccurrences.get(currentTriplet);
					newTripletOccurrences.put(currentTriplet, (currentCount==null)?1:currentCount+1);
				}
			}
		}
	}

	public boolean dataFitsCipherAlphabet(AbstractAlphabet alphabet) {
		return dataFitMetric(alphabet) >= ALPHABETS_DATA_FITS_THRESHHOLD;
	}
	
	public double dataFitMetric(AbstractAlphabet alphabet) {
		List<Character> remoteCharacters = new LinkedList<Character>();
		for(char c: alphabet.getCharacterSet()) {
			remoteCharacters.add(c);
		}
		Set<Character> thisCharacters = getTextCharacters();
		
		return compareTwoAlphabets(remoteCharacters, thisCharacters);
	}

	public Set<Character> getTextCharacters() {
		return characterSet;
	}

	public static double compareTwoAlphabets(List<Character> remoteCharacters, Set<Character> thisCharacters) {
		Set<Character> disjunction = calcAlphaDisjunction(remoteCharacters, thisCharacters);
		Set<Character> conjunction = calcAlphaConjunction(remoteCharacters, thisCharacters);
		
		if(disjunction.size() == 0) return 0;
		return (double)(conjunction.size()) / (double)(disjunction.size());
	}

	private static Set<Character> calcAlphaConjunction(List<Character> remoteCharacters, Set<Character> thisCharacters) {
		Set<Character> conjunction = new HashSet<Character>();
		for(Character c: thisCharacters) {
			if(remoteCharacters.contains(c)) {
				conjunction.add(c);
			}
		}
		return conjunction;
	}

	private static Set<Character> calcAlphaDisjunction(List<Character> remoteCharacters, Set<Character> thisCharacters) {
		Set<Character> disjunction = new HashSet<Character>();
		disjunction.addAll(thisCharacters);
		disjunction.addAll(remoteCharacters);
		return disjunction;
	}
	
	public double getFrequencyForCharacter(Character c) {
		return getFrequencyFor(characterOccurrences, c, totalCharacterCount);
	}
	public double getFrequencyForDoublet(String doublet) {
		return getFrequencyFor(doubletOccurrences, doublet, totalDoubletCount);
	}
	public double getFrequencyForTriplet(String triplet) {
		return getFrequencyFor(tripletOccurrences, triplet, totalTripletCount);
	}
	public String toStringShort() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("character counts: %s\n", characterOccurrences.toString())); //$NON-NLS-1$
		sb.append(String.format("doublet counts: %s\n", doubletOccurrences.toString())); //$NON-NLS-1$
		sb.append(String.format("triplet counts: %s", tripletOccurrences.toString())); //$NON-NLS-1$
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return String.format("Statistics; chars: %s", characterOccurrences.toString()); //$NON-NLS-1$
	}
	
	public static TextStatistic getDummyStatistic() {
		return new TextStatistic("abcdefghijklmnopqrstuvwxyz"); //$NON-NLS-1$
	}

	public Map<Character, Integer> getCharacterOccurrences() {
		return characterOccurrences;
	}

	public Map<String, Integer> getDoubletOccurrences() {
		return doubletOccurrences;
	}

	public Map<String, Integer> getTripletOccurrences() {
		return tripletOccurrences;
	}
}
