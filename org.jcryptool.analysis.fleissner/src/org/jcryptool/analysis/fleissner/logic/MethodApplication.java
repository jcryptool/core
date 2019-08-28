package org.jcryptool.analysis.fleissner.logic;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.analysis.fleissner.UI.FleissnerWindow;


/**
 * @author Dinah
 *
 */
public class MethodApplication{

	private static final Logger log = Logger.getLogger( FleissnerGrilleSolver.class.getName() );
	
    private ArrayList<Integer> possibleTemplateLengths = new ArrayList<>();

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
	
//	parameters for analysis
    private double value, oldValue, alltimeLow=Double.MAX_VALUE;
    private int changes=0, iAll=0, grilleNumber=0, improvement = 0; 
    private int x,y,move;
    private int[] bestTemplate=null;
    private String lastImprovement = null, bestDecryptedText = "", procedure = "";
	
    private Text fwAnalysisOutput;
	
	public MethodApplication(ParameterSettings ps, Text analysisOutput, double argStatistics[]) throws FileNotFoundException {
        // applies parameter settings from ParameterSettings and sets and executes method
        this.fwAnalysisOutput = analysisOutput;
        this.method = ps.getMethod();
        this.encryptedText = ps.getEncryptedText();
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
		if (method.equals("analyze")) {
			try {
				this.tv = new TextValuator(statistics, language, nGramSize);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new FileNotFoundException("File not found !");
			}
			if ((this.grille == null) && (isPlaintext)) {
				// Build a random grille
//			    fw.setAnalysisOutput("Grillvalue: "+String.valueOf(grille)+", \nrandom grille will be created\n");
			    analysisOutput.append("Grillvalue: "+String.valueOf(grille)+", \nrandom grille will be created\n");
				log.info("Grillvalue: "+String.valueOf(grille)+", \nrandom grille will be created");
				this.grille = new int[2*holes];	
				fg.clearGrille();
				String key = "";
				
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
				key += x+",";
				grille[(2*i)+1] = y;
				key += y+",";
				}
				fwAnalysisOutput.append("random grille: "+key+" created with\nTextInLine:\n"+textInLine+"\nisPlaintext: "+isPlaintext+"\nkeyLength: "+templateLength+"\n");
				log.info("random grille: "+key+" created with\nTextInLine:\n"+textInLine+"\nisPlaintext: "+isPlaintext+"\nkeyLength: "+templateLength);
			}
		}
	}		
	
	public void analyze () {
		
//		Executes one of the methods Brute-Force or Hill-Climbing dependent on templateLength

	    start = System.currentTimeMillis();
	    fwAnalysisOutput.append("Start analysis \n");
	    log.info("Start analysis ");
	    
	    if (this.possibleTemplateLengths.isEmpty()) {
	    	
			ct.load(textInLine, isPlaintext, templateLength, grille, fg);
			fwAnalysisOutput.append("INFO:\n"+ct.toString()+"\n");
			log.info("INFO:\n"+ct.toString());
			if (templateLength<5){
//				Brute Force for up to 4 x 4 grilles. Creates all possibles grilles and evaluates each text decrypted by them
				procedure = "Brute-Force";
				fwAnalysisOutput.append("Start "+procedure+"\n");
				log.info("Start "+procedure);
				this.bruteForce();
				fwAnalysisOutput.append("\nFinished "+procedure+"\n");
				log.info("\nFinished "+procedure);
			}
			else {
//				Hill-Cimbing for templates from 5 x 5. creates random grilles and evaluates those and slightly variations of them before
//				trying new random grilles.
				procedure = "Hill-Climbing";
				fwAnalysisOutput.append("Start "+procedure+"\n");
				log.info("Start "+procedure);
				this.hillClimbing();
				fwAnalysisOutput.append("\nFinished "+procedure+"\n");
				log.info("\nFinished "+procedure);
			}
	    }else {
	    	double tempvalue = Double.MAX_VALUE, relativeValue, tempRelativeValue = Double.MAX_VALUE;
	    	int[] tempTemplate=null;
	    	int tempLength=0, tempTry=0;
	    	String tempProcedure=null, tempLastImprovement=null;
	    	
	    	for (int length : this.possibleTemplateLengths) {
	    		this.templateLength = length;
	    		fwAnalysisOutput.append("Try with length: "+templateLength+"\n");
	    		log.info("Try with length: "+templateLength);
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
				fwAnalysisOutput.append("INFO:\n"+ct.toString()+"\n");
				log.info("INFO:\n"+ct.toString());
	    		if (templateLength<5){
//	    			Brute Force for up to 4 x 4 grilles. Creates all possibles grilles and evaluates each text decrypted by them
	    			procedure = "Brute-Force";
//	    			fwAnalysisOutput.append("Start "+procedure);
	    			log.info("Start "+procedure);
	    			this.bruteForce();
//	    			fwAnalysisOutput.append("\nFinished "+procedure);
	    			log.info("\nFinished "+procedure);
	    		}
	    		else {
//	    			Hill-Cimbing for templates from 5 x 5. creates random grilles and evaluates those and slightly variations of them before
//	    			trying new random grilles.
	    			procedure = "Hill-Climbing";
	    			fwAnalysisOutput.append("Start "+procedure);
	    			log.info("Start "+procedure);
	    			this.hillClimbing();
	    			fwAnalysisOutput.append("\nFinished "+procedure);
	    			log.info("\nFinished "+procedure);
	    		}
	    		relativeValue = alltimeLow/fg.decryptText(ct.getText()).length();
	    		if (relativeValue<tempRelativeValue) {
	    		    fwAnalysisOutput.append("value has been changed. Relative value is: "+relativeValue+" relative to "+alltimeLow+" with length "+length);
	    			log.info("value has been changed. Relative value is: "+relativeValue+" relative to "+alltimeLow+" with length "+length);
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
	    		bestDecryptedText = "";
	    		procedure = "";
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
	
	public void bruteForce() {

//		clears grilles in FleissnerGrille from potential earlier encryptions before building new ones
		fg.clearGrille();
		ArrayList<int[]> templateList = fg.bruteForce(templateLength, holes);
		if (templateList.isEmpty()) {
		    fwAnalysisOutput.append("templateList is empty");
			log.info("templateList is empty");
			return;
		}
		fwAnalysisOutput.append("sift through all templates");
		log.info("sift through all templates");
		// using every possible template / Grille
		for (int[] template : templateList) {
				
			iAll++;
			fg.useTemplate(template, templateLength);
			decryptedText = fg.decryptText(ct.getText());
			value = tv.evaluate(decryptedText);
			if (value < alltimeLow)
			{
//				better grille found, saves new template
				alltimeLow = value;
				bestTemplate = template;
				grilleNumber = iAll;
			}	
			fwAnalysisOutput.append("\n\nGrille: "+iAll+fg);
            fwAnalysisOutput.append("Accurateness: " + value + " (best: "+alltimeLow+")");
            fwAnalysisOutput.append("Decrypted text:\n ==> "+decryptedText+"\n");
			log.info("\n\nGrille: "+iAll+fg);
			log.info("Accurateness: " + value + " (best: "+alltimeLow+")");
			log.info("Decrypted text:\n ==> "+decryptedText+"\n");
		}
		fg.useTemplate(bestTemplate, templateLength);	
	}
	
	public void hillClimbing() {

		tries = restart;
		sub = BigInteger.valueOf(1);
		
		do {
//			clears grilles in FleissnerGrille from potential earlier encryptions before building a random new one
			fg.clearGrille();
			oldValue = Double.MAX_VALUE;
			double min;
			int minX=0, minY=0, minMove=0;
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
			fwAnalysisOutput.append("Restart: "+sub);
			log.info("Restart: "+sub/*+", fleissnergrille to string (random): "+fg*/);
			decryptedText = fg.decryptText(ct.getText());
			min = tv.evaluate(decryptedText);
			fwAnalysisOutput.append("Decrypted Text: "+decryptedText+"\nwith value: "+String.valueOf(min));
			log.info("Decrypted Text: "+decryptedText+"\nwith value: "+String.valueOf(min));
			
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
						fwAnalysisOutput.append("\nbest grille yet with "+changes+" changes, where value is " +alltimeLow+"\n"+fg+"\n");
						log.info("best grille yet with "+changes+" changes, where value is " +alltimeLow+"\n"+fg);
					}
					fwAnalysisOutput.append("\ntry: " + iAll + ", changes: "+changes + " (last at: " + grilleNumber + "in restart: "+lastImprovement+"), accurateness: " + min + " (best: "+oldValue+", alltime: "+alltimeLow+")\n");
                    fwAnalysisOutput.append("\n==> "+decryptedText+"\n Grille: \n"+fg+"\n");
					log.info("try: " + iAll + ", changes: "+changes + " (last at: " + grilleNumber + "in restart: "+lastImprovement+"), accurateness: " + min + " (best: "+oldValue+", alltime: "+alltimeLow+")");
					log.info("==> "+decryptedText+"\n Grille: \n"+fg);
				}
			} while (Math.abs(iAll-improvement)<1);
			tries = restart.subtract(sub);
			sub = sub.add(BigInteger.valueOf(1));
			improvement = 0;
			iAll = 0;
			
		}while(tries.compareTo(BigInteger.valueOf(0))==1);
		
		fg.useTemplate(bestTemplate, templateLength);
		int rotMove = 0;
//		checks all 4 rotation positions of the found grille
		for (move=1; move<=4; move++)
		{
			fg.rotate();
			decryptedText = fg.decryptText(ct.getText());
			value = tv.evaluate(decryptedText);
						
			if (value < alltimeLow)
			{
				alltimeLow = value;
				rotMove = move;
				improvement++;
			}							
		}
		if (improvement != 0) {
			
//			log.info("Improvement by "+rotMove+" rotation(s).");		
			for (int moves = 1 ; moves <= rotMove; moves++) {
				fg.rotate();
			}
			bestTemplate = fg.saveTemplate(holes);
		}
	}
	
//	encrypts given plaintext with given key directly through load method of class CryptedText
	public void encrypt(){

		ct.load(textInLine, isPlaintext, templateLength, grille, fg);
		encryptedText = "";
		for(char[][]textPart:ct.getText()) {
			for (int y = 0; y < templateLength; y++) {
				for (int x = 0; x < templateLength; x++) {
					encryptedText += textPart[x][y];
				}
			}
		}
	}
	
//	decrypts given crypted text with given key by decryption method of class FleissnerGrille
	public void decrypt(){

		ct.load(textInLine, isPlaintext, templateLength, grille, fg);
		fg.useTemplate(grille, templateLength);
		decryptedText = fg.decryptText(ct.getText());
		fwAnalysisOutput.append("Crypted text succesfully decrypted");
		log.info("Crypted text succesfully decrypted");
	}
	
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

    public String toString() {
		
		String output= null;
		String bestTemplateCoordinates = "";
		
		switch(this.method) {
		
		case "analyze":
			bestDecryptedText = fg.decryptText(ct.getText());
			value = tv.evaluate(bestDecryptedText);
			for (int i = 0; i<bestTemplate.length;i++) {
				bestTemplateCoordinates+=bestTemplate[i];
			}
			output = "\nBest grille: "+bestTemplateCoordinates+"\n"+fg+"\nwith length: "+templateLength+" found at try: "+grilleNumber+", in Restart: "+lastImprovement;
			output += "\nDecrpyted text:\n\n"+bestDecryptedText+"\n\n";
//			output += "Procedure: "+procedure+"\n";
//			output += "Accurateness: " + value+" (where alltimelow is "+alltimeLow+ ")\n";
			output += "Finished analysis in "+end+" miliseconds";
				
			break;
		case "encrypt":
			output = "\nEncrypted text:\n\n"+encryptedText+",\nencrypted with key:"+fg;
			for (int i = 0; i<grille.length;i++) {
				bestTemplateCoordinates+=grille[i];
			}
			output += "\nKey coordinates: "+bestTemplateCoordinates;
			output += "\nwith length: "+templateLength;
			break;
		case "decrypt":
			output = "\nDecrypted text:\n\n"+decryptedText+", decryption with key:"+fg;
			for (int i = 0; i<grille.length;i++) {
				bestTemplateCoordinates+=grille[i];
			}
			output += "\nKey coordinates: "+bestTemplateCoordinates;
			output += "\nwith length: "+templateLength;
			break;
		case "keyGenerator":
			output = "Key: "+fg;
			for (int h=0;h<grille.length;h++) {
				output += grille[h]+",";
			}
			output += "\nwith length: "+templateLength;
			fg.clearGrille();
		}
		return output;
	}
}
