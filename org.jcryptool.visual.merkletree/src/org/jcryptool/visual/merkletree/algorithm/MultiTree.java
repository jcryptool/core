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

public abstract class MultiTree implements ISimpleMerkle {
	// Starting over again
    int leafCounter = 0;	
	int d;
	int l;
	OTS otsAlgo;
	int h;
	int w;
	String message;
    Node[] treeArray;


	byte[] seed;
    byte[] bitmaskSeed;
	MessageDigest mDigest;

	public ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();

	
    public void setLeafCount(int i) {
        leafCounter = i;
    }
    
    
	@Override
	/**
	 * Selects the Signature Algorithm defautl Algorithm is the WOTSPlus
	 * Algorithm after the Algorithm is selected the method ->
	 * generateKeyPairsAndLeaves() is called
	 */
	public void selectOneTimeSignatureAlgorithm(String hash, String algo) {
		this.otsAlgo = new WOTSPlus(w, hash, seed);
		if (this.mDigest == null) {
			try {
				mDigest = MessageDigest.getInstance(hash);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	public void TreeParams(int d, int h, int w, String message) {
		this.otsAlgo = new WOTSPlus(w, message, getSeed()); // wots+ has the
		// length
		// from message and not
		// from hash
		// winternitz parameter stays the same for every tree (duh)

		if (h % d != 0) {
			/*
			 * müssen teilerfremd sein throw hDevidesDwithRemainderException(){
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

	// nr of trees for the hypertree klickibunti

	// public String xPrivKey;
	// public String xPubKey;
	/*
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

	/*
	 * void setLayers(int d) { this.d = d; }
	 */
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
	String algorithmID = this.algorithmID;
	byte[] publicRoot = Layer.getMerkleRoot();
	byte[] publicSeed = this.getSeed();


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
	 * returns the height of the tree Tree with only one Node has height 0 Tree
	 * with 4 Nodes has height 2
	 *
	 * public int getTreeHeight() { return (int) MathUtils.log2nlz(leafCounter);
	 * }
	 */
	/**
	 * @author zuck
	 * @param SK
	 *            XMSS secret key
	 * @param s
	 *            start index
	 * @param t
	 *            target node height
	 * @param seed
	 *            seed
	 * @return root node of a tree of height t
	 *
	 *         public Node treeHash(int s, int t, byte[] seed) {
	 * 
	 *         Node node; // TODO make new Node class Stack<Node> stack = new
	 *         Stack<Node>(); byte[][] pKey; OTSHashAddress otsAdrs = new
	 *         OTSHashAddress(); LTreeAddress lAdrs = new LTreeAddress();
	 *         HashTreeAddress hAdrs = new HashTreeAddress();
	 * 
	 *         if (s % (1 << t) != 0) { return null; }
	 * 
	 *         for (int i = 0; i < (1 << t); i++) { // i < 2^t
	 *         otsAdrs.setOTSBit(true); otsAdrs.setOTSAddress(s + i); pKey =
	 *         publicKeys.get(s + i); lAdrs.setOTSBit(false);
	 *         lAdrs.setLTreeBit(true); lAdrs.setLTreeAddress(s + i); node = new
	 *         XMSSNode(generateLTree(pKey, bitmaskSeed, lAdrs));
	 *         hAdrs.setLTreeBit(false); hAdrs.setTreeHeight(0);
	 *         hAdrs.setTreeIndex(i + s); saveNodeInfos(node,
	 *         hAdrs.getTreeIndex()); // if the stack is empty the first node
	 *         will be put into the stack // if the current node and the next
	 *         node on the stack have the same height hash them and // put the
	 *         new one back with height+1 while (!stack.empty() &&
	 *         stack.peek().getHeight() == node.getHeight()) {
	 *         hAdrs.setTreeIndex((hAdrs.getTreeIndex() - 1) / 2); node = new
	 *         XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(),
	 *         bitmaskSeed, hAdrs)); hAdrs.setTreeHeight(hAdrs.getTreeHeight() +
	 *         1); node.setHeight(hAdrs.getTreeHeight()); saveNodeInfos(node,
	 *         hAdrs.getTreeIndex()); // save nodes on higher heights }
	 * 
	 *         stack.push(node); } // result will be root of the tree or subtree
	 *         return stack.pop(); }
	 */
	/**
	 * Generates the public key of the XMSS
	 * 
	 * public void xmss_genPK() { Node root; byte[] seed; seed =
	 * getPublicSeed();
	 * 
	 * root = treeHash(0, getTreeHeight(), seed); xPubKey =
	 * Converter._byteToHex(root.getContent()) + "|" +
	 * Converter._byteToHex(seed); }
	 * 
	 * public void xmss_genSK() { int index = 0; // index of the next unused
	 * wots+ key xPrivKey = Integer.toString(index) + "|" +
	 * Converter._byteToHex(seed); for (int i = 0; i < privKeys.size(); i++) {
	 * xPrivKey += "|"; xPrivKey += Converter._2dByteToHex(privKeys.get(i)); } }
	 * 
	 * byte [] getPublicSeed(){ return this.PK_Seed; }
	 */

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
	/*
	 * static void treehash(byte [] node, int height, int index, byte []SK_Seed,
	 * params params, char []pub_seed, int []addr) {
	 * 
	 * int idx = index; byte[] n = params.getN(params); // use three different
	 * addresses because at this point we use all three formats in parallel int
	 * [] ots_addr=new int[8]; int []ltree_addr=new int[8]; int node_addr[]=new
	 * int[8]; // only copy layer and tree address parts memcpy(ots_addr, addr,
	 * 12); // type = ots setType(ots_addr, 0); memcpy(ltree_addr, addr, 12);
	 * setType(ltree_addr, 1); memcpy(node_addr, addr, 12); setType(node_addr,
	 * 2);
	 * 
	 * int lastnode, i; char stack[]=new char[(height+1)*n]; int
	 * stacklevels[]=new int[height+1]; int stackoffset=0;
	 * 
	 * lastnode = idx+(1 << height);
	 * 
	 * for (; idx < lastnode; idx++) { setLtreeADRS(ltree_addr, idx);
	 * setOTSADRS(ots_addr, idx); gen_leaf_wots(stack+stackoffset*n, sk_seed,
	 * params, pub_seed, ltree_addr, ots_addr); stacklevels[stackoffset] = 0;
	 * stackoffset++; while (stackoffset>1 && stacklevels[stackoffset-1] ==
	 * stacklevels[stackoffset-2]) { setTreeHeight(node_addr,
	 * stacklevels[stackoffset-1]); setTreeIndex(node_addr, (idx >>
	 * (stacklevels[stackoffset-1]+1))); hash_h(stack+(stackoffset-2)*n,
	 * stack+(stackoffset-2)*n, pub_seed, node_addr, n);
	 * stacklevels[stackoffset-2]++; stackoffset--; } } for (i=0; i < n; i++)
	 * node[i] = stack[i]; }
	 * 
	 */

	/**
	 * @author zuck
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
			node = new XMSSNode(generateLTree(pKey, bitmaskSeed, lAdrs));
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
				node = new XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(), bitmaskSeed, hAdrs));
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
     * @param pKey first node
     * @param pKey2 second node
     * @param seed the seed for the bitmask generator
     * @param adrs lAdress construct used for bitmask generator
     */
    public byte[] rand_hash(byte[] pKey, byte[] pKey2, byte[] seed, Address adrs) {

        int len = pKey.length;
        byte[] bitmk, key;
        byte[] message = ByteUtils.concatenate(pKey, pKey2);

        bitmk = generateBitmask(seed, adrs);

        adrs.setKeyBit(true);
        adrs.setBlockBit(false);
        key = randomGenerator(seed, adrs.getAddress(), len);
        for (int i = 0; i < message.length; i++) {
            // XOR message with bitmask
            message[i] ^= bitmk[i];
        }
        byte[] tohash = ByteUtils.concatenate(key, message);
        return mDigest.digest(tohash);
    }
    
    
    /*
     * @param seed
     * @param len length of half the bitmask
     * @param lAdrs the address construct
     * @return a bitmask
     */
    public byte[] generateBitmask(byte[] seed, Address adrs) {
        byte[] bitmk_0, bitmk_1, bitmk;
        int len = otsAlgo.getLength();
        adrs.setKeyBit(false);
        adrs.setBlockBit(false);
        bitmk_0 = randomGenerator(seed, adrs.getAddress(), len);
        adrs.setBlockBit(true);
        bitmk_1 = randomGenerator(seed, adrs.getAddress(), len);
        bitmk = ByteUtils.concatenate(bitmk_0, bitmk_1);
        return bitmk;
    }


    /**
     * @author zuck PRNG used to generate the bitmasks and the key for hashing
     * @param seed seed for the PRNG
     * @param address address of left/right node
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
     * returns the height of the tree Tree with only one Node has height 0 Tree with 4 Nodes has
     * height 2
     */
    public int getTreeHeight() {
        return (int) MathUtils.log2nlz(leafCounter);
    }
    

}
