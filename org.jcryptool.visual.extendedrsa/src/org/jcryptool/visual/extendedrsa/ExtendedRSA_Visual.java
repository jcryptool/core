// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012, 2020 JCrypTool Team and Contributors
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.core.util.ui.auto.LayoutAdvisor;
import org.jcryptool.core.util.ui.auto.SmoothScroller;
import org.jcryptool.crypto.keystore.ui.views.nodes.Contact;
import org.jcryptool.crypto.keystore.ui.views.nodes.ContactManager;
import org.jcryptool.visual.extendedrsa.ui.wizards.DeleteIdentityWizard;
import org.jcryptool.visual.extendedrsa.ui.wizards.ManageVisibleIdentitesWizard;
import org.jcryptool.visual.extendedrsa.ui.wizards.NewIdentityWizard;

/**
 * Represents the visual
 * 
 * @author Christoph Schnepf, Patrick Zillner
 * 
 */
public class ExtendedRSA_Visual extends ViewPart {

	private final String ALICE = Messages.ExtendedRSA_Visual_1;
	private final String BOB = Messages.ExtendedRSA_Visual_2;
	private final String BLANK = Messages.ExtendedRSA_Visual_3;

	private ScrolledComposite sc;
	private Composite composite;
	private Composite headComposite;
	private Group grp_id_mgmt;
	private Button btn_newID;
	private Button btn_manageID;
	private Button btn_delID;
	private Composite comp_center;
	private ExtendedTabFolder tabFolder;
	private StyledText txtExplain;

	public ExtendedRSA_Visual() {
	}

	@Override
	public void createPartControl(Composite parent) {

		// make the composite scrollable
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		composite = new Composite(sc, SWT.NONE);
		sc.setContent(composite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		composite.setLayout(new GridLayout());

		// Begin - Header
		headComposite = new Composite(composite, SWT.NONE);
		headComposite.setBackground(ColorService.WHITE);
		headComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		headComposite.setLayout(new GridLayout());


		TitleAndDescriptionComposite headerTextfield = new TitleAndDescriptionComposite(headComposite);
		headerTextfield.setTitleAndDescription(Messages.ExtendedRSA_Visual_4, Messages.ExtendedRSA_Visual_5);
		headerTextfield.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		grp_id_mgmt = new Group(composite, SWT.NONE);
		grp_id_mgmt.setText(Messages.ExtendedRSA_Visual_6);
		grp_id_mgmt.setLayout(new GridLayout(3, false));

		btn_newID = new Button(grp_id_mgmt, SWT.PUSH);
		btn_manageID = new Button(grp_id_mgmt, SWT.PUSH);
		btn_delID = new Button(grp_id_mgmt, SWT.PUSH);

		btn_newID.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardDialog newIdentityWizard = new WizardDialog(getSite().getShell(),
						new NewIdentityWizard(tabFolder, btn_delID));
				newIdentityWizard.setHelpAvailable(false);
				newIdentityWizard.setPageSize(667, SWT.DEFAULT);
				newIdentityWizard.open();
				grp_id_mgmt.update();
			}
		});
		btn_newID.setText(Messages.ExtendedRSA_Visual_7);
		btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		btn_manageID.setText(Messages.ExtendedRSA_Visual_8);
		btn_manageID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btn_manageID.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardDialog manageVisibleIdentitiesWizard = new WizardDialog(getSite().getShell(),
						new ManageVisibleIdentitesWizard(tabFolder, txtExplain));
				manageVisibleIdentitiesWizard.setHelpAvailable(false);
				manageVisibleIdentitiesWizard.setPageSize(667, SWT.DEFAULT);
				manageVisibleIdentitiesWizard.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		btn_delID.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WizardDialog deleteIdentityWizard = new WizardDialog(getSite().getShell(),
						new DeleteIdentityWizard(tabFolder, btn_delID));
				deleteIdentityWizard.setHelpAvailable(false);
				deleteIdentityWizard.setPageSize(667, SWT.DEFAULT);
				deleteIdentityWizard.open();
				grp_id_mgmt.update();
			}
		});
		btn_delID.setText(Messages.ExtendedRSA_Visual_9);
		btn_delID.setEnabled(ContactManager.getInstance().getContactSize() > 2);
		btn_newID.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

		// Beginn des TabFolders
		comp_center = new Composite(composite, SWT.NONE);
		comp_center.setLayout(new GridLayout(2, false));
		comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tabFolder = new ExtendedTabFolder(comp_center, SWT.V_SCROLL);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tabFolder.setLayoutData(gd_tabFolder);

		Group grp_explain = new Group(comp_center, SWT.NONE);
		grp_explain.setLayout(new GridLayout(1, true));
		grp_explain.setText(Messages.ExtendedRSA_Visual_10);
		grp_explain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		txtExplain = new StyledText(grp_explain, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
		GridData gd_txtEplain = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_txtEplain.widthHint = 400;
		txtExplain.setLayoutData(gd_txtEplain);
		txtExplain.setBackground(ColorService.LIGHTGRAY);
		txtExplain.setAlwaysShowScrollBars(false);

		initKeystore();

		sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		LayoutAdvisor.addPreLayoutRootComposite(sc);
		
		// This makes the ScrolledComposite scrolling, when the mouse 
		// is on a Text with one or more of the following tags: SWT.READ_ONLY,
		// SWT.V_SCROLL or SWT.H_SCROLL.
		SmoothScroller.scrollSmooth(sc);
		
		// Register the context help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, Activator.PLUGIN_ID + ".view"); //$NON-NLS-1$
	}

	private void initKeystore() {
		try {
			IdentityManager iMgr = IdentityManager.getInstance();
			Vector<String> contactNames = iMgr.getContacts();
			if (!contactNames.contains(ALICE)) {
				// create Alice in the keystore
				iMgr.createIdentity(ALICE, Messages.ExtendedRSA_Visual_11, Messages.ExtendedRSA_Visual_12, 1024);
			}
			Vector<String> keyAlgos = iMgr.getAsymmetricKeyAlgorithms(ALICE);

			int count = 0;
			int count2 = 0;
			for (int i = 0; i < keyAlgos.size(); i++) {
				if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_13)) {
					count++;
				}
				if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_14)) {
					count2++;
				}
			}
			if (count == 0) {
				iMgr.createIdentity(ALICE, Messages.ExtendedRSA_Visual_15, Messages.ExtendedRSA_Visual_16, 1024);
			}
			if (count2 == 0) {
				iMgr.createIdentity(ALICE, Messages.ExtendedRSA_Visual_17, Messages.ExtendedRSA_Visual_18, 1024);
			}

			String[] alice_split = ALICE.split(BLANK);
			new Identity(tabFolder, SWT.NONE, new Contact(ALICE, alice_split[0], alice_split[1],
					Messages.ExtendedRSA_Visual_19, Messages.ExtendedRSA_Visual_20), txtExplain);

			if (!contactNames.contains(BOB)) {
				// create Bob in the keystore
				iMgr.createIdentity(BOB, Messages.ExtendedRSA_Visual_21, Messages.ExtendedRSA_Visual_22, 1024);
			}
			keyAlgos = iMgr.getAsymmetricKeyAlgorithms(BOB);

			count = 0;
			count2 = 0;
			for (int i = 0; i < keyAlgos.size(); i++) {
				if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_23)) {
					count++;
				}
				if (keyAlgos.get(i).startsWith(Messages.ExtendedRSA_Visual_24)) {
					count2++;
				}
			}
			if (count == 0) {
				// create an MpRSA-Key
				iMgr.createIdentity(BOB, Messages.ExtendedRSA_Visual_25, Messages.ExtendedRSA_Visual_26, 1024);
			}
			if (count2 == 0) {
				// create an RSA-Key
				iMgr.createIdentity(BOB, Messages.ExtendedRSA_Visual_27, Messages.ExtendedRSA_Visual_28, 1024);
			}

			String[] bob_split = BOB.split(BLANK);
			new Identity(tabFolder, SWT.NONE, new Contact(BOB, bob_split[0], bob_split[1],
					Messages.ExtendedRSA_Visual_29, Messages.ExtendedRSA_Visual_30), txtExplain);

		} catch (Exception e) {
			LogUtil.logError(e);
		}

	}

	@Override
	public void setFocus() {
		sc.setFocus();
	}
}
