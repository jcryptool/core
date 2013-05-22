package org.jcryptool.crypto.analysis.substitution.ui.modules.utils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.jcryptool.crypto.analysis.substitution.calc.TextStatistic;

public class StatisticsDisplayer extends Composite {

	private static final int BAR_HEIGHT = 12;
	private static final int SCROLL_COMP_WIDTH_HINT = 120;
	private static final int GRAPH_HORZ_SPACING = 2;
	private TextStatistic referenceStatistic;
	private TextStatistic ciphertextStatistic;
	private Color DEFAULT_BAR_COLOR;
	private List<Composite> referenceGraphs;
	private List<Composite> cipherGraphs;
	private Combo combo;
	private ScrolledComposite scReference;
	private ScrolledComposite scCipher;
	private Composite compReferenceMain;
	private Composite compCipherMain;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatisticsDisplayer(Composite parent, int style, TextStatistic referenceStatistic, TextStatistic ciphertextStatistic) {
		super(parent, style);
		
		this.DEFAULT_BAR_COLOR = getDisplay().getSystemColor(SWT.COLOR_BLUE);
		
		this.referenceStatistic = referenceStatistic;
		this.ciphertextStatistic = ciphertextStatistic;
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblShowFrequenciesOf = new Label(composite, SWT.NONE);
		lblShowFrequenciesOf.setText("Show frequencies of:");
		
		combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems(new String[] {"Single characters", "Character pairs", "Character triples"});
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = combo.getSelectionIndex();
				if(index > -1) {
					selectGraphType(index);
				} else {
					combo.select(0);
				}
			}
		});
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(2, true);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblReferenceText = new Label(composite_1, SWT.NONE);
		lblReferenceText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblReferenceText.setText("Reference text");
		
		Label lblCiphertext = new Label(composite_1, SWT.NONE);
		lblCiphertext.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblCiphertext.setText("Ciphertext");
		
		scReference = new ScrolledComposite(composite_1, SWT.BORDER | SWT.V_SCROLL);
		GridData scReferenceLData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		scReferenceLData.widthHint = SCROLL_COMP_WIDTH_HINT;
		scReference.setLayoutData(scReferenceLData);
		scReference.setExpandHorizontal(true);
		scReference.setExpandVertical(true);
		
		compReferenceMain = new Composite(scReference, SWT.NONE);
		compReferenceMain.setLayout(new GridLayout(1, false));
		
		referenceGraphs = fillSCWithStatistic(scReference, compReferenceMain, referenceStatistic);
		
		scReference.setContent(compReferenceMain);
		scReference.setMinSize(compReferenceMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		scCipher = new ScrolledComposite(composite_1, SWT.BORDER | SWT.V_SCROLL);
		GridData scCipherLData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		scCipherLData.widthHint = SCROLL_COMP_WIDTH_HINT;
		scCipher.setLayoutData(scCipherLData);
		scCipher.setExpandHorizontal(true);
		scCipher.setExpandVertical(true);
		
		compCipherMain = new Composite(scCipher, SWT.NONE);
		compCipherMain.setLayout(new GridLayout(1, false));
		
		cipherGraphs = fillSCWithStatistic(scCipher, compCipherMain, ciphertextStatistic);
		
		scCipher.setContent(compCipherMain);
		scCipher.setMinSize(compCipherMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		bindScrollbars();
		initialize();

	}

	
	
	private void initialize() {
		selectGraphType(1);
	}



	private void showObject(Control control, boolean show) {
		GridData layoutData = (GridData) control.getLayoutData();
		layoutData.exclude = !show;
		control.setVisible(show);
	}
	
	private void selectGraphType(int index) {
		for(int i=0; i<3; i++) {
			showObject(referenceGraphs.get(i), index==i);
			showObject(cipherGraphs.get(i), index==i);
		}
		scReference.setMinSize(compReferenceMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scCipher.setMinSize(compCipherMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		this.layout(new Control[]{referenceGraphs.get(index), cipherGraphs.get(index), scReference, scCipher});
		
		if(combo.getSelectionIndex() != index) {
			combo.select(index);
		}
	}



	private void bindScrollbars() {
		final ScrollBar vBarC = scCipher.getVerticalBar();
		final ScrollBar vBarR = scReference.getVerticalBar();
		
		
		
		Listener listenerC = new Listener() {
            public void handleEvent(Event e) {
                vBarR.setSelection(Math.min(vBarR.getMaximum(), vBarC.getSelection()));
                scReference.setOrigin(0, vBarC.getSelection());
            }
        };
        Listener listenerR = new Listener() {
            public void handleEvent(Event e) {
            	vBarC.setSelection(Math.min(vBarC.getMaximum(), vBarR.getSelection()));
            	scCipher.setOrigin(0, vBarR.getSelection());
            }
        };
        
        vBarC.addListener(SWT.Selection, listenerC);
        vBarR.addListener(SWT.Selection, listenerR);
        
	}

	
	private static LinkedHashMap<String, Double> generateOrderedUnettsMap(TextStatistic statistic) {
		LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
		for(Character key: statistic.getCharacterOccurrences().keySet()) {
			result.put(key.toString(), statistic.getFrequencyForCharacter(key));
		}
		return result;
	}
	private static LinkedHashMap<String, Double> generateOrderedTripletMap(TextStatistic statistic) {
		LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
		for(String key: statistic.getTripletOccurrences().keySet()) {
			result.put(key.toString(), statistic.getFrequencyForTriplet(key));
		}
		return result;
	}
	private static LinkedHashMap<String, Double> generateOrderedDoubletMap(TextStatistic statistic) {
		LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
		for(String key: statistic.getDoubletOccurrences().keySet()) {
			result.put(key.toString(), statistic.getFrequencyForDoublet(key));
		}
		return result;
	}


	private List<Composite> fillSCWithStatistic(ScrolledComposite scCipher, Composite compCipherMain, TextStatistic ciphertextStatistic2) {
		LinkedList<LinkedHashMap<String, Double>> data = new LinkedList<LinkedHashMap<String, Double>>();
		data.add(generateOrderedUnettsMap(ciphertextStatistic2));
		data.add(generateOrderedDoubletMap(ciphertextStatistic2));
		data.add(generateOrderedTripletMap(ciphertextStatistic2));
		
		LinkedList<Composite> graphs = new LinkedList<Composite>();
		for(LinkedHashMap<String, Double> graphData: data) {
			Composite graph = createGraphCompositeFromData(graphData, compCipherMain);
			graphs.add(graph);
		}
		
		return graphs;
	}

	private Composite createGraphCompositeFromData(LinkedHashMap<String, Double> graphData, Composite compCipherMain) {
		double biggestDouble = graphData.isEmpty()?1:graphData.entrySet().iterator().next().getValue();
		
		Composite graph = new Composite(compCipherMain, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = GRAPH_HORZ_SPACING;
		graph.setLayout(layout);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		layoutData.exclude = true;
		graph.setLayoutData(layoutData);
		graph.setVisible(false);
		
		for(Map.Entry<String, Double> entry: graphData.entrySet()) {
			String text = entry.getKey();
			double percentOfWidth = entry.getValue()/biggestDouble;
			int pixels = (int) Math.round(percentOfWidth*(double)calcMaxBarWidth(compCipherMain));
			
			Label label = new Label(graph, SWT.NONE);
			label.setText('['+text+']');
			Composite bar = generateBar(graph, pixels, DEFAULT_BAR_COLOR);
		}
		
		return graph;
	}

	private static Composite generateBar(Composite parent, int width, Color barColor) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridData layoutData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
		layoutData.widthHint = width;
		layoutData.heightHint = BAR_HEIGHT;
		comp.setLayoutData(layoutData);
		comp.setBackground(barColor);
		return comp;
	}


	private double calcMaxBarWidth(Composite compCipherMain) {
		return SCROLL_COMP_WIDTH_HINT-35;
	}



	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
