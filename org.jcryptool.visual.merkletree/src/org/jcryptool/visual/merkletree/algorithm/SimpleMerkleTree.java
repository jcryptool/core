package org.jcryptool.visual.merkletree.algorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

public class SimpleMerkleTree implements ISimpleMerkle {

	int keyIndex;
	byte[] seed;
	boolean treeGenerated;
	OTS otsAlgo;

	// ArrayList<MerkleTreeNode> leaves = new ArrayList<>();
	// ArrayList<ArrayList<MerkleTreeNode>> merkleTreeHeight;
	// ArrayList<MerkleTreeNode> merkleTreeRow = null;

	ArrayList<Node> tree = new ArrayList<Node>();
	ArrayList<Node> leaves = new ArrayList<Node>();
	// ArrayList<ArrayList<Node>> merkleTreeHeight;
	// ArrayList<Node> merkleTreeRow = null;

	MessageDigest mDigest;
	ArrayList<byte[][]> privKeys = new ArrayList<byte[][]>();
	ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();
	int leafCounter = 0;
	int leafNumber = 0;

	
	/* 
	SimpleMerkleTree(int leafCounter) {
		this.leafCounter = leafCounter;
		this.treeGenerated = false;
	}

	public SimpleMerkleTree(byte[] seed, byte[] publicSeed, int leafCounter) {
		this.seed = seed;
		this.publicSeed = publicSeed;
		this.treeGenerated = false;
		this.keyIndex = 0;
		this.leafCounter = leafCounter;
	}
	*/
	//TODO:
	//keyindex =0, eingefügt sonst bug? -> Lindi
	public SimpleMerkleTree(){
		this.treeGenerated = false;
		this.keyIndex = 0;
	}
	@Override
	public void setSeed(byte[] seed) {
		this.seed = seed;

	}
	
	@Override
	public byte[] getSeed() {
		return seed;
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
	public int getLeafCounter() {
		return leafCounter;
	}

	@Override
	public ArrayList<Node> getTree() {
		return this.tree;
	}

	@Override
	public boolean isGenerated() {
		return treeGenerated;
	}

	@Override
	public Node getTreeLeaf(int treeLeafNumber) {
		return leaves.get(treeLeafNumber);
	}

	@Override
	public void generateMerkleTree() {

		int height = getTreeHeight();

		if (height == 0) {
			return;
		}
		// int tHorizontal = leafCounter;

		tree = new ArrayList<Node>();
		tree.addAll(leaves);
		Node helperNode;
		ArrayList<Node> treeLevel = new ArrayList<Node>();
		int index = 0;
		int levelCount;
		int NodeLevelCounter = tree.size();
		int treeIndex = 0;
		for (levelCount = 0; levelCount < height; levelCount++) {
			treeLevel = new ArrayList<Node>();
			NodeLevelCounter = (int) Math.round(NodeLevelCounter / 2.0);

			for (index = 0; index < NodeLevelCounter; index++, treeIndex += 2) {
				if (treeIndex + 1 < tree.size()) {
					byte[] content = hashingContent(tree.get(treeIndex), tree.get(treeIndex + 1));
					helperNode = new SimpleNode(content, tree.get(treeIndex), tree.get(treeIndex + 1));
					treeLevel.add(helperNode);
					treeLevel.get(index).getConnectedTo().add(tree.get(treeIndex));
					treeLevel.get(index).getConnectedTo().add(tree.get(treeIndex + 1));
					tree.get(treeIndex).setParent(treeLevel.get(index));
					tree.get(treeIndex + 1).setParent(treeLevel.get(index));
					
				//zuck: deadcode/falsch?
				} else {
					byte[] content = hashingContent(tree.get(treeIndex), tree.get(treeIndex));
					helperNode = new SimpleNode(content, false, 0);
					helperNode.setLeft(tree.get(treeIndex));
					treeLevel.add(helperNode);
					treeLevel.get(index).getConnectedTo().add(tree.get(treeIndex));
					tree.get(treeIndex).setParent(treeLevel.get(index));
				}				

			}
			treeIndex = tree.size(); //unnütz
			tree.addAll(treeLevel);
		}
		/*
		 * for (int i = 0; i < height-1; i++) { int nextRowNode = 0;
		 * 
		 * for (int j = 0; j < tHorizontal; j += 2) {
		 * 
		 * byte[] content = hashingContent(tree.get(i), tree.get(i+1));
		 * helperNode = new Node(content, tree.get(i), tree.get(i+1));
		 * tree.add(helperNode); nextRowNode++;
		 * 
		 * } if (tHorizontal % 2 == 1) { byte[] content =
		 * hashingContent(merkleTreeHeight.get(i).get((tHorizontal - 1)),
		 * merkleTreeHeight.get(i).get((tHorizontal - 1))); helperNode = new
		 * MerkleTreeNode(i + 1, nextRowNode + 1, content);
		 * merkleTreeRow.add(helperNode); }
		 * 
		 * merkleTreeHeight.add(merkleTreeRow); }
		 */
		treeGenerated = true;
	}

	byte[] hashingContent(Node a, Node b) {
		byte[] toHash = appendByteArrays(a.getName(), b.getName());

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

		return Integer.bitCount(Integer.highestOneBit(this.leafCounter - 1) * 2 - 1);

	}

	@Override
	public void selectOneTimeSignatureAlgorithm(String hash, String algo) {
		switch (algo) {
		case "WOTS":
			this.otsAlgo = new WinternitzOTS(16, hash);
			break;
		case "WOTSPlus":
			this.otsAlgo = new WOTSPlusMerkle(16, hash, this.seed);
			break;
		default:
			this.otsAlgo = new WOTSPlusMerkle(16, hash, this.seed);
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
	public String sign(String message) {
		String tmpSignature="";
		int iHeight = this.keyIndex;
		int treeHeight = tree.size();
		
		if(this.keyIndex < this.privKeys.size()) {
			
			String messageHash = org.jcryptool.visual.merkletree.files.Converter
					._byteToHex(this.otsAlgo.hashMessage(message));
			String b = org.jcryptool.visual.merkletree.files.Converter
					._byteToHex(this.otsAlgo.initB());
			
			this.otsAlgo.setPrivateKey(this.privKeys.get(this.keyIndex));
			this.otsAlgo.setPublicKey(this.publicKeys.get(this.keyIndex));
			this.otsAlgo.setMessage(messageHash.getBytes());
			this.otsAlgo.sign();
			
			tmpSignature = Integer.toString(this.keyIndex)+"|";
			tmpSignature += org.jcryptool.visual.merkletree.files.Converter
					._byteToHex(this.otsAlgo.getSignature());// to-be-done

			
			
			while (iHeight < treeHeight-1) {
				if(this.tree.get(iHeight).getParent().getLeft().equals(this.tree.get(iHeight))) {
					tmpSignature= tmpSignature+ '|'+this.tree.get(iHeight).getParent().getRight().getNameAsString();
				}
				else if (this.tree.get(iHeight).getParent().getRight().equals(this.tree.get(iHeight))) {
					tmpSignature = tmpSignature + '|' + this.tree.get(iHeight).getParent().getLeft().getNameAsString();
				}
				iHeight=this.tree.lastIndexOf(this.tree.get(iHeight).getParent());
			}
			this.keyIndex++;
		}
		return tmpSignature; // OTS Signatur+tmpSignature to byte array
	}

	@Override
	public boolean verify(String message, String signature) {
		String[] signer = signature.split("\\|");
		boolean verifier=true;
		int keyIndex = Integer.parseInt(signer[0]);
		byte[][] curPubKey = this.publicKeys.get(keyIndex);	
		//set OTS Algorithm values
		String messageHash = org.jcryptool.visual.merkletree.files.Converter
				._byteToHex(this.otsAlgo.hashMessage(message));
		
		otsAlgo.setPrivateKey(this.privKeys.get(keyIndex));		
		otsAlgo.setPublicKey(publicKeys.get(keyIndex));
		otsAlgo.setSignature(org.jcryptool.visual.merkletree.files.Converter
				._hexStringToByte(signer[1]));
		otsAlgo.setMessage(messageHash.getBytes());
		
		
		verifier = otsAlgo.verify();
		//wozu wird das gemacht? wird doch eh nur am ende verfier zurückgegeben
		int iHigh=keyIndex;
		//String currentAuthPath="";
		byte[] currentNode=leaves.get(keyIndex).getName();
		int treeHigh=tree.size();
		if (verifier) {
			while (iHigh < treeHigh-1) {
				if(this.tree.get(iHigh).getParent().getLeft().equals(this.tree.get(iHigh))) {
					//currentAuthPath=this.tree.get(iHigh).getParent().getRight().getNameAsString();
					currentNode=this.createNode(currentNode,this.tree.get(iHigh).getParent().getRight().getName());
					
					if(!Arrays.equals(currentNode,this.tree.get(iHigh).getParent().getName())) {
						return false;
					}
					else {
						currentNode=this.tree.get(iHigh).getParent().getName();
					}
				}
				else if (this.tree.get(iHigh).getParent().getRight().equals(this.tree.get(iHigh))) {
					//currentAuthPath = this.tree.get(iHigh).getParent().getLeft().getNameAsString();
					currentNode=this.createNode(this.tree.get(iHigh).getParent().getLeft().getName(),currentNode);
					if(!Arrays.equals(currentNode,this.tree.get(iHigh).getParent().getName())) {
						return false;
					}
					else {
						currentNode=this.tree.get(iHigh).getParent().getName();
					}
				}
				iHigh=this.tree.lastIndexOf(this.tree.get(iHigh).getParent());
			}
		}
		return verifier;
	}
	public boolean verify(String message, String signature,int markedLeafIndex) {
		String[] signer = signature.split("\\|");
		int keyIndex = Integer.parseInt(signer[0]);
		if(markedLeafIndex != keyIndex) {
			return false;
		}
		else
			return this.verify(message, signature);
		
	}
	public byte[] createNode(byte[]node1,byte[]node2) {
		byte[] toHash = appendByteArrays(node1, node2);

		return mDigest.digest(toHash);

		
	}
	public void generateKeyPairsAndLeaves() {
		Node leaf;
		byte[] d1pubKey;
		String code;
		for (int i = 0; i < this.leafCounter; i++) {
			this.otsAlgo.generateKeyPair();
			this.privKeys.add(this.otsAlgo.getPrivateKey());
			this.publicKeys.add(this.otsAlgo.getPublicKey());
			// Frage byte[][] zu byte[] ?????
			d1pubKey = org.jcryptool.visual.merkletree.files.Converter._hexStringToByte(
					org.jcryptool.visual.merkletree.files.Converter._2dByteToHex(this.otsAlgo.getPublicKey()));
			leaf = new SimpleNode(this.mDigest.digest(d1pubKey), true, i);
			this.leafNumber++;
			code= org.jcryptool.visual.merkletree.files.Converter
					._byteToHex(d1pubKey).substring(0, 5);
			code+="...";
			code+= org.jcryptool.visual.merkletree.files.Converter
					._byteToHex(d1pubKey).substring(d1pubKey.length-5, d1pubKey.length);
			leaf.setCode(code);
			this.leaves.add(leaf);
		}
		
	}
	
	public void setLeafCount(int i) {
		leafCounter = i;
	}
	
}
