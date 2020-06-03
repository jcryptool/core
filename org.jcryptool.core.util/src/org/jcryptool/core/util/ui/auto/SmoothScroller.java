package org.jcryptool.core.util.ui.auto;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;

/**
 * This class is an (hopefully) easy to use class to
 * have an effect if the user scrolls its scroll wheel.</br>
 * There are some SWT controls that do not forward a 
 * scroll event to a scrollable control and so a scroll event 
 * sometimes got lost. This is pretty annoying. So this class
 * fixes this wrong behaviour and something scrolls if the 
 * user scrolls its mouse wheel.
 * 
 * @author Thorben Groos (thorben.groos@web.de)
 *
 */
public class SmoothScroller {
	
	private static SmoothScroller defaultInstance;
 
	/**
	 * The use of the singleton here is used, to allow 
	 * an easy use of the smooth scroller via a static call
	 * of SmoothScroller.scrollSmooth(ScrolledComposite).</br>
	 * This static method calls the get instance method to 
	 * enabl accessing the argument (ScrolledComposite).
	 * @return An instance of the smooth scroller.
	 */
	private static SmoothScroller getInstance() {
		if (SmoothScroller.defaultInstance == null) {
			SmoothScroller.defaultInstance = new SmoothScroller();
		}
		return SmoothScroller.defaultInstance;
	}
	
	/**
	 * Call this method on the top level scrolled composite.</br>
	 * Not on "parent" given by <code>createPartControl(Composite parent)</code>
	 * but on its first child.
	 * @param c
	 */
	public static void scrollSmooth(ScrolledComposite sc) {
		SmoothScroller sm = SmoothScroller.getInstance();
		sm.addTo(sc);
	}
	
	/**
	 * This method checks, if a control has one or more of the following SWT-flags set.
	 * <code>SWT.V_SCROLL</code>, <code> SWT.H_SCROLL</code> or <code>SWT.READ_ONLY</code>
	 * and adds a <code>MouseWheelListener</code> to it, that propagates a scroll Event (
	 * occurs when you scroll your mouse wheel) to the scrollbar of the scrolledComposite
	 * that is given in the <code>scrollSmooth</code>-Method (of course, only if the
	 * scrollEvent has no effect on the control where the mouse is on).</br></br>
	 * Notice: This method calls itself (recursive call).
	 * 
	 * @param control The control for that the MouseWheelListener should be added.
	 */
	private void addTo(Control control) {
		for (Control child : ((Composite) control).getChildren()) {
			// We only want to add the MouseWheel Listener to SWT Texts.
			if (child instanceof Text) {
				// Texts with one of the following SWT-tags prevent 
				// a scolledComposite from being scrolled, if the user
				// scrolls on its mouse wheel.
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
