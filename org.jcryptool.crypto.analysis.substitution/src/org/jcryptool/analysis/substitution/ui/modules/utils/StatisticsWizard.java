package org.jcryptool.analysis.substitution.ui.modules.utils;

import org.eclipse.jface.wizard.Wizard;
import org.jcryptool.analysis.substitution.calc.TextStatistic;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class StatisticsWizard extends Wizard {

	private StatisticsWizardPage page1;

	public StatisticsWizard(AbstractAlphabet alphabet) {
		setWindowTitle(Messages.StatisticsWizard_0);
		page1 = new StatisticsWizardPage(alphabet);
	}

	private TextStatistic statistics = null;
	
	@Override
	public void addPages() {
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		this.statistics = page1.getStatisticsLoader().getSelectedStatistic();
		return true;
	}
	
	public TextStatistic getStatistics() {
		return statistics;
	}

}
