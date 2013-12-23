package org.jcryptool.visual.ssl.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.visual.ssl.protocol.Message;

public class Attacks{
	private boolean decision=true;

	public Attacks() 
	{
		if(Message.getServerHelloHash()=="SHA1")
		{
			createDialog(Messages.AttacksSHA1);
		}
		else if(Message.getServerHelloHash()=="MD5")
		{
			createDialog(Messages.AttacksMD5);
		}
		if(Message.getServerHelloVersion()==0&&Message.getServerHelloCipherMode()=="CBC")
		{
			createDialog(Messages.AttacksBEAST);
		}
		if(Message.getServerHelloCipher()=="RC4")
		{
			createDialog(Messages.AttacksRC4);
		}
		else if(Message.getServerHelloCipher()=="DES")
		{
			createDialog(Messages.AttacksDES);
		}
		else if(Message.getServerHelloCipher()=="NULL")
		{
			createDialog(Messages.AttacksNoCipher);
		}
		if((Message.getServerHelloVersion()==0||Message.getServerHelloVersion()==1)&&Message.getServerHelloCipherMode()=="CBC")
		{
			createDialog(Messages.AttacksLucky13);
		}
		if(Message.getServerHelloKeyExchange()=="RSA")
		{
			createDialog(Messages.AttacksRSA);
		}
	}

	public void createDialog(String strAttackInfo) {
		 MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING|SWT.YES|SWT.NO);
		 messageBox.setMessage(strAttackInfo + Messages.AttacksProceed);
		 messageBox.setText(Messages.AttacksCaution);
		 int result = messageBox.open();
		if(result==SWT.NO)
		{
			decision = false;
		}
	}
	public boolean getDecision()
	{
		return decision;
	}
}
