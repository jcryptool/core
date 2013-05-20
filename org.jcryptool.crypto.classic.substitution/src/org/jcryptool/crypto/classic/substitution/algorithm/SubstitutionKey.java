package org.jcryptool.crypto.classic.substitution.algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class SubstitutionKey {
	
	public static class PasswordToKeyMethod {
		
		private boolean passwordDirection;
		private boolean restDirection;
		private boolean passwordFirst;

		
		/**
		 * Creates a key creation method. 
		 * 
		 * @param passwordFirst whether the password should build the first, or the last part of the key
		 * @param passwordDirection whether the password should be filled in lexically (from left to right,
		 * regardless of passwordFirst), which corresponds to 'true', or antilexically.
		 * @param restDirection whether the rest of the alphabet should be filled up lexically (from left to right,
		 * regardless of passwordFirst), which corresponds to 'true', or antilexically.
		 */
		public PasswordToKeyMethod(boolean passwordFirst, boolean passwordDirection, boolean restDirection) {
			this.passwordFirst = passwordFirst;
			this.passwordDirection = passwordDirection;
			this.restDirection = restDirection;
		}

		public static Comparator<Character> comparatorForFillInDirection(boolean passwordDirection) {
			return genericComparator(passwordDirection);
		}
		
		private static Comparator<Character> genericComparator(boolean lexicalDirection) {
			final int lexicalDirectionMultiplicator = lexicalDirection?1:-1;
			return new Comparator<Character>() {
				@Override
				public int compare(Character o1, Character o2) {
					if(o1==null && o2==null) {
						return 0;
					} else if(o1==null && o2!=null) {
						return 1*lexicalDirectionMultiplicator;
					} else if(o1!=null && o2==null) {
						return -1*lexicalDirectionMultiplicator;
					} else {
						return o1.compareTo(o2)*lexicalDirectionMultiplicator;
					}
				}
			};
		}
		
		
		public static SubstitutionKey createKey(String password, AbstractAlphabet alphabet, boolean passwordDirection, boolean restDirection, boolean passwordFirst) {
			LinkedList<Character> substitutionList = fillSubstitutionListWithPassword(password, alphabet, passwordDirection,
					restDirection, passwordFirst);
			
			try {
				return new SubstitutionKey(substitutionList, alphabet);
			} catch (SubstitutionKeyValidityException e) {
				throw new RuntimeException("Unexpected: substitution key method should never fail.");
			}
		}

		public static LinkedList<Character> fillSubstitutionListWithPassword(String password, AbstractAlphabet alphabet,
				boolean passwordDirection, boolean restDirection, boolean passwordFirst) {
			LinkedList<Character> substitutionList = new LinkedList<Character>();
			LinkedHashSet<Character> filteredPasswordSet = new LinkedHashSet<Character>();
			for(char c: password.toCharArray()) {
				if(alphabet.contains(c)) filteredPasswordSet.add(c);
			}
			LinkedList<Character> filteredPassword = new LinkedList<Character>(filteredPasswordSet);
			
			LinkedList<Character> restList = new LinkedList<Character>();
			for(char c: alphabet.getCharacterSet()) {
				if(!filteredPassword.contains(Character.valueOf(c))) restList.add(c);
			}
			
			List<Character> listTofillIn = new LinkedList<Character>(
					passwordFirst?filteredPassword:restList
					);
			boolean fillInDirection = passwordFirst?passwordDirection:restDirection;
			if(!fillInDirection) Collections.reverse(listTofillIn);
			substitutionList.addAll(listTofillIn);

			
			listTofillIn = new LinkedList<Character>(
					passwordFirst?restList:filteredPassword
					);
			fillInDirection = passwordFirst?restDirection:passwordDirection;
			if(!fillInDirection) Collections.reverse(listTofillIn);
			substitutionList.addAll(listTofillIn);
			
			if(substitutionList.size() != alphabet.getCharacterSet().length) {
				throw new RuntimeException("Unexpected: substitutionKey creation: created substitution list length is not correct (should never happen)");
			}
			return substitutionList;
		}
		
		public SubstitutionKey createKey(String password, AbstractAlphabet alphabet) {
			return PasswordToKeyMethod.createKey(password, alphabet, passwordDirection, restDirection, passwordFirst);
		}
	}

	private Map<Character, Character> substitutions;

	public SubstitutionKey(Map<Character, Character> substitutions) throws SubstitutionKeyValidityException {
		String validationResult = validateMapping(substitutions);
		if(validationResult != null) throw new SubstitutionKeyValidityException(validationResult);
		this.substitutions = substitutions;
	}

	public SubstitutionKey(LinkedList<Character> substitutionList, AbstractAlphabet alphabet) throws SubstitutionKeyValidityException {
		this(mappingFromSubstitutionList(substitutionList, alphabet));
	}

	private static Map<Character, Character> mappingFromSubstitutionList(LinkedList<Character> substitutionList, AbstractAlphabet alphabet) {
		Map<Character, Character> mapping = new HashMap<Character, Character>();
		char[] characterSet = alphabet.getCharacterSet();
		for (int i = 0; i < characterSet.length; i++) {
			char c = characterSet[i];
			mapping.put(c, substitutionList.get(i));
		}
		return mapping;
	}

	public static Map<Character, Character> invertSubstitution(Map<Character, Character> substitutionMapping) {
		Map<Character, Character> result = new HashMap<Character, Character>();
		for(Entry<Character, Character> mapping: substitutionMapping.entrySet()) {
			result.put(mapping.getValue(), mapping.getKey());
		}
		
		return result;
	}
	
	public static SubstitutionKey createIdentitySubstitution(AbstractAlphabet alphabet) {
		return new PasswordToKeyMethod(true, true, true).createKey("", alphabet);
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
	
	/**
	 * Given a plaintext alphabet, this method returns the substitutions to the
	 * plaintext characters in the order in which they appear in the alphabet.<br><br>
	 * 
	 * If the alphabet contains characters that are not mapped by this substitution key, 
	 * they are filled up at the end of the String.
	 * 
	 * @param alphabet the plaintext alphabet
	 * @return
	 */
	public String toStringSubstitutionCharList(AbstractAlphabet alphabet) {
		StringBuffer keyIdeal = new StringBuffer();
		for(char c: alphabet.getCharacterSet()) {
			Character substitutionFor = this.getSubstitutionFor(c);
			if(substitutionFor != null) {
				keyIdeal.append(substitutionFor);
			}
		}
		//filling up...
		LinkedList<Character> substList = PasswordToKeyMethod.fillSubstitutionListWithPassword(
				keyIdeal.toString(), 
				alphabet, 
				true, 
				true, 
				true); 
		StringBuilder stringKeyBuilder = new StringBuilder();
		for(Character c: substList) {
			stringKeyBuilder.append(c);
		}
		return stringKeyBuilder.toString();
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
