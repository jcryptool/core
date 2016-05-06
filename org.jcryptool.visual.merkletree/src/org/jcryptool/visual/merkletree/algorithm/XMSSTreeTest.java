package org.jcryptool.visual.merkletree.algorithm;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class XMSSTreeTest {


	@Test
	public void testAddPrivateSeed() {
		XMSSTree merkle = new XMSSTree(null, null, 0);
		byte[] privateS = {1, 2, 3, 4};
		merkle.addPrivateSeed(privateS);
		assertEquals(merkle.getPrivateSeed(), privateS);	
	}

	@Test
	public void testAddPublicSeed() {
		XMSSTree merkle = new XMSSTree(null, null, 0);
		byte[] publicS = {1, 2, 3, 4};
		merkle.addPublicSeed(publicS);
		assertEquals(merkle.getPublicSeed(), publicS);
	}

	@Test
	public void testAddTreeLeaf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMerkleRoot() {
		XMSSTree merkle = new XMSSTree(null, null, 16);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTS");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		byte[] root = merkle.getMerkleRoot();
		System.out.println(root);
	}

	@Test
	public void testGetPrivateSeed() {
		//Generate Merkle Tree
		XMSSTree merkle = new XMSSTree(null, null, 0);
		assertEquals(merkle.getPrivateSeed(), null);
		
		byte[] privateS = {1, 2, 3, 4, 5 };
		merkle = new XMSSTree(privateS, null, 0);
		assertEquals(merkle.getPrivateSeed(), privateS);
	}

	@Test
	public void testGetTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPublicSeed() {
		//Generate Merkle Tree
		XMSSTree merkle = new XMSSTree(null, null, 0);
		assertEquals(merkle.getPublicSeed(), null);
		
		byte[] publicS = {1, 2, 3, 4, 5 };
		merkle = new XMSSTree(null, publicS, 0);
		assertEquals(merkle.getPublicSeed(), publicS);
		
	}

	@Test
	public void testGetLeafCounter() {
		XMSSTree merkle = new XMSSTree(null, null, 0);
		assertEquals(merkle.getLeafCounter(), 0);
		
		merkle = new XMSSTree(null, null, 2);
		assertEquals(merkle.getLeafCounter(), 2);


	}

	@Test
	public void testGetTreeLeaf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNodeContentbyIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateLTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testHashLTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testHashNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateMerkleTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testHashingContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTreeHeight() {
		
		XMSSTree merkle = new XMSSTree(null, null, 2);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTS");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertEquals(merkle.getTreeHeight(),1);
		
		merkle = new XMSSTree(null, null, 8);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTS");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertEquals(merkle.getTreeHeight(),3);

		merkle = new XMSSTree(null, null, 16);
		merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTS");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertEquals(merkle.getTreeHeight(),4);
	}

	@Test
	public void testSelectHashAlgorithm() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectOneTimeSignatureAlgorithm() {
		fail("Not yet implemented");
	}

	@Test
	public void testSign() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testVerifyStringStringInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateKeyPairsAndLeaves() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsGenerated() {
		XMSSTree merkle = new XMSSTree(null, null, 0);
		assertEquals(merkle.isGenerated(), false);
		merkle.generateMerkleTree();
		assertEquals(merkle.isGenerated(), true);
	}

	@Test
	public void testGetOneTimeSignatureAlgorithm() {
		fail("Not yet implemented");
	}

	@Test
	public void TestrandomGenerator(){
		XMSSTree merkle = new XMSSTree(null, null, 0);
		byte[] address = {1,2,3,4};
		byte[] seed1 = {4,5,6,7,8};
		byte[] seed2 = {7,5,6,7,8};
		byte[] psrv1 = merkle.randomGenerator(seed1, address, 20);
		byte[] psrv2 = merkle.randomGenerator(seed1, address, 20);
		assertArrayEquals(psrv1, psrv2);
		
		psrv2 = merkle.randomGenerator(seed2, address, 20);
		assertFalse(Arrays.equals(psrv1, psrv2));

	}
}