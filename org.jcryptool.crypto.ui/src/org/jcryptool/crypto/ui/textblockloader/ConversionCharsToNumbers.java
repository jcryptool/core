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
package org.jcryptool.crypto.ui.textblockloader;

import java.util.LinkedList;
import java.util.List;

public abstract class ConversionCharsToNumbers {

	public abstract Integer convert(Character c);
	public abstract Character revert(Integer i);
	public abstract Integer getMaxNumberValue();
	
	public List<Integer> convert(String s) {
		List<Integer> result = new LinkedList<Integer>();
		for(char c: s.toCharArray()) {
			Integer converted = convert(c);
			if(converted != null) {
				result.add(converted);
			}
		}
		return result;
	}
	
	public String revert(List<Integer> numbers) {
		StringBuilder result = new StringBuilder();
		for(Integer number: numbers) {
			Character reverted = this.revert(number);
			if(reverted != null) {
				result.append(reverted);
			}
		}
		
		return result.toString();
	}
	
}
