// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.adfgvx.algorithm;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * Provides command line functionality for the ADFGVX algorithm.
 *
 * @author SLeischnig
 * @version 0.6.0
 */
public class AdfgvxCmd extends ClassicAlgorithmCmd {

	
	char[] substitutionKeyForDataobject = null;
	char[] transpositionKeyForDataobject = null;
	
	@SuppressWarnings("static-access")
	@Override
	protected void createKeyOptions(Options options) {
		options.addOption(OptionBuilder.withLongOpt("keySubstitution").hasArg(true) //$NON-NLS-1$
				.withArgName("SUBSTITUTION_KEY").isRequired().withDescription( //$NON-NLS-1$
						Messages.AdfgvxCmd_substKeyDescription).create(
						"kS")); //$NON-NLS-1$
		options.addOption(OptionBuilder.withLongOpt("keyTransposition").hasArg(true) //$NON-NLS-1$
				.withArgName("TRANSPOSITION_KEY").isRequired().withDescription( //$NON-NLS-1$
						Messages.AdfgvxCmd_transpKeyDescription).create(
						"kT")); //$NON-NLS-1$
	}


	@Override
	protected char[] handleKeyOption(CommandLine commandLine, StringBuilder parseResultOut)
			throws ParseException {
		
		//returns null -- but writes these two global variables.
		substitutionKeyForDataobject = null;
		transpositionKeyForDataobject = null;
		
		
		
		if (commandLine.hasOption("kS")) { //$NON-NLS-1$
			String key = commandLine.getOptionValue("kS"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = AdfgvxAlgorithm.specification.getKeyVerificatorsSubstitutionKey();
			verifyKey(key, verificators);
			
			this.substitutionKeyForDataobject = AdfgvxAlgorithm.specification.substitutionKeyInputStringToDataobjectFormat(key);
		} else {
			throw new ParseException(Messages.AdfgvxCmd_specifySubstKey);
		}
		
		if (commandLine.hasOption("kT")) { //$NON-NLS-1$
			String key = commandLine.getOptionValue("kT"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = AdfgvxAlgorithm.specification.getKeyVerificatorsTranspositionKey();
			verifyKey(key, verificators);
			
			this.transpositionKeyForDataobject = AdfgvxAlgorithm.specification.transpositionKeyInputStringToDataobjectFormat(key);
		} else {
			throw new ParseException(Messages.AdfgvxCmd_specifyTranspKey);
		}
		
		return null;
	}



	//does nothing -- key options are different since there are two keys
	//DO NOT USE
	@Override
	protected char[] keyToDataobjectFormat(String key) {
		LogUtil.logWarning("Method keyToDataobjectFormat(String) in AdfgvxCmd is not intended to be used."); //$NON-NLS-1$
		return null;
	}
	
	

	
	
	@Override
	protected AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter) {
		AbstractClassicAlgorithm algorithm = new AdfgvxAlgorithm();
		algorithm.init(cryptMode, inputStream, alphabet2, this.substitutionKeyForDataobject, this.transpositionKeyForDataobject, null);
		algorithm.setFilter(filter);
		
		return algorithm;
	}

	@Override
	protected ClassicAlgorithmSpecification getSpecification() {
		return AdfgvxAlgorithm.specification;
	}
	
}
