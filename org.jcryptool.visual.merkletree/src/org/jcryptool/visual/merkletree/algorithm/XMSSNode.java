package org.jcryptool.visual.merkletree.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jcryptool.visual.merkletree.files.Converter;

public class XMSSNode implements Node{

	private byte[] content; //the hash value of the node
	private int height;
	private int index;
	private Node left;
	private Node right;
	private Node parent;
	private List<Node> connections = new ArrayList<Node>();	
	private boolean leaf;
	private String authPath;
	
	
	public XMSSNode(byte[] content){
		this.content = content;
		height = 0;
	}
	
	@Override
	public void setHeight(int i) {
		height=i;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public byte[] getContent(){
		return content;
	}

	@Override
	public int getIndex() {
		return index;
	}
	
	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public Node getLeft() {
		return left;
	}

	@Override
	public void setLeft(Node left) {
		this.left = left;
	}

	@Override
	public Node getRight() {
		return right;
	}

	@Override
	public void setRight(Node right) {
		this.right = right;
	}

	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

	@Override
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
		
	}

	@Override
	public byte[] getName() {
		return getContent();
	}

	@Override
	public String getNameAsString() {
		return getCode();
	}

	@Override
	public void setName(byte[] name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Node> getConnectedTo() {
		return connections;
	}

	@Override
	public String getCode() {
		return Converter._byteToHex(getContent());
	}

	@Override
	public void setCode(String code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLeafNumber(int leafNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLeafNumber() {
		return index;
	}

	
	public boolean equals(Node arg0) {
		if(Arrays.equals(arg0.getContent(), content)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setAuthPath(int treeHeight){
		authPath = Integer.toBinaryString(~index);
		authPath = authPath.substring(authPath.length() - treeHeight);		
	}
		
	public String getAuthPath(){
		return authPath;
	}
	
	
	
	
	
}
