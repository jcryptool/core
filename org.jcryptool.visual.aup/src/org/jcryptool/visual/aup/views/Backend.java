// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2017 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----

package org.jcryptool.visual.aup.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.aup.AndroidUnlockPatternPlugin;
import org.jcryptool.visual.aup.views.AupView.ApuState;

/**
 *
 * @author Michael Schäfer
 * @author Stefan Kraus <stefan.kraus05@gmail.com>
 *
 *
 */
public class Backend {

	private static final String AUP_FOLDER = "aup"; //$NON-NLS-1$
	private static final String AUP_FILE = "pattern"; //$NON-NLS-1$

	private final static int arrayLengthStd = 10;

	private final static Color STANDARD = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	private final static Color ROT = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private final static Color GELB = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	private final static Color GRUEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	/**
	 * Computes the greatest common divisor of a and b.
	 * <p>
	 * Enforce that neither a nor b is 0!
	 *
	 * @param a
	 *            != 0
	 * @param b
	 *            != 0
	 * @return gcd(a, b)
	 */
	static int gcd(int a, int b) {
		int x = Math.abs(a);
		int y = Math.abs(b);
		while (x != y) {
			if (x > y)
				x = x - y;
			else
				y = y - x;
		}
		return x;
	}

	private Color lineColor = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	private boolean isChangeable = false;
	private boolean first = true;
	private int modus;// 1=set;2=change;3=check
	private int length = 0;
	private int matrixSize = 3; // size of the matrix; AUP 3x3
	private int tryCount = 0;
	private int[] order = new int[arrayLengthStd];
	private int[] ordersaved;
	private int[] ordertmp;

	int[][] points = new int[8][4];

	private AupView visual;

	/**
	 * Initialize some variables
	 *
	 * @param aupView
	 */
	public Backend(AupView aupView) {
		visual = aupView;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				points[i][j] = 0;
			}
		}
	}

	/**
	 * adds the number to the array which should be checked.
	 *
	 * @param btnNummer
	 *            number which should be added to the patter
	 */
	public void add(int btnNummer) {
		if (this.length > 9) {
			LogUtil.logError("pattern is longer then 9 -> impossible"); //$NON-NLS-1$
		} else if (length == 0) {
			// add starting point
			order[length] = btnNummer + 1;
			length++;
		} else {
			// add point
			int px1 = (order[length - 1] - 1) % matrixSize; // order: numbers from 1 to 9
			int py1 = (order[length - 1] - 1) / matrixSize; // order: numbers from 1 to 9
			int px2 = btnNummer % matrixSize; // btnNummer: numbers from 0 to 8
			int py2 = btnNummer / matrixSize; // btnNummer: numbers from 0 to 8
			int dx = px2 - px1;
			int dy = py2 - py1;
			float arc = (float) (Math.atan2(dy, dx) * 180 / Math.PI);

			if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
				// check for intermediate points
				// read in used points
				boolean used[] = new boolean[matrixSize * matrixSize];
				for (int i = 0; i < length; i++) {
					used[order[i] - 1] = true;
				}
				int steps, intP;
				if (dx == 0) {
					// vertical line
					steps = Math.abs(dy) + 1; // calculate the number of points on the line
					dy = (int) Math.signum(dy);
					for (int i = 0; i < steps; i++) {
						// add all intermediate points
						intP = ((py1 + i * dy) * matrixSize + px1);
						if (!used[intP]) { // check if point is already used -> add if not
							visual.getCntrBtn()[order[length - 1] - 1].setData("arc", arc);
							order[length] = intP + 1; // +1 because the buttons are numbered from 1
														// to 9
							length++;
						}
					}
				} else if (dy == 0) {
					// horizontal line
					steps = Math.abs(dx) + 1; // calculate the number of points on the line
					dx = (int) Math.signum(dx);
					for (int i = 0; i < steps; i++) {
						// add all intermediate points
						intP = (py1 * matrixSize + (px1 + i * dx));
						if (!used[intP]) { // check if point is already used -> add if not
							visual.getCntrBtn()[order[length - 1] - 1].setData("arc", arc);
							order[length] = intP + 1; // +1 because the buttons are numbered from 1
														// to 9
							length++;
						}
					}
				} else {
					steps = dx;
					int gcd;
					while ((gcd = gcd(dx, dy)) != 1) {
						// eliminate all common divisors
						dx /= gcd;
						dy /= gcd;
					}
					steps = (steps / dx) + 1; // calculate the number of points on the line
					for (int i = 0; i < steps; i++) {
						// add all intermediate points
						intP = ((py1 + i * dy) * matrixSize + (px1 + i * dx));
						if (!used[intP]) { // check if point is already used -> add if not
							visual.getCntrBtn()[order[length - 1] - 1].setData("arc", arc);
							order[length] = intP + 1; // +1 because the buttons are numbered from 1
														// to 9
							length++;
						}
					}
				}
			} else {
				// no intermediate points; dx == 0, dy == +-1 || dx == +-1 , dy == 0 || dx == dy
				// ==
				// 0
				// add the clicked point
				visual.getCntrBtn()[order[length - 1] - 1].setData("arc", arc);
				order[length] = btnNummer + 1;
				length++;
			}
		}
	}

	/**
	 * Simulated click to 'Cancel' button.<br>
	 * Resets the user input, cleans up the Mainbutton area and deletes the status
	 * text + image.
	 */
	public void btnCancelClick() {
		cancel();
		// reset text only when order comes from user
		visual.setStatusText("", null); //$NON-NLS-1$
	}

	public void btnMainClick(int btnNummer) {
		add(btnNummer);
		visual.getBtnCancel().setEnabled(true);
		if (!isGreatEnough()) {
			setColor(GELB);
			visual.setStatusText(Messages.Backend_TEXT_TO_SHORT, ApuState.WARNING);
		} else {
			setColor(GRUEN);
			visual.getBtnSave().setEnabled(true);
			visual.getBtnSave().setBackground(GRUEN);
			visual.setStatusText(Messages.Backend_TEXT_VALID, ApuState.INFO);
		}
		if (length > 1) {
			visual.getCenterbox().redraw();
		}
	}

	public void btnSaveClick() {
		if (modus == 1) { // set
			setPattern();
		} else if (modus == 2) { // change
			if (!isChangeable) {
				if (checkPattern()) {
					isChangeable = true;
					visual.updateProgress();
					visual.getBtnSave().setEnabled(false);
					visual.getBtnSave().setBackground(STANDARD);
				}
			} else {
				setPattern();
			}

		} else if (modus == 3) { // check
			checkPattern();
			cancel();
		} else {
			visual.MsgBox(Messages.Backend_PopupErrorHeading, Messages.Backend_PopupErrorMessage,
					SWT.ICON_ERROR | SWT.OK);
		}
	}

	/**
	 * Resets the user input and cleans up the Mainbutton area.
	 */
	public void cancel() {
		resetBtn();
		resetOrder();
	}

	/**
	 * Checks if entered pattern equals saved one. Counts also the failed check
	 * attempts.
	 */
	private boolean checkPattern() {
		if (Arrays.equals(order, ordersaved)) {
			visual.setStatusText(Messages.Backend_PopupValidMessage, ApuState.OK);
			resetBtn();
			resetOrder();
			tryCount = 0;
			return true;
		} else {
			tryCount++;
			visual.setStatusText(String.format(Messages.Backend_PopupWrongMessage, tryCount), ApuState.ERROR);
			resetBtn();
			resetOrder();

		}
		return false;
	}

	private File getAupFile() throws IOException {
		File aupFolder = new File(new File(DirectoryService.getWorkspaceDir()), AUP_FOLDER);
		if (!aupFolder.exists()) {
			aupFolder.mkdir();
		}
		File aupFile = new File(aupFolder, AUP_FILE);
		if (!aupFile.exists()) {
			if (aupFile.createNewFile()) {
				return aupFile;
			} else {
				throw new IOException("Unable to create File: " + aupFile.getAbsolutePath()); //$NON-NLS-1$
			}
		} else {
			return aupFile;
		}
	}

	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * @return modus
	 */
	public int getModus() {
		return modus;
	}

	/**
	 * @return the points
	 */
	public int[][] getPoints() {
		return this.points;
	}

	/**
	 * Checks if a pattern can be load from a savefile and sets the applicable mode
	 */
	public void init() {

		restore();
		if (ordersaved == null || ordersaved.length != 10 || ordersaved[0] == 0 || ordersaved[1] == 0
				|| ordersaved[2] == 0 || ordersaved[3] == 0) { // noch kein Pattern vorhanden, oder
																// falsch gespeichert
			ordersaved = new int[10];
			for (int i = 0; i < ordersaved.length; i++) {
				ordersaved[i] = 0;
			}
			setModus(1);
		} else {// pattern vorhanden. Modus wird auf check gesetzt
			setModus(3);
		}
	}

	protected boolean isChangeable() {
		return isChangeable;
	}

	protected boolean isFirst() {
		return first;
	}

	/**
	 * check whether the pattern is long enough (>4)
	 *
	 * @return
	 */
	private boolean isGreatEnough() {
		if (length < 4 || length > 9) {
			return false;
		}
		return true;
	}

	/**
	 * Resets the GUI and progress information to the initial state of the current
	 * mode.
	 */
	private void modusChanged() {
		// cancel current operation
		cancel();

		// reset progress
		isChangeable = false;
		first = true;
		setColor(STANDARD);
		visual.getBtnSave().setEnabled(false);
		visual.getBtnSave().setBackground(STANDARD);

		switch (modus) {
		case 1:
		case 2: {
			visual.getBtnSave().setText(Messages.Backend_ButtonContinueText);
			break;
		}
		case 3:
			visual.getBtnSave().setText(Messages.Backend_ButtonCheckText);
		}

		visual.updateProgress();
		updateModus();
	}

	public void recalculateLines() {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				points[i][j] = 0;
			}
		}
		for (int i = 0; i < length - 1; i++) {
			points[i][0] = visual.getCntrBtn()[order[i] - 1].getLocation().x
					+ visual.getCntrBtn()[order[i] - 1].getSize().x / 2;
			points[i][1] = visual.getCntrBtn()[order[i] - 1].getLocation().y
					+ visual.getCntrBtn()[order[i] - 1].getSize().y / 2;
			points[i][2] = visual.getCntrBtn()[order[i + 1] - 1].getLocation().x
					+ visual.getCntrBtn()[order[i + 1] - 1].getSize().x / 2;
			points[i][3] = visual.getCntrBtn()[order[i + 1] - 1].getLocation().y
					+ visual.getCntrBtn()[order[i + 1] - 1].getSize().y / 2;
		}
	}

	/**
	 * Resets all state information! Use with great care.
	 */
	public void reset() {
		ordersaved = new int[10];
		for (int i = 0; i < ordersaved.length; i++) {
			ordersaved[i] = 0;
		}
		resetBtn();
		resetOrder();
		save();
		setModus(1);
	}

	private void resetBtn() {
		for (Label btn : visual.getCntrBtn()) {
			btn.setData("arc", null);
		}
		setColor(STANDARD);
	}

	private void resetOrder() {
		order = new int[10];
		for (int i = 0; i < order.length; i++) {
			order[i] = 0;
		}
		length = 0;

	}

	/**
	 * read from AUP savefile and save in order[]
	 */
	public void restore() {
		BufferedReader reader;
		ordersaved = new int[10];
		try {
			reader = new BufferedReader(new FileReader(getAupFile()));
			String zeile = reader.readLine();
			while (zeile != null) {
				String[] values = zeile.split(";"); //$NON-NLS-1$
				for (int i = 0; i < values.length && i < 10; i++) {
					ordersaved[i] = Integer.parseInt(values[i]);
				}
				zeile = reader.readLine();
			}
		} catch (IOException ex) {
			LogUtil.logError(AndroidUnlockPatternPlugin.PLUGIN_ID, ex);
		}
	}

	/**
	 * saves order[] in AUP savefile separated by semicolon
	 *
	 * @return false on error else true
	 */
	public boolean save() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(getAupFile()));
			for (int i = 0; i < order.length; i++) {
				writer.write(order[i] + ";"); //$NON-NLS-1$
			}
			writer.close();
		} catch (IOException e) {
			LogUtil.logError("Error on saving pattern file.\n" + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	private void saveOrder() {
		if (order.length == arrayLengthStd && ordersaved.length == arrayLengthStd) {
			for (int i = 0; i < order.length; i++) {
				ordersaved[i] = order[i];
			}
			save();
		} else {
			LogUtil.logError("Interal Error: order.length!=ordersaved.length!=10"); //$NON-NLS-1$
		}

	}

	private void setColor(Color farbe) {
		if (farbe != STANDARD) {
			for (int i = 0; i < order.length; i++) {
				if (order[i] != 0) {
					if (farbe == GELB) {
						if (visual.getCntrBtn()[order[i] - 1].getImage() != null)
							visual.getCntrBtn()[order[i] - 1].getImage().dispose(); // dispose old
																					// image
						Image img = AndroidUnlockPatternPlugin.getImageDescriptor("icons/yellow.png") //$NON-NLS-1$
								.createImage(visual.getCntrBtn()[i].getDisplay());
						visual.getCntrBtn()[order[i] - 1].setImage(img);
						visual.getCntrBtn()[order[i] - 1].setData("icon", "icons/yellow.png"); //$NON-NLS-1$ //$NON-NLS-2$
					} else if (farbe == ROT) {
						if (visual.getCntrBtn()[order[i] - 1].getImage() != null)
							visual.getCntrBtn()[order[i] - 1].getImage().dispose(); // dispose old
																					// image
						Image img = AndroidUnlockPatternPlugin.getImageDescriptor("icons/red.png") //$NON-NLS-1$
								.createImage(visual.getCntrBtn()[i].getDisplay());
						visual.getCntrBtn()[order[i] - 1].setImage(img);
						visual.getCntrBtn()[order[i] - 1].setData("icon", "icons/red.png"); //$NON-NLS-1$ //$NON-NLS-2$

					} else if (farbe == GRUEN) {
						if (visual.getCntrBtn()[order[i] - 1].getImage() != null)
							visual.getCntrBtn()[order[i] - 1].getImage().dispose(); // dispose old
																					// image
						Image img = AndroidUnlockPatternPlugin.getImageDescriptor("icons/green.png") //$NON-NLS-1$
								.createImage(visual.getCntrBtn()[i].getDisplay());
						visual.getCntrBtn()[order[i] - 1].setImage(img);
						visual.getCntrBtn()[order[i] - 1].setData("icon", "icons/green.png"); //$NON-NLS-1$ //$NON-NLS-2$

					}
					// visual.getCntrBtn()[order[i] - 1].setBackground(farbe);
					lineColor = farbe;
					visual.getCenterbox().redraw();
				}
			}
		} else {
			for (Label btn : visual.getCntrBtn()) {
				if (btn.getImage() != null)
					btn.getImage().dispose();
				Image img = AndroidUnlockPatternPlugin.getImageDescriptor("icons/black.png") //$NON-NLS-1$
						.createImage(btn.getDisplay());
				btn.setImage(img);
				btn.setData("icon", "icons/black.png"); //$NON-NLS-1$ //$NON-NLS-2$

			}
			// lineColor=farbe;
		}
		visual.getCenterbox().redraw();
	}

	/**
	 * @param modus
	 *            the modus to set
	 */
	public void setModus(int modus) {
		this.modus = modus;
		modusChanged();
	}

	/**
	 * Checks if the inputed pattern is valid. If so it is in case of the <b>first
	 * input</b> temporary saved. In case of a <b>matching confirmation input</b> it
	 * is saved and the mode is changed to <i>check</i>. In case of a <b>not
	 * matching confirmation input</b> the input is reseted and an information
	 * message is displayed in the status text.
	 */
	private void setPattern() {
		// if (isValid() && isGreatEnough()) {
		if (isGreatEnough()) {
			if (first) {
				first = false;
				ordertmp = new int[arrayLengthStd];
				for (int i = 0; i < order.length; i++) {
					ordertmp[i] = order[i];
				}
				visual.getBtnSave().setText(Messages.AndroidUnlockPattern_ButtonSaveText);
				visual.updateProgress();
				visual.setStatusText("", null); //$NON-NLS-1$
				cancel();
			} else if (Arrays.equals(ordertmp, order)) {
				saveOrder();
				cancel();
				setModus(3);
				visual.setStatusText(Messages.Backend_PopupSavedMessage, ApuState.INFO);
			} else {
				// MsgBox unequal pattern or Error
				btnCancelClick();
				visual.setStatusText(Messages.Backend_PopupNotSavedMessage, ApuState.ERROR);
			}
		} else {
			btnCancelClick();
			visual.MsgBox(Messages.Backend_PopupInvalidHeading, Messages.Backend_PopupInvalidMessage,
					SWT.ICON_ERROR | SWT.OK);
		}
	}

	/**
	 * checks which Modus is active and set it in the UI also disables non-available
	 * modes
	 *
	 */
	public void updateModus() {
		if (getModus() == 1) { // Modus SET
			visual.getSetPattern().setSelection(true);
			visual.getChangePattern().setSelection(false);
			visual.getCheckPattern().setSelection(false);
			visual.getSetPattern().setEnabled(true);
			visual.getChangePattern().setEnabled(false);
			visual.getCheckPattern().setEnabled(false);
		} else if (getModus() == 2) { // Modus change
			visual.getSetPattern().setSelection(false);
			visual.getChangePattern().setSelection(true);
			visual.getCheckPattern().setSelection(false);
			visual.getSetPattern().setEnabled(false);
			visual.getChangePattern().setEnabled(true);
			visual.getCheckPattern().setEnabled(true);
		} else if (getModus() == 3) { // Modus check
			visual.getSetPattern().setSelection(false);
			visual.getChangePattern().setSelection(false);
			visual.getCheckPattern().setSelection(true);
			visual.getSetPattern().setEnabled(false);
			visual.getChangePattern().setEnabled(true);
			visual.getCheckPattern().setEnabled(true);
		} else { // Fehlerfall
			LogUtil.logError(
					"schwerer Fehler in \"checkModus\" bitte den Entwickler kontaktieren - Please contact the developer"); //$NON-NLS-1$
		}
	}
}
