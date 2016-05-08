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
import org.jcryptool.visual.merkletree.files.Converter;
import org.jcryptool.visual.merkletree.files.MathUtils;



public class WOTSPlusXMSS implements OTS{
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
	
	private OTSHashAddress otsAdrs;

	/**
	 * Creates a new Winternitz OTS.
	 *
	 * @param w
	 *            Winternitz parameter w
	 */
	public WOTSPlusXMSS(int w, String hash, byte[] seed) {

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
	
	public void setAddress(OTSHashAddress otsAdrs){
		this.otsAdrs = otsAdrs;
	}

	/**
	 * Generates a key pair.
	 *
	 * @param seed
	 *            Seed
	 */
	
	public void generateKeyPair() {
		
		generatePrivateKey();
		generatePublicKey();
	}

	
	/**
	 * Convert the input to a base w representation.
	 *
	 * @param input
	 *            Input
	 * @param base
	 *            base of the output
	 * @return Base w representation of the input
	 */
	private byte[] convertToBaseW(byte[] input, int base) {

		int in = 0;
		int out = 0;
		long total = 0; //unsigned int in RFC thus long
		int bits = 0;
		int consumed;
		byte[] basew = new byte[(8 * input.length) / MathUtils.log2nlz(w)];
		
		for(consumed = 0; consumed < 8 * input.length; consumed += MathUtils.log2nlz(base)) {
			if(bits == 0) {
				total = input[in];
				in++;
				bits += 8;
			}
			bits -= MathUtils.log2nlz(base);
			basew[out] = (byte)((total >> bits) & (w-1) );
			out++;
		}		
		
		return basew;
	}
	
	/**
	 * 
	 * @param input
	 * 			n-byte string to calculate the hash of
	 * @param i
	 * 			start index
	 * @param s
	 * 			number of steps
	 * @param seed
	 * 			the seed for the bitmask and key generation
	 * @param otsAdrs
	 * 			the address construct for the bitmask and key generation
	 * @return
	 * 			the s times hashed input
	 */
	public byte[] chain(byte[] input, int i, int s, byte[] seed, OTSHashAddress otsAdrs){
		byte[] tmp = new byte[input.length];
		byte[] bitmask = new byte[input.length];
		byte[] key = new byte[input.length];
		
		if(s == 0) {
			return input;
		}
		tmp = chain(input, i, s-1, seed, otsAdrs);
		otsAdrs.setHashAdress(i+s-1);
		otsAdrs.setKeyBit(false);
		bitmask = randomGenerator(seed, otsAdrs.getAddress(), tmp.length);
		otsAdrs.setKeyBit(true);
		key = randomGenerator(seed, otsAdrs.getAddress(), tmp.length);
		for(int j = 0; j < tmp.length; j++){
			tmp[j] ^= bitmask[j]; 
		}
		tmp = hashMessage(key, tmp);
		return tmp;		
	}
	
	/**
	 * Generates the private key.
	 *
	 * @return
	 * 		returns a secret key
	 */
	
	public void generatePrivateKey() {

		byte[][] privateKey = new byte[l][n];
		Random sRandom = new Random();
		for(int i = 0; i < privateKey.length; i++) {
			sRandom.nextBytes(privateKey[i]);
		}
		this.privateKey = privateKey;
	}
		

	/**
	 * Generates the public key.
	 *
	 */
	
	public void generatePublicKey() {
		byte[][] publicKey = new byte[l][n];
		
		for( int i = 0; i < privateKey.length; i++) {
			otsAdrs.setChainAddress(i);
			publicKey[i] = chain(privateKey[i], 0, w-1, seed, otsAdrs);			
		}
		this.publicKey = publicKey;
	}

	/**
	 * Signs a message. Both Keys have to be set
	 *
	 * @param message
	 *            Message
	 * @return Signature of the message
	 */
	
	public byte[][]sign(byte[] message, byte[] seed, OTSHashAddress otsAdrs) {

		byte[][] signature = new byte[l][n];
		long csum = 0;	//checksum is byte[] for compatibility with basew method
		int l2_bytes;
		byte[] messageW = new byte[l]; 
		
		
		messageW = convertToBaseW(message, w);
		
		//compute checksum
		for( int i = 0; i < messageW.length; i++) {
			csum = csum + w - 1 - messageW[i];
		}
		
		//convert csum to base w
		csum = csum << ( 8 -( ( l2 * MathUtils.log2nlz(w) ) % 8 ));
		l2_bytes = (int)Math.ceil( ( l2 * MathUtils.log2nlz(w)) / 8.0);
		byte[] csum_bytes = new byte[l2_bytes];
		//copnvert csum to byte[]
		csum_bytes = BigInteger.valueOf(csum).toByteArray();
		csum_bytes = convertToBaseW(csum_bytes, w);
		messageW = ByteUtils.concatenate(messageW, csum_bytes);
		for (int i = 0; i < l; i++){
			otsAdrs.setChainAddress(i);
			signature[i] = chain(privateKey[i], 0, messageW[i], seed, otsAdrs);
			
		}
		return signature;
	}
	
	
	public byte[][] pkFromSig(String sig, byte[] message, byte[] seed, OTSHashAddress otsAdrs) {

		byte[][] pk = new byte[l][n];
		long csum = 0;	//checksum is byte[] for compatibility with basew method
		int l2_bytes;
		byte[] messageW = new byte[l]; 
		byte[][] signature = Converter._hexStringTo2dByte(sig, l);
		
		messageW = convertToBaseW(message, w);		
		//compute checksum
		for( int i = 0; i < messageW.length; i++) {
		
			csum = csum + w - 1 - messageW[i];
			
		}
		
		//convert csum to base w
		csum = csum << ( 8 -( ( l2 * MathUtils.log2nlz(w) ) % 8 ));
		l2_bytes = (int)Math.ceil( ( l2 * MathUtils.log2nlz(w)) / 8.0);
		byte[] csum_bytes = new byte[l2_bytes];
		//copnvert csum to byte[]
		csum_bytes = BigInteger.valueOf(csum).toByteArray();
		csum_bytes = convertToBaseW(csum_bytes, w);
		messageW = ByteUtils.concatenate(messageW, csum_bytes);
		for (int i = 0; i < l; i++){
			otsAdrs.setChainAddress(i);
			pk[i] = chain(signature[i], messageW[i], w-1-messageW[i], seed, otsAdrs);		//BIgInteger is used to convert byte[] to integer
			
		}
		return pk;
		
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
		l1 = (int) Math.ceil((double)m / MathUtils.log2nlz(w));
		l2 = (int) Math.floor(MathUtils.log2nlz(l1 * (w - 1)) / (double)MathUtils.log2nlz(w)) + 1;
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
	 * Returns the private key.
	 *
	 * @return Private key
	 */
	
	public byte[][] getPrivateKey() {
		return privateKey;
	}

	/**
	 * Returns the public key.
	 *
	 * @return Public key
	 */
	
	public byte[][] getPublicKey() {
		return publicKey;
	}

	/**
	 * Returns the length l.
	 * 
	 * @return Length l
	 */
	
	public int getLength() {
		return l;
	}

	/**
	 * returns public key length of WOTS+
	 */
	
	public int getPublicKeyLength() {
		return l + w - 1;
	}

	/**
	 * Returns the message length.
	 *
	 * @return Message length
	 */
	
	public int getMessageLength() {
		return m;
	}

	/**
	 * returns Bitstring bi
	 * 
	 * @return
	 */
	
	public byte[] getBi() {
		return b;
	}

	/**
	 * returns the signature
	 */
	
	public byte[] getSignature() {
		return signature;
	}

	/**
	 * returns hashed message
	 */
	
	public byte[] getMessageHash() {
		return this.messageHash;
	}

	/**
	 * returns blocklength n
	 */
	
	public int getN() {
		return n;
	}

	/**
	 * returns l
	 */
	
	public int getL() {
		return l;
	}

	/**
	 * Allows to set a custom Private Key
	 * 
	 * @param p
	 */
	
	public void setPrivateKey(byte[][] p) {
		this.privateKey = p;
	}

	/**
	 * Allows to set a custom Public Key
	 * 
	 * @param p
	 */
	
	public void setPublicKey(byte[][] p) {
		this.publicKey = p;
	}

	/**
	 * hashes message and set as new message
	 */
	
	public void setMessage(byte[] message) {
		this.messageHash = message;
	}

	/**
	 * sets new bi
	 */
	
	public void setBi(byte[] b) {
		this.b = b;
	}

	/**
	 * sets new signature
	 */
	
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	/**
	 * sets new w and calculates new lengths
	 */
	
	public void setW(int w) {
		this.w = w;
		calculateLengths();
	}

	/**
	 * hashes message and returns hash
	 */
	
	public byte[] hashMessage(byte[] key, byte[] value) {
		messageHash = digest.digest(ByteUtils.concatenate(key, value));
		return messageHash;
	}

	/**
	 * returns new calculated Bitstring bi
	 */
	
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
		byte[] merge = org.jcryptool.visual.merkletree.files.ByteUtils.concatenate(this.seed, keyAdrs);

		byte[] hash = sDigest.digest(org.jcryptool.visual.merkletree.files.ByteUtils.concatenate(merge, bCount));
		this.seedCount++;
		return hash;
	}
	
	public byte[] randomGenerator(byte[] seed, byte[] adrs, int len) {
		byte[] res = new byte[len+32];	//erstellen des zu befüllenden arrays
		byte[] padding = new byte[32];
		MessageDigest hash = null;
		try {
			hash = MessageDigest.getInstance("SHA-256");
		} catch(NoSuchAlgorithmException e) {
			//zuck: Der Algo existiert!
		}
		seed = ByteUtils.concatenate(padding, seed);
		seed = ByteUtils.concatenate(seed, adrs);
		res = hash.digest(seed);
		return res;
	}

	@Override
	public void sign() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] hashMessage(String message) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
