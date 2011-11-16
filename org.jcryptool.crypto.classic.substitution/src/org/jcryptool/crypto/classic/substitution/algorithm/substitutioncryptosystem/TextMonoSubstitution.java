package org.jcryptool.crypto.classic.substitution.algorithm.substitutioncryptosystem;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;
import org.jcryptool.core.cryptosystem.core.text.CharAlphabet;
import org.jcryptool.core.cryptosystem.core.text.TextCompatibleCryptosystemWrapper;
import org.jcryptool.core.cryptosystem.core.text.TextConverter;

public class TextMonoSubstitution extends TextCompatibleCryptosystemWrapper<Character, Character, MonoalphabeticSubstitutionKey<Character>> {

	
	
	public TextMonoSubstitution(Alphabet<? extends Character> plaintextAlphabet) {
		super(createSubstitutionSystem(plaintextAlphabet), 
			TextConverter.CHAR_TEXT_CONVERTER, TextConverter.CHAR_TEXT_CONVERTER);
	}

	
	private static MonoalphabeticSubstitutionCryptosystem<Character> createSubstitutionSystem(Alphabet<? extends Character> plaintextAlphabet) {
		return new MonoalphabeticSubstitutionCryptosystem<Character>(plaintextAlphabet);
	}
	
	public static void main(String[] args) throws ElementNotInAlphabetException {
		CharAlphabet alphabet = 
			new CharAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		TextMonoSubstitution cryptosystem = 
			new TextMonoSubstitution(alphabet);
		MonoalphabeticSubstitutionKey<Character> key = 
			new MonoalphabeticSubstitutionKey<Character>(Alphabet.createFilledAlphabetFromKeyword(alphabet.parseString("KEY"), alphabet));
		
		String plaintext = "THISISATESTTEXT";
		String encryptedText;
		String decryptedText;
		
		encryptedText = cryptosystem.encryptTextToText(plaintext, key);
		decryptedText = cryptosystem.decryptTextToText(encryptedText, key);
		
		System.out.println("Plaintext:                   " + plaintext);
		System.out.println("encrypt(plaintext,  'key') = " + encryptedText);
		System.out.println("decrypt(ciphertext, 'key') = " + decryptedText);
	}
	
	
}
