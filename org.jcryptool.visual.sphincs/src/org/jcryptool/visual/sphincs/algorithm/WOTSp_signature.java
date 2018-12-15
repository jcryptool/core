package org.jcryptool.visual.sphincs.algorithm;

public class WOTSp_signature {
	public WOTSp_signature(byte[] root) {
		this._root = root;
	}
	
	public byte[] getRoot() {
		return this._root;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < _root.length; i++) {
			if(i != 0)
				sb.append(", ");
			sb.append(_root[i]);
		}
		sb.append("]");
		// TODO Auto-generated method stub
		return sb.toString();
	}
	
	byte[] _root;
}
