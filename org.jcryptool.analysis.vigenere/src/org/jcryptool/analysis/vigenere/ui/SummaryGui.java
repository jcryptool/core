/* *****************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.analysis.vigenere.interfaces.DataProvider;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorNotFoundException;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;


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
	
	private Composite centerComposite;
	private Composite chooseEditorComposite;
	private Composite buttonComposite;

	public SummaryGui(ContentDelegator parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			this.setLayout(new GridLayout());
			this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			
			TitleAndDescriptionComposite titleAndDescription = new TitleAndDescriptionComposite(this);
			titleAndDescription.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			titleAndDescription.setTitle(Messages.SummaryGui_text_title);
			titleAndDescription.setDescription(Messages.SummaryGui_text_description);
			
			gsteps = new Group(this, SWT.NONE);
			gsteps.setLayout(new GridLayout());
			gsteps.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			gsteps.setText(Messages.SummaryGui_group_steps);

			tinto = new Text(gsteps, SWT.NONE);
			tinto.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			tinto.setText(Messages.SummaryGui_text_intro);
			tinto.setEditable(false);

			//Picture step one
			lione = new Label(gsteps, SWT.CENTER);
			lione.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			lione.setImage(DataProvider.getInstance().getImage(1));

			//Picture step two
			litwo = new Label(gsteps, SWT.CENTER);
			GridData gd_litwo = new GridData(SWT.FILL, SWT.FILL, true, false);
			gd_litwo.verticalIndent = 15;
			litwo.setLayoutData(gd_litwo);
			litwo.setImage(DataProvider.getInstance().getImage(2));

			//Picture sstep three
			lithree = new Label(gsteps, SWT.CENTER);
			GridData gd_lithree = new GridData(SWT.FILL, SWT.FILL, true, false);
			gd_lithree.verticalIndent = 15;
			lithree.setLayoutData(gd_lithree);
			lithree.setImage(DataProvider.getInstance().getImage(3));

			centerComposite = new Composite(this, SWT.NONE);
			centerComposite.setLayout(new GridLayout(2, true));
			GridData gd_centerComposite = new GridData(SWT.FILL, SWT.FILL, true, false);
			// widthHint needs to be set that the text wraps.
			gd_centerComposite.widthHint = 600;
			centerComposite.setLayoutData(gd_centerComposite);

			tdescr = new Text(centerComposite, SWT.MULTI | SWT.WRAP);
			tdescr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
			tdescr.setText(Messages.SummaryGui_text_help);
			tdescr.setEditable(false);
			tdescr.setFont(FontService.getSmallFont());

			chooseEditorComposite = new Composite(centerComposite, SWT.NONE);
			chooseEditorComposite.setLayout(new GridLayout(2, false));
			chooseEditorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			// leditor = new Label(this, SWT.NONE);
			leditor = new Label(chooseEditorComposite, SWT.NONE);
			leditor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			leditor.setText(Messages.SummaryGui_0);

			ceditor = new Combo(chooseEditorComposite, SWT.READ_ONLY);
			ceditor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			refreshEditors();
			ceditor.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (editorReferenceList != null) {
						explicitelySelectedEditor = editorReferenceList[ceditor.getSelectionIndex()];
						setSelectedEditor(explicitelySelectedEditor);
					}
				}
			});

			lsepend = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			lsepend.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			buttonComposite = new Composite(this, SWT.NONE);
			buttonComposite.setLayout(new GridLayout(2, false));
			buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

			bstart = new Button(buttonComposite, SWT.PUSH);
			bstart.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
			bstart.setText(Messages.SummaryGui_button_start);
			bstart.setToolTipText(Messages.SummaryGui_ttip_start);
			bstart.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					start();
				}
			});
			bstart.setEnabled(canProceed());

			bdecrypt = new Button(buttonComposite, SWT.PUSH);
			bdecrypt.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
			bdecrypt.setText(Messages.SummaryGui_button_analyze);
			bdecrypt.setToolTipText(Messages.SummaryGui_ttip_analyze);
			bdecrypt.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					quickanalysis();
				}
			});
			bdecrypt.setEnabled(canProceed());

			getShell().setDefaultButton(bstart);
			layout();
			pack();
		} catch (Exception ex) {
			LogUtil.logError(ex);
		}

		registerEditorListeners();
	}

	private void registerEditorListeners() {
		/*
		 * Strategy of this observer: - if no editor has been explicitly selected,
		 * always select the active editor - if the active editor is null, try to get
		 * the first editor, even if not marked as the active editor - if the editor
		 * that was explicitly selected is closed, act like there was no explicit
		 * selection.
		 */
		final Observer o1;
		EditorsManager.getInstance().getActiveEditorChangedObservable().addObserver((o1 = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (explicitelySelectedEditor == null) {
					if (selectedEditor == null) {
						selectEditorComboItem(EditorsManager.getInstance().getActiveEditorReference());
					} else {
						selectEditorComboItem(selectedEditor);
					}
				}
			}
		}));

		final Observer o2;
		EditorsManager.getInstance().getEditorAvailabilityObservable().addObserver((o2 = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				refreshEditors();
			}
		}));

		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				EditorsManager.getInstance().getActiveEditorChangedObservable().deleteObserver(o1);
				EditorsManager.getInstance().getEditorAvailabilityObservable().deleteObserver(o2);
			}
		});
	}

	/**
	 * Request editorReferenceList of all open editors from plug-in interface and
	 * sets content of this combo box accordingly.
	 */
	private void refreshEditors() {
		try {
			editorReferenceList = DataProvider.getInstance().getEditorReferences();
			if (EditorsManager.getInstance().isEditorOpen()) {
				selectedEditor = EditorsManager.getInstance().getActiveEditorReference();
			}

			if (!Arrays.asList(editorReferenceList).contains(selectedEditor))
				setSelectedEditor(null);
			if (!Arrays.asList(editorReferenceList).contains(explicitelySelectedEditor))
				explicitelySelectedEditor = null;

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
	 * Tries to select the combo item for the given IEditorReference. this reference
	 * must be also contained in {@link #editorReferenceList}. If the reference is
	 * null, the first item will be selected.
	 *
	 * @param editorRef
	 */
	private void selectEditorComboItem(IEditorReference editorRef) {
		if (ceditor.getItemCount() > 0) {
			if (editorRef == null) {
				ceditor.select(0);
				setSelectedEditor(editorReferenceList[0]);
			} else {
				int pos = -1;
				if ((pos = Arrays.asList(editorReferenceList).indexOf(editorRef)) != -1) {
					ceditor.select(pos);
					setSelectedEditor(editorRef);
				} else {
					throw new IllegalArgumentException(
							"Can't select the given editor because it is not contained in the combo box"); //$NON-NLS-1$
				}
			}
		} else {
			throw new UnsupportedOperationException("Can't select an item in an empty combo box"); //$NON-NLS-1$
		}
	}

	private void start() {
		refreshEditors();
		ContentDelegator del = (ContentDelegator) getParent();
		del.toFriedman(selectedEditor);
	}

	private void quickanalysis() {
		refreshEditors();
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
		if (bdecrypt != null && !bdecrypt.isDisposed())
			bdecrypt.setEnabled(canProceed());
		if (bstart != null && !bstart.isDisposed())
			bstart.setEnabled(canProceed());
	}
}
