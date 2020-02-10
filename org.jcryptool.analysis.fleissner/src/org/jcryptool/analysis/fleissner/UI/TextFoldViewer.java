package org.jcryptool.analysis.fleissner.UI;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

public class TextFoldViewer extends Composite {
	private Text text;
	private List<TextPresentation> content = new LinkedList<TextPresentation>();
	private Map<TextPresentation, ExpandItem> expandItems = new IdentityHashMap<TextPresentation, ExpandItem>();
	public List<Runnable> redrawListeners = new LinkedList<Runnable>();
	private Composite parentComposite;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public TextFoldViewer(Composite parent, int style, List<TextPresentation> texts) {
		super(parent, style);
		this.parentComposite = parent;
		setLayout(new GridLayout(1, false));

		this.content.addAll(texts);
		for (TextPresentation pres : this.content) {
			this.addTextControls(pres);
		}
	}

	private void addTextControls(TextPresentation textpresentation) {
		ExpandBar expandBar = new ExpandBar(this, SWT.NONE);

		ExpandItem expandItem = new ExpandItem(expandBar, SWT.NONE);
		expandItem.setExpanded(true);
		expandItem.setText(textpresentation.getFirstLine());

		int flags = SWT.BORDER | SWT.MULTI;
		if (textpresentation.withScrollHorizontal) {
			flags = flags | SWT.H_SCROLL;
		} else {
			flags = flags | SWT.WRAP;
		}

		Composite textWrapper = new Composite(expandBar, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		textWrapper.setLayout(layout);

		text = new Text(expandBar, flags);
		text.setText(textpresentation.getLinesAfterFirstAsString());

		GridData textGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		GridData expandBarGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		if (textpresentation.tryFullLineWidth) {
			expandBarGridData.widthHint = text.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		}
		expandBar.setLayoutData(expandBarGridData);
		text.setLayoutData(textGridData);

		expandItem.setControl(text);

		computeExpandedHeight(textpresentation, expandItem, expandBar, text);

		ExpandListener expandA = new ExpandAdapter() {
			@Override
			public void itemCollapsed(ExpandEvent e) {
				onTextHeightChange(textpresentation, expandItem, expandBar, text);
			}

			@Override
			public void itemExpanded(ExpandEvent e) {
				onTextHeightChange(textpresentation, expandItem, expandBar, text);
			}
		};
		expandItem.setExpanded(textpresentation.expandedInitially);
		expandBar.addExpandListener(expandA);
		expandBar.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				for (Runnable listener : redrawListeners) {
					parentComposite.getShell().setSize(parentComposite.getShell().getSize().x,
							parentComposite.getShell().getSize().y + twiddleWindowSize());
					listener.run();
					System.out.println("resized");
				}
			}
		});
		expandItems.put(textpresentation, expandItem);
	}

	public ExpandItem getExpandItem(TextPresentation presentationItem) {
		return this.expandItems.get(presentationItem);
	}

	private void onTextHeightChange(TextPresentation textpresentation, ExpandItem xpndtmNewExpanditem,
			ExpandBar expandBar, Text text2) {
		// The commented-out snippets are tricks to force repaint and re-layout that
		// were not strictly necessary; for future reference.

		this.layout(true, true);
		this.requestLayout();
		this.getShell().layout(true, true);

		// Without this, some ExpandBar items mysteriously disappear until next window
		// resize
		parentComposite.getShell().setSize(parentComposite.getShell().getSize().x,
				parentComposite.getShell().getSize().y + this.twiddleWindowSize());

	}

	private int lastTwiddle = -1;

	private int twiddleWindowSize() {
		this.lastTwiddle = this.lastTwiddle * -1;
		return lastTwiddle;
	}

	private void computeExpandedHeight(TextPresentation textpresentation, ExpandItem expandItem, ExpandBar expandBar,
			Text textControl) {
		expandItem.setHeight(expandItem.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
