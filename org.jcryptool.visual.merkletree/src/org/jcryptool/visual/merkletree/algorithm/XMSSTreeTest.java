package org.jcryptool.visual.merkletree.algorithm;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.hamcrest.CoreMatchers;
import org.jcryptool.visual.merkletree.files.Converter;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class XMSSTreeTest {
	

	@Test
	public void testGetRoot() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertThat(merkle.getRoot().equals(merkle.getRoot()), is(true));
		Node root = merkle.treeHash(0, merkle.getTreeHeight(), seed);
		assertThat(merkle.getRoot().equals(root), is(true));
	}

	@Test
	public void testGenerateLTree() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		byte[][] pKey;
		LTreeAddress adrs = new LTreeAddress();
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		pKey = merkle.publicKeys.get(0);
		byte[] res1 = merkle.generateLTree(pKey, seed, adrs);
		byte[] res2 = merkle.generateLTree(pKey, seed, adrs);
		assertArrayEquals(res1, res2);
		
	}

	@Test
	public void testTreeHash() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		Node res1 = merkle.treeHash(0, 1, seed);
		Node res2 = merkle.treeHash(0, 1, seed);
		assertThat(res1.equals(res2), is(true));
	}

	@Test
	public void testGenerateMerkleTree() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertThat(merkle.getTree().size(), is(7));
		assertNotNull(merkle.xPubKey);
		assertNotNull(merkle.xPrivKey);
		assertThat(merkle.isGenerated(), is(true));
		
		
	}

	@Test
	public void testGetTreeHeight() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertThat(merkle.getTreeHeight(), is(2));
	}

	@Test
	public void testSign() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		merkle.verify("42 is the answer to everything", merkle.sign("42 is the answer to everything"));
	}

	@Test
	public void testGenerateKeyPairsAndLeaves() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		assertThat(merkle.privKeys.size(), is(leafCount));
		assertThat(merkle.publicKeys.size(), is(leafCount));
	}
	
	@Test
	public void testRandomGeneratorByteArrayByteArrayInt() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed1 = {42};
		byte[] address1 = {42};
		byte[] seed2 = {21};
		byte[] address2 = {21};
		int len = 32;
		byte[] res1 = merkle.randomGenerator(seed1, address1, len);
		byte[] res2 = merkle.randomGenerator(seed1, address1, len);
		byte[] res3 = merkle.randomGenerator(seed2, address2, len);
		byte[] res4 = merkle.randomGenerator(seed2, address2, len);
		assertThat(res1, is(res2));
		assertThat(res3, is(res4));
		assertThat(res1, is(not(res3)));
		assertThat(res2, is(not(res4)));		
	}

	@Test
	public void testRootFromSig() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		XMSSNode result = merkle.rootFromSig("42 is the answer to everything", merkle.sign("42 is the answer to everything"));
		assertThat(result.equals(merkle.getRoot()), is(true));
	}

	@Test
	public void testGenerateBitmask() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		byte[] seed1 = {42};
		Address address1 = new LTreeAddress();
		address1.setBlockBit(true);
		byte[] seed2 = {21};
		Address address2 = new LTreeAddress();
		byte[] res1 = merkle.generateBitmask(seed1, address1);
		byte[] res2 = merkle.generateBitmask(seed1, address1);
		byte[] res3 = merkle.generateBitmask(seed2, address2);
		byte[] res4 = merkle.generateBitmask(seed2, address2);
		assertThat(res1, is(res2));
		assertThat(res3, is(res4));
		assertThat(res1, is(not(res3)));
		assertThat(res2, is(not(res4)));
	}

	@Test
	public void testXmss_genPK() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		merkle.xmss_genPK();
		String xmssPK = merkle.xPubKey;
		String[] splitted = xmssPK.split("\\|");
		assertThat(splitted[0].equals(Converter._byteToHex(merkle.getRoot().getContent())), is(true));
		assertThat(splitted[1].equals(Converter._byteToHex(seed)), is(true));
	}

	@Test
	public void testBuildAuth() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		ArrayList<Node> tree = merkle.getTree();
		ArrayList<Node> auth = merkle.buildAuth(0, seed);
		assertThat(auth.get(0).equals(tree.get(1)), is(true));
		assertThat(auth.get(1).equals(tree.get(5)), is(true));
	}

	@Test
	public void testXmss_genSK() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (4);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		merkle.xmss_genSK();
		String xmssSK = merkle.xPrivKey;
		String[] splitted = xmssSK.split("\\|");
		assertThat(splitted[1].equals(Converter._byteToHex(seed)), is(true));
		assertThat(splitted[0].equals("0"), is(true));
		for(int i = 0; i < 4; i++){
			assertThat(splitted[2+i].equals(Converter._2dByteToHex(merkle.privKeys.get(i))), is(true));
		}
		merkle.sign("42 is the answer to everything");
		xmssSK = merkle.xPrivKey;
		splitted = xmssSK.split("\\|");
		assertThat(splitted[0].equals("1"), is(true));
	}

	@Test
	public void testRandomGeneratorByteArrayStringInt() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed1 = {42};
		byte[] seed2 = {21};
		int len = 32;
		String message1 = "42 is the answer to everything";
		String message2 = "21 is just half of the answer";
		byte[] res1 = merkle.randomGenerator(seed1, message1, len);
		byte[] res2 = merkle.randomGenerator(seed1, message1, len);
		byte[] res3 = merkle.randomGenerator(seed2, message2, len);
		byte[] res4 = merkle.randomGenerator(seed2, message2, len);
		assertThat(res1, is(res2));
		assertThat(res3, is(res4));
		assertThat(res1, is(not(res3)));
		assertThat(res2, is(not(res4)));	
	}

	@Test
	public void testSaveNodeInfos() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (8);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		ArrayList<Node> tree = merkle.getTree();
		Node leaf0 = tree.get(0);
		Node node13 = tree.get(13);
		assertThat(leaf0.getIndex(), is(0));
		assertThat(leaf0.isLeaf(), is(true));
		assertThat(leaf0.getAuthPath().length(), is(3) );
		assertThat(node13.getIndex(), is(13));
		assertThat(node13.isLeaf(), is(false));
		assertThat(node13.getAuthPath().length(), is(3) );
		leafCount = (16);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		tree = merkle.getTree();
		leaf0 = tree.get(0);
		node13 = tree.get(13);
		assertThat(leaf0.getAuthPath().length(), is(4) );
		assertThat(node13.getAuthPath().length(), is(4) );
	}
	
	@Test
	public void testSetConnections() {
		XMSSTree merkle = new XMSSTree();
		byte[] seed = {42};
		byte[] bmSeed = {42};
		int leafCount = (8);
		merkle.setSeed(seed);
		merkle.setBitmaskSeed(bmSeed);
		merkle.setLeafCount(leafCount);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256", "WOTSPlus");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		merkle.setConnections();
		ArrayList<Node> tree = merkle.getTree();
		Node node8 = tree.get(8);
		Node node0 = tree.get(0);
		Node node1 = tree.get(1);
		Node node12 = tree.get(12);
		
		assertThat(node8.getLeft().equals(node0), is(true));
		assertThat(node8.getRight().equals(node1), is(true));
		assertThat(node0.getParent().equals(node8), is(true));
		assertThat(node1.getParent().equals(node8), is(true));
		assertThat(node8.getParent().equals(node12), is(true));
	}

}
