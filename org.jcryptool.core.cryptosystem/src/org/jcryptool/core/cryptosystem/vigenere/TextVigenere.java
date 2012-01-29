package org.jcryptool.core.cryptosystem.vigenere;

import org.jcryptool.core.cryptosystem.core.Cryptosystem;
import org.jcryptool.core.cryptosystem.core.text.CharAlphabet;
import org.jcryptool.core.cryptosystem.core.text.TextCompatibleCryptosystemWrapper;
import org.jcryptool.core.cryptosystem.core.text.TextConverter;

public class TextVigenere extends TextCompatibleCryptosystemWrapper<Character, Character, VigenereKey<Character>> {

    public TextVigenere(CharAlphabet charAlphabet) {
        super(createVigenereSystem(charAlphabet), TextConverter.CHAR_TEXT_CONVERTER, TextConverter.CHAR_TEXT_CONVERTER);
    }

    private static Cryptosystem<Character, Character, VigenereKey<Character>> createVigenereSystem(
            CharAlphabet charAlphabet) {
        return new VigenereCryptosystem<Character>(charAlphabet);
    }
}
