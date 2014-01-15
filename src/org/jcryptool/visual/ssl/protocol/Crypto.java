package org.jcryptool.visual.ssl.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;

import codec.Base64;
import codec.CorruptedCodeException;
import codec.Hex;

/**
 * This class implements all the needed crypto functions for the SSL/TLS
 * handshake.
 * <p>
 * It holds functions for key exchange calculation, hashes, keys and
 * certificates.
 * 
 * @author Denk Gandalf
 * 
 */
public class Crypto {

	/**
	 * The provider which is used for cipher suits, hashes, key exchanges and
	 * certificates.
	 */
	private final static String BOUNCY_CASTLE_PROVIDER = "BC";

	/**
	 * Generates a random value which is used for all key calculations. This
	 * speeds up the performance because it is calculated only once.
	 */
	private static SecureRandom secure = new SecureRandom();

	/**
	 * Generates a default certificate with the given key pair {@link pubKey}
	 * The certificate will be singed with the {@link sigKey} and uses the
	 * {@link strHash} with and the {@link strSignature} algorithm.
	 * 
	 * @param key
	 * @throws CertificateEncodingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	public X509Certificate generateX509(KeyPair pubKey, KeyPair sigKey,
			String strHash, String strSignature)
			throws CertificateEncodingException, InvalidKeyException,
			IllegalStateException, NoSuchProviderException,
			NoSuchAlgorithmException, SignatureException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date notBefor = new Date();
		Date notAfter = new Date();
		notAfter.setYear(notBefor.getYear() + 1);
		notAfter.setHours(23);
		notAfter.setMinutes(59);
		notAfter.setSeconds(59);

		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal certName = new X500Principal("CN=Test Server Certificate");
		certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		certGen.setIssuerDN(certName);
		certGen.setNotAfter(notAfter);
		certGen.setNotBefore(notBefor);
		certGen.setSubjectDN(certName);
		certGen.setPublicKey(pubKey.getPublic());
		certGen.setSignatureAlgorithm(strHash + "With" + strSignature);

		X509Certificate cert = certGen.generate(sigKey.getPrivate(), "BC");

		return cert;
	}

	/**
	 * Generates the hash of a given strMessage with the given strHash
	 * 
	 * @param strHash
	 *            The hash used
	 * @param strMessage
	 *            The message which is hashed
	 * @return returns the hash of the message
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public String generateHash(String strHash, String strMessage)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String hash = "";
		MessageDigest md = MessageDigest.getInstance(strHash);
		md.update(strMessage.getBytes("UTF-8"));
		byte[] digest = md.digest();
		hash = Hex.encode(digest);
		return hash;
	}

	/**
	 * Decrypts a given message using the GCM mode
	 * 
	 * @param Key
	 *            the key which is used for decryption
	 * @param message
	 *            the message which is decrypted
	 * @return the decrypted message
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchProviderException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws CorruptedCodeException
	 */
	public String decryptGCM(Key Key, String message)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IOException,
			InvalidAlgorithmParameterException, NoSuchProviderException,
			IllegalBlockSizeException, BadPaddingException,
			CorruptedCodeException {

		Cipher dCipher = Cipher.getInstance(Key.getAlgorithm()
				+ "/GCM/NoPadding", "BC");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(Key.getEncoded());

		dCipher.init(Cipher.DECRYPT_MODE, Key, ivParameterSpec);

		byte[] decode = Base64.decode(message);
		byte[] decValue = dCipher.doFinal(decode);

		return new String(decValue);
	}

	/**
	 * Encrypts a given message with the key using the GCm mode
	 * 
	 * @param Key
	 *            the key which is used for encryption
	 * @param message
	 *            the message which is encrypted
	 * @return the encrypted message
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchProviderException
	 * @throws InvalidAlgorithmParameterException
	 */
	public String encryptGCM(Key Key, String message)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException,
			InvalidAlgorithmParameterException {
		Cipher eCipher = Cipher.getInstance(Key.getAlgorithm()
				+ "/GCM/NoPadding", "BC");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(Key.getEncoded());

		eCipher.init(Cipher.ENCRYPT_MODE, Key, ivParameterSpec);

		String encrypt = Base64.encode(eCipher.doFinal(message.getBytes()));

		return encrypt;
	}

	/**
	 * Decrypts a given message with a key using the CBC mode.
	 * 
	 * @param Key
	 *            the key which is used for decryption
	 * @param message
	 *            the message which is decrypted
	 * @return the decrypted message
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 * @throws NoSuchProviderException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws CorruptedCodeException
	 */
	public String decryptCBC(Key Key, String message)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IOException,
			InvalidAlgorithmParameterException, NoSuchProviderException,
			IllegalBlockSizeException, BadPaddingException,
			CorruptedCodeException {

		Cipher dCipher = Cipher.getInstance(Key.getAlgorithm(), "BC");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(Key.getEncoded());

		if (Key.getAlgorithm().equals("RC4")
				|| Key.getAlgorithm().equals("RSA")) {
			dCipher.init(Cipher.DECRYPT_MODE, Key);
		} else {
			dCipher.init(Cipher.DECRYPT_MODE, Key, ivParameterSpec);
		}

		byte[] decode = Base64.decode(message);
		byte[] decValue = dCipher.doFinal(decode);

		return new String(decValue);
	}

	/**
	 * Encrypts a given message with a key using the CBC mode.
	 * 
	 * @param Key
	 *            Key which is used for encryption
	 * @param message
	 *            The message that is encrypted
	 * @return The encrypted message
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchProviderException
	 */
	public String encryptCBC(Key Key, String message)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IOException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException {
		Cipher eCipher = Cipher.getInstance(Key.getAlgorithm(), "BC");
		eCipher.init(Cipher.ENCRYPT_MODE, Key);

		String encrypt = Base64.encode(eCipher.doFinal(message.getBytes()));

		return encrypt;
	}

	/**
	 * Generates the Key for a cipher with the given algorithm {@link strKeyTyp}
	 * and the size {@link KeySize}.
	 * 
	 * @param strKeyTyp
	 *            Kind of key which is generated
	 * @param KeySize
	 *            size of the key
	 * @return a key for the given algorithm
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchProviderException
	 */
	public Key generateKey(String strKeyTyp, int KeySize)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException {
		KeyGenerator keyGen = KeyGenerator.getInstance(strKeyTyp, "BC");

		keyGen.init(KeySize, secure);
		Key genKey = keyGen.generateKey();
		return genKey;
	}

	/**
	 * Generates a key pair generator for the given algorithm {@link strKeyTyp}
	 * and with the size of {@link KeySize}.
	 * 
	 * @param strKeyTyp
	 *            The kind of algorithmn which is used
	 * @param KeySize
	 *            The size of the Key
	 * @return A key generator for keys
	 * @throws Exception
	 */
	public KeyPairGenerator generateGenerator(String strKeyTyp, int KeySize)
			throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
				strKeyTyp, BOUNCY_CASTLE_PROVIDER);

		if (strKeyTyp.contentEquals("DiffieHellman")) {
			keyPairGenerator = KeyPairGenerator.getInstance(strKeyTyp);
			keyPairGenerator.initialize(KeySize, secure);
		} else {
			keyPairGenerator.initialize(KeySize, secure);

		}

		return keyPairGenerator;
	}

	/**
	 * Generates a public and private key from a given KeyPairGenerator
	 * 
	 * @param kpG
	 *            the KeyPair Generator
	 * @return the KeyPair
	 */
	public KeyPair generateExchangeKey(KeyPairGenerator kpG) {
		KeyPair key = kpG.generateKeyPair();

		return key;
	}

}
