// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2020, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.core.introduction.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * This class provides a static method {@link #resize(Image, int, int)}
 * which is used to scale images to a specific size with a better quality
 * than <code>ImageData#scaleTo(int width, int height)</code> does.</br></br>
 * FYI This method is in own class because I previously i used a lot more
 * code to resize images. After switching i just does not wanted to move
 * the method <code>AlgorithmInstruction</code>.</br></br>
 * The whole code I copied from {@link http://www.aniszczyk.org/2007/08/09/resizing-images-using-swt/}.
 * 
 * @author Thorben Groos
 *
 */
public class ImageScaler {

	/**
	 * Scales an Image to a specific width and height.
	 * @param image The original image
	 * @param width The width the image should be scaled to.
	 * @param height The height the image should be scaled to.
	 * @return A new image with the given width and height.
	 */
	public static Image resize(Image image, int width, int height) {
		  Image scaled = new Image(Display.getDefault(), width, height);
		  GC gc = new GC(scaled);
		  gc.setAntialias(SWT.ON);
		  gc.setInterpolation(SWT.HIGH);
		  gc.drawImage(image, 0, 0,image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		  gc.dispose();
		  return scaled;
		}
}
