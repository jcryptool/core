package org.jcryptool.crypto.classic.substitution.algorithm.substitutioncryptosystem;

import org.jcryptool.core.cryptosystem.core.Alphabet;
import org.jcryptool.core.cryptosystem.core.Key;

/**
 * The key class to the monoalphabetic substitution cryptosystem
 * 
 * @author Simon L
 *
 */
public class MonoalphabeticSubstitutionKey<EType> extends Key {
	private Alphabet<EType> substitutionAlphabet;
	
	/**
	 * Creates the key with the (non-null, non-empty) substitution alphabet
	 * 
	 * @param substitutionAlphabet the substitution alphabet (this object will not be stored or altered, only the elements will be taken)
	 */
	public MonoalphabeticSubstitutionKey(Alphabet<? extends EType> substitutionAlphabet) {
		if(substitutionAlphabet == null || substitutionAlphabet.getContent().size() == 0) {
			if(substitutionAlphabet == null) throw new IllegalArgumentException("null alphabet");
			throw new IllegalArgumentException("empty alphabet");
		}
		this.substitutionAlphabet = new Alphabet<EType>(substitutionAlphabet.getContent());
	}
	
	/**
	 * @return the substitution alphabet
	 */
	public Alphabet<EType> getSubstitutionAlphabet() {
		return substitutionAlphabet;
	}
	
}
