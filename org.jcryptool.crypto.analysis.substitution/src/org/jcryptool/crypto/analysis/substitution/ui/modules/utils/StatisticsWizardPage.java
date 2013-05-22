package org.jcryptool.crypto.analysis.substitution.ui.modules.utils;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.analysis.substitution.ui.modules.StatisticsSelector;

public class StatisticsWizardPage extends WizardPage {

	private StatisticsSelector statisticsLoader;
	private AbstractAlphabet alphabet;

	/**
	 * Create the wizard.
	 */
	public StatisticsWizardPage(AbstractAlphabet alphabet) {
		super("Reference statistic");
		this.alphabet = alphabet;
		setTitle("Reference statistic");
		setDescription("Load a reference statistic for the substitution analysis.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());
		
		statisticsLoader = new StatisticsSelector(container, container, SWT.NONE);
		statisticsLoader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		statisticsLoader.setAlphabet(alphabet);
		
		statisticsLoader.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				StatisticsWizardPage.this.setPageComplete(statisticsLoader.getSelectedStatistic() != null);
			}
		});
		
		StatisticsWizardPage.this.setPageComplete(statisticsLoader.getSelectedStatistic() != null);
		setControl(container);
	}

	public StatisticsSelector getStatisticsLoader() {
		return statisticsLoader;
	}

}
