/**
 * 
 */
package org.jcryptool.analysis.fleissner2.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jcryptool.analysis.fleissner2.Activator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;

/**
 * @author Dinah
 *
 */
public class LoadFiles {
    
    protected String textFiles(/*boolean plain, */int exampleIndex) {
        
        String textName = "";
        
//        if (plain) {
            switch (exampleIndex) {
            
            case 0: 
                textName = "files/dawkinsGerPlaintext.txt";
                break;
            case 1:     
                textName = "files/wikiFruehchristlicheKunstGerPlaintext.txt";
                break;
            case 2:
                textName = "files/dawkinsEngPlaintext.txt";
                break;
            case 3:
                textName = "files/visualArtsEngPlaintext.txt";
                break;     
            }
//        }
//        else {
//            switch (exampleIndex) {
//            
//            case 0: 
//                textName = "files/dawkinsGerCiphertext7.txt";
//                break;
//            case 1:     
//                textName = "files/wikiFruehchristlicheKunstGerCiphertext7.txt";
//                break;
//            case 2:
//                textName = "files/dawkinsEngCiphertext7.txt";
//                break;
//            case 3:
//                textName = "files/visualArtsEngCiphertext7.txt";
//                break;     
//            }
//        }
        
        return textName;
    }
    
    protected String statisticFiles(int exampleIndex) {
        
        String textName = "";
        
            switch (exampleIndex) {
            
            case 0: 
                textName = "files/de-4gram-nocs.bin";
                break;
            case 1:     
                textName = "files/de-3gram-nocs.bin";
                break;
            case 2:
                textName = "files/en-4gram-nocs.bin";
                break;
            case 3:
                textName = "files/en-3gram-nocs.bin";
                break;     
            }
        
        return textName;
    }

    
    /**
     * opens a resource file stream
     *
     * @param filename the file path
     * @return the inputStream containing the file's content
     */
    protected InputStream openMyTestStream(final String filename) {
        try {
            URL installURL = Activator.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
            URL url = new URL(installURL, filename);
            return (url.openStream());
        } catch (MalformedURLException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        return null;
    }
    
    
    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    protected String InputStreamToString(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(Activator.PLUGIN_ID, e1);
        } catch (IllegalArgumentException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }

        StringBuffer myStrBuf = new StringBuffer();
//        String line = null;
//        try {
//            line = reader.readLine();
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
//            while (line !=null) {
//                output += line;
//                line=reader.readLine();
//            }
            
            
        } catch (IOException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }

}
