package org.jcryptool.visual.aup.PermBruteForce;

public class UnlockPattern {

	private int feldlaenge = 10;
	private int order[] = new int[feldlaenge];
	private boolean isActive[] = new boolean[feldlaenge];

	/**
	 * Constructor initialise
	 */
	public UnlockPattern(){
		init();
	}
	public boolean verify() throws Exception {
		resetIsActive();
		boolean isEnd=false;

		if (order.length != 10) {
			throw new Exception("Internal Error: Wrong length of Array");
		}
		if(!(order[9]==0)){
			return false;
		}
		for (int i = 0; i < order.length - 1; i++) {

			switch (order[i]) {
			case 0:
				isEnd=true;
				// Startpunkt==Endpunkt
				if (i > 0 && order[i - 1] == order[0]) {
					return false;
				}

				// weniger als 4 Punkte
				else if (i < 4) {
					return false;
				}
				break;
			case 1:

				if (isEnd || isActive[1] || (order[i + 1] == 3 && !isActive[2])
						|| (order[i + 1] == 7 && !isActive[4])
						|| (order[i + 1] == 9 && !isActive[5])) {
					return false;
				}

				isActive[1] = true;
				break;
			case 2:
				if (isEnd || isActive[2] || (order[i + 1] == 8 && !isActive[5])) {
					return false;
				}
				isActive[2] = true;
				break;
			case 3:
				if (isEnd || isActive[3] || (order[i + 1] == 1 && !isActive[2])
						|| (order[i + 1] == 7 && !isActive[5])
						|| (order[i + 1] == 9 && !isActive[6])) {
					return false;
				}
				isActive[3] = true;
				break;
			case 4:
				if (isEnd || isActive[4] || (order[i + 1] == 6 && !isActive[5])) {
					return false;
				}
				isActive[4] = true;
				break;
			case 5:
				if (isEnd || isActive[5]) {
					return false;
				}
				isActive[5] = true;
				break;
			case 6:
				if (isEnd || isActive[6] || (order[i + 1] == 4 && !isActive[5])) {
					return false;
				}
				isActive[6] = true;
				break;
			case 7:
				if (isEnd || isActive[7] || (order[i + 1] == 1 && !isActive[4])
						|| (order[i + 1] == 3 && !isActive[5])
						|| (order[i + 1] == 9 && !isActive[8])) {
					return false;
				}
				isActive[7] = true;
				break;
			case 8:
				if (isEnd || isActive[8] || (order[i + 1] == 2 && !isActive[5])) {
					return false;
				}
				isActive[8] = true;
				break;
			case 9:
				if (isEnd || isActive[9] || (order[i + 1] == 1 && !isActive[5])
						|| (order[i + 1] == 3 && !isActive[6])
						|| (order[i + 1] == 7 && !isActive[8])) {
					return false;
				}
				isActive[9] = true;
				break;
			}

		}

		return true;
	}

	private void resetIsActive() {
		for (int i = 0; i < order.length; i++) {
			isActive[i] = false;
		}

	}
	public void addNumber(int number) {
		for (int i = 0; i < order.length; i++) {
			if (order[i] == 0) {
				order[i] = number;
				break;
			}
		}
	}

	public void setOrder(int[] reihenfolge) throws Exception {
		if (reihenfolge.length == 10) {
			order = reihenfolge;
		} else {
			throw new Exception("length of Array is not Correct");
		}
	}

	public void init() {
		for (int i = 0; i < order.length; i++) {
			order[i] = 0;
			isActive[i] = false;
		}
	}

}
