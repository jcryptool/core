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
package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import org.jcryptool.visual.merkletree.files.ByteUtils;

public class SimpleMerkleTree implements ISimpleMerkle {

    int keyIndex;
    byte[] seed;
    boolean treeGenerated;
    OTS otsAlgo;

    ArrayList<Node> tree = new ArrayList<Node>();
    ArrayList<Node> leaves = new ArrayList<Node>();

    MessageDigest mDigest;

    ArrayList<byte[][]> privKeys = new ArrayList<byte[][]>();
    ArrayList<byte[][]> publicKeys = new ArrayList<byte[][]>();

    int leafCounter = 0;
    int leafNumber = 0;

    OTSHashAddress otsAdrs = new OTSHashAddress();

    public SimpleMerkleTree() {
        this.treeGenerated = false;
        this.keyIndex = 0;
    }

    @Override
    public void setSeed(byte[] seed) {
        this.seed = seed;

    }

    @Override
    public byte[] getSeed() {
        return seed;
    }

    @Override
    public byte[] getMerkleRoot() {
        for (int i = 0; i < tree.size(); i++) {
            if (tree.get(i).getParent() == null) {
                return tree.get(i).getName();
            }
        }
        return null;
        // return merkleTreeHeight.get(getTreeHeight()).get(0).getContent();
    }

    @Override
    public int getLeafCounter() {
        return leafCounter;
    }

    @Override
    public ArrayList<Node> getTree() {
        return this.tree;
    }

    @Override
    public boolean isGenerated() {
        return treeGenerated;
    }

    @Override
    public Node getTreeLeaf(int treeLeafNumber) {
        return leaves.get(treeLeafNumber);
    }

    @Override
    public void generateMerkleTree() {

        int height = getTreeHeight();

        if (height == 0) {
            return;
        }
        // int tHorizontal = leafCounter;

        tree = new ArrayList<Node>();
        tree.addAll(leaves);
        Node helperNode;
        ArrayList<Node> treeLevel = new ArrayList<Node>();
        int index = 0;
        int levelCount;
        int NodeLevelCounter = tree.size();
        int treeIndex = 0;
        for (levelCount = 0; levelCount < height; levelCount++) {
            treeLevel = new ArrayList<Node>();
            NodeLevelCounter = (int) Math.round(NodeLevelCounter / 2.0);

            for (index = 0; index < NodeLevelCounter; index++, treeIndex += 2) {
                if (treeIndex + 1 < tree.size()) {
                    byte[] content = hashingContent(tree.get(treeIndex), tree.get(treeIndex + 1));
                    helperNode = new SimpleNode(content, tree.get(treeIndex), tree.get(treeIndex + 1));
                    treeLevel.add(helperNode);
                    treeLevel.get(index).getConnectedTo().add(tree.get(treeIndex));
                    treeLevel.get(index).getConnectedTo().add(tree.get(treeIndex + 1));
                    tree.get(treeIndex).setParent(treeLevel.get(index));
                    tree.get(treeIndex + 1).setParent(treeLevel.get(index));

                    // zuck: deadcode/falsch?
                } else {
                    byte[] content = hashingContent(tree.get(treeIndex), tree.get(treeIndex));
                    helperNode = new SimpleNode(content, false, 0);
                    helperNode.setLeft(tree.get(treeIndex));
                    treeLevel.add(helperNode);
                    treeLevel.get(index).getConnectedTo().add(tree.get(treeIndex));
                    tree.get(treeIndex).setParent(treeLevel.get(index));
                }

            }
            treeIndex = tree.size(); // unnütz
            tree.addAll(treeLevel);
        }
        treeGenerated = true;
    }

    byte[] hashingContent(Node a, Node b) {
        byte[] toHash = appendByteArrays(a.getName(), b.getName());

        return mDigest.digest(toHash);
    }

    byte[] appendByteArrays(byte[] array1, byte[] array2) {
        byte[] appended;
        String String1 = array1.toString();
        String String2 = array2.toString();
        String String3 = String1 + String2;

        appended = String3.getBytes();

        return appended;
    }

    public int getTreeHeight() {
        return Integer.bitCount(Integer.highestOneBit(this.leafCounter - 1) * 2 - 1);
    }

    @Override
    public void selectOneTimeSignatureAlgorithm(String hash, String algo) {
        switch (algo) {
        case "WOTS":
            this.otsAlgo = new WinternitzOTS(16, hash);
            break;
        case "WOTSPlus":
            this.otsAlgo = new WOTSPlus(16, hash, this.seed);
            break;
        default:
            this.otsAlgo = new WOTSPlus(16, hash, this.seed);
            break;
        }
        if (this.mDigest == null) {
            try {
                mDigest = MessageDigest.getInstance(hash);
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public String sign(String message) {
        // String tmpSignature="";
        int iHeight = this.keyIndex;
        int treeHeight = tree.size();

        byte[] messageHash = randomGenerator(BigInteger.valueOf(keyIndex).toByteArray(), message.getBytes(),
                message.length());
        this.otsAlgo.setPrivateKey(this.privKeys.get(this.keyIndex));
        this.otsAlgo.setPublicKey(this.publicKeys.get(this.keyIndex));

        byte[][] ots_sig = ((WOTSPlus) otsAlgo).sign(messageHash, seed, otsAdrs);

        String tmpSignature = Integer.toString(this.keyIndex) + "|";
        tmpSignature += org.jcryptool.visual.merkletree.files.Converter._2dByteToHex(ots_sig);

        while (iHeight < treeHeight - 1) {
            if (this.tree.get(iHeight).getParent().getLeft().equals(this.tree.get(iHeight))) {
                tmpSignature = tmpSignature + '|' + this.tree.get(iHeight).getParent().getRight().getNameAsString();
            } else if (this.tree.get(iHeight).getParent().getRight().equals(this.tree.get(iHeight))) {
                tmpSignature = tmpSignature + '|' + this.tree.get(iHeight).getParent().getLeft().getNameAsString();
            }
            iHeight = this.tree.lastIndexOf(this.tree.get(iHeight).getParent());
        }
        this.keyIndex++;
        return tmpSignature; // OTS Signatur+tmpSignature to byte array
    }

    @Override
    public boolean verify(String message, String signature) {
        String[] signer = signature.split("\\|");
        int keyIndex = Integer.parseInt(signer[0]);
        // set OTS Algorithm values
        this.otsAlgo.setPrivateKey(this.privKeys.get(this.keyIndex));
        this.otsAlgo.setPublicKey(this.publicKeys.get(this.keyIndex));

        int iHigh = keyIndex;
        // String currentAuthPath="";
        byte[] currentNode = leaves.get(keyIndex).getName();
        int treeHigh = tree.size();
        while (iHigh < treeHigh - 1) {
            if (this.tree.get(iHigh).getParent().getLeft().equals(this.tree.get(iHigh))) {
                // currentAuthPath=this.tree.get(iHigh).getParent().getRight().getNameAsString();
                currentNode = this.createNode(currentNode, this.tree.get(iHigh).getParent().getRight().getName());

                if (!Arrays.equals(currentNode, this.tree.get(iHigh).getParent().getName())) {
                    return false;
                } else {
                    currentNode = this.tree.get(iHigh).getParent().getName();
                }
            } else if (this.tree.get(iHigh).getParent().getRight().equals(this.tree.get(iHigh))) {
                // currentAuthPath = this.tree.get(iHigh).getParent().getLeft().getNameAsString();
                currentNode = this.createNode(this.tree.get(iHigh).getParent().getLeft().getName(), currentNode);
                if (!Arrays.equals(currentNode, this.tree.get(iHigh).getParent().getName())) {
                    return false;
                } else {
                    currentNode = this.tree.get(iHigh).getParent().getName();
                }
            }
            iHigh = this.tree.lastIndexOf(this.tree.get(iHigh).getParent());
        }
        return true;
    }

    public boolean verify(String message, String signature, int markedLeafIndex) {
        String[] signer = signature.split("\\|");
        int keyIndex = Integer.parseInt(signer[0]);
        if (markedLeafIndex != keyIndex) {
            return false;
        } else
            return this.verify(message, signature);

    }

    public byte[] createNode(byte[] node1, byte[] node2) {
        byte[] toHash = appendByteArrays(node1, node2);

        return mDigest.digest(toHash);

    }

    public void generateKeyPairsAndLeaves() {
        Node leaf;
        byte[] d1pubKey;
        String code;
        for (int i = 0; i < this.leafCounter; i++) {
            // generates a new WOTS/ WOTSPlus Keypair (public and secret key)
            if (otsAlgo instanceof WOTSPlus) {
                ((WOTSPlus) otsAlgo).setAddress(otsAdrs);
            }
            this.otsAlgo.generateKeyPair();
            this.privKeys.add(this.otsAlgo.getPrivateKey());
            this.publicKeys.add(this.otsAlgo.getPublicKey());
            d1pubKey = org.jcryptool.visual.merkletree.files.Converter._hexStringToByte(
                    org.jcryptool.visual.merkletree.files.Converter._2dByteToHex(this.otsAlgo.getPublicKey()));
            leaf = new SimpleNode(this.mDigest.digest(d1pubKey), true, i);
            this.leafNumber++;
            code = org.jcryptool.visual.merkletree.files.Converter._byteToHex(d1pubKey).substring(0, 5);
            code += "...";
            code += org.jcryptool.visual.merkletree.files.Converter._byteToHex(d1pubKey).substring(d1pubKey.length - 5,
                    d1pubKey.length);
            leaf.setCode(code);
            leaf.setAuthPath(getTreeHeight());
            this.leaves.add(leaf);
        }

    }

    public void setLeafCount(int i) {
        leafCounter = i;
    }

    /**
     * @author zuck PRNG used to generate the bitmasks and the key for hashing
     * @param seed seed for the PRNG
     * @param address address of left/right node
     */
    public byte[] randomGenerator(byte[] seed, byte[] address, int len) {
        byte[] res = new byte[len + 32]; // erstellen des zu befüllenden arrays
        byte[] padding = new byte[32];
        MessageDigest hash = null;
        try {
            hash = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // zuck: Der Algo existiert!
        }
        seed = ByteUtils.concatenate(padding, seed);
        seed = ByteUtils.concatenate(seed, address);
        res = hash.digest(seed);
        return res;
    }

    /**
     * Calculate the Key lenght of the created Keys in Byte
     */
    public String getKeyLength() {
        /*
         * Convert the Keys to a String and concanate them
         */
        String keys = "";
        for (int i = 0; i < privKeys.size(); i++) {
            keys += (ByteUtils.toHexString(privKeys.get(i)));
        }

        for (int i = 0; i < publicKeys.size(); i++) {
            keys += (ByteUtils.toHexString(publicKeys.get(i)));
        }

        /*
         * calculate the lenght of the Keys
         */

        int length = keys.length();
        length = length / 2;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(length);
        String keyLength = sb.toString();
        return keyLength;

    }

}
