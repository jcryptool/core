//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.merkletree.algorithm;

public class MerkleTreeNode {
    byte[] content;
    int height;
    int width;

    MerkleTreeNode(int height, int width, byte[] content) {
        this.content = content;
        this.height = height;
        this.width = width;
    }

    int getHeight() {

        return this.height;
    }

    int getWidth() {
        return this.width;
    }

    byte[] getContent() {

        return this.content;
    }

    public String contentAsString() {
        return content.toString();

    }
}
