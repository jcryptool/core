package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.RevokeCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;

public class SideBarListener implements SelectionListener {

	CreateCert cCert;
	ShowCert sCert;
	RevokeCert rCert;
	Composite comp_right;
	public SideBarListener(CreateCert cCert, ShowCert sCert, RevokeCert rCert, Composite comp_right){
		//this.cCert = cCert;
		this.rCert = new RevokeCert(comp_right);
		this.cCert = new CreateCert(comp_right);
		this.sCert = new ShowCert(comp_right);
		//this.sCert = sCert;
		this.comp_right = comp_right;
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Button btn = (Button)arg0.getSource();

		if(cCert != null) {
			cCert.dispose();
		}
		if(sCert != null) {
			sCert.dispose();
		}
		if(rCert != null) {
			rCert.dispose();
		}
		String text = btn.getText();
		System.out.println(text);
		if(text.equals("Create Certificate")){
			cCert = new CreateCert(comp_right);
			cCert.setVisible(true);
		}
		else if(text.equals("Show Certificate")){
			sCert = new ShowCert(comp_right);
			sCert.setVisible(true);
		}
		else if(text.equals("Revoke Certificate")){
			rCert = new RevokeCert(comp_right);
			rCert.setVisible(true);
		}
		comp_right.layout(true);
	}

}
