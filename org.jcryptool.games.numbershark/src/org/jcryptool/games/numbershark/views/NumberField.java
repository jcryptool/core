package org.jcryptool.games.numbershark.views;

import java.util.ArrayList;

public class NumberField {
	private int gameSize;
	private Number[] numberField;

	public NumberField(int gameSize) {
		numberField = new Number[gameSize];
		for (int i = 0; i < gameSize; i++) {
			numberField[i] = new Number(i + 1);
		}
		this.gameSize = gameSize;
	}

	public void take(int n) {
			if (numberField[n - 1].isEnabled()) {
				numberField[n - 1].setEnabled(false);
				ArrayList<Integer> divisors = numberField[n - 1].getDivisors();

				for (int i = 0; i < divisors.size(); i++) {
					int divisor = divisors.get(i);
					if (numberField[divisor - 1].isEnabled()) {
						numberField[divisor - 1].setEnabled(false);
					}
				}
			}
		
	}

	public Number[] getNumberField() {
		return numberField;
	}

	public int getActiveNumbers() {
		int activeNumbers = 0;
		for(int i = 1; i < gameSize+1; i++){
			if(getActiveDivisors(i).size()>= 1){
				activeNumbers++;
			}
		}
		return activeNumbers;
	}

	public boolean isActiveNumber(int n) {
		return numberField[n - 1].isEnabled();
	}

	public int getGameSize() {
		return gameSize;
	}

	public ArrayList<Integer> getActiveDivisors(int n) {
		ArrayList<Integer> allDivisors = numberField[n - 1].getDivisors();

		ArrayList<Integer> divisors = new ArrayList<Integer>();

		for (int i = 0; i < allDivisors.size(); i++) {
			int divisor = allDivisors.get(i);
			if (numberField[divisor - 1].isEnabled()) {
				divisors.add(divisor);
			}
		}
		return divisors;
	}

	public int getActiveDivsorsSum(int n) {
		ArrayList<Integer> divisors = getActiveDivisors(n);
		int sum = 0;
		for (int i = 0; i < divisors.size(); i++) {
			sum += divisors.get(i);
		}
		return sum;
	}

	public int getNumberOfActDivs(int n) {
		ArrayList<Integer> divisors = getActiveDivisors(n);
		return divisors.size();
	}
}
