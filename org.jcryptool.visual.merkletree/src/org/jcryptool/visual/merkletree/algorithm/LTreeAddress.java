package org.jcryptool.visual.merkletree.algorithm;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class LTreeAddress extends Address {
	byte layerAddress = 0;	//8 bit layer address
	byte[] treeAddress = {0,0,0,0,0};	//40 Bit layer address
	byte otsBit;	//7bit padding with otsbit
	byte lTreeBit;	//7bit padding with lTreeBit 
	byte[] lTreeAddress = new byte[3];	//24bit l-tree address
	byte treeHeight;	//24bit padding with 8bit tree height
	byte[] treeIndex = new byte[3];	//24bit tree index
	byte blockKeyBit;	//6 bit padding with 1bit block bit and 1 bit key
	
	@Override
	public void setHashAdress(int i) {
		// do nothing, not needed

	}

	@Override
	public void setKeyBit(boolean bit) {
		if(bit == true) {
			blockKeyBit |= 1; //set keyBit (bit nr. 0) to 1
		} else{
			blockKeyBit &= 0; //set keyBit (bit nr. 0) to 0
		}
	}

	@Override
	public void setChainAddress(int i) {
		// do nothing, not needed
		

	}

	@Override
	public void setBlockBit(boolean bit) {
		if(bit == true) {
			blockKeyBit |= 2; //set blockBit (bit nr. 1) to 1
		} else {
			blockKeyBit &= 1; //set blockBit (bit nr. 1) to 0
		}

	}

	@Override
	public void setTreeHeight(int i) {
		treeHeight = (byte) i;

	}

	@Override
	public void setTreeIndex(int i) {
		treeIndex[0] = (byte)(i >> 16);
		treeIndex[1] = (byte)(i >> 8);
		treeIndex[2] = (byte)(i);

	}

	@Override
	public int getTreeHeight() {
		return treeHeight;
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
		lTreeAddress[0] = (byte)(i >> 16);
		lTreeAddress[1] = (byte)(i >> 8);
		lTreeAddress[2] = (byte)(i);
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
		ByteUtils.concatenate(temp, lTreeAddress);
		ByteUtils.concatenate(temp, treeHeight);
		ByteUtils.concatenate(temp, treeIndex);
		ByteUtils.concatenate(temp, blockKeyBit);
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
