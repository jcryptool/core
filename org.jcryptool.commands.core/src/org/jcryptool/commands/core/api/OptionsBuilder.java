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
package org.jcryptool.commands.core.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class OptionsBuilder {
	private List<Option> options = new ArrayList<Option>();
	private List<String[]> goups = new ArrayList<String[]>();

	public OptionsBuilder() {
		super();
	}

	public OptionsBuilder addRequiredOption(String longOption,
			String shortOption, String description) {
		return this.addOption(longOption, shortOption, true, description, null, false, ',', 0);
	}

	public OptionsBuilder addOptionalOption(String longOption,
			String shortOption, String description) {
		return this.addOption(longOption, shortOption, false, description, null, false, ',', 0);
	}

	public OptionsBuilder addOptionWithRequiredSingleArg(String longOption,
			String shortOption, String description, boolean required, String argName){
		return this.addOption(longOption, shortOption, required, description, argName, true, ',', 1);
	}

	public OptionsBuilder addOption(String longOption,
			String shortOption, boolean required, String description, String argName,
			boolean argRequired, char argValueSeparator, int argsCount) {
		Option option = new Option(shortOption, longOption, true, description);
		option.setArgName(argName);
		option.setRequired(required);
		option.setOptionalArg(argRequired);
		option.setValueSeparator(argValueSeparator);
		option.setArgs(argsCount);
		options.add(option);
		return this;
	}

	public OptionsBuilder group(String... options){
		this.goups.add(options);
		return this;
	}

	public Options create() {
		return null;
	}
}
