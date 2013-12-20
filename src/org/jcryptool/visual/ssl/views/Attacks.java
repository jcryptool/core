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
			createDialog("SHA1");
		}
		else if(Message.getServerHelloHash()=="MD5")
		{
			createDialog("MD5");
		}
		if(Message.getServerHelloVersion()==0&&Message.getServerHelloCipherMode()=="CBC")
		{
			createDialog("BEAST");
		}
		if(Message.getServerHelloCipher()=="RC4")
		{
			createDialog("RC4");
		}
		else if(Message.getServerHelloCipher()=="DES")
		{
			createDialog("DES");
		}
		else if(Message.getServerHelloCipher()=="NULL")
		{
			createDialog("keine Verschlüsselung");
		}
		if((Message.getServerHelloVersion()==0||Message.getServerHelloVersion()==1)&&Message.getServerHelloCipherMode()=="CBC")
		{
			createDialog("Lucky13");
		}
		if(Message.getServerHelloKeyExchange()=="RSA")
		{
			createDialog("Non Forward Secrecy");
		}
	}

	public void createDialog(String strAttackInfo) {
		 MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING|SWT.YES|SWT.NO);
		 messageBox.setMessage(strAttackInfo + "\n Trotzdem fortsetzen?");
		 messageBox.setText("Achtung");
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
