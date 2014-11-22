package algorithmstool.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.jcryptool.core.logging.utils.LogUtil;

import algorithmstool.Activator;
import algorithmstool.model.retrievers.Retriever;

public class RetrieversViewer extends Composite {

	private List<? extends Retriever> retrievers;
	private LinkedList<Button> checkBtns;
	private Observer o;
	private Observer oForConfigs;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RetrieversViewer(Composite parent, int style, List<? extends Retriever> retrievers, Observer o) {
		super(parent, style);
		this.retrievers = retrievers;
		this.o = o;
		
		this.oForConfigs = new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				RetrieversViewer.this.notifyObserver();
			}
		};
		
		makeUI();
	}
	
	private void notifyObserver() {
		this.o.update(null, null);
	}

	private void makeUI() {
		this.checkBtns = new LinkedList<Button>();
		
		Layout gridLayout = new GridLayout(2, false);
		this.setLayout(gridLayout);
		
		for(Retriever r: this.retrievers) {
			Button cb = new Button(this, SWT.CHECK);
			cb.setLayoutData(new GridData());
			cb.setSelection(true);
			cb.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					RetrieversViewer.this.notifyObserver();
				}
			});
			this.checkBtns.add(cb);
			
			RetrieverViewer v = new RetrieverViewer(r, this, 10, SWT.NONE, this.oForConfigs);
			v.setLayoutData(new GridData());
		}
	}
	
	public List<? extends Retriever> getRetrievers() {
		return retrievers;
	}
	
	public Boolean isRetrieverSelected(Retriever retriever) {
		List<Boolean> data = this.getBtnData();
		int pos = this.getRetrievers().indexOf(retriever);
		if(pos<0) {
			LogUtil.logError(Activator.PLUGIN_ID, "retriever object not found in retrieverViewer");
			return false;
		} else {
			return data.get(pos);
		}
	}
	
	
	
	public List<Boolean> getBtnData() {
		List<Boolean> result = new LinkedList<Boolean>();
		for(Button cb: this.checkBtns) {
			result.add(cb.getSelection());
		}
		return result;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
