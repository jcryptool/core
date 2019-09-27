package org.jcryptool.visual.sphincsplus.interfaces;

import org.jcryptool.visual.sphincsplus.algorithm.ADRSTypes;

/**
 * An address ADRS is a 32-byte value that follows a defined structure. In addition, it comes with
 * set methods to manipulate the address, and with get methods to read the different Fields of the
 * address.
 * 
 * There are five different types of addresses for different use cases:
 * {@link org.jcryptool.visual.sphincsplus.algorithm.ADRSTypes}
 * 
 * @author Lukas Stelzer
 *
 */
public interface IADRS {

    public void setTreeHeight(int n);

    public void setTreeIndex(int n);

    public void setType(ADRSTypes type);

    public void setLayerAddress(int n);

    public void setTreeAddress(byte[] n);

    public void setKeyPairAddress(byte[] idx_leaf);

    public void setChainAddress(int n);

    public void setHashAddress(int n);

    public int getTreeHeigh();

    public int getTreeIndex();

    public ADRSTypes getType();

    public int getLayerAddress();

    public byte[] getTreeAddress();

    public byte[] getKeyPairAddress();

    public int getChainAddress();

    public int getHashAddress();

    public byte[] toByteArray();
}
