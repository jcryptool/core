//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.huffmanCoding.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Miray Inel
 * 
 */
public class Node {
	private int name;
	private double value;
	private boolean leaf;
	private Node left;
	private Node right;
	private Node parent;
	private List<Node> connections;
	private String code;

	/**
	 * Creates a node
	 * 
	 * @param name
	 *            name of the node.
	 * @param leaf
	 *            true if leaf false if not
	 */
	public Node(int name, boolean leaf) {
		this.name = name;
		this.leaf = leaf;
		connections = new ArrayList<Node>();
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
	public Node(int name, double value, Node left, Node right) {
		this.name = name;
		this.value = value;
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
	public int getName() {
		return name;
	}

	/**
	 * The string representation of the node
	 * 
	 * @return name as string
	 */
	public String getNameAsString() {
		switch (name) {
		case 0:
			return "NUL"; // Null
		case 9:
			return "TAB"; // Tabulator
		case 10:
			return "LF"; // Line Feed
		case 13:
			return "CR"; // Carriage Return
		case 32:
			return "\u2423"; // Space

		default:
			return String.valueOf((char) name);
		}
	}

	/**
	 * Sets the name of the node
	 * 
	 * @param name
	 */
	public void setName(int name) {
		this.name = name;
	}

	/**
	 * Returns the value of the node
	 * 
	 * @return the value of the node (probability)
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value of the node
	 * 
	 * @param value
	 *            value of the node (probability)
	 */
	public void setValue(double value) {
		this.value = value;
	}

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
	 * Returns the code of the Node only when the node is a leaf
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

	@Override
	public String toString() {
		return "[ " + name + " " + value + " " + code + " " + leaf + " ]";
	}

}
