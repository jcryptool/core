package org.jcryptool.visual.wots;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.graphics.Point;

public class WotsView extends ViewPart {

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
	GridData gd_txt_message;
	GridData gd_txt_SigKeySize;
	GridData gd_lblMessageHash;
	GridData gd_txt_Hash;
	GridData gd_txt_Output;
	GridData gd_btnLoadMessageFrom;
	GridData gd_txt_MessageSize;
	GridData gd_txt_HashSize;
	GridData gd_lblWinternitzParameterw;
	GridData gd_txt_winternitzP;
	GridData gd_btnWots;
	GridData gd_lblHashFunction;
	GridData gd_cmb_Hash;
	GridData gd_btnWotsPlus;
	GridData gd_lblSignatureKey;
	GridData gd_lblVerificationKey;
	GridData gd_txt_VerKeySize;
	GridData gd_lblBi;
	GridData gd_img_right;
	GridData gd_txt_BSize;
	GridData gd_lblSignature;
	GridData gd_txt_SignatureSize;
	GridData gd_btn_Genkey;
	GridData gd_btn_Sign;
	GridData gd_btn_VerifySig;
	GridData gd_btn_reset;
	GridData gd_btn_restart;
	GridData gd_btn_Details;
	GridData gd_txt_Sig;
	GridData gd_txt_Bi;
	GridData gd_txt_Verifkey;
	GridData gd_txt_Sigkey;
	
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
	private String messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
	private String b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
	private boolean details = false;
	private boolean disable = true;
	//private boolean coloured = false;
	private int ctr;
	private Text[] txtToEnableOrDisable;
	private Button[] btnToEnableOrDisable; 

	public WotsView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl_container = new GridLayout(11, true);
		gl_container.marginTop = 10;
		gl_container.marginRight = 10;
		gl_container.marginLeft = 10;
		gl_container.marginBottom = 10;
		container.setLayout(gl_container);
		{
			lblMessage = new Label(container, SWT.NONE);
			lblMessage.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
			lblMessage.setText("Message");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			lblMessageHash = new Label(container, SWT.NONE);
			lblMessageHash.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
			lblMessageHash.setText("Hash");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_message = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			gd_txt_message = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_message.minimumHeight = 25;
			txt_message.setLayoutData(gd_txt_message);
			txt_message.setText("Standard Message");
			txt_message.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					
					clearOutput();
					
					disable = false;
					
					// Changes hash and Bitstring bi if message is modified
					message = txt_message.getText();
					messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
					b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
					txt_Hash.setText(messageHash);
					txt_Bi.setText(b);
					
					updateLengths();
					disable = true;
				}
			});
		}
		{
			txt_Hash = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Hash = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Hash.minimumHeight = 25;
			txt_Hash.setLayoutData(gd_txt_Hash);
			txt_Hash.setText(messageHash);
			txt_Hash.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					
					clearOutput();
					
					// Changes Hash and Bitstring bi if modified
									
					ctr++;
					
					if (ctr%2 != 0 && disable) {
						setDisabled(txt_Hash);
					} else {
						messageHash = txt_Hash.getText();
						txt_HashSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(messageHash).length/2) + "/" + n + " Bytes");

						if (org.jcryptool.visual.wots.files.Converter._stringToByte(messageHash).length/2 == n) {
							
							instance.setMessage(org.jcryptool.visual.wots.files.Converter._hexStringToByte(messageHash));
							b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
							ctr = 1;
							txt_Bi.setText(b);
							setEnabled();
						} 
					}
				}
			});
		}
		{
			txt_Output = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			txt_Output.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 5));
			txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");
		}
		{
			txt_MessageSize = new Label(container, SWT.NONE);
			txt_MessageSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
			txt_MessageSize.setText("New Label");
		}
		{
			btnLoadMessageFrom = new Button(container, SWT.NONE);
			btnLoadMessageFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			btnLoadMessageFrom.setText("Load Message from File");
			btnLoadMessageFrom.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					// Loads message from file
					JFileChooser chooser = new JFileChooser();
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				       System.out.println("You chose to open this file: " +
				            chooser.getSelectedFile().getName());
				       
				       File file = chooser.getSelectedFile();
				       String path = file.getAbsolutePath();
				       try {
							txt_message.setText(org.jcryptool.visual.wots.files.WotsComposite.readFile(path));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
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
			txt_HashSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			txt_HashSize.setText("New Label");
		}
		{
			lblWinternitzParameterw = new Label(container, SWT.NONE);
			lblWinternitzParameterw.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			lblWinternitzParameterw.setText("Winternitz Parameter w");
		}
		{
			txt_winternitzP = new Text(container, SWT.BORDER);
			txt_winternitzP.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			txt_winternitzP.setText("4");
			txt_winternitzP.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					
					clearOutput();
					
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
				}
			});
		}
		new Label(container, SWT.NONE);
		{
			btnWots = new Button(container, SWT.RADIO);
			btnWots.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			btnWots.setText("WOTS");
			btnWots.setSelection(true);
			btnWots.addSelectionListener( new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					clearOutput();
					
					disable = false;
					
					// Changes type to WOTS and resets what is necessary to do so
					instance = new org.jcryptool.visual.wots.WinternitzOTS(w, hashFunction);
					privateKey = "";
					publicKey = "";
					signature = "";
					txt_Sigkey.setText("");
					txt_Verifkey.setText("");
					txt_Sig.setText("");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));
					txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");
					
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
			lblHashFunction.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			lblHashFunction.setText("Hash function");
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
					
					messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
					b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
					
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
							//TODO ERROR Handling
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
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
		}
		new Label(container, SWT.NONE);
		{
			btnWotsPlus = new Button(container, SWT.RADIO);
			btnWotsPlus.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			btnWotsPlus.setText("WOTS+");
			btnWotsPlus.addSelectionListener( new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					clearOutput();
					
					disable = false;
					
					// Changes type to WOTS+ and resets what is necessary to do so
					instance = new org.jcryptool.visual.wots.WOTSPlus(w, hashFunction);
					privateKey = "";
					publicKey = "";
					signature = "";
					txt_Sigkey.setText("");
					txt_Verifkey.setText("");
					txt_Sig.setText("");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));
					txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");

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
			lblSignatureKey.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
			lblSignatureKey.setText("Private Key");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			lblVerificationKey = new Label(container, SWT.NONE);
			lblVerificationKey.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
			lblVerificationKey.setText("Public Key");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_Sigkey = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Sigkey = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Sigkey.minimumHeight = 25;
			txt_Sigkey.setLayoutData(gd_txt_Sigkey);
			txt_Sigkey.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					
					clearOutput();
					updateLengths();
					
					// Changes Private Key if modified
					
					ctr++;
					
					if (ctr%2 != 0 && disable) {
						setDisabled(txt_Sigkey);
					} else {
						privateKey = txt_Sigkey.getText();
						txt_SigKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(privateKey).length/2) + "/" + (n*l) + " Bytes");
				
						if (org.jcryptool.visual.wots.files.Converter._stringToByte(privateKey).length/2 == n*l) {
							setEnabled();
						}
					}
				}
			});
		}
		{
			txt_Verifkey = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Verifkey = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Verifkey.minimumHeight = 25;
			txt_Verifkey.setLayoutData(gd_txt_Verifkey);
			txt_Verifkey.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
							
					clearOutput();
					
					// Changes Public Key if modified
									
					ctr++;
					
					if (ctr%2 != 0 && disable) {
						setDisabled(txt_Verifkey);
					} else {
						publicKey = txt_Verifkey.getText();
						txt_VerKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(publicKey).length/2) + "/" + (n*instance.getPublicKeyLength()) + " Bytes");
				
						if (org.jcryptool.visual.wots.files.Converter._stringToByte(publicKey).length/2 == (n*instance.getPublicKeyLength())) {
							setEnabled();
						}
					}
				}
			});
		}
		{
			img_right = new Label(container, SWT.CENTER);
			img_right.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 5));
			img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));	
		}
		{
			txt_SigKeySize = new Label(container, SWT.NONE);
			txt_SigKeySize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			txt_SigKeySize.setText("New Label");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_VerKeySize = new Label(container, SWT.RIGHT);
			txt_VerKeySize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			txt_VerKeySize.setText("New Label");
		}
		{
			lblSignature = new Label(container, SWT.NONE);
			lblSignature.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
			lblSignature.setText("Signature");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			lblBi = new Label(container, SWT.NONE);
			lblBi.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
			lblBi.setText("B_i");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_Sig = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Sig = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Sig.minimumHeight = 25;
			txt_Sig.setLayoutData(gd_txt_Sig);
			txt_Sig.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					
					clearOutput();
					
					// Changes signature if modified
									
					ctr++;
					
					if (ctr%2 != 0 && disable) {
						setDisabled(txt_Sig);
					} else {
						signature = txt_Sig.getText();
						txt_SignatureSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(signature).length/2) + "/" + (n*l) + " Bytes");
				
						if (org.jcryptool.visual.wots.files.Converter._stringToByte(signature).length/2 == n*l) {
							setEnabled();
						}
					}
				}
			});
		}
		{
			txt_Bi = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
			gd_txt_Bi = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
			gd_txt_Bi.minimumHeight = 25;
			txt_Bi.setLayoutData(gd_txt_Bi);
			txt_Bi.setText(b);
			txt_Bi.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					
					clearOutput();
					
					// Changes Bitstring bi if modified
									
					ctr++;
					
					if (ctr%2 != 0 && disable) {
						setDisabled(txt_Bi);
					} else {
						b = txt_Bi.getText();
						instance.setBi(org.jcryptool.visual.wots.files.Converter._hexStringToByte(b));
						txt_BSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(b).length/2) + "/" + l + " Bytes");
				
						if (org.jcryptool.visual.wots.files.Converter._stringToByte(b).length/2 == l) {
							setEnabled();
						}
					}
				}
			});
		}
		{
			txt_SignatureSize = new Label(container, SWT.NONE);
			txt_SignatureSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			txt_SignatureSize.setText("New Label");
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			txt_BSize = new Label(container, SWT.RIGHT);
			txt_BSize.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			txt_BSize.setText("New Label");
		}
		{
			btn_Genkey = new Button(container, SWT.NONE);
			btn_Genkey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			btn_Genkey.setText("Generate Keys");
			btn_Genkey.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					clearOutput();
					
					// KEY GENERATION
							
					disable = false;
					
					if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {
						
						// Set Image & Output field for WOTS
						txt_Output.setText("Explanation of the WOTS Key-Generation.");
						img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Key_Generation.PNG"));
				    
					} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {
						
						// Set Image & Output field for WOTS+
						txt_Output.setText("Explanation of the WOTS+ Key-Generation.");
						img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/WOTSPlus.PNG"));
						
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
			btn_Sign.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			btn_Sign.setText("Generate Signature");
			btn_Sign.setEnabled(false);
			btn_Sign.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					clearOutput();
					
					// SIGNATURE GENERATION
							
					disable = false;
					
					if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {
					
						// Set Image & Output field for WOTS
						txt_Output.setText("Explanation of the WOTS Signature-Generation.");
						img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Signature_Generation.PNG"));
						
					} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {
						
						// Set Image & Output field for WOTS+
						txt_Output.setText("Explanation of the WOTS+ Key-Generation.");
						img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/WOTSPlus.PNG"));
						
					} else {
						
						// TODO ERROR MESSAGE
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
			btn_VerifySig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
			btn_VerifySig.setText("Verify Signature");
			btn_VerifySig.setEnabled(false);
			btn_VerifySig.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					// SIGNATURE VERIFICATION
									
					disable = false;
					
					if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {
					
						// Set Image & Output field for WOTS
						txt_Output.setText("Explanation of the WOTS Signature-Verification.");
						img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Signature_Verification.PNG"));
						
					} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {
						
						// Set Image & Output field for WOTS+
						txt_Output.setText("Explanation of the WOTS+ Signature-Verification.");
						img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/WOTSPlus.PNG"));
						
					} else {
						
						// TODO ERROR MESSAGE
						
					}
					
					// Verify Signature
					setOutputs();
					if (instance.verify()) {
						btn_VerifySig.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 0, 255, 0));
						btn_VerifySig.setText("Signature valid");
					} else {
						btn_VerifySig.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 0, 0));
						btn_VerifySig.setText("Signature rejected");
					}
					//coloured = true;
					//getOutputs();
					disable = true;
				}
			});
		}
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		{
			btn_Details = new Button(container, SWT.NONE);
			gd_btn_Details = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			gd_btn_Details.minimumWidth = 95;
			btn_Details.setLayoutData(gd_btn_Details);
			//btn_Details.setText("Hide details");
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
						// TODO error message
					}	
					//container.pack();
					//container.redraw();
					//txt_message.redraw();
					//container.reskin(SWT.ALL);
					//container.update();
					//txt_message.setLayoutData(gd_txt_message);
					//txt_message.setRedraw(true);
					//txt_message.update();
					//Point tmp = txt_message.getLocation();
					//txt_message.setLocation(1,1);
					
					//gd_txt_Bi.minimumHeight = 25;
					//txt_message.setLayoutData(SWT.FILL, SWT.FILL, true, true, 3, 1);
					
				}
			});
		}
		{
			btn_reset = new Button(container, SWT.NONE);
			btn_reset.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
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
			btn_restart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			btn_restart.setText("Restart");
			btn_restart.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					restart();
				}
			});
		}

		// Finisch Initialization
		txtToEnableOrDisable = new Text[]{txt_message,txt_Sigkey,txt_Verifkey,txt_Hash,txt_Sig,txt_Bi,txt_winternitzP};
		btnToEnableOrDisable = new Button[]{btnWots,btnWotsPlus,btn_Genkey,btn_VerifySig,btn_Sign,btnLoadMessageFrom};
		disableDetails();
		updateLengths();
		createActions();
		initializeToolBar();
		initializeMenu();
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
	 * Sets the Variables of the WOTS/WOTS+ instance to the one defined in this class
	 */
	private void setOutputs() {
		
		instance.setW(w);
		instance.setMessageDigest(hashFunction);
		instance.setPrivateKey(org.jcryptool.visual.wots.files.Converter._hexStringTo2dByte(privateKey, instance.getLength()));
		instance.setPublicKey(org.jcryptool.visual.wots.files.Converter._hexStringTo2dByte(publicKey, instance.getPublicKeyLength()));
		instance.setSignature(org.jcryptool.visual.wots.files.Converter._hexStringToByte(signature));
		instance.setMessage(org.jcryptool.visual.wots.files.Converter._hexStringToByte(messageHash));
		instance.setBi(org.jcryptool.visual.wots.files.Converter._hexStringToByte(b));
	}
	
	/**
	 * Get the calculated Values from the WOTS/WOTS+ instance and set global variables of this class
	 * Sets txt-fields to the values got from WOTS/WOTS+ instance
	 */
	private void getOutputs() {
		this.privateKey = org.jcryptool.visual.wots.files.Converter._2dByteToHex(instance.getPrivateKey());
		this.publicKey = org.jcryptool.visual.wots.files.Converter._2dByteToHex(instance.getPublicKey());
		this.signature = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.getSignature());
		this.messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.getMessageHash());
		this.b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.getBi());
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
	 * Method that disables all txt-fields and buttons that can be activated/edited except for the @param exception
	 */
	private void setDisabled(Text exception) {
		
		// Disables all Buttons and editable text-fields except for the given exception
		for (int i = 0; i < txtToEnableOrDisable.length; i++) {
			if (!txtToEnableOrDisable[i].equals(exception)) {
				txtToEnableOrDisable[i].setEnabled(false);
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
		}
		for (int i = 0; i < btnToEnableOrDisable.length; i++) {
			btnToEnableOrDisable[i].setEnabled(true);
		}
		cmb_Hash.setEnabled(true);
		ctr = 0;
	}
	
	private void reset() {
		
		clearOutput();
		
		privateKey = "";
		publicKey = "";
		signature = "";
		messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
		b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
		
		txt_Sigkey.setText("");
		txt_Sig.setText("");
		txt_Verifkey.setText("");
		txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");
		img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));
		txt_Hash.setText(messageHash);
		txt_Bi.setText(b);
		
		updateLengths();
		setEnabled();
		
		btn_Sign.setEnabled(false);
		btn_VerifySig.setEnabled(false);
	}
	
	private void updateLengths() {
		
		txt_MessageSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(message).length) + " Bytes");
		txt_SigKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(privateKey).length/2) + "/" + (n*l) + " Bytes");
		txt_VerKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(publicKey).length/2) + "/" + (n*instance.getPublicKeyLength()) + " Bytes");
		txt_HashSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._hexStringToByte(messageHash).length) + "/" + n + " Bytes");
		txt_SignatureSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(signature).length/2) + "/" + (n*l) + " Bytes");
		txt_BSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._hexStringToByte(b).length) + "/" + l + " Bytes");
	}
	
	private void clearOutput() {
		//if (coloured) {
		btn_VerifySig.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 240, 240, 240));
		btn_VerifySig.setText("Verify Signature");
		//coloured = false;
		//}
	}
	
	private void restart() {
		
		hashFunction = "SHA-256";
		instance = new WinternitzOTS(4, hashFunction);
		privateKey = "";
		publicKey = "";
		signature = "";
		w = 4;
		n = instance.getN();
		l = instance.getL();
		message = "standard message";
		messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
		b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
		details = false;
		disable = true;

		// Set Attributes for Objects
	
		btn_Details.setText("Show details");
		
		btnWots.setSelection(true);
		btnWotsPlus.setSelection(false);

		lblMessageHash.setEnabled(false);
		lblMessageHash.setVisible(false);
		
		lblBi.setEnabled(false);
		lblBi.setVisible(false);
		
		txt_winternitzP.setText("4");
		txt_Sigkey.setText("");
		clearOutput();
		txt_message.setText("standard message");
		txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");
		txt_Hash.setEnabled(false);
		txt_Hash.setVisible(false);
		txt_Hash.setText(messageHash);
		txt_Bi.setEnabled(false);
		txt_Bi.setVisible(false);
		txt_Bi.setText(b);
		txt_HashSize.setEnabled(false);
		txt_HashSize.setVisible(false);
		txt_BSize.setEnabled(false);
		txt_BSize.setVisible(false);
		
		img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));		
		cmb_Hash.select(0);
		
		txt_Sig.setText("");
		txt_Sigkey.setText("");
		txt_Verifkey.setText("");
		
		updateLengths();
		setEnabled();
		
		btn_Sign.setEnabled(false);
		btn_VerifySig.setEnabled(false);
	}
	
	private void disableDetails() {
		
		btn_Details.setText("Show Details");
		
		// make Message and Hash
		gd_txt_Hash.exclude = true;
		txt_Hash.setEnabled(false);
		txt_Hash.setVisible(false);
		gd_txt_message.horizontalSpan = 6;
		lblMessageHash.setVisible(false);
		txt_HashSize.setVisible(false);
		gd_txt_Sig.minimumHeight = 25;
		
		// make b_i and Signature
		gd_txt_Bi.exclude = true;
		txt_Bi.setEnabled(false);
		txt_Bi.setVisible(false);
		gd_txt_Sig.horizontalSpan = 6;
		lblBi.setVisible(false);
		txt_BSize.setVisible(false);
		gd_txt_Sig.minimumHeight = 50;
	}
	
	private void enableDetails() {
		
		btn_Details.setText("Hide Details");
		
		// make Message and Hash
		gd_txt_Hash.exclude = false;
		txt_Hash.setEnabled(true);
		txt_Hash.setVisible(true);
		gd_txt_message.horizontalSpan = 3;
		lblMessageHash.setVisible(true);
		txt_HashSize.setVisible(true);
				
		// make b_i and Signature
		gd_txt_Bi.exclude = false;
		txt_Bi.setEnabled(true);
		txt_Bi.setVisible(true);
		gd_txt_Sig.horizontalSpan = 3;
		lblBi.setVisible(true);
		txt_BSize.setVisible(true);
	}
}
