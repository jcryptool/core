package org.jcryptool.visual.merkletree.algorithm;

import java.io.ByteArrayOutputStream;
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

public abstract class MultiTree implements ISimpleMerkle {
	// Starting over again
	int leafCounter = 0;
	int d;
	int l;
	int n;
	OTS otsAlgo;
	int h;
	int w;
	int index_len;
	byte[] message;
	Node[] treeArray;
	byte[] sk_seed;
	byte[] sk_prf;
	byte[] pub_seed;
	String sk;

	byte[] seed;
	BigInteger bitmaskSeed;
	MessageDigest mDigest;

	public ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();

	public void setLeafCount(int i) {
		leafCounter = i;
	}
	
	
	
	/*
	 * TODO LENA: 
	 * Compute Autopath
	 * better way to manage keys (optional)
	 */

	@Override
	/**
	 * Selects the Signature Algorithm defautl Algorithm is the WOTSPlus
	 * Algorithm after the Algorithm is selected the method ->
	 * generateKeyPairsAndLeaves() is called
	 */
	public void selectOneTimeSignatureAlgorithm(String hash, String algo) {
		byte[] s = seed.toByteArray();
		this.otsAlgo = new WOTSPlus(w, hash, s);
		if (this.mDigest == null) {
			try {
				mDigest = MessageDigest.getInstance(hash);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	// public void TreeParams(int d, int h, int w, String message) {
	// this.otsAlgo = new WOTSPlus(w, message, getSeed()); // wots+ has the
	// length
	// from message and not
	// from hash
	// winternitz parameter stays the same for every tree (duh)
	/*
	 * if (h % d != 0) {
	 *
	 * müssen teilerfremd sein throw hDevidesDwithRemainderException(){
	 * 
	 * }
	 *
	 * } this.l = h / d; int i = 0; while (i < d) { // TODO } }
	 * 
	 * /* nr of trees for the hypertree klickibunti
	 * 
	 * public String xPrivKey; public String xPubKey;
	 *
	 * OTS otsAlgo; MessageDigest mDigest; static int id; Node Parent; String
	 * cipherSuite; String algorithmID; public byte[] SK_MT; public byte[]
	 * PK_MT; byte[] SK_Seed; byte[] PK_Seed;
	 * 
	 * public params params;
	 * 
	 * 
	 * 
	 * /* void setParameters(int n, int h, int d, int w){
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
	/*
	 * public double getXMSSTreeCount(int h, int d) { if (l == (this.d - 1))
	 * return 1; else { double s = Math.pow(2, (h / d)); return s; } }
	 * 
	 * public void xmssmt_public_key() { /* +---------------------------------+
	 * | algorithm OID | +---------------------------------+ | | | root node | n
	 * bytes | | +---------------------------------+ | | | SEED | n bytes | |
	 * +---------------------------------+
	 */
	/*
	 * String algorithmID = this.algorithmID; byte[] publicRoot =
	 * Layer.getMerkleRoot(); byte[] publicSeed = this.getSeed();
	 */

	/**
	 * @author zuck, Lena Heimberger (modified, because BigIntegers are just
	 *         handier)
	 * @param SK
	 *            XMSS secret key
	 * @param s
	 *            start index
	 * @param t
	 *            target node height
	 * @param seed
	 *            seed
	 * @return root node of a tree of height t
	 */
	public Node treeHash(int s, int t, byte[] seed) {
		byte[] bSeed = bitmaskSeed.toByteArray(); // 'cause bytearrays
		Node node; // TODO make new Node class
		Stack<Node> stack = new Stack<Node>();
		byte[][] pKey;
		OTSHashAddress otsAdrs = new OTSHashAddress();
		LTreeAddress lAdrs = new LTreeAddress();
		HashTreeAddress hAdrs = new HashTreeAddress();

		if (s % (1 << t) != 0) {
			return null;
		}

		for (int i = 0; i < (1 << t); i++) { // i < 2^t
			otsAdrs.setOTSBit(true);
			otsAdrs.setOTSAddress(s + i);
			pKey = publicKeys.get(s + i);
			lAdrs.setOTSBit(false);
			lAdrs.setLTreeBit(true);
			lAdrs.setLTreeAddress(s + i);
			node = new XMSSNode(generateLTree(pKey, bSeed, lAdrs));
			hAdrs.setLTreeBit(false);
			hAdrs.setTreeHeight(0);
			hAdrs.setTreeIndex(i + s);
			saveNodeInfos(node, hAdrs.getTreeIndex());
			// if the stack is empty the first node will be put into the stack
			// if the current node and the next node on the stack have the same
			// height hash them and
			// put the new one back with height+1
			while (!stack.empty() && stack.peek().getHeight() == node.getHeight()) {
				hAdrs.setTreeIndex((hAdrs.getTreeIndex() - 1) / 2);
				node = new XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(), bSeed, hAdrs));
				hAdrs.setTreeHeight(hAdrs.getTreeHeight() + 1);
				node.setHeight(hAdrs.getTreeHeight());
				saveNodeInfos(node, hAdrs.getTreeIndex()); // save nodes on
															// higher heights
			}

			stack.push(node);
		}
		// result will be root of the tree or subtree
		return stack.pop();
	}

	public byte[] generateLTree(byte[][] pKey, byte[] seed, LTreeAddress adrs) {
		byte[][] pubKey = pKey.clone();
		int len = pubKey.length;
		Address lAdrs = adrs;
		lAdrs.setTreeHeight(0);

		while (len > 1) {
			for (int i = 0; i < Math.floor(len / 2.0); i = i + 1) {
				lAdrs.setTreeIndex(i);
				// zuck: Hashing der leaves/nodes
				pubKey[i] = rand_hash(pubKey[2 * i], pubKey[2 * i + 1], seed, lAdrs);
			}
			if (len % 2 == 1) {
				// zuck: Nachrücken der ungeraden Node
				pubKey[(int) (Math.floor(len / 2.0))] = pubKey[(int) len - 1];
			}
			// zuck: Anpassen der Anzahl an Nodes bzw. setzen der Anzahl der
			// Nodes auf der neuen
			// Höhe
			len = (int) Math.ceil((len / 2.0));
			lAdrs.setTreeHeight(lAdrs.getTreeHeight() + 1);
		}
		return pubKey[0];
	}

	/**
	 * XORs two nodes and hashes them with a salt
	 * 
	 * @param pKey
	 *            first node
	 * @param pKey2
	 *            second node
	 * @param seed
	 *            the seed for the bitmask generator
	 * @param adrs
	 *            lAdress construct used for bitmask generator
	 */
	public byte[] rand_hash(byte[] pKey, byte[] pKey2, byte[] seed, Address adrs) {

		int len = pKey.length;
		byte[] bitmk, key;
		byte[] message = ByteUtils.concatenate(pKey, pKey2);

		bitmk = generateBitmask(seed, adrs);

		adrs.setKeyBit(true);
		adrs.setBlockBit(false);
		key = XMSSTree.randomGenerator(seed, adrs.getAddress(), len);
		for (int i = 0; i < message.length; i++) {
			// XOR message with bitmask
			message[i] ^= bitmk[i];
		}
		byte[] tohash = ByteUtils.concatenate(key, message);
		return mDigest.digest(tohash);
	}

	/*
	 * @param seed
	 * 
	 * @param len length of half the bitmask
	 * 
	 * @param lAdrs the address construct
	 * 
	 * @return a bitmask
	 */
	public byte[] generateBitmask(byte[] seed, Address adrs) {
		byte[] bitmk_0, bitmk_1, bitmk;
		int len = otsAlgo.getLength();
		adrs.setKeyBit(false);
		adrs.setBlockBit(false);
		bitmk_0 = XMSSTree.randomGenerator(seed, adrs.getAddress(), len);
		adrs.setBlockBit(true);
		bitmk_1 = randomGenerator(seed, adrs.getAddress(), len);
		bitmk = ByteUtils.concatenate(bitmk_0, bitmk_1);
		return bitmk;
	}

	/**
	 * @author zuck PRNG used to generate the bitmasks and the key for hashing
	 * @param seed
	 *            seed for the PRNG
	 * @param address
	 *            address of left/right node
	 */
	public byte[] randomGenerator(byte[] seed, byte[] address, int len) {
		byte[] res = new byte[len + 32]; // erstellen des zu befüllenden arrays
		byte[] padding = new byte[32];
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// zuck: Der Algo existiert!
		}
		seed = ByteUtils.concatenate(padding, seed);
		seed = ByteUtils.concatenate(seed, address);
		res = hash.digest(seed);
		return res;
	}

	public void saveNodeInfos(Node node, int ix) {
		int index = 0;
		for (int i = 0; i < node.getHeight(); i++) {
			index += leafCounter / (1 << i);
		}
		if (node.getHeight() == 0) {
			node.setLeaf(true);
		}
		index = index + ix;
		node.setIndex(index);
		treeArray[index] = node;
		if (node instanceof XMSSNode) {
			((XMSSNode) node).setAuthPath(getTreeHeight());
		}
	}

	/**
	 * returns the height of the tree Tree with only one Node has height 0 Tree
	 * with 4 Nodes has height 2
	 */
	public int getTreeHeight() {
		return (int) MathUtils.log2nlz(leafCounter);
	}

	/**
	 * @author C Code by Hülsing, modelled in Java by Heimberger
	 * 
	 * Signs a message.
	 * Returns
	 * 1. an array containing the signature followed by the message AND
	 * 2. an updated secret key!
	 *
	 */
	int xmssmt_sign(BigInteger sig_msg, BigInteger sig_msg_len, String msg)
	{
	  long idx_tree;
	  int idx_leaf;
	  int i;
	  int idx_len=index_len;

	  // Init working params
	  byte R[]=new byte[n];
	  byte hash_key[]=new byte[3*n];
	  byte msg_h[]=new byte[n];
	  byte root[]=new byte[n];
	  byte ots_seed[]=new byte[n];
	  byte idx_bytes_32[]=new byte[32];

	  // Extract SK
	  int idx = 0;
	  byte[] sek=sk.getBytes();
	  for (i = 0; i < idx_len; i++) {
	    idx |= ((long)sek[i]) << 8*(idx_len - 1 - i);
	  }
	  
	  ByteArrayOutputStream sks=new ByteArrayOutputStream();
	  sks.write(sek, idx_len, n);	  
	  sk_seed=sks.toByteArray();
	  
	  ByteArrayOutputStream skp=new ByteArrayOutputStream();
	  skp.write(sek, (idx_len+n), n);
	  sk_prf=skp.toByteArray();
	  
	  ByteArrayOutputStream pus=new ByteArrayOutputStream();
	  pus.write(sek, (idx_len+2*n), n);
	  pub_seed=pus.toByteArray();
	  
	  
	  // Update SK
	  for (i = 0; i < idx_len; i++) {
	    sek[i] = (byte) (((idx + 1) >> 8*(idx_len - 1 - i)) & 255);
	  }
	  
	  // First compute pseudorandom value

	  byte []r=XMSSTree.randomGenerator(getSK_Seed(), message, message.length);	  
	  // Generate hash key (R || root || idx)
	  ByteArrayOutputStream hak=new ByteArrayOutputStream();
	  hak.write(R, 0, n);
  	  hak.write('|');
	  hak.write(sek, idx_len+3*n, n);
	  hash_key=hak.toByteArray();

	  // Then use it for message digest
	  byte []h_msg=XMSSTree.randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(idx).toByteArray(), r),
              msg.getBytes(), msg.length());

	  // Start collecting signature
//	  *sig_msg_len = 0;
	  byte []sigmsg=sig_msg.toByteArray();

	  // Copy index to signature
	  for (i = 0; i < idx_len; i++) {
	    sigmsg[i] = (byte) ((idx >> 8*(idx_len - 1 - i)) & 255);
	  }

	  ByteArrayOutputStream sigmessage=new ByteArrayOutputStream();
	  sigmessage.write(sigmsg);
  	  sigmessage.write('|');
	  sigmessage.write(idx_len);
  	  sigmessage.write('|');
	  sigmsg=sigmessage.toByteArray();
	  
//	  *sig_msg_len += idx_len;

	  // Copy R to signature
	  for (i=0; i < n; i++)
	    sigmsg[i] = R[i];

	  sigmessage.write(n);
	  sigmsg=sigmessage.toByteArray();

//	  *sig_msg_len += n;

	  // ----------------------------------
	  // Now we start to "really sign"
	  // ----------------------------------

	  // Handle lowest layer separately as it is slightly different...

 
	  OTSHashAddress ots_addr=new OTSHashAddress();
	  ots_addr.setOTSBit(true);
	  ots_addr.setOTSAddress(idx);
	  byte [][]sk_idx;
	((OTS) ots_addr).setPrivateKey(sk_idx);
	  byte [][]pk_idx;
	((OTS) ots_addr).setPublicKey(pk_idx);
	  
	  
	  //compute the WOTS+ signature happen
      byte[][] ots_sig = ((WOTSPlus) otsAlgo).sign(sigmsg, seed, ots_addr);

      int j;
      for (j = 1; j < params->d; j++) {
        // Prepare Address
        idx_leaf = (idx_tree & ((1 << tree_h)-1));
        idx_tree = idx_tree >> tree_h;
        setLayerADRS(ots_addr, j);
        setTreeADRS(ots_addr, idx_tree);
        setOTSADRS(ots_addr, idx_leaf);

        // Compute seed for OTS key pair
        get_seed(ots_seed, sk_seed, n, ots_addr);

        // Compute WOTS signature
        byte [][]wotssig=WOTSPlus.sign(sigmsg, root, ots_seed, w, pub_seed, ots_addr);

  	  ByteArrayOutputStream sigmessage=new ByteArrayOutputStream();
  	  sigmessage.write(sigmsg);
  	  sigmessage.write('|');
  	  sigmessage.write(w);
  	  sigmsg=sigmessage.toByteArray();
     
  //TODO
  	  compute_authpath_wots(root, sig_msg, idx_leaf, sk_seed, &(params->xmss_par), pub_seed, ots_addr);
        sigmsg += tree_h*n;
      //  *sig_msg_len += tree_h*n;
      }

	  ByteArrayOutputStream sigmessage=new ByteArrayOutputStream();
	  sigmessage.write(sigmsg);
	  sigmessage.write('|');
	  sigmessage.write(msg);
	  sigmsg=sigmessage.toByteArray();
	
	 // *sig_msg_len += msglen;

	  return sigmsg;
	}

	public byte[] getSK_Seed() {
		String[] splitted = xPrivKey.split("\\|"); // splits the xmss private
													// key in its components
		return splitted[1].getBytes(); // private key seed is always second
	}

}
