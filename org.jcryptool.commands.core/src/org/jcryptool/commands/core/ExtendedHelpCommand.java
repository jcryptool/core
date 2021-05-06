//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011, 2021 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.core;

import java.util.List;

/**
 * Provides additional help features to a command, like examples and aliases
 * 
 * @author Simon L
 */
public interface ExtendedHelpCommand extends Command {
	
	public static final String EXAMPLES_NODE_NAME = "example"; //$NON-NLS-1$
	public static final String EXAMPLES_SELECTOR_CMDLINE = "commandline"; //$NON-NLS-1$
	public static final String EXAMPLES_SELECTOR_EXPLANATION = "explanation"; //$NON-NLS-1$
	public static final String ALIAS_SELECTOR_ALIAS = "alias"; //$NON-NLS-1$
	public static final String ALIASES_NODE_NAME = "alias"; //$NON-NLS-1$
	
	/**
	 * Example for usage of a command (simple store and read dataobject)
	 * 
	 * @author Simon L
	 */
	public class Example {
		public String exampleCmdLine;
		public String explanation;
		public Example(String exampleCmdLine, String explanation) {
			this.exampleCmdLine = exampleCmdLine;
			this.explanation = explanation;
		}
	}
	
	/**
	 * Provides ALL synonyms (aliases) for this command.
	 * 
	 * @return
	 */
	public List<String> getAliases();
	
	/**
	 * Provides Examples for usage of the command
	 * 
	 * @return
	 */
	public List<Example> getExamples();
	
	/**
	 * @return whether this command is a secondary command, which means, that it was created to handle the alias of an original command.
	 */
	public boolean isAliasCommand();

	/**
	 * @return the command name that is not an alias
	 */
	public String getOriginalCommandName();
	
	
}
