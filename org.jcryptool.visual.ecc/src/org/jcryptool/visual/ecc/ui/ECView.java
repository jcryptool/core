// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.visual.ecc.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.core.util.constants.IConstants;
import org.jcryptool.core.util.directories.DirectoryService;
import org.jcryptool.visual.ecc.ECCPlugin;
import org.jcryptool.visual.ecc.Messages;

public class ECView extends ViewPart {
    private Composite parent;
    private ECContentReal contentReal;
    private ECContentFp contentFp;
    private StackLayout layout;
    private ECContentFm contentFm;
    private ECContentLarge contentLarge;

    private String log = ""; //$NON-NLS-1$
    private String saveLocation;
    private String saveFileName = ""; //$NON-NLS-1$
    public boolean autoSave = false;
    public int saveTo = 0;
    private File logFile;
    private IWorkbenchPage editorPage;

    /**
     * The constructor.
     */
    public ECView() {
    }

    public String getFileName() {
        return saveFileName;
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {
        this.parent = parent;

        layout = new StackLayout();
        parent.setLayout(layout);

        contentReal = new ECContentReal(parent, SWT.NONE, this);
        contentFp = new ECContentFp(parent, SWT.NONE, this);
        contentFm = new ECContentFm(parent, SWT.NONE, this);
        contentLarge = new ECContentLarge(parent, SWT.NONE, this);

        layout.topControl = contentReal;

        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent.getShell(),
                "org.jcryptool.visual.ecc.eccview"); //$NON-NLS-1$

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(System.currentTimeMillis());
        log(Messages.getString("ECView.LogHeader1") + c.get(Calendar.DATE) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR) + " " + Messages.getString("ECView.LogHeader2") + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        parent.setFocus();
    }

    public void showReal() {
        if (!layout.topControl.equals(contentReal)) {
            contentReal.adjustButtons();
            layout.topControl = contentReal;
            parent.layout();
        }
    }

    public void showFp() {
        if (!layout.topControl.equals(contentFp)) {
            contentFp.adjustButtons();
            layout.topControl = contentFp;
            parent.layout();
        }
    }

    public void showFm() {
        if (!layout.topControl.equals(contentFm)) {
            contentFm.adjustButtons();
            layout.topControl = contentFm;
            parent.layout();
        }
    }

    public void showLarge() {
        if (!layout.topControl.equals(contentLarge)) {
            contentLarge.adjustButtons();
            layout.topControl = contentLarge;
            parent.layout();
        }
    }

    public void log(String s) {
        log += s + "\n"; //$NON-NLS-1$
        if (autoSave) {
            if (saveTo == 1) {// To text editor
                saveToEditor();
            } else if (saveTo == 2) {// to text file
                saveToFile();
            }
            log = ""; //$NON-NLS-1$
        }
    }

    public void saveLog() {
        if (saveTo == 1)
            saveToEditor();
        else if (saveTo == 2)
            saveToFile();

        if (saveTo != 0)
            log = ""; //$NON-NLS-1$
    }

    private void saveToEditor() {
        if (logFile == null) {
            logFile = new File(new File(DirectoryService.getTempDir()), "calculations.txt"); //$NON-NLS-1$ //$NON-NLS-2$
            logFile.deleteOnExit();
        }

        saveToFile();

        if (editorPage == null)
            editorPage = getSite().getPage();

        IEditorReference[] er = editorPage.getEditorReferences();
        for (int i = 0; i < er.length; i++) {
            if (er[i].getName().equals("calculations.txt")) { //$NON-NLS-1$
                er[i].getEditor(false).getSite().getPage().closeEditor(er[i].getEditor(false),
                        false);
            }
        }

        try {
            IPath location = new Path(logFile.getAbsolutePath());
            editorPage.openEditor(new PathEditorInput(location),
                    "org.jcryptool.editor.text.editor.JCTTextEditor"); //$NON-NLS-1$
        } catch (PartInitException e) {
            LogUtil.logError(ECCPlugin.PLUGIN_ID, e);
        }
    }

    private void saveToFile() {
        if (logFile == null) {
            selectFileLocation();
        } else {
            try {
                String[] sa = log.split("\n"); //$NON-NLS-1$
                if (sa.length > 1 || !sa[0].equals("")) { //$NON-NLS-1$
                    FileWriter fw = new FileWriter(logFile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    for (int i = 0; i < sa.length; i++) {
                        if (i < sa.length - 1 || (i == sa.length - 1 && !sa[i].equals(""))) { //$NON-NLS-1$
                            bw.write(sa[i]);
                            bw.newLine();
                        }
                    }
                    bw.close();
                    fw.close();
                }
            } catch (Exception ex) {
                LogUtil.logError(ECCPlugin.PLUGIN_ID, ex);
            }
        }
    }

    public void selectFileLocation() {
        FileDialog dialog = new FileDialog(layout.topControl.getShell(), SWT.SAVE);
        dialog.setFilterNames(new String[] {IConstants.TXT_FILTER_NAME, IConstants.ALL_FILTER_NAME});
        dialog.setFilterExtensions(new String[] {IConstants.TXT_FILTER_EXTENSION, IConstants.ALL_FILTER_EXTENSION});
        dialog.setFilterPath(DirectoryService.getUserHomeDir()); //$NON-NLS-1$
        dialog.setFileName("calculations.txt"); //$NON-NLS-1$
        dialog.setOverwrite(true);
        saveLocation = dialog.open();
        saveFileName = dialog.getFileName();

        if (saveLocation != null && saveFileName != null) {
            logFile = new File(saveLocation);
            saveToFile();
        }
    }
}