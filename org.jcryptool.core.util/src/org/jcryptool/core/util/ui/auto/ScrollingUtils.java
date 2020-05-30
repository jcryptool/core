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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class ScrollingUtils {

	// assumed immutable by convention
	public static class ScrollableControl {
		public static int UP = 1;
		public static int DOWN = -1;

		public Control control;
		public ScrollBar scrollBar;

		public ScrollableControl(Control control, ScrollBar scrollBar) {
			super();
			this.control = control;
			this.scrollBar = scrollBar;
		}

		public static Optional<ScrollableControl> ofWidgetOptional(Widget w) {
			if (!(w instanceof Control)) {
				return Optional.empty();
			}
			Control c = (Control) w;

			if (c instanceof ScrolledComposite) {
				ScrolledComposite scrolledComposite = (ScrolledComposite) c;
				return Optional.of(new ScrollableControl(scrolledComposite, scrolledComposite.getVerticalBar()));
			}
			if (c instanceof Text) {
				Text text = (Text) c;
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

		// this method performs the scroll of a scrolling MouseEvent which is assumed to
		// be left-over
		// TODO: this method only handles ScrolledComposites; all else would throw
		// exception
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

		public void propagateScrollIfNecessary(MouseEvent e) {
			// test if the event has any effect
			if (ScrollingUtils.isScrollEventWithoutEffectHere(this.control, this.scrollBar, e)) {
				// get scrollable controls up the SWT tree
				List<ScrollableControl> scrollableAbove = ScrollingUtils.getScrollableWidgetsAbove(this.control);
				// ... only those that have scrolling place left
				List<ScrollableControl> scrollableWithSpaceAbove = scrollableAbove.stream()
						.filter(x -> !ScrollingUtils.isScrollEventWithoutEffectHere(x.control, x.scrollBar, e))
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

	public static List<ScrollableControl> getScrollableWidgetsAbove(Control control) {
		return getParentsOf(control).stream().map(x -> ScrollableControl.ofWidgetOptional(x)).filter(x -> x.isPresent())
				.map(x -> x.get()).collect(Collectors.toList());
	}

	/**
	 * Checks if the innerScrollableBar reached the top or bottom position.
	 * 
	 * @param control
	 * @param innerScrollableBar
	 * @param e
	 * @return True, if the scrollbar reached the top or bottom. False, if the
	 *         scrollbar is somewhere between the top and bottom.
	 */
	public static boolean isScrollEventWithoutEffectHere(Control control, ScrollBar innerScrollableBar, MouseEvent e) {

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
		return true;
	}

}
