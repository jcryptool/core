package org.jcryptool.visual.merkletree.algorithm;

import java.util.ArrayList;

public interface ISimpleMerkle {
	// Variablen

	public void setSeed(byte[] seed);

	public byte[] getMerkleRoot();

	public byte[] getSeed();

	public int getLeafCounter();

	public boolean isGenerated();

	public Node getTreeLeaf(int treeLeaveNumber);
	
	public void generateKeyPairsAndLeaves();

	public ArrayList<Node> getTree();

	public void generateMerkleTree();

	public String sign(String message);

	public boolean verify(String message, String signature);

	public boolean verify(String message, String signature, int keyIndex);
	// Options

	public void selectOneTimeSignatureAlgorithm(String hash, String algo);
			
	public void setLeafCount(int i);
	
}
