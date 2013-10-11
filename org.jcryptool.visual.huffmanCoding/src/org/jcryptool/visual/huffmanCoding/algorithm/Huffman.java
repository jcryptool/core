package org.jcryptool.visual.huffmanCoding.algorithm;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * 
 * @author Miray Inel
 */
public class Huffman {

	/**
	 * The translation table used to compress/uncompress the last file.
	 * 
	 * This table is represented by an array of BitStrings, where table[i]
	 * holds the BitString for the i-th character.
	 * 
	 * You may use an array with 256 entries even if not every character occurs
	 * in the inputfile.
	 */
	private BitString[] table;
	private Node root;
	private StringBuilder sb = null;
	private ArrayList<Node> resultNodeList = null;

	public Huffman() {
		super();
		resultNodeList = new ArrayList<Node>();
	}

	/**
	 * This method performs the compression of the file inFilename. It writes
	 * the results to the OutputStream out.
	 * 
	 * @param inFilename
	 *            the file to compress
	 * @param out
	 * 				the OutputStream
	 * @throws IOException
	 */
	public void compress(String inFilename, OutputStream out) throws IOException {

		InputStream is = new ByteArrayInputStream(inFilename.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		HuffmanStreamWriter hsw = new HuffmanStreamWriter(out);

		/*
		 * read file and create a statistic
		 */
		ArrayList<Integer> file = new ArrayList<Integer>();
		int[] charStats = createStatistics(br, file);

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

		/*
		 * write the codeTable to file and write all the codes
		 */
		hsw.writeCodeTable(table);
		for (Integer i : file) {
			hsw.write(table[i]);
		}
		hsw.close();
	}

	/**
	 * This method performs the uncompression of the file inFilename. It writes
	 * the results to the OutputStream out.
	 * 
	 * @param inFilename
	 * 				the file to uncompress
	 * @throws IOException
	 */
	public void uncompress(String inFilename) throws IOException {

		sb = new StringBuilder();
		HuffmanStreamReader hsr = new HuffmanStreamReader(new FileInputStream(inFilename));

		table = hsr.getCodeTable();
		root = buildTreeReverse(table);

		for (Node node : resultNodeList) {
			if (node.isLeaf()) {
				BitString bs = table[node.getName()];
				node.setCode(bs.toString());
			}
		}

		/*
		 * output for Debugging
		 */
		// ArrayList<Integer> t = getPreOrderRek(root, new
		// ArrayList<Integer>());
		// System.out.println("    Preorder:\n    " + t);

		/*
		 * Traverse the huffman tree. Read the input bit by bit. If bit == 1 go
		 * left, if bit == 0 go right
		 */
		Node currentNode = root;
		int tmp = hsr.readBit();
		while (tmp != -1) {
			if (tmp == 1)
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
			tmp = hsr.readBit();
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

	public StringBuilder getSb() {
		return sb;
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
			if (tmp == (char) 0)
				throw new InvalidCharacterException();
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
		 * for all entry in the codeTable. get the i-th bitString from the
		 * codeTable and create nodes for all bits from the bitString
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

	/**
	 * Preorder travesierung(for Debugging)
	 * 
	 * @param n
	 *            root
	 */
	private ArrayList<Integer> getPreOrderRek(Node n, ArrayList<Integer> result) {
		if (n != null) {
			if (n.isLeaf()) {
				result.add(n.getName());
				return result;
			}
			getPreOrderRek(n.getLeft(), result);
			getPreOrderRek(n.getRight(), result);
		}
		return result;
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