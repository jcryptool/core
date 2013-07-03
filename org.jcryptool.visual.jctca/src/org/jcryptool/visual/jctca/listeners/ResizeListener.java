package org.jcryptool.visual.jctca.listeners;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.ResizeHelper;

/**
 * Listener for resizing images in the plugin
 * 
 * @param img label, which contains the resized image
 * @param comp_image composite, which contains the label img
 **/
public class ResizeListener implements ControlListener {

    Image image;
    Image img_scaled;
    Label img;
    Composite comp_image;
    Composite composite;
    ResizeHelper util;
    Image help;

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
                help = Activator.getImageDescriptor("icons/" + lang + "/minica_create.png").createImage();//$NON-NLS-1$
            } else if (image_name == "Architekturskizze Zertifikatswiderruf") {//$NON-NLS-1$
                help = Activator.getImageDescriptor("icons/" + lang + "/minica_revoke.png")//$NON-NLS-1$
                        .createImage();
            } else if (image_name == "Architekturskizze Signaturpr\u00FCfung") {//$NON-NLS-1$
                help = Activator.getImageDescriptor("icons/" + lang + "/minica_check.png")//$NON-NLS-1$
                        .createImage();
            } else {
                help = Activator.getImageDescriptor("icons/ausweis.jpeg")//$NON-NLS-1$
                        .createImage();
            }
            // create image in new size
            img_scaled = new Image(img.getDisplay(), help.getImageData().scaledTo(width_scaled, height_scaled));
            img.setImage(img_scaled);

            comp_image.layout();
        }
    }

}
