// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;

/*************************************************************************
 *  Compilation:  javac RSA.java
 *  Execution:    java RSA N
 *
 *  Generate an N-bit public and private RSA key and use to encrypt
 *  and decrypt a random message.
 *
 *  % java RSA 50
 *  public  = 65537
 *  private = 553699199426609
 *  modulus = 825641896390631
 *  message   = 48194775244950
 *  encrpyted = 321340212160104
 *  decrypted = 48194775244950
 *
 *  Known bugs (not addressed for simplicity)
 *  -----------------------------------------
 *  - It could be the case that the message >= modulus. To avoid, use
 *    a do-while loop to generate key until modulus happen to be exactly N bits.
 *
 *  - It's possible that gcd(phi, publicKey) != 1 in which case
 *    the key generation fails. This will only happen if phi is a
 *    multiple of 65537. To avoid, use a do-while loop to generate
 *    keys until the gcd is 1.
 *
 *************************************************************************/

import java.math.BigInteger;
import java.security.SecureRandom;


public class RSA {
   private final static BigInteger one      = new BigInteger("1");
   private final static SecureRandom random = new SecureRandom();

   private BigInteger d;
   private BigInteger e;
   private BigInteger modulus;

   // generate an N-bit (roughly) public and private key
   RSA(int N) {
      BigInteger p = BigInteger.probablePrime(N/2, random);
      BigInteger q = BigInteger.probablePrime(N/2, random);
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus    = p.multiply(q);
      e  = new BigInteger("65537");     // common value in practice = 2^16 + 1
      d = e.modInverse(phi);
   }


   BigInteger encrypt(BigInteger message) {
      return message.modPow(e, modulus);
   }

   BigInteger decrypt(BigInteger encrypted) {
      return encrypted.modPow(d, modulus);
   }

   public String toString() {
      String s = "";
      s += "public  = " + e  + "\n";
      s += "private = " + d + "\n";
      s += "modulus = " + modulus;
      return s;
   }

   public static void main(String[] args) {
      int N = Integer.parseInt(args[0]);
      RSA key = new RSA(N);
      System.out.println(key);

      // create random message, encrypt and decrypt
      BigInteger message = new BigInteger(N-1, random);

      //// create message by converting string to integer
      // String s = "test";
      // byte[] bytes = s.getBytes();
      // BigInteger message = new BigInteger(s);

      BigInteger encrypt = key.encrypt(message);
      BigInteger decrypt = key.decrypt(encrypt);
      System.out.println("message   = " + message);
      System.out.println("encrpyted = " + encrypt);
      System.out.println("decrypted = " + decrypt);
   }
}
