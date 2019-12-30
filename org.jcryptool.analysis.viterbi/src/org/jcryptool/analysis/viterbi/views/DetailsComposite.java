// -----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2011, 2020 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
// -----END DISCLAIMER-----
package org.jcryptool.analysis.viterbi.views;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.analysis.viterbi.algorithm.Path;
import org.jcryptool.analysis.viterbi.algorithm.Viterbi;
import org.jcryptool.analysis.viterbi.algorithm.Viterbi.IterationRecord;

/**
 * This class generates the content of the "Viterbi" tab. With this tab the user can break the running key cipher
 * created in the first tab.
 *
 * @author Georg Chalupar, Niederwieser Martin, Scheuchenpflug Simon
 */
public class DetailsComposite extends Composite {
    private static final int LIMIT_CONST = 50;
	/* set default values */
//    private static final int HORIZONTAL_SPACING = 15;
//    private static final int MARGIN_WIDTH = 5;

//    private static final int LOADBUTTONHEIGHT = 30;
//    private static final int LOADBUTTONWIDTH = 120;

//    private static final int CONTINUEBUTTONHEIGHT = 30;
//    private static final int CONTINUEBUTTONWIDTH = 150;

    /* colors for backgrounds. */
//    private static final Color WHITE = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

	private ViterbiView viterbiView;
	private Table table;
	
	
	
	private Optional<Viterbi> finishedAnalysis; // recalculate all, refresh all
	private Map<Integer, IterationRecord> records() {
		return this.finishedAnalysis.get().records;
	}
	
	private int minStep() {
		return this.records().keySet().stream().min(Comparator.comparingInt(i->i)).get();
	}
	private int maxStep() {
		return this.records().keySet().stream().max(Comparator.comparingInt(i->i)).get();
	}
	private List<Path> pathsForStep(Integer step) {
		return records().get(step).paths;
	}
	private List<PathAddr> addressesFor(Integer step) {
		return IntStream.range(0, records().get(step).paths.size()).mapToObj(i -> new PathAddr(step, i))
				.collect(Collectors.toList());
	}
	private PathAddr winningAddress() {
		return new PathAddr(records().size()-1, 0);
	}
	
	private boolean track1Selected = true; // display refresh all
	private Integer currentStep;
	private Integer currentRank;
	
	private PathAddr currentAddr() {
		return new PathAddr(currentStep, currentRank);
	}

	private Optional<PathAddr> highlightedPath; // recalculate paths, refresh all
	private Optional<List<PathAddr>> derivedHighlights;
	private Text dyns_rightPathDisplay;
	private Text dyns_leftPathDisplay;
	private Text dyns_winning;
	private Combo dyn_combo;
	private Table table_1;
	private Optional<List<PathAddr>> calcHighlightedPaths() {
		BiFunction<String, String, Boolean> matches = (String recordP, String highlP) -> {
			int prefixL = Math.min(highlP.length(), recordP.length());
			return recordP.substring(0, prefixL).equals(highlP.substring(0,  prefixL));
		};
		Optional<List<PathAddr>> paths = highlightedPath.map(a -> 
			IntStream.range(minStep(), maxStep()+1)
				.mapToObj(step -> addressesFor(step).stream())
				.flatMap(pathStream -> pathStream.filter(
						recordP -> matches.apply(recordP.resolve(), a.resolve())
						)).collect(Collectors.toList())
			);
		return paths;
	}

	private class PathAddr {
		public int step;
		public int rank;
		public PathAddr(int step, int rank) {
			super();
			this.step = step;
			this.rank = rank;
		}
		
		private String resolve() {
			Path path = records().get(step).paths.get(rank);
			return DetailsComposite.this.pathToString(path);
		}
		private Path resolvePath() {
			Path path = records().get(step).paths.get(rank);
			return path;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof PathAddr) {
				PathAddr pathAddr = (PathAddr) obj;
				return this.rank == pathAddr.rank && this.step == pathAddr.step;
			} else {
				return false;
			}
		}

	}

    /**
     * Creates the tab
     *
     */
    public DetailsComposite(final Composite parent, final int style, ViterbiView viterbiView) {
        super(parent, style);
		this.viterbiView = viterbiView;
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 15;
		setLayout(gridLayout);
		
		Label lblViterbiAnalysisDetails = new Label(this, SWT.NONE);
		lblViterbiAnalysisDetails.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblViterbiAnalysisDetails.setText(Messages.DetailsComposite_lblViterbiAnalysisDetails_text);
		lblViterbiAnalysisDetails.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblViterbiAnalysisDetails.setBackground(SWTResourceManager.getColor(255, 255, 255));
		
		Text lblInThisComposite = new Text(this, SWT.WRAP);
		GridData lbl1LData = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		lbl1LData.widthHint = 400;
		lblInThisComposite.setLayoutData(lbl1LData);
		lblInThisComposite.setText(Messages.DetailsComposite_0);
		
		Composite composite_2 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(3, false);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblWinningPath = new Label(composite_2, SWT.NONE);
		lblWinningPath.setText(Messages.DetailsComposite_1);
		
		dyns_winning = new Text(composite_2, SWT.BORDER);
		dyns_winning.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		dyns_winning.setSize(434, 31);
		dyns_winning.setText(Messages.DetailsComposite_2);
		dyns_winning.setEditable(false);
		
		Label lblPlaintextOr = new Label(composite_2, SWT.NONE);
		lblPlaintextOr.setText(Messages.DetailsComposite_3);
		
		Button btn_r1 = new Button(composite_2, SWT.RADIO);
		btn_r1.setSelection(true);
		btn_r1.setText(Messages.DetailsComposite_4);
		
		Button btn_r2 = new Button(composite_2, SWT.RADIO);
		btn_r2.setText(Messages.DetailsComposite_5);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		composite.setLayout(new GridLayout(7, false));
		
		Label lblStep = new Label(composite, SWT.NONE);
		lblStep.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblStep.setText(Messages.DetailsComposite_6);
		
		Button btn_first = new Button(composite, SWT.NONE);
		btn_first.setText("<<"); //$NON-NLS-1$
		
		Button btn_back = new Button(composite, SWT.NONE);
		btn_back.setText("<"); //$NON-NLS-1$
		
		dyn_combo = new Combo(composite, SWT.NONE);
		GridData gd_dyn_combo = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_dyn_combo.widthHint = 50;
		gd_dyn_combo.minimumWidth = 50;
		dyn_combo.setLayoutData(gd_dyn_combo);
		
		Button btn_fwd = new Button(composite, SWT.NONE);
		btn_fwd.setText(">"); //$NON-NLS-1$
		
		Button btn_last = new Button(composite, SWT.NONE);
		btn_last.setText(">>"); //$NON-NLS-1$
		
		Label lblCandidatePathsOf = new Label(composite, SWT.NONE);
		lblCandidatePathsOf.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 7, 1));
		lblCandidatePathsOf.setText(Messages.DetailsComposite_lblCandidatePathsOf_text);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnRank = new TableColumn(table, SWT.NONE);
		tblclmnRank.setWidth(65);
		tblclmnRank.setText(Messages.DetailsComposite_11);
		
		TableColumn tblclmnPp = new TableColumn(table, SWT.NONE);
		tblclmnPp.setWidth(108);
		tblclmnPp.setText(Messages.DetailsComposite_12);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(700);
		tblclmnNewColumn.setText(Messages.DetailsComposite_13);
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		composite_1.setLayout(new GridLayout(3, false));
		
		Button btnHighlightAndLook = new Button(composite_1, SWT.NONE);
		btnHighlightAndLook.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		btnHighlightAndLook.setText(Messages.DetailsComposite_14);
		
		Button btnClear = new Button(composite_1, SWT.NONE);
		btnClear.setText(Messages.DetailsComposite_15);
		
		Label lblPredecessorsOfSelected = new Label(composite_1, SWT.NONE);
		lblPredecessorsOfSelected.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblPredecessorsOfSelected.setText(Messages.DetailsComposite_16);
		new Label(composite_1, SWT.NONE);
		
		table_1 = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		TableColumn tblclmnStep = new TableColumn(table_1, SWT.NONE);
		tblclmnStep.setWidth(65);
		tblclmnStep.setText(Messages.DetailsComposite_tblclmnStep_text);
		
		TableColumn tblclmnRank_1 = new TableColumn(table_1, SWT.NONE);
		tblclmnRank_1.setWidth(65);
		tblclmnRank_1.setText(Messages.DetailsComposite_tblclmnRank_1_text);
		
		TableColumn tableColumn_2 = new TableColumn(table_1, SWT.NONE);
		tableColumn_2.setWidth(700);
		tableColumn_2.setText(Messages.DetailsComposite_xnew7);
		
		Label lblHighlighted_1 = new Label(composite_1, SWT.NONE);
		lblHighlighted_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHighlighted_1.setText(Messages.DetailsComposite_17);
		
		dyns_rightPathDisplay = new Text(composite_1, SWT.BORDER);
		dyns_rightPathDisplay.setEditable(false);
		dyns_rightPathDisplay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Label lblSelected = new Label(composite, SWT.NONE);
		lblSelected.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSelected.setText(Messages.DetailsComposite_18);
		
		dyns_leftPathDisplay = new Text(composite, SWT.BORDER);
		dyns_leftPathDisplay.setEditable(false);
		dyns_leftPathDisplay.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		
        table.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = table.getSelectionIndex();
        		tableSelected(idx);
        	}
		});
        
        dyn_combo.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = Integer.parseInt(dyn_combo.getText());
        		if(idx != -1 && idx != currentStep) {
        			navigate(new PathAddr(idx, 1));
        		}
        	}
        });
        
        btn_back.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = currentStep - 1;
        		if(idx != -1 && idx != currentStep) {
        			navigate(new PathAddr(idx, 1));
        		}
        		super.widgetSelected(e);
        	}
        });
        btn_fwd.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = currentStep + 1;
        		if(idx <= maxStep() && idx != currentStep) {
        			navigate(new PathAddr(idx, 1));
        		}
        		super.widgetSelected(e);
        	}
        });
        btn_first.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = minStep();
        		if(idx <= maxStep() && idx != currentStep) {
        			navigate(new PathAddr(idx, 1));
        		}
        		super.widgetSelected(e);
        	}
        });
        btn_last.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = maxStep();
        		if(idx <= maxStep() && idx != currentStep) {
        			navigate(new PathAddr(idx, 1));
        		}
        		super.widgetSelected(e);
        	}
        });

        btn_r1.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		track1Selected = btn_r1.getSelection();
        		refreshDisplay();
        	}
		});
        btn_r2.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		track1Selected = ! btn_r2.getSelection();
        		refreshDisplay();
        	}
        });
        btnClear.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		setHighlighted(Optional.empty());
        	}
		});
        btnHighlightAndLook.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		if(highlightedPath.isPresent() && highlightedPath.get().rank == currentRank && 
        				highlightedPath.get().step == currentStep) {
        			return;
        		}
        		setHighlighted(Optional.of(new PathAddr(currentStep, currentRank)));
        	}
        });
        table_1.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		int idx = table_1.getSelectionIndex();
        		if(idx < 0) return;
        		PathAddr addr = derivedHighlights.get().get(idx);
        		if(! addr.equals(currentAddr())){
        			navigate(addr);
        		}
        	}
		});
    }

    public void setHighlighted(Optional<PathAddr> addr) {
    	this.highlightedPath = addr;
    	this.derivedHighlights = calcHighlightedPaths();
    	
    	displayHighlightsFiltered(new LinkedList<>());
    	if(! this.derivedHighlights.isPresent()) return;
    	
    	displayHighlightsFiltered(this.derivedHighlights.get());
    	navigate(currentAddr());
    	updateGlobalLabels();
    }
    
    public void setAnalysis(Viterbi viterbi) {
    	this.finishedAnalysis = Optional.of(viterbi);
    	for(int i=minStep(); i<=maxStep(); i++) {
    		this.dyn_combo.add(""+i); //$NON-NLS-1$
    	}
    	
    	setHighlighted(Optional.empty());
    	navigate(winningAddress());
    }
    
	protected void refreshDisplay() {
		//TODO: sloppy implementation but it does
		navigate(new PathAddr(currentStep, currentRank));
		if(this.derivedHighlights.isPresent()) {
				displayHighlightsFiltered(this.derivedHighlights.get());
		}
	}

	protected void tableSelected(int idx) {
		currentRank = idx;
		updateGlobalLabels();
		
//		if(!derivedHighlights.isPresent()) return;
//		List<PathAddr> list2 = this.derivedHighlights.get();
//		for (int i = 0; i < list2.size(); i++) {
//			PathAddr x = list2.get(i);
//			if(x.equals(currentAddr())) {
//				list.setSelection(i);
//			}
//		}
	}

	private void navigate(PathAddr pathAddr) {
		this.currentStep = pathAddr.step;
		this.currentRank = pathAddr.rank;
		
		displayTableContent(pathsForStep(currentStep));
		selectTableEntry(currentRank);
		this.dyn_combo.setText(""+pathAddr.step); //$NON-NLS-1$
		
		updateGlobalLabels();
	}
	
	private void updateGlobalLabels() {
		String lblCurrentPath = new PathAddr(currentStep, currentRank).resolve();
		String lblHighlighted = 
				highlightedPath.map(h -> String.format(Messages.DetailsComposite_21, h.step, h.rank+1, limitTo(h.resolve(), 10000))).orElse(Messages.DetailsComposite_22);
		
		dyns_leftPathDisplay.setText(lblCurrentPath);
		dyns_rightPathDisplay.setText(lblHighlighted);
		dyns_winning.setText(winningAddress().resolve());
		
		dyns_leftPathDisplay.setSelection(dyns_leftPathDisplay.getText().length()-1);
		dyns_rightPathDisplay.setSelection(dyns_rightPathDisplay.getText().length()-1);
		dyns_winning.setSelection(dyns_winning.getText().length()-1);
	}
	private void selectTableEntry(Integer currentRank) {
		table.setSelection(currentRank);
//		scrollbar = currentRank / (table.getVerticalBar().getMaximum())
//		table.getVerticalBar().setSelection();
	}
	
	private void displayTableContent(List<Path> pathsForStep) {
		table.removeAll();
		
		table.clearAll();
		for (int i = 0; i < pathsForStep.size(); i++) {
			Path p = pathsForStep.get(i);
			String displ = limitTo(pathToString(p), LIMIT_CONST);
			Double prob = p.getProbability();
			
			TableItem ti = new TableItem(table, SWT.NONE);
			if(derivedHighlights.isPresent() && derivedHighlights.get().stream().map(a->a.resolvePath()).anyMatch(q -> q.equals(p))) {
				ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			}
			ti.setText(new String[]{""+(i+1), ""+prob, displ}); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	private String limitTo(String s, int i) {
		
		return s.length() > i ? "..."+s.substring(s.length()-i) : s; //$NON-NLS-1$
	}

	private void displayHighlightsFiltered(List<PathAddr> derivedHighlights2) {
		table_1.removeAll();
		table_1.clearAll();
		derivedHighlights2.forEach(h -> {
			Path p = h.resolvePath();
			String displ = limitTo(pathToString(p), LIMIT_CONST);
			
			TableItem ti = new TableItem(table_1, SWT.NONE);
			ti.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			ti.setText(new String[]{""+(h.step), ""+(h.rank+1), displ}); //$NON-NLS-1$ //$NON-NLS-2$
		});
	}
	public String pathToString(Path path) {
		return this.track1Selected ? path.getPlain1() : path.getPlain2();
	}
}
