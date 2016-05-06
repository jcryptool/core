package org.jcryptool.visual.merkletree.algorithm;

public class XMSSNode {

	private byte[] content; //the hash value of the node
	private int height;
	
	public XMSSNode(byte[] content){
		this.content = content;
		height = 0;
	}
	
	public void setHeight(int i) {
		height=i;
	}
	
	public int getHeight() {
		return height;
	}
	
	public byte[] getContent(){
		return content;
	}
}
