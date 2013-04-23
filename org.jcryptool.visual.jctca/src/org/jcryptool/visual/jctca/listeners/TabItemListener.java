package org.jcryptool.visual.jctca.listeners;


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.jcryptool.visual.jctca.UserViews.CreateCert;
import org.jcryptool.visual.jctca.UserViews.RevokeCert;
import org.jcryptool.visual.jctca.UserViews.ShowCert;
import org.jcryptool.visual.jctca.UserViews.SignCert;

// TODO: dispose left side of usertab

public class TabItemListener implements SelectionListener {
TabFolder parent;
Composite grp_exp;
	
	public TabItemListener(TabFolder parent, Composite grp_exp){
		this.parent = parent;
		this.grp_exp = grp_exp;

	}
	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		System.out.println(parent.getSelectionIndex());
		Label lbl_exp = (Label)grp_exp.getChildren()[0];
		if(parent.getSelectionIndex()==0){
			lbl_exp.setText("Hi, I explain what is going on in User.");
			Group x = (Group)(parent.getChildren()[0]);
			Control[] foo = x.getChildren();
			System.out.println(foo.length);
			System.out.println(foo[0].getClass());
			System.out.println(foo[1].getClass());
			Composite right = (Composite)foo[1];
			if(right.getChildren().length>0){
				Composite right2 = (Composite)right.getChildren()[0];
				right2.dispose();
				x.layout(true);
			}
		}
		else if(parent.getSelectionIndex()==1){
			lbl_exp.setText("Hi, I explain what is going on in RA.");
		}
		else if(parent.getSelectionIndex()==2){
			lbl_exp.setText("Hi, I explain what is going on in CA.");
		}
		parent.layout(true);
		grp_exp.layout(true);
	}

}
