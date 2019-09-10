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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import org.bouncycastle.util.encoders.UTF8;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tracker;
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
    
    private String fileName;
    private Grille model;
    private Composite headerComposite;
    private Composite mainComposite;
    private Composite analysisOut;
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
    private boolean userText = false;
    private boolean userStatistics = false;

    private boolean startSettings = true;
    private Hashtable<Integer, Integer> htRestarts = new Hashtable<Integer, Integer>();
    
    private int textState = 0;
    private int languageState = 0;
    private int statisticState = 0;
    private int statisticInputState = 0;
    private int textInputState = 0;
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
    private InputStream fis = null;
    private InputStream fisOld = null;
    private LoadFiles lf = new LoadFiles();
    
    private static final int MIN_WIDTH_LEFT = 100;
    private static final int MIN_WIDTH_RIGHT = 50;


    public FleissnerWindow(Composite parent, int style) {
        super(parent, style);
       
        model = new Grille();
        model.setKey(new KeySchablone(7));
        
        SashForm sashForm = new SashForm(this, SWT.BORDER | SWT.BORDER_DASH | SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1));

        GridLayout gridLayoutParent = new GridLayout(3, false);
        
        ScrolledComposite scrolledMainComposite = new ScrolledComposite(sashForm,/* SWT.H_SCROLL | */SWT.V_SCROLL);
        scrolledMainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainComposite = new Composite(scrolledMainComposite, SWT.NONE);     
        mainComposite.setLayout(gridLayoutParent);
        
        ScrolledComposite scrolledAnalysisOutComposite = new ScrolledComposite(sashForm,/* SWT.H_SCROLL | */SWT.V_SCROLL);
        scrolledAnalysisOutComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));
        analysisOut = new Composite(scrolledAnalysisOutComposite, SWT.NONE);
        analysisOut.setLayout(gridLayoutParent);
        
//        fw = new FleissnerWindow(parent, SWT.NONE);
//        fw.setLayout(new GridLayout(1,true));
//        fw.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,1));
        
//        mainComposite = new Composite(sashForm,  SWT.NONE/*SWT.DEFAULT*/);
//        mainComposite.setLayout(new GridLayout(3, false));
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        
//        analysisOut = new Composite(sashForm,  SWT.NONE/*SWT.DEFAULT*/);
//        analysisOut.setLayout(new GridLayout(3, false));
//        analysisOut.setLayout(gridLayoutParent);
        analysisOut.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//        analysisOut.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        createHeader(mainComposite);
        createMethod(mainComposite);
        createKey(mainComposite);
        createInOutText(mainComposite);
        Composite platzHalter = new Composite(mainComposite, SWT.NONE);
        platzHalter.setLayout(new GridLayout());
        platzHalter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,1));
        createText(mainComposite);
        createAnalysisOutput(analysisOut);
        
        scrolledMainComposite.setContent(mainComposite);
        scrolledMainComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledMainComposite.setExpandHorizontal(true);
        scrolledMainComposite.setExpandVertical(true);
        scrolledMainComposite.layout();
        
        scrolledAnalysisOutComposite.setContent(analysisOut);
        scrolledAnalysisOutComposite.setMinSize(analysisOut.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//        scrolledAnalysisOutComposite.setMinSize(SWT.DEFAULT, 50);
        scrolledAnalysisOutComposite.setExpandHorizontal(true);
        scrolledAnalysisOutComposite.setExpandVertical(true);
        scrolledAnalysisOutComposite.layout();
        
        int[] sashWeights = sashForm.getWeights();
        System.out.println("SashWeights: "+/*sashWeights[0]+","+sashWeights[1]*/Arrays.toString(sashWeights));
        
//        sashForm.setSashWidth(10);
        int[] weights = { 4,1};
////        int[] weights = { mainComposite.getSize().y,this.getSize().y-mainComposite.getSize().y};
        sashForm.setWeights(weights);
        
//        Das habe ich grad noch bei Stackoverflow gefunden, muss es aber noch anpassen
//        this.addListener(SWT.Resize, new Listener()
//        {
//            @Override
//            public void handleEvent(Event arg0)
//            {
//                int width = parent.getClientArea().width;
//                int[] weights = sashForm.getWeights();
//
//                if(width >= MIN_WIDTH_LEFT + MIN_WIDTH_RIGHT)
//                {
//                    weights[0] = 1000000 * MIN_WIDTH_LEFT / width;
//                    weights[1] = 1000000 - weights[0];
//                }
//                else
//                {
//                    weights[0] = 1000000 * MIN_WIDTH_LEFT / (MIN_WIDTH_LEFT + MIN_WIDTH_RIGHT);
//                    weights[1] = 1000000 * MIN_WIDTH_RIGHT / (MIN_WIDTH_LEFT + MIN_WIDTH_RIGHT);
//                }
//
//                System.out.println(width + " " + Arrays.toString(weights));
//
//                sashForm.setWeights(weights);
//            }
//        });
//
//        this.pack();
        
        sashWeights = sashForm.getWeights();
        System.out.println("SashWeights: "+/*sashWeights[0]+","+sashWeights[1]*/Arrays.toString(sashWeights));
        
        startSettings = false;
        reset("Konstruktor");
       
    }
  
    private void createMethod(Composite parent) {
        
        methodComposite = new Group(parent, SWT.NONE);
        methodComposite.setLayout(new GridLayout());
        methodComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        methodComposite.setText("Methode");
        
        method = new Composite(methodComposite, SWT.NONE);
        method.setLayout(new GridLayout());
        method.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, true));
        
        process = new Composite(methodComposite, SWT.NONE);
        process.setLayout(new GridLayout());
        process.setLayoutData(new GridData(SWT.FILL, SWT.DOWN, false, false));
         
        analyze = new Button(method, SWT.RADIO);
        analyze.setText("Analyse");
        analyze.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                if (!startSettings) {
                    if (analyze.getSelection()) {
                        if (!argMethod.equals("analyze")) {
                            System.out.println("Analyze selected. Method 'analyze' will be started.");
                            analyze();
                            checkArgs("analyze Selection Listener");
                            reset("'analyze'-Listener");
                        }
                    }  
                    else {
//                        resetAnalysisSettings(); notwendig???
                        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
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
                    if (!argMethod.equals("enrypt")) {
                        encrypt();
                        checkArgs("Encrypt Selection Listener");
                        reset("'encrypt'-Listener");
                    }
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
                    if (!argMethod.equals("decrypt")) {
                        decrypt();
                        checkArgs("decrypt Selection Listener");
                        reset("'decrypt'-Listener");
                    }
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
        start.setToolTipText("Führt die ausgewählte Methode aus");
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
        example.setToolTipText("Führt eine Analyse mit vorgegebenen Daten - Text, Sprache, Sprachstatistik - durch");
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
        
        GridData startOptions = new GridData(SWT.FILL, SWT.TOP, true, true);
        start.setLayoutData(startOptions);
        example.setLayoutData(startOptions);
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
                reset("'keySize'-Listener");
                canvasKey.removeMouseListener(keyListener);
                canvasKey.addMouseListener(keyListener);
                if (exampleText.getSelection() && !plain)
                    refreshExampleText();
                reset("keySize");
            }
        });
        
        randomKey = new Button(key, SWT.PUSH);
        
        GridData gd_setHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false,2,1);
        gd_setHoles.horizontalIndent = 20;
        randomKey.setLayoutData(gd_setHoles);
        randomKey.setEnabled(false);
        randomKey.setText("Zufälliger Schlüssel");
        randomKey.setToolTipText("Erzeugt zufällig einen Schlüssel in der ausgewählten Schlüssellänge zur Ver- oder Entschlüsselung");
        randomKey.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {

                generateRandomKey();
                reset("'randomKey'-Listener");
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
        deleteHoles.setToolTipText("Setzt die ausgewählten Schlüsselfelder zurück");
        deleteHoles.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
               
                deleteHoles();
                reset("'deleteHoles'-Listener");
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }
   
    private void createInOutText(Composite parent) {
        
        inOutText = new Group(parent, /*SWT.V_SCROLL*/SWT.NONE);
        inOutText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 6));
        // Composite should have no border, so it looks like the groups on the left.
        GridLayout gl_inOutText = new GridLayout(1,false);
        inOutText.setLayout(gl_inOutText); 
        
        createPlaintext(inOutText);
        
        createCiphertext(inOutText);
    }
    
    
    private void createPlaintext(Composite parent) {
        
        plaintextComposite = new Group(parent, /* SWT.V_SCROLL*/SWT.NONE);
        plaintextComposite.setLayout(new GridLayout());
        plaintextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true/*, 1, 3*/));
        plaintextComposite.setText("Klartext" + "(0)");
        
        
     
        plaintext = new Text(plaintextComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL/* | SWT.H_SCROLL*/);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = plaintextComposite.getSize().y; 
        gridData.heightHint = plaintextComposite.getSize().x; 
        plaintext.setLayoutData(gridData);
        plaintext.setBackground(ColorService.WHITE);
        plaintext.setEditable(false);
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
                    if (writeText.getSelection() && encrypt.getSelection()) {
                        argText =plaintext.getText();
                        reset("'plaintext'-ModifyListener");
                    }   
                }
//                reset("'plaintext'-ModifyListener");
            }
        });
    }
    
    private void createCiphertext(Composite parent) {
        
        ciphertextComposite = new Group(parent,  /*SWT.MULTI | SWT.WRAP | SWT.V_SCROLL*/ SWT.NONE);
        ciphertextComposite.setLayout(new GridLayout());
        ciphertextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true/*, 1, 3*/));
        ciphertextComposite.setText("Geheimtext" + " (0)");
        
        ciphertext = new Text(ciphertextComposite,  SWT.MULTI | SWT.WRAP | SWT.V_SCROLL/* | SWT.H_SCROLL*/);
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = ciphertextComposite.getSize().y; 
        gridData.heightHint = ciphertextComposite.getSize().x; 
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
                    if (writeText.getSelection() && !encrypt.getSelection()) {
                        argText =ciphertext.getText();
                        reset("'ciphertext'-ModifyListener");
                    }     
                }
//                reset("'ciphertext'-ModifyListener");
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
        analysisSettingsGroup.setLayout(new GridLayout(4, false));
        analysisSettingsGroup.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, false, 2, 1));
        analysisSettingsGroup.setText("Analyseeinstellungen");
        analysisSettingsGroup.setEnabled(true);
      
        createLoadtextComposite(textSelectionGroup);
        
        String[] items = {"Deutsch", "Englisch"};
      
        Composite languageAndRestarts = new Composite(analysisSettingsGroup, SWT.NONE);
        languageAndRestarts.setLayout(new GridLayout(4, false));
        languageAndRestarts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        
        Group statisticGroup = new Group(analysisSettingsGroup, SWT.NONE);
        statisticGroup.setLayout(new GridLayout(4,false));
        statisticGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
        statisticGroup.setText("Statistikauswahl");
        
        chooseLanguage = new Text(languageAndRestarts, SWT.WRAP | SWT.MULTI);
        chooseLanguage.setText("Sprache");
        chooseLanguage.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        chooseLanguage.setBackground(ColorService.LIGHTGRAY);

        // Create a dropdown Combo
        language = new Combo(languageAndRestarts, SWT.DROP_DOWN | SWT.READ_ONLY);
        language.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        language.setItems(items);
        language.select(0);
        argLanguage = "german";
        language.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (languageState != language.getSelectionIndex()) {
                    updateLanguageSettings("language");
                    if (statistics.getSelection()) {
                        resetStatistics();
                        statisticState = selectStatistic.getSelectionIndex();
                    }
                    if (exampleText.getSelection()) {
                        refreshExampleText();
                        textState = chooseExample.getSelectionIndex();
                    }
                    setArgLanguage();
                    checkArgs("language Selection Listener");
                    languageState = language.getSelectionIndex();
                    reset("'language'-Listener");
                }
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
        restarts.setToolTipText("Bestimmt die Anzahl die Wiederholungen mit neuen Zufallsschablonen bei der Hill-Climbing Methode");
      
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
                    if (textInputState != 0) {
                        loadedTextName.setText("");
                        
                        userText = false;
                        
                        System.out.println("UserText (in exampleText): "+String.valueOf(userText));
                        textSelection(true, false, false, false);
                        refreshExampleText();

                        checkArgs("Example Text Selection Listener");
                        textInputState = 0;
                    }
                    reset("'exampleText'-Listener");
                }  
            }
        });
        exampleText.setSelection(true);

        // Create a dropdown Combo
        chooseExample = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        chooseExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        chooseExample.setItems(items);
        chooseExample.select(0);
        
        chooseExample.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                if (textState!=chooseExample.getSelectionIndex()) {
                    userText = false;
                    refreshExampleText();
                    
                    if (checkTextLangChange()) {
                        updateLanguageSettings("text");
                        if (statistics.getSelection()) {
                            resetStatistics();
                            statisticState = selectStatistic.getSelectionIndex();
                        }
                        setArgLanguage();
                        languageState = language.getSelectionIndex();
                    }  
                    if (argMethod.equals("analyze"))
                        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
                    
                    textState = chooseExample.getSelectionIndex(); 
                    checkArgs("chooseExample SelectionListener");
                    reset("'chooseExample'-Listener");
                }
            }
        });
        
        writeText = new Button(thirdGroup, SWT.RADIO);
        writeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        writeText.setText("Geheimtext manuell eingeben");

        loadOwntext = new Button(thirdGroup, SWT.RADIO);
        loadOwntext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        loadOwntext.setText("Eigener Text");
        loadOwntext.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (loadOwntext.getSelection()) {
                    
                    if (textInputState != 1) {
                        deleteHoles();

                        userText = true;
                        argText = "";
                        textName = "";
                        resetTexts();
                        textSelection(false, true, false, false);
                        
                        checkArgs("loadOwnText SelectionListener");
                        textInputState = 1;
                        reset("'loadOwnText'-Listener");
                    }
                } 
            }
        });
       
        loadText = new Button(thirdGroup, SWT.PUSH);
        loadText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
        loadText.setText("Text laden");
        loadText.setEnabled(false);
        loadText.setToolTipText("Hier kann eine eigene Textdatei geladen werden. Kodierung: UTF-8");
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
                
                loadedTextName.setText(a[a.length-1]);
                
                if (argMethod.equals("analyze"))
                    analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
                
                resetTexts();
                reset("loadText-Listener");
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
//        textNameIdentifier.setVisible(false);
        
        loadedTextName = new Text(thirdGroup, /*SWT.WRAP | SWT.MULTI*/ SWT.NONE);
        loadedTextName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        loadedTextName.setBackground(ColorService.LIGHTGRAY);
//        loadedTextName.setVisible(false);
       
//        writeText = new Button(thirdGroup, SWT.RADIO);
//        writeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
//        writeText.setText("Geheimtext manuell eingeben");
        writeText.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (writeText.getSelection()) {
                   
                    loadedTextName.setText("");
                    
//                    analyze.setEnabled(false);
                    boolean editPlaintext = false;
                    boolean editCiphertext = false;
                    userText = true;

                    if (plain) {
                        editPlaintext = true;
                        editCiphertext = false;
                    }else {
                        editPlaintext = false;
                        editCiphertext = true;
                    }
                    if (textInputState != 2) {
                        
                        deleteHoles();
                        argText = "";
                        resetTexts();
                    }
                    textSelection(false, false, editPlaintext, editCiphertext);
                    checkArgs("writeText SelectionListener");
                    reset("'writeText'-Listener");
                    textInputState = 2;
                }   
            }
        });
    }
    
    private void createLoadstatisticsComposite(Group thirdGroup) {
        // TODO Auto-generated method stub    
        String[] items = { "de-4-gram-nocs.bin"/*, "de-3-gram-nocs.bin"*/, "en-4-gram-nocs.bin", "en-3-gram-nocs.bin"};
        
        statistics = new Button(thirdGroup, SWT.RADIO);
        statistics.setSelection(true);
        statistics.setText("Sprachstatistik");
        statistics.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                
                if (statistics.getSelection()) {
                    if (statisticInputState != 0) {
//                        statisticNameIdentifier.setVisible(false);
//                        loadedStatisticName.setVisible(false);
                        loadedStatisticName.setText("");
                        userStatistics = false;
                        statisticSelection();
                        updateLanguageSettings("text");
                        resetStatistics();
                        checkArgs("statistics SelectionListener");
                        reset("'statistics'-Listener");
                        statisticInputState = 0;
                    }
                }
            }
        });


        // Create a dropdown Combo
         selectStatistic = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
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
                 
                 if (statisticState != selectStatistic.getSelectionIndex()) {
                     if (selectStatistic.getSelectionIndex()==2) {
                         nGramSize.setSelection(3);
                     }
                     else {
                         nGramSize.setSelection(4);
                     }
                     if (statistics.getSelection()) {
                         resetStatistics();
                         statisticState = selectStatistic.getSelectionIndex();
                     }
                     if (checkStatisticLangChange()) {
                         updateLanguageSettings("statistic");
                         if (exampleText.getSelection()) {
                             refreshExampleText();
                             textState = chooseExample.getSelectionIndex();
                         }
                         setArgLanguage();
                         languageState = language.getSelectionIndex();
                     }
                     checkArgs("selectStatistic SelectionListener");
                     reset("'selectStatistic'-Listener Ende");
                 }
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
                     if (statisticInputState != 1) {
                         fis = null;
                         statisticName = "";
                         userStatistics = true;
                         statisticSelection();
                         checkArgs("statisticsLoad SelectionListener");
                         reset("statisticsLoad'-Listener");
                         statisticInputState = 1;
                     }
                 }
             }
         });
        
         loadStatistics = new Button(thirdGroup, SWT.PUSH);
         loadStatistics.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
         loadStatistics.setText("Statistik laden");     
         loadStatistics.setEnabled(false);
         loadStatistics.setToolTipText("Hier kann eine eigene Sprachstatistik geladen werden. Format: Für Quadgramme bspw. sortiert von AAAA bis ZZZZ, wobei jedes Datum ein double (8byte) ist. Die Statistik sollte bereits logarithmisiert sein");
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
//                    loadedStatisticName.setVisible(true);
//                    statisticNameIdentifier.setVisible(true);
                    userStatistics = true;
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    LogUtil.logError(Activator.PLUGIN_ID, "Statistik auswählen", e1, true);
                }
                 checkArgs("loadStatistics SelectionListener");
                 reset("loadStatistics'-Listener Ende");
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
//         statisticNameIdentifier.setVisible(false);
         
         loadedStatisticName = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
         loadedStatisticName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
         loadedStatisticName.setBackground(ColorService.LIGHTGRAY);
//         loadedStatisticName.setVisible(false);
    }
    
    private void createAnalysisOutput(Composite parent) {
        
        analysis = new Group(parent, /* SWT.V_SCROLL*/SWT.NONE/*SWT.DRAG*/);
        analysis.setLayout(new GridLayout());
        analysis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true/*, 3, 1*/));
        analysis.setText("Ausgabe Analyse");
    
        analysisOutput = new Text(analysis, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        analysisOutput.setLayoutData(gridData);
        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
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
                LogUtil.logInfo(count+". Zeile in 'text' geschrieben (in reader while schleife): "+text);
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
            
            LogUtil.logError(Activator.PLUGIN_ID, "text could not be loaded", e, true);
            return "text could not be loaded";
            //Error handling

        } catch (IOException e) {
            
            LogUtil.logError(Activator.PLUGIN_ID, "text could not be loaded", e, true);
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
        headerText.setLayoutData(new GridData(SWT.LEFT, SWT.UP, false, true));
        headerText.setFont(FontService.getHugeBoldFont());
        headerText.setEditable(false);
        headerText.setBackground(ColorService.WHITE);

        
        Text descriptionText = new Text(headerComposite, SWT.WRAP | SWT.MULTI);
        descriptionText.setText("Hier kommt eine kurze Beschreibung hin.");
        descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, true));
        descriptionText.setEditable(false);
        descriptionText.setBackground(ColorService.WHITE);
    }
    
    public void textSelection(boolean enableChooseExample, boolean enableLoadText, boolean editPlaintext, boolean editCiphertext) {
        
        System.out.println("textSelection-Method started\n");
        
        chooseExample.setEnabled(enableChooseExample);
        loadText.setEnabled(enableLoadText);
        plaintext.setEditable(editPlaintext);
        ciphertext.setEditable(editCiphertext);
        
        if (argMethod.equals("analyze"))
            analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
        
//        if (!enableLoadText) 
//            loadedTextName.setText("");
    }
    
    public void statisticSelection() {
        
        System.out.println("statisticSelection-Method started\n");
        
//        reset("statisticSelection() Anfang");
        
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
    
    public void setArgLanguage() {
        
        System.out.println("setArgLanguage-Method started\n");
        
//        reset("setArgLanguage() Anfang");
        
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

        setArgText();
        deleteHoles();
        
//      key settings
      canvasKey.setEnabled(false);
      randomKey.setEnabled(false);
      deleteHoles.setEnabled(false);
      
      plaintext.setEnabled(false);
      plaintext.setForeground(null);
      ciphertext.setEnabled(true);
      ciphertext.setForeground(null);
      
//      analysis settings
      analysis.setEnabled(true);
      analysisSettingsGroup.setEnabled(true);
      analysisOutput.setEnabled(true);
      
      chooseLanguage.setEnabled(true);
      language.setEnabled(true);
      setArgLanguage();
      
      restarts.setEnabled(true);
      statistics.setEnabled(true);
      statisticsLoad.setEnabled(true);
      
      if (statistics.getSelection()) {
          nGramSize.setEnabled(false);
          selectStatistic.setEnabled(true);
          loadStatistics.setEnabled(false);
          resetStatistics();
      }else {
          nGramSize.setEnabled(true);
          selectStatistic.setEnabled(false);
          loadStatistics.setEnabled(true);
          statisticName = "";
          fis = null;
      }
    }
    
    public void encrypt() {
        
        System.out.println("encrypt()-Method entered");
        plain = true;

        argMethod = "encrypt";
        deleteHoles();
        setArgText();     
        
//        key settings
        canvasKey.setEnabled(true);
        randomKey.setEnabled(true);
        deleteHoles.setEnabled(true);
       
//        text settings
//        writeText.setText("Klartext manuell eingeben");
//
//        if (textInputState!=1)
//            loadedTextName.setText("");
//        
//        if (textInputState == 2) {
//            plaintext.setEditable(true);
//            ciphertext.setEditable(false);
//        }
        
        plaintext.setEnabled(true);
        plaintext.setForeground(null);
        ciphertext.setEnabled(false);
        ciphertext.setForeground(null);
        
//        analysis settings
        analysisSettingsGroup.setEnabled(false);
        analysis.setEnabled(false);
        analysisOutput.setEnabled(false);

        statistics.setEnabled(false);
        selectStatistic.setEnabled(false);
        statisticsLoad.setEnabled(false);
        loadStatistics.setEnabled(false);
//        loadedStatisticName.setVisible(false);
//        statisticNameIdentifier.setVisible(false);
        loadedStatisticName.setText("");
        userStatistics = false;
        statisticName = "";
        fis = null;
        
        language.setEnabled(false);
        restarts.setEnabled(false);
        nGramSize.setEnabled(false);   
    }
    
    public void decrypt() {
        
        System.out.println("Decrypt()-Method entered");
        checkArgs("decrypt() Anfang");
        plain = false;
        argMethod = "decrypt";

        setArgText();
        
//        key settings
        canvasKey.setEnabled(true);
        randomKey.setEnabled(true);
        deleteHoles.setEnabled(true);
       
//        text settings
//        writeText.setEnabled(true);
//        writeText.setText("Geheimtext manuell eingeben");
//        
//        if (textInputState!=1)
//            loadedTextName.setText("");
//        
//        if (textInputState == 2) {
//            plaintext.setEditable(false);
//            ciphertext.setEditable(true);
//        }
        
        plaintext.setEnabled(false);
        plaintext.setForeground(null);
        ciphertext.setEnabled(true);
        ciphertext.setForeground(null);
        
//        analysis settings
        analysisSettingsGroup.setEnabled(false);
        analysis.setEnabled(false);
        analysisOutput.setEnabled(false);

        statistics.setEnabled(false);
        selectStatistic.setEnabled(false);
        statisticsLoad.setEnabled(false);
        loadStatistics.setEnabled(false);
//        loadedStatisticName.setVisible(false);
//        statisticNameIdentifier.setVisible(false);
        loadedStatisticName.setText("");
        userStatistics = false;
        statisticName = "";
        fis = null;
        
        language.setEnabled(false);
        restarts.setEnabled(false);
        nGramSize.setEnabled(false); 
        
        checkArgs("decrypt() Ende");

    }
    
    public void startMethod() {
        
//        String keyToString;
        System.out.println("startMethod-Method started\n");
        System.out.println("Method (in startMethod): "+argMethod);
        if (argMethod != null) {
            switch (argMethod) {
            
            case "analyze":
                userText = true;
                analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
                System.out.println("Analyze selected?: "+String.valueOf(analyze.getSelection()));

                args = new String[12];
                args[0] = "-method";
                args[1] = argMethod;
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
                
                if (fisOld != fis || !oldNgramSize.equals(nGramSize.getText())) {
                    try {
                        argStatistics = lf.loadBinNgramFrequencies(fis, argLanguage, nGramSize.getSelection());
//                        System.out.println("\nStatistik geladen in startMethod(analyze): "+String.valueOf(fis)+"\n");
//                        analysisOutput.append("\nStatistik geladen in startMethod(analyze): "+String.valueOf(fis)+"\n");
                        fisOld = fis;
                        oldNgramSize = nGramSize.getText();
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID,"Gültige Statistik eingeben", e, true);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID,"Datei nicht gefunden", e, true);
                    } catch (InvalidParameterCombinationException e) {
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID, "Invalid Parameter Combination", e, true);
                        return;
                    }
                }
                
//                System.out.println("start widget for analysis");
//                analysisOutput.append("\nstart widget for analysis\n");
                analysisOutput.append("\nLanguage: "+argLanguage+"\nText: "+textName+"\nStatistic: "+statisticName);
                analysisOutput.append("\nnGramSize: "+nGramSize.getText()+"\nRestarts: "+restarts.getText()+"\nKey length: "+keySize.getText());
                
                break;
                
            case "encrypt":
                System.out.println("Encrypt selected?: "+String.valueOf(encrypt.getSelection()));
                if (encrypt.getSelection()) {
                    userText = true;
                    System.out.println("userText changed in case encrypt in startMethod");
                }
                args = new String[6];
                args[0] = "-method";
                args[1] = argMethod;
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
//                if (decrypt.getSelection() && exampleText.getSelection()) {
                    userText = true;
                    System.out.println("userText: "+String.valueOf(userText)+" in case decrypt in startMethod");
//                }
                args = new String[6];
                args[0] = "-method";
                args[1] = argMethod;
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
        System.out.println("startMethod() ended");
    }
    
    public void startApplication() {
        
        System.out.println("Method 'startApplication' started.");
        ParameterSettings ps = null;
        MethodApplication ma = null;
        
        try {
//          Configuration of given parameters and selecting and applying one of the three methods
            ps = new ParameterSettings(args/*, this.analysisOutput*/);
            ma = new MethodApplication(ps, this.analysisOutput, argStatistics);
            
        } catch (InvalidParameterCombinationException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
//            System.out.println("Bitte gültige Parameter eingeben");
            LogUtil.logError(Activator.PLUGIN_ID, "Bitte gültige Parameter eingeben", ex, true);
            return;
        }catch(FileNotFoundException ex) {
            ex.printStackTrace();
//            System.out.println("File not found !");
            LogUtil.logError(Activator.PLUGIN_ID, "Datei nicht gefunden", ex, true);
            return;
        }
          switch(argMethod) {
          case "analyze": ma.analyze();
                          analysisOutput.append(ma.toString());
                          plaintext.setEnabled(true);
                          plaintext.setForeground(ColorService.GRAY);
                          plaintext.setText("Gefundener Klartext:\n\n"+ma.getBestDecryptedText());
                          keyToLogic = ma.getBestTemplate();
                          printFoundKey();
                          break;
          case "encrypt": ma.encrypt();
                          if (encrypt.getSelection()) {
                              ciphertext.setEnabled(true);
                              ciphertext.setForeground(ColorService.GRAY);
                          }
                          ciphertext.setText(ma.getEncryptedText());
                          break;
          case "decrypt": ma.decrypt();
                          plaintext.setEnabled(true);
                          plaintext.setForeground(ColorService.GRAY);
                          plaintext.setText(ma.getDecryptedText());
                          break;
          }
//          analysis.setEnabled(true);
//          analysisOutput.setEnabled(true);
//          analysisOutput.append(ma.toString());
          System.out.println("startApplication() ended");
          checkArgs("startApplication() Ende");
    }
    
    public void exampleAnalysis() {
        
        System.out.println("exampleAnalysis() gestartet");
        
//        userText = true;
        resetAnalysisSettings();      
        System.out.println("userText (in exmapleAnalysis()): "+String.valueOf(userText)+", restart() starting...\n");
        analysisOutput.append("userText (in exmapleAnalysis()): "+String.valueOf(userText)+", restart() starting...\n");
//        pretend restart
        startSettings = true;
        
//        Settings for key
        resetKey();
        
//      Settings for method
        resetMethod(); 
        
//        Settings for text selection
        resetTextSelection();
        
        startSettings = false;
        System.out.println("...restart done\n");
        analysisOutput.append("...restart done\n");
        
      args = new String[12];
      args[0] = "-method";
      args[1] = argMethod;
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
        
        fis = lf.openMyFileStream("files/de-4gram-nocs.bin");
        statisticName = "de-4gram-nocs.bin";
        try {
            argStatistics = lf.loadBinNgramFrequencies(fis, argLanguage, nGramSize.getSelection());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID,"Gültige Statistik eingeben", e, true);
            return;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID,"Datei nicht gefunden", e, true);
            return;
        } catch (InvalidParameterCombinationException e) {
            e.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID,"Invalid Parameter Combination", e, true);
            return;
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
        //reset the old grille
        System.out.println("begin generate RandomKey");
        model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
        
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
            keyToLogic[0] = y;
            keyToLogic[1] = x;
            System.out.println("keyToLogic(y): "+keyToLogic[0]+", keyToLogic(x): "+keyToLogic[1]);
            System.out.println("sets x: "+x+", sets y: "+y+", setted : "+model.getKey().get(x, y));
        } while (!model.getKey().isValid());
        System.out.println("Schlüssel nach Zufallserzeugung: "+model.translateKeyToLogic());
        
        canvasKey.redraw();

    }
    
    public void deleteHoles() {
        System.out.println("deleteHoles-Method started\n");
        model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
        canvasKey.redraw();
        canvasKey.removeMouseListener(keyListener);
        canvasKey.addMouseListener(keyListener);  
    }
    
    public void reset(String methode) {

        System.out.println("reset-Method in "+methode+" started\n");
        checkOkButton();
        canvasKey.redraw();
    }
    

    public void refreshExampleText() {
  
        System.out.println("refreshTexts-Method started\n");

        userText = false;
        textName = lf.textFiles(chooseExample.getSelectionIndex());
        argText = lf.InputStreamToString(lf.openMyTestStream(textName));
  
        if (!plain)
          randomEncryption();
  
        resetTexts();
    }
  
    public void resetTexts() {

        System.out.println("resetTexts-Method started\n");
        if (plain) {
          plaintext.setText(argText);
          ciphertext.setText("");
        }else {
          ciphertext.setText(argText);
          plaintext.setText("");
        }
    }
    
    public void resetStatistics(){
        
        System.out.println("resetStatistics-Method started\n");
            statisticName = lf.statisticFiles(selectStatistic.getSelectionIndex());
            fis = lf.openMyFileStream(statisticName);
            File statFile = new File(statisticName);
            try {
                System.out.println("Statisticsize of "+statisticName+": "+fis.available());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    public void checkOkButton() {
        
        System.out.println("checkOkButton-Method started\n");
        
        if (analyze.getSelection()) {
            
            if(!argText.equals("")  && fis!=null) 
                start.setEnabled(true);
            else
                start.setEnabled(false);
        }else {
            
            if (model.getKey().isValid() && !argText.equals("")) { //$NON-NLS-1$
//                System.out.println("Schlüssel: \n"+model.getKey());
                System.out.println("model translatekeyToLogic (in checkOkButton): "+model.translateKeyToLogic());
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            } 
        }  
    }
    
    public void resetAnalysisSettings() {
        
        System.out.println("resetAnalysisSettings-Method started\n");
        
        language.select(0);
        argLanguage = "german";
        languageState = 0;
        statistics.setSelection(true);
        statisticInputState = 0;
        statisticsLoad.setSelection(false);
        selectStatistic.select(0);
        statisticState = 0;
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
        textInputState = 0;
        loadOwntext.setSelection(false);
        writeText.setSelection(false);
        textSelection(true, false, false, false);
        chooseExample.select(0);
        textState = 0;
        plaintext.setEnabled(false);
        ciphertext.setEnabled(true);
        ciphertext.setForeground(null);
    }
    
    public void setArgText() {
        
        String tempArgText, textInput;
        boolean edit;
        if (plain) {
            tempArgText = plaintext.getText();
            textInput = "Klartext";
            edit = true;

        }else {
            tempArgText = ciphertext.getText();
            textInput = "Geheimtext";
            edit = false;
        }
   
        writeText.setText(textInput+" manuell eingeben");
        
        if (textInputState!=1 || tempArgText.equals(""))
            loadedTextName.setText("");
        
        if (textInputState == 2) {
            plaintext.setEditable(edit);
            ciphertext.setEditable(!edit);
        }
        if (startSettings) {
            argText = lf.InputStreamToString(lf.openMyTestStream("files/dawkinsGerCiphertext7.txt"));
            resetTexts();
        }else if (exampleText.getSelection()) {
            
            if (userText && !tempArgText.equals("")) {
                argText = tempArgText;
                resetTexts();
            }else {
                refreshExampleText();
            }
        }else {
            argText = tempArgText;
            resetTexts();
        }
    }
    
    
    public void updateLanguageSettings(String setting) {
        
//        textState und languageState
        switch(setting) {
        case "text": 
            if (chooseExample.getSelectionIndex() == 0 || chooseExample.getSelectionIndex() == 1) {
                language.select(0);
                selectStatistic.select(0);
            }else {
                language.select(1);
                if (nGramSize.getSelection()==4)
                    selectStatistic.select(1);
                else
                    selectStatistic.select(2);
            }
            break;
        case "language":
            if (language.getSelectionIndex() == 0) {
                chooseExample.select(0);
                selectStatistic.select(0);
            }else {
                chooseExample.select(2);
                selectStatistic.select(1);
            }
            break;
        case "statistic":
            if (selectStatistic.getSelectionIndex()==0) {
                if (exampleText.getSelection() && textState!=1)
                    chooseExample.select(0);
                language.select(0);
            }else {
                if (exampleText.getSelection() && textState!=3)
                    chooseExample.select(2);
                language.select(1);
            }
            break;
        }
    }
    
    public void randomEncryption() {
        
        System.out.println("randomEncryption-Method started\n");
        
        String tempMethod = argMethod;
        
        generateRandomKey();
        argMethod = "encrypt";
        startMethod();
        argMethod = tempMethod; 
        argText = ciphertext.getText();
        if (argMethod.equals("analyze"))
            deleteHoles();
    }
    
    public void printFoundKey() {
        
        deleteHoles();
        for (int i =0; i<keyToLogic.length/2; i++) {
            model.getKey().set(keyToLogic[2*i+1], keyToLogic[2*i]);
        }
//        userText = true;
    }
    
    public void resetMethod() {
        
        System.out.println("defaultMethod-Method started\n");
        
        analyze.setSelection(true);
        encrypt.setSelection(false);
        decrypt.setSelection(false);
        
        analyze();
    }
    
    
    public boolean checkTextLangChange() {
        
        boolean change;
        
        if (languageState == 0 && (chooseExample.getSelectionIndex() == 2 || chooseExample.getSelectionIndex() == 3)) {
            change = true;
        }else if (languageState == 1 && (chooseExample.getSelectionIndex() == 0 || chooseExample.getSelectionIndex() == 1)) {
            change = true;
        }else {
            change = false;
        }
        return change;
    }
    
    public boolean checkStatisticLangChange() {
        
        boolean change;
        
        if (languageState == 0 && (selectStatistic.getSelectionIndex() == 1 || selectStatistic.getSelectionIndex() == 2)) {
            change = true;
        }else if (languageState == 1 && selectStatistic.getSelectionIndex() == 0) {
            change = true;
        }else {
            change = false;
        }
        return change;
    }
    
    public void checkTextInput() {
        
        switch(textInputState) {
        
        case 0:
            
            break;
        case 1:
            
            break;
        case 2:
            
            break;
        }
    }
    
    public void checkArgs(String methode) {
        System.out.println("checkArgs started in: "+methode+"\nStartSettings?: "+startSettings);
        System.out.println("ArgMethod: "+argMethod+"\nTextname: "+textName+"\nArgText: "+argText+"\nArgLanguage: "+argLanguage+"\nArgStatistic: "+statisticName+" \ntextInputState: "+textInputState+"\nstatInoutState: "+statisticInputState+"\nfis: "+fis);
        System.out.println("Plain?: "+plain+"\nUserText?: "+userText);
        System.out.println("Plaintext editable: "+plaintext.getEditable()+"\nCiphertext editable: "+ciphertext.getEditable());
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
