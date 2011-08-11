package org.jcryptool.analysis.kegver.layer2;



public class SetMethodFailedException extends Exception {

	public SetMethodFailedException(String inString) {
		super(inString);
	}
	
	public SetMethodFailedException() {
		super("This should not happen!");
	}
	
	@Override
	public void printStackTrace(){
		System.err.println(Tools.catchThis(this));	
		super.printStackTrace();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8396066443760996418L;

}
