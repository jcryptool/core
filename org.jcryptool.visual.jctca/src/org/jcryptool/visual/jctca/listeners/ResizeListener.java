package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.ResizeHelper;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void controlResized(ControlEvent e) {
		ResizeHelper util = new ResizeHelper();
		String image_name = util.get_image_name();

		System.out.println(e.getSource());
		image = img.getImage();
		if (image != null) {
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
			if (image_name == "Architekturskizze Zertifikatserzeugung") {
				help = Activator.getImageDescriptor(
						"icons/minica_create_cert.jpg").createImage();
			} else if (image_name == "Architekturskizze Zertifikatswiderruf") {
				help = Activator.getImageDescriptor("icons/minica_revoke.jpg")
						.createImage();
			} else if (image_name == "Architekturskizze Signaturprüfung") {
				help = Activator.getImageDescriptor("icons/minica_check.jpg")
						.createImage();
			} else {
				help = Activator.getImageDescriptor("icons/ausweis.jpeg")
						.createImage();
			}

			img_scaled = new Image(img.getDisplay(), help.getImageData()
					.scaledTo(width_scaled, height_scaled));
			img.setImage(img_scaled);
			comp_image.layout();
		}
	}

}
