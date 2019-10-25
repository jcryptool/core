/**
 * 
 */
package org.jcryptool.analysis.fleissner.UI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.analysis.fleissner.logic.InvalidParameterCombinationException;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;

/**
 * @author Dinah
 *
 */
public class LoadFiles {
    
    /**
     * returns name of selected item in drop down button
     * @param exampleIndex
     * @return fileName of chosen fileIndex
     */
    protected String textFiles(int exampleIndex) {
        
        String textName = null;
        
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

        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            } 
        } catch (IOException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }
    
    /**
     * returns the fileName related to item index chosen in drop down
     * @param exampleIndex
     * @return the fileName related to index
     */
    protected String statisticFiles(int exampleIndex) {
        
        String textName = null;
        
            switch (exampleIndex) {
            
            case 0: 
                textName = "files/de-4gram-nocs.bin";
                break;
            case 1:
                textName = "files/en-4gram-nocs.bin";
                break;
            case 2:
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
    protected InputStream openMyFileStream(final String filename) {
        try {
            URL installURL = new URL(Activator.getDefault().getBundle().getEntry("/"), filename); //$NON-NLS-1$  	
        	InputStream is = installURL.openConnection().getInputStream();

        	return is;
        } catch (MalformedURLException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        } catch (IOException e) {
            LogUtil.logError(Activator.PLUGIN_ID, e);
        }
        return null;
    }
    
    /**
     * opening file for input text
     * @param type
     * @return the choosen .txt file path
     */
    public String openFileDialog(int type) {

        Shell shell = new Shell();
        FileDialog dialog = new FileDialog(shell, type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { "*.txt" }); //limits the eligible files to *.txt files
        dialog.setFilterNames(new String[] { "Text Files (*.txt)" }); //$NON-NLS-1$
        dialog.setOverwrite(true);
        return dialog.open();
    }
    
    /**
     * opening file for input statistics
     * @param type
     * @return the choosen statistics .bin file path
     */
    public String openStatFileDialog(int type) {
        
        Shell shell = new Shell();
        FileDialog dialog = new FileDialog(shell, type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { "*.bin" }); //limits the eligible files to *.bin files
        dialog.setFilterNames(new String[] { "Binary Files (*.bin)" }); //$NON-NLS-1$
        dialog.setOverwrite(true);
        return dialog.open();
    }
    
    /**
     * 
     * @param bytes
     * @return
     */
    public double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }
    
    /**
     * loads language statistic as double array
     * @param file the fileInputStream
     * @param language
     * @param nGramSize
     * @return double array with statistic values
     * @throws FileNotFoundException
     * @throws InvalidParameterCombinationException
     */
    public double[] loadBinNgramFrequencies(InputStream file, String language, int nGramSize) throws FileNotFoundException, InvalidParameterCombinationException
    {
        int m = 0;
        
        switch(language) {
        case "german":  m = 30;
                        break;
        case "english": m = 26;
                        break;
        }

        double size=0, fileSize =0;
        try {
            size = Math.pow(m, nGramSize)*8;
            fileSize = file.available();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        if (fileSize != size)
            throw new InvalidParameterCombinationException("Statistic does not fit nGram size and/or language");
        double ngrams[] = new double[(int) Math.pow(m, nGramSize)];
        ByteBuffer myByteBuffer = ByteBuffer.allocate(((int) size) * 8);
        myByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        DoubleBuffer doubleBuffer = myByteBuffer.asDoubleBuffer();

        try {          
        	ReadableByteChannel rbc = Channels.newChannel(file);
            rbc.read(myByteBuffer);
            rbc.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.logError(Activator.PLUGIN_ID, e);
                throw new FileNotFoundException("File not found !");
            }   
        doubleBuffer.get(ngrams);
        
        return ngrams;
    }
}
