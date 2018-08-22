//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.analysis.substitution.ui.modules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.analysis.substitution.Activator;
import org.jcryptool.analysis.substitution.calc.TextStatistic;
import org.jcryptool.analysis.substitution.ui.modules.utils.StatisticsSelector;
import org.jcryptool.core.operations.algorithm.classic.textmodify.Transform;
import org.jcryptool.core.operations.algorithm.classic.textmodify.TransformData;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.ui.alphabets.AlphabetSelectorComposite;
import org.jcryptool.crypto.ui.textloader.ui.ControlHatcher;
import org.jcryptool.crypto.ui.textloader.ui.wizard.TextLoadController;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.util.NestedEnableDisableSwitcher;

public class SubstitutionAnalysisConfigPanel extends Composite {

	public static final String TEXTTRANSFORM_HINT = Messages.SubstitutionAnalysisConfigPanel_0;

	public static class State {
		public AbstractAlphabet getAlphabet() {
			return alphabet;
		}

		public TextStatistic getStatistics() {
			return statistics;
		}

		TextInputWithSource loadedText;
		TransformData selectedTransformation;
		AbstractAlphabet alphabet;
		TextStatistic statistics;
		boolean isStatisticPredefined;
		private SubstitutionAnalysisConfigPanel panel;
		private boolean ready;
		
		public State(SubstitutionAnalysisConfigPanel panel) {
			this.panel = panel;
			initializePanelState();
		}

		private void initializePanelState() {
			this.loadedText = null;
			this.selectedTransformation = null;
			this.alphabet = AlphabetsManager.getInstance().getDefaultAlphabet();
			this.statistics = null;
			this.isStatisticPredefined = false;
			writeToPanel();
		}

		private void writeToPanel() {
			panel.writeValues(this);
			updateEnabledStates();
		}
		
		private void updateEnabledStates() {
			boolean enabledStateAlphabet = calcEnabledStateAlphabet();
			boolean enabledStateStatistics = calcEnabledStateStatistics();
			boolean enabledStateStartAnalysis = calcEnabledStateStartAnalysis();
			if(panel.grpAlphabet.isEnabled()!=enabledStateAlphabet) panel.grpAlphabetEnabler.setEnabled(enabledStateAlphabet);
			if(panel.grpStatisticalData.isEnabled()!=enabledStateStatistics) panel.grpStatisticsEnabler.setEnabled(enabledStateStatistics);
			if(panel.grpAnalysisStart.isEnabled()!=enabledStateStartAnalysis) panel.grpAnalysisStartEnabler.setEnabled(enabledStateStartAnalysis);
			
			this.ready = enabledStateStartAnalysis; 
		}
		
		public boolean isReady() {
			return this.ready;
		}

		private boolean calcEnabledStateStartAnalysis() {
			return this.loadedText != null && this.alphabet != null && this.statistics != null;
		}

		private boolean calcEnabledStateStatistics() {
			return this.loadedText != null && this.alphabet != null;
		}

		private boolean calcEnabledStateAlphabet() {
			return this.loadedText != null;
		}

		public String getTextForAnalysis() {
			if(loadedText != null) {
				String text = loadedText.getText();
				if(this.selectedTransformation != null) {
					text = Transform.transformText(text, this.selectedTransformation);
				}
				
				TransformData alphabetAutoTransform = new TransformData();
				alphabetAutoTransform.setAlphabetTransformationON(true);
				alphabetAutoTransform.setSelectedAlphabet(this.alphabet);
				text = Transform.transformText(text, alphabetAutoTransform);
				text = replaceIllegalCharacters(text);
				return text;
			} else {
				return null;
			}
		}
		
		private String replaceIllegalCharacters(String text) {
			// TODO warn user
			String result = text.replaceAll(Pattern.quote("\n"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			result = result.replaceAll(Pattern.quote("\r"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			result = result.replaceAll(Pattern.quote("\t"), ""); //$NON-NLS-1$ //$NON-NLS-2$
			return result;
		}

		public static List<AbstractAlphabet> findSelectableAlphabetsForText(String text, TransformData transformation) {
			Set<Character> uniqueChars = getUniqueCharactersFromText(text, transformation);
			List<AbstractAlphabet> alphas = getSuitableAlphabetsForChars(uniqueChars);
			return alphas;
		}
		
		public static TextStatistic findPredefinedStatisticFor(AbstractAlphabet alphabet) {
			List<TextStatistic> suitableStatistics = StatisticsSelector.getSuitableStatistics(alphabet, Activator.getPredefinedStatisticsProvider());
			if(suitableStatistics.isEmpty()) {
				return null;
			} else {
				return suitableStatistics.get(0);
			}
		}
		
		public static AbstractAlphabet findFittingAlphabetForText(String text, TransformData transformation) {
			final Set<Character> uniqueCharactersInText = getUniqueCharactersFromText(text, transformation);
			final Map<Double, AbstractAlphabet> alphabetScores = new HashMap<Double, AbstractAlphabet>();
			List<AbstractAlphabet> suitableAlphabets = getSuitableAlphabetsForChars(uniqueCharactersInText);
			
			Double biggestScore = Double.MIN_VALUE;
			for (int i = 0; i < suitableAlphabets.size(); i++) {
				AbstractAlphabet alpha = suitableAlphabets.get(i);
				List<Character> alphaAsList = alphaContentAsList(alpha);
				
				Double similarityScore = TextStatistic.compareTwoAlphabets(alphaAsList, uniqueCharactersInText);
				if(biggestScore < similarityScore) biggestScore = similarityScore;
				alphabetScores.put(similarityScore, alpha);
			}
			
			if(biggestScore < 0) return AlphabetsManager.getInstance().getDefaultAlphabet();
			return alphabetScores.get(biggestScore);
		}

		private static Set<Character> getUniqueCharactersFromText(String text, TransformData transformation) {
			final Set<Character> uniqueCharactersInText = new HashSet<Character>();
			final String transformedText = Transform.transformText(text, transformation);
			for(final char c: transformedText.toCharArray()) uniqueCharactersInText.add(c);
			return uniqueCharactersInText;
		}

		public static List<AbstractAlphabet> getSuitableAlphabetsForChars(final Set<Character> uniqueCharactersInText) {
			List<AbstractAlphabet> suitableAlphabets;
			final AbstractAlphabet[] allAlphabets = AlphabetsManager.getInstance().getAlphabets();
			suitableAlphabets = getSuitableAlphabetsForTextCharacters(uniqueCharactersInText, allAlphabets);
			return suitableAlphabets;
		}

		public static List<AbstractAlphabet> getSuitableAlphabetsForTextCharacters(Set<Character> uniqueCharactersInText, AbstractAlphabet[] allAlphabets) {
			List<AbstractAlphabet> suitableAlphabets = new LinkedList<AbstractAlphabet>();
			for(AbstractAlphabet alpha: allAlphabets) {
				boolean suitable = isAlphabetSuitableForTextCharacters(uniqueCharactersInText, alpha);
				if(suitable) {
					suitableAlphabets.add(alpha);
				}
			}
			return suitableAlphabets;
		}

		public static boolean isAlphabetSuitableForTextCharacters(Set<Character> uniqueCharactersInText, AbstractAlphabet alpha) {
			boolean suitable = true;
			for(Character c: uniqueCharactersInText) {
				if(!alpha.contains(c)
						&& !( c==' ' || c== '\n' || c == '\r')
						) {
					suitable = false;
				}
			}
			return suitable;
		}
		
		private static List<Character> alphaContentAsList(AbstractAlphabet alpha) {
			LinkedList<Character> result = new LinkedList<Character>();
			for(char c: alpha.getCharacterSet()) result.add(c);
			return result;
		}

		public void notifyTextChange(TextInputWithSource text, TransformData transformData) {
			TextInputWithSource previousLoadedText = this.loadedText;
			
			this.loadedText = text;
			this.selectedTransformation = transformData;
			
			updateEnabledStates();
			newTextArrivedUpdates(previousLoadedText);
		}

		public void notifyStartButtonClicked() {
			panel.startAnalysis(getTextForAnalysis(), this.alphabet, this.statistics);
		}

		public void notifyStatisticChange(TextStatistic selectedStatistic, boolean predefinedStatistic) {
			TextStatistic previousStatistic = this.statistics;
			
			this.statistics = selectedStatistic;
			
			updateEnabledStates();
			newStatisticArrivedUpdate(previousStatistic);
		}

		public void notifyAlphabetChange(AbstractAlphabet content) {
			AbstractAlphabet previousAlphabet = this.alphabet;
			this.alphabet = content;
			
			updateEnabledStates();
			newAlphabetArrivedUpdates(previousAlphabet);
		}

		private void newTextArrivedUpdates(TextInputWithSource previousLoadedText) {
			if(this.loadedText != null) {
				AbstractAlphabet lastAlphabet = panel.getAlphaSelector().getAlphabetInput().getContent();
				
				TransformData tr = this.selectedTransformation!=null?this.selectedTransformation:new TransformData();
				List<AbstractAlphabet> selectableAlphabets = findSelectableAlphabetsForText(this.loadedText.getText(), tr);
				AbstractAlphabet bestAlphabet = findFittingAlphabetForText(this.loadedText.getText(), tr);
				panel.getAlphaSelector().setAcceptedAlphabets(selectableAlphabets);
				panel.getAlphaSelector().getAlphabetInput().writeContent(bestAlphabet);
				panel.getAlphaSelector().getAlphabetInput().synchronizeWithUserSide();
				
				if(lastAlphabet.equals(bestAlphabet)) {
					notifyAlphabetChange(bestAlphabet);
				}
			}
		}

		private void newStatisticArrivedUpdate(TextStatistic previousStatistic) {
			// nothing to do...
		}

		private void newAlphabetArrivedUpdates(AbstractAlphabet previousAlphabet) {
			if(this.alphabet != null) {
				panel.getStatisticsSelector().setAlphabet(this.alphabet);
				if(panel.getStatisticsSelector().hasPredefinedStatistics()) {
					panel.getStatisticsSelector().setPredefinedStatistics(0, true);
				}
			}
		}
		
	}
	
	private static final int WIDTH_HINT_GENERAL = 300;
	private Group grpCiphertext;
	private Group grpAlphabet;
	private Group grpStatisticalData;
	private Group grpAnalysisStart;
	private AlphabetSelectorComposite alphaSelector;
	private StatisticsSelector statisticsSelector;
	private TextLoadController textSelector;
	private Button startBtn;
	private NestedEnableDisableSwitcher grpCiphertextEnabler;
	private NestedEnableDisableSwitcher grpAlphabetEnabler;
	private NestedEnableDisableSwitcher grpStatisticsEnabler;
	private NestedEnableDisableSwitcher grpAnalysisStartEnabler;
	private State panelState;
	private LinkedList<Observer> observers;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SubstitutionAnalysisConfigPanel(Composite parent, int style) {
		super(parent, style);
		this.observers = new LinkedList<Observer>();
//		GridLayout thisLayout = new GridLayout(1, false);
//		thisLayout.verticalSpacing = 13;
//		setLayout(thisLayout);
		setLayout(new GridLayout());
		
		Label lblSubstitutionAnalysis = new Label(this, SWT.NONE);
		lblSubstitutionAnalysis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//		lblSubstitutionAnalysis.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD)); //$NON-NLS-1$
		lblSubstitutionAnalysis.setFont(FontService.getHeaderFont());
		lblSubstitutionAnalysis.setText(Messages.SubstitutionAnalysisConfigPanel_8);
		
		Label lblBlablabla = new Label(this, SWT.NONE);
//		GridData lblBlablablaLData = new GridData(SWT.FILL, SWT.CENTER, true, false);
//		lblBlablablaLData.verticalIndent = 5;
//		lblBlablabla.setLayoutData(lblBlablablaLData);
		lblBlablabla.setText(Messages.SubstitutionAnalysisConfigPanel_9);
		
		grpCiphertext = new Group(this, SWT.NONE);
		grpCiphertext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		grpCiphertext.setText(Messages.SubstitutionAnalysisConfigPanel_10);
		grpCiphertext.setLayout(new GridLayout(1, false));
		
		Label lblPleaseSelectA = new Label(grpCiphertext, SWT.NONE);
		lblPleaseSelectA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		lblPleaseSelectA.setText(Messages.SubstitutionAnalysisConfigPanel_11);
		
		textSelector = new TextLoadController(grpCiphertext, this, SWT.NONE, true, true);
		textSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
		textSelector.setControlHatcherAfterWizText(new ControlHatcher.LabelHatcher(
				TEXTTRANSFORM_HINT
				));
		makeTextSelectorListener();
		
		grpAlphabet = new Group(this, SWT.NONE);
		grpAlphabet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		grpAlphabet.setText(Messages.SubstitutionAnalysisConfigPanel_12);
		grpAlphabet.setLayout(new GridLayout(2, false));
		
		Label lblTheAlphabetWhich = new Label(grpAlphabet, SWT.WRAP);
		GridData lblTheAlphabetWhichLData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		lblTheAlphabetWhich.setLayoutData(lblTheAlphabetWhichLData);
		lblTheAlphabetWhichLData.widthHint = WIDTH_HINT_GENERAL;
		lblTheAlphabetWhich.setText(Messages.SubstitutionAnalysisConfigPanel_13);
		
		Label lblNewLabel = new Label(grpAlphabet, SWT.NONE);
		lblNewLabel.setText(Messages.SubstitutionAnalysisConfigPanel_14);
		
		alphaSelector = new AlphabetSelectorComposite(grpAlphabet, AlphabetsManager.getInstance().getDefaultAlphabet(), AlphabetSelectorComposite.Mode.SINGLE_COMBO_BOX_WITH_CUSTOM_ALPHABETS);
		alphaSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
		makeAlphaSelectorListener();
		
		grpStatisticalData = new Group(this, SWT.NONE);
		grpStatisticalData.setLayout(new GridLayout(1, false));
		grpStatisticalData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpStatisticalData.setText(Messages.SubstitutionAnalysisConfigPanel_15);
		
		Label lblStatistics = new Label(grpStatisticalData, SWT.WRAP);
		GridData lblStatisticsLData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		lblStatistics.setLayoutData(lblStatisticsLData);
		lblStatisticsLData.widthHint = WIDTH_HINT_GENERAL;
		lblStatistics.setText(Messages.SubstitutionAnalysisConfigPanel_16+ "\n" + //$NON-NLS-1$ 
				Messages.SubstitutionAnalysisConfigPanel_18);
		
		statisticsSelector = new StatisticsSelector(grpStatisticalData, this, SWT.NONE);
		statisticsSelector.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
		makeStatisticsListener();
		
		grpAnalysisStart = new Group(this, SWT.NONE);
		grpAnalysisStart.setLayout(new GridLayout(1, false));
		grpAnalysisStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpAnalysisStart.setText(Messages.SubstitutionAnalysisConfigPanel_19);
		
		Label lblStartAnalysis = new Label(grpAnalysisStart, SWT.WRAP);
		GridData lblStartAnalysisLData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		lblStartAnalysis.setLayoutData(lblStartAnalysisLData);
		lblStartAnalysisLData.widthHint = WIDTH_HINT_GENERAL;
		lblStartAnalysis.setText(Messages.SubstitutionAnalysisConfigPanel_20+"\n" + //$NON-NLS-1$ 
				Messages.SubstitutionAnalysisConfigPanel_22);
		
		startBtn = new Button(grpAnalysisStart, SWT.PUSH);
		startBtn.setText(Messages.SubstitutionAnalysisConfigPanel_23);
		makeStartBtnListener();
		
		grpCiphertextEnabler = new NestedEnableDisableSwitcher(grpCiphertext);
		grpAlphabetEnabler = new NestedEnableDisableSwitcher(grpAlphabet);
		grpStatisticsEnabler = new NestedEnableDisableSwitcher(grpStatisticalData);
		grpAnalysisStartEnabler = new NestedEnableDisableSwitcher(grpAnalysisStart);
		
		this.panelState = new State(this);
	}

	public void startAnalysis(String textForAnalysis, AbstractAlphabet alphabet, TextStatistic statistics) {
		notifyObservers();
	}

	public void addObserver(Observer o) {
		this.observers.add(o);
	}
	
	private void notifyObservers() {
		for(Observer o: this.observers) {
			o.update(null, null);
		}
	}

	public void writeValues(State state) {
		textSelector.setTextData(state.loadedText, state.selectedTransformation, false);
		
		if(state.loadedText != null) {
			String tempText = state.loadedText.getText();
			TransformData tr = state.selectedTransformation!=null?state.selectedTransformation:new TransformData();
			
			List<AbstractAlphabet> acceptedAlphabets = State.findSelectableAlphabetsForText(tempText, tr);
			alphaSelector.setAcceptedAlphabets(acceptedAlphabets);
		} else {
			alphaSelector.setAcceptedAlphabetsToAll();
		}
		
		AbstractAlphabet alphaToWrite;
		if(state.alphabet != null) {
			alphaToWrite = state.alphabet;
		} else {
			if(state.loadedText != null) {
				
				alphaToWrite = State.findFittingAlphabetForText(
						state.loadedText.getText(), 
						state.selectedTransformation!=null?state.selectedTransformation:new TransformData());
			} else {
				alphaToWrite = AlphabetsManager.getInstance().getDefaultAlphabet();
			}
		}
		alphaSelector.getAlphabetInput().writeContent(state.alphabet);
		alphaSelector.getAlphabetInput().synchronizeWithUserSide();
		
		
		if(state.isStatisticPredefined) {
			statisticsSelector.setPredefinedStatistics(state.statistics, true);
		} else {
			//TODO: save source and transformData in state or think about sth else;
			//shortcut taken here... just setting default data for source and transformation
			statisticsSelector.setCustomStatistics(state.statistics, new TextInputWithSource("abcde"), new TransformData(), true); //$NON-NLS-1$
		}
	}

	private void makeStartBtnListener() {
		startBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(getState() != null) {
					getState().notifyStartButtonClicked();
				}
			}
		});
	}

	private void makeTextSelectorListener() {
		textSelector.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(getTextSelector().getText() != null) {
					if(getState() != null) {
						getState().notifyTextChange(getTextSelector().getText(), getTextSelector().getTransformData());
					}
				}
			}
		});
	}

	private void makeStatisticsListener() {
		statisticsSelector.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(getState() != null) {
					getState().notifyStatisticChange(getStatisticsSelector().getSelectedStatistic(), getStatisticsSelector().isPredefinedStatistic());
				}
			}
		});
	}

	private void makeAlphaSelectorListener() {
		getAlphaSelector().getAlphabetInput().addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				if(getState() != null) {
					getState().notifyAlphabetChange(getAlphaSelector().getAlphabetInput().getContent());
				}
			}
		});
	}

	public State getState() {
		return this.panelState;
	}
	
	public TextLoadController getTextSelector() {
		return textSelector;
	}
	
	public AlphabetSelectorComposite getAlphaSelector() {
		return alphaSelector;
	}
	
	public StatisticsSelector getStatisticsSelector() {
		return statisticsSelector;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
