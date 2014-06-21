package org.jcryptool.crypto.ui.textblockloader.conversion;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.ui.textblockloader.ConversionCharsToNumbers;

public class AlphabetCharsToNumbers extends ConversionCharsToNumbers {

	private AbstractAlphabet alpha;
	private boolean is256ASCII;
	
	public AlphabetCharsToNumbers(AbstractAlphabet alpha, boolean is256ASCII) {
		super();
		this.alpha = alpha;
		this.is256ASCII = is256ASCII;
	}
	
	public AlphabetCharsToNumbers(AbstractAlphabet alpha) {
		this(alpha, false);
	}

	@Override
	public Integer convert(Character c) {
		if(alpha.contains(c)){
			int idx = -1;
			for(int i = 0; i<alpha.getCharacterSet().length; i++) {
				if(c.charValue() == alpha.getCharacterSet()[i]) {
					idx = i;
					break;
				}
			}
			
			if(idx < 0) return null;
			return idx;
			
		} else {
			return null;
		}
	}
	
	@Override
	public Character revert(Integer i) {
		return convertBack(i);
	}

	public Character convertBack(Integer number) {
		if(number != null && number > -1 && number < this.alpha.getCharacterSet().length) {
			return this.alpha.getCharacterSet()[number];
		} else {
			return null;
		}
	}
	
	public boolean isIs256ASCII() {
		return is256ASCII;
	}
	
	public AbstractAlphabet getAlpha() {
		return alpha;
	}

	@Override
	public Integer getMaxNumberValue() {
		return alpha.getCharacterSet().length-1;
	}
	
}
