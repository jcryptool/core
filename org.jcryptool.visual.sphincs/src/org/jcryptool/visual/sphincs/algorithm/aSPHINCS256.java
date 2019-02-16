package org.jcryptool.visual.sphincs.algorithm;

/**
 * @author lehrbaumm
 * Abstract Class for SPHINCS Algorithm
 */
public abstract class aSPHINCS256 {
	PrivateKey _privKey;
	PublicKey _pubKey;
	WOTSp_signature[] _Wots;
	
	public PublicKey getPublicKey() {
		return this._pubKey;
	}
	
	public PrivateKey getPrivateKey() {
		return this._privKey;
	}
	
	public abstract void generateKeys();
	
	public abstract Signature sign(byte[] message);
	
	public abstract Signature sign(String message);
	
	public abstract boolean verify(Signature signature, byte[] message);
	
	public abstract boolean verify(Signature signature, String message);
	
	public abstract WOTSp_signature getWOTS(int level, long tree, long subleaf);
	public abstract String[] getPath(Signature sig);
}
