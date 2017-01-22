package org.jcryptool.visual.merkletree.algorithm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;

public class MultiTree implements ISimpleMerkle {
	// Starting over again
	int leafCounter = 0;
	int d; // set
	int idx_len;
	int idx;
	int n;
	OTS otsAlgo;
	int h;
	int w;
	int keyIndex;
	byte[] message;
	Node[] treeArray;
	byte[] seed; //random value for the PRF
	byte[] sk_prf;
	byte[] sk = new byte[n]; //private(secret) key
	byte[] pk = new byte[n]; //public key
	boolean treeGenerated;
	OTSHashAddress otsAdrs = new OTSHashAddress();
	ArrayList<Node> tree;
	Node rootNode;

	byte[] bitmaskSeed;
	MessageDigest mDigest;

	public ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();
	public ArrayList<byte[][]> privKeys = new ArrayList<byte[][]>();

	public void setLeafCount(int i) {
		leafCounter = i;
	}

	@Override
	/**
	 * Selects the Signature Algorithm default Algorithm is the WOTSPlus
	 * Algorithm after the Algorithm is selected the method ->
	 * generateKeyPairsAndLeaves() is called
	 */
	public void selectOneTimeSignatureAlgorithm(String hash, String algo) {
		byte[] s = seed;
		this.otsAlgo = new WOTSPlus(w, hash, s);
		if (this.mDigest == null) {
			try {
				mDigest = MessageDigest.getInstance(hash);
			} catch (NoSuchAlgorithmException e) {
				// existiert eh
				e.printStackTrace();
			}
		}
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
		XMSSTree xtree = new XMSSTree();
		key = xtree.randomGenerator(seed, adrs.getAddress(), len);
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
		XMSSTree xtree = new XMSSTree();
		bitmk_0 = xtree.randomGenerator(seed, adrs.getAddress(), len);
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
	 * Generates an authentication path as array list with authentication nodes
	 * 
	 * @param i
	 *            index of the WOTS+ key pair
	 */
	public ArrayList<Node> buildAuth(int i, byte[] seed) {
		ArrayList<Node> auth = new ArrayList<Node>();
		for (int j = 0; j < getTreeHeight(); j++) {
			int k = ((int) Math.floor((double) i / (1 << j))) ^ 1;
			auth.add(j, treeHash((k * (1 << j)), j, seed));
		}
		return auth;
	}

	/**
	 * @author C Code by Hülsing, modelled in Java by Heimberger
	 * 
	 *         Signs a message. Returns 1. an array containing the signature
	 *         followed by the message AND 2. an updated secret key!
	 *
	 */
	public String sign(String message) {
		long idx_tree;
		int idx_leaf;
		if(keyIndex>=leafCounter) return "";
		String msg = message; //ERR leer
		int i;

		// Init working params
		byte R[] = new byte[n]; // pseudo-random value
		byte hash_key[] = new byte[3 * n]; // dunno, they are used
		byte msg_h[] = new byte[n]; // also used

		// Extract SK
		byte[] sek = sk;

		//ERR wird nicht betreten
		for (i = 0; i < idx_len; i++) {
			idx |= ((long) sek[i]) << 8 * (idx_len - 1 - i);
		}

		// note: ByteArrayOutputStream is closed by the Garbage Collector, hence
		// no .close() is needed. The code of
		// close() in ByteArrayOutputStream itself is dead code (no
		// implementation is present).
		ByteArrayOutputStream sks = new ByteArrayOutputStream();
		sks.write(sek, idx_len, n);
		seed = sks.toByteArray();

		ByteArrayOutputStream skp = new ByteArrayOutputStream();
		skp.write(sek, (idx_len + n), n);
		sk_prf = skp.toByteArray();

		ByteArrayOutputStream pus = new ByteArrayOutputStream();
		pus.write(sek, (idx_len + 2 * n), n);
		seed = pus.toByteArray();

		// Update SK
		for (i = 0; i < idx_len; i++) {
			sek[i] = (byte) (((idx + 1) >> 8 * (idx_len - 1 - i)) & 255);
		}

		// First compute pseudorandom value

		XMSSTree xtree = new XMSSTree();

		R = xtree.randomGenerator(getSK_Seed(), message, message.length());
		// Generate hash key (R || root || idx)
		ByteArrayOutputStream hak = new ByteArrayOutputStream();
		hak.write(R, 0, n);
		hak.write('|');
		hak.write(sek, idx_len + 3 * n, n);
		hash_key = hak.toByteArray();

		// Then use it for message digest
		msg_h = xtree.randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(idx).toByteArray(), R), msg.getBytes(),
				msg.length());

		// collecting signature
		byte[] sigmsg = msg_h;

		// Copy index to signature
		for (i = 0; i < idx_len; i++) {
			sigmsg[i] = (byte) ((idx >> 8 * (idx_len - 1 - i)) & 255);
		}

		ByteArrayOutputStream sigmessage = new ByteArrayOutputStream();
		try {
			sigmessage.write(sigmsg);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		sigmessage.write('|');
		sigmessage.write(idx_len);
		sigmsg = sigmessage.toByteArray();

		// Copy R to signature
		for (i = 0; i < n; i++)
			sigmessage.write(R[i]);

		sigmessage.write(n);
		sigmsg = sigmessage.toByteArray();

		// signing
		// set everything for the WOTS+ magic

		OTSHashAddress ots_addr = new OTSHashAddress();
		ots_addr.setOTSBit(true);
		ots_addr.setOTSAddress(this.keyIndex);
		otsAlgo.setPrivateKey(privKeys.get(keyIndex));
		otsAlgo.setPublicKey(publicKeys.get(keyIndex));
		
		// compute the WOTS+ signature
		byte[][] ots_sig = ((WOTSPlus) otsAlgo).sign(sigmsg, seed, ots_addr);

		ArrayList<Node>auth=buildAuth(keyIndex, seed);
		
		String signature = Integer.toString(keyIndex) + "|" + Converter._byteToHex(R) + "|"
				+ Converter._2dByteToHex(ots_sig);
		for (i = 0; i < auth.size(); i++) {
			signature = signature + "|" + Converter._byteToHex(auth.get(i).getContent());
		}
		
		int h=(int)getTreeHeight();
		//loop over remaining layers... 
//		for(i=1; i<d; i++){
//			idx_leaf=(int) (idx_tree&((1<<h)-1));
//			idx_tree= idx_tree >> h;
//			ots_addr.setOTSAddress(idx_leaf);
//			ots_addr.setOTSBit(true);
//			otsAlgo.setPrivateKey(privKeys.get(idx_leaf));
//			otsAlgo.setPublicKey(publicKeys.get(idx_leaf));
//			ots_sig=((WOTSPlus)otsAlgo).sign(sigmsg, seed, ots_addr);
//			signature+= "|" + Converter._2dByteToHex(ots_sig);
//			
//			for (int i = 0; i < auth.size(); i++) {
//				signature = signature + "|" + Converter._byteToHex(auth.get(i).getContent());
//			}
//			signature+="| Signatur "
//		}
//		
		if (keyIndex<(leafCounter-1)) keyIndex++;
		return signature;
	}

	public String getPrivateKey() {
		String sek = new String();
		sek = keyIndex + "|" + Converter._byteToHex(getSeed());
		for (int i = 0; i < leafCounter; i++) {
			sek += "|";
			sek += Converter._2dByteToHex(privKeys.get(i));
		}
		return sek;
	}

	public String getPublicKey() {
		String publicKeyString = Converter._byteToHex(rootNode.getContent()) + "|" + Converter._byteToHex(seed);
		return publicKeyString;
	}

	@Override
	public String getKeyLength() {
		// System.err.println(sk);
		int length = getPrivateKey().length() + getPublicKey().length();
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append((length / 2));
		String keyLength = sb.toString();
		return keyLength;
	}

	/**
	 * @author Lena returns number of trees on a certain layer
	 * 
	 * @param h
	 *            overall height of the tree
	 * @param d
	 *            layer on which the number of trees is searched
	 * @return trees on a layer
	 */

	public double getXMSSTreeCount(int h, int d) {
		if (keyIndex == (this.d - 1))
			return 1;
		else {
			double s = Math.pow(2, (h / d));
			return s;
		}
	}

	public byte[] getIndex(String s) {
	//	String[] splitted = s.split("\\|"); // splits the xmss private
	//	return Converter._stringToByte(splitted[0]);// private key seed is
		String indexString=String.valueOf(keyIndex);
		return indexString.getBytes();											// always second
	}

	public byte[] getSK_Seed() {
		return this.seed;
	}

	/**
	 * returns the root node of the MerkleTree
	 */
	@Override
	public byte[] getMerkleRoot() {
		return getRoot().getContent();
	}

	public Node getRoot() {
		return treeHash(0, getTreeHeight(), seed);
	}

	@Override
	public byte[] getSeed() {
		return this.seed;
	}

	public long getOverallLeaves() {
		return (long) Math.pow(2, h);
	}

	@Override
	public int getLeafCounter() {
		return leafCounter;
	}

	@Override
	public boolean isGenerated() {
		return treeGenerated;
	}

	@Override
	public Node getTreeLeaf(int treeLeaveNumber) {
		// Lena: brauch ma eh ned, oda?
		// weil interface
		return null;
	}

	public void generatePrivateKey() {
		SecureRandom r = new SecureRandom();
		r.nextBytes(sk);
	}

	@Override
	public void generateKeyPairsAndLeaves() {
		//generatePrivateKey();
		for (int i = 0; i < this.leafCounter; i++) {
			// generates a new WOTS/ WOTSPlus Keypair (public and secret key)
			if (otsAlgo instanceof WOTSPlus) {
				((WOTSPlus) otsAlgo).setAddress(otsAdrs);
			}

			this.otsAlgo.generateKeyPair();
			// adds the private Key of the generated keypair to the private key
			// list of privKeys
			privKeys.add(otsAlgo.getPrivateKey());
			publicKeys.add(otsAlgo.getPublicKey());

		}

		randomGenerator(seed, sk, 3 * n);

		// SecureRandom prf = new SecureRandom();
		ByteArrayOutputStream pek = new ByteArrayOutputStream();
		pek.write(sk, 2 * n + idx_len, n);
		pk = pek.toByteArray();

		keyIndex=0;
		//keyIndex = d - 1;
		// treeHash(keyIndex, getTreeHeight(), seed);
		System.arraycopy(pk, 0, sk, 3 * n + idx_len, n);

	}

	@Override
	public ArrayList<Node> getTree() {
		return this.tree;
	}

	/**
	 * Initialises the tree. Requires seed, bitmaskSeed and leafCounter to be
	 * set beforehand and then the execution of generateKeyPairsAndLeaves()
	 */
	public void generateMerkleTree() {
		generateKeyPairsAndLeaves();
		treeArray = new Node[(1 << (getTreeHeight() + 1)) - 1];
		rootNode = treeHash(0, getTreeHeight(), bitmaskSeed);
	//	System.err.println(rootNode.getContent().toString());
		tree = new ArrayList<Node>(Arrays.asList(treeArray));
		setConnections();
		treeGenerated = true;
	}

	@Override
	public boolean verify(String message, String signature) {
		// TODO
		// first hash message
		WOTSPlus wots = new WOTSPlus(w, signature, bitmaskSeed);
		byte[] msg = message.getBytes();
		byte[] hash = randomGenerator(seed, msg, message.length());

		// Validate WOTS Signature
		if (wots.verify(hash.toString(), signature) == false) {
			System.err.println("SIGNATURE VERIFYCATION FAILED: ABORT");
			return false;
		}
		// get PK
		byte[][] pek = wots.pkFromSig(signature, msg, seed, otsAdrs);

		return true;
	}

	@Override
	public boolean verify(String message, String signature, int keyIndex) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setIndex(int i){
		this.keyIndex=i;
		this.h=getTreeHeight();
		this.idx_len=(h+7)/8;
	}
	
	public void setSingleTreeHeight(int h){
		this.h=h;
	}
	
	public void setBitmaskSeed(byte[] seed){
		this.bitmaskSeed=seed;
	}
	
	@Override
	public void setSeed(byte[] seed) {
		this.seed = seed;
	}

	/**
	 * sets message and message length (n)
	 */
	public void setMessage(byte[] message) {
		this.message = message;
		this.n = message.length;
	}

	@Override
	public void setWinternitzParameter(int w) {
		this.w = w;
	}

	/**
	 * 
	 * @param d
	 *            nr of layers on the tree
	 */
	public void setLayers(int d) {
		this.d = d;
	}

	/**
	 * Fills the connections list in the node for the GUI
	 */
	public void setConnections() {
		Node left, right, parent;
		List<Node> connections;
		for (int i = 0; i < tree.size() - 1; i = i + 2) {
			left = tree.get(i);
			right = tree.get(i + 1);
			parent = tree.get(i / 2 + leafCounter);
			connections = parent.getConnectedTo();
			parent.setLeft(left);
			parent.setRight(right);
			left.setParent(parent);
			right.setParent(parent);
			connections.add(left);
			connections.add(right);
		}
	}

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

		Node node;
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
				pubKey[i] = rand_hash(pubKey[2 * i], pubKey[2 * i + 1], seed, lAdrs);
			}
			if (len % 2 == 1) {
				pubKey[(int) (Math.floor(len / 2.0))] = pubKey[(int) len - 1];
			}
			len = (int) Math.ceil((len / 2.0));
			lAdrs.setTreeHeight(lAdrs.getTreeHeight() + 1);
		}
		return pubKey[0];
	}

	@Override
	public int getKeyIndex() {
		return keyIndex;
	}

}
