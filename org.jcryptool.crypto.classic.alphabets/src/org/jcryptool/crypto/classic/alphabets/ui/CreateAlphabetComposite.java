package org.jcryptool.crypto.classic.alphabets.ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.operations.alphabets.AbstractAlphabet;
import org.jcryptool.core.operations.alphabets.AlphabetsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.core.util.input.ButtonInput;
import org.jcryptool.core.util.input.InputVerificationResult;
import org.jcryptool.crypto.classic.alphabets.AlphabetsPlugin;
import org.jcryptool.crypto.classic.alphabets.composite.CompositeAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.BlockAlphabet;
import org.jcryptool.crypto.classic.alphabets.ui.alphabetblocks.RangeBlockAlphabet;


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
public class CreateAlphabetComposite extends org.eclipse.swt.widgets.Composite {
	private Composite composite_1;
	private Composite composite_2;
	private Link linkViewHint;
	private Composite compMain;
	private Label lblKlickenSieDie;
	private Composite grpBausteine;
	private Composite compBlocks;
	private Label label;
	private Label label_1;
	private Composite comp_block_controls;
	private Button btnVorhandeneAlphabeteAls;
	private Button btnNeuerBaustein;
	private Composite spacer_footer;
	private Composite compResult;
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
		CreateAlphabetComposite inst = new CreateAlphabetComposite(shell);
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

	public CreateAlphabetComposite(org.eclipse.swt.widgets.Composite parent) {
		super(parent, SWT.NONE);
		initGUI();
	}

	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout();
			thisLayout.makeColumnsEqualWidth = true;
			this.setLayout(thisLayout);

			compMain = new Composite(this, SWT.NONE);
			compMain.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			compMain.setLayout(new GridLayout(1, false));

			grpBausteine = new Composite(compMain, SWT.NONE);
			grpBausteine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout gl_grpBausteine = new GridLayout(1, false);
			gl_grpBausteine.marginHeight = 0;
			gl_grpBausteine.marginWidth = 0;
			grpBausteine.setLayout(gl_grpBausteine);

			lblKlickenSieDie = new Label(grpBausteine, SWT.WRAP);
			lblKlickenSieDie.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
			lblKlickenSieDie.setText("Klicken Sie die verschiedenen Bausteine an, um ein Alphabet zusammenzusetzen:");

			comp_blocks_hull = new Composite(grpBausteine, SWT.BORDER);
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
			btnVorhandeneAlphabeteAls.setText("Vorhandene Alphabete als Bausteine zeigen");
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

			btnNeuerBaustein = new Button(comp_block_controls, SWT.NONE);
			btnNeuerBaustein.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
				}
			});
			btnNeuerBaustein.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
			btnNeuerBaustein.setText("Neuer Baustein");
			Image addImg = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ADD);
			btnNeuerBaustein.setImage(addImg);

			lblAusgewhlteBlcke = new Label(grpBausteine, SWT.NONE);
			GridData gd_lblAusgewhlteBlcke = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_lblAusgewhlteBlcke.verticalIndent = 5;
			lblAusgewhlteBlcke.setLayoutData(gd_lblAusgewhlteBlcke);
			lblAusgewhlteBlcke.setText("Ausgewählte Blöcke:");

			grpBlockResult = new Composite(grpBausteine, SWT.NONE);
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
			lblHierErscheinenDie.setText("Hier erscheinen die Blöcke, aus denen das Alphabet zusammengesetzt wird");

			Composite composite_3 = new Composite(this, SWT.NONE);
			composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			composite_3.setLayout(new GridLayout());

			Link link = new Link(composite_3, SWT.NONE);
			link.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

				}
			});
			link.setText("<a>mehr Optionen anzeigen...</a>");

			label_3 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			label_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			compResult = new Composite(this, SWT.NONE);
			compResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			compResult.setLayout(new GridLayout(1, false));

			lblAlphabetinhaltergebnis = new Label(compResult, SWT.NONE);
			GridData gd_lblAlphabetinhaltergebnis = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_lblAlphabetinhaltergebnis.verticalIndent = 5;
			lblAlphabetinhaltergebnis.setLayoutData(gd_lblAlphabetinhaltergebnis);
			lblAlphabetinhaltergebnis.setText("Alphabetinhalt (Ergebnis):");

			composite = new Composite(compResult, SWT.NONE);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			composite.setLayout(new GridLayout(2, false));

			lblBuchstaben_1 = new Label(composite, SWT.NONE);
			lblBuchstaben_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblBuchstaben_1.setText("Buchstaben: ");

			text_ResultCharacters = new Text(composite, SWT.BORDER);
			text_ResultCharacters.setEditable(false);
			text_ResultCharacters.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			lblLnge = new Label(composite, SWT.NONE);
			lblLnge.setText("Länge: ");

			lbl_ResultLength = new Label(composite, SWT.NONE);
			lbl_ResultLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			lbl_ResultLength.setText("0");

			spacer_footer = new Composite(this, SWT.NONE);
			spacer_footer.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
			GridLayout gl_spacer_footer = new GridLayout(1, false);
			gl_spacer_footer.marginHeight = 0;
			gl_spacer_footer.marginWidth = 0;
			spacer_footer.setLayout(gl_spacer_footer);

			label_1 = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
			label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			Composite compViewHint = new Composite(this, SWT.NONE);
			GridLayout gl_compViewHint = new GridLayout(1, false);
			gl_compViewHint.marginHeight = 0;
			gl_compViewHint.marginWidth = 0;
			compViewHint.setLayout(gl_compViewHint);
			compViewHint.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

			linkViewHint = new Link(compViewHint, SWT.NONE);
			linkViewHint.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			linkViewHint.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
			linkViewHint.setText("<a>zur manuellen Eingabe wecheln</a>");


			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initializeAvailableBlocks();
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
		BlockAlphabet UMLAUTE = new BlockAlphabet("AÖÜ");

		BlockAlphabet space = new BlockAlphabet(" ", "[Leerzeichen");
		BlockAlphabet linebreak = new BlockAlphabet("\r\n", "Zeilenumbruch (Unix/Windows)");
		BlockAlphabet punctuation = new BlockAlphabet(".:,;-!?");
		BlockAlphabet asciiVisibles = new BlockAlphabet("\"#$%&'()*+/0123456789<=>@[\\]^_`{|}~", "Sichtbare ASCII-Sonderzeichen");

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

	private void addButtonToSelectedSection(BlockAlphabet alpha) {
		final Button newButton = new Button(grpBlockResult, SWT.PUSH);
		newButton.setLayoutData(new RowData(SWT.DEFAULT, 33));
		newButton.setText(alpha.getBlockName());

		if(grpBlockResult.getChildren().length > 1) {
			lblHierErscheinenDie.setVisible(false);
			RowData l = (RowData) lblHierErscheinenDie.getLayoutData();
			l.exclude = true;
		}

		grpBlockResult.pack();
		grpBlockResult.layout();
		this.layout();

		blockButtonsSelected.add(newButton);
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
			this.layout();
		}
	}

	private BlockAlphabet getSelectedBlockAlphaForButton(Button btn) {
		return selectedBlockAlphabetsInOrder.get(blockButtonsSelected.indexOf(btn));
	}

	private Button getButtonForSelectedBlockAlphabet(BlockAlphabet alpha) {
		return blockButtonsSelected.get(selectedBlockAlphabetsInOrder.indexOf(alpha));
	}

	protected void selectAvailableAlphabet(
			BlockAlphabet alpha, boolean selection) {
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
			throw new RuntimeException("Tried to select or unselect an alphabet which was already in this state");
		}

		refreshResultAlphabet();
	}

	private void refreshResultAlphabet() {
		if(selectedBlockAlphabets.size() > 0) {
			CompositeAlphabet result = new CompositeAlphabet(selectedBlockAlphabetsInOrder);
			text_ResultCharacters.setText(String.valueOf(result.getCharacterSet()));
			lbl_ResultLength.setText(""+result.getCharacterSet().length);
		} else {
			text_ResultCharacters.setText("");
			lbl_ResultLength.setText("");
		}
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
		blockAlphabetsAvailable.remove(blockAlpha);
		Button b = getButtonForAvailableBlockAlphabet(blockAlpha);
		if(b != null) {
			blockButtonsAvailable.remove(b);
			b.dispose();
			if(layout) {
				compBlocks.layout();
				this.layout();
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

	private void addAvailableBlockAlphabet(BlockAlphabet blockAlphabet, BlockType type, boolean layout) {
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

		if(layout) {
			compBlocks.layout();
			this.layout();
		}
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

}
