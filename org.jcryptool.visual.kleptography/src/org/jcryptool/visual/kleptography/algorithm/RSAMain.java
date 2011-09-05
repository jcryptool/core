// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.kleptography.algorithm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.jcryptool.core.logging.utils.LogUtil;

/**
 * A straightforward RSAMain implementation using BigIntegers with a
 * padding scheme or other more complex tricks.
 * Features functions to initiate kleptographic attacks based on
 * the prime generation process.
 * The main source for the algorithms and techniques is Young and Yung's
 * thorough and unique "Malicious Cryptography".
 * @author Patrick Vacek
 */
public class RSAMain {

	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger phi;
	private BigInteger e;
	private BigInteger d;
	private String message;
	private String cipherHex;
	private String deciphered;
	private byte[] cipherBytes;
	private BigInteger id;
	private BigInteger index;
	private BigInteger seed;
	private BigInteger attackerN;
	private BigInteger attackerE;
	private BigInteger attackerD;
	private BigInteger nPrime;
	/**
	 * Set to true after the first time a fixed P is generated, so that the next
	 * calls to generate primes only generate a new P. If the bit count or key generation
	 * method is changed, reset this to false.
	 */
	private boolean initFixed;
	/** Set to true after the ID and index have been initialized; never reset thereafter. */
	private boolean initPRF;
	/** Set to true after the seed has been initialized; never reset thereafter. */
	private boolean initPRG;
	/** Set to true after the attacker's keys have been initialized; never reset thereafter. */
	private boolean initSETUP;
	private BigInteger encryptedP;

	private Kleptography klepto;

	public void setP(BigInteger p) {
		this.p = p;
	}
	public BigInteger getP() {
		return p;
	}
	public void setQ(BigInteger q) {
		this.q = q;
	}
	public BigInteger getQ() {
		return q;
	}
	public void setN(BigInteger n) {
		this.n = n;
	}
	public BigInteger getN() {
		return n;
	}
	public void setPhi(BigInteger phi) {
		this.phi = phi;
	}
	public BigInteger getPhi() {
		return phi;
	}
	public void setE(BigInteger e) {
		this.e = e;
	}
	public BigInteger getE() {
		return e;
	}
	public void setD(BigInteger d) {
		this.d = d;
	}
	public BigInteger getD() {
		return d;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setCipherHex(String ciphertext) {
		this.cipherHex = ciphertext;
	}
	public String getCipherHex() {
		return cipherHex;
	}
	public void setDeciphered(String deciphered) {
		this.deciphered = deciphered;
	}
	public String getDeciphered() {
		return deciphered;
	}
	public void setCipherBytes(byte[] cipherBytes) {
		this.cipherBytes = cipherBytes;
	}
	public byte[] getCipherBytes() {
		return cipherBytes;
	}
	public void setID(BigInteger id) {
		this.id = id;
	}
	public BigInteger getID() {
		return id;
	}
	/** Reset the index to zero. */
	public final void resetIndex() {
		index = BigInteger.ZERO;
	}
	/** Increments the index. */
	public final void incrementIndex() {
		index = index.add(BigInteger.ONE);
	}
	public BigInteger getIndex() {
		return index;
	}
	public void setSeed(BigInteger seed) {
		this.seed = seed;
	}
	public BigInteger getSeed() {
		return seed;
	}
	public void setAttackerN(BigInteger attackerN) {
		this.attackerN = attackerN;
	}
	public BigInteger getAttackerN() {
		return attackerN;
	}
	public void setAttackerE(BigInteger attackerE) {
		this.attackerE = attackerE;
	}
	public BigInteger getAttackerE() {
		return attackerE;
	}
	public void setAttackerD(BigInteger attackerD) {
		this.attackerD = attackerD;
	}
	public BigInteger getAttackerD() {
		return attackerD;
	}
	public void setInitFixed(boolean initFixed) {
		this.initFixed = initFixed;
	}
	public boolean getInitFixed() {
		return initFixed;
	}
	public void setInitPRF(boolean initPRF) {
		this.initPRF = initPRF;
	}
	public boolean getInitPRF() {
		return initPRF;
	}
	public void setInitPRG(boolean initARG) {
		this.initPRG = initARG;
	}
	public boolean getInitPRG() {
		return initPRG;
	}
	public void setInitSETUP(boolean initSETUP) {
		this.initSETUP = initSETUP;
	}
	public boolean getInitSETUP() {
		return initSETUP;
	}
	public void setEncryptedP(BigInteger encryptedP) {
		this.encryptedP = encryptedP;
	}
	public BigInteger getEncryptedP() {
		return encryptedP;
	}
	public void setNPrime(BigInteger nPrime) {
		this.nPrime = nPrime;
	}
	public BigInteger getNPrime() {
		return nPrime;
	}

	/**
	 * Constructor: initialize all BigInts to zero and init flags to false.
	 * @param klepto A reference to the kleptography driver class.
	 */
	public RSAMain(Kleptography klepto) {
		this.klepto = klepto;
		setP(BigInteger.ZERO);
		setQ(BigInteger.ZERO);
		setN(BigInteger.ZERO);
		setPhi(BigInteger.ZERO);
		setE(BigInteger.ZERO);
		setD(BigInteger.ZERO);
		setID(BigInteger.ZERO);
		resetIndex();
		setSeed(BigInteger.ZERO);
		setInitFixed(false);
		setInitPRF(false);
		setInitPRG(false);
		setInitSETUP(false);
		setAttackerN(BigInteger.ZERO);
		setAttackerE(BigInteger.ZERO);
		setAttackerD(BigInteger.ZERO);
		setEncryptedP(BigInteger.ZERO);
	}

	/**
	 * Generates honest primes P and Q, both of which must be
	 * random numbers of length equal to half of the key length.
	 */
	public void genHonestPrimes() {
		// Keep trying to generate primes until we get a good pair.
		while(true) {
			// First generate P, which should be a prime number of the desired length.
			genHonestP();
			// Then generate Q according to the same rules.
			genHonestQ();
			// If P = Q or the length in bits of P * Q is less than the
			// desired bit length - 1, try again.
			// Otherwise, we have our numbers!
			if(!getP().equals(getQ()) && getP().multiply(getQ()).compareTo(
					(new BigInteger("2").pow(klepto.functions.getBitCount() - 1))) >= 0) { //$NON-NLS-1$
				setInitFixed(true);
				break;
			}
		}
	}

	/**
	 * Generated an honest prime P with the set bit length / 2.
	 */
	private void genHonestP() {
		setP(BigInteger.probablePrime(klepto.functions.getBitCount() / 2, klepto.functions.getRandom()));
	}

	/**
	 * Generates an honest prime Q with the set bit length / 2.
	 */
	private void genHonestQ() {
		setQ(BigInteger.probablePrime(klepto.functions.getBitCount() / 2, klepto.functions.getRandom()));
	}

	/**
	 * Verifies that a given number actually is prime.
	 * @param number The number to check.
	 * @return True: is prime; false: is not.
	 */
	public boolean verifyPrime(BigInteger number) {
		return number.isProbablePrime(100);
	}

	/**
	 * Verifies that a given number is long enough (bit length / 2).
	 * @param num The number to check.
	 * @return True: is the right length; false: is not.
	 */
	public boolean verifyHalfBitLength(BigInteger num) {
		return num.bitLength() == klepto.functions.getBitCount() / 2;
	}

	/**
	 * Verifies that two numbers are not equal.
	 * @param num1 The first number.
	 * @param num2 The second number.
	 * @return True: the numbers are not equal; false: they are.
	 */
	public boolean verifyNotEqual(BigInteger num1, BigInteger num2) {
		return !num1.equals(num2);
	}

	/**
	 * The first time through generates honest primes P and Q, and
	 * each call thereafter only generates a new (honest) prime Q,
	 * meaning P remains fixed.
	 */
	public void genFixedPRandomQ() {
		if(!getInitFixed()) {
			genHonestPrimes();
			setInitFixed(true);
		}
		else {
			// Keep trying to generate a prime Q until we get a good pair.
			while(true) {
				// Then generate Q according to the same rules.
				genHonestQ();
				// If P = Q or the length in bits of P * Q is less than the
				// desired bit length - 1, try again. Otherwise, we have our numbers!
				if(!getP().equals(getQ()) &&
						getP().multiply(getQ()).doubleValue() >= Math.pow(2, klepto.functions.getBitCount() - 1)) {
					break;
				}
			}
		}
	}

	/**
	 * Generates a new 160-bit ID and resets the index to zero.
	 */
	public void genNewID() {
		setID(new BigInteger(160, klepto.functions.getRandom()));
		resetIndex();
	}

	/**
	 * Generates a 48-bit long integer by using ID and index as input and
	 * hashing that with MD5 into a byte array. The least-significant byte
	 * gets saved, the index gets incremented, and the inputs get rehashed.
	 * Six hashes providing one byte each = 6 bytes = 48 bits.
	 * This is the length of a seed for the Java random function.
	 * @return 48-bit pseudo-random long int.
	 */
	public long pseudoRandomFunction() {
		byte[] byteSeed = new byte[6];
		// Run six iterations to get one byte each time.
		for(int j = 0; j < 6; j++) {
			// Combine the inputs into one byte array.
			byte[] idBytes = getID().toByteArray();
			byte[] indexBytes = getIndex().toByteArray();
			byte[] inputBytes = new byte[idBytes.length + indexBytes.length];
			System.arraycopy(idBytes, 0, inputBytes, 0, idBytes.length);
			System.arraycopy(indexBytes, 0, inputBytes, idBytes.length, indexBytes.length);
			// Must be surrounded in a try-catch.
			try {
				// Hash the inputs via MD5 into a new byte array.
				MessageDigest algorithm = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
				algorithm.reset();
				algorithm.update(inputBytes);
				byte[] messageDigest = algorithm.digest();
				// Save the least-significant byte.
				byteSeed[j] = messageDigest[messageDigest.length - 1];
			} catch (NoSuchAlgorithmException e) {
				// This should never happen, unless MD5 magically were to disappear...
	            LogUtil.logError(e);
			}
			incrementIndex();
		}
		// Convert the six-byte array into a long int.
		long longSeed = 0;
		for (int j = 0; j < byteSeed.length; j++)
		{
			longSeed = (longSeed << 8) + (byteSeed[j] & 0xff);
		}
		return longSeed;
	}

	/**
	 * Generates a prime P by means of a pseudo-random function.
	 * The function takes the ID and index as input and spits out
	 * a 48-bit seed to be used here for generating a "random" prime p.
	 * Q is then generated normally (honestly).
	 */
	public void genPseudoRandomFunctionP() {
		// Generate P via pseudo-random function.
		setP(BigInteger.probablePrime(klepto.functions.getBitCount() / 2, new Random(pseudoRandomFunction())));
		// Keep trying to generate a prime Q until we get a good pair.
		// If a generated pair isn't good, it's not worth starting over
		// with a new P since it takes far longer to generate a P than a Q.
		while(true) {
			// Then generate Q according to the same rules.
			genHonestQ();
			// If P = Q or the length in bits of P * Q is less than the
			// desired bit length - 1, try again.
			// Otherwise, we have our numbers!
			if(!getP().equals(getQ()) &&
					getP().multiply(getQ()).doubleValue() >= Math.pow(2, klepto.functions.getBitCount() - 1)) {
				break;
			}
		}
	}

	/**
	 * Generates a new seed for the pseudo-random generator.
	 * 48 bits is the longest size allowed with the Random() function.
	 */
	public void genNewSeed() {
		setSeed(new BigInteger(48, klepto.functions.getRandom()));
	}

	/**
	 * A pesudo-random generator based on Blum-Blum-Shub.
	 * The idea is to use the current 48-bit seed to generate a new 51-bit pseudo-random number,
	 * from which the first three bits are stored and the other 48 are saved as a new seed.
	 * After running this for sixteen iterations, we will get 48 apparently random bits,
	 * which can then be used elsewhere for generating P.
	 * Blum-Blum-Shub uses the formula (x ^ (2 ^ i) mod n) mod 2 for i = 0 to 50, and in each
	 * iteration the most-significant bit is stored and used to build the next seed and the
	 * apparently random bitstream. N is the product of two large (256-bit in this implementation)
	 * random primes (derived from the current seed) that both must be congruent to 3 mod 4.
	 * Note that three bits are used from each iteration to build the final bitstream -
	 * the original paper describes just taking one, but later research suggestions additional
	 * bits may be considered apparently random. Young and Yung suggest log(log(|n|)) bits but
	 * then claim that for |n| = 768 that 9 bits can be used, although log(log(768) rounds down
	 * to 3. (log(768) rounds down to 9, though.) Menezes, van Oorschot, and Vanstone in the
	 * Handbook of Applied Cryptography recommend c * log(log(|n|)) but do not provide c.
	 * I took the safe route of just using log(log(|n|)), which, for a 512-bit n, is about 3.
	 * @return A pseudo-random bit stream to be used as a seed.
	 */
	public long pseudoRandomGenerator() {
		// We will build the apparently random bitstream in this BigInteger.
		BigInteger bigIntSeed = new BigInteger("0"); //$NON-NLS-1$
		// Run sixteen iterations to get three bits each time.
		for(int i = 0; i < 16; i++) {
			// Get a new source of random based off of the current seed.
			Random rand = new Random(getSeed().longValue());
			// Generate two large random primes based off of the current seed.
			// I determined 256 bits to be large enough.
			// Both must be congruent to 3 mod 4 and must not equal each other.
			BigInteger tempP;
			BigInteger tempQ;
			while(true) {
				tempP = BigInteger.probablePrime(256, rand);
				if(tempP.mod((new BigInteger("4"))).equals((new BigInteger("3")))) { //$NON-NLS-1$ //$NON-NLS-2$
					break;
				}
			}
			while(true) {
				tempQ = BigInteger.probablePrime(256, rand);
				if(tempQ.mod((new BigInteger("4"))).equals((new BigInteger("3"))) && !tempP.equals(tempQ)) { //$NON-NLS-1$ //$NON-NLS-2$
					break;
				}
			}
			// The primes exist only to generate N, the product of P and Q.
			BigInteger tempN = tempP.multiply(tempQ);
			StringBuilder tempBitStream = new StringBuilder();
			// This loop is the core of the algorithm; this is where we iteratively
			// get single bits to save to a temporary bit stream.
			for(int j = 0; j < 51; j++) {
				BigInteger tempResult = getSeed().modPow((new BigInteger("2")).pow(j), tempN). //$NON-NLS-1$
				mod(BigInteger.ONE.add(BigInteger.ONE));
				if(tempResult.testBit(0)) {
					tempBitStream.append("1"); //$NON-NLS-1$
				}
				else
					tempBitStream.append("0"); //$NON-NLS-1$
			}
			// First convert the bit stream into a BigInteger.
			BigInteger tempBigIntStream = new BigInteger(tempBitStream.substring(0, 3), 2);
			// Take the three most significant bits of the temporary bit stream
			// and add them to the random bitstream / seed.
			bigIntSeed = bigIntSeed.shiftLeft(3).or(tempBigIntStream);
			// Take the other 48 bits and save them as the next "master" seed.
			BigInteger tempMasterSeed = new BigInteger(tempBitStream.substring(3), 2);
			setSeed(tempMasterSeed);
		}
		// Convert the 48-bit BigInt into a long to be used for a seed to generate a P.
		long longSeed = 0;
		longSeed = bigIntSeed.longValue();
		return longSeed;
	}

	/**
	 * Generates a prime P by means of an pseudo-random generator.
	 * Using a given seed, a P will be generated that will be indistinguishable
	 * from an actual random number. The seed used to generate it will be overwritten
	 * by a new seed. Q is then generated normally (honestly).
	 */
	public void genPseudoRandomGeneratorP() {
		// Generate P via pseudo-random generator.
		setP(BigInteger.probablePrime(klepto.functions.getBitCount() / 2, new Random(pseudoRandomGenerator())));
		// Keep trying to generate a prime Q until we get a good pair.
		// If a generated pair isn't good, it's not worth starting over
		// with a new P since it takes far longer to generate a P than a Q.
		while(true) {
			// Then generate Q according to the same rules.
			genHonestQ();
			// If P = Q or the length in bits of P * Q is less than the
			// desired bit length - 1, try again.
			// Otherwise, we have our numbers!
			if(!getP().equals(getQ()) &&
					getP().multiply(getQ()).doubleValue() >= Math.pow(2, klepto.functions.getBitCount() - 1)) {
				break;
			}
		}
	}

	/**
	 * Generates public and private keys for the attacker.
	 * The length of the keys is half of whatever the current normal length is so
	 * that the result is of the same length as the cryptosystem's prime P.
	 * Note that the generated pairs are biased to be only in the higher half of their
	 * bit-space; if N is too small, finding a suitable P becomes effectively impossible.
	 * This is somewhat less than ideal and in a real implementation probably would not fly.
	 */
	public void genAttackerKeys() {
		// First halve the bit count.
		klepto.functions.setBitCount(klepto.functions.getBitCount() / 2);
		// Keep trying to generate primes until we get a good pair.
		while(true) {
			// First generate P, which should be a prime number of the desired length.
			genHonestP();
			// Then generate Q according to the same rules.
			genHonestQ();
			// Normally we would just check that P != Q and the length in bits of P * Q
			// is more than the desired bit length - 1, but we will only accept a composite N
			// in the upper half of possible values to prevent problems later on.
			if(!getP().equals(getQ()) && getP().multiply(getQ()).compareTo(
					new BigInteger("2").pow(klepto.functions.getBitCount() - 1). //$NON-NLS-1$
					add(new BigInteger("2").pow(klepto.functions.getBitCount() - 2))) >= 0) { //$NON-NLS-1$
				break;
			}
		}
		// Generate/calculate all the important values.
		calculateN();
		calculatePhi();
		generateRandomE();
		calculateD();
		// Copy N, E, and D to extra variables, since we need the main ones for
		// use in the actual cryptosystem!
		setAttackerN(getN());
		setAttackerE(getE());
		setAttackerD(getD());
		// Clear all the BigInts used for the main cryptosystem.
		// This is important: otherwise, if the user generates the attacker's keys and then
		// changes the radix display, these values will be displayed!
		setP(BigInteger.ZERO);
		setQ(BigInteger.ZERO);
		setN(BigInteger.ZERO);
		setPhi(BigInteger.ZERO);
		setE(BigInteger.ZERO);
		setD(BigInteger.ZERO);
		// Return the bit count back to normal.
		klepto.functions.setBitCount(klepto.functions.getBitCount() * 2);
	}

	/**
	 * Generates primes according to a basic SETUP attack.
	 * This function can take a while to run, because the chances of finding an acceptable
	 * solution are rather small. I've considered adding a limit to the attempts so that
	 * the program doesn't lock up. The point here is that P is generated honestly but then
	 * encrypted with the attacker's public key and copied to the upper bits of the composite N.
	 * (The lower bits are created randomly.) Then Q is solved for by division, and if the proper
	 * conditions aren't met (such as Q not being prime, which is the most common problem), then
	 * we must try again.
	 */
	public void genSetupPrimes() {
		while(true) {
//			if(i > 1000) {
//				genHonestPrimes();
//				break;
//			}
			// Start with an honest P.
			genHonestP();
			// P must be less than the attacker's N, otherwise the encrypted P won't decrypt
			// to the same number. N should be large enough that this isn't too big of a problem,
			// but this is a significant drawback of this implementation.
			if(getP().compareTo(getAttackerN()) >= 0) {
				continue;
			}
			// Encrypt P and then unbias it, since the encrypted P must be less than
			// the attacker's N instead of the whole range of X-bit primes.
			setEncryptedP(getP().modPow(getAttackerE(), getAttackerN()));
			setEncryptedP(probBiasRemoval(getAttackerN(),
					(new BigInteger("2")).pow(klepto.functions.getBitCount() / 2), getEncryptedP())); //$NON-NLS-1$
			// If the unbiasing returned a failure value (-1), start over.
			if(getEncryptedP().compareTo(BigInteger.ZERO) < 0) {
				continue;
			}
			// Create N' by concatenating the encrypted P to a random value.
			BigInteger rand = new BigInteger(klepto.functions.getBitCount() / 2, klepto.functions.getRandom());
			BigInteger tempN = getEncryptedP().shiftLeft(klepto.functions.getBitCount() / 2).or(rand);
			// Solve for Q. Q must be long enough, prime, not equal to P, and must form a
			// composite N when multiplied with P that is also long enough.
			// The chances of Q being prime are relatively small, which is the main problem
			// with this whole concept, as in, why it takes so long to run.
			setQ(tempN.divide(getP()));
			if(getQ().bitLength() != klepto.functions.getBitCount() / 2 ||
					!getQ().isProbablePrime(100) ||
					getP().multiply(getQ()).bitLength() < klepto.functions.getBitCount() ||
					getP().equals(getQ())) {
				continue;
			}
			// Success! Save N' and exit the infinite for loop!
			setNPrime(tempN);
			break;
		}
	}

	/**
	 * Removes the probabilistic bias inherent in creating an ecrypted P less than N
	 * instead of across the whole range of X-bit values. It is given that S > R > S/2 and
	 * X < R instead of X < S like it should be. Thus, for X < S - R, half of the time, set
	 * X = S - 1 - X. For X >= S - R, half the time, give a failure result. This should change
	 * the distribution of X to be more balanced, although the failure possibility is
	 * disadvantageous.
	 * @param r The value of the attacker's N value; the maximum value + 1 for possible values of X.
	 * @param s The value of 2^(bitCount / 2); the ideal maximum possible value of X.
	 * @param x The input value whose bias must be corrected; an encrypted prime P in our case.
	 * @return The corrected version of X or a failure/error value of -1.
	 */
	public BigInteger probBiasRemoval(BigInteger r, BigInteger s, BigInteger x) {
		BigInteger temp = BigInteger.ONE.negate();
		boolean b = (new BigInteger(1, klepto.functions.getRandom())).testBit(0);
		if(x.compareTo(s.subtract(r)) < 0 && b) { // X < S - R and b = true
			temp = x;
		}
		else if(x.compareTo(s.subtract(r)) < 0 && !b) { // X < S - R and b = false
			temp = s.subtract(BigInteger.ONE).subtract(x);
		}
		else if(x.compareTo(s.subtract(r)) >= 0 && b) { // X >= S - R and b = true
			temp = x;
		}
		// else (if) X >= S - R and b = false: failure, return -1.
		return temp;
	}


	/**
	 * Calculates N = P * Q
	 */
	public void calculateN() {
		setN(klepto.functions.calculateN(getP(), getQ()));
	}

	/**
	 * Calculates Phi, Euler's totient function of P * Q.
	 * Since P and Q are primes, Phi = (P - 1) * (Q - 1).
	 */
	public void calculatePhi() {
		setPhi(klepto.functions.calculatePhi(getP(), getQ()));
	}

	/**
	 * Generates a random E, where 1 < E < Phi and gcd(E, Phi) = 1
	 */
	public void generateRandomE() {
		setE(klepto.functions.generateE(getPhi()));
	}

	/**
	 * Sets a default value for E. The standard value is 2^16+1 = 65537x10 = 10001x16.
	 * However, if the bit length is over 16, this value will be greater than Phi, which is less
	 * than ideal. In such a case, use 2^4+1 = 17x10 = 11x16 instead.
	 */
	public void setDefaultE() {
		if(klepto.functions.getBitCount() > 16)
			setE(new BigInteger("65537"));
		else
			setE(new BigInteger("17"));
	}

	/**
	 * Verifies that a given number is greater than or equal to 1 and less than Phi.
	 * @param e The number to check.
	 * @return True: the number is within the desired range; false: it is not.
	 */
	public boolean verifyESize(BigInteger e) {
		return e.compareTo(BigInteger.ONE) >= 1 && e.compareTo(phi) <= -1;
	}

	/**
	 * Verifies that a given number is relatively prime to Phi.
	 * @param e The number to check.
	 * @return True: they are relatively prime; false: they are not.
	 */
	public boolean verifyERelativePrime(BigInteger e) {
		return e.gcd(phi).equals(BigInteger.ONE);
	}

	/**
	 * Calculates D by means of solving for the modular inverse of E mod Phi.
	 * D * E should be congruent to 1 mod Phi.
	 */
	public void calculateD() {
		setD(klepto.functions.calculateD(getE(), getPhi()));
	}

	/**
	 * Encrypts the plaintext message (set via setMessage()) with the specified
	 * bit length. The ciphertext is accessible in a hex string from getCipherHex()
	 * and in a byte array from getCipherBytes().
	 */
	public void encrypt() {
		setCipherBytes(klepto.functions.encrypt(getMessage(), getE(), getN()));
		// The loop will take care of the master byte array on its own, but for
		// the sake of representing the ciphertext in a somewhat human-understandable
		// fashion, we will convert the bytes to hex.
		// I previously attempted to decode the bytes back to into a character array,
		// but the presence of unprintable characters in every existing character set
		// cause too many problems - and not because they can't be printed, but because
		// Java converts them all automatically to something else, screwing up the cipher.
		setCipherHex(RSAFunctions.convertBytesToHex(getCipherBytes()));
	}

	/**
	 * Decrypts the encrypted ciphertext, which can be read via getDecrypted().
	 * Unless something went very wrong, it should be the same as the original message.
	 */
	public void decrypt() {
		setDeciphered(klepto.functions.decrypt(getCipherBytes(), getD(), getN()));
	}
} // end class RSAMain