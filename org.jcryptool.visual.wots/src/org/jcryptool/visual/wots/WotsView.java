package org.jcryptool.visual.wots;

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
import org.eclipse.swt.layout.GridLayout;
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
//import org.eclipse.draw2d;
//import org.eclipse.swt.custom.ScrolledComposite;

/**
 * @author Hannes Sochor <sochorhannes@gmail.com>
 * @author Klaus Sch�nberger <TODO edit email>
 * @author Raphael Luger <TODO edit email>
 */
public class WotsView extends ViewPart {
	public WotsView() {
		setPartName("WOTS-Visualisierung");
	}
	public static final String ID = "asdf.view";
	
    private ScrolledComposite scrolledComposite;
    private Composite mainContent;

	// Text-Fields for message 
	private Text txt_message;
	private Text txt_MessageSize;
	// Text-Fields for Private Key
	private Text txt_Sigkey;
	private Text txt_SigKeySize;
	// Text-Fields for Public Key
	private Text txt_Verifkey;
	private Text txt_VerKeySize;
	// Text-Fields for Hash
	private Text txt_Hash;
	private Text txt_HashSize;
	// Text-Fields for Signature
	private Text txt_Sig;
	private Text txt_SignatureSize;
	// Text-Fields for Bitstring bi
	private Text txt_Bi;
	private Text txt_BSize;
	// Text-Field for Winternitz Parameter p
	private Text txt_winternitzP;
	// Text-Field for Output if Signature is valid or not
	private Text txt_true_false;
	// Buttons to switch between WOTS and WOTS+
	private Button btnWots;
	private Button btnWotsPlus;
	// Labels for txt-fields
	private Label lblMessageHash;
	private Label lblBi;
	private Label lblSignature;
	private Label lblWotsVisualization;
	private Label lblMessage;
	private Label lblWinternitzParameterw;
	private Label lblHashFunction;
	private Label lblSignatureKey;
	private Label lblVerificationKey;
	// Buttons to generate Keys, Signature, Verification, loading Files, reset and toggle Details
	private Button btn_Genkey;
	private Button btn_VerifySig;
	private Button btn_Sign;
	private Button btnLoadMessageFrom;
	private Button btn_reset;
	private Button btn_Details;
	// Output fields for rigth side
	private Label img_right;
	private Text txt_Output;
	// Dropdown List to choose Hash-Algorithm
	private Combo cmb_Hash;
	
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
	private int ctr;
	private Text[] txtToEnableOrDisable;
	private Button[] btnToEnableOrDisable; 
	
	/**
	 * @wbp.nonvisual location=214,209
	 */
	//private final JFileChooser fileChooser = new JFileChooser();
	
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(final Composite parent) {
	    scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
	    mainContent = new Composite(scrolledComposite, SWT.NONE);
        scrolledComposite.setContent(mainContent);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.setMinSize(new Point(1220, 720));
	    
	    //parent.setToolTipText("");
		//parent.setLayout(null);
		
		// Initialize Objects
		btn_Genkey = new Button(mainContent, SWT.NONE);
		btn_Genkey.setBounds(10, 674, 116, 25);
		btn_Sign = new Button(mainContent, SWT.NONE);
		btn_Sign.setBounds(132, 674, 146, 25);
		btn_VerifySig = new Button(mainContent, SWT.NONE);
		btn_VerifySig.setBounds(284, 674, 122, 25);
		btnLoadMessageFrom = new Button(mainContent, SWT.NONE);
		btnLoadMessageFrom.setBounds(10, 160, 177, 25);
		btn_reset = new Button(mainContent, SWT.NONE);
		btn_reset.setBounds(520, 674, 75, 25);
		btnWots = new Button(mainContent, SWT.RADIO);
		btnWots.setBounds(352, 186, 111, 20);
		btnWotsPlus = new Button(mainContent, SWT.RADIO);
		btnWotsPlus.setBounds(352, 217, 111, 20);
		btn_Details = new Button(mainContent, SWT.NONE);
		btn_Details.setBounds(412, 674, 102, 25);

		lblWotsVisualization = new Label(mainContent, SWT.NONE);
		lblWotsVisualization.setBounds(10, 10, 140, 21);
		lblMessage = new Label(mainContent, SWT.NONE);
		lblMessage.setBounds(10, 37, 86, 21);
		lblWinternitzParameterw = new Label(mainContent, SWT.NONE);
		lblWinternitzParameterw.setBounds(10, 200, 140, 21);
		lblHashFunction = new Label(mainContent, SWT.NONE);
		lblHashFunction.setBounds(10, 224, 96, 25);
		lblSignatureKey = new Label(mainContent, SWT.NONE);
		lblSignatureKey.setBounds(10, 262, 93, 20);
		lblVerificationKey = new Label(mainContent, SWT.NONE);
		lblVerificationKey.setBounds(352, 262, 111, 20);
		lblSignature = new Label(mainContent, SWT.NONE);
		lblSignature.setBounds(10, 463, 75, 21);
		lblMessageHash = new Label(mainContent, SWT.NONE);
		lblMessageHash.setBounds(354, 37, 109, 20);
		lblBi = new Label(mainContent, SWT.NONE);
		lblBi.setBounds(10, 389, 70, 20);
		
		txt_message = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txt_message.setBounds(9, 58, 679, 96);
		txt_winternitzP = new Text(mainContent, SWT.BORDER);
		txt_winternitzP.setBounds(177, 197, 31, 21);
		txt_Sigkey = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txt_Sigkey.setBounds(10, 283, 336, 151);
		txt_Verifkey = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txt_Verifkey.setBounds(352, 283, 336, 151);
		txt_Sig = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txt_Sig.setBounds(10, 490, 570, 107);
		txt_true_false = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.CENTER);
		txt_true_false.setBounds(586, 490, 102, 107);
		txt_Output = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		txt_Output.setBounds(723, 58, 483, 282);
		txt_Hash = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txt_Hash.setBounds(352, 58, 336, 96);
		txt_Bi = new Text(mainContent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		txt_Bi.setBounds(10, 415, 678, 56);
		txt_MessageSize = new Text(mainContent, SWT.BORDER | SWT.READ_ONLY);
		txt_MessageSize.setBounds(610, 159, 78, 26);
		txt_SigKeySize = new Text(mainContent, SWT.BORDER | SWT.READ_ONLY);
		txt_SigKeySize.setBounds(238, 440, 108, 26);
		txt_VerKeySize = new Text(mainContent, SWT.BORDER | SWT.READ_ONLY);
		txt_VerKeySize.setBounds(580, 440, 108, 26);
		txt_HashSize = new Text(mainContent, SWT.BORDER | SWT.READ_ONLY);
		txt_HashSize.setBounds(610, 159, 78, 26);
		txt_SignatureSize = new Text(mainContent, SWT.BORDER | SWT.READ_ONLY);
		txt_SignatureSize.setBounds(472, 603, 108, 26);
		txt_BSize = new Text(mainContent, SWT.BORDER | SWT.READ_ONLY);
		txt_BSize.setBounds(610, 477, 78, 26);
		
		cmb_Hash = new Combo(mainContent, SWT.NONE);
		cmb_Hash.setBounds(112, 221, 96, 28);
		img_right = new Label(mainContent, 0);
		img_right.setBounds(723, 346, 483, 322);

		// Set Attributes for Objects
		btn_Genkey.setText("Generate keys");
		btn_Sign.setText("Generate signature");
		btn_VerifySig.setText("Verify signature");
		btnLoadMessageFrom.setText("Load message from file");
		btn_reset.setText("Reset");
		btnWots.setText("WOTS");
		btnWots.setSelection(true);
		btnWotsPlus.setText("WOTS+");
		btn_Details.setText("Toggle Details");

		lblWotsVisualization.setText("WOTS Visualization");
		lblMessage.setText("Message");
		lblWinternitzParameterw.setText("Winternitz Parameter (w)");
		lblHashFunction.setText("Hash function");
		lblSignatureKey.setText("Signature key");
		lblVerificationKey.setText("Verification key");
		lblSignature.setText("Signature");
		lblMessageHash.setText("Message Hash");
		lblMessageHash.setEnabled(false);
		lblMessageHash.setVisible(false);
		lblBi.setText("Bi");
		lblBi.setEnabled(false);
		lblBi.setVisible(false);
		
		txt_winternitzP.setText("4");
		txt_Sigkey.setText("");
		txt_true_false.setEditable(false);
		txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 255, 255));
		txt_message.setText("standard message");
		txt_Output.setEditable(false);
		txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");
		txt_Hash.setEnabled(false);
		txt_Hash.setVisible(false);
		txt_Hash.setText(messageHash);
		txt_Bi.setEnabled(false);
		txt_Bi.setVisible(false);
		txt_Bi.setText(b);
		txt_MessageSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(message).length) + " Bytes");
		txt_SigKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(privateKey).length/2) + "/" + (n*l) + " B");
		txt_VerKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(publicKey).length/2) + "/" + (n*instance.getPublicKeyLength()) + " B");
		txt_HashSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._hexStringToByte(messageHash).length) + "/" + n + " B");
		txt_HashSize.setEnabled(false);
		txt_HashSize.setVisible(false);
		txt_SignatureSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(signature).length/2) + "/" + (n*l) + " B");
		txt_BSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._hexStringToByte(b).length) + "/" + l + " B");
		txt_BSize.setEnabled(false);
		txt_BSize.setVisible(false);
		
		cmb_Hash.setText("SHA-256");
		cmb_Hash.add("SHA-256");
		cmb_Hash.add("SHA-1");
		cmb_Hash.add("MD5");
		img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));
		
		

		// ########################################
		// ## ADD SELECTION LISTENER FOR BUTTONS ##
		// ########################################
		
		btn_Genkey.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// KEY GENERATION
				
				disable = false;
				txt_true_false.setText("");
				txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 255, 255));
				
				if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {
					
					// Set Image & Output field for WOTS
					txt_Output.setText("This message should explain the WOTS Key-Generation.");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Key_Generation.PNG"));
			    
				} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {
					
					// Set Image & Output field for WOTS+
					txt_Output.setText("This message should explain the WOTS+ Key-Generation.");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/WOTSPlus.PNG"));
					
				} else {
					
					// TODO ERROR MESSAGE
					
				}
				
				// Generate Keys
				setOutputs();
				instance.generateKeyPair();
				getOutputs();
				
				disable = true;
			}
		});

		btn_Sign.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// SIGNATURE GENERATION
				
				disable = false;
				txt_true_false.setText("");
				txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 255, 255));
				
				if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {
				
					// Set Image & Output field for WOTS
					txt_Output.setText("This message should explain the WOTS Signature-Generation.");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Signature_Generation.PNG"));
					
				} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {
					
					// Set Image & Output field for WOTS+
					txt_Output.setText("This message should explain the WOTS+ Key-Generation.");
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
		
		btn_VerifySig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// SIGNATURE VERIFICATION
				
				disable = false;
				
				if (btnWots.getSelection() && !btnWotsPlus.getSelection()) {
				
					// Set Image & Output field for WOTS
					txt_Output.setText("This message should explain the WOTS Signature-Verification.");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Signature_Verification.PNG"));
					
				} else if (!btnWots.getSelection() && btnWotsPlus.getSelection()) {
					
					// Set Image & Output field for WOTS+
					txt_Output.setText("This message should explain the WOTS+ Signature-Verification.");
					img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/WOTSPlus.PNG"));
					
				} else {
					
					// TODO ERROR MESSAGE
					
				}
				
				// Verify Signature
				setOutputs();
				if (instance.verify()) {
					txt_true_false.setText("Signature valid");
					txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 0, 255, 0));
				} else {
					txt_true_false.setText("Signature rejected");
					txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 0, 0));

				}
				getOutputs();
				disable = true;
			}
		});
		
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
		
		btn_reset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				// resets everything
				reset();
			}
		});
		
		btnWots.addSelectionListener( new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// Changes type to WOTS and resets what is necessary to do so
				instance = new org.jcryptool.visual.wots.WinternitzOTS(w, hashFunction);
				privateKey = "";
				publicKey = "";
				signature = "";
				txt_Sigkey.setText(".");
				txt_Sigkey.setText("");
				txt_Verifkey.setText(".");
				txt_Verifkey.setText("");
				txt_Sig.setText("");
				txt_true_false.setText("");
				txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 255, 255));
				
				setEnabled();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		
		btnWotsPlus.addSelectionListener( new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// Changes type to WOTS+ and resets what is necessary to do so
				instance = new org.jcryptool.visual.wots.WOTSPlus(w, hashFunction);
				privateKey = "";
				publicKey = "";
				signature = "";
				txt_Sigkey.setText("");
				txt_Verifkey.setText("");
				txt_Sig.setText("");
				txt_true_false.setText("");
				txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 255, 255));
				
				setEnabled();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		
		btn_Details.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (!details) {
					
					// Sets the View to a more detailed Version
					
					details = true;
					
					txt_Hash.setEnabled(true);
					txt_Hash.setVisible(true);
					
					lblMessageHash.setEnabled(true);
					lblMessageHash.setVisible(true);
					
					txt_Bi.setEnabled(true);
					txt_Bi.setVisible(true);
					
					lblBi.setEnabled(true);
					lblBi.setVisible(true);
					
					txt_HashSize.setEnabled(true);
					txt_HashSize.setVisible(true);
					
					txt_BSize.setEnabled(true);
					txt_BSize.setVisible(true);
					
					// Compress txt_fields to fit detailed view
					
					txt_message.setBounds(9, 58, 337, 96);
					txt_MessageSize.setBounds(268, 159, 78, 26);
					
					txt_Sigkey.setBounds(10, 283, 336, 75);
					txt_SigKeySize.setBounds(238, 363, 108, 26);
					
					txt_Verifkey.setBounds(352, 283, 336, 75);
					txt_VerKeySize.setBounds(580, 363, 108, 26);
					
					txt_Sig.setBounds(10, 531, 570, 107);
					txt_SignatureSize.setBounds(472, 642, 108, 26);
					txt_true_false.setBounds(586, 531, 102, 107);
					lblSignature.setBounds(10, 500, 75, 21);
					
				} else if (details) {
					
					// Hides the details shown before
					
					details = false;
					
					txt_Hash.setEnabled(false);
					txt_Hash.setVisible(false);
					
					lblMessageHash.setEnabled(false);
					lblMessageHash.setVisible(false);
					
					txt_Bi.setEnabled(false);
					txt_Bi.setVisible(false);
					
					lblBi.setEnabled(false);
					lblBi.setVisible(false);
					
					txt_HashSize.setEnabled(false);
					txt_HashSize.setVisible(false);
					
					txt_BSize.setEnabled(false);
					txt_BSize.setVisible(false);
					
					// Set sizes back to original
					
					txt_message.setBounds(9, 58, 679, 96);
					txt_MessageSize.setBounds(610, 159, 78, 26);
			
					txt_Sigkey.setBounds(10, 283, 336, 151);
					txt_SigKeySize.setBounds(238, 440, 108, 26);
					
					txt_Verifkey.setBounds(352, 283, 336, 151);
					txt_VerKeySize.setBounds(580, 440, 108, 26);
					
					txt_Sig.setBounds(10, 490, 570, 107);
					txt_SignatureSize.setBounds(472, 603, 108, 26);
					txt_true_false.setBounds(586, 490, 102, 107);
					lblSignature.setBounds(10, 463, 75, 21);
				
				} else {
					// TODO error message
				}		
			}
		});
		
		// ############################################
		// ## ADD MODIFY LISTENER FOR DROP-DOWN-LIST ##
		// ############################################
		
		cmb_Hash.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				reset();
				
				disable = false;
				
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
				
				disable = true;
				
				setOutputs();
				getOutputs();
				
				txt_message.setText(message);
				
				reset();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// #########################################
		// ## ADD MODIFY LISTENER FOR TEXT-FIELDS ##
		// #########################################
		
		txt_message.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
				// Changes hash and Bitstring bi if message is modified
				disable = false;
				message = txt_message.getText();
				messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
				b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
				txt_Hash.setText(messageHash);
				txt_Bi.setText(b);
				txt_MessageSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(message).length) + " Bytes");
				
				disable = true;
			}
		});
		
		txt_winternitzP.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				// Changes Winternitz Parameter if modified
				w = Integer.parseInt(txt_winternitzP.getText());
				instance.setW(w);
				
				// TODO Should also change Sizes of keys when edited (Now they only change if one of the other btns is activated
			}
		});
		
		txt_Sigkey.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				// Changes Private Key if modified
				
				ctr++;
				
				if (ctr%2 != 0 && disable) {
					setDisabled(txt_Sigkey);
				} else {
					privateKey = txt_Sigkey.getText();
					txt_SigKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(privateKey).length/2) + "/" + (n*l) + " B");
			
					if (org.jcryptool.visual.wots.files.Converter._stringToByte(privateKey).length/2 == n*l) {
						setEnabled();
					}
				}
			}
		});
		
		txt_Verifkey.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
								
				// Changes Public Key if modified
				
				ctr++;
				
				if (ctr%2 != 0 && disable) {
					setDisabled(txt_Verifkey);
				} else {
					publicKey = txt_Verifkey.getText();
					txt_VerKeySize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(publicKey).length/2) + "/" + (n*instance.getPublicKeyLength()) + " B");
			
					if (org.jcryptool.visual.wots.files.Converter._stringToByte(publicKey).length/2 == (n*instance.getPublicKeyLength())) {
						setEnabled();
					}
				}
				
				//System.out.println(instance.getPublicKeyLength());
				//System.out.println(n);
			}
		});
		
		txt_Sig.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				// Changes signature if modified
				
				ctr++;
				
				if (ctr%2 != 0 && disable) {
					setDisabled(txt_Sig);
				} else {
					signature = txt_Sig.getText();
					txt_SignatureSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(signature).length/2) + "/" + (n*l) + " B");
			
					if (org.jcryptool.visual.wots.files.Converter._stringToByte(signature).length/2 == n*l) {
						setEnabled();
					}
				}
			}
		});
		
		txt_Hash.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				// Changes Hash and Bitstring bi if modified
				
				ctr++;
				
				if (ctr%2 != 0 && disable) {
					setDisabled(txt_Hash);
				} else {
					messageHash = txt_Hash.getText();
					txt_HashSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(messageHash).length/2) + "/" + n + " B");

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
		
		txt_Bi.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				
				// Changes Bitstring bi if modified
				
				ctr++;
				
				if (ctr%2 != 0 && disable) {
					setDisabled(txt_Bi);
				} else {
					b = txt_Bi.getText();
					instance.setBi(org.jcryptool.visual.wots.files.Converter._hexStringToByte(b));
					txt_BSize.setText(Integer.toString(org.jcryptool.visual.wots.files.Converter._stringToByte(b).length/2) + "/" + l + " B");
			
					if (org.jcryptool.visual.wots.files.Converter._stringToByte(b).length/2 == l) {
						setEnabled();
					}
				}
			}
		});
		
		
		// Finisch Initialization
		txtToEnableOrDisable = new Text[]{txt_message,txt_Sigkey,txt_Verifkey,txt_Hash,txt_Sig,txt_Bi,txt_winternitzP};
		btnToEnableOrDisable = new Button[]{btnWots,btnWotsPlus,btn_Genkey,btn_VerifySig,btn_Sign,btnLoadMessageFrom};
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
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
		
		//System.out.println(n);
		//System.out.println(l);
		//System.out.println(w);
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
		
		ctr = 0;
	}
	
	private void reset() {
		
		if (btnWots.getSelection()) {
			instance = new WinternitzOTS(4, hashFunction);
		} else {
			instance = new WOTSPlus(4, hashFunction);
		}
		privateKey = "";
		publicKey = "";
		signature = "";
		w = 4;
		message = "standard message";
		messageHash = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.hashMessage(message));
		b = org.jcryptool.visual.wots.files.Converter._byteToHex(instance.initB());
		
		txt_message.setText("standard message");
		txt_Sigkey.setText("");
		txt_Sig.setText("");
		txt_Verifkey.setText("");
		txt_winternitzP.setText("4");
		txt_true_false.setText("");
		txt_true_false.setBackground(new Color(org.eclipse.swt.widgets.Display.getCurrent(), 255, 255, 255));
		txt_Output.setText("This is the welcome message of our plugin, please insert something which makes more sense!");
		img_right.setImage(ResourceManager.getPluginImage("org.jcryptool.visual.wots", "images/Overview2.PNG"));
		txt_Hash.setText(messageHash);
		txt_Bi.setText(b);
		
		//btnWots.setSelection(true);
		//btnWotsPlus.setSelection(false);
		
		// reset key lengths
		
		txt_SigKeySize.setText("0/" + (n*l) + " B");
		txt_VerKeySize.setText("0/" + (n*instance.getPublicKeyLength()) + " B");
		
		setEnabled();
	}
}

