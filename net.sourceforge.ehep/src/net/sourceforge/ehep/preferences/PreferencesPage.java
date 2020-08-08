/*==========================================================================
 * 
 * PreferencesPage.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.10 $
 * $Date: 2012/11/06 16:45:20 $
 * 
 * Created on 5-Dec-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import net.sourceforge.ehep.EhepPlugin;
import net.sourceforge.ehep.core.EHEP;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>PreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

/**
 * @author Marcel Palko
 * @author randallco@users.sourceforge.net
 */
public class PreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {
	static FontData[] fontDefault = new FontData[] {new FontData(EHEP.DEFAULT_FONT_NAME, EHEP.DEFAULT_FONT_SIZE, EHEP.DEFAULT_FONT_STYLE)};
	FontData[] fontData;
	
	IWorkbench workbench;
	FontDialog fontDialog;
	Label      fontDisplay;
	
	ColorFieldEditor colorBgEditor;
	ColorFieldEditor colorFgEditor;
	
	ColorFieldEditor colorBgTable;
	ColorFieldEditor colorFgTable;
	
	ColorFieldEditor colorBgChangedRow;
	ColorFieldEditor colorFgChangedRow;
	
	ColorFieldEditor colorBgInsertedRow;
	ColorFieldEditor colorFgInsertedRow;

	ColorFieldEditor colorBgUndoedRow;
	ColorFieldEditor colorFgUndoedRow;	
	
	ColorFieldEditor colorBgFindResultRow;
	ColorFieldEditor colorFgFindResultRow;

	ColorFieldEditor colorBgSelectedRow;
	ColorFieldEditor colorFgSelectedRow;

	IntegerFieldEditor maxUndoSteps;

	Button debugModeButton;
	
	/**
	 * Constructor
	 */
	public PreferencesPage() {
		super();
		setPreferenceStore(EhepPlugin.getDefault().getPreferenceStore());
		setDescription(null);
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	private void initializeDefaults() {
		IPreferenceStore store = getPreferenceStore();
		//
		// Set default font
		//
		initializeFontDefaults(store);
		//
		// Set default plugin colors
		//
		initializeColorDefaults(store);
		//
		// Set default undo step limits
		//
		store.setDefault(EHEP.PROPERTY_MAX_UNDO_STEPS, EHEP.MAX_UNDO_STEPS);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		this.workbench = workbench;
		setPreferenceStore(EhepPlugin.getDefault().getPreferenceStore());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		//
		// Get default or previously selected font
		//
		fontData = PreferenceConverter.getFontDataArray(getPreferenceStore(), EHEP.PROPERTY_FONT);

		//
		// Create font dialog
		//
		fontDialog = new FontDialog(workbench.getActiveWorkbenchWindow().getShell());

		//
		// Create main panel
		//
		Composite mainPanel = new Composite(parent, SWT.NULL);
		mainPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout innerLayout = new GridLayout();
		innerLayout.numColumns = 1;
		innerLayout.marginHeight = 0;
		innerLayout.marginWidth = 0;
		mainPanel.setLayout(innerLayout);
		
		//
		// Create font panel
		//
		createFontPanel(mainPanel);
		
		//
		// Create color panel
		//
		createColorPanel(mainPanel);

		//
		// Create Plugin Association panel
		//
		createPluginAssociationPanel(mainPanel);

		//
        // Create Misc. panel for Debug & Undo/Redo subpanels
        //
        Composite miscPanel = new Composite(mainPanel, SWT.NULL);
        miscPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout miscLayout = new GridLayout();
        miscLayout.numColumns = 2;
        miscLayout.marginHeight = 0;
        miscLayout.marginWidth = 0;
        miscPanel.setLayout(miscLayout);

        //
		// Create Debug panel
		//
		createDebugPanel(miscPanel);

		//
		// Create Undo panel
		//
		createUndoPanel(miscPanel);
		
		return mainPanel;
	}

	/**
	 * Updates label with selected font
	 */
	private void updateFontDisplay() {
		if (fontData == null) {
			fontDisplay.setText(Messages.PreferencesPage_0);
		} // if
		else {
			String style = Messages.PreferencesPage_1;
			if (fontData[0].getStyle() == SWT.BOLD) {
				style = Messages.PreferencesPage_2;
			} else if (fontData[0].getStyle() == SWT.ITALIC) {
				style = Messages.PreferencesPage_3;
			} else if (fontData[0].getStyle() == (SWT.BOLD | SWT.ITALIC)) {
				style = Messages.PreferencesPage_4;
			}
			fontDisplay.setText(fontData[0].getName() + '-' + style + '-' + fontData[0].getHeight());
		} // else
	}

	/** 
	 * Save the preferences to the preference store.
	 */
	public boolean performOk() {
		PreferenceConverter.setValue(getPreferenceStore(), EHEP.PROPERTY_FONT, fontData);
		colorBgEditor.store();
		colorFgEditor.store();
		colorBgTable.store();
		colorFgTable.store();
		colorBgChangedRow.store();
		colorFgChangedRow.store();
		colorBgInsertedRow.store();
		colorFgInsertedRow.store();
		colorBgUndoedRow.store();
		colorFgUndoedRow.store();
		colorBgFindResultRow.store();
		colorFgFindResultRow.store();
		colorBgSelectedRow.store();
		colorFgSelectedRow.store();
		maxUndoSteps.store();
		getPreferenceStore().setValue(EHEP.PROPERTY_DEBUG_MODE, debugModeButton.getSelection());
		return super.performOk();
	}

	/**
	 * Sets default preferences
	 */
	protected void performDefaults() {
		fontData = fontDefault;
		updateFontDisplay();
		colorBgEditor.loadDefault();
		colorFgEditor.loadDefault();
		colorBgTable.loadDefault();
		colorFgTable.loadDefault();
		colorBgChangedRow.loadDefault();
		colorFgChangedRow.loadDefault();
		colorBgInsertedRow.loadDefault();
		colorFgInsertedRow.loadDefault();
		colorBgUndoedRow.loadDefault();
		colorFgUndoedRow.loadDefault();
		colorBgFindResultRow.loadDefault();
		colorFgFindResultRow.loadDefault();
		colorBgSelectedRow.loadDefault();
		colorFgSelectedRow.loadDefault();
		debugModeButton.setSelection(getPreferenceStore().getDefaultBoolean(EHEP.PROPERTY_DEBUG_MODE));
		maxUndoSteps.loadDefault();
	}

	/**
	 * Sets default font
	 * @param store
	 */
	static public void initializeFontDefaults(IPreferenceStore store) {
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_FONT, fontDefault);
	}
	
	/**
	 * Sets default colors
	 * @param store
	 */
	static public void initializeColorDefaults(IPreferenceStore store) {
		//
		// Set default background colors
		//
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_TABLE, EHEP.COLOR_BACKGROUND_TABLE);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_EDITOR, EHEP.COLOR_BACKGROUND_EDITOR);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_CHANGED_CELL, EHEP.COLOR_BACKGROUND_CHANGED_CELL);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_INSERTED_CELL, EHEP.COLOR_BACKGROUND_INSERTED_CELL);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_UNDO, EHEP.COLOR_BACKGROUND_UNDO);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_FIND_RESULT, EHEP.COLOR_BACKGROUND_FIND_RESULT);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_BACKGROUND_SELECTED, EHEP.COLOR_BACKGROUND_SELECTED);
		//
		// Set default foreground colors
		//
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_TABLE, EHEP.COLOR_FOREGROUND_TABLE);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_EDITOR, EHEP.COLOR_FOREGROUND_EDITOR);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_CHANGED_CELL, EHEP.COLOR_FOREGROUND_CHANGED_CELL);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_INSERTED_CELL, EHEP.COLOR_FOREGROUND_INSERTED_CELL);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_UNDO, EHEP.COLOR_FOREGROUND_UNDO);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_FIND_RESULT, EHEP.COLOR_FOREGROUND_FIND_RESULT);
		PreferenceConverter.setDefault(store, EHEP.PROPERTY_COLOR_FOREGROUND_SELECTED, EHEP.COLOR_FOREGROUND_SELECTED);
	}

	private void createFontPanel(Composite mainPanel) {
		//
		// Create font panel
		//
		Composite fontPanel = new Composite(mainPanel, SWT.NULL);
		fontPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout fontLayout = new GridLayout();
		fontLayout.marginHeight = 0;
		fontLayout.marginWidth = 0;
		fontLayout.numColumns = 1;
		fontPanel.setLayout(fontLayout);
		Group fontSettingsGroup = new Group(fontPanel, SWT.NONE);
		fontSettingsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fontSettingsGroup.setText(Messages.PreferencesPage_5);
		GridLayout fontGroupLayout = new GridLayout();
		fontGroupLayout.numColumns = 3;
		fontSettingsGroup.setLayout(fontGroupLayout);

		//
		// Font label
		//
		fontDisplay = new Label(fontSettingsGroup, SWT.NULL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		fontDisplay.setLayoutData(data);
		//updateFontDisplay();

		//
		// "Choose font..." button
		//
		Button fontButton = new Button(fontSettingsGroup, SWT.PUSH);
		fontButton.setText(Messages.PreferencesPage_6);
		fontButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				//
				// Nothing here...
				//
			}
			public void widgetSelected(SelectionEvent e) {
				if (fontData != null) {
					fontDialog.setFontList(fontData);
				}
				FontData data = fontDialog.open();
				if (data != null) {
					//
					// Remember selected font
					//
					fontData = new FontData[1];
					fontData[0] = data;
					updateFontDisplay();
				} // if
			} // widgetSelected()
		} // SelectionListener()
		);

		//
		// "Default font" button
		//
		Button defaultButton = new Button(fontSettingsGroup, SWT.PUSH);
		defaultButton.setText(Messages.PreferencesPage_7);
		defaultButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				fontData = fontDefault;
				updateFontDisplay();
			}
		});

		//
		// Update font settings display
		//
		updateFontDisplay();
	}

	private void createColorPanel(Composite mainPanel) {
		//
		// Create color panel
		//
		Composite colorPanel = new Composite(mainPanel, SWT.NULL);
		colorPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout colorLayout = new GridLayout();
		colorLayout.marginHeight = 0;
		colorLayout.marginWidth = 0;
		colorLayout.numColumns = 1;
		colorPanel.setLayout(colorLayout);
		
		Group colorSettingsGroup = new Group(colorPanel, SWT.NONE);
		colorSettingsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		colorSettingsGroup.setText(Messages.PreferencesPage_8);
		colorSettingsGroup.setLayout(new GridLayout(2, true));

		//
		// left part
		//
		Composite left = new Composite(colorSettingsGroup, SWT.NONE);
		left.setLayout(new GridLayout(3, false));
		left.setLayoutData(new GridData(GridData.FILL_BOTH));

		createColorHeader(left);
		
		//
		// Table colors
		//
		Label l1 = new Label(left, SWT.NULL);
		l1.setText(Messages.PreferencesPage_9);
		GridData l1Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l1Grid.horizontalAlignment = GridData.END;
		l1.setLayoutData(l1Grid);
		
		Composite colorBgTablePanel = new Composite(left, SWT.NULL);
		GridLayout colorBgTableLayout = new GridLayout();
		colorBgTableLayout.marginHeight = 0;
		colorBgTableLayout.marginWidth = 0;
		colorBgTableLayout.numColumns = 1;
		colorBgTablePanel.setLayout(colorBgTableLayout);
		colorBgTable = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_TABLE, "", colorBgTablePanel); //$NON-NLS-1$
		colorBgTable.setPage(this); // Since Eclipse 3.1+
		colorBgTable.setPreferenceStore(getPreferenceStore());
		colorBgTable.load();

		Composite colorFgTablePanel = new Composite(left, SWT.NULL);
		GridLayout colorFgTableLayout = new GridLayout();
		colorFgTableLayout.marginHeight = 0;
		colorFgTableLayout.marginWidth = 0;
		colorFgTableLayout.numColumns = 1;
		colorFgTablePanel.setLayout(colorFgTableLayout);
		colorFgTable = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_TABLE, "", colorFgTablePanel); //$NON-NLS-1$
		colorFgTable.setPage(this); // Since Eclipse 3.1+
		colorFgTable.setPreferenceStore(getPreferenceStore());
		colorFgTable.load();

		//
		// Cell editor colors
		//
		Label l2 = new Label(left, SWT.NULL);
		l2.setText(Messages.PreferencesPage_12);
		GridData l2Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l2Grid.horizontalAlignment = GridData.END;
		l2.setLayoutData(l2Grid);
		
		Composite colorBgCellEditorPanel = new Composite(left, SWT.NULL);
		GridLayout colorBgCellEditorLayout = new GridLayout();
		colorBgCellEditorLayout.marginHeight = 0;
		colorBgCellEditorLayout.marginWidth = 0;
		colorBgCellEditorLayout.numColumns = 1;
		colorBgCellEditorPanel.setLayout(colorBgCellEditorLayout);
		colorBgEditor = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_EDITOR, "", colorBgCellEditorPanel); //$NON-NLS-1$
		colorBgEditor.setPage(this); // Since Eclipse 3.1+
		colorBgEditor.setPreferenceStore(getPreferenceStore());
		colorBgEditor.load();

		Composite colorFgCellEditorPanel = new Composite(left, SWT.NULL);
		GridLayout colorFgCellEditorLayout = new GridLayout();
		colorFgCellEditorLayout.marginHeight = 0;
		colorFgCellEditorLayout.marginWidth = 0;
		colorFgCellEditorLayout.numColumns = 1;
		colorFgCellEditorPanel.setLayout(colorFgCellEditorLayout);
		colorFgEditor = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_EDITOR, "", colorFgCellEditorPanel); //$NON-NLS-1$
		colorFgEditor.setPage(this); // Since Eclipse 3.1+
		colorFgEditor.setPreferenceStore(getPreferenceStore());
		colorFgEditor.load();
		
		//
		// Changed cell colors
		//
		Label l3 = new Label(left, SWT.NULL);
		l3.setText(Messages.PreferencesPage_15);
		GridData l3Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l3Grid.horizontalAlignment = GridData.END;
		l3.setLayoutData(l3Grid);
		
		Composite colorBgChangedCellPanel = new Composite(left, SWT.NULL);
		GridLayout colorBgChangedCellLayout = new GridLayout();
		colorBgChangedCellLayout.marginHeight = 0;
		colorBgChangedCellLayout.marginWidth = 0;
		colorBgChangedCellLayout.numColumns = 1;
		colorBgChangedCellPanel.setLayout(colorBgChangedCellLayout);
		colorBgChangedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_CHANGED_CELL, "", colorBgChangedCellPanel); //$NON-NLS-1$
		colorBgChangedRow.setPage(this); // Since Eclipse 3.1+
		colorBgChangedRow.setPreferenceStore(getPreferenceStore());
		colorBgChangedRow.load();

		Composite colorFgChangedCellPanel = new Composite(left, SWT.NULL);
		GridLayout colorFgChangedCellLayout = new GridLayout();
		colorFgChangedCellLayout.marginHeight = 0;
		colorFgChangedCellLayout.marginWidth = 0;
		colorFgChangedCellLayout.numColumns = 1;
		colorFgChangedCellPanel.setLayout(colorFgChangedCellLayout);
		colorFgChangedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_CHANGED_CELL, "", colorFgChangedCellPanel); //$NON-NLS-1$
		colorFgChangedRow.setPage(this); // Since Eclipse 3.1+
		colorFgChangedRow.setPreferenceStore(getPreferenceStore());
		colorFgChangedRow.load();

		//
		// Inserted cell colors
		//
		Label l4 = new Label(left, SWT.NULL);
		l4.setText(Messages.PreferencesPage_18);
		GridData l4Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l4Grid.horizontalAlignment = GridData.END;
		l4.setLayoutData(l4Grid);

		Composite colorBgInsertedCellPanel = new Composite(left, SWT.NULL);
		GridLayout colorBgInsertedCellLayout = new GridLayout();
		colorBgInsertedCellLayout.marginHeight = 0;
		colorBgInsertedCellLayout.marginWidth = 0;
		colorBgInsertedCellLayout.numColumns = 1;
		colorBgInsertedCellPanel.setLayout(colorBgInsertedCellLayout);
		colorBgInsertedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_INSERTED_CELL, "", colorBgInsertedCellPanel); //$NON-NLS-1$
		colorBgInsertedRow.setPage(this); // Since Eclipse 3.1+
		colorBgInsertedRow.setPreferenceStore(getPreferenceStore());
		colorBgInsertedRow.load();

		Composite colorFgInsertedCellPanel = new Composite(left, SWT.NULL);
		GridLayout colorFgInsertedCellLayout = new GridLayout();
		colorFgInsertedCellLayout.marginHeight = 0;
		colorFgInsertedCellLayout.marginWidth = 0;
		colorFgInsertedCellLayout.numColumns = 1;
		colorFgInsertedCellPanel.setLayout(colorFgInsertedCellLayout);
		colorFgInsertedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_INSERTED_CELL, "", colorFgInsertedCellPanel); //$NON-NLS-1$
		colorFgInsertedRow.setPage(this); // Since Eclipse 3.1+
		colorFgInsertedRow.setPreferenceStore(getPreferenceStore());
		colorFgInsertedRow.load();

		//
		// right part
		//
		Composite right = new Composite(colorSettingsGroup, SWT.NONE);
		right.setLayout(new GridLayout(3, false));
		right.setLayoutData(new GridData(GridData.FILL_BOTH));

		createColorHeader(right);

		//
		// Undoed cell colors
		//
		Label l5 = new Label(right, SWT.NULL);
		l5.setText(Messages.PreferencesPage_21);
		GridData l5Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l5Grid.horizontalAlignment = GridData.END;
		l5.setLayoutData(l5Grid);

		Composite colorBgUndoPanel = new Composite(right, SWT.NULL);
		GridLayout colorBgUndoLayout = new GridLayout();
		colorBgUndoLayout.marginHeight = 0;
		colorBgUndoLayout.marginWidth = 0;
		colorBgUndoLayout.numColumns = 1;
		colorBgUndoPanel.setLayout(colorBgUndoLayout);
		colorBgUndoedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_UNDO, "", colorBgUndoPanel); //$NON-NLS-1$
		colorBgUndoedRow.setPage(this); // Since Eclipse 3.1+
		colorBgUndoedRow.setPreferenceStore(getPreferenceStore());
		colorBgUndoedRow.load();

		Composite colorFgUndoPanel = new Composite(right, SWT.NULL);
		GridLayout colorFgUndoLayout = new GridLayout();
		colorFgUndoLayout.marginHeight = 0;
		colorFgUndoLayout.marginWidth = 0;
		colorFgUndoLayout.numColumns = 1;
		colorFgUndoPanel.setLayout(colorFgUndoLayout);
		colorFgUndoedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_UNDO, "", colorFgUndoPanel); //$NON-NLS-1$
		colorFgUndoedRow.setPage(this); // Since Eclipse 3.1+
		colorFgUndoedRow.setPreferenceStore(getPreferenceStore());
		colorFgUndoedRow.load();	

		//
		// Find result
		//
		Label l6 = new Label(right, SWT.NULL);
		l6.setText(Messages.PreferencesPage_24);
		GridData l6Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l6Grid.horizontalAlignment = GridData.END;
		l6.setLayoutData(l6Grid);

		Composite colorBgFindResultPanel = new Composite(right, SWT.NULL);
		GridLayout colorBgFindResultLayout = new GridLayout();
		colorBgFindResultLayout.marginHeight = 0;
		colorBgFindResultLayout.marginWidth = 0;
		colorBgFindResultLayout.numColumns = 1;
		colorBgFindResultPanel.setLayout(colorBgFindResultLayout);
		colorBgFindResultRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_FIND_RESULT, "", colorBgFindResultPanel); //$NON-NLS-1$
		colorBgFindResultRow.setPage(this); // Since Eclipse 3.1+
		colorBgFindResultRow.setPreferenceStore(getPreferenceStore());
		colorBgFindResultRow.load();

		Composite colorFgFindResultPanel = new Composite(right, SWT.NULL);
		GridLayout colorFgFindResultLayout = new GridLayout();
		colorFgFindResultLayout.marginHeight = 0;
		colorFgFindResultLayout.marginWidth = 0;
		colorFgFindResultLayout.numColumns = 1;
		colorFgFindResultPanel.setLayout(colorFgFindResultLayout);
		colorFgFindResultRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_FIND_RESULT, "", colorFgFindResultPanel); //$NON-NLS-1$
		colorFgFindResultRow.setPage(this); // Since Eclipse 3.1+
		colorFgFindResultRow.setPreferenceStore(getPreferenceStore());
		colorFgFindResultRow.load();
		
		//
		// Selected
		//
		Label l7 = new Label(right, SWT.NULL);
		l7.setText(Messages.PreferencesPage_27);
		GridData l7Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		l7Grid.horizontalAlignment = GridData.END;
		l7.setLayoutData(l7Grid);

		Composite colorBgSelectedPanel = new Composite(right, SWT.NULL);
		GridLayout colorBgSelectedLayout = new GridLayout();
		colorBgSelectedLayout.marginHeight = 0;
		colorBgSelectedLayout.marginWidth = 0;
		colorBgSelectedLayout.numColumns = 1;
		colorBgSelectedPanel.setLayout(colorBgSelectedLayout);
		colorBgSelectedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_BACKGROUND_SELECTED, "", colorBgSelectedPanel); //$NON-NLS-1$
		colorBgSelectedRow.setPage(this); // Since Eclipse 3.1+
		colorBgSelectedRow.setPreferenceStore(getPreferenceStore());
		colorBgSelectedRow.load();

		Composite colorFgSelectedPanel = new Composite(right, SWT.NULL);
		GridLayout colorFgSelectedLayout = new GridLayout();
		colorFgSelectedLayout.marginHeight = 0;
		colorFgSelectedLayout.marginWidth = 0;
		colorFgSelectedLayout.numColumns = 1;
		colorFgSelectedPanel.setLayout(colorFgSelectedLayout);
		colorFgSelectedRow = new ColorFieldEditor(EHEP.PROPERTY_COLOR_FOREGROUND_SELECTED, "", colorFgSelectedPanel); //$NON-NLS-1$
		colorFgSelectedRow.setPage(this); // Since Eclipse 3.1+
		colorFgSelectedRow.setPreferenceStore(getPreferenceStore());
		colorFgSelectedRow.load();
	}

	private void createColorHeader(Composite colorInnerPanel) {
		//
		// Header
		//
		Label h1 = new Label(colorInnerPanel, SWT.NULL);
		h1.setText(""); //$NON-NLS-1$
		
		Label h2 = new Label(colorInnerPanel, SWT.NULL);
		h2.setText(Messages.PreferencesPage_31);
		GridData h2Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		h2Grid.horizontalAlignment = GridData.CENTER;
		h2.setLayoutData(h2Grid);

		Label h3 = new Label(colorInnerPanel, SWT.NULL);
		h3.setText(Messages.PreferencesPage_32);
		GridData h3Grid = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		h3Grid.horizontalAlignment = GridData.CENTER;
		h3.setLayoutData(h3Grid);
	}

	private void createPluginAssociationPanel(Composite mainPanel) {
		Composite associationPanel = new Composite(mainPanel, SWT.NULL);
		associationPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout associationLayout = new GridLayout();
		associationLayout.marginHeight = 0;
		associationLayout.marginWidth = 0;
		associationLayout.numColumns = 1;
		associationPanel.setLayout(associationLayout);
		Group associationSettingsGroup = new Group(associationPanel, SWT.NONE);
		associationSettingsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		associationSettingsGroup.setText(Messages.PreferencesPage_33);
		GridLayout associationLabelGroupLayout = new GridLayout();
		associationLabelGroupLayout.numColumns = 2;
		associationSettingsGroup.setLayout(associationLabelGroupLayout);

		//
		// Label
		//
		Label associationLabel = new Label(associationSettingsGroup, SWT.NULL);
		associationLabel.setText(Messages.PreferencesPage_34);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		associationLabel.setLayoutData(data);

		//
		// "Apply..." button
		//
		Button associationButton = new Button(associationSettingsGroup, SWT.PUSH);
		associationButton.setText(Messages.PreferencesPage_35);
		associationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				Utils.registerPluginForAllExtensions();
			}
		});

		Label disAssociationLabel = new Label(associationSettingsGroup, SWT.NULL);
		disAssociationLabel.setText(Messages.PreferencesPage_36);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.grabExcessHorizontalSpace = true;
		disAssociationLabel.setLayoutData(data);

		Button disAssociationButton = new Button(associationSettingsGroup, SWT.PUSH);
		disAssociationButton.setText(Messages.PreferencesPage_37);
		disAssociationButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				Utils.removePluginFromAllExtensions();
			}
		});
	}

	private void createDebugPanel(Composite mainPanel) {
		Composite debugPanel = new Composite(mainPanel, SWT.NULL);
		debugPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout debugLayout = new GridLayout();
		debugLayout.marginHeight = 0;
		debugLayout.marginWidth = 0;
		debugLayout.numColumns = 1;
		debugPanel.setLayout(debugLayout);
		Group debugSettingsGroup = new Group(debugPanel, SWT.NONE);
		debugSettingsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		debugSettingsGroup.setText(Messages.PreferencesPage_38);
		GridLayout debugLabelGroupLayout = new GridLayout();
		debugLabelGroupLayout.numColumns = 1;
		debugSettingsGroup.setLayout(debugLabelGroupLayout);

		//
		// "Debug mode" check box
		//
		debugModeButton = new Button(debugSettingsGroup, SWT.CHECK);
		debugModeButton.setText(Messages.PreferencesPage_39);
		debugModeButton.setToolTipText(Messages.PreferencesPage_40);
		debugModeButton.setSelection(getPreferenceStore().getBoolean(EHEP.PROPERTY_DEBUG_MODE));
	}
	
	private void createUndoPanel(Composite mainPanel) {
		Composite undoPanel = new Composite(mainPanel, SWT.NULL);
		undoPanel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout undoLayout = new GridLayout();
		undoLayout.marginHeight = 0;
		undoLayout.marginWidth = 0;
		undoLayout.numColumns = 1;
		undoPanel.setLayout(undoLayout);
		Group undoSettingsGroup = new Group(undoPanel, SWT.NONE);
		undoSettingsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		undoSettingsGroup.setText(Messages.PreferencesPage_41);
		GridLayout undoLabelGroupLayout = new GridLayout();
		undoLabelGroupLayout.numColumns = 1;
        undoLabelGroupLayout.marginHeight = 0;
        undoLabelGroupLayout.marginWidth = 9;
		undoSettingsGroup.setLayout(undoLabelGroupLayout);

		//
		// "Undo" text field
		//
		maxUndoSteps = new IntegerFieldEditor(EHEP.PROPERTY_MAX_UNDO_STEPS, Messages.PreferencesPage_42, undoSettingsGroup, 4);
		maxUndoSteps.setPage(this); // Since Eclipse 3.1+
		maxUndoSteps.setPreferenceStore(getPreferenceStore());
		maxUndoSteps.load();
	}
}
