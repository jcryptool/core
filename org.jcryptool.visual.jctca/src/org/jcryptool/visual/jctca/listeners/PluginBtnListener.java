package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.JCTCA_Visual;
import org.jcryptool.visual.jctca.ResizeHelper;

public class PluginBtnListener implements SelectionListener {

	JCTCA_Visual visual;
	Label lbl_img;
	Image image;
	Image help;
	StyledText exp;

	public PluginBtnListener(JCTCA_Visual visual, Label lbl_img, StyledText exp) {
		this.visual = visual;
		this.lbl_img = lbl_img;
		this.exp = exp;
	}


	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Composite comp_image = lbl_img.getParent();
		String text = ((Button) arg0.getSource()).getText();
		ResizeHelper util = new ResizeHelper();
		
		if (text.equals(Messages.PluginBtnListener_strcmp_continue)) {
			visual.disposeCompCenter();
			visual.showCenter();
			exp.setText("Erklaerunstext plugin allgemein");
		} else if (text.equals(Messages.PluginBtnListener_strcmp_create_cert)) {
			help = Activator.getImageDescriptor(
					Messages.PluginBtnListener_create_cert_arch_path)
					.createImage();
			exp.setText("Erklaerungstext Create");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("create");
		} else if (text.equals(Messages.PluginBtnListener_strcmp_rev_cert)) {
			help = Activator.getImageDescriptor(
					Messages.PluginBtnListener_rev_cert_arch_path)
					.createImage();
			exp.setText("Erklaerungstext Revoke");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("revoke");
		} else if (text.equals(Messages.PluginBtnListener_strcmp_check_sig)) {
			help = Activator.getImageDescriptor(
					Messages.PluginBtnListener_check_sig_arch_path)
					.createImage();
			exp.setText("Erklaerungstext Check");
			util.resize_image(lbl_img, comp_image, help);
			util.set_image_name("check");

		}
		
	}

}
