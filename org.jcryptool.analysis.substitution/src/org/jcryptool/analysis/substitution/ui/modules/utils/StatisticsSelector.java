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
package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.analysis.substitution.Activator;
import org.jcryptool.analysis.substitution.calc.DynamicPredefinedStatisticsProvider;
import org.jcryptool.analysis.substitution.calc.TextStatistic;
import org.jcryptool.analysis.substitution.ui.modules.Messages;
import org.jcryptool.analysis.substitution.ui.modules.SubstitutionAnalysisConfigPanel;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.crypto.ui.textloader.ui.ControlHatcher;
import org.jcryptool.crypto.ui.textloader.ui.wizard.TextLoadController;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.util.NestedEnableDisableSwitcher;

public class StatisticsSelector extends Composite {

	private static final String NOCONTENT_COMBO_STRING = Messages.StatisticsSelector_0;
	private AbstractAlphabet alpha;
	private List<Observer> observers;
	private Composite layoutRoot;
	private TextLoadController referenceTextSelector;
	private TextStatistic analyzedReferenceText;
	private boolean isPredefinedStatistic;
	private TextStatistic selectedStatistic;
	private Combo comboPredefined;
	private LinkedList<TextStatistic> predefinedStatistics;
	private Button btnCustom;
	private Button btnPredefined;
	private NestedEnableDisableSwitcher customControlsEnabler;
	private NestedEnableDisableSwitcher predefinedControlsEnabler;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatisticsSelector(Composite parent, Composite layoutRoot, int style) {
		super(parent, style);
		this.layoutRoot = layoutRoot;
		this.observers = new LinkedList<Observer>();
		this.alpha = getDefaultAlphabetInitialization();
		
		setLayout(new GridLayout(2, false));
		
		btnPredefined = new Button(this, SWT.RADIO);
		btnPredefined.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnPredefined.getSelection()) {
					setPredefinedControlsEnabled(true);
					setPredefinedStatistics(comboPredefined.getSelectionIndex(), true);
				}
			}
		});
		btnPredefined.setText(Messages.StatisticsSelector_1);
		
		comboPredefined = new Combo(this, SWT.NONE);
		GridData comboLData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		comboPredefined.setText(NOCONTENT_COMBO_STRING);
		comboLData.widthHint = comboPredefined.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		comboPredefined.setLayoutData(comboLData);
		
		comboPredefined.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnPredefined.getSelection()) {
					setPredefinedStatistics(comboPredefined.getSelectionIndex(), true);
				}
			}
		});
		
		btnCustom = new Button(this, SWT.RADIO);
		btnCustom.setText(Messages.StatisticsSelector_2);
		
		btnCustom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(btnCustom.getSelection()) {
					setCustomControlsEnabled(true);
					setCustomStatistics(analyzedReferenceText, referenceTextSelector.getText(), referenceTextSelector.getTransformData(), true);
				}
			}
		});
		
		referenceTextSelector = new TextLoadController(this, layoutRoot, SWT.NONE, false, true);
		referenceTextSelector.setControlHatcherAfterWizText(new ControlHatcher.LabelHatcher(
				SubstitutionAnalysisConfigPanel.TEXTTRANSFORM_HINT+"\n\n"+ //$NON-NLS-1$
						Messages.StatisticsSelector_4 +
						Messages.StatisticsSelector_5
				));
		referenceTextSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
		
		referenceTextSelector.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				TextInputWithSource text = referenceTextSelector.getText();
				if(text != null) {
					TextStatistic newCustomStatistic = analyzeCustomText(text, referenceTextSelector.getTransformData());
					setCustomStatistics(newCustomStatistic, text, referenceTextSelector.getTransformData(), true);
				}
			}
		});
		customControlsEnabler = new NestedEnableDisableSwitcher(referenceTextSelector);
		customControlsEnabler.alwaysRefresh = true;
		predefinedControlsEnabler = new NestedEnableDisableSwitcher(comboPredefined);
		predefinedControlsEnabler.alwaysRefresh = true;
		
		setInitialState();
		
	}

	private void setInitialState() {
		initializePredefinedStatistics(getAlphabet());
		if(this.predefinedStatistics.size() != 0) {
			setPredefinedStatistics(0, false);
		} else {
			setCustomStatistics(null, null, null, false);
		}
	}

	public void addObserver(Observer o) {
		this.observers.add(o);
	}

	protected TextStatistic analyzeCustomText(TextInputWithSource text, TransformData transformData) {
		// TODO maybe show progress?
		// TODO: write identification information (origin e.g.)
		// TODO: maybe saving of statistics?
		// TODO: hint, that the statistics will be filtered by the selected alphabet
		String textToAnalyze;
		
		textToAnalyze = text.getText();
		
		textToAnalyze = prepareCustomTextForAnalysis(transformData, textToAnalyze);
		
		return new TextStatistic(textToAnalyze);
	}

	private String prepareCustomTextForAnalysis(TransformData selectedTransformData, String textToAnalyze) {
		return DynamicPredefinedStatisticsProvider.prepareFileTextForAnalysis(textToAnalyze, selectedTransformData, getAlphabet());
	}

	private void initializePredefinedStatistics(AbstractAlphabet alphabet) {
		PredefinedStatisticsProvider statisticsProvider = getDefaultStatisticsProvider();
		List<TextStatistic> statistics = getSuitableStatistics(alphabet, statisticsProvider);
		
		setPredefinedStatisticsNoRefresh(statistics);
	}
	
	private void reinitializePredefinedStatistics(AbstractAlphabet oldAlpha, 
			AbstractAlphabet newAlpha, 
			TextStatistic previousSelection,
			boolean previousSelectionWasPredefined
			) {
		PredefinedStatisticsProvider statisticsProvider = getDefaultStatisticsProvider();
		List<TextStatistic> statistics = getSuitableStatistics(newAlpha, statisticsProvider);
	
		TextStatistic newSelection = null;
		boolean newSelectionIsPredefined = previousSelectionWasPredefined;
		boolean prevExistsInNewPredefined = statistics.contains(previousSelection);
		
		if(previousSelectionWasPredefined) {
			if(statistics.size()==0) {
				newSelectionIsPredefined = false;
				newSelection = analyzedReferenceText;
			} else {
				if(prevExistsInNewPredefined) {
					newSelectionIsPredefined = true;
					newSelection = previousSelection;
				} else {
					newSelection = statistics.get(0);
					newSelectionIsPredefined = true;
				}
			}
		} else {
			if(previousSelection != null) {
				TextInputWithSource previousSource = referenceTextSelector.getText();
				TransformData previousTransformData = referenceTextSelector.getTransformData();
				//re-analyze with new alphabet
				newSelection = analyzeCustomText(previousSource, previousTransformData);
				newSelectionIsPredefined = false;
			} else {
				newSelection = null;
				newSelectionIsPredefined = false;
			}
		}
		
		setPredefinedStatisticsNoRefresh(statistics);
		if(newSelectionIsPredefined) {
			setPredefinedStatistics(statistics.indexOf(newSelection), true);
		} else {
			setCustomStatistics(newSelection, referenceTextSelector.getText(), referenceTextSelector.getTransformData(), true);
		}
	}

	private void setPredefinedStatisticsNoRefresh(List<TextStatistic> statistics) {
		this.predefinedStatistics = new LinkedList<TextStatistic>(statistics);
		this.comboPredefined.setItems(new String[]{});
		setPredefinedSectionEnabled(statistics.size()>0);
		if(statistics.size()>0) {
			for (int i = 0; i < statistics.size(); i++) {
				TextStatistic s = statistics.get(i);
				comboPredefined.add(generateComboStringItemFor(s, i));
			}
		}
	}

	public void setCustomStatistics(TextStatistic selectedCustomStatistics, TextInputWithSource source, TransformData transformData, boolean notifyObservers) {
		//TODO: transform text
		
		this.selectedStatistic = selectedCustomStatistics;
		if(selectedCustomStatistics != null) {
			analyzedReferenceText = selectedCustomStatistics;
			if(referenceTextSelector.getText() != source || referenceTextSelector.getTransformData() != transformData) {
				referenceTextSelector.setTextData(source, transformData, false);
			}
		} else {
			analyzedReferenceText = null;
			referenceTextSelector.setTextData(null, null, false);
		}
		if(!btnCustom.getSelection()) {
			btnCustom.setSelection(true);
		}
		btnPredefined.setSelection(false);
		
		this.isPredefinedStatistic = false;
		
		setPredefinedControlsEnabled(false);
		setCustomControlsEnabled(true);
		if(notifyObservers) notifyObservers();
	}
	

	public void setPredefinedStatistics(TextStatistic statistic, boolean notifyObservers) {
		int index = predefinedStatistics.indexOf(statistic);
		if(index > -1) {
			setPredefinedStatistics(index, notifyObservers);
		} else {
			setCustomStatistics(null, null, null, notifyObservers);
		}
	}
	
	public void setPredefinedStatistics(int indexOfPredefinedStatistic, boolean notifyObservers) {
		if(indexOfPredefinedStatistic > -1) {
			this.selectedStatistic = predefinedStatistics.get(indexOfPredefinedStatistic);
			
			if(! (comboPredefined.getSelectionIndex() == indexOfPredefinedStatistic)) {
				comboPredefined.select(indexOfPredefinedStatistic);
			}
			
		} else {
			this.selectedStatistic = null;
			
			comboPredefined.select(-1);
			
		}
		if(!btnPredefined.getSelection()) {
			btnPredefined.setSelection(true);
		}
		btnCustom.setSelection(false);
		this.isPredefinedStatistic = true;
		
		setCustomControlsEnabled(false);
		setPredefinedControlsEnabled(true);
		if(notifyObservers) notifyObservers();
	}
	
	private void setCustomControlsEnabled(boolean b) {
		customControlsEnabler.setEnabled(b);
	}

	private void setPredefinedControlsEnabled(boolean b) {
		predefinedControlsEnabler.setEnabled(b);
	}

	private String generateComboStringItemFor(TextStatistic s, int i) {
		return String.format(Messages.StatisticsSelector_6, s.getLanguage(), s.getName());
	}

	private void setPredefinedSectionEnabled(boolean b) {
		//TODO: set flags for whole component enabling/disabling
		predefinedControlsEnabler.setEnabled(b);
		btnPredefined.setEnabled(b);
		if(!b) {
			comboPredefined.setText(NOCONTENT_COMBO_STRING);
		} else {
			comboPredefined.setText(""); //$NON-NLS-1$
		}
	}
	
	private static List<Character> alphaContentAsList(AbstractAlphabet alpha) {
		LinkedList<Character> result = new LinkedList<Character>();
		for(char c: alpha.getCharacterSet()) result.add(c);
		return result;
	}

	public static List<TextStatistic> getSuitableStatistics(AbstractAlphabet newAlpha, PredefinedStatisticsProvider statisticsProvider) {
		List<TextStatistic> suitableStatistics = new LinkedList<TextStatistic>();
		List<TextStatistic> allStatistics = statisticsProvider.getPredefinedStatistics();
		
		Set<String> names = new LinkedHashSet<String>();
		LinkedHashMap<TextStatistic, Double> scoreMap = new LinkedHashMap<TextStatistic, Double>();
		for(TextStatistic statistic: allStatistics) {
			double score = TextStatistic.compareTwoAlphabets(alphaContentAsList(newAlpha), statistic.getTextCharacters());
			scoreMap.put(statistic, score);
			names.add(statistic.getName());
		}
		for(String name: names) {
			Double maxVal = Double.MIN_VALUE;
			TextStatistic best = null;
			
			for(Entry<TextStatistic, Double> entry: scoreMap.entrySet()) {
				if(entry.getKey().getName().equals(name) && entry.getValue()>maxVal) {
					best = entry.getKey();
					maxVal = entry.getValue();
				}
			}
			if(best != null) {
				suitableStatistics.add(best);
			}
		}
		return suitableStatistics;
	}
	
	private void notifyObservers() {
		for(Observer o: this.observers) {
			o.update(null, null);
		}
	}

	private static List<TextStatistic> myStats;
	static{
		myStats = new LinkedList<TextStatistic>();
		myStats.add(TextStatistic.getDummyStatistic());
		myStats.add(new TextStatistic("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")); //$NON-NLS-1$
		myStats.add(new TextStatistic("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ")); //$NON-NLS-1$
	}
	
	private PredefinedStatisticsProvider getDefaultStatisticsProvider() {
		return Activator.getPredefinedStatisticsProvider();
	}

	private AbstractAlphabet getDefaultAlphabetInitialization() {
		return AlphabetsManager.getInstance().getDefaultAlphabet();
	}

	public void setAlphabet(AbstractAlphabet newAlpha) {
		AbstractAlphabet oldAlpha = getAlphabet();
		this.alpha = newAlpha;
		reinitializePredefinedStatistics(oldAlpha, newAlpha, getSelectedStatistic(), isPredefinedStatistic());
	}
	
	public AbstractAlphabet getAlphabet() {
		return alpha;
	}
	
	public boolean isPredefinedStatistic() {
		return this.isPredefinedStatistic;
	}
	
	public TextStatistic getSelectedStatistic() {
		return this.selectedStatistic;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public boolean hasPredefinedStatistics() {
		return !predefinedStatistics.isEmpty();
	}

}
