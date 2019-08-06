/**
 * 
 */
package org.jcryptool.analysis.fleissner.UI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.channels.FileChannel;
import org.eclipse.core.runtime.FileLocator;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;

/**
 * @author Dinah
 *
 */
public class LoadFiles {
    
    protected String textFiles(/*boolean plain, */int exampleIndex) {
        
        String textName = "";
        
            switch (exampleIndex) {
            
            case 0: 
                textName = "files/dawkinsGerPlaintextOriginal.txt";
                break;
            case 1:     
                textName = "files/wikiFruehchristlicheKunstGerPlaintextOriginal.txt";
                break;
            case 2:
                textName = "files/dawkinsEngPlaintextOriginal.txt";
                break;
            case 3:
                textName = "files/visualArtsEngPlaintext.txt";
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

    protected FileInputStream openMyFileStream(final String filename) {
        try {
        	
        	
        	
        	
            URL installURL = new URL(Activator.getDefault().getBundle().getEntry("/"), filename); //$NON-NLS-1$
//            URL url = new URL(installURL, filename);
            File file = new File(FileLocator.resolve(installURL).toURI());
            return new FileInputStream(file);
        } catch (MalformedURLException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        } catch (URISyntaxException e) {
        	LogUtil.logError(Activator.PLUGIN_ID, e);
		}
        return null;
    }
    
    public double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
    
//  load text statistic
    public double[] loadBinNgramFrequencies(FileInputStream file, String language, int nGramSize) throws FileNotFoundException
    {
        int m = 0;
        
        switch(language) {
        case "german":  m = 30;
                        break;
        case "english": m = 26;
                        break;
        }


        double ngrams[] = new double[(int) Math.pow(m, nGramSize)];
        ByteBuffer myByteBuffer = ByteBuffer.allocate(((int) Math.pow(m, nGramSize)) * 8);
        myByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        DoubleBuffer doubleBuffer = myByteBuffer.asDoubleBuffer();

        try { 
//            FileInputStream fileInputStream = new FileInputStream(filename);
            FileChannel fileChannel = file.getChannel();
            
            fileChannel.read(myByteBuffer);
            fileChannel.close();
            file.close();   
//            log.info("Statistics succesfully loaded");
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw new FileNotFoundException("File not found !");
            }   
        doubleBuffer.get(ngrams);
        
        return ngrams;
    }
}
