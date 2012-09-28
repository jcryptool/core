package org.jcryptool.crypto.classic.alphabets.ui.customalphabets.customhistory;

import java.util.LinkedList;
import java.util.List;

import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;

public class CustomAlphabetHistoryManager {
	
	public static List<AbstractAlphabet> customAlphabets = new LinkedList<AbstractAlphabet>();
	static {
		//TODO: !remove
		AtomAlphabet a1 = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXY0123456789");
		a1.setName("A-Z and digits");
		customAlphabets.add(a1);
		
		AtomAlphabet a2 = new AtomAlphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
		a1.setName("A-Z a-z and digits");
		customAlphabets.add(a2);
	}
	
}
