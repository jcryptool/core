package org.jcryptool.visual.jctca.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.Views;

public class SideBarListener implements SelectionListener {

	Views view;
	public SideBarListener(Views view){
		this.view = view;
	}
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		Button btn = (Button)arg0.getSource();
		view.setVisible(false);
		String text = btn.getText();
		System.out.println(text);
		if(text.equals("Create Certificate")){
		//	cCert.setVisible(true);
		}
		else if(text.equals("Show Certificate")){
		//	sCert.setVisible(true);
		}
	}

}
