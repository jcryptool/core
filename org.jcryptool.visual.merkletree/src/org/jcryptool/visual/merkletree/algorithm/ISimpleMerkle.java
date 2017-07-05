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

import java.util.ArrayList;

public interface ISimpleMerkle {
	// Variablen

	public void setSeed(byte[] seed);

	public byte[] getMerkleRoot();

	public byte[] getSeed();

	public int getLeafCounter();

	public boolean isGenerated();

	public Node getTreeLeaf(int treeLeaveNumber);

	public void generateKeyPairsAndLeaves();

	public ArrayList<Node> getTree();

	public void generateMerkleTree();

	public String sign(String message);

	public boolean verify(String message, String signature);

	public boolean verify(String message, String signature, int keyIndex);
	// Options

	public void setIndex(int i);

	public void selectOneTimeSignatureAlgorithm(String hash, String algo);

	public void setLeafCount(int i);

	public int getKeyLength();

	public void setWinternitzParameter(int w);

	public int getKeyIndex();

	public String getPrivateKey();

	public String getPublicKey();
}
