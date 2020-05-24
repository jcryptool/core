// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.fleissner.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.jcryptool.analysis.fleissner.Activator;
import org.jcryptool.analysis.fleissner.UI.FleissnerWindow;
import org.jcryptool.core.logging.utils.LogUtil;


/**
 * @author Dinah
 *
 */
public class MethodApplication{
    
    private ArrayList<Integer> possibleTemplateLengths = new ArrayList<>();
    private ArrayList<String> analysisOut = new ArrayList<>();

//  parameters given by user or default
    private String method, decryptedText, encryptedText, textInLine, language;
    private int templateLength, holes, nGramSize;
    private int[] grille;
    private BigInteger restart, tries, sub;
    private boolean isPlaintext;
    private CryptedText ct;
    private FleissnerGrille fg;
    private TextValuator tv;
    private long start, end;
    private double statistics[];
    
//  parameters for analysis
    private double value, oldValue, alltimeLow=Double.MAX_VALUE;
    private int changes=0, iAll=0, grilleNumber=0, improvement = 0, rotMove = 0;; 
    private int x,y,move;
    private int[] bestTemplate=null;
    private String lastImprovement = null, bestDecryptedText = Messages.MethodApplication_empty, procedure = Messages.MethodApplication_empty, change =Messages.MethodApplication_empty;
    
    private String fwAnalysisOutput;
    
    /**
     * applies parameter settings from ParameterSettings and sets and executes method
     * 
     * @param ps object parameter settings 
     * @param analysisOutput the text field in FleissnerWindow where analysis progress is displayed
     * @param argStatistics
     * @throws FileNotFoundException
     */
    public MethodApplication(ParameterSettings ps, double argStatistics[]) throws FileNotFoundException {
        
        this.fwAnalysisOutput = new String(Messages.MethodApplication_empty);
        this.method = ps.getMethod();
        this.textInLine = ps.getTextInLine();
        this.templateLength = ps.getTemplateLength();
        if (!ps.getPossibleTemplateLengths().isEmpty()) {
            this.possibleTemplateLengths = ps.getPossibleTemplateLengths();
        }
        this.isPlaintext = ps.isPlaintext();
        this.holes = ps.getHoles();
        this.grille = ps.getGrille();
        this.restart = ps.getRestart(); 
        this.language = ps.getLanguage();
        this.statistics = argStatistics;
        this.nGramSize = ps.getnGramSize();
        this.ct = new CryptedText();
        this.fg = new FleissnerGrille(templateLength);
        if (method.equals(Messages.MethodApplication_method_analyze)) {
            try {
                this.tv = new TextValuator(statistics, language, nGramSize);
            } catch (FileNotFoundException e) {
                LogUtil.logError(Activator.PLUGIN_ID, Messages.MethodApplication_error_statFileNotFound, e, true);
                throw new FileNotFoundException(Messages.MethodApplication_error_fileNotFound);
            }
        }
    }       
    
    /**
     * Executes one of the methods Brute-Force or Hill-Climbing dependent on templateLength
     */
    public void analyze () {

        start = System.currentTimeMillis();
        fwAnalysisOutput += Messages.MethodApplication_output_StartAnalysis;
        LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_StartAnalysis);
        
        if (this.possibleTemplateLengths.isEmpty()) {
            
            ct.load(textInLine, isPlaintext, templateLength, grille, fg);
            fwAnalysisOutput += Messages.MethodApplication_output_info+ct.toString();
            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_info+ct.toString());
            if (templateLength<5){
//              Brute Force for up to 4 x 4 grilles. Creates all possibles grilles and evaluates each text decrypted by them
                procedure = Messages.MethodApplication_procedure_bruteForce;
                fwAnalysisOutput += Messages.MethodApplication_output_startProcedure+procedure;
                LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_startProcedure+procedure);
                this.bruteForce();
                fwAnalysisOutput += Messages.MethodApplication_output_finishedProcedure+procedure;
                LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_finishedProcedure+procedure);
            }
            else {
//              Hill-Cimbing for templates from 5 x 5. creates random grilles and evaluates those and slightly variations of them before
//              trying new random grilles.
                procedure = Messages.MethodApplication_procedure_hillClimbing;
                fwAnalysisOutput += Messages.MethodApplication_output_startProcedure+procedure;
                LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_startProcedure+procedure);
                this.hillClimbing();
                fwAnalysisOutput += Messages.MethodApplication_output_finishedProcedure+procedure;
                LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_finishedProcedure+procedure);
            }
        }else {
//          if no key size is given, key size will be narrowed down to the only possible ones and those will be analyzed
            double tempvalue = Double.MAX_VALUE, relativeValue, tempRelativeValue = Double.MAX_VALUE;
            int[] tempTemplate=null;
            int tempLength=0, tempTry=0;
            String tempProcedure=null, tempLastImprovement=null;
            
            for (int length : this.possibleTemplateLengths) {
                this.templateLength = length;
                fwAnalysisOutput += Messages.MethodApplication_output_tryNewLength+templateLength;
                LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_tryNewLength+templateLength);
                if (templateLength%2==0) {
                    this.holes = (int) (Math.pow(templateLength, 2))/4;
                }
                else {
                    this.holes = (int) (Math.pow(templateLength, 2)-1)/4;
                }
                this.fg = new FleissnerGrille(templateLength);
                this.ct = new CryptedText();
                this.grille = null;
                this.isPlaintext = false;
                ct.load(textInLine, isPlaintext, templateLength, grille, fg);
                fwAnalysisOutput += Messages.MethodApplication_output_info+ct.toString();
                LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_info+ct.toString());
                if (templateLength<5){
//                  Brute Force for up to 4 x 4 grilles. Creates all possibles grilles and evaluates each text decrypted by them
                    procedure = Messages.MethodApplication_procedure_bruteForce;
                    fwAnalysisOutput += Messages.MethodApplication_output_startProcedure+procedure;
                    LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_startProcedure+procedure);
                    this.bruteForce();
                    fwAnalysisOutput += Messages.MethodApplication_output_finishedProcedure+procedure;
                    LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_finishedProcedure+procedure);
                }
                else {
//                  Hill-Cimbing for templates from 5 x 5. creates random grilles and evaluates those and slightly variations of them before
//                  trying new random grilles.
                    procedure = Messages.MethodApplication_procedure_hillClimbing;
                    fwAnalysisOutput += Messages.MethodApplication_output_startProcedure+procedure;
                    LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_startProcedure+procedure);
                    this.hillClimbing();
                    fwAnalysisOutput += Messages.MethodApplication_output_finishedProcedure+procedure;
                    LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_finishedProcedure+procedure);
                }
//              sets value in relation to size
                relativeValue = alltimeLow/fg.decryptText(ct.getText()).length();
                if (relativeValue<tempRelativeValue) {
                    fwAnalysisOutput += Messages.MethodApplication_output_relativeValue+myRound(relativeValue/*, 2*/)+Messages.MethodApplication_output_relativeTo+myRound(alltimeLow/*, 2*/)+Messages.MethodApplication_output_length+length;
                    LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_relativeValue+myRound(relativeValue/*, 2*/)+Messages.MethodApplication_output_relativeTo+myRound(alltimeLow/*, 2*/)+Messages.MethodApplication_output_length+length);
                    tempRelativeValue = relativeValue;
                    tempvalue = alltimeLow;
                    tempTemplate = bestTemplate;
                    tempLength = templateLength;
                    tempLastImprovement = lastImprovement;
                    tempProcedure = procedure;
                    tempTry = grilleNumber;
                }
                alltimeLow=Double.MAX_VALUE;
                changes=0;
                iAll=0;
                grilleNumber=0;
                improvement = 0;
                bestTemplate=null;
                lastImprovement = null;
                bestDecryptedText = Messages.MethodApplication_empty;
                procedure = Messages.MethodApplication_empty;
            }
            alltimeLow = tempvalue;
            bestTemplate = tempTemplate;
            this.templateLength = tempLength;
            lastImprovement = tempLastImprovement;
            procedure = tempProcedure;
            grilleNumber = tempTry;
            
            this.fg = new FleissnerGrille(templateLength);
            this.ct = new CryptedText();
            ct.load(textInLine, isPlaintext, tempLength, grille, fg);
            fg.useTemplate(bestTemplate, templateLength);
        }
        end = System.currentTimeMillis() - start; 
    }
    
    /**
     * bruteForce() method tries every possible grille with current key size and chooses the one that generates the best text value
     */
    public void bruteForce() {

//      clears grilles in FleissnerGrille from potential earlier encryptions before building new ones
        fg.clearGrille();
        ArrayList<int[]> templateList = fg.bruteForce(templateLength, holes);
        if (templateList.isEmpty()) {
            fwAnalysisOutput += Messages.MethodApplication_output_emptyList;
            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_emptyList);
            return;
        }
        fwAnalysisOutput += Messages.MethodApplication_output_siftThroughTemplates;
        LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_siftThroughTemplates);
        // using every possible template / Grille
        for (int[] template : templateList) {
                
            iAll++;
            fg.useTemplate(template, templateLength);
            decryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(decryptedText);
            if (value < alltimeLow)
            {
//              better grille found, saves new template
                alltimeLow = value;
                bestTemplate = template;
                grilleNumber = iAll;
            }   
            
            fwAnalysisOutput += Messages.MethodApplication_output_grille+iAll+Messages.FleissnerGrille_break+fg+Messages.MethodApplication_break+fg.templateToString(holes);
            fwAnalysisOutput += Messages.MethodApplication_output_acurateness + myRound(value) +" (" +Messages.MethodApplication_output_best+myRound(alltimeLow)+")";
            fwAnalysisOutput += Messages.MethodApplication_output_decrypted+decryptedText+Messages.MethodApplication_break;

            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_grille+iAll+Messages.FleissnerGrille_break+fg+Messages.FleissnerGrille_break+fg.templateToString(holes));
            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_acurateness + myRound(value) +" ("+ Messages.MethodApplication_output_best+myRound(alltimeLow)+")");
            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_decrypted+decryptedText+Messages.MethodApplication_break);
            
            String lastLine = fwAnalysisOutput.substring(fwAnalysisOutput.lastIndexOf("\n"));
            
            analysisOut.add(fwAnalysisOutput);
            this.fwAnalysisOutput = new String(Messages.MethodApplication_empty);
            
            String visualDivide = Messages.MethodApplication_break;
            for (int i=0;i<lastLine.length()*2;i++)
                visualDivide+="-";
            LogUtil.logInfo(Activator.PLUGIN_ID, visualDivide+Messages.MethodApplication_break); //$NON-NLS-1$

        }
        fg.useTemplate(bestTemplate, templateLength);   
    }
    
    /**
     * hillClimbing method is the analysis method for grilles of size 5 and higher. It creates random grilles in respective size
     * and makes step by step changes to improve text value. If local maximum is reached a new random grille will be created.
     * This process goes on until the number of 'restarts' is reached and the grille that created the best valued text will be
     * chosen as the right one.
     */
    public void hillClimbing() {

        tries = restart;
        sub = BigInteger.valueOf(1);
        
        do {
//          clears grilles in FleissnerGrille from potential earlier encryptions before building a random new one
            fg.clearGrille();
//          start with highest possible value so process will minimize value from the beginning
            oldValue = Double.MAX_VALUE;
            double min;
            int minX=0, minY=0, minMove=0;
//          create random grille
            for(int i=0; i<holes; i++)
            {
                do
                {
                    x = ThreadLocalRandom.current().nextInt(0, templateLength);
                    y = ThreadLocalRandom.current().nextInt(0, templateLength);
                }
                while (!fg.isPossible(x, y));

            fg.setState(x, y, true);
            }
            fwAnalysisOutput += Messages.MethodApplication_output_restart+sub;
            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_restart+sub);
            decryptedText = fg.decryptText(ct.getText());
            min = tv.evaluate(decryptedText);
            fwAnalysisOutput += Messages.MethodApplication_output_decrypted2+decryptedText+Messages.MethodApplication_output_value+myRound(min/*, 2*/);
            LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_decrypted2+decryptedText+Messages.MethodApplication_info_value+myRound(min/*, 2*/));
            
            do{
                iAll++;
                // calculate the best possible solution if only changing one of the cells
                for (x=0; x<templateLength; x++)
                {
                    for (y=0; y<templateLength; y++)
                    {
                        if (fg.isFilled(x, y))
                        {
                            for (move=1; move<=3; move++)
                            {
//                              try other 3 positions of every hole in grille
                                fg.change(x, y, move);
                                decryptedText = fg.decryptText(ct.getText());
                                value = tv.evaluate(decryptedText);
                                fg.undoChange(x, y, move);
                                
                                if (value < min)
                                {
                                    min = value;
                                    minX = x;
                                    minY = y;
                                    minMove = move;
                                }   
                            }
                        }
                    }
                }
                
                if (min < oldValue)
                {
                    // we found a better solution by changing one of the cells
                    // go on with this new solution
                    fg.change(minX, minY, minMove);
                    oldValue = min;
                    decryptedText = fg.decryptText(ct.getText());
                    improvement++;
                    if (oldValue<alltimeLow) 
                    {
                        alltimeLow=oldValue;
                        grilleNumber=iAll;
                        bestDecryptedText = fg.decryptText(ct.getText());
                        bestTemplate = fg.saveTemplate(holes);
                        lastImprovement = String.valueOf(sub);
                        changes++;
                        fwAnalysisOutput += Messages.MethodApplication_output_bestGrilleYet+changes+" "+countChanges()+Messages.MethodApplication_output_costFunctionValue +myRound(alltimeLow)+Messages.MethodApplication_break+fg+Messages.MethodApplication_break+fg.templateToString(holes); //$NON-NLS-2$ //$NON-NLS-4$ //$NON-NLS-5$
                        LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_bestGrilleYet+changes+" "+countChanges()+Messages.MethodApplication_output_costFunctionValue +myRound(alltimeLow)+Messages.MethodApplication_break+fg+Messages.MethodApplication_break+fg.templateToString(holes)); //$NON-NLS-2$ //$NON-NLS-4$
                    } 
                    fwAnalysisOutput += Messages.MethodApplication_output_try + iAll + Messages.MethodApplication_output_changes+changes + " ("+Messages.MethodApplication_output_lastChange + grilleNumber + Messages.MethodApplication_output_inRestart+lastImprovement+")"+Messages.MethodApplication_output_acurateness2 + myRound(min) + " ("+Messages.MethodApplication_output_best+myRound(oldValue)+Messages.MethodApplication_output_alltime+myRound(alltimeLow)+")"+Messages.MethodApplication_break; //$NON-NLS-8$
                    fwAnalysisOutput += "\n==> "+decryptedText+Messages.MethodApplication_output_grille+fg+Messages.MethodApplication_break+fg.templateToString(holes); //$NON-NLS-1$ //$NON-NLS-3$
                    LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_output_try2 + iAll + Messages.MethodApplication_output_changes+changes +" ("+Messages.MethodApplication_output_lastChange + grilleNumber + Messages.MethodApplication_output_inRestart+lastImprovement+Messages.MethodApplication_output_acurateness2 + myRound(min) +" ("+Messages.MethodApplication_output_best+myRound(oldValue)+Messages.MethodApplication_output_alltime+myRound(alltimeLow)+")"); //$NON-NLS-8$
                    LogUtil.logInfo(Activator.PLUGIN_ID, "==> "+decryptedText+Messages.MethodApplication_info_grille+fg+Messages.MethodApplication_break+fg.templateToString(holes)); //$NON-NLS-1$
                }
            } while (Math.abs(iAll-improvement)<1);
            tries = restart.subtract(sub);
            sub = sub.add(BigInteger.valueOf(1));
            improvement = 0;
            iAll = 0;
            
            String lastLine = fwAnalysisOutput.substring(fwAnalysisOutput.lastIndexOf("\n"));
            analysisOut.add(fwAnalysisOutput);
            this.fwAnalysisOutput = new String(Messages.MethodApplication_empty);
            String visualDivide = Messages.MethodApplication_break;
            for (int i=0;i<lastLine.length()*2;i++)
                visualDivide+="-";
            LogUtil.logInfo(Activator.PLUGIN_ID, visualDivide+Messages.MethodApplication_break); //$NON-NLS-1$

            
//          start next restart
            
        }while(tries.compareTo(BigInteger.valueOf(0))==1);
        
        fg.useTemplate(bestTemplate, templateLength);
//        int rotMove = 0;
//      checks all 4 rotation positions of the found grille for improvement
        this.fwAnalysisOutput+=Messages.MethodApplication_output_rotations;
        for (move=1; move<=4; move++)
        {
            String rotation;
            if (move!=4)
                rotation = Messages.MethodApplication_output_rotationPos+move;
            else
                rotation = Messages.MethodApplication_output_originalPos;
            fg.rotate();
            decryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(decryptedText);
            this.fwAnalysisOutput+=rotation+":\n"+fg+"\n\n"+fg.templateToString(holes)+"\nwith value "+myRound(value)+" and decrypted text:\n\n"+decryptedText+"\n\n";
            analysisOut.add(fwAnalysisOutput);
            this.fwAnalysisOutput = new String(Messages.MethodApplication_empty);            
            
            if (value < alltimeLow)
            {
                alltimeLow = value;
                rotMove = move;
                improvement++;
            }                           
        }
        if (improvement != 0) {
                    
            for (int moves = 1 ; moves <= rotMove; moves++) {
                fg.rotate();
            }
            bestTemplate = fg.saveTemplate(holes);
        }
    }
    
    /**
     * encrypts given plaintext with given key directly through load method of class CryptedText
     */
    public void encrypt(){

        ct.load(textInLine, isPlaintext, templateLength, grille, fg);
        encryptedText = Messages.MethodApplication_empty;
        for(char[][]textPart:ct.getText()) {
            for (int y = 0; y < templateLength; y++) {
                for (int x = 0; x < templateLength; x++) {
                    encryptedText += textPart[x][y];
                }
            }
        }
        LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_encryptionSuccesfull);
    }
    
    /**
     * decrypts given crypted text with given key by decryption method of class FleissnerGrille
     */
    public void decrypt(){

        ct.load(textInLine, isPlaintext, templateLength, grille, fg);
        fg.useTemplate(grille, templateLength);
        decryptedText = fg.decryptText(ct.getText());
        LogUtil.logInfo(Activator.PLUGIN_ID, Messages.MethodApplication_info_decryptionSuccesfull);
    }
    
    /**
     * generates random key and saves it as int array 'grille'
     */
    public void keyGenerator() {
        this.grille = new int[2*holes]; 
        fg.clearGrille();
        
        for(int i=0; i<holes; i++)
        {
            do
            {
                x = ThreadLocalRandom.current().nextInt(0, templateLength);
                y = ThreadLocalRandom.current().nextInt(0, templateLength);
            }
            while (!fg.isPossible(x, y));

            fg.setState(x, y, true);
            grille[2*i] = x;
            grille[(2*i)+1] = y;
        }
    }
    
    public String countChanges() {
        if (this.changes!=1)
            change = Messages.MethodApplication_output_countChanges_pl;
        else
            change = Messages.MethodApplication_output_countChanges_sg;
        return change;
    }
    
    public String countRotations() {
        String rotations = Messages.MethodApplication_empty;
        if (this.rotMove!=1)
            rotations = Messages.MethodApplication_output_countRotations_pl;
        else
            rotations = Messages.MethodApplication_output_countRotations_sg;
        return rotations;
    }
    
    public String countImprovements() {
        String improvements = Messages.MethodApplication_empty;
        if (this.improvement!=1)
            improvements = Messages.MethodApplication_output_countImprovements_pl;
        else
            improvements = Messages.MethodApplication_output_countImprovements_sg;
        return improvements;
    }
  
    public String myRound(double wert) {
        double tempWert = wert;
        if (language.equals("english"))  //$NON-NLS-1$
            tempWert/=0.1E16;
        DecimalFormat formatter = new DecimalFormat("#.##");
        return  formatter.format(tempWert);
    }
    
    /**
     * @return the fg
     */
    public FleissnerGrille getFg() {
        return fg;
    }

    /**
     * @param fg the fg to set
     */
    public void setFg(FleissnerGrille fg) {
        this.fg = fg;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public String getDecryptedText() {
        return decryptedText;
    }
    public String getBestDecryptedText() {
        return bestDecryptedText;
    }

    public int[] getBestTemplate() {
        return bestTemplate;
    }

    /**
     * @return the fwAnalysisOutput
     */
    public String getFwAnalysisOutput() {
        return fwAnalysisOutput;
    }

    /**
     * @param fwAnalysisOutput the fwAnalysisOutput to set
     */
    public void setFwAnalysisOutput(String fwAnalysisOutput) {
        this.fwAnalysisOutput = fwAnalysisOutput;
    }

    /**
     * @return the analysisOut
     */
    public ArrayList<String> getAnalysisOut() {
        return analysisOut;
    }

    @Override
    public String toString() {
        
        String output= null;
        String bestTemplateCoordinates = Messages.MethodApplication_empty;
        
        switch(this.method) {
        
        case "analyze": //$NON-NLS-1$
            String time;
            bestDecryptedText = fg.decryptText(ct.getText());
            value = tv.evaluate(bestDecryptedText);
            for (int i = 0; i<bestTemplate.length;i++) {
                bestTemplateCoordinates+=bestTemplate[i];
            }
            output = Messages.MethodApplication_output_bestGrille+bestTemplateCoordinates+Messages.MethodApplication_break+fg+Messages.MethodApplication_output_length_final+templateLength+Messages.MethodApplication_output_try_final+grilleNumber+Messages.MethodApplication_output_restart_final+lastImprovement+" with value "+myRound(value); //$NON-NLS-2$
            output += Messages.MethodApplication_break+rotMove+countRotations()+improvement+countImprovements();
            output += Messages.MethodApplication_output_decrypted_final+bestDecryptedText+Messages.MethodApplication_doubleBreak; //$NON-NLS-2$
//          adjusts time format depending of spent time for analysis
            if (end<60000)
                time = end+Messages.MethodApplication_outputTime;
            else {
                long timeInDays, timeInHours, timeInMinutes, timeInSeconds;
                
                timeInSeconds = end/1000;
                Duration duration = Duration.ofSeconds(timeInSeconds);

                timeInDays = duration.toDays();
                duration=duration.minusDays(timeInDays);
                timeInHours = duration.toHours();
                duration=duration.minusHours(timeInHours);
                timeInMinutes = duration.toMinutes();
                duration=duration.minusMinutes(timeInMinutes);
                timeInSeconds = duration.getSeconds();
                time = String.format(Messages.MethodApplication_output_timeFormat_in, timeInDays, timeInHours, timeInMinutes, timeInSeconds)+Messages.MethodApplication_output_timeFormat_out;
            }
            output += Messages.MethodApplication_output_finished_final+time;
            break;
        case "encrypt": //$NON-NLS-1$
            output = Messages.MethodApplication_output_encrypted+encryptedText+Messages.MethodApplication_output_encryptionKey+fg;
            for (int i = 0; i<grille.length;i++) {
                bestTemplateCoordinates+=grille[i];
            }
            output += Messages.MethodApplication_output_keyCoordinates+bestTemplateCoordinates;
            output += Messages.MethodApplication_output_length_final+templateLength;
            break;
        case "decrypt": //$NON-NLS-1$
            output = Messages.MethodApplication_output_decrypted_final+decryptedText+Messages.MethodApplication_output_decryptionKey+fg;
            for (int i = 0; i<grille.length;i++) {
                bestTemplateCoordinates+=grille[i];
            }
            output += Messages.MethodApplication_output_keyCoordinates+bestTemplateCoordinates;
            output += Messages.MethodApplication_output_length_final+templateLength;
            break;
        case "keyGenerator":
            output = Messages.MethodApplication_output_key+fg;
            for (int h=0;h<grille.length;h++) {
                output += grille[h]+","; //$NON-NLS-1$
            }
            output += Messages.MethodApplication_output_length_final+templateLength;
            fg.clearGrille();
        }
        return output;
    }
}