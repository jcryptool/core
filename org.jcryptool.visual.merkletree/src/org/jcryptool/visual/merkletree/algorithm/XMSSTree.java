package org.jcryptool.visual.merkletree.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class XMSSTree implements ISimpleMerkle {

	WOTSPlusXMSS wot;
	// ArrayList<byte[]>pubKeys = new ArrayList<byte[]>();
	int leafCounter = 0;
	int leafNumber = 0;
	int keyLength;
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

	XMSSTree(byte[] privateSeed, byte[] publicSeed, int keyLength, int leafCounter) {
		this.privateSeed = privateSeed;
		this.publicSeed = publicSeed;
		this.keyLength = keyLength;
		this.treeGenerated = false;
		this.keyIndex = 0;
		this.leafCounter = leafCounter;
		this.wot = new WOTSPlusXMSS(16, "SHA256", privateSeed);
	}

	XMSSTree(int keyLength, int leafCounter) {
		this.keyLength = keyLength;
		this.leafCounter = leafCounter;
		this.treeGenerated = false;
	}

	@Override
	public void addPrivateSeed(byte[] privateSeed) {
		this.privateSeed = privateSeed;

	}

	@Override
	public void addPublicSeed(byte[] publicSeed) {
		this.publicSeed = publicSeed;
	}

	@Override
	public void addTreeLeaf(byte[] LeafContent, String pubKey) {
		Node leafNode = new Node(LeafContent, true, ++this.leafNumber);
		leafNode.setCode(pubKey);
		leaves.add(leafNode);
	}

	@Override
	public byte[] getMerkleRoot() {

		for (int i = 0; i < tree.size(); i++) {
			if (tree.get(i).getParent() == null) {
				return tree.get(i).getName();
			}
		}
		return null;
		// return merkleTreeHeight.get(getTreeHeight()).get(0).getContent();
	}

	@Override
	public byte[] getPrivateSeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Node> getTree() {
		return this.tree;
	}

	@Override
	public byte[] getPublicSeed() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getKeyLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLeafCounter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node getTreeLeaf(int treeLeafNumber) {

		return leaves.get(treeLeafNumber);
	}

	@Override
	public byte[] getNodeContentbyIndex(int index) {

		return tree.get(index).getName();
	}

	public byte[] generateLTree(int index) {

		double len = publicKeys.get(index).length;
		byte[][] pubKeys = publicKeys.get(index);

		while (len > 1) {
			for (int i = 0; i < Math.floor(len / 2); i = i + 1) {
				// Hashing mit (0^n || K || M xor B)
				pubKeys[i] = this.hashLTree(pubKeys[2 * i], pubKeys[2 * i + 1], null, this.privateSeed);
			}
			if (len % 2 == 1) {
				pubKeys[(int) (Math.floor(len / 2) + 1)] = pubKeys[(int) len];
			}
			len = Math.ceil((len / 2));
		}
		return pubKeys[0];
	}

	public byte[] hashLTree(byte[] pKey, byte[] pKey2, byte[] adrs, byte[] seed) {

		// get Bitmask and Keysecret
		byte[] ksecret = { 0, 0, 0, 0, 0 };
		byte[] bitmk = { 0, 0, 0, 0, 0 };
		byte[] message = this.appendByteArrays(pKey, pKey2);
		for (int i = 0; i < message.length; i++) {
			message[i] ^= bitmk[0];
		}
		String tohash = String.format("%512s", (ksecret.toString() + message.toString()));
		return mDigest.digest(tohash.getBytes());
	}

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

		tree = new ArrayList<Node>();

		for (int c = 0; c < this.leafCounter; c++) {
			// this.pubKeys = new ArrayList<byte[]>(); ......
			this.addTreeLeaf(this.generateLTree(c), "L-Tree_Keys");
		}
		int height = Integer.bitCount(Integer.highestOneBit(this.leaves.size() - 1) * 2 - 1);

		if (height == 0) {
			return;
		}
		// tree.addAll(leaves);
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

		/*
		 * int tHorizontal = leaves.size();
		 * 
		 * merkleTreeHeight = new ArrayList<ArrayList<MerkleTreeNode>>();
		 * MerkleTreeNode helperNode;
		 * 
		 * merkleTreeHeight.add(this.leaves);
		 * 
		 * for (int i = 0; i < height-1; i++) {
		 * 
		 * merkleTreeRow = new ArrayList<MerkleTreeNode>(); int nextRowNode = 0;
		 * 
		 * for (int j = 0; j < tHorizontal; j += 2) {
		 * 
		 * byte[] content =
		 * this.hashNode(merkleTreeHeight.get(i).get(j).getContent(),
		 * merkleTreeHeight.get(i).get(j + 1).getContent(), null,
		 * this.publicSeed); helperNode = new MerkleTreeNode(i + 1, nextRowNode,
		 * content); merkleTreeRow.add(helperNode); nextRowNode++;
		 * 
		 * } if (tHorizontal % 2 == 1) { byte[] content =
		 * this.hashNode(merkleTreeHeight.get(i).get((tHorizontal -
		 * 1)).getContent(), merkleTreeHeight.get(i).get((tHorizontal -
		 * 1)).getContent(),null,this.publicSeed); helperNode = new
		 * MerkleTreeNode(i + 1, nextRowNode + 1, content);
		 * merkleTreeRow.add(helperNode); }
		 * 
		 * merkleTreeHeight.add(merkleTreeRow); }
		 */

		treeGenerated = true;
	}

	byte[] hashingContent(MerkleTreeNode a, MerkleTreeNode b) {
		byte[] toHash = appendByteArrays(a.getContent(), b.getContent());

		return mDigest.digest(toHash);
	}

	byte[] appendByteArrays(byte[] array1, byte[] array2) {
		byte[] appended;
		String String1 = array1.toString();
		String String2 = array2.toString();
		String String3 = String1 + String2;

		appended = String3.getBytes();

		return appended;
	}

	public int getTreeHeight() {
		/*
		 * int height = 1; double helper = (double) Leaves.size();
		 * 
		 * if(helper == 0){ return 0; }
		 * 
		 * 
		 * do { height++;
		 * 
		 * }while ((Math.ceil(helper /= 2)) > 1);
		 * 
		 * return height;
		 */
		return Integer.bitCount(Integer.highestOneBit(this.leaves.size() - 1) * 2 - 1);

	}

	@Override
	public void selectHashAlgorithmus(String hAlgo) {
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
	public void selectOneTimeSignatureAlgorithmus(String hash, String algo) {
		switch (algo) {
		case "WOTS":
			this.otsAlgo = new WinternitzOTS(16, hash);
			break;
		case "WOTSPlus":
			this.otsAlgo = new WOTSPlusMerkle(16, hash, this.privateSeed);
			break;
		default:
			this.otsAlgo = new WOTSPlusMerkle(16, hash, this.privateSeed);
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
		this.generateKeyPairsAndLeaves();

	}

	@Override
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

	private void generateKeyPairsAndLeaves() {
		Node leaf;
		byte[] d1pubKey;
		for (int i = 0; i < this.leafCounter; i++) {
			this.otsAlgo.generateKeyPair();
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
	public boolean isGenerated() {

		return treeGenerated;
	}
	@Override
	public OTS getOneTimeSignatureAlgorithmus() {
		return this.otsAlgo;
	}
}
