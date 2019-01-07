//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.merkletree.algorithm;

/**
 * @author Hannes Sochor <sochorhannes@gmail.com> Source Code by Moritz Horsch
 *         <horsch@cdc.informatik.tu-darmstadt.de>
 */
public interface OTS {

    public void generateKeyPair();

    public void generatePrivateKey();

    public void generatePublicKey();

    public void sign();

    public boolean verify(String message, String signature);

    public byte[][] getPrivateKey();

    public byte[][] getPublicKey();

    public int getLength();

    public int getMessageLength();

    public byte[] getSignature();

    public byte[] getBi();

    public int getPublicKeyLength();

    public int getN();

    public int getL();

    public byte[] getMessageHash();

    public void setMessage(byte[] message);

    public void setBi(byte[] b);

    public void setSignature(byte[] signature);

    public void setW(int w);

    public byte[] hashMessage(String message);

    public byte[] initB();

    public void setPrivateKey(byte[][] p);

    public void setPublicKey(byte[][] p);

    public void setMessageDigest(String digest);

	public boolean verify();

}
