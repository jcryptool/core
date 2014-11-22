package algorithmstool.ui;

import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import algorithmstool.model.retrievers.Retriever;

public class RetrieverViewer extends Composite {

	private Retriever retriever;
	private Observer o;
	private boolean displayLink = false;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RetrieverViewer(Retriever retriever, Composite parent, int spacing, int style) {
		this(retriever, parent, spacing, style, null);
	}

	public RetrieverViewer(Retriever retriever, Composite parent, int spacing, int style, Observer oForConfigs) {
		super(parent, style);
		this.retriever = retriever;
		this.o = oForConfigs;
		
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = spacing;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Label label = new Label(this, SWT.NONE);
		label.setText(retriever.getName());
		
		if(displayLink) {
			Link link = new Link(this, SWT.NONE);
			link.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			link.setText("<a>(konfigurieren)</a>");
			link.setCursor(new Cursor(getDisplay(), SWT.CURSOR_HAND));
			link.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean configured = RetrieverViewer.this.retriever.configure(getShell());
					if(configured) {
						RetrieverViewer.this.notifyObserver();
					}
				}
			});
		}

	}
	
	private void notifyObserver() {
		if(this.o != null) this.o.update(null, null);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
