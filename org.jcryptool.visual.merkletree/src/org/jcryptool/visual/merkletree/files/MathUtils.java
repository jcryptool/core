package org.jcryptool.visual.merkletree.files;

public class MathUtils {
	
	/**
	 * 
	 * @param number
	 * 			Number of which ld is calculated
	 * @return ld(number)
	 */
	public static int log2nlz( int number )
	{
	    if( number == 0 )
	        return 0; // or throw exception
	    return 31 - Integer.numberOfLeadingZeros( number );
	}
}
