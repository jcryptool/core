//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ResizeHelper {
    Image image;
    Image img_scaled;
    // internal name of image - this is important because we're chosing images
    // based on string compares
    // if this isn't set properly, stuff will break!
    static String image_name;

    public void resize_image(Label img, Composite comp_image, Image help) {
        image = img.getImage();
        int width = image.getBounds().width;
        int height = image.getBounds().height;
        double ratio = (double) width / (double) height;
        int width_scaled = 1;
        int height_scaled = 1;

        if ((comp_image.getBounds().width / comp_image.getBounds().height) < ratio) {
            width_scaled = comp_image.getBounds().width;
            height_scaled = (int) (height - ((width - width_scaled) / ratio));
        } else {
            height_scaled = comp_image.getBounds().height;
            width_scaled = (int) (width - (height - height_scaled) * ratio);
        }

        img_scaled = new Image(img.getDisplay(), help.getImageData().scaledTo(width_scaled, height_scaled));
        img.setImage(img_scaled);
        comp_image.layout();
    }

    /**
     * 
     * set the internal name of an image
     * 
     * @param image_name internal name of the image
     */
    public void set_image_name(String image_name) {
        ResizeHelper.image_name = image_name;

    }

    /**
     * 
     * Get the internal name of an image (i.e.: "Architekturskizze Zertifikat erzeugen")
     * 
     * @return name of the image as String
     */
    public String get_image_name() {
        if (image_name == null) {
            // Default to create certificate intro scheme, because this is the
            // first image displayed in the view
            // and therefore has to be shown if the user hasn't clicked any
            // buttons yet
            image_name = "Architekturskizze Zertifikatserzeugung";//$NON-NLS-1$
        }
        return ResizeHelper.image_name;
    }
}
