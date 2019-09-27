package org.jcryptool.visual.sphincsplus.algorithm;

import org.jcryptool.visual.sphincsplus.interfaces.IADRS;

public class ADRS implements IADRS {
    private int layerAddress;
    private byte[] treeAddress;
    private ADRSTypes type;
    private byte[] keyPairAddress;
    private int chainAddress;
    private int hashAddress;
    private int treeHeight;
    private int treeIndex;

    public ADRS() {
        this.layerAddress = 0;
        this.treeAddress = new byte[12];
        this.type = ADRSTypes.WOTS_PK;
        this.keyPairAddress = new byte[4];
        this.chainAddress = 0;
        this.hashAddress = 0;
        this.treeHeight = 0;
        this.treeIndex = 0;
    }

    @Override
	public byte[] toByteArray() {
        byte[] result = new byte[32];
        byte[] layerAddress = Utils.intToByteArray(this.layerAddress);
        byte[] treeAddress = this.treeAddress;
        byte[] type = Utils.intToByteArray(typeToInt());
        byte[] keyPairAddress = this.keyPairAddress;

        switch (this.type) {
        case WOTS_HASH:
        case WOTS_PK:
            byte[] chainAddress = Utils.intToByteArray(this.chainAddress);
            byte[] hashAddress = Utils.intToByteArray(this.hashAddress);
            byte[][] tempArray0 = { layerAddress, treeAddress, type, keyPairAddress, chainAddress, hashAddress };
            result = Utils.concatenateByteArrays(tempArray0);
            break;
        case TREE:
        case FORS_TREE:
        case FORS_ROOTS:
            byte[] treeHeight = Utils.intToByteArray(this.treeHeight);
            byte[] treeIndex = Utils.intToByteArray(this.treeIndex);
            byte[][] tempArray1 = { layerAddress, treeAddress, type, keyPairAddress, treeHeight, treeIndex };
            result = Utils.concatenateByteArrays(tempArray1);
            break;
        }
        return result;
    }

    public ADRS deepCopy() {
        ADRS copy = new ADRS();
        copy.layerAddress = layerAddress;
        // BigInt is immutable
        copy.treeAddress = treeAddress;
        copy.type = type;
        copy.keyPairAddress = keyPairAddress;
        copy.chainAddress = chainAddress;
        copy.hashAddress = hashAddress;
        copy.treeHeight = treeHeight;
        copy.treeIndex = treeIndex;
        return copy;
    }

    @Override
    public void setTreeHeight(int n) {
        this.treeHeight = n;

    }

    @Override
    public void setTreeIndex(int n) {
        this.treeIndex = n;

    }

    @Override
    public void setType(ADRSTypes type) {
        this.type = type;
        switch (type) {
        case WOTS_PK:
        case FORS_ROOTS:
            this.chainAddress = 0;
            this.hashAddress = 0;
        case WOTS_HASH:
            this.treeHeight = 0;
            this.treeIndex = 0;
            break;
        case TREE:
            this.keyPairAddress = new byte[4];
        case FORS_TREE:
            this.chainAddress = 0;
            this.hashAddress = 0;
            break;
        default:
            break;
        }
    }

    @Override
    public void setLayerAddress(int n) {
        this.layerAddress = n;

    }

    @Override
    public void setTreeAddress(byte[] n) {
        this.treeAddress = n;

    }

    @Override
    public void setKeyPairAddress(byte[] idx_leaf) {
        this.keyPairAddress = idx_leaf;

    }

    @Override
    public void setChainAddress(int n) {
        this.chainAddress = n;

    }

    @Override
    public void setHashAddress(int n) {
        this.hashAddress = n;

    }

    @Override
    public int getTreeHeigh() {
        return this.treeHeight;
    }

    @Override
    public int getTreeIndex() {
        return this.treeIndex;
    }

    @Override
    public ADRSTypes getType() {
        return this.type;
    }

    @Override
    public int getLayerAddress() {
        return this.layerAddress;
    }

    @Override
    public byte[] getTreeAddress() {
        return this.treeAddress;
    }

    @Override
    public byte[] getKeyPairAddress() {
        return this.keyPairAddress;
    }

    @Override
    public int getChainAddress() {
        return this.chainAddress;
    }

    @Override
    public int getHashAddress() {
        return this.hashAddress;
    }

    private int typeToInt() {
        int value = 0;
        switch (this.type) {
        case WOTS_HASH:
            value = 0;
            break;
        case WOTS_PK:
            value = 1;
            break;
        case TREE:
            value = 2;
            break;
        case FORS_TREE:
            value = 3;
            break;
        case FORS_ROOTS:
            value = 4;
            break;
        }
        return value;
    }
}
