package edu.kit.iks.zudoku;

import java.awt.Color;

import javax.swing.JPanel;

@SuppressWarnings("serial") // Objects of this class are not meant to be serialized. 
public class Zudoku extends JPanel {
	/* 
	 * Settings
	 */
	public static boolean PROOF_MODE_ACTIVE = false; // Proof mode not implemented yet.
	public static boolean VERIFICATION_MODE_ACTIVE = true;
	
	public static int SUDOKU_SIZE = 4;
	
	public static boolean WINDOW_FULLSCREEN = false;
	
	public static final Color KIT_GREEN = new Color(0, 157, 130);
	public static final Color KRYPTOLOGIKUM_BLUE = new Color(7, 123, 205);
	public static final Color KRYPTOLOGIKUM_GREY = new Color(70, 70, 70);
	
	public static final int CARD_WIDTH = 131;
	public static final int CARD_HEIGHT = 131;
	public static final int CARD_FONT_HEIGHT = 20;
	public static final String CARD_FRONT_IMAGE = "/images/kryptologikum-front.png";
	public static final String CARD_BACK_IMAGE = "/images/kryptologikum-back.png";
	public static final Color CARD_FIXED_BORDER_COLOR = KRYPTOLOGIKUM_BLUE;

	public static int SELECT_BUTTON_WIDTH = 80;
	public static int SELECT_BUTTON_HEIGHT = 80;
	public static int SELECT_BUTTON_IMAGE_WIDTH = 80;
	public static int SELECT_BUTTON_IMAGE_HEIGHT = 80;
	public static final String SELECT_COLUMN_BUTTON_IMAGE = "/images/select-column-button-kryptologikum.png";
	public static final String SELECT_ROW_BUTTON_IMAGE = "/images/select-row-button-kryptologikum.png";
	public static final String SELECT_BLOCK_BUTTON_IMAGE = "/images/select-block-button-kryptologikum.png";
	
	public static final int STACK_WIDTH = CARD_WIDTH;
	public static final int STACK_HEIGHT = CARD_HEIGHT;
	public static final Color STACK_BACKGROUND_COLOR = KRYPTOLOGIKUM_GREY;
	
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	
	public static final int STACK_PADDING = 10;       // Padding between two stacks
	public static final int SUDOKU_TOP_PADDING = 30;  // Padding between the sudoku and adjacent elements above 
                                                      // (e.g., verification stacks)
	public static final int SUDOKU_LEFT_PADDING = -80;// Padding between the sudoku and adjacent elements on the left
                                                      // (e.g., supply stacks)
                                                      // TODO: fix this crude hack to not waste space if there are not supply stacks
	public static final int BORDER_PADDING = 10;      // Padding between panel border and contained elements
	
	public static final int NUMBER_OF_HINTS = 4; // Number of hints (automatically capped by total amount of fields)
	
	private static final int FRAMERATE = 20; // Frames per second. Default: 60. Set to lower values, e.g., 20 on slow machines. 
	public static final int FADEOUTS_FRAMERATE = FRAMERATE; 
	public static final int ANIMATIONS_FRAMERATE = FRAMERATE;
	public static final int ANIMATIONS_DURATION = 2000; // Duration in milliseconds

	public Zudoku() {
		add(new VerificationPanel(this, new SudokuField(false)));	
		setBackground(Color.BLACK);
	}
}
