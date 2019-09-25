package org.jcryptool.analysis.fleissner.UI;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ThreadLocalRandom;

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
    
    private Grille model;
    private SashForm sashForm;
    private Composite headerComposite;
    private Composite mainComposite;
    private Composite analysisOut;
    private Composite method;
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
    private Text descriptionText;
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
//    private Hashtable<Integer, Integer> htRestarts = new Hashtable<Integer, Integer>();
    
    private int[] weights = {4,1};
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
    private String oldLanguage = null;
    private InputStream fis = null;
    private InputStream fisOld = null;
    private LoadFiles lf = new LoadFiles();


    /**
     * Constructor creates header and main, sets new default grille
     * @param parent
     * @param style
     */
    public FleissnerWindow(Composite parent, int style) {
        super(parent, style);
       
        model = new Grille();
        model.setKey(new KeySchablone(7));
       
        createHeader(this);
        createMain(this);
        
        startSettings = false;
        reset();
    }
    
    /**
     * creates main composite that includes all information except of header
     * @param parent
     */
    private void createMain(Composite parent) {

       sashForm = new SashForm(parent, SWT.BORDER | SWT.BORDER_DASH | SWT.VERTICAL);
       sashForm.setLayout(new GridLayout());
       sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));    

       GridLayout gridLayoutParent = new GridLayout(3, false);
       GridData gdAnalysisOut = new GridData(SWT.FILL, SWT.FILL, true, true);
       
       ScrolledComposite scrolledMainComposite = new ScrolledComposite(sashForm, SWT.V_SCROLL);
       scrolledMainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
       mainComposite = new Composite(scrolledMainComposite, SWT.NONE);     
       mainComposite.setLayout(gridLayoutParent);
       mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
       
       ScrolledComposite scrolledAnalysisOutComposite = new ScrolledComposite(sashForm, SWT.V_SCROLL);
       scrolledAnalysisOutComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
       analysisOut = new Composite(scrolledAnalysisOutComposite, SWT.NONE);
       analysisOut.setLayout(gridLayoutParent);
       analysisOut.setLayoutData(gdAnalysisOut);

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
       scrolledAnalysisOutComposite.setExpandHorizontal(true);
       scrolledAnalysisOutComposite.setExpandVertical(true);
       scrolledAnalysisOutComposite.layout();
       
       sashForm.setWeights(weights);

//       controls resize of analysis output
       scrolledAnalysisOutComposite.addListener(SWT.Resize, new Listener(){
         @Override
         public void handleEvent(Event arg0)
         {
             Rectangle r = scrolledAnalysisOutComposite.getClientArea();
             if (analysisOut.getBounds().height>r.height) {

                 gdAnalysisOut.minimumHeight = r.height;
                 analysisOut.setLayoutData(gdAnalysisOut);
                 sashForm.setWeights(weights);
             }
         } 
      });
    }
  
    /**
     * creates method composite where one of the three methods can be choosen, method can be started or example analysis can be run
     * @param parent
     */
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
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
            @Override
            public void widgetSelected(SelectionEvent e) {
//                only execute after plugin has already been started
                if (!startSettings) {
//                    only execute if analyze as method has been selected, not through switching between other radio buttons in same composite
                    if (analyze.getSelection()) {
//                        only execute if 'analyze' was not already selected
                        if (!argMethod.equals("analyze")) {
                            analyze();
                            reset();
                        }
                    }  
                    else {
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
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if (encrypt.getSelection()) {
//                  only execute if 'encrypt' was not already selected
                    if (!argMethod.equals("enrypt")) {
                        encrypt();
                        reset();
                    }
                }  
            }
        });
        
        decrypt = new Button(method, SWT.RADIO);
        decrypt.setText("Entschlüsselung");
        decrypt.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
            @Override
            public void widgetSelected(SelectionEvent e) {

                if (decrypt.getSelection()) {
//                  only execute if 'decrypt' was not already selected
                    if (!argMethod.equals("decrypt")) {
                        decrypt();
                        reset();
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
                try {
                    startMethod();
                } catch (InvalidParameterCombinationException e1) {
                    LogUtil.logError(Activator.PLUGIN_ID, "Keine Methode ausgewählt", e1, true);
                    e1.printStackTrace();
                }
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
    
    /**
     * creates key composite including key grille, grille length and buttons 'random key' and 'delete key' 
     * to respectively generate a random key or delete the chosen holes
     * @param parent
     */
    private void createKey(Composite parent) {
        
        key = new Group(parent, SWT.NONE);
        key.setLayout(new GridLayout(3, false));
        key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 2));
        key.setText("Schlüssel");

//        canvas for grille with fixed height and width
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
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
//            refreshes key with changing key sizes and refreshes Example text, if needed
            public void widgetSelected(SelectionEvent e) {  
                if (Integer.parseInt(keySize.getText()) > 20 || Integer.parseInt(keySize.getText()) < 2)
                    keySize.setSelection(7);
                model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
                canvasKey.redraw();
                canvasKey.removeMouseListener(keyListener);
                canvasKey.addMouseListener(keyListener);
                if (exampleText.getSelection() && !plain)
                    refreshExampleText();
                reset();
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
                reset();
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
                reset();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }
   
    /**
     * creates composite for plaintext and ciphertext
     * @param parent
     */
    private void createInOutText(Composite parent) {
        
        inOutText = new Group(parent, SWT.NONE);
        inOutText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 6));
        GridLayout gl_inOutText = new GridLayout(1,false);
        inOutText.setLayout(gl_inOutText); 
        
        createPlaintext(inOutText);
        
        createCiphertext(inOutText);
    }
    
    /**
     * creates and controls plaintext composite
     * @param parent
     */
    private void createPlaintext(Composite parent) {
        
        plaintextComposite = new Group(parent, SWT.NONE);
        plaintextComposite.setLayout(new GridLayout());
        plaintextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        plaintextComposite.setText("Klartext" + "(0)");

        plaintext = new Text(plaintextComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        limits text field width and height so text will wrap at the end of composite
        gridData.widthHint = plaintextComposite.getSize().y; 
        gridData.heightHint = plaintextComposite.getSize().x; 
        plaintext.setLayoutData(gridData);
        plaintext.setBackground(ColorService.WHITE);
        plaintext.setEditable(false);
        plaintext.setEnabled(false);
        plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                e.doit = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        plaintext.addModifyListener(new ModifyListener() {
            
            @Override
//            parameter 'argText' is set every time plaintext input is modified
            public void modifyText(ModifyEvent e) {
                plaintextComposite.setText("Klartext (" + plaintext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ 
                
                if (!startSettings) {
                    if (writeText.getSelection() && encrypt.getSelection()) {
                        argText = plaintext.getText();
                        reset();
                    }   
                }
            }
        });
    }
    
    /**
     * creates and controls ciphertext composite
     * @param parent
     */
    private void createCiphertext(Composite parent) {
        
        ciphertextComposite = new Group(parent, SWT.NONE);
        ciphertextComposite.setLayout(new GridLayout());
        ciphertextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        ciphertextComposite.setText("Geheimtext" + " (0)");
        
        ciphertext = new Text(ciphertextComposite,  SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//        limits text field width and height so text will wrap at the end of composite
        gridData.widthHint = ciphertextComposite.getSize().y; 
        gridData.heightHint = ciphertextComposite.getSize().x; 
        ciphertext.setLayoutData(gridData);
        ciphertext.setEnabled(true);
        ciphertext.setEditable(false);
        ciphertext.setBackground(ColorService.WHITE);
//        default text
        textName = "files/dawkinsGerCiphertext7.txt";
        argText = lf.InputStreamToString(lf.openMyTestStream(textName));
        refreshInOutTexts();

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
//          parameter 'argText' is set every time ciphertext input is modified
            public void modifyText(ModifyEvent e) {
                ciphertextComposite.setText("Geheimtext (" + ciphertext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ 
                if (!startSettings) {
                    if (writeText.getSelection() && !encrypt.getSelection()) {
                        argText =ciphertext.getText();
                        reset();
                    }     
                }
            }
        });
        ciphertextComposite.setText("Geheimtext (" + ciphertext.getText().length() + ")");
    }
    
    /**
     * creates composites for text selection and analysis settings. Text selection manages the text input. Analysis settings manages the used
     * language, number of restarts and used language statistics as well as statistics input
     * @param parent
     */
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
        analysisSettingsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
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

        language = new Combo(languageAndRestarts, SWT.DROP_DOWN | SWT.READ_ONLY);
        language.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        language.setItems(items);
        language.select(0);
//        default language
        argLanguage = "german";
        language.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                
//                listener should only react when another item has been chosen
                if (languageState != language.getSelectionIndex()) {
                    updateLanguageSettings("language");
                    if (statistics.getSelection()) {
//                        changes example statistic when language changed
                        refreshStatistics(null);
                    }
                    if (exampleText.getSelection()) {
//                        changes example text when language changed
                        refreshExampleText();
                        textState = chooseExample.getSelectionIndex();
                    }
                    setArgLanguage();
                    languageState = language.getSelectionIndex();
                    reset();
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
     * creates buttons and controls in text selection composite
     * @param thirdGroup
     */
    private void createLoadtextComposite(Composite thirdGroup) {
        String[] items = {"Richard Dawkins - Der Gotteswahn", "Wikipedia - Frühchristliche Kunst", "Richard Dawkins - The God Delusion", "Wikipedia - Visual Arts"};
        
        exampleText = new Button(thirdGroup, SWT.RADIO);
        exampleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        exampleText.setText("Beispieltext");
        exampleText.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if (exampleText.getSelection()) {
                    if (textInputState != 0) {
                        loadedTextName.setText(""); 
                        userText = false;
                        textSelection(true, false, false, false);
                        refreshExampleText();
                        textInputState = 0;
                    }
                    reset();
                }  
            }
        });
        exampleText.setSelection(true);

        chooseExample = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        chooseExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        chooseExample.setItems(items);
        chooseExample.select(0);
        chooseExample.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {

                if (textState!=chooseExample.getSelectionIndex()) {
                    userText = false;
                    refreshExampleText();
                    
                    if (checkTextLangChange()) {
                        updateLanguageSettings("text");
                        if (statistics.getSelection()) {
                            refreshStatistics(null);
                        }
                        setArgLanguage();
                        languageState = language.getSelectionIndex();
                    }  
                    if (argMethod.equals("analyze"))
                        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
                    
                    textState = chooseExample.getSelectionIndex(); 
                    reset();
                }
            }
        });

        
        writeText = new Button(thirdGroup, SWT.RADIO);
        writeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        writeText.setText("Geheimtext manuell eingeben");
        writeText.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if (writeText.getSelection()) {
                   
//                    sets plaintext or ciphertext editable for encryption respectively analysis or decryption
                    loadedTextName.setText("");
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
                        refreshInOutTexts();
                    }
                    textSelection(false, false, editPlaintext, editCiphertext);
                    reset();
                    textInputState = 2;
                }   
            }
        });

        loadOwntext = new Button(thirdGroup, SWT.RADIO);
        loadOwntext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        loadOwntext.setText("Eigener Text");
        loadOwntext.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if (loadOwntext.getSelection()) {
                    
                    if (textInputState != 1) {
                        deleteHoles();
                        userText = true;
                        argText = "";
                        textName = "";
                        refreshInOutTexts();
                        textSelection(false, true, false, false);
                        textInputState = 1;
                        reset();
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
//                opening file with fileDialog
                String filename = openFileDialog(SWT.OPEN);
                String[] a = filename.split("\\\\");
                textName = filename;
//                load opened file
                argText = loadNormal(filename);
                userText = true; 
//                display fileName to user
                loadedTextName.setText(a[a.length-1]);
                
                if (argMethod.equals("analyze"))
                    analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
                
                refreshInOutTexts();
                reset();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);    
            }
        }); 
        
        textNameIdentifier = new Label(thirdGroup, SWT.NONE);
        textNameIdentifier.setText("Geladener Text: "); //$NON-NLS-1$
        textNameIdentifier.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true));
        
        loadedTextName = new Text(thirdGroup, SWT.NONE);
        loadedTextName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
        loadedTextName.setBackground(ColorService.LIGHTGRAY);
    }
    
    /**
     * creates and controls buttons and other tools for analysis settings 
     * @param thirdGroup
     */
    private void createLoadstatisticsComposite(Group thirdGroup) {
        String[] items = { "de-4-gram-nocs.bin", "en-4-gram-nocs.bin", "en-3-gram-nocs.bin"};
        
        statistics = new Button(thirdGroup, SWT.RADIO);
        statistics.setSelection(true);
        statistics.setText("Sprachstatistik");
        statistics.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                if (statistics.getSelection()) {
                    if (statisticInputState != 0) {
                        loadedStatisticName.setText("");
                        userStatistics = false;
                        statisticSelection();
                        updateLanguageSettings("text");
                        refreshStatistics(null);
                        reset();
                        statisticInputState = 0;
                    }
                }
            }
        });

         selectStatistic = new Combo(thirdGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
         selectStatistic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
         selectStatistic.setItems(items);
         selectStatistic.select(0);
         refreshStatistics(null);
         selectStatistic.addSelectionListener(new SelectionListener() {
             @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                 widgetSelected(e);         
             }
             @Override
            public void widgetSelected(SelectionEvent e) {
                 
                 if (statisticState != selectStatistic.getSelectionIndex()) {
                     if (selectStatistic.getSelectionIndex()==2) {
                         nGramSize.setSelection(3);
                     }
                     else {
                         nGramSize.setSelection(4);
                     }
                     if (statistics.getSelection()) {
                         refreshStatistics(null);
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
                     reset();
                 }
             }
         });

         statisticsLoad = new Button(thirdGroup, SWT.RADIO);
         statisticsLoad.setText("Eigene Statistik");
         statisticsLoad.addSelectionListener(new SelectionListener() {
             @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                 widgetSelected(e);
             }
             @Override
            public void widgetSelected(SelectionEvent e) {
                 
                 if (statisticsLoad.getSelection()) {
                     if (statisticInputState != 1) {
                         userStatistics = true;
                         refreshStatistics(null);
                         statisticSelection();
                         reset();
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
//                 open user statistic with fileDialog
                 String filename = openStatFileDialog(SWT.OPEN);
                 userStatistics = true;
//                 set fileInputStream from filename
                 refreshStatistics(filename);
                 String[] a = filename.split("\\\\");
                 loadedStatisticName.setText(a[a.length-1]);
                 reset();
             }
             @Override
             public void widgetDefaultSelected(SelectionEvent e) {
                 widgetSelected(e);
             }
         });    
        
         chooseNGramSize = new Label(thirdGroup, SWT.NONE);
         chooseNGramSize.setText("Größe nGram"); //$NON-NLS-1$
         chooseNGramSize.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
         
//         nGramSize currently limited to 3-4 because there are no other working nGrams with the same structure
         nGramSize = new Spinner(thirdGroup, SWT.NONE);
         nGramSize.setMinimum(3);
         nGramSize.setMaximum(4);
         nGramSize.setIncrement(1);
         nGramSize.setSelection(4);
         nGramSize.setEnabled(false);
         nGramSize.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
         nGramSize.setToolTipText("Länge 'n' eines Datums in der nGram-Statistik, bspw. n=4 für ein Datum 'AAAA'");
         
         statisticNameIdentifier = new Label(thirdGroup, SWT.NONE);
         statisticNameIdentifier.setText("Geladene Statistik: "); //$NON-NLS-1$
         statisticNameIdentifier.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
         
         loadedStatisticName = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
         loadedStatisticName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
         loadedStatisticName.setBackground(ColorService.LIGHTGRAY);
    }
    
    /**
     * creates composite for analysis output where all results if analysis will be displayed
     * @param parent
     */
    private void createAnalysisOutput(Composite parent) {
        
        analysis = new Group(parent, SWT.NONE);
        analysis.setLayout(new GridLayout());
        analysis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        analysis.setText("Ausgabe Analyse");
    
        analysisOutput = new Text(analysis, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        analysisOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
        analysisOutput.setEditable(false);
        analysisOutput.setBackground(ColorService.WHITE);
    }
    
    /**
     * opening file for input text
     * @param type
     * @return the choosen .txt file path
     */
    private String openFileDialog(int type) {
        FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
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
    private String openStatFileDialog(int type) {
        FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { "*.bin" }); //limits the eligible files to *.bin files
        dialog.setFilterNames(new String[] { "Binary Files (*.bin)" }); //$NON-NLS-1$
        dialog.setOverwrite(true);
        return dialog.open();
    }
    
    /**
     * reads choosen .txt file into a String
     * @param fileName
     * @return the input String
     */
    private String loadNormal(String fileName) {

        BufferedReader reader = null;
        String text = "";
        
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), IConstants.UTF8_ENCODING));         
            String line = reader.readLine();
        
            int count = 1;
            while (line  != null) {
                text += line;
                LogUtil.logInfo(count+". Zeile in 'text' geschrieben (in reader while schleife): "+text);
                count++;
                line = reader.readLine();
            }
        } catch (NumberFormatException nfe) {
            LogUtil.logError(Activator.PLUGIN_ID, nfe);
            MessageBox brokenFile = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
            brokenFile.setText("Loading text encountered a problem"); //$NON-NLS-1$
            brokenFile.setMessage("Text could not be loaded. There is a wrong character in the loaded file.\n"); //$NON-NLS-1$
            brokenFile.open();
           
        } catch (FileNotFoundException e) {   
            LogUtil.logError(Activator.PLUGIN_ID, "text could not be loaded", e, true);
            return "text could not be loaded";

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

        return text;
    }

    /**
     * Creates the header and short description.
     * @param parent
     */
    private void createHeader(Composite parent) {
        headerComposite = new Composite(parent, SWT.NONE);
        headerComposite.setBackground(ColorService.WHITE);
        headerComposite.setLayout(new GridLayout());
        headerComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
   
        Text headerText = new Text(headerComposite, SWT.READ_ONLY);
        headerText.setText("Fleißner-Analyse");
        headerText.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false));
        headerText.setFont(FontService.getHugeBoldFont());
        headerText.setEditable(false);
        headerText.setBackground(ColorService.WHITE);
        
        descriptionText = new Text(headerComposite, SWT.Resize | SWT.READ_ONLY |SWT.WRAP | SWT.MULTI);
        descriptionText.setBackground(ColorService.WHITE);
        
//        field 'descriptionText' has to be limited so it won't allocate unnecessary space
        final GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.widthHint = headerComposite.getClientArea().width;
        gridData.heightHint = (descriptionText.getLineCount())*(descriptionText.getLineHeight());

        descriptionText.setLayoutData(gridData);
        descriptionText.setEditable(false);       
        descriptionText.addListener(SWT.Resize | SWT.MouseDoubleClick, new Listener(){
            
            @Override
//            recalculating needed space for description with resize by movement of text field
            public void handleEvent(Event arg0)
            {
                gridData.heightHint = (descriptionText.getLineCount())*(descriptionText.getLineHeight());
                descriptionText.requestLayout();
            } 
         });
        descriptionText.setText("Dieses Plug-In dient zur Analyse von Geheimtexten, die durch die Fleißner-Schablone verschlüsselt wurden. Dazu werden Geheimtexte mit einer Schlüssellänge von <5 mit dem 'brute-force'-Verfahren analysiert, bei dem jede mögliche Schablone dieser Größe ausprobiert wird. Bei Geheimtexten mit einem größeren zugehörigen Schlüssel kommt das Verfahren des 'hill-Climbing' zum Einsatz, wobei zufällig ausgewählte Schablonen schrittweise verbessert werden. Texte können auch zuvor selbst verschlüsselt oder entschlüsselt werden.");
    }
    
    /**
     *  installs settings for 'analyze' is chosen as the active method
     */
    public void analyze() {
         
        plain = false;
        argMethod = "analyze";

        setArgText();
        deleteHoles();

//        text settings
        plaintext.setEnabled(false);
        plaintext.setForeground(null);
        ciphertext.setEnabled(true);
        ciphertext.setForeground(null);
        
//        key settings
        canvasKey.setEnabled(false);
        randomKey.setEnabled(false);
        deleteHoles.setEnabled(false);
      
//        analysis settings
        analysis.setEnabled(true);
        analysisSettingsGroup.setEnabled(true);
        analysisOutput.setEnabled(true);
      
        chooseLanguage.setEnabled(true);
        language.setEnabled(true);
        setArgLanguage();
      
        restarts.setEnabled(true);
        statistics.setEnabled(true);
        statisticsLoad.setEnabled(true);
      
        if (statistics.getSelection()) 
            userStatistics = false;
        else 
            userStatistics = true;
      
        statisticSelection();
        refreshStatistics(null);
    }
    
    /**
     * installs settings for 'encrypt' is chosen as the active method
     */
    public void encrypt() {
        
        plain = true;

        argMethod = "encrypt";
        
        deleteHoles();
        setArgText();     
        
//        text settings
        plaintext.setEnabled(true);
        plaintext.setForeground(null);
        ciphertext.setEnabled(false);
        ciphertext.setForeground(null);
        
//        key settings
        canvasKey.setEnabled(true);
        randomKey.setEnabled(true);
        deleteHoles.setEnabled(true);
        
//        analysis settings
        analysisSettingsGroup.setEnabled(false);
        analysis.setEnabled(false);
        analysisOutput.setEnabled(false);

        statistics.setEnabled(false);
        selectStatistic.setEnabled(false);
        statisticsLoad.setEnabled(false);
        loadStatistics.setEnabled(false);
        loadedStatisticName.setText("");
        userStatistics = false;

        refreshStatistics(null);
        
        language.setEnabled(false);
        restarts.setEnabled(false);
        nGramSize.setEnabled(false);   
    }
    
    /**
     * installs settings for 'decrypt' is chosen as the active method
     */
    public void decrypt() {
        
        plain = false;
        argMethod = "decrypt";

        setArgText();
      
//        text settings
        plaintext.setEnabled(false);
        plaintext.setForeground(null);
        ciphertext.setEnabled(true);
        ciphertext.setForeground(null);
        
//        key settings
        canvasKey.setEnabled(true);
        randomKey.setEnabled(true);
        deleteHoles.setEnabled(true);
        
//        analysis settings
        analysisSettingsGroup.setEnabled(false);
        analysis.setEnabled(false);
        analysisOutput.setEnabled(false);

        statistics.setEnabled(false);
        selectStatistic.setEnabled(false);
        statisticsLoad.setEnabled(false);
        loadStatistics.setEnabled(false);
        loadedStatisticName.setText("");
        userStatistics = false;

        refreshStatistics(null);
        
        language.setEnabled(false);
        restarts.setEnabled(false);
        nGramSize.setEnabled(false); 
    }
    
    /**
     * sets the arguments for chosen method and starts method 'startApplication' that execudes method
     * @throws InvalidParameterCombinationException
     */
    public void startMethod() throws InvalidParameterCombinationException{
        
        if (argMethod != null) {
            switch (argMethod) {
            
            case "analyze":
                userText = true;
                analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");

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
                
//                argStatistics shall only be reloaded if changed
                if (fisOld != fis || !oldNgramSize.equals(nGramSize.getText()) || !oldLanguage.equals(argLanguage)) {
                    try {
                        argStatistics = lf.loadBinNgramFrequencies(fis, argLanguage, nGramSize.getSelection());
                        fisOld = fis;
                        oldNgramSize = nGramSize.getText();
                        oldLanguage = argLanguage;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID,"Gültige Statistik eingeben", e, true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID,"Datei nicht gefunden", e, true);
                    } catch (InvalidParameterCombinationException e) {
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID, "Invalid Parameter Combination", e, true);
                        return;
                    }
                }
                break;
                
            case "encrypt":
//                if 'encrypt' is not selected this method will only be called in random encryption and userText stays false
                if (encrypt.getSelection()) 
                    userText = true;

                args = new String[6];
                args[0] = "-method";
                args[1] = argMethod;
                args[2] = "-key";
                argKey = model.translateKeyToLogic();
                args[3] = argKey;
                args[4] = "-plaintext";
                args[5] = argText;
                
                break;
                
            case "decrypt":   

                userText = true;

                args = new String[6];
                args[0] = "-method";
                args[1] = argMethod;
                args[2] = "-key";
                argKey = model.translateKeyToLogic();
                args[3] = argKey;
                args[4] = "-cryptedText";
                args[5] = argText;
                
                break;
            }
        }else {
            throw new InvalidParameterCombinationException("Keine Methode ausgewählt");
        }
        startApplication();
    }
    
    /**
     * executes chosen method with given parameters and returns result in particular text field
     */
    public void startApplication() {
        
        ParameterSettings ps = null;
        MethodApplication ma = null;
        
        try {
//          Configuration of given parameters and selecting and applying one of the three methods
            ps = new ParameterSettings(args);
            ma = new MethodApplication(ps, this.analysisOutput, argStatistics);
            
        } catch (InvalidParameterCombinationException ex) {
            ex.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID, "Bitte gültige Parameter eingeben", ex, true);
            return;
        }catch(FileNotFoundException ex) {
            ex.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID, "Datei nicht gefunden", ex, true);
            return;
        }
          switch(argMethod) {
          case "analyze": analysisOutput.append("\nParameter: \n"+checkArgs());
                          ma.analyze();
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
    }
    
    /**
     * method for example analysis: resets all settings to default and sets parameters for example analysis, then starts 
     * method 'startApplication' that executes analysis
     */
    public void exampleAnalysis() {
        
//        reset all settings to default

        startSettings = true;
        
//        Settings for key
        resetKey();

//        Settings for analysis
        resetAnalysisSettings(); 
        
//        Settings for method
        resetMethod(); 
        
//        Settings for text selection
        resetTextSelection();
        
        startSettings = false;
        
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
        
        try {
            argStatistics = lf.loadBinNgramFrequencies(fis, argLanguage, nGramSize.getSelection());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID,"Gültige Statistik eingeben", e, true);
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID,"Datei nicht gefunden", e, true);
            return;
        } catch (InvalidParameterCombinationException e) {
            e.printStackTrace();
            LogUtil.logError(Activator.PLUGIN_ID,"Invalid Parameter Combination", e, true);
            return;
        }
        startApplication();
    }
    
    /**
     * generates a random key with current key length and displays key in grille
     */
    public void generateRandomKey() {

        model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
        
        int size = model.getKey().getSize();
        int x,y;

        do {
            do
            {
                x = ThreadLocalRandom.current().nextInt(0, size);
                y = ThreadLocalRandom.current().nextInt(0, size);
            }
            while (model.getKey().get(x, y) != '0');
            model.getKey().set(x, y);
        } while (!model.getKey().isValid());
        
        canvasKey.redraw();
    }
    
    /**
     * executes random encryption with random key for example texts that are needed for analysis or decryption. If 'analyze' is
     * the chosen method the key displayed on the grille will be deleted afterwards
     */
    public void randomEncryption() {
      
        String tempMethod = argMethod;
      
        generateRandomKey();
        argMethod = "encrypt";
        try {
            startMethod();
        } catch (InvalidParameterCombinationException e) {
            LogUtil.logError(Activator.PLUGIN_ID, "Keine Methode ausgewählt", e, true);
            e.printStackTrace();
        }
        argMethod = tempMethod; 
        argText = ciphertext.getText();
        if (argMethod.equals("analyze"))
            deleteHoles();
    }
    
    /**
     * deletes all chosen holes on the grille
     */
    public void deleteHoles() {
        
        model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
        canvasKey.redraw();
        canvasKey.removeMouseListener(keyListener);
        canvasKey.addMouseListener(keyListener);  
    }
    
    /**
     * refreshes parameter argText after changes in example text selection
     */
    public void refreshExampleText() {
  
        userText = false;
        textName = lf.textFiles(chooseExample.getSelectionIndex());
        argText = lf.InputStreamToString(lf.openMyTestStream(textName));
  
        if (!plain)
          randomEncryption();
  
        refreshInOutTexts();
    }
  
    /**
     * refreshes displayed text in plaintext or ciphertext field after parameter argtext has changed
     */
    public void refreshInOutTexts() {

        if (plain) {
            plaintext.setText(argText);
            ciphertext.setText("");
        }else {
            ciphertext.setText(argText);
            plaintext.setText("");
        }
    }
    
    /**
     * sets statisticName and (FileInputStream) fis when input statistic has changed
     * @param fileName
     */
    public void refreshStatistics(String fileName){
 
        if (argMethod.equals("analyze")){
            if (userStatistics) {
                if (fileName!=null) {
//                    only if user loads own statistics
                    try {
                        fis = new FileInputStream(fileName);
                        statisticName = fileName;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        LogUtil.logError(Activator.PLUGIN_ID, "Statistik konnte nicht geladen werden", e, true);
                    }
                }else {
                    statisticName = "";
                    fis = null;
                }
            }else {
                statisticName = lf.statisticFiles(selectStatistic.getSelectionIndex());
                fis = lf.openMyFileStream(statisticName);
                statisticState = selectStatistic.getSelectionIndex();
            }
        }else {
            statisticName = "";
            fis = null;
        }
    }

    /**
     * enables and disables buttons and text fields in composite for text selection dependent on chosen text input
     * @param enableChooseExample
     * @param enableLoadText
     * @param editPlaintext
     * @param editCiphertext
     */
    public void textSelection(boolean enableChooseExample, boolean enableLoadText, boolean editPlaintext, boolean editCiphertext) {
 
        chooseExample.setEnabled(enableChooseExample);
        loadText.setEnabled(enableLoadText);
        plaintext.setEditable(editPlaintext);
        ciphertext.setEditable(editCiphertext);
      
        if (argMethod.equals("analyze"))
            analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
    }
  
    /**
     * enables and disables buttons for statistics settings dependent on chosen statistics input
     */
    public void statisticSelection() {
      
        if (!userStatistics) {
            selectStatistic.setEnabled(true);
            loadStatistics.setEnabled(false);
            nGramSize.setEnabled(false);  
        }
        else {
            selectStatistic.setEnabled(false);
            loadStatistics.setEnabled(true);
            nGramSize.setEnabled(true);  
        }
    }
  
    /**
     * sets parameter argLanguage
     */
    public void setArgLanguage() {
           
        if (language.getSelectionIndex()==0) {
            argLanguage = "german";
        }
        else {
            argLanguage = "english";
        }
    }

    /**
     * sets parameter 'argText' in method changes and manages text input settings
     */
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
            refreshInOutTexts();
        }else if (exampleText.getSelection()) {
            
            if (userText && !tempArgText.equals("")) {
                argText = tempArgText;
                refreshInOutTexts();
            }else {
                refreshExampleText();
            }
        }else {
            argText = tempArgText;
            refreshInOutTexts();
        }
    }
    
    /**
     * updates settings of all three important statistics settings (text, language, statistic)
     * if one of them  has been changed
     * @param setting
     */
    public void updateLanguageSettings(String setting) {
        
        switch(setting) {
        case "text": 
            if (chooseExample.getSelectionIndex() == 0 || chooseExample.getSelectionIndex() == 1) {
                language.select(0);
                selectStatistic.select(0);
                nGramSize.setSelection(4);
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
                if (textState!=1)
                    chooseExample.select(0);
                language.select(0);
            }else {
                if (textState!=3)
                    chooseExample.select(2);
                language.select(1);
            }
            break;
        }
    }
    
    /**
     * displays key in grille that has been found in analysis
     */
    public void printFoundKey() {
        
        deleteHoles();
        for (int i =0; i<keyToLogic.length/2; i++) {
            model.getKey().set(keyToLogic[2*i+1], keyToLogic[2*i]);
        }
    }
    
    /**
     * checks if all parameters for chosen method are correct and enables 'start' button if so
     */
    public void checkOkButton() {
        
        if (analyze.getSelection()) {
          
            if(!argText.equals("")  && fis!=null) 
                start.setEnabled(true);
            else
                start.setEnabled(false);
       }else {
          
           if (model.getKey().isValid() && !argText.equals("")) { //$NON-NLS-1$
               start.setEnabled(true);
           } else {
               start.setEnabled(false);
           } 
       }  
    }
    
    /**
     * checks start button and redraws grille
     */
    public void reset() {

        checkOkButton();
        canvasKey.redraw();
    }
    
    /**
     * resets analysis settings to default
     */
    public void resetAnalysisSettings() {
        
        language.select(0);
        argLanguage = "german";
        languageState = 0;
        statistics.setSelection(true);
        statisticInputState = 0;
        statisticsLoad.setSelection(false);
        selectStatistic.select(0);
        restarts.setSelection(5);
        nGramSize.setSelection(4);
        analysisOutput.setText("Gefundene Schablone/ Fortschritt\n");
    }
    
    /**
     * resets key to default
     */
    public void resetKey() { 
        
        keySize.setSelection(7);
        deleteHoles();
    }
    
    /**
     * resets text selection settings to default
     */
    public void resetTextSelection() {
        
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
    
    /**
     * resets method settings to default
     */
    public void resetMethod() {
        
        analyze.setSelection(true);
        encrypt.setSelection(false);
        decrypt.setSelection(false);
        
        analyze();
    }
    
    /**
     * checks if change of example text selection has changed language
     * @return change that is true if language has changed and false otherwise
     */
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
    
    /**
     * checks if change of example language statistics selection has changed language
     * @return change that is true if language has changed and false otherwise
     */
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
    
    /**
     * 
     * @return the most important parameters for starting analysis. Method will be executed at the start of every analysis
     */
    public String checkArgs() {
        
        String args = "";
        
        args += "\nText: "+textName;
        args += "\nSchlüssellänge: "+keySize.getText();
        args += "\nRestarts: "+restarts.getText();
        args += "\nSprache: "+argLanguage;
        args += "\nSprachstatistik: "+statisticName;
        args += "\nGröße nGram: "+nGramSize.getText();
        
        return args;
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
