//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import static java.math.BigInteger.ONE;
import static org.jcryptool.visual.library.Lib.LOW_PRIMES;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

import de.flexiprovider.core.rsa.RSAPrivateCrtKey;


/**
 * This class represents an identity in the visual.
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class Identity extends TabItem {

	private String identityName;
	private String forename;
	private String surname;
	private String organisation;
	private String region;
	private Group generalGroup;
	private Composite composite;
	private Label label;
	private Button enc_and_send;
	private Group actionGroup_1;
	private Group actionGroup_2;
	private Group actionGroup_3;
	private Group actionGroup_4;
	private GridLayout actionLayout_1;
	private GridLayout actionLayout_2;
	private GridLayout actionLayout_3;
	private GridLayout actionLayout_4;
	private Label initActions;
	private Label initActions2;
	private Text subjectInput;
	private Text clearMessage;
	private Text encryptedMessage;
	private Text decryptedMessage;
	private Text encryptedMessage_Tab2;
	private Combo messageRecipient;
	private Button sendMessage;
	private Button encryptMessage;
	private Button decryptMessage;
	private Combo recipientKeys;
	private Button receive_and_decrypt;
	private Button attackPublicKey;
	private Button keymanagement;
	private GridData group_1;
	private GridData group_2;
	private GridData group_3;
	private GridData group_4;
	private int forerunner;
	private int id;
	private Combo selectMessage;
	private Combo decryptionKeys;
	private Button deleteMessage;
	private Text pwPrivKey;
	private ExtendedTabFolder extTF;
	private TabFolder tf_keyMgmt;
	private TabItem keyMgmt_1;
	private TabItem keyMgmt_2;
	private TabItem keyMgmt_3;
	private Button radio_RSA;
	private Button radio_ExtRSA;
	private Button radio_RSA_tab2;
	private Button radio_ExtRSA_tab2;
	private Composite tab1;
	private Composite tab2;
	private Composite tab3;
	private Combo combo_rsaP;
	private Combo combo_rsaQ;
	private Combo combo_rsaE;
	private BigInteger bi_rsaP;
	private BigInteger bi_rsaQ;
	private BigInteger bi_rsaE;
	private BigInteger bi_rsaD; // D berechnen
	private BigInteger bi_rsaN;
	private BigInteger bi_rsaPhi;
	private BigInteger bi_ExtrsaP;
	private BigInteger bi_ExtrsaQ;
	private BigInteger bi_ExtrsaR;
	private BigInteger bi_ExtrsaS;
	private BigInteger bi_ExtrsaT;
	private BigInteger bi_ExtrsaE;
	private BigInteger bi_ExtrsaN;
	private BigInteger bi_ExtrsaPhi;
	private Button pickRandomE;
	private Button pickRandomExtE;
	private Vector<String> possibleEs;
	private Label errorLabel_1;
	private boolean pIsPrime;
	private boolean qIsPrime;
	private boolean rIsPrime;
	private boolean sIsPrime;
	private boolean tIsPrime;
	private boolean eIsValid;
//	private boolean init;
	private Composite rsaComposite;
	private Composite rsaExMainComposite;
	private Composite rsaExComposite1;
	private Composite rsaExComposite2;
	private Composite rsaExComposite3;
	private Combo numberOfPrimesExRSA;
	private Combo combo_ExrsaP;
	private Combo combo_ExrsaQ;
	private Combo combo_ExrsaE;
	private Combo combo_ExrsaR;
	private Combo combo_ExrsaS;
	private Combo combo_ExrsaT;
	private Label rsa_ex_S;
	private Label rsa_ex_T;
	private Text password1;
	private Text password2;
	private Text ext_password1; 
	private Text ext_password2;
	private Combo rsa_length;
	private Combo extRsa_length;
	private Combo extRsa_numberPrimes_tab2;
	private Button createKey;
	private Button createKey_Tab2;
	private Combo selectedKey_Keydata;
	private Text password_keydata;
	private Button showKeydata;
	private Table keyData;
	private TableColumn column_parameter;
	private TableColumn column_value;
	private Label txtExplain;
	private Label init_tab1;
	private String pw1;
	private String pw2;
	private String pw1_Ext;
	private String pw2_Ext;
	private int validCount;
	private IdentityManager iMgr;
	private HashMap<String, KeyStoreAlias> rec;
	private Vector<BigInteger> pubKeyParameters;
	private Label lbl_notification_tab2;
	private KeyStoreAlias publicAlias;
	private KeyStoreAlias privateAlias;
	private Label lbl_pwWrong;
	private RsaImplementation rsa_impl;
	private Label infolabel_tab2;
	private boolean isPubKey;
	private TreeMap<String, KeyStoreAlias> allKeys_keydata;
	private Label wrongPW_keydata;
	private Label lbl_enterPW;
	private boolean stopUpdateE;
	private Button attackKey;
	private Combo keyToAttack;
	private Label lbl_noKeyToAttack;
	HashMap<String, KeyStoreAlias> attackableKeys;
	private Table keyData_attacked;
	private TableColumn column_parameter_attacked;
	private TableColumn column_value_attacked;	
	private Label attack_hint;
	private Vector<BigInteger> primesE;
	
	private final String EXPLAIN_INIT = "Die Identit\u00e4ten aus Ihrem Schl\u00fcsselspeicher werden in dieser Visualisierung als Tabs (Registerkarten) angezeigt. Schon bei der Auslieferung befinden sich die Identit\u00e4ten „Alice Whitehat“ und „Bob Whitehat“ im Schl\u00fcsselspeicher und werden dehalb auch initial schon als Tabs angezeigt.\n\nJede Registerkarte stellt eine Identit\u00e4t dar. Durch den Button „Identit\u00e4ten ein-/ausblenden“ k\u00f6nnen bestehende Identit\u00e4ten als Registerkarten angezeigt oder ausgeblendet werden. Wenn eine neue Identit\u00e4t erstellt wird, wird diese erst als Registerkarte angezeigt, wenn sie durch „Identit\u00e4ten ein-/ausblenden“ ausgew\u00e4hlt wurde!\n\nZu allen Aktionen finden Sie in diesem Bereich Erl\u00e4uterungen.";
	private final String EXPLAIN_ENCRYPT = "Aktion: Nachricht verschl\u00fcsseln und senden\n\nF\u00fcr den Verschl\u00fcsselungsvorgang werden die Parameter N und e ben\u00f6tigt. Mehr Informationen zu den einzelnen Parametern finden Sie in der Registerkarte „Meine Schl\u00fcssel“  in der „Schl\u00fcsselverwaltung“.\n\n Vorgehensweise:\n\n1) Geben Sie einen optionalen Betreff und eine beliebige Nachricht ein.\n\n2) W\u00e4hlen Sie einen Empf\u00e4nger aus (zum Beispiel Bob Whitehat). Hinweis: Diese Visualisierung erlaubt nur einen Empf\u00e4nger.\n\n3) W\u00e4hlen Sie einen \u00f6ffentlichen Schl\u00fcssel des Empf\u00e4ngers aus.\n\n4) Klicken Sie auf den Button „Nachricht verschl\u00fcsseln“.\n\n5) Klicken Sie auf den Button „Nachricht senden“, um die verschl\u00fcsselte Nachricht zu verschicken und im Nachrichtenspeicher abzulegen.";
	private final String EXPLAIN_DECRYPT = "Aktion: Nachricht empfangen und entschl\u00fcsseln\n\nF\u00fcr den Entschl\u00fcsselungsvorgang werden die Parameter N und d ben\u00f6tigt. Mehr Informationen zu den einzelnen Parametern finden Sie in der Registerkarte „Meine Schl\u00fcssel“  in der „Schl\u00fcsselverwaltung“.\n\nVorgehensweise:\n\n1) W\u00e4hlen Sie eine beliebige Nachricht aus dem Nachrichtenspeicher aus.\n\n2) W\u00e4hlen Sie einen Ihrer privaten Schl\u00fcssel aus, und geben Sie das entsprechende Passwort ein.\n\n3) Klicken Sie auf den Button „Nachricht entschl\u00fcsseln“. Die Nachricht kann nur mit dem passenden privaten Schl\u00fcssel erfolgreich entschl\u00fcsselt werden.\n\n4) Klicken Sie auf den Button \"Nachricht l\u00f6schen\", um die verschl\u00fcsselte Nachricht aus dem Nachrichtenspeicher zu l\u00f6schen.";
	private final String EXPLAIN_SENDED = "Die Nachricht wurde erfolgreich in den Nachrichtenspeicher aufgenommen. Sie k\u00f6nnen nun eine neue Nachricht verschl\u00fcssen und senden oder sich die verschl\u00fcsselte Nachricht beim Empf\u00e4nger ansehen.";
	private final String EXPLAIN_DELETED = "Die Nachricht wurde nun endg\u00fcltig aus dem Nachrichtenspeicher gel\u00f6scht. Falls f\u00fcr diese Identit\u00e4ten weitere Nachrichten im Nachrichtenspeicher existieren, k\u00f6nnen sie sich diese nun ansehen. Andernfalls m\u00fcssen Sie vorher weitere Nachrichten verschl\u00fcsseln und an diese Identit\u00e4t senden.";
	private final String PW_WRONG = "Fehler: Das eingegebene Passwort f\u00fcr den selektierten privaten Schl\u00fcssel ist FALSCH!";
	private final String EXPLAIN_KEYMGMT_TAB1 = "Aktion: Schl\u00fcsselverwaltung - Neuen Schl\u00fcssel erstellen\n\nHier kann ein Schl\u00fcssel mit ausgew\u00e4hlten Parametern erstellt werden. Der RSA-Algorithmus ist in zwei Varianten implementiert:\n\n1) F\u00fcr den klassischen RSA-Algorithmus werden zwei verschiedene Primzahlen p und q ben\u00f6tigt. Diese k\u00f6nnen entweder ausgew\u00e4hlt oder eingegeben werden. Sofern p und q zul\u00e4ssig sind, kann ein Exponent e ausgesucht, eingegeben oder generiert werden.\n\n2) Beim „multi-primen RSA“ muss zuerst die Anzahl der Primzahlen (zwischen 3 und 5) festgelegt werden. Die Parameter k\u00f6nnen hier analog zum klassischen RSA angegeben werden.\n\nAm Ende der Schl\u00fcsselerzeugung muss ein Passwort f\u00fcr den privaten Schl\u00fcssel festgelegt werden.\n\nKlicken Sie auf den Button „Schl\u00fcssel erstellen“, um den neuen Schl\u00fcssel mit den gew\u00e4hlten Parametern im Schl\u00fcsselspeicher abzulegen.";
	private final String EXPLAIN_KEYMGMT_TAB2 ="Aktion: Schl\u00fcsselverwaltung - Neuen Schl\u00fcssel erstellen (erweitert)\n\nIn dieser Registerkarte k\u00f6nnen Schl\u00fcssel mit aktuell verwendeten Schl\u00fcssell\u00e4ngen erstellt werden. Es muss nur die gew\u00fcnschte Schl\u00fcssell\u00e4nge bzw. die Anzahl der Primzahlen ausgew\u00e4hlt werden.\n\nAm Ende der Schl\u00fcsselerzeugung muss ein Passwort f\u00fcr den privaten Schl\u00fcssel festgelegt werden.\n\nKlicken Sie auf den Button „Schl\u00fcssel erstellen“, um den neuen Schl\u00fcssel mit den gew\u00e4hlten Parametern im Schl\u00fcsselspeicher abzulegen.";
	private final String EXPLAIN_KEYMGMT_TAB3 = "Aktion: Schl\u00fcsselverwaltung - Meine Schl\u00fcssel\n\nEin Schl\u00fcsselpaar besteht aus einem „privaten Schl\u00fcssel“ und einem „\u00f6ffentlichen Schl\u00fcssel“:\n\na) Der private Schl\u00fcssel (N, d):\n- Der Modulus N ist sowohl Teil des \u00f6ffentlichen wie des privaten Schl\u00fcssels.\n- Der private Exponent d muss geheim gehalten werden. Er ergab sich aus der Berechnung e^(-1) modulo phi(N).\n\nb) Der \u00f6ffentliche Schl\u00fcssel (N, e):\n- Der Modulus N ist sowohl Teil des \u00f6ffentlichen wie des privaten Schl\u00fcssels.\n- Der \u00f6ffentliche Exponent e (er befindet sich \u00fcblicherweise im Bereich von 17 – 65537).\n\nBeim „multi-primen“ RSA-Verfahren k\u00f6nnen mehr als zwei Primzahlen gew\u00e4hlt werden. In diesem Plugin ist die Anzahl auf drei bis f\u00fcnf Primzahlen beschr\u00e4nkt. Theoretisch ist die Anzahl jedoch nach oben offen.";
	private final String EXPLAIN_ATTACK_PUBKEY = "Aktion: \u00d6ffentlichen Schl\u00fcssel angreifen\n\nIn dieser Registerkarte k\u00f6nnen Sie versuchen, einen \u00f6ffentlichen Schl\u00fcssel anzugreifen. Wenn der Modulus N kurz genug ist (weniger als 256 bit L\u00e4nge), wird es m\u00f6glich sein, N in \"endlicher Zeit\" zu faktorisieren. Die Faktorisierung liefert die verwendeten Primzahlen, und mit diesen kann der geheime Schl\u00fcssel d rekonstruiert werden. Somit k\u00f6nnen Sie sich als jemand anderes ausgeben und eventuell Nachrichten lesen, die nicht f\u00fcr Sie bestimmt sind oder Nachrichten im Namen von jemand anderem signieren.\n\nAchtung: Das Faktorisieren wird viel Zeit in Anspruch nehmen. Je nach Hardware und Schl\u00fcssell\u00e4nge k\u00f6nnte dieser Vorgang mehrere Jahre dauern!\n\nDa die Sicherheit von RSA auf langen Schl\u00fcsseln beruht (bei denen der Faktorisierungsvorgang mehrere Jahre dauern w\u00fcrde), wird empfohlen, zum Verschl\u00fcsseln/Signieren Schl\u00fcssel gr\u00f6ßer als 1024 bit L\u00e4nge zu verwenden.";
	private final String ENTER_TWO_PRIMES = "W\u00e4hlen Sie 2 verschiedene Primzahlen p und q, sowie einen Exponenten e:";
	private final String ENTER_THREE_PRIMES = "W\u00e4hlen Sie 3 verschiedene Primzahlen p, q, r und einen Exponenten e:";
	private final String ENTER_FOUR_PRIMES = "W\u00e4hlen Sie 4 verschiedene Primzahlen p, q, r, s und einen Exponenten e:";
	private final String ENTER_FIVE_PRIMES = "W\u00e4hlen Sie 5 verschiedene Primzahlen p, q, r, s, t und einen Exponenten e:";
	private final String TAB2_INIT = "Hier k\u00f6nnen Sie neue Schl\u00fcssel mit g\u00e4ngigen Schl\u00fcsselgr\u00f6\u00dfen erstellen:"; 
	private final String TAB3_INIT = "Hier k\u00f6nnen Sie sich Ihre privaten Schl\u00fcsselpaare und die \u00f6ffentlichen Schl\u00fcssel aller Mitspieler ansehen. \n\nHinweis: Um einen Ihrer privaten Schl\u00fcssel anzuzeigen, m\u00fcssen Sie Ihr Passwort eingeben. Die Anzeige \u00f6ffentlicher Schl\u00fcssel (public keys) erfordert keine Passwort-Eingabe.";
	private final String NO_ENCRYPTED_MESSAGES = "Achtung: Momentan sind keine verschl\u00fcsselten Nachrichten f\u00fcr diese Identit\u00e4t verf\u00fcgbar.";
	private final String NO_PRIME_P = "Achtung: 'p' ist keine Primzahl!"; 
	private final String NO_PRIME_Q = "Achtung: 'q' ist keine Primzahl!"; 
	private final String NO_PRIME_R = "Achtung: 'r' ist keine Primzahl!"; 
	private final String NO_PRIME_S = "Achtung: 's' ist keine Primzahl!"; 
	private final String NO_PRIME_T = "Achtung: 't' ist keine Primzahl!"; 
	private final String NO_VALID_E = "Achtung: 'e' ist kein passender Exponent ('e' muss zwischen 3 und 65537 liegen)!";
	private final String PRIMES_EQUAL = "Achtung: Bitte verschiedene Primzahlen ausw\u00e4hlen!";
	private final String VALUE_TOO_SMALL="Achtung: Bitte eine gr\u00f6ßere Primzahl als 2 eingeben, da ansonsten keine Werte f\u00fcr 'e' m\u00f6glich sind!";
	private final String NO_KEY_TO_ATTACK = "Achtung: Es ist noch kein angreifbarer Schl\u00fcssel (kleiner als 128 bit Schl\u00fcssell\u00e4nge) im Schl\u00fcsselspeicher.";
	
    /** a {@link VerifyListener} instance that makes sure only digits are entered. */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

    /** a {@link ModifyListener} instance that calls {@link #calcParams()} whenever a value is changed. */
    private final ModifyListener ml = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
                try {
                    bi_rsaP = new BigInteger(combo_rsaP.getText());
                    bi_rsaQ = new BigInteger(combo_rsaQ.getText());
                    checkParameter();
                } catch (NumberFormatException nfe) {
                	bi_rsaN = Constants.MINUS_ONE;
                	bi_rsaPhi =Constants.MINUS_ONE;
                }
            }
    };
	
	public Identity(ExtendedTabFolder parent, int style, String identityName, String forename, String surname, String organisation, String region, Label explain) {
		super(parent, style);
		this.extTF = parent;
		this.identityName = identityName;
		this.forename = forename;
		this.surname = surname;
		this.organisation = organisation;
		this.region = region;
		this.id = parent.getItemCount();
		this.txtExplain = explain;
		
		iMgr = IdentityManager.getInstance();
		rsa_impl = new RsaImplementation();
		stopUpdateE = false;
		
		primesE = getEPrimes();
		
		//set the text of the TabItem
		this.setText(identityName);
		forerunner = 0;	
		
		txtExplain.setText(EXPLAIN_INIT);
		
		// define the layout for the whole TabItem now
		generalGroup = new Group(parent, SWT.NONE);
		generalGroup.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		//2 columns (actions and the actionswindow)
		generalGroup.setLayout(new GridLayout(2, false));
		this.setControl(generalGroup);
		
		//Grid-Layout for all the buttons on the left side
		composite = new Composite(generalGroup, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		label = new Label(composite, SWT.CENTER);
		label.setFont(FontService.getNormalBoldFont());
		label.setText("Aktionen:");
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,false, 1, 1));
		
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		enc_and_send = new Button(composite, SWT.PUSH);
		enc_and_send.addSelectionListener(new SelectionAdapter() {
			@Override
			//Button 1
			public void widgetSelected(final SelectionEvent e) {
				txtExplain.setText(EXPLAIN_ENCRYPT);
				
				if(actionGroup_1.isDisposed()){
					createActionGroup1();
				}
				if (forerunner != 1){
					actionGroup_2.dispose();
					actionGroup_3.dispose();
					actionGroup_4.dispose();
					
					initActions.setText("Betreff der Nachricht: ");
					createSpacer(actionGroup_1);
					
					subjectInput = new Text(actionGroup_1, SWT.BORDER);
					GridData gd_subject = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_subject.heightHint = 20;
					subjectInput.setLayoutData(gd_subject);
					subjectInput.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							txtExplain.setText(EXPLAIN_ENCRYPT);
						}
					});
					createSpacer(actionGroup_1);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Nachricht:");
					GridData gd_message = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_message.heightHint = 20;
					label.setLayoutData(gd_message);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Verschl\u00fcsselte Nachricht (hexadezimale Darstellung):");
					GridData gd_enc_message = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_enc_message.heightHint = 20;
					label.setLayoutData(gd_enc_message);
					
					clearMessage = new Text(actionGroup_1, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
					clearMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					clearMessage.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							changeButtonVisibility();
							if (messageRecipient.getItemCount() == 0){
								addRecipientsToCombo();
							}
							txtExplain.setText(EXPLAIN_ENCRYPT);
						}
							
					});
					encryptedMessage = new Text(actionGroup_1, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL|SWT.READ_ONLY);
					encryptedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					
					createSpacer(actionGroup_1);
					createSpacer(actionGroup_1);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Empf\u00e4nger:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_1);
					
					messageRecipient = new Combo(actionGroup_1, SWT.READ_ONLY);
					GridData gd_recp = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_recp.heightHint = 20;
					messageRecipient.setLayoutData(gd_recp);
					messageRecipient.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibility();
							fillRecipientKeys();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					sendMessage = new Button(actionGroup_1, SWT.PUSH);
					sendMessage.setText("Nachricht senden");
					sendMessage.setEnabled(false);
					sendMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							System.out.println("nun wird die nachricht gesendet");

							int keyID = Integer.parseInt(recipientKeys.getItem(recipientKeys.getSelectionIndex()).substring(recipientKeys.getItem(recipientKeys.getSelectionIndex()).indexOf(':')+1));

//							System.out.println("keyID: "+keyID);
							
							extTF.addMessageToQueue(new SecureMessage(encryptedMessage.getText(), keyID,Identity.this.identityName, rec.get(recipientKeys.getText()), subjectInput.getText()));
							encryptedMessage.setText("");
							subjectInput.setText("");
							clearMessage.setText("");
							
							txtExplain.setText(EXPLAIN_SENDED);

							recipientKeys.removeAll();
							messageRecipient.removeAll();
							encryptMessage.setEnabled(false);
							sendMessage.setEnabled(false);
						}
					});
					sendMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Schl\u00fcssel ausw\u00e4hlen:");
					GridData gd_lbl_rk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_lbl_rk.heightHint = 20;
					label.setLayoutData(gd_lbl_rk);
					createSpacer(actionGroup_1);
					
					recipientKeys = new Combo(actionGroup_1, SWT.READ_ONLY);
					recipientKeys.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibility();
							pubKeyParameters = iMgr.getPublicKeyParameters(rec.get(recipientKeys.getText()));
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					GridData gd_rk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_rk.heightHint = 20;
					recipientKeys.setLayoutData(gd_rk);
					createSpacer(actionGroup_1);
					
					createSpacer(actionGroup_1);
					createSpacer(actionGroup_1);
					
					encryptMessage = new Button(actionGroup_1, SWT.PUSH);
					encryptMessage.setText("Nachricht verschl\u00fcsseln");
					encryptMessage.setEnabled(false);
					encryptMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							encryptedMessage.setText(rsa_impl.encrypt(clearMessage.getText(), pubKeyParameters.get(1), pubKeyParameters.get(0)));
							sendMessage.setEnabled(true);
						}
					});
					encryptMessage.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1));
					createSpacer(actionGroup_1);
					
					actionGroup_1.redraw();
					actionGroup_1.layout();
				}
				generalGroup.redraw();
				generalGroup.layout();
				forerunner = 1;
			}
		});
		enc_and_send.setText("Nachricht verschl\u00fcsseln und senden");
		enc_and_send.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		receive_and_decrypt = new Button(composite, SWT.PUSH);
		receive_and_decrypt.addSelectionListener(new SelectionAdapter() {
			@Override //Button 2
			public void widgetSelected(SelectionEvent e) {
				txtExplain.setText(EXPLAIN_DECRYPT);
				if (forerunner != 2){
					actionGroup_1.dispose();
					actionGroup_3.dispose();
					actionGroup_4.dispose();
					
					createActionGroup2();
					
					initActions2 = new Label(actionGroup_2, SWT.NONE);
					initActions2.setText("Nachricht ausw\u00e4hlen: ");
					GridData chMsg = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
					chMsg.heightHint = 20;
					initActions2.setLayoutData(chMsg);
					
					infolabel_tab2 = new Label(actionGroup_2, SWT.WRAP);
					infolabel_tab2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					infolabel_tab2.setText("");
					GridData infomess = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 2);
					infomess.heightHint = 40;
					infolabel_tab2.setLayoutData(infomess);

					selectMessage = new Combo(actionGroup_2, SWT.READ_ONLY);
					GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_combo.heightHint = 20;
					selectMessage.setLayoutData(gd_combo);
					selectMessage.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibilityTab2();
						
							SecureMessage currentMsg = extTF.getMessageWithID(Integer.parseInt(selectMessage.getText().substring(selectMessage.getText().lastIndexOf(' ')+1)));
						
							//display the message in hex
							encryptedMessage_Tab2.setText(currentMsg.getEncryptedMessage());
							
							HashMap<String,KeyStoreAlias> privKeys =iMgr.getPrivateKeys(Identity.this.identityName);
							decryptionKeys.setEnabled(true);
							decryptionKeys.setItems(privKeys.keySet().toArray(new String[privKeys.size()]));
							
							//select the "correct" key
							String hashCurrent = currentMsg.getRecipient().getHashValue();
							int count = 0;
							for (KeyStoreAlias ksa : privKeys.values()){
								if (!ksa.getHashValue().equals(hashCurrent)){
									count++;
								}else{
									decryptionKeys.select(count);
								}
							}
							
							privateAlias = privKeys.get(decryptionKeys.getText());
			                publicAlias = iMgr.getPublicForPrivateRSA(privateAlias);
			                pwPrivKey.setEnabled(true);
			                pwPrivKey.setText("");
			                decryptMessage.setEnabled(false);
			                deleteMessage.setEnabled(false);
			                decryptedMessage.setText("");
			                txtExplain.setText(EXPLAIN_DECRYPT);
			            }
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Verschl\u00fcsselte Nachricht (hexadezimale Darstellung):");
					GridData gd_message = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_message.heightHint = 20;
					label.setLayoutData(gd_message);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Entschl\u00fcsselte Nachricht:");
					GridData gd_message_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_message_2.heightHint = 20;
					label.setLayoutData(gd_message_2);
					
					encryptedMessage_Tab2= new Text(actionGroup_2, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL| SWT.READ_ONLY);
					encryptedMessage_Tab2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					encryptedMessage_Tab2.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							changeButtonVisibilityTab2();
						}
					});
					
					decryptedMessage = new Text(actionGroup_2, SWT.MULTI | SWT.WRAP| SWT.BORDER | SWT.V_SCROLL | SWT.READ_ONLY);
					decryptedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					
					createSpacer(actionGroup_2);
					createSpacer(actionGroup_2);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Schl\u00fcssel ausw\u00e4hlen:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_2);
					
					decryptionKeys = new Combo(actionGroup_2, SWT.READ_ONLY);
					decryptionKeys.setEnabled(false);
					decryptionKeys.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibilityTab2();
							pwPrivKey.setText("");
							
							HashMap<String,KeyStoreAlias> privKeys =iMgr.getPrivateKeys(Identity.this.identityName);
							privateAlias = privKeys.get(decryptionKeys.getText());
			                publicAlias = iMgr.getPublicForPrivateRSA(privateAlias);
			                pwPrivKey.setText("");
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					GridData gd_dk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_dk.heightHint = 20;
					decryptionKeys.setLayoutData(gd_dk);
					deleteMessage = new Button(actionGroup_2, SWT.PUSH);
					deleteMessage.setText("Nachricht l\u00f6schen");
					deleteMessage.setEnabled(false);
					deleteMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							extTF.deleteMessageWithID(Integer.parseInt(selectMessage.getText().substring(selectMessage.getText().lastIndexOf(' ')+1)));
							
							System.out.println("nun wird die nachricht gel\u00f6scht");
							txtExplain.setText(EXPLAIN_DELETED);
							
							selectMessage.removeAll();
							fillSelectMessage();
							
							encryptedMessage_Tab2.setText("");
							decryptionKeys.removeAll();
							pwPrivKey.setText("");
							pwPrivKey.setEnabled(false);
							decryptedMessage.setText("");
							decryptMessage.setEnabled(false);
							deleteMessage.setEnabled(false);
						}
					});
					deleteMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Passwort eingeben:");
					GridData gd_pw = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_pw.heightHint = 20;
					label.setLayoutData(gd_pw);
					createSpacer(actionGroup_2);
					
					pwPrivKey = new Text(actionGroup_2, SWT.BORDER|SWT.PASSWORD);
					pwPrivKey.setEnabled(false);
					pwPrivKey.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							changeButtonVisibilityTab2();	
						}
					});
					
					GridData gd_key = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_key.widthHint = 200;
					gd_key.heightHint = 20;
					pwPrivKey.setLayoutData(gd_key);
					
					lbl_pwWrong = new Label (actionGroup_2, SWT.WRAP);
					lbl_pwWrong.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					GridData gd_pwWrong = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
					gd_pwWrong.heightHint = 20;
					lbl_pwWrong.setLayoutData(gd_pwWrong);

					createSpacer(actionGroup_2);
					
					decryptMessage = new Button(actionGroup_2, SWT.PUSH);
					decryptMessage.setText("Nachricht entschl\u00fcsseln");
					decryptMessage.setEnabled(false);
					decryptMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							
							deleteMessage.setEnabled(true);
							
							RSAPrivateCrtKey privkey = iMgr.getPrivateKey(privateAlias, pwPrivKey.getText());
							if(privkey == null){
								//can't catch the  'java.security.UnrecoverableKeyException'
								lbl_pwWrong.setText(PW_WRONG);
							}else{
								lbl_pwWrong.setText("");
								System.out.println("nun wird die nachricht entschl\u00fcsselt");
								Vector<BigInteger> privKeyValues = iMgr.getPrivateKeyParametersRSA(privkey);

								for (BigInteger bi: privKeyValues){
									System.out.println("wert: "+bi+" bitlength: "+bi.bitLength()+" bitcount:"+bi.bitCount());
								}
								
								//we need d (position 0) and N (position 1)
								decryptedMessage.setText(rsa_impl.decrypt(encryptedMessage_Tab2.getText(), privKeyValues.get(1), privKeyValues.get(0)));
							}
												
						}
					});
					decryptMessage.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1));
					createSpacer(actionGroup_2);
					
					fillSelectMessage();
					
					generalGroup.redraw();
					generalGroup.layout();
					forerunner = 2;
				}
				
			}
		});
		receive_and_decrypt.setText("Nachricht empfangen und entschl\u00fcsseln");
		receive_and_decrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		keymanagement = new Button(composite, SWT.PUSH);
		keymanagement.addSelectionListener(new SelectionAdapter() {
			@Override //Button 3
			public void widgetSelected(SelectionEvent e) {
				txtExplain.setText(EXPLAIN_KEYMGMT_TAB1);
				if (forerunner != 3){
					actionGroup_1.dispose();
					actionGroup_2.dispose();
					actionGroup_4.dispose();
					
					bi_rsaE = null;
					bi_rsaP = null;
					bi_rsaQ = null;
					
					createActionGroup3();
					
					tf_keyMgmt = new TabFolder(actionGroup_3, SWT.NONE);
					tf_keyMgmt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					tf_keyMgmt.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							resetRSAValues();
							if (tf_keyMgmt.getSelectionIndex() == 0){
								txtExplain.setText(EXPLAIN_KEYMGMT_TAB1);
								eIsValid = false;
								password1.setEnabled(false);
								password2.setEnabled(false);
								pickRandomE.setEnabled(false);
								combo_rsaE.setEnabled(false);
								pickRandomExtE.setEnabled(false);
								combo_ExrsaE.setEnabled(false);
							}
							if (tf_keyMgmt.getSelectionIndex() == 1){
								txtExplain.setText(EXPLAIN_KEYMGMT_TAB2);
							}
							if (tf_keyMgmt.getSelectionIndex() == 2){
								txtExplain.setText(EXPLAIN_KEYMGMT_TAB3);
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					//Tab "New Key"
					keyMgmt_1 = new TabItem(tf_keyMgmt, SWT.NONE);
					keyMgmt_1.setText("Neuen Schl\u00fcssel erstellen");
					tab1 = new Composite(tf_keyMgmt, SWT.NONE);
					tab1.setLayout(new GridLayout(1, false));
					keyMgmt_1.setControl(tab1);
					
					init_tab1 = new Label(tab1, SWT.NONE);
					init_tab1.setText(ENTER_TWO_PRIMES);
					init_tab1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					errorLabel_1 = new Label(tab1, SWT.NONE);
					errorLabel_1.setFont(FontService.getNormalBoldFont());
					errorLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					errorLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					radio_RSA = new Button(tab1, SWT.RADIO);
					radio_RSA.setText("RSA");
					radio_RSA.setSelection(true);
					radio_RSA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
					radio_RSA.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility();
							init_tab1.setText(ENTER_TWO_PRIMES);
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					//composite for the labels and dropdows
					rsaComposite = new Composite(tab1, SWT.NONE);
					rsaComposite.setLayout(new GridLayout(10, false));
					
					Label rsaP = new Label(rsaComposite, SWT.NONE);
					rsaP.setText("p:");
					GridData gd_rsa_p = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_p.heightHint = 20;
					rsaP.setLayoutData(gd_rsa_p);
					
					combo_rsaP = new Combo(rsaComposite, SWT.NONE);
					fillPrimesTo(combo_rsaP);
					combo_rsaP.addModifyListener(ml);
					combo_rsaP.addVerifyListener(VL);
					combo_rsaP.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
					    	//check if p and q are primes and e is valid
							bi_rsaP = new BigInteger(combo_rsaP.getItem(combo_rsaP.getSelectionIndex()));
							combo_rsaE.setText("");
							bi_rsaE = null;
							stopUpdateE = false;
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_rsaP.addKeyListener(new KeyListener() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_rsaP.getText().length() >0){
								bi_rsaP = new BigInteger(combo_rsaP.getText());
								bi_rsaE = null;
								combo_rsaE.setText("");
								stopUpdateE = false;
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					GridData gd_combo_rsaP = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_rsaP.heightHint = 20;
					combo_rsaP.setLayoutData(gd_combo_rsaP);
					
					Label rsaQ = new Label(rsaComposite, SWT.NONE);
					rsaQ.setText("q:");
					GridData gd_rsa_q = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_q.heightHint = 20;
					rsaQ.setLayoutData(gd_rsa_q);
					
					combo_rsaQ = new Combo(rsaComposite, SWT.NONE);
					fillPrimesTo(combo_rsaQ);
					combo_rsaQ.addModifyListener(ml);
					combo_rsaQ.addVerifyListener(VL);
					GridData gd_combo_rsaQ = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_rsaQ.heightHint = 20;
					combo_rsaQ.setLayoutData(gd_combo_rsaQ);
					combo_rsaQ.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
					    	//check if p and q are primes and e is valid
							bi_rsaQ = new BigInteger(combo_rsaQ.getItem(combo_rsaQ.getSelectionIndex()));
							combo_rsaE.setText("");
							bi_rsaE = null;
							stopUpdateE = false;
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_rsaQ.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_rsaQ.getText().length() >0){
								bi_rsaQ = new BigInteger(combo_rsaQ.getText());
								combo_rsaE.setText("");
								bi_rsaE = null;
								stopUpdateE = false;
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					for(int i = 0; i < 6; i++){
						createSpacer(rsaComposite);
					}
					
					Label rsaE = new Label(rsaComposite, SWT.NONE);
					rsaE.setText("e:");
					GridData gd_rsa_e = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_e.heightHint = 20;
					rsaE.setLayoutData(gd_rsa_e);
					
					combo_rsaE = new Combo(rsaComposite, SWT.NONE);
					combo_rsaE.setEnabled(false);
					GridData gd_combo_rsaE = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_rsaE.heightHint = 20;
					gd_combo_rsaE.widthHint = 100;
					combo_rsaE.addVerifyListener(VL);
					combo_rsaE.setLayoutData(gd_combo_rsaE);
					combo_rsaE.addKeyListener(new KeyListener() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_rsaE.getText().length() >0){
								bi_rsaE = new BigInteger(combo_rsaE.getText());
								stopUpdateE = true;
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {
						}
					});
					combo_rsaE.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_rsaE = new BigInteger(combo_rsaE.getItem(combo_rsaE.getSelectionIndex()));
							stopUpdateE = true;
							eIsValid = true;
							checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					pickRandomE = new Button(rsaComposite, SWT.PUSH);
			        pickRandomE.setText("Zuf\u00e4lliges 'e' w\u00e4hlen");
			        pickRandomE.setEnabled(false);
			        pickRandomE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
			        pickRandomE.addSelectionListener(new SelectionListener() {

			            public void widgetSelected(SelectionEvent e) {
			            	bi_rsaE = new BigInteger(possibleEs.get((int)(Math.random()*possibleEs.size())));
			            	combo_rsaE.setText(bi_rsaE.toString());
			            	combo_rsaE.setText(bi_rsaE.toString());
			            	eIsValid = true;
			            	errorLabel_1.setText("");
			            	stopUpdateE = true;
			            	checkParameter();
			            }

			            public void widgetDefaultSelected(SelectionEvent e) {}
			        });
					
					for(int i = 0; i < 3; i++){
						createSpacer(rsaComposite);
					}
					
					
					radio_ExtRSA = new Button(tab1, SWT.RADIO);
					radio_ExtRSA.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility();
							init_tab1.setText(ENTER_THREE_PRIMES);
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					radio_ExtRSA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
					radio_ExtRSA.setText("Multi-primer RSA");
					
					//composite for the labels and dropdows
					rsaExMainComposite = new Composite(tab1, SWT.NONE);
					rsaExMainComposite.setLayout(new GridLayout(1, false));
					
					rsaExComposite1 = new Composite(rsaExMainComposite, SWT.NONE);
					rsaExComposite1.setLayout(new GridLayout(10, true));
					
					Label rsa_ex_Anz = new Label(rsaExComposite1, SWT.NONE);
					rsa_ex_Anz.setText("Anzahl der Primzahlen (3-5):");
					GridData gd_rsa_ex_Anz = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_ex_Anz.heightHint = 20;
					rsaP.setLayoutData(gd_rsa_ex_Anz);
					
					numberOfPrimesExRSA = new Combo(rsaExComposite1, SWT.READ_ONLY);
					GridData gd_combo_numberP = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_combo_numberP.heightHint = 20;
					gd_combo_numberP.widthHint = 50;
					numberOfPrimesExRSA.setLayoutData(gd_combo_numberP);
					numberOfPrimesExRSA.setEnabled(false);
					numberOfPrimesExRSA.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							int value = Integer.parseInt(numberOfPrimesExRSA.getItem(numberOfPrimesExRSA.getSelectionIndex()));
							switch (value){
								case 3: combo_ExrsaS.setVisible(false);
										combo_ExrsaT.setVisible(false);
										rsa_ex_S.setVisible(false);
										rsa_ex_T.setVisible(false);
										init_tab1.setText(ENTER_THREE_PRIMES);
										additionalChanges();
										break;
										
								case 4: combo_ExrsaS.setVisible(true);
										combo_ExrsaT.setVisible(false);
										rsa_ex_S.setVisible(true);
										rsa_ex_T.setVisible(false);
										init_tab1.setText(ENTER_FOUR_PRIMES);
										additionalChanges();
										break;
								
								case 5: combo_ExrsaS.setVisible(true);
										combo_ExrsaT.setVisible(true);
										rsa_ex_S.setVisible(true);
										rsa_ex_T.setVisible(true);
										init_tab1.setText(ENTER_FIVE_PRIMES);
										additionalChanges();
										break;
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					for (int i = 3; i < 6; i++){
						numberOfPrimesExRSA.add(""+i);
					}
					numberOfPrimesExRSA.select(0);
					for (int i = 0; i < 8; i++){
						createSpacer(rsaExComposite1);
					}
					
					rsaExComposite2 = new Composite(rsaExMainComposite, SWT.NONE);
					rsaExComposite2.setLayout(new GridLayout(10, false));
					
					//P
					Label rsa_ex_P = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_P.setText("p:");
					GridData gd_rsa_exP = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exP.heightHint = 20;
					rsa_ex_P.setLayoutData(gd_rsa_exP);
					combo_ExrsaP = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaP = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaP.heightHint = 20;
					gd_combo_ExrsaP.widthHint = 100;
					combo_ExrsaP.setLayoutData(gd_combo_ExrsaP);
					combo_ExrsaP.addVerifyListener(VL);
					combo_ExrsaP.setEnabled(false);
					fillPrimesTo(combo_ExrsaP);
					combo_ExrsaP.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaP = new BigInteger(combo_ExrsaP.getItem(combo_ExrsaP.getSelectionIndex()));
							resetMPInputs();
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaP.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaP.getText().length() >0){
								bi_ExtrsaP = new BigInteger(combo_ExrsaP.getText());
								resetMPInputs();
								checkParameter();
							}else{
								bi_ExtrsaE = null;
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//Q
					Label rsa_ex_Q = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_Q.setText("q:");
					GridData gd_rsa_exQ = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exQ.heightHint = 20;
					rsa_ex_Q.setLayoutData(gd_rsa_exQ);
					combo_ExrsaQ = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaQ = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaQ.heightHint = 20;
					gd_combo_ExrsaQ.widthHint = 100;
					combo_ExrsaQ.setLayoutData(gd_combo_ExrsaQ);
					combo_ExrsaQ.addVerifyListener(VL);
					combo_ExrsaQ.setEnabled(false);
					fillPrimesTo(combo_ExrsaQ);
					combo_ExrsaQ.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaQ = new BigInteger(combo_ExrsaQ.getItem(combo_ExrsaQ.getSelectionIndex()));
							resetMPInputs();
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaQ.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaQ.getText().length() >0){
								bi_ExtrsaQ = new BigInteger(combo_ExrsaQ.getText());
								resetMPInputs();
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//R
					Label rsa_ex_R = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_R.setText("r:");
					GridData gd_rsa_exR = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exR.heightHint = 20;
					rsa_ex_R.setLayoutData(gd_rsa_exR);
					combo_ExrsaR = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaR = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaR.heightHint = 20;
					gd_combo_ExrsaR.widthHint = 100;
					combo_ExrsaR.setLayoutData(gd_combo_ExrsaR);
					combo_ExrsaR.addVerifyListener(VL);
					combo_ExrsaR.setEnabled(false);
					fillPrimesTo(combo_ExrsaR);
					combo_ExrsaR.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaR = new BigInteger(combo_ExrsaR.getItem(combo_ExrsaR.getSelectionIndex()));
							resetMPInputs();
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaR.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaR.getText().length() >0){
								bi_ExtrsaR = new BigInteger(combo_ExrsaR.getText());
								resetMPInputs();
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//S
					rsa_ex_S = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_S.setText("s:");
					GridData gd_rsa_exS = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exS.heightHint = 20;
					rsa_ex_S.setLayoutData(gd_rsa_exS);
					rsa_ex_S.setVisible(false);
					combo_ExrsaS = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaS = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaS.heightHint = 20;
					gd_combo_ExrsaS.widthHint = 100;
					combo_ExrsaS.setLayoutData(gd_combo_ExrsaS);
					combo_ExrsaS.addVerifyListener(VL);
					combo_ExrsaS.setEnabled(false);
					combo_ExrsaS.setVisible(false);
					fillPrimesTo(combo_ExrsaS);
					combo_ExrsaS.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaS = new BigInteger(combo_ExrsaS.getItem(combo_ExrsaS.getSelectionIndex()));
							resetMPInputs();
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaS.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaS.getText().length() >0){
								bi_ExtrsaS = new BigInteger(combo_ExrsaS.getText());
								resetMPInputs();
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//T
					rsa_ex_T = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_T.setText("t:");
					GridData gd_rsa_exT = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exT.heightHint = 20;
					rsa_ex_T.setLayoutData(gd_rsa_exT);
					rsa_ex_T.setVisible(false);
					combo_ExrsaT = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaT = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaT.heightHint = 20;
					gd_combo_ExrsaT.widthHint = 100;
					combo_ExrsaT.setLayoutData(gd_combo_ExrsaT);
					combo_ExrsaT.addVerifyListener(VL);
					combo_ExrsaT.setEnabled(false);
					combo_ExrsaT.setVisible(false);
					fillPrimesTo(combo_ExrsaT);
					combo_ExrsaT.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaT = new BigInteger(combo_ExrsaT.getItem(combo_ExrsaT.getSelectionIndex()));
							resetMPInputs();
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaT.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaT.getText().length() >0){
								bi_ExtrsaT = new BigInteger(combo_ExrsaT.getText());
								resetMPInputs();
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//E
					Label rsa_ex_E = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_E.setText("e:");
					GridData gd_rsa_exE = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exE.heightHint = 20;
					rsa_ex_E.setLayoutData(gd_rsa_exE);
					combo_ExrsaE = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaE = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaE.heightHint = 20;
					gd_combo_ExrsaE.widthHint = 100;
					combo_ExrsaE.setLayoutData(gd_combo_ExrsaE);
					combo_ExrsaE.addVerifyListener(VL);
					combo_ExrsaE.setEnabled(false);
					combo_ExrsaE.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaE.getText().length() >0){
								bi_ExtrsaE = new BigInteger(combo_ExrsaE.getText());
								stopUpdateE = true;
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {
						}
					});
					combo_ExrsaE.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaE = new BigInteger(combo_ExrsaE.getItem(combo_ExrsaE.getSelectionIndex()));
							stopUpdateE = true;
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					pickRandomExtE = new Button(rsaExComposite2, SWT.PUSH);
					pickRandomExtE.setText("Zuf\u00e4lliges 'e' w\u00e4hlen");
					pickRandomExtE.setEnabled(false);
					pickRandomExtE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
			        pickRandomExtE.addSelectionListener(new SelectionListener() {

			            public void widgetSelected(SelectionEvent e) {
			            	bi_ExtrsaE = new BigInteger(possibleEs.get((int)(Math.random()*possibleEs.size())));
			            	combo_ExrsaE.setText(bi_ExtrsaE.toString());
			            	eIsValid = true;
			            	errorLabel_1.setText("");
			            	stopUpdateE = true;
			            	checkParameter();
			            }

			            public void widgetDefaultSelected(SelectionEvent e) {}
			        });
					
					new Label(rsaExMainComposite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));					
					
					rsaExComposite3 = new Composite(rsaExMainComposite, SWT.NONE);
					rsaExComposite3.setLayout(new GridLayout(5, true));
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					
					//enter password
					Label rsa_password = new Label(rsaExComposite3, SWT.NONE);
					rsa_password.setText("Passwort eingeben:");
					GridData gd_rsa_pw = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw.heightHint = 20;
					rsa_password.setLayoutData(gd_rsa_pw);
					password1 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw1.heightHint = 20;
					gd_combo_pw1.widthHint = 100;
					password1.setEnabled(false);
					password1.setLayoutData(gd_combo_pw1);
					password1.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw1 = password1.getText();
							checkPasswords();
						}
					});
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					//enter password again
					Label rsa_password2 = new Label(rsaExComposite3, SWT.NONE);
					rsa_password2.setText("Passwort wiederholen:");
					GridData gd_rsa_pw2 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw2.heightHint = 20;
					rsa_password2.setLayoutData(gd_rsa_pw2);
					password2 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw2.heightHint = 20;
					gd_combo_pw2.widthHint = 100;
					password2.setEnabled(false);
					password2.setLayoutData(gd_combo_pw2);
					password2.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw2 = password2.getText();
							checkPasswords();
						}
					});
					
					for (int i = 0; i < 4; i++){
						createSpacer(rsaExComposite3);
					}
					
					createKey = new Button(rsaExComposite3, SWT.PUSH);
					createKey.setText("Schl\u00fcssel erstellen");
					createKey.setEnabled(false);
					GridData gd_createKey = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_createKey.heightHint = 20;
					gd_createKey.widthHint = 100;
					createKey.setLayoutData(gd_createKey);
					createKey.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (radio_RSA.getSelection()){

								System.out.println("nun wird ein klassischer RSA key erstellt. Parameter: P"+bi_rsaP+" Q: "+bi_rsaQ+" E:"+bi_rsaE+" pw: "+pw1);
								
								bi_rsaD = bi_rsaE.modInverse(bi_rsaPhi);
								
								errorLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
								if (bi_rsaD.toString().length() < 40){
									errorLabel_1.setText("Klassischer RSA-Schl\u00fcssel wurde erstellt. Der geheime Exponent d ist: "+bi_rsaD+".");
								}else{
									errorLabel_1.setText("RSA-Schl\u00fcssel wurde erstellt. Schl\u00fcsseldetails aufgrund der Gr\u00f6\u00dfe bitte im Tab 'Meine Schl\u00fcssel' betrachten");
								}
								
								iMgr.saveKeyToKeystore(Identity.this.identityName, password1.getText(), bi_rsaN, bi_rsaP, bi_rsaQ, bi_rsaE, bi_rsaD);
								
								password1.setText("");
								password1.setEnabled(false);
								password2.setText("");
								password2.setEnabled(false);
								fillPrimesTo(combo_rsaP);
								fillPrimesTo(combo_rsaQ);
								combo_rsaE.removeAll();
								pickRandomE.setEnabled(false);
								combo_rsaE.setEnabled(false);
								eIsValid = false;
								resetRSAValues();
								createKey.setEnabled(false);
							}else{
								System.out.println("nun wird ein mp-RSA key erstellt. Parameter: P"+bi_ExtrsaP+" Q: "+bi_ExtrsaQ+" R: "+bi_ExtrsaR+"E:"+bi_ExtrsaE+" pw: "+pw1);
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					//Tab "New Key (extended)"
					keyMgmt_2 = new TabItem(tf_keyMgmt, SWT.NONE);
					keyMgmt_2.setText("Neuen Schl\u00fcssel erstellen (erweitert)");
					tab2 = new Composite(tf_keyMgmt, SWT.NONE);
					tab2.setLayout(new GridLayout(1, false));
					keyMgmt_2.setControl(tab2);
					
					Label lbl_init_tab2 = new Label(tab2, SWT.NONE);
					lbl_init_tab2.setText(TAB2_INIT);
					GridData gd_init_tab2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_init_tab2.heightHint = 20;
					lbl_init_tab2.setLayoutData(gd_init_tab2);
					
					lbl_notification_tab2 = new Label(tab2, SWT.NONE);
					GridData gd_noti_tab2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_noti_tab2.heightHint = 20;
					lbl_notification_tab2.setLayoutData(gd_noti_tab2);
					
					
					radio_RSA_tab2 = new Button(tab2, SWT.RADIO);
					radio_RSA_tab2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
					radio_RSA_tab2.setText("RSA");
					radio_RSA_tab2.setSelection(true);
					radio_RSA_tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility_Tab2();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					Composite rsaComp = new Composite(tab2, SWT.NONE);
					rsaComp.setLayout(new GridLayout(2, false));
					
					
					Label rsa_label = new Label(rsaComp, SWT.NONE);
					rsa_label.setText("Schl\u00fcssell\u00e4nge w\u00e4hlen:");
					GridData gd_rsa_lab = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_lab.heightHint = 20;
					rsa_label.setLayoutData(gd_rsa_lab);
					
					rsa_length = new Combo(rsaComp, SWT.READ_ONLY);
					rsa_length.add("1024");
					rsa_length.add("2048");
					rsa_length.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							ext_password1.setText("");
							ext_password2.setText("");	
							lbl_notification_tab2.setText("");
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					GridData gd_rsa_length_comb = new GridData(SWT.LEFT, SWT.LEFT, true, true, 1, 1);
					gd_rsa_length_comb.heightHint = 20;
					rsa_length.select(0);
					rsa_length.setLayoutData(gd_rsa_length_comb);
					
					for (int i = 0; i < 4; i++){
						createSpacer(rsaComp);
					}
					
					
					radio_ExtRSA_tab2 = new Button(tab2, SWT.RADIO);
					radio_ExtRSA_tab2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
					radio_ExtRSA_tab2.setText("Multi-primer RSA");
					radio_ExtRSA_tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility_Tab2();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					Composite rsaExtComp = new Composite(tab2, SWT.NONE);
					rsaExtComp.setLayout(new GridLayout(2, false));
					
					
					Label rsa_extLabel = new Label(rsaExtComp, SWT.NONE);
					rsa_extLabel.setText("Anzahl der Primzahlen (3-5):");
					GridData gd_rsa_extlab = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_extlab.heightHint = 20;
					rsa_extLabel.setLayoutData(gd_rsa_extlab);
					
					extRsa_numberPrimes_tab2 = new Combo(rsaExtComp, SWT.READ_ONLY);
					GridData gd_combo_ExtnumberP = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_combo_ExtnumberP.heightHint = 20;
					gd_combo_ExtnumberP.widthHint = 50;
					extRsa_numberPrimes_tab2.setLayoutData(gd_combo_ExtnumberP);
					extRsa_numberPrimes_tab2.setEnabled(false);
					for (int i = 3; i < 6; i++){
						extRsa_numberPrimes_tab2.add(""+i);
					}
					extRsa_numberPrimes_tab2.select(0);
					extRsa_numberPrimes_tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							ext_password1.setText("");
							ext_password2.setText("");
							lbl_notification_tab2.setText("");
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					Label rsa_extlength = new Label(rsaExtComp, SWT.NONE);
					rsa_extlength.setText("Schl\u00fcssell\u00e4nge w\u00e4hlen:");
					GridData gd_rsa_extlength = new GridData(SWT.LEFT, SWT.LEFT, true, true, 1, 1);
					gd_rsa_extlength.heightHint = 20;
					rsa_extlength.setLayoutData(gd_rsa_extlength);
					
					extRsa_length = new Combo(rsaExtComp, SWT.READ_ONLY);
					extRsa_length.add("1024");
					extRsa_length.add("2048");
					extRsa_length.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							ext_password1.setText("");
							ext_password2.setText("");	
							lbl_notification_tab2.setText("");
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});

					GridData gd_Extrsa_length_comb = new GridData(SWT.LEFT, SWT.LEFT, true, true, 1, 1);
					gd_Extrsa_length_comb.heightHint = 20;
					extRsa_length.select(0);
					extRsa_length.setLayoutData(gd_Extrsa_length_comb);
					extRsa_length.setEnabled(false);
					
					new Label(tab2, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
					new Label(tab2, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
					
					new Label(tab2, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
					
					
					rsaExComposite3 = new Composite(tab2, SWT.NONE);
					rsaExComposite3.setLayout(new GridLayout(5, true));
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					
					//enter password
					Label rsa_password_2 = new Label(rsaExComposite3, SWT.NONE);
					rsa_password_2.setText("Passwort eingeben:");
					GridData gd_rsa_pw_2 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw_2.heightHint = 20;
					rsa_password_2.setLayoutData(gd_rsa_pw_2);
					ext_password1 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw1_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw1_2.heightHint = 20;
					gd_combo_pw1_2.widthHint = 100;
					ext_password1.setLayoutData(gd_combo_pw1_2);
					ext_password1.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw1_Ext = ext_password1.getText();
							checkPasswords();
						}
					});
					
					
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					//enter password again
					Label rsa_password2_2 = new Label(rsaExComposite3, SWT.NONE);
					rsa_password2_2.setText("Passwort wiederholen:");
					GridData gd_rsa_pw2_2 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw2_2.heightHint = 20;
					rsa_password2_2.setLayoutData(gd_rsa_pw2_2);
					ext_password2 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw2_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw2_2.heightHint = 20;
					gd_combo_pw2_2.widthHint = 100;
					ext_password2.setLayoutData(gd_combo_pw2_2);
					ext_password2.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw2_Ext = ext_password2.getText();
							checkPasswords();
						}
					});
					
					for (int i = 0; i < 4; i++){
						createSpacer(rsaExComposite3);
					}
					
					createKey_Tab2 = new Button(rsaExComposite3, SWT.PUSH);
					createKey_Tab2.setText("Schl\u00fcssel erstellen");
					GridData gd_createKey_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_createKey_2.heightHint = 20;
					gd_createKey_2.widthHint = 100;
					createKey_Tab2.setLayoutData(gd_createKey_2);
					createKey_Tab2.setEnabled(false);
					createKey_Tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (radio_ExtRSA_tab2.getSelection()){
								System.out.println("Nun wird gro\u00dfer multiprimer RSA-key erstellt");	
							}else{
								iMgr.createIdentity(Identity.this.identityName, "RSA", ext_password1.getText(), Integer.parseInt(rsa_length.getItem(rsa_length.getSelectionIndex())));
								System.out.println("gro\u00dfen klassischen RSA erstellen: name:"+Identity.this.identityName+" pw: "+ext_password1.getText()+" l\u00e4nge: "+ rsa_length.getItem(rsa_length.getSelectionIndex()));	
								lbl_notification_tab2.setFont(FontService.getNormalBoldFont());
								lbl_notification_tab2.setText("Klassischer RSA-Key erfolgreich erstellt. Schl\u00fcsseldetails aufgrund der Gr\u00f6\u00dfe bitte im Tab 'Meine Schl\u00fcssel' betrachten.");
								
								createKey_Tab2.setEnabled(false);
								ext_password1.setText("");
								ext_password2.setText("");
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					//Tab "My Keys"
					keyMgmt_3 = new TabItem(tf_keyMgmt, SWT.NONE);
					keyMgmt_3.setText("Meine Schl\u00fcssel");
					tab3 = new Composite(tf_keyMgmt, SWT.NONE);;
					tab3.setLayout(new GridLayout(1, false));
					keyMgmt_3.setControl(tab3);
					
					Label lbl_init_tab3 = new Label(tab3, SWT.WRAP);
					lbl_init_tab3.setText(TAB3_INIT);
					lbl_init_tab3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
					
					
					Composite myKeyData = new Composite(tab3, SWT.NONE);
					myKeyData.setLayout(new GridLayout(4, false));
					myKeyData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					//combo "select key"
					Label lbl_selectKey = new Label(myKeyData, SWT.WRAP);
					lbl_selectKey.setText("Schl\u00fcssel ausw\u00e4hlen:");
					GridData gd_lbl_selKey = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_lbl_selKey.heightHint = 20;
					lbl_selectKey.setLayoutData(gd_lbl_selKey);
					selectedKey_Keydata = new Combo(myKeyData, SWT.READ_ONLY);
					GridData gd_selKey = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_selKey.heightHint = 20;
					gd_selKey.widthHint = 250;
					selectedKey_Keydata.setLayoutData(gd_selKey);
					allKeys_keydata = iMgr.loadAllKeysForIdentityAndOtherPublics(Identity.this.identityName);
					
					selectedKey_Keydata.setItems(allKeys_keydata.keySet().toArray(new String[allKeys_keydata.size()]));
					
					selectedKey_Keydata.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							keyData.removeAll();
							
							if (selectedKey_Keydata.getItem(selectedKey_Keydata.getSelectionIndex()).contains("PublicKey")){
								showKeydata.setEnabled(true);
								password_keydata.setVisible(false);
								lbl_enterPW.setVisible(false);
								password_keydata.setText("");
								isPubKey = true;
							}else{
								showKeydata.setEnabled(false);
								password_keydata.setVisible(true);
								lbl_enterPW.setVisible(true);
								password_keydata.setText("");
								isPubKey = false;
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					//button "show keydata"
					showKeydata = new Button(myKeyData, SWT.PUSH);
					GridData gd_showKeydata = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_showKeydata.heightHint = 20;
					gd_showKeydata.widthHint = 100;
					showKeydata.setLayoutData(gd_showKeydata);
					showKeydata.setText("Schl\u00fcsseldaten anzeigen");
					showKeydata.setEnabled(false);
					showKeydata.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							keyData.removeAll();
							Vector<String> descriptions = null;
							Vector<String> values = null;
							if (isPubKey){
								descriptions = new Vector<String>(Arrays.asList("Algorithmus", "Format", "e", "N"));
								values = iMgr.getAllRSAPubKeyParameters(allKeys_keydata.get(selectedKey_Keydata.getText()));
							}else{
								String keyDescription = selectedKey_Keydata.getText().substring(selectedKey_Keydata.getText().lastIndexOf(' ')+1);
								if (keyDescription.startsWith("RSA")){
									descriptions = new Vector<String>(Arrays.asList("Algorithmus", "Format", "p","q","d","e","N"));
									values = iMgr.getAllRSAPrivKeyParameters(allKeys_keydata.get(selectedKey_Keydata.getText()), password_keydata.getText());
								}else{
									//mp-rsa key
									values = iMgr.getAllMpRSAPrivKeyParameters(allKeys_keydata.get(selectedKey_Keydata.getText()), password_keydata.getText());
									System.out.println("size: "+values.size());
									String str_otherPrimeinfo = values.get(values.size()-4);
									String []split_opi = str_otherPrimeinfo.split(" ");
									switch (split_opi.length){
									case 1: descriptions = new Vector<String>(Arrays.asList("Algorithmus", "Format", "p","q","r","d","e","N"));
											break;
											
									case 2: descriptions = new Vector<String>(Arrays.asList("Algorithmus", "Format", "p","q","r","s","d","e","N"));
											break;
											
									case 3: descriptions = new Vector<String>(Arrays.asList("Algorithmus", "Format", "p","q","r","s","t","d","e","N"));
											break;
									}
									//TODO fix
								}
							}
							if (values.size() == 0){
								wrongPW_keydata.setText(PW_WRONG);
							}else{
								wrongPW_keydata.setText("");
								for (int i = 0; i < descriptions.size(); i++){
									TableItem ti = new TableItem(keyData, SWT.NONE);
									ti.setText(new String[]{descriptions.get(i), values.get(i)});
									
								}
//								System.out.println("resize to: "+values.get(values.size()-1).length()*8);
								column_value.setWidth((values.get(values.size()-1).length()*8)+100);
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					createSpacer(myKeyData);
					
					
					//textfield "enter password"
					lbl_enterPW = new Label(myKeyData, SWT.NONE);
					lbl_enterPW.setText("Passwort eingeben:");
					GridData gd_enterPW = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_enterPW.heightHint = 20;
					lbl_enterPW.setLayoutData(gd_enterPW);
					lbl_enterPW.setVisible(false);
					password_keydata = new Text(myKeyData, SWT.PASSWORD|SWT.BORDER);
					GridData gd_pw = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_pw.heightHint = 20;
					gd_pw.widthHint = 200;
					password_keydata.setLayoutData(gd_pw);
					password_keydata.setVisible(false);
					password_keydata.addKeyListener(new KeyListener() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							showKeydata.setEnabled(true);
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					createSpacer(myKeyData);
					createSpacer(myKeyData);
					
					wrongPW_keydata = new Label(myKeyData, SWT.WRAP);
					GridData gd_wrongPW_kd = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1);
					gd_wrongPW_kd.heightHint = 30;
					gd_wrongPW_kd.widthHint = 200;
					wrongPW_keydata.setLayoutData(gd_wrongPW_kd);
					wrongPW_keydata.setFont(FontService.getNormalBoldFont());
					wrongPW_keydata.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					
					if (selectedKey_Keydata.getItemCount() == 0){
						wrongPW_keydata.setText("Achtung: Noch keine Schl\u00fcssel f\u00fcr diese Identit\u00e4t vorhanden.");
						selectedKey_Keydata.setEnabled(false);
					}
					
					new Label(tab3, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					keyData = new Table(tab3, SWT.BORDER|SWT.FULL_SELECTION);
					GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_table.heightHint = 130;
					keyData.setLayoutData(gd_table);
					keyData.setHeaderVisible(true);
					keyData.setLinesVisible(true);
					
					
					column_parameter = new TableColumn(keyData, SWT.NONE);
					column_parameter.setWidth(100);
					column_parameter.setText("Parameter");
					
					column_value = new TableColumn(keyData, SWT.NONE);
					column_value.setWidth(500);
					column_value.setText("Wert");
					

					generalGroup.redraw();
					generalGroup.layout();
					forerunner = 3;
				}
			}
		});
		keymanagement.setText("Schl\u00fcsselverwaltung");
		keymanagement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		attackPublicKey = new Button(composite, SWT.PUSH);
		attackPublicKey.addSelectionListener(new SelectionAdapter() {
			@Override //Button 4
			public void widgetSelected(SelectionEvent e) {
				txtExplain.setText(EXPLAIN_ATTACK_PUBKEY);
				if (forerunner != 4){
					actionGroup_1.dispose();
					actionGroup_2.dispose();
					actionGroup_3.dispose();
					
					createActionGroup4();
					
					lbl_noKeyToAttack = new Label(actionGroup_4, SWT.NONE);
					lbl_noKeyToAttack.setFont(FontService.getNormalBoldFont());
					lbl_noKeyToAttack.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					GridData gd_lbl_noKey = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
					gd_lbl_noKey.heightHint = 20;
					lbl_noKeyToAttack.setLayoutData(gd_lbl_noKey);				
					
					Label l = new Label(actionGroup_4, SWT.NONE);
					l.setText("W\u00e4hlen Sie einen \u00f6ffentlichen Schl\u00fcssel: ");
					GridData gd_lbl_attack = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_lbl_attack.heightHint = 20;
					l.setLayoutData(gd_lbl_attack);
					
					for(int i = 0; i < 3; i++){
						createSpacer(actionGroup_4);
					}
					
					keyToAttack = new Combo(actionGroup_4, SWT.READ_ONLY);
					GridData combo_attack = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					combo_attack.heightHint = 20;
					combo_attack.widthHint = 250;
					keyToAttack.setLayoutData(combo_attack);
					attackableKeys = iMgr.getAttackablePublicKeys(Identity.this.identityName);
					if (attackableKeys.size() == 0){
						keyToAttack.setEnabled(false);
						lbl_noKeyToAttack.setText(NO_KEY_TO_ATTACK);
					}
					keyToAttack.setItems(attackableKeys.keySet().toArray(new String[attackableKeys.size()]));
					keyToAttack.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (keyToAttack.getSelectionIndex() != -1 && attackableKeys.size()>0){
								attackKey.setEnabled(true);
								Vector<BigInteger> actualKey = iMgr.getPublicKeyParameters(attackableKeys.get(keyToAttack.getText()));
								
								String name = keyToAttack.getText().substring(0,keyToAttack.getText().indexOf('-')-1);
								attack_hint.setText("Mit Klick auf \"Schl\u00fcssel attackieren\" wird versucht, den Exponten N vom "+name+" zu faktorisieren:\nBitl\u00e4nge von N:"+actualKey.get(0).bitLength()+"bit \nN: "+actualKey.get(0));		
								keyData_attacked.setVisible(false);
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					attackKey = new Button(actionGroup_4, SWT.PUSH);
					attackKey.setText("Schl\u00fcssel attackieren");
					attackKey.setEnabled(false);
					GridData btn_attack = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					btn_attack.heightHint = 20;
					btn_attack.widthHint = 200;
					attackKey.setLayoutData(btn_attack);
					attackKey.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							BigInteger publicN = null;
							BigInteger publicE = null;
							Vector<String> pubKeyParameter = iMgr.getAllRSAPubKeyParameters(attackableKeys.get(keyToAttack.getText()));
							if(pubKeyParameter.size()>0){
								publicN = new BigInteger(pubKeyParameter.get(pubKeyParameter.size()-1));
								publicE = new BigInteger(pubKeyParameter.get(pubKeyParameter.size()-2));
								factorizePubKey(publicN,publicE);
								
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					for(int i = 0; i < 6; i++){
						createSpacer(actionGroup_4);
					}

					attack_hint = new Label(actionGroup_4, SWT.WRAP);
					GridData at_hint = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 2);
					at_hint.heightHint = 110;
					attack_hint.setLayoutData(at_hint);
					
					keyData_attacked = new Table(actionGroup_4, SWT.BORDER|SWT.FULL_SELECTION);
					GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1);
					gd_table.heightHint = 130;
					keyData_attacked.setLayoutData(gd_table);
					keyData_attacked.setHeaderVisible(true);
					keyData_attacked.setLinesVisible(true);
					keyData_attacked.setVisible(false);
					
					
					column_parameter_attacked = new TableColumn(keyData_attacked, SWT.NONE);
					column_parameter_attacked.setWidth(100);
					column_parameter_attacked.setText("Parameter");
					
					column_parameter_attacked = new TableColumn(keyData_attacked, SWT.NONE);
					column_parameter_attacked.setWidth(500);
					column_parameter_attacked.setText("Wert");

					
					generalGroup.redraw();
					generalGroup.layout();
					forerunner = 4;
				}
			}
		});
		attackPublicKey.setText("\u00d6ffentlichen Schl\u00fcssel angreifen");
		attackPublicKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//add spacer to the buttons on the left (for further actions :) )
		for (int i = 0; i < 15; i++){
			createSpacer(composite);
		}
		
		createActionGroup1();
		
		createActionGroup2();
		group_2.exclude = true;
		actionGroup_2.setVisible(false);
		
		createActionGroup3();
		group_3.exclude = true;
		actionGroup_3.setVisible(false);
		
		createActionGroup4();
		actionGroup_4.setVisible(false);
		group_4.exclude = true;
	}
	
	private void factorizePubKey(final BigInteger n, final BigInteger e){
		new Thread(){
			
			private BigDecimal getNextPrime(final BigDecimal start){
				BigDecimal prime = start.add(BigDecimal.ONE);
				boolean isPrime = false;
				while (!isPrime){
					if (!Lib.isPrime(prime.toBigInteger())){
						prime = prime.add(BigDecimal.ONE);
					}else{
						isPrime = true;
					}
				}
				return prime;
			}
			@Override
			public void run(){
				final long start = System.currentTimeMillis();
				System.out.println("factorize n: "+n+"("+n.bitLength()+")bit");
				boolean finish = false;
				final Vector<BigInteger> divisors = new Vector<BigInteger>();
				BigDecimal divisor = new BigDecimal(3);
				BigDecimal decN = new BigDecimal(n);
				BigInteger result = BigInteger.ONE;
				boolean check = false;
				
				while (!finish){
					System.out.println("aktuelle primzahl: "+divisor);
					BigDecimal[]erg = decN.divideAndRemainder(divisor);
					if (erg[1].intValue() == 0){
						divisors.add(erg[0].toBigInteger());
						divisor = getNextPrime(divisor);
						check = true;
					}else{
						divisor = getNextPrime(divisor);
						check = false;
					}
	
					if (divisors.size() > 1 && check){
						for (int i = 0; i < divisors.size(); i++){
							result = result.multiply(divisors.get(i));
						}
						if (result.equals(decN.toBigInteger())){
							System.out.println("factorized!");
							finish = true;
						}
						check = false;
					}
	
					if (finish){
						new UIJob("Display results") { 
							@Override
							public IStatus runInUIThread(IProgressMonitor monitor) {
								System.out.println("Dauer in ms: " + (System.currentTimeMillis() - start));
								keyData_attacked.setVisible(true);
								keyData_attacked.removeAll();
								attack_hint.setText(attack_hint.getText()+"\nDie Faktorisierung war erfolgreich. Folgende Werte konnten rekonstruiert werden:");
								keyData_attacked.setVisible(true);
								TableItem ti_p;
								TableItem ti_q;
								TableItem ti_r;
								TableItem ti_s;
								TableItem ti_t;
								BigInteger phi = BigInteger.ONE;
								
								switch (divisors.size()){
								case 2: ti_p = new TableItem(keyData_attacked, SWT.NONE);
										ti_p.setText(new String[]{"p",""+divisors.get(0)});
										ti_q = new TableItem(keyData_attacked, SWT.NONE);
										ti_q.setText(new String[]{"q",""+divisors.get(1)});
										phi = divisors.get(0).subtract(BigInteger.ONE).multiply(divisors.get(1).subtract(BigInteger.ONE));
										break;
										
								case 3: ti_p = new TableItem(keyData_attacked, SWT.NONE);
										ti_p.setText(new String[]{"p",""+divisors.get(0)});
										ti_q = new TableItem(keyData_attacked, SWT.NONE);
										ti_q.setText(new String[]{"q",""+divisors.get(1)});
										ti_r = new TableItem(keyData_attacked, SWT.NONE);
										ti_r.setText(new String[]{"r",""+divisors.get(2)});
										phi = divisors.get(0).subtract(BigInteger.ONE).multiply(divisors.get(1).subtract(BigInteger.ONE)).multiply(divisors.get(2).subtract(BigInteger.ONE));
										break;
								
								case 4: ti_p = new TableItem(keyData_attacked, SWT.NONE);
										ti_p.setText(new String[]{"p",""+divisors.get(0)});
										ti_q = new TableItem(keyData_attacked, SWT.NONE);
										ti_q.setText(new String[]{"q",""+divisors.get(1)});
										ti_r = new TableItem(keyData_attacked, SWT.NONE);
										ti_r.setText(new String[]{"r",""+divisors.get(2)});
										ti_s = new TableItem(keyData_attacked, SWT.NONE);
										ti_s.setText(new String[]{"s",""+divisors.get(3)});
										phi = divisors.get(0).subtract(BigInteger.ONE).multiply(divisors.get(1).subtract(BigInteger.ONE)).multiply(divisors.get(2).subtract(BigInteger.ONE)).multiply(divisors.get(3).subtract(BigInteger.ONE));
										break;
										
								case 5: ti_p = new TableItem(keyData_attacked, SWT.NONE);
										ti_p.setText(new String[]{"p",""+divisors.get(0)});
										ti_q = new TableItem(keyData_attacked, SWT.NONE);
										ti_q.setText(new String[]{"q",""+divisors.get(1)});
										ti_r = new TableItem(keyData_attacked, SWT.NONE);
										ti_r.setText(new String[]{"r",""+divisors.get(2)});
										ti_s = new TableItem(keyData_attacked, SWT.NONE);
										ti_s.setText(new String[]{"s",""+divisors.get(3)});
										ti_t = new TableItem(keyData_attacked, SWT.NONE);
										ti_t.setText(new String[]{"t",""+divisors.get(4)});
										phi = divisors.get(0).subtract(BigInteger.ONE).multiply(divisors.get(1).subtract(BigInteger.ONE)).multiply(divisors.get(2).subtract(BigInteger.ONE)).multiply(divisors.get(3).subtract(BigInteger.ONE)).multiply(divisors.get(4).subtract(BigInteger.ONE));
										break;
								}
								
								TableItem ti_e = new TableItem(keyData_attacked, SWT.NONE);
								ti_e.setText(new String[]{"e",""+e});
								
								TableItem ti_d = new TableItem(keyData_attacked, SWT.NONE);
								ti_d.setText(new String[]{"d",""+e.modInverse(phi)});	
								return Status.OK_STATUS;
							} 
						}.schedule();
						
					}
				}
			}
		}.start();
	}
	
	private void additionalChanges(){
		password1.setText("");
		password2.setText("");
		password1.setEnabled(false);
		password2.setEnabled(false);
		createKey.setEnabled(false);
		combo_ExrsaE.removeAll();
		pickRandomExtE.setEnabled(false);
		combo_ExrsaE.setEnabled(false);
		resetRSAValues();
	}
	
	private void fillRecipientKeys(){
//		Vector<String> rec = iMgr.getPublicKeys(this.identityName);
		if(!messageRecipient.getText().equals("")){
			rec = iMgr.getPublicKeys(messageRecipient.getText());
			recipientKeys.setItems(rec.keySet().toArray(new String[rec.size()]));
			recipientKeys.select(0);
			pubKeyParameters = iMgr.getPublicKeyParameters(rec.get(recipientKeys.getText()));
		}
	}
	
	private void fillSelectMessage(){
		for (SecureMessage sec :extTF.getMessageQueue()){
			if (sec.getRecipient().getContactName().equals(identityName)){
				String subject = "";
				if (sec.getSubject().equals("")){
					subject = "kein Betreff";
				}else{
					subject = sec.getSubject();
				}
				String message = ""+subject+" - "+sec.getRecipient().getContactName()+" - "+sec.getMessageID();
				selectMessage.add(message);
				System.out.println(message);
			}
		}
		if (selectMessage.getItemCount() == 0){
			infolabel_tab2.setText(NO_ENCRYPTED_MESSAGES);
			selectMessage.setEnabled(false);
			pwPrivKey.setEnabled(false);
			decryptionKeys.setEnabled(false);
			encryptedMessage_Tab2.setEnabled(false);
			decryptedMessage.setEnabled(false);
		}
	}
	
	private void changeButtonVisibilityTab2(){
		if ((selectMessage.getSelectionIndex() != -1) &&(encryptedMessage_Tab2.getText().length() >1 ) && (decryptionKeys.getSelectionIndex() != -1) && (pwPrivKey.getText().length() > 0)){
			decryptMessage.setEnabled(true);
		}
	}
	
	
	private void changeButtonVisibility(){
		if ((clearMessage.getText().length() > 0) && (messageRecipient.getSelectionIndex() != -1)){
			encryptMessage.setEnabled(true);
		}
	}
	
	private void checkPasswords(){
		if (pw1 != null && pw2 != null){
			if (pw1.equals(pw2) && eIsValid){
				createKey.setEnabled(true);
			}else{
				createKey.setEnabled(false);
			}
//			System.out.println("[DEBUG]"+pw1.length()+ "-"+ pw2.length()+"+++++ "+eIsValid);
		}
		if(pw1_Ext != null && pw2_Ext != null){
			if (pw1_Ext.equals(pw2_Ext)&&(pw1_Ext.length()>0 && pw2_Ext.length()>0)){
				createKey_Tab2.setEnabled(true);
			}else{
				createKey_Tab2.setEnabled(false);
			}
		}
		
	}
	
	private void changeRSAVisibility_Tab2(){
		if (radio_RSA_tab2.getSelection()){
			//Radiobutton "RSA" is activated
			rsa_length.setEnabled(true);
			extRsa_length.setEnabled(false);
			extRsa_numberPrimes_tab2.setEnabled(false);

		}else{
			//Radiobutton "Multi-prime RSA" is activated
			rsa_length.setEnabled(false);
			extRsa_length.setEnabled(true);
			extRsa_numberPrimes_tab2.setEnabled(true);
		}
		lbl_notification_tab2.setText("");
		createKey_Tab2.setEnabled(false);
		ext_password1.setText("");
		ext_password2.setText("");
	}
	private void deactivateSave(){
		password1.setText("");
		password2.setText("");
		password1.setEnabled(false);
		password2.setEnabled(false);
		createKey.setEnabled(false);
		pickRandomE.setEnabled(false);
		combo_rsaE.setEnabled(false);
		combo_rsaE.setText("");
		eIsValid = false;
		bi_rsaE = null;
	}
	
	private void changeRSAVisibility(){
		if (radio_RSA.getSelection()){
			//Radiobutton "RSA" is activated
			
			pickRandomExtE.setEnabled(false);
			combo_ExrsaE.setEnabled(false);
			eIsValid = false;
			
			combo_rsaP.setEnabled(true);
			combo_rsaQ.setEnabled(true);
			combo_ExrsaP.setEnabled(false);
			combo_ExrsaQ.setEnabled(false);
			combo_ExrsaR.setEnabled(false);
			combo_ExrsaS.setEnabled(false);
			combo_ExrsaT.setEnabled(false);
			
			combo_ExrsaS.setVisible(false);
			combo_ExrsaT.setVisible(false);
			rsa_ex_S.setVisible(false);
			rsa_ex_T.setVisible(false);
			
			numberOfPrimesExRSA.select(0);
			numberOfPrimesExRSA.setEnabled(false);
			
			combo_rsaE.removeAll();
			
		}else{
			//Radiobutton "Multi-prime RSA" is activated
			pickRandomE.setEnabled(false);
			combo_rsaE.setEnabled(false);
			eIsValid = false;
			
			numberOfPrimesExRSA.setEnabled(true);
			numberOfPrimesExRSA.select(0);
			combo_rsaE.setEnabled(false);
			combo_rsaP.setEnabled(false);
			combo_rsaQ.setEnabled(false);
			combo_ExrsaP.setEnabled(true);
			combo_ExrsaQ.setEnabled(true);
			combo_ExrsaR.setEnabled(true);
			combo_ExrsaS.setEnabled(true);
			combo_ExrsaT.setEnabled(true);
			
			combo_ExrsaS.setVisible(false);
			combo_ExrsaT.setVisible(false);
			rsa_ex_S.setVisible(false);
			rsa_ex_T.setVisible(false);
			combo_ExrsaE.removeAll();
		}
		password1.setText("");
		password2.setText("");
		password1.setEnabled(false);
    	password2.setEnabled(false);
		createKey.setEnabled(false);
		
		errorLabel_1.setText("");
		txtExplain.setText(EXPLAIN_KEYMGMT_TAB1);
		resetRSAValues();
	}
	
	private void resetMPInputs(){
		bi_ExtrsaE = null;
		combo_ExrsaE.setText("");
		password1.setEnabled(false);
		password2.setEnabled(false);
		password1.setText("");
		password2.setText("");
		pickRandomExtE.setEnabled(false);
		combo_ExrsaE.setEnabled(false);
		stopUpdateE = false;
	}
	
	private void resetRSAValues(){
		bi_rsaE = null;
		bi_rsaP = null;
		bi_rsaQ = null;
		bi_ExtrsaP = null;
		bi_ExtrsaQ = null;
		bi_ExtrsaR = null;
		bi_ExtrsaS = null;
		bi_ExtrsaT = null;
		bi_ExtrsaE = null;
		bi_ExtrsaN = null;
		bi_ExtrsaPhi = null;
		
		if(combo_rsaE != null){
			combo_rsaE.setText("");
		}
		if(combo_rsaP != null){
			combo_rsaP.setText("");
		}
		if(combo_rsaQ != null){
			combo_rsaQ.setText("");
		}
		if(combo_ExrsaP != null){
			combo_ExrsaP.setText("");
		}
		if(combo_ExrsaQ != null){
			combo_ExrsaQ.setText("");
		}
		if(combo_ExrsaR != null){
			combo_ExrsaR.setText("");
		}
		if(combo_ExrsaS != null){
			combo_ExrsaS.setText("");
		}
		if(combo_ExrsaT != null){
			combo_ExrsaT.setText("");
		}
		if(combo_ExrsaE != null){
			combo_ExrsaE.setText("");
		}
	}
	
	/**
	 * get all primes for a possible 'e'. 
	 * @return 6542 prime numbers from 3 to 65537
	 */
	private Vector<BigInteger> getEPrimes(){
		BigInteger start = new BigInteger("3");
		BigInteger end = new BigInteger("65537");
		Vector<BigInteger> ePrimes = new Vector<BigInteger>();
		while (start.compareTo(end) <= 0){
			if (!Lib.isPrime(start)){
				start = start.add(BigInteger.ONE);
			}else{
				ePrimes.add(start);
				start = start.add(BigInteger.ONE);
			}
		}
		return ePrimes;
	}
	
	private void addRecipientsToCombo(){
		Vector<String> recipients = new Vector<String>();
		if (messageRecipient != null){
			messageRecipient.removeAll();
		}
		
		for (String s : iMgr.getContacts()){
//			if (!s.equals(this.identityName) && !recipients.contains(s)){
			if (!recipients.contains(s)){
				if (IdentityManager.getInstance().countOwnKeys(s)>0){
					recipients.add(s);
					System.out.println("[DEBUG] filling recipients... adding: "+s);
				}else{
					System.out.println("[DEBUG] recipient "+s+" has no keys");
				}
			}
		}
		messageRecipient.setItems(recipients.toArray(new String[recipients.size()]));
	}
	
    /**
     * fills all primes into the given combo item.
     * @param combo the list from which a prime can be selected
     */
    private void fillPrimesTo(final Combo combo) {
    	combo.removeAll();
        for (Integer i : LOW_PRIMES) {
            combo.add(i.toString());
        }
    }
	
	private void createActionGroup1(){
		actionGroup_1 = new Group(generalGroup, SWT.NONE);
		actionGroup_1.setText("Aktionsfenster");
		actionLayout_1 = new GridLayout(2, true);
		actionLayout_1.horizontalSpacing = 80;

		actionGroup_1.setLayout(actionLayout_1);
		group_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		group_1.exclude = false;
		actionGroup_1.setLayoutData(group_1);
		initActions = new Label(actionGroup_1, SWT.WRAP);
		initActions.setText("W\u00e4hlen Sie eine Aktion f\u00fcr die aktuelle Identit\u00e4t ("+identityName+"), oder einen Button im Feld „Identit\u00e4ten-Verwaltung“.");
		actionGroup_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	private void createActionGroup2(){
		actionGroup_2 = new Group(generalGroup, SWT.NONE);
		actionGroup_2.setText("Aktionsfenster");
		actionLayout_2 = new GridLayout(2, true);
		actionLayout_2.horizontalSpacing = 80;
		actionGroup_2.setLayout(actionLayout_2);
		group_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		actionGroup_2.setLayoutData(group_2);
	}
	private void createActionGroup3(){
		actionGroup_3 = new Group(generalGroup, SWT.NONE);
		actionGroup_3.setText("Aktionsfenster");
		actionLayout_3 = new GridLayout(1, false);
		actionLayout_3.horizontalSpacing = 80;
		actionGroup_3.setLayout(actionLayout_3);
		group_3 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		actionGroup_3.setLayoutData(group_3);
	}
	
	private void createActionGroup4(){
		actionGroup_4 = new Group(generalGroup, SWT.NONE);
		actionGroup_4.setText("Aktionsfenster");
		actionLayout_4 = new GridLayout(4, false);
		actionGroup_4.setLayout(actionLayout_4);
		group_4 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		actionGroup_4.setLayoutData(group_4);
	}	
	
	private Label createSpacer(final Composite location){
		Label spacer = new Label(location, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		return spacer;
	}

	public final String getIdentityName() {
		return identityName;
	}

	public final void setIdentityName(String identityName) {
		this.identityName = identityName;
	}


	public final String getForename() {
		return forename;
	}

	public final void setForename(final String forename) {
		this.forename = forename;
	}

	public final String getSurname() {
		return surname;
	}

	public final void setSurname(final String surname) {
		this.surname = surname;
	}

	public final String getOrganisation() {
		return organisation;
	}

	public final void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	public final String getRegion() {
		return region;
	}

	public final void setRegion(final String region) {
		this.region = region;
	}

	public int getId() {
		return id;
	}

	@Override 
	protected void checkSubclass() { 
	}
	
	/**
	 * main check-method for the entered parameters
	 */
    private void checkParameter(){
    	pIsPrime = false;
    	qIsPrime = false;
    	rIsPrime = false;
    	sIsPrime = false;
    	tIsPrime = false;
    	eIsValid = false;
    	BigInteger minimum = new BigInteger("3");
    	
    	errorLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
    	
    	if (radio_RSA.getSelection()){
	    	if(bi_rsaP!=null){
	    		if (!bi_rsaP.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_rsaP)) {
		        	errorLabel_1.setText(NO_PRIME_P);
		        	combo_rsaE.removeAll();
		        	pIsPrime = false;
		        	deactivateSave();
		        }else if (bi_rsaP.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
	        		errorLabel_1.setText("");
	        		pIsPrime = true;
		        }
	    	}
	        if (bi_rsaQ!=null){
		        if (!bi_rsaQ.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_rsaQ)) {
		        	errorLabel_1.setText(NO_PRIME_Q);
		        	combo_rsaE.removeAll();
		        	qIsPrime = false;
		        	deactivateSave();
		        }else if (bi_rsaQ.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
		        	errorLabel_1.setText("");
		        	qIsPrime = true;
		        }
	        }
	        //show only the error-message again, because the message will be overwritten in the code above
	        if (qIsPrime && !pIsPrime && bi_rsaP != null){
	        	errorLabel_1.setText(NO_PRIME_P);
	        }
	        if(!pIsPrime && !qIsPrime){
	        	combo_rsaE.setText("");
	        }
	        if(pIsPrime && qIsPrime){
		        if (!bi_rsaP.equals(bi_rsaQ)) {
		        	bi_rsaN = bi_rsaP.multiply(bi_rsaQ);                    	
		        	bi_rsaPhi = Lib.calcPhi(bi_rsaP, bi_rsaQ);
		        	pickRandomE.setEnabled(true); 
		        	
	        		if (!stopUpdateE){
	        			combo_rsaE.setEnabled(true);
		        		combo_rsaE.removeAll();
	        			fillE();
	        		}
		            
		            if(bi_rsaE != null){
		            	if (!possibleEs.contains(bi_rsaE.toString())) {
		    	        	errorLabel_1.setText(NO_VALID_E);
		    	        	password1.setText("");
		    	    		password2.setText("");
		    	    		createKey.setEnabled(false);
		    	    		eIsValid = false;
		    	        }else{
		    	        	errorLabel_1.setText("");
		    	        	eIsValid = true;
		    	        }
		            }
	
		        } else {
		        	bi_rsaN = Constants.MINUS_ONE;
		        	bi_rsaPhi = Constants.MINUS_ONE;
		        	combo_rsaE.removeAll();
		        	errorLabel_1.setText(PRIMES_EQUAL);
		        	pickRandomE.setEnabled(false);
		        	combo_rsaE.setEnabled(false);
		        }
		        
		        password1.setEnabled(eIsValid);
	        	password2.setEnabled(eIsValid);
	        }
    	}else{
    		//check the multi-prime RSA parameters
    		bi_ExtrsaN = Constants.MINUS_ONE;
    		bi_ExtrsaPhi = Constants.MINUS_ONE;
    		
    		if(bi_ExtrsaP!=null){
	    		if (!bi_ExtrsaP.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaP)) {
		        	errorLabel_1.setText(NO_PRIME_P);
		        	pIsPrime = false;
		        	disableSomeExRSAParams();
		        }else if (bi_ExtrsaP.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
	        		errorLabel_1.setText("");
	        		pIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaQ!=null){
	    		if (!bi_ExtrsaQ.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaQ)) {
		        	errorLabel_1.setText(NO_PRIME_Q);
		        	qIsPrime = false;
		        	disableSomeExRSAParams();
		        }else if (bi_ExtrsaQ.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
	        		errorLabel_1.setText("");
	        		qIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaR!=null){
	    		if (!bi_ExtrsaR.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaR)) {
		        	errorLabel_1.setText(NO_PRIME_R);
		        	rIsPrime = false;
		        	disableSomeExRSAParams();
		        }else if (bi_ExtrsaR.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
	        		errorLabel_1.setText("");
	        		rIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaS!=null){
	    		if (!bi_ExtrsaS.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaS)) {
		        	errorLabel_1.setText(NO_PRIME_S);
		        	sIsPrime = false;
		        	disableSomeExRSAParams();
		        }else if (bi_ExtrsaS.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
	        		errorLabel_1.setText("");
	        		sIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaT!=null){
	    		if (!bi_ExtrsaT.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaT)) {
		        	errorLabel_1.setText(NO_PRIME_T);
		        	tIsPrime = false;
		        	disableSomeExRSAParams();
		        }else if (bi_ExtrsaT.compareTo(minimum)< 0){
	        		errorLabel_1.setText(VALUE_TOO_SMALL);
	        	}else{
	        		errorLabel_1.setText("");
	        		tIsPrime = true;
		        }
	    	}
    		
    		//check belated changes and show only the message.. the rest is done in the methods above
    		if (!bi_ExtrsaP.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaP) && combo_ExrsaP.getText().length()>0){
    			errorLabel_1.setText(NO_PRIME_P);
    		}else if (bi_ExtrsaP != null && bi_ExtrsaP.compareTo(minimum)< 0){
        		errorLabel_1.setText(VALUE_TOO_SMALL);
        	}
    		
    		if (bi_ExtrsaQ != null && !bi_ExtrsaQ.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaQ) && combo_ExrsaQ.getText().length()>0){
    			errorLabel_1.setText(NO_PRIME_Q);
    		}else if (bi_ExtrsaQ != null && bi_ExtrsaQ.compareTo(minimum)< 0){
        		errorLabel_1.setText(VALUE_TOO_SMALL);
        	}
    		
    		if (bi_ExtrsaR != null && !bi_ExtrsaR.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaR) && combo_ExrsaR.getText().length()>0){
    			errorLabel_1.setText(NO_PRIME_R);
    		}else if (bi_ExtrsaR != null && bi_ExtrsaR.compareTo(minimum)< 0){
        		errorLabel_1.setText(VALUE_TOO_SMALL);
        	}
    		
    		if (bi_ExtrsaS != null && !bi_ExtrsaS.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaS) && combo_ExrsaS.getText().length()>0){
    			errorLabel_1.setText(NO_PRIME_S);
    		}else if (bi_ExtrsaS != null && bi_ExtrsaS.compareTo(minimum)< 0){
        		errorLabel_1.setText(VALUE_TOO_SMALL);
        	}
    		
    		if (bi_ExtrsaT != null && !bi_ExtrsaT.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaT) && combo_ExrsaT.getText().length()>0){
    			errorLabel_1.setText(NO_PRIME_T);
    		}else if (bi_ExtrsaT != null && bi_ExtrsaT.compareTo(minimum)< 0){
        		errorLabel_1.setText(VALUE_TOO_SMALL);
        	}
    		
    		//check, if different primes are chosen
    		validCount = 0;
    		TreeSet<BigInteger> checkPrimes = new TreeSet<BigInteger>();
    		if (bi_ExtrsaP != null && pIsPrime){
    			checkPrimes.add(bi_ExtrsaP);
    			validCount++;
    		}else{
    			combo_ExrsaE.removeAll();
    		}
    		
    		if (bi_ExtrsaQ != null && qIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaQ)){
    				errorLabel_1.setText(PRIMES_EQUAL);
    				resetMPInputs();
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaQ);
        			validCount++;
        		}
    		}
    		
    		if (bi_ExtrsaR != null && rIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaR)){
    				errorLabel_1.setText(PRIMES_EQUAL);
    				resetMPInputs();
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaR);
        			validCount++;
        		}
    		}
    		
    		if (bi_ExtrsaS != null && sIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaS)){
    				errorLabel_1.setText(PRIMES_EQUAL);
    				resetMPInputs();
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaS);
        			validCount++;
        		}
    		}
    		
    		if (bi_ExtrsaT != null && tIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaT)){
    				errorLabel_1.setText(PRIMES_EQUAL);
    				resetMPInputs();
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaT);
        			validCount++;
        		}
    		}

    		int selectednumberOfPrimes = Integer.parseInt(numberOfPrimesExRSA.getItem(numberOfPrimesExRSA.getSelectionIndex()));
    		
    		if (validCount == selectednumberOfPrimes){
    			switch (selectednumberOfPrimes){
    				case 3: bi_ExtrsaN = bi_ExtrsaP.multiply(bi_ExtrsaQ).multiply(bi_ExtrsaR);
    						bi_ExtrsaPhi = (bi_ExtrsaP.subtract(ONE)).multiply(bi_ExtrsaQ.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE));
    						break;
    						
    				case 4: bi_ExtrsaN = bi_ExtrsaP.multiply(bi_ExtrsaQ).multiply(bi_ExtrsaR).multiply(bi_ExtrsaS);
							bi_ExtrsaPhi = (bi_ExtrsaP.subtract(ONE)).multiply(bi_ExtrsaQ.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE));
							break;
							
    				case 5: bi_ExtrsaN = bi_ExtrsaP.multiply(bi_ExtrsaQ).multiply(bi_ExtrsaR).multiply(bi_ExtrsaS).multiply(bi_ExtrsaT);
							bi_ExtrsaPhi = (bi_ExtrsaP.subtract(ONE)).multiply(bi_ExtrsaQ.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE)).multiply(bi_ExtrsaT.subtract(ONE));
							break;
    			}
    			if(!stopUpdateE){
    				combo_ExrsaE.removeAll();
        			fillE();
    			}
    			pickRandomExtE.setEnabled(true);
    			combo_ExrsaE.setEnabled(true);

		        
    			if(bi_ExtrsaE != null){
	            	if (!possibleEs.contains(bi_ExtrsaE.toString())) {
	    	        	errorLabel_1.setText(NO_VALID_E);
	    	        	eIsValid = false;
	    	        }else{
	    	        	errorLabel_1.setText("");
	    	        	eIsValid = true;
	        			
	    	        }
	            	password1.setEnabled(eIsValid);
    	        	password2.setEnabled(eIsValid);
	            }
    		}
    	}
    	
        
    }
    private void disableSomeExRSAParams(){
    	eIsValid = false;
    	pickRandomExtE.setEnabled(false); 
    	combo_ExrsaE.setEnabled(false);
    	combo_ExrsaE.removeAll();
    	createKey.setEnabled(false);
    }
    
    private void fillE(){
    	eIsValid = false;
    	password1.setText("");
    	password2.setText("");
    	createKey.setEnabled(false);
    	possibleEs = new Vector<String>();
    	
    	if(radio_RSA.getSelection()){
        	combo_rsaE.removeAll();
	        if (!bi_rsaP.equals(bi_rsaQ)) {
	            for (int i = 0; i < primesE.size(); i++){
	            	if (primesE.get(i).compareTo(bi_rsaN) < 0){
	            		possibleEs.add(primesE.get(i).toString());
	            	}
	            }
	            combo_rsaE.setItems(possibleEs.toArray(new String[possibleEs.size()]));
	        }
    	}else{
    		combo_ExrsaE.removeAll();
    		for (int i = 0; i < primesE.size(); i++){
            	if (primesE.get(i).compareTo(bi_ExtrsaN) < 0){
            		possibleEs.add(primesE.get(i).toString());
            	}
            }
            combo_ExrsaE.setItems(possibleEs.toArray(new String[possibleEs.size()]));
    	}
    }
}