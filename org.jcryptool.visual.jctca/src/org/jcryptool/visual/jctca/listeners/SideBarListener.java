package org.jcryptool.visual.jctca.listeners;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.RevokeCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;

public class SideBarListener implements SelectionListener {

	CreateCert cCert;
	ShowCert sCert;
	RevokeCert rCert;
	SignCert siCert;
	Composite comp_right;
	Group comp_exp;
	public SideBarListener(CreateCert cCert, ShowCert sCert, RevokeCert rCert, SignCert siCert, Group comp_exp, Composite comp_right){
		//this.cCert = cCert;
//		this.rCert = new RevokeCert(comp_right);
//		this.cCert = new CreateCert(comp_right, comp_exp);
//		this.sCert = new ShowCert(comp_right);
//		this.siCert = new SignCert(comp_right);
		//this.sCert = sCert;
		this.comp_right = comp_right;
		this.comp_exp = comp_exp;
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
		if(siCert != null) {
			siCert.dispose();
		}
		String text = btn.getText();
		System.out.println(text);
		if(text.equals("Neues Zertifikat anfordern")){
			cCert = new CreateCert(comp_right, comp_exp);
			cCert.setVisible(true);
		}
		else if(text.equals("Zertifikate verwalten")){
			sCert = new ShowCert(comp_right);
			sCert.setVisible(true);
		}
		else if(text.equals("Zertifikat widerrufen")){
			rCert = new RevokeCert(comp_right);
			rCert.setVisible(true);
		}
		else if(text.equals("Text oder Datei signieren")){
			siCert = new SignCert(comp_right);
			siCert.setVisible(true);
		}
		comp_right.layout(true);
	}

}
