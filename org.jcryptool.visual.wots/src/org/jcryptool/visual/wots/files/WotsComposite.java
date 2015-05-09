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