package org.jcryptool.analysis.fleissner2;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class FleissnerGrilleSolver {
	
	private static final Logger log = Logger.getLogger( FleissnerGrilleSolver.class.getName() );
	
	public static void main(String[] args) {
		
//		args = new String[6];
		
//		for (int i=14; i<21;i++) {
//			log.info("Evaluation für i="+i+"\n");
			for (int j=1; j<21; j++) {
				log.info("Anfang Durchlauf: "+j+"\n");
				
//				args[0] = "-method";
//				args[1] = "analyze";
//				args[2] = "-key";
//				args[3] = "16,1,5,16,6,14,16,2,12,5,7,13,9,9,13,9,17,16,12,2,7,8,1,3,15,8,14,12,0,17,5,10,4,12,16,8,16,9,13,4,6,17,12,17,0,14,11,12,8,5,15,1,0,15,13,6,4,16,15,6,6,8,1,10,12,11,0,16,14,9,0,10,15,14,13,3,17,15,15,3,3,1,4,5,15,2,8,15,6,11,10,7,6,16,14,4,10,8,16,4,2,4,10,2,0,8,11,7,16,10,13,15,0,5,8,4,14,3,0,4,5,7,9,11,14,7,17,6,6,15,17,10,2,12,0,13,0,3,7,2,8,3,7,4,9,5,3,12,16,6,10,6,0,9,6,3,11,4,1,12,3,7";
//				args[4] = "-dataCryptedText";
//				args[5] = "src/logic/example2GerEncryptedLength18.txt";
//				args[2] = "-keyLength";
//				args[3] = String.valueOf(i);
//				args[4] = "-restarts";
//				args[5] = "5";

				ParameterSettings ps = null;
				MethodApplication ma = null;
				String method = null;
				
//				assign users values to parameters
				try {
//					Configuration of given parameters and selecting and applying one of the three methods
					ps = new ParameterSettings(args);
					ma = new MethodApplication(ps);
					method = ps.getMethod();
					
				} catch (InvalidParameterCombinationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("Please enter valid parameters\nFehler in Main Methode!!!!");
					return;
				}catch(FileNotFoundException ex) {
					ex.printStackTrace();
					log.info("File not found !");
					return;
				}
				
				switch(method) {
				case "analyze": ma.analyze();
								break;
				case "encrypt": ma.encrypt();
								break;
				case "decrypt": ma.decrypt();
								break;
				case "keyGenerator": ma.keyGenerator();
								break;
				}
				log.info("To String method of Method Application in fgSolver: \n"+ma.toString());
				log.info("Ende Durchlauf: "+j+"\n");
			}
		}
//	}
}
