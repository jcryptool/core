/*==========================================================================
 * 
 * EHEP.java
 * 
 * $Author: anatolibarski $
 * $Revision: 1.7 $
 * $Date: 2012/11/06 16:45:21 $
 * $Name:  $
 * 
 * Created on 26-Nov-2003
 * Created by Marcel Palko alias Randallco (randallco@users.sourceforge.net)
 *==========================================================================*/
package net.sourceforge.ehep.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

/**
 * This class defines the EHEP public constants
 * @author Marcel Palko alias Randallco
 * @author randallco@users.sourceforge.net
 */
public class EHEP {
	/**
	 * Plugin name
	 */
	public final static String PLUGIN_NAME = "ehep"; //$NON-NLS-1$

	/**
	 * Plugin ID
	 */
	public final static String PLUGIN_ID = "net.sourceforge." + PLUGIN_NAME; //$NON-NLS-1$

	/**
	 * Full plugin name
	 */
	public final static String FULL_PLUGIN_NAME = PLUGIN_ID;

	/**
	 * Number of data columns in the table
	 */
	public final static int TABLE_NUM_DATA_COLUMNS = 16;

	/**
	 * Total number of columns in the table<br/>
	 * Table has following format:<br/>
	 * <pre>
	 * Column #0 #1  #2  #3  #4  #5  #6  #7  #8  #9  #10 #11 #12 #13 #14 #15 #16       #17
	 * XXXXXXXX: XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  XX  AAAAAAAAAAAAAAAA
	 * </pre>
	 */
	public final static int TABLE_NUM_COLUMNS = TABLE_NUM_DATA_COLUMNS + 2;
	
	/**
	 * The content of empty table cell
	 */
	public final static String TABLE_EMPTY_CELL = "  "; //$NON-NLS-1$
	
	/**
	 * Default buffer size in bytes
	 */
	public final static int BUFFER_SIZE = 10000;

	public final static String DIALOG_TITLE_EXCEPTION = Messages.EHEP_3;
	public final static String DIALOG_TITLE_ERROR     = Messages.EHEP_4;
	public final static String DIALOG_TITLE_WARNING   = Messages.EHEP_5;
	
	public final static int DIALOG_TYPE_INSERT = 1;
	public final static int DIALOG_TYPE_APPEND = 2;
	
	/**
	 * Property name for font
	 */
	public final static String PROPERTY_FONT = PLUGIN_NAME + ".font"; //$NON-NLS-1$
	
	/**
	 * Property names for background and foreground colors
	 */
	public final static String PROPERTY_COLOR_BACKGROUND_TABLE        = PLUGIN_NAME + ".color.bg.table"; //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_TABLE        = PLUGIN_NAME + ".color.fg.table"; //$NON-NLS-1$

	public final static String PROPERTY_COLOR_BACKGROUND_EDITOR       = PLUGIN_NAME + ".color.bg.editor"; //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_EDITOR       = PLUGIN_NAME + ".color.fg.editor"; //$NON-NLS-1$

	public final static String PROPERTY_COLOR_BACKGROUND_CHANGED_CELL = PLUGIN_NAME + ".color.bg.changed_cell";  //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_CHANGED_CELL = PLUGIN_NAME + ".color.fg.changed_cell"; //$NON-NLS-1$

	public final static String PROPERTY_COLOR_BACKGROUND_INSERTED_CELL = PLUGIN_NAME + ".color.bg.inserted_cell";  //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_INSERTED_CELL = PLUGIN_NAME + ".color.fg.inserted_cell"; //$NON-NLS-1$

	public final static String PROPERTY_COLOR_BACKGROUND_FIND_RESULT = PLUGIN_NAME + ".color.bg.find_result";  //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_FIND_RESULT = PLUGIN_NAME + ".color.fg.find_result"; //$NON-NLS-1$

	public final static String PROPERTY_COLOR_BACKGROUND_UNDO = PLUGIN_NAME + ".color.bg.undo";  //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_UNDO = PLUGIN_NAME + ".color.fg.undo";	 //$NON-NLS-1$
	
	public final static String PROPERTY_COLOR_BACKGROUND_SELECTED = PLUGIN_NAME + ".color.bg.selected";  //$NON-NLS-1$
	public final static String PROPERTY_COLOR_FOREGROUND_SELECTED = PLUGIN_NAME + ".color.fg.selected"; //$NON-NLS-1$

	/**
	 * Default font name
	 */
	public final static String DEFAULT_FONT_NAME = "Courier New"; //$NON-NLS-1$
	
	/**
	 * Default font size
	 */
	public final static int DEFAULT_FONT_SIZE = 10;

	/**
	 * Default font style
	 */
	public final static int DEFAULT_FONT_STYLE = SWT.NORMAL;

	/**
	 * Default plugin colors
	 */
	public final static RGB COLOR_BACKGROUND_TABLE = new RGB(255, 255, 255);
	public final static RGB COLOR_FOREGROUND_TABLE = new RGB(0, 0, 0);
	
	public final static RGB COLOR_BACKGROUND_EDITOR = new RGB(255, 0, 128);
	public final static RGB COLOR_FOREGROUND_EDITOR = new RGB(255, 255, 255);
	
	public final static RGB COLOR_BACKGROUND_CHANGED_CELL = new RGB(255, 225, 240);
	public final static RGB COLOR_FOREGROUND_CHANGED_CELL = new RGB(0, 0, 0);
	
	public final static RGB COLOR_BACKGROUND_INSERTED_CELL = new RGB(184, 235, 254);
	public final static RGB COLOR_FOREGROUND_INSERTED_CELL = new RGB(0, 0, 0);
	
	public final static RGB COLOR_BACKGROUND_UNDO = new RGB(200, 255, 200);
	public final static RGB COLOR_FOREGROUND_UNDO = new RGB(0, 0, 0);	

	public final static RGB COLOR_BACKGROUND_FIND_RESULT = new RGB(192, 192, 192);
	public final static RGB COLOR_FOREGROUND_FIND_RESULT = new RGB(0, 0, 0);	

	public final static RGB COLOR_BACKGROUND_SELECTED = new RGB(220, 220, 220);
	public final static RGB COLOR_FOREGROUND_SELECTED = new RGB(0, 0, 0);	

	/**
	 * Property name for debug mode
	 */
	public final static String PROPERTY_DEBUG_MODE = PLUGIN_NAME + ".debug.mode"; //$NON-NLS-1$
	
	public final static String PROPERTY_FIND_HEX = PLUGIN_NAME + ".find.hex"; //$NON-NLS-1$
	public final static String PROPERTY_REPLACE_HEX = PLUGIN_NAME + ".replace.hex"; //$NON-NLS-1$
	public final static String PROPERTY_MATCH_CASE = PLUGIN_NAME + ".match.case"; //$NON-NLS-1$
	public final static String PROPERTY_WRAP = PLUGIN_NAME + ".wrap"; //$NON-NLS-1$

	/**
	 * Default debug mode
	 */
	public final static String DEFAULT_DEBUG_MODE = "0"; //$NON-NLS-1$
	
	/**
	 * Property name for undo/redo
	 */
	public final static String PROPERTY_MAX_UNDO_STEPS = PLUGIN_NAME + ".undo.steps"; //$NON-NLS-1$
	public final static int MAX_UNDO_STEPS = 100;
	
	/**
	 * Table cell status
	 */
	public final static int CELL_STATUS_NORMAL   = 0;
	public final static int CELL_STATUS_CHANGED = 1;
	public final static int CELL_STATUS_INSERTED = 2;
	public final static int CELL_STATUS_UNDO = 3;
	public final static int CELL_STATUS_FINDRESULT = 4;
	public final static int CELL_STATUS_SELECTED = 0x80;
}
