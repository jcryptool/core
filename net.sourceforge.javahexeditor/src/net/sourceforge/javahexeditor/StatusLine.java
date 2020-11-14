/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * The official javahexeditor site is sourceforge.net/projects/javahexeditor
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sourceforge.javahexeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import net.sourceforge.javahexeditor.BinaryContent.RangeSelection;
import net.sourceforge.javahexeditor.common.NumberUtility;

/**
 * Status line component of the editor. Displays the current position, value at
 * position, the insert/overwrite status and the file size.
 */
final class StatusLine extends Composite {

	private Label valueLabel;
	private Label insertModeLabel;
	private Label sizeLabel;

	/**
	 * Create a status line part
	 *
	 * @param parent            parent in the widget hierarchy
	 * @param style             not used
	 * @param withLeftSeparator so it can be put besides other status items (for
	 *                          plugin)
	 */
	public StatusLine(Composite parent, int style, boolean withLeftSeparator) {
		super(parent, style);
		initialize(withLeftSeparator);
	}

	private void initialize(boolean withSeparator) {
		GridLayout statusLayout = new GridLayout(3, true);
		statusLayout.marginHeight = 0;
		statusLayout.marginWidth = 0;
		setLayout(statusLayout);
		
		Composite composite_left = new Composite(this, SWT.NONE);
		composite_left.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout_composite_left = new GridLayout();
		gridLayout_composite_left.numColumns = withSeparator ? 2 : 1;
		gridLayout_composite_left.marginHeight = 0;
		composite_left.setLayout(gridLayout_composite_left);

		if (withSeparator) {
			new Label(composite_left, SWT.SEPARATOR);
		}
		
		insertModeLabel = new Label(composite_left, SWT.SHADOW_NONE);
		insertModeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite composite_center = new Composite(this, SWT.NONE);
		composite_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout_composite_center = new GridLayout(2,  false);
		gridLayout_composite_center.marginHeight = 0;
		composite_center.setLayout(gridLayout_composite_center);

		new Label(composite_center, SWT.SEPARATOR);

		valueLabel = new Label(composite_center, SWT.SHADOW_NONE);
		valueLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Composite composite_right = new Composite(this, SWT.NONE);
		composite_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gridLayout_composite_right = new GridLayout(2,  false);
		gridLayout_composite_right.marginHeight = 0;
		composite_right.setLayout(gridLayout_composite_right);

		new Label(composite_right, SWT.SEPARATOR);

		sizeLabel = new Label(composite_right, SWT.SHADOW_NONE);
		GC gc = new GC(sizeLabel);
		double fontCharWidth = gc.getFontMetrics().getAverageCharacterWidth();
		GridData gridData_sizeLabel = new GridData(SWT.FILL, SWT.FILL, true, true);
		// Allocate 25 Chars of space. In combination with the equal coulmn sizes, all 
		// columns are 25 Characters wide.
		gridData_sizeLabel.widthHint = (int) (25 * fontCharWidth);
		sizeLabel.setLayoutData(gridData_sizeLabel);

	}

	/**
	 * Update the insert/overwrite mode.
	 *
	 * @param insert <code>true</code> for insert mode, or <code>false</code> for
	 *               overwrite
	 */
	public void updateInsertMode(boolean insert) {
		if (isDisposed() || insertModeLabel.isDisposed()) {
			return;
		}

		insertModeLabel.setText(insert ? Texts.STATUS_LINE_MODE_INSERT : Texts.STATUS_LINE_MODE_OVERWRITE);
	}


	/**
	 * Clear the value status.
	 */
	public void clearValue() {
		if (isDisposed() || valueLabel.isDisposed()) {
			return;
		}
		valueLabel.setText(Texts.EMPTY);
	}

	/**
	 * Update the value status. Displays its decimal, hex and binary value
	 *
	 * @param value value to display
	 */
	public void updateValue(byte value) {
		if (isDisposed() || valueLabel.isDisposed()) {
			return;
		}
		
		int unsignedValue = value & 0xff;
		String binaryText = "0000000" + Integer.toBinaryString(unsignedValue);
		binaryText = binaryText.substring(binaryText.length() - 8);
		String text = NumberUtility.getHexString(unsignedValue) +
				" = " +
				NumberUtility.getDecimalString(unsignedValue) + 
				" = " +
				binaryText;
		
		valueLabel.setText(text);
	}
	
	/**
	 * Clear the size status.
	 */
	public void clearSize() {
		if (isDisposed() || valueLabel.isDisposed()) {
			return;
		}
		sizeLabel.setText(Texts.EMPTY);
	}


	public void updateSelectionAndSize(RangeSelection selection) {
		if (isDisposed() || sizeLabel.isDisposed()) {
			return;
		}
		sizeLabel.setText(NumberUtility.getDecimalAndHexRangeString(selection.start, selection.end));
	}

	public void updatePoitionAndSize(long caretPos, long size) {
		if (isDisposed() || sizeLabel.isDisposed()) {
			return;
		}
		sizeLabel.setText(NumberUtility.getDecimalAndHexString(caretPos) + " : " + NumberUtility.getDecimalAndHexString(size));
	}

}
