package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.jcryptool.visual.merkletree.Descriptions.XMSS_MT;
import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;

public class XMSSTree implements ISimpleMerkle {

	// ArrayList<byte[]>pubKeys = new ArrayList<byte[]>();
	int leafCounter = 0;
	int leafNumber = 0;
	int keyIndex;
	byte[] privateSeed;
	byte[] publicSeed;
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

	/**
	 * Constructor for XMSS-Tree
	 * @param privateSeed
	 * @param publicSeed	set to null if no user input
	 * @param leafCounter -> Anzahl der Blätter des Baums
	 *
	XMSSTree(byte[] privateSeed, byte[] publicSeed, int leafCounter, byte[] bitmask) { //privateSeed unnötig? bleiben bis auf weiters bei publicSeed
	}
	/**
	 * Constructor for XMSS-Tree
	 * no private and public Seed set -> use default values
	 * @param keyLength
	 * @param leafCounter
	 *

	XMSSTree(int leafCounter) {
		this.leafCounter = leafCounter;
		this.treeGenerated = false;
	}
	*/
	
	public void setLeafCount(int i) {
		leafCounter = i;
	}
	
	public void setPublicSeed(byte[] seed) {
		publicSeed = seed;
	}

	@Override
	/**
	 * Set the private seed
	 */
	public void addPrivateSeed(byte[] privateSeed) {
		this.privateSeed = privateSeed;

	}
	/**
	 * Set the public seed
	 */
	@Override
	public void addPublicSeed(byte[] publicSeed) {
		this.publicSeed = publicSeed;
	}

	@Override
	/**
	 * @author christoph
	 * Adds a new Leaf to the leaf ArrayList
	 * leafNode Name is the Content
	 * leafNode Code is the Public Key
	 * leafNumber is increased by 1
	 */
	public void addTreeLeaf(byte[] LeafContent, String pubKey) {
		Node leafNode = new SimpleNode(LeafContent, true, ++this.leafNumber);
		leafNode.setCode(pubKey);
		leaves.add(leafNode);
	}

	
	/**
	 * returns the root node of the MerkleTree
	 */
	public Node getRoot() {
		return treeHash(0, getTreeHeight(), publicSeed);
	}

	@Override
	public byte[] getPrivateSeed() {
		return privateSeed;
	}

	@Override
	public ArrayList<Node> getTree() {
		return this.tree;
	}

	@Override
	public byte[] getPublicSeed() {
		return publicSeed; //dummy wert
		//return publicSeed;

	}

	@Override
	/**
	 * @author christoph
	 * returns the number of leafs
	 */
	public int getLeafCounter() {
		return leafCounter;
	}

	@Override
	public Node getTreeLeaf(int treeLeafNumber) {
		return leaves.get(treeLeafNumber);
	}

	@Override
	/**
	 * @author christoph
	 * Returns the content of an given node index
	 * getName() -> Content is stored in the name field of the array list
	 */
	public byte[] getNodeContentbyIndex(int index) {
		return tree.get(index).getContent();
	}

	//TODO add parameter pubKey, seed, adrs
	public byte[] generateLTree(byte[][] pubKey, byte[] seed, LTreeAddress adrs) {
		int len = pubKey.length;
		Address lAdrs = adrs;
		lAdrs.setTreeHeight(0);

		while (len > 1) {
			for (int i = 0; i < Math.floor(len / 2); i = i + 1) {
				lAdrs.setTreeIndex(i);
				//zuck: Hashing der leaves/nodes				
				pubKey[i] = rand_hash(pubKey[2 * i], pubKey[2 * i + 1], seed, lAdrs);
			}
			if (len % 2 == 1) {
				//zuck: Nachrücken der ungeraden Node 
				pubKey[(int) (Math.floor(len / 2))] = pubKey[(int) len -1 ];
			}
			//zuck: Anpassen der Anzahl an Nodes bzw. setzen der Anzahl der Nodes auf der neuen Höhe
			len = (int)Math.ceil((len / 2));
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
		//Formatiert den ksecret und message zu einem 512 Byte hexadezimalen Wert
		String tohash = String.format("%512s", (key.toString() + message.toString()));
		return mDigest.digest(tohash.getBytes());
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
			node = new XMSSNode(generateLTree(pKey, seed, lAdrs));
			hAdrs.setLTreeBit(false);
			hAdrs.setTreeHeight(0);
			hAdrs.setTreeIndex(i+s);
			saveNodeInfos(node, hAdrs.getTreeIndex());
			//if the stack is empty the first node will be put into the stack
			//if the current node and the next node on the stack have the same height hash them and put the new one back with height+1			
			while(!stack.empty() && stack.peek().getHeight() == node.getHeight()) {					
				hAdrs.setTreeIndex((hAdrs.getTreeIndex() -1) / 2);
				node = new XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(), seed, hAdrs));
				hAdrs.setTreeHeight(hAdrs.getTreeHeight() + 1);
				node.setHeight( hAdrs.getTreeHeight());
				saveNodeInfos(node, hAdrs.getTreeIndex()); //save nodes on higher heights
			}
			
			
			stack.push(node);
		}
		//result will be root of the tree or subtree
		return stack.pop();
		/*
		String tohash = String.format("%1024s", (ksecret.toString() + message.toString()));
		return mDigest.digest(tohash.getBytes());
		*/
	}

	//TODO Änderungen anpassen
	@Override
	public void generateMerkleTree() {
		treeArray = new Node[(1 << (getTreeHeight()+1)) - 1];
		treeHash(0, getTreeHeight(), publicSeed);
		tree = new ArrayList<Node>(Arrays.asList(treeArray));
		setConnections();
		xmss_genPK();
		xmss_genSK();
		treeGenerated = true;
		/**
		 * Generate the leafs
		 *
		
		//creates a new node list for the tree
		tree = new ArrayList<Node>();

		//leafcounter is defined in the constructor -> add the amount of defined leafs
		for (int c = 0; c < this.leafCounter; c++) {
			//for every tree leaf add an lTreeRoot
			this.addTreeLeaf(this.generateLTree(c), "L-Tree-Keys");
		}
		
		/**
		 * Generate the tree
		 *
		int height = getTreeHeight();
		if (height == 0) {
			return;
		}

		Node helperNode;
		ArrayList<Node> treeLevel = new ArrayList<Node>();
		int index = 0;
		int levelCount;
		int NodeLevelCounter = tree.size();
		int treeIndex = 0;
		for (levelCount = 0; levelCount < height - 1; levelCount++) {
			treeLevel = new ArrayList<Node>();
			NodeLevelCounter = (int) Math.round(NodeLevelCounter / 2.0);

			for (index = 0; index < NodeLevelCounter; index++, treeIndex += 2) {
				if (treeIndex + 1 <= tree.size()) {
					byte[] content = hashNode(tree.get(treeIndex).getName(), tree.get(treeIndex + 1).getName(), null,
							this.publicSeed);
					helperNode = new Node(content, tree.get(treeIndex), tree.get(treeIndex + 1));
					treeLevel.add(helperNode);
					tree.get(treeIndex).setParent(treeLevel.get(index));
					tree.get(treeIndex + 1).setParent(treeLevel.get(index));
				} else {
					byte[] content = hashNode(tree.get(treeIndex).getName(), tree.get(treeIndex).getName(), null,
							this.publicSeed);
					helperNode = new Node(content, false, 0);
					helperNode.setLeft(tree.get(treeIndex));
					treeLevel.add(helperNode);
					tree.get(treeIndex).setParent(treeLevel.get(index));
				}

			}
			treeIndex = tree.size();
			tree.addAll(treeLevel);
		}

		//set the treeGenerated to true -> needed for method isGenerated()
		treeGenerated = true;
	}

	byte[] hashingContent(MerkleTreeNode a, MerkleTreeNode b) {
		byte[] toHash = ByteUtils.concatenate(a.getContent(), b.getContent());

		return mDigest.digest(toHash);
		*/
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
	public void selectHashAlgorithm(String hAlgo) {
		try {
			mDigest = MessageDigest.getInstance(hAlgo);
		} catch (NoSuchAlgorithmException e) {
			// TODO Do stuff with it!
			e.printStackTrace();
			try {
				mDigest = MessageDigest.getInstance("SHA256");
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

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
			this.otsAlgo = new WOTSPlusXMSS(16, hash, publicSeed);
			break;
		default:
			this.otsAlgo = new WOTSPlusXMSS(16, hash, publicSeed);
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
		ArrayList<Node> auth = buildAuth(index, publicSeed);
		byte[] r = randomGenerator(getSK_Seed(),message, message.length());
		//index || r as seed for hashing the message
		byte[] hashedMessage = randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(index).toByteArray(),r), message.getBytes(), message.length());
		OTSHashAddress otsAdrs = new OTSHashAddress();		
		otsAdrs.setOTSBit(true);
		otsAdrs.setOTSAddress(index);
		byte[][] ots_sig = ((WOTSPlusXMSS) otsAlgo).sign(hashedMessage, publicSeed, otsAdrs);		
		String signature = Integer.toString(index) + "|" + Converter._byteToHex(r) + "|" + Converter._2dByteToHex(ots_sig);
		for(int i = 0; i < auth.size(); i++){
			signature = signature + "|" + Converter._byteToHex(auth.get(i).getContent());
		}
		setIndex(xPrivKey, index + 1);
		return signature;	
		
		/*Vorgängerzeugs
		
		int iHeight = 0;
		int treeHeight = getTreeHeight();

		this.otsAlgo.setPrivateKey(this.privKeys.get(this.keyIndex));
		this.otsAlgo.setPublicKey(this.publicKeys.get(this.keyIndex));
		this.otsAlgo.setMessage(message.getBytes());
		this.otsAlgo.sign();
		
		
		tmpSignature = this.otsAlgo.getSignature().toString();// to-be-done

		while (iHeight < treeHeight) {
			if (keyIndex % 2 == 0) {
				tmpSignature = tmpSignature + '|' + getNodeContentbyIndex(iHeight * keyIndex + 1).toString();
				keyIndex = keyIndex / 2;
			} else {
				tmpSignature = tmpSignature + '|' + getNodeContentbyIndex(iHeight * keyIndex - 1).toString();
				keyIndex = (keyIndex - 1) / 2;
			}
			iHeight++;
		}
		tmpSignature += '|' + Integer.toString(this.keyIndex);
		this.keyIndex++;
		return tmpSignature; // OTS Signatur+tmpSignature to byte array
		*/
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
	 * NOT IMPLEMENTED YET!!!!!!!
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
		Node leaf;
		byte[] d1pubKey;
		for (int i = 0; i < this.leafCounter; i++) {
			//generates a new WOTS/ WOTSPlus Keypair (public and secret key)
			if(otsAlgo instanceof WOTSPlusXMSS){
				((WOTSPlusXMSS) otsAlgo).setAddress(otsAdrs);
			}
			this.otsAlgo.generateKeyPair();
			//adds the private Key of the generated keypair to the private key list of privKeys
			this.privKeys.add(this.otsAlgo.getPrivateKey());
			this.publicKeys.add(this.otsAlgo.getPublicKey());
			
			/* dead code?
			// Frage byte[][] zu byte[] ?????
			d1pubKey = org.jcryptool.visual.merkletree.files.Converter._hexStringToByte(
					org.jcryptool.visual.merkletree.files.Converter._2dByteToHex(this.otsAlgo.getPublicKey()));
			leaf = new Node(this.mDigest.digest(d1pubKey), true, i);
			this.leafNumber++;
			leaf.setCode(d1pubKey.toString());
			this.leaves.add(leaf);
			*/
		}
	}

	@Override
	/**
	 * Returns if the tree is already generated
	 */
	public boolean isGenerated() {
		return treeGenerated;
	}
	@Override
	/**
	 * returns the used One Time Signature Algorithm including the public key, secret key, message
	 */
	public OTS getOneTimeSignatureAlgorithm() {
		return this.otsAlgo;
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
		byte[][] pk_ots;
		WOTSPlusXMSS wots = (WOTSPlusXMSS) this.otsAlgo;
		//index || r as seed for hashing the message
		byte[] hashedMessage = randomGenerator(ByteUtils.concatenate(BigInteger.valueOf(index).toByteArray(),r), message.getBytes(), message.length());
		otsAdrs.setOTSBit(true);
		otsAdrs.setOTSAddress(index);
		pk_ots = wots.pkFromSig(splitted[2], hashedMessage, publicSeed, otsAdrs);
		lAdrs.setOTSBit(false);
		lAdrs.setLTreeBit(true);
		lAdrs.setLTreeAddress(index);
		XMSSNode[] node = new XMSSNode[2];
		node[0] = new XMSSNode(generateLTree(pk_ots, publicSeed, lAdrs));
		lAdrs.setLTreeBit(false);
		lAdrs.setTreeIndex(index);
		for(int k = 0; k < getTreeHeight(); k++) {
			lAdrs.setTreeHeight(k);
			if(Math.floor(index / 1<<k) % 2 == 0) {
				lAdrs.setTreeIndex(lAdrs.getTreeIndex() / 2);
				node[1] = new XMSSNode(rand_hash(node[0].getContent(), Converter._hexStringToByte(splitted[3+k]), publicSeed, lAdrs));	//splitted[3] is auth[0] in signature				
			} else {
				node[1] = new XMSSNode(rand_hash(Converter._hexStringToByte(splitted[3+k]), node[0].getContent(), publicSeed, lAdrs));
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
		seed = publicSeed;
		
		
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
			int k = ((int)Math.floor(i / (1 << j))) ^ 1;
			auth.add(j,treeHash(k* (1 << j),j, seed));
		}
		return auth;
	}
	
	public void xmss_genSK() {
		int index = 0; //index of the next unused wots+ key
		byte[] key;	//random key for PRNG
		byte[][] iterator;
		key = generateSeed(32); //welche größe hat der key?
		xPrivKey = Integer.toString(index) + "|" + Converter._byteToHex(key);
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
	
	//FIXME: brauche des unabhängig von ana merkleTree instanz -> static
	public byte[] generateSeed(int len) {
		SecureRandom rnd = new SecureRandom();
		byte[] seed = new byte[len];
		rnd.nextBytes(seed);
		return seed;		
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
}
