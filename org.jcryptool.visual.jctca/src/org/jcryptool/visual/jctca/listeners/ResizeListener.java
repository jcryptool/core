//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2019 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.ResizeHelper;

/**
 * Listener for resizing images in the plugin
 * 
 * @param img label, which contains the resized image
 * @param comp_image composite, which contains the label img
 **/
public class ResizeListener implements ControlListener {
    private Image image;
    private Image img_scaled;
    private Label img;
    private Composite comp_image;
    private Image help;

    public ResizeListener(Label img, Composite comp_image) {
        this.img = img;
        this.comp_image = comp_image;
    }

    @Override
    public void controlMoved(ControlEvent e) {
    }

    /**
     * function for resizing the image to fit in the containing composite
     * 
     * @param e triggered event
     **/
    @Override
    public void controlResized(ControlEvent e) {
        ResizeHelper util = new ResizeHelper();
        String image_name = util.get_image_name();

        image = img.getImage();
        if (image != null) {
            int width = image.getBounds().width;
            int height = image.getBounds().height;
            double ratio = (double) width / (double) height;
            int width_scaled = 1;
            int height_scaled = 1;

            // if image is smaller
            if ((comp_image.getBounds().width / comp_image.getBounds().height) < ratio) {
                width_scaled = comp_image.getBounds().width;
                height_scaled = (int) (height - ((width - width_scaled) / ratio));
            } else {
                // if image is bigger
                height_scaled = comp_image.getBounds().height;
                width_scaled = (int) (width - (height - height_scaled) * ratio);
            }
            String lang = Platform.getNL().substring(0, 2);
            if (Platform.getNL().substring(0, 2).equals("de")) {
                lang = "de";
            } else {
                lang = "en";
            }
            // load matching image
            if (image_name == "Architekturskizze Zertifikatserzeugung") {//$NON-NLS-1$
                help = ImageService.getImage(Activator.PLUGIN_ID, "icons/" + lang + "/minica_create.png");
            } else if (image_name == "Architekturskizze Zertifikatswiderruf") {//$NON-NLS-1$
                help = ImageService.getImage(Activator.PLUGIN_ID, "icons/" + lang + "/minica_revoke.png");
            } else if (image_name == "Architekturskizze Signaturpr\u00FCfung") {//$NON-NLS-1$
                help = ImageService.getImage(Activator.PLUGIN_ID, "icons/" + lang + "/minica_check.png");
            } else {
                help = ImageService.getImage(Activator.PLUGIN_ID, "icons/ausweis.jpeg");
            }
            // create image in new size
            img_scaled = new Image(img.getDisplay(), help.getImageData().scaledTo(width_scaled, height_scaled));
            img.setImage(img_scaled);

            comp_image.layout();
        }
    }

}
