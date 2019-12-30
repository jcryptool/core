//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textblockloader.conversion;

import java.util.List;

import org.jcryptool.crypto.ui.textblockloader.ConversionCharsToNumbers;
import org.jcryptool.crypto.ui.textblockloader.ConversionNumbersToBlocks;

public class ConversionStringToBlocks {
	
	private ConversionCharsToNumbers ctn;
	private ConversionNumbersToBlocks ntb;
	
	
	
	public ConversionStringToBlocks(ConversionCharsToNumbers ctn,
			ConversionNumbersToBlocks ntb) {
		this.ctn = ctn;
		this.ntb = ntb;
	}

	public List<Integer> convert(String text) {
		List<Integer> numbers = ctn.convert(text);
		List<Integer> blocks = ntb.convert(numbers);
		return blocks;
	}
	
	public String revert(List<Integer> blocks) {
		List<Integer> numbers = ntb.revert(blocks);
		String string = ctn.revert(numbers);
		return string;
	}
	
	public ConversionCharsToNumbers getCtn() {
		return ctn;
	}
	
	public ConversionNumbersToBlocks getNtb() {
		return ntb;
	}
	
}
