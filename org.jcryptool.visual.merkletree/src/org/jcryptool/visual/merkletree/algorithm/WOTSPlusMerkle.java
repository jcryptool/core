package org.jcryptool.visual.merkletree.algorithm;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.jcryptool.visual.merkletree.files.ByteUtils;
import org.jcryptool.visual.merkletree.files.MathUtils;

public class WOTSPlusMerkle implements OTS {
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
	// signature
	private byte[] signature;
	// hashed message
	private byte[] messageHash;
	// Bitstring b
	private byte[] b;
	// private Seed
	private byte[] seed;
	// Counter
	private int seedCount;
	// Seed digest
	private MessageDigest sDigest;

	/**
	 * Creates a new Winternitz OTS.
	 *
	 * @param w
	 *            Winternitz parameter w
	 */
	public WOTSPlusMerkle(int w, String hash, byte[] seed) {

		// Try to set up hash-function
		try {
			digest = MessageDigest.getInstance(hash);
			this.sDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		// Set winternitz parameter and block-length
		this.w = w;
		this.n = digest.getDigestLength();
		// Set Private Seed + Counter
		this.seed = seed;
		this.seedCount = 0;
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
		byte[] pSeed;
		byte[] rand = new byte[n];
		Random sRandom = new Random();
		pSeed = this.generateSeed();
		long value = 0;
		for (int i = 0; i < pSeed.length; i++)
		{
		   value = (value << 8) + (pSeed[i] & 0xff);
		}
		sRandom.setSeed(value);
		for (int i = 0; i < l; i++) {

			rand = new byte[n];
			pSeed = this.generateSeed();
			value=0;
			for (int j = 0; j < pSeed.length; j++)
			{
			   value = (value << 8) + (pSeed[j] & 0xff);
			}
			sRandom.setSeed(value);
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

		publicKey = new byte[l + w - 1][n];

		byte[] seed = new byte[n];
		SecureRandom sRandom = new SecureRandom();

		// Fills first w-1 blocks of public key with random values ri
		for (int i = 0; i < w - 1; i++) {
			seed = new byte[n];
			sRandom.nextBytes(seed);
			publicKey[i] = seed;
		}

		// Hash + xor with ri each part w-1 times
		for (int i = w - 1; i < l + w - 1; i++) {

			System.arraycopy(privateKey[i - (w - 1)], 0, publicKey[i], 0, publicKey[i].length);

			for (int j = 0; j < w - 1; j++) {

				for (int k = 0; k < publicKey[i].length; k++)
					publicKey[i][k] = (byte) (publicKey[i][k] ^ publicKey[j][k]);

				publicKey[i] = calcHash(publicKey[i]);
			}
		}
	}

	/**
	 * Signs a message. Both Keys have to be set
	 *
	 * @param message
	 *            Message
	 * @return Signature of the message
	 */
	@Override
	public void sign() {

		byte[][] tmpSignature = new byte[l][n];

		// Hash + xor with ri each part bi times
		for (int i = 0; i < l; i++) {

			System.arraycopy(privateKey[i], 0, tmpSignature[i], 0, tmpSignature[i].length);
			// tmpSignature[i] = this.privateKey[i];

			for (int j = 0; j < (b[i] & 0xFF); j++) {

				for (int k = 0; k < tmpSignature[i].length; k++)
					tmpSignature[i][k] = (byte) (tmpSignature[i][k] ^ publicKey[j][k]);

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

		// Hash + xor each part w-1-bi times and verifies it with public Key
		for (int i = 0; i < l; i++) {

			for (int j = 0; j < (w - 1 - (b[i] & 0xFF)); j++) {

				for (int k = 0; k < tmpSignature[i].length; k++)
					tmpSignature[i][k] = (byte) (tmpSignature[i][k] ^ publicKey[j + (b[i] & 0xFF)][k]);

				tmpSignature[i] = calcHash(tmpSignature[i]);
			}

			// Compare sigma_i with pk_i
			if (!Arrays.equals(tmpSignature[i], publicKey[i + w - 1])) {
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
	 * Returns the length l.
	 * 
	 * @return Length l
	 */
	@Override
	public int getLength() {
		return l;
	}

	/**
	 * returns public key length of WOTS+
	 */
	@Override
	public int getPublicKeyLength() {
		return l + w - 1;
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
	 * returns Bitstring bi
	 * 
	 * @return
	 */
	@Override
	public byte[] getBi() {
		return b;
	}

	/**
	 * returns the signature
	 */
	@Override
	public byte[] getSignature() {
		return signature;
	}

	/**
	 * returns hashed message
	 */
	@Override
	public byte[] getMessageHash() {
		return this.messageHash;
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
	 * hashes message and set as new message
	 */
	@Override
	public void setMessage(byte[] message) {
		this.messageHash = message;
	}

	/**
	 * sets new bi
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
	 * sets new w andd calculates new lengths
	 */
	@Override
	public void setW(int w) {
		this.w = w;
		calculateLengths();
	}

	/**
	 * hashes message, set as new message and returns hash
	 */
	@Override
	public byte[] hashMessage(String message) {
		this.messageHash = digest.digest(org.jcryptool.visual.merkletree.files.Converter._stringToByte(message));
		return messageHash;
	}

	/**
	 * returns new calculated Bitstring bi
	 */
	@Override
	public byte[] initB() {
		calculateExponentB();
		return b;
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

	private byte[] generateSeed() {

		// Seed||Keyadresse||counter
		byte[] bCount = ByteBuffer.allocate(4).putInt(seedCount).array();
		byte[] keyAdrs = new byte[256 - this.seed.length - bCount.length];
		Arrays.fill(keyAdrs, (byte) 0);
		byte[] merge = ByteUtils.concatenate(this.seed, keyAdrs);

		byte[] hash = sDigest.digest(ByteUtils.concatenate(merge, bCount));
		this.seedCount++;
		return hash;
	}
}
