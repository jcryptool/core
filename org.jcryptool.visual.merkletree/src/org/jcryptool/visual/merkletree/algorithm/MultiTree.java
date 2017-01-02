package org.jcryptool.visual.merkletree.algorithm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.jcryptool.visual.merkletree.Descriptions.XMSS;
import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;
import org.omg.IOP.Encoding;

public class MultiTree implements ISimpleMerkle {
	// Starting over again
	int leafCounter = 0;
	int d;
	int l;
	int idx_len;
	int n;
	OTS otsAlgo;
	int h;
	int w;
	int index_len;
	int keyIndex;
	byte[] message;
	Node[] treeArray;
	byte[] sk_seed;
	byte[] sk_prf;
	byte[] pub_seed;
	// ArrayList<byte[][]> sek;
	byte[] sk;
	byte[] pk;
	// ArrayList<byte[][]> puk;
	boolean treeGenerated;

	ArrayList<Node> tree;

	byte[] seed;
	byte[] bitmaskSeed;
	MessageDigest mDigest;

	public ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();

	public void setLeafCount(int i) {
		leafCounter = i;
	}

	/*
	 * TODO LENA: Compute Autopath Key Management WOTS+
	 */

	@Override
	/**
	 * Selects the Signature Algorithm defautl Algorithm is the WOTSPlus
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
	 * } this.l = h / d; int i = 0; while (i < d) { } }
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
	 * @return trees on a layer
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
		String msg = message;
		int i;
		int idx_len = index_len;

		// Init working params
		byte R[] = new byte[n]; // pseudo-random value
		byte hash_key[] = new byte[3 * n]; // dunno, they are used
		byte msg_h[] = new byte[n]; // also used
		// byte root[] = new byte[n];
		// byte ots_seed[] = new byte[n];

		// byte idx_bytes_32[]=new byte[32];

		// Extract SK
		int idx = 0;
		byte[] sek = sk;
		for (i = 0; i < idx_len; i++) {
			idx |= ((long) sek[i]) << 8 * (idx_len - 1 - i);
		}

		// note: ByteArrayOutputStream is closed by the Garbage Collector, hence
		// no .close() is needed. The code of
		// close() in ByteArrayOutputStream itself is dead code (no
		// implementation is present).
		ByteArrayOutputStream sks = new ByteArrayOutputStream();
		sks.write(sek, idx_len, n);
		sk_seed = sks.toByteArray();

		ByteArrayOutputStream skp = new ByteArrayOutputStream();
		skp.write(sek, (idx_len + n), n);
		sk_prf = skp.toByteArray();

		ByteArrayOutputStream pus = new ByteArrayOutputStream();
		pus.write(sek, (idx_len + 2 * n), n);
		pub_seed = pus.toByteArray();

		// Update SK
		for (i = 0; i < idx_len; i++) {
			sek[i] = (byte) (((idx + 1) >> 8 * (idx_len - 1 - i)) & 255);
		}

		// First compute pseudorandom value

		R = XMSSTree.randomGenerator(getSK_Seed(), message, message.length());
		// Generate hash key (R || root || idx)
		ByteArrayOutputStream hak = new ByteArrayOutputStream();
		hak.write(R, 0, n);
		hak.write('|');
		hak.write(sek, idx_len + 3 * n, n);
		hash_key = hak.toByteArray();

		// Then use it for message digest
		msg_h = XMSSTree.randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(idx).toByteArray(), R),
				msg.getBytes(), msg.length());

		// Start collecting signature
		// *sig_msg_len = 0;
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

		// ----------------------------------
		// Now we start to "really sign"
		// ----------------------------------

		// Handle lowest layer separately as it is slightly different...

		OTSHashAddress ots_addr = new OTSHashAddress();
		ots_addr.setOTSBit(true);
		ots_addr.setOTSAddress(idx);
		byte[][] sk_idx = null;
		java.util.Arrays.fill(getIndex(sk.toString()), sk_idx[0][1]);
		((OTS) ots_addr).setPrivateKey(sk_idx);
		byte[][] pk_idx = null;
		((OTS) ots_addr).setPublicKey(pk_idx);

		// compute the WOTS+ signature
		byte[][] ots_sig = ((WOTSPlus) otsAlgo).sign(sigmsg, seed, ots_addr);

		// ByteArrayOutputStream sigmessage1 = new ByteArrayOutputStream();
		// try {
		// sigmessage1.write(sigmsg);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// sigmessage1.write('|');
		// sigmessage1.write(w);
		// sigmsg = sigmessage1.toByteArray();

		ArrayList<Node> auth = buildAuth(idx, seed);

		// ByteArrayOutputStream signmessage = new ByteArrayOutputStream();
		// try {
		// signmessage.write(sigmsg);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// signmessage.write(h * n);
		// signmessage.write('|');
		// try {
		// signmessage.write(msg.getBytes());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// sigmsg = signmessage.toByteArray();

		// *sig_msg_len += msglen;

		String signature = Integer.toString(idx) + "|" + Converter._byteToHex(R) + "|"
				+ Converter._2dByteToHex(ots_sig);
		for (i = 0; i < auth.size(); i++) {
			signature = signature + "|" + Converter._byteToHex(auth.get(i).getContent());
		}
		return signature;
	}

	// public void setIndex(String sk, int index) {
	// String[] splitted = sk.split("\\|"); // splits the xmss private key in
	// // its components
	// splitted[0] = Integer.toString(index); // index is the first part of the
	// // private key
	// this.sk = String.join("|", splitted);
	// keyIndex++;
	// }

	public byte[] getIndex(String s) {
		String[] splitted = s.split("\\|"); // splits the xmss private
		return Converter._stringToByte(splitted[0]);// private key seed is
													// always second
	}

	public byte[] getSK_Seed() {
		return this.sk_seed;
	}

	@Override
	public void setSeed(byte[] seed) {
		this.seed = seed;
	}

	@Override
	public byte[] getMerkleRoot() {
		return getRoot().getContent();
	}

	/**
	 * returns the root node of the MerkleTree
	 */
	public Node getRoot() {
		return treeHash(0, getTreeHeight(), seed);
	}

	@Override
	public byte[] getSeed() {
		return this.seed;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generateKeyPairsAndLeaves() {
		int i = 0;
		for (i = 0; i < sk.length; i++) {
			sk[i] = 0;
		}
		randomGenerator(sk_seed, sk, 3 * n);

		ByteArrayOutputStream pek = new ByteArrayOutputStream();
		pek.write(sk, 2 * n + idx_len, n);
		pk = pek.toByteArray();

		keyIndex = d - 1;
		treeHash(keyIndex, getTreeHeight(), seed);
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
		treeArray = new Node[(1 << (getTreeHeight() + 1)) - 1];
		treeHash(0, getTreeHeight(), bitmaskSeed);
		tree = new ArrayList<Node>(Arrays.asList(treeArray));
		setConnections();
		generateKeyPairsAndLeaves();
		treeGenerated = true;
	}

	@Override
	public boolean verify(String message, String signature) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verify(String message, String signature, int keyIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getSK(){
		String sek=sk.toString();
		return sek;
	}
	
	public String getPK(){
		String pek=pk.toString();
		return pek;
	}
	
	
	@Override
	public String getKeyLength() {
		int privLength = sk.length;
		int pubLength = pk.length;
		int length = privLength + pubLength;
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append(length);
		String keyLength = sb.toString();
		return keyLength;
	}

	@Override
	public void setWinternitzParameter(int w) {
		this.w = w;
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
     * @param SK XMSS secret key
     * @param s start index
     * @param t target node height
     * @param seed seed
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
            // if the current node and the next node on the stack have the same height hash them and
            // put the new one back with height+1
            while (!stack.empty() && stack.peek().getHeight() == node.getHeight()) {
                hAdrs.setTreeIndex((hAdrs.getTreeIndex() - 1) / 2);
                node = new XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(), bitmaskSeed, hAdrs));
                hAdrs.setTreeHeight(hAdrs.getTreeHeight() + 1);
                node.setHeight(hAdrs.getTreeHeight());
                saveNodeInfos(node, hAdrs.getTreeIndex()); // save nodes on higher heights
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
}
