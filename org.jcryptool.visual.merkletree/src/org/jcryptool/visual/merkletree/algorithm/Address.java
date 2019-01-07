//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
/**
 * 
 */
package org.jcryptool.visual.merkletree.algorithm;

/**
 * @author Zuck
 *
 *         This class implements the address structure to randomize each PRNG call
 */
public abstract class Address {
    byte layerAddress;
    byte[] treeAddress = new byte[5];
    byte otsBit;

    public abstract void setHashAdress(int i);

    public abstract void setKeyBit(boolean bit);

    public abstract void setChainAddress(int i);

    public abstract void setBlockBit(boolean bit);

    public abstract void setTreeHeight(int i);

    public abstract void setTreeIndex(int i);

    public abstract int getTreeHeight();

    public abstract void setOTSBit(boolean bit);

    public abstract void setOTSAddress(int i);

    public abstract void setLTreeBit(boolean bit);

    public abstract void setLTreeAddress(int i);

    public abstract void setLayerAddress(int i);

    public abstract void setTreeAddress(int i);

    public abstract byte[] getAddress();

    public abstract int getTreeIndex();

}