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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * CommandFactory and Factory via loadExtensions()
 *
 * @author paw
 *
 */
public class CommandFactory {
    static public List<Command> loadExtensions() {
        // TODO: load aliases as single, own commands, but mark them as aliases
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint("org.jcryptool.commands.core.commands"); //$NON-NLS-1$
        IConfigurationElement points[] = extensionPoint.getConfigurationElements();
        List<Command> commands = new LinkedList<Command>();

        for (IConfigurationElement point : points) {
            if (isExtendedCommand(point)) {
                ProxiedExtendedCommand cmdMainObj = new ProxiedExtendedCommand(point, false);
                commands.add(cmdMainObj);
                // Add "duplicate" command objects to the pool which are marked as aliases
                for (String alias : cmdMainObj.getAliases()) {
                    ProxiedExtendedCommand aliasObj = new ProxiedExtendedCommand(point, true);
                    aliasObj.setCommandName(alias);
                    commands.add(aliasObj);
                }
            } else {
                commands.add(new ProxiedCommand(point));
            }
        }

        return commands;
    }

    public static List<Command> loadUniqueExtensions() {
        List<Command> commands = loadExtensions();
        Set<Command> commandsToRemove = new HashSet<Command>();
        for (Command command : commands) {
            if (command.getCommandName().equals("HELP"))commandsToRemove.add(command); //$NON-NLS-1$
            if (command instanceof ExtendedHelpCommand) {
                if (((ExtendedHelpCommand) command).isAliasCommand()) {
                    commandsToRemove.add(command);
                }
            }
        }
        for (Command command : commandsToRemove)
            commands.remove(command);

        return commands;
    }

    private static boolean isExtendedCommand(IConfigurationElement iConfigurationElement) {
        if (iConfigurationElement.getChildren().length != 0)
            return true;
        return false;
    }
}
