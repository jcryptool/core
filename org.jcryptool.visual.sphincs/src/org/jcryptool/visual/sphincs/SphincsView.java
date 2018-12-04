//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.sphincs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.visual.sphincs.algorithm.Signature;
import org.jcryptool.visual.sphincs.algorithm.aSPHINCS256;
import org.jcryptool.visual.sphincs.algorithm.bcSPHINCS256;
import org.jcryptool.visual.sphincs.ui.SphincsConstant;
import org.jcryptool.visual.sphincs.ui.SphincsTreeView;
import org.jcryptool.visual.sphincs.ui.SphincsKeyGenerationView;
import org.jcryptool.visual.sphincs.ui.SphincsSignVerifyView;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;


/**
 * Main GUI class, providing the basic composite, managing tabs and calling
 * other GUI classes
 * 
 * @author Philipp Guggenberger
 * 
 * 
 */
public class SphincsView extends ViewPart {

    
    public SphincsView() {
        
    }
   
    private Composite parent;
    aSPHINCS256 bcSphincs;
    private Signature sphincsSig;
        
    
    // TabItems in TabFolder
    private TabItem tbtmParameter0;
    private TabItem tbtmParameter1;
    private TabItem tbtmParameter2;
        
    // Tab-Handling
    private SphincsKeyGenerationView keyTab;                        // tab 1 Key-Generation  
    public SphincsSignVerifyView signatureTab;                     // Tab 2 Sign and Verify
    public SphincsTreeView descriptionTab;                         // Tab 3 Sphincs-Description
    private ViewPart masterView;
    private ScrolledComposite scrolledComposite;
    private TabFolder tabFolder;                                    // TabFolder on ScrolledComposite
    private int previousTab = 0;
    private boolean mustCreateTab[];
    private Shell shell;
    
  
    

    @Override
    public void createPartControl(Composite parent) {
        
        this.parent = parent;
        masterView = this;
        mustCreateTab = new boolean[4];
        shell = parent.getShell();
        
        bcSphincs = new bcSPHINCS256();
               
        
        // makes the connection to the help of the plug-in
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(), "org.jcryptool.visual.sphincs.sphincsview");
        
        // Main Composite
        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        // Tab Folder to put TabItems in there
        tabFolder = new TabFolder(scrolledComposite, SWT.NONE);
        
        // Tab 1: Key and Bitsmask
        tbtmParameter0 = new TabItem(tabFolder, SWT.NONE);
        tbtmParameter0.setText(SphincsDescriptions.SphincsTab_0);
        keyTab = new SphincsKeyGenerationView(tabFolder,SWT.NONE, masterView, bcSphincs);
        tbtmParameter0.setControl(keyTab);
                  
        // Tab 2 Sign
        tbtmParameter1 = new TabItem(tabFolder, SWT.NONE);
        tbtmParameter1.setText(SphincsDescriptions.SphincsTab_1);
        
        // Tab 3 Sphincs-DescriptionView
        tbtmParameter2 = new TabItem(tabFolder, SWT.NONE);
        tbtmParameter2.setText(SphincsDescriptions.SphincsTab_2);
        
        // Begin User Tab Listener
        // gets called when a user clicks a tab
        tabFolder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                int select = tabFolder.getSelectionIndex();
               
                tabFolder.setSelection(select);
                setTab(select);
            }               
        });
        scrolledComposite.setContent(tabFolder);
        scrolledComposite.setMinSize(SphincsConstant.PLUGIN_WIDTH, SphincsConstant.PLUGIN_HEIGTH);
    }

    /**
     * Switches to the given tab
     * 
     * @param tab
     */
    public void setTab(int tab) { 
        switch (tab) {
         case 0:         
           tbtmParameter0.setControl(keyTab);
           previousTab = 0;
           break;
        case 1:
            if(keyTab.getUpdatedKey()) {
                resetTabs();
                keyTab.setUpdatedKey(false);
            }
            
            if (signatureTab == null || mustCreateTab[1]) {            
                signatureTab = new SphincsSignVerifyView(tabFolder, SWT.NONE, bcSphincs);
                mustCreateTab[1] = false;
            }
   
            tbtmParameter1.setControl(signatureTab);
            previousTab = 1;
            break;   
        case 2:
            if (signatureTab == null) { 
                showMessageBox();
                tabFolder.setSelection(previousTab);
                tab = previousTab;
                break;
            }
            else if (descriptionTab == null || mustCreateTab[2]) {
                sphincsSig = signatureTab.getSphincsSignatur();
                        
                if (sphincsSig != null) {
                    descriptionTab = new SphincsTreeView(tabFolder, SWT.NONE, masterView, bcSphincs, sphincsSig);
                    mustCreateTab[2] = false;  
                    }
                else {
                    showMessageBox();
                    tabFolder.setSelection(previousTab);
                    tab = previousTab;
                }                    
            }
            
            tbtmParameter2.setControl(descriptionTab);
            previousTab = 2;
            break;
            
        default:
            break;
        }

        tabFolder.setSelection(tab);
    }
    
    @Override
    public void setFocus() {
       
        
    }
    /**
     * resets the view, it's needed by JCrypTool
     */
    public void resetView() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();

        reset();
    }


    /**
     * For the reset button
     */
    public void reset() {
        Control[] children = parent.getChildren();
        for (Control control : children) {
            control.dispose();
        }
        createPartControl(parent);
        parent.layout();
        resetTabs();
        
    }
    
  private void resetTabs() {
      signatureTab = null;
      descriptionTab = null;
  }
  
  private void showMessageBox() {
      MessageBox messageBoxx = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
      messageBoxx.setMessage(SphincsDescriptions.SignatureMessageBoxText);
      messageBoxx.setText("Info");
      messageBoxx.open();
  }
    
}