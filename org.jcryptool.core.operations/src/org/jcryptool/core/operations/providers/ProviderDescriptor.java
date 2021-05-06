// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2021 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.operations.providers;

import org.jcryptool.core.operations.IOperationsConstants;

/**
 * A meta descriptor for crypto providers.
 * 
 * @author t-kern
 * 
 */
public class ProviderDescriptor {

    /** The id of the plug-in supplying the provider */
    private String id;

    /** The value of the provider object's <code>getName()</code> method */
    private String name;

    /** The value of the provider object's <code>getInfo()</code> method */
    private String info;

    /**
     * Creates a new ProviderDescriptor.
     * 
     * @param name The <code>getName()</code> value of the provider object
     * @param info the <code>getInfo()</code> value of the provider object
     */
    public ProviderDescriptor(String name, String info) {
        this.id = IOperationsConstants.PLATFORM;
        this.name = name;
        this.info = info;
    }

    /**
     * Creates a new ProviderDescriptor.
     * 
     * @param id The id of the plug-in supplying the provider
     * @param name The <code>getName()</code> value of the provider object
     * @param info the <code>getInfo()</code> value of the provider object
     */
    public ProviderDescriptor(String id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

    /**
     * Returns the id of the plug-in supplying the provider
     * 
     * @return The id of the plug-in supplying the provider
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the <code>getName()</code> value.
     * 
     * @return The <code>getName()</code> value
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the <code>getInfo()</code> value.
     * 
     * @return The <code>getInfo()</code> value
     */
    public String getInfo() {
        return info;
    }

    /**
     * Returns a String containing all meta information.
     * 
     * @return A String containing all meta information
     */
    public String toCompleteString() {
        return "(" + id + ", " + name + ", " + info + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return info;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (o instanceof ProviderDescriptor) {
            if (name.equals(((ProviderDescriptor) o).getName())) {
                return true;
            } else
                return false;
        } else
            return false;
    }

}