package org.jcryptool.crypto.classic.substitution.algorithm.substitutioncryptosystem;

import org.jcryptool.core.cryptosystem.core.Alphabet;
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
}
