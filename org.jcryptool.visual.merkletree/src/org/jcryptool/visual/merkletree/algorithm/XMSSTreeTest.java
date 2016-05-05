package org.jcryptool.visual.merkletree.algorithm;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class XMSSTreeTest {

	@Test
	public void testXMSSTreeByteArrayByteArrayIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testXMSSTreeIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPrivateSeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddPublicSeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddTreeLeaf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMerkleRoot() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrivateSeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPublicSeed() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetKeyLength() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLeafCounter() {
		fail("Not yet implemented");
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
	public void testAppendByteArrays() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTreeHeight() {
		/**
		 * Test not finished yet -> method is not called correctly
		 */
		ArrayList<Node> leaves = new ArrayList<Node>();
		byte[] content = { 0, 0, 0, 0, 0 };
		Node leafNode1 = new Node(content, true, 1);
		Node leafNode2 = new Node(content, true, 2);
		Node leafNode3 = new Node(content, true, 3);
		Node leafNode4 = new Node(content, true, 4);
		Node leafNode5 = new Node(content, true, 5);
		Node leafNode6 = new Node(content, true, 6);
		Node leafNode7 = new Node(content, true, 7);
		Node leafNode8 = new Node(content, true, 8);
		leaves.add(leafNode1);
		leaves.add(leafNode2);
		leaves.add(leafNode3);
		leaves.add(leafNode4);

		int height = Integer.bitCount(Integer.highestOneBit(leaves.size() - 1) * 2 - 1);
		assertEquals(height, 2);
		
		leaves.add(leafNode5);
		leaves.add(leafNode6);
		leaves.add(leafNode7);
		leaves.add(leafNode8);
		
		height = Integer.bitCount(Integer.highestOneBit(leaves.size() - 1) * 2 - 1);
		assertEquals(height, 3);

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
	public void testGetOneTimeSignatureAlgorithm() {
		fail("Not yet implemented");
	}

}
