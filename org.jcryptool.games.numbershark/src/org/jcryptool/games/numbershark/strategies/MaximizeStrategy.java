package org.jcryptool.games.numbershark.strategies;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jcryptool.games.numbershark.views.NumberField;

public class MaximizeStrategy {

	private String[][] output = null;
	private int stoppedAt;
	private int von = 2;

	public MaximizeStrategy(int von, int bis, IProgressMonitor monitor){
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
			int k = 0;
			int maxWin = 0;
			for (int i = gameSize; i > 0; i--) {
				if (numberField.isActiveNumber(i)
						&& numberField.getNumberOfActDivs(i) > 0) {
					int sumOfDivs = numberField.getActiveDivsorsSum(i);

					int win = i - sumOfDivs;
					if (win > maxWin) {
						k = i;
						maxWin = win;
					}
				}
			}
			if (k == 0) {
				break;
			}
			numberField.take(k);
			playerPoints += k;
			takeSeq += "" + k + ",";
		}

		takeSeq = takeSeq.substring(0, takeSeq.length() - 1);

		int sharkPoints = gameSize * (gameSize + 1) / 2 - playerPoints;
		output[gameSize - von][0] = "" + gameSize;
		output[gameSize - von][1] = "" + playerPoints;
		output[gameSize - von][2] = "" + sharkPoints;
		output[gameSize - von][3] = takeSeq;
	}

	public int getStoppedAt() {
		return stoppedAt;
	}

	public String[][] getOutput() {
		return output;
	}

}
