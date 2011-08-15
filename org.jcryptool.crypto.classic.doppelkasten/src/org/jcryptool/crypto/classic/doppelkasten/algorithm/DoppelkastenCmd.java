//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.doppelkasten.algorithm;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd;
import org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmSpecification;

/**
 * 
 * 
 * @author Simon L
 */
public class DoppelkastenCmd extends ClassicAlgorithmCmd {

	
	
	@SuppressWarnings("static-access")
	@Override
	protected void createKeyOptions(Options options) {
		options.addOption(OptionBuilder.withLongOpt("key").hasArg(true) //$NON-NLS-1$
				.withArgName("KEY").isRequired().withDescription( //$NON-NLS-1$
						org.jcryptool.crypto.classic.doppelkasten.algorithm.Messages.DoppelkastenCmd_firstKeyDetails).create(
						"k")); //$NON-NLS-1$
		options.addOption(OptionBuilder.withLongOpt("key2").hasArg(true) //$NON-NLS-1$
				.withArgName("KEY").isRequired().withDescription( //$NON-NLS-1$
						org.jcryptool.crypto.classic.doppelkasten.algorithm.Messages.DoppelkastenCmd_secondKeyDetails).create(
						"k2")); //$NON-NLS-1$
	}

	@Override
	protected char[] handleKeyOption(CommandLine commandLine,
			StringBuilder parseResultOut) throws ParseException {
		String key1 = null;
		String key2 = null;
		if (commandLine.hasOption("k")) { //$NON-NLS-1$
			String key = commandLine.getOptionValue("k"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = getSpecification().getKeyVerificators();
			verifyKey(key, verificators);
			
			key1 = key;
			
		} else {
			throw new ParseException(org.jcryptool.crypto.classic.doppelkasten.algorithm.Messages.DoppelkastenCmd_specifyKeys);
		}
		
		if (commandLine.hasOption("k2")) { //$NON-NLS-1$
			String key = commandLine.getOptionValue("k2"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = getSpecification().getKeyVerificators();
			verifyKey(key, verificators);
			
			key2 = key;
			
		} else {
			throw new ParseException(org.jcryptool.crypto.classic.doppelkasten.algorithm.Messages.DoppelkastenCmd_specifyKeys);
		}
		
		return DoppelkastenAlgorithm.specification.keyInputStringToDataobjectFormat(key1, key2);
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd#initializeAlgorithm(int, java.io.InputStream, org.jcryptool.core.operations.alphabets.AbstractAlphabet, char[], boolean)
	 */
	@Override
	protected AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter) {
		DoppelkastenAlgorithm algo = new DoppelkastenAlgorithm();
		algo.init(cryptMode, inputStream, alphabet2, key, null);
		return algo;
	}

	@Override
	protected ClassicAlgorithmSpecification getSpecification() {
		return DoppelkastenAlgorithm.specification;
	}

	@Override
	protected char[] keyToDataobjectFormat(String key) {
		//not to be used
		return null;
	}
	
	

}
