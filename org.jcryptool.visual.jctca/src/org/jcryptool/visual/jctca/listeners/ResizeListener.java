package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;

public class ResizeListener implements ControlListener {

	Image image;
	Image img_scaled;
	Label img;
	Composite comp_image;
	Composite composite;

	public ResizeListener(Label img, Composite comp_image, Composite composite) {
		this.img = img;
		this.comp_image = comp_image;
		this.composite = composite;
	}

	@Override
	public void controlMoved(ControlEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controlResized(ControlEvent e) {
		// comp_image = bild mit aktueller groesse
		String create_img = "icons/minica_create_cert.jpg";
		String revoke_img = "icons/minica_revoke.jpg";
		String check_img = "icons/minica_check.jpg";
		String ausweis_img = "icons/ausweis.jpeg";


		image = img.getImage();
		int width = image.getBounds().width;
		int height = image.getBounds().height;
		int width_scaled = 1;
		int height_scaled = 1;
		
		
		if ((comp_image.getBounds().width / comp_image.getBounds().height) < 2.91) {
			width_scaled = comp_image.getBounds().width;
			height_scaled = (int) (height - ((width - width_scaled) / 2.91));
		} else {
			height_scaled = comp_image.getBounds().height;
			width_scaled = (int) (width - (height - height_scaled) * 2.91);
		}
		
		Image help = Activator.getImageDescriptor(create_img).createImage();
		img_scaled = new Image(img.getDisplay(), help.getImageData().scaledTo(
				width_scaled, height_scaled));
		img.setImage(img_scaled);
		comp_image.layout();
	}

}
