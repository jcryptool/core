// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class ScrollbarTest {

	public static void main(String[] args) {

		final String[] list = new String[128];

		for (int i = 0; i < list.length; i++) {

			list[i] = i + "-String-that-is-quite-long-" + i;

		}

		Display display = new Display();

		int style = SWT.SHELL_TRIM | SWT.H_SCROLL | SWT.V_SCROLL;

		style |= SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;

		final Shell shell = new Shell(display, style);

		Listener scrollListener = new Listener() {

			public void handleEvent(Event event) {

				shell.redraw();

			}

		};

		final ScrollBar hBar = shell.getHorizontalBar();

		final ScrollBar vBar = shell.getVerticalBar();

		hBar.addListener(SWT.Selection, scrollListener);

		vBar.addListener(SWT.Selection, scrollListener);

		final Color listForeground =

		display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);

		final Color listBackground =

		display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);

		shell.addListener(SWT.Paint, new Listener() {

			public void handleEvent(Event event) {

				GC gc = event.gc;

				gc.setForeground(listForeground);

				gc.setBackground(listBackground);

				Rectangle rect = shell.getClientArea();

				gc.fillRectangle(rect);

				int x = -hBar.getSelection();

				int height = gc.stringExtent("").y;

				int start = vBar.getSelection();

				int end =

				Math.min(

				list.length - 1,

				start + rect.height / height);

				for (int i = start; i <= end; i++) {

					gc.drawText(

					list[i],

					x + 2,

					(i - start) * height + 2);

				}

			}

		});

		shell.addListener(SWT.Resize, new Listener() {

			public void handleEvent(Event event) {

				int hSelection = hBar.getSelection();

				int vSelection = vBar.getSelection();

				Rectangle rect = shell.getClientArea();

				GC gc = new GC(shell);

				int width = 0;

				for (int i = 0; i < list.length; i++) {

					width =

					Math.max(

					width,

					gc.textExtent(list[i]).x);

				}

				width += 4;

				hBar.setMaximum(width);

				hBar.setThumb(Math.min(width, rect.width));

				hBar.setPageIncrement(hBar.getThumb());

				int height = gc.stringExtent("").y;

				vBar.setMaximum(list.length);

				int page = Math.max(1, rect.height / height);

				vBar.setThumb(Math.min(page, list.length));

				vBar.setPageIncrement(vBar.getThumb() - 1);

				gc.dispose();

				if (hSelection != hBar.getSelection()

				|| vSelection != vBar.getSelection()) {

					shell.redraw();

				}

			}

		});

		shell.setSize(200, 200);

		shell.open();

		while (!shell.isDisposed()) {

			if (!display.readAndDispatch())
				display.sleep();

		}

		display.dispose();

	}

}
