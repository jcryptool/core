package org.jcryptool.visual.merkletree.algorithm;

public class MerkleTreeNode {
    byte[] content;
    int height;
    int width;

    MerkleTreeNode(int height, int width, byte[] content) {
        this.content = content;
        this.height = height;
        this.width = width;
    }

    int getHeight() {

        return this.height;
    }

    int getWidth() {
        return this.width;
    }

    byte[] getContent() {

        return this.content;
    }

    public String contentAsString() {
        return content.toString();

    }
}
