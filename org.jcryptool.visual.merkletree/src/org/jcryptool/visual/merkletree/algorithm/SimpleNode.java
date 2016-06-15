package org.jcryptool.visual.merkletree.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.jcryptool.visual.merkletree.files.Converter;

/**
 * 
 * @author Kevin Muehlboeck
 * 
 */
public class SimpleNode implements Node{
	

	// Representation of the Hash of its Node
	private byte[] content;
	
	// private double value;
	// when true, then it's a leaf
	private boolean leaf;
	// next left Node
	private Node left;
	// next right Node
	private Node right;
	// Parent Node
	private Node parent;
	// has to be done
	private List<Node> connections;
	// if leaf, number of the leaf
	private int index=-1;
	// Representation of the Public Key (ONLY LEAFS!!!)
	private String code;

	/**
	 * Creates a node
	 * 
	 * @param name
	 *            name of the node.
	 * @param leaf
	 *            true if leaf false if not
	 */
	public SimpleNode(byte[] content, boolean leaf, int index) {
		this.content = content;
		this.leaf = leaf;
		connections = new ArrayList<Node>();
		this.index = index;
	}

	/**
	 * Creates a node
	 * 
	 * @param name
	 *            name of the node
	 * @param value
	 *            the probability of the node
	 * @param left
	 *            left child
	 * @param right
	 *            right child
	 */
	public SimpleNode(byte[] content, /* double value, */ Node left, Node right) {
		this.content = content;
		// this.value = value;
		this.left = left;
		this.right = right;
		connections = new ArrayList<Node>();
	}

	/**
	 * Checks if the node is a leaf
	 * 
	 * @return true if leaf false if not
	 */
	public boolean isLeaf() {
		return this.leaf;
	}

	/**
	 * Returns the left child node
	 * 
	 * @return Left child
	 */
	public Node getLeft() {
		return left;
	}

	/**
	 * Sets the left child node
	 * 
	 * @param left
	 *            left child node
	 */
	public void setLeft(Node left) {
		this.left = left;
	}

	/**
	 * Returns the right child node
	 * 
	 * @return Right child
	 */
	public Node getRight() {
		return right;
	}

	/**
	 * Sets the right child node
	 * 
	 * @param right
	 *            right child node
	 */
	public void setRight(Node right) {
		this.right = right;
	}

	/**
	 * Sets the leaf mark
	 * 
	 * @param leaf
	 *            true if leaf false if not
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/**
	 * Returns the name of the node
	 * 
	 * @return name of node
	 */
	public byte[] getName() {
		return content;
	}

	/**
	 * The string representation of the node
	 * 
	 * @return name as string
	 */
	public String getNameAsString() {
		return org.jcryptool.visual.merkletree.files.Converter._byteToHex(content);
	}

	/**
	 * Sets the name of the node
	 * 
	 * @param name
	 */
	public void setName(byte[] content) {
		this.content = content;
	}

	/**
	 * Returns the value of the node
	 * 
	 * @return the value of the node (probability)
	 */
	// public double getValue() {
	// return 0;
	// }

	/**
	 * Sets the value of the node
	 * 
	 * @param value
	 *            value of the node (probability)
	 */
	// public void setValue(double value) {
	// this.value = value;
	// }

	/**
	 * Returns the parent node of the node
	 * 
	 * @return
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Sets the parent od the node
	 * 
	 * @param parent
	 *            the parent node. Null if node is the root node
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * Returns the all connected nodes
	 * 
	 * @return the connected nodes list
	 */
	public List<Node> getConnectedTo() {
		return connections;
	}

	/**
	 * Returns the code of the Node only when the node is a leaf (Represents the
	 * Public Key)
	 * 
	 * @return the code of the leaf
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code of the node
	 * 
	 * @param code
	 *            the code of the leaf
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Sets the leafNumber of the node
	 * 
	 * @param leafNumber
	 *            the number of the leaf
	 */
	public void setLeafNumber(int index) {
		this.index = index;
	}

	/**
	 * Returns the leafNumber of the Node only when the node is a leaf
	 * 
	 * @return the number of the leaf
	 */
	public int getLeafNumber() {
		return this.index;
	}

	@Override
	public String toString() {
		return "[ " + content + " " + code + " " + leaf + " ]";
	}

	@Override
	public void setHeight(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuthPath(int treeHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAuthPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
