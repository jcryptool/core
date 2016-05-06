package org.jcryptool.visual.merkletree.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Stack;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class XMSSTree implements ISimpleMerkle {

	// ArrayList<byte[]>pubKeys = new ArrayList<byte[]>();
	int leafCounter = 0;
	int leafNumber = 0;
	int keyIndex;
	byte[] privateSeed;
	byte[] publicSeed;
	boolean treeGenerated;
	OTS otsAlgo;
	Address hAdrs = new HashTreeAddress();

	// ArrayList<MerkleTreeNode> leaves = new ArrayList<>();
	// ArrayList<ArrayList<MerkleTreeNode>> merkleTreeHeight;
	// ArrayList<MerkleTreeNode> merkleTreeRow = null;

	ArrayList<Node> tree = new ArrayList<Node>();
	ArrayList<Node> leaves = new ArrayList<Node>();

	MessageDigest mDigest;
	ArrayList<byte[][]> privKeys = new ArrayList<byte[][]>();
	ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();

	/**
	 * Constructor for XMSS-Tree
	 * @param privateSeed
	 * @param publicSeed
	 * @param leafCounter -> Anzahl der Blätter des Baums
	 */
	XMSSTree(byte[] privateSeed, byte[] publicSeed, int leafCounter) {
		this.privateSeed = privateSeed;
		this.publicSeed = publicSeed;
		this.leafCounter = leafCounter;
		this.treeGenerated = false;
		this.keyIndex = 0;
	}
	/**
	 * Constructor for XMSS-Tree
	 * no private and public Seed set -> use default values
	 * @param keyLength
	 * @param leafCounter
	 */

	XMSSTree(int leafCounter) {
		this.leafCounter = leafCounter;
		this.treeGenerated = false;
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
		Node leafNode = new Node(LeafContent, true, ++this.leafNumber);
		leafNode.setCode(pubKey);
		leaves.add(leafNode);
	}

	@Override
	/**
	 * returns the root node of the MerkleTree
	 * while i is smaller then the tree size and there is a parent -> i++
	 * when parent == null -> return the current position of i -> this is the root node
	 */
	public byte[] getMerkleRoot() {
		for (int i = 0; i < tree.size(); i++) {
			if (tree.get(i).getParent() == null) {
				return tree.get(i).getName();
			}
		}
		return null;
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
		return publicSeed;

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
		return tree.get(index).getName();
	}

	//TODO add parameter pubKey, seed, adrs
	public byte[] generateLTree(byte[][] pubKey, byte[] seed, Address adrs) {
		double len = pubKey.length;
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
			len = Math.ceil((len / 2));
			lAdrs.setTreeHeight(lAdrs.getTreeHeight() + 1);
			}
		return pubKey[0];
	}

	public byte[] rand_hash(byte[] pKey, byte[] pKey2, byte[] seed, Address lAdrs) {
		
		int len = pKey.length;
		byte[] bitmk_0, bitmk_1, bitmk, key;
		byte[] message = ByteUtils.concatenate(pKey, pKey2);
		lAdrs.setKeyBit(false);
		lAdrs.setBlockBit(false);
		bitmk_0 = randomGenerator(seed, lAdrs.getAddress(), len);
		lAdrs.setBlockBit(true);
		bitmk_1 = randomGenerator(seed, lAdrs.getAddress(), len);
		lAdrs.setKeyBit(true);
		lAdrs.setBlockBit(false);
		key = randomGenerator(seed, lAdrs.getAddress(), len);
		bitmk = ByteUtils.concatenate(bitmk_0, bitmk_1);
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
	public XMSSNode treeHash(byte[] SK, int s, int t, byte[] seed) {
		
		XMSSNode node; //TODO make new Node class
		Stack<XMSSNode> stack = new Stack<XMSSNode>();
		Address otsAdrs = new OTSHashAddress();
		
		if(s % (1 << t) != 0){
			return null;
		}		
		
		for( int i = 0; i < (1 << t); i++) { // i < 2^t
			otsAdrs.setOTSBit(true);
			otsAdrs.setOTSAddress(s+i);
			pKey = WOTS_genPK(privKeys.get(s+i), seed, otsAdrs); //TODO implement WOTS_genPK or change wots+; return byte[][]
			otsAdrs.setOTSBit(false);
			otsAdrs.setLTreeBit(true);
			otsAdrs.setLTreeAddress(s+i);
			node = new XMSSNode(generateLTree(pKey, seed, otsAdrs)); //TODO take the byte[] and put it into a node
			otsAdrs.setLTreeBit(false);
			otsAdrs.setTreeHeight(0);
			otsAdrs.setTreeIndex(i+s);
			//if the stack is empty the first node will be put into the stack
			//if the current node and the next node on the stack have the same height hash them and put the new one pack with height+1
			if(!stack.empty()){
				while(stack.peek().getHeight() == node.getHeight()) {
					otsAdrs.setTreeIndex((otsAdrs.getTreeIndex() -1) / 2);
					node = new XMSSNode(rand_hash(stack.pop().getContent(), node.getContent(), seed, otsAdrs));
					otsAdrs.setTreeHeight(otsAdrs.getTreeHeight() + 1);
					node.setHeight(node.getHeight() + 1);
				}
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
		/**
		 * Generate the leafs
		 */
		
		//creates a new node list for the tree
		tree = new ArrayList<Node>();

		//leafcounter is defined in the constructor -> add the amount of defined leafs
		for (int c = 0; c < this.leafCounter; c++) {
			//for every tree leaf add an lTreeRoot
			this.addTreeLeaf(this.generateLTree(c), "L-Tree-Keys");
		}
		
		/**
		 * Generate the tree
		 */
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
	}
	
	/**
	 * returns the height of the tree
	 * Tree with only one Node has height 0
	 * Tree with 4 Nodes has height 2
	 */
	public int getTreeHeight() {
		return Integer.bitCount(Integer.highestOneBit(this.leaves.size() - 1) * 2 - 1);
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
			this.otsAlgo = new WOTSPlusXMSS(16, hash, this.privateSeed);
			break;
		default:
			this.otsAlgo = new WOTSPlusXMSS(16, hash, this.privateSeed);
			break;
		}
		if (this.mDigest == null) {
			try {
				mDigest = MessageDigest.getInstance(hash);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
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
		int keyIndex = this.keyIndex;
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
	}

	@Override
	/**
	 * Verifys the Signature and return true or false
	 */
	public boolean verify(String message, String signature) {
		String[] signer = signature.split("|");
		boolean verifier;
		int keyIndex = Integer.parseInt(signer[signer.length - 1]);
		byte[][] curPubKey = this.publicKeys.get(keyIndex);
		otsAlgo.setPublicKey(curPubKey);
		otsAlgo.setSignature(signer[0].getBytes());
		verifier = otsAlgo.verify();
		if (verifier) {
			if (keyIndex % 2 == 0)
				ByteUtils.concatenate(leaves.get(keyIndex).getName(), signer[1].getBytes());
			for (int i = 1; i < signer.length; i++) {
				// TODO: GenerateRootKey

			}
		}
		return verifier;
	}
	
	@Override
	/**
	 * Verifys the Signature of an given index and return true or false
	 * NOT IMPLEMENTED YET!!!!!!!
	 */
	public boolean verify(String message, String signature, int keyIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	public void generateKeyPairsAndLeaves() {
		Node leaf;
		byte[] d1pubKey;
		for (int i = 0; i < this.leafCounter; i++) {
			//generates a new WOTS/ WOTSPlus Keypair (public and secret key)
			this.otsAlgo.generateKeyPair();
			//adds the private Key of the generated keypair to the private key list of privKeys
			this.privKeys.add(this.otsAlgo.getPrivateKey());
			this.publicKeys.add(this.otsAlgo.getPublicKey());
			
			// Frage byte[][] zu byte[] ?????
			d1pubKey = org.jcryptool.visual.merkletree.files.Converter._hexStringToByte(
					org.jcryptool.visual.merkletree.files.Converter._2dByteToHex(this.otsAlgo.getPublicKey()));
			leaf = new Node(this.mDigest.digest(d1pubKey), true, i);
			this.leafNumber++;
			leaf.setCode(d1pubKey.toString());
			this.leaves.add(leaf);
			
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
}
