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
package org.jcryptool.crypto.classic.xor.algorithm;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
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
public class XorCmd extends ClassicAlgorithmCmd {

	private char[] keyString;
	private char[] keyPathToFile;
	
	/* (non-Javadoc)
	 * @see org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd#keyToDataobjectFormat(java.lang.String)
	 */
	@Override
	protected char[] keyToDataobjectFormat(String key) {
		return XorAlgorithm.specification.keyInputStringToDataobjectFormat(key);
	}
	
	

	@SuppressWarnings("static-access")
	@Override
	protected void createKeyOptions(Options options) {
		OptionGroup keyGrp = new OptionGroup();
		keyGrp.addOption(OptionBuilder.withLongOpt("key").hasArg(true) //$NON-NLS-1$
				.withArgName("KEY").withDescription( //$NON-NLS-1$
						Messages.XorCmd_keyDetailsString).create(
						"k")); //$NON-NLS-1$
		keyGrp.addOption(OptionBuilder.withLongOpt("keyFile").hasArg(true) //$NON-NLS-1$
				.withArgName("KEYFILE").withDescription( //$NON-NLS-1$
						Messages.XorCmd_keyDetailsFilepath).create(
						"kFile")); //$NON-NLS-1$
		options.addOptionGroup(keyGrp);
	}



	@Override
	protected char[] handleKeyOption(CommandLine commandLine,
			StringBuilder parseResultOut) throws ParseException {
		
		if (commandLine.hasOption("k") && commandLine.hasOption("kFile")) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new ParseException(Messages.XorCmd_onlyOneKeyMsg);
		}
		
		if (commandLine.hasOption("k")) { //$NON-NLS-1$
			keyPathToFile = keyToDataobjectFormat(null);
			String key = commandLine.getOptionValue("k"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = getSpecification().getKeyVerificators();
			verifyKey(key, verificators);
			
			keyString = keyToDataobjectFormat(key);
			
		} else {
			keyString = keyToDataobjectFormat(null);
			if (commandLine.hasOption("kFile")) { //$NON-NLS-1$
				String key = commandLine.getOptionValue("kFile"); //$NON-NLS-1$
				//verificate the key
				
				List<KeyVerificator> verificators = XorAlgorithm.specification.getKeyFileVerificators();
				verifyKey(key, verificators);
				
				keyPathToFile = keyToDataobjectFormat(key);
				
			} else {
				throw new ParseException(org.jcryptool.crypto.classic.model.algorithm.Messages.ClassicAlgorithmCmd_specifyKeyMsg);
			}
		}
		
		return null;
	}



	/* (non-Javadoc)
	 * @see org.jcryptool.crypto.classic.model.algorithm.ClassicAlgorithmCmd#initializeAlgorithm(int, java.io.InputStream, org.jcryptool.core.operations.alphabets.AbstractAlphabet, char[], boolean)
	 */
	@Override
	protected AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter) {
		AbstractClassicAlgorithm algorithm = new XorAlgorithm();
		algorithm.init(cryptMode, inputStream, alphabet2, keyString, keyPathToFile, null);
		algorithm.setFilter(filter);
		
		return algorithm;
	}



	@Override
	protected ClassicAlgorithmSpecification getSpecification() {
		return XorAlgorithm.specification;
	}

}
