/* *****************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * ***************************************************************************
 */
package org.jcryptool.analysis.vigenere.interfaces;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.jcryptool.analysis.freqanalysis.FreqAnalysisPlugin;
import org.jcryptool.analysis.vigenere.VigenereBreakerPlugin;
import org.jcryptool.analysis.vigenere.exceptions.IllegalActionException;
import org.jcryptool.analysis.vigenere.exceptions.IllegalInputException;
import org.jcryptool.analysis.vigenere.exceptions.NoContentException;
import org.jcryptool.analysis.vigenere.ui.OptionsDialogGui;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.AbstractAlgorithm;
import org.jcryptool.core.operations.algorithm.classic.AbstractClassicAlgorithm;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.operations.dataobject.IDataObject;
import org.jcryptool.core.operations.dataobject.classic.IClassicDataObject;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.editors.EditorNotFoundException;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.crypto.classic.vigenere.algorithm.VigenereAlgorithm;

/**
 * Serves as gateway between this plug-in and the other external plug-ins. Delegate all requests to the proper classes
 * of those plug-ins.
 *
 * @author Ronny Wolf
 * @version 0.0.1, 07/08/10
 */
public class DataProvider {
    private final LetterFrequency letter;

    /**
     * Constructs a new <code>DataProvider</code>. To prevent initialization from the outside the constructor is
     * private. This is part of the Singleton design pattern.
     */
    private DataProvider() {
        this.letter = new LetterFrequency();
    }

    /**
     * Contains the only instance <code>DataProvider</code>. This private class is part of the Singleton design pattern.
     * The instance is loaded on the first execution of {@link DataProvider#getInstance()}.
     *
     * @author Ronny Wolf
     * @version 0.0.1, 07/08/10
     */
    private static class SingletonHolder {
        private static final DataProvider INSTANCE = new DataProvider();
    }

    /**
     * Returns the only instance of <code>DataProvider</code>.
     *
     * @return the instance of the plug-in interface.
     */
    public synchronized static DataProvider getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     *
     * Requests list of titles of all open editors from the editor manager of JCrypTool core plug-in.
     *
     * @return a collection of titles of all open editors.
     * @throws EditorNotFoundException if editor reference could not be found or accessed.
     * @see {@link EditorsManager#getEditorTitleList()}
     */
    public String[] getEditorTitles() throws EditorNotFoundException {
        List<IEditorReference> refs = EditorsManager.getInstance().getEditorReferences();
        if (refs.size() == 0)
            throw new EditorNotFoundException();
        List<String> result = new LinkedList<String>();
        for (IEditorReference ref : refs) {
            result.add(ref.getName());
        }
        return result.toArray(new String[0]);
    }

    /**
     *
     * Requests references of all open editors from the editor manager of JCrypTool core plug-in.
     *
     * @return a collection of titles of all open editors.
     * @throws EditorNotFoundException if editor reference could not be found or accessed.
     * @see {@link EditorsManager#getEditorTitleList()}
     */
    public IEditorReference[] getEditorReferences() throws EditorNotFoundException {
        List<IEditorReference> refs = EditorsManager.getInstance().getEditorReferences();
        if (refs.size() == 0)
            throw new EditorNotFoundException();
        return refs.toArray(new IEditorReference[0]);
    }

    /**
     * reads the current value from an input stream
     *
     * @param in the input stream
     */
    private String InputStreamToString(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, IConstants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e1) {
            LogUtil.logError(VigenereBreakerPlugin.PLUGIN_ID, e1);
        }

        StringBuffer myStrBuf = new StringBuffer();
        int charOut = 0;
        String output = ""; //$NON-NLS-1$
        try {
            while ((charOut = reader.read()) != -1) {
                myStrBuf.append(String.valueOf((char) charOut));
            }
        } catch (IOException e) {
            LogUtil.logError(VigenereBreakerPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }
    
    private String InputStreamToString2(InputStream in) {
    	ByteArrayOutputStream result = new ByteArrayOutputStream();
    	byte[] buffer = new byte[1024];
    	int length;
    	try {
			while ((length = in.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
			}
			return result.toString("UTF-8");	
		} catch (IOException e) {
			LogUtil.logError(e);
			return "";
		}
    	// StandardCharsets.UTF_8.name() > JDK 7
    }

    /**
     * Requests content of the referenced editor from the editor manager of JCrypTool core plug-in. Also converts the
     * streamed content of editor manager into a <code>String</code>.
     *
     * @param editorRef the Reference object to the editor
     * @return the content of the editor; or <code>null</code> if no content is available.
     * @throws NoContentException if editor contains no characters.
     * @throws IllegalInputException if there is no input stream to read
     * @see {@link EditorsManager#getEditorContent(String)}
     */
    public String getEditorContent(final IEditorReference editorRef) throws NoContentException, IllegalInputException {
        InputStream content = EditorsManager.getInstance().getContentInputStream(editorRef.getEditor(false));

        if (null == content) {
            throw new IllegalInputException("Could not find input stream.");
        }

        String converted = InputStreamToString2(content);

        if ("".equals(converted)) { // throw exception if string is empty
            throw new NoContentException("Editor contains no characters!");
        }

        return converted;
    }

    /**
     * Requests content of editor with this title from the editor manager of JCrypTool core plug-in. Also converts the
     * streamed content of editor manager into a <code>String</code>.
     *
     * @param title the title of the editor to look up.
     * @return the content of the editor; or <code>null</code> if no content is available.
     * @throws IllegalInputException if no editor with this title is available.
     * @throws NoContentException if editor with this name contains no characters.
     * @throws EditorNotFoundException if editor with this title could not be found or accessed.
     * @see {@link EditorsManager#getEditorContent(String)}
     */
    public String getEditorContent(final String title) throws IllegalInputException, NoContentException,
            EditorNotFoundException {
        BufferedReader br = null;

        try {
            InputStream content = EditorsManager.getInstance().getEditorContent(title);

            if (null == content) {
                throw new IllegalInputException("Could not find input stream.");
            }

            br = new BufferedReader(new InputStreamReader(content, IConstants.UTF8_ENCODING));
            StringBuffer buffer = new StringBuffer();
            String line = br.readLine();

            while (null != line) {
                // don't add CRLF, otherwise results in problems by
                // formatting the string. CRLF bad!!!
                buffer.append(line);
                // buffer.append(line + "\n");
                // add CRLF, readLine() removes it.
                line = br.readLine();
            }

            String converted = buffer.toString();

            if ("".equals(converted)) { // throw exception if string is empty
                throw new NoContentException("Editor contains no characters!");
            }

            // quick fix: no spaces allowed.
            converted = replaceWhitespaces(converted);

            return converted;
        } catch (UnsupportedEncodingException ueEx) {
            logError(ueEx);
        } catch (IOException ioEx) {
            logError(ioEx);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    logError(ex);
                }
            }
        }

        throw new IllegalInputException("Other error occurred!");
    }

    /**
     * Searches in this string for whitespace characters and removes them. Search happens through regular expression.
     *
     * @param with the text with whitespaces to remove.
     * @return the text with all whitespaces removed.
     */
    private String replaceWhitespaces(final String with) {
    	return with.replaceAll("\\s*", "");
    	
//        Pattern pattern = Pattern.compile("\\SX*?");
//        Matcher matcher = pattern.matcher(with);
//        String without = "";
//
//        while (matcher.find()) {
//            without = without.concat(matcher.group());
//        }
//
//        return without;
    }

    /**
     * Deciphers this chiffre text with help of this pass phrase. Uses this filter alphabet for decryption process.
     * <p>
     * Constructs a new object of <code>VigenereAlgorithm</code> after streaming this chiffre text. Through this
     * subclass of <code>AbstractClassicAlgorithm</code> the Vigenère object has now access to all classic encryption
     * and decryption algorithms. Thereby the <code>class</code> of the objects indicates the used classic algorithm (
     * <code>VigenereAlgorithm</code> for Vigenère) and a variable the used method (<code>DECRYPT_MODE</code> for
     * deciphering).
     * <p>
     * Also needs to identify the used <code>AbstractAlphabet</code>. Possible alphabets are:
     * <ul>
     * <li><code>Printable ASCII</code>
     * <li><code>Upper and lower Latin (A-Z,a-z)</code>
     * <li><code>Upper Latin (A-Z)</code>
     * <li><code>Lower Latin (a-z)</code>
     * <li><code>Playfair/alike alphabet (25chars, w/o "J")</code>
     * <li><code>ADFGVX Alphabet</code>
     * <li><code>XOR Alphabet with 32 characters</code>
     * <li><code>XOR Alphabet with 64 characters</code>
     * </ul>
     * <p>
     * Thereupon initializes and executes the decryption algorithms. Finally converts the streamed plain text back into
     * a string of characters and returns it.
     *
     * @param chiffre the chiffre text to decipher.
     * @param passphrase the key to decipher the chiffre text with.
     * @param alphabet the filter alphabet to use.
     * @return the deciphered plain text.
     *
     * @see {@link VigenereAlgorithm}
     * @see {@link AbstractClassicAlgorithm}
     * @see {@link AlphabetsManager#getAlphabetByName(String)}
     * @see {@link AbstractAlgorithm#DECRYPT_MODE}
     */
    public String decrypt(final String chiffre, final String passphrase, final String alphabet) {
        try {
            InputStream cis = new ByteArrayInputStream(chiffre.getBytes(IConstants.UTF8_ENCODING));
            AbstractClassicAlgorithm algorithm = new VigenereAlgorithm();

            AbstractAlphabet alpha = AlphabetsManager.getInstance().getAlphabetByName(alphabet);

            algorithm.init(AbstractAlgorithm.DECRYPT_MODE, cis, alpha, passphrase.toCharArray(), null);

            IDataObject data = algorithm.execute();

            IClassicDataObject classic = (IClassicDataObject) data;
            InputStream plain = classic.getOutputIS();

            StringBuffer buffer = new StringBuffer();
           
            if(plain == null) {
            	if(classic.getOutput() != null) {
            		buffer.append(classic.getOutput());
            	} else {
            		LogUtil.logError(new RuntimeException("Vigenere breaker: could not decrypt text with guessed character"));
            	}
            } else {
            	InputStreamReader isr = new InputStreamReader(plain);
            	BufferedReader br = new BufferedReader(isr);
            	String line = br.readLine();
            	
            	while (null != line) {
            		// don't add CRLF, otherwise results in problems by
            		// formatting the string. CRLF bad!!!
            		buffer.append(line);
            		// buffer.append(line + "\n"); // add CRLF, readLine()
            		// removes it.
            		line = br.readLine();
            	}
            }

            return buffer.toString();
        } catch (UnsupportedEncodingException ueEx) {
            logError(ueEx);
        } catch (IOException ioEx) {
            logError(ioEx);
        }

        return null;
    }

    /**
     * Requests list of all available alphabets from proper manager and compiles own formatted list afterwards.
     *
     * @return a collection of alphabet designators.
     */
    public String[] getAlphabets() {
        AbstractAlphabet[] as = AlphabetsManager.getInstance().getAlphabets();
        List<String> list = new ArrayList<String>();

        for (AbstractAlphabet a : as) {
            list.add(a.getName());
        }

        return list.toArray(new String[0]);
    }

    /**
     * Looks up the index number of the default alphabet in list of all alphabets.
     *
     * @return the found index number; or <code>0</code> if default alphabet is not listed or available.
     * @deprecated since preferences are stored. Index identification is now handled internally in
     *             {@link OptionsDialogGui}.
     */
    @Deprecated
	public int getDefaultIndexAlphabets() {
        String defalph = AlphabetsManager.getInstance().getDefaultAlphabet().getName();
        String[] alphs = getAlphabets();

        for (int i = 0; i < alphs.length; i++) {
            if (defalph.equals(alphs[i])) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Compiles list of all available reference texts from frequency analysis plug-in.
     *
     * @return a collection of reference text designators.
     */
    public String[] listReferenceTexts() {
        List<String> list = new ArrayList<String>();

        list.add(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname1);
        list.add(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname2);
        list.add(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_englishreftextname1);

        return list.toArray(new String[0]);
    }

    /**
     * Looks up the index number of the default reference text in list of all reference texts.
     *
     * @return the found index number; or <code>0</code> if default text is not listed or available.
     * @deprecated since preferences are stored. Index identification is now handled internally in
     *             {@link OptionsDialogGui}.
     */
    @Deprecated
	public int getDefaultIndexReferences() {
        String defref = org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname1;
        String[] refs = listReferenceTexts();

        for (int i = 0; i < refs.length; i++) {
            if (defref.equals(refs[i])) {
                return i;
            }
        }

        return 0;
    }

    /**
     * Looks up the default reference text from frequence analysis plug-in.
     *
     * @return the default reference text.
     */
    public String getDefaultReference() {
        return org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname1;
    }

    /**
     * Looks up the default alphabet from alphabet manager.
     *
     * @return the default alphabet.
     */
    public String getDefaultAlphabet() {
        return AlphabetsManager.getInstance().getDefaultAlphabet().getName();
    }

    /**
     * Request the character set of the alphabet with this identifier. For example the designator
     * <code>Upper and lower Latin (A-Z,a-z)</code> returns a string containing all upper case characters <code>A</code>
     * through <code>Z</code> and lower case characters <code>a</code> through <code>z</code>;
     * <code>ABCDEFGHIJKLMNOPQRSTUVWXYZ</code> and <code>abcdefghijklmnopqrstuvwxyz</code> to be exact.
     *
     * @param ident the unique designator of the alphabet to look up.
     * @return a string containing all characters of the alphabet to look up.
     */
    public String getAlphabet(final String ident) {
        AbstractAlphabet alph = AlphabetsManager.getInstance().getAlphabetByName(ident);

        return String.valueOf(alph.getCharacterSet());
    }

    /**
     * Creates an image object of the three Vigenère steps.
     * <p>
     * cases:
     * <ol>
     * <li>Friedman test
     * <li>frequency distribution
     * <li>decryption
     * </ol>
     *
     * @param step the step of the image to create.
     * @return object of the image to create; or <code>null</code> if image is unknown or not found.
     */
    public Image getImage(final int step) {
        String path;

        switch (step) {
            case 1:
                path = "img/" + Messages.UI_file1_2;
                break;
            case 2:
                path = "img/" + Messages.UI_file2_2;
                break;
            case 3:
                path = "img/" + Messages.UI_file3_2;
                break;
            default:
                path = null;
                break;
        }

        if (null != path) {
            return ImageService.getImage(VigenereBreakerPlugin.PLUGIN_ID, path);
        }

        return null;
    }

    /**
     * Reads the content of the reference file with this identifying name.
     *
     * @param ident the designator of the reference file to read.
     * @return the content of the reference file.
     * @throws NoContentException if file could not be found or read properly.
     */
    public String readReferenceText(final String ident) throws NoContentException {
        try {
            String name = getFilename(ident);
            URL url = new URL(FreqAnalysisPlugin.getDefault().getBundle().getEntry("/"), name);
            InputStream in = url.openStream();
            InputStreamReader isr = new InputStreamReader(in, IConstants.UTF8_ENCODING);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            String line = br.readLine();

            while (null != line) {
                // buffer.append(line + "\n"); // CRLF bad!!!
                buffer.append(line + "\n");
                line = br.readLine();
            }

            return buffer.toString();
        } catch (MalformedURLException muEx) {
            logError(muEx);
        } catch (UnsupportedEncodingException ueEx) {
            logError(ueEx);
        } catch (IOException ioEx) {
            logError(ioEx);
        }

        throw new NoContentException("Unable to find or read reference text.");
    }

    /**
     * Returns the file name to this designator.
     *
     * @param ident the designator of the reference text file.
     * @return the file name of the reference text.
     */
    private String getFilename(final String ident) {
        if (ident.equals(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname1)) {
            return org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_0;
        } else if (ident.equals(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname2)) {
            return org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_1;
        } else {
            return org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_2;
        }
    }

    /**
     * Opens a new editor with this content.
     *
     * @param content the content to open the editor with.
     * @throws IllegalActionException if new editor could not be opened.
     */
    public void openEditor(final String content) throws IllegalActionException {
        try {
            InputStream cis = new ByteArrayInputStream(content.getBytes(IConstants.UTF8_ENCODING));
            IEditorInput output = AbstractEditorService.createOutputFile(cis);
            EditorsManager.getInstance().openNewTextEditor(output);
        } catch (UnsupportedEncodingException usEx) {
            logError(usEx);
            throw new IllegalActionException("Unsupported encoding.");
        } catch (PartInitException piEx) {
            logError(piEx);
            throw new IllegalActionException("Could not initialize.");
        }
    }

    public HashMap<Integer, Float> getLetterFrequency(final String ident) {
        if (ident.equals(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname1)) {
            return letter.getGerman();
        } else if (ident.equals(org.jcryptool.analysis.freqanalysis.ui.Messages.FullAnalysisUI_germanreftextname2)) {
            return letter.getGerman();
        } else {
            return letter.getEnglish();
        }
    }

    private void logError(final Exception ex) {
        LogUtil.logError(ex);
    }

	public static String filterChiffre(String chiff) {
		TransformData filter = new TransformData();
		filter.setUnmodified();
		filter.setUppercaseTransformationOn(true);
		filter.setLeerTransformationON(true);
		filter.setAlphabetTransformationON(false);
		return Transform.transformText(chiff, filter);
	}
}
