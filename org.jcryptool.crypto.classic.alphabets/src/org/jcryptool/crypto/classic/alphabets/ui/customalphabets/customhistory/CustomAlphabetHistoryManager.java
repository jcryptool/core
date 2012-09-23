package org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

public class CustomAlphabetHistoryManager {
	
	public static List<AbstractAlphabet> customAlphabets = new LinkedList<AbstractAlphabet>();
	static {
		//TODO: !remove
		customAlphabets.add(new AtomAlphabet("abcdefg\n\t x"));
	}
	
}
