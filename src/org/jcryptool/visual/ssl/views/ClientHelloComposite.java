package org.jcryptool.visual.ssl.views;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import org.eclipse.draw2d.CheckBox;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.visual.ssl.protocol.Message;
import org.jcryptool.visual.ssl.protocol.ProtocolStep;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.viewers.ComboViewer;

public class ClientHelloComposite extends Composite implements ProtocolStep
{
	private Text txtRandom;
	private boolean infoText=false;
	private Button btnInformation;
	private Button btnGenerate;
	private Combo cmdCipher;
	private Combo cmdVersion;
	private Button btnNextStep;
	private SslView sslView;
	private ThreadedSeedGenerator randGenerator;
	private List<String> tls0= new ArrayList<String>();
	private List<String> tls1= new ArrayList<String>();
	private List<String> tls2= new ArrayList<String>();
	private List<CheckBox> cb0 = new ArrayList<CheckBox>();
	private List<String> tls0CipherSuites= new ArrayList<String>();
	private List<String> tls1CipherSuites= new ArrayList<String>();
	private List<String> tls2CipherSuites= new ArrayList<String>();
	
	public ClientHelloComposite(Composite parent, int style, final SslView sslView) 
	{
		super(parent, style);
		this.sslView = sslView;
		defineTlsLists();
		
		cb0.add(new CheckBox("Hey"));
		
		Group grpClientHello = new Group(this, SWT.NONE);
		grpClientHello.setText(Messages.ClientHelloComposite_group_text);
		grpClientHello.setBounds(10, 0, 326, 175);
		
		Label lblCipherSuite = new Label(grpClientHello, SWT.NONE);
		lblCipherSuite.setLocation(10, 85);
		lblCipherSuite.setSize(70, 15);
		lblCipherSuite.setText(Messages.ClientHelloCompositeLblCipherSuit);
		
		Label lblRandom = new Label(grpClientHello, SWT.NONE);
		lblRandom.setLocation(10, 55);
		lblRandom.setSize(55, 15);
		lblRandom.setText(Messages.ClientHelloCompositeLblRandom);
		
		Label lblSessionId = new Label(grpClientHello, SWT.NONE);
		lblSessionId.setLocation(10, 115);
		lblSessionId.setSize(70, 15);
		lblSessionId.setText(Messages.ClientHelloCompositeLblSessionId);
		
		Label lblSessionIdValue = new Label(grpClientHello, SWT.NONE);
		lblSessionIdValue.setLocation(90, 115);
		lblSessionIdValue.setSize(75, 15);
		lblSessionIdValue.setText("0");
		
		Label lblVersion = new Label(grpClientHello, SWT.NONE);
		lblVersion.setLocation(10, 25);
		lblVersion.setSize(55, 15);
		lblVersion.setText(Messages.ClientHelloCompositeLblVersion);
		
		cmdCipher = new Combo(grpClientHello, SWT.NONE);
		cmdCipher.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if(cmdVersion.getSelectionIndex()==0)
				{
					if(tls0CipherSuites.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex())))
					{
						tls0CipherSuites.remove(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
					}
					else if(tls0.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex())))
					{
						tls0CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));	
					}
				}
				if(cmdVersion.getSelectionIndex()==1)
				{
					if(tls1CipherSuites.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex())))
					{
						tls1CipherSuites.remove(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
					}
					else if(tls1.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex())))
					{
						tls1CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
					}
				}
				if(cmdVersion.getSelectionIndex()==2)
				{
					if(tls2CipherSuites.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex())))
					{
						tls2CipherSuites.remove(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
					}
					else if(tls2.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex())))
					{
						tls2CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
					}
				}
				refreshInformations();
			}
		}
		);
		cmdCipher.setLocation(90, 82);
		cmdCipher.setSize(160, 23);
		for(int i = 0;i<tls2.size();i++)
		{
			cmdCipher.add(tls2.get(i));
		}
		cmdCipher.select(27);
		
		cmdVersion = new Combo(grpClientHello, SWT.NONE);
		cmdVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmdCipher.removeAll();
				if(cmdVersion.getSelectionIndex()==0)
				{
					for(int i = 0;i<tls0.size();i++)
					{
						cmdCipher.add(tls0.get(i));
					}
				}
				else if(cmdVersion.getSelectionIndex()==1)
				{
					for(int i = 0;i<tls1.size();i++)
					{
						cmdCipher.add(tls1.get(i));
					}
				}
				else if(cmdVersion.getSelectionIndex()==2)
				{
					for(int i = 0;i<tls2.size();i++)
					{
						cmdCipher.add(tls2.get(i));
					}
				}
				cmdCipher.select(0);
				refreshInformations();
			}
		});
		cmdVersion.setLocation(90, 22);
		cmdVersion.setSize(90, 23);
		cmdVersion.add(Messages.Tls0);
		cmdVersion.add(Messages.Tls1);
		cmdVersion.add(Messages.Tls2);
		cmdVersion.select(2);
		
		btnInformation = new Button(grpClientHello, SWT.NONE);
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) 
			{
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ClientHelloInformationText);
				 messageBox.setText(Messages.ClientHelloCompositeBtnInformation);
				 messageBox.open();*/
				infoText=true;
				refreshInformations();
			}
		});
		btnInformation.setLocation(65, 140);
		btnInformation.setSize(100, 25);
		btnInformation.setText(Messages.ClientHelloCompositeBtnInformation);
		
		btnGenerate = new Button(grpClientHello, SWT.NONE);
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				createRandom();
			}
		});
		btnGenerate.setLocation(240, 50);
		btnGenerate.setSize(75, 25);
		btnGenerate.setText(Messages.ClientHelloCompositeBtnGenerate);
		
		txtRandom = new Text(grpClientHello, SWT.BORDER);
		txtRandom.setLocation(90, 52);
		txtRandom.setSize(140, 23);
		
		btnNextStep = new Button(grpClientHello, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setText(Messages.ClientHelloCompositeBtnNextStep);
		btnNextStep.setBounds(175, 140, 140, 25);
		
		tls2CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
		sslView.setStxInformationText(Messages.stxInformationSelectedCiphers +Messages.stxInformationTLS1 +Messages.stxInformationTLS2+ tls2CipherSuites.get(0) +"\n\n" + Messages.stxInformationRandomValue + txtRandom.getText());
	}

	public void refreshInformations()
	{
		String text0 = "";
		String text1 = "";
		String text2 = "";
		for(int i = 0;i < tls0CipherSuites.size();i++)
		{
			text0 += tls0CipherSuites.get(i) +"\n";
		}
		for(int i = 0;i < tls1CipherSuites.size();i++)
		{
			text1 += tls1CipherSuites.get(i) +"\n";
		}
		for(int i = 0;i < tls2CipherSuites.size();i++)
		{
			text2 += tls2CipherSuites.get(i) +"\n";
		}
		if(infoText)
		{
			sslView.setStxInformationText(Messages.stxInformationSelectedCiphers + text0 +Messages.stxInformationTLS1 + text1 + Messages.stxInformationTLS2 + text2 + "\n" + Messages.stxInformationRandomValue + txtRandom.getText()+"\n\n"+ Messages.ClientHelloInformationText);
		}
		else
		{
			sslView.setStxInformationText(Messages.stxInformationSelectedCiphers + text0 +Messages.stxInformationTLS1 + text1 + Messages.stxInformationTLS2 + text2 + "\n" + Messages.stxInformationRandomValue + txtRandom.getText()+"\n\n");
		}
	}
	
	public void enableControls()
	{
		txtRandom.setEnabled(true);
		btnInformation.setEnabled(true);
		btnGenerate.setEnabled(true);
		cmdCipher.setEnabled(true);
		cmdVersion.setEnabled(true);
		btnNextStep.setEnabled(true);
	}
	
	private void createRandom()
	{
		String text="";
		long gmtUnixTime = (new java.util.Date().getTime()/1000);
		
		text+=(Long.toHexString(gmtUnixTime));
		randGenerator = new ThreadedSeedGenerator();
	
		byte[] rand = randGenerator.generateSeed(28,false);
		text += bytArrayToHex(rand);
		txtRandom.setText(text);
		refreshInformations();
		
	}
	
	private String bytArrayToHex(byte[] a) 
	{
		   StringBuilder sb = new StringBuilder();
		   for(byte b: a)
		      sb.append(String.format("%02x", b&0xff));
		   return sb.toString();
		}
	public void disableControls() 
	{
		txtRandom.setEnabled(false);
		btnInformation.setEnabled(false);
		btnGenerate.setEnabled(false);
		cmdCipher.setEnabled(false);
		cmdVersion.setEnabled(false);
		btnNextStep.setEnabled(false);
	}

	public boolean checkParameters()
	{
		if(txtRandom.getText()=="")
		{
			createRandom();
		}
		try
		{
			if(txtRandom.getText().length()!=64)
			{
				throw new Exception();
			}
			for(int i = 0; i < 8;i++)
			{
				Long.parseLong(txtRandom.getText(i*8, (i+1)*8),16);
			}
		}
		catch(NumberFormatException exc)
		{
			 MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING|SWT.OK);
			 messageBox.setMessage(Messages.ServerHelloCompositeErrorRandom);
			 messageBox.setText(Messages.ServerHelloCompositeError);
			 messageBox.open();
			return false;
		}
		catch(Exception exc)
		{
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING|SWT.OK);
			messageBox.setMessage(Messages.ServerHelloCompositeErrorRandomShort);
			messageBox.setText(Messages.ServerHelloCompositeError);
		    messageBox.open();
			return false;
		}
		Message.setClientHelloVersion(cmdVersion.getSelectionIndex());
		Message.setClientHelloSessionID(0);
		Message.setClientHelloRandom(txtRandom.getText());
		Message.setClientHelloTls0CipherSuites(tls0CipherSuites);
		Message.setClientHelloTls1CipherSuites(tls1CipherSuites);
		Message.setClientHelloTls2CipherSuites(tls2CipherSuites);
		return true;
	}
	
	@Override
	protected void checkSubclass() 
	{
		// Disable the check that prevents subclassing of SWT components
	}
	private void defineTlsLists()
	{
		tls0.add(Messages.TLS0_RSA_WITH_NULL_MD5);
		tls0.add(Messages.TLS0_RSA_WITH_NULL_SHA);
		tls0.add(Messages.TLS0_RSA_WITH_RC4_128_MD5);
		tls0.add(Messages.TLS0_RSA_WITH_RC4_128_SHA);
		tls0.add(Messages.TLS0_RSA_WITH_3DES_EDE_CBC_SHA);
		tls0.add(Messages.TLS0_RSA_WITH_DES_CBC_SHA);
		tls0.add(Messages.TLS0_DH_DSS_WITH_3DES_EDE_CBC_SHA);
		tls0.add(Messages.TLS0_DH_DSS_WITH_DES_CBC_SHA);
		tls0.add(Messages.TLS0_DH_RSA_WITH_3DES_EDE_CBC_SHA);
		tls0.add(Messages.TLS0_DH_RSA_WITH_DES_CBC_SHA);
		tls0.add(Messages.TLS0_DHE_DSS_WITH_3DES_EDE_CBC_SHA);
		tls0.add(Messages.TLS0_DHE_DSS_WITH_DES_CBC_SHA);
		tls0.add(Messages.TLS0_DHE_RSA_WITH_3DES_EDE_CBC_SHA);
		tls0.add(Messages.TLS0_DHE_RSA_WITH_DES_CBC_SHA);
		
		tls1.add(Messages.TLS0_RSA_WITH_NULL_MD5);
		tls1.add(Messages.TLS0_RSA_WITH_NULL_SHA);
		tls1.add(Messages.TLS0_RSA_WITH_RC4_128_MD5);
		tls1.add(Messages.TLS0_RSA_WITH_RC4_128_SHA);
		tls1.add(Messages.TLS0_RSA_WITH_3DES_EDE_CBC_SHA);
		tls1.add(Messages.TLS0_RSA_WITH_DES_CBC_SHA);
		tls1.add(Messages.TLS0_DH_DSS_WITH_3DES_EDE_CBC_SHA);
		tls1.add(Messages.TLS0_DH_DSS_WITH_DES_CBC_SHA);
		tls1.add(Messages.TLS0_DH_RSA_WITH_3DES_EDE_CBC_SHA);
		tls1.add(Messages.TLS0_DH_RSA_WITH_DES_CBC_SHA);
		tls1.add(Messages.TLS0_DHE_DSS_WITH_3DES_EDE_CBC_SHA);
		tls1.add(Messages.TLS0_DHE_DSS_WITH_DES_CBC_SHA);
		tls1.add(Messages.TLS0_DHE_RSA_WITH_3DES_EDE_CBC_SHA);
		tls1.add(Messages.TLS0_DHE_RSA_WITH_DES_CBC_SHA);
		
		tls1.add(Messages.TLS1_RSA_WITH_AES_128_CBC_SHA);
		tls1.add(Messages.TLS1_DH_DSS_WITH_AES_128_CBC_SHA);
		tls1.add(Messages.TLS1_DH_RSA_WITH_AES_128_CBC_SHA);
		tls1.add(Messages.TLS1_DHE_DSS_WITH_AES_128_CBC_SHA);
		tls1.add(Messages.TLS1_DHE_RSA_WITH_AES_128_CBC_SHA);
		tls1.add(Messages.TLS1_RSA_WITH_AES_256_CBC_SHA);
		tls1.add(Messages.TLS1_DH_DSS_WITH_AES_256_CBC_SHA);
		tls1.add(Messages.TLS1_DH_RSA_WITH_AES_256_CBC_SHA);
		tls1.add(Messages.TLS1_DHE_DSS_WITH_AES_256_CBC_SHA);
		tls1.add(Messages.TLS1_DHE_RSA_WITH_AES_256_CBC_SHA);
		
		
		tls2.add(Messages.TLS2_RSA_WITH_NULL_MD5);
		tls2.add(Messages.TLS2_RSA_WITH_NULL_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_NULL_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_RC4_128_MD5);
		tls2.add(Messages.TLS2_RSA_WITH_RC4_128_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_CBC_SHA256);
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_3DES_EDE_CBC_SHA);
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_CBC_SHA);
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_CBC_SHA);
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_CBC_SHA256);
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_AES_128_GCM_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_GCM_SHA384);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_GCM_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_GCM_SHA384);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_GCM_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_GCM_SHA384);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_GCM_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_GCM_SHA384);
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_GCM_SHA256);
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_GCM_SHA384);
	}
}
