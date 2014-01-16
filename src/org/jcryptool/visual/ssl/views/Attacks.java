package org.jcryptool.visual.ssl.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.visual.ssl.protocol.Message;

public class Attacks{
	private boolean decision=true;

	public Attacks() 
	{
		if(Message.getServerHelloHash().equals("SHA1"))
		{
			createDialog(Messages.AttacksSHA1);
		}
		else if(Message.getServerHelloHash().equals("MD5"))
		{
			createDialog(Messages.AttacksMD5);
		}
		if(Message.getServerHelloVersion().equals("0301")&&Message.getServerHelloCipherMode().equals("CBC"))
		{
			createDialog(Messages.AttacksBEAST);
		}
		if(Message.getServerHelloCipher().equals("RC4_128"))
		{
			createDialog(Messages.AttacksRC4);
		}
		else if(Message.getServerHelloCipher().equals("DES"))
		{
			createDialog(Messages.AttacksDES);
		}
		else if(Message.getServerHelloCipher().equals("NULL"))
		{
			createDialog(Messages.AttacksNoCipher);
		}
		if((Message.getServerHelloVersion().equals("0301")||Message.getServerHelloVersion().equals("0302"))&&Message.getServerHelloCipherMode().equals("CBC"))
		{
			createDialog(Messages.AttacksLucky13);
		}
		if(Message.getServerHelloKeyExchange().equals("RSA"))
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
