package org.jcryptool.crypto.ui.textblockloader;

import java.math.BigInteger;
import java.util.List;

public abstract class ConversionNumbersToBlocks {

	public abstract List<Integer> convert(List<Integer> i);
	public abstract List<Integer> revert(List<Integer> i);
	public abstract Integer getMaxBlockValue();
	
}
