package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
		
		if(text.equals(Messages.PluginBtnListener_strcmp_continue)){
			visual.disposeCompCenter();
			visual.showCenter();
		}
		else if(text.equals(Messages.PluginBtnListener_strcmp_create_cert)){
			lbl_img.setImage(Activator.getImageDescriptor(Messages.PluginBtnListener_create_cert_arch_path).createImage());
		}
		else if(text.equals(Messages.PluginBtnListener_strcmp_rev_cert)){
			lbl_img.setImage(Activator.getImageDescriptor(Messages.PluginBtnListener_rev_cert_arch_path).createImage());
		}
		else if(text.equals(Messages.PluginBtnListener_strcmp_check_sig)){
			lbl_img.setImage(Activator.getImageDescriptor(Messages.PluginBtnListener_check_sig_arch_path).createImage());
		}
	}

}
