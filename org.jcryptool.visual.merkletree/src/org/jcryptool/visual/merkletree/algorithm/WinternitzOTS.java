package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.MathUtils;

/**
 * @author Hannes Sochor <sochorhannes@gmail.com> Source Code by Moritz Horsch
 *         <horsch@cdc.informatik.tu-darmstadt.de>
 */
public class WinternitzOTS implements OTS {

	// Lengths
	private int m, l, l1, l2;
	// Winternitz parameter
	private int w;
	// Security parameter
	private int n;
	// Private key
	private byte[][] privateKey;
	// Public key
	private byte[][] publicKey;
	// Message digest
	private MessageDigest digest;
	// Signature
	private byte[] signature;
	// hashed message
	private byte[] messageHash;
	// Bitstring b
	private byte[] b;

	/**
	 * Creates a new Winternitz OTS.
	 *
	 * @param w
	 *            Winternitz parameter w
	 */
	public WinternitzOTS(int w, String hash) {

		// Try to set up hash-function
		try {
			digest = MessageDigest.getInstance(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		// Set winternitz parameter and block-length
		this.w = w;
		this.n = digest.getDigestLength();

		// Calculate m, l, l1, l2
		calculateLengths();
	}

	/**
	 * Sets the message digest to hash messages.
	 *
	 * @param digest
	 *            Message digest
	 */
	@Override
	public void setMessageDigest(String digest) {

		try {
			this.digest = MessageDigest.getInstance(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		this.n = this.digest.getDigestLength();

		// Update lengths
		calculateLengths();
	}

	/**
	 * Generates a key pair.
	 *
	 * @param seed
	 *            Seed
	 */
	@Override
	public void generateKeyPair() {
		generatePrivateKey();
		generatePublicKey();
	}

	/**
	 * Generates the private key.
	 *
	 * @param seed
	 *            Seed
	 */
	@Override
	public void generatePrivateKey() {

		privateKey = new byte[l][n];

		byte[] rand = new byte[n];
		SecureRandom sRandom = new SecureRandom();

		for (int i = 0; i < l; i++) {

			rand = new byte[n];
			sRandom.nextBytes(rand);
			privateKey[i] = rand;
		}
	}

	/**
	 * Generates the public key.
	 *
	 */
	@Override
	public void generatePublicKey() {

		publicKey = new byte[l][n];

		// Hash each part of private key w-1 times
		for (int i = 0; i < l; i++) {

			System.arraycopy(privateKey[i], 0, publicKey[i], 0, publicKey[i].length);

			for (int j = 0; j < w - 1; j++) {
				publicKey[i] = calcHash(publicKey[i]);
			}
		}
	}

	/**
	 * Signs a message.
	 *
	 * @param message
	 *            Message
	 * @return Signature of the message
	 */
	@Override
	public void sign() {

		byte[][] tmpSignature = new byte[l][n];

		// Hash each part of the private key bi times
		for (int i = 0; i < l; i++) {

			tmpSignature[i] = this.privateKey[i];

			for (int j = 0; j < (b[i] & 0xFF); j++) {
				tmpSignature[i] = calcHash(tmpSignature[i]);
			}
		}

		signature = org.jcryptool.visual.merkletree.files.Converter
				._hexStringToByte(org.jcryptool.visual.merkletree.files.Converter._2dByteToHex(tmpSignature));
	}

	/**
	 * Verifies a the signature of a message.
	 *
	 * @param message
	 *            Message
	 * @param signature
	 *            Signature
	 * @return True if the signature is valid, otherwise false
	 */
	@Override
	public boolean verify() {

		byte[][] tmpSignature = org.jcryptool.visual.merkletree.files.Converter
				._hexStringTo2dByte((org.jcryptool.visual.merkletree.files.Converter._byteToHex(signature)), l);

		// Hash each part of the signature w-1-bi times and verifies it with
		// public Key
		for (int i = 0; i < l; i++) {

			for (int j = 0; j < (w - 1 - (b[i] & 0xFF)); j++) {
				tmpSignature[i] = calcHash(tmpSignature[i]);
			}

			// Compare sigma_i with pk_i
			if (!Arrays.equals(tmpSignature[i], publicKey[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Calculate the lengths l1, l2, and l.
	 */
	private void calculateLengths() {

		m = digest.getDigestLength() * 8;
		l1 = (int) Math.ceil(m / MathUtils.log2nlz(w));
		l2 = (int) Math.floor(MathUtils.log2nlz(l1 * (w - 1)) / MathUtils.log2nlz(w)) + 1;
		l = l1 + l2;
	}

	/**
	 * Calculate the exponent b (b_1, ..., b_l).
	 *
	 * @param message
	 *            Message
	 * @return Exponent b
	 */
	private void calculateExponentB() {

		// Convert message to base w representation
		byte[] mBaseW = convertToBaseW(messageHash, l1);

		// Calculate checksum c
		BigInteger checksum = BigInteger.ZERO;
		for (int i = 0; i < l1; i++) {
			checksum = checksum.add(BigInteger.valueOf(w - 1 - (mBaseW[i] & 0xFF)));
		}

		// Convert checksum to base w representation
		byte[] checksumBaseW = convertToBaseW(checksum.toByteArray(), l2);

		// Concatenate message and checksum
		byte[] b = ByteUtils.concatenate(mBaseW, checksumBaseW);

		this.b = b;
	}

	/**
	 * Convert the input to a base w representation.
	 *
	 * @param input
	 *            Input
	 * @param length
	 *            Length of the output
	 * @return Base w representation of the input
	 */
	private byte[] convertToBaseW(byte[] input, int length) {

		BigInteger i = new BigInteger(1, input);
		BigInteger b = BigInteger.valueOf(w);
		ArrayList<Byte> result = new ArrayList<Byte>();

		while (i.compareTo(BigInteger.ZERO) != 0) {
			result.add(i.mod(b).byteValue());
			i = i.divide(b);
		}

		byte[] ret = new byte[length];
		for (int j = (length - result.size()); j < ret.length; j++) {
			ret[j] = result.get(ret.length - j - 1).byteValue();
		}

		return ret;
	}

	/**
	 * Returns the private key.
	 *
	 * @return Private key
	 */
	@Override
	public byte[][] getPrivateKey() {
		return privateKey;
	}

	/**
	 * Returns the public key.
	 *
	 * @return Public key
	 */
	@Override
	public byte[][] getPublicKey() {
		return publicKey;
	}

	/**
	 * Returns the length l. The size of the signature, public, and private key
	 * is l * n.
	 *
	 * @return Length l
	 */
	@Override
	public int getLength() {
		return l;
	}

	/**
	 * returns the public Key Length
	 */
	@Override
	public int getPublicKeyLength() {
		return l;
	}

	/**
	 * Returns the message length.
	 *
	 * @return Message length
	 */
	@Override
	public int getMessageLength() {
		return m;
	}

	/**
	 * returns the signature
	 */
	@Override
	public byte[] getSignature() {
		return signature;
	}

	/**
	 * returns blocklength n
	 */
	@Override
	public int getN() {
		return n;
	}

	/**
	 * returns l
	 */
	@Override
	public int getL() {
		return l;
	}

	/**
	 * returns hashed message
	 */
	@Override
	public byte[] getMessageHash() {
		return this.messageHash;
	}

	/**
	 * returns Bitstring bi
	 */
	@Override
	public byte[] getBi() {
		return b;
	}

	/**
	 * hashes given message and set new message hash
	 */
	@Override
	public void setMessage(byte[] message) {
		this.messageHash = message;
	}

	/**
	 * Sets new bi
	 */
	@Override
	public void setBi(byte[] b) {
		this.b = b;
	}

	/**
	 * sets new signature
	 */
	@Override
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	/**
	 * sets new w and calculates new length
	 */
	@Override
	public void setW(int w) {
		this.w = w;
		calculateLengths();
	}

	/**
	 * hashes message and sets new message hash + returns it
	 */
	@Override
	public byte[] hashMessage(String message) {
		this.messageHash = digest.digest(org.jcryptool.visual.merkletree.files.Converter._stringToByte(message));
		return messageHash;
	}

	/**
	 * returns new calculated Bitstring b
	 */
	@Override
	public byte[] initB() {
		calculateExponentB();
		return b;
	}

	/**
	 * Allows to set a custom Private Key
	 * 
	 * @param p
	 */
	@Override
	public void setPrivateKey(byte[][] p) {
		this.privateKey = p;
	}

	/**
	 * Allows to set a custom Public Key
	 * 
	 * @param p
	 */
	@Override
	public void setPublicKey(byte[][] p) {
		this.publicKey = p;
	}

	/**
	 * Generates a hash-String to a given String
	 * 
	 * @return
	 */
	private byte[] calcHash(byte[] tmp) {

		String base = org.jcryptool.visual.merkletree.files.Converter._byteToHex(tmp);

		try {
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return org.jcryptool.visual.merkletree.files.Converter._hexStringToByte(hexString.toString());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
