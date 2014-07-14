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
package org.jcryptool.webbrowser.ui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * Shows the WebBrowser browser and controls
 *
 * @author mwalthart
 *
 */
public class BrowserView extends ViewPart {
    protected static final String BROWSER_HOME = "http://www.cryptool.org"; //$NON-NLS-1$
    private static Browser browser;
    private Controls controls;
    private Text status;
    private HashMap<String, String> history = new HashMap<String, String>();

    @Override
    public void createPartControl(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 2;
        parent.setLayout(gridLayout);

        controls = new Controls(parent, SWT.NONE, this);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.heightHint = 28;
        controls.setLayoutData(gridData);

        browser = new Browser(parent, SWT.NONE);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setUrl(BROWSER_HOME);
        browser.addLocationListener(new LocationListener() {
            public void changed(LocationEvent event) {
                history.put(browser.getUrl(), browser.getUrl());
                controls.getUrlField().setItems(history.values().toArray(new String[0]));
                controls.getUrlField().setText(browser.getUrl());
                controls.getBackButton().setEnabled(browser.isBackEnabled());
                controls.getForwardButton().setEnabled(browser.isForwardEnabled());
                controls.animateReloadButton(false);
            }

            public void changing(LocationEvent event) {
                controls.animateReloadButton(true);
            }
        });
        browser.addStatusTextListener(new StatusTextListener() {
            public void changed(StatusTextEvent event) {
                status.setText(event.text);
            }
        });
        status = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.SINGLE);
        status.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
                "org.jcryptool.webbrowser.webBrowserView"); //$NON-NLS-1$
    }

    @Override
    public void setFocus() {
        browser.setFocus();
    }

    /**
     * returns the current browser
     *
     * @return the current browser
     */
    public Browser getBrowser() {
        return browser;
    }

    /**
     * opens the given url in the browser
     *
     * @param url the url to open
     */
    public static void openUrl(String url) {
        browser.setUrl(url);
    }

    @Override
    public void dispose() {
        if (browser != null) {
            browser.dispose();
        }
        super.dispose();
    }
}
