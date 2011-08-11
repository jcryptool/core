package org.jcryptool.analysis.kegver.layer3.kegverprotocol;


public abstract class KegverStateSuper {
	
	private KegverStateContext aKegVer = null;

	public KegverStateSuper(KegverStateContext inKegVer){
		this.setKegVer(inKegVer);
	}

	protected KegverStateContext getKegver() {
		return this.aKegVer;
	}
	
	final private KegverStateContext setKegVer(KegverStateContext inKegVer) {
		this.aKegVer = inKegVer;
		return this.getKegver();
	}
}
