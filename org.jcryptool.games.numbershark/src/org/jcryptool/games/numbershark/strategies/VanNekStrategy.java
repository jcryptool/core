package org.jcryptool.games.numbershark.strategies;


import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jcryptool.games.numbershark.views.NumberField;

public class VanNekStrategy {

	private String[][] output = null;
	private int stoppedAt;
	private int von = 2;

	public VanNekStrategy(int von, int bis, IProgressMonitor monitor){
		this.von = von;

		output = new String[bis + 1 - von][5];
		for (int gameSize = von; gameSize <= bis; gameSize++) {
			if (!monitor.isCanceled()) {
				long start = System.currentTimeMillis();
				calculate(gameSize);

				long stop = System.currentTimeMillis();
				int t = (int) (stop - start);
				String zeit = t < 1000 ? t + "ms" : (t / 1000) + "s";

				output[gameSize - von][4] = zeit;
				stoppedAt = gameSize;
			}
		}
	}

	private void calculate(int gameSize) {
		NumberField numberField = new NumberField(gameSize);
		int playerPoints = 0;
		String takeSeq = "";
		while (numberField.getActiveNumbers() > 0) {
			int k = step1(numberField);
			while(k != -1){
				numberField.take(k);
				takeSeq += "" + k + ",";
				playerPoints+=k;
				k = step1(numberField);
				
			}
			k = step2(numberField);
			if(k == -1){
				break;
			}
			numberField.take(k);
			takeSeq += "" + k + ",";
			playerPoints += k;
			
		}


		takeSeq = takeSeq.substring(0, takeSeq.length() - 1);
		int sharkPoints = gameSize * (gameSize + 1) / 2 - playerPoints;
		output[gameSize - von][0] = "" + gameSize;
		output[gameSize - von][1] = "" + playerPoints;
		output[gameSize - von][2] = "" + sharkPoints;
		output[gameSize - von][3] = takeSeq;
	}


	private int step1(NumberField numberField){
		int gameSize = numberField.getGameSize();
		for(int i = gameSize; i > gameSize/2; i--){
			if(numberField.getActiveDivisors(i).size() == 1){
				return i;
			}
		}
		return -1;
	}
	
	private int step2(NumberField numberField){
		ArrayList<Integer> chooseableNumbers = new ArrayList<Integer>();
		int gameSize = numberField.getGameSize();
		for(int i = numberField.getGameSize(); i > gameSize/2; i--){
			if(numberField.getActiveDivisors(i).size() == 2){
				chooseableNumbers.add(i);
			}
		}
		for(int i = gameSize/2; i > 0; i--){
			if(numberField.getActiveDivisors(i).size() == 1){
				chooseableNumbers.add(i);
				break;
			}
		}
		if(chooseableNumbers.size() > 0){
			java.util.Random random = new java.util.Random();
			int k = random.nextInt(chooseableNumbers.size());

			return chooseableNumbers.get(k);
		}
		
		return -1;
	}
	
	public int getStoppedAt() {
		return stoppedAt;
	}

	public String[][] getOutput() {
		return output;
	}

}
