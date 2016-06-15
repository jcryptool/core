package org.jcryptool.visual.merkletree.algorithm;

import java.util.List;

public interface Node {

	public boolean isLeaf();
	
	public Node getLeft();
	
	public void setLeft(Node left);
	
	public Node getRight();
	
	public void setRight(Node right);
	
	public void setLeaf(boolean leaf);
	
	public byte[] getName();
	
	public String getNameAsString();
	
	public void setName(byte[] name);
	
	public Node getParent();
	
	public void setParent(Node parent);
	
	public List<Node> getConnectedTo();
	
	public String getCode();
	
	public void setCode(String code);
	
	public void setLeafNumber(int leafNumber);
	
	public int getLeafNumber();

	public String toString();
	
	public void setHeight(int i);
	
	public int getHeight();
	
	public byte[] getContent();

	public int getIndex();
	
	public void setIndex(int index);
	
	public void setAuthPath(int treeHeight);
	
	public String getAuthPath();
}
