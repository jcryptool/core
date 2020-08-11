// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2009, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.algorithm;

import java.util.Random;

/**
 * This class is used to create a random factor 

 * @author  Biqiang Jiang

 * @version 1.0, 01/09/09

 * @since   JDK1.5.7

 */
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
