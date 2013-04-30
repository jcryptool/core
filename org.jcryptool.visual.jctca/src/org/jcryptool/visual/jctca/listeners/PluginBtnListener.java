package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.JCTCA_Visual;

public class PluginBtnListener implements SelectionListener{

	JCTCA_Visual visual;
	Label lbl_img;
	public PluginBtnListener(JCTCA_Visual visual, Label lbl_img){
		this.visual = visual;
		this.lbl_img = lbl_img; 
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		String text = ((Button)arg0.getSource()).getText();
		
		if(text.equals("Weiter zum Plugin")){
			visual.disposeCompCenter();
			visual.showCenter();
		}
		else if(text.equals("Zertifikat erzeugen")){
			Image img = Activator.getImageDescriptor("icons/minica_create_cert.jpg").createImage();
			lbl_img.setImage(img);
		}
		else if(text.equals("Zertifikat widerrufen")){
			lbl_img.setImage(Activator.getImageDescriptor("icons/minica_revoke.jpg").createImage());
		}
		else if(text.equals("Signatur überprüfen")){
			lbl_img.setImage(Activator.getImageDescriptor("icons/minica_check.jpg").createImage());
		}
	}

}
