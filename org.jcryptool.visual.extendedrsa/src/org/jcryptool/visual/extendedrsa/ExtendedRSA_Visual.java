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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Iterator;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.keys.KeyType;
import org.jcryptool.crypto.keystore.KeyStorePlugin;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.crypto.keystore.backend.KeyStoreManager;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.visual.extendedrsa.ui.wizard.DeleteIdentityWizard;
import org.jcryptool.visual.extendedrsa.ui.wizard.ManageVisibleIdentitesWizard;
import org.jcryptool.visual.extendedrsa.ui.wizard.NewIdentityWizard;
import org.jcryptool.crypto.keystore.descriptors.interfaces.IContactDescriptor;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.events.MouseListener;
//import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;


/**
 * Represents the visual
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class ExtendedRSA_Visual extends ViewPart{

	public static final String ID = "org.jcryptool.visual.extendedrsa.ExtendedRSAView";
	public static final String ALICE = "Alice Whitehat";
	public static final String BOB = "Bob Whitehat";
	
	private ScrolledComposite sc;
	private Composite composite;
	private GridLayout gl;
	private Composite headComposite;
	private Label label;
	private StyledText head_description;
	private Group grp_id_mgmt;
	private Button btn_newID;
	private Button btn_manageID;
	private Button btn_delID;
	private Composite comp_center;
	private ExtendedTabFolder tabFolder;
	private Identity identity;
	private Label txtExplain;
	private Enumeration<String> aliases;
//    private KeyStoreAlias keyStoreAlias;
    
	
	public ExtendedRSA_Visual() {
	}

	@Override
	public void createPartControl(Composite parent) {

		//make the composite scrollable
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		composite = new Composite(sc, SWT.NONE);
		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(composite.computeSize(1000, 800));
		
		gl = new GridLayout(1, false);
		gl.verticalSpacing = 20;
        composite.setLayout(gl);
    
		//Begin - Header
		headComposite = new Composite(composite, SWT.NONE);
		headComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		headComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		headComposite.setLayout(new GridLayout());
		
		label = new Label(headComposite, SWT.NONE);
		label.setFont(FontService.getHeaderFont());
		label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label.setText("Extended RSA-Kryptosystem");
		head_description = new StyledText(headComposite, SWT.READ_ONLY | SWT.MULTI| SWT.WRAP);
		head_description.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,false));
		head_description.setText("In diesem Plugin k\u00f6nnen Sie mit dem RSA-Verfahren verschiedene Aktionen durchf\u00fchren. Dazu agieren Sie im Namen unterschiedlicher Identit\u00e4ten: Sie k\u00f6nnten als 'Alice' einen Text verschl\u00fcsseln und an 'Bob' senden.  Bob kann dann die empfangene Nachricht entschl\u00fcsseln. Und umgekehrt.");	
		//End - Header

		grp_id_mgmt = new Group(composite, SWT.NONE);
		grp_id_mgmt.setText("Identit\u00e4tenverwaltung");
		grp_id_mgmt.setLayout(new GridLayout(3,true));

		btn_newID = new Button(grp_id_mgmt, SWT.PUSH);
		btn_manageID = new Button(grp_id_mgmt, SWT.PUSH);
		btn_delID = new Button(grp_id_mgmt, SWT.PUSH);
		
		
		btn_newID.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//button "Identität löschen" mitgeben... zum deaktivieren/aktivieren, falls zu wenige Identitäten existiern
				new WizardDialog(getSite().getShell(), new NewIdentityWizard(tabFolder)).open();
				grp_id_mgmt.update();
			}
		});
		btn_newID.setText("Neue Identit\u00e4t erstellen");
		btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		//neuer button begin
		btn_manageID.setText("Identit\u00e4t ein-/ausblenden");
		btn_manageID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btn_manageID.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				new WizardDialog(getSite().getShell(), new ManageVisibleIdentitesWizard()).open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		//neuer button ende
		
		btn_delID.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//button "Identität löschen" mitgeben... zum deaktivieren/aktivieren, falls zu wenige Identitäten existiern nachm löschen
				new WizardDialog(getSite().getShell(), new DeleteIdentityWizard(tabFolder)).open();
				grp_id_mgmt.update();
			}
		});
		btn_delID.setText("Identit\u00e4t l\u00f6schen");
		btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		comp_center = new Composite(composite, SWT.NONE);
		//2 columns (tabs and explanation) --> new GridLayout(2, false);
        comp_center.setLayout(new GridLayout(2, false)); 
        comp_center.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tabFolder = new ExtendedTabFolder(comp_center, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tabFolder.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				txtExplain.setText("Die Identitäten aus Ihrem Schlüsselspeicher werden in dieser Visualisierung als Tabs (Registerkarten) angezeigt. Schon bei der Auslieferung befinden sich die Identitäten „Alice“ und „Bob“ im Schlüsselspeicher und werden dehalb auch initial schon als Tabs angezeigt.\n\nJede Registerkarte stellt eine Identität dar. Durch den Button „Identitäten ein-/ausblenden“ können bestehende Identitäten als Registerkarten angezeigt oder ausgeblendet werden. Wenn eine neue Identität erstellt wird, wird diese erst als Registerkarte angezeigt, wenn sie durch „Identitäten ein-/ausblenden“ ausgewählt wurde!\n\nWird nun ein Button auf einer Registerkarte angeklickt (und so eine Aktion im Namen einer Identität durchgeführt), wird eine Hilfe im Feld „Erklärungen“ angezeigt.");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
////		tabFolder.addMouseMoveListener(new MouseMoveListener() {
//			
//			@Override
//			public void mouseMove(MouseEvent e) {
//				if (tabFolder.getItemCount() < 3){
//					btn_delID.setEnabled(false);
//				}else{
//					btn_delID.setEnabled(true);
//				}
//			}
//		});
//		btn_delID.setEnabled(false);
		

		Group grp_explain = new Group(comp_center, SWT.NONE);
		grp_explain.setLayout(new GridLayout(1, true));
		GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_explain.widthHint = 270;
		
		grp_explain.setText("Erkl\u00e4rungen");
		
		txtExplain = new Label(grp_explain,  SWT.WRAP);
		GridData gd_txtEplain = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_txtEplain.heightHint = 300;
		txtExplain.setLayoutData(gd_txtEplain);

		grp_explain.setLayoutData(gd_explain);
		
		initKeystore(tabFolder);
	}

	private void initKeystore(TabFolder tabfolder) {
        try{
        	if (!KeyStorePlugin.isInitialized()){
        		KeyStorePlugin.initialize();
        	}
        	
        	IdentityManager iMgr = IdentityManager.getInstance();
            Vector<String> contactNames = iMgr.getContacts();

            if (!contactNames.contains(ALICE)){
            	//create Alice in the keystore
            	iMgr.createIdentity(ALICE, "RSA", "1234", 1024);
            	System.out.println("[DEBUG] ALICE generiert rsa key");
            }
        	Vector<String> keyAlgos = iMgr.getAssymetricKeyAlgorithms(ALICE);

        	if (!keyAlgos.contains("MpRSA")){
//            		iMgr.createIdentity(ALICE, "MpRSA", "1234", 1024);
        		System.out.println("[DEBUG]mprsa-key wird generiert...alice");
        	}
        	if (!keyAlgos.contains("RSA")){
//            		iMgr.createIdentity(ALICE, "RSA", "1234", 1024);
        		System.out.println("[DEBUG]rsa-key wird generiert... alice");
        	}
              
            
            String[] alice_split = ALICE.split(" ");
            //create "Alice" in the visual
    		identity = new Identity(tabFolder, SWT.NONE, ALICE, alice_split[0], alice_split[1], "none", "unknown", txtExplain);
    		
    		
            if (!contactNames.contains(BOB)){
            	//create Bob in the keystore
            	iMgr.createIdentity(BOB, "RSA", "1234", 1024);
            	System.out.println("[DEBUG]rsakey wird generiert...bob");
            }
        	keyAlgos = iMgr.getAssymetricKeyAlgorithms(BOB);

        	if (!keyAlgos.contains("MpRSA")){
//            		iMgr.createIdentity("Bob", "MpRSA", "1234", 1024);
        		System.out.println("[DEBUG]mprsa-key wird generiert...bob");
        	}
        	if (!keyAlgos.contains("RSA")){
//            		iMgr.createIdentity("Bob", "RSA", "1234", 1024);
        		System.out.println("[DEBUG]rsa-key wird generiert...bob");
        	}
            
            
            String[] bob_split = BOB.split(" ");
            //create "Bob" in the visual
    		identity = new Identity(tabFolder, SWT.NONE, BOB, bob_split[0], bob_split[1], "none", "unknown", txtExplain);
        	
            
        }catch (Exception e) {
            LogUtil.logError(e);
        }    
	}

	@Override
	public void setFocus() {}
}
