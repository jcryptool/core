/**
 * This class is used to create a random factor 

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */

package org.jcryptool.algorithm;


import java.util.Random;

public class RandomFactorCreator {
	
	
	public int randomCreator(int p) {
		
		Random rd1 = new Random();
		
		for(int i=0; i<10;i++){
			int r = rd1.nextInt(p);
			if(r!=0)
				return r;
		
		
		}
		return p;

	}
	

}
