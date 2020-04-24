package org.jcryptool.core.util.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
		styledTextGridData.widthHint = this.getClientArea().width - 10;
		
		styledText.setLayoutData(styledTextGridData);	
		styledText.addListener(SWT.Resize, event -> {

			if (styledTextGridData.heightHint != styledText.computeSize(this.getClientArea().width - 10, SWT.DEFAULT).y) {
				styledTextGridData.heightHint = styledText.computeSize(this.getClientArea().width - 10, SWT.DEFAULT).y;
				styledText.requestLayout();
				
				
				if (sc != null) {
					ScrolledComposite scrolledComp = (ScrolledComposite) sc;
					scrolledComp.notifyListeners(SWT.Resize, new Event());
					
				}
			}
		} );
		
		// This is the menu that is shown after richt clicking on the Title and Description.
		Menu selectAndCopyMenu = new Menu(styledText);
		styledText.setMenu(selectAndCopyMenu);
		
		// This is the menu entry to copy the marked text to the clipsboard
		MenuItem copyItem = new MenuItem(selectAndCopyMenu, SWT.NONE);
		copyItem.setText(Messages.TitleAndDescriptionCopyMenuItem);
		copyItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				styledText.copy();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				styledText.copy();
			}
		});
		
		// This is the menu entry to mark the title and description
		MenuItem selectAllItem = new MenuItem(selectAndCopyMenu, SWT.NONE);
		selectAllItem.setText(Messages.TitleAndDescriptionSelectAllMenuItem);
		selectAllItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				styledText.selectAll();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				styledText.selectAll();
			}
		});	
		
		getScrolledCompositeParent(this);
		
		
		// If there is a ScrolledComposite somewhere in the parent composites
		// add a Resize Listener to it to fix the long-scrollbar-problem.
		if (sc != null) {
			ScrolledComposite scrolledComp = (ScrolledComposite) sc;
			scrolledComp.addListener(SWT.Resize, event -> {
				
	            firstChildOfsc.layout();
	            scrolledComp.setMinSize(firstChildOfsc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	        } );
			

		}
	}
	
	
	private void getScrolledCompositeParent(Composite current) {
		sc = current;
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
		

	}


}
