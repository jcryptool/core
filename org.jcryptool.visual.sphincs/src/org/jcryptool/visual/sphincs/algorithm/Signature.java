package org.jcryptool.visual.sphincs.algorithm;

import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.jcryptool.visual.sphincs.algorithm.bc_code.SPHINCS256Config;
import org.jcryptool.visual.sphincs.algorithm.bc_code.Tree;

public class Signature {
	byte[] _i;
	byte[] _r;
	byte[] _sigMessage;
	byte[][] _sigWOTS;
	byte[][] _auth;
	byte[] _data;
	
	
	public Signature(byte[] data) {
		int sig_offset = 0;
		this._r = Arrays.copyOfRange(data, sig_offset, sig_offset + 32 );
		sig_offset += 32;
		this._i = Arrays.copyOfRange(data, sig_offset, sig_offset+8);
		sig_offset += 8;
		this._sigMessage = Arrays.copyOfRange(data, sig_offset, sig_offset+13312);
		sig_offset += 13312;
		this._sigWOTS = new byte[12][2144];
		this._auth = new byte[12][5*32];
		for (int i = 0; i < 12; i++) {
			this._sigWOTS[i] = Arrays.copyOfRange(data, sig_offset, sig_offset+2144);
			sig_offset += 2144;
			this._auth[i] = Arrays.copyOfRange(data, sig_offset, sig_offset+(5*32));
			sig_offset += 5*32;
		}
		this._data = new byte[data.length];
		System.arraycopy(data, 0, this._data, 0, data.length);
	}
	
	public Signature(byte[] i, byte[] r, byte[] sigMessage, byte[][] sigWOTS, byte[][] auth) {
		this._i = i;
		this._r = r;
		this._sigMessage = sigMessage;
		this._sigWOTS = sigWOTS;
		this._auth = auth;
	}
	
	public long getLeafidx() {
		long leafidx = 0;
		for (int i = 0; i < 8; i++) {
			leafidx ^= ((long)(this._i[i] & 0xff) << (8 * i));
		}
		return leafidx;
	}
	
	public long getSubTree() {
		
		return (getLeafidx() >>> 5);
	}
	
	public int getSubLeaf() {
		return ((int)(getLeafidx() & ((1 << 5) - 1)));
	}
	
	public String getIndex() {
		return Base64(this._i);
	}
	
	public String getRandomValue() {
		return Base64(this._r);
	}
	
	public String getHorstSignature() {
		return Base64(this._sigMessage);
	}
	

	
	public String getAuthPath() {
		StringBuilder sb = new StringBuilder();
		String[] _paths = getAutPathStrings();
		for (int i = 0; i < 12; i++) {
			if (i != 0)
				sb.append("|");
			sb.append(_paths[i]);
		}
		return sb.toString();
	}
	
	String[] getAutPathStrings() {
		String[] Paths = new String[12];
		for (int i = 0; i < 12; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(getWOTSSignature(i));
			sb.append(" | ");
			sb.append(getAuth(i));
			Paths[i] =  sb.toString();
		}
		return Paths;
	}
	
	String byteArrayToString(byte[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < array.length; i++)
		{
			if(i != 0)
				sb.append(", ");
			sb.append(array[i]);
		}
		sb.append("]");
		return sb.toString();		
	}
	
	String getWOTSSignature(int index) {
		return Base64(this._sigWOTS[index]);
	}
	
	String getAuth(int index) {
		return Base64(this._auth[index]);
	}
	
	public byte[] getBytes() {
		return _data;
	}
	
	String Base64(byte[] _buff) {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_buff);
	}

	@Override
	public String toString() {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_data);
	}
}
