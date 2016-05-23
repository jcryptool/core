package org.jcryptool.visual.merkletree.algorithm;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class XMSSTreeTest {


	@Test
	public void testAddPrivateSeed() {
		XMSSTree merkle = new XMSSTree();
		byte[] privateS = {1, 2, 3, 4};
		merkle.addPrivateSeed(privateS);
		assertEquals(merkle.getPrivateSeed(), privateS);	
	}

	@Test
	public void testAddPublicSeed() {
		XMSSTree merkle = new XMSSTree();
		byte[] publicS = {1, 2, 3, 4};
		merkle.addPublicSeed(publicS);
		assertEquals(merkle.getPublicSeed(), publicS);
	}

	@Test
	public void testAddTreeLeaf() {
		XMSSTree merkle = new XMSSTree();
		//Leaves List has to be empty
		assertEquals(merkle.leaves.size(), 0);
		//add first Leaf
		merkle.addTreeLeaf(null , null);
		assertNull(merkle.getTreeLeaf(0).getName());
		assertNull(merkle.getTreeLeaf(0).getCode());
		//add second Leaf
		byte[] temp = {1,2,3,4};
		merkle.addTreeLeaf(temp , "Test");
		assertEquals(merkle.getTreeLeaf(1).getName(), temp);
		assertEquals(merkle.getTreeLeaf(1).getCode(), "Test");
		//check Leaves size
		assertEquals(merkle.leaves.size(), 2);
	}

	@Test
	public void testGetMerkleRoot() {
	}


	@Test
	public void testGetLeafCounter() {
		XMSSTree merkle = new XMSSTree();
		assertEquals(merkle.getLeafCounter(), 0);
		
		merkle = new XMSSTree();
		merkle.addTreeLeaf(null, null);
		merkle.addTreeLeaf(null, null);
		assertEquals(merkle.getLeafCounter(), 2);


	}

	@Test
	public void testGetTreeLeaf() {
		XMSSTree merkle = new XMSSTree();
		//add two Leaves
		merkle.addTreeLeaf(null , null);
		byte[] temp = {1,2,3,4};
		merkle.addTreeLeaf(temp , "Test");
		
		//check if get function is correct
		Node node = merkle.getTreeLeaf(0);
		assertEquals(node.getName(), null);
		assertEquals(node.getCode(), null);
		
		node = merkle.getTreeLeaf(1);
		assertEquals(node.getName(), temp);
		assertEquals(node.getCode(), "Test");

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
		
		XMSSTree merkle = new XMSSTree();
		merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTS");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		System.out.println(merkle.getTreeHeight());
		assertEquals(merkle.getTreeHeight(),1);
		
		merkle = new XMSSTree();
		merkle.selectOneTimeSignatureAlgorithm("SHA-256","WOTS");
		merkle.generateKeyPairsAndLeaves();
		merkle.generateMerkleTree();
		assertEquals(merkle.getTreeHeight(),3);

		merkle = new XMSSTree();
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
		XMSSTree merkle = new XMSSTree();
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
		XMSSTree merkle = new XMSSTree();
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
