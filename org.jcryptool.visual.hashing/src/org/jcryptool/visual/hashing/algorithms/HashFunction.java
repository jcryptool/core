package org.jcryptool.visual.hashing.algorithms;

/**
 * 
 * @author Ferit Dogan
 * 
 */
public enum HashFunction {
	MD2("MD2 (128 bits)"), MD4("MD4 (128 bits)"), MD5("MD5 (128 bits)"), SHA1("SHA-1 (160 bits)"), SHA256( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"SHA-256 (256 bits)"), SHA512("SHA-512 (512 bits)"), RIPEMD160("RIPEMD-160 (160 bits)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private final String hashFunctionName;

	private HashFunction(String name) {
		hashFunctionName = name;
	}

	public HashFunction getName(String name) {
		for (HashFunction h : values()) {
			if (h.hashFunctionName.compareToIgnoreCase(name) == 0) {
				HashFunction value = valueOf(h.name());
				return value;
			}
		}
		return null;
	}
}
