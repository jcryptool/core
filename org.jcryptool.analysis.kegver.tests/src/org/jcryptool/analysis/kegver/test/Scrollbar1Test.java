// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.kegver.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Scrollbar1Test {

	public static void main(String[] args) {
        Display display = new Display();
        final Shell shell = new Shell(display);
        FormLayout FormLayout = new FormLayout();
        FormLayout.marginWidth = 0;
        FormLayout.marginHeight = 0;
        shell.setLayout(FormLayout);

        final Text text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP
                        | SWT.READ_ONLY | SWT.V_SCROLL);
        text.setText("The quick brown fox jumps over the lazy dog and I struggle with SWT.");
        FormData FormData = new FormData(10, 10);
        text.setLayoutData(FormData);

        shell.setBounds(100, 100, 100, 80);
        shell.open();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                        display.sleep();
        }
        display.dispose();
}


}
