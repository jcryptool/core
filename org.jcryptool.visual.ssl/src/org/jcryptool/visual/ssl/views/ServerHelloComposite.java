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
 * Class that represents the ServerHello Step of SSL/TLS
 * 
 * @author Kapfer
 * 
 */
public class ServerHelloComposite extends Composite implements ProtocolStep {
	private boolean infoText = false;
	private Text txtRandom;
	private Text txtSessionID;
	private Button btnInfo;
	private Button btnGenerate;
	private Combo cmbCipherSuite;
	private Combo cmbVersion;
	private SecureRandom randGenerator;
	private Button btnNextStep;
	private SslView sslView;
	private List<String> tls0 = new ArrayList<String>();
	private List<String> tls1 = new ArrayList<String>();
	private List<String> tls2 = new ArrayList<String>();
	private List<String> tls0Hex = new ArrayList<String>();
	private List<String> tls1Hex = new ArrayList<String>();
	private List<String> tls2Hex = new ArrayList<String>();
	
	private Group grpServerHello;
	private Label lblVersion;
	private Label lblRandom;
	private Label lblCipherSuite;
	private Label lblSessionID;

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
	public ServerHelloComposite(Composite parent, int style,
			final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;
		
		defineTlsLists();
		
		grpServerHello = new Group(this, SWT.NONE);
		grpServerHello.setBounds(0, 0, 326, 175);
		grpServerHello.setText(Messages.ServerHelloCompositeGrpServerHello);
		
		btnInfo = new Button(grpServerHello, SWT.NONE);
		btnInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				infoText = !infoText;
				if(btnInfo.getText().equals(Messages.btnInformationToggleParams)){
					btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
				}else{
					btnInfo.setText(Messages.btnInformationToggleParams);
				}
				refreshInformations();
			}
		});
		btnInfo.setBounds(65, 140, 100, 25);
		btnInfo.setText(Messages.ServerHelloCompositeBtnInfo);
		
		lblVersion = new Label(grpServerHello, SWT.NONE);
		lblVersion.setBounds(10, 25, 55, 15);
		lblVersion.setText(Messages.ServerHelloCompositeLblVersion);

		lblRandom = new Label(grpServerHello, SWT.NONE);
		lblRandom.setBounds(10, 55, 55, 15);
		lblRandom.setText(Messages.ServerHelloCompositeLblRandom);

		lblCipherSuite = new Label(grpServerHello, SWT.NONE);
		lblCipherSuite.setBounds(10, 85, 70, 15);
		lblCipherSuite.setText(Messages.ServerHelloCompositeLblCipherSuite);

		lblSessionID = new Label(grpServerHello, SWT.NONE);
		lblSessionID.setBounds(10, 115, 55, 15);
		lblSessionID.setText(Messages.ServerHelloCompositeLblSessionID);

		txtRandom = new Text(grpServerHello, SWT.BORDER);
		txtRandom.setBounds(90, 52, 140, 23);

		cmbCipherSuite = new Combo(grpServerHello, SWT.NONE);
		cmbCipherSuite.setBounds(90, 82, 160, 23);
		
		txtSessionID = new Text(grpServerHello, SWT.BORDER);
		txtSessionID.setBounds(90, 112, 75, 23);

		btnGenerate = new Button(grpServerHello, SWT.NONE);
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				createRandom();
			}
		});
		btnGenerate.setBounds(240, 50, 75, 25);
		btnGenerate.setText(Messages.ServerHelloCompositeBtnGenerate);

		cmbVersion = new Combo(grpServerHello, SWT.NONE);
		cmbVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cmbCipherSuite.removeAll();
				if (cmbVersion.getSelectionIndex() == 0) {
					for (int i = 0; i < tls0.size(); i++) {
						cmbCipherSuite.add(tls0.get(i));
					}
				} else if (cmbVersion.getSelectionIndex() == 1) {
					for (int i = 0; i < tls1.size(); i++) {
						cmbCipherSuite.add(tls1.get(i));
					}
				} else if (cmbVersion.getSelectionIndex() == 2) {
					for (int i = 0; i < tls2.size(); i++) {
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
		btnNextStep.setBounds(175, 140, 140, 25);
		btnNextStep.setText(Messages.ServerHelloCompositeBtnNextStep);
		
	}

	/**
	 * 
	 */
	public void startStep() {
		
		cmbVersion.select(2);
		cmbCipherSuite.removeAll();
		for (int i = 0; i < tls2.size(); i++) {
			cmbCipherSuite.add(tls2.get(i));
		}
		cmbCipherSuite.select(33);

		refreshInformations();
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

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#refreshInformations()
	 */
	public void refreshInformations() {
		String text0 = "";
		String text1 = "";
		String text2 = "";
		for (int i = 0; i < Message.getClientHelloTls0CipherSuites().size(); i++) {
			text0 += Message.getClientHelloTls0CipherSuites().get(i) + "\n";
		}
		for (int i = 0; i < Message.getClientHelloTls1CipherSuites().size(); i++) {
			text1 += Message.getClientHelloTls1CipherSuites().get(i) + "\n";
		}
		for (int i = 0; i < Message.getClientHelloTls2CipherSuites().size(); i++) {
			text2 += Message.getClientHelloTls2CipherSuites().get(i) + "\n";
		}
		if (infoText) {
			sslView.setStxInformationText(Messages.ServerHelloInformationText);
		}
		else if(txtRandom.getText().equals(""))
		{
			String infoText="";
			infoText += Messages.stxInformationSelectedCiphers;
			if(Message.getClientHelloTls0CipherSuites().isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS0+text0;
			}
			if(Message.getClientHelloTls1CipherSuites().isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS1+text1;
			}
			if(Message.getClientHelloTls2CipherSuites().isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS2+text2;
			}
			infoText+="\n"+Messages.stxInformationRandomValue+Message.getClientHelloRandom()+"\n\n"+Messages.stxInformationCipherSuitesExchanged;
			sslView.setStxInformationText(infoText);
		}
		else 
		{
			String infoText="";
			infoText += Messages.stxInformationSelectedCiphers;
			if(Message.getClientHelloTls0CipherSuites().isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS0+text0;
			}
			if(Message.getClientHelloTls1CipherSuites().isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS1+text1;
			}
			if(Message.getClientHelloTls2CipherSuites().isEmpty()==false)
			{
				infoText+=Messages.stxInformationTLS2+text2;
			}
			infoText+="\n"+Messages.stxInformationRandomValue+Message.getClientHelloRandom()
					+"\n\n"+Messages.stxInformationCipherSuitesExchanged
					+Messages.stxInformationServerHello
					+Messages.stxInformationRandomValue + txtRandom.getText();
			sslView.setStxInformationText(infoText);
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see protocol.ProtocolStep#enableControls()
	 */
	public void enableControls() {
		btnInfo.setEnabled(true);
		btnGenerate.setEnabled(true);
		txtRandom.setEnabled(true);
		txtSessionID.setEnabled(true);
		cmbVersion.setEnabled(true);
		cmbCipherSuite.setEnabled(true);
		btnNextStep.setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see protocol.ProtocolStep#disableControls()
	 */
	public void disableControls() {
		btnInfo.setEnabled(false);
		btnGenerate.setEnabled(false);
		txtRandom.setEnabled(false);
		txtSessionID.setEnabled(false);
		cmbVersion.setEnabled(false);
		cmbCipherSuite.setEnabled(false);
		btnNextStep.setEnabled(false);
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see protocol.ProtocolStep#checkParameters()
	 */
	public boolean checkParameters() {
		String length = "00004c";
		String type ="02";
		String sessionIDLength="";
		String sessionID="";
		String cipherSuite="";
		
		if (txtSessionID.getText().equals("")) {
			txtSessionID.setText("1");
		}
		if (txtRandom.getText().equals("")) {
			createRandom();
		}
		try {
			if (txtRandom.getText().length() != 64) {
				throw new IllegalArgumentException();
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
		} catch (IllegalArgumentException exc) {
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING
					| SWT.OK);
			messageBox
					.setMessage(Messages.ServerHelloCompositeErrorRandomShort);
			messageBox.setText(Messages.ServerHelloCompositeError);
			messageBox.open();
			return false;
		}
		
		try 
		{
			if(txtSessionID.getText().length()>64)
			{
				throw new IllegalArgumentException();
			}
			int j = 0;
			int k = 0;
			for (int i = 0; i <= txtSessionID.getText().length()/8; i++) {
				if(i*8==txtSessionID.getText().length())
				{
					k++;
				}
				else if(i == txtSessionID.getText().length()/8)
				{
					if(Long.parseLong(txtSessionID.getText(i * 8, txtSessionID.getText().length()), 16)==0)
					{
						k++;
					}
				}
				else
				{
					if(Long.parseLong(txtSessionID.getText(i * 8, (i + 1) * 8), 16)==0)
					{
						k++;
					}
				}
				j++;
			}	
			if(j==k)
			{
				throw new IllegalStateException();
			}
		} 
		catch (IllegalStateException exc)
		{
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING
					| SWT.OK);
			messageBox.setMessage(Messages.ServerHelloCompositeErrorSessionIDNull);
			messageBox.setText(Messages.ServerHelloCompositeError);
			messageBox.open();
			return false;
		}
		catch (NumberFormatException exc) 
		{
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING
					| SWT.OK);
			messageBox.setMessage(Messages.ServerHelloCompositeErrorSessionID);
			messageBox.setText(Messages.ServerHelloCompositeError);
			messageBox.open();
			return false;
		}
		catch (IllegalArgumentException exc) {
			MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING
					| SWT.OK);
			messageBox.setMessage(Messages.ServerHelloCompositeErrorSessionIDLength);
			messageBox.setText(Messages.ServerHelloCompositeError);
			messageBox.open();
			return false;
		}
		if (cmbVersion.getSelectionIndex() == 0
				&& Message.getClientHelloTls0CipherSuites().contains(
						cmbCipherSuite.getItem(cmbCipherSuite
								.getSelectionIndex())) == false) {
			jumpToClientHello();
			return false;
		} else if (cmbVersion.getSelectionIndex() == 1
				&& Message.getClientHelloTls1CipherSuites().contains(
						cmbCipherSuite.getItem(cmbCipherSuite
								.getSelectionIndex())) == false) {
			jumpToClientHello();
			return false;
		} else if (cmbVersion.getSelectionIndex() == 2
				&& Message.getClientHelloTls2CipherSuites().contains(
						cmbCipherSuite.getItem(cmbCipherSuite
								.getSelectionIndex())) == false) {
			jumpToClientHello();
			return false;
		}
		infoText = false;
		refreshInformations();
		setMessageProperties();
		
		if(txtSessionID.getText().length()%2==0)
		{
			sessionID=txtSessionID.getText();
			sessionIDLength=Integer.toString((txtSessionID.getText().length())/2);
		}
		else
		{
			sessionID="0"+txtSessionID.getText();
			sessionIDLength=Integer.toString((txtSessionID.getText().length()+1)/2);
		}
		
		Message.setMessageServerHello(type+length+Message.getServerHelloVersion()+txtRandom.getText()+sessionIDLength+sessionID+cipherSuite);
		
		Attacks attack = new Attacks();
		return attack.getDecision();
	}

	/**
	 * Jumps back to Client Hello (used when there are no matching cipher suites)
	 */
	private void jumpToClientHello() {
		sslView.backToClientHello();
		sslView.setStxInformationText(Messages.stxInformationCipherSuiteRefused1
				+ cmbCipherSuite.getItem(cmbCipherSuite.getSelectionIndex())
				+ Messages.stxInformationCipherSuiteRefused2);
	}

	/**
	 * Sets all the properties of Server Hello in the Message class
	 */
	private void setMessageProperties() {
		int selectedItem = cmbCipherSuite.getSelectionIndex();
		Message.setServerHelloCipherSuite(cmbCipherSuite.getItem(selectedItem));
		if(cmbVersion.getSelectionIndex()==0){
			Message.setServerHelloVersion("0301");
		}else if(cmbVersion.getSelectionIndex()==1){
			Message.setServerHelloVersion("0302");
		}else{
			Message.setServerHelloVersion("0303");
		}
		
		Message.setServerHelloSessionID(txtSessionID.getText());
		Message.setServerHelloRandom(txtRandom.getText());
		if (cmbCipherSuite.getItem(selectedItem).contains("SHA256")) {
			Message.setServerHelloHash("SHA256");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("SHA384")) {
			Message.setServerHelloHash("SHA384");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("SHA")) {
			Message.setServerHelloHash("SHA1");
		} else {
			Message.setServerHelloHash("MD5");
		}
		if (cmbCipherSuite.getItem(selectedItem).contains("DHE_RSA")) {
			Message.setServerHelloKeyExchange("DHE_RSA");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DHE_DSS")) {
			Message.setServerHelloKeyExchange("DHE_DSS");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DH_RSA")) {
			Message.setServerHelloKeyExchange("DH_RSA");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DH_DSS")) {
			Message.setServerHelloKeyExchange("DH_DSS");
		} else {
			Message.setServerHelloKeyExchange("RSA");
		}
		if (cmbCipherSuite.getItem(selectedItem).contains("AES_256")) {
			Message.setServerHelloCipher("AES_256");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("AES_128")) {
			Message.setServerHelloCipher("AES_128");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("3DES")) {
			Message.setServerHelloCipher("3DES");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DES")) {
			Message.setServerHelloCipher("DES");
		} else if (cmbCipherSuite.getItem(selectedItem).contains("RC4_128")) {
			Message.setServerHelloCipher("RC4_128");
		} else {
			Message.setServerHelloCipher("NULL");
		}
		if (cmbCipherSuite.getItem(selectedItem).contains("GCM")) {
			Message.setServerHelloCipherMode("GCM");
		} else {
			Message.setServerHelloCipherMode("CBC");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
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
		
		tls1Hex.add("0005");
		tls1Hex.add("000A");
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

		tls1Hex.add("000D");
		tls1Hex.add("0010");
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
		txtRandom.setText("");
		txtSessionID.setText("");
		cmbCipherSuite.select(33);
		cmbVersion.select(2);
		infoText=false;
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
