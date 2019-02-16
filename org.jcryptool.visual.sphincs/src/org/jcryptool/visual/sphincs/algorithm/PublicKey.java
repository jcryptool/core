package org.jcryptool.visual.sphincs.algorithm;

import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

public class PublicKey {
	public PublicKey(byte[] holeKey) {
		this._rootNode = Arrays.copyOfRange(holeKey, 32*32, 32*32+32);
		this._bitmasks = new byte[32][32];
		for(int i = 0; i < 32; i++) {
			this._bitmasks[i] = Arrays.copyOfRange(holeKey, 32*i, 32 * (i+1));
		}
		_key = new byte[holeKey.length];
		System.arraycopy(holeKey, 0, _key, 0, holeKey.length);
	}	
	
	public PublicKey(byte[] rootNode, byte[][] bitMasks) {
		this._rootNode = rootNode;
		this._bitmasks = bitMasks;
	}
	
	public int getLength() {
		return this._key.length;
	}
	
	public byte[] getRootNode() {
		return this._rootNode;
	}
	
	public String getRootNodeToString() {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_rootNode);
	}
	
	public byte[][] getBitMasks() {
		return this._bitmasks;
	}
	
	public byte[] getBitMask(int index) {
		return this._bitmasks[index];
	}
	
	public byte[] getData() {
		return _key;
	}
	
	public byte[] getMask() {
		byte[] mask = new byte[32*32];
		for (int i = 0; i<32*32; i++) {
			mask[i] = _bitmasks[i/32][i%32];
		}
		return mask;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Encoder Base64Encode = Base64.getEncoder();
		sb.append(Base64Encode.encodeToString(getMask()));
		sb.append(Base64Encode.encodeToString(_rootNode));
		return sb.toString();
	}
	
	byte[] _rootNode;
	byte[][] _bitmasks;
	byte[] _key;
}
