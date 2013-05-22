package org.jcryptool.crypto.analysis.substitution.views;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.crypto.analysis.substitution.calc.TextStatistic;
import org.jcryptool.crypto.analysis.substitution.ui.modules.SubstitutionAnalysisConfigPanel;
import org.jcryptool.crypto.analysis.substitution.ui.modules.SubstitutionAnalysisPanel;
import org.jcryptool.crypto.analysis.substitution.views.SubstitutionAnalysisView.State.Step;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SubstitutionAnalysisView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jcryptool.crypto.analysis.substitution.views.SubstitutionAnalysisView";

	private Action action1;
	private Composite mainComposite;

	private State state;

	private Composite mainPanel;

	private SubstitutionAnalysisConfigPanel configPanel;

	private SubstitutionAnalysisPanel analysisPanel;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	public static class State {
		private Step step;

		public enum Step {
			CONFIG, ANALYSIS
		}
		
		public State(Step step) {
			this.step = step;
		}
		
		public Step getStep() {
			return step;
		}
	}
	
	/**
	 * The constructor.
	 */
	public SubstitutionAnalysisView() {
		this.state = new State(State.Step.CONFIG);
	}
	
	private Composite getMainComposite() {
		return mainComposite;
	}
	
	public void createPartControl(Composite parent) {
		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		
		createAppropriatePanel(this.state);
		
		makeActions();
		contributeToActionBars();
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getMainComposite(), "org.jcryptool.crypto.analysis.substitution.viewer");
	}

	private void createAppropriatePanel(State state) {
		if(this.mainPanel != null && !this.mainPanel.isDisposed()) {
			this.mainPanel.dispose();
		}
		if(state.getStep() == Step.CONFIG) {
			SubstitutionAnalysisConfigPanel panel = createConfigPanel(mainComposite);
			setMainPanel(panel);
			this.configPanel = panel;
		} else if(state.getStep() == Step.ANALYSIS) {
			org.jcryptool.crypto.analysis.substitution.ui.modules.SubstitutionAnalysisConfigPanel.State data = this.configPanel.getState();
			SubstitutionAnalysisPanel panel = createAnalysisPanel(mainComposite, data.getTextForAnalysis(), data.getAlphabet(), data.getStatistics());
			setMainPanel(panel);
			this.analysisPanel = panel;
		} else {
			throw new RuntimeException("unsupported state in substitution analysis");
		}
	}

	private SubstitutionAnalysisPanel createAnalysisPanel(Composite mainComposite, String textForAnalysis, AbstractAlphabet alphabet,
			TextStatistic statistics) {
		SubstitutionAnalysisPanel substitutionAnalysisPanel = new SubstitutionAnalysisPanel(mainComposite, SWT.NONE, textForAnalysis, alphabet, statistics, generateUpperLowerCaseMode(alphabet));
		substitutionAnalysisPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return substitutionAnalysisPanel;
	}

	private static boolean generateUpperLowerCaseMode(AbstractAlphabet alphabet) {
		String ref = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		LinkedList<Character> refList = new LinkedList<Character>();
		for(char c: ref.toCharArray()) refList.add(c);
		
		Set<Character> compareAlphaSet = new HashSet<Character>();
		for(char c: alphabet.getCharacterSet()) compareAlphaSet.add(c);
		
		double compareValue = TextStatistic.compareTwoAlphabets(refList, compareAlphaSet);
		if(compareValue > 0.9) return true;
		return false;
	}

	private void setMainPanel(Composite panel) {
		this.mainPanel = panel;
		getMainComposite().layout(new Control[]{this.mainPanel});
	}

	private SubstitutionAnalysisConfigPanel createConfigPanel(Composite parent) {
		final SubstitutionAnalysisConfigPanel panel = new SubstitutionAnalysisConfigPanel(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		panel.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				if(panel.getState().isReady()) {
					state = new State(State.Step.ANALYSIS);
					createAppropriatePanel(state);
				}
			}
		});
		return panel;
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				resetAnalysis();
			}
		};
		action1.setText("Reset analysis");
		action1.setToolTipText("Starts the analysis over.");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		
	}

	protected void resetAnalysis() {
		this.state = new State(Step.CONFIG);
		createAppropriatePanel(state);
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(
				getMainComposite().getShell(),
			"Substitution analysis",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		getMainComposite().setFocus();
	}
}