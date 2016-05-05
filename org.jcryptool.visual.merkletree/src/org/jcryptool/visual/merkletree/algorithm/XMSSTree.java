package org.jcryptool.visual.merkletree.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

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

	//TODO zuck: address nach rfc einfügen
	public byte[] generateLTree(int index) {
		double len = publicKeys.get(index).length;
		byte[][] pubKeys = publicKeys.get(index);
		//adrs.setTreeHeight(0);

		while (len > 1) {
			for (int i = 0; i < Math.floor(len / 2); i = i + 1) {
				//adrs.setTreeIndex(i);
				//zuck: Hashing der leaves/nodes				
				pubKeys[i] = this.hashLTree(pubKeys[2 * i], pubKeys[2 * i + 1], null, this.privateSeed);
			}
			if (len % 2 == 1) {
				//zuck: Nachrücken der ungeraden Node 
				pubKeys[(int) (Math.floor(len / 2))] = pubKeys[(int) len -1 ];
			}
			//zuck: Anpassen der Anzahl an Nodes bzw. setzen der Anzahl der Nodes auf der neuen Höhe
			len = Math.ceil((len / 2));
			//adrs.setTreeHeight(adrs.getTreeHeight() + 1);
			}
		return pubKeys[0];
	}

	public byte[] hashLTree(byte[] pKey, byte[] pKey2, byte[] adrs, byte[] seed) {
		
		int len = pKey.length;
		byte[] bitmk_0, bitmk_1, bitmk, key;
		byte[] message = this.appendByteArrays(pKey, pKey2);		
		//adrs.setKeyBit(0);
		//adrs.setBlockBit(0);
		bitmk_0 = randomGenerator(seed, adrs, len);
		//adrs.setBlockBit(1);
		bitmk_1 = randomGenerator(seed, adrs, len);
		//adrs.setKeyBit(1);
		//adrs.setBlockBit(0);
		key = randomGenerator(seed, adrs, len);
		bitmk = ByteUtils.concatenate(bitmk_0, bitmk_1);
		for (int i = 0; i < message.length; i++) {
			//XOR message with bitmask
			message[i] ^= bitmk[i];
		}
		//Formatiert den ksecret und message zu einem 512 Byte hexadezimalen Wert
		String tohash = String.format("%512s", (key.toString() + message.toString()));
		return mDigest.digest(tohash.getBytes());
		}

	//zuck: hashNode müsste überflüssig sein; selbe wie hashLTree
	public byte[] hashNode(byte[] pKey, byte[] pKey2, byte[] adrs, byte[] seed) {

		// get Bitmasken and Keysecret
		byte[] ksecret = { 0, 0, 0, 0, 0 };
		byte[] bitmk = { 0, 0, 0, 0, 0 };
		byte[] message = this.appendByteArrays(pKey, pKey2);
		for (int i = 0; i < message.length; i++) {
			message[i] ^= bitmk[0];
		}
		String tohash = String.format("%1024s", (ksecret.toString() + message.toString()));
		return mDigest.digest(tohash.getBytes());
	}

	@Override
	public void generateMerkleTree() {
		/**
		 * Generate the leafs
		 */
		
		//creates a new node list for the tree
		tree = new ArrayList<Node>();

		//leafcounter is defined in the constructor -> add the amount of defined leafs
		for (int c = 0; c < this.leafCounter; c++) {
			//for every tree leaf add an leave -> with the value of LeaveContent and public Key
			//not working yet -> "L-Tree_Keys"
			this.addTreeLeaf(this.generateLTree(c), "L-Tree_Keys");
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
		byte[] toHash = appendByteArrays(a.getContent(), b.getContent());

		return mDigest.digest(toHash);
	}

	/**
	 * append two given Byte Arrays
	 * used in the method hashing content
	 * @param array1 -> Node 1
	 * @param array2 -> Node 2
	 * @return -> appended Nodes as byte[] array
	 */
	byte[] appendByteArrays(byte[] array1, byte[] array2) {
		byte[] appended;
		String String1 = array1.toString();
		String String2 = array2.toString();
		String String3 = String1 + String2;

		appended = String3.getBytes();

		return appended;
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
				this.appendByteArrays(leaves.get(keyIndex).getName(), signer[1].getBytes());
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
	private byte[] randomGenerator(byte[] seed, byte[] address, int len) {
		byte[] res = new byte[len];	//erstellen des zu befüllenden arrays
		SecureRandom rnd = null;
		try {
			rnd = SecureRandom.getInstance("SHA1PRNG");
		} catch(NoSuchAlgorithmException e) {
			//zuck: Der Algo existiert!
		}
		seed = ByteUtils.concatenate(seed, address);
		rnd.setSeed(seed);	//zuck: setzen des seeds seed+address für den PRNG
		rnd.nextBytes(res); //zuck: befüllen des byte[]
		return res;
	}
}
