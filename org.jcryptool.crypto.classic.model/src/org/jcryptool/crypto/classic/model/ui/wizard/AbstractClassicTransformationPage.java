//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2010 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.classic.model.ui.wizard;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.analysis.textmodify.wizard.ModifySelectionComposite;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.crypto.classic.alphabets.preferences.TransformationPreferenceSet;
import org.jcryptool.crypto.classic.alphabets.preferences.TransformationsPreferencePage;
import org.jcryptool.crypto.classic.model.ClassicCryptoModelPlugin;
import org.osgi.service.prefs.Preferences;

public class AbstractClassicTransformationPage extends WizardPage {

	private ModifySelectionComposite transformComposite;
	private TransformData firstTransformData;
	private boolean didCreate = false;
	//private Label infoLabel;

	/**
	 * Creates a new instance of AbstractClassicTransformationPage
	 */
	public AbstractClassicTransformationPage() {
		super("", "", null); //$NON-NLS-1$ //$NON-NLS-2$
		setTitle(Messages.AbstractClassicTransformationPage_pageTitle);
		setMessage(Messages.AbstractClassicTransformationPage_pageMessage);
	}

	/**
	 * Creates a new instance of AbstractClassicTransformationPage, defining its window title.
	 * @param title the window title
	 */
	public AbstractClassicTransformationPage(String title) {
		super("", title, null); //$NON-NLS-1$
		setTitle(title);
		setMessage(Messages.AbstractClassicTransformationPage_pageMessage);
	}

	/**
	 * Creates a new instance of AbstractClassicTransformationPage, defining its window title and message.
	 * @param title the window title
	 * @param message the message of the page
	 */
	public AbstractClassicTransformationPage(String title, String message) {
		super("", title, null); //$NON-NLS-1$
		setTitle(title);
		setMessage(message);
	}

	public void createControl(Composite parent) {
		Composite pageComposite = new Composite(parent, SWT.NULL);
			GridData pageCompositeLayoutData = new GridData();
			GridLayout pageCompositeLayout = new GridLayout();
			pageCompositeLayoutData.grabExcessHorizontalSpace = true; pageCompositeLayoutData.grabExcessVerticalSpace = true;
			pageCompositeLayoutData.horizontalAlignment = SWT.FILL; pageCompositeLayoutData.verticalAlignment = SWT.FILL;
			pageComposite.setLayout(pageCompositeLayout);
			pageComposite.setLayoutData(pageCompositeLayoutData);

			transformComposite = new ModifySelectionComposite(pageComposite, SWT.NONE);
				GridData composite1LData = new GridData();
				composite1LData.grabExcessHorizontalSpace = true;
				composite1LData.horizontalAlignment = GridData.FILL;
				composite1LData.verticalAlignment = GridData.FILL;
				GridLayout composite1Layout1 = new GridLayout();
				composite1Layout1.makeColumnsEqualWidth = true;
				composite1Layout1.marginWidth = 0;
				composite1Layout1.marginHeight = 0;
				transformComposite.setLayout(composite1Layout1);
				GridLayout composite1Layout = new GridLayout();
				composite1Layout.makeColumnsEqualWidth = true;
				composite1Layout.marginWidth = 0;
				transformComposite.setLayoutData(composite1LData);

				if(firstTransformData != null) transformComposite.setTransformData(firstTransformData);
				didCreate=true;

			/**infoLabel = new Label(pageComposite, SWT.NONE);
				GridData label2LData = new GridData();
				label2LData.grabExcessHorizontalSpace = false;
				label2LData.grabExcessVerticalSpace = false;
				label2LData.horizontalAlignment = GridData.FILL;
				label2LData.verticalAlignment = GridData.END;
				label2LData.verticalIndent = 10;
				infoLabel.setLayoutData(label2LData);
				infoLabel.setText("You can change the standard transformations in: Cryptography->Alphabets->Default transformations");
					"Kryptographie->Alphabete->Standard-Transformationen"
					"Cryptography->Alphabets->Default transformations"
				**/

		setControl(pageComposite);
		setPageComplete(true);

		setHelpAvailable();
	}

	/**
	 * Subclasses should override this procedure to set the Help available flag and id, like
	 * PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), DelastellePlugin.PLUGIN_ID + ".delastelleWizard");
	 *
	 */
	private void setHelpAvailable() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), ClassicCryptoModelPlugin.PLUGIN_ID + ".wizard"); //$NON-NLS-1$
	}

	/** load the standard Transformation for a specified currentAlphabet from the global settings.
	 * @param alphaName the name of the currentAlphabet
	 * @return the Transformation
	 */
	public static TransformData getTransformFromName(String alphaName) {
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(TransformationsPreferencePage.PREFID);
		Preferences mainnode = preferences.node(TransformationsPreferencePage.SUBNODE);
		Preferences myNode = mainnode.node(alphaName);
		if(nodeExists(myNode)) {
			TransformData loadedPreferenceSet = TransformationsPreferencePage.getDataFromNode(myNode);
			return loadedPreferenceSet;
		}
		else return TransformationPreferenceSet.getDefaultSetting(alphaName);
	}

	/** Loads a specific transformation set in the wizard.
	 * @param in the transformation
	 */
	public void setTransformData(TransformData in) {
		if(didCreate) this.transformComposite.setTransformData(in);
		else this.firstTransformData = in;
	}

	/** return the wizard's selection as a transformation
	 * @return
	 */
	public TransformData getTransformData() {
		return transformComposite.getTransformData();
	}

	/** prooves, if a specific node in the transformation settings exists
	 * @param myNode parent node
	 * @return whether the node exists or not
	 */
	private static boolean nodeExists(Preferences myNode) {
		if(! myNode.get(TransformationPreferenceSet.ID_UPPC_ON, "default").equals("default")) return true; //$NON-NLS-1$ //$NON-NLS-2$
		return false;
	}

}
