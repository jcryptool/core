// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2019 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ECDH.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.ECDH.ECDHPlugin;

/** 
 *   @author original author unknown
 *   Class which handles the animation in the main view exchanging the message.
 *   
 *   IMPORTANT: I know this animation doesn't work on Linux, maybe take a look at that
 *   TODO: Fix this class not working on Linux
 */
public class Animation extends Thread {
	private Canvas canvasExchange;
	private String messageA;
	private String messageB;
	private String drawnMessage;

	public Animation(Canvas canvasExchange) {
		this(canvasExchange, "", "");
	}

	public Animation(Canvas canvasExchange, String messageA, String messageB) {
		this.canvasExchange = canvasExchange;
		this.messageA = messageA;
		this.messageB = messageB;
	}

	public void setMessage(String messageA, String messageB) {
		this.messageA = messageA;
		this.messageB = messageB;
	}

	/**
	 * Play and show the animation. Messages should be set beforehand
	 */
	public void run() {
		GC gc = new GC(canvasExchange);
		Image original = new Image(canvasExchange.getDisplay(), canvasExchange.getBounds().width,
				canvasExchange.getBounds().height);
		gc.copyArea(original, 0, canvasExchange.getBounds().height / 2);

		int canvasExchangeWidth = canvasExchange.getBounds().width;
		int canvasExchangeHeight = canvasExchange.getBounds().height;

		// Starting positions of text
		int x = -70;
		int y = 10;

		/*
		 * In the first step message A is drawn from left (Alice) to right (Bob)
		 */
		drawnMessage = messageA;
		for (int i = 0; i < 136; i++) {
			if (i < 12) {
				x += canvasExchangeWidth / (4 * 12);
			} else if (i < 36) {
				// The -72 correspond to 18 pixels because 72/(4*12)=1,5*12=18 // Comment of
				// original author
				y += (canvasExchangeHeight) / (4 * 24);
			} else if (i < 60) {
				x += canvasExchangeWidth / (2 * 24);
				y += (canvasExchangeHeight - 72) / (4 * 24);
			} else if (i < 68) {
				x += canvasExchangeWidth / (4 * 8);
			} else if (i == 68) {
				/*
				 * At this point message A has reached it's target. Set the coordinates for
				 * message B and let if go from right (Bob) to left (Alice)
				 */
				y = 10;
				x = canvasExchangeWidth - 70;
				drawnMessage = messageB;
			} else if (i < 80) {
				x -= canvasExchangeWidth / (4 * 12);
			} else if (i < 104) {
				// The -72 correspond to 18 pixels because 72/(4*12)=1,5*12=18 // Comment of
				// original author
				y += (canvasExchangeHeight) / (4 * 24);
			} else if (i < 128) {
				x -= canvasExchangeWidth / (2 * 24);
				y += (canvasExchangeHeight - 72) / (4 * 24);
			} else if (i < 136) {
				x -= canvasExchangeWidth / (4 * 8);
			}

			Image im = new Image(canvasExchange.getDisplay(), original, SWT.IMAGE_COPY);
			GC gcI = new GC(im);
			gcI.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			gcI.setFont(FontService.getHeaderFont());
			gcI.drawText(drawnMessage, x, y, true);
			gc.drawImage(im, 0, (canvasExchange.getBounds().height / 2));

			try {
				sleep(50);
			} catch (InterruptedException ex) {
				LogUtil.logError(ECDHPlugin.PLUGIN_ID, ex);
			}
			gcI.dispose();
		}
		gc.dispose();
		canvasExchange.redraw();
	}
}
