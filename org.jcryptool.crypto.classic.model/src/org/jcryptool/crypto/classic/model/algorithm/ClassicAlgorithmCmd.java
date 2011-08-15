//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2008 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.algorithm;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.osgi.util.NLS;
import org.jcryptool.commands.core.api.AbstractCommand;
import org.jcryptool.commands.core.api.IllegalCommandException;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.dataobject.classic.ClassicDataObject;
import org.jcryptool.core.operations.keys.KeyVerificator;
import org.jcryptool.core.util.input.InputVerificationResult;

/**
 * This class provides a template for creating a console command for a classic algorithm.
 * This template assumes that the classic algorithm has a specification (see {@link ClassicAlgorithmSpecification})
 * which can be set by overriding the getter method {@link ClassicAlgorithmCmd#getSpecification()} (returns a
 * default specification if not overridden which doesn't really restrict uses of alphabets, key and others.) 
 *
 * @author SLeischnig
 *
 */
public abstract class ClassicAlgorithmCmd extends AbstractCommand {
	
	private static final String MODEENCRYPTION_LONG_OPTIONNAME = "modeEncrypt";
	private static final String MODEDECRYPTION_LONG_OPTIONNAME = "modeDecrypt";
	private static final String NOFILTER_LONG_OPTIONNAME = "noFilter";
	private static final String NOFILTER_OPTIONNAME = "noFi";
	private static final String ALPHABET_LONG_OPTIONNAME = "currentAlphabet";
	private static final String ALPHABET_OPTION_NAME = "a";
	private static final String KEY_ARGUMENT1_NAME = "KEY";
	private static final String KEY_LONG_OPTIONNAME = "key";
	private static final String KEY_OPTIONNAME = "k";
	private static final String MODEDECRYPTION_OPTIONNAME = "D";
	private static final String MODEENCRYPTION_OPTIONNAME = "E";

	private StringBuilder result = null;
	
	protected AbstractAlphabet currentAlphabet = null;
	
	public ClassicAlgorithmCmd() {
	}

	protected ClassicAlgorithmSpecification getSpecification() {
		return new ClassicAlgorithmSpecification() {
			
		};
	}

	/**
	 * creates the commandline options for the key selection. creates one key input option. if you need more keys, or better overall control, override this.
	 * 
	 * @param options the options object which contains all commandline options. use the methods of this object in order to create the key options.
	 */
	@SuppressWarnings("static-access")
	protected void createKeyOptions(Options options) {
		options.addOption(OptionBuilder.withLongOpt(KEY_LONG_OPTIONNAME).hasArg(true) //$NON-NLS-1$
				.withArgName(KEY_ARGUMENT1_NAME).isRequired().withDescription( //$NON-NLS-1$
						getKeyDetailString()).create(
						KEY_OPTIONNAME)); //$NON-NLS-1$
	}

	/**
	 * creates the commandline options for the currentAlphabet selection. Does only create options if more than one currentAlphabet is available.
	 * Override if you need better control over the options.
	 * 
	 * @param options the options object which contains all commandline options. use the methods of this object in order to create the currentAlphabet options.
	 */
	@SuppressWarnings("static-access")
	protected void createAlphabetOptions(Options options) {
		List<AbstractAlphabet> availableAlphabets = getSpecification().getAvailablePlainTextAlphabets();
		if(availableAlphabets.size() > 1) {
			String availableAlphabetsString = createAvailabeAlphabetsString(availableAlphabets);
			AbstractAlphabet defaultAlphabet = getSpecification().getDefaultPlainTextAlphabet();
			
			options.addOption(OptionBuilder.withLongOpt(ALPHABET_LONG_OPTIONNAME).hasArg(true) //$NON-NLS-1$
					.withArgName("ALPHABET").withDescription( //$NON-NLS-1$
							NLS.bind(Messages.ClassicAlgorithmCmd_alphabetsDetails, new Object[] {availableAlphabetsString, defaultAlphabet.getShortName()}))
					.create(ALPHABET_OPTION_NAME)); //$NON-NLS-1$
		}			
		
	}

	/**
	 * subclasses may contribute options here (Does by default nothing)
	 * 
	 * @param options
	 */
	protected void createOtherOptions(Options options) {
	}

	/**
	 * The string which is displayed as info next to the key parameter in the
	 * automatically generated argument list.
	 * 
	 * @return
	 */
	protected String getKeyDetailString() {
		return Messages.ClassicAlgorithmCmd_keyDetails;
	}

	/**
	 * Verifies a String key against a set of verificators, throwing a parse exception
	 * with the reason from the verification result as massage.
	 * 
	 * @param key the key
	 * @param verificators the collection of verificators
	 * @return
	 * @throws ParseException when the verification as not successful.
	 */
	protected InputVerificationResult verifyKey(String key, Collection<KeyVerificator> verificators) throws ParseException {
		InputVerificationResult verificationResult = KeyVerificator.verify(key, currentAlphabet, verificators);
		
		if(! verificationResult.isValid()) {
			if(verificationResult.isStandaloneMessage()) {
				throw new ParseException(verificationResult.getMessage());
			} else {
				String mask = Messages.ClassicAlgorithmCmd_notwellformedMsg;
				throw new ParseException(String.format(mask, verificationResult.getMessage()));
			}
		}
		
		return verificationResult;
	}
	
	/**
	 * converts the read-in key to the dataobject-compatible format. The String argument is already
	 * validated against the key validators from the algorithm classification object.
	 * For things that might be needed forcalculating the key representation for the dataobject (like an currentAlphabet e. g.),
	 * those parameters have to be saved in global variables. This method is the last method that parses the command line,
	 * so the other parameters are already read. for example, the currentAlphabet is stored in #currentAlphabet.
	 * 
	 * @param key the key string from the command line (already validated)
	 * @return the dataobject representation of the key
	 */
	protected abstract char[] keyToDataobjectFormat(String key);

	/**
	 * Reads the key from the command line, verifies it against the key verificators
	 * from the specification and invokes the abstract method which is intended to return the 
	 * dataobject-formatted key.
	 * 
	 * @param commandLine
	 * @param parseResultOut 
	 * @return
	 * @throws ParseException
	 */
	protected char[] handleKeyOption(CommandLine commandLine, StringBuilder parseResultOut) throws ParseException {
		if (commandLine.hasOption(KEY_OPTIONNAME)) { //$NON-NLS-1$
			String key = commandLine.getOptionValue(KEY_OPTIONNAME); //$NON-NLS-1$
			//verificate the key
			
			List<KeyVerificator> verificators = getSpecification().getKeyVerificators();
			verifyKey(key, verificators);
			
			return keyToDataobjectFormat(key);
			
		} else {
			throw new ParseException(Messages.ClassicAlgorithmCmd_specifyKeyMsg);
		}
	}
	
	
	protected AbstractAlphabet handleAlphabetOption(CommandLine commandLine) throws ParseException {
		if(getSpecification().getAvailablePlainTextAlphabets().size() > 1) {
			if (commandLine.hasOption(ALPHABET_OPTION_NAME)) { //$NON-NLS-1$
				String alphabetName = commandLine.getOptionValue(ALPHABET_OPTION_NAME); //$NON-NLS-1$
				AbstractAlphabet result = AlphabetsManager.getInstance().getAlphabetByShortName(alphabetName);
				if(result != null) {
					if(getSpecification().isValidPlainTextAlphabet(result) && getSpecification().isValidAlphabetCombination(result, result)) {
						return result;
					} else {
						String mask = Messages.ClassicAlgorithmCmd_alphabetnotsupportedMsg;
						throw new ParseException(String.format(mask, alphabetName, createAvailabeAlphabetsString(getSpecification().getAvailablePlainTextAlphabets())));
					}
				} else {
					String mask = Messages.ClassicAlgorithmCmd_alphabetnotfoundMsg;
					throw new ParseException(String.format(mask, alphabetName));
				}
			} else {
				return getSpecification().getDefaultPlainTextAlphabet();
			}
		} else {
			return getSpecification().getDefaultPlainTextAlphabet();
		}
	}

	/**
	 * handles options contributed by subclasses. (Does by default nothing)
	 * 
	 * @param commandLine
	 * @param parseResultOut 
	 * @throws ParseException 
	 */
	protected void handleOtherOptions(CommandLine commandLine, StringBuilder parseResultOut) throws ParseException {
	}

	/**
	 * returns a new initialized algorithm object, ready to execute.
	 * 
	 * @param cryptMode {@link AbstractAlgorithm#ENCRYPT_MODE}, {@link AbstractAlgorithm#DECRYPT_MODE}
	 * @param inputStream the input text
	 * @param alphabet2 the selected currentAlphabet
	 * @param key the key (in dataobject format)
	 * @param filter whether to filter non-currentAlphabet-chars or not.
	 */
	protected abstract AbstractClassicAlgorithm initializeAlgorithm(int cryptMode,
			InputStream inputStream, AbstractAlphabet alphabet2, char[] key,
			boolean filter);

	@SuppressWarnings("static-access")
	public Options createOptions() {
		Options options = new Options();
		
		// key input options
		createKeyOptions(options);
		// Alphabet input options
		createAlphabetOptions(options);
		// Option not to filter nonalphabetic characters (if option is set, filtering does NOT occur) -> filtering by default. 
		options.addOption(OptionBuilder.withLongOpt(NOFILTER_LONG_OPTIONNAME).hasArg( //$NON-NLS-1$
				false).withDescription(Messages.ClassicAlgorithmCmd_filteroption).create(
				NOFILTER_OPTIONNAME)); //$NON-NLS-1$
	
		// Encoding or decoding options group:
		
		OptionGroup codingMode = new OptionGroup();
		codingMode.addOption(OptionBuilder.withLongOpt(MODEENCRYPTION_LONG_OPTIONNAME).hasArg( //$NON-NLS-1$
				false).withDescription(Messages.ClassicAlgorithmCmd_encryptMode).create(MODEENCRYPTION_OPTIONNAME)); //$NON-NLS-1$
		codingMode.addOption(OptionBuilder.withLongOpt(MODEDECRYPTION_LONG_OPTIONNAME).hasArg( //$NON-NLS-1$
				false).withDescription(Messages.ClassicAlgorithmCmd_decryptMode).create(MODEDECRYPTION_OPTIONNAME)); //$NON-NLS-1$
		codingMode.setRequired(true);
		options.addOptionGroup(codingMode);
	
		// Text input option group
		
		createInputOptions(options);
		
		// other options contributed by subclasses
		
		createOtherOptions(options);
	
		return options;
	}

	public void execute(CommandLine commandLine) throws IllegalCommandException {
		/**
		 * Order of argument reading:
		 * 		text
		 * 		currentAlphabet
		 * 		something
		 * 		key (last!)
		 */
		super.execute(commandLine);
		
		currentAlphabet = null;
		
		result = new StringBuilder();

		// read text 
		
		try {
			
			InputStream inputStream = null;
			try {
				inputStream = handleInputOption(commandLine);
			} catch (FileNotFoundException e) {
				result.append(e.getLocalizedMessage());
				return;
			}
			
			// read currentAlphabet 
			
			AbstractAlphabet alphabet = handleAlphabetOption(commandLine);
			this.currentAlphabet = alphabet;
	
			// read other options
			
			int cryptMode = -1;
			if (commandLine.hasOption(MODEDECRYPTION_OPTIONNAME)) { //$NON-NLS-1$
				cryptMode = AbstractAlgorithm.DECRYPT_MODE;
			} else {
				cryptMode = AbstractAlgorithm.ENCRYPT_MODE;
			}
	
			boolean filter = ! commandLine.hasOption(NOFILTER_OPTIONNAME); //$NON-NLS-1$
	
			
			// handle options contributed by subclasses
			
			handleOtherOptions(commandLine, result);			
			
			// read the key
			
			char[] key = handleKeyOption(commandLine, result);
			
			// initialize the algorithm
			AbstractClassicAlgorithm algorithm = initializeAlgorithm(cryptMode, inputStream, alphabet, key, filter);
			
			//execute
			
			ClassicDataObject dataObject = (ClassicDataObject) algorithm.execute();
	
			
			
			//finish
			
			if (!commandLine.getArgList().isEmpty()) {
				result.append(Messages.ClassicAlgorithmCmd_ignoredargs);
				result.append(commandLine.getArgList());
				result.append("\n\n"); //$NON-NLS-1$
			}
			
			
			result.append(dataObject.getOutputStream().toString());
			
		} catch (ParseException e) {
			result.append(Messages.ClassicAlgorithmCmd_error + e.getMessage());
		}
		
	}


	public String getResult() {
		return result.toString();
	}

	public static String createAvailabeAlphabetsString(List<AbstractAlphabet> alphas) {
		StringBuilder builder = new StringBuilder();
	
		for (AbstractAlphabet alpha : alphas) {
			builder.append(alpha.getShortName());
			builder.append(", "); //$NON-NLS-1$
		}
		builder.delete(builder.length() - 1, builder.length());
		return builder.toString();
	}
	
}
