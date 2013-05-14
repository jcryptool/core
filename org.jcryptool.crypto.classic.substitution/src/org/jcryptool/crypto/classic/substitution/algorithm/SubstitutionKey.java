package org.jcryptool.crypto.classic.substitution.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class SubstitutionKey {

	private Map<Character, Character> substitutions;

	public SubstitutionKey(Map<Character, Character> substitutions) throws SubstitutionKeyValidityException {
		String validationResult = validateMapping(substitutions);
		if(validationResult != null) throw new SubstitutionKeyValidityException(validationResult);
		this.substitutions = substitutions;
	}

	public static Map<Character, Character> invertSubstitution(Map<Character, Character> substitutionMapping) {
		Map<Character, Character> result = new HashMap<Character, Character>();
		for(Entry<Character, Character> mapping: substitutionMapping.entrySet()) {
			result.put(mapping.getValue(), mapping.getKey());
		}
		
		return result;
	}
	
	public Character getSubstitutionFor(Character c) {
		return substitutions.get(c);
	}
	
	public Map<Character, Character> getSubstitutions() {
		return new HashMap<Character, Character>(substitutions);
	}
	
	/**
	 * Validates if a substitution substitutions is well-formed. <br />
	 * The requirements are: <br /> 
	 * - no character may be target of a substitutions more than once <br />
	 * - each character for which a substitutions exists, must exist as target of a substitutions
	 * <br /><br />
	 * 
	 * 
	 * @param substitutions the substitution
	 * @return the cause for a rejection, or null, if everything is OK
	 */
	public static String validateMapping(Map<Character, Character> substitutions) {
		List<Character> seenTargets = new LinkedList<Character>();
		for(Character key: substitutions.keySet()) {
			Character target = substitutions.get(key);
			if(target == null) {
				return String.format("For character %s no substitution is set (or null)", key);
			}
			if(! seenTargets.contains(target)) {
				seenTargets.add(target);
			} else {
				return "Character %s appeared as substitution target more than once.";
			}
		}
		
		List<Character> keySet = new LinkedList<Character>(substitutions.keySet());
		List<Character> valueSet = new LinkedList<Character>(substitutions.values());
		
		if(keySet.size() != valueSet.size()) {
			return "Each character for which a substitution exists, must exist as target of a substitution";
		}
		
		for(Character c: keySet) {
			if(! valueSet.contains(c)) {
				return "Each character for which a substitution exists, must exist as target of a substitution";			}
		}
		for(Character c: valueSet) {
			if(! keySet.contains(c)) {
				return "Each character for which a substitution exists, must exist as target of a substitution";
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return getSubstitutions().toString();
	}
	
	public String toString(AbstractAlphabet alphabet) {
		StringBuilder formatStringBuilder = new StringBuilder();
		String[] formatArgs = new String[alphabet.getCharacterSet().length*2];
		char[] characterSet = alphabet.getCharacterSet();
		formatStringBuilder.append("{");
		for (int i = 0; i < characterSet.length; i++) {
			char c = characterSet[i];
			if(i==0) {
				formatStringBuilder.append("%s=%s");
			} else {
				formatStringBuilder.append(", %s=%s");
			}
			formatArgs[i*2] = String.valueOf(c);
			formatArgs[i*2+1] = String.valueOf(getSubstitutionFor(c));
		}
		formatStringBuilder.append("}");
		
		return String.format(formatStringBuilder.toString(), formatArgs);
	}
	
}
