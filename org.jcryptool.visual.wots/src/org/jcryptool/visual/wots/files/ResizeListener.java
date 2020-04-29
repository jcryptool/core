//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2015, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.wots.files;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.util.images.ImageService;
import org.jcryptool.visual.wots.WotsView;

import wots.WOTSPlugin;

/**
 * Listener for resizing images in the plugin
 * 
 * @param img label, which contains the resized image
 * @param comp_image composite, which contains the label img
 **/
public class ResizeListener implements ControlListener {

	private Image image;
    private Label img;
    private Composite comp_image;

    public ResizeListener(Label img, Composite container) {
        this.img = img;
        this.comp_image = container;
    }

    @Override
    public void controlMoved(ControlEvent e) {
    }

    /**
     * function for resizing the image to fit in the containing composite.
     * Note this function does not work correctly. When 
     * (comp_image.getBounds().width / comp_image.getBounds().height) changes from 
     * less than 2 to greater than 2 the image height gets correct, but before it is wrong.
     * At a ration around 1.5 to 1.9 the image height is to big.
     * 
     * @param e triggered event
     * 
     **/
    @Override
    public void controlResized(ControlEvent e) {
       
    	image = img.getImage();
        if (image != null) {
            int width = image.getBounds().width;
            int height = image.getBounds().height;
            double ratio = (double) width / (double) height;
            int width_scaled = 1;
            int height_scaled = 1;
            
//            System.out.println("Breite/Höhe Composite: " + comp_image.getBounds().width + " " + comp_image.getBounds().height);
//            System.out.println("Breite/Höhe Bild (vor skalierung): " + image.getBounds().width + " " + image.getBounds().height);


            // if container is smaller than image
            if ((comp_image.getBounds().width / comp_image.getBounds().height) < ratio) {
                width_scaled = comp_image.getBounds().width;
//                int temp = width_scaled / width;
                height_scaled = (int) (height - ((width - width_scaled) / ratio));
//                height_scaled = height * temp;
            } else {
                // if container is bigger than image
                height_scaled = comp_image.getBounds().height;
                width_scaled = (int) (width - (height - height_scaled) * ratio);
//                int temp = height_scaled / height;
//                width_scaled = width * temp;
            }


            //Composite bigger than image
//            if (comp_image.getBounds().width > width || comp_image.getBounds().height > height) {
//            	width_scaled = comp_image.getBounds().width;
//            	height_scaled = comp_image.getBounds().height;
//            }
    	
            
//        Image img_scaled = new Image(img.getDisplay(), org.eclipse.ui.plugin.AbstractUIPlugin.imageDescriptorFromPlugin("org.jcryptool.visual.wots", org.jcryptool.visual.wots.WotsView.currentImg).createImage().getImageData().scaledTo(width_scaled, height_scaled));
        if(WotsView.currentImg != null && img != null && img.getDisplay() != null) {
			Image img_scaled = new Image(img.getDisplay(), 
					ImageService.getImage(WOTSPlugin.PLUGIN_ID, WotsView.currentImg)
					.getImageData().scaledTo(width_scaled, height_scaled));
	//            System.err.println("-----------------------------------");
	//            System.out.println(e.toString());
	//            System.err.println("-----------------------------------");
	//            System.out.println(e.getSource().toString());
			img.setImage(img_scaled);
        }
//        System.out.println("Breite/Höhe Bild (nach skalierung): " + img.getImage().getBounds().width + " " + img.getImage().getBounds().height);
//        System.out.println("--------------------------------------------------");
       	comp_image.layout();
        }
    }
}


