package org.jcryptool.visual.ssl.views;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

/**
 * Class that represents the ServerHello Step of SSL/TLS
 * 
 * @author Kapfer
 *
 */
public class ServerHelloComposite extends Composite implements ProtocolStep
{
	private boolean infoText=false;
	private Text txtRandom;
	private Text txtSessionID;
	private Button btnInfo;
	private Button btnGenerate;
	private Combo cmbCipherSuite;
	private Combo cmbVersion;
	private ThreadedSeedGenerator randGenerator;
	private Button btnNextStep;
	private SslView sslView;
	private List<String> tls0= new ArrayList<String>();
	private List<String> tls1= new ArrayList<String>();
	private List<String> tls2= new ArrayList<String>();


	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ServerHelloComposite(Composite parent, int style, final SslView sslView) 
	{
		super(parent, style);
		
		this.sslView = sslView;
		defineTlsLists();
		
		Group grpServerHello = new Group(this, SWT.NONE);
		grpServerHello.setText(Messages.ServerHelloCompositeGrpServerHello);
		grpServerHello.setBounds(10, 10, 326, 175);
		
		btnInfo = new Button(grpServerHello, SWT.NONE);
		btnInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				/*MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_INFORMATION|SWT.OK);
				 messageBox.setMessage(Messages.ServerHelloInformationText);
				 messageBox.setText(Messages.ServerHelloCompositeBtnInfo);
				 messageBox.open();*/
				infoText=true;
				refreshInformations();
			}
		});
		btnInfo.setBounds(65, 140, 100, 25);
		btnInfo.setText(Messages.ServerHelloCompositeBtnInfo);
		
		Label lblVersion = new Label(grpServerHello, SWT.NONE);
		lblVersion.setBounds(10, 25, 55, 15);
		lblVersion.setText(Messages.ServerHelloCompositeLblVersion);
		
		Label lblRandom = new Label(grpServerHello, SWT.NONE);
		lblRandom.setBounds(10, 55, 55, 15);
		lblRandom.setText(Messages.ServerHelloCompositeLblRandom);
		
		Label lblCipherSuite = new Label(grpServerHello, SWT.NONE);
		lblCipherSuite.setBounds(10, 85, 70, 15);
		lblCipherSuite.setText(Messages.ServerHelloCompositeLblCipherSuite);
		
		Label lblSessionID = new Label(grpServerHello, SWT.NONE);
		lblSessionID.setBounds(10, 115, 55, 15);
		lblSessionID.setText(Messages.ServerHelloCompositeLblSessionID);
		
		txtRandom = new Text(grpServerHello, SWT.BORDER);
		txtRandom.setBounds(90, 52, 140, 23);
		
		cmbCipherSuite = new Combo(grpServerHello, SWT.NONE);
		cmbCipherSuite.setBounds(90, 82, 160, 23);
		for(int i = 0;i<tls2.size();i++)
		{
			cmbCipherSuite.add(tls2.get(i));
		}
		cmbCipherSuite.select(27);
		
		txtSessionID = new Text(grpServerHello, SWT.BORDER);
		txtSessionID.setBounds(90, 112, 75, 23);
		
		btnGenerate = new Button(grpServerHello, SWT.NONE);
		btnGenerate.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseUp(MouseEvent e)
			{
				createRandom();
			}
		});
		btnGenerate.setBounds(240, 50, 75, 25);
		btnGenerate.setText(Messages.ServerHelloCompositeBtnGenerate);
		
		cmbVersion = new Combo(grpServerHello, SWT.NONE);
		cmbVersion.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				cmbCipherSuite.removeAll();
				if(cmbVersion.getSelectionIndex()==0)
				{
					for(int i = 0;i<tls0.size();i++)
					{
						cmbCipherSuite.add(tls0.get(i));
					}
				}
				else if(cmbVersion.getSelectionIndex()==1)
				{
					for(int i = 0;i<tls1.size();i++)
					{
						cmbCipherSuite.add(tls1.get(i));
					}
				}
				else if(cmbVersion.getSelectionIndex()==2)
				{
					for(int i = 0;i<tls2.size();i++)
					{
						cmbCipherSuite.add(tls2.get(i));
					}
				}
				cmbCipherSuite.select(0);
			}
		});
		cmbVersion.setBounds(90, 22, 90, 20);
		cmbVersion.add(Messages.Tls0);
		cmbVersion.add(Messages.Tls1);
		cmbVersion.add(Messages.Tls2);
		cmbVersion.select(2);
		
		btnNextStep = new Button(grpServerHello, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setText(Messages.ServerHelloCompositeBtnNextStep);
		btnNextStep.setBounds(175, 140, 140, 25);
		refreshInformations();
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
	public void refreshInformations()
	{
		String text0 = "";
		String text1 = "";
		String text2 = "";
		for(int i = 0;i < Message.getClientHelloTls0CipherSuites().size();i++)
		{
			text0 += Message.getClientHelloTls0CipherSuites().get(i) +"\n";
		}
		for(int i = 0;i < Message.getClientHelloTls1CipherSuites().size();i++)
		{
			text1 += Message.getClientHelloTls1CipherSuites().get(i) +"\n";
		}
		for(int i = 0;i < Message.getClientHelloTls2CipherSuites().size();i++)
		{
			text2 += Message.getClientHelloTls2CipherSuites().get(i) +"\n";
		}
		if(infoText)
		{
			sslView.setStxInformationText(Messages.stxInformationSelectedCiphers + text0 +Messages.stxInformationTLS1 + text1 + Messages.stxInformationTLS2 + text2 + "\n" + Messages.stxInformationRandomValue + Message.getClientHelloRandom()+"\n\n"+ Messages.stxInformationCipherSuitesExchanged+Messages.stxInformationRandomValue+ txtRandom.getText()+"\n\n" +Messages.ServerHelloInformationText);
		}
		else
		{
			sslView.setStxInformationText(Messages.stxInformationSelectedCiphers + text0 +Messages.stxInformationTLS1 + text1 + Messages.stxInformationTLS2 + text2 + "\n" + Messages.stxInformationRandomValue + Message.getClientHelloRandom()+"\n\n"+Messages.stxInformationCipherSuitesExchanged+Messages.stxInformationRandomValue+ txtRandom.getText());
		}
	}
	private String bytArrayToHex(byte[] a) 
	{
		   StringBuilder sb = new StringBuilder();
		   for(byte b: a)
		      sb.append(String.format("%02x", b&0xff));
		   return sb.toString();
		}
	
	/* (non-Javadoc)
	 * @see protocol.ProtocolStep#enableControls()
	 */
	public void enableControls()
	{
		btnInfo.setEnabled(true);
		btnGenerate.setEnabled(true);
		txtRandom.setEnabled(true);
		txtSessionID.setEnabled(true);
		cmbVersion.setEnabled(true);
		cmbCipherSuite.setEnabled(true);
		btnNextStep.setEnabled(true);
	}
	
	/* (non-Javadoc)
	 * @see protocol.ProtocolStep#disableControls()
	 */
	public void disableControls() 
	{
		btnInfo.setEnabled(false);
		btnGenerate.setEnabled(false);
		txtRandom.setEnabled(false);
		txtSessionID.setEnabled(false);
		cmbVersion.setEnabled(false);
		cmbCipherSuite.setEnabled(false);
		btnNextStep.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see protocol.ProtocolStep#checkParameters()
	 */
	public boolean checkParameters()
	{
		if(txtSessionID.getText()=="")
		{
			txtSessionID.setText("1");
		}
		if(txtRandom.getText()=="")
		{
			createRandom();
		}
		try
		{
			if(txtRandom.getText().length()!=64)
			{
				throw new IllegalArgumentException();
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
		catch(IllegalArgumentException exc)
		{
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING|SWT.OK);
			messageBox.setMessage(Messages.ServerHelloCompositeErrorRandomShort);
			messageBox.setText(Messages.ServerHelloCompositeError);
		    messageBox.open();
			return false;
		}
		try
		{
			Integer.parseInt(txtSessionID.getText());
		}
		catch(NumberFormatException exc)
		{
			 MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ICON_WARNING|SWT.OK);
			 messageBox.setMessage(Messages.ServerHelloCompositeErrorSessionID);
			 messageBox.setText(Messages.ServerHelloCompositeError);
			 messageBox.open();
			return false;
		}
		if(cmbVersion.getSelectionIndex()==0&&Message.getClientHelloTls0CipherSuites().contains(cmbCipherSuite.getItem(cmbCipherSuite.getSelectionIndex()))==false)
		{
			jumpToClientHello();
			return false;
		}
		else if(cmbVersion.getSelectionIndex()==1&&Message.getClientHelloTls1CipherSuites().contains(cmbCipherSuite.getItem(cmbCipherSuite.getSelectionIndex()))==false)
		{
			jumpToClientHello();
			return false;
		}
		else if(cmbVersion.getSelectionIndex()==2&&Message.getClientHelloTls2CipherSuites().contains(cmbCipherSuite.getItem(cmbCipherSuite.getSelectionIndex()))==false)
		{
			jumpToClientHello();
			return false;
		}
		setMessageProperties();
		Attacks attack = new Attacks();
		return attack.getDecision();
		//return true;
	}
	
	private void jumpToClientHello()
	{
		sslView.backToClientHello();
		sslView.setStxInformationText(Messages.stxInformationCipherSuiteRefused1 + cmbCipherSuite.getItem(cmbCipherSuite.getSelectionIndex())
				+ Messages.stxInformationCipherSuiteRefused2);
	}
	
	private void setMessageProperties()
	{
		int selectedItem = cmbCipherSuite.getSelectionIndex();
		Message.setServerHelloCipherSuite(cmbCipherSuite.getItem(selectedItem));
		Message.setServerHelloVersion(cmbVersion.getSelectionIndex());
		Message.setServerHelloSessionID(Integer.parseInt(txtSessionID.getText().toString()));
		Message.setServerHelloRandom(txtRandom.getText());
		if(cmbCipherSuite.getItem(selectedItem).contains("SHA256"))
		{
			Message.setServerHelloHash("SHA256");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("SHA384"))
		{
			Message.setServerHelloHash("SHA384");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("SHA"))
		{
			Message.setServerHelloHash("SHA1");
		}
		else
		{
			Message.setServerHelloHash("MD5");
		}
		if(cmbCipherSuite.getItem(selectedItem).contains("DHE_RSA"))
		{
			Message.setServerHelloKeyExchange("DHE_RSA");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("DHE_DSS"))
		{
			Message.setServerHelloKeyExchange("DHE_DSS");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("DH_RSA"))
		{
			Message.setServerHelloKeyExchange("DH_RSA");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("DH_DSS"))
		{
			Message.setServerHelloKeyExchange("DH_DSS");
		}
		else
		{
			Message.setServerHelloKeyExchange("RSA");
		}
		if(cmbCipherSuite.getItem(selectedItem).contains("AES_256"))
		{
			Message.setServerHelloCipher("AES_256");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("AES_128"))
		{
			Message.setServerHelloCipher("AES_128");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("3DES"))
		{
			Message.setServerHelloCipher("3DES");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("DES"))
		{
			Message.setServerHelloCipher("DES");
		}
		else if(cmbCipherSuite.getItem(selectedItem).contains("RC4_128"))
		{
			Message.setServerHelloCipher("RC4_128");
		}
		else
		{
			Message.setServerHelloCipher("NULL");
		}
		if(cmbCipherSuite.getItem(selectedItem).contains("GCM"))
		{
			Message.setServerHelloCipherMode("GCM");
		}
		else
		{
			Message.setServerHelloCipherMode("CBC");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
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
