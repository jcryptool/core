//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2014, 2020 JCrypTool Team and Contributors
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
	private Label checkSelectionLabel;
	private GridData checkSelectionLabelLayout;

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
	public ServerHelloComposite(Composite parent, int style, final SslView sslView) {
		super(parent, style);
		this.sslView = sslView;
		
		defineTlsLists();
		
		grpServerHello = new Group(this, SWT.NONE);
		grpServerHello.setLayout(new GridLayout(6, false));
		grpServerHello.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		grpServerHello.setText(Messages.ServerHelloCompositeGrpServerHello);
		
		//Version
		lblVersion = new Label(grpServerHello, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblVersion.setText(Messages.ServerHelloCompositeLblVersion);
		
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
				checkSelection();
			}
		});
		cmbVersion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		cmbVersion.add(Messages.Tls0);
		cmbVersion.add(Messages.Tls1);
		cmbVersion.add(Messages.Tls2);
		cmbVersion.select(2);
		
		//Random 
		lblRandom = new Label(grpServerHello, SWT.NONE);
		lblRandom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblRandom.setText(Messages.ServerHelloCompositeLblRandom);
		
		txtRandom = new Text(grpServerHello, SWT.BORDER);
		txtRandom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));
		
		btnGenerate = new Button(grpServerHello, SWT.NONE);
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				createRandom();
			}
		});
		btnGenerate.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		btnGenerate.setText(Messages.ServerHelloCompositeBtnGenerate);
		
		//Cipher
		lblCipherSuite = new Label(grpServerHello, SWT.NONE);
		lblCipherSuite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblCipherSuite.setText(Messages.ServerHelloCompositeLblCipherSuite);
		
		cmbCipherSuite = new Combo(grpServerHello, SWT.NONE);
		cmbCipherSuite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		cmbCipherSuite.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkSelection();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//SessionId
		lblSessionID = new Label(grpServerHello, SWT.NONE);
		lblSessionID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSessionID.setText(Messages.ServerHelloCompositeLblSessionID);
		
		txtSessionID = new Text(grpServerHello, SWT.BORDER);
		txtSessionID.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		//Buttons
		Composite btnComposite = new Composite(grpServerHello, SWT.NONE);
		btnComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
		btnComposite.setLayout(new GridLayout(2, true));
		
		btnInfo = new Button(btnComposite, SWT.NONE);
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
		btnInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnInfo.setText(Messages.ServerHelloCompositeBtnInfo);

		btnNextStep = new Button(btnComposite, SWT.NONE);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				sslView.nextStep();
			}
		});
		btnNextStep.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnNextStep.setText(Messages.ServerHelloCompositeBtnNextStep);
		
		checkSelectionLabel = new Label(btnComposite, SWT.WRAP);
		checkSelectionLabelLayout = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		checkSelectionLabel.setLayoutData(checkSelectionLabelLayout);
		
		checkSelectionLabel.setText(""); //$NON-NLS-1$
		checkSelectionLabel.setForeground(new Color(getDisplay(), 190, 100, 20));
	}

	protected void checkSelection() {
		String checkParametersResult = checkCipherSuite();
		if (checkParametersResult == ProtocolStep.OK || checkParametersResult.length() == 0) {
			changeCheckSelectionLabel(false, ""); //$NON-NLS-1$
		}
		else {
			changeCheckSelectionLabel(true, checkParametersResult);
		}
	}

	private void changeCheckSelectionLabel(boolean b, String string) {
		GridData l = checkSelectionLabelLayout;
		Label w = checkSelectionLabel;
		w.setText(string);
		w.setVisible(b);
		l.exclude = ! b;
		sslView.rootComp.layout(new Control[] {w});
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
		checkSelection();
	}

	/**
	 * Creates a random number and writes it into the random textbox
	 */
	private void createRandom() {
		String text = ""; //$NON-NLS-1$
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
	@Override
	public void refreshInformations() {
		String text0 = ""; //$NON-NLS-1$
		String text1 = ""; //$NON-NLS-1$
		String text2 = ""; //$NON-NLS-1$
		for (int i = 0; i < Message.getClientHelloTls0CipherSuites().size(); i++) {
			text0 += Message.getClientHelloTls0CipherSuites().get(i) + "\n"; //$NON-NLS-1$
		}
		for (int i = 0; i < Message.getClientHelloTls1CipherSuites().size(); i++) {
			text1 += Message.getClientHelloTls1CipherSuites().get(i) + "\n"; //$NON-NLS-1$
		}
		for (int i = 0; i < Message.getClientHelloTls2CipherSuites().size(); i++) {
			text2 += Message.getClientHelloTls2CipherSuites().get(i) + "\n"; //$NON-NLS-1$
		}
		if (infoText) {
			sslView.setStxInformationText(Messages.ServerHelloInformationText);
		}
		else if(txtRandom.getText().equals("")) //$NON-NLS-1$
		{
			String infoText=""; //$NON-NLS-1$
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
			infoText+="\n"+Messages.stxInformationRandomValue+Message.getClientHelloRandom()+"\n\n"+Messages.stxInformationCipherSuitesExchanged; //$NON-NLS-1$ //$NON-NLS-2$
			sslView.setStxInformationText(infoText);
		}
		else 
		{
			String infoText=""; //$NON-NLS-1$
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
			infoText+="\n"+Messages.stxInformationRandomValue+Message.getClientHelloRandom() //$NON-NLS-1$
					+"\n\n"+Messages.stxInformationCipherSuitesExchanged //$NON-NLS-1$
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
			sb.append(String.format("%02x", b & 0xff)); //$NON-NLS-1$
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see protocol.ProtocolStep#enableControls()
	 */
	@Override
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
	@Override
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
	@Override
	public String checkParameters() {
		String length = "00004c"; //$NON-NLS-1$
		String type ="02"; //$NON-NLS-1$
		String sessionIDLength=""; //$NON-NLS-1$
		String sessionID=""; //$NON-NLS-1$
		String cipherSuite=""; //$NON-NLS-1$
		
		if (txtSessionID.getText().equals("")) { //$NON-NLS-1$
			txtSessionID.setText("1"); //$NON-NLS-1$
		}
		if (txtRandom.getText().equals("")) { //$NON-NLS-1$
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
			return Messages.ServerHelloCompositeErrorRandom;
		} catch (IllegalArgumentException exc) {
			return Messages.ServerHelloCompositeErrorRandomShort;
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
			return Messages.ServerHelloCompositeErrorSessionIDNull;
		}
		catch (NumberFormatException exc) 
		{
			return Messages.ServerHelloCompositeErrorSessionID;
		}
		catch (IllegalArgumentException exc) {
			return Messages.ServerHelloCompositeErrorSessionIDLength;
		}
		String ciphersuiteCheck = checkCipherSuite();
		if(! ciphersuiteCheck.equals(ProtocolStep.OK)) {
			jumpToClientHello();
			return ciphersuiteCheck;
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
			sessionID="0"+txtSessionID.getText(); //$NON-NLS-1$
			sessionIDLength=Integer.toString((txtSessionID.getText().length()+1)/2);
		}
		
		Message.setMessageServerHello(type+length+Message.getServerHelloVersion()+txtRandom.getText()+sessionIDLength+sessionID+cipherSuite);
		
		Attacks attack = new Attacks();
		return attack.getDecision() ? ProtocolStep.OK : ""; //$NON-NLS-1$
	}

	private String checkCipherSuite() {
		String item = cmbCipherSuite.getItem(cmbCipherSuite
				.getSelectionIndex());
		if (cmbVersion.getSelectionIndex() == 0
				&& Message.getClientHelloTls0CipherSuites().contains(
						item) == false) {
			List<String> available = Message.getClientHelloTls0CipherSuites();
			if(available.isEmpty()) {
				available = new LinkedList<>();
				available.add(Messages.ServerHelloComposite_0);
			}
			return String.format(Messages.ServerHelloComposite_1, item, "TLS 1.0", available.stream().map(s -> "- "+s).collect(Collectors.joining("\n"))); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
// 			return String.format("%s is not a %s cipher suite; the protocol has been reset.", item, "TLS 0");
		} else if (cmbVersion.getSelectionIndex() == 1
				&& Message.getClientHelloTls1CipherSuites().contains(
						item) == false) {
			List<String> available = Message.getClientHelloTls1CipherSuites();
			if(available.isEmpty()) {
				available = new LinkedList<>();
				available.add(Messages.ServerHelloComposite_0);
			}
			return String.format(Messages.ServerHelloComposite_1, item, "TLS 1.1", available.stream().map(s -> "- "+s).collect(Collectors.joining("\n"))); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
// 			return String.format("%s is not a %s cipher suite; the protocol has been reset.", item, "TLS 1");
		} else if (cmbVersion.getSelectionIndex() == 2
				&& Message.getClientHelloTls2CipherSuites().contains(
						item) == false) {
			List<String> available = Message.getClientHelloTls2CipherSuites();
			if(available.isEmpty()) {
				available = new LinkedList<>();
				available.add(Messages.ServerHelloComposite_0);
			}
			return String.format(Messages.ServerHelloComposite_1, item, "TLS 1.2", available.stream().map(s -> "- "+s).collect(Collectors.joining("\n"))); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
// 			return String.format("%s is not a %s cipher suite; the protocol has been reset.", item, "TLS 2");
		}
		return ProtocolStep.OK;
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
			Message.setServerHelloVersion("0301"); //$NON-NLS-1$
		}else if(cmbVersion.getSelectionIndex()==1){
			Message.setServerHelloVersion("0302"); //$NON-NLS-1$
		}else{
			Message.setServerHelloVersion("0303"); //$NON-NLS-1$
		}
		
		Message.setServerHelloSessionID(txtSessionID.getText());
		Message.setServerHelloRandom(txtRandom.getText());
		if (cmbCipherSuite.getItem(selectedItem).contains("SHA256")) { //$NON-NLS-1$
			Message.setServerHelloHash("SHA256"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("SHA384")) { //$NON-NLS-1$
			Message.setServerHelloHash("SHA384"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("SHA")) { //$NON-NLS-1$
			Message.setServerHelloHash("SHA1"); //$NON-NLS-1$
		} else {
			Message.setServerHelloHash("MD5"); //$NON-NLS-1$
		}
		if (cmbCipherSuite.getItem(selectedItem).contains("DHE_RSA")) { //$NON-NLS-1$
			Message.setServerHelloKeyExchange("DHE_RSA"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DHE_DSS")) { //$NON-NLS-1$
			Message.setServerHelloKeyExchange("DHE_DSS"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DH_RSA")) { //$NON-NLS-1$
			Message.setServerHelloKeyExchange("DH_RSA"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DH_DSS")) { //$NON-NLS-1$
			Message.setServerHelloKeyExchange("DH_DSS"); //$NON-NLS-1$
		} else {
			Message.setServerHelloKeyExchange("RSA"); //$NON-NLS-1$
		}
		if (cmbCipherSuite.getItem(selectedItem).contains("AES_256")) { //$NON-NLS-1$
			Message.setServerHelloCipher("AES_256"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("AES_128")) { //$NON-NLS-1$
			Message.setServerHelloCipher("AES_128"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("3DES")) { //$NON-NLS-1$
			Message.setServerHelloCipher("3DES"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("DES")) { //$NON-NLS-1$
			Message.setServerHelloCipher("DES"); //$NON-NLS-1$
		} else if (cmbCipherSuite.getItem(selectedItem).contains("RC4_128")) { //$NON-NLS-1$
			Message.setServerHelloCipher("RC4_128"); //$NON-NLS-1$
		} else {
			Message.setServerHelloCipher("NULL"); //$NON-NLS-1$
		}
		if (cmbCipherSuite.getItem(selectedItem).contains("GCM")) { //$NON-NLS-1$
			Message.setServerHelloCipherMode("GCM"); //$NON-NLS-1$
		} else {
			Message.setServerHelloCipherMode("CBC"); //$NON-NLS-1$
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

		tls0Hex.add("0001"); //$NON-NLS-1$
		tls0Hex.add("0002"); //$NON-NLS-1$
		tls0Hex.add("0004"); //$NON-NLS-1$
		tls0Hex.add("0005"); //$NON-NLS-1$
		tls0Hex.add("000A"); //$NON-NLS-1$
		tls0Hex.add("0009"); //$NON-NLS-1$
		tls0Hex.add("000D"); //$NON-NLS-1$
		tls0Hex.add("000C"); //$NON-NLS-1$
		tls0Hex.add("0010"); //$NON-NLS-1$
		tls0Hex.add("000F"); //$NON-NLS-1$
		tls0Hex.add("0013"); //$NON-NLS-1$
		tls0Hex.add("0012"); //$NON-NLS-1$
		tls0Hex.add("0016"); //$NON-NLS-1$
		tls0Hex.add("0015"); //$NON-NLS-1$
		
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

		tls1Hex.add("0001"); //$NON-NLS-1$
		tls1Hex.add("0002"); //$NON-NLS-1$
		tls1Hex.add("0004"); //$NON-NLS-1$
		tls1Hex.add("0005"); //$NON-NLS-1$
		tls1Hex.add("000A"); //$NON-NLS-1$
		tls1Hex.add("0009"); //$NON-NLS-1$
		tls1Hex.add("000D"); //$NON-NLS-1$
		tls1Hex.add("000C"); //$NON-NLS-1$
		tls1Hex.add("0010"); //$NON-NLS-1$
		tls1Hex.add("000F"); //$NON-NLS-1$
		tls1Hex.add("0013"); //$NON-NLS-1$
		tls1Hex.add("0012"); //$NON-NLS-1$
		tls1Hex.add("0016"); //$NON-NLS-1$
		tls1Hex.add("0015"); //$NON-NLS-1$

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

		tls1Hex.add("002F"); //$NON-NLS-1$
		tls1Hex.add("0030"); //$NON-NLS-1$
		tls1Hex.add("0031"); //$NON-NLS-1$
		tls1Hex.add("0032"); //$NON-NLS-1$
		tls1Hex.add("0033"); //$NON-NLS-1$
		tls1Hex.add("0035"); //$NON-NLS-1$
		tls1Hex.add("0036"); //$NON-NLS-1$
		tls1Hex.add("0037"); //$NON-NLS-1$
		tls1Hex.add("0038"); //$NON-NLS-1$
		tls1Hex.add("0039"); //$NON-NLS-1$
		
		tls2.add(Messages.TLS2_RSA_WITH_NULL_MD5);
		tls2.add(Messages.TLS2_RSA_WITH_NULL_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_NULL_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_RC4_128_MD5);
		
		tls2Hex.add("0001"); //$NON-NLS-1$
		tls2Hex.add("0002"); //$NON-NLS-1$
		tls2Hex.add("003B"); //$NON-NLS-1$
		tls2Hex.add("0004"); //$NON-NLS-1$

		tls2.add(Messages.TLS2_RSA_WITH_RC4_128_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_CBC_SHA);
		
		tls1Hex.add("0005"); //$NON-NLS-1$
		tls1Hex.add("000A"); //$NON-NLS-1$
		tls2Hex.add("002F"); //$NON-NLS-1$
		tls2Hex.add("0035"); //$NON-NLS-1$

		tls2.add(Messages.TLS2_RSA_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_RSA_WITH_AES_256_CBC_SHA256);
		
		tls2Hex.add("003C"); //$NON-NLS-1$
		tls2Hex.add("003D"); //$NON-NLS-1$

		tls2.add(Messages.TLS2_DH_DSS_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_3DES_EDE_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_3DES_EDE_CBC_SHA);

		tls1Hex.add("000D"); //$NON-NLS-1$
		tls1Hex.add("0010"); //$NON-NLS-1$
		tls2Hex.add("0013"); //$NON-NLS-1$
		tls2Hex.add("0016"); //$NON-NLS-1$
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_CBC_SHA);

		tls2Hex.add("0030"); //$NON-NLS-1$
		tls2Hex.add("0031"); //$NON-NLS-1$
		tls2Hex.add("0032"); //$NON-NLS-1$
		tls2Hex.add("0033"); //$NON-NLS-1$
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_CBC_SHA);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_CBC_SHA);

		tls2Hex.add("0036"); //$NON-NLS-1$
		tls2Hex.add("0037"); //$NON-NLS-1$
		tls2Hex.add("0038"); //$NON-NLS-1$
		tls2Hex.add("0039"); //$NON-NLS-1$
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_128_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_128_CBC_SHA256);

		tls2Hex.add("003E"); //$NON-NLS-1$
		tls2Hex.add("003F"); //$NON-NLS-1$
		tls2Hex.add("0040"); //$NON-NLS-1$
		tls2Hex.add("0067"); //$NON-NLS-1$
		
		tls2.add(Messages.TLS2_DH_DSS_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DH_RSA_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_DSS_WITH_AES_256_CBC_SHA256);
		tls2.add(Messages.TLS2_DHE_RSA_WITH_AES_256_CBC_SHA256);
		
		tls2Hex.add("0068"); //$NON-NLS-1$
		tls2Hex.add("0069"); //$NON-NLS-1$
		tls2Hex.add("006A"); //$NON-NLS-1$
		tls2Hex.add("006B"); //$NON-NLS-1$
		
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
		
		tls2Hex.add("009C"); //$NON-NLS-1$
		tls2Hex.add("009D"); //$NON-NLS-1$
		tls2Hex.add("009E"); //$NON-NLS-1$
		tls2Hex.add("009F"); //$NON-NLS-1$
		tls2Hex.add("00A0"); //$NON-NLS-1$
		tls2Hex.add("00A1"); //$NON-NLS-1$
		tls2Hex.add("00A2"); //$NON-NLS-1$
		tls2Hex.add("00A3"); //$NON-NLS-1$
		tls2Hex.add("00A4"); //$NON-NLS-1$
		tls2Hex.add("00A5"); //$NON-NLS-1$
		
	}

	/* (non-Javadoc)
	 * @see org.jcryptool.visual.ssl.protocol.ProtocolStep#resetStep()
	 */
	@Override
	public void resetStep() {
		txtRandom.setText(""); //$NON-NLS-1$
		txtSessionID.setText(""); //$NON-NLS-1$
		cmbCipherSuite.select(33);
		cmbVersion.select(2);
		infoText=false;
		btnInfo.setText(Messages.ClientCertificateCompositeBtnInfo);
	}
}
