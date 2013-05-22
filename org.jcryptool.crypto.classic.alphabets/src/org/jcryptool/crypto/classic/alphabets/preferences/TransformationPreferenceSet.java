// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.preferences;

import java.util.HashMap;
import java.util.Map;

import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;

/**
 * @author Simon Preference Set to convert a single TransformationData object to a saveable
 *         Preference Format
 */
public class TransformationPreferenceSet {
    private static final String ALPHA_DATA_SEPARATOR = "{alpha-data-separator}";

	// Will be the subnode's name
    public static final String ID_TRANSFORM_DATA = "TransformData";


    private static Map<String, TransformData> standardSettings() {
    	HashMap<String, TransformData> result = new HashMap<String, TransformData>();
    	
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_ascii_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Printable ASCII"),
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					false, // upper/lowercase transformation activate flag
    					false, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					false  // umlaut transformation activate flag
    			)
    	);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_uplolatin_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Upper and lower Latin (A-Z,a-z)"),
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					false, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_uplatin_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Upper Latin (A-Z)"),
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_lowlatin_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Lower Latin (a-z)"),
    					false, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_playfair_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Playfair/alike alphabet (25chars, w/o \"J\")"),
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_adfgvx_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("ADFGVX Alphabet"),
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_xor32_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Xor Alphabet with 32 characters"),
    					true, //(if "UPPERCASE" or "LOWERCASE" is selected as standard, if this transformation is activated) 
    					true, // upper/lowercase transformation activate flag
    					true, // delete-blanks-transformation activate flag
    					true, // alphabet transformation activate flag
    					true  // umlaut transformation activate flag
    					)
    			);
    	result.put(org.jcryptool.crypto.classic.alphabets.tools.Messages.AlphabetStore_alpha_xor64_long,
    			new TransformData(
    					AlphabetsManager.getInstance().getAlphabetByName("Xor Alphabet with 64 characters"),
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
