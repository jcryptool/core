/* *****************************************************************************
 * Copyright (c) 2010 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * ****************************************************************************/
package org.jcryptool.analysis.vigenere.ui;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.UIManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.analysis.vigenere.views.VigenereBreakerView;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorNotFoundException;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.fonts.FontService;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class SummaryGui extends Content {
    private Group gsteps;
    private Text tinto;

    private Label lione;
    private Label litwo;
    private Label lithree;
    private Label leditor;
    private Label lsepend;

    private Text tdescr;

    private Button bstart;
    private Button bdecrypt;
    private Combo ceditor;
    private IEditorReference explicitelySelectedEditor = null;
    private IEditorReference selectedEditor = null;
	private IEditorReference[] editorReferenceList;

    public SummaryGui(ContentDelegator parent, int style) {
        super(parent, style);
        initGUI();
    }

    private void initGUI() {
        try {
            String s = UIManager.getSystemLookAndFeelClassName();
            Font system = getDisplay().getSystemFont();
            FontData base = system.getFontData()[0];

            if (s.endsWith(VigenereBreakerView.GTK)) {
                base.setHeight(9);
            }

            FormLayout thisLayout = new FormLayout();
            this.setLayout(thisLayout);
            {
            	gsteps = new Group(this, SWT.NONE);
            	FormLayout grpstepsLayout = new FormLayout();
            	gsteps.setLayout(grpstepsLayout);
            	FormData grpstepsLData = new FormData();
            	grpstepsLData.left =  new FormAttachment(0, 1000, 10);
            	grpstepsLData.top =  new FormAttachment(0, 1000, 10);
            	grpstepsLData.width = 754;
            	grpstepsLData.height = 222;
            	gsteps.setLayoutData(grpstepsLData);
            	gsteps.setText(Messages.SummaryGui_group_steps);
            	gsteps.setFont(FontService.getNormalFont());
            	{
            		tinto = new Text(gsteps, SWT.NONE);
            		FormData tintoLData = new FormData();
            		tintoLData.left = new FormAttachment(0, 1000, 17);
            		tintoLData.top = new FormAttachment(0, 1000, 5);
            		tintoLData.width = 714;
            		tintoLData.height = 30;
            		tinto.setLayoutData(tintoLData);
            		tinto.setText(Messages.SummaryGui_text_intro);
            		tinto.setBackground(new Color(getDisplay(), 240, 240, 240));
            		tinto.setEditable(false);
            		tinto.setEnabled(false);
            		tinto.setFont(FontService.getNormalFont());
            	}
            	{
            		FormData labimgoneLData = new FormData();
            		labimgoneLData.left = new FormAttachment(0, 1000, 18);
            		labimgoneLData.top = new FormAttachment(0, 1000, 38);
            		labimgoneLData.width = 720;
            		labimgoneLData.height = 45;
            		lione = new Label(gsteps, SWT.NONE);
            		lione.setLayoutData(labimgoneLData);
            		lione.setAlignment(SWT.CENTER);
            		lione.setImage(DataProvider.getInstance().getImage(1));
            	}
            	{
            		litwo = new Label(gsteps, SWT.NONE);
            		FormData labimgtwoLData = new FormData();
            		labimgtwoLData.left = new FormAttachment(0, 1000, 18);
            		labimgtwoLData.top = new FormAttachment(0, 1000, 98);
            		labimgtwoLData.width = 720;
            		labimgtwoLData.height = 45;
            		litwo.setLayoutData(labimgtwoLData);
            		litwo.setAlignment(SWT.CENTER);
            		litwo.setImage(DataProvider.getInstance().getImage(2));
            	}
            	{
            		lithree = new Label(gsteps, SWT.NONE);
            		FormData labimgthreeLData = new FormData();
            		labimgthreeLData.left = new FormAttachment(0, 1000, 18);
            		labimgthreeLData.top = new FormAttachment(0, 1000, 158);
            		labimgthreeLData.width = 720;
            		labimgthreeLData.height = 45;
            		lithree.setLayoutData(labimgthreeLData);
            		lithree.setAlignment(SWT.CENTER);
            		lithree.setImage(DataProvider.getInstance().getImage(3));
            	}
            }
            {
            	tdescr = new Text(this, SWT.MULTI | SWT.WRAP);
            	FormData tsdecrLData = new FormData();
            	tsdecrLData.left =  new FormAttachment(0, 1000, 10);
            	tsdecrLData.top =  new FormAttachment(0, 1000, 265);
            	tsdecrLData.width = 274;
            	//                tsdecrLData.height = 95;
            	tdescr.setLayoutData(tsdecrLData);
            	tdescr.setText(Messages.SummaryGui_text_help);
            	tdescr.setEditable(false);
            	tdescr.setEnabled(false);
            	tdescr.setFont(FontService.getSmallFont());
            }
            {
                leditor = new Label(this, SWT.NONE);
                FormData labinputLData = new FormData();
                labinputLData.left = new FormAttachment(0, 1000, 292);
                labinputLData.top = new FormAttachment(0, 1000, 279);
                labinputLData.width = 160;
                labinputLData.height = 15;
                leditor.setLayoutData(labinputLData);
                leditor.setText("Editor mit Geheimtext:");
                leditor.setFont(FontService.getNormalFont());
                leditor.setAlignment(SWT.RIGHT);
            }
            {
            	ceditor = new Combo(this, SWT.READ_ONLY);
            	FormData cmbinputLData = new FormData();
            	cmbinputLData.left =  new FormAttachment(0, 1000, 465);
            	cmbinputLData.top =  new FormAttachment(0, 1000, 275);
            	cmbinputLData.width = 174;
            	cmbinputLData.height = 23;
            	ceditor.setLayoutData(cmbinputLData);
            	ceditor.setFont(FontService.getNormalFont());
            	refreshEditors();
            	ceditor.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if(editorReferenceList != null) {
							explicitelySelectedEditor = editorReferenceList[ceditor.getSelectionIndex()];
							setSelectedEditor(explicitelySelectedEditor);
						}
					}
				});
            }
            {
            	FormData lsependLData = new FormData();
            	lsependLData.left =  new FormAttachment(0, 1000, 9);
            	lsependLData.top = new FormAttachment(tdescr, 10);
            	lsependLData.width = 760;
            	lsependLData.height = 2;
            	lsepend = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
            	lsepend.setLayoutData(lsependLData);
            }
            {
            	bstart = new Button(this, SWT.PUSH | SWT.CENTER);
            	FormData butstartLData = new FormData();
            	butstartLData.left =  new FormAttachment(0, 1000, 10);
            	butstartLData.top =  new FormAttachment(lsepend, 10);
            	butstartLData.height = 25;
            	bstart.setLayoutData(butstartLData);
            	bstart.setText(Messages.SummaryGui_button_start);
            	bstart.setToolTipText(Messages.SummaryGui_ttip_start);
            	bstart.setFont(FontService.getNormalFont());
            	bstart.addSelectionListener(new SelectionAdapter() {
            		public void widgetSelected(SelectionEvent evt) {
            			start();
            		}
            	});
            	bstart.setEnabled(canProceed());
            }
            {
            	bdecrypt = new Button(this, SWT.PUSH | SWT.CENTER);
            	FormData bdecryptLData = new FormData();
            	bdecryptLData.left = new FormAttachment(bstart, 10);
            	bdecryptLData.top = new FormAttachment(lsepend, 10);
            	bdecryptLData.height = 25;
            	bdecrypt.setLayoutData(bdecryptLData);
            	bdecrypt.setText(Messages.SummaryGui_button_analyze);
            	bdecrypt.setToolTipText(Messages.SummaryGui_ttip_analyze);
            	bdecrypt.setFont(FontService.getNormalFont());
            	bdecrypt.addSelectionListener(new SelectionAdapter() {
            		public void widgetSelected(SelectionEvent event) {
            			quickanalysis();
            		}
            	});
            	bdecrypt.setEnabled(canProceed());
            }
            getShell().setDefaultButton(bstart);
            this.layout();
            pack();
        } catch (Exception ex) {
            LogUtil.logError(ex);
        }

        registerEditorListeners();
    }

    private void registerEditorListeners() {
		/* Strategy of this observer:
		 *  - if no editor has been explicitly selected, always select the active editor
		 *  - if the active editor is null, try to get the first editor, even if not marked as the active editor
		 *  - if the editor that was explicitly selected is closed, act like there was no explicit selection.
		 */
    	final Observer o1;
    	EditorsManager.getInstance().getActiveEditorChangedObservable().addObserver((o1=new Observer() {
			public void update(Observable o, Object arg) {
				if(explicitelySelectedEditor == null) {
					selectEditorComboItem(EditorsManager.getInstance().getActiveEditorReference());
				}
			}
		}));

    	final Observer o2;
    	EditorsManager.getInstance().getEditorAvailabilityObservable().addObserver((o2=new Observer() {
			public void update(Observable o, Object arg) {
				refreshEditors();
			}
		}));

    	this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				EditorsManager.getInstance().getActiveEditorChangedObservable().deleteObserver(o1);
				EditorsManager.getInstance().getEditorAvailabilityObservable().deleteObserver(o2);
			}
		});
	}

	/**
     * Request editorReferenceList of all open editors from plug-in interface and sets content
     * of this combo box accordingly.
     */
    private void refreshEditors() {
        try {
        	editorReferenceList = DataProvider.getInstance().getEditorReferences();

        	if(!Arrays.asList(editorReferenceList).contains(selectedEditor)) setSelectedEditor(null);
        	if(!Arrays.asList(editorReferenceList).contains(explicitelySelectedEditor)) explicitelySelectedEditor = null;

            // 0 or 1 entries in editorReferenceList: enable combo
            switch (editorReferenceList.length) {
            case 0:
            	ceditor.setItems(new String[0]);
            	ceditor.setText(Messages.SummaryGui_combo_warning);
                // fall-through
            case 1:
                ceditor.setEnabled(false);
                break;
            default:
            	ceditor.setEnabled(true);
                break;
            }

            String[] items = new String[editorReferenceList.length];
            for (int i = 0; i < editorReferenceList.length; i++) {
				items[i] = editorReferenceList[i].getTitle();
			}

            // at least one entry: fill editorReferenceList in combo
            if (0 < editorReferenceList.length) {
                ceditor.setItems(items);
            }
            selectEditorComboItem(selectedEditor);

        } catch (EditorNotFoundException enfEx) {
        	editorReferenceList = null;
        	explicitelySelectedEditor = null;
            // just warning, not message box necessary.
            ceditor.setEnabled(false);
            ceditor.setItems(new String[0]);
            ceditor.setText(Messages.SummaryGui_combo_warning);
        }
    }

    /**
     * Tries to select the combo item for the given IEditorReference. this reference must be also contained
     * in {@link #editorReferenceList}. If the reference is null, the first item will be selected.
     *
     * @param editorRef
     */
    private void selectEditorComboItem(
			IEditorReference editorRef) {
		if(ceditor.getItemCount() > 0) {
			if(editorRef == null) {
				ceditor.select(0);
				setSelectedEditor(editorReferenceList[0]);
			} else {
				int pos = -1;
				if((pos = Arrays.asList(editorReferenceList).indexOf(editorRef)) != -1) {
					ceditor.select(pos);
					setSelectedEditor(editorRef);
				} else {
					throw new IllegalArgumentException("Can't select the given editor because it is not contained in the combo box");
				}
			}
		} else {
			throw new UnsupportedOperationException("Can't select an item in an empty combo box");
		}
	}

    private void start() {
        ContentDelegator del = (ContentDelegator) getParent();
        del.toFriedman(selectedEditor);
    }

    private void quickanalysis() {
        ContentDelegator del = (ContentDelegator) getParent();
        del.toQuick(selectedEditor);
    }

	public IEditorReference getSelectedEditor() {
		return selectedEditor;
	}

	private boolean canProceed() {
		return getSelectedEditor() != null;
	}

	public void setSelectedEditor(IEditorReference selectedEditor) {
		this.selectedEditor = selectedEditor;
		if(bdecrypt != null && !bdecrypt.isDisposed()) bdecrypt.setEnabled(canProceed());
		if(bstart != null && !bstart.isDisposed()) bstart.setEnabled(canProceed());
	}
}
