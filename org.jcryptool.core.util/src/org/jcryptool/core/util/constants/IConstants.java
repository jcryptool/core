// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.constants;

/**
 * Interface for JCrypTool global constants.
 *
 * @author Dominik Schadow
 * @version 0.9.5
 */
public interface IConstants {
    String BIN_FILE_TYPE_EXTENSION = ".bin"; //$NON-NLS-1$
    String TXT_FILE_TYPE_EXTENSION = ".txt"; //$NON-NLS-1$
    String XML_FILE_TYPE_EXTENSION = ".xml"; //$NON-NLS-1$
    String OUTPUT_REGEXP = "out\\d\\d\\d.((xml)|(bin)|(txt))"; //$NON-NLS-1$
    String TXT_FILTER_EXTENSION = "*.txt"; //$NON-NLS-1$
    String ALL_FILTER_EXTENSION = "*.*"; //$NON-NLS-1$
    String TXT_FILTER_NAME = Messages.IConstants_0;
    String ALL_FILTER_NAME = Messages.IConstants_1;
}
