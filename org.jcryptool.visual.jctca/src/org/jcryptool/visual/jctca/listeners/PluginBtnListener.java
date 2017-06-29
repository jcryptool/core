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
package org.jcryptool.visual.jctca.listeners;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.visual.jctca.Activator;
import org.jcryptool.visual.jctca.JCTCA_Visual;
import org.jcryptool.visual.jctca.ResizeHelper;

/**
 * Listener Class for the buttons at the start of the plugin
 */
public class PluginBtnListener implements SelectionListener {
    private JCTCA_Visual visual;
    private Label lbl_img;
    private Image help;
    private StyledText exp;
    private String lang;

    public PluginBtnListener(JCTCA_Visual visual, Label lbl_img, StyledText exp) {
        this.visual = visual;
        this.lbl_img = lbl_img;
        this.exp = exp;
        lang = Platform.getNL().substring(0, 2);
        if (Platform.getNL().substring(0, 2).equals("de")) {
            lang = "de";
        } else {
            lang = "en";
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        Composite comp_image = lbl_img.getParent();
        // get the button that was pressed
        Button btn = (Button) arg0.getSource();
        // check which button it was: 0 = create, 1 = revoke, 2 = check, 3 = continue to plugin (see JCTCA_Visual.java)
        Integer data = (Integer) btn.getData();
        int pressed = data.intValue();
        ResizeHelper util = new ResizeHelper();

        // take action according to button pressed
        switch (pressed) {
            case 0: // data is set to 0 if create needs to be shown - see JCTCA_Visual.java
                help = Activator.getImageDescriptor("icons/" + lang + "/minica_create.png")//$NON-NLS-1$
                        .createImage();
                exp.setText(Messages.PluginBtnListener_archpic_create_explain);
                util.resize_image(lbl_img, comp_image, help);
                util.set_image_name("Architekturskizze Zertifikatserzeugung");//$NON-NLS-1$
                break;
            case 1: // data is set to 1 if revoke needs to be shown - see JCTCA_Visual.java
                help = Activator.getImageDescriptor("icons/" + lang + "/minica_revoke.png")//$NON-NLS-1$
                        .createImage();
                exp.setText(Messages.PluginBtnListener_archpic_revoke_explain);
                util.resize_image(lbl_img, comp_image, help);
                util.set_image_name("Architekturskizze Zertifikatswiderruf");//$NON-NLS-1$
                break;
            case 2: // data is set to 2 if check needs to be shown - see JCTCA_Visual.java
                help = Activator.getImageDescriptor("icons/" + lang + "/minica_check.png")//$NON-NLS-1$
                        .createImage();
                exp.setText(Messages.PluginBtnListener_archpic_check_explain);
                util.resize_image(lbl_img, comp_image, help);
                util.set_image_name("Architekturskizze Signaturpr\u00FCfung");//$NON-NLS-1$
                break;
            case 3: // is set to 3 if user wants to continue to the plugin - see JCTCA_Visual.java
                visual.disposeCompCenter();
                visual.showCenter();
                exp.setText(Messages.PluginBtnListener_visual_intro_text);
                break;
        }

    }

}
