package org.jcryptool.visual.sphincs.algorithm;

import java.awt.List;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Base64.Encoder;

public class PrivateKey {
	public PrivateKey(byte[] holeKey) {
		this._seed = Arrays.copyOfRange(holeKey, 0, 32);
		this._bitmasks = new byte[32][32];
		for(int i = 0; i < 32; i++) {
			this._bitmasks[i] = Arrays.copyOfRange(holeKey, 32+32*i, 32+32 * (i+1));
		}
		this._sk2 = Arrays.copyOfRange(holeKey, 1088-32, 1088);
		_key = new byte[holeKey.length];
		System.arraycopy(holeKey, 0, _key, 0, holeKey.length);
	}
	
	public PrivateKey(byte[] seed, byte[] sk2, byte[][] bitmasks) {
		this._seed = seed;
		this._sk2 = sk2;
		this._bitmasks = bitmasks;
	}
	
	public byte[] getSeed() {
		return this._seed;
	}
	
	public String getSeedToString() {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_seed);
	}
	
	public byte[] getSecret() {
		return this._sk2;
	}
	
	public String getSecretToString() {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_sk2);
	}
	
	public byte[][] getBitmasks() {
		return this._bitmasks;
	} 
	
	public byte[] getKey() {
		return _key;
	}
	
	byte[] getMask() {
		byte[] mask = new byte[32*32];
		for (int i = 0; i<32*32; i++) {
			mask[i] = _bitmasks[i/32][i%32];
		}
		return mask;
	}
	
	public String BitMasksToString() {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(getMask());
	}

	
	public int getLength() {
		return this._key.length;
	}
	
	
	@Override
	public String toString() {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_key);
	}



	byte[] _seed;
	byte[] _sk2;
	byte[][] _bitmasks;
	byte[] _key;
}
