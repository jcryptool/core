package org.jcryptool.analysis.fleissner.UI;


import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.analysis.fleissner.key.Grille;
import org.jcryptool.analysis.fleissner.key.KeySchablone;
import org.jcryptool.analysis.fleissner.logic.InvalidParameterCombinationException;
import org.jcryptool.analysis.fleissner.logic.MethodApplication;
import org.jcryptool.analysis.fleissner.logic.ParameterSettings;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;


public class FleissnerWindow extends Composite{
    
//    private ArrayList<int[]> keyArgs;
    
    private String fileName;
    private Grille model;
    private Composite headerComposite;
    private Composite mainComposite;
    private Composite method;
    private Composite text;
    private Composite process;
    private Group key;
    private Group inOutText;
    private Group plaintextComposite;
    private Group ciphertextComposite;
    private Group analysis;
    private Group textSelectionGroup;
    private Group analysisSettingsGroup;
    private Group methodComposite;
    private Canvas canvasKey;
    private KeyListener keyListener;
    private Text plaintext;
    private Text ciphertext;
    private Text analysisOutput;
    private Text chooseLanguage;
    private Label statisticNameIdentifier;
    private Text loadedStatisticName;
    private Label textNameIdentifier;
    private Text loadedTextName;
    private Spinner keySize;
    private Spinner restarts;
    private Spinner nGramSize;
    private Button analyze;
    private Button encrypt;
    private Button decrypt;
    private Button writeText;
    private Button statistics;
    private Button start;
    private Button statisticsLoad;
    private Button loadStatistics;
    private Button exampleText;
    private Button loadOwntext;
    private Button loadText;
    private Button deleteHoles;
    private Button randomKey;
    private Combo language;
    private Combo chooseExample;
    private Combo selectStatistic;
    private Label numberOfRestarts;
    private Label chooseNGramSize;
    private boolean plain = false;
    private boolean userKey = false;
    private boolean userText = false;
    private boolean userStatistics = false;

    private boolean startSettings = true;
    private Hashtable<Integer, Integer> htRestarts = new Hashtable<Integer, Integer>();
    
    private int[] keyToLogic = null;
    private String argMethod = null;
    private String argText = "";
    private String argKey = null;
    private double[] argStatistics = null;
    private String argLanguage = null;
    private String[] args;
    private String textName;
    private String statisticName;
    private String oldNgramSize = null;
    private FileInputStream fis;
    private FileInputStream fisOld = null;
    private LoadFiles lf = new LoadFiles();


    public FleissnerWindow(Composite parent, int style) {
        super(parent, style);
       
        model = new Grille();
        model.setKey(new KeySchablone(7));
        
        mainComposite = new Composite(parent,  SWT.NONE);
        mainComposite.setLayout(new GridLayout(3, false));
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        createHeader(mainComposite);
        createMethod(mainComposite);
        createKey(mainComposite);
        createInOutText(mainComposite);
        Composite platzHalter = new Composite(mainComposite, SWT.NONE);
        platzHalter.setLayout(new GridLayout());
        platzHalter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,1));
        createText(mainComposite);
        createAnalysisOutput(mainComposite);
        
        startSettings = false;
        reset();
       
    }
  
    private void createMethod(Composite parent) {
        
        methodComposite = new Group(parent, SWT.NONE);
        methodComposite.setLayout(new GridLayout());
        methodComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        methodComposite.setText("Methode");
        
        process = new Composite(methodComposite, SWT.NONE);
        process.setLayout(new GridLayout());
        process.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false/*, 1, 2*/));
        
        method = new Composite(methodComposite, SWT.NONE);
        method.setLayout(new GridLayout());
        method.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false/*, 1, 2*/));
      
        analyze = new Button(method, SWT.RADIO);
        analyze.setText("Analyse");
        analyze.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                if (!startSettings) {
                    if (analyze.getSelection()) {
                        System.out.println("Analyze selected. Method 'analyze' will be started.");
                        analyze();
                        checkArgs("analyze Selection Listener");
                        reset();
                    }  
                }
            }
        });
        analyze.setSelection(true);
        argMethod = "analyze";
        
        encrypt = new Button(method, SWT.RADIO);
        encrypt.setText("Verschlüsselung");
        encrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                if (encrypt.getSelection()) {
                    encrypt();
                    checkArgs("Encrypt Selection Listener");
                    reset();
                }     
            }
        });
        
        decrypt = new Button(method, SWT.RADIO);
        decrypt.setText("Entschlüsselung");
        decrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                if (decrypt.getSelection()) {
                    decrypt();
                    checkArgs("decrypt Selection Listener");
                    reset();
                }   
            }
        });
        
        GridData methods = new GridData(SWT.FILL, SWT.TOP, true, true);
        analyze.setLayoutData(methods);
        encrypt.setLayoutData(methods);
        decrypt.setLayoutData(methods);
      
        start = new Button(process, SWT.PUSH);
        start.setText("Start");
        start.setEnabled(true);
        start.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
                
            }
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                checkArgs("start Selection Listener");
                startMethod();
            }
        });
        
        Button example = new Button(process, SWT.PUSH);
        example.setText("Beispielanalyse");
        example.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
            
            @Override
            public void widgetSelected(SelectionEvent e) {   
                
                exampleAnalysis();
            }   
        });
        
        Button reset = new Button(process, SWT.PUSH);
        reset.setText("Neustart");   
        reset.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                restart();
                reset();
            }
        });
        
        GridData startOptions = new GridData(SWT.FILL, SWT.TOP, true, true);
        start.setLayoutData(startOptions);
        example.setLayoutData(startOptions);
        reset.setLayoutData(startOptions);
    }
    
    private void createKey(Composite parent) {
        
        key = new Group(parent, SWT.NONE);
        key.setLayout(new GridLayout(3, false));
        key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        key.setText("Schlüssel");

        canvasKey = new Canvas(key, SWT.DOUBLE_BUFFERED);
        canvasKey.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        canvasKey.addPaintListener(new KeyPainter(canvasKey, model));
        keyListener = new org.jcryptool.analysis.fleissner.UI.KeyListener(model, this);
        canvasKey.addMouseListener(keyListener);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 4);
        gridData.widthHint = 201;
        gridData.heightHint = 201;
        canvasKey.setLayoutData(gridData);
        canvasKey.setEnabled(false);

        Label spinner = new Label(key, SWT.NONE);
        spinner.setText("Schlüssellänge"); //$NON-NLS-1$
//        spinner.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        GridData gd_spinner = new GridData(SWT.FILL, SWT.TOP, false, true);
        gd_spinner.horizontalIndent = 20;
        spinner.setLayoutData(gd_spinner);

        keySize = new Spinner(key, SWT.NONE);
        keySize.setMinimum(2);
        keySize.setMaximum(20);
        keySize.setIncrement(1);
        keySize.setSelection(7);
        keySize.setEnabled(true);
        GridData gd_keySize = new GridData(SWT.LEFT, SWT.TOP, false, true);
        gd_keySize.horizontalIndent = 20;
        keySize.setLayoutData(gd_keySize); 
        keySize.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {  
                if (Integer.parseInt(keySize.getText()) > 20 || Integer.parseInt(keySize.getText()) < 2)
                    keySize.setSelection(7);
                model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
                reset();
                canvasKey.removeMouseListener(keyListener);
                canvasKey.addMouseListener(keyListener);
                if (exampleText.getSelection() && !plain)
                    refreshTexts();
            }
        });
        
        randomKey = new Button(key, SWT.PUSH);
        
        GridData gd_setHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false,2,1);
        gd_setHoles.horizontalIndent = 20;
        randomKey.setLayoutData(gd_setHoles);
        randomKey.setEnabled(false);
        randomKey.setText("Zufälliger Schlüssel");
        randomKey.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {

                generateRandomKey();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e); 
            }
        });
        
        deleteHoles = new Button(key, SWT.PUSH);
        GridData gd_deleteHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false,2,1);
        gd_deleteHoles.horizontalIndent = 20;
        deleteHoles.setLayoutData(gd_deleteHoles);
        deleteHoles.setEnabled(false);
        deleteHoles.setText("Schlüssel zurücksetzen"); //$NON-NLS-1$
        deleteHoles.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
               
                deleteHoles();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }
   
    private void createInOutText(Composite parent) {
        
        inOutText = new Group(parent, SWT.NONE);
        inOutText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 6));
        // Composite should have no border, so it looks like the groups on the left.
        GridLayout gl_inOutText = new GridLayout();
        inOutText.setLayout(gl_inOutText); 
        
        createPlaintext(inOutText);
        
        createCiphertext(inOutText);
        
        reset(); /*notwendig?*/
    }
    
    
    private void createPlaintext(Composite parent) {
        
        plaintextComposite = new Group(parent,  SWT.NONE);
        plaintextComposite.setLayout(new GridLayout());
        plaintextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
        plaintextComposite.setText("Klartext" + "(0)");
     
        plaintext = new Text(plaintextComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = plaintextComposite.getSize().y; 
        plaintext.setLayoutData(gridData);
        plaintext.setBackground(ColorService.WHITE);
//        plaintext.setEditable(false);
        plaintext.setEnabled(false);
        plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyPressed(KeyEvent e) {
//                e.doit = false;
                e.doit = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        plaintext.addModifyListener(new ModifyListener() {
            
            @Override
            public void modifyText(ModifyEvent e) {
                plaintextComposite.setText("Klartext (" + plaintext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                
                if (!startSettings) {
                    if (writeText.getSelection())
                        argText =plaintext.getText();
//                    writeTextToArgText(plain);
                }
                reset();
            }
                
        });
    }
    
    private void createCiphertext(Composite parent) {
        
        ciphertextComposite = new Group(parent,  SWT.NONE);
        ciphertextComposite.setLayout(new GridLayout());
        ciphertextComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 3));
        ciphertextComposite.setText("Geheimtext" + " (0)");
        
        ciphertext = new Text(ciphertextComposite,  SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = ciphertextComposite.getSize().y; 
        ciphertext.setLayoutData(gridData);
        ciphertext.setEnabled(true);
        ciphertext.setEditable(false);
        ciphertext.setBackground(ColorService.WHITE);
        textName = "files/dawkinsGerCiphertext7.txt";
        argText = lf.InputStreamToString(lf.openMyTestStream(textName));
        resetTexts();

        ciphertext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyPressed(KeyEvent e) {

                e.doit = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        ciphertext.addModifyListener(new ModifyListener() {
            
            @Override
            public void modifyText(ModifyEvent e) {
                ciphertextComposite.setText("Geheimtext (" + ciphertext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                if (!startSettings) {
                    if (writeText.getSelection())
                        argText =ciphertext.getText();
//                    writeTextToArgText(plain);
                }
                reset();
            }
        });
        ciphertextComposite.setText("Geheimtext (" + ciphertext.getText().length() + ")");
    }
    
    private void createText(Composite parent) {
        
        textSelectionGroup = new Group(parent, SWT.NONE);
        textSelectionGroup.setLayout(new GridLayout(4, false));
        textSelectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        textSelectionGroup.setText("Textauswahl");
        
        Composite platzHalter = new Composite(parent, SWT.NONE);
        platzHalter.setLayout(new GridLayout());
        platzHalter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,1));
        
        analysisSettingsGroup = new Group(parent, SWT.NONE);
        analysisSettingsGroup.setLayout(new GridLayout(2, false));
        analysisSettingsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        analysisSettingsGroup.setText("Analyseeinstellungen");
        analysisSettingsGroup.setEnabled(true);
      
        createLoadtextComposite(textSelectionGroup);
        
        String[] items = {"Deutsch", "Englisch"};
      
        Composite languageAndRestarts = new Composite(analysisSettingsGroup, SWT.NONE);
        languageAndRestarts.setLayout(new GridLayout(4, false));
        languageAndRestarts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        Group statisticGroup = new Group(analysisSettingsGroup, SWT.NONE);
        statisticGroup.setLayout(new GridLayout(4,false));
        statisticGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
        statisticGroup.setText("Statistikauswahl");
        
        chooseLanguage = new Text(languageAndRestarts, SWT.WRAP | SWT.MULTI);
        chooseLanguage.setText("Sprache");
        chooseLanguage.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        chooseLanguage.setBackground(ColorService.LIGHTGRAY);

        // Create a dropdown Combo
        language = new Combo(languageAndRestarts, SWT.DROP_DOWN);
        language.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        language.setItems(items);
        language.select(0);
        argLanguage = "german";
        language.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if ((language.getSelectionIndex()==0) && (nGramSize.getSelection()==4)) {
                    selectStatistic.select(0);
                    chooseExample.select(0);
                }
                else if ((language.getSelectionIndex()==0) && (nGramSize.getSelection()==3)){
                    selectStatistic.select(1);
                    chooseExample.select(0);
                }
                else if((language.getSelectionIndex()==1) && (nGramSize.getSelection()==4)) {
                    selectStatistic.select(2);
                    chooseExample.select(2);
                }
                else if((language.getSelectionIndex()==1) && (nGramSize.getSelection()==3)) {
                    selectStatistic.select(3);
                    chooseExample.select(2);
                }  
                if (statistics.getSelection()) {
                    resetStatistics();
                }
                if (exampleText.getSelection()) {
                    refreshTexts();
                }
                languageSelection();
                checkArgs("language Selection Listener");
            }
        });
      
        numberOfRestarts = new Label(languageAndRestarts, SWT.NONE);
        numberOfRestarts.setText("Restarts"); //$NON-NLS-1$
        numberOfRestarts.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
        
        restarts = new Spinner(languageAndRestarts, SWT.NONE);
        restarts.setMinimum(1);
        restarts.setMaximum(1400);
        restarts.setIncrement(1);
        restarts.setSelection(5);
        restarts.setEnabled(true);
        restarts.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
      
        createLoadstatisticsComposite(statisticGroup);
  }
    


    /**
     * Hier findet das laden einer Datei statt.
     * @param firstGroup
     */
    private void createLoadtextComposite(Composite thirdGroup) {
        // TODO Auto-generated method stub
        String[] items = {"Richard Dawkins - Der Gotteswahn", "Wikipedia - Frühchristliche Kunst", "Richard Dawkins - The God Delusion", "Wikipedia - Visual Arts"};
        
        exampleText = new Button(thirdGroup, SWT.RADIO);
        exampleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        exampleText.setText("Beispieltext");
        exampleText.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (exampleText.getSelection()) {
                    textNameIdentifier.setVisible(false);
                    loadedTextName.setText("");
                    loadedTextName.setVisible(false);
                    
                    analyze.setEnabled(true);
                    userText = false;
                    if (encrypt.getSelection()) {
                        userKey = true;
                    }else {
                        userKey = false;
                    }
                    
                    System.out.println("UserText (in exampleText): "+String.valueOf(userText));
                    textSelection(false, true, false, false);
                    refreshTexts();
                    if (analyze.getSelection()) {
                        deleteHoles();
                    }
                    checkArgs("Example Text Selection Listener");
                }
            }
        });
        exampleText.setSelection(true);

        // Create a dropdown Combo
        chooseExample = new Combo(thirdGroup, SWT.DROP_DOWN);
        chooseExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        chooseExample.setItems(items);
        chooseExample.select(0);
        
        chooseExample.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                userText = false;
                if (!plain) {
                    userKey = false;
                }
                refreshTexts();
                
                if (chooseExample.getSelectionIndex() == 0 || chooseExample.getSelectionIndex() == 1) {
                    language.select(0);
                    
                }
                else {
                    language.select(1);
                }
                languageSelection();
                checkArgs("chooseExample SelectionListener");
            }
        });

        loadOwntext = new Button(thirdGroup, SWT.RADIO);
        loadOwntext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        loadOwntext.setText("Eigener Text");
        loadOwntext.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (loadOwntext.getSelection()) {
                    analyze.setEnabled(true);
                    plaintext.setText("");
                    ciphertext.setText("");
                    userText = true;
                    userKey = true;
                    argText = "";
                    textName = "";
                    System.out.println("UserText (in loadOwnText): "+String.valueOf(userText));
                    textSelection(true, false, false, false);
                    
                    checkArgs("loadOwnText SelectionListener");
                }
            }
        });
       
        loadText = new Button(thirdGroup, SWT.PUSH);
        loadText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        loadText.setText("Text laden");
        loadText.setEnabled(false);
        loadText.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                String filename = openFileDialog(SWT.OPEN);
                String[] a = filename.split("\\\\");
                textName = filename;
                System.out.println("Dialog opened. File: "+filename+" choosen");
                argText = loadNormal(filename);
                userText = true;
                userKey = true;
                
                textNameIdentifier.setVisible(true);
                loadedTextName.setText(a[a.length-1]);
                loadedTextName.setVisible(true);
                
                resetTexts();
                checkArgs("loadtext SelectionListener");
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                widgetSelected(e);
                
            }
        }); 
        
        
        textNameIdentifier = new Label(thirdGroup, SWT.NONE);
        textNameIdentifier.setText("Geladener Text: "); //$NON-NLS-1$
        textNameIdentifier.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
        textNameIdentifier.setVisible(false);
        
        loadedTextName = new Text(thirdGroup, /*SWT.WRAP | SWT.MULTI*/ SWT.NONE);
        loadedTextName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        loadedTextName.setBackground(ColorService.LIGHTGRAY);
        loadedTextName.setVisible(false);
       
        writeText = new Button(thirdGroup, SWT.RADIO);
        writeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        writeText.setEnabled(false);
        writeText.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (writeText.getSelection()) {
                    textNameIdentifier.setVisible(false);
                    loadedTextName.setText("");
                    loadedTextName.setVisible(false);
                    
                    analyze.setEnabled(false);
                    boolean editPlaintext = false;
                    boolean editCiphertext = false;
                    userText = true;
                    userKey = true;
                    System.out.println("UserText (in writeText): "+String.valueOf(userText));
                    
                    if (encrypt.getSelection()) {
                        editPlaintext = true;
                        editCiphertext = false;
                        plaintext.setText("");
                    }else if(decrypt.getSelection()) {
                        editPlaintext = false;
                        editCiphertext = true;
                        ciphertext.setText("");
                    }
                    textSelection(false, false, editPlaintext, editCiphertext);
                    checkArgs("writeText SelectionListener");
                }   
            }
        });
        
    
    }
    
    private void createLoadstatisticsComposite(Group thirdGroup) {
        // TODO Auto-generated method stub    
        String[] items = { "de-4-gram-nocs.bin", "de-3-gram-nocs.bin", "en-4-gram-nocs.bin", "en-3-gram-nocs.bin"};
        
        statistics = new Button(thirdGroup, SWT.RADIO);
        statistics.setSelection(true);
        statistics.setText("Sprachstatistik");
        statistics.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (statistics.getSelection()) {
                    statisticNameIdentifier.setVisible(false);
                    loadedStatisticName.setVisible(false);
                    userStatistics = false;
                    resetStatistics();
                    statisticSelection();
                    checkArgs("statistics SelectionListener");
                }
            }
        });


        // Create a dropdown Combo
         selectStatistic = new Combo(thirdGroup, SWT.DROP_DOWN);
         selectStatistic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
         selectStatistic.setItems(items);
         selectStatistic.select(0);
         statisticName = lf.statisticFiles(selectStatistic.getSelectionIndex());
         fis = lf.openMyFileStream(statisticName);
         selectStatistic.addSelectionListener(new SelectionListener() {
             public void widgetDefaultSelected(SelectionEvent e) {
                 widgetSelected(e);
             }

             public void widgetSelected(SelectionEvent e) {
                 
                 if ((selectStatistic.getSelectionIndex()==0) || (selectStatistic.getSelectionIndex()==2)) {
                     nGramSize.setSelection(4);
                 }
                 else {
                     nGramSize.setSelection(3);
                 }
                 if (statistics.getSelection()) {
                     resetStatistics();
                 }
                 
                 languageSelection();
                 checkArgs("selectStatistic SelectionListener");
             }
         });

         statisticsLoad = new Button(thirdGroup, SWT.RADIO);
         statisticsLoad.setText("Eigene Statistik");
         statisticsLoad.addSelectionListener(new SelectionListener() {
             public void widgetDefaultSelected(SelectionEvent e) {
                 widgetSelected(e);
             }

             public void widgetSelected(SelectionEvent e) {
                 
                 if (statisticsLoad.getSelection()) {
                     fis = null;
                     userStatistics = true;
                     statisticSelection();
                     checkArgs("statisticsLoad SelectionListener");
                     reset();
                 }
             }
         });
        
         loadStatistics = new Button(thirdGroup, SWT.PUSH);
         loadStatistics.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
         loadStatistics.setText("Statistik laden");     
         loadStatistics.setEnabled(false);
         loadStatistics.addSelectionListener(new SelectionListener() {
             
             @Override
             public void widgetSelected(SelectionEvent e) {
                 // TODO Auto-generated method stub
                 String filename = openStatFileDialog(SWT.OPEN);
                 try {
                    fis = new FileInputStream(filename);
                    String[] a = filename.split("\\\\");
                    statisticName = filename;
//                    loadedStatisticName.setText(filename);
                    loadedStatisticName.setText(a[a.length-1]);
                    loadedStatisticName.setVisible(true);
                    statisticNameIdentifier.setVisible(true);
                    userStatistics = true;
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                 checkArgs("loadStatistics SelectionListener");
                 reset();
             }
             
             @Override
             public void widgetDefaultSelected(SelectionEvent e) {
                 // TODO Auto-generated method stub
                 
             }
         });    
        
         chooseNGramSize = new Label(thirdGroup, SWT.NONE);
         chooseNGramSize.setText("Größe nGram"); //$NON-NLS-1$
         chooseNGramSize.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
         
         nGramSize = new Spinner(thirdGroup, SWT.NONE);
         nGramSize.setMinimum(3);
         nGramSize.setMaximum(4);
         nGramSize.setIncrement(1);
         nGramSize.setSelection(4);
         nGramSize.setEnabled(false);
         nGramSize.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
         
         statisticNameIdentifier = new Label(thirdGroup, SWT.NONE);
         statisticNameIdentifier.setText("Geladene Statistik: "); //$NON-NLS-1$
         statisticNameIdentifier.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
         statisticNameIdentifier.setVisible(false);
         
         loadedStatisticName = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
         loadedStatisticName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
         loadedStatisticName.setBackground(ColorService.LIGHTGRAY);
         loadedStatisticName.setVisible(false);
    }
    
    private void createAnalysisOutput(Composite parent) {
        
        analysis = new Group(parent,  SWT.NONE);
        analysis.setLayout(new GridLayout());
        analysis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        analysis.setText("Ausgabe Analyse");
    
        analysisOutput = new Text(analysis, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        analysisOutput.setLayoutData(gridData);
        analysisOutput.setText("Gefundene Schablone/ \nFortschritt");
        analysisOutput.setEditable(false);
        analysisOutput.setBackground(ColorService.WHITE);
        
 }
    
    /**
     *  Suchen einer Datei
     * @param type
     * @return
     */
    private String openFileDialog(int type) {
        FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
        dialog.setFilterNames(new String[] { "Text Files (*.txt)" }); //$NON-NLS-1$
        dialog.setOverwrite(true);
        return dialog.open();
}
    
    private String/*File*/ openStatFileDialog(int type/*Component type*/) {
        FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { "*.bin" }); //$NON-NLS-1$
        dialog.setFilterNames(new String[] { "Binary Files (*.bin)" }); //$NON-NLS-1$
        dialog.setOverwrite(true);
        return dialog.open();
}
    
    private String loadNormal(String fileName) {
        
        System.out.println("loadNormalFile geöffnet");
        BufferedReader reader = null;
        String text = "";
        System.out.println("Buffered reader geöffnet und 'null' gesetzt");
        
        try {
            System.out.println("try-Block eröffnet");
//            InputStream is = new InputStream(fileName);
//            reader = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(fileName), Charset.forName("Cp1252")));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), IConstants.UTF8_ENCODING));
            System.out.println("new Buffered Reader von "+fileName+" gesetzt");
            String line = reader.readLine();
            System.out.println("Erste Zeile ausgelesen als 'line': "+line);
            

            int count = 1;
            while (line  != null) {
                text += line;
                System.out.println(count+". Zeile in 'text' geschrieben (in reader while schleife): "+text);
                count++;
                line = reader.readLine();
                
            }
        } catch (NumberFormatException nfe) {
            LogUtil.logError(Activator.PLUGIN_ID, nfe);
            MessageBox brokenFile = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
            brokenFile.setText("Loading puzzle encountered a problem"); //$NON-NLS-1$
            brokenFile.setMessage("Puzzle could not be loaded. There is a wrong character in the loaded file.\n"); //$NON-NLS-1$
            brokenFile.open();
           
        } catch (FileNotFoundException e) {
            return "text could not be loaded";
            //Error handling

        } catch (IOException e) {
            
            return "text could not be loaded";
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                LogUtil.logError(Activator.PLUGIN_ID, e);
            }
        }

        return text;//das array zurückgebe;
}
    

    /**
     * Creates the header and short description.
     * @param parent
     */
    private void createHeader(Composite parent) {
        // TODO Auto-generated method stub
        headerComposite = new Composite(parent, SWT.NONE);
        headerComposite.setBackground(ColorService.WHITE);
        headerComposite.setLayout(new GridLayout());
        headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        
        Text headerText = new Text(headerComposite, SWT.NONE);
        headerText.setText("Fleißner-Analyse");
        headerText.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        headerText.setFont(FontService.getHugeBoldFont());
        headerText.setEditable(false);
        headerText.setBackground(ColorService.WHITE);

        
        Text descriptionText = new Text(headerComposite, SWT.WRAP | SWT.MULTI);
        descriptionText.setText("Hier kommt eine kurze Beschreibung hin.");
        descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        descriptionText.setEditable(false);
        descriptionText.setBackground(ColorService.WHITE);
    }
    
    public void textSelection(boolean enableLoadText, boolean enableChooseExample, boolean editPlaintext, boolean editCiphertext) {
        
        System.out.println("textSelection-Method started\n");
        reset();
        
        loadText.setEnabled(enableLoadText);
        chooseExample.setEnabled(enableChooseExample);
        plaintext.setEditable(editPlaintext);
        ciphertext.setEditable(editCiphertext);
    }
    
    public void statisticSelection() {
        
        System.out.println("statisticSelection-Method started\n");
        
        reset();
        
        if (!userStatistics) {
            selectStatistic.setEnabled(true);
            loadStatistics.setEnabled(false);
            nGramSize.setEnabled(false);  
            nGramSize.setSelection(4);
            
        }
        else {
            selectStatistic.setEnabled(false);
            loadStatistics.setEnabled(true);
            nGramSize.setEnabled(true);  
        }
    }
    
    public void languageSelection() {
        
        System.out.println("languageSelection-Method started\n");
        
        reset();
        
        if (language.getSelectionIndex()==0) {
            argLanguage = "german";
        }
        else {
            argLanguage = "english";
        }
    }
    
    public void analyze() {
        
        plain = false;
        argMethod = "analyze";
        System.out.println("Methode: 'Analyse' (analyze()) gestartet\n");
        System.out.println("userText (in analyze()): "+String.valueOf(userText));
        if (startSettings) {
            argText = lf.InputStreamToString(lf.openMyTestStream("files/dawkinsGerCiphertext7.txt"));
        }else if (userText) {

            argText = ciphertext.getText();
        }
        //      encrypt text for analysis, if ciphertext was not generated by User
        else {
            textName = lf.textFiles(chooseExample.getSelectionIndex());
            argText = lf.InputStreamToString(lf.openMyTestStream(textName));
            randomEncryption();
            userKey = false;
        }
        resetTexts();
        deleteHoles(); /*Bei Texten aus vorheriger Verschlüsselung evtl. Schlüssel behalten???*/
        reset();
        
//      key settings
      canvasKey.setEnabled(false);
      randomKey.setEnabled(false);
      deleteHoles.setEnabled(false);
      
//      text settings
      writeText.setEnabled(false);
      
//      analysis settings
      analysis.setEnabled(true);
      analysisSettingsGroup.setEnabled(true);
      analysisOutput.setEnabled(true);
      
      chooseLanguage.setEnabled(true);
      language.setEnabled(true);
      languageSelection();
      
      restarts.setEnabled(true);
      statistics.setEnabled(true);
      statisticsLoad.setEnabled(true);
      
      if (statistics.getSelection()) {
          
          nGramSize.setEnabled(false);
          selectStatistic.setEnabled(true);
          loadStatistics.setEnabled(false);
      }else {
          nGramSize.setEnabled(true);
          selectStatistic.setEnabled(false);
          loadStatistics.setEnabled(true);
      }

      
//      if (!userStatistics) {
//          argStatistics = lf.statisticFiles(selectStatistic.getSelectionIndex());
//      }
      


      plaintext.setEnabled(false);
      ciphertext.setEnabled(true);
    }
    
    public void encrypt() {
        
        System.out.println("encrypt()-Method entered");
        plain = true;
        argMethod = "encrypt";
        deleteHoles();
        System.out.println("userText (in encrypt()): "+String.valueOf(userText));
        
        if (userText) {
            argText = plaintext.getText();
        }
        else {
            refreshTexts();
        }
        
        reset();
        resetTexts();
        
//        key settings
        canvasKey.setEnabled(true);
        randomKey.setEnabled(true);
        deleteHoles.setEnabled(true);
       
//        text settings
        writeText.setEnabled(true);
        writeText.setText("Klartext manuell eingeben");
        
        plaintext.setEnabled(true);
        ciphertext.setEnabled(false);
        
//        analysis settings
        analysisSettingsGroup.setEnabled(false);
        analysis.setEnabled(false);
        analysisOutput.setEnabled(false);

        statistics.setEnabled(false);
        selectStatistic.setEnabled(false);
        statisticsLoad.setEnabled(false);
        loadStatistics.setEnabled(false);
        loadedStatisticName.setVisible(false);
        statisticNameIdentifier.setVisible(false);
        userStatistics = false;
        fis = null;
        
        language.setEnabled(false);
        restarts.setEnabled(false);
        nGramSize.setEnabled(false);   
    }
    
    public void decrypt() {
        
        System.out.println("Decrypt()-Method entered");
        plain = false;
        argMethod = "decrypt";
        System.out.println("userText (in decrypt()): "+String.valueOf(userText));
        
        if (userText) {
            argText = ciphertext.getText();
            plaintext.setText("");
        }
        else {
            textName = lf.textFiles(chooseExample.getSelectionIndex());
            argText = lf.InputStreamToString(lf.openMyTestStream(textName));
            
//          encrypt text
            randomEncryption();
        }
        
        reset();
        resetTexts();
        
//        key settings
        canvasKey.setEnabled(true);
        randomKey.setEnabled(true);
        deleteHoles.setEnabled(true);
       
//        text settings
        writeText.setEnabled(true);
        writeText.setText("Geheimtext manuell eingeben");
        
        plaintext.setEnabled(false);
        ciphertext.setEnabled(true);
        
//        analysis settings
        analysisSettingsGroup.setEnabled(false);
        analysis.setEnabled(false);
        analysisOutput.setEnabled(false);

        statistics.setEnabled(false);
        selectStatistic.setEnabled(false);
        statisticsLoad.setEnabled(false);
        loadStatistics.setEnabled(false);
        loadedStatisticName.setVisible(false);
        statisticNameIdentifier.setVisible(false);
        userStatistics = false;
        fis = null;
        
        language.setEnabled(false);
        restarts.setEnabled(false);
        nGramSize.setEnabled(false); 

    }
    
    public void startMethod() {
        
        String keyToString;
        System.out.println("startMethod-Method started\n");
        System.out.println("Method (in startMethod): "+argMethod);
        if (argMethod != null) {
            switch (argMethod) {
            
            case "analyze":
                System.out.println("Analyze selected?: "+String.valueOf(analyze.getSelection()));
//                if (analyze.getSelection() && exampleText.getSelection()) {
//                    userText = false;
//                    System.out.println("userText changed in case analyze in startMethod");
//                }
                args = new String[12];
                args[0] = "-method";
                args[1] = "analyze";
                args[2] = "-keyLength";
                args[3] = keySize.getText();
                args[4] = "-cryptedText";
                args[5] = argText;
                args[6] = "-nGramSize";
                args[7] = nGramSize.getText();
                args[8] = "-language";
                args[9] = argLanguage;
                args[10] = "-restarts";
                args[11] = restarts.getText();
                
//                fis = lf.openMyFileStream(lf.statisticFiles(selectStatistic.getSelectionIndex()));
                
                if (fisOld != fis || !oldNgramSize.equals(nGramSize.getText())) {
                    try {
                        argStatistics = lf.loadBinNgramFrequencies(fis, argLanguage, Integer.parseInt(nGramSize.getText()));
                        System.out.println("\nStatistik geladen in startMethod(analyze): "+String.valueOf(fis)+"\n");
                        analysisOutput.append("\nStatistik geladen in startMethod(analyze): "+String.valueOf(fis)+"\n");
                        fisOld = fis;
                        oldNgramSize = nGramSize.getText();
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
                System.out.println("start widget for analysis");
                analysisOutput.append("\nstart widget for analysis\n");
                analysisOutput.append("\nLanguage: "+argLanguage+"\nText: "+textName+"\nStatistic: "+statisticName);
                analysisOutput.append("\nnGramSize: "+nGramSize.getText()+"\nRestarts: "+restarts.getText()+"\nKeyLength: "+keySize.getText());
                
                break;
                
            case "encrypt":
                System.out.println("Encrypt selected?: "+String.valueOf(encrypt.getSelection()));
                if (encrypt.getSelection()) {
                    userKey = true;
                    userText = true;
                    System.out.println("userText changed in case encrypt in startMethod");
                }
                args = new String[6];
                args[0] = "-method";
                args[1] = "encrypt";
                args[2] = "-key";
                argKey = model.translateKeyToLogic();
                System.out.println("argKey (in start button:encrypt): "+argKey);
                args[3] = argKey;
                args[4] = "-plaintext";
                args[5] = argText;
                System.out.println("start widget for encryption");
                
                break;
                
            case "decrypt":   
                System.out.println("Decrypt selected?: "+String.valueOf(decrypt.getSelection()));
                if (decrypt.getSelection() && exampleText.getSelection()) {
                    userText = true;
                    System.out.println("userText: "+String.valueOf(userText)+" in case decrypt in startMethod");
                }
                if (writeText.getSelection() && decrypt.getSelection()) {
                    argText = ciphertext.getText();
                }
                if (decrypt.getSelection()) {
                    userKey = true;
                    System.out.println("userKey: "+String.valueOf(userText)+" in case decrypt in startMethod");
                }
                args = new String[6];
                args[0] = "-method";
                args[1] = "decrypt";
                args[2] = "-key";
                argKey = model.translateKeyToLogic();
                System.out.println("argKey (in start button: decrypt): "+argKey);
                args[3] = argKey;
                args[4] = "-cryptedText";
                args[5] = argText;
                System.out.println("start widget for decryption");
                
                break;
                
                default: System.out.println("No method choosen (in 'startMethod')");
                break;
            }
        }else {
            System.out.println("'argMethod' not initialized");
            analysisOutput.append("'argMethod' not initialized");
            exampleAnalysis();
            return;
        }

        checkArgs("startMethod(), before startApplication()");
        startApplication();
    }
    
    public void startApplication() {
        
        System.out.println("Method 'startApplication' started.");
        ParameterSettings ps = null;
        MethodApplication ma = null;
        
        try {
//          Configuration of given parameters and selecting and applying one of the three methods
            ps = new ParameterSettings(args, this.analysisOutput);
            ma = new MethodApplication(ps, this.analysisOutput, argStatistics);
            
        } catch (InvalidParameterCombinationException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            System.out.println("Please enter valid parameters\nFehler in Main Methode!!!!");
            return;
        }catch(FileNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("File not found !");
            return;
        }
          switch(argMethod) {
          case "analyze": ma.analyze();
                          analysisOutput.append(ma.toString());
                          plaintext.setText("Gefundener Klartext:\n\n"+ma.getBestDecryptedText());
                          break;
          case "encrypt": ma.encrypt();
                          ciphertext.setText(ma.getEncryptedText());
                          break;
          case "decrypt": ma.decrypt();
                          plaintext.setText(ma.getDecryptedText());
                          break;
          }
          System.out.println("startMethod() ended");

//          analysisOutput.append("To String method of Method Application in fgSolver: \n"+ma.toString());
//          System.out.println("To String method of Method Application in fgSolver: \n"+ma.toString());
    }
    
    public void exampleAnalysis() {
        
        System.out.println("exampleAnalysis() gestartet");

        args = new String[12];
        args[0] = "-method";
        args[1] = "analyze";
        args[2] = "-keyLength";
        args[3] = "7";
        args[4] = "-cryptedText";
        args[5] = lf.InputStreamToString(lf.openMyTestStream("files/dawkinsGerCiphertext7.txt"));
        args[6] = "-nGramSize";
        args[7] = "4";
        args[8] = "-language";
        args[9] = "german";
        args[10] = "-restarts";
        args[11] = "5";
        
        userText = true;
        System.out.println("userText (in exmapleAnalysis()): "+String.valueOf(userText)+", restart() starting...\n");
        analysisOutput.append("userText (in exmapleAnalysis()): "+String.valueOf(userText)+", restart() starting...\n");
//        pretend restart
        reset();
        startSettings = true;
        
//        Settings for key
        resetKey();
        
//        Settings for text selection
        resetTextSelection();
        
//      Settings for method
        defaultMethod();
        
//        settings for analysis
        language.select(0);
        statistics.setSelection(true);
        statisticsLoad.setSelection(false);
        selectStatistic.select(0);
        restarts.setSelection(5);
        nGramSize.setSelection(4);
        
        startSettings = false;
        System.out.println("...restart done\n");
        analysisOutput.append("...restart done\n");
        
        fis = lf.openMyFileStream("files/de-4gram-nocs.bin");
        try {
            argStatistics = lf.loadBinNgramFrequencies(fis, "german", 4);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("start widget for analysis\n");
        analysisOutput.append("start widget for analysis\n");
        
        analysisOutput.append("\nBeispielanalyse gestartet...\n");
        System.out.println("\nBeispielanalyse gestartet...\n"); 
        
        checkArgs("exampleAnalysis(), before startApplication()");
        startApplication();
        
        analysisOutput.append("\n...Beispielanalyse durchgeführt");
        System.out.println("\n...Beispielanalyse durchgeführt");   
    }
    
    public void generateRandomKey() {
        //reset the old schablone
        System.out.println("begin generate RandomKey");
        model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
//        keyArgs = new ArrayList<>();
//        reset();
        
        int size = model.getKey().getSize();
        System.out.println("Size before random grille: "+size);

        int x,y;

        do {
            do
            {
                x = ThreadLocalRandom.current().nextInt(0, size);
                y = ThreadLocalRandom.current().nextInt(0, size);
            }
            while (model.getKey().get(x, y) != '0');
            model.getKey().set(x, y);
            keyToLogic = new int[2];
//            System.out.println("Size of key array: "+keyToLogic.length);
            keyToLogic[0] = y;
            keyToLogic[1] = x;
//            keyArgs.add(keyToLogic);
//            System.out.println(keyArgs.get(keyArgs.size()-1));
            System.out.println("keyToLogic(y): "+keyToLogic[0]+", keyToLogic(x): "+keyToLogic[1]);
            System.out.println("sets x: "+x+", sets y: "+y+", setted : "+model.getKey().get(x, y));
        } while (!model.getKey().isValid());
//        int i=0;
//        for (int[] key : keyArgs) {
//            System.out.println("show ArrayList Element "+i+": ("+key[0]+","+key[1]+")");
//            i++;
//        }
        System.out.println("Schlüssel nach Zufallserzeugung: "+model.translateKeyToLogic());
        
        reset();

    }
    
    public void deleteHoles() {
        System.out.println("deleteHoles-Method started\n");
        model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
        reset();
        canvasKey.removeMouseListener(keyListener);
        canvasKey.addMouseListener(keyListener);  
    }
    
    public void reset() {

        System.out.println("reset-Method started\n");
        checkOkButton();
        canvasKey.redraw();
    }
    
    public void refreshTexts() {
        
        System.out.println("refreshTexts-Method started\n");
        reset();
        
        textName = lf.textFiles(chooseExample.getSelectionIndex());
        argText = lf.InputStreamToString(lf.openMyTestStream(textName));
        
        resetTexts();
    }
    
    public void resetTexts() {
        
        System.out.println("resetTexts-Method started\n");
        if (startSettings) {
            
            ciphertext.setText(argText);
            plaintext.setText("");
        }else if (!plain) {
            
            if (!userKey) {
                randomEncryption();
            }
            
            ciphertext.setText(argText);
            plaintext.setText("");
        }else {
            plaintext.setText(argText);
            ciphertext.setText("");
        }
        reset();
    }
    
    public void resetStatistics(){
        
        System.out.println("resetStatistics-Method started\n");
            statisticName = lf.statisticFiles(selectStatistic.getSelectionIndex());
            fis = lf.openMyFileStream(statisticName);
    }
    
    public void checkOkButton() {
        
        System.out.println("checkOkButton-Method started\n");
        
        if (analyze.getSelection()) {
            
            if(/*!ciphertext.getText().equals("")*/!argText.equals("")  && fis!=null) {
                start.setEnabled(true);
            }else {
                start.setEnabled(false);
            }
            
        }else if (encrypt.getSelection()) {
            
            if (model.getKey().isValid() && !argText.equals("")/*!plaintext.getText().equals("")*/) { //$NON-NLS-1$
                System.out.println("Schlüssel: \n"+model.getKey());
//                keyArgs = new ArrayList<>();
//                int size = model.getKey().getSize();
//                for (int x=0; x<size; x++) {
//                    for (int y=0; y<size; y++) {
//                        if (model.getKey().get(x, y)=='1') {
//                            keyToLogic = new int[2];
//                            keyToLogic[0] = y;
//                            keyToLogic[1] = x;
//                            keyArgs.add(keyToLogic);
//                            System.out.println("(In CheckOKButton) keyToLogic(y): "+keyToLogic[0]+", keyToLogic(x): "+keyToLogic[1]);
//                            System.out.println("sets x: "+x+", sets y: "+y+", setted : "+model.getKey().get(x, y));
//                        }
//                    }
//                }
//                int i=1;
//                for (int[] key : keyArgs) {
//                    System.out.println("show ArrayList Element "+i+": ("+key[0]+","+key[1]+")");
//                    i++;
//                }
                System.out.println("model translatekeyToLogic (in checkOkButton): "+model.translateKeyToLogic());
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            }
        }else {
            
            if (model.getKey().isValid() && !argText.equals("")/*!ciphertext.getText().equals("")*/) { //$NON-NLS-1$
                System.out.println("Schlüssel: \n"+model.getKey());
                System.out.println("model translatekeyToLogic (in checkOkButton): "+model.translateKeyToLogic());
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            } 
        }  
    }
    
//    public void checkUsertext() {
//        
//        System.out.println("checkUsertext-Method started\n");
//        if (userText && !ciphertext.getText().isEmpty() ) {
//            
//        }
//    }
    
    public void restart() {
        
        System.out.println("restart-Method started\n");

        startSettings = true;
        
//        Settings for key
        resetKey();
        
//        Settings for text selection
        resetTextSelection();
        
//      Settings for method
        defaultMethod();
        
//        settings for analysis
        resetAnalysisSettings();
        
        startSettings = false;
        reset();
    }
    
    public void resetAnalysisSettings() {
        
        System.out.println("resetAnalysisSettings-Method started\n");
        
        language.select(0);
        statistics.setSelection(true);
        statisticsLoad.setSelection(false);
        selectStatistic.select(0);
        restarts.setSelection(5);
        nGramSize.setSelection(4);
        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
    }
    
    public void resetKey() { 
        
        System.out.println("resetKey-Method started\n");
        
        keySize.setSelection(7);
        deleteHoles();
    }
    
    public void resetTextSelection() {
        
        System.out.println("resetTextSelection-Method started\n");
        
        userText = false;
        
        exampleText.setSelection(true);
        loadOwntext.setSelection(false);
        writeText.setSelection(false);
        textSelection(false, true, false, false);
        loadText.setEnabled(false);
        chooseExample.setEnabled(true);
        chooseExample.select(0);
        writeText.setEnabled(false);
        plaintext.setEditable(false);
        plaintext.setEnabled(false);
        ciphertext.setEditable(false);
        ciphertext.setEnabled(true);
    }
    
    public void randomEncryption() {
        
        System.out.println("randomEncryption-Method started\n");
        
        String tempMethod = argMethod;
        
        generateRandomKey();
        argMethod = "encrypt";
        startMethod();
        argMethod = tempMethod; 
        argText = ciphertext.getText();
    }
    
    public void defaultMethod() {
        
        System.out.println("defaultMethod-Method started\n");
        
        analyze.setSelection(true);
        encrypt.setSelection(false);
        decrypt.setSelection(false);
        
        analyze();
    }
    
    public void checkArgs(String methode) {
        System.out.println("checkArgs started in: "+methode+"\nStartSettings?: "+startSettings);
        System.out.println("ArgMethod: "+argMethod+"\nArgText: "+textName+"\nArgLanguage: "+argLanguage+"\nArgStatistic: "+statisticName);
        System.out.println("\nPlain?: "+plain+"\nUserText?: "+userText+"\nUserKey?: "+userKey);
    }

    public Canvas getCanvasKey() {
        return canvasKey;
    }

    public void setCanvasKey(Canvas canvasKey) {
        this.canvasKey = canvasKey;
    }

    public Grille getModel() {
        return model;
    }

    public void setModel(Grille model) {
        this.model = model;
    } 
    
    public Spinner getKeySize() {
        return keySize;
    }

    public void setKeySize(Spinner keySize) {
        this.keySize = keySize;
    }
    

}
