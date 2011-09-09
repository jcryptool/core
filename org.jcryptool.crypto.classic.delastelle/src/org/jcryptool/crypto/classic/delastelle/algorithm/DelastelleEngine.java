// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.delastelle.algorithm;

import org.jcryptool.core.operations.algorithm.classic.IClassicAlgorithmEngine;
import org.jcryptool.core.operations.alphabets.AlphaConverter;

/**
 * The CaesarEngine class represents the engine of the Caesar algorithm class.
 *
 * @see org.jcryptool.DelastelleAlgorithm.caesar.algorithm.CaesarAlgorithm It
 *      provides Caesar-based encryption and decryption on integer basis.
 *
 * @author simlei
 * @version 0.1
 */
public class DelastelleEngine implements IClassicAlgorithmEngine {

    private int[][] readKey(int[][] mySquare) {
        int[][] myList = new int[91][2];
        for (int i = 65; i < 91; i++) {
            myList[i][0] = -1;
            myList[i][1] = -1;
        }

        // Aus dem KeySquare die KeyList bestimmen
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                myList[mySquare[i][j]][0] = i;
                myList[mySquare[i][j]][1] = j;
            }
        }
        return myList;
    }

    private int[][] getKeySquare(String key) {
        int[][] mySquare = new int[5][5];
        String key2 = key.toUpperCase();
        int i = 0;
        while (i < key2.length()) {
            int charval = (int) key2.charAt(i);
            if (charval < 65 || charval > 90)
                key2 = key2.substring(0, i - 1).concat(key2.substring(i + 1));
            else
                i++;
        }
        i = 0;
        while (i < key2.length()) {
            boolean unique = true;
            for (int k = 0; k < i; k++)
                if (key2.charAt(k) == key2.charAt(i))
                    unique = false;
            if (!unique)
                key2 = key2.substring(0, i).concat(key2.substring(i + 1));
            else
                i++;
        }

        String alphaJ = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

        for (i = 0; i < alphaJ.length(); i++) {
            boolean included = false;
            for (int k = 0; k < key2.length(); k++)
                if (key2.charAt(k) == alphaJ.charAt(i))
                    included = true;
            if (!included)
                key2 = key2.concat(String.valueOf(alphaJ.charAt(i)));
        }

        int count = 0;
        for (i = 0; i < 5; i++) {
            for (int k = 0; k < 5; k++) {
                mySquare[k][i] = key2.charAt(count);
                count++;
            }
        }

        return mySquare;
    }

    /**
     * Decryption
     *
     * @param input data to be decrypted.
     * @param key which the decryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the decrypted data as an int array.
     */
    public int[] doDecryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
            int pastChars) {

        char bigrammalt1, bigrammalt2;
        String bigrammneu;
        char zufallsbuchstabe;
        int x_1, y_1, x_2, y_2;
        StringBuilder keyString = new StringBuilder();
        StringBuilder rohtext = new StringBuilder();

        for (int i = 0; i < key.length; i++)
            keyString.append(alphaChars[key[i]]);
        for (int i = 0; i < input.length; i++)
            rohtext.append(alphaChars[input[i]]);
        int[][] keySquare = getKeySquare(keyString.toString());
        int[][] keyList = readKey(keySquare);

        // durch zwei teilbare Textlänge erzielen
        if (rohtext.length() % 2 == 1) {

            zufallsbuchstabe = rohtext.charAt(rohtext.length() - 1);
            while (zufallsbuchstabe == rohtext.charAt(rohtext.length() - 1)) {
                int randomIndex = (int) ((double) Math.floor(Math.random() * (double) rohtext.length()));
                zufallsbuchstabe = rohtext.charAt(randomIndex);
            }
            rohtext.append(zufallsbuchstabe);
        }

        // der eigentliche Ver-/Entschlüsselungsschritt
        StringBuilder chiffre = new StringBuilder();
        for (int i = 0; i < rohtext.length(); i++)
            if (i % 2 == 1) {
                // Bigramm herausnehmen und Koordinaten einlesem
                bigrammalt1 = rohtext.charAt(i - 1);
                bigrammalt2 = rohtext.charAt(i);
                x_1 = keyList[(int) (bigrammalt1)][0];
                y_1 = keyList[(int) (bigrammalt1)][1];
                x_2 = keyList[(int) (bigrammalt2)][0];
                y_2 = keyList[(int) (bigrammalt2)][1];
                // Bigramm durch ver-/entschlüsseltes Bigramm mittels
                // Umordnung der Koordinaten ersetzen
                bigrammneu = String.valueOf((char) (byte) keySquare[y_2][y_1]).concat(
                        String.valueOf((char) (byte) keySquare[x_2][x_1]));
                // Zum Chiffretext hinzufügen
                chiffre.append(bigrammneu);
            }

        int[] ciphertext = new int[chiffre.length()];
        for (int i = 0; i < chiffre.length(); i++) {
            for (int k = 0; k < alphaChars.length; k++)
                if (chiffre.charAt(i) == alphaChars[k]) {
                    ciphertext[i] = k;
                }
        }

        return ciphertext;
    }

    /**
     * Encryption
     *
     * @param input data to be encrypted.
     * @param key which the encryption uses.
     * @param alphaLength the length of the currentAlphabet.
     * @return the encrypted data as an int array.
     */
    public int[] doEncryption(int[] input, int[] key, int alphaLength, int[] alphabet, char nullchar,
            char[] alphaChars, char[] keyChars, char[] inputNoNonAlphaChar, AlphaConverter alphaConv, char[] key2,
            int pastChars) {
        char bigrammalt1, bigrammalt2;
        String bigrammneu;
        char zufallsbuchstabe;
        int x_1, y_1, x_2, y_2;
        StringBuilder keyString = new StringBuilder();
        StringBuilder rohtext = new StringBuilder();

        for (int i = 0; i < key.length; i++)
            keyString.append(alphaChars[key[i]]);
        for (int i = 0; i < input.length; i++)
            rohtext.append(alphaChars[input[i]]);
        int[][] keySquare = getKeySquare(keyString.toString());
        int[][] keyList = readKey(keySquare);

        // durch zwei teilbare Textlänge erzielen
        if (rohtext.length() % 2 == 1) {

            zufallsbuchstabe = rohtext.charAt(rohtext.length() - 1);
            while (zufallsbuchstabe == rohtext.charAt(rohtext.length() - 1)) {
                int randomIndex = (int) ((double) Math.floor(Math.random() * (double) rohtext.length()));
                zufallsbuchstabe = rohtext.charAt(randomIndex);
            }
            rohtext.append(zufallsbuchstabe);
        }

        // der eigentliche Ver-/Entschlüsselungsschritt
        StringBuilder chiffre = new StringBuilder();
        for (int i = 0; i < rohtext.length(); i++)
            if (i % 2 == 1) {
                // Bigramm herausnehmen und Koordinaten einlesem
                bigrammalt1 = rohtext.charAt(i - 1);
                bigrammalt2 = rohtext.charAt(i);
                x_1 = keyList[(int) (bigrammalt1)][0];
                y_1 = keyList[(int) (bigrammalt1)][1];
                x_2 = keyList[(int) (bigrammalt2)][0];
                y_2 = keyList[(int) (bigrammalt2)][1];
                // Bigramm durch ver-/entschlüsseltes Bigramm mittels
                // Umordnung der Koordinaten ersetzen
                bigrammneu = String.valueOf((char) (byte) keySquare[y_2][y_1]).concat(
                        String.valueOf((char) (byte) keySquare[x_2][x_1]));
                // Zum Chiffretext hinzufügen
                chiffre.append(bigrammneu);
            }

        int[] ciphertext = new int[chiffre.length()];
        for (int i = 0; i < chiffre.length(); i++) {
            for (int k = 0; k < alphaChars.length; k++)
                if (chiffre.charAt(i) == alphaChars[k]) {
                    ciphertext[i] = k;
                }
        }

        return ciphertext;
    }

}
