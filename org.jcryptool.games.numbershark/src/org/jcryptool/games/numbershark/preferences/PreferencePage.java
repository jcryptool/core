//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2008 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.games.numbershark.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jcryptool.games.numbershark.NumberSharkPlugin;

/**
 * @author Markus Schu
 * @author markus_schu@web.de
 * @version 0.0.11, 28.11.2008
 *
 *          This class represents a preference page that is contributed to the
 *          Preferences dialog. By subclassing
 *          <samp>FieldEditorPreferencePage</samp>, we can use the field support
 *          built into JFace that allows us to create a page that is small and
 *          knows how to save, restore and apply itself.
 *          <p>
 *          This page is used to modify preferences only. They are stored in the
 *          preference store that belongs to the main plug-in class. That way,
 *          preferences can be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	//private IntegerFieldEditor IFE_MAX;
	private RadioGroupFieldEditor RFE_NSGM;
	private RadioGroupFieldEditor RFE_NSPM;
	private BooleanFieldEditor BFE_SBI;

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(NumberSharkPlugin.getDefault().getPreferenceStore());
		setDescription("");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {

		addField(RFE_NSGM = new RadioGroupFieldEditor("Number Shark.NSGM",
				Messages.getString("l1_pp_text"), 1, new String[][] {
						{ Messages.getString("l2_pp_text"), "0" },
						{ Messages.getString("l3_pp_text"), "1" },
						{ Messages.getString("l4_pp_text"), "2" } }, super
						.getFieldEditorParent(), true));

		addField(RFE_NSPM = new RadioGroupFieldEditor("Number Shark.NSPM",
				Messages.getString("l5_pp_text"), 1, new String[][] {
						{ Messages.getString("l6_pp_text"), "0" },
						{ Messages.getString("l7_pp_text"), "1" },
						{ Messages.getString("l8_pp_text"), "2" } }, super
						.getFieldEditorParent(), true));

		addField(BFE_SBI = new BooleanFieldEditor("Number Shark.SBI", Messages
				.getString("l9_pp_text"), super.getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

		super.propertyChange(event);

		RFE_NSPM.store();
		RFE_NSGM.store();

		if (RFE_NSGM.getPreferenceStore().getInt("Number Shark.NSGM") > 0
				&& RFE_NSPM.getPreferenceStore().getInt("Number Shark.NSPM") > 0) {
			RFE_NSPM.getPreferenceStore().setValue("Number Shark.NSPM", 0);
			RFE_NSPM.load();
			RFE_NSPM.setEnabled(false, super.getFieldEditorParent());
		} else {
			RFE_NSPM.setEnabled(true, super.getFieldEditorParent());
		}
	}

	@Override
	protected void initialize() {

		super.initialize();

		if (RFE_NSGM.getPreferenceStore().getInt("Number Shark.NSGM") > 0
				&& RFE_NSPM.getPreferenceStore().getInt("Number Shark.NSPM") > 0) {
			RFE_NSPM.getPreferenceStore().setValue("Number Shark.NSPM", 0);
			RFE_NSPM.load();
			RFE_NSPM.setEnabled(false, super.getFieldEditorParent());
		} else {
			RFE_NSPM.setEnabled(true, super.getFieldEditorParent());
		}
	}

	@Override
	protected void performDefaults() {

		super.performDefaults();

		BFE_SBI.getPreferenceStore().setValue("Number Shark.SBI", true);
		RFE_NSGM.getPreferenceStore().setValue("Number Shark.NSGM", 0);
		RFE_NSPM.getPreferenceStore().setValue("Number Shark.NSPM", 2);
		//IFE_MAX.getPreferenceStore().setValue("Number Shark.MAX", 1024);

		BFE_SBI.store();
		RFE_NSPM.store();
		RFE_NSGM.store();
		//IFE_MAX.store();

		if (RFE_NSGM.getPreferenceStore().getInt("Number Shark.NSGM") > 0
				&& RFE_NSPM.getPreferenceStore().getInt("Number Shark.NSPM") > 0) {
			RFE_NSPM.getPreferenceStore().setValue("Number Shark.NSPM", 0);
			RFE_NSPM.load();
			RFE_NSPM.setEnabled(false, super.getFieldEditorParent());
		} else {
			RFE_NSPM.setEnabled(true, super.getFieldEditorParent());
		}
	}

	@Override
	protected void performApply() {

		super.performApply();

		if (RFE_NSGM.getPreferenceStore().getInt("Number Shark.NSGM") > 0
				&& RFE_NSPM.getPreferenceStore().getInt("Number Shark.NSPM") > 0) {
			RFE_NSPM.getPreferenceStore().setValue("Number Shark.NSPM", 0);
			RFE_NSPM.load();
			RFE_NSPM.setEnabled(false, super.getFieldEditorParent());
		} else {
			RFE_NSPM.setEnabled(true, super.getFieldEditorParent());
		}
	}

	@Override
	public boolean performOk() {
		BFE_SBI.store();
		RFE_NSPM.store();
		RFE_NSGM.store();
		//IFE_MAX.store();

		return super.performOk();
	}
}