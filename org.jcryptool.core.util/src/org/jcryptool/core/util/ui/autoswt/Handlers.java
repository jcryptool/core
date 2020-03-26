package org.jcryptool.core.util.ui.autoswt;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class Handlers {

	private static void traverseMarkWidthHint_FirstStrategy(Control c) {
		if (c instanceof Composite) {
			Composite composite = (Composite) c;
			
			for (Control child : composite.getChildren()) {
				traverseMarkWidthHint_FirstStrategy(child);
			}
		} else {
			boolean hasWrap = (c.getStyle() & SWT.WRAP) != 0;
// 			System.err.println(String.format("%s has wrap: %s", c, hasWrap));
			if (hasWrap) {
				Object layoutData = c.getLayoutData();
				if (layoutData instanceof GridData) {
					GridData gridData = (GridData) layoutData;
// 					System.err.println(String.format("%s has GridData: %s and widthHint: %s, at a SWT.DEFAULT of %s", c, gridData, gridData.widthHint, SWT.DEFAULT));
					if(gridData.widthHint == SWT.DEFAULT) {
						List<Composite> parents = getParents(c);
// 						System.err.println(String.format("%s has GridData: %s and parents: %s", c, gridData, parents));
						if (parents.stream().anyMatch(parent -> parent.getClass().equals(ScrolledComposite.class))) {
							// here, the layout is changed globally
							// conditions:
							// * is below a ScrolledComposite
							// * has the SWT.WRAP style
							// * has a GridLayout object 
							int computedSize = c.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
							gridData.widthHint = computedSize;
							System.err.println(String.format("corrected GridData.widthHint for element %s with computed width %s", c, computedSize));
						}
					}
				}
			}
			
		}
		
	}

	private static List<Composite> getParents(Control c) {
		Composite parent = c.getParent();

		List<Composite> result;
		if (parent != null) {
			result = getParents(parent);
			result.add(parent);
		} else {
			result = new LinkedList<>();
		}
		
		return result;
	}

	/**
	 * The default handler for adding widthHints below scrolled composites for labels that have SWT.WRAP
	 */
	public static IControlEventHandler createScrolledWrapWidthHintHandler() {
		return new IControlEventHandler() {
			@Override
			public void onIninitializationFinished(LayoutAdvisor advisor, Control c) {
				IControlEventHandler.super.onIninitializationFinished(advisor, c);
				traverseMarkWidthHint_FirstStrategy(c);
			}
		};
	}
	
}
