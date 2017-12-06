//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.algorithm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author Miray Inel
 * @author <i>revised by</i>
 * @author Michael Altenhuber
 * 
 */
public class Huffman {

	/**
	 * The translation table used to compress/uncompress the last file.
	 *
	 * This table is represented by an array of BitStrings, where table[i] holds the
	 * BitString for the i-th character.
	 *
	 * You may use an array with 256 entries even if not every character occurs in
	 * the inputfile.
	 */
	private BitString[] table;
	private Node root;
	private StringBuilder sb = null;
	@Inject
	private ArrayList<Node> resultNodeList = null;
	private OutputStream out;
	private ArrayList<Integer> bitArray;
	private int[] huffmanBinaryCompressed;

	public Huffman() {
		super();
		resultNodeList = new ArrayList<Node>();
	}

	/**
	 * This method performs the compression of the file inFilename. It writes the
	 * results to the OutputStream out.
	 *
	 * @param input
	 *            the file to compress
	 * @param out
	 *            the OutputStream
	 * @throws IOException
	 */
	public void compress(String input) throws IOException {

		InputStream is = new ByteArrayInputStream(input.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		bitArray = new ArrayList<>();
		/*
		 * read file and create a statistic
		 */
		ArrayList<Integer> inputFile = new ArrayList<Integer>();
		int[] charStats = createStatistics(br, inputFile);

		/*
		 * create node set and calculate the probabilities
		 */
		ArrayList<Node> nodeList = createNodeSet(charStats);

		/*
		 * do the huffman
		 */
		ArrayList<Node> leafList = (ArrayList<Node>) nodeList.clone();
		createHuffmanTree(nodeList);

		/*
		 * create table with the leafSet. The leafSet contains all the leafs
		 */
		this.table = createTable(leafList);

		for (Node node : resultNodeList) {
			if (node.isLeaf()) {
				BitString bs = table[node.getName()];
				node.setCode(bs.toString());
			}
		}

		huffmanBinaryCompressed = createFileAsInt(inputFile);
	}

	public int[] getHuffmanBinary() {
		if (huffmanBinaryCompressed == null)
			return new int[0];
		return huffmanBinaryCompressed;
	}

	public void writeHuffmanBinary(File file) throws IOException {
		if (huffmanBinaryCompressed == null) {
			throw new IllegalStateException("No compressed huffman encoded data available");
		}
		try (OutputStream out = new FileOutputStream(file)) {

			for (int huffmanByte : huffmanBinaryCompressed) {
				out.write(huffmanByte);
			}
		}
	}

	/**
	 * Creates the binary structure for the huffman tree.
	 * 
	 * This used to be done by HuffmanStreamWriter but I think it's nicer to be
	 * first assembled and then write it in one chunk.
	 * 
	 * @param file
	 * @return a Integer Array of the binary representation of the huffman tree
	 */
	private int[] createFileAsInt(ArrayList<Integer> inputFile) {
		// First we create the table
		bitArray.clear();

		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				bitArray.add(i);
				bitArray.add(table[i].getLength());
				bitStringToInteger(table[i]);
				commitBuffer();
			}
		}
		// write end of table
		bitArray.add(0);
		bitArray.add(0);

		// Now we search for every character in the file, take its value from the table
		// and write it to the binary (int) buffer
		for (Integer i : inputFile) {
			bitStringToInteger(table[i]);
		}
		commitBuffer();

		int[] fileAsInt = new int[bitArray.size()];

		for (int i = 0; i < fileAsInt.length; ++i) {
			fileAsInt[i] = bitArray.get(i);
		}

		return fileAsInt;
	}

	int buffer = 0;
	int offset = 0;

	private void bitStringToInteger(BitString bitString) {
		int bit;

		boolean[] bits = bitString.getBitArray();
		for (int i = 0; i < bits.length; ++i, ++offset) {
			if (offset >= 8) {
				commitBuffer();
			}
			bit = bits[i] ? 1 : 0;
			buffer |= bit << (7 - offset);

		}

	}

	private void commitBuffer() {
		bitArray.add(buffer);
		buffer = 0;
		offset = 0;
	}

	/**
	 * This method performs the uncompression of the byte array (as Integer). It can
	 * be fetched by calling getMessage()
	 *
	 * @param path
	 *            the byte data
	 * @throws IOException
	 */
	public void uncompress(int[] huffmanBinary) {

		table = new BitString[256];
		int character, codeLength, byteLength, i = 0;
		character = huffmanBinary[i++];
		codeLength = huffmanBinary[i++];
		// read table entry by entry

		for (; character != -1 && codeLength != 0;) {

			byteLength = (codeLength - 1) / 8 + 1;
			byte[] bytes = new byte[byteLength];

			for (int k = 0; k < byteLength; k++)
				bytes[k] = (byte) huffmanBinary[i++];

			table[character] = new BitString(bytes, codeLength);

			character = huffmanBinary[i++];
			codeLength = huffmanBinary[i++];
		}

		sb = new StringBuilder();
		root = buildTreeReverse(table);

		Node currentNode = root;
		int mask = 0x80;

		int tmp = huffmanBinary[i] & mask;
		mask >>= 1;

		while (i < huffmanBinary.length) {
			if (tmp != 0)
				currentNode = currentNode.getLeft();
			else
				currentNode = currentNode.getRight();

			/*
			 * if the Node is a leaf then write the output stream
			 */
			if (currentNode.isLeaf()) {
				if (currentNode.getName() == 0) {
					return;
				}
				sb.append((char) currentNode.getName());
				currentNode = root;
			}
			if (mask == 0) {
				++i;
				mask = 0x80;
			}

			tmp = huffmanBinary[i] & mask;
			mask >>= 1;
		}

	}

	/**
	 * This method returns the code table used by the last compress/uncompress
	 * operation.
	 *
	 * @return the code table
	 */
	public BitString[] getCodeTable() {
		return this.table;
	}

	public String getMessage() {
		if (sb != null)
			return sb.toString();
		else
			return "";
	}

	public ArrayList<Node> getResultNodeList() {
		return resultNodeList;
	}

	private void createHuffmanTree(ArrayList<Node> nodeList) {
		Node leftNode;
		Node rightNode;
		Node parent;
		while (nodeList.size() > 1) {
			leftNode = findLow(nodeList);
			nodeList.remove(leftNode);

			rightNode = findLow(nodeList);
			nodeList.remove(rightNode);

			parent = new Node(-1, (leftNode.getValue() + rightNode.getValue()), leftNode, rightNode);
			parent.getConnectedTo().add(leftNode);
			parent.getConnectedTo().add(rightNode);

			leftNode.setParent(parent);
			rightNode.setParent(parent);
			nodeList.add(parent);
			resultNodeList.add(parent);
		}
	}

	private ArrayList<Node> createNodeSet(int[] charStats) {
		ArrayList<Node> nodeList = new ArrayList<Node>();

		int numberOfChar = 0;
		for (int i = 0; i < charStats.length; i++) {
			if (charStats[i] != 0) {
				numberOfChar += charStats[i];
				Node n = new Node(i, true);
				nodeList.add(n);
				resultNodeList.add(n);
			}
		}

		for (Node n : nodeList) {
			n.setValue((double) charStats[n.getName()] / (double) numberOfChar);
		}
		return nodeList;
	}

	private int[] createStatistics(BufferedReader br, ArrayList<Integer> file) throws IOException {
		int tmp;
		int charStats[] = new int[256];
		tmp = br.read();
		if (tmp == -1) {
			charStats[0]++;
			file.add(0);
		} else
			file.add(tmp);
		while (tmp != -1) {
			if (tmp == (char) 0 || tmp >= 256) {
				br.close();
				throw new InvalidCharacterException();
			}
			charStats[tmp]++;
			tmp = br.read();

			if (tmp == -1) {
				charStats[0]++;
				file.add(0);
			} else
				file.add(tmp);
		}
		return charStats;
	}

	/**
	 * Builds the Huffman tree
	 *
	 * @param table
	 *            translation table
	 * @return the created tree
	 */
	private Node buildTreeReverse(BitString[] table) {
		BitString currentBitString;
		boolean[] currentBits;
		Node tmpRoot = new Node(-1, false);
		Node currentNode = tmpRoot;

		resultNodeList.add(currentNode);

		/*
		 * for all entry in the codeTable. get the i-th bitString from the codeTable and
		 * create nodes for all bits from the bitString
		 */
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				currentBitString = table[i];
				currentBits = currentBitString.getBitArray();

				for (int j = 0; j < currentBits.length; j++) {
					if (currentBits[j]) {
						if (currentNode.getLeft() == null) {
							Node n = new Node(-1, false);
							currentNode.setLeft(n);
							n.setParent(currentNode);
							resultNodeList.add(n);
							currentNode.getConnectedTo().add(n);
							if (j == currentBits.length - 1) {
								currentNode.getLeft().setName(i);
								currentNode.getLeft().setLeaf(true);
								currentNode.getLeft().setCode(table[i].toString());
							}
						}
						currentNode = currentNode.getLeft();

					} else {
						if (currentNode.getRight() == null) {
							Node n = new Node(-1, false);
							currentNode.setRight(n);
							n.setParent(currentNode);
							resultNodeList.add(n);
							currentNode.getConnectedTo().add(n);
							if (j == currentBits.length - 1) {
								currentNode.getRight().setName(i);
								currentNode.getRight().setLeaf(true);
								currentNode.getRight().setCode(table[i].toString());
							}
						}
						currentNode = currentNode.getRight();
					}
				}
				currentNode = tmpRoot;
			}
		}
		return tmpRoot;
	}

	/**
	 * Find the node with the smallest value
	 *
	 * @param set
	 * @return node
	 */
	private Node findLow(ArrayList<Node> set) {
		if (set == null || set.size() == 0)
			return null;

		Node tmpNode = set.get(0);

		for (Node n : set) {
			if (n.getValue() < tmpNode.getValue())
				tmpNode = n;
		}
		return tmpNode;
	}

	private BitString[] createTable(ArrayList<Node> leafSet) {
		Node tmpNode;
		Node tmpParent;
		BitString bs;
		ArrayList<Boolean> code;
		BitString[] table = new BitString[256];

		for (Node n : leafSet) {
			code = new ArrayList<Boolean>();
			tmpNode = n;
			tmpParent = n.getParent();

			while (tmpParent != null) {
				if (tmpParent.getLeft().equals(tmpNode)) {
					code.add(0, true);

				} else {
					code.add(0, false);

				}

				tmpNode = tmpNode.getParent();
				tmpParent = tmpNode.getParent();
			}
			bs = new BitString();
			if (leafSet.size() == 1)
				bs.append(false);
			else
				for (Boolean b : code) {
					bs.append(b);
				}
			table[n.getName()] = bs;
		}
		return table;
	}
}