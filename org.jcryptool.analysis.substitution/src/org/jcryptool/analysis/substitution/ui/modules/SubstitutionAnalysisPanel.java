//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2013, 2020 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.jcryptool.analysis.substitution.calc.TextStatistic;
import org.jcryptool.analysis.substitution.ui.modules.utils.StatisticsDisplayer;
import org.jcryptool.analysis.substitution.ui.modules.utils.StatisticsWizard;
import org.jcryptool.analysis.substitution.ui.modules.utils.SubstKeyViewer;
import org.jcryptool.analysis.substitution.ui.modules.utils.SubstitutionAnalysisText;
import org.jcryptool.analysis.substitution.ui.modules.utils.SubstitutionKeyEditor;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.util.ui.TitleAndDescriptionComposite;
import org.jcryptool.crypto.ui.textsource.TextInputWithSourceDisplayer;
import org.jcryptool.editor.text.JCTTextEditorPlugin;

public class SubstitutionAnalysisPanel extends Composite {

	private static final int MAIN_LBL_WIDTH_HINT = 250;
	private AbstractAlphabet alphabet;
	private TextStatistic referenceStatistic;
	private String text;
	private TextStatistic cipherStatistic;
	private StatisticsDisplayer statisticsDisplayer;
	private SubstitutionKeyEditor substEditor;
	private boolean upperLowerCipherMode;
	private SubstitutionAnalysisText previewer;
	private Button restOfAlphaLexical;
	private Button restOfAlphaAntilexical;
	private Button btnFillMappings;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SubstitutionAnalysisPanel(Composite parent, int style, String text, AbstractAlphabet alphabet, TextStatistic referenceStatistic, boolean upperLowerCipherMode) {
		super(parent, style);
		this.text = text;
		this.alphabet = alphabet;
		this.referenceStatistic = referenceStatistic;
		this.upperLowerCipherMode = upperLowerCipherMode;
		this.cipherStatistic = new TextStatistic(text);
		
		initGUI();
	}

	private void initGUI() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
	
		TitleAndDescriptionComposite td = new TitleAndDescriptionComposite(this);
		GridData gdTitle = new GridData(SWT.FILL, SWT.FILL, true, false);
		gdTitle.minimumWidth = MAIN_LBL_WIDTH_HINT;
		td.setLayoutData(gdTitle);
		td.setTitle(Messages.SubstitutionAnalysisPanel_0);
		td.setDescription(Messages.SubstitutionAnalysisPanel_4);

		
		Group grpLetterFrequencyStatistics = new Group(this, SWT.NONE);
		grpLetterFrequencyStatistics.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 3));
		grpLetterFrequencyStatistics.setText(Messages.SubstitutionAnalysisPanel_1);
		grpLetterFrequencyStatistics.setLayout(new GridLayout(1, false));
		
		initLetterFreqGroup(grpLetterFrequencyStatistics);
		
		Group grpSubstitutions = new Group(this, SWT.NONE);
		grpSubstitutions.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpSubstitutions.setText(Messages.SubstitutionAnalysisPanel_2);
		grpSubstitutions.setLayout(new GridLayout(1, false));
		
		initSubstGroup(grpSubstitutions);
		
		Group grpPreviewOfThe = new Group(this, SWT.NONE);
		grpPreviewOfThe.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpPreviewOfThe.setText(Messages.SubstitutionAnalysisPanel_3);
		grpPreviewOfThe.setLayout(new GridLayout(1, false));
		
		initPreviewGroup(grpPreviewOfThe);
	}

	private void initPreviewGroup(Group grpPreviewOfThe) {
		previewer = new SubstitutionAnalysisText(grpPreviewOfThe, SWT.NONE, text, upperLowerCipherMode);
		previewer.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));
		
		Button exportButton = new Button(grpPreviewOfThe, SWT.PUSH);
		exportButton.setText(Messages.SubstitutionAnalysisPanel_5);
		Image image = JCTTextEditorPlugin.getDefault().getImageRegistry()
                        .get(JCTTextEditorPlugin.JCT_TEXT_EDITOR_ICON);
		exportButton.setImage(image);
		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String calcPlaintext = calculatePlaintext();
				TextInputWithSourceDisplayer.openTextInEditor(calcPlaintext, "substAnalysisOutput.txt"); //$NON-NLS-1$
			}
		});
	}

	protected String calculatePlaintext() {
		return previewer.getLastPlaintext();
	}

	private void initSubstGroup(Group grpSubstitutions) {
		substEditor = new SubstitutionKeyEditor(grpSubstitutions, SWT.NONE, alphabet);
		substEditor.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true));
		
		substEditor.setCharMappingExternal(new HashMap<Character, Character>());
		substEditor.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				preview();
				btnFillMappings.setEnabled(!substEditor.isCompleteData());
			}
		});
		
		Composite keyPreview = new Composite(grpSubstitutions, SWT.NONE);
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		keyPreview.setLayout(layout);
		keyPreview.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button lblKeyPreview = new Button(keyPreview, SWT.PUSH);
		GridData lblKeyPreviewLayoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		lblKeyPreview.setLayoutData(lblKeyPreviewLayoutData);
		lblKeyPreview.setText(Messages.SubstitutionAnalysisPanel_7);
		lblKeyPreview.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportMapping();
			}
		});

		btnFillMappings = new Button(keyPreview, SWT.PUSH);
		GridData lblbtnFillMappings = new GridData(SWT.TRAIL, SWT.CENTER, true, false, 1, 1);
		btnFillMappings.setLayoutData(lblbtnFillMappings);
		btnFillMappings.setText(Messages.SubstitutionAnalysisPanel_6);
		btnFillMappings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				substEditor.setCharMappingExternal(fillUpMissingCharacters(substEditor.getCharMapping(), alphabet, restOfAlphaLexical.getSelection()));
			}
		});
		
		Composite compRestDirection = new Composite(keyPreview, SWT.NONE);
		GridLayout compRestDirectionLayout = new GridLayout(1, false);
		compRestDirectionLayout.marginWidth = 0;
		compRestDirectionLayout.marginHeight = 0;
		compRestDirection.setLayout(compRestDirectionLayout);
		GridData compRestDirectionLData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1);
		compRestDirection.setLayoutData(compRestDirectionLData);
		
		restOfAlphaLexical = new Button(compRestDirection, SWT.RADIO);
		restOfAlphaLexical.setText(org.jcryptool.crypto.classic.substitution.ui.Messages.SubstitutionKeyEditor_8);
		boolean lexicalBtnSelection = true;
		restOfAlphaLexical.setSelection(
				lexicalBtnSelection
				);
		
		restOfAlphaAntilexical = new Button(compRestDirection, SWT.RADIO);
		restOfAlphaAntilexical.setText(org.jcryptool.crypto.classic.substitution.ui.Messages.SubstitutionKeyEditor_9);
		restOfAlphaAntilexical.setSelection(
				!lexicalBtnSelection
				);
		
		Button btnResetMappings = new Button(keyPreview, SWT.PUSH);
		GridData lblbtnResetMappings = new GridData(SWT.TRAIL, SWT.CENTER, true, false, 1, 1);
		btnResetMappings.setLayoutData(lblbtnResetMappings);
		btnResetMappings.setText(Messages.SubstitutionAnalysisPanel_9);
		btnResetMappings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				substEditor.setCharMappingExternal(new HashMap<Character, Character>());
			}
		});
	}

	protected Map<Character, Character> fillUpMissingCharacters(Map<Character, Character> charMapping,
			AbstractAlphabet alphabet2, boolean direction) {
		Map<Character, Character> fillupMap = new HashMap<Character, Character>();
		LinkedList<Character> restCharacters = new LinkedList<Character>(alphabet2.asList());
		if(!direction) {
			Collections.reverse(restCharacters);
		}
		for(Character c: charMapping.values()) if(c != null) restCharacters.remove(c);
		for(Character c: alphabet2.asList()) {
			if(charMapping.get(c) != null) {
				fillupMap.put(c, charMapping.get(c));
			} else {
				if(! restCharacters.isEmpty()) {
					fillupMap.put(c, restCharacters.removeFirst());
				}
			}
		}
		return fillupMap;
	}

	protected void exportMapping() {
		SubstKeyViewer.show(substEditor.getCharMapping(), alphabet);
	}

	protected void preview() {
		Map<Character, Character> mapping = substEditor.getCharMapping();
		previewer.setMapping(mapping);
		
		statisticsDisplayer.setCharMapping(mapping);
		
	}

	private void initLetterFreqGroup(final Group grpLetterFrequencyStatistics) {
		statisticsDisplayer = new StatisticsDisplayer(grpLetterFrequencyStatistics, this, SWT.NONE, referenceStatistic, cipherStatistic, alphabet, upperLowerCipherMode);
		statisticsDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		statisticsDisplayer.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				if(arg instanceof Map) {
					Map<Character, Character> map = (Map<Character, Character>) arg;
					for(Map.Entry<Character, Character> e: map.entrySet()) {
						substEditor.setSingleSubstitution(e.getKey(), e.getValue());
					}
				}
			}
		});
		
		final Button loadStatisticsButton = new Button(grpLetterFrequencyStatistics, SWT.PUSH);
		loadStatisticsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		loadStatisticsButton.setText(Messages.SubstitutionAnalysisPanel_13);
		loadStatisticsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StatisticsWizard w = new StatisticsWizard(alphabet);
				WizardDialog d = new WizardDialog(getShell(), w);
				int result = d.open();
				
				if(result == Window.OK) {
					TextStatistic stat = w.getStatistics();
					
					if(stat != null) {
						SubstitutionAnalysisPanel.this.referenceStatistic = stat;

						statisticsDisplayer.dispose();
						loadStatisticsButton.dispose();
						
						initLetterFreqGroup(grpLetterFrequencyStatistics);
						SubstitutionAnalysisPanel.this.layout(new Control[]{statisticsDisplayer});
					}
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
