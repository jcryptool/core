package org.jcryptool.core.cryptosystem.vigenere;

import org.jcryptool.core.cryptosystem.core.Cryptosystem;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;
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
	
	public static void main(String[] args) throws ElementNotInAlphabetException {
		CharAlphabet alphabet = 
			new CharAlphabet("abcdefghijklmnopqrstuvwxyz ");
		TextVigenere vigenereCryptosystem = 
			new TextVigenere(alphabet);
		VigenereKey<Character> key = 
			new VigenereKey<Character>(alphabet.parseString("key"), alphabet);
		
		String plaintext = "this is a test text";
		String encryptedText;
		String decryptedText;
		
		encryptedText = vigenereCryptosystem.encryptTextToText(plaintext, key);
		decryptedText = vigenereCryptosystem.decryptTextToText(encryptedText, key);
		
		System.out.println("Plaintext:                   " + plaintext);
		System.out.println("encrypt(plaintext,  'key') = " + encryptedText);
		System.out.println("decrypt(ciphertext, 'key') = " + decryptedText);
	}


}
