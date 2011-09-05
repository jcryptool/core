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
package org.jcryptool.visual.PairingBDII.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {
    private IntroductionAndParameters situation;
    private Illustration illustration;
    private DefinitionAndDetails protocol;
    private Logging tryagain;
    private ScrolledComposite scrolledComposite;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());

        scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);

        final Composite scrolledContent = new Composite(scrolledComposite, SWT.NONE);
        scrolledContent.setLayout(new GridLayout(2, false));
        situation = new IntroductionAndParameters(scrolledContent);
        illustration = new Illustration(scrolledContent);
        protocol = new DefinitionAndDetails(scrolledContent);
        tryagain = new Logging(scrolledContent);

        Model.getDefault().setLinks(situation, illustration, protocol, tryagain);
        Model.getDefault().setNumberOfUsers(4);
        Model.getDefault().reset();
        Model.getDefault().setupStep1();

        scrolledComposite.setContent(scrolledContent);
        scrolledComposite.setMinSize(scrolledContent.computeSize(800, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        scrolledComposite.getVerticalBar().setIncrement(20);
        scrolledComposite.getVerticalBar().setPageIncrement(250);
        scrolledComposite.layout();
    }

    @Override
    public void setFocus() {
        scrolledComposite.setFocus();
    }
}