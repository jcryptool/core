package org.jcryptool.visual.jctca;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ResizeHelper {
	Image image;
	Image img_scaled;
	static String image_name; 
	
	public void resize_image(Label img, Composite comp_image, Image help) {
		image = img.getImage();
		int width = image.getBounds().width;
		int height = image.getBounds().height;
		double ratio = (double)width / (double)height;
		int width_scaled = 1;
		int height_scaled = 1;

		if ((comp_image.getBounds().width / comp_image.getBounds().height) < ratio) {
			width_scaled = comp_image.getBounds().width;
			height_scaled = (int) (height - ((width - width_scaled) / ratio));
		} else {
			height_scaled = comp_image.getBounds().height;
			width_scaled = (int) (width - (height - height_scaled) * ratio);
		}
		
		img_scaled = new Image(img.getDisplay(), help.getImageData().scaledTo(
				width_scaled, height_scaled));
		img.setImage(img_scaled);
		comp_image.layout();
	}

	public void set_image_name(String image_name){
		this.image_name = image_name;
		
	}
	
	public String get_image_name() {
		if(image_name == null) {
			image_name = "create";
		}
		return this.image_name;
	}
}
