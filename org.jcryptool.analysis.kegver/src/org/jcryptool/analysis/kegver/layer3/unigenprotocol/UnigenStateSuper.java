package org.jcryptool.analysis.kegver.layer3.unigenprotocol;

public class UnigenStateSuper {
	
	private UnigenStateContext aUnigen = null;

	public UnigenStateSuper(UnigenStateContext inUnigen){
		this.setUnigen(inUnigen);
	}

	protected UnigenStateContext getUnigen() {
		return this.aUnigen;
	}
	
	final private UnigenStateContext setUnigen(UnigenStateContext inUnigen) {
		this.aUnigen = inUnigen;
		return this.getUnigen();
	}
}