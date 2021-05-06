// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.dataobject.classic;

import java.io.InputStream;

import org.jcryptool.core.operations.dataobject.IDataObject;

/**
 * Provides a top level frame for the data objects used for classic algorithms.
 * 
 * @author amro
 * @author t-kern
 * @version 0.1
 * 
 */
public interface IClassicDataObject extends IDataObject {

    /**
     * 
     * @return an InputStream with cipher output
     */
    public InputStream getOutputIS();

    /**
     * Interface implementors must provide a getter for the final output of algorithms execution.
     * 
     * Specifically final means final, e. g. for classic algorithms if non alpha filters should not be filtered the
     * merged final output.
     * 
     * @return the final output of an execution of an algorithm
     */
    public String getOutput();

    /**
     * Setter for the plain text
     * 
     * @param input the plain text
     */
    public void setPlain(String input);

}
