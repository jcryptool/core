package org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

public class CustomAlphabetHistoryManager {
	
	public static List<AbstractAlphabet> customAlphabets = new LinkedList<AbstractAlphabet>();
	static {
		//TODO: !remove
		AtomAlphabet a1 = new AtomAlphabet("abcdefg\n\t x");
		a1.setName("Test-Alpha1");
		customAlphabets.add(a1);
		
		AtomAlphabet a2 = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		a1.setName("A-Za-z");
		customAlphabets.add(a2);
	}
	
}
