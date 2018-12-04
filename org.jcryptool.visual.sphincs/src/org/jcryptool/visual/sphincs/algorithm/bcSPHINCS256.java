package org.jcryptool.visual.sphincs.algorithm;


import java.util.List;
import java.util.Base64.Encoder;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;

//add the provider package
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.bouncycastle.pqc.crypto.sphincs.*;
import org.bouncycastle.crypto.*;

import org.jcryptool.visual.sphincs.algorithm.bc_code.*;

public class bcSPHINCS256 extends aSPHINCS256 {
	SPHINCS256KeyGenerationParameters _GenerationParameter;
	SecureRandom _SecureRandom;
	Digest _MessageDigest;
	Digest _TreeDigest;
	
	SPHINCS256KeyPairGenerator _KeyGenerator;
	
	SPHINCSPrivateKeyParameters _PrivateKey;
	SPHINCSPublicKeyParameters _PublicKey;
	
	SPHINCS256Signer _SPHINCS;

	public bcSPHINCS256() {
		_Wots = new WOTSp_signature[12];
		Security.addProvider(new BouncyCastleProvider());
		_KeyGenerator = new SPHINCS256KeyPairGenerator();
		_SecureRandom = new SecureRandom();
		_MessageDigest = new SHA256Digest();
		_TreeDigest = new SHA512Digest();
		_GenerationParameter = new SPHINCS256KeyGenerationParameters(_SecureRandom, _MessageDigest);
		_KeyGenerator.init(_GenerationParameter);
		_SPHINCS = new SPHINCS256Signer(_MessageDigest, _TreeDigest);
	}

	@Override
	public void generateKeys() {
		AsymmetricCipherKeyPair Key;
		Key = _KeyGenerator.generateKeyPair();
		_PrivateKey = (SPHINCSPrivateKeyParameters)Key.getPrivate();
		_PublicKey = (SPHINCSPublicKeyParameters)Key.getPublic();
		_privKey = new PrivateKey(_PrivateKey.getKeyData());
		_pubKey = new PublicKey(_PublicKey.getKeyData());
	}

	@Override
	public Signature sign(byte[] message) {
		_SPHINCS.init(true, _PrivateKey);
		byte[] signature = _SPHINCS.generateSignature(message);
		return new Signature(signature);
	}
	
	public Signature sign(String message) {
		Signature sig = sign(message.getBytes());
		return sig;
	}

	@Override
	public boolean verify(Signature signature, byte[] message) {
		_SPHINCS.init(false, _PublicKey);
		return _SPHINCS.verifySignature(message, signature.getBytes());
	}

	@Override
	public boolean verify(Signature signature, String message) {
		return verify(signature, message.getBytes());
	}

	@Override
	public WOTSp_signature getWOTS(int level, long tree, long subleaf) {        
		Tree.leafaddr a = new Tree.leafaddr();
		byte[] buffer = new byte[32];


        // Initialization of top-subtree address
        a.level = level;
        a.subtree = tree;
        a.subleaf = subleaf;

        HashFunctions hs = new HashFunctions(_MessageDigest);

        // Format pk: [|N_MASKS*params.HASH_BYTES| Bitmasks || root]
        // Construct top subtree
        Tree.treehash(hs, buffer, 0, 5, _privKey._key, a, _pubKey._key, 0);


		// TODO Auto-generated method stub
		return new WOTSp_signature(buffer);
	}
	
	public String[] getPath(Signature sig) {
		List<String> _data = new ArrayList<String>();
		Tree.leafaddr a = new Tree.leafaddr();
		a.subleaf = 0;
		a.subtree = sig.getSubTree();
		a.level = SPHINCS256Config.N_LEVELS;
		int counter = 0;
		for (int i = 0; i < SPHINCS256Config.N_LEVELS; i++) {
			if (i == SPHINCS256Config.N_LEVELS-1) {
				for (int j = 0; j < SPHINCS256Config.SUBTREE_HEIGHT; j++) {
					long index = (long)Math.pow(2, SPHINCS256Config.SUBTREE_HEIGHT - (j+1)) - 1;
					_data.add(Base64(getWOTS(i, a.subtree, index)._root));
					if (index !=  0)
					_data.add(Base64(getWOTS(i, a.subtree, index + 1)._root));
				}
				break;
			}
			if (i == 0) {
				_data.add(Base64(getWOTS(a.level, a.subtree, 0)._root));
				_data.add(Base64(getWOTS(a.level, a.subtree ^ 0x01, 0)._root));
			}
			else {
				for (int j = 0; j < SPHINCS256Config.SUBTREE_HEIGHT; j++) {
					long index = (long)Math.pow(2, SPHINCS256Config.SUBTREE_HEIGHT - (j+1)) - 1;
					_data.add(Base64(getWOTS(i, a.subtree, index)._root));
					if (index !=  0)
					_data.add(Base64(getWOTS(i, a.subtree, index + 1)._root));
				}
				_data.add(Base64(getWOTS(a.level, a.subtree ^ 0x01, 0)._root));
			}
            a.subtree >>>= SPHINCS256Config.SUBTREE_HEIGHT;
            a.level--;
		}
		String[] stringArray = _data.toArray(new String[0]);
		return stringArray;
	}
	

	
	String Base64(byte[] _buff) {
		Encoder Base64Encode = Base64.getEncoder();
		return Base64Encode.encodeToString(_buff);
	}

}
