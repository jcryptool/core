// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.runtime.FileLocator;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AbstractAlphabetStore;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.crypto.classic.alphabets.Alphabet;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Manages all available alphabets.
 *
 * @author t-kern (refactoring to the new architecture)
 */
public class AlphabetStore extends AbstractAlphabetStore {
    private static final String ALPHABET_FOLDER = "alphabets"; //$NON-NLS-1$
    private static final String ALPHABET_FILE = "alphabets.xml"; //$NON-NLS-1$
    private static final String ALPHABET_XML_SCHEMA_PATH = "alphabets.xsd"; //$NON-NLS-1$
    private Vector<Alphabet> alphabets;
    private String alphaPath;

    /**
     * Adds an alphabet to the alphabet store
     *
     * @param alpha
     */
    @Override
	public void addAlphabet(AbstractAlphabet alpha) {
        alphabets.addElement(convertAbstractAlphaToAlpha(alpha));
        try {
            storeAlphabets();
        } catch (IOException e) {
            LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Exception while adding an alphabet", e, true); //$NON-NLS-1$
        }
    }

    //TODO: !alphaRefactoring: this method won't be needed anymore...
    public static Alphabet convertAbstractAlphaToAlpha(AbstractAlphabet alpha) {
    	if(alpha instanceof Alphabet) {
        	return (Alphabet) alpha;
        } else { //TODO: !alphaRefactor Diese Verzweigung muss entfernt werden. 
        	return new Alphabet(alpha.getCharacterSet(), alpha.getName(), alpha.getShortName(), AbstractAlphabet.DISPLAY, alpha.isBasic());
        }
	}

	private void addInternAlphabet(Alphabet alpha) {
        alphabets.addElement(alpha);
    }

    @Override
	public Alphabet[] getAlphabets() {
        Alphabet[] alphas = new Alphabet[alphabets.size()];

        alphabets.toArray(alphas);

        return alphas;
    }

    @Override
	public Alphabet getAlphabetByName(String name) {
        Enumeration<Alphabet> enumerator = alphabets.elements();

        while (enumerator.hasMoreElements()) {
            Alphabet alpha = enumerator.nextElement();
            if (alpha.getName().equals(name)) {
                return alpha;
            }
        }

        return null;
    }

    @Override
    public AbstractAlphabet getAlphabetByShortName(String name) {
        Enumeration<Alphabet> enumerator = alphabets.elements();

        while (enumerator.hasMoreElements()) {
            Alphabet alpha = enumerator.nextElement();
            if (name.equals(alpha.getShortName())) {
                return alpha;
            }
        }

        return null;
    }

    @Override
	public void setAlphabets(AbstractAlphabet[] alphas) {
        Vector<Alphabet> v = new Vector<Alphabet>(alphas.length);

        for (int i = 0; i < alphas.length; i++) {
            v.addElement(convertAbstractAlphaToAlpha(alphas[i]));
        }

        alphabets = v;
    }

    public Alphabet getDefaultAlphabet() {
        Enumeration<Alphabet> enumerator = alphabets.elements();

        while (enumerator.hasMoreElements()) {
            Alphabet alpha = enumerator.nextElement();
            if (alpha.isDefaultAlphabet()) {
                return alpha;
            }
        }

        return null;
    }

    boolean hardResetDebug = false;
    @Override
	public void init() {
        // if alphabets is not null the alphabet store will only be initialized
        if (alphabets == null) {
            try {
                File alphaFile = getAlphabetsFile();
                alphaPath = alphaFile.getAbsolutePath();
                if (alphaFile.exists()) {
                    // for the case the alphabets file is empty, recreate anyway
                    if (alphaFile.length() > 0 && !hardResetDebug) {
                        validateAlphabet(alphaFile);
                        loadAlphabets(alphaFile);
                    } else {
                        processAlphabetsCreation();
                    }
                }
            } catch (ParserConfigurationException e) {
                LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "A serious configuration error was indicated.", e, false); //$NON-NLS-1$
            } catch (SAXException e) {
                LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Exception while validating the alphabets xml file. \n" //$NON-NLS-1$
                        + " The containing xml is not well-formed.", e, true); //$NON-NLS-1$

                processAlphabetsCreation();
            } catch (IOException e) {
                LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Exception while initializing the alphabets", e, false); //$NON-NLS-1$
            } catch (AlphaFileOutOfDateException e) {
            	LogUtil.logInfo(AlphabetsPlugin.PLUGIN_ID, e.getMessage());
			}
        } else {
        	ensureDefaultAlphabetIsSet();
        }
		processAlphabetsCreation();
    }

    private void ensureDefaultAlphabetIsSet() {
        for (Alphabet alpha: alphabets) {
        	if (alpha.getName().equals(AlphabetsManager.FACTORY_DEFAULT_ALPHABET) || alpha.getName().equals(AlphabetsManager.FACTORY_DEFAULT_ALPHABET_DE)) {
        		alpha.setDefaultAlphabet(true);
			}
        	else
        	{
        		alpha.setDefaultAlphabet(false);
        	}
        }
	}

	private void processAlphabetsCreation() {
        alphabets = new Vector<Alphabet>();
        generateStandardAlphabets();
        generateClassicAlphabets();
        ensureDefaultAlphabetIsSet();
        try {
            storeAlphabets();
        } catch (IOException e) {
            LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Exception while storing the alphabets", e, true); //$NON-NLS-1$
        }
    }

    private void validateAlphabet(File alphaFile) throws ParserConfigurationException, SAXException, IOException, AlphaFileOutOfDateException {
        // parse an XML document into a DOM tree
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(alphaFile);
        
        NodeList characterSets = document.getElementsByTagName("characterSet"); //$NON-NLS-1$
        boolean umlautsFound = false;
        for(int i=0; i<characterSets.getLength(); i++) {
        	Node set = characterSets.item(i);
        	String setContent = set.getTextContent();
        	if(setContent != null) {
        		if(setContent.contains("ö")) { //$NON-NLS-1$
        			umlautsFound = true;
        			break;
        		}
        	}
        }
        if(!umlautsFound) {
        	throw new AlphaFileOutOfDateException("no umlauts were found in the present alphabet file; this must be out of date. creating a new file..."); //$NON-NLS-1$
        }

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(FileLocator.toFileURL(
                getClass().getClassLoader().getResource(ALPHABET_XML_SCHEMA_PATH)).toString());
        Schema schema = factory.newSchema(schemaFile);

        // create a validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree
        validator.validate(new DOMSource(document));
    }

    private void generateClassicAlphabets() {
        // ADFGVX
        char[] adfgvxCharset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(); //$NON-NLS-1$
        Alphabet adfgvxAlphabet = new Alphabet(adfgvxCharset,
                Messages.AlphabetStore_alpha_adfgvx_long, Messages.AlphabetStore_alpha_adfgvx_short, AbstractAlphabet.NO_DISPLAY, true); 
        addInternAlphabet(adfgvxAlphabet);

        // Xor with 32 (2^5)
        char[] xor32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ012345".toCharArray(); //$NON-NLS-1$
        Alphabet xor32Alphabet = new Alphabet(xor32,
                Messages.AlphabetStore_alpha_xor32_long, Messages.AlphabetStore_alpha_xor32_short, AbstractAlphabet.NO_DISPLAY, true); 
        addInternAlphabet(xor32Alphabet);

        // Xor with 64 (2^6)
        char[] xor64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789,.".toCharArray(); //$NON-NLS-1$
        Alphabet xor64Alphabet = new Alphabet(xor64,
                Messages.AlphabetStore_alpha_xor64_long, Messages.AlphabetStore_alpha_xor64_short, AbstractAlphabet.NO_DISPLAY, true); 
        addInternAlphabet(xor64Alphabet);
    }

    private void generateStandardAlphabets() {
    	//TODO: maybe do umlauts in another alphabet, but for now...
        char[] extraChars = Messages.AlphabetStore_extrachars.toCharArray();
    	char[] set = new char[95+extraChars.length];
        for (int i = 32; i < 127; i++) {
            set[i - 32] = (char) i;
        }
        for(int i=0; i<extraChars.length; i++) {
        	set[set.length-extraChars.length+i] = extraChars[i];
        }
        Alphabet shortAsciiAlphabet = new Alphabet(set, Messages.AlphabetStore_alpha_ascii_long, Messages.AlphabetStore_alpha_ascii_short, AbstractAlphabet.NO_DISPLAY, true); 

        addInternAlphabet(shortAsciiAlphabet);

        char[] set2 = new char[52];
        for (int i = 65; i < 91; i++) {
            set2[i - 65] = (char) i;
        }
        for (int i = 97; i < 123; i++) {
            set2[i - 71] = (char) i;
        }

        Alphabet characterAlphabet = new Alphabet(set2,
                Messages.AlphabetStore_alpha_uplolatin_long, Messages.AlphabetStore_alpha_uplolatin_short, AbstractAlphabet.NO_DISPLAY, true); 

        addInternAlphabet(characterAlphabet);

        char[] set4 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); //$NON-NLS-1$

        Alphabet latinUpperCaseAlphabet = new Alphabet(set4,
                Messages.AlphabetStore_alpha_uplatin_long, Messages.AlphabetStore_alpha_uplatin_short, AbstractAlphabet.NO_DISPLAY, true); 

        addInternAlphabet(latinUpperCaseAlphabet);

        char[] set5 = "abcdefghijklmnopqrstuvwxyz".toCharArray(); //$NON-NLS-1$

        Alphabet latinLowerCaseAlphabet = new Alphabet(set5,
                Messages.AlphabetStore_alpha_lowlatin_long, Messages.AlphabetStore_alpha_lowlatin_short, AbstractAlphabet.NO_DISPLAY, true); 

        addInternAlphabet(latinLowerCaseAlphabet);

        characterAlphabet.setDefaultAlphabet(true);

        char[] set6 = "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray(); //$NON-NLS-1$

        Alphabet playfairalphabet = new Alphabet(set6,
                Messages.AlphabetStore_alpha_playfair_long, Messages.AlphabetStore_alpha_playfair_short, AbstractAlphabet.NO_DISPLAY, true); 

        addInternAlphabet(playfairalphabet);
    }

    @Override
	public void removeAlphabet(AbstractAlphabet alphabet) {
        alphabets.remove(alphabet);

        try {
            storeAlphabets();
        } catch (IOException e) {
            LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Exception while removing an alphabet", e, true); //$NON-NLS-1$
        }
    }

    private File getAlphabetsFile() throws IOException {
        File alphabets = new File(new File(DirectoryService.getWorkspaceDir()), ALPHABET_FOLDER);
        if (!alphabets.exists()) {
            alphabets.mkdir();
        }
        File alphabetsFile = new File(alphabets, ALPHABET_FILE);
        if (!alphabetsFile.exists()) {
            if (alphabetsFile.createNewFile()) {
                return alphabetsFile;
            } else {
                throw new IOException("Unable to create File: " + alphabetsFile.getAbsolutePath()); //$NON-NLS-1$
            }
        } else {
            return alphabetsFile;
        }
    }

    @Override
	public void storeAlphabets() throws IOException {
        if (alphaPath == null || new File(alphaPath).canWrite()) {
            AlphabetPersistence.saveAlphabetsToXML(alphabets, new OutputStreamWriter(new FileOutputStream(alphaPath),
                    Charset.forName(IConstants.UTF8_ENCODING)));
        } else {
            throw new IOException("Either no alphabet path was initialized or writing access is denied."); //$NON-NLS-1$
        }
    }

    private void loadAlphabets(File file) throws IOException {
        InputStreamReader isw = new InputStreamReader(new FileInputStream(file),
                Charset.forName(IConstants.UTF8_ENCODING));

        alphabets = new Vector<Alphabet>(new AlphabetPersistence().loadAlphabetsFromXML(isw));

        isw.close();
    }

    public String[] getAlphabetsList() {
        Enumeration<Alphabet> e = alphabets.elements();

        String[] alphaTitles = new String[alphabets.size()];

        for (int i = 0; e.hasMoreElements(); i++) {
            Alphabet alphabet = e.nextElement();
            alphaTitles[i] = alphabet.getName();
        }

        return alphaTitles;
    }

    @Override
	public void updateAlphabet(String alphabetTitle, char[] newCharacterSet) {
        Alphabet modAlpha = getAlphabetByName(alphabetTitle);

        modAlpha.setCharacterSet(newCharacterSet);

        try {
            storeAlphabets();
        } catch (IOException e) {
            LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, "Exception while updating an alphabet", e, true); //$NON-NLS-1$
        }
    }

    @Override
	public String[] getSelfCreatedAlphaList() {
        Vector<String> v = new Vector<String>();
        Enumeration<Alphabet> e = alphabets.elements();
        while (e.hasMoreElements()) {
            Alphabet alphabet = e.nextElement();
            if (!alphabet.isBasic()) {
                v.add(alphabet.getName());
            }
        }
        String[] array = new String[v.size()];
        return v.toArray(array);

    }

    @Override
    public int getSize() {
        return alphabets.size();
    }

}
