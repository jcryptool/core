//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2012, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.crypto.ui.textsource;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.IOperationsConstants;
import org.jcryptool.core.operations.OperationsPlugin;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.core.operations.util.PathEditorInput;
import org.jcryptool.crypto.ui.CryptoUIPlugin;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

public class TextInputWithSourceDisplayer extends Composite {
    private static final String FILE_BY_CLICKING_ON_THE_ICON = Messages.TextInputWithSourceDisplayer_fileOpenHint;
    private static final String EDITOR_BY_CLICKING_ON_THE_ICON = Messages.TextInputWithSourceDisplayer_editorOpenHint;
    private static final String USERINPUT_BY_CLICKING_ON_THE_ICON = Messages.TextInputWithSourceDisplayer_userinputOpenHint;
    private static ImageRegistry imageRegistry;
    private Label lblPrimaryInputDescription;
    private TextInputWithSource text;
    private Style displayStyle;
    private Composite layoutRoot;
    private Label lblPrimaryInputType;
    private MouseListener primaryIconSelectionListener;
    private Composite originComposite;
    private TextInputWithSourceDisplayer manualOriginDisplayer;
    private Label lblOriginCaption;
    private Label lblOriginCaptionEnd;

    public static class Style {
        boolean images;
        boolean showFrontLabel;

        public Style(boolean images, boolean showFrontLabel) {
			this.images = images;
			this.showFrontLabel = showFrontLabel;

        }

        public Style(boolean images) {
            this(images, true);
        }

    }

    /**
     * @param parent the parent
     * @param layoutRoot the composite which is used to layout when hiding / showing controls. must be "big" enough,
     *        that it itself won't need to resize.
     * @param displayStyle the display style (see {@link Style})
     * @wbp.parser.constructor
     */
    public TextInputWithSourceDisplayer(Composite parent, Composite layoutRoot, TextInputWithSource initialInput,
            Style displayStyle) {
        this(parent, layoutRoot, initialInput, displayStyle, true);
    }

    private TextInputWithSourceDisplayer(Composite parent, Composite layoutRoot, TextInputWithSource initialInput,
            Style displayStyle, boolean nesting) {
        super(parent, SWT.NONE);
        this.layoutRoot = layoutRoot;
        this.displayStyle = displayStyle;
        this.text = initialInput;
        GridLayout layout = new GridLayout(3, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        setLayout(layout);
        {
            // for displaying either an icon (if this is set in the style) or
            // textual description of the input type.
            lblPrimaryInputType = new Label(this, SWT.NONE);
            GridData lblPrimaryInputTypeLData = new GridData(SWT.FILL, SWT.CENTER, false, false);
            lblPrimaryInputType.setLayoutData(lblPrimaryInputTypeLData);
        }
        {
            // for displaying the "data" part: the inputs source
            // (file/editor/manual..)
            // (and the manual input source if this is the case)
            lblPrimaryInputDescription = new Label(this, SWT.NONE);
            GridData lblPrimaryInputDescriptionLData = new GridData(SWT.FILL, SWT.CENTER, true, false);
            lblPrimaryInputDescriptionLData.horizontalSpan = 1;
            lblPrimaryInputDescriptionLData.horizontalIndent = 5;
            lblPrimaryInputDescription.setLayoutData(lblPrimaryInputDescriptionLData);
            lblPrimaryInputDescription.setText(""); //$NON-NLS-1$
        }
        // more than one nesting is not allowed (hence this private constructor)
        if (nesting) {
            originComposite = new Composite(this, SWT.NONE);
            GridLayout originCompositeLayout = new GridLayout(3, false);
            originCompositeLayout.marginHeight = 0;
            originCompositeLayout.marginWidth = 0;
            originCompositeLayout.horizontalSpacing = 0;
            originComposite.setLayout(originCompositeLayout);
            GridData originCompositeLayoutData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
            originCompositeLayoutData.horizontalIndent = 5;
            originComposite.setLayoutData(originCompositeLayoutData);
            {
                lblOriginCaption = new Label(originComposite, SWT.NONE);
                lblOriginCaption.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
                lblOriginCaption.setText(Messages.TextInputWithSourceDisplayer_originLabelBeginning);
            }
            {
                // displaying the manual input origin composite
                manualOriginDisplayer = new TextInputWithSourceDisplayer(originComposite, layoutRoot, initialInput,
                        displayStyle, false);
                GridData manualOriginDisplayerLData = new GridData(SWT.FILL, SWT.CENTER, true, false);
                // manualOriginDisplayerLData.horizontalIndent = 5;
                manualOriginDisplayerLData.horizontalSpan = 1;
                manualOriginDisplayer.setLayoutData(manualOriginDisplayerLData);
            }
            {
                lblOriginCaptionEnd = new Label(originComposite, SWT.NONE);
                lblOriginCaptionEnd.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
                lblOriginCaptionEnd.setText(")"); //$NON-NLS-1$
            }
        }

        refreshDisplay();
    }

    private static boolean isDisplayOrigin(TextInputWithSource text) {
        return text.getSourceType() == TextSourceType.USERINPUT && text.userInputOrigin != null;
    }

    /**
     * Returns the icon for the file type with the specified extension.
     *
     * @param extension
     * @return
     */
    private static Image getIcon(File file) {
        String extension = getExtensionFor(file);

        if (imageRegistry == null)
            imageRegistry = new ImageRegistry();
        Image image = imageRegistry.get(extension);
        if (image != null)
            return image;

        Program program = Program.findProgram(extension);
        ImageData imageData = (program == null ? null : program.getImageData());
        if (imageData != null) {
            image = new Image(Display.getCurrent(), imageData);
            imageRegistry.put(extension, image);
        }

        return image;
    }

    private static String getExtensionFor(File file) {
        String filename = file.getName();
        int index = -1;
        String residualName = filename;
        while ((index = residualName.indexOf('.')) > 0) {
            residualName = residualName.substring(index + 1);
        }
        if (residualName.equals(filename))
            return ""; //$NON-NLS-1$
        return residualName;

    }

    private static Image generateImageFor(TextInputWithSource text) {
        if (text.getSourceType() == TextSourceType.FILE) {
            if (text.file != null) {
                Image generatedImageFromExtension = getIcon(text.file);
                if (generatedImageFromExtension != null) {
                    return generatedImageFromExtension;
                } else {
                    return CryptoUIPlugin.getDefault().getImageRegistry()
                            .get(CryptoUIPlugin.FILE_INPUT_ICON);
                }
            } else {
                return CryptoUIPlugin.getDefault().getImageRegistry()
                        .get(CryptoUIPlugin.FILE_INPUT_ICON);
            }
        } else if (text.getSourceType() == TextSourceType.JCTEDITOR) {
            if (text.editorReference != null) {
                return text.editorReference.getTitleImage();
            } else {
                return JCTTextEditorPlugin.getDefault().getImageRegistry()
                        .get(JCTTextEditorPlugin.JCT_TEXT_EDITOR_ICON);
            }
        } else { // USERINPUT
            return CryptoUIPlugin.getDefault().getImageRegistry()
                    .get(CryptoUIPlugin.KEYBOARD_INPUT_ICON);
        }
    }

    private static String generateTooltipForPrimaryTypeLabel(TextInputWithSource textInput, Style displayStyle) {
        /*
         * general policy: images: path + onclick hinweis textual: path
         */

        // Tooltip for description part in case of file source
        String openFileHintString = FILE_BY_CLICKING_ON_THE_ICON;
        if (textInput.getSourceType() == TextSourceType.FILE) {
            String descriptionTooltipMask;
            if (textInput.file != null) { // text file set -> opening possible
                String filepath = textInput.file.getAbsolutePath();
                if (displayStyle.images) { // images -> clickbtn
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, filepath, openFileHintString);
                } else {
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, filepath);
                }
            } else { // no file set (but text must always be set -> opening
                     // possible still)
                if (displayStyle.images) { // images -> clickbtn
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, openFileHintString);
                } else {
                    return null; // no tooltip here ^^ cause no path found.
                }
            }

        }

        // Tooltip for description part in case of file source
        String openEditorHintString = EDITOR_BY_CLICKING_ON_THE_ICON;
        if (textInput.getSourceType() == TextSourceType.JCTEDITOR) {
            String descriptionTooltipMask;
            if (textInput.editorReference != null) { // text file set -> opening
                // possible
                String editorTitle = textInput.editorReference.getTitle();
                if (displayStyle.images) { // images -> clickbtn
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, editorTitle, openEditorHintString);
                } else {
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, editorTitle);
                }
            } else { // no file set (but text must always be set -> opening
                     // possible still)
                if (displayStyle.images) { // images -> clickbtn
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, openEditorHintString);
                } else {
                    return null; // no tooltip here ^^ cause no editor found
                }
            }
        }

        // Tooltip for description part in case of file source
        String openUserInputHintString = USERINPUT_BY_CLICKING_ON_THE_ICON;
        if (textInput.getSourceType() == TextSourceType.USERINPUT) {
            String descriptionTooltipMask;
            String title = Messages.TextInputWithSourceDisplayer_manualinput;
            if (textInput.userInputOrigin != null) { // origin exists
                if (displayStyle.images) { // images -> clickbtn
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, title, openUserInputHintString);
                } else {
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, title);
                }
            } else { // no origin exists
                if (displayStyle.images) { // images -> clickbtn
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, title, openUserInputHintString);
                } else {
                    return null; // no tooltip here ^^ origin not found and no
                                 // button
                }
            }
        }

        // default (though every case should be covered)
        return null;
    }

    private static String generateTooltipForPrimaryDescriptionLabel(TextInputWithSource text, Style displayStyle) {
        /*
         * general policy: images: path + onclick hinweis textual: path
         */

        // Tooltip for description part in case of file source
        String openFileHintString = FILE_BY_CLICKING_ON_THE_ICON;
        if (text.getSourceType() == TextSourceType.FILE) {
            String descriptionTooltipMask;
            if (text.file != null) { // text file set -> opening possible
                String filepath = text.file.getAbsolutePath();
                if (displayStyle.images) { // there is a click btn next to this
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, filepath, openFileHintString);
                } else {
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, filepath);
                }
            } else { // no file set (but text must always be set -> opening
                     // possible still)
                if (displayStyle.images) { // there is a click btn next to this
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, openFileHintString);
                } else {
                    return null; // no tooltip here ^^ cause no path found.
                }
            }

        }

        // Tooltip for description part in case of file source
        String openEditorHintString = EDITOR_BY_CLICKING_ON_THE_ICON;
        if (text.getSourceType() == TextSourceType.JCTEDITOR) {
            String descriptionTooltipMask;
            if (text.editorReference != null) { // text file set -> opening
                                                // possible
                String editorTitle = text.editorReference.getTitle();
                if (displayStyle.images) { // there is a click btn next to this
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, editorTitle, openEditorHintString);
                } else {
                    // here is only to display the editor title which is already
                    // done on the label
                    return null;
                    // descriptionTooltipMask = "%s";
                    // return String.format(descriptionTooltipMask,
                    // editorTitle);
                }
            } else { // no file set (but text must always be set -> opening
                     // possible still)
                if (displayStyle.images) { // there is a click btn next to this
                    descriptionTooltipMask = "%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, openEditorHintString);
                } else {
                    return null; // no tooltip here ^^ cause no editor found
                }
            }
        }

        // Tooltip for description part in case of file source
        String openUserInputHintString = USERINPUT_BY_CLICKING_ON_THE_ICON;
        if (text.getSourceType() == TextSourceType.USERINPUT) {
            String descriptionTooltipMask;
            String title = Messages.TextInputWithSourceDisplayer_manualinput;
            if (text.userInputOrigin != null) { // origin exists
                if (displayStyle.images) { // there is a click btn next to this
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, title, openUserInputHintString);
                } else {
                    // would only show "manual input"
                    return null;
                    // descriptionTooltipMask = "%s";
                    // return String.format(descriptionTooltipMask, title);
                }
            } else { // no origin exists
                if (displayStyle.images) { // there is a click btn next to this
                    descriptionTooltipMask = "%s\n%s"; //$NON-NLS-1$
                    return String.format(descriptionTooltipMask, title, openUserInputHintString);
                } else {
                    return null; // no tooltip here ^^ origin not found and no
                                 // button
                }
            }
        }

        // default (though every case should be covered)
        return null;
    }

    private static String generatePrimarySourceTypeString(TextInputWithSource textInput) {
        return generateGeneralInputTypeString(textInput.getSourceType()) + ":"; //$NON-NLS-1$
    }

    private static String generatePrimaryDescriptionString(TextInputWithSource textInput) {
        if (textInput.getSourceType() == TextSourceType.USERINPUT) {
            return Messages.TextInputWithSourceDisplayer_manualinput;
        } else if (textInput.getSourceType() == TextSourceType.JCTEDITOR) {
            if (textInput.editorReference != null) {
                return textInput.editorReference.getTitle();
            } else {
                return ""; //$NON-NLS-1$
            }
        } else { // if(textInput.getSourceType() == TextSourceType.FILE) {
            if (textInput.file != null) {
                return textInput.file.getName();
            } else {
                return ""; //$NON-NLS-1$
            }
        }
    }

    private static String generateGeneralInputTypeString(TextSourceType sourceType) {
        if (sourceType == TextSourceType.USERINPUT) {
            return "Manual input"; //$NON-NLS-1$
        } else if (sourceType == TextSourceType.JCTEDITOR) {
            return "editor"; //$NON-NLS-1$
        } else { // if(textInput.getSourceType() == TextSourceType.FILE) {
            return "file"; //$NON-NLS-1$
        }
    }

    public void setText(TextInputWithSource t) {
        this.text = t;
        refreshDisplay();
    }

    private void refreshDisplay() {
        List<Control> controlsToLayout = new LinkedList<Control>();
        lblPrimaryInputType.setToolTipText(null);
        lblPrimaryInputDescription.setToolTipText(null);
        lblPrimaryInputType.setText(""); //$NON-NLS-1$
        lblPrimaryInputType.setImage(null);
        lblPrimaryInputType.setCursor(null);

        lblPrimaryInputDescription.setToolTipText(generateTooltipForPrimaryDescriptionLabel(text, displayStyle));
        lblPrimaryInputType.setToolTipText(generateTooltipForPrimaryTypeLabel(text, displayStyle));

        if (displayStyle.images) {
            lblPrimaryInputType.setCursor(new Cursor(this.getDisplay(), SWT.CURSOR_HAND));
            lblPrimaryInputType.setText(""); //$NON-NLS-1$
            lblPrimaryInputType.setToolTipText(generateTooltipForPrimaryTypeLabel(text, displayStyle));
            lblPrimaryInputType.setImage(generateImageFor(text));

            if (primaryIconSelectionListener == null) {
                primaryIconSelectionListener = new MouseListener() {
                    @Override
                    public void mouseUp(MouseEvent e) {
                    }

                    @Override
                    public void mouseDown(MouseEvent e) {
                        doPrimaryClickActionFor(text);
                    }

                    @Override
                    public void mouseDoubleClick(MouseEvent e) {
                    }
                };
                lblPrimaryInputType.addMouseListener(primaryIconSelectionListener);
            }

            lblPrimaryInputDescription.setText(generatePrimaryDescriptionString(text));

            controlsToLayout.add(lblPrimaryInputDescription);
            controlsToLayout.add(lblPrimaryInputType);

            if (originComposite != null) {
                boolean displayOrigin = isDisplayOrigin(text);
                if (displayOrigin)
                    manualOriginDisplayer.setText(text.userInputOrigin);

                displayOrigin(displayOrigin, false);
                controlsToLayout.add(originComposite);
            }
        } else {
            lblPrimaryInputType.setText(generatePrimarySourceTypeString(text));
            lblPrimaryInputType.setToolTipText(generateTooltipForPrimaryTypeLabel(text, displayStyle));

            controlsToLayout.add(lblPrimaryInputDescription);
            controlsToLayout.add(lblPrimaryInputType);

            if (originComposite != null) {
                boolean displayOrigin = isDisplayOrigin(text);
                if (displayOrigin) {
                    manualOriginDisplayer.setText(text.userInputOrigin);
                }

                displayOrigin(displayOrigin, false);
                controlsToLayout.add(originComposite);
            }
        }

        Control[] layoutTargets = controlsToLayout.toArray(new Control[] {});
        layoutRoot.layout(layoutTargets);
    }

    private void displayOrigin(boolean b, boolean layout) {
        GridData lDataOriginDisplayer = (GridData) originComposite.getLayoutData();
        if (lDataOriginDisplayer.exclude == b) { // if sth needs to change
            originComposite.setVisible(b);
            lDataOriginDisplayer.exclude = !b;

            if (layout)
                this.layoutRoot.layout(new Control[] {originComposite});
        }
    }

    protected void doPrimaryClickActionFor(TextInputWithSource text) {
        // TODO:check everywhere that the text in the editor is really still the
        // same as at loading time
        if (text.getSourceType() == TextSourceType.FILE) {
            if (text.file != null && text.file.exists()) {
                openFileInEditor(text.file);
            } else {
                openTextInEditor(text.getText(), text.file.getName());
            }
        } else if (text.getSourceType() == TextSourceType.JCTEDITOR) {
            if (text.editorReference != null && text.editorReference.getEditor(false) != null) {
                bringEditorToFront(text.editorReference);
            } else if (text.editorReference != null && text.editorReference.getEditor(false) == null) {
                if (text.editorReference.getEditor(true) != null) {
                    bringEditorToFront(text.editorReference);
                } else {
                    openTextInEditor(text.getText(), text.editorReference.getTitle());
                }
            } else {
                openTextInEditor(text.getText(), "new"); //$NON-NLS-1$
            }
        } else if (text.getSourceType() == TextSourceType.USERINPUT) {
            if (text.userInputOrigin != null && text.userInputOrigin.getText().equals(text.getText())) {
                doPrimaryClickActionFor(text.userInputOrigin);
            } else {
                openTextInEditor(text.getText(), "new"); //$NON-NLS-1$
            }
        }
    }

    private static void bringEditorToFront(IEditorReference editorReference) {
        IEditorPart editor = editorReference.getEditor(true);
        editor.getEditorSite().getPage().activate(editor);
    }

    public static void openTextInEditor(String text, String name) {
        IEditorPart editorPart = getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        final String editorId = editorPart == null ? "org.jcryptool.editor.text.editor.JCTTextEditor" : editorPart
                .getSite().getId();

        IEditorInput editorInput = AbstractEditorService.createOutputFile(text);
        performOpenEditor(editorInput, editorId);
    }

    public static void openFileInEditor(File file) {
        IEditorPart editorPart = getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        final String editorId = editorPart == null ? "org.jcryptool.editor.text.editor.JCTTextEditor" : editorPart
                .getSite().getId();
        ;
        File outputFile = file;

        // try {
        // FileOutputStream fos = new FileOutputStream(outputFile);
        // fos.write(content.getBytes());
        // fos.flush();
        // fos.close();
        // } catch (IOException e) {
        //         LogUtil.logError(OperationsPlugin.PLUGIN_ID, "Exception while writing to an output stream", e, false); //$NON-NLS-1$
        // }
        // outputFile.deleteOnExit();
        performOpenEditor(new PathEditorInput(new Path(outputFile.getAbsolutePath())), editorId);
    }

    /**
     * Tries to open the given IEditorInput in the Editor associated with the given ID. This method must be executed in
     * an async way since a Job may be used to execute the cryptographic operation.
     *
     * @param input The IEditorInput that shall be displayed
     * @param editorId The ID of the Editor that is supposed to open
     */
    protected static void performOpenEditor(final IEditorInput input, final String editorId) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                try {
                    getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
                } catch (PartInitException ex) {
                    try {
                        getActiveWorkbenchWindow().getActivePage()
                                .openEditor(input, IOperationsConstants.ID_HEX_EDITOR);
                    } catch (PartInitException e) {
                        LogUtil.logError(OperationsPlugin.PLUGIN_ID, e);
                    }
                }
            }
        });
    }

    /**
     * Getter for the active workbench window
     *
     * @return the active workbench window
     */
    protected static IWorkbenchWindow getActiveWorkbenchWindow() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        return window;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
