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
	Composite comp_center;

	public ResizeListener(Label img, Composite comp_image, Composite comp_center) {
		this.img = img;
		this.comp_image = comp_image;
		this.comp_center = comp_center;
	}

	@Override
	public void controlMoved(ControlEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controlResized(ControlEvent e) {
		// comp_image = bild mit aktueller groesser
		String create_img = "icons/minica_create_cert.jpg";
		Image help = Activator.getImageDescriptor(create_img).createImage();
		
		image = img.getImage();
		
		int width = image.getBounds().width;
		int width_scaled = comp_image.getBounds().width;
		System.out.println(width_scaled);
		int height = (image.getBounds().height);
		int heigth_scaled = (int) (height - ((width - width_scaled) / 2.9));
		System.out.println(heigth_scaled);
		img_scaled = new Image(img.getDisplay(),help.getImageData().scaledTo(width_scaled, heigth_scaled));
		img.setImage(img_scaled);

		comp_center.layout();
	}

}
