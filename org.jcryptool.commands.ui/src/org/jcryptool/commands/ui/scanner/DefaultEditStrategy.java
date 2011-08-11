//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.commands.ui.scanner;

import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

public final class DefaultEditStrategy implements IAutoEditStrategy {
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		command.offset = document.getLength()-1;
		command.shiftsCaret = true;
		command.text = ""; //$NON-NLS-1$
	}
}