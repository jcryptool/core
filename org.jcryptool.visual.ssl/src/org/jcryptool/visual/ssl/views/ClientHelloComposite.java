//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.ssl.views;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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
 * Class that represents the ClientHello Step of SSL/TLS
 * 
 * @author Kapfer
 *
 */
public class ClientHelloComposite extends Composite implements ProtocolStep {
	private Text txtRandom;
	private boolean infoText = false;
	private Button btnInformation;
	private Button btnGenerate;
	private Combo cmdCipher;
	private Combo cmdVersion;
	private Button btnNextStep;
	private SslView sslView;
	private SecureRandom randGenerator;
	private List<String> tls0 = new ArrayList<String>();
	private List<String> tls1 = new ArrayList<String>();
	private List<String> tls2 = new ArrayList<String>();
	private List<String> tls0CipherSuites = new ArrayList<String>();
	private List<String> tls1CipherSuites = new ArrayList<String>();
	private List<String> tls2CipherSuites = new ArrayList<String>();
	private List<String> tls0CipherSuitesHex = new ArrayList<String>();
	private List<String> tls1CipherSuitesHex = new ArrayList<String>();
	private List<String> tls2CipherSuitesHex = new ArrayList<String>();
	private List<String> tls0Hex = new ArrayList<String>();
	private List<String> tls1Hex = new ArrayList<String>();
	private List<String> tls2Hex = new ArrayList<String>();
	
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * 			the parent composite
	 * @param style
	 * 			the style when called from SslView
	 * @param sslView
	 * 			the element who calls, in our case SslView
	 */
	public ClientHelloComposite(Composite parent, int style,final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;

		defineTlsLists();

		Group grpClientHello = new Group(this, SWT.NONE);
		grpClientHello.setText(Messages.ClientHelloCompositeGrpClientHello);
		grpClientHello.setBounds(0, 0, 330, 175);

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
		

		cmdVersion = new Combo(grpClientHello, SWT.NONE);
		cmdVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmdCipher.removeAll();
				if (cmdVersion.getSelectionIndex() == 0) {
					for (int i = 0; i < tls0.size(); i++) {
						cmdCipher.add(tls0.get(i));
					}
				} else if (cmdVersion.getSelectionIndex() == 1) {
					for (int i = 0; i < tls1.size(); i++) {
						cmdCipher.add(tls1.get(i));
					}
				} else if (cmdVersion.getSelectionIndex() == 2) {
					for (int i = 0; i < tls2.size(); i++) {
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

		cmdCipher = new Combo(grpClientHello, SWT.NONE);
		cmdCipher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (cmdVersion.getSelectionIndex() == 0) {
					if (tls0CipherSuites.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex()))) 
					{
						tls0CipherSuites.remove(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
						tls0CipherSuitesHex.remove(tls0Hex.get(cmdCipher.getSelectionIndex()));
					} 
					else if (tls0.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex()))) 
					{
						tls0CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
						tls0CipherSuitesHex.add(tls0Hex.get(cmdCipher.getSelectionIndex()));
					}
				}
				if (cmdVersion.getSelectionIndex() == 1) {
					if (tls1CipherSuites.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex()))) 
					{
						tls1CipherSuites.remove(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
						tls1CipherSuitesHex.remove(tls1Hex.get(cmdCipher.getSelectionIndex()));
					} 
					else if (tls1.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex()))) 
					{
						tls1CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
						tls1CipherSuitesHex.add(tls1Hex.get(cmdCipher.getSelectionIndex()));
					}
				}
				if (cmdVersion.getSelectionIndex() == 2) {
					if (tls2CipherSuites.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex()))) 
					{
						tls2CipherSuites.remove(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
						tls2CipherSuitesHex.remove(tls2Hex.get(cmdCipher.getSelectionIndex()));
					} 
					else if (tls2.contains(cmdCipher.getItem(cmdCipher.getSelectionIndex()))) 
					{
						tls2CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
						tls2CipherSuitesHex.add(tls2Hex.get(cmdCipher.getSelectionIndex()));
					}
				}
				refreshInformations();
			}
		});

		cmdCipher.setLocation(90, 82);
		cmdCipher.setSize(160, 23);
		for (int i = 0; i < tls2.size(); i++) {
			cmdCipher.add(tls2.get(i));
		}
		cmdCipher.select(33);


		btnInformation = new Button(grpClientHello, SWT.NONE);
		btnInformation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInformation.getText().equals(Messages.btnInformationToggleParams)){
					btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInformation.setText(Messages.btnInformationToggleParams);
				}
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
		tls2CipherSuitesHex.add(tls2Hex.get(cmdCipher.getSelectionIndex()));
		if(txtRandom.getText().equals("")) {
			sslView.setStxInformationText(Messages.stxInformationSelectedCiphers
					+ Messages.stxInformationTLS1 + Messages.stxInformationTLS2
					+ tls2CipherSuites.get(0) + "\n\n");
		}else {
			sslView.setStxInformationText(Messages.stxInformationSelectedCiphers
					+ Messages.stxInformationTLS1 + Messages.stxInformationTLS2
					+ tls2CipherSuites.get(0) + "\n\n"
					+ Messages.stxInformationRandomValue + txtRandom.getText() + "\n\n");
		}
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#refreshInformations()
	 */
	public void refreshInformations() {
		String text0 = "";
		String text1 = "";
		String text2 = "";
		for (int i = 0; i < tls0CipherSuites.size(); i++) {
			text0 += tls0CipherSuites.get(i) + "\n";
		}
		for (int i = 0; i < tls1CipherSuites.size(); i++) {
			text1 += tls1CipherSuites.get(i) + "\n";
		}
		for (int i = 0; i < tls2CipherSuites.size(); i++) {
			text2 += tls2CipherSuites.get(i) + "\n";
		}
		if (infoText) {
			sslView.setStxInformationText(Messages.ClientHelloInformationText);
		} 
		else if(txtRandom.getText().equals(""))
		{
			String infoText="";
			infoText += Messages.stxInformationSelectedCiphers;
			if(tls0CipherSuites.isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS0+text0;
			}
			if(tls1CipherSuites.isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS1+text1;
			}
			if(tls2CipherSuites.isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS2+text2;
			}
			infoText+="\n\n";
			sslView.setStxInformationText(infoText);
		}
		else 
		{
			String infoText="";
			infoText += Messages.stxInformationSelectedCiphers;
			if(tls0CipherSuites.isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS0+text0;
			}
			if(tls1CipherSuites.isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS1+text1;
			}
			if(tls2CipherSuites.isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS2+text2;
			}
			infoText+="\n"+Messages.stxInformationRandomValue+txtRandom.getText();
			infoText+="\n\n";
			sslView.setStxInformationText(infoText);
		}
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#enableControls()
	 */
	public void enableControls() {
		txtRandom.setEnabled(true);
		btnInformation.setEnabled(true);
		btnGenerate.setEnabled(true);
		cmdCipher.setEnabled(true);
		cmdVersion.setEnabled(true);
		btnNextStep.setEnabled(true);
	}

	/**
	 * Creates a random number and writes it into the random textbox
	 */
	private void createRandom() {
		String text = "";
		long gmtUnixTime = (new java.util.Date().getTime() / 1000);

		text += (Long.toHexString(gmtUnixTime));
		randGenerator = new SecureRandom();
		byte[] rand = randGenerator.generateSeed(28);
		text += bytArrayToHex(rand);
		txtRandom.setText(text);
		refreshInformations();
	}

	/**
	 * Converts a byte array into a string hex value 
	 *
	 * 
	 * @param a
	 * 			the byte array to be converted
	 * @return
	 * 			the hex string
	 */
	private String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#disableControls()
	 */
	public void disableControls() {
		txtRandom.setEnabled(false);
		btnInformation.setEnabled(false);
		btnGenerate.setEnabled(false);
		cmdCipher.setEnabled(false);
		cmdVersion.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#checkParameters()
	 */
	public boolean checkParameters() {
		String version="";
		String length = "0000ad";
		String type ="01";
		String sessionID="00";
		String cipherSuite="";
		String cipherSuiteLength="";
		
		if(txtRandom.getText().equals("")){
			createRandom();
		}
		try {
			if (txtRandom.getText().length() != 64) {
				throw new Exception();
			}
			for (int i = 0; i < 8; i++) {
				Long.parseLong(txtRandom.getText(i * 8, (i + 1) * 8), 16);
			}
		} catch (NumberFormatException exc) {
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING
					| SWT.OK);
			messageBox.setMessage(Messages.ServerHelloCompositeErrorRandom);
			messageBox.setText(Messages.ServerHelloCompositeError);
			messageBox.open();
			return false;
		} catch (Exception exc) {
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING
					| SWT.OK);
			messageBox
					.setMessage(Messages.ServerHelloCompositeErrorRandomShort);
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
		infoText = false;
		
		version ="";
		if(tls2CipherSuitesHex.size()!=0)
		{
			version = "0303";
			
		}
		else if(tls1CipherSuitesHex.size()!=0)
		{
			version = "0302";
		}
		else
		{
			version = "0301";
		}
		
		for(int i = 0;i < tls0CipherSuitesHex.size();i++)
		{
			cipherSuite += tls0CipherSuitesHex.get(i); 
		}
		for(int i = 0;i < tls1CipherSuitesHex.size();i++)
		{
			cipherSuite += tls1CipherSuitesHex.get(i); 
		}
		for(int i = 0;i < tls2CipherSuitesHex.size();i++)
		{
			cipherSuite += tls2CipherSuitesHex.get(i); 
		}
		cipherSuiteLength = Integer.toHexString(tls0CipherSuitesHex.size()*2+tls1CipherSuitesHex.size()*2+tls2CipherSuitesHex.size()*2);
		if(cipherSuiteLength.length()==1)
		{
			cipherSuiteLength = "000" + cipherSuiteLength;
		}
		else if(cipherSuiteLength.length()==2)
		{
			cipherSuiteLength = "00" + cipherSuiteLength;
		}
		if(cipherSuiteLength.length()==3)
		{
			cipherSuiteLength = "0" + cipherSuiteLength;
		}
		Message.setMessageClientHello(type + length + version + txtRandom.getText() + sessionID + cipherSuiteLength + cipherSuite);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Composite#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * Fills the arraylists for the cipher suites and the arraylists for the cipher suites hex values with values
	 */
	private void defineTlsLists() {
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

		tls0Hex.add("0001");
		tls0Hex.add("0002");
		tls0Hex.add("0004");
		tls0Hex.add("0005");
		tls0Hex.add("000A");
		tls0Hex.add("0009");
		tls0Hex.add("000D");
		tls0Hex.add("000C");
		tls0Hex.add("0010");
		tls0Hex.add("000F");
		tls0Hex.add("0013");
		tls0Hex.add("0012");
		tls0Hex.add("0016");
		tls0Hex.add("0015");
		
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

		tls1Hex.add("0001");
		tls1Hex.add("0002");
		tls1Hex.add("0004");
		tls1Hex.add("0005");
		tls1Hex.add("000A");
		tls1Hex.add("0009");
		tls1Hex.add("000D");
		tls1Hex.add("000C");
		tls1Hex.add("0010");
		tls1Hex.add("000F");
		tls1Hex.add("0013");
		tls1Hex.add("0012");
		tls1Hex.add("0016");
		tls1Hex.add("0015");

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

		tls1Hex.add("002F");
		tls1Hex.add("0030");
		tls1Hex.add("0031");
		tls1Hex.add("0032");
		tls1Hex.add("0033");
		tls1Hex.add("0035");
		tls1Hex.add("0036");
		tls1Hex.add("0037");
		tls1Hex.add("0038");
		tls1Hex.add("0039");
		
		tls2.add(Messages.TLS2_RSA_WITH_NULL_MD5);
		tls2.add(Messages.TLS2_RSA_WITH_NULL_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_NULL_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_RC4_128_MD5);
		
		tls2Hex.add("0001");
		tls2Hex.add("0002");
		tls2Hex.add("003B");
		tls2Hex.add("0004");

		tls2.add(Messages.TLS2_RSA_WITH_RC4_128_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_CBC_SHA);
		
		tls2Hex.add("0005");
		tls2Hex.add("000A");
		tls2Hex.add("002F");
		tls2Hex.add("0035");

		tls2.add(Messages.TLS2_RSA_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_CBC_SHA256);
		
		tls2Hex.add("003C");
		tls2Hex.add("003D");

		tls2.add(Messages.TLS2_DH_DSS_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_3DES_EDE_CBC_SHA);

		tls2Hex.add("000D");
		tls2Hex.add("0010");
		tls2Hex.add("0013");
		tls2Hex.add("0016");
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_CBC_SHA);

		tls2Hex.add("0030");
		tls2Hex.add("0031");
		tls2Hex.add("0032");
		tls2Hex.add("0033");
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_CBC_SHA);

		tls2Hex.add("0036");
		tls2Hex.add("0037");
		tls2Hex.add("0038");
		tls2Hex.add("0039");
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_CBC_SHA256);

		tls2Hex.add("003E");
		tls2Hex.add("003F");
		tls2Hex.add("0040");
		tls2Hex.add("0067");
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_CBC_SHA256);
		
		tls2Hex.add("0068");
		tls2Hex.add("0069");
		tls2Hex.add("006A");
		tls2Hex.add("006B");
		
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
		
		tls2Hex.add("009C");
		tls2Hex.add("009D");
		tls2Hex.add("009E");
		tls2Hex.add("009F");
		tls2Hex.add("00A0");
		tls2Hex.add("00A1");
		tls2Hex.add("00A2");
		tls2Hex.add("00A3");
		tls2Hex.add("00A4");
		tls2Hex.add("00A5");
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#resetStep()
	 */
	@Override
	public void resetStep() {
		tls0CipherSuites.clear();
		tls1CipherSuites.clear();
		tls2CipherSuites.clear();
		tls0CipherSuitesHex.clear();
		tls1CipherSuitesHex.clear();
		tls2CipherSuitesHex.clear();
		cmdVersion.select(2);
		cmdCipher.removeAll();
		for (int i = 0; i < tls2.size(); i++) {
			cmdCipher.add(tls2.get(i));
		}
		cmdCipher.select(33);
		tls2CipherSuites.add(cmdCipher.getItem(cmdCipher.getSelectionIndex()));
		tls2CipherSuitesHex.add(tls2Hex.get(cmdCipher.getSelectionIndex()));
		txtRandom.setText("");
		infoText=false;
		refreshInformations();
		btnInformation.setText(Messages.ClientCertificateCompositeBtnInfo);
		
	}
}
