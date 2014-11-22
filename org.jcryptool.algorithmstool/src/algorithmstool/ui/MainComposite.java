package algorithmstool.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.crypto.flexiprovider.algorithms.ui.views.providers.FlexiProviderAlgorithmsViewContentProvider;
import org.jcryptool.crypto.flexiprovider.ui.nodes.ITreeNode;

import algorithmstool.model.IAlgorithmDescr;
import algorithmstool.model.retrievers.Retriever;
import algorithmstool.model.retrievers.impl.DocViewCryptoRetriever;
import algorithmstool.model.retrievers.impl.DocViewRestRetriever;
import algorithmstool.model.retrievers.impl.FlexiProviderRetriever;

@SuppressWarnings("serial")
public class MainComposite extends Composite {

	private List<Retriever> retrievers;
	private DocViewCryptoRetriever retrDocViewAlgos;

	private DocViewRestRetriever retrAnalysis;

	private DocViewRestRetriever retrVis;

	private DocViewRestRetriever retrGames;

	private RetrieversViewer rV;

	private Observer retrieverCfgObserver;

	private RetrieverListViewer table;
	
	private Button retryFP;
	private Composite fpComp;
	private Group rVGroup;
	private List<FlexiProviderRetriever> fpRetrievers;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MainComposite(Composite parent, int style) {
		super(parent, style);
		makeDefaultRetrievers();
		
		generateUI();
		renderDisplay();
	}

	private List<Retriever> makeDefaultRetrievers() {
		this.retrievers = new LinkedList<Retriever>();
		
		String stubDescrDoc = "Dokumentenansicht > ";
		final List<String> stubPathDoc = new LinkedList<String>(){{add("[D]");}};
		
		String name;
		LinkedList<String> basePath;
		String descr;
		String extPt;
		
		name = "Algorithmen";
		basePath = appendList(stubPathDoc, name);
		descr = stubDescrDoc + "Algorithmen";
		retrDocViewAlgos = new DocViewCryptoRetriever(basePath, descr);
		retrievers.add(retrDocViewAlgos);
		
		extPt = "org.jcryptool.core.operations.analysis";
		name = "Analysen";
		basePath = appendList(stubPathDoc, name);
		descr = stubDescrDoc + name;
		retrAnalysis = new DocViewRestRetriever(basePath, extPt, descr);
		retrievers.add(retrAnalysis);
		
		extPt = "org.jcryptool.core.operations.visuals";
		name = "Visualisierungen";
		basePath = appendList(stubPathDoc, name);
		descr = stubDescrDoc + name;
		retrVis = new DocViewRestRetriever(basePath, extPt, descr);
		retrievers.add(retrVis);
		
		extPt = "org.jcryptool.core.operations.games";
		name = "Spiele";
		basePath = appendList(stubPathDoc, name);
		descr = stubDescrDoc + name;
		retrGames = new DocViewRestRetriever(basePath, extPt, descr);
		retrievers.add(retrGames);
		
		if(FlexiProviderRetriever.canInitialize()) {
			loadFPRetrievers();
		}
		
		return retrievers;
	}

	private void loadFPRetrievers() {
		this.fpRetrievers = new LinkedList<FlexiProviderRetriever>();
		
		String name;
		List<String> basePath;
		String descr;
		
		String stubDescrDoc = "FP > ";
		final List<String> stubPathDoc = new LinkedList<String>(){{add("[A]");}};
		
		ITreeNode root = FlexiProviderAlgorithmsViewContentProvider._invisibleRoot;
		for(Object n: root.getChildrenArray()) {
			ITreeNode node = (ITreeNode) n;
			
			name = node.getName();
			basePath = appendList(stubPathDoc, name);
			descr = stubDescrDoc + name;
			FlexiProviderRetriever retr = new FlexiProviderRetriever(basePath, node, descr);
			retrievers.add(retr);
			fpRetrievers.add(retr);
		}
	}

	private LinkedList<String> appendList(final List<String> stubPathDoc, final String name) {
		return new LinkedList<String>() {{ addAll(stubPathDoc); add(name); }};
	}

	private void generateUI() {
		
		this.retrieverCfgObserver = new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				renderDisplay();
			}
		};
		
		//-----------

		setLayout(new GridLayout(2, false));
		
		GridData rVGroupLayoutData = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
		createRetrieversViewerGroup(this, rVGroupLayoutData);
		
		GridData dispGroupLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		createDisplayGroup(this, dispGroupLayoutData);
	}

	private void createDisplayGroup(Composite parent, GridData dispGroupLayoutData) {
		Group dispGroup = new Group(parent, SWT.NONE);
		dispGroup.setLayout(new GridLayout());
		dispGroup.setLayoutData(dispGroupLayoutData);
		dispGroup.setText("Algorithmen");
		
		this.table = new RetrieverListViewer(dispGroup, SWT.NONE, Collections.EMPTY_LIST, retrieverCfgObserver);
		this.table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label lblClickInfo = new Label(dispGroup, SWT.WRAP);
		GridData lblCILLD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lblCILLD.widthHint = 600;
		lblClickInfo.setLayoutData(lblCILLD);
		lblClickInfo.setText("Durch Doppelklick auf einen Eintrag der Tabelle wird der Optionsdialog zum zugehörigen Algorithmen-Knoten (s. links); aufgerufen. Darin kann der Stammpfad für alle Elemente dieses Knotens angepasst werden.");
		
		Label lblSortInfo = new Label(dispGroup, SWT.WRAP);
		GridData lblSI = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lblSI.widthHint = 600;
		lblSortInfo.setLayoutData(lblSI);
		lblSortInfo.setText("Sortieren der Spalten ist möglich. STRG+Click auf die Spalten setzt die Sortierung wieder zurück.");
	}

	private void createRetrieversViewerGroup(Composite parent, GridData layoutData) {
		rVGroup = new Group(parent, SWT.NONE);
		rVGroup.setLayout(new GridLayout());
		rVGroup.setLayoutData(layoutData);
		rVGroup.setText("Algorithmen-Knoten");
		
		this.rV = new RetrieversViewer(rVGroup, SWT.None, this.retrievers, retrieverCfgObserver);
		this.rV.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Button exportBtn = new Button(rVGroup, SWT.PUSH);
		exportBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		exportBtn.setText("Export des Tabelleninhalts...");
		exportBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MainComposite.this.exportClicked();
			}
		});
		
		if(FlexiProviderRetriever.canInitialize()) {
			//Do nothing, should be already loaded
		} else {
			makeFPLoadArea(rVGroup);
		}
	}

	private void makeFPLoadArea(Group rVGroup) {
		fpComp = new Composite(rVGroup, SWT.NONE);
		fpComp.setLayout(new GridLayout(2, false));
		
		Label l = new Label(fpComp, SWT.WRAP);
		l.setText("Die Flexiprovider-Algorithmen konnten noch nicht geladen werden. Dazu muss zuerst einmal die Algorithmen/FP Perspektive geöffnet worden sein. Mit dem folgenden Button können Sie dann die FP-Algorithmen-Retriever laden.");
		GridData lData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lData.widthHint = 250;
		l.setLayoutData(lData);
		
		Button b = new Button(fpComp, SWT.PUSH);
		b.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
		b.setText("Refresh");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fpRefresh();
			}
		});
		
		
	}

	private void hideFPArea() {
		if(this.fpComp != null) {
			this.fpComp.dispose();
			MainComposite.this.layout();
		}
	}
	
	protected void fpRefresh() {
		if(FlexiProviderRetriever.canInitialize()) {
			loadFPRetrievers();
			for(Control c: this.getChildren()) {
				c.dispose();
			}
			this.generateUI();
			layout(true);
			this.renderDisplay();
		} else {
			MessageDialog.openInformation(getShell(), ":-/", "Der FlexiProvider- Algorithmenbaum konnte nicht gefunden werden. Die Algorithmen-View muss einmal geladen worden sein.");
		}
	}

	protected void exportClicked() {
		List<IAlgorithmDescr> algos = new LinkedList<IAlgorithmDescr>(this.table.getSortedAlgos());
		ExportWizard wizard = new ExportWizard(" > ", "\t", algos);
		WizardDialog dialog = new WizardDialog(getShell(), wizard);
		int result = dialog.open();
	}

	protected void renderDisplay() {
		List<? extends Retriever> rs = getEnabledRetrievers();
		
		table.setRetrievers(rs);
	}

	private List<? extends Retriever> getEnabledRetrievers() {
		List<Retriever> result = new LinkedList<Retriever>();
		for(Retriever r: this.retrievers) {
			if(this.rV.isRetrieverSelected(r)) {
				result.add(r);
			}
		}
		return result;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
