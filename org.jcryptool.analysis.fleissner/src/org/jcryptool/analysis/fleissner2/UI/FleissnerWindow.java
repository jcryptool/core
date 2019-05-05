package org.jcryptool.analysis.fleissner2.UI;


import java.io.BufferedReader;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

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
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.fleissner2.key.Grille;
import org.jcryptool.analysis.fleissner2.key.KeySchablone;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.core.util.fonts.FontService;


public class FleissnerWindow extends Composite{
    
    private String fileName;
    private Grille model;
    private Composite headerComposite;
    private Composite mainComposite;
    private Composite method;
    private Group key;
    private Composite text;
    private Group inOutText;
    private Group plaintextComposite;
    private Group ciphertextComposite;
    private Composite process;
    private Group analysis;
    private Canvas canvasKey;
    private KeyListener keyListener;
    private Button start;
    private Text plaintext;
    private Text ciphertext;
    private Spinner keySize;
    private Text analysisOutput;
    private Button analyze;
    private Button encrypt;
    private Button decrypt;
    private Group textSelectionGroup;
    private Group analysisSettingsGroup;
    private Spinner restarts;
    private Combo language;
    private Button writeText;
    private Text chooseLanguage;
    private Group methodComposite;
    private Label numberOfRestarts;
    private Button statistics;
    private Combo selectStatistic;
    private Button statisticsLoad;
    private Button loadStatistics;
    private Button exampleText;
    private Combo chooseExample;
    private Button loadOwntext;
    private Button loadText;


    public FleissnerWindow(Composite parent, int style) {
        super(parent, style);
       
        model = new Grille();
        model.setKey(new KeySchablone(6));
        
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
        analyze.setSelection(true);
        analyze.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
                analysis.setEnabled(true);
                writeText.setEnabled(false);
                analysisSettingsGroup.setEnabled(true);
                chooseLanguage.setEnabled(true);
                language.setEnabled(true);
                
                analysis.setEnabled(true);
                analysisOutput.setEnabled(true);
                numberOfRestarts.setEnabled(true);
                restarts.setEnabled(true);
                statistics.setEnabled(true);
                selectStatistic.setEnabled(true);
                statisticsLoad.setEnabled(true);
                loadStatistics.setEnabled(true);
                plaintext.setEnabled(false);
                ciphertext.setEnabled(true);
                
            }
        });
        analyze.setSelection(true);
        
        encrypt = new Button(method, SWT.RADIO);
        encrypt.setText("Verschlüsselung");
        encrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
                analysis.setEnabled(false);
                writeText.setEnabled(true);
                writeText.setText("Klartext manuell eingeben");
                analysisSettingsGroup.setEnabled(false);
                chooseLanguage.setEnabled(false);
                language.setEnabled(false);
                
                analysis.setEnabled(false);
                analysisOutput.setEnabled(false);
                numberOfRestarts.setEnabled(false);
                restarts.setEnabled(false);
                statistics.setEnabled(false);
                selectStatistic.setEnabled(false);
                statisticsLoad.setEnabled(false);
                loadStatistics.setEnabled(false);
                plaintext.setEnabled(true);
                plaintext.setEditable(true);
                ciphertext.setEnabled(false);
                
            }
        });
        
        
        decrypt = new Button(method, SWT.RADIO);
        decrypt.setText("Entschlüsselung");
        decrypt.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {

                reset();
                analysis.setEnabled(false);
                writeText.setEnabled(true);
                writeText.setText("Geheimtext manuell eingeben");
                analysisSettingsGroup.setEnabled(false);
                chooseLanguage.setEnabled(false);
                language.setEnabled(false);
                
                analysis.setEnabled(false);
                analysisOutput.setEnabled(false);
                numberOfRestarts.setEnabled(false);
                restarts.setEnabled(false);
                statistics.setEnabled(false);
                selectStatistic.setEnabled(false);
                statisticsLoad.setEnabled(false);
                loadStatistics.setEnabled(false);
                plaintext.setEnabled(false);
                ciphertext.setEnabled(true);
            }
        });
        
        GridData methods = new GridData(SWT.FILL, SWT.TOP, true, true);
        analyze.setLayoutData(methods);
        encrypt.setLayoutData(methods);
        decrypt.setLayoutData(methods);
      
        start = new Button(process, SWT.PUSH);
        start.setText("Start");
        start.setEnabled(false);
        
        Button example = new Button(process, SWT.PUSH);
        example.setText("Beispielanalyse");
        
        Button reset = new Button(process, SWT.PUSH);
        reset.setText("Neustart");   
        
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
        keyListener = new org.jcryptool.analysis.fleissner2.UI.KeyListener(model, this);
        canvasKey.addMouseListener(keyListener);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 4);
        gridData.widthHint = 201;
        gridData.heightHint = 201;
        canvasKey.setLayoutData(gridData);

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
        keySize.setSelection(6);
        keySize.setEnabled(true);
        GridData gd_keySize = new GridData(SWT.LEFT, SWT.TOP, false, true);
        gd_keySize.horizontalIndent = 20;
        keySize.setLayoutData(gd_keySize);
//        keySize.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));
        
        keySize.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            
            }

            public void widgetSelected(SelectionEvent e) {  
                if (Integer.parseInt(keySize.getText()) > 20 || Integer.parseInt(keySize.getText()) < 2)
                    keySize.setSelection(6);
                model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
                reset();
                canvasKey.removeMouseListener(keyListener);
                canvasKey.addMouseListener(keyListener);
            }
        });
        
        Button randomKey = new Button(key, SWT.PUSH);
        
        GridData gd_setHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false,2,1);
        gd_setHoles.horizontalIndent = 20;
        randomKey.setLayoutData(gd_setHoles);
        randomKey.setText("Zufälliger Schlüssel");
        randomKey.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                //reset the old schablone
                model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
                reset();
                
                int size = model.getKey().getSize();
                int x;
                int y;

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
                canvasKey.removeMouseListener(keyListener);
                canvasKey.addMouseListener(keyListener);
                checkOkButton();
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
                
            }
        });
        
        Button deleteHoles = new Button(key, SWT.PUSH);
        GridData gd_deleteHoles = new GridData(SWT.FILL, SWT.BOTTOM, false, false,2,1);
        gd_deleteHoles.horizontalIndent = 20;
        deleteHoles.setLayoutData(gd_deleteHoles);
        deleteHoles.setText("Schlüssel zurücksetzen"); //$NON-NLS-1$
        deleteHoles.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.setKey(new KeySchablone(Integer.parseInt(keySize.getText())));
                reset();
                canvasKey.removeMouseListener(keyListener);
                canvasKey.addMouseListener(keyListener);
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
    }
    
    
    private void createPlaintext(Composite parent) {
        
        plaintextComposite = new Group(parent,  SWT.NONE);
        plaintextComposite.setLayout(new GridLayout());
        plaintextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
        plaintextComposite.setText("Klartext" + "(0)");
     
        plaintext = new Text(plaintextComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        plaintext.setLayoutData(gridData);
//        plaintext.setBackground(ColorService.WHITE);
//        plaintext.setEditable(false);
        plaintext.setEnabled(false);
        plaintext.setText("plaintext");
        plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                e.doit = false;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        plaintext.addModifyListener(new ModifyListener() {
            
            @Override
            public void modifyText(ModifyEvent e) {
                plaintextComposite.setText("Klartext (" + plaintext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                reset();
            }
        });
        
    }
    
    private void createCiphertext(Composite parent) {
        
        ciphertextComposite = new Group(parent,  SWT.NONE);
        ciphertextComposite.setLayout(new GridLayout());
        ciphertextComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
        ciphertextComposite.setText("Geheimtext" + " (0)");
        
        ciphertext = new Text(ciphertextComposite,  SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        ciphertext.setLayoutData(gridData);
//        ciphertext.setEnabled(false);
//        ciphertext.setEditable(false);
//        ciphertext.setBackground(ColorService.WHITE);
        ciphertext.setText("ciphertext");
        ciphertext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                e.doit = false;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        ciphertext.addModifyListener(new ModifyListener() {
            
            @Override
            public void modifyText(ModifyEvent e) {
                ciphertextComposite.setText("Geheimtext (" + ciphertext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        });

    }
    
    private void createText(Composite parent) {
        
      textSelectionGroup = new Group(parent, SWT.NONE);
      textSelectionGroup.setLayout(new GridLayout(2, false));
      textSelectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
      textSelectionGroup.setText("Textauswahl");
      
      Composite platzHalter = new Composite(parent, SWT.NONE);
      platzHalter.setLayout(new GridLayout());
      platzHalter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2,1));
      
      analysisSettingsGroup = new Group(parent, SWT.NONE);
      analysisSettingsGroup.setLayout(new GridLayout(2, false));
      analysisSettingsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
      analysisSettingsGroup.setText("Analyseeinstellungen");
      if(analyze.getSelection()) {
          analysisSettingsGroup.setEnabled(true);
      }
      else {
          analysisSettingsGroup.setEnabled(false);
      }
//      textGroup2.setEnabled(false);
    
      createLoadtextComposite(textSelectionGroup);
      
      String[] items = { "Deutsch", "Englisch"};
      
      Composite languageAndRestarts = new Composite(analysisSettingsGroup, SWT.NONE);
      languageAndRestarts.setLayout(new GridLayout(4, false));
      languageAndRestarts.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
      
      Group statisticGroup = new Group(analysisSettingsGroup, SWT.NONE);
      statisticGroup.setLayout(new GridLayout(2,false));
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
      
      numberOfRestarts = new Label(languageAndRestarts, SWT.NONE);
      numberOfRestarts.setText("Restarts"); //$NON-NLS-1$
      numberOfRestarts.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false));
      
      restarts = new Spinner(languageAndRestarts, SWT.NONE);
      restarts.setMinimum(1);
      restarts.setMaximum(600);
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
       String[] items = { "Der Gotteswahn - Richard Dawkins", "Irgendein Wikipediaeintrag"};
      
       exampleText = new Button(thirdGroup, SWT.RADIO);
       exampleText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
       exampleText.setText("Beispieltext");
       exampleText.addSelectionListener(new SelectionListener() {
           public void widgetDefaultSelected(SelectionEvent e) {
               widgetSelected(e);
           }

           public void widgetSelected(SelectionEvent e) {
               reset();
               loadText.setEnabled(false);
               chooseExample.setEnabled(true);
               plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
                   
                   @Override
                   public void keyPressed(KeyEvent e) {
                       e.doit = false;
                       plaintext.setText("plaintext");
                       
                   }

                   @Override
                   public void keyReleased(KeyEvent e) {
                       
                   }
               });
           }
       });
       exampleText.setSelection(true);
       
//       Text chooseText = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
//       chooseText.setText("Beispieltext wählen");
//       chooseText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
//       chooseText.setEditable(false);
//       chooseText.setBackground(ColorService.LIGHTGRAY);

      // Create a dropdown Combo
       chooseExample = new Combo(thirdGroup, SWT.DROP_DOWN);
       chooseExample.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
       chooseExample.setItems(items);
       chooseExample.select(0);
       chooseExample.addSelectionListener(new SelectionListener() {
           public void widgetDefaultSelected(SelectionEvent e) {
               widgetSelected(e);
           }

           public void widgetSelected(SelectionEvent e) {
               reset();
               
               if (chooseExample.getSelectionIndex()==0) {
                   String inputPlaintext =  loadNormal("/example2Ger.txt");
                   plaintext.setText(inputPlaintext);
               }
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
               reset();

               loadText.setEnabled(true);
               chooseExample.setEnabled(false);
               plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
                   
                   @Override
                   public void keyPressed(KeyEvent e) {
                       e.doit = false;
//                       plaintext.setText("");
                       
                   }

                   @Override
                   public void keyReleased(KeyEvent e) {
                       
                   }
               });
           }
       });
       
//       Text loadFile = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
//       loadFile.setText("Eigenen Text wählen");
//       loadFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
//       loadFile.setEditable(false);
//       loadFile.setBackground(ColorService.LIGHTGRAY);
      
       loadText = new Button(thirdGroup, SWT.PUSH);
       loadText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
       loadText.setText("Text laden");
       loadText.setEnabled(false);
       
       writeText = new Button(thirdGroup, SWT.RADIO);
       writeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
       writeText.setEnabled(false);
       writeText.addSelectionListener(new SelectionListener() {
           public void widgetDefaultSelected(SelectionEvent e) {
               widgetSelected(e);
           }

           public void widgetSelected(SelectionEvent e) {
               reset();
               loadText.setEnabled(false);
               chooseExample.setEnabled(false);
//               plaintext.setEditable(true);
               plaintext.addKeyListener(new org.eclipse.swt.events.KeyListener() {
                   
                   @Override
                   public void keyPressed(KeyEvent e) {
                       e.doit = true;
//                       plaintext.setText("");
                       
                   }

                   @Override
                   public void keyReleased(KeyEvent e) {
                       
                   }
               });
           }
       });
       
       loadText.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                String filename = openFileDialog(SWT.OPEN);
                int[] a/* = loadNormal(fileName)*/;
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
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
                reset();
                selectStatistic.setEnabled(true);
                loadStatistics.setEnabled(false);
            }
        });
        
//        Text chooseStatistic = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
//        chooseStatistic.setText("Sprachstatistik wählen");
//        chooseStatistic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
//        chooseStatistic.setEditable(false);
//        chooseStatistic.setBackground(ColorService.LIGHTGRAY);

       // Create a dropdown Combo
        selectStatistic = new Combo(thirdGroup, SWT.DROP_DOWN);
        selectStatistic.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        selectStatistic.setItems(items);
        selectStatistic.select(0);

        statisticsLoad = new Button(thirdGroup, SWT.RADIO);
        statisticsLoad.setText("Eigene Statistik");
        statisticsLoad.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                reset();
                selectStatistic.setEnabled(false);
                loadStatistics.setEnabled(true);
            }
        });
        
//        Text loadFile = new Text(thirdGroup, SWT.WRAP | SWT.MULTI);
//        loadFile.setText("Eigene Statistik wählen");
//        loadFile.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
//        loadFile.setEditable(false);
//        loadFile.setBackground(ColorService.LIGHTGRAY);
       
        loadStatistics = new Button(thirdGroup, SWT.PUSH);
        loadStatistics.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        loadStatistics.setText("Statistik laden");     
        loadStatistics.setEnabled(false);
        loadStatistics.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                String filename = openStatFileDialog(SWT.OPEN);
                int[] a/* = loadNormal(fileName)*/;
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
        });       
    }
    
    private void createAnalysisOutput(Composite parent) {
        
        analysis = new Group(parent,  SWT.NONE);
        analysis.setLayout(new GridLayout());
        analysis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        analysis.setText("Ausgabe Analyse");
    
        analysisOutput = new Text(analysis, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        analysisOutput.setLayoutData(gridData);
        analysisOutput.addModifyListener(new ModifyListener() {
          
          @Override
          public void modifyText(ModifyEvent e) {
              plaintextComposite.setText("Klartext (" + plaintext.getText().length() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              reset();
          }
        });
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
    
    private String openStatFileDialog(int type) {
        FileDialog dialog = new FileDialog(getDisplay().getActiveShell(), type);
        dialog.setFilterPath(DirectoryService.getUserHomeDir());
        dialog.setFilterExtensions(new String[] { "*.bin" }); //$NON-NLS-1$
        dialog.setFilterNames(new String[] { "Binary Files (*.bin)" }); //$NON-NLS-1$
        dialog.setOverwrite(true);
        return dialog.open();
}
    
    private /*int[]*/ String loadNormal(String fileName) {
        
//        BufferedReader reader = null;
        String text = "";
        
//        try {
//            reader = new BufferedReader(new FileReader(fileName));
//            int count = 0;
//            String line = reader.readLine();
//            text = line;
            
            try {
                String source = fileName;
                Path sourceFile = Paths.get(source);
                BufferedReader brFile = Files.newBufferedReader(sourceFile, Charset.forName("Cp1252"));
                text=brFile.readLine();
                        
                brFile.close();
                System.out.println("Text type: plaintext from file\nText: "+text);
                
                } catch (Exception e) {
                    System.out.println("An error occured during file handling: " + e);
                    e.printStackTrace();
                    return "File not found";
//                    throw new FileNotFoundException();
                }
            
//            reader.close();
//            while (line  != null) {
//                text += line;
//                line = reader.readLine();
//                
//                //Hier wird die gelesen Zeile für Zeile
//                //hiier füllst du das array
//            }
//        } catch (NumberFormatException nfe) {
//            LogUtil.logError(Activator.PLUGIN_ID, nfe);
//            MessageBox brokenFile = new MessageBox(getDisplay().getActiveShell(), SWT.OK);
//            brokenFile.setText("Loading puzzle encountered a problem"); //$NON-NLS-1$
//            brokenFile.setMessage("Puzzle could not be loaded. There is a wrong character in the loaded file.\n"); //$NON-NLS-1$
//            brokenFile.open();
//           
//        } catch (FileNotFoundException e) {
//            return "text could not be loaded";
//            //Error handling
//
//        } catch (IOException e) {
//            
//            return "text could not be loaded";
//        } finally {
//            try {
////                reader.close();
//            } catch (IOException e) {
//                LogUtil.logError(Activator.PLUGIN_ID, e);
//            }
//        }

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
    
    public void reset() {

        checkOkButton();
        canvasKey.redraw();
    }
    
    public void checkOkButton() {
        if (analyze.getSelection()) {
            
        }else if (encrypt.getSelection()) {
            
            if (model.getKey().isValid() && !plaintext.getText().equals("")) { //$NON-NLS-1$
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            }
        }else {
            
            if (model.getKey().isValid() && !ciphertext.getText().equals("")) { //$NON-NLS-1$
                start.setEnabled(true);
            } else {
                start.setEnabled(false);
            }
            
        }  
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
