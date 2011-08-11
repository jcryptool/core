package org.jcryptool.crypto.classic.grille.algorithm;


/**
 * Ver- und entschlüsselt einen Text mit der Grille-Verschlüsselung
 * 
 * @author Patricia
 * 
 */
public class Grille {
    private KeySchablone key;

    /**
     * prüft ob der zu verschlüsselnde Text in die Schablone passt
     * 
     * @param key der verwendete Schlüssel
     * @param plaintext der zu verschlüsselnde Text
     * @return den Plaintext an die Schablonenlänge angepasst
     */
    public String check(String plaintext) {
        int plaintextLength = plaintext.length();
        int blockSize = key.getSize() * key.getSize() - key.getSize() % 2;
        int blockCount = (int) Math.ceil((double) plaintext.length() / blockSize);

        if (plaintext.length() < blockCount * blockSize) {
            for (int i = 0; i < blockCount * blockSize - plaintextLength; i++) {
                plaintext += generateRandomChar(plaintext);
            }
        }
        return plaintext;
    }

    public char generateRandomChar(String plaintext) {
        return plaintext.charAt((int) Math.floor(Math.random() * plaintext.length()));
    }

    /**
     * Verschlüsselt den Plaintext.
     * 
     * @param key der verwendete Schlüssel
     * @param plaintext der zu verschlüsselnde Text
     * @return der verschlüsselte Text
     */
    public String encrypt(String plaintext) {
        KeySchablone encKey = key;
        String cipher = "";
        for (int i = 0; i < plaintext.length(); i += encKey.getSize() * encKey.getSize() - encKey.getSize() % 2) {
            cipher += encryptSingleBlock(plaintext, i);
        }
        encKey.rotateClockwise();

        return cipher;
    }

    public String encryptSingleBlock(String plaintext, int plaintextBlockPosition) {
        KeySchablone encKey = key;
        Schablone crypt = new Schablone(encKey.getSize());
        encKey.rotateCounterClockwise();
        for (int rotation = 0; rotation < 4; rotation++) {
            plaintextBlockPosition = encryptAndTurn(plaintext, plaintextBlockPosition, crypt);
        }
        if (encKey.getSize() % 2 == 1)
            crypt.set(encKey.getSize() / 2, encKey.getSize() / 2, generateRandomChar(plaintext));
        String ciphertext = "";
        for (int r = 0; r < crypt.getSize(); r++) {
            for (int c = 0; c < crypt.getSize(); c++) {
                ciphertext = ciphertext + crypt.get(r, c);
            }
        }

        return ciphertext;
    }

    public int encryptAndTurn(String plaintext, int plaintextBlockPosition, Schablone crypt) {
        KeySchablone encKey = key;
        encKey.rotateClockwise();
        for (int r = 0; r < encKey.getSize(); r++) {
            for (int c = 0; c < encKey.getSize(); c++) {
                if (encKey.get(r, c) == '1') {
                    crypt.set(r, c, plaintext.charAt(plaintextBlockPosition++));
                }
            }
        }

        return plaintextBlockPosition;
    }

    /**
     * Entschlüsselt den Ciphertext
     * 
     * @param key verwendeter Schlüssel
     * @param ciphertext zu entschlüsselnder Text
     * @return der entschlüsselte Text
     */
    public String decrypt(String ciphertext) {
        KeySchablone decKey = key;
        String plain = "";
        for (int i = 0; i < ciphertext.length(); i += decKey.getSize() * decKey.getSize() - decKey.getSize() % 2) {

            plain += decryptSingleBlock(ciphertext, i);

        }

        return plain;
    }

    public String decryptSingleBlock(String ciphertext, int ciphertextBlockPosition) {
        KeySchablone decKey = key;
        String plaintext = "";
        Schablone decrypt = new Schablone(decKey.getSize());
        for (int i = 0; i < decrypt.getSize() * decrypt.getSize(); i++) {
            decrypt.set(i / decrypt.getSize(), i % decrypt.getSize(), ciphertext.charAt(ciphertextBlockPosition + i));
        }
        decKey.rotateCounterClockwise();
        for (int rotation = 0; rotation < 4; rotation++) {
            decKey.rotateClockwise();
            for (int r = 0; r < decKey.getSize(); r++) {
                for (int c = 0; c < decKey.getSize(); c++) {
                    if (decKey.get(r, c) == '1') {
                        plaintext += decrypt.get(r, c);
                    }
                }
            }

        }
        return plaintext;
    }

    public void setKey(KeySchablone key) {
        this.key = key;
    }

    public KeySchablone getKey() {
        return key;
    }
}
