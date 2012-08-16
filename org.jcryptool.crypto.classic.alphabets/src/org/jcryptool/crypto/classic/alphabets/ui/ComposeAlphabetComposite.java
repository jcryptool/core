package org.jcryptool.crypto.classic.alphabets.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import org.eclipse.equinox.internal.app.MainApplicationLauncher;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;
import org.jcryptool.crypto.classic.alphabets.composite.AtomAlphabet;
import org.jcryptool.crypto.classic.alphabets.composite.CompositeAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.BlockAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.ExcludeCharBlock;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.HasOriginalBlockAlpha;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.RangeBlockAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.RevertBlock;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.paramselectionui.LeaveOutCharSelectorWizard;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ComposeAlphabetComposite extends org.eclipse.swt.widgets.Composite {
	private Composite composite_1;
	private Composite composite_2;
	private Link linkViewHint;
	private Composite compMain;
	private Label lblKlickenSieDie;
	private Group grpBausteine;
	private Composite compBlocks;
	private Label label;
	private Label label_1;
	private Composite comp_block_controls;
	private Button btnVorhandeneAlphabeteAls;
	private Button btnNeuerBaustein;
	private Composite spacer_footer;
	private Group compResult;
	private Label lblAlphabetinhaltergebnis;
	private Composite composite;
	private Label lblBuchstaben_1;
	private Label lblLnge;
	private Text text_ResultCharacters;
	private Label lbl_ResultLength;
	private Label label_3;
	private Composite comp_blocks_hull;
	private Label lblA;

	private List<Button> blockButtonsAvailable = new LinkedList<Button>();
	private List<Button> blockButtonsSelected = new LinkedList<Button>();

	private List<BlockAlphabet> blockAlphabetsAvailable = new LinkedList<BlockAlphabet>();
	/**
	 * key is the original alphabet, value is the post-selection transformed alphabet.
	 */
	private Map<BlockAlphabet, BlockAlphabet> selectedBlockAlphabets = new IdentityHashMap<BlockAlphabet, BlockAlphabet>();
	/**
	 * maintains the correct order of the selected alphabets
	 */
	private List<BlockAlphabet> selectedBlockAlphabetsInOrder = new LinkedList<BlockAlphabet>();


	private AbstractUIInput<Boolean> showExistingAlphasAsBlocks;
	private GridData gd_compBlocks;
	private Composite grpBlockResult;
	private Label lblHierErscheinenDie;
	private Label lblAusgewhlteBlcke;
	private AbstractAlphabet result = new AtomAlphabet("");
	
	public Composite layoutRoot = null;
	private AbstractUIInput<AbstractAlphabet> alphabetInput;
	private Label lblrightclickOnThe;

	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/
	protected void checkSubclass() {
	}

	/**
	* Auto-generated method to display this
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		ComposeAlphabetComposite inst = new ComposeAlphabetComposite(shell);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public ComposeAlphabetComposite(org.eclipse.swt.widgets.Composite parent) {
		super(parent, SWT.NONE);
		initGUI();
	}

	private void initGUI() {
		try {
			layoutRoot = this;
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			this.setLayout(thisLayout);

			compMain = new Composite(this, SWT.NONE);
			compMain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout mainLayout = new GridLayout(1, false);
			mainLayout.marginHeight = 0;
			mainLayout.marginWidth = 0;
			compMain.setLayout(mainLayout);

			grpBausteine = new Group(compMain, SWT.NONE);
			grpBausteine.setText("Click on the alphabet blocks to compose a new alphabet:");
			grpBausteine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout gl_grpBausteine = new GridLayout(1, false);
			gl_grpBausteine.marginHeight = 0;
			gl_grpBausteine.marginWidth = 0;
			grpBausteine.setLayout(gl_grpBausteine);

//			lblKlickenSieDie = new Label(grpBausteine, SWT.WRAP);
//			lblKlickenSieDie.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
//			lblKlickenSieDie.setText("Klicken Sie die verschiedenen Bausteine an, um ein Alphabet zusammenzusetzen:");

			comp_blocks_hull = new Composite(grpBausteine, SWT.NONE);
			comp_blocks_hull.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			comp_blocks_hull.setLayout(new GridLayout(1, false));

			compBlocks = new Composite(comp_blocks_hull, SWT.NONE);
			RowLayout rl_compBlocks = new RowLayout(SWT.HORIZONTAL);
			rl_compBlocks.marginTop = 0;
			rl_compBlocks.marginBottom = 0;
			rl_compBlocks.justify = true;
			rl_compBlocks.center = true;
			rl_compBlocks.fill = true;
			rl_compBlocks.spacing = 0;
			rl_compBlocks.marginRight = 0;
			rl_compBlocks.marginLeft = 0;
			compBlocks.setLayout(rl_compBlocks);
			gd_compBlocks = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
//			gd_compBlocks.minimumWidth = 1;
//			gd_compBlocks.widthHint = 1;
			compBlocks.setLayoutData(gd_compBlocks);

			lblA = new Label(comp_blocks_hull, SWT.SEPARATOR | SWT.HORIZONTAL);
			GridData gd_lblA = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_lblA.verticalIndent = 10;
			lblA.setLayoutData(gd_lblA);

//			label = new Label(grpBausteine, SWT.SEPARATOR | SWT.HORIZONTAL);
//			label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			comp_block_controls = new Composite(comp_blocks_hull, SWT.NONE);
			comp_block_controls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout gl_comp_block_controls = new GridLayout(2, false);
			gl_comp_block_controls.marginWidth = 0;
			gl_comp_block_controls.marginHeight = 0;
			comp_block_controls.setLayout(gl_comp_block_controls);

			btnVorhandeneAlphabeteAls = new Button(comp_block_controls, SWT.CHECK);
			btnVorhandeneAlphabeteAls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			btnVorhandeneAlphabeteAls.setText("Show existing alphabets as blocks");
			Image showExistingImg = AlphabetsPlugin.getImageDescriptor("img/search.gif").createImage();
			btnVorhandeneAlphabeteAls.setImage(showExistingImg);
			showExistingAlphasAsBlocks = new ButtonInput() {
				@Override
				protected InputVerificationResult verifyUserChange() {
					return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
				}
				@Override
				public String getName() {
					return "Zeige existierende Alphabete";
				}
				@Override
				protected Boolean getDefaultContent() {
					return false;
				}
				@Override
				public Button getButton() {
					return btnVorhandeneAlphabeteAls;
				}
			};
			btnVorhandeneAlphabeteAls.setEnabled(true);
			
			showExistingAlphasAsBlocks.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(arg == null) {
						if(showExistingAlphasAsBlocks.getContent()) {
							showExistingAlphasAsBlocks.writeContent(false);
							showExistingAlphasAsBlocks.synchronizeWithUserSide();
							showNotImplementedTooltip(btnVorhandeneAlphabeteAls);
						}
					}
				}
			});

			btnNeuerBaustein = new Button(comp_block_controls, SWT.NONE);
			btnNeuerBaustein.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					BlockAlphabet newBlock = createNewBlockAlphabet();
					if(newBlock != null) {
						addAvailableBlockAlphabet(newBlock, BlockType.SELFCREATED);
					} else {
						showNotImplementedTooltip(btnNeuerBaustein);
					}
				}
			});
			btnNeuerBaustein.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			btnNeuerBaustein.setText("New alphabet block");
			Image addImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
			btnNeuerBaustein.setImage(addImg);

//			lblAusgewhlteBlcke = new Label(grpBausteine, SWT.NONE);
//			GridData gd_lblAusgewhlteBlcke = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//			gd_lblAusgewhlteBlcke.verticalIndent = 5;
//			lblAusgewhlteBlcke.setLayoutData(gd_lblAusgewhlteBlcke);
//			lblAusgewhlteBlcke.setText("Ausgewählte Bausteine:");

//			Composite composite_3 = new Composite(this, SWT.NONE);
//			composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//			composite_3.setLayout(new GridLayout());
//
//			Link link = new Link(composite_3, SWT.NONE);
//			link.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//
//				}
//			});
//			link.setText("<a>mehr Optionen anzeigen...</a>");

//			label_3 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
//			label_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			compResult = new Group(this, SWT.NONE);
			compResult.setText("Selected blocks and resulting alphabet:");
			compResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			compResult.setLayout(new GridLayout(1, false));
			
			grpBlockResult = new Composite(compResult, SWT.NONE);
			RowLayout rl_grpBlockResult = new RowLayout(SWT.HORIZONTAL);
			rl_grpBlockResult.marginTop = 0;
			rl_grpBlockResult.marginBottom = 0;
			rl_grpBlockResult.center = true;
			rl_grpBlockResult.spacing = 0;
			rl_grpBlockResult.marginRight = 0;
			rl_grpBlockResult.marginLeft = 0;
			grpBlockResult.setLayout(rl_grpBlockResult);
			grpBlockResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			lblHierErscheinenDie = new Label(grpBlockResult, SWT.NONE);
			lblHierErscheinenDie.setLayoutData(new RowData());
			lblHierErscheinenDie.setEnabled(false);
			lblHierErscheinenDie.setText("The selected blocks from which the alphabet will be composed will appear here.");
			
			lblrightclickOnThe = new Label(compResult, SWT.NONE);
			lblrightclickOnThe.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.ITALIC));
			lblrightclickOnThe.setText("(Right-click on the selected blocks to reorder and (slightly) transform them)");
			
			lblAlphabetinhaltergebnis = new Label(compResult, SWT.NONE);
			GridData gd_lblAlphabetinhaltergebnis = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_lblAlphabetinhaltergebnis.verticalIndent = 5;
			lblAlphabetinhaltergebnis.setLayoutData(gd_lblAlphabetinhaltergebnis);
			lblAlphabetinhaltergebnis.setText("Result (composed alphabet):");

			composite = new Composite(compResult, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout gl_composite = new GridLayout(2, false);
			gl_composite.marginHeight = 0;
			composite.setLayout(gl_composite);

			lblBuchstaben_1 = new Label(composite, SWT.NONE);
			lblBuchstaben_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblBuchstaben_1.setText("Alphabet characters: ");

			text_ResultCharacters = new Text(composite, SWT.BORDER);
			text_ResultCharacters.setEditable(false);
			text_ResultCharacters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			lblLnge = new Label(composite, SWT.NONE);
			lblLnge.setText("Alphabet length: ");

			lbl_ResultLength = new Label(composite, SWT.NONE);
			lbl_ResultLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lbl_ResultLength.setText("0");

			layoutRoot.layout();
		} catch (Exception ex) {
			LogUtil.logError(ex);
		}

		initializeAvailableBlocks();
		
		alphabetInput = new AbstractUIInput<AbstractAlphabet>() {

			@Override
			protected InputVerificationResult verifyUserChange() {
				return InputVerificationResult.DEFAULT_RESULT_EVERYTHING_OK;
			}

			@Override
			public AbstractAlphabet readContent() {
				return result;
			}

			@Override
			public void writeContent(AbstractAlphabet content) {
				//nothing to do here; will only be called one time when initializing th input
			}

			@Override
			protected AbstractAlphabet getDefaultContent() {
				return new AtomAlphabet("");
			}

			@Override
			public String getName() {
				return "Alphabet";
			}
		};
	}

	protected void showNotImplementedTooltip(Control control) {
		ToolTip t = new ToolTip(getShell(), SWT.NONE);
		t.setText("I am sorry, but this function is not yet implemented.");
		Point loc = control.toDisplay(control.getSize());
		t.setLocation(loc);
		t.setVisible(true);
	}

	/**
	 * Prompts user action to create a new blockalphabet.
	 * 
	 * @return null if canceled.
	 */
	protected BlockAlphabet createNewBlockAlphabet() {
		return null;
	}

	private void initializeAvailableBlocks() {
		addAvailableBlockAlphabets(getDefaultBlocks(), BlockType.DEFAULT);
		gd_compBlocks.minimumWidth = compBlocks.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		gd_compBlocks.widthHint = gd_compBlocks.minimumWidth;
		compBlocks.setLayoutData(gd_compBlocks);
		if(showExistingAlphasAsBlocks.getContent()) {
			addAvailableBlockAlphabets(getExistingAlphabetBlocks(), BlockType.PREEXISTING);
		}
	}

	private List<BlockAlphabet> getDefaultBlocks() {
		RangeBlockAlphabet A_Z = new RangeBlockAlphabet('A', 'Z');
		RangeBlockAlphabet a_z = new RangeBlockAlphabet('a', 'z');
		RangeBlockAlphabet digits = new RangeBlockAlphabet('0', '9');

		BlockAlphabet umlaute = new BlockAlphabet("äöüß");
		BlockAlphabet UMLAUTE = new BlockAlphabet("ÄÖÜ");

		BlockAlphabet space = new BlockAlphabet(" ", "[Space]");
		BlockAlphabet linebreak = new BlockAlphabet("\r\n", "Linebreak (Unix/Windows)");
		BlockAlphabet punctuation = new BlockAlphabet(".:,;-!?");
		BlockAlphabet asciiVisibles = new BlockAlphabet("\"#$%&'()*+/<=>@[\\]^_`{|}~", "ASCII beyond latin alphabet");

		return Arrays.asList(new BlockAlphabet[]{A_Z, a_z, digits, umlaute, UMLAUTE, space, linebreak, punctuation, asciiVisibles});
	}

	private List<BlockAlphabet> getExistingAlphabetBlocks() {
		List<BlockAlphabet> currentBlocks = blockAlphabetsAvailable;
		List<BlockAlphabet> result = new LinkedList<BlockAlphabet>();
		List<AbstractAlphabet> existingAlphas = Arrays.asList(AlphabetsManager.getInstance().getAlphabets());

		for(AbstractAlphabet existingAlpha: existingAlphas) {
			boolean alreadyExistsAsBlock = false;
			for(BlockAlphabet existingBlock: currentBlocks) {
				if(String.valueOf(existingBlock.getCharacterSet()).equals(String.valueOf(existingAlpha.getCharacterSet()))) {
					alreadyExistsAsBlock = true;
					break;
				}
				if(!alreadyExistsAsBlock) {
					result.add(new BlockAlphabet(String.valueOf(existingAlpha.getCharacterSet()), existingAlpha.getShortName()));
				}
			}
		}

		return result;
	}

	enum BlockType {
		DEFAULT, PREEXISTING, SELFCREATED;

		public List<BlockAlphabet> blocks = new LinkedList<BlockAlphabet>();
	}

	private void addButtonToSelectedSection(final BlockAlphabet alpha) {
		final Button newButton = new Button(grpBlockResult, SWT.PUSH);
		newButton.setLayoutData(new RowData(SWT.DEFAULT, 33));
		blockButtonsSelected.add(newButton);
		displaySelectedBlockOnButton(newButton, alpha);

		Menu contextMenu = createMenuForSelectedBlockBtn(alpha, selectedBlockAlphabets.get(alpha), newButton);
		newButton.setMenu(contextMenu);
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(newButton.getMenu() != null) {
					newButton.getMenu().setVisible(true);
				}
			}
		});
		
		if(grpBlockResult.getChildren().length > 1) {
			lblHierErscheinenDie.setVisible(false);
			RowData l = (RowData) lblHierErscheinenDie.getLayoutData();
			l.exclude = true;
		}
		
		refreshContextMenusSelectedButtons();

		grpBlockResult.pack();
		grpBlockResult.layout();
		layoutRoot.layout();
	}

	private Menu createMenuForSelectedBlockBtn(final BlockAlphabet originalAlphaBlock, final BlockAlphabet actualBlockForm, final Button button) {
		Menu contextMenu = new Menu(button.getShell(), SWT.POP_UP);
		{
			MenuItem showContentItem = new MenuItem(contextMenu, SWT.PUSH);
			showContentItem.setText("&Show block content");
//			Image showImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
			Image showImg = AlphabetsPlugin.getImageDescriptor("img/search.gif").createImage();
			showContentItem.setImage(showImg);
			
			showContentItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					showBlockContentOnscreen(button, actualBlockForm, getTypeFor(originalAlphaBlock));
				}
			});
			
			MenuItem removeBlockItem = new MenuItem(contextMenu, SWT.PUSH);
			removeBlockItem.setText("&Don't use this block");
			Image removeImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
			removeBlockItem.setImage(removeImg);
			
			removeBlockItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectAvailableAlphabet(originalAlphaBlock, false);
				}
			});
			
			boolean isEligibleForShiftLeft = selectedBlockAlphabetsInOrder.indexOf(originalAlphaBlock) > 0;
			boolean showShiftLeft = true;
			if(showShiftLeft) {
				MenuItem shiftLeftItem = new MenuItem(contextMenu, SWT.PUSH);
				if(! isEligibleForShiftLeft) shiftLeftItem.setEnabled(false);
				shiftLeftItem.setText("Move left");
				Image shiftLeftImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK);
				shiftLeftItem.setImage(shiftLeftImg);
				
				shiftLeftItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						int actualIndex = selectedBlockAlphabetsInOrder.indexOf(originalAlphaBlock);
						BlockAlphabet otherBlock = selectedBlockAlphabetsInOrder.get(actualIndex-1);
						selectedBlockAlphabetsInOrder.set(actualIndex, otherBlock);
						selectedBlockAlphabetsInOrder.set(actualIndex-1, originalAlphaBlock);
						Button thisButton = blockButtonsSelected.get(actualIndex);
						Button otherButton = blockButtonsSelected.get(actualIndex-1);
						blockButtonsSelected.set(actualIndex, otherButton);
						blockButtonsSelected.set(actualIndex-1, thisButton);
						thisButton.moveAbove(otherButton);
						layoutRoot.layout(new Control[]{otherButton, thisButton});
						calcAndDisplayResultAlphabet();
						refreshContextMenusSelectedButtons();
					}
				});
			}

			boolean isEligibleForShiftRight = selectedBlockAlphabetsInOrder.indexOf(originalAlphaBlock) < selectedBlockAlphabetsInOrder.size()-1;
			boolean showShiftRight = true;
			if(showShiftRight) {
				MenuItem shiftRightItem = new MenuItem(contextMenu, SWT.PUSH);
				if(! isEligibleForShiftRight) shiftRightItem.setEnabled(false);
				shiftRightItem.setText("Move right");
				Image shiftLeftImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD);
				shiftRightItem.setImage(shiftLeftImg);
				
				shiftRightItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						int actualIndex = selectedBlockAlphabetsInOrder.indexOf(originalAlphaBlock);
						BlockAlphabet otherBlock = selectedBlockAlphabetsInOrder.get(actualIndex+1);
						selectedBlockAlphabetsInOrder.set(actualIndex, otherBlock);
						selectedBlockAlphabetsInOrder.set(actualIndex+1, originalAlphaBlock);
						Button thisButton = blockButtonsSelected.get(actualIndex);
						Button otherButton = blockButtonsSelected.get(actualIndex+1);
						blockButtonsSelected.set(actualIndex, otherButton);
						blockButtonsSelected.set(actualIndex+1, thisButton);
						thisButton.moveBelow(otherButton);
						layoutRoot.layout(new Control[]{otherButton, thisButton});
						calcAndDisplayResultAlphabet();
						refreshContextMenusSelectedButtons();
					}
				});
			}
			
			boolean isEligibleForReverse = actualBlockForm.getCharacterSet().length > 1;
			MenuItem reverseAlphaItem;
			if(isEligibleForReverse) {
				reverseAlphaItem = new MenuItem(contextMenu, SWT.PUSH);
				reverseAlphaItem.setText("&Reverse order...");
				Image reverseImg = null;//PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
				reverseAlphaItem.setImage(reverseImg);
				
				reverseAlphaItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						BlockAlphabet transformedAlpha = transformAlphabetBlock(originalAlphaBlock, RevertBlock.class);
						selectedBlockAlphabets.put(originalAlphaBlock, transformedAlpha);
						displaySelectedBlockOnButton(button, transformedAlpha);
						calcAndDisplayResultAlphabet();
						refreshContextMenuForSelectedBlockBtn(originalAlphaBlock);
					}
				});
			}
			
			boolean isEligibleForLeaveOneOut = actualBlockForm.getCharacterSet().length > 1;
			MenuItem looItem = null;
			if(isEligibleForLeaveOneOut) {
				looItem = new MenuItem(contextMenu, SWT.PUSH);
				looItem.setText("&Leave one character out...");
				Image looImg = null;//PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
				looItem.setImage(looImg);
				
				looItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						BlockAlphabet transformedAlpha = transformAlphabetBlock(originalAlphaBlock, ExcludeCharBlock.class);
						selectedBlockAlphabets.put(originalAlphaBlock, transformedAlpha);
						displaySelectedBlockOnButton(button, transformedAlpha);
						calcAndDisplayResultAlphabet();
						refreshContextMenuForSelectedBlockBtn(originalAlphaBlock);
					}
				});
			}
			
			MenuItem looCancelItem = null;
			boolean isEligibleForCancelLeaveOneOut = actualBlockForm instanceof ExcludeCharBlock;
			if(isEligibleForCancelLeaveOneOut) {
				looCancelItem = new MenuItem(contextMenu, SWT.PUSH);
				looCancelItem.setText("Restore left out characters");
				Image looCancelImg = null;//PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
				looCancelItem.setImage(looCancelImg);
				
				looCancelItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						BlockAlphabet transformedAlpha = actualBlockForm;
						while(transformedAlpha instanceof ExcludeCharBlock) {
							ExcludeCharBlock hasOriginalBlockAlpha = (ExcludeCharBlock) transformedAlpha;
							transformedAlpha = hasOriginalBlockAlpha.getOrigAlphabet();
						}
						selectedBlockAlphabets.put(originalAlphaBlock, transformedAlpha);
						displaySelectedBlockOnButton(button, transformedAlpha);
						calcAndDisplayResultAlphabet();
						refreshContextMenuForSelectedBlockBtn(originalAlphaBlock);
					}
				});
			}
			
			boolean isEligibleForResetModifications = actualBlockForm instanceof HasOriginalBlockAlpha;
			if(isEligibleForResetModifications) {
				MenuItem resetItem = new MenuItem(contextMenu, SWT.PUSH);
				resetItem.setText("Reset modifications to this block");
				Image resetImg = null;//PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
				resetItem.setImage(resetImg);
				
				resetItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						BlockAlphabet transformedAlpha = originalAlphaBlock;
						selectedBlockAlphabets.put(originalAlphaBlock, transformedAlpha);
						displaySelectedBlockOnButton(button, transformedAlpha);
						calcAndDisplayResultAlphabet();
						refreshContextMenuForSelectedBlockBtn(originalAlphaBlock);
					}
				});
			}
			
			if(isEligibleForLeaveOneOut && isEligibleForCancelLeaveOneOut) {
				new MenuItem(contextMenu, SWT.SEPARATOR, contextMenu.indexOf(looItem));
				if(contextMenu.getItemCount()-1>contextMenu.indexOf(looCancelItem)) {
					new MenuItem(contextMenu, SWT.SEPARATOR, contextMenu.indexOf(looItem)+2);
				}
			}
			
			if(contextMenu.getItemCount() > 2) {//after view/remove
				new MenuItem(contextMenu, SWT.SEPARATOR, 2);
			}
			if(contextMenu.getItemCount() > 5) { //after move right/löeft
				new MenuItem(contextMenu, SWT.SEPARATOR, 5);
			}
		}
		
		return contextMenu;
	}

	/**
	 * Transforms a selected alphabet with the given method. May involve blocking user interface interaction.
	 * 
	 * @param alpha the originally selected alphabet block
	 * @param method the method (class of the new BlockAlphabet) to perform.
	 * 
	 * @returns true if the user didn't cancel
	 */
	protected BlockAlphabet transformAlphabetBlock(BlockAlphabet alpha, Class<? extends BlockAlphabet> method) {
		Button btn = getButtonForSelectedBlockAlphabet(alpha);
		BlockAlphabet alphaNow = selectedBlockAlphabets.get(alpha); //This is necessary, 
			//because the could've been a transformation in place already (so we chain those)
		
		if(method == RevertBlock.class) {
			if(alphaNow instanceof RevertBlock) {
				return ((RevertBlock) alphaNow).getOrigAlphabet();
			} else {
				return new RevertBlock(alphaNow);
			}
		} else if(method == ExcludeCharBlock.class) {
			LeaveOutCharSelectorWizard charSelectorWizard = new LeaveOutCharSelectorWizard(alphaNow);
			WizardDialog charSelector = new WizardDialog(getShell(), charSelectorWizard);
			int dialogResult = charSelector.open();
						
			Character leaveOutChar = charSelectorWizard.getSelectedChar();
			if(leaveOutChar != null) {
				return new ExcludeCharBlock(alphaNow, leaveOutChar);
			} else {
				return alphaNow;
			}
		} else {
			LogUtil.logError(new Exception("Couldn't perform transformation: no known type."));
			return alphaNow;
		}
	}

	/**
	 * displays a selected block-alphabet on a button.
	 * 
	 * @param button the button
	 * @param alpha the alphabet in it's post-selection transformed form (because the alphabet may be transformed after it has been selected)
	 */
	private void displaySelectedBlockOnButton(Button button, BlockAlphabet alpha) {
		button.setText(alpha.getBlockName());
		compResult.layout(new Control[]{button});
		//TODO: more detail.
	}

	private void removeButtonFromSelectedSection(BlockAlphabet alpha) {
		Button b = getButtonForSelectedBlockAlphabet(alpha);
		if(b != null) {
			blockButtonsSelected.remove(b);
			b.dispose();

			if(grpBlockResult.getChildren().length <= 1) {
				lblHierErscheinenDie.setVisible(true);
				RowData l = (RowData) lblHierErscheinenDie.getLayoutData();
				l.exclude = false;
			}

			grpBlockResult.layout();
			layoutRoot.layout();
		}
	}

	private BlockAlphabet getSelectedBlockAlphaForButton(Button btn) {
		return selectedBlockAlphabetsInOrder.get(blockButtonsSelected.indexOf(btn));
	}

	private Button getButtonForSelectedBlockAlphabet(BlockAlphabet alpha) {
		return blockButtonsSelected.get(selectedBlockAlphabetsInOrder.indexOf(alpha));
	}

	protected void selectAvailableAlphabet(BlockAlphabet alpha, boolean selection) {
		
		if(selectedBlockAlphabetsInOrder.contains(alpha) != selection) {
			if(selection) {
				selectedBlockAlphabetsInOrder.add(alpha);
				selectedBlockAlphabets.put(alpha, alpha);

				addButtonToSelectedSection(alpha);
			} else {
				removeButtonFromSelectedSection(alpha);

				selectedBlockAlphabetsInOrder.remove(alpha);
				selectedBlockAlphabets.remove(alpha);
			}
		} else {
//			throw new RuntimeException("Tried to select or unselect an alphabet which was already in this state");
			//no error necessary
		}
		Button availableAlphaBtn = getButtonForAvailableBlockAlphabet(alpha);
		availableAlphaBtn.setSelection(selection);

		calcAndDisplayResultAlphabet();
	}

	protected void refreshContextMenuForSelectedBlockBtn(BlockAlphabet originalAlphaBlock) {
		Button buttonForSelectedBlockAlphabet = getButtonForSelectedBlockAlphabet(originalAlphaBlock);
		buttonForSelectedBlockAlphabet.getMenu().dispose();
		buttonForSelectedBlockAlphabet.setMenu(
				createMenuForSelectedBlockBtn(originalAlphaBlock, selectedBlockAlphabets.get(originalAlphaBlock), buttonForSelectedBlockAlphabet)
			);
	}
	
	private void refreshContextMenusSelectedButtons() {
		for(BlockAlphabet alpha: selectedBlockAlphabetsInOrder) {
			refreshContextMenuForSelectedBlockBtn(alpha);
		}
	}

	private void calcAndDisplayResultAlphabet() {
		if(selectedBlockAlphabets != null && selectedBlockAlphabets.size() > 0) {
			List<AtomAlphabet> alphas = new LinkedList<AtomAlphabet>();
			for(BlockAlphabet a: selectedBlockAlphabetsInOrder) {
				alphas.add(selectedBlockAlphabets.get(a));
			}
			result = new CompositeAlphabet(alphas);
			text_ResultCharacters.setText(AtomAlphabet.alphabetContentAsString(result.getCharacterSet()));
			lbl_ResultLength.setText(""+result.getCharacterSet().length);
		} else {
			text_ResultCharacters.setText("");
			lbl_ResultLength.setText("");
			result = new AtomAlphabet("");
		}
		alphabetInput.synchronizeWithUserSide();
	}

	private BlockType getTypeFor(BlockAlphabet alpha) {
		for(BlockType t: BlockType.values()) {
			if(t.blocks.contains(alpha)) return t;
		}
		return null;
	}

	private BlockAlphabet getAvailableBlockAlphaForButton(Button btn) {
		return blockAlphabetsAvailable.get(blockButtonsAvailable.indexOf(btn));
	}

	private Button getButtonForAvailableBlockAlphabet(BlockAlphabet alpha) {
		return blockButtonsAvailable.get(blockAlphabetsAvailable.indexOf(alpha));
	}


	//-3-methods for removing block alphabets that are available for selection
	private void removeAvailableBlockAlphabet(BlockAlphabet blockAlpha, boolean layout) {
		selectAvailableAlphabet(blockAlpha, false);
		Button b = getButtonForAvailableBlockAlphabet(blockAlpha);
		blockAlphabetsAvailable.remove(blockAlpha);
		if(b != null) {
			blockButtonsAvailable.remove(b);
			b.dispose();
			if(layout) {
				compBlocks.layout();
				layoutRoot.layout();
			}
		}
	}
	private void removeAvailableBlockAlphabet(BlockAlphabet blockAlpha) {
		removeAvailableBlockAlphabet(blockAlpha, true);
	}
	private void removeAvailableBlockAlphabets(Collection<BlockAlphabet> blockAlphas) {
		int counter = 0;
		for(BlockAlphabet alpha: blockAlphas) {
			removeAvailableBlockAlphabet(alpha, counter == blockAlphas.size()-1);
			counter++;
		}
	}

	private void addAvailableBlockAlphabet(final BlockAlphabet blockAlphabet, final BlockType type, boolean layout) {
		final Button newButton = new Button(compBlocks, SWT.TOGGLE);
		newButton.setLayoutData(new RowData(SWT.DEFAULT, 33));
		newButton.setText(blockAlphabet.getBlockName());

		blockAlphabetsAvailable.add(blockAlphabet);
		blockButtonsAvailable.add(newButton);
		type.blocks.add(blockAlphabet);

		newButton.addSelectionListener(new SelectionAdapter() {
			Button father = newButton;
			@Override
			public void widgetSelected(SelectionEvent e) {
				BlockAlphabet correspondingAvailableAlphabet = getAvailableBlockAlphaForButton(father);
				selectAvailableAlphabet(correspondingAvailableAlphabet, father.getSelection());
			}
		});
		
		Menu contextMenu = new Menu(compMain.getShell(), SWT.POP_UP);
		{
			MenuItem showContentItem = new MenuItem(contextMenu, SWT.PUSH);
			showContentItem.setText("&Show block content");
//			Image showImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
			Image showImg = AlphabetsPlugin.getImageDescriptor("img/search.gif").createImage();
			showContentItem.setImage(showImg);
			
			showContentItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					showBlockContentOnscreen(newButton, blockAlphabet, type);
				}
			});
			
			if(type == BlockType.SELFCREATED) {
				MenuItem removeBlockItem = new MenuItem(contextMenu, SWT.PUSH);
				removeBlockItem.setText("&Remove this self-created block");
				Image removeImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE);
				removeBlockItem.setImage(removeImg);
				
				removeBlockItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						removeAvailableBlockAlphabet(blockAlphabet);
					}
				});
			}
		}
		
		newButton.setMenu(contextMenu);
		

		if(layout) {
			compBlocks.layout();
			layoutRoot.layout();
		}
	}

	/**
	 * Displays the content of an alphabet block onscreen.
	 * 
	 * @param representant the widget representing the alphabet block
	 * @param blockAlphabet the blockAlphabet
	 * @param type the type
	 */
	protected void showBlockContentOnscreen(Button representant, BlockAlphabet blockAlphabet, BlockType type) {
		ShowAlphaContentWindow shell = new ShowAlphaContentWindow(getDisplay(), blockAlphabet);
		shell.open();
		int width = getDisplay().getBounds().width;
		int height = getDisplay().getBounds().height;
		shell.setLocation(width/2-160, height/2-40);
	}

	private void addAvailableBlockAlphabet(BlockAlphabet blockAlphabet, BlockType type) {
		addAvailableBlockAlphabet(blockAlphabet, type, true);
	}

	private void addAvailableBlockAlphabets(Collection<BlockAlphabet> blockAlphabets, BlockType type) {
		int counter = 0;
		for(BlockAlphabet alpha: blockAlphabets) {
			addAvailableBlockAlphabet(alpha, type, counter == blockAlphabets.size()-1);
			counter++;
		}
	}

	private void resetBlockSelections() {
		LinkedList<BlockAlphabet> blockAlphabetsCurrentlyAvailableClone = new LinkedList<BlockAlphabet>(blockAlphabetsAvailable);
		for(BlockAlphabet a: blockAlphabetsCurrentlyAvailableClone) {
			selectAvailableAlphabet(a, false);
		}
	}

	private boolean isBlockAlphabetSelected(BlockAlphabet a) {
		return selectedBlockAlphabetsInOrder.contains(a);
	}
	
	public AbstractAlphabet getAlphabet() {
		return alphabetInput.getContent();
	}

	public AbstractUIInput<AbstractAlphabet> getAlphabetInput() {
		return alphabetInput;
	}
}
