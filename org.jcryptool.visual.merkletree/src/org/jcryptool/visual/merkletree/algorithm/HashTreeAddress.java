package org.jcryptool.visual.merkletree.algorithm;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class HashTreeAddress extends Address {
	byte layerAddress = 0;
	byte[] treeAddress = {0,0,0,0,0};
	byte otsBit;
	byte lTreeBit;
	byte[] treeHeight = new byte[4];
	byte[] treeIndex = new byte[3];
	byte blockBit;
	byte keyBit;

	@Override
	public void setHashAdress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setKeyBit(boolean bit) {
		if(bit == true) {
			keyBit = 1; //set keyBit (bit nr. 0) to 1
		} else{
			keyBit = 0; //set keyBit (bit nr. 0) to 0
		}
	}

	@Override
	public void setChainAddress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setBlockBit(boolean bit) {
		if(bit == true) {
			blockBit = 1; //set blockBit (bit nr. 1) to 1
		} else {
			blockBit = 0; //set blockBit (bit nr. 1) to 0
		}

	}


	@Override
	public void setTreeHeight(int i) {
		treeHeight[3] = (byte) i;

	}

	@Override
	public void setTreeIndex(int i) {
		treeIndex[0] = (byte)(i >> 16);
		treeIndex[1] = (byte)(i >> 8);
		treeIndex[2] = (byte)(i);
	}

	@Override
	public int getTreeHeight() {
		return treeHeight[3];
	}

	@Override
	public void setOTSBit(boolean bit) {
		if(bit == true) {
			otsBit = 1; //set otsBit (bit nr. 1) to 1
		} else {
			otsBit = 0; //set otsBit (bit nr. 1) to 0
		}

	}

	@Override
	public void setOTSAddress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setLTreeBit(boolean bit) {
		if(bit == true) {
			lTreeBit = 1; //set otsBit (bit nr. 1) to 1
		} else {
			lTreeBit = 0; //set otsBit (bit nr. 1) to 0
		}

	}

	@Override
	public void setLTreeAddress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setLayerAddress(int i) {
		layerAddress = (byte) i;
	}

	@Override
	public void setTreeAddress(int i) {
		// TODO Auto-generated method stub

	}
	
	@Override
	/**
	 * @return Address construct as in the rfc
	 */
	public byte[] getAddress(){
		byte[] temp = ByteUtils.concatenate(layerAddress, treeAddress);
		//geht hier nicht noch Padding ab??
		ByteUtils.concatenate(temp, otsBit);
		ByteUtils.concatenate(temp, lTreeBit);
		ByteUtils.concatenate(temp, treeHeight);
		ByteUtils.concatenate(temp, treeIndex);
		ByteUtils.concatenate(temp, blockBit);
		ByteUtils.concatenate(temp, keyBit);
		return temp;
		}
	
	public int getTreeIndex() {
		int res = 0;
		res += treeIndex[0] << 16;
		res += treeIndex[1] << 8;
		res += treeIndex[2];
		return res;
	}

}
