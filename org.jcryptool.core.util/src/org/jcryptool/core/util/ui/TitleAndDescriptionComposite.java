package org.jcryptool.core.util.ui;

import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.jcryptool.core.util.colors.ColorService;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.core.util.images.ImageService;

public class TitleAndDescriptionComposite extends Composite {
	
	private StyledText styledText;
	private GridData styledTextGridData;
	private String title = "";
	private String description = "";
	private Composite sc;
	private Composite firstChildOfsc;
	
	
	/**
	 * If this option is present, display a help button that, on click, consumes this widget and is intended to show help. The class VisualPluginHelp contains some useful consumers for visual plugins.
	 */
	private Optional<Consumer<TitleAndDescriptionComposite>> helpAction;
	
	public TitleAndDescriptionComposite(Composite parent) {
		this(parent, VisualPluginHelp.makeDefaultTADHelpAction(parent));
	}

	public TitleAndDescriptionComposite(Composite parent, Optional<Consumer<TitleAndDescriptionComposite>> helpAction) {
		super(parent, SWT.NONE);
		this.setBackground(ColorService.WHITE);
		this.helpAction = helpAction;
		GridLayout thisLayout = new GridLayout(2, false);
		thisLayout.horizontalSpacing = 0;
		thisLayout.verticalSpacing = 0;
		this.setLayout(thisLayout);

		Composite tadArea = new Composite(this, SWT.NONE);
		tadArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		tadArea.setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));

		Composite helpArea = new Composite(this, SWT.NONE);
		helpArea.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		GridLayout hLayout = new GridLayout(2, false);
		hLayout.marginWidth = 0;
		hLayout.marginHeight = 0;
		helpArea.setBackground(ColorService.WHITE);
		helpArea.setLayout(hLayout);

		makeTitleAndDescriptionWidgets(tadArea);

		if (this.helpAction.isPresent()) {
			Label helpButton = new Label(helpArea, SWT.NONE);
			helpButton.setBackground(ColorService.WHITE);
			helpButton.setCursor(new Cursor(getDisplay(), SWT.CURSOR_HAND));
			helpButton.setImage(ImageService.getImage("org.jcryptool.core.util", "icons/tadHelpButton_72.png"));
			GridData helpBtnLayoutData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
			helpBtnLayoutData.widthHint = 72;
			helpBtnLayoutData.heightHint = 72;
			helpButton.setLayoutData(helpBtnLayoutData);
			helpButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					TitleAndDescriptionComposite.this.helpAction.get().accept(TitleAndDescriptionComposite.this);
				}
			});
		}
		
		if (this.helpAction.isPresent()) {
			Consumer<TitleAndDescriptionComposite> helpConsumer = this.helpAction.get();
// 			helpConsumer.accept(this);
		}
		
	}
	
	private void makeTitleAndDescriptionWidgets(Composite parent) {
		GridLayout pLayout = new GridLayout();
		pLayout.marginWidth = 0;
		pLayout.marginHeight = 0;
		parent.setLayout(pLayout);
		
		styledText = new StyledText(parent, SWT.READ_ONLY | SWT.WRAP);

		styledTextGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		styledTextGridData.widthHint = parent.getClientArea().width - 10;
		
		styledText.setLayoutData(styledTextGridData);	
		styledText.addListener(SWT.Resize, event -> {

			if (styledTextGridData.heightHint != styledText.computeSize(parent.getClientArea().width - 10, SWT.DEFAULT).y) {
				styledTextGridData.heightHint = styledText.computeSize(parent.getClientArea().width - 10, SWT.DEFAULT).y;
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
		
		getScrolledCompositeParent(parent);
		
		
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
