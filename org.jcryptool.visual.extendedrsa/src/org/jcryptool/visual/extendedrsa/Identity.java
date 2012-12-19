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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.util.fonts.FontService;

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
	private Text encryptedMessage_2;
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
	private Combo subjectChoose;
	private Combo decryptionKey;
	private Button deleteMessage;
	private Text pwPrivKey;
	
	public Identity(TabFolder parent, int style, String identityName, String forename, String surname, String organisation, String region) {
		super(parent, style);
		this.identityName = identityName;
		this.forename = forename;
		this.surname = surname;
		this.organisation = organisation;
		this.region = region;
		this.id = parent.getItemCount();
		
		//set the text of the TabItem
		this.setText(identityName);
		forerunner = 0;	
		
		//*** define the layout for the whole TabItem now
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
				if(actionGroup_1.isDisposed()){
					createActionGroup1();
				}
				if (forerunner != 1){
					actionGroup_2.dispose();
					actionGroup_3.dispose();
					actionGroup_4.dispose();
					
					initActions.setText("Betreff der Nachricht: ");
					createSpacer(actionGroup_1);
					
					subjectInput = new Text(actionGroup_1, SWT.NONE);
					GridData gd_subject = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_subject.heightHint = 13;
					subjectInput.setLayoutData(gd_subject);
					createSpacer(actionGroup_1);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Nachricht:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Verschl\u00fcsselte Nachricht:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					
					clearMessage = new Text(actionGroup_1, SWT.MULTI | SWT.WRAP);
					clearMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 18));
					encryptedMessage = new Text(actionGroup_1, SWT.MULTI | SWT.WRAP);
					encryptedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 18));
					
					createSpacer(actionGroup_1);
					createSpacer(actionGroup_1);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Empf\u00e4nger:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_1);
					
					messageRecipient = new Combo(actionGroup_1, SWT.READ_ONLY);
					GridData gd_recp = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_recp.heightHint = 13;
					messageRecipient.setLayoutData(gd_recp);
					sendMessage = new Button(actionGroup_1, SWT.PUSH);
					sendMessage.setText("Nachricht senden");
					sendMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							encryptedMessage.setText("nun wird die nachricht gesendet");
						}
					});
					sendMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Schl\u00fcssel ausw\u00e4hlen:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_1);
					
					recipientKeys = new Combo(actionGroup_1, SWT.READ_ONLY);
					GridData gd_rk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_rk.heightHint = 13;
					recipientKeys.setLayoutData(gd_rk);
					createSpacer(actionGroup_1);
					
					createSpacer(actionGroup_1);
					createSpacer(actionGroup_1);
					
					encryptMessage = new Button(actionGroup_1, SWT.PUSH);
					encryptMessage.setText("Nachricht verschl\u00fcsseln");
					encryptMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							encryptedMessage.setText("nun wird die nachricht verschl\u00fcsselt");
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
				if (forerunner != 2){
					actionGroup_1.dispose();
					actionGroup_3.dispose();
					actionGroup_4.dispose();
					
					createActionGroup2();
						
					initActions2 = new Label(actionGroup_2, SWT.NONE);
					initActions2.setText("Nachricht ausw\u00e4hlen: ");
					initActions2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					createSpacer(actionGroup_2);	
					
//					label = new Label (actionGroup_2, SWT.NONE);
//					label.setText("testtest");
//					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					subjectChoose = new Combo(actionGroup_2, SWT.READ_ONLY);
					GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_combo.heightHint = 13;
					subjectChoose.setLayoutData(gd_combo);
					
					createSpacer(actionGroup_2);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Verschl\u00fcsselte Nachricht:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Entschl\u00fcsselte Nachricht:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					
					encryptedMessage_2= new Text(actionGroup_2, SWT.MULTI | SWT.WRAP);
					encryptedMessage_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 18));
					decryptedMessage = new Text(actionGroup_2, SWT.MULTI | SWT.WRAP);
					decryptedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 18));
					
					createSpacer(actionGroup_2);
					createSpacer(actionGroup_2);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Schl\u00fcssel ausw\u00e4hlen:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_2);
					
					decryptionKey = new Combo(actionGroup_2, SWT.READ_ONLY);
					GridData gd_dk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_dk.heightHint = 13;
					decryptionKey.setLayoutData(gd_dk);
					deleteMessage = new Button(actionGroup_2, SWT.PUSH);
					deleteMessage.setText("Nachricht l\u00f6schen");
					deleteMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							decryptedMessage.setText("nun wird die nachricht gel\u00f6scht");
						}
					});
					deleteMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Passwort eingeben:");
					GridData gd_pw = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_pw.heightHint = 13;
					label.setLayoutData(gd_pw);
					createSpacer(actionGroup_2);
					
					pwPrivKey = new Text(actionGroup_2, SWT.NONE);
					GridData gd_key = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_key.widthHint = 200;
					pwPrivKey.setLayoutData(gd_key);
					createSpacer(actionGroup_2);
					
					createSpacer(actionGroup_2);
					createSpacer(actionGroup_2);
					
					decryptMessage = new Button(actionGroup_2, SWT.PUSH);
					decryptMessage.setText("Nachricht entschl\u00fcsseln");
					decryptMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							decryptedMessage.setText("nun wird die nachricht verschl\u00fcsselt");
						}
					});
					decryptMessage.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1));
					createSpacer(actionGroup_2);
					
					
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
				actionGroup_1.setVisible(false);
				actionGroup_2.setVisible(false);
				actionGroup_4.setVisible(false);
				group_1.exclude = true;
				group_2.exclude = true;
				group_4.exclude = true;
			}
		});
		keymanagement.setText("Schl\u00fcsselverwaltung");
		keymanagement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		attackPublicKey = new Button(composite, SWT.PUSH);
		attackPublicKey.addSelectionListener(new SelectionAdapter() {
			@Override //Button 4
			public void widgetSelected(SelectionEvent e) {
				
				
			}
		});
		attackPublicKey.setText("\u00d6ffentlichen Schl\u00fcssel angreifen");
		attackPublicKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//add spacer to the buttons on the left (for further actions :) )
		for (int i = 0; i < 9; i++){
			createSpacer(composite);
		}
		
		createActionGroup1();
		
		createActionGroup2();
		group_2.exclude = true;
		actionGroup_2.setVisible(false);
		
		actionGroup_3 = new Group(generalGroup, SWT.NONE);
		actionGroup_3.setText("Aktionsfenster 3");
		actionLayout_3 = new GridLayout(1, false);
		actionGroup_3.setLayout(actionLayout_3);
		group_3 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		group_3.exclude = true;
		actionGroup_3.setLayoutData(group_3);
		actionGroup_3.setVisible(false);
		
		actionGroup_4 = new Group(generalGroup, SWT.NONE);
		actionGroup_4.setText("Aktionsfenster 4");
		actionLayout_4 = new GridLayout(1, false);
		actionGroup_4.setLayout(actionLayout_4);
		group_4 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		group_4.exclude = true;
		actionGroup_4.setLayoutData(group_4);
		actionGroup_4.setVisible(false);
		
	}
	private void createActionGroup1(){
		actionGroup_1 = new Group(generalGroup, SWT.NONE);
		actionGroup_1.setText("Aktionsfenster");
		actionLayout_1 = new GridLayout(2, false);
		actionLayout_1.horizontalSpacing = 80;

		actionGroup_1.setLayout(actionLayout_1);
		group_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		group_1.exclude = false;
		actionGroup_1.setLayoutData(group_1);
		initActions = new Label(actionGroup_1, SWT.NONE);
		initActions.setText("Bitte w\u00e4hlen Sie eine Aktion.");
		actionGroup_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	private void createActionGroup2(){
		actionGroup_2 = new Group(generalGroup, SWT.NONE);
		actionGroup_2.setText("Aktionsfenster");
		actionLayout_2 = new GridLayout(2, false);
		actionLayout_2.horizontalSpacing = 80;
		actionGroup_2.setLayout(actionLayout_2);
		group_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		actionGroup_2.setLayoutData(group_2);
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
//	public void setId(int id) {
//		this.id = id;
//	}
	@Override 
	protected void checkSubclass() { 
	}
}
