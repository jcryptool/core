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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.crypto.classic.vernam.VernamPlugin;
/**
 * The WizardPage for the Vernam cipher
 * 
 * @author Michael Sommer (M1S)
 * @version 0.0.1
 *
 */
public class VernamWizardPage extends WizardPage implements Listener {
	
	private Label lblShowNumberSystem;
	private Label lblKeyLength;
	
	public Button rdBtnEncrypt;
	public Button rdBtnDecrypt;
	
	private Button rdBtnDecimal;
	private Button rdBtnOctal;
	private Button rdBtnBinary;
	private Button rdBtnHexa;
	private Button rdBtnManKeyInput;
	private Button rdBtnKeyInputByFile;
	
	private Button btnKeyInputByHandKeySave;
	private Button btnKeyInputByHandLoadFile;
	
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
	private Text keyInputByHandTextField;
	
	/**
	 * Create the wizard.
	 */
	public VernamWizardPage() {
		super("Vernam Wizard Page");
		setTitle("Vernam Wizard Page");
		setDescription(	"Bitte geben Sie einen Schlüssel für die Verschlüsselung des Textes ein.\n" +
						"Achten Sie darauf, dass die Schlüssellänge gleich der Textlänge sein muss.\n");
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
		
		rdBtnDecimal.setSelection( true );
		lblShowNumberSystem.setText( "0 1 2 3 4 5 6 7 8 9" );
		
		keyInputByHandTextField.setEditable( false );
		btnKeyInputByHandKeySave.setEnabled( false );
		btnKeyInputByHandLoadFile.setEnabled( false );
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
	 * Creates the key input by hand or by file
	 */
	private void createKeyInputByHandGroup( Composite parent )
	{
		Group grpKeyInputByHand = new Group(parent, SWT.NONE);
		grpKeyInputByHand.setText("Schlüsseleingabe");
		grpKeyInputByHand.setBounds(10, 147, 554, 319);
		
		btnKeyInputByHandKeySave = new Button(grpKeyInputByHand, SWT.NONE);
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
		btnKeyInputByHandKeySave.setBounds(10, 113, 534, 25);
		btnKeyInputByHandKeySave.setText("Schlüsseleingabe speichern");
		
		rdBtnManKeyInput = new Button(grpKeyInputByHand, SWT.RADIO);
		rdBtnManKeyInput.setBounds(10, 25, 180, 16);
		rdBtnManKeyInput.addListener(SWT.Selection, this);
		rdBtnManKeyInput.setText("Manuelle Schlüsseleingabe");
		
		rdBtnKeyInputByFile = new Button(grpKeyInputByHand, SWT.RADIO);
		rdBtnKeyInputByFile.setBounds(10, 144, 534, 16);
		rdBtnKeyInputByFile.addListener(SWT.Selection, this);
		rdBtnKeyInputByFile.setText("Schlüsseleingabe per Datei");
		
		keyInputByFileTextField = new Text(grpKeyInputByHand, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		keyInputByFileTextField.setBounds(10, 166, 534, 60);
		keyInputByFileTextField.setEditable(false);
		
		btnKeyInputByHandLoadFile = new Button(grpKeyInputByHand, SWT.NONE);
		btnKeyInputByHandLoadFile.setBounds(10, 232, 534, 25);
		btnKeyInputByHandLoadFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				keyInputByFileTextField.setText( loadKeyFile() );
				checkKeyInput();
			}
		});
		btnKeyInputByHandLoadFile.setText("Schlüsseldatei laden");
		
		keyInputByHandTextField = new Text(grpKeyInputByHand, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		keyInputByHandTextField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) 
			{
				lblKeyLength.setText(""+keyInputByHandTextField.getCharCount());
				checkKeyInput();
			}
		});
		keyInputByHandTextField.addVerifyListener(new VerifyListener() 
		{
			public void verifyText(VerifyEvent e) 
			{
				setErrorMessage(null);
				if (e.character != SWT.BS && e.character != SWT.DEL)
				{
					if( rdBtnDecimal.getSelection() )
					{
						if( !decValues.contains( e.text ) )
						{
							setErrorMessage("Es handelt sich um keine Dezimalzahl bei Ihrer Eingabe: " + e.text);
							e.doit = false;
						}
					}
					if( rdBtnBinary.getSelection() )
					{
						if( !binValues.contains( e.text ) )
						{
							setErrorMessage("Es handelt sich um keine Binärzahl bei Ihrer Eingabe: " + e.text);
							e.doit = false;
						}
					}
					if( rdBtnOctal.getSelection() )
					{
						if( !octValues.contains( e.text ) )
						{
							setErrorMessage("Es handelt sich um keine Oktalzahl bei Ihrer Eingabe: " + e.text);
							e.doit = false;
						}
					}
					if( rdBtnHexa.getSelection() )
					{
						if( !hexValues.contains( e.text ) )
						{
							setErrorMessage("Es handelt sich um keine Hexadezimalzahl bei Ihrer Eingabe: " + e.text);
							e.doit = false;
						}
					}
				}
		}});
		keyInputByHandTextField.setBounds(10, 47, 534, 60);
		
		Label lblNumberSystemInfo = new Label(grpKeyInputByHand, SWT.NONE);
		lblNumberSystemInfo.setBounds(196, 25, 140, 13);
		lblNumberSystemInfo.setText("Inhalt des Zahlensystemes:");
		
		lblShowNumberSystem = new Label(grpKeyInputByHand, SWT.NONE);
		lblShowNumberSystem.setBounds(342, 25, 202, 13);
		
		lblKeyLength = new Label(grpKeyInputByHand, SWT.NONE);
		lblKeyLength.setBounds(379, 296, 165, 13);
		
		Label lblKeyLengthInfo = new Label(grpKeyInputByHand, SWT.NONE);
		lblKeyLengthInfo.setBounds(292, 296, 81, 13);
		lblKeyLengthInfo.setText("Schlüssellänge:");
		
		Label lblTextLengthInfo = new Label(grpKeyInputByHand, SWT.NONE);
		lblTextLengthInfo.setBounds(10, 296, 70, 13);
		lblTextLengthInfo.setText("Textlänge:");
		
		Label textLengthVar = new Label(grpKeyInputByHand, SWT.NONE);
		textLengthVar.setBounds(86, 296, 88, 13);
		textLengthVar.setText(""+lengthOfEditorInput);
	}
	/**
	 *	Event handling for radio buttons in the number system group
	 */
	public void handleEvent(Event event) 
	{
		if( event.widget == rdBtnDecimal )
		{
			lblShowNumberSystem.setText("0 1 2 3 4 5 6 7 8 9");
		}
		if( event.widget == rdBtnOctal )
		{
			lblShowNumberSystem.setText("0 1 2 3 4 5 6 7");
		}
		if( event.widget == rdBtnBinary )
		{
			lblShowNumberSystem.setText("0 1");
		}
		if( event.widget == rdBtnHexa )
		{
			lblShowNumberSystem.setText("0 1 2 3 4 5 6 7 8 9 A B C D E F");
		}
		// enable the key input textfield and save-key-to-file button
		// also it disabled the load-key-from-file button
		if( event.widget == rdBtnManKeyInput )
		{
			keyInputByHandTextField.setEditable( true );
			btnKeyInputByHandKeySave.setEnabled( true );
			btnKeyInputByHandLoadFile.setEnabled( false );
		}
		//
		//
		if( event.widget == rdBtnKeyInputByFile )
		{
			btnKeyInputByHandLoadFile.setEnabled( true );
			keyInputByHandTextField.setEditable( false );
			btnKeyInputByHandKeySave.setEnabled( false );
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
		//
		if( lengthOfEditorInput == keyInputByHandTextField.getCharCount() )
		{
			setPageComplete(true);
		}
		else if( lengthOfEditorInput == keyInputByFileTextField.getCharCount() )
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
		setErrorMessage(null);
		StringBuilder writeInFile = new StringBuilder(keyInputByHandTextField.getText());
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
		setErrorMessage(null);
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
				   // is key length equal text length?
				   int len = sb.toString().length();
				   if( len != lengthOfEditorInput )
				   {
					   setErrorMessage("Die Schlüssellänge muss gleich der Textlänge sein!");
				   }
				   lblKeyLength.setText(""+len);
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
