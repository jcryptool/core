//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.jctca;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.visual.jctca.listeners.PluginBtnListener;
import org.jcryptool.visual.jctca.listeners.ResizeListener;
import org.jcryptool.visual.jctca.listeners.TabItemListener;
import org.jcryptool.visual.jctca.tabs.CertificationTab;
import org.jcryptool.visual.jctca.tabs.RegistrationTab;
import org.jcryptool.visual.jctca.tabs.SecondUserTab;
import org.jcryptool.visual.jctca.tabs.UserTab;

/**
 * 
 * This class implements the Certificate Authority visual for the JCrypTool.
 * 
 * @author Marco Macala, Kerstin Reisinger
 * 
 */

public class JCTCA_Visual extends ViewPart {
    private static final Color WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

    private Composite composite;
    private Composite head_composite;
    private StyledText head_description;
    private Composite comp_center;
    private TabFolder tabFolder;
    private StyledText stl_explain;
    private ScrolledComposite root;

    @Override
    public void createPartControl(Composite parent) {
        root = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        composite = new Composite(root, SWT.NONE);
        root.setContent(composite);
        root.setExpandHorizontal(true);
        root.setExpandVertical(true);

        composite.setLayout(new GridLayout(1, false));

        // Begin - headline area
        head_composite = new Composite(composite, SWT.NONE);
        head_composite.setBackground(WHITE);
        head_composite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        head_composite.setLayout(new GridLayout());

        Label headline = new Label(head_composite, SWT.NONE);
        headline.setFont(FontService.getHeaderFont());
        headline.setBackground(WHITE);
        // Set the headline text to the title of the plugin
        headline.setText(Messages.JCTCA_Visual_Plugin_Headline);
        head_description = new StyledText(head_composite, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
        GridData gd_head_description = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_head_description.widthHint = 700;
        head_description.setLayoutData(gd_head_description);
        // set the short introduction text for the certificate creation picture
        // because this is the first text that needs to be shown
        head_description.setText(Messages.JCTCA_Visual_archpic_create_text);
        head_description.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				//Resize the control when the text changes
				parent.layout(new Control[] {head_description});
			}
		});
        // End - Header
        showArchitecture();

    }

    /**
     * 
     * Displays the architecture pictures of jct-ca
     * 
     */
    public void showArchitecture() {
        comp_center = new Composite(composite, SWT.NONE);
        comp_center.setLayout(new GridLayout(1, false));
        comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        Composite comp_buttons = new Composite(comp_center, SWT.NONE);
        comp_buttons.setLayout(new GridLayout(4, false));
        GridData btns_ld = new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1);
        btns_ld.minimumHeight = 30;
        comp_buttons.setLayoutData(btns_ld);

        Composite comp_image = new Composite(comp_center, SWT.FILL);
        comp_image.setLayout(new GridLayout(1, false));
        comp_image.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        // set path_to_create_img and load image at that path into help
        String path_to_create_img = Platform.getNL().substring(0, 2);
        if (Platform.getNL().substring(0, 2).equals("de")) {
            path_to_create_img = "icons/de/minica_create.png"; //$NON-NLS-1$
        } else {
            path_to_create_img = "icons/en/minica_create.png"; //$NON-NLS-1$
        }
        Image help = Activator.getImageDescriptor(path_to_create_img).createImage();
        Label lbl_img = new Label(comp_image, SWT.WRAP | SWT.RESIZE);
        lbl_img.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        lbl_img.setImage(help);
        lbl_img.addControlListener(new ResizeListener(lbl_img, comp_image));
        Button btn_showCreate = new Button(comp_buttons, SWT.PUSH);
        btn_showCreate.setText(Messages.JCTCA_Visual_btn_show_archpic_create);
        btn_showCreate.setData(new Integer(0)); // set data for the listener -
                                                // see PluginBtnListener.java
        btn_showCreate.addSelectionListener(new PluginBtnListener(this, lbl_img, head_description));

        Button btn_showRevoke = new Button(comp_buttons, SWT.PUSH);
        btn_showRevoke.setText(Messages.JCTCA_Visual_btn_show_archpic_revoke);
        btn_showRevoke.setData(new Integer(1));
        btn_showRevoke.addSelectionListener(new PluginBtnListener(this, lbl_img, head_description));

        Button btn_showCheck = new Button(comp_buttons, SWT.PUSH);
        btn_showCheck.setText(Messages.JCTCA_Visual_btn_show_archpic_check);
        btn_showCheck.setData(new Integer(2));
        btn_showCheck.addSelectionListener(new PluginBtnListener(this, lbl_img, head_description));

        Button btn_continue = new Button(comp_buttons, SWT.PUSH);
        btn_continue.setText(Messages.JCTCA_Visual_btn_continue_to_plugin);
        btn_continue.setData(new Integer(3));
        btn_continue.addSelectionListener(new PluginBtnListener(this, lbl_img, head_description));
        composite.layout(true);
        
        root.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public void showCenter() {
    	
        comp_center = new Composite(composite, SWT.NONE);
        // 2 columns (tabs and explanation) --> new GridLayout(2, false);
        comp_center.setLayout(new GridLayout(2, false));
        comp_center.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        tabFolder = new TabFolder(comp_center, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Group grp_explain = new Group(comp_center, SWT.NONE);
        grp_explain.setLayout(new GridLayout(1, true));
        GridData gd_explain = new GridData(SWT.FILL, SWT.FILL, false, true);
        gd_explain.widthHint = 500;
        grp_explain.setLayoutData(gd_explain);
        grp_explain.setText(Messages.JCTCA_Visual_grp_explain_headline);

        // label for showing explanation texts
        stl_explain = new StyledText(grp_explain, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gd_txt_explain = new GridData(SWT.FILL, SWT.FILL, true, true);
        stl_explain.setLayoutData(gd_txt_explain);

        TabItemListener tabItemListener = new TabItemListener(tabFolder, grp_explain);
        tabFolder.addSelectionListener(tabItemListener);

        new UserTab(tabFolder, grp_explain, SWT.NONE);

        new RegistrationTab(tabFolder, grp_explain, SWT.NONE);
        
        new CertificationTab(tabFolder, grp_explain, SWT.NONE);

        new SecondUserTab(tabFolder, grp_explain, SWT.NONE);

        tabFolder.setSelection(0);
        composite.layout(true);
        
        root.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        
    }

    public void disposeCompCenter() {
        this.comp_center.dispose();
    }

    @Override
    public void setFocus() {
    	root.setFocus();
    }
}
