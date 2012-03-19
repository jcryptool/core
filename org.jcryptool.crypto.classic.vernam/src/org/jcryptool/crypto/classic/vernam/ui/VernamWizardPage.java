//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.vernam.ui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.classic.vernam.VernamPlugin;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
/**
 * The WizardPage for the Vernam cipher
 * 
 * @author Michael Sommer (M1S)
 * @version 0.0.1
 *
 */
public class VernamWizardPage extends WizardPage implements Listener {
	
	public Button rdBtnEncrypt;
	public Button rdBtnDecrypt;
	
	private Button rdBtnDecimal;
	private Button rdBtnOctal;
	private Button rdBtnBinary;
	private Button rdBtnHexa;
	
	private Text keyInputByHandTextField;
	private Text keyInputByFileTextField;
	
	Shell shell;

	// Contains all decimal digits
	private ArrayList<String> decValues = new ArrayList<String>(10);
	// Contains all binary digits
	private ArrayList<String> binValues = new ArrayList<String>(2);
	// Contains all octal digits
	private ArrayList<String> octValues = new ArrayList<String>(8);
	// Contains all hexadecimal digits
	private ArrayList<String> hexValues = new ArrayList<String>(22);
	
	private StringBuffer editorInput = new StringBuffer();
	private final int lengthOfEditorInput = getTextFromEditorAsCharCount();
	private boolean operationInputEncrypt;
	private boolean operationInputDecrypt;
	
	/**
	 * Create the wizard.
	 */
	public VernamWizardPage() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
		setupDecimalValues();
		setupBinaryValues();
		setupOctalValues();
		setupHexadecimalValues();
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(null);
		
		createOperationGroup( container );
		
		createNumberSystemGroup( container );
		
		createKeyInputByHandGroup( container );
		
		createKeyInputByFileGroup( container );
		
		createUserInformation( container );
		
		keyInputByHandTextField.setEditable(false);
		
		setPageComplete( false );
	}
	/**
	 * Creates the operation group
	 */
	private void createOperationGroup( Composite parent )
	{
		Group grpOperation = new Group(parent, SWT.NONE);
		grpOperation.setText("Operation");
		grpOperation.setBounds(10, 10, 554, 50);
		
		rdBtnEncrypt = new Button(grpOperation, SWT.RADIO);
		rdBtnEncrypt.setBounds(10, 24, 90, 16);
		rdBtnEncrypt.addListener( SWT.Selection, this );
		rdBtnEncrypt.setText("Verschlüsseln");
		
		rdBtnDecrypt = new Button(grpOperation, SWT.RADIO);
		rdBtnDecrypt.setBounds(300, 24, 90, 16);
		rdBtnDecrypt.addListener( SWT.Selection, this );
		rdBtnDecrypt.setText("Entschlüsseln");
	}
	/**
	 * Creates the number systems group
	 */
	private void createNumberSystemGroup( Composite parent )
	{
		Group grpNumberSystems = new Group(parent, SWT.NONE);
		grpNumberSystems.setText("Zahlensysteme");
		grpNumberSystems.setBounds(10, 66, 554, 75);
		
		rdBtnDecimal = new Button(grpNumberSystems, SWT.RADIO);
		rdBtnDecimal.setBounds(10, 27, 90, 16);
		rdBtnDecimal.addListener( SWT.Selection, this );
		rdBtnDecimal.setText("Dezimal");
		
		rdBtnOctal = new Button(grpNumberSystems, SWT.RADIO);
		rdBtnOctal.setBounds(10, 49, 90, 16);
		rdBtnOctal.addListener( SWT.Selection, this );;
		rdBtnOctal.setText("Oktal");
		
		rdBtnBinary = new Button(grpNumberSystems, SWT.RADIO);
		rdBtnBinary.setBounds(300, 27, 90, 16);
		rdBtnBinary.addListener( SWT.Selection, this );
		rdBtnBinary.setText("Binär");
		
		rdBtnHexa = new Button(grpNumberSystems, SWT.RADIO);
		rdBtnHexa.setBounds(300, 49, 90, 16);
		rdBtnHexa.addListener( SWT.Selection, this );
		rdBtnHexa.setText("Hexadezimal");
	}
	/**
	 * Creates the key input by hand group
	 */
	private void createKeyInputByHandGroup( Composite parent )
	{
		Group grpKeyInputByHand = new Group(parent, SWT.NONE);
		grpKeyInputByHand.setText("Manuelle Schlüsseleingabe");
		grpKeyInputByHand.setBounds(10, 147, 554, 90);
		
		keyInputByHandTextField = new Text(grpKeyInputByHand, SWT.BORDER);
		keyInputByHandTextField.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) 
			{
				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL)
				{
					if( rdBtnDecimal.getSelection() )
					{
						if( !decValues.contains( e.text ) )
						{
							setErrorMessage("Nicht DEC " + e.text);
							e.doit = false;
						}
					}
					if( rdBtnBinary.getSelection() )
					{
						if( !binValues.contains( e.text ) )
						{
							setErrorMessage("Nicht BIN " + e.text);
							e.doit = false;
						}
					}
					if( rdBtnOctal.getSelection() )
					{
						if( !octValues.contains( e.text ) )
						{
							setErrorMessage("Nicht OCT " + e.text);
							e.doit = false;
						}
					}
					if( rdBtnHexa.getSelection() )
					{
						if( !hexValues.contains( e.text ) )
						{
							setErrorMessage("Nicht HEX " + e.text);
							e.doit = false;
						}
					}
					checkKeyInput();
				}
			}
		});
		keyInputByHandTextField.setBounds(10, 25, 534, 21);
		
		Button btnKeyInputByHandKeySave = new Button(grpKeyInputByHand, SWT.NONE);
		btnKeyInputByHandKeySave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				setErrorMessage(null);
				if( keyInputByHandTextField.getCharCount() == 0 )
				{
					setErrorMessage( "Die Länge der Schlüsseleingabe ist 0!" );
				}
				else
				{
					saveKeyToFile();
				}
			}
		});
		btnKeyInputByHandKeySave.setBounds(10, 52, 534, 25);
		btnKeyInputByHandKeySave.setText("Schlüsseleingabe speichern");
	}
	/**
	 * Creates the key input by file group
	 */
	private void createKeyInputByFileGroup( Composite parent )
	{
		Group grpKeyInputByFile = new Group(parent, SWT.NONE);
		grpKeyInputByFile.setText("Schlüsseleingabe per Datei");
		grpKeyInputByFile.setBounds(10, 243, 554, 90);
		
		keyInputByFileTextField = new Text(grpKeyInputByFile, SWT.BORDER);
		keyInputByFileTextField.setEditable(false);
		keyInputByFileTextField.setBounds(10, 25, 534, 21);
		
		Button btnKeyInputByHandLoadFile = new Button(grpKeyInputByFile, SWT.NONE);
		btnKeyInputByHandLoadFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setErrorMessage( null );
				if( keyInputByHandTextField.getCharCount() >= 1 )
				{
					setErrorMessage("Die Schlüsseleingabe per Datei kann nicht erfolgen, solange" +
									" das Textfeld für die manuelle Eingabe NICHT leer ist.");
				}
				else
				{
					keyInputByFileTextField.setText( loadKeyFile() );
					checkKeyInput();
				}
			}
		});
		btnKeyInputByHandLoadFile.setBounds(10, 52, 534, 25);
		btnKeyInputByHandLoadFile.setText("Schlüsseldatei laden");
	}
	/**
	 * Creates User information
	 */
	private void createUserInformation( Composite parent )
	{
		Group grpHelp = new Group(parent, SWT.NONE);
		grpHelp.setText("Hilfe");
		grpHelp.setBounds(10, 339, 554, 50);
		
		Button btnKeyLength = new Button(grpHelp, SWT.NONE);
		btnKeyLength.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				msgBoxForKeyAndTextLength();
			}
		});
		btnKeyLength.setBounds(10, 15, 534, 25);
		btnKeyLength.setText("Schlüssellänge überprüfen");
	}
	/**
	 *	Event handling for radio buttons in the number system group
	 */
	public void handleEvent(Event event) 
	{
		if( event.widget == rdBtnDecimal )
		{
			rdBtnOctal.setEnabled(false);
			rdBtnBinary.setEnabled(false);
			rdBtnHexa.setEnabled(false);
			keyInputByHandTextField.setEditable(true);
		}
		if( event.widget == rdBtnOctal )
		{
			rdBtnDecimal.setEnabled(false);
			rdBtnBinary.setEnabled(false);
			rdBtnHexa.setEnabled(false);
			keyInputByHandTextField.setEditable(true);
		}
		if( event.widget == rdBtnBinary )
		{
			rdBtnDecimal.setEnabled(false);
			rdBtnOctal.setEnabled(false);
			rdBtnHexa.setEnabled(false);
			keyInputByHandTextField.setEditable(true);
		}
		if( event.widget == rdBtnHexa )
		{
			rdBtnDecimal.setEnabled(false);
			rdBtnOctal.setEnabled(false);
			rdBtnBinary.setEnabled(false);
			keyInputByHandTextField.setEditable(true);
		}
		if( event.widget == rdBtnEncrypt )
		{
			operationInputEncrypt = true;
			operationInputDecrypt = false;
		}
		if( event.widget == rdBtnDecrypt )
		{
			operationInputDecrypt = true;
			operationInputEncrypt = false;
		}
	}
	// own methods
	/**
	 * Method compares, whether key length and editorinput length are equal
	 * If both equal the Page is complete ;-)
	 * 
	 */
	private void checkKeyInput()
	{
		int charCountFromInputByFile = keyInputByFileTextField.getCharCount();
		int charCountFromInputByHand = keyInputByHandTextField.getCharCount();
		if( lengthOfEditorInput == charCountFromInputByHand )
		{
			setPageComplete(true);
		}
		else if( lengthOfEditorInput == charCountFromInputByFile )
		{
			setPageComplete(true);
		}
		else
		{
			setPageComplete(false);
		}
	}
	/**
	 * Method returns the length of the current Editorinput. 
	 * 
	 * @return int (length of the editor input without signs)
	 */
	public int getTextFromEditorAsCharCount()
	{
		InputStream stream = EditorsManager.getInstance().getActiveEditorContentInputStream();
		char[] textInChar = null;
	    String text3 = "";
	    text3 = InputStreamToString(stream);
	    text3 = modifyKeyInput(text3);
	    textInChar = text3.toCharArray();
	    return textInChar.length;
	}
	/**
	 * Method modify the inputtext
	 * all signs f.e Umlaute and so on will be delete
	 * 
	 * @param text (text from textfield or file)
	 * @return String (contains the modified text)
	 */
	private String modifyKeyInput( String text )
	{
		String text2;
        String replaceThis = "Ä";
        String withThis = "AE";
        text2 = text.replaceAll(replaceThis, withThis);
        replaceThis = "ä";
        withThis = "ae";
        text2 = text2.replace(replaceThis, withThis);
        replaceThis = "Ö";
        withThis = "OE";
        text2 = text2.replace(replaceThis, withThis);
        replaceThis = "ö";
        withThis = "oe"; 
        text2 = text2.replace(replaceThis, withThis); 
        replaceThis = "Ü";
        withThis = "UE"; 
        text2 = text2.replace(replaceThis, withThis);
        replaceThis = "ü"; 
        withThis = "ue"; 
        text2 = text2.replace(replaceThis, withThis);
        if (text.toUpperCase().equals(text)) { 
            replaceThis = "ß";
            withThis = "SS";
            text2 = text2.replace(replaceThis, withThis);
        } else if (text2.toUpperCase().equals(text2)) {
            replaceThis = "ß"; 
            withThis = "ss";
            text2 = text2.replace(replaceThis, withThis); 
        }  
        char[] alphabet = {	'0','1','2','3','4','5',
							'6','7','8','9','a','b',
							'c','d','e','f','g','h',
							'i','j','k','l','m','n',
							'o','p','q','r','s','t',
							'u','v','w','x','y','z'
							};
        char[] inputText = text2.toLowerCase().toCharArray();
        for( int i = 0; i < inputText.length; i++ )
        {
        	for( int j = 0; j < alphabet.length; j++ )
        	{
        		if( inputText[i] == alphabet[j] )
        		{
        			editorInput.append(alphabet[j]);
        		}
        	}
        }
        return editorInput.toString();
	}
	/**
	 * Method parses the Editorinput in String and returns this  
	 * 
	 * @param in (active editor window)
	 * @return output (string)
	 */
	private String InputStreamToString(InputStream in) {
    	BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			LogUtil.logError(VernamPlugin.PLUGIN_ID, e1);
		}
		StringBuffer myStrBuf = new StringBuffer();
        String output = "";
        String line = null;
        try {
            while ( (line = reader.readLine()) != null ) {
            	line = line.replaceAll(" ", "");
            	myStrBuf.append( line );
            }
        } catch (IOException e) {
            LogUtil.logError(VernamPlugin.PLUGIN_ID, e);
        }
        output = myStrBuf.toString();
        return output;
    }
	/**
	 * Method saves the input from keyInputByHandTextField in a file,
	 * if the filename exists, a msgbox appears and shows this to the user. 
	 * The user can replace the old file with the new file. It is only one file extension useable 
	 * .txt
	 */
	private void  saveKeyToFile()
	{
		StringBuilder writeInFile = new StringBuilder();
		String fileName = null;
		boolean done = false;
		FileDialog fd = new FileDialog( Display.getCurrent().getActiveShell(), SWT.SAVE );
		fd.setFilterExtensions(new String[] {"*.txt"});
		fd.setText( "Datei speichern" );
		while( !done )
		{
			fileName = fd.open();
			if( fileName == null )
			{
				done = true;
			}
			else
			{
				File file = new File( fileName );
				if( file.exists() )
				{
					MessageBox mb = new MessageBox(fd.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
					mb.setMessage(fileName + " ist bereits vorhanden. Soll sie ersetzt werden?");
					done = mb.open() == SWT.YES;
					// Override file content with keytext input 
					PrintWriter pwr;
					try {
						pwr = new PrintWriter( new FileWriter( fileName ) );
						pwr.print( writeInFile.toString() );
						pwr.close();
					} 
					catch(IOException e) 
					{
						LogUtil.logError(VernamPlugin.PLUGIN_ID, e);
					}
				}
				else
				{
					PrintWriter pwr;
					try {
						pwr = new PrintWriter( new FileWriter( fileName ) );
						pwr.print( writeInFile.toString() );
						pwr.close();
					} 
					catch(IOException e) 
					{
						LogUtil.logError(VernamPlugin.PLUGIN_ID, e);
					}
					
					done = true;
				}
			}
		}
	}
	/**
	 * Loads key from a file
	 * @return String (modified String)
	 */
	private String loadKeyFile()
	{
		FileDialog fd = new FileDialog( Display.getCurrent().getActiveShell(), SWT.OPEN );
		fd.setFilterExtensions(new String[] {"*.txt"});
		fd.setText("Datei öffnen");  
		String strFile = fd.open();
		if (strFile != null)
		{
			try 
			{
				   FileInputStream fis = new FileInputStream( strFile );
				   InputStreamReader isr = new InputStreamReader( fis, Charset.forName( "ISO-8859-1" ) );
				   BufferedReader br = new BufferedReader( isr );
				   StringBuilder sb = new StringBuilder();
				   String line = null;
				   while ( (line=br.readLine()) != null ) 
				   {
					   line = line.toLowerCase();
					   line = line.replaceAll(" ", "");
					   sb.append( line );
				   }
				   br.close();
				   return ( sb.toString() );
			} 
			catch ( final Exception ex ) 
			{
				LogUtil.logError( VernamPlugin.PLUGIN_ID, ex );
			}
		}
		return "";
	}
	/**
	 * Methode erzeugt eine MessageBox, in der die Länge des Textes, 
	 * sowie die Länge des eingegebenen Schlüssel angezeigt wird
	 * 
	 * Method creates a Messagebox, the msgbox shows the Editorinput length and
	 * the key length, from file and by hand
	 * 
	 */
	private void msgBoxForKeyAndTextLength()
	{
		MessageDialog messageDialog = new MessageDialog(shell, "Überprüfen der Schlüssellänge", null,
				"Länge des zuverschlüsselnen Textes: " + lengthOfEditorInput 
				+ "\n" 
				+ "Länge der Schlüsseleingabe " + (keyInputByHandTextField.getCharCount() == 0 ? keyInputByFileTextField.getCharCount() : keyInputByHandTextField.getCharCount()), 
		        MessageDialog.INFORMATION,
		        new String[] { "OK"}, 1);
		if (messageDialog.open() == 1) {}
	}
	/**
	 * 
	 */
	public String getMyText() 
	{
		return editorInput.toString();
	}
	/**
	 * 
	 */
	public String getKey()
	{
		if( keyInputByHandTextField.getText() == null )
		{
			return keyInputByFileTextField.getText();
		}
		else
		{
			return keyInputByHandTextField.getText();
		}
	}
	/**
	 * 
	 */
	public boolean getEncrypt()
	{
		return operationInputEncrypt;
	}
	/**
	 * 
	 */
	public boolean getDecrypt()
	{
		return operationInputDecrypt;
	}
	/**
	 * Sets up an ArrayList with decimal values.
	 */
	private void setupDecimalValues() {
		decValues.add(0, "0"); decValues.add(1, "1"); decValues.add(2, "2"); decValues.add(3, "3");
		decValues.add(4, "4"); decValues.add(5, "5"); decValues.add(6, "6"); decValues.add(7, "7");
		decValues.add(8, "8"); decValues.add(9, "9"); decValues.add(10, "10");
	}
	/**
	 * Sets up an ArrayList with binary values.
	 */
	private void setupBinaryValues() {
		binValues.add(0, "0"); binValues.add(1, "1");
	}
	/**
	 * Sets up an ArrayList with octal values.
	 */
	private void setupOctalValues() {
		octValues.add(0, "0"); octValues.add(1, "1"); octValues.add(2, "2"); octValues.add(3, "3");
		octValues.add(4, "4"); octValues.add(5, "5"); octValues.add(6, "6"); octValues.add(7, "7");
	}
	/**
	 * Sets up an ArrayList with hexadecimal values.
	 */
	private void setupHexadecimalValues() {
		hexValues.add(0, "0"); hexValues.add(1, "1"); hexValues.add(2, "2"); hexValues.add(3, "3");
		hexValues.add(4, "4"); hexValues.add(5, "5"); hexValues.add(6, "6"); hexValues.add(7, "7");
		hexValues.add(8, "8"); hexValues.add(9, "9"); hexValues.add(10, "A"); hexValues.add(11, "B");
		hexValues.add(12, "C"); hexValues.add(13, "D"); hexValues.add(14, "E"); hexValues.add(15, "F");
		hexValues.add(10, "a"); hexValues.add(11, "b");	hexValues.add(12, "c"); hexValues.add(13, "d"); 
		hexValues.add(14, "e"); hexValues.add(15, "f");
	}
}
