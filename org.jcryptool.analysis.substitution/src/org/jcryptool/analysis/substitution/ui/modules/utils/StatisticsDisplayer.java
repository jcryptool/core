package org.jcryptool.analysis.substitution.ui.modules.utils;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.analysis.substitution.calc.TextStatistic;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;

public class StatisticsDisplayer extends Composite {

	private static final int BAR_SPACING = 3;
	private static final int BAR_HEIGHT = 12;
	private static final int SCROLL_COMP_WIDTH_HINT = 120;
	private static final int TEXT_WIDTH_MAX = 35;
	private static final int GRAPH_HORZ_SPACING = 0;
	private Color DEFAULT_BAR_COLOR;
	private Color DEFAULT_MAPPED_LABEL_COLOR;
	
	private TextStatistic referenceStatistic;
	private TextStatistic ciphertextStatistic;
	private List<Composite> referenceGraphs;
	private List<Composite> cipherGraphs;
	private Combo combo;
	private ScrolledComposite scReference;
	private ScrolledComposite scCipher;
	private Composite compReferenceMain;
	private Composite compCipherMain;
	
	private Map<Character, Character> charMapping;
	private boolean upperLowerCaseMode;
	private HashMap<String, Label> mappedLabels;
	private boolean isShowMappedLabels;
	private Button btnShowMappedLabels;
	private Composite layoutRoot;
	private IdentityHashMap<Control, String> controlDataMap;
	private AbstractAlphabet alphabet;
	private List<Observer> observers;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatisticsDisplayer(Composite parent, Composite layoutRoot, int style, TextStatistic referenceStatistic, TextStatistic ciphertextStatistic, AbstractAlphabet alphabet, boolean upperLowerCaseMode) {
		super(parent, style);
		this.alphabet = alphabet;
		observers = new LinkedList<Observer>();
		this.controlDataMap = new IdentityHashMap<Control, String>();
		
		this.layoutRoot = layoutRoot;
		this.upperLowerCaseMode = upperLowerCaseMode;
		charMapping = new HashMap<Character, Character>();
		this.mappedLabels = new HashMap<String, Label>();
		
		this.DEFAULT_BAR_COLOR = getDisplay().getSystemColor(SWT.COLOR_BLUE);
		this.DEFAULT_MAPPED_LABEL_COLOR = getDisplay().getSystemColor(SWT.COLOR_RED);
		
		this.referenceStatistic = referenceStatistic;
		this.ciphertextStatistic = ciphertextStatistic;
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblShowFrequenciesOf = new Label(composite, SWT.NONE);
		lblShowFrequenciesOf.setText(Messages.StatisticsDisplayer_0);
		
		combo = new Combo(composite, SWT.NONE);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems(new String[] {Messages.StatisticsDisplayer_1, Messages.StatisticsDisplayer_2, Messages.StatisticsDisplayer_3});
		
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
		
		btnShowMappedLabels = new Button(this, SWT.CHECK);
		btnShowMappedLabels.setText(Messages.StatisticsDisplayer_6);
		btnShowMappedLabels.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAllMappedLabels(btnShowMappedLabels.getSelection());
			}
		});
		
		Label lblClickHint = new Label(this, SWT.WRAP);
		GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		layoutData.widthHint = SCROLL_COMP_WIDTH_HINT*2;
		lblClickHint.setLayoutData(layoutData);
		lblClickHint.setText(Messages.StatisticsDisplayer_7 );
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblReferenceText = new Label(composite_1, SWT.NONE);
		lblReferenceText.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblReferenceText.setText(Messages.StatisticsDisplayer_4);
		
		Label lblCiphertext = new Label(composite_1, SWT.NONE);
		lblCiphertext.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblCiphertext.setText(Messages.StatisticsDisplayer_5);
		
		scReference = new ScrolledComposite(composite_1, SWT.BORDER | SWT.V_SCROLL);
		GridData scReferenceLData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		scReferenceLData.widthHint = SCROLL_COMP_WIDTH_HINT;
		scReference.setLayoutData(scReferenceLData);
		scReference.setExpandHorizontal(true);
		scReference.setExpandVertical(true);
		
		compReferenceMain = new Composite(scReference, SWT.NONE);
		compReferenceMain.setLayout(new GridLayout(1, false));
		
		referenceGraphs = fillSCWithStatistic(scReference, compReferenceMain, referenceStatistic, false);
		
		scReference.setContent(compReferenceMain);
		scReference.setMinSize(compReferenceMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		scCipher = new ScrolledComposite(composite_1, SWT.BORDER | SWT.V_SCROLL);
		GridData scCipherLData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		scCipherLData.widthHint = calcWidthHintCipherComp(this.isShowMappedLabels);
		scCipher.setLayoutData(scCipherLData);
		scCipher.setExpandHorizontal(true);
		scCipher.setExpandVertical(true);
		
		compCipherMain = new Composite(scCipher, SWT.NONE);
		compCipherMain.setLayout(new GridLayout(1, false));
		
		cipherGraphs = fillSCWithStatistic(scCipher, compCipherMain, ciphertextStatistic, true);
		
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
		
		showAllMappedLabels(this.isShowMappedLabels);
	}



	private void bindScrollbars() {
		final ScrollBar vBarC = scCipher.getVerticalBar();
		final ScrollBar vBarR = scReference.getVerticalBar();
		
		
		
		Listener listenerC = new Listener() {
            @Override
			public void handleEvent(Event e) {
                vBarR.setSelection(Math.min(vBarR.getMaximum(), vBarC.getSelection()));
                scReference.setOrigin(0, vBarC.getSelection());
            }
        };
        Listener listenerR = new Listener() {
            @Override
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


	private List<Composite> fillSCWithStatistic(ScrolledComposite scCipher, Composite compCipherMain, TextStatistic ciphertextStatistic2, boolean isCipher) {
		LinkedList<LinkedHashMap<String, Double>> data = new LinkedList<LinkedHashMap<String, Double>>();
		data.add(generateOrderedUnettsMap(ciphertextStatistic2));
		data.add(generateOrderedDoubletMap(ciphertextStatistic2));
		data.add(generateOrderedTripletMap(ciphertextStatistic2));
		
		LinkedList<Composite> graphs = new LinkedList<Composite>();
		for(LinkedHashMap<String, Double> graphData: data) {
			Composite graph = createGraphCompositeFromData(graphData, compCipherMain, isCipher);
			graphs.add(graph);
		}
		
		return graphs;
	}

	private Composite createGraphCompositeFromData(LinkedHashMap<String, Double> graphData, Composite compCipherMain, boolean isCipherBox) {
		double biggestDouble = graphData.isEmpty()?1:graphData.entrySet().iterator().next().getValue();
		
		Composite graph = new Composite(compCipherMain, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
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
			makeOneFreqItem(compCipherMain, graph, text, percentOfWidth, isCipherBox);
		}
		
		return graph;
	}



	private void makeOneFreqItem(Composite compCipherMain, Composite graph, final String text, double percentOfWidth, boolean isCipherBox) {
		int pixels = (int) Math.round(percentOfWidth*calcMaxBarWidth(compCipherMain));
		String textForLabel = text;
		textForLabel = applyUpperLowerCaseMode(isCipherBox, textForLabel);
		String textForMappedLabel = text;
		textForMappedLabel = generateMappedLabelString(text, this.charMapping);
		
		if(isCipherBox) {
			Label mappedLabel = new Label(graph, SWT.NONE);
			GridData layoutData = new GridData(SWT.TRAIL, SWT.CENTER, false, false);
			mappedLabel.setLayoutData(layoutData);
			mappedLabel.setText(textForMappedLabel);
			mappedLabel.setForeground(DEFAULT_MAPPED_LABEL_COLOR);
			mappedLabel.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
			
			mappedLabel.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_HAND));
			
			this.mappedLabels.put(text, mappedLabel);
			this.controlDataMap.put(mappedLabel, text);

			Label label = new Label(graph, SWT.NONE);
			label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
			label.setText('['+textForLabel+']');
			label.setCursor(getDisplay().getSystemCursor(SWT.CURSOR_HAND));
			setMappedLabelVisible(mappedLabel, this.isShowMappedLabels);
			label.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
			
			this.controlDataMap.put(label, text);
			
			MouseListener selectionListener = new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					Widget lbl = e.widget;
					String data = text;
					if(data != null) {
						displaySubstSetter(data, lbl);
					}
				}
			};
			mappedLabel.addMouseListener(selectionListener);
			label.addMouseListener(selectionListener);
			
			Composite bar = generateBar(graph, pixels, DEFAULT_BAR_COLOR, isCipherBox);
		} else {
			Composite bar = generateBar(graph, pixels, DEFAULT_BAR_COLOR, isCipherBox);
			
			Label label = new Label(graph, SWT.NONE);
			GridData layoutData = new GridData(SWT.TRAIL, SWT.CENTER, false, false);
			layoutData.horizontalIndent = BAR_SPACING;
			label.setLayoutData(layoutData);
			label.setText('['+textForLabel+']');
			label.setFont(SWTResourceManager.getFont("Courier New", 10, SWT.NORMAL));
			
			Label l = new Label(graph, SWT.NONE);
			l.setText(" "); //$NON-NLS-1$
		}
		
	}



	protected void displaySubstSetter(String data, Widget lbl) {
		Map<Character, Character> map = SubstitutionChooser.getMappingBySelectorShell(alphabet, data);
		if(map != null) {
			List<Character> removeSet = new LinkedList<Character>();
			for(Character c: map.keySet()) {

				if(map.get(c) == null) {
					removeSet.add(c);
				}
			}
			for(Character c: removeSet) map.remove(c);
			notifyParentAboutNewSubst(map);
		}
	}


	private void notifyParentAboutNewSubst(Map<Character, Character> map) {
		for(Observer o: observers) {
			o.update(null, map);
		}
	}


	private void setMappedLabelVisible(Label mappedLabel, boolean visible) {
		GridData lData = (GridData) mappedLabel.getLayoutData();
		mappedLabel.setVisible(visible);
		mappedLabel.setText(""); //$NON-NLS-1$
//		lData.exclude = !visible;
	}



	private String applyUpperLowerCaseMode(boolean isCipher, String text) {
		if(upperLowerCaseMode) {
			if(isCipher) {
				text = text.toUpperCase();
			} else {
				text = text.toLowerCase();
			}
		}
		return text;
	}

	private String generateMappedLabelString(String text, Map<Character, Character> charMapping) {
		char[] ciphertextStringArray = applyUpperLowerCaseMode(true, text).toCharArray();
		char[] plaintextStringArray = applyUpperLowerCaseMode(true, text).toCharArray();
		for (int i = 0; i < plaintextStringArray.length; i++) {
			char c = plaintextStringArray[i];
			Character subst = charMapping.get(c);
			if(subst != null) {
				subst = applyUpperLowerCaseMode(false, Character.toString(subst)).charAt(0);
				plaintextStringArray[i] = subst;
				ciphertextStringArray[i] = subst;
			}
		}
		String plaintext = String.valueOf(plaintextStringArray);
		String ciphertext = String.valueOf(ciphertextStringArray);
		return "["+ciphertext+"]"; //$NON-NLS-1$ //$NON-NLS-2$
	}



	private static Composite generateBar(Composite parent, int width, Color barColor, boolean isCipherBox) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridData layoutData = new GridData(isCipherBox?SWT.BEGINNING:SWT.TRAIL, SWT.CENTER, true, false);
		layoutData.widthHint = width;
		layoutData.heightHint = BAR_HEIGHT;
		if(isCipherBox) layoutData.horizontalIndent = BAR_SPACING;
		comp.setLayoutData(layoutData);
		comp.setBackground(barColor);
		return comp;
	}


	private double calcMaxBarWidth(Composite compCipherMain) {
		return SCROLL_COMP_WIDTH_HINT-TEXT_WIDTH_MAX-10;
	}

	public void setCharMapping(Map<Character, Character> mapping) {
		this.charMapping = new HashMap<Character, Character>(mapping);
		refreshMappedLabels();
	}

	protected void showAllMappedLabels(boolean selection) {
		this.isShowMappedLabels = selection;
		refreshMappedLabels();
		Control[] mappedLabelCtrls = new Control[this.mappedLabels.size()+2];
		mappedLabelCtrls[0] = scCipher;
		mappedLabelCtrls[1] = compCipherMain;
		int i=2;
		for(Entry<String, Label> entry: this.mappedLabels.entrySet()) {
			String labelData = entry.getKey();
			Label label = entry.getValue();
			
//			label.setText(generateMappedLabelString(labelData, charMapping));
			setMappedLabelVisible(label, selection);
			mappedLabelCtrls[i] = label;
			i++;
		}
		scCipher.setMinSize(compCipherMain.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		GridData layoutCipher = (GridData) scCipher.getLayoutData();
		layoutCipher.widthHint = calcWidthHintCipherComp(selection);
		refreshMappedLabels();
		layoutRoot.layout(mappedLabelCtrls);
	}
	
	private int calcWidthHintCipherComp(boolean mappedLabelsVisible) {
		return SCROLL_COMP_WIDTH_HINT+(mappedLabelsVisible?TEXT_WIDTH_MAX:0);
	}


	private void refreshMappedLabels() {
		for(Entry<String, Label> entry: this.mappedLabels.entrySet()) {
			Label label = entry.getValue();
			String data = entry.getKey();
			if(label.isVisible()) label.setText(generateMappedLabelString(data, this.charMapping));
		}
	}



	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void addObserver(Observer o) {
		this.observers.add(o);
	}
}
