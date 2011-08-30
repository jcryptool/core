//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2011 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.layer5;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.logging.utils.LogUtil;

public class KegverGui extends Composite {

    private Composite aKegverContent = null;

    public KegverGui(Composite parent, int style) {
        super(parent, style);
        initGUI();
    }

    private void initGUI() {
        try {
            FormLayout thisLayout = new FormLayout();
            setLayout(thisLayout);
            setSize(800, 620);

            Font system = getDisplay().getSystemFont();
            FontData base = system.getFontData()[0];
            base.setHeight(9);

            FontData normdat = new FontData(base.getName(), base.getHeight(),
                    base.getStyle());
            Font normfont = new Font(getDisplay(), normdat);

            getParent().setFont(normfont);

            this.setContent(new KegverContent(this, SWT.NONE));

            this.layout();
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }
    }

    /*
     * Getter and setter
     */

	private Composite setContent(Composite inContent) {
		this.aKegverContent = inContent;
		return this.getContent();
	}

	private Composite getContent() {
		return this.aKegverContent;
	}
}