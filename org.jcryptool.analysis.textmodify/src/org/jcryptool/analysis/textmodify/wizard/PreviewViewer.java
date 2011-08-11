// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.textmodify.wizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PreviewViewer extends org.eclipse.swt.widgets.Dialog {

    private Shell dialogShell;
    private Text text1;
    private String title, field1;
    private Label label1;
    private Button buttonClose;
    private Label label2;
    private Text text2;
    private Composite pageComposite;
    private String field2;

    public PreviewViewer(Shell parent) {
        super(parent, SWT.NONE);
    }

    public PreviewViewer(Shell parent, String pTitle, String pText) {
        super(parent, SWT.NONE);
        field1 = pText;
        title = pTitle;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public void setText(String pText, TransformData myTransformData) {
        field1 = pText;
        field2 = Transform.transformText(field1, myTransformData);
    }

    public void open() {
        Shell parent = getParent();
        dialogShell = new Shell(parent, SWT.DIALOG_TRIM);

        GridLayout dialogShellLayout = new GridLayout();
        dialogShell.setLayout(dialogShellLayout);
        dialogShell.setText(title);
        {
            GridData pageCompositeLData = new GridData();
            pageCompositeLData.grabExcessHorizontalSpace = true;
            pageCompositeLData.grabExcessVerticalSpace = true;
            pageCompositeLData.verticalAlignment = GridData.FILL;
            pageCompositeLData.horizontalAlignment = GridData.FILL;
            pageComposite = new Composite(dialogShell, SWT.NONE);
            GridLayout pageCompositeLayout = new GridLayout();
            pageCompositeLayout.numColumns = 2;
            pageCompositeLayout.makeColumnsEqualWidth = true;
            pageComposite.setLayout(pageCompositeLayout);
            pageComposite.setLayoutData(pageCompositeLData);
            {
                label1 = new Label(pageComposite, SWT.NONE);
                GridData label1LData = new GridData();
                label1LData.grabExcessHorizontalSpace = true;
                label1LData.horizontalAlignment = GridData.FILL;
                label1.setLayoutData(label1LData);
                label1.setText(Messages.PreviewViewer_original);
            }
            {
                label2 = new Label(pageComposite, SWT.NONE);
                GridData label2LData = new GridData();
                label2LData.grabExcessHorizontalSpace = true;
                label2LData.horizontalAlignment = GridData.FILL;
                label2.setLayoutData(label2LData);
                label2.setText(Messages.PreviewViewer_transformed);
            }
            {
                text1 = new Text(pageComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
                GridData text1LData = new GridData();
                text1LData.grabExcessHorizontalSpace = true;
                text1LData.grabExcessVerticalSpace = true;
                text1LData.verticalAlignment = GridData.FILL;
                text1LData.horizontalAlignment = GridData.FILL;
                text1LData.verticalIndent = 5;
                text1.setLayoutData(text1LData);
                text1.setText(field1);
                text1.setEditable(false);

                Listener listener = new Listener() {
                    int lastIndex = text1.getTopIndex();

                    public void handleEvent(Event e) {
                        int index = text1.getTopIndex();
                        if (index != lastIndex) {
                            lastIndex = index;
                            text2.setTopIndex(index);
                        }
                    }
                };

                ScrollBar vBar = text1.getVerticalBar();
                if (vBar != null) {
                    vBar.addListener(SWT.Selection, listener);
                }
            }
            {
                text2 = new Text(pageComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
                GridData text2LData = new GridData();
                text2LData.grabExcessHorizontalSpace = true;
                text2LData.grabExcessVerticalSpace = true;
                text2LData.verticalAlignment = GridData.FILL;
                text2LData.horizontalAlignment = GridData.FILL;
                text2LData.verticalIndent = 5;
                text2.setLayoutData(text2LData);
                text2.setText(field2);
                text2.setEditable(false);

                Listener listener = new Listener() {
                    int lastIndex = text2.getTopIndex();

                    public void handleEvent(Event e) {
                        int index = text2.getTopIndex();
                        if (index != lastIndex) {
                            lastIndex = index;
                            text1.setTopIndex(index);
                        }
                    }
                };

                ScrollBar vBar = text2.getVerticalBar();
                if (vBar != null) {
                    vBar.addListener(SWT.Selection, listener);
                }
            }
            {
            	buttonClose = new Button(pageComposite, SWT.PUSH | SWT.CENTER);
            	GridData buttonCloseLData = new GridData();
            	buttonCloseLData.grabExcessHorizontalSpace = true;
            	buttonCloseLData.horizontalAlignment = GridData.FILL;
            	buttonCloseLData.horizontalSpan = 2;
            	buttonClose.setLayoutData(buttonCloseLData);
            	buttonClose.setText(Messages.PreviewViewer_close);
            	buttonClose.addSelectionListener(new SelectionAdapter() {
            		public void widgetSelected(SelectionEvent evt) {
            			dialogShell.close();
            		}
            	});
            }
        }
        dialogShell.setSize(new Point(400, 500));
        dialogShell.layout();
        // dialogShell.pack();
        dialogShell
                .setLocation(getParent()
                        .toDisplay(
                                (int) Math.round((getParent().getBounds().width - dialogShell
                                        .getBounds().width) / 2), 10));
        dialogShell.open();
        Display display = dialogShell.getDisplay();
        while (!dialogShell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

}