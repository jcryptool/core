// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.crypto.classic.alphabets.preferences;

import java.util.Vector;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jcryptool.analysis.textmodify.wizard.ModifySelectionComposite;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then
 * you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of
 * Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO
 * JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class TransformationsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public final static String PREFID = AlphabetsPlugin.PLUGIN_ID;
    public final static String SUBNODE = "standdardtransformations"; //$NON-NLS-1$

    private ModifySelectionComposite composite1;
    private Composite pageComposite;
    private Group transformationGroup;
    private Composite composite2;
    private Combo alphabetCombo;

    private String[] alphabets;
    private TransformData[] preferenceSet;

    private String actualAlphabetName;
    private TransformData firstFormSetting;
    private Label label1;

    public TransformationsPreferencePage() {
        initializeAlphabets();
        loadPreferences();
    }

    public TransformationsPreferencePage(String title) {
        super(title);
        initializeAlphabets();
        loadPreferences();
    }

    public TransformationsPreferencePage(String title, ImageDescriptor image) {
        super(title, image);
        initializeAlphabets();
        loadPreferences();
    }

    private boolean nodeExists(Preferences myNode) {
        if (!myNode.get(TransformationPreferenceSet.ID_UPPC_ON, "default").equals("default")) //$NON-NLS-1$ //$NON-NLS-2$
            return true;
        return false;
    }

    public static TransformData getDataFromNode(Preferences myNode) {
        String[][] preferenceTemplate = TransformationPreferenceSet.getTemplate();
        for (int i = 0; i < TransformationPreferenceSet.PREFERENCE_COUNT; i++) {
            preferenceTemplate[i][1] = myNode.get(preferenceTemplate[i][0], "default"); //$NON-NLS-1$
        }

        TransformationPreferenceSet temp = TransformationPreferenceSet.fromStringArray(preferenceTemplate);
        TransformData result = temp.toTransformData();
        return result;
    }

    private void loadPreferences() {
        Preferences preferences = ConfigurationScope.INSTANCE.getNode(PREFID);
        Preferences mainnode = preferences.node(SUBNODE);

        for (int i = 0; i < alphabets.length; i++) {
            Preferences myNode = mainnode.node(alphabets[i]);
            if (nodeExists(myNode)) {
                preferenceSet[i] = getDataFromNode(myNode);
            } else {
                preferenceSet[i] = TransformationPreferenceSet.getDefaultSetting(alphabets[i]);
            }
        }

    }

    private void setPrefsToDefault() {
        Preferences preferences = ConfigurationScope.INSTANCE.getNode(PREFID);
        Preferences mainnode = preferences.node(SUBNODE);

        for (int i = 0; i < alphabets.length; i++) {
            Preferences myNode = mainnode.node(alphabets[i]);
            if (nodeExists(myNode)) {
                try {
                    myNode.clear();
                    preferences.flush();
                } catch (BackingStoreException e) {
                    LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, e);
                }
            }
        }

        loadPreferences();
        alphabetCombo.setItems(new String[0]);
        for (int i = 0; i < alphabets.length; i++)
            alphabetCombo.add(alphabets[i]);
        if (alphabets.length > 0) {
            alphabetCombo.setText(alphabets[0]);
            actualAlphabetName = alphabets[0];

            firstFormSetting = preferenceSet[0];
            actualAlphabetName = alphabets[0];

            composite1.setTransformData(firstFormSetting);
        }

        savePreferences();
    }

    private void savePreferences() {
        Preferences preferences = ConfigurationScope.INSTANCE.getNode(PREFID);
        Preferences mainnode = preferences.node(SUBNODE);
        TransformationPreferenceSet mySet;
        String[][] myPrefSaveArray;

        for (int i = 0; i < alphabets.length; i++) {
            Preferences myNode = mainnode.node(alphabets[i]);
            mySet = new TransformationPreferenceSet(preferenceSet[i], alphabets[i]);
            myPrefSaveArray = mySet.toStringArray();
            for (int k = 0; k < TransformationPreferenceSet.PREFERENCE_COUNT; k++) {
                myNode.put(myPrefSaveArray[k][0], myPrefSaveArray[k][1]);
            }
        }
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            LogUtil.logError(AlphabetsPlugin.PLUGIN_ID, e);
        }
    }

    private void initializeAlphabets() {
        Vector<String> myV = new Vector<String>();
        AbstractAlphabet[] alphas = AlphabetsManager.getInstance().getAlphabets();
        for (int i = 0; i < alphas.length; i++) {
            myV.addElement(alphas[i].getName());
        }
        alphabets = myV.toArray(new String[0]);

        preferenceSet = new TransformData[alphabets.length];
    }

    @Override
    protected Control createContents(Composite parent) {
        // GridLayout parentLayout = new GridLayout();
        // parentLayout.makeColumnsEqualWidth = true;
        // parent.setLayout(parentLayout);
        {
            GridData pageCompositeLData = new GridData();
            pageCompositeLData.grabExcessHorizontalSpace = true;
            pageCompositeLData.grabExcessVerticalSpace = true;
            pageCompositeLData.horizontalAlignment = GridData.FILL;
            pageCompositeLData.verticalAlignment = GridData.FILL;
            pageComposite = new Composite(parent, SWT.NONE);
            GridLayout pageCompositeLayout = new GridLayout();
            pageCompositeLayout.makeColumnsEqualWidth = true;
            pageCompositeLayout.marginWidth = 0;
            pageCompositeLayout.marginHeight = 0;
            pageComposite.setLayout(pageCompositeLayout);
            pageComposite.setLayoutData(pageCompositeLData);
            {
                GridData composite2LData = new GridData();
                composite2LData.grabExcessHorizontalSpace = true;
                composite2LData.horizontalAlignment = GridData.FILL;
                composite2 = new Composite(pageComposite, SWT.NONE);
                GridLayout composite2Layout = new GridLayout();
                composite2Layout.numColumns = 2;
                composite2Layout.marginBottom = 10;
                composite2.setLayout(composite2Layout);
                composite2.setLayoutData(composite2LData);
                {
                    label1 = new Label(composite2, SWT.NONE);
                    GridData label1LData = new GridData();
                    label1.setLayoutData(label1LData);
                    label1.setText(Messages.getString("TransformationsPreferencePage.0")); //$NON-NLS-1$
                }
                {
                    GridData alphabetComboLData = new GridData();
                    alphabetComboLData.grabExcessHorizontalSpace = true;
                    alphabetComboLData.horizontalAlignment = GridData.FILL;
                    alphabetCombo = new Combo(composite2, SWT.NONE);
                    alphabetCombo.setLayoutData(alphabetComboLData);
                    alphabetCombo.addSelectionListener(new SelectionAdapter() {
                        public void widgetSelected(SelectionEvent evt) {
                            boolean siteChanged = false;
                            if (alphabetCombo.getText() != actualAlphabetName) {
                                for (int i = 0; i < alphabets.length; i++) {
                                    if (alphabets[i].equals(actualAlphabetName) && !siteChanged) {
                                        preferenceSet[i] = composite1.getTransformData();
                                        for (int k = 0; k < alphabets.length; k++) {
                                            if (alphabets[k].equals(alphabetCombo.getText())) {
                                                composite1.setTransformData(preferenceSet[k]);
                                                actualAlphabetName = alphabets[k];
                                                siteChanged = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                    alphabetCombo.addModifyListener(new ModifyListener() {
                        public void modifyText(ModifyEvent evt) {

                        }
                    });

                    for (int i = 0; i < alphabets.length; i++)
                        alphabetCombo.add(alphabets[i]);
                    if (alphabets.length > 0) {
                        alphabetCombo.setText(alphabets[0]);
                        actualAlphabetName = alphabets[0];

                        firstFormSetting = preferenceSet[0];
                        actualAlphabetName = alphabets[0];
                    }
                }
            }
            {
                transformationGroup = new Group(pageComposite, SWT.NONE);
                GridLayout TransformationGroupLayout = new GridLayout();
                TransformationGroupLayout.makeColumnsEqualWidth = true;
                TransformationGroupLayout.marginLeft = 5;
                TransformationGroupLayout.marginBottom = 5;
                TransformationGroupLayout.marginTop = 5;
                transformationGroup.setLayout(TransformationGroupLayout);
                GridData TransformationGroupLData = new GridData();
                TransformationGroupLData.grabExcessHorizontalSpace = true;
                TransformationGroupLData.horizontalAlignment = GridData.FILL;
                TransformationGroupLData.verticalAlignment = GridData.FILL;
                transformationGroup.setLayoutData(TransformationGroupLData);
                transformationGroup.setText(Messages.getString("TransformationsPreferencePage.1")); //$NON-NLS-1$
                {
                    GridData composite1LData = new GridData();
                    composite1LData.grabExcessHorizontalSpace = true;
                    composite1LData.horizontalAlignment = GridData.FILL;
                    composite1LData.verticalAlignment = GridData.FILL;
                    composite1 = new ModifySelectionComposite(transformationGroup, SWT.NONE, firstFormSetting);
                    GridLayout composite1Layout1 = new GridLayout();
                    composite1Layout1.makeColumnsEqualWidth = true;
                    composite1Layout1.marginWidth = 0;
                    composite1Layout1.marginHeight = 0;
                    composite1.setLayout(composite1Layout1);
                    GridLayout composite1Layout = new GridLayout();
                    composite1Layout.makeColumnsEqualWidth = true;
                    composite1Layout.marginWidth = 0;
                    composite1.setLayoutData(composite1LData);
                }
            }
        }
        return null;
    }

    private void saveCurrentState() {
        for (int i = 0; i < alphabets.length; i++) {
            if (alphabets[i].equals(actualAlphabetName)) {
                preferenceSet[i] = composite1.getTransformData();
            }
        }
    }

    @Override
    protected void performApply() {
        saveCurrentState();
        savePreferences();

        performOk();
    }

    @Override
    public boolean performOk() {
        saveCurrentState();
        savePreferences();

        return true;
    }

    @Override
    protected void performDefaults() {
        setPrefsToDefault();
    }

    public void init(IWorkbench workbench) {

    }

}
