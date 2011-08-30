package org.jcryptool.analysis.kegver.layer4;

import org.jcryptool.analysis.kegver.layer3.L3;
import org.jcryptool.analysis.kegver.layer5.L5;

/**
 * Separates business logic from user interface
 * @author hkh
 *
 */
public class L4 {
				
	public static String printString(String inString) {
		return L5.get().printString(inString);
	}

	public static void startL3() {
		L3.startKegverDummy();
	}
}
