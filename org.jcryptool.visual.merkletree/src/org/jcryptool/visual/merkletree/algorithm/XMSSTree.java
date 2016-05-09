package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;

public class XMSSTree implements ISimpleMerkle {

	// ArrayList<byte[]>pubKeys = new ArrayList<byte[]>();
	int leafCounter = 0;
	int leafNumber = 0;
	int keyIndex;
	byte[] seed;
	byte[] bitmaskSeed;
	boolean treeGenerated;
	OTS otsAlgo;
	HashTreeAddress hAdrs = new HashTreeAddress();
	OTSHashAddress otsAdrs = new OTSHashAddress();
	LTreeAddress lAdrs = new LTreeAddress();
	byte[] bitmask;
	String xPrivKey;
	String xPubKey;

	// ArrayList<MerkleTreeNode> leaves = new ArrayList<>();
	// ArrayList<ArrayList<MerkleTreeNode>> merkleTreeHeight;
	// ArrayList<MerkleTreeNode> merkleTreeRow = null;

	ArrayList<Node> tree;
	Node[] treeArray;
	ArrayList<Node> leaves = new ArrayList<Node>();

	MessageDigest mDigest;
	ArrayList<byte[][]> privKeys = new ArrayList<byte[][]>();
	ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();

	public void setLeafCount(int i) {
		leafCounter = i;
	}
	
	public void setSeed(byte[] seed) {
		this.seed = seed;
	}
	
	/**
	 * returns the root node of the MerkleTree
	 */
	public Node getRoot() {
		return treeHash(0, getTreeHeight(), seed);
	}

	@Override
	public ArrayList<Node> getTree() {
		return this.tree;
	}

	@Override
	/**
	 * @author Christoph Sonnberger
	 * returns the number of leafs
	 */
	public int getLeafCounter() {
		return leafCounter;
	}

	@Override
	public Node getTreeLeaf(int treeLeafNumber) {
		return leaves.get(treeLeafNumber);
	}

	//TODO add parameter pubKey, seed, adrs
	public byte[] generateLTree(byte[][] pKey, byte[] seed, LTreeAddress adrs) {
		byte[][] pubKey = pKey.clone();
		int len = pubKey.length;
		Address lAdrs = adrs;
		lAdrs.setTreeHeight(0);

		while (len > 1) {
			for (int i = 0; i < Math.floor(len / 2.0); i = i + 1) {
				lAdrs.setTreeIndex(i);
				//zuck: Hashing der leaves/nodes				
				pubKey[i] = rand_hash(pubKey[2 * i], pubKey[2 * i + 1], seed, lAdrs);
			}
			if (len % 2 == 1) {
				//zuck: Nachrücken der ungeraden Node 
				pubKey[(int) (Math.floor(len / 2.0))] = pubKey[(int) len -1 ];
			}
			//zuck: Anpassen der Anzahl an Nodes bzw. setzen der Anzahl der Nodes auf der neuen Höhe
			len = (int)Math.ceil((len / 2.0));
			lAdrs.setTreeHeight(lAdrs.getTreeHeight() + 1);
			}
		return pubKey[0];
	}

	public byte[] rand_hash(byte[] pKey, byte[] pKey2, byte[] seed, Address adrs) {
		
		int len = pKey.length;
		byte[] bitmk, key;
		byte[] message = ByteUtils.concatenate(pKey, pKey2);
		
		bitmk =generateBitmask(seed, adrs);		
		
		adrs.setKeyBit(true);
		adrs.setBlockBit(false);
		key = randomGenerator(seed, adrs.getAddress(), len);
		for (int i = 0; i < message.length; i++) {
			//XOR message with bitmask
			message[i] ^= bitmk[i];
		}
		byte[] tohash = ByteUtils.concatenate(key, message);
		return mDigest.digest(tohash);
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
		
		Node node; //TODO make new Node class
		Stack<Node> stack = new Stack<Node>();
		byte[][] pKey;
		OTSHashAddress otsAdrs = new OTSHashAddress();
		LTreeAddress lAdrs = new LTreeAddress();
		HashTreeAddress hAdrs = new HashTreeAddress();
		
		if(s % (1 << t) != 0){
			return null;
		}		
		
		for( int i = 0; i < (1 << t); i++) { // i < 2^t
			otsAdrs.setOTSBit(true);
			otsAdrs.setOTSAddress(s+i);
			//pKey = WOTS_genPK(privKeys.get(s+i), seed, otsAdrs); //TODO implement WOTS_genPK or change wots+; return byte[][]
			pKey = publicKeys.get(s+i);
			lAdrs.setOTSBit(false);
			lAdrs.setLTreeBit(true);
			lAdrs.setLTreeAddress(s+i);
			node = new XMSSNode(generateLTree(pKey, bitmaskSeed, lAdrs));
			hAdrs.setLTreeBit(false);
			hAdrs.setTreeHeight(0);
			hAdrs.setTreeIndex(i+s);
			saveNodeInfos(node, hAdrs.getTreeIndex());
			//if the stack is empty the first node will be put into the stack
			//if the current node and the next node on the stack have the same height hash them and put the new one back with height+1			
			while(!stack.empty() && stack.peek().getHeight() == node.getHeight()) {					
				hAdrs.setTreeIndex((hAdrs.getTreeIndex() -1) / 2);
				node = new XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(), bitmaskSeed, hAdrs));
				hAdrs.setTreeHeight(hAdrs.getTreeHeight() + 1);
				node.setHeight( hAdrs.getTreeHeight());
				saveNodeInfos(node, hAdrs.getTreeIndex()); //save nodes on higher heights
			}
			
			
			stack.push(node);
		}
		//result will be root of the tree or subtree
		return stack.pop();
	}

	//TODO Änderungen anpassen
	@Override
	public void generateMerkleTree() {
		treeArray = new Node[(1 << (getTreeHeight()+1)) - 1];
		treeHash(0, getTreeHeight(), bitmaskSeed);
		tree = new ArrayList<Node>(Arrays.asList(treeArray));
		setConnections();
		xmss_genPK();
		xmss_genSK();
		treeGenerated = true;
	}
	
	/**
	 * returns the height of the tree
	 * Tree with only one Node has height 0
	 * Tree with 4 Nodes has height 2
	 */
	public int getTreeHeight() {
		return (int)MathUtils.log2nlz(leafCounter);
	}

	@Override
	/**
	 * Selects the Signature Algorithm
	 * defautl Algorithm is the WOTSPlus Algorithm
	 * after the Algorithm is selected the method -> generateKeyPairsAndLeaves() is called
	 */
	public void selectOneTimeSignatureAlgorithm(String hash, String algo) {
		switch (algo) {
		case "WOTS":
			this.otsAlgo = new WinternitzOTS(16, hash);
			break;
		case "WOTSPlus":
			this.otsAlgo = new WOTSPlusXMSS(16, hash, seed);
			break;
		default:
			this.otsAlgo = new WOTSPlusXMSS(16, hash, seed);
			break;
		}
		if (this.mDigest == null) {
			try {
				mDigest = MessageDigest.getInstance(hash);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	/**
	 * signs the given Message String using the the defined OTS Algorithm
	 * returns the Signature
	 */
	public String sign(String message) {
		String tmpSignature;
		int index = getIndex(xPrivKey);
		ArrayList<Node> auth = buildAuth(index, seed);
		byte[] r = randomGenerator(getSK_Seed(),message, message.length());
		//index || r as seed for hashing the message
		byte[] hashedMessage = randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(index).toByteArray(),r), message.getBytes(), message.length());
		OTSHashAddress otsAdrs = new OTSHashAddress();		
		otsAdrs.setOTSBit(true);
		otsAdrs.setOTSAddress(index);
		otsAlgo.setPrivateKey(privKeys.get(index));
		otsAlgo.setPublicKey(publicKeys.get(index));
		byte[][] ots_sig = ((WOTSPlusXMSS) otsAlgo).sign(hashedMessage, seed, otsAdrs);		
		String signature = Integer.toString(index) + "|" + Converter._byteToHex(r) + "|" + Converter._2dByteToHex(ots_sig);
		for(int i = 0; i < auth.size(); i++){
			signature = signature + "|" + Converter._byteToHex(auth.get(i).getContent());
		}
		setIndex(xPrivKey, index + 1);
		return signature;		
		
	}

	
	@Override
	/**
	 * Verifys the Signature and return true or false
	 */
	public boolean verify(String message, String signature) {
		XMSSNode node = rootFromSig(message, signature);
		if(node.equals(getRoot())){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	/**
	 * Verifys the Signature of an given index and return true or false
	 */
	public boolean verify(String message, String signature, int markedLeafIndex) {
		String[] signer = signature.split("\\|");
		int keyIndex = Integer.parseInt(signer[0]);
		if(markedLeafIndex != keyIndex) {
			return false;
		}else {
			return this.verify(message, signature);
		}
	}

	public void generateKeyPairsAndLeaves() {
		for (int i = 0; i < this.leafCounter; i++) {
			//generates a new WOTS/ WOTSPlus Keypair (public and secret key)
			if(otsAlgo instanceof WOTSPlusXMSS){
				((WOTSPlusXMSS) otsAlgo).setAddress(otsAdrs);
			}
			this.otsAlgo.generateKeyPair();
			//adds the private Key of the generated keypair to the private key list of privKeys
			this.privKeys.add(this.otsAlgo.getPrivateKey());
			this.publicKeys.add(this.otsAlgo.getPublicKey());
		}
	}

	@Override
	/**
	 * Returns if the tree is already generated
	 */
	public boolean isGenerated() {
		return treeGenerated;
	}
	
	/**
	 * @author zuck
	 * PRNG used to generate the bitmasks and the key for hashing
	 * @param seed
	 * 		seed for the PRNG
	 * @param address
	 * 		address of left/right node
	 */
	public byte[] randomGenerator(byte[] seed, byte[] address, int len) {
		byte[] res = new byte[len+32];	//erstellen des zu befüllenden arrays
		byte[] padding = new byte[32];
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException e) {
			//zuck: Der Algo existiert!
		}
		seed = ByteUtils.concatenate(padding, seed);
		seed = ByteUtils.concatenate(seed, address);
		res = hash.digest(seed);
		return res;
	}
	
	/**
	 * Generates the bitmask if not set by user
	*/
	public XMSSNode rootFromSig(String message, String signature){
		String[] splitted = signature.split("\\|");	//split the signature in its components
		byte[] r = Converter._hexStringToByte(splitted[1]);	//seed is always second in signature
		int index = Integer.parseInt(splitted[0]);	//index is always first in signature
		OTSHashAddress otsAdrs = new OTSHashAddress();
		LTreeAddress lAdrs = new LTreeAddress();
		byte[][] pk_ots;
		WOTSPlusXMSS wots = (WOTSPlusXMSS) this.otsAlgo;
		//index || r as seed for hashing the message
		byte[] hashedMessage = randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(index).toByteArray(),r), message.getBytes(), message.length());
		otsAdrs.setOTSBit(true);
		otsAdrs.setOTSAddress(index);
		wots.setPrivateKey(privKeys.get(index));
		wots.setPublicKey(publicKeys.get(index));
		pk_ots = wots.pkFromSig(splitted[2], hashedMessage, seed, otsAdrs);
		lAdrs.setOTSBit(false);
		lAdrs.setLTreeBit(true);
		lAdrs.setLTreeAddress(index);
		XMSSNode[] node = new XMSSNode[2];
		node[0] = new XMSSNode(generateLTree(pk_ots, bitmaskSeed, lAdrs));
		lAdrs.setLTreeBit(false);
		lAdrs.setTreeIndex(index);
		for(int k = 0; k < getTreeHeight(); k++) {
			lAdrs.setTreeHeight(k);
			if(Math.floor((double)index / (1<<k)) % 2 == 0) {
				lAdrs.setTreeIndex(lAdrs.getTreeIndex() / 2);
				node[1] = new XMSSNode(rand_hash(node[0].getContent(), Converter._hexStringToByte(splitted[3+k]), bitmaskSeed, lAdrs));	//splitted[3] is auth[0] in signature				
			} else {
				node[1] = new XMSSNode(rand_hash(Converter._hexStringToByte(splitted[3+k]), node[0].getContent(), bitmaskSeed, lAdrs));
			}
			node[0] = node[1];
		}
		return node[0];
		
	}
	
	/*
	 * @param seed
	 * @param len	length of half the bitmask
	 * @param lAdrs	the address construct
	 * @return	a bitmask
	 */
	public byte[] generateBitmask(byte[] seed, Address adrs){
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
	 * Generates the public key of the XMSS
	 */
	public void xmss_genPK() {
		Node root;
		byte[] seed;
		seed = bitmaskSeed;
		
		
		root = treeHash(0, getTreeHeight(), seed);
		xPubKey = Converter._byteToHex(root.getContent()) + "|" + Converter._byteToHex(seed);
	}
	
	/**
	 * Generates an authentication path as array list with authentication nodes
	 * @param i	index of the WOTS+ key pair
	 */
	public ArrayList<Node> buildAuth(int i,byte[] seed) {
		ArrayList<Node> auth = new ArrayList<Node>();
		for(int j = 0; j < getTreeHeight(); j++) {
			int k = ((int)Math.floor((double)i / (1 << j))) ^ 1;
			auth.add(j,treeHash(k* (1 << j),j, seed));
		}
		return auth;
	}
	
	public void xmss_genSK() {
		int index = 0; //index of the next unused wots+ key
		byte[] key;	//random key for PRNG
		byte[][] iterator;
		xPrivKey = Integer.toString(index) + "|" + Converter._byteToHex(seed);
		for(int i = 0; i < privKeys.size(); i++) {			
			iterator = privKeys.get(i); //gets the i-th n byte wots+ priv key
			xPrivKey += "|";
			for(int j = 0; j < iterator[i].length; j++) {
				xPrivKey += Converter._byteToHex(iterator[i]); 
			}
		}		
	}
	
	public int getIndex(String xPrivKey) {
		String[] splitted = xPrivKey.split("\\|");	//splits the xmss private key in its components
		int index = Integer.parseInt(splitted[0]);	//index is the first part of the private key
		return index;
	}
	
	public byte[] randomGenerator(byte[] seed, String message, int len) {
		byte[] res = new byte[len+32];	//erstellen des zu befüllenden arrays
		byte[] padding = new byte[32];
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException e) {
			//zuck: Der Algo existiert!
		}
		//nach hulsing mit padding
		seed = ByteUtils.concatenate(padding, seed);
		seed = ByteUtils.concatenate(seed, message.getBytes());
		res = hash.digest(seed);
		return res;
	}
	
	public void setIndex(String xPrivKey, int index){
		String[] splitted = xPrivKey.split("\\|");	//splits the xmss private key in its components
		splitted[0]	= Integer.toString(index);	//index is the first part of the private key
		this.xPrivKey = String.join("|",splitted);
		keyIndex++;
	}
	
	public byte[] getSK_Seed() {
		String[] splitted = xPrivKey.split("\\|");	//splits the xmss private key in its components
		return splitted[1].getBytes();	//private key seed is always second
	}
	@Override
	public byte[] getMerkleRoot() {
		return getRoot().getContent();
	}
	
	public boolean checkBitmask(byte[] bitmask){
		if(bitmask.length == otsAlgo.getLength()) {
			return true;
		}else{
			return false;
		}
	}
	
	public void saveNodeInfos(Node node, int ix) {	
		int index = 0;
		for(int i = 0; i < node.getHeight(); i++){
			index += leafCounter / (1 << i);
		}
		if(node.getHeight() == 0){
			node.setLeaf(true);
		}
		index = index + ix;
		node.setIndex(index);
		treeArray[index] = node;
		if(node instanceof XMSSNode){
			((XMSSNode)node).setAuthPath(getTreeHeight());
		}
	}
		
	public void setNeighbors(){
		Node left, right;
		for(int i = 0; i < tree.size() -1; i = i+2){				
					left = tree.get(i);
					right = tree.get(i+1);
					left.setLeft(left);
					left.setRight(right);
					right.setLeft(left);
					right.setRight(right);
			}
	}
	
	public void setConnections(){
		Node left, right, parent;
		List<Node> connections;
		for(int i = 0; i < tree.size()-1; i = i+2) {			
			left = tree.get(i);
			right = tree.get(i+1);
			parent = tree.get(i/2 + leafCounter);
			connections = parent.getConnectedTo();
			parent.setLeft(left);
			parent.setRight(right);
			left.setParent(parent);
			right.setParent(parent);
			connections.add(left);
			connections.add(right);
			
		}
	}
	
	public void setBitmaskSeed(byte[] seed) {
		bitmaskSeed = seed;
	}

	public byte[] getBitmaskSeed() {
		return bitmaskSeed;
	}
	
	@Override
	public byte[] getSeed() {
		return seed;
	}
}
