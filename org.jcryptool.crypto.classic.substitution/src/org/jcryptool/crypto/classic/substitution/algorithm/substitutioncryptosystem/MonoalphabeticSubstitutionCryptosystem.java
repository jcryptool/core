package org.jcryptool.crypto.classic.substitution.algorithm.substitutioncryptosystem;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.Cryptosystem;
import org.jcryptool.core.cryptosystem.core.exception.ElementNotInAlphabetException;

public class MonoalphabeticSubstitutionCryptosystem<P> extends Cryptosystem<P, P, MonoalphabeticSubstitutionKey<P>> {

	public MonoalphabeticSubstitutionCryptosystem(Alphabet<? extends P> plainTextAlphabet) {
		super(plainTextAlphabet, plainTextAlphabet);
	}

	/**
	 * Calculates the monoalphabetic substitution key which reverses the cryptographic process at same operation mode (encryption / decryption)
	 * from the given key [ encryption(encryption(PLAINTEXT, KEY), INVERSE_KEY) = PLAINTEXT ] 
	 * 
	 * @param substitutionAlphabet the substitution alphabet to reverse
	 * @param plainTextAlphabet the plain text alphabet
	 * @return the inverse substitution alphabet
	 * @throws IllegalArgumentException if the substitution- and the plain text alphabet do not contain the same set of characters.
	 */
	private static <EType> List<EType> calcInverseSubstitutionAlphabet(Alphabet<EType> substitutionAlphabet, Alphabet<? extends EType> plainTextAlphabet) {
		if(! substitutionAlphabet.isSetEqualTo(plainTextAlphabet)) {
			throw new IllegalArgumentException("substitution alphabet and plaintext alphabet have different character sets.");
		}
		
		//"pt"=Plain text
		List<EType> substAlpha = substitutionAlphabet.getContent();
		List<? extends EType> ptAlpha = plainTextAlphabet.getContent();
		List<EType> inverseAlpha = new LinkedList<EType>();
		
		for(EType ptAlphaElement: ptAlpha) {
			
			EType elementBeforeEncryption = ptAlpha.get(substAlpha.indexOf(ptAlphaElement));
			inverseAlpha.add(elementBeforeEncryption);
		}
		
		return inverseAlpha;
	}

	/**
	 * see: {@link #calcInverseSubstitutionAlphabet(Alphabet, Alphabet)}
	 */
	private static <EType> MonoalphabeticSubstitutionKey<EType> calcInverseSubstitutionKey(MonoalphabeticSubstitutionKey<EType> substitutionKey, Alphabet<? extends EType> plaintextAlphabet) {
		return new MonoalphabeticSubstitutionKey<EType>(
				new Alphabet<EType>(
						calcInverseSubstitutionAlphabet(substitutionKey.getSubstitutionAlphabet(), plaintextAlphabet)
					)
			);
	}
	
	@Override
	public List<P> encrypt(List<? extends P> plainText, MonoalphabeticSubstitutionKey<P> key)
		throws ElementNotInAlphabetException {
		List<P> result = new LinkedList<P>();
		for(int i=0; i<plainText.size(); i++) {
			P plainTextElement = plainText.get(i);
			if(!getPlainTextAlphabet().getContent().contains(plainTextElement)) {
				throw new ElementNotInAlphabetException();
			}
			
			int plainTextElementPosInPlainTextAlpha = getPlainTextAlphabet().getContent().indexOf(plainTextElement);
			P substitute = key.getSubstitutionAlphabet().getContent().get(plainTextElementPosInPlainTextAlpha);
			result.add(substitute);
		}
		return result;
	}

	@Override
	public List<P> decrypt(List<? extends P> cipherText, MonoalphabeticSubstitutionKey<P> key)
		throws ElementNotInAlphabetException {
		return encrypt(cipherText, calcInverseSubstitutionKey(key, getPlainTextAlphabet()));
	}

}
