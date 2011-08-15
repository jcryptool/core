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
package org.jcryptool.crypto.classic.transposition.algorithm;

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
 * Provides command line functionality for the transposition algorithm.
 *
 * @author SLeischnig
 * @version 0.6.0
 */
public class TranspositionAlgorithmCmd extends ClassicAlgorithmCmd {

	
	private char[] firstTranspositionKey;
	private char[] secondTranspositionKey;
	
	private boolean readInOrder1;
	private boolean readOutOrder1;
	private boolean readInOrder2;
	private boolean readOutOrder2;


	private String getKey2DetailString() {
		return Messages.TranspositionAlgorithmCmd_key2details;
	}


	@SuppressWarnings("static-access")
	@Override
	protected void createKeyOptions(Options options) {
		options.addOption(OptionBuilder.withLongOpt("key").hasArg(true) //$NON-NLS-1$
				.withArgName("KEY").isRequired().withDescription( //$NON-NLS-1$
						getKeyDetailString()).create(
						"k")); //$NON-NLS-1$
		
		options.addOption(OptionBuilder.withLongOpt("key2").hasArg(true) //$NON-NLS-1$
				.withArgName("KEY").withDescription( //$NON-NLS-1$
						getKey2DetailString()).create(
						"k2")); //$NON-NLS-1$
	}


	@Override
	protected char[] handleKeyOption(CommandLine commandLine, StringBuilder parseResultOut)
			throws ParseException {
		//returns null -- but writes these global variables.
		this.firstTranspositionKey = null;
		this.secondTranspositionKey = null;
		
		//first key
		
		if (commandLine.hasOption("k")) { //$NON-NLS-1$
			String key = commandLine.getOptionValue("k"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = TranspositionAlgorithm.specification.getKeyVerificators();
			verifyKey(key, verificators);
			
			this.firstTranspositionKey = TranspositionAlgorithm.specification.keyInputStringToDataobjectFormat(key, this.currentAlphabet, this.readInOrder1, this.readOutOrder1);
		} else {
			throw new ParseException(org.jcryptool.crypto.classic.model.algorithm.Messages.ClassicAlgorithmCmd_specifyKeyMsg);
		}
		
		//second key
		
		if (commandLine.hasOption("k2")) { //$NON-NLS-1$
			String key = commandLine.getOptionValue("k2"); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = TranspositionAlgorithm.specification.getKeyVerificators();
			verifyKey(key, verificators);
			
			this.secondTranspositionKey = TranspositionAlgorithm.specification.keyInputStringToDataobjectFormat(key, this.currentAlphabet, this.readInOrder2, this.readOutOrder2);
		} else {
			this.secondTranspositionKey = TranspositionAlgorithm.specification.keyInputStringToDataobjectFormat(String.valueOf(this.currentAlphabet.getCharacterSet()[0]), this.currentAlphabet, this.readInOrder2, this.readOutOrder2);
		}
		
		return null;
	}


	
	

	@SuppressWarnings("static-access")
	@Override
	protected void createOtherOptions(Options options) {
		String readinDetails = Messages.TranspositionAlgorithmCmd_readInOrderDetails;
		//String readoutDetails = Messages.TranspositionAlgorithmCmd_readOutOrderDetails;
		
		String forT1 = Messages.TranspositionAlgorithmCmd_appliesFor1st;
//		String forT2 = Messages.TranspositionAlgorithmCmd_appliesFor2nd;
		
		String seeAbove = Messages.TranspositionAlgorithmCmd_seeabove;
		
		options.addOption(OptionBuilder.withLongOpt("transposition1ReadInOrder").hasArg(true) //$NON-NLS-1$
				.withArgName("ORDER = 'cw'/'rw'").withDescription( //$NON-NLS-1$
						readinDetails + Messages.TranspositionAlgorithmCmd_rowwise_sentence_end + forT1).create(
						"t1ReadIn")); //$NON-NLS-1$
		options.addOption(OptionBuilder.withLongOpt("transposition1ReadOutOrder").hasArg(true) //$NON-NLS-1$
				.withArgName("ORDER = 'cw'/'rw'").withDescription( //$NON-NLS-1$
						//readoutDetails + "column-wise). " + forT1).create(
						seeAbove + Messages.TranspositionAlgorithmCmd_columnwise_sentence_end).create(
						"t1ReadOut")); //$NON-NLS-1$
		options.addOption(OptionBuilder.withLongOpt("transposition2ReadInOrder").hasArg(true) //$NON-NLS-1$
				.withArgName("ORDER = 'cw'/'rw'").withDescription( //$NON-NLS-1$
						//readinDetails + "row-wise). " + forT2).create(
						seeAbove + Messages.TranspositionAlgorithmCmd_rowwise_sentence_end).create(
						"t2ReadIn")); //$NON-NLS-1$
		options.addOption(OptionBuilder.withLongOpt("transposition2ReadOutOrder").hasArg(true) //$NON-NLS-1$
				.withArgName("ORDER = 'cw'/'rw'").withDescription( //$NON-NLS-1$
						//readoutDetails + "column-wise). " + forT2).create(
						seeAbove + Messages.TranspositionAlgorithmCmd_columnwise_sentence_end).create(
						"t2ReadOut")); //$NON-NLS-1$
		
	}


	@Override
	protected void handleOtherOptions(CommandLine commandLine, StringBuilder parseResultOut) throws ParseException {
		boolean t1ReadInDefault = TranspositionTable.DIR_ROWWISE;
		boolean t1ReadOutDefault = TranspositionTable.DIR_COLUMNWISE;
		boolean t2ReadInDefault = TranspositionTable.DIR_ROWWISE;
		boolean t2ReadOutDefault = TranspositionTable.DIR_COLUMNWISE;
		
		if((commandLine.hasOption("t2ReadIn") || commandLine.hasOption("t2ReadOut")) && ! commandLine.hasOption("k2")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			parseResultOut.append(Messages.TranspositionAlgorithmCmd_noneedfor2ndkeyorders);
		}
		
		
		if(commandLine.hasOption("t1ReadIn")) { //$NON-NLS-1$
			if(commandLine.getOptionValue("t1ReadIn").equals("rw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readInOrder1 = TranspositionTable.DIR_ROWWISE;
			} else if(commandLine.getOptionValue("t1ReadIn").equals("cw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readInOrder1 = TranspositionTable.DIR_COLUMNWISE;
			} else {
				throw new ParseException(Messages.TranspositionAlgorithmCmd_notValidOrderOption);
			}
			
		} else readInOrder1 = t1ReadInDefault;
		
		if(commandLine.hasOption("t1ReadOut")) { //$NON-NLS-1$
			if(commandLine.getOptionValue("t1ReadOut").equals("rw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readOutOrder1 = TranspositionTable.DIR_ROWWISE;
			} else if(commandLine.getOptionValue("t1ReadOut").equals("cw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readOutOrder1 = TranspositionTable.DIR_COLUMNWISE;
			} else {
				throw new ParseException(Messages.TranspositionAlgorithmCmd_notValidOrderOption);
			}
			
		} else readOutOrder1 = t1ReadOutDefault;
		
		if(commandLine.hasOption("t2ReadIn")) { //$NON-NLS-1$
			if(commandLine.getOptionValue("t2ReadIn").equals("rw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readInOrder2 = TranspositionTable.DIR_ROWWISE;
			} else if(commandLine.getOptionValue("t2ReadIn").equals("cw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readInOrder2 = TranspositionTable.DIR_COLUMNWISE;
			} else {
				throw new ParseException(Messages.TranspositionAlgorithmCmd_notValidOrderOption);
			}
			
		} else readInOrder2 = t2ReadInDefault;
		
		if(commandLine.hasOption("t2ReadOut")) { //$NON-NLS-1$
			if(commandLine.getOptionValue("t2ReadOut").equals("rw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readOutOrder2 = TranspositionTable.DIR_ROWWISE;
			} else if(commandLine.getOptionValue("t2ReadOut").equals("cw")) { //$NON-NLS-1$ //$NON-NLS-2$
				readOutOrder2 = TranspositionTable.DIR_COLUMNWISE;
			} else {
				throw new ParseException(Messages.TranspositionAlgorithmCmd_notValidOrderOption);
			}
			
		} else readOutOrder2 = t2ReadOutDefault;
		
	}


	//does nothing -- key options are different since there are two keys and miscellanous other options
	//DO NOT USE
	@Override
	protected char[] keyToDataobjectFormat(String key) {
		LogUtil.logWarning("Method keyToDataobjectFormat(String) in TranspositionCmd is not intended to be used."); //$NON-NLS-1$
		return null;
	}
	
	

	
	
	@Override
	protected AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter) {
		AbstractClassicAlgorithm algorithm = new TranspositionAlgorithm();
		algorithm.init(cryptMode, inputStream, alphabet2, this.firstTranspositionKey, this.secondTranspositionKey, null);
		algorithm.setFilter(filter);
		
		return algorithm;
	}

	@Override
	protected ClassicAlgorithmSpecification getSpecification() {
		return TranspositionAlgorithm.specification;
	}
	
}
