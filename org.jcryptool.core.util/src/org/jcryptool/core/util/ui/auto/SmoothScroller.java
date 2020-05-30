package org.jcryptool.core.util.ui.auto;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;

public class SmoothScroller {
	
	private static SmoothScroller defaultInstance;
 
	private static SmoothScroller getInstance() {
		if (SmoothScroller.defaultInstance == null) {
			SmoothScroller.defaultInstance = new SmoothScroller();
		}
		return SmoothScroller.defaultInstance;
	}
	
	/**
	 * Call this method on the top level composite.</br>
	 * Not on "parent" given by <code>createPartControl(Composite parent)</code>
	 * but on its first child.
	 * @param c
	 */
	public static void scrollWidgetSmooth(ScrolledComposite sc) {
		SmoothScroller sm = SmoothScroller.getInstance();
		sm.addTo(sc);
	}
	
	private void addTo(Control sc) {
		for (Control child : ((Composite) sc).getChildren()) {
			if (child instanceof Text) {
				if (ScrollingUtils.controlHasFlag(child, new int[] {SWT.V_SCROLL, SWT.H_SCROLL, SWT.READ_ONLY})) {
					child.addMouseWheelListener(new MouseWheelListener() {
						
						@Override
						public void mouseScrolled(MouseEvent e) {
							ScrollingUtils.ScrollableControl thisAsScrollable;
							if ((child.getStyle() & SWT.V_SCROLL) != 0) {
								thisAsScrollable = new ScrollingUtils.ScrollableControl(child, ((Scrollable) child).getVerticalBar());
							} else if ((child.getStyle() & SWT.H_SCROLL) != 0) {
								thisAsScrollable = new ScrollingUtils.ScrollableControl(child, ((Scrollable) child).getHorizontalBar());
							} else {
								// This is the case if the Text has the SWT.READ_ONLY tag.
								thisAsScrollable = new ScrollingUtils.ScrollableControl(child, null);
							}
							thisAsScrollable.propagateScrollIfNecessary(e);
						}
					});
				}
				
			}

			
			
			// Das macht den rekursiven Aufruf der Methode.
			// Es prüft, ob das aktuelle Control ein Composite ist
			// oder eine Klasse die davon erbt. Nur dann soll die
			// Methode aufgerufen werden, da andere Klassen nicht
			// die Methode getChildren() haben, mit der ich in 
			// Zeile 40 die childs eines Controls hole.
			Class<?> cls = child.getClass();
			if (Composite.class.isAssignableFrom(cls)) {
				addTo(child);
			}
		}

	}
	
	

}
