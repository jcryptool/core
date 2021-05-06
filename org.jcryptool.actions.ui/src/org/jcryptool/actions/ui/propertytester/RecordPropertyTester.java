// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.actions.ui.propertytester;

import org.eclipse.core.commands.Command;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.services.IServiceLocator;

/**
 * <b>Record Property Tester</b> for the Actions view. Provides a test method for checking whether
 * the Record-Button in the Toolbar is active or not. Corresponding to this the other commands
 * are deactivated.
 *
 * @author Thomas Wiese
 * @version 0.5.0
 */
public class RecordPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {

		final IServiceLocator locator = (IServiceLocator) receiver;
		final ICommandService commandService = locator.getService(ICommandService.class);
		final Command command = commandService.getCommand("org.jcryptool.actions.recordCommand"); //$NON-NLS-1$
		return (Boolean)command.getState("org.jcryptool.actions.recordCommand.toggleState").getValue(); //$NON-NLS-1$
	}

}
