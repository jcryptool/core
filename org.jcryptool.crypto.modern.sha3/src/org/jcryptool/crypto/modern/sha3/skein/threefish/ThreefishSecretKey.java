// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool team and contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.modern.sha3.skein.threefish;

import static org.jcryptool.crypto.modern.sha3.skein.algorithm.SkeinUtil.lsbBytesToArrayOfLong;

import javax.crypto.SecretKey;

/**
 * JAVADOC .
 *
 * @author maartenb
 * @author $Author: $
 * @since 4 nov 2008
 * @version $Revision: $
 */
public class ThreefishSecretKey implements SecretKey {
	private static final long serialVersionUID = 1L;

	private final long[] keyWords;

	public ThreefishSecretKey(final long[] keyWords) {
		this.keyWords = keyWords.clone();
	}

	/**
	 * Temporary public constructor that builds a TreeFish compatible secret
	 * key. To be replaced by a factory.
	 *
	 * @param keyBytes
	 *            JAVADOC .
	 */
	public ThreefishSecretKey(final byte[] keyBytes) {
		if (keyBytes == null) {
			throw new IllegalArgumentException(
					"Please supply key data to this constructor (null provided)");
		}

		final int keySizeBits = keyBytes.length * Byte.SIZE;
		switch (keySizeBits) {
		case ThreefishImpl.BLOCK_SIZE_BITS_256:
		case ThreefishImpl.BLOCK_SIZE_BITS_512:
		case ThreefishImpl.BLOCK_SIZE_BITS_1024:
			keyWords = lsbBytesToArrayOfLong(keyBytes);
			break;
		default:
			throw new IllegalArgumentException(
					"Key data size is invalid, it should be"
							+ " either 256, 512 or 1024 bits long");
		}

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.security.Key#getAlgorithm()
	 */
	public String getAlgorithm() {
		return null;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.security.Key#getEncoded()
	 */

	public byte[] getEncoded() {
		return null;

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.security.Key#getFormat()
	 */

	public String getFormat() {
		return null;

	}

	public void getKeyWords(final long[] keyWordsBuffer) {
		for (int i = 0; i < keyWords.length; i++) {
			keyWordsBuffer[i] = keyWords[i];
		}
	}

	public int getKeySizeInWords() {
		return keyWords.length;
	}
}