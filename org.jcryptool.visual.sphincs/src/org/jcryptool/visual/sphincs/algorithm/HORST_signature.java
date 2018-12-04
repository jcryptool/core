package org.jcryptool.visual.sphincs.algorithm;

public class HORST_signature {
	public HORST_signature(byte[] root, byte[][] leaves) {
		this._root = root;
		this._leaves = leaves;
	}
	
	public byte[] getRoot() {
		return this._root;
	}
	
	public byte[][] getLeaves() {
		return this._leaves;
	}
	
	public byte[] getLeave(int index) {
		return this._leaves[index];
	}
	
	byte[]	_root;
	byte[][] _leaves;
}
