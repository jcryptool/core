package org.jcryptool.crypto.analysis.substitution.views;


import java.lang.Thread.State;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.crypto.analysis.substitution.ui.modules.StatisticsSelector;
import org.jcryptool.crypto.analysis.substitution.ui.modules.SubstitutionAnalysisConfigPanel;
import org.jcryptool.crypto.analysis.substitution.ui.modules.TextLoadController;
import org.jcryptool.crypto.analysis.substitution.ui.wizard.loadtext.LoadTextWizard;
import org.jcryptool.crypto.analysis.substitution.views.SubstitutionAnalysisView.State.Step;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.textsource.TextInputWithSourceDisplayer;


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
	private Action action2;

	private Composite mainComposite;

	private State state;

	private Composite mainPanel;

	private Composite configPanel;

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
			this.configPanel = this.mainPanel;
		} else if(state.getStep() == Step.ANALYSIS) {
			//TODO: implement
		} else {
			throw new RuntimeException("unsupported state in substitution analysis");
		}
	}

	private void setMainPanel(SubstitutionAnalysisConfigPanel panel) {
		this.mainPanel = panel;
		getMainComposite().layout(new Control[]{this.mainPanel});
	}

	private SubstitutionAnalysisConfigPanel createConfigPanel(Composite parent) {
		SubstitutionAnalysisConfigPanel panel = new SubstitutionAnalysisConfigPanel(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return panel;
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
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