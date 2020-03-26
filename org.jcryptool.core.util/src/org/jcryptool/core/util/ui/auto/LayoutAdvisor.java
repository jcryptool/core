package org.jcryptool.core.util.ui.auto;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class LayoutAdvisor {
	
	
	private static LayoutAdvisor defaultInstance;

	public List<Control> managed = new LinkedList<>();
	
	public static LayoutAdvisor getInstance() {
		if( LayoutAdvisor.defaultInstance == null ) {
			LayoutAdvisor.defaultInstance = new LayoutAdvisor();
			return getInstance();
		}
		return LayoutAdvisor.defaultInstance;
	}
	
	public void addManaged(Control control) {
		this.managed.add(control);
	}
	
	/**
	 * Interface method to add a composite to be managed by this instance. Without optional args (tbd.), 
	 * this method will apply JCT-wide, "conservative default" strategies that should work out-of-the-box 
	 * with any layout (tweaking only values that are deemed to be "omitted by negligence").
	 * 
	 * This method should be called before any .layout() calls are made, but all the children of the root 
	 * composite should have already been added to it.
	 * 
	 * @param c the composite
	 */
	public static void addPreLayoutRootComposite(Composite c) {
		LayoutAdvisor inst = getInstance();
		inst.addManaged(c);
		traverseMarkWidthHint_FirstStrategy(c);
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
	
	private static void traverseMarkWidthHint_FirstStrategy(Control c) {
		if (c instanceof Composite) {
			Composite composite = (Composite) c;
			
			for (Control child : composite.getChildren()) {
				traverseMarkWidthHint_FirstStrategy(child);
			}
		} else {
			boolean hasWrap = (c.getStyle() & SWT.WRAP) != 0;
// 			System.err.println(String.format("%s has wrap: %s", c, hasWrap));
			if (hasWrap || c instanceof Text || c instanceof StyledText) {
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
							// * has the SWT.WRAP style or is a Text or StyledText widget 
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
	

}
