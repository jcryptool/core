package org.jcryptool.visual.merkletree.algorithm;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class OTSHashAddress extends Address {
	byte layerAddress = 0;	//8 bit layer address
	byte[] treeAddress = {0,0,0,0,0};	//40 Bit layer address
	byte otsBit;	//7bit padding with ots bit
	byte[] otsAddress = new byte[3];	//24bit ots address
	byte[] chainAddress = new byte[2];	//16bit chain address
	byte hashAddress;	//8bit hash address
	byte keyBit;	//7bit padding with 1bit key bit

	@Override
	public void setHashAdress(int i) {
		hashAddress = (byte)i;
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
		chainAddress[0] = (byte)(i >> 8);
		chainAddress[1] = (byte)(i);
	}

	@Override
	public void setBlockBit(boolean bit) {
		// do nothing, not needed

	}

	@Override
	public void setTreeHeight(int i) {
		// do nothing, not needed

	}

	@Override
	public void setTreeIndex(int i) {
		// do nothing, not needed

	}

	@Override
	public int getTreeHeight() {
		// do nothing, not needed
		return 0;
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
		otsAddress[0] = (byte)(i >> 16);
		otsAddress[1] = (byte)(i >> 8);
		otsAddress[2] = (byte)(i);
	}

	@Override
	public void setLTreeBit(boolean bit) {
		// do nothing, not needed

	}

	@Override
	public void setLTreeAddress(int i) {
		
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
		byte[] padding = new byte[2];	//16bit padding
		byte[] temp = ByteUtils.concatenate(layerAddress, treeAddress);
		ByteUtils.concatenate(temp, otsBit);
		ByteUtils.concatenate(temp, padding);
		ByteUtils.concatenate(temp, otsAddress);
		ByteUtils.concatenate(temp, chainAddress);
		ByteUtils.concatenate(temp, hashAddress);
		ByteUtils.concatenate(temp, keyBit);
		return temp;	
	}
	
	public int getTreeIndex() {
		// do nothing, not needed
		return 0;
	}

}
