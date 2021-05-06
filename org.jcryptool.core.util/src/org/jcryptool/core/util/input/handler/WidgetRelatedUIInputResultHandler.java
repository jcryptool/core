// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.util.input.handler;

import org.eclipse.swt.widgets.Control;
import org.jcryptool.core.util.input.AbstractUIInput;

/**
 * Handler for AbstractUIInput verification messages. Maps a Control to every AbstractUIInput, ensuring that messages
 * can be shown very close to the region of interest.
 * 
 * @author Simon L
 */
public interface WidgetRelatedUIInputResultHandler extends UIInputResultHandler {

    public Control mapInputToWidget(AbstractUIInput<?> input);

}
