package org.jcryptool.crypto.ui.textloader.ui.wizard.loadtext;

import java.io.File;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.core.operations.editors.EditorsManager;
import org.jcryptool.core.util.input.AbstractUIInput;
import org.jcryptool.crypto.ui.textloader.ui.ControlHatcher;
import org.jcryptool.crypto.ui.textsource.TextInputWithSource;
import org.jcryptool.crypto.ui.textsource.TextSourceType;

public class LoadTextWizardPage extends WizardPage {

	private Composite container;
	private Group grpText;
	private Composite compTextInputMode;
	private Composite composite;
	private Button btnJcteditor;
	private Combo comboEditorInputSelector;
	private Button btnDatei;
	private Composite compFileInputDetails;
	private Label lblFilenametxt;
	private Link linkChangeFile;
	private Button btnZwischenablageeigeneEingabe;
	private Group grpTextinputCtrls;
	private Composite textfieldComp;
	private Text txtInputText;
	private List<IEditorReference> editorRefs;
	private org.jcryptool.crypto.ui.textloader.ui.wizard.loadtext.TextonlyInput textOnlyInput;
	/**
	 * The file that was last using the file selection wizard on press of the
	 * "select text from file" radiobutton
	 */
	protected File fileTextInput;
	private TextInputWithSource initTextObject;
	private boolean isPageBuilt;
	private UIInputTextWithSource textInput;
	private ControlHatcher beforeWizardTextParasiteLabel;
	private ControlHatcher afterWizardTextParasiteLabel;

	/**
	 * Create the wizard.
	 */
	public LoadTextWizardPage() {
		this(null, null);
	}

	public LoadTextWizardPage(ControlHatcher beforeWizardTextParasiteLabel, ControlHatcher afterWizardTextParasiteLabel) {
		super("Load text");
		this.beforeWizardTextParasiteLabel = beforeWizardTextParasiteLabel;
		this.afterWizardTextParasiteLabel = afterWizardTextParasiteLabel;
		setTitle("Load text");
		setDescription("Load a text for the substitution analysis");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		if(beforeWizardTextParasiteLabel != null) {
			Control control = beforeWizardTextParasiteLabel.hatch(container);
			GridData lData = (GridData) control.getLayoutData();
			lData.verticalSpan = 1;
//			lData.verticalIndent = 1;
		}
		
		{
			grpText = new Group(container, SWT.NONE);
			grpText.setText(Messages.TranspTextWizardPage_grpText_text);
			grpText.setLayout(new GridLayout(2, false));
			grpText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			{
				compTextInputMode = new Composite(grpText, SWT.NONE);
				compTextInputMode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
				compTextInputMode.setLayout(new GridLayout(1, false));

				Label lblLadenDesTextes = new Label(compTextInputMode, SWT.NONE);
				lblLadenDesTextes.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
				lblLadenDesTextes.setBounds(0, 0, 55, 15);
				lblLadenDesTextes.setText(Messages.TranspTextWizardPage_lblLadenDesTextes_text);
				{
					composite = new Composite(compTextInputMode, SWT.NONE);
					composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					composite.setLayout(new GridLayout(1, false));
					{
						btnJcteditor = new Button(composite, SWT.RADIO);
						btnJcteditor.setText(Messages.TranspTextWizardPage_btnJcteditor_text);
					}
					{
						comboEditorInputSelector = new Combo(composite, SWT.NONE) {
							@Override
							protected void checkSubclass() {};
							@Override
							public org.eclipse.swt.graphics.Point computeSize(int wHint, int hHint, boolean changed) {
								Point result = super.computeSize(wHint, hHint, changed);
								return new Point(getAppropriateXValue(result.x, 160), result.y);
							};

							private int getAppropriateXValue(int superXCalc, int maxSize) {
								return Math.min(superXCalc, maxSize);
							}
						};
						GridData gd_comboEditorInputSelector = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
						gd_comboEditorInputSelector.horizontalIndent = 15;
						comboEditorInputSelector.setLayoutData(gd_comboEditorInputSelector);
					}
					{
						btnDatei = new Button(composite, SWT.RADIO);
						btnDatei.setText(Messages.TranspTextWizardPage_btnDatei_text);
					}
					{
						compFileInputDetails = new Composite(composite, SWT.NONE);
						GridData gd_compFileInputDetails = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
						gd_compFileInputDetails.horizontalIndent = 15;
						compFileInputDetails.setLayoutData(gd_compFileInputDetails);
						GridLayout gl_compFileInputDetails = new GridLayout(2, false);
						gl_compFileInputDetails.marginWidth = 0;
						gl_compFileInputDetails.marginHeight = 0;
						compFileInputDetails.setLayout(gl_compFileInputDetails);

						lblFilenametxt = new Label(compFileInputDetails, SWT.NONE);
						lblFilenametxt.setBounds(0, 0, 55, 15);
						lblFilenametxt.setText(Messages.TranspTextWizardPage_lblFilenametxt_text);

						linkChangeFile = new Link(compFileInputDetails, SWT.NONE);
						linkChangeFile.setText(Messages.TranspTextWizardPage_link_text);
					}
					{
						btnZwischenablageeigeneEingabe = new Button(composite, SWT.RADIO);
						btnZwischenablageeigeneEingabe
							.setText(Messages.TranspTextWizardPage_btnZwischenablageeigeneEingabe_text);
						btnZwischenablageeigeneEingabe.addSelectionListener(new SelectionAdapter() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								if(btnZwischenablageeigeneEingabe.getSelection()) {
									doTxfieldActionPreservingVisibleLines(new Runnable() {
										@Override
										public void run() {
											txtInputText.setSelection(txtInputText.getText().length(), 0);
											txtInputText.forceFocus();
										}
									}, txtInputText);
								}
							}
						});
					}
				}
			}
			{
				grpTextinputCtrls = new Group(grpText, SWT.NONE);
				GridLayout group2Layout = new GridLayout();
				grpTextinputCtrls.setLayout(group2Layout);
				GridData group2LData = new GridData();
				group2LData.verticalAlignment = SWT.FILL;
				group2LData.grabExcessHorizontalSpace = true;
				group2LData.horizontalAlignment = GridData.FILL;
				grpTextinputCtrls.setLayoutData(group2LData);
				grpTextinputCtrls.setText(Messages.TranspTextWizardPage_text);
				{
					textfieldComp = new Composite(grpTextinputCtrls, SWT.NONE);
					GridLayout composite2Layout = new GridLayout();
					composite2Layout.makeColumnsEqualWidth = true;
					composite2Layout.marginHeight = 0;
					composite2Layout.marginWidth = 0;
					GridData composite2LData = new GridData();
					composite2LData.grabExcessHorizontalSpace = true;
					composite2LData.horizontalAlignment = GridData.FILL;
					composite2LData.grabExcessVerticalSpace = true;
					composite2LData.verticalAlignment = GridData.FILL;
					textfieldComp.setLayoutData(composite2LData);
					textfieldComp.setLayout(composite2Layout);
					{
						txtInputText = new Text(textfieldComp, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
						GridData text1LData = new GridData();
						text1LData.grabExcessVerticalSpace = true;
						text1LData.verticalAlignment = SWT.FILL;
						text1LData.grabExcessHorizontalSpace = true;
						text1LData.horizontalAlignment = GridData.FILL;

						GC temp = new GC(txtInputText);
						int lines = 4;
						int charHeight = temp.getFontMetrics().getAscent() + 2 * temp.getFontMetrics().getLeading();
						int height = lines * charHeight;
						temp.dispose();
						text1LData.widthHint = 200;
						text1LData.heightHint = height;

						txtInputText.setLayoutData(text1LData);

					}
				}
			}
		}
		
		if(afterWizardTextParasiteLabel != null) {
//			new Label(container, SWT.NONE);
			Control control = afterWizardTextParasiteLabel.hatch(container);
			GridData lData = (GridData) control.getLayoutData();
			lData.verticalSpan = 1;
			lData.verticalIndent = 10;
		}

		setControl(container);
		initializeTextInput();
		setPageComplete(true);
		isPageBuilt = true;
	}
	
	/**
	 * Runs a runnable, which executes some code. The top visible line number of the text field is
	 * remembered, and after the runnable has finished, the top visible line is restored is set to the remembered number.
	 *
	 * @param runnable
	 */
	protected void doTxfieldActionPreservingVisibleLines(final Runnable runnable, Text textfield) {
		final Display display = textfield.getDisplay();
		final int topIndex = textfield.getTopIndex();
		runnable.run();

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					LogUtil.logError(e);
				}
				Runnable r = new Runnable() {
					@Override
					public void run() {
						txtInputText.setTopIndex(topIndex);
					}
				};

				display.syncExec(r);
			};
		}.start();
	}
	
	private void loadEditorsAndFillEditorChooser() {
		IEditorReference activeReference = EditorsManager.getInstance().getActiveEditorReference();
		for (int i = 0; i < editorRefs.size(); i++) {
			comboEditorInputSelector.add(editorRefs.get(i).getTitle());
			if (activeReference != null && activeReference.equals(editorRefs.get(i))) {
				comboEditorInputSelector.setText(editorRefs.get(i).getTitle());
				comboEditorInputSelector.select(i);
			}
		}
		if (activeReference == null) {
			if (editorRefs.size() > 0) {
				comboEditorInputSelector.select(0);
			}
		}
	}
	
	private void initializeTextInput() {

		editorRefs = EditorsManager.getInstance().getEditorReferences();
		if (editorRefs.size() > 0) {
			// TODO: rename method (no loading here!)
			loadEditorsAndFillEditorChooser();
		}

		textOnlyInput = new TextonlyInput() {
			@Override
			public Text getTextfield() {
				return txtInputText;
			}
		};

		textInput = new UIInputTextWithSource(editorRefs) {
			@Override
			protected Button getFileRadioButton() {
				return btnDatei;
			}

			@Override
			protected Button getBtnJctEditorOption() {
				return btnJcteditor;
			}

			@Override
			protected Button getBtnOwninput() {
				return btnZwischenablageeigeneEingabe;
			}

			@Override
			protected File getSelectedFile() {
				return fileTextInput;
			}

			@Override
			protected Combo getComboEditors() {
				return comboEditorInputSelector;
			}

			@Override
			protected Text getTextFieldForTextInput() {
				return txtInputText;
			}

			@Override
			protected IEditorReference getSelectedEditor() {
				return getCurrentlySelectedEditor();
			}

			@Override
			protected void setUIState(TextInputWithSource content, boolean b) {
				setTextInputUIState(content, b);
			}

			@Override
			protected TextInputWithSource getInitialTextObject() {
				return initTextObject;
			}

			@Override
			protected List<IEditorReference> getEditorsNotNecessarilyFresh() {
				return editorRefs;
			}

			@Override
			protected AbstractUIInput<String> getTextOnlyInput() {
				return textOnlyInput;
			}
		};

		btnJcteditor.setEnabled(editorRefs.size() > 0);
		btnDatei.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnDatei.getSelection()) {
					if (fileTextInput == null) fileTextInput = openFileSelectionDialogue();
					textInput.synchronizeWithUserSide();
					setTextInputUIState(textInput.getContent(), true);
				}
			}
		});
		btnJcteditor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnJcteditor.getSelection()) {
					textInput.synchronizeWithUserSide();
					setTextInputUIState(textInput.getContent(), true);
				}
			}
		});
		btnZwischenablageeigeneEingabe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnZwischenablageeigeneEingabe.getSelection()) {
					textInput.synchronizeWithUserSide();
					setTextInputUIState(textInput.getContent(), true);
				}
			}
		});
		comboEditorInputSelector.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textInput.synchronizeWithUserSide();
				setTextInputUIState(textInput.getContent(), true);
			}
		});
		linkChangeFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileTextInput = openFileSelectionDialogue();
				textInput.synchronizeWithUserSide();
				setTextInputUIState(textInput.getContent(), true);
			}
		});
	}

	/**
	 * Assumes that the selection index is not "-1" -> something is actually
	 * selected
	 *
	 * @return
	 */
	protected IEditorReference getCurrentlySelectedEditor() {
		int index = comboEditorInputSelector.getSelectionIndex();
		return editorRefs.get(index);
	}

	private void setText(TextInputWithSource initFirstTabText) {
		if (isPageBuilt()) {
			textInput.writeContent(initFirstTabText);
			textInput.synchronizeWithUserSide();
		} else {
			initTextObject = initFirstTabText;
		}
	}

	protected void setTextInputUIState(TextInputWithSource text, boolean writeText) {
		TextSourceType sourceType = text.getSourceType();
		String textString = text.getText();
		btnDatei.setSelection(sourceType.equals(TextSourceType.FILE));
		btnJcteditor.setSelection(sourceType.equals(TextSourceType.JCTEDITOR));
		btnZwischenablageeigeneEingabe.setSelection(sourceType.equals(TextSourceType.USERINPUT));
		if (writeText) {
			if (textInput != null) {
				textOnlyInput.writeContent(textString);
				textOnlyInput.synchronizeWithUserSide();
			} else {
				txtInputText.setText(textString);
			}
		}
	
		showLoadFromEditorComponents(sourceType.equals(TextSourceType.JCTEDITOR));
		if (sourceType.equals(TextSourceType.JCTEDITOR)) {
			//TODO:
			comboEditorInputSelector.select(editorRefs.indexOf(text.editorReference));
		}
		if (sourceType.equals(TextSourceType.FILE)) {
			showLoadFromFileComponents(sourceType.equals(TextSourceType.FILE), text.file.getName());
			// fileTextInput is somewhat the representation of the file
			// selection wizard UI, so this fits here
			fileTextInput = text.file;
		} else {
			showLoadFromFileComponents(sourceType.equals(TextSourceType.FILE), ""); //$NON-NLS-1$
		}
	
		txtInputText.setEditable(sourceType.equals(TextSourceType.USERINPUT));
	}

	/**
	 * Opens a modal dialogue to select a file from the file system. If the file
	 * selection is cancelled, null is returned.
	 *
	 * @return the file, or null at dialogue cancel
	 */
	protected File openFileSelectionDialogue() {
		FileDialog fd = new FileDialog(btnDatei.getShell(), SWT.OPEN);
		fd.setText(Messages.TranspTextWizardPage_windowsfiledialogtitle);
		fd.setFilterPath(null);
		String[] filterExt = { "*.*" }; //$NON-NLS-1$
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		if (selected == null) return null;
		return new File(selected);
	}

	public boolean isPageBuilt() {
		return isPageBuilt;
	}

	private void showLoadFromFileComponents(boolean b, String fileName) {
		if (b) {
			GridData ldata = (GridData) compFileInputDetails.getLayoutData();
			ldata.exclude = !b;
			compFileInputDetails.setVisible(b);
			lblFilenametxt.setText(fileName);
			container.layout(new Control[] { lblFilenametxt, linkChangeFile, compFileInputDetails });
		} else {
			GridData ldata = (GridData) compFileInputDetails.getLayoutData();
			ldata.exclude = !b;
			compFileInputDetails.setVisible(b);
			container.layout(new Control[] { lblFilenametxt, linkChangeFile, compFileInputDetails });
		}
	}

	private void showLoadFromEditorComponents(boolean b) {
		if (b) {
			GridData ldata = (GridData) comboEditorInputSelector.getLayoutData();
			ldata.exclude = !b;
			comboEditorInputSelector.setVisible(b);
			container.layout(new Control[] { comboEditorInputSelector });
		} else {
			GridData ldata = (GridData) comboEditorInputSelector.getLayoutData();
			ldata.exclude = !b;
			comboEditorInputSelector.setVisible(b);
			container.layout(new Control[] { comboEditorInputSelector });
		}
	}

	public void setPageConfiguration(TextInputWithSource config) {
			setText(config);
	}
	
	public TextInputWithSource getPageConfiguration() {
		return textInput.getContent();
	}

}
