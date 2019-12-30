// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.sphincsplus.algorithm;

import org.jcryptool.visual.sphincsplus.EnvironmentParameters;

/**
 * This Class represents the authentication-path of one tree
 * 
 * @author Lukas Stelzer
 *
 */
public class AUTH {
    private byte[][] path;
    public int length = 0;
    /**
     * type can be "fors" or "xmss" The type defines the length of the AUTH-Path (xmss=>path[h'][n]
     * fors=>path[a][n])
     * 
     * @param type
     */
    public AUTH(byte[] path, String type) {
        int maxIndex = 0;
        if (type == "fors") {
            maxIndex = EnvironmentParameters.a;
        } else {
            maxIndex = EnvironmentParameters.h / EnvironmentParameters.d;
        }
        this.path = new byte[maxIndex][EnvironmentParameters.n];
        for (int i = 0; i < maxIndex; i++) {
            System.arraycopy(path, i * EnvironmentParameters.n, this.path[i], 0, EnvironmentParameters.n);
        }
    }

    /**
     * type can be "fors" or "xmss" The type defines the length of the AUTH-Path (xmss=>path[h'][n]
     * fors=>path[a][n])
     * 
     * @param type
     */
    public AUTH(String type) {
        if (type == "fors") {
            this.path = new byte[EnvironmentParameters.a][EnvironmentParameters.n];
        } else {
            int hXMSS = EnvironmentParameters.h / EnvironmentParameters.d;
            this.path = new byte[hXMSS][EnvironmentParameters.n];
        }
    }

    /**
     * This Method sets one node of the authentication-path
     * 
     * @param path
     * @param index
     */
    public void setNode(byte[] path, int index) {
        this.path[index] = path;
        this.length += path.length;
    }

    /**
     * 
     * @param index
     * @return a single node of the authentication-path
     */
    public byte[] getNode(int index) {
        return this.path[index];
    }

    /**
     * 
     * @return the entire authentication-path of one tree
     */
    public byte[] getAUTH() {
        return Utils.concatenateByteArrays(this.path);

    }
}
