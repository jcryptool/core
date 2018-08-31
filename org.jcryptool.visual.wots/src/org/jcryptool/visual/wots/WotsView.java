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
package org.jcryptool.visual.wots;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.wots.files.Converter;
import org.jcryptool.visual.wots.files.ResizeListener;

import wots.WOTSPlugin;

public class WotsView extends ViewPart {

	public static final String ID = "org.jcryptool.visual.wots.WOTSView2"; //$NON-NLS-1$
	private Button btn_Genkey;
	private Button btn_Sign;
	private Button btn_VerifySig;
	private Button btn_Details;
	private Label img_right;
	private Text txt_Sig;
	private Label txt_SignatureSize;
	private Label lblMessage;
	private Text txt_message;
	private Text txt_Hash;
	private Label txt_MessageSize;
	private Label txt_HashSize;
	private Button btnLoadMessageFrom;
	private Label lblWinternitzParameterw;
	private Label lblHashFunction;
	private Text txt_winternitzP;
	private Button btnWots;
	private Button btnWotsPlus;
	private Label lblSignatureKey;
	private Text txt_Sigkey;
	private Text txt_Verifkey;
	private Label lblVerificationKey;
	private Label txt_SigKeySize;
	private Label txt_VerKeySize;
	private Label lblSignature;
	private Text txt_Bi;
	private Label lblBi;
	private Label txt_BSize;
	private Text txt_Output;
	private Combo cmb_Hash;
	private Label lblMessageHash;

	// Grid Data
	private GridData gd_txt_message;
	private GridData gd_txt_Hash;
	private GridData gd_btn_Details;
	private GridData gd_txt_Sig;
	private GridData gd_txt_Bi;
	private GridData gd_txt_Verifkey;
	private GridData gd_txt_Sigkey;

	// Parameter for WOTS/WOTS+
	private String hashFunction = "SHA-256";
	private OTS instance = new WinternitzOTS(4, hashFunction);
	private String privateKey = "";
	private String publicKey = "";
	private String signature = "";
	private int w = 4;
	private int n = instance.getN();
	private int l = instance.getL();
	private String message = Descriptions.defaultMessage_txt;
	private String messageHash = Converter._byteToHex(instance.hashMessage(message));
	private String b = Converter._byteToHex(instance.initB());
	private boolean details = false;
	private boolean disable = true;
	private int ctr;
	private Text[] txtToEnableOrDisable;
	private Button[] btnToEnableOrDisable;
	
	private static String language = getLanguage();
	public static String currentImg = "icons/" + language + "/Overview2.PNG";

	private ScrolledComposite scrolledContainer;
	private Composite container;
	private Composite composite;
	private Text txtTheWinternitzonetimesignatureIs;
	private Text txtWinternitzOtsignaturewots;
	private Composite header;


	/**
	 * Create contents of the view part. The layout manager has no option to define
	 * a maximum size for text boxes, buttons, etc.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {

		// set context help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, WOTSPlugin.PLUGIN_ID + ".kontextHilfe"); //$NON-NLS-1$
		
		scrolledContainer = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledContainer.setExpandHorizontal(true);
		scrolledContainer.setExpandVertical(true);

		container = new Composite(scrolledContainer, SWT.NONE);
		container.setLayout(new GridLayout(6, true));
		
		header = new Composite(container, SWT.None);
		header.setLayout(new GridLayout());
		header.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 6, 1));
		header.setBackground(ColorService.WHITE);

		txtWinternitzOtsignaturewots = new Text(header, SWT.NONE);
		txtWinternitzOtsignaturewots.setFont(FontService.getHeaderFont());
		txtWinternitzOtsignaturewots.setBackground(ColorService.WHITE);
		txtWinternitzOtsignaturewots.setText(Descriptions.headline_txt);
		txtWinternitzOtsignaturewots.setEditable(false);

		txtTheWinternitzonetimesignatureIs = new Text(header, SWT.NONE);
		txtTheWinternitzonetimesignatureIs.setBackground(ColorService.WHITE);
		txtTheWinternitzonetimesignatureIs.setText(Descriptions.header_txt);
		txtTheWinternitzonetimesignatureIs.setEditable(false);

		lblMessage = new Label(container, SWT.NONE);
		lblMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		lblMessage.setText(Descriptions.message_txt);

		lblMessageHash = new Label(container, SWT.NONE);
		lblMessageHash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1));
		lblMessageHash.setText(Descriptions.hash_txt);

		txt_message = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		gd_txt_message = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txt_message.minimumHeight = 100;
		gd_txt_message.heightHint = 100;
		txt_message.setLayoutData(gd_txt_message);
		txt_message.setText(Descriptions.defaultMessage_txt);
		txt_message.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				clearOutput(false);

				disable = false;

				// Changes hash and Bitstring bi if message is modified
				message = txt_message.getText();
				messageHash = Converter._byteToHex(instance.hashMessage(message));
				b = Converter._byteToHex(instance.initB());
				txt_Hash.setText(messageHash);
				txt_Bi.setText(b);

				updateLengths();
				disable = true;
			}
		});

		txt_Hash = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		gd_txt_Hash = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txt_Hash.minimumHeight = 100;
		gd_txt_Hash.heightHint = 100;
		txt_Hash.setLayoutData(gd_txt_Hash);
		txt_Hash.setText(messageHash);
		txt_Hash.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				clearOutput(true);

				// Changes Hash and Bitstring bi if modified

				ctr++;

				if (checkHex(txt_Hash.getText())) {
					txt_Hash.setBackground(ColorService.RED);
					setDisabled(txt_Hash);
					txt_HashSize.setText(Descriptions.invalidChar_txt);
				} else {

					txt_Hash.setBackground(ColorService.WHITE);

					if (ctr % 2 != 0 && disable) {
						setDisabled(txt_Hash);
						txt_HashSize.setBackground(ColorService.RED);
					} else {
						messageHash = txt_Hash.getText();
						txt_HashSize.setText(Integer.toString(
								Converter._stringToByte(messageHash).length / 2) + "/"
								+ n + " Bytes");

						if (Converter._stringToByte(messageHash).length / 2 == n) {

							instance.setMessage(
									Converter._hexStringToByte(messageHash));
							b = Converter._byteToHex(instance.initB());
							ctr = 1;
							txt_Bi.setText(b);
							txt_HashSize.setBackground(ColorService.LIGHTGRAY);
							clearOutput(false);
							setEnabled();
						}
					}
				}
			}
		});
		
		txt_Output = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_txt_Output = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 5);
		gd_txt_Output.widthHint = 400;
		gd_txt_Output.heightHint = 300;
		txt_Output.setLayoutData(gd_txt_Output);
		txt_Output.setText(Descriptions.outWelcome_txt);

		txt_MessageSize = new Label(container, SWT.TOP);
		txt_MessageSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		btnLoadMessageFrom = new Button(container, SWT.NONE);
		btnLoadMessageFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		btnLoadMessageFrom.setText(Descriptions.loadMessage_txt);
		btnLoadMessageFrom.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
				fd.setFilterExtensions(new String[] { "*.txt" }); //$NON-NLS-1$
				fd.setFilterNames(new String[] { Descriptions.fileType_txt });
				String filePath = fd.open();

				if (filePath != null) {
					File file = new File(filePath);
					try {
						Scanner scanner = new Scanner(file, "ISO-8859-1"); //$NON-NLS-1$
						String fileString = scanner.useDelimiter("\\Z").next(); //$NON-NLS-1$
						scanner.close();

						txt_message.setText(fileString);

					} catch (FileNotFoundException ex) {
						LogUtil.logError(ex);
					}
				}
				reset();
			}
		});

		txt_HashSize = new Label(container, SWT.RIGHT);
		txt_HashSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));

		lblWinternitzParameterw = new Label(container, SWT.NONE);
		GridData gd_lblWinternitzParameterw = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gd_lblWinternitzParameterw.verticalIndent = 15;
		lblWinternitzParameterw.setLayoutData(gd_lblWinternitzParameterw);
		lblWinternitzParameterw.setText(Descriptions.winPara_txt);

		txt_winternitzP = new Text(container, SWT.BORDER);
		GridData gd_txt_winternitzP = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd_txt_winternitzP.verticalIndent = 15;
		txt_winternitzP.setLayoutData(gd_txt_winternitzP);
		txt_winternitzP.setText("4");
		txt_winternitzP.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {

				clearOutput(false);

				if (txt_winternitzP.getText().equals("")) {
					setDisabled(txt_winternitzP);
					txt_Hash.setBackground(ColorService.LIGHTGRAY);
					txt_Sigkey.setBackground(ColorService.LIGHTGRAY);
					txt_Verifkey.setBackground(ColorService.LIGHTGRAY);
					txt_Sig.setBackground(ColorService.LIGHTGRAY);
					txt_Bi.setBackground(ColorService.LIGHTGRAY);
				} else {
					// Changes Winternitz Parameter if modified
					w = Integer.parseInt(txt_winternitzP.getText());
					privateKey = "";
					publicKey = "";
					signature = "";
					setOutputs();
					instance.initB();
					getOutputs();
					updateLengths();
					setEnabled();
					btn_Sign.setEnabled(false);
					btn_VerifySig.setEnabled(false);

					txt_HashSize.setBackground(ColorService.LIGHTGRAY);
					txt_BSize.setBackground(ColorService.LIGHTGRAY);
					txt_SigKeySize.setBackground(ColorService.LIGHTGRAY);
					txt_Sig.setBackground(ColorService.WHITE);
				}
			}
		});

		btnWots = new Button(container, SWT.RADIO);
		GridData gd_btnWots = new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1);
		gd_btnWots.verticalIndent = 15;
		btnWots.setLayoutData(gd_btnWots);
		btnWots.setText("WOTS");
		btnWots.setSelection(true);
		btnWots.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				clearOutput(false);

				disable = false;

				// Changes type to WOTS and resets what is necessary to do
				// so
				instance = new WinternitzOTS(w, hashFunction);
				privateKey = "";
				publicKey = "";
				signature = "";
				txt_Sigkey.setText("");
				txt_Verifkey.setText("");
				txt_Sig.setText("");
				setRightImage("icons/" + language + "/Overview2.PNG");
				
				txt_Output.setText(Descriptions.outWelcome_txt);

				updateLengths();
				disable = true;

				btn_Sign.setEnabled(false);
				btn_VerifySig.setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		lblHashFunction = new Label(container, SWT.NONE);
		lblHashFunction.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblHashFunction.setText(Descriptions.hashFunction_txt);

		cmb_Hash = new Combo(container, SWT.READ_ONLY);
		cmb_Hash.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		cmb_Hash.add("SHA-256");
		cmb_Hash.add("SHA-1");
		cmb_Hash.add("MD5");
		cmb_Hash.select(0);
		cmb_Hash.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				messageHash = Converter._byteToHex(instance.hashMessage(message));
				b = Converter._byteToHex(instance.initB());

				txt_Hash.setText(messageHash);
				txt_Bi.setText(b);

				int index = cmb_Hash.getSelectionIndex();

				switch (index) {
				case 0:
					hashFunction = "SHA-256";
					break;
				case 1:
					hashFunction = "SHA-1";
					break;
				case 2:
					hashFunction = "MD5";
					break;
				default:
					hashFunction = "SHA-256";
					cmb_Hash.select(0);
				}

				privateKey = "";
				publicKey = "";
				signature = "";

				setOutputs();
				getOutputs();

				txt_message.setText(message);

				updateLengths();
				setEnabled();

				btn_Sign.setEnabled(false);
				btn_VerifySig.setEnabled(false);

				txt_Sigkey.setBackground(ColorService.WHITE);
				txt_SigKeySize.setBackground(ColorService.LIGHTGRAY);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

		btnWotsPlus = new Button(container, SWT.RADIO);
		btnWotsPlus.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		btnWotsPlus.setText("WOTS+");
		btnWotsPlus.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				clearOutput(false);

				disable = false;

				// Changes type to WOTS+ and resets what is necessary to do so
				instance = new WOTSPlus(w, hashFunction);
				privateKey = "";
				publicKey = "";
				signature = "";
				txt_Sigkey.setText("");
				txt_Verifkey.setText("");
				txt_Sig.setText("");
				setRightImage("icons/" + language + "/Overview2.PNG");
				txt_Output.setText(Descriptions.outWelcome_txt);

				updateLengths();

				disable = true;

				btn_Sign.setEnabled(false);
				btn_VerifySig.setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		lblSignatureKey = new Label(container, SWT.NONE);
		GridData gd_lblSignatureKey = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lblSignatureKey.verticalIndent = 15;
		lblSignatureKey.setLayoutData(gd_lblSignatureKey);
		lblSignatureKey.setText(Descriptions.privateKey_txt);

		lblVerificationKey = new Label(container, SWT.NONE);
		GridData gd_lblVerificationKey = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lblVerificationKey.verticalIndent = 15;
		lblVerificationKey.setLayoutData(gd_lblVerificationKey);
		lblVerificationKey.setText(Descriptions.publicKey_txt);

		txt_Sigkey = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		gd_txt_Sigkey = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txt_Sigkey.minimumHeight = 100;
		gd_txt_Sigkey.heightHint = 100;
		txt_Sigkey.setLayoutData(gd_txt_Sigkey);
		txt_Sigkey.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {

				clearOutput(true);
				updateLengths();

				// Changes Private Key if modified
				ctr++;

				if (checkHex(txt_Sigkey.getText())) {
					txt_Sigkey.setBackground(ColorService.RED);
					setDisabled(txt_Sigkey);
					txt_SigKeySize.setText(Descriptions.invalidChar_txt);
				} else {

					txt_Sigkey.setBackground(ColorService.WHITE);
					if (ctr % 2 != 0 && disable) {
						setDisabled(txt_Sigkey);
						txt_SigKeySize.setBackground(ColorService.RED);
					} else {
						privateKey = txt_Sigkey.getText();
						txt_SigKeySize.setText(Integer.toString(
								Converter._stringToByte(privateKey).length / 2) + "/"
								+ (n * l) + " " + Descriptions.byte_txt);

						if (Converter._stringToByte(privateKey).length / 2 == n * l) {
							clearOutput(false);
							setEnabled();
							txt_SigKeySize.setBackground(ColorService.LIGHTGRAY);
						}
					}
				}
			}
		});

		txt_Verifkey = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		gd_txt_Verifkey = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txt_Verifkey.minimumHeight = 100;
		gd_txt_Verifkey.heightHint = 100;
		txt_Verifkey.setLayoutData(gd_txt_Verifkey);
		txt_Verifkey.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {

				clearOutput(true);

				// Changes Public Key if modified
				ctr++;

				if (checkHex(txt_Verifkey.getText())) {
					txt_Verifkey.setBackground(ColorService.RED);
					setDisabled(txt_Verifkey);
					txt_VerKeySize.setText(Descriptions.invalidChar_txt);
				} else {
					txt_Verifkey.setBackground(ColorService.WHITE);
					if (ctr % 2 != 0 && disable) {
						setDisabled(txt_Verifkey);
						txt_VerKeySize.setBackground(ColorService.RED);
					} else {
						publicKey = txt_Verifkey.getText();
						txt_VerKeySize.setText(Integer
								.toString(Converter._stringToByte(publicKey).length / 2)
								+ "/" + (n * instance.getPublicKeyLength()) + " Bytes");

						if (Converter._stringToByte(publicKey).length
								/ 2 == (n * instance.getPublicKeyLength())) {
							clearOutput(false);
							setEnabled();
							txt_VerKeySize.setBackground(ColorService.LIGHTGRAY);
						}
					}
				}
			}
		});

		composite = new Composite(container, SWT.NONE);
		composite.setBackground(ColorService.WHITE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 6));

		img_right = new Label(composite, SWT.CENTER);
		img_right.setBackground(ColorService.WHITE);
		GridData gd_img_right = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_img_right.widthHint = 50;
		gd_img_right.heightHint = 50;
		img_right.setLayoutData(gd_img_right);
		img_right.setImage(WOTSPlugin.getImageDescriptor("icons/" + language + "/Overview2.PNG").createImage());
		img_right.addControlListener(new ResizeListener(img_right, composite));

		txt_SigKeySize = new Label(container, SWT.NONE);
		txt_SigKeySize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));

		txt_VerKeySize = new Label(container, SWT.RIGHT);
		txt_VerKeySize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));

		lblSignature = new Label(container, SWT.NONE);
		GridData gd_lblSignature = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lblSignature.verticalIndent = 15;
		lblSignature.setLayoutData(gd_lblSignature);
		lblSignature.setText(Descriptions.signature_txt);

		lblBi = new Label(container, SWT.NONE);
		GridData gd_lblBi = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1);
		gd_lblBi.verticalIndent = 15;
		lblBi.setLayoutData(gd_lblBi);
		lblBi.setText("b_i");

		txt_Sig = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		gd_txt_Sig = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txt_Sig.minimumHeight = 100;
		gd_txt_Sig.heightHint = 100;
		txt_Sig.setLayoutData(gd_txt_Sig);
		txt_Sig.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {

				clearOutput(false);

				// Changes signature if modified
				ctr++;

				if (checkHex(txt_Sig.getText())) {
					txt_Sig.setBackground(ColorService.RED);
					setDisabled(txt_Sig);
					txt_SignatureSize.setText(Descriptions.invalidChar_txt);
				} else {
					txt_Sig.setBackground(ColorService.WHITE);
					if (ctr % 2 != 0 && disable) {
						setDisabled(txt_Sig);
						txt_SignatureSize.setBackground(ColorService.RED);
					} else {
						signature = txt_Sig.getText();
						txt_SignatureSize.setText(Integer
								.toString(Converter._stringToByte(signature).length / 2)
								+ "/" + (n * l) + " Bytes");

						if (Converter._stringToByte(signature).length / 2 == n * l) {
							setEnabled();
							txt_SignatureSize.setBackground(ColorService.LIGHTGRAY);
						}
					}
				}
			}
		});

		txt_Bi = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		gd_txt_Bi = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_txt_Bi.minimumHeight = 100;
		gd_txt_Bi.heightHint = 100;
		txt_Bi.setLayoutData(gd_txt_Bi);
		txt_Bi.setText(b);
		txt_Bi.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {

				clearOutput(true);

				// Changes Bitstring bi if modified
				ctr++;

				if (checkHex(txt_Bi.getText())) {
					txt_Bi.setBackground(ColorService.RED);
					setDisabled(txt_Bi);
					txt_BSize.setText(Descriptions.invalidChar_txt);
				} else {

					txt_Bi.setBackground(ColorService.WHITE);
					if (ctr % 2 != 0 && disable) {
						setDisabled(txt_Bi);
						txt_BSize.setBackground(ColorService.RED);
					} else {
						b = txt_Bi.getText();
						instance.setBi(Converter._hexStringToByte(b));
						txt_BSize.setText(
								Integer.toString(Converter._stringToByte(b).length / 2)
										+ "/" + l + " Bytes");

						if (Converter._stringToByte(b).length / 2 == l) {
							clearOutput(false);
							setEnabled();
							txt_BSize.setBackground(ColorService.LIGHTGRAY);
						}
					}
				}
			}
		});

		txt_SignatureSize = new Label(container, SWT.LEFT);
		txt_SignatureSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		
		txt_BSize = new Label(container, SWT.RIGHT);
		txt_BSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));

		btn_Genkey = new Button(container, SWT.NONE);
		GridData gd_btn_Genkey = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gd_btn_Genkey.verticalIndent = 15;
		btn_Genkey.setLayoutData(gd_btn_Genkey);
		btn_Genkey.setText(Descriptions.btnGenKeys_txt);
		btn_Genkey.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				clearOutput(false);

				// KEY GENERATION

				disable = false;

				if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {

					// Set Image & Output field for WOTS
					txt_Output.setText(Descriptions.outGenKeys_txt);
					setRightImage("icons/" + language + "/Key_Generation.PNG");

				} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {

					// Set Image & Output field for WOTS+
					txt_Output.setText(Descriptions.outGenKeysPlus_txt);
					setRightImage("icons/" + language + "/Key_Generation.PNG");

				} else {

					txt_Output.setText(Descriptions.error_txt);

				}

				// Generate Keys
				setOutputs();
				instance.generateKeyPair();
				getOutputs();

				disable = true;

				btn_VerifySig.setEnabled(false);
			}
		});

		btn_Sign = new Button(container, SWT.NONE);
		GridData gd_btn_Sign = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gd_btn_Sign.verticalIndent = 15;
		btn_Sign.setLayoutData(gd_btn_Sign);
		btn_Sign.setText(Descriptions.btnGenSig_txt);
		btn_Sign.setEnabled(false);
		btn_Sign.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				clearOutput(false);

				// SIGNATURE GENERATION

				disable = false;

				if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {

					// Set Image & Output field for WOTS
					txt_Output.setText(Descriptions.outGenSig_txt);
					setRightImage("icons/" + language + "/Signature_Generation.PNG");

				} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {

					// Set Image & Output field for WOTS+
					txt_Output.setText(Descriptions.outGenSigPlus_txt);
					setRightImage("icons/" + language + "/Signature_Generation.PNG");
				} else {
					btnWots.setSelection(true);
					btnWotsPlus.setSelection(false);
				}

				// Sign message and put Signature in Output Field
				setOutputs();
				instance.sign();
				getOutputs();

				disable = true;

			}
		});

		btn_VerifySig = new Button(container, SWT.NONE);
		GridData gd_btn_VerifySig = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gd_btn_VerifySig.verticalIndent = 15;
		btn_VerifySig.setLayoutData(gd_btn_VerifySig);
		btn_VerifySig.setText(Descriptions.btnVerSig_txt);
		btn_VerifySig.setEnabled(false);
		btn_VerifySig.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				// SIGNATURE VERIFICATION

				disable = false;

				if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {

					// Set Image & Output field for WOTS
					txt_Output.setText(Descriptions.outVerSig_txt);
					setRightImage("icons/" + language + "/Signature_Verification.PNG");

				} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {

					// Set Image & Output field for WOTS+
					txt_Output.setText(Descriptions.outVerSigPlus_txt);
					setRightImage("icons/" + language + "/Signature_Verification.PNG");

				} else {
					btnWots.setSelection(true);
					btnWotsPlus.setSelection(false);
				}

				// Verify Signature
				setOutputs();
				if (instance.verify()) {
					txt_Sig.setBackground(ColorService.GREEN);
				} else {
					txt_Sig.setBackground(ColorService.RED);
				}
				disable = true;
			}
		});

		btn_Details = new Button(container, SWT.NONE);
		gd_btn_Details = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gd_btn_Details.verticalIndent = 15;
		btn_Details.setLayoutData(gd_btn_Details);
		btn_Details.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!details) {

					details = true;
					enableDetails();

				} else if (details) {

					details = false;
					disableDetails();

				} else {
					txt_Output.setText(Descriptions.error_txt);
				}
				container.layout();
				txt_HashSize.setBackground(ColorService.LIGHTGRAY);
			}
		});

		// Finisch Initialization
		txtToEnableOrDisable = new Text[] { txt_message, txt_Sigkey, txt_Verifkey, txt_Hash, txt_Sig, txt_Bi,
				txt_winternitzP };
		btnToEnableOrDisable = new Button[] { btnWots, btnWotsPlus, btn_Genkey, btn_VerifySig, btn_Sign,
				btnLoadMessageFrom };
		disableDetails();
		updateLengths();

		scrolledContainer.setContent(container);
		scrolledContainer.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	public void setFocus() {
		container.setFocus();
	}
	
	/**
	 * Get the language the user uses.
	 * This is important for the img_right pictures. Thea are available in de and en.
	 * I don't know how the language ist specified correctly.
	 */
	private static String getLanguage() {
		if (System.getProperty("osgi.nl.user").equals("de_DE") || System.getProperty("osgi.nl.user").equals("de")) {
			return "de";
		} else {
			return "en";
		}
	}
	
	/**
	 * Sets the image in the bottom right corner
	 * @param path
	 */
	private void setRightImage(String path) {
		currentImg = path;
		Image tmp = new Image(img_right.getDisplay(), WOTSPlugin.getImageDescriptor(path)
				.createImage().getImageData()
				.scaledTo(img_right.getImage().getBounds().width, img_right.getImage().getBounds().height));
		img_right.setImage(tmp);
	}

	/**
	 * Sets the Variables of the WOTS/WOTS+ instance to the one defined in this
	 * class
	 */
	private void setOutputs() {

		instance.setW(w);
		instance.setMessageDigest(hashFunction);
		instance.setPrivateKey(Converter._hexStringTo2dByte(privateKey, instance.getLength()));
		instance.setPublicKey(Converter._hexStringTo2dByte(publicKey, instance.getPublicKeyLength()));
		instance.setSignature(Converter._hexStringToByte(signature));
		instance.setMessage(Converter._hexStringToByte(messageHash));
		instance.setBi(Converter._hexStringToByte(b));
	}

	/**
	 * Get the calculated Values from the WOTS/WOTS+ instance and set global
	 * variables of this class Sets txt-fields to the values got from WOTS/WOTS+
	 * instance
	 */
	private void getOutputs() {
		privateKey = Converter._2dByteToHex(instance.getPrivateKey());
		publicKey = Converter._2dByteToHex(instance.getPublicKey());
		signature = Converter._byteToHex(instance.getSignature());
		messageHash = Converter._byteToHex(instance.getMessageHash());
		b = Converter._byteToHex(instance.getBi());
		n = instance.getN();
		l = instance.getL();

		txt_Sigkey.setText(privateKey);
		txt_Verifkey.setText(publicKey);
		txt_Bi.setText(b);
		txt_Sig.setText(signature);
		txt_Hash.setText(messageHash);

		updateLengths();
	}

	/**
	 * Method that disables all txt-fields and buttons that can be activated/edited
	 * except for the @param exception
	 */
	private void setDisabled(Text exception) {

		// Disables all Buttons and editable text-fields except for the given
		// exception
		for (int i = 0; i < txtToEnableOrDisable.length; i++) {
			if (!txtToEnableOrDisable[i].equals(exception)) {
				txtToEnableOrDisable[i].setEnabled(false);
				txtToEnableOrDisable[i].setEditable(false);
				txtToEnableOrDisable[i].setBackground(ColorService.LIGHTGRAY);
			}
		}
		for (int i = 0; i < btnToEnableOrDisable.length; i++) {
			btnToEnableOrDisable[i].setEnabled(false);
		}
		cmb_Hash.setEnabled(false);
	}

	/**
	 * Enables all Buttons and txt-fields that can be activated/edited
	 */
	private void setEnabled() {

		// Enables all Buttons and editable text-fields
		for (int i = 0; i < txtToEnableOrDisable.length; i++) {
			txtToEnableOrDisable[i].setEnabled(true);
			txtToEnableOrDisable[i].setEditable(true);
			txtToEnableOrDisable[i].setBackground(ColorService.WHITE);
		}
		for (int i = 0; i < btnToEnableOrDisable.length; i++) {
			btnToEnableOrDisable[i].setEnabled(true);
		}
		cmb_Hash.setEnabled(true);
		ctr = 0;
	}

	/**
	 * Clears Keys + Signature fields
	 */
	public void reset() {

		privateKey = "";
		publicKey = "";
		signature = "";
		messageHash = Converter._byteToHex(instance.hashMessage(message));
		b = Converter._byteToHex(instance.initB());

		txt_Sigkey.setText("");
		txt_Sig.setText("");
		txt_Verifkey.setText("");
		txt_Output.setText(Descriptions.outWelcome_txt);
		setRightImage("icons/" + language + "/Overview2.PNG");
		txt_Hash.setText(messageHash);
		txt_Bi.setText(b);

		updateLengths();
		setEnabled();

		btn_Sign.setEnabled(false);
		btn_VerifySig.setEnabled(false);

		clearOutput(false);

		txt_SigKeySize.setBackground(ColorService.LIGHTGRAY);
		txt_VerKeySize.setBackground(ColorService.LIGHTGRAY);
		txt_SignatureSize.setBackground(ColorService.LIGHTGRAY);
		txt_BSize.setBackground(ColorService.LIGHTGRAY);
		
		
		container.layout();
	}

	/**
	 * Updates Lengths of all txt_fields and set correct values to txt_lenght fields
	 */
	private void updateLengths() {

		txt_MessageSize
				.setText(Integer.toString(Converter._stringToByte(message).length) + " "
						+ Descriptions.byte_txt);
		txt_SigKeySize.setText(
				Integer.toString(Converter._stringToByte(privateKey).length / 2) + "/"
						+ (n * l) + " " + Descriptions.byte_txt);
		txt_VerKeySize
				.setText(Integer.toString(Converter._stringToByte(publicKey).length / 2)
						+ "/" + (n * instance.getPublicKeyLength()) + " " + Descriptions.byte_txt);
		txt_HashSize.setText(
				Integer.toString(Converter._hexStringToByte(messageHash).length) + "/"
						+ n + " " + Descriptions.byte_txt);
		txt_SignatureSize
				.setText(Integer.toString(Converter._stringToByte(signature).length / 2)
						+ "/" + (n * l) + " " + Descriptions.byte_txt);
		txt_BSize.setText(Integer.toString(Converter._hexStringToByte(b).length) + "/"
				+ l + " " + Descriptions.byte_txt);
	}

	/**
	 * Sets back verify button
	 */
	private void clearOutput(boolean changeColour) {
		if (changeColour) {
			txt_Sig.setBackground(ColorService.LIGHTGRAY);
		} else {
			txt_Sig.setBackground(ColorService.WHITE);
		}
		btn_VerifySig.setText(Descriptions.btnVerSig_txt);
	}

	/**
	 * Sets everything back to original state
	 */
	public void restart() {

		hashFunction = "SHA-256";
		instance = new WinternitzOTS(4, hashFunction);
		privateKey = "";
		publicKey = "";
		signature = "";
		w = 4;
		n = instance.getN();
		l = instance.getL();
		message = Descriptions.defaultMessage_txt;
		messageHash = Converter._byteToHex(instance.hashMessage(message));
		b = Converter._byteToHex(instance.initB());
		details = false;
		disable = true;

		// Set Attributes for Objects

		btn_Details.setText(Descriptions.showDetails_txt);

		btnWots.setSelection(true);
		btnWotsPlus.setSelection(false);

		lblMessageHash.setEnabled(false);
		lblMessageHash.setVisible(false);

		lblBi.setEnabled(false);
		lblBi.setVisible(false);

		txt_winternitzP.setText("4");
		txt_Sigkey.setText("");
		txt_message.setText(Descriptions.defaultMessage_txt);
		txt_Output.setText(Descriptions.outWelcome_txt);
		txt_Hash.setText(messageHash);
		txt_Bi.setText(b);
		disableDetails();

		setRightImage("icons/" + language + "/Overview2.PNG");
		cmb_Hash.select(0);

		txt_Sig.setText("");
		txt_Sigkey.setText("");
		txt_Verifkey.setText("");

		updateLengths();
		setEnabled();

		btn_Sign.setEnabled(false);
		btn_VerifySig.setEnabled(false);
		clearOutput(false);

		txt_SigKeySize.setBackground(ColorService.LIGHTGRAY);
		txt_VerKeySize.setBackground(ColorService.LIGHTGRAY);
		txt_SignatureSize.setBackground(ColorService.LIGHTGRAY);
		txt_BSize.setBackground(ColorService.LIGHTGRAY);

		container.layout();
	}

	/**
	 * Disables detailed view
	 */
	private void disableDetails() {

		btn_Details.setText(Descriptions.showDetails_txt);

		// make Message and Hash
		gd_txt_Hash.exclude = true;
		txt_Hash.setEnabled(false);
		txt_Hash.setVisible(false);
		gd_txt_message.horizontalSpan = 4;
		lblMessageHash.setVisible(false);
		lblMessageHash.setEnabled(false);
		txt_HashSize.setVisible(false);

		// make b_i and Signature
		gd_txt_Bi.exclude = true;
		txt_Bi.setEnabled(false);
		txt_Bi.setVisible(false);
		gd_txt_Sig.horizontalSpan = 4;
		lblBi.setVisible(false);
		lblBi.setEnabled(false);
		txt_BSize.setVisible(false);
	}

	/**
	 * Enables Detailed view
	 */
	private void enableDetails() {

		btn_Details.setText(Descriptions.hideDetails_txt);

		// make Message and Hash
		gd_txt_Hash.exclude = false;
		txt_Hash.setEnabled(true);
		txt_Hash.setVisible(true);
		gd_txt_message.horizontalSpan = 2;
		lblMessageHash.setVisible(true);
		lblMessageHash.setEnabled(true);
		txt_HashSize.setVisible(true);

		// make b_i and Signature
		gd_txt_Bi.exclude = false;
		txt_Bi.setEnabled(true);
		txt_Bi.setVisible(true);
		gd_txt_Sig.horizontalSpan = 2;
		lblBi.setVisible(true);
		lblBi.setEnabled(true);
		txt_BSize.setVisible(true);
	}

	/**
	 * Checks if a given String contains other chars than hex
	 * 
	 * @param testObject
	 * @return
	 */
	private boolean checkHex(String testObject) {

		char[] testArray = testObject.toCharArray();

		for (int i = 0; i < testArray.length; i++) {

			if ((testArray[i] < 'A' || testArray[i] > 'F') && (testArray[i] < '0' || testArray[i] > '9')) {
				return true;
			}
		}
		return false;
	}

}
