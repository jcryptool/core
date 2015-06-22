package org.jcryptool.visual.wots;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

public class WotsView extends ViewPart {

	private boolean german = true;

	public static final String ID = "org.jcryptool.visual.wots.WOTSView2"; //$NON-NLS-1$
	private Button btn_Genkey;
	private Button btn_Sign;
	private Button btn_VerifySig;
	private Button btn_Details;
	private Button btn_reset;
	private Button btn_restart;
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
	private GridData gd_cmb_Hash;
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
	private String message = "standard message";
	private String messageHash = org.jcryptool.visual.wots.files.Converter
			._byteToHex(instance.hashMessage(message));
	private String b = org.jcryptool.visual.wots.files.Converter
			._byteToHex(instance.initB());
	private boolean details = false;
	private boolean disable = true;
	// private boolean coloured = false;
	private int ctr;
	private Text[] txtToEnableOrDisable;
	private Button[] btnToEnableOrDisable;

	// Set language strings
	private String message_txt;
	private String loadMessage_txt;
	private String winPara_txt;
	private String hashFunction_txt;
	private String privateKey_txt;
	private String publicKey_txt;
	private String signature_txt;
	private String btnGenKeys_txt;
	private String btnGenSig_txt;
	private String btnVerSig_txt;
	private String showDetails_txt;
	private String hideDetails_txt;
	private String outWelcome_txt;
	private String outGenKeys_txt;
	private String outGenSig_txt;
	private String outVerSig_txt;
	private String outGenKeysPlus_txt;
	private String outGenSigPlus_txt;
	private String outVerSigPlus_txt;
	private String Byte_txt;
	private String Hash_txt;
	private String header_txt;
	private String headline_txt;

	public static String currentImg = "images/Overview2.JPG";

	ScrolledComposite scrolledContainer;
	Composite container;
	private Composite composite;
	private Text txtTheWinternitzonetimesignatureIs;
	private Text txtWinternitzOtsignaturewots;

	public WotsView() {
	}

	/**
	 * Create contents of the view part. The layout manager has no option to
	 * define a maximum size for text boxes, buttons, etc.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {

		setLanguage();

		scrolledContainer = new ScrolledComposite(parent, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledContainer.setAlwaysShowScrollBars(true);
		scrolledContainer.setExpandHorizontal(true);
		scrolledContainer.setExpandVertical(true);

		container = new Composite(scrolledContainer, SWT.NONE);
		GridLayout gl_container = new GridLayout(11, true);
		gl_container.marginTop = 10;
		gl_container.marginRight = 10;
		gl_container.marginLeft = 10;
		gl_container.marginBottom = 10;
		container.setLayout(gl_container);
		{
			txtWinternitzOtsignaturewots = new Text(container, SWT.NONE);
			txtWinternitzOtsignaturewots.setFont(SWTResourceManager.getFont(
					"Segoe UI", 11, SWT.BOLD));
			txtWinternitzOtsignaturewots.setText(headline_txt);
			txtWinternitzOtsignaturewots.setEditable(false);
			txtWinternitzOtsignaturewots.setLayoutData(new GridData(SWT.FILL,
					SWT.CENTER, true, false, 6, 1));
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txtTheWinternitzonetimesignatureIs = new Text(container, SWT.NONE);
			txtTheWinternitzonetimesignatureIs.setText(header_txt);
			txtTheWinternitzonetimesignatureIs.setEditable(false);
			txtTheWinternitzonetimesignatureIs.setLayoutData(new GridData(
					SWT.FILL, SWT.CENTER, true, false, 8, 1));
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			lblMessage = new Label(container, SWT.NONE);
			lblMessage.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true,
					false, 1, 1));
			lblMessage.setText(message_txt);
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			lblMessageHash = new Label(container, SWT.NONE);
			lblMessageHash.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM,
					true, false, 1, 1));
			lblMessageHash.setText(Hash_txt);
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_message = new Text(container, SWT.BORDER | SWT.WRAP
					| SWT.V_SCROLL | SWT.MULTI);
			gd_txt_message = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_message.minimumHeight = 100;
			txt_message.setLayoutData(gd_txt_message);
			txt_message.setText("Standard Message");
			txt_message.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(false);

					disable = false;

					// Changes hash and Bitstring bi if message is modified
					message = txt_message.getText();
					messageHash = org.jcryptool.visual.wots.files.Converter
							._byteToHex(instance.hashMessage(message));
					b = org.jcryptool.visual.wots.files.Converter
							._byteToHex(instance.initB());
					txt_Hash.setText(messageHash);
					txt_Bi.setText(b);

					updateLengths();
					disable = true;
				}
			});
		}
		{
			txt_Hash = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
					| SWT.MULTI);
			gd_txt_Hash = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Hash.minimumHeight = 25;
			txt_Hash.setLayoutData(gd_txt_Hash);
			txt_Hash.setText(messageHash);
			txt_Hash.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(true);

					// Changes Hash and Bitstring bi if modified

					ctr++;

					if (checkHex(txt_Hash.getText())) {
						txt_Hash.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 0, 0));
						setDisabled(txt_Hash);
						txt_HashSize.setText("Invalid character");
					} else {

						txt_Hash.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 255, 255));

						if (ctr % 2 != 0 && disable) {
							setDisabled(txt_Hash);
							txt_HashSize.setBackground(new Color(
									org.eclipse.swt.widgets.Display
											.getCurrent(), 255, 0, 0));
						} else {
							messageHash = txt_Hash.getText();
							txt_HashSize.setText(Integer
									.toString(org.jcryptool.visual.wots.files.Converter
											._stringToByte(messageHash).length / 2)
									+ "/" + n + " Bytes");

							if (org.jcryptool.visual.wots.files.Converter
									._stringToByte(messageHash).length / 2 == n) {

								instance.setMessage(org.jcryptool.visual.wots.files.Converter
										._hexStringToByte(messageHash));
								b = org.jcryptool.visual.wots.files.Converter
										._byteToHex(instance.initB());
								ctr = 1;
								txt_Bi.setText(b);
								txt_HashSize.setBackground(new Color(
										org.eclipse.swt.widgets.Display
												.getCurrent(), 240, 240, 240));
								clearOutput(false);
								setEnabled();
							}
						}
					}
				}
			});
		}
		{
			txt_Output = new Text(container, SWT.BORDER | SWT.READ_ONLY
					| SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			txt_Output.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 5, 5));
			txt_Output.setText(outWelcome_txt);
		}
		{
			txt_MessageSize = new Label(container, SWT.NONE);
			txt_MessageSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false, 1, 1));
			txt_MessageSize.setText("New Label");
		}
		{
			btnLoadMessageFrom = new Button(container, SWT.NONE);
			btnLoadMessageFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 2, 1));
			btnLoadMessageFrom.setText(loadMessage_txt);
			btnLoadMessageFrom.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					// Loads message from file
					JFileChooser chooser = new JFileChooser();
					int returnVal = chooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						System.out.println("You chose to open this file: "
								+ chooser.getSelectedFile().getName());

						File file = chooser.getSelectedFile();
						String path = file.getAbsolutePath();
						try {
							txt_message
									.setText(org.jcryptool.visual.wots.files.WotsComposite
											.readFile(path));
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null,
									"Failed to load message from file",
									"Error", JOptionPane.OK_OPTION);
							txt_message.setText("Standard message");
							e1.printStackTrace();
						}
					}

					setEnabled();
				}
			});
		}
		new Label(container, SWT.NONE);
		{
			txt_HashSize = new Label(container, SWT.RIGHT);
			txt_HashSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false, 2, 1));
			txt_HashSize.setText("New Label");
		}
		{
			lblWinternitzParameterw = new Label(container, SWT.NONE);
			lblWinternitzParameterw.setLayoutData(new GridData(SWT.LEFT,
					SWT.CENTER, true, false, 2, 1));
			lblWinternitzParameterw.setText(winPara_txt);
		}
		{
			txt_winternitzP = new Text(container, SWT.BORDER);
			txt_winternitzP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
					true, false, 1, 1));
			txt_winternitzP.setText("4");
			txt_winternitzP.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(false);

					if (txt_winternitzP.getText().equals("")) {
						setDisabled(txt_winternitzP);
						txt_Hash.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
							240, 240, 240));
						txt_Sigkey.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
							240, 240, 240));
						txt_Verifkey.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
							240, 240, 240));
						txt_Sig.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
							240, 240, 240));
						txt_Bi.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
							240, 240, 240));
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

						txt_HashSize.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								240, 240, 240));
						txt_BSize.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								240, 240, 240));
						txt_SigKeySize.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								240, 240, 240));
						txt_Sig.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
							255, 255, 255));
					}
				}
			});
		}
		new Label(container, SWT.NONE);
		{
			btnWots = new Button(container, SWT.RADIO);
			btnWots.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
					false, 2, 1));
			btnWots.setText("WOTS");
			btnWots.setSelection(true);
			btnWots.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					clearOutput(false);

					disable = false;

					// Changes type to WOTS and resets what is necessary to do
					// so
					instance = new org.jcryptool.visual.wots.WinternitzOTS(w,
							hashFunction);
					privateKey = "";
					publicKey = "";
					signature = "";
					txt_Sigkey.setText("");
					txt_Verifkey.setText("");
					txt_Sig.setText("");
					currentImg = "images/Overview2.JPG";
					Image tmp = new Image(
							img_right.getDisplay(),
							org.eclipse.ui.plugin.AbstractUIPlugin
									.imageDescriptorFromPlugin(
											"org.jcryptool.visual.wots",
											org.jcryptool.visual.wots.WotsView.currentImg)
									.createImage()
									.getImageData()
									.scaledTo(
											img_right.getImage().getBounds().width,
											img_right.getImage().getBounds().height));
					img_right.setImage(tmp);
					txt_Output.setText(outWelcome_txt);

					updateLengths();
					disable = true;

					btn_Sign.setEnabled(false);
					btn_VerifySig.setEnabled(false);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		{
			lblHashFunction = new Label(container, SWT.NONE);
			lblHashFunction.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
					true, false, 2, 1));
			lblHashFunction.setText(hashFunction_txt);
		}
		{
			cmb_Hash = new Combo(container, SWT.NONE);
			gd_cmb_Hash = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_cmb_Hash.heightHint = 4;
			cmb_Hash.setLayoutData(gd_cmb_Hash);
			cmb_Hash.add("SHA-256");
			cmb_Hash.add("SHA-1");
			cmb_Hash.add("MD5");
			cmb_Hash.select(0);
			cmb_Hash.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {

					messageHash = org.jcryptool.visual.wots.files.Converter
							._byteToHex(instance.hashMessage(message));
					b = org.jcryptool.visual.wots.files.Converter
							._byteToHex(instance.initB());

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
						btnWots.setSelection(true);
						break;
					default:
						JOptionPane
								.showMessageDialog(
										null,
										"No Hash function selected, SHA-256 will be selected automatically!",
										"Error", JOptionPane.OK_OPTION);
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

					txt_Sigkey.setBackground(new Color(
							org.eclipse.swt.widgets.Display.getCurrent(), 255,
							255, 255));
					txt_SigKeySize.setBackground(new Color(
							org.eclipse.swt.widgets.Display.getCurrent(), 240,
							240, 240));
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		new Label(container, SWT.NONE);
		{
			btnWotsPlus = new Button(container, SWT.RADIO);
			btnWotsPlus.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
					false, 2, 1));
			btnWotsPlus.setText("WOTS+");
			btnWotsPlus.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					clearOutput(false);

					disable = false;

					// Changes type to WOTS+ and resets what is necessary to do
					// so
					instance = new org.jcryptool.visual.wots.WOTSPlus(w,
							hashFunction);
					privateKey = "";
					publicKey = "";
					signature = "";
					txt_Sigkey.setText("");
					txt_Verifkey.setText("");
					txt_Sig.setText("");
					currentImg = "images/WOTSPlus.JPG";
					Image tmp = new Image(
							img_right.getDisplay(),
							org.eclipse.ui.plugin.AbstractUIPlugin
									.imageDescriptorFromPlugin(
											"org.jcryptool.visual.wots",
											org.jcryptool.visual.wots.WotsView.currentImg)
									.createImage()
									.getImageData()
									.scaledTo(
											img_right.getImage().getBounds().width,
											img_right.getImage().getBounds().height));
					img_right.setImage(tmp);
					txt_Output.setText(outWelcome_txt);

					updateLengths();

					disable = true;

					btn_Sign.setEnabled(false);
					btn_VerifySig.setEnabled(false);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		{
			lblSignatureKey = new Label(container, SWT.NONE);
			lblSignatureKey.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM,
					true, false, 2, 1));
			lblSignatureKey.setText(privateKey_txt);

		}
		new Label(container, SWT.NONE);
		{
			lblVerificationKey = new Label(container, SWT.NONE);
			lblVerificationKey.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM,
					true, false, 2, 1));
			lblVerificationKey.setText(publicKey_txt);
		}
		new Label(container, SWT.NONE);
		{
			txt_Sigkey = new Text(container, SWT.BORDER | SWT.WRAP
					| SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Sigkey = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Sigkey.minimumHeight = 100;
			txt_Sigkey.setLayoutData(gd_txt_Sigkey);
			txt_Sigkey.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(true);
					updateLengths();

					// Changes Private Key if modified

					ctr++;

					if (checkHex(txt_Sigkey.getText())) {
						txt_Sigkey.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 0, 0));
						setDisabled(txt_Sigkey);
						txt_SigKeySize.setText("Invalid character");
					} else {

						txt_Sigkey.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 255, 255));

						if (ctr % 2 != 0 && disable) {
							setDisabled(txt_Sigkey);
							txt_SigKeySize.setBackground(new Color(
									org.eclipse.swt.widgets.Display
											.getCurrent(), 255, 0, 0));
						} else {
							privateKey = txt_Sigkey.getText();
							txt_SigKeySize.setText(Integer
									.toString(org.jcryptool.visual.wots.files.Converter
											._stringToByte(privateKey).length / 2)
									+ "/" + (n * l) + Byte_txt);

							if (org.jcryptool.visual.wots.files.Converter
									._stringToByte(privateKey).length / 2 == n
									* l) {
								clearOutput(false);
								setEnabled();
								txt_SigKeySize.setBackground(new Color(
										org.eclipse.swt.widgets.Display
												.getCurrent(), 240, 240, 240));
							}
						}
					}
				}
			});

		}
		{
			txt_Verifkey = new Text(container, SWT.BORDER | SWT.WRAP
					| SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Verifkey = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Verifkey.minimumHeight = 25;
			txt_Verifkey.setLayoutData(gd_txt_Verifkey);
			txt_Verifkey.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(true);

					// Changes Public Key if modified

					ctr++;

					if (checkHex(txt_Verifkey.getText())) {
						txt_Verifkey.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 0, 0));
						setDisabled(txt_Verifkey);
						txt_VerKeySize.setText("Invalid character");
					} else {

						txt_Verifkey.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 255, 255));

						if (ctr % 2 != 0 && disable) {
							setDisabled(txt_Verifkey);
							txt_VerKeySize.setBackground(new Color(
									org.eclipse.swt.widgets.Display
											.getCurrent(), 255, 0, 0));
						} else {
							publicKey = txt_Verifkey.getText();
							txt_VerKeySize.setText(Integer
									.toString(org.jcryptool.visual.wots.files.Converter
											._stringToByte(publicKey).length / 2)
									+ "/"
									+ (n * instance.getPublicKeyLength())
									+ " Bytes");

							if (org.jcryptool.visual.wots.files.Converter
									._stringToByte(publicKey).length / 2 == (n * instance
									.getPublicKeyLength())) {
								clearOutput(false);
								setEnabled();
								txt_VerKeySize.setBackground(new Color(
										org.eclipse.swt.widgets.Display
												.getCurrent(), 240, 240, 240));
							}
						}
					}
				}
			});
		}
		{
			composite = new Composite(container, SWT.NONE);
			composite.setLayout(new GridLayout(1, false));
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true, 5, 5));
			{
				img_right = new Label(composite, SWT.CENTER);
				img_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						true, 1, 1));
				img_right.setSize(96, 184);
				img_right.setImage(ResourceManager.getPluginImage(
						"org.jcryptool.visual.wots", "images/Overview2.JPG"));
				img_right
						.addControlListener(new org.jcryptool.visual.wots.files.ResizeListener(
								img_right, composite));
			}
		}
		{
			txt_SigKeySize = new Label(container, SWT.NONE);
			txt_SigKeySize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false, 2, 1));
			txt_SigKeySize.setText("New Label");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_VerKeySize = new Label(container, SWT.RIGHT);
			txt_VerKeySize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false, 2, 1));
			txt_VerKeySize.setText("New Label");
		}
		{
			lblSignature = new Label(container, SWT.NONE);
			lblSignature.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true,
					false, 2, 1));
			lblSignature.setText(signature_txt);
		}
		new Label(container, SWT.NONE);
		{
			lblBi = new Label(container, SWT.NONE);
			lblBi.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false,
					1, 1));
			lblBi.setText("b_i");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_Sig = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
					| SWT.MULTI);
			gd_txt_Sig = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Sig.minimumHeight = 125;
			txt_Sig.setLayoutData(gd_txt_Sig);

			txt_Sig.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(false);

					// Changes signature if modified

					ctr++;

					if (checkHex(txt_Sig.getText())) {
						txt_Sig.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 0, 0));
						setDisabled(txt_Sig);
						txt_SignatureSize.setText("Invalid character");
					} else {

						txt_Sig.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 255, 255));

						if (ctr % 2 != 0 && disable) {
							setDisabled(txt_Sig);
							txt_SignatureSize.setBackground(new Color(
									org.eclipse.swt.widgets.Display
											.getCurrent(), 255, 0, 0));
						} else {
							signature = txt_Sig.getText();
							txt_SignatureSize.setText(Integer
									.toString(org.jcryptool.visual.wots.files.Converter
											._stringToByte(signature).length / 2)
									+ "/" + (n * l) + " Bytes");

							if (org.jcryptool.visual.wots.files.Converter
									._stringToByte(signature).length / 2 == n
									* l) {
								setEnabled();
								txt_SignatureSize.setBackground(new Color(
										org.eclipse.swt.widgets.Display
												.getCurrent(), 240, 240, 240));
							}
						}
					}
				}
			});

		}
		{
			txt_Bi = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
					| SWT.MULTI);
			gd_txt_Bi = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Bi.minimumHeight = 125;
			txt_Bi.setLayoutData(gd_txt_Bi);
			txt_Bi.setText(b);

			txt_Bi.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {

					clearOutput(true);

					// Changes Bitstring bi if modified
					ctr++;

					if (checkHex(txt_Bi.getText())) {
						txt_Bi.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 0, 0));
						setDisabled(txt_Bi);
						txt_BSize.setText("Invalid character");
					} else {

						txt_Bi.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 255, 255));

						if (ctr % 2 != 0 && disable) {
							setDisabled(txt_Bi);
							txt_BSize.setBackground(new Color(
									org.eclipse.swt.widgets.Display
											.getCurrent(), 255, 0, 0));
						} else {
							b = txt_Bi.getText();
							instance.setBi(org.jcryptool.visual.wots.files.Converter
									._hexStringToByte(b));
							txt_BSize.setText(Integer
									.toString(org.jcryptool.visual.wots.files.Converter
											._stringToByte(b).length / 2)
									+ "/" + l + " Bytes");

							if (org.jcryptool.visual.wots.files.Converter
									._stringToByte(b).length / 2 == l) {
								clearOutput(false);
								setEnabled();
								txt_BSize.setBackground(new Color(
										org.eclipse.swt.widgets.Display
												.getCurrent(), 240, 240, 240));
							}
						}
					}
				}
			});
		}
		{
			txt_SignatureSize = new Label(container, SWT.NONE);
			txt_SignatureSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
					true, false, 2, 1));
			txt_SignatureSize.setText("New Label");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_BSize = new Label(container, SWT.RIGHT);
			txt_BSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
					false, 2, 1));
			txt_BSize.setText("New Label");
		}
		{
			btn_Genkey = new Button(container, SWT.NONE);
			btn_Genkey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 2, 1));
			btn_Genkey.setText(btnGenKeys_txt);
			btn_Genkey.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					clearOutput(false);

					// KEY GENERATION

					disable = false;

					if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {

						// Set Image & Output field for WOTS
						txt_Output.setText(outGenKeys_txt);
						currentImg = "images/Key_Generation.JPG";
						Image tmp = new Image(
								img_right.getDisplay(),
								org.eclipse.ui.plugin.AbstractUIPlugin
										.imageDescriptorFromPlugin(
												"org.jcryptool.visual.wots",
												org.jcryptool.visual.wots.WotsView.currentImg)
										.createImage()
										.getImageData()
										.scaledTo(
												img_right.getImage()
														.getBounds().width,
												img_right.getImage()
														.getBounds().height));
						img_right.setImage(tmp);

					} else if (!btnWots.getSelection()
							&& btnWotsPlus.getSelection()) {

						// Set Image & Output field for WOTS+
						txt_Output.setText(outGenKeysPlus_txt);
						currentImg = "images/WOTSPlus.JPG";
						Image tmp = new Image(
								img_right.getDisplay(),
								org.eclipse.ui.plugin.AbstractUIPlugin
										.imageDescriptorFromPlugin(
												"org.jcryptool.visual.wots",
												org.jcryptool.visual.wots.WotsView.currentImg)
										.createImage()
										.getImageData()
										.scaledTo(
												img_right.getImage()
														.getBounds().width,
												img_right.getImage()
														.getBounds().height));
						img_right.setImage(tmp);

					} else {

						txt_Output.setText("An Error occured");

					}

					// Generate Keys
					setOutputs();
					instance.generateKeyPair();
					getOutputs();

					disable = true;

					btn_VerifySig.setEnabled(false);
				}
			});
		}
		{
			btn_Sign = new Button(container, SWT.NONE);
			btn_Sign.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 2, 1));
			btn_Sign.setText(btnGenSig_txt);
			btn_Sign.setEnabled(false);
			btn_Sign.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					clearOutput(false);

					// SIGNATURE GENERATION

					disable = false;

					if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {

						// Set Image & Output field for WOTS
						txt_Output.setText(outGenSig_txt);
						currentImg = "images/Signature_Generation.JPG";
						Image tmp = new Image(
								img_right.getDisplay(),
								org.eclipse.ui.plugin.AbstractUIPlugin
										.imageDescriptorFromPlugin(
												"org.jcryptool.visual.wots",
												org.jcryptool.visual.wots.WotsView.currentImg)
										.createImage()
										.getImageData()
										.scaledTo(
												img_right.getImage()
														.getBounds().width,
												img_right.getImage()
														.getBounds().height));
						img_right.setImage(tmp);

					} else if (!btnWots.getSelection()
							&& btnWotsPlus.getSelection()) {

						// Set Image & Output field for WOTS+
						txt_Output.setText(outGenSigPlus_txt);
						currentImg = "images/WOTSPlus.JPG";
						Image tmp = new Image(
								img_right.getDisplay(),
								org.eclipse.ui.plugin.AbstractUIPlugin
										.imageDescriptorFromPlugin(
												"org.jcryptool.visual.wots",
												org.jcryptool.visual.wots.WotsView.currentImg)
										.createImage()
										.getImageData()
										.scaledTo(
												img_right.getImage()
														.getBounds().width,
												img_right.getImage()
														.getBounds().height));
						img_right.setImage(tmp);

					} else {

						JOptionPane
								.showMessageDialog(
										null,
										"No WOTS version selected, WOTS will be selected automatically!",
										"Error", JOptionPane.OK_OPTION);
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
		}
		{
			btn_VerifySig = new Button(container, SWT.NONE);
			btn_VerifySig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 2, 1));
			btn_VerifySig.setText(btnVerSig_txt);
			btn_VerifySig.setEnabled(false);
			btn_VerifySig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					// SIGNATURE VERIFICATION

					disable = false;

					if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {

						// Set Image & Output field for WOTS
						txt_Output.setText(outVerSig_txt);
						currentImg = "images/Signature_Verification.JPG";
						Image tmp = new Image(
								img_right.getDisplay(),
								org.eclipse.ui.plugin.AbstractUIPlugin
										.imageDescriptorFromPlugin(
												"org.jcryptool.visual.wots",
												org.jcryptool.visual.wots.WotsView.currentImg)
										.createImage()
										.getImageData()
										.scaledTo(
												img_right.getImage()
														.getBounds().width,
												img_right.getImage()
														.getBounds().height));
						img_right.setImage(tmp);

					} else if (!btnWots.getSelection()
							&& btnWotsPlus.getSelection()) {

						// Set Image & Output field for WOTS+
						txt_Output.setText(outVerSigPlus_txt);
						currentImg = "images/WOTSPlus.JPG";
						Image tmp = new Image(
								img_right.getDisplay(),
								org.eclipse.ui.plugin.AbstractUIPlugin
										.imageDescriptorFromPlugin(
												"org.jcryptool.visual.wots",
												org.jcryptool.visual.wots.WotsView.currentImg)
										.createImage()
										.getImageData()
										.scaledTo(
												img_right.getImage()
														.getBounds().width,
												img_right.getImage()
														.getBounds().height));
						img_right.setImage(tmp);

					} else {

						JOptionPane
								.showMessageDialog(
										null,
										"No WOTS version selected, WOTS will be selected automatically!",
										"Error", JOptionPane.OK_OPTION);
						btnWots.setSelection(true);
						btnWotsPlus.setSelection(false);
					}

					// Verify Signature
					setOutputs();
					if (instance.verify()) {
						txt_Sig.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								0, 255, 0));
					} else {
						txt_Sig.setBackground(new Color(
								org.eclipse.swt.widgets.Display.getCurrent(),
								255, 0, 0));
					}
					disable = true;
				}
			});
		}
		new Label(container, SWT.NONE);
		{
			btn_Details = new Button(container, SWT.NONE);
			gd_btn_Details = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
			gd_btn_Details.minimumWidth = 95;
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
						txt_Output.setText("An Error occured");
					}
					container.layout();
					txt_HashSize.setBackground(new Color(
							org.eclipse.swt.widgets.Display.getCurrent(), 240,
							240, 240));
				}
			});
		}
		{
			btn_reset = new Button(container, SWT.NONE);
			btn_reset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			btn_reset.setText("Reset");
			btn_reset.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					reset();
				}
			});
		}
		{
			btn_restart = new Button(container, SWT.NONE);
			btn_restart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					false, 1, 1));
			btn_restart.setText("Restart");
			btn_restart.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					restart();
				}
			});
		}

		// Finisch Initialization
		txtToEnableOrDisable = new Text[] { txt_message, txt_Sigkey,
				txt_Verifkey, txt_Hash, txt_Sig, txt_Bi, txt_winternitzP };
		btnToEnableOrDisable = new Button[] { btnWots, btnWotsPlus, btn_Genkey,
				btn_VerifySig, btn_Sign, btnLoadMessageFrom };
		disableDetails();
		updateLengths();

		scrolledContainer.setContent(container);
		scrolledContainer.setMinSize(1200, 800);

		// createActions();
		// initializeToolBar();
		// initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	/**
	 * Sets the Variables of the WOTS/WOTS+ instance to the one defined in this
	 * class
	 */
	private void setOutputs() {

		instance.setW(w);
		instance.setMessageDigest(hashFunction);
		instance.setPrivateKey(org.jcryptool.visual.wots.files.Converter
				._hexStringTo2dByte(privateKey, instance.getLength()));
		instance.setPublicKey(org.jcryptool.visual.wots.files.Converter
				._hexStringTo2dByte(publicKey, instance.getPublicKeyLength()));
		instance.setSignature(org.jcryptool.visual.wots.files.Converter
				._hexStringToByte(signature));
		instance.setMessage(org.jcryptool.visual.wots.files.Converter
				._hexStringToByte(messageHash));
		instance.setBi(org.jcryptool.visual.wots.files.Converter
				._hexStringToByte(b));
	}

	/**
	 * Get the calculated Values from the WOTS/WOTS+ instance and set global
	 * variables of this class Sets txt-fields to the values got from WOTS/WOTS+
	 * instance
	 */
	private void getOutputs() {
		this.privateKey = org.jcryptool.visual.wots.files.Converter
				._2dByteToHex(instance.getPrivateKey());
		this.publicKey = org.jcryptool.visual.wots.files.Converter
				._2dByteToHex(instance.getPublicKey());
		this.signature = org.jcryptool.visual.wots.files.Converter
				._byteToHex(instance.getSignature());
		this.messageHash = org.jcryptool.visual.wots.files.Converter
				._byteToHex(instance.getMessageHash());
		this.b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance
				.getBi());
		this.n = instance.getN();
		this.l = instance.getL();

		txt_Sigkey.setText(privateKey);
		txt_Verifkey.setText(publicKey);
		txt_Bi.setText(b);
		txt_Sig.setText(signature);
		txt_Hash.setText(messageHash);

		updateLengths();
	}

	/**
	 * Method that disables all txt-fields and buttons that can be
	 * activated/edited except for the @param exception
	 */
	private void setDisabled(Text exception) {

		// Disables all Buttons and editable text-fields except for the given
		// exception
		for (int i = 0; i < txtToEnableOrDisable.length; i++) {
			if (!txtToEnableOrDisable[i].equals(exception)) {
				txtToEnableOrDisable[i].setEnabled(false);
				txtToEnableOrDisable[i].setEditable(false);
				txtToEnableOrDisable[i].setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
						240, 240, 240));
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
			txtToEnableOrDisable[i].setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(),
					255, 255, 255));
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
		messageHash = org.jcryptool.visual.wots.files.Converter
				._byteToHex(instance.hashMessage(message));
		b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance
				.initB());

		txt_Sigkey.setText("");
		txt_Sig.setText("");
		txt_Verifkey.setText("");
		txt_Output.setText(outWelcome_txt);
		currentImg = "images/Overview2.JPG";
		Image tmp = new Image(img_right.getDisplay(),
				org.eclipse.ui.plugin.AbstractUIPlugin
						.imageDescriptorFromPlugin("org.jcryptool.visual.wots",
								org.jcryptool.visual.wots.WotsView.currentImg)
						.createImage()
						.getImageData()
						.scaledTo(img_right.getImage().getBounds().width,
								img_right.getImage().getBounds().height));
		img_right.setImage(tmp);
		txt_Hash.setText(messageHash);
		txt_Bi.setText(b);

		updateLengths();
		setEnabled();

		btn_Sign.setEnabled(false);
		btn_VerifySig.setEnabled(false);

		clearOutput(false);

		txt_SigKeySize.setBackground(new Color(org.eclipse.swt.widgets.Display
				.getCurrent(), 240, 240, 240));
		txt_VerKeySize.setBackground(new Color(org.eclipse.swt.widgets.Display
				.getCurrent(), 240, 240, 240));
		txt_SignatureSize.setBackground(new Color(
				org.eclipse.swt.widgets.Display.getCurrent(), 240, 240, 240));
		txt_BSize.setBackground(new Color(org.eclipse.swt.widgets.Display
				.getCurrent(), 240, 240, 240));

		container.layout();
	}

	/**
	 * Updates Lengths of all txt_fields and set correct values to txt_lenght
	 * fields
	 */
	private void updateLengths() {

		txt_MessageSize.setText(Integer
				.toString(org.jcryptool.visual.wots.files.Converter
						._stringToByte(message).length)
				+ Byte_txt);
		txt_SigKeySize.setText(Integer
				.toString(org.jcryptool.visual.wots.files.Converter
						._stringToByte(privateKey).length / 2)
				+ "/" + (n * l) + Byte_txt);
		txt_VerKeySize.setText(Integer
				.toString(org.jcryptool.visual.wots.files.Converter
						._stringToByte(publicKey).length / 2)
				+ "/" + (n * instance.getPublicKeyLength()) + Byte_txt);
		txt_HashSize.setText(Integer
				.toString(org.jcryptool.visual.wots.files.Converter
						._hexStringToByte(messageHash).length)
				+ "/" + n + Byte_txt);
		txt_SignatureSize.setText(Integer
				.toString(org.jcryptool.visual.wots.files.Converter
						._stringToByte(signature).length / 2)
				+ "/" + (n * l) + Byte_txt);
		txt_BSize.setText(Integer
				.toString(org.jcryptool.visual.wots.files.Converter
						._hexStringToByte(b).length)
				+ "/" + l + Byte_txt);
	}

	/**
	 * Sets back verify button
	 */
	private void clearOutput(boolean changeColour) {
		if (changeColour) {
			txt_Sig.setBackground(new Color(org.eclipse.swt.widgets.Display
					.getCurrent(), 240, 240, 240));
		} else {
			txt_Sig.setBackground(new Color(org.eclipse.swt.widgets.Display
					.getCurrent(), 255, 255, 255));
		}
		btn_VerifySig.setText(btnVerSig_txt);
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
		message = "standard message";
		messageHash = org.jcryptool.visual.wots.files.Converter
				._byteToHex(instance.hashMessage(message));
		b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance
				.initB());
		details = false;
		disable = true;

		// Set Attributes for Objects

		btn_Details.setText(showDetails_txt);

		btnWots.setSelection(true);
		btnWotsPlus.setSelection(false);

		lblMessageHash.setEnabled(false);
		lblMessageHash.setVisible(false);

		lblBi.setEnabled(false);
		lblBi.setVisible(false);

		txt_winternitzP.setText("4");
		txt_Sigkey.setText("");
		txt_message.setText("standard message");
		txt_Output.setText(outWelcome_txt);
		txt_Hash.setText(messageHash);
		txt_Bi.setText(b);
		disableDetails();

		currentImg = "images/Overview2.JPG";
		Image tmp = new Image(img_right.getDisplay(),
				org.eclipse.ui.plugin.AbstractUIPlugin
						.imageDescriptorFromPlugin("org.jcryptool.visual.wots",
								org.jcryptool.visual.wots.WotsView.currentImg)
						.createImage()
						.getImageData()
						.scaledTo(img_right.getImage().getBounds().width,
								img_right.getImage().getBounds().height));
		img_right.setImage(tmp);
		cmb_Hash.select(0);

		txt_Sig.setText("");
		txt_Sigkey.setText("");
		txt_Verifkey.setText("");

		updateLengths();
		setEnabled();

		btn_Sign.setEnabled(false);
		btn_VerifySig.setEnabled(false);
		clearOutput(false);

		txt_SigKeySize.setBackground(new Color(org.eclipse.swt.widgets.Display
				.getCurrent(), 240, 240, 240));
		txt_VerKeySize.setBackground(new Color(org.eclipse.swt.widgets.Display
				.getCurrent(), 240, 240, 240));
		txt_SignatureSize.setBackground(new Color(
				org.eclipse.swt.widgets.Display.getCurrent(), 240, 240, 240));
		txt_BSize.setBackground(new Color(org.eclipse.swt.widgets.Display
				.getCurrent(), 240, 240, 240));

		container.layout();
	}

	/**
	 * Disables detailed view
	 */
	private void disableDetails() {

		btn_Details.setText(showDetails_txt);

		// make Message and Hash
		gd_txt_Hash.exclude = true;
		txt_Hash.setEnabled(false);
		txt_Hash.setVisible(false);
		gd_txt_message.horizontalSpan = 6;
		lblMessageHash.setVisible(false);
		lblMessageHash.setEnabled(false);
		txt_HashSize.setVisible(false);
		gd_txt_Sig.minimumHeight = 25;

		// make b_i and Signature
		gd_txt_Bi.exclude = true;
		txt_Bi.setEnabled(false);
		txt_Bi.setVisible(false);
		gd_txt_Sig.horizontalSpan = 6;
		lblBi.setVisible(false);
		lblBi.setEnabled(false);
		txt_BSize.setVisible(false);
		gd_txt_Sig.minimumHeight = 50;
	}

	/**
	 * Enables Detailed view
	 */
	private void enableDetails() {

		btn_Details.setText(hideDetails_txt);

		// make Message and Hash
		gd_txt_Hash.exclude = false;
		txt_Hash.setEnabled(true);
		txt_Hash.setVisible(true);
		gd_txt_message.horizontalSpan = 3;
		lblMessageHash.setVisible(true);
		lblMessageHash.setEnabled(true);
		txt_HashSize.setVisible(true);

		// make b_i and Signature
		gd_txt_Bi.exclude = false;
		txt_Bi.setEnabled(true);
		txt_Bi.setVisible(true);
		gd_txt_Sig.horizontalSpan = 3;
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

			if ((testArray[i] < 'A' || testArray[i] > 'F')
					&& (testArray[i] < '0' || testArray[i] > '9')) {
				return true;
			}
		}
		return false;
	}

	private void setLanguage() {

		// Set language
		if (german) {
			headline_txt = "Winternitz Einmal-Signatur (WOTS / WOTS+)";
			header_txt = "Die Winternitz Einmal-Signatur ist ein Algorithmus zum Signieren einer Nachricht";
			message_txt = "Nachricht";
			loadMessage_txt = "Lade Nachricht aus Datei";
			winPara_txt = "Winternitz Parameter w";
			hashFunction_txt = "Hashfunktion";
			privateKey_txt = "Privater Schl\u00fcssel";
			publicKey_txt = "\u00D6ffentlicher Schl\u00fcssel";
			signature_txt = "Signatur";
			btnGenKeys_txt = "Schl\u00fcssel erstellen";
			btnGenSig_txt = "Signatur erstellen";
			btnVerSig_txt = "Signatur pr\u00fcfen";
			showDetails_txt = "Details anzeigen";
			hideDetails_txt = "Details verstecken";
			Byte_txt = " Byte";
			Hash_txt = "Hash der Nachricht";
			outWelcome_txt = "Willkommen zur JCT-Visualisierung WOTS/WOTS+\n\n"
					+ "Das Winternitz-Einmal-Signaturverfahren (Winternitz-One-Time-Signature - WOTS)"
					+ " ist ein Hash-basiertes Verfahren zum digitalen Signieren von Nachrichten. Der "
					+ "\u00f6ffentliche Schl\u00fcssel  wird erzeugt, indem die Bl\u00f6cke des privaten Schl\u00fcssels eine "
					+ "bestimmte Anzahl oft eine Hashfunktion durchlaufen (diese Anzahl ist abh\u00e4ngig vom "
					+ "Winternitz-Parameter w (\u2265 2)).\n\nDetails siehe Online-Hilfe: www.onlinehilfe.com/Einleitung";
			outGenKeys_txt = "Schl\u00fcsselerzeugung:\n\nBei der Schl\u00fcsselerzeugung von WOTS werden zuerst die Parameter t, n und w "
					+ "ermittelt.\n\nPrivater Schl\u00fcssel:\nEs werden t Bl\u00f6cke mit n zuf\u00e4lligen Bytes aufgef\u00fcllt.\n\n"
					+ "\u00D6ffentlicher Schl\u00fcssel:\nJeder Block des privaten Schl\u00fcssels durchl\u00e4uft w-1 mal die "
					+ "Hashfunktion. Das Resultat sind die Bl\u00f6cke des \u00f6ffentlichen Schl\u00fcssels.\n\nDetails und "
					+ "Formeln zur Berechnung der Parameter siehe Online-Hilfe: www.onlinehilfe.com/WOTS-Key-Generation";
			outGenSig_txt = "Signaturerzeugung:\n\nBei der Erstellung einer Signatur mit WOTS wird zuerst der Hash der Nachricht "
					+ "erzeugt und eine Pr\u00fcfsumme \u00fcber diesen berechnet.\n\nDiese beiden Werte werden in t "
					+ "Teile aufgeteilt und f\u00fcr das weitere Vorgehen als Integer-Werte b_i interpretiert.\n\n"
					+ "Bei der Erstellung der Signatur werden die Bl\u00f6cke des privaten Schl\u00fcssels jeweils b_i "
					+ "mal gehashed.\n\nDetails und Formel zur Berechnung der Pr\u00fcfsumme siehe Online-Hilfe: "
					+ "www.onlinehilfe.com/WOTS-Signature-Generation";
			outVerSig_txt = "Signaturpr\u00fcfung:\n\nBei der Pr\u00fcfung der Signatur mit WOTS werden zuerst die b_i Werte gleich wie "
					+ "bei der Signaturerstellung berechnet.\n\nDanach wird jeder Block der Signatur w-1-b_i "
					+ "mal gehashed, somit wurde der private Schl\u00fcssel nun insgesamt w-1 mal gehashed und es "
					+ "sollte jeder berechnete Block dem Wert des \u00f6ffentlichen Schl\u00fcssels entsprechen.\n\n"
					+ "Details und Formel zur Berechnung der Pr\u00fcfsumme siehe Online-Hilfe: "
					+ "www.onlinehilfe.com/WOTS-Signature-Verification";
			outGenKeysPlus_txt = "Schl\u00fcsselerzeugung:\n\nBei der Schl\u00fcsselerzeugung von WOTS+ werden zuerst die Parameter t, n und "
					+ "w ermittelt.\n\nPrivater Schl\u00fcssel:\nEs werden t Bl\u00f6cke mit n zuf\u00e4lligen Bytes aufgef\u00fcllt.\n\n"
					+ "\u00D6ffentlicher Schl\u00fcssel:\nZus\u00e4tzlich werden w-1 Bl\u00f6cke (R) zuf\u00e4llig generiert. Danach wird "
					+ "auf jeden Block des privaten Schl\u00fcssels in w-1 Runden zuerst ein XOR mit den Bl\u00f6cken R und "
					+ "danach die Hashfunktion angewandt.\nDer \u00f6ffentliche Schl\u00fcssel besteht nun aus den "
					+ "berechneten Bl\u00f6cken plus den vorangestellten Bl\u00f6cken von R.\n\nDetails und Formeln zur "
					+ "Berechnung der Parameter siehe Online-Hilfe: www.onlinehilfe.com/WOTS+-Key-Generation";
			outGenSigPlus_txt = "Signaturerzeugung:\n\nBei der Erstellung einer Signatur mit WOTS+ wird zuerst der Hash der Nachricht "
					+ "erzeugt und eine Pr\u00fcfsumme \u00fcber diesen berechnet.Diese beiden Werte werden in t "
					+ "Teile aufgeteilt und f\u00fcr das weitere Vorgehen als Integer-Werte b_i interpretiert.\n\n"
					+ "Bei der Erstellung der Signatur werden die Bl\u00f6cke des privaten Schl\u00fcssels nun jeweils "
					+ "in b_i Runden bearbeitet (jede Runde XOR + Hash).\n\nDetails und Formel zur Berechnung "
					+ "der Pr\u00fcfsumme siehe Online-Hilfe: www.onlinehilfe.com/WOTS+-Signature-Generation";
			outVerSigPlus_txt = "Signaturpr\u00fcfung:\n\nBei der Pr\u00fcfung der Signatur mit WOTS+ werden zuerst die b_i Werte gleich wie "
					+ "bei der Signaturerstellung berechnet.\n\nDanach wird jeder Block der Signatur in w-1-b_i "
					+ "Runden bearbeitet (jede Runde XOR + Hash), somit wurde der private Schl\u00fcssel nun "
					+ "insgesamt w-1 mal bearbeitet und es sollte jeder berechnete Block dem Wert des "
					+ "\u00f6ffentlichen Schl\u00fcssels entsprechen.\n\nDetails und Formel zur Berechnung der Pr\u00fcfsumme "
					+ "siehe Online-Hilfe: www.onlinehilfe.com/WOTS+-Signature-Verification";
		} else {
			headline_txt = "Winternitz OT-Signature (WOTS / WOTS+)";
			header_txt = "The Winternitz-One-Time-Signature is a algorithm to create a signature to a given message";
			message_txt = "Message";
			loadMessage_txt = "Load message from file";
			winPara_txt = "Winternitz parameter w";
			hashFunction_txt = "Hash function";
			privateKey_txt = "Private key";
			publicKey_txt = "Public key";
			signature_txt = "Signature";
			btnGenKeys_txt = "Generate keys";
			btnGenSig_txt = "Generate signature";
			btnVerSig_txt = "Verify signature";
			showDetails_txt = "Show details";
			hideDetails_txt = "Hide details";
			Byte_txt = " byte";
			Hash_txt = "Message hash";
			outWelcome_txt = "Welcome to JCT-Visualization WOTS/WOTS+\n\nThe Winternitz-One-Time-Signature-"
					+ "Algorithm is a hash based method to sign messages. The public key is generated by "
					+ "hashing the randomly generated blocks of the private key. How often the blocks are "
					+ "hashed is determined by the Winternitz parameter w (\u2265 2)).\n\nAs a step of calculating the "
					+ "public key, the signature is generated. Because of the one-way functionality of the "
					+ "used hash function it is impossible to get back to the private key using the signature, "
					+ "but with further calculation the result will be the public key.\n\nFor more details "
					+ "visit: www.onlinehilfe.com/Introduction";
			outGenKeys_txt = "Key generation:\n\nWhen generating the keys, the parameters t,n and w are defined first.\n\nPrivate "
					+ "key:\nt blocks are filled with n random bytes.\n\nPublic key:\nEvery block of the private"
					+ " key is hashed w-1 times, which results into the blocks of the public key.\n\nFor more "
					+ "details and formulas on how to generate the parameters visit: www.onlinehilfe.com/WOTS-key-generation";
			outGenSig_txt = "Signature generation:\n\nWhen generating a signature of a message, first the hash-value and a checksum over "
					+ "the hash-value are calculated. These values are split up into t parts and will be used "
					+ "as integer values b_i for further calculations.\n\nTo compute the signature, the blocks "
					+ "of the private key are hashed b_i times.\n\nFor more details and formulas on how to "
					+ "calculate the checksum visit: www.onlinehilfe.com/WOTS-signature-generation";
			outVerSig_txt = "Signature verification:\n\nTo verify a signature, first the b_i values are calculated (see signature "
					+ "generation).\n\nSecond the blocks of the signature are hashed w-1-b_i times. If the "
					+ "computation matches the blocks of the public key (hashed w-1 times), the signature is "
					+ "valid.\n\nFor more details and formulas on checksum calculation the checksum visit: "
					+ "www.onlinehilfe.com/WOTS-signature-verification";
			outGenKeysPlus_txt = "Key generation:\n\nWhen generating the keys, the parameters t,n and w are defined first.\n\n"
					+ "Private key:\nt blocks are filled with n random bytes.\n\nPublic key:\nAdditionally w-1 "
					+ "random blocks (R) are generated. Now on every block, in w-1 rounds, first a XOR with the "
					+ "block of R followed by the hash function are applied. The public key consists of the "
					+ "calculated blocks and the blocks of R.\n\nFor more details and formulas on how to "
					+ "generate the parameters visit: www.onlinehilfe.com/WOTS+-key-generation";
			outGenSigPlus_txt = "Signature generation:\n\nWhen generating a signature of a message, first the hash-value and a checksum "
					+ "over the hash-value are calculated. These values are split up into t parts and will be "
					+ "used as integer values b_i for further calculations.\n\nTo compute the signature, the "
					+ "blocks of the private key are processed in b_i rounds (XOR and hash every round).\n\nFor "
					+ "more details and formulas on how to calculate the checksum visit: www.onlinehilfe.com/WOTS+-signature-generation";
			outVerSigPlus_txt = "Signature verification:\n\nTo verify a signature, first the b_i values are calculated (see signature "
					+ "generation).\n\nSecond the blocks of the signature are processed w-1-b_i times (XOR and "
					+ "hash every round).  If the computation matches the blocks of the public key (processed "
					+ "w-1 times), the signature is valid.\n\nFor more details and formulas on how to calculate "
					+ "the checksum visit: www.onlinehilfe.com/WOTS+-signature-verification";
		}
	}
}
