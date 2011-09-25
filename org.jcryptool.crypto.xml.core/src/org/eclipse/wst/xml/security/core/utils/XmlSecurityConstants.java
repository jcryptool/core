/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.utils;

import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;

/**
 * <p>Utility class to return the constant names for signature algorithms, message digest algorithms,
 * canonicalization and transformation algorithms, encryption algorithms and the key cipher
 * algorithm.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class XmlSecurityConstants {
    /**
     * Returns the signature algorithm.
     *
     * @param signatureAlgorithmString The selected signature algorithm
     * @return The standardized signature algorithm name
     */
    public static String getSignatureAlgorithm(final String signatureAlgorithmString) {
        if (signatureAlgorithmString.equalsIgnoreCase("DSA with SHA 1 (DSS)")) {
            return XMLSignature.ALGO_ID_SIGNATURE_DSA;
        } else if (signatureAlgorithmString.equalsIgnoreCase("Elliptic Curve DSA (ECDSA)")) {
            return XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA with MD5")) {
            return XMLSignature.ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA")) {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA with RIPEMD160")) {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_RIPEMD160;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA with SHA 1")) {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA with SHA 256")) {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA with SHA 384")) {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA384;
        } else if (signatureAlgorithmString.equalsIgnoreCase("RSA with SHA 512")) {
            return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512;
        }

        return null;
    }

    /**
     * Returns the message digest algorithm.
     *
     * @param messageDigestAlgorithmString The selected message digest algorithm
     * @return The standardized message digest algorithm name
     */
    public static String getMessageDigestAlgorithm(final String messageDigestAlgorithmString) {
        if (messageDigestAlgorithmString.equalsIgnoreCase("MD5")) {
            return MessageDigestAlgorithm.ALGO_ID_DIGEST_NOT_RECOMMENDED_MD5;
        } else if (messageDigestAlgorithmString.equalsIgnoreCase("RIPEMD 160")) {
            return MessageDigestAlgorithm.ALGO_ID_DIGEST_RIPEMD160;
        } else if (messageDigestAlgorithmString.equalsIgnoreCase("SHA 1")) {
            return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1;
        } else if (messageDigestAlgorithmString.equalsIgnoreCase("SHA 256")) {
            return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA256;
        } else if (messageDigestAlgorithmString.equalsIgnoreCase("SHA 384")) {
            return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA384;
        } else if (messageDigestAlgorithmString.equalsIgnoreCase("SHA 512")) {
            return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA512;
        }

        return null;
    }

    /**
     * Returns the canonicalization algorithm.
     *
     * @param canonicalizationAlgorithmString The selected canonicalization algorithm
     * @return The standardized canonicalization algorithm name
     */
    public static String getCanonicalizationAlgorithm(final String canonicalizationAlgorithmString) {
        if (canonicalizationAlgorithmString.equalsIgnoreCase("Exclusive without comments")) {
            return Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;
        } else if (canonicalizationAlgorithmString.equalsIgnoreCase("Exclusive with comments")) {
            return Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS;
        } else if (canonicalizationAlgorithmString.equalsIgnoreCase("Inclusive without comments")) {
            return Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS;
        } else if (canonicalizationAlgorithmString.equalsIgnoreCase("Inclusive with comments")) {
            return Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS;
        }

        return null;
    }

    /**
     * Returns the transformation algorithm.
     *
     * @param transformationAlgorithmString The selected transformation algorithm
     * @return The standardized transformation algorithm name
     */
    public static String getTransformationAlgorithm(final String transformationAlgorithmString) {
        if (transformationAlgorithmString.equalsIgnoreCase("Base64")) {
            return Transforms.TRANSFORM_BASE64_DECODE;
        } else if (transformationAlgorithmString.equalsIgnoreCase("Enveloped Signature")) {
            return Transforms.TRANSFORM_ENVELOPED_SIGNATURE;
        } else if (transformationAlgorithmString.equalsIgnoreCase("Exclusive without comments")) {
            return Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS;
        } else if (transformationAlgorithmString.equalsIgnoreCase("Exclusive with comments")) {
            return Transforms.TRANSFORM_C14N_EXCL_WITH_COMMENTS;
        } else if (transformationAlgorithmString.equalsIgnoreCase("Inclusive without comments")) {
            return Transforms.TRANSFORM_C14N_OMIT_COMMENTS;
        } else if (transformationAlgorithmString.equalsIgnoreCase("Inclusive with comments")) {
            return Transforms.TRANSFORM_C14N_WITH_COMMENTS;
        } else if (transformationAlgorithmString.equalsIgnoreCase("XPath")) {
            return Transforms.TRANSFORM_XPATH;
        } else if (transformationAlgorithmString.equalsIgnoreCase("XPath 2 Filter")) {
            return Transforms.TRANSFORM_XPATH2FILTER;
        } else if (transformationAlgorithmString.equalsIgnoreCase("XPath 2 Filter 04")) {
            return Transforms.TRANSFORM_XPATH2FILTER04;
        } else if (transformationAlgorithmString.equalsIgnoreCase("XPath Filter CHGP")) {
            return Transforms.TRANSFORM_XPATHFILTERCHGP;
        } else if (transformationAlgorithmString.equalsIgnoreCase("XPointer")) {
            return Transforms.TRANSFORM_XPOINTER;
        } else if (transformationAlgorithmString.equalsIgnoreCase("XSLT")) {
            return Transforms.TRANSFORM_XSLT;
        }

        return null;
    }

    /**
     * Returns the encryption algorithm.
     *
     * @param encryptionAlgorithmString The selected encryption algorithm
     * @return The standardized encryption algorithm name
     */
    public static String getEncryptionAlgorithm(final String encryptionAlgorithmString) {
        if (encryptionAlgorithmString.equalsIgnoreCase("AES 128")) {
            return XMLCipher.AES_128;
        } else if (encryptionAlgorithmString.equalsIgnoreCase("AES 192")) {
            return XMLCipher.AES_192;
        } else if (encryptionAlgorithmString.equalsIgnoreCase("AES 256")) {
            return XMLCipher.AES_256;
        } else if (encryptionAlgorithmString.equalsIgnoreCase("Triple DES")) {
            return XMLCipher.TRIPLEDES;
        }

        return null;
    }

    /**
     * Returns the key cipher algorithm.
     *
     * @param keyCipherAlgorithmString The selected key cipher algorithm
     * @return The standardized key cipher algorithm name
     */
    public static String getKeyCipherAlgorithm(final String keyCipherAlgorithmString) {
        if (keyCipherAlgorithmString.equalsIgnoreCase("AES-128 Key Wrap")) {
            return XMLCipher.AES_128_KeyWrap;
        } else if (keyCipherAlgorithmString.equalsIgnoreCase("AES-192 Key Wrap")) {
            return XMLCipher.AES_192_KeyWrap;
        } else if (keyCipherAlgorithmString.equalsIgnoreCase("AES-256 Key Wrap")) {
            return XMLCipher.AES_256_KeyWrap;
        } else if (keyCipherAlgorithmString.equalsIgnoreCase("Triple DES Key Wrap")) {
            return XMLCipher.TRIPLEDES_KeyWrap;
        } else if (keyCipherAlgorithmString.equalsIgnoreCase("RSA OAEP")) {
            return XMLCipher.RSA_OAEP;
        } else if (keyCipherAlgorithmString.equalsIgnoreCase("RSA 1.5")) {
            return XMLCipher.RSA_v1dot5;
        }

        return null;
    }

    /**
     * Returns the standard key file algorithm for the given algorithm.
     *
     * @param keyAlgorithm The algorithm to look for
     * @return The correct name for the algorithm
     */
    public static String getKeyAlgoritm(final String keyAlgorithm) {
        if (keyAlgorithm.equalsIgnoreCase("AES")) {
            return "AES";
        } else if (keyAlgorithm.equalsIgnoreCase("Blowfish")) {
            return "Blowfish";
        } else if (keyAlgorithm.equalsIgnoreCase("DES")) {
            return "DES";
        } else if (keyAlgorithm.equalsIgnoreCase("Triple DES")) {
            return "DESede";
        }

        return null;
    }
}
