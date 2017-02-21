// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.preferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;

/**
 * @author Simon Preference Set to convert a single TransformationData object to a saveable
 *         Preference Format
 */
public class TransformationPreferenceSet {

	// Will be the subnode's name
    public static final String ID_TRANSFORM_DATA = "TransformData"; //$NON-NLS-1$

    
    private static TransformData getDefaultByHeuristic(List<Character> alphabet) {
    	TransformData result = new TransformData();
    	
    	int casing = determineCasing(alphabet);
    	if(casing == 0) {
    		result.setUppercaseTransformationOn(false);
    	} else {
    		result.setUppercaseTransformationOn(true);
    		result.setDoUppercase(casing<0?false:true);
    	}
    	
    	result.setUmlautTransformationON(!determineUmlauts(alphabet));
    	result.setLeerTransformationON(!determineLeer(alphabet));
    	result.setAlphabetTransformationON(false);
    	
    	return result;
    }
    
    public static TransformData getDefaultByHeuristic(AbstractAlphabet alphabet) {
    	return getDefaultByHeuristic(AbstractAlphabet.alphaToList(alphabet));
    }
    
    private static boolean determineLeer(List<Character> alphabet) {
		char[] leer = Messages.getString("TransformationPreferenceSet_2").toCharArray(); //$NON-NLS-1$
		for(char c:leer) if(alphabet.contains(c)) return true;
		return false;
	}

	private static boolean determineUmlauts(List<Character> alphabet) {
		char[] umlauts = Messages.getString("TransformationPreferenceSet_3").toCharArray(); //$NON-NLS-1$
		for(char umlaut:umlauts) if(alphabet.contains(umlaut)) return true;
		return false;
	}

	/**
     * 0: upper and lowercase...
     * 
     * 
     * @param alphabet
     * @return
     */
    private static int determineCasing(List<Character> alphabet) {
		String latinAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //$NON-NLS-1$
		char[] uppercaseChars = latinAlpha.toCharArray(); 
		char[] lowercaseChars = latinAlpha.toLowerCase().toCharArray();
		int score = 0;
		int found = 0;
		for(char c: uppercaseChars) {
			if(alphabet.contains(c)) {
				score ++;
				found ++;
			}
		}
		for(char c: lowercaseChars) {
			if(alphabet.contains(c)) {
				score --;
				found ++;
			}
		}
		
		if(Math.abs(score) < found/4 && Math.abs(found-latinAlpha.length()) < 4) {
			return 0;
		}
		return (int) Math.signum(score);
	}

    private static Map<String, TransformData> standardSettings() {
    	HashMap<String, TransformData> result = new HashMap<String, TransformData>();
    	
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_ascii_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Printable ASCII"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					false, // upper/lowercase transformation activate flag
    					false, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					false  // umlaut transformation activate flag
    			)
    	);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_uplolatin_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Upper and lower Latin (A-Z,a-z)"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					false, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_uplatin_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Upper Latin (A-Z)"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_lowlatin_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Lower Latin (a-z)"), //$NON-NLS-1$
    					false, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_playfair_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Playfair/alike alphabet (25chars, w/o \"J\")"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_adfgvx_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("ADFGVX Alphabet"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_xor32_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Xor Alphabet with 32 characters"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_xor64_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Xor Alphabet with 64 characters"), //$NON-NLS-1$
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					false, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	
    	return result;
    	
    }
    
    public static boolean hasStandardSetting(String alphabetName) {
    	return standardSettings().containsKey(alphabetName);
    }
    
    public static TransformData getDefaultSetting(String alphabetName) {
    	if(standardSettings().containsKey(alphabetName)) {
    		return standardSettings().get(alphabetName);
    	}

        return new TransformData();
    }

}
