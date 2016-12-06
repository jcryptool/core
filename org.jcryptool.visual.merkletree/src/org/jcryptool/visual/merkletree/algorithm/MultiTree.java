package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;

public abstract class MultiTree implements ISimpleMerkleTree {
	// nr of trees for the hypertree klickibunti

	// public String xPrivKey;
	// public String xPubKey;
	public byte[] seed;
	OTS otsAlgo;
	MessageDigest mDigest;
	static int id;
	Node Parent;
	String cipherSuite;
	String algorithmID;
	public byte[] SK_MT;
	public byte[] PK_MT;
	public params params;

	public MultiTree(int d, int h, int w, String message) {
		this.otsAlgo = new WOTSPlus(w, message, seed); // wots+ has the length
														// from message and not
														// from hash
		// winternitz parameter stays the same for every tree (duh)

		if (h % d != 0) {
			/*
			 * throw hDevidesDwithRemainderException(){
			 * 
			 * }
			 */
		}
		this.l = h / d;
		int i = 0;
		while (i < d) {
			// TODO
		}
	}

	/*
	 * void setParameters(int n, int h, int d, int w){
	 * 
	 * 
	 * }
	 */
	// make visible which tree signs which one--> parent

	void setLayers(int d) {
		this.d = d;
	}

	/**
	 * returns number of trees on a certain layer
	 * 
	 * @param h
	 *            overall height of the tree
	 * @param d
	 *            layer on which the number of trees is searched
	 * @return
	 */
	public double getXMSSTreeCount(int h, int d) {
		if (l == (this.d - 1))
			return 1;
		else {
			double s = Math.pow(2, (h / d));
			return s;
		}
	}

	public void xmssmt_public_key() {
		/*
		 * +---------------------------------+ | algorithm OID |
		 * +---------------------------------+ | | | root node | n bytes | |
		 * +---------------------------------+ | | | SEED | n bytes | |
		 * +---------------------------------+
		 */
		String algorithmID = this.algorithmID;
		byte[] publicRoot = Layer.getMerkleRoot();
		byte[] publicSeed = this.getSeed();
	}

	public void visualiseSig() {
		/*
		 * +---------------------------------+ | | | index idx_sig | ceil(h / 8)
		 * bytes | | +---------------------------------+ | | | randomness r | n
		 * bytes | | +---------------------------------+ | | | (reduced) XMSS
		 * signature Sig | (h / d + len) * n bytes | (bottom layer 0) | | |
		 * +---------------------------------+ | | | (reduced) XMSS signature
		 * Sig | (h / d + len) * n bytes | (layer 1) | | |
		 * +---------------------------------+ | | ~ .... ~ | |
		 * +---------------------------------+ | | | (reduced) XMSS signature
		 * Sig | (h / d + len) * n bytes | (layer d - 1) | | |
		 * +---------------------------------+
		 */
		// TODO
	}

	/**
	 * strongly oriented on Hulsing's C implementation, variable and function names etc stay the same
	 * @return
	 */
	
	void xmss_keypair(byte [] pk, byte [] sk, params params)
	{
	  byte[] n = params.getN(params);
	  // Set idx = 0
	  sk[0] = 0;
	  sk[1] = 0;
	  sk[2] = 0;
	  sk[3] = 0;
	  // Init SK_SEED (n byte), SK_PRF (n byte), and PUB_SEED (n byte)
	  XMSSTree.randomGenerator(sk.getSeed(), MessageDigest.getInstance(sk), pk.getSeed());
	  if((n*3)<1048576){
		  byte []random=new byte[(n*3)];
		  new Random().nextBytes(random);
	  }
	  else{
	  byte []random=new byte[1048576];
	  new Random().nextBytes(random);
	  
	  }
	  // Copy PUB_SEED to public key

	  System.arraycopy(sk,	n,pk,n,n)
	  uint32_t addr[8] = {0, 0, 0, 0, 0, 0, 0, 0};
	  // Compute root
	  treehash(pk, params->h, 0, sk+4, params, sk+4+2*n, addr);
	  // copy root to sk
	  memcpy(sk+4+3*n, pk, n);
	  return 0;
	}

	/*
	 * public byte[] XMSS_MT_KeyGen(){ // Example initialization int idx_MT = 0;
	 * //multiTreeIndex setIdx(SK_MT, idx_MT);s initialize SK_PRF with a
	 * uniformly random n-byte string; setSK_PRF(SK_MT, SK_PRF); initialize SEED
	 * with a uniformly random n-byte string; setSEED(SK_MT, SEED);
	 * 
	 * // generate reduced XMSS private keys ADRS = toByte(0, 32); for ( layer =
	 * 0; layer < d; layer++ ) { ADRS.setLayerAddress(layer); for ( tree = 0;
	 * tree < (1 << ((d - 1 - layer) * (h / d))); tree++ ) {
	 * ADRS.setTreeAddress(tree); for ( i = 0; i < 2^h; i++ ) {
	 * WOTS_genSK(wots_sk[i]); } setXMSS_SK(SK_MT, wots_sk, tree, layer); } }
	 * 
	 * SK = getXMSS_SK(SK_MT, 0, d - 1); setSEED(SK, SEED); root = treeHash(SK,
	 * 0, h / d, ADRS); setRoot(SK_MT, root);
	 * 
	 * PK_MT = (root || SEED); return (SK_MT || PK_MT); }
	 */

	/*
	 * public void chooseAlgorihm() {
	 * 
	 * // eig. klassenvariable, aber for reasons private static final String[]
	 * // cipherSuites={"xmssmt_sha2-256_w16_h20_d2",
	 * 
	 * 
	 * final String[] cipherSuites = { "xmssmt_sha2-256_w16_h20_d2",
	 * "xmssmt_sha2-256_w16_h20_d4", "xmssmt_sha2-256_w16_h40_d2",
	 * "xmssmt_sha2-256_w16_h40_d4", "xmssmt_sha2-256_w16_h40_d8",
	 * "case xmssmt_sha2-256_w16_h60_d3", "xmssmt_sha2-256_w16_h60_d6",
	 * "xmssmt_sha2-256_w16_h60_d12", "xmssmt_shake128_w16_h20_d2",
	 * "xmssmt_shake128_w16_h20_d4", "xmssmt_shake128_w16_h40_d2",
	 * "xmssmt_shake128_w16_h40_d4", "xmssmt_shake128_w16_h40_d8",
	 * "xmssmt_shake128_w16_h60_d3", "xmssmt_shake128_w16_h60_d6",
	 * "xmssmt_shake128_w16_h60_d12", "xmssmt_sha2-512_w16_h20_d2",
	 * "xmssmt_sha2-512_w16_h20_d4", "xmssmt_sha2-512_w16_h40_d2",
	 * "xmssmt_sha2-512_w16_h40_d4", "xmssmt_sha2-512_w16_h40_d8",
	 * "xmssmt_sha2-512_w16_h60_d3", "xmssmt_sha2-512_w16_h60_d6",
	 * "xmssmt_sha2-512_w16_h60_d12", "xmssmt_shake256_w16_h20_d2",
	 * "xmssmt_shake256_w16_h20_d4", "xmssmt_shake256_w16_h40_d2",
	 * "xmssmt_shake256_w16_h40_d4", "xmssmt_shake256_w16_h40_d8",
	 * "xmssmt_shake256_w16_h60_d3", "xmssmt_shake256_w16_h60_d6",
	 * "xmssmt_shake256_w16_h60_d12" };
	 * 
	 * // Display display = new Display(); // Shell shell = new Shell(display);
	 * // shell.setLayout(new FillLayout());
	 * 
	 * // Create a dropdown Combo Combo combo = new Combo(shell, SWT.DROP_DOWN |
	 * SWT.READ_ONLY); readOnly.setItems(cipherSuites);
	 * 
	 * shell.open(); while (!shell.isDisposed()) { if
	 * (!display.readAndDispatch()) { display.sleep(); } } display.dispose();
	 * 
	 * }
	 */

	public byte[] getSeed() {
		return params.getN(params);
	}

}
