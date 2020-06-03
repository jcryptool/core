package org.jcryptool.core.util.ui.auto;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Widget;

/**
 * This class does all the logic work for the SmoothScroller.java.</br></br>
 * It works like that: It checks if the mouse wheel is scrolled
 * (this is done by the mouseWheelListener in the SmoothScroller class)
 * and then checks if the scrolling would have an effect (with effect I
 * mean some control scrolls or not). If yes it does nothing, but if not
 * it forwards this scroll event to the next scrollable control in the
 * parent structure and then this control is scrolled.
 * 
 * @author Simon Leischnig
 *
 */
public class ScrollingUtils {

	// assumed immutable by convention
	protected static class ScrollableControl {
		public static int UP = 1;
		public static int DOWN = -1;

		public Control control;
		public ScrollBar scrollBar;

		/**
		 * Creates an instance of the class <code>ScrollableControl</code>
		 * @param control A scrollable control like a scrolledComposite.
		 * @param scrollBar The scrollbar of the scrollable control. You can get it with 
		 * <code>control.getVerticalBar()</code>.
		 */
		public ScrollableControl(Control control, ScrollBar scrollBar) {
			super();
			this.control = control;
			this.scrollBar = scrollBar;
		}

		/**
         *
         * Takes any widget (superclass of Control), tests whether the
         * widget can be wrapped with the class "ScrollableControl",
         * and returns the appropriate Optional<ScrollableControl>.
         * In the context of SmoothScroller, this is effectively the test
         * of whether to propagate scroll-whell signals to that control or not
         * 
		 * @param w the widget
		 * @return an Optional of that class if, and an empty Optional otherwise.
		 */
		public static Optional<ScrollableControl> ofWidgetOptional(Widget w) {
			if (!(w instanceof Control)) {
				return Optional.empty();
			}
			Control c = (Control) w;

			if (c instanceof ScrolledComposite) {
				ScrolledComposite scrolledComposite = (ScrolledComposite) c;
				return Optional.of(new ScrollableControl(scrolledComposite, scrolledComposite.getVerticalBar()));
			}
			
//			if (c instanceof Text || c instanceof StyledText) {
			if (c instanceof Scrollable) {
				Scrollable text = (Scrollable) c;
				if (ScrollingUtils.controlHasFlag(text, new int[] {SWT.V_SCROLL, SWT.H_SCROLL, SWT.READ_ONLY})) {
					if (text.getVerticalBar() != null) {
						return Optional.of(new ScrollableControl(text, text.getVerticalBar()));
					} else if (text.getHorizontalBar() != null) {
						return Optional.of(new ScrollableControl(text, text.getHorizontalBar()));
					} 
				}
			}
			return Optional.empty();
		}

		/**
		 * This method performs the scroll of a scrolling MouseEvent which is assumed to
		 * be left-over.</br></br>
		 * This method only handles ScrolledComposites; all else would throw 
		 * exception. But this is no problem, because it is only added to ScroledComposite. 
		 * The SmoothScroller class takes care of that.
		 * @param count The amount of lines the scrollbar should be scroller.</br>
		 * This is 3 (scroll up) or -3 (scroll down) by default (at least on windows).
		 */
		public void scrollByCount(int count) {
			this.scrollBar.setSelection(this.scrollBar.getSelection() - count * this.scrollBar.getIncrement());
			if (this.control instanceof ScrolledComposite) {
				ScrolledComposite sc = (ScrolledComposite) this.control;
				Point currentOrigin = sc.getOrigin();
				sc.setOrigin(currentOrigin.x, currentOrigin.y - count * this.scrollBar.getIncrement());
			} else {
				throw new RuntimeException(
						"Incomplete distinction of scrollable Controls -- only ScrolledComposite is handled at the moment. FIXME!");
			}
			this.control.redraw();
		}

		/**
		 * Forwards a mouse event (scrolling of the mouse wheel) to the 
		 * scrollable control that should be scrolled instead of the 
		 * control the mouse is currently on.
		 * @param e The scroll event from the mouseWheelListener.
		 */
		public void propagateScrollIfNecessary(MouseEvent e) {
			// test if the event has any effect
			if (ScrollingUtils.isScrollEventWithoutEffectHere(this.scrollBar, e)) {
				// get scrollable controls up the SWT tree
				List<ScrollableControl> scrollableAbove = ScrollingUtils.getScrollableWidgetsAbove(this.control);
				// ... only those that have scrolling place left
				List<ScrollableControl> scrollableWithSpaceAbove = scrollableAbove.stream()
						.filter(x -> !ScrollingUtils.isScrollEventWithoutEffectHere(x.scrollBar, e))
						.collect(Collectors.toList());

				// if there are any such scroll receivers, scroll the first (nearest) one!
				// TODO: see [1] below
				if (!scrollableWithSpaceAbove.isEmpty()) {
					ScrollableControl firstWithSpaceLeft = scrollableWithSpaceAbove.get(0);
					firstWithSpaceLeft.scrollByCount(e.count);
				}
			}
		}
	}

	// [1]
	// if we want to be 100% correct, we have to take into account the possibility,
	// that one scroll may be divided into
	// - the part that was effectively scrolled
	// - the part that has not been scrolled and is left over (and could possible be
	// divided up into all if that effect cascaded there, too)
	// ... but this is total overkill in complexity here.
	//
	// however, since .setOrigin(...) is called naively on scrolledComposites,
	// there is the (hyptothetical! not tested) possibility of a scrolled composite
	// to push its content out in the negative direction.

	public static List<Control> getParentsOf(Widget widget) {
		if (!(widget instanceof Control)) {
			return new LinkedList<>();
		}
		Control control = (Control) widget;
		Composite thisParent = control.getParent();
		List<Control> result = new LinkedList<Control>();
		if (thisParent != null) {
			result.add(thisParent);
			List<Control> thoseParents = getParentsOf(thisParent);
			result.addAll(thoseParents);
		}
		return result;
	}

	/**
	 * Checks if the control has one or more flags set.
	 * @param c 
	 * @param flags
	 * @return True, if the control has one or more of the given flags set. False, if
	 * none of the given flags is set.
	 */
	public static boolean controlHasFlag(Control c, int[] flags) {
		boolean result = false;
		for (int flag : flags) {
			if ((c.getStyle() & flag) != 0) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * This method searches for scrollable control in the parent structure
	 * of a control.
	 * @param control The control for which you want to check whether it is within a scrollable control.
	 * @return A maybe empty list with scrollable controls that are around/contain
	 * the given control.
	 */
	public static List<ScrollableControl> getScrollableWidgetsAbove(Control control) {
		return getParentsOf(control).stream()
				.map(ctrl -> ScrollableControl.ofWidgetOptional(ctrl))
				.filter(x -> x.isPresent())
				.map(x -> x.get())
				.collect(Collectors.toList());
	}

	/**
	 * Checks if the innerScrollableBar reached the top or bottom position.
	 * 
	 * @param innerScrollableBar The scrollbar of the control the mouse is currently on. Can be null,
	 * if there is no scrollbar (this is the case when SWT.READ_ONLY is set as only flag.)
	 * @param e The MouseEvent gotten from the mouseWheelListener (see SmoothScroller.java)
	 * @return True, if the scrollbar reached the top or bottom. False, if the
	 *         scrollbar is somewhere between the top and bottom.
	 */
	public static boolean isScrollEventWithoutEffectHere(ScrollBar innerScrollableBar, MouseEvent e) {

		int direction = e.count < 0 ? ScrollableControl.DOWN : ScrollableControl.UP;

		if (innerScrollableBar != null) {
			int minimalPos = innerScrollableBar.getMinimum();
			int currentPos = innerScrollableBar.getSelection();
			int maximalPos = innerScrollableBar.getMaximum();
			int thumbSize = innerScrollableBar.getThumb();

			if (direction == ScrollableControl.UP && currentPos <= minimalPos) {
				return true;
			}
			if (direction == ScrollableControl.DOWN && currentPos + thumbSize >= maximalPos) {
				return true;
			}
			return false;
		}
		// Scrolling is without effect, because the control has no scrollbar at all.
		return true;
	}

}
