//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2015, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.wots.files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WotsComposite {
	
	public WotsComposite(){
		
	}
	
	public static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        
	       // view.txt_message.setText(sb.toString());
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
//test push