package org.jcryptool.analysis.kegver.layer3;

import org.jcryptool.analysis.kegver.layer3.kegverprotocol.KegverStateContext;

public class L3 {
	
	public static String startKegverDummy(){
		CABehavior inCA = new DummyCA();
		UserBehavior inUser = new DummyUser();
		KegverStateContext aKegverStateContext = new KegverStateContext(inCA, inUser);
		aKegverStateContext.setup();
		return null;
	}

}
