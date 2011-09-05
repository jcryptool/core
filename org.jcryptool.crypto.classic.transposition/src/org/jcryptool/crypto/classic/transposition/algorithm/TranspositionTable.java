// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.transposition.algorithm;

/**
 * Modelling a columnar transposition
 *
 * @author Simon L
 */
public class TranspositionTable {
	public static boolean DIR_ROWWISE = false;
	public static boolean DIR_COLUMNWISE = true;
	private Character[][] content;
	private int colCount;

	public static final Character EMPTY = Character.MAX_VALUE;

	/**
	 * creates an empty TranspositionTable with x columns
	 *
	 * @param colCount
	 */
	public TranspositionTable(int colCount) {
		if (colCount < 1)
			throw new IllegalArgumentException(
					"column count must be positive nonzero."); //$NON-NLS-1$
		this.colCount = colCount;

		clearTable();
	}

	/**
	 * Returns, how high the highest column will be, when <code>charCount</code>
	 * characters will be filled into a table of <code>colCount</code> columns.
	 *
	 * @param charCount
	 *            the number of characters filled into the empty table
	 * @param colCount
	 *            the number of columns of the table
	 * @return the height of the highest column
	 */
	private static int getTableMeasureMaxheight(int charCount, int colCount) {
		return (int) Math.ceil((double) charCount / (double) colCount);
	}

	/**
	 * Returns, how much columns will have the maximum height in a filled
	 * Transposition table.
	 *
	 * @param charCount
	 *            the number of characters filled into the empty table
	 * @param colCount
	 *            the number of columns of the table
	 * @return the number if columns who have the highest height of all
	 *         (Difference of heights can only be one or zero between columns)
	 */
	private static int getTableMeasureNrOfMaxheightColumns(int charCount,
			int colCount) {
		return charCount % colCount == 0 ? colCount : charCount % colCount;
	}

	/**
	 * Creates a "mask" for a filling-in of characters into the table. That
	 * means, that occupied places will be marked with the #EMPTY character, so
	 * that there a filling in of characters wont occur. Free to write places
	 * will have just null. But returned is always a rectangular array. <br />
	 * <br />
	 * This mask is created as follows:<br />
	 * (All examples: 5 columns, 13 chars to fill in) <br />
	 * <br />
	 * # masks always have to have the following form: (O: free; X: occupied)<br />
	 * <code>
	 *  O O O O O<br />
	 *  O O O O O<br />
	 *  O O X O X</code><br />
	 * meaning, that under a X there is never a O again. <br />
	 * <br />
	 * # the given key has the following function: think of it as if the lowest
	 * row had the form <code>O O O X X</code>. now, the given key encrypts the
	 * row, and the row is now <code>O O X O X</code>. The mask is created such,
	 * that the lowest row will have those X at the "encrypted" positions,
	 * assuming, they were all at the end before the encryption. hint: if a key
	 * of the form "1|2|3|4|5" is entered, the mask is generated, such that all
	 * X are at the and of the row also.
	 *
	 */
	private Character[][] createMaskForTable(int charCount, int rowCount,
			TranspositionKey maskGenerationKey) {

		if (maskGenerationKey.getLength() != colCount)
			throw new IllegalArgumentException(
					"key length and column count are different"); //$NON-NLS-1$

		int highestCol = getTableMeasureMaxheight(charCount, rowCount);
		int nrOfMaxheight = getTableMeasureNrOfMaxheightColumns(charCount,
				rowCount);
		int nrOfX = colCount - nrOfMaxheight;

		Character[][] result = new Character[colCount][highestCol];

		int[] posOfX = new int[nrOfX];
		for (int i = 0; i < nrOfX; i++) {
			posOfX[i] = maskGenerationKey.get(colCount - i - 1);
			result[posOfX[i]][highestCol - 1] = EMPTY;
		}

		return result;
	}

	/**
	 * Fills a number of characters into this table. if the chars do not fill
	 * only whole rowss so it is assumed, that the blanks are at the end of the
	 * last row. For changing this assumption, see
	 * {@link #fillCharsIntoTable(char[], boolean, TranspositionKey)}.
	 *
	 * @param chars
	 *            the characters to fill in
	 * @param direction
	 *            the reading in direction (true - columnwise; false - rowwise)
	 */
	public void fillCharsIntoTable(char[] chars, boolean direction) {
		TranspositionKey defaultAssumption = TranspositionKey
				.getDefaultUnchangingKey(colCount);

		fillCharsIntoTable(chars, direction, defaultAssumption);
	}

	/**
	 * Fills a number of characters into this table. Empties out the table
	 * before. the key parameter is for setting the blanks in the last row like
	 * they would be when they wouldve been at the end of the row as filler, and
	 * then encrypted with this key.
	 *
	 * @param chars
	 *            the characters to fill in
	 * @param direction
	 *            the reading in direction (true - columnwise; false - rowwise)
	 * @param keyToPredictBlanks
	 *            the key for predicting blank positions (see above)
	 */
	public void fillCharsIntoTable(char[] chars, boolean direction,
			TranspositionKey keyToPredictBlanks) {
		Character[][] table = createMaskForTable(chars.length, colCount,
				keyToPredictBlanks);
		int height = getTableMeasureMaxheight(chars.length, colCount);
		int width = colCount;

		int x = 0;
		int y = 0;
		int index = 0;
		while (index < chars.length) {
			// proove if char can be entered here..
			if (x > width - 1) {
				if (direction) {
					throw new IndexOutOfBoundsException(
							"went over the columns bound at columnwise read-in - must be masks, or algorithms, error."); //$NON-NLS-1$
				} else {
					y++;
					x = 0;
				}
			} else if (y > height - 1) {
				if (direction) {
					x++;
					y = 0;
				} else {
					throw new IndexOutOfBoundsException(
							"went over the rows bound at rowwise read-in - must be masks, or algorithms, error."); //$NON-NLS-1$
				}
			} else if (table[x][y] != null && table[x][y].equals(EMPTY)) {
				if (direction) {
					y++;
				} else {
					x++;
				}
			} else { // if can be entered, write it
				table[x][y] = chars[index];
				index++;
				if (direction) {
					y++;
				} else {
					x++;
				}
			}
		}

		this.content = table;
	}

	private int calcSavedContentLength() {
		int sum = 0;
		for (int i = 0; i < content.length; i++) {
			for (int k = 0; k < content[i].length; k++) {
				sum += (content[i][k] == null || !content[i][k].equals(EMPTY)) ? 1
						: 0;
			}
		}
		return sum;
	}

	public char[] readOutContent(boolean direction) {
		int contentLength = calcSavedContentLength();
		char[] result = new char[contentLength];

		int width = content.length;
		int index = 0;
		int x = 0;
		int y = 0;
		while (index < contentLength) {
			// proove if char can be read here..
			if (x > width - 1) {
				if (direction) {
					throw new IndexOutOfBoundsException(
							"went over the columns bound at columnwise read-out - must be masks, or algorithms, error."); //$NON-NLS-1$
				} else {
					y++;
					x = 0;
				}
			} else if (y > content[x].length - 1) {
				if (direction) {
					x++;
					y = 0;
				} else {
					throw new IndexOutOfBoundsException(
							"went over the rows bound at rowwise read-out - must be masks, or algorithms, error."); //$NON-NLS-1$
				}
			} else if (content[x][y] == null) {
				throw new UnsupportedOperationException(
						"Encountered null at readout -- cant interprete that."); //$NON-NLS-1$
			} else if (content[x][y].equals(EMPTY)) {
				if (direction) {
					y++;
				} else {
					x++;
				}
			} else { // if can be read, read it ^^
				result[index] = content[x][y];
				index++;
				if (direction) {
					y++;
				} else {
					x++;
				}
			}
		}

		return result;
	}

	/**
	 * Transpose the columns of this transposition table according to the given
	 * key
	 *
	 * @param key
	 *            the key
	 */
	public void transposeColumns(TranspositionKey key) {
		Character[][] newKey;
		if (key.getLength() != colCount)
			throw new IllegalArgumentException(
					"key length and table column count do not match."); //$NON-NLS-1$
		newKey = new Character[colCount][];
		for (int i = 0; i < newKey.length; i++) {
			newKey[i] = content[key.getIndexOf(i)];
		}

		content = newKey;
	}

	public void clearTable() {
		content = new Character[colCount][0];
	}

	/**
	 * @return the content of the table. empty fields are {@link #EMPTY}
	 */
	public Character[][] getContent() {
		return content;
	}
}
