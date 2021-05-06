// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.commands.core;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 *
 *
 * @author Simon L
 */
public class ProxiedExtendedCommand extends ProxiedCommand implements ExtendedHelpCommand {

    private boolean isProxiedCommand;

    ProxiedExtendedCommand(IConfigurationElement configurationElement, boolean isProxiedCommand) {
        super(configurationElement);
        this.isProxiedCommand = isProxiedCommand;
    }

    ProxiedExtendedCommand(IConfigurationElement configurationElement) {
        this(configurationElement, false);
    }

    public List<String> getAliases() {
        IConfigurationElement[] aliasNodes = configurationElement.getChildren(ALIASES_NODE_NAME);
        List<String> result = new LinkedList<String>();
        for (IConfigurationElement exampleNode : aliasNodes) {
            result.add(exampleNode.getAttribute(ALIAS_SELECTOR_ALIAS));
        }
        return result;
    }

    public List<Example> getExamples() {
        IConfigurationElement[] exampleNodes = configurationElement.getChildren(ExtendedHelpCommand.EXAMPLES_NODE_NAME);
        List<Example> result = new LinkedList<Example>();
        for (IConfigurationElement exampleNode : exampleNodes) {
            result.add(new Example(exampleNode.getAttribute(EXAMPLES_SELECTOR_CMDLINE),
                    exampleNode.getAttribute(EXAMPLES_SELECTOR_EXPLANATION)));
        }
        return result;
    }

    public boolean isAliasCommand() {
        return isProxiedCommand;
    }

    public String getOriginalCommandName() {
        return configurationElement.getAttribute(COMMAND_NAME_ATTRIBUTE);
    }

}
