package org.jcryptool.core.util.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;

public class TitleAndDescriptionComposite extends Composite {
	
	
	private StyledText styledText;
	private GridData styledTextGridData;
	private String title = "";
	private String description = "";
	private Composite sc;
	private Composite firstChildOfsc;
	

	
	public TitleAndDescriptionComposite(Composite parent) {
		super(parent, SWT.NONE);
		this.setBackground(ColorService.WHITE);
		this.setLayout(new GridLayout());
		
		
		styledText = new StyledText(this, SWT.READ_ONLY | SWT.WRAP);

		styledTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		styledTextGridData.widthHint = this.getClientArea().width;
		
		styledText.setLayoutData(styledTextGridData);	
		styledText.addListener(SWT.Resize, event -> {
			System.err.println("styledText resize triggered");

			if (styledTextGridData.heightHint != styledText.computeSize(this.getClientArea().width, SWT.DEFAULT).y) {
				System.out.print("Height Hint: " + styledTextGridData.heightHint);
				System.out.println(" ComputedSize: " + styledText.computeSize(this.getClientArea().width, SWT.DEFAULT).y);
				styledTextGridData.heightHint = styledText.computeSize(this.getClientArea().width, SWT.DEFAULT).y;
				styledText.requestLayout();
				
				
				if (sc != null) {
					ScrolledComposite scrolledComp = (ScrolledComposite) sc;
					System.err.println("Trigger scrolledComp Resize Listener");
					scrolledComp.notifyListeners(SWT.Resize, new Event());
					
				}
				
			}
//			styledTextGridData.heightHint = styledText.computeSize(this.getClientArea().width, SWT.DEFAULT).y;
//			System.out.println("StyledText height hint: " + styledTextGridData.heightHint);
//			
//			System.out.println("StyledText preffered size: " + styledText.computeSize(this.getClientArea().width, SWT.DEFAULT));
			
//			styledText.requestLayout();
//			styledText.layout(true);
			
		} );
			
		
		
		getScrolledCompositeParent(this);
		System.out.println("---------------------");
		System.out.println(sc.getClass());
		System.out.println(firstChildOfsc.getClass());
		
		
		// If there is a ScrolledComposite somewhere in the parent composites
		// add a Resize Listener to it to fix the long-scrollbar-problem.
		if (sc != null) {
			ScrolledComposite scrolledComp = (ScrolledComposite) sc;
			scrolledComp.addListener(SWT.Resize, event -> {
				
//	            int height = firstChildOfsc.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + styledText.getSize().y;
//				int height = firstChildOfsc.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
//	            System.out.println("Fensterhöhe: " + height);
	            firstChildOfsc.layout();
	            firstChildOfsc.setBackground(ColorService.WHITE);
	            scrolledComp.setMinSize(firstChildOfsc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//	            firstChildOfsc.layout();
//	            System.out.println("StyledText actual height: " + styledText.getSize());
	            System.err.println("scrolledComposite Resize / MouseDoubleClick called");
	        } );
			

		}
	}
	
	
	private void getScrolledCompositeParent(Composite current) {
		sc = current;
		System.out.println(sc.getClass());
		if (current.getParent() != null) {
			if (!sc.getClass().equals(ScrolledComposite.class)) {
				firstChildOfsc = sc;
				getScrolledCompositeParent(current.getParent());
			}
		} else {
			// If no ScrolledComposite is used set sc = null.
			// In the constructor it is checked if sc not equal to null.
			sc = null;
		}
	}
	
	public void setTitle(String title) {
		this.title = title;
		styleText();
	}
	
	public void setDescription(String description) {
		this.description = description;
		styleText();
	}
	
	public void setTitleAndDescription(String title, String description) {
		this.title = title;
		this.description = description;
		styleText();
	}
	
	private void styleText() {
		// User does not set title and description
//		if (title.isEmpty() && description.isEmpty()) {
//			title = "Please set a title for this plugin via: TitleAndDescriptionComposite#setTitle(String title).";
//			description = "Please set a description for this plugin via TitleAndDescriptionComposite#setDescription(String description).";
//		} else 
//			// User only set title, no description
//			if (!title.isEmpty() && description.isEmpty()) {
//			description = "Please set a description for this plugin via TitleAndDescriptionComposite#setDescription(String description).";
//		} else 
//			// User only set description, no title
//			if (title.isEmpty() && !description.isEmpty()) {
//			title = "Please set a title for this pluginvia: TitleAndDescriptionComposite#setTitle(String title).";
//		}
		
		if (title.isEmpty() && description.isEmpty()) {
			
		} else if (!title.isEmpty() && description.isEmpty()) {
			styledText.setText(title);
			
			StyleRange sr1 = new StyleRange();
			sr1.start = 0;
			sr1.length = title.length();
			sr1.font = FontService.getHeaderFont();
			styledText.setStyleRange(sr1);
		} else if (title.isEmpty() && !description.isEmpty()) {
			styledText.setText(description);
		} else if (!title.isEmpty() && !description.isEmpty()) {
			styledText.setText(title + "\n" + description);
			
			// Vertical indent between title and description
			// FIXME Der blaue Unterstrich, nachdem man die Beschreibung markiert hat
			// hängt hiermit zusammen. Nimmt man die Zeile raus, bleibt der Unterstrich
			// nach dem markieren eines Textes nicht mehr erhalten (so sollte es sein).
			styledText.setLineVerticalIndent(1, styledText.getLineHeight(title.length() + 1) / 5);
			
			StyleRange sr1 = new StyleRange();
			sr1.start = 0;
			sr1.length = title.length();
			sr1.font = FontService.getHeaderFont();
			styledText.setStyleRange(sr1);
		}
		
//		styledTextGridData.heightHint = styledText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;

	}


}
