package org.jcryptool.visual.wots.files;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

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
     * function for resizing the image to fit in the containing composite
     * 
     * @param e triggered event
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

            // if image is smaller
            if ((comp_image.getBounds().width / comp_image.getBounds().height) < ratio) {
                width_scaled = comp_image.getBounds().width;
                height_scaled = (int) (height - ((width - width_scaled) / ratio));
            } else {
                // if image is bigger
                height_scaled = comp_image.getBounds().height;
                width_scaled = (int) (width - (height - height_scaled) * ratio);
            }
    	
            
        Image img_scaled = new Image(img.getDisplay(), org.eclipse.ui.plugin.AbstractUIPlugin.imageDescriptorFromPlugin("org.jcryptool.visual.wots", org.jcryptool.visual.wots.WotsView.currentImg).createImage().getImageData().scaledTo(width_scaled, height_scaled));
        img.setImage(img_scaled);
       	comp_image.layout();
        

        
        }
    }
}


